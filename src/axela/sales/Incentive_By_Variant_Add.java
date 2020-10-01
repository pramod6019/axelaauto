// Ved Prakash (14 Feb 2013)
/*Modified By Sangita on 17th april 2013*/
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Incentive_By_Variant_Add extends Connect {

	public String branch_id = "0";
	public String incentive_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String comp_id = "0", brand_id = "0", incentivevariant_id = "", item_id = "", model_id = "0", emp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String SqlJoin = "";
	public String month = "", year = "";
	public String from_date = "", to_date = "", amount = "", add = "", incentivetarget_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		SOP("1...");
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			CheckPerm(comp_id, "emp_team_access", request, response);
			add = PadQuotes(request.getParameter("add_button"));
			msg = PadQuotes(request.getParameter("msg"));
			model_id = PadQuotes(request.getParameter("model"));
			SOP("model_id=" + model_id);
			item_id = PadQuotes(request.getParameter("item_id"));
			SOP("item_id=" + item_id);
			SOP("add=" + add);
			if (!comp_id.equals("0")) {
				if (add.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						StrHTML = AddIncentive();
					} else {
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
		SOP("get values");
		brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
		from_date = PadQuotes(request.getParameter("from_date"));
		to_date = PadQuotes(request.getParameter("to_date"));
		amount = PadQuotes(request.getParameter("amount"));
		month = CNumeric(PadQuotes(request.getParameter("dr_month")));
		if (month.length() == 1) {
			month = "0" + month;
		}
		year = CNumeric(PadQuotes(request.getParameter("dr_year")));
		item_id = PadQuotes(request.getParameter("item_id"));
		SOP("item_id==" + item_id);
	}

	protected String CheckForm() {
		msg = "";

		if (item_id.equals("0")) {
			msg += "<br>Select Variant!";
		}

		if (from_date.equals("")) {
			msg += "<br>Enter From Date!";
		}

		if (to_date.equals("")) {
			msg += "<br>Enter To Date!";
		}

		if (!from_date.equals("")) {
			from_date = from_date.replaceAll("nbsp", "&");

			StrSql = "SELECT "
					+ " incentivevariant_item_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_variant"
					+ " WHERE 1 = 1"
					+ " AND ((incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(from_date))
					+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(from_date)) + ")"
					+ " OR ( incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(to_date))
					+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(to_date)) + "))"
					+ " AND incentivevariant_item_id =" + item_id;
			// SOP("already==" + StrSql);
			if (ExecuteQuery(StrSql).equals("")) {
				if (Long.parseLong(ConvertShortDateToStr(to_date)) < Long.parseLong(ConvertShortDateToStr(from_date))) {
					StrHTML = "<br>From Date cannot be greater than To Date!";
				}
			} else {
				msg += "<br>Date Range already present!";
			}
		} else {
			msg += "<br>Enter From Date!";
		}

		if (!from_date.equals("")) {
			from_date = from_date.replaceAll("nbsp", "&");

			StrSql = "SELECT "
					+ " incentivevariant_item_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_incentive_variant"
					+ " WHERE 1 = 1"
					+ " AND ((incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(from_date))
					+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(from_date)) + ")"
					+ " OR ( incentivevariant_startdate <= " + Long.parseLong(ConvertShortDateToStr(to_date))
					+ " AND incentivevariant_enddate >= " + Long.parseLong(ConvertShortDateToStr(to_date)) + "))"
					+ " AND incentivevariant_item_id =" + item_id;
			// SOP("already==" + StrSql);
			if (ExecuteQuery(StrSql).equals("")) {
				if (Long.parseLong(ConvertShortDateToStr(to_date)) < Long.parseLong(ConvertShortDateToStr(from_date))) {
					StrHTML = "<br>To Date cannot be less than From Date!";
				}
			} else {
				msg += "<br>Date Range already present!";
			}
		} else {
			msg += "<br>Enter To Date!";
		}

		if (amount.equals("")) {
			msg += "<br>Enter Amount!";
		}

		// SOP("msg ===" + msg);

		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
		return msg;
	}

	public String AddIncentive() {
		// SOP("Add Incentive");
		try {

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_incentive_variant"
					+ " (incentivevariant_item_id,"
					+ " incentivevariant_startdate,"
					+ " incentivevariant_enddate,"
					+ " incentivevariant_amount,"
					+ " incentivevariant_entry_id,"
					+ " incentivevariant_entry_date)"
					+ " VALUES"
					+ " (" + item_id + ","
					+ " '" + ConvertShortDateToStr(from_date) + "',"
					+ " '" + ConvertShortDateToStr(to_date) + "',"
					+ " '" + amount + "',"
					+ " '" + emp_id + "',"
					+ "'" + ToLongDate(kknow()) + "')";
			SOP("StrSql====Add=" + StrSql);
			updateQuery(StrSql);
			StrHTML = "Incentive Added Successfully!";

		} catch (Exception e) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return StrHTML;
	}

	public String PopulateVariant(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			// //SOP(Str);
			StrSql = "SELECT item_id, item_name "
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1 = 1"
					+ " AND item_model_id = " + model_id
					+ " GROUP BY item_id "
					+ " ORDER BY item_name ";
			// SOP("StrSql======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(Selectdrop(crs.getInt("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
