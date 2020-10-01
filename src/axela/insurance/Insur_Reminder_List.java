package axela.insurance;
//Shilpashree 05 nov 2015

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Insur_Reminder_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a> &gt; <a href=../portal/manager.jsp>Business Manager</a> &gt; <a href=../insurance/insur-reminder-list.jsp?all=yes>List Insurance Reminder</a><b>:</b>";
	public String LinkListPage = "../insurance/insur-reminder-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=../insurance/insur-reminder-update.jsp?add=yes>Add Insurance Reminder...</a>";
	public String ExportPerm = "";
	public String emp_id = "0", branch_id = "0";
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
	public String insurreminder_id = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Insurance Reminder ID", "numeric", "insurreminder_id"},
			{"Days", "text", "insurreminder_days"},
			{"Email Subject", "text", "insurreminder_email_sub"},
			{"Email", "boolean", "insurreminder_email_enable"},
			{"Sms", "boolean", "insurreminder_sms_enable"},
			{"Entry By", "text", "insurreminder_entry_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Entry Date", "date", "insurreminder_entry_time"},
			{"Modified By", "text", "insurreminder_modified_id IN (SELECT emp_id FROM axela_emp WHERE emp_name"},
			{"Modified Date", "date", "insurreminder_modified_time"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = GetSession("comp_id", request);
			CheckPerm(comp_id, "emp_role_id, emp_insurance_enquiry_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = GetSession("emp_id", request);
				branch_id = GetSession("emp_branch_id", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request) + "");
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				insurreminder_id = CNumeric(PadQuotes(request.getParameter("insurreminder_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND insurreminder_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Insurance Reminder(s)!";
					StrSearch = StrSearch + " AND insurreminder_id > 0";
				} else if (!(insurreminder_id.equals("0"))) {
					msg = msg + "<br>Results for Insurance Reminder = " + insurreminder_id + "!";
					StrSearch = StrSearch + " AND insurreminder_id = " + insurreminder_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter Search Text!";
						StrSearch = StrSearch + " AND insurreminder_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (!StrSearch.equals("")) {
					session.setAttribute("insurstrsql", StrSearch);
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
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
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (!msg.equals("")) {
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			// to know no of records depending on search
			StrSql = "SELECT insurreminder_id, insurreminder_days, insurreminder_email_sub,"
					+ " insurreminder_email_format, insurreminder_sms_format,"
					+ " IF(insurreminder_email_enable = 1, 'Yes', 'No') AS 'Email',"
					+ " IF(insurreminder_sms_enable = 1, 'Yes', 'No') AS 'Sms'"
					+ " FROM " + compdb(comp_id) + "axela_insurance_reminder"
					+ " WHERE 1 = 1";
			// SOP("StrSql-----" + StrSql);
			CountSql = "SELECT COUNT(DISTINCT insurreminder_id)"
					+ " FROM " + compdb(comp_id) + "axela_insurance_reminder"
					+ " WHERE 1 = 1";

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

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Insurance Reminder(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "../insurance/insur-reminder-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql = StrSql + " ORDER BY insurreminder_id DESC";
				StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				try {
					CachedRowSet crs = processQuery(StrSql, 0);
					String active = "";
					StrHTML = "";
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("\n<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">");
					Str.append("<thead><tr>\n");
					Str.append("<tr align=center>\n");
					Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
					Str.append("<th width=5%>ID</th>\n");
					Str.append("<th>Insurance Reminder Days</th>\n");
					Str.append("<th data-hide=\"phone\" width=20%>Email</th>\n");
					Str.append("<th data-hide=\"phone, tablet\" width=20%>SMS</th>\n");
					Str.append("<th data-hide=\"phone, tablet\" width=20%>Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("insurreminder_id")).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("insurreminder_days")).append(active).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("Email")).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("Sms")).append("</td>\n");
						Str.append("<td valign=top align=left><a href=\"../insurance/insur-reminder-update.jsp?update=yes&insurreminder_id=").append(crs.getString("insurreminder_id"))
								.append(" \">Update Insurance Reminder</a><br>");
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
				} catch (Exception ex) {
					SOPError(ClientName + "===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><br><br><b><font color=red>No Insurance Reminder(s) Found!</font></b><br><br>");
			}
		}
		return Str.toString();
	}
}
