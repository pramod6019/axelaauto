package axela.portal;
//////// Modified by Ceeba on  October 17, 2008
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Email_Exe_Send1 extends Connect {

	public String email_from = "";
	public String email_subject = "";
	public String email_id = "0";
	public String email_mobileno = "";
	public String email_msg = "";
	public String email_date = "";
	public String email_sent = "";
	public String email_entry_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String student_branch_id = "0";
	public String branch_id = "0";
	public String branchfilter = "";
	public String emp_role_id = "0";
	public String emp_jobtitle_id = "0";
	public String status = "";
	public String sendB = "";
	public String msg = "";
	public String StrSql = "";
	public String BranchAccess = "";
	public String comp_email_enable = "";
	public String[] exe_team_trans;
	public String email_allemail = "1";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_email_send", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				email_allemail = PadQuotes(request.getParameter("chk_email_allexe"));
				if (email_allemail.equals("on")) {
					email_allemail = "1";
				} else {
					email_allemail = "0";
				}
				if (branch_id.equals("0")) {
					student_branch_id = PadQuotes(request.getParameter("dr_branch"));
				} else {
					student_branch_id = branch_id;
				}
				if (student_branch_id.equals("")) {
					student_branch_id = "-1";
				}
				emp_jobtitle_id = CNumeric(PadQuotes(request.getParameter("dr_desig")));
				emp_role_id = CNumeric(PadQuotes(request.getParameter("dr_role")));

				String SqlStr = "select comp_email_enable "
						+ " from " + compdb(comp_id) + "axela_comp "
						+ " where comp_active = '1'";
				CachedRowSet crs = processQuery(SqlStr, 0);
				while (crs.next()) {
					comp_email_enable = crs.getString("comp_email_enable");
				}
				crs.close();
				if (comp_email_enable.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				if (!branch_id.equals("0")) {
					branchfilter = " and student_branch_id = " + branch_id;
				}
				sendB = PadQuotes(request.getParameter("send_button"));
				msg = PadQuotes(request.getParameter("msg"));
				email_id = PadQuotes(request.getParameter("email_id"));
				email_subject = PadQuotes(request.getParameter("txt_email_subject"));
				email_msg = PadQuotes(request.getParameter("txt_email_msg"));

				if (!"Send".equals(sendB)) {
					email_mobileno = "";
					email_date = "";
					email_sent = "";
					email_entry_id = "";
				} else {
					CheckPerm(comp_id, "emp_email_send", request, response);
					GetValues(request, response);
					email_entry_id = CNumeric(GetSession("emp_id", request));
					email_date = ToLongDate(kknow());
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						CheckForm();
						if (msg.equals("")) {
							ListExe();
						}
						if (msg.equals("")) {
							if (email_allemail.equals("1")) {
								email_allemail = "on";
							} else {
								email_allemail = "off";
							}
							response.sendRedirect(response.encodeRedirectURL("email-exe-send.jsp?chk_email_allexe=" + email_allemail + "&msg=Email sent successfully!"));
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

	protected void CheckForm() {
		msg = "";
		if (email_subject.equals("")) {
			msg = msg + "<br>Enter Subject!";
		}
		if (email_allemail.equals("0")) {
			if (exe_team_trans == null) {
				msg = msg + "<br>Select Executives!";
			}
		}
		if (email_msg.equals("")) {
			msg = msg + "<br>Enter Message!";
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			email_id = PadQuotes(request.getParameter("email_id"));
			email_subject = PadQuotes(request.getParameter("txt_email_subject"));
			email_mobileno = PadQuotes(request.getParameter("txt_email_mobileno"));
			email_msg = PadQuotes(request.getParameter("txt_email_msg"));
			email_allemail = PadQuotes(request.getParameter("chk_email_allexe"));
			if (email_allemail.equals("on")) {
				email_allemail = "1";
			} else {
				email_allemail = "0";
			}
			exe_team_trans = request.getParameterValues("emailexe_exe");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ListExe() {
		String StrSearch = "";
		email_from = ExecuteQuery("select comp_email1 from " + compdb(comp_id) + "axela_comp");
		if (email_subject.length() > 1000) {
			email_subject = email_subject.substring(0, 1000);
		}
		if (email_msg.length() > 10000) {
			email_msg = email_msg.substring(0, 10000);
		}
		try {
			email_subject = "replace('" + email_subject + "','[NAME]',emp_name)";
			email_subject = "replace(" + email_subject + ",'[EXECUTIVEID]',emp_id)";
			email_subject = "replace(" + email_subject + ",'[EXECUTIVEREFNO]',emp_ref_no)";

			email_msg = "replace('" + email_msg + "','[NAME]',emp_name)";
			email_msg = "replace(" + email_msg + ",'[EXECUTIVEID]',emp_id)";
			email_msg = "replace(" + email_msg + ",'[EXECUTIVEREFNO]',emp_ref_no)";

			StrSql = "SELECT "
					+ " '" + email_from + "', "
					+ " emp_email1, "
					+ " " + email_subject + ", "
					+ " " + email_msg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " emp_id, "
					+ " " + email_entry_id + ", "
					+ " 0 "
					+ " from " + compdb(comp_id) + "axela_emp "
					+ " left  join " + compdb(comp_id) + "axela_branch on branch_id = emp_branch_id";

			StrSql = StrSql + " where emp_active = '1' " + BranchAccess;

			if (email_allemail.equals("1")) {
				if (student_branch_id.equals("-1")) {
					StrSearch = StrSearch + " ";
				} else if (student_branch_id.equals("0")) {
					StrSearch = StrSearch + " and emp_branch_id = 0";
				} else {
					StrSearch = StrSearch + " and (emp_branch_id = " + student_branch_id + " or emp_id in (select " + compdb(comp_id) + "axela_emp_branch.emp_id from " + compdb(comp_id)
							+ "axela_emp_branch where " + compdb(comp_id) + "axela_emp_branch.emp_branch_id = " + student_branch_id + "))";
				}
			} else if (email_allemail.equals("0")) {
				if (exe_team_trans != null) {
					String id = "";
					for (int i = 0; i < exe_team_trans.length; i++) {
						id = id + " " + exe_team_trans[i] + ", ";
					}
					StrSearch = StrSearch + " and emp_id in (" + id.substring(0, id.lastIndexOf(",")) + ")";
				}
			}
			StrSearch = StrSearch + " and emp_email1!='' ";
			if (!emp_role_id.equals("0")) {
				StrSearch = StrSearch + " and emp_role_id =" + emp_role_id;
			}
			if (!emp_jobtitle_id.equals("0")) {
				StrSearch = StrSearch + " and emp_jobtitle_id =" + emp_jobtitle_id;
			}
			StrSql = StrSql + StrSearch;
			StrSql = StrSql + " group by emp_id order by emp_id desc ";
			AddFields(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields(String Sql) {
		if (msg.equals("")) {
			try {
				StrSql = "insert into " + compdb(comp_id) + "axela_email "
						+ "("
						+ "email_from, "
						+ "email_to, "
						+ "email_subject,"
						+ "email_msg, "
						+ "email_date, "
						+ "email_emp_id, "
						+ "email_entry_id,"
						+ "email_sent)"
						+ " " + Sql + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateExecutives() {
		try {
			StrSql = "select emp_id,emp_name,emp_ref_no from " + compdb(comp_id) + "axela_emp "
					+ " left  join " + compdb(comp_id) + "axela_branch on branch_id = emp_branch_id "
					+ " WHERE emp_email1!='' and emp_active='1' " + BranchAccess;
			if (!student_branch_id.equals("-1")) {
				StrSql = StrSql + " and emp_branch_id = " + student_branch_id;
			}
			if (!emp_role_id.equals("0")) {
				StrSql = StrSql + " and emp_role_id = " + emp_role_id;
			}
			if (!emp_jobtitle_id.equals("0")) {
				StrSql = StrSql + " and emp_jobtitle_id = " + emp_jobtitle_id;
			}
			StrSql = StrSql + " order by emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String Exec = " ";
			while (crs.next()) {
				Exec = Exec + "<option value=" + crs.getString("emp_id") + ">" + crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")</option> \n";
			}
			crs.close();
			return Exec;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExecutivesTrans() {
		String stringval = "";
		try {
			StrSql = " SELECT " + compdb(comp_id) + "axela_emp.emp_id,emp_name, "
					+ " emp_ref_no "
					+ " from " + compdb(comp_id) + "axela_emp "
					+ " where  emp_id ='" + emp_id + "' and emp_active='1' "
					+ " order by emp_name";
			if (exe_team_trans != null) {
				StrSql = "select emp_id,emp_name,emp_ref_no from " + compdb(comp_id) + "axela_emp WHERE emp_active=1 "
						+ " order by emp_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (exe_team_trans != null) {
					for (int i = 0; i < exe_team_trans.length; i++) {
						if (crs.getString("emp_id").equals(exe_team_trans[i])) {
							stringval = stringval + "<option value=" + crs.getString("emp_id") + " selected>" + crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")</option> \n";
						}
					}
				}
			}
			crs.close();
			return stringval;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "select branch_id,branch_name, branch_code from " + compdb(comp_id) + "axela_branch "
					+ " where  branch_active='1' "
					+ BranchAccess
					+ " order by branch_brand_id, branch_branchtype_id, branch_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =-1>All Branches</option>");
			stringval.append("<option value =0 ").append(StrSelectdrop("0", student_branch_id)).append(">Head Office</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), student_branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option> \n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDesignation() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "select jobtitle_id, jobtitle_desc from " + compdb(comp_id) + "axela_jobtitle where jobtitle_id!=1 order by jobtitle_desc";
			CachedRowSet crs = processQuery(StrSql, 0);
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("jobtitle_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("jobtitle_id"), emp_jobtitle_id));
				stringval.append(">").append(crs.getString("jobtitle_desc")).append("</option> \n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateRole() {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "select role_id,role_name from " + compdb(comp_id) + "axela_emp_role "
					+ " order by role_name ";
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("role_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("role_id"), emp_role_id));
				stringval.append(">").append(crs.getString("role_name")).append("</option> \n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
