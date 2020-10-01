package axela.insurance;
/*saiman 27th june 2012 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Manage_EnquiryDisposition extends Connect {
	// ////// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=index.jsp>Inventory</a>"
			+ " &gt; <a href=inventory-cat-list.jsp?all=yes>List Categories</a>:";
	public String LinkExportPage = "index.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=inventory-cat-update.jsp?Add=yes>Add New Category...</a>";
	public String ExportPerm = "";
	public String BranchAccess = "", branch_id = "0";
	public String StrHTML = "", emp_id = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrSql = "";
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
	public String roomtype_id = "";
	public String smart = "";
	private String cat_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Cat ID", "numeric", "cat_id"},
			{"Name", "text", "cat_name"},
			{"Active", "boolean", "cat_active"},
			{"Entry By", "text", "cat_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "cat_entry_date"},
			{"Modified By", "text", "cat_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "cat_modified_date"}
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
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND cat_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Categories!";
					StrSearch = StrSearch + " and cat_id > 0 ";
				} else if (!(cat_id.equals("0"))) {
					msg = msg + "<br>Results for Category ID= " + cat_id + "!";
					StrSearch = StrSearch + " and cat_id = " + cat_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND cat_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("itemcatstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("itemcatstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("itemcatstrsql", StrSearch, request);
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
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search

		StrSql = "SELECT cat_id, cat_name, count(item_id) AS itemcount";

		StrJoin = " FROM " + compdb(comp_id) + "axela_inventory_cat"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item on item_cat_id = cat_id"
				+ " WHERE 1 = 1";

		CountSql = "SELECT COUNT(distinct(cat_id))";

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

			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Categories";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "inventory-cat-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

			if (all.equals("yes")) {
				StrSql = StrSql + " GROUP BY cat_id ORDER BY cat_id desc";
			} else {
				StrSql = StrSql + " GROUP BY cat_id";
			}
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Id</th>\n");
				Str.append("<th>Categories</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=\"right\">").append(count).append("</td>\n");
					Str.append("<td align=\"right\">").append(crs.getString("cat_id")).append("</td>\n");
					Str.append("<td>").append(crs.getString("cat_name"));
					Str.append("</td>");
					Str.append("<td align=\"center\"><a href=\"inventory-cat-update.jsp?Update=yes&cat_id=");
					Str.append(crs.getString("cat_id")).append(" \">Update Category</a><br><a href=inventory-item-list.jsp?cat_id=");
					Str.append(crs.getString("cat_id")).append(" \">List Items</a> (").append(crs.getString("itemcount"));
					Str.append(")");
					Str.append("</td>\n");
				}
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Categories found!</font><br><br>";
		}
		return Str.toString();
	}
}
