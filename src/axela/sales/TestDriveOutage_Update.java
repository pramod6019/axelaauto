package axela.sales;
//@Bhagwan Singh 11 feb 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class TestDriveOutage_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String outage_id = "";
	public String outage_veh_id = "0";
	public String testdrive_type = "";
	public String outage_fromtime = "";
	public String outagefromtime = "";
	public String outagetotime = "";
	public String outage_totime = "";
	public String testdrive_location_id = "0";
	public String outage_desc = "";
	public String outage_notes = "";
	public String outage_entry_id = "0";
	public String outage_entry_date = "";
	public String outage_modified_id = "";
	public String outage_modified_date = "";
	public String veh_branch_id = "0";
	public String BranchAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				BranchAccess = GetSession("BranchAccess", request);
				outage_id = CNumeric(PadQuotes(request.getParameter("outage_id")));
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						outage_veh_id = "";
						testdrive_location_id = "";
						testdrive_type = "1";
						outage_desc = "";
						outage_notes = "";
						outagefromtime = "";
						outage_fromtime = "";
						outagetotime = "";
						outage_totime = "";
					} else {
						if (ReturnPerm(comp_id, "emp_testdrive_add", request).equals("1")) {
							GetValues(request, response);
							outage_entry_id = CNumeric(GetSession("emp_id", request));
							outage_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("testdriveoutage-list.jsp?outage_id=" + outage_id + "&msg=Vehicle Outage Added Successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Vehicle Outage".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Vehicle Outage".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							GetValues(request, response);
							outage_modified_id = CNumeric(GetSession("emp_id", request));
							outage_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("testdriveoutage-list.jsp?outage_id=" + outage_id + "&msg=Vehicle Outage Details Updated Successfully"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Vehicle Outage".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_testdrive_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("testdriveoutage-list.jsp?all=yes"));
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
		outage_id = CNumeric(PadQuotes(request.getParameter("outage_id")));
		veh_branch_id = PadQuotes(request.getParameter("dr_branch_id"));
		outage_veh_id = PadQuotes(request.getParameter("dr_vehicle"));
		outagefromtime = PadQuotes(request.getParameter("txt_outage_fromtime"));
		outage_fromtime = ConvertLongDateToStr(outagefromtime);
		outagetotime = PadQuotes(request.getParameter("txt_outage_totime"));
		outage_totime = ConvertLongDateToStr(outagetotime);
		outage_desc = PadQuotes(request.getParameter("txt_outage_desc"));
		outage_notes = PadQuotes(request.getParameter("txt_outage_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (veh_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (outage_veh_id.equals("0")) {
			msg = msg + "<br>Select Vehicle!";
		}
		if (outagefromtime.equals("")) {
			msg = msg + "<br>Select From Date!";
		}
		if (outagetotime.equals("")) {
			msg = msg + "<br>Select To Date!";
		}
		if (!outagefromtime.equals("")) {
			if (!isValidDateFormatLong(outagefromtime)) {
				msg = msg + "<br>Select valid From Time!";
			} else {
				outage_fromtime = ConvertLongDateToStr(outagefromtime);
			}
		}
		if (!outagetotime.equals("")) {
			if (!isValidDateFormatLong(outagetotime)) {
				msg = msg + "<br>Select valid To Time!";
			} else {
				outage_totime = ConvertLongDateToStr(outagetotime);
			}
			if (Long.parseLong(outage_totime) <= Long.parseLong(outage_fromtime)) {
				msg = msg + "<br>To Time must be greater than " + strToLongDate(outage_fromtime) + "!";
			}
		}
		if (outage_desc.length() > 1000) {
			outage_desc = outage_desc.substring(0, 999);
		}
		if (outage_notes.length() > 1000) {
			outage_notes = outage_notes.substring(0, 999);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				outage_id = ExecuteQuery("SELECT (coalesce(max(outage_id),0)+1) FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage"
						+ " (outage_id,"
						+ " outage_veh_id,"
						+ " outage_fromtime, "
						+ " outage_totime, "
						+ " outage_desc,"
						+ " outage_notes,"
						+ " outage_entry_id,"
						+ " outage_entry_date"
						+ " )"
						+ " values"
						+ " (" + outage_id + ","
						+ " " + outage_veh_id + ","
						+ " '" + outage_fromtime + "',"
						+ " '" + outage_totime + "',"
						+ " '" + outage_desc + "',"
						+ " '" + outage_notes + "',"
						+ " " + outage_entry_id + ","
						+ " '" + outage_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage"
						+ " SET"
						+ " outage_veh_id = " + outage_veh_id + ","
						+ " outage_fromtime = '" + outage_fromtime + "',"
						+ " outage_totime = '" + outage_totime + "', "
						+ " outage_desc = '" + outage_desc + "', "
						+ " outage_notes = '" + outage_notes + "', "
						+ " outage_modified_id = " + outage_modified_id + ", "
						+ " outage_modified_date = '" + outage_modified_date + "'"
						+ " where outage_id = " + outage_id + "";
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
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage where outage_id = " + outage_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select outage_veh_id, outage_fromtime, outage_totime, outage_modified_id,"
					+ " outage_entry_id, outage_entry_date, outage_modified_date, outage_desc,"
					+ " outage_notes, veh_branch_id"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage"
					+ " inner join " + compdb(comp_id) + "axela_sales_testdrive_vehicle on veh_id = outage_veh_id"
					+ " where outage_id = " + outage_id + "";
			// SOP("query of PopulateFields " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					outage_veh_id = crs.getString("outage_veh_id");
					outage_fromtime = crs.getString("outage_fromtime");
					veh_branch_id = crs.getString("veh_branch_id");
					outagefromtime = strToLongDate(outage_fromtime);
					outage_totime = crs.getString("outage_totime");
					outagetotime = strToLongDate(outage_totime);
					outage_desc = crs.getString("outage_desc");
					outage_notes = crs.getString("outage_notes");
					outage_entry_id = crs.getString("outage_entry_id");
					entry_by = Exename(comp_id, crs.getInt("outage_entry_id"));
					entry_date = strToLongDate(crs.getString("outage_entry_date"));
					outage_modified_id = crs.getString("outage_modified_id");
					if (!outage_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(outage_modified_id));
						modified_date = strToLongDate(crs.getString("outage_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Test Drive Outage!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranch() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "Select branch_id,branch_name, branch_code from " + compdb(comp_id) + "axela_branch"
					+ " where branch_active = '1' " + BranchAccess
					+ " order by branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value=0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), veh_branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateVehicle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select veh_id, veh_name, veh_regno"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_vehicle "
					+ " where veh_branch_id = " + veh_branch_id
					+ " order by veh_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_vehicle class=form-control id=dr_vehicle>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("veh_id"));
				Str.append(StrSelectdrop(crs.getString("veh_id"), outage_veh_id));
				Str.append(">").append(crs.getString("veh_name")).append(" - ").append(crs.getString("veh_regno")).append("</option>\n");
			}
			Str.append("</Select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
