package axela.sales;
//aJIt 11th January, 2013
//SATISH 23-FEB-2013

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Veh_Quote_Print_PDF extends Connect {

	public String quote_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess;
	public String ExeAccess;
	public String total_disc = "";
	public String grandtotal = "";
	PdfPTable item_table;
	PdfPTable config_table;
	PdfPTable disc_table;
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 9, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 9);
	public String quoteitem_rowcount = "0", quote_exprice = "0";
	public String item_taxcal = "0";
	public int count1 = 0;
	public String quoteitem_option_group = "";
	public String group_name = "";
	public double quote_grandtotal = 0.0;
	public double quote_netamt = 0.0;
	public double quoteitem_price = 0.0;
	public double quote_before_ex_disc = 0.0;
	public double quoteitem_totalprice = 0.0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				if (ReturnPerm(comp_id, "emp_sales_quote_access", request).equals("1")) {
					QuoteDetails(comp_id, request, response, quote_id, BranchAccess, ExeAccess, "pdf");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void QuoteDetails(String comp_id, HttpServletRequest request, HttpServletResponse response, String quote_id, String BranchAccess, String ExeAccess, String purpose) throws IOException,
			DocumentException {
		try {
			SOP("coming----");
			StrSql = "SELECT quote_date, quote_id, CONCAT('QT', branch_code, quote_no) AS quote_no,"
					+ " quote_discamt, comp_name, branch_add, branch_pin, branch_code, branch_city.city_name,"
					+ " branch_state.state_name, branch_phone1, branch_mobile1, branch_email1, branch_logo,"
					+ " quote_fin_option1, quote_fin_loan1, quote_fin_tenure1, quote_fin_adv_emi1,"
					+ " quote_fin_emi1, quote_fin_fee1, quote_fin_downpayment1, quote_fin_baloonemi1,"
					+ " quote_fin_baloonemi2, quote_fin_baloonemi3, quote_fin_option2, quote_fin_loan2,"
					+ " quote_fin_tenure2, quote_fin_adv_emi2, quote_fin_emi2, quote_fin_fee2,"
					+ " quote_fin_downpayment2, quote_fin_option3, quote_fin_loan3, quote_fin_tenure3,"
					+ " quote_fin_adv_emi3, quote_fin_emi3, quote_fin_fee3, quote_fin_downpayment3,"
					+ " comp_name, quote_desc, quote_exprice, quote_netamt, customer_address, jobtitle_desc,"
					+ " COALESCE(customer_city.city_name,'') AS acc_city, customer_pin, COALESCE(customer_state.state_name,'') AS acc_state,"
					+ " quote_ship_address, quote_ship_city, quote_ship_pin, quote_ship_state, quote_terms,"
					+ " quote_grandtotal, branch_invoice_name, customer_id, customer_name, exe.emp_name as emp_name,"
					+ " COALESCE(exe.emp_phone1,'') as emp_phone1, COALESCE(exe.emp_mobile1,'') as emp_mobile1, COALESCE(exe.emp_email1,'') as emp_email1, enquiry_id, CONCAT('ENQ', branch_code, enquiry_no) AS enquiry_no,"
					+ " jobtitle_id,\n"
					+ " COALESCE(teamlead.emp_name,'') as tl_emp_name, COALESCE(teamlead.emp_mobile1,'') as tl_emp_mobile1, COALESCE(teamlead.emp_email1,'') as tl_emp_email1\n"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city branch_city ON branch_city.city_id = branch_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city customer_city ON customer_city.city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state branch_state ON branch_state.state_id = branch_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state customer_state ON customer_state.state_id = customer_city.city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exe ON exe.emp_id = quote_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = exe.emp_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id\n"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp teamlead on teamlead.emp_id = team_emp_id, "
					// + compdb(comp_id) + "axela_comp\n"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE quote_id = " + quote_id + BranchAccess + ExeAccess.replace("emp_id", "exe.emp_id") + ""
					+ " GROUP BY quote_id"
					+ " ORDER BY quote_id DESC";
			// SOP("StrSql==QUOTE DETAILS=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Document document = new Document();
				document.setMargins(20, 20, 20, 20);
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "Quote_" + quote_id + ".pdf"));
				}
				document.open();
				while (crs.next()) {
					quote_exprice = crs.getString("quote_exprice");
					total_disc = crs.getString("quote_discamt");
					quote_grandtotal = crs.getDouble("quote_grandtotal");
					quote_netamt = crs.getDouble("quote_netamt");
					grandtotal = crs.getString("quote_grandtotal");

					PdfPTable top_table = new PdfPTable(2);
					top_table.setWidthPercentage(100);
					top_table.setKeepTogether(true);

					PdfPCell cell;
					cell = new PdfPCell();
					if (AppRun().equals("1")) {
						if (!crs.getString("branch_logo").equals("")) {
							Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo"));
							cell.addElement(new Chunk(branch_logo, 0, 0));
							cell.setFixedHeight(branch_logo.getHeight());
						} else {
							cell.addElement(new Phrase(""));
						}
						cell.setPaddingLeft(0);
						cell.setBorderWidthLeft(0);
						cell.setBorderWidthRight(0);
						cell.setBorderWidthTop(0);
						cell.setBorderWidthBottom(0.1f);
						cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
						top_table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("PRO FORMA INVOICE", header_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setBorderWidthLeft(0);
					cell.setBorderWidthRight(0);
					cell.setBorderWidthTop(0);
					cell.setBorderWidthBottom(0.1f);
					top_table.addCell(cell);

					document.add(top_table);

					PdfPTable table = new PdfPTable(2);
					table.setWidthPercentage(100);

					String contact = "", comp_name = "";

					cell = new PdfPCell();
					cell.addElement(new Phrase("Customer: " + crs.getString("customer_name"), normal_font));
					cell.addElement(new Phrase(unescapehtml(crs.getString("customer_address")
							+ ", " + crs.getString("acc_city")
							+ "-" + crs.getString("customer_pin")
							+ ", " + crs.getString("acc_state")
							+ "."), normal_font));
					cell.addElement(new Phrase(unescapehtml(contact), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setRowspan(3);
					table.addCell(cell);

					cell = new PdfPCell(new Phrase("Date: " + strToShortDate(crs.getString("quote_date")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setFixedHeight(30);
					table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Quote ID: " + crs.getString("quote_id"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setFixedHeight(30);
					table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase("Enquiry ID: " + crs.getString("enquiry_id"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cell.setFixedHeight(30);
					table.addCell(cell);

					comp_name += "M/s. " + crs.getString("branch_invoice_name");
					contact += crs.getString("branch_add") + ",";
					contact += "\n" + crs.getString("city_name") + " - " + crs.getString("branch_pin") + ",\n";
					contact += crs.getString("state_name") + ".";
					if (!crs.getString("branch_phone1").equals("")) {
						contact += "\n" + crs.getString("branch_phone1");
					}
					if (!crs.getString("branch_mobile1").equals("")) {
						contact += "\n" + crs.getString("branch_mobile1");
					}
					//
					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml(comp_name), bold_font));
					cell.addElement(new Phrase(unescapehtml(contact), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					table.addCell(cell);

					cell = new PdfPCell();
					cell.addElement(new Phrase(unescapehtml("Customer ID: " + crs.getString("customer_id")), normal_font));
					cell.setRowspan(2);
					table.addCell(cell);

					document.add(table);

					ItemDetails(comp_id, quote_id);
					document.add(item_table);

					PdfPTable total_table = new PdfPTable(2);
					total_table.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase(unescapehtml("Amount Chargeable (in words):\n"
							+ "Rupees " + toTitleCase(NumberToWordFormat((int) crs.getDouble("quote_grandtotal")))
							+ " Only/-.\n" + " "), normal_font));
					cell.setColspan(2);
					total_table.addCell(cell);

					String finance_data = "";
					String quote_fin_loan1 = "", quote_fin_loan2 = "", quote_fin_loan3 = "";
					String quote_fin_tenure1 = "", quote_fin_tenure2 = "", quote_fin_tenure3 = "";
					String quote_fin_adv_emi1 = "", quote_fin_adv_emi2 = "", quote_fin_adv_emi3 = "";
					String quote_fin_emi1 = "", quote_fin_emi2 = "", quote_fin_emi3 = "";
					String quote_fin_baloonemi1 = "", quote_fin_baloonemi2 = "", quote_fin_baloonemi3 = "";
					String quote_fin_fee1 = "", quote_fin_fee2 = "", quote_fin_fee3 = "";
					String quote_fin_downpayment1 = "", quote_fin_downpayment2 = "", quote_fin_downpayment3 = "";

					quote_fin_loan1 = crs.getString("quote_fin_loan1");
					if (!quote_fin_loan1.equals("")) {
						quote_fin_loan1 = IndFormat(quote_fin_loan1);
					}

					quote_fin_loan2 = crs.getString("quote_fin_loan2");
					if (!quote_fin_loan2.equals("")) {
						quote_fin_loan2 = IndFormat(quote_fin_loan2);
					}

					quote_fin_loan3 = crs.getString("quote_fin_loan3");
					if (!quote_fin_loan3.equals("")) {
						quote_fin_loan3 = IndFormat(quote_fin_loan3);
					}

					quote_fin_tenure1 = crs.getString("quote_fin_tenure1");
					if (!quote_fin_tenure1.equals("")) {
						quote_fin_tenure1 = IndFormat(quote_fin_tenure1);
					}

					quote_fin_tenure2 = crs.getString("quote_fin_tenure2");
					if (!quote_fin_tenure2.equals("")) {
						quote_fin_tenure2 = IndFormat(quote_fin_tenure2);
					}

					quote_fin_tenure3 = crs.getString("quote_fin_tenure3");
					if (!quote_fin_tenure3.equals("")) {
						quote_fin_tenure3 = IndFormat(quote_fin_tenure3);
					}

					quote_fin_adv_emi1 = crs.getString("quote_fin_adv_emi1");
					if (!quote_fin_adv_emi1.equals("")) {
						quote_fin_adv_emi1 = IndFormat(quote_fin_adv_emi1);
					}

					quote_fin_adv_emi2 = crs.getString("quote_fin_adv_emi2");
					if (!quote_fin_adv_emi2.equals("")) {
						quote_fin_adv_emi2 = IndFormat(quote_fin_adv_emi2);
					}

					quote_fin_adv_emi3 = crs.getString("quote_fin_adv_emi3");
					if (!quote_fin_adv_emi3.equals("")) {
						quote_fin_adv_emi3 = IndFormat(quote_fin_adv_emi3);
					}

					quote_fin_emi1 = crs.getString("quote_fin_emi1");
					if (!quote_fin_emi1.equals("")) {
						quote_fin_emi1 = IndFormat(quote_fin_emi1);
					}

					quote_fin_emi2 = crs.getString("quote_fin_emi2");
					if (!quote_fin_emi2.equals("")) {
						quote_fin_emi2 = IndFormat(quote_fin_emi2);
					}

					quote_fin_emi3 = crs.getString("quote_fin_emi3");
					if (!quote_fin_emi3.equals("")) {
						quote_fin_emi3 = IndFormat(quote_fin_emi3);
					}
					//
					quote_fin_baloonemi1 = crs.getString("quote_fin_baloonemi1");
					if (!quote_fin_baloonemi1.equals("")) {
						quote_fin_baloonemi1 = IndFormat(quote_fin_baloonemi1);
					}

					quote_fin_baloonemi2 = crs.getString("quote_fin_baloonemi2");
					if (!quote_fin_baloonemi2.equals("")) {
						quote_fin_baloonemi2 = IndFormat(quote_fin_baloonemi2);
					}

					quote_fin_baloonemi3 = crs.getString("quote_fin_baloonemi3");
					if (!quote_fin_baloonemi3.equals("")) {
						quote_fin_baloonemi3 = IndFormat(quote_fin_baloonemi3);
					}

					quote_fin_fee1 = crs.getString("quote_fin_fee1");
					if (!quote_fin_fee1.equals("")) {
						quote_fin_fee1 = IndFormat(quote_fin_fee1);
					}

					quote_fin_fee2 = crs.getString("quote_fin_fee2");
					if (!quote_fin_fee2.equals("")) {
						quote_fin_fee2 = IndFormat(quote_fin_fee2);
					}

					quote_fin_fee3 = crs.getString("quote_fin_fee3");
					if (!quote_fin_fee3.equals("")) {
						quote_fin_fee3 = IndFormat(quote_fin_fee3);
					}

					quote_fin_downpayment1 = crs.getString("quote_fin_downpayment1");
					if (!quote_fin_downpayment1.equals("")) {
						quote_fin_downpayment1 = IndFormat(quote_fin_downpayment1);
					}

					quote_fin_downpayment2 = crs.getString("quote_fin_downpayment2");
					if (!quote_fin_downpayment2.equals("")) {
						quote_fin_downpayment2 = IndFormat(quote_fin_downpayment2);
					}

					quote_fin_downpayment3 = crs.getString("quote_fin_downpayment3");
					if (!quote_fin_downpayment3.equals("")) {
						quote_fin_downpayment3 = IndFormat(quote_fin_downpayment3);
					}

					PdfPTable finance_table = new PdfPTable(4);
					cell = new PdfPCell(new Phrase("Finance Proposal", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(4);
					finance_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Finance Options", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					finance_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("quote_fin_option1"), bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					finance_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("quote_fin_option2"), bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					finance_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("quote_fin_option3"), bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					finance_table.addCell(cell);

					if (!quote_fin_loan1.equals("")) {
						finance_data = "data";
						cell = new PdfPCell(new Phrase("Loan Amount: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_loan1, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_loan2, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_loan3, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);
					}

					if (!quote_fin_tenure1.equals("")) {
						finance_data = "data";
						cell = new PdfPCell(new Phrase("Tenure: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_tenure1, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_tenure2, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_tenure3, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);
					}

					if (!quote_fin_adv_emi1.equals("")) {
						finance_data = "data";
						cell = new PdfPCell(new Phrase("No. of Advance E.M.I.: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_adv_emi1, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_adv_emi2, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_adv_emi3, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);
					}

					if (!quote_fin_emi1.equals("")) {
						finance_data = "data";
						cell = new PdfPCell(new Phrase("E.M.I./Rental: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_emi1, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_emi2, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_emi3, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);
					}

					if (!quote_fin_baloonemi1.equals("")) {
						finance_data = "data";
						cell = new PdfPCell(new Phrase("Baloon E.M.I./Bullet E.M.I.: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_baloonemi1, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_baloonemi2, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_baloonemi3, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);
					}

					if (!quote_fin_fee1.equals("")) {
						finance_data = "data";
						cell = new PdfPCell(new Phrase("Processing Fee: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_fee1, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_fee2, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_fee3, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);
					}

					if (!quote_fin_downpayment1.equals("")) {
						finance_data = "data";
						cell = new PdfPCell(new Phrase("Net Booking Amount with/Without Optional Packages & Accessories: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_downpayment1, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_downpayment2, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);

						cell = new PdfPCell(new Phrase(quote_fin_downpayment3, normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						finance_table.addCell(cell);
					}

					if (!finance_data.equals("")) {
						cell = new PdfPCell(finance_table);
						cell.setColspan(2);
						total_table.addCell(cell);
					}

					if (crs.getString("quote_desc").length() > 5) {
						StringReader reader = new StringReader(unescapehtml("<font size=2>" + crs.getString("quote_desc") + "</font>"));
						List arr = (List) HTMLWorker.parseToList(reader, null, null);
						Phrase phrase = new Phrase("");
						for (int i = 0; i < arr.size(); i++) {
							Element element = (Element) arr.get(i);
							phrase.add(element);
						}
						cell = new PdfPCell(phrase);
						cell.setColspan(2);
						total_table.addCell(cell);
					}

					if (crs.getString("quote_terms").length() > 5) {
						// StyleSheet st = new StyleSheet();
						// st.loadTagStyle("body", "leading", "16,0");
						StringReader reader = new StringReader(unescapehtml("<font size=1>" + crs.getString("quote_terms") + "</font>"));
						List arr = (List) HTMLWorker.parseToList(reader, null);
						cell = new PdfPCell();
						for (int i = 0; i < arr.size(); i++) {
							Element element = (Element) arr.get(i);
							cell.addElement(element);
							cell.setColspan(2);
							// cell.setLeading(3f, 1.2f);
						}
						total_table.addCell(cell);
					}
					// document.add(total_table);
					PdfPTable sub_table = new PdfPTable(3);
					sub_table.setWidthPercentage(100);
					sub_table.setWidths(new int[]{30, 30, 40});
					cell = new PdfPCell(new Phrase(unescapehtml("Authorised Customer Signatory:"), normal_font));
					sub_table.addCell(cell);

					contact = "";
					contact += "Team Leader\n";
					contact += crs.getString("tl_emp_name") + "\n";
					if (!crs.getString("tl_emp_mobile1").equals("")) {
						contact += crs.getString("tl_emp_mobile1") + "\n";
					}
					if (!crs.getString("tl_emp_email1").equals("")) {
						contact += crs.getString("tl_emp_email1");
					}
					cell = new PdfPCell(new Phrase(unescapehtml(contact), normal_font));
					cell.setPaddingRight(0);
					sub_table.addCell(cell);

					String comp = "";
					comp = "For M/s. " + crs.getString("branch_invoice_name");
					comp += "\n" + crs.getString("emp_name");
					comp += "\n";
					comp += "\n";
					comp += "\n";
					comp += "\n" + crs.getString("jobtitle_desc");
					if (!crs.getString("emp_phone1").equals("")) {
						comp += "\n" + crs.getString("emp_phone1");
					}

					if (!crs.getString("emp_mobile1").equals("")) {
						comp += "\n" + crs.getString("emp_mobile1");
					}
					cell = new PdfPCell(new Phrase(unescapehtml(comp), normal_font));
					cell.setNoWrap(true);
					// cell.setPaddingLeft(10);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sub_table.addCell(cell);

					document.add(total_table);
					document.add(sub_table);
					// document.add(total_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Quote!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void ItemDetails(String comp_id, String quote_id) throws DocumentException {
		int count = 0;
		int colspan = 0;
		String configured_item = "";
		String main_item_disc = "0";

		if (total_disc.equals("0.00")) {
			item_table = new PdfPTable(3);
			item_table.setWidths(new int[]{2, 50, 6});
		} else {
			item_table = new PdfPTable(3);
			item_table.setWidths(new int[]{2, 50, 6});
		}

		if (total_disc.equals("0.00")) {
			config_table = new PdfPTable(2);
			config_table.setWidths(new int[]{50, 6});

			disc_table = new PdfPTable(2);
			disc_table.setWidths(new int[]{50, 6});
		} else {
			config_table = new PdfPTable(2);
			config_table.setWidths(new int[]{50, 6});

			disc_table = new PdfPTable(2);
			disc_table.setWidths(new int[]{50, 6});
		}

		item_table.setWidthPercentage(100);
		config_table.setWidthPercentage(100);
		disc_table.setWidthPercentage(100);

		PdfPCell cell;
		StrSql = "SELECT item_name, item_code, item_small_desc, item_aftertaxcal, quoteitem_rowcount,"
				+ " quoteitem_qty, uom_shortname, quoteitem_price, quoteitem_disc, quoteitem_total,"
				+ " quoteitem_option_group, quoteitem_option_id, quoteitem_tax, quoteitem_option_group_tax,"
				+ " COALESCE((SELECT GROUP_CONCAT(CONCAT(opt.quoteitem_option_group, ': ', optitem.item_name,"
				+ " IF(optitem.item_code != '', CONCAT(' (', optitem.item_code, ')'), ''),"
				+ " CONCAT(' Qty: ', opt.quoteitem_qty),"
				+ " IF(optitem.item_small_desc != '', CONCAT('\n', optitem.item_small_desc), ''))"
				+ " ORDER BY opt.quoteitem_id ASC SEPARATOR '\n\n')"
				+ " FROM " + compdb(comp_id) + "axela_sales_quote_item opt"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item optitem ON optitem.item_id = opt.quoteitem_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_option ON option_item_id = opt.quoteitem_item_id"
				+ " WHERE opt.quoteitem_quote_id = invitem.quoteitem_quote_id"
				+ " AND opt.quoteitem_option_id = invitem.quoteitem_rowcount), '') AS optionitems"
				+ " FROM " + compdb(comp_id) + "axela_sales_quote_item invitem"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item item ON item.item_id = invitem.quoteitem_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item.item_uom_id"
				+ " WHERE invitem.quoteitem_quote_id = " + quote_id + ""
				+ " GROUP BY quoteitem_id"
				+ " ORDER BY quoteitem_option_group_tax, quoteitem_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			count++;
			int cnt = 0;
			double value = 0.00;
			// String valueDouble="0.0";

			cell = new PdfPCell(new Phrase("#", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase(" ", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Amount", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase(count + "", normal_font));
			cell.setNoWrap(true);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			item_table.addCell(cell);
			while (crs.next()) {
				cnt++;
				if (cnt == 1) {
					value = crs.getDouble("quoteitem_price");
					// valueDouble=df.format(crs.getDouble("quoteitem_price"));
					// SOP("value===" +valueDouble);
				}
				quoteitem_rowcount = crs.getString("quoteitem_rowcount");
				item_taxcal = crs.getString("quoteitem_option_group_tax");
				quoteitem_option_group = crs.getString("quoteitem_option_group");
				quoteitem_totalprice = crs.getDouble("quoteitem_total");
				String item_name = crs.getString("item_name");

				if (!quoteitem_rowcount.equals("0")) {
					if (!crs.getString("item_code").equals("")) {
						item_name += " (" + crs.getString("item_code") + ")";
					}
				}
				item_name = item_name.replace("<br>", "\n");

				// MAIN ITEM
				if (!quoteitem_rowcount.equals("0")) {
					if (!crs.getString("quoteitem_disc").equals("0")) {
						main_item_disc = crs.getString("quoteitem_disc");
					}
					if (!item_name.equals("")) {
						cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
					} else {
						cell = new PdfPCell(new Phrase(" ", normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);

					if (!item_name.equals("")) {
						cell = new PdfPCell(new Phrase(String.valueOf(df.format(value)) + "", normal_font));
					} else {
						cell = new PdfPCell(new Phrase(" ", normal_font));
					}
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);
				} else {
					// GROUP_NAME
					configured_item = quoteitem_option_group;
					if (item_taxcal.equals("1")) {
						if (!crs.getString("quoteitem_option_id").equals("0") && !group_name.equals(quoteitem_option_group)) {
							group_name = quoteitem_option_group;
							if (group_name.equals("Additional Discounts")) {
								cell = new PdfPCell(new Phrase("Consumer Offer", bold_font));
								cell.setColspan(2);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								disc_table.addCell(cell);
							} else {
								cell = new PdfPCell(new Phrase(crs.getString("quoteitem_option_group"), bold_font));
								cell.setColspan(2);
								cell.setHorizontalAlignment(Element.ALIGN_LEFT);
								config_table.addCell(cell);
							}
						}

						if (quoteitem_option_group.equals("Additional Discounts") && crs.getDouble("quoteitem_disc") > 0) {
							if (Double.parseDouble(CNumeric(main_item_disc)) > 0) {
								quote_before_ex_disc = Double.parseDouble(CNumeric(main_item_disc));
								cell = new PdfPCell(new Phrase("Cash Discount", normal_font));
								disc_table.addCell(cell);

								cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(Double.parseDouble(CNumeric(main_item_disc))))), normal_font));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cell.setNoWrap(true);
								disc_table.addCell(cell);
								main_item_disc = "";
							}

							// ITEM_NAME
							if (!item_name.equals("")) {
								quote_before_ex_disc += crs.getDouble("quoteitem_disc");
								// SOP("quote_before_ex_disc===" + quote_before_ex_disc);
								if (!item_name.equals("")) {
									cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
								} else {
									cell = new PdfPCell(new Phrase(" ", normal_font));
								}
								disc_table.addCell(cell);

								// SOP("quoteitem_disc===" + crs.getDouble("quoteitem_disc"));
								cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("quoteitem_disc")))), normal_font));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cell.setNoWrap(true);
								disc_table.addCell(cell);
							} else {
								if (!item_name.equals("")) {
									cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
								} else {
									cell = new PdfPCell(new Phrase(" ", normal_font));
								}
								config_table.addCell(cell);

								cell = new PdfPCell(new Phrase(" ", normal_font));
								cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
								cell.setNoWrap(true);
								config_table.addCell(cell);
							}
						} else {
							// ITEM_NAME
							if (!item_name.equals("")) {
								cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
							} else {
								cell = new PdfPCell(new Phrase(" ", normal_font));
							}
							config_table.addCell(cell);

							cell = new PdfPCell(new Phrase(" ", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							config_table.addCell(cell);
						}
					}

					if (total_disc.equals("0.00")) {
						colspan = 2;
					} else {
						colspan = 2;
					}

					if (item_taxcal.equals("2") && count1 == 0) {
						if (Double.parseDouble(CNumeric(main_item_disc)) > 0) {
							quote_before_ex_disc = Double.parseDouble(CNumeric(main_item_disc));

							cell = new PdfPCell(new Phrase("Consumer Offer", bold_font));
							cell.setColspan(2);
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							disc_table.addCell(cell);

							cell = new PdfPCell(new Phrase("Cash Discount", normal_font));
							disc_table.addCell(cell);

							cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(Double.parseDouble(CNumeric(main_item_disc))))) + ".00", normal_font));
							// SOP("discout==" + String.valueOf(df.format(Double.parseDouble(CNumeric(main_item_disc)))));
							// SOP("IndFormat==" + );
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							disc_table.addCell(cell);
							main_item_disc = "";
						}
						cell = new PdfPCell(new Phrase("Ex-Showroom Price: ", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						if (!item_name.equals("")) {
							cell.setColspan(colspan - 1);
						} else {
							cell.setColspan(colspan - 2);
						}
						config_table.addCell(cell);
						cell = new PdfPCell(new Phrase(String.valueOf(df.format(value)) + "", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						config_table.addCell(cell);

						cell = new PdfPCell(disc_table);
						cell.setColspan(2);
						config_table.addCell(cell);

						if (quote_before_ex_disc > 0) {
							cell = new PdfPCell(new Phrase("Ex-Showroom Price after Consumer Offer: ", bold_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							if (!item_name.equals("")) {
								cell.setColspan(colspan - 1);
							} else {
								cell.setColspan(colspan - 2);
							}
							config_table.addCell(cell);

							// SOP("quote_exprice==item=" + quote_exprice);
							// SOP("new==" + (value - quote_before_ex_disc));
							cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(value - quote_before_ex_disc)) + ".00", bold_font));
							double val = Double.parseDouble(String.valueOf((value - quote_before_ex_disc)));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							cell.setNoWrap(true);
							config_table.addCell(cell);
						}
						count++;
					}

					// AFTER TAX
					if (item_taxcal.equals("2")) {
						count1++;
						cell = new PdfPCell(new Phrase(unescapehtml(item_name), normal_font));
						cell.setNoWrap(true);
						config_table.addCell(cell);

						cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(crs.getDouble("quoteitem_total")))) + ".00", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						cell.setNoWrap(true);
						config_table.addCell(cell);
					}
				}
			}

			crs.close();
			// SOP("count====" + count);
			if (count1 == 0) {
				// SOP("count1====" + count1);
				cell = new PdfPCell(new Phrase(""));
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase("Ex-Showroom Price:", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				// cell.setPaddingRight(60);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(String.valueOf(df.format(value)) + "", bold_font));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				// cell.setPaddingRight(60);
				cell.setNoWrap(true);
				item_table.addCell(cell);

				if (quote_before_ex_disc > 0) {
					cell = new PdfPCell(new Phrase(""));
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Ex-Showroom Price after Consumer Offer: ", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);

					cell = new PdfPCell(new Phrase(IndFormat(quote_exprice), bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setNoWrap(true);
					item_table.addCell(cell);
				}
			}

			if (!configured_item.equals("")) {
				cell = new PdfPCell(new Phrase(""));
				item_table.addCell(cell);

				cell = new PdfPCell(new Phrase(""));
				cell.setPadding(0);
				cell.addElement(config_table);
				cell.setColspan(colspan);
				item_table.addCell(cell);
			}

			if (total_disc.equals("0.00")) {
				colspan = 2;
			} else {
				colspan = 2;
			}

			cell = new PdfPCell(new Phrase(""));
			item_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Grand Total:", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// if (count1 == 0) {
			// cell.setPaddingRight(60);
			// }
			cell.setNoWrap(true);
			item_table.addCell(cell);
			cell = new PdfPCell(new Phrase(IndFormat(String.valueOf(df.format(quote_grandtotal))) + ".00", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cell.setNoWrap(true);
			item_table.addCell(cell);

		} catch (Exception ex) {
			SOP("Zenica===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
