package axela.sales;

// Created - 24, 26, 27,28 august 2013
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Exe_Ref_Sales extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "", brand_id = "", region_id = "";
	public String[] team_ids, exe_ids, model_ids, soe_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "", soe_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrTeam = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String dr_totalby = "0";
	public String emp_all_exe = "";
	public String filter = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				CheckSession(request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				filter = PadQuotes(request.getParameter("filter"));
				GetValues(request, response);
				CheckForm();
				// SOP("go--------------" + go);
				if (go.equals("Go")) {
					StrSearch = BranchAccess.replace("branch_id", "enquiry_branch_id");

					StrSearch += ExeAccess;

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND emp_id IN (" + exe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListData();
					}
				}
				if (filter.equals("yes")) {
					EnquiryFilter(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
			// SOP("starttime--------1--------" + starttime);
		}

		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
			// SOP("endtime--------1--------" + endtime);
		}

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				targetstarttime = starttime.substring(0, 6) + "01000000";
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>Select End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				targetendtime = endtime.substring(0, 6) + "31000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListData() {
		try {
			int count = 0;
			int totalenquiry = 0;
			int totalbooking = 0;
			int totaldelivered = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT emp_id, emp_name, emp_ref_no, "
					+ " "
					+ " COUNT(DISTINCT(CASE WHEN "
					+ " SUBSTR(enquiry_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(enquiry_date,1,8) <= SUBSTR('" + endtime + "',1,8)"
					+ " THEN enquiry_id END)) AS enquiry,"
					+ " COUNT(DISTINCT(CASE WHEN "
					+ " SUBSTR(so_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(so_date,1,8) <= SUBSTR('" + endtime + "',1,8)"
					+ " AND so_active = 1 THEN so_id END)) AS booking,"
					+ " COUNT(DISTINCT(CASE WHEN "
					+ " SUBSTR(so_delivered_date,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(so_delivered_date,1,8) <= SUBSTR('" + endtime + "',1,8)"
					+ " AND so_active = 1"
					+ " AND so_delivered_date != '' "
					+ " THEN so_id END)) AS delivered "
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_refemp_id = emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id  = enquiry_id "
					+ " WHERE 1 = 1"
					+ StrSearch
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("query--------------------- " + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"table-responsive table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");

			if (crs.isBeforeFirst()) {
				Str.append("<thead><tr>\n");
				Str.append("<th style=\"text-align:center\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Sales Consultant</th>\n");
				Str.append("<th>Enquiries</th>\n");
				Str.append("<th data-hide=\"phone\">Bookings</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Deliveries</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count++;
					totalenquiry += crs.getInt("enquiry");
					totalbooking += crs.getInt("booking");
					totaldelivered += crs.getInt("delivered");
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append("\">")
							.append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(")").append("</a></td>\n");
					Str.append("<td align=right><a href=../sales/report-exe-ref-sales.jsp?filter=yes&enquiry=yes&&starttime=" + starttime + "&endtime=" + endtime + "&filterbranch_id=" + branch_id
							+ "&filterregion_id=" + region_id + "&filterbrand_id=" + brand_id + "&filteremp_id=" + crs.getInt("emp_id")
							+ " target=_blank>").append(crs.getInt("enquiry")).append("</a></td>\n");

					Str.append("<td align=right><a href=../sales/report-exe-ref-sales.jsp?filter=yes&booking=yes&&starttime=" + starttime + "&endtime=" + endtime + "&filterbranch_id=" + branch_id
							+ "&filterregion_id=" + region_id + "&filterbrand_id=" + brand_id + "&filteremp_id=" + crs.getInt("emp_id")
							+ " target=_blank>").append(crs.getInt("booking")).append("</a></td>\n");

					Str.append("<td align=right><a href=../sales/report-exe-ref-sales.jsp?filter=yes&delivered=yes&&starttime=" + starttime + "&endtime=" + endtime + "&filterbranch_id=" + branch_id
							+ "&filterregion_id=" + region_id + "&filterbrand_id=" + brand_id + "&filteremp_id=" + crs.getInt("emp_id")
							+ " target=_blank>").append(crs.getInt("delivered")).append("</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td colspan=2 align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><a href=../sales/report-exe-ref-sales.jsp?filter=yes&enquiry=yes&&starttime=" + starttime + "&endtime=" + endtime + "&filterbranch_id=" + branch_id
						+ "&filterregion_id=" + region_id + "&filterbrand_id=" + brand_id // + "&filteremp_id=" + enquiry_refemp_id
						+ " target=_blank><b>").append(totalenquiry).append("</b></td>\n");
				Str.append("<td align=right><a href=../sales/report-exe-ref-sales.jsp?filter=yes&booking=yes&&starttime=" + starttime + "&endtime=" + endtime + "&filterbranch_id=" + branch_id
						+ "&filterregion_id=" + region_id + "&filterbrand_id=" + brand_id // + "&filteremp_id=" + enquiry_refemp_id
						+ " target=_blank><b>").append(totalbooking).append("</b></td>\n");
				Str.append("<td align=right><a href=../sales/report-exe-ref-sales.jsp?filter=yes&delivered=yes&&starttime=" + starttime + "&endtime=" + endtime + "&filterbranch_id=" + branch_id
						+ "&filterregion_id=" + region_id + "&filterbrand_id=" + brand_id // + "&filteremp_id=" + enquiry_refemp_id
						+ " target=_blank><b>").append(totaldelivered).append("</b></td>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Records Found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void EnquiryFilter(HttpServletRequest request, HttpServletResponse response) {
		try {
			String enquirybranch_id = "", enquiryregion_id = "", enquiryemp_id = "", enquirybrand_id = "";
			HttpSession session = request.getSession(true);

			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			enquirybrand_id = PadQuotes(request.getParameter("filterbrand_id"));
			enquiryregion_id = PadQuotes(request.getParameter("filterregion_id"));
			enquirybranch_id = PadQuotes(request.getParameter("filterbranch_id"));

			enquiryemp_id = PadQuotes(request.getParameter("filteremp_id"));
			String enquiry = PadQuotes(request.getParameter("enquiry"));
			String booking = PadQuotes(request.getParameter("booking"));
			String delivered = PadQuotes(request.getParameter("delivered"));

			if (enquiry.equals("yes")) {
				StrSearch = " AND SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "' , 1, 8)"
						+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "' , 1, 8)"
						+ " AND enquiry_refemp_id !=''";

				if (!enquirybrand_id.equals("")) {
					StrSearch += " AND branch_brand_id IN (" + enquirybrand_id + ")";
				}
				if (!enquirybranch_id.equals("")) {
					StrSearch += " AND enquiry_branch_id IN (" + enquirybranch_id + ")";
				}
				if (!enquiryregion_id.equals("")) {
					StrSearch += " AND branch_region_id IN (" + enquiryregion_id + ")";
				}
				if (!enquiryemp_id.equals("")) {
					StrSearch += " AND enquiry_refemp_id IN (" + enquiryemp_id + ")";
				}
				// SOP("StrSearch----------" + StrSearch);
				SetSession("enquirystrsql", StrSearch, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/enquiry-list.jsp?smart=yes"));
			}
			if (booking.equals("yes")) {
				StrSearch = " AND so_active = 1"
						+ " AND SUBSTR(so_date, 1, 8) >= SUBSTR('" + starttime + "' , 1, 8)"
						+ " AND SUBSTR(so_date, 1, 8) <= SUBSTR('" + endtime + "' , 1, 8)";
				if (!enquirybrand_id.equals("")) {
					StrSearch += " AND branch_brand_id IN (" + enquirybrand_id + ")";
				}
				if (!enquirybranch_id.equals("")) {
					StrSearch += " AND branch_id IN (" + enquirybranch_id + ")";
				}
				if (!enquiryregion_id.equals("")) {
					StrSearch += " AND branch_region_id IN (" + enquiryregion_id + ")";
				}
				if (!enquiryemp_id.equals("")) {
					StrSearch += " AND so_enquiry_id IN (SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry "
							+ " WHERE enquiry_refemp_id IN (" + enquiryemp_id + "))";
				}
				// SOP("StrSearch----------" + StrSearch);
				SetSession("sostrsql", StrSearch, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
			}
			if (delivered.equals("yes")) {
				StrSearch = " AND so_active = 1"
						+ " AND so_delivered_date !=''"
						+ " AND SUBSTR(so_delivered_date, 1, 8) >= SUBSTR('" + starttime + "' , 1, 8)"
						+ " AND SUBSTR(so_delivered_date, 1, 8) <= SUBSTR('" + endtime + "' , 1, 8)";
				if (!enquirybrand_id.equals("")) {
					StrSearch += " AND branch_brand_id IN (" + enquirybrand_id + ")";
				}
				if (!enquirybranch_id.equals("")) {
					StrSearch += " AND branch_id IN (" + enquirybranch_id + ")";
				}
				if (!enquiryregion_id.equals("")) {
					StrSearch += " AND branch_region_id IN (" + enquiryregion_id + ")";
				}
				if (!enquiryemp_id.equals("")) {
					StrSearch += " AND so_enquiry_id IN (SELECT enquiry_id FROM " + compdb(comp_id) + "axela_sales_enquiry "
							+ " WHERE enquiry_refemp_id IN (" + enquiryemp_id + "))";
				}
				// SOP("StrSearch----------" + StrSearch);
				SetSession("sostrsql", StrSearch, request);
				response.sendRedirect(response.encodeRedirectURL("../sales/veh-salesorder-list.jsp?smart=yes"));
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
