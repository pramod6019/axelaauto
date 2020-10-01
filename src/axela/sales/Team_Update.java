// Ved Prakash (16 Feb 2013)
// modified by Sangita , 7th may 2013
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Team_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String team_emp_id = "0";
	public String team_crm_emp_id = "0", team_preownedbranch_id = "0", team_preownedemp_id = "0";
	public String team_pbf_emp_id = "0", team_psf_emp_id = "0", team_servicepsf_emp_id = "0";
	public String team_id = "0";
	public String team_branch_id = "0";
	public String dr_branch_id = "0";
	public String teamtrans_emp_id = "0";
	public String branch_name = "";
	public String team_name = "";
	public String team_entry_id = "0";
	public String team_entry_date = "";
	public String team_modified_id = "0";
	public String team_modified_date = "";
	public String[] list_exe_trans = new String[10];
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "";
	public String QueryString = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String team_active = "1";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_team_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				team_id = CNumeric(PadQuotes(request.getParameter("team_id")));
				team_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
				team_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				if (!dr_branch_id.equals("0")) {
					team_branch_id = dr_branch_id;
				}
				branch_name = ExecuteQuery("Select branch_name from " + compdb(comp_id) + "axela_branch where branch_id = " + team_branch_id + "");
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_team_add", request).equals("1")) {
							team_entry_id = emp_id;
							team_entry_date = ToLongDate(kknow());
							AddFields();
							UpdateList();
							// DeleteExe();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("team-list.jsp?team_id=" + team_id + "&dr_branch=" + team_branch_id + "&msg=Team Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Team".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Team".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_team_edit", request).equals("1")) {
							GetValues(request, response);
							team_modified_id = emp_id;
							team_modified_date = ToLongDate(kknow());
							UpdateFields();
							UpdateList();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("team-list.jsp?team_id=" + team_id + "&dr_branch=" + team_branch_id + "&msg= Group Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Team".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_team_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("team-list.jsp?msg=Group Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		team_name = PadQuotes(request.getParameter("txt_team_name"));
		team_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		team_crm_emp_id = CNumeric(PadQuotes(request.getParameter("dr_team_crm_emp")));
		team_pbf_emp_id = CNumeric(PadQuotes(request.getParameter("dr_team_pbf_emp_id")));
		team_psf_emp_id = CNumeric(PadQuotes(request.getParameter("dr_team_psf_emp_id")));
		team_servicepsf_emp_id = CNumeric(PadQuotes(request.getParameter("dr_team_servicepsf_emp_id")));
		// SOP("team_service_psf_emp_id====" + team_servicepsf_emp_id);
		team_preownedbranch_id = CNumeric(PadQuotes(request.getParameter("dr_team_preownedbranch_id")));
		// SOP("team_preownedbranch_id====" + team_preownedbranch_id);
		team_preownedemp_id = CNumeric(PadQuotes(request.getParameter("dr_team_preownedemp_id")));
		// SOP("team_preownedemp_id====" + team_preownedemp_id);
		list_exe_trans = request.getParameterValues("list_exe_trans");
		team_active = CheckBoxValue(PadQuotes(request.getParameter("team_active")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (team_name.equals("")) {
			msg = msg + "<br>Enter Team Name!";
		} else {
			try {
				if (!team_name.equals("")) {
					StrSql = "SELECT team_name FROM " + compdb(comp_id) + "axela_sales_team"
							+ " WHERE team_name = '" + team_name
							+ "' AND team_branch_id = " + team_branch_id + "";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND team_id!= " + team_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Team Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (team_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (team_emp_id.equals("0")) {
			msg = msg + "<br>Select Team Manager!";
		}
		if (list_exe_trans == null) {
			msg = msg + "<br>Select Sales Consultant(s) for the Team!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				team_id = ExecuteQuery("SELECT (COALESCE(MAX(team_id), 0) + 1) AS team_id " + " FROM " + compdb(comp_id) + "axela_sales_team");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_team"
						+ " (team_id,"
						+ " team_name,"
						+ " team_emp_id,"
						+ " team_crm_emp_id,"
						+ " team_pbf_emp_id,"
						+ " team_psf_emp_id,"
						+ " team_servicepsf_emp_id,"
						+ " team_preownedbranch_id,"
						+ " team_preownedemp_id,"
						+ " team_active,"
						+ " team_branch_id,"
						+ " team_entry_id,"
						+ " team_entry_date)"
						+ " VALUES"
						+ " (" + team_id + ","
						+ " '" + team_name + "',"
						+ " " + team_emp_id + ","
						+ " " + team_crm_emp_id + ","
						+ " " + team_pbf_emp_id + ","
						+ " " + team_psf_emp_id + ","
						+ " " + team_servicepsf_emp_id + ","
						+ " " + team_preownedbranch_id + ","
						+ " " + team_preownedemp_id + ", "
						+ " " + team_active + ", "
						+ " " + team_branch_id + ","
						+ " " + team_entry_id + ","
						+ " '" + team_entry_date + "')";
				// SOP("StrSql-----insert---" + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT team_id, team_name, team_emp_id, team_crm_emp_id, "
					+ " team_pbf_emp_id, team_psf_emp_id, team_servicepsf_emp_id, "
					+ " team_preownedbranch_id, team_preownedemp_id, team_active, team_entry_id,"
					+ " team_entry_date, team_modified_id, team_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_id = " + team_id + "";
			// SOP("StrSql===pop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					team_id = crs.getString("team_id");
					team_name = crs.getString("team_name");
					team_emp_id = crs.getString("team_emp_id");
					team_crm_emp_id = crs.getString("team_crm_emp_id");
					team_pbf_emp_id = crs.getString("team_pbf_emp_id");
					team_psf_emp_id = crs.getString("team_psf_emp_id");
					team_servicepsf_emp_id = crs.getString("team_servicepsf_emp_id");
					team_preownedemp_id = crs.getString("team_preownedemp_id");
					team_preownedbranch_id = crs.getString("team_preownedbranch_id");
					team_active = crs.getString("team_active");
					team_entry_date = crs.getString("team_entry_date");
					team_entry_id = crs.getString("team_entry_id");
					entry_by = Exename(comp_id, crs.getInt("team_entry_id"));
					entry_date = strToLongDate(crs.getString("team_entry_date"));
					team_modified_id = crs.getString("team_modified_id");
					if (!team_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(team_modified_id));
						modified_date = strToLongDate(crs.getString("team_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_team"
						+ " SET"
						+ " team_name = '" + team_name + "',"
						+ " team_branch_id = '" + team_branch_id + "',"
						+ " team_emp_id = " + team_emp_id + ","
						+ " team_crm_emp_id = " + team_crm_emp_id + ","
						+ " team_pbf_emp_id = " + team_pbf_emp_id + ","
						+ " team_psf_emp_id = " + team_psf_emp_id + ","
						+ " team_servicepsf_emp_id = " + team_servicepsf_emp_id + ","
						+ " team_preownedbranch_id = " + team_preownedbranch_id + ","
						+ " team_preownedemp_id = " + team_preownedemp_id + ","
						+ " team_active = " + team_active + ","
						+ " team_modified_id = " + team_modified_id + ","
						+ " team_modified_date = '" + team_modified_date + "'"
						+ " WHERE team_id = " + team_id + "";
				// SOP("team_preownedemp_id===update==" + StrSql);
				updateQuery(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
						+ " SET crm_emp_id = COALESCE ((SELECT CASE "
						+ " WHEN crmdays_exe_type = 1 THEN "
						+ " (CASE WHEN crmdays_crmtype_id = 1 THEN team_crm_emp_id "
						+ " WHEN crmdays_crmtype_id = 2 THEN team_pbf_emp_id"
						+ " WHEN crmdays_crmtype_id = 3 THEN team_psf_emp_id END)"
						+ " WHEN crmdays_exe_type = 2 THEN enquiry_emp_id"
						+ " WHEN crmdays_exe_type = 3 THEN team_emp_id"
						+ " WHEN crmdays_exe_type = 4 THEN team_servicepsf_emp_id"
						+ " END"
						+ " FROM " + compdb(comp_id) + "axela_sales_team"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
						+ " WHERE team_branch_id = enquiry_branch_id"
						+ " AND teamtrans_emp_id = enquiry_emp_id"
						+ " LIMIT 1),0)"
						+ " WHERE 1 = 1 "
						+ " AND crm_desc = ''";
				// SOP("StrSql---------" + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void UpdateList() {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id = " + team_id + "";
				updateQuery(StrSql);
				if (list_exe_trans != null) {
					for (int i = 0; i < list_exe_trans.length; i++) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_team_exe"
								+ "(teamtrans_team_id, teamtrans_emp_id)"
								+ " VALUES(" + team_id + ", "
								+ list_exe_trans[i] + ")";
						updateQuery(StrSql);

						StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_emp_id =" + list_exe_trans[i] + ""
								+ " AND teamtrans_team_id !=" + team_id + "";
						updateQuery(StrSql);
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_team_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " WHERE enquiry_team_id = " + team_id + "";
		// SOP("delete====" + StrSql);
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Team is associated with Enquiry!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_team_exe"
						+ " WHERE teamtrans_team_id = " + team_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_team"
						+ " WHERE team_id = " + team_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateManager() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " ORDER BY emp_name";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), team_emp_id));
				Str.append(">").append(crs.getString("emp_name"));
				Str.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_sales = '1'"
					+ " AND (emp_branch_id = " + team_branch_id + " OR emp_id = 1 "
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id AND empbr.emp_branch_id=" + team_branch_id + "))"
					+ " AND emp_active = 1"
					+ " ORDER BY emp_name";
			// SOP("------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append(">");
				Str.append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutivesTrans() {
		StringBuilder Str = new StringBuilder();
		try {
			if (update.equals("yes") && !updateB.equals("Update Team")) {
				StrSql = " SELECT emp_id, emp_name, emp_ref_no"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
						+ " WHERE teamtrans_team_id = " + team_id + " and emp_active='1'";
				StrSql = StrSql
						+ " ORDER BY emp_name";
			}
			if (add.equals("yes") && list_exe_trans != null) {
				StrSql = "SELECT emp_id, emp_name, emp_ref_no"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1 = 1 "
						// + " AND emp_active=1"
						+ " ORDER BY emp_name";
			}
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if ((add.equals("yes") || updateB.equals("Update Team")) && list_exe_trans != null) {
					for (int i = 0; i < list_exe_trans.length; i++) {
						if (crs.getString("emp_id").equals(list_exe_trans[i])) {
							Str.append("<option value=").append(crs.getString("emp_id"));
							Str.append(" selected>").append(crs.getString("emp_name"));
							Str.append("(").append(crs.getString("emp_ref_no"));
							Str.append(")</option>\n");
						}
					}
				} else if (update.equals("yes") && !updateB.equals("Update Team")) {
					Str.append("<option value=").append(crs.getString("emp_id"));
					Str.append(" selected>").append(crs.getString("emp_name"));
					Str.append(" (").append(crs.getString("emp_ref_no"));
					Str.append(")</option>\n");
				}
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCRMExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " ORDER BY emp_name";
			// SOP("100=="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), team_crm_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePBFExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " ORDER BY emp_name";
			// SOP("100=="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), team_pbf_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePSFExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " ORDER BY emp_name";
			// SOP("100=="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), team_psf_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateServicePSFExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " ORDER BY emp_name";
			// SOP("100=="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), team_servicepsf_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no"));
				Str.append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreOwnedBranch() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active = '1' "
					+ " AND branch_branchtype_id = 2 "
					+ " ORDER BY branch_name";
			// SOP("StrSql===branch===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), team_preownedbranch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreownedExecutives(String comp_id, String team_preownedbranch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1 = 1"
					+ " AND emp_preowned = '1'"
					+ " AND emp_active = '1'"
					+ " AND emp_branch_id = " + team_preownedbranch_id + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql---popexe--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_team_preownedemp_id' id='dr_team_preownedemp_id' class='form-control'>");
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), team_preownedemp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
