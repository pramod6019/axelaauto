package axela.service;
//sangita
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_VehFollowup_LostCase extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "0";
	public String comp_id = "0";
	public String[] exe_ids, preownedmodel_ids;
	public String exe_id = "", model_id = "";
	public String StrHTML = "", StrClosedHTML = "";
	// public String BranchAccess = "";
	public String StrSearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String StrSql = "", StrSmart = "", filter = "";
	public MIS_Check1 mischeck = new MIS_Check1();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				filter = PadQuotes(request.getParameter("filter"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch += ExeAccess;

					if (!starttime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(vehfollowup_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " AND SUBSTR(vehfollowup_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND vehfollowup_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND variant_preownedmodel_id IN (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrClosedHTML = LostCaseSummary();
						StrHTML = LostCaseDetails();
					}
				}
				if (filter.equals("yes")) {
					LostcaseSearchDetails(request, response);
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

		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");

		model_id = RetrunSelectArrVal(request, "dr_model");
		preownedmodel_ids = request.getParameterValues("dr_model");
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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String LostCaseSummary() {
		int total = 0;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehlostcase1_name, COUNT(vehlostcase1_id) AS Total";
			String CountSql = " SELECT COUNT(vehlostcase1_id)";
			String StrJoin = " FROM " + compdb(comp_id) + "axela_service_followup_lostcase1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_vehlostcase1_id = vehlostcase1_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = vehfollowup_emp_id "
					+ " WHERE 1 = 1"
					+ StrSearch + "";
			CountSql = CountSql + StrJoin;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			StrJoin = StrJoin + " GROUP BY vehlostcase1_id "
					+ " ORDER BY Total DESC";
			StrSql = StrSql + StrJoin;
			SOP("StrSql-------LostCaseSummary-------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 width=\"100%\" cellpadding=0 class=\"listtable\">");
				Str.append("<tr>\n");
				Str.append("<th>Lost Case</th>");
				Str.append("<th width=\"10%\">%</th>");
				Str.append("<th width=\"10%\">Total</th>");
				Str.append("</tr>");
				while (crs.next()) {
					total = total + crs.getInt("Total");

					Str.append("<tr>");
					Str.append("<td>").append(crs.getString("vehlostcase1_name")).append("</td>");
					Str.append("<td align=right>").append(getPercentage(crs.getInt("Total"), total)).append("</td>");
					Str.append("<td align=right>").append(crs.getString("Total")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td align=right>&nbsp;</td>");
				Str.append("<td align=right><b>Total: </b></td>");
				Str.append("<td align=right><b>").append(total).append("</b></td>");
				Str.append("</tr>");
				Str.append("</table>");
				crs.beforeFirst();
				chart_data = "[";
				while (crs.next()) {
					count++;
					chart_data = chart_data + "{'type': '" + crs.getString("vehlostcase1_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total = chart_data_total + crs.getInt("Total");
					// SOP("chart_data_total------" + chart_data_total);
					if (count < TotalRecords) {
						chart_data = chart_data + ",";
					} else {
					}
				}
				chart_data = chart_data + "]";
				SOP("chart_data = " + chart_data);
			} else {
				msg = msg + "<br> No Vehicle Follow-up Lost Case Summary Found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String LostCaseDetails() {
		StringBuilder Str = new StringBuilder();
		String strtitle = "", strjoin = "";
		int[] losatcasetotal;
		int[] lostcasetotallink;
		int count = 0;
		int colcount = 0;
		int vehcount = 0;
		try {

			StrSql = "SELECT vehlostcase1_id, vehlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_service_followup_lostcase1"
					+ " WHERE 1 = 1";

			StrSql += " GROUP BY vehlostcase1_id"
					+ " ORDER BY vehlostcase1_name";

			SOP("StrSql==1111==" + StrSql);
			CachedRowSet crs1 = processQuery(StrSql, 0);

			strtitle = "SELECT emp_id, emp_name,vehcount";
			strjoin = " FROM " + compdb(comp_id) + "axela_emp INNER JOIN ( SELECT COUNT(DISTINCT veh_id) AS vehcount, vehfollowup_emp_id";
			while (crs1.next()) {
				count++;
				strtitle += "," + "Lostcase" + count + " AS '" + crs1.getString("vehlostcase1_name") + "'";
				strjoin += ", SUM(IF(vehlostcase1_id = " + crs1.getString("vehlostcase1_id") + ", 1, 0)) AS Lostcase" + count;

			}
			colcount = count + 1;
			crs1.beforeFirst();
			lostcasetotallink = new int[colcount];
			int k = 0;
			while (crs1.next()) {
				// for (int i = 0; i < colcount; i++) {
				lostcasetotallink[k++] = crs1.getInt("vehlostcase1_id");
				// }
			}
			// SOP("colcount==" + colcount);
			losatcasetotal = new int[colcount];
			strjoin += " FROM " + compdb(comp_id) + "axela_service_followup_lostcase1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_vehlostcase1_id = vehlostcase1_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id "
					+ " WHERE 1 = 1"
					+ StrSearch
					+ " GROUP BY vehfollowup_emp_id";
			strjoin += ") AS tbllostcase ON tbllostcase.vehfollowup_emp_id = emp_id";
			StrSql = strtitle + strjoin;
			// SOP("StrSql====" + StrSql);
			// colcount = count + 3;
			// losatcasetotal = new int[colcount];

			// SOP("StrSql-------LostCaseDetails---------" + StrSqlBreaker(StrSql));
			Str.append("<div class=\"table \">\n");
			Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("<th data-toggle=\"true\">#</th>\n");
			Str.append("<th>Executive</th>\n");
			Str.append("<th>Vehicles</th>\n");
			crs1.beforeFirst();
			while (crs1.next()) {
				Str.append("<th data-hide=\"phone, tablet\" align=center>");
				Str.append(crs1.getString("vehlostcase1_name"));
				Str.append("</th>\n");
			}
			// Str.append("<th data-hide=\"phone, tablet\" align=center>");
			// Str.append("Total");
			// Str.append("</th>\n");

			Str.append("</tr>");
			Str.append("</thead>\n");

			// Close the CRs for CRM concern Desc
			CachedRowSet crs2 = processQuery(StrSql, 0);
			count = 0;

			crs2.beforeFirst();
			Str.append("<tbody>\n");
			count = 0;

			while (crs2.next()) {
				crs1.beforeFirst();
				count++;
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>").append(count).append("</b></td>\n");
				Str.append("<td align=left>").append(crs2.getString("emp_name")).append("</td>\n");

				int rowtotal = 0;
				// SOP("colcount==" + colcount);
				for (int i = 3; i <= colcount + 2; i++) {
					// int j = 0;
					Str.append("<td align=right>");
					// Str.append(crs2.getString(i));
					Str.append("<a href=../service/report-vehfollowup-lostcase.jsp?filter=yes&emp_id=").append(crs2.getString("emp_id"));
					if (i != 3) {
						Str.append("&vehfollowup_vehlostcase1_id=" + lostcasetotallink[i - 4]);
					}
					Str.append("&starttime=" + starttime);
					Str.append("&endtime=" + endtime);
					Str.append("&model_id=" + model_id);
					// SOP("int===" + losatcasetotallink[i - 3]);
					Str.append(">").append(crs2.getString(i)).append("</a>");
					Str.append("</td>\n");
					rowtotal += crs2.getInt(i);
					losatcasetotal[i - 3] += crs2.getInt(i);
				}
				vehcount += crs2.getInt(3);
				// Str.append("<td align=right><b>").append(rowtotal - crs2.getInt(3)).append("</b></td>\n");
				Str.append("</tr>");
			}

			Str.append("<tr align=center>\n");

			Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
			int grandtotal = 0;
			for (int i = 3; i <= colcount + 2; i++) {
				Str.append("<td data-hide=\"phone, tablet\" align=right><b><a href=../service/report-vehfollowup-lostcase.jsp?filter=yes&");

				if (i != 3) {
					Str.append("&vehfollowup_vehlostcase1_id=" + lostcasetotallink[i - 4]);
				}
				Str.append("&starttime=" + starttime);
				Str.append("&endtime=" + endtime);
				Str.append("&model_id=" + model_id);
				Str.append(">").append(losatcasetotal[i - 3]).append("</a>");
				Str.append("</b></td>\n");
				grandtotal += losatcasetotal[i - 3];
			}
			// Str.append("<td align=right><b>" + (grandtotal - vehcount) + "</b></td>\n");
			Str.append("</tr>");
			Str.append("</table>");
			Str.append("</div>");

			return Str.toString();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	private void LostcaseSearchDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			// HttpSession session = request.getSession(true);
			String emp_id = PadQuotes(request.getParameter("emp_id"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String model_id = PadQuotes(request.getParameter("model_id"));
			String vehfollowup_vehlostcase1_id = CNumeric(PadQuotes(request.getParameter("vehfollowup_vehlostcase1_id")));

			StrSmart += " AND veh_id IN (SELECT vehfollowup_veh_id FROM " + compdb(comp_id) + "axela_service_followup" + " WHERE 1=1 ";
			if (!emp_id.equals("")) {
				StrSmart += " AND vehfollowup_emp_id IN ( " + emp_id + ")";
			}

			if (!vehfollowup_vehlostcase1_id.equals("0")) {
				StrSmart += " AND vehfollowup_vehlostcase1_id =" + vehfollowup_vehlostcase1_id;
			}
			else {
				StrSmart += " AND vehfollowup_vehlostcase1_id != 0";
			}
			StrSmart += " AND SUBSTR(vehfollowup_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(vehfollowup_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";

			if (!model_id.equals("")) {
				StrSmart += " AND variant_preownedmodel_id IN ( " + model_id + ")";
			}

			SetSession("vehstrsql", StrSmart, request);
			response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCRMExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_crm = 1"
					+ " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Service Advisor</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("insuremp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name"
					+ " FROM axelaauto.axela_preowned_model"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_preownedmodel_id = preownedmodel_id"
					+ " WHERE 1 = 1"
					+ " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id"));
				Str.append(ArrSelectdrop(crs.getInt("preownedmodel_id"), preownedmodel_ids));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
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
