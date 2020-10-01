//sangita
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToXLSX;

public class Report_JobCard_PSF extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String StrSql = "";
	public String branch_id = "0", exe_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String go = "";
	public String historydate = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String jc_emp_id = "";
	public String emp_id = "0";
	public String jc_id = "";
	public String pending_followup = "";
	public String msg = "", jcpsfsatisfied = "";
	public String[] psfdays_ids;
	public String jcpsf_psffeedbacktype_id = "0";
	public String jcpsf_satisfied = "0", psfdays_id = "0";
	public String exportcount = "";
	public String export = "";
	public String emp_all_exe = "";
	StringBuilder strpendingso = new StringBuilder();
	public Report_Check reportCheck = new Report_Check();
	public MIS_Check1 mischeck = new MIS_Check1();
	public String[] brand_ids, region_ids, zone_ids, branch_ids, preownedmodel_ids, advisor_ids, tech_ids, jccat_ids, jctype_ids;
	public String brand_id = "", region_id = "", zone_id, psf_branch_id = "", preownedmodel_id = "", advisor_id = "", tech_id = "", jccat_id = "", jctype_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			//
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("branch_id===" + branch_id);
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				go = PadQuotes(request.getParameter("submit_button"));
				export = PadQuotes(request.getParameter("export_button"));

				if (starttime.equals("")) {
					start_time = ReportStartdate();
				}
				if (endtime.equals("")) {
					end_time = strToShortDate(ToShortDate(kknow()));
				}
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					StrSearch = BranchAccess.replace("branch_id", "jc_branch_id") + "";
					if (emp_all_exe.equals("0"))
					{
						StrSearch += ExeAccess.replace("emp_id", "jcpsf_emp_id");
					}

					if (!psfdays_id.equals("")) {
						StrSearch += " AND jcpsf_psfdays_id IN (" + psfdays_id + ")";
					}

					if (!jcpsf_psffeedbacktype_id.equals("0")) {
						StrSearch += " AND jcpsf_psffeedbacktype_id = " + jcpsf_psffeedbacktype_id;
					}
					if (!jcpsf_satisfied.equals("0")) {
						StrSearch += " AND jcpsf_satisfied = " + jcpsf_satisfied;
					}

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}

					if (!zone_id.equals("")) {
						StrSearch += " AND branch_zone_id IN (" + zone_id + " )";
					}

					if (!branch_id.equals("")) {
						StrSearch += " AND jc_branch_id IN (" + branch_id + " )";
					}

					if (!exe_id.equals("") && !exe_id.equals("0")) {
						StrSearch += " AND jcpsf_emp_id = " + exe_id + "";
					}

					if (pending_followup.equals("0")) {
						StrSearch += " AND SUBSTR(jcpsf_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(jcpsf_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) ";
					}

					if (pending_followup.equals("1")) {
						StrSearch += " AND SUBSTR(jcpsf_followup_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
								+ " AND SUBSTR(jcpsf_followup_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8) "
								+ " AND jcpsf_desc = ''"
								+ " AND jcpsf_psffeedbacktype_id = 0 "
								+ " AND jcpsf_satisfied = 0 "
								+ " AND SUBSTR(jcpsf_followup_time, 1, 8) < SUBSTR('" + (ToLongDate(kknow())) + "', 1, 8) ";
					}
					if (!preownedmodel_id.equals("")) {
						StrSearch += " AND variant_preownedmodel_id IN (" + preownedmodel_id + ")";
					}

					if (!jccat_id.equals("")) {
						StrSearch += " AND jc_jccat_id IN (" + jccat_id + ")";
					}

					if (!jctype_id.equals("")) {
						StrSearch += " AND jc_jctype_id IN (" + jctype_id + ")";
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}

					if (msg.equals("")) {
						StrHTML = JCPSF();
						if (!StrSearch.equals("")) {
							SetSession("jcpsfstrsql", StrSearch, request);
						}
					}
				}

				if (export.equals("Export") && !go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!GetSession("jcpsfstrsql", request).equals("")) {
						StrSearch = GetSession("jcpsfstrsql", request);
						JCPSFDetails(request, response);
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
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		exe_id = request.getParameter("dr_emp_id");
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
		if (pending_followup.equals("on")) {
			pending_followup = "1";
		} else {
			pending_followup = "0";
		}
		psfdays_id = RetrunSelectArrVal(request, "dr_crmdays_id");
		psfdays_ids = request.getParameterValues("dr_crmdays_id");
		jcpsf_psffeedbacktype_id = CNumeric(PadQuotes(request.getParameter("dr_jcpsffeedbacktype")));
		jcpsf_satisfied = CNumeric(PadQuotes(request.getParameter("dr_jcpsf_satisfied")));
		preownedmodel_id = RetrunSelectArrVal(request, "dr_model_id");
		preownedmodel_ids = request.getParameterValues("dr_model_id");
		jccat_id = RetrunSelectArrVal(request, "dr_jccat_id");
		jccat_ids = request.getParameterValues("dr_jccat_id");
		jctype_id = RetrunSelectArrVal(request, "dr_jctype_id");
		jctype_ids = request.getParameterValues("dr_jctype_id");

	}

	public String JCPSF() {
		try {
			int count = 0;
			StringBuilder Str = new StringBuilder();
			StrSql = " SELECT  jc_id, COALESCE (jc_ro_no, '') AS jc_ro_no, jc_time_in,"
					// + " jcstage_name, "
					+ " jcpsf_followup_time, jc_time_out, variant_name, jcpsf_desc, "
					+ " COALESCE(jccat_name, '')AS jccat_name,"
					+ " COALESCE(jctype_name, '') AS jctype_name,"
					+ " COALESCE (psffeedbacktype_name, '') AS psffeedbacktype_name,"
					+ " COALESCE(e.emp_id,0) AS jc_emp_id, COALESCE(jcpsf_satisfied, '0') AS 'jcpsf_satisfied',"
					+ " COALESCE(CONCAT(e.emp_name,' (',e.emp_ref_no,')'" + " ), '') AS jc_emp_name, "
					+ " COALESCE(jcexe.emp_id,0) AS psf_emp_id,"
					+ " COALESCE(CONCAT( jcexe.emp_name, ' (',jcexe.emp_ref_no,')'), '') AS emp_name,"
					+ " veh_id, veh_reg_no, jcpsf_id, customer_id, customer_name, contact_id, veh_iacs,"
					+ " CONCAT(title_desc,' ',contact_fname,' ',contact_lname) AS contact_name,veh_classified,"
					+ " contact_mobile1, contact_mobile2,contact_email1, contact_email2, psfdays_daycount, psfdays_desc"
					+ " FROM  " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN axela_brand ON brand_id = psfdays_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id";
			StrSql += " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id "
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp SESSION ON SESSION .emp_id = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp e ON e.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp jcexe ON jcexe.emp_id = jcpsf_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp ON entryemp.emp_id = jcpsf_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp ON modifiedemp.emp_id = jcpsf_modified_id"
					+ " LEFT JOIN axela_service_psf_feedbacktype ON psffeedbacktype_id = jcpsf_psffeedbacktype_id"
					+ " WHERE jc_active = 1"
					+ StrSearch
					+ " ORDER BY"
					+ " jcpsf_followup_time,"
					+ " jc_id"
					+ " LIMIT 500";
			// if (emp_id.equals("1")) {
			// SOPInfo("StrSql -------------" + StrSql);
			// }
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-responsive table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=center>\n");
				Str.append("<th data-hide=\"phone\" align=center>#</th>\n");
				Str.append("<th data-toggle=\"true\">Job Card ID</th>\n");
				Str.append("<th style=\"width:200px;\">Customer</th>\n");
				Str.append("<th data-hide=\"phone\">Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone\">Time In</th>\n");
				Str.append("<th data-hide=\"phone\">Time Out</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Item</th>\n");
				// Str.append("<th align=\"center\">Stage</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Service Advisor</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">PSF Executive</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">PSF Time</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">PSF Days</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Category</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Job Card Type</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">PSF Feedback Type</th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Experience </th>\n");
				Str.append("<th data-hide=\"phone,tablet\">Description</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("jc_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("jc_id") + ");'");
					Str.append(" style='height:200px'>\n");
					Str.append("<td>").append(count).append("</td>");
					Str.append("<td nowrap><a href=jobcard-list.jsp?jc_id=").append(crs.getString("jc_id")).append(">");
					Str.append(crs.getString("jc_id")).append("</a>");
					if (!crs.getString("jc_ro_no").equals("")) {
						Str.append("<br>JC NO.: ").append(crs.getString("jc_ro_no"));
					}
					Str.append("</td>\n");
					Str.append("<td nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_name")).append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 5, "M", crs.getString("jc_id")))
								.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}

					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 5, "M", crs.getString("jc_id")))
								.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
					}

					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("jc_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email1")).append("\">");
						Str.append(crs.getString("contact_email1")).append("</a></span>");
					}

					if (!crs.getString("contact_email2").equals("")) {
						Str.append("<br><span class='customer_info customer_" + crs.getString("jc_id") + "'  style='display: none;'><a href=\"mailto:")
								.append(crs.getString("contact_email2")).append("\">");
						Str.append(crs.getString("contact_email2")).append("</a></span>");
					}
					Str.append("</td>\n");
					Str.append("<td nowrap><a href=\"../service/vehicle-list.jsp?veh_id=");
					Str.append(crs.getString("veh_id")).append("\">").append(SplitRegNo(crs.getString("veh_reg_no"), 2));
					Str.append("</a>");
					if (crs.getString("veh_classified").equals("1")) {
						Str.append("<br/><font color=\"red\"><b>[Classified]</b></font>");
					}
					Str.append("</td>");
					Str.append("<td>").append(strToShortDate(crs.getString("jc_time_in"))).append("</td>");
					Str.append("<td>").append(strToShortDate(crs.getString("jc_time_out"))).append("</td>");
					Str.append("<td>").append(crs.getString("variant_name")).append("</td>");
					// Str.append("<td align=\"left\">").append().append("</td>");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("jc_emp_id")).append(">");
					Str.append(crs.getString("jc_emp_name")).append("</a></td>");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("psf_emp_id")).append(">");
					Str.append(crs.getString("emp_name")).append("</a></td>");
					Str.append("<td><a href=jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id"));
					Str.append("#tabs-9 target=blank>");
					Str.append(strToLongDate(crs.getString("jcpsf_followup_time"))).append("</a></td>\n");
					// Str.append("<td align=\"center\"><a href=jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("#tabs-9>");
					// Str.append(strToLongDate(crs.getString("jcpsf_followup_time"))).append("</a></td>\n");
					Str.append("<td><a href=jobcard-psf-update.jsp?update=yes&jc_id=").append(crs.getString("jc_id")).append("&jcpsf_id=");
					Str.append(crs.getString("jcpsf_id")).append(" target=blank>").append(crs.getString("psfdays_daycount")).append(crs.getString("psfdays_desc")).append("</a></td>\n");
					Str.append("<td>").append(crs.getString("jccat_name")).append("</td>");
					Str.append("<td>").append(crs.getString("jctype_name")).append("</td>");
					Str.append("<td>").append(crs.getString("psffeedbacktype_name")).append("</td>");

					if (crs.getString("jcpsf_satisfied").equals("1"))
					{
						jcpsfsatisfied = "Satisfied";
					} else if (crs.getString("jcpsf_satisfied").equals("2")) {
						jcpsfsatisfied = "Dis-Satisfied";
					} else if (crs.getString("jcpsf_satisfied").equals("0")) {
						jcpsfsatisfied = "";
					}
					Str.append("<td>").append(jcpsfsatisfied).append("</td>");
					Str.append("<td>").append(crs.getString("jcpsf_desc")).append("</td>");
					Str.append("</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<center><font color=\"red\"><b>No PSF Follow-up found!</b></font></center>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulatePSFExecutive(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					// + " ("
					+ " AND emp_service_psf = 1";
			if (!branch_id.equals("")) {
				StrSql += " AND (emp_branch_id IN ( " + branch_id + " )"
						+ " OR emp_id = 1"
						+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
						+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id";

				StrSql += " AND empbr.emp_branch_id IN ( " + branch_id + " )";
				StrSql += " )) ";
			}

			StrSql += ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// SOP("StrSql----" + StrSql);

			Str.append("<select name=\"dr_emp_id\" id=\"dr_emp_id\" class=\"form-control\">\n");
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), exe_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
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
	public String PopulatePSFFeedbackType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT psffeedbacktype_id, psffeedbacktype_name"
					+ " FROM axela_service_psf_feedbacktype"
					+ " ORDER BY psffeedbacktype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("psffeedbacktype_id"));
				Str.append(StrSelectdrop(crs.getString("psffeedbacktype_id"), jcpsf_psffeedbacktype_id));
				Str.append(">").append(crs.getString("psffeedbacktype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	protected void CheckForm() {
		msg = "";
		// if (branch_id.equals("0")) {
		// msg = msg + "<br>Select Branch!";
		// }

		if (starttime.equals("")) {
			msg += "<br>Select Start Date!";
		} else {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				start_time = strToShortDate(starttime);
			} else {
				msg += "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}

		if (endtime.equals("")) {
			msg += "<br>Select End Date!<br>";
		} else {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg += "<br>Start Date should be less than End date!";
				}
				end_time = strToShortDate(endtime);
			} else {
				msg += "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
		// if (jcpsf_satisfied.equals("0")) {
		// msg += "<br>Select Experience!";
		// }

	}

	public void JCPSFDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			SOP("branch_id==" + branch_id);
			String strcf = "";
			StrSql = "SELECT jcpsfcf_id, jcpsfcf_title, cftype_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf_cf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsfcf_crmdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id "
					+ " INNER JOIN axela_cf_type ON cftype_id = jcpsfcf_cftype_id "
					+ " WHERE 1=1 "
					+ " AND jcpsfcf_active = 1"
					+ " AND psfdays_active = 1";

			if (!branch_id.equals("")) {
				StrSql += " AND branch_id IN (" + branch_id + " )";
			}

			if (!psfdays_id.equals("")) {
				StrSql += " AND psfdays_id IN (" + psfdays_id + " )";
			}

			StrSql += " GROUP BY jcpsfcf_id"
					+ " ORDER BY psfdays_daycount, jcpsfcf_rank";
			// SOP("StrSql==111=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (crs.getString("cftype_id").equals("6")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(jcpsfcftrans_value, '%d/%m/%Y %h:%i'), '') AS 'jcpsfcftrans_value'";
					} else if (crs.getString("cftype_id").equals("5")) {
						strcf += " (SELECT COALESCE(DATE_FORMAT(jcpsfcftrans_value, '%d/%m/%Y'), '') AS 'jcpsfcftrans_value'";
					} else {
						strcf += "(SELECT COALESCE(jcpsfcftrans_value,'') AS 'jcpsfcftrans_value' ";
					}
					strcf += "FROM " + compdb(comp_id) + "axela_service_jc_psf_trans "
							+ " WHERE jcpsfcftrans_jcpsf_id = jcpsf_id"
							+ " AND jcpsfcftrans_jcpsfcf_id = " + crs.getString("jcpsfcf_id") + " Limit 1) as '" + crs.getString("jcpsfcf_title") + "',";

				}
			}
			crs.close();
			StrSql = "SELECT jc_id AS 'Job Card ID',"
					+ " COALESCE(DATE_FORMAT(jc_time_in, '%d/%m/%Y %h:%i'), '')  AS 'Time In',"
					+ " COALESCE(DATE_FORMAT(jc_time_out, '%d/%m/%Y %h:%i'), '')  AS 'Time Out',"
					+ " COALESCE(jctype_name, '') AS 'Type',"
					+ " COALESCE(preownedmodel_name, '') AS 'Model',"
					+ " variant_name AS 'Item',"
					+ " COALESCE(jcstage_name, '') AS 'Stage',"
					+ " veh_reg_no AS 'Reg. No.',"
					+ " jc_ro_no AS 'RO NO.',"
					+ " COALESCE(jc_kms, 0) AS 'Kms',"
					+ " COALESCE(DATE_FORMAT(veh_sale_date, '%d/%m/%Y %h:%i'), '') AS 'Date of Sale',"
					+ " IF(substr(veh_sale_date, 1, 8) < (SELECT DATE_FORMAT(SUBDATE(NOW(), INTERVAL 6 month), '%Y%m%d')), 'Old','New') AS 'Type',"
					+ " COALESCE(customer_name, '') AS 'Customer',"
					+ " COALESCE(customer_mobile1, '') AS 'Mobile 1',"
					+ " COALESCE(customer_mobile2, '') AS 'Mobile 2',"
					+ " COALESCE(customer_email1, '') AS 'Email 1',"
					+ " COALESCE(customer_email2, '') AS 'Email 2',"
					+ " COALESCE(contact_phone1, '') AS 'Phone 1',"
					+ " COALESCE(contact_phone2, '') AS 'Phone 2',"
					+ " COALESCE(jcpsf_desc, '') AS 'Description', "
					+ " CONCAT(psfdays_daycount, psfdays_desc) AS 'PSF Days',"
					+ " COALESCE(psffeedbacktype_name, '') AS 'Feedback Name',"
					+ " IF(jcpsf_satisfied = 1,'Yes','NO') AS 'Satisfied',"
					+ " COALESCE (entryemp.emp_name, 0) AS 'Executive Name',"
					+ " COALESCE(service.emp_name, '') AS 'Service Adviser',"
					+ " COALESCE(DATE_FORMAT(jcpsf_entry_time, '%d/%m/%Y %h:%i'), '') AS 'JobCard Entry Time',"
					+ " COALESCE(variant_name, '') AS 'Variant',"
					+ " CONCAT(title_desc,' ',contact_fname, ' ', contact_lname	) AS 'Customer Name',"
					+ " contact_address AS 'Contact Address'," + strcf
					+ " COALESCE(jcpsfconcern_desc, '') AS 'Concern'"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN axela_brand ON brand_id = psfdays_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"

					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"

					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp SESSION ON SESSION.emp_id = 1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp jcexe on jcexe.emp_id = jcpsf_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp service ON service.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp entryemp ON entryemp.emp_id = jcpsf_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp modifiedemp ON modifiedemp.emp_id = jcpsf_modified_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " LEFT JOIN axela_service_psf_feedbacktype  ON psffeedbacktype_id = jcpsf_psffeedbacktype_id"
					+ " WHERE jc_active = 1" + StrSearch + ""
					+ " GROUP BY jcpsf_id";
			// SOP("StrSql===Report PSF Details===" + StrSql);
			crs = processQuery(StrSql, 0);

			new ExportToXLSX().Export(request, response, crs, "PSFDetails", comp_id);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateCRMSatisfied(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", jcpsf_satisfied)).append(">Satisfied</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", jcpsf_satisfied)).append(">Dis-Satisfied</option>\n");
		return Str.toString();
	}

	public String PopulateJobCardCategory(String jc_cat_ids[], String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		// SOP("Coming in Tech...");
		try {
			StrSql = "SELECT jccat_id, jccat_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
					// + " INNER  JOIN " + compdb(comp_id) + "axela_service_jc ON jc_emp_id = emp_id"
					+ " WHERE 1 = 1";

			// SOP("Technician======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_jccat_id\" id=\"dr_jccat_id\" class='form-control multiselect-dropdown' multiple=multiple size=10>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("jccat_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("jccat_id"), jc_cat_ids));
					Str.append(">").append(crs.getString("jccat_name")).append("</option> \n");
				}
			}
			Str.append("</select>");

			crs.close();
			// SOP("Str==" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobCardType(String jc_type_ids[], String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		// SOP("Coming in Tech...");
		try {
			StrSql = "SELECT jctype_id, jctype_name "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					// + " INNER  JOIN " + compdb(comp_id) + "axela_service_jc ON jc_emp_id = emp_id"
					+ " WHERE 1 = 1";

			// SOP("Technician======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_jctype_id\" id=\"dr_jctype_id\" class='form-control multiselect-dropdown' multiple=multiple size=10>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("jctype_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("jctype_id"), jc_type_ids));
					Str.append(">").append(crs.getString("jctype_name")).append("</option> \n");
				}
			}
			Str.append("</select>");

			crs.close();
			// SOP("Str==" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
