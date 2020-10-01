// Ved Prakash (14 Feb 2013)
/*Modified By Sangita on 17th april 2013*/
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Incentive_By_Target_Add extends Connect {

	public String branch_id = "0";
	public String incentive_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0", brand_id = "0", band_id = "", emp_id = "0";
	public String StrSql = "", already = "";
	public String StrSearch = "";
	public String SqlJoin = "";
	public String month = "", year = "";
	public String band_from = "", band_to = "", amount = "", add = "", incentivetarget_id = "", percentage = "";
	public String slab_from = "", slab_to = "", so_amount = "", finance_amount = "", insurance_amount = "",
			ew_amount = "", accessmin_amt = "", accessmin_amount = "", access_amount = "", exchange_amount = "";
	public String type = "", addtype = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			CheckPerm(comp_id, "emp_team_access", request, response);
			add = PadQuotes(request.getParameter("add_button"));
			msg = PadQuotes(request.getParameter("msg"));
			type = PadQuotes(request.getParameter("type"));

			switch (type) {
				case "slabwise" :
					addtype = "Slab Wise";
					break;

				case "overallwise" :
					addtype = "Overall Wise";
					break;

				case "insurancewise" :
					addtype = "Insurance Wise";
					break;

				case "ewwise" :
					addtype = "Ext. Warranty Wise";
					break;

				case "exchangewise" :
					addtype = "Exchange Wise";
					break;

				case "financewise" :
					addtype = "Finance Wise";
					break;

				case "accessorieswise" :
					addtype = "Accessories Wise";
					break;

			}

			if (type.equals("slabwise")) {
				month = CNumeric(PadQuotes(request.getParameter("month")));
				if (month.length() == 1) {
					month = "0" + month;
				}
				year = CNumeric(PadQuotes(request.getParameter("year")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				StrSql = "SELECT incentiveslab_accessmin"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE incentiveslab_brand_id = " + brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = " + (year + month);
				// SOP("StrSql===" + StrSql);
				accessmin_amt = CNumeric(ExecuteQuery(StrSql));

				if (!accessmin_amt.equals("") && !accessmin_amt.equals("0")) {
					accessmin_amount = accessmin_amt;
				}

				// SOP("accessmin_amount===" + accessmin_amount);
			}
			if (!comp_id.equals("0")) {
				if (add.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					// SOP("msg==" + msg);
					// SOP("type===" + type);
					if (msg.equals("") && type.equals("overallwise")) {
						StrHTML = AddIncentive();
					}
					else if (msg.equals("") && type.equals("slabwise")) {
						StrHTML = AddSlab();
					}
					else if (msg.equals("") && type.equals("insurancewise")) {
						StrHTML = AddInsurSlab();
					}
					else if (msg.equals("") && type.equals("ewwise")) {
						StrHTML = AddEWSlab();
					}
					else if (msg.equals("") && type.equals("exchangewise")) {
						StrHTML = AddExchangeSlab();
					}
					else if (msg.equals("") && type.equals("financewise")) {
						StrHTML = AddFinanceSlab();
					}
					else if (msg.equals("") && type.equals("accessorieswise")) {
						StrHTML = AddAccessoriesSlab();
					}
					else {
						StrHTML = msg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
		band_id = CNumeric(PadQuotes(request.getParameter("band_id")));
		month = CNumeric(PadQuotes(request.getParameter("month")));
		if (month.length() == 1) {
			month = "0" + month;
		}
		year = CNumeric(PadQuotes(request.getParameter("year")));
		band_from = CNumeric(PadQuotes(request.getParameter("band_from")));
		band_to = CNumeric(PadQuotes(request.getParameter("band_to")));
		amount = CNumeric(PadQuotes(request.getParameter("amount")));
		percentage = CNumeric(PadQuotes(request.getParameter("percentage")));

		slab_from = PadQuotes(request.getParameter("slab_from"));
		slab_to = PadQuotes(request.getParameter("slab_to"));
		so_amount = CNumeric(PadQuotes(request.getParameter("so_amount")));
		finance_amount = CNumeric(PadQuotes(request.getParameter("finance_amount")));
		insurance_amount = CNumeric(PadQuotes(request.getParameter("insurance_amount")));
		ew_amount = CNumeric(PadQuotes(request.getParameter("ew_amount")));
		accessmin_amount = CNumeric(PadQuotes(request.getParameter("accessmin_amount")));
		access_amount = CNumeric(PadQuotes(request.getParameter("access_amount")));
		exchange_amount = CNumeric(PadQuotes(request.getParameter("exchange_amount")));
	}

	protected String CheckForm() {
		msg = "";
		if (type.equals("overallwise")) {
			if (!band_from.equals("")) {
				band_from = band_from.replaceAll("nbsp", "&");

				StrSql = "SELECT "
						+ " incentivetargetband_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
						+ " WHERE 1 = 1"
						+ " AND ((incentivetargetband_from <= " + band_from
						+ " AND incentivetargetband_to >= " + band_from + ")"
						+ " OR ( incentivetargetband_from <= " + band_to
						+ " AND incentivetargetband_to >= " + band_to + "))"
						+ " AND SUBSTR(incentivetarget_startdate,1,6) = '" + (year + month) + "'"
						+ " AND incentivetarget_brand_id =" + brand_id;
				// SOP("already==" + StrSql);
				already = CNumeric(ExecuteQuery(StrSql));
				// SOP("already==" + already);
				if (already.equals("0")) {
					if (Integer.parseInt(band_from) > Integer.parseInt(band_to)) {
						msg += "Band From cannot be less than Band To!";
					}
				} else {
					msg += "Band Range already present!";
				}
			} else {
				msg += "Enter Band From!";
			}

			if (!band_to.equals("")) {
				band_to = band_to.replaceAll("nbsp", "&");

				StrSql = "SELECT "
						+ " COALESCE(incentivetargetband_id, '') AS incentivetargetband_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
						+ " WHERE 1 = 1"
						+ " AND ((incentivetargetband_from <= " + band_from
						+ " AND incentivetargetband_to >= " + band_from + ")"
						+ " OR ( incentivetargetband_from <= " + band_to
						+ " AND incentivetargetband_to >= " + band_to + "))"
						+ " AND SUBSTR(incentivetarget_startdate, 1, 6) = '" + (year + month) + "'"
						+ " AND incentivetarget_brand_id =" + brand_id;
				// SOP("already==" + StrSql);
				already = ExecuteQuery(StrSql);
				if (ExecuteQuery(StrSql).equals("")) {
					if (Integer.parseInt(band_from) > Integer.parseInt(band_to)) {
						msg += "<br>Band To cannot be less than Band From!";
					}
				} else {
					msg += "<br>Band Range already present!";
				}
			} else {
				msg += "<br>Enter Band To!";
			}

			if (amount.equals("")) {
				msg += "<br>Enter Amount";
			}
		}

		if (!type.equals("overallwise")) {

			if (type.equals("slabwise")) {
				StrSql = "SELECT "
						+ " incentiveslab_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " WHERE 1 = 1"
						+ " AND (incentiveslab_from <= " + slab_from
						+ " AND incentiveslab_to >= " + slab_from + ")"
						+ " OR ( incentiveslab_from <= " + slab_to
						+ " AND incentiveslab_to >= " + slab_to + ")"
						+ " AND incentiveslab_id !=" + band_id
						+ " AND SUBSTR(incentiveslab_date,1,6) =" + (year + month)
						+ " AND incentiveslab_brand_id =" + brand_id;
			}
			if (type.equals("insurancewise")) {
				StrSql = "SELECT "
						+ " incentiveinsurslab_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
						+ " WHERE 1 = 1"
						+ " AND (incentiveinsurslab_from <= " + slab_from
						+ " AND incentiveinsurslab_to >= " + slab_from + ")"
						+ " OR ( incentiveinsurslab_from <= " + slab_to
						+ " AND incentiveinsurslab_to >= " + slab_to + ")"
						+ " AND incentiveinsurslab_id !=" + band_id
						+ " AND SUBSTR(incentiveinsurslab_date,1,6) =" + (year + month)
						+ " AND incentiveinsurslab_brand_id =" + brand_id;
			}
			if (type.equals("financewise")) {
				StrSql = "SELECT "
						+ " incentivefinanceslab_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
						+ " WHERE 1 = 1"
						+ " AND (incentivefinanceslab_from <= " + slab_from
						+ " AND incentivefinanceslab_to >= " + slab_from + ")"
						+ " OR ( incentivefinanceslab_from <= " + slab_to
						+ " AND incentivefinanceslab_to >= " + slab_to + ")"
						+ " AND incentivefinanceslab_id !=" + band_id
						+ " AND SUBSTR(incentivefinanceslab_date,1,6) =" + (year + month)
						+ " AND incentivefinanceslab_brand_id =" + brand_id;
			}
			if (type.equals("accessorieswise")) {
				StrSql = "SELECT "
						+ " incentiveaccesslab_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
						+ " WHERE 1 = 1"
						+ " AND (incentiveaccesslab_from <= " + slab_from
						+ " AND incentiveaccesslab_to >= " + slab_from + ")"
						+ " OR ( incentiveaccesslab_from <= " + slab_to
						+ " AND incentiveaccesslab_to >= " + slab_to + ")"
						+ " AND incentiveaccesslab_id !=" + band_id
						+ " AND SUBSTR(incentiveaccesslab_date,1,6) =" + (year + month)
						+ " AND incentiveaccesslab_brand_id =" + brand_id;
			}
			if (type.equals("ewwise")) {
				StrSql = "SELECT "
						+ " incentiveewslab_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
						+ " WHERE 1 = 1"
						+ " AND (incentiveewslab_from <= " + slab_from
						+ " AND incentiveewslab_to >= " + slab_from + ")"
						+ " OR ( incentiveewslab_from <= " + slab_to
						+ " AND incentiveewslab_to >= " + slab_to + ")"
						+ " AND incentiveewslab_id !=" + band_id
						+ " AND SUBSTR(incentiveewslab_date,1,6) =" + (year + month)
						+ " AND incentiveewslab_brand_id =" + brand_id;
			}
			if (type.equals("exchangewise")) {
				StrSql = "SELECT "
						+ " incentiveexchangeslab_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
						+ " WHERE 1 = 1"
						+ " AND (incentiveexchangeslab_from <= " + slab_from
						+ " AND incentiveexchangeslab_to >= " + slab_from + ")"
						+ " OR ( incentiveexchangeslab_from <= " + slab_to
						+ " AND incentiveexchangeslab_to >= " + slab_to + ")"
						+ " AND incentiveexchangeslab_id !=" + band_id
						+ " AND SUBSTR(incentiveexchangeslab_date,1,6) =" + (year + month)
						+ " AND incentiveexchangeslab_brand_id =" + brand_id;
			}
			if (!slab_from.equals("")) {
				slab_from = slab_from.replaceAll("nbsp", "&");
				// SOP("already==from==" + StrSql);
				already = CNumeric(ExecuteQuery(StrSql));
				// SOP("already==" + already);
				if (already.equals("0")) {
					if (Integer.parseInt(slab_from) > Integer.parseInt(slab_to)) {
						msg += "Slab From cannot be less than Slab To!";
					}
				} else {
					msg += "Slab Range already present!";
				}
			} else {
				msg += "Enter Slab From!";
			}

			if (!slab_to.equals("")) {
				slab_to = slab_to.replaceAll("nbsp", "&");
				// SOP("already==" + StrSql);
				already = ExecuteQuery(StrSql);
				if (ExecuteQuery(StrSql).equals("")) {
					if (Integer.parseInt(slab_from) > Integer.parseInt(slab_to)) {
						msg += "<br>Slab To cannot be less than Slab From!";
					}
				} else {
					msg += "<br>Slab Range already present!";
				}
			} else {
				msg += "<br>Enter Slab To!";
			}
		}
		if (type.equals("financewise") || type.equals("accessorieswise")) {
			if (percentage.equals("")) {
				msg += "<br>Enter Percentage";
			}
		}
		if (type.equals("insurancewise") || type.equals("ewwise") || type.equals("exchangewise")) {
			if (amount.equals("")) {
				msg += "<br>Enter Amount";
			}
		}
		// SOP("msg ===" + msg);
		if (!msg.equals("")) {
			msg = "Error!<br>" + msg;
		}
		return msg;
	}

	public String AddIncentive() {
		// SOP("Add Incentive");
		try {
			StrSql = "SELECT incentivetarget_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target "
					+ " WHERE 1 = 1"
					+ " AND incentivetarget_brand_id =" + brand_id
					+ " AND SUBSTR(incentivetarget_startdate, 1, 6) = " + year + month;
			// SOP("StrSql====incentivetarget_id=" + StrSql);
			incentivetarget_id = ExecuteQuery(StrSql);
			// SOP("incentivetarget_id===" + incentivetarget_id);

			if (!incentivetarget_id.equals("")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_target_band"
						+ " (incentivetargetband_incentivetarget_id,"
						+ " incentivetargetband_from,"
						+ " incentivetargetband_to,"
						+ " incentivetargetband_amount,"
						+ " incentivetargetband_entry_id,"
						+ " incentivetargetband_entry_date)"
						+ " VALUES"
						+ " (" + incentivetarget_id + ","
						+ " '" + band_from + "',"
						+ " " + band_to + ","
						+ " '" + amount + "',"
						+ " '" + emp_id + "',"
						+ "'" + ToLongDate(kknow()) + "')";
				// SOP("StrSql====Add=" + StrSql);
				updateQuery(StrSql);
				StrHTML = "Incentive Band Added Successfully!";
			} else {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_target"
						+ " (incentivetarget_brand_id,"
						+ " incentivetarget_startdate,"
						+ " incentivetarget_entry_id,"
						+ " incentivetarget_entry_date)"
						+ " VALUES"
						+ " (" + brand_id + ","
						+ " " + year + month + "00000000,"
						+ " '" + emp_id + "',"
						+ "'" + ToLongDate(kknow()) + "')";
				incentivetarget_id = UpdateQueryReturnID(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_target_band"
						+ " (incentivetargetband_incentivetarget_id,"
						+ " incentivetargetband_from,"
						+ " incentivetargetband_to,"
						+ " incentivetargetband_amount,"
						+ " incentivetargetband_entry_id,"
						+ " incentivetargetband_entry_date)"
						+ " VALUES"
						+ " (" + incentivetarget_id + ","
						+ " '" + band_from + "',"
						+ " " + band_to + ","
						+ " '" + amount + "',"
						+ " '" + emp_id + "',"
						+ "'" + ToLongDate(kknow()) + "')";
				// SOP("StrSql====Add=" + StrSql);
				updateQuery(StrSql);
				StrHTML = "Incentive Band Added Successfully!";
			}

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

	public String AddSlab() {

		try {

			if (!accessmin_amt.equals(accessmin_amount)) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_incentive_slab"
						+ " SET"
						+ " incentiveslab_accessmin = '" + accessmin_amount + "'"
						+ " WHERE incentiveslab_brand_id = " + brand_id
						+ " AND SUBSTR(incentiveslab_date,1,6) = " + (year + month);
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			}

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
					+ " VALUES"
					+ " (" + brand_id + ","
					+ " " + year + month + "00000000,"
					+ " '" + slab_from + "',"
					+ " " + slab_to + ","
					+ " '" + so_amount + "',"
					+ " '" + finance_amount + "',"
					+ " '" + insurance_amount + "',"
					+ " '" + ew_amount + "',"
					+ " '" + accessmin_amount + "',"
					+ " '" + access_amount + "',"
					+ " '" + exchange_amount + "',"
					+ " '" + emp_id + "',"
					+ "'" + ToLongDate(kknow()) + "')";
			// SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);
			StrHTML = "Incentive Slab Added Successfully!";

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

	public String AddInsurSlab() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
					+ " (incentiveinsurslab_brand_id,"
					+ " incentiveinsurslab_date,"
					+ " incentiveinsurslab_from,"
					+ " incentiveinsurslab_to,"
					+ " incentiveinsurslab_amt,"
					+ " incentiveinsurslab_entry_id,"
					+ " incentiveinsurslab_entry_date)"
					+ " VALUES"
					+ " (" + brand_id + ","
					+ " " + year + month + "00000000,"
					+ " '" + slab_from + "',"
					+ " " + slab_to + ","
					+ " '" + amount + "',"
					+ " '" + emp_id + "',"
					+ "'" + ToLongDate(kknow()) + "')";
			// SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);
			StrHTML = "Insurance Slab Added Successfully!";

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

	public String AddEWSlab() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
					+ " (incentiveewslab_brand_id,"
					+ " incentiveewslab_date,"
					+ " incentiveewslab_from,"
					+ " incentiveewslab_to,"
					+ " incentiveewslab_amt,"
					+ " incentiveewslab_entry_id,"
					+ " incentiveewslab_entry_date)"
					+ " VALUES"
					+ " (" + brand_id + ","
					+ " " + year + month + "00000000,"
					+ " '" + slab_from + "',"
					+ " " + slab_to + ","
					+ " '" + amount + "',"
					+ " '" + emp_id + "',"
					+ "'" + ToLongDate(kknow()) + "')";
			// SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);
			StrHTML = "Ext. Warranty Slab Added Successfully!";

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

	public String AddExchangeSlab() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
					+ " (incentiveexchangeslab_brand_id,"
					+ " incentiveexchangeslab_date,"
					+ " incentiveexchangeslab_from,"
					+ " incentiveexchangeslab_to,"
					+ " incentiveexchangeslab_amt,"
					+ " incentiveexchangeslab_entry_id,"
					+ " incentiveexchangeslab_entry_date)"
					+ " VALUES"
					+ " (" + brand_id + ","
					+ " " + year + month + "00000000,"
					+ " '" + slab_from + "',"
					+ " " + slab_to + ","
					+ " '" + amount + "',"
					+ " '" + emp_id + "',"
					+ "'" + ToLongDate(kknow()) + "')";
			// SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);
			StrHTML = "Exchange Slab Added Successfully!";

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

	public String AddFinanceSlab() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
					+ " (incentivefinanceslab_brand_id,"
					+ " incentivefinanceslab_date,"
					+ " incentivefinanceslab_from,"
					+ " incentivefinanceslab_to,"
					+ " incentivefinanceslab_amt,"
					+ " incentivefinanceslab_entry_id,"
					+ " incentivefinanceslab_entry_date)"
					+ " VALUES"
					+ " (" + brand_id + ","
					+ " " + year + month + "00000000,"
					+ " '" + slab_from + "',"
					+ " " + slab_to + ","
					+ " '" + amount + "',"
					+ " '" + emp_id + "',"
					+ "'" + ToLongDate(kknow()) + "')";
			// SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);
			StrHTML = "Finance Slab Added Successfully!";

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

	public String AddAccessoriesSlab() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
					+ " (incentiveaccesslab_brand_id,"
					+ " incentiveaccesslab_date,"
					+ " incentiveaccesslab_from,"
					+ " incentiveaccesslab_to,"
					+ " incentiveaccesslab_perc,"
					+ " incentiveaccesslab_entry_id,"
					+ " incentiveaccesslab_entry_date)"
					+ " VALUES"
					+ " (" + brand_id + ","
					+ " " + year + month + "00000000,"
					+ " '" + slab_from + "',"
					+ " " + slab_to + ","
					+ " '" + percentage + "',"
					+ " '" + emp_id + "',"
					+ "'" + ToLongDate(kknow()) + "')";
			// SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);
			StrHTML = "Accessories Slab Added Successfully!";

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

}
