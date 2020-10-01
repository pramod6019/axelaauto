package axela.service;

/*
 * @author Gurumurthy TS 11 FEB 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accessories.MIS_Check;
import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Ticket_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=index.jsp>Service</a>"
			+ " &gt; <a href=ticket.jsp>Tickets</a>"
			+ " &gt; <a href=ticket-list.jsp?all=yes>List Tickets</a><b>:</b><br />";
	public String LinkExportPage = "ticket-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkPrintPage = "ticket-print.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=\"ticket-add.jsp?add=yes\">Add New Ticket ...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String all = "";
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
	public String ExeAccess = "";
	public String ticket_emp_id = "0";
	public String emp_id = "0";
	public String ticket_id = "0";
	public String emp_all_exe = "";
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
	public String[] brand_ids, region_ids, branch_ids, ticketjctype_ids;
	public String brand_id = "", region_id = "", branch_id = "", emp_branch_id = "", ticketjctype_id = "";
	public String category_id = "";
	public String tickettype_id = "", ticketdays_id = "", dr_branch_id = "";
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public MIS_Check accessmischeck = new MIS_Check();

	public String project_id = "";
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";
	public String[] ticketstatus_ids;
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Ticket ID", "numeric", "ticket_id"},
			{"Subject", "text", "ticket_subject"},
			{"Description", "text", "ticket_desc"},
			{"Source", "text", "ticket_ticketsource_id IN (SELECT ticketsource_id FROM compdb.axela_service_ticket_source WHERE ticketsource_name"},
			{"Status", "text", "ticketstatus_name"},
			{"Department", "text", "ticket_dept_name"},
			{"Category", "text", "ticket_ticketcat_id IN (SELECT ticketcat_id FROM compdb.axela_service_ticket_cat WHERE ticketcat_name"},
			{"Type", "text", "ticket_tickettype_id IN (SELECT tickettype_id FROM compdb.axela_service_ticket_type WHERE tickettype_name"},
			{"Priority", "text", "priorityticket_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc,' ',contact_fname,' ',contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Executive", "text", "emp_name"},
			{"Brand Name", "text", "branch_brand_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Branch Name", "text", "branch_name"},
			{"Enquiry ID", "numeric", "ticket_enquiry_id"},
			{"Pre-Owned ID", "numeric", "ticket_preowned_id"},
			{"SO ID", "numeric", "ticket_so_id"},
			{"CRM ID", "numeric", "ticket_crm_id"},
			{"CRM Follow-up Time", "date", "ticket_crm_id IN (SELECT crm_id FROM compdb.axela_sales_crm WHERE crm_followup_time"},
			{"JC ID", "numeric", "ticket_jc_id"},
			{"JC Date", "date", "ticket_jc_id IN (SELECT jc_id FROM compdb.axela_service_jc WHERE jc_time_in"},
			{"JCPSF ID", "numeric", "ticket_jcpsf_id"},
			{"JCPSF Follow-up Time", "date", "ticket_jcpsf_id IN (SELECT jcpsf_id FROM compdb.axela_service_jc_psf WHERE jcpsf_followup_time"},
			{"Vehicle ID", "numeric", "ticket_veh_id"},
			{"Reg. No.", "text", "ticket_veh_id IN (SELECT veh_id FROM compdb.axela_service_veh WHERE veh_reg_no"},
			{"Chassis Number", "text", "ticket_veh_id IN (SELECT veh_id FROM compdb.axela_service_veh WHERE veh_chassis_no"},

			{"Variant", "text", "ticket_veh_id IN (SELECT veh_id FROM compdb.axela_service_veh "
					+ " INNER JOIN axela_preowned_variant ON variant_id = veh_variant_id"
					+ " WHERE variant_name"},

			{"Model", "text", "ticket_veh_id IN (SELECT veh_id FROM compdb.axela_service_veh"
					+ " INNER JOIN axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE preownedmodel_name"},

			{"Notes", "text", "ticket_notes"},
			{"Entry By", "text", "ticket_entry_id in (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "ticket_entry_date"},
			{"Modified By", "text", "ticket_modified_id in (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "ticket_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// SOP("BranchAccess==" + BranchAccess);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				emp_id = CNumeric(GetSession("emp_id", request));
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
				emp_branch_id = GetSession("emp_branch_id", request);

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
					StrSearch = StrSearch + " AND ticket_id =" + ticket_id + "";
				} else if (!(previous_ticket_id.equals("0"))) {
					msg = msg + "<br>Results for Related Ticket ID =" + previous_ticket_id + "!";
					StrSearch = StrSearch + " AND IF((SELECT ticket_customer_id FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_id = " + previous_ticket_id + ")!=0, "
							+ " ticket_customer_id IN (SELECT ticket_customer_id FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_id = " + previous_ticket_id + "), "
							+ " ticket_emp_id IN (SELECT ticket_emp_id FROM " + compdb(comp_id) + "axela_service_ticket WHERE ticket_id = " + previous_ticket_id + "))";
				} else if (!(contact_id.equals("0"))) {
					msg = msg + "<br>Results for Contact ID =" + contact_id + "!";
					StrSearch = StrSearch + " AND ticket_contact_id =" + contact_id + "";
				} else if (!(customer_id.equals("0"))) {
					msg = msg + "<br>Results for Customer ID =" + customer_id + "!";
					StrSearch = StrSearch + " AND ticket_customer_id =" + customer_id + "";
				} else if (!(ticket_emp_id.equals("0"))) {
					msg = msg + "<br>Results for Executive ID =" + ticket_emp_id + "!";
					StrSearch = StrSearch + " AND ticket_emp_id =" + ticket_emp_id + " AND ticket_ticketstatus_id!=3 ";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("ticketstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("ticketstrsql", request);
					}
				} else if (!ticket_customer_id.equals("0")) {
					msg = "Results for Customer ID = " + ticket_customer_id + "!";
					StrSearch = StrSearch + " AND customer_id = " + ticket_customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = StrSearch + SmartSearch.BuildSmartSql(smartarr, request);
					// SOP("StrSearch==" + StrSearch);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND ticket_id = 0";
					} else {
						msg = "Results for Search!";
					}
				}
				// SOP("StrSearch----" + StrSearch);
				SetSession("ticketstrsql", StrSearch, request);
				// // SOP("StrSearch====" + StrSearch);
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
		String update_info = "";
		String ticketstatus_id = "";
		long closetime = 0;
		StringBuilder Str = new StringBuilder();

		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// // SOP("emp_branch_id=====222==" + emp_branch_id);
				// to know no of records depending on search
				StrSql = "SELECT ticket_id, ticket_subject, ticket_desc, ticket_customer_id, ticket_contact_id,"
						+ " COALESCE(customer_id, 0) AS customer_id, coalesce(customer_name, '') AS customer_name,"
						+ " COALESCE(ticketcat_name, '') AS ticketcat_name,"
						+ " COALESCE(tickettype_name, '') AS tickettype_name,"
						+ " COALESCE(branch_id, 0) AS branch_id, "
						+ " COALESCE(branch_name, '') AS branch_name,"
						+ " COALESCE (ticket_branch_id, '') AS ticket_branch_id,"
						+ " COALESCE (branch_name,'') AS ticketbranch_name,"
						+ " COALESCE(branch_brand_id,'') AS branch_brand_id,"

						// for ref column
						+ " ticket_enquiry_id, ticket_preowned_id, ticket_preowned_crm_id,"
						+ " ticket_so_id, ticket_crm_id, ticket_veh_id, ticket_jc_id, ticket_jcpsf_id,"
						+ " priorityticket_name, priorityticket_id,"
						+ " ticket_trigger,"
						+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name, ticketstatus_id, ticketstatus_name,"
						+ " COALESCE(contact_id,0) AS contact_id,"
						+ " COALESCE(contact_mobile1,'') AS contact_mobile1,"
						+ " COALESCE(contact_mobile2,'') AS contact_mobile2,"
						+ " COALESCE(contact_email1, '') AS contact_email1,"
						+ " COALESCE(contact_email2, '') AS contact_email2, "
						+ " emp_id, CONCAT(emp_name, ' (',emp_ref_no, ')') AS emp_name, ticket_dept_name,"
						+ " ticket_report_time, ticket_due_time, ticket_closed_time";

				CountSql = " SELECT COUNT(distinct(ticket_id)) ";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_ticket"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id";
				if ((emp_all_exe.equals("1") && !emp_id.equals("1"))) {
					if (!emp_branch_id.equals("0")) {
						SqlJoin += " AND emp_branch_id = " + emp_branch_id;
					}
				}
				SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_source ON ticketsource_id = ticket_ticketsource_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id";
				if (!ticketjctype_id.equals("")) {
					SqlJoin += " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id";
				}

				SqlJoin += " WHERE 1 = 1 "
						+ BranchAccess.replace("branch_id", "ticket_branch_id");

				// to show his own ticket if he is form head office

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;
				// // SOP("CountSql------------1-------" + CountSql);
				QueryString = QueryString.replaceAll("&orderby=" + orderby, "");
				QueryString = QueryString.replaceAll("&ordertype=" + ordertype, "");
				ordernavi = "ticket-list.jsp?" + QueryString;
				if (ordertype.equals("asc")) {
					ordertype = "desc";
				} else {
					ordertype = "asc";
				}

				if (!(StrSearch.equals(""))) {
					StrSql += ExeAccess + StrSearch + " GROUP BY ticket_id";
					if (orderby.equals("")) {
						StrSql = StrSql + " ORDER BY ticket_id DESC ";
					} else {
						StrSql = StrSql + " ORDER BY " + orderby + " " + ordertype + " ";
					}
				}
				if (all.equals("yes")) {
					CountSql += " AND ticket_ticketstatus_id != 3";
				}

				CountSql += ExeAccess + StrSearch;
				// SOP("StrSql-----2--------------" + StrSql);
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

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_service_ticket\\b",
								"FROM " + compdb(comp_id) + "axela_service_ticket"
										+ " INNER JOIN (SELECT ticket_id FROM " + compdb(comp_id) + "axela_service_ticket"
										+ " WHERE 1 = 1"
										+ " AND ticket_ticketstatus_id != 3" + ExeAccess.replace("emp_id", "ticket_emp_id")
										+ " GROUP BY ticket_id"
										+ " ORDER BY ticket_id DESC"
										// + " LIMIT " + (StartRec - 1) + ", " + recperpage
										+ ") AS myresults USING (ticket_id)");
						// // SOP("StrSql==" + StrSql);
						// StrSql = "SELECT * FROM (" + StrSql + ") AS datatable";

						if (orderby.equals("")) {
							StrSql += " ORDER BY ticket_id DESC";
						} else {
							StrSql += " ORDER BY " + orderby + " " + ordertype + " ";
						}
					}
					// else {
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// }
					if (emp_id.equals("1")) {
						// SOPInfo("StrSql -------------" + StrSql);
					}
					// SOP("StrSql -------------" + StrSql);
					crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >").append(GridLink("ID", "ticket_id", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Ticket", "ticket_subject", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th >").append(GridLink("Customer", "customer_name", ordernavi, ordertype)).append("</th>\n");
					// Str.append("<th data-hide=\"phone\">").append("Reference").append("</th>\n");
					Str.append("<th data-hide=\"phone\">").append(GridLink("Report Time", "ticket_report_time", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Due Time", "ticket_due_time", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Status", "ticketstatus_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Priority", "priorityticket_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Level", "ticket_trigger", ordernavi, ordertype)).append("</th>\n");
					if (config_service_ticket_cat.equals("1")) {
						Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Category", "ticketcat_name", ordernavi, ordertype)).append("</th>\n");
					}
					if (config_service_ticket_type.equals("1")) {
						Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Type", "tickettype_name", ordernavi, ordertype)).append("</th>\n");
					}
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Department", "ticket_dept_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Executive", "emp_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Branch", "branch_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						ticketstatus_id = crs.getString("ticketstatus_id");
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("ticket_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("ticket_id") + ");'");
						Str.append(" >\n");
						Str.append("<td>").append(count).append("</td>\n");
						Str.append("<td><a href=\"javascript:remote=window.open('ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("','ticketdash','');remote.focus();\">");
						Str.append(crs.getString("ticket_id")).append("</td>\n");
						Str.append("<td>");

						// Str.append("<a href=\"ticket-dash.jsp?ticket_id=").append(crs.getString("ticket_id")).append("\">");
						Str.append(crs.getString("ticket_subject")).append("</a></br>");
						if (!crs.getString("ticket_enquiry_id").equals("") && !crs.getString("ticket_enquiry_id").equals("0")) {
							Str.append("Enquiry ID: ");
							Str.append("<a href=\"../sales/enquiry-list.jsp?enquiry_id=").append(crs.getString("ticket_enquiry_id")).append(" \">");
							Str.append(crs.getString("ticket_enquiry_id")).append("</a></br>");
						}
						if (!crs.getString("ticket_preowned_id").equals("") && !crs.getString("ticket_preowned_id").equals("0")) {
							Str.append("Pre-Owned ID: ");
							Str.append("<a href=\"../preowned/preowned-list.jsp?preowned_id=").append(crs.getString("ticket_preowned_id")).append(" \">");
							Str.append(crs.getString("ticket_preowned_id")).append("</a></br>");
						}
						if (!crs.getString("ticket_so_id").equals("") && !crs.getString("ticket_so_id").equals("0")) {
							Str.append("SO ID: ");
							Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("ticket_so_id")).append(" \">");
							Str.append(crs.getString("ticket_so_id")).append("</a></br>");
						}
						if (!crs.getString("ticket_crm_id").equals("") && !crs.getString("ticket_crm_id").equals("0")) {
							Str.append("CRM ID: ");
							Str.append("<a href=\"../sales/enquiry-dash.jsp?enquiry_id=").append(crs.getString("ticket_enquiry_id")).append("#tabs-3").append(" \">");
							Str.append(crs.getString("ticket_crm_id")).append("</a></br>");
						}

						if (!crs.getString("ticket_preowned_id").equals("") && !crs.getString("ticket_preowned_id").equals("0")) {
							Str.append("Preowned CRM ID: ");
							Str.append("<a href=\"../preowned/preowned-dash-crmfollowup.jsp?preowned_id=").append(crs.getString("ticket_preowned_id")).append(" \">");
							Str.append(crs.getString("ticket_preowned_crm_id")).append("</a></br>");
						}

						if (!crs.getString("ticket_veh_id").equals("") && !crs.getString("ticket_veh_id").equals("0")) {
							Str.append("Vehicle ID: ");
							Str.append("<a href=\"../service/vehicle-list.jsp?veh_id=").append(crs.getString("ticket_veh_id")).append(" \">");
							Str.append(crs.getString("ticket_veh_id")).append("</a></br>");
						}
						if (!crs.getString("ticket_jc_id").equals("") && !crs.getString("ticket_jc_id").equals("0")) {
							Str.append("JC ID: ");
							Str.append("<a href=\"../service/jobcard-list.jsp?jc_id=").append(crs.getString("ticket_jc_id")).append(" \">");
							Str.append(crs.getString("ticket_jc_id")).append("</a></br>");
						}
						if (!crs.getString("ticket_jcpsf_id").equals("") && !crs.getString("ticket_jcpsf_id").equals("0")) {
							Str.append("JCPSF ID: ");
							Str.append("<a href=\"../service/jobcard-dash.jsp?jc_id=").append(crs.getString("ticket_jc_id")).append("#tabs-9").append(" \">");
							Str.append(crs.getString("ticket_jcpsf_id")).append("</a></br>");
						}
						Str.append("</td>");

						Str.append("<td>");
						if (!crs.getString("contact_id").equals("0")) {
							Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append(" \">");
							Str.append("").append(crs.getString("customer_name")).append("</a><br>");
							Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("ticket_contact_id")).append(" \">");
							Str.append("").append(crs.getString("contact_name")).append("</a>");
							if (!crs.getString("contact_mobile1").equals("")) {
								Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("ticket_id")))
										.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
							}
							if (!crs.getString("contact_mobile2").equals("")) {
								Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("ticket_id")))
										.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
							}
							if (!crs.getString("contact_email1").equals("")) {
								Str.append("<br><span class='customer_info customer_" + crs.getString("ticket_id") + "'  style='display: none;'><a href=\"mailto:")
										.append(crs.getString("contact_email1")).append("\">");
								Str.append(crs.getString("contact_email1")).append("</a></span>");
							}
							if (!crs.getString("contact_email2").equals("")) {
								Str.append("<br><span class='customer_info customer_" + crs.getString("ticket_id") + "'  style='display: none;'><a href=\"mailto:")
										.append(crs.getString("contact_email2")).append("\">");
								Str.append(crs.getString("contact_email2")).append("</a></span>");
							}
						}
						Str.append("</td>");
						Str.append("<td align=\"center\">").append(strToLongDate(crs.getString("ticket_report_time"))).append("</td>\n");
						Str.append("<td align=\"center\">");
						if (!crs.getString("ticket_closed_time").equals("")) {
							closetime = crs.getLong("ticket_closed_time");
						} else {
							closetime = Long.parseLong(ToLongDate(kknow()));
						}
						if (!crs.getString("ticket_due_time").equals("")) {
							if (closetime >= Long.parseLong(crs.getString("ticket_due_time"))) {
								Str.append("<font color=#ff0000>").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
							} else {
								Str.append("<font color=blue>").append(strToLongDate(crs.getString("ticket_due_time"))).append("</font>");
							}
						}
						Str.append("</td>\n");
						if (ticketstatus_id.equals("3")) {
							Str.append("<td><font color=\"red\">").append(crs.getString("ticketstatus_name")).append("</font></td>\n");
						} else {
							Str.append("<td >").append(crs.getString("ticketstatus_name")).append("</td>\n");
						}
						Str.append("<td nowrap>").append(crs.getString("priorityticket_name")).append("</td>\n");
						Str.append("<td nowrap>").append(crs.getString("ticket_trigger")).append("</td>\n");
						if (config_service_ticket_cat.equals("1")) {
							Str.append("<td nowrap>").append(crs.getString("ticketcat_name")).append("</td>\n");
						}
						if (config_service_ticket_type.equals("1")) {
							Str.append("<td  nowrap>").append(crs.getString("tickettype_name")).append("</td>\n");
						}
						Str.append("<td>").append(crs.getString("ticket_dept_name")).append("</td>\n");
						Str.append("<td>");
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
						Str.append("<td>");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("ticket_branch_id")).append("\">").append(crs.getString("ticketbranch_name"))
								.append("</a></td>");
						Str.append("</td>\n<td nowrap>");
						update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul style='margin-top: -5px;' class='dropdown-content dropdown-menu pull-right'>"

								+ "<li role=presentation><a href=\"ticket-update.jsp?update=yes&ticket_id="
								+ crs.getInt("ticket_id") + "\">Update Ticket</a></li>"

								+ "<li role=presentation><a href=\"ticket-summary-print.jsp?ticket_id="
								+ crs.getInt("ticket_id") + "\">Print Ticket</a></li>";

						if ((!crs.getString("ticket_jc_id").equals("0")) && !crs.getString("ticket_jcpsf_id").equals("0")) {
							if (crs.getString("branch_brand_id").equals("2")) {
								update_info += "<li role=presentation><a href=\"ticket-maruti-print.jsp?reportfrom=ticket-maruti-print&ticket_id=";
							}
							else {
								update_info += "<li role=presentation><a href=\"ticket-general-print.jsp?reportfrom=ticket-general-print&ticket_id=";
							}
							update_info += crs.getInt("ticket_id") + " \"> PSF Ticket Print </a></li>"
									+ "<li role=presentation><a href=\"ticket-maruti-sa-homevisit-print.jsp?reportfrom=ticket-maruti-sa-homevisit-print&ticket_id="
									+ crs.getInt("ticket_id") + "\">Print Service Advisor Home Visit</a></li>";
						}
						update_info += "</ul></div></center></div>";
						Str.append(update_info);
						Str.append("</td>");
						Str.append("</tr>\n");
						Str.append("</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();

				} else {
					Str.append("<br><br><br><br><font color=red><b><center>No Ticket(s) found!</center></b></font><br><br>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
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
		ticketdays_id = CNumeric(PadQuotes(request.getParameter("dr_ticketdays_id")));
		brand_id = RetrunSelectArrVal(request, "dr_brand");
		brand_ids = request.getParameterValues("dr_brand");
		// // SOP("brand_id-----------" + brand_id);
		// // SOP("brand_ids-----------" + brand_ids);
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		ticketjctype_id = RetrunSelectArrVal(request, "dr_ticketjctype_id");
		ticketjctype_ids = request.getParameterValues("dr_ticketjctype_id");

		if (request.getParameterValues("chk_ticket_status") != null) {
			ticketstatus_ids = request.getParameterValues("chk_ticket_status");
		}
		if (!executive_id.equals("0")) {
			StrSearch = StrSearch + " AND ticket_emp_id = " + executive_id;
		}
		if (!ticket_dept_id.equals("0")) {
			StrSearch = StrSearch + " AND ticket_ticket_dept_id = " + ticket_dept_id;
		}
		if (!priority_id.equals("0")) {
			StrSearch = StrSearch + " AND ticket_priorityticket_id = " + priority_id;
		}
		if (!category_id.equals("0")) {
			StrSearch = StrSearch + " AND ticket_ticketcat_id = " + category_id;
		}
		if (!tickettype_id.equals("0")) {
			StrSearch = StrSearch + " AND ticket_tickettype_id = " + tickettype_id;
		}

		if (!ticketdays_id.equals("0")) {
			if (tickettype_id.equals("1") || tickettype_id.equals("2") || tickettype_id.equals("3")) {
				StrSearch += " AND ticket_crm_id IN (SELECT crm_id FROM " + compdb(comp_id) + "axela_sales_crm WHERE crm_crmdays_id = " + ticketdays_id + ")";
			}
			else if (tickettype_id.equals("4")) {
				StrSearch += " AND ticket_jcpsf_id IN (SELECT jcpsf_id FROM " + compdb(comp_id) + "axela_service_jc_psf WHERE jcpsf_psfdays_id = " + ticketdays_id + ")";
			}
		}

		if (!brand_id.equals("")) {
			StrSearch = StrSearch + " AND branch_brand_id IN (" + brand_id + ")";
		}
		if (!region_id.equals("")) {
			StrSearch = StrSearch + " AND branch_region_id IN (" + region_id + ")";
		}
		if (!branch_id.equals("")) {
			StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
		}

		if (!ticketjctype_id.equals("")) {
			StrSearch = StrSearch + " AND jc_jctype_id IN (" + ticketjctype_id + ")";
		}

		if (ticketstatus_ids != null) {
			StrSearch = StrSearch + " AND ticket_ticketstatus_id IN (" + RetrunSelectArrVal(request, "chk_ticket_status") + ")";
		}
		StringBuilder Str = new StringBuilder();
		Str.append("<table class=\"footable-loaded footable\">");
		Str.append("<tr>\n");
		Str.append("<td nowrap><div class=\"container-fluid\">");

		Str.append("<div class=\"form-element3\">");
		Str.append("<div>Brands:</div>");
		Str.append("<div id=\"multiprincipal\">");
		Str.append("<select name=dr_brand size=10 multiple=multiple class='form-control multiselect-dropdown' id=dr_brand");
		Str.append(" onChange=PopulateBranches();PopulateRegion();>");
		Str.append(accessmischeck.PopulatePrincipal(brand_ids, comp_id, request));
		Str.append("</select></div></div>");

		Str.append("<div class=\"form-element3\">");
		Str.append("<div>Regions:</div>");
		Str.append("<span id=regionHint>");
		Str.append(accessmischeck.PopulateRegion(brand_id, region_ids, comp_id, request));
		Str.append("</span></div>");

		Str.append("<div class=\"form-element3\">");
		Str.append("<div>Branches:</div>");
		Str.append("<span id=branchHint>");
		Str.append(accessmischeck.PopulateBranches(brand_id, region_id, branch_ids, comp_id, request));
		Str.append("</span></div>");

		Str.append("<div class=\"form-element3\"><div>Jc Type:</div>");
		Str.append("<select name='dr_ticketjctype_id' class='form-control multiselect-dropdown'  id='dr_ticketjctype_id' multiple='multiple' size=10>");
		Str.append(PopulateJcType());
		Str.append("</select></div>\n");

		Str.append("</div></td>");
		Str.append("</tr>\n");

		Str.append("<tr>\n");
		Str.append("<td>");
		Str.append("<div class=\"form-element4\"><label>Executive:</label>");
		Str.append("<select name=dr_executive class='form-control' id=dr_executive>");
		Str.append(PopulateExecutive());
		Str.append("</select></div>");

		Str.append("<div class=\"form-element4\"><label >Department:  </label>");
		Str.append("<select name=dr_ticket_dept class='form-control' id=dr_ticket_dept>");
		Str.append(PopulateDepartment());
		Str.append("</select></div>");

		if (config_service_ticket_cat.equals("1")) {
			Str.append("<div class=\"form-element4\"><label >Category:</label>");
			Str.append("<select name=dr_category class='form-control' id=dr_category>");
			Str.append(PopulateCategory());
			Str.append("</select></div>");
		}
		Str.append("</td>\n");
		Str.append("</tr>");
		Str.append("<tr>\n");
		Str.append("<td>");
		Str.append("<div class=\"form-element4\"><label>Priority:</label>");
		Str.append("<select name=dr_priority class='form-control' id=dr_priority>");
		Str.append(PopulatePriority());
		Str.append("</select></div>");

		if (config_service_ticket_type.equals("1")) {
			Str.append("<div class=\"form-element4\"><label>Type: </label>");
			Str.append("<select name=dr_tickettype_id class='form-control' id=dr_tickettype_id onchange=\"PopulateDays();\">");
			Str.append(PopulateType());
			Str.append("</select></div>");
		}
		Str.append("<div class=\"form-element4\"><label>Days: </label>");
		Str.append("<div  id='crmdaysHint'>");
		Str.append(PopulateDays(comp_id, tickettype_id, branch_id));
		Str.append("</div></td>\n");
		Str.append("</tr>");

		// Str.append("</select></div></div>\n");
		// Str.append("</div></td>\n");
		// Str.append("</tr>\n");
		//
		// Str.append("</td>\n");
		// Str.append("</tr>");
		Str.append("<tr>");
		Str.append("<td align=center><br>");
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
			StrSql = "SELECT emp_name, emp_ref_no, emp_id,"
					+ " COALESCE(ticketcount, 0) AS ticketcount"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN (SELECT COUNT(ticket_id) AS ticketcount, ticket_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket "
					+ " WHERE ticket_ticketstatus_id != 3"
					+ " GROUP BY ticket_emp_id"
					+ " ) AS tblticket ON tblticket.ticket_emp_id = emp_id"
					+ " WHERE emp_active = '1'"
					+ " AND emp_ticket_owner ='1'"
					+ " GROUP BY emp_id "
					+ "	ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// // SOP("StrSql-all-" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), executive_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") [").append(crs.getString("ticketcount")).append("]</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateDepartment() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		try {
			StrSql = "SELECT ticket_dept_id, ticket_dept_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept"
					+ " WHERE 1=1 "
					+ " GROUP BY ticket_dept_id"
					+ " ORDER BY ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticket_dept_id"), ticket_dept_id));
				Str.append(">").append(crs.getString("ticket_dept_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT priorityticket_id, priorityticket_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_priority "
					+ " WHERE 1=1 "
					+ " GROUP BY priorityticket_id"
					+ " ORDER BY priorityticket_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityticket_id")).append("");
				Str.append(StrSelectdrop(crs.getString("priorityticket_id"), priority_id));
				Str.append(">").append(crs.getString("priorityticket_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCategory() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT ticketcat_id, ticketcat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_cat "
					+ " WHERE 1=1 "
					+ " GROUP BY ticketcat_id "
					+ " ORDER BY ticketcat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketcat_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ticketcat_id"), category_id));
				Str.append(">").append(crs.getString("ticketcat_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT tickettype_id, tickettype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type "
					+ " WHERE 1 = 1 "
					+ " GROUP BY tickettype_id "
					+ " ORDER BY tickettype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tickettype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("tickettype_id"), tickettype_id));
				Str.append(">").append(crs.getString("tickettype_name")).append("</option><br> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateJcType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type "
					+ " WHERE 1 = 1 "
					+ " GROUP BY jctype_id "
					+ " ORDER BY jctype_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("jctype_id"), ticketjctype_ids));
				Str.append(">").append(crs.getString("jctype_name")).append("</option><br> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		String ids = "";
		StrSql = "SELECT ticketstatus_id, ticketstatus_name "
				+ " FROM " + compdb(comp_id) + "axela_service_ticket_status "
				+ " ORDER BY ticketstatus_rank";
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
				Str.append(">").append(crs.getString("ticketstatus_name")).append("&nbsp;&nbsp;&nbsp;&nbsp;");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {

		StrSql = " SELECT config_service_ticket_cat, config_service_ticket_type "
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1";
		// // SOP("StrSql---"+StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_service_ticket_cat = crs.getString("config_service_ticket_cat");
				config_service_ticket_type = crs.getString("config_service_ticket_type");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateDays(String comp_id, String tickettype_id, String ticket_brand_id) {
		StringBuilder Str = new StringBuilder();
		// // SOP("tickettype_id===" + tickettype_id);
		// // SOP("ticket_brand_id===" + ticket_brand_id);
		try {
			if (tickettype_id.equals("1") || tickettype_id.equals("2") || tickettype_id.equals("3")) {
				StrSql = "SELECT COALESCE(crmdays_id,0) AS id,"
						+ " COALESCE(CONCAT(crmdays_daycount, crmdays_desc),'' )AS days_desc, brand_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " INNER JOIN axela_brand ON brand_id = crmdays_brand_id"
						+ " WHERE 1 = 1";
				StrSql += " AND crmdays_crmtype_id =" + tickettype_id + "";
				if (!ticket_brand_id.equals("null") && !ticket_brand_id.equals("")) {
					StrSql += " AND brand_id IN ( " + ticket_brand_id + " )";
				}
				StrSql += " GROUP BY brand_id, crmdays_id"
						+ " ORDER BY brand_name, crmdays_daycount";
			}
			else if (tickettype_id.equals("4")) {
				StrSql = "SELECT COALESCE(psfdays_id,0) AS id,"
						+ " COALESCE(CONCAT(psfdays_daycount, psfdays_desc),'') AS days_desc, brand_name"
						+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
						+ " INNER JOIN axela_brand ON brand_id = psfdays_brand_id"
						+ " WHERE 1=1";
				if (!ticket_brand_id.equals("null") && !ticket_brand_id.equals("")) {
					StrSql += " AND brand_id IN ( " + ticket_brand_id + " )";
				}
				StrSql += " GROUP BY brand_id, psfdays_id"
						+ " ORDER BY brand_name, psfdays_daycount";
			}
			else {
				StrSql = "SELECT '' AS id, '' AS days_desc, '' AS brand_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " WHERE 1<>1";
			}
			// // SOP("StrSql-------------PopulateDays---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_ticketdays_id\" name=\"dr_ticketdays_id\" class='form-control'>");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("id")).append("");
				Str.append(StrSelectdrop(crs.getString("id"), ticketdays_id));
				Str.append(">").append(crs.getString("brand_name")).append(" - ").append(crs.getString("days_desc")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
