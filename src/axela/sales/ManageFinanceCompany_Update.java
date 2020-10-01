package axela.sales;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageFinanceCompany_Update extends Connect {

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
	public String fincomp_id = "0";
	public String fincomp_name = "";
	public String fincomp_active = "0";
	public String QueryString = "";
	public String fincomp_entry_id = "0";
	public String fincomp_entry_date = "";
	public String fincomp_modified_id = "0";
	public String fincomp_modified_date = "";
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
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				fincomp_id = CNumeric(PadQuotes(request.getParameter("fincomp_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						fincomp_name = "";
						fincomp_active = "1";
					} else {
						GetValues(request, response);
						fincomp_entry_id = emp_id;
						fincomp_entry_date = ToLongDate(kknow());
						// SOP("fincomp_entry_date------------" +
						// fincomp_entry_date);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managefinancecompany-list.jsp?fincomp_id=" + fincomp_id + "&msg= FinanceCompany Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Finance Company".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Finance Company".equals(deleteB)) {
						GetValues(request, response);
						fincomp_modified_id = emp_id;
						fincomp_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managefinancecompany-list.jsp?fincomp_id=" + fincomp_id + "&msg=Finance Company Updated Successfully!"));
						}
					} else if ("Delete Finance Company".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managefinancecompany-list.jsp?msg=Finance Company Deleted Successfully!"));
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
		fincomp_name = PadQuotes(request.getParameter("txt_fincomp_name"));
		fincomp_active = CheckBoxValue(PadQuotes(request.getParameter("chk_fincomp_active")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (fincomp_name.equals("")) {
			msg = msg + "<br>Enter Finance Company!";
		}
		try {
			if (!fincomp_name.equals("")) {
				StrSql = "Select fincomp_name from " + compdb(comp_id) + "axela_finance_comp where fincomp_name = '" + fincomp_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and fincomp_id != " + fincomp_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Finance Company Found! ";
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
				fincomp_id = ExecuteQuery("SELECT (COALESCE(MAX(fincomp_id),0)+1) from " + compdb(comp_id) + "axela_finance_comp");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_finance_comp"
						+ " (fincomp_id,"
						+ " fincomp_name,"
						+ " fincomp_active,"
						+ " fincomp_entry_id,"
						+ " fincomp_entry_date)"
						+ " VALUES"
						+ " (" + fincomp_id + ","
						+ " '" + fincomp_name + "',"
						+ " '" + fincomp_active + "',"
						+ " " + fincomp_entry_id + ","
						+ " '" + fincomp_entry_date + "')";
				// SOP("in--------" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select * from " + compdb(comp_id) + "axela_finance_comp where fincomp_id = " + fincomp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					fincomp_name = crs.getString("fincomp_name");
					fincomp_active = crs.getString("fincomp_active");
					fincomp_entry_id = crs.getString("fincomp_entry_id");
					if (!fincomp_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(fincomp_entry_id));
						entry_date = strToLongDate(crs.getString("fincomp_entry_date"));
					}
					fincomp_modified_id = crs.getString("fincomp_modified_id");
					if (!fincomp_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(fincomp_modified_id));
						modified_date = strToLongDate(crs.getString("fincomp_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Finance Company!"));
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_finance_comp"
						+ " SET"
						+ " fincomp_name = '" + fincomp_name + "',"
						+ " fincomp_active = '" + fincomp_active + "',"
						+ " fincomp_modified_id = " + fincomp_modified_id + ","
						+ " fincomp_modified_date = '" + fincomp_modified_date + "'"
						+ " where fincomp_id = " + fincomp_id + "";
				// SOP("33---" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		// StrSql = "SELECT enquiry_fincomp_id FROM " + compdb(comp_id) +
		// "axela_sales_enquiry where enquiry_fincomp_id = " + fincomp_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>FinanceCompany is Associated with Enquiry!";
		// }
		// StrSql = "SELECT lead_fincomp_id FROM " + compdb(comp_id) +
		// "axela_sales_lead where lead_fincomp_id = " + fincomp_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>FinanceCompany is Associated with Lead!";
		// }
		// StrSql = "SELECT customer_fincomp_id FROM " + compdb(comp_id) +
		// "axela_customer where customer_fincomp_id = " + fincomp_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>FinanceCompany is Associated with Customer!";
		// }
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) +
						"axela_finance_comp where fincomp_id =" + fincomp_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new
						Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
