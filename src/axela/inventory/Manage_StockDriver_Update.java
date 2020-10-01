// Smitha Nag (16 march 2013)
package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_StockDriver_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String stockdriver_id = "0";
	public String stockdriver_name = "";
	public String stockdriver_active = "";

	public String checkperm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String stockdriver_entry_id = "0";
	public String stockdriver_entry_date = "";
	public String stockdriver_modified_id = "";
	public String stockdriver_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				stockdriver_id = CNumeric(PadQuotes(request.getParameter("stockdriver_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						stockdriver_active = "1";
					} else {
						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							GetValues(request, response);
							stockdriver_entry_id = emp_id;
							stockdriver_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-stockdriver.jsp?stockdriver_id=" + stockdriver_id + "&msg=Driver Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Driver".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Driver".equals(deleteB)) {

						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							GetValues(request, response);
							stockdriver_modified_id = emp_id;
							stockdriver_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-stockdriver.jsp?stockdriver_id=" + stockdriver_id + "&msg=Driver Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Driver".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_stock_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-stockdriver.jsp?msg=Driver Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		stockdriver_id = CNumeric(PadQuotes(request.getParameter("txt_stockdriver_name")));
		stockdriver_name = PadQuotes(request.getParameter("txt_stockdriver_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		stockdriver_active = CheckBoxValue(PadQuotes(request.getParameter("ch_driver_active")));
	}

	protected void CheckForm() {
		if (stockdriver_name.equals("")) {
			msg = msg + "<br>Enter Driver Name!";
		} else {
			try {
				if (!stockdriver_name.equals("")) {
					StrSql = "SELECT vehstockdriver_name"
							+ " FROM " + compdb(comp_id) + "axela_vehstock_driver"
							+ " WHERE vehstockdriver_name = '" + stockdriver_name + "'";
					if (update.equals("yes")) {
						StrSql += " and vehstockdriver_id!='" + stockdriver_id + "'";
					}
					// SOP("StrSql....."+StrSql);
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Driver Found!";
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				stockdriver_id = ExecuteQuery("SELECT (COALESCE(MAX(vehstockdriver_id),0)+1) FROM " + compdb(comp_id) + "axela_vehstock_driver");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_driver"
						+ " (vehstockdriver_id,"
						+ " vehstockdriver_name,"
						+ " vehstockdriver_active,"
						+ " vehstockdriver_entry_id,"
						+ " vehstockdriver_entry_date)"
						+ " VALUES"
						+ " (" + stockdriver_id + ","
						+ " '" + stockdriver_name + "',"
						+ " '" + stockdriver_active + "',"
						+ " " + stockdriver_entry_id + ","
						+ " '" + stockdriver_entry_date + "')";
				updateQuery(StrSql);
				// SOP(StrSqlBreaker(StrSql));
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT vehstockdriver_id, vehstockdriver_name, "
					+ " vehstockdriver_entry_date, vehstockdriver_entry_id, vehstockdriver_active, "
					+ " vehstockdriver_modified_id, vehstockdriver_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_driver"
					+ " WHERE vehstockdriver_id = " + stockdriver_id + "";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					stockdriver_name = crs.getString("vehstockdriver_name");
					// driver_active = crs.getString("driver_active");
					stockdriver_entry_id = crs.getString("vehstockdriver_entry_id");
					entry_by = Exename(comp_id, crs.getInt("vehstockdriver_entry_id"));
					entry_date = strToLongDate(crs.getString("vehstockdriver_entry_date"));
					stockdriver_modified_id = crs.getString("vehstockdriver_modified_id");
					stockdriver_active = crs.getString("vehstockdriver_active");
					if (!stockdriver_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(stockdriver_modified_id));
						modified_date = strToLongDate(crs.getString("vehstockdriver_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Driver ID!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock_driver"
						+ " SET"
						+ " vehstockdriver_name = '" + stockdriver_name + "',"
						+ " vehstockdriver_active = '" + stockdriver_active + "',"
						+ " vehstockdriver_modified_id = " + stockdriver_modified_id + ","
						+ " vehstockdriver_modified_date = '" + stockdriver_modified_date + "'"
						+ " WHERE vehstockdriver_id = " + stockdriver_id + "";
				// SOP("StrSql--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT testdrive_out_stockdriver_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive"
				+ " WHERE testdrive_out_stockdriver_id = " + stockdriver_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Driver is associated with Stock Mileage!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_driver"
						+ " WHERE vehstockdriver_id = " + stockdriver_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
