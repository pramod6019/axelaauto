package axela.service;
//sangita
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_PSF_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String[] exe_ids;
	public String exe_id = "";
	public String branch_id = "0";
	public String comp_id = "0";
	public static String msg = "";
	public Report_Check reportexe = new Report_Check();
	public String emp_all_exe = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("branch_id==" + branch_id);
				BranchAccess = GetSession("BranchAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				ExeAccess = GetSession("ExeAccess", request);

				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					if ((!dr_branch_id.equals("0"))) {
						StrSearch = " and jc_branch_id = " + dr_branch_id;
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
					}
				}
				if (msg.equals("")) {
					StrHTML = JCTriggerStatus();
				} else {
					msg = "Error!" + msg;
				}
				StrHTML += getFollowupPriority();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
	}

	public String JCTriggerStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "select branch_id, concat(branch_name,' (',branch_code,')') as branch_name "
					+ " from " + compdb(comp_id) + "axela_branch "
					+ " where branch_active='1' "
					+ " and branch_id = " + dr_branch_id + ""
					+ " order by branch_name ";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				while (crs1.next()) {
					Str.append("<tr align=center><th colspan=5>").append(crs1.getString("branch_name")).append("</th></tr>");
					Str.append("<tr align=center>\n");
					Str.append("<td align=center><b>Level 1</b></td>\n");
					Str.append("<td align=center><b>Level 2</b></td>\n");
					Str.append("<td align=center><b>Level 3</b></td>\n");
					Str.append("<td align=center><b>Level 4</b></td>\n");
					Str.append("<td align=center><b>Level 5</b></td>\n");
					Str.append("</tr>");
					Str.append("<tr align=center>\n");
					for (int i = 1; i <= 5; i++) {
						StrSql = " SELECT jc_id, concat(emp_name,' (',emp_ref_no,')') as emp_name"
								+ " from " + compdb(comp_id) + "axela_service_jc"
								+ " left join " + compdb(comp_id) + "axela_service_jc_psf on jcpsf_jc_id = jc_id"
								+ " AND jcpsf_jcpsffeedbacktype_id = 0"
								+ " and jcpsf_jcpsfdays_id=2"
								+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = jcpsf_emp_id"
								+ " where 1=1 and jc_active = 1"
								+ " and jcpsf_trigger=" + i
								+ StrSearch + BranchAccess.replace("branch_id", "jc_branch_id");
						if (emp_all_exe.equals("0"))
						{
							StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");
						}
						StrSql += " group by jc_id order by emp_name";
						// SOP("StrSql==" + StrSql);
						CachedRowSet crs2 =processQuery(StrSql, 0);
						crs2.beforeFirst();
						if (crs2.isBeforeFirst()) {
							Str.append("<td valign=top align=left >");
							while (crs2.next()) {
								Str.append("<a href=\"javascript:remote=window.open('jobcard-dash.jsp?jc_id=").append(crs2.getString("jc_id")).append("#tabs-9','reportservice','');remote.focus();\">")
										.append(crs2.getString("jc_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
							}
							Str.append("</td>");
						} else {
							Str.append("<td valign=top align=center>--<br><br></td>");
						}
						crs2.close();
					}
					Str.append("</tr>");
				}
				Str.append("</table><br>");
				// Str.append(getFollowupPriority());
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateServiceExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1' and (emp_service_psf=1 OR emp_service_psf_iacs=1 OR emp_crm=1) and"
					+ " (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + ""
					+ " group by emp_id order by emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String getFollowupPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select * "
					+ " from " + compdb(comp_id) + "axela_service_jc_priority "
					+ " order by priorityjc_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr><th colspan=9>Priority</th></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td align=center><b>#</b></td>\n");
				Str.append("<td align=center><b>Priority</b></td>\n");
				Str.append("<td align=center><b>Description</b></td>\n");
				Str.append("<td align=center><b>Due Hours</b></td>\n");
				Str.append("<td align=center><b>Level 1</b></td>\n");
				Str.append("<td align=center><b>Level 2</b></td>\n");
				Str.append("<td align=center><b>Level 3</b></td>\n");
				Str.append("<td align=center><b>Level 4</b></td>\n");
				Str.append("<td align=center><b>Level 5</b></td>\n");
				Str.append("</tr>");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityjc_desc")).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_duehrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger1_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger2_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger3_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger4_hrs"))).append("</td>\n");
					Str.append("<td align=center>").append(ConvertHoursToDaysHrsMins(crs.getDouble("priorityjc_trigger5_hrs"))).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</table>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
