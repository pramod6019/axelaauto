package axela.service;
//Bhagwan Singh

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Movement_Update extends Connect {

	public String update = "";
	public String deleteB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String msg = "";
	public String emp_id = "0", emp_role_id = "0";
	public String vehmove_id = "0";
	public String vehmove_branch_id = "0", vehmove_jctype_id = "0";
	public String vehmove_reg_no = "0";
	public String vehmove_timein = "";
	public String vehmovetimein = "";
	public String vehmove_timeout = "";
	public String vehmovetimeout = "";
	public String QueryString = "";
	public String vehmove_timein_entry_id = "0";
	public String vehmove_timeout_entry_id = "0";
	public String vehmove_entry_date = "";
	public String vehmove_modified_id = "0";
	public String vehmove_modified_date = "";
	public String timein_entry_by = "";
	public String timeout_entry_by = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_movement_access", request, response);
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				vehmove_id = CNumeric(PadQuotes(request.getParameter("vehmove_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(update)) {
					if (!updateB.equals("yes") && !deleteB.equals("Delete Vehicle Movement")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Vehicle Movement")) {
						if (ReturnPerm(comp_id, "emp_movement_edit", request).equals("1")) {
							GetValues(request, response);
							vehmove_modified_id = emp_id;
							vehmove_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("movement-list.jsp?vehmove_id=" + vehmove_id + "&msg=Vehicle Movement Updated Successfully!"));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					} else if (deleteB.equals("Delete Vehicle Movement")) {
						if (ReturnPerm(comp_id, "emp_movement_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("movement-list.jsp?msg=Vehicle Movement Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
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
		vehmove_branch_id = CNumeric(PadQuotes(request.getParameter("dr_vehmove_branch_id")));
		vehmove_jctype_id = CNumeric(PadQuotes(request.getParameter("dr_vehmove_jctype_id")));
		vehmove_reg_no = PadQuotes(request.getParameter("txt_vehmove_reg_no"));
		vehmove_timein = PadQuotes(request.getParameter("txt_vehmove_timein"));
		vehmove_timeout = PadQuotes(request.getParameter("txt_vehmove_timeout"));
		timein_entry_by = PadQuotes(request.getParameter("timein_entry_by"));
		timeout_entry_by = PadQuotes(request.getParameter("timeout_entry_by"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (vehmove_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (vehmove_jctype_id.equals("0")) {
			msg += "<br>Select JC Type!";
		}

		if (vehmove_reg_no.equals("")) {
			msg += "<br>Enter Registration Number!";
		} else {
			vehmove_reg_no = vehmove_reg_no.replaceAll(" ", "");
			vehmove_reg_no = vehmove_reg_no.replaceAll("-", "");
			if (!IsValidRegNo(vehmove_reg_no)) {
				msg += "<br>Enter valid Registration No.!";
			}
		}

		if (vehmove_timein.equals("")) {
			msg += "<br>Enter Time In!";
		} else if (!vehmove_timein.equals("")) {
			if (isValidDateFormatLong(vehmove_timein)) {
				vehmovetimein = ConvertLongDateToStr(vehmove_timein);
			} else {
				msg += "<br>Enter valid Time In!";
			}
		}

		if (!vehmove_timeout.equals("")) {
			if (isValidDateFormatLong(vehmove_timeout)) {
				vehmovetimeout = ConvertLongDateToStr(vehmove_timeout);
			} else {
				msg += "<br>Enter valid Time Out!";
			}
		}

		if (!vehmove_timein.equals("") && !vehmove_timeout.equals("")) {
			if (isValidDateFormatLong(vehmove_timein) && isValidDateFormatLong(vehmove_timeout)) {
				if (Long.parseLong(vehmovetimeout) < Long.parseLong(vehmovetimein)) {
					msg += "<br>Time Out must be greater than or equal to the Time In!";
				}
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT vehmove_id, vehmove_reg_no, vehmove_branch_id, vehmove_jctype_id, vehmove_timein,"
					+ " vehmove_timeout, vehmove_timein_entry_id, vehmove_timeout_entry_id,"
					+ " vehmove_modified_date, vehmove_modified_id"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehmove_branch_id"
					+ " WHERE vehmove_id = " + vehmove_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehmove_reg_no = SplitRegNo(crs.getString("vehmove_reg_no"), 2);
					vehmove_branch_id = crs.getString("vehmove_branch_id");
					vehmove_jctype_id = crs.getString("vehmove_jctype_id");
					vehmove_timein = strToLongDate(crs.getString("vehmove_timein"));
					vehmove_timeout = strToLongDate(crs.getString("vehmove_timeout"));
					vehmove_timein_entry_id = crs.getString("vehmove_timein_entry_id");
					if (!vehmove_timein_entry_id.equals("0")) {
						timein_entry_by = Exename(comp_id, Integer.parseInt(vehmove_timein_entry_id));
					}
					vehmove_timeout_entry_id = crs.getString("vehmove_timeout_entry_id");
					if (!vehmove_timeout_entry_id.equals("0")) {
						timeout_entry_by = Exename(comp_id, Integer.parseInt(vehmove_timeout_entry_id));
					}
					vehmove_modified_id = crs.getString("vehmove_modified_id");
					if (!vehmove_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(vehmove_modified_id));
						modified_date = strToLongDate(crs.getString("vehmove_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle Movement!"));
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
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh_move"
					+ " SET"
					+ " vehmove_reg_no = '" + vehmove_reg_no + "',"
					+ " vehmove_branch_id = " + vehmove_branch_id + ","
					+ " vehmove_jctype_id = " + vehmove_jctype_id + ","
					+ " vehmove_timein = '" + vehmovetimein + "',"
					+ " vehmove_timeout = '" + vehmovetimeout + "',"
					+ " vehmove_modified_id = " + vehmove_modified_id + ","
					+ " vehmove_modified_date = '" + vehmove_modified_date + "'"
					+ " WHERE vehmove_id = " + vehmove_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_veh_move"
					+ " WHERE vehmove_id = " + vehmove_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Branch</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(Selectdrop(crs.getInt("branch_id"), vehmove_branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJCType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " ORDER BY jctype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id"));
				Str.append(StrSelectdrop(crs.getString("jctype_id"), vehmove_jctype_id));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
