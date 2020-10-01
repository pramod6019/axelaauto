package axela.insurance;
//sangita
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Insur_LostCase extends Connect {

	public static String msg = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String emp_id = "", branch_id = "0";
	public String comp_id = "0";
	public String[] exe_ids, model_ids, carmanuf_ids;
	public String exe_id = "", model_id = "", carmanuf_id = "";
	public String StrHTML = "", StrClosedHTML = "";
	// public String BranchAccess = "";
	public String StrSearch = "";
	public String ExeAccess = "";
	public String chart_data = "";
	public int chart_data_total = 0;
	public String go = "";
	public String NoChart = "";
	public int TotalRecords = 0;
	public String StrSql = "";
	public MIS_Check mischeck = new MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
			if (!comp_id.equals("0")) {
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				CheckForm();
				if (go.equals("Go")) {
					StrSearch += ExeAccess;

					if (!starttime.equals("")) {
						StrSearch = StrSearch + " and SUBSTR(insurenquiry_entry_date, 1, 8) >= SUBSTR('" + starttime + "', 1, 8)";
					}
					if (!endtime.equals("")) {
						StrSearch = StrSearch + " and SUBSTR(insurenquiry_entry_date, 1, 8) <= SUBSTR('" + endtime + "', 1, 8)";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " and insurenquiry_emp_id in (" + exe_id + ")";
					}
					if (!carmanuf_id.equals("")) {
						StrSearch = StrSearch + " and carmanuf_id in (" + carmanuf_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " and preownedmodel_id in (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrClosedHTML = LostCaseSummary();
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

		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");

		carmanuf_id = RetrunSelectArrVal(request, "dr_manufacturer");
		carmanuf_ids = request.getParameterValues("dr_manufacturer");
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
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
			StrSql = "SELECT insurlostcase1_name, "
					+ " COUNT(insurlostcase1_id) AS Total";
			String CountSql = " SELECT COUNT(insurlostcase1_id)";
			String StrJoin = " FROM " + compdb(comp_id) + "axela_insurance_lostcase1"
					+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_insurlostcase1_id = insurlostcase1_id";

			if (!carmanuf_id.equals("")) {
				StrJoin += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id";
			}
			// if (!model_id.equals("") && carmanuf_id.equals("")) {
			// StrJoin += " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = insurenquiry_variant_id "
			// + " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id ";
			// }

			StrJoin += " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = insurenquiry_emp_id "
					+ " WHERE 1 = 1"
					+ StrSearch + "";

			CountSql = CountSql + StrJoin;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			StrJoin = StrJoin + " group by insurlostcase1_id "
					+ " ORDER BY Total DESC";
			StrSql = StrSql + StrJoin;
			// SOP("StrSql===insur lostcase===" + StrSql);
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
				}
				crs.beforeFirst();
				while (crs.next()) {
					// SOP("coming......2");
					Str.append("<tr>");
					Str.append("<td>").append(crs.getString("insurlostcase1_name")).append("</td>");
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
					chart_data = chart_data + "{'type': '" + crs.getString("insurlostcase1_name") + "', 'total':" + crs.getString("Total") + "}";
					chart_data_total = chart_data_total + crs.getInt("Total");
					if (count < TotalRecords) {
						chart_data = chart_data + ",";
					} else {
					}
				}
				chart_data = chart_data + "]";
				// SOP("chart_data = " + chart_data);
			} else {
				NoChart = "No Insurance Lost Case Summary Found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	public String PopulateInsurExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_insur = 1"
					+ " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
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
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_model_id = model_id"
					+ " where model_active = '1'"
					+ " and model_sales = '1'"
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
