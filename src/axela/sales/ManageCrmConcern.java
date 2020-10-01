package axela.sales;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageCrmConcern extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managecrmconcern.jsp?all=yes>List CRM Concern</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managecrmconcern-update.jsp?add=yes>Add CRM Concern...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String crmconcern_id = "0";
	public String stage_rank = "";
	public String Up = "";
	public String Down = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"CRM Concern ID", "numeric", "crmconcern_id"},
			{"CRM Type", "text", "crmtype_name IN (SELECT crmtype_name FROM axela_sales_crm_type WHERE crmtype_name"},
			{"CRM Concern Desc", "text", "crmconcern_desc"},
			{"Entry By", "text", "crmconcern_entry_id IN (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "crmconcern_entry_date"},
			{"Modified By", "text", "crmconcern_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "crmconcern_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				crmconcern_id = CNumeric(PadQuotes(request.getParameter("crmconcern_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND crmconcern_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all CRM Concern(s)!";
					StrSearch = StrSearch + " and crmconcern_id > 0";
				}

				if (!(crmconcern_id.equals("0"))) {
					msg = msg + "<br>Results for CRM Concern ID = " + crmconcern_id + "!";
					StrSearch = StrSearch + " AND crmconcern_id = " + crmconcern_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("crmconcernstrsql", StrSearch, request);
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
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			StrSql = "Select crmconcern_id, crmtype_name, crmconcern_desc";

			CountSql = "SELECT COUNT(DISTINCT crmconcern_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_crm_concern"
					+ " INNER JOIN axela_sales_crm_type ON crmtype_id = crmconcern_crmtype_id"
					+ " WHERE 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " ORDER BY crmconcern_id DESC";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>CRM Type</th>\n");
					Str.append("<th>CRM Concern Desc</th>\n");
					Str.append("<th data-hide=\"phone\" width = 20%>Actions</th>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("crmtype_name")).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("crmconcern_desc")).append("</td>\n");
						Str.append("<td valign=top><a href=\"managecrmconcern-update.jsp?update=yes&crmconcern_id=").append(crs.getString("crmconcern_id")).append(" \">Update CRM Concern</a></td>\n");
					}
					Str.append("</tr>\n");
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No CRM Concern(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}
}
