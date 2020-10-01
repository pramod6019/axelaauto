package axela.service;
//Saiman 16th feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Service_SOB_Dash extends Connect {

	public String submitB = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, zone_ids, branch_ids, model_ids, advisor_ids, tech_ids, jccat_ids;
	public String brand_id = "", region_id = "", zone_id, model_id = "", advisor_id = "", tech_id = "", jccat_id = "";
	public String StrHTML = "", StrClosedHTML = "", Strhtml = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", jobcardemp = "", technicianemp = "", empmainjoin = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String emp_all_exe = "";
	public MIS_Check1 mischeck = new MIS_Check1();
	public String SearchURL = "report-service-sob-dash.jsp?";
	public String StrFilter = "", filter = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("branch_id===="+branch_id);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				filter = PadQuotes(request.getParameter("filter"));
				if (filter.equals("yes")) {
					SoeDetails(request, response);
				}

				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!zone_id.equals("")) {
						StrSearch += " AND branch_zone_id IN (" + zone_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch += StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch += " AND preownedmodel_id IN (" + model_id + ")";
					}
					if (!advisor_id.equals("")) {
						jobcardemp = " AND jc_emp_id IN (" + advisor_id + ")";
					}
					if (!tech_id.equals("")) {
						technicianemp = " AND jc_technician_emp_id IN (" + tech_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ServiceBookingSummary();
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
			starttime = strToShortDate(ToShortDate(kknow()));
		}
		if (endtime.equals("")) {
			endtime = strToShortDate(ToShortDate(kknow()));
		}
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		zone_id = RetrunSelectArrVal(request, "dr_zone");
		zone_ids = request.getParameterValues("dr_zone");
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		model_id = RetrunSelectArrVal(request, "dr_model_id");
		model_ids = request.getParameterValues("dr_model_id");
		advisor_id = RetrunSelectArrVal(request, "dr_jc_emp_id");
		advisor_ids = request.getParameterValues("dr_jc_emp_id");
		tech_id = RetrunSelectArrVal(request, "dr_tech_emp_id");
		tech_ids = request.getParameterValues("dr_tech_emp_id");

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
				// SOP("starttime===" + starttime);
				// SOP("start_time===" + start_time);
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
				// SOP("endtime===" + endtime);
				// SOP("endtime===" + endtime);
				// SOP("end_time===" + end_time);
				// endtime = ToLongDate(AddHoursDate(StringToDate(endtime), 1,
				// 0, 0));
			} else {
				msg = msg + "<br>Enter Valid End Date!";
				endtime = "";
			}
		}

	}

	public String ServiceBookingSummary() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		int enquiry_total = 0;
		int serviced_total = 0;
		StringBuilder sob_ids = new StringBuilder();
		try {
			StrSql = " SELECT "
					+ " sob_id,"
					+ " COUNT(vehfollowup_id) AS enquiry,"
					// + " COALESCE (SUM(serviced), 0) AS serviced,"
					+ " COALESCE ((SELECT COUNT(DISTINCT jc_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_veh_id = jc_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " WHERE 1 = 1"
					+ " AND SUBSTR(jc_time_in, 1, 8) >= SUBSTR(vehfollowup_enquiry_time,1,8)"
					+ " AND SUBSTR(vehfollowup_enquiry_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_enquiry_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";

			if (!advisor_id.equals("")) {
				StrSql += jobcardemp;
			}
			if (!tech_id.equals("")) {
				StrSql += technicianemp;
			}

			StrSql += StrSearch
					+ " AND vehfollowup_veh_id = veh_id"
					+ " AND vehfollowup_sob_id = sob_id), 0) AS serviced ,"

					+ " COALESCE(sob_name, '') AS sobname"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON sob_id = vehfollowup_sob_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id";

			if (!region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}
			if (!zone_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_zone ON zone_id = branch_zone_id";
			}
			if (!brand_id.equals("") || !model_id.equals("")) {
				StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";

				if (!model_id.equals("")) {
					// StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_brand_id = brand_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id";
					StrSql += " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = brand_id"
							+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = preownedmodel_carmanuf_id";
				}
			}
			// StrSql += " LEFT JOIN (SELECT COUNT(jc_id) AS serviced,jc_veh_id, jc_time_in, jc_emp_id, jc_technician_emp_id"
			// + " FROM " + compdb(comp_id) + "axela_service_jc"
			// + " WHERE 1=1";
			// if (!advisor_id.equals("")) {
			// StrSql += jobcardemp;
			// }
			// if (!tech_id.equals("")) {
			// StrSql += technicianemp;
			// }
			// StrSql += " GROUP BY jc_veh_id ) AS tblservice ON tblservice.jc_veh_id = veh_id"
			// + " AND SUBSTR(jc_time_in, 1, 8) >= SUBSTR(vehfollowup_enquiry_time, 1, 8) ";

			StrSql += " WHERE 1 = 1"
					+ " AND SUBSTR(vehfollowup_enquiry_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_enquiry_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			if (!StrSearch.equals("")) {
				StrSql += StrSearch;
			}

			StrSql += " GROUP BY sob_id "
					+ " ORDER BY sob_name";

			// SOP("StrSql----------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder multiSelect = new StringBuilder();
			multiSelect.append("&starttime=").append(starttime)
					.append("&endtime=").append(endtime)
					.append("&brand_id=").append(brand_id)
					.append("&region_id=").append(region_id)
					.append("&zone_id=").append(zone_id)
					.append("&branch_id=").append(branch_id)
					.append("&model_id=").append(model_id)
					.append("&advisor_id=").append(advisor_id)
					.append("&tech_id=").append(tech_id)
					.append(" target=_blank");

			if (crs.isBeforeFirst()) {
				Str.append("<div>\n");
				Str.append("<table class=\"table  table-bordered table-hover sticky-header \" data-filter=\"#filter\">\n");
				Str.append("<thead style=\"margin-top:50px;\">");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">SOB</th>\n");
				Str.append("<th data-hide=\"phone\">Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\">Serviced</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					enquiry_total += crs.getInt("enquiry");
					serviced_total += crs.getInt("serviced");
					sob_ids = sob_ids.append(crs.getString("sob_id") + ",");
					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=center>").append(crs.getString("sobname")).append("</td>\n");
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&enquiry=yes&sob_id=");
					Str.append(crs.getString("sob_id") + "&").append(multiSelect + ">").append(crs.getString("enquiry")).append("</a></td>");
					Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&serviced=yes&sob_id=");
					Str.append(crs.getString("sob_id") + "&").append(multiSelect + ">").append(crs.getString("serviced")).append("</a></td>");
					Str.append("</tr>");
				}
				Str.append("<tr align=center>\n");
				Str.append("<td colspan='2' align=right><b>Total:</b></td>");
				Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&enquiry=yes&sob_ids=" + sob_ids);
				Str.append(multiSelect + "><b>").append(enquiry_total).append("</b></a></td>");
				Str.append("<td valign=top align=right><a href=").append(SearchURL).append("filter=yes&serviced=yes&sob_ids=" + sob_ids);
				Str.append(multiSelect + "><b>").append(serviced_total).append("</b></a></td>");
				Str.append("</tr>");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color='red'>No Service Enquiry found!</font></b>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	private void SoeDetails(HttpServletRequest request, HttpServletResponse response) {

		try {
			HttpSession session = request.getSession(true);
			String starttime = CNumeric(PadQuotes(request.getParameter("starttime")));
			String endtime = CNumeric(PadQuotes(request.getParameter("endtime")));
			String enquiry = PadQuotes(request.getParameter("enquiry"));
			String serviced = PadQuotes(request.getParameter("serviced"));
			String brand_id = PadQuotes(RetrunSelectArrVal(request, "brand_id"));
			String zone_id = PadQuotes(RetrunSelectArrVal(request, "zone_id"));
			String region_id = PadQuotes(RetrunSelectArrVal(request, "region_id"));
			String branch_id = PadQuotes(RetrunSelectArrVal(request, "branch_id"));
			String model_id = PadQuotes(RetrunSelectArrVal(request, "model_id"));
			String advisor_id = PadQuotes(RetrunSelectArrVal(request, "advisor_id"));
			String tech_id = PadQuotes(request.getParameter("tech_id"));
			String sob_id = PadQuotes(request.getParameter("sob_id"));
			String sob_ids = PadQuotes(RetrunSelectArrVal(request, "sob_ids"));
			if (!sob_ids.equals("")) {
				sob_ids = CleanArrVal(sob_ids);
			}
			// SOP("brand_id===" + brand_id);
			// SOP("region_id===" + region_id);
			// SOP("zone_id===" + zone_id);
			// SOP("branch_id===" + branch_id);
			// SOP("model_id===" + model_id);
			// SOP("advisor_id===" + advisor_id);
			// SOP("tech_id===" + tech_id);
			// SOP("serviced===" + serviced);
			// SOP("enquiry===" + enquiry);
			// SOP("starttime===" + starttime);
			// SOP("endtime===" + endtime);

			// Brand
			if (!brand_id.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
			}
			// Regions
			if (!region_id.equals("")) {
				StrSearch += " AND branch_region_id IN (" + region_id + ")";
			}
			// Zone
			if (!zone_id.equals("")) {
				StrSearch += " AND branch_zone_id IN (" + zone_id + ")";
			}
			// Branch
			if (!branch_id.equals("")) {
				StrSearch += " AND branch_id IN (" + branch_id + ")";
			}
			// Models
			if (!model_id.equals("")) {
				StrSearch += " AND preownedmodel_id IN (" + model_id + ")";
			}

			StrSearch += " AND SUBSTR(vehfollowup_enquiry_time, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(vehfollowup_enquiry_time, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			if (!sob_id.equals("")) {
				StrSearch += " AND vehfollowup_sob_id = " + sob_id;
			}

			if (!sob_ids.equals("")) {
				StrSearch += " AND vehfollowup_sob_id IN (" + sob_ids + ")";
			}

			if (enquiry.equals("yes")) {
				StrFilter = " AND veh_id IN ("
						+ " SELECT "
						+ " DISTINCT veh_id "
						+ " FROM	" + compdb(comp_id) + "axela_service_followup"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id "
						+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " WHERE 1 = 1 "
						+ StrSearch
						+ " GROUP BY veh_id"
						+ ")";
				// SOP("StrFilter==" + StrFilter);
				SetSession("vehstrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../service/vehicle-list.jsp?smart=yes"));
			}
			if (serviced.equals("yes")) {
				if (!advisor_id.equals("")) {
					StrSearch += " AND jc_emp_id IN (" + advisor_id + ")";
				}
				if (!tech_id.equals("")) {
					StrSearch += " AND jc_technician_emp_id IN (" + tech_id + ")";
				}

				StrFilter = " AND jc_id IN ("
						+ " SELECT "
						+ " DISTINCT jc_id "
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_followup ON vehfollowup_veh_id = jc_veh_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehfollowup_veh_id "
						+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id "
						+ " WHERE 1 = 1 "
						+ " AND SUBSTR(jc_time_in, 1, 8) >= SUBSTR(vehfollowup_enquiry_time,1,8)"
						+ StrSearch
						+ ")";

				SetSession("jcstrsql", StrFilter, request);
				response.sendRedirect(response.encodeRedirectURL("../service/jobcard-list.jsp?smart=yes"));
			}

		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
