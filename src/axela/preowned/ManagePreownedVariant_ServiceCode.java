//  Bhagwan Singh (27th June 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManagePreownedVariant_ServiceCode extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managepreownedvariant-servicecode.jsp?all=yes>List Pre-Owned Variant Service Code</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
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
	public String all = "";
	public String servicecode_id = "0";
	public String servicecode_variant_id = "0";
	public String variant_preownedmodel_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Service Code ID", "numeric", "servicecode_id"},
			{"Service Code", "text", "servicecode_code"},
			{"Variant ID", "numeric", "servicecode_variant_id"},
			{"Variant Name", "text", "variant_name"},
			{"Model ID", "numeric", "preownedmodel_id"},
			{"Model Name", "text", "preownedmodel_name"},
			{"Manufacturer ID", "numeric", "preownedmodel_carmanuf_id"},
			{"Manufacturer", "text", "carmanuf_name"}
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
				servicecode_id = CNumeric(PadQuotes(request.getParameter("servicecode_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				LinkAddPage = "<a href=managepreownedvariant-servicecode-update.jsp?add=yes>Add Pre-Owned Variant Service Code...</a>";

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND servicecode_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for all Pre-Owned Variant Service Code(s)!";
					StrSearch = StrSearch + " AND servicecode_id > 0";
				} else if (!(servicecode_id.equals("0"))) {
					msg = msg + "<br>Results for Pre-Owned variant Service Code ID = " + servicecode_id + "!";
					StrSearch = StrSearch + " AND servicecode_id = " + servicecode_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND servicecode_id = 0";
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
			StrSql = "SELECT servicecode_id, servicecode_code, servicecode_variant_id, variant_name, preownedmodel_id, preownedmodel_name,"
					+ " COALESCE(carmanuf_name, '') AS carmanuf_name, preownedmodel_carmanuf_id";

			CountSql = "SELECT Count(DISTINCT servicecode_id)";

			SqlJoin = " FROM axela_preowned_variant_servicecode"
					+ " INNER JOIN axela_preowned_variant ON variant_id = servicecode_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1";

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
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Pre-Owned Variant Service Code(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "managepreownedvariant-servicecode.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				// display on jsp pages
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY servicecode_id DESC";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Manufacturer</th>\n");
					Str.append("<th data-hide=\"phone\">Model</th>\n");
					Str.append("<th>Variant</th>\n");
					Str.append("<th>Service Code</th>\n");
					Str.append("<th data-hide=\"phone\" width = 20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top><a href=\"managepreownedmanufacturer.jsp?carmanuf_id=").append(crs.getString("preownedmodel_carmanuf_id")).append("\">")
								.append(crs.getString("carmanuf_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left> <a href=\"managepreownedmodel.jsp?preownedmodel_id=").append(crs.getString("preownedmodel_id")).append("\">")
								.append(crs.getString("preownedmodel_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left> <a href=\"managepreownedvariant.jsp?variant_id=").append(crs.getString("servicecode_variant_id")).append("\">")
								.append(crs.getString("variant_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left >").append(crs.getString("servicecode_code")).append("</td>\n");
						Str.append("<td valign=top align=left > <a href=\"managepreownedvariant-servicecode-update.jsp?update=yes&servicecode_id=").append(crs.getString("servicecode_id"))
								.append(" \">Update Service Code</a></td>\n");
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
				Str.append("<br><br><br><br><font color=red><b>No Pre-Owned Variant Service Code(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}
}
