package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class ManageEnquiryPriority extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=manageenquirypriority.jsp?all=yes>List Enquiry Priority</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=manageenquirypriority-update.jsp?add=yes>Add Priority...</a>";
	public String ExportPerm = "";
	public String StrHTML = "";
	public String msg = "";
	public String Up = "";
	public String Down = "";
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
	public String priorityenquiry_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Priority Enquiry ID", "numeric", "priorityenquiry_id"},
			{"Name", "text", "priorityenquiry_name"},
			{"Desc", "text", "priorityenquiry_desc"},
			{"Rank", "numeric", "priorityenquiry_rank"},
			{"DueHrs", "numeric", "priorityenquiry_duehrs"},
			{"Trigger1", "numeric", "priorityenquiry_trigger1_hrs"},
			{"Trigger2", "numeric", "priorityenquiry_trigger2_hrs"},
			{"Trigger3", "numeric", "priorityenquiry_trigger3_hrs"},
			{"Trigger4", "numeric", "priorityenquiry_trigger4_hrs"},
			{"Trigger5", "numeric", "priorityenquiry_trigger5_hrs"},
			{"Entry By", "text", "priorityenquiry_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "priorityenquiry_entry_date"},
			{"Modified By", "text", "priorityenquiry_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "priorityenquiry_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				Up = PadQuotes(request.getParameter("Up"));
				Down = PadQuotes(request.getParameter("Down"));
				priorityenquiry_id = CNumeric(PadQuotes(request.getParameter("priorityenquiry_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND priorityenquiry_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Priority !";
					StrSearch = StrSearch + " AND priorityenquiry_id > 0";
				}

				if (Up.equals("yes")) {
					moveup();
					response.sendRedirect(response.encodeRedirectURL("manageenquirypriority.jsp?msg=Priority Moved Up Successfully!"));
				}
				if (Down.equals("yes")) {
					movedown();
					response.sendRedirect(response.encodeRedirectURL("manageenquirypriority.jsp?msg=Priority Moved Down Successfully!"));
				}

				if (!(priorityenquiry_id.equals("0"))) {
					msg = msg + "<br>Results for Enquiry Priority Type ID = " + priorityenquiry_id + "!";
					StrSearch = StrSearch + " and priorityenquiry_id = " + priorityenquiry_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
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
			StrSql = "Select priorityenquiry_id, priorityenquiry_name, priorityenquiry_desc";

			CountSql = "SELECT Count(distinct priorityenquiry_id)";

			SqlJoin = " from " + compdb(comp_id) + "axela_sales_enquiry_priority"
					+ " where 1=1";

			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			if (!(StrSearch.equals(""))) {
				StrSql = StrSql + StrSearch;
				CountSql = CountSql + StrSearch;
			}
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " order by priorityenquiry_rank";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					int count = 0;
					Str.append("<div class=\"table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th>Priority Details</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Actions</th>\n");
					Str.append("<th>Order</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top>").append(crs.getString("priorityenquiry_name")).append("</td>\n");
						Str.append("<td valign=top nowrap><a href=\"manageenquirypriority-update.jsp?update=yes&priorityenquiry_id=").append(crs.getString("priorityenquiry_id"))
								.append(" \">Update Priority</a></td>\n");
						Str.append("<td valign=top align=center><a href=\"manageenquirypriority.jsp?Up=yes&priorityenquiry_id=").append(crs.getString("priorityenquiry_id"))
								.append(" \">Up</a> - <a href=\"manageenquirypriority.jsp?Down=yes&priorityenquiry_id=").append(crs.getString("priorityenquiry_id")).append(" \">Down</a></td>\n");
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
				RecCountDisplay = "<br><br><font color=red><b>No Priority Found!</b></font>";
			}
		}
		return Str.toString();
	}

	public void moveup() {
		int tempRank;
		int priorityenquiry_rank;
		try {
			priorityenquiry_rank = Integer.parseInt(ExecuteQuery("SELECT priorityenquiry_rank from " + compdb(comp_id) + "axela_sales_enquiry_priority where  priorityenquiry_id = "
					+ priorityenquiry_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select min(priorityenquiry_rank) as min1 from " + compdb(comp_id) + "axela_sales_enquiry_priority where 1=1"));
			if (priorityenquiry_rank != tempRank) {
				tempRank = priorityenquiry_rank - 1;
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_priority set priorityenquiry_rank = " + priorityenquiry_rank + " where priorityenquiry_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_priority set priorityenquiry_rank = " + tempRank + " where priorityenquiry_rank = " + priorityenquiry_rank
						+ " and priorityenquiry_id = " + priorityenquiry_id + "";
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
			priorityenquiry_rank = Integer.parseInt(ExecuteQuery("SELECT priorityenquiry_rank from " + compdb(comp_id) + "axela_sales_enquiry_priority where priorityenquiry_id = "
					+ priorityenquiry_id + ""));
			tempRank = Integer.parseInt(ExecuteQuery("Select max(priorityenquiry_rank) as max1 from " + compdb(comp_id) + "axela_sales_enquiry_priority where 1=1"));
			if (priorityenquiry_rank != tempRank) {
				tempRank = priorityenquiry_rank + 1;
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_priority set priorityenquiry_rank = " + priorityenquiry_rank + " where priorityenquiry_rank = " + tempRank + "";
				updateQuery(StrSql);
				StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry_priority set priorityenquiry_rank=" + tempRank + " where  priorityenquiry_rank = " + priorityenquiry_rank
						+ " and priorityenquiry_id = " + priorityenquiry_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
