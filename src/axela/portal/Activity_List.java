// Ved (11 Feb 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Activity_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=activity.jsp>Activities</a>"
			+ " &gt; <a href=../portal/activity-list.jsp?all=yes>List Activities</a>:";
	public String LinkExportPage = "activity-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=activity-update.jsp?add=yes>Add New Activity...</a>";
	public String ExportPerm = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String all = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String ExeAccess = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String activity_id = "";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Activity Id", "numeric", "activity_id"},
			{"Type", "text", "type_name"},
			{"Priority", "text", "priority_name"},
			{"Executive", "text", "emp_name"},
			{"Contact", "text", "IF(contact_fname != '', CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), activity_contact_person)"},
			{"Title", "text", "activity_title"},
			{"Description", "text", "activity_desc"},
			{"Start Time", "date", "activity_start_datetime"},
			{"End Time", "date", "activity_end_datetime"},
			{"Phone1", "text", "activity_Phone1"},
			{"Phone2", "text", "activity_Phone2"},
			{"Venue", "text", "activity_venue"},
			{"Status", "text", "status_desc"},
			{"Feedback", "text", "activity_feedback"},
			{"Remarks", "text", "activity_remarks"},
			{"Entry By", "numeric", "activity_entry_id"},
			{"Entry Date", "date", "activity_entry_datetime"},
			{"Modified By", "numeric", "activity_modified_id"},
			{"Modified Date", "date", "activity_modified_datetime"},
			{"Feedback Entry By", "numeric", "activity_feedback_entry_id"},
			{"Feedback Entry Date", "date", "activity_feedback_entry_datetime"},
			{"Remarks Entry By", "numeric", "activity_remarks_entry_id"},
			{"Remarks Entry Date", "date", "activity_remarks_entry_datetime"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_activity_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				ExeAccess = GetSession("ExeAccess", request);
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				activity_id = CNumeric(PadQuotes(request.getParameter("activity_id")));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());

				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND activity_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Activitie(s)!";
					StrSearch += " and activity_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Activitie(s)!";
					StrSearch += " AND activity_id > 0";
				} else if (!(activity_id.equals("0"))) {
					msg += "<br>Results for Activity ID = " + activity_id + "!";
					StrSearch += " and activity_id =" + activity_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND activity_id = 0";
					} else {
						msg = "Results for Search!";
					}

				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("activitystrsql", request).equals("")) {
						StrSearch += GetSession("activitystrsql", request);
					}
				}
				StrSearch += ExeAccess.replace("emp_id", "activity_emp_id");
				if (!StrSearch.equals("")) {
					SetSession("activitystrsql", StrSearch, request);
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
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				if (!msg.equals("Select a search parameter!")) {

					StrSql = "SELECT activity_id, activity_emp_id, activity_contact_id, activity_title,"
							+ " activity_start_datetime, activity_end_datetime, activity_contact_person,"
							+ " activity_Phone1, activity_Phone2, activity_venue, activity_remarks,"
							+ " activity_desc, activity_entry_datetime, activity_modified_datetime,"
							+ " activity_feedback_entry_id, activity_feedback_entry_datetime,"
							+ " activity_remarks_entry_id, activity_remarks_entry_datetime,"
							+ " priority_name, type_name, COALESCE(status_desc, '') AS status_desc,"
							+ " COALESCE(contact_id, 0) AS contact_id,"
							+ " COALESCE(CONCAT(title_desc, '', contact_fname, '', contact_lname), '') AS contact_name, "
							+ " activity_feedback, emp_id, CONCAT(emp_name, '(', emp_ref_no, ')') AS emp_name";

					CountSql = "SELECT COUNT(DISTINCT activity_id)";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_activity"
							+ " INNER JOIN " + compdb(comp_id) + "axela_activity_type ON " + compdb(comp_id) + "axela_activity.activity_type_id = " + compdb(comp_id) + "axela_activity_type.type_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON " + compdb(comp_id) + "axela_activity.activity_emp_id = " + compdb(comp_id) + "axela_emp.emp_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_activity_priority ON priority_id = activity_priority_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_activity_status ON status_id = activity_status_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON " + compdb(comp_id) + "axela_activity.activity_contact_id = " + compdb(comp_id)
							+ "axela_customer_contact.contact_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
							+ " WHERE 1 = 1";

					StrSql += SqlJoin;
					CountSql += SqlJoin;

					if (!StrSearch.equals("")) {
						StrSql += StrSearch + " ORDER BY activity_id DESC";
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
						// If limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Activitie(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "activity-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount = PageCount + 1;
						}
						// Display on Jsp Page

						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

						if (!all.equals("recent")) {
							crs = processQuery(StrSql, 0);
						}
						int count = StartRec - 1;
						Str.append("<div class=\"  table-responsive\">\n");
						Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
						Str.append("<thead>\n");
						Str.append("<tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th>ID</th>\n");
						Str.append("<th>Type</th>\n");
						Str.append("<th>Title</th>\n");
						Str.append("<th data-hide=\"phone\">Description</th>\n");
						Str.append("<th data-hide=\"phone\">Status</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Feedback</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Remark</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Executive</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Start Time</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">End Time</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Priority</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Contact</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						Str.append("</tr>\n");
						while (crs.next()) {
							count++;
							Str.append("<tr>\n");
							Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
							Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("activity_id"));
							Str.append("</td>\n<td valign=\"top\">").append(crs.getString("type_name")).append("</td>\n");
							Str.append("<td valign=\"top\">");
							Str.append("<a href=\"../portal/activity.jsp?d=").append(strToLongDate(crs.getString("activity_start_datetime"))).append("\">\n");
							Str.append(crs.getString("activity_title")).append("</a></td>\n");
							Str.append("<td valign=\"top\">").append(crs.getString("activity_desc")).append("</td>\n");
							Str.append("<td valign=\"top\">").append(crs.getString("status_desc")).append("</td>\n");
							Str.append("<td valign=\"top\">").append(crs.getString("activity_feedback")).append("</td>\n");
							Str.append("<td valign=\"top\">").append(crs.getString("activity_remarks")).append("</td>\n");
							Str.append("<td valign=\"top\"><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append("\">\n");
							Str.append(crs.getString("emp_name")).append("<br></a></td>\n");
							Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("activity_start_datetime"))).append("</td>\n");
							Str.append("<td valign=\"top\" align=\"center\">\n");
							Str.append(strToLongDate(crs.getString("activity_end_datetime"))).append("</td>\n");
							Str.append("<td valign=\"top\">").append(crs.getString("priority_name")).append("</td>\n");
							Str.append("<td valign=\"top\">");
							if (!crs.getString("activity_contact_id").equals("0")) {
								Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">\n");
								Str.append(crs.getString("contact_name")).append("</a><br>\n");
							} else {
								Str.append(crs.getString("activity_contact_person")).append("<br>\n");
							}
							if (!crs.getString("activity_Phone1").equals("") || !crs.getString("activity_Phone2").equals("")) {
								Str.append(crs.getString("activity_Phone1")).append("<br>\n");
								Str.append(crs.getString("activity_Phone2")).append("<br>\n");
							}
							Str.append("</td>\n");
							Str.append("<td valign=\"top\" nowrap>");
							Str.append("<a href=\"activity-update.jsp?update=yes").append("&activity_id=").append(crs.getString("activity_id")).append("\">Update Activity</a><br>\n");
							if (Long.parseLong(crs.getString("activity_end_datetime")) <= Long.parseLong(ToLongDate(kknow()))) {
								Str.append("<a href=\"activity-feedback.jsp?activity_id=").append(crs.getString("activity_id")).append("\">Update Feedback</a><br>\n");
							}
							Str.append("<a href=\"activity-remarks.jsp?activity_id=").append(crs.getString("activity_id")).append("\">Update Remarks</a><br>\n");
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();

					} else {
						RecCountDisplay = "<br><br><br><br><font color=\"red\">No Activitie(s) Found!</font><br><br>";
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
