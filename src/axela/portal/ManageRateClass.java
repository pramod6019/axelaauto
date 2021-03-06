package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageRateClass extends Connect {

	public String LinkHeader = "<li><a href=../portal/home.jsp>Home</a> &gt;&nbsp; </li><li><a href=manager.jsp>&nbsp;Business Manager</a> &nbsp;&gt;&nbsp; </li><li><a href=managerateclass.jsp?all=yes>&nbsp;List Rate Classes</a><b>:</b></li>";
	public String LinkListPage = "managerateclass.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=managerateclass-update.jsp?add=yes>Add Rate Class...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
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
	public String rateclass_id = "0";
	public String branch_id = "";
	public String rateclass_type = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {{"Keyword", "text", "keyword_arr"},
			{"Branchclass ID", "numeric", "rateclass_id "},
			{"Branchclass Name", "text", "rateclass_name"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				all = PadQuotes(request.getParameter("all"));
				msg = PadQuotes(request.getParameter("msg"));
				rateclass_id = CNumeric(PadQuotes(request
						.getParameter("rateclass_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND rateclass_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Rate Classes!";
					StrSearch = StrSearch + " and rateclass_id > 0";
				}

				if (!(rateclass_id.equals("0"))) {
					msg = msg + "<br>Results for Rate Class ID = "
							+ rateclass_id + "!";
					StrSearch = StrSearch + " and rateclass_id = "
							+ rateclass_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("branchclassstrsql", StrSearch, request);
				}
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search

		StrSql = "Select rateclass_id, rateclass_name, rateclass_type from "
				+ compdb(comp_id) + "axela_rate_class where 1 = 1";
		CountSql = "SELECT Count(distinct rateclass_id) from "
				+ compdb(comp_id) + "axela_rate_class where 1 = 1";

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

			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " Rate Class(es)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "managerateclass.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			StrSql = StrSql + " order by rateclass_id desc";
			StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage
					+ "";

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;

				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\"># </b></th>\n");
				Str.append("<th>Branch Class Details</b></th>\n");
				Str.append("<th>Branch Class Type</b></th>\n");
				Str.append("<th data-hide=\"phone\">Actions</b></th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align='center'>").append(count).append("</td>\n");
					StringBuilder append = Str.append("<td valign=top>")
							.append(crs.getString("rateclass_name"))
							.append("</td>\n");
					if (crs.getString("rateclass_type").equals("1")) {
						rateclass_type = "Sales";
					} else if (crs.getString("rateclass_type").equals("2")) {
						rateclass_type = "Purchase";
					}
					Str.append("<td>").append(rateclass_type);
					Str.append("</td>\n");
					Str.append(
							"<td><a href=\"managerateclass-update.jsp?update=yes&rateclass_id=")
							.append(crs.getString("rateclass_id"))
							.append(" \">Update Rate Class</a></td>\n");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Branch Class(s) Found!</font><br><br>";
		}
		return Str.toString();
	}
}
