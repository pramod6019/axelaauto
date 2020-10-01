//Bhagwan Singh (27th June 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManagePreownedManufacturer extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managepreownedmanufacturer.jsp?all=yes>List Pre-Owned Manufacturer</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managepreownedmanufacturer-update.jsp?add=yes>Add Pre-Owned Manufacturer...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String PageURL = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String carmanuf_id = "0";
	public String all = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Manufacturer ID", "numeric", "carmanuf_id"},
			{"Manufacturer", "text", "carmanuf_name"},
			{"Entry By", "text", "preownedmodel_entry_id in (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "preownedmodel_entry_date"},
			{"Modified By", "text", "preownedmodel_modified_id in (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "preownedmodel_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id, emp_preowned_stock_add", request, response);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				carmanuf_id = CNumeric(PadQuotes(request.getParameter("carmanuf_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				// SOP("carmanuf_id"+carmanuf_id);

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND carmanuf_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Pre-Owned Manufacturer(s)!";
					StrSearch = StrSearch + " AND carmanuf_id > 0";
				} else if (!(carmanuf_id.equals("0"))) {
					msg = msg + "<br>Results for Pre-Owned Manufacturer ID = " + carmanuf_id + "!";
					StrSearch = StrSearch + " AND carmanuf_id = " + carmanuf_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND carmanuf_id = 0";
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
			// Check PageCurrent is valid for parse int
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "SELECT carmanuf_id, carmanuf_name,"
					+ " (SELECT COUNT(preownedmodel_id) FROM axela_preowned_model"
					+ " WHERE preownedmodel_carmanuf_id = carmanuf_id) AS varcount";

			CountSql = "SELECT COUNT(DISTINCT carmanuf_id)";

			SqlJoin = " FROM axela_preowned_manuf"
					+ " LEFT JOIN axela_preowned_model ON preownedmodel_carmanuf_id = carmanuf_id"
					+ " WHERE 1 = 1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			// SOP("Str---" + StrSql);
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Pre-Owned Model(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				// PageURL = "managecontractservice.jsp?" + QueryString +
				// "&PageCurrent=";
				PageURL = "managepreownedmanufacturer.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " GROUP BY carmanuf_id ORDER BY carmanuf_id DESC";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Manufacturer</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("carmanuf_name")).append("</td>\n");
						Str.append("<td valign=top> <a href=\"managepreownedmanufacturer-update.jsp?update=yes&carmanuf_id=").append(crs.getString("carmanuf_id"))
								.append(" \">Update Pre-Owned Manufacturer</a>\n");
						// String varcount =
						// ExecuteQuery("select count(variant_id) from axela_preowned_variant where variant_carmanuf_id = ");
						Str.append("<br><a href=\"managepreownedmodel.jsp?preownedmodel_carmanuf_id=").append(crs.getString("carmanuf_id")).append(" \">List Model</a>\n(")
								.append(crs.getString("varcount")).append(")");
						Str.append("</td>");
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
				Str.append("<br><br><br><br><font color=red><b>No Pre-Owned Model(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}
}
