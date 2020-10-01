package axela.sales;
// modified - 24, 26, 27,28 august 2013
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Email_Penetration extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, soe_ids;
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "", soe_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrTeam = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	public String SearchURL = "report-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				if (!header.equals("no")) {
					CheckSession(request, response);
					emp_id = CNumeric(GetSession("emp_id", request));
					branch_id = CNumeric(GetSession("emp_branch_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
				}
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess + ExeAccess;// / .replace("branch_id", "team_branch_id") + " " + ExeAccess;

					if (!brand_id.equals("") && branch_id.equals("")) {
						StrSearch += " AND branch_brand_id in (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id in (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						StrModel = " and enquiry_model_id in (" + model_id + ")";
					}
					if (!soe_id.equals(""))
					{
						StrSoe = " and enquiry_soe_id in (" + soe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListPenetration();
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
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
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
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");

		// chk_team_lead = PadQuotes(request.getParameter("chk_team_lead"));
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				targetstarttime = starttime.substring(0, 6) + "01000000";
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				targetendtime = endtime.substring(0, 6) + "31000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListPenetration() {
		StringBuilder Str = new StringBuilder();
		int totalenquirycount = 0;
		int totalenqemailcount = 0;
		int totalbookingcount = 0;
		int totalbookemailcount = 0;
		String totalenqemailcountperc = "0";
		String totalbookemailcountperc = "0";

		String grandenqemailcountperc = "0";
		String grandbookemailcountperc = "0";

		StrSql = " SELECT brand_id, brand_name, region_id, region_name, branch_id, branch_name,"
				// retailtarget
				+ " COALESCE((select count(enquiry_id) from " + compdb(comp_id) + "axela_sales_enquiry ";
		if (!team_id.equals("")) {
			mischeck.exe_branch_id = branch_id;
			mischeck.branch_id = branch_id;
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id  "
					// + " INNER JOIN  " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " AND teamtrans_emp_id = enquiry_emp_id"
					+ " AND teamtrans_team_id in (" + team_id + ")";
		}
		StrSql += " WHERE enquiry_branch_id = branch_id "
				+ " AND SUBSTR(enquiry_date, 1,8) >= SUBSTR(" + starttime + ",1,8)"
				+ " AND SUBSTR(enquiry_date,1,8) <= SUBSTR(" + endtime + ",1,8)"
				+ ExeAccess.replace("emp_id", "enquiry_emp_id");
		if (!exe_id.equals("")) {
			StrSql += " AND enquiry_emp_id IN ( " + exe_id + ")";
		}
		if (!model_id.equals("")) {
			StrSql += StrModel;
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		StrSql += "),0) AS enquirycount,"

				+ " COALESCE((select count(enquiry_id) from " + compdb(comp_id) + "axela_sales_enquiry "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON enquiry_customer_id = customer_id ";
		if (!team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = enquiry_emp_id  "
					// + " INNER JOIN  " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " AND teamtrans_emp_id = enquiry_emp_id"
					+ " AND teamtrans_team_id in (" + team_id + ")";
		}
		StrSql += " WHERE enquiry_branch_id = branch_id "
				+ " AND SUBSTR(enquiry_date, 1,8) >= SUBSTR(" + starttime + ",1,8)"
				+ " AND SUBSTR(enquiry_date,1,8) <= SUBSTR(" + endtime + ",1,8)"
				+ " and customer_email1 != ''"
				+ ExeAccess.replace("emp_id", "enquiry_emp_id");
		if (!exe_id.equals("")) {
			StrSql += " AND enquiry_emp_id IN (" + exe_id + ")";
		}
		if (!model_id.equals("")) {
			StrSql += StrModel;
		}
		if (!soe_id.equals("")) {
			StrSql += StrSoe;
		}
		StrSql += "),0) AS enqemailcount,"
				// ///////////////////////////////////////////////////////////////////////////////////////////////
				+ " COALESCE((select count(so_id) from " + compdb(comp_id) + "axela_sales_so ";
		if (!team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id  "
					// + " INNER JOIN  " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " AND teamtrans_emp_id = so_emp_id"
					+ " AND teamtrans_team_id in (" + team_id + ")";
		}
		StrSql += " WHERE so_branch_id = branch_id "
				+ " AND SUBSTR(so_date, 1,8) >= SUBSTR(" + starttime + ",1,8)"
				+ " AND SUBSTR(so_date,1,8) <= SUBSTR(" + endtime + ",1,8)"
				+ " and so_active = 1"
				+ ExeAccess.replace("emp_id", "so_emp_id");
		if (!exe_id.equals("")) {
			StrSql += " AND so_emp_id IN( " + exe_id + ")";
		}
		if (!model_id.equals("")) {
			// StrSql += StrModel;
			StrSql += " AND so_vehstock_id IN(SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " WHERE item_model_id IN(" + model_id + "))";
		}
		if (!soe_id.equals("")) {
			StrSql += " AND so_enquiry_id IN(SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_soe_id IN(" + soe_id + "))";
		}
		StrSql += "),0) AS bookingcount,"

				+ " COALESCE((select count(so_id) from " + compdb(comp_id) + "axela_sales_so "
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON so_customer_id = customer_id";
		if (!team_id.equals("")) {
			StrSql += " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id  "
					// + " INNER JOIN  " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " AND teamtrans_emp_id = so_emp_id"
					+ " AND teamtrans_team_id in (" + team_id + ")";
		}
		StrSql += " WHERE so_branch_id = branch_id "
				+ " AND SUBSTR(so_date, 1,8) >= SUBSTR(" + starttime + ",1,8)"
				+ " AND SUBSTR(so_date,1,8) <= SUBSTR(" + endtime + ",1,8)"
				+ " and so_active = 1 and customer_email1 != ''"
				+ ExeAccess.replace("emp_id", "so_emp_id");
		if (!exe_id.equals("")) {
			StrSql += " AND so_emp_id IN (" + exe_id + ")";
		}
		if (!model_id.equals("")) {
			// StrSql += StrModel;
			StrSql += " AND so_vehstock_id IN(SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " WHERE item_model_id IN(" + model_id + "))";
		}
		if (!soe_id.equals("")) {
			StrSql += " AND so_enquiry_id IN(SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_soe_id IN(" + soe_id + "))";
		}
		StrSql += "),0) AS bookingemailcount"

				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id"
				+ " INNER JOIN axela_brand ON brand_id = branch_brand_id"
				+ " where  1=1 "
				+ StrSearch
				+ " GROUP BY brand_name, region_name, branch_name";
		// + " ORDER BY branch_name, enq.emp_name, so.emp_name";
		// SOP("ListPenetration--------------" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered table-hover\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th align=center data-toggle=\"true\">Principal</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Region</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Enquiry</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Email</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Email %</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Booking</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Email</th>\n");
				Str.append("<th align=center data-hide=\"phone, tablet\">Email %</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					totalenquirycount += crs.getInt("enquirycount");
					totalenqemailcount += crs.getInt("enqemailcount");
					totalbookingcount += crs.getInt("bookingcount");
					totalbookemailcount += crs.getInt("bookingemailcount");
					totalenqemailcountperc = getPercentage((double) crs.getInt("enqemailcount"), (double) crs.getInt("enquirycount"));
					totalbookemailcountperc = getPercentage((double) crs.getInt("bookingemailcount"), (double) crs.getInt("bookingcount"));

					Str.append("<tr align=center valign=top>\n");
					Str.append("<td align=\"left\">").append(crs.getString("brand_name")).append("</td>");
					Str.append("<td align=\"left\">").append(crs.getString("region_name")).append("</td>");
					Str.append("<td align=\"left\">").append(crs.getString("branch_name")).append("</td>");
					Str.append("<td align=\"right\">").append(crs.getString("enquirycount")).append("</td>");
					Str.append("<td align=\"right\">").append(crs.getString("enqemailcount")).append("</td>");
					Str.append("<td align=\"right\">").append(totalenqemailcountperc).append("%").append("</td>");
					Str.append("<td align=\"right\">").append(crs.getString("bookingcount")).append("</td>");
					Str.append("<td align=\"right\">").append(crs.getString("bookingemailcount")).append("</td>");
					Str.append("<td align=\"right\">").append(totalbookemailcountperc).append("%").append("</td>");
					Str.append("</tr>");
				}
				grandenqemailcountperc = getPercentage((double) totalenqemailcount, (double) totalenquirycount);
				grandbookemailcountperc = getPercentage((double) totalbookemailcount, (double) totalbookingcount);
				Str.append("<tr>");
				Str.append("<td colspan=3\" align=\"right\"><b>Total:</b>").append("</td>\n");
				Str.append("<td align=\"right\">").append(totalenquirycount).append("</td>");
				Str.append("<td align=\"right\">").append(totalenqemailcount).append("</td>");
				Str.append("<td align=\"right\">").append(grandenqemailcountperc).append("%").append("</td>");
				Str.append("<td align=\"right\">").append(totalbookingcount).append("</td>");
				Str.append("<td align=\"right\">").append(totalbookemailcount).append("</td>");
				Str.append("<td align=\"right\">").append(grandbookemailcountperc).append("%").append("</td>");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

			} else {
				Str.append("<font color=red><br><br><br><b>No Details Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "select team_id, team_name "
					+ " from " + compdb(comp_id) + "axela_sales_team "
					+ " where team_branch_id=" + dr_branch_id + " "
					+ " group by team_id "
					+ " order by team_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSoe()
	{
		String sb = "";
		try
		{
			StrSql = " select soe_id, soe_name "
					+ " from " + compdb(comp_id) + "axela_soe "
					// + " inner join " + compdb(comp_id) + "axela_inventory_item on item_model_id=model_id"
					+ " order by soe_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				sb = sb + "<option value=" + crs.getString("soe_id") + "";
				sb = sb + ArrSelectdrop(crs.getInt("soe_id"), soe_ids);
				sb = sb + ">" + crs.getString("soe_name") + "</option>\n";
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return sb;
	}

}
