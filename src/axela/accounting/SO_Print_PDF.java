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
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class SO_Print_PDF extends Connect {

	public String voucher_id = "0";
	public String voucherclass_id = "0";
	public String vouchertype_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	// public String company_id = "0";

	public String ExeAccess = "";
	public String comp_id = "0";
	double total_taxamt = 0.00;
	public String total_disc = "";
	public double grand_total = 0.0;
	DecimalFormat deci = new DecimalFormat("0.00");
	public String emp_name = "", emp_id = "";
	PdfPTable item_table;
	Font header_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 8, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 8);
	ItemDetails_Print_PDF bill_obj = new ItemDetails_Print_PDF();
	Paragraph paragraph = null;
	PdfWriter writer;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			CheckPerm(comp_id, "emp_acc_voucher_access," + " emp_acc_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);

				ExeAccess = GetSession("ExeAccess", request);

				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				voucherclass_id = CNumeric(PadQuotes(request
						.getParameter("voucherclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request
						.getParameter("vouchertype_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				SalesOrderDetails(request, response, voucher_id, BranchAccess,
						ExeAccess, "pdf");
			}
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void SalesOrderDetails(HttpServletRequest request,
			HttpServletResponse response, String voucher_id,
			String BranchAccess, String ExeAccess, String purpose)
			throws IOException, DocumentException {

		String tempStr = "";

		try {

			// StrSql = "SELECT  voucher_date, voucher_id, voucher_amount, voucher_customer_id, voucher_consignee_add, voucher_billing_add, "
			// + " voucher_narration, voucher_notes, voucher_entry_id,"
			// + " vouchertrans_cheque_bank, vouchertrans_cheque_no, location_name,"
			// + " vouchertrans_cheque_date, vouchertype_label,"
			// + " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount, vouchertype_turnoverdisc, voucher_ref_no, voucher_custref_no,"
			// + " voucher_no, vouchertype_prefix, voucher_no_prefix,"
			// + " voucher_gatepass, voucher_lrno, voucher_cashdiscount, voucher_turnoverdisc,"
			// + " branch_add, branch_pin, branch_phone1, branch_phone2, branch_mobile1, branch_mobile2, branch_email1, branch_email2, branch_invoice_name, branch_tin_no,"
			// + "  comp_logo, comp_name,"
			// + " customer_name, customer_address, customer_code, customer_pin,customer_name, customer_mobile1, customer_mobile2,"
			// + " customer_phone1, customer_phone2, customer_email1, customer_email2, customer_tin_no, customer_cst_no,"
			// + " CONCAT(contact_fname,' ', contact_lname) AS contact_name, title_desc,"
			// + " COALESCE(branchcity.city_name,'') as city_name,"
			// + " COALESCE(branchstate.state_name,'')  as state_name,COALESCE(customercity.city_name,'')  as cust_city,"
			// + " COALESCE( customerstate.state_name ,'') as cust_state,"
			// + " COALESCE( branchcountry.country_name ,'') as branch_country,"
			// + " COALESCE( customercountry.country_name ,'') as cust_country,"
			// + " voucherlocation_prefix,"
			// + " voucherlocation_suffix,"
			// + " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms, jobtitle_desc, zone_name,"
			// + " company_name"
			// + " FROM " + compdb(comp_id) + "axela_acc_voucher"
			// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_zone ON zone_id = customer_zone_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity  ON  branchcity.city_id = branch_city_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_country branchcountry ON branchcountry.country_id = branchstate.state_country_id"
			// + " LEFT JOIN " + compdb(comp_id) + "axela_country customercountry ON customercountry.country_id = customerstate.state_country_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_class ON voucherclass_id = " + voucherclass_id
			// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_location ON voucherlocation_voucher_type_id = vouchertype_id"
			// + " AND voucherlocation_year_id = voucher_year_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = vouchertrans_location_id"
			// + " AND location_id = voucherlocation_location_id"
			// + " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id "
			// + " INNER JOIN " + compdb(comp_id) + "axela_company ON company_id =" + comp_id + ","
			// + "  axela_comp"
			// // + " WHERE voucher_id = " + voucher_id + BranchAccess +
			// // ExeAccess + ""
			// + " WHERE voucher_id = "
			// + voucher_id
			// + " AND branch_company_id = "
			// + comp_id
			// + " AND vouchertype_id = "
			// + vouchertype_id
			// + " GROUP BY voucher_id" + " ORDER BY voucher_id DESC";

			StrSql = "SELECT"
					+ " voucher_date, voucher_id, voucher_amount, voucher_customer_id,"
					+ " voucher_consignee_add, voucher_billing_add, voucher_narration,"
					+ " voucher_notes, voucher_entry_id, vouchertrans_cheque_bank,"
					+ " vouchertrans_cheque_no, vouchertrans_cheque_date, vouchertype_label,"
					+ " vouchertype_gatepass, vouchertype_lrno, vouchertype_cashdiscount,"
					+ " vouchertype_turnoverdisc, voucher_ref_no, voucher_custref_no,"
					+ " voucher_no, vouchertype_prefix, vouchertype_suffix,"
					+ " voucher_gatepass, voucher_lrno, voucher_cashdiscount,"
					+ " voucher_turnoverdisc, branch_add, branch_vat, branch_pin,"
					+ " branch_phone1, branch_phone2, branch_mobile1, branch_mobile2,"
					+ " branch_email1, branch_email2, branch_invoice_name, comp_logo,"
					+ " comp_name, customer_name, customer_pan_no, customer_address, customer_code,"
					+ " customer_pin, customer_name, customer_mobile1, customer_mobile2,"
					+ " customer_phone1, customer_phone2, customer_email1, customer_email2,"
					+ " CONCAT( contact_fname, ' ', contact_lname ) AS contact_name,"
					+ " title_desc, COALESCE (branchcity.city_name, '') AS city_name,"
					+ " COALESCE (branchstate.state_name, '') AS state_name,"
					+ " COALESCE (customercity.city_name, '') AS cust_city,"
					+ " COALESCE ( customerstate.state_name, '' ) AS cust_state,"
					+ " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms,"
					+ " jobtitle_desc, comp_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = " + voucherclass_id
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_voucherclass_id = voucherclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id
					+ " WHERE"
					+ " voucher_id = " + voucher_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " GROUP BY"
					+ " voucher_id"
					+ " ORDER BY"
					+ " voucher_id DESC";

			// SOP("StrSql===so print==" + StrSqlBreaker(StrSql));
			CachedRowSet rs = processQuery(StrSql, 0);
			if (rs.isBeforeFirst()) {
				Document document = new Document();
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					// response.setHeader("Content-Disposition",
					// "Attachment; filename=\"invoice.pdf\"");
					writer = PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath());
					if (!f.exists()) {
						f.mkdirs();
					}
					writer = PdfWriter.getInstance(document, new FileOutputStream(
							CachePath() + "Invoice_" + voucher_id + ".pdf"));
				}
				document.open();
				while (rs.next()) {
					PdfPTable top_table = new PdfPTable(2);
					top_table.setWidthPercentage(100);
					PdfPCell cell;
					cell = new PdfPCell(new Phrase(""));
					if (!rs.getString("comp_logo").equals("")) {
						// Image comp_logo = Image.getInstance(CompLogoPath() +
						// unescapehtml(rs.getString("comp_logo")));
						// cell.addElement(new Chunk(comp_logo, 0, 0));
						// cell.setFixedHeight(comp_logo.getHeight());
					} else {
						cell.addElement(new Phrase(""));
					}
					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(rs.getString("vouchertype_label") + " "), header_font));
					cell.setBackgroundColor(new BaseColor(120, 200, 50));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);
					String contact = "", comp_name = "";
					comp_name += rs.getString("comp_name");
					contact += rs.getString("branch_add") + ",";
					contact += "\n" + rs.getString("city_name") + " - ";
					contact += rs.getString("branch_pin") + ",\n";
					contact += rs.getString("state_name") + ",\n";
					// + rs.getString("branch_country") + ".";
					if (!rs.getString("branch_phone1").equals("")) {
						contact += "\n" + rs.getString("branch_phone1");
						if (!rs.getString("branch_phone2").equals("")) {
							contact += ", " + rs.getString("branch_phone2");
						}
					}

					if (!rs.getString("branch_mobile1").equals("")) {
						contact += "\n" + rs.getString("branch_mobile1");
						if (!rs.getString("branch_mobile2").equals("")) {
							contact += ", " + rs.getString("branch_mobile2");
						}
					}

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(comp_name),
							bold_font));
					cell.addElement(new Phrase(unescapehtml(contact),
							normal_font));

					if (!rs.getString("branch_vat").equals("")) {
						paragraph = new Paragraph();
						paragraph.add(new Phrase("TIN No.: ", bold_font));
						paragraph.add(new Phrase(rs.getString("branch_vat"),
								normal_font));
						cell.addElement(new Phrase(paragraph));
					}

					// cell.addElement(new Phrase("CST No.: "
					// + rs.getString("branch_cst_no"), bold_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setRowspan(3);
					top_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase("Date: "
									+ strToShortDate(unescapehtml(rs
											.getString("voucher_date"))),
									normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					top_table.addCell(cell);
					String voucher_no = ""
							// +rs.getString("voucherlocation_prefix")
							+ rs.getString("vouchertype_prefix") + rs.getString("voucher_no") + rs.getString("vouchertype_suffix");
					cell = new PdfPCell(new Phrase(
							unescapehtml(rs.getString("vouchertype_label"))
									+ " No.: "
									+ unescapehtml(voucher_no),
							normal_font));

					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					top_table.addCell(cell);
					// Reference No

					paragraph = new Paragraph();
					paragraph.add(new Phrase("Ref. No.: ", normal_font));
					paragraph.add(new Phrase(unescapehtml(rs.getString("voucher_ref_no")),
							normal_font));
					int x = 35;
					if (vouchertype_id.equals("114") || vouchertype_id.equals("102")) {
						paragraph.add(new Phrase("\nPurchase Order No.: ", normal_font));
						paragraph.add(new Phrase(unescapehtml(rs.getString("voucher_custref_no")),
								normal_font));
						x = 40;
					}

					cell.addElement(new Phrase(paragraph));

					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(x);
					top_table.addCell(cell);
					cell = new PdfPCell();

					// Billing Address
					if (!rs.getString("customer_code").equals("")) {
						paragraph = new Paragraph();
						paragraph.add(new Phrase("Code: ", bold_font));
						paragraph.add(new Phrase(rs.getString("customer_code"),
								normal_font));
						cell.addElement(new Phrase(paragraph));
					}

					cell.addElement(new Phrase("Customer : ", bold_font));
					if (!rs.getString("customer_name").equals("")) {
						cell.addElement(new Phrase(unescapehtml(rs
								.getString("customer_name")) + ".",
								normal_font));
					}

					if (!rs.getString("voucher_billing_add").equals("")) {
						cell.addElement(new Phrase(unescapehtml(rs
								.getString("voucher_billing_add")) + ".",
								normal_font));
					}
					// Mobile
					if (!rs.getString("customer_mobile1").equals("")) {
						tempStr = "Mobile No.: "
								+ rs.getString("customer_mobile1");
						if (!rs.getString("customer_mobile2").equals("")) {
							tempStr += ", " + rs.getString("customer_mobile2");
						}
						cell.addElement(new Phrase(tempStr, normal_font));
					}
					// Phone
					if (!rs.getString("customer_phone1").equals("")) {
						tempStr = "Phone No.: "
								+ rs.getString("customer_phone1");
						if (!rs.getString("customer_phone2").equals("")) {
							tempStr += ", " + rs.getString("customer_mobile2");
						}
						cell.addElement(new Phrase(tempStr, normal_font));
					}
					// Email
					if (!rs.getString("customer_email1").equals("")) {
						tempStr = "Email ID: "
								+ rs.getString("customer_email1");
						if (!rs.getString("customer_email2").equals("")) {
							tempStr += ", " + rs.getString("customer_email2");
						}
						cell.addElement(new Phrase(tempStr, normal_font));
					}
					paragraph = new Paragraph();
					// paragraph.add(new Phrase("TIN No.: ", bold_font));
					// paragraph.add(new Phrase(rs.getString("customer_tin_no"), normal_font));
					paragraph.add(new Phrase("PAN No.: ", bold_font));
					paragraph.add(new Phrase(rs.getString("customer_pan_no"), normal_font));
					cell.addElement(new Phrase(paragraph));

					paragraph = new Paragraph();
					// paragraph.add(new Phrase("CST No.: ", bold_font));
					// paragraph.add(new Phrase(rs.getString("customer_cst_no"), normal_font));
					cell.addElement(new Phrase(paragraph));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					top_table.addCell(cell);
					cell = new PdfPCell();

					if (vouchertype_id.equals("102")) {
						if (!rs.getString("voucher_consignee_add").equals("")) {
							cell.addElement(new Phrase(
									"Delivery Instructions: ", bold_font));
							cell.addElement(new Phrase(unescapehtml(rs
									.getString("voucher_consignee_add")) + ".",
									normal_font));
						}
					}
					if (vouchertype_id.equals("114") || vouchertype_id.equals("101")) {

						if (!rs.getString("voucher_consignee_add").equals("")) {
							cell.addElement(new Phrase(
									"Delivery Instructions: ", bold_font));
							cell.addElement(new Phrase(unescapehtml(rs
									.getString("voucher_consignee_add")) + ".",
									normal_font));
						}

						paragraph = new Paragraph();
						paragraph.add(new Phrase("Remark: ", bold_font));
						if (!rs.getString("voucher_entry_id").equals("0")) {
							paragraph
									.add(new Phrase(
											unescapehtml(ExecuteQuery("SELECT emp_name FROM axela_emp WHERE emp_id="
													+ rs.getString("voucher_entry_id"))),
											normal_font));
						}
						cell.addElement(new Phrase(paragraph));
					}

					if (vouchertype_id.equals("115")
							|| vouchertype_id.equals("1")
							|| vouchertype_id.equals("4")
							|| vouchertype_id.equals("3")) {

						if (vouchertype_id.equals("4")) {
							paragraph = new Paragraph();
							paragraph.add(new Phrase("Gate Pass: ", bold_font));
							if (!rs.getString("voucher_gatepass").equals("")) {
								paragraph.add(new Phrase(unescapehtml(rs
										.getString("voucher_gatepass")),
										normal_font));
							}
							cell.addElement(new Phrase(paragraph));
						}

						paragraph = new Paragraph();
						paragraph.add(new Phrase("Lr. No.: ", bold_font));
						if (!rs.getString("voucher_lrno").equals("")) {
							paragraph.add(new Phrase(unescapehtml(rs
									.getString("voucher_lrno")), normal_font));
						}
						cell.addElement(new Phrase(paragraph));

						if (vouchertype_id.equals("4")) {
							cell.addElement(new Phrase("Destination: ",
									bold_font));
							cell.addElement(new Phrase(unescapehtml(rs
									.getString("voucher_consignee_add")),
									normal_font));
						}
					}
					if (vouchertype_id.equals("115")) {

						paragraph = new Paragraph();
						paragraph.add(new Phrase("T.O.D.: ", bold_font));
						if (!rs.getString("voucher_turnoverdisc").equals("")) {
							paragraph.add(new Phrase(unescapehtml(rs
									.getString("voucher_turnoverdisc")),
									normal_font));
						}
						cell.addElement(new Phrase(paragraph));

						if (!vouchertype_id.equals("3")) {
							paragraph = new Paragraph();
							paragraph.add(new Phrase("C.D.: ", bold_font));
							if (!rs.getString("voucher_cashdiscount")
									.equals("")) {
								paragraph.add(new Phrase(unescapehtml(rs
										.getString("voucher_cashdiscount")),
										normal_font));
							}
							cell.addElement(new Phrase(paragraph));
						}
					}

					if (vouchertype_id.equals("114")
							|| vouchertype_id.equals("3") || vouchertype_id.equals("114")) {
						paragraph = new Paragraph();
						paragraph.add(new Phrase("Godown: ", bold_font));
						if (!rs.getString("location_name").equals("")) {
							paragraph.add(new Phrase(unescapehtml(rs
									.getString("location_name")), normal_font));
						}
						cell.addElement(new Phrase(paragraph));

					}
					if (vouchertype_id.equals("102")) {
						paragraph = new Paragraph();
						paragraph.add(new Phrase("Zone: ", bold_font));
						if (!rs.getString("zone_name").equals("")) {
							paragraph.add(new Phrase(unescapehtml(rs.getString("zone_name")), normal_font));
						}
						cell.addElement(new Phrase(paragraph));
					}
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					top_table.addCell(cell);
					// END

					document.add(top_table);
					SOP("aaa");

					// Item Details
					item_table = bill_obj.ItemDetails(voucher_id, vouchertype_id, request);
					SOP("bbb");
					grand_total = bill_obj.grand_total;
					document.add(item_table);
					PdfPTable total_table = new PdfPTable(2);
					total_table.setWidthPercentage(100);
					if (vouchertype_id.equals("102")
							|| vouchertype_id.equals("114")
							|| vouchertype_id.equals("3")
							|| vouchertype_id.equals("101")) {
						cell = new PdfPCell(
								new Phrase("Amount Chargeable (in words): " + GetSession("currency_name", request)
										+ " "
										+ toTitleCase(IndianCurrencyFormatToWord((((long) Math
												.round(grand_total)))))
										+ " Only/-.\n" + " ",
										normal_font));
						cell.setColspan(2);
						total_table.addCell(cell);
					}
					SOP("ccc");
					if (rs.getString("voucher_terms").length() > 0) {
						StringReader reader = new StringReader(
								unescapehtml("<font size=2>"
										+ rs.getString("voucher_terms")
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
					// String comp = "";
					// comp = "For M/s. "
					// + unescapehtml(rs.getString("company_name"));
					// comp += "\n" + "";
					// comp += "\n" + "";
					//
					// comp += "\n" + "";
					// comp += "\n" + "";
					// comp += "\n" + "Authorized Signatory";
					// cell = new PdfPCell(new Phrase(unescapehtml(comp),
					// normal_font));
					// total_table.addCell(cell);
					// cell = new PdfPCell(new Phrase(""));
					// total_table.addCell(cell);
					// document.add(total_table);
					SOP("ddd");
					if (!vouchertype_id.equals("102")) {
						StrSql = "SELECT emp_name"
								+ " FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_id = " + emp_id
								+ " AND emp_active = '1'";
						emp_name = ExecuteQuery(StrSql);
						emp_name = emp_name.toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
						PdfContentByte cb = writer.getDirectContent();
						ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(unescapehtml("         Printed By: " + emp_name), normal_font), 0, document.bottom() - 2, 0);
					}
					SOP("eee");
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Purchase Order!");
			}
			rs.close();
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
}