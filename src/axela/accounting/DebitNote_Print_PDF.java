package axela.accounting;
//BISHESH 25th JUL, 2016

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
public class DebitNote_Print_PDF extends Connect {

	public String voucher_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "0";
	public String StrSql = "";
	public String comp_id = "";
	public String StrHTML = "";
	public String formatdigit_id = "0";
	public String config_format_decimal = "0";
	DecimalFormat df = new DecimalFormat("0.00");

	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "";
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 9, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 9);
	PdfWriter writer;
	public String emp_name = "", emp_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request) + "");
			CheckPerm(comp_id, "emp_acc_debit_note_access", request, response);
			if (!comp_id.equals("0")) {
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				formatdigit_id = GetSession("formatdigit_id", request);
				config_format_decimal = GetSession("config_format_decimal", request);
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));
				if (ReturnPerm(comp_id, "emp_acc_debit_note_access", request).equals("1")) {
					DebitNoteDetails(request, response, voucher_id, BranchAccess, ExeAccess, "pdf");
				}
			}
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void DebitNoteDetails(HttpServletRequest request, HttpServletResponse response, String payment_id, String BranchAccess, String ExeAccess, String purpose) throws IOException,
			DocumentException {
		try {
			StrSql = "SELECT comp_name, comp_logo, voucher_id,"
					+ " CONCAT(vouchertype_prefix,voucher_no,vouchertype_suffix) AS voucher_no,"
					+ " COALESCE (vouchertype_id, '') AS vouchertype_id,"
					+ " voucherclass_id, vouchertype_name,"
					+ " COALESCE (voucher_amount, '') AS voucher_amount,"
					+ " COALESCE (voucher_billing_add, '') AS voucher_billing_add,"
					+ " COALESCE (voucher_consignee_add, '') AS voucher_consignee_add,"
					+ " COALESCE (voucher_notes, '') AS voucher_notes,"
					+ " COALESCE (voucher_narration, '') AS voucher_narration,"
					+ " voucher_date, vouchertrans_cheque_bank, vouchertrans_cheque_no,"
					+ " vouchertrans_cheque_date, vouchertrans_paymode_id,"
					+ " COALESCE (paymode_name, '') AS paymode_name,"
					+ " CONCAT( branch_name, ' (', branch_code, ')' ) AS branch_name,"
					+ " COALESCE (branch_add, '0') AS branch_add,"
					+ " COALESCE (branch_phone1, '0') AS branch_phone1,"
					+ " COALESCE (branch_email1, '0') AS branch_email1,"
					+ " COALESCE (branch_mobile1, '0') AS branch_mobile1,"
					+ " COALESCE (branch_pin, '0') AS branch_pin,"
					+ " COALESCE (voucher_customer_id, '0') AS voucher_customer_id,"
					+ " COALESCE (customer_id, '0') AS customer_id,"
					+ " COALESCE (customer_name, '') AS customer_name,"
					+ " COALESCE (customer_address, '') AS customer_address,"
					+ " COALESCE (customer_pin, '') AS customer_pin,"
					+ " COALESCE (customer_code, '') AS customer_code,"
					+ " COALESCE (title_desc, '') AS title_desc,"
					+ " COALESCE (contact_fname, '') AS contact_fname,"
					+ " COALESCE (contact_lname, '') AS contact_lname,"
					+ " COALESCE (CONCAT( emp_name, ' (', emp_ref_no, ')' ) ) AS emp_name,"
					+ " COALESCE (emp_photo, '') AS emp_photo,"
					+ " COALESCE (emp_sex, '') AS emp_sex,"
					+ " COALESCE (emp_id, '') AS emp_id,"
					+ " COALESCE (emp_phone1, '') AS emp_phone1,"
					+ " COALESCE (emp_mobile1, '') AS emp_mobile1,COALESCE (emp_email1, '') AS emp_email1,"
					+ " COALESCE (jobtitle_desc, '') AS jobtitle_desc, COALESCE (vouchertype_prefix, '') AS vouchertype_prefix,"
					+ " COALESCE (vouchertype_suffix, '') AS vouchertype_suffix,"
					+ " COALESCE ( IF ( vouchertype_id = 9, ("
					+ " SELECT customer_ledgertype FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
					+ " WHERE"
					+ " vouchertrans_voucher_id = voucher_id"
					+ " AND vouchertrans_dc = 1 LIMIT 1 ),"
					+ " IF ( vouchertype_id = 15, ( SELECT customer_ledgertype"
					+ " FROM "
					+ compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
					+ " WHERE vouchertrans_voucher_id = voucher_id AND vouchertrans_dc = 0 LIMIT 1"
					+ " ), 0 ) ), 0 ) AS paymodeid,"
					+ " COALESCE (branchcity.city_name, '') AS city_name,"
					+ " COALESCE (branchstate.state_name, '') AS state_name,"
					+ " COALESCE (customercity.city_name, '') AS cust_city,"
					+ " COALESCE ( customerstate.state_name, '' ) AS cust_state"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN axelaauto.axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " LEFT JOIN axela_acc_paymode ON paymode_id = vouchertrans_paymode_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city branchcity ON branchcity.city_id = branch_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city customercity ON customercity.city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state customerstate ON customerstate.state_id = customercity.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id = " + comp_id
					+ " WHERE"
					+ " 1 = 1"
					+ " AND voucher_id = " + voucher_id
					+ " AND vouchertype_id = " + vouchertype_id
					+ " AND vouchertype_voucherclass_id = " + voucherclass_id
					+ " GROUP BY"
					+ " voucher_id";

			// SOP("StrSql===" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				String msg = "";
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					writer = PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath());
					if (!f.exists()) {
						f.mkdirs();
					}
					writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath() + "DebitNote_" + voucher_id + ".pdf"));
				}
				document.open();

				while (crs.next()) {

					po_id = CNumeric(crs.getString("voucher_id"));

					msg = "Dear Sir\n";

					msg += "\nWe have debited to your Account Rupees. " + (int) crs.getDouble("voucher_amount") + "/- (Rupees "
							+ toTitleCase(IndianCurrencyFormatToWord((int) crs.getDouble("voucher_amount")))
							+ " Only/-) ";

					if (crs.getString("vouchertrans_paymode_id").equals("2")) {
						msg += " vide cheque number " + crs.getString("vouchertrans_cheque_no") + " dated " + strToShortDate(crs.getString("vouchertrans_cheque_date")) + " drawn on "
								+ crs.getString("vouchertrans_cheque_bank") + "";
					}

					if (!po_id.equals("0")) {
						msg += " towards PO ID " + crs.getString("voucher_id") +
								" dated " + strToShortDate(crs.getString("voucher_date"))
								+ ".\n\n\n";
					} else {
						msg += " towards " + config_customer_name + " ID " +
								crs.getString("voucher_customer_id") + " dated " +
								strToShortDate(crs.getString("voucher_date")) + ".\n\n\n";
					}

					PdfPTable top_table = new PdfPTable(1);
					top_table.setWidthPercentage(100);
					PdfPCell cell;

					cell = new PdfPCell();
					if (!crs.getString("comp_logo").equals("")) {
					} else {
						cell.addElement(new Phrase(""));
					}
					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("vouchertype_name").toUpperCase()), header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					document.add(top_table);

					PdfPTable table = new PdfPTable(2);
					table.setWidthPercentage(100);
					String contact = "", comp_name = "";
					comp_name += crs.getString("comp_name");
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
					cell.addElement(new Phrase(unescapehtml(comp_name), bold_font));
					cell.addElement(new Phrase(unescapehtml(contact), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml("To,\n" + crs.getString("customer_name")
							+ "\n" + crs.getString("customer_address")
							+ "\n" + crs.getString("cust_city")
							+ "\n" + crs.getString("cust_state")) + ".", normal_font));
					cell.setRowspan(2);
					if (po_id.equals("0")) {
						cell.setColspan(2);
					}
					table.addCell(cell);
					document.add(table);

					PdfPTable total_table = new PdfPTable(1);
					total_table.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase(unescapehtml("Vch. No	: " + crs.getString("voucher_no")
							+ "\n Date	: " + strToShortDate(crs.getString("voucher_date"))), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBorderWidthBottom(0);
					total_table.addCell(cell);

					cell = new PdfPCell(new Phrase(unescapehtml(msg), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorderWidthTop(0);
					total_table.addCell(cell);

					cell = new PdfPCell(new Phrase(unescapehtml("Narration :" + crs.getString("voucher_narration")), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorderWidthTop(0);
					total_table.addCell(cell);
					String comp = "";
					comp = "For M/s. " + crs.getString("comp_name");
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
					document.add(total_table);

					emp_name = crs.getString("emp_name") + ", on " + strToLongDate(ToLongDate(kknow())) + "";

					PdfContentByte cb = writer.getDirectContent();
					ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(unescapehtml("Printed By: " + emp_name), normal_font), 0, document.bottom() - 2, 0);
				}
				document.close();
				crs.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid DebitNote!");
			}
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
