package axela.service;

//created by ankit
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Ticket_Status extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "", year = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "", branch = "";
	public String[] brand_ids, region_ids, zone_ids, branch_ids, model_ids, dr_ticket_owner_ids, ticketdays_ids;
	public String brand_id = "", region_id = "", zone_id = "", dr_ticket_owner_id = "", ticketdays_id;
	public String dr_totalby = "0";
	public String include_pending = "", ticket_overdue = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String branch_name = "";
	public String StrSearch = "", StrFilter = "", filter = "";
	public String emp_all_exe = "";

	public String ticket_priorityticket_id = "", ticket_ticket_dept_id = "", ticket_tickettype_id = "",
			ticket_ticketcat_id = "", ticket_ticketsource_id = "";
	public String[] ticket_priorityticket_ids, ticket_ticket_dept_ids, ticket_tickettype_ids,
			ticket_ticketcat_ids, ticket_ticketsource_ids;

	static DecimalFormat deci = new DecimalFormat("0.000");
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	HashMap<String, Integer> ticketstatusname = new HashMap<String, Integer>();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				filter = PadQuotes(request.getParameter("filter"));
				starttime = PadQuotes(request.getParameter("txt_starttime"));
				endtime = PadQuotes(request.getParameter("txt_endtime"));
				if (!endtime.equals("")) {
					year = endtime.substring(6, 10);
				}

				if (starttime.equals("")) {
					starttime = strToShortDate(ToShortDate(kknow()));
					start_time = strToShortDate(ToShortDate(kknow()));
				}
				if (endtime.equals("")) {
					endtime = strToShortDate(ToShortDate(kknow()));
					end_time = strToShortDate(ToShortDate(kknow()));
				}

				if (filter.equals("yes")) {
					TicketListRedirect(request, response);
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					// // SOP("Start D===" + starttime.substring(0, 6) + "01");
					// // SOP("End D====" + endtime.substring(0, 6) + "31");
					// BranchAccess = BranchAccess;
					// .replace("branch_id", "jc_branch_id");
					// ExeAccess += ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!zone_id.equals("")) {
						StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}
					if (!dr_ticket_owner_id.equals("")) {
						StrSearch += " AND ticket_emp_id IN (" + dr_ticket_owner_id + ")";
					}

					if (!ticket_ticket_dept_id.equals("")) {
						StrSearch += " AND ticket_ticket_dept_id IN (" + ticket_ticket_dept_id + ")";
					}
					if (!ticket_ticketcat_id.equals("")) {
						StrSearch += " AND ticket_ticketcat_id IN (" + ticket_ticketcat_id + ")";
					}
					if (!ticket_tickettype_id.equals("")) {
						StrSearch += " AND ticket_tickettype_id IN (" + ticket_tickettype_id + ")";
					}

					if (!ticketdays_id.equals("") && ((ticket_tickettype_id.contains("1")
							|| ticket_tickettype_id.contains("2")
							|| ticket_tickettype_id.contains("3"))
							&& (!ticket_tickettype_id.contains("4")
							&& !ticket_tickettype_id.contains("5")
							&& !ticket_tickettype_id.contains("6")
							&& !dr_totalby.equals("jc_emp_id")))) {
						StrSearch += " AND crmdays_id IN (" + ticketdays_id + ")";
					}
					if (!ticketdays_id.equals("") && (ticket_tickettype_id.contains("4"))
							&& (!ticket_tickettype_id.contains("1")
									&& !ticket_tickettype_id.contains("2")
									&& !ticket_tickettype_id.contains("3")
									&& !ticket_tickettype_id.contains("5")
									&& !ticket_tickettype_id.contains("6")
									&& !dr_totalby.equals("enquiry_emp_id"))) {
						StrSearch += " AND psfdays_id IN (" + ticketdays_id + ")";
					}

					if (!ticket_ticketsource_id.equals("")) {
						StrSearch += " AND ticket_ticketsource_id IN (" + ticket_ticketsource_id + ")";
					}
					if (!ticket_priorityticket_id.equals("")) {
						StrSearch += " AND ticket_priorityticket_id IN (" + ticket_priorityticket_id + ")";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = TicketStatusDetails();
					}
				}
			}

			// // SOP("StrSearch_----------" + StrSearch);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");

		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		dr_ticket_owner_id = RetrunSelectArrVal(request, "dr_ticket_owner");
		dr_ticket_owner_ids = request.getParameterValues("dr_ticket_owner");
		dr_totalby = PadQuotes(request.getParameter("dr_totalby"));

		ticket_tickettype_ids = request.getParameterValues("dr_ticket_tickettype_id");
		ticket_ticketsource_ids = request.getParameterValues("dr_ticket_ticketsource_id");
		ticket_priorityticket_ids = request.getParameterValues("dr_ticket_priorityticket_id");
		ticket_ticket_dept_ids = request.getParameterValues("dr_ticket_dept_id");
		ticket_ticketcat_ids = request.getParameterValues("dr_ticket_ticketcat_id");
		ticketdays_ids = request.getParameterValues("dr_ticketdays_id");

		ticket_tickettype_id = RetrunSelectArrVal(request, "dr_ticket_tickettype_id");
		ticket_ticketsource_id = RetrunSelectArrVal(request, "dr_ticket_ticketsource_id");
		ticket_priorityticket_id = RetrunSelectArrVal(request, "dr_ticket_priorityticket_id");
		ticket_ticket_dept_id = RetrunSelectArrVal(request, "dr_ticket_dept_id");
		ticket_ticketcat_id = RetrunSelectArrVal(request, "dr_ticket_ticketcat_id");
		ticketdays_id = RetrunSelectArrVal(request, "dr_ticketdays_id");

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		include_pending = PadQuotes(request.getParameter("chk_include_pending"));
		if (include_pending.equals("on")) {
			include_pending = "1";
		}
		else {
			include_pending = "0";
		}

		ticket_overdue = PadQuotes(request.getParameter("chk_overdue"));
		if (ticket_overdue.equals("on")) {
			ticket_overdue = "1";
		}
		else {
			ticket_overdue = "0";
		}
	}
	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>SELECT Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>SELECT End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String TicketStatusDetails() {
		try {
			String SearchURL = "<a href=../service/report-ticket-status.jsp?filter=yes";
			String type = "", typename = "";

			String StrSqlcount = "";
			int count = 0;
			long rowcount = 0;
			long totalrowcount = 0;
			HashMap<String, String> statusname = new LinkedHashMap<String, String>();
			int rowlasttime = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT COALESCE(ticketstatus_id, 0) AS 'ticketstatus_id',"
					+ " COALESCE(ticketstatus_name, '') AS 'ticketstatus_name'"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_status"
					+ " WHERE 1 = 1"
					+ " GROUP BY ticketstatus_id"
					+ " ORDER BY ticketstatus_rank";

			CachedRowSet crsticketsql = processQuery(StrSql, 0);
			// // SOP("crstrconcernsql====" + StrSql);
			while (crsticketsql.next()) {
				rowlasttime++;
				if (include_pending.equals("1") && ticket_overdue.equals("1")) {
					StrSqlcount += " SUM(IF(ticket_ticketstatus_id = " + crsticketsql.getString("ticketstatus_id")
							+ " AND SUBSTRING(ticket_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
							+ " AND SUBSTRING(ticket_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) , 1, 0))"
							+ " AS '" + crsticketsql.getString("ticketstatus_name") + "',";
				} else {
					StrSqlcount += " SUM(IF(ticket_ticketstatus_id = " + crsticketsql.getString("ticketstatus_id") + ", 1, 0))"
							+ " AS '" + crsticketsql.getString("ticketstatus_name") + "',";
				}
				statusname.put(crsticketsql.getString("ticketstatus_name"), "0");
			}
			if (!StrSqlcount.equals("")) {
				StrSqlcount = StrSqlcount.substring(0, StrSqlcount.length() - 1);
			}
			// // SOP("StrSqlcount====" + StrSqlcount);

			StrSql = " SELECT COALESCE(ticket.emp_id,0) AS ticketemp_id,";
			if (dr_totalby.equals("emp_id")) {
				StrSql += " COALESCE(CONCAT(ticket.emp_name, ' (', ticket.emp_ref_no, ')'),'') AS ticketemp_name, ";
			}
			if (dr_totalby.equals("enquiry_emp_id")) {
				StrSql += " COALESCE(enqemp.emp_id,0) AS enqemp_id,"
						+ " COALESCE(CONCAT(enqemp.emp_name, ' (', enqemp.emp_ref_no, ')'),'') AS enqemp_name, ";
			}
			if (dr_totalby.equals("jc_emp_id")) {
				StrSql += " COALESCE(serviceemp.emp_id,0) AS jcemp_id,"
						+ " COALESCE(CONCAT(serviceemp.emp_name, ' (', serviceemp.emp_ref_no, ')'),'') AS jcemp_name, ";
			}

			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " brand_id, brand_name, ";
			}
			if (dr_totalby.equals("branch_zone_id")) {
				StrSql += " zone_id, zone_name, ";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " region_id, region_name, ";
			}
			if (dr_totalby.equals("ticket_branch_id")) {
				StrSql += " branch_id, branch_name, ";
			}

			// StrSql += " COUNT(DISTINCT ticket_id) AS 'ticketcount',"
			// + " COUNT(DISTINCT ticketstatus_id) AS 'ticketstatuscount',";

			if (include_pending.equals("1")) {
				StrSql += " SUM(IF(ticket_ticketstatus_id NOT IN (3, 6), 1, 0 )) AS 'Pending',";
			}
			if (ticket_overdue.equals("1")) {
				StrSql += "SUM(IF(ticket_ticketstatus_id NOT IN (3, 6) AND SUBSTRING(ticket_due_time, 1, 12) <= SUBSTR('" + ToLongDate(kknow()) + "', 1, 12), 1, 0)) AS 'Overdue',";
			}
			StrSql += StrSqlcount
					+ " FROM " + compdb(comp_id) + "axela_service_ticket ";

			if (!dr_totalby.equals("emp_id") || !brand_id.equals("") || !region_id.equals("")
					|| !zone_id.equals("") || !branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id";
			}
			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
			}
			if (dr_totalby.equals("branch_zone_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_zone ON zone_id = branch_zone_id";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}

			if (!ticketdays_id.equals("") && ((ticket_tickettype_id.contains("1")
					|| ticket_tickettype_id.contains("2")
					|| ticket_tickettype_id.contains("3"))
					&& (!ticket_tickettype_id.contains("4")
					&& !ticket_tickettype_id.contains("5")
					&& !ticket_tickettype_id.contains("6")))
					|| dr_totalby.equals("enquiry_emp_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_id = ticket_crm_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = ticket_enquiry_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp enqemp ON enqemp.emp_id = enquiry_emp_id";
			}

			if (!ticketdays_id.equals("") && (ticket_tickettype_id.contains("4"))
					&& (!ticket_tickettype_id.contains("1")
							&& !ticket_tickettype_id.contains("2")
							&& !ticket_tickettype_id.contains("3")
							&& !ticket_tickettype_id.contains("5")
							&& !ticket_tickettype_id.contains("6"))
					|| dr_totalby.equals("jc_emp_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_id = ticket_jcpsf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp serviceemp ON serviceemp.emp_id = jc_emp_id";;
			}

			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ticket ON ticket.emp_id = ticket_emp_id ";
			StrSql += " WHERE 1 = 1 ";

			if (!include_pending.equals("1") && !ticket_overdue.equals("1")) {
				StrSql += " AND SUBSTRING(ticket_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTRING(ticket_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			}
			StrSql += " AND ticket.emp_active = 1"
					+ " AND ticket_ticketstatus_id != 0"
					+ StrSearch;

			StrSql += BranchAccess.replace("branch_id", "ticket_branch_id");
			StrSql += ExeAccess.replace("emp_id", "ticket_emp_id");

			StrSql += " GROUP BY ";

			if (dr_totalby.equals("enquiry_emp_id")) {
				StrSql += " enqemp.emp_id";
			} else if (dr_totalby.equals("jc_emp_id")) {
				StrSql += " serviceemp.emp_id";
			} else {
				StrSql += " ticket.emp_id";
			}

			StrSql += " ORDER BY ";

			if (dr_totalby.equals("enquiry_emp_id")) {
				StrSql += " enqemp.emp_name";
			} else if (dr_totalby.equals("jc_emp_id")) {
				StrSql += " serviceemp.emp_name";
			} else {
				StrSql += " ticket.emp_name";
			}

			// SOPInfo("StrSql------222----------------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {

				SearchURL += "&starttime=" + starttime
						+ "&endtime=" + endtime
						+ "&brand=" + brand_id
						+ "&region_id=" + region_id
						+ "&zone_id=" + zone_id
						+ "&branch_id=" + branch_id
						+ "&tkt_owner_ids=" + dr_ticket_owner_id
						+ "&priority_id=" + ticket_priorityticket_id
						+ "&dept_id=" + ticket_ticket_dept_id
						+ "&type_id=" + ticket_tickettype_id
						+ "&ticketdays_id=" + ticketdays_id
						+ "&source_id=" + ticket_ticketsource_id
						+ "&cat_id=" + ticket_ticketcat_id;

				Str.append("<div class=\"table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" style=\"text-align:center\">#</th>\n");

				if (dr_totalby.equals("emp_id")) {
					Str.append("<th data-toggle=\"true\">Ticket Owner</th>\n");
					type = "&type=emp";
					typename = "ticketemp_id";
				}
				if (dr_totalby.equals("enquiry_emp_id")) {
					Str.append("<th data-toggle=\"true\">Ticket Owner</th>\n");
					type = "&type=enqemp";
					typename = "enqemp_id";
				}
				if (dr_totalby.equals("jc_emp_id")) {
					Str.append("<th data-toggle=\"true\">Ticket Owner</th>\n");
					type = "&type=jcemp";
					typename = "jcemp_id";
				}
				if (dr_totalby.equals("branch_brand_id")) {
					Str.append("<th data-toggle=\"true\">Brand</th>\n");
					type = "&type=brand";
					typename = "brand_id";
				}
				if (dr_totalby.equals("branch_zone_id")) {
					Str.append("<th data-toggle=\"true\">Zone</th>\n");
					type = "&type=zone";
					typename = "zone_id";
				}
				if (dr_totalby.equals("branch_region_id")) {
					Str.append("<th data-toggle=\"true\">Region</th>\n");
					type = "&type=region";
					typename = "region_id";
				}
				if (dr_totalby.equals("ticket_branch_id")) {
					Str.append("<th data-toggle=\"true\">Branch</th>\n");
					type = "&type=branch";
					typename = "branch_id";
				}

				if (include_pending.equals("1")) {
					Str.append("<th data-toggle=\"true\">Pending</th>\n");
				}
				if (ticket_overdue.equals("1")) {
					Str.append("<th data-toggle=\"true\">Overdue</th>\n");
				}

				crsticketsql.beforeFirst();
				while (crsticketsql.next()) {
					Str.append("<th data-hide=\"phone\">" + crsticketsql.getString("ticketstatus_name") + "</th>\n");
				}
				Str.append("<th data-hide=\"phone\">Total</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				HashMap<String, Integer> ticketstatustotal = new HashMap<>();
				crsticketsql.beforeFirst();
				if (include_pending.equals("1")) {
					ticketstatustotal.put("pending", 0);
				}
				if (ticket_overdue.equals("1")) {
					ticketstatustotal.put("overdue", 0);
				}
				while (crsticketsql.next()) {
					// for (int i = 1; i <=
					// strconcernsql.getInt("concerntypecount"); i++) {
					ticketstatustotal.put(crsticketsql.getString("ticketstatus_name"), 0);
					// }
				}

				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");

					if (dr_totalby.equals("emp_id")) {
						Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("ticketemp_id"))
								.append(" target=_blank>").append(crs.getString("ticketemp_name")).append("</a></td>\n");
					}
					// // SOP("00000");
					if (dr_totalby.equals("enquiry_emp_id")) {
						Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("enqemp_id"))
								.append(" target=_blank>").append(crs.getString("enqemp_name")).append("</a></td>\n");
					}
					// // SOP("00001111");
					if (dr_totalby.equals("jc_emp_id")) {
						Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("jcemp_id"))
								.append(" target=_blank>").append(crs.getString("jcemp_name")).append("</a></td>\n");
					}
					// // SOP("11111");
					if (dr_totalby.equals("branch_brand_id")) {
						Str.append("<td valign=top align=left>" + crs.getString("brand_name") + "</a></td>");
					}
					if (dr_totalby.equals("branch_zone_id")) {
						Str.append("<td valign=top align=left>" + crs.getString("zone_name") + "</a></td>");
					}
					if (dr_totalby.equals("branch_region_id")) {
						Str.append("<td valign=top align=left>" + crs.getString("region_name") + "</a></td>");
					}
					if (dr_totalby.equals("ticket_branch_id")) {
						Str.append("<td valign=top align=left><a href=../portal/branch-summary.jsp?branch_id="
								+ crs.getString("branch_id") + "taregt=_blank>" + crs.getString("branch_name") + "</a></td>");
					}
					// // SOP("2222");
					if (include_pending.equals("1")) {
						Str.append("<td align=right>").append(SearchURL).append(type)
								.append("&typeid=").append(crs.getString(typename))
								.append("&ticketstatusid=1,2,5,7")
								.append("&chk_include_pending=" + include_pending)
								.append("&chk_overdue=" + ticket_overdue)
								.append(" target=_blank>")
								.append(crs.getString("Pending")).append("</a></td>");
					}
					if (ticket_overdue.equals("1")) {
						Str.append("<td align=right>").append(SearchURL).append(type)
								.append("&typeid=").append(crs.getString(typename))
								.append("&ticketstatusid=1,2,5,7")
								.append("&chk_include_pending=" + include_pending)
								.append("&chk_overdue=" + ticket_overdue)
								.append(" target=_blank>")
								.append(crs.getString("Overdue")).append("</a></td>");
					}
					crsticketsql.beforeFirst();
					int rowtime = 0;
					while (crsticketsql.next()) {
						rowtime++;
						// if (rowtime <= 1) {
						// rowcount += crs.getInt("Pending");
						// }
						// // SOP("3333" + typename);
						rowcount += crs.getInt(crsticketsql.getString("ticketstatus_name"));
						Str.append("<td align=right>").append(SearchURL).append(type)
								.append("&typeid=").append(crs.getString(typename))
								.append("&ticketstatusid=").append(crsticketsql.getString("ticketstatus_id"))
								.append("&chk_include_pending=" + include_pending)
								.append("&chk_overdue=" + ticket_overdue)
								.append(" target=_blank>")
								.append(crs.getInt(crsticketsql.getString("ticketstatus_name")))
								.append("</a></td>\n");
						ticketstatustotal.put(crsticketsql.getString("ticketstatus_name") + "",
								ticketstatustotal.get(crsticketsql.getString("ticketstatus_name")
										+ "") + crs.getInt(crsticketsql.getString("ticketstatus_name")));
					}
					// // SOP("555");
					if (include_pending.equals("1")) {
						ticketstatustotal.put("pending", ticketstatustotal.get("pending") + crs.getInt("Pending"));
					}
					if (ticket_overdue.equals("1")) {
						ticketstatustotal.put("overdue", ticketstatustotal.get("overdue") + crs.getInt("Overdue"));
					}
					Str.append("<td align=right><b>").append(SearchURL).append(type)
							.append("&typeid=").append(crs.getString(typename))
							.append("&chk_include_pending=" + include_pending)
							.append("&chk_overdue=" + ticket_overdue)
							.append(" target=_blank>")
							.append(rowcount).append("</a></b>\n");
					Str.append("</td></tr>");
					totalrowcount += rowcount;
					if (rowlasttime == rowtime) {
						rowcount = 0;
					}
				}
				// // SOP("coming...12");
				Str.append("<tr>\n");
				Str.append("<td align=right colspan='2'><b>Total: </b></td>\n");
				if (include_pending.equals("1")) {
					Str.append("<td align=right><b>").append(SearchURL + type).append("&ticketstatusid=1,2,5,7")
							.append("&chk_include_pending=" + include_pending)
							.append("&chk_overdue=" + ticket_overdue).append(" target=_blank>"
									+ ticketstatustotal.get("pending") + "</a></b></td>\n");
				}
				// // SOP("coming...13");
				if (ticket_overdue.equals("1")) {
					Str.append("<td align=right><b>").append(SearchURL + type).append("&ticketstatusid=1,2,5,7")
							.append("&chk_include_pending=" + include_pending)
							.append("&chk_overdue=" + ticket_overdue).append(" target=_blank>"
									+ ticketstatustotal.get("overdue") + "</a></b></td>\n");
				}
				crsticketsql.beforeFirst();
				// // SOP("coming...14");
				while (crsticketsql.next()) {
					Str.append("<td align=right><b>").append(SearchURL + type).append("&ticketstatusid=")
							.append(crsticketsql.getString("ticketstatus_id"))
							.append("&chk_include_pending=" + include_pending)
							.append("&chk_overdue=" + ticket_overdue).append(" target=_blank>")
							.append(ticketstatustotal.get(crsticketsql.getString("ticketstatus_name")))
							.append("</a></b></td>\n");
					ticketstatusname.put(crsticketsql.getString("ticketstatus_name"), ticketstatustotal.get(crsticketsql.getString("ticketstatus_name")));
				}
				// // SOP("coming...15");
				Str.append("<td align=right><b>").append(SearchURL + type)
						.append("&chk_include_pending=" + include_pending)
						.append("&chk_overdue=" + ticket_overdue).append(" target=_blank>")
						.append(totalrowcount).append("</a></b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				// // SOP("coming...last");
			} else {
				Str.append("<font color=red><center><b>No Ticket Found!</b></center></font><br>");
			}
			crs.close();
			crsticketsql.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {
		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1 = 1"
					+ " AND branch_active = 1 "
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + region_id + ") ";
			}
			if (!zone_id.equals("")) {
				StrSql += " AND branch_zone_id IN (" + zone_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// // SOP("StrSql------PopulateBranches-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name='dr_branch_id' id='dr_branch_id' class='form-control multiselect-dropdown' multiple='multiple' size=10 onchange=\"PopulateTicketOwner();\" style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=emp_id").append(StrSelectdrop("emp_id", dr_totalby)).append(">Ticket Owner</option>\n");
		Str.append("<option value=enquiry_emp_id").append(StrSelectdrop("enquiry_emp_id", dr_totalby)).append(">Sales Consultant</option>\n");
		Str.append("<option value=jc_emp_id").append(StrSelectdrop("jc_emp_id", dr_totalby)).append(">Service Executive</option>\n");
		Str.append("<option value=branch_brand_id").append(StrSelectdrop("branch_brand_id", dr_totalby)).append(">Brand</option>\n");
		Str.append("<option value=branch_zone_id").append(StrSelectdrop("branch_zone_id", dr_totalby)).append(">Zone</option>\n");
		Str.append("<option value=branch_region_id").append(StrSelectdrop("branch_region_id", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=ticket_branch_id").append(StrSelectdrop("ticket_branch_id", dr_totalby)).append(">Branches</option>\n");
		return Str.toString();
	}

	public String PopulateTicketCategory(String depertment_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ticketcat_id, ticketcat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_cat "
					+ " WHERE 1 = 1 ";
			if (!depertment_id.equals("")) {
				StrSql += " AND ticketcat_ticketdept_id IN (" + depertment_id + ") ";
			}
			StrSql += " GROUP BY ticketcat_id "
					+ " ORDER BY ticketcat_name";

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_ticket_ticketcat_id\" class=\"form-control multiselect-dropdown\" multiple=\"multiple\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketcat_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("ticketcat_id"), ticket_ticketcat_ids));
				Str.append(">").append(crs.getString("ticketcat_name")).append("</option> \n");
			}
			Str.append("</select>");

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTicketType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT tickettype_id, tickettype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type "
					+ " WHERE 1 = 1 "
					+ " GROUP BY tickettype_id "
					+ " ORDER BY tickettype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tickettype_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("tickettype_id"), ticket_tickettype_ids));
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

	public String PopulateSourceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ticketsource_id, ticketsource_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_source "
					+ " where 1 = 1 "
					+ " GROUP BY ticketsource_id"
					+ " ORDER BY ticketsource_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticketsource_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("ticketsource_id"), ticket_ticketsource_ids));
				Str.append(">").append(crs.getString("ticketsource_name")).append("</option> \n");
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
		try {
			StrSql = "SELECT ticket_dept_id, ticket_dept_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_dept "
					+ " WHERE 1 = 1 "
					+ " GROUP BY ticket_dept_id"
					+ " ORDER BY ticket_dept_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ticket_dept_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("ticket_dept_id"), ticket_ticket_dept_ids));
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

	public String PopulateTicketPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT priorityticket_id, priorityticket_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_priority "
					+ " WHERE 1 = 1 "
					+ " GROUP BY priorityticket_id"
					+ " ORDER BY priorityticket_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityticket_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("priorityticket_id"), ticket_priorityticket_ids));
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

	private void TicketListRedirect(HttpServletRequest request, HttpServletResponse response) {

		try {
			HttpSession session = request.getSession(true);
			String brand_ids = PadQuotes(request.getParameter("brand"));
			String region_ids = PadQuotes(request.getParameter("region_id"));
			String zone_ids = PadQuotes(request.getParameter("zone_id"));
			String branch_ids = PadQuotes(request.getParameter("branch_id"));
			String emp_ids = PadQuotes(request.getParameter("tkt_owner_ids"));
			String ticketstatusid = PadQuotes(request.getParameter("ticketstatusid"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String type = PadQuotes(request.getParameter("type"));
			String typeid = PadQuotes(request.getParameter("typeid"));

			String include_pending = PadQuotes(request.getParameter("chk_include_pending"));
			String ticket_overdue = PadQuotes(request.getParameter("chk_overdue"));

			String ticket_priorityticket_id = PadQuotes(request.getParameter("priority_id"));
			String ticket_ticket_dept_id = PadQuotes(request.getParameter("dept_id"));
			String ticket_tickettype_id = PadQuotes(request.getParameter("type_id"));
			String ticketdays_id = PadQuotes(request.getParameter("ticketdays_id"));
			String ticket_ticketsource_id = PadQuotes(request.getParameter("source_id"));
			String ticket_ticketcat_id = PadQuotes(request.getParameter("cat_id"));

			// Brand
			if (!brand_ids.equals("")) {
				StrFilter += " AND branch_brand_id IN (" + brand_ids + ") ";
			}
			// Regions
			if (!region_ids.equals("")) {
				StrFilter += " AND branch_region_id IN (" + region_ids + ") ";
			}
			// Zone
			if (!zone_ids.equals("")) {
				StrFilter += " AND branch_zone_id IN (" + zone_ids + ") ";
			}
			// Branch
			if (!branch_ids.equals("")) {
				StrFilter += " AND branch_id IN (" + branch_ids + ") ";
			}
			// Ticket Owner
			if (!emp_ids.equals("")) {
				StrFilter += " AND emp_id IN (" + emp_ids + ") ";
			}
			// Ticket Status
			if (!ticketstatusid.equals("")) {
				StrFilter += " AND ticket_ticketstatus_id IN (" + ticketstatusid + ") ";
			}
			// Ticket Dept
			if (!ticket_ticket_dept_id.equals("")) {
				StrFilter += " AND ticket_ticket_dept_id IN (" + ticket_ticket_dept_id + ")";
			}
			// Ticket Category
			if (!ticket_ticketcat_id.equals("")) {
				StrFilter += " AND ticket_ticketcat_id IN (" + ticket_ticketcat_id + ")";
			}
			// Ticket Type
			if (!ticket_tickettype_id.equals("")) {
				StrFilter += " AND ticket_tickettype_id IN (" + ticket_tickettype_id + ")";
			}
			// Ticket Source
			if (!ticket_ticketsource_id.equals("")) {
				StrFilter += " AND ticket_ticketsource_id IN (" + ticket_ticketsource_id + ")";
			}
			// Ticket Priority
			if (!ticket_priorityticket_id.equals("")) {
				StrFilter += " AND ticket_priorityticket_id IN (" + ticket_priorityticket_id + ")";
			}

			switch (type) {
				case "brand" :
					StrFilter += " AND branch_brand_id =" + typeid;
					break;
				case "region" :
					StrFilter += " AND branch_region_id =" + typeid;
					break;
				case "zone" :
					StrFilter += " AND branch_zone_id =" + typeid;
					break;
				case "branch" :
					StrFilter += " AND branch_id =" + typeid;
					break;
				case "emp" :
					if (!ticketdays_id.equals("")
							&& ((ticket_tickettype_id.contains("1")
									|| ticket_tickettype_id.contains("2")
									|| ticket_tickettype_id.contains("3"))
							&& (!ticket_tickettype_id.contains("4")
									&& !ticket_tickettype_id.contains("5")
									&& !ticket_tickettype_id.contains("6")))) {

						StrFilter += "AND ticket_crm_id IN(SELECT crm_id FROM " + compdb(comp_id) + "axela_sales_enquiry ";

						if (!ticketdays_id.equals("")) {
							StrFilter += " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id "
									+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id";
						}

						StrFilter += " WHERE 1 = 1";

						if (!ticketdays_id.equals("")) {
							StrFilter += " AND crmdays_id IN (" + ticketdays_id + ")";
						}

						StrFilter += ")";

					} else if (!ticketdays_id.equals("") && (ticket_tickettype_id.contains("4"))
							&& (!ticket_tickettype_id.contains("1")
									&& !ticket_tickettype_id.contains("2")
									&& !ticket_tickettype_id.contains("3")
									&& !ticket_tickettype_id.contains("5")
									&& !ticket_tickettype_id.contains("6"))
							|| dr_totalby.equals("jc_emp_id")) {
						StrFilter += "AND ticket_jcpsf_id IN(SELECT jcpsf_id FROM " + compdb(comp_id) + "axela_service_jc ";

						if (!ticketdays_id.equals("")) {
							StrFilter += " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id "
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id";
						}

						if (!ticketdays_id.equals("")) {
							StrFilter += " WHERE 1 = 1 "
									+ " AND psfdays_id IN (" + ticketdays_id + ")";
						}
						StrFilter += ")";

					}

					if (!typeid.equals("")) {
						StrFilter += " AND ticket_emp_id =" + typeid;
					}
					break;
				case "enqemp" :
					StrFilter += "AND ticket_enquiry_id IN(SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry ";

					if (!ticketdays_id.equals("")) {
						StrFilter += " INNER JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_enquiry_id = enquiry_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id";
					}

					if (!typeid.equals("")) {
						StrFilter += " WHERE enquiry_emp_id = " + typeid;
					} else {
						StrFilter += " WHERE 1 = 1";
					}

					if (!ticketdays_id.equals("")) {
						StrFilter += " AND crmdays_id IN (" + ticketdays_id + ")";
					}

					StrFilter += ")";
					break;
				case "jcemp" :
					StrFilter += "AND ticket_jc_id IN(SELECT jc_id FROM " + compdb(comp_id) + "axela_service_jc ";

					if (!ticketdays_id.equals("")) {
						StrFilter += " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id";
					}

					if (!typeid.equals("")) {
						StrFilter += " WHERE jc_emp_id = " + typeid;
					} else {
						StrFilter += " WHERE 1 = 1";
					}

					if (!ticketdays_id.equals("")) {
						StrFilter += " AND psfdays_id IN (" + ticketdays_id + ")";
					}
					StrFilter += ")";
					break;
			}

			if (include_pending.equals("0") && ticket_overdue.equals("0")) {
				StrFilter += " AND SUBSTRING(ticket_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTRING(ticket_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			}

			if (include_pending.equals("1") && ticket_overdue.equals("1") && ticketstatusid.length() <= 1) {
				StrFilter += " AND SUBSTRING(ticket_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTRING(ticket_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			}

			if (ticket_overdue.equals("1") && ticketstatusid.length() > 1) {
				StrFilter += " AND SUBSTRING(ticket_due_time, 1, 12) <= SUBSTR('" + ToLongDate(kknow()) + "', 1, 12)";
			}

			StrFilter += " AND emp_active = 1"
					+ " AND ticket_ticketstatus_id != 0 ";
			// // SOP("StrSearch====" + StrFilter);
			SetSession("ticketstrsql", StrFilter, request);
			response.sendRedirect(response.encodeRedirectURL("../service/ticket-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateDays(String comp_id, String tickettype_id, String ticket_brand_id) {
		StringBuilder Str = new StringBuilder();
		// // SOP("tickettype_id=11==" + tickettype_id);
		// // SOP("=====" + ((tickettype_id.contains("1")
		// || tickettype_id.contains("2")
		// || tickettype_id.contains("3"))
		// && (!tickettype_id.contains("4"))
		// ));

		// ticket_brand_id = CleanArrVal(ticket_brand_id);
		// SOP("ticket_brand_id=22==" + ticket_brand_id);

		try {
			if ((tickettype_id.contains("1")
					|| tickettype_id.contains("2")
					|| tickettype_id.contains("3"))
					&& (!tickettype_id.contains("4")
							&& !tickettype_id.contains("5")
							&& !tickettype_id.contains("6"))) {
				StrSql = "SELECT COALESCE(crmdays_id,0) AS id,"
						+ " COALESCE(CONCAT(crmdays_daycount, crmdays_desc),'' )AS days_desc, brand_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
						+ " INNER JOIN axela_brand ON brand_id = crmdays_brand_id"
						+ " WHERE 1 = 1";
				StrSql += " AND crmdays_crmtype_id  IN (" + tickettype_id + ")";
				if (!ticket_brand_id.equals("null") && !ticket_brand_id.equals("")) {
					StrSql += " AND brand_id IN ( " + ticket_brand_id + " )";
				}
				StrSql += " GROUP BY brand_id, crmdays_id"
						+ " ORDER BY brand_name, crmdays_daycount";
			}
			else if ((tickettype_id.contains("4"))
					&& (!tickettype_id.contains("1")
							&& !tickettype_id.contains("2")
							&& !tickettype_id.contains("3")
							&& !tickettype_id.contains("5")
							&& !tickettype_id.contains("6"))) {
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
			// SOP("StrSql-------------PopulateDays---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_ticketdays_id\" name=\"dr_ticketdays_id\" class='form-control multiselect-dropdown' multiple='multiple'>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("id"), ticketdays_ids));
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
