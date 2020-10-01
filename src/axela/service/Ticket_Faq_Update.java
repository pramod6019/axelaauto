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

public class Ticket_Faq_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String ticketfaq_id = "0";
	public String ticketfaq_question = "";
	public String ticketfaq_answer = "";
	public String ticketfaq_faqservice_id = "0";
	public String faqservice_ticket_dept_id = "0";
	public String ticketfaq_active = "";
	public String ticketfaq_entry_id = "0";
	public String ticketfaq_entry_date = "";
	public String ticketfaq_modified_id = "0";
	public String ticketfaq_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "";
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
				QueryString = PadQuotes(request.getQueryString());
				ticketfaq_id = CNumeric(PadQuotes(request.getParameter("ticketfaq_id")));
				// PopulateConfigDetails();

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						ticketfaq_active = "1";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_faq_add", request).equals("1")) {
							ticketfaq_entry_id = emp_id;
							ticketfaq_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-faq-list.jsp?msg=FAQ added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete FAQ".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete FAQ".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_faq_edit", request).equals("1")) {
							ticketfaq_modified_id = emp_id;
							ticketfaq_modified_date = ToLongDate(kknow());
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-faq-list.jsp?ticketfaq_id=" + ticketfaq_id + "&msg=FAQ updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete FAQ".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_faq_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("ticket-faq-list.jsp?msg=FAQ deleted successfully!"));
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

		faqservice_ticket_dept_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_dept")));
		ticketfaq_id = CNumeric(PadQuotes(request.getParameter("ticketfaq_id")));
		ticketfaq_faqservice_id = CNumeric(PadQuotes(request.getParameter("dr_cat")));
		ticketfaq_question = PadQuotes(request.getParameter("txt_ticketfaq_question"));
		ticketfaq_answer = PadQuotes(request.getParameter("txt_ticketfaq_answer"));
		ticketfaq_active = PadQuotes(request.getParameter("chk_active"));
		if (ticketfaq_active.equals("on")) {
			ticketfaq_active = "1";
		} else {
			ticketfaq_active = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (faqservice_ticket_dept_id.equals("0")) {
			msg = msg + "<br>Select Deparment!";
		}
		if (ticketfaq_faqservice_id.equals("0")) {
			msg = msg + "<br>Select Category!";
		}
		if (ticketfaq_question.equals("")) {
			msg = msg + "<br>Enter Question!";
		}
		if (!ticketfaq_question.equals("") && !ticketfaq_faqservice_id.equals("0")) {
			try {
				StrSql = "select ticketfaq_question from " + compdb(comp_id) + "axela_service_ticket_faq where ticketfaq_question = '" + ticketfaq_question + "' ";
				if (update.equals("yes")) {
					StrSql = StrSql + " and ticketfaq_id!=" + ticketfaq_id;
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Question found!";
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (ticketfaq_answer.equals("")) {
			msg = msg + "<br>Enter Answer!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "insert into " + compdb(comp_id) + "axela_service_ticket_faq"
						+ "( "
						+ "ticketfaq_faqservice_id,"
						+ "ticketfaq_question,"
						+ "ticketfaq_answer,"
						+ "ticketfaq_active,"
						+ "ticketfaq_entry_id ,"
						+ "ticketfaq_entry_date ,"
						+ "ticketfaq_modified_id ,"
						+ "ticketfaq_modified_date ) "
						+ "values	"
						+ "( "
						+ "" + ticketfaq_faqservice_id + ","
						+ "'" + ticketfaq_question + "',"
						+ "'" + ticketfaq_answer + "',"
						+ "'" + ticketfaq_active + "',"
						+ "" + ticketfaq_entry_id + ", "
						+ "" + ticketfaq_entry_date + ", "
						+ "0 ,"
						+ "''"
						+ " )";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "select " + compdb(comp_id) + "axela_service_ticket_faq.*, faqservice_ticket_dept_id "
					+ " from " + compdb(comp_id) + "axela_service_ticket_faq "
					+ " inner join " + compdb(comp_id) + "axela_service_ticket_faq_service on faqservice_id = ticketfaq_faqservice_id "
					+ " where ticketfaq_id = " + ticketfaq_id + "";
			// SOP("StrSql"+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticketfaq_faqservice_id = crs.getString("ticketfaq_faqservice_id");
					faqservice_ticket_dept_id = crs.getString("faqservice_ticket_dept_id");
					ticketfaq_question = crs.getString("ticketfaq_question");
					ticketfaq_answer = crs.getString("ticketfaq_answer");
					ticketfaq_active = crs.getString("ticketfaq_active");
					ticketfaq_entry_id = crs.getString("ticketfaq_entry_id");
					ticketfaq_entry_date = crs.getString("ticketfaq_entry_date");
					ticketfaq_modified_id = crs.getString("ticketfaq_modified_id");
					if (!crs.getString("ticketfaq_entry_id").equals("0")) {
						entry_by = Exename(comp_id, crs.getInt("ticketfaq_entry_id"));
						entry_date = strToLongDate(crs.getString("ticketfaq_entry_date"));
					}
					if (!ticketfaq_modified_id.equals("0")) {
						modified_by = Exename(comp_id, crs.getInt("ticketfaq_modified_id"));
						modified_date = strToLongDate(crs.getString("ticketfaq_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Faq Ticket!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_ticket_faq"
						+ " SET "
						+ "ticketfaq_faqservice_id = " + ticketfaq_faqservice_id + ","
						+ "ticketfaq_question = '" + ticketfaq_question + "',"
						+ "ticketfaq_answer = '" + ticketfaq_answer + "',"
						+ "ticketfaq_active = '" + ticketfaq_active + "',"
						+ "ticketfaq_modified_id= " + ticketfaq_modified_id + ", "
						+ "ticketfaq_modified_date= '" + ticketfaq_modified_date + "' "
						+ "where ticketfaq_id = " + ticketfaq_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT ticketfaq_faqservice_id from " + compdb(comp_id) + "axela_service_ticket_faq where ticketfaq_faqservice_id = " + ticketfaq_faqservice_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>FAQ is associated with a Category!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_service_ticket_faq where ticketfaq_id =" + ticketfaq_id + "";
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
			Str.append("<option value=0>Select</option>" + "");
			StrSql = "SELECT ticket_dept_id, ticket_dept_name "
					+ " from " + compdb(comp_id) + "axela_service_ticket_dept "
					+ " order by ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), faqservice_ticket_dept_id));
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

	public String PopulateCat() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT faqservice_id, faqservice_name "
					+ " from " + compdb(comp_id) + "axela_service_ticket_faq_service "
					+ " where faqservice_ticket_dept_id = " + faqservice_ticket_dept_id
					+ " group by faqservice_id order by faqservice_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_cat id=dr_cat class='form-control' >"
					+ "<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("faqservice_id")).append("");
				Str.append(Selectdrop(crs.getInt("faqservice_id"), ticketfaq_faqservice_id));
				Str.append(">").append(crs.getString("faqservice_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		Str.append("</select>");
		return Str.toString();
	}
	// public void PopulateConfigDetails() {
	//
	// StrSql = " SELECT emp_role_id "
	// + " from " + compdb(comp_id) + "axela_config, " + compdb(comp_id) +
	// "axela_comp, " + compdb(comp_id) + "axela_emp "
	// + " where 1 = 1 and emp_id = " + emp_id;
	// CachedRowSet crs = processQuery(StrSql, 0);
	// // SOP("StrSql--" + StrSql);
	// try {
	// while (crs.next()) {
	// emp_role_id = crs.getString("emp_role_id");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// }
	// }
}
