package axela.sales;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Incentive_Executive extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public String include_inactive_exe = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", exe_brand_id = "0";
	public String StrHTML1 = "", StrHTML2 = "", StrHTML3 = "", StrHTML4 = "", StrHTML5 = "", StrHTML6 = "", StrHTML7 = "", StrHTML8 = "", StrHTML9 = "";
	public String StrHTML10 = "", StrHTML11 = "", StrHTML12 = "", StrHTML13 = "", StrHTML14 = "", StrHTML15 = "", StrHTML16 = "";
	public String BranchAccess = "", emp_copy_access = "0", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "0", dr_orderby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	public String month = "", year = "";
	public DecimalFormat deci = new DecimalFormat("0.00");
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	public String marital_status = "", emp_active = "", sex = "", address = "", Img = "", targetper = "", emp_name = "", so_brand_id = "";
	public String currexp[];
	public String emp_prevexp[];
	public String monthName = "";
	public int soachieved = 0, sotarget = 0, somintarget = 0;
	public String sorateamount = "0.00", sofinanceamount = "0.00", soinsuramount = "0.00", soewamount = "0.00", somgaamount = "0.00", soexchangeamount = "0.00";
	public double financeamount = 0.00, insuramount = 0.00, ewamount = 0.00, mgaamount = 0.00, exchangeamount = 0.00;
	public String cardfinanceamount = "0.00", cardinsuramount = "0.00", cardewamount = "0.00", cardmgaamount = "0.00", cardexchangeamount = "0.00";
	public String sofinancecount = "0", soinsurcount = "0", soewcount = "0", somgacount = "0", soexchangecount = "0";
	public String soband = "0", financeband = "0", insurband = "0", ewband = "0", mgaband = "0", exchangeband = "0";
	public String slabachsoamount = "0.00", slabachfinanceamount = "0.00", slabachinsuramount = "0.00", slabachewamount = "0.00";
	public String slabachmgaamount = "0.00", slabachexchangeamount = "0.00", slabtotal = "0.00", incentivetotal = "0.00";
	public double grandTotal = 0.00;
	CachedRowSet crsvariant = null;

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
				emp_copy_access = ReturnPerm(comp_id, "emp_copy_access", request);

				month = CNumeric(PadQuotes(request.getParameter("dr_month")));

				if (month.length() == 1) {
					month = "0" + month;
				}
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));

				if (go.equals("")) {
					month = (ConvertShortDateToStr(DateToShortDate(kknow())).substring(4, 6));
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
						StrSearch = StrSearch + " AND emp_id =" + exe_id + "";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						PopulateTarget();
						StrHTML1 = IncentiveByVariant();
						IncentiveBySlab();
						StrHTML4 = IncentiveByOverall();
						StrHTML5 = IncentiveByInsurance();
						StrHTML6 = IncentiveByFinance();
						StrHTML7 = IncentiveByAccessories();
						StrHTML8 = IncentiveByEW();
						StrHTML9 = IncentiveByExchange();

						StrHTML10 = IncentiveSlabBand(exe_brand_id, (year + month));
						StrHTML11 = IncentiveOverallBand(exe_brand_id, (year + month));
						StrHTML12 = IncentiveInsurBand(exe_brand_id, (year + month));
						StrHTML13 = IncentiveFinanceBand(exe_brand_id, (year + month));
						StrHTML14 = IncentiveMgaBand(exe_brand_id, (year + month));
						StrHTML15 = IncentiveEWBand(exe_brand_id, (year + month));
						StrHTML16 = IncentiveExchangeBand(exe_brand_id, (year + month));
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void PopulateTarget() throws SQLException {
		// SOP("PopulateTarget");
		try {
			StrSql = " SELECT"
					+ " emp_id,"
					+ " emp_name,"
					+ " emp_ref_no,"
					+ " COALESCE (sotarget, 0) AS sotarget,"
					+ " somin,"
					+ " COALESCE (IF(soachieved >= somin, soachieved, 0), 0) AS soachieved,"
					+ " COALESCE (sofinanceamount, 0) AS sofinanceamount,"
					+ " COALESCE (sofinancecount,0) AS sofinancecount,"
					+ " COALESCE (soinsuramount, 0) AS soinsuramount,"
					+ " COALESCE (soinsurancecount,0) AS soinsurancecount,"
					+ " COALESCE (soewamount, 0) AS soewamount,"
					+ " COALESCE (soewcount,0) AS soewcount,"
					+ " COALESCE (somgaamount, 0) AS somgaamount,"
					+ " COALESCE (somgacount,0) AS somgacount,"
					+ " COALESCE (soexchangeamount, 0) AS soexchangeamount,"
					+ " COALESCE (soexchangecount,0) AS soexchangecount,"
					+ " branch_brand_id,"
					+ " ( SELECT"
					+ " incentive_total"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive"
					+ " WHERE incentive_emp_id = " + exe_id
					+ " AND SUBSTR(incentive_startdate,1,6) = '" + (year + month) + "'"
					+ " ) AS incentivetotal "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN "
					+ "( SELECT"
					+ " target_so_count AS sotarget,"
					+ " target_so_min AS somin,"
					+ " target_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_target"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(target_startdate, 1, 6) = '" + (year + month) + "'"
					+ " AND SUBSTR(target_enddate, 1, 6) = '" + (year + month) + "'"
					+ " GROUP BY target_emp_id"
					+ " ) tblsotarget ON tblsotarget.target_emp_id = emp_id"
					+ " LEFT JOIN "
					+ "( SELECT"
					+ " COUNT(so_id) AS soachieved,"
					+ " SUM(so_finance_amt) AS sofinanceamount,"
					+ " SUM(IF(so_finance_amt !=0,1,0)) AS sofinancecount,"
					+ " SUM(so_insur_amount) AS soinsuramount,"
					+ " SUM(IF(so_insur_amount !=0,1,0)) AS soinsurancecount,"
					+ " SUM(so_ew_amount) AS soewamount,"
					+ " SUM(IF(so_ew_amount !=0,1,0)) AS soewcount,"
					// + " SUM(so_mga_amount) AS somgaamount,"

					+ " SUM(IF(so_mga_amount != 0"
					+ " AND so_mga_amount >= (SELECT incentiveslab_accessmin "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE incentiveslab_brand_id = branch_brand_id"
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
					+ " LIMIT 1), so_mga_amount, 0)) AS somgaamount,"

					+ " SUM(IF(so_mga_amount != 0"
					+ " AND so_mga_amount >= (SELECT incentiveslab_accessmin "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE incentiveslab_brand_id = branch_brand_id"
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
					+ " LIMIT 1), 1, 0)) AS somgacount,"

					// + " SUM(IF(so_mga_amount !=0,1,0)) AS somgacount,"
					+ " SUM(so_exchange_amount) AS soexchangeamount,"
					+ " SUM(IF(so_exchange_amount !=0,1,0)) AS soexchangecount,"
					+ " so_emp_id,"
					+ " branch_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(so_retail_date, 1, 6)  = '" + (year + month) + "'"
					+ " GROUP BY so_emp_id"
					+ " ) tblso ON tblso.so_emp_id = emp_id"
					+ " WHERE 1 = 1"
					+ " AND emp_id = " + exe_id
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			SOP("StrSql==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				sotarget = crs.getInt("sotarget");
				somintarget = crs.getInt("somin");
				soachieved = crs.getInt("soachieved");
				// SOP("soachieved==" + soachieved);
				targetper = getPercentage(soachieved, sotarget);
				// // SOP("targetper==" + targetper);
				cardfinanceamount = IndDecimalFormat(deci.format(crs.getDouble("sofinanceamount")));
				cardinsuramount = IndDecimalFormat(deci.format(crs.getDouble("soinsuramount")));
				cardewamount = IndDecimalFormat(deci.format(crs.getDouble("soewamount")));
				cardmgaamount = IndDecimalFormat(deci.format(crs.getDouble("somgaamount")));
				cardexchangeamount = IndDecimalFormat(deci.format(crs.getDouble("soexchangeamount")));
				financeamount = crs.getDouble("sofinanceamount");
				sofinancecount = crs.getString("sofinancecount");
				soinsurcount = crs.getString("soinsurancecount");
				soewcount = crs.getString("soewcount");
				somgacount = crs.getString("somgacount");
				soexchangecount = crs.getString("soexchangecount");
				emp_name = crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")";
				so_brand_id = crs.getString("branch_brand_id");
				incentivetotal = IndDecimalFormat(deci.format(crs.getDouble("incentivetotal")));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	private String IncentiveByVariant() throws SQLException {
		// SOP("variant");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;
		double variant_total = 0.0, stock_total = 0.0;

		StrSql = " SELECT"
				+ " so_id,"
				+ " so_vehstock_id, "
				+ " so_emp_id,"
				+ " so_retail_date,"
				+ " COALESCE(incentivevariant_amount,0) AS incentivevariant_amount,"
				+ " vehstock_incentive,"
				+ " so_insur_amount,"
				+ " so_finance_amt,"
				+ " so_mga_amount,"
				+ " so_ew_amount,"
				+ " so_exchange_amount,"
				+ " item_name"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_incentive_variant ON incentivevariant_item_id = so_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
				+ " WHERE so_emp_id = " + exe_id
				+ " AND SUBSTR(so_retail_date, 1, 6) = '" + (year + month) + "'"
				+ " GROUP BY so_id"
				+ " ORDER BY so_retail_date";
		// SOP("StrSql==" + StrSql);

		crsvariant = processQuery(StrSql, 0);

		if (crsvariant.isBeforeFirst()) {
			// StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");
			StrHead.append("<th>Variant</th>\n");
			StrHead.append("<th>SO ID</th>\n");
			StrHead.append("<th>Stock ID</th>\n");
			StrHead.append("<th data-hide=\"phone\">Retail Date</th>\n");
			StrHead.append("<th data-hide=\"phone\">Insurance</th>\n");
			StrHead.append("<th data-hide=\"phone\">Finance</th>\n");
			StrHead.append("<th data-hide=\"phone\">Accessories</th>\n");
			StrHead.append("<th data-hide=\"phone\">Ext. Warranty</th>\n");
			StrHead.append("<th data-hide=\"phone\">Exchange</th>\n");
			StrHead.append("<th data-hide=\"phone\">Variant Incentive</th>\n");
			StrHead.append("<th data-hide=\"phone\">Stock Incentive</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crsvariant.next()) {
				count++;
				variant_total += crsvariant.getDouble("incentivevariant_amount");
				stock_total += crsvariant.getDouble("vehstock_incentive");
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=left>");
				Strbody.append(crsvariant.getString("item_name"));
				Strbody.append("</td>");

				Strbody.append("<td align=center>").append("<a href=\"veh-salesorder-list.jsp?so_id=");
				Strbody.append(crsvariant.getString("so_id")).append("\">");
				Strbody.append(crsvariant.getString("so_id")).append("</a></td>\n");

				Strbody.append("<td align=center>").append("<a href=\"../inventory/stock-list.jsp?vehstock_id=");
				Strbody.append(crsvariant.getString("so_vehstock_id")).append("\">");
				Strbody.append(crsvariant.getString("so_vehstock_id")).append("</a></td>\n");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(strToShortDate(crsvariant.getString("so_retail_date")));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crsvariant.getDouble("so_insur_amount"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crsvariant.getDouble("so_finance_amt"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crsvariant.getDouble("so_mga_amount"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crsvariant.getDouble("so_ew_amount"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crsvariant.getDouble("so_exchange_amount"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crsvariant.getDouble("incentivevariant_amount"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crsvariant.getDouble("vehstock_incentive"))));
				Strbody.append("</td></tr>");
			}

			Strbody.append("<tr><td valign=top align=right colspan=10><b>Total:</b></td>");
			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(variant_total)));
			Strbody.append("</b></td>");

			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(stock_total)));
			Strbody.append("</b></td>");

			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			grandTotal += variant_total + stock_total;
		} else {
			Strbody.append("<font color=red><br><b>No Details Found!</b><br></font>");
		}

		return Strbody.toString();
	}

	private String IncentiveByOverall() throws SQLException {
		// SOP("over");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;
		double total = 0.0;

		StrSql = " SELECT"
				+ " incentivetargetband_amount,"
				+ " incentivetargetband_from,"
				+ " incentivetargetband_to"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON incentivetarget_id = incentivetargetband_incentivetarget_id"
				+ " WHERE 1 = 1"
				+ " AND incentivetarget_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentivetarget_startdate,1,6) = '" + (year + month) + "'"
				+ " AND incentivetargetband_from <= '" + (targetper) + "'"
				+ " AND incentivetargetband_to >= '" + (targetper) + "'"
				+ " OR incentivetargetband_id = ( "
				+ " SELECT "
				+ " incentivetargetband_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band "
				+ " WHERE SUBSTR(incentivetarget_startdate, 1, 6) = '" + (year + month) + "'"
				+ " AND incentivetarget_brand_id = " + so_brand_id
				+ " AND incentivetargetband_from < '" + (targetper) + "'"
				+ " ORDER BY incentivetargetband_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("StrSql==" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			// StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");
			StrHead.append("<th>Achieved %</th>\n");
			StrHead.append("<th>Band</th>\n");
			StrHead.append("<th>Amount</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crs.next()) {
				count++;
				total += crs.getDouble("incentivetargetband_amount");
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(targetper + "%");
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getString("incentivetargetband_from") + "% -" + crs.getString("incentivetargetband_to") + "%");
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentivetargetband_amount"))));
				Strbody.append("</td></tr>");
			}
			Strbody.append("<tr><td valign=top align=right colspan=3><b>Total:</b></td>");
			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(total)));
			Strbody.append("<b></td>");

			grandTotal += total;
			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			// Strbody.append("</div>\n");
		} else {
			Strbody.append("<font color=red><br><b>No Details Found!</b><br></font>");
		}
		crs.close();
		return Strbody.toString();
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		month = CNumeric(PadQuotes(request.getParameter("dr_month")));
		if (month.length() == 1) {
			month = "0" + month;
		}
		monthName = TextMonth(Integer.parseInt(month) - 1);
		year = CNumeric(PadQuotes(request.getParameter("dr_year")));

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		exe_id = CNumeric(PadQuotes(request.getParameter("dr_sale_executive")));

		exe_brand_id = ExecuteQuery("SELECT branch_brand_id "
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_branch_id = branch_id"
				+ " WHERE emp_id =" + exe_id);

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		if (chk_team_lead.equals("on")) {
			chk_team_lead = "1";
		} else {
			chk_team_lead = "0";
		}

	}

	protected void CheckForm() {
		msg = "";
		if (month.equals("00")) {
			msg += "<br>Select Month!";
		}
		if (year.equals("0")) {
			msg += "<br>Select Year!";
		}
		if (exe_id.equals("0")) {
			msg += "<br>Select Sales Consultant!";
		}
	}

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " AND team_active = 1"
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// // SOP("PopulateTeam query ==== " + StrSql);
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

	public String PopulateYear(String year) {
		StringBuilder Str = new StringBuilder();
		int currentYear = Integer.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow()))));
		// // SOP("year==" + currentYear);
		// Str.append("<option value =-1>Select</option>");
		for (int i = currentYear; i >= 2000; i--) {
			Str.append("<option value =").append(i).append("");
			Str.append(Selectdrop(i, year)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateMonth(String month) {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		Str.append("<option value = 1").append(Selectdrop(1, month)).append(">January</option>\n");
		Str.append("<option value = 2").append(Selectdrop(2, month)).append(">February</option>\n");
		Str.append("<option value = 3").append(Selectdrop(3, month)).append(">March</option>\n");
		Str.append("<option value = 4").append(Selectdrop(4, month)).append(">April</option>\n");
		Str.append("<option value = 5").append(Selectdrop(5, month)).append(">May</option>\n");
		Str.append("<option value = 6").append(Selectdrop(6, month)).append(">June</option>\n");
		Str.append("<option value = 7").append(Selectdrop(7, month)).append(">July</option>\n");
		Str.append("<option value = 8").append(Selectdrop(8, month)).append(">August</option>\n");
		Str.append("<option value = 9").append(Selectdrop(9, month)).append(">September</option>\n");
		Str.append("<option value = 10").append(Selectdrop(10, month)).append(">October</option>\n");
		Str.append("<option value = 11").append(Selectdrop(11, month)).append(">November</option>\n");
		Str.append("<option value = 12").append(Selectdrop(12, month)).append(">December</option>\n");
		return Str.toString();
	}
	public String PopulateEmp(String team_id, String sale_executive, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
					+ " WHERE 1 = 1"
					+ " AND emp_active = '1' "
					+ " AND emp_sales = '1'"
					+ ExeAccess;
			if (!branch_id.equals("")) {
				StrSql = StrSql + " AND emp_branch_id IN (" + branch_id + ")";
			}

			if (!team_id.equals("")) {
				StrSql = StrSql + " AND teamtrans_team_id IN (" + team_id + ")";
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			// // SOP("PopulateEmp==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_sale_executive\" name=\"dr_sale_executive\" class=\"form-control\">");
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), sale_executive));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	private String IncentiveByFinance() throws SQLException {
		// SOP("Fin");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0, lakh = 0;
		double total = 0.0, amount = 0.00;

		lakh = (int) (financeamount / 100000);
		StrSql = " SELECT"
				+ " incentivefinanceslab_from,"
				+ " incentivefinanceslab_to,"
				+ " incentivefinanceslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
				+ " WHERE 1 = 1"
				+ " AND incentivefinanceslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentivefinanceslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentivefinanceslab_from <= '" + (cardfinanceamount.replaceAll(",", "")) + "'"
				+ " AND incentivefinanceslab_to >= '" + (cardfinanceamount.replaceAll(",", "")) + "'"
				+ " OR incentivefinanceslab_id = ( "
				+ " SELECT "
				+ " incentivefinanceslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance "
				+ " WHERE SUBSTR(incentivefinanceslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentivefinanceslab_brand_id = " + so_brand_id
				+ " AND incentivefinanceslab_from < '" + cardfinanceamount.replaceAll(",", "") + "'"
				+ " ORDER BY incentivefinanceslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("StrSql==Fin==" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			// StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");
			StrHead.append("<th>Achieved Count</th>\n");
			StrHead.append("<th>Achieved Amount</th>\n");
			StrHead.append("<th>Band</th>\n");
			StrHead.append("<th>Rate</th>\n");
			StrHead.append("<th>Amount</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crs.next()) {
				count++;
				amount = (crs.getDouble("incentivefinanceslab_amt") * lakh);
				// // SOP("per==" + (crs.getDouble("incentivefinanceslab_perc") / 100));
				// // SOP("financeamount==" + financeamount);
				// // SOP("amount==" + amount);
				total += amount;
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(sofinancecount);
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(cardfinanceamount);
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_from"))) + " -" + IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_to"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getDouble("incentivefinanceslab_amt"));
				Strbody.append("</td>");
				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(amount)));
				Strbody.append("</td></tr>");
			}
			Strbody.append("<tr><td valign=top align=right colspan=5><b>Total:</b></td>");
			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(amount)));
			Strbody.append("<b></td>");

			grandTotal += amount;

			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			// Strbody.append("</div>\n");
		} else {
			Strbody.append("<font color=red><br><b>No Details Found!</b><br></font>");
		}
		crs.close();
		return Strbody.toString();
	}

	private String IncentiveByInsurance() throws SQLException {
		// SOP("insur");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;
		double total = 0.0, amount = 0.00;

		StrSql = " SELECT"
				+ " incentiveinsurslab_from,"
				+ " incentiveinsurslab_to,"
				+ " incentiveinsurslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
				+ " WHERE 1 = 1"
				+ " AND incentiveinsurslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveinsurslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveinsurslab_from <= '" + (getPercentage(Double.parseDouble(soinsurcount), soachieved)) + "'"
				+ " AND incentiveinsurslab_to >= '" + (getPercentage(Double.parseDouble(soinsurcount), soachieved)) + "'"
				+ " OR incentiveinsurslab_id = ( "
				+ " SELECT "
				+ " incentiveinsurslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur "
				+ " WHERE SUBSTR(incentiveinsurslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveinsurslab_brand_id = " + so_brand_id
				+ " AND incentiveinsurslab_from < '" + (getPercentage(Double.parseDouble(soinsurcount), soachieved)) + "'"
				+ " ORDER BY incentiveinsurslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("StrSql==" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			// StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");
			StrHead.append("<th>Achieved Count</th>\n");
			StrHead.append("<th>Achieved Amount</th>\n");
			StrHead.append("<th>Band</th>\n");
			StrHead.append("<th>Rate</th>\n");
			StrHead.append("<th>Amount</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crs.next()) {
				count++;
				total += crs.getDouble("incentiveinsurslab_amt") * Integer.parseInt(soinsurcount);
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(soinsurcount);
				Strbody.append("</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(cardinsuramount);
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getString("incentiveinsurslab_from") + "% -" + crs.getString("incentiveinsurslab_to") + "%");
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveinsurslab_amt"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(total)));
				Strbody.append("</td></tr>");
			}
			Strbody.append("<tr><td valign=top align=right colspan=5><b>Total:</b></td>");
			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(total)));
			Strbody.append("<b></td>");

			grandTotal += total;

			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			// Strbody.append("</div>\n");
		} else {
			Strbody.append("<font color=red><br><b>No Details Found!</b><br></font>");
		}
		crs.close();
		return Strbody.toString();
	}

	private void IncentiveBySlab() throws SQLException {
		// SOP("slab");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;
		double total = 0.0, amount = 0.00;
		CachedRowSet crs;
		StrSql = " SELECT"
				+ " incentiveslab_from,"
				+ " incentiveslab_to,"
				+ " incentiveslab_soamt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
				+ " WHERE 1 = 1"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveslab_from <= '" + (soachieved) + "'"
				+ " AND incentiveslab_to >= '" + (soachieved) + "'"
				+ " OR incentiveslab_id = ( "
				+ " SELECT "
				+ " incentiveslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
				+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND incentiveslab_from <='" + (soachieved) + "'"
				+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("str===1==" + StrSql);
		crs = processQuery(StrSql, 0);
		while (crs.next()) {

			soband = crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to");
			sorateamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt")));
			slabachsoamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt") * soachieved));
			total += crs.getDouble("incentiveslab_soamt") * soachieved;
		}
		crs.close();

		StrSql = " SELECT"
				+ " incentiveslab_from,"
				+ " incentiveslab_to,"
				+ " incentiveslab_financeamt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
				+ " WHERE 1 = 1"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveslab_from <= '" + (sofinancecount) + "'"
				+ " AND incentiveslab_to >= '" + (sofinancecount) + "'"
				+ " OR incentiveslab_id = ( "
				+ " SELECT "
				+ " incentiveslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
				+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND incentiveslab_from <= '" + (sofinancecount) + "'"
				+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";

		crs = processQuery(StrSql, 0);
		while (crs.next()) {

			financeband = crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to");
			sofinanceamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_financeamt")));
			slabachfinanceamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_financeamt") * Integer.parseInt(sofinancecount)));
			total += crs.getDouble("incentiveslab_financeamt") * Integer.parseInt(sofinancecount);
		}
		crs.close();

		StrSql = " SELECT"
				+ " incentiveslab_from,"
				+ " incentiveslab_to,"
				+ " incentiveslab_insuramt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
				+ " WHERE 1 = 1"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveslab_from <= '" + (soinsurcount) + "'"
				+ " AND incentiveslab_to >= '" + (soinsurcount) + "'"
				+ " OR incentiveslab_id = ( "
				+ " SELECT "
				+ " incentiveslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
				+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND incentiveslab_from <= '" + (soinsurcount) + "'"
				+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";

		crs = processQuery(StrSql, 0);
		while (crs.next()) {

			insurband = crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to");
			soinsuramount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_insuramt")));
			slabachinsuramount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_insuramt") * Integer.parseInt(soinsurcount)));
			total += crs.getDouble("incentiveslab_insuramt") * Integer.parseInt(soinsurcount);
		}
		crs.close();

		StrSql = " SELECT"
				+ " incentiveslab_from,"
				+ " incentiveslab_to,"
				+ " incentiveslab_accessamt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
				+ " WHERE 1 = 1"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveslab_from <= '" + (somgacount) + "'"
				+ " AND incentiveslab_to >= '" + (somgacount) + "'"
				+ " OR incentiveslab_id = ( "
				+ " SELECT "
				+ " incentiveslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
				+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND incentiveslab_from <= '" + (somgacount) + "'"
				+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("Access==" + StrSql);
		crs = processQuery(StrSql, 0);
		while (crs.next()) {

			mgaband = crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to");
			somgaamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessamt")));
			slabachmgaamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessamt") * Integer.parseInt(somgacount)));
			total += crs.getDouble("incentiveslab_accessamt") * Integer.parseInt(somgacount);
		}
		crs.close();

		StrSql = " SELECT"
				+ " incentiveslab_from,"
				+ " incentiveslab_to,"
				+ " incentiveslab_ewamt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
				+ " WHERE 1 = 1"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveslab_from <= '" + (soewcount) + "'"
				+ " AND incentiveslab_to >= '" + (soewcount) + "'"
				+ " OR incentiveslab_id = ( "
				+ " SELECT "
				+ " incentiveslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
				+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND incentiveslab_from <= '" + (soewcount) + "'"
				+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";

		crs = processQuery(StrSql, 0);
		while (crs.next()) {

			ewband = crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to");
			soewamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_ewamt")));
			slabachewamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_ewamt") * Integer.parseInt(soewcount)));
			total += crs.getDouble("incentiveslab_ewamt") * Integer.parseInt(soewcount);
		}
		crs.close();

		StrSql = " SELECT"
				+ " incentiveslab_from,"
				+ " incentiveslab_to,"
				+ " incentiveslab_exchangeamt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
				+ " WHERE 1 = 1"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveslab_from <= '" + (soexchangecount) + "'"
				+ " AND incentiveslab_to >= '" + (soexchangecount) + "'"
				+ " OR incentiveslab_id = ( "
				+ " SELECT "
				+ " incentiveslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
				+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveslab_brand_id = " + so_brand_id
				+ " AND incentiveslab_from <= '" + (soexchangecount) + "'"
				+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";

		crs = processQuery(StrSql, 0);
		while (crs.next()) {

			exchangeband = crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to");
			soexchangeamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_exchangeamt")));
			slabachexchangeamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_exchangeamt") * Integer.parseInt(soexchangecount)));
			total += crs.getDouble("incentiveslab_exchangeamt") * Integer.parseInt(soexchangecount);
		}
		crs.close();
		slabtotal = IndDecimalFormat(deci.format(total));
		grandTotal += (total);
	}

	private String IncentiveByAccessories() throws SQLException {
		// // SOP("Access");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;
		double total = 0.0, amount = 0.00;

		StrSql = " SELECT"
				+ " incentiveaccesslab_from,"
				+ " incentiveaccesslab_to,"
				+ " incentiveaccesslab_perc"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
				+ " WHERE 1 = 1"
				+ " AND incentiveaccesslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveaccesslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveaccesslab_from <= '" + (cardmgaamount.replaceAll(",", "")) + "'"
				+ " AND incentiveaccesslab_to >= '" + (cardmgaamount.replaceAll(",", "")) + "'"
				+ " OR incentiveaccesslab_id = ( "
				+ " SELECT "
				+ " incentiveaccesslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories "
				+ " WHERE SUBSTR(incentiveaccesslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveaccesslab_brand_id = " + so_brand_id
				+ " AND incentiveaccesslab_from < '" + cardmgaamount.replaceAll(",", "") + "'"
				+ " ORDER BY incentiveaccesslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("StrSql==Acces==" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			// StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");
			StrHead.append("<th>Achieved Count</th>\n");
			StrHead.append("<th>Achieved Amount</th>\n");
			StrHead.append("<th>Band</th>\n");
			StrHead.append("<th>Percentage</th>\n");
			StrHead.append("<th>Amount</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crs.next()) {
				count++;
				amount = (crs.getDouble("incentiveaccesslab_perc") / 100) * Double.parseDouble(cardmgaamount.replaceAll(",", ""));
				total += amount;
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(somgacount);
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(cardmgaamount);
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_from"))) + " -" + IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_to"))));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getDouble("incentiveaccesslab_perc") + "%");
				Strbody.append("</td>");
				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(amount)));
				Strbody.append("</td></tr>");
			}
			Strbody.append("<tr><td valign=top align=right colspan=5><b>Total:</b></td>");
			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(amount)));
			Strbody.append("<b></td>");

			grandTotal += total;

			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			// Strbody.append("</div>\n");
		} else {
			Strbody.append("<font color=red><br><b>No Details Found!</b><br></font>");
		}
		crs.close();
		return Strbody.toString();
	}

	private String IncentiveByEW() throws SQLException {
		// SOP("EW");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;
		double total = 0.0, amount = 0.00;

		StrSql = " SELECT"
				+ " incentiveewslab_from,"
				+ " incentiveewslab_to,"
				+ " incentiveewslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
				+ " WHERE 1 = 1"
				+ " AND incentiveewslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveewslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveewslab_from <= '" + (getPercentage(Double.parseDouble(soewcount), soachieved)) + "'"
				+ " AND incentiveewslab_to >= '" + (getPercentage(Double.parseDouble(soewcount), soachieved)) + "'"
				+ " OR incentiveewslab_id = ( "
				+ " SELECT "
				+ " incentiveewslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew "
				+ " WHERE SUBSTR(incentiveewslab_date, 1, 6) = '" + (year + month) + "'"
				+ " AND incentiveewslab_brand_id = " + so_brand_id
				+ " AND incentiveewslab_from < '" + (getPercentage(Double.parseDouble(soewcount), soachieved)) + "'"
				+ " ORDER BY incentiveewslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("StrSql==EW===" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			// StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");
			StrHead.append("<th>Achieved Count</th>\n");
			StrHead.append("<th>Achieved Amount</th>\n");
			StrHead.append("<th>Band</th>\n");
			StrHead.append("<th>Rate</th>\n");
			StrHead.append("<th>Amount</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crs.next()) {
				count++;
				total += crs.getDouble("incentiveewslab_amt") * Integer.parseInt(soewcount);
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(soewcount);
				Strbody.append("</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(cardewamount);
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getString("incentiveewslab_from") + "% -" + crs.getString("incentiveewslab_to") + "%");
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getString("incentiveewslab_amt"));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(total)));
				Strbody.append("</td></tr>");
			}
			Strbody.append("<tr><td valign=top align=right colspan=5><b>Total:</b></td>");
			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(total)));
			Strbody.append("<b></td>");

			grandTotal += total;

			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			// Strbody.append("</div>\n");
		} else {
			Strbody.append("<font color=red><br><b>No Details Found!</b><br></font>");
		}
		crs.close();
		return Strbody.toString();
	}

	private String IncentiveByExchange() throws SQLException {
		// SOP("Exc");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		String SqlJoin = "";
		int count = 0;
		double total = 0.0, amount = 0.00;

		StrSql = " SELECT"
				+ " incentiveexchangeslab_from,"
				+ " incentiveexchangeslab_to,"
				+ " incentiveexchangeslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
				+ " WHERE 1 = 1"
				+ " AND incentiveexchangeslab_brand_id = " + so_brand_id
				+ " AND SUBSTR(incentiveexchangeslab_date,1,6) = '" + (year + month) + "'"
				+ " AND incentiveexchangeslab_from <= '" + (getPercentage(Double.parseDouble(soexchangecount), soachieved)) + "'"
				+ " AND incentiveexchangeslab_to >= '" + (getPercentage(Double.parseDouble(soexchangecount), soachieved)) + "'"
				+ " OR incentiveexchangeslab_id = ( "
				+ " SELECT "
				+ " incentiveexchangeslab_id "
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange "
				+ " WHERE SUBSTR(incentiveexchangeslab_date, 1, 6) = '" + (year) + "'"
				+ " AND incentiveexchangeslab_brand_id = " + so_brand_id
				+ " AND incentiveexchangeslab_from < '" + (getPercentage(Double.parseDouble(soexchangecount), soachieved)) + "'"
				+ " ORDER BY incentiveexchangeslab_id DESC LIMIT 1 "
				+ " )"
				+ " LIMIT 1";
		// SOP("StrSql==Ex==" + StrSql);

		CachedRowSet crs = processQuery(StrSql, 0);

		if (crs.isBeforeFirst()) {
			// StrHead.append("<div class=\"table-responsive table-bordered\">\n");
			StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			StrHead.append("<thead>");
			StrHead.append("<tr>\n");
			StrHead.append("<th data-hide=\"phone\">#</th>\n");
			StrHead.append("<th>Achieved Count</th>\n");
			StrHead.append("<th>Achieved Amount</th>\n");
			StrHead.append("<th>Band</th>\n");
			StrHead.append("<th>Rate</th>\n");
			StrHead.append("<th>Amount</th>\n");
			StrHead.append("</tr>\n");
			StrHead.append("</thead>\n");
			StrHead.append("<tbody>\n");

			Strbody.append(StrHead);

			while (crs.next()) {
				count++;
				total += crs.getDouble("incentiveexchangeslab_amt") * Integer.parseInt(soexchangecount);
				Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(soexchangecount);
				Strbody.append("</td>");
				Strbody.append("<td valign=top align=center>");
				Strbody.append(cardexchangeamount);
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getString("incentiveexchangeslab_from") + "% -" + crs.getString("incentiveexchangeslab_to") + "%");
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=center>");
				Strbody.append(crs.getString("incentiveexchangeslab_amt"));
				Strbody.append("</td>");

				Strbody.append("<td valign=top align=right>");
				Strbody.append(IndDecimalFormat(deci.format(total)));
				Strbody.append("</td></tr>");
			}
			Strbody.append("<tr><td valign=top align=right colspan=5><b>Total:</b></td>");
			Strbody.append("<td valign=top align=right><b>");
			Strbody.append(IndDecimalFormat(deci.format(total)));
			Strbody.append("<b></td>");

			grandTotal += total;
			Strbody.append("</tbody>\n");
			Strbody.append("</table>\n");
			// Strbody.append("</div>\n");
		} else {
			Strbody.append("<font color=red><br><b>No Details Found!</b><br></font>");
		}
		crs.close();
		return Strbody.toString();
	}

	private String IncentiveInsurBand(String brand_id, String date) throws SQLException {
		// SOP("variant");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		int count = 0;

		StrSql = "SELECT "
				+ " incentiveinsurslab_id,"
				+ " incentiveinsurslab_from,"
				+ " incentiveinsurslab_to,"
				+ " incentiveinsurslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveinsurslab_date, 1, 6) = " + date
				+ " AND incentiveinsurslab_brand_id =" + brand_id
				+ " ORDER BY incentiveinsurslab_date ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");
				StrHead.append("<th>Band</th>\n");
				StrHead.append("<th data-hide=\"phone\">Amount</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Strbody.append(StrHead);

				while (crs.next()) {
					count++;
					Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
					Strbody.append("<td valign=top align=center>");
					Strbody.append(crs.getString("incentiveinsurslab_from") + "% - " + crs.getString("incentiveinsurslab_to") + "%");
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveinsurslab_amt"))));
					Strbody.append("</td></tr>");
				}

				Strbody.append("</tbody>\n");
				Strbody.append("</table>\n");
			} else {
				Strbody.append("<font color=red><br><b>No Insurance wise Incentive Found!</b><br></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Strbody.toString();
	}

	private String IncentiveFinanceBand(String brand_id, String date) throws SQLException {
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		int count = 0;

		StrSql = "SELECT "
				+ " incentivefinanceslab_id,"
				+ " incentivefinanceslab_from,"
				+ " incentivefinanceslab_to,"
				+ " incentivefinanceslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentivefinanceslab_date, 1, 6) = " + date
				+ " AND incentivefinanceslab_brand_id =" + brand_id
				+ " ORDER BY incentivefinanceslab_date ";
		// SOP("Finance==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");
				StrHead.append("<th>Band</th>\n");
				StrHead.append("<th data-hide=\"phone\">Amount</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Strbody.append(StrHead);

				while (crs.next()) {
					count++;
					Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
					Strbody.append("<td valign=top align=center>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_from"))) + " - " + IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_to"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_amt"))));
					Strbody.append("</td></tr>");
				}

				Strbody.append("</tbody>\n");
				Strbody.append("</table>\n");
			} else {
				Strbody.append("<font color=red><br><b>No Finance wise Incentive Found!</b><br></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Strbody.toString();
	}

	private String IncentiveMgaBand(String brand_id, String date) throws SQLException {
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		int count = 0;

		StrSql = "SELECT "
				+ " incentiveaccesslab_id,"
				+ " incentiveaccesslab_from,"
				+ " incentiveaccesslab_to,"
				+ " incentiveaccesslab_perc"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveaccesslab_date, 1, 6) = " + date
				+ " AND incentiveaccesslab_brand_id =" + brand_id
				+ " ORDER BY incentiveaccesslab_date ";
		// SOP("Mga==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");
				StrHead.append("<th>Band</th>\n");
				StrHead.append("<th data-hide=\"phone\">Percentage</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Strbody.append(StrHead);

				while (crs.next()) {
					count++;
					Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
					Strbody.append("<td valign=top align=center>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_from"))) + " - " + IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_to"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(crs.getDouble("incentiveaccesslab_perc") + " %");
					Strbody.append("</td></tr>");
				}

				Strbody.append("</tbody>\n");
				Strbody.append("</table>\n");
			} else {
				Strbody.append("<font color=red><br><b>No Accessories wise Incentive Found!</b><br></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Strbody.toString();
	}

	private String IncentiveEWBand(String brand_id, String date) throws SQLException {
		// SOP("variant");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		int count = 0;

		StrSql = "SELECT "
				+ " incentiveewslab_id,"
				+ " incentiveewslab_from,"
				+ " incentiveewslab_to,"
				+ " incentiveewslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveewslab_date, 1, 6) = " + date
				+ " AND incentiveewslab_brand_id =" + brand_id
				+ " ORDER BY incentiveewslab_date ";

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");
				StrHead.append("<th>Band</th>\n");
				StrHead.append("<th data-hide=\"phone\">Amount</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Strbody.append(StrHead);

				while (crs.next()) {
					count++;
					Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
					Strbody.append("<td valign=top align=center>");
					Strbody.append(crs.getString("incentiveewslab_from") + "% - " + crs.getString("incentiveewslab_to") + "%");
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveewslab_amt"))));
					Strbody.append("</td></tr>");
				}

				Strbody.append("</tbody>\n");
				Strbody.append("</table>\n");
			} else {
				Strbody.append("<font color=red><br><b>No Ext. Warranty wise Incentive Found!</b><br></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Strbody.toString();
	}

	private String IncentiveExchangeBand(String brand_id, String date) throws SQLException {
		// SOP("variant");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		int count = 0;

		StrSql = "SELECT "
				+ " incentiveexchangeslab_id,"
				+ " incentiveexchangeslab_from,"
				+ " incentiveexchangeslab_to,"
				+ " incentiveexchangeslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveexchangeslab_date, 1, 6) = " + date
				+ " AND incentiveexchangeslab_brand_id =" + brand_id
				+ " ORDER BY incentiveexchangeslab_date ";

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");
				StrHead.append("<th>Band</th>\n");
				StrHead.append("<th data-hide=\"phone\">Amount</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Strbody.append(StrHead);

				while (crs.next()) {
					count++;
					Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
					Strbody.append("<td valign=top align=center>");
					Strbody.append(crs.getString("incentiveexchangeslab_from") + "% - " + crs.getString("incentiveexchangeslab_to") + "%");
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveexchangeslab_amt"))));
					Strbody.append("</td></tr>");
				}

				Strbody.append("</tbody>\n");
				Strbody.append("</table>\n");
			} else {
				Strbody.append("<font color=red><br><b>No Exchange wise Incentive Found!</b><br></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Strbody.toString();
	}

	private String IncentiveSlabBand(String brand_id, String date) throws SQLException {
		// SOP("variant");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		int count = 0;

		StrSql = "SELECT "
				+ " incentiveslab_id,"
				+ " incentiveslab_from,"
				+ " incentiveslab_to,"
				+ " incentiveslab_soamt,"
				+ " incentiveslab_financeamt,"
				+ " incentiveslab_insuramt,"
				+ " incentiveslab_ewamt,"
				+ " incentiveslab_accessmin,"
				+ " incentiveslab_accessamt,"
				+ " incentiveslab_exchangeamt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveslab_date, 1, 6) = " + year + month
				+ " AND incentiveslab_brand_id =" + brand_id;
		// + " ORDER BY incentivetargetband_from ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StrHead.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				StrHead.append("<thead><tr>\n");
				StrHead.append("<th>#</th>\n");
				StrHead.append("<th>Band</th>\n");
				StrHead.append("<th>SO Amount</th>\n");
				StrHead.append("<th>Finance Amount</th>\n");
				StrHead.append("<th>Insurance Amount</th>\n");
				StrHead.append("<th>Ext. Warranty Amount</th>\n");
				StrHead.append("<th>Accessories Minimum</th>\n");
				StrHead.append("<th>Accessories Amount</th>\n");
				StrHead.append("<th>Exchange Amount</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Strbody.append(StrHead);

				while (crs.next()) {
					count++;
					Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
					Strbody.append("<td valign=top align=center>");
					Strbody.append(crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_financeamt"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_insuramt"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_ewamt"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessmin"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessamt"))));
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_exchangeamt"))));
					Strbody.append("</td></tr>");
				}

				Strbody.append("</tbody>\n");
				Strbody.append("</table>\n");
			} else {
				Strbody.append("<font color=red><br><b>No Slab wise Incentive Found!</b><br></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Strbody.toString();
	}

	private String IncentiveOverallBand(String brand_id, String date) throws SQLException {
		// SOP("variant");
		StringBuilder StrHead = new StringBuilder();
		StringBuilder Strbody = new StringBuilder();
		int count = 0;

		StrSql = "SELECT "
				+ " incentivetargetband_from,"
				+ " incentivetargetband_to,"
				+ " incentivetargetband_amount"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
				+ " WHERE 1 = 1"
				+ " AND incentivetarget_brand_id =" + brand_id
				+ " AND SUBSTR(incentivetarget_startdate, 1, 6) = " + year + month
				+ " ORDER BY incentivetargetband_from ";

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				StrHead.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				StrHead.append("<thead>");
				StrHead.append("<tr>\n");
				StrHead.append("<th data-hide=\"phone\">#</th>\n");
				StrHead.append("<th>Band</th>\n");
				StrHead.append("<th data-hide=\"phone\">Amount</th>\n");
				StrHead.append("</tr>\n");
				StrHead.append("</thead>\n");
				StrHead.append("<tbody>\n");

				Strbody.append(StrHead);

				while (crs.next()) {
					count++;
					Strbody.append("<tr><td valign=top align=center>" + count + "</td>");
					Strbody.append("<td valign=top align=center>");
					Strbody.append(crs.getString("incentivetargetband_from") + "% - " + crs.getString("incentivetargetband_to") + "%");
					Strbody.append("</td>");

					Strbody.append("<td valign=top align=right>");
					Strbody.append(IndDecimalFormat(deci.format(crs.getDouble("incentivetargetband_amount"))));
					Strbody.append("</td></tr>");
				}

				Strbody.append("</tbody>\n");
				Strbody.append("</table>\n");
			} else {
				Strbody.append("<font color=red><br><b>No Overall wise Incentive Found!</b><br></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Strbody.toString();
	}

}
