package axela.inventory;
//aJIt 9th October, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Inventory_Salescat_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=index.jsp>Inventory</a> &gt; <a href=inventory-salescat-list.jsp?all=yes>List Sales Categories</a>:";
	public String LinkExportPage = "index.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=inventory-salescat-update.jsp?Add=yes>Add New Sales Category...</a>";
	public String ExportPerm = "";
	public String BranchAccess = "", branch_id = "0";
	public String StrHTML = "", emp_id = "0";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String group = "";
	public String smart = "";
	private String salescat_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Salescat ID", "numeric", "salescat_id"},
			{"Name", "text", "salescat_name"},
			{"Entry By", "text", "salescat_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "salescat_entry_date"},
			{"Modified By", "text", "salescat_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "salescat_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				salescat_id = CNumeric(PadQuotes(request.getParameter("salescat_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND salescat_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Sales Categories!";
					StrSearch = StrSearch + " and salescat_id > 0 ";
				} else if (!salescat_id.equals("0")) {
					msg = msg + "<br>Results for Sales Category ID= " + salescat_id + "!";
					StrSearch = StrSearch + " and salescat_id =" + salescat_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND salescat_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("salescatstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("salescatstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("salescatstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		StringBuilder Str = new StringBuilder();
		int TotalRecords = 0;
		String StrJoin = "";
		String CountSql = "";
		String PageURL = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search

			StrSql = "Select salescat_id, salescat_name, count(trans_item_id) as itemcount";

			StrJoin = " from " + compdb(comp_id) + "axela_inventory_salescat"
					+ " left join " + compdb(comp_id) + "axela_inventory_salescat_trans on trans_salescat_id = salescat_id"
					+ " where 1=1";

			CountSql = "SELECT Count(distinct(salescat_id))";

			StrSql = StrSql + StrJoin;
			CountSql = CountSql + StrJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}

			CountSql = ExecuteQuery(CountSql);
			if (!CountSql.equals("")) {
				TotalRecords = Integer.parseInt(CountSql);
			} else {
				TotalRecords = 0;
			}

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Sales Categories";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "inventory-salescat-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				if (all.equals("yes")) {
					StrSql = StrSql + " group by salescat_id order by salescat_id desc ";
				} else {
					StrSql = StrSql + " group by salescat_id order by salescat_id";
				}
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

				try {

					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-bordered\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Sales Categories</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead><tr>\n");
					Str.append("<tbody><tr>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td>").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("salescat_name"));
						Str.append("</td>");
						Str.append("<td nowrap>");
						Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
								+ "<button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"inventory-salescat-update.jsp?Update=yes&salescat_id="
								+ crs.getString("salescat_id") + " \">Update Sales Category</a></li>"
								+ "<li role=presentation><a href=inventory-item-list.jsp?salescat_id=" + crs.getString("salescat_id") + " \">List Items ("
								+ crs.getString("itemcount") + ")</a></li></ul></div></center></div>");
						Str.append("</td>\n");
					}
					Str.append("</tr>\n");

					Str.append("</tbody><tr>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Sales Categories found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
