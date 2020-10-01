package axela.service;

//created by ankit
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Coupon_Campaign extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "", year = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "", branch = "";
	public String[] brand_ids, campaigntype_ids, department_ids;
	public String brand_id = "", campaigntype_id = "", department_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String branch_name = "";
	public String StrSearch = "";
	public String emp_all_exe = "";

	static DecimalFormat deci = new DecimalFormat("0.000");

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

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					if (!brand_id.equals("")) {
						StrSearch += " AND couponcampaign_brand_id IN (" + brand_id + ") ";
					}
					if (!campaigntype_id.equals("")) {
						StrSearch += " AND couponcampaign_campaigntype_id IN (" + campaigntype_id + ") ";
					}
					if (!department_id.equals("")) {
						StrSearch += " AND couponcampaign_department_id IN (" + department_id + ") ";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = CouponCampaignDetails();
					}
				}
			}

			// SOP("StrSearch_----------" + StrSearch);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		campaigntype_id = RetrunSelectArrVal(request, "dr_campaign_id");
		campaigntype_ids = request.getParameterValues("dr_campaign_id");
		department_id = RetrunSelectArrVal(request, "dr_dept_id");
		department_ids = request.getParameterValues("dr_dept_id");

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

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
			msg = msg + "<br>Select End Date!<br>";
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

	public String CouponCampaignDetails() {
		try {

			int count = 0;
			int totalcouponcount = 0, totalcouponissued = 0, totalcouponnotissued = 0, totalcouponredeemed = 0, totalcouponnotredeemed = 0;
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT couponcampaign_id, couponcampaign_name, couponcampaign_active,"
					+ " couponcampaign_couponvalue, COUNT(coupon_couponcampaign_id) AS couponcount,"
					+ " SUM(IF(coupon_contact_id != 0, 1, 0)) AS couponissued,"
					+ " SUM(IF(coupon_contact_id = 0, 1, 0)) AS couponnotissued,"
					+ " SUM(IF(coupon_redeem_emp_id != 0 AND coupon_redeem_time != '', 1, 0)) AS couponredeemed,"
					+ " SUM(IF(coupon_redeem_emp_id = 0	AND coupon_redeem_time = '', 1, 0)) AS couponnotredeemed"
					+ " FROM " + compdb(comp_id) + "axela_service_coupon_campaign"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon ON coupon_couponcampaign_id = couponcampaign_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = coupon_contact_id"
					+ " WHERE 1 = 1"
					// + " AND couponcampaign_active = 1"
					+ " AND SUBSTRING(couponcampaign_startdate, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTRING(couponcampaign_enddate, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)"
					+ StrSearch
					+ " GROUP BY couponcampaign_id";

			SOP("CouponCampaignStatusDetails------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" style=\"text-align:center\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Campaign</th>\n");
				Str.append("<th data-hide=\"phone\">Coupon Count</th>\n");
				Str.append("<th data-hide=\"phone\">Coupon Issued</th>\n");
				Str.append("<th data-hide=\"phone\">Coupon Not Issued</th>\n");
				Str.append("<th data-hide=\"phone\">Coupon Redeemed</th>\n");
				Str.append("<th data-hide=\"phone\">Coupon Not Redeemed</th>\n");
				Str.append("<th data-hide=\"phone\">Coupon Value</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				while (crs.next()) {
					count++;
					totalcouponcount += crs.getInt("couponcount");
					totalcouponissued += crs.getInt("couponissued");
					totalcouponnotissued += crs.getInt("couponnotissued");
					totalcouponredeemed += crs.getInt("couponredeemed");
					totalcouponnotredeemed += crs.getInt("couponnotredeemed");

					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=couponcampaign-list.jsp?couponcampaign_id=")
							.append(crs.getString("couponcampaign_id")).append(" target=_blank>")
							.append(crs.getString("couponcampaign_name")).append("</a>");
					if (crs.getString("couponcampaign_active").equals("0")) {
						Str.append("<br/> <font color=red><b>[Inactive]</b></font>");
					}
					Str.append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("couponcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("couponissued")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("couponnotissued")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("couponredeemed")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("couponnotredeemed")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("couponcampaign_couponvalue")).append("</td>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td align=right colspan='2'><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(totalcouponcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalcouponissued).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalcouponnotissued).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalcouponredeemed).append("</b></td>\n");
				Str.append("<td align=right><b>").append(totalcouponnotredeemed).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><center><b>No Coupon Campaign Found!</b></center></font><br>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateBrand(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1 = 1"
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(ArrSelectdrop(crs.getInt("brand_id"), brand_ids));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDepartment(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT department_id, department_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp_department"
					+ " WHERE 1 = 1"
					+ " GROUP BY department_id"
					+ " ORDER BY department_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("department_id"));
				Str.append(ArrSelectdrop(crs.getInt("department_id"), department_ids));
				Str.append(">").append(crs.getString("department_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT couponcampaintype_id, couponcampaintype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_couponcampain_type"
					+ " WHERE 1 = 1"
					+ " GROUP BY couponcampaintype_id"
					+ " ORDER BY couponcampaintype_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("couponcampaintype_id"));
				Str.append(ArrSelectdrop(crs.getInt("couponcampaintype_id"), campaigntype_ids));
				Str.append(">").append(crs.getString("couponcampaintype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

}
