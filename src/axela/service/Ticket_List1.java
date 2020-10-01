package axela.service;

/*
 * @author Gurumurthy TS 11 FEB 2013
 */
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Ticket_List1 extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=index.jsp>Service</a>"
			+ " &gt; <a href=ticket.jsp>Tickets</a>"
			+ " &gt; <a href=ticket-list.jsp?all=yes>List Tickets</a>:";
	public String LinkExportPage = "ticket-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String comp_id = "0";
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
	// public String BranchAccess = "";
	public String ExeAccess = "";
	public String ticket_emp_id = "0";
	public String ticket_id = "0";
	public String ticket_customer_id = "0";
	public String customer_id = "0";
	public String contact_id = "0";
	public String previous_ticket_id = "0";
	public String advhtml = "";
	public String config_service_ticket_cat = "";
	public String config_service_ticket_type = "";
	public Smart SmartSearch = new Smart();
	public String advSearch = "";
	public String executive_id = "";
	public String ticket_dept_id = "";
	public String priority_id = "";
	public String category_id = "";
	public String tickettype_id = "";
	public String project_id = "";
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";
	public String[] ticketstatus_ids;
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Ticket ID", "numeric", "ticket_id"},
			{"Source", "text", "ticket_ticketsource_id in (select ticketsource_id from " + compdb(comp_id) + "axela_service_ticket_source where ticketsource_name"},
			{"Status", "text", "ticketstatus_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "concat(title_desc,' ',contact_fname,' ',contact_lname)"},
			{"Contact Mobile", "text", "concat(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "concat(contact_email1, contact_email2)"},
			{"Vehicle ID", "numeric", "veh_id"},
			{"Reg. No.", "text", "veh_reg_no"},
			{"Chassis Number", "text", "veh_chassis_no"},
			{"Item Name", "text", "item_name"},
			{"Department", "text", "ticket_dept_name"},
			{"Category", "text", "ticket_ticketcat_id in (select ticketcat_id from " + compdb(comp_id) + "axela_service_ticket_cat where ticketcat_name"},
			{"Type", "text", "ticket_tickettype_id in (select tickettype_id from " + compdb(comp_id) + "axela_service_ticket_type where tickettype_name"},
			{"Priority", "text", "priorityticket_name"},
			{"Executive", "text", "emp_name"},
			{"Subject", "text", "ticket_subject"},
			{"Description", "text", "ticket_desc"},
			{"Notes", "text", "ticket_notes"},
			{"Entry By", "text", "ticket_entry_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Entry Date", "date", "ticket_entry_date"},
			{"Modified By", "text", "ticket_modified_id in (select emp_id from " + compdb(comp_id) + "axela_emp where emp_name"},
			{"Modified Date", "date", "ticket_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				// BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// ExeAccess = ExeAccess.replace("emp_id", "ticket_emp_id");
				CheckPerm(comp_id, "emp_ticket_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				ticket_customer_id = CNumeric(PadQuotes(request.getParameter("ticket_customer_id")));
				previous_ticket_id = CNumeric(PadQuotes(request.getParameter("previous_ticket_id")));
				ticket_emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				orderby = PadQuotes(request.getParameter("orderby"));
				ordertype = PadQuotes(request.getParameter("ordertype"));
				// String ticket_due_time = DueTime("20130408154500", 10.00,
				// 18.00, 24, "1", "0", "0", "0", "0", "0", "0");
				// SOP("ticket_due_time--" + ticket_due_time);

				PopulateConfigDetails();
				advhtml = BuildAdvHtml(request, response);

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND ticket_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Tickets!";
					// StrSearch = StrSearch +
					// " and ticket_ticketstatus_id!=3 ";
				} else if (!(ticket_id.equals("0"))) {
					msg = msg + "<br>Results for Ticket ID =" + ticket_id + "!";
					StrSearch = StrSearch + " and ticket_id =" + ticket_id + "";
				} else if (!(previous_ticket_id.equals("0"))) {
					msg = msg + "<br>Results for Related Ticket ID =" + previous_ticket_id + "!";
					StrSearch = StrSearch + " and if((select ticket_customer_id from " + compdb(comp_id) + "axela_service_ticket where ticket_id = " + previous_ticket_id + ")!=0, "
							+ " ticket_customer_id in (select ticket_customer_id from " + compdb(comp_id) + "axela_service_ticket where ticket_id = " + previous_ticket_id + "), "
							+ " ticket_emp_id in (select ticket_emp_id from " + compdb(comp_id) + "axela_service_ticket where ticket_id = " + previous_ticket_id + "))";
				} else if (!(contact_id.equals("0"))) {
					msg = msg + "<br>Results for Contact ID =" + contact_id + "!";
					StrSearch = StrSearch + " and ticket_contact_id =" + contact_id + "";
				} else if (!(customer_id.equals("0"))) {
					msg = msg + "<br>Results for Customer ID =" + customer_id + "!";
					StrSearch = StrSearch + " and ticket_customer_id =" + customer_id + "";
				} else if (!(ticket_emp_id.equals("0"))) {
					msg = msg + "<br>Results for Executive ID =" + ticket_emp_id + "!";
					StrSearch = StrSearch + " and ticket_emp_id =" + ticket_emp_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("ticketstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("ticketstrsql", request);
					}
				} else if (!ticket_customer_id.equals("0")) {
					msg = "Results for Customer ID = " + ticket_customer_id + "!";
					StrSearch = StrSearch + " and customer_id = " + ticket_customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = StrSearch + SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND ticket_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}

				// if (!StrSearch.equals("")) {
				SetSession("ticketstrsql", StrSearch, request);
				// }

				StrHTML = Listdata();
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

	public String Listdata() {
		Connection conn1 = null;
		ResultSet rs = null;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		String ticketstatus_id = "";
		long closetime = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				// to know no of records depending on search
				StrSql = "Select ticket_id, ticket_subject, ticket_desc, ticket_customer_id, ticket_contact_id,"
						+ " coalesce(customer_id, 0) as customer_id, coalesce(customer_name, '') as customer_name,"
						+ " coalesce(ticketcat_name, '') as ticketcat_name,"
						+ " coalesce(tickettype_name, '') as tickettype_name,"
						// + ", ticketsource_name, "
						+ " priorityticket_name, priorityticket_id,"
						+ " ticket_trigger, coalesce(concat(title_desc, ' ', contact_fname, ' ', contact_lname), '') as contact_name, ticketstatus_id, ticketstatus_name,"
						+ " coalesce(contact_id,0) as contact_id, coalesce(contact_mobile1,'') as contact_mobile1,"
						+ " coalesce(contact_mobile2,'') as contact_mobile2, coalesce(contact_email1, '') as contact_email1, coalesce(contact_email2, '') as contact_email2, "
						+ " emp_id, CONCAT(emp_name, ' (',emp_ref_no, ')') as emp_name, ticket_dept_name,"
						+ " ticket_report_time, ticket_due_time, ticket_closed_time,"
						+ " coalesce(veh_id, 0) as veh_id, coalesce(veh_reg_no, '') as veh_reg_no, coalesce(veh_chassis_no,'') veh_chassis_no, "
						+ " coalesce(item_name, '') as item_name,"
						+ " coalesce(jc_id, 0) as jc_id";

				CountSql = " SELECT Count(distinct(ticket_id)) ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = ticket_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_source ON ticketsource_id = ticket_ticketsource_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id "
						// + BranchAccess + ""
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " WHERE 1 = 1 " + "  and ticket_id = ? ";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				QueryString = QueryString.replaceAll("&orderby=" + orderby, "");
				QueryString = QueryString.replaceAll("&ordertype=" + ordertype, "");
				ordernavi = "ticket-list.jsp?" + QueryString;
				if (ordertype.equals("asc")) {
					ordertype = "desc";
				} else {
					ordertype = "asc";
				}

				conn1 = connectDB();
				PreparedStatement pstmt = conn1.prepareStatement(StrSql);
				// SOP("pstmt = " + pstmt);
				pstmt.setString(1, ticket_id);
				// pstmt.setString(2, ticket_emp_id);
				// SOP("StrSql = " + StrSqlBreaker(StrSql));
				rs = pstmt.executeQuery();

				// StrSql += ExeAccess
				// + " and ticket_id =?"
				// + "  and ticket_emp_id =?"
				// + " GROUP BY ticket_id";
				// if (orderby.equals("")) {
				// StrSql = StrSql + " order by ticket_id desc ";
				// } else {
				// StrSql = StrSql + " order by " + orderby + " " + ordertype +
				// " ";
				// }
				// if (!(StrSearch.equals(""))) {
				// StrSql += ExeAccess + StrSearch + " GROUP BY ticket_id";
				// if (orderby.equals("")) {
				// StrSql = StrSql + " order by ticket_id desc ";
				// } else {
				// StrSql = StrSql + " order by " + orderby + " " + ordertype +
				// " ";
				// }
				// }
				if (all.equals("yes")) {
					CountSql += " and ticket_ticketstatus_id!=3";
				}
				CountSql += ExeAccess + StrSearch;

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Ticket(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "ticket-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page

					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					// if (all.equals("yes")) {
					// StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) +
					// "axela_service_ticket\\b",
					// "FROM " + compdb(comp_id) + "axela_service_ticket"
					// + " INNER JOIN (SELECT ticket_id FROM " + compdb(comp_id)
					// + "axela_service_ticket"
					// + " WHERE 1 = 1"
					// + " and ticket_ticketstatus_id!=3" +
					// ExeAccess.replace("emp_id", "ticket_emp_id")
					// + " GROUP BY ticket_id"
					// + " ORDER BY ticket_id DESC"
					// + " LIMIT " + (StartRec - 1) + ", " + recperpage +
					// ") AS myresults USING (ticket_id)");
					//
					// StrSql = "SELECT * FROM (" + StrSql + ") AS datatable";
					//
					// if (orderby.equals("")) {
					// StrSql += " ORDER BY ticket_id DESC";
					// } else {
					// StrSql += " ORDER BY " + orderby + " " + ordertype + " ";
					// }
					// } else {
					// StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage
					// + "";
					// }
					// SOP("CountSql = " + StrSqlBreaker(CountSql));
					// SOP("StrSql = " + StrSqlBreaker(StrSql));

					int count = StartRec - 1;

					Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
					Str.append("<tr align=center>\n");
					Str.append("<th width=3%>#</th>\n");
					Str.append("<th>").append(GridLink("ID", "ticket_id", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Ticket", "ticket_subject", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Customer", "customer_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Vehicle", "veh_id", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Report Time", "ticket_report_time", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Due Time", "ticket_due_time", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Status", "ticketstatus_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Priority", "priorityticket_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Level", "ticket_trigger", ordernavi, ordertype)).append("</th>\n");
					if (config_service_ticket_cat.equals("1")) {
						Str.append("<th>").append(GridLink("Category", "ticketcat_name", ordernavi, ordertype)).append("</th>\n");
					}
					if (config_service_ticket_type.equals("1")) {
						Str.append("<th>").append(GridLink("Type", "tickettype_name", ordernavi, ordertype)).append("</th>\n");
					}
					Str.append("<th>").append(GridLink("Department", "ticket_dept_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Executive", "emp_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");
					while (rs.next()) {
						count = count + 1;
						ticketstatus_id = rs.getString("ticketstatus_id");
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=center>").append(rs.getString("ticket_id")).append("</td>\n");
						Str.append("<td valign=top align=left>");
						Str.append("<a href=\"javascript:remote=window.open('ticket-dash.jsp?ticket_id=").append(rs.getString("ticket_id")).append("','ticketdash','');remote.focus();\">");
						// Str.append("<a href=\"ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("\">");
						Str.append(rs.getString("ticket_subject")).append("</a></td>");
						Str.append("<td valign=top align=left nowrap>");
						if (!rs.getString("contact_id").equals("0")) {
							Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(rs.getString("customer_id")).append(" \">");
							Str.append("").append(rs.getString("customer_name")).append("</a><br>");
							Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(rs.getString("ticket_contact_id")).append(" \">");
							Str.append("").append(rs.getString("contact_name")).append("</a>");
							if (!rs.getString("contact_mobile1").equals("")) {
								Str.append("<br>").append(SplitPhoneNo(rs.getString("contact_mobile1"), 5, "M"));
							}
							if (!rs.getString("contact_mobile2").equals("")) {
								Str.append("<br>").append(SplitPhoneNo(rs.getString("contact_mobile2"), 5, "M"));
							}
							if (!rs.getString("contact_email1").equals("")) {
								Str.append("<br><a href=mailto:").append(rs.getString("contact_email1")).append(">").append(rs.getString("contact_email1")).append("</a>");
							}
							if (!rs.getString("contact_email2").equals("")) {
								Str.append("<br><a href=mailto:").append(rs.getString("contact_email2")).append(">").append(rs.getString("contact_email2")).append("</a>");
							}
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left nowrap>");
						if (!rs.getString("item_name").equals("")) {
							Str.append(rs.getString("item_name"));
							if (!rs.getString("veh_reg_no").equals("")) {
								Str.append("<br>").append(SplitRegNo(rs.getString("veh_reg_no"), 4));
							} else {
								Str.append("<br>").append(rs.getString("veh_chassis_no"));
							}
							Str.append("<br><a href=vehicle-list.jsp?veh_id=").append(rs.getString("veh_id")).append(">").append("Vehicle ID: ").append(rs.getString("veh_id")).append("</a>");
						}
						if (!rs.getString("jc_id").equals("0")) {
							Str.append("<br><a href=jobcard-list.jsp?jc_id=").append(rs.getString("jc_id")).append(">").append("JC ID: ").append(rs.getString("jc_id")).append("</a>");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=center nowrap>").append(strToLongDate(rs.getString("ticket_report_time"))).append("</td>\n");
						Str.append("<td valign=top align=center nowrap>");
						if (!rs.getString("ticket_closed_time").equals("")) {
							closetime = rs.getLong("ticket_closed_time");
						} else {
							closetime = Long.parseLong(ToLongDate(kknow()));
						}
						if (!rs.getString("ticket_due_time").equals("")) {
							if (closetime >= Long.parseLong(rs.getString("ticket_due_time"))) {
								Str.append("<font color=#ff0000>").append(strToLongDate(rs.getString("ticket_due_time"))).append("</font>");
							} else {
								Str.append("<font color=blue>").append(strToLongDate(rs.getString("ticket_due_time"))).append("</font>");
							}
						}
						Str.append("</td>\n");
						if (ticketstatus_id.equals("3")) {
							Str.append("<td valign=top align=left><font color=\"red\">").append(rs.getString("ticketstatus_name")).append("</font></td>\n");
						} else {
							Str.append("<td valign=top align=left>").append(rs.getString("ticketstatus_name")).append("</td>\n");
						}
						Str.append("<td valign=top align=left nowrap>").append(rs.getString("priorityticket_name")).append("</td>\n");
						Str.append("<td valign=top align=center nowrap>").append(rs.getString("ticket_trigger")).append("</td>\n");
						if (config_service_ticket_cat.equals("1")) {
							Str.append("<td valign=top align=left nowrap>").append(rs.getString("ticketcat_name")).append("</td>\n");
						}
						if (config_service_ticket_type.equals("1")) {
							Str.append("<td valign=top align=left nowrap>").append(rs.getString("tickettype_name")).append("</td>\n");
						}
						Str.append("<td valign=top align=left nowrap>").append(rs.getString("ticket_dept_name")).append("</td>\n");
						Str.append("<td valign=top align=left nowrap>");
						Str.append(" <a href=\"../portal/executive-summary.jsp?emp_id=").append(rs.getInt("emp_id")).append("\">").append(rs.getString("emp_name")).append("</a></td>");
						Str.append("<td valign=top align=left nowrap>");
						Str.append("<a href=\"ticket-update.jsp?update=yes&ticket_id=").append(rs.getInt("ticket_id")).append(" \">Update Ticket</a><br>"
								+ "<a href=\"ticket-summary-print.jsp?ticket_id=").append(rs.getInt("ticket_id")).append(" \">Print Ticket</a><br></td>");
						Str.append("</tr>\n");
						Str.append("</tr>\n");
					}
					Str.append("</table>\n");
					rs.close();
					conn1.close();

				} else {
					Str.append("<br><br><br><br><font color=red><b>No Ticket(s) found!</b></font><br><br>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}

		}
		return Str.toString();
	}

	public String BuildAdvHtml(HttpServletRequest request, HttpServletResponse response) {
		executive_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		ticket_dept_id = CNumeric(PadQuotes(request.getParameter("dr_ticket_dept")));
		priority_id = CNumeric(PadQuotes(request.getParameter("dr_priority")));
		category_id = CNumeric(PadQuotes(request.getParameter("dr_category")));
		tickettype_id = CNumeric(PadQuotes(request.getParameter("dr_tickettype_id")));
		if (request.getParameterValues("chk_ticket_status") != null) {
			ticketstatus_ids = request.getParameterValues("chk_ticket_status");
		}
		if (!executive_id.equals("0")) {
			StrSearch = StrSearch + " and ticket_emp_id = " + executive_id;
		}
		if (!ticket_dept_id.equals("0")) {
			StrSearch = StrSearch + " and ticket_ticket_dept_id = " + ticket_dept_id;
		}
		if (!priority_id.equals("0")) {
			StrSearch = StrSearch + " and ticket_priorityticket_id = " + priority_id;
		}
		if (!category_id.equals("0")) {
			StrSearch = StrSearch + " and ticket_ticketcat_id = " + category_id;
		}
		if (!tickettype_id.equals("0")) {
			StrSearch = StrSearch + " and ticket_tickettype_id = " + tickettype_id;
		}
		if (ticketstatus_ids != null) {
			StrSearch = StrSearch + " and ticket_ticketstatus_id in (" + RetrunSelectArrVal(request, "chk_ticket_status") + ")";
		}
		StringBuilder Str = new StringBuilder();
		Str.append("<table width=100% align=center cellspacing=0 cellpadding=0 border=0>\n");
		Str.append("<tr align=center>\n");
		Str.append("<td nowrap>");
		Str.append("Executive: <select name=dr_executive class=selectbox id=dr_executive>");
		Str.append(PopulateExecutive());
		Str.append("</select>");
		Str.append("&nbsp;&nbsp;Department: <select name=dr_ticket_dept class=selectbox id=dr_ticket_dept>");
		Str.append(PopulateDepartment());
		Str.append("</select>");
		Str.append("&nbsp;&nbsp;Priority: <select name=dr_priority class=selectbox id=dr_priority>");
		Str.append(PopulatePriority());
		Str.append("</select>");
		if (config_service_ticket_cat.equals("1")) {
			Str.append("&nbsp;&nbsp;Category: <select name=dr_category class=selectbox id=dr_category>");
			Str.append(PopulateCategory());
			Str.append("</select>");
		}
		if (config_service_ticket_type.equals("1")) {
			Str.append("&nbsp;&nbsp;Type: <select name=dr_tickettype_id class=selectbox id=dr_tickettype_id>");
			Str.append(PopulateType());
			Str.append("</select>");
		}
		// Str.append("&nbsp;&nbsp;Project: <select name=dr_project class=selectbox id=dr_project>");
		// Str.append(PopulateProject());
		// Str.append("</select>");
		Str.append("</td>\n");
		Str.append("</tr>");
		Str.append("<tr>");
		Str.append("<td align=center>");
		Str.append(PopulateStatus());
		Str.append("</td>\n");
		Str.append("</tr>");
		Str.append("</table>");
		return Str.toString();
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		try {
			StrSql = "select emp_name, emp_ref_no, " + compdb(comp_id) + "axela_emp.emp_id as emp_id, count(ticket_id) as ticketcount "
					+ " from " + compdb(comp_id) + "axela_emp "
					+ " inner join " + compdb(comp_id) + "axela_service_ticket on ticket_emp_id = " + compdb(comp_id) + "axela_emp.emp_id "
					+ " where emp_active = '1' " + ExeAccess
					+ " group by emp_id "
					+ " order by emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql-all-" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), executive_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDepartment() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		try {
			StrSql = "select ticket_dept_id, ticket_dept_name "
					+ " from " + compdb(comp_id) + "axela_service_ticket_dept"
					+ " inner join " + compdb(comp_id) + "axela_service_ticket on ticket_ticket_dept_id = ticket_dept_id "
					+ " where 1=1 " + ExeAccess.replace("emp_id", "ticket_emp_id")
					+ " group by ticket_dept_id"
					+ " order by ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), ticket_dept_id));
				Str.append(">").append(crs.getString("ticket_dept_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "select priorityticket_id, priorityticket_name "
					+ " from " + compdb(comp_id) + "axela_service_ticket_priority "
					+ " inner join " + compdb(comp_id) + "axela_service_ticket on ticket_priorityticket_id = priorityticket_id "
					+ " where 1=1 " + ExeAccess.replace("emp_id", "ticket_emp_id")
					+ " group by priorityticket_id"
					+ " order by priorityticket_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityticket_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityticket_id"), priority_id));
				Str.append(">").append(crs.getString("priorityticket_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCategory() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "select ticketcat_id, ticketcat_name "
					+ " from " + compdb(comp_id) + "axela_service_ticket_cat "
					+ " inner join " + compdb(comp_id) + "axela_service_ticket on ticket_ticketcat_id = ticketcat_id "
					+ " where 1=1 " + ExeAccess.replace("emp_id", "ticket_emp_id")
					+ " group by ticketcat_id "
					+ " order by ticketcat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketcat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketcat_id"), category_id));
				Str.append(">").append(crs.getString("ticketcat_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "select tickettype_id, tickettype_name "
					+ " from " + compdb(comp_id) + "axela_service_ticket_type "
					+ " inner join " + compdb(comp_id) + "axela_service_ticket on ticket_tickettype_id = tickettype_id "
					+ " where 1=1 " + ExeAccess.replace("emp_id", "ticket_emp_id")
					+ " group by tickettype_id "
					+ " order by tickettype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tickettype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("tickettype_id"), tickettype_id));
				Str.append(">").append(crs.getString("tickettype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		String ids = "";
		StrSql = "Select ticketstatus_id, ticketstatus_name "
				+ " from " + compdb(comp_id) + "axela_service_ticket_status "
				+ " order by ticketstatus_rank";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (all.equals("yes")) {
				while (crs.next()) {
					if (crs.getInt("ticketstatus_id") != 3) {
						ids = ids + " " + crs.getInt("ticketstatus_id");
					}
				}
				ticketstatus_ids = ids.trim().split(" ");
			}
			crs.beforeFirst();
			while (crs.next()) {
				Str.append("<input type=checkbox name=chk_ticket_status value=").append(crs.getString("ticketstatus_id")).append(" ");
				Str.append(ArrSelectCheck(crs.getInt("ticketstatus_id"), ticketstatus_ids)).append("");
				Str.append(">").append(crs.getString("ticketstatus_name")).append("&nbsp;");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {

		StrSql = " select config_service_ticket_cat, config_service_ticket_type "
				+ " from " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
				+ " where 1 = 1";
		// SOP("StrSql---"+StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
