package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_JobCard_ControlBoard1 extends Connect {

	public String jc_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String StrSql = "", StrSearch = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String branch_id = "0";
	public String dr_branch_id = "0", go = "";
	public String[] jc_stage_ids;
	public String jc_stage_id = "0";
	public String[] jc_type_ids;
	public String jc_type_id = "0";
	public String jc_data = "";
	public String ModelJoin = "";
	public String[] technicianexe_ids, advisorexe_ids, model_ids;
	public String technicianexe_id = "0", advisorexe_id = "0", model_id = "0";
	public Report_Check reportexe = new Report_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_service_jobcard_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));
				reportexe.status = "yes";

				GetValues(request, response);
				if (go.equals("Go")) {
					if (!jc_stage_id.equals("")) {
						StrSearch += " AND jc_jcstage_id IN (" + jc_stage_id + ")";
					}

					if (!jc_type_id.equals("")) {
						StrSearch += " AND jc_jctype_id IN (" + jc_type_id + ")";
					}

					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND jc_branch_id = " + dr_branch_id + "";
					} else {
						msg = "Select Branch!";
					}

					if (!technicianexe_id.equals("")) {
						StrSearch += " AND jc_technician_emp_id IN (" + technicianexe_id + ")";
					}

					if (!advisorexe_id.equals("")) {
						StrSearch += " AND jc_emp_id IN (" + advisorexe_id + ")";
					}

					if (!model_id.equals("")) {
						ModelJoin = " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
						StrSearch += " AND item_model_id IN (" + model_id + ")";
					}

					if (msg.equals("")) {
						StrSearch += " AND jc_time_out = ''";
						jc_data = ListJobCards();
						SetSession("jcstrsql", StrSearch, request);
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

		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		jc_stage_id = RetrunSelectArrVal(request, "dr_jc_stage");
		jc_stage_ids = request.getParameterValues("dr_jc_stage");

		jc_type_id = RetrunSelectArrVal(request, "dr_jc_type");
		jc_type_ids = request.getParameterValues("dr_jc_type");

		technicianexe_id = RetrunSelectArrVal(request, "dr_technician");
		technicianexe_ids = request.getParameterValues("dr_technician");

		advisorexe_id = RetrunSelectArrVal(request, "dr_advisor");
		advisorexe_ids = request.getParameterValues("dr_advisor");

		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
	}

	public String ListJobCards() {
		try {
			StringBuilder Str = new StringBuilder();
			String now = ToLongDate(kknow());
			int count = 0;
			StrSql = "SELECT jc_id, COALESCE(veh_reg_no, '') AS veh_reg_no, COALESCE(veh_id, 0) AS veh_id,"
					+ " CONCAT(adv.emp_name, ' (', adv.emp_ref_no, ')') AS adv_empname, jcstage_name, jc_notes,"
					+ " COALESCE(MIN(baytrans_start_time), '') AS starttime, COALESCE(MAX(baytrans_end_time), '') AS endtime,"
					+ " COALESCE(CONCAT(tech.emp_name, ' (', tech.emp_ref_no, ')'), '') AS tech_empname,"
					+ " COALESCE(customer_id, 0) AS customer_id, COALESCE(customer_name, '') AS customer_name,"
					+ " COALESCE(item_id, 0) AS item_id, jcstage_colour, jctype_name, jc_time_in, jc_ro_no,"
					+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS item_name,"
					+ " COALESCE(veh_chassis_no, '') AS veh_chassis_no, jc_time_in, jc_time_out, jc_time_promised,"
					+ " COALESCE((DATE_FORMAT(CONCAT('2014-12-24 ', (CONCAT(FLOOR(HOUR(TIMEDIFF(jc_time_in, '" + now + "'))/24), ':',"
					+ " MOD(HOUR(TIMEDIFF(jc_time_in, '" + now + "')), 24), ':',"
					+ " MINUTE(TIMEDIFF(jc_time_in, '" + now + "')), ''))), '%H:%i:%s')), '') AS tat"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp adv ON adv.emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp tech ON tech.emp_id = jc_technician_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_bay_trans ON baytrans_jc_id = jc_id"
					+ " WHERE 1 = 1" + StrSearch + ""
					+ " GROUP BY jc_id"
					+ " ORDER BY jc_time_in ASC"
					+ " LIMIT 1000";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"3\" style=\"border-collapse:collapse;border-color:#726a7a;padding:3px;\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Item</th>\n");
				Str.append("<th>Job Card</th>\n");
				Str.append("<th>Reg. No.</th>\n");
				Str.append("<th>Chassis No.</th>\n");
				Str.append("<th>RO No.</th>\n");
				Str.append("<th>Remarks</th>\n");
				Str.append("<th>Service Advisor</th>\n");
				Str.append("<th>Technician</th>\n");
				Str.append("<th>Start Time</th>\n");
				Str.append("<th>End Time</th>\n");
				Str.append("<th>Tentative Delivery Date</th>\n");
				Str.append("<th>Stage</th>\n");
				Str.append("<th>Type</th>\n");
				Str.append("<th>TAT</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr id=" + crs.getString("jc_id") + " class=manhours valign=\"top\" bgcolor=\"").append(crs.getString("jcstage_colour")).append("\">\n")
							.append("<td align=\"center\">").append(count);
					Str.append("</td>\n<td align=\"left\">");
					if (!crs.getString("customer_id").equals("0")) {
						Str.append("<a href=\"../portal/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a>");
					}
					Str.append("&nbsp;</td>\n<td align=\"left\">");
					if (!crs.getString("item_id").equals("0")) {
						Str.append("<a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id")).append("\">");
						Str.append(crs.getString("item_name")).append("</a>");
					}
					Str.append("&nbsp;</td>\n<td align=\"center\">");
					Str.append("<a href=\"javascript:remote=window.open('jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("','jcdash','');remote.focus();\">");
					Str.append(crs.getString("jc_id")).append("</a>");
					Str.append("</td>\n<td align=\"center\">");
					if (!crs.getString("veh_id").equals("0")) {
						Str.append("<a href=\"javascript:remote=window.open('vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("','vehicledash','');remote.focus();\">");
						Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
					}
					Str.append("&nbsp;</td>\n<td align=\"center\">");
					Str.append(crs.getString("veh_chassis_no"));
					// Str.append("</td>\n<td align=\"center\">").append("<a href=\"javascript:manhourdetails("+crs.getString("jc_id")+");\">").append(crs.getString("veh_chassis_no"));
					Str.append("</td>\n<td align=\"center\">").append(crs.getString("jc_ro_no"));
					Str.append("</td>\n<td align=\"left\">").append(crs.getString("jc_notes"));
					Str.append("</td>\n<td align=\"left\">").append(crs.getString("adv_empname"));
					Str.append("</td>\n<td align=\"left\">").append(crs.getString("tech_empname"));
					Str.append("</td>\n<td align=\"center\">");

					if (!crs.getString("starttime").equals("")) {
						// Str.append("<div class=\"tooltip\">");;
						Str.append(strToLongDate(crs.getString("starttime")));
						// Str.append("</div>");
					}
					Str.append("</td>\n<td align=\"center\">");
					if (!crs.getString("endtime").equals("")) {
						Str.append(strToLongDate(crs.getString("endtime")));
					}
					Str.append("&nbsp;</td>\n<td align=\"center\">");
					if (Long.parseLong(crs.getString("jc_time_promised")) < Long.parseLong(ToLongDate(kknow()))) {
						Str.append("<div id=\"div_promised_time\"><b>").append(strToLongDate(crs.getString("jc_time_promised"))).append("</b></div>");
					} else {
						Str.append(strToLongDate(crs.getString("jc_time_promised")));
					}
					Str.append("</td>\n<td align=\"center\">").append(crs.getString("jcstage_name"));
					Str.append("</td>\n<td align=\"center\">").append(crs.getString("jctype_name"));
					Str.append("</td>\n<td align=\"center\">").append(crs.getString("tat"));
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<font color=\"red\"><b>No Job Card found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobCardStage() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jcstage_id, jcstage_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " WHERE jcstage_id != 6"
					+ " GROUP BY jcstage_id"
					+ " ORDER BY jcstage_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_jc_stage\" id=\"dr_jc_stage\" class=\"textbox\" multiple=\"multiple\" size=\"10\" style=\"width:130px\">\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jcstage_id"));
				Str.append(ArrSelectdrop(crs.getInt("jcstage_id"), jc_stage_ids));
				Str.append(">").append(crs.getString("jcstage_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateJobCardType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " GROUP BY jctype_id"
					+ " ORDER BY jctype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_jc_type\" id=\"dr_jc_type\" class=\"textbox\" multiple=\"multiple\" size=\"10\" style=\"width:130px\">\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id"));
				Str.append(ArrSelectdrop(crs.getInt("jctype_id"), jc_type_ids));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(ArrSelectdrop(crs.getInt("model_id"), model_ids));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
