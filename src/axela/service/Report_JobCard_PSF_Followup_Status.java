package axela.service;
//divya 30th may 2013
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_PSF_Followup_Status extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", comp_id = "0", branch_id = "0";
	public String StrHTML = "", StrClosedHTML = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String StrSql = "";
	public String TeamJoin = "";
	public String dr_totalby = "0";
	// public String crmfollowupdays_id = "";
	public String emp_all_exe = "";

	public String[] exe_ids, brand_ids, region_ids, jc_branch_ids, crm_emp_ids, psfdays_ids, model_ids, jcpsfconcern_ids;
	public String exe_id = "", crm_emp_id = "", brand_id = "", region_id = "", psfdays_id = "", model_id = "", jcpsfconcern_id = "";
	public String jc_branch_id = "", StrHTMLpsfconcern = "";
	public String filter = "", StrSmart = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	DecimalFormat deci = new DecimalFormat("###.##");
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// for psf branch to band
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_mis_access", request, response);
			HttpSession session = request.getSession(true);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				filter = PadQuotes(request.getParameter("filter"));
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch = BranchAccess.replace("branch_id", "jc_branch_id") + "";

					if (!starttime.equals("")) {
						StrSearch += " AND jcpsf_followup_time >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						StrSearch += " AND jcpsf_followup_time <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!brand_id.equals("") && jc_branch_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!jc_branch_id.equals("")) {
						StrSearch += " AND jc_branch_id IN (" + jc_branch_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND variant_preownedmodel_id IN (" + model_id + ")";
					}

					if (!exe_id.equals("")) {
						StrSearch += " AND jc_emp_id IN (" + exe_id + ")";
					}
					if (!psfdays_id.equals("")) {
						StrSearch += " AND jcpsf_psfdays_id IN (" + psfdays_id + ")";
					}

					if (!crm_emp_id.equals("")) {
						StrSearch = " AND jcpsf_emp_id IN (" + crm_emp_id + ")";
					}

					if (!jcpsfconcern_id.equals("")) {
						StrSearch += " AND jcpsf_jcpsfconcern_id IN (" + jcpsfconcern_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						PreparePieChart();
						StrHTML = jcPsfDetails();
						StrHTMLpsfconcern = PopulatePSFConcernDetails();
					}
				}

				if (filter.equals("yes")) {
					PSfConcernSearchDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
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
		dr_totalby = CNumeric(PadQuotes(request.getParameter("dr_totalby")));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		jc_branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		jc_branch_ids = request.getParameterValues("dr_branch_id");

		// exe_id = RetrunSelectArrVal(request, "dr_executive");
		// exe_ids = request.getParameterValues("dr_executive");

		crm_emp_id = RetrunSelectArrVal(request, "dr_emp_id");
		crm_emp_ids = request.getParameterValues("dr_emp_id");

		model_id = RetrunSelectArrVal(request, "dr_model_id");
		model_ids = request.getParameterValues("dr_model_id");

		psfdays_id = RetrunSelectArrVal(request, "dr_crmdays");
		psfdays_ids = request.getParameterValues("dr_crmdays");

		jcpsfconcern_id = RetrunSelectArrVal(request, "dr_jcpsfconcern_id");
		jcpsfconcern_ids = request.getParameterValues("dr_jcpsfconcern_id");

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

	public String PreparePieChart() {
		StrSql = " SELECT psffeedbacktype_id, psffeedbacktype_name, COUNT(jcpsf_id) AS Total ";
		String CountSql = " SELECT COUNT(psffeedbacktype_id)";
		String StrJoin = " FROM axelaauto.axela_service_psf_feedbacktype"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf ON jcpsf_psffeedbacktype_id = psffeedbacktype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
				+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jcpsf_emp_id"
				+ TeamJoin
				+ " WHERE 1 = 1 "
				+ StrSearch;
		if (emp_all_exe.equals("0"))
		{
			StrJoin += ExeAccess.replace("emp_id", "jcpsf_emp_id");
		}
		CountSql = CountSql + StrJoin;
		TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
		StrJoin = StrJoin + " GROUP BY psffeedbacktype_id ORDER BY Total desc";
		StrSql = StrSql + StrJoin;
		SOP("str--" + StrSql);
		int count = 0;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				SOP("1");
				chart_data = "[";
				while (crs.next()) {
					count++;
					// chart_data = chart_data + "['" + crs.getString("psffeedbacktype_name") + " (" + crs.getString("Total") + ")'," + crs.getString("Total") + "]";
					chart_data = chart_data + "{'type': '" + crs.getString("psffeedbacktype_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total = chart_data_total + crs.getInt("Total");
					if (count < TotalRecords) {
						chart_data = chart_data + ",";
					} else {
					}
				}
				chart_data = chart_data + "]";
			} else {
				SOP("2");
				NoChart = "<center><font color=red><b>No PSF Follow-up Found!</b></font></center>";
			}
			crs.close();
		} catch (SQLException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return "";
	}

	public String PopulateSalesExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id=emp_id"
					+ " WHERE emp_active = '1'  AND emp_sales='1' "
					+ " AND (emp_branch_id = " + dr_branch_id + " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " and empbr.emp_branch_id = " + dr_branch_id + ")) " + ExeAccess + "";

			// SOP("team_id--"+team_id);
			// if (!team_id.equals("")) {
			// StrSql = StrSql + " AND teamtrans_team_id IN (" + team_id + ")";
			// }
			StrSql = StrSql + " GROUP BY emp_id ORDER BY emp_name";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_executive id=dr_executive class='form-control multiselect-dropdown' multiple=\"multiple\" size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("emp_id"), exe_ids));
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
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

	public String PopulatePSFFollowupDays() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT psfdays_id, concat(psfdays_daycount,psfdays_desc, ' Call') as psfdays_desc"
					+ " from " + compdb(comp_id) + "axela_service_jc_psfdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id "
					+ " where branch_id = " + dr_branch_id
					+ " group by psfdays_id"
					+ " order by psfdays_daycount";
			// SOP("StrSql====crmfollowupdays=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_crmdays id=dr_crmdays class='form-control multiselect-dropdown' multiple=\"multiple\" size=10>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psfdays_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("psfdays_id"), psfdays_ids));
				Str.append(">").append(crs.getString("psfdays_desc")).append("</option> \n");
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

	public String PopulateTotalBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", dr_totalby)).append(">Consultants</option>\n");
		// Str.append("<option value=2").append(StrSelectdrop("2", dr_totalby)).append(">Teams</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_totalby)).append(">Branches</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", dr_totalby)).append(">Brands</option>\n");
		// Str.append("<option value=6").append(StrSelectdrop("6", dr_totalby)).append(">Models</option>\n");
		return Str.toString();
	}

	public String jcPsfDetails() {
		try {
			int count = 0;
			double followupcount = 0;
			int grandfollowupcount = 0;

			double contactable = 0;
			int grandcontactable = 0;
			double contactable_perc = 0;
			double grandcontactable_perc = 0;

			double noncontactable = 0;
			int grandnoncontactable = 0;
			double noncontactable_perc = 0;
			double grandnoncontactable_perc = 0;

			double satisfied = 0;
			double grandsatisfied = 0;
			double grandsatisfiedperc = 0.00;
			double dissatisfied = 0;
			double granddissatisfied = 0;
			double granddissatisfiedperc = 0.00;
			double satisfied_perc = 0.00;
			double dissatisfied_perc = 0.00;
			StringBuilder Str = new StringBuilder();

			StrSql = "SELECT COUNT(jcpsf_id) as followupcount, "
					+ " COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id = 1 THEN jcpsf_id END ) as 'contactable',"
					+ " COUNT(DISTINCT CASE WHEN jcpsf_psffeedbacktype_id = 2 THEN jcpsf_id END ) as 'noncontactable',"
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 1 THEN jcpsf_id END ) as 'satisfied',"
					+ " COUNT(DISTINCT CASE WHEN jcpsf_satisfied = 2 THEN jcpsf_id END ) as 'dissatisfied',"
					+ " emp_id, emp_name, branch_name ";
			if (dr_totalby.equals("4")) {
				StrSql += " ,region_name";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " ,brand_name";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_service_jc_psf "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = jc_branch_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id";
			if (dr_totalby.equals("4")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region on region_id = branch_region_id";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " INNER JOIN axela_brand on brand_id = branch_brand_id";
			}
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = jcpsf_emp_id"
					+ TeamJoin
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ StrSearch
					+ BranchAccess.replace("branch_id", "jc_branch_id");
			StrSql += ExeAccess.replace("emp_id", "jcpsf_emp_id");
			StrSql += " GROUP BY ";
			if (dr_totalby.equals("1")) {
				StrSql += " emp_id";
			}
			else if (dr_totalby.equals("3")) {
				StrSql += " branch_id ";
			}
			else if (dr_totalby.equals("4")) {
				StrSql += " branch_region_id";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " branch_brand_id";
			}
			StrSql += " ORDER BY";

			if (dr_totalby.equals("1")) {
				StrSql += " emp_name";
			}
			else if (dr_totalby.equals("3")) {
				StrSql += " branch_name ";
			}
			else if (dr_totalby.equals("4")) {
				StrSql += " region_name";
			}
			else if (dr_totalby.equals("5")) {
				StrSql += " brand_name";
			}
			// + " LIMIT 1000";

			SOP("StrSql====CRMFollowup============" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div>\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
				Str.append("<thead>\n");
				Str.append("<tr>\n");
				Str.append("<th style=\"text-align:center\" data-hide=\"phone\">#</th>\n");
				if (dr_totalby.equals("1")) {
					Str.append("<th data-toggle=\"true\" align=center>CRM Name</th>\n");
				}
				else if (dr_totalby.equals("3")) {
					Str.append("<th data-toggle=\"true\" align=center>Branch</th>\n");
				}
				else if (dr_totalby.equals("4")) {
					Str.append("<th data-toggle=\"true\" align=center>Region</th>\n");
				}
				else if (dr_totalby.equals("5")) {
					Str.append("<th data-toggle=\"true\" align=center>Brand</th>\n");
				}
				Str.append("<th align=center>Total</th>\n");
				Str.append("<th data-hide=\"phone\">Contactable</th>\n");
				Str.append("<th data-hide=\"phone\">Contactable %</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Non Contactable</th>\n");
				Str.append("<th data-hide=\"phone,tablet\" align=center>Non Contactable %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center>Satisfied </th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Satisfied %</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied</th>\n");
				Str.append("<th data-hide=\"phone,tablet\"  align=center nowrap>Dis Satisfied %</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					satisfied = 0.00;
					contactable = 0.00;
					contactable_perc = 0.00;
					noncontactable_perc = 0.00;
					satisfied_perc = 0.00;
					dissatisfied_perc = 0.00;
					followupcount = crs.getDouble("followupcount");
					contactable = crs.getDouble("contactable");
					noncontactable = crs.getDouble("noncontactable");
					satisfied = crs.getDouble("satisfied");
					dissatisfied = crs.getDouble("dissatisfied");

					if (contactable != 0) {
						satisfied_perc = (satisfied / contactable) * 100;
						contactable_perc = (contactable / followupcount) * 100;
					}
					if (contactable != 0) {
						dissatisfied_perc = (dissatisfied / contactable) * 100;
					}
					if (noncontactable != 0) {
						noncontactable_perc = (noncontactable / followupcount) * 100;
					}
					grandfollowupcount += followupcount;
					grandcontactable += contactable;
					grandnoncontactable += noncontactable;
					grandsatisfied += satisfied;
					granddissatisfied += dissatisfied;

					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					if (dr_totalby.equals("1")) {
						Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</td>\n");
					}
					else if (dr_totalby.equals("3")) {
						Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
					}
					else if (dr_totalby.equals("4")) {
						Str.append("<td align=left>").append(crs.getString("region_name")).append("</td>\n");
					}
					else if (dr_totalby.equals("5")) {
						Str.append("<td align=left>").append(crs.getString("brand_name")).append("</td>\n");
					}

					Str.append("<td align=right>").append(crs.getString("followupcount")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("contactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(contactable_perc)).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("noncontactable")).append("</td>\n");
					Str.append("<td align=right>").append(deci.format(noncontactable_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("satisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(satisfied_perc)).append("</td>\n");
					Str.append("<td align=right width=6%>").append(crs.getString("dissatisfied")).append("</td>\n");
					Str.append("<td align=right width=6%>").append(deci.format(dissatisfied_perc)).append("</td>\n");

					Str.append("</tr>");

				}
				// SOP("grandfollowupcount-------" + grandfollowupcount);
				// SOP("grandcontactable-------" + grandcontactable);
				// SOP("grandnoncontactable-------" + grandnoncontactable);

				if (grandcontactable != 0) {
					grandsatisfiedperc = (grandsatisfied / grandcontactable * 100);
					grandcontactable_perc = ((grandcontactable + 0.0) / grandfollowupcount) * 100;
					// SOP("grandcontactable_perc-------" + grandcontactable_perc);
				}
				if (grandcontactable != 0) {
					granddissatisfiedperc = (granddissatisfied / grandcontactable * 100);
				}
				if (grandnoncontactable != 0)
				{
					grandnoncontactable_perc = ((grandnoncontactable + 0.0) / grandfollowupcount) * 100;
				}
				// SOP("grandnoncontactable_perc-------" + grandnoncontactable_perc);
				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");
				Str.append("<td align=right><b>").append(grandfollowupcount).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandcontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandcontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandnoncontactable).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandnoncontactable_perc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(grandsatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(grandsatisfiedperc)).append("</b></td>\n");
				Str.append("<td align=right><b>").append(granddissatisfied).append("</b></td>\n");
				Str.append("<td align=right><b>").append(deci.format(granddissatisfiedperc)).append("</b></td>\n");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=red><b>No CRM Follow-up found!</b></font>");
			}
			crs.close();

			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePSFConcernDetails() throws SQLException {
		StringBuilder Str = new StringBuilder();
		String strtitle = "", strjoin = "";
		int[] concerntotal;
		int[] concerntotallink;
		int count = 0;
		int colcount = 0;
		StrSql = "SELECT jcpsfconcern_id ,"
				+ " jcpsfconcern_desc"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_concern"
				+ " WHERE 1 = 1";
		if (!jcpsfconcern_id.equals("")) {
			StrSql += " AND jcpsfconcern_id IN (" + jcpsfconcern_id + ")";
		}

		StrSql += " GROUP BY jcpsfconcern_id"
				+ " ORDER BY jcpsfconcern_desc";

		// SOP("StrSql==1111==" + StrSql);
		CachedRowSet crs1 = processQuery(StrSql, 0);

		strtitle = "SELECT ";
		if (dr_totalby.equals("1")) {
			strtitle += " emp_id, emp_name";
		}
		else if (dr_totalby.equals("3")) {
			strtitle += " branch_id , branch_name";
		}
		else if (dr_totalby.equals("4")) {
			strtitle += " branch_region_id , region_name";
		}
		else if (dr_totalby.equals("5")) {
			strtitle += " branch_brand_id , brand_name";
		}

		strjoin = " FROM " + compdb(comp_id) + "axela_emp INNER JOIN ( SELECT jc_emp_id";

		if (dr_totalby.equals("1")) {
			strjoin += " , jcpsf_emp_id";
		}
		if (dr_totalby.equals("3")) {
			strjoin += " ,branch_id , branch_name";
		}
		else if (dr_totalby.equals("4")) {
			strjoin += " ,branch_region_id ,region_name";
		}
		else if (dr_totalby.equals("5")) {
			strjoin += " ,branch_brand_id ,brand_name";
		}
		// SOP("strjoin===" + strjoin);
		while (crs1.next()) {
			count++;
			strtitle += "," + "Concern" + count + " AS '" + crs1.getString("jcpsfconcern_desc") + "'";
			strjoin += ", SUM(IF(jcpsfconcern_id = " + crs1.getString("jcpsfconcern_id") + ", 1, 0)) AS Concern" + count;
		}
		colcount = count;
		concerntotal = new int[colcount];
		concerntotallink = new int[colcount];
		int k = 0;
		crs1.beforeFirst();
		while (crs1.next()) {
			concerntotallink[k++] = crs1.getInt("jcpsfconcern_id");
		}

		strjoin += " FROM " + compdb(comp_id) + "axela_service_jc_psf"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = jc_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch_region on region_id = branch_region_id"
				+ " INNER JOIN axelaauto.axela_brand on brand_id = branch_brand_id"
				+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
				+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
				+ " WHERE 1 = 1"
				+ StrSearch
				+ " GROUP BY ";
		if (dr_totalby.equals("1")) {
			strjoin += " jcpsf_emp_id";
		}
		else if (dr_totalby.equals("3")) {
			strjoin += " branch_id ";
		}
		else if (dr_totalby.equals("4")) {
			strjoin += " branch_region_id";
		}
		else if (dr_totalby.equals("5")) {
			strjoin += " branch_brand_id";
		}

		strjoin += ") AS tblconcern ON tblconcern.jc_emp_id = emp_id"
				+ " WHERE 1 = 1"
				+ " GROUP BY ";

		if (dr_totalby.equals("1")) {
			strjoin += " jcpsf_emp_id";
		}
		else if (dr_totalby.equals("3")) {
			strjoin += " branch_id ";
		}
		else if (dr_totalby.equals("4")) {
			strjoin += " branch_region_id";
		}
		else if (dr_totalby.equals("5")) {
			strjoin += " branch_brand_id";
		}
		StrSql = strtitle + strjoin;

		SOP("StrSql==last==" + StrSql);

		Str.append("<div class=\"table \">\n");
		Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
		Str.append("<thead><tr>\n");
		Str.append("<th data-hide=\"phone\">#</th>\n");

		if (dr_totalby.equals("1")) {
			Str.append("<th>Consultant</th>\n");
		}
		else if (dr_totalby.equals("3")) {
			Str.append("<th>Branch</th>\n");
		}
		else if (dr_totalby.equals("4")) {
			Str.append("<th>Region</th>\n");
		}
		else if (dr_totalby.equals("5")) {
			Str.append("<th>Brand</th>\n");
		}
		crs1.beforeFirst();
		while (crs1.next()) {
			Str.append("<th data-hide=\"phone, tablet\" align=center>");
			Str.append(crs1.getString("jcpsfconcern_desc"));
			Str.append("</th>\n");
		}
		Str.append("<th data-hide=\"phone, tablet\" align=center>");
		Str.append("Total");
		Str.append("</th>\n");

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
			if (dr_totalby.equals("1")) {
				Str.append("<td align=left>").append(crs2.getString("emp_name")).append("</td>\n");
			}
			else if (dr_totalby.equals("3")) {
				Str.append("<td align=left>").append(crs2.getString("branch_name")).append("</td>\n");
			}
			else if (dr_totalby.equals("4")) {
				Str.append("<td align=left>").append(crs2.getString("region_name")).append("</td>\n");
			}
			else if (dr_totalby.equals("5")) {
				Str.append("<td align=left>").append(crs2.getString("brand_name")).append("</td>\n");
			}

			int rowtotal = 0;

			for (int i = 3; i <= colcount + 2; i++) {

				Str.append("<td align=right><b><a href=../service/report-jobcard-psf-followup-status.jsp?filter=yes");

				if (dr_totalby.equals("1")) {
					Str.append("&emp_id=").append(crs2.getString("emp_id"));
				}
				else if (dr_totalby.equals("3")) {
					Str.append("&branch_id=").append(crs2.getString("branch_id"));
				}
				else if (dr_totalby.equals("4")) {
					Str.append("&region_id=").append(crs2.getString("branch_region_id"));
				}
				else if (dr_totalby.equals("5")) {
					Str.append("&brand_id=").append(crs2.getString("branch_brand_id"));
				}

				Str.append("&jcpsfconcern_id=" + concerntotallink[i - 3]);
				Str.append("&starttime=" + starttime);
				Str.append("&endtime=" + endtime);
				Str.append("&brand_id=" + brand_id);
				Str.append("&region_id=" + region_id);
				Str.append("&branch_id=" + jc_branch_id);
				Str.append("&model_id=" + model_id);
				Str.append("&exe_id=" + crm_emp_id);
				Str.append("&jcpsf_psfdays_id=" + psfdays_id);
				Str.append("&jcpsfconcern_id=" + jcpsfconcern_id);
				Str.append(">");
				Str.append(crs2.getString(i));
				Str.append("</a>");
				Str.append("</b></td>\n");

				rowtotal += crs2.getInt(i);
				concerntotal[i - 3] += crs2.getInt(i);
			}
			Str.append("<td align=right><b>")
					.append("<a href=../service/report-jobcard-psf-followup-status.jsp?filter=yes");
			if (dr_totalby.equals("1")) {
				Str.append("&emp_id=").append(crs2.getString("emp_id"));
			}
			else if (dr_totalby.equals("3")) {
				Str.append("&branch_id=").append(crs2.getString("branch_id"));
			}
			else if (dr_totalby.equals("4")) {
				Str.append("&region_id=").append(crs2.getString("branch_region_id"));
			}
			else if (dr_totalby.equals("5")) {
				Str.append("&brand_id=").append(crs2.getString("branch_brand_id"));
			}
			Str.append("&starttime=" + starttime);
			Str.append("&endtime=" + endtime);
			Str.append("&brand_id=" + brand_id);
			Str.append("&region_id=" + region_id);
			// Str.append("&branch_id=" + jc_branch_id);
			Str.append("&model_id=" + model_id);
			Str.append("&exe_id=" + crm_emp_id);
			Str.append("&jcpsf_psfdays_id=" + psfdays_id);
			Str.append("&jcpsfconcern_id=" + jcpsfconcern_id);
			Str.append(">")
					.append(rowtotal)
					.append("</a>")
					.append("</b></td>\n");
			Str.append("</tr>");
		}

		Str.append("<tr align=center>\n");

		Str.append("<td align=right colspan=2><b>Total: </b></td>\n");
		int grandtotal = 0;
		for (int i = 3; i <= colcount + 2; i++) {
			Str.append("<td align=right><b><a href=../service/report-jobcard-psf-followup-status.jsp?filter=yes");

			Str.append("&jcpsfconcern_id=" + concerntotallink[i - 3]);
			Str.append("&starttime=" + starttime);
			Str.append("&endtime=" + endtime);
			Str.append("&brand_id=" + brand_id);
			Str.append("&region_id=" + region_id);
			Str.append("&branch_id=" + jc_branch_id);
			Str.append("&model_id=" + model_id);
			Str.append("&exe_id=" + crm_emp_id);
			Str.append("&jcpsf_psfdays_id=" + psfdays_id);
			Str.append("&jcpsfconcern_id=" + jcpsfconcern_id);
			Str.append(">").append(concerntotal[i - 3]).append("</a></b></td>\n");
			grandtotal += concerntotal[i - 3];
		}
		Str.append("<td align=right><b>" + grandtotal + "</b></td>\n");
		Str.append("</tr>");
		Str.append("</table>");
		Str.append("</div>");

		return Str.toString();
	}
	public void PSfConcernSearchDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			// HttpSession session = request.getSession(true);
			String emp_id = PadQuotes(request.getParameter("emp_id"));
			String starttime = PadQuotes(request.getParameter("starttime"));
			String endtime = PadQuotes(request.getParameter("endtime"));
			String brand_id = PadQuotes(request.getParameter("brand_id"));
			String region_id = PadQuotes(request.getParameter("region_id"));
			String branch_id = PadQuotes(request.getParameter("branch_id"));
			String model_id = PadQuotes(request.getParameter("model_id"));
			String exe_id = PadQuotes(request.getParameter("exe_id"));
			String jcpsf_psfdays_id = PadQuotes(request.getParameter("jcpsf_psfdays_id"));
			String jcpsfconcern_id = CNumeric(PadQuotes(request.getParameter("jcpsfconcern_id")));

			StrSmart += " AND jc_id IN (SELECT jcpsf_jc_id FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id";
			StrSmart += " WHERE 1=1 ";
			if (!jcpsf_psfdays_id.equals("")) {
				StrSmart += " AND psfdays_id = " + jcpsf_psfdays_id;
			}

			if (!jcpsfconcern_id.equals("0")) {
				StrSmart += " AND jcpsf_jcpsfconcern_id =" + jcpsfconcern_id;
			}
			else {
				StrSmart += " AND jcpsf_jcpsfconcern_id != 0";
			}
			StrSmart += " AND SUBSTR(jcpsf_followup_time,1,8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(jcpsf_followup_time,1,8) <= SUBSTR('" + endtime + "',1,8))";

			SOP("exe_id===" + exe_id);

			// enquiry filters
			if (!emp_id.equals("")) {
				StrSmart += " AND jc_emp_id IN ( " + emp_id + ")";
			}

			if (!brand_id.equals("")) {
				mischeck.brand_id = brand_id;
				StrSmart += " AND branch_brand_id IN (" + brand_id + ") ";
			}

			if (!region_id.equals("")) {
				StrSmart += " AND branch_region_id IN (" + region_id + ") ";
			}

			if (!branch_id.equals("")) {
				mischeck.exe_branch_id = branch_id;
				StrSmart += " AND branch_id IN (" + branch_id + ")";
			}

			if (!model_id.equals("")) {
				StrSmart += " AND preownedmodel_id IN ( " + model_id + ")";
			}

			SOP("StrSmart===" + StrSmart);
			SetSession("jcstrsql", StrSmart, request);
			response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
