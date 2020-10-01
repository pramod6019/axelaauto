// Ved (30 Jan 2013)
package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Home extends Connect {

	public static String msg = "";
	public String StrSql = "";
	public String StrNews = "";
	public String PermStr = "";
	public String ExeAccess = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String activity_exe_id = "";
	public String branch_id = "";
	public String BranchAccess = "";
	public String BatchTimeTableStr = "";
	public String go = "";
	public String module = "";
	public String type = "";
	public String search = "";
	public String value = "";
	public String comp_module_insurance = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				SetSession("activity_emp_id", emp_id, request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				search = PadQuotes(request.getParameter("txt_search"));
				module = PadQuotes(request.getParameter("dr_module_id"));
				type = PadQuotes(request.getParameter("dr_module_type"));
				go = PadQuotes(request.getParameter("btn_go"));

				comp_module_insurance = GetSession("comp_module_insurance", request);

				StringBuilder str = new StringBuilder();
				search = str.append(search).toString();
				search = search.trim();
				if (go.equals("Go")) {
					if (search != null && search != "" && !search.isEmpty())
					{
						response.sendRedirect(response.encodeRedirectURL(SearchModule(search, module, type)));
					}

				} else {
					StrNews = ListNews();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	public String PopulateModule() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=Enquiry>Enquiry</option>\n");
			Str.append("<option value=TestDrive>Test Drive</option>\n");
			Str.append("<option value=Quote>Quote</option>\n");
			Str.append("<option value=SalesOrder>Sales Order</option>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModuleType() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=Keyword>Keyword</option>\n");
			Str.append("<option value=ID>ID</option>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SearchModule(String search, String module, String type) {
		String Str = "", url = "";
		String paramkey = "?1_dr_field=0-text&1_dr_param=0-text&1_txt_value_1=search_text&1_dr_filter=and&dr_enquirybranch=0&dr_executive=0&dr_enquirypriority=0&dr_soe=0&dr_sob=0&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1";
		String paramid = "?1_dr_field=search_dropdown&1_dr_param=0-numeric&1_txt_value_1=search_text&1_dr_filter=and&dr_enquirybranch=0&dr_executive=0&dr_enquirypriority=0&dr_soe=0&dr_sob=0&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1";
		try {
			if (module.equals("Enquiry")) {
				url = "../sales/enquiry-list.jsp";
			}

			if (module.equals("TestDrive")) {
				url = "../sales/testdrive-list.jsp";
			}

			if (module.equals("Quote")) {
				url = "../sales/veh-quote-list.jsp";
			}

			if (module.equals("SalesOrder")) {
				url = "../sales/veh-salesorder-list.jsp";
			}

			if (type.equals("ID")) {
				Str = url + paramid;
			} else if (type.equals("Keyword")) {
				Str = url + paramkey;
			}
			Str = Str.replace("search_text", search).replace("search_dropdown", "1-numeric");
			return unescapehtml(Str);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ListNews() {
		StringBuilder Str = new StringBuilder();
		String news_desc = "";
		// SOP("BranchAccess = " + BranchAccess);
		try {
			StrSql = "";
			StrSql = "SELECT news_id, news_topic, news_date, news_branch_id, news_desc FROM (";
			if (branch_id.equals("0")) {
				StrSql += "SELECT news_id, news_topic, news_date, 'ho' AS news_branch_id, news_desc"
						+ " FROM " + compdb(comp_id) + "axela_news_ho"
						+ " WHERE news_featured = '1'"
						+ " AND news_active = '1'"
						+ " UNION";
			}
			StrSql += " SELECT news_id, news_topic, news_date, news_branch_id, news_desc"
					+ " FROM " + compdb(comp_id) + "axela_news_branch"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = news_branch_id"
					+ " WHERE news_featured = '1'"
					+ " AND news_active = '1' and (news_branch_id=0 " + BranchAccess.replace("branch_id", "news_branch_id").replace("and", "or") + ")";
			StrSql += ") AS t"
					+ " where news_id is not null"
					+ " ORDER BY news_date DESC";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<ul id=\"news-container\">");
				while (crs.next()) {
					count++;
					Str.append("<li>");
					if (crs.getString("news_branch_id").equals("ho")) {
						Str.append("<a href=news-summary.jsp?honews_id=" + crs.getString("news_id") + ">");
						Str.append(crs.getString("news_topic") + " (" + strToShortDate(crs.getString("news_date")) + ")</a>");
						if (!crs.getString("news_desc").equals("")) {
							news_desc = crs.getString("news_desc");
							if (news_desc.length() > 80) {
								news_desc = news_desc.replace(news_desc.substring(80), " <a href=news-summary.jsp?honews_id=" + crs.getString("news_id") + "> Read more...</a>");
							}
							Str.append("<br>" + news_desc + "<br/>");
						}
					} else {
						Str.append("<a href=news-summary.jsp?news_id=" + crs.getString("news_id") + ">");
						Str.append(crs.getString("news_topic") + " (" + strToShortDate(crs.getString("news_date")) + ")</a>");
						if (!crs.getString("news_desc").equals("")) {
							news_desc = crs.getString("news_desc");
							if (news_desc.length() > 80) {
								news_desc = news_desc.substring(0, 80);
								news_desc = news_desc + "<a href=news-summary.jsp?news_id=" + crs.getString("news_id") + "> Read more...</a>";
							}
							Str.append("<br>" + news_desc + "<br/>");

						}
					}
					Str.append("</li>");
				}
				Str.append("</ul>");
			} else {
				Str.append("<br><br><br><br><font color=red><b>No news items found.</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOP("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
		return Str.toString();
	}
}
