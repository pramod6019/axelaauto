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

public class ManageInsurComp_Update extends Connect {

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
	public String inscomp_id = "0";
	public String inscomp_name = "";
	public String inscomp_active = "0";
	public String inscomp_entry_id = "0";
	public String entry_by = "";
	public String inscomp_entry_date = "";
	public String inscomp_modified_id = "0";
	public String modified_by = "";
	public String inscomp_modified_date = "";
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
				inscomp_id = CNumeric(PadQuotes(request.getParameter("inscomp_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					if (inscomp_id.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("index.jsp"));
					} else if (inscomp_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Insurance Company!"));
					}
				}

				if (add.equals("yes")) {
					status = "Add Insurance ";
				} else if (update.equals("yes")) {
					status = "Update Insurance ";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						inscomp_active = "1";
					} else {
						GetValues(request, response);
						inscomp_entry_id = emp_id;
						inscomp_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurcomp.jsp?inscomp_id=" + inscomp_id + "&msg=Insurance Company added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Insurance Company".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Insurance Company".equals(deleteB)) {
						GetValues(request, response);
						inscomp_modified_id = emp_id;
						inscomp_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurcomp.jsp?inscomp_id=" + inscomp_id + "&msg=Insurance Company updated Successfully!"));
						}
					}
					if ("Delete Insurance Company".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurcomp.jsp?msg=Insurance Company deleted Successfully!"));
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
		inscomp_name = PadQuotes(request.getParameter("txt_inscomp_name"));
		inscomp_active = CheckBoxValue(PadQuotes(request.getParameter("chk_inscomp_active")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (inscomp_name.equals("")) {
			msg = "<br>Enter Insurance Company Name!";
		} else {
			StrSql = "SELECT inscomp_name FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_name = '" + inscomp_name + "'";
			if (update.equals("yes")) {
				StrSql = StrSql + " AND inscomp_id != " + inscomp_id + "";
			}
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Insurance Company found!";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {

			inscomp_id = ExecuteQuery("SELECT COALESCE(MAX(inscomp_id),0)+1 AS inscomp_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_comp");

			StrSql = "INSERT into " + compdb(comp_id) + "axela_insurance_comp"
					+ " (inscomp_id,"
					+ " inscomp_name,"
					+ " inscomp_active,"
					+ " inscomp_entry_id,"
					+ " inscomp_entry_date)"
					+ " values"
					+ " (" + inscomp_id + ","
					+ " '" + inscomp_name + "',"
					+ " '" + inscomp_active + "',"
					+ " " + inscomp_entry_id + ","
					+ " '" + inscomp_entry_date + "')";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_id = " + inscomp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					inscomp_name = crs.getString("inscomp_name");
					inscomp_active = crs.getString("inscomp_active");
					inscomp_entry_id = crs.getString("inscomp_entry_id");
					if (!inscomp_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(inscomp_entry_id));
					}
					entry_date = strToLongDate(crs.getString("inscomp_entry_date"));
					inscomp_modified_id = crs.getString("inscomp_modified_id");
					if (!inscomp_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(inscomp_modified_id));
					}
					modified_date = strToLongDate(crs.getString("inscomp_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Insurance Company!"));
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
			StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_comp"
					+ " SET"
					+ " inscomp_name = '" + inscomp_name + "',"
					+ " inscomp_active = '" + inscomp_active + "',"
					+ " inscomp_modified_id = " + inscomp_modified_id + ","
					+ " inscomp_modified_date = '" + inscomp_modified_date + "'"
					+ " WHERE inscomp_id = " + inscomp_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT insurpolicy_inscomp_id FROM " + compdb(comp_id) + "axela_insurance_policy"
				+ " where insurpolicy_inscomp_id = " + inscomp_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Insurance Company is associated with Insurance!";
		}
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_id = " + inscomp_id + "";
			updateQuery(StrSql);
		}
	}
}
