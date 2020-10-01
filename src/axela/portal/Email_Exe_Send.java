package axela.portal;
//////// Modified by Ceeba on  October 17, 2008
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Email_Exe_Send extends Connect {

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

				StrSql = "SELECT comp_email_enable "
						+ " FROM " + compdb(comp_id) + "axela_comp "
						+ " WHERE comp_active = '1'";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					comp_email_enable = crs.getString("comp_email_enable");
				}
				crs.close();
				if (comp_email_enable.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				if (!branch_id.equals("0")) {
					branchfilter = " AND student_branch_id = " + branch_id;
				}
				sendB = PadQuotes(request.getParameter("send_button"));
				// SOP("send==" + sendB);
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
					// SOP("111");
					email_date = ToLongDate(kknow());
					// SOP("222");
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						CheckForm();
						// SOP("33");
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
		// SOP("coming");
		msg = "";
		// SOP("exe_team_trans=3=" + exe_team_trans.length);
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
		// SOP("msg==" + msg);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			// SOP("COMING");
			email_id = PadQuotes(request.getParameter("email_id"));
			email_subject = PadQuotes(request.getParameter("txt_email_subject"));
			email_mobileno = PadQuotes(request.getParameter("txt_email_mobileno"));
			email_msg = PadQuotes(request.getParameter("txt_email_msg"));
			email_allemail = PadQuotes(request.getParameter("chk_email_allexe"));

			// exe_team_trans = request.getParameterValues("exe_team_trans");
			// SOP("exe_team_trans===" + exe_team_trans);
			// for (String s : exe_team_trans) {
			// SOP("s==" + s);
			// }
			if (email_allemail.equals("")) {
				exe_team_trans = request.getParameterValues("exe_team_trans");
				 SOP("exe_team_trans===" + exe_team_trans);
					 for (String s : exe_team_trans) {
					 SOP("s==" + s);
					 }
			}
			 SOP("exe_team_trans=22=" + exe_team_trans[1]);
			if (email_allemail.equals("on")) {
				email_allemail = "1";
			} else {
				email_allemail = "0";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void ListExe() {
		String StrSearch = "";
		email_from = ExecuteQuery("SELECT config_admin_email FROM " + compdb(comp_id) + "axela_config");
		if (email_subject.length() > 1000) {
			email_subject = email_subject.substring(0, 1000);
		}
		if (email_msg.length() > 10000) {
			email_msg = email_msg.substring(0, 10000);
		}
		try {
			email_subject = "REPLACE('" + email_subject + "','[NAME]',emp_name)";
			email_subject = "REPLACE(" + email_subject + ",'[EXECUTIVEID]',emp_id)";
			email_subject = "REPLACE(" + email_subject + ",'[EXECUTIVEREFNO]',emp_ref_no)";

			email_msg = "REPLACE('" + email_msg + "','[NAME]',emp_name)";
			email_msg = "REPLACE(" + email_msg + ",'[EXECUTIVEID]',emp_id)";
			email_msg = "REPLACE(" + email_msg + ",'[EXECUTIVEREFNO]',emp_ref_no)";

			StrSql = "SELECT "
					+ " '" + email_from + "', "
					+ " emp_email1, "
					+ " " + email_subject + ", "
					+ " " + email_msg + ", "
					+ " '" + ToLongDate(kknow()) + "', "
					+ " emp_id, "
					+ " " + email_entry_id + ", "
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";

			StrSql = StrSql + " WHERE emp_active = '1' " + BranchAccess;
			// SOP("StrSql===email_exe_send==="+StrSql);
			if (email_allemail.equals("1")) {
				if (student_branch_id.equals("-1")) {
					StrSearch = StrSearch + " ";
				} else if (student_branch_id.equals("0")) {
					StrSearch = StrSearch + " AND emp_branch_id = 0";
				} else {
					StrSearch = StrSearch + " AND (emp_branch_id = " + student_branch_id
							+ " OR emp_id in (SELECT " + compdb(comp_id) + "axela_emp_branch.emp_id "
							+ " FROM " + compdb(comp_id) + "axela_emp_branch"
							+ " WHERE " + compdb(comp_id) + "axela_emp_branch.emp_branch_id = " + student_branch_id + "))";
				}
			} else if (email_allemail.equals("0")) {
				if (exe_team_trans != null) {
					// SOP("coming123");
					String id = "";
					// SOP("exe_team_trans.length==" + exe_team_trans.length);
					for (int i = 0; i < exe_team_trans.length; i++) {
						id = id + " " + exe_team_trans[i] + ", ";
						// SOP("id==" + id);
					}

					StrSearch = StrSearch + " AND emp_id in (" + id.substring(0, id.lastIndexOf(",")) + ")";
				}
			}
			StrSearch = StrSearch + " AND emp_email1!='' ";
			if (!emp_role_id.equals("0")) {
				StrSearch = StrSearch + " AND emp_role_id =" + emp_role_id;
			}
			if (!emp_jobtitle_id.equals("0")) {
				StrSearch = StrSearch + " AND emp_jobtitle_id =" + emp_jobtitle_id;
			}
			StrSql = StrSql + StrSearch;
			StrSql = StrSql + " GROUP BY emp_id ";
			StrSql = StrSql + "ORDER BY emp_id DESC ";
			// SOP("StrSql==" + StrSql);
			AddFields(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields(String Sql) {
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email "
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
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateExecutives() {
		try {
			StrSql = "SELECT emp_id,emp_name,emp_ref_no "
					+ "FROM " + compdb(comp_id) + "axela_emp "
					+ " LEFT  JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id "
					+ " WHERE emp_email1!='' AND emp_active='1' " + BranchAccess;
			if (!student_branch_id.equals("-1")) {
				StrSql = StrSql + " AND emp_branch_id = " + student_branch_id;
			}
			if (!emp_role_id.equals("0")) {
				StrSql = StrSql + " AND emp_role_id = " + emp_role_id;
			}
			if (!emp_jobtitle_id.equals("0")) {
				StrSql = StrSql + " AND emp_jobtitle_id = " + emp_jobtitle_id;
			}
			StrSql = StrSql + " ORDER BY emp_name";
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
		// SOP("===111=PopulateExecutivesTrans=");
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id,emp_name, emp_ref_no "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE  emp_id ='" + emp_id + "' AND emp_active='1' "
					+ " ORDER BY emp_name";
			if (exe_team_trans != null) {
				StrSql = "SELECT emp_id,emp_name,emp_ref_no"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_active=1 "
						+ " ORDER BY emp_name";
			}
			// SOP("StrSql" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				if (exe_team_trans != null) {
					for (int i = 0; i < exe_team_trans.length; i++) {
						if (crs.getString("emp_id").equals(exe_team_trans[i])) {
							Str.append("<option value=").append(crs.getString("emp_id"));
							Str.append(" selected>").append(crs.getString("emp_name"));
							Str.append(" (").append(crs.getString("emp_ref_no")).append(")</option>\n");

						}
					}
				}
			}
			crs.close();
			// SOP("===111=PopulateExecutivesTrans values=" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch() {
		StringBuilder stringval = new StringBuilder();
		try {
			StrSql = "SELECT branch_id,branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE  branch_active='1' " + BranchAccess
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";
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
			StrSql = " SELECT jobtitle_id, jobtitle_desc"
					+ " FROM " + compdb(comp_id) + "axela_jobtitle"
					+ " WHERE jobtitle_id!=1 "
					+ " ORDER BY jobtitle_desc";
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
			StrSql = " SELECT role_id,role_name "
					+ " FROM " + compdb(comp_id) + "axela_emp_role "
					+ " ORDER BY role_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
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
