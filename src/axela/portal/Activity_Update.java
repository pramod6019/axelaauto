// Ved (11 Feb, 18 March 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Activity_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String emp_role = "";
	public String contact_id = "0";
	public String activity_id = "0";
	public String activity_type_id = "0";
	public String activity_priority_id = "0";
	public String activity_contact_id = "0";
	public String[] activity_emp_idl;
	public String activity_emp_id = "0";
	public String activity_start_datetime = "";
	public String start_datetime = "";
	public String activity_end_datetime = "";
	public String activity_reminder = "";
	public String activity_reminder_set = "";
	public String activity_contact_person = "";
	public String activity_title = "";
	public String activity_desc = "";
	public String activity_Phone1 = "";
	public String activity_Phone2 = "";
	public String activity_venue = "";
	public String activity_status_id = "";
	public String activity_feedback = "";
	public String activity_remarks = "";
	public String activity_entry_id = "0";
	public String activity_entry_datetime = "";
	public String activity_modified_id = "0";
	public String activity_modified_datetime = "";
	public String activity_feedback_entry_id = "0";
	public String activity_feedback_entry_datetime = "";
	public String activity_remarks_entry_id = "0";
	public String activity_remarks_entry_datetime = "";
	public String end_datetime = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String client = "";
	public String date = "";
	public String StartHour = "";
	public String data = "";
	public String ExeAccess = "";
	public String empPop = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_activity_access", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				activity_id = PadQuotes(request.getParameter("activity_id"));
				activity_type_id = CNumeric(PadQuotes(request.getParameter("drop_activity_type_id")));
				StartHour = PadQuotes(request.getParameter("h"));
				contact_id = PadQuotes(request.getParameter("contact_id"));
				data = PadQuotes(request.getParameter("data"));
				empPop = PadQuotes(request.getParameter("employee_pop"));

				if (add.equals("yes")) {
					status = "Add";
					String str = ToLongDate(kknow());
					if (!PadQuotes(request.getParameter("d")).equals("")) {
						date = request.getParameter("d");
					} else {
						date = strToShortDate(str);
					}
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						if (activity_id.equals("")) {
							if (!contact_id.equals("")) {
								getClientInfo(response);
							} else {
								activity_contact_person = "";
							}
							activity_contact_id = contact_id;
							activity_reminder = "";
							activity_reminder_set = "";
							activity_title = "";
							activity_desc = "";
							activity_priority_id = "2";
							activity_start_datetime = strToLongDate(ToLongDate(kknow()));
							start_datetime = activity_start_datetime;
							activity_end_datetime = strToLongDate(ToLongDate(AddHoursDate(StringToDate(ToLongDate(kknow())), 0, 0, 30)));
							end_datetime = activity_end_datetime;
							activity_Phone1 = "";
							activity_Phone2 = "";
							activity_venue = "";
							activity_status_id = "";
							activity_feedback = "";
							activity_remarks = "";
							activity_emp_id = emp_id;
						} else if (!activity_id.equals("")) {
							getClientInfo(response);
							PopulateFields(response);
							activity_status_id = "0";
							activity_feedback = "";
							activity_remarks = "";
						}
					} else if ("yes".equals(addB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_activity_add", request).equals("1")) {
							getClientInfo(response);
							activity_entry_id = emp_id;
							activity_entry_datetime = ToLongDate(kknow());
							activity_modified_id = "0";
							activity_modified_datetime = "";
							activity_feedback_entry_id = "0";
							activity_feedback_entry_datetime = "";
							activity_remarks_entry_id = "0";
							activity_remarks_entry_datetime = "";
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								if (data.equals("yes")) {
									response.sendRedirect(response.encodeRedirectURL("../portal/employee-pop-activity.jsp?contact_id=" + activity_contact_id + ""));
								} else if (!data.equals("yes")) {
									response.sendRedirect(response.encodeRedirectURL("../portal/activity-list.jsp?activity_id=" + activity_id + "&msg=Activity Details Added Successfully!"));
								}
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Activity".equals(deleteB)) {
						PopulateFields(response);
						if (!contact_id.equals("") && !contact_id.equals("0")) {
							getClientInfo(response);
						}
					} else if ("yes".equals(updateB) && !"Delete Activity".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_activity_edit", request).equals("1")) {
							activity_modified_id = emp_id;
							activity_modified_datetime = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								if (!PadQuotes(request.getParameter("d")).equals("")) {
									date = request.getParameter("d");
									if (!date.equals("")) {
										response.sendRedirect(response.encodeRedirectURL("../portal/home.jsp?d=" + date + "&msg=Activity details updated successfully!"));
									}
								} else {
									response.sendRedirect(response.encodeRedirectURL("../portal/activity-list.jsp?activity_id=" + activity_id + "&msg=Activity Details Updated Successfully!"));
								}
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Activity".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_activity_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("activity-list.jsp?msg=Activity details deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			activity_emp_idl = request.getParameterValues("list_executives");
			activity_emp_id = PadQuotes(request.getParameter("list_executives"));
			activity_type_id = PadQuotes(request.getParameter("drop_activity_type_id"));
			activity_title = PadQuotes(request.getParameter("txt_activity_title"));
			activity_desc = PadQuotes(request.getParameter("txt_activity_desc"));
			activity_priority_id = PadQuotes(request.getParameter("drop_activity_priority_id"));
			if (empPop.equals("")) {
				activity_contact_person = PadQuotes(request.getParameter("txt_activity_contact_person"));
			}
			activity_Phone1 = PadQuotes(request.getParameter("txt_activity_Phone1"));
			activity_Phone2 = PadQuotes(request.getParameter("txt_activity_Phone2"));
			activity_venue = PadQuotes(request.getParameter("txt_activity_venue"));
			activity_status_id = CNumeric(PadQuotes(request.getParameter("drop_activity_status_id")));
			activity_id = PadQuotes(request.getParameter("activity_id"));
			client = PadQuotes(request.getParameter("contact"));
			activity_contact_id = PadQuotes(request.getParameter("activity_contact_id"));
			contact_id = PadQuotes(request.getParameter("contact_id"));
			activity_contact_id = contact_id;
			start_datetime = PadQuotes(request.getParameter("txt_start_time"));
			activity_start_datetime = ConvertLongDateToStr(start_datetime);
			end_datetime = PadQuotes(request.getParameter("txt_end_time"));
			activity_end_datetime = ConvertLongDateToStr(end_datetime);
			entry_by = PadQuotes(request.getParameter("entry_by"));
			entry_date = PadQuotes(request.getParameter("entry_date"));
			modified_by = PadQuotes(request.getParameter("modified_by"));
			modified_date = PadQuotes(request.getParameter("modified_date"));
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		if (activity_emp_id.equals("")) {
			msg = "<br>Select Executive!";
		}
		if (activity_type_id.equals("0")) {
			msg += "<br>Select Type!";
		}
		if (activity_title.equals("")) {
			msg += "<br>Enter Title!";
		}
		if (start_datetime.equals("")) {
			msg += "<br>Select Start Time!";
		}
		if (!start_datetime.equals("")) {
			if (!isValidDateFormatLong(start_datetime)) {
				msg += "<br>Enter Valid Start Time!";
			}
		}
		if (end_datetime.equals("")) {
			msg += "<br>Select End Time!";
		}
		if (!end_datetime.equals("")) {
			if (!isValidDateFormatLong(end_datetime)) {
				msg += "<br>Enter Valid End Time!";
			}
		}
		if (!start_datetime.equals("") && isValidDateFormatLong(start_datetime) && !end_datetime.equals("") && isValidDateFormatLong(end_datetime)) {
			if (Long.parseLong(ConvertLongDateToStr(end_datetime)) < Long.parseLong(ConvertLongDateToStr(start_datetime))) {
				msg += "<br>End Time must be greater than or equal to Start Time!";
			}
		}
		if (activity_priority_id.equals("0") && !activity_priority_id.equals("")) {
			msg += "<br>Select Priority!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				// SOP("activity_emp_idl==" + activity_emp_idl.length);
				for (int i = 0; i < activity_emp_idl.length; i++) {
					activity_id = ExecuteQuery("SELECT COALESCE(MAX(activity_id), 0) + 1 AS activity_id FROM " + compdb(comp_id) + "axela_activity");

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_activity"
							+ " (activity_id,"
							+ " activity_emp_id,"
							+ " activity_type_id,"
							+ " activity_title,"
							+ " activity_desc,"
							+ " activity_start_datetime,"
							+ " activity_end_datetime,"
							+ " activity_priority_id,"
							+ " activity_contact_id,"
							+ " activity_contact_person,"
							+ " activity_Phone1,"
							+ " activity_Phone2,"
							+ " activity_venue,"
							+ " activity_status_id,"
							+ " activity_feedback,"
							+ " activity_remarks,"
							+ " activity_feedback_entry_id,"
							+ " activity_feedback_entry_datetime,"
							+ " activity_remarks_entry_id,"
							+ " activity_remarks_entry_datetime,"
							+ " activity_trigger,"
							+ " activity_entry_id,"
							+ " activity_entry_datetime)"
							+ " VALUES"
							+ " (" + CNumeric(activity_id) + ","
							+ " " + activity_emp_idl[i] + ","
							+ " " + activity_type_id + ","
							+ " '" + activity_title + "',"
							+ " '" + activity_desc + "',"
							+ " '" + activity_start_datetime + "',"
							+ " '" + activity_end_datetime + "',"
							+ " " + activity_priority_id + ","
							+ " " + CNumeric(activity_contact_id) + ","
							+ " '" + activity_contact_person + "',"
							+ " '" + activity_Phone1 + "',"
							+ " '" + activity_Phone2 + "',"
							+ " '" + activity_venue + "',"
							+ " " + activity_status_id + ","
							+ " '" + activity_feedback + "',"
							+ " '" + activity_remarks + "',"
							+ " " + activity_feedback_entry_id + ","
							+ " '" + activity_feedback_entry_datetime + "',"
							+ " " + activity_remarks_entry_id + ","
							+ " '" + activity_remarks_entry_datetime + "',"
							+ " 0,"
							+ " " + activity_entry_id + ","
							+ " '" + activity_entry_datetime + "')";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT activity_feedback, activity_remarks, activity_type_id,"
					+ " activity_emp_id, activity_contact_id, activity_priority_id,"
					+ " activity_start_datetime, activity_end_datetime, activity_contact_person,"
					+ " activity_title, activity_desc, activity_Phone1, activity_Phone2,"
					+ " activity_venue, activity_entry_id, activity_entry_datetime,"
					+ " activity_modified_id,activity_modified_datetime, contact_fname"
					+ " FROM " + compdb(comp_id) + "axela_activity"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = activity_contact_id"
					+ " WHERE activity_id = " + CNumeric(activity_id) + "";
			// SOP("---" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					activity_feedback = crs.getString("activity_feedback");
					activity_remarks = crs.getString("activity_remarks");
					activity_type_id = crs.getString("activity_type_id");
					activity_emp_id = crs.getString("activity_emp_id");
					activity_contact_id = crs.getString("activity_contact_id");
					contact_id = activity_contact_id;
					client = "<b><a href=../customer/customer-contact-list.jsp?contact_id=" + activity_contact_id + " target=_blank> " + crs.getString("contact_fname") + "(" + activity_contact_id
							+ ")</a></b>";
					activity_priority_id = crs.getString("activity_priority_id");
					activity_start_datetime = crs.getString("activity_start_datetime");
					activity_end_datetime = crs.getString("activity_end_datetime");
					start_datetime = strToLongDate(activity_start_datetime);
					activity_end_datetime = crs.getString("activity_end_datetime");
					end_datetime = strToLongDate(activity_end_datetime);
					activity_contact_person = crs.getString("activity_contact_person");
					activity_title = crs.getString("activity_title");
					activity_desc = crs.getString("activity_desc");
					activity_Phone1 = crs.getString("activity_Phone1");
					activity_Phone2 = crs.getString("activity_Phone2");
					activity_venue = crs.getString("activity_venue");
					activity_entry_id = crs.getString("activity_entry_id");
					if (!activity_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(activity_entry_id));
					}
					entry_date = strToLongDate(crs.getString("activity_entry_datetime"));
					activity_modified_id = crs.getString("activity_modified_id");
					if (!activity_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(activity_modified_id));
						modified_date = strToLongDate(crs.getString("activity_modified_datetime"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Activity!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_activity"
						+ " SET"
						+ " activity_emp_id = " + activity_emp_id + ","
						+ " activity_type_id = " + activity_type_id + ","
						+ " activity_contact_id = " + CNumeric(activity_contact_id) + ","
						+ " activity_priority_id = " + activity_priority_id + ","
						+ " activity_start_datetime = '" + activity_start_datetime + "',"
						+ " activity_end_datetime = '" + activity_end_datetime + "',"
						+ " activity_contact_person = '" + activity_contact_person + "',"
						+ " activity_title = '" + activity_title + "',"
						+ " activity_desc = '" + activity_desc + "',"
						+ " activity_Phone1 = '" + activity_Phone1 + "',"
						+ " activity_Phone2 = '" + activity_Phone2 + "',"
						+ " activity_venue = '" + activity_venue + "',"
						+ " activity_modified_id = " + activity_modified_id + ","
						+ " activity_modified_datetime = '" + activity_modified_datetime + "'"
						+ " where activity_id = " + CNumeric(activity_id) + "";
				// SOP("Upd SQL====" + StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_activity"
						+ " WHERE activity_id = " + CNumeric(activity_id) + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateActivitytype() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=0>Select</option>\n");
			StrSql = "SELECT type_id, type_name"
					+ " FROM " + compdb(comp_id) + "axela_activity_type"
					+ " ORDER BY type_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("type_id"));
				Str.append(StrSelectdrop(crs.getString("type_id"), activity_type_id));
				Str.append(">").append(crs.getString("type_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateExe() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'" + ExeAccess
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				if ("Add Activity".equals(addB) && activity_emp_idl != null) {
					Str.append(ArrSelectdrop(crs.getInt("emp_id"), activity_emp_idl));
				} else {
					Str.append(Selectdrop(crs.getInt("emp_id"), activity_emp_id));
				}
				Str.append(">").append(Exename(comp_id, crs.getInt("emp_id"))).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>\n");
		try {
			StrSql = "SELECT priority_id, priority_name"
					+ " FROM " + compdb(comp_id) + "axela_activity_priority"
					+ " ORDER BY priority_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priority_id"));
				Str.append(StrSelectdrop(crs.getString("priority_id"), activity_priority_id));
				Str.append(">").append(crs.getString("priority_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void getClientInfo(HttpServletResponse response) {
		try {
			if (!activity_id.equals("")) {
				activity_contact_person = ExecuteQuery("SELECT activity_contact_person from " + compdb(comp_id) + "axela_activity where activity_id=" + CNumeric(activity_id));
			}
			if (!contact_id.equals("")) {
				activity_contact_id = contact_id;
				StrSql = "SELECT concat(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " WHERE contact_id = " + CNumeric(contact_id) + "";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						String str = "";
						if (!crs.getString("contact_name").equals("")) {
							str += crs.getString("contact_name") + "";
						}
						client = "<b><a href=../customer/customer-contact-list.jsp?contact_id=" + contact_id + "> " + str + " (" + contact_id + ")</a></b>";
						activity_contact_person = crs.getString("contact_name");
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Contact!"));
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
