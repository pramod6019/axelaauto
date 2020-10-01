package axela.accounting;
//BISHESH 25th JULY, 2016

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

public class Journal_Print_PDF extends Connect {

	public String voucher_id = "0";
	public String vouchertype_id = "0";
	public String voucherclass_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String comp_id = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String po_id = "0";
	public String config_customer_name = "";
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 10);
	PdfWriter writer;
	public String emp_name = "", emp_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_journal_access", request, response);
			if (!comp_id.equals("0")) {
				config_customer_name = GetSession("config_customer_name", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				emp_id = CNumeric(GetSession("emp_id", request));

				if (ReturnPerm(comp_id, "emp_acc_journal_access", request).equals("1")) {
					JournalDetails(request, response, voucher_id, BranchAccess, ExeAccess, "pdf");
				}
			}
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void JournalDetails(HttpServletRequest request, HttpServletResponse response, String payment_id, String BranchAccess, String ExeAccess, String purpose) throws IOException,
			DocumentException {
		try {

			StrSql = "SELECT"
					+ " voucher_id, voucher_date, voucher_amount,"
					+ " CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no,"
					+ " voucher_narration,"
					+ " vouchertype_label,"
					+ " vouchertrans_amount, vouchertrans_dc,"
					+ " (SELECT customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE customer_id = vouchertrans_customer_id) AS customer_name,"
					+ " branch_add, branch_pin, branch_phone1, branch_mobile1, branch_email1, branch_invoice_name,"
					+ " COALESCE(branchcity.city_name,'') as city_name,"
					+ " COALESCE(branchstate.state_name,'')  as state_name,"
					// + " COALESCE( branchcountry.country_name ,'') as branch_country,"
					+ " comp_name,"
					+ " comp_logo"
					+ " company_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branchcity  ON  branchcity.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state branchstate ON branchstate.state_id = branchcity.city_state_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_country branchcountry ON branchcountry.country_id = branchstate.state_country_id, " + compdb(comp_id) + "axela_comp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_comp ON comp_id =" + comp_id
					+ " WHERE 1=1"
					+ " AND voucher_id =" + voucher_id
					// + " AND branch_company_id =" + comp_id
					+ " ORDER BY voucher_id";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			PdfPTable top_table = new PdfPTable(2);
			top_table.setWidthPercentage(100);
			PdfPCell cell;

			PdfPTable table = new PdfPTable(2);
			table.setWidthPercentage(100);

			PdfPTable total_table = new PdfPTable(3);
			int[] columnWidths = new int[]{15, 5, 5};
			total_table.setWidths(columnWidths);
			total_table.setWidthPercentage(100);

			PdfPTable bottom_table = new PdfPTable(4);
			columnWidths = new int[]{5, 5, 5, 5};
			bottom_table.setWidths(columnWidths);
			bottom_table.setWidthPercentage(100);

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					writer = PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath());
					if (!f.exists()) {
						f.mkdirs();
					}
					writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath() + "Journal_" + voucher_id + ".pdf"));
				}
				document.open();

				if (crs.next()) {
					cell = new PdfPCell();
					if (!crs.getString("comp_logo").equals("")) {
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
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("vouchertype_label").toUpperCase() + " VOUCHER"), header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					top_table.addCell(cell);

					document.add(top_table);

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
					cell.setRowspan(3);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Date: " + strToShortDate(unescapehtml(crs.getString("voucher_date"))), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					table.addCell(cell);
					cell = new PdfPCell(new Phrase("Vch. No.: " + unescapehtml(crs.getString("voucher_no")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setFixedHeight(35);
					table.addCell(cell);

					document.add(table);

					cell = new PdfPCell(new Phrase("Particulars", bold_font));
					cell.setPadding(3);
					cell.setBorderWidthRight(0);
					total_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Debit Amt", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBorderWidthRight(0);
					cell.setPadding(3);
					total_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Credit Amt", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setPadding(3);
					total_table.addCell(cell);
				}
				crs.beforeFirst();
				while (crs.next()) {
					if (crs.getString("vouchertrans_dc").equals("1")) {
						cell = new PdfPCell(new Phrase(crs.getString("customer_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setBorderWidthRight(0);
						cell.setBorderWidthTop(0);
						cell.setPadding(3);
						total_table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("vouchertrans_amount"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setBorderWidthRight(0);
						cell.setBorderWidthTop(0);
						cell.setPadding(3);
						total_table.addCell(cell);

						cell = new PdfPCell(new Phrase(" ", normal_font));
						cell.setBorderWidthTop(0);
						total_table.addCell(cell);

					}

					if (crs.getString("vouchertrans_dc").equals("0")) {
						cell = new PdfPCell(new Phrase(crs.getString("customer_name"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setBorderWidthRight(0);
						cell.setBorderWidthTop(0);
						cell.setPadding(3);
						total_table.addCell(cell);

						cell = new PdfPCell(new Phrase(" ", normal_font));
						cell.setBorderWidthRight(0);
						cell.setBorderWidthTop(0);
						total_table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("vouchertrans_amount"), normal_font));
						cell.setVerticalAlignment(Element.ALIGN_CENTER);
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setBorderWidthTop(0);
						cell.setPadding(3);
						total_table.addCell(cell);
					}
				}
				crs.beforeFirst();
				if (crs.next()) {
					cell = new PdfPCell(new Phrase("TOTAL" + " ", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBorderWidthRight(0);
					cell.setBorderWidthTop(0);
					cell.setPadding(3);
					total_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("voucher_amount"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBorderWidthRight(0);
					cell.setBorderWidthTop(0);
					cell.setPadding(3);
					total_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("voucher_amount"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBorderWidthTop(0);
					cell.setPadding(3);
					total_table.addCell(cell);

					cell.addElement(new Phrase("Narration : "
							+ "\n" + crs.getString("voucher_narration"), normal_font));
					cell.setColspan(3);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorderWidthBottom(0);
					cell.setPadding(3);
					total_table.addCell(cell);
					document.add(total_table);
				}

				cell = new PdfPCell(new Phrase("\n\nChecked By", normal_font));
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorderWidthRight(0);
				bottom_table.addCell(cell);

				cell = new PdfPCell(new Phrase("\n\nVerified By", normal_font));
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorderWidthRight(0);
				cell.setBorderWidthLeft(0);
				bottom_table.addCell(cell);

				cell = new PdfPCell(new Phrase("\n\nApproved By", normal_font));
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorderWidthRight(0);
				cell.setBorderWidthLeft(0);
				bottom_table.addCell(cell);

				cell = new PdfPCell(new Phrase("\n\nSignature", normal_font));
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setBorderWidthLeft(0);
				bottom_table.addCell(cell);
				document.add(bottom_table);
				StrSql = "SELECT emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id
						+ " AND emp_active = '1'";
				emp_name = ExecuteQuery(StrSql);
				emp_name = emp_name.toUpperCase() + ", on " + strToLongDate(ToLongDate(kknow())) + "";
				PdfContentByte cb = writer.getDirectContent();
				ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase(unescapehtml("Printed By: " + emp_name), normal_font), 0, document.bottom() - 2, 0);
				document.close();
				crs.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Receipt!");
			}
		} catch (Exception ex) {
			System.out.println("AxA Pro===" + this.getClass().getName());
			System.out.println("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
