// Ved Prakash (14 Feb 2013)--
//edited 30 april, 2 may 2013(smitha n)
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageTestDriveColour extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managetestdrivecolour.jsp?all=yes>List Colours</a><b>:</b>";
	public String LinkExportPage = "colour-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=managetestdrivecolour-update.jsp?add=yes>Add New Colour...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String QueryString = "";
	public String StrSearch = "";
	public String SqlJoin = "";
	public int recperpage = 0;
	public String RecCountDisplay = "";
	public int PageCount = 10;
	public int PageCurrent = 0;
	public int PageSize = 0;
	public String PageURL = "";
	public String PageNaviStr = "";
	public String PageCurrents = "";
	public String colour_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String all = "";
	public String smart = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Colour ID", "numeric", "colour_id"},
			{"Colour Name", "text", "colour_name"},
			{"Colour Code", "text", "colour_code"},
			{"Item ID", "numeric", "colour_item_id"},
			{"Item Name", "text", "item_name"},
			{"Item Code", "text", "item_code"},
			{"Model ID", "numeric", "colour_model_id"},
			{"Model Name", "text", "model_name"},
			{"Active", "boolean", "colour_active"},
			{"Entry By", "text", "colour_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "colour_entry_date"},
			{"Modified By", "text", "colour_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "colour_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				colour_id = CNumeric(PadQuotes(request.getParameter("colour_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND colour_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Colours!";
					StrSearch = StrSearch + " and colour_id > 0";
				} else if (!(colour_id.equals("0"))) {
					msg = msg + "<br>Results for Colour ID = " + colour_id + "!";
					StrSearch = StrSearch + " and colour_id = " + colour_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Results for Search!";
					}
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
		String active = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			StrSql = "Select colour_id, colour_name, colour_code, colour_active, model_name, item_id, concat(item_name, ' (', item_code, ')') as item_name ";

			SqlJoin = " from " + compdb(comp_id) + "axela_sales_testdrive_colour"
					+ " inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id=colour_model_id  "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id=colour_item_id "
					+ " where 1=1";
			CountSql = "SELECT Count(distinct colour_id)";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

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

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Colour(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "managetestdrivecolour.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " order by colour_id desc";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";
				try {
					// SOP("StrSql of testdrive colour === "+StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Colour</th>\n");
					Str.append("<th>Code</th>\n");
					Str.append("<th data-hide=\"phone\">Model</th>\n");
					Str.append("<th data-hide=\"phone\">Item</th>\n");
					Str.append("<th data-hide=\"phone, tablet\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						if (crs.getString("colour_active").equals("0")) {
							active = "<font color=red><b>[Inactive]</b></font>";
						} else {
							active = "";
						}
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>");
						Str.append(crs.getString("colour_name")).append("<br>" + active);
						// Str.append(" (").append(crs.getString("colour_id")).append(")");
						Str.append("</td>\n");
						Str.append("<td>").append(crs.getString("colour_code")).append("</td>\n");
						Str.append("<td>").append(crs.getString("model_name")).append("</td>\n");
						Str.append("<td><a href=../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append(">").append(crs.getString("item_name")).append("</a></td>\n");
						Str.append("<td valign=top><a href=\"managetestdrivecolour-update.jsp?update=yes&colour_id=");
						Str.append(crs.getString("colour_id")).append("\">Update Colour</a></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Colour(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
