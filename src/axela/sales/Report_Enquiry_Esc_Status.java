package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Enquiry_Esc_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String[] team_ids, exe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "";
	public String branch_id = "", brand_id = "", region_id = "";
	public String comp_id = "0";
	public String msg = "";
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access, emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				// dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();

				if (go.equals("Go")) {
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += " AND enquiry_branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";

					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND emp_id IN (SELECT teamtrans_emp_id "
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					// SOP("StrSearch---"+StrSearch);
					if (msg.equals("")) {
						StrHTML = EnquiryTriggerStatus();
					} else {
						msg = "Error!" + msg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
	}

	public String EnquiryTriggerStatus() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT branch_id, concat(branch_name,' (',branch_code,')') AS branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE branch_active = '1' ";
			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + ")";
			}
			StrSql += " ORDER BY branch_name ";
			// SOP("StrSql-----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">");
				while (crs1.next()) {
					Str.append("<thead><tr>\n");
					Str.append("<th style=\"text-align:center\" data-toggle=\"true\" colspan=5>").append(crs1.getString("branch_name")).append("</th></tr>");
					Str.append("<tr align=center>\n");
					Str.append("<td data-hide=\"phone\"><b>Level 1</b></td>\n");
					Str.append("<td data-hide=\"phone\"><b>Level 2</b></td>\n");
					Str.append("<td data-hide=\"phone\"><b>Level 3</b></td>\n");
					Str.append("<td data-hide=\"phone\"><b>Level 4</b></td>\n");
					Str.append("<td data-hide=\"phone\"><b>Level 5</b></td>\n");
					Str.append("</tr>");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("<tr align=center>\n");
					for (int i = 1; i <= 5; i++) {
						StrSql = " SELECT enquiry_id, concat(emp_name,' (',emp_ref_no,')') AS emp_name "
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
								+ " WHERE 1 = 1 "
								+ " AND enquiry_trigger=" + i + " AND enquiry_status_id = 1 "
								+ StrSearch + BranchAccess.replace("branch_id", "enquiry_branch_id");

						StrSql += ExeAccess.replace("emp_id", "enquiry_emp_id");

						StrSql += " GROUP BY enquiry_id "
								+ " ORDER BY emp_name, enquiry_id";
						// SOP("StrSql-----2------" + StrSqlBreaker(StrSql));
						CachedRowSet crs2 = processQuery(StrSql, 0);
						if (crs2.isBeforeFirst()) {
							Str.append("<td valign=top align=left >");
							while (crs2.next()) {
								Str.append("<a href=\"javascript:remote=window.open('enquiry-dash.jsp?enquiry_id=").append(crs2.getString("enquiry_id"))
										.append("','reportsales','');remote.focus();\">").append(crs2.getString("enquiry_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
								// Str.append("<a href=enquiry-dash.jsp?enquiry_id=").append(crs2.getString("enquiry_id")).append(">").append(crs2.getString("enquiry_id")).append(": ").append(crs2.getString("emp_name")).append("</a><br>");
							}
							Str.append("</td>");
						} else {
							Str.append("<td valign=top align=center>--<br><br></td>");
						}
						crs2.close();
					}
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
				Str.append(getPriority());
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

	public String getPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
					+ " ORDER BY priorityenquiry_name";
			// SOP("StrSql=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"  table-bordered\"><br>\n");
				Str.append("<table class=\"table table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th style=\"text-align:center\" data-toggle=\"true\" colspan=9>Enquiry Priority</th></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>#</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>Priority</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=center><b>Description</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Due Hours</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 1</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 2</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 3</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 4</b></td>\n");
				Str.append("<td data-hide=\"phone, tablet\"><b>Level 5</b></td>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityenquiry_name")).append("</td>\n");
					Str.append("<td align=left>").append(crs.getString("priorityenquiry_desc")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityenquiry_duehrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityenquiry_trigger1_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityenquiry_trigger2_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityenquiry_trigger3_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityenquiry_trigger4_hrs")).append("</td>\n");
					Str.append("<td align=center>").append(crs.getString("priorityenquiry_trigger5_hrs")).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
