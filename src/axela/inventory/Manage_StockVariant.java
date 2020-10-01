// Smitha Nag (16 March 2013)
package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Manage_StockVariant extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manage-stockvariant.jsp?all=yes>List Stock Variants</a>:";
	public String LinkExportPage = "colour-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=manage-stockvariant-update.jsp?add=yes>Add Stock Variant...</a>";
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
	public String vehstockvariant_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String all = "", active = "";
	public String smart = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			// {"Id", "numeric", "vehstockvariant_id"},
			{"Name", "text", "vehstockvariant_item_id in (SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_name"},
			{"Brand", "text", "model_brand_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Code", "text", "vehstockvariant_code"},
			{"Model Name", "text", "model_id in (SELECT model_id FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_name"},
			{"Entry By", "numeric", "vehstockvariant_entry_id in (SELECT emp_id from " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "vehstockvariant_entry_date"},
			{"Modified By", "numeric", "vehstockvariant_modified_id in (SELECT emp_id from " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "vehstockvariant_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				vehstockvariant_id = CNumeric(PadQuotes(request.getParameter("stockvariant_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND vehstockvariant_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Stock Variants!";
					StrSearch = StrSearch + " and vehstockvariant_id > 0";
				} else if (!(vehstockvariant_id.equals("0"))) {
					msg = msg + "<br>Results for Stock Variant ID = " + vehstockvariant_id + "!";
					StrSearch = StrSearch + " AND vehstockvariant_id = " + vehstockvariant_id + "";
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata() {
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

			StrSql = "SELECT vehstockvariant_id, vehstockvariant_item_id, vehstockvariant_code,"
					+ " item_name, item_code,"
					+ " model_name";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_vehstock_variant"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstockvariant_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " "
					+ " where 1=1";
			CountSql = "SELECT COUNT(distinct vehstockvariant_id)";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			// SOP("CountSql---" + CountSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Stock Variant(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "manage-stockvariant.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY vehstockvariant_id desc";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				try {
					// SOP("StrSql====" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					// Str.append("<th width=5%>ID</th>\n");
					Str.append("<th data-hide=\"phone\">Item</th>\n");
					Str.append("<th >Code</th>\n");
					Str.append("<th >Model</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td align='center'>").append(count).append("</td>\n");
						// Str.append("<td valign=top align=center>").append(crs.getString("vehstockvariant_id")).append("</td>\n");
						Str.append("<td><a href=\"inventory-item-list.jsp?item_id=");
						Str.append(crs.getString("vehstockvariant_item_id")).append("\">").append(crs.getString("item_name")).append("</a></td>\n");
						Str.append("<td>");
						Str.append(crs.getString("vehstockvariant_code"));
						Str.append("</td>\n");

						Str.append("<td>");
						Str.append(crs.getString("model_name"));
						Str.append("</td>\n");

						Str.append("<td nowrap>");
						Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
								+ "<button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"manage-stockvariant-update.jsp?update=yes&stockvariant_id="
								+ crs.getString("vehstockvariant_id") + "\">Update Stock Variant</a></li></ul></div></center></div></td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Stock Variant(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
