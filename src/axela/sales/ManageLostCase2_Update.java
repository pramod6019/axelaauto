package axela.sales;
/*
 * @author Dilip Kumar (10th July 2013)
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageLostCase2_Update extends Connect {

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
	public String lostcase2_id = "0";
	public String lostcase2_lostcase1_id = "0";
	// public String evalhead_id = "0";
	public String lostcase2_name = "";
	public String lostcase2_entry_id = "0";
	public String lostcase2_entry_by = "";
	public String lostcase2_entry_date = "";
	public String lostcase2_modified_id = "0";
	public String lostcase2_modified_by = "";
	public String lostcase2_modified_date = "";
	public String entry_by = "", modified_by = "";
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
				lostcase2_id = CNumeric(PadQuotes(request.getParameter("lostcase2_id")));
				lostcase2_lostcase1_id = PadQuotes(request.getParameter("lostcase1_id"));
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
						lostcase2_entry_id = emp_id;
						lostcase2_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase2.jsp?lostcase2_id=" + lostcase2_id + "&msg=Lost Case 2 Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Lost Case 2".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Lost Case 2".equals(deleteB)) {
						GetValues(request, response);

						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase2.jsp?lostcase2_id=" + lostcase2_id + "&msg=Lost Case 2 Updated Successfully!"));
						}
					}

					if ("Delete Lost Case 2".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managelostcase2.jsp?msg=Lost Case 2 Deleted Successfully!"));
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
		lostcase2_lostcase1_id = PadQuotes(request.getParameter("dr_lostcase2_lostcase1_id"));
		lostcase2_name = PadQuotes(request.getParameter("txt_lostcase2_name"));
		lostcase2_entry_by = PadQuotes(request.getParameter("entry_by"));
		lostcase2_modified_by = PadQuotes(request.getParameter("modified_by"));
		lostcase2_entry_date = PadQuotes(request.getParameter("entry_date"));
		lostcase2_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (lostcase2_lostcase1_id.equals("0")) {
			msg = "<br>Select Lost Case1!";
		}
		if (lostcase2_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			StrSql = "SELECT lostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " WHERE lostcase2_name = '" + lostcase2_name + "'"
					+ " AND lostcase2_lostcase1_id = " + lostcase2_lostcase1_id + ""
					+ " AND lostcase2_id != " + lostcase2_id + "";

			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Name Found! ";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				lostcase2_id = ExecuteQuery("SELECT COALESCE(MAX(lostcase2_id), 0) + 1"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
						+ "(lostcase2_id,"
						+ " lostcase2_name,"
						+ " lostcase2_lostcase1_id,"
						+ " lostcase2_entry_id,"
						+ " lostcase2_entry_date)"
						+ " VALUES("
						+ " " + lostcase2_id + ","
						+ " '" + lostcase2_name + "',"
						+ " " + lostcase2_lostcase1_id + ","
						+ " " + lostcase2_entry_id + ","
						+ " '" + lostcase2_entry_date + "')";

				// lostcase2_id = UpdateQueryReturnID(StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT lostcase1_id, lostcase2_name, lostcase2_entry_id,"
					+ " lostcase2_entry_date, lostcase2_modified_id, lostcase2_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = lostcase2_lostcase1_id"
					+ " WHERE lostcase2_id = " + lostcase2_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					lostcase2_lostcase1_id = crs.getString("lostcase1_id");
					lostcase2_name = crs.getString("lostcase2_name");
					lostcase2_entry_id = crs.getString("lostcase2_entry_id");
					if (!lostcase2_entry_id.equals("")) {
						lostcase2_entry_by = Exename(comp_id, Integer.parseInt(lostcase2_entry_id));
					}
					lostcase2_entry_date = strToLongDate(crs.getString("lostcase2_entry_date"));
					lostcase2_modified_id = crs.getString("lostcase2_modified_id");
					if (!lostcase2_modified_id.equals("")) {
						lostcase2_modified_by = Exename(comp_id, Integer.parseInt(lostcase2_modified_id));
					}
					lostcase2_modified_date = strToLongDate(crs.getString("lostcase2_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Lost Case2!"));
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
				lostcase2_modified_id = emp_id;
				lostcase2_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
						+ " SET"
						+ " lostcase2_lostcase1_id = " + lostcase2_lostcase1_id + ","
						+ " lostcase2_name = '" + lostcase2_name + "',"
						+ " lostcase2_modified_id = " + lostcase2_modified_id + ","
						+ " lostcase2_modified_date = '" + lostcase2_modified_date + "'"
						+ " WHERE lostcase2_id = " + lostcase2_id + "";
				// SOP("11=="+StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_lostcase2_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_lostcase2_id = " + lostcase2_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Lost Case 2 is Associated with Opportunity!";
		}

		StrSql = "SELECT lostcase3_lostcase2_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
				+ " WHERE lostcase3_lostcase2_id = " + lostcase2_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Lost Case 2 is associated with Lost Case 3!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
						+ " WHERE lostcase2_id = " + lostcase2_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateLostCase1() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT lostcase1_id, lostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " ORDER BY lostcase1_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("lostcase1_id"));
				Str.append(StrSelectdrop(crs.getString("lostcase1_id"), lostcase2_lostcase1_id));
				Str.append(">").append(crs.getString("lostcase1_name")).append("</option>\n");
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
