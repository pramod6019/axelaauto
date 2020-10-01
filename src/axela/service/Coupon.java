package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Coupon extends Connect {

	public String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSearch = "";
	public String smart = "";
	public String StrSql = "";
	public String branch_id = "0";
	public String dr_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String reportURL = "";
	public String ListLink = "<a href=couponcampaign-list.jsp?smart=yes>Click here to List Coupon Campaigns</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_coupon_campaign_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " AND SUBSTR(couponcampaign_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8) ";
					}

					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(couponcampaign_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
					}

					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND branch_id =" + dr_branch_id;
					}

					StrSearch += " AND couponcampaign_active = 1";
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						SetSession("couponstrsql", StrSearch, request);
						StrHTML = CouponSummery(request);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		} else {
			dr_branch_id = branch_id;
		}
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>Select End Date!";
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg += "<br>Enter valid End Date!";
				endtime = "";
			}
		}
	}

	public String CouponSummery(HttpServletRequest request) {
		int coupon_count = 0, coupon_redeemed_count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT couponcampaign_id, couponcampaign_name,"
					+ " couponcampaign_couponcount, couponcampaign_couponredeemdcount"
					+ " FROM " + compdb(comp_id) + "axela_service_coupon_campaign"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_coupon ON coupon_couponcampaign_id = couponcampaign_id"
					+ " INNER JOIN axela_brand ON brand_id = couponcampaign_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE 1=1"
					+ StrSearch
					+ " GROUP BY couponcampaign_id"
					+ " ORDER BY couponcampaign_name";
			SOP("Strsql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<b>Coupon Summery</b><br>");
				Str.append("<table class=\"table table-bordered table-responsive table-hover \" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>Campaign Name</th>\n");
				Str.append("<th width=\"20%\">Coupon Count</th>\n");
				Str.append("<th width=\"20%\">Coupon Redeemed</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					coupon_count += crs.getInt("couponcampaign_couponcount");
					coupon_redeemed_count += crs.getInt("couponcampaign_couponredeemdcount");
					Str.append("<tr>\n<td valign=\"top\" align=\"left\">");
					Str.append("<a href=\"../service/couponcampaign-list.jsp?couponcampaign_id=").append(crs.getString("couponcampaign_id")).append("\">").append(crs.getString("couponcampaign_name"))
							.append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"right\">").append(crs.getString("couponcampaign_couponcount"));
					Str.append("<td valign=\"top\" align=\"right\">").append(crs.getString("couponcampaign_couponredeemdcount"));
					Str.append("</td>\n</tr>\n");
				}
				Str.append("<tr>\n<td align=\"right\"><b>Total: </b></td>\n");
				Str.append("<td align=\"right\"><b>").append(coupon_count).append("</b></td>\n");
				Str.append("<td align=\"right\"><b>").append(coupon_redeemed_count).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
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
