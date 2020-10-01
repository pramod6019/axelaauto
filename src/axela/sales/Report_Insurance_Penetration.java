package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insurance_Penetration extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids;
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String StrSearch = "";
	// public String SearchURL = "report-monitoring-board-search.jsp";
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));

				if (go.equals("")) {
					start_time = DateToShortDate(kknow());
					end_time = DateToShortDate(kknow());
					msg = "";
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					StrSearch = BranchAccess.replace("branch_id", "emp_branch_id");
					StrSearch += ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += "AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch += " AND team_id IN (" + team_id + ") ";
					}
					if (!exe_id.equals("")) {
						StrSearch += " AND emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND model_id IN (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListInsurancePenetration();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg += "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);

			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListInsurancePenetration() {

		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT emp_id, CONCAT(emp_name,' (', emp_ref_no,')') AS emp_name, emp_active,"
				+ " COUNT(so_id) AS socount,"
				+ " SUM(IF(so_insur_amount != 0, 1, 0)) AS insurgivencount,"
				+ " SUM(IF(so_insur_amount = 0, 1, 0)) AS insurnotgivencount"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id";
		if (!branch_id.equals("") || !region_id.equals("") || !brand_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id";
		}
		if (!team_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id ";
		}
		if (!model_id.equals("")) {
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
		}
		StrSql += " WHERE 1 = 1"
				+ " AND so_active = 1"
				+ " AND emp_active = 1"
				+ " AND emp_id != 1"
				+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
				+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
				+ StrSearch
				+ " GROUP BY so_emp_id"
				+ " ORDER BY emp_name";

		// SOP("Query-------ListFinacePenetration-----------" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				// Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\" id='table'>\n");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th >Executive</th>\n");
				Str.append("<th >SO Count</th>\n");
				Str.append("<th data-hide=\"phone\">Insurance Done</th>\n");
				Str.append("<th data-hide=\"phone\">Insurance Not Done</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				int total_so_count = 0, total_insurgiven_count = 0, total_insurnotgiven_count = 0;

				while (crs.next()) {
					count++;
					total_so_count += crs.getInt("socount");
					total_insurgiven_count += crs.getInt("insurgivencount");
					total_insurnotgiven_count += crs.getInt("insurnotgivencount");

					Str.append("<tr>");
					Str.append("<td align='center'>").append(count).append("</td>");
					Str.append("<td><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id"))
							.append("\">").append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td align='right'>").append(crs.getString("socount")).append("</td>");
					Str.append("<td align='right'>").append(crs.getString("insurgivencount")).append("</td>");
					Str.append("<td align='right'>").append(crs.getString("insurnotgivencount")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td></td>");
				Str.append("<td align='right'><b>Total:</b></td>");
				Str.append("<td align='right'><b>").append(total_so_count).append("</b></td>");
				Str.append("<td align='right'><b>").append(total_insurgiven_count).append("</b></td>");
				Str.append("<td align='right'><b>").append(total_insurnotgiven_count).append("</b></td>");
				Str.append("</tr>");
				Str.append("</table>");
				// Str.append("</div>");
			} else {
				Str.append("<font color=red><br><br><br><b>No Details Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id = " + dr_branch_id + " "
					+ " AND team_active = 1"
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
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

}
