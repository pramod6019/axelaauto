// Smitha Nag (16 march 2013)
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTestDriveDriver_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String driver_id = "0";
	public String driver_name = "";
	public String driver_active = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String driver_entry_id = "0";
	public String driver_entry_date = "";
	public String driver_modified_id = "";
	public String driver_modified_date = "";
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
				driver_id = CNumeric(PadQuotes(request.getParameter("driver_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						driver_active = "1";
					} else {
						GetValues(request, response);
						driver_entry_id = emp_id;
						driver_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivedriver.jsp?driver_id=" + driver_id + "&msg=Driver Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Driver".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Driver".equals(deleteB)) {
						GetValues(request, response);
						driver_modified_id = emp_id;
						driver_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivedriver.jsp?driver_id=" + driver_id + "&msg=Driver Updated Successfully!"));
						}
					} else if ("Delete Driver".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivedriver.jsp?msg=Driver Deleted Successfully!"));
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
		driver_id = CNumeric(PadQuotes(request.getParameter("driver_id")));
		driver_name = PadQuotes(request.getParameter("txt_driver_name"));
		driver_active = PadQuotes(request.getParameter("ch_driver_active"));
		if (driver_active.equals("on")) {
			driver_active = "1";
		} else {
			driver_active = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (driver_name.equals("")) {
			msg = msg + "<br>Enter Driver Name!";
		} else {
			try {
				if (!driver_name.equals("")) {
					StrSql = "Select driver_name"
							+ " from " + compdb(comp_id) + "axela_sales_testdrive_driver"
							+ " where driver_name = '" + driver_name + "'";
					if (update.equals("yes")) {
						StrSql += " and driver_id!='" + driver_id + "'";
					}
					// SOP("StrSql....."+StrSql);
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Driver Found!";
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				driver_id = ExecuteQuery("Select (coalesce(max(driver_id),0)+1) from " + compdb(comp_id) + "axela_sales_testdrive_driver");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_testdrive_driver"
						+ " (driver_id,"
						+ " driver_name,"
						+ " driver_active,"
						+ " driver_entry_id,"
						+ " driver_entry_date)"
						+ " values"
						+ " (" + driver_id + ","
						+ " '" + driver_name + "',"
						+ " '" + driver_active + "',"
						+ " " + driver_entry_id + ","
						+ " '" + driver_entry_date + "')";
				updateQuery(StrSql);
				// SOP(StrSqlBreaker(StrSql));
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select driver_id, driver_name, driver_active, driver_entry_date, driver_entry_id,"
					+ " driver_modified_id, driver_modified_date"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_driver"
					+ " where driver_id = " + driver_id + "";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					driver_name = crs.getString("driver_name");
					driver_active = crs.getString("driver_active");
					driver_entry_id = crs.getString("driver_entry_id");
					entry_by = Exename(comp_id, crs.getInt("driver_entry_id"));
					entry_date = strToLongDate(crs.getString("driver_entry_date"));
					driver_modified_id = crs.getString("driver_modified_id");
					if (!driver_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(driver_modified_id));
						modified_date = strToLongDate(crs.getString("driver_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Driver ID!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_driver"
						+ " SET"
						+ " driver_name = '" + driver_name + "',"
						+ " driver_active  = " + driver_active + ","
						+ " driver_modified_id = " + driver_modified_id + ","
						+ " driver_modified_date = '" + driver_modified_date + "'"
						+ " where driver_id = " + driver_id + "";
				// SOP("StrSql--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT testdrive_out_driver_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " where testdrive_out_driver_id = " + driver_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Driver is associated with Test Drive Mileage!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive_driver"
						+ " where driver_id = " + driver_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
