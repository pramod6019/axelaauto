package axela.app;
//@Shilpashree 05 oct 2015

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Model_Offers_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; Offer "
			+ " &gt; <a href=../app/model-offers-list.jsp?all=yes>List Offers</a>:";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=../app/model-offers-update.jsp?add=yes>Add Offer...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String msg = "";
	public String Up = "";
	public String Down = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String all = "";
	public String offers_id = "0";
	public String comp_id = "0";
	public String homefilter = "", fromdate = "", todate = "";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	// CachedowSet parameter
	Map<Integer, Object> map = new HashMap<>();
	public int mapkey = 0;
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Offers ID", "numeric", "offers_id"},
			{"Offers Name", "text", "offers_topic"},
			{"Description", "text", "offers_desc"},
			{"Active", "boolean", "offers_active"},
			{"Entry By", "text", "offers_entry_id in (SELECT emp_id from compdb.axela_emp where emp_name"},
			{"Entry Date", "date", "offers_entry_date"},
			{"Modified By", "text", "offers_modified_id in (SELECT emp_id from compdb.axela_emp where emp_name"},
			{"Modified Date", "date", "offers_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = GetSession("emp_id", request).toString();
				CheckPerm(comp_id, "emp_role_id", request, response);
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				offers_id = CNumeric(PadQuotes(request.getParameter("offers_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				homefilter = PadQuotes(request.getParameter("homefilter"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = StrSearch + " AND offers_id = 0";
				} else if (all.equals("yes")) {
					msg = "Results for all Offers!";
					StrSearch = StrSearch + " AND offers_id > 0";
				}
				if (!offers_id.equals("0")) {
					msg = msg + "<br>Result for Offer ID = " + offers_id + "!";
					StrSearch = StrSearch + " AND offers_id = ? ";
					mapkey++;
					map.put(mapkey, offers_id);
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND offers_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) // for smart search
				{
					msg = msg + "<br>Results for Search!";
					if (GetSession("offersstrsql", request) != null) {
						StrSearch = StrSearch + GetSession("offersstrsql", request);
					}
				}
				if (homefilter.equals("yes")) {
					fromdate = PadQuotes(request.getParameter("from"));
					todate = PadQuotes(request.getParameter("to"));
					fromdate = ConvertShortDateToStr(fromdate).substring(0, 8);
					todate = ConvertShortDateToStr(todate).substring(0, 8);
					StrSearch += " AND (SUBSTR(offers_entry_date, 1,8) >= '" + fromdate + "'"
							+ " AND SUBSTR(offers_entry_date, 1,8) <= '" + todate + "')";
					msg = "Results for Offers!";
				}

				SetSession("offersstrsql", StrSearch, request);
				StrHTML = ListData();
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

	public String ListData() {
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				StrSql = "SELECT offers_id, offers_offertype_id, offers_topic, offers_desc, offertype_name, offers_active ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_app_offers"
						+ " INNER JOIN " + compdb(comp_id) + " axela_app_offers_type ON offertype_id = offers_offertype_id"
						+ " WHERE 1 = 1";

				CountSql = " SELECT COUNT(DISTINCT(offers_id))";

				StrSql += SqlJoin;
				CountSql += SqlJoin;
				if (!(StrSearch.equals(""))) {
					StrSql += StrSearch
							+ " ORDER BY offers_id DESC";
				}
				CountSql += StrSearch;
				TotalRecords = Integer.parseInt(ExecutePrepQuery(CountSql, map, 0));
				if (TotalRecords != 0) {
					CachedRowSet crs = processPrepQuery(StrSql, map, 0);
					int count = 0;
					// Str.append("<div class=\"  table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover  table-responsive \" data-filter=\"#filter\">\n");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Offers</th>\n");
					Str.append("<th data-hide=\"phone\">Type</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Description</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					Str.append("</tr>\n");

					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=center nowrap>");
						Str.append(crs.getString("offers_id")).append("").append("</td>\n");
						Str.append("<td valign=top align=left>");
						Str.append(crs.getString("offers_topic"));
						if (crs.getString("offers_active").equals("0")) {
							Str.append("<br><font color=\"red\">[Inactive]</font>");
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=left>");
						Str.append(crs.getString("offertype_name")).append("").append("</td>\n");
						Str.append("<td valign=top align=left>");
						Str.append(unescapehtml(crs.getString("offers_desc"))).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../app/model-offers-update.jsp?update=yes&offers_id=").append(crs.getInt("offers_id")).append("\">Update Offer</a>");
						Str.append("</td>");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					// Str.append("</div>\n");
					crs.close();
					map.clear();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Offer(s) found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
