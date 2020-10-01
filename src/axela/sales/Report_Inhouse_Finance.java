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

public class Report_Inhouse_Finance extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, soe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "", soe_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
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
	public String dr_totalby = "0";
	public String emp_all_exe = "";
	// public String TeamSql = "";
	public String filter = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_so_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				// SOP("===="+getPercentage(100, 10));
				// SOP("header = " + header);
				if (!header.equals("no")) {
					CheckSession(request, response);
					emp_id = CNumeric(GetSession("emp_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
					filter = PadQuotes(request.getParameter("filter"));
				}
				GetValues(request, response);
				CheckForm();
				// SOP("go--------------" + go);

				if (filter.equals("yes")) {

					CheckRedirect(request, response);
				}

				if (go.equals("Go")) {
					StrSearch = BranchAccess.replace("branch_id", "so_branch_id") + " " + ExeAccess;

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + "AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND item_model_id IN (" + model_id + ")";
					}
					if (!soe_id.equals("")) {
						StrSearch += " AND enquiry_soe_id IN (" + soe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}

					if (msg.equals("")) {
						StrHTML = ListData();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		soe_id = RetrunSelectArrVal(request, "dr_soe");
		soe_ids = request.getParameterValues("dr_soe");
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				// SOP("startime====" + starttime);
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
				if (!starttime.equals("") && !endtime.equals("")
						&& Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				targetendtime = endtime.substring(0, 6) + "31000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListData() {
		int count = 0;
		int socount = 0;
		double amount = 0.0;
		StringBuilder Str = new StringBuilder();
		StringBuilder StrHead = new StringBuilder();
		StrSql = "SELECT COUNT(so_id) AS socount,"
				+ " SUM(so_finance_amt) AS so_finance_amt, fincomp_name, fincomp_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_finance_comp ON fincomp_id = so_fincomp_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
				+ " AND soitem_rowcount != 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id  "
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id "
				// + StrModel.replace("model_id", "item_model_id")
				+ " WHERE so_active='1'"
				+ " AND so_finance_amt != 0 "
				+ StrSearch + ""
				+ " AND substr(so_date, 1, 8) >= substr('" + starttime + "', 1 ,8) "
				+ " AND substr(so_date, 1, 8) <= substr('" + endtime + "', 1, 8) "
				+ " AND so_fintype_id = 1"
				+ " GROUP BY fincomp_id "
				+ " ORDER BY so_finance_amt DESC";
		// SOP("Inhouse finance query--------------------- " + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<td valign=top>");
				if (!header.equals("no")) {
					Str.append("<div class=\"table-bordered table-hover\">\n");
					Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				} else {
					Str.append("<div class=\"table-bordered\">\n");
					Str.append("<table class=\"table  \" data-filter=\"#filter\" style=\"border-collapse:collapse;border-color:#726a7a;padding:3px;\">\n");
				}
				StrHead.append("<tr>\n");
				StrHead.append("<thead><tr>\n");
				StrHead.append("<th style=\"text-align:center\">#</th>\n");
				StrHead.append("<th data-toggle=\"true\">Bank</th>\n");
				StrHead.append("<th data-hide=\"phone\">Sales Orders</th>\n");
				StrHead.append("<th data-hide=\"phone\">Amount</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");
				Str.append(StrHead.toString());

				while (crs.next()) {
					count++;
					socount += crs.getInt("socount");
					amount = amount + crs.getDouble("so_finance_amt");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td align=left>" + crs.getString("fincomp_name") + "</td>\n");
					Str.append("<td align=right><a href=\"../sales/report-inhouse-finance.jsp?filter=yes"
							+ "&fincomp_id=" + crs.getInt("fincomp_id")
							+ "&starttime=" + starttime
							+ "&endtime=" + endtime
							+ "&brand_id=" + brand_id
							+ "&region_id=" + region_id
							+ "&branch_id=" + branch_id
							+ "&model_id=" + model_id
							+ "&team_id=" + team_id
							+ "&exe_id=" + exe_id
							+ "&soe=" + soe_id
							+ "\" target=\"_blank\">" + crs.getInt("socount") + "</a></td>\n");
					Str.append("<td align=right>" + IndFormat((crs.getDouble("so_finance_amt") + "")) + "</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td colspan=2 align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b><a href=\"../sales/report-inhouse-finance.jsp?filter=yes"
						+ "&starttime=" + starttime
						+ "&endtime=" + endtime
						+ "&brand_id=" + brand_id
						+ "&region_id=" + region_id
						+ "&branch_id=" + branch_id
						+ "&model_id=" + model_id
						+ "&team_id=" + team_id
						+ "&exe_id=" + exe_id
						+ "&soe=" + soe_id
						+ "\" target=\"_blank\">" + socount + "</a></b></td>\n");
				Str.append("<td align=right><b>" + IndFormat(amount + "") + "</b></td>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Records Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSoe() {
		String sb = "";
		try {
			StrSql = " SELECT soe_id, soe_name "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_model_id=model_id"
					+ " ORDER BY soe_name ";
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
	protected void CheckRedirect(HttpServletRequest request, HttpServletResponse response) throws IOException {

		starttime = PadQuotes(request.getParameter("starttime"));
		endtime = PadQuotes(request.getParameter("endtime"));
		brand_id = PadQuotes(request.getParameter("brand_id"));
		region_id = PadQuotes(request.getParameter("region_id"));
		branch_id = PadQuotes(request.getParameter("branch_id"));
		exe_id = PadQuotes(request.getParameter("exe_id"));
		team_id = PadQuotes(request.getParameter("team_id"));
		model_id = PadQuotes(request.getParameter("model_id"));
		soe_id = PadQuotes(request.getParameter("soe_id"));
		String fincomp_id = PadQuotes(request.getParameter("fincomp_id"));

		StrSearch = " AND so_active = '1'"
				+ " AND so_finance_amt != 0"
				+ " AND so_fintype_id = 1 ";

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
			StrSearch = StrSearch + " AND team_id IN (" + team_id + ")";
		}
		if (!exe_id.equals("")) {
			StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
		}
		if (!model_id.equals("")) {
			StrSearch += " AND item_model_id IN (" + model_id + ")";
		}
		if (!soe_id.equals("")) {
			StrSearch += " AND enquiry_soe_id IN (" + soe_id + ")";

		}
		if (!starttime.equals("") && !endtime.equals("")) {
			StrSearch += " AND SUBSTR(so_date, 1, 8) >= SUBSTR(" + starttime + ", 1, 8)"
					+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR(" + endtime + ", 1, 8)";
		}

		if (!fincomp_id.equals("")) {
			StrSearch += " AND so_fincomp_id = " + fincomp_id;
		}

		// SOP("StrSearch----" + StrSearch);
		SetSession("sostrsql", StrSearch, request);
		response.sendRedirect("../sales/veh-salesorder-list.jsp?smart=yes");
	}
}
