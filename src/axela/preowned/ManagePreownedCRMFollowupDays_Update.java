package axela.preowned;
//Sangita 23nd may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedCRMFollowupDays_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String precrmfollowupdays_brand_id = "0";
	public String precrmfollowupdays_id = "0";
	public String precrmfollowupdays_daycount = "0";
	public String precrmfollowupdays_desc = "";
	public String QueryString = "";
	public String precrmfollowupdays_precrmtype_id = "0";
	public String precrmfollowupdays_exe_type = "0";
	public String dr_precrmfollowupdays_brand_id = "0";
	public int followup_chk = 0;
	public String precrmfollowupdays_script = "";
	public String precrmfollowupdays_active = "";
	public String precrmfollowupdays_lostfollowup = "";
	public String precrmfollowupdays_testdrivefollowup = "";
	public String precrmfollowupdays_homevisitfollowup = "";
	public String precrmfollowupdays_waitingperiod = "";
	public String precrmfollowupdays_so_inactive = "";
	// crm
	public String precrmfollowupdays_contactable_email_enable = "";
	public String precrmfollowupdays_contactable_email_sub = "";
	public String precrmfollowupdays_contactable_email_format = "";
	public String precrmfollowupdays_contactable_sms_format = "";
	public String precrmfollowupdays_contactable_sms_enable = "";

	public String precrmfollowupdays_noncontactable_email_sub = "";
	public String precrmfollowupdays_noncontactable_email_format = "";
	public String precrmfollowupdays_noncontactable_sms_format = "";
	public String precrmfollowupdays_noncontactable_email_enable = "";
	public String precrmfollowupdays_noncontactable_sms_enable = "";

	public String precrmfollowupdays_satisfied_email_enable = "";
	public String precrmfollowupdays_satisfied_email_sub = "";
	public String precrmfollowupdays_satisfied_email_format = "";
	public String precrmfollowupdays_satisfied_email_exe_sub = "";
	public String precrmfollowupdays_satisfied_email_exe_format = "";
	public String precrmfollowupdays_satisfied_sms_enable = "";
	public String precrmfollowupdays_satisfied_sms_format = "";
	public String precrmfollowupdays_satisfied_sms_exe_format = "";
	public String precrmfollowupdays_dissatisfied_email_enable = "";
	public String precrmfollowupdays_dissatisfied_email_sub = "";
	public String precrmfollowupdays_dissatisfied_email_format = "";
	public String precrmfollowupdays_dissatisfied_email_exe_sub = "";
	public String precrmfollowupdays_dissatisfied_email_exe_format = "";
	public String precrmfollowupdays_dissatisfied_sms_enable = "";
	public String precrmfollowupdays_dissatisfied_sms_format = "";
	public String precrmfollowupdays_dissatisfied_sms_exe_format = "";

	public String precrmfollowupdays_entry_id = "0";
	public String precrmfollowupdays_entry_date = "";
	public String precrmfollowupdays_modified_id = "0";
	public String precrmfollowupdays_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				precrmfollowupdays_id = PadQuotes(request.getParameter("precrmfollowupdays_id"));
				precrmfollowupdays_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				// SOP("status--------" + status);

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						precrmfollowupdays_daycount = "0";
						precrmfollowupdays_active = "1";
					} else {
						GetValues(request, response);
						precrmfollowupdays_entry_id = emp_id;
						precrmfollowupdays_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedcrmfollowupdays.jsp?dr_brand=" + precrmfollowupdays_brand_id
									+ "&msg=Pre-Owned CRM Follow-up Day Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Followup Days".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Followup Days".equals(deleteB)) {
						GetValues(request, response);
						precrmfollowupdays_modified_id = emp_id;
						precrmfollowupdays_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedcrmfollowupdays.jsp?dr_brand=" + precrmfollowupdays_brand_id
									+ "&msg=Pre-Owned CRM Follow-up Day Updated Successfully!"));
						}
					} else if ("Delete Followup Days".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedcrmfollowupdays.jsp?dr_brand=" + precrmfollowupdays_brand_id
									+ "&msg=Pre-Owned CRM Follow-up Day Deleted Successfully!"));
						}
					}
				}

			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		precrmfollowupdays_exe_type = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowupdays_exe_type")));
		precrmfollowupdays_precrmtype_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowupdays_precrmtype_id")));
		precrmfollowupdays_daycount = CNumeric(PadQuotes(request.getParameter("txt_precrmfollowupdays_daycount")));
		precrmfollowupdays_desc = PadQuotes(request.getParameter("txt_precrmfollowupdays_desc"));
		precrmfollowupdays_brand_id = CNumeric(PadQuotes(request.getParameter("dr_precrmfollowupdays_brand_id")));
		precrmfollowupdays_script = PadQuotes(request.getParameter("precrmfollowupdays_script"));
		precrmfollowupdays_contactable_email_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_contactable_email_enable"));
		precrmfollowupdays_contactable_sms_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_contactable_sms_enable"));
		precrmfollowupdays_noncontactable_email_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_noncontactable_email_enable"));
		precrmfollowupdays_noncontactable_sms_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_noncontactable_sms_enable"));

		precrmfollowupdays_satisfied_email_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_satisfied_email_enable"));
		precrmfollowupdays_satisfied_sms_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_satisfied_sms_enable"));
		precrmfollowupdays_dissatisfied_email_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_dissatisfied_email_enable"));
		precrmfollowupdays_dissatisfied_sms_enable = PadQuotes(request.getParameter("chk_precrmfollowupdays_dissatisfied_sms_enable"));
		precrmfollowupdays_testdrivefollowup = PadQuotes(request.getParameter("chk_precrmfollowupdays_testdrivefollowup"));
		precrmfollowupdays_homevisitfollowup = PadQuotes(request.getParameter("chk_precrmfollowupdays_homevisitfollowup"));
		precrmfollowupdays_active = PadQuotes(request.getParameter("chk_precrmfollowupdays_active"));

		// SOP("precrmfollowupdays_active--------" + precrmfollowupdays_active);

		if (precrmfollowupdays_active.equals("on")) {
			precrmfollowupdays_active = "1";
		} else {
			precrmfollowupdays_active = "0";
		}
		precrmfollowupdays_lostfollowup = PadQuotes(request.getParameter("chk_precrmfollowupdays_lostfollowup"));
		if (precrmfollowupdays_lostfollowup.equals("on")) {
			precrmfollowupdays_lostfollowup = "1";
		} else {
			precrmfollowupdays_lostfollowup = "0";
		}
		if (precrmfollowupdays_testdrivefollowup.equals("on")) {
			precrmfollowupdays_testdrivefollowup = "1";
		} else {
			precrmfollowupdays_testdrivefollowup = "0";
		}
		if (precrmfollowupdays_homevisitfollowup.equals("on")) {
			precrmfollowupdays_homevisitfollowup = "1";
		} else {
			precrmfollowupdays_homevisitfollowup = "0";
		}
		precrmfollowupdays_waitingperiod = PadQuotes(request.getParameter("chk_precrmfollowupdays_waitingperiod"));
		if (precrmfollowupdays_waitingperiod.equals("on")) {
			precrmfollowupdays_waitingperiod = "1";
		} else {
			precrmfollowupdays_waitingperiod = "0";
		}

		precrmfollowupdays_so_inactive = PadQuotes(request.getParameter("chk_precrmfollowupdays_so_inactive"));
		if (precrmfollowupdays_so_inactive.equals("on")) {
			precrmfollowupdays_so_inactive = "1";
		} else {
			precrmfollowupdays_so_inactive = "0";
		}

		if (precrmfollowupdays_contactable_email_enable.equals("on")) {
			precrmfollowupdays_contactable_email_enable = "1";
		} else {
			precrmfollowupdays_contactable_email_enable = "0";
		}
		// SOP("precrmfollowupdays_contactable_email_enable---------" + precrmfollowupdays_contactable_email_enable);
		if (precrmfollowupdays_contactable_sms_enable.equals("on")) {
			precrmfollowupdays_contactable_sms_enable = "1";
		} else {
			precrmfollowupdays_contactable_sms_enable = "0";
		}

		if (precrmfollowupdays_noncontactable_email_enable.equals("on")) {
			precrmfollowupdays_noncontactable_email_enable = "1";
		} else {
			precrmfollowupdays_noncontactable_email_enable = "0";
		}
		if (precrmfollowupdays_noncontactable_sms_enable.equals("on")) {
			precrmfollowupdays_noncontactable_sms_enable = "1";
		} else {
			precrmfollowupdays_noncontactable_sms_enable = "0";
		}

		if (precrmfollowupdays_satisfied_email_enable.equals("on")) {
			precrmfollowupdays_satisfied_email_enable = "1";
		} else {
			precrmfollowupdays_satisfied_email_enable = "0";
		}
		if (precrmfollowupdays_satisfied_sms_enable.equals("on")) {
			precrmfollowupdays_satisfied_sms_enable = "1";
		} else {
			precrmfollowupdays_satisfied_sms_enable = "0";
		}
		if (precrmfollowupdays_dissatisfied_email_enable.equals("on")) {
			precrmfollowupdays_dissatisfied_email_enable = "1";
		} else {
			precrmfollowupdays_dissatisfied_email_enable = "0";
		}
		if (precrmfollowupdays_dissatisfied_sms_enable.equals("on")) {
			precrmfollowupdays_dissatisfied_sms_enable = "1";
		} else {
			precrmfollowupdays_dissatisfied_sms_enable = "0";
		}

		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (precrmfollowupdays_brand_id.equals("0")) {
			msg = msg + "<br>Select Brand!";
		}
		if (precrmfollowupdays_precrmtype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}
		if (precrmfollowupdays_exe_type.equals("0")) {
			msg = msg + "<br>Select Pre-Owned Consultant Type!";
		}
		if (precrmfollowupdays_daycount.equals("0")) {
			msg = msg + "<br>Enter Days!";
		} else {
			try {
				if (!precrmfollowupdays_daycount.equals("0") && !precrmfollowupdays_exe_type.equals("0")) {
					StrSql = "SELECT precrmfollowupdays_daycount"
							+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
							+ " WHERE precrmfollowupdays_daycount = " + precrmfollowupdays_daycount + ""
							+ " AND precrmfollowupdays_desc = '" + precrmfollowupdays_desc + "'"
							+ " AND precrmfollowupdays_brand_id = " + precrmfollowupdays_brand_id + ""
							+ " AND precrmfollowupdays_exe_type = " + precrmfollowupdays_exe_type + "";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND precrmfollowupdays_id!= " + precrmfollowupdays_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					// SOP("StrSql==" + StrSql);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Days Found!";
					}
					crs.close();
				}
				if (!precrmfollowupdays_exe_type.equals("0")) {
					StrSql = "SELECT precrmfollowupdays_exe_type"
							+ " FROM " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
							+ " WHERE precrmfollowupdays_daycount = " + precrmfollowupdays_daycount + ""
							+ " AND precrmfollowupdays_desc = '" + precrmfollowupdays_desc + "'"
							+ " AND precrmfollowupdays_brand_id = " + precrmfollowupdays_brand_id + ""
							+ " AND precrmfollowupdays_exe_type = " + precrmfollowupdays_exe_type + "";
					// + " AND precrmfollowupdays_exe_type = " + precrmfollowupdays_exe_type + "";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND precrmfollowupdays_id!= " + precrmfollowupdays_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					// SOP("StrSql===" + StrSql);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Pre-Owned Consultant Type Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (precrmfollowupdays_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (precrmfollowupdays_script.equals("")) {
			msg = msg + "<br>Enter Script!";
		}

		if (precrmfollowupdays_lostfollowup.equals("1")) {
			followup_chk++;
		}
		if (precrmfollowupdays_testdrivefollowup.equals("1")) {
			followup_chk++;
		}
		if (precrmfollowupdays_homevisitfollowup.equals("1")) {
			followup_chk++;
		}
		// SOP("followup_chk----------" + followup_chk);
		if (followup_chk > 1) {
			msg = msg + "<br>Select Either Lost Or Test Drive Or Home Visit Follow-up!";
		}

	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				precrmfollowupdays_id = ExecuteQuery("Select (coalesce(max(precrmfollowupdays_id),0)+1) from " + compdb(comp_id) + "axela_preowned_crmfollowupdays");
				// SOP("precrmfollowupdays_id------------" + precrmfollowupdays_id);
				StrSql = "Insert into " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
						+ " (precrmfollowupdays_id,"
						+ " precrmfollowupdays_brand_id,"
						+ " precrmfollowupdays_precrmtype_id,"
						+ " precrmfollowupdays_exe_type,"
						+ " precrmfollowupdays_daycount,"
						+ " precrmfollowupdays_desc,"
						+ " precrmfollowupdays_script,"
						+ " precrmfollowupdays_lostfollowup,"
						+ " precrmfollowupdays_testdrivefollowup,"
						+ " precrmfollowupdays_homevisitfollowup,"
						+ " precrmfollowupdays_waitingperiod,"
						+ " precrmfollowupdays_so_inactive,"
						+ " precrmfollowupdays_active,"
						+ " precrmfollowupdays_contactable_email_sub,"
						+ " precrmfollowupdays_contactable_email_format,"
						+ " precrmfollowupdays_contactable_email_exe_format,"
						+ " precrmfollowupdays_contactable_sms_format,"
						+ " precrmfollowupdays_noncontactable_email_sub,"
						+ " precrmfollowupdays_noncontactable_email_exe_format,"
						+ " precrmfollowupdays_noncontactable_email_format,"
						+ " precrmfollowupdays_noncontactable_sms_format,"
						+ " precrmfollowupdays_satisfied_email_sub,"
						+ " precrmfollowupdays_satisfied_email_format,"
						+ " precrmfollowupdays_satisfied_email_exe_format,"
						+ " precrmfollowupdays_satisfied_sms_format,"
						+ " precrmfollowupdays_dissatisfied_email_sub,"
						+ " precrmfollowupdays_dissatisfied_email_format,"
						+ " precrmfollowupdays_dissatisfied_email_exe_format,"
						+ " precrmfollowupdays_dissatisfied_sms_format,"
						+ " precrmfollowupdays_entry_id,"
						+ " precrmfollowupdays_entry_date)"
						+ " values"
						+ " (" + precrmfollowupdays_id + ","
						+ " " + precrmfollowupdays_brand_id + ","
						+ " " + precrmfollowupdays_precrmtype_id + ","
						+ " " + precrmfollowupdays_exe_type + ","
						+ " " + precrmfollowupdays_daycount + ","
						+ " '" + precrmfollowupdays_desc + "',"
						+ " '" + precrmfollowupdays_script + "',"
						+ " '" + precrmfollowupdays_lostfollowup + "',"
						+ " '" + precrmfollowupdays_testdrivefollowup + "',"
						+ " '" + precrmfollowupdays_homevisitfollowup + "',"
						+ " '" + precrmfollowupdays_waitingperiod + "',"
						+ " '" + precrmfollowupdays_so_inactive + "',"
						+ " " + precrmfollowupdays_active + ","
						+ " '" + precrmfollowupdays_contactable_email_sub + "',"
						+ " '" + precrmfollowupdays_contactable_email_format + "',"
						+ " '', " // precrmfollowupdays_contactable_email_exe_format
						+ " '" + precrmfollowupdays_contactable_sms_format + "',"
						+ " '" + precrmfollowupdays_noncontactable_email_sub + "',"
						+ " '', " // precrmfollowupdays_noncontactable_email_exe_format
						+ " '" + precrmfollowupdays_noncontactable_email_format + "',"
						+ " '" + precrmfollowupdays_noncontactable_sms_format + "',"
						+ " '" + precrmfollowupdays_satisfied_email_sub + "',"
						+ " '" + precrmfollowupdays_satisfied_email_format + "',"
						+ " '', " // precrmfollowupdays_satisfied_email_exe_format
						+ " '" + precrmfollowupdays_satisfied_sms_format + "',"
						+ " '" + precrmfollowupdays_dissatisfied_email_sub + "',"
						+ " '" + precrmfollowupdays_dissatisfied_email_format + "',"
						+ " '', " // precrmfollowupdays_dissatisfied_email_exe_format
						+ " '" + precrmfollowupdays_dissatisfied_sms_format + "',"
						+ " " + precrmfollowupdays_entry_id + ","
						+ " '" + precrmfollowupdays_entry_date + "')";
				// SOP("StrSql-------------Add----" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select precrmfollowupdays_brand_id,   precrmfollowupdays_exe_type, precrmfollowupdays_precrmtype_id,  "
					+ " precrmfollowupdays_daycount, precrmfollowupdays_desc, precrmfollowupdays_script, precrmfollowupdays_active,"
					+ " precrmfollowupdays_contactable_email_enable,"
					+ " precrmfollowupdays_contactable_email_sub,"
					+ " precrmfollowupdays_contactable_email_format,"
					+ " precrmfollowupdays_contactable_sms_enable,"
					+ " precrmfollowupdays_contactable_sms_format,"
					+ " precrmfollowupdays_noncontactable_email_enable,"
					+ " precrmfollowupdays_noncontactable_email_sub,"
					+ " precrmfollowupdays_noncontactable_email_format,"
					+ " precrmfollowupdays_noncontactable_sms_enable,"
					+ " precrmfollowupdays_noncontactable_sms_format,"

					+ " precrmfollowupdays_satisfied_email_enable,"
					+ " precrmfollowupdays_satisfied_email_sub,"
					+ " precrmfollowupdays_satisfied_email_format,"
					+ " precrmfollowupdays_satisfied_sms_enable,"
					+ " precrmfollowupdays_satisfied_sms_format,"
					+ " precrmfollowupdays_dissatisfied_email_enable,"
					+ " precrmfollowupdays_dissatisfied_email_sub,"
					+ " precrmfollowupdays_dissatisfied_email_format,"
					+ " precrmfollowupdays_dissatisfied_sms_enable, "
					+ " precrmfollowupdays_dissatisfied_sms_format,"
					+ " precrmfollowupdays_lostfollowup, precrmfollowupdays_testdrivefollowup, precrmfollowupdays_homevisitfollowup,"
					+ " precrmfollowupdays_waitingperiod, precrmfollowupdays_so_inactive, "
					+ "precrmfollowupdays_entry_id, precrmfollowupdays_entry_date,"
					+ "precrmfollowupdays_modified_id, precrmfollowupdays_modified_date from " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
					+ " where precrmfollowupdays_id = " + precrmfollowupdays_id + "";
			// SOP("StrSql-----populate----- " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					precrmfollowupdays_brand_id = crs.getString("precrmfollowupdays_brand_id");
					precrmfollowupdays_precrmtype_id = crs.getString("precrmfollowupdays_precrmtype_id");
					precrmfollowupdays_exe_type = crs.getString("precrmfollowupdays_exe_type");
					precrmfollowupdays_daycount = crs.getString("precrmfollowupdays_daycount");
					precrmfollowupdays_desc = crs.getString("precrmfollowupdays_desc");
					precrmfollowupdays_desc = crs.getString("precrmfollowupdays_desc");
					precrmfollowupdays_script = crs.getString("precrmfollowupdays_script");
					precrmfollowupdays_active = crs.getString("precrmfollowupdays_active");
					precrmfollowupdays_contactable_email_enable = crs.getString("precrmfollowupdays_contactable_email_enable");
					precrmfollowupdays_contactable_email_sub = crs.getString("precrmfollowupdays_contactable_email_sub");
					precrmfollowupdays_contactable_email_format = crs.getString("precrmfollowupdays_contactable_email_format");
					precrmfollowupdays_contactable_sms_enable = crs.getString("precrmfollowupdays_contactable_sms_enable");
					precrmfollowupdays_contactable_sms_format = crs.getString("precrmfollowupdays_contactable_sms_format");
					precrmfollowupdays_noncontactable_email_enable = crs.getString("precrmfollowupdays_noncontactable_email_enable");
					precrmfollowupdays_noncontactable_email_sub = crs.getString("precrmfollowupdays_noncontactable_email_sub");
					precrmfollowupdays_noncontactable_email_format = crs.getString("precrmfollowupdays_noncontactable_email_format");
					precrmfollowupdays_noncontactable_sms_enable = crs.getString("precrmfollowupdays_noncontactable_sms_enable");
					precrmfollowupdays_noncontactable_sms_format = crs.getString("precrmfollowupdays_noncontactable_sms_format");

					precrmfollowupdays_satisfied_email_enable = crs.getString("precrmfollowupdays_satisfied_email_enable");
					precrmfollowupdays_satisfied_email_sub = crs.getString("precrmfollowupdays_satisfied_email_sub");
					precrmfollowupdays_satisfied_email_format = crs.getString("precrmfollowupdays_satisfied_email_format");
					precrmfollowupdays_satisfied_sms_enable = crs.getString("precrmfollowupdays_satisfied_sms_enable");
					precrmfollowupdays_satisfied_sms_format = crs.getString("precrmfollowupdays_satisfied_sms_format");
					precrmfollowupdays_dissatisfied_email_enable = crs.getString("precrmfollowupdays_dissatisfied_email_enable");
					precrmfollowupdays_dissatisfied_email_sub = crs.getString("precrmfollowupdays_dissatisfied_email_sub");
					precrmfollowupdays_dissatisfied_email_format = crs.getString("precrmfollowupdays_dissatisfied_email_format");
					precrmfollowupdays_dissatisfied_sms_enable = crs.getString("precrmfollowupdays_dissatisfied_sms_enable");
					precrmfollowupdays_dissatisfied_sms_format = crs.getString("precrmfollowupdays_dissatisfied_sms_format");
					precrmfollowupdays_lostfollowup = crs.getString("precrmfollowupdays_lostfollowup");
					precrmfollowupdays_testdrivefollowup = crs.getString("precrmfollowupdays_testdrivefollowup");
					precrmfollowupdays_homevisitfollowup = crs.getString("precrmfollowupdays_homevisitfollowup");
					precrmfollowupdays_waitingperiod = crs.getString("precrmfollowupdays_waitingperiod");
					precrmfollowupdays_so_inactive = crs.getString("precrmfollowupdays_so_inactive");
					precrmfollowupdays_entry_id = crs.getString("precrmfollowupdays_entry_id");
					if (!precrmfollowupdays_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(precrmfollowupdays_entry_id));
						entry_date = strToLongDate(crs.getString("precrmfollowupdays_entry_date"));
					}
					precrmfollowupdays_modified_id = crs.getString("precrmfollowupdays_modified_id");
					if (!precrmfollowupdays_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(precrmfollowupdays_modified_id));
						modified_date = strToLongDate(crs.getString("precrmfollowupdays_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid CRM Follow-up Day!"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crmfollowupdays"
						+ " SET"
						+ " precrmfollowupdays_brand_id = '" + precrmfollowupdays_brand_id + "',"
						+ " precrmfollowupdays_precrmtype_id = '" + precrmfollowupdays_precrmtype_id + "',"
						+ " precrmfollowupdays_exe_type = '" + precrmfollowupdays_exe_type + "',"
						+ " precrmfollowupdays_daycount = " + precrmfollowupdays_daycount + ","
						+ " precrmfollowupdays_desc = '" + precrmfollowupdays_desc + "',"
						+ " precrmfollowupdays_script = '" + precrmfollowupdays_script + "',"
						+ " precrmfollowupdays_lostfollowup = '" + precrmfollowupdays_lostfollowup + "',"
						+ " precrmfollowupdays_testdrivefollowup = '" + precrmfollowupdays_testdrivefollowup + "',"
						+ " precrmfollowupdays_homevisitfollowup = '" + precrmfollowupdays_homevisitfollowup + "',"
						+ " precrmfollowupdays_waitingperiod = '" + precrmfollowupdays_waitingperiod + "',"
						+ " precrmfollowupdays_so_inactive = '" + precrmfollowupdays_so_inactive + "',"
						+ " precrmfollowupdays_active = '" + precrmfollowupdays_active + "',"
						+ " precrmfollowupdays_contactable_email_enable = '" + precrmfollowupdays_contactable_email_enable + "',"
						+ " precrmfollowupdays_contactable_sms_enable = '" + precrmfollowupdays_contactable_sms_enable + "',"
						+ " precrmfollowupdays_noncontactable_email_enable = '" + precrmfollowupdays_noncontactable_email_enable + "',"
						+ " precrmfollowupdays_noncontactable_sms_enable = '" + precrmfollowupdays_noncontactable_sms_enable + "',"
						+ " precrmfollowupdays_satisfied_email_enable = '" + precrmfollowupdays_satisfied_email_enable + "',"
						+ " precrmfollowupdays_satisfied_sms_enable = '" + precrmfollowupdays_satisfied_sms_enable + "',"
						+ " precrmfollowupdays_dissatisfied_email_enable = '" + precrmfollowupdays_dissatisfied_email_enable + "',"
						+ " precrmfollowupdays_dissatisfied_sms_enable = '" + precrmfollowupdays_dissatisfied_sms_enable + "',"
						+ " precrmfollowupdays_modified_id = " + precrmfollowupdays_modified_id + ","

						+ " precrmfollowupdays_modified_date = '" + precrmfollowupdays_modified_date + "'"
						+ " where precrmfollowupdays_id = " + precrmfollowupdays_id + "";
				// SOP("StrSql-------update-------" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {

				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		StrSql = "select precrmfollowup_precrmfollowupdays_id from " + compdb(comp_id) + "axela_preowned_crmfollowup where precrmfollowup_precrmfollowupdays_id = "
				+ precrmfollowupdays_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>CRM Follow-up days is associated with CRM Follow-up!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_preowned_crmfollowupdays where precrmfollowupdays_id =" + precrmfollowupdays_id;
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id = 2"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), precrmfollowupdays_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCRMExecutiveType() {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", precrmfollowupdays_exe_type)).append(">CRM Executive</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", precrmfollowupdays_exe_type)).append(">Sales Consultant</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", precrmfollowupdays_exe_type)).append(">Manager</option>\n");
		return Str.toString();
	}

	public String PopulateCRMType(String precrmfollowupdays_precrmtype_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select precrmtype_id, precrmtype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_crm_type order by precrmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("precrmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("precrmtype_id"), precrmfollowupdays_precrmtype_id));
				Str.append(">").append(crs.getString("precrmtype_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}
}
