package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;
public class Manage_Enquiry_Tag_List extends Connect {

	public String LinkHeader = "<li><a href=home.jsp>Home</a> &gt;&nbsp </li><li><a href=../portal/manager.jsp>Business Manager</a> &gt;&nbsp </li><li> <a href=manageenquirytaglist.jsp>Enquiry List Tags</a><b>:</b></li>";
	public String LinkListPage = "tag-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=manageenquirytagupdate.jsp?Add=yes>Add Enquiry Tag...</a>";
	public String ExportPerm = "";
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
	public String tag_id = "0";
	public String all = "";
	public String tag_name = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Tag ID", "numeric", "tag_id"},
			{"Tag Name", "text", "tag_name"},
			{"Entry By", "text", "tag_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "tag_entry_date"},
			{"Modified By", "text", "tag_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "tag_modified_date"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				tag_id = CNumeric(PadQuotes(request.getParameter("tag_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " WHERE tag_id = 0";
				}
				if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Tags!";
					StrSearch = StrSearch + " WHERE tag_id > 0";
				}

				if (!tag_id.equals("0")) {
					msg = msg + "<br>Results for Tag ID = " + tag_id + "!";
					StrSearch = StrSearch + " WHERE tag_id = " + tag_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("tagstrsql", StrSearch, request);
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
		String StrJoin = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = "SELECT tag_id, tag_name, tag_colour";

		StrJoin = " FROM " + compdb(comp_id) + "axela_customer_tag";
		// + " INNER JOIN " + compdb(comp_id) + "axela_tag_color ON color_tag_name=tag_colour";

		CountSql = " SELECT Count(distinct tag_id)";

		StrSql = StrSql + StrJoin;
		SOP("StrSql===" + StrSql);
		CountSql = CountSql + StrJoin;
		// SOP("CountSql===" + CountSql);
		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
			CountSql = CountSql + StrSearch;
			// SOP("StrSql==list==" + StrSql);
			SOP("CountSql==list==" + CountSql);
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
					+ " of " + TotalRecords + " Tag(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "manageenquirytaglist.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
					PageListSize);
			StrSql = StrSql + " GROUP BY tag_id ORDER BY tag_id";
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage
					+ "";
			SOP("StrSql==tags===" + StrSql);
			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Tag</th>\n");
				Str.append("<th>Color</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align=\"center\">").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append(crs.getString("tag_name"));
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<a class=\"form-control\" name=\"tag_id_color\" style='background:").append(crs.getString("tag_colour")).append("'\">");
					Str.append("</a>");
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append(
							"<a href=\"manageenquirytagupdate.jsp?Update=yes&tag_id=")
							.append(crs.getString("tag_id"))
							.append(" \">Update Tag</a>");
					Str.append("</td>\n");
				}
				crs.close();
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				// SOP("Table List=====" + Str.toString());
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<center><br><br><br><br><font color=red>No Tag(s) Found!</font><br><br></center>";
		}
		return Str.toString();
	}
}
