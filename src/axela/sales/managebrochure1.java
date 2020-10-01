/*Saiman 26th March 2013 , 29th march 2013
 * Ved Prakash (25 Feb 2013)
 * smitha nag 29 march 2013
 */
package axela.sales;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class managebrochure1 extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String year = "";
	public int curryear = 0;
	public String target_id = "0";
	public String target_enquiry_count = "";
	public String target_so_amount = "";
	public String target_enquiry_calls_count = "";
	public String target_enquiry_meetings_count = "";
	public String target_enquiry_testdrives_count = "";
	public String target_enquiry_hot_count = "";
	public String target_so_count = "";
	public String target_enddate = "";
	public String target_startdate = "";
	public String targetid = "0";
	public String enquirycount = "0";
	public String enquirycall = "0";
	public String enquirymeeting = "0";
	public String enquirytestdrive = "0";
	public String enquiryhot = "0";
	public String enquiryso = "0";
	public String amt = "0";
	public int total_enquiry_count = 0, total_enquiry_calls_count = 0;
	public int total_enquiry_meetings_count = 0, total_enquiry_testdrives_count = 0;
	public int total_enquiry_hot_count = 0, total_so_count = 0;
	public int total_so_amount = 0;
	public String QueryString = "";
	public String ExeAccess = "";
	public String smartarr[][] = {};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id, emp_target_access", request, response);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				emp_id = CNumeric(PadQuotes(request.getParameter("dr_executives")));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				if (year.equals("0")) {
					year = Integer.toString(curryear);
				}
				CheckForm();
				StrHTML = Listdata(request);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata(HttpServletRequest request) throws SQLException {
		StringBuilder Str = new StringBuilder();
		if (!emp_id.equals("0")) {
			String tid[];
			String tsdate[];
			String tedate[];
			String tenquirycount[];
			String tenquirycall[];
			String tenquirymeeting[];
			String tenquirytestdrive[];
			String tenquiryhot[];
			String tenquiryso[];
			String tenquirysoamt[];

			StrSql = "Select target_id,"
					+ " coalesce(target_startdate, '') as target_startdate,"
					+ " coalesce(target_enddate, '') as target_enddate,"
					+ " coalesce(target_enquiry_count, 0) as target_enquiry_count,"
					+ " coalesce(target_enquiry_calls_count, 0) as target_enquiry_calls_count,"
					+ " coalesce(target_enquiry_meetings_count, 0) as target_enquiry_meetings_count,"
					+ " coalesce(target_enquiry_testdrives_count, 0) as target_enquiry_testdrives_count,"
					+ " coalesce(target_enquiry_hot_count, 0) as target_enquiry_hot_count,"
					+ " coalesce(target_so_count, 0) as target_so_count,"
					+ " coalesce(target_so_amount, 0) as target_so_amount";

			CountSql = "SELECT Count(distinct target_id)";

			SqlJoin = " from " + compdb(comp_id) + "axela_sales_target"
					+ " where target_emp_id=" + emp_id + ""
					+ " and SUBSTR(target_startdate, 1, 8) >= " + year + "0101"
					+ " and SUBSTR(target_enddate, 1, 8)<=" + year + "1231";
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int ind = 0;
				int index = Integer.parseInt(ExecuteQuery(CountSql));
				tid = new String[index];
				tsdate = new String[index];
				tedate = new String[index];
				tenquirycount = new String[index];
				tenquirycall = new String[index];
				tenquirymeeting = new String[index];
				tenquirytestdrive = new String[index];
				tenquiryhot = new String[index];
				tenquiryso = new String[index];
				tenquirysoamt = new String[index];
				while (crs.next()) {
					tid[ind] = crs.getString("target_id");
					tsdate[ind] = crs.getString("target_startdate");
					tedate[ind] = crs.getString("target_enddate");
					tenquirycount[ind] = crs.getString("target_enquiry_count");
					tenquirycall[ind] = crs.getString("target_enquiry_calls_count");
					tenquirymeeting[ind] = crs.getString("target_enquiry_meetings_count");
					tenquirytestdrive[ind] = crs.getString("target_enquiry_testdrives_count");
					tenquiryhot[ind] = crs.getString("target_enquiry_hot_count");
					tenquiryso[ind] = crs.getString("target_so_count");
					tenquirysoamt[ind] = crs.getString("target_so_amount");
					ind++;
				}

				Str.append("<table border=1 cellpadding=0 cellspacing=0 class=listtable>\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Month</th>\n");
				Str.append("<th>Enquiry Count</th>\n");
				Str.append("<th>Enquiry Calls</th>\n");
				Str.append("<th>Enquiry Meeting</th>\n");
				Str.append("<th>Enquiry Test Drives</th>\n");
				Str.append("<th>Enquiry Hot</th>\n");
				Str.append("<th>Enquiry SO</th>\n");
				Str.append("<th>Enquiry SO Amount</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				int count = 0;
				for (int i = 1; i <= 12; i++) {
					count = count + 1;
					for (int j = 0; j < tsdate.length; j++) {
						if (tsdate[j].equals(year + doublenum(i) + "01000000")) {
							targetid = tid[j];
							enquirycount = tenquirycount[j];
							enquirycall = tenquirycall[j];
							enquirymeeting = tenquirymeeting[j];
							enquirytestdrive = tenquirytestdrive[j];
							enquiryhot = tenquiryhot[j];
							enquiryso = tenquiryso[j];
							amt = tenquirysoamt[j];
							break;
						} else {
							targetid = "0";
							enquirycount = "0";
							enquirycall = "0";
							enquirymeeting = "0";
							enquirytestdrive = "0";
							enquiryhot = "0";
							enquiryso = "0";
							amt = "0";
						}
					}
					Str.append("<tr><td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top>");
					Str.append(TextMonth(i - 1)).append("-").append(year);
					Str.append("<input name=\"txt_target_id_").append(count).append("\" type=\"hidden\" size=\"10\" maxlength=\"10\" value=").append(targetid).append(">");
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>").append(enquirycount).append("</td>\n");
					Str.append("<td valign=top align=right>").append(enquirycall).append("</td>\n");
					Str.append("<td valign=top align=right>").append(enquirymeeting).append("</td>\n");
					Str.append("<td valign=top align=right>").append(enquirytestdrive).append("</td>\n");
					Str.append("<td valign=top align=right>").append(enquiryhot).append("</td>\n");
					Str.append("<td valign=top align=right>").append(enquiryso).append("</td>\n");
					Str.append("<td valign=top align=right>").append(amt).append("</td>\n");
					Str.append("<td valign=top align=right><a href=\"target-model-list.jsp?target_id=").append(targetid).append("&emp_id=").append(emp_id).append("&year=").append(year)
							.append("&month=").append(i).append("\">Update Target</a></td>\n");
					Str.append("</tr>\n");

					total_enquiry_count = total_enquiry_count + Integer.parseInt(enquirycount);
					total_enquiry_calls_count = total_enquiry_calls_count + Integer.parseInt(enquirycall);
					total_enquiry_meetings_count = total_enquiry_meetings_count + Integer.parseInt(enquirymeeting);
					total_enquiry_testdrives_count = total_enquiry_testdrives_count + Integer.parseInt(enquirytestdrive);
					total_enquiry_hot_count = total_enquiry_hot_count + Integer.parseInt(enquiryhot);
					total_so_count = total_so_count + Integer.parseInt(enquiryso);
					total_so_amount = total_so_amount + Integer.parseInt(amt);
				}
				Str.append("<tr>");
				Str.append("<td colspan='2' align = 'right'><b>Total:</b></td>\n");
				Str.append("<td align = 'right'><b>" + total_enquiry_count + "</b></td>\n");
				Str.append("<td align = 'right'><b>" + total_enquiry_calls_count + "</b></td>\n");
				Str.append("<td align = 'right'><b>" + total_enquiry_meetings_count + "</b></td>\n");
				Str.append("<td align = 'right'><b>" + total_enquiry_testdrives_count + "</b></td>\n");
				Str.append("<td align = 'right'><b>" + total_enquiry_hot_count + "</b></td>\n");
				Str.append("<td align = 'right'><b>" + total_so_count + "</b></td>\n");
				Str.append("<td align = 'right'><b>" + total_so_amount + "</b></td>\n");
				Str.append("</tr>\n");
				Str.append("</table>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	protected void CheckForm() {
		if (emp_id.equals("0")) {
			msg = "Select Sales Consultant!";
		}
	}

	public String PopulateEmp() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>Select</option>");
		try {
			StrSql = "SELECT emp_id, concat(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " from " + compdb(comp_id) + "axela_emp"
					+ " where emp_sales=1" + ExeAccess
					+ " group by emp_id"
					+ " order by emp_name";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateYear() {
		StringBuilder Str = new StringBuilder();
		try {
			for (int i = curryear - 3; i <= curryear + 3; i++) {
				Str.append("<option value=").append(i);
				Str.append(StrSelectdrop(Integer.toString(i), year));
				Str.append(">").append(i).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
