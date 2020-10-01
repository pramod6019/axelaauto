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

public class Report_ExpenseStatement extends Connect {

	public String StrHTML = "";

	public String msg = "";
	public String StrSql = "";
	public String StrSql2 = "";

	public String StrSql_Summary = "";
	public String StrSearch = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String comp_id = "0";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String vouchertype_name = "";
	public String QueryString = "";
	public String customer_id = "0";
	public double running_bal1 = 0.00;
	public double running_bal = 0.00;
	public double oldrunning_bal = 0.00;
	public double drtrans_total = 0.00;
	public double drtrans_total1 = 0.00, crtrans_total1 = 0.00;
	public Double trans_total1 = 0.00;
	public String start_date = "";
	public String end_date = "";
	public String startdate = "";
	public String enddate = "";
	public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	public String[] brand_ids, region_ids, jc_branch_ids;
	public String vouchertype_id = "0", accgroup_id = "0", accsubgroup_id = "0", branch_id = "0";
	public String go = "";
	DecimalFormat df = new DecimalFormat("0.00");
	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	public Ledger_Check ledgercheck = new Ledger_Check();
	public SubGroup_Check subgroupcheck = new SubGroup_Check();
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public axela.accounting.Acc_Check acccheck = new axela.accounting.Acc_Check();
	public double oppbalance = 0.00;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access,emp_report_access", request, response);
			if (!comp_id.equals("0")) {

				QueryString = PadQuotes(request.getQueryString());
				customer_id = CNumeric(PadQuotes(request.getParameter("ledger")));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				msg = PadQuotes(request.getParameter("msg"));
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
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	protected void CheckForm() {
		msg = "";
		if (customer_id.equals("0") || customer_id.equals("")) {
			if (accgroup_id.equals("0") || accgroup_id.equals("")) {
				if (branch_id.equals("0") || branch_id.equals("")) {
					msg += "<br>Select Group or Branch or Ledger!";
				}
			}
		}
		if (!startdate.equals("")) {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg += "<br>Enter Valid Start Date!";
			}
		}
		if (!enddate.equals("")) {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (!startdate.equals("") && !enddate.equals("")
						&& Long.parseLong(start_date) > Long.parseLong(end_date)) {
					msg += "<br>Start Date should be less than End date!";
				}
			} else {
				msg += "<br>Enter Valid End Date!";
			}
		}

		if (!customer_id.equals("") && !start_date.equals("") && end_date.equals("")) {
			msg += "<br>Select End Date!";
		} else if (!customer_id.equals("") && start_date.equals("") && !end_date.equals("")) {
			msg += "<br>Select Start Date!";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		customer_id = CNumeric(PadQuotes(request.getParameter("ledger")));
		startdate = PadQuotes(request.getParameter("txt_startdate"));
		enddate = PadQuotes(request.getParameter("txt_enddate"));
		vouchertype_id = CNumeric(PadQuotes(request.getParameter("dr_voucher_type")));
		accgroup_id = CNumeric(PadQuotes(request.getParameter("dr_group")));
		// accsubgroup_id = CNumeric(PadQuotes(request.getParameter("dr_subgroup")));
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		oppbalance = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("oppbalance"))));

	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int PrevStartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		double temptotal = 0.00;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		StrSql = "SELECT accgroup_id, customer_name, voucher_id,"
				+ " accgroup_name, accgroup_alie,"
				+ " (customer_open_bal +  (SELECT"
				+ " COALESCE((SUM(if(vouchertrans_dc=1,vouchertrans_amount,0))-SUM(IF(vouchertrans_dc=0,vouchertrans_amount,0))),0)"
				+ " FROM  " + compdb(comp_id) + "axela_acc_voucher "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id "
				+ " WHERE voucher_active = 1 AND vouchertrans_customer_id = customer_id "
				+ " AND SUBSTR(voucher_date ,1,8) < SUBSTR(" + start_date + ",1,8) )) AS oppbalance, "
				+ " voucher_id, voucher_vouchertype_id, voucher_date, voucher_no,"
				+ " voucher_ref_no, voucher_narration, voucher_notes, vouchertrans_amount, vouchertrans_dc,"
				+ " CONCAT(vouchertype_name,' ','No:',voucher_no) AS vouchertype_name, vouchertype_label,"
				+ " vouchertrans_paymode_id";
		SqlJoin += " FROM  " + compdb(comp_id) + "axela_acc_voucher "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
				// + " INNER JOIN  " + compdb(comp_id) + "axela_acc_subgroup ON accsubgroup_id = customer_accgroup_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id "
				+ " INNER JOIN  " + compdb(comp_id) + "axela_branch on branch_id = voucher_branch_id"
				+ " WHERE 1 = 1"
				+ " AND vouchertype_id = 16";

		CountSql = "SELECT COUNT(DISTINCT voucher_id)";
		if (!customer_id.equals("0")) {
			SqlJoin += " AND customer_id = " + customer_id;
		}
		if (!accgroup_id.equals("0")) {
			SqlJoin += " AND accgroup_id = " + accgroup_id;
		}
		if (!branch_id.equals("0")) {
			SqlJoin += " AND branch_id = " + branch_id;
		}
		SqlJoin += " AND SUBSTR(voucher_date,1,8) >= SUBSTR(" + start_date + ",1,8)"
				+ " AND SUBSTR(voucher_date,1,8) <= SUBSTR(" + end_date + ",1,8)";

		CountSql += SqlJoin;
		StrSql += SqlJoin + " Group BY voucher_id";

		TotalRecords = Integer.parseInt(CNumeric(PadQuotes(ExecutePrepQuery(CountSql, prepmap, 0) + "")));
		if (TotalRecords != 0) {

			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Voucher(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}

			StrSql += " ORDER BY voucher_date, voucher_id";
			StrSql_Summary = StrSql;

			CachedRowSet crs2 = processQuery(StrSql_Summary, 0);
			try {
				while (crs2.next()) {
					temptotal += Double.parseDouble(PadQuotes(crs2.getString("vouchertrans_amount")));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			// SOP("StrSql===voucher list===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			PageURL = "report-expensestatement.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			double tempoppbalance = 0.00;
			try {

				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");

				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th><span class=\"footable-toggle\"></span>ID</th>\n");
				Str.append("<th data-hide=\"phone\">Voucher</th>\n");
				Str.append("<th data-hide=\"phone\">Type</th>\n");
				Str.append("<th data-hide=\"phone\">Party Name</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Voucher No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Ref No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Running Balance</th>\n");
				Str.append("</tr></thead>\n");

				if (crs.isBeforeFirst()) {
					crs.first();
					if (PageCurrents.equals("1")) {
						oppbalance = Double.parseDouble(df.format(crs.getDouble("oppbalance")));
					} else if (!PageCurrents.equals("1")
							&& !PageCurrents.equals("0") && tempoppbalance != 0) {
						oppbalance = Double.parseDouble(df.format(tempoppbalance));
					}

					crs.beforeFirst();
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align='center'>").append(strToShortDate(crs.getString("voucher_date")));
						if (!crs.getString("voucher_notes").equals("")) {
							Str.append("<br>Comments: ").append(crs.getString("voucher_notes"));
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("voucher_id")).append("</td>\n");
						Str.append("<td valign=top align=center>");
						Str.append("<a href='../accounting/voucher-list.jsp?voucher_id=" + crs.getString("voucher_id"));
						Str.append("&vouchertype_id=" + crs.getString("voucher_vouchertype_id")).append("'>");
						Str.append(crs.getString("vouchertype_name") + "</a>").append("</td>\n");

						Str.append("<td valign=top align=left>").append(ExecuteQuery("SELECT customer_name"
								+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
								+ " WHERE vouchertrans_voucher_id = " + crs.getString("voucher_id")
								+ " AND vouchertrans_id = ("
								+ " SELECT vouchertrans_id + 1 FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
								+ " WHERE vouchertrans_voucher_id = " + crs.getString("voucher_id")
								+ " LIMIT 1)")).append("</td>\n");

						Str.append("<td valign=top align=left>").append(crs.getString("customer_name")).append("</td>\n");

						Str.append("<td valign=top align='center'>").append(crs.getString("voucher_no")).append("</td>\n");
						Str.append("<td valign=top align='center'>").append(crs.getString("voucher_ref_no")).append("</td>\n");

						Str.append("<td valign=top align=right>").append(crs.getString("vouchertrans_amount")).append("</td>\n");
						running_bal = running_bal + Double.parseDouble(crs.getString("vouchertrans_amount"));
						Str.append("<td valign=top align=right>");
						Str.append(df.format(Double.parseDouble(String.valueOf(running_bal))) + " DR");
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("<tr><td valign=top align=right colspan=9><b>Total:</b></td>\n");
					Str.append("<td valign=top align=right><b>\n").append(running_bal).append(" DR</b></td></tr>");
					crs.close();
					Str.append("<tr style=\"border:none;background-color:white; height:70px;\">");
					Str.append("<td colspan=9 valign=top align=center>").append(PageNaviStr);
					Str.append("</td>");
					Str.append("</tr>");
					Str.append("</table>");
					Str.append("</div>");
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=right colspan=9>").append("<b>Total:</b>").append("</td>\n");
					Str.append("<td valign=top align=right><b>").append(temptotal).append(" DR</b></td>\n");
					Str.append("</tr>\n");
					Str.append("</table>");
					Str.append("</div>");

				}
			} catch (Exception ex) {
				SOPError(" Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {

			RecCountDisplay = "<br><br><br><br><font color=red><b>No Voucher(s) Found!</b></font><br><br>";
		}
		return Str.toString();
	}
	public String PopulateGroup() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT accgrouppop_id, accgrouppop_name"
					+ " FROM  " + compdb(comp_id) + " axela_acc_group_pop"
					+ " ORDER BY accgrouppop_alie, accgrouppop_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("accgrouppop_id"));
				Str.append(StrSelectdrop(crs.getString("accgrouppop_id"), accgroup_id));
				Str.append(">").append(crs.getString("accgrouppop_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return Str.toString();
	}

}
