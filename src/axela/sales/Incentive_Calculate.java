package axela.sales;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Incentive_Calculate extends Connect {

	public String StrSql = "", CountSql = "", SqlJoin = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids;
	public String brand_id = "", region_id = "", team_id = "0", exe_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", emp_copy_access = "0", dr_branch_id = "0";
	public String go = "";
	public String dr_totalby = "0", dr_orderby = "0";
	public String ExeAccess = "";
	public String StrSearch = "";
	public String emp_all_exe = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();

	public String month = "", year = "", so_brand_id = "", targetper = "";
	public String soachieved = "0", sotarget = "0", somintarget = "0";
	public String sorateamount = "0.00", sofinanceamount = "0.00", soinsuramount = "0.00", soewamount = "0.00", somgaamount = "0.00", soexchangeamount = "0.00";
	public String financeamount = "0.00", insuramount = "0.00", ewamount = "0.00", mgaamount = "0.00", exchangeamount = "0.00";
	public String cardfinanceamount = "0.00", cardinsuramount = "0.00", cardewamount = "0.00", cardmgaamount = "0.00", cardexchangeamount = "0.00";
	public String sofinancecount = "0", soinsurcount = "0", soewcount = "0", somgacount = "0", soexchangecount = "0";
	public String soband = "0", financeband = "0", insurband = "0", ewband = "0", mgaband = "0", exchangeband = "0";
	public double slabachsoamount = 0.00, slabachfinanceamount = 0.00, slabachinsuramount = 0.00, slabachewamount = 0.00;
	public double slabachmgaamount = 0.00, slabachexchangeamount = 0.00, slabtotal = 0.00;
	public double grandTotal = 0.00;
	public double incentive_variant = 0.00, incentive_stock = 0.00, incentive_slab = 0.00, incentive_insurance = 0.00, incentive_total = 0.00;
	public double incentive_finance = 0.00, incentive_ew = 0.00, incentive_exchange = 0.00, incentive_overall = 0.00;
	public String incentiveaccesslab_perc = "0.0", incentive_access = "0.00";

	public int execount = 0, TotalRecords = 0;

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

				if (go.equals("")) {
					msg = "";
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
						StrSearch += " AND branch_brand_id =" + brand_id + " ";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = IncentiveUpdate(comp_id, brand_id, exe_id, (year + month));
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		month = CNumeric(PadQuotes(request.getParameter("dr_month")));
		if (month.length() == 1) {
			month = "0" + month;
		}
		year = CNumeric(PadQuotes(request.getParameter("dr_year")));

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
		if (!branch_id.equals("")) {
			mischeck.exe_branch_id = branch_id;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (brand_id.equals("")) {
			msg += "<br>Select Brand!";
		}
		if ((!region_id.equals("") || !branch_id.equals("")) && exe_id.equals("")) {
			msg += "<br>Select Sales Consultant!";
		}
		if (year.equals("0")) {
			msg += "<br>Select Year!";
		}
		if (month.equals("00")) {
			msg += "<br>Select Month!";
		}

	}

	public String PopulatePrincipal(String brand_id, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// //SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1 = 1"
					+ BranchAccess
					// + " AND branch_branchtype_id IN (1, 2) "
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("StrSql======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = -1>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(Selectdrop(crs.getInt("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
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

	public String IncentiveUpdate(String comp_id, String brand_id, String exe_id, String year) throws SQLException {
		int count = 0, insurCount = 0;
		double total = 0.0, amount = 0.00;
		CachedRowSet crs = null;
		CachedRowSet crs1 = null;
		Connection conntx = null;
		Statement stmttx = null;
		conntx = connectDB();
		conntx.setAutoCommit(false);
		stmttx = conntx.createStatement();

		Map<String, HashMap<String, String>> emp_details = new HashMap<String, HashMap<String, String>>();
		Map<String, Double> incentive_total = new HashMap<String, Double>();
		HashMap<String, String> incentiveByemp = new HashMap<String, String>();
		try {
			StrSql = " SELECT"
					+ " emp_id,"
					+ " branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_incentive ON incentive_emp_id = emp_id"
					+ " AND SUBSTR(incentive_startdate,1,6) = " + year
					+ " WHERE branch_brand_id = " + brand_id
					+ " AND emp_sales = 1"
					+ " AND incentive_emp_id IS NULL";
			if (!exe_id.equals("")) {
				StrSql += " AND emp_id IN (" + exe_id + ")";
			}

			// SOP("All Exe===" + StrSql);

			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					count++;
					// SOP("count==" + count);
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive"
							+ " (incentive_brand_id,"
							+ " incentive_emp_id,"
							+ " incentive_startdate)"
							+ " VALUES"
							+ " (" + crs.getString("branch_brand_id") + ","
							+ " " + crs.getString("emp_id") + ","
							+ " " + year + "00000000)";
					// SOP("Insert===" + StrSql);
					stmttx.addBatch(StrSql);
					if (count == 30 || crs.isLast()) {
						stmttx.executeBatch();
						count = 0;
					}
				}
				crs.close();
				conntx.commit();
			}

			// Exe Targets and Achieved
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
					+ " branch_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " LEFT JOIN "
					+ "( SELECT"
					+ " target_so_count AS sotarget,"
					+ " target_so_min AS somin,"
					+ " target_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_target"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(target_startdate, 1, 6) = '" + (year) + "'"
					+ " AND SUBSTR(target_enddate, 1, 6) = '" + (year) + "'"
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
					+ " SUM(IF(so_mga_amount != 0"
					+ " AND so_mga_amount >= (SELECT incentiveslab_accessmin "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE incentiveslab_brand_id = branch_brand_id"
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
					+ " LIMIT 1), so_mga_amount, 0)) AS somgaamount,"

					+ " SUM(IF(so_mga_amount != 0"
					+ " AND so_mga_amount >= (SELECT incentiveslab_accessmin "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE incentiveslab_brand_id = branch_brand_id"
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
					+ " LIMIT 1), 1, 0)) AS somgacount,"

					+ " SUM(so_exchange_amount) AS soexchangeamount,"
					+ " SUM(IF(so_exchange_amount !=0,1,0)) AS soexchangecount,"
					+ " so_emp_id"
					// + " branch_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(so_retail_date, 1, 6)  = '" + (year) + "'"
					+ " GROUP BY so_emp_id"
					+ " ) tblso ON tblso.so_emp_id = emp_id"
					+ " WHERE 1 = 1";
			if (!exe_id.equals("")) {
				StrSql += " AND emp_id IN (" + exe_id + ")";
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql=populate=" + StrSql);

			crs = processQuery(StrSql, 0);

			while (crs.next()) {
				insurCount++;
				HashMap<String, String> incentiveCount = new HashMap<String, String>();
				incentiveCount.put("sotarget", crs.getString("sotarget"));
				incentiveCount.put("somintarget", crs.getString("somin"));
				incentiveCount.put("soachieved", crs.getString("soachieved"));
				incentiveCount.put("targetper", getPercentage(Integer.parseInt(crs.getString("soachieved")), Integer.parseInt(crs.getString("sotarget"))));
				incentiveCount.put("insuramount", crs.getString("soinsuramount"));
				incentiveCount.put("ewamount", crs.getString("soewamount"));
				incentiveCount.put("mgaamount", crs.getString("somgaamount"));
				incentiveCount.put("exchangeamount", crs.getString("soexchangeamount"));
				incentiveCount.put("financeamount", crs.getString("sofinanceamount"));
				incentiveCount.put("financecount", crs.getString("sofinancecount"));
				incentiveCount.put("insurcount", crs.getString("soinsurancecount"));
				incentiveCount.put("ewcount", crs.getString("soewcount"));
				incentiveCount.put("mgacount", crs.getString("somgacount"));
				incentiveCount.put("exchangecount", crs.getString("soexchangecount"));
				incentiveCount.put("brand_id", crs.getString("branch_brand_id"));
				emp_details.put(crs.getString("emp_id"), incentiveCount);
			}
			// SOP("targetper==" + getPercentage(Integer.parseInt(soachieved), Integer.parseInt(sotarget)));
			// SOP("soachieved==" + soachieved);
			// SOP("sotarget==" + sotarget);

			crs.beforeFirst();
			for (int i = 0; i < insurCount; i++) {
				crs.next();
				int lakh = 0;
				incentiveByemp = emp_details.get(crs.getString("emp_id"));
				// SOPInfo("emp_id==" + crs.getString("emp_id"));
				// SOPInfo("incentiveByemp==" + incentiveByemp);
				soachieved = incentiveByemp.get("soachieved");
				soinsurcount = incentiveByemp.get("insurcount");
				so_brand_id = incentiveByemp.get("brand_id");
				sotarget = incentiveByemp.get("sotarget");
				somintarget = incentiveByemp.get("somintarget");
				targetper = incentiveByemp.get("targetper");
				insuramount = incentiveByemp.get("insuramount");
				ewamount = incentiveByemp.get("ewamount");
				mgaamount = incentiveByemp.get("mgaamount");
				exchangeamount = incentiveByemp.get("exchangeamount");
				financeamount = incentiveByemp.get("financeamount");
				lakh = (int) Double.parseDouble(financeamount) / 100000;
				sofinancecount = incentiveByemp.get("financecount");
				soewcount = incentiveByemp.get("ewcount");
				somgacount = incentiveByemp.get("mgacount");
				soexchangecount = incentiveByemp.get("exchangecount");

				// Insurance
				StrSql = " SELECT"
						+ " incentiveinsurslab_amt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
						+ " WHERE 1 = 1"
						+ " AND incentiveinsurslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveinsurslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveinsurslab_from <= '" + (getPercentage(Double.parseDouble(soinsurcount), Double.parseDouble(soachieved))) + "'"
						+ " AND incentiveinsurslab_to >= '" + (getPercentage(Double.parseDouble(soinsurcount), Double.parseDouble(soachieved))) + "'"
						+ " OR incentiveinsurslab_id = ( "
						+ " SELECT "
						+ " incentiveinsurslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur "
						+ " WHERE SUBSTR(incentiveinsurslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveinsurslab_brand_id = " + so_brand_id
						+ " AND incentiveinsurslab_from < '" + (getPercentage(Double.parseDouble(soinsurcount), Double.parseDouble(soachieved))) + "'"
						+ " ORDER BY incentiveinsurslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("incentiveinsurslab_amt=" + i + "=" + StrSql);
				incentive_insurance = Double.parseDouble(CNumeric(ExecuteQuery(StrSql))) * Integer.parseInt(soinsurcount);

				// Finance
				StrSql = " SELECT"
						+ " incentivefinanceslab_amt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
						+ " WHERE 1 = 1"
						+ " AND incentivefinanceslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentivefinanceslab_date,1,6) = '" + (year) + "'"
						+ " AND incentivefinanceslab_from <= '" + financeamount + "'"
						+ " AND incentivefinanceslab_to >= '" + financeamount + "'"
						+ " OR incentivefinanceslab_id = ( "
						+ " SELECT "
						+ " incentivefinanceslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance "
						+ " WHERE SUBSTR(incentivefinanceslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentivefinanceslab_brand_id = " + so_brand_id
						+ " AND incentivefinanceslab_from < '" + financeamount + "'"
						+ " ORDER BY incentivefinanceslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("StrSql==Fin==" + StrSql);

				incentive_finance = Double.parseDouble(CNumeric(ExecuteQuery(StrSql))) * lakh;

				// Accessories
				StrSql = " SELECT"
						+ " COALESCE (incentiveaccesslab_perc, 0.0) AS incentiveaccesslab_perc"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
						+ " WHERE 1 = 1"
						+ " AND incentiveaccesslab_brand_id = " + brand_id
						+ " AND SUBSTR(incentiveaccesslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveaccesslab_from <= '" + mgaamount + "'"
						+ " AND incentiveaccesslab_to >= '" + mgaamount + "'"
						+ " OR incentiveaccesslab_id = ( "
						+ " SELECT "
						+ " incentiveaccesslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories "
						+ " WHERE SUBSTR(incentiveaccesslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveaccesslab_brand_id = " + brand_id
						+ " AND incentiveaccesslab_from < '" + mgaamount + "'"
						+ " ORDER BY incentiveaccesslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("StrSql==Acces==" + StrSql);
				crs1 = processQuery(StrSql, 0);

				while (crs1.next()) {
					incentiveaccesslab_perc = crs1.getString("incentiveaccesslab_perc");
				}
				crs1.close();
				// SOP("incentiveaccesslab_perc==" + incentiveaccesslab_perc);
				incentive_access = deci.format((Double.parseDouble(incentiveaccesslab_perc) / 100) * Double.parseDouble(mgaamount));
				// SOP("incentive_access==" + incentive_access);

				// Ext. Warranty
				StrSql = " SELECT"
						+ " COALESCE (incentiveewslab_amt, 0) AS incentiveewslab_amt "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
						+ " WHERE 1 = 1"
						+ " AND incentiveewslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveewslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveewslab_from <= '" + (getPercentage(Double.parseDouble(soewcount), Double.parseDouble(soachieved))) + "'"
						+ " AND incentiveewslab_to >= '" + (getPercentage(Double.parseDouble(soewcount), Double.parseDouble(soachieved))) + "'"
						+ " OR incentiveewslab_id = ( "
						+ " SELECT "
						+ " incentiveewslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew "
						+ " WHERE SUBSTR(incentiveewslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveewslab_brand_id = " + so_brand_id
						+ " AND incentiveewslab_from < '" + (getPercentage(Double.parseDouble(soewcount), Double.parseDouble(soachieved))) + "'"
						+ " ORDER BY incentiveewslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";

				incentive_ew = Double.parseDouble(CNumeric(ExecuteQuery(StrSql))) * Integer.parseInt(soewcount);

				// Exchange
				StrSql = " SELECT"
						+ " COALESCE (incentiveexchangeslab_amt, 0) AS incentiveexchangeslab_amt "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
						+ " WHERE 1 = 1"
						+ " AND incentiveexchangeslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveexchangeslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveexchangeslab_from <= '" + (getPercentage(Double.parseDouble(soexchangecount), Double.parseDouble(soachieved))) + "'"
						+ " AND incentiveexchangeslab_to >= '" + (getPercentage(Double.parseDouble(soexchangecount), Double.parseDouble(soachieved))) + "'"
						+ " OR incentiveexchangeslab_id = ( "
						+ " SELECT "
						+ " incentiveexchangeslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange "
						+ " WHERE SUBSTR(incentiveexchangeslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveexchangeslab_brand_id = " + so_brand_id
						+ " AND incentiveexchangeslab_from < '" + (getPercentage(Double.parseDouble(soexchangecount), Double.parseDouble(soachieved))) + "'"
						+ " ORDER BY incentiveexchangeslab_id DESC LIMIT 1 "
						+ " )"

						+ " LIMIT 1";
				// SOP("StrSql==Ex==" + StrSql);
				incentive_exchange = Double.parseDouble(CNumeric(ExecuteQuery(StrSql))) * Integer.parseInt(soexchangecount);
				// SOP("incentive_exchange==" + incentive_exchange);

				// Slab-Wise
				// SO Slab
				StrSql = " SELECT"
						+ " incentiveslab_soamt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE 1 = 1"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveslab_from <= '" + (soachieved) + "'"
						+ " AND incentiveslab_to >= '" + (soachieved) + "'"
						+ " OR incentiveslab_id = ( "
						+ " SELECT "
						+ " incentiveslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
						+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND incentiveslab_from <= '" + soachieved + "'"
						+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOPInfo("IncentiveBySlab========1===" + StrSql);
				crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					slabachsoamount = crs1.getDouble("incentiveslab_soamt") * Double.parseDouble(soachieved);
				}
				crs1.close();

				// Insurance Slab
				StrSql = " SELECT"
						+ " incentiveslab_insuramt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE 1 = 1"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveslab_from <= '" + (soinsurcount) + "'"
						+ " AND incentiveslab_to >= '" + (soinsurcount) + "'"
						+ " OR incentiveslab_id = ( "
						+ " SELECT "
						+ " incentiveslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
						+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND incentiveslab_from <= '" + (soinsurcount) + "'"
						+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("IncentiveBySlab========3===" + StrSql);
				crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					slabachinsuramount = crs1.getDouble("incentiveslab_insuramt") * Integer.parseInt(soinsurcount);
				}
				crs1.close();

				// Finance Slab
				StrSql = " SELECT"
						+ " incentiveslab_financeamt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE 1 = 1"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveslab_from <= '" + (sofinancecount) + "'"
						+ " AND incentiveslab_to >= '" + (sofinancecount) + "'"
						+ " OR incentiveslab_id = ( "
						+ " SELECT "
						+ " incentiveslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
						+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND incentiveslab_from <= '" + (sofinancecount) + "'"
						+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("IncentiveBySlab========2===" + StrSql);
				crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					slabachfinanceamount = crs1.getDouble("incentiveslab_financeamt") * Integer.parseInt(sofinancecount);
				}
				crs1.close();

				// Accessories Slab
				StrSql = " SELECT"
						+ " incentiveslab_accessamt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE 1 = 1"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveslab_from <= '" + (somgacount) + "'"
						+ " AND incentiveslab_to >= '" + (somgacount) + "'"
						+ " OR incentiveslab_id = ( "
						+ " SELECT "
						+ " incentiveslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
						+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND incentiveslab_from <= '" + (somgacount) + "'"
						+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("IncentiveBySlab========4===" + StrSql);
				crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					slabachmgaamount = crs1.getDouble("incentiveslab_accessamt") * Integer.parseInt(somgacount);
				}
				crs1.close();

				// Ext. Warranty Slab
				StrSql = " SELECT"
						+ " incentiveslab_ewamt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE 1 = 1"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveslab_from <= '" + (soewcount) + "'"
						+ " AND incentiveslab_to >= '" + (soewcount) + "'"
						+ " OR incentiveslab_id = ( "
						+ " SELECT "
						+ " incentiveslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
						+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND incentiveslab_from <= '" + (soewcount) + "'"
						+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("IncentiveBySlab========5===" + StrSql);
				crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					slabachewamount = crs1.getDouble("incentiveslab_ewamt") * Integer.parseInt(soewcount);
					total += crs1.getDouble("incentiveslab_ewamt") * Integer.parseInt(soewcount);
				}
				crs1.close();

				// Exchange Slab
				StrSql = " SELECT"
						+ " incentiveslab_exchangeamt"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE 1 = 1"
						+ " AND incentiveslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = '" + (year) + "'"
						+ " AND incentiveslab_from <= '" + (soexchangecount) + "'"
						+ " AND incentiveslab_to >= '" + (soexchangecount) + "'"
						+ " OR incentiveslab_id = ( "
						+ " SELECT "
						+ " incentiveslab_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
						+ " WHERE incentiveslab_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentiveslab_date, 1, 6) = '" + (year) + "'"
						+ " AND incentiveslab_from <= '" + (soexchangecount) + "'"
						+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("IncentiveBySlab========6===" + StrSql);
				crs1 = processQuery(StrSql, 0);
				while (crs1.next()) {
					slabachexchangeamount = crs1.getDouble("incentiveslab_exchangeamt") * Integer.parseInt(soexchangecount);
				}
				crs1.close();

				// Overall-Wise
				StrSql = " SELECT"
						+ " incentivetargetband_amount"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON incentivetarget_id = incentivetargetband_incentivetarget_id"
						+ " WHERE 1 = 1"
						+ " AND incentivetarget_brand_id = " + so_brand_id
						+ " AND SUBSTR(incentivetarget_startdate,1,6) = '" + (year) + "'"
						+ " AND incentivetargetband_from <= '" + (targetper) + "'"
						+ " AND incentivetargetband_to >= '" + (targetper) + "'"
						+ " OR incentivetargetband_id = ( "
						+ " SELECT "
						+ " incentivetargetband_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band "
						+ " WHERE SUBSTR(incentivetarget_startdate, 1, 6) = '" + (year) + "'"
						+ " AND incentivetarget_brand_id = " + so_brand_id
						+ " AND incentivetargetband_from < '" + (targetper) + "'"
						+ " AND incentivetarget_brand_id = " + so_brand_id
						+ " ORDER BY incentivetargetband_id DESC LIMIT 1 "
						+ " )"
						+ " LIMIT 1";
				// SOP("IncentiveByOverall==" + StrSql);

				incentive_overall = Double.parseDouble(CNumeric(ExecuteQuery(StrSql)));
				total = (slabachsoamount + slabachinsuramount + slabachewamount + slabachfinanceamount + slabachmgaamount + slabachexchangeamount
						+ incentive_overall + incentive_insurance + incentive_finance + Double.parseDouble(incentive_access) + incentive_exchange
						+ incentive_ew);
				// SOP("total===" + total);

				incentive_total.put(crs.getString("emp_id"), total);

				// Update for slab, insurance, finance, ew, exchange, accessories and overall
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive"
						+ " SET"
						+ " incentive_soslab = " + slabachsoamount + ","
						+ " incentive_insurslab = " + slabachinsuramount + ","
						+ " incentive_ewslab = " + slabachewamount + ","
						+ " incentive_financeslab = " + slabachfinanceamount + ","
						+ " incentive_accessoriessslab = " + slabachmgaamount + ","
						+ " incentive_exchangeslab = " + slabachexchangeamount + ","
						+ " incentive_overall = " + incentive_overall + ","
						+ " incentive_insur = " + incentive_insurance + ","
						+ " incentive_finance = " + incentive_finance + ","
						+ " incentive_accessories = " + incentive_access + ","
						+ " incentive_exchange = " + incentive_exchange + ","
						+ " incentive_ew = " + incentive_ew + ""
						+ " WHERE incentive_emp_id = " + crs.getString("emp_id")
						+ " AND incentive_startdate = '" + year + "00000000'";
				// SOPInfo("UPDATE=" + i + "=" + StrSql);

				slabachsoamount = 0;
				slabachinsuramount = 0;
				slabachewamount = 0;
				slabachfinanceamount = 0;
				slabachmgaamount = 0;
				slabachexchangeamount = 0;
				incentive_overall = 0;
				incentive_insurance = 0;
				incentive_finance = 0;
				incentive_access = "0";
				incentive_exchange = 0;
				incentive_ew = 0;
				stmttx.addBatch(StrSql);
				if (i == 30 || crs.isLast()) {
					// SOPInfo("=123==Execute======");
					stmttx.executeBatch();
				}
			}
			crs.close();
			conntx.commit();

			// For Variant and Stock
			StrSql = " SELECT"
					+ " emp_id,"
					+ " SUM(incentivevariant_amount) AS variantincentive,"
					+ " SUM(vehstock_incentive) AS stockincentive"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_emp_id = emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_incentive_variant ON incentivevariant_item_id = so_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " WHERE 1 = 1"
					+ " AND branch_brand_id = " + brand_id
					+ " AND SUBSTR(so_retail_date, 1, 6) = '" + (year) + "'"
					+ " AND so_active = 1"
					+ " AND SUBSTR(incentivevariant_startdate, 1, 6) = '" + (year) + "'"
					+ " AND SUBSTR(incentivevariant_enddate, 1, 6) = '" + (year) + "'";
			if (!exe_id.equals("")) {
				StrSql += " AND emp_id IN (" + exe_id + ")";
			}
			StrSql += " GROUP BY so_emp_id";
			// SOP("IncentiveByVariant==" + StrSql);
			crs = processQuery(StrSql, 0);

			while (crs.next()) {
				count++;

				// Update for Variant, Stock and Total
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive"
						+ " SET"
						+ " incentive_variant = " + crs.getDouble("variantincentive") + ","
						+ " incentive_vehstock = " + crs.getDouble("stockincentive") + ""
						+ " WHERE incentive_emp_id = " + crs.getString("emp_id")
						+ " AND incentive_startdate = '" + year + "00000000'";
				// SOPInfo("variant====" + StrSql);

				stmttx.addBatch(StrSql);
				if (count == 30 || crs.isLast()) {
					stmttx.executeBatch();
				}
			}
			crs.close();
			conntx.commit();

			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
			}

			// Update for Total
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = incentive_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id "
					+ " SET"
					+ " incentive_total = (incentive_variant  "
					+ " + incentive_vehstock "
					+ " + incentive_soslab "
					+ " + incentive_insurslab "
					+ " + incentive_ewslab "
					+ " + incentive_financeslab "
					+ " + incentive_accessoriessslab "
					+ " + incentive_exchangeslab "
					+ " + incentive_overall "
					+ " + incentive_ew "
					+ " + incentive_insur "
					+ " + incentive_finance "
					+ " + incentive_accessories + incentive_exchange) "
					+ " WHERE 1 = 1"
					+ StrSearch
					+ " AND incentive_startdate = '" + year + "00000000'";
			// SOP("update total==" + StrSql);
			updateQuery(StrSql);

		} catch (Exception ex) {
			try {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		} finally {
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.setAutoCommit(true);
				conntx.close();
			}
		}
		StrHTML = "<b><font color=red>Incentive Calculated Successfully!</font></b>";
		return StrHTML;
	}
}
