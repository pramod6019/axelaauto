package axela.sales;
//Saiman 16th feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_SOE_SO_Dash extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "0";
	public String[] team_ids, exe_ids, model_ids;
	public String team_id = "", exe_id = "", model_id = "";
	public String StrHTML = "", StrClosedHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String comp_id = "0";
	public String SOSearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					SOSearch = BranchAccess + ExeAccess;

					if (!starttime.equals("")) {
						SOSearch = SOSearch + " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						SOSearch = SOSearch + " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!exe_id.equals("")) {
						SOSearch = SOSearch + " AND so_emp_id in (" + exe_id + ")";
					}
					if (!dr_branch_id.equals("0")) {
						SOSearch = SOSearch + " AND so_branch_id =" + dr_branch_id;
					}
					if (!model_id.equals("")) {
						SOSearch = SOSearch + " AND item_model_id in (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						SOSearch = SOSearch + " AND team_id in (" + team_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = SOSummary();
						StrClosedHTML = SOClosedSummary();
					}
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
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
		exe_ids = request.getParameterValues("dr_executive");

		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");

		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
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
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String SOSummary() {
		int total = 0;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		StringBuilder StrChart = new StringBuilder();
		try {
			StrSql = " SELECT soe_name, "
					+ " count(so_id) as Total"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_soe_id = soe_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so on so_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id AND soitem_rowcount > 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = so_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " WHERE 1 = 1 AND so_active=1 " + SOSearch + ""
					+ " GROUP BY soe_id "
					+ " ORDER BY Total desc";
			// SOP("StrSql in enquirySummary==========" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr>");
				Str.append("<th>SOE</th>");
				Str.append("<th>Total</th>");
				Str.append("</tr>");
				while (crs.next()) {
					total = total + crs.getInt("total");
					Str.append("<tr>");
					Str.append("<td>" + crs.getString("soe_name") + "</td>");
					Str.append("<td align=right>" + crs.getString("total") + "</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>");
				Str.append("<td align=right><b>" + total + "</b></td>");
				Str.append("</tr>");
				Str.append("</table>");
				// //// Build pie chart
				crs.beforeFirst();
				StrChart.append("[");
				while (crs.next()) {
					count++;
					StrChart.append("{'type':'" + crs.getString("soe_name") + "','total':" + crs.getString("Total") + "}");
					StrChart.append(",");
					chart_data_total = chart_data_total + crs.getInt("Total");
					// StrChart.append("['" + crs.getString("soe_name") + " (" + crs.getString("Total") + ")'," + crs.getString("Total") + "]");
					// StrChart.append(",");
				}
				chart_data = StrChart.toString();
				chart_data = chart_data.toString().substring(0, chart_data.lastIndexOf(","));
				chart_data = chart_data + "]";
				// chart_data = StrChart.toString();
				// chart_data = chart_data.toString().substring(0, chart_data.lastIndexOf(","));
				// chart_data = chart_data + "]";
				// SOP("chart===" + chart_data);

			} else {
				Str.append("No Sales Order Found!");
				NoChart = "No Sales Orders Found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String SOClosedSummary() {
		int total = 0;
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = " SELECT soe_name, count(so_id) as Total "
					+ " FROM " + compdb(comp_id) + " axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_soe_id = soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so on so_enquiry_id = enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id AND soitem_rowcount > 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " WHERE 1 = 1 AND so_active=0 " + SOSearch + ""
					+ " GROUP BY soe_id "
					+ " ORDER BY Total desc, soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr>");
				Str.append("<th>SOE</th>");
				Str.append("<th>Total</th>");
				Str.append("</tr>");
				while (crs.next()) {
					total = total + crs.getInt("total");
					Str.append("<tr>");
					Str.append("<td>" + crs.getString("soe_name") + "</td>");
					Str.append("<td align=right>" + crs.getString("total") + "</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right><b>Total: </b></td>");
				Str.append("<td align=right><b>" + total + "</b></td>");
				Str.append("</tr>");
				Str.append("</table>");
			} else {
				Str.append("No Sales Orders Found!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateGroup() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT group_id, group_name"
					+ " FROM" + compdb(comp_id) + " axela_group"
					+ " ORDER BY group_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("group_id"));
				Str.append(ArrSelectdrop(crs.getInt("group_id"), team_ids));
				Str.append(">").append(crs.getString("group_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option>\n)");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name "
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id "
					+ " WHERE emp_active = '1'  AND emp_sales='1' "
					+ " AND (emp_branch_id = " + dr_branch_id + " OR emp_id = 1"
					+ " OR emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";

			if (!team_id.equals("")) {
				StrSql = StrSql + " AND teamtrans_team_id in (" + team_id + ")";
			}
			StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class=selectbox multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + " axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_model_id = model_id "
					+ " WHERE model_active = '1'"
					+ " AND model_sales = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
