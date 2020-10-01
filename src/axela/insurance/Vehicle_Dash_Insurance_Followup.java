package axela.insurance;

////////////Divya 18th April 2013
////////////Dilip 05 May 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle_Dash_Insurance_Followup extends Connect {

	public String msg = "";
	public String veh_id = "0";
	public String veh_reg_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String submitB = "";
	public String delete = "";
	public String insurfollowup_id = "0";
	public String customerdetail = "";
	// public String insurfollowup_followup_time = "";
	public String insurfollowup_followup_time = "";
	public String insurfollowup_entry_id = "0";
	public String insurfollowup_entry_time = "";
	public String insurfollowup_desc = "";
	public String enquiry_status_id = "0";
	public String insurfollowup_emp_id = "0";
	public String insurfollowup_followuptype_id = "0";
	public String followup_time = "";
	public String valid_followup_time = "";
	public String followuptime = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				submitB = PadQuotes(request.getParameter("submit_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				insurfollowup_id = CNumeric(PadQuotes(request.getParameter("insurfollowup_id")));
				customerdetail = ListCustomerDetails();
				StrSql = "SELECT veh_reg_no FROM " + compdb(comp_id) + "axela_service_veh"
						+ " WHERE veh_id = " + veh_id + "";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						veh_reg_no = crs.getString("veh_reg_no");
					}
					StrHTML = ListInsuranceFollowup(comp_id, veh_id);
					if ("yes".equals(delete) && !insurfollowup_id.equals("0") && emp_id.equals("1")) {
						DeleteFields();
						response.sendRedirect(response.encodeRedirectURL("vehicle-dash-insurance-followup.jsp?veh_id=" + veh_id + "&msg=Follow-up deleted successfully!"));
					}
					if ("Submit".equals(submitB)) {
						GetValues(request);
						insurfollowup_emp_id = emp_id;
						insurfollowup_entry_id = emp_id;
						insurfollowup_entry_time = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("vehicle-dash-insurance-followup.jsp?veh_id=" + veh_id + "&msg=Follow-up added successfully!"));
						}
					}
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Vehicle ID!");
				}
				crs.close();
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

	public String ListCustomerDetails() {
		SOP("comp_id------" + comp_id);
		StringBuilder Str = new StringBuilder();
		StrSql = " SELECT customer_id, customer_name, contact_id, contact_mobile1,"
				+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts"
				+ " FROM " + compdb(comp_id) + "axela_insurance_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh on veh_id = insurfollowup_veh_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = veh_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_customer_id = customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
				+ " WHERE insurfollowup_veh_id = " + veh_id + ""
				+ " GROUP BY customer_id";
		// SOP("StrSql==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
			while (crs.next()) {
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>Customer: <a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(" \">");
				Str.append(crs.getString("customer_name"));
				Str.append(" (").append(crs.getString("customer_id")).append(")");
				Str.append("</td>\n<td align=center>Contact: <a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
				Str.append(crs.getString("contacts")).append("</a>");
				Str.append("</td>\n<td align=center>Mobile: ").append(crs.getString("contact_mobile1")).append("</td>\n");
				Str.append("</tr>\n");
			}
			Str.append("</table>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListInsuranceFollowup(String comp_id, String veh_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT insurfollowup_id, insurfollowup_veh_id, insurfollowup_followup_time,"
				+ " insurfollowup_desc, insurfollowup_entry_id, insurfollowup_entry_time, "
				+ " followuptype_name, emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
				+ " FROM " + compdb(comp_id) + "axela_insurance_followup"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = insurfollowup_veh_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = insurfollowup_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_followup_type ON followuptype_id = insurfollowup_followuptype_id"
				+ " WHERE insurfollowup_veh_id = " + veh_id + BranchAccess + ExeAccess + ""
				+ " ORDER BY insurfollowup_id";
		// SOP("StrSql--"+StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Time</th>\n");
				Str.append("<th>Follow-up Type</th>\n");
				Str.append("<th>Follow-up Description</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th>Entry by</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count);
					Str.append("</td>\n<td valign=top align=center >").append(strToLongDate(crs.getString("insurfollowup_followup_time")));
					if (emp_id.equals("1")) {
						Str.append("<br><a href=\"vehicle-dash-insurance-followup.jsp?Delete=yes&veh_id=").append(crs.getString("insurfollowup_veh_id")).append("&insurfollowup_id=")
								.append(crs.getString("insurfollowup_id")).append(" \">Delete Follow-up</a>");
					}
					Str.append("</td>\n<td valign=top align=left >").append(crs.getString("followuptype_name"));
					Str.append("</td>\n<td valign=top align=left >").append(crs.getString("insurfollowup_desc"));
					Str.append("</td>\n<td valign=top><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a>");
					Str.append("</td>\n<td valign=top align=left >");
					if (!crs.getString("insurfollowup_entry_id").equals("0")) {
						Str.append(Exename(comp_id, Integer.parseInt(crs.getString("insurfollowup_entry_id"))));
						Str.append("<br>").append(strToLongDate(crs.getString("insurfollowup_entry_time")));
					}
					Str.append("&nbsp;</td>\n</tr>");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Follow-up found!</b></font><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void GetValues(HttpServletRequest request) {
		insurfollowup_desc = PadQuotes(request.getParameter("txt_insurfollowup_desc"));
		insurfollowup_followuptype_id = CNumeric(PadQuotes(request.getParameter("dr_insurfollowuptype")));
		followup_time = PadQuotes(request.getParameter("txt_insurfollowup_time"));
	}

	protected void CheckForm() {
		msg = "";
		if (insurfollowup_emp_id.equals("0")) {
			msg = msg + "<br>No Executive allocated!";
		}
		if (insurfollowup_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (followup_time.equals("")) {
			msg = msg + "<br>Select Follow-up Time!";
		}
		if (!followup_time.equals("") && !isValidDateFormatLong(followup_time)) {
			msg = msg + "<br>Enter Valid Follow-up Time!";
		}
		if (!followup_time.equals("") && isValidDateFormatLong(followup_time)) {
			insurfollowup_followup_time = ConvertLongDateToStr(followup_time);
		}
		if (insurfollowup_followuptype_id.equals("0")) {
			msg = msg + "<br>Select Follow-up Type!";
		}
		followuptime = ExecuteQuery("select insurfollowup_followup_time from " + compdb(comp_id) + "axela_insurance_followup where insurfollowup_desc=''");
		if (!followuptime.equals("") && isValidDateFormatLong(strToLongDate(followuptime))) {
			if (Long.parseLong(insurfollowup_followup_time) < Long.parseLong(followuptime)) {
				msg = msg + "<br>Next Follow-up time should be greater from previous follow-up time!";
			}
		}
		// Insurance Followup reminder should not exceed 10 months
		followuptime = DateToShortDate(kknow());
		valid_followup_time = AddDayMonthYear(followuptime, 0, 0, 10, 0);
		if (!valid_followup_time.equals("") && isValidDateFormatShort(valid_followup_time)) {
			if (Long.parseLong(insurfollowup_followup_time) > Long.parseLong(ConvertShortDateToStr(valid_followup_time))) {
				msg = msg + "<br>Next Follow-up Time should not exceed 10 months from present time!";
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_followup"
						+ " WHERE insurfollowup_id = " + insurfollowup_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_followup"
						+ " SET"
						+ " insurfollowup_desc = '" + insurfollowup_desc + "'"
						+ " WHERE insurfollowup_desc = '' ";
				updateQuery(StrSql);
				StrSql = " INSERT INTO " + compdb(comp_id) + "axela_insurance_followup"
						+ " (insurfollowup_veh_id, "
						+ " insurfollowup_emp_id, "
						+ " insurfollowup_followuptype_id, "
						+ " insurfollowup_followup_time, "
						+ " insurfollowup_desc, "
						+ " insurfollowup_entry_id, "
						+ " insurfollowup_entry_time, "
						+ " insurfollowup_trigger) "
						+ " VALUES"
						+ " ('" + veh_id + "',"
						+ " " + insurfollowup_emp_id + ","
						+ " " + insurfollowup_followuptype_id + ","
						+ " '" + insurfollowup_followup_time + "',"
						+ " '',"
						+ "" + insurfollowup_entry_id + ","
						+ " '" + insurfollowup_entry_time + "',"
						+ " 0)";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateFollowuptype() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT followuptype_id, followuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_followup_type"
					+ " ORDER BY followuptype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {

				Str.append("<option value=").append(crs.getString("followuptype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("followuptype_id"), insurfollowup_followuptype_id));
				Str.append(">").append(crs.getString("followuptype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
