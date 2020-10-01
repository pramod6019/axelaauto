package axela.sales;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageLostCase1_Update extends Connect {

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
	public String lostcase1_id = "0";
	public String lostcase1_name = "";
	public String QueryString = "";
	public String lostcase1_entry_id = "0";
	public String lostcase1_entry_date = "";
	public String lostcase1_modified_id = "0";
	public String lostcase1_modified_date = "";
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
				lostcase1_id = CNumeric(PadQuotes(request.getParameter("lostcase1_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						lostcase1_name = "";
					} else {
						GetValues(request, response);
						lostcase1_entry_id = emp_id;
						lostcase1_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase1.jsp?lostcase1_id=" + lostcase1_id + "&msg=Lost Case 1 Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Lost Case 1".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Lost Case 1".equals(deleteB)) {
						GetValues(request, response);
						lostcase1_modified_id = emp_id;
						lostcase1_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase1.jsp?lostcase1_id=" + lostcase1_id + "&msg=Lost Case 1 Updated Successfully!"));
						}
					} else if ("Delete Lost Case 1".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase1.jsp?msg=Lost Case 1 Deleted Successfully!"));
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
		lostcase1_name = PadQuotes(request.getParameter("txt_lostcase1_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (lostcase1_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			StrSql = "SELECT lostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " WHERE lostcase1_name = '" + lostcase1_name + "'"
					+ " AND lostcase1_id != " + lostcase1_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Name Found!";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				lostcase1_id = ExecuteQuery("SELECT (COALESCE(MAX(lostcase1_id),0)+1) FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
						+ " (lostcase1_id,"
						+ " lostcase1_name,"
						+ " lostcase1_entry_id,"
						+ " lostcase1_entry_date)"
						+ " VALUES"
						+ " (" + lostcase1_id + ","
						+ " '" + lostcase1_name + "',"
						+ " " + lostcase1_entry_id + ","
						+ " '" + lostcase1_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " WHERE lostcase1_id = " + lostcase1_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					lostcase1_name = crs.getString("lostcase1_name");
					lostcase1_entry_id = crs.getString("lostcase1_entry_id");
					if (!lostcase1_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(lostcase1_entry_id));
						entry_date = strToLongDate(crs.getString("lostcase1_entry_date"));
					}
					lostcase1_modified_id = crs.getString("lostcase1_modified_id");
					if (!lostcase1_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(lostcase1_modified_id));
						modified_date = strToLongDate(crs.getString("lostcase1_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Lost Case 1!"));
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
						+ " SET"
						+ " lostcase1_name = '" + lostcase1_name + "',"
						+ " lostcase1_modified_id = " + lostcase1_modified_id + ","
						+ " lostcase1_modified_date = '" + lostcase1_modified_date + "' "
						+ " WHERE lostcase1_id = " + lostcase1_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		StrSql = "SELECT enquiry_lostcase1_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_lostcase1_id = " + lostcase1_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Lost Case 1 is Associated with Opportunity!";
		}
		StrSql = "SELECT lostcase2_lostcase1_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
				+ " WHERE lostcase2_lostcase1_id = " + lostcase1_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Lost Case 1 is Associated with Lost Case 2!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
						+ " WHERE lostcase1_id = " + lostcase1_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
