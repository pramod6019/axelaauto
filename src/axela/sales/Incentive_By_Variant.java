// Ved Prakash (14 Feb 2013)
/*Modified By Sangita on 17th april 2013*/
package axela.sales;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Incentive_By_Variant extends Connect {

	public String branch_id = "0";
	public String incentive_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0", brand_id = "0", model_id = "0", incentivevariant_id = "", item_id = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String SqlJoin = "";
	public String month = "", year = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_team_access", request, response);
			if (!comp_id.equals("0")) {
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
				model_id = CNumeric(PadQuotes(request.getParameter("dr_model")));
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
					StrHTML = Listdata(brand_id, model_id, comp_id, year, month);
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

	public String Listdata(String brand_id, String model_id, String comp_id, String year, String month) {
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT "
				+ " item_id, item_name,"
				+ " incentivevariant_id, incentivevariant_startdate,"
				+ " incentivevariant_enddate,"
				+ " incentivevariant_amount"
				+ " FROM " + compdb(comp_id) + "axela_sales_incentive_variant"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = incentivevariant_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ " WHERE 1 = 1"
				+ " AND SUBSTR(incentivevariant_startdate, 1, 6) = " + year + month
				+ " AND model_brand_id =" + brand_id
				+ " AND item_model_id =" + model_id
				+ " GROUP BY incentivevariant_id"
				+ " ORDER BY incentivevariant_startdate ";
		// SOP("list==" + StrSql);

		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th>Count</th>\n");
				Str.append("<th data-hide=\"phone\">Variant</th>\n");
				Str.append("<th>From Date</th>\n");
				Str.append("<th>To Date</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Delete</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					incentivevariant_id = crs.getString("incentivevariant_id");
					item_id = crs.getString("item_id");
					Str.append("<tr>");
					Str.append("<td align=center>");
					Str.append(count);
					Str.append("</td>");
					Str.append("<td>");
					Str.append(crs.getString("item_name"));
					Str.append("</td>");
					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentivevariant_startdate_" + incentivevariant_id + "_" + item_id + "\""
							+ " id=\"txt_incentivevariant_startdate_" + incentivevariant_id + "_" + item_id + "\""
							+ " type=\"text\""
							+ " class=\"form-control datepicker\" ");
					Str.append("onChange=\"SecurityCheck('txt_incentivevariant_startdate_" + incentivevariant_id + "_" + item_id + "',this,'hint_txt_incentivevariant_startdate_" + incentivevariant_id
							+ "_" + item_id + "');\" ");
					Str.append("value=\"" + strToShortDate(crs.getString("incentivevariant_startdate")) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivevariant_startdate_" + incentivevariant_id + "_" + item_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<input name=\"txt_incentivevariant_enddate_" + incentivevariant_id + "_" + item_id + "\""
							+ " id=\"txt_incentivevariant_enddate_" + incentivevariant_id + "_" + item_id + "\""
							+ " type=\"text\""
							+ " class=\"form-control datepicker\" ");
					Str.append("onChange=\"SecurityCheck('txt_incentivevariant_enddate_" + incentivevariant_id + "_" + item_id + "',this,'hint_txt_incentivevariant_enddate_" + incentivevariant_id
							+ "_" + item_id + "');\" ");
					Str.append("value=\"" + strToShortDate(crs.getString("incentivevariant_enddate")) + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivevariant_enddate_" + incentivevariant_id + "_" + item_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append("<input name=\"txt_incentivevariant_amount_" + incentivevariant_id + "_" + item_id + "\""
							+ " id=\"txt_incentivevariant_amount_" + incentivevariant_id + "_" + item_id + "\""
							+ " type=\"text\""
							+ " class=\"form-control\" ");
					Str.append("onChange=\"SecurityCheck('txt_incentivevariant_amount_" + incentivevariant_id + "_" + item_id + "',this,'hint_txt_incentivevariant_amount_" + incentivevariant_id + "_"
							+ item_id + "');\" ");
					Str.append("value=\"" + crs.getString("incentivevariant_amount") + "\" size=\"10\" maxlength=\"10\" /> ");
					Str.append("<span class=\"hint\" id=\"hint_txt_incentivevariant_amount_" + incentivevariant_id + "_" + item_id + "\"></span>");
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append("<b><a class=\"button btn btn-success glyphicon glyphicon-trash\" onclick='deleteIncentive(" + incentivevariant_id + ")'").append("></a></b>");
					Str.append("</td>");

					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
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
			Str.append("<option value ='0'>Select</option>");
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

	public String PopulateModel(String brand_id, String model_id, String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			// //SOP(Str);
			StrSql = "SELECT model_id, model_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1"
					+ " AND model_brand_id = " + brand_id
					+ " GROUP BY model_id "
					+ " ORDER BY model_name ";
			// SOP("StrSql======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value ='0'>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(Selectdrop(crs.getInt("model_id"), model_id));
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
		if (model_id.equals("0")) {
			msg += "Select Model!<br>";
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

	protected String DeleteIncentive(String incentivevariant_id, String comp_id) {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_incentive_variant"
					+ " WHERE incentivevariant_id = " + incentivevariant_id;
			// SOP("delete==" + StrSql);
			updateQuery(StrSql);
			msg = "Incentive Band deleted successfully! ";
		}
		return msg;
	}

}
