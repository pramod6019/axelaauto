package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Franchisee_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../portal/manager.jsp>Business Manager</a>"
			+ " &gt; <a href=../portal/franchisee-list.jsp?all=yes>List Franchisees</a><b>:</b>";
	public String LinkListPage = "franchisee-list.jsp";
	public String LinkExportPage = "franchisee.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkFilterPage = "franchisee-filter.jsp";
	public String LinkAddPage = "<a href=franchisee-update.jsp?add=yes>Add Franchisee...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
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
	public String smart = "";
	public String franchisee_id = "0";
	public String franchisee_name = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Franchisee ID", "numeric", "franchisee_id"},
			{"Name", "text", "franchisee_name"},
			{"Contact", "text", "franchisee_contact"},
			{"Phone1", "text", "franchisee_phone1"},
			{"Phone2", "text", "franchisee_phone2"},
			{"Mobile1", "text", "franchisee_mobile1"},
			{"Mobile2", "text", "franchisee_mobile2"},
			{"Email1", "text", "franchisee_email1"},
			{"Email2", "text", "franchisee_email2"},
			{"URL", "text", "franchisee_url"},
			{"Address", "text", "franchisee_add"},
			{"Pin", "text", "franchisee_pin"},
			{"Active", "boolean", "franchisee_active"},
			{"Notes", "text", "franchisee_notes"},
			{"Entry By", "text", "franchisee_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "franchisee_entry_date"},
			{"Modified By", "text", "franchisee_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "franchisee_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				franchisee_id = CNumeric(PadQuotes(request.getParameter("franchisee_id")));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND franchisee_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Franchisee(s)!";
					StrSearch = StrSearch + " and franchisee_id > 0";
				} else if (!(franchisee_id.equals("0"))) {
					msg = msg + "<br>Results for Franchisee ID = " + franchisee_id + "!";
					StrSearch = StrSearch + " and franchisee_id = " + franchisee_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Smart Search!";
					StrSearch = StrSearch + GetSession("franchiseestrsql", request);
				}
				if (!StrSearch.equals("")) {
					SetSession("franchiseestrsql", StrSearch, request);
				}

				if (!StrSearch.equals("")) {
					SetSession("franchiseePrintSearchStr", StrSearch, request);
					SetSession("franchiseeFilterStr", StrSearch, request);
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
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String StrJoin = "";

		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			if (!msg.equals("Select a Search Parameter!")) {
				StrSql = " Select franchisee_pin, franchisee_id, franchisee_name,"
						+ " franchisee_active, franchisee_city_id, franchisee_phone1,"
						+ " franchisee_phone2, franchisee_mobile1, franchisee_mobile2,"
						+ " franchisee_email1, franchisee_email2, franchisee_add, city_name";
				StrJoin = " from " + compdb(comp_id) + "axela_franchisee"
						+ " inner join " + compdb(comp_id) + "axela_franchisee_type on franchiseetype_id = franchisee_franchiseetype_id"
						+ " inner join " + compdb(comp_id) + "axela_city on city_id = franchisee_city_id"
						+ " where 1 = 1";

				CountSql = "SELECT Count(distinct franchisee_id)";
				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrJoin + StrSearch;
					CountSql = CountSql + StrJoin + StrSearch;
				}
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Franchisee(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "franchisee-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page

					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					// StrSql = StrSql + StrSearch;
					if (all.equals("yes")) {
						StrSql = StrSql + " group by franchisee_id order by franchisee_id desc";
					} else {
						StrSql = StrSql + " group by franchisee_id order by franchisee_id ";
					}
					StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

					try {
						CachedRowSet crs = processQuery(StrSql, 0);
						int count = StartRec - 1;
						String active = "";
						Str.append("<div class=\"table-responsive\">\n");
						Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
						Str.append("<th>Franchisee Details</th>\n");
						Str.append("<th>Contacts</th>\n");
						Str.append("<th data-hide=\"phone\">Address</th>\n");
						Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");

						while (crs.next()) {
							count = count + 1;
							if (crs.getString("franchisee_active").equals("0")) {
								active = "<font color=red >[Inactive]</font>";
							} else {
								active = "";
							}
							Str.append("<tr>\n");
							Str.append("<td valign=top align=center>").append(count).append("</td>\n");
							Str.append("<td valign=top>");
							Str.append("").append(crs.getString("franchisee_name")).append(active).append("");
							Str.append("</td>");
							Str.append("<td valign=top nowrap>");

							Str.append(SplitPhoneNo(crs.getString("franchisee_phone1"), 4, "T")).append("<br>");

							if (!crs.getString("franchisee_phone2").equals("") && crs.getString("franchisee_phone2") != null) {
								Str.append(SplitPhoneNo(crs.getString("franchisee_phone2"), 4, "T")).append("<br>");
							}

							Str.append(SplitPhoneNo(crs.getString("franchisee_mobile1"), 5, "M")).append("<br>");

							if (!crs.getString("franchisee_mobile2").equals("") && crs.getString("franchisee_mobile2") != null) {
								Str.append(SplitPhoneNo(crs.getString("franchisee_mobile2"), 5, "M")).append("<br>");
							}
							if (!crs.getString("franchisee_email1").equals("") && crs.getString("franchisee_email1") != null) {
								Str.append("<a href=mailto:").append(crs.getString("franchisee_email1")).append(">").append(crs.getString("franchisee_email1")).append("</a><br>");
							}
							if (!crs.getString("franchisee_email2").equals("") && crs.getString("franchisee_email2") != null) {
								Str.append("<a href=mailto:").append(crs.getString("franchisee_email2")).append(">").append(crs.getString("franchisee_email2")).append("</a>");
							}
							Str.append("</td>");
							Str.append("<td valign=top>");
							if (!crs.getString("franchisee_add").equals("")) {
								Str.append("").append(crs.getString("franchisee_add")).append(", ");
							}
							if (crs.getString("city_name") != null) {
								Str.append("").append(crs.getString("city_name")).append("");
							}
							if (!crs.getString("franchisee_pin").equals("") && crs.getString("franchisee_pin") != null) {
								Str.append(" - ").append(crs.getString("franchisee_pin")).append(", ");
							}
							if (crs.getString("city_name") != null && crs.getString("franchisee_pin").equals("")) {
								Str.append(", ");
							}
							Str.append("</td>\n");
							Str.append("<td valign=top nowrap><a href=\"franchisee-update.jsp?update=yes&franchisee_id=").append(crs.getString("franchisee_id"))
									.append(" \">Update Franchisee</a><br>" + "<a href=branch-list.jsp?franchisee_id=").append(crs.getString("franchisee_id")).append(">List Branches</a></td>");
						}
						Str.append("</tr>\n");
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
					RecCountDisplay = "<br><br><br><br><font color=red>No Franchisee(s) Found!</font><br><br>";
				}
			}
		}
		return Str.toString();
	}
}
