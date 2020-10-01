// Bhagwan Singh (3rd Aug 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedLostCase3_Update extends Connect {

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
	public String preownedlostcase3_id = "0";
	public String preownedlostcase3_name = "";
	public String preownedlostcase3_lostcase2_id = "0";
	public String preownedlostcase3_entry_id = "0";
	public String preownedlostcase3_entry_by = "";
	public String preownedlostcase3_entry_date = "";
	public String preownedlostcase3_modified_id = "0";
	public String preownedlostcase3_modified_by = "";
	public String preownedlostcase3_modified_date = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				preownedlostcase3_id = CNumeric(PadQuotes(request.getParameter("preownedlostcase3_id")));
				preownedlostcase3_lostcase2_id = CNumeric(PadQuotes(request.getParameter("preownedlostcase2_id")));
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
						preownedlostcase3_entry_id = emp_id;
						preownedlostcase3_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase3.jsp?preownedlostcase3_id=" + preownedlostcase3_id
									+ "&msg=Pre-Owned Lost Case 3 Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre Owned Lost Case 3".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre Owned Lost Case 3".equals(deleteB)) {
						GetValues(request, response);
						preownedlostcase3_modified_id = emp_id;
						preownedlostcase3_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase3.jsp?preownedlostcase3_id=" + preownedlostcase3_id
									+ "&msg=Pre-Owned Lost Case 3 Updated Successfully!"));
						}
					}
					if ("Delete Pre Owned Lost Case 3".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase3.jsp?msg=Pre-Owned Lost Case 3 Deleted Successfully!"));
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
		preownedlostcase3_name = PadQuotes(request.getParameter("txt_preownedlostcase3_name"));
		preownedlostcase3_lostcase2_id = PadQuotes(request.getParameter("dr_preownedlostcase3_lostcase2_id"));
		preownedlostcase3_entry_by = PadQuotes(request.getParameter("entry_by"));
		preownedlostcase3_modified_by = PadQuotes(request.getParameter("modified_by"));
		preownedlostcase3_entry_date = PadQuotes(request.getParameter("entry_date"));
		preownedlostcase3_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (preownedlostcase3_lostcase2_id.equals("0")) {
			msg = "<br>Select Pre Owned Lost Case  2!";
		}
		if (preownedlostcase3_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			StrSql = "SELECT preownedlostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase3"
					+ " WHERE preownedlostcase3_name = '" + preownedlostcase3_name + "'"
					+ " AND preownedlostcase3_lostcase2_id = " + preownedlostcase3_lostcase2_id + "";
			if (update.equals("yes")) {
				StrSql += " AND preownedlostcase3_id != " + preownedlostcase3_id + "";
			}

			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Name Found! ";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				preownedlostcase3_id = ExecuteQuery("SELECT COALESCE(MAX(preownedlostcase3_id),0)+1 FROM " + compdb(comp_id) + "axela_preowned_lostcase3");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_lostcase3"
						+ " (preownedlostcase3_id,"
						+ " preownedlostcase3_name,"
						+ " preownedlostcase3_lostcase2_id,"
						+ " preownedlostcase3_entry_id,"
						+ " preownedlostcase3_entry_date)"
						+ " VALUES("
						+ " " + preownedlostcase3_id + ","
						+ " '" + preownedlostcase3_name + "',"
						+ " " + preownedlostcase3_lostcase2_id + ","
						+ " " + preownedlostcase3_entry_id + ","
						+ " '" + preownedlostcase3_entry_date + "')";
				// preownedlostcase3_id = UpdateQueryReturnID(StrSql);
				// SOP(Str);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT preownedlostcase3_name, preownedlostcase3_lostcase2_id, preownedlostcase3_entry_id,"
					+ " preownedlostcase3_entry_date, preownedlostcase3_modified_id, preownedlostcase3_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase3"
					+ " WHERE preownedlostcase3_id = " + preownedlostcase3_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preownedlostcase3_name = crs.getString("preownedlostcase3_name");
					preownedlostcase3_lostcase2_id = crs.getString("preownedlostcase3_lostcase2_id");
					preownedlostcase3_entry_id = crs.getString("preownedlostcase3_entry_id");
					if (!preownedlostcase3_entry_id.equals("")) {
						preownedlostcase3_entry_by = Exename(comp_id, Integer.parseInt(preownedlostcase3_entry_id));
					}
					preownedlostcase3_entry_date = strToLongDate(crs.getString("preownedlostcase3_entry_date"));
					preownedlostcase3_modified_id = crs.getString("preownedlostcase3_modified_id");
					if (!preownedlostcase3_modified_id.equals("")) {
						preownedlostcase3_modified_by = Exename(comp_id, Integer.parseInt(preownedlostcase3_modified_id));
					}
					preownedlostcase3_modified_date = strToLongDate(crs.getString("preownedlostcase3_modified_date"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_lostcase3"
						+ " SET"
						+ " preownedlostcase3_name = '" + preownedlostcase3_name + "',"
						+ " preownedlostcase3_lostcase2_id = " + preownedlostcase3_lostcase2_id + ","
						+ " preownedlostcase3_modified_id = " + preownedlostcase3_modified_id + ","
						+ " preownedlostcase3_modified_date  = '" + preownedlostcase3_modified_date + "'"
						+ " WHERE preownedlostcase3_id = " + preownedlostcase3_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT preowned_preownedlostcase3_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned"
				+ " WHERE preowned_preownedlostcase3_id = " + preownedlostcase3_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Pre Owned Lost Case 3 is Associated with PreOwned!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_lostcase3"
						+ " WHERE preownedlostcase3_id = " + preownedlostcase3_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateLostCase2() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlostcase2_id, CONCAT(preownedlostcase1_name, ' - ', preownedlostcase2_name) as preownedlostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_lostcase1 ON preownedlostcase1_id = preownedlostcase2_lostcase1_id"
					+ " ORDER BY preownedlostcase1_name, preownedlostcase2_name";
			// SOP("==="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlostcase2_id"));
				Str.append(StrSelectdrop(crs.getString("preownedlostcase2_id"), preownedlostcase3_lostcase2_id));
				Str.append(">").append(crs.getString("preownedlostcase2_name")).append("</option>\n");
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
