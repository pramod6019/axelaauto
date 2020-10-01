//Sri Venkatesh 5 apr 2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Courtesy_List extends Connect {

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../service/index.jsp>Service</a>"
			+ " &gt; <a href=courtesy.jsp>Courtesy</a>"
			+ " &gt; <a href=courtesy-list.jsp?all=yes>List Courtesy Cars</a><b>:</b>";
	public String LinkExportPage = "";
	public String LinkAddPage = "<a href=courtesy-update.jsp?add=yes>Add New Courtesy...</a>";
	public String ExportPerm = "";
	public String courtesycar_id = "";
	public String BranchAccess = "";
	public String comp_id = "0";
	public String msg = "";
	public String QueryString = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String all = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String smart = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Courtesycar ID", "numeric", "courtesycar_id"},
			{"Customer ID", "numeric", "courtesycar_customer_id"},
			{"Contact ID", "numeric", "courtesycar_contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "courtesycar_contact_name"},
			{"Contact Mobile", "text", "concat(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "concat(contact_email1, contact_email2)"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "concat(branch_name,branch_code)"},
			{"Courtesy ID", "numeric", "courtesycar_id"},
			{"Vehicle ID", "numeric", "courtesyveh_id "},
			{"Vehicle Name", "numeric", "courtesyveh_name"},
			{"Time from", "date", "courtesycar_time_from"},
			{"Time to", "date", "courtesycar_time_to"},
			{"Address", "text", "courtesycar_add"},
			{"Landmark", "text", "courtesycar_landmark"},
			{"Notes", "text", "courtesycar_notes"},
			{"Entry By", "text", "courtesycar_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "courtesycar_entry_date"},
			{"Modified By", "text", "courtesycar_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "courtesycar_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_courtesy_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "courtesyveh_branch_id");
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				courtesycar_id = CNumeric(PadQuotes(request.getParameter("courtesycar_id")));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND courtesycar_id = 0";
				} else if ("yes".equals(all)) {
					msg = msg + "<br>Results for All Courtesy Cars!";
					StrSearch = StrSearch + " and courtesycar_id > 0";
				} else if (!courtesycar_id.equals("0")) {
					msg = msg + "<br>Result for Courtesy Car ID = " + courtesycar_id + "!";
					StrSearch = StrSearch + " and courtesycar_id = " + courtesycar_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search Text!";
						StrSearch = StrSearch + " AND courtesycar_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				if (smart.equals("yes")) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("courtesycarstrsql", request).equals("")) {
						StrSearch = GetSession("courtesycarstrsql", request);
					}
				}
				StrSearch += BranchAccess;

				SetSession("courtesycarstrsql", StrSearch, request);
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
		int StartRec = 0;
		int EndRec = 0;
		int PageListSize = 10;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT courtesycar_id, courtesycar_customer_id, courtesycar_contact_id, courtesyveh_id,"
						+ " courtesycar_time_from, courtesycar_time_to, courtesycar_contact_name,"
						+ " courtesycar_mobile1, courtesycar_mobile2, contact_email1, contact_email2,"
						+ " courtesycar_add, courtesyveh_name, customer_name, courtesycar_landmark,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname";

				CountSql = "SELECT COUNT(DISTINCT courtesycar_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_courtesy_car"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle ON courtesyveh_id = courtesycar_courtesyveh_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = courtesyveh_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = courtesycar_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = courtesycar_contact_id"
						+ " WHERE 1 = 1 ";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY courtesycar_id"
							+ " ORDER BY courtesycar_id DESC";
				}
				CountSql += StrSearch;

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Courtesy Car(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "courtesy-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_service_courtesy_car\\b",
								"FROM " + compdb(comp_id) + "axela_service_courtesy_car"
										+ " INNER JOIN (SELECT courtesycar_id FROM " + compdb(comp_id) + "axela_service_courtesy_car"
										+ " WHERE 1 = 1" + StrSearch + ""
										+ " GROUP BY courtesycar_id"
										+ " ORDER BY courtesycar_id DESC"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (courtesycar_id)");
						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
								+ " ORDER BY courtesycar_id DESC";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}
					crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
					Str.append("<tr align=\"center\">\n");
					Str.append("<th>#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Customer</th>\n");
					Str.append("<th>Address</th>\n");
					Str.append("<th>Car</th>\n");
					Str.append("<th>From Time</th>\n");
					Str.append("<th>To Time</th>\n");
					Str.append("<th>Branch</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(count).append("</td>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("courtesycar_id"));
						Str.append("</td>\n<td align=\"left\" valign=\"top\" nowrap>");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("courtesycar_customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a>");
						Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("courtesycar_contact_id")).append("\">");
						Str.append(crs.getString("courtesycar_contact_name")).append("</a><br>");
						if (!crs.getString("courtesycar_mobile1").equals("")) {
							Str.append(SplitPhoneNo(crs.getString("courtesycar_mobile1"), 5, "M")).append("<br>");
						}

						if (!crs.getString("courtesycar_mobile2").equals("")) {
							Str.append(SplitPhoneNo(crs.getString("courtesycar_mobile2"), 5, "M")).append("<br>");
						}

						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a><br>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>");
						}
						Str.append("</td>\n<td align=\"left\" valign=\"top\">");
						Str.append(crs.getString("courtesycar_add"));
						if (!crs.getString("courtesycar_landmark").equals("")) {
							Str.append("<br>").append(crs.getString("courtesycar_landmark"));
						}
						Str.append("</td>\n<td valign=\"top\">\n");
						Str.append("<a href=\"../service/managecourtesyvehicle.jsp?courtesyveh_id=").append(crs.getString("courtesyveh_id")).append("\">");
						Str.append(crs.getString("courtesyveh_name")).append("</b></a>");
						Str.append("</td>\n<td valign=\"top\">\n");
						Str.append(strToLongDate(crs.getString("courtesycar_time_from")));
						Str.append("</td>\n<td valign=\"top\">\n");
						Str.append(strToLongDate(crs.getString("courtesycar_time_to")));
						Str.append("</td>\n<td valign=\"top\">\n");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append("\">");
						Str.append(crs.getString("branchname")).append("</b></a>");
						Str.append("</td>\n<td valign=\"top\" nowrap>");
						Str.append("<a href=\"courtesy-update.jsp?update=yes&courtesycar_id=").append(crs.getString("courtesycar_id")).append("\">Update Courtesy Car</a>");
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();

				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Courtesy Car(s) Found!</font><br><br>";
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
