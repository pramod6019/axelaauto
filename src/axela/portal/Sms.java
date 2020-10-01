package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Sms extends Connect {

	public String submitB = "";
	public String comp_id = "0";
	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "0", branch_id = "0";
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
	public String ListLink = "<a href=sms-list.jsp?smart=yes>Click here to List SMS</a>";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_sms_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				smart = PadQuotes(request.getParameter("smart"));

				if (!smart.equals("yes")) {
					GetValues(request, response);
					CheckForm();
					if (!starttime.equals("")) {
						StrSearch = " and sms_date >= '" + starttime + "'";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and sms_date <= '" + endtime + "'";
					}
					StrSearch = StrSearch + BranchAccess;
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						SetSession("smsstrsql", StrSearch, request);
						StrHTML = SmsSummary(request);
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
				endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String SmsSummary(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		String StrSql = "";
		int totalsms_count = 0, daysms_count = 0, totalemp_count = 0;
		int totalcustomer_count = 0, credit = 0;
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = " SELECT concat(substring(sms_date,1,8),'000000') as smsdate,"
					+ " sum(sms_credits) as smscredits, "
					+ " count(emp_id) as empcount, "
					+ " count(contact_id) as contactcount "
					+ " from " + compdb(comp_id) + "axela_sms "
					+ " left  join " + compdb(comp_id) + "axela_emp on emp_id= sms_emp_id "
					+ " left  join " + compdb(comp_id) + "axela_customer_contact on contact_id= sms_contact_id "
					+ // " left  join " + compdb(comp_id) + "axela_customer on customer_id= sms_customer_id " +
					" where 1=1 " + StrSearch;
			StrSql = StrSql + " group by smsdate";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<b>SMS Summary</b><br>");
				Str.append("<div class=\"container-fluid portlet box  \">");
				Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">SMS Summary</div>");
				Str.append("</div>");
				Str.append("<div class=\"portlet-body\">");
				Str.append("<div class=\"tab-pane\">");
				Str.append("<table class=\"table table-hover\">");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>Executive</th>\n");
				Str.append("<th>Contact</th>\n");
				Str.append("<th>SMS Count</th>\n");
				Str.append("<th>Credits</th>\n");
				Str.append("</tr></thead>\n");

				while (crs.next()) {
					daysms_count = 0;
					totalsms_count = totalsms_count + +crs.getInt("empcount") + crs.getInt("contactcount");
					totalcustomer_count = totalcustomer_count + crs.getInt("contactcount");
					totalemp_count = totalemp_count + crs.getInt("empcount");
					daysms_count = daysms_count + crs.getInt("empcount") + crs.getInt("contactcount");
					credit = credit + crs.getInt("smscredits");
					if (isSunday(crs.getString("smsdate"))) {
						Str.append("<tr bgcolor=pink>\n");
					} else {
						Str.append("<tr>\n");
					}
					Str.append("<td valign=top align=left >" + ConvertLongDate(crs.getString("smsdate")) + "</td>\n");
					Str.append("<td valign=top align=right>" + crs.getString("empcount") + "</td>");
					Str.append("<td valign=top align=right>" + crs.getString("contactcount") + "</td>");
					Str.append("<td valign=top align=right>" + daysms_count + "</td>");
					Str.append("<td valign=top align=right>" + crs.getInt("smscredits") + "</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>\n");
				Str.append("<td valign=top align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b>" + totalemp_count + "</b></td>\n");
				Str.append("<td align=right><b>" + totalcustomer_count + "</b></td>\n");
				Str.append("<td align=right><b>" + totalsms_count + "</b></td>\n");
				Str.append("<td align=right><b>" + credit + "</b></td>\n");
				Str.append("</tr>");
				Str.append("</table></div></div></div>");
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
