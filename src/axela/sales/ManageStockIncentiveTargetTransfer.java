package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageStockIncentiveTargetTransfer extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a>&gt;&nbsp;</li><li><a href=manager.jsp>Business Manager</a> &gt;&nbsp </li><li><a href=managetargettransfer.jsp>Target Transfer</a>:</li>";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String brand_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public int curryear = 0;
	public String from_year = "", to_year = "";
	public String from_month = "", to_month = "";
	public String slab_from = "", slab_to = "", so_amount = "", finance_amount = "", insurance_amount = "", incentiveslab_date = "",
			ew_amount = "", accessmin_amt = "", accessmin_amount = "", access_amount = "", exchange_amount = "";
	public String year = "";
	public String go = "";

	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				from_year = PadQuotes(request.getParameter("dr_year"));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				go = PadQuotes(request.getParameter("submit_btn"));
				if (from_year.equals("0")) {
					from_year = curryear + "";
				}
				if (go.equals("Submit")) {
					GetValues(request);
					CheckForm();
					if (msg.equals("")) {
						OverallTargetTranfer();
						SlabWiseTargetTranfer();
						InsuranceWiseTargetTranfer();
						FinanceWiseTargetTranfer();
						AccesoriesWiseTargetTranfer();
						EwWiseTargetTranfer();
						ExchangeWiseTargetTranfer();
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void CheckForm() {

		msg = "";
		// if (brand_id.equals("")) {
		// msg = msg + "<br>Select Brand!<br>";
		// }
		if (from_year.equals("0")) {
			msg = msg + "<br>Select From Year!";
		}

		if (to_year.equals("0")) {
			msg = msg + "<br>Select To Year!";
		}
		if (from_month.equals("00")) {
			msg = msg + "<br>Select From Month!";
		}

		if (to_month.equals("00")) {
			msg = msg + "<br>Select To Month!";
		}

		if (!from_year.equals("0") && !from_month.equals("0") && !to_year.equals("0") && !to_month.equals("0")) {
			if ((Integer.parseInt(to_year + to_month) <= (Integer.parseInt(from_year + from_month)))) {
				msg = "<br>To Date should be greater than From Date!<br>";
			}
		}
	}

	protected void GetValues(HttpServletRequest request) {

		brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
		from_year = PadQuotes(request.getParameter("dr_from_year"));
		from_month = PadQuotes(request.getParameter("dr_from_month"));
		to_year = PadQuotes(request.getParameter("dr_to_year"));
		to_month = PadQuotes(request.getParameter("dr_to_month"));
		if (!brand_id.equals("")) {
			StrSearch += "AND branch_brand_id IN(" + brand_id + ")";
		}
	}

	protected void SlabWiseTargetTranfer() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = " + to_year + to_month
					+ " AND incentiveslab_brand_id = " + brand_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			StrSql = "SELECT "
					+ " " + brand_id + ","
					+ " " + to_year + to_month + "00000000,"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_soamt,"
					+ " incentiveslab_financeamt,"
					+ " incentiveslab_insuramt,"
					+ " incentiveslab_ewamt,"
					+ " incentiveslab_accessmin,"
					+ " incentiveslab_accessamt,"
					+ " incentiveslab_exchangeamt,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = " + from_year + from_month
					+ " AND incentiveslab_brand_id = " + brand_id;
			// SOP("StrSql======" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " (incentiveslab_brand_id,"
					+ " incentiveslab_date,"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_soamt,"
					+ " incentiveslab_financeamt,"
					+ " incentiveslab_insuramt,"
					+ " incentiveslab_ewamt,"
					+ " incentiveslab_accessmin,"
					+ " incentiveslab_accessamt,"
					+ " incentiveslab_exchangeamt,"
					+ " incentiveslab_entry_id,"
					+ " incentiveslab_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);

			msg += "<br>Slab Incentive Target Transfered Successfully!<br>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void OverallTargetTranfer() {
		String incentivetarget_id = "";
		try {

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
					+ " WHERE incentivetargetband_incentivetarget_id IN ("
					+ " SELECT "
					+ " incentivetarget_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target"
					+ " WHERE SUBSTR(incentivetarget_startdate,1,6) = " + to_year + to_month
					+ " AND incentivetarget_brand_id = " + brand_id
					+ " )";
			// SOP("delete=1=" + StrSql);
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_target"
					+ " WHERE SUBSTR(incentivetarget_startdate,1,6) = " + to_year + to_month
					+ " AND incentivetarget_brand_id = " + brand_id;
			// SOP("delete=2=" + StrSql);
			updateQuery(StrSql);

			StrSql = "SELECT "
					+ " " + brand_id + ","
					+ " " + to_year + to_month + "00000000,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentivetarget_startdate, 1, 6) = " + from_year + from_month
					+ " AND incentivetarget_brand_id = " + brand_id;
			// SOP("StrSql===1===" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_target"
					+ " (incentivetarget_brand_id,"
					+ " incentivetarget_startdate,"
					+ " incentivetarget_entry_id,"
					+ " incentivetarget_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql==1==Add=" + StrSql);
			incentivetarget_id = UpdateQueryReturnID(StrSql);

			StrSql = "SELECT "
					+ " " + incentivetarget_id + ","
					+ " incentivetargetband_from,"
					+ " incentivetargetband_to,"
					+ " incentivetargetband_amount,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON incentivetarget_id  = incentivetargetband_incentivetarget_id"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentivetarget_startdate, 1, 6) = " + from_year + from_month
					+ " AND incentivetarget_brand_id = " + brand_id;
			// SOP("StrSql==2====" + StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_target_band"
					+ " (incentivetargetband_incentivetarget_id,"
					+ " incentivetargetband_from,"
					+ " incentivetargetband_to,"
					+ " incentivetargetband_amount,"
					+ " incentivetargetband_entry_id,"
					+ " incentivetargetband_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql====Add=2==" + StrSql);
			updateQuery(StrSql);

			msg = "<br>Overall Target Transfered Successfully!<br>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void InsuranceWiseTargetTranfer() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
					+ " WHERE SUBSTR(incentiveinsurslab_date, 1, 6) = " + to_year + to_month
					+ " AND incentiveinsurslab_brand_id = " + brand_id;
			// SOP("delete==insur==" + StrSql);
			updateQuery(StrSql);

			StrSql = "SELECT "
					+ " " + brand_id + ","
					+ " " + to_year + to_month + "00000000,"
					+ " incentiveinsurslab_from,"
					+ " incentiveinsurslab_to,"
					+ " incentiveinsurslab_amt,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveinsurslab_date, 1, 6) = " + from_year + from_month
					+ " AND incentiveinsurslab_brand_id = " + brand_id;
			// SOP("StrSql===insur===" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
					+ " (incentiveinsurslab_brand_id,"
					+ " incentiveinsurslab_date,"
					+ " incentiveinsurslab_from,"
					+ " incentiveinsurslab_to,"
					+ " incentiveinsurslab_amt,"
					+ " incentiveinsurslab_entry_id,"
					+ " incentiveinsurslab_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql====Add=insur==" + StrSql);
			updateQuery(StrSql);

			msg += "<br>Insurance Target Transfered Successfully!<br>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void FinanceWiseTargetTranfer() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
					+ " WHERE SUBSTR(incentivefinanceslab_date, 1, 6) = " + to_year + to_month
					+ " AND incentivefinanceslab_brand_id = " + brand_id;
			// SOP("delete==fin==" + StrSql);
			updateQuery(StrSql);

			StrSql = "SELECT "
					+ " " + brand_id + ","
					+ " " + to_year + to_month + "00000000,"
					+ " incentivefinanceslab_from,"
					+ " incentivefinanceslab_to,"
					+ " incentivefinanceslab_amt,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentivefinanceslab_date, 1, 6) = " + from_year + from_month
					+ " AND incentivefinanceslab_brand_id = " + brand_id;
			// SOP("StrSql===fin===" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
					+ " (incentivefinanceslab_brand_id,"
					+ " incentivefinanceslab_date,"
					+ " incentivefinanceslab_from,"
					+ " incentivefinanceslab_to,"
					+ " incentivefinanceslab_amt,"
					+ " incentivefinanceslab_entry_id,"
					+ " incentivefinanceslab_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql====Add=fin==" + StrSql);
			updateQuery(StrSql);

			msg += "<br>Finance Target Transfered Successfully!<br>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void AccesoriesWiseTargetTranfer() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
					+ " WHERE SUBSTR(incentiveaccesslab_date, 1, 6) = " + to_year + to_month
					+ " AND incentiveaccesslab_brand_id = " + brand_id;
			// SOP("delete==acces==" + StrSql);
			updateQuery(StrSql);

			StrSql = "SELECT "
					+ " " + brand_id + ","
					+ " " + to_year + to_month + "00000000,"
					+ " incentiveaccesslab_from,"
					+ " incentiveaccesslab_to,"
					+ " incentiveaccesslab_perc,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveaccesslab_date, 1, 6) = " + from_year + from_month
					+ " AND incentiveaccesslab_brand_id = " + brand_id;
			// SOP("StrSql===acces===" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
					+ " (incentiveaccesslab_brand_id,"
					+ " incentiveaccesslab_date,"
					+ " incentiveaccesslab_from,"
					+ " incentiveaccesslab_to,"
					+ " incentiveaccesslab_perc,"
					+ " incentiveaccesslab_entry_id,"
					+ " incentiveaccesslab_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql====Add=acces==" + StrSql);
			updateQuery(StrSql);

			msg += "<br>Accessories Target Transfered Successfully!<br>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void EwWiseTargetTranfer() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
					+ " WHERE SUBSTR(incentiveewslab_date, 1, 6) = " + to_year + to_month
					+ " AND incentiveewslab_brand_id = " + brand_id;
			// SOP("delete==Ew==" + StrSql);
			updateQuery(StrSql);

			StrSql = "SELECT "
					+ " " + brand_id + ","
					+ " " + to_year + to_month + "00000000,"
					+ " incentiveewslab_from,"
					+ " incentiveewslab_to,"
					+ " incentiveewslab_amt,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveewslab_date, 1, 6) = " + from_year + from_month
					+ " AND incentiveewslab_brand_id = " + brand_id;
			// SOP("StrSql===Ew===" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
					+ " (incentiveewslab_brand_id,"
					+ " incentiveewslab_date,"
					+ " incentiveewslab_from,"
					+ " incentiveewslab_to,"
					+ " incentiveewslab_amt,"
					+ " incentiveewslab_entry_id,"
					+ " incentiveewslab_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql====Add=Ew==" + StrSql);
			updateQuery(StrSql);

			msg += "<br>Extended Warranty Target Transfered Successfully!<br>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void ExchangeWiseTargetTranfer() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
					+ " WHERE SUBSTR(incentiveexchangeslab_date, 1, 6) = " + to_year + to_month
					+ " AND incentiveexchangeslab_brand_id = " + brand_id;
			// SOP("delete==exchange==" + StrSql);
			updateQuery(StrSql);

			StrSql = "SELECT "
					+ " " + brand_id + ","
					+ " " + to_year + to_month + "00000000,"
					+ " incentiveexchangeslab_from,"
					+ " incentiveexchangeslab_to,"
					+ " incentiveexchangeslab_amt,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveexchangeslab_date, 1, 6) = " + from_year + from_month
					+ " AND incentiveexchangeslab_brand_id = " + brand_id;
			// SOP("StrSql===exchange===" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
					+ " (incentiveexchangeslab_brand_id,"
					+ " incentiveexchangeslab_date,"
					+ " incentiveexchangeslab_from,"
					+ " incentiveexchangeslab_to,"
					+ " incentiveexchangeslab_amt,"
					+ " incentiveexchangeslab_entry_id,"
					+ " incentiveexchangeslab_entry_date)"
					+ " "
					+ StrSql + "";
			// SOP("StrSql====Add=exchange==" + StrSql);
			updateQuery(StrSql);

			msg += "<br>Exchange Target Transfered Successfully!<br>";

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

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

	public String PopulateYear(String year, int yeartype) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		if (yeartype == 0) {
			for (int i = curryear - 3; i <= curryear + 3; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		} else if (yeartype == 1) {
			for (int i = curryear - 1; i <= curryear + 1; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		}
		return Str.toString();
	}
	public String PopulateMonth(String month) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=00").append(StrSelectdrop("00", month)).append(">Select</option>");
		Str.append("<option value=01").append(StrSelectdrop("01", month)).append(">January</option>");
		Str.append("<option value=02").append(StrSelectdrop("02", month)).append(">February</option>");
		Str.append("<option value=03").append(StrSelectdrop("03", month)).append(">March</option>");
		Str.append("<option value=04").append(StrSelectdrop("04", month)).append(">April</option>");
		Str.append("<option value=05").append(StrSelectdrop("05", month)).append(">May</option>");
		Str.append("<option value=06").append(StrSelectdrop("06", month)).append(">June</option>");
		Str.append("<option value=07").append(StrSelectdrop("07", month)).append(">July</option>");
		Str.append("<option value=08").append(StrSelectdrop("08", month)).append(">August</option>");
		Str.append("<option value=09").append(StrSelectdrop("09", month)).append(">September</option>");
		Str.append("<option value=10").append(StrSelectdrop("10", month)).append(">October</option>");
		Str.append("<option value=11").append(StrSelectdrop("11", month)).append(">November</option>");
		Str.append("<option value=12").append(StrSelectdrop("12", month)).append(">December</option>");

		return Str.toString();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		doPost(request, response);
	}

}
