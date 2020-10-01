package axela.preowned;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedLostCase1_Update extends Connect {

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
	public String preownedlostcase1_id = "0";
	public String preownedlostcase1_name = "";
	public String QueryString = "";
	public String preownedlostcase1_entry_id = "0";
	public String preownedlostcase1_entry_date = "";
	public String preownedlostcase1_modified_id = "0";
	public String preownedlostcase1_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				preownedlostcase1_id = CNumeric(PadQuotes(request.getParameter("preownedlostcase1_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						preownedlostcase1_name = "";
					} else {
						GetValues(request, response);
						preownedlostcase1_entry_id = emp_id;
						preownedlostcase1_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase1.jsp?preownedlostcase1_id=" + preownedlostcase1_id
									+ "&msg=Pre Owned Lost Case 1 Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre Owned Lost Case 1".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre Owned Lost Case 1".equals(deleteB)) {
						GetValues(request, response);
						preownedlostcase1_modified_id = emp_id;
						preownedlostcase1_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase1.jsp?preownedlostcase1_id=" + preownedlostcase1_id
									+ "&msg=Pre Owned Lost Case  1 Updated Successfully!"));
						}
					} else if ("Delete Pre Owned Lost Case 1".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedlostcase1.jsp?msg=Pre Owned Lost Case 1 Deleted Successfully!"));
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
		preownedlostcase1_name = PadQuotes(request.getParameter("txt_preownedlostcase1_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (preownedlostcase1_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			StrSql = "SELECT preownedlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase1"
					+ " WHERE preownedlostcase1_name = '" + preownedlostcase1_name + "'";
			if (update.equals("yes")) {
				StrSql += " AND preownedlostcase1_id != " + preownedlostcase1_id + "";
			}
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Name Found!";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				preownedlostcase1_id = ExecuteQuery("SELECT (COALESCE(MAX(preownedlostcase1_id),0)+1) FROM " + compdb(comp_id) + "axela_preowned_lostcase1");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_lostcase1"
						+ " (preownedlostcase1_id,"
						+ " preownedlostcase1_name,"
						+ " preownedlostcase1_entry_id,"
						+ " preownedlostcase1_entry_date)"
						+ " VALUES"
						+ " (" + preownedlostcase1_id + ","
						+ " '" + preownedlostcase1_name + "',"
						+ " " + preownedlostcase1_entry_id + ","
						+ " '" + preownedlostcase1_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_preowned_lostcase1"
					+ " WHERE preownedlostcase1_id = " + preownedlostcase1_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preownedlostcase1_name = crs.getString("preownedlostcase1_name");
					preownedlostcase1_entry_id = crs.getString("preownedlostcase1_entry_id");
					if (!preownedlostcase1_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(preownedlostcase1_entry_id));

						entry_date = strToLongDate(crs.getString("preownedlostcase1_entry_date"));
					}
					preownedlostcase1_modified_id = crs.getString("preownedlostcase1_modified_id");
					if (!preownedlostcase1_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(preownedlostcase1_modified_id));
						modified_date = strToLongDate(crs.getString("preownedlostcase1_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Pre Owned Lost Case  1!"));
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_preowned_lostcase1"
						+ " SET"
						+ " preownedlostcase1_name = '" + preownedlostcase1_name + "',"
						+ " preownedlostcase1_modified_id = " + preownedlostcase1_modified_id + ","
						+ " preownedlostcase1_modified_date = '" + preownedlostcase1_modified_date + "' "
						+ " WHERE preownedlostcase1_id = " + preownedlostcase1_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		StrSql = "SELECT preowned_preownedlostcase1_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned"
				+ " WHERE preowned_preownedlostcase1_id = " + preownedlostcase1_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Pre Owned Lost Case  1 is Associated with PreOwned!";
		}
		StrSql = "SELECT preownedlostcase2_lostcase1_id"
				+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
				+ " WHERE preownedlostcase2_lostcase1_id = " + preownedlostcase1_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Pre Owned Lost Case 1 is Associated with Pre Owned Lost Case 2!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_lostcase1"
						+ " WHERE preownedlostcase1_id = " + preownedlostcase1_id + "";
				updateQuery(StrSql);
				// SOP(Str);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
