package axela.service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_PSF_Update extends Connect {

	public String update = "";
	public String add = "";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "", ticketmsg = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String enquiry_id = "0";
	public String enquiry_no = "";
	public String enquiry_dmsno = "";
	public String jcpsf_id = "";
	public String jcpsf_mobile1 = "";
	public String jcpsf_mobile2 = "";
	public String jcpsf_mobile3 = "", jcpsf_mobile4 = "", jcpsf_mobile5 = "", jcpsf_mobile6 = "";
	public String jcpsf_email1 = "";
	public String jcpsf_email2 = "";
	public String crm_lostcase1_id = "0";
	public String crm_lostcase2_id = "0";
	public String crm_lostcase3_id = "0";
	public String jcpsf_desc = "";
	public String jcpsf_psffeedbacktype_id = "0";
	public String jcpsf_entry_id = "0";
	public String crm_entry_date = "";
	public String jcpsf_modified_id = "0";
	public String jcpsf_modified_time = "";
	public String crmtype_name = "";
	public String return_perm = "0";
	public String psfdays_lostfollowup = "", crmdays = "";
	public String jcpsf_psfdays_id = "";
	public String enquiry_date, enquiry_close_date = "";
	public String soe_name = "";
	public String sob_name = "";
	public String lostcase1_name = "";
	public String lostcase2_name = "";
	public String lostcase3_name = "";
	public String exe_name = "", enquiryexe_name = "";
	public String exe_email1 = "";
	public String manager_email1 = "", crmemp_email1 = "", enquiryexe_email1 = "";
	public String manager_name = "", crm_name = "";
	public String manager_mobile1 = "", crmemp_mobile1 = "";
	public String manager_jobtitle = "", crm_jobtitle = "";
	public String status_name = "";
	public String stage_name = "";
	public String so_id = "0";
	public String so_no = "";
	public String so_date = "";
	public String so_delivered_date = "";
	public String so_payment_date = "";
	public String so_booking_amount = "";
	public String so_grandtotal = "";
	public String preownedmodel_name = "";
	public String customer_id = "0";
	public String customer_name = "";
	public String contact_id = "0";
	public String contact_name = "";
	public String contactname = "";
	public String contact_mobile1 = "";
	public String contactmobile1 = "";
	public String contactmobile2 = "";
	public String contact_mobile2 = "";
	public String contact_mobile3 = "", contact_mobile4 = "", contact_mobile5 = "", contact_mobile6 = "";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_address = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String variant_name = "";
	public String jcpsf_emp_id = "";
	public String ExeAccess = "", StrScript = "";
	public String jc_id = "0";
	public String veh_id = "0";
	public String jcpsf_ticket_emp_id = "0";
	public String jcpsf_tickettype_id = "0";
	public String jcpsf_jcpsfconcern_id = "0";
	public String jcpsf_nextfollowuptime = "";
	public String jc_branch_id = "";
	public String jcpsf_jc_id = "";
	public String enquiry_emp_id = "";
	public String psfdays_crmtype_id = "";
	public String psfdays_desc = "";
	public String veh_classified = "";
	public String jcpsf_satisfied = "";
	public String jcexe_empname = "";
	public String jcexe_empemail1 = "";
	public String saexe_emp_mobile1 = "";
	public String curryear = "", year1 = "", branch_ticket_email = "";
	public String branch_name = "", option_name = "", sooptiondesc = "";
	public String fincomp_name = "", engine_no = "", chassis_no = "", reg_no = "", occ_name = "";
	public String branch_brand_id = "";

	public String branch_email1 = "";
	public String config_email_enable = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_sms_enable = "";
	public String branch_jcpsf_email = "";
	public String branch_jcpsf_mobile = "";

	// ///Jcpsf feilds
	public String psfdays_satisfied_email_enable = "";
	public String psfdays_satisfied_email_sub = "";
	public String psfdays_satisfied_email_format = "";
	public String psfdays_satisfied_email_exe_sub = "";
	public String psfdays_satisfied_email_exe_format = "";
	public String psfdays_satisfied_sms_enable = "";
	public String psfdays_satisfied_sms_format = "";
	public String psfdays_satisfied_sms_exe_format = "";
	public String psfdays_dissatisfied_email_enable = "";
	public String psfdays_dissatisfied_email_sub = "";
	public String psfdays_dissatisfied_email_format = "";
	public String psfdays_dissatisfied_email_exe_sub = "";
	public String psfdays_dissatisfied_email_exe_format = "";
	public String psfdays_dissatisfied_sms_enable = "";
	public String psfdays_dissatisfied_sms_format = "";
	public String psfdays_dissatisfied_sms_exe_format = "";
	public String send_contact_email = "";

	// contact able
	public String psfdays_contactable_email_enable = "";
	public String psfdays_contactable_email_sub = "";
	public String psfdays_contactable_email_format = "";
	public String psfdays_contactable_email_exe_sub = "";
	public String psfdays_contactable_email_exe_format = "";
	public String psfdays_contactable_sms_enable = "";
	public String psfdays_contactable_sms_format = "";
	public String psfdays_contactable_sms_exe_format = "";
	// non contact able
	public String psfdays_noncontactable_email_enable = "";
	public String psfdays_noncontactable_email_sub = "";
	public String psfdays_noncontactable_email_format = "";
	public String psfdays_noncontactable_email_exe_sub = "";
	public String psfdays_noncontactable_email_exe_format = "";
	public String psfdays_noncontactable_sms_enable = "";
	public String psfdays_noncontactable_sms_format = "";
	public String psfdays_noncontactable_sms_exe_format = "";

	public String ticket_emp_email1 = "";
	public String ticket_emp_email2 = "";
	public String ticket_emp_mobile1 = "";
	public String ticket_emp_mobile2 = "";

	public String config_ticket_jcpsf_email_sub = "";
	public String config_ticket_jcpsf_email_format = "";
	public String preious_ticket_id = "0";
	public String ticket_id = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws SQLException {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				jcpsf_id = CNumeric(PadQuotes(request.getParameter("jcpsf_id")));
				curryear = SplitYear(ToLongDate(kknow()));
				year1 = (Integer.parseInt(curryear) + 10) + "";
				SOP("coming");
				PopulateFields(response);
				if ("Update Follow-up".equals(updateB)) {
					GetValues(request);
					PopulateConfigDetails(comp_id);
					if (add.equals("add")) {
						return_perm = ReturnPerm(comp_id, "emp_service_jobcard_add", request);
					} else {
						return_perm = ReturnPerm(comp_id, "emp_service_jobcard_edit", request);
					}
					if (return_perm.equals("1")) {
						CheckForm(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							UpdateCRMFollowup();
							PSFCustomFieldUpdate(comp_id, jcpsf_id, "yes", request);

							// SOP("Transaction started...");
							// conntx = connectDB();
							// conntx.setAutoCommit(false);
							// stmttx = conntx.createStatement();

							if (jcpsf_entry_id.equals("0")) {
								SendCommunications();
							}

							if (jcpsf_satisfied.equals("2") && preious_ticket_id.equals("0")) {
								AddTicketDetails();
							}
							if (!ticket_id.equals("")) {
								ticketmsg = "Ticket ID: " + ticket_id + " is Raised!";
							}

							response.sendRedirect(response.encodeRedirectURL("jobcard-dash.jsp?jc_id=" + jcpsf_jc_id + "&jcpsfmsg=PSF Follow-up updated successfully!"
									+ "&ticketmsg=" + ticketmsg
									+ "#tabs-9"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateFields(HttpServletResponse response) throws SQLException, IOException {
		String script = "";

		script = "REPLACE(psfdays_script, '[SALUTATION]'," + "'" + GetSalutation(ToLongDate(kknow())) + "')";
		script = "REPLACE(" + script + ",'[CONTACTNAME]',concat(title_desc,' ', contact_fname,' ', contact_lname))";
		script = "REPLACE(" + script + ",'[EXENAME]',jcexe.emp_name) as psfdays_script";

		StrSql = "SELECT " + script + " ,"
				+ " jcpsf_jc_id, jc_branch_id, COALESCE (branch_ticket_email, '') AS branch_ticket_email,"
				+ " jcpsf_desc,	psfdays_daycount, psfdays_desc,	jcpsf_psffeedbacktype_id,"
				+ " jcpsf_satisfied, COALESCE (entryemp.emp_id, 0) AS crmentryid,"
				+ " COALESCE (entryemp.emp_name, '') AS crmentryname,"
				+ " COALESCE (jcpsf_entry_time, '') AS jcpsf_entry_time,"
				+ " COALESCE (modifiedemp.emp_id, 0) AS crmmodifiedid,"
				+ " COALESCE (modifiedemp.emp_name, '') AS modifiedname,"
				+ " COALESCE (jcpsf_modified_time, '') AS jcpsf_modified_time, jcpsf_psfdays_id,"
				+ " jc_id, jc_veh_id, variant_name, jcpsf_nextfollowuptime,"
				+ " customer_id, customer_name,	contact_id,"
				+ " COALESCE(concat(title_desc,' ',contact_fname, ' ', contact_lname),'') AS contact_name,"
				+ " contact_address, title_gender, contact_mobile1,	contact_mobile2,"
				+ " contact_mobile3, contact_mobile4, contact_mobile5, contact_mobile6,"
				+ " contact_email1,	contact_email2,	contact_phone1,	contact_phone2,"
				+ " COALESCE(SESSION .emp_name,'') AS exe_name,"
				+ " COALESCE(SESSION .emp_email1,'') AS exe_email1,"
				+ " COALESCE(jcexe.emp_name,'') AS jcexe_empname,"
				+ " COALESCE(jcexe.emp_email1,'') AS jcexe_empemail1,"
				+ " COALESCE(saexe.emp_mobile1,'') AS saexe_emp_mobile1, veh_classified,"
				+ " branch_name, branch_ticket_email, jcpsf_ticket_emp_id, jcpsf_jcpsfconcern_id,branch_brand_id"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
				+ " INNER JOIN axela_brand ON brand_id = branch_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp saexe on saexe.emp_id = jc_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp SESSION ON SESSION.emp_id = 1"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp jcexe on jcexe.emp_id = jcpsf_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp ON entryemp.emp_id = jcpsf_entry_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp ON modifiedemp.emp_id = jcpsf_modified_id"
				+ " WHERE jcpsf_id = " + jcpsf_id
				+ " GROUP BY"
				+ " jcpsf_id";

		// SOP("StrSql--PSF-----" + StrSql);
		CachedRowSet crs1 = processQuery(StrSql, 0);
		if (crs1.isBeforeFirst()) {
			while (crs1.next()) {
				StrScript = crs1.getString("psfdays_script");
				branch_name = crs1.getString("branch_name");
				branch_ticket_email = crs1.getString("branch_ticket_email");
				jc_id = crs1.getString("jc_id");
				veh_id = crs1.getString("jc_veh_id");
				jcexe_empname = crs1.getString("jcexe_empname");
				jcexe_empemail1 = crs1.getString("jcexe_empemail1");
				saexe_emp_mobile1 = crs1.getString("saexe_emp_mobile1");
				variant_name = crs1.getString("variant_name");
				jcpsf_desc = crs1.getString("jcpsf_desc");
				jc_branch_id = crs1.getString("jc_branch_id");
				jcpsf_jc_id = crs1.getString("jcpsf_jc_id");
				psfdays_desc = crs1.getString("psfdays_daycount")
						+ crs1.getString("psfdays_desc");
				jcpsf_psffeedbacktype_id = crs1.getString("jcpsf_psffeedbacktype_id");
				jcpsf_satisfied = crs1.getString("jcpsf_satisfied");
				jcpsf_entry_id = crs1.getString("crmentryid");
				jcpsf_ticket_emp_id = crs1.getString("jcpsf_ticket_emp_id");
				jcpsf_jcpsfconcern_id = crs1.getString("jcpsf_jcpsfconcern_id");
				jcpsf_nextfollowuptime = strToLongDate(crs1.getString("jcpsf_nextfollowuptime"));
				branch_brand_id = crs1.getString("branch_brand_id");
				veh_classified = crs1.getString("veh_classified");
				if (!jcpsf_entry_id.equals("0")) {
					entry_by = "<a href=../portal/executive-summary.jsp?emp_id="
							+ crs1.getInt("crmentryid")
							+ ">"
							+ crs1.getString("crmentryname") + "</a>";
					entry_date = strToLongDate(crs1.getString("jcpsf_entry_time"));
				}
				jcpsf_modified_id = crs1.getString("crmmodifiedid");
				if (!jcpsf_modified_id.equals("0")) {
					modified_by = "<a href=../portal/executive-summary.jsp?emp_id="
							+ crs1.getInt("crmmodifiedid")
							+ ">"
							+ crs1.getString("modifiedname") + "</a>";
					modified_date = strToLongDate(crs1
							.getString("jcpsf_modified_time"));
				}
				jcpsf_psfdays_id = crs1.getString("jcpsf_psfdays_id");
				customer_id = crs1.getString("customer_id");
				customer_name = "<a href=../customer/customer-list.jsp?customer_id="
						+ crs1.getString("customer_id")
						+ " target=_blank>"
						+ crs1.getString("customer_name") + "</a>";
				contact_id = crs1.getString("contact_id");
				contact_name = "<a href=../customer/customer-contact-list.jsp?contact_id="
						+ crs1.getString("contact_id")
						+ " target=_blank>"
						+ crs1.getString("contact_name") + "</a>";
				contactname = crs1.getString("contact_name");

				contact_mobile1 = crs1.getString("contact_mobile1");
				contact_mobile2 = crs1.getString("contact_mobile2");
				contact_mobile3 = crs1.getString("contact_mobile3");
				contact_mobile4 = crs1.getString("contact_mobile4");
				contact_mobile5 = crs1.getString("contact_mobile5");
				contact_mobile6 = crs1.getString("contact_mobile6");

				contact_email1 = crs1.getString("contact_email1");
				contact_email2 = crs1.getString("contact_email2");
				contact_phone1 = crs1.getString("contact_phone1");
				contact_phone2 = crs1.getString("contact_phone2");
				contact_address = crs1.getString("contact_address");
				// variant_name = crs1.getString("variant_name");
				// crmtype_name = crs1.getString("crmtype_name");

				jcpsf_mobile1 = crs1.getString("contact_mobile1");
				jcpsf_mobile2 = crs1.getString("contact_mobile2");
				jcpsf_mobile3 = crs1.getString("contact_mobile3");
				jcpsf_mobile4 = crs1.getString("contact_mobile4");
				jcpsf_mobile5 = crs1.getString("contact_mobile5");
				jcpsf_mobile6 = crs1.getString("contact_mobile6");
				jcpsf_email1 = crs1.getString("contact_email1");
				jcpsf_email2 = crs1.getString("contact_email2");
				jcpsf_desc = crs1.getString("jcpsf_desc");
			}
		} else {
			msg += "Invalid PSF!";
			response.sendRedirect(response
					.encodeRedirectURL("../portal/error.jsp?msg=" + msg));
		}
		crs1.close();
	}

	protected void GetValues(HttpServletRequest request) {
		add = PadQuotes(request.getParameter("txt_add"));
		crm_lostcase1_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_lostcase1_id")));
		crm_lostcase2_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_lostcase2_id")));
		crm_lostcase3_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_lostcase3_id")));
		jcpsf_desc = PadQuotes(request.getParameter("txt_jcpsf_desc"));
		jcpsf_psffeedbacktype_id = PadQuotes(request
				.getParameter("dr_feedbacktype"));
		StrScript = unescapehtml(PadQuotes(request
				.getParameter("txt_StrScript")));
		jcpsf_mobile1 = PadQuotes(request.getParameter("txt_jcpsf_mobile1"));
		jcpsf_mobile2 = PadQuotes(request.getParameter("txt_jcpsf_mobile2"));

		jcpsf_mobile3 = PadQuotes(request.getParameter("txt_jcpsf_mobile3"));
		jcpsf_mobile4 = PadQuotes(request.getParameter("txt_jcpsf_mobile4"));
		jcpsf_mobile5 = PadQuotes(request.getParameter("txt_jcpsf_mobile5"));
		jcpsf_mobile6 = PadQuotes(request.getParameter("txt_jcpsf_mobile6"));

		jcpsf_email1 = PadQuotes(request.getParameter("txt_jcpsf_email1"));
		jcpsf_email2 = PadQuotes(request.getParameter("txt_jcpsf_email2"));
		jcpsf_satisfied = PadQuotes(request.getParameter("dr_jcpsf_satisfied"));
		jcpsf_ticket_emp_id = PadQuotes(request.getParameter("dr_ticketowner_id"));
		jcpsf_jcpsfconcern_id = PadQuotes(request.getParameter("dr_jcpsfconcern_id"));
		jcpsf_nextfollowuptime = PadQuotes(request.getParameter("txt_jcpsf_nextfollowuptime"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		// jcpsf_entry_id = PadQuotes(request.getParameter("jcpsf_entry_id"));
		jcpsf_modified_id = PadQuotes(request.getParameter("jcpsf_modified_id"));
	}

	public void UpdateCRMFollowup() throws SQLException {
		if (msg.equals("")) {
			try {
				// creating the connection for crm details update
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_psf"
						+ " SET" + " jcpsf_mobile1 = '" + jcpsf_mobile1 + "',"
						+ " jcpsf_mobile2 = '" + jcpsf_mobile2 + "',"
						+ " jcpsf_email1 = '" + jcpsf_email1 + "',"
						+ " jcpsf_email2 = '" + jcpsf_email2 + "',"
						// + " crm_lostcase1_id = " + crm_lostcase1_id+ ","
						// + " crm_lostcase2_id = " + crm_lostcase2_id+ ","
						// + " crm_lostcase3_id = " + crm_lostcase3_id+ ","
						+ " jcpsf_desc = '" + jcpsf_desc + "',"
						+ " jcpsf_psffeedbacktype_id = " + jcpsf_psffeedbacktype_id + ","
						+ " jcpsf_satisfied = '" + jcpsf_satisfied + "',"
						+ " jcpsf_ticket_emp_id = " + jcpsf_ticket_emp_id + ","
						+ " jcpsf_jcpsfconcern_id = " + jcpsf_jcpsfconcern_id + ","
						+ " jcpsf_nextfollowuptime = '" + ConvertLongDateToStr(jcpsf_nextfollowuptime) + "',";
				if (jcpsf_entry_id.equals("0")) {
					StrSql = StrSql + " jcpsf_entry_id = " + emp_id + ","
							+ " jcpsf_entry_time = '" + ToLongDate(kknow()) + "'";
				} else {
					StrSql = StrSql + " jcpsf_modified_id = " + emp_id + ","
							+ " jcpsf_modified_time = '" + ToLongDate(kknow())
							+ "'";
				}
				StrSql = StrSql + " WHERE jcpsf_id = " + jcpsf_id + "";

				stmttx.addBatch(StrSql);

				if (!contact_mobile1.equals(jcpsf_mobile1)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Mobile 1" + "',"
							+ " '" + contact_mobile1 + "',"
							+ " '" + jcpsf_mobile1 + "')";
					stmttx.addBatch(StrSql);
					// SOP("strsqlll======22222====" + StrSql);
				}
				if (!contact_mobile2.equals(jcpsf_mobile2)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Mobile 2" + "',"
							+ " '" + contact_mobile2 + "',"
							+ " '" + jcpsf_mobile2 + "')";
					stmttx.addBatch(StrSql);
				}
				if (!contact_mobile3.equals(jcpsf_mobile3)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Mobile 3" + "',"
							+ " '" + contact_mobile3 + "',"
							+ " '" + jcpsf_mobile3 + "')";
					stmttx.addBatch(StrSql);
				}
				if (!contact_mobile4.equals(jcpsf_mobile4)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Mobile 4" + "',"
							+ " '" + contact_mobile4 + "',"
							+ " '" + jcpsf_mobile4 + "')";
					stmttx.addBatch(StrSql);
				}
				if (!contact_mobile5.equals(jcpsf_mobile5)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Mobile 5" + "',"
							+ " '" + contact_mobile5 + "',"
							+ " '" + jcpsf_mobile5 + "')";
					stmttx.addBatch(StrSql);
				}
				if (!contact_mobile6.equals(jcpsf_mobile6)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Mobile 6" + "',"
							+ " '" + contact_mobile6 + "',"
							+ " '" + jcpsf_mobile6 + "')";
					stmttx.addBatch(StrSql);
				}
				if (!contact_email1.equals(jcpsf_email1)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Email 1" + "',"
							+ " '" + contact_email1 + "',"
							+ " '" + jcpsf_email1 + "')";
					stmttx.addBatch(StrSql);
				}
				if (!contact_email2.equals(jcpsf_email2)) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
							+ " (history_jc_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + jc_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + "Contact Email 2" + "',"
							+ " '" + contact_email2 + "',"
							+ " '" + jcpsf_email2 + "')";
					stmttx.addBatch(StrSql);
				}

				// //update email for contact/////
				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " SET" + " contact_mobile1 = '" + jcpsf_mobile1 + "',"
						+ " contact_mobile2 = '" + jcpsf_mobile2 + "',"
						+ " contact_mobile3 = '" + jcpsf_mobile3 + "',"
						+ " contact_mobile4 = '" + jcpsf_mobile4 + "',"
						+ " contact_mobile5 = '" + jcpsf_mobile5 + "',"
						+ " contact_mobile6 = '" + jcpsf_mobile6 + "',"
						+ " contact_email1 = '" + jcpsf_email1 + "',"
						+ " contact_email2 = '" + jcpsf_email2 + "'"
						+ " WHERE contact_id = " + contact_id + "";
				// SOP("StrSql = " + StrSql);
				stmttx.addBatch(StrSql);
				stmttx.executeBatch();
				conntx.commit();

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
				}
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		msg += PSFCustomFieldValidate(comp_id, jcpsf_id, "2", request);
		SOP("coming....");
		if (jcpsf_psffeedbacktype_id.equals("0")) {
			msg = msg + "<br>Select Feedback Type!";
		}
		if (!jcpsf_mobile1.equals("")) {
			if (!IsValidMobileNo11(jcpsf_mobile1)) {
				msg = msg + "<br>Enter Valid Contact Mobile 1!";
			}
		}
		if (!jcpsf_mobile2.equals("")) {
			if (!IsValidMobileNo11(jcpsf_mobile2)) {
				msg = msg + "<br>Enter Valid Contact Mobile 2!";
			}
		}
		if (!jcpsf_email1.equals("")) {
			if (!IsValidEmail(jcpsf_email1)) {
				msg = msg + "<br>Enter Valid Contact Email 1 !";
			}
		}
		if (!jcpsf_email2.equals("")) {
			if (!IsValidEmail(jcpsf_email2)) {
				msg = msg + "<br>Enter Valid Contact Email 2!";
			}
		}
		// if(psfdays_lostfollowup.equals("1") &&
		// jcpsf_psffeedbacktype_id.equals("1")) {
		//
		// if(crm_lostcase1_id.equals("0")) {
		// msg = msg + "<br>Select Lost Case 1!";
		// }
		// if(crm_lostcase2_id.equals("0")) {
		// msg = msg + "<br>Select Lost Case 2!";
		// }
		// if(crm_lostcase3_id.equals("0")) {
		// msg = msg + "<br>Select Lost Case 3!";
		// }
		// } else {
		// crm_lostcase1_id = "0";
		// crm_lostcase2_id = "0";
		// crm_lostcase3_id = "0";
		// }
		if (jcpsf_desc.equals("")) {
			msg = msg + "<br>Enter Feedback!";
		} else if (jcpsf_satisfied.equals("2")) {
			if (jcpsf_desc.length() < 5) {
				msg = msg + "<br>Feedback should have atleast five characters!";
			}
			// jcpsf_desc = jcpsf_desc.substring(0, 4999);
		}

		if (jcpsf_satisfied.equals("2")) {
			if (jcpsf_jcpsfconcern_id.equals("0")) {
				msg = msg + "<br>Select Service Concern !";
			}
			if (jcpsf_ticket_emp_id.equals("0")) {
				msg = msg + "<br>Select Ticket Owner!";
			}
		}

		if (jcpsf_psffeedbacktype_id.equals("1")) {
			if (jcpsf_satisfied.equals("0")) {
				msg = msg + "<br>Select Overall Experience!";
			}
		}
		if (!jcpsf_psffeedbacktype_id.equals("1")) {
			jcpsf_satisfied = "0";
			jcpsf_jcpsfconcern_id = "0";
			jcpsf_ticket_emp_id = "0";
		}

		StrSql = "SELECT ticket_id FROM " + compdb(comp_id) + "axela_service_ticket"
				+ " WHERE 1 = 1"
				+ " AND ticket_jcpsf_id = " + jcpsf_id
				+ " AND ticket_ticketstatus_id IN (1, 2, 5)";
		preious_ticket_id = CNumeric(ExecuteQuery(StrSql));

		if (jcpsf_satisfied.equals("1")) {
			jcpsf_jcpsfconcern_id = "0";
			jcpsf_ticket_emp_id = "0";
			// SOP("Str====" + StrSql);

			if (!preious_ticket_id.equals("0")) {
				msg += "<br>Ticket ID " + preious_ticket_id + " is Already Open!";
			}
		}
	}

	public void SendCommunications() throws Exception {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();

			if (!contact_email2.equals("") && !contact_email1.equals("")) {
				send_contact_email = contact_email1 + "," + contact_email2;
			} else if (!contact_email1.equals("")) {
				send_contact_email = contact_email1;
			} else if (!contact_email2.equals("")) {
				send_contact_email = contact_email2;
			}
			// SOP("saexe_emp_mobile1==" + saexe_emp_mobile1);
			if (jcpsf_psffeedbacktype_id.equals("1")) {
				if (comp_email_enable.equals("1")
						&& config_email_enable.equals("1")
						&& !branch_email1.equals("")
						&& psfdays_contactable_email_enable.equals("1")) {

					if (!contact_email1.equals("")
							&& !psfdays_contactable_email_format.equals("")
							&& !psfdays_contactable_email_sub.equals("")) {
						SendEmail("customer", jcpsf_psffeedbacktype_id, "0", comp_id);
					}

					if (!branch_email1.equals("")
							&& !psfdays_contactable_email_exe_format.equals("")
							&& !psfdays_contactable_email_exe_sub.equals("")) {
						SendEmail("executive", jcpsf_psffeedbacktype_id, "0", comp_id);
					}
				}

				if (comp_sms_enable.equals("1")
						&& config_sms_enable.equals("1")
						&& psfdays_contactable_sms_enable.equals("1")) {

					if (!psfdays_contactable_sms_format.equals("")) {
						if (!jcpsf_mobile1.equals("")) {
							SendSMS("customer", jcpsf_psffeedbacktype_id, "0", jcpsf_mobile1);
						}
						if (!jcpsf_mobile2.equals("")) {
							SendSMS("customer", jcpsf_psffeedbacktype_id, "0", jcpsf_mobile2);
						}
					}

					if (!psfdays_contactable_sms_exe_format.equals("")) {
						if (!saexe_emp_mobile1.equals("")) {
							SendSMS("executive", jcpsf_psffeedbacktype_id, "0", saexe_emp_mobile1);
						}
					}
				}
			}

			if (!jcpsf_psffeedbacktype_id.equals("0") && !jcpsf_psffeedbacktype_id.equals("1")
					&& !jcpsf_psffeedbacktype_id.equals("3")) {

				if (comp_email_enable.equals("1")
						&& config_email_enable.equals("1")
						&& !branch_email1.equals("")
						&& psfdays_noncontactable_email_enable.equals("1")) {

					if (!contact_email1.equals("")
							&& !psfdays_noncontactable_email_format.equals("")
							&& !psfdays_noncontactable_email_sub.equals("")) {
						SendEmail("customer", jcpsf_psffeedbacktype_id, "0", comp_id);
					}

					if (!branch_email1.equals("")
							&& !psfdays_noncontactable_email_exe_format.equals("")
							&& !psfdays_noncontactable_email_exe_sub.equals("")) {
						SendEmail("executive", jcpsf_psffeedbacktype_id, "0", comp_id);
					}
				}
				if (comp_sms_enable.equals("1")
						&& config_sms_enable.equals("1")
						&& psfdays_noncontactable_sms_enable.equals("1")) {

					if (!psfdays_noncontactable_sms_format.equals("")) {

						if (!jcpsf_mobile1.equals("")) {
							SendSMS("customer", jcpsf_psffeedbacktype_id, "0", jcpsf_mobile1);
						}
						if (!jcpsf_mobile2.equals("")) {
							SendSMS("customer", jcpsf_psffeedbacktype_id, "0", jcpsf_mobile2);
						}
					}

					if (!psfdays_noncontactable_sms_exe_format.equals("")) {
						if (!saexe_emp_mobile1.equals("")) {
							SendSMS("executive", jcpsf_psffeedbacktype_id, "0", saexe_emp_mobile1);
						}
					}
				}
			}
			// SOP("comp_email_enable==" + comp_email_enable);
			// SOP("config_email_enable==" + config_email_enable);
			// SOP("branch_email1==" + branch_email1);
			// SOP("psfdays_satisfied_email_enable==" + psfdays_satisfied_email_enable);
			// SOP("psfdays_satisfied_email_format==" + psfdays_satisfied_email_format);
			// SOP("psfdays_satisfied_email_sub==" + psfdays_satisfied_email_sub);
			// SOP("branch_jcpsf_email==" + branch_jcpsf_email);
			// SOP("branch_email1==" + branch_email1);
			// SOP("psfdays_satisfied_email_exe_format==" + psfdays_satisfied_email_exe_format);
			if (jcpsf_satisfied.equals("1")) {
				if (comp_email_enable.equals("1") && config_email_enable.equals("1")
						&& !branch_email1.equals("") && psfdays_satisfied_email_enable.equals("1")) {

					if (!contact_email1.equals("")
							&& !psfdays_satisfied_email_format.equals("")
							&& !psfdays_satisfied_email_sub.equals("")) {
						SendEmail("customer", "0", jcpsf_satisfied, comp_id);
					}

					if (!branch_email1.equals("")
							&& !psfdays_satisfied_email_exe_format.equals("")
							&& !psfdays_satisfied_email_exe_sub.equals("")) {
						SendEmail("executive", "0", jcpsf_satisfied, comp_id);
					}
				}

				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
						&& psfdays_satisfied_sms_enable.equals("1")) {

					if (!psfdays_satisfied_sms_format.equals("")) {

						if (!jcpsf_mobile1.equals("")) {
							SendSMS("customer", "0", jcpsf_satisfied, jcpsf_mobile1);
						}
						if (!jcpsf_mobile2.equals("")) {
							SendSMS("customer", "0", jcpsf_satisfied, jcpsf_mobile2);
						}
					}
					if (!psfdays_satisfied_sms_exe_format.equals("")) {
						if (!saexe_emp_mobile1.equals("")) {
							// SendSMS("executive", "0", jcpsf_satisfied, saexe_emp_mobile1);
						}
					}
				}

			}

			if (jcpsf_satisfied.equals("2")) {
				if (comp_email_enable.equals("1") && config_email_enable.equals("1")
						&& !branch_email1.equals("") && psfdays_dissatisfied_email_enable.equals("1")) {

					if (!contact_email1.equals("")
							&& !psfdays_dissatisfied_email_format.equals("")
							&& !psfdays_dissatisfied_email_sub.equals("")) {
						SendEmail("customer", "0", jcpsf_satisfied, comp_id);
					}

					if (!branch_jcpsf_email.equals("") && !branch_email1.equals("")
							&& !psfdays_dissatisfied_email_exe_format.equals("")
							&& !psfdays_dissatisfied_email_exe_sub.equals("")) {
						SendEmail("executive", "0", jcpsf_satisfied, comp_id);
					}
				}

				if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
						&& psfdays_dissatisfied_sms_enable.equals("1")) {

					if (!psfdays_dissatisfied_sms_format.equals("")) {

						if (!jcpsf_mobile1.equals("")) {
							SendSMS("customer", "0", jcpsf_satisfied, jcpsf_mobile1);
						}
						if (!jcpsf_mobile2.equals("")) {
							SendSMS("customer", "0", jcpsf_satisfied, jcpsf_mobile2);
						}
					}
					if (!psfdays_dissatisfied_sms_exe_format.equals("")) {
						if (!saexe_emp_mobile1.equals("")) {
							SendSMS("executive", "0", jcpsf_satisfied, saexe_emp_mobile1);
						}
					}
				}
			}
			conntx.commit();
			// SOP("Transaction commit...");
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("AxelaAuto====" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
			// msg = "<br>Transaction Error!";
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}

	}

	public void AddTicketDetails() throws Exception {
		try {

			Ticket_Add tkt = new Ticket_Add();
			tkt.comp_id = comp_id;
			tkt.emp_id = emp_id;
			tkt.ticket_branch_id = jc_branch_id;
			tkt.ticket_customer_id = customer_id;
			tkt.ticket_contact_id = contact_id;

			if (!jcpsf_email1.equals("")) {
				tkt.contact_email1 = jcpsf_email1;
			}
			if (!jcpsf_email2.equals("")) {
				tkt.contact_email2 = jcpsf_email2;
			}

			if (!enquiryexe_email1.equals("")) {
				tkt.ticket_cc += "" + enquiryexe_email1 + ",";
			}
			if (!crmemp_email1.equals("")) {
				tkt.ticket_cc += crmemp_email1 + ",";
			}
			if (!manager_email1.equals("")) {
				tkt.ticket_cc += manager_email1 + ",";
			}
			if (!branch_ticket_email.equals("")) {
				tkt.ticket_cc += branch_ticket_email + ",";
			}
			if (!branch_jcpsf_email.equals("")) {
				tkt.ticket_cc += branch_jcpsf_email + "";
			}
			if (!jcpsf_mobile1.equals("")) {
				tkt.contact_mobile1 = jcpsf_mobile1;
			}
			if (!jcpsf_mobile1.equals("")) {
				tkt.contact_mobile2 = jcpsf_mobile2;
			}

			tkt.ticket_emp_id = jcpsf_ticket_emp_id;
			tkt.ticket_ticketsource_id = "1";
			tkt.ticket_report_time = ToLongDate(kknow());
			tkt.ticket_ticketstatus_id = "1";
			tkt.ticket_priorityticket_id = "1";
			tkt.ticket_ticket_dept_id = "2"; // service
			tkt.ticket_subject = "Dis-satisfied CRM Call";
			tkt.ticket_desc = jcpsf_desc;
			tkt.customer_branch_id = jc_branch_id;
			// SOP("veh_id=====" + veh_id);
			tkt.ticket_veh_id = veh_id;
			tkt.ticket_jc_id = jc_id;
			tkt.ticket_jcpsf_id = jcpsf_id;
			tkt.ticket_tickettype_id = "4";
			tkt.ticket_entry_id = emp_id;
			tkt.ticket_entry_date = ToLongDate(kknow());
			tkt.branch_brand_id = branch_brand_id;
			tkt.PopulateConfigDetails(comp_id);
			tkt.AddFields(comp_id);

			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			ticket_id = tkt.ticket_id;

			if (!ticket_id.equals("0")
					&& !config_ticket_jcpsf_email_sub.equals("")
					&& !config_ticket_jcpsf_email_format.equals("")) {
				SendJCPSFTicketEmail();
			}
			// commit for email transaction
			conntx.commit();

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("AxelaAuto====" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
			// msg = "<br>Transaction Error!";
		} finally {
			conntx.setAutoCommit(true);
			stmttx.close();
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}

	}

	public String PopulatePSFFeedbackType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT psffeedbacktype_id, psffeedbacktype_name"
					+ " FROM axela_service_psf_feedbacktype" + " WHERE 1=1"
					+ " ORDER BY psffeedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("psffeedbacktype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("psffeedbacktype_id"), jcpsf_psffeedbacktype_id));
				Str.append(">").append(crs.getString("psffeedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateLostCase1(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase1_id, lostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " WHERE 1=1"
					+ " ORDER BY lostcase1_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase1_id"), crm_lostcase1_id));
				Str.append(">").append(crs.getString("lostcase1_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase2(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase2_id, lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " WHERE lostcase2_lostcase1_id = " + CNumeric(crm_lostcase1_id)
					+ " ORDER BY lostcase2_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"), crm_lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase3(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase3_id, lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_lostcase2_id = " + CNumeric(crm_lostcase2_id)
					+ " ORDER BY lostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("lostcase3_id"), crm_lostcase3_id));
				Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException {
		doPost(request, response);
	}

	public String PopulateCRMSatisfied(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		// Str.append("<option value=0").append(StrSelectdrop("0",
		// crmfollowup_satisfied)).append(">Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", jcpsf_satisfied)).append(">Satisfied</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", jcpsf_satisfied)).append(">Dis-Satisfied</option>\n");
		return Str.toString();
	}

	public String PopulateExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, COALESCE(CONCAT(emp_name,' (',emp_ref_no,')'),'') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_ticket_owner='1' "
					+ " AND emp_active='1'"
					+ " AND (emp_branch_id = " + jc_branch_id
					+ " OR emp_id = 1"
					+ " OR emp_id in (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE "
					+ compdb(comp_id)
					+ "axela_emp.emp_id=empbr.emp_id"
					+ " AND empbr.emp_branch_id="
					+ jc_branch_id + "))"
					// + ExeAccess
					// + " and emp_id = " + enquiry_emp_id
					+ " group by emp_id " + " ORDER BY emp_name ";
			// SOP("StrSql--------e----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), jcpsf_ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateConcern(String comp_id, String jcpsf_jcpsfconcern_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT jcpsfconcern_id, jcpsfconcern_desc"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_concern ";
			// SOP("StrSql-1-" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jcpsfconcern_id")).append("");
				Str.append(StrSelectdrop(crs.getString("jcpsfconcern_id"), jcpsf_jcpsfconcern_id));
				Str.append(">").append(crs.getString("jcpsfconcern_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public void GetEnquiryLostCase(String enquiry_id) {
		try {
			StrSql = "SELECT "
					+ " lostcase1_id, COALESCE (concat('(',lostcase1_name,')'), '') lostcase1_name,"
					+ " lostcase2_id, COALESCE (concat('(',lostcase2_name,')'), '') lostcase2_name,"
					+ " lostcase3_id, COALESCE (concat('(',lostcase3_name,')'), '') lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = enquiry_lostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id = enquiry_lostcase2_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_id = enquiry_lostcase3_id"
					+ " WHERE 1=1"
					+ " AND enquiry_id = " + enquiry_id
					+ " GROUP BY enquiry_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					lostcase1_name = crs.getString("lostcase1_name");
					lostcase2_name = crs.getString("lostcase2_name");
					lostcase3_name = crs.getString("lostcase3_name");
					if (!add.equals("add")) {
						crm_lostcase1_id = crs.getString("lostcase1_id");
						crm_lostcase2_id = crs.getString("lostcase2_id");
						crm_lostcase3_id = crs.getString("lostcase3_id");
					}
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

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

	public void PopulateConfigDetails(String comp_id) {

		StrSql = " SELECT "
				+ " config_email_enable, branch_jcpsf_email, branch_jcpsf_mobile,"
				+ " COALESCE(emp_email1,'') AS ticket_emp_email1,"
				+ " COALESCE(emp_email2,'') AS ticket_emp_email2,"
				+ " COALESCE(emp_mobile1,'') AS ticket_emp_mobile1,"
				+ " COALESCE(emp_mobile2,'') AS ticket_emp_mobile2,"
				+ " config_email_enable,"
				+ " config_sms_enable, comp_email_enable, comp_sms_enable,branch_email1, "
				+ " config_ticket_jcpsf_email_sub,config_ticket_jcpsf_email_format,"
				+ " psfdays_contactable_email_enable, psfdays_contactable_email_sub, psfdays_contactable_email_format,"
				+ " psfdays_contactable_email_exe_sub, psfdays_contactable_email_exe_format,"
				+ " psfdays_contactable_sms_enable, psfdays_contactable_sms_format,"
				+ " psfdays_contactable_sms_exe_format , psfdays_noncontactable_email_enable,"
				+ " psfdays_noncontactable_email_sub , psfdays_noncontactable_email_format,"
				+ " psfdays_noncontactable_email_exe_sub , psfdays_noncontactable_email_exe_format,"
				+ " psfdays_noncontactable_sms_enable , psfdays_noncontactable_sms_format,"
				+ " psfdays_noncontactable_sms_exe_format ,"
				+ " psfdays_satisfied_email_enable, psfdays_satisfied_email_sub ,"
				+ " psfdays_satisfied_email_format ,psfdays_satisfied_sms_enable, "
				+ " psfdays_satisfied_email_exe_sub, psfdays_satisfied_email_exe_format,"
				+ " psfdays_satisfied_sms_format, psfdays_satisfied_sms_exe_format, "
				+ " psfdays_dissatisfied_email_enable ,psfdays_dissatisfied_email_sub ,"
				+ " psfdays_dissatisfied_email_format, psfdays_dissatisfied_sms_enable ,"
				+ " psfdays_dissatisfied_email_exe_sub, psfdays_dissatisfied_email_exe_format,"
				+ " psfdays_dissatisfied_sms_format, psfdays_dissatisfied_sms_exe_format";

		StrSql += " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_psfdays_id = psfdays_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = " + jcpsf_ticket_emp_id
				+ " INNER JOIN axela_brand ON brand_id = branch_brand_id,"
				+ compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1 "
				+ " AND jcpsf_id = " + jcpsf_id;

		// SOP("StrSql-------12--" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				branch_email1 = crs.getString("branch_email1");

				ticket_emp_email1 = crs.getString("ticket_emp_email1");
				ticket_emp_email2 = crs.getString("ticket_emp_email2");
				ticket_emp_mobile1 = crs.getString("ticket_emp_mobile1");
				ticket_emp_mobile2 = crs.getString("ticket_emp_mobile2");

				config_email_enable = crs.getString("config_email_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_ticket_jcpsf_email_sub = crs.getString("config_ticket_jcpsf_email_sub");
				config_ticket_jcpsf_email_format = crs.getString("config_ticket_jcpsf_email_format");
				branch_jcpsf_email = crs.getString("branch_jcpsf_email");
				branch_jcpsf_mobile = crs.getString("branch_jcpsf_mobile");
				psfdays_contactable_email_enable = crs.getString("psfdays_contactable_email_enable");
				psfdays_contactable_email_sub = crs.getString("psfdays_contactable_email_sub");
				psfdays_contactable_email_format = crs.getString("psfdays_contactable_email_format");
				psfdays_contactable_email_exe_sub = crs.getString("psfdays_contactable_email_exe_sub");
				psfdays_contactable_email_exe_format = crs.getString("psfdays_contactable_email_exe_format");
				psfdays_contactable_sms_enable = crs.getString("psfdays_contactable_sms_enable");
				psfdays_contactable_sms_format = crs.getString("psfdays_contactable_sms_format");
				psfdays_contactable_sms_exe_format = crs.getString("psfdays_contactable_sms_exe_format");
				psfdays_noncontactable_email_enable = crs.getString("psfdays_noncontactable_email_enable");
				psfdays_noncontactable_email_sub = crs.getString("psfdays_noncontactable_email_sub");
				psfdays_noncontactable_email_format = crs.getString("psfdays_noncontactable_email_format");
				psfdays_noncontactable_email_exe_sub = crs.getString("psfdays_noncontactable_email_exe_sub");
				psfdays_noncontactable_email_exe_format = crs.getString("psfdays_noncontactable_email_exe_format");
				psfdays_noncontactable_sms_enable = crs.getString("psfdays_noncontactable_sms_enable");
				psfdays_noncontactable_sms_format = crs.getString("psfdays_noncontactable_sms_format");
				psfdays_noncontactable_sms_exe_format = crs.getString("psfdays_noncontactable_sms_exe_format");
				psfdays_satisfied_email_enable = crs.getString("psfdays_satisfied_email_enable");
				psfdays_satisfied_email_sub = crs.getString("psfdays_satisfied_email_sub");
				psfdays_satisfied_email_format = crs.getString("psfdays_satisfied_email_format");
				psfdays_satisfied_email_exe_sub = crs.getString("psfdays_satisfied_email_exe_sub");
				psfdays_satisfied_email_exe_format = crs.getString("psfdays_satisfied_email_exe_format");
				psfdays_satisfied_sms_enable = crs.getString("psfdays_satisfied_sms_enable");
				psfdays_satisfied_sms_format = crs.getString("psfdays_satisfied_sms_format");
				psfdays_satisfied_sms_exe_format = crs.getString("psfdays_satisfied_sms_exe_format");
				psfdays_dissatisfied_email_enable = crs.getString("psfdays_dissatisfied_email_enable");
				psfdays_dissatisfied_email_sub = crs.getString("psfdays_dissatisfied_email_sub");
				psfdays_dissatisfied_email_format = crs.getString("psfdays_dissatisfied_email_format");
				psfdays_dissatisfied_email_exe_sub = crs.getString("psfdays_dissatisfied_email_exe_sub");
				psfdays_dissatisfied_email_exe_format = crs.getString("psfdays_dissatisfied_email_exe_format");
				psfdays_dissatisfied_sms_enable = crs.getString("psfdays_dissatisfied_sms_enable");
				psfdays_dissatisfied_sms_format = crs.getString("psfdays_dissatisfied_sms_format");
				psfdays_dissatisfied_sms_exe_format = crs.getString("psfdays_dissatisfied_sms_exe_format");

			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void SendEmail(String type, String contactable, String satisfied, String comp_id) throws SQLException {
		// SOP("email to jcpsf===" + type);
		String email_from = "", email_to = "", email_cc = "", email_contact_id = "", email_contact_name = "";
		String emailmsg = "";
		String sub = "";
		if (type.equals("customer")) {
			email_from = branch_email1;
			email_to = send_contact_email;
			email_contact_id = contact_id;
			email_contact_name = contact_name;

			if (contactable.equals("1")) {
				sub = psfdays_contactable_email_sub;
				emailmsg = psfdays_contactable_email_format;

			}
			else if (!contactable.equals("0") && !contactable.equals("1")) {
				sub = psfdays_noncontactable_email_sub;
				emailmsg = psfdays_noncontactable_email_format;
			}
			if (satisfied.equals("1")) {
				sub = psfdays_satisfied_email_sub;
				emailmsg = psfdays_satisfied_email_format;
			}
			else if (satisfied.equals("2")) {
				if (!branch_jcpsf_email.equals("")) {
					email_cc = branch_jcpsf_email;
				}
				sub = psfdays_dissatisfied_email_sub;
				emailmsg = psfdays_dissatisfied_email_format;
			}
		}

		else if (type.equals("executive")) {
			email_from = branch_email1;
			if (!jcexe_empemail1.equals("")) {
				email_to = jcexe_empemail1;
			}
			email_contact_id = "0";
			if (contactable.equals("1")) {
				sub = psfdays_contactable_email_exe_sub;
				emailmsg = psfdays_contactable_email_exe_format;
			}
			else if (!contactable.equals("0") && !contactable.equals("1")) {
				sub = psfdays_noncontactable_email_exe_sub;
				emailmsg = psfdays_noncontactable_email_exe_format;
			}
			if (satisfied.equals("1")) {
				sub = psfdays_satisfied_email_exe_sub;
				emailmsg = psfdays_satisfied_email_exe_format;
			}
			else if (satisfied.equals("2")) {
				if (!branch_jcpsf_email.equals("") && !email_to.contains(branch_jcpsf_email)) {
					email_to += "," + branch_jcpsf_email;
				}
				sub = psfdays_dissatisfied_email_exe_sub;
				emailmsg = psfdays_dissatisfied_email_exe_format;
			}
		}

		email_to = RemoveDuplicateEmails(email_to);
		email_cc = RemoveDuplicateEmails(email_cc);
		// email sub\
		// SOP("satisfied-------" + satisfied);
		// SOP("contactable-------" + contactable);
		// SOP("type-------" + type);
		// SOP("email_to===" + email_to);
		// SOP("email_cc===" + email_cc);

		sub = "REPLACE('" + sub + "','[JCID]', jc_id)";
		sub = "REPLACE(" + sub + ",'[JCDATE]', DATE_FORMAT(jc_time_in, '%d/%m/%Y'))";
		sub = "REPLACE(" + sub + ",'[PSFDAY]', concat( psfdays_daycount, ' ', psfdays_desc ))";
		sub = "REPLACE(" + sub + ",'[ENTRYDATE]', DATE_FORMAT(jcpsf_entry_time, '%d/%m/%Y'))";
		sub = "REPLACE(" + sub + ",'[VOC]',COALESCE(jcpsf_desc, ''))";
		sub = "REPLACE(" + sub + ",'[PSFCONCERN]',COALESCE(jcpsfconcern_desc, ''))";
		sub = "REPLACE(" + sub + ",'[JCPSFEXE]',psfexe.emp_name)";
		sub = "REPLACE(" + sub + ",'[JCPSFEXEJOBTITLE]',psfexejobtitle.jobtitle_desc)";
		sub = "REPLACE(" + sub + ",'[JCPSFEXEMOBILE1]',psfexe.emp_mobile1)";
		sub = "REPLACE(" + sub + ",'[JCPSFEXEEMAIL1]',psfexe.emp_email1)";
		sub = "REPLACE(" + sub + ",'[SAEXE]',COALESCE(saexe.emp_name,''))";
		sub = "REPLACE(" + sub + ",'[SAEXEJOBTITLE]',COALESCE(saexejobtitle.jobtitle_desc,''))";
		sub = "REPLACE(" + sub + ",'[SAEXEMOBILE1]',COALESCE(saexe.emp_mobile1,''))";
		sub = "REPLACE(" + sub + ",'[SAEXEEMAIL1]',COALESCE(saexe.emp_email1,''))";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "REPLACE(" + sub + ",'[CONTACTID]',contact_id)";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE2]',contact_mobile2)";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL2]', contact_email2)";
		sub = "REPLACE(" + sub + ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		sub = "REPLACE(" + sub + ",'[VARIANT]',coalesce(variant_name, ''))";
		sub = "REPLACE(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "REPLACE(" + sub + ",'[BRANCHEMAIL]', COALESCE(branch_email1, ''))";
		sub = "REPLACE(" + sub + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1, ''))";
		sub = "REPLACE(" + sub + ",'[BRANCHMOBILE2]', COALESCE(branch_mobile2, ''))";
		sub = "REPLACE(" + sub + ",'[REGNO]', veh_reg_no)";
		// email message
		emailmsg = "REPLACE('" + emailmsg + "','[JCID]', jc_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCDATE]', DATE_FORMAT(jc_time_in, '%d/%m/%Y'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[PSFDAY]', concat( psfdays_daycount, ' ', psfdays_desc ))";
		emailmsg = "REPLACE(" + emailmsg + ",'[ENTRYDATE]', DATE_FORMAT(jcpsf_entry_time, '%d/%m/%Y'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[VOC]',COALESCE(jcpsf_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[PSFCONCERN]',COALESCE(jcpsfconcern_desc, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXE]',psfexe.emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXEJOBTITLE]',psfexejobtitle.jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXEMOBILE1]',psfexe.emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXEEMAIL1]',psfexe.emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXE]',COALESCE(saexe.emp_name,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXEJOBTITLE]',COALESCE(saexejobtitle.jobtitle_desc,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXEMOBILE1]',COALESCE(saexe.emp_mobile1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXEEMAIL1]',COALESCE(saexe.emp_email1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTID]',contact_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE2]',contact_mobile2)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL2]', contact_email2)";
		emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[VARIANT]',coalesce(variant_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHEMAIL]', COALESCE(branch_email1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHMOBILE2]', COALESCE(branch_mobile2, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[REGNO]', veh_reg_no)";
		try {
			StrSql = "SELECT "
					+ "	branch_id,"
					+ " " + email_contact_id + ", "
					+ " '" + email_contact_name + "', "
					+ " '" + email_from + "', "
					+ " '" + email_to + "', "
					+ " '" + email_cc + "', "
					+ " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"

					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_emp psfexe ON psfexe.emp_id = jcpsf_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle psfexejobtitle on psfexejobtitle.jobtitle_id = psfexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp saexe ON saexe.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle saexejobtitle on saexejobtitle.jobtitle_id = saexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " WHERE jcpsf_id = " + jcpsf_id;

			// SOP("StrSql--mail-----------" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email "
					+ "("
					+ "	email_branch_id,"
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					+ " email_cc, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_emp_id, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql-----------" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS(String type, String contactable, String satisfied, String contact_mobile) throws SQLException {
		String smsmsg = "", sms_contact_id = "", sms_contact_name = "", sms_to = "";
		if (type.equals("customer")) {
			sms_contact_id = contact_id;
			sms_contact_name = contact_name;
			sms_to = contact_mobile;
			if (contactable.equals("1")) {
				smsmsg = psfdays_contactable_sms_format;
			}
			else if (!contactable.equals("0") && !contactable.equals("1")) {
				smsmsg = psfdays_noncontactable_sms_format;
			}
			if (satisfied.equals("1")) {
				smsmsg = psfdays_satisfied_sms_format;
			}
			else if (satisfied.equals("2")) {
				smsmsg = psfdays_dissatisfied_sms_format;
			}
		}
		if (type.equals("executive")) {
			// SOP("saexe_emp_mobile1==22===" + contact_mobile);
			sms_to = contact_mobile;
			sms_contact_id = "0";// jcpsfexeid;
			// sms_contact_name = jcpsfexename;

			if (contactable.equals("1")) {
				smsmsg = psfdays_contactable_sms_format;
			}
			else if (!contactable.equals("0") && !contactable.equals("1")) {
				smsmsg = psfdays_noncontactable_sms_format;
			}
			if (satisfied.equals("1")) {
				smsmsg = psfdays_satisfied_sms_exe_format;
			}
			else if (satisfied.equals("2")) {
				smsmsg = psfdays_dissatisfied_sms_exe_format;
			}
		}
		// SOP("satisfied-------" + satisfied);
		// SOP("contactable-------" + contactable);
		// SOP("type-------" + type);

		smsmsg = "REPLACE('" + smsmsg + "','[JCID]', jc_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[JCDATE]', DATE_FORMAT(jc_time_in, '%d/%m/%Y'))";
		smsmsg = "REPLACE(" + smsmsg + ",'[PSFDAY]', concat( psfdays_daycount, ' ', psfdays_desc ))";
		smsmsg = "REPLACE(" + smsmsg + ",'[ENTRYDATE]', DATE_FORMAT(jcpsf_entry_time, '%d/%m/%Y'))";
		smsmsg = "REPLACE(" + smsmsg + ",'[VOC]',COALESCE(jcpsf_desc, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[PSFCONCERN]',COALESCE(jcpsfconcern_desc, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[JCPSFEXE]',psfexe.emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[JCPSFEXEJOBTITLE]',psfexejobtitle.jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ",'[JCPSFEXEMOBILE1]',psfexe.emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[JCPSFEXEEMAIL1]',psfexe.emp_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[SAEXE]',COALESCE(saexe.emp_name,''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[SAEXEJOBTITLE]',COALESCE(saexejobtitle.jobtitle_desc,''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[SAEXEMOBILE1]',COALESCE(saexe.emp_mobile1,''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[SAEXEEMAIL1]',COALESCE(saexe.emp_email1,''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTID]',contact_id)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTMOBILE2]',contact_mobile2)";
		smsmsg = "REPLACE(" + smsmsg + ",'[CONTACTEMAIL2]', contact_email2)";
		smsmsg = "REPLACE(" + smsmsg + ",'[MODELNAME]',coalesce(preownedmodel_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[VARIANT]',coalesce(variant_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHEMAIL]', COALESCE(branch_email1, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHMOBILE1]', COALESCE(branch_mobile1, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[BRANCHMOBILE2]', COALESCE(branch_mobile2, ''))";
		smsmsg = "REPLACE(" + smsmsg + ",'[REGNO]', veh_reg_no)";

		try {
			StrSql = "SELECT"
					+ " " + jc_branch_id + ","
					+ " " + sms_contact_id + ","
					+ " '" + sms_contact_name + "',"
					+ " '" + sms_to + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " '" + emp_id + "',"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp psfexe ON psfexe.emp_id = jcpsf_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle psfexejobtitle on psfexejobtitle.jobtitle_id = psfexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp saexe ON saexe.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle saexejobtitle on saexejobtitle.jobtitle_id = saexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " WHERE jcpsf_id = " + jcpsf_id;
			// SOP("StrSql======sms=====" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_emp_id,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("StrSql======sms=====" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void SendJCPSFTicketEmail() throws SQLException {
		String emailmsg = "", sub = "", email_from = "", email_to = "";
		email_from = branch_email1;
		if (!branch_jcpsf_email.equals("")) {
			email_to = branch_jcpsf_email;
		}
		if (!ticket_emp_email1.equals("") && !email_to.contains(ticket_emp_email1)) {
			email_to += "," + ticket_emp_email1;
		}
		if (!ticket_emp_email2.equals("") && !email_to.contains(ticket_emp_email2)) {
			email_to += "," + ticket_emp_email2;
		}
		if (!jcexe_empemail1.equals("") && !email_to.contains(jcexe_empemail1)) {
			email_to += "," + jcexe_empemail1;
		}

		email_to = RemoveDuplicateEmails(email_to);

		sub = config_ticket_jcpsf_email_sub;
		emailmsg = config_ticket_jcpsf_email_format;

		sub = "REPLACE('" + sub + "','[TICKETID]',ticket_id)";
		sub = "REPLACE(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "REPLACE(" + sub + ",'[CONTACTID]',ticket_contact_id)";
		sub = "REPLACE(" + sub + ",'[CONTACTNAME]',COALESCE(concat(contact_fname,' ', contact_lname),''))";
		sub = "REPLACE(" + sub + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		sub = "REPLACE(" + sub + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		sub = "REPLACE(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
		sub = "REPLACE(" + sub + ",'[VOC]',ticket_desc)";
		sub = "REPLACE(" + sub + ",'[CONCERN]',COALESCE(jcpsfconcern_desc,''))";
		sub = "REPLACE(" + sub + ",'[MODELNAME]',COALESCE(preownedmodel_name, ''))";
		sub = "REPLACE(" + sub + ",'[VARIANT]',COALESCE(variant_name, ''))";
		sub = "REPLACE(" + sub + ",'[REGNO]',COALESCE(veh_reg_no, ''))";
		sub = "REPLACE(" + sub + ",'[CHASSISNO]',COALESCE(veh_chassis_no, ''))";
		sub = "REPLACE(" + sub + ",'[ENGINENO]',COALESCE(veh_engine_no, ''))";
		sub = "REPLACE(" + sub + ",'[PSFDAY]',COALESCE(CONCAT(psfdays_daycount,' ',psfdays_desc),''))";
		sub = "REPLACE(" + sub + ",'[JCPSFEXE]',COALESCE(jcpsfexe.emp_name,''))";
		sub = "REPLACE(" + sub + ",'[JCPSFEXEJOBTITLE]',COALESCE(jcpsfexejobtitle.jobtitle_desc,''))";
		sub = "REPLACE(" + sub + ",'[JCPSFEXEMOBILE1]',COALESCE(jcpsfexe.emp_mobile1,''))";
		sub = "REPLACE(" + sub + ",'[JCPSFEXEEMAIL1]',COALESCE(jcpsfexe.emp_email1,''))";
		sub = "REPLACE(" + sub + ",'[TECHNICIANNAME]',COALESCE(techexe.emp_name,''))";
		sub = "REPLACE(" + sub + ",'[SAEXE]',COALESCE(saexe.emp_name,''))";
		sub = "REPLACE(" + sub + ",'[SAEXEJOBTITLE]',COALESCE(saexejobtitle.jobtitle_desc,''))";
		sub = "REPLACE(" + sub + ",'[SAEXEMOBILE1]',COALESCE(saexe.emp_mobile1,''))";
		sub = "REPLACE(" + sub + ",'[SAEXEEMAIL1]',COALESCE(saexe.emp_email1,''))";
		sub = "REPLACE(" + sub + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		sub = "REPLACE(" + sub + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		sub = "REPLACE(" + sub + ",'[TICKETOWNER]',owner.emp_name)";
		sub = "REPLACE(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
		sub = "REPLACE(" + sub + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		sub = "REPLACE(" + sub + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		sub = "REPLACE(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
		sub = "REPLACE(" + sub + ",'[PRIORITY]',priorityticket_name)";
		sub = "REPLACE(" + sub + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		sub = "REPLACE(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
		sub = "REPLACE(" + sub + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";
		// ===============
		emailmsg = "REPLACE('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTNAME]',COALESCE(concat(contact_fname,' ', contact_lname),''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "REPLACE(" + emailmsg + ",'[VOC]',ticket_desc)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CONCERN]',COALESCE(jcpsfconcern_desc,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[MODELNAME]',COALESCE(preownedmodel_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[VARIANT]',COALESCE(variant_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[REGNO]',COALESCE(veh_reg_no, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CHASSISNO]',COALESCE(veh_chassis_no, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[ENGINENO]',COALESCE(veh_engine_no, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[PSFDAY]',COALESCE(CONCAT(psfdays_daycount,' ',psfdays_desc),''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXE]',COALESCE(jcpsfexe.emp_name,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXEJOBTITLE]',COALESCE(jcpsfexejobtitle.jobtitle_desc,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXEMOBILE1]',COALESCE(jcpsfexe.emp_mobile1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[JCPSFEXEEMAIL1]',COALESCE(jcpsfexe.emp_email1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TECHNICIANNAME]',COALESCE(techexe.emp_name,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXE]',COALESCE(saexe.emp_name,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXEJOBTITLE]',COALESCE(saexejobtitle.jobtitle_desc,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXEMOBILE1]',COALESCE(saexe.emp_mobile1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[SAEXEEMAIL1]',COALESCE(saexe.emp_email1,''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETOWNER]',owner.emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		emailmsg = "REPLACE(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "REPLACE(" + emailmsg + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		try {
			StrSql = "SELECT"
					+ " branch_id , "
					+ " 0 , "
					+ " '', "
					+ " '" + email_from + "', "
					+ " '" + email_to + "', "
					+ " " + sub + ", "
					+ " " + emailmsg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp owner ON owner.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id "
					// + " LEFT JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp jcpsfexe ON jcpsfexe.emp_id = jcpsf_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle jcpsfexejobtitle ON jcpsfexejobtitle.jobtitle_id = jcpsfexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp saexe ON saexe.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_jobtitle saexejobtitle ON saexejobtitle.jobtitle_id = saexe.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp techexe ON techexe.emp_id = jc_technician_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					+ " WHERE ticket_id = " + ticket_id
					+ " GROUP BY ticket_id"
					+ " LIMIT 1";
			// SOP("SendCRMTicketEmail---emaile---" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ "("
					+ " email_branch_id, "
					+ " email_contact_id, "
					+ " email_contact, "
					+ " email_from, "
					+ " email_to, "
					+ " email_subject,"
					+ " email_msg, "
					+ " email_date, "
					+ " email_emp_id, "
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("SendCRMTicketEmail---emaile---" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
