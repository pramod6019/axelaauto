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

public class Inventory_ItemService_List extends Connect {
	// ///// List page links

	public String LinkHeader = "";
	public String LinkExportPage = "warehouse.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	// public String item_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
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
	public String itemservice_item_id = "0";
	public String item_name = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Item Service ID", "numeric", "itemservice_item_id"},
			{"JC Type ID", "numeric", "itemservice_jctype_id"},
			{"Kilometer", "numeric", "itemservice_kms"},
			{"Day", "numeric", "itemservice_days"}
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
				itemservice_item_id = CNumeric(PadQuotes(request.getParameter("item_id")));

				if (!itemservice_item_id.equals("0")) {
					StrSql = "select if(item_code!='',concat(item_name,' (',item_code,')'), item_name) as item_name"
							+ " from " + compdb(comp_id) + "axela_inventory_item"
							+ " where item_id=" + itemservice_item_id
							+ " order by item_name ";
					item_name = ExecuteQuery(StrSql);
				} else {
					response.sendRedirect("../inventory/inventory-item-list.jsp?all=yes");
				}
				LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../inventory/index.jsp>Inventory</a> &gt;"
						+ " <a href=../inventory/inventory-item-list.jsp?all=yes>List Items</a> &gt; <a href=../inventory/inventory-item-list.jsp?item_id=" + itemservice_item_id + ">" + item_name
						+ "</a> &gt;"
						+ " <a href=../inventory/inventory-itemservice-list.jsp?item_id=" + itemservice_item_id + ">List Service</a>:";
				LinkAddPage = "<a href=../inventory/inventory-itemservice-update.jsp?add=yes&item_id=" + itemservice_item_id + ">Add New Service..</a>";
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (!(itemservice_item_id.equals("0"))) {
					msg = msg + "<br>Results for Services of Item ID=" + itemservice_item_id + "!";
					StrSearch = StrSearch + " and itemservice_item_id =" + itemservice_item_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
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
					SetSession("itemservicestrsql", StrSearch, request);
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

			StrSql = "SELECT itemservice_id, itemservice_item_id, itemservice_jctype_id, itemservice_kms,"
					+ " itemservice_days, jctype_name";

			CountSql = "SELECT Count(distinct(itemservice_item_id))";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_inventory_item_service"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = itemservice_jctype_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!StrSearch.equals("")) {
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
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Service(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "inventory-itemservice-list.jsp?" + QueryString + "&PageCurrent=";

				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				// StrSql = StrSql +
				// " group by itemservice_item_id order by itemservice_item_id desc ";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

				CachedRowSet crs = processQuery(StrSql, 0);
				try {
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>Service ID</th>\n");
					Str.append("<th>Type</th>\n");
					Str.append("<th data-hide=\"phone\">Kilometer</th>\n");
					Str.append("<th data-hide=\"phone\">Day</th>\n");
					Str.append("<th data-hide=\"phone, tablet\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align=center valign=top>").append(count).append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("itemservice_id")).append("</td>");
						Str.append("<td valign=top align=center>").append(crs.getString("jctype_name")).append("</td>");
						Str.append("<td valign=top align=center>").append(crs.getString("itemservice_kms")).append("</td>");
						Str.append("<td valign=top align=center>").append(crs.getString("itemservice_days")).append("</td>");
						Str.append("<td valign=top align=left nowrap>");
						Str.append("<a href=\"../inventory/inventory-itemservice-update.jsp?update=yes&itemservice_id=").append(crs.getString("itemservice_id")).append("\">Update Service</a>");
						// Str.append("<br><a href=\"../service/jobcard-update.jsp?add=yes&jc_veh_id=").append(crs.getString("veh_id")).append(" \">Add Job Card</a>");
						Str.append("</td>\n");
						Str.append("</tr>");
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
				Str.append("<br><br><font color=red><b>No Service(s) found!</b></font>");
			}
		}
		return Str.toString();
	}
}
