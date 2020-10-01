package axela.service;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Index extends Connect {
	
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String StrJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrLibrary = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrSearch = "";
	public String smart = "";
	public String ticketchart_data = "";
	public String callchart_data = "";
	public String jcprioritychart_data = "";
	public String servicefollowupescchart_data = "";
	public String psfchart_data = "";
	public String insurancechart_data = "";
	public int TotalRecords = 0;
	
	public String datetype = "", StrHTML = "";
	public String curr_date = ToShortDate(kknow());
	public String back_date = "";
	public String curr_month = "", back_month = "";
	public String startquarter = "", endquarter = "";
	public String back_startquarter = "", back_endquarter = "";
	
	public String filter_brand_id = "", filter_region_id = "", filter_branch_id = "";
	public String[] brand_ids, region_ids, jc_branch_ids;
	
	// / Today
	public String veh_countday = "0";
	public String jc_countday = "0";
	public String ticket_countday = "0";
	public String service_booking_countday = "0";
	public String parts_amtday = "0";
	public String labour_amtday = "0";
	// Month
	public String veh_countmonth = "0";
	public String jc_countmonth = "0";
	public String ticket_countmonth = "0";
	public String service_booking_countmonth = "0";
	public String parts_amtmonth = "0";
	public String labour_amtmonth = "0";
	// Quarter
	public String veh_countquarter = "0";
	public String jc_countquarter = "0";
	public String ticket_countquarter = "0";
	public String service_booking_countquarter = "0";
	public String parts_amtquarter = "0";
	public String labour_amtquarter = "0";
	
	// Today
	public String veh_countday_old = "0";
	public String jc_countday_old = "0";
	public String ticket_countday_old = "0";
	public String service_booking_countday_old = "0";
	public String parts_amtday_old = "0";
	public String labour_amtday_old = "0";
	// Month
	public String veh_countmonth_old = "0";
	public String jc_countmonth_old = "0";
	public String ticket_countmonth_old = "0";
	public String service_booking_countmonth_old = "0";
	public String parts_amtmonth_old = "0";
	public String labour_amtmonth_old = "0";
	// Quarter
	public String veh_countquarter_old = "0";
	public String jc_countquarter_old = "0";
	public String ticket_countquarter_old = "0";
	public String service_booking_countquarter_old = "0";
	public String parts_amtquarter_old = "0";
	public String labour_amtquarter_old = "0";
	
	// refresh All AJAX
	public String refreshAll = "", cards = "";
	// for redirect
	public String filter = "";
	public String timefilter = "";
	public String opt = "";
	String period = "";
	
	public String ticketescstatus = "";
	public String jcpriorityescstatus = "";
	public String servicefollowupescstatus = "";
	public String jcpsfescstatus = "";
	public String openjobcards = "";
	public String opentickets = "";
	DecimalFormat df = new DecimalFormat("0.00");
	
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_call_access, emp_service_jobcard_access, emp_ticket_access, emp_service_vehicle_access, emp_service_insurance_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				refreshAll = PadQuotes(request.getParameter("refreshAll"));
				cards = PadQuotes(request.getParameter("cards"));
				ticketescstatus = PadQuotes(request.getParameter("TicketEscStatus"));
				jcpriorityescstatus = PadQuotes(request.getParameter("JCPriorityEscStatus"));
				servicefollowupescstatus = PadQuotes(request.getParameter("ServiceFollowupEscStatus"));
				jcpsfescstatus = PadQuotes(request.getParameter("JcPSFEscStatus"));
				openjobcards = PadQuotes(request.getParameter("openjobcards"));
				opentickets = PadQuotes(request.getParameter("opentickets"));
				timefilter = PadQuotes(request.getParameter("timefilter"));
				filter_brand_id = CleanArrVal(PadQuotes(request.getParameter("brand_id")));
				filter_region_id = CleanArrVal(PadQuotes(request.getParameter("region_id")));
				filter_branch_id = CleanArrVal(PadQuotes(request.getParameter("dr_branch_id")));
				// for redirect
				filter = PadQuotes(request.getParameter("filter"));
				if (filter.equals("yes")) {
					opt = PadQuotes(request.getParameter("opt"));
					populateconfigdetails();
					CheckRedirect(opt, request, response);
				}
				// for redirect
				if (smart == null) {
					smart = "";
				}
				
				if (msg.equals("") && refreshAll.equals("")) {
					
					if (smart.equals("yes")) {
					} else {
						SetSession("ticketstrsql", StrSearch, request);
					}
				} else if (msg.equals("")) {
					
					if (refreshAll.equals("yes") && ticketescstatus.equals("yes")) {
						StrHTML = TicketEscStatus();
					}
					if (refreshAll.equals("yes") && jcpriorityescstatus.equals("yes")) {
						StrHTML = JCPriorityEscStatus();
					}
					if (refreshAll.equals("yes") && servicefollowupescstatus.equals("yes")) {
						StrHTML = ServiceFollowupEscStatus();
					}
					if (refreshAll.equals("yes") && jcpsfescstatus.equals("yes")) {
						StrHTML = JcPSFEscStatus();
					}
					if (refreshAll.equals("yes") && openjobcards.equals("yes")) {
						populateconfigdetails();
						StrHTML = JCsOpen(comp_id);
					}
					if (refreshAll.equals("yes") && opentickets.equals("yes")) {
						populateconfigdetails();
						StrHTML = TicketsOpen(comp_id);
					}
					if (refreshAll.equals("yes") && cards.equals("yes")) {
						populateconfigdetails();
						StrHTML = ServiceVehDetails(comp_id);
					}
					
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String TicketEscStatus() {
		try {
			
			StrSql = "SELECT"
					+ " COALESCE(SUM(if(ticket_trigger=1,1,0)),0) AS trigger1,"
					+ " COALESCE(SUM(if(ticket_trigger=2,1,0)),0) AS trigger2,"
					+ " COALESCE(SUM(if(ticket_trigger=3,1,0)),0) AS trigger3,"
					+ " COALESCE(SUM(if(ticket_trigger=4,1,0)),0) AS trigger4,"
					+ " COALESCE(SUM(if(ticket_trigger=5,1,0)),0) AS trigger5"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " WHERE 1 = 1"
					+ " AND ticket_trigger > 0"
					+ " AND ticket_ticketstatus_id != 3"
					+ ExeAccess.replace("emp_id", "ticket_emp_id");
			
			// SOP("StrSql=TicketEscStatus=" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			
			ticketchart_data = "[";
			
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ticketchart_data = ticketchart_data + "{\"level\": \"Level " + 5 + "\", \"value\":" + crs.getString("trigger5")
							+ ", \"color\":\"#FF0033\", \"url\":\"report-ticket-esc-status.jsp\"},";
					ticketchart_data = ticketchart_data + "{\"level\": \"Level " + 4 + "\", \"value\":" + crs.getString("trigger4")
							+ ", \"color\":\"#FF8030\", \"url\":\"report-ticket-esc-status.jsp\"},";
					ticketchart_data = ticketchart_data + "{\"level\": \"Level " + 3 + "\", \"value\":" + crs.getString("trigger3")
							+ ", \"color\":\"#7ba5de\", \"url\":\"report-ticket-esc-status.jsp\"},";
					ticketchart_data = ticketchart_data + "{\"level\": \"Level " + 2 + "\", \"value\":" + crs.getString("trigger2")
							+ ", \"color\":\"#F8FF01\", \"url\":\"report-ticket-esc-status.jsp\"},";
					ticketchart_data = ticketchart_data + "{\"level\": \"Level " + 1 + "\", \"value\":" + crs.getString("trigger1")
							+ ", \"color\":\"#c3a7e2\", \"url\":\"report-ticket-esc-status.jsp\"}";
				}
				ticketchart_data += "]";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return ticketchart_data.toString();
	}
	
	public String ServiceFollowupEscStatus() {
		try {
			
			StrSql = "SELECT"
					+ " COALESCE(SUM(if(vehfollowup_trigger=1,1,0)),0) AS trigger1,"
					+ " COALESCE(SUM(if(vehfollowup_trigger=2,1,0)),0) AS trigger2,"
					+ " COALESCE(SUM(if(vehfollowup_trigger=3,1,0)),0) AS trigger3,"
					+ " COALESCE(SUM(if(vehfollowup_trigger=4,1,0)),0) AS trigger4,"
					+ " COALESCE(SUM(if(vehfollowup_trigger=5,1,0)),0) AS trigger5"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id";
			
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id";
			}
			
			StrSql += " WHERE 1 = 1"
					+ " AND vehfollowup_desc = ''"
					+ " AND vehfollowup_followup_time <= '" + ToLongDate(kknow()) + "'"
					+ " AND vehfollowup_trigger > 0";
			
			StrSql += BranchAccess.replace("branch_id", "veh_branch_id")
					+ ExeAccess.replace("emp_id", "vehfollowup_emp_id");
			
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			// SOP("StrSql-----ServiceFollowupEscStatus-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			servicefollowupescchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					
					servicefollowupescchart_data += "{\"level\": \"Level " + 5 + "\", \"value\":" + crs.getString("trigger5")
							+ ", \"color\":\"#FF0033\", \"url\":\"report-servicefollowup-esc-status.jsp\"},";
					servicefollowupescchart_data += "{\"level\": \"Level " + 4 + "\", \"value\":" + crs.getString("trigger4")
							+ ", \"color\":\"#FF8030\" , \"url\":\"report-servicefollowup-esc-status.jsp\"},";
					servicefollowupescchart_data += "{\"level\": \"Level " + 3 + "\", \"value\":" + crs.getString("trigger3")
							+ ", \"color\":\"#7ba5de\", \"url\":\"report-servicefollowup-esc-status.jsp\"},";
					servicefollowupescchart_data += "{\"level\": \"Level " + 2 + "\", \"value\":" + crs.getString("trigger2")
							+ ", \"color\":\"#F8FF01\", \"url\":\"report-servicefollowup-esc-status.jsp\"},";
					servicefollowupescchart_data += "{\"level\": \"Level " + 1 + "\", \"value\":" + crs.getString("trigger1")
							+ ", \"color\":\"#c3a7e2\", \"url\":\"report-servicefollowup-esc-status.jsp\"}";
				}
				servicefollowupescchart_data += "]";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return servicefollowupescchart_data.toString();
	}
	
	public String JCPriorityEscStatus() {
		try {
			
			StrSql = "SELECT"
					+ " COALESCE(SUM(if(jc_priority_trigger=1,1,0)),0) AS trigger1,"
					+ " COALESCE(SUM(if(jc_priority_trigger=2,1,0)),0) AS trigger2,"
					+ " COALESCE(SUM(if(jc_priority_trigger=3,1,0)),0) AS trigger3,"
					+ " COALESCE(SUM(if(jc_priority_trigger=4,1,0)),0) AS trigger4,"
					+ " COALESCE(SUM(if(jc_priority_trigger=5,1,0)),0) AS trigger5"
					+ " FROM " + compdb(comp_id) + "axela_service_jc";
			
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = jc_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ " AND	jc_time_ready = ''"
					+ " AND jc_time_promised < " + ToLongDate(kknow())
					+ " AND jc_active = 1";
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id");
			
			// SOP("StrSql=====JCPriorityEscStatus===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			jcprioritychart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					
					jcprioritychart_data += "{\"level\": \"Level " + 5 + "\", \"value\":" + crs.getString("trigger5")
							+ ", \"color\":\"#FF0033\", \"url\":\"report-jobcard-priority-esc-status.jsp\"},";
					jcprioritychart_data += "{\"level\": \"Level " + 4 + "\", \"value\":" + crs.getString("trigger4")
							+ ", \"color\":\"#FF8030\", \"url\":\"report-jobcard-priority-esc-status.jsp\"},";
					jcprioritychart_data += "{\"level\": \"Level " + 3 + "\", \"value\":" + crs.getString("trigger3")
							+ ", \"color\":\"#7ba5de\", \"url\":\"report-jobcard-priority-esc-status.jsp\"},";
					jcprioritychart_data += "{\"level\": \"Level " + 2 + "\", \"value\":" + crs.getString("trigger2")
							+ ", \"color\":\"#F8FF01\", \"url\":\"report-jobcard-priority-esc-status.jsp\"},";
					jcprioritychart_data += "{\"level\": \"Level " + 1 + "\", \"value\":" + crs.getString("trigger1")
							+ ", \"color\":\"#c3a7e2\", \"url\":\"report-jobcard-priority-esc-status.jsp\"}";
					
				}
				jcprioritychart_data += "]";
			}
			// SOP("jcprioritychart_data = " + jcprioritychart_data);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return jcprioritychart_data.toString();
	}
	
	public String JcPSFEscStatus() {
		try {
			
			StrSql = "SELECT"
					+ " COALESCE(SUM(if(jcpsf_trigger=1,1,0)),0) AS trigger1,"
					+ " COALESCE(SUM(if(jcpsf_trigger=2,1,0)),0) AS trigger2,"
					+ " COALESCE(SUM(if(jcpsf_trigger=3,1,0)),0) AS trigger3,"
					+ " COALESCE(SUM(if(jcpsf_trigger=4,1,0)),0) AS trigger4,"
					+ " COALESCE(SUM(if(jcpsf_trigger=5,1,0)),0) AS trigger5"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc on jc_id = jcpsf_jc_id";
			
			if (!filter_branch_id.equals("") || !filter_brand_id.equals("") || !filter_region_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = jc_branch_id";
			}
			
			StrSql += " WHERE 1 = 1"
					+ " AND jcpsf_psffeedbacktype_id = 0"
					+ " AND jcpsf_trigger > 0"
					+ " AND jc_active = 1";
			
			StrSql += BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jcpsf_emp_id");
			
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			
			// SOP("StrSql==---JcPSFEscStatus---" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			psfchart_data = "[";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					
					psfchart_data += "{\"level\": \"Level " + 5 + "\", \"value\":" + crs.getString("trigger5")
							+ ", \"color\":\"#FF0033\", \"url\":\"report-jobcard-psf-esc-status.jsp\"},";
					psfchart_data += "{\"level\": \"Level " + 4 + "\", \"value\":" + crs.getString("trigger4")
							+ ", \"color\":\"#FF8030\", \"url\":\"report-jobcard-psf-esc-status.jsp\"},";
					psfchart_data += "{\"level\": \"Level " + 3 + "\", \"value\":" + crs.getString("trigger3")
							+ ", \"color\":\"#7ba5de\", \"url\":\"report-jobcard-psf-esc-status.jsp\"},";
					psfchart_data += "{\"level\": \"Level " + 2 + "\", \"value\":" + crs.getString("trigger2")
							+ ", \"color\":\"#F8FF01\", \"url\":\"report-jobcard-psf-esc-status.jsp\"},";
					psfchart_data += "{\"level\": \"Level " + 1 + "\", \"value\":" + crs.getString("trigger1")
							+ ", \"color\":\"#c3a7e2\", \"url\":\"report-jobcard-psf-esc-status.jsp\"}";
					
				}
				psfchart_data += "]";
			}
			// SOP("chart_data = " + chart_data);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return psfchart_data.toString();
	}
	
	public String TicketsOpen(String comp_id) {
		try {
			int totalticketcount = 0;
			StringBuilder Str = new StringBuilder();
			
			StrSql = "SELECT emp_id, concat(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " COUNT(ticket_id) as ticketcount "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id";
			
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id";
			}
			StrSql += " WHERE 1=1"
					+ " AND ticket_ticketstatus_id != 3";
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN( " + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN( " + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_brand_id IN( " + filter_branch_id + ")";
			}
			StrSql += BranchAccess.replace("branch_id", "ticket_branch_id")
					+ ExeAccess;
			if (timefilter.equals("month")) {
				StrSql += " AND SUBSTR(ticket_entry_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
			} else if (timefilter.equals("quarter")) {
				StrSql += " AND SUBSTR(ticket_entry_date,1,6) >= " + startquarter
						+ " AND SUBSTR(ticket_entry_date,1,6) <= " + endquarter;
			} else {
				StrSql += " AND SUBSTR(ticket_entry_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)";
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql=22===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				while (crs.next()) {
					totalticketcount = totalticketcount + crs.getInt("ticketcount");
					Str.append("<tr align=center>\n");
					Str.append("<td align=left><a href=\"../service/ticket-list.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td align=right width=10%>").append(crs.getString("ticketcount")).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(totalticketcount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</table>\n");
			} else {
				Str.append("<center><div>No Open Tickets!</center></div>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String JCsOpen(String comp_id) {
		try {
			int totalinsurcount = 0;
			StringBuilder Str = new StringBuilder();
			
			StrSql = "SELECT emp_id,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,"
					+ " COUNT(jc_id) as jccount "
					+ " FROM " + compdb(comp_id) + "axela_service_jc";
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id";
			
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id";
			}
			
			StrSql += " WHERE 1=1"
					+ " AND jc_time_ready = '' "
					+ ExeAccess
					+ BranchAccess.replace("branch_id", "jc_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN( " + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN( " + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_brand_id IN( " + filter_branch_id + ")";
			}
			
			if (timefilter.equals("month")) {
				StrSql += " AND SUBSTR(jc_time_in,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
			} else if (timefilter.equals("quarter")) {
				StrSql += " AND SUBSTR(jc_time_in,1,6) >= " + startquarter
						+ " AND SUBSTR(jc_time_in,1,6) <= " + endquarter;
			} else {
				StrSql += " AND SUBSTR(jc_time_in,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)";
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				while (crs.next()) {
					totalinsurcount = totalinsurcount + crs.getInt("jccount");
					Str.append("<tr align=center>\n");
					Str.append("<td align=left><a href=\"../service/jobcard-list.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td align=right width=10%>").append(crs.getString("jccount")).append("</td>\n");
					Str.append("</tr>");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td align=right><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(totalinsurcount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</table>\n");
			} else {
				Str.append("<center><div>No Open Job Cards!</div></center>");
			}
			
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	
	public String ListReports() {
		StringBuilder Str = new StringBuilder();
		
		StrSql = "SELECT report_id, report_name, report_url"
				+ " FROM " + maindb() + "module_report"
				+ " INNER JOIN " + maindb() + "module ON module_id = report_module_id"
				+ " WHERE report_module_id = 6 AND report_moduledisplay = 1"
				+ " AND report_active = 1"
				+ " ORDER BY report_rank";
		// SOP("StrSql===" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\"  \">\n");
				Str.append("<table class=\"table  \" data-filter=\"#filter\">\n");
				while (crs.next()) {
					Str.append("<tr>");
					Str.append("<td><a href=").append(crs.getString("report_url")).append(" target=_blank >").append(crs.getString("report_name")).append("</a></td>");
					Str.append("</tr>");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<b><font color=red><b>No Reports found!</b></font>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	
	public String ServiceVehDetails(String comp_id) {
		String new_value = "", old_value = "";
		try {
			CachedRowSet crs = null;
			
			StrSql = "SELECT"
					+ " COALESCE(sum(if(SUBSTR(veh_entry_date, 1, 8) = " + curr_date + ", 1, 0)),0) AS vehcountday,"
					+ " COALESCE(sum(if(SUBSTR(veh_entry_date, 1, 6) = " + curr_month + ", 1, 0)),0) AS vehcountmonth,"
					+ " COALESCE(sum(if(SUBSTR(veh_entry_date, 1, 6) >= " + startquarter + " AND SUBSTR(veh_entry_date, 1, 6) <= " + endquarter + ", 1, 0)),0) AS vehcountquarter,"
					+ " COALESCE(sum(if(SUBSTR(veh_entry_date, 1, 8) = " + back_date + ", 1, 0)),0) AS vehcountdayold,"
					+ " COALESCE(sum(if(SUBSTR(veh_entry_date, 1, 6) = " + back_month + ", 1, 0)),0) AS vehcountmonthold,"
					+ " COALESCE(sum(if(SUBSTR(veh_entry_date, 1, 6) >= " + back_startquarter + " AND SUBSTR(veh_entry_date, 1, 6) <= " + back_endquarter + ", 1, 0)),0) AS vehcountquarterold,"
					+ " "
					+ " COALESCE(service_jc.jccountday,0) AS jccountday,"
					+ " COALESCE(service_jc.jccountmonth,0) AS jccountmonth,"
					+ " COALESCE(service_jc.jccountquarter,0) AS jccountquarter,"
					+ " COALESCE(service_jc.jccountdayold,0) AS jccountdayold,"
					+ " COALESCE(service_jc.jccountmonthold,0) AS jccountmonthold,"
					+ " COALESCE(service_jc.jccountquarterold,0) AS jccountquarterold,"
					
					+ " COALESCE(service_jc.partsamtday,0) AS partsamtday,"
					+ " COALESCE(service_jc.partsamtmonth,0) AS partsamtmonth,"
					+ " COALESCE(service_jc.partsquater,0) AS partsamtquarter,"
					+ " COALESCE(service_jc.partsamtdayold,0) AS partsamtdayold,"
					+ " COALESCE(service_jc.partsamtmonthold,0) AS partsamtmonthold,"
					+ " COALESCE(service_jc.partsamtquaterold,0) AS partsamtquaterold,"
					+ " "
					+ " COALESCE(service_jc.labouramtday,0) AS labouramtday,"
					+ " COALESCE(service_jc.labouramtmonth,0) AS labouramtmonth,"
					+ " COALESCE(service_jc.labouramtquarter,0) AS labouramtquarter,"
					+ " COALESCE(service_jc.labouramtdayold,0) AS labouramtdayold,"
					+ " COALESCE(service_jc.labouramtmonthold,0) AS labouramtmonthold,"
					+ " COALESCE(service_jc.labouramtquarterold,0) AS labouramtquarterold,"
					+ " "
					+ " COALESCE(tblticket.ticketcountday,0) AS ticketcountday,"
					+ " COALESCE(tblticket.ticketcountmonth,0) AS ticketcountmonth,"
					+ " COALESCE(tblticket.ticketcountquarter,0) AS ticketcountquarter,"
					+ " COALESCE(tblticket.ticketcountdayold,0) AS ticketcountdayold,"
					+ " COALESCE(tblticket.ticketcountmonthold,0) AS ticketcountmonthold,"
					+ " COALESCE(tblticket.ticketcountquarterold,0) AS ticketcountquarterold,"
					+ " "
					+ " COALESCE(service_booking.servicebookingday,0) AS servicecountday,"
					+ " COALESCE(service_booking.servicebookingmonth,0) AS servicecountmonth,"
					+ " COALESCE(service_booking.servicebookingquarter,0) AS servicecountquarter,"
					+ " COALESCE(service_booking.servicebookingdayold,0) AS servicecountdayold,"
					+ " COALESCE(service_booking.servicebookingmonthold,0) AS servicecountmonthold,"
					+ " COALESCE(service_booking.servicebookingquarterold,0) AS servicecountquarterold"
					+ " "
					+ " FROM " + compdb(comp_id) + "axela_service_veh";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = veh_branch_id";
			}
			
			StrSql += " ,("
					+ " SELECT"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) = "
					+ curr_date
					+ ", 1, 0)) AS jccountday,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) = "
					+ curr_month
					+ ", 1, 0)) AS jccountmonth,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) >= "
					+ startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= "
					+ endquarter
					+ ", 1, 0)) AS jccountquarter,"
					+ " "
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) = "
					+ back_date
					+ ", 1, 0)) AS jccountdayold,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) = "
					+ back_month
					+ ", 1, 0)) AS jccountmonthold,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) >= "
					+ back_startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= "
					+ back_endquarter
					+ ", 1, 0))"
					+ " AS jccountquarterold,"
					
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) = "
					+ curr_date
					+ ", (jc_bill_cash_parts_tyre + jc_bill_cash_parts_oil + jc_bill_cash_parts_battery + jc_bill_cash_parts_brake + jc_bill_cash_parts_accessories "
					+ " + jc_bill_cash_parts + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_extwarranty + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng), 0))"
					+ " AS partsamtday,"
					
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) = "
					+ curr_month
					+ ",(jc_bill_cash_parts_tyre + jc_bill_cash_parts_oil + jc_bill_cash_parts_battery + jc_bill_cash_parts_brake + jc_bill_cash_parts_accessories"
					+ " + jc_bill_cash_parts + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_extwarranty + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng), 0) )"
					+ " AS partsamtmonth,"
					
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) >= "
					+ startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= "
					+ endquarter
					+ ",(jc_bill_cash_parts_tyre + jc_bill_cash_parts_oil + jc_bill_cash_parts_battery + jc_bill_cash_parts_brake + jc_bill_cash_parts_accessories"
					+ " + jc_bill_cash_parts + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_extwarranty + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng), 0)) "
					+ " AS partsquater,"
					
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) = "
					+ back_date
					+ ", (jc_bill_cash_parts_tyre + jc_bill_cash_parts_oil + jc_bill_cash_parts_battery + jc_bill_cash_parts_brake + jc_bill_cash_parts_accessories"
					+ " + jc_bill_cash_parts + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_extwarranty + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng), 0))"
					+ " AS partsamtdayold,"
					
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) = "
					+ back_month
					+ ", (jc_bill_cash_parts_tyre + jc_bill_cash_parts_oil + jc_bill_cash_parts_battery + jc_bill_cash_parts_brake + jc_bill_cash_parts_accessories"
					+ " + jc_bill_cash_parts + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_extwarranty + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng), 0))"
					+ " AS partsamtmonthold,"
					
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) >= "
					+ back_startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= "
					+ back_endquarter
					+ ", (jc_bill_cash_parts_tyre + jc_bill_cash_parts_oil + jc_bill_cash_parts_battery + jc_bill_cash_parts_brake + jc_bill_cash_parts_accessories"
					+ " + jc_bill_cash_parts + jc_bill_cash_parts_valueadd + jc_bill_cash_parts_extwarranty + jc_bill_cash_parts_wheelalign + jc_bill_cash_parts_cng), 0))"
					+ " AS partsamtquaterold,"
					+ " "
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) = "
					+ curr_date
					+ ", jc_bill_cash_labour + jc_bill_cash_labour_tyre + jc_bill_cash_labour_oil + jc_bill_cash_labour_battery + jc_bill_cash_labour_brake + jc_bill_cash_labour_accessories + jc_bill_cash_labour_valueadd + jc_bill_cash_labour_extwarranty + jc_bill_cash_labour_wheelalign + jc_bill_cash_labour_cng, 0)) AS labouramtday,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) = "
					+ curr_month
					+ ", jc_bill_cash_labour + jc_bill_cash_labour_tyre + jc_bill_cash_labour_oil + jc_bill_cash_labour_battery + jc_bill_cash_labour_brake + jc_bill_cash_labour_accessories + jc_bill_cash_labour_valueadd + jc_bill_cash_labour_extwarranty + jc_bill_cash_labour_wheelalign + jc_bill_cash_labour_cng, 0)) AS labouramtmonth,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) >= "
					+ startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= "
					+ endquarter
					+ ", jc_bill_cash_labour + jc_bill_cash_labour_tyre + jc_bill_cash_labour_oil + jc_bill_cash_labour_battery + jc_bill_cash_labour_brake + jc_bill_cash_labour_accessories + jc_bill_cash_labour_valueadd + jc_bill_cash_labour_extwarranty + jc_bill_cash_labour_wheelalign + jc_bill_cash_labour_cng, 0)) "
					+ " AS labouramtquarter,"
					+ " "
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 8) = "
					+ back_date
					+ ", jc_bill_cash_labour + jc_bill_cash_labour_tyre + jc_bill_cash_labour_oil + jc_bill_cash_labour_battery + jc_bill_cash_labour_brake + jc_bill_cash_labour_accessories + jc_bill_cash_labour_valueadd + jc_bill_cash_labour_extwarranty + jc_bill_cash_labour_wheelalign + jc_bill_cash_labour_cng, 0)) AS labouramtdayold,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) = "
					+ back_month
					+ ", jc_bill_cash_labour + jc_bill_cash_labour_tyre + jc_bill_cash_labour_oil + jc_bill_cash_labour_battery + jc_bill_cash_labour_brake + jc_bill_cash_labour_accessories + jc_bill_cash_labour_valueadd + jc_bill_cash_labour_extwarranty + jc_bill_cash_labour_wheelalign + jc_bill_cash_labour_cng, 0)) AS labouramtmonthold,"
					+ " sum(if(SUBSTR(jc_bill_cash_date, 1, 6) >= "
					+ back_startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= "
					+ back_endquarter
					+ ", jc_bill_cash_labour + jc_bill_cash_labour_tyre + jc_bill_cash_labour_oil + jc_bill_cash_labour_battery + jc_bill_cash_labour_brake + jc_bill_cash_labour_accessories + jc_bill_cash_labour_valueadd + jc_bill_cash_labour_extwarranty + jc_bill_cash_labour_wheelalign + jc_bill_cash_labour_cng, 0))"
					+ " AS labouramtquarterold"
					+ " FROM " + compdb(comp_id) + "axela_service_jc";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "jc_emp_id")
					+ BranchAccess.replace("branch_id", "jc_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) AS service_jc,"
					+ " ("
					+ " SELECT"
					+ " sum(if(SUBSTR(ticket_entry_date, 1, 8) = " + curr_date + ", 1, 0)) AS ticketcountday,"
					+ " sum(if(SUBSTR(ticket_entry_date, 1, 6) = " + curr_month + ", 1, 0)) AS ticketcountmonth,"
					+ " sum(if(SUBSTR(ticket_entry_date, 1, 6) >= " + startquarter + " AND SUBSTR(ticket_entry_date, 1, 6) <= " + endquarter + " , 1, 0)) AS ticketcountquarter,"
					+ " "
					+ " sum(if(SUBSTR(ticket_entry_date, 1, 8) = " + back_date + ", 1, 0)) AS ticketcountdayold,"
					+ " sum(if(SUBSTR(ticket_entry_date, 1, 6) = " + back_month + ", 1, 0)) AS ticketcountmonthold,"
					+ " sum(if(SUBSTR(ticket_entry_date, 1, 6) >= " + back_startquarter + " AND SUBSTR(ticket_entry_date, 1, 6) <= " + back_endquarter + " , 1, 0)) AS ticketcountquarterold"
					+ " FROM " + compdb(comp_id) + "axela_service_ticket";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = ticket_branch_id";
			}
			StrSql += " WHERE 1 = 1"
					+ ExeAccess.replace("emp_id", "ticket_emp_id")
					+ BranchAccess.replace("branch_id", "ticket_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) AS tblticket,"
					+ " ("
					+ " SELECT"
					+ " SUM(IF (vehfollowup_vehaction_id = 1 AND (SUBSTR(vehfollowup_entry_time, 1, 8) = " + curr_date + "), 1, 0))  AS servicebookingday,"
					+ " SUM(IF(vehfollowup_vehaction_id = 1 AND(SUBSTR(vehfollowup_entry_time, 1, 6) = " + curr_month + "), 1, 0)) AS servicebookingmonth,"
					+ " SUM(IF (vehfollowup_vehaction_id = 1 AND (SUBSTR(vehfollowup_entry_time, 1, 6) >= " + startquarter
					+ " AND SUBSTR(vehfollowup_entry_time, 1, 6) <= " + endquarter + "), 1, 0)) AS servicebookingquarter,"
					+ " "
					+ " SUM(IF (vehfollowup_vehaction_id = 1 AND (SUBSTR(vehfollowup_entry_time, 1, 8) = " + back_date + "), 1, 0)) AS servicebookingdayold,"
					+ " SUM(IF(vehfollowup_vehaction_id = 1 AND(SUBSTR(vehfollowup_entry_time, 1, 6) = " + back_month + "), 1, 0))  AS servicebookingmonthold,"
					+ " SUM(IF (vehfollowup_vehaction_id = 1 AND (SUBSTR(vehfollowup_entry_time, 1, 6) >= " + back_startquarter
					+ " AND SUBSTR(vehfollowup_entry_time, 1, 6) <= " + back_endquarter + "), 1, 0)) AS servicebookingquarterold"
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id";
			if (!filter_brand_id.equals("") || !filter_region_id.equals("") || !filter_branch_id.equals("")) {
				StrSql += "  INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id";
			}
			StrSql += " WHERE 1=1"
					+ ExeAccess.replace("emp_id", "veh_emp_id")
					+ BranchAccess.replace("branch_id", "veh_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			StrSql += " ) AS service_booking"
					+ " where 1=1"
					+ ExeAccess.replace("emp_id", "veh_emp_id")
					+ BranchAccess.replace("branch_id", "veh_branch_id");
			if (!filter_brand_id.equals("")) {
				StrSql += " AND branch_brand_id IN (" + filter_brand_id + ")";
			}
			if (!filter_region_id.equals("")) {
				StrSql += " AND branch_region_id IN (" + filter_region_id + ")";
			}
			if (!filter_branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + filter_branch_id + ")";
			}
			
			// SOPInfo("StrSql==cart==" + StrSql);
			
			crs = processQuery(StrSql);
			while (crs.next()) {
				
				veh_countday = crs.getString("vehcountday");
				veh_countmonth = crs.getString("vehcountmonth");
				veh_countquarter = crs.getString("vehcountquarter");
				
				jc_countday = crs.getString("jccountday");
				jc_countmonth = crs.getString("jccountmonth");
				jc_countquarter = crs.getString("jccountquarter");
				
				ticket_countday = crs.getString("ticketcountday");
				ticket_countmonth = crs.getString("ticketcountmonth");
				ticket_countquarter = crs.getString("ticketcountquarter");
				
				service_booking_countday = crs.getString("servicecountday");
				service_booking_countmonth = crs.getString("servicecountmonth");
				service_booking_countquarter = crs.getString("servicecountquarter");
				
				parts_amtday = (int) crs.getDouble("partsamtday") + "";
				parts_amtmonth = (int) crs.getDouble("partsamtmonth") + "";
				parts_amtquarter = (int) crs.getDouble("partsamtquarter") + "";
				
				labour_amtday = (int) crs.getDouble("labouramtday") + "";
				labour_amtmonth = (int) crs.getDouble("labouramtmonth") + "";
				labour_amtquarter = (int) crs.getDouble("labouramtquarter") + "";
				
				new_value = veh_countday + "," + veh_countmonth + "," + veh_countquarter + ","
						+ jc_countday + "," + jc_countmonth + "," + jc_countquarter + ","
						+ ticket_countday + "," + ticket_countmonth + "," + ticket_countquarter + ","
						+ service_booking_countday + "," + service_booking_countmonth + "," + service_booking_countquarter + ","
						+ parts_amtday + "," + parts_amtmonth + "," + parts_amtquarter + ","
						+ labour_amtday + "," + labour_amtmonth + "," + labour_amtquarter;
				// SOPInfo("new_value==" + new_value);
				// =====================================================================================================
				
				veh_countday_old = crs.getString("vehcountdayold");
				veh_countmonth_old = crs.getString("vehcountmonthold");
				veh_countquarter_old = crs.getString("vehcountquarterold");
				
				jc_countday_old = crs.getString("jccountdayold");
				jc_countmonth_old = crs.getString("jccountmonthold");
				jc_countquarter_old = crs.getString("jccountquarterold");
				
				ticket_countday_old = crs.getString("ticketcountdayold");
				ticket_countmonth_old = crs.getString("ticketcountmonthold");
				ticket_countquarter_old = crs.getString("ticketcountquarterold");
				
				service_booking_countday_old = crs.getString("servicecountdayold");
				service_booking_countmonth_old = crs.getString("servicecountmonthold");
				service_booking_countquarter_old = crs.getString("servicecountquarterold");
				
				parts_amtday_old = (int) crs.getDouble("partsamtdayold") + "";
				parts_amtmonth_old = (int) crs.getDouble("partsamtmonthold") + "";
				parts_amtquarter_old = (int) crs.getDouble("partsamtquaterold") + "";
				
				labour_amtday_old = (int) crs.getDouble("labouramtdayold") + "";
				
				labour_amtmonth_old = (int) crs.getDouble("labouramtmonthold") + "";
				
				labour_amtquarter_old = (int) crs.getDouble("labouramtquarterold") + "";
				
				old_value = veh_countday_old + "," + veh_countmonth_old + "," + veh_countquarter_old + ","
						+ jc_countday_old + "," + jc_countmonth_old + "," + jc_countquarter_old + ","
						+ ticket_countday_old + "," + ticket_countmonth_old + "," + ticket_countquarter_old + ","
						+ service_booking_countday_old + "," + service_booking_countmonth_old + "," + service_booking_countquarter_old + ","
						+ parts_amtday_old + "," + parts_amtmonth_old + "," + parts_amtquarter_old + ","
						+ labour_amtday_old + "," + labour_amtmonth_old + "," + labour_amtquarter_old;
				// SOPInfo("old_value==" + old_value);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new_value + ":" + old_value;
	}
	
	public void populateconfigdetails() {
		
		curr_date = curr_date.substring(0, 8);
		curr_month = ToShortDate(kknow()).substring(0, 6);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		back_month = ToLongDate(cal.getTime()).substring(0, 6);
		cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		back_date = ToLongDate(cal.getTime()).substring(0, 8);
		
		if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 1 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 3) {
			
			startquarter = ToShortDate(kknow()).substring(0, 4) + "01";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "03";
			
			cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);
			
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "10";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "12";
			
		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 4 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 6) {
			
			startquarter = ToShortDate(kknow()).substring(0, 4) + "04";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "06";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "01";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "03";
			
		} else if (Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) >= 7 && Integer.parseInt(ToShortDate(kknow()).substring(4, 6)) <= 9) {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "07";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "09";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "04";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "06";
		} else {
			startquarter = ToShortDate(kknow()).substring(0, 4) + "10";
			endquarter = ToShortDate(kknow()).substring(0, 4) + "12";
			back_startquarter = ToShortDate(cal.getTime()).substring(0, 4) + "07";
			back_endquarter = ToShortDate(cal.getTime()).substring(0, 4) + "09";
		}
		
	}
	
	protected void CheckRedirect(String opt, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		period = PadQuotes(request.getParameter("period"));
		filter_brand_id = CleanArrVal(PadQuotes(request.getParameter("brand_id")));
		filter_region_id = CleanArrVal(PadQuotes(request.getParameter("region_id")));
		filter_branch_id = CleanArrVal(PadQuotes(request.getParameter("dr_branch_id")));
		
		if (!filter_brand_id.equals("")) {
			StrSearch = " AND branch_brand_id IN (" + filter_brand_id + ")";
		}
		if (!filter_region_id.equals("")) {
			StrSearch += " AND branch_region_id IN (" + filter_region_id + ")";
		}
		if (!filter_branch_id.equals("")) {
			StrSearch += " AND branch_id IN (" + filter_branch_id + ")";
		}
		
		// Vehicles
		if (opt.equals("vehicles") && period.equals("today")) {
			StrSearch += " AND SUBSTR(veh_entry_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("vehicles") && period.equals("month")) {
			StrSearch += " AND SUBSTR(veh_entry_date, 1, 6) = " + curr_month;
		} else if (opt.equals("vehicles") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(veh_entry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(veh_entry_date, 1, 6) <= " + endquarter;
		}
		
		// jobcard
		if (opt.equals("jobcard") && period.equals("today")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("jobcard") && period.equals("month")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 6) = " + curr_month;
		} else if (opt.equals("jobcard") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= " + endquarter;
		}
		
		// tickets
		if (opt.equals("tickets") && period.equals("today")) {
			StrSearch += " AND SUBSTR(ticket_entry_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("tickets") && period.equals("month")) {
			StrSearch += " AND SUBSTR(ticket_entry_date, 1, 6) = " + curr_month;
		} else if (opt.equals("tickets") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(ticket_entry_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(ticket_entry_date, 1, 6) <= " + endquarter;
		}
		
		// booking
		if (opt.equals("booking") && period.equals("today")) {
			StrSearch += " AND veh_id IN (SELECT vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE vehfollowup_vehaction_id = 1"
					+ " AND SUBSTR(vehfollowup_entry_time, 1, 8) = SUBSTR('" + curr_date + "', 1, 8))";
		} else if (opt.equals("booking") && period.equals("month")) {
			StrSearch += " AND veh_id IN  (SELECT vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE vehfollowup_vehaction_id = 1"
					+ " AND SUBSTR(vehfollowup_entry_time, 1, 6) = " + curr_month + ")";
		} else if (opt.equals("booking") && period.equals("quarter")) {
			StrSearch += " AND veh_id IN  (SELECT vehfollowup_veh_id "
					+ " FROM " + compdb(comp_id) + "axela_service_followup"
					+ " WHERE vehfollowup_vehaction_id = 1"
					+ " AND SUBSTR(vehfollowup_entry_time, 1, 6) >= " + startquarter
					+ " AND SUBSTR(vehfollowup_entry_time, 1, 6) <= " + endquarter + ")";
		}
		
		// partsamount
		if (opt.equals("partsamount") && period.equals("today")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("partsamount") && period.equals("month")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 6) = " + curr_month;
		} else if (opt.equals("partsamount") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= " + endquarter;
		}
		
		// labouramount
		if (opt.equals("labouramount") && period.equals("today")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 8) = SUBSTR('" + curr_date + "', 1, 8)";
		} else if (opt.equals("labouramount") && period.equals("month")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 6) = " + curr_month;
		} else if (opt.equals("labouramount") && period.equals("quarter")) {
			StrSearch += " AND SUBSTR(jc_bill_cash_date, 1, 6) >= " + startquarter
					+ " AND SUBSTR(jc_bill_cash_date, 1, 6) <= " + endquarter;
		}
		
		// if (opt.equals("booking")) {
		// StrSearch += " AND ticket_veh_id != 0 ";
		// }
		// else if (opt.equals("delivered")) {
		// StrSearch += " AND so_delivered_date != '' ";
		// } else if (opt.equals("cancelled")) {
		// StrSearch += " AND so_active = 0"
		// + " AND so_cancel_date != '' ";
		// } else if (opt.equals("retail")) {
		// StrSearch += " AND so_active = 1"
		// + " AND so_retail_date != '' ";
		// }
		// SOP("StrSearch-----" + StrSearch);
		
		if (opt.equals("vehicles") || opt.equals("booking")) {
			SetSession("vehstrsql", StrSearch, request);
			response.sendRedirect("../service/vehicle-list.jsp?smart=yes");
			return;
		}
		
		if (opt.equals("tickets")) {
			SetSession("ticketstrsql", StrSearch, request);
			response.sendRedirect("../service/ticket-list.jsp?smart=yes");
			return;
		}
		
		if (opt.equals("jobcard") || opt.equals("partsamount") || opt.equals("labouramount")) {
			SetSession("jcstrsql", StrSearch, request);
			response.sendRedirect("../service/jobcard-list.jsp?smart=yes");
			return;
		}
		
	}
}