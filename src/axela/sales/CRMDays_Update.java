package axela.sales;
//divya 31st may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class CRMDays_Update extends Connect {

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
	public int followup_chk = 0;
	public String crmdays_brand_id = "0";
	public String crmdays_crmtype_id = "0";
	public String crmdays_exe_type = "0";
	public String dr_brand_id = "0";
	public String crmdays_id = "0";
	public String crmdays_daycount = "0";
	public String crmdays_desc = "";
	public String crmdays_script = "";
	public String crmdays_active = "";
	public String crmdays_lostfollowup = "";
	public String crmdays_testdrivefollowup = "";
	public String crmdays_homevisitfollowup = "";
	public String crmdays_waitingperiod = "";
	public String crmdays_so_inactive = "";
	public String QueryString = "";
	// crm
	public String crmdays_contactable_email_enable = "";
	public String crmdays_contactable_email_sub = "";
	public String crmdays_contactable_email_format = "";
	public String crmdays_contactable_sms_format = "";
	public String crmdays_contactable_sms_enable = "";

	public String crmdays_noncontactable_email_sub = "";
	public String crmdays_noncontactable_email_format = "";
	public String crmdays_noncontactable_sms_format = "";
	public String crmdays_noncontactable_email_enable = "";
	public String crmdays_noncontactable_sms_enable = "";

	public String crmdays_satisfied_email_enable = "";
	public String crmdays_satisfied_email_sub = "";
	public String crmdays_satisfied_email_format = "";
	public String crmdays_satisfied_email_exe_sub = "";
	public String crmdays_satisfied_email_exe_format = "";
	public String crmdays_satisfied_sms_enable = "";
	public String crmdays_satisfied_sms_format = "";
	public String crmdays_satisfied_sms_exe_format = "";
	public String crmdays_dissatisfied_email_enable = "";
	public String crmdays_dissatisfied_email_sub = "";
	public String crmdays_dissatisfied_email_format = "";
	public String crmdays_dissatisfied_email_exe_sub = "";
	public String crmdays_dissatisfied_email_exe_format = "";
	public String crmdays_dissatisfied_sms_enable = "";
	public String crmdays_dissatisfied_sms_format = "";
	public String crmdays_dissatisfied_sms_exe_format = "";
	public String crmdays_entry_id = "0";
	public String crmdays_entry_date = "";
	public String crmdays_modified_id = "0";
	public String crmdays_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				crmdays_id = PadQuotes(request.getParameter("crmdays_id"));
				QueryString = PadQuotes(request.getQueryString());
				crmdays_brand_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_brand_id")));
				dr_brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				if (!dr_brand_id.equals("0")) {
					crmdays_brand_id = dr_brand_id;
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						crmdays_daycount = "0";
						crmdays_active = "1";
					} else {
						GetValues(request, response);
						crmdays_entry_id = emp_id;
						crmdays_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("crmdays.jsp?dr_brand=" + crmdays_brand_id + "&msg=CRM Day Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Followup Days".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Followup Days".equals(deleteB)) {
						GetValues(request, response);
						crmdays_modified_id = emp_id;
						crmdays_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("crmdays.jsp?dr_brand=" + crmdays_brand_id + "&msg=CRM Day Updated Successfully!"));
						}
					} else if ("Delete Followup Days".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("crmdays.jsp?dr_brand=" + crmdays_brand_id + "&msg=CRM Day Deleted Successfully!"));
						}
					}
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
		crmdays_crmtype_id = CNumeric(PadQuotes(request.getParameter("dr_crmdays_crmtype_id")));
		crmdays_exe_type = CNumeric(PadQuotes(request.getParameter("dr_crmdays_exe_type")));
		crmdays_daycount = CNumeric(PadQuotes(request.getParameter("txt_crmdays_daycount")));
		crmdays_desc = PadQuotes(request.getParameter("txt_crmdays_desc"));
		crmdays_script = PadQuotes(request.getParameter("txt_crmdays_script"));
		crmdays_contactable_email_enable = PadQuotes(request.getParameter("chk_crmdays_contactable_email_enable"));
		crmdays_contactable_sms_enable = PadQuotes(request.getParameter("chk_crmdays_contactable_sms_enable"));
		crmdays_noncontactable_email_enable = PadQuotes(request.getParameter("chk_crmdays_noncontactable_email_enable"));
		crmdays_noncontactable_sms_enable = PadQuotes(request.getParameter("chk_crmdays_noncontactable_sms_enable"));

		crmdays_satisfied_email_enable = PadQuotes(request.getParameter("chk_crmdays_satisfied_email_enable"));
		crmdays_satisfied_sms_enable = PadQuotes(request.getParameter("chk_crmdays_satisfied_sms_enable"));
		crmdays_dissatisfied_email_enable = PadQuotes(request.getParameter("chk_crmdays_dissatisfied_email_enable"));
		crmdays_dissatisfied_sms_enable = PadQuotes(request.getParameter("chk_crmdays_dissatisfied_sms_enable"));
		crmdays_testdrivefollowup = PadQuotes(request.getParameter("chk_crmdays_testdrivefollowup"));
		crmdays_homevisitfollowup = PadQuotes(request.getParameter("chk_crmdays_homevisitfollowup"));
		crmdays_active = PadQuotes(request.getParameter("chk_crmdays_active"));
		if (crmdays_active.equals("on")) {
			crmdays_active = "1";
		} else {
			crmdays_active = "0";
		}
		crmdays_lostfollowup = PadQuotes(request.getParameter("chk_crmdays_lost"));
		if (crmdays_lostfollowup.equals("on")) {
			crmdays_lostfollowup = "1";
		} else {
			crmdays_lostfollowup = "0";
		}
		if (crmdays_testdrivefollowup.equals("on")) {
			crmdays_testdrivefollowup = "1";
		} else {
			crmdays_testdrivefollowup = "0";
		}
		if (crmdays_homevisitfollowup.equals("on")) {
			crmdays_homevisitfollowup = "1";
		} else {
			crmdays_homevisitfollowup = "0";
		}
		crmdays_waitingperiod = PadQuotes(request.getParameter("chk_crmdays_waitingperiod"));
		if (crmdays_waitingperiod.equals("on")) {
			crmdays_waitingperiod = "1";
		} else {
			crmdays_waitingperiod = "0";
		}

		crmdays_so_inactive = PadQuotes(request.getParameter("chk_crmdays_so_inactive"));
		if (crmdays_so_inactive.equals("on")) {
			crmdays_so_inactive = "1";
		} else {
			crmdays_so_inactive = "0";
		}

		if (crmdays_contactable_email_enable.equals("on")) {
			crmdays_contactable_email_enable = "1";
		} else {
			crmdays_contactable_email_enable = "0";
		}
		if (crmdays_contactable_sms_enable.equals("on")) {
			crmdays_contactable_sms_enable = "1";
		} else {
			crmdays_contactable_sms_enable = "0";
		}

		if (crmdays_noncontactable_email_enable.equals("on")) {
			crmdays_noncontactable_email_enable = "1";
		} else {
			crmdays_noncontactable_email_enable = "0";
		}
		if (crmdays_noncontactable_sms_enable.equals("on")) {
			crmdays_noncontactable_sms_enable = "1";
		} else {
			crmdays_noncontactable_sms_enable = "0";
		}

		if (crmdays_satisfied_email_enable.equals("on")) {
			crmdays_satisfied_email_enable = "1";
		} else {
			crmdays_satisfied_email_enable = "0";
		}
		if (crmdays_satisfied_sms_enable.equals("on")) {
			crmdays_satisfied_sms_enable = "1";
		} else {
			crmdays_satisfied_sms_enable = "0";
		}
		if (crmdays_dissatisfied_email_enable.equals("on")) {
			crmdays_dissatisfied_email_enable = "1";
		} else {
			crmdays_dissatisfied_email_enable = "0";
		}
		if (crmdays_dissatisfied_sms_enable.equals("on")) {
			crmdays_dissatisfied_sms_enable = "1";
		} else {
			crmdays_dissatisfied_sms_enable = "0";
		}

		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (crmdays_brand_id.equals("0")) {
			msg = msg + "<br>Select Brand!";
		}
		if (crmdays_crmtype_id.equals("0")) {
			msg = msg + "<br>Select CRM Type!";
		}
		if (crmdays_exe_type.equals("0")) {
			msg = msg + "<br>Select Sales Consultant!";
		}
		if (crmdays_daycount.equals("0")) {
			msg = msg + "<br>Enter Days!";
		} else {
			try {
				if (!crmdays_daycount.equals("0") && !crmdays_exe_type.equals("0")) {
					StrSql = "SELECT crmdays_daycount"
							+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
							+ " WHERE crmdays_daycount = " + crmdays_daycount + ""
							+ " AND crmdays_desc = '" + crmdays_desc + "'"
							+ " AND crmdays_brand_id = " + crmdays_brand_id + ""
							+ " AND crmdays_crmtype_id = " + crmdays_crmtype_id + ""
							+ " AND crmdays_exe_type = " + crmdays_exe_type + "";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND crmdays_id!= " + crmdays_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					SOP("StrSql==" + StrSql);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Days Found!";
					}
					crs.close();
				}
				if (!crmdays_crmtype_id.equals("0") && !crmdays_exe_type.equals("0")) {
					StrSql = "SELECT crmdays_exe_type"
							+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
							+ " WHERE crmdays_daycount = " + crmdays_daycount + ""
							+ " AND crmdays_desc = '" + crmdays_desc + "'"
							+ " AND crmdays_brand_id = " + crmdays_brand_id + ""
							+ " AND crmdays_crmtype_id = " + crmdays_crmtype_id + ""
							+ " AND crmdays_exe_type = " + crmdays_exe_type + "";
					// + " AND crmdays_exe_type = " + crmdays_exe_type + "";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND crmdays_id!= " + crmdays_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					// SOP("StrSql===" + StrSql);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Sales Consultant Type Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (crmdays_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (crmdays_script.equals("")) {
			msg = msg + "<br>Enter Script!";
		}
		if (!crmdays_crmtype_id.equals("1")) {
			crmdays_lostfollowup = "0";
			crmdays_testdrivefollowup = "0";
			crmdays_homevisitfollowup = "0";
		}
		if (!crmdays_crmtype_id.equals("2")) {
			crmdays_waitingperiod = "0";
			crmdays_so_inactive = "0";
		}
		if (crmdays_lostfollowup.equals("1")) {
			followup_chk++;
		}
		if (crmdays_testdrivefollowup.equals("1")) {
			followup_chk++;
		}
		if (crmdays_homevisitfollowup.equals("1")) {
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
				crmdays_id = ExecuteQuery("Select (COALESCE(MAX(crmdays_id),0)+1)"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_crmdays"
						+ " (crmdays_id,"
						+ " crmdays_brand_id,"
						+ " crmdays_crmtype_id,"
						+ " crmdays_exe_type,"
						+ " crmdays_daycount,"
						+ " crmdays_desc,"
						+ " crmdays_script,"
						+ " crmdays_lostfollowup,"
						+ " crmdays_testdrivefollowup,"
						+ " crmdays_homevisitfollowup,"
						+ " crmdays_waitingperiod,"
						+ " crmdays_so_inactive,"
						+ " crmdays_active,"
						+ " crmdays_contactable_email_sub,"
						+ " crmdays_contactable_email_format,"
						+ " crmdays_contactable_email_exe_format,"
						+ " crmdays_contactable_sms_format,"
						+ " crmdays_noncontactable_email_sub,"
						+ " crmdays_noncontactable_email_exe_format,"
						+ " crmdays_noncontactable_email_format,"
						+ " crmdays_noncontactable_sms_format,"
						+ " crmdays_satisfied_email_sub,"
						+ " crmdays_satisfied_email_format,"
						+ " crmdays_satisfied_email_exe_format,"
						+ " crmdays_satisfied_sms_format,"
						+ " crmdays_dissatisfied_email_sub,"
						+ " crmdays_dissatisfied_email_format,"
						+ " crmdays_dissatisfied_email_exe_format,"
						+ " crmdays_dissatisfied_sms_format,"
						+ " crmdays_entry_id,"
						+ " crmdays_entry_date)"
						+ " VALUES"
						+ " (" + crmdays_id + ","
						+ " " + crmdays_brand_id + ","
						+ " " + crmdays_crmtype_id + ","
						+ " " + crmdays_exe_type + ","
						+ " " + crmdays_daycount + ","
						+ " '" + crmdays_desc + "',"
						+ " '" + crmdays_script + "',"
						+ " '" + crmdays_lostfollowup + "',"
						+ " '" + crmdays_testdrivefollowup + "',"
						+ " '" + crmdays_homevisitfollowup + "',"
						+ " '" + crmdays_waitingperiod + "',"
						+ " '" + crmdays_so_inactive + "',"
						+ " " + crmdays_active + ","
						+ " '" + crmdays_contactable_email_sub + "',"
						+ " '" + crmdays_contactable_email_format + "',"
						+ " '', " // crmdays_contactable_email_exe_format
						+ " '" + crmdays_contactable_sms_format + "',"
						+ " '" + crmdays_noncontactable_email_sub + "',"
						+ " '', " // crmdays_noncontactable_email_exe_format
						+ " '" + crmdays_noncontactable_email_format + "',"
						+ " '" + crmdays_noncontactable_sms_format + "',"
						+ " '" + crmdays_satisfied_email_sub + "',"
						+ " '" + crmdays_satisfied_email_format + "',"
						+ " '', " // crmdays_satisfied_email_exe_format
						+ " '" + crmdays_satisfied_sms_format + "',"
						+ " '" + crmdays_dissatisfied_email_sub + "',"
						+ " '" + crmdays_dissatisfied_email_format + "',"
						+ " '', " // crmdays_dissatisfied_email_exe_format
						+ " '" + crmdays_dissatisfied_sms_format + "',"
						+ " " + crmdays_entry_id + ","
						+ " '" + crmdays_entry_date + "')";
				// SOP("add---------" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT crmdays_brand_id, crmdays_crmtype_id, crmdays_exe_type, "
					+ " crmdays_daycount, crmdays_desc, crmdays_script, crmdays_active,"
					+ " crmdays_contactable_email_enable,"
					+ " crmdays_contactable_email_sub,"
					+ " crmdays_contactable_email_format,"
					+ " crmdays_contactable_sms_enable,"
					+ " crmdays_contactable_sms_format,"
					+ " crmdays_noncontactable_email_enable,"
					+ " crmdays_noncontactable_email_sub,"
					+ " crmdays_noncontactable_email_format,"
					+ " crmdays_noncontactable_sms_enable,"
					+ " crmdays_noncontactable_sms_format,"

					+ " crmdays_satisfied_email_enable,"
					+ " crmdays_satisfied_email_sub,"
					+ " crmdays_satisfied_email_format,"
					+ " crmdays_satisfied_sms_enable,"
					+ " crmdays_satisfied_sms_format,"
					+ " crmdays_dissatisfied_email_enable,"
					+ " crmdays_dissatisfied_email_sub,"
					+ " crmdays_dissatisfied_email_format,"
					+ " crmdays_dissatisfied_sms_enable, "
					+ " crmdays_dissatisfied_sms_format,"
					+ " crmdays_lostfollowup, crmdays_testdrivefollowup, crmdays_homevisitfollowup,"
					+ " crmdays_waitingperiod, crmdays_so_inactive, crmdays_entry_id, crmdays_entry_date,"
					+ " crmdays_modified_id, crmdays_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
					+ " WHERE crmdays_id = " + crmdays_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					crmdays_brand_id = crs.getString("crmdays_brand_id");
					crmdays_crmtype_id = crs.getString("crmdays_crmtype_id");
					crmdays_exe_type = crs.getString("crmdays_exe_type");
					crmdays_daycount = crs.getString("crmdays_daycount");
					crmdays_desc = crs.getString("crmdays_desc");
					crmdays_script = crs.getString("crmdays_script");
					crmdays_active = crs.getString("crmdays_active");
					crmdays_contactable_email_enable = crs.getString("crmdays_contactable_email_enable");
					crmdays_contactable_email_sub = crs.getString("crmdays_contactable_email_sub");
					crmdays_contactable_email_format = crs.getString("crmdays_contactable_email_format");
					crmdays_contactable_sms_enable = crs.getString("crmdays_contactable_sms_enable");
					crmdays_contactable_sms_format = crs.getString("crmdays_contactable_sms_format");
					crmdays_noncontactable_email_enable = crs.getString("crmdays_noncontactable_email_enable");
					crmdays_noncontactable_email_sub = crs.getString("crmdays_noncontactable_email_sub");
					crmdays_noncontactable_email_format = crs.getString("crmdays_noncontactable_email_format");
					crmdays_noncontactable_sms_enable = crs.getString("crmdays_noncontactable_sms_enable");
					crmdays_noncontactable_sms_format = crs.getString("crmdays_noncontactable_sms_format");

					crmdays_satisfied_email_enable = crs.getString("crmdays_satisfied_email_enable");
					crmdays_satisfied_email_sub = crs.getString("crmdays_satisfied_email_sub");
					crmdays_satisfied_email_format = crs.getString("crmdays_satisfied_email_format");
					crmdays_satisfied_sms_enable = crs.getString("crmdays_satisfied_sms_enable");
					crmdays_satisfied_sms_format = crs.getString("crmdays_satisfied_sms_format");
					crmdays_dissatisfied_email_enable = crs.getString("crmdays_dissatisfied_email_enable");
					crmdays_dissatisfied_email_sub = crs.getString("crmdays_dissatisfied_email_sub");
					crmdays_dissatisfied_email_format = crs.getString("crmdays_dissatisfied_email_format");
					crmdays_dissatisfied_sms_enable = crs.getString("crmdays_dissatisfied_sms_enable");
					crmdays_dissatisfied_sms_format = crs.getString("crmdays_dissatisfied_sms_format");
					crmdays_lostfollowup = crs.getString("crmdays_lostfollowup");
					crmdays_testdrivefollowup = crs.getString("crmdays_testdrivefollowup");
					crmdays_homevisitfollowup = crs.getString("crmdays_homevisitfollowup");
					crmdays_waitingperiod = crs.getString("crmdays_waitingperiod");
					crmdays_so_inactive = crs.getString("crmdays_so_inactive");
					crmdays_entry_id = crs.getString("crmdays_entry_id");
					if (!crmdays_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(crmdays_entry_id));
						entry_date = strToLongDate(crs.getString("crmdays_entry_date"));
					}
					crmdays_modified_id = crs.getString("crmdays_modified_id");
					if (!crmdays_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(crmdays_modified_id));
						modified_date = strToLongDate(crs.getString("crmdays_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid CRM Follow-up Day!"));
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crmdays"
						+ " SET"
						+ " crmdays_brand_id = '" + crmdays_brand_id + "',"
						+ " crmdays_crmtype_id = " + crmdays_crmtype_id + ","
						+ " crmdays_exe_type = '" + crmdays_exe_type + "',"
						+ " crmdays_daycount = " + crmdays_daycount + ","
						+ " crmdays_desc = '" + crmdays_desc + "',"
						+ " crmdays_script = '" + crmdays_script + "',"
						+ " crmdays_lostfollowup = '" + crmdays_lostfollowup + "',"
						+ " crmdays_testdrivefollowup = '" + crmdays_testdrivefollowup + "',"
						+ " crmdays_homevisitfollowup = '" + crmdays_homevisitfollowup + "',"
						+ " crmdays_waitingperiod = '" + crmdays_waitingperiod + "',"
						+ " crmdays_so_inactive = '" + crmdays_so_inactive + "',"
						+ " crmdays_active = '" + crmdays_active + "',"
						+ " crmdays_contactable_email_enable = '" + crmdays_contactable_email_enable + "',"
						+ " crmdays_contactable_sms_enable = '" + crmdays_contactable_sms_enable + "',"
						+ " crmdays_noncontactable_email_enable = '" + crmdays_noncontactable_email_enable + "',"
						+ " crmdays_noncontactable_sms_enable = '" + crmdays_noncontactable_sms_enable + "',"
						+ " crmdays_satisfied_email_enable = '" + crmdays_satisfied_email_enable + "',"
						+ " crmdays_satisfied_sms_enable = '" + crmdays_satisfied_sms_enable + "',"
						+ " crmdays_dissatisfied_email_enable = '" + crmdays_dissatisfied_email_enable + "',"
						+ " crmdays_dissatisfied_sms_enable = '" + crmdays_dissatisfied_sms_enable + "',"
						+ " crmdays_modified_id = " + crmdays_modified_id + ","
						+ " crmdays_modified_date = '" + crmdays_modified_date + "'"
						+ " WHERE crmdays_id = " + crmdays_id + "";
				// SOP("StrSql-------update-------" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {

				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		StrSql = "SELECT crm_crmdays_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_crm"
				+ " WHERE crm_crmdays_id = " + crmdays_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>CRM days is associated with CRM Follow-up!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " WHERE crmdays_id =" + crmdays_id;
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateCRMType(String crmdays_crmtype_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT crmtype_id, crmtype_name"
					+ " FROM axela_sales_crm_type ORDER BY crmtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("crmtype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("crmtype_id"), crmdays_crmtype_id));
				Str.append(">").append(crs.getString("crmtype_name")).append("</option>\n");
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

	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id IN (1,2)"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), crmdays_brand_id));
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
		Str.append("<option value=1").append(StrSelectdrop("1", crmdays_exe_type)).append(">CRM Executives</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", crmdays_exe_type)).append(">Sales Consultant</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", crmdays_exe_type)).append(">Manager</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", crmdays_exe_type)).append(">Service CRM Executives</option>\n");
		return Str.toString();
	}
}
