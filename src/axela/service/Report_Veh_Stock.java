package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Veh_Stock extends Connect {

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
	public String jc_data = "";

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

				GetValues(request, response);
				if (go.equals("Go")) {
					if (!jc_stage_id.equals("")) {
						StrSearch += " AND jc_jcstage_id IN (" + jc_stage_id + ")";
					}

					if (!dr_branch_id.equals("0")) {
						StrSearch += " AND jc_branch_id = " + dr_branch_id + "";
					} else {
						msg = "Select Branch!";
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
	}

	public String ListJobCards() {
		try {
			StringBuilder Str = new StringBuilder();
			String now = ToLongDate(kknow());
			int count = 0;
			StrSql = "SELECT jc_id, jc_ro_no, jc_reg_no, COALESCE(veh_id, 0) AS veh_id, jc_time_in,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS empname, jcstage_name,"
					// + " (CONCAT(FLOOR(HOUR(TIMEDIFF(jc_time_in, '" + now + "'))/24), ':',"
					// + " MOD(HOUR(TIMEDIFF(jc_time_in, '" + now + "')), 24), ':',"
					// + " MINUTE(TIMEDIFF(jc_time_in, '" + now + "')), '')) AS tat"
					+ " COALESCE((DATE_FORMAT(CONCAT('2014-12-24 ' ,(CONCAT(FLOOR(HOUR(TIMEDIFF(jc_time_in, '" + now + "'))/24), ':',"
					+ " MOD(HOUR(TIMEDIFF(jc_time_in, '" + now + "')), 24), ':',"
					+ " MINUTE(TIMEDIFF(jc_time_in, '" + now + "')), ''))), '%H:%i:%s')), '') AS tat"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " WHERE 1 = 1" + StrSearch + ""
					+ " ORDER BY jc_time_in ASC"
					+ " LIMIT 1000";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th>#</th>\n");
				Str.append("<th>Job Card ID</th>\n");
				Str.append("<th>RO No.</th>\n");
				Str.append("<th>Reg. No.</th>\n");
				Str.append("<th>Vehicle ID</th>\n");
				Str.append("<th>Service Advisor</th>\n");
				Str.append("<th>Stage</th>\n");
				Str.append("<th>Time In</th>\n");
				Str.append("<th>TAT</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n<td align=\"center\">").append(count);
					Str.append("</td>\n<td align=\"center\">");
					Str.append("<a href=\"javascript:remote=window.open('jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("','jcdash','');remote.focus();\">");
					Str.append(crs.getString("jc_id")).append("</a>");
					Str.append("</td>\n<td align=\"center\">").append(crs.getString("jc_ro_no"));
					Str.append("</td>\n<td align=\"center\">").append(SplitRegNo(crs.getString("jc_reg_no"), 2));
					Str.append("</td>\n<td align=\"center\">");
					if (crs.getString("veh_id").equals("0")) {
						Str.append("<a href=\"../service/vehicle-update.jsp?add=yes\">Add Vehicle</a>");
					} else {
						Str.append("<a href=\"javascript:remote=window.open('vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("','vehicledash','');remote.focus();\">");
						Str.append(crs.getString("veh_id")).append("</a>");
					}
					Str.append("</td>\n<td align=\"left\">").append(crs.getString("empname"));
					Str.append("</td>\n<td align=\"center\">").append(crs.getString("jcstage_name"));
					Str.append("</td>\n<td align=\"center\">").append(strToLongDate(crs.getString("jc_time_in")));
					Str.append("</td>\n<td align=\"center\">").append(crs.getString("tat"));
					Str.append("</td>\n</tr>\n");
				}
				crs.close();
				Str.append("</table>\n");
			} else {
				Str.append("<font color=\"red\">No Job Card found!</font>");
			}
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

			Str.append("<select name=\"dr_jc_stage\" id=\"dr_jc_stage\" class=\"textbox\" multiple=\"multiple\" size=10 style=\"width:250px\">\n");
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
}
