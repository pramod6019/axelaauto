package axela.service;
//@Dilip Kumar 03 APR 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managecourtesyvehicleoutage_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String checkperm = "";
	public String entry_by = "";
	public String modified_by = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String courtesyoutage_id = "0";
	public String courtesyoutage_courtesyveh_id = "0";
	public String courtesyoutage_fromtime = "";
	public String courtesyoutagefromtime = "";
	public String courtesyoutagetotime = "";
	public String courtesyoutage_totime = "";
	public String courtesyoutage_desc = "";
	public String courtesyoutage_notes = "";
	public String courtesyoutage_entry_id = "0";
	public String courtesyoutage_entry_date = "";
	public String courtesyoutage_modified_id = "";
	public String courtesyoutage_modified_date = "";
	public String courtesyveh_branch_id = "0";
	public String BranchAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_service_courtesy_access", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				BranchAccess = GetSession("BranchAccess", request);
				courtesyoutage_id = CNumeric(PadQuotes(request.getParameter("courtesyoutage_id")));

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_courtesy_add", request).equals("1")) {
							courtesyoutage_entry_id = CNumeric(GetSession("emp_id", request));
							courtesyoutage_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managecourtesyvehicleoutage.jsp?courtesyoutage_id=" + courtesyoutage_id + "&msg=Vehicle Outage added successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !deleteB.equals("Delete Vehicle Outage")) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !deleteB.equals("Delete Vehicle Outage")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_courtesy_edit", request).equals("1")) {
							courtesyoutage_modified_id = CNumeric(GetSession("emp_id", request));
							courtesyoutage_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managecourtesyvehicleoutage.jsp?courtesyoutage_id=" + courtesyoutage_id + "&msg=Vehicle Outage updated successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Vehicle Outage")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_courtesy_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managecourtesyvehicleoutage.jsp?msg=Vehicle Outage deleted successfully"));
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

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		courtesyoutage_id = CNumeric(PadQuotes(request.getParameter("courtesyoutage_id")));
		courtesyveh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		courtesyoutage_courtesyveh_id = CNumeric(PadQuotes(request.getParameter("dr_vehicle")));
		courtesyoutagefromtime = PadQuotes(request.getParameter("txt_outage_fromtime"));
		courtesyoutage_fromtime = ConvertLongDateToStr(courtesyoutagefromtime);
		courtesyoutagetotime = PadQuotes(request.getParameter("txt_outage_totime"));
		courtesyoutage_totime = ConvertLongDateToStr(courtesyoutagetotime);
		courtesyoutage_desc = PadQuotes(request.getParameter("txt_outage_desc"));
		courtesyoutage_notes = PadQuotes(request.getParameter("txt_outage_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		courtesyoutage_entry_date = PadQuotes(request.getParameter("courtesyoutage_entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		courtesyoutage_modified_date = PadQuotes(request.getParameter("courtesyoutage_modified_date"));
	}

	protected void CheckForm() {
		if (courtesyveh_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (courtesyoutage_courtesyveh_id.equals("0")) {
			msg += "<br>Select Vehicle!";
		}

		if (courtesyoutagefromtime.equals("")) {
			msg += "<br>Select From Date!";
		} else {
			if (!isValidDateFormatLong(courtesyoutagefromtime)) {
				msg += "<br>Select valid From Time!";
			} else {
				courtesyoutage_fromtime = ConvertLongDateToStr(courtesyoutagefromtime);
			}
		}

		if (courtesyoutagetotime.equals("")) {
			msg += "<br>Select To Date!";
		} else {
			if (!isValidDateFormatLong(courtesyoutagetotime)) {
				msg += "<br>Select valid To Time!";
			} else {
				courtesyoutage_totime = ConvertLongDateToStr(courtesyoutagetotime);
			}
			if (Long.parseLong(courtesyoutage_totime) <= Long.parseLong(courtesyoutage_fromtime)) {
				msg += "<br>To Time must be greater than " + strToLongDate(courtesyoutage_fromtime) + "!";
			}
		}

		if (courtesyoutage_desc.length() > 1000) {
			courtesyoutage_desc = courtesyoutage_desc.substring(0, 999);
		}

		if (courtesyoutage_notes.length() > 1000) {
			courtesyoutage_notes = courtesyoutage_notes.substring(0, 999);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			courtesyoutage_id = ExecuteQuery("SELECT (COALESCE(MAX(courtesyoutage_id), 0) + 1)"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle_outage");

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_courtesy_vehicle_outage"
					+ " (courtesyoutage_id,"
					+ " courtesyoutage_courtesyveh_id,"
					+ " courtesyoutage_fromtime, "
					+ " courtesyoutage_totime, "
					+ " courtesyoutage_desc,"
					+ " courtesyoutage_notes,"
					+ " courtesyoutage_entry_id,"
					+ " courtesyoutage_entry_date)"
					+ " VALUES"
					+ " (" + courtesyoutage_id + ","
					+ " " + courtesyoutage_courtesyveh_id + ","
					+ " '" + courtesyoutage_fromtime + "',"
					+ " '" + courtesyoutage_totime + "',"
					+ " '" + courtesyoutage_desc + "',"
					+ " '" + courtesyoutage_notes + "',"
					+ " " + courtesyoutage_entry_id + ","
					+ " '" + courtesyoutage_entry_date + "')";
			updateQuery(StrSql);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_service_courtesy_vehicle_outage"
					+ " SET"
					+ " courtesyoutage_courtesyveh_id = " + courtesyoutage_courtesyveh_id + ","
					+ " courtesyoutage_fromtime = '" + courtesyoutage_fromtime + "',"
					+ " courtesyoutage_totime = '" + courtesyoutage_totime + "', "
					+ " courtesyoutage_desc = '" + courtesyoutage_desc + "', "
					+ " courtesyoutage_notes = '" + courtesyoutage_notes + "', "
					+ " courtesyoutage_modified_id = " + courtesyoutage_modified_id + ", "
					+ " courtesyoutage_modified_date = '" + courtesyoutage_modified_date + "'"
					+ " WHERE courtesyoutage_id = " + courtesyoutage_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle_outage"
					+ " WHERE courtesyoutage_id = " + courtesyoutage_id + "";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT courtesyoutage_courtesyveh_id, courtesyoutage_fromtime, courtesyoutage_totime,"
					+ " courtesyoutage_modified_id, courtesyoutage_entry_id, courtesyoutage_entry_date,"
					+ " courtesyoutage_modified_date, courtesyoutage_desc,"
					+ " courtesyoutage_notes, courtesyveh_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle_outage"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesyoutage_courtesyveh_id"
					+ " WHERE courtesyoutage_id = " + courtesyoutage_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					courtesyoutage_courtesyveh_id = crs.getString("courtesyoutage_courtesyveh_id");
					courtesyoutage_fromtime = crs.getString("courtesyoutage_fromtime");
					courtesyveh_branch_id = crs.getString("courtesyveh_branch_id");
					courtesyoutagefromtime = strToLongDate(courtesyoutage_fromtime);
					courtesyoutage_totime = crs.getString("courtesyoutage_totime");
					courtesyoutagetotime = strToLongDate(courtesyoutage_totime);
					courtesyoutage_desc = crs.getString("courtesyoutage_desc");
					courtesyoutage_notes = crs.getString("courtesyoutage_notes");
					courtesyoutage_entry_id = crs.getString("courtesyoutage_entry_id");
					entry_by = Exename(comp_id, crs.getInt("courtesyoutage_entry_id"));
					courtesyoutage_entry_date = strToLongDate(crs.getString("courtesyoutage_entry_date"));
					courtesyoutage_modified_id = crs.getString("courtesyoutage_modified_id");
					if (!courtesyoutage_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(courtesyoutage_modified_id));
						courtesyoutage_modified_date = strToLongDate(crs.getString("courtesyoutage_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle Outage!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1" + BranchAccess + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), courtesyveh_branch_id));
				Str.append(">").append(crs.getString("branch_name"));
				Str.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateVehicle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT courtesyveh_id, courtesyveh_name, courtesyveh_regno"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_vehicle"
					+ " WHERE courtesyveh_active = 1"
					+ " AND courtesyveh_branch_id = '" + courtesyveh_branch_id + "'"
					+ " ORDER BY courtesyveh_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_vehicle\" class=form-control id=\"dr_vehicle\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("courtesyveh_id"));
				Str.append(StrSelectdrop(crs.getString("courtesyveh_id"), courtesyoutage_courtesyveh_id));
				Str.append(">").append(crs.getString("courtesyveh_name")).append(" - ");
				Str.append(SplitRegNo(crs.getString("courtesyveh_regno"), 2)).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
