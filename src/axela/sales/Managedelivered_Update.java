package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Managedelivered_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String delstatus_id = "0";
	public String delstatus_name = "";
	public String delstatus_rank = "";
	public String QueryString = "";
	public String delstatus_entry_id = "0";
	public String delstatus_entry_date = "";
	public String delstatus_modified_id = "0";
	public String delstatus_modified_date = "";
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
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						delstatus_name = "";
					} else {
						GetValues(request, response);
						delstatus_entry_id = emp_id;
						delstatus_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managedelivered.jsp?delstatus_id=" + delstatus_id + "&msg=Delivery Status Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Delivery Status".equals(deleteB)) {
						delstatus_id = CNumeric(PadQuotes(request.getParameter("delstatus_id")));
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Delivery Status".equals(deleteB)) {
						GetValues(request, response);
						delstatus_modified_id = emp_id;
						delstatus_modified_date = ToLongDate(kknow());
						UpdateFields(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managedelivered.jsp?delstatus_id=" + delstatus_id + "&msg=Delivery Status Updated Successfully!"));
						}
					} else if ("Delete Delivery Status".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields(request);
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managedelivered.jsp?msg=Delivery Status Deleted Successfully!"));
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
		delstatus_id = CNumeric(PadQuotes(request.getParameter("delstatus_id")));
		delstatus_name = PadQuotes(request.getParameter("txt_delstatus_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (delstatus_name.equals("")) {
				msg = msg + "<br> Enter the status Name!";
			}

			if (!delstatus_name.equals("")) {
				StrSql = "Select delstatus_name from " + compdb(comp_id) + "axela_sales_so_delstatus where delstatus_name = '" + delstatus_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and delstatus_id != " + delstatus_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Status Name Found!";
				}
				crs.close();
			}
			if (update.equals("yes")) {
				if (delstatus_id.equals("5")) {
					msg = msg + " <br>Delivered Status Cannot Be Deleted";
				}
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
				delstatus_id = ExecuteQuery("Select (coalesce(max(delstatus_id),0)+1) from " + compdb(comp_id) + "axela_sales_so_delstatus");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_so_delstatus"
						+ " (delstatus_id,"
						+ " delstatus_name,"
						+ " delstatus_rank,"
						+ " delstatus_entry_id,"
						+ " delstatus_entry_date)"
						+ " values"
						+ " (" + delstatus_id + ","
						+ " '" + delstatus_name + "',"
						+ " (Select (coalesce(max(delstatus_rank),0)+1) from " + compdb(comp_id) + "axela_sales_so_delstatus as Rank where 1=1),"
						+ " " + delstatus_entry_id + ","
						+ " '" + delstatus_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "select *"
					+ " from " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " where delstatus_id = " + delstatus_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					delstatus_name = crs.getString("delstatus_name");
					delstatus_entry_id = crs.getString("delstatus_entry_id");
					if (!delstatus_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(delstatus_entry_id));
						entry_date = strToLongDate(crs.getString("delstatus_entry_date"));
					}
					delstatus_modified_id = crs.getString("delstatus_modified_id");
					if (!delstatus_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(delstatus_modified_id));
						modified_date = strToLongDate(crs.getString("delstatus_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Delivery Status!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = " UPDATE " + compdb(comp_id) + "axela_sales_so_delstatus"
						+ " SET"
						+ " delstatus_name = '" + delstatus_name + "',"
						+ " delstatus_modified_id = " + delstatus_modified_id + ","
						+ " delstatus_modified_date = '" + delstatus_modified_date + "'"
						+ " where delstatus_id = " + delstatus_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields(HttpServletRequest request) {
		if (delstatus_id.equals("5")) {
			msg = msg + " <br>Delivered Status Cannot Be Deleted";
		}
		StrSql = "SELECT vehstock_delstatus_id from " + compdb(comp_id) + "axela_vehstock where vehstock_delstatus_id = " + delstatus_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Delivery Status is associated with Stock!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_so_delstatus where delstatus_id = " + delstatus_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
