package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_LostCase extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "", brand_id = "", region_id = "", dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids;
	public String go = "";
	public String team_id = "";
	public String comp_id = "0";
	public String status_date = "";
	public String enquiry_emp_id = "";
	public String enquiry_id = "";
	public String msg = "";
	public String fromdate = "";
	public String todate = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_opportunity_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				if (go.equals("Go")) {
					CheckForm();
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch = StrSearch + "AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " and teamtrans_team_id IN ( " + team_id + ")";
					}

					StrSearch = StrSearch + " and SUBSTR(enquiry_status_date,1,8) >= SUBSTR('" + ConvertShortDateToStr(fromdate) + "',1,8) "
							+ " AND SUBSTR(enquiry_status_date,1,8) <= SUBSTR('" + ToLongDate(AddHoursDate(StringToDate((ConvertShortDateToStr(todate))), 1, 0, 0)) + "',1,8)";

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = LostCase();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		brand_id = RetrunSelectArrVal(request, "dr_branch");
		brand_ids = request.getParameterValues("dr_branch");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");

		fromdate = PadQuotes(request.getParameter("txt_fromdate"));
		if (fromdate.equals("")) {
			fromdate = strToShortDate(ToShortDate(kknow()));
		}
		todate = PadQuotes(request.getParameter("txt_todate"));

		if (todate.equals("")) {
			todate = strToShortDate(ToShortDate(kknow()));
		}
	}

	public String LostCase() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT enquiry_id, enquiry_date, enquiry_close_date, enquiry_status_date, item_name, stage_name, status_name, enquiry_status_desc,"
					+ " COALESCE(e.emp_id,'0') AS enquiry_emp_id, concat(e.emp_name,' (', e.emp_ref_no, ')') AS enquiry_emp_name,"
					+ " COALESCE(lostcase1_name,'') AS lostcase1_name, COALESCE(lostcase2_name,'') AS lostcase2_name,"
					+ " COALESCE(lostcase3_name,'') AS lostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id = enquiry_lostcase1_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id = enquiry_lostcase2_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_id = enquiry_lostcase3_id"
					+ " WHERE 1=1 "
					// + " AND enquiry_branch_id = 1 "
					+ " AND" + " enquiry_status_id > 2 "
					+ StrSearch + ""
					+ " ORDER BY enquiry_status_date LIMIT 1000";
			// SOP("StrSql=====rept====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Oppr ID</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th data-hide=\"phone\">Variant</th>\n");
				Str.append("<th data-hide=\"phone\">Stage</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Status Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Lost Case 1</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Lost Case 2</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Lost Case 3</th>\n");
				Str.append("</tr>");
				Str.append("</thead>");
				Str.append("<tbody>");
				while (crs.next()) {
					count++;
					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center><a href=enquiry-list.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append(">").append(crs.getString("enquiry_id")).append("</a></td>\n");
					Str.append("<td align=left>");
					Str.append(strToShortDate(crs.getString("enquiry_date"))).append(" - ").append(strToShortDate(crs.getString("enquiry_close_date")));
					Str.append("</td>");
					Str.append("<td align=left>").append(crs.getString("item_name")).append("</td>");
					Str.append("<td align=left>").append(crs.getString("stage_name")).append("</td>");
					Str.append("<td align=left>").append(crs.getString("status_name")).append("<br>");
					Str.append(crs.getString("enquiry_status_desc")).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("enquiry_emp_id")).append(">").append(crs.getString("enquiry_emp_name"))
							.append("</a></td>");
					Str.append("<td align=left>").append(strToShortDate(crs.getString("enquiry_status_date"))).append("</td>");
					Str.append("<td align=left>").append(crs.getString("lostcase1_name")).append("</td>");
					Str.append("<td align=left>").append(crs.getString("lostcase2_name")).append("</td>");
					Str.append("<td align=left>").append(crs.getString("lostcase3_name")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>");
				Str.append("</table>");
				Str.append("</div>");
			} else {
				Str.append("<center><font color=red><b>No Lost Case found!</b></font></center>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_branch_id = " + dr_branch_id
					+ " GROUP BY team_id"
					+ " ORDER BY team_name";
			// SOP("StrSql====team=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_team id=dr_team class=selectbox >");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(StrSelectdrop(crs.getString("team_id"), team_id));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void CheckForm() {
		msg = "";
		if (dr_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (fromdate.equals("")) {
			msg = msg + "<br>Select From Date!";
		} else {
			if (!isValidDateFormatShort(fromdate)) {
				msg = msg + "<br>InValid  From Date!";
			}
		}
		if (todate.equals("")) {
			msg = msg + "<br>Select To Date!";
		} else {
			if (isValidDateFormatShort(todate)) {
				// todate = ConvertShortDateToStr(todate);
				// SOP("todate=="+todate);
				if (!fromdate.equals("") && !todate.equals("") && Long.parseLong(ConvertShortDateToStr(fromdate)) > Long.parseLong(ConvertShortDateToStr(todate))) {
					msg = msg + "<br>From Date should be less than To date!";
				}

				// todate = ToLongDate(AddHoursDate(StringToDate((todate)), 1,
				// 0, 0));
				// SOP("todate2=="+todate);

			} else {
				msg = msg + "<br>Enter Valid To Date!";
				todate = "";
			}
		}

	}
}
