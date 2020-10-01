package axela.portal;
//Murali 10th aug

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Email_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=home.jsp>Home</a>"
			+ " &gt; <a href=email.jsp>Email</a>"
			+ " &gt; <a href=email-list.jsp>List Email</a>:";
	public String LinkListPage = "email-list.jsp";
	public String LinkExportPage = "email.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkFilterPage = "email-filter.jsp";
	public String LinkAddPage = "<a href=email-send.jsp?Add=yes>Add New Email...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
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
	public String ServiceTax = "";
	public String all = "";
	public String smart = "";
	public String email_id = "0";
	public String course_name = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Email ID", "numeric", "email_id"},
			{"From Email", "text", "email_from"},
			{"To Email", "text", "email_to"},
			{"To CC", "text", "email_cc"},
			{"To BCC", "text", "email_bcc"},
			{"Subject", "text", "email_subject"},
			{"Message", "text", "email_msg"},
			{"Attachments", "text", "email_attach"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch", "text", "branch_name"},
			{"Sent", "boolean", "email_sent"},
			{"Date", "date", "email_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_email_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				email_id = CNumeric(PadQuotes(request.getParameter("email_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND email_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Email!";
					StrSearch = StrSearch + " and email_id > 0";
					StrSearch += " AND SUBSTR(email_date,1,8) >= DATE_FORMAT(ADDDATE('" + ToLongDate(kknow()) + "',INTERVAL -10 DAY),'%Y%m%d')";
				} else if (!(email_id.equals("0"))) {
					msg = msg + "<br>Results for Email Code =" + email_id + "";
					StrSearch = StrSearch + " and email_id =" + email_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					msg = "Result for Search";
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Smart Search!";
					StrSearch = StrSearch + GetSession("emailstrsql", request);
				}
				if (!StrSearch.equals("")) {
					SetSession("emailstrsql", StrSearch, request);
				}
				if (!StrSearch.equals("")) {
					SetSession("emailPrintSearchStr", StrSearch, request);
					SetSession("emailFilterStr", StrSearch, request);
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

		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);

		// to know no of records depending on search
		StrSql = " SELECT email_id, email_from, email_to,"
				+ " email_cc, email_bcc, "
				+ " COALESCE(contact_id,'') as contact_id, "
				+ " COALESCE(CONCAT(contact_fname,''),'') as contact_fname, "
				+ " COALESCE(CONCAT(" + compdb(comp_id) + "axela_emp.emp_name,' ('," + compdb(comp_id) + "axela_emp.emp_ref_no,')'),'') as entry_name,"
				+ " COALESCE(CONCAT(email_emp.emp_name,' (',email_emp.emp_ref_no,')'),'') as emp_name,"
				+ " email_subject, email_msg, email_date, email_sent, "
				+ " email_emp_id,email_contact_id, email_entry_id ";

		CountSql = " SELECT COUNT(DISTINCT email_id) ";

		StrJoin = " FROM " + compdb(comp_id) + "axela_email"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = email_contact_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = email_branch_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = email_entry_id "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS email_emp ON email_emp.emp_id=email_emp_id "
				+ " WHERE 1=1 ";

		StrSql += StrJoin;
		CountSql += StrJoin;

		if (!StrSearch.equals("")) {
			StrSql += StrSearch + " GROUP BY email_id";
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
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Email(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
			}
			PageURL = "email-list.jsp?" + QueryString + "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page
			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			if (all.equals("yes")) {
				StrSql = StrSql + " ORDER BY email_id DESC";
			} else {
				StrSql = StrSql + "  ORDER BY email_id DESC";
			}
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
			try {
				// SOP("StrSql===list email===" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				int count = StartRec - 1;

				Str.append("\n <div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Executive</th>\n");
				Str.append("<th>Contact</th>\n");
				Str.append("<th data-hide=\"phone\">From Address</th>\n");
				Str.append("<th data-hide=\"phone\" >To Address</th>\n");
				Str.append("<th data-hide=\"phone\" >Email CC</th>\n");
				Str.append("<th data-hide=\"phone\" >Email BCC</th>\n");
				Str.append("<th data-hide=\"phone\" style=\"min-width: 240px;\">Subject</th>\n");
				Str.append("<th data-hide=\"phone\">Date</th>\n");
				Str.append("<th data-hide=\"phone\">Status</th>\n");
				Str.append("<th data-hide=\"phone\">Sent By</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td>");
					if (!crs.getString("emp_name").equals("")) {
						Str.append("<a href=\"executive-summary.jsp?emp_id=").append(crs.getString("email_emp_id")).append("\" target=_blank>").append(crs.getString("emp_name")).append("</a>");
					}
					Str.append("</td>");
					Str.append("<td>");
					if (!crs.getString("contact_fname").equals("")) {
						Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("email_contact_id")).append("\">").append(crs.getString("contact_fname"))
								.append("</a>");
					}
					Str.append("</td>");

					// FROM
					Str.append("<td>");
					if (crs.getString("email_from") != null && !crs.getString("email_from").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("email_from")).append(">").append(crs.getString("email_from")).append("</a>\n");
					} else {
						Str.append("");
					}
					Str.append("</td>");

					// TO
					Str.append("<td>");
					if (crs.getString("email_to") != null && !crs.getString("email_to").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("email_to")).append(">").append(crs.getString("email_to")).append("</a>\n");
					} else {
						Str.append("");
					}
					Str.append("</td>");

					// CC
					Str.append("<td>");
					if (crs.getString("email_cc") != null && !crs.getString("email_cc").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("email_cc")).append(">").append(crs.getString("email_cc")).append("</a>\n");
					} else {
						Str.append("");
					}
					Str.append("</td>");

					// BCC
					Str.append("<td>");
					if (crs.getString("email_bcc") != null && !crs.getString("email_bcc").equals("")) {
						Str.append("<a href=mailto:").append(crs.getString("email_bcc")).append(">").append(crs.getString("email_bcc")).append("</a>\n");
					} else {
						Str.append("");
					}
					Str.append("</td>");

					Str.append("<td >");
					if (crs.getString("email_subject") != null && !crs.getString("email_subject").equals("")) {
						Str.append("<a href=email-summary.jsp?email_id=").append(crs.getString("email_id")).append(">").append(crs.getString("email_subject")).append("</a>");
					} else {
						Str.append("---");
					}
					Str.append("</td>");
					Str.append("<td>").append(strToLongDate(crs.getString("email_date"))).append("</td>");
					Str.append("<td>");
					if (crs.getString("email_sent").equals("1")) {
						Str.append("Sent");
					} else {
						Str.append("Not Sent");
					}
					Str.append("</td>");
					Str.append("<td><a href=\"executive-summary.jsp?emp_id=").append(crs.getString("email_entry_id")).append("\" target=_blank>").append(crs.getString("entry_name"))
							.append("</a></td>");
					Str.append("</tr>\n");
				}
				Str.append("</tbody></table></div>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			RecCountDisplay = "<br><br><br><br><font color=red>No Email(s) found!</font><br><br>";
		}
		return Str.toString();
	}
}
