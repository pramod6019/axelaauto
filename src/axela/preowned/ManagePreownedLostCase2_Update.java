package axela.preowned;
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

public class ManagePreownedLostCase2_Update extends Connect {

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
	public String preownedlostcase2_id = "0";
	public String preownedlostcase2_lostcase1_id = "0";
	// public String evalhead_id = "0";
	public String preownedlostcase2_name = "";
	public String preownedlostcase2_entry_id = "0";
	public String preownedlostcase2_entry_by = "";
	public String preownedlostcase2_entry_date = "";
	public String preownedlostcase2_modified_id = "0";
	public String preownedlostcase2_modified_by = "";
	public String preownedlostcase2_modified_date = "";
	public String entry_by = "", modified_by = "";
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
				preownedlostcase2_lostcase1_id = CNumeric(PadQuotes(request.getParameter("preownedlostcase1_id")));
				preownedlostcase2_id = CNumeric(PadQuotes(request.getParameter("preownedlostcase2_id")));
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
						preownedlostcase2_entry_id = emp_id;
						preownedlostcase2_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase2.jsp?preownedlostcase2_id=" + preownedlostcase2_id + "&msg=Lost Case 2 Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre Owned Lost Case 2".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre Owned Lost Case 2".equals(deleteB)) {
						GetValues(request, response);

						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase2.jsp?preownedlostcase2_id=" + preownedlostcase2_id + "&msg=Lost Case 2 Updated Successfully!"));
						}
					}

					if ("Delete Pre Owned Lost Case 2".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase2.jsp?msg=Lost Case 2 Deleted Successfully!"));
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
		preownedlostcase2_lostcase1_id = PadQuotes(request.getParameter("dr_preownedlostcase2_lostcase1_id"));
		preownedlostcase2_name = PadQuotes(request.getParameter("txt_preownedlostcase2_name"));
		preownedlostcase2_entry_by = PadQuotes(request.getParameter("entry_by"));
		preownedlostcase2_modified_by = PadQuotes(request.getParameter("modified_by"));
		preownedlostcase2_entry_date = PadQuotes(request.getParameter("entry_date"));
		preownedlostcase2_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		// SOP("================="+preownedlostcase2_id);
		// SOP("================="+preownedlostcase2_lostcase1_id);
		msg = "";
		if (preownedlostcase2_lostcase1_id.equals("0")) {
			msg = "<br>Select Lost Case1!";
		}
		if (preownedlostcase2_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			StrSql = "SELECT preownedlostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
					+ " WHERE preownedlostcase2_name = '" + preownedlostcase2_name + "'"
					+ " AND preownedlostcase2_lostcase1_id = " + preownedlostcase2_lostcase1_id + "";
			if (update.equals("yes")) {
				StrSql += " AND preownedlostcase2_id != " + preownedlostcase2_id + "";
			}
			// SOP("================="+StrSqlBreaker(StrSql));
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Name Found! ";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				preownedlostcase2_id = ExecuteQuery("SELECT COALESCE(MAX(preownedlostcase2_id), 0) + 1"
						+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase2");

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_lostcase2"
						+ "(preownedlostcase2_id,"
						+ " preownedlostcase2_name,"
						+ " preownedlostcase2_lostcase1_id,"
						+ " preownedlostcase2_entry_id,"
						+ " preownedlostcase2_entry_date)"
						+ " VALUES("
						+ " " + preownedlostcase2_id + ","
						+ " '" + preownedlostcase2_name + "',"
						+ " " + preownedlostcase2_lostcase1_id + ","
						+ " " + preownedlostcase2_entry_id + ","
						+ " '" + preownedlostcase2_entry_date + "')";

				// preownedlostcase2_id = UpdateQueryReturnID(StrSql);
				// SOP("sop========"+ StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT preownedlostcase1_id, preownedlostcase2_name, preownedlostcase2_entry_id,"
					+ " preownedlostcase2_entry_date, preownedlostcase2_modified_id, preownedlostcase2_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_lostcase1 ON preownedlostcase1_id = preownedlostcase2_lostcase1_id"
					+ " WHERE preownedlostcase2_id = " + preownedlostcase2_id + "";
			// SOP("StrSql-----p-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preownedlostcase2_lostcase1_id = crs.getString("preownedlostcase1_id");
					preownedlostcase2_name = crs.getString("preownedlostcase2_name");
					preownedlostcase2_entry_id = crs.getString("preownedlostcase2_entry_id");
					if (!preownedlostcase2_entry_id.equals("")) {
						preownedlostcase2_entry_by = Exename(comp_id, Integer.parseInt(preownedlostcase2_entry_id));
					}
					preownedlostcase2_entry_date = strToLongDate(crs.getString("preownedlostcase2_entry_date"));
					preownedlostcase2_modified_id = crs.getString("preownedlostcase2_modified_id");
					if (!preownedlostcase2_modified_id.equals("")) {
						preownedlostcase2_modified_by = Exename(comp_id, Integer.parseInt(preownedlostcase2_modified_id));
					}
					preownedlostcase2_modified_date = strToLongDate(crs.getString("preownedlostcase2_modified_date"));
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
				preownedlostcase2_modified_id = emp_id;
				preownedlostcase2_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_lostcase2"
						+ " SET"
						+ " preownedlostcase2_lostcase1_id = " + preownedlostcase2_lostcase1_id + ","
						+ " preownedlostcase2_name = '" + preownedlostcase2_name + "',"
						+ " preownedlostcase2_modified_id = " + preownedlostcase2_modified_id + ","
						+ " preownedlostcase2_modified_date = '" + preownedlostcase2_modified_date + "'"
						+ " WHERE preownedlostcase2_id = " + preownedlostcase2_id + "";
				// SOP("11=="+StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT preowned_preownedlostcase2_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned"
				+ " WHERE preowned_preownedlostcase2_id = " + preownedlostcase2_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Lost Case 2 is Associated with PreOwned!";
		}

		StrSql = "SELECT preownedlostcase3_lostcase2_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase3"
				+ " WHERE preownedlostcase3_lostcase2_id = " + preownedlostcase2_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Lost Case 2 is associated with Lost Case 3!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
						+ " WHERE preownedlostcase2_id = " + preownedlostcase2_id + "";
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
			StrSql = "SELECT preownedlostcase1_id, preownedlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase1"
					+ " ORDER BY preownedlostcase1_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlostcase1_id"));
				Str.append(StrSelectdrop(crs.getString("preownedlostcase1_id"), preownedlostcase2_lostcase1_id));
				Str.append(">").append(crs.getString("preownedlostcase1_name")).append("</option>\n");
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
