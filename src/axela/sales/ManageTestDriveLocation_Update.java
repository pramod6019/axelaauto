package axela.sales;
//@Bhagwan Singh 12 feb 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTestDriveLocation_Update extends Connect {

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
	public String location_id = "0";
	public String location_name = "";
	public String location_branch_id = "0";
	public String location_leadtime = "";
	public String location_testdrive_dur = "";
	public String location_active = "";
	public String QueryString = "";
	public String location_entry_id = "0";
	public String location_entry_date = "";
	public String location_modified_id = "0";
	public String location_modified_date = "";
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
				location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						location_name = "";
						location_active = "1";
					} else {
						GetValues(request, response);
						location_entry_id = emp_id;
						location_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivelocation.jsp?location_id=" + location_id + "&msg=Location Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Location".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Location".equals(deleteB)) {
						GetValues(request, response);
						location_modified_id = emp_id;
						location_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivelocation.jsp?location_id=" + location_id + "&msg=Location Updated Successfully!"));
						}
					} else if ("Delete Location".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivelocation.jsp?msg=Location Deleted Successfully!"));
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
		location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
		location_name = PadQuotes(request.getParameter("txt_location_name"));
		location_branch_id = PadQuotes(request.getParameter("dr_location_branch_id"));
		location_leadtime = PadQuotes(request.getParameter("txt_location_leadtime"));
		location_testdrive_dur = PadQuotes(request.getParameter("txt_location_testdrive_dur"));
		location_active = PadQuotes(request.getParameter("chk_location_active"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		if (location_active.equals("on")) {
			location_active = "1";
		} else {
			location_active = "0";
		}
	}

	protected void CheckForm() {
		try {
			if (location_branch_id.equals("0")) {
				msg = msg + "<br> Select the Branch Name!";
			}
			if (location_name.equals("")) {
				msg = msg + "<br> Enter the Location Name!";
			}
			if (location_leadtime.equals("")) {
				msg = msg + "<br> Enter the Lead Time!";
			}
			if (location_testdrive_dur.equals("")) {
				msg = msg + "<br> Enter the Test Drive Duration!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				location_id = ExecuteQuery("Select (coalesce(max(location_id),0)+1) from " + compdb(comp_id) + "axela_sales_testdrive_location");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_testdrive_location"
						+ " (location_id,"
						+ " location_name,"
						+ " location_branch_id,"
						+ " location_leadtime,"
						+ " location_testdrive_dur,"
						+ " location_active,"
						+ " location_entry_id,"
						+ " location_entry_date)"
						+ " values"
						+ " (" + location_id + ","
						+ " '" + location_name + "',"
						+ " " + location_branch_id + ","
						+ " " + location_leadtime + ","
						+ " " + location_testdrive_dur + ","
						+ " " + location_active + ","
						+ " " + location_entry_id + ","
						+ " '" + location_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			StrSql = "SELECT location_name, location_branch_id, location_leadtime,"
					+ " location_testdrive_dur, location_active, location_entry_id,"
					+ " location_entry_date, COALESCE(location_modified_id, '0') as location_modified_id,"
					+ " COALESCE(location_modified_date, '') as location_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_location"
					+ " WHERE location_id = " + location_id + "";
			// SOP("StrSql...."+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					location_name = crs.getString("location_name");
					location_branch_id = crs.getString("location_branch_id");
					location_leadtime = crs.getString("location_leadtime");
					location_testdrive_dur = crs.getString("location_testdrive_dur");
					location_active = crs.getString("location_active");
					location_entry_id = crs.getString("location_entry_id");
					if (!location_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(location_entry_id));
						entry_date = strToLongDate(crs.getString("location_entry_date"));
					}
					location_modified_id = crs.getString("location_modified_id");
					if (!location_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(location_modified_id));
						modified_date = strToLongDate(crs.getString("location_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Location!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_location"
						+ " SET"
						+ " location_name = '" + location_name + "',"
						+ " location_branch_id = " + location_branch_id + ","
						+ " location_leadtime = " + location_leadtime + ","
						+ " location_testdrive_dur = " + location_testdrive_dur + ","
						+ " location_active = " + location_active + ","
						+ " location_modified_id = " + location_modified_id + ","
						+ " location_modified_date = '" + location_modified_date + "'"
						+ " WHERE location_id = " + location_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		StrSql = "SELECT COUNT(testdrive_id) FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " WHERE testdrive_location_id = " + location_id + "";
		if (!ExecuteQuery(StrSql).equals("0")) {
			msg += "<br>Location is associated with Testdrive!";
		}
		
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_testdrive_location"
						+ " WHERE location_id = " + location_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
