// Sangita 05 APRIL 2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Pickup_List extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../service/index.jsp\">Service</a>"
			+ " &gt; <a href=\"pickup.jsp\">Pickup</a>"
			+ " &gt; <a href=\"pickup-list.jsp?all=yes\">List Pickup</a>:";
	public String LinkExportPage = "pickup-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=\"pickup-update.jsp?add=yes\">Add New Pickup...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 10;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String pickup_id = "0";
	public String pickup_veh_id = "0";
	public String smart = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Pickup ID", "numeric", "pickup_id"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "pickup_contact_name"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(pickup_mobile1, '-', ''), REPLACE(pickup_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Vehicle Reg.No.", "text", "veh_reg_no"},
			{"Driver", "text", "emp_name"},
			{"Active", "boolean", "pickup_active"},
			{"Location", "text", "location_name"},
			{"Pickup Time", "date", "pickup_time"},
			{"Entry By", "text", "pickup_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "pickup_entry_date"},
			{"Modified By", "text", "pickup_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "pickup_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "pickup_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "pickup_emp_id");
				CheckPerm(comp_id, "emp_service_pickup_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				pickup_id = CNumeric(PadQuotes(request.getParameter("pickup_id")));
				pickup_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND pickup_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Pickups!";
				} else if (!pickup_id.equals("0")) {
					msg += "Result for Pickup ID = " + pickup_id + "!";
					StrSearch += " AND pickup_id = " + pickup_id + "";
				} else if (!pickup_veh_id.equals("0")) {
					msg += "Result for Pickup Vehicle ID = " + pickup_veh_id + "!";
					StrSearch += " AND pickup_veh_id = " + pickup_veh_id + "";
				} else if (advSearch.equals("Search")) {
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND pickup_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}

				if (smart.equals("yes")) {
					msg += "<br>Results of Search!";
					if (!GetSession("pickupstrsql", request).equals("")) {
						StrSearch = GetSession("pickupstrsql", request);
					}
				}
				StrSearch += BranchAccess + ExeAccess;

				SetSession("pickupstrsql", StrSearch, request);
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
		String active = "";
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT pickup_id, customer_name, contact_id, pickup_contact_name , pickup_add,"
						+ " pickup_landmark, pickup_mobile1, pickup_mobile2, contact_email1, contact_email2,"
						+ " pickup_time_to, pickup_time_from, pickuptype_name, pickup_active,"
						+ " pickup_notes, COALESCE(veh_reg_no, '') AS regno, COALESCE(veh_id, '') AS veh_id,"
						+ " customer_id, branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
						+ " emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
						+ " pickup_time, location_id, location_name, pickuptype_id";

				CountSql = "SELECT COUNT(DISTINCT pickup_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_pickup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_location ON location_id= pickup_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_pickup_type ON pickuptype_id= pickup_pickuptype_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = pickup_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = pickup_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = pickup_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = pickup_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = pickup_veh_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY pickup_id"
							+ " ORDER BY pickup_id DESC";
				}
				CountSql += StrSearch;
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Pickup(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "pickup-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}

					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_service_pickup\\b",
								"FROM " + compdb(comp_id) + "axela_service_pickup"
										+ " INNER JOIN (SELECT pickup_id FROM " + compdb(comp_id) + "axela_service_pickup"
										+ " WHERE 1 = 1" + StrSearch + ""
										+ " GROUP BY pickup_id"
										+ " ORDER BY pickup_id DESC"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (pickup_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
								+ " ORDER BY pickup_id DESC";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}
					crs = processQuery(StrSql, 0);
					int count = StartRec - 1;

					Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
					Str.append("<tr align=\"center\">\n");
					Str.append("<th>#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Customer</th>\n");
					Str.append("<th>Address</th>\n");
					Str.append("<th>Type</th>\n");
					Str.append("<th>Time</th>\n");
					Str.append("<th>Duration</th>\n");
					Str.append("<th>Location</th>\n");
					Str.append("<th>Veh Reg.No.</th>\n");
					Str.append("<th>Driver</th>\n");
					Str.append("<th>Branch</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");
					while (crs.next()) {
						count++;
						if (crs.getString("pickup_active").equals("0")) {
							active = " <font color=\"red\"><b>[Inactive]</b></font>";
						} else {
							active = "";
						}
						Str.append("<tr>\n<td align=\"center\" valign=top>").append(count).append("</td>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("pickup_id")).append(active);
						Str.append("</td>\n<td align=\"left\" valign=\"top\" nowrap>");
						Str.append("<a href=../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(">");
						Str.append(crs.getString("customer_name")).append("</a>");
						Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
						Str.append(crs.getString("pickup_contact_name")).append("</a><br>");
						if (!crs.getString("pickup_mobile1").equals("")) {
							Str.append(SplitPhoneNo(crs.getString("pickup_mobile1"), 5, "M")).append("<br>");
						}

						if (!crs.getString("pickup_mobile2").equals("")) {
							Str.append(SplitPhoneNo(crs.getString("pickup_mobile2"), 5, "M")).append("<br>");
						}

						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a><br>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>");
						}
						Str.append("</td>\n<td align=\"left\" valign=\"top\">");
						Str.append(crs.getString("pickup_add")).append("<br>");
						if (!crs.getString("pickup_landmark").equals("")) {
							Str.append(crs.getString("pickup_landmark")).append("<br>");
						}
						Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>").append(crs.getString("pickuptype_name"));
						Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>");
						Str.append(strToLongDate(crs.getString("pickup_time")));
						Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>");
						if (!crs.getString("pickup_time_from").equals("") && !crs.getString("pickup_time_to").equals("")) {
							Str.append(PeriodTime(crs.getString("pickup_time_from"), crs.getString("pickup_time_to"), "1"));
						}
						Str.append("</td>\n<td align=\"left\" valign=\"top\">\n");
						Str.append("<a href=../service/managelocation.jsp?location_id=").append(crs.getString("location_id")).append(">");
						Str.append(crs.getString("location_name")).append("</a></td>");
						Str.append("<td align=\"left\" valign=\"top\">");
						Str.append("<a href=\"../service/vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
						Str.append(SplitRegNo(crs.getString("regno"), 2)).append("</a>");
						Str.append("</td>\n<td align=\"left\" valign=\"top\">");
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"));
						Str.append("</a></td>\n<td align=\"left\" valign=\"top\">\n");
						Str.append("<a href=../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append(">");
						Str.append(crs.getString("branchname"));
						Str.append("</a></td>\n<td valign=\"top\" align=\"left\" nowrap>");
						Str.append("<a href=\"pickup-update.jsp?update=yes&pickup_id=").append(crs.getString("pickup_id")).append("\">Update Pickup</a>");
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Pickup(s) found!</font><br><br>";
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
