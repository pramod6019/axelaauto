// Ved Prakash (14 Feb 2013)
/*Modified By Sangita on 17th april 2013*/
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

public class Incentive_By_Target extends Connect {

	public String branch_id = "0";
	public String incentive_id = "0";
	public String StrHTML = "", StrHTML1 = "", StrHTML2 = "", StrHTML3 = "", StrHTML4 = "", StrHTML5 = "", StrHTML6 = "";
	public String msg = "";
	public String comp_id = "0", brand_id = "0", band_id = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String SqlJoin = "";
	public String month = "", year = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_team_access", request, response);
			if (!comp_id.equals("0")) {
				PopulateMonth(month);
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				// SOP("dr_brand==" + brand_id);
				month = CNumeric(PadQuotes(request.getParameter("dr_month")));
				if (month.length() == 1) {
					month = "0" + month;
				}
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));
				if (month.equals("00")) {
					month = (ConvertShortDateToStr(DateToShortDate(kknow())).substring(4, 6));
				}
				msg = PadQuotes(request.getParameter("msg"));
				CheckForm();
				if (msg.equals("")) {
					StrHTML = Listdata(brand_id, comp_id, year, month);
					StrHTML1 = SlabWise(brand_id, comp_id, year, month);
					StrHTML2 = InsuranceSlab(brand_id, comp_id, year, month);
					StrHTML3 = FinanceSlab(brand_id, comp_id, year, month);
					StrHTML4 = AccessoriesSlab(brand_id, comp_id, year, month);
					StrHTML5 = EWSlab(brand_id, comp_id, year, month);
					StrHTML6 = ExchangeSlab(brand_id, comp_id, year, month);
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

	public String Listdata(String brand_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT "
				+ " incentivetargetband_id,"
				+ " incentivetarget_brand_id,"
				+ " brand_name,"
				+ " incentivetarget_startdate,"
				+ " incentivetargetband_from,"
				+ " incentivetargetband_to,"
				+ " incentivetargetband_amount"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
				+ " INNER JOIN axelaauto.axela_brand ON brand_id = incentivetarget_brand_id"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentivetarget_startdate, 1, 6) = " + year + month
				+ " AND brand_id =" + brand_id
				+ " ORDER BY incentivetargetband_from ";

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th data-hide=\"phone\">Brand</th>\n");
				Str.append("<th>Band From</th>\n");
				Str.append("<th>Band To</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					band_id = crs.getString("incentivetargetband_id");
					count++;
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td>");
					Str.append(crs.getString("brand_name"));
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentivetargetband_from_" + band_id + "\" id=\"txt_incentivetargetband_from_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck('txt_incentivetargetband_from_" + band_id + "',this,'hint_txt_incentivetargetband_from_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentivetargetband_from") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivetargetband_from_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentivetargetband_to_" + band_id + "\" id=\"txt_incentivetargetband_to_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck('txt_incentivetargetband_to_" + band_id + "',this,'hint_txt_incentivetargetband_to_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentivetargetband_to") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivetargetband_to_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentivetargetband_amount_" + band_id + "\" id=\"txt_incentivetargetband_amount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck('txt_incentivetargetband_amount_" + band_id + "',this,'hint_txt_incentivetargetband_amount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentivetargetband_amount"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivetargetband_amount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + band_id + ", \"overallwise\")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Incentive Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
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

	public String PopulateYear(String year) {
		StringBuilder Str = new StringBuilder();
		for (int i = Integer.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow())))); i >= 2000; i--) {
			Str.append("<option value =").append(i).append("");
			Str.append(Selectdrop(i, year)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		if (brand_id.equals("0")) {
			msg = "Select Brand!<br>";
		}

		if (month.equals("00")) {
			msg += "Select Month!<br>";
		}

		if (year.equals("0")) {
			msg += "Select Year!<br>";
		}
		if (!msg.equals("")) {
			msg = "Error!<br>" + msg;
		}
	}

	protected String DeleteIncentive(String band_id, String brand_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_target_band"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_incentive_target ON  incentivetarget_id = incentivetargetband_incentivetarget_id"
					+ " WHERE incentivetargetband_id = " + band_id;
			// + " AND incentivetarget_brand_id = " + brand_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Incentive Band deleted successfully! ";
		}
		// StrHTML = Listdata(brand_id, comp_id, year, month);
		return msg;
	}

	protected String DeleteSlab(String band_id, String brand_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab"
					+ " WHERE incentiveslab_id = " + band_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Incentive Slab deleted successfully! ";
		}
		// StrHTML = Listdata(brand_id, comp_id, year, month);
		return msg;
	}

	public String SlabWise(String brand_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();

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
				int count = 0;
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>From</th>\n");
				Str.append("<th>To</th>\n");
				Str.append("<th>SO Amount</th>\n");
				Str.append("<th>Finance Amount</th>\n");
				Str.append("<th>Insurance Amount</th>\n");
				Str.append("<th>Ext. Warranty Amount</th>\n");
				Str.append("<th>Accessories Minimum</th>\n");
				Str.append("<th>Accessories Amount</th>\n");
				Str.append("<th>Exchange Amount</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					band_id = crs.getString("incentiveslab_id");
					count++;
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveslab_from_" + band_id + "\" id=\"txt_incentiveslab_from_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_from_" + band_id + "',this,'hint_txt_incentiveslab_from_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveslab_from") + "\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_from_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveslab_to_" + band_id + "\" id=\"txt_incentiveslab_to_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_to_" + band_id + "',this,'hint_txt_incentiveslab_to_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveslab_to") + "\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_to_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveslab_soamount_" + band_id + "\" id=\"txt_incentiveslab_soamount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_soamount_" + band_id + "',this,'hint_txt_incentiveslab_soamount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_soamt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_soamount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveslab_finamount_" + band_id + "\" id=\"txt_incentiveslab_finamount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_finamount_" + band_id + "',this,'hint_txt_incentiveslab_finamount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_financeamt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_finamount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveslab_insuramount_" + band_id + "\" id=\"txt_incentiveslab_insuramount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_insuramount_" + band_id + "',this,'hint_txt_incentiveslab_insuramount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_insuramt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_insuramount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveslab_ewamount_" + band_id + "\" id=\"txt_incentiveslab_ewamount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_ewamount_" + band_id + "',this,'hint_txt_incentiveslab_ewamount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_ewamt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_ewamount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveslab_accessminamount_" + band_id + "\" id=\"txt_incentiveslab_accessminamount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_accessminamount_" + band_id + "',this,'hint_txt_incentiveslab_accessminamount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessmin"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_accessminamount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveslab_accessamount_" + band_id + "\" id=\"txt_incentiveslab_accessamount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_accessamount_" + band_id + "',this,'hint_txt_incentiveslab_accessamount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_accessamt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_accessamount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveslab_exchangeamount_" + band_id + "\" id=\"txt_incentiveslab_exchangeamount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck2('txt_incentiveslab_exchangeamount_" + band_id + "',this,'hint_txt_incentiveslab_exchangeamount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveslab_exchangeamt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveslab_exchangeamount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + band_id + ", \"slabwise\")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Incentive Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected String DeleteInsurance(String band_id, String brand_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
					+ " WHERE incentiveinsurslab_id = " + band_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Insurance Slab deleted successfully! ";
		}
		// StrHTML = Listdata(brand_id, comp_id, year, month);
		return msg;
	}

	public String InsuranceSlab(String brand_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<div align=right>");
		// Str.append("<a class=\"button btn btn-success\" data-target='#Hintclicktocalldash80' data-toggle='modal href=../sales/incentive-by-overall-add.jsp?band_id=<%=mybean.band_id%>&brand_id="
		// + brand_id + "&month=" + month + "&year=" + year + "'>Add Incentive</a>");
		// Str.append("</div>");

		StrSql = "SELECT "
				+ " incentiveinsurslab_id,"
				+ " incentiveinsurslab_from,"
				+ " incentiveinsurslab_to,"
				+ " incentiveinsurslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_insur"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveinsurslab_date, 1, 6) = " + year + month
				+ " AND incentiveinsurslab_brand_id =" + brand_id
				+ " ORDER BY incentiveinsurslab_date ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>From</th>\n");
				Str.append("<th>To</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					band_id = crs.getString("incentiveinsurslab_id");
					count++;
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveinsurslab_from_" + band_id + "\" id=\"txt_incentiveinsurslab_from_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck3('txt_incentiveinsurslab_from_" + band_id + "',this,'hint_txt_incentiveinsurslab_from_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveinsurslab_from") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveinsurslab_from_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveinsurslab_to_" + band_id + "\" id=\"txt_incentiveinsurslab_to_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck3('txt_incentiveinsurslab_to_" + band_id + "',this,'hint_txt_incentiveinsurslab_to_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveinsurslab_to") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveinsurslab_to_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveinsurslab_amount_" + band_id + "\" id=\"txt_incentiveinsurslab_amount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck3('txt_incentiveinsurslab_amount_" + band_id + "',this,'hint_txt_incentiveinsurslab_amount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveinsurslab_amt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveinsurslab_amount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + band_id + ", \"insurancewise\")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Incentive Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected String DeleteAccessories(String band_id, String brand_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
					+ " WHERE incentiveaccesslab_id = " + band_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Accessories Slab deleted successfully! ";
		}
		// StrHTML = Listdata(brand_id, comp_id, year, month);
		return msg;
	}

	public String AccessoriesSlab(String brand_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<div align=right>");
		// Str.append("<a class=\"button btn btn-success\" data-target='#Hintclicktocalldash80' data-toggle='modal href=../sales/incentive-by-overall-add.jsp?band_id=<%=mybean.band_id%>&brand_id="
		// + brand_id + "&month=" + month + "&year=" + year + "'>Add Incentive</a>");
		// Str.append("</div>");

		StrSql = "SELECT "
				+ " incentiveaccesslab_id,"
				+ " incentiveaccesslab_from,"
				+ " incentiveaccesslab_to,"
				+ " incentiveaccesslab_perc"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_accessories"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveaccesslab_date, 1, 6) = " + year + month
				+ " AND incentiveaccesslab_brand_id =" + brand_id
				+ " ORDER BY incentiveaccesslab_date ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>From</th>\n");
				Str.append("<th>To</th>\n");
				Str.append("<th>Perentage</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					band_id = crs.getString("incentiveaccesslab_id");
					count++;
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveaccesslab_from_" + band_id + "\" id=\"txt_incentiveaccesslab_from_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck5('txt_incentiveaccesslab_from_" + band_id + "',this,'hint_txt_incentiveaccesslab_from_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_from"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveaccesslab_from_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveaccesslab_to_" + band_id + "\" id=\"txt_incentiveaccesslab_to_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck5('txt_incentiveaccesslab_to_" + band_id + "',this,'hint_txt_incentiveaccesslab_to_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveaccesslab_to"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveaccesslab_to_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveaccesslab_perc_" + band_id + "\" id=\"txt_incentiveaccesslab_perc_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck5('txt_incentiveaccesslab_perc_" + band_id + "',this,'hint_txt_incentiveaccesslab_perc_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveaccesslab_perc") + "%\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveaccesslab_perc_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + band_id + ", \"accessorieswise\")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Incentive Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected String DeleteEW(String band_id, String brand_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
					+ " WHERE incentiveewslab_id = " + band_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Extended Warranty Slab deleted successfully! ";
		}
		// StrHTML = Listdata(brand_id, comp_id, year, month);
		return msg;
	}

	public String EWSlab(String brand_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<div align=right>");
		// Str.append("<a class=\"button btn btn-success\" data-target='#Hintclicktocalldash80' data-toggle='modal href=../sales/incentive-by-overall-add.jsp?band_id=<%=mybean.band_id%>&brand_id="
		// + brand_id + "&month=" + month + "&year=" + year + "'>Add Incentive</a>");
		// Str.append("</div>");

		StrSql = "SELECT "
				+ " incentiveewslab_id,"
				+ " incentiveewslab_from,"
				+ " incentiveewslab_to,"
				+ " incentiveewslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_ew"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveewslab_date, 1, 6) = " + year + month
				+ " AND incentiveewslab_brand_id =" + brand_id
				+ " ORDER BY incentiveewslab_date ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>From</th>\n");
				Str.append("<th>To</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					band_id = crs.getString("incentiveewslab_id");
					count++;
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveewslab_from_" + band_id + "\" id=\"txt_incentiveewslab_from_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck6('txt_incentiveewslab_from_" + band_id + "',this,'hint_txt_incentiveewslab_from_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveewslab_from") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveewslab_from_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveewslab_to_" + band_id + "\" id=\"txt_incentiveewslab_to_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck6('txt_incentiveewslab_to_" + band_id + "',this,'hint_txt_incentiveewslab_to_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveewslab_to") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveewslab_to_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveewslab_amount_" + band_id + "\" id=\"txt_incentiveewslab_amount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck6('txt_incentiveewslab_amount_" + band_id + "',this,'hint_txt_incentiveewslab_amount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveewslab_amt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveewslab_amount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + band_id + ", \"ewwise\")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Incentive Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected String DeleteExchange(String band_id, String brand_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
					+ " WHERE incentiveexchangeslab_id = " + band_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Exchange Slab deleted successfully! ";
		}
		// StrHTML = Listdata(brand_id, comp_id, year, month);
		return msg;
	}

	public String ExchangeSlab(String brand_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<div align=right>");
		// Str.append("<a class=\"button btn btn-success\" data-target='#Hintclicktocalldash80' data-toggle='modal href=../sales/incentive-by-overall-add.jsp?band_id=<%=mybean.band_id%>&brand_id="
		// + brand_id + "&month=" + month + "&year=" + year + "'>Add Incentive</a>");
		// Str.append("</div>");

		StrSql = "SELECT "
				+ " incentiveexchangeslab_id,"
				+ " incentiveexchangeslab_from,"
				+ " incentiveexchangeslab_to,"
				+ " incentiveexchangeslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_exchange"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentiveexchangeslab_date, 1, 6) = " + year + month
				+ " AND incentiveexchangeslab_brand_id =" + brand_id
				+ " ORDER BY incentiveexchangeslab_date ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>From</th>\n");
				Str.append("<th>To</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					band_id = crs.getString("incentiveexchangeslab_id");
					count++;
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveexchangeslab_from_" + band_id + "\" id=\"txt_incentiveexchangeslab_from_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck7('txt_incentiveexchangeslab_from_" + band_id + "',this,'hint_txt_incentiveexchangeslab_from_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveexchangeslab_from") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveexchangeslab_from_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentiveexchangeslab_to_" + band_id + "\" id=\"txt_incentiveexchangeslab_to_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck7('txt_incentiveexchangeslab_to_" + band_id + "',this,'hint_txt_incentiveexchangeslab_to_" + band_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentiveexchangeslab_to") + "%\" size=\"4\" maxlength=\"4\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveexchangeslab_to_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentiveexchangeslab_amount_" + band_id + "\" id=\"txt_incentiveexchangeslab_amount_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck7('txt_incentiveexchangeslab_amount_" + band_id + "',this,'hint_txt_incentiveexchangeslab_amount_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentiveexchangeslab_amt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentiveexchangeslab_amount_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + band_id + ", \"exchangewise\")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Incentive Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected String DeleteFinance(String band_id, String brand_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
					+ " WHERE incentivefinanceslab_id = " + band_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Finance Slab deleted successfully! ";
		}
		// StrHTML = Listdata(brand_id, comp_id, year, month);
		return msg;
	}

	public String FinanceSlab(String brand_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<div align=right>");
		// Str.append("<a class=\"button btn btn-success\" data-target='#Hintclicktocalldash80' data-toggle='modal href=../sales/incentive-by-overall-add.jsp?band_id=<%=mybean.band_id%>&brand_id="
		// + brand_id + "&month=" + month + "&year=" + year + "'>Add Incentive</a>");
		// Str.append("</div>");

		StrSql = "SELECT "
				+ " incentivefinanceslab_id,"
				+ " incentivefinanceslab_from,"
				+ " incentivefinanceslab_to,"
				+ " incentivefinanceslab_amt"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_slab_finance"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentivefinanceslab_date, 1, 6) = " + year + month
				+ " AND incentivefinanceslab_brand_id =" + brand_id
				+ " ORDER BY incentivefinanceslab_date ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>From</th>\n");
				Str.append("<th>To</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					band_id = crs.getString("incentivefinanceslab_id");
					count++;
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentivefinanceslab_from_" + band_id + "\" id=\"txt_incentivefinanceslab_from_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck4('txt_incentivefinanceslab_from_" + band_id + "',this,'hint_txt_incentivefinanceslab_from_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_from"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivefinanceslab_from_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentivefinanceslab_to_" + band_id + "\" id=\"txt_incentivefinanceslab_to_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck4('txt_incentivefinanceslab_to_" + band_id + "',this,'hint_txt_incentivefinanceslab_to_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_to"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivefinanceslab_to_" + band_id + "\"></span>");
					Str.append("</td>");
					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentivefinanceslab_amt_" + band_id + "\" id=\"txt_incentivefinanceslab_amt_" + band_id + "\" type=\"text\" class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck4('txt_incentivefinanceslab_amt_" + band_id + "',this,'hint_txt_incentivefinanceslab_amt_" + band_id + "');\" ");
					Str.append("value=\"" + IndDecimalFormat(deci.format(crs.getDouble("incentivefinanceslab_amt"))) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivefinanceslab_amt_" + band_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + band_id + ", \"financewise\")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><font color=red><b>No Incentive Found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
