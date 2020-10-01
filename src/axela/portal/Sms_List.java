package axela.portal;
//Murali 10th aug
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Sms_List extends Connect {

	public String LinkHeader = "<li><a href=\"../portal/home.jsp\"> Home </a> &gt; </li><li><a href=\"sms.jsp\"> SMS </a> &gt; </li><li><a href=\"sms-list.jsp\"> List SMS </a>:</li>";
	public String LinkListPage = "sms-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "<a href=sms-send.jsp?Add=yes>Add New SMS...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String smart = "";
	public String all = "";
	public String StrSql = "";
	public String PageNaviStr = "";
	public String StrSearch = "";
	public String RecCountDisplay = "";
	public String branch_id = "0";
	public String sms_id = "0";
	public String PageCurrents = "";
	public String QueryString = "";
	public String BranchAccess = "";
	public String search = "";
	public String StrHTML = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String sent = "", customer_id = "0";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {{"Keyword", "text", "keyword_arr"},
			{"SMS ID", "numeric", "sms_id"},
			{"Mobile", "text", "sms_mobileno"},
			{"Message", "text", "sms_msg"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch", "text", "branch_name"},
			{"Message", "text", "sms_msg"},
			{"Date", "date", "sms_date"},
			{"Credits", "numeric", "sms_credits"},
			{"Sent", "boolean", "sms_sent"}};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage",
						request));
				CheckPerm(comp_id, "emp_sms_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = CNumeric(PadQuotes(request
						.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				msg = PadQuotes(request.getParameter("msg"));
				sms_id = CNumeric(PadQuotes(request.getParameter("sms_id")));
				customer_id = CNumeric(PadQuotes(request
						.getParameter("customer_id")));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND sms_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all SMS!";
					StrSearch = StrSearch + " and sms_id > 0";
					StrSearch += " AND SUBSTR(sms_date,1,8) >= DATE_FORMAT(ADDDATE('" + ToLongDate(kknow()) + "',INTERVAL -10 DAY),'%Y%m%d')";
				} else if (!(sms_id.equals("0"))) {
					msg = msg + "<br>Results for SMS ID =" + sms_id + "";
					StrSearch = StrSearch + "and sms_id =" + sms_id + " ";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Smart Search!";
					StrSearch = StrSearch + GetSession("smsstrsql", request);
				}
				if (!(customer_id.equals("0"))) {
					msg = "Results for Contact ID =" + customer_id + "";
					StrSearch = StrSearch + " and customer_id =" + customer_id
							+ "";
				}
				if (!StrSearch.equals("")) {
					SetSession("smsstrsql", StrSearch, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("smsPrintSearchStr", StrSearch, request);
					SetSession("smsFilterStr", StrSearch, request);
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

	public String Listdata() {

		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		String StrJoin = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		StrSql = " SELECT "
				+ "	COALESCE(branch_name, '') AS branch_name, "
				+ " COALESCE(sms_branch_id,'') AS sms_branch_id, "
				+ " COALESCE(contact_id,'') AS contact_id, "
				+ " COALESCE(CONCAT(contact_fname,contact_lname),'') AS contact_name, "
				+ " COALESCE(CONCAT("
				+ compdb(comp_id)
				+ "axela_emp.emp_name,' (',"
				+ compdb(comp_id)
				+ "axela_emp.emp_ref_no,')'),'') AS entry_name,"
				+ " COALESCE(CONCAT(sms_emp.emp_name,' (',sms_emp.emp_ref_no,')'),'') AS emp_name,"
				+ " sms_id, sms_mobileno, sms_msg, sms_emp_id, sms_credits, sms_contact_id,  "
				+ " sms_date, sms_sent, sms_entry_id";

		CountSql = " SELECT Count(DISTINCT sms_id)";

		StrJoin = " FROM " + compdb(comp_id) + "axela_sms "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = sms_branch_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = sms_contact_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp on emp_id=sms_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS sms_emp ON sms_emp.emp_id=sms_emp_id "
				+ " WHERE 1=1 ";

		StrSql += StrJoin;
		CountSql += StrJoin;

		if (!StrSearch.equals("")) {
			StrSql += StrSearch + " GROUP BY sms_id";
			CountSql += StrSearch;
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
					+ " of " + TotalRecords + " SMS(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "sms-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			if (all.equals("yes")) {
				StrSql = StrSql + " ORDER BY sms_id DESC";
			} else {
				StrSql = StrSql + " ORDER BY sms_id DESC";
			}
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int count = StartRec - 1;
				Str.append("\n <div class=\"  table-bordered\"><table class=\"table  table-hover table-striped table-bordered\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Branch</th>\n");
				Str.append("<th data-toggle=\"true\">Executive</th>\n");
				Str.append("<th>Contact</th>\n");
				Str.append("<th data-hide=\"phone\">Mobile No</th>\n");
				Str.append("<th data-hide=\"phone\">Message</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Credits</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sent By</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td align='center'>").append(count).append("</td>");

					Str.append("<td>");
					if (!crs.getString("branch_name").equals("")) {
						Str.append("<a href=\"branch-summary.jsp?branch_id=")
								.append(crs.getString("sms_branch_id"))
								.append("\">").append(crs.getString("branch_name"))
								.append("</a>");
					}
					Str.append("</td>");

					Str.append("<td>");
					if (!crs.getString("emp_name").equals("")) {
						Str.append("<a href=\"executive-summary.jsp?emp_id=")
								.append(crs.getString("sms_emp_id"))
								.append("\">").append(crs.getString("emp_name"))
								.append("</a>");
					}
					Str.append("</td>");
					Str.append("<td>");
					if (!crs.getString("contact_name").equals("")) {
						Str.append(
								"<a href=\"../customer/customer-contact-list.jsp?contact_id=")
								.append(crs.getString("sms_contact_id"))
								.append("\">")
								.append(crs.getString("contact_name"))
								.append("</a>");
					}
					Str.append("</td>");
					Str.append("<td>");
					if (crs.getString("sms_mobileno") != null
							&& !crs.getString("sms_mobileno").equals("")) {
						Str.append(crs.getString("sms_mobileno")).append("");
					} else {
						Str.append("---");
					}
					Str.append("</td>");
					Str.append("<td>");
					if (crs.getString("sms_msg") != null
							&& !crs.getString("sms_msg").equals("")) {
						Str.append(crs.getString("sms_msg")).append("");
					} else {
						Str.append("---");
					}
					Str.append("</td>");
					Str.append("<td>")
							.append(strToLongDate(crs.getString("sms_date")))
							.append("</td>");
					Str.append("<td valign=top align=center >")
							.append(crs.getString("sms_credits"))
							.append("</td>");
					if (crs.getString("sms_sent").equals("0")) {
						sent = "Not Sent";
					} else {
						sent = "Sent";
					}
					Str.append("<td>").append(sent).append("</td>");
					Str.append("<td><a href=\"executive-summary.jsp?emp_id=")
							.append(crs.getString("sms_entry_id"))
							.append("\" target=_blank>")
							.append(crs.getString("entry_name"))
							.append("</a></td>");
					Str.append("</tr>\n");
				}
				Str.append("</tbody></table></div>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No SMS(s) found!</font><br><br>";
		}
		return Str.toString();
	}
}
