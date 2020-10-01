package axela.axelaauto_app;
//sangita

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.sales.CellField;
import cloudify.connect.Connect;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class App_Enquiry_TrackingCard extends Connect {

	public String enquiry_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String BranchAccess;
	public String comp_id = "0";
	public String ExeAccess;
	PdfPTable followup_table;
	PdfPTable testdrive_details;
	Font header_font1 = FontFactory.getFont("Helvetica", 12, Font.BOLD, BaseColor.WHITE);
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 7, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 7);
	public static final String Font1 = "D:/webapp3/axelaauto-ddmotors/media/template/webdings.ttf";
	// Font tick_font = FontFactory.getFont("D:/webapp3/axelaauto-ddmotors/media/template/webdings.ttf", 9, Font.BOLD, BaseColor.b);
	// FontFactory.register("D:/webapp3/axelaauto-ddmotors/media/template");
	// BaseFont base123 = BaseFont.createFont("D:/webapp3/axelaauto-ddmotors/media/template/webdings.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
	public File f1 = null;
	ArrayList<PdfFormField> list = new ArrayList<PdfFormField>();
	private String Dzire;
	private String SX;
	public String emp_uuid = "";
	public String emp_id = "0", branch_brand_id = "0";
	public String access = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			branch_brand_id = CNumeric(PadQuotes(request.getParameter("branch_brand_id")));
			enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			SOP("emp_id=============" + emp_id);
			SOP("branch_brand_id=============" + branch_brand_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				if (branch_brand_id.equals("1") || branch_brand_id.equals("2") || branch_brand_id.equals("153")) {
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-trackingcard-maruti.jsp?enquiry_id=" + enquiry_id + "&brand_id=" + branch_brand_id + "&dr_format=pdf"));
				}
				else if (branch_brand_id.equals("6")) {
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-trackingcard-hyundai.jsp?dr_report=trackingcard-hyundai&enquiry_id=" + enquiry_id + "&brand_id="
							+ branch_brand_id + "&dr_format=pdf"));
				}
				else if (branch_brand_id.equals("7")) {
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-trackingcard-ford.jsp?dr_report=trackingcard-ford&enquiry_id=" + enquiry_id + "&brand_id="
							+ branch_brand_id + "&dr_format=pdf"));
				}
				else if (branch_brand_id.equals("55")) {
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-trackingcard-mb.jsp?dr_report=trackingcard-mb&enquiry_id=" + enquiry_id + "&brand_id="
							+ branch_brand_id + "&dr_format=pdf"));
				}
				else if (branch_brand_id.equals("151")) {
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-trackingcard-triumph.jsp?dr_report=enquiry-trackingcard-triumph&enquiry_id=" + enquiry_id + "&brand_id="
							+ branch_brand_id + "&dr_format=pdf"));
				} else if (branch_brand_id.equals("11")) {
					SOP("branch_brand_id===============" + branch_brand_id);
					response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-trackingcard-skoda.jsp?dr_report=enquiry-trackingcard-skoda" +
							"&enquiry_id=" + enquiry_id + "&brand_id=" + branch_brand_id +
							"&dr_format=pdf"));
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void EnquiryDetails(HttpServletRequest request, HttpServletResponse response, String enquiry_id, String BranchAccess, String ExeAccess, String purpose) throws IOException,
			DocumentException {

		try {
			BaseFont base = BaseFont.createFont(TemplatePath(comp_id) + "webdings.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
			// BaseFont base = BaseFont.createFont("D:/webapp3/axelaauto-ddmotors/media/template/webdings.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
			String tickfont = "";
			StrSql = "SELECT enquiry_id, enquiry_title, enquiry_desc, enquiry_dmsno, coalesce(occ_name,'') as occ_name,"
					+ " coalesce(contact_jobtitle,'') as contact_jobtitle,"
					+ " enquiry_qcsno, enquiry_date, enquiry_close_date, enquiry_monthkms_id, enquiry_item_id,"
					+ " enquiry_purchasemode_id,  enquiry_income_id,"
					+ " enquiry_familymember_count, enquiry_expectation_id, "
					+ " enquiry_othercar, enquiry_buyertype_id, enquiry_ownership_id, "
					+ " enquiry_existingvehicle, enquiry_purchasemonth, enquiry_loancompletionmonth, enquiry_currentemi,"
					+ " enquiry_kms, enquiry_loanfinancer, enquiry_expectedprice, enquiry_quotedprice,"
					+ " enquiry_age_id, enquiry_occ_id, enquiry_fuelallowance,"
					+ " ABS(CAST(enquiry_expectedprice AS CHAR) - CAST(enquiry_quotedprice AS CHAR)) AS gap,"
					+ " customer_id, customer_name, contact_id, concat(title_desc,' ',contact_fname,' ',"
					+ " contact_lname) as contactname, contact_phone1, contact_mobile1, contact_mobile2, contact_email1,"
					+ " contact_email2, contact_city_id, city_name, contact_pin, contact_address,"
					+ " coalesce(enquiry_model_id, 0) as enquiry_model_id, coalesce(model_name, 0) as model_name, enquiry_custtype_id, "
					+ " enquiry_soe_id, COALESCE(option_name,'') AS option_name, enquiry_priorityenquiry_id,"
					+ " emp_id, concat(emp_name,' (', emp_ref_no, ')') as emp_name, title_gender,"
					+ " branch_id, branch_code, concat(branch_name,' (', branch_code, ')') as branchname, branch_logo, comp_logo"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_id = contact_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_ownership on ownership_id = enquiry_ownership_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_age on age_id = enquiry_age_id"
					// + " left join " + compdb(comp_id) + "axela_sales_enquiry_add_ownership on status_id = enquiry_status_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_occ on occ_id = enquiry_occ_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_custtype on custtype_id = enquiry_custtype_id"
					+ " left join " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_model_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_expectation on expectation_id = enquiry_expectation_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_income on income_id = enquiry_income_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms on monthkms_id = enquiry_monthkms_id"
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype on buyertype_id = enquiry_buyertype_id"
					+ " left join " + compdb(comp_id) + "axela_vehstock_option on option_id = enquiry_option_id "
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode on purchasemode_id = enquiry_purchasemode_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " WHERE enquiry_id = " + enquiry_id + BranchAccess + ExeAccess + ""
					+ " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC";

			// SOP("StrSql======" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Document document = new Document();
				// PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
				document.setMargins(20, 20, 20, 20);
				PdfWriter writer = null;
				if (purpose.equals("pdf")) {
					response.setContentType("application/pdf");
					writer = PdfWriter.getInstance(document, response.getOutputStream());
				} else if (purpose.equals("file")) {
					File f = new File(CachePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					writer = PdfWriter.getInstance(document, new FileOutputStream(CachePath(comp_id) + "Enquiry_" + enquiry_id + ".pdf"));
					f1 = new File(CachePath(comp_id) + "Enquiry_" + enquiry_id + ".pdf");
				}
				document.open();
				while (crs.next()) {
					PdfPCell cell;
					cell = new PdfPCell();
					PdfPTable header_table = new PdfPTable(8);
					header_table.setWidthPercentage(100);

					PdfPTable img_table = new PdfPTable(1);
					img_table.setWidthPercentage(100);
					cell = new PdfPCell();
					if (!crs.getString("comp_logo").equals("")) {
						Image comp_logo = Image.getInstance(CompLogoPath() + crs.getString("comp_logo"));
						comp_logo.scaleAbsolute(50, 30);
						cell.addElement(new Chunk(comp_logo, 0, -30));
						cell.setFixedHeight(comp_logo.getHeight());
					} else {
						cell.addElement(new Phrase(""));
					}

					img_table.addCell(cell);
					cell = new PdfPCell(img_table);
					cell.setRowspan(2);
					header_table.addCell(cell);
					cell = new PdfPCell(new Phrase("ENQUIRY TRACKING CARD", header_font1));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.BLACK);
					// cell.setc(BaseColor.BLACK);
					cell.setColspan(6);
					header_table.addCell(cell);
					cell = new PdfPCell();
					Image branch_logo = Image.getInstance(TemplatePath(comp_id) + "maruti_suzuki_logo.jpg");
					if (!crs.getString("branch_logo").equals("")) {
						branch_logo.scaleAbsolute(60, 40);
						cell.addElement(new Chunk(branch_logo, 0, -30));
						cell.setRowspan(2);
						header_table.addCell(cell);
					} else {
						cell.addElement(new Phrase(""));
					}

					cell = new PdfPCell(new Phrase("DMS NO.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					header_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_dmsno"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					header_table.addCell(cell);
					cell = new PdfPCell(new Phrase("DSE Name: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					header_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("emp_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					header_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Date of Enquiry: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					header_table.addCell(cell);

					cell = new PdfPCell(new Phrase(strToShortDate(crs.getString("enquiry_date")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					header_table.addCell(cell);

					document.add(header_table);
					PdfPTable space_table1 = new PdfPTable(1);
					space_table1.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("CIN No.: U74899DL1974PLC007169", bold_font));
					cell.setBorder(0);
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					space_table1.addCell(cell);
					document.add(space_table1);

					PdfPTable customer_table = new PdfPTable(9);
					customer_table.setWidths(new int[]{2, 10, 10, 10, 10, 10, 10, 10, 10});
					customer_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("Customer Details", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					cell.setRowspan(10);
					cell.setRotation(90);
					cell.setBackgroundColor(BaseColor.GRAY);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Contact Details", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.GRAY);
					cell.setColspan(4);
					customer_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Occupation Details", bold_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.GRAY);
					cell.setColspan(4);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Name: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("contactname"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Occupation: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("occ_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Mobile No.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("contact_mobile1"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Alternate Contact No.: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("contact_mobile2"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Office landline: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("contact_phone1"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Designation: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("contact_jobtitle"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Email Address: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("contact_email1"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Office e-mail id: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Residence Address: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setRowspan(2);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("contact_address"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Office Address: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setRowspan(2);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase(" ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("City: " + crs.getString("city_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Pin code: " + crs.getString("contact_pin"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);
					//
					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setColspan(3);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("City: " + crs.getString("city_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Pin code: " + crs.getString("contact_pin"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);
					//
					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					PdfPTable Model_table = new PdfPTable(13);
					Model_table.setWidths(new int[]{12, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10});
					Model_table.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("Model Interested: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell);

					PdfPCell c1 = new PdfPCell();
					c1.setBorder(0);
					c1.addElement(new Chunk(Checkmark("Omni", crs.getString("model_name")), new Font(base, 11)));
					c1.addElement(new Chunk("Omini", normal_font));
					c1.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(c1);

					PdfPCell cell2 = new PdfPCell();
					cell2.addElement(new Chunk(Checkmark("Alto", crs.getString("model_name")), new Font(base, 11)));
					cell2.addElement(new Chunk("Alto", normal_font));
					cell2.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell2);

					PdfPCell cell3 = new PdfPCell();
					cell3.addElement(new Chunk(Checkmark("WagonR", crs.getString("model_name")), new Font(base, 11)));
					cell3.addElement(new Chunk("WagonR", normal_font));
					cell3.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell3);

					PdfPCell cell4 = new PdfPCell();
					cell4.addElement(new Chunk(Checkmark("Swift Dzire", crs.getString("model_name")), new Font(base, 11)));
					cell4.addElement(new Chunk("Dzire", normal_font));
					cell4.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell4);

					PdfPCell cell5 = new PdfPCell();
					cell5.addElement(new Chunk(Checkmark("SX4", crs.getString("model_name")), new Font(base, 11)));
					cell5.addElement(new Chunk("SX4", normal_font));
					cell5.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell5);

					PdfPCell cell6 = new PdfPCell();
					cell6.addElement(new Chunk(Checkmark("Versa", crs.getString("model_name")), new Font(base, 11)));
					cell6.addElement(new Chunk("Versa", normal_font));
					cell6.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell6);

					PdfPCell cell7 = new PdfPCell();
					cell7.addElement(new Chunk(Checkmark("Swift", crs.getString("model_name")), new Font(base, 11)));
					cell7.addElement(new Chunk("Swift", normal_font));
					cell7.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell7);

					PdfPCell cell8 = new PdfPCell();
					cell8.addElement(new Chunk(Checkmark("Ritz", crs.getString("model_name")), new Font(base, 11)));
					cell8.addElement(new Chunk("Ritz", normal_font));
					cell8.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell8);

					PdfPCell cell9 = new PdfPCell();
					cell9.addElement(new Chunk(Checkmark("Celerio", crs.getString("model_name")), new Font(base, 11)));
					cell9.addElement(new Chunk("Celerio", normal_font));
					cell9.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell9);

					PdfPCell cell10 = new PdfPCell();
					cell10.addElement(new Chunk(Checkmark("Ertiga", crs.getString("model_name")), new Font(base, 11)));
					cell10.addElement(new Chunk("Ertiga", normal_font));
					cell10.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell10);

					PdfPCell cell11 = new PdfPCell();
					cell11.addElement(new Chunk(Checkmark("EECO", crs.getString("model_name")), new Font(base, 11)));
					cell11.addElement(new Chunk("EECO", normal_font));
					cell11.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell11);

					PdfPCell cell12 = new PdfPCell();
					cell12.addElement(new Chunk(Checkmark("Ciaz", crs.getString("model_name")), new Font(base, 11)));
					cell12.addElement(new Chunk("Ciaz", normal_font));
					cell2.setVerticalAlignment(Element.ALIGN_CENTER);
					Model_table.addCell(cell12);

					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setColspan(7);
					cell = new PdfPCell(Model_table);
					cell.setColspan(13);
					customer_table.addCell(cell);
					// /////////////////////////////////
					cell = new PdfPCell(new Phrase("Preferred mode of communication: ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					PdfPCell cellmob = new PdfPCell();
					cellmob.addElement(new Chunk("a", new Font(base, 11)));
					cellmob.addElement(new Chunk("Mobile", normal_font));
					cellmob.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cellmob);

					cell = new PdfPCell(new Phrase("Residence Landline", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Office Landline", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Type of Customer", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);

					PdfPTable custtable = new PdfPTable(3);
					custtable.setWidths(new int[]{4, 4, 5});

					cell = new PdfPCell(new Phrase(Checkmark("1", crs.getString("enquiry_custtype_id")), new Font(base, 11)));
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_RIGHT);
					custtable.addCell(cell);
					cell = new PdfPCell(new Phrase(Checkmark("2", crs.getString("enquiry_custtype_id")), new Font(base, 11)));
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					custtable.addCell(cell);
					cell = new PdfPCell(new Phrase(Checkmark("3", crs.getString("enquiry_custtype_id")), new Font(base, 11)));
					cell.setBorder(0);
					custtable.addCell(cell);

					cell = new PdfPCell(new Phrase("Corporate/", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_RIGHT);
					cell.setBorder(0);
					custtable.addCell(cell);
					cell = new PdfPCell(new Phrase("Individual/", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					custtable.addCell(cell);
					cell = new PdfPCell(new Phrase("Taxi", normal_font));
					cell.setBorder(0);
					custtable.addCell(cell);
					cell = new PdfPCell(custtable);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					customer_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Corporate/Individual/Taxi", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Scheme", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer_table.addCell(cell);
					// /////////////////////////////////

					PdfPTable cust = new PdfPTable(8);
					cust.setWidths(new int[]{10, 15, 10, 10, 10, 10, 10, 10});

					cell = new PdfPCell(new Phrase("Approx. Age ", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cust.addCell(cell);

					PdfPTable age = new PdfPTable(4);
					age.setWidths(new int[]{10, 10, 10, 10});

					cell = new PdfPCell(new Phrase(Checkmark("1", crs.getString("enquiry_age_id")), new Font(base, 11)));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(new Phrase(Checkmark("2", crs.getString("enquiry_age_id")), new Font(base, 11)));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(new Phrase(Checkmark("3", crs.getString("enquiry_age_id")), new Font(base, 11)));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(new Phrase(Checkmark("4", crs.getString("enquiry_age_id")), new Font(base, 11)));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(new Phrase("<=30", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(new Phrase("30-40", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(new Phrase("40-50", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(new Phrase(">50", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					age.addCell(cell);

					cell = new PdfPCell(age);
					cust.addCell(cell);

					cell = new PdfPCell(new Phrase("Gender", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cust.addCell(cell);

					// PdfPCell cellaq = new PdfPCell();
					Paragraph p = new Paragraph();
					p.add(new Chunk(Checkmark("1", crs.getString("title_gender")), new Font(base, 11)));
					p.add(new Chunk(Checkmark("2", crs.getString("title_gender")), new Font(base, 11)));
					Paragraph p1 = new Paragraph();
					p1.add(new Chunk("M/", normal_font));
					p1.add(new Chunk(" F", normal_font));
					cell.addElement(p);
					cell.addElement(p1);
					// cell = new PdfPCell(new Phrase("M/F", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_TOP);
					cust.addCell(cell);

					cell = new PdfPCell(new Phrase("Fuel Allowance\n(Corporate Only)", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cust.addCell(cell);

					String fuel = "";
					if (CNumeric(crs.getString("enquiry_fuelallowance")).equals("0")) {
						fuel = "0";
					} else {
						fuel = "1";
					}

					Paragraph fuel1 = new Paragraph();
					fuel1.add(new Chunk(Checkmark("1", fuel), new Font(base, 11)));
					fuel1.add(new Chunk(Checkmark("2", fuel), new Font(base, 11)));
					Paragraph fuel2 = new Paragraph();
					fuel2.add(new Chunk("Yes/", normal_font));
					fuel2.add(new Chunk(" No", normal_font));
					cell.addElement(fuel1);
					cell.addElement(fuel2);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cust.addCell(cell);

					cell = new PdfPCell(new Phrase("Fuel Allowance Amountpm", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cust.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_fuelallowance"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cust.addCell(cell);

					cell = new PdfPCell(cust);
					cell.setColspan(8);
					customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase(crs.getString("contact_pin"), normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setColspan(3);
					// customer_table.addCell(cell);

					// cell = new PdfPCell(new Phrase("City: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase(crs.getString("city_name"), normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setColspan(3);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("City: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Pin code: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase(crs.getString("contact_pin"), normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setColspan(3);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("Pin code: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Preferred mode of communication: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("Type of Customer: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase(crs.getString("custtype_name"), normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setColspan(3);
					// customer_table.addCell(cell);
					// cell = new PdfPCell(new Phrase("Approx. Age: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("Fuel Allowance: ", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// customer_table.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase(crs.getString("enquiry_fuelallowance"), normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// cell.setColspan(3);
					// customer_table.addCell(cell);
					document.add(customer_table);

					PdfPTable space_table2 = new PdfPTable(1);
					space_table2.setWidthPercentage(100);
					cell = new PdfPCell();
					cell.addElement(new Phrase("", bold_font));
					cell.setBorder(0);
					cell.setFixedHeight(5);
					space_table2.addCell(cell);
					document.add(space_table2);

					PdfPTable source = new PdfPTable(3);

					source.setWidths(new int[]{2, 40, 40});
					source.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("Source", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setRowspan(2);
					cell.setRotation(90);
					cell.setBackgroundColor(BaseColor.GRAY);
					source.addCell(cell);
					// source.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("Natural (Customer Contacted Us First): ", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.GRAY);
					source.addCell(cell);

					cell = new PdfPCell(new Phrase("Generated (We Contracted The Customer First): ", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.GRAY);
					source.addCell(cell);

					PdfPTable source_table = new PdfPTable(8);
					source_table.setWidthPercentage(100);

					PdfPCell soe1 = new PdfPCell();
					soe1.addElement(new Chunk(Checkmark("1", crs.getString("enquiry_soe_id")), new Font(base, 11)));
					soe1.addElement(new Chunk("Walk-in", normal_font));
					soe1.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe1);

					PdfPCell soe2 = new PdfPCell();
					soe2.addElement(new Chunk(Checkmark("2", crs.getString("enquiry_soe_id")), new Font(base, 11)));
					soe2.addElement(new Chunk("Incoming Telephone", normal_font));
					soe2.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe2);

					PdfPCell soe3 = new PdfPCell();
					soe3.addElement(new Chunk(Checkmark("3", crs.getString("enquiry_soe_id")), new Font(base, 11)));
					soe3.addElement(new Chunk("Reference(FNR-friends/Neighbours/Relatives", normal_font));
					soe3.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe3);

					PdfPCell soe4 = new PdfPCell();
					soe4.addElement(new Chunk(Checkmark("8", crs.getString("enquiry_soe_id")), new Font(base, 11)));
					soe4.addElement(new Chunk("Atm/Web/call Center", normal_font));
					soe4.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe4);

					PdfPCell soe5 = new PdfPCell();
					soe5.addElement(new Chunk(Checkmark("7", crs.getString("enquiry_soe_id")), new Font(base, 11)));
					soe5.addElement(new Chunk("Cold Call/Field Visit", normal_font));
					soe5.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe5);

					PdfPCell soe6 = new PdfPCell();
					soe6.addElement(new Chunk(Checkmark("6", crs.getString("enquiry_soe_id")), new Font(base, 11)));
					soe6.addElement(new Chunk("Events/Campaign", normal_font));
					soe6.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe6);

					PdfPCell soe7 = new PdfPCell();
					soe7.addElement(new Chunk(Checkmark("3", crs.getString("enquiry_soe_id")), new Font(base, 11)));
					soe7.addElement(new Chunk("Reference(Generated)", normal_font));
					soe7.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe7);

					PdfPCell soe8 = new PdfPCell();
					soe8.addElement(new Chunk("", new Font(base, 11)));
					soe8.addElement(new Chunk("Auto Cards Reference No.", normal_font));
					soe8.setVerticalAlignment(Element.ALIGN_CENTER);
					source_table.addCell(soe8);

					cell = new PdfPCell(source_table);
					cell.setColspan(2);

					source.addCell(cell);

					document.add(source);

					PdfPTable space_table3 = new PdfPTable(1);
					space_table3.setWidthPercentage(100);
					cell = new PdfPCell();
					cell.addElement(new Phrase("", bold_font));
					cell.setBorder(0);
					cell.setFixedHeight(5);
					space_table3.addCell(cell);
					document.add(space_table3);

					PdfPTable customer = new PdfPTable(6);
					customer.setWidths(new int[]{2, 30, 10, 10, 10, 10});
					customer.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("Understand Your Customer", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.GRAY);
					cell.setRowspan(8);
					cell.setRotation(90);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("How many kilometers you drive in a month?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					PdfPCell kms1 = new PdfPCell();
					kms1.addElement(new Chunk(Checkmark("1", crs.getString("enquiry_monthkms_id")), new Font(base, 11)));
					kms1.addElement(new Chunk("< 1000", normal_font));
					kms1.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(kms1);

					PdfPCell kms2 = new PdfPCell();
					kms2.addElement(new Chunk(Checkmark("2", crs.getString("enquiry_monthkms_id")), new Font(base, 11)));
					kms2.addElement(new Chunk("1000 to <= 1500", normal_font));
					kms2.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(kms2);

					PdfPCell kms3 = new PdfPCell();
					kms3.addElement(new Chunk(Checkmark("3", crs.getString("enquiry_monthkms_id")), new Font(base, 11)));
					kms3.addElement(new Chunk("1500 to <= 2000", normal_font));
					kms3.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(kms3);

					PdfPCell kms4 = new PdfPCell();
					kms4.addElement(new Chunk(Checkmark("4", crs.getString("enquiry_monthkms_id")), new Font(base, 11)));
					kms4.addElement(new Chunk("> 2000", normal_font));
					kms4.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(kms4);

					cell = new PdfPCell(new Phrase("What will be your mode of purchase?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					PdfPCell mode1 = new PdfPCell();
					mode1.addElement(new Chunk(Checkmark("1", crs.getString("enquiry_purchasemode_id")), new Font(base, 11)));
					mode1.addElement(new Chunk("Cash", normal_font));
					mode1.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(mode1);

					PdfPCell mode2 = new PdfPCell();
					mode2.addElement(new Chunk(Checkmark("2", crs.getString("enquiry_purchasemode_id")), new Font(base, 11)));
					mode2.addElement(new Chunk("Finance", normal_font));
					// cell = new PdfPCell(new Phrase("Finance", normal_font));
					mode2.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(mode2);

					PdfPCell mode3 = new PdfPCell();
					mode3.addElement(new Chunk(Checkmark("3", crs.getString("enquiry_purchasemode_id")), new Font(base, 11)));
					mode3.addElement(new Chunk("Self Arranged Finance", normal_font));
					mode3.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(mode3);

					PdfPCell mode4 = new PdfPCell();
					mode4.addElement(new Chunk(Checkmark("4", crs.getString("enquiry_purchasemode_id")), new Font(base, 11)));
					mode4.addElement(new Chunk("Company Finance", normal_font));
					mode4.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(mode4);

					cell = new PdfPCell(new Phrase("What is your approximate annual household income (Rs)?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					PdfPCell income1 = new PdfPCell();
					income1.addElement(new Chunk(Checkmark("1", crs.getString("enquiry_income_id")), new Font(base, 11)));
					income1.addElement(new Chunk("<= 3 lacs", normal_font));
					income1.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(income1);

					PdfPCell income2 = new PdfPCell();
					income2.addElement(new Chunk(Checkmark("2", crs.getString("enquiry_income_id")), new Font(base, 11)));
					income2.addElement(new Chunk("3 lacs to 6 lacs", normal_font));
					income2.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(income2);

					PdfPCell income3 = new PdfPCell();
					income3.addElement(new Chunk(Checkmark("3", crs.getString("enquiry_income_id")), new Font(base, 11)));
					income3.addElement(new Chunk("6 lacs to 10 lacs", normal_font));
					income3.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(income3);

					PdfPCell income4 = new PdfPCell();
					income4.addElement(new Chunk(Checkmark("4", crs.getString("enquiry_income_id")), new Font(base, 11)));
					income4.addElement(new Chunk("> 10 lacs", normal_font));
					income4.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(income4);

					cell = new PdfPCell(new Phrase("Which model are you interested in?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("Model: " + crs.getString("model_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("Fuel Type", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("Colour: " + crs.getString("option_name"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("How many members are there in your family?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_familymember_count"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(4);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("What is top most priority expectations from the car?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("Mileage/Features/Power/Looks/Value for Money", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(4);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("By when, are you expecting to finalize your new car?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					PdfPCell prior1 = new PdfPCell();
					prior1.addElement(new Chunk(Checkmark("1", crs.getString("enquiry_priorityenquiry_id")), new Font(base, 11)));
					prior1.addElement(new Chunk("<=15 Days\n(Hot)", normal_font));
					prior1.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(prior1);

					PdfPCell prior2 = new PdfPCell();
					prior2.addElement(new Chunk(Checkmark("2", crs.getString("enquiry_priorityenquiry_id")), new Font(base, 11)));
					prior2.addElement(new Chunk("16 to 30 Days\n(Warm)", normal_font));
					prior2.setVerticalAlignment(Element.ALIGN_CENTER);
					prior2.setColspan(2);
					customer.addCell(prior2);

					PdfPCell prior3 = new PdfPCell();
					prior3.addElement(new Chunk(Checkmark("3", crs.getString("enquiry_priorityenquiry_id")), new Font(base, 11)));
					prior3.addElement(new Chunk(">30 Days\n(Cold)", normal_font));
					prior3.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(prior3);

					cell = new PdfPCell(new Phrase("Any other car you have in mind?", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_othercar"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					customer.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(3);
					customer.addCell(cell);
					document.add(customer);

					PdfPTable space_table4 = new PdfPTable(1);
					space_table4.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setBorder(0);
					cell.setFixedHeight(5);
					space_table4.addCell(cell);
					document.add(space_table4);

					PdfPTable exchange_details = new PdfPTable(10);
					exchange_details.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("Exchange Details", bold_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.GRAY);
					cell.setColspan(10);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase("Type of Buyer:", normal_font));// crs.getString("buyertype_name")
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange_details.addCell(cell);

					PdfPTable buyer = new PdfPTable(3);
					buyer.setWidths(new int[]{3, 3, 10});

					cell = new PdfPCell(new Phrase(Checkmark("1", crs.getString("enquiry_buyertype_id")), new Font(base, 11)));
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_RIGHT);
					buyer.addCell(cell);
					cell = new PdfPCell(new Phrase(Checkmark("2", crs.getString("enquiry_buyertype_id")), new Font(base, 11)));
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					buyer.addCell(cell);
					cell = new PdfPCell(new Phrase(Checkmark("3", crs.getString("enquiry_buyertype_id")), new Font(base, 11)));
					cell.setBorder(0);
					buyer.addCell(cell);

					cell = new PdfPCell(new Phrase("First Time/", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_RIGHT);
					cell.setBorder(0);
					buyer.addCell(cell);
					cell = new PdfPCell(new Phrase("Additional/", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					buyer.addCell(cell);
					cell = new PdfPCell(new Phrase("replacement", normal_font));
					cell.setBorder(0);
					buyer.addCell(cell);
					cell = new PdfPCell(buyer);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(4);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase("Existing Vehicle Details:", normal_font));// crs.getString("enquiry_existingvehicle")
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase("Make: " + crs.getString("enquiry_existingvehicle"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase("Purchase Month/Year", normal_font));// crs.getString("enquiry_purchasemonth")
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase(strToShortDate(crs.getString("enquiry_purchasemonth")), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange_details.addCell(cell);

					PdfPTable exchange = new PdfPTable(10);
					exchange.setWidths(new int[]{10, 15, 10, 10, 10, 10, 10, 10, 10, 10});

					cell = new PdfPCell(new Phrase("Ownership", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					PdfPTable exchangeown = new PdfPTable(3);
					exchangeown.setWidths(new int[]{6, 9, 8});

					cell = new PdfPCell(new Phrase(Checkmark("1", crs.getString("enquiry_ownership_id")), new Font(base, 11)));
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_RIGHT);
					exchangeown.addCell(cell);
					cell = new PdfPCell(new Phrase(Checkmark("2", crs.getString("enquiry_ownership_id")), new Font(base, 11)));
					cell.setBorder(0);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchangeown.addCell(cell);
					cell = new PdfPCell(new Phrase(Checkmark("3", crs.getString("enquiry_ownership_id")), new Font(base, 11)));
					cell.setBorder(0);
					exchangeown.addCell(cell);

					cell = new PdfPCell(new Phrase("First/", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_RIGHT);
					cell.setBorder(0);
					exchangeown.addCell(cell);
					cell = new PdfPCell(new Phrase("Second/", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setBorder(0);
					exchangeown.addCell(cell);
					cell = new PdfPCell(new Phrase("Third", normal_font));
					cell.setBorder(0);
					exchangeown.addCell(cell);
					cell = new PdfPCell(exchangeown);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase("Loan Completion Month/Year(if any):", normal_font));// crs.getString("enquiry_loancompletionmonth")
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_loancompletionmonth"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase("Current EMI (Rs):", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_currentemi"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase("Kms:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_kms"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase("Financer Name (Loan):", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(new Phrase(crs.getString("enquiry_loanfinancer"), normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange.addCell(cell);

					cell = new PdfPCell(exchange);
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(10);
					exchange_details.addCell(cell);
					// /////////////////////
					cell = new PdfPCell(new Phrase("Expected Price crs.(A):", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase(IndDecimalFormat(crs.getString("enquiry_expectedprice")) + " /-", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase("Quoted Price(B):", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase(IndDecimalFormat(crs.getString("enquiry_quotedprice")) + " /-", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					exchange_details.addCell(cell);
					// ///////////////////////

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase("Gap crs.(A-B):", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					exchange_details.addCell(cell);

					cell = new PdfPCell(new Phrase(IndDecimalFormat(crs.getString("gap")) + " /-", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					exchange_details.addCell(cell);
					document.add(exchange_details);
					// /////////////////////////

					PdfPTable space_table5 = new PdfPTable(1);
					space_table5.setWidthPercentage(100);
					cell = new PdfPCell();
					cell.addElement(new Phrase("", bold_font));
					cell.setBorder(0);
					cell.setFixedHeight(5);
					space_table5.addCell(cell);
					document.add(space_table5);
					TestDriveDetails(enquiry_id, base);
					document.add(testdrive_details);
					PdfPTable space_table6 = new PdfPTable(1);
					space_table6.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setBorder(0);
					cell.setFixedHeight(5);
					space_table6.addCell(cell);
					document.add(space_table6);

					PdfPTable summary = new PdfPTable(8);
					summary.setWidths(new int[]{10, 10, 10, 10, 10, 10, 10, 10});
					summary.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("Summary", bold_font));
					cell.setBackgroundColor(BaseColor.GRAY);
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(8);
					summary.addCell(cell);

					cell = new PdfPCell(new Phrase("Status:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);

					cell = new PdfPCell(new Phrase("Booked/Dropped/Lost", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);

					// cell = new PdfPCell(new Phrase("Model & Variant enquired for:", normal_font));
					// cell.setVerticalAlignment(Element.ALIGN_CENTER);
					// summary.addCell(cell);
					//
					// cell = new PdfPCell(new Phrase("", normal_font));
					// cell.setVerticalAlignment(ElModelement.ALIGN_CENTER);
					// summary.addCell(cell);
					cell = new PdfPCell(new Phrase("Model & Variant Enquired For:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);

					cell = new PdfPCell(new Phrase("Model & Variant Booked:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);

					cell = new PdfPCell(new Phrase("Date of Booking:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					summary.addCell(cell);
					document.add(summary);

					PdfPTable space_table7 = new PdfPTable(1);
					space_table7.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setBorder(0);
					cell.setFixedHeight(5);
					space_table7.addCell(cell);
					document.add(space_table7);
					//
					FollowupDetails(enquiry_id);
					document.add(followup_table);

					PdfPTable space_table = new PdfPTable(2);
					space_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					cell.setFixedHeight(5);
					space_table.addCell(cell);
					document.add(space_table);
					//
					PdfPTable lost_table = new PdfPTable(7);
					lost_table.setWidths(new int[]{10, 10, 10, 10, 10, 10, 10});
					lost_table.setWidthPercentage(100);
					lost_table.setKeepTogether(true);

					cell = new PdfPCell(new Phrase("Lost Case Details if Applicable", bold_font));

					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setBackgroundColor(BaseColor.GRAY);
					cell.setColspan(7);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Lost to:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Competition Company/Model", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Co Dealer/Model:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Used Car(Please Specify):", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);
					// //////////////////////////////
					cell = new PdfPCell(new Phrase("Reason:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Price/Features/More Discount/Availability/Better Follow-up", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Availability/Better Follow-up/Better Deal/Others", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Customer's VOC:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(5);
					lost_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Team Leader's VOC:", normal_font));
					cell.setVerticalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					lost_table.addCell(cell);
					document.add(lost_table);

					PdfPTable space_table8 = new PdfPTable(1);
					space_table8.setWidthPercentage(100);
					cell = new PdfPCell(new Phrase("", bold_font));
					cell.setBorder(0);
					cell.setFixedHeight(5);
					space_table8.addCell(cell);
					document.add(space_table8);

					PdfPTable sign_table = new PdfPTable(4);
					sign_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("Signature:", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setFixedHeight(70);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("DSE:", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setFixedHeight(70);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("TL:", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setFixedHeight(70);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("GM:", bold_font));
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					// cell.setFixedHeight(70);
					sign_table.addCell(cell);

					document.add(sign_table);

					// ///////////////////////////////////////////////////////////////
					PdfPTable top_table = new PdfPTable(1);
					top_table.setWidthPercentage(100);
					top_table.setKeepTogether(true);

					int yes_checked = 0;
					// PdfPCell cell;

					PdfPTable formtable = new PdfPTable(5);
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
					cell = new PdfPCell(new Phrase("Customer’s Signature", normal_font));
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

					cell = new PdfPCell(new Phrase("Dear Sir,\n\n"
							+ "            On my request, you have provided me the Car no _____________________________________ (Demo Car) for test driving. I confirm to have undertaken as under:"
							+ "\n1) The ‘Demo Car’ shall be driven by me only."
							+ "\n2) The ‘Demo Car’ shall be used for the purpose of test drive only."
							+ "\n3) I shall test drive the ‘Demo Car’ only in the presence of authorized representative of your Company."
							+ "\n4) I hold a valid and effective during license, a copy of which is being provided herewith."
							+ "\n5) I shall test drive the ‘Demo Car’ only within the municipal limits of city of Delhi."
							+ "\n6) I shall abide by any all Rules of the Road Regulations while test driving of the ‘Demo Car’."
							+ "\n7) While conducting the test drive, I shall not engage in any activity which may be illegal or which may otherwise prejudice to M/s DD Motocrs."
							+ "\n\n"
							+ "            In the event of any legal and/or civil consequence and arising from any handling/use of the aforesaid vehicle."
							+ " I undertake to Indemnify M/s DD Motors against all liabilities. The indemnity shall include but not be limited to police challans,"
							+ " damages to ‘Demo Car’ and third party property, and any other civil/criminal action that may result from my using the ‘Demo Car’"
							+ "\nThanking You"
							+ "\n\nYours faithfully"
							+ "\n\n\n"
							+ "Customer’s Signature\n\n", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setBorder(0);
					cell.setColspan(4);
					newtable.addCell(cell);

					top_table.addCell(newtable);
					document.add(top_table);

					for (PdfFormField field : list) {
						writer.addAnnotation(field);
					}

					PdfPTable space_table10 = new PdfPTable(1);
					space_table10.setWidthPercentage(100);
					//
					cell = new PdfPCell(new Phrase("\n\n", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					space_table10.addCell(cell);
					document.add(space_table10);

					// formtable.setWidths(new int[]{10, 3, 1, 4, 1});
					// formtable.getDefaultCell().setFixedHeight(10);
					PdfPTable five_col1 = new PdfPTable(1);
					five_col1.setWidthPercentage(100);
					five_col1.setKeepTogether(true);

					PdfPTable five_col = new PdfPTable(5);
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

				}
				document.close();
			} else {
				if (response != null) {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Enquiry!");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void TestDriveDetails(String enquiry_id, BaseFont base) throws DocumentException {
		testdrive_details = new PdfPTable(9);
		testdrive_details.setWidths(new int[]{10, 10, 10, 10, 10, 10, 10, 10, 10});
		testdrive_details.setWidthPercentage(100);

		PdfPCell cell;
		StrSql = "select testdrive_id, testdriveveh_id, testdriveveh_name, branch_code, customer_name, contact_id, concat(contact_fname,' ',"
				+ " contact_lname) as contactname, contact_mobile1, contact_mobile2,  "
				+ " coalesce(model_name, '') as  model_name, testdrive_location_id,"
				+ " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed,"
				+ " testdrive_doc_value, customer_id, enquiry_id, branch_id, CONCAT(branch_name,' (',branch_code,')') as branchname, "
				+ " emp_id, concat(emp_name,' (', emp_ref_no, ')') as emp_name, "
				+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms, testdrive_out_time, testdrive_out_kms, "
				+ " testdrive_fb_taken, coalesce(testdrive_fb_status_id,'0') as testdrive_fb_status_id, coalesce(status_name, '') as status_name,"
				+ " if(testdrive_fb_taken=2,testdrive_fb_notes,'') as testdrive_notes,"
				+ " testdrive_fb_budget, testdrive_fb_finance, testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
				+ " testdrive_fb_insurance, testdrive_fb_insurance_comments  "
				+ " from " + compdb(comp_id) + "axela_sales_testdrive "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id "
				+ " inner Join " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id = testdrive_veh_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = veh_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id "
				+ " left join " + compdb(comp_id) + "axela_sales_testdrive_status on status_id= testdrive_fb_status_id"
				+ " where 1=1 and testdrive_enquiry_id=" + enquiry_id + BranchAccess + ExeAccess + ""
				+ " group by testdrive_id"
				+ " order by testdrive_id desc";
		// SOP("StrSql test=====" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		String testdrive_time = "", PeriodTime = "", location_id = "", testdrive_notes = "", model_name = "";
		try {
			while (crs.next()) {
				model_name = crs.getString("model_name");
				testdrive_time = strToLongDate(crs.getString("testdrive_time"));
				PeriodTime = PeriodTime(crs.getString("testdrive_time_from"), crs.getString("testdrive_time_to"), "1");
				location_id = crs.getString("testdrive_location_id");
				testdrive_notes = crs.getString("testdrive_notes");
			}
			cell = new PdfPCell(new Phrase("Test Drive Details", bold_font));
			// cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(9);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("DMS Test Drive No.:", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("Date/Time:", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase(testdrive_time, normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("Model:", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase(model_name, normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("Duration(Time/Kms):", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase(PeriodTime, normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("Start(M):", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("Place of TD:", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			PdfPTable testtable = new PdfPTable(2);
			testtable.setWidths(new int[]{3, 5});

			cell = new PdfPCell(new Phrase(Checkmark("1", location_id), new Font(base, 11)));
			cell.setBorder(0);
			cell.setVerticalAlignment(Element.ALIGN_RIGHT);
			testtable.addCell(cell);
			cell = new PdfPCell(new Phrase(Checkmark("2", location_id), new Font(base, 11)));
			cell.setBorder(0);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testtable.addCell(cell);

			cell = new PdfPCell(new Phrase("Dealership/", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_RIGHT);
			cell.setBorder(0);
			testtable.addCell(cell);
			cell = new PdfPCell(new Phrase("Customer Place", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_LEFT);
			cell.setBorder(0);
			testtable.addCell(cell);

			cell = new PdfPCell(testtable);
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("Reasons if Test Drive not given:", normal_font));// / testdrive_notes
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase(testdrive_notes, normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("Customer Sign", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			testdrive_details.addCell(cell);

			cell = new PdfPCell(new Phrase("", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(2);
			testdrive_details.addCell(cell);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		cell = new PdfPCell();
		cell.addElement(new Phrase("Customer Sign:", normal_font));
		cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
		cell.setFixedHeight(50);
		cell.setColspan(4);
		testdrive_details.addCell(cell);
	}

	public void FollowupDetails(String enquiry_id) throws DocumentException {
		int diff = 0;
		followup_table = new PdfPTable(5);
		followup_table.setWidthPercentage(100);
		followup_table.getKeepTogether();
		PdfPCell cell;
		// folloup_table.setWidths(new int[]{2, 50, 6});
		cell = new PdfPCell(new Phrase("FOLLOW UP DETAILS", header_font1));
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.BLACK);
		cell.setColspan(5);
		followup_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Date", bold_font));
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.GRAY);
		followup_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Mode of Contact", bold_font));
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.GRAY);
		followup_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Customer's Comment", bold_font));
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.GRAY);
		followup_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Days due for purchase", bold_font));
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.GRAY);
		followup_table.addCell(cell);

		cell = new PdfPCell(new Phrase("Next Action Date", bold_font));
		cell.setVerticalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.GRAY);
		followup_table.addCell(cell);

		StrSql = " select followup_id, followup_enquiry_id, followup_followup_time, followup_desc, "
				+ " followup_entry_time, DATEDIFF(enquiry_close_date,enquiry_date) AS diff,"
				+ " followuptype_name, CONCAT(emp_name,' (',emp_ref_no,')') as entry_by,"
				+ " COALESCE((SELECT followup_followup_time FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
				+ " WHERE followup_enquiry_id=" + enquiry_id + " and followup_id>f.followup_id"
				+ " order by followup_followup_time limit  1),'') as nextfollowup"
				+ " from " + compdb(comp_id) + "axela_sales_enquiry_followup f "
				+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = f.followup_enquiry_id"
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = f.followup_entry_id "
				+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_followup_type on followuptype_id = f.followup_followuptype_id "
				+ " where 1 = 1 and f.followup_enquiry_id = " + enquiry_id + " "
				+ " order by f.followup_followup_time ";
		// SOP("StrSql---" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				// diff = (int)getDaysBetween(crs.getString("enquiry_date"), crs.getString("enquiry_close_date"));
				cell = new PdfPCell(new Phrase(strToLongDate(crs.getString("followup_followup_time")), normal_font));
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				followup_table.addCell(cell);

				cell = new PdfPCell(new Phrase(crs.getString("followuptype_name"), normal_font));
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				followup_table.addCell(cell);

				cell = new PdfPCell(new Phrase((crs.getString("followup_desc").replace("<br/>", "\n")), normal_font));
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				followup_table.addCell(cell);

				cell = new PdfPCell(new Phrase(crs.getString("diff"), normal_font));
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				followup_table.addCell(cell);

				cell = new PdfPCell(new Phrase(strToShortDate(crs.getString("nextfollowup")), normal_font));
				cell.setVerticalAlignment(Element.ALIGN_CENTER);
				followup_table.addCell(cell);
			}
			cell = new PdfPCell(new Phrase("Date", bold_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.GRAY);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Team Leader's Comment", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.GRAY);
			cell.setColspan(3);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Next Action Date", bold_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.GRAY);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(3);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Date", bold_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.GRAY);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("General Manager's Comment", bold_font));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.GRAY);
			cell.setColspan(3);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("Next Action Date", bold_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.GRAY);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			cell.setColspan(3);
			followup_table.addCell(cell);

			cell = new PdfPCell(new Phrase("\n", normal_font));
			cell.setVerticalAlignment(Element.ALIGN_CENTER);
			followup_table.addCell(cell);

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
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

	protected String Checkmark(String name, String value) {
		String checked = "";
		if (value.contains(name) && !value.contains("Swift")) {
			checked = "a";
		}
		if (value.equals("Swift") && name.equals("Swift")) {
			checked = "a";
		} else if (value.equals("Swift Dzire") && name.equals("Swift Dzire")) {
			checked = "a";
		}
		return checked;
	}
}
