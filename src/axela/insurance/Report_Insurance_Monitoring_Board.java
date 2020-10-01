package axela.insurance;

//created by ankit
import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insurance_Monitoring_Board extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "", year = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "";
	public String[] brand_ids, region_ids, branch_ids, carmanuf_ids, model_ids, insur_emp_ids, field_emp_ids;
	public String brand_id = "", branch_id = "", region_id = "", carmanuf_id = "", model_id = "", insuremp_id = "", fieldemp_id = "";
	public String StrHTML = "", header = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "", dr_orderby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "", StrJoin = "";
	public String dr_total_by = "", dr_order_by = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	public String SearchURL = "report-insurance-monitoring-board-search.jsp";
	DecimalFormat deci = new DecimalFormat("0.00");
	DecimalFormat deci1 = new DecimalFormat("0");
	public MIS_Check misCheck = new MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				header = PadQuotes(request.getParameter("header"));
				emp_id = CNumeric(GetSession("emp_id", request));
				// SOP("===="+getPercentage(100, 10));
				if (!header.equals("no")) {

					branch_id = CNumeric(GetSession("emp_branch_id", request));
					BranchAccess = GetSession("BranchAccess", request);
					ExeAccess = GetSession("ExeAccess", request);
					emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
					go = PadQuotes(request.getParameter("submit_button"));
				}
				if (!header.equals("no")) {
					GetValues(request, response);
					CheckForm();

					if (go.equals("Go")) {
						// SOP("Start D===" + starttime.substring(0, 6) + "01");
						// SOP("End D====" + endtime.substring(0, 6) + "31");
						BranchAccess = BranchAccess.replace("branch_id", "jc_branch_id");
						ExeAccess += ExeAccess;
						if (!brand_id.equals("")) {
							StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
						}
						if (!region_id.equals("")) {
							StrSearch += " AND branch_region_id IN (" + region_id + ") ";
						}
						if (!branch_id.equals("")) {
							StrSearch += " AND branch_id IN (" + branch_id + ")";
						}
						if (!carmanuf_id.equals("")) {
							StrSearch += " AND carmanuf_id IN (" + carmanuf_id + ")";
						}
						if (!model_id.equals("")) {
							StrSearch += " AND preownedmodel_id IN (" + model_id + ")";
						}
						if (!insuremp_id.equals("") || !fieldemp_id.equals("")) {
							StrSearch += " AND emp_id IN (" + insuremp_id + fieldemp_id + ")";
						} else if (!insuremp_id.equals("")) {
							StrSearch += " AND emp_id IN (" + insuremp_id + ")";
						} else if (!fieldemp_id.equals("")) {
							StrSearch += " AND emp_id IN (" + fieldemp_id + ")";
						}
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						}
						if (msg.equals("")) {
							StrHTML = ListInsuranceMonitoringBoard();
							// SOP("StrHTML===" + StrHTML);
						}
					}
				}
			}

			// SOP("StrSearch_----------" + StrSearch);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		starttime = PadQuotes(request.getParameter("txt_starttime"));
		endtime = PadQuotes(request.getParameter("txt_endtime"));
		if (!starttime.equals("")) {
			year = starttime.substring(6, 10);
		}
		dr_totalby = PadQuotes(request.getParameter("dr_totalby"));
		dr_orderby = PadQuotes(request.getParameter("dr_orderby"));
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		carmanuf_id = RetrunSelectArrVal(request, "dr_manufacturer");
		carmanuf_ids = request.getParameterValues("dr_manufacturer");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		insuremp_id = RetrunSelectArrVal(request, "dr_insur_emp_id");
		insur_emp_ids = request.getParameterValues("dr_insur_emp_id");
		fieldemp_id = RetrunSelectArrVal(request, "dr_field_exe");
		field_emp_ids = request.getParameterValues("dr_field_exe");
		// SOP("starttime==" + starttime);
		// SOP("endtime==" + endtime);
		// SOP("dr_orderby==" + dr_orderby);
		// SOP("dr_totalby==" + dr_totalby);
		// SOP("brand_id==" + brand_id);
		// SOP("region_id==" + region_id);
		// SOP("branch_id==" + branch_id);
		// SOP("carmanuf_id==" + carmanuf_id);
		// SOP("model_id==" + model_id);
		// SOP("insuremp_id==" + insuremp_id);
		// SOP("fieldemp_id==" + fieldemp_id);

		if (starttime.equals("")) {
			starttime = strToShortDate(ToShortDate(kknow()));
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		} else {
			dr_branch_id = branch_id;
		}

	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>SELECT Start Date!";
		}
		if (!starttime.equals("")) {
			if (isValidDateFormatShort(starttime)) {
				starttime = ConvertShortDateToStr(starttime);
				targetstarttime = starttime.substring(0, 6) + "01000000";
				start_time = strToShortDate(starttime);
			} else {
				msg = msg + "<br>Enter Valid Start Date!";
				starttime = "";
			}
		}
		if (endtime.equals("")) {
			msg = msg + "<br>SELECT End Date!<br>";
		}
		if (!endtime.equals("")) {
			if (isValidDateFormatShort(endtime)) {
				endtime = ConvertShortDateToStr(endtime);
				if (!starttime.equals("") && !endtime.equals("") && Long.parseLong(starttime) > Long.parseLong(endtime)) {
					msg = msg + "<br>Start Date should be less than End date!";
				}
				targetendtime = endtime.substring(0, 6) + "31000000";
				end_time = strToShortDate(endtime);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1, 0, 0));

			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}
	}

	public String ListInsuranceMonitoringBoard() {
		try {
			int count = 0;
			int enquiry_target = 0;
			int policy_target = 0;
			int openenquiry = 0;
			int freshenquiry = 0;
			int lostenquiry = 0;
			int policies = 0;
			double premiumamt = 0;

			int totalenquiry_target = 0;
			int totalpolicy_target = 0;
			int totalopenenquiry = 0;
			int totalfreshenquiry = 0;
			int totallostenquiry = 0;
			int totalpolicies = 0;
			double totalpremiumamt = 0;

			StringBuilder Str = new StringBuilder();
			StringBuilder appendStr = new StringBuilder();
			appendStr.append(SearchURL)
					.append("?starttime=" + starttime).append("&endtime=" + endtime).append("&dr_year=" + year)
					.append("&brand_id=" + brand_id).append("&region_id=" + region_id).append("&dr_branch_id=" + dr_branch_id)
					.append("&carmanuf_id=" + carmanuf_id).append("&model_id=" + model_id)
					.append("&insuremp_id=" + insuremp_id).append("&fieldemp_id=" + fieldemp_id)
					.append("&enquiry=");

			if (dr_totalby.equals("emp_id")) {
				dr_total_by += "emp_id";
			}
			if (dr_totalby.equals("emp_branch_id")) {
				dr_total_by += " emp_branch_id";
			}
			if (dr_totalby.equals("branch_region_id")) {
				dr_total_by += " branch_region_id";
				StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}
			if (dr_totalby.equals("branch_brand_id")) {
				dr_total_by += " branch_brand_id";
				StrJoin += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
			}

			StrSql = "SELECT "
					+ " emp_id,"
					+ " emp_name,"
					+ " branch_brand_id,"
					+ " branch_region_id,"
					+ " branch_id,"
					+ " branch_name,";
			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " brand_name,";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " region_name,";
			}

			StrSql += " COALESCE(insurenquirytarget,0) AS insurenquirytarget,"
					+ " COALESCE(insurpolicytarget,0) AS insurpolicytarget,"
					+ " COALESCE(insurenquiryopen,0) AS insurenquiryopen,"
					+ " COALESCE(insurenquiryfresh,0) AS insurenquiryfresh,"
					+ " COALESCE(insurenquirylost,0) AS insurenquirylost,"
					+ " COALESCE(insurpolicy,0) AS insurpolicy,"
					+ " COALESCE(insurpremium,0) AS insurpremium"
					// Main Table
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = emp_branch_id";
			if (!StrSearch.equals("")) {
				StrSql += StrJoin;
			}
			// Target
			StrSql += " LEFT JOIN ( SELECT"
					+ " SUM(IF(SUBSTR(insurance_target_startdate, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(insurance_target_enddate, 1, 8) <= SUBSTR('" + endtime + "',1,8) ,"
					+ " insurance_target_enquiry_count,0)) AS insurenquirytarget,"
					+ " SUM(IF(SUBSTR(insurance_target_startdate, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(insurance_target_enddate, 1, 8) <= SUBSTR('" + endtime + "',1,8) ,"
					+ " insurance_target_policy_count,0)) AS insurpolicytarget,"
					+ " insurance_target_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_target"
					+ " WHERE 1=1"
					+ " GROUP BY insurance_target_emp_id)"
					+ " AS tbltarget ON tbltarget.insurance_target_emp_id = emp_id"
					// Enquiry
					+ " LEFT JOIN ( SELECT"
					+ " SUM(IF(SUBSTR(insurenquiry_date, 1, 8) < SUBSTR('" + starttime + "',1,8)"
					+ " AND insurenquiry_insurstatus_id = 1,1,0)) AS insurenquiryopen,"
					+ " SUM(IF(SUBSTR(insurenquiry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(insurenquiry_entry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8) ,1,0)) AS insurenquiryfresh,"
					+ " SUM(IF(SUBSTR(insurenquiry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(insurenquiry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8)"
					+ " AND (insurenquiry_insurstatus_id = 3 OR insurenquiry_insurstatus_id = 4	),1,0)) AS insurenquirylost,"
					+ " insurenquiry_emp_id,"
					+ " insurenquiry_variant_id "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry GROUP BY insurenquiry_emp_id) AS tblenq ON tblenq.insurenquiry_emp_id = emp_id"
					// Policy
					+ " LEFT JOIN ( SELECT"
					+ " SUM(IF(SUBSTR(insurpolicy_entry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(insurpolicy_entry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8) ,1,0)) AS insurpolicy,"
					+ " SUM(IF(SUBSTR(insurpolicy_entry_date, 1, 8) >= SUBSTR('" + starttime + "',1,8)"
					+ " AND SUBSTR(insurpolicy_entry_date, 1, 8) <= SUBSTR('" + endtime + "',1,8) ,insurpolicy_premium_amt,0)) AS insurpremium,"
					+ " insurpolicy_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy";

			StrSql += " WHERE 1=1"
					+ " GROUP BY insurpolicy_emp_id)"
					+ " AS tblpolicy ON tblpolicy.insurpolicy_emp_id = emp_id";

			if (!carmanuf_id.equals("")) {
				StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id ";
			}
			if (dr_totalby.equals("branch_brand_id")) {
				StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id ";
			}
			if (dr_totalby.equals("branch_region_id")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}

			StrSql += " WHERE emp_insur = 1"
					+ StrSearch
					+ " GROUP BY " + dr_total_by;
			if (!dr_orderby.equals("0")) {
				StrSql += " ORDER BY " + dr_orderby + " DESC ";
			}

			// StrSql += " = emp_id"
			// + StrSearch
			// + empmainjoin;
			// StrSql += BranchAccess
			// + ExeAccess;
			SOP("StrSql==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<div class='container'>");
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered \" data-filter=\"#filter\">\n");
				Str.append("<thead style=\"margin-top:50px;\">");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				String title_head = "", id = "", total_by = "";
				switch (dr_totalby) {
					case "branch_brand_id" :
						title_head = "Brand";
						total_by = "brand";
						break;
					case "branch_region_id" :
						title_head = "Region";
						total_by = "region";
						break;
					case "emp_branch_id" :
						title_head = "Branch";
						total_by = "branch";
						break;
					case "emp_id" :
						title_head = "Insurance Executive";
						total_by = "emp";
						break;
					default :
						title_head = "Insurance Executive";
						total_by = "emp";
						break;
				}

				Str.append("<th title=" + title_head + ">" + title_head + "</th>\n");

				Str.append("<th data-hide=\"phone\" title=\"Enquiry Target\">Enquiry Target</th>\n");
				Str.append("<th data-hide=\"phone\" title=\"Open Enquiry\">Open Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\" title=\"Fresh Enquiry\">Fresh Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\" title=\"Lost Enquiry\">Lost Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\" title=\"Policy Target\">Policy Target</th>\n");
				Str.append("<th data-hide=\"phone\" title=\"Policies\">Policies</th>\n");
				Str.append("<th data-hide=\"phone\" title=\"Premium Amount\">Premium Amount</th>\n");

				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					emp_id = crs.getString("emp_id");
					brand_id = crs.getString("branch_brand_id");
					region_id = crs.getString("branch_region_id");
					branch_id = crs.getString("branch_id");
					enquiry_target = crs.getInt("insurenquirytarget");
					policy_target = crs.getInt("insurpolicytarget");
					openenquiry = crs.getInt("insurenquiryopen");
					freshenquiry = crs.getInt("insurenquiryfresh");
					lostenquiry = crs.getInt("insurenquirylost");
					policies = crs.getInt("insurpolicy");
					premiumamt = crs.getDouble("insurpremium");

					totalenquiry_target += enquiry_target;
					totalpolicy_target += policy_target;
					totalopenenquiry += openenquiry;
					totalfreshenquiry += freshenquiry;
					totallostenquiry += lostenquiry;
					totalpolicies += policies;
					totalpremiumamt += premiumamt;

					count++;

					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");

					switch (dr_totalby) {
						case "emp_branch_id" :
							Str.append("<td align=left>").append(crs.getString("branch_name")).append("</td>\n");
							id = branch_id;
							break;
						case "branch_region_id" :
							Str.append("<td align=left>").append(crs.getString("region_name")).append("</td>\n");
							id = region_id;
							break;
						case "branch_brand_id" :
							Str.append("<td align=left>").append(crs.getString("brand_name")).append("</td>\n");
							id = brand_id;
							break;
						case "emp_id" :
							Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id")).append(">")
									.append(crs.getString("emp_name")).append("</a></td>\n");
							id = emp_id;
							break;
						default :
							Str.append("<td align=left><a href=../portal/executive-summary.jsp?emp_id=" + crs.getString("emp_id")).append(">")
									.append(crs.getString("emp_name")).append("</a></td>\n");
							id = emp_id;
							break;
					}

					if (dr_totalby.equals("emp_id")) {
						Str.append("<td align=right>").append(IndDecimalFormat(deci1.format(enquiry_target))).append("</td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("openenquiry").append("&value=" + id + " target=_blank>")
								.append(openenquiry).append("</a></td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("freshenquiry").append("&value=" + id + " target=_blank>")
								.append(freshenquiry).append("</a></td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("lostenquiry").append("&value=" + id + " target=_blank>")
								.append(lostenquiry).append("</a></td>\n");
						Str.append("<td align=right>").append(IndDecimalFormat(deci1.format(policy_target))).append("</td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("policy").append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
								.append(policies).append("</a></td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("policy").append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
								.append(unescapehtml(IndDecimalFormat(deci.format((premiumamt))))).append("</a></td>\n");

					} else {
						Str.append("<td align=right>").append(IndDecimalFormat(deci1.format(enquiry_target))).append("</td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("openenquiry").append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
								.append(openenquiry).append("</a></td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("freshenquiry").append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
								.append(freshenquiry).append("</a></td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("lostenquiry").append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
								.append(lostenquiry).append("</a></td>\n");
						Str.append("<td align=right>").append(IndDecimalFormat(deci1.format(policy_target))).append("</td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("policy").append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
								.append(policies).append("</a></td>\n");
						Str.append("<td align=right><a href=").append(appendStr).append("policy").append("&total_by=" + total_by).append("&value=" + id + " target=_blank>")
								.append(unescapehtml(IndDecimalFormat(deci.format((premiumamt))))).append("</a></td>\n");

					}
					Str.append("</tr>");
				}

				Str.append("<tr>\n");
				Str.append("<td align=right colspan=\"2\"><b>Total: </b></td>\n");

				Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci1.format((totalenquiry_target))))).append("</b></td>\n");
				Str.append("<td align=right><b><a href=").append(appendStr).append("totalopenenquiry").append(" target=_blank>")
						.append(totalopenenquiry).append("</a></b></td>\n");
				Str.append("<td align=right><b><a href=").append(appendStr).append("totalfreshenquiry").append(" target=_blank>")
						.append(totalfreshenquiry).append("</a></b></td>\n");
				Str.append("<td align=right><b><a href=").append(appendStr).append("totallostenquiry").append(" target=_blank>")
						.append(totallostenquiry).append("</a></b></td>\n");
				Str.append("<td align=right><b>").append(unescapehtml(IndDecimalFormat(deci1.format((totalpolicy_target))))).append("</b></td>\n");
				Str.append("<td align=right><b><a href=").append(appendStr).append("totalpolicy").append(" target=_blank>")
						.append(totalpolicies).append("</a></b></td>\n");
				Str.append("<td align=right><b><a href=").append(appendStr).append("totalpolicy").append(" target=_blank>").append(unescapehtml(IndDecimalFormat(deci.format((totalpremiumamt)))))
						.append("</a></b></td>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
				// Str.append("</div>");
				// SOP("str total by" + Str);

			} else {
				Str.append("<font color=red><center><b>No Reports found!</b></center></font><br>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateTotalBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=emp_id").append(StrSelectdrop("emp_id", dr_totalby)).append(">Executives</option>\n");
		Str.append("<option value=branch_brand_id").append(StrSelectdrop("branch_brand_id", dr_totalby)).append(">Brands</option>\n");
		Str.append("<option value=branch_region_id").append(StrSelectdrop("branch_region_id", dr_totalby)).append(">Regions</option>\n");
		Str.append("<option value=emp_branch_id").append(StrSelectdrop("emp_branch_id", dr_totalby)).append(">Branches</option>\n");

		return Str.toString();
	}

	public String PopulateOrderBy(String comp_id, String dr_orderby) {
		StringBuilder Str = new StringBuilder();

		Str.append("<option value=0>Select</option>");
		Str.append("<option value=insurenquirytarget").append(StrSelectdrop("insurenquirytarget", dr_orderby)).append(">Enquiry Target</option>\n");
		Str.append("<option value=insurenquiryopen").append(StrSelectdrop("insurenquiryopen", dr_orderby)).append(">Open Enquiry</option>\n");
		Str.append("<option value=insurenquiryfresh").append(StrSelectdrop("insurenquiryfresh", dr_orderby)).append(">Fresh Enquiry</option>\n");
		Str.append("<option value=insurenquirylost").append(StrSelectdrop("insurenquirylost", dr_orderby)).append(">Lost Enquiry</option>\n");
		Str.append("<option value=insurpolicytarget").append(StrSelectdrop("insurpolicytarget", dr_orderby)).append(">Policy Target</option>\n");
		Str.append("<option value=insurpolicy").append(StrSelectdrop("insurpolicy", dr_orderby)).append(">Policies</option>\n");
		Str.append("<option value=insurpremium").append(StrSelectdrop("insurpremium", dr_orderby)).append(">Premium Amount</option>\n");
		return Str.toString();
	}

}
