//@Bhagwan Singh 11 feb 2013
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Ticket_Followup extends Connect {

	public String msg = "";
	public String start_time = "";
	public String starttime = "";
	public String end_time = "";
	public String endtime = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String smart = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String[] brand_ids, region_ids, branch_ids, owner_ids;
	public String brand_id = "", region_id = "", branch_id = "", owner_id = "", tickettype_id = "0", go = "";
	public String ListLink = "<a href=ticket-list.jsp?smart=yes>Click here to List Tickets</a>";

	public String ticket_id = "", ticket_desc = "", ticket_next_followup_time = "", ticket_owner = "", ticket_branch = "", ticket_branch_id = "0";

	public axela.accessories.MIS_Check mischeck = new axela.accessories.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("branch_id===" + branch_id);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_customer_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (!starttime.equals("")) {
					StrSearch = " AND SUBSTR(tickettrans_nextfollowup_time,1,8) >= SUBSTR('" + starttime + "',1,8)";
				}

				if (!endtime.equals("")) {
					StrSearch += " AND SUBSTR(tickettrans_nextfollowup_time,1,8) <= SUBSTR('" + endtime + "',1,8)";
				}

				if (go.equals("Go")) {

					if (!tickettype_id.equals("0")) {
						StrSearch += " AND ticket_tickettype_id =" + tickettype_id;
					}

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}

					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}

					if (!branch_id.equals("")) {
						StrSearch += StrSearch + " AND branch_id IN (" + branch_id + ")";
					}

					if (!owner_id.equals("")) {
						StrSearch += " AND ticket_emp_id IN (" + owner_id + ")";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}

					if (msg.equals("")) {
						// SetSession("ticketstrsql", StrSearch, request);
						StrHTML = TicketFollowupList(request);
						if (StrHTML.equals("")) {
							StrHTML = "<font color='#ff0000'><b>No Follow-up Found!</b></font>";
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		tickettype_id = CNumeric(PadQuotes(request.getParameter("dr_type")));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		owner_id = RetrunSelectArrVal(request, "dr_owner_id");
		owner_ids = request.getParameterValues("dr_owner_id");

		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		// if (branch_id.equals("0")) {
		// dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		// if (dr_branch_id.equals("")) {
		// dr_branch_id = "0";
		// }
		// } else {
		// dr_branch_id = branch_id;
		// }
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
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
			msg = msg + "<br>Select End Date!";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String TicketFollowupList(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		StrHTML = "";
		try {
			StrSql = " SELECT "
					+ " tickettrans_ticket_id, ticket_branch_id, tickettrans_followup,"
					+ " tickettrans_nextfollowup_time, ticket_emp_id, "
					+ " COALESCE(branch_name,'') AS branch_name"
					+ " FROM  " + compdb(comp_id) + "axela_service_ticket_trans"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket ON ticket_id= tickettrans_ticket_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id= ticket_branch_id"
					+ " WHERE 1 = 1"
					+ " AND tickettrans_followup = ''"
					+ "" + StrSearch
					+ BranchAccess;
			// + " GROUP BY branch_id"
			// + " ORDER BY branchname";
			// SOP("Followup details======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover sticky-header \" data-filter=\"#filter\">\n");
				Str.append("<thead style=\"margin-top:50px;\">");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Ticket ID</th>\n");
				Str.append("<th data-hide=\"phone\" style=\"width: 500px;\">Followup Description</th>\n");
				Str.append("<th data-hide=\"phone\">Followup Time</th>\n");
				Str.append("<th data-hide=\"phone\">Ticket Owner</th>\n");
				Str.append("<th data-hide=\"phone\">Branch</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					ticket_id = crs.getString("tickettrans_ticket_id");
					ticket_desc = crs.getString("tickettrans_followup");
					ticket_next_followup_time = crs.getString("tickettrans_nextfollowup_time");
					ticket_owner = Exename(comp_id, crs.getInt("ticket_emp_id"));
					ticket_branch = crs.getString("branch_name");
					ticket_branch_id = crs.getString("ticket_branch_id");

					count++;

					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");

					Str.append("<td align=center>").append(ticket_id).append("</td>\n");
					Str.append("<td align=left style=\"width: 500px;\">").append(ticket_desc).append("</td>\n");
					Str.append("<td align=center><a href=\"javascript:remote=window.open('ticket-dash.jsp?ticket_id=" + ticket_id)
							.append("#tabs-2").append("','ticketdash','');remote.focus();\">")
							.append(strToLongDate(ticket_next_followup_time)).append("</a></td>\n");
					Str.append("<td align=center>").append(ticket_owner).append("</td>\n");
					Str.append("<td align=center><a href=../portal/branch-list.jsp?branch_id=" + ticket_branch_id + " target=_blank>")
							.append(ticket_branch).append("</a></td>\n");

					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTicketOwner(String owner_ids[], String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=emp_branch_id"
					+ " WHERE emp_active = 1";
			// if (!branch_id.equals("0"))
			// {
			// SOP("10.12");
			// StrSql += " AND branch_id IN (" + branch_id + ")";
			// }
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("Owner===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_owner_id id=dr_owner_id class='form-control multiselect-dropdown' multiple=multiple size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), owner_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n)");
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

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> Select </option>");
		try {
			StrSql = "SELECT tickettype_id, tickettype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_ticket_type "
					+ " WHERE 1=1 "
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

}
