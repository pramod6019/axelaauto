/*Saiman 26th March 2013 , 29th march 2013
 * Ved Prakash (25 Feb 2013)
 * smitha nag 29 march 2013
 */
package axela.insurance;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Insurance_Target_List extends Connect {

	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String emp_id = "0";
	public String comp_id = "0", branch_id = "0";
	public String emp_role_id = "0";
	public String year = "";
	public int curryear = 0;
	public String insurance_target_id = "0";
	public String insurance_enquiry_count = "0", insurance_target_policy_count = "0";
	public long total_insurance_target_enquiry_count = 0, total_insurance_target_policy_count = 0;
	public String QueryString = "";
	public String ExeAccess = "", smart = "", StrSearch = "";
	public String smartarr[][] = {};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "service_target_access", request, response);
			branch_id = GetSession("emp_branch_id", request);
			if (!comp_id.equals("0")) {
				msg = PadQuotes(request.getParameter("msg"));
				if (branch_id.equals("0")) {
					branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
				}
				if (branch_id.equals("0")) {
					branch_id = ExecuteQuery("SELECT branch_id "
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_active = 1 "
							+ " AND branch_branchtype_id IN (3) LIMIT 1");
				}
				emp_id = CNumeric(PadQuotes(request.getParameter("dr_executives")));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				QueryString = PadQuotes(request.getQueryString());
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));
				if (year.equals("0")) {
					year = Integer.toString(curryear);
				}

				if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("insurancetargetstrsql", request).equals("")) {
						StrSearch += GetSession("insurancestrsql", request);
						SOP("StrSearch===" + StrSearch);
					}
				}

				CheckForm();
				StrHTML = ListData(request);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData(HttpServletRequest request) throws SQLException {
		StringBuilder Str = new StringBuilder();
		if (!emp_id.equals("0")) {
			String tid[];
			String tsdate[];
			String tedate[];
			String tenquirycount[];
			String tpolicy[];

			StrSql = "SELECT insurance_target_id,"
					+ " COALESCE (insurance_target_startdate, '') AS insurance_target_startdate,"
					+ " COALESCE (insurance_target_enddate, '') AS insurance_target_enddate,"
					+ " COALESCE(insurance_target_enquiry_count, 0) AS insurance_target_enquiry_count,"
					+ " COALESCE(insurance_target_policy_count, 0) AS insurance_target_policy_count";

			CountSql = "SELECT COUNT(DISTINCT insurance_target_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_insurance_target"
					+ " WHERE insurance_target_emp_id=" + emp_id + ""
					+ " AND SUBSTR(insurance_target_startdate, 1, 8) >= " + year + "0101"
					+ " AND SUBSTR(insurance_target_enddate, 1, 8)<=" + year + "1231";
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;

			// SOP("target list query ======= " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int ind = 0;
				int index = Integer.parseInt(ExecuteQuery(CountSql));
				tid = new String[index];
				tsdate = new String[index];
				tedate = new String[index];
				tenquirycount = new String[index];
				tpolicy = new String[index];

				while (crs.next()) {
					tid[ind] = crs.getString("insurance_target_id");
					tsdate[ind] = crs.getString("insurance_target_startdate");
					tedate[ind] = crs.getString("insurance_target_enddate");
					tenquirycount[ind] = crs.getString("insurance_target_enquiry_count");
					tpolicy[ind] = crs.getString("insurance_target_policy_count");
					ind++;
				}

				Str.append("<div class=\" table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th >Month</th>\n");
				Str.append("<th >Insurance Enquiry Count</th>\n");
				Str.append("<th >Insurance Policy Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				for (int i = 1; i <= 12; i++) {
					count = count + 1;
					for (int j = 0; j < tsdate.length; j++) {
						if (tsdate[j].equals(year + doublenum(i) + "01000000")) {
							insurance_target_id = tid[j];
							insurance_enquiry_count = tenquirycount[j];
							insurance_target_policy_count = tpolicy[j];
							break;
						} else {
							insurance_target_id = "0";
							insurance_enquiry_count = "0";
							insurance_target_policy_count = "0";
						}
					}
					Str.append("<tr><td>").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append(TextMonth(i - 1)).append("-").append(year);
					// Str.append("<input name=\"txt_target_id_").append(count).append("\" type=\"hidden\" size=\"10\" maxlength=\"10\" value=").append(servicetargetid).append(">");
					Str.append("</td>\n");
					Str.append("<td align=right>").append(insurance_enquiry_count).append("</td>\n");
					Str.append("<td align=right>").append(insurance_target_policy_count).append("</td>\n");
					Str.append("<td align=left><a href=\"insurance-target-update.jsp?update=yes&insurance_target_id=").append(insurance_target_id)
							.append("&insurance_target_exe_id=").append(emp_id).append("&year=").append(year)
							.append("&month=").append(i).append("\">Update Target</a></td>\n");
					Str.append("</tr>\n");
					total_insurance_target_enquiry_count += Long.parseLong(insurance_enquiry_count);
					total_insurance_target_policy_count += Long.parseLong(insurance_target_policy_count);
				}
				Str.append("<tr>\n");
				Str.append("<td>\n</td>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b>").append(total_insurance_target_enquiry_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_insurance_target_policy_count).append("</b></td>\n");
				Str.append("<td>\n</td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
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
		msg = "";
		if (branch_id.equals("0")) {
			msg = msg + "Select Branch!";
		} else {
			if (emp_id.equals("0")) {
				msg = msg + "Select Executive!";
			}
		}
	}

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id = 3";
			SqlStr += " ORDER BY branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append("(").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateEmp(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		if (branch_id.equals("0")) {
			branch_id = "-1";
		}
		Str.append("<select name=\"dr_executives\" class=\"form-control\" id=\"dr_executives\" onChange=\"document.form1.submit()\">");
		Str.append("<option value=\"0\">Select</option>");
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_insur = 1"
					// + " AND emp_branch_id =" + branch_id
					+ ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql emp====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");

			}
			Str.append("</select>");
			crs.close();
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
