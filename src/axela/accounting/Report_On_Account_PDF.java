package axela.accounting;

//JEET 26th NOV, 2014 

//import axela.sales.Pdf_Table;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Report_On_Account_PDF extends Connect {

	public String voucher_id = "0";
	public String voucherclass_id = "0";
	public String vouchertype_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String StrJoin = "";
	public String StrHTML = "";
	public String BranchAccess = "";
	public String comp_id = "0";

	public String ExeAccess = "";
	public String city_id = "";
	public String zone_id = "0";
	public String customer_id = "";
	double total_taxamt = 0.00;
	public String total_disc = "";
	public double grand_total = 0.0;
	DecimalFormat deci = new DecimalFormat("0.00");
	PdfPTable top_table;
	PdfPTable table_header;
	PdfPTable document_header;
	PdfPCell cell;
	Font header_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 8, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 8);
	Paragraph paragraph = null;
	int count = 0;
	DecimalFormat df = new DecimalFormat("0.00");

	// public String recordperpage= "10";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_voucher_access,"
					+ " emp_acc_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				customer_id = PadQuotes(request.getParameter("ledger"));
				city_id = PadQuotes(request.getParameter("maincity"));
				zone_id = CNumeric(PadQuotes(request.getParameter("dr_zone_id")));
				ReceivablesDetails(request, response, voucher_id, BranchAccess,
						ExeAccess, "pdf");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void ReceivablesDetails(HttpServletRequest request,
			HttpServletResponse response, String voucher_id,
			String BranchAccess, String ExeAccess, String purpose)
			throws IOException, DocumentException {
		String tempStr = "";
		String custid = "0";
		int TotalRecords = 0;
		String CountSql = "";
		try {
			StrSql = "select voucher_id, voucher_date, voucher_customer_id,"
					+ " concat(vouchertype_prefix,voucher_no,vouchertype_suffix) as voucher_no, voucher_ref_no, "
					+ " @invamt:=voucher_amount as voucher_amount, "
					+ " @adjmt:=(select"
					+ " coalesce(sum(voucherbal_amount),0)"
					+ " from axela_acc_voucher_bal where voucherbal_voucher_id = voucher_id) as adjmt, "
					+ " (voucher_amount -"
					+ " coalesce((select sum(voucherbal_amount)"
					+ " FROM  " + compdb(comp_id) + "axela_acc_voucher_bal where voucherbal_voucher_id = voucher_id),0)) as netdue, "
					+ " vouchertype_name,"
					+ " customer_name, customer_code, customer_paydays_id";

			StrJoin = " FROM  " + compdb(comp_id) + "axela_acc_voucher "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on customer_id = voucher_customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
					+ " where 1=1 "
					+ " AND branch_company_id = " + comp_id
					+ " and vouchertype_id in (109, 105, 106, 107, 116) "
					+ " and voucher_active = 1 "
					+ " and voucher_customer_id = " + customer_id;
			StrSearch = " group by voucher_id "
					+ " order by voucher_date, voucher_id";

			StrSql += StrJoin + StrSearch;

			CachedRowSet crs = processQuery(StrSql, 0);
			CachedRowSet rscopy = crs.createCopy();
			CachedRowSet rscopy1 = crs.createCopy();

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
			document_header = new PdfPTable(9);
			document_header.setWidthPercentage(100);

			table_header = new PdfPTable(9);
			table_header.setWidthPercentage(100);

			top_table = new PdfPTable(9);
			top_table.setWidthPercentage(100);

			GetDocHeader(document_header);

			GetTableHeader(table_header);

			int count = 0;
			int count_filter = 0;
			String zonename = "";
			if (crs.isBeforeFirst()) {
				crs.first();

				cell = new PdfPCell(new Phrase(crs
						.getString("customer_name"), bold_font));
				cell.setColspan(4);
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBackgroundColor(new BaseColor(120, 200, 50));
				cell.setBorderWidthLeft(0.25f);
				// cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Code: "
						+ crs.getString("customer_code"), bold_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBackgroundColor(new BaseColor(120, 200, 50));
				// cell.setBorderWidthLeft(0.25f);
				// cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				cell.setColspan(3);
				top_table.addCell(cell);
				cell = new PdfPCell(new Phrase("Credit Days: "
						+ crs.getString("customer_paydays_id"),
						bold_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBackgroundColor(new BaseColor(120, 200, 50));
				// cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				cell.setColspan(3);
				top_table.addCell(cell);
				crs.beforeFirst();

				customerTrans(rscopy);
				customerTransSummary(rscopy1);

				document.add(document_header);
				document.add(table_header);
				document.add(top_table);

			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Receivables!");
			}

			document.close();
			crs.close();
			rscopy.close();
			rscopy1.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void customerTrans(CachedRowSet crs) {
		StringBuilder Str = new StringBuilder();
		count = 0;
		try {
			while (crs.next()) {
				cell = new PdfPCell(new Phrase(++count + "", normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

				cell = new PdfPCell(new Phrase(crs.getString("voucher_id"),
						normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

				cell = new PdfPCell(new Phrase(strToShortDate(crs
						.getString("voucher_date")), normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

				cell = new PdfPCell(new Phrase(crs.getString("voucher_no"),
						normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);
				//
				cell = new PdfPCell(new Phrase(crs
						.getString("vouchertype_name"), normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

				cell = new PdfPCell(new Phrase(crs
						.getString("voucher_ref_no"), normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

				cell = new PdfPCell(new Phrase(deci.format(crs
						.getDouble("voucher_amount")), normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

				cell = new PdfPCell(new Phrase(df.format(crs
						.getDouble("adjmt"))
						+ "",
						normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

				cell = new PdfPCell(new Phrase(df.format(crs
						.getDouble("netdue"))
						+ "",
						normal_font));
				cell.setBorderWidth(0);
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
				cell.setBorderWidthLeft(0.25f);
				cell.setBorderWidthRight(0.25f);
				cell.setBorderWidthTop(0.25f);
				cell.setBorderWidthBottom(0.25f);
				top_table.addCell(cell);

			}
			crs.first();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void customerTransSummary(CachedRowSet crs) {
		double invtotal = 0.00, recetotal = 0.00, netduetotal = 0.00, baltotal = 0.00, adjtotal = 0.00;
		StringBuilder Str = new StringBuilder();
		try {
			while (crs.next()) {
				invtotal += crs.getDouble("voucher_amount");
				adjtotal += crs.getDouble("adjmt");
				baltotal += crs.getDouble("netdue");
			}
			crs.first();
			cell = new PdfPCell(new Phrase("Total: ", bold_font));
			cell.setBorderWidth(0);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			cell.setBorderWidthLeft(0.25f);
			cell.setBorderWidthRight(0.25f);
			cell.setBorderWidthTop(0.25f);
			cell.setBorderWidthBottom(0.25f);
			cell.setColspan(8);
			top_table.addCell(cell);

			cell = new PdfPCell(new Phrase(df.format(baltotal) + "", bold_font));
			cell.setBorderWidth(0);
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
			cell.setBorderWidthLeft(0.25f);
			cell.setBorderWidthRight(0.25f);
			cell.setBorderWidthTop(0.25f);
			cell.setBorderWidthBottom(0.25f);
			top_table.addCell(cell);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void GetDocHeader(PdfPTable top_table) {

		cell = new PdfPCell(new Phrase("MAHESH HARDWARE & PIPES PVT. LTD.",
				header_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(0);
		cell.setColspan(9);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"On Account Details (Un-Allocated Vouchers)", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(0);
		cell.setColspan(9);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("", normal_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(0);
		cell.setColspan(9);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("As on " + DateToShortDate(kknow()),
				normal_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorder(0);
		cell.setColspan(9);
		top_table.addCell(cell);
	}

	public void GetTableHeader(PdfPTable top_table) {
		cell = new PdfPCell(new Phrase("#", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("ID", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Date", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("No.", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Voucher", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Ref. No.", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Amount", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Adj. Amount", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Balance", bold_font));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setBorderWidthLeft(0.25f);
		cell.setBorderWidthRight(0.25f);
		cell.setBorderWidthTop(0.25f);
		cell.setBorderWidthBottom(0.25f);
		top_table.addCell(cell);
	}
}