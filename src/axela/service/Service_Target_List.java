/*Saiman 26th March 2013 , 29th march 2013
 * Ved Prakash (25 Feb 2013)
 * smitha nag 29 march 2013
 */
package axela.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Service_Target_List extends Connect {

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
	public String servicetargetid = "0";
	public String servicejccount = "0", servicepmsamount = "0";
	public String servicelabouramount = "0";
	public String servicetlabouramount = "0";
	public String servicepartamount = "0";
	public String servicetyrecount = "0";
	public String servicetyreamount = "0";
	public String servicebreakcount = "0";
	public String servicebreakamount = "0";
	public String servicebatterycount = "0";
	public String servicebatteryamount = "0";
	public String serviceoilamount = "0";
	public String serviceaccessamount = "0";
	public String amt = "0", serviceextwarrantycount = "0", serviceextwarrantyamount = "0";
	public String servicewheelalignmentamount = "0", servicecngamount = "0";
	public int total_service_jc_count = 0, total_service_pms_count = 0;
	public double total_service_labour_amount = 0, total_service_part_amount = 0;
	public double total_service_oil_amount = 0, total_service_tyre_count = 0, total_service_tyre_amount = 0;
	public double total_service_break_count = 0, total_service_break_amount = 0, total_service_battery_count = 0, total_service_battery_amount = 0;
	public double total_enquiry_hot_count = 0, total_service_access_amount = 0;
	public double total_service_vas_amount = 0, total_service_extwarranty_count = 0, total_service_extwarranty_amount = 0;
	public double total_service_wheelalignment_amount = 0, total_service_cng_amount = 0;
	public String QueryString = "";
	public String BranchAccess = "", ExeAccess = "";

	public String[] brand_ids, region_ids, zone_ids, branch_ids;
	public String brand_id = "", region_id = "", zone_id, model_id = "";
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();

	public String smartarr[][] = {};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "service_target_access", request, response);
			branch_id = GetSession("emp_branch_id", request);

			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				msg = PadQuotes(request.getParameter("msg"));
				// SOP("Brach Id---" + branch_id);
				brand_id = RetrunSelectArrVal(request, "dr_principal");
				brand_ids = request.getParameterValues("dr_principal");
				region_id = RetrunSelectArrVal(request, "dr_region");
				region_ids = request.getParameterValues("dr_region");
				zone_id = RetrunSelectArrVal(request, "dr_zone");
				zone_ids = request.getParameterValues("dr_zone");
				branch_id = RetrunSelectArrVal(request, "dr_branch_id");
				branch_ids = request.getParameterValues("dr_branch_id");
				emp_id = CNumeric(PadQuotes(request.getParameter("dr_executives")));

				// SOP("dr_branch==" + branch_id);
				// SOP("exe_id==" + emp_id);
				if (branch_id.equals("0")) {
					StrSql = "SELECT branch_id "
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_active = 1 "
							+ BranchAccess
							+ " AND branch_branchtype_id IN (3) LIMIT 1";
					// SOP("Get Branch ------" + StrSql);
					branch_id = ExecuteQuery(StrSql);
				}
				// SOP("Brach Id---" + branch_id);

				QueryString = PadQuotes(request.getQueryString());
				year = CNumeric(PadQuotes(request.getParameter("dr_year")));

				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				curryear = Integer.parseInt(ToLongDate(kknow()).substring(0, 4));

				// SOP("emp_id===" + emp_id);
				// SOP("year===" + year);

				if (year.equals("0")) {
					year = Integer.toString(curryear);
				}

				// SOP("year===" + year + "====dr_executives====" + emp_id);

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
			String tjccount[];
			String tpsmcount[];
			String tlabouramount[];
			String tpartsamount[];
			String toilamount[];
			String ttyrecount[];
			String ttyreamount[];
			String tbreakcount[];
			String tbreakamount[];
			String tbatterycount[];
			String tbatteryamount[];
			String taccessamount[];
			String tvasaounmt[];
			String textwarrantycount[];
			String textwarrantyamount[];
			String twheelaounmt[];
			String tcngaounmt[];

			StrSql = "SELECT service_target_id,"
					+ " COALESCE (service_target_startdate, '') AS service_target_startdate,"
					+ " COALESCE (service_target_enddate, '') AS service_target_enddate,"
					+ " COALESCE(service_target_jc_count, 0) AS service_target_jc_count,"
					+ " COALESCE(service_target_pms_count, 0) AS service_target_pms_count,"
					+ " COALESCE (service_target_labour_amount,0) AS service_target_labour_amount,"
					+ " COALESCE(service_target_parts_amount, 0) AS service_target_parts_amount,"
					+ " COALESCE(service_target_oil_amount, 0) AS service_target_oil_amount,"
					+ " COALESCE(service_target_tyre_count, 0) AS service_target_tyre_count,"
					+ " COALESCE(service_target_tyre_amount, 0) AS service_target_tyre_amount,"
					+ " COALESCE(service_target_break_count, 0) AS service_target_break_count,"
					+ " COALESCE(service_target_break_amount, 0) AS service_target_break_amount,"
					+ " COALESCE(service_target_battery_count, 0) AS service_target_battery_count,"
					+ " COALESCE(service_target_battery_amount, 0) AS service_target_battery_amount,"
					+ " COALESCE(service_target_accessories_amount, 0) AS service_target_accessories_amount,"
					+ " COALESCE(service_target_vas_amount, 0) AS service_target_vas_amount,"
					+ " COALESCE(service_target_extwarranty_count, 0) AS service_target_extwarranty_count,"
					+ " COALESCE(service_target_extwarranty_amount, 0) AS service_target_extwarranty_amount,"
					+ " COALESCE(service_target_wheelalign_amount, 0) AS service_target_wheelalign_amount,"
					+ " COALESCE(service_target_cng_amount, 0) AS service_target_cng_amount";

			CountSql = "SELECT COUNT(distinct service_target_id)";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_service_target"
					+ " WHERE service_target_emp_id=" + emp_id + ""
					+ " AND SUBSTR(service_target_startdate, 1, 8) >= " + year + "0101"
					+ " AND SUBSTR(service_target_enddate, 1, 8)<=" + year + "1231";
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
				tjccount = new String[index];
				tpsmcount = new String[index];
				tlabouramount = new String[index];
				tpartsamount = new String[index];
				toilamount = new String[index];
				ttyrecount = new String[index];
				ttyreamount = new String[index];
				tbreakcount = new String[index];
				tbreakamount = new String[index];
				tbatterycount = new String[index];
				tbatteryamount = new String[index];
				taccessamount = new String[index];
				tvasaounmt = new String[index];
				textwarrantycount = new String[index];
				textwarrantyamount = new String[index];
				twheelaounmt = new String[index];
				tcngaounmt = new String[index];
				while (crs.next()) {
					tid[ind] = crs.getString("service_target_id");
					tsdate[ind] = crs.getString("service_target_startdate");
					tedate[ind] = crs.getString("service_target_enddate");
					tjccount[ind] = String.valueOf(crs.getInt("service_target_jc_count"));
					tpsmcount[ind] = String.valueOf(crs.getDouble("service_target_pms_count"));
					tlabouramount[ind] = String.valueOf(crs.getDouble("service_target_labour_amount"));
					tpartsamount[ind] = String.valueOf(crs.getDouble("service_target_parts_amount"));
					toilamount[ind] = String.valueOf(crs.getDouble("service_target_oil_amount"));
					ttyrecount[ind] = String.valueOf(crs.getDouble("service_target_tyre_count"));
					ttyreamount[ind] = String.valueOf(crs.getDouble("service_target_tyre_amount"));
					tbreakcount[ind] = String.valueOf(crs.getDouble("service_target_break_count"));
					tbreakamount[ind] = String.valueOf(crs.getDouble("service_target_break_amount"));
					tbatterycount[ind] = String.valueOf(crs.getDouble("service_target_battery_count"));
					tbatteryamount[ind] = String.valueOf(crs.getDouble("service_target_battery_amount"));
					taccessamount[ind] = String.valueOf(crs.getDouble("service_target_accessories_amount"));
					tvasaounmt[ind] = String.valueOf(crs.getDouble("service_target_vas_amount"));
					textwarrantycount[ind] = String.valueOf(crs.getDouble("service_target_extwarranty_count"));
					textwarrantyamount[ind] = String.valueOf(crs.getDouble("service_target_extwarranty_amount"));
					twheelaounmt[ind] = String.valueOf(crs.getDouble("service_target_wheelalign_amount"));
					tcngaounmt[ind] = String.valueOf(crs.getDouble("service_target_cng_amount"));
					ind++;
				}

				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Month</th>\n");
				Str.append("<th>JobCard Count</th>\n");
				Str.append("<th >PMS Count</th>\n");
				Str.append("<th>Labour Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Parts Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Oil Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Tyre Count</th>\n");
				Str.append("<th data-hide=\"phone\">Tyre Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Brake Count </th>\n");
				Str.append("<th data-hide=\"phone\">Brake Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Battery Count</th>\n");
				Str.append("<th data-hide=\"phone\">Battery Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Accessories Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">VAS Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Extented Warranty Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Extented Warranty Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Wheel Alignment Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">CNG Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				int count = 0;
				for (int i = 1; i <= 12; i++) {
					count = count + 1;
					for (int j = 0; j < tsdate.length; j++) {
						if (tsdate[j].equals(year + doublenum(i) + "01000000")) {
							servicetargetid = tid[j];
							servicejccount = tjccount[j];
							servicepmsamount = tpsmcount[j];
							servicelabouramount = tlabouramount[j];
							servicepartamount = tpartsamount[j];
							serviceoilamount = toilamount[j];
							servicetyrecount = ttyrecount[j];
							servicetyreamount = ttyreamount[j];
							servicebreakcount = tbreakcount[j];
							servicebreakamount = tbreakamount[j];
							servicebatterycount = tbatterycount[j];
							servicebatteryamount = tbatteryamount[j];
							serviceaccessamount = taccessamount[j];
							amt = tvasaounmt[j];
							serviceextwarrantycount = textwarrantycount[j];
							serviceextwarrantyamount = textwarrantyamount[j];
							servicewheelalignmentamount = twheelaounmt[j];
							servicecngamount = tcngaounmt[j];
							break;
						} else {
							servicetargetid = "0";
							servicejccount = "0";
							servicepmsamount = "0";
							servicelabouramount = "0";
							servicepartamount = "0";
							serviceoilamount = "0";
							servicetyrecount = "0";
							servicetyreamount = "0";
							servicebreakcount = "0";
							servicebreakamount = "0";
							servicebatterycount = "0";
							servicebatteryamount = "0";
							serviceaccessamount = "0";
							amt = "0";
							serviceextwarrantycount = "0";
							serviceextwarrantyamount = "0";
							servicewheelalignmentamount = "0";
							servicecngamount = "0";
						}
					}
					Str.append("<tr><td>").append(count).append("</td>\n");
					Str.append("<td>");
					Str.append(TextMonth(i - 1)).append("-").append(year);
					// Str.append("<input name=\"txt_target_id_").append(count).append("\" type=\"hidden\" size=\"10\" maxlength=\"10\" value=").append(servicetargetid).append(">");
					Str.append("</td>\n");
					Str.append("<td align=right>").append(servicejccount).append("</td>\n");
					Str.append("<td align=right>").append(servicepmsamount).append("</td>\n");
					Str.append("<td align=right>").append(servicelabouramount).append("</td>\n");
					Str.append("<td align=right>").append(servicepartamount).append("</td>\n");
					Str.append("<td align=right>").append(serviceoilamount).append("</td>\n");
					Str.append("<td align=right>").append(servicetyrecount).append("</td>\n");
					Str.append("<td align=right>").append(servicetyreamount).append("</td>\n");
					Str.append("<td align=right>").append(servicebreakcount).append("</td>\n");
					Str.append("<td align=right>").append(servicebreakamount).append("</td>\n");
					Str.append("<td align=right>").append(servicebatterycount).append("</td>\n");
					Str.append("<td align=right>").append(servicebatteryamount).append("</td>\n");
					Str.append("<td align=right>").append(serviceaccessamount).append("</td>\n");
					Str.append("<td align=right>").append(amt).append("</td>\n");
					Str.append("<td align=right>").append(serviceextwarrantycount).append("</td>\n");
					Str.append("<td align=right>").append(serviceextwarrantyamount).append("</td>\n");
					Str.append("<td align=right>").append(servicewheelalignmentamount).append("</td>\n");
					Str.append("<td align=right>").append(servicecngamount).append("</td>\n");
					Str.append("<td><a href=\"service-target-update.jsp?update=yes&service_target_id=").append(servicetargetid).append("&emp_id=").append(emp_id).append("&year=").append(year)
							.append("&month=")
							.append(i)
							.append("\">Update Target</a></td>\n");
					Str.append("</tr>\n");

					total_service_jc_count += Integer.parseInt(servicejccount);
					total_service_pms_count += Integer.parseInt(servicejccount);
					total_service_labour_amount += Double.parseDouble(servicelabouramount);
					total_service_part_amount += Double.parseDouble(servicepartamount);
					total_service_oil_amount += Double.parseDouble(serviceoilamount);
					total_service_tyre_count += Double.parseDouble(servicetyrecount);
					total_service_tyre_amount += Double.parseDouble(servicetyreamount);
					total_service_break_count += Double.parseDouble(servicebreakcount);
					total_service_break_amount += Double.parseDouble(servicebreakamount);
					total_service_battery_count += Double.parseDouble(servicebatterycount);
					total_service_battery_amount += Double.parseDouble(servicebatteryamount);
					total_service_access_amount += Double.parseDouble(serviceaccessamount);
					total_service_vas_amount += (int) Double.parseDouble(amt);
					total_service_extwarranty_count += (int) Double.parseDouble(serviceextwarrantycount);
					total_service_extwarranty_amount += (int) Double.parseDouble(serviceextwarrantyamount);
					total_service_wheelalignment_amount += (int) Double.parseDouble(servicewheelalignmentamount);
					total_service_cng_amount += (int) Double.parseDouble(servicecngamount);
				}
				Str.append("<tr>\n");
				Str.append("<td>\n</td>\n");
				Str.append("<td align=right><b>Total:</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_jc_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_pms_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_labour_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_part_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_oil_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_tyre_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_tyre_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_break_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_break_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_battery_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_battery_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_access_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_vas_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_extwarranty_count).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_extwarranty_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_wheelalignment_amount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(total_service_cng_amount).append("</b></td>\n");
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
				msg = msg + "Service Advisor!";
			}
		}
	}

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1 "
					+ " AND branch_branchtype_id = 3"
					+ BranchAccess;
			SqlStr += " ORDER BY branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	// public String PopulateEmp(String branch_id, String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// if (branch_id.equals("0")) {
	// branch_id = "-1";
	// }
	// Str.append("<select name=\"dr_executives\" class=\"form-control\" id=\"dr_executives\" onChange=\"document.form1.submit()\">");
	// Str.append("<option value=\"0\">Select</option>");
	// try {
	// StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
	// + " FROM " + compdb(comp_id) + "axela_emp"
	// + " WHERE emp_service = 1"
	// + " AND emp_branch_id =" + branch_id
	// + ExeAccess
	// + " GROUP BY emp_id"
	// + " ORDER BY emp_name";
	// SOP("StrSql emp====" + StrSql);
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("emp_id"));
	// Str.append(StrSelectdrop(crs.getString("emp_id"), emp_id));
	// Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
	//
	// }
	// Str.append("</select>");
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

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
