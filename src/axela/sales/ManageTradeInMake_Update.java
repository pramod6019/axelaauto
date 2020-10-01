package axela.sales;
//Shivaprasad 7July2014

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTradeInMake_Update extends Connect {

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
	public String tradeinmake_id = "0";
	public String tradeinmake_name = "";
	public String QueryString = "";
	public String tradeinmake_entry_id = "0";
	public String tradeinmake_entry_date = "";
	public String tradeinmake_modified_id = "0";
	public String tradeinmake_modified_date = "";
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
				tradeinmake_id = CNumeric(PadQuotes(request.getParameter("tradeinmake_id")));
				QueryString = PadQuotes(request.getQueryString());
				// SOP("deleteB = " + deleteB);

				if (add.equals("yes")) {

				} else if (update.equals("yes")) {

				}

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
						tradeinmake_name = "";
					} else {
						GetValues(request, response);
						tradeinmake_entry_id = emp_id;
						tradeinmake_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetradeinmake.jsp?msg=Make Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Make".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Make".equals(deleteB)) {
						GetValues(request, response);
						tradeinmake_modified_id = emp_id;
						tradeinmake_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetradeinmake.jsp?tradeinmake_id=" + tradeinmake_id + "&msg=Make Updated Successfully!"));
						}
					} else if ("Delete Make".equals(deleteB)) {
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetradeinmake.jsp?msg=Make Deleted Successfully!"));
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
		tradeinmake_name = PadQuotes(request.getParameter("txt_tradeinmake_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (tradeinmake_name.equals("")) {
			msg = msg + "<br>Enter Make!";
		}
		try {
			if (!tradeinmake_name.equals("")) {
				StrSql = "SELECT tradeinmake_name FROM " + compdb(comp_id) + "axela_sales_tradein_make WHERE tradeinmake_name = '" + tradeinmake_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and tradeinmake_id != " + tradeinmake_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Make Found! ";
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
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_tradein_make"
						+ " (tradeinmake_name ,"
						+ " tradeinmake_entry_id,"
						+ " tradeinmake_entry_date)"
						+ " VALUES"
						+ " ('" + tradeinmake_name + "',"
						+ " " + tradeinmake_entry_id + ","
						+ " '" + tradeinmake_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT tradeinmake_name, tradeinmake_entry_id, tradeinmake_entry_date, tradeinmake_modified_id, tradeinmake_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_sales_tradein_make "
					+ " WHERE tradeinmake_id=" + tradeinmake_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					tradeinmake_name = crs.getString("tradeinmake_name");
					tradeinmake_entry_id = crs.getString("tradeinmake_entry_id");
					if (!tradeinmake_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(tradeinmake_entry_id));
						entry_date = strToLongDate(crs.getString("tradeinmake_entry_date"));
					}
					tradeinmake_modified_id = crs.getString("tradeinmake_modified_id");
					if (!tradeinmake_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(tradeinmake_modified_id));
						modified_date = strToLongDate(crs.getString("tradeinmake_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Make!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_tradein_make"
						+ " SET"
						+ " tradeinmake_name = '" + tradeinmake_name + "',"
						+ " tradeinmake_modified_id = " + tradeinmake_modified_id + ","
						+ " tradeinmake_modified_date = '" + tradeinmake_modified_date + "'"
						+ " WHERE tradeinmake_id = " + tradeinmake_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_tradeinmake_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_tradeinmake_id = " + tradeinmake_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Make is Associated with Enquiry!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE from " + compdb(comp_id) + "axela_sales_tradein_make WHERE tradeinmake_id = " + tradeinmake_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
