package axela.insurance;

/**
 * @author Dilip Kumar
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInsurPolicy_Update extends Connect {

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
	public String policytype_id = "0";
	public String policytype_name = "";
	public String policytype_entry_id = "0";
	public String entry_by = "";
	public String policytype_entry_date = "";
	public String policytype_modified_id = "0";
	public String modified_by = "";
	public String policytype_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
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
				policytype_id = CNumeric(PadQuotes(request.getParameter("policytype_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					if (policytype_id.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("index.jsp"));
					} else if (policytype_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Policy!"));
					}
				}

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						policytype_entry_id = emp_id;
						policytype_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurpolicy.jsp?policytype_id=" + policytype_id + "&msg=Policy added successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Policy".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Policy".equals(deleteB)) {
						GetValues(request, response);
						policytype_modified_id = emp_id;
						policytype_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurpolicy.jsp?policytype_id=" + policytype_id + "&msg=Policy updated successfully!"));
						}
					}
					if ("Delete Policy".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurpolicy.jsp?msg=Policy deleted successfully!"));
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
		policytype_name = PadQuotes(request.getParameter("txt_insurpolicy_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (policytype_name.equals("")) {
			msg = "<br>Enter Policy Name!";
		} else {
			StrSql = "SELECT policytype_name FROM " + compdb(comp_id) + "axela_insurance_policy_type"
					+ " WHERE policytype_name = '" + policytype_name + "'";
			if (update.equals("yes")) {
				StrSql = StrSql + " AND policytype_id != " + policytype_id + "";
			}
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Policy found!";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			policytype_id = ExecuteQuery("SELECT COALESCE(MAX(policytype_id),0)+1 AS policytype_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy_type");

			StrSql = "INSERT into " + compdb(comp_id) + "axela_insurance_policy_type"
					+ " (policytype_id,"
					+ " policytype_name,"
					+ " policytype_entry_id,"
					+ " policytype_entry_date)"
					+ " values"
					+ " (" + policytype_id + ","
					+ " '" + policytype_name + "',"
					+ " " + policytype_entry_id + ","
					+ " '" + policytype_entry_date + "')";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_insurance_policy_type"
					+ " WHERE policytype_id = " + policytype_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					policytype_name = crs.getString("policytype_name");
					policytype_entry_id = crs.getString("policytype_entry_id");
					if (!policytype_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(policytype_entry_id));
					}
					entry_date = strToLongDate(crs.getString("policytype_entry_date"));
					policytype_modified_id = crs.getString("policytype_modified_id");
					if (!policytype_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(policytype_modified_id));
					}
					modified_date = strToLongDate(crs.getString("policytype_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Policy!"));
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
			StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_policy_type"
					+ " SET"
					+ " policytype_name = '" + policytype_name + "',"
					+ " policytype_modified_id = " + policytype_modified_id + ","
					+ " policytype_modified_date = '" + policytype_modified_date + "'"
					+ " WHERE policytype_id = " + policytype_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT insurpolicy_policytype_id FROM " + compdb(comp_id) + "axela_insurance_policy"
				+ " where insurpolicy_policytype_id = " + policytype_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Insurance Policy is associated with Insurance!";
		}
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_policy_type"
					+ " WHERE policytype_id = " + policytype_id + "";
			updateQuery(StrSql);
		}
	}
}
