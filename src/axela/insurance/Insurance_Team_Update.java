// Ved Prakash (16 Feb 2013)
// modified by Sangita , 7th may 2013
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance_Team_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String insurteam_emp_id = "0";
	public String insurteam_psf_emp_id = "0";
	public String insurteam_id = "0";
	public String insurteam_branch_id = "0";
	public String dr_branch_id = "0";
	public String insurteamtrans_emp_id = "0";
	public String branch_name = "";
	public String insurteam_name = "";
	public String insurteam_entry_id = "0";
	public String insurteam_entry_date = "";
	public String insurteam_modified_id = "0";
	public String insurteam_modified_date = "";
	public String[] list_exe_trans = new String[10];
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "";
	public String QueryString = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String insurteam_active = "1";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_team_access", request, response);
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
				insurteam_id = CNumeric(PadQuotes(request.getParameter("insurteam_id")));
				SOP("insurteam_id===" + insurteam_id);
				insurteam_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
				insurteam_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				if (!dr_branch_id.equals("0")) {
					insurteam_branch_id = dr_branch_id;
				}
				branch_name = ExecuteQuery("SELECT branch_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + insurteam_branch_id + "");
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_insurance_team_add", request).equals("1")) {
							insurteam_entry_id = emp_id;
							insurteam_entry_date = ToLongDate(kknow());
							AddFields();
							UpdateList();
							// DeleteExe();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("insurance-team-list.jsp?insurteam_id=" + insurteam_id + "&dr_branch=" + insurteam_branch_id + "&msg=Team added Successfully!"));
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
						SOP("GetValues before");
						GetValues(request, response);
						SOP("GetValues after");
						if (ReturnPerm(comp_id, "emp_insurance_team_edit", request).equals("1")) {
							GetValues(request, response);
							insurteam_modified_id = emp_id;
							insurteam_modified_date = ToLongDate(kknow());
							UpdateFields();
							UpdateList();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insurance-team-list.jsp?insurteam_id=" + insurteam_id + "&dr_branch=" + insurteam_branch_id
										+ "&msg= Group updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Team".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_insurance_team_delete", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insurance-team-list.jsp?msg=Group deleted Successfully!"));
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

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		insurteam_name = PadQuotes(request.getParameter("txt_team_name"));
		insurteam_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		insurteam_psf_emp_id = CNumeric(PadQuotes(request.getParameter("dr_team_psf_emp_id")));
		list_exe_trans = request.getParameterValues("list_exe_trans");
		insurteam_active = CheckBoxValue(PadQuotes(request.getParameter("team_active")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (insurteam_name.equals("")) {
			msg = msg + "<br>Enter Team Name!";
		} else {
			try {
				if (!insurteam_name.equals("")) {
					StrSql = "SELECT insurteam_name FROM " + compdb(comp_id) + "axela_insurance_team"
							+ " WHERE insurteam_name = '" + insurteam_name
							+ "' AND insurteam_branch_id = " + insurteam_branch_id + "";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND insurteam_id!= " + insurteam_id + "";
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
		if (insurteam_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (insurteam_emp_id.equals("0")) {
			msg = msg + "<br>Select Team Manager!";
		}
		if (list_exe_trans == null) {
			msg = msg + "<br>Select Insurance Consultant(s) for the Team!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				insurteam_id = ExecuteQuery("SELECT (COALESCE(MAX(insurteam_id),0)+1) AS insurteam_id"
						+ " FROM " + compdb(comp_id) + "axela_insurance_team");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_team"
						+ " (insurteam_id,"
						+ " insurteam_name,"
						+ " insurteam_emp_id,"
						+ " insurteam_psf_emp_id,"
						+ " insurteam_active,"
						+ " insurteam_branch_id,"
						+ " insurteam_entry_id,"
						+ " insurteam_entry_date)"
						+ " VALUES"
						+ " (" + insurteam_id + ","
						+ " '" + insurteam_name + "',"
						+ " " + insurteam_emp_id + ","
						+ " " + insurteam_psf_emp_id + ","
						+ " " + insurteam_active + ", "
						+ " " + insurteam_branch_id + ","
						+ " " + insurteam_entry_id + ","
						+ " '" + insurteam_entry_date + "')";
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
			StrSql = "SELECT insurteam_id, insurteam_name, insurteam_emp_id, "
					+ " insurteam_psf_emp_id, "
					+ " insurteam_active, insurteam_entry_id,"
					+ " insurteam_entry_date, insurteam_modified_id, insurteam_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_insurance_team"
					+ " WHERE insurteam_id = " + insurteam_id + "";
			SOP("StrSql===pop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					insurteam_id = crs.getString("insurteam_id");
					insurteam_name = crs.getString("insurteam_name");
					insurteam_emp_id = crs.getString("insurteam_emp_id");
					insurteam_psf_emp_id = crs.getString("insurteam_psf_emp_id");
					insurteam_active = crs.getString("insurteam_active");
					insurteam_entry_date = crs.getString("insurteam_entry_date");
					insurteam_entry_id = crs.getString("insurteam_entry_id");
					entry_by = Exename(comp_id, crs.getInt("insurteam_entry_id"));
					entry_date = strToLongDate(crs.getString("insurteam_entry_date"));
					insurteam_modified_id = crs.getString("insurteam_modified_id");
					if (!insurteam_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(insurteam_modified_id));
						modified_date = strToLongDate(crs.getString("insurteam_modified_date"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_team"
						+ " SET"
						+ " insurteam_name = '" + insurteam_name + "',"
						+ " insurteam_branch_id = '" + insurteam_branch_id + "',"
						+ " insurteam_emp_id = " + insurteam_emp_id + ","
						+ " insurteam_psf_emp_id = " + insurteam_psf_emp_id + ","
						+ " insurteam_active = " + insurteam_active + ","
						+ " insurteam_modified_id = " + insurteam_modified_id + ","
						+ " insurteam_modified_date = '" + insurteam_modified_date + "'"
						+ " WHERE insurteam_id = " + insurteam_id + "";
				SOP("team_insurteam_id===update==" + StrSql);
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
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_team_exe"
						+ " WHERE insurteamtrans_team_id = " + insurteam_id + "";
				updateQuery(StrSql);
				if (list_exe_trans != null) {
					for (int i = 0; i < list_exe_trans.length; i++) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_team_exe"
								+ "(insurteamtrans_team_id, insurteamtrans_emp_id)"
								+ " VALUES(" + insurteam_id + ", "
								+ list_exe_trans[i] + ")";
						updateQuery(StrSql);

						StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_team_exe"
								+ " WHERE insurteamtrans_emp_id =" + list_exe_trans[i] + ""
								+ " AND insurteamtrans_team_id !=" + insurteam_id + "";
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
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_team_exe"
					+ " WHERE insurteamtrans_team_id = " + insurteam_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_team"
					+ " WHERE insurteam_id = " + insurteam_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// }
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
				Str.append(Selectdrop(crs.getInt("emp_id"), insurteam_emp_id));
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
					+ " WHERE emp_insur = '1'"
					+ " AND (emp_branch_id = " + insurteam_branch_id
					+ " OR emp_id = 1 "
					+ " OR emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id"
					+ " AND empbr.emp_branch_id=" + insurteam_branch_id + "))"
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
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_team_exe ON insurteamtrans_emp_id = emp_id"
						+ " WHERE insurteamtrans_team_id = " + insurteam_id + " AND emp_active='1'";
				StrSql = StrSql + " ORDER BY emp_name";
			}
			if (add.equals("yes") && list_exe_trans != null) {
				StrSql = "SELECT emp_id, emp_name, emp_ref_no"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE 1=1 "
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
				Str.append(Selectdrop(crs.getInt("emp_id"), insurteam_psf_emp_id));
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

}
