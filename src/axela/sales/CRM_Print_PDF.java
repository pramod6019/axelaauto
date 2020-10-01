package axela.sales;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class CRM_Print_PDF extends Connect {

	public String crm_id = "0";
	public String so_id = "0";
	public String StrSql = "";
	public String StrHTML = "", salutation = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess;
	public String ExeAccess;
	public String total_disc = "";
	public String grandtotal = "";
	public String comp_id = "0";
	public String emp_id = "0";
	PdfPTable item_table;
	PdfPTable config_table;
	Font header_font = FontFactory.getFont("Helvetica", 12, Font.BOLD);
	Font bold_font = FontFactory.getFont("Helvetica", 9, Font.BOLD);
	Font normal_font = FontFactory.getFont("Helvetica", 9);
	public String quoteitem_rowcount = "0", quote_exprice = "";
	public String item_taxcal = "0";
	public int count1 = 0;
	public String quoteitem_option_group = "";
	public String group_name = "";
	public double quote_grandtotal = 0.0;
	public double quote_netamt = 0.0;
	public double quoteitem_price = 0.0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				crm_id = CNumeric(PadQuotes(request.getParameter("crm_id")));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				if (ReturnPerm(comp_id, "emp_enquiry_access", request).equals("1")) {
					FollowupDetails(request, response, so_id, BranchAccess,
							ExeAccess.replace("emp_id", "so_emp_id"), comp_id,
							"pdf");
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void FollowupDetails(HttpServletRequest request,
			HttpServletResponse response, String so_id, String BranchAccess,
			String ExeAccess, String comp_id, String purpose)
			throws IOException, DocumentException {
		try {
			String script = "";

			script = "REPLACE(crmdays_script, '[SALUTATION]'," + "'"
					+ GetSalutation(ToLongDate(kknow())) + "')";
			script = "REPLACE("
					+ script
					+ ",'[CONTACTNAME]',concat(title_desc,' ', contact_fname,' ', contact_lname))";
			script = "REPLACE(" + script
					+ ",'[EXENAME]',enquiryexe.emp_name) as crmdays_script";

			StrSql = "SELECT "
					+ script
					+ ", enquiry_branch_id, enquiry_close_date, enquiry_buyertype_id, enquiry_existingvehicle,"
					+ " enquiry_purchasemonth, enquiry_priorityenquiry_id, enquiry_occ_id,"
					+ " enquiry_lostcase1_id, enquiry_lostcase2_id, enquiry_lostcase3_id,"
					+ " enquiry_status_desc, crm_id, crm_desc,"
					// + " COALESCE(crm_followup_time, '') AS crm_followup_time,"
					+ " COALESCE(IF(crm_modified_id = '',crm_entry_time,crm_modified_time), '') AS 'crm_followup_time',"
					+ " crmtype_name, crmdays_entry_date,"
					+ " crmdays_crmtype_id, crmdays_daycount, "
					+ " COALESCE(crmfeedbacktype_name, '') AS crmfeedbacktype_name,"
					+ " COALESCE(enquiryexe.emp_name,'') as enquiryexe_name, "
					+ " COALESCE(manager.emp_name,'') as manager_name,"
					+ " COALESCE(crm.emp_name,'') AS crm_name,"
					+ " COALESCE(crm.emp_mobile1,'') AS crmemp_mobile1,"
					+ " COALESCE(crm.emp_email1,'') AS crmemp_email1,"
					+ " COALESCE(crmjobtitle.jobtitle_desc,'') AS crm_jobtitle,"
					+ " enquiry_id, enquiry_no, enquiry_dmsno, crm_crmdays_id,"
					+ " stage_name, status_name,"
					+ " COALESCE(soe_name,'') soe_name,"
					+ " COALESCE(sob_name,'') sob_name,"
					+ " customer_id, customer_name, custtype_name,"
					+ " cancelreason_name, crmdays_so_inactive,"
					+ " contact_id,"
					+ " COALESCE(concat(title_desc,' ', contact_fname,' ', contact_lname), '') AS contact_name, title_gender,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, contact_phone1, contact_phone2, contact_address,"
					+ " COALESCE(session.emp_name,'') AS exe_name,"
					+ " COALESCE(session.emp_email1,'') AS exe_email1, enquiry_date, item_name,"
					+ " COALESCE (so_id, 0) AS so_id,"
					+ " COALESCE (so_no, '') AS so_no,"
					+ " COALESCE (so_date, '') AS so_date,"
					+ " COALESCE (so_delivered_date, '') AS so_delivered_date,"
					+ " COALESCE (so_payment_date, '') AS so_payment_date,"
					+ " COALESCE (so_booking_amount, 0) AS so_booking_amount,"
					+ " COALESCE (so_promise_date, '') AS so_promise_date,"
					+ " COALESCE (so_grandtotal, '') AS so_grandtotal,"

					+ " COALESCE(so_ew_amount, 0) AS so_ew_amount,"
					+ " COALESCE (model_name, '') AS model_name, branch_name, "
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Paintwork',"
					// + " COALESCE (soopt.option_desc, '') AS sooptiondesc,"
					+ " COALESCE (fincomp_name, '') AS fincomp_name,"
					+ " COALESCE (vehstock_chassis_no, '') AS chassis_no,"
					+ " COALESCE (so_reg_no, '') AS reg_no,"
					+ " COALESCE (vehstock_engine_no, '') AS engine_no,"
					+ " COALESCE (occ_name, '') AS occ_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays on crmdays_id = crm_crmdays_id"
					+ " INNER JOIN axela_brand on brand_id = crmdays_brand_id"
					+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmdays_crmtype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = crm_enquiry_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_branch ON  branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp session on session.emp_id = " + emp_id + " "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp enquiryexe on enquiryexe.emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_custtype ON custtype_id = enquiry_custtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_cancelreason on cancelreason_id = crm_cancelreason_id"
					+ " LEFT JOIN axela_sales_crm_feedbacktype ON crmfeedbacktype_id = crm_crmfeedbacktype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager on manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp crm on crm.emp_id = crm_emp_id " // team_crm_emp_id
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle crmjobtitle on crmjobtitle.jobtitle_id = crm.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = crm_so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option enopt ON enopt.option_id = enquiry_option_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option soopt ON soopt.option_id = so_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ ON occ_id = enquiry_occ_id"
					+ " WHERE crm_id = " + crm_id + ""
					+ " GROUP BY enquiry_id";
			// SOP("StrSql--CRM Print----------------" + StrSql);
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
					PdfWriter.getInstance(document, new FileOutputStream(
							CachePath(comp_id) + "So_" + so_id + ".pdf"));
				}
				document.open();
				while (crs.next()) {
					if (crs.getString("title_gender").equals("1")) {
						salutation = "Sir";
					} else {
						salutation = "Madam";
					}
					PdfPCell cell;

					PdfPTable space_table = new PdfPTable(2);
					space_table.setWidthPercentage(100);

					cell = new PdfPCell(new Phrase("", header_font));
					cell.setBorderWidth(0);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(2);
					space_table.addCell(cell);
					document.add(space_table);

					PdfPTable sodetails_table = new PdfPTable(4);
					sodetails_table.setWidthPercentage(100);
					sodetails_table.setKeepTogether(true);

					cell = new PdfPCell(new Phrase(crs.getString("crmtype_name")
							+ " FOLLOW UP", header_font));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setColspan(4);
					sodetails_table.addCell(cell);
					// SOP("------" + crs.getString("crmdays_crmtype_id"));
					if (crs.getString("crmdays_crmtype_id").equals("1")) {
						cell = new PdfPCell(new Phrase("Enquiry ID: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								crs.getString("enquiry_id"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Enquiry DMS No.: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								crs.getString("enquiry_dmsno"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Enquiry No.: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								crs.getString("enquiry_no"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Enquiry Date: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								strToShortDate(crs.getString("enquiry_date")),
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

					}

					cell = new PdfPCell(new Phrase("Customer Name: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("customer_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Contact Name: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Mobile 1: ", normal_font));
					// cell.addElement(new Phrase("Mobile 1: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_mobile1"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Email 1: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_email1"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Mobile 2: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_mobile2"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Email 2: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("contact_email2"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Customer Type: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("custtype_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Extended Warranty: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							IndDecimalFormat(df.format(crs.getDouble("so_ew_amount"))), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					if (!crs.getString("crmdays_crmtype_id").equals("3")) {
						cell = new PdfPCell(
								new Phrase("Phone 1: ", normal_font));
						// cell.addElement(new Phrase("Phone 1: ",
						// normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								crs.getString("contact_phone1"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(
								new Phrase("Phone 2: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								crs.getString("contact_phone2"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("Model: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							unescapehtml(crs.getString("model_name")),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase("Variant: ",
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							unescapehtml(crs.getString("item_name")),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase("Colour: ", normal_font));
					// cell.addElement(new Phrase("Colour: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("Paintwork"),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase("Branch Name: ", normal_font));
					// cell.addElement(new Phrase("Branch Name: ",
					// normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(crs.getString("branch_name"),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(new Phrase(" Executive: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("enquiryexe_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);

					cell = new PdfPCell(
							new Phrase("Team Leader: ", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					sodetails_table.addCell(cell);
					cell = new PdfPCell(new Phrase(
							crs.getString("manager_name"), normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					sodetails_table.addCell(cell);
					if (!crs.getString("so_id").equals("0")) {

						if (crs.getString("crmdays_crmtype_id").equals("2")) {

							cell = new PdfPCell(new Phrase("Allotment No.: ",
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase("", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);
						}

						cell = new PdfPCell(new Phrase("Date of Booking: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								strToShortDate(crs.getString("so_date")),
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Booking Amount: ",
								normal_font));
						// cell.addElement(new Phrase("Booking Amount: ",
						// normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								IndDecimalFormat(df.format(crs
										.getDouble("so_booking_amount"))),
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Chassis No.: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								crs.getString("chassis_no"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Engine No.: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								crs.getString("engine_no"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Date Of Delivery: ",
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(new Phrase(
								strToShortDate(crs.getString("so_delivered_date")),
								normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						// cell = new PdfPCell(new Phrase("Occupation: ",
						// normal_font));
						// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						// sodetails_table.addCell(cell);
						// cell = new PdfPCell(new Phrase(
						// unescapehtml(crs.getString("occ_name")),
						// normal_font));
						// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						// sodetails_table.addCell(cell);
						//
						// cell = new PdfPCell(new Phrase("Contact Address: ",
						// normal_font));
						// cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						// sodetails_table.addCell(cell);
						// cell = new PdfPCell(new Phrase(
						// crs.getString("contact_address"),
						// normal_font));
						// cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						// sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase(
								"Contact date: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(
								new Phrase(strToShortDate(crs
										.getString("crm_followup_time")),
										normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);
						if (crs.getString("crmdays_so_inactive").equals("1"))
						{

							cell = new PdfPCell(new Phrase(
									"Car Cancel Reason", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(crs.getString("cancelreason_name"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);
						}
						else
						{
							cell = new PdfPCell(new Phrase(
									"", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase("", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);
						}
						if (!crs.getString("crmdays_crmtype_id").equals("2")) {

							cell = new PdfPCell(new Phrase(
									"SO Delivery Date: ", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(
									new Phrase(strToShortDate(crs
											.getString("so_delivered_date")),
											normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

							cell = new PdfPCell(new Phrase("SO Variant: ",
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									unescapehtml(crs.getString("item_name")),
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

							cell = new PdfPCell(new Phrase("Engine No.: ",
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									crs.getString("engine_no"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

							cell = new PdfPCell(new Phrase(
									"Vehicle Chassis No.: ", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									crs.getString("chassis_no"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

							cell = new PdfPCell(new Phrase(
									"Vehicle Register No.: ", normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									crs.getString("reg_no"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

							cell = new PdfPCell(new Phrase("Contact Address: ",
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									crs.getString("contact_address"),
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

							cell = new PdfPCell(new Phrase("Occupation: ",
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									crs.getString("occ_name"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

							cell = new PdfPCell(new Phrase("Bank Name: ",
									normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
							sodetails_table.addCell(cell);
							cell = new PdfPCell(new Phrase(
									crs.getString("fincomp_name"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							sodetails_table.addCell(cell);

						}
						cell = new PdfPCell(new Phrase(
								"Tentative Delivery Date: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(
								new Phrase(strToShortDate(crs
										.getString("so_promise_date")),
										normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);

						cell = new PdfPCell(new Phrase(
								"Customer Contact date: ", normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
						sodetails_table.addCell(cell);
						cell = new PdfPCell(
								new Phrase(strToLongDate(crs
										.getString("crmdays_entry_date")),
										normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						sodetails_table.addCell(cell);
						document.add(sodetails_table);
					}

					PdfPTable script_table = new PdfPTable(1);
					script_table.setWidthPercentage(100);
					script_table.setKeepTogether(true);

					StringBuilder PbfStr = new StringBuilder();
					PbfStr.append(crs.getString("crmdays_script"));
					cell = new PdfPCell(new Phrase(PbfStr.toString(),
							normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					script_table.addCell(cell);

					document.add(script_table);
					// int count = 0;
					String StrSql = "SELECT crmcf_title,"
							+ " COALESCE(crmcftrans_value, '') AS crmcftrans_value,"
							+ " COALESCE(crmcftrans_voc, '') AS crmcftrans_voc, crmcf_cftype_id"
							+ " FROM "
							+ compdb(comp_id)
							+ "axela_sales_crm"
							+ " INNER JOIN "
							+ compdb(comp_id)
							+ "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
							+ " INNER JOIN "
							+ compdb(comp_id)
							+ "axela_sales_crm_cf ON crmcf_crmdays_id = crmdays_id"
							+ " LEFT JOIN "
							+ compdb(comp_id)
							+ "axela_sales_crm_trans ON crmcftrans_crmcf_id = crmcf_id AND crmcftrans_crm_id = "
							+ crm_id + ""
							+ " WHERE 1=1 "
							+ " AND crmcf_active = 1"
							// + " AND crmcftrans_value =''"
							+ " AND crm_id = " + crm_id + ""
							+ " GROUP BY crmcf_id" + " ORDER BY crmcf_rank";
					CachedRowSet crs1 = processQuery(StrSql, 0);
					// SOP("StrSql--CRM Custom Fields Print-----" + StrSqlBreaker(StrSql));
					PdfPTable questions_table = new PdfPTable(3);
					questions_table.setWidthPercentage(100);
					questions_table.setKeepTogether(true);
					questions_table.setWidths(new int[]{40, 10, 15});
					if (crs1.isBeforeFirst()) {
						// cell = new PdfPCell(new Phrase("#", bold_font));
						// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						// questions_table.addCell(cell);
						cell = new PdfPCell(new Phrase("Questions", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						questions_table.addCell(cell);
						cell = new PdfPCell(new Phrase("Yes/No", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						questions_table.addCell(cell);
						cell = new PdfPCell(new Phrase("VOC", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						questions_table.addCell(cell);
						while (crs1.next()) {
							// count++;
							// cell = new PdfPCell(new Phrase(count + "",
							// normal_font));
							// cell.setHorizontalAlignment(Element.ALIGN_CENTER);
							// questions_table.addCell(cell);

							cell = new PdfPCell(new Phrase(
									crs1.getString("crmcf_title"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							questions_table.addCell(cell);
							if (crs1.getString("crmcf_cftype_id").equals("5")
									|| crs1.getString("crmcf_cftype_id").equals(
											"6")) {
								cell = new PdfPCell(new Phrase(strToShortDate(crs1.getString("crmcftrans_value")), normal_font));
							} else if (crs1.getString("crmcf_cftype_id").equals("3")) {
								if (crs1.getString("crmcftrans_value").equals("1")) {
									cell = new PdfPCell(new Phrase(
											"Yes", normal_font));
								} else {
									cell = new PdfPCell(new Phrase(
											"No", normal_font));
								}
							}
							else {
								cell = new PdfPCell(new Phrase(
										crs1.getString("crmcftrans_value"),
										normal_font));
							}
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							questions_table.addCell(cell);

							cell = new PdfPCell(new Phrase(crs1.getString("crmcftrans_voc"), normal_font));
							cell.setHorizontalAlignment(Element.ALIGN_LEFT);
							questions_table.addCell(cell);

						}
						crs1.close();

					}
					document.add(questions_table);
					if (!crs.getString("crmfeedbacktype_name").equals("")
							&& !crs.getString("crm_desc").equals("")) {
						// feedback table
						PdfPTable feedback_table = new PdfPTable(2);
						feedback_table.setWidthPercentage(100);
						feedback_table.setKeepTogether(true);
						feedback_table.setWidths(new int[]{7, 40});

						cell = new PdfPCell(new Phrase("Feedback", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("crm_desc"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						cell = new PdfPCell(new Phrase("Feedback Type", bold_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						cell = new PdfPCell(new Phrase(crs.getString("crmfeedbacktype_name"), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						feedback_table.addCell(cell);

						document.add(feedback_table);
					}

					PdfPTable sign_table = new PdfPTable(2);
					sign_table.setWidthPercentage(100);
					sign_table.setKeepTogether(true);
					if (crs.getString("crmdays_crmtype_id").equals("2")) {
						StringBuilder signStr = new StringBuilder();

						signStr.append("1.Please Bring Your Docket at the time of Delivery.\n\n");

						signStr.append("2.Thank you very much for giving your valuable time. We are agreeing to see you soon on the day of delivery..\n\n");

						signStr.append("3.We assure you of our committed services in the future.\n\n");
						cell = new PdfPCell(new Phrase(signStr.toString(), normal_font));
						cell.setHorizontalAlignment(Element.ALIGN_LEFT);
						cell.setColspan(2);
						cell.setBorder(0);
						sign_table.addCell(cell);
					}

					cell = new PdfPCell(new Phrase("(GM Signature)", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(50);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("(CCM Signature)", bold_font));
					cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
					cell.setFixedHeight(50);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("\n\nRegards", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					cell.setBorder(0);
					sign_table.addCell(cell);

					cell = new PdfPCell(new Phrase("\nCRM DEPT", normal_font));
					cell.setHorizontalAlignment(Element.ALIGN_LEFT);
					cell.setColspan(2);
					cell.setBorder(0);
					sign_table.addCell(cell);

					document.add(sign_table);
				}
				document.close();
			} else {
				response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	public String GetSalutation(String date) {
		String salutation = "";
		int time = 0;
		time = Integer.parseInt(date.substring(8, 10));
		if (time < 12 && time >= 0) {
			salutation = "Good Morning";
		} else if (time < 16 && time >= 12) {
			salutation = "Good Afternoon";
		} else if (time < 24 && time >= 16) {
			salutation = "Good Evening";
		}
		return salutation;
	}
}
