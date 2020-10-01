package axela.insurance;
//Saiman 16th feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insurance_SOB_Dash extends Connect {

	public String submitB = "";
	public String msg = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "";
	public String[] brand_ids, region_ids, branch_ids, carmanuf_ids, model_ids, insur_emp_ids;
	public String brand_id = "", branch_id = "", region_id = "", carmanuf_id = "", model_id = "", insuremp_id = "";
	public String StrHTML = "", StrClosedHTML = "", Strhtml = "";
	public String BranchAccess = "", dr_branch_id = "0";
	public String StrSearch = "", insuremp = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String emp_all_exe = "";
	public MIS_Check mischeck = new MIS_Check();

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
					if (!branch_id.equals("")) {
						StrSearch += StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!carmanuf_id.equals("")) {
						StrSearch = StrSearch + " and carmanuf_id in (" + carmanuf_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " and preownedmodel_id in (" + model_id + ")";
					}
					if (!insuremp_id.equals("")) {
						insuremp = " AND insurpolicy_emp_id IN (" + insuremp_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = InsuranceSOBBookingSummary();
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
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
		carmanuf_id = RetrunSelectArrVal(request, "dr_manufacturer");
		carmanuf_ids = request.getParameterValues("dr_manufacturer");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		insuremp_id = RetrunSelectArrVal(request, "dr_insur_emp_id");
		insur_emp_ids = request.getParameterValues("dr_insur_emp_id");

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

	public String InsuranceSOBBookingSummary() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			StrSql = " SELECT "
					+ " COUNT(insurenquiry_id) AS enquiry,"
					+ " COALESCE (SUM(policy), 0) AS policy,"
					+ " COALESCE(sob_name, '') AS sobname"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_sob_id = sob_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id";

			if (!region_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id";
			}
			if (!brand_id.equals("") || !model_id.equals("")) {
				StrSql += " INNER JOIN axelaauto.axela_brand ON brand_id = branch_brand_id";
			}
			if (!carmanuf_id.equals("")) {
				StrSql += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
			}
			StrSql += " LEFT JOIN (SELECT COUNT(insurpolicy_id) AS policy, insurpolicy_insurenquiry_id, insurpolicy_entry_date, insurpolicy_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_insurance_policy"
					+ " WHERE 1=1";

			if (!insuremp_id.equals("")) {
				StrSql += insuremp;
			}
			StrSql += " GROUP BY insurpolicy_insurenquiry_id ) AS tblinsur ON tblinsur.insurpolicy_insurenquiry_id = insurenquiry_id"
					+ " AND SUBSTR(insurpolicy_entry_date, 1, 8) >= SUBSTR(insurenquiry_entry_date, 1, 8) ";

			StrSql += " WHERE 1 = 1"
					+ " AND SUBSTR(insurenquiry_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)"
					+ " AND SUBSTR(insurenquiry_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
			if (!StrSearch.equals("")) {
				StrSql += StrSearch;
			}

			StrSql += " GROUP BY sob_id "
					+ " ORDER BY sob_name";

			// SOP("StrSql----------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover sticky-header \" data-filter=\"#filter\">\n");
				Str.append("<thead style=\"margin-top:50px;\">");
				Str.append("<tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th data-hide=\"phone\">SOB</th>\n");
				Str.append("<th data-hide=\"phone\">Enquiry</th>\n");
				Str.append("<th data-hide=\"phone\">Policy Booked</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				crs.last();
				crs.beforeFirst();
				while (crs.next()) {
					count++;

					Str.append("<tr align=left valign=top>\n");
					Str.append("<td align=center>").append(count).append("</td>");

					Str.append("<td align=center>").append(crs.getString("sobname")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("enquiry")).append("</td>\n");
					Str.append("<td align=right>").append(crs.getString("policy")).append("</td>\n");

					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<b><font color='red'>No Insurance Enquiry found!</font></b>");
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
