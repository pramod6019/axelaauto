package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Ticket_Faq_Cat_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String service_id = "0";
	public String service_name = "";
	public String service_entry_id = "0";
	public String entry_by = "";
	public String service_entry_date = "";
	public String entry_date = "";
	public String service_modified_id = "0";
	public String modified_by = "";
	public String service_modified_date = "";
	public String modified_date = "";
	public String chkPermMsg = "";
	public String ticket_ticket_dept_id = "0";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_service_faq_access", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				service_id = CNumeric(PadQuotes(request.getParameter("service_id")));
				ticket_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_dept_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_faq_add", request).equals("1")) {
							service_entry_id = emp_id;
							service_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-faq-cat-list.jsp?msg=Category added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Category".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Category".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_faq_edit", request).equals("1")) {
							service_modified_id = emp_id;
							service_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-faq-cat-list.jsp?service_id=" + service_id + "&msg=Category updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Category".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_faq_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-faq-cat-list.jsp?service_id=" + service_id + "&msg=Category deleted successfully!"));
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
		service_name = PadQuotes(request.getParameter("txt_service_name"));
		ticket_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_dept_id")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (ticket_ticket_dept_id.equals("0")) {
			msg = msg + "<br>Select Department!";
		}
		if (service_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}
		try {
			StrSql = "Select faqservice_name"
					+ " from " + compdb(comp_id) + "axela_service_ticket_faq_service"
					+ " where faqservice_name = '" + service_name + "'";
			if (update.equals("yes")) {
				StrSql = StrSql + " and faqservice_id != " + CNumeric(service_id) + "";
			}
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				msg = msg + "<br>Similar Name found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "Insert into " + compdb(comp_id) + "axela_service_ticket_faq_service"
						+ " (faqservice_id,"
						+ " faqservice_ticket_dept_id,"
						+ " faqservice_name)"
						+ " values"
						+ " ((Select (coalesce(max(faqservice_id),0)+1) from " + compdb(comp_id) + "axela_service_ticket_faq_service as category_id),"
						+ " '" + ticket_ticket_dept_id + "',"
						+ " '" + service_name + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			StrSql = "Select faqservice_id, faqservice_name, faqservice_ticket_dept_id"
					+ " from " + compdb(comp_id) + "axela_service_ticket_faq_service"
					+ " inner join " + compdb(comp_id) + "axela_service_ticket_dept on ticket_dept_id = faqservice_ticket_dept_id"
					+ " where faqservice_id = " + service_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					service_name = crs.getString("faqservice_name");
					ticket_ticket_dept_id = crs.getString("faqservice_ticket_dept_id");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Service!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket_faq_service"
						+ " SET"
						+ " faqservice_ticket_dept_id = " + ticket_ticket_dept_id + ","
						+ " faqservice_name = '" + service_name + "'"
						+ " where faqservice_id = " + service_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT faqservice_ticket_dept_id from " + compdb(comp_id) + "axela_service_ticket_faq_service"
				+ " where faqservice_ticket_dept_id = " + ticket_ticket_dept_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Category is associated with a Department!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_service_ticket_faq_service"
						+ " where faqservice_id = " + service_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateDepartment() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select ticket_dept_id, ticket_dept_name"
					+ " from " + compdb(comp_id) + "axela_service_ticket_dept"
					+ " order by ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (!update.equals("yes") || entry_date.equals("")) {
				Str.append("<option value = 0> Select </option>");
			} else {
				Str.append("");
			}
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), ticket_ticket_dept_id));
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
