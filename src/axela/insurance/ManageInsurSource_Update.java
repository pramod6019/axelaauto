// JEET 07 NOV 2014
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInsurSource_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "", jctype_workhour = "";
	public String StrSql = "";
	public String msg = "";
	public String insursource_id = "0";
	public String insursource_name = "";
	public String insursource_entry_id = "0";
	public String insursource_entry_by = "";
	public String insursource_entry_date = "";
	public String insursource_modified_id = "0";
	public String insursource_modified_by = "";
	public String insursource_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String entry_by = "";
	public String modified_by = "";
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
				insursource_id = CNumeric(PadQuotes(request.getParameter("insursource_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					if (insursource_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Insurance Source!"));
					}
				}

				if ("yes".equals(add)) {
					status = "Add";
					if ("yes".equals(addB)) {
						GetValues(request, response);
						insursource_entry_id = emp_id;
						insursource_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsursource.jsp?insursource_id=" + insursource_id + "&msg= Insurance Source Added Successfully!"));
						}
					}
				} else if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Insurance Source".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Insurance Source".equals(deleteB)) {
						GetValues(request, response);
						insursource_modified_id = emp_id;
						insursource_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsursource.jsp?insursource_id=" + insursource_id + "&msg= Insurance Source Updated Successfully!"));
						}
					} else if ("Delete Insurance Source".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsursource.jsp?msg= Insurance Source  Deleted Successfully!"));
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
		insursource_name = PadQuotes(request.getParameter("txt_insursource_name"));
		insursource_entry_by = PadQuotes(request.getParameter("entry_by"));
		insursource_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (insursource_name.equals("")) {
			msg = "<br>Enter Insurance Source Name!";
		}
		try {
			if (!insursource_name.equals("")) {
				StrSql = "Select insursource_name from " + compdb(comp_id) + "axela_insurance_source"
						+ " where insursource_name = '" + insursource_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and insursource_id != " + insursource_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Insurance Source Found!";
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
				insursource_id = ExecuteQuery("Select coalesce(max(insursource_id), 0)+1 as insursource_id from " + compdb(comp_id) + "axela_insurance_source");

				if (insursource_id == null || insursource_id.equals("")) {
					insursource_id = "0";
				}
				int insurtype_idi = Integer.parseInt(insursource_id) + 1;
				insursource_id = "" + insurtype_idi;

				StrSql = "Insert into " + compdb(comp_id) + "axela_insurance_source"
						+ " (insursource_id,"
						+ " insursource_name,"
						+ " insursource_entry_id,"
						+ " insursource_entry_date)"
						+ " values"
						+ " (" + insursource_id + ","
						+ " '" + insursource_name + "',"
						+ " " + insursource_entry_id + ","
						+ " '" + insursource_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select *"
					+ " FROM " + compdb(comp_id) + "axela_insurance_source"
					+ " where insursource_id = " + insursource_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insursource_name = crs.getString("insursource_name");
					insursource_entry_id = crs.getString("insursource_entry_id");
					if (!insursource_entry_id.equals("")) {
						insursource_entry_by = Exename(comp_id, Integer.parseInt(insursource_entry_id));
					}
					entry_date = strToLongDate(crs.getString("insursource_entry_date"));
					insursource_modified_id = crs.getString("insursource_modified_id");
					if (!insursource_modified_id.equals("")) {
						insursource_modified_by = Exename(comp_id, Integer.parseInt(insursource_modified_id));
					}
					modified_date = strToLongDate(crs.getString("insursource_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg= Invalid Insurance Source!"));
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
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_source"
						+ " SET"
						+ " insursource_name = '" + insursource_name + "',"
						+ " insursource_modified_id = " + insursource_modified_id + ","
						+ " insursource_modified_date = '" + insursource_modified_date + "'"
						+ " where insursource_id = " + insursource_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		msg = "";
		StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_insursource_id = " + insursource_id;
		if (!ExecuteQuery(StrSql).equals("0")) {
			msg += "<br>Insurance Source is associated with Vechicle!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_insurance_source"
						+ " where insursource_id = " + insursource_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
