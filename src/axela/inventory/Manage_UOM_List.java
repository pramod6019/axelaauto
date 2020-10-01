package axela.inventory;
/* saiman 26th june 2012 */
//aJIt 9th October, 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Manage_UOM_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=../inventory/manage-uom-list.jsp?all=yes>List UOM</a>:";
	public String LinkExportPage = "warehouse.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=../inventory/manage-uom-update.jsp?add=yes>Add UOM...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String smart = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String uom_id = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Measurment ID", "numeric", "uom_id"},
			{"Name", "text", "uom_name"},
			{"Parent ID", "numeric", "uom_parent_id"},
			{"Entry By", "text", "uom_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "uom_entry_date"},
			{"Modified By", "text", "uom_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "uom_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				uom_id = CNumeric(PadQuotes(request.getParameter("uom_id")));

				if (!uom_id.equals("0")) {
					LinkAddPage = "<a href=../inventory/manage-uom-update.jsp?add=yes&uom_id=" + uom_id + ">Add Uom..</a>";
				}
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND uom_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All UOMs!";
					StrSearch = StrSearch + " and uom_id > 0 ";
				} else if (!(uom_id.equals("0"))) {
					msg = msg + "<br>Results for UOM ID = " + uom_id + "!";
					StrSearch = StrSearch + " and uom_id = " + uom_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND uom_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("itemsstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("itemsstrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("itemsstrsql", StrSearch, request);
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
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "SELECT uom_id, uom_name, uom_ratio, uom.uom_parent_id,"
					+ " COALESCE((SELECT uom_name FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE uom_id = uom.uom_parent_id), '') AS uom_parent_name";

			StrJoin = " FROM " + compdb(comp_id) + "axela_inventory_uom uom"
					+ " WHERE 1 = 1";

			CountSql = "SELECT COUNT(DISTINCT(uom_id))";

			StrSql = StrSql + StrJoin;
			// SOP("StrSql---" + StrSql);
			CountSql = CountSql + StrJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " UOM(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "manage-uom-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

				StrSql = StrSql + " GROUP BY uom_id ORDER BY uom_id DESC";

				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th data-toggle=\"true\">Name</th>\n");
					Str.append("<th>Parent</th>");
					Str.append("<th data-hide=\"phone, tablet\">Ratio</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("uom_name")).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("uom_parent_name")).append("</td>\n");
						Str.append("<td valign=top>");
						if (!crs.getString("uom_parent_name").equals("")) {
							Str.append(crs.getString("uom_ratio"));
						} else {
							Str.append("&nbsp;");
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=left nowrap><a href=\"manage-uom-update.jsp?update=yes&uom_id=");
						Str.append(crs.getString("uom_id")).append(" \">Update UOM</a><br>");
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
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
				RecCountDisplay = "<br><br><br><br><font color=red>No UOM(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
