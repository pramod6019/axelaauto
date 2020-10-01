package axela.insurance;
//Shilpashree 05 nov 2015

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insur_Reminder_Update extends Connect {

	public String add = "";
	public String comp_id = "";
	public String emp_id = "";
	public String branch_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String insurreminder_id = "0";
	public String insurreminder_days = "";
	public String insurreminder_email_sub = "";
	public String insurreminder_email_format = "";
	public String insurreminder_sms_format = "";
	public String insurreminder_email_enable = "", insurreminder_sms_enable = "";
	public String QueryString = "";
	public String insurreminder_entry_id = "0";
	public String insurreminder_entry_time = "";
	public String insurreminder_modified_id = "0";
	public String insurreminder_modified_time = "";
	public String insurreminder_entry_by = "";
	public String insurreminder_modified_by = "";
	public String entry_date = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = session.getAttribute("emp_id") + "";
				branch_id = session.getAttribute("emp_branch_id") + "";
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				insurreminder_id = CNumeric(PadQuotes(request.getParameter("insurreminder_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						insurreminder_days = "";
						insurreminder_email_enable = "";
						insurreminder_email_sub = "";
						insurreminder_email_format = "";
						insurreminder_sms_enable = "";
						insurreminder_sms_format = "";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							insurreminder_entry_id = emp_id;
							insurreminder_entry_time = ToLongDate(kknow());
							insurreminder_modified_time = "";
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../insurance/insur-reminder-list.jsp?insurreminder_id=" + insurreminder_id
										+ "&msg=Insurance Reminder added Successfully!"));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Insurance Reminder".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Insurance Reminder".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							insurreminder_modified_id = emp_id;
							insurreminder_modified_time = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../insurance/insur-reminder-list.jsp?insurreminder_id=" + insurreminder_id
										+ "&msg=Insurance Reminder updated Successfully!"
										+ msg + ""));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					} else if ("Delete Insurance Reminder".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insur-reminder-list.jsp?msg=Insurance Reminder deleted Successfully!"));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					}

				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		insurreminder_days = PadQuotes(request.getParameter("txt_insurreminder_days"));
		insurreminder_email_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_insurreminder_email_enable")));
		insurreminder_email_sub = PadQuotes(request.getParameter("txt_insurreminder_email_sub"));
		insurreminder_email_format = PadQuotes(request.getParameter("txt_insurreminder_email_format"));
		insurreminder_sms_enable = CheckBoxValue(PadQuotes(request.getParameter("chk_insurreminder_sms_enable")));
		insurreminder_sms_format = PadQuotes(request.getParameter("txt_insurreminder_sms_format"));
		insurreminder_entry_by = PadQuotes(request.getParameter("insurreminder_entry_by"));
		entry_date = PadQuotes(request.getParameter("insurreminder_entry_date"));
		insurreminder_modified_by = PadQuotes(request.getParameter("insurreminder_modified_by"));
		modified_date = PadQuotes(request.getParameter("insurreminder_modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (insurreminder_days.equals("")) {
			msg = msg + "<br>Enter Days!";
		}
		try {
			if (!insurreminder_days.equals("")) {
				if (update.equals("yes") && !insurreminder_days.equals("")) {
					StrSql = "SELECT insurreminder_days FROM " + compdb(comp_id) + "axela_insurance_reminder "
							+ " WHERE insurreminder_days = '" + insurreminder_days + "'"
							+ " AND insurreminder_id != " + insurreminder_id + "";
				}
				if (add.equals("yes") && !insurreminder_days.equals("")) {
					StrSql = "SELECT insurreminder_days FROM " + compdb(comp_id) + "axela_insurance_reminder "
							+ " WHERE insurreminder_days = '" + insurreminder_days + "'";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Insurance Reminder Found!";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		if (insurreminder_email_sub.length() > 1000) {
			insurreminder_email_sub = insurreminder_email_sub.substring(0, 1000);
		}
		if (insurreminder_email_format.length() > 8000) {
			insurreminder_email_format = insurreminder_email_format.substring(0, 8000);
		}
		if (insurreminder_sms_format.length() > 1000) {
			insurreminder_sms_format = insurreminder_sms_format.substring(0, 1000);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				insurreminder_id = ExecuteQuery("SELECT MAX(insurreminder_id) AS ID FROM " + compdb(comp_id) + "axela_insurance_reminder");
				if (insurreminder_id == null || insurreminder_id.equals("")) {
					insurreminder_id = "0";
				}
				int insurreminder_idi = Integer.parseInt(insurreminder_id) + 1;
				insurreminder_id = "" + insurreminder_idi;

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_reminder"
						+ " (insurreminder_id,"
						+ " insurreminder_days,"
						+ " insurreminder_email_enable,"
						+ " insurreminder_email_sub,"
						+ " insurreminder_email_format,"
						+ " insurreminder_sms_enable,"
						+ " insurreminder_sms_format,"
						+ " insurreminder_entry_id,"
						+ " insurreminder_entry_time)"
						+ " VALUES"
						+ " ('" + insurreminder_id + "',"
						+ " '" + insurreminder_days + "',"
						+ " '" + insurreminder_email_enable + "',"
						+ " '" + insurreminder_email_sub + "',"
						+ " '" + insurreminder_email_format + "',"
						+ " '" + insurreminder_sms_enable + "',"
						+ " '" + insurreminder_sms_format + "',"
						+ " " + insurreminder_entry_id + ","
						+ " '" + insurreminder_entry_time + "')";
				// SOP("StrSql--------" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_insurance_reminder "
					+ " WHERE insurreminder_id = " + insurreminder_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insurreminder_id = crs.getString("insurreminder_id");
					insurreminder_days = crs.getString("insurreminder_days");
					insurreminder_email_enable = crs.getString("insurreminder_email_enable");
					insurreminder_email_sub = crs.getString("insurreminder_email_sub");
					insurreminder_email_format = crs.getString("insurreminder_email_format");
					insurreminder_sms_enable = crs.getString("insurreminder_sms_enable");
					insurreminder_sms_format = crs.getString("insurreminder_sms_format");
					insurreminder_entry_id = crs.getString("insurreminder_entry_id");
					if (!insurreminder_entry_id.equals("0")) {
						insurreminder_entry_by = Exename(comp_id, Integer.parseInt(insurreminder_entry_id));
						entry_date = strToLongDate(crs.getString("insurreminder_entry_time"));
					}
					insurreminder_modified_id = crs.getString("insurreminder_modified_id");
					if (!insurreminder_modified_id.equals("0")) {
						insurreminder_modified_by = Exename(comp_id, Integer.parseInt(insurreminder_modified_id));
						modified_date = strToLongDate(crs.getString("insurreminder_modified_time"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Insurance Reminder!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_reminder"
						+ " SET"
						+ " insurreminder_days = '" + insurreminder_days + "',"
						+ " insurreminder_email_enable = '" + insurreminder_email_enable + "',"
						+ " insurreminder_email_sub = '" + insurreminder_email_sub + "',"
						+ " insurreminder_email_format = '" + insurreminder_email_format + "',"
						+ " insurreminder_sms_enable = '" + insurreminder_sms_enable + "',"
						+ " insurreminder_sms_format = '" + insurreminder_sms_format + "',"
						+ " insurreminder_modified_id = " + insurreminder_modified_id + ","
						+ " insurreminder_modified_time = '" + insurreminder_modified_time + "'"
						+ " WHERE insurreminder_id = " + insurreminder_id + "";
				updateQuery(StrSql);
				// SOP("StrSql---***********----" + StrSql);
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_reminder"
						+ " WHERE insurreminder_id = " + insurreminder_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
