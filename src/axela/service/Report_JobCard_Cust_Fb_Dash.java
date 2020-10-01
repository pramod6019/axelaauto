package axela.service;
// smitha nag 24 june 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_Cust_Fb_Dash extends Connect {

	public String StrSql = "", JCSql = "";
	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public String emp_id = "", branch_id = "0";
	public String[] team_ids, exe_ids, model_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String ExeAccess = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String JCSearch = "";
	public int TotalJCFB = 0;
	public int TotalJC = 0;
	public String go = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				ExeAccess = GetSession("ExeAccess", request);
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					if (!dr_branch_id.equals("0")) {
						JCSearch = JCSearch + " and jc_branch_id =" + dr_branch_id;
					}
					if (!exe_id.equals("")) {
						JCSearch = JCSearch + " and jc_emp_id in (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						JCSearch = JCSearch + " and item_model_id in (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						JCSql = "select count(DISTINCT jc_id) as jccount"
								+ " from " + compdb(comp_id) + "axela_service_jc"
								+ " inner join " + compdb(comp_id) + "axela_service_jc_item on jcitem_jc_id = jc_id"
								+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = jcitem_item_id"
								+ " where jc_fb_entry_id!=0 "
								+ " and jcitem_rowcount!=0"
								+ " and SUBSTR(jc_time_in, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
								+ " and SUBSTR(jc_time_in, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
								+ JCSearch + " ";
						// SOP("Cust FB given----" +JCSql);
						CachedRowSet crs = processQuery(JCSql, 0);
						while (crs.next()) {
							TotalJCFB = crs.getInt("jccount");
						}
						crs.close();
						JCSql = "select count(DISTINCT jc_id) as jccount "
								+ " from " + compdb(comp_id) + "axela_service_jc  "
								+ " inner join " + compdb(comp_id) + "axela_service_jc_item on jcitem_jc_id = jc_id"
								+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id = jcitem_item_id"
								+ " where jc_active = 1 and jc_time_out!=''"
								+ " and jcitem_rowcount!=0"
								+ " and SUBSTR(jc_time_in, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) "
								+ " and SUBSTR(jc_time_in, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
								+ JCSearch + " ";
						// SOP("total sales order----" +JCSql);
						crs = processQuery(JCSql, 0);
						while (crs.next()) {
							TotalJC = crs.getInt("jccount");
						}
						crs.close();
						StrHTML = ListFeedback(request);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		// SOP("exe_id"+exe_id);
		exe_ids = request.getParameterValues("dr_executive");
		// SOP("exe_id..."+exe_ids);
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!";
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListFeedback(HttpServletRequest request) {
		StrSql = "";
		String anscount = "";
		int count = 0;
		int points = 0;
		int totalpoints = 0;
		CachedRowSet crs4 = null;
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "select group_id, group_name "
					+ " from " + compdb(comp_id) + "axela_service_jc_fb_group "
					+ " inner join " + compdb(comp_id) + "axela_service_jc_fb on fb_group_id = group_id"
					+ " where fb_active='1' group by group_name order by group_rank";
			// SOP("StrSql center --" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);
			if (crs1.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=center>\n");
				Str.append("<td colspan=2 align=center><b>Total Job Card: " + TotalJC + "</b></td>\n");
				Str.append("</tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td colspan=2 align=center><b>Total PSF Customer Feedbacks: " + TotalJCFB + "</b></td>\n");
				Str.append("</tr>");
				while (crs1.next()) {
					count = 0;
					Str.append("<tr align=center>\n");
					Str.append("<td colspan=2 align=left><b><font color=red>" + crs1.getString("group_name") + ":</font></b></td>\n");
					Str.append("</tr>");
					StrSql = "select fb_id, fb_query, fb_option1, fb_option2, fb_option3, fb_option4, fb_option5,"
							+ " fb_option_points1, fb_option_points2, fb_option_points3, fb_option_points4, fb_option_points5 "
							+ " from " + compdb(comp_id) + "axela_service_jc_fb "
							+ " where fb_type=1 and fb_active='1' and fb_group_id=" + crs1.getString("group_id") + ""
							+ " group by fb_id";
					// SOP("StrSql==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						points = 0;
						count++;
						Str.append("<tr align=center>\n");
						Str.append("<td align=center width=5%><b>" + count + ".</b></td>\n");
						Str.append("<td align=left>" + crs.getString("fb_query") + "</td>\n");
						Str.append("</tr>");
						Str.append("<tr align=center>\n");
						Str.append("<td align=left>&nbsp;</td><td align=left>\n");
						for (int i = 1; i <= 5; i++) {
							if (!crs.getString("fb_option" + i + "").equals("")) {
								Str.append("&nbsp;" + crs.getString("fb_option" + i + "") + "");
								StrSql = "select count(fbtrans_id) as anscount "
										+ " from " + compdb(comp_id) + "axela_service_jc_fb_trans "
										+ " where fbtrans_fb_id=" + crs.getString("fb_id")
										+ " and fbtrans_option" + i + " = 1 "
										+ " and fbtrans_jc_id in (" + JCSql.replace("count(DISTINCT jc_id) as jccount", "jc_id") + ")";
								// SOP("strsql fffffff..." + StrSql);
								crs4 = processQuery(StrSql, 0);
								while (crs4.next()) {
									anscount = crs4.getString("anscount");
								}
								crs4.close();
								// anscount = ExecuteQuery(StrSql);
								Str.append("&nbsp;=&nbsp;<b>" + getPercentage(TotalJCFB, Integer.parseInt(anscount))
										+ "%&nbsp;&nbsp;" + Integer.parseInt(crs.getString("fb_option_points" + i + "")) * Integer.parseInt(anscount) + "</b><br>");
								points = points + Integer.parseInt(crs.getString("fb_option_points" + i + "")) * Integer.parseInt(anscount);
								totalpoints = totalpoints + Integer.parseInt(crs.getString("fb_option_points" + i + "")) * Integer.parseInt(anscount);
							}
						}
						Str.append("Total Points: <b>" + points + "</b>");
						Str.append("</td>");
						Str.append("</tr>");
					}
					crs.close();
				}
				Str.append("<tr><td colspan=2 align=center><b>Total Points: " + totalpoints + "</b></td></tr>");
				Str.append("</table>");
			} else {
				Str.append("<font color=red><b>No Questions(s) found!</b></font>");
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
					+ " WHERE emp_active = '1' and emp_service='1' and (emp_branch_id = " + dr_branch_id + " or emp_id = 1"
					+ " or emp_id in (SELECT empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess
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

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select model_id, model_name  "
					+ " from " + compdb(comp_id) + "axela_inventory_item_model  "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_model_id=model_id  "
					+ " group by model_id order by model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateCountry==========" + StrSql);
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("model_id") + "");
				Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
				Str.append(">" + crs.getString("model_name") + "</option>\n");
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
