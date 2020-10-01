package axela.sales;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Model_Breakup extends Connect {
	
	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] team_ids, exe_ids, model_ids, brand_ids, region_ids, branch_ids;
	public String team_id = "", exe_id = "", model_id = "", brand_id = "", region_id = "";
	public String StrHTML = "";
	public String comp_id = "0";
	public String BranchAccess = "", dr_branch_id = "0";
	public String brandSearch = "";
	public String StrSearch = "", enquiry_Model = "";
	public String go = "";
	public String ExeAccess = "";
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_opportunity_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				// SOP("go-----/-----" + go);
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess.replaceAll("branch_id", "enquiry_branch_id").replace(")", "  OR enquiry_branch_id=0)") + " ";
					
					StrSearch = ExeAccess.replaceAll("emp_id", "enquiry_emp_id").replace(")", "  OR enquiry_emp_id=0)") + " ";
					
					if (!brand_id.equals("")) {
						StrSearch += " AND model_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + "AND branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						enquiry_Model += " AND enquiry_model_id IN (" + model_id + ")";
					}
					if (!team_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						mischeck.branch_id = branch_id;
						StrSearch += " AND enquiry_emp_id IN (SELECT teamtrans_emp_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team_exe"
								+ " WHERE teamtrans_team_id IN (" + team_id + "))";
					}
					if (!exe_id.equals("")) {
						StrSearch += " AND enquiry_emp_id IN (" + exe_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						StrHTML = ListData();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("DDMotors===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (starttime.equals("")) {
			starttime = ReportStartdate();
		}
		
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (brand_id.equals("0")) {
			brand_id = CNumeric(PadQuotes(request.getParameter("dr_principal")));
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
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
	}
	
	protected void CheckForm() {
		msg = "";
		
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		}
		
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		
		if (endtime.equals("")) {
			go += "<br>Select End Date!<br>";
		}
		
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}
	
	public String ListData() {
		String StrSqlsoe = "", StrSqlbooking = "", StrSqlretail = "", StrSqlfeedback = "";
		String Strenqsoejoin = "", Strbookingsoejoin = "", Strretailsoejoin = "";
		StringBuilder Str = new StringBuilder();
		try {
			// ////SOE
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " ORDER BY soe_name";
			// SOP("999999999----------" + StrSql);
			ResultSet rssoe = processQuery(StrSql, 0);
			while (rssoe.next()) {
				
				Strenqsoejoin += " COALESCE(SUM(" + "enqsoe_" + rssoe.getString("soe_id") + "),0) AS '" + rssoe.getString("soe_name") + "',";
				Strbookingsoejoin += " COALESCE(SUM(" + "bookingsoe_" + rssoe.getString("soe_id") + "),0) AS '" + rssoe.getString("soe_name") + "-booking',";
				Strretailsoejoin += " COALESCE(SUM(" + "retailsoe_" + rssoe.getString("soe_id") + "),0) AS '" + rssoe.getString("soe_name") + "-retail',";
				
				StrSqlsoe += " COUNT(DISTINCT CASE"
						+ " WHEN SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
						+ " AND enquiry_soe_id = " + rssoe.getString("soe_id")
						+ " THEN enquiry_id END) AS '" + "enqsoe_" + rssoe.getString("soe_id") + "',";
				
				StrSqlbooking += " COUNT(DISTINCT CASE "
						+ " WHEN SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
						+ " AND enquiry_soe_id = " + rssoe.getString("soe_id")
						+ " AND so_active=1"
						+ " THEN enquiry_id END) AS '" + "bookingsoe_" + rssoe.getString("soe_id") + "',";
				
				StrSqlretail += " COUNT(DISTINCT CASE"
						+ " WHEN SUBSTR(enquiry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
						+ " AND SUBSTR(enquiry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
						+ " AND enquiry_soe_id = " + rssoe.getString("soe_id")
						+ " AND so_active = 1 "
						+ " AND so_delivered_date != ''"
						+ " THEN enquiry_id END) AS '" + "retailsoe_" + rssoe.getString("soe_id") + "',";
			}
			
			StrSql = "SELECT model_id, model_name,"
					+ Strenqsoejoin
					+ Strbookingsoejoin
					+ Strretailsoejoin.substring(0, Strretailsoejoin.length() - 1)
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					
					+ " LEFT JOIN ( SELECT enquiry_model_id,"
					+ " branch_brand_id, branch_region_id, branch_id,"
					+ " enquiry_emp_id, enquiry_team_id,"
					+ StrSqlsoe
					+ StrSqlbooking
					+ StrSqlretail.substring(0, StrSqlretail.length() - 1)
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					// + " INNER JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
					
					+ " WHERE 1=1"
					+ StrSearch
					+ enquiry_Model
					+ BranchAccess
					+ " GROUP BY enquiry_soe_id,enquiry_model_id"
					+ " ) AS tblenquiry ON tblenquiry.enquiry_model_id = model_id"
					+ " WHERE"
					+ " model_active = 1"
					+ StrSearch
					+ enquiry_Model
					+ BranchAccess
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			
			SOP("StrSql-------22222222--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			//
			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\"  table-bordered table-hover\">\n");
				Str.append("<table class=\"table   table-bordered table-hover \" data-filter=\"#filter\">");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th ></th>\n");
				Str.append("<th ></th>\n");
				rssoe.last();
				int soe[] = new int[rssoe.getRow()];
				int booking[] = new int[rssoe.getRow()];
				int retail[] = new int[rssoe.getRow()];
				int soecount = 0, bookingcount = 0, retailcount = 0;
				rssoe.beforeFirst();
				while (rssoe.next()) {
					Str.append("<th data-hide=\"phone\" colspan=3>").append(rssoe.getString("soe_name")).append("</th>\n");
				}
				Str.append("<th data-hide=\"phone, tablet\" colspan=3>Total</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				
				Str.append("<tr align=center>\n");
				Str.append("<td data-toggle=\"true\"><b>#</b></td>\n");
				Str.append("<td ><b>Model</b></td>\n");
				rssoe.beforeFirst();
				while (rssoe.next()) {
					Str.append("<td ><b>E</b></td>\n");
					Str.append("<td ><b>B</b></td>\n");
					Str.append("<td ><b>R</b></td>\n");
				}
				Str.append("<td ><b>E</b></td>\n");
				Str.append("<td ><b>B</b></td>\n");
				Str.append("<td ><b>R</b></td>\n");
				Str.append("</tr>\n");
				
				int count = 0, soetotal = 0, bookingtotal = 0, retailtotal = 0, totalsoecount = 0, totalbookingcount = 0, totalretailcount = 0;
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>");
					Str.append("<td valign=top align=left ><a href=../inventory/item-model.jsp?model_id=").append(crs.getString("model_id")).append(">").append(crs.getString("model_name"))
							.append("</a></td>");
					soecount = 0;
					bookingcount = 0;
					retailcount = 0;
					
					rssoe.beforeFirst();
					while (rssoe.next()) {
						Str.append("<td align=right>").append(crs.getString(rssoe.getString("soe_name"))).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString(rssoe.getString("soe_name") + "-booking")).append("</td>\n");
						Str.append("<td align=right>").append(crs.getString(rssoe.getString("soe_name") + "-retail")).append("</td>\n");
						soe[soecount] = soe[soecount] + crs.getInt(rssoe.getString("soe_name"));
						booking[bookingcount] = booking[bookingcount] + crs.getInt(rssoe.getString("soe_name") + "-booking");
						retail[retailcount] = retail[retailcount] + crs.getInt(rssoe.getString("soe_name") + "-retail");
						
						soecount++;
						bookingcount++;
						retailcount++;
						soetotal = soetotal + crs.getInt(rssoe.getString("soe_name"));
						bookingtotal = bookingtotal + crs.getInt(rssoe.getString("soe_name") + "-booking");
						retailtotal = retailtotal + crs.getInt(rssoe.getString("soe_name") + "-retail");
					}
					Str.append("<td valign=top align=right >").append(soetotal).append("</td>");
					Str.append("<td valign=top align=right >").append(bookingtotal).append("</td>");
					Str.append("<td valign=top align=right >").append(retailtotal).append("</td>");
					soetotal = 0;
					bookingtotal = 0;
					retailtotal = 0;
					Str.append("</tr>\n");
					
				}
				Str.append("<tr>\n");
				Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
				Str.append("<td valign=top align=right><b>Total:</b></td>");
				soecount = 0;
				bookingcount = 0;
				retailcount = 0;
				rssoe.beforeFirst();
				while (rssoe.next()) {
					Str.append("<td align=right><b>").append(soe[soecount]).append("</b></td>\n");
					Str.append("<td align=right><b>").append(booking[bookingcount]).append("</b></td>\n");
					Str.append("<td align=right><b>").append(retail[retailcount]).append("</b></td>\n");
					totalsoecount += soe[soecount];
					totalbookingcount += booking[bookingcount];
					totalretailcount += retail[retailcount];
					soecount++;
					bookingcount++;
					retailcount++;
				}
				Str.append("<td align=right><b>").append(totalsoecount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalbookingcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalretailcount).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				// Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();
			
		} catch (Exception ex) {
			SOPError("DDMotors===" + this.getClass().getName());
			SOPError("Error IN " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
