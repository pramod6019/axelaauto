package axela.accounting;

//JEET 26th NOV, 2014 

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PO_Print_PDF extends Connect {

	public String voucher_id = "0";
	public String voucherclass_id = "0";
	public String vouchertype_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String comp_id = "0";

	public String ExeAccess = "";
	double total_taxamt = 0.00;
	public String total_disc = "";
	public double grand_total = 0.0;
	DecimalFormat deci = new DecimalFormat("0.00");
	PdfPTable item_table;
	Font header_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 8, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 8);
	ItemDetails_Print_PDF bill_obj = new ItemDetails_Print_PDF();
	Paragraph paragraph = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = GetSession("comp_id", request);
			CheckPerm(comp_id, "emp_acc_voucher_access," + " emp_acc_purchase_order_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);

				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				voucherclass_id = CNumeric(PadQuotes(request
						.getParameter("voucherclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request
						.getParameter("vouchertype_id")));
				// if (ReturnPerm(comp_id, "emp_purchase_order_access", request).equals("1")) {
				PurchaseOrderDetails(request, response, voucher_id, BranchAccess,
						ExeAccess, "pdf");
				// }
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void PurchaseOrderDetails(HttpServletRequest request,
			HttpServletResponse response, String voucher_id,
			String BranchAccess, String ExeAccess, String purpose)
			throws IOException, DocumentException {
		String tempStr = "";
		try {

			StrSql = "SELECT  voucher_date, voucher_id, voucher_amount, voucher_customer_id, voucher_consignee_add, voucher_billing_add, "
					+ " voucher_narration, voucher_notes,"
					+ " vouchertrans_cheque_bank, vouchertrans_cheque_no, location_name,"
					+ " vouchertrans_cheque_date, vouchertype_label,"
					+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount, vouchertype_turnoverdisc, voucher_ref_no,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " voucher_gatepass, voucher_lrno, voucher_cashdiscount, voucher_turnoverdisc,"
					+ " branch_add, branch_pin, branch_phone1, branch_phone2, branch_mobile1, branch_mobile2, branch_email1, branch_email2, branch_invoice_name, "
					// + "branch_tin_no,"
					+ "  comp_logo, comp_name,"
					+ " customer_name, customer_address, customer_pin,customer_name, customer_mobile1, customer_mobile2,"
					+ " customer_phone1, customer_phone2, customer_email1, customer_email2, "
					// + "customer_tin_no, customer_cst_no,"
					+ " CONCAT(contact_fname,' ', contact_lname) AS contact_name, title_desc,"
					+ " COALESCE(branchcity.city_name,'') as city_name,"
					+ " COALESCE(branchstate.state_name,'')  as state_name,COALESCE(customercity.city_name,'')  as cust_city,"
					+ " COALESCE( customerstate.state_name ,'') as cust_state,"
					// + " COALESCE( branchcountry.country_name ,'') as branch_country,"
					// + " COALESCE( customercountry.country_name ,'') as cust_country,"
					+ " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms, jobtitle_desc"
					// + "zone_name"

					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					// + " LEFT JOIN  " + compdb(comp_id) + "axela_zone ON zone_id = customer_zone_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity  ON  branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					// + " LEFT JOIN axela_country branchcountry ON branchcountry.country_id = branchstate.state_country_id"
					// + " LEFT JOIN axela_country customercountry ON customercountry.country_id = customerstate.state_country_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN  axela_acc_voucher_class ON voucherclass_id = "
					+ voucherclass_id
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans_location_id "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id ,"
					+ "  " + compdb(comp_id) + "axela_comp"
					// + " WHERE voucher_id = " + voucher_id + BranchAccess +
					// ExeAccess + ""
					+ " WHERE voucher_id = "
					+ voucher_id
					+ " AND vouchertype_id = "
					+ vouchertype_id
					+ " GROUP BY voucher_id" + " ORDER BY voucher_id DESC";
			// SOP("StrSql===po print==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Document document = new Document();
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					PdfWriter.getInstance(document, new FileOutputStream(
							CachePath(comp_id) + "Invoice_" + voucher_id + ".pdf"));
				}
				document.open();
				while (crs.next()) {
					PdfPTable top_table = new PdfPTable(2);
					top_table.setWidthPercentage(100);
					PdfPCell cell;
					cell = new PdfPCell(new Phrase(""));

					if (!crs.getString("comp_logo").equals("")) {
						// Image comp_logo = Image.getInstance(CompLogoPath() +
						// unescapehtml(crs.getString("comp_logo")));
						// cell.addElement(new Chunk(comp_logo, 0, 0));
						// cell.setFixedHeight(comp_logo.getHeight());
					} else {
						cell.addElement(new Phrase(""));
					}

					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							unescapehtml(crs.getString("vouchertype_label") + " "),
							header_font));
					cell.setBackgroundColor(new BaseColor(120, 200, 50));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);
					String contact = "", comp_name = "";
					comp_name += "M/s. " + crs.getString("branch_invoice_name");
					contact += crs.getString("branch_add") + ",";
					contact += "\n" + crs.getString("city_name") + " - "
							+ crs.getString("branch_pin") + ",\n";
					contact += crs.getString("state_name") + ",\n";
					// + crs.getString("branch_country") + ".";
					if (!crs.getString("branch_phone1").equals("")) {
						contact += "\n" + crs.getString("branch_phone1");
						if (!crs.getString("branch_phone2").equals("")) {
							contact += ", " + crs.getString("branch_phone2");
						}
					}

					if (!crs.getString("branch_mobile1").equals("")) {
						contact += "\n" + crs.getString("branch_mobile1");
						if (!crs.getString("branch_mobile2").equals("")) {
							contact += ", " + crs.getString("branch_mobile2");
						}
					}
					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(comp_name),
							bold_font));
					cell.addElement(new Phrase(unescapehtml(contact),
							normal_font));
					// if (!crs.getString("branch_tin_no").equals("")) {
					// paragraph = new Paragraph();
					// paragraph.add(new Phrase("TIN No.: ", bold_font));
					// paragraph.add(new Phrase(unescapehtml(crs.getString("branch_tin_no")), normal_font));
					// cell.addElement(new Phrase(paragraph));
					// }
					// cell.addElement(new Phrase("CST No.: "
					// + crs.getString("branch_cst_no"), bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setRowspan(3);
					top_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase("Date: "
									+ strToShortDate(unescapehtml(crs
											.getString("voucher_date"))),
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase(
							unescapehtml(crs.getString("vouchertype_label"))
									+ " No.: "
									+ unescapehtml(crs.getString("voucher_no")),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					top_table.addCell(cell);

					// Reference No
					cell = new PdfPCell(new Phrase("Ref. No.: "
							+ unescapehtml(crs.getString("voucher_ref_no")),
							normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					top_table.addCell(cell);

					// Billing Address
					cell = new PdfPCell();
					cell.addElement(new Phrase("Supplier: ", bold_font));
					if (!crs.getString("voucher_billing_add").equals("")) {
						cell.addElement(new Phrase(unescapehtml(crs
								.getString("voucher_billing_add")) + ".",
								normal_font));
					}
					tempStr = "Mobile No.: " + crs.getString("customer_mobile1");
					if (!crs.getString("customer_mobile2").equals("")) {
						tempStr += ", " + crs.getString("customer_mobile2");
					}
					cell.addElement(new Phrase(tempStr, normal_font));

					tempStr = "Phone No.: " + crs.getString("customer_phone1");
					if (!crs.getString("customer_phone2").equals("")) {
						tempStr += ", " + crs.getString("customer_mobile2");
					}
					cell.addElement(new Phrase(tempStr, normal_font));
					tempStr = "Email ID: " + crs.getString("customer_email1");
					if (!crs.getString("customer_email2").equals("")) {
						tempStr += ", " + crs.getString("customer_email2");
					}
					cell.addElement(new Phrase(tempStr, normal_font));

					// paragraph = new Paragraph();
					// paragraph.add(new Phrase("TIN No.: ", bold_font));
					// paragraph.add(new Phrase(unescapehtml(crs.getString("customer_tin_no")), normal_font));
					// cell.addElement(new Phrase(paragraph));

					// paragraph = new Paragraph();
					// paragraph.add(new Phrase("CST No.: ", bold_font));
					// paragraph.add(new Phrase(unescapehtml(crs.getString("customer_cst_no")), normal_font));
					// cell.addElement(new Phrase(paragraph));

					// if (crs.getString("voucher_consignee_add").equals("")) {
					// cell.setColspan(2);
					// }
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					top_table.addCell(cell);

					// Consignee Address
					// if (!crs.getString("voucher_consignee_add").equals("")) {
					// cell = new PdfPCell();
					// cell.addElement(new Phrase("Consignee Address:",
					// bold_font));
					// cell.addElement(new Phrase(unescapehtml(rs
					// .getString("voucher_consignee_add")) + ".",
					// normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_TOP);
					// top_table.addCell(cell);
					// }
					// START
					cell = new PdfPCell();
					if (vouchertype_id.equals("115") || vouchertype_id.equals("1")
							|| vouchertype_id.equals("2") || vouchertype_id.equals("117")) {
						//

						if (vouchertype_id.equals("2")) {
							paragraph = new Paragraph();
							paragraph.add(new Phrase("Gate Pass: ", bold_font));
							if (!crs.getString("voucher_gatepass").equals("")) {
								paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_gatepass")), normal_font));
							}
							cell.addElement(new Phrase(paragraph));

							// paragraph = new Paragraph();
							// paragraph.add(new Phrase("Gate Pass Date: ",bold_font));
							// if (!crs.getString("voucher_gatepass").equals("")) {
							// paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_gatepass")),normal_font));
							// }
							// cell.addElement(new Phrase(paragraph));
						}
						// if(vouchertype_id.equals("117")){
						// paragraph = new Paragraph();
						// paragraph.add(new Phrase("Received By: ",bold_font));
						// if (!crs.getString("voucher_gatepass").equals("")) {
						// paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_gatepass")),normal_font));
						// }
						// cell.addElement(new Phrase(paragraph));
						// }

						// paragraph = new Paragraph();
						// paragraph.add(new Phrase("Transporter: ",bold_font));
						// if (!crs.getString("voucher_gatepass").equals("")) {
						// paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_gatepass")),normal_font));
						// }
						// cell.addElement(new Phrase(paragraph));

						paragraph = new Paragraph();
						paragraph.add(new Phrase("Lr. No.: ", bold_font));
						if (!crs.getString("voucher_lrno").equals("")) {
							paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_lrno")), normal_font));
						}
						cell.addElement(new Phrase(paragraph));

						if (vouchertype_id.equals("2")) {
							paragraph = new Paragraph();
							paragraph.add(new Phrase("Destination: ", bold_font));
							// if (!crs.getString("voucher_lrno").equals("")) {
							// paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_lrno")),normal_font));
							// }
							cell.addElement(new Phrase(paragraph));

						}
					}
					if (vouchertype_id.equals("115")) {
						paragraph = new Paragraph();
						paragraph.add(new Phrase("T.O.D.: ", bold_font));
						if (!crs.getString("voucher_turnoverdisc").equals("0.0")) {
							paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_turnoverdisc")), normal_font));
						}
						cell.addElement(new Phrase(paragraph));

						paragraph = new Paragraph();
						paragraph.add(new Phrase("C.D.: ", bold_font));
						if (!crs.getString("voucher_cashdiscount").equals("0.0")) {
							paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_cashdiscount")), normal_font));
						}
						cell.addElement(new Phrase(paragraph));
					}

					if (vouchertype_id.equals("10") || vouchertype_id.equals("108")) {
						paragraph = new Paragraph();
						paragraph.add(new Phrase("Godown: ", bold_font));
						if (!crs.getString("location_name").equals("")) {
							paragraph.add(new Phrase(unescapehtml(crs.getString("location_name")), normal_font));
						}
						cell.addElement(new Phrase(paragraph));
					}

					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setFixedHeight(35);
					top_table.addCell(cell);
					// END

					document.add(top_table);

					// Item Details
					item_table = bill_obj.ItemDetails(voucher_id, vouchertype_id, request);
					grand_total = bill_obj.grand_total;
					document.add(item_table);
					PdfPTable total_table = new PdfPTable(2);
					total_table.setWidthPercentage(100);
					if (vouchertype_id.equals("115")) {
						cell = new PdfPCell(
								new Phrase(
										"Amount Chargeable (in words): "
												+ GetSession("currency_name",
														request)
												+ " "
												+ toTitleCase(IndianCurrencyFormatToWord((((long) Math
														.round(grand_total)))))
												+ " Only/-.\n" + " ", normal_font));
						cell.setColspan(2);
						total_table.addCell(cell);
					}
					if (crs.getString("voucher_notes").length() > 0) {
						paragraph = new Paragraph();
						paragraph.add(new Phrase("Narration: ", bold_font));
						paragraph.add(new Phrase(unescapehtml(crs.getString("voucher_notes")), normal_font));
						cell = new PdfPCell(new Phrase(paragraph));
						cell.setColspan(2);
						total_table.addCell(cell);
					}
					if (crs.getString("voucher_terms").length() > 0) {
						StringReader reader = new StringReader(
								unescapehtml("<font size=2>"
										+ crs.getString("voucher_terms")
										+ "</font>"));
						List arr = (List) HTMLWorker.parseToList(reader, null,
								null);
						Phrase phrase = new Phrase("");
						for (int i = 0; i < arr.size(); i++) {
							Element element = (Element) arr.get(i);
							phrase.add(element);
						}
						cell = new PdfPCell(phrase);
						cell.setColspan(2);
						total_table.addCell(cell);
					}

					String comp = "";
					comp = "For M/s. "
							+ unescapehtml(crs.getString("branch_invoice_name"));
					comp += "\n" + "";
					comp += "\n" + "";
					comp += "\n" + "";
					comp += "\n" + "";
					comp += "\n" + "Authorized Signatory";
					cell = new PdfPCell(new Phrase(unescapehtml(comp),
							normal_font));
					total_table.addCell(cell);
					cell = new PdfPCell(new Phrase(""));
					total_table.addCell(cell);
					document.add(total_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Purchase Order!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
}
