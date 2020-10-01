// Bhagwan Singh (3rd Aug 2013)
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageLostCase3_Update extends Connect {

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
	public String lostcase3_id = "0";
	public String lostcase3_name = "";
	public String lostcase3_lostcase2_id = "0";
	public String lostcase3_entry_id = "0";
	public String lostcase3_entry_by = "";
	public String lostcase3_entry_date = "";
	public String lostcase3_modified_id = "0";
	public String lostcase3_modified_by = "";
	public String lostcase3_modified_date = "";
	public String QueryString = "";

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
				lostcase3_id = CNumeric(PadQuotes(request.getParameter("lostcase3_id")));
				lostcase3_lostcase2_id = CNumeric(PadQuotes(request.getParameter("lostcase2_id")));
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
						lostcase3_entry_id = emp_id;
						lostcase3_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase3.jsp?lostcase3_id=" + lostcase3_id + "&msg=Lost Case 3 Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Lost Case 3".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Lost Case 3".equals(deleteB)) {
						GetValues(request, response);
						lostcase3_modified_id = emp_id;
						lostcase3_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase3.jsp?lostcase3_id=" + lostcase3_id + "&msg=Lost Case 3 Updated Successfully!"));
						}
					}
					if ("Delete Lost Case 3".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase3.jsp?msg=Lost Case 3 Deleted Successfully!"));
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
		lostcase3_name = PadQuotes(request.getParameter("txt_lostcase3_name"));
		lostcase3_lostcase2_id = PadQuotes(request.getParameter("dr_lostcase3_lostcase2_id"));
		lostcase3_entry_by = PadQuotes(request.getParameter("entry_by"));
		lostcase3_modified_by = PadQuotes(request.getParameter("modified_by"));
		lostcase3_entry_date = PadQuotes(request.getParameter("entry_date"));
		lostcase3_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (lostcase3_lostcase2_id.equals("0")) {
			msg = "<br>Select Lost Case  2!";
		}
		if (lostcase3_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			StrSql = "SELECT lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_name = '" + lostcase3_name + "'"
					+ " AND lostcase3_lostcase2_id = " + lostcase3_lostcase2_id + ""
					+ " AND lostcase3_id != " + lostcase3_id + "";

			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Name Found! ";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				lostcase3_id = ExecuteQuery("SELECT COALESCE(MAX(lostcase3_id),0)+1 FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
						+ " (lostcase3_id,"
						+ " lostcase3_name,"
						+ " lostcase3_lostcase2_id,"
						+ " lostcase3_entry_id,"
						+ " lostcase3_entry_date)"
						+ " VALUES("
						+ " " + lostcase3_id + ","
						+ " '" + lostcase3_name + "',"
						+ " " + lostcase3_lostcase2_id + ","
						+ " " + lostcase3_entry_id + ","
						+ " '" + lostcase3_entry_date + "')";
				// lostcase3_id = UpdateQueryReturnID(StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT lostcase3_name, lostcase3_lostcase2_id, lostcase3_entry_id,"
					+ " lostcase3_entry_date, lostcase3_modified_id, lostcase3_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " WHERE lostcase3_id = " + lostcase3_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					lostcase3_name = crs.getString("lostcase3_name");
					lostcase3_lostcase2_id = crs.getString("lostcase3_lostcase2_id");
					lostcase3_entry_id = crs.getString("lostcase3_entry_id");
					if (!lostcase3_entry_id.equals("")) {
						lostcase3_entry_by = Exename(comp_id, Integer.parseInt(lostcase3_entry_id));
					}
					lostcase3_entry_date = strToLongDate(crs.getString("lostcase3_entry_date"));
					lostcase3_modified_id = crs.getString("lostcase3_modified_id");
					if (!lostcase3_modified_id.equals("")) {
						lostcase3_modified_by = Exename(comp_id, Integer.parseInt(lostcase3_modified_id));
					}
					lostcase3_modified_date = strToLongDate(crs.getString("lostcase3_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Evaluation Details!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
						+ " SET"
						+ " lostcase3_name = '" + lostcase3_name + "',"
						+ " lostcase3_lostcase2_id = " + lostcase3_lostcase2_id + ","
						+ " lostcase3_modified_id = " + lostcase3_modified_id + ","
						+ " lostcase3_modified_date  = '" + lostcase3_modified_date + "'"
						+ " WHERE lostcase3_id = " + lostcase3_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_lostcase3_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_lostcase3_id = " + lostcase3_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Lost Case 3 is Associated with Opportunity!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
						+ " WHERE lostcase3_id = " + lostcase3_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateLostCase2() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase2_id, CONCAT(lostcase1_name, ' - ', lostcase2_name) as lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = lostcase2_lostcase1_id"
					+ " ORDER BY lostcase1_name, lostcase2_name";
			// SOP("==="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase2_id"));
				Str.append(StrSelectdrop(crs.getString("lostcase2_id"), lostcase3_lostcase2_id));
				Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
