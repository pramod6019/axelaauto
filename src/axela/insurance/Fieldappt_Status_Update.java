// smitha nag 13 feb 2013
// saiman 2nd may 2013
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Fieldappt_Status_Update extends Connect {

	public String feedbackB = "";
	public String msg = "";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String branch_id = "";
	public String BranchAccess = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String fieldappt_id = "0";
	public String fieldappt_fieldappttype_id = "0";
	public String fieldappt_insurenquiry_id = "0";
	public String fieldappt_appttime = "";
	public String fieldappt_fromtime = "";
	public String fieldappt_status_taken = "";
	public String fieldappt_status_notes = "";
	public String fieldappt_status_entry_id = "0";
	public String fieldappt_status_entry_date = "";
	public String fieldappt_status_modified_id = "0";
	public String fieldappt_status_modified_date = "";
	public String insurenquiry_customer_id = "";
	public String insurenquiry_branch_id = "";
	public String insurenquiry_date = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_id = "";
	public String contact_mobile1 = "";
	public String executive_name = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	// public String config_sms_enable = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				QueryString = PadQuotes(request.getQueryString());
				BranchAccess = GetSession("BranchAccess", request);
				feedbackB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				fieldappt_id = CNumeric(PadQuotes(request.getParameter("fieldappt_id")));

				EnquiryDetails(response);
				// getConfigFields();

				if (feedbackB.equals("")) {
					PopulateFields(response);
				} else if ("Update Status".equals(feedbackB)) {
					GetValues(request, response);
					if (entry_by.equals("")) {
						if (ReturnPerm(comp_id, "emp_testdrive_add", request).equals("1")) {
							fieldappt_status_entry_id = CNumeric(GetSession("emp_id", request));
							fieldappt_status_entry_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("fieldappt-list.jsp?fieldappt_id=" + fieldappt_id + "&msg=Status Updated Successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else {
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							fieldappt_status_modified_id = CNumeric(GetSession("emp_id", request));
							fieldappt_status_modified_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("fieldappt-list.jsp?fieldappt_id=" + fieldappt_id + "&msg=Status Updated Successfully"));
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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		fieldappt_id = PadQuotes(request.getParameter("fieldappt_id"));
		fieldappt_insurenquiry_id = PadQuotes(request.getParameter("fieldappt_insurenquiry_id"));
		fieldappt_status_taken = PadQuotes(request.getParameter("dr_fieldappt_status_taken"));
		fieldappt_status_notes = PadQuotes(request.getParameter("txt_fieldappt_status_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {

		msg = "";
		if (Long.parseLong(fieldappt_appttime) > Long.parseLong(ToLongDate(kknow()))) {
			msg = msg + "<br>Field Appointment Status can be given after " + strToLongDate(fieldappt_appttime) + "!";
		}
		if (fieldappt_status_taken.equals("0")) {
			msg = msg + "<br>Select Field Appointment Taken!";
		}
		if (fieldappt_status_notes.equals("")) {
			msg = msg + "<br>Enter Field Appointment Status!";
		}
		if (fieldappt_status_notes.length() > 1000) {
			fieldappt_status_notes = fieldappt_status_notes.substring(0, 999);
		}

	}

	protected void UpdateFields(HttpServletRequest request) {

		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_fieldappt"
						+ " SET "
						+ "fieldappt_status_taken = '" + fieldappt_status_taken + "', "
						+ "fieldappt_status_notes = '" + fieldappt_status_notes + "', ";
				if (entry_by.equals("")) {
					StrSql = StrSql + "fieldappt_status_entry_id = '" + emp_id + "', "
							+ "fieldappt_status_entry_date = '" + ToLongDate(kknow()) + "', ";
				}
				StrSql += "fieldappt_status_modified_id = " + fieldappt_status_modified_id + ", "
						+ "fieldappt_status_modified_date = '" + fieldappt_status_modified_date + "' "
						+ "WHERE fieldappt_id = " + fieldappt_id + " ";
				// SOP("StrSql---" + StrSql);
				updateQuery(StrSql);
				// if (comp_id.equals("1009")) {
				// SendSMS();
				// }

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT fieldappt_status_taken,  fieldappt_status_notes, "
					+ " fieldappt_status_entry_id, fieldappt_status_entry_date, fieldappt_status_modified_id, fieldappt_status_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt "
					+ " WHERE fieldappt_id = " + fieldappt_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				fieldappt_status_taken = crs.getString("fieldappt_status_taken");
				fieldappt_status_notes = crs.getString("fieldappt_status_notes");
				fieldappt_status_entry_id = crs.getString("fieldappt_status_entry_id");
				if (!fieldappt_status_entry_id.equals("0")) {
					entry_by = Exename(comp_id, crs.getInt("fieldappt_status_entry_id"));
					entry_date = strToLongDate(crs.getString("fieldappt_status_entry_date"));
				}
				fieldappt_status_modified_id = crs.getString("fieldappt_status_modified_id");
				if (!fieldappt_status_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(fieldappt_status_modified_id));
					modified_date = strToLongDate(crs.getString("fieldappt_status_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void EnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT insurenquiry_id, insurenquiry_branch_id, insurenquiry_emp_id,"
					+ " CONCAT(emp_name, ' ', emp_mobile1) AS emp_name, "
					+ " insurenquiry_date, fieldappt_appttime, fieldappt_fieldappttype_id,"
					+ " fieldappt_fromtime, fieldappt_totime, fieldappt_insurenquiry_id, branch_code,"
					+ " customer_id, customer_name, contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name, contact_mobile1 "
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = fieldappt_insurenquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = fieldappt_emp_id "
					+ " WHERE fieldappt_id = " + fieldappt_id
					+ BranchAccess + "";
			// SOP("StrSql--EnquiryDetails--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insurenquiry_customer_id = crs.getString("customer_id");
					contact_id = crs.getString("contact_id");
					fieldappt_insurenquiry_id = crs.getString("fieldappt_insurenquiry_id");
					fieldappt_fieldappttype_id = crs.getString("fieldappt_fieldappttype_id");
					customer_name = crs.getString("customer_name");
					insurenquiry_branch_id = crs.getString("insurenquiry_branch_id");
					insurenquiry_date = crs.getString("insurenquiry_date");
					fieldappt_appttime = crs.getString("fieldappt_appttime");
					fieldappt_fromtime = strToLongDate(crs.getString("fieldappt_fromtime"));
					if (!crs.getString("fieldappt_fromtime").equals("")) {
						fieldappt_fromtime = PeriodTime(crs.getString("fieldappt_fromtime"), crs.getString("fieldappt_totime"), "1");
					}
					executive_name = crs.getString("emp_name");
					contact_name = crs.getString("contact_name");
					contact_mobile1 = crs.getString("contact_mobile1");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Field Appointment!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// protected void SendSMS() {
	// String msg = "";
	// if (config_sms_enable.equals("1") && !contact_mobile1.equals("") && fieldappt_status_taken.equals("1")) {
	// msg = "Greetings from DD Motors! Dear " + contact_name + " Hope you had a wonderful Maruti Drive Experience, For any further assistance please contact Sales Manager " + executive_name
	// + " .\nWarm Regards,\nDD Motocrs.";
	// try {
	// String Sql = "insert into " + compdb(comp_id) + "axela_sms"
	// + "(sms_branch_id,"
	// + "sms_mobileno,"
	// + "sms_msg,"
	// + "sms_date ,"
	// + "sms_sent ,"
	// + "sms_contact_id, "
	// // + "sms_customer_id ,"
	// + "sms_entry_id)"
	// + "values"
	// + "(" + branch_id + ","
	// + "'" + contact_mobile1 + "',"
	// + "'" + msg + "',"
	// + "'" + ToLongDate(kknow()) + "',"
	// + "'0',"
	// + "'" + contact_id + "',"
	// // + "'" + testdrive_customer_id + "',"
	// + "'" + emp_id + "')";
	// updateQuery(Sql);
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }
	// }

	// public void getConfigFields() {
	// StrSql = "Select config_sms_enable from " + compdb(comp_id) + "axela_config";
	// try {
	// CachedRowSet crs = processQuery(StrSql, 0);
	// while (crs.next()) {
	// config_sms_enable = crs.getString("config_sms_enable");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

	public String PopulateApptType(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT fieldappttype_id, fieldappttype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_fieldappt_type "
					+ " WHERE 1=1";

			StrSql += " GROUP BY fieldappttype_id "
					+ " ORDER BY fieldappttype_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select Appointment Type</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fieldappttype_id")).append("");
				Str.append(Selectdrop(crs.getInt("fieldappttype_id"), fieldappt_fieldappttype_id));
				Str.append(">").append(crs.getString("fieldappttype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateFieldApptTaken() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0").append(Selectdrop(1, fieldappt_status_taken)).append(">Select</option>\n");
		Str.append("<option value = 1").append(Selectdrop(1, fieldappt_status_taken)).append(">Completed</option>\n");
		Str.append("<option value = 2").append(Selectdrop(2, fieldappt_status_taken)).append(">Not Completed</option>\n");
		return Str.toString();
	}

}
