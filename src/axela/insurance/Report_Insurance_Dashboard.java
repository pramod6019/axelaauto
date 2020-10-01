package axela.insurance;
//aJIt
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insurance_Dashboard extends Connect {

	public String submitB = "";
	public String StrSql = "";
	public String startdate = "", start_date = "";
	public String enddate = "", end_date = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String insurpolicy_branch_id = "0", branch_id = "0";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String go = "";
	public Date date = new Date();
	public DecimalFormat df = new DecimalFormat("0.00");
	public String ExeAccess = "";
	public String[] exe_ids;
	public String exe_id = "";
	DecimalFormat deci = new DecimalFormat("###.##");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				if (branch_id.equals("0")) {
					insurpolicy_branch_id = CNumeric(PadQuotes(request.getParameter("dr_bay_branch_id")));
				} else {
					insurpolicy_branch_id = branch_id;
				}
				go = PadQuotes(request.getParameter("submit_button"));
				startdate = ReportStartdate();
				enddate = strToShortDate(ToShortDate(kknow()));

				if (go.equals("Go")) {

					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						if (!exe_id.equals("")) {
							StrSearch = StrSearch + " and emp_id in (" + exe_id + ")";
						}
						StrHTML = ListData();
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
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		startdate = PadQuotes(request.getParameter("txt_startdate"));
		enddate = PadQuotes(request.getParameter("txt_enddate"));

		if (!startdate.equals("")) {
			start_date = ConvertShortDateToStr(startdate);
		}

		if (!enddate.equals("")) {
			end_date = ConvertShortDateToStr(enddate);
		}
	}

	protected void CheckForm() {
		msg = "";
		if (startdate.equals("")) {
			msg += "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(startdate)) {
				start_date = ConvertShortDateToStr(startdate);
			} else {
				msg += "<br>Enter valid Start Date!";
				start_date = "";
			}
		}

		if (enddate.equals("")) {
			msg += "<br>Select End Date!";
		} else {
			if (isValidDateFormatShort(enddate)) {
				end_date = ConvertShortDateToStr(enddate);
				if (!start_date.equals("") && !end_date.equals("")
						&& Long.parseLong(start_date) > Long.parseLong(end_date)) {
					msg += "<br>Start Date should be less than End date!";
				}
			} else {
				msg += "<br>Enter valid End Date!";
				end_date = "";
			}
		}
	}

	public String ListData() {
		StringBuilder Str = new StringBuilder();
		start_date = start_date.substring(0, 8);
		end_date = end_date.substring(0, 8);
		int ttlinsurenquiry = 0, ttlsold = 0, ttlamt = 0;
		int ttlfollowup = 0, ttlcompleted = 0;
		double insurenquirycount1 = 0.0;
		double soldper = 0.0;
		double sold = 0.0;

		StrSql = "SELECT "
				+ " emp_id, "
				+ " emp_name, "
				+ " (SELECT "
				+ " COUNT(DISTINCT insurenquiry_id) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
				+ " WHERE insurenquiry_emp_id = emp_id "
				+ " AND SUBSTR(insurenquiry_entry_date, 1, 8) >= '" + start_date + "'"
				+ " AND SUBSTR(insurenquiry_entry_date, 1, 8) <= '" + end_date + "'"
				+ " ) AS insurenquirycount, "
				+ " (SELECT "
				+ " COUNT(insurenquiryfollowup_id) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id "
				+ " WHERE insurenquiry_emp_id = emp_id "
				+ " AND SUBSTR(insurenquiryfollowup_followup_time, 1, 8) >= '" + start_date + "'"
				+ " AND SUBSTR(insurenquiryfollowup_followup_time, 1, 8) <= '" + end_date + "'"
				+ " ) AS followupcount, "
				+ " (SELECT "
				+ " COUNT(insurenquiryfollowup_id) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup "
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_id = insurenquiryfollowup_insurenquiry_id "
				+ " WHERE insurenquiry_emp_id = emp_id "
				+ " AND insurenquiryfollowup_desc = ! '' "
				+ " AND SUBSTR(insurenquiryfollowup_followup_time, 1, 8) >= '" + start_date + "'"
				+ " AND SUBSTR(insurenquiryfollowup_followup_time, 1, 8) <= '" + end_date + "'"
				+ " ) AS completed, "
				+ " (SELECT "
				+ " COUNT(insurpolicy_id) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_policy "
				+ " WHERE insurpolicy_emp_id = emp_id "
				+ " AND insurpolicy_active = 1 "
				+ " AND SUBSTR(insurpolicy_date, 1, 8) >= '" + start_date + "'"
				+ " AND SUBSTR(insurpolicy_date, 1, 8) <= '" + end_date + "'"
				+ " ) AS sold, "
				+ " COALESCE ((SELECT "
				+ " SUM(insurpolicy_premium_amt) "
				+ " FROM " + compdb(comp_id) + "axela_insurance_policy "
				+ " WHERE insurpolicy_emp_id = emp_id "
				+ " AND insurpolicy_active = 1 "
				+ " AND SUBSTR(insurpolicy_date, 1, 8) >= '" + start_date + "'"
				+ " AND SUBSTR(insurpolicy_date, 1, 8) <= '" + end_date + "'"
				+ " ),0) AS amt "
				+ " FROM " + compdb(comp_id) + "axela_emp "
				+ " WHERE 1 = 1 "
				+ " AND emp_active = 1 "
				+ " AND emp_insur = 1 "
				+ " GROUP BY emp_id "
				+ " ORDER BY emp_name";

		// SOP("StrSql---------" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-responsive table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\"  nowrap>#</th>\n");
				Str.append("<th data-toggle=\"true\" nowrap>Name</th>\n");
				Str.append("<th nowrap>Insurance Enquiry Count</th>\n");
				Str.append("<th  data-hide=\"phone\" nowrap>Follow-up</th>\n");
				Str.append("<th data-hide=\"phone\" nowrap>Follow-up Completed</th>\n");
				Str.append("<th data-hide=\"phone\" nowrap>Insurance Sold</th>\n");
				Str.append("<th data-hide=\"phone\" nowrap>Insurance Sold%</th>\n");
				Str.append("<th data-hide=\"phone\" nowrap>Insurance Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					ttlinsurenquiry = ttlinsurenquiry + crs.getInt("insurenquirycount");
					ttlfollowup = ttlfollowup + crs.getInt("followupcount");
					ttlcompleted = ttlcompleted + crs.getInt("completed");
					ttlsold = ttlsold + crs.getInt("sold");
					ttlamt = ttlamt + (int) crs.getDouble("amt");
					sold = crs.getDouble("sold");
					insurenquirycount1 = crs.getDouble("insurenquirycount");
					if (insurenquirycount1 != 0.0)
					{
						soldper = (sold / insurenquirycount1) * 100;
						soldper = Double.parseDouble(deci.format(soldper));
					} else
					{
						soldper = 0.00;
					}
					Str.append("<tr>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(count).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("emp_name")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("insurenquirycount")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("followupcount")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("completed")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("sold")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(deciFormat(soldper + "")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("amt")).append("</td>\n");
					Str.append("</tr>\n");
				}
				double ttlsoldper = 0.0;

				if (ttlinsurenquiry != 0.0)
				{
					ttlsoldper = ((double) ttlsold / ttlinsurenquiry) * 100;
				} else {
					ttlsoldper = 0.00;
				}
				Str.append("<tr>\n");
				Str.append("<td align=\"right\"><b>Total:</b>").append("</td>\n");
				Str.append("<td align=\"right\" valign=\"top\">").append(" ").append("</td>\n");
				Str.append("<td align=\"right\" valign=\"top\"><b>").append(ttlinsurenquiry).append("</b></td>\n");
				Str.append("<td align=\"right\" valign=\"top\"><b>").append(ttlfollowup).append("</b></td>\n");
				Str.append("<td align=\"right\" valign=\"top\"><b>").append(ttlcompleted).append("</b></td>\n");
				Str.append("<td align=\"right\" valign=\"top\"><b>").append(ttlsold).append("</b></td>\n");
				Str.append("<td align=\"right\" valign=\"top\"><b>").append(deciFormat(deci.format(ttlsoldper) + "")).append("</b></td>\n");
				Str.append("<td align=\"right\" valign=\"top\"><b>").append(deciFormat(IndDecimalFormat(ttlamt + ""))).append("</b></td>\n");
				Str.append("</tr>\n");

				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=\"red\"><b>No Details Found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = 1 "
					+ " and emp_insur=1 " + ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=dr_executive id=dr_executive class=form-control multiple=\"multiple\" size=10 style=\"width:250px\">");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			// Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String deciFormat(String val)
	{
		String Str = "";
		if (val.contains(".") && val.replace(".", "-").split("-")[1].length() == 1) {
			Str = val + "0";
		} else if (val.contains(".") && val.replace(".", "-").split("-")[1].length() > 1) {
			Str = deci.format(Double.parseDouble(val)) + "";
		} else if ((Str.contains(".") && Str.replace(".", "-").split("-")[1].length() > 1)) {
			Str = Str + "0";
		} else if (val.equalsIgnoreCase("NaN") || val.equalsIgnoreCase("Infinity")) {
			Str = "0.00";
		} else {
			Str = val + ".00";
		}
		return Str;
	}
}
