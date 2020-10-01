// smitha nag june 6 2013
package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Salesorder_Status extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String Sqljoin = "";
	public String StrSearch = "";
	public String comp_id = "0";

	public String QueryString = "";
	public String report_id = "0";
	public String customer_id = "";
	public double running_bal = 0.00;
	public double trans_total = 0.00;
	public Double trans_total1 = 0.00;
	public String currentbal_opp_amount = "";
	public String accgroup_alie = "";
	public String voucher_id = "0";
	public String dr_module_id = "";
	public String LinkAddPage = "";
	public String start_date = "";
	public String end_date = "";

	public String startdate = "";
	public String enddate = "";
	public String go = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	public String item_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				report_id = CNumeric(PadQuotes(request.getParameter("report_id")));
				go = PadQuotes(request.getParameter("submit_button"));
				startdate = ReportStartdate();
				enddate = DateToShortDate(kknow());
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						StrHTML = Listdata();
					}
				}
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				startdate = "";
			}
		}
		if (!enddate.equals("")) {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (!startdate.equals("") && !enddate.equals("") && Long.parseLong(start_date) > Long.parseLong(end_date)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				enddate = "";
			}
		}

		if (item_id.equals("0")) {
			msg += "<br>Select Period!";
		}
		if (!start_date.equals("") && end_date.equals("")) {
			msg = msg + "<br>Select End Date!";
		} else if (start_date.equals("") && !end_date.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// customer_id = PadQuotes(request.getParameter("ledger"));
		startdate = PadQuotes(request.getParameter("txt_starttime"));
		enddate = PadQuotes(request.getParameter("txt_endtime"));
		item_id = CNumeric(PadQuotes(request.getParameter("dr_period")));
		SOP("period_id==" + item_id);
	}

	public String Listdata() {
		int count = 0;
		double inflow = 0.00, outflow = 0.00, difference = 0.00;
		String period = "";
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT voucher_id, voucher_date, vouchertrans_paymode_id, vouchertrans_amount, customer_name, \n"
				+ " accgroup_alie\n"
				+ " from axela_acc_voucher\n"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans on vouchertrans_voucher_id = voucher_id\n"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer on vouchertrans_customer_id = customer_id\n"
				// + " INNER JOIN  " + compdb(comp_id) + "axela_acc_subgroup on  accsubgroup_id = customer_accgroup_id\n"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group on  accgroup_id = customer_accgroup_id\n"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
				+ " where vouchertrans_paymode_id != 0"
				+ " AND branch_company_id = " + comp_id;

		try {
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"footable listtable\">");
				Str.append("<thead></tr>");
				Str.append("<tr align=center>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Period</th>\n");
				Str.append("<th data-hide=\"phone\">In-Flow</th>\n");
				Str.append("<th data-hide=\"phone\">Out-Flow</th>\n");
				Str.append("<th data-hide=\"phone\">Difference</th>\n");
				Str.append("</tr></thead>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align='center'>").append(count).append("</td>\n");
					Str.append("<td align='center'>").append(crs.getString("period")).append("</td>\n");
					Str.append("<td align='right'>").append(inflow).append("</td>\n");
					Str.append("<td align='right'>").append(outflow).append("</td>\n");
					Str.append("<td align='right'>").append(difference).append("</td>\n");
					Str.append("</tr>\n");
				}
				crs.close();
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Cash Flow Report Found!</b></font><br><br>");
			}
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}

	public String PopulateItem() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value='0'>Select</option>");
		StrSql = "select item_id, item_name"
				+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
				+ " where item_active = 1"
				+ " group by item_id"
				+ " order by item_name";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Str.append("<option value='").append(crs.getString("item_id") + "'");
				Str.append(Selectdrop(crs.getInt("item_id"), item_id)).append(">");
				Str.append(crs.getString("item_name")).append("</option>");
			}
			crs.close();;
		} catch (Exception ex) {
			SOP(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
