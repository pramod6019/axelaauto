package axela.ws.axelaautoapp;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Executive_Univ_Check;
import axela.portal.Header;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_Incentive_HomeData extends Connect {

	public String StrSql = "";
	public String comp_id = "0";
	public String emp_id = "", emp_uuid = "", branch_id = "";
	public String brand_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSearch = "";
	public String emp_active = "", targetper = "", so_brand_id = "";
	public int soachieved = 0, sotarget = 0, somintarget = 0;
	public String sorateamount = "0.00", sofinanceamount = "0.00", soinsuramount = "0.00", soewamount = "0.00", somgaamount = "0.00", soexchangeamount = "0.00";
	public double financeamount = 0.00, insuramount = 0.00, ewamount = 0.00, mgaamount = 0.00, exchangeamount = 0.00;
	public String cardfinanceamount = "0.00", cardinsuramount = "0.00", cardewamount = "0.00", cardmgaamount = "0.00", cardexchangeamount = "0.00";
	public String sofinancecount = "0", soinsurcount = "0", soewcount = "0", somgacount = "0", soexchangecount = "0";
	public String soband = "0", financeband = "0", insurband = "0", ewband = "0", mgaband = "0", exchangeband = "0";
	public String slabachsoamount = "0.00", slabachfinanceamount = "0.00", slabachinsuramount = "0.00", slabachewamount = "0.00";
	public String slabachmgaamount = "0.00", slabachexchangeamount = "0.00", slabtotal = "0.00", incentivetotal = "0.00";
	public double grandTotal = 0.00;
	CachedRowSet crs = null;
	JSONObject output = new JSONObject();
	LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
	ArrayList<String> list = new ArrayList<String>();
	Executive_Univ_Check update = new Executive_Univ_Check();
	Gson gson = new Gson();
	DecimalFormat df = new DecimalFormat("0.00");
	public DecimalFormat deci = new DecimalFormat("0.00");

	public JSONObject IncentiveHome(JSONObject input, @Context HttpServletRequest request) throws Exception {
		try {
			if (AppRun().equals("0")) {
				SOP("input=======IncentiveHome=============" + input);
			}
			HttpSession session = request.getSession(true);
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = PadQuotes((String) input.get("emp_uuid"));
			}
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			brand_id = CNumeric(ExecuteQuery("SELECT branch_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_branch_id = branch_id"
					+ " WHERE emp_id =" + emp_id));
			if (!(emp_id).equals("0")) {
				// Start emp_active check
				emp_active = CNumeric(ExecuteQuery("SELECT emp_active"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id));
				if (emp_active.equals("0")) {
					map.put("emp_active", emp_active);
					return output;
				}
				// End emp_active check
				PopulateTarget();
				if (sotarget != 0) {
					IncentiveByVariant();
					IncentiveBySlab();
					IncentiveByOverall();
					IncentiveByInsurance();
					IncentiveByFinance();
					IncentiveByAccessories();
					IncentiveByEW();
					IncentiveByExchange();

					IncentiveSlabBand();
					IncentiveOverallBand();
					IncentiveInsurBand();
					IncentiveFinanceBand();
					IncentiveMgaBand();
					IncentiveEWBand();
					IncentiveExchangeBand();
					list.add(IndDecimalFormat(deci.format(grandTotal)));
					output.put("grandtotal", list);
				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto-App ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		if (AppRun().equals("0")) {
			SOP("output=======IncentiveHome=============" + output.toString(1));
		}
		return output;
	}
	private void PopulateTarget() throws SQLException {
		try {
			StrSql = " SELECT"
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
					+ " WHERE incentive_emp_id = " + emp_id
					+ " AND SUBSTR(incentive_startdate,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " ) AS incentivetotal "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN "
					+ "( SELECT"
					+ " COALESCE(target_so_count, 0) AS sotarget,"
					+ " target_so_min AS somin,"
					+ " target_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_target"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(target_startdate, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND SUBSTR(target_enddate, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " GROUP BY target_emp_id"
					+ " ) tblsotarget ON tblsotarget.target_emp_id = emp_id"
					+ " LEFT JOIN "
					+ "( SELECT"
					+ " COALESCE(COUNT(so_id), 0) AS soachieved,"
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
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " LIMIT 1), so_mga_amount, 0)) AS somgaamount,"

					+ " SUM(IF(so_mga_amount != 0"
					+ " AND so_mga_amount >= (SELECT incentiveslab_accessmin "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE incentiveslab_brand_id = branch_brand_id"
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " LIMIT 1), 1, 0)) AS somgacount,"

					// + " SUM(IF(so_mga_amount !=0,1,0)) AS somgacount,"
					+ " SUM(so_exchange_amount) AS soexchangeamount,"
					+ " SUM(IF(so_exchange_amount !=0,1,0)) AS soexchangecount,"
					+ " so_emp_id,"
					+ " branch_brand_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(so_retail_date, 1, 6)  = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " GROUP BY so_emp_id"
					+ " ) tblso ON tblso.so_emp_id = emp_id"
					+ " WHERE 1 = 1"
					+ " AND emp_id = " + emp_id
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			SOP("StrSql===========PopulateTarget============" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					sotarget = crs.getInt("sotarget");
					map.put("sotarget", String.valueOf(sotarget));
					map.put("somin", String.valueOf(crs.getInt("somin")));
					cardfinanceamount = IndDecimalFormat(deci.format(crs.getDouble("sofinanceamount")));
					soachieved = crs.getInt("soachieved");
					map.put("soachieved", String.valueOf(soachieved));
					targetper = getPercentage(crs.getInt("soachieved"), crs.getInt("sotarget"));
					SOP("targetper================" + targetper);
					map.put("targetper", targetper);
					map.put("sofinanceamount", String.valueOf(financeamount));
					map.put("cardfinanceamount", cardfinanceamount);
					cardinsuramount = IndDecimalFormat(deci.format(crs.getDouble("soinsuramount")));
					map.put("cardinsuramount", cardinsuramount);
					cardewamount = IndDecimalFormat(deci.format(crs.getDouble("soewamount")));
					map.put("cardewamount", cardewamount);
					cardmgaamount = IndDecimalFormat(deci.format(crs.getDouble("somgaamount")));
					map.put("cardmgaamount", cardmgaamount);
					cardexchangeamount = IndDecimalFormat(deci.format(crs.getDouble("soexchangeamount")));
					financeamount = crs.getDouble("sofinanceamount");
					map.put("cardexchangeamount", cardexchangeamount);
					sofinancecount = crs.getString("sofinancecount");
					map.put("sofinancecount", sofinancecount);
					soinsurcount = crs.getString("soinsurancecount");
					map.put("soinsurancecount", soinsurcount);
					soewcount = crs.getString("soewcount");
					map.put("soewcount", soewcount);
					somgacount = crs.getString("somgacount");
					map.put("somgacount", somgacount);
					soexchangecount = crs.getString("soexchangecount");
					map.put("soexchangecount", soexchangecount);
					map.put("incentivetotal", IndDecimalFormat(deci.format(crs.getDouble("incentivetotal"))));
					so_brand_id = crs.getString("branch_brand_id");
					list.add(gson.toJson(map));
					// emp_name = crs.getString("emp_name") + " (" + crs.getString("emp_ref_no") + ")";
				}
			}
			map.clear();
			output.put("populatetarget", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveByVariant() throws SQLException {
		try {
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
					+ " WHERE so_emp_id = " + emp_id
					+ " AND SUBSTR(so_retail_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " GROUP BY so_id"
					+ " ORDER BY so_retail_date";
			SOP("StrSql==========IncentiveByVariant============" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					count++;
					variant_total += crs.getDouble("incentivevariant_amount");
					stock_total += crs.getDouble("vehstock_incentive");
					map.put("count", String.valueOf(count));
					map.put("item_name", crs.getString("item_name"));
					map.put("so_id", crs.getString("so_id"));
					map.put("so_vehstock_id", crs.getString("so_vehstock_id"));
					map.put("so_retail_date", strToShortDate(crs.getString("so_retail_date")));
					map.put("so_insur_amount", IndDecimalFormat(deci.format(crs.getDouble("so_insur_amount"))));
					map.put("so_finance_amt", IndDecimalFormat(deci.format(crs.getDouble("so_finance_amt"))));
					map.put("so_mga_amount", IndDecimalFormat(deci.format(crs.getDouble("so_mga_amount"))));
					map.put("so_ew_amount", IndDecimalFormat(deci.format(crs.getDouble("so_ew_amount"))));
					map.put("so_exchange_amount", IndDecimalFormat(deci.format(crs.getDouble("so_exchange_amount"))));
					map.put("incentivevariant_amount", IndDecimalFormat(deci.format(crs.getDouble("incentivevariant_amount"))));
					map.put("vehstock_incentive", IndDecimalFormat(deci.format(crs.getDouble("vehstock_incentive"))));
					list.add(gson.toJson(map));
					map.clear();
				}
				grandTotal += variant_total + stock_total;
				map.put("variant_total", IndDecimalFormat(deci.format(variant_total)));
				map.put("stock_total", IndDecimalFormat(deci.format(stock_total)));
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("incentivebyvariant", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveBySlab() throws SQLException {
		try {
			int count = 0;
			double total = 0.0, amount = 0.00;
			CachedRowSet crs;
			map = new LinkedHashMap<String, String>();
			list = new ArrayList<String>();
			StrSql = " SELECT"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_soamt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE 1 = 1"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (soachieved) + "'"
					+ " AND incentiveslab_to >= '" + (soachieved) + "'"
					+ " OR incentiveslab_id = ( "
					+ " SELECT "
					+ " incentiveslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
					+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from < '" + (soachieved) + "'"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOPInfo("StrSql========1===" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				map.put("distributionby", "SO Retail");
				map.put("achieved", String.valueOf(soachieved));
				map.put("band", crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
				map.put("incentive", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt"))));
				map.put("amount", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt") * soachieved)));
				slabachsoamount = IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt") * soachieved));
				total += crs.getDouble("incentiveslab_soamt") * soachieved;
				list.add(gson.toJson(map));
				map.clear();
			}
			crs.close();

			StrSql = " SELECT"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_insuramt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE 1 = 1"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (soinsurcount) + "'"
					+ " AND incentiveslab_to >= '" + (soinsurcount) + "'"
					+ " OR incentiveslab_id = ( "
					+ " SELECT "
					+ " incentiveslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
					+ " WHERE SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (soinsurcount) + "'"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql========2==" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				map.put("distributionby", "Insurance");
				map.put("achieved", soinsurcount);
				map.put("band", crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
				map.put("incentive", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_insuramt"))));
				map.put("amount", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_insuramt") * Integer.parseInt(soinsurcount))));
				total += crs.getDouble("incentiveslab_insuramt") * Integer.parseInt(soinsurcount);
				list.add(gson.toJson(map));
				map.clear();
			}
			crs.close();

			StrSql = " SELECT"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_financeamt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE 1 = 1"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveslab_date,1,6) =  SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (sofinancecount) + "'"
					+ " AND incentiveslab_to >= '" + (sofinancecount) + "'"
					+ " OR incentiveslab_id = ( "
					+ " SELECT "
					+ " incentiveslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
					+ " WHERE SUBSTR(incentiveslab_date, 1, 6) =  SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (sofinancecount) + "'"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql========3==" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				map.put("distributionby", "Finance");
				map.put("achieved", sofinancecount);
				map.put("band", crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
				map.put("incentive", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_financeamt"))));
				map.put("amount", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_financeamt") * Integer.parseInt(sofinancecount))));
				total += crs.getDouble("incentiveslab_financeamt") * Integer.parseInt(sofinancecount);
				list.add(gson.toJson(map));
				map.clear();
			}
			crs.close();

			StrSql = " SELECT"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_accessamt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE 1 = 1"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (somgacount) + "'"
					+ " AND incentiveslab_to >= '" + (somgacount) + "'"
					+ " OR incentiveslab_id = ( "
					+ " SELECT "
					+ " incentiveslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
					+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (somgacount) + "'"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql=======4==" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				map.put("distributionby", "Accessories");
				map.put("achieved", somgacount);
				map.put("band", crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
				map.put("incentive", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessamt"))));
				map.put("amount", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessamt") * Integer.parseInt(somgacount))));
				total += crs.getDouble("incentiveslab_accessamt") * Integer.parseInt(somgacount);
				list.add(gson.toJson(map));
				map.clear();
			}
			crs.close();

			StrSql = " SELECT"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_ewamt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE 1 = 1"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (soewcount) + "'"
					+ " AND incentiveslab_to >= '" + (soewcount) + "'"
					+ " OR incentiveslab_id = ( "
					+ " SELECT "
					+ " incentiveslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
					+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (soewcount) + "'"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql=======5==" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				map.put("distributionby", "Ext. Warranty ");
				map.put("achieved", soewcount);
				map.put("band", crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
				map.put("incentive", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_ewamt"))));
				map.put("amount", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_ewamt") * Integer.parseInt(soewcount))));
				total += crs.getDouble("incentiveslab_ewamt") * Integer.parseInt(soewcount);
				list.add(gson.toJson(map));
				map.clear();
			}
			crs.close();

			StrSql = " SELECT"
					+ " incentiveslab_from,"
					+ " incentiveslab_to,"
					+ " incentiveslab_exchangeamt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE 1 = 1"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (soexchangecount) + "'"
					+ " AND incentiveslab_to >= '" + (soexchangecount) + "'"
					+ " OR incentiveslab_id = ( "
					+ " SELECT "
					+ " incentiveslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab "
					+ " WHERE SUBSTR(incentiveslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_from <= '" + (soexchangecount) + "'"
					+ " AND incentiveslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql=======6==" + StrSql);
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				map.put("distributionby", "Exchange");
				map.put("achieved", soexchangecount);
				map.put("band", crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
				map.put("incentive", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_exchangeamt"))));
				map.put("amount", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_exchangeamt") * Integer.parseInt(soexchangecount))));
				total += crs.getDouble("incentiveslab_exchangeamt") * Integer.parseInt(soexchangecount);
				list.add(gson.toJson(map));
				map.clear();
			}
			crs.close();
			slabtotal = IndDecimalFormat(deci.format(total));
			grandTotal += total;
			if (!slabtotal.equals("0.00")) {
				map.put("total", slabtotal);
				list.add(gson.toJson(map));
				map.clear();
			}
			if (!list.isEmpty()) {
				output.put("incentivebyslab", list);
			}
			list.clear();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveByOverall() {
		try {
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
					+ " AND SUBSTR(incentivetarget_startdate,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentivetargetband_from <= '" + (targetper) + "'"
					+ " AND incentivetargetband_to >= '" + (targetper) + "'"
					+ " OR incentivetargetband_id = ( "
					+ " SELECT "
					+ " incentivetargetband_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band "
					+ " WHERE SUBSTR(incentivetarget_startdate, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentivetargetband_from < '" + (targetper) + "'"
					+ " AND incentivetarget_brand_id = " + so_brand_id
					+ " ORDER BY incentivetargetband_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			SOP("StrSql=======IncentiveByOverall=========" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					count++;
					total += crs.getDouble("incentivetargetband_amount");
					map.put("count", String.valueOf(count));
					map.put("target_per", targetper);
					map.put("incentive_targetband", crs.getString("incentivetargetband_from") + "% -" + crs.getString("incentivetargetband_to") + "%");
					map.put("incentivetargetband_amount", IndDecimalFormat(deci.format(crs.getDouble("incentivetargetband_amount"))));
					map.put("incentive_total", IndDecimalFormat(deci.format(total)));
					list.add(gson.toJson(map));
					map.clear();
				}
				map.put("total", IndDecimalFormat(deci.format(total)));
				grandTotal += total;
				list.add(gson.toJson(map));
			}
			output.put("incentivebyoverall", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveByInsurance() {
		try {
			int count = 0;
			double total = 0.0, amount = 0.00;
			StrSql = " SELECT"
					+ " incentiveinsurslab_from,"
					+ " incentiveinsurslab_to,"
					+ " incentiveinsurslab_amt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
					+ " WHERE 1 = 1"
					+ " AND incentiveinsurslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveinsurslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveinsurslab_from <= '" + (getPercentage(Double.parseDouble(soinsurcount), soachieved)) + "'"
					+ " AND incentiveinsurslab_to >= '" + (getPercentage(Double.parseDouble(soinsurcount), soachieved)) + "'"
					+ " OR incentiveinsurslab_id = ( "
					+ " SELECT "
					+ " incentiveinsurslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur "
					+ " WHERE SUBSTR(incentiveinsurslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveinsurslab_from < '" + (getPercentage(Double.parseDouble(soinsurcount), soachieved)) + "'"
					+ " AND incentiveinsurslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveinsurslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql=======IncentiveByInsurance=======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					count++;
					total += crs.getDouble("incentiveinsurslab_amt") * Integer.parseInt(soinsurcount);
					map.put("count", String.valueOf(count));
					map.put("soinsurcount", soinsurcount);
					map.put("cardinsuramount", cardinsuramount);
					map.put("incentiveinsurslab", crs.getString("incentiveinsurslab_from") + "% -" + crs.getString("incentiveinsurslab_to") + "%");
					map.put("soinsurcount", soinsurcount);
					map.put("cardinsuramount", cardinsuramount);
					map.put("incentiveinsurslab_amt", IndDecimalFormat(deci.format(crs.getDouble("incentiveinsurslab_amt"))));
					map.put("incentiveinsur_total", IndDecimalFormat(deci.format(total)));
					list.add(gson.toJson(map));
					map.clear();
				}
				map.put("total", IndDecimalFormat(deci.format(total)));
				grandTotal += total;
				list.add(gson.toJson(map));
			}
			output.put("incentivebyinsurance", list);
			list.clear();
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveByFinance() throws SQLException {
		try {
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
					+ " AND SUBSTR(incentivefinanceslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentivefinanceslab_from <= '" + (cardfinanceamount.replaceAll(",", "")) + "'"
					+ " AND incentivefinanceslab_to >= '" + (cardfinanceamount.replaceAll(",", "")) + "'"
					+ " OR incentivefinanceslab_id = ( "
					+ " SELECT "
					+ " incentivefinanceslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance "
					+ " WHERE SUBSTR(incentivefinanceslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentivefinanceslab_from < '" + cardfinanceamount.replaceAll(",", "") + "'"
					+ " AND incentivefinanceslab_brand_id = " + so_brand_id
					+ " ORDER BY incentivefinanceslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql==IncentiveByFinance==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					count++;
					amount = (crs.getDouble("incentivefinanceslab_amt") * lakh);
					total += amount;
					map.put("count", String.valueOf(count));
					map.put("count", String.valueOf(count));
					map.put("sofinancecount", sofinancecount);
					map.put("cardfinanceamount", cardfinanceamount);
					map.put("incentivefinanceslabband",
							IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_from"))) + " -" + IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_to"))));
					map.put("incentivefinanceslab_amt", String.valueOf(crs.getDouble("incentivefinanceslab_amt")));
					map.put("incentivefinanceslab_total", IndDecimalFormat(deci.format(amount)));
					list.add(gson.toJson(map));
					map.clear();
				}
				map.put("total", IndDecimalFormat(deci.format(amount)));
				list.add(gson.toJson(map));
				map.clear();
				grandTotal += amount;
			}
			output.put("incentivebyfinance", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveByAccessories() {
		try {
			int count = 0;
			double total = 0.0, amount = 0.00;

			StrSql = " SELECT"
					+ " incentiveaccesslab_from,"
					+ " incentiveaccesslab_to,"
					+ " incentiveaccesslab_perc"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
					+ " WHERE 1 = 1"
					+ " AND incentiveaccesslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveaccesslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveaccesslab_from <= '" + (cardmgaamount.replaceAll(",", "")) + "'"
					+ " AND incentiveaccesslab_to >= '" + (cardmgaamount.replaceAll(",", "")) + "'"
					+ " OR incentiveaccesslab_id = ( "
					+ " SELECT "
					+ " incentiveaccesslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories "
					+ " WHERE SUBSTR(incentiveaccesslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveaccesslab_from < '" + cardmgaamount.replaceAll(",", "") + "'"
					+ " AND incentiveaccesslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveaccesslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql======IncentiveByAccessories========" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					count++;
					amount = (crs.getDouble("incentiveaccesslab_perc") / 100) * Double.parseDouble(cardmgaamount.replaceAll(",", ""));
					total += amount;
					map.put("count", String.valueOf(count));
					map.put("somgacount", somgacount);
					map.put("cardmgaamount", cardmgaamount);
					map.put("incentiveaccesslabband",
							IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_from"))) + " -" + IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_to"))));
					map.put("incentiveaccesslab_perc", crs.getDouble("incentiveaccesslab_perc") + "%");
					map.put("incentiveaccess_total", IndDecimalFormat(deci.format(amount)));
					list.add(gson.toJson(map));
					map.clear();
				}
				map.put("total", IndDecimalFormat(deci.format(amount)));
				list.add(gson.toJson(map));
				map.clear();
				grandTotal += amount;
			}
			output.put("incentivebyaccessories", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveByEW() {
		try {
			int count = 0;
			double total = 0.0, amount = 0.00;
			StrSql = " SELECT"
					+ " incentiveewslab_from,"
					+ " incentiveewslab_to,"
					+ " incentiveewslab_amt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
					+ " WHERE 1 = 1"
					+ " AND incentiveewslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveewslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveewslab_from <= '" + (getPercentage(Double.parseDouble(soewcount), soachieved)) + "'"
					+ " AND incentiveewslab_to >= '" + (getPercentage(Double.parseDouble(soewcount), soachieved)) + "'"
					+ " OR incentiveewslab_id = ( "
					+ " SELECT "
					+ " incentiveewslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew "
					+ " WHERE SUBSTR(incentiveewslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveewslab_from < '" + (getPercentage(Double.parseDouble(soewcount), soachieved)) + "'"
					+ " AND incentiveewslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveewslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql==IncentiveByEW===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					count++;
					total += crs.getDouble("incentiveewslab_amt") * Integer.parseInt(soewcount);
					map.put("count", String.valueOf(count));
					map.put("soewcount", soewcount);
					map.put("cardewamount", cardewamount);
					map.put("incentiveewslabband", crs.getString("incentiveewslab_from") + "% -" + crs.getString("incentiveewslab_to") + "%");
					map.put("incentiveewslab_amt", crs.getString("incentiveewslab_amt"));
					map.put("incentiveews_total", IndDecimalFormat(deci.format(total)));
					list.add(gson.toJson(map));
					map.clear();
				}
				map.put("total", IndDecimalFormat(deci.format(total)));
				grandTotal += total;
				list.add(gson.toJson(map));
				map.clear();
			}
			output.put("incentivebyew", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveByExchange() {
		try {
			int count = 0;
			double total = 0.0, amount = 0.00;
			StrSql = " SELECT"
					+ " incentiveexchangeslab_from,"
					+ " incentiveexchangeslab_to,"
					+ " incentiveexchangeslab_amt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
					+ " WHERE 1 = 1"
					+ " AND incentiveexchangeslab_brand_id = " + so_brand_id
					+ " AND SUBSTR(incentiveexchangeslab_date,1,6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveexchangeslab_from <= '" + (getPercentage(Double.parseDouble(soexchangecount), soachieved)) + "'"
					+ " AND incentiveexchangeslab_to >= '" + (getPercentage(Double.parseDouble(soexchangecount), soachieved)) + "'"
					+ " OR incentiveexchangeslab_id = ( "
					+ " SELECT "
					+ " incentiveexchangeslab_id "
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange "
					+ " WHERE SUBSTR(incentiveexchangeslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveexchangeslab_from < '" + (getPercentage(Double.parseDouble(soexchangecount), soachieved)) + "'"
					+ " AND incentiveexchangeslab_brand_id = " + so_brand_id
					+ " ORDER BY incentiveexchangeslab_id DESC LIMIT 1 "
					+ " )"
					+ " LIMIT 1";
			// SOP("StrSql==IncentiveByExchange==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map = new LinkedHashMap<String, String>();
				list = new ArrayList<String>();
				while (crs.next()) {
					count++;
					total += crs.getDouble("incentiveexchangeslab_amt") * Integer.parseInt(soexchangecount);
					map.put("count", String.valueOf(count));
					map.put("soexchangecount", soexchangecount);
					map.put("cardexchangeamount", cardexchangeamount);
					map.put("incentiveexchangesband", crs.getString("incentiveexchangeslab_from") + "% -" + crs.getString("incentiveexchangeslab_to") + "%");
					map.put("incentiveexchangeslab_amt", crs.getString("incentiveexchangeslab_amt"));
					map.put("incentiveexchanges_total", IndDecimalFormat(deci.format(total)));
					list.add(gson.toJson(map));
					map.clear();
				}
				map.put("total", IndDecimalFormat(deci.format(total)));
				list.add(gson.toJson(map));
				map.clear();
				grandTotal += total;
			}
			output.put("incentivebyexchange", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveSlabBand() {
		try {
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
					+ " AND SUBSTR(incentiveslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveslab_brand_id =" + brand_id;
			// + " ORDER BY incentivetargetband_from ";
			// SOP("StrSql========IncentiveSlabBand==========" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					map.put("count", String.valueOf(count));
					map.put("incentiveslabband", crs.getString("incentiveslab_from") + " - " + crs.getString("incentiveslab_to"));
					map.put("incentiveslab_soamt", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt"))));
					map.put("incentiveslab_financeamt", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_financeamt"))));
					map.put("incentiveslab_insuramt", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_insuramt"))));
					map.put("incentiveslab_ewamt", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_ewamt"))));
					map.put("incentiveslab_accessmin", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessmin"))));
					map.put("incentiveslab_accessamt", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessamt"))));
					map.put("incentiveslab_exchangeamt", IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_exchangeamt"))));
					list.add(gson.toJson(map));
					map.clear();
				}
			}
			output.put("incentiveslabband", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveOverallBand() {
		try {
			int count = 0;
			StrSql = "SELECT "
					+ " incentivetargetband_from,"
					+ " incentivetargetband_to,"
					+ " incentivetargetband_amount"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentivetarget_startdate, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentivetarget_brand_id =" + brand_id
					+ " ORDER BY incentivetargetband_from ";
			// SOP("StrSql===IncentiveOverallBand=======" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("count", String.valueOf(count));
					map.put("incentivetargetbandband", crs.getString("incentivetargetband_from") + "% - " + crs.getString("incentivetargetband_to") + "%");
					map.put("incentivetargetband_amount", IndDecimalFormat(deci.format(crs.getDouble("incentivetargetband_amount"))));
					list.add(gson.toJson(map));
					map.clear();
				}
			}
			output.put("incentiveoverallband", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveInsurBand() {
		try {
			int count = 0;
			StrSql = "SELECT "
					+ " incentiveinsurslab_id,"
					+ " incentiveinsurslab_from,"
					+ " incentiveinsurslab_to,"
					+ " incentiveinsurslab_amt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveinsurslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveinsurslab_brand_id =" + brand_id
					+ " ORDER BY incentiveinsurslab_date ";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					map.put("count", String.valueOf(count));
					map.put("incentiveinsurslabband", crs.getString("incentiveinsurslab_from") + "% - " + crs.getString("incentiveinsurslab_to") + "%");
					map.put("incentiveinsurslab_amt", IndDecimalFormat(deci.format(crs.getDouble("incentiveinsurslab_amt"))));
					list.add(gson.toJson(map));
					map.clear();
				}
			}
			output.put("incentiveinsurband", list);
			list.clear();
			crs.close();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveFinanceBand() {
		int count = 0;
		try {
			StrSql = "SELECT "
					+ " incentivefinanceslab_id,"
					+ " incentivefinanceslab_from,"
					+ " incentivefinanceslab_to,"
					+ " incentivefinanceslab_amt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentivefinanceslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentivefinanceslab_brand_id =" + brand_id
					+ " ORDER BY incentivefinanceslab_date ";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					map.put("count", String.valueOf(count));
					map.put("incentivefinanceslabband",
							IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_from"))) + " - " + IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_to"))));
					map.put("incentivefinanceslab_amt", IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_amt"))));
					list.add(gson.toJson(map));
					map.clear();
				}
			}
			output.put("incentivefinanceband", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveMgaBand() {
		try {
			int count = 0;
			StrSql = "SELECT "
					+ " incentiveaccesslab_id,"
					+ " incentiveaccesslab_from,"
					+ " incentiveaccesslab_to,"
					+ " incentiveaccesslab_perc"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveaccesslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveaccesslab_brand_id =" + brand_id
					+ " ORDER BY incentiveaccesslab_date ";
			// SOP("StrSql=====IncentiveMgaBand========" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					map.put("count", String.valueOf(count));
					map.put("incentiveaccesslaband",
							IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_from"))) + " - " + IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_to"))));
					map.put("incentiveaccesslab_perc", crs.getDouble("incentiveaccesslab_perc") + " %");
					list.add(gson.toJson(map));
					map.clear();
				}
			}
			output.put("incentivemgaband", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveEWBand() {
		try {
			int count = 0;
			StrSql = "SELECT "
					+ " incentiveewslab_id,"
					+ " incentiveewslab_from,"
					+ " incentiveewslab_to,"
					+ " incentiveewslab_amt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveewslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveewslab_brand_id =" + brand_id
					+ " ORDER BY incentiveewslab_date ";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					map.put("count", String.valueOf(count));
					map.put("incentiveewslabband", crs.getString("incentiveewslab_from") + "% - " + crs.getString("incentiveewslab_to") + "%");
					map.put("incentiveewslab_amt", IndDecimalFormat(deci.format(crs.getDouble("incentiveewslab_amt"))));
					list.add(gson.toJson(map));
					map.clear();
				}
			}
			output.put("incentiveewband", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void IncentiveExchangeBand() throws SQLException {
		try {
			int count = 0;
			StrSql = "SELECT "
					+ " incentiveexchangeslab_id,"
					+ " incentiveexchangeslab_from,"
					+ " incentiveexchangeslab_to,"
					+ " incentiveexchangeslab_amt"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(incentiveexchangeslab_date, 1, 6) = SUBSTR('" + ToLongDate(kknow()).replace("12", "01") + "',1,6)"
					+ " AND incentiveexchangeslab_brand_id =" + brand_id
					+ " ORDER BY incentiveexchangeslab_date ";
			SOP("StrSql=====IncentiveExchangeBand========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					count++;
					map.put("count", String.valueOf(count));
					map.put("incentiveexchangeslabband", crs.getString("incentiveexchangeslab_from") + "% - " + crs.getString("incentiveexchangeslab_to") + "%");
					map.put("incentiveexchangeslab_amt", String.valueOf(IndDecimalFormat(deci.format(crs.getDouble("incentiveexchangeslab_amt")))));
					list.add(gson.toJson(map));
					map.clear();
				}
			}
			output.put("incentiveexchangeband", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}