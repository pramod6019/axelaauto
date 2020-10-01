package axela.service;
//bhagwan singh 12 March, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Call_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../service/index.jsp>Service</a>"
			+ " &gt; <a href=../service/call.jsp>Calls</a>"
			+ " &gt; <a href=call-list.jsp?all=yes>List Calls</a>:";
	public String LinkExportPage = "call-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=call-update.jsp?add=yes>Add New Call...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String group = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String call_id = "0";
	public String call_customer_id = "0";
	public String call_veh_id = "0";
	public String call_emp_id = "0";
	public String customer_id = "0";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Call ID", "numeric", "call_id"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "call_contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Vehicle ID", "numeric", "call_veh_id"},
			{"Call Type", "text", "calltype_name"},
			{"Call Time", "date", "call_time"},
			{"Reg. No.", "text", "veh_reg_no"},
			{"Entry By", "text", "call_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "call_entry_date"},
			{"Modified By", "text", "call_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "call_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "call_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "call_emp_id");
				CheckPerm(comp_id, "emp_service_call_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = unescapehtml(PadQuotes(request.getParameter("msg")));
				call_id = CNumeric(PadQuotes(request.getParameter("call_id")));
				call_veh_id = CNumeric(PadQuotes(request.getParameter("call_veh_id")));
				call_customer_id = CNumeric(PadQuotes(request.getParameter("call_customer_id")));
				call_emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND call_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br>Results for all Calls!";
					// StrSearch = StrSearch + " AND call_id > 0";
				} else if (!call_id.equals("0")) {
					msg = msg + "<br>Results for Call ID = " + call_id + "!";
					StrSearch = StrSearch + " AND call_id = " + call_id;
				} else if (!call_veh_id.equals("0")) {
					msg = msg + "<br>Results for Vehicle ID = " + call_veh_id + "!";
					StrSearch = StrSearch + " AND call_veh_id = " + call_veh_id;
				} else if (!call_emp_id.equals("0")) {
					msg = msg + "<br>Results for Executive ID = " + call_emp_id + "!";
					StrSearch = StrSearch + " AND call_emp_id = " + call_emp_id + ""
							+ " AND call_followup_time != ''"
							+ " AND call_call_id = 0"
							+ " AND call_followup_time < " + ToLongDate(kknow());
				} else if (!customer_id.equals("0")) {
					msg = msg + "<br>Results for Customer ID = " + customer_id + "!";
					StrSearch = StrSearch + " AND customer_id = " + customer_id;
				} else if (!call_customer_id.equals("0")) {
					msg = msg + "<br>Results for Customer ID = " + call_customer_id + "!";
					StrSearch = StrSearch + " AND customer_id = " + call_customer_id;
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND call_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("callstrsql", request).equals("")) {
						StrSearch = GetSession("callstrsql", request);
					}
				}
				StrSearch += BranchAccess + ExeAccess;
				SetSession("callstrsql", StrSearch, request);
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
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT calltype_name, call_id, call_branch_id, call_customer_voice, call_followup_time,"
						+ " contact_id, CONCAT(contact_fname, ' ', contact_lname) AS contact_name, call_time,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, COALESCE(item_id, 0) AS item_id,"
						+ " call_trigger, customer_name, customer_id,"
						+ " COALESCE(veh_chassis_no, '') AS veh_chassis_no, COALESCE(veh_engine_no, '') AS veh_engine_no,"
						+ " COALESCE(veh_reg_no, '') AS veh_reg_no, COALESCE(veh_id, 0) AS veh_id, title_desc,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, COALESCE(model_name, '') AS model_name,"
						+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS item_name,"
						+ " COALESCE(booking_id, 0) AS booking_id, emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') as emp_name";

				CountSql = "SELECT COUNT(DISTINCT(call_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_call"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = call_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_call_type ON calltype_id = call_type_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_booking ON booking_call_id = call_id"
						+ " WHERE 1 = 1 ";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY call_id"
							+ " ORDER BY call_id DESC";
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

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Call(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "call-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_service_call\\b",
								"FROM " + compdb(comp_id) + "axela_service_call"
										+ " INNER JOIN (SELECT call_id FROM " + compdb(comp_id) + "axela_service_call"
										+ " WHERE 1 = 1" + StrSearch + ""
										+ " GROUP BY call_id"
										+ " ORDER BY call_id DESC"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (call_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
								+ " ORDER BY call_id DESC";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}
					crs = processQuery(StrSql, 0);

					int count = StartRec - 1;

					Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">");
					Str.append("<tr align=\"center\">\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>Customer</th>\n");
					Str.append("<th>Call Type</th>\n");
					Str.append("<th data-hide=\"phone\">Customer Voice</th>\n");
					Str.append("<th data-hide=\"phone\">Call Time</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Follow-up Time</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Item</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Reg. No.</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Executive</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");

					while (crs.next()) {
						count++;
						Str.append("<tr>\n");
						Str.append("<td align=\"center\" valign=\"top\">").append(count);
						Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(crs.getString("call_id"));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a>");
						if (!crs.getString("contact_name").equals("")) {
							Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=");
							Str.append(crs.getString("contact_id")).append("\">");
							Str.append(crs.getString("title_desc")).append(" ").append(crs.getString("contact_name")).append("</a>");
						}

						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
						}

						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
						}

						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>");
						}
						Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("calltype_name"));
						Str.append("</td>\n<td align=\"left\" valign=\"top\">").append(crs.getString("call_customer_voice"));
						Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("call_time")));
						Str.append("</td>\n<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("call_followup_time")));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">");
						if (!crs.getString("item_id").equals("") && !crs.getString("item_id").equals("0")) {
							Str.append("<a href=../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append(">").append(crs.getString("item_name")).append("</a>");
						}
						Str.append("</td>\n<td align=\"left\" valign=\"top\" nowrap><a href=vehicle-list.jsp?veh_id=");
						Str.append(crs.getString("veh_id")).append(">").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a></td>\n");
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/executive-summary.jsp?emp_id=");
						Str.append(crs.getString("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>\n");
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/branch-summary.jsp?branch_id=");
						Str.append(crs.getString("call_branch_id")).append("\">").append(crs.getString("branchname")).append("</a></td>\n");
						Str.append("<td align=\"left\" valign=\"top\" nowrap>");
						Str.append("<a href=\"call-update.jsp?update=yes&call_id=").append(crs.getString("call_id")).append("\">Update Call</a>");
						if (!crs.getString("booking_id").equals("0")) {
							Str.append("<br><a href=\"booking-update.jsp?update=yes&booking_id=").append(crs.getString("booking_id"));
							Str.append("&customer_id=").append(crs.getString("customer_id")).append("\">Update Booking</a>");
						}
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} else {
					Str.append("<br><br><font color=\"red\"><b>No Call(s) found!</b></font>");
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
