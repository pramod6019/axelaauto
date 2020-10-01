//@Bhagwan Singh 11 feb 2013
package axela.inbound;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Inbound_Monitoring_Board extends Connect {

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
	public String[] brand_ids, region_ids, branch_ids;
	public String brand_id = "", region_id = "", branch_id = "", go = "";
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
					StrSearch = " AND SUBSTR(call_entry_time,1,8) >= SUBSTR('" + starttime + "',1,8)";
				}

				if (!endtime.equals("")) {
					StrSearch += " AND SUBSTR(call_entry_time,1,8) <= SUBSTR('" + endtime + "',1,8)";
				}

				if (go.equals("Go")) {

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}

					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}

					if (!branch_id.equals("")) {
						StrSearch += StrSearch + " AND branch_id IN (" + branch_id + ")";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}

					if (msg.equals("")) {
						// SetSession("ticketstrsql", StrSearch, request);
						StrHTML = ListData(request);
						if (StrHTML.equals("")) {
							StrHTML = "<font color='#ff0000'><b>No Data Found!</b></font>";
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
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");

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

	public String ListData(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		StrHTML = "";
		try {
			StrSql = " SELECT call_emp_id,"
					+ " COUNT(call_id) AS callcount,"
					+ " COUNT(DISTINCT enquiry_id) AS enquirycount,"
					+ " COUNT(DISTINCT preowned_id) AS preownedcount,"
					+ " COUNT(DISTINCT vehfollowup_id) AS vehfollowupcount,"
					+ " COUNT(DISTINCT callback_id) AS callbackcount,"
					+ " COUNT(DISTINCT ticket_id) AS ticketcount,"
					+ " emp_id, emp_name, emp_ref_no"
					+ " FROM  " + compdb(comp_id) + "axela_call"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = call_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_call_id = call_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned ON preowned_call_id = call_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_call_id = call_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_callback ON callback_call_id = call_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket ON ticket_call_id = call_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id"
					+ " WHERE 1 = 1"
					+ "" + StrSearch
					+ BranchAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("Monitoring board---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
				Str.append("<thead style=\"margin-top:50px;\">");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">Executive</th>\n");
				Str.append("<th data-hide=\"phone\">Call Count</th>\n");
				Str.append("<th data-hide=\"phone\">Enquiry Count</th>\n");
				Str.append("<th data-hide=\"phone\">Preowned Count</th>\n");
				Str.append("<th data-hide=\"phone\">Service Booking Count</th>\n");
				Str.append("<th data-hide=\"phone\">Call Back Count</th>\n");
				Str.append("<th data-hide=\"phone\">Ticket Count</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					Str.append("<td align=center>");
					Str.append(++count);
					Str.append("</td>");

					Str.append("<td align=center>");
					Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), crs.getString("emp_ref_no")));
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append(crs.getString("callcount"));
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append(crs.getString("enquirycount"));
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append(crs.getString("preownedcount"));
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append(crs.getString("vehfollowupcount"));
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append(crs.getString("callbackcount"));
					Str.append("</td>");

					Str.append("<td align=right>");
					Str.append(crs.getString("ticketcount"));
					Str.append("</td>");

				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				// Str.append("</div>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
