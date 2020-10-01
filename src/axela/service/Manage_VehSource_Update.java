package axela.service;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_VehSource_Update extends Connect {

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
	public String branch_id = "0";
	public String vehsource_id = "0";
	public String vehsource_name = "";
	public String QueryString = "";
	public String vehsource_entry_id = "0";
	public String vehsource_entry_date = "";
	public String vehsource_modified_id = "0";
	public String vehsource_modified_date = "";
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
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				vehsource_id = CNumeric(PadQuotes(request.getParameter("vehsource_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						vehsource_name = "";
					} else {
						GetValues(request, response);
						vehsource_entry_id = emp_id;
						vehsource_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-vehsource.jsp?vehsource_id=" + vehsource_id + "&msg=Vehicle Source Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Vehicle Source".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Vehicle Source".equals(deleteB)) {
						GetValues(request, response);
						vehsource_modified_id = emp_id;
						vehsource_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-vehsource.jsp?vehsource_id=" + vehsource_id + "&msg=Vehicle Source Updated Successfully!"));
						}
					} else if ("Delete Vehicle Source".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-vehsource.jsp?msg=Vehicle Source Deleted Successfully!"));
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
		vehsource_name = PadQuotes(request.getParameter("txt_vehsource_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (vehsource_name.equals("")) {
			msg = msg + "<br>Enter Vehicle Source!";
		}
		try {
			if (!vehsource_name.equals("")) {
				StrSql = "Select vehsource_name from " + compdb(comp_id) + "axela_service_veh_source where vehsource_name = '" + vehsource_name + "'";
				if (update.equals("yes")) {
					StrSql += " and vehsource_id != " + vehsource_id + "";
				}
				if (add.equals("yes")) {
					StrSql += " and vehsource_id != " + vehsource_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Vehicle Source Found! ";
				}
				crs.close();
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
				vehsource_id = ExecuteQuery("SELECT (COALESCE(MAX(vehsource_id),0)+1)"
						+ " FROM " + compdb(comp_id) + "axela_service_veh_source");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_source"
						+ " (vehsource_name,"
						+ " vehsource_entry_id,"
						+ " vehsource_entry_date)"
						+ " values"
						+ " ('" + vehsource_name + "',"
						// + " '" + vehsource_name + "')"
						+ " " + vehsource_entry_id + ","
						+ " '" + vehsource_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_service_veh_source"
					+ " WHERE vehsource_id=" + vehsource_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehsource_name = crs.getString("vehsource_name");
					vehsource_entry_id = crs.getString("vehsource_entry_id");
					if (!vehsource_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(vehsource_entry_id));
						entry_date = strToLongDate(crs.getString("vehsource_entry_date"));
					}
					vehsource_modified_id = crs.getString("vehsource_modified_id");
					if (!vehsource_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(vehsource_modified_id));
						modified_date = strToLongDate(crs.getString("vehsource_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle Source!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh_source"
						+ " SET"
						+ " vehsource_name = '" + vehsource_name + "',"
						+ " vehsource_modified_id = " + vehsource_modified_id + ","
						+ " vehsource_modified_date = '" + vehsource_modified_date + "'"
						+ " WHERE vehsource_id = " + vehsource_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		// StrSql = "SELECT enquiry_sob_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_sob_id = " + vehsource_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>SOB is Associated with Enquiry!";
		// }
		// StrSql = "SELECT lead_sob_id FROM " + compdb(comp_id) + "axela_sales_lead where lead_sob_id = " + vehsource_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>SOB is Associated with Lead!";
		// }
		StrSql = "SELECT veh_vehsource_id"
				+ " FROM " + compdb(comp_id) + "axela_service_veh"
				+ " WHERE veh_vehsource_id = " + vehsource_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Vehicle Source is Associated With Service Vehicle!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_service_veh_source"
						+ " WHERE vehsource_id = " + vehsource_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
