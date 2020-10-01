package axela.service;
/*
 * @author Bhagwan Singh, 8/01/2013
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTicketCat_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String ticketcat_id = "0";
	public String ticketdept_id = "0";
	public String ticketcat_name = "";
	public String ticketcat_entry_id = "0";
	public String ticketcat_entry_by = "";
	public String ticketcat_entry_date = "";
	public String ticketcat_modified_id = "0";
	public String ticketcat_modified_by = "";
	public String ticketcat_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "", modified_by = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ticketcat_id = CNumeric(PadQuotes(request.getParameter("ticketcat_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					if (ticketcat_id.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("index.jsp"));
					} else if (ticketcat_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket Category!"));
					}
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						ticketcat_entry_id = emp_id;
						ticketcat_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageticketcat.jsp?ticketcat_id=" + ticketcat_id + "&msg=Ticket Category Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Ticket Category".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Ticket Category".equals(deleteB)) {
						GetValues(request, response);
						ticketcat_modified_id = emp_id;
						ticketcat_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageticketcat.jsp?ticketcat_id=" + ticketcat_id + "&msg=Ticket Category Updated Successfully!"));
						}
					}
					if ("Delete Ticket Category".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageticketcat.jsp?msg=Ticket Category Deleted Successfully!"));
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
		ticketdept_id = CNumeric((request.getParameter("dr_catdept_id")));
		ticketcat_name = PadQuotes(request.getParameter("txt_ticketcat_name"));
		ticketcat_entry_by = PadQuotes(request.getParameter("entry_by"));
		ticketcat_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (ticketdept_id.equals("0")) {
			msg = "<br>Select Department!";
		}
		if (ticketcat_name.equals("")) {
			msg += "<br>Enter Ticket Category Name!";
		}
		try {
			if (!ticketcat_name.equals("") && !ticketdept_id.equals("0")) {
				StrSql = "SELECT ticketcat_name"
						+ " FROM " + compdb(comp_id) + "axela_service_ticket_cat"
						+ " WHERE 1=1"
						+ " AND ticketcat_ticketdept_id = " + ticketdept_id
						+ " AND ticketcat_name = '" + ticketcat_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND ticketcat_id != " + ticketcat_id + "";
				}
				SOP("StrSql===" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Ticket Category Found!";
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
				ticketcat_id = ExecuteQuery("SELECT coalesce(max(ticketcat_id),0)+1 as ticketcat_id FROM " + compdb(comp_id) + "axela_service_ticket_cat");

				StrSql = "INSERT into " + compdb(comp_id) + "axela_service_ticket_cat"
						+ "(ticketcat_id,"
						+ " ticketcat_ticketdept_id,"
						+ " ticketcat_name,"
						+ " ticketcat_entry_id,"
						+ " ticketcat_entry_date)"
						+ " values"
						+ " (" + ticketcat_id + ","
						+ " '" + ticketdept_id + "',"
						+ " '" + ticketcat_name + "',"
						+ " " + ticketcat_entry_id + ","
						+ " '" + ticketcat_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT *"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_cat"
					+ " WHERE ticketcat_id = " + ticketcat_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticketdept_id = crs.getString("ticketcat_ticketdept_id");
					ticketcat_name = crs.getString("ticketcat_name");
					ticketcat_entry_id = crs.getString("ticketcat_entry_id");
					if (!ticketcat_entry_id.equals("0")) {
						ticketcat_entry_by = Exename(comp_id, Integer.parseInt(ticketcat_entry_id));
					}
					entry_date = strToLongDate(crs.getString("ticketcat_entry_date"));
					ticketcat_modified_id = crs.getString("ticketcat_modified_id");
					if (!ticketcat_modified_id.equals("0")) {
						ticketcat_modified_by = Exename(comp_id, Integer.parseInt(ticketcat_modified_id));
					}
					modified_date = strToLongDate(crs.getString("ticketcat_modified_date"));

				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Ticket Category!"));
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
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_service_ticket_cat"
						+ " SET"
						+ " ticketcat_ticketdept_id=" + ticketdept_id + ","
						+ " ticketcat_name = '" + ticketcat_name + "',"
						+ " ticketcat_modified_id = " + ticketcat_modified_id + ","
						+ " ticketcat_modified_date = '" + ticketcat_modified_date + "'"
						+ " WHERE ticketcat_id = " + ticketcat_id + "";
				SOP("coming..." + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT ticket_ticketcat_id FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_ticketcat_id = " + ticketcat_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Category is associated with Ticket!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_ticket_cat WHERE ticketcat_id = " + ticketcat_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateDepartment() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticket_dept_id, ticket_dept_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept "
					+ " WHERE 1 = 1 "
					+ " GROUP BY ticket_dept_id"
					+ " ORDER BY ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), ticketdept_id));
				Str.append(">").append(crs.getString("ticket_dept_name")).append("</option> \n");
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
