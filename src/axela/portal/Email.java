package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Email extends Connect {

	public String submitB = "";
	public static String msg = "";
	public String starttime = "";
	public String start_time = "";
	public String endtime = "";
	public String end_time = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String StrHTML = "";
	public String BranchAccess;
	public String StrSearch = "";
	public String smart = "";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String displayprint = "";
	public String location = "";
	public String RefreshForm = "";
	public String ExportPerm = "";
	public String EnableSearch = "1";
	public String reportURL = "";
	public String ListLink = "<a href=email-list.jsp?smart=yes>Click here to List Emails</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_email_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));
				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and email_date >= '" + starttime + "'";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and email_date <= '" + endtime + "'";
					}
					StrSearch = StrSearch + BranchAccess;
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("emailstrsql", StrSearch, request);
						StrHTML = EmailSummary(request);
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
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
				endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String EmailSummary(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		String StrSql = "";
		int totalemail_count = 0, totalemp_count = 0;
		int totalcustomer_count = 0;
		int dayemail_count = 0;
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = " SELECT concat(substring(email_date,1,8),'000000') as emaildate, "
					+ " count(emp_id) as empcount, "
					+ " count(customer_id) as contactcount "
					+ " from " + compdb(comp_id) + "axela_email "
					+ " left  join " + compdb(comp_id) + "axela_emp on emp_id= email_emp_id "
					+ " left  join " + compdb(comp_id) + "axela_customer on customer_id= email_contact_id "
					+ " where 1=1" + StrSearch;
			StrSql = StrSql + " group by emaildate";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"portlet box  \"><div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Email Summary</div></div><div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th data-hide=\"phone\">Contact</th>\n");
				Str.append("<th data-hide=\"phone\">Email Count</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					dayemail_count = 0;
					totalcustomer_count = totalcustomer_count + crs.getInt("contactcount");
					totalemp_count = totalemp_count + crs.getInt("empcount");
					dayemail_count = dayemail_count + crs.getInt("empcount") + crs.getInt("contactcount");
					if (isSunday(crs.getString("emaildate"))) {
						Str.append("<tr bgcolor=pink>\n");
					} else {
						Str.append("<tr>\n");
					}
					Str.append("<td>").append(ConvertLongDate(crs.getString("emaildate"))).append("</td>\n");
					Str.append("<td>").append(crs.getString("empcount")).append("</td>");
					Str.append("<td>").append(crs.getString("contactcount")).append("</td>");
					Str.append("<td>").append(dayemail_count).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td><b>Total:</b></td>\n");
				Str.append("<td><b>").append(totalemp_count).append("</b></td>\n");
				Str.append("<td><b>").append(totalcustomer_count).append("</b></td>\n");
				Str.append("<td><b>").append(totalcustomer_count + totalemp_count).append("</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody></table></div></div></div>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
