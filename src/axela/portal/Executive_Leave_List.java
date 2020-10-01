// Ved Prakash (12 Feb 2013), 
// edited- caching issue -4 april 2013(smitha nag)
// Modify & Formatting - Ved Prakash (28th August 2013)
package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Executive_Leave_List extends Connect {

	// public String LinkHeader = "";
	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=../portal/executives.jsp>Executives</a>"
			+ " &gt; <a href=\"../portal/executive-list.jsp?all=yes\">List Executives</a>:";
	// public String LinkListPage = "executive-list.jsp";
	public String LinkAddPage = "<a href=executives-update.jsp?add=yes>Add New Executive...</a>";
	public String LinkExportPage = "";
	public String ExportPerm = "";
	public String all = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String smart = "";
	public String PageURL = "";
	public String PageNaviStr = "";
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public String QueryString = "";
	public String emp_id = "0";
	public String leave_id = "0";
	public String comp_id = "0";
	public String emp_idsession = "0";
	public String drop_search;
	public String txt_search = "";
	public String active = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{ "Keyword", "text", "keyword_arr" },
			{ "Executive ID", "numeric", "emp_id" },
			{ "Name", "text", "emp_name" },
			{ "User Name", "text", "emp_uname" },
			{ "Role", "text", "role_name" },
			{ "Department", "text", "department_name" },
			{ "Reference No.", "text", "emp_ref_no" },
			{ "Job Title", "text", "jobtitle_desc" },
			{ "Sex", "boolean", "emp_sex" },
			{ "DOB", "date", "emp_dob" },
			{ "Marital Status", "boolean", "emp_married" },
			{ "Qualification", "text", "emp_qualification" },
			{ "Certification", "text", "emp_certification" },
			{ "Phone1", "text", "emp_phone1" },
			{ "Phone2", "text", "emp_phone2" },
			{ "Mobile1", "text", "emp_mobile1" },
			{ "Mobile2", "text", "emp_mobile2" },
			{ "Email1", "text", "emp_email1" },
			{ "Email2", "text", "emp_email2" },
			{ "Full Address", "text", "CONCAT(emp_address, ', ', emp_city,' - ', emp_pin, ', ',emp_state)" },
			{ "City", "text", "emp_city" },
			{ "Pin/Zip", "numeric", "emp_pin" },
			{ "State", "text", "emp_state" },
			{ "Landmark", "text", "emp_landmark" },
			{ "Active", "boolean", "emp_active" },
			{ "Branch ID", "numeric", "emp_branch_id" },
			{ "Executive", "boolean", "emp_all_exe" },
			{ "Sales", "boolean", "emp_sales" },
			{ "Monitoring Board", "boolean", "emp_mtrboard" },
			{ "Quote Price Update", "boolean", "emp_quote_priceupdate" },
			{ "Quote Discount Update", "boolean", "emp_quote_discountupdate" },
			{ "SO Price Update", "boolean", "emp_so_priceupdate" },
			{ "SO Discount Update", "boolean", "emp_so_discountupdate" },
			{ "Invoice Price Update", "boolean", "emp_invoice_priceupdate" },
			{ "Invoice Discount Update", "boolean", "emp_invoice_discountupdate" },
			{ "Service", "boolean", "emp_service" },
			{ "SC Price Update", "boolean", "emp_jc_priceupdate" },
			{ "SC Discount Update", "boolean", "emp_jc_discountupdate" },
			{ "Ticket Owner", "boolean", "emp_ticket_owner" },
			{ "Ticket Close", "boolean", "emp_ticket_close" },
			{ "MIS", "boolean", "emp_mis_access" },
			{ "Reports Access", "boolean", "emp_report_access" },
			{ "Export Reports", "boolean", "emp_export_access" },
			{ "Copy Access", "boolean", "emp_copy_access" },
			// {"Driver", "boolean", "emp_driver"},
			// {"Presser", "boolean", "emp_presser"},
			// {"Presser Rate", "numeric", "emp_presser_rate"},
			{ "Priority Activity Level1", "boolean", "emp_priorityactivity_level1" },
			{ "Priority Activity Level2", "boolean", "emp_priorityactivity_level2" },
			{ "Priority Activity Level3", "boolean", "emp_priorityactivity_level3" },
			{ "Priority Activity Level4", "boolean", "emp_priorityactivity_level4" },
			{ "Priority Activity Level5", "boolean", "emp_priorityactivity_level5" },
			{ "Priority Project Level1", "boolean", "emp_priorityproject_level1" },
			{ "Priority Project Level2", "boolean", "emp_priorityproject_level2" },
			{ "Priority Project Level3", "boolean", "emp_priorityproject_level3" },
			{ "Priority Project Level4", "boolean", "emp_priorityproject_level4" },
			{ "Priority Project Level5", "boolean", "emp_priorityproject_level5" },
			{ "Priority Task Level1", "boolean", "emp_prioritytask_level1" },
			{ "Priority Task Level2", "boolean", "emp_prioritytask_level2" },
			{ "Priority Task Level3", "boolean", "emp_prioritytask_level3" },
			{ "Priority Task Level4", "boolean", "emp_prioritytask_level4" },
			{ "Priority Task Level5", "boolean", "emp_prioritytask_level5" },
			{ "Priority Enquiryfollowup Level1", "boolean", "emp_priorityenquiryfollowup_level1" },
			{ "Priority Enquiryfollowup Level2", "boolean", "emp_priorityenquiryfollowup_level2" },
			{ "Priority Enquiryfollowup Level3", "boolean", "emp_priorityenquiryfollowup_level3" },
			{ "Priority Enquiryfollowup Level4", "boolean", "emp_priorityenquiryfollowup_level4" },
			{ "Priority Enquiryfollowup Level5", "boolean", "emp_priorityenquiryfollowup_level5" },
			{ "Priorityenquiry Level1", "boolean", "emp_priorityenquiry_level1" },
			{ "Priorityenquiry Level2", "boolean", "emp_priorityenquiry_level2" },
			{ "Priorityenquiry Level3", "boolean", "emp_priorityenquiry_level3" },
			{ "Priorityenquiry Level4", "boolean", "emp_priorityenquiry_level4" },
			{ "Priorityenquiry Level5", "boolean", "emp_priorityenquiry_level5" },
			{ "Priority Balance Level1", "boolean", "emp_prioritybalance_level1" },
			{ "Priority Balance Level2", "boolean", "emp_prioritybalance_level2" },
			{ "Priority Balance Level3", "boolean", "emp_prioritybalance_level3" },
			{ "Priority Balance Level4", "boolean", "emp_prioritybalance_level4" },
			{ "Priority Balance Level5", "boolean", "emp_prioritybalance_level5" },
			{ "Priority Ticket Level1", "boolean", "emp_priorityticket_level1" },
			{ "Priority Ticket Level2", "boolean", "emp_priorityticket_level2" },
			{ "Priority Ticket Level3", "boolean", "emp_priorityticket_level3" },
			{ "Priority Ticket Level4", "boolean", "emp_priorityticket_level4" },
			{ "Priority Ticket Level5", "boolean", "emp_priorityticket_level5" },
			{ "IP Access", "text", "emp_ip_access" },
			{ "Prbranch ID", "numeric", "emp_prbranch_id" },
			{ "Structure ID", "numeric", "emp_structure_id" },
			{ "Sal Calc From", "date", "emp_sal_calc_from" },
			{ "Esi No", "text", "emp_esi_no" },
			{ "Dispensary ID", "numeric", "emp_dispensary_id" },
			{ "PF NO", "numeric", "emp_pf_no" },
			{ "Pf No Dept File", "numeric", "emp_pf_no_dept_file" },
			{ "Date of Join", "date", "emp_date_of_join" },
			{ "Date of Relieve", "date", "emp_date_of_relieve" },
			{ "Reason of Leaving", "text", "emp_reason_of_leaving" },
			{ "Notes", "text", "emp_notes" },
			{ "Entry By", "text", "emp_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name" },
			{ "Entry Date", "date", "emp_entry_date" },
			{ "Modified By", "text", "emp_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name" },
			{ "Modified Date", "date", "emp_modified_date" }
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_idsession = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				msg = PadQuotes(request.getParameter("msg"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				// SOP("emp_id = " + emp_id);
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				// Date d = new Date();
				// d.getTime();
				// LinkHeader =
				// "<a href=\"../portal/home.jsp\">Home</a> &gt; <a href=executives.jsp>Executive</a> &gt; <a href=\"../portal/executive-list.jsp?target="
				// +
				// Math.random()+"&time="+d.getTime()+"&all=yes\">List Executives</a>:";
				// if (!emp_id.equals("1")) {
				// StrSearch = " AND emp_id != 1";
				// }
				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND emp_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Leaves!";
					StrSearch += " AND leave_id > 0";
				} else if (!(emp_id.equals("0"))) {
					msg += "<br>Result for Leave = " + leave_id + "!";
					StrSearch += " AND leave_id = " + leave_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND leave_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
				}
				StrHTML = Listdata();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String Img = "", address;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			// Check PageCurrent is valid for parse int
			if (PageCurrents.equals("0")) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);

			StrSql = "SELECT leave_id, leave_fromdate, leave_todate, leave_reason";

			StrJoin = " FROM " + compdb(comp_id) + "axela_emp_leave"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_emp_role ON role_id = emp_role_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_emp_department ON department_id = emp_department_id "
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_branch ON branch_id = emp_branch_id "
					+ " WHERE 1 = 1";

			CountSql = "SELECT COUNT(DISTINCT(emp_id))";

			StrSql += StrJoin + StrSearch;
			CountSql += StrJoin + StrSearch;

			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				// if limit ie. 10 > totalrecord
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}
				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Leave(s)";

				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
				}
				PageURL = "executive-leave-list.jsp?" + QueryString + "&PageCurrent=";
				PageCount = (TotalRecords / recperpage);

				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page

				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				if (all.equals("yes")) {
					StrSql += " ORDER BY leave_id DESC";
				}
				// else {
				// StrSql += " ORDER BY emp_name";
				// }
				StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
				// SOP(StrSqlBreaker(StrSql));
				try {
					CachedRowSet crs =processQuery(StrSql, 0);
					int count = StartRec - 1;
					String altcol = "";
					Str.append("<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" class=\"listtable\">\n");
					Str.append("<tr align=\"center\">\n");
					Str.append("<th>#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Leave From Date</th>\n");
					Str.append("<th>Leave To Date</th>\n");
					Str.append("<th>Leave Reason</th>\n");
					Str.append("</tr>\n");
					while (crs.next()) {
						// creating new date class to prevent storing of images
						// in browser cache
						// Date d = new Date();
						// if (crs.getString("emp_photo").equals("")) {
						// Img = "";
						// } else {
						// // Img = "<img src=../Thumbnail.do?empphoto=" +
						// // crs.getString("emp_photo") + "&width=100&time=" +
						// // d.getTime() + "&target=" +
						// // Math.random()+"&dummy=84456663 alt=" +
						// // crs.getString("emp_name") + "><br>";
						// Img = "<img src=../Thumbnail.do?empphoto=" +
						// crs.getString("emp_photo")
						// + "&width=100&time=" + d.getTime() + "&target=" +
						// Math.random()
						// + "&dummy=84456663 alt=" + crs.getString("emp_name") +
						// "><br>";
						// }
						count = count + 1;
						// if (crs.getString("emp_active").equals("0")) {
						// active = "<font color=red > [Inactive] </font>";
						// } else {
						// active = "";
						// }
						// altcol = "";
						// if (crs.getString("emp_sex").equals("1")) {
						// altcol = "dbebff";
						// } else if (crs.getString("emp_sex").equals("0")) {
						// altcol = "ffdfdf";
						// }
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=\"center\" bgcolor='").append(altcol).append("'>").append(count).append("</td>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("emp_id")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">");
						Str.append(Img).append("<b>").append(crs.getString("emp_name")).append("</b>").append(active);
						// Str.append("<br>Ref. No.: ").append(crs.getString("emp_ref_no")).append("<br>Role: ");
						// Str.append(crs.getString("role_name")).append("<br>Job Title: ").append(crs.getString("jobtitle_desc"));
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">\n");
						address = crs.getString("emp_address");
						if (!crs.getString("emp_city").equals("")) {
							address += ",<br>" + crs.getString("emp_city");
						}
						if (!crs.getString("emp_pin").equals("")) {
							address += " - " + crs.getString("emp_pin");
						}
						if (!crs.getString("emp_state").equals("")) {
							address += ",<br>" + crs.getString("emp_state") + ".";
						}
						Str.append(address).append("&nbsp;</td>\n");
						// if (!crs.getString("branch_id").equals("0")) {
						// Str.append("<td valign=\"top\" align=\"left\"><a href=../portal/branch-summary.jsp?branch_id=");
						// Str.append(crs.getString("branch_id")).append(">").append(crs.getString("branch_name"));
						// Str.append(" (").append(crs.getString("branch_code")).append(")</a></td>\n");
						// } else {
						// Str.append("<td valign=\"top\" align=\"left\">Head Office</td>\n");
						// }
						Str.append("<td valign=\"top\" align=\"left\" nowrap>");
						Str.append("<a href=../portal/executives-update.jsp?update=yes&emp_id=");
						Str.append(crs.getInt("emp_id")).append(">Leave Update</a>\n");
						// if (!crs.getString("emp_structure_id").equals("0")) {
						// Str.append("<br><a href=../payroll/executives-sal-details.jsp?emp_id=");
						// Str.append(crs.getInt("emp_id")).append(">Executive Salary Details</a>\n");
						// }
						// if (emp_idsession.equals("1")) {
						// Str.append("<br><a href=executive-access.jsp?emp_id=");
						// Str.append(crs.getString("emp_id")).append(">Access Rights</a>\n");
						// }
						// Str.append("<br><a href=\"../portal/executive-docs-list.jsp?emp_id=");
						// Str.append(crs.getString("emp_id")).append(" \">List Documents</a>\n");
						// Str.append("<br><a href=\"../portal/executives-photo.jsp?emp_id=");
						// Str.append(crs.getString("emp_id")).append("\">Update Photo</a>\n");
						// if (emp_idsession.equals("1")) {
						// Str.append("<br><a href=spawn-emp.jsp?emp_id=").append(crs.getString("emp_id"));
						// Str.append(">Sign In</a>\n");
						// }
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					crs.close();
					Str.append("</table>\n");
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Executive(s) Found!</font><br><br>";
			}
		}
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
