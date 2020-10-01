package axela.service;
//divya 30th may 2013
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Psfcomplaint_Analysis extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", comp_id = "0", jc_branch_id = "";
	public String brand_id = "", region_id = "";
	public String[] brand_ids, region_ids, exe_ids, jc_branch_ids, crm_emp_ids, jc_emp_ids, psfdays_ids, jctype_ids, preownedmodel_ids;
	public String team_id = "", crm_emp_id = "", jc_emp_id = "", psfdays_id = "", jctype_id = "", preownedmodel_id = "";
	public String StrHTML = "", StrHTML1 = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", PsfStrSearch = "", CrmStrSearch = "";
	public String BookingSearchStr = "", FollowupSearchStr = "", StrSearchmodel = "";
	public String EmpSearch = "";
	public String ExeAccess = "";
	public String go = "";
	public String bookingvehcount = "";
	public int TotalRecords = 0;
	public String StrSql = "";
	public String TeamJoin = "";
	public String emp_all_exe = "";
	public String chart_data = "";
	public String technicianchart_data = "";
	public int chart_data_total = 0;
	public int technicianchart_data_total = 0;
	HashMap<String, Integer> concernname = new HashMap<String, Integer>();
	HashMap<String, Integer> techconcernname = new HashMap<String, Integer>();
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	// public String crmfollowupdays_id = "";
	DecimalFormat deci = new DecimalFormat("###.##");
	@SuppressWarnings("deprecation")
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				bookingvehcount = PadQuotes(request.getParameter("bookingvehcount"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {

					StrSearch += BranchAccess.replace("branch_id", "jc_branch_id");

					if (!brand_id.equals("") && jc_branch_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!jc_branch_id.equals("")) {
						StrSearch += " AND jc_branch_id IN (" + jc_branch_id + ")";
					}
					if (!preownedmodel_id.equals("")) {
						StrSearch += " AND variant_preownedmodel_id IN (" + preownedmodel_id + ")";
					}
					if (!starttime.equals("")) {
						StrSearch += " AND SUBSTR(jc_time_out, 1, 8) >= SUBSTR('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(jc_time_out, 1, 8) <= SUBSTR('" + endtime + "',1,8)";
					}
					if (!jc_emp_id.equals("")) {
						PsfStrSearch = " AND jc_emp_id IN (" + jc_emp_id + ")";
					}
					if (!psfdays_id.equals("")) {
						StrSearch += " AND jcpsf_psfdays_id IN (" + psfdays_id + ")";
					}
					if (!jctype_id.equals("")) {
						StrSearch += " AND jc_jctype_id IN (" + jctype_id + ")";
					}
					if (!crm_emp_id.equals("")) {
						CrmStrSearch = " AND jcpsf_emp_id IN (" + crm_emp_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = AdviserFollowupDetails();
						StrHTML1 = TechFollowupDetails();

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
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		jc_branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		jc_branch_ids = request.getParameterValues("dr_branch_id");

		jc_emp_id = RetrunSelectArrVal(request, "dr_jc_emp_id");
		jc_emp_ids = request.getParameterValues("dr_jc_emp_id");

		crm_emp_id = RetrunSelectArrVal(request, "dr_emp_id");
		crm_emp_ids = request.getParameterValues("dr_emp_id");
		psfdays_id = RetrunSelectArrVal(request, "dr_crmdays_id");
		psfdays_ids = request.getParameterValues("dr_crmdays_id");
		jctype_id = RetrunSelectArrVal(request, "dr_jc_jctype_id");
		jctype_ids = request.getParameterValues("dr_jc_jctype_id");
		preownedmodel_id = RetrunSelectArrVal(request, "dr_model_id");
		preownedmodel_ids = request.getParameterValues("dr_model_id");
	}

	protected void CheckForm() {
		msg = "";
		// if (dr_branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }
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
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String AdviserFollowupDetails() {
		try {
			String StrSqlcount = "";
			int count = 0;
			double jccount = 0.0;
			double psfcount = 0.0;
			double satisfied = 0.0;
			double dissatisfied = 0.0;
			double grandsatisfied = 0.0;
			double granddissatisfied = 0.0;
			double contactable = 0.0;

			int grandjccount = 0;
			int grandpsfcount = 0;
			int grandcontactable = 0;
			double rowcount = 0;
			double totalrowcount = 0;

			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT COALESCE(jcpsfconcern_id,0) AS 'jcpsfconcern_id',"
					+ " COALESCE(jcpsfconcern_desc,'') AS 'jcpsfconcern_desc',"
					+ " COUNT(DISTINCT jcpsfconcern_id) AS  'concerntypecount'"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_concern "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jcpsfconcern_id = jcpsfconcern_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id=jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id "
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1"
					+ StrSearch
					+ PsfStrSearch
					+ CrmStrSearch;

			StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");

			StrSql += " GROUP BY jcpsfconcern_id"
					+ " ORDER BY jcpsfconcern_desc";

			CachedRowSet crstrconcernsql = processQuery(StrSql, 0);
			// SOP("crstrconcernsql---111----" + StrSql);
			while (crstrconcernsql.next()) {

				StrSqlcount += " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 2 AND jcpsf_jcpsfconcern_id =" + crstrconcernsql.getString("jcpsfconcern_id") + " THEN jcpsf_id END )"
						+ " AS '" + crstrconcernsql.getString("jcpsfconcern_desc") + "',";

			}
			StrSql = " SELECT emp_id, emp_name, "
					+ " COUNT(DISTINCT jc_id) AS jccount, "
					+ " COUNT(DISTINCT jcpsf_id) AS 'psfcount',"
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 2 AND jcpsf_jcpsfconcern_id != 0 THEN jcpsf_id END) AS 'dissatisfied',"
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 1 THEN jcpsf_id END) AS 'satisfied', "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id = 1 THEN jcpsf_id END) AS 'contactable',"
					+ StrSqlcount + "'test'"
					+ " FROM " + compdb(comp_id) + "axela_service_veh "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_veh_id = veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id "
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1"
					+ StrSearch
					+ PsfStrSearch
					+ CrmStrSearch;

			StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");

			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql------222----------------" + StrSql);
			// + " LIMIT 1000";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered  table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" style=\"text-align:center\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Service Advisor</th>\n");
				Str.append("<th>Total PSF</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Contactable</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Dis Satisfied</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Dis Satisfied %</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Satisfied</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Satisfied %</th>\n");
				crstrconcernsql.beforeFirst();
				while (crstrconcernsql.next()) {
					Str.append("<th data-hide=\"phone\">" + crstrconcernsql.getString("jcpsfconcern_desc") + "</th>\n");
				}
				Str.append("<th data-hide=\"phone\">Total</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				HashMap<String, Integer> concerntotal = new HashMap<>();
				crstrconcernsql.beforeFirst();

				while (crstrconcernsql.next()) {
					// for (int i = 1; i <=
					// strconcernsql.getInt("concerntypecount"); i++) {
					concerntotal.put(crstrconcernsql.getString("jcpsfconcern_desc"), 0);
					// }
				}
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					jccount = crs.getInt("jccount");
					psfcount = crs.getInt("psfcount");
					dissatisfied = crs.getInt("dissatisfied");
					satisfied = crs.getInt("satisfied");
					contactable = crs.getInt("contactable");
					grandjccount += jccount;
					grandpsfcount += psfcount;
					grandsatisfied += satisfied;
					granddissatisfied += dissatisfied;
					grandcontactable += crs.getInt("contactable");
					count++;
					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name"))
							.append("</a></td>\n");
					Str.append("<td align=right>").append(crs.getString("psfcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("contactable")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("dissatisfied")).append("</td>\n");
					Str.append("<td align=right>").append(Double.parseDouble(getPercentage((double) dissatisfied, (double) contactable))).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("satisfied")).append("</td>\n");
					Str.append("<td align=right>").append(Double.parseDouble(getPercentage((double) satisfied, (double) contactable))).append("</td>\n");
					crstrconcernsql.beforeFirst();
					while (crstrconcernsql.next()) {
						rowcount += crs.getInt(crstrconcernsql.getString("jcpsfconcern_desc"));

						// for (int i = 1; i <=
						// strconcernsql.getInt("concerntypecount"); i++) {

						Str.append("<td align=right>").append(crs.getInt(crstrconcernsql.getString("jcpsfconcern_desc"))).append("</td>\n");
						concerntotal.put(crstrconcernsql.getString("jcpsfconcern_desc") + "",
								concerntotal.get(crstrconcernsql.getString("jcpsfconcern_desc") + "") + crs.getInt(crstrconcernsql.getString("jcpsfconcern_desc")));

						// }

					}
					Str.append("<td align=right>").append(rowcount).append("</td>\n");
					Str.append("</tr>");
					totalrowcount += rowcount;
					rowcount = 0;
				}
				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(grandpsfcount).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append(grandcontactable).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append((int) granddissatisfied).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append(Double.parseDouble(getPercentage((double) granddissatisfied, (double) grandcontactable))).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append((int) grandsatisfied).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append(Double.parseDouble(getPercentage((double) grandsatisfied, (double) grandcontactable))).append("</b></td>\n");
				crstrconcernsql.beforeFirst();
				while (crstrconcernsql.next()) {
					// for (int i = 1; i <=
					// strconcernsql.getInt("concerntypecount"); i++) {
					Str.append("<td align=right>").append("" + concerntotal.get(crstrconcernsql.getString("jcpsfconcern_desc")) + "</td>\n");
					concernname.put(crstrconcernsql.getString("jcpsfconcern_desc"), concerntotal.get(crstrconcernsql.getString("jcpsfconcern_desc")));
					// }
				}
				Str.append("<td align=right><b>").append(totalrowcount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				// Str.append("</div>\n");
				PreparePieChart();
			} else {
				Str.append("<font color=red><center><b>No Service Advisor found!</b></center></font><br>");
			}
			crstrconcernsql.close();
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PreparePieChart() {
		chart_data = "[";
		for (String str : concernname.keySet()) {

			chart_data = chart_data + "{'type': '" + str + "', 'total':" + concernname.get(str) + "}";
			chart_data_total = chart_data_total + concernname.get(str);

			chart_data = chart_data + ",";

		}
		chart_data = chart_data + "]";
		return "";

	}

	public String TechFollowupDetails() {
		try {
			String StrSqlcount = "";
			int count = 0;
			double jccount = 0.0;
			double psfcount = 0.0;
			double satisfied = 0.0;
			double dissatisfied = 0.0;
			double grandsatisfied = 0.0;
			double granddissatisfied = 0.0;
			double contactable = 0.0;

			int grandjccount = 0;
			int grandpsfcount = 0;
			int grandcontactable = 0;
			double rowcount = 0;
			double totalrowcount = 0;

			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT COALESCE(jcpsfconcern_id,0) AS 'jcpsfconcern_id',"
					+ " COALESCE(jcpsfconcern_desc,'') AS 'jcpsfconcern_desc',"
					+ " COUNT(DISTINCT jcpsfconcern_id) AS  'concerntypecount'"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_concern "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jcpsfconcern_id = jcpsfconcern_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_technician_emp_id "
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1"
					+ StrSearch
					+ PsfStrSearch
					+ CrmStrSearch;

			StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");

			StrSql += " GROUP BY jcpsfconcern_id"
					+ " ORDER BY jcpsfconcern_desc";
			// SOP("StrSql----333---" + StrSql);
			CachedRowSet crstrconcernsql = processQuery(StrSql, 0);
			while (crstrconcernsql.next()) {
				StrSqlcount += " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 2 AND jcpsf_jcpsfconcern_id =" + crstrconcernsql.getString("jcpsfconcern_id") + " THEN jcpsf_id END )"
						+ " AS '" + crstrconcernsql.getString("jcpsfconcern_desc") + "',";
			}
			StrSql = " SELECT emp_id, emp_name, "
					+ " COUNT(DISTINCT jc_id) AS jccount, "
					+ " COUNT(DISTINCT jcpsf_id) AS 'psfcount',"
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 2 AND jcpsf_jcpsfconcern_id !=0 THEN jcpsf_id END) AS 'dissatisfied',"
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 1 THEN jcpsf_id END) AS 'satisfied', "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id = 1 THEN jcpsf_id END) AS 'contactable',"
					+ StrSqlcount + "'test'"
					+ " FROM " + compdb(comp_id) + "axela_service_veh "
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_veh_id = veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_jc_id = jc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_technician_emp_id "
					+ " WHERE 1 = 1"
					+ " AND emp_active = 1"
					+ StrSearch
					+ PsfStrSearch
					+ CrmStrSearch;

			StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");

			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// + " LIMIT 1000";

			// SOP("StrSql====444=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead>");
				Str.append("<tr>\n");
				Str.append("<th data-hide=\"phone\" style=\"text-align:center\">#</th>\n");
				Str.append("<th data-toggle=\"true\">Technician</th>\n");
				Str.append("<th>Total PSF</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Contactable</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Dis Satisfied</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Dis Satisfied %</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Satisfied</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Satisfied %</th>\n");
				crstrconcernsql.beforeFirst();
				while (crstrconcernsql.next()) {
					Str.append("<th data-hide=\"phone\">" + crstrconcernsql.getString("jcpsfconcern_desc") + "</th>\n");
				}
				Str.append("<th data-hide=\"phone\">Total</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				HashMap<String, Integer> concerntotal = new HashMap<>();
				crstrconcernsql.beforeFirst();

				while (crstrconcernsql.next()) {
					// for (int i = 1; i <=
					// strconcernsql.getInt("concerntypecount"); i++) {
					concerntotal.put(crstrconcernsql.getString("jcpsfconcern_desc"), 0);
					// }
				}
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					jccount = crs.getInt("jccount");
					psfcount = crs.getInt("psfcount");
					dissatisfied = crs.getInt("dissatisfied");
					satisfied = crs.getInt("satisfied");
					contactable = crs.getInt("contactable");
					grandjccount += jccount;
					grandpsfcount += psfcount;
					grandsatisfied += satisfied;
					granddissatisfied += dissatisfied;
					grandcontactable += crs.getInt("contactable");
					count++;
					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id"))
							.append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td align=right>").append(crs.getString("psfcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("contactable")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("dissatisfied")).append("</td>\n");
					Str.append("<td align=right>").append(Double.parseDouble(getPercentage((double) dissatisfied, (double) contactable))).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("satisfied")).append("</td>\n");
					Str.append("<td align=right>").append(Double.parseDouble(getPercentage((double) satisfied, (double) contactable))).append("</td>\n");

					crstrconcernsql.beforeFirst();
					while (crstrconcernsql.next()) {
						rowcount += crs.getInt(crstrconcernsql.getString("jcpsfconcern_desc"));

						// for (int i = 1; i <=
						// strconcernsql.getInt("concerntypecount"); i++) {

						Str.append("<td align=right>").append(crs.getInt(crstrconcernsql.getString("jcpsfconcern_desc"))).append("</td>\n");
						concerntotal.put(crstrconcernsql.getString("jcpsfconcern_desc") + "",
								concerntotal.get(crstrconcernsql.getString("jcpsfconcern_desc") + "") + crs.getInt(crstrconcernsql.getString("jcpsfconcern_desc")));

						// }

					}
					Str.append("<td align=right>").append(rowcount).append("</td>\n");
					Str.append("</tr>");
					totalrowcount += rowcount;
					rowcount = 0;
				}
				Str.append("<tr>\n");
				Str.append("<td data-hide=\"phone\" align=right colspan=\"2\"><b>Total: </b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append(grandpsfcount).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append((int) grandcontactable).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append((int) granddissatisfied).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append(Double.parseDouble(getPercentage((double) granddissatisfied, (double) grandcontactable))).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append((int) grandsatisfied).append("</b></td>\n");
				Str.append("<td data-hide=\"phone\" align=right><b>").append(Double.parseDouble(getPercentage((double) grandsatisfied, (double) grandcontactable))).append("</b></td>\n");
				crstrconcernsql.beforeFirst();
				while (crstrconcernsql.next()) {
					// for (int i = 1; i <=
					// strconcernsql.getInt("concerntypecount"); i++) {
					Str.append("<td align=right><b>").append("" + concerntotal.get(crstrconcernsql.getString("jcpsfconcern_desc")) + "</b></td>\n");
					techconcernname.put(crstrconcernsql.getString("jcpsfconcern_desc"), concerntotal.get(crstrconcernsql.getString("jcpsfconcern_desc")));
					// }
				}
				Str.append("<td align=right><b>").append(totalrowcount).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

				PrepareTechnicianPieChart();
			} else {
				Str.append("<font color=red><center><b>No Technician found!</b></center></font><br>");
			}
			crstrconcernsql.close();
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PrepareTechnicianPieChart() {
		technicianchart_data = "[";
		for (String str : techconcernname.keySet()) {

			technicianchart_data = technicianchart_data + "{'type': '" + str + "', 'total':" + techconcernname.get(str) + "}";
			technicianchart_data_total = technicianchart_data_total + techconcernname.get(str);

			technicianchart_data = technicianchart_data + ",";

		}
		technicianchart_data = technicianchart_data + "]";
		return "";

	}

	public String PopulateAdviser() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, emp_name "
					+ " from " + compdb(comp_id) + "axela_emp"
					+ " INNER  JOIN " + compdb(comp_id) + "axela_service_jc ON jc_emp_id = emp_id"
					+ " WHERE emp_active = 1"
					// + " AND emp_service = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql--PopulateAdviser----" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), jc_emp_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n)");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id IN (3)"
					// + " AND branch_branchtype_id = 3"
					+ BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			if (!region_id.equals("")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql------PopulateBranches-----" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch id=dr_branch class=textbox multiple=multiple size=10 onchange=\"PopulateExecutives();PopulateTeams();PopulateCRMDays();\"  style=\"width:250px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobcardType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_jctype_id = jctype_id"
					+ " WHERE 1 = 1";
			// if (!jc_branch_id.equals("")) {
			// }
			StrSql += " GROUP BY jctype_id"
					+ " ORDER BY jctype_name";
			// SOP("strsql==PSFDays=======" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("jctype_id"), jctype_ids));
				Str.append(">").append(crs.getString("jctype_name")).append("</option> \n");
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
