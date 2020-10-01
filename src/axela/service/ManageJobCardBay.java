package axela.service;

/**
 * @author Gurumurthy TS 17 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageJobCardBay extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=managejobcardbay.jsp?all=yes>List Job Card Bay</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=managejobcardbay-update.jsp?add=yes>Add Job Card Bay...</a>";
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
	public String Up = "";
	public String Down = "";
	public String QueryString = "";
	public String bay_id = "0";
	public String all = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Bay ID", "numeric", "bay_id"},
			{"Bay Branch ID", "numeric", "bay_branch_id"},
			{"Bay Name", "text", "bay_name"},
			{"Bay Open", "text", "bay_open"},
			{"Bay Active", "text", "bay_active"},
			{"Bay Notes", "text", "bay_notes"},
			{"Bay Rank", "text", "bay_rank"},
			{"Entry By", "text", "bay_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "bay_entry_date"},
			{"Modified By", "text", "bay_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "bay_modified_date"}
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
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				bay_id = CNumeric(PadQuotes(request.getParameter("bay_id")));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND bay_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Job Card Bay(s)!";
					StrSearch = StrSearch + " and bay_id > 0";
				}
				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("managejobcardbay.jsp?msg=Bay Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("managejobcardbay.jsp?msg=Bay Moved Down Successfully!"));
				}
				if (!(bay_id.equals("0"))) {
					msg = msg + "<br>Results for Job Card Bay ID = " + bay_id + "!";
					StrSearch = StrSearch + " and bay_id = " + bay_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND bay_id = 0";
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "Select bay_id, bay_branch_id, bay_name, bay_open, bay_active, "
					+ " bay_rank, coalesce(concat(branch_name, '(',branch_code, ')'),'') as branch_name ";
			CountSql = "SELECT Count(distinct bay_id)";
			SqlJoin = " from " + compdb(comp_id) + "axela_service_jc_bay"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = bay_branch_id"
					+ " where 1 = 1";

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
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Job Card Bay(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				// PageURL = "managecontractservice.jsp?" + QueryString + "&PageCurrent=";
				PageURL = "managejobcardbay.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " order by bay_rank ";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", " + recperpage + "";

				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1, j = 0;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th width=5% data-toggle=\"true\"><span class=\"footable-toggle\"></span>#</th>\n");
					Str.append("<th>Job Card Bay Details</th>\n");
					Str.append("<th>Branch</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("<th width=20%>Actions</th>\n");
					Str.append("</tr></thead><tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");
						Str.append("<td valign=top align=left >").append(crs.getString("bay_name"));
						if ((crs.getString("bay_open").equals("0")) || (crs.getString("bay_active").equals("0"))) {
							Str.append("<br><font color=red><b>[Inactive]</b></font>").append("");
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=left ><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("bay_branch_id")).append("\">")
								.append(crs.getString("branch_name")).append("</a></td>");
						Str.append("<td valign=top align=center><a href=\"managejobcardbay.jsp?Up=yes&bay_id=").append(crs.getString("bay_id"))
								.append(" \">Up</a> - <a href=\"managejobcardbay.jsp?Down=yes&bay_id=").append(crs.getString("bay_id")).append(" \">Down</a></td>\n");
						Str.append("<td valign=top align=left><a href=\"managejobcardbay-update.jsp?update=yes&bay_id=").append(crs.getString("bay_id")).append(" \">Update Job Card Bay</a></td>\n");
						Str.append("</tr>\n");
					}
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
				Str.append("<br><br><br><br><font color=red><b>No Job Card Bay(s) Found!</b></font><br><br>");
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int priorityenquiry_rank;
		try {
			priorityenquiry_rank = Integer.parseInt(ExecuteQuery("SELECT bay_rank from " + compdb(comp_id) + "axela_service_jc_bay where  bay_id = " + bay_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select min(bay_rank) as min1 from " + compdb(comp_id) + "axela_service_jc_bay where 1 = 1"));
			// SOP("priorityenquiry_rank=="+priorityenquiry_rank+" tempRank==="+tempRank);
			if (priorityenquiry_rank != tempRank) {
				tempRank = priorityenquiry_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_bay set bay_rank = " + priorityenquiry_rank + " where bay_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_bay set bay_rank = " + tempRank + " where bay_rank = " + priorityenquiry_rank + " and bay_id = " + bay_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void movedown() {
		int tempRank;
		int priorityenquiry_rank;
		try {
			priorityenquiry_rank = Integer.parseInt(ExecuteQuery("SELECT bay_rank from " + compdb(comp_id) + "axela_service_jc_bay where bay_id=" + bay_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("select max(bay_rank) as max1 from " + compdb(comp_id) + "axela_service_jc_bay where 1=1"));
			if (priorityenquiry_rank != tempRank) {
				tempRank = priorityenquiry_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_bay set bay_rank = " + priorityenquiry_rank + " where  bay_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_service_jc_bay set bay_rank = " + tempRank + " where  bay_rank = " + priorityenquiry_rank + " and bay_id = " + bay_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
