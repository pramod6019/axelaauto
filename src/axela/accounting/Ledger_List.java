package axela.accounting;
// shivaprasad 8 oct 2014

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Ledger_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=index.jsp>Accounting</a>"
			+ " &gt; <a href=ledger.jsp>Ledger</a> &gt; <a href=ledger-list.jsp?all=yes>List Ledger</a>:";
	public String LinkListPage = "ledger-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkPrintPage = "";
	public String LinkAddPage = "<a href=ledger-update.jsp?add=yes>Add Ledger...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	// public String comp_id = "0";
	public String smart = "";
	public String advhtml = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";

	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;

	int PageListSize = 10;
	int StartRec = 0;
	int EndRec = 0;
	int TotalRecords = 0;

	public String PageCurrents = "";
	public String QueryString = "";
	public String strq = "";
	public String customer_id = "0";
	public String comp_id = "0";
	public String all = "";
	public String advSearch = "";
	public String dialogue = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public Smart SmartSearch = new Smart();
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Ledger ID", "numeric", "customer_id"},
			{"Ledger Name", "text", "customer_name"},
			{"Ledger Code", "text", "customer_code"},
			{"Group", "text", "accgrouppop_name"},
			{"Opening Balance", "numeric", "customer_open_bal"},
			{"Closing Balance", "numeric", "customer_curr_bal"},
			{"Group", "text", "accgroup_name"},
			{"Entry By", "text", "customer_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "customer_entry_date"},
			{"Modified By", "text", "customer_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "customer_modified_date"}
	};
	public String accsubgroup_id = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id, emp_acc_voucher_access", request, response);
			if (!comp_id.equals("0"))
			{
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request)));
				emp_id = CNumeric(GetSession("emp_id", request)) + "";
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				smart = PadQuotes(request.getParameter("smart"));
				dialogue = PadQuotes(request.getParameter("dialogue"));
				accsubgroup_id = CNumeric(PadQuotes(request.getParameter("dr_subgroup")));
				// advhtml = BuildAdvHtml(request, response);
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND customer_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Ledgers!";
					StrSearch = StrSearch + " AND customer_id > 0";
				}
				else if (!customer_id.equals("0")) {
					msg = msg + "<br>Results for Ledger ID = " + customer_id + "!";
					StrSearch = StrSearch + " AND customer_id =" + customer_id;
					map.put(1, customer_id);
				}

				else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND customer_id = 0 ";
					}

					else {
						msg = "Results for Search!";
					}
				}

				if (!StrSearch.equals("")) {
					SetSession("ledgerstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	// public String BuildAdvHtml(HttpServletRequest request, HttpServletResponse response) {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<center>\n");
	// Str.append("<div class=\"container-fluid\"><div class=\"col-md-4 col-xs-12\"><label class=\"control-label col-md-4 col-xs-1\">Sub Group:</label>");
	// Str.append("<div class=\"col-md-8 col-xs-12\"><select name=\"dr_subgroup\" class=\"form-control\" id=\"dr_subgroup\">");
	// Str.append(PopulateSubGroup(accsubgroup_id, "", request));
	// Str.append("</select></div></div>\n");
	// Str.append("</center>\n");
	// if (!accsubgroup_id.equals("0")) {
	// StrSearch += " AND accsubgroup_id = " + accsubgroup_id + "";
	// }
	//
	// return Str.toString();
	// }

	public String Listdata() {

		String CountSql = "";
		String PageURL = "";
		CachedRowSet crs = null;
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "SELECT customer_id, customer_name,"
				+ " COALESCE ((CASE"
				+ " WHEN accgrouppop_alie = 1 THEN 'Assets'"
				+ " WHEN accgrouppop_alie = 2 THEN 'Liabilities'"
				+ " WHEN accgrouppop_alie = 3 THEN 'Income'"
				+ " WHEN accgrouppop_alie = 4 THEN 'Expense'"
				+ " WHEN accgrouppop_alie = 5 THEN 'Owners Equity'"
				+ " ELSE '' END ), '' ) AS accgrouppop_alie,"
				+ " customer_code, customer_open_bal, customer_curr_bal,"
				+ " customer_accgroup_id, accgrouppop_name";
		SqlJoin = " FROM " + compdb(comp_id) + "axela_customer"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
				+ " WHERE 1 = 1"
				+ " AND customer_type = 0";

		CountSql = "SELECT COUNT(DISTINCT customer_id)";
		StrSql += SqlJoin;

		CountSql += SqlJoin;
		if (!(StrSearch.equals(""))) {
			StrSql += StrSearch;
		}
		CountSql += StrSearch;
		// SOP("CountSql=====" + CountSql);
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		if (TotalRecords > 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}

			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Ledger(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "ledger-list.jsp?" + strq + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			StrSql = StrSql + " ORDER BY customer_id DESC";
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			// SOP("StrSql-------" + StrSql);
			try {
				crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">ID</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Code</th>\n");
				Str.append("<th data-toggle=\"true\">Ledger</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Group</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Opening Balance</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Current Balance</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr></thead><tboby>\n");
				while (crs.next())
				{
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("customer_id")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("customer_code")).append("</td>\n");
					Str.append("<td valign=top>" + " <a href=\"report-ledgerstatement.jsp?ledger=").append(crs.getString("customer_id"))
							.append(" \">").append(crs.getString("customer_name")).append("</a></td>\n");
					Str.append("<td valign=top>").append(crs.getString("accgrouppop_name")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("accgrouppop_alie")).append("</td>\n");
					Str.append("<td valign=top align=right>").append(df.format(Double.parseDouble(crs.getString("customer_open_bal")))).append("</td>\n");
					Str.append("<td valign=top align=right>").append(df.format(Double.parseDouble(crs.getString("customer_curr_bal")))).append("</td>\n");
					if (crs.getString("customer_accgroup_id").equals("32") || crs.getString("customer_accgroup_id").equals("31"))
					{
						Str.append("<td valign=top align=left> " + "<a href='../customer/customer-update.jsp?update=yes&customer_id=")
								.append(crs.getString("customer_id")).append("' \">Update Ledger</a></td>\n");
					}
					else
					{
						Str.append("<td valign=top align=left> " + " <a href=\"ledger-update.jsp?update=yes&customer_id=")
								.append(crs.getString("customer_id")).append(" \">Update Ledger</a></td>\n");
					}
				}
				crs.close();
				map.clear();
				Str.append("</tr>\n");
				Str.append("</tbody></table></div>\n");
			} catch (Exception ex) {

				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red><b>No Ledger(s) Found!</b></font><br><br>";
		}

		return Str.toString();

	}
	// public String PopulateSubGroup(String accsubgroup_id, String param, HttpServletRequest request) {
	// String SubGroupAccess = "";
	// StringBuilder stringval = new StringBuilder();
	// HttpSession session = request.getSession(true);
	// SubGroupAccess = GetSession("SubGroupAccess", request);
	// String comp_id = CNumeric(GetSession("comp_id", request));
	// try {
	// String SqlStr = "SELECT accsubgroup_id, accsubgroup_name"
	// + " FROM " + compdb(comp_id) + "axela_acc_subgroup";
	// SqlStr += SubGroupAccess + ""
	// + " ORDER BY accsubgroup_id, accsubgroup_name";
	// // SOP("SqlStr===" + SqlStr);
	// CachedRowSet crs = processQuery(SqlStr, 0);
	// if (param.equals("")) {
	// stringval.append("<option value =0>Select SubGroup</option>");
	// } else if (param.equals("all")) {
	// stringval.append("<option value =0>All SubGroup</option>");
	// } else {
	// stringval.append("<option value =0>").append(param).append("</option>");
	// }
	// while (crs.next()) {
	// stringval.append("<option value=").append(crs.getString("accsubgroup_id")).append(">")
	// .append(crs.getString("accsubgroup_name"))
	// .append("</option>\n");
	// }
	// crs.close();
	// return stringval.toString();
	// } catch (Exception ex) {
	// SOPError("AxelaAuto=== " + this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName()
	// + " : " + ex);
	// return "";
	// }
	// }

}
