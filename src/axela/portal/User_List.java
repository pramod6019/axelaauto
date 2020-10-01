//@Bhagwan Singh 11 feb 2013
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class User_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../app/home.jsp>App</a>"
			+ " &gt; <a href=../app/servicebooking-list.jsp?all=yes>List Service Booking</a>"
			+ " &gt; <a href=../portal/user-list.jsp?all=yes>List Users</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String all = "";
	public String StrHTML = "";
	public String smart = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";

	public String user_id = "0";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String config_sales_enquiry = "0";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"User ID", "numeric", "user_id"},
			{"User Name", "text", "CONCAT(title_desc, ' ', user_fname, ' ', user_lname)"},
			{"User Mobile", "text", "CONCAT(REPLACE(user_mobile,'-', ''))"},
			{"Email", "text", "user_email"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_contact_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
				PopulateConfigDetails();

				LinkAddPage = "";

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND user_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all  Service Users!";
					StrSearch += " and user_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Service User";
					StrSearch += " AND user_id > 0";
				}
				else if (!user_id.equals("0")) {
					StrSearch += " AND user_id = " + user_id + "";
					msg += "<br>Results for Service User for User ID = " + user_id + "!";
				}
				else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND user_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) // for smart search
				{
					msg = msg + "<br>Results of Search!";
					if (!GetSession("contactstrsql", request).equals("")) {
						StrSearch += GetSession("contactstrsql", request);
					}
				}

				StrSearch += BranchAccess;
				if (!StrSearch.equals("")) {
					SetSession("contactstrsql", StrSearch, request);
				}
				StrHTML = ListData();

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

	public String ListData() {
		CachedRowSet crs = null;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		String address = "";
		String active = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search
				StrSql = "SELECT user_id, user_active,"
						+ " COALESCE(CONCAT(title_desc, ' ', user_fname, ' ', user_lname), '') AS user_name,"
						+ " user_mobile, user_email";

				CountSql = " SELECT COUNT(DISTINCT(user_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_app_user"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = user_title_id"
						+ " WHERE 1 = 1";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;
				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY user_id"
							+ " ORDER BY user_id DESC";
					CountSql += StrSearch;
				}
				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Service User(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "user-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					if (!all.equals("recent")) {
						crs = processQuery(StrSql, 0);
					}
					int count = StartRec - 1;
					Str.append("<div class=\"  table\">\n");
					Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th>User Name</th>\n");
					Str.append("<th data-hide=\"phone\">Communication</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr></thead><tbody>\n");
					while (crs.next()) {
						count++;
						if (crs.getString("user_active").equals("0")) {
							active = "<font color=\"red\"><b>&nbsp;[Inactive]</b></font>";
						} else {
							active = "";
						}
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("user_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("user_id") + ");'");
						Str.append(" style='height:200px'>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("user_id")).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("user_name")).append(active).append("</td>\n");
						Str.append("<td valign=top align=left>");
						if (!crs.getString("user_mobile").equals("")) {
							Str.append("<span class='customer_info customer_" + crs.getString("user_id") + "' style='display: none;'>" + crs.getString("user_mobile") + "</span>")
									.append(" (M)").append("<br>");
						}
						if (!crs.getString("user_email").equals("")) {
							Str.append("<span class='customer_info customer_" + crs.getString("user_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("user_email")).append("\">");
							Str.append(crs.getString("user_email")).append("</a></span>").append("<br>");
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=left nowrap>");

						Str.append("<a href=user-update.jsp?update=yes&user_id=").append(crs.getString("user_id"));
						Str.append(">Update Service User</a>");

						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody></table>\n");
					crs.close();

				} else {
					Str.append("<br><br><br><br><b><font color=\"red\">No Contact Person(s) found!</font><b><br><br>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	protected void PopulateConfigDetails() {

		StrSql = "SELECT COALESCE(comp_email_enable,'') AS comp_email_enable,"
				+ " COALESCE(comp_sms_enable,'') AS comp_sms_enable,"
				+ " COALESCE(config_email_enable,'') AS config_email_enable,"
				+ " COALESCE(config_sms_enable,'') AS config_sms_enable"
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp on emp_id = " + emp_id;
		try {
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
