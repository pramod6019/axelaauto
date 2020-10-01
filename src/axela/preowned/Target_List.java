/*Saiman 26th March 2013 , 29th march 2013
 * Ved Prakash (25 Feb 2013)
 * smitha nag 29 march 2013
 */
package axela.preowned;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_List extends Connect {

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
	public String preownedtarget_enquiry_count = "";
	public String preownedtarget_eval_count = "";
	public String preownedtarget_purchase_count = "";
	public String target_enddate = "";
	public String target_startdate = "";
	public String preownedtargetid = "0";
	public String preownedenquirycount = "0";
	public String preownedevalcount = "0";
	public String preownedpurchasecount = "0";
	public int total_preowned_enquiry_count = 0, total_preowned_eval_count = 0;
	public int total_preowned_purchase_count = 0;
	public String QueryString = "";
	public String ExeAccess = "";
	public String smartarr[][] = {};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_preowned_target_access", request, response);
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
				StrHTML = ListData(request);
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

	public String ListData(HttpServletRequest request) throws SQLException {
		StringBuilder Str = new StringBuilder();
		if (!emp_id.equals("0")) {
			String tid[];
			String tsdate[];
			String tedate[];
			String tenquirycount[];
			String tenquiryevalcount[];
			String tpurchasecount[];

			StrSql = "SELECT preownedtarget_id,"
					+ " COALESCE(preownedtarget_startdate, '') AS preownedtarget_startdate,"
					+ " COALESCE(preownedtarget_enddate, '') AS preownedtarget_enddate,"
					+ " COALESCE(preownedtarget_enquiry_count, 0) AS preownedtarget_enquiry_count,"
					+ " COALESCE(preownedtarget_enquiry_eval_count, 0) AS preownedtarget_enquiry_eval_count,"
					+ " COALESCE(preownedtarget_purchase_count, 0) AS preownedtarget_purchase_count";

			CountSql = "SELECT COUNT(DISTINCT preownedtarget_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_target"
					+ " WHERE preownedtarget_emp_id = " + emp_id + ""
					+ " AND SUBSTR(preownedtarget_startdate, 1, 8) >= " + year + "0101"
					+ " AND SUBSTR(preownedtarget_enddate, 1, 8)<= " + year + "1231";
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			SOP("target list query ======= " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				int ind = 0;
				int index = Integer.parseInt(ExecuteQuery(CountSql));
				tid = new String[index];
				tsdate = new String[index];
				tedate = new String[index];
				tenquirycount = new String[index];
				tenquiryevalcount = new String[index];
				tpurchasecount = new String[index];
				while (crs.next()) {
					tid[ind] = crs.getString("preownedtarget_id");
					tsdate[ind] = crs.getString("preownedtarget_startdate");
					tedate[ind] = crs.getString("preownedtarget_enddate");
					tenquirycount[ind] = crs.getString("preownedtarget_enquiry_count");
					tenquiryevalcount[ind] = crs.getString("preownedtarget_enquiry_eval_count");
					tpurchasecount[ind] = crs.getString("preownedtarget_purchase_count");
					ind++;
				}

				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th >Month</th>\n");
				Str.append("<th>Pre-Owned Count</th>\n");
				Str.append("<th data-hide=\"phone\">Evaluation</th>\n");
				Str.append("<th data-hide=\"phone\">Purchase Count</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				for (int i = 1; i <= 12; i++) {
					count = count + 1;
					for (int j = 0; j < tsdate.length; j++) {
						if (tsdate[j].equals(year + doublenum(i) + "01000000")) {
							preownedtargetid = tid[j];
							preownedenquirycount = tenquirycount[j];
							preownedevalcount = tenquiryevalcount[j];
							preownedpurchasecount = tpurchasecount[j];
							break;
						} else {
							preownedtargetid = "0";
							preownedenquirycount = "0";
							preownedevalcount = "0";
							preownedpurchasecount = "0";
						}
					}

					Str.append("<tr>");
					Str.append("<td align=center>").append(count).append("</td>\n");
					Str.append("<td>").append(TextMonth(i - 1)).append("-").append(year);
					Str.append("<input name=\"txt_target_id_").append(count)
							.append("\" type=\"hidden\" size=\"10\" maxlength=\"10\" value=")
							.append(preownedtargetid).append(">");
					Str.append("</td>\n");
					Str.append("<td align=right>").append(preownedenquirycount).append("</td>\n");
					Str.append("<td align=right>").append(preownedevalcount).append("</td>\n");
					Str.append("<td align=right>").append(preownedpurchasecount).append("</td>\n");
					Str.append("<td align=left>");
					Str.append("<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'>"
							+ "<button type=button style='margin: 0' class='btn btn-success'>"
							+ "<i class='fa fa-pencil'></i></button>"
							+ "<ul class='dropdown-content dropdown-menu pull-right'>"
							+ "<li role=presentation><a href=\"target-update.jsp?update=yes&preownedtarget_id=" + preownedtargetid
							+ "&preownedtarget_exe_id=" + emp_id + "&year=" + year
							+ "&month=" + i + "\">Update Target</a></li></ul></div></center></div></td>\n");
					Str.append("</tr>\n");

					total_preowned_enquiry_count += Integer.parseInt(preownedenquirycount);
					total_preowned_eval_count += Integer.parseInt(preownedevalcount);
					total_preowned_purchase_count += Integer.parseInt(preownedpurchasecount);
				}
				Str.append("<tr>\n");
				Str.append("<td>\n</td>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b>").append(total_preowned_enquiry_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_preowned_eval_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_preowned_purchase_count).append("</b></td>\n");
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
		if (emp_id.equals("0")) {
			msg = "Select Pre-Owned Consultant!";
		}
	}

	public String PopulateEmp() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_preowned = 1"
					+ ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
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
