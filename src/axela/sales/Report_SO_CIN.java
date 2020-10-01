package axela.sales;
//divya 30th may 2013
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_SO_CIN extends Connect {

	public static String msg = "";
	public String emp_id = "", comp_id = "0";
	public String brand_id = "", region_id = "";
	public String[] brand_ids, region_ids, exe_ids, model_ids;
	public String branch_id = "";
	public String branch_name = "";
	public String so_id = "";
	public String[] branch_ids, team_ids;
	public String team_id = "", crm_emp_id = "", model_id = "", exe_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "";
	public String EmpSearch = "";
	public String ExeAccess = "";
	public String go = "";
	public int TotalRecords = 0;
	public String StrSql = "";
	public String TeamJoin = "", TeamSearch = "";
	public String emp_all_exe = "";
	public String chart_data = "";
	public String cinstatuschart_data = "";
	public int chart_data_total = 0;
	public int cinstatuschart_data_total = 0;
	public String filter = "", StrFilter = "";

	HashMap<String, Integer> cinconcernname = new HashMap<String, Integer>();

	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	DecimalFormat deci = new DecimalFormat("###.##");
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				filter = PadQuotes(request.getParameter("filter"));
				if (branch_id.equals("0")) {
					dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				} else {
					dr_branch_id = branch_id;
				}
				if (filter.equals("yes")) {
					SalesOrderDetails(request, response);
				}

				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {

					if (!brand_id.equals("") && branch_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}

					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND item_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						TeamJoin = " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id ";
						TeamSearch = " AND team_id IN (" + team_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN (" + exe_id + " )";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = CINStatusDetails();

					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		CheckSession(request, response);

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_brand");
		brand_ids = request.getParameterValues("dr_brand");
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
		if (!branch_id.equals("")) {
			StrSearch += " AND so_branch_id IN( " + branch_id + ")";
		}
		if (!region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + region_id + ") ";
		}
		// if (brand_id.equals("")) {
		// msg += "<br>Select Brand!";
		// }
	}

	public String CINStatusDetails() {
		try {
			String StrSqlcount = "";
			int count = 0;
			double rowcount = 0;
			double totalrowcount = 0;
			HashMap<String, String> cinname = new LinkedHashMap<String, String>();

			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT COALESCE(cinstatus_id, 0) AS 'cinstatus_id',"
					+ " COALESCE(cinstatus_name, '') AS 'cinstatus_name'"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cin_status"
					+ " WHERE 1 = 1"
					+ " AND cinstatus_id != 1"
					+ " GROUP BY cinstatus_id"
					+ " ORDER BY cinstatus_rank";

			CachedRowSet crstrconcernsql = processQuery(StrSql, 0);
			// SOP("crstrconcernsql====" + StrSql);
			while (crstrconcernsql.next()) {
				StrSqlcount += "SUM(IF(cinstatus_id = " + crstrconcernsql.getString("cinstatus_id") + ",1,0))" + " AS '" + crstrconcernsql.getString("cinstatus_name") + "',";
				cinname.put(crstrconcernsql.getString("cinstatus_name"), "0");
			}

			if (!StrSqlcount.equals("")) {
				StrSqlcount = StrSqlcount.substring(0, StrSqlcount.length() - 1);
			}
			// SOP("StrSqlcount====" + StrSqlcount);

			StrSql = " SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, "
					+ " COUNT(DISTINCT so_id) AS socount,"
					+ " COUNT(DISTINCT cinstatus_id) AS 'cincount',"
					+ StrSqlcount
					+ " FROM " + compdb(comp_id) + "axela_sales_so "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_cin_status ON cinstatus_id = so_cinstatus_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id ";
			if (!TeamSearch.equals("")) {
				StrSql += TeamJoin;
			}
			StrSql += " WHERE 1 = 1 "
					+ " AND emp_active = 1"
					+ " AND so_active = 0"
					+ " AND so_cinstatus_id != 1"
					+ StrSearch;
			if (!TeamSearch.equals("")) {
				StrSql += TeamSearch;
			}
			StrSql += BranchAccess.replace("branch_id", "so_branch_id");
			StrSql += ExeAccess.replace("emp_id", "so_emp_id");

			StrSql += " GROUP BY emp_id"
					// + " HAVING SUM(IF(cinstatus_id = 2, 1, 0)) != 0"
					// + " OR SUM(IF(cinstatus_id = 3, 1, 0)) != 0"
					// + " OR SUM(IF(cinstatus_id = 4, 1, 0)) != 0"

					+ " ORDER BY emp_name";
			// SOP("StrSql------222-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" style=\"text-align:center\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Sales Consultant</th>\n");
				crstrconcernsql.beforeFirst();
				while (crstrconcernsql.next()) {
					Str.append("<th data-hide=\"phone\">" + crstrconcernsql.getString("cinstatus_name") + "</th>\n");
				}
				Str.append("<th data-hide=\"phone\">Total</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				HashMap<String, Integer> cinstatustotal = new HashMap<>();
				crstrconcernsql.beforeFirst();

				while (crstrconcernsql.next()) {
					// for (int i = 1; i <=
					// strconcernsql.getInt("concerntypecount"); i++) {
					cinstatustotal.put(crstrconcernsql.getString("cinstatus_name"), 0);
					// }
				}
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id"))
							.append(">").append(crs.getString("emp_name")).append("</a></td>\n");

					crstrconcernsql.beforeFirst();
					while (crstrconcernsql.next()) {
						rowcount += crs.getInt(crstrconcernsql.getString("cinstatus_name"));

						Str.append("<td align=right>").append("<a href=../sales/report-so-cin.jsp?filter=yes")
								.append("&emp_id=").append(crs.getString("emp_id")).append("&")
								.append("cinstatusid=").append(crstrconcernsql.getString("cinstatus_id")).append("&")
								.append("brand=").append(brand_id).append("&")
								.append("region_id=").append(region_id).append("&")
								.append("branch_id=").append(branch_id).append("&")
								.append("team_id=").append(team_id).append("&")
								.append("model_id=").append(model_id)
								.append(">").append(crs.getInt(crstrconcernsql.getString("cinstatus_name")))
								.append("</a></td>\n");
						cinstatustotal.put(crstrconcernsql.getString("cinstatus_name") + "",
								cinstatustotal.get(crstrconcernsql.getString("cinstatus_name")
										+ "") + crs.getInt(crstrconcernsql.getString("cinstatus_name")));
						// SOP("cinstatus_name===" + crstrconcernsql.getString("cinstatus_name"));
					}
					Str.append("<td align=right>").append("<a href=../sales/report-so-cin.jsp?filter=yes")
							.append("&emp_id=").append(crs.getString("emp_id"))
							.append("&").append("brand=").append(brand_id)
							.append("&").append("region_id=").append(region_id)
							.append("&").append("branch_id=").append(branch_id)
							.append("&").append("team_id=").append(team_id)
							.append("&").append("model_id=").append(model_id)
							.append(">").append(rowcount)
							.append("</a></td>\n");
					Str.append("</tr>");
					totalrowcount += rowcount;
					rowcount = 0;

				}
				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");

				crstrconcernsql.beforeFirst();
				while (crstrconcernsql.next()) {
					// for (int i = 1; i <=
					// strconcernsql.getInt("concerntypecount"); i++) {
					Str.append("<td align=right><b>").append("<a href=../sales/report-so-cin.jsp?filter=yes")
							.append("&").append("cinstatusid=").append(crstrconcernsql.getString("cinstatus_id"))
							.append("&").append("brand=").append(brand_id)
							.append("&").append("region_id=").append(region_id)
							.append("&").append("branch_id=").append(branch_id)
							.append("&").append("team_id=").append(team_id)
							.append("&").append("model_id=").append(model_id)
							.append(">").append(cinstatustotal.get(crstrconcernsql.getString("cinstatus_name")))
							.append("</b></td>\n");
					cinconcernname.put(crstrconcernsql.getString("cinstatus_name"), cinstatustotal.get(crstrconcernsql.getString("cinstatus_name")));
					// }
				}

				Str.append("<td align=right><b>").append("<a href=../sales/report-so-cin.jsp?filter=yes")
						.append("&emp_id=")
						.append("&").append("cinstatusid=")
						.append("&").append("brand=").append(brand_id)
						.append("&").append("region_id=").append(region_id)
						.append("&").append("branch_id=").append(branch_id)
						.append("&").append("model_id=").append(model_id)
						.append(">").append(totalrowcount)
						.append("</a></b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				PreparePieChart();
			} else {
				Str.append("<font color=red><center><b>No CIN Status Found!</b></center></font><br>");
			}
			crs.close();
			crstrconcernsql.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PreparePieChart() {
		chart_data = "[";
		for (String str : cinconcernname.keySet()) {
			chart_data = chart_data
					+ "{'type': '" + str
					+ "', 'total':" + cinconcernname.get(str)
					+ "}";
			chart_data_total = chart_data_total + cinconcernname.get(str);
			chart_data = chart_data + ",";
		}
		chart_data = chart_data + "]";
		return "";
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + " axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
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

	public String PopulateExecutive() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales = '1'";

			if (!emp_id.equals("1")) {
				StrSql += " " + ExeAccess + "";
			}
			StrSql = StrSql + " ORDER BY emp_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=textbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(" )").append("</option> \n");
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
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDeliveryStatus(String[] deliverystatus_ids, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT delstatus_id, delstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " WHERE delstatus_id != 0"
					+ " GROUP BY delstatus_id"
					+ " ORDER BY delstatus_name";
			// SOP("StrSql-----------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("delstatus_id"));
				Str.append(ArrSelectdrop(crs.getInt("delstatus_id"), deliverystatus_ids));
				Str.append(">").append(crs.getString("delstatus_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType(String[] fueltype_ids, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1 = 1"
					+ " GROUP BY fueltype_id"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id"));
				Str.append(ArrSelectdrop(crs.getInt("fueltype_id"), fueltype_ids));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	private void SalesOrderDetails(HttpServletRequest request, HttpServletResponse response) {

		try {
			HttpSession session = request.getSession(true);
			String brand_ids = PadQuotes(RetrunSelectArrVal(request, "brand_id"));
			String region_ids = PadQuotes(RetrunSelectArrVal(request, "region_id"));
			String branch_ids = PadQuotes(RetrunSelectArrVal(request, "branch_id"));
			String model_ids = PadQuotes(RetrunSelectArrVal(request, "model_id"));
			String team_ids = PadQuotes(RetrunSelectArrVal(request, "team_id"));
			String emp_ids = PadQuotes(RetrunSelectArrVal(request, "emp_id"));
			String cinstatusid = PadQuotes(RetrunSelectArrVal(request, "cinstatusid"));

			// Brand
			if (!brand_ids.equals("")) {
				StrFilter += " AND branch_brand_id IN (" + brand_ids + ") ";
			}
			// Regions
			if (!region_ids.equals("")) {
				StrFilter += " AND branch_region_id IN (" + region_ids + ") ";
			}
			// Branch
			if (!brand_ids.equals("")) {
				StrFilter += " AND so_branch_id IN (" + branch_ids + ") ";
			}
			// Models
			if (!brand_ids.equals("")) {
				StrFilter += " AND item_model_id IN (" + model_ids + ") ";
			}
			// Teams
			if (!team_ids.equals("")) {
				StrFilter += " AND team_id IN (" + team_ids + ") ";
			}
			// Sales Consultant
			if (!emp_ids.equals("")) {
				StrFilter += " AND emp_id IN (" + emp_ids + ") ";
			}
			// Cin Status
			if (!cinstatusid.equals("")) {
				StrFilter += " AND so_cinstatus_id IN (" + cinstatusid + ") ";
			} else {
				StrFilter += " AND so_cinstatus_id > 1 ";
			}
			StrFilter += " AND so_active = 0";
			// SOP("StrSearch====" + StrFilter);
			SetSession("sostrsql", StrFilter, request);
			response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
