package axela.sales;
//sn 14 feb 2013- 17feb 2013(edited)

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

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
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TestDrive_Print_GatePass extends Connect {
	
	public String comp_id = "0";
	public String branch_id = "0";
	public String invoice_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String branch_city_id = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess;
	public String ExeAccess;
	// public String total_disc = "";
	// public String comp_name = "";
	// public String contact = "";
	PdfPTable item_table;
	PdfPTable top_table;
	PdfPTable body_table;
	PdfPTable two_col;
	PdfPTable header_table;
	PdfPTable six_col;
	PdfPTable four_col;
	PdfPTable five_col;
	PdfPTable formtable = new PdfPTable(5);
	Font header_font = FontFactory.getFont("Helvetica", 10, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 8, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 8);
	Font underline = FontFactory.getFont("Helvetica", 9, Font.UNDERLINE);
	// private String no_rec;
	// private int totalcount;
	public File f1 = null;
	public String StrSearch = "";
	public String testdrive_id = "", enquiry_id = "";
	// private int Rectangle;
	public String testdrive_out_time;
	public String emp_id;
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String attachment = "";
	ArrayList<PdfFormField> list = new ArrayList<PdfFormField>();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				invoice_id = CNumeric(PadQuotes(request.getParameter("invoice_id")));
				testdrive_id = PadQuotes(request.getParameter("testdrive_id"));
				testdrive_out_time = PadQuotes(request.getParameter("testdrive_out_time"));
				if (ReturnPerm(comp_id, "emp_testdrive_access", request).equals("1")) {
					if (testdrive_out_time.equals("")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
								+ " SET"
								+ " testdrive_out_time = '" + ToLongDate(kknow()) + "',"
								+ " testdrive_mileage_entry_id = '" + emp_id + "', "
								+ " testdrive_mileage_entry_date = '" + ToLongDate(kknow()) + "' "
								+ " WHERE testdrive_id = " + testdrive_id + " ";
						updateQuery(StrSql);
					}
					// crs.getString("testdrive_in_time").equals(""))
					StrSearch = " AND testdrive_id = " + testdrive_id + "";
					
					InvoiceDetails(request, response, "pdf");
				} else {
					response.sendRedirect(AccessDenied());
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public void InvoiceDetails(HttpServletRequest request, HttpServletResponse response, String purpose) throws IOException, DocumentException {
		// StringBuilder Str = new StringBuilder();
		/*
		 * Added BY Saiman 30th April 2013 Code for saving tha pdf file to cache folder and sending a mail to Admin
		 */
		File pdfFile = new File(CachePath(comp_id) + "gatepass_" + testdrive_id + ".pdf");
		if (pdfFile.exists()) {
			pdfFile.delete();
		}
		
		FileOutputStream file = new FileOutputStream(pdfFile);
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
		// PdfWriter.getInstance(document, file);
		StrSql = "Select config_admin_email, config_email_enable from " + compdb(comp_id) + "axela_config ";
		CachedRowSet crs1 = processQuery(StrSql, 0);
		try {
			while (crs1.next()) {
				config_admin_email = crs1.getString("config_admin_email");
				config_email_enable = crs1.getString("config_email_enable");
			}
			attachment = CachePath(comp_id) + "gatepass_" + testdrive_id + ".pdf,gatepass_" + testdrive_id + ".pdf";
			crs1.close();
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		
		/* end */
		if (purpose.equals("pdf")) {
			response.setContentType("application/pdf");
			PdfWriter.getInstance(document, response.getOutputStream());
		} else if (purpose.equals("file")) {
			File f = new File(CachePath(comp_id));
			if (!f.exists()) {
				f.mkdirs();
			}
			writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "gatepass_" + testdrive_id + ".pdf"));
			f1 = new File(CachePath(comp_id) + "gatepass_" + testdrive_id + ".pdf");
		}
		document.open();
		// int count = 0;
		// / int colspan = 0;
		
		PdfPCell cell;
		StrSql = "SELECT enquiry_id, concat(title_desc,' ',contact_fname,' ', contact_lname) AS contactname,"
				+ " COALESCE(concat(customer_address,', ',city_name,' - ', customer_pin,'.'),'') AS customer_address,"
				+ " customer_phone1, customer_mobile1, testdriveveh_name, location_name,  testdrive_time_from, testdrive_out_time,"
				+ " testdrive_out_kms, testdrive_in_kms, testdrive_license_no, testdrive_license_address, testdrive_license_issued_by,"
				+ " testdrive_license_valid,  concat(testdriveemp.emp_name, ' (',testdriveemp.emp_ref_no,')') as executive,"
				// + " driveremp.emp_name as driver, testdriveveh_regno , comp_logo  "
				+ " driver_name AS driver, testdriveveh_regno, branch_city_id, branch_id, branch_logo  "
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id= testdrive_location_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id   "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id   "
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id   "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id =  testdrive_testdriveveh_id   "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp as testdriveemp ON testdriveemp.emp_id = testdrive_emp_id  "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_driver  ON driver_id = testdrive_out_driver_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_emp as driveremp on driveremp.emp_id= testdrive_out_driver_id"
				+ ", " + compdb(comp_id) + "axela_comp  "
				+ " WHERE  testdrive_out_time!='' " + StrSearch + " " + BranchAccess + ""
				+ " GROUP BY testdrive_id ORDER BY branch_name, customer_name";
		// SOP("query .........s.... " + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_id = crs.getString("enquiry_id");
					branch_id = crs.getString("branch_id");
					branch_city_id = crs.getString("branch_city_id");
					header_table = new PdfPTable(2);
					header_table.setWidthPercentage(100);
					header_table.setKeepTogether(true);
					
					cell = new PdfPCell();
					if (!crs.getString("branch_logo").equals("")) {
						Image branch_logo = Image.getInstance(BranchLogoPath(comp_id) + crs.getString("branch_logo"));
						branch_logo.setAlignment(Image.ALIGN_LEFT);
						branch_logo.setAlignment(Image.ALIGN_TOP);
						
						branch_logo.scaleAbsoluteHeight(1000f);
						branch_logo.scaleAbsoluteWidth(500f);
						cell.addElement(new Chunk(branch_logo, 0, 0));
						cell.setFixedHeight(100f);
						
					} else {
						cell.addElement(new Phrase(""));
					}
					cell.setBorderWidth(0);
					cell.setPaddingLeft(0);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					header_table.addCell(cell);
					cell = new PdfPCell(new Phrase("TEST DRIVE CAR AGREEMENT", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					header_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("", bold_font));
					// cell.setBorderWidth(0);
					// header_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("", bold_font));
					// cell.setBorderWidth(0);
					// header_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("", bold_font));
					// cell.setBorderWidth(0);
					// header_table.addCell(cell);
					document.add(header_table);
					
					item_table = new PdfPTable(2);
					item_table.setWidths(new int[]{4, 12});
					item_table.setWidthPercentage(100);
					item_table.setKeepTogether(true);
					cell = new PdfPCell(new Phrase("Name of the Customer:", bold_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("contactname")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Address:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setBorderWidth(0);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("customer_address")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Mobile:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setBorderWidth(0);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("customer_mobile1")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Tel:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setBorderWidth(0);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("customer_phone1")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Test Drive Vehicle Requested:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setBorderWidth(0);
					item_table.addCell(cell);
					// cell = new PdfPCell(new Phrase(crs.getString("veh_name"), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// item_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdriveveh_name")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Registration Number:", bold_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdriveveh_regno")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					item_table.addCell(cell);
					document.add(item_table);
					
					two_col = new PdfPTable(4);
					// two_col.setWidths(new int[]{ 8, 12});
					two_col.setWidthPercentage(100);
					two_col.setKeepTogether(true);
					cell = new PdfPCell(new Phrase("Date:", bold_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// padding done here.......................................................
					// cell.setPadding(10);
					// cell.setPaddingBottom(5);
					two_col.addCell(cell);
					cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("testdrive_time_from")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					two_col.addCell(cell);
					cell = new PdfPCell(new Phrase("Place of Test Drive:", bold_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					two_col.addCell(cell);
					cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("location_name")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					two_col.addCell(cell);
					cell = new PdfPCell(new Phrase("Time (Commence):", bold_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					two_col.addCell(cell);
					cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("testdrive_out_time")), normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					two_col.addCell(cell);
					cell = new PdfPCell(new Phrase("Time (End):", bold_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					two_col.addCell(cell);
					cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					two_col.addCell(cell);
					document.add(two_col);
					
					top_table = new PdfPTable(1);
					top_table.setWidthPercentage(100);
					top_table.setKeepTogether(true);
					// now
					int yes_checked = 0;
					// PdfPCell cell;
					
					formtable.setWidths(new int[]{10, 3, 1, 4, 1});
					formtable.getDefaultCell().setFixedHeight(10);
					
					cell = new PdfPCell(new Phrase("Checklist for Sales Consultant", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					cell.setColspan(5);
					formtable.addCell(cell);
					// --- * ---
					cell = new PdfPCell(new Phrase("1. Was the test drive given to the customer?", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Yes", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "a", list, 0, 2, 40, 3, "");
					
					cell = new PdfPCell(new Phrase("No", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "b", list, 0, 2, 40, 3, "");
					// --- * ---
					
					cell = new PdfPCell(new Phrase("2. IF yes, then mention the place of the test drive given.", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Dealership", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "c", list, 0, 2, 40, 3, "");
					
					cell = new PdfPCell(new Phrase("Customer Place ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "d", list, 0, 2, 40, 3, "");
					// --- * ---
					
					cell = new PdfPCell(new Phrase("3. Was the test drive given within 3 hours of enquiry?", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Yes", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 40, 3, "");
					
					cell = new PdfPCell(new Phrase("No", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "f", list, 0, 2, 40, 3, "");
					// --- * ---
					
					cell = new PdfPCell(new Phrase("4. If no, mention the reasons for the same", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(3);
					formtable.addCell(cell);
					
					// createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 40, 3, "");
					//
					// cell = new PdfPCell(new Phrase("No", normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setBorder(0);
					// formtable.addCell(cell);
					//
					// createCheckBox(formtable, document, writer, 0, cell, "f", list, 0, 2, 40, 3, "");
					// --- * ---
					
					cell = new PdfPCell(new Phrase("\n ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(5);
					formtable.addCell(cell);
					// --- * ---
					
					cell = new PdfPCell(new Phrase("1) Did you get the test drive on the model of your choice?", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 40, 3, "");
					
					createCheckBox(formtable, document, writer, 0, cell, "f", list, 1, 2, 95, 3, "");
					
					cell = new PdfPCell(new Phrase("   ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					
					// top_table.addCell(formtable);
					// --- * ---
					
					cell = new PdfPCell(new Phrase("2) Was the condition of the vehicle in which you got the test drive was ok?", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 40, 3, "");
					
					createCheckBox(formtable, document, writer, 0, cell, "f", list, 1, 2, 95, 3, "");
					
					cell = new PdfPCell(new Phrase("   ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					// --- * ---
					
					cell = new PdfPCell(new Phrase("3) Was the test drive route ok?", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 40, 3, "");
					
					createCheckBox(formtable, document, writer, 0, cell, "f", list, 1, 2, 95, 3, "");
					
					cell = new PdfPCell(new Phrase("   ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					// top_table.addCell(formtable);
					// --- * ---
					
					cell = new PdfPCell(new Phrase("4) What was the duration of the test drive? (In Minutes)", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 40, 3, "");
					
					createCheckBox(formtable, document, writer, 0, cell, "f", list, 1, 2, 95, 3, "");
					
					cell = new PdfPCell(new Phrase("   ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					// top_table.addCell(formtable);
					// --- * ---
					
					cell = new PdfPCell(new Phrase("5) Was the duration of the test drive sufficient?", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 40, 3, "");
					
					createCheckBox(formtable, document, writer, 0, cell, "f", list, 1, 2, 95, 3, "");
					
					cell = new PdfPCell(new Phrase("   ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					formtable.addCell(cell);
					// --- * ---
					
					cell = new PdfPCell(new Phrase("6) Did you get the test drive at the place and time of your choice?", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					formtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("   ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(3);
					formtable.addCell(cell);
					// top_table.addCell(formtable);
					// --- * ---
					
					cell = new PdfPCell(new Phrase("7) Were the explanation given by the salesman to your satisfaction during the test drive", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					formtable.addCell(cell);
					
					createCheckBox(formtable, document, writer, 0, cell, "e", list, 0, 2, 0, 2, "");
					
					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(3);
					cell.setBorder(0);
					formtable.addCell(cell);
					top_table.addCell(formtable);
					// top_table.addCell(formtable);
					// --- * ---
					
					PdfPTable fdbacktable = new PdfPTable(2);
					cell = new PdfPCell(new Phrase("Overall Feedback/suggestions:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					fdbacktable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n  ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					fdbacktable.addCell(cell);
					top_table.addCell(fdbacktable);
					
					PdfPTable sigtable = new PdfPTable(4);
					cell = new PdfPCell(new Phrase("Customers Signature", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("SM/TL   Signature", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Date:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n\n\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Date:", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n\n\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					sigtable.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(2);
					sigtable.addCell(cell);
					top_table.addCell(sigtable);
					
					PdfPTable newtable = new PdfPTable(1);
					cell = new PdfPCell(new Phrase("Authorization to Test Driving of the Vehicle", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					cell.setRowspan(2);
					newtable.addCell(cell);
					String city_name = ExecuteQuery("SELECT city_name FROM " + compdb(comp_id) + "axela_city WHERE city_id=" + branch_city_id + "");
					String branch_invoice_name = ExecuteQuery("SELECT branch_invoice_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id=" + branch_id + "");
					cell = new PdfPCell(new Phrase("Dear Sir,\n\n"
							+ "            On my request, you have provided me the Car no _____________________________________ (Demo Car) for test driving. I confirm to have undertaken as under:"
							+ "\n1) The Demo Car shall be driven by me only."
							+ "\n2) The ˜Demo Car shall be used for the purpose of test drive only."
							+ "\n3) I shall test drive the Demo Car only in the presence of authorized representative of your Company."
							+ "\n4) I hold a valid and effective during license, a copy of which is being provided herewith."
							+ "\n5) I shall test drive the Demo Car only within the municipal limits of city of " + city_name + "."
							+ "\n6) I shall abide by any all Rules of the Road Regulations while test driving of the Demo Car."
							+ "\n7) While conducting the test drive, I shall not engage in any activity which may be illegal or which may otherwise prejudice to M/s " + branch_invoice_name + "."
							+ "\n\n"
							+ "             In the event of any legal and/or civil consequence and arising from any handling/use of the aforesaid vehicle."
							+ " I undertake to Indemnify M/s " + branch_invoice_name + " against all liabilities. The indemnity shall include but not be limited to police challans,"
							+ " damages to Demo Car and third party property, and any other civil/criminal action that may result from my using the Demo Car."
							+ "\nThanking You"
							+ "\n\nYours faithfully"
							+ "\n\n\n"
							+ "Customers Signature\n\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(4);
					newtable.addCell(cell);
					
					top_table.addCell(newtable);
					document.add(top_table);
					
					for (PdfFormField field : list) {
						writer.addAnnotation(field);
					}
					
					PdfPTable space_table1 = new PdfPTable(1);
					space_table1.setWidthPercentage(100);
					//
					cell = new PdfPCell(new Phrase("\n\n", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					space_table1.addCell(cell);
					document.add(space_table1);
					
					// formtable.setWidths(new int[]{10, 3, 1, 4, 1});
					// formtable.getDefaultCell().setFixedHeight(10);
					PdfPTable five_col1 = new PdfPTable(1);
					five_col1.setWidthPercentage(100);
					five_col1.setKeepTogether(true);
					
					five_col = new PdfPTable(5);
					five_col.setWidthPercentage(100);
					five_col.setKeepTogether(true);
					
					cell = new PdfPCell(new Phrase("TEST DRIVE GATE PASS\n", bold_font));
					cell.setNoWrap(true);
					cell.setBorder(0);
					cell.setColspan(5);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					five_col.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Customer's Name:", bold_font));
					cell.setNoWrap(true);
					cell.setBorder(0);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("_____________________\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("          Time Out:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("_____________________\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Location:", bold_font));
					cell.setNoWrap(true);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("_____________________\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("          Time In:", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("_____________________\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					
					cell = new PdfPCell(new Phrase("DSE Name:", bold_font));
					cell.setNoWrap(true);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("_____________________\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("Signature", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					cell.setBorder(0);
					five_col.addCell(cell);
					
					// cell = new PdfPCell(new Phrase("Date:", bold_font));
					// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					// five_col.addCell(cell);
					// cell = new PdfPCell(new Phrase(strToLongDate(ToLongDate(kknow())), normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// five_col.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Vehicle No.:", bold_font));
					cell.setNoWrap(true);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("_____________________\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					cell.setBorder(0);
					five_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("Date:", bold_font));
					// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					// five_col.addCell(cell);
					// cell = new PdfPCell(new Phrase(strToLongDate(ToLongDate(kknow())), normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// five_col.addCell(cell);
					
					cell = new PdfPCell(new Phrase("Model No.:", bold_font));
					cell.setNoWrap(true);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("_____________________\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("GM/SM (Sales)", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setBorder(0);
					five_col.addCell(cell);
					cell = new PdfPCell(new Phrase("Security Incharge", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					five_col.addCell(cell);
					
					cell = new PdfPCell(new Phrase("\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(5);
					cell.setBorder(0);
					five_col.addCell(cell);
					
					// cell = new PdfPCell(new Phrase("Agreed and Signed:", bold_font));
					// cell.setNoWrap(true);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// five_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("\n ", normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					// five_col.addCell(cell);
					// cell = new PdfPCell(new Phrase(" ", normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					// five_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("Date:", bold_font));
					// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					// five_col.addCell(cell);
					// cell = new PdfPCell(new Phrase(strToLongDate(ToLongDate(kknow())), normal_font));
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// five_col.addCell(cell);
					five_col1.addCell(five_col);
					document.add(five_col1);
					//
					// six_col = new PdfPTable(6);
					// // body_table.setWidths(new int[]{2,2,2,2,3,3});
					// six_col.setWidthPercentage(100);
					// six_col.setKeepTogether(true);
					// cell = new PdfPCell(new Phrase("Driving License#:", bold_font));
					// // cell.setBorderWidth(0);
					// cell.setNoWrap(true);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdrive_license_no")), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("Issued by:", bold_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("testdrive_license_issued_by")), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("Valid till:", bold_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("testdrive_license_valid")), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("Copy of Driving License Enclose:", bold_font));
					// // cell.setBorderWidth(0);
					// cell.setNoWrap(true);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("", bold_font));
					// // cell.setBorderWidth(0);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("", bold_font));
					// // cell.setBorderWidth(0);
					// six_col.addCell(cell);
					// cell = new PdfPCell(new Phrase("", bold_font));
					// // cell.setBorderWidth(0);
					// six_col.addCell(cell);
					// document.add(six_col);
					//
					// body_table = new PdfPTable(4);
					// // body_table.setWidths(new int[]{ 8, 12});
					// body_table.setWidthPercentage(100);
					// body_table.setKeepTogether(true);
					// cell = new PdfPCell(new Phrase("Sales Manager:", normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase(crs.getString("executive"), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Driver's Name:", normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase(unescapehtml(crs.getString("driver")), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Approved by:", normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("", normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Purpose of use:", normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Test Drive", bold_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Starting Kilometers:", normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase(crs.getString("testdrive_out_kms"), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Closing Kilometers:", normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// cell = new PdfPCell(new Phrase(crs.getString("testdrive_in_kms"), normal_font));
					// // cell.setBorderWidth(0);
					// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// body_table.addCell(cell);
					// document.add(body_table);
					document.close();
					file.close();
					if (!config_admin_email.equals("")
							&& config_email_enable.equals("1")) {
						postMail(config_admin_email, "", "", config_admin_email, "Gate Pass For Test Drive ID: " + testdrive_id, "Gate Pass For Test Drive ID: " + testdrive_id, attachment, comp_id);
					}
				}
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Gate Pass!");
				// Str.append("Invalid Gate Pass!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		document.close();
	}
	public void createCheckBox(PdfPTable table, Document document, PdfWriter writer, int checked, PdfPCell cell, String name, ArrayList<PdfFormField> list, int left, int bottom, int right, int top,
			String border) throws DocumentException {
		
		boolean chk_box = false;
		// checked or unchecked
		if (checked == 1) {
			chk_box = true;
		}
		
		PdfFormField checkbox = PdfFormField.createCheckBox(writer);
		// set the name for checkboxes
		checkbox.setFieldName(name);
		checkbox.setFieldFlags(PdfFormField.FF_READ_ONLY);
		// create the check box
		cell = new PdfPCell(new Phrase(""));
		cell.setCellEvent(new CellField(writer, checkbox, chk_box, "Yes", left, bottom, right, top));
		if (border.equals("")) {
			cell.setBorder(0);
		}
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.addCell(cell);
		
		list.add(checkbox);
	}
	
}
