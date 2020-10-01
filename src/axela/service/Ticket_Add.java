/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ticket_Add extends Connect {

	public String emp_role_id = "0";
	public String ticket_ticketcat_id = "0";
	public String ticket_tickettype_id = "0";
	public String ticket_id = "0";
	public String ticket_branch_id = "0", ticket_parent_id = "0";
	public String ticket_customer_id = "0";
	public String customer_name = "";
	public String ticket_ticketstatus_id = "0";
	public String ticket_subject = "";
	public String ticket_desc = "";
	public String ticket_read = "";
	public String ticket_notes = "";
	public String ticket_ticketsource_id = "0";
	public String ticket_priorityticket_id = "0";
	public String ticket_ticket_dept_id = "0";
	public String ticket_cc = "";
	public String[] cc = null;
	public String ticket_emp_id = "0";
	public String ticket_due_time = "";

	public String ticket_enquiry_id = "0";
	public String ticket_preowned_id = "0";
	public String ticket_so_id = "0";
	public String ticket_crm_id = "0";
	public String ticket_preowned_crm_id = "0";
	public String ticket_veh_id = "0";
	public String ticket_jc_id = "0";
	public String ticket_jcpsf_id = "0";

	public String veh_id = "";
	public String veh_chassis_no = "";
	public String veh_reg_no = "";
	public String jc_id = "";
	//
	public String ticket_trigger1_hrs = "";
	public String ticket_trigger2_hrs = "";
	public String ticket_trigger3_hrs = "";
	public String ticket_trigger4_hrs = "";
	public String ticket_trigger5_hrs = "";
	public String ticket_dept_trigger1_hrs = "";
	public String ticket_dept_trigger2_hrs = "";
	public String ticket_dept_trigger3_hrs = "";
	public String ticket_dept_trigger4_hrs = "";
	public String ticket_dept_trigger5_hrs = "";
	// /
	public String ticket_report_time = "";
	public String ticket_trigger = "";
	public String ticket_entry_id = "0";
	public String ticket_entry_date = "";
	public String ticket_modified_id = "0";
	public String ticket_modified_date = "";
	public String branch_brand_id = "0";
	public String add = "";
	public String addB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String SqlCount = "";
	public String StrHTML = "";
	public String customer_branch_id = "0";
	public String ticket_contact_id = "0";
	public String config_service_ticket_cat = "";
	public String config_service_ticket_type = "";
	public String config_email_enable = "";
	public String config_admin_email = "";
	public String config_ticket_new_email_enable = "";
	public String config_ticket_new_email_format = "";
	public String config_ticket_new_email_sub = "";
	public String config_ticket_new_email_exe_enable = "";
	public String config_ticket_new_email_exe_format = "";
	public String config_ticket_new_email_exe_sub = "";
	public String config_ticket_new_sms_enable = "";
	public String config_ticket_new_sms_format = "";
	public String config_ticket_new_sms_exe_format = "";
	public String comp_sms_enable = "";
	public String comp_email_enable = "";
	public String config_sms_enable = "";

	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_mobile2 = "";
	public String contact_email1 = "";
	public String contact_email2 = "";
	public String send_contact_email = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String branch_email = "";
	public String branch_ticket_email = "";
	public String emp_email_formail = "";
	public String entryemp_email = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public String search_contact = "";
	public String search_customer_id = "0";
	public String search_customer_name = "";
	public String search_customer_branch_id = "0";
	public String search_customer_city_id = "0";
	public String search_contact_id = "0";
	public String search_contact_title_id = "0";
	public String search_contact_fname = "";
	public String search_contact_lname = "";
	public String search_contact_mobile1 = "91-";
	public String search_contact_phone1 = "";
	public String search_contact_email1 = "";

	public String branch_id = "", param = "", branch_type = "", branch_name = "";
	// inbound package fields
	public String inbound_check = "";
	public String ticket_call_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				// CheckPerm(comp_id, "emp_ticket_add", request, response);
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				inbound_check = PadQuotes(request.getParameter("inbound"));
				ticket_call_id = CNumeric(PadQuotes(request.getParameter("ticket_call_id")));

				ticket_emp_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_emp_id")));
				ticket_parent_id = CNumeric(PadQuotes(request.getParameter("ticket_parent_id")));
				ticket_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				veh_chassis_no = PadQuotes(request.getParameter("veh_chassis_no"));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				ticket_branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				// SOP("ticket_branch_id===" + ticket_branch_id);
				if (!ticket_branch_id.equals("0")) {
					branch_name = ExecuteQuery("SELECT branch_name FROM " + compdb(comp_id) + "axela_branch "
							+ " WHERE branch_id = " + ticket_branch_id);
				}
				// This used only when ticket add is done from contact search
				search_contact = PadQuotes(request.getParameter("contact"));
				// ----------------------------------------------------

				// // SOP("jc_id=="+jc_id);
				PopulateConfigDetails(comp_id);
				if (!ticket_contact_id.equals("0")) {
					PopulateContactDetails();
				}

				if (!veh_id.equals("0") || !veh_chassis_no.equals("") || !jc_id.equals("0")) {
					PopulateVehContactDetails(comp_id);
				}

				if ((!ticket_contact_id.equals("0") || !veh_id.equals("0") || !veh_chassis_no.equals("") || !jc_id.equals("0")) && ticket_customer_id.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact!"));
				}

				if (!ticket_parent_id.equals("0")) {
					PopulateFields(response);
				}

				if (search_contact.equals("yes")) {
					// This used only when ticket add is done from contact search
					search_contact_mobile1 = PadQuotes(request.getParameter("contact_mobile"));
					search_contact_phone1 = PadQuotes(request.getParameter("contact_phno"));
					// -----------------------------------------------------------
				}
				if (add.equals("yes")) {
					status = "Add";
				}

				if (ticket_ticketstatus_id.equals("0")) {
					ticket_ticketstatus_id = "1";
				}

				if (!"yes".equals(addB)) {
					ticket_emp_id = emp_id;
					ticket_trigger = "0";
					ticket_report_time = ToLongDate(kknow());
				} else {
					GetValues(request, response);
					ticket_entry_id = emp_id;
					ticket_entry_date = ToLongDate(kknow());
					ticket_report_time = ToLongDate(kknow());
					PopulateConfigDetails(comp_id);
					AddFields(comp_id);
					if (!msg.equals("")) {
						msg = "Error! " + msg;
					} else {
						if (inbound_check.equals("yes")) {
							msg = "Ticket added successfully!";
						} else {
							response.sendRedirect(response.encodeRedirectURL("ticket-list.jsp?ticket_id=" + ticket_id + "&msg=Ticket added successfully!"));
						}
					}
					// } else {
					// response.sendRedirect(AccessDenied());
					// }
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// ticket_id =
			// CNumeric(PadQuotes(request.getParameter("ticket_id")));

			// This used only when ticket add is done from contact search
			if (search_contact.equals("yes")) {

				search_customer_branch_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_branch_id")));
				search_customer_city_id = CNumeric(ExecuteQuery("SELECT branch_city_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + search_customer_branch_id));

				search_contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_search_title")));
				search_contact_fname = PadQuotes(request.getParameter("txt_search_contact_fname"));
				search_contact_lname = PadQuotes(request.getParameter("txt_search_contact_lname"));
				search_contact_email1 = PadQuotes(request.getParameter("txt_search_contact_email1"));
				search_contact_mobile1 = PadQuotes(request.getParameter("txt_search_contact_mobile1"));
				search_contact_phone1 = PadQuotes(request.getParameter("txt_search_contact_phone1"));

				search_customer_name = toTitleCase(search_contact_fname + " " + search_contact_lname);

			}
			// -----------------------------------------------------------
			// for inbound package only
			if (inbound_check.equals("yes")) {
				ticket_customer_id = CNumeric(PadQuotes(request.getParameter("ticket_customer_id")));
				ticket_contact_id = CNumeric(PadQuotes(request.getParameter("ticket_contact_id")));
			}
			// --------------------
			if (ticket_branch_id.equals("0")) {
				ticket_branch_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_branch_id")));
			}
			ticket_parent_id = CNumeric(PadQuotes(request.getParameter("ticket_parent_id")));
			ticket_subject = PadQuotes(request.getParameter("txt_ticket_subject"));
			ticket_desc = PadQuotes(request.getParameter("txt_ticket_desc"));
			ticket_ticketcat_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_ticketcat_id")));
			ticket_tickettype_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_tickettype_id")));
			ticket_ticketsource_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_ticketsource_id")));
			ticket_priorityticket_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_priorityticket_id")));
			ticket_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_dept_id")));
			ticket_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
			if (emp_id.equals(ticket_emp_id)) {
				ticket_read = "1";
			} else {
				ticket_read = "0";
			}
			ticket_trigger = "0";
			ticket_cc = (PadQuotes(request.getParameter("txt_ticket_cc")));
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm(String comp_id) {
		msg = "";

		if (search_contact.equals("yes")) {

			if (search_contact_title_id.equals("0")) {
				msg = msg + "<br>Select Title!";
			}
			if (search_contact_fname.equals("")) {
				msg = msg + "<br>Enter the Contact Person Name!";
			} else {
				search_contact_fname = toTitleCase(search_contact_fname);
			}

			if (!search_contact_lname.equals("")) {
				search_contact_lname = toTitleCase(search_contact_lname);
			}

			if (customer_name.equals("")) {
				search_customer_name = (search_contact_fname + " " + search_contact_lname).trim();
				search_customer_name = toTitleCase(search_customer_name);
			}

			if (!search_contact_email1.equals("")) {
				if (!IsValidEmail(search_contact_email1)) {
					msg = msg + "<br>Enter Valid Contact Email 1!";
				}
			}
			if (search_contact_mobile1.equals("") && search_contact_phone1.equals("")) {
				msg = msg + "<br>Enter Either Contact Mobile 1 or Phone 1!";
			} else if (!search_contact_mobile1.equals("")) {
				if (!IsValidMobileNo11(search_contact_mobile1)) {
					msg = msg + "<br>Enter Valid Contact Mobile 1!";
				}
			}
			if (!search_contact_phone1.equals("")) {
				if (!IsValidPhoneNo11(search_contact_phone1)) {
					msg = msg + "<br>Enter Valid Contact Phone!";
				}
			}

		}

		if (ticket_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (ticket_subject.equals("")) {
			msg = msg + "<br>Enter Subject!";
		} else if (ticket_subject.length() < 5) {
			msg = msg + "<br>Subject should have atleast five characters!";
		}
		if (ticket_desc.equals("")) {
			msg = msg + "<br>Enter Brief Description!";
		} else if (ticket_desc.length() < 5) {
			msg = msg + "<br>Description should have atleast five characters!";
		}
		if (ticket_ticketsource_id.equals("0")) {
			msg = msg + "<br>Select Source!";
		}
		if (ticket_priorityticket_id.equals("0")) {
			msg = msg + "<br>Select Priority!";
		}
		if (ticket_ticket_dept_id.equals("0")) {
			msg = msg + "<br>Select Department!";
		}
		if (ticket_emp_id.equals("0")) {
			msg = msg + "<br>Select Executive!";
		}
		if (!ticket_ticket_dept_id.equals("") && !ticket_ticket_dept_id.equals("0")) {
			String duehrs = "", business_hrs = "", starttime = "", endtime = "", sun = "", mon = "", tue = "", wed = "", thu = "", fri = "", sat = "";
			StrSql = " SELECT ticket_dept_duehrs, ticket_dept_business_hrs, ticket_dept_starttime, ticket_dept_endtime,"
					+ " ticket_dept_sun, ticket_dept_mon, ticket_dept_tue, ticket_dept_wed, ticket_dept_thu, ticket_dept_fri, ticket_dept_sat,"
					+ " ticket_dept_trigger1_hrs, ticket_dept_trigger2_hrs, ticket_dept_trigger3_hrs, ticket_dept_trigger4_hrs,"
					+ " ticket_dept_trigger5_hrs"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept"
					+ " WHERE ticket_dept_id = " + ticket_ticket_dept_id + "";
			CachedRowSet crs1 = processQuery(StrSql, 0);

			try {
				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {
						duehrs = crs1.getString("ticket_dept_duehrs");
						business_hrs = crs1.getString("ticket_dept_business_hrs");
						starttime = crs1.getString("ticket_dept_starttime");
						endtime = crs1.getString("ticket_dept_endtime");
						sun = crs1.getString("ticket_dept_sun");
						mon = crs1.getString("ticket_dept_mon");
						tue = crs1.getString("ticket_dept_tue");
						wed = crs1.getString("ticket_dept_wed");
						thu = crs1.getString("ticket_dept_thu");
						fri = crs1.getString("ticket_dept_fri");
						sat = crs1.getString("ticket_dept_sat");
						ticket_dept_trigger1_hrs = crs1.getString("ticket_dept_trigger1_hrs");
						ticket_dept_trigger2_hrs = crs1.getString("ticket_dept_trigger2_hrs");
						ticket_dept_trigger3_hrs = crs1.getString("ticket_dept_trigger3_hrs");
						ticket_dept_trigger4_hrs = crs1.getString("ticket_dept_trigger4_hrs");
						ticket_dept_trigger5_hrs = crs1.getString("ticket_dept_trigger5_hrs");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				crs1.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (business_hrs.equals("1")) {
				if (Double.parseDouble(starttime) < Double.parseDouble(endtime)) {

					ArrayList public_holidate = publicHolidays(ticket_report_time, customer_branch_id, comp_id);
					// int i = Integer.parseInt(duehrs);
					if (!duehrs.equals("0")) {
						// double starttime1 = Double.parseDouble(starttime);
						// double endtime1 = Double.parseDouble(starttime);
						ticket_due_time = DueTime(ticket_report_time, duehrs,
								Double.parseDouble(starttime), Double.parseDouble(endtime),
								sun, mon, tue, wed, thu, fri, sat, public_holidate);
					} else {
						ticket_due_time = ticket_report_time;
					}

					// *** start- ticket_trigger_hrs
					// ---------------1--------------------
					if (!ticket_dept_trigger1_hrs.equals("0")) {
						ticket_trigger1_hrs = DueTime(ticket_report_time, ticket_dept_trigger1_hrs,
								Double.parseDouble(starttime), Double.parseDouble(endtime),
								sun, mon, tue, wed, thu, fri, sat, public_holidate);
					} else {
						ticket_trigger1_hrs = "";
					}
					// ---------------2--------------------
					if (!ticket_dept_trigger2_hrs.equals("0")) {
						ticket_trigger2_hrs = DueTime(ticket_report_time, ticket_dept_trigger2_hrs,
								Double.parseDouble(starttime), Double.parseDouble(endtime),
								sun, mon, tue, wed, thu, fri, sat, public_holidate);
					} else {
						ticket_trigger2_hrs = "";
					}
					// ---------------3--------------------
					if (!ticket_dept_trigger3_hrs.equals("0")) {
						ticket_trigger3_hrs = DueTime(ticket_report_time, ticket_dept_trigger3_hrs,
								Double.parseDouble(starttime), Double.parseDouble(endtime),
								sun, mon, tue, wed, thu, fri, sat, public_holidate);
					} else {
						ticket_trigger3_hrs = "";
					}
					// ---------------4--------------------
					if (!ticket_dept_trigger4_hrs.equals("0")) {
						ticket_trigger4_hrs = DueTime(ticket_report_time, ticket_dept_trigger4_hrs,
								Double.parseDouble(starttime), Double.parseDouble(endtime),
								sun, mon, tue, wed, thu, fri, sat, public_holidate);
					} else {
						ticket_trigger4_hrs = "";
					}
					// ---------------5--------------------
					if (!ticket_dept_trigger5_hrs.equals("0")) {
						ticket_trigger5_hrs = DueTime(ticket_report_time, ticket_dept_trigger5_hrs,
								Double.parseDouble(starttime), Double.parseDouble(endtime),
								sun, mon, tue, wed, thu, fri, sat, public_holidate);
					} else {
						ticket_trigger5_hrs = "";
					}
					// *** eof- ticket_trigger_hrs
				}
			} else {
				if (!duehrs.equals("0")) {
					ticket_due_time = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Double.parseDouble(duehrs), 0));
				} else {
					ticket_due_time = ticket_report_time;
				}
				// ---------------1--------------------
				if (!ticket_dept_trigger1_hrs.equals("0")) {
					ticket_trigger1_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Double.parseDouble(ticket_dept_trigger1_hrs), 0));
				} else {
					ticket_trigger1_hrs = "";
				}
				// ---------------2--------------------
				if (!ticket_dept_trigger2_hrs.equals("0")) {
					ticket_trigger2_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Double.parseDouble(ticket_dept_trigger2_hrs), 0));
				} else {
					ticket_trigger2_hrs = "";
				}
				// ---------------3--------------------
				if (!ticket_dept_trigger3_hrs.equals("0")) {
					ticket_trigger3_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Double.parseDouble(ticket_dept_trigger3_hrs), 0));
				} else {
					ticket_trigger3_hrs = "";
				}
				// ---------------4--------------------
				if (!ticket_dept_trigger4_hrs.equals("0")) {
					ticket_trigger4_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Double.parseDouble(ticket_dept_trigger4_hrs), 0));
				} else {
					ticket_trigger4_hrs = "";
				}
				// ---------------5--------------------
				if (!ticket_dept_trigger5_hrs.equals("0")) {
					ticket_trigger5_hrs = ToLongDate(AddHoursDate(StringToDate(ticket_report_time), 0, Double.parseDouble(ticket_dept_trigger5_hrs), 0));
				} else {
					ticket_trigger5_hrs = "";
				}
			}
		}
		String ccmsg = "";
		if (!ticket_cc.equals("")) {
			cc = ticket_cc.split(",");
			for (int i = 0; i < cc.length; i++) {
				// // SOP("cc["+i+"] = " + cc[i]);
				cc[i] = cc[i].replace(" ", "");
				if (!IsValidEmail(cc[i])) {
					ccmsg = "<br>Enter Valid Email CC!";
				}
			}
			msg = msg + ccmsg;
		}
	}

	public void AddFields(String comp_id) throws Exception {
		CheckForm(comp_id);
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				if (search_contact.equals("yes")) {
					AddCustomerFields();
					AddContactFields();
				}

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_ticket"
						+ " ( "
						+ " ticket_parent_id,"
						+ " ticket_branch_id,"
						+ " ticket_customer_id,"
						+ " ticket_contact_id,"
						+ " ticket_enquiry_id,"
						+ " ticket_preowned_id,"
						+ " ticket_so_id,"
						+ " ticket_crm_id,"
						+ " ticket_preowned_crm_id,"
						+ " ticket_veh_id,"
						+ " ticket_jc_id,"
						+ " ticket_jcpsf_id,"

						+ " ticket_ticketsource_id,"
						+ " ticket_report_time,"
						+ " ticket_ticketstatus_id,"
						+ " ticket_ticketcat_id,"
						+ " ticket_tickettype_id,"
						+ " ticket_priorityticket_id,"
						+ " ticket_cc,"
						+ " ticket_ticket_dept_id,"
						+ " ticket_subject,"
						+ " ticket_desc,"
						+ " ticket_emp_id,"
						+ " ticket_due_time,"
						+ " ticket_trigger1_hrs,"
						+ " ticket_trigger2_hrs,"
						+ " ticket_trigger3_hrs,"
						+ " ticket_trigger4_hrs,"
						+ " ticket_trigger5_hrs,"
						+ " ticket_notes,"
						+ " ticket_trigger,";
				if (inbound_check.equals("yes") && !ticket_call_id.equals("0")) {
					StrSql += " ticket_call_id, ";
				}
				StrSql += " ticket_read,"
						+ " ticket_entry_id,"
						+ " ticket_entry_date)"
						+ " VALUES"
						+ " ( "
						+ ticket_parent_id + ","
						+ " " + ticket_branch_id + ","// branch_id
						+ " " + ticket_customer_id + ","
						+ " " + ticket_contact_id + ","

						+ " " + ticket_enquiry_id + ","
						+ " " + ticket_preowned_id + ","
						+ " " + ticket_so_id + ","
						+ " " + ticket_crm_id + ","
						+ " " + ticket_preowned_crm_id + ","
						+ " " + ticket_veh_id + ","
						+ " " + ticket_jc_id + ","
						+ " " + ticket_jcpsf_id + ","

						+ " " + ticket_ticketsource_id + ","
						+ " '" + ticket_report_time + "',"
						+ " " + ticket_ticketstatus_id + ","
						+ " " + ticket_ticketcat_id + ","
						+ " " + ticket_tickettype_id + ","
						+ " " + ticket_priorityticket_id + ","
						+ " '" + ticket_cc + "',"
						+ " " + ticket_ticket_dept_id + ","
						+ " '" + ticket_subject + "',"
						+ " '" + ticket_desc + "',"
						+ " " + ticket_emp_id + ","
						+ " '" + ticket_due_time + "',"
						+ " '" + ticket_trigger1_hrs + "',"
						+ " '" + ticket_trigger2_hrs + "',"
						+ " '" + ticket_trigger3_hrs + "',"
						+ " '" + ticket_trigger4_hrs + "',"
						+ " '" + ticket_trigger5_hrs + "',"
						+ " '',"
						+ " 0,";
				if (inbound_check.equals("yes") && !ticket_call_id.equals("0")) {
					StrSql += " " + ticket_call_id + ",";
				}
				StrSql += " '" + ticket_read + "',"
						+ " " + ticket_entry_id + ","
						+ " '" + ticket_entry_date + "')";
				SOP("StrSql-ticket add-------------" + StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					ticket_id = rs.getString(1);
				}
				rs.close();
				StrSql = "INSERT INTO " + compdb(comp_id)
						+ "axela_service_ticket_history (history_ticket_id, history_emp_id, history_datetime ,history_actiontype, history_oldvalue, history_newvalue)"
						+ " VALUES"
						+ " (" + ticket_id + ", " + ticket_entry_id + ", '" + ticket_entry_date + "', 'NEW_TICKET', '', 'New ticket added')";
				stmttx.execute(StrSql);

				// // SOP("contact_email1--------------" + contact_email1);
				// // SOP("config_ticket_new_email_enable--------------" + config_ticket_new_email_enable);
				// // SOP("config_ticket_new_email_format--------------" + config_ticket_new_email_format);
				// // SOP("config_ticket_new_email_sub--------------" + config_ticket_new_email_sub);
				// // SOP("config_ticket_new_email_exe_format--------------" + config_ticket_new_email_exe_format);
				// // SOP("config_ticket_new_email_exe_sub--------------" + config_ticket_new_email_exe_sub);

				// // SOP("config_ticket_new_sms_enable--------------" + config_ticket_new_sms_enable);
				// // SOP("config_ticket_new_sms_format--------------" + config_ticket_new_sms_format);
				// // SOP("config_ticket_new_sms_exe_format--------------" + config_ticket_new_sms_exe_format);

				// // SOP("comp_sms_enable---------" + comp_sms_enable);
				// // SOP("config_sms_enable ----------- " + config_sms_enable);
				// // SOP("config_ticket_new_sms_enable------------ " + config_ticket_new_sms_enable);
				// // SOP("send contact_mobile1---------" + contact_mobile1);
				// // SOP("send contact_mobile2-------" + contact_mobile2);

				if (!ticket_id.equals("") && !ticket_id.equals("0")) {
					if (!contact_email2.equals("") && !contact_email1.equals("")) {
						send_contact_email = contact_email1 + "," + contact_email2;
					} else if (!contact_email1.equals("")) {
						send_contact_email = contact_email1;
					} else if (!contact_email2.equals("")) {
						send_contact_email = contact_email2;
					}
					// if (!ticket_cc.equals("") && !send_contact_email.equals("")) {
					// send_contact_email += "," + ticket_cc;
					// }

					if (comp_email_enable.equals("1")
							&& config_email_enable.equals("1")
							&& !config_admin_email.equals("")
							&& config_ticket_new_email_enable.equals("1")
							&& !branch_email.equals("")) {

						if (!send_contact_email.equals("")
								&& !config_ticket_new_email_format.equals("")
								&& !config_ticket_new_email_sub.equals("")) {
							SendEmail(comp_id);
						}

						if (!config_ticket_new_email_exe_format.equals("")
								&& !config_ticket_new_email_exe_sub.equals("") && !emp_email_formail.equals("")) {
							if (!ticket_emp_id.equals(ticket_entry_id) && !entryemp_email.equals("")) {
								emp_email_formail = emp_email_formail + "," + entryemp_email;
							}
							if (ticket_crm_id.equals("0") && ticket_preowned_crm_id.equals("0") && ticket_jcpsf_id.equals("0")) {
								SendEmailToExecutive(comp_id);
							}
						}
					}
					if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")
							&& config_ticket_new_sms_enable.equals("1")) {
						if (!config_ticket_new_sms_format.equals("")) {

							if (!contact_mobile1.equals("")) {
								SendSMS(comp_id, contact_mobile1);
							}
							if (!contact_mobile2.equals("")) {
								SendSMS(comp_id, contact_mobile2);
							}
						}
						if (ticket_crm_id.equals("0") && ticket_preowned_crm_id.equals("0") && ticket_jcpsf_id.equals("0")) {
							if (!config_ticket_new_sms_exe_format.equals("")) {
								if (!emp_mobile1.equals("")) {
									SendSMSToExecutive(comp_id, emp_mobile1);
								}
								if (!emp_mobile2.equals("")) {
									SendSMSToExecutive(comp_id, emp_mobile2);
								}
							}
						}
					}
				}
				conntx.commit();
				// // SOP("Transaction commit...");
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

	public String PopulateSourceType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticketsource_id, ticketsource_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_source "
					+ " where 1 = 1 "
					+ " group by ticketsource_id"
					+ " order by ticketsource_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketsource_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketsource_id"), ticket_ticketsource_id));
				Str.append(">").append(crs.getString("ticketsource_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketPriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT priorityticket_id, priorityticket_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_priority "
					+ " where 1 = 1 "
					+ " group by priorityticket_id"
					+ " order by priorityticket_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityticket_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityticket_id"), ticket_priorityticket_id));
				Str.append(">").append(crs.getString("priorityticket_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDepartment() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticket_dept_id, ticket_dept_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept "
					+ " where 1 = 1 "
					+ " group by ticket_dept_id"
					+ " order by ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), ticket_ticket_dept_id));
				Str.append(">").append(crs.getString("ticket_dept_name")).append("</option> \n");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// public String PopulateExecutive() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value = 0> Select </option>");
	// try {
	// StrSql = "SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, " +
	// compdb(comp_id) + "axela_emp.emp_id AS emp_id"
	// + " FROM " + compdb(comp_id) + "axela_emp"
	// +
	// " where 1=1 and emp_active='1' AND emp_id != 1 AND emp_ticket_owner = '1'"
	// + " and (emp_branch_id = " + customer_branch_id + " "
	// + " or emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) +
	// "axela_emp_branch empbr "
	// + " where " + compdb(comp_id) +
	// "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id=" +
	// customer_branch_id + "))" + ExeAccess
	// + " GROUP BY " + compdb(comp_id) + "axela_emp.emp_id"
	// + " ORDER BY emp_name";
	// // // SOP("StrSql PopulateExecutive= " + StrSql);
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("emp_id"));
	// Str.append(StrSelectdrop(crs.getString("emp_id"), ticket_emp_id));
	// Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }
	public String PopulateAllExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_service_ticket ON ticket_emp_id = " +
					// compdb(comp_id) + "axela_emp.emp_id"
					// + " AND ticket_ticketstatus_id != 3"
					+ " WHERE emp_active = '1'"
					+ " AND emp_ticket_owner = '1'"
					// + ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// // SOP("StrSql PopulateAllExecutive= " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), ticket_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketCategory(String depertment_id, String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT ticketcat_id, ticketcat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_cat "
					+ " WHERE 1 = 1 ";
			if (!depertment_id.equals("0")) {
				StrSql += " AND ticketcat_ticketdept_id = " + depertment_id;
			}
			StrSql += " group by ticketcat_id "
					+ " order by ticketcat_name";

			// // SOP("StrSqll== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_ticket_ticketcat_id\" class=\"form-control\">");
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketcat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketcat_id"), ticket_ticketcat_id));
				Str.append(">").append(crs.getString("ticketcat_name")).append("</option> \n");
			}
			Str.append("</select>");

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT tickettype_id, tickettype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type "
					+ " where 1 = 1 "
					+ " group by tickettype_id "
					+ " order by tickettype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tickettype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("tickettype_id"), ticket_tickettype_id));
				Str.append(">").append(crs.getString("tickettype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void PopulateConfigDetails(String comp_id) {

		StrSql = " SELECT branch_email1, config_service_ticket_cat, config_service_ticket_type,"
				+ " config_email_enable, config_admin_email, config_sms_enable, comp_email_enable, comp_sms_enable,"
				+ " config_ticket_new_email_enable, config_ticket_new_email_sub, config_ticket_new_email_format, "
				+ " config_ticket_new_email_exe_sub, config_ticket_new_email_exe_format, "
				+ " config_ticket_new_sms_enable, config_ticket_new_sms_format, config_ticket_new_sms_exe_format,"
				+ " COALESCE(ticketemp.emp_email1,'') as emp_email1, COALESCE(ticketemp.emp_email2,'') as emp_email2,"
				+ " COALESCE(ticketemp.emp_name,'') as emp_name,"
				+ " COALESCE(ticketemp.emp_mobile1,'') as emp_mobile1 , COALESCE(ticketemp.emp_mobile2,'') as emp_mobile2, "
				+ " COALESCE(ticketentry.emp_email1,'') as entryemp_email,"
				+ " COALESCE(branch_ticket_email,'')branch_ticket_email"
				+ " FROM " + compdb(comp_id) + " axela_branch"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ticketemp ON ticketemp.emp_id = " + ticket_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ticketentry ON ticketentry.emp_id = " + emp_id + ","
				+ compdb(comp_id) + "axela_comp,"
				+ compdb(comp_id) + " axela_config";
		if (!ticket_branch_id.equals("0")) {
			StrSql += " WHERE branch_id = " + ticket_branch_id;
		}

		// // SOP("StrSql---" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				branch_email = crs.getString("branch_email1");
				branch_ticket_email = crs.getString("branch_ticket_email");
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
				config_email_enable = crs.getString("config_email_enable");
				config_admin_email = crs.getString("config_admin_email");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				config_ticket_new_email_enable = crs.getString("config_ticket_new_email_enable");
				config_ticket_new_email_format = crs.getString("config_ticket_new_email_format");
				config_ticket_new_email_sub = crs.getString("config_ticket_new_email_sub");
				config_ticket_new_email_exe_format = crs.getString("config_ticket_new_email_exe_format");
				config_ticket_new_email_exe_sub = crs.getString("config_ticket_new_email_exe_sub");
				config_ticket_new_sms_enable = crs.getString("config_ticket_new_sms_enable");
				config_ticket_new_sms_format = crs.getString("config_ticket_new_sms_format");
				config_ticket_new_sms_exe_format = crs.getString("config_ticket_new_sms_exe_format");
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				entryemp_email = crs.getString("entryemp_email");
				// // SOP("entryemp_email = " + entryemp_email);
				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
				if (!branch_ticket_email.equals("") && !emp_email_formail.equals("")) {
					emp_email_formail = emp_email_formail + "," + branch_ticket_email;
				}
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendEmail(String comp_id) throws SQLException {
		String emailmsg, sub;
		emailmsg = config_ticket_new_email_format;
		sub = config_ticket_new_email_sub;
		sub = "replace('" + sub + "','[TICKETID]',ticket_id)";
		sub = "replace(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
		sub = "replace(" + sub + ",'[CONCERN]',ticket_desc)";
		sub = "replace(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
		sub = "replace(" + sub + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		sub = "replace(" + sub + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		sub = "replace(" + sub + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		sub = "replace(" + sub + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		sub = "replace(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
		sub = "replace(" + sub + ",'[PRIORITY]',priorityticket_name)";
		sub = "replace(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		sub = "replace(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		sub = "replace(" + sub + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[EXECUTIVENAME]',ticketexe.emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',ticketexe.emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEMOBILE2]',ticketexe.emp_mobile2)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',ticketexe.emp_email1)";
		sub = "replace(" + sub + ",'[EXEEMAIL2]',ticketexe.emp_email2)";
		sub = "replace(" + sub + ",'[CONTACTID]',ticket_contact_id)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[MODELNAME]',COALESCE(model_name, ''))";
		sub = "replace(" + sub + ",'[REGNO]',COALESCE(veh_reg_no, ''))";

		emailmsg = "replace('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "replace(" + emailmsg + ",'[CONCERN]',ticket_desc)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTIME]', DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]', DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[DELIVERYDATE]', COALESCE(DATE_FORMAT(so_delivered_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[EXECUTIVENAME]',ticketexe.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',ticketexe.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE2]',ticketexe.emp_mobile2)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',ticketexe.emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL2]',ticketexe.emp_email2)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[REGNO]',COALESCE(veh_reg_no, ''))";

		ticket_cc = RemoveDuplicateEmails(ticket_cc);
		send_contact_email = RemoveDuplicateEmails(send_contact_email);

		try {
			StrSql = "SELECT "
					+ "	COALESCE(branch_id,1),"
					+ " " + ticket_contact_id + ", "
					+ " '" + contact_name + "', "
					+ " '" + branch_email + "', "
					+ " '" + send_contact_email + "', "
					+ " '" + ticket_cc + "', "
					+ " " + (sub) + ", "
					+ " " + (emailmsg) + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " " + emp_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ticketexe on ticketexe.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = ticketexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = ticket_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type on tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = ticket_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_id = ticket_so_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm sales on sales.crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = ticket_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = ticket_jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_id = ticket_jcpsf_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " where ticket_id=" + ticket_id
					+ " GROUP BY ticket_id"
					+ " limit 1";
			// // SOP("StrSql--mail--"+StrSql);
			StrSql = "insert into " + compdb(comp_id) + "axela_email "
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
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			// SOP("StrSql----------ticket--email-------" + StrSql);
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

	protected void SendEmailToExecutive(String comp_id) throws SQLException {
		String emailmsg, sub;
		emailmsg = config_ticket_new_email_exe_format;
		sub = config_ticket_new_email_exe_sub;

		sub = "replace('" + sub + "','[TICKETID]',ticket_id)";
		sub = "replace(" + sub + ",'[TICKETSUBJECT]',ticket_subject)";
		sub = "replace(" + sub + ",'[CONCERN]',ticket_desc)";
		sub = "replace(" + sub + ",'[TICKETSTATUS]',ticketstatus_name)";
		sub = "replace(" + sub + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		sub = "replace(" + sub + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		sub = "replace(" + sub + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		sub = "replace(" + sub + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		sub = "replace(" + sub + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		sub = "replace(" + sub + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		sub = "replace(" + sub + ",'[DELIVERYDATE]', IF(so_delivered_date = '','',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'),'')))";
		sub = "replace(" + sub + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		sub = "replace(" + sub + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		sub = "replace(" + sub + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		sub = "replace(" + sub + ",'[TICKETOWNER]',owner.emp_name)";
		sub = "replace(" + sub + ",'[EXECUTIVENAME]',sesn.emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',sesn.emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEMOBILE2]',sesn.emp_mobile2)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',sesn.emp_email1)";
		sub = "replace(" + sub + ",'[EXEEMAIL2]',sesn.emp_email2)";
		sub = "replace(" + sub + ",'[MODELNAME]',COALESCE(model_name, ''))";
		sub = "replace(" + sub + ",'[REGNO]',COALESCE(veh_reg_no, ''))";
		sub = "replace(" + sub + ",'[DEPARTMENT]',ticket_dept_name)";
		sub = "replace(" + sub + ",'[PRIORITY]',priorityticket_name)";
		sub = "replace(" + sub + ",'[CONTACTID]',ticket_contact_id)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',COALESCE(concat(contact_fname,' ', contact_lname),''))";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		sub = "replace(" + sub + ",'[CUSTOMERID]',ticket_customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		emailmsg = "replace('" + emailmsg + "','[TICKETID]',ticket_id)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		emailmsg = "replace(" + emailmsg + ",'[CONCERN]',ticket_desc)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		emailmsg = "replace(" + emailmsg + ",'[DELIVERYDATE]', IF(so_delivered_date = '','',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'),'')))";
		emailmsg = "replace(" + emailmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[TICKETOWNER]',owner.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXECUTIVENAME]',sesn.emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',sesn.emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE2]',sesn.emp_mobile2)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',sesn.emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL2]',sesn.emp_email2)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		emailmsg = "replace(" + emailmsg + ",'[REGNO]',COALESCE(veh_reg_no, ''))";
		emailmsg = "replace(" + emailmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		emailmsg = "replace(" + emailmsg + ",'[PRIORITY]',priorityticket_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTID]',ticket_contact_id)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',COALESCE(concat(contact_fname,' ', contact_lname),''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		ticket_cc = RemoveDuplicateEmails(ticket_cc);
		emp_email_formail = RemoveDuplicateEmails(emp_email_formail);

		try {
			StrSql = "SELECT"
					+ "	COALESCE(branch_id,1),"
					+ " " + emp_id + ","
					+ " sesn.emp_name ,"
					+ " '" + branch_email + "',"
					+ " '" + emp_email_formail + "',"
					+ " '" + ticket_cc + "',"
					+ " " + sub + ","
					+ " " + emailmsg + ","
					+ " '" + ToLongDate(kknow()) + "', "
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp owner on owner.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sesn on sesn.emp_id = " + emp_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = sesn.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type on tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id "
					// + " LEFT JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = ticket_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_id = ticket_so_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm sales on sales.crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = ticket_jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_id = ticket_jcpsf_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " where ticket_id=" + ticket_id
					+ " GROUP BY ticket_id"
					+ " limit 1";
			// // SOP("StrSqlselect-in-ticket-" + StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_emp_id,"
					+ " email_emp,"
					+ " email_from,"
					+ " email_to,"
					+ " email_cc,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id)"
					+ " " + StrSql + "";
			// // SOP("StrSqlinsert-ticket------SendEmailToExecutive------------" + StrSqlBreaker(StrSql));
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

	protected void SendSMS(String comp_id, String contact_mobile) throws SQLException {
		String smsmsg = config_ticket_new_sms_format;

		smsmsg = "replace('" + smsmsg + "','[TICKETID]',ticket_id)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		smsmsg = "replace(" + smsmsg + ",'[CONCERN]',ticket_desc)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		smsmsg = "replace(" + smsmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[DELIVERYDATE]', IF(so_delivered_date = '','',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'),'')))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		// smsmsg = "replace(" + smsmsg + ",'[EXECUTIVENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',ticketexe.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE2]',ticketexe.emp_mobile2)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',ticketexe.emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL2]',ticketexe.emp_email2)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[REGNO]',COALESCE(veh_reg_no, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		smsmsg = "replace(" + smsmsg + ",'[PRIORITY]',priorityticket_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',ticket_contact_id)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";

		try {
			StrSql = "SELECT "
					+ "	COALESCE(branch_id,1),"
					+ "" + emp_id + ", "
					+ "" + ticket_contact_id + ", "
					+ " '" + contact_name + "', "
					+ " '" + contact_mobile + "', "
					+ " " + (smsmsg) + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " 0, "
					+ " " + emp_id + " "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ticketexe on ticketexe.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = ticketexe.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = ticket_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id "
					// + " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type on tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = ticket_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_id = ticket_so_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm sales on sales.crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = ticket_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = ticket_jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_id = ticket_jcpsf_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " where ticket_id=" + ticket_id
					+ " GROUP BY ticket_id"
					+ " limit 1";
			StrSql = "insert into " + compdb(comp_id) + "axela_sms"
					+ "(sms_branch_id,"
					+ "sms_emp_id,"
					+ "sms_contact_id,"
					+ "sms_contact,"
					+ "sms_mobileno,"
					+ "sms_msg,"
					+ "sms_date ,"
					+ "sms_sent ,"
					+ "sms_entry_id)"
					+ " " + StrSql + "";
			// // SOP("StrSql----------insert sms-----------" + StrSql);
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

	protected void SendSMSToExecutive(String comp_id, String emp_mobile) throws SQLException {
		String smsmsg = config_ticket_new_sms_exe_format;

		smsmsg = "replace('" + smsmsg + "','[TICKETID]',ticket_id)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSUBJECT]',ticket_subject)";
		smsmsg = "replace(" + smsmsg + ",'[CONCERN]',ticket_desc)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETSTATUS]',ticketstatus_name)";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTIME]',DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i'))";
		smsmsg = "replace(" + smsmsg + ",'[TICKETCAT]',COALESCE(ticketcat_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[TICKETTYPE]',COALESCE(tickettype_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DUETIME]',DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i'))";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYID]', COALESCE(enquiry_id, ''))";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYDATE]', COALESCE(DATE_FORMAT(enquiry_date, '%d/%m/%Y'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[SODATE]', COALESCE(DATE_FORMAT(so_date, '%d/%m/%Y'), ''))";
		smsmsg = "replace(" + smsmsg + ",'[DELIVERYDATE]', IF(so_delivered_date = '','',COALESCE (DATE_FORMAT(so_delivered_date,'%d/%m/%Y'),'')))";
		smsmsg = "replace(" + smsmsg + ",'[BRANCHNAME]', COALESCE(branch_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[TEAMLEAD]', COALESCE(manager.emp_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[SALESEXE]', COALESCE(enquiryexe.emp_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[TICKETOWNER]',owner.emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',sesn.emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE2]',sesn.emp_mobile2)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',sesn.emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL2]',sesn.emp_email2)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',COALESCE(model_name, ''))";
		smsmsg = "replace(" + smsmsg + ",'[REGNO]',COALESCE(veh_reg_no, ''))";
		smsmsg = "replace(" + smsmsg + ",'[DEPARTMENT]',ticket_dept_name)";
		smsmsg = "replace(" + smsmsg + ",'[PRIORITY]',priorityticket_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTID]',ticket_contact_id)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',COALESCE(concat(contact_fname,' ', contact_lname),''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',COALESCE(contact_mobile1,''))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',COALESCE(contact_email1, ''))";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',ticket_customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',COALESCE(customer_name, ''))";

		try {
			StrSql = "SELECT"
					+ "	COALESCE(branch_id,1),"
					+ " " + emp_id + ","
					+ " sesn.emp_name ,"
					+ " '" + emp_mobile + "',"
					+ " " + (smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp owner on owner.emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sesn on sesn.emp_id = " + emp_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = sesn.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status on ticketstatus_id = ticket_ticketstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority on priorityticket_id = ticket_priorityticket_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = ticket_ticket_dept_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat on ticketcat_id = ticket_ticketcat_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type on tickettype_id = ticket_tickettype_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = ticket_customer_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = ticket_contact_id "
					// + " LEFT JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = ticket_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_id = ticket_so_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_crm sales on sales.crm_id = ticket_crm_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp enquiryexe ON enquiryexe.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = ticket_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = ticket_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = ticket_jc_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_id = ticket_jcpsf_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = enquiry_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " WHERE ticket_id = " + ticket_id
					+ " GROUP BY ticket_id"
					+ " LIMIT 1";

			// // SOP("StrSql===" + StrSql);
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_emp_id,"
					+ " sms_emp,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);

			// // SOP("StrSql------SendSMSToExecutive-----------------" + StrSql);
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

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_service_ticket.*, "
					+ " COALESCE(concat(title_desc,' ',contact_fname,' ',contact_lname),'') as contact_name, "
					+ " COALESCE(customer_name,'') as customer_name, COALESCE(customer_branch_id, 0) as customer_branch_id "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id =ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id =contact_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title on title_id =contact_title_id"
					+ " where ticket_id=" + ticket_parent_id + BranchAccess + ExeAccess;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// ticket_id = crs.getString("ticket_id");
					customer_branch_id = crs.getString("customer_branch_id");
					ticket_contact_id = crs.getString("ticket_contact_id");
					ticket_customer_id = crs.getString("ticket_customer_id");
					ticket_parent_id = crs.getString("ticket_parent_id");
					customer_name = crs.getString("customer_name");
					contact_name = crs.getString("contact_name");
					ticket_ticketstatus_id = crs.getString("ticket_ticketstatus_id");
					ticket_ticketcat_id = crs.getString("ticket_ticketcat_id");
					ticket_tickettype_id = crs.getString("ticket_tickettype_id");
					ticket_subject = crs.getString("ticket_subject");
					ticket_desc = crs.getString("ticket_desc");
					ticket_emp_id = crs.getString("ticket_emp_id");
					ticket_read = crs.getString("ticket_read");
					if (ticket_read.equals("0") && emp_id.equals(ticket_emp_id)) {
						StrSql = "Update " + compdb(comp_id) + "axela_service_ticket set ticket_read='1' where ticket_id=" + ticket_id + "";
						updateQuery(StrSql);
					}
					ticket_notes = crs.getString("ticket_notes");
					ticket_ticketsource_id = crs.getString("ticket_ticketsource_id");
					ticket_ticket_dept_id = crs.getString("ticket_ticket_dept_id");
					ticket_priorityticket_id = crs.getString("ticket_priorityticket_id");
					ticket_due_time = crs.getString("ticket_due_time");
				}
			} else {
				// msg = "<br><br>No Ticket found!";
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateContactDetails() {
		StrSql = " SELECT customer_name, customer_id, customer_branch_id, contact_id,"
				+ " concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
				+ " contact_email1, contact_email2, contact_mobile1, contact_mobile2"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id =contact_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id =contact_title_id"
				+ " where contact_id=" + CNumeric(ticket_contact_id) + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticket_contact_id = crs.getString("contact_id");
					ticket_customer_id = crs.getString("customer_id");
					customer_name = crs.getString("customer_name");
					customer_branch_id = crs.getString("customer_branch_id");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_name = crs.getString("contact_name");
				}
			}
			crs.close();
		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public void PopulateVehContactDetails(String comp_id) {
		StrSql = " SELECT customer_name, customer_id, customer_branch_id, contact_id,"
				+ " concat(title_desc,' ',contact_fname,' ',contact_lname) as contact_name,"
				+ " contact_email1, contact_email2, contact_mobile1, contact_mobile2, veh_id, veh_reg_no"
				+ " FROM " + compdb(comp_id) + "axela_customer_contact"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id =contact_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh on veh_contact_id = contact_id";
		if (!jc_id.equals("0")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_jc on jc_contact_id = contact_id";
		}
		StrSql += " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " WHERE 1 = 1";
		if (!veh_id.equals("0")) {
			StrSql += " and veh_id=" + veh_id + "";
		}
		if (!veh_chassis_no.equals("")) {
			StrSql += " and veh_chassis_no = '" + veh_chassis_no + "'";
		}
		if (!jc_id.equals("0")) {
			StrSql += " and jc_id=" + jc_id + "";
		}
		// // SOP("StrSql--" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticket_contact_id = crs.getString("contact_id");
					ticket_customer_id = crs.getString("customer_id");
					customer_name = crs.getString("customer_name");
					customer_branch_id = crs.getString("customer_branch_id");
					contact_email1 = crs.getString("contact_email1");
					contact_email2 = crs.getString("contact_email2");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_name = crs.getString("contact_name");
					veh_id = crs.getString("veh_id");
					if (!crs.getString("veh_reg_no").equals("")) {
						veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 4);
					}
				}
			}
			crs.close();
		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	public String PopulateTitle(String contact_title_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title" + " WHERE 1 =  1";
			if (!CNumeric(contact_title_id).equals("0")) {
				StrSql += " AND title_id = " + contact_title_id + "";
			}
			StrSql += " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0> Select </option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	protected void AddCustomerFields() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ " ("
					+ " customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_city_id,"
					+ " customer_accgroup_id,"
					+ " customer_type,"
					+ " customer_soe_id,"
					+ " customer_sob_id,"
					+ " customer_since,"
					+ " customer_address,"
					+ " customer_pin,"
					+ " customer_email1,"
					+ " customer_active,"
					+ " customer_notes,"
					+ " customer_entry_id,"
					+ " customer_entry_date,"
					+ " customer_modified_id,"
					+ " customer_modified_date"
					+ " )"
					+ " VALUES "
					+ " ("
					+ " " + search_customer_branch_id + "," // enquiry_branch_id
					+ " '" + search_customer_name + "'," // customer_name
					+ " '" + search_contact_mobile1 + "'," // contact_mobile1
					+ " " + search_customer_city_id + "," // contact_city_id
					+ " 32," // customer_accgroup_id
					+ " 1," // customer_type
					+ " '0'," // enquiry_soe_id
					+ " '0'," // enquiry_sob_id
					+ " '" + ToShortDate(kknow()) + "'," // customer_since
					+ " ''," // contact_address
					+ " ''," // contact_pin
					+ " '" + search_contact_email1 + "'," // contact_email1
					+ " '1'," // customer_active
					+ " ''," // customer_notes
					+ " " + emp_id + "," // customer_entry_id
					+ " '" + ToLongDate(kknow()) + "'," // customer_entry_date
					+ " 0," // customer_modified_id
					+ " ''"
					+ " )"; // customer_modified_date
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				search_customer_id = rs.getString(1);
				// SOP("search_customer_id----" + search_customer_id);
				ticket_customer_id = search_customer_id;
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void AddContactFields() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
					+ " ("
					+ " contact_customer_id,"
					+ " contact_contacttype_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_jobtitle,"
					+ " contact_email1,"
					+ " contact_mobile1,"
					+ " contact_phone1,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date"
					+ " )"
					+ " VALUES "
					+ " ("
					+ "	'" + search_customer_id + "'," // contact_customer_id
					+ " 1," // contact_contacttype_id
					+ " " + search_contact_title_id + "," // contact_title_id
					+ " '" + search_contact_fname + "'," // contact_fname
					+ " '" + search_contact_lname + "'," // contact_lname
					+ " ''," // contact_jobtitle
					+ " '" + search_contact_email1 + "'," // contact_email1
					+ " '" + search_contact_mobile1 + "'," // contact_mobile1
					+ " '" + search_contact_phone1 + "'," // contact_phone1
					+ " ''," // contact_address
					+ " " + search_customer_city_id + "," // contact_city_id
					+ " ''," // contact_pin
					+ " '1'," // contact_active
					+ " ''," // contact_notes
					+ " " + emp_id + "," // contact_entry_id
					+ " '" + ToLongDate(kknow()) + "'" // contact_entry_date
					+ " )";
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				search_contact_id = rs.getString(1);
				// SOP("search_contact_id----" + search_contact_id);
				ticket_contact_id = rs.getString(1);
			}
			rs.close();

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connemsgction rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
