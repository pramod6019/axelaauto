package axela.accounting;
//JEET 26th NOV, 2014

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Payment_Print_PDF extends Connect {

	public String voucher_id = "0";
	public String comp_id = "0";
	public String voucherclass_id = "15";
	public String vouchertype_id = "0";

	public String StrSql = "";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "";
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 10);
	PdfWriter writer;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_payment_access", request, response);
			if (!comp_id.equals("0")) {
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				formatdigit_id = GetSession("formatdigit_id", request);
				config_format_decimal = GetSession("config_format_decimal", request);
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));

				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				if (ReturnPerm(comp_id, "emp_payment_access", request).equals("1")) {
					PaymentDetails(request, response, voucher_id, BranchAccess, ExeAccess, "pdf");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PaymentDetails(HttpServletRequest request, HttpServletResponse response, String payment_id, String BranchAccess, String ExeAccess, String purpose) throws IOException,
			DocumentException {
		try {
			StrSql = "SELECT voucher_id, voucher_date, vouchertype_id, voucherclass_id, voucher_amount, voucher_customer_id, voucher_consignee_add, voucher_billing_add, "
					+ " voucher_narration, voucher_notes,"
					+ " vouchertrans_cheque_bank, vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date, vouchertype_label, vouchertrans_paymode_id,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " branch_add, branch_pin, branch_phone1, branch_mobile1, branch_email1, branch_invoice_name,"
					+ "  comp_logo, comp_name,"
					+ " customer_name, customer_address, customer_pin,"
					+ " CONCAT(contact_fname,' ', contact_lname) AS contact_name, title_desc,"
					+ " COALESCE(branchcity.city_name,'') as city_name,"
					+ " COALESCE(branchstate.state_name,'')  as state_name,COALESCE(customercity.city_name,'')  as cust_city,"
					+ " COALESCE( customerstate.state_name ,'') as cust_state,"
					// + " COALESCE( branchcountry.country_name ,'') as branch_country,"
					// + " COALESCE( customercountry.country_name ,'') as cust_country,"
					+ " emp_name, emp_phone1, emp_mobile1, emp_email1, voucher_terms, paymode_name, jobtitle_desc"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id  AND voucherclass_id =" + voucherclass_id
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					// + " INNER JOIN  " + compdb(comp_id) + "axela_country customercountry ON customercountry.country_id = customerstate.state_country_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_city branchcity  ON  branchcity.city_id = branch_city_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					// + " INNER JOIN  " + compdb(comp_id) + "axela_country branchcountry ON branchcountry.country_id = branchstate.state_country_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE voucher_id = " + voucher_id + BranchAccess + ExeAccess
					+ " AND vouchertype_id = " + vouchertype_id
					+ " GROUP BY voucher_id"
					+ " ORDER BY voucher_id DESC";

			// SOP("StrSql===" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				String msg = "";
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					writer = PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "Payment_" + voucher_id + ".pdf"));
				}
				document.open();

				while (crs.next()) {
					po_id = CNumeric(unescapehtml(crs.getString("voucher_id")));
					msg = "\nPaying a sum of crs. " + (int) crs.getDouble("voucher_amount") + "/- (Rupees "
							// + toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount")))
							+ " Only/-) By ";
					msg += crs.getString("paymode_name");
					if (crs.getString("vouchertrans_paymode_id").equals("3")) {
						msg += " vide cheque number " + crs.getString("vouchertrans_cheque_no") + " dated " + strToShortDate(crs.getString("vouchertrans_cheque_date")) + " drawn on "
								+ crs.getString("vouchertrans_cheque_bank") + "";
					}
					if (!po_id.equals("0")) {
						msg += " towards PO ID " + crs.getString("voucher_id") + " dated " + strToShortDate(crs.getString("voucher_date")) + ".\n\n\n";
					} else {
						msg += " towards " + config_customer_name + " ID " + crs.getString("voucher_customer_id") + " dated " + ConvertShortDateToStr(crs.getString("voucher_date") + ".\n\n\n");
					}
					PdfPTable top_table = new PdfPTable(2);
					top_table.setWidthPercentage(100);
					PdfPCell cell;

					cell = new PdfPCell();
					if (!crs.getString("comp_logo").equals("")) {
						// Image comp_logo = Image.getInstance(CompLogoPath() + crs.getString("comp_logo"));
						// cell.addElement(new Chunk(comp_logo, 0, 0));
						// cell.setFixedHeight(comp_logo.getHeight());
					} else {
						cell.addElement(new Phrase(""));
					}
					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("vouchertype_label"), header_font));
					cell.setBackgroundColor(new BaseColor(120, 200, 50));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					document.add(top_table);

					PdfPTable table = new PdfPTable(2);
					table.setWidthPercentage(100);
					String contact = "", comp_name = "";
					comp_name += "M/s. " + unescapehtml(crs.getString("branch_invoice_name"));
					contact += crs.getString("branch_add") + ",";
					contact += "\n" + crs.getString("city_name") + " - " + crs.getString("branch_pin") + ",\n";
					contact += crs.getString("state_name") + ".";
					if (!crs.getString("branch_phone1").equals("")) {
						contact += "\n" + crs.getString("branch_phone1");
					}
					if (!crs.getString("branch_mobile1").equals("")) {
						contact += "\n" + crs.getString("branch_mobile1");
					}
					cell = new PdfPCell();
					cell.addElement(new Phrase(comp_name, bold_font));
					cell.addElement(new Phrase(contact, normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setRowspan(3);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase("Date: " + strToShortDate(crs.getString("voucher_date")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Payment ID: " + crs.getString("voucher_id"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Payment No.: " + crs.getString("voucher_no"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("customer_name")
							+ "\n" + crs.getString("customer_address")
							+ "\n" + crs.getString("cust_city")
							+ "\n" + crs.getString("cust_state") + ".", normal_font));
					cell.setRowspan(2);
					if (po_id.equals("0")) {
						cell.setColspan(2);
					}
					table.addCell(cell);
					if (!po_id.equals("0")) {
						cell = new PdfPCell(new Phrase("PO Date: " + strToShortDate(crs.getString("voucher_date")), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setFixedHeight(35);
						table.addCell(cell);

						cell = new PdfPCell(new Phrase("PO ID: " + crs.getString("voucher_id"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setFixedHeight(35);
						table.addCell(cell);
					} else {
						table.addCell(" ");
					}
					document.add(table);

					PdfPTable total_table = new PdfPTable(2);
					total_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase(unescapehtml(msg), normal_font));
					cell.setColspan(2);
					total_table.addCell(cell);

					String comp = "";
					comp = "For M/s. " + crs.getString("branch_invoice_name");
					comp += "\n" + crs.getString("emp_name");
					comp += "\n" + "";
					comp += "\n" + "";
					comp += "\n" + "";
					comp += "\n" + crs.getString("jobtitle_desc");
					if (!crs.getString("emp_phone1").equals("")) {
						comp += "\n" + crs.getString("emp_phone1");
					}

					if (!crs.getString("emp_mobile1").equals("")) {
						comp += "\n" + crs.getString("emp_mobile1");
					}

					if (!crs.getString("emp_email1").equals("")) {
						comp += "\n" + crs.getString("emp_email1");
					}
					cell = new PdfPCell(new Phrase(unescapehtml(comp), normal_font));
					total_table.addCell(cell);

					cell = new PdfPCell(new Phrase(""));
					total_table.addCell(cell);

					document.add(total_table);
				}
				document.close();
				crs.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Payment!");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
