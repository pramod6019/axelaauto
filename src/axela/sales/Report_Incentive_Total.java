package axela.sales;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Incentive_Total extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public String include_inactive_exe = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", emp_copy_access = "0", dr_branch_id = "0";
	public String go = "", filter = "", chk_team_lead = "0";
	public String dr_totalby = "0", dr_orderby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String StrSearch = "", StrFilter = "";
	public String emp_all_exe = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	public int years = 0;
	public int months = 0;
	public int days = 0;
	public int soachived = 0;
	public double incentive_variant = 0.00;
	public double incentive_vehstock = 0.00;
	public double incentive_soslab = 0.00;
	public double incentive_insurslab = 0.00;
	public double incentive_ewslab = 0.00;
	public double incentive_financeslab = 0.00;
	public double incentive_accessoriessslab = 0.00;
	public double incentive_exchangeslab = 0.00;
	public double incentive_overall = 0.00;
	public double incentive_ew = 0.00;
	public double incentive_insur = 0.00;
	public double incentive_finance = 0.00;
	public double incentive_accessories = 0.00;
	public double incentive_exchange = 0.00;
	public double incentive_total = 0.00;
	public ArrayList dr_totalby_ids = new ArrayList();

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
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				filter = PadQuotes(request.getParameter("filter"));
				emp_copy_access = ReturnPerm(comp_id, "emp_copy_access", request);

				if (go.equals("")) {
					start_time = DateToShortDate(kknow());
					end_time = DateToShortDate(kknow());
					msg = "";
				}

				if (filter.equals("yes")) {
					RedirectSOList(request, response);
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					StrSearch = BranchAccess.replace("branch_id", "team_branch_id");
					if (emp_all_exe.equals("0")) {
						StrSearch += ExeAccess;
					}
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
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ") ";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private String ListData() throws SQLException {
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;

		String mainGroupBy = "";
		String mainOrderBy = "";
		String fields = "";

		switch (dr_totalby) {
			case "1" :
				mainGroupBy = " emp_id";
				mainOrderBy = " emp_name";
				fields = " emp_id, emp_name, emp_ref_no, ";
				break;

			case "2" :
				mainGroupBy = " team_id";
				mainOrderBy = " team_name";
				fields = " team_id, team_name, ";
				StrSearch += " AND team_active = 1";
				break;

			case "3" :
				mainGroupBy = " branch_id";
				mainOrderBy = " branch_id";
				fields = " branch_id, branch_name, ";
				break;

			case "4" :
				mainGroupBy = " region_id";
				mainOrderBy = " region_name";
				fields = " region_id, region_name, ";
				break;
			case "5" :
				mainGroupBy = " brand_id";
				mainOrderBy = " brand_name";
				fields = " brand_id, brand_name, ";
				break;
		}

		if (dr_totalby.equals("2")
				|| !team_id.equals("")) {
			SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id";

		}

		// if (dr_totalby.equals("3")
		// || dr_totalby.equals("4")
		// || dr_totalby.equals("5")
		// || !branch_id.equals("")
		// || !region_id.equals("")
		// || !brand_id.equals("")) {
		// SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
		// }
		if (dr_totalby.equals("4")) {
			SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
		}
		if (dr_totalby.equals("5")) {
			SqlJoin += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
		}

		StrSql = " SELECT " + fields
				+ " COALESCE (SUM(soachieved), 0) AS soachieved,"
				+ " COALESCE (SUM(incentive_variant), 0) AS incentive_variant,"
				+ " COALESCE (SUM(incentive_vehstock), 0) AS incentive_vehstock,"
				+ " COALESCE (SUM(incentive_ew), 0) AS incentive_ew,"
				+ " COALESCE (SUM(incentive_insur), 0) AS incentive_insur,"
				+ " COALESCE (SUM(incentive_finance), 0) AS incentive_finance,"
				+ " COALESCE (SUM(incentive_accessories),0) AS incentive_accessories,"
				+ " COALESCE (SUM(incentive_exchange), 0) AS incentive_exchange,"
				+ " COALESCE (SUM(incentive_soslab), 0) AS incentive_soslab,"
				+ " COALESCE (SUM(incentive_insurslab), 0) AS incentive_insurslab,"
				+ " COALESCE (SUM(incentive_ewslab), 0) AS incentive_ewslab,"
				+ " COALESCE (SUM(incentive_financeslab), 0) AS incentive_financeslab,"
				+ " COALESCE (SUM(incentive_accessoriessslab), 0) AS incentive_accessoriessslab,"
				+ " COALESCE (SUM(incentive_exchangeslab), 0) AS incentive_exchangeslab,"
				+ " COALESCE (SUM(incentive_overall), 0) AS incentive_overall,"
				+ " COALESCE (SUM(incentive_total), 0) AS incentive_total"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = incentive_emp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
				+ " LEFT JOIN (SELECT COUNT(so_id) AS soachieved, so_emp_id FROM " + compdb(comp_id) + "axela_sales_so"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(so_retail_date, 1, 6) = SUBSTR(" + starttime + ", 1, 6)"
				+ " GROUP BY so_emp_id )"
				+ " tblso ON tblso.so_emp_id = incentive_emp_id"
				+ SqlJoin
				+ " WHERE 1=1"
				+ StrSearch
				+ " AND SUBSTR(incentive_startdate, 1, 6) >= SUBSTR(" + starttime + ", 1, 6)"
				+ " AND SUBSTR(incentive_startdate, 1, 6) <= SUBSTR(" + endtime + ", 1, 6)";
		StrSql += " AND emp_sales = '1' "
				+ " AND branch_active = '1'";
		if (!include_inactive_exe.equals("1")) {
			StrSql += " AND emp_active = 1 ";
		}
		StrSql += " GROUP BY " + mainGroupBy
				+ " ORDER BY " + mainOrderBy;
		SOP("StrSql==" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");

			switch (dr_totalby) {
				case "1" :
					StrHead.append("<th>Sales Consultant</th>\n");
					break;
				case "2" :
					StrHead.append("<th>Team</th>\n");
					break;
				case "3" :
					StrHead.append("<th>Branch</th>\n");
					break;
				case "4" :
					StrHead.append("<th>Region</th>\n");
					break;
				case "5" :
					StrHead.append("<th>Brand</th>\n");
					break;
			}
			StrHead.append("<th data-hide=\"phone\">SO</th>\n");
			StrHead.append("<th data-hide=\"phone\">Variant</th>\n");
			StrHead.append("<th data-hide=\"phone\">VIN</th>\n");
			StrHead.append("<th data-hide=\"phone\">SO Slab</th>\n");
			StrHead.append("<th data-hide=\"phone\">Insurance Slab</th>\n");
			StrHead.append("<th data-hide=\"phone\">EW Slab</th>\n");
			StrHead.append("<th data-hide=\"phone\">Finance Slab</th>\n");
			StrHead.append("<th data-hide=\"phone\">Accessories Slab</th>\n");
			StrHead.append("<th data-hide=\"phone\">Exchange Slab</th>\n");
			StrHead.append("<th data-hide=\"phone\">Overall Target</th>\n");
			StrHead.append("<th data-hide=\"phone\">EW</th>\n");
			StrHead.append("<th data-hide=\"phone\">Insurance</th>\n");
			StrHead.append("<th data-hide=\"phone\">Finance</th>\n");
			StrHead.append("<th data-hide=\"phone\">Accessories</th>\n");
			StrHead.append("<th data-hide=\"phone\">Exchange</th>\n");
			StrHead.append("<th data-hide=\"phone\">Total</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crs.next()) {
				count++;
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=left>");
				switch (dr_totalby) {
					case "1" :
						dr_totalby_ids.add(crs.getString("emp_id"));
						Strbody.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), crs.getString("emp_ref_no")));
						break;
					case "2" :
						dr_totalby_ids.add(crs.getString("team_id"));
						Strbody.append(crs.getString("team_name"));
						break;
					case "3" :
						dr_totalby_ids.add(crs.getString("branch_id"));
						Strbody.append(crs.getString("branch_name"));
						break;
					case "4" :
						dr_totalby_ids.add(crs.getString("region_id"));
						Strbody.append(crs.getString("region_name"));
						break;
					case "5" :
						dr_totalby_ids.add(crs.getString("brand_id"));
						Strbody.append(crs.getString("brand_name"));
						break;
				}

				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>").append("<a href=\"report-incentive-total.jsp?filter=yes")
						.append("&starttime=").append(starttime)
						.append("&endtime=").append(endtime)
						.append("&brand_ids=").append(brand_id)
						.append("&region_ids=").append(region_id)
						.append("&branch_ids=").append(branch_id)
						.append("&team_ids=").append(team_id)
						.append("&emp_ids=").append(exe_id);

				switch (dr_totalby) {
					case "1" :
						Strbody.append("&so_emp_id=" + crs.getInt("emp_id"));
						break;
					case "2" :
						Strbody.append("&team_id=" + crs.getInt("team_id"));
						break;
					case "3" :
						Strbody.append("&branch_id=" + crs.getInt("branch_id"));
						break;
					case "4" :
						Strbody.append("&region_id=" + crs.getInt("region_id"));
						break;
					case "5" :
						Strbody.append("&brand_id=" + crs.getInt("brand_id"));
						break;
				}

				Strbody.append("\" target=_blank>");
				Strbody.append(crs.getString("soachieved"));
				Strbody.append("</a></td>");

				soachived += crs.getInt("soachieved");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_variant"))));
				Strbody.append("</td>");

				incentive_variant += crs.getDouble("incentive_variant");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_vehstock"))));
				Strbody.append("</td>");

				incentive_vehstock += crs.getDouble("incentive_vehstock");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_soslab"))));
				Strbody.append("</td>");

				incentive_soslab += crs.getDouble("incentive_soslab");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_insurslab"))));
				Strbody.append("</td>");

				incentive_insurslab += crs.getDouble("incentive_insurslab");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_ewslab"))));
				Strbody.append("</td>");

				incentive_ewslab += crs.getDouble("incentive_ewslab");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_financeslab"))));
				Strbody.append("</td>");

				incentive_financeslab += crs.getDouble("incentive_financeslab");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_accessoriessslab"))));
				Strbody.append("</td>");

				incentive_accessoriessslab += crs.getDouble("incentive_accessoriessslab");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_exchangeslab"))));
				Strbody.append("</td>");

				incentive_exchangeslab += crs.getDouble("incentive_exchangeslab");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_overall"))));
				Strbody.append("</td>");

				incentive_overall += crs.getDouble("incentive_overall");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_ew"))));
				Strbody.append("</td>");

				incentive_ew += crs.getDouble("incentive_ew");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_insur"))));
				Strbody.append("</td>");

				incentive_insur += crs.getDouble("incentive_insur");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_finance"))));
				Strbody.append("</td>");

				incentive_finance += crs.getDouble("incentive_finance");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_accessories"))));
				Strbody.append("</td>");

				incentive_accessories += crs.getDouble("incentive_accessories");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_exchange"))));
				Strbody.append("</td>");

				incentive_exchange += crs.getDouble("incentive_exchange");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndFormat(deci.format(crs.getDouble("incentive_total"))));
				Strbody.append("</td></tr>");

				incentive_total += crs.getDouble("incentive_total");

			}
			Strbody.append("<tr><td valign=top align=right colspan=2><b>Total</b></td>");

			Strbody.append("<td valign=top align=right>").append("<a href=\"report-incentive-total.jsp?filter=yes")
					.append("&starttime=").append(starttime)
					.append("&endtime=").append(endtime)
					.append("&brand_ids=").append(brand_id)
					.append("&region_ids=").append(region_id)
					.append("&branch_ids=").append(branch_id)
					.append("&team_ids=").append(team_id)
					.append("&emp_ids=").append(exe_id)
					.append("&dr_totalby=").append(dr_totalby)
					.append("&dr_totalby_ids=").append(dr_totalby_ids)
					.append("\" target=_blank><b>");
			Strbody.append(soachived);
			Strbody.append("</a></b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_variant)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_vehstock)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_soslab)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_insurslab)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_ewslab)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_financeslab)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_accessoriessslab)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_exchangeslab)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_overall)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_ew)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_insur)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_finance)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_accessories)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_exchange)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndFormat(deci.format(incentive_total)));
			Strbody.append("</b></td>");

			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			Strbody.append("</div>\n");
		} else {
			Strbody.append("<font color=red><br><br><br><b>No Details Found!</b></font>");
		}

		return Strbody.toString();
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

		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
		dr_orderby = request.getParameter("dr_orderby");

		// if (!exe_ids.equals("")) {
		// mischeck.exe_ids = exe_ids;
		// }
		// SOP("exe_ids==" + exe_id);

		if (chk_team_lead.equals("on")) {
			chk_team_lead = "1";
		} else {
			chk_team_lead = "0";
		}

		include_inactive_exe = PadQuotes(request.getParameter("chk_include_inactive_exe"));
		if (include_inactive_exe.equals("on")) {
			include_inactive_exe = "1";
		}
		else {
			include_inactive_exe = "0";
		}

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

	// MODEL DASH QUERY

	// Pre owned MODEL DASH QUERY

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
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

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Consultants</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", dr_totalby)).append(">Brands</option>\n");
		return Str.toString();
	}

	private void RedirectSOList(HttpServletRequest request, HttpServletResponse response) {

		try {
			HttpSession session = request.getSession(true);

			String starttime = CNumeric(PadQuotes(request.getParameter("starttime")));
			String endtime = CNumeric(PadQuotes(request.getParameter("endtime")));
			String brand_ids = PadQuotes(RetrunSelectArrVal(request, "brand_ids"));
			String branch_ids = PadQuotes(RetrunSelectArrVal(request, "branch_ids"));
			String region_ids = PadQuotes(RetrunSelectArrVal(request, "region_ids"));
			String team_ids = PadQuotes(RetrunSelectArrVal(request, "team_ids"));
			String exe_ids = PadQuotes(RetrunSelectArrVal(request, "exe_ids"));

			String so_emp_id = CNumeric(PadQuotes(request.getParameter("so_emp_id")));
			String team_id = CNumeric(PadQuotes(RetrunSelectArrVal(request, "team_id")));
			String branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			String region_id = CNumeric(PadQuotes(RetrunSelectArrVal(request, "region_id")));
			String brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));

			String dr_totalby_ids = PadQuotes(RetrunSelectArrVal(request, "dr_totalby_ids")).replace("[", "").replace("]", "");
			String dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));

			// SOP("starttime==" + starttime);
			// SOP("endtime==" + endtime);
			// SOP("brand_ids==" + brand_ids);
			// SOP("branch_ids==" + branch_ids);
			// SOP("region_ids==" + region_ids);
			// SOP("team_ids==" + team_ids);
			// SOP("exe_ids==" + exe_ids);
			// SOP("so_emp_id==" + so_emp_id);
			// SOP("team_id==" + team_id);
			// SOP("branch_id==" + branch_id);
			// SOP("region_id==" + region_id);
			// SOP("brand_id==" + brand_id);
			// SOP("dr_totalby==" + dr_totalby);
			// SOP("dr_totalby_ids==" + dr_totalby_ids);

			if (!starttime.equals("") && !endtime.equals("")) {
				StrFilter += " AND SUBSTR(so_retail_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTR(so_retail_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
			}

			// Brand
			if (!brand_ids.equals("")) {
				StrFilter += " AND branch_brand_id IN (" + brand_ids + ") ";
			}
			// Regions
			if (!region_ids.equals("")) {
				StrFilter += " AND branch_region_id IN (" + region_ids + ")";
			}
			// Branch
			if (!branch_ids.equals("")) {
				StrFilter += " AND branch_id IN (" + branch_ids + ")";
			}
			// Sales Consultant
			if (!exe_ids.equals("")) {
				StrFilter += " AND so_emp_id IN (" + exe_ids + ")";
			}
			// Team
			if (!team_ids.equals("")) {
				StrFilter += " AND so_emp_id IN (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_team_id IN (" + team_ids + "))";
			}

			// so_emp_id
			if (!so_emp_id.equals("0")) {
				StrFilter += " AND so_emp_id =" + so_emp_id;
			}
			// team_id
			if (!team_id.equals("0")) {
				StrFilter += " AND so_emp_id IN ("
						+ " SELECT teamtrans_emp_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = teamtrans_emp_id"
						+ " WHERE teamtrans_team_id IN (" + team_id + "))"
						+ " AND emp_active = 1";
			}
			// branch_id
			if (!branch_id.equals("0")) {
				StrFilter += " AND branch_id =" + branch_id;
			}
			// region_id
			if (!region_id.equals("0")) {
				StrFilter += " AND region_id =" + region_id;
			}
			// brand_id
			if (!brand_id.equals("0")) {
				StrFilter += " AND branch_brand_id =" + brand_id;
			}

			switch (dr_totalby) {
				case "1" :
					StrFilter += " AND so_emp_id IN (" + dr_totalby_ids + ")";
					break;
				case "2" :
					StrFilter += " AND so_emp_id IN ("
							+ " SELECT teamtrans_emp_id "
							+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = teamtrans_emp_id"
							+ " WHERE teamtrans_team_id IN (" + dr_totalby_ids + "))"
							+ " AND emp_active = 1";
					break;
				case "3" :
					StrFilter += " AND branch_id IN (" + dr_totalby_ids + ")";
					break;
				case "4" :
					StrFilter += " AND branch_region_id IN (" + dr_totalby_ids + ")";
					break;
				case "5" :
					StrFilter += " AND branch_brand_id IN (" + dr_totalby_ids + ") ";
					break;
			}

			StrFilter += " AND emp_sales = '1'"
					+ " AND branch_active = '1'"
					+ " AND emp_active = 1";

			SOP("StrFilter==" + StrFilter);

			SetSession("sostrsql", StrFilter, request);
			response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
