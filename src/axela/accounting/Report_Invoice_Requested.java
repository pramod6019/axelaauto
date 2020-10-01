package axela.accounting;

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Invoice_Requested extends Connect {

	public String StrSql = "";
	public String starttime = "", start_time = "";
	public String endtime = "", end_time = "";
	public String comp_id = "0";
	public static String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, soe_ids;
	public String preowned_model = "", include_inactive_exe = "", include_preowned = "";
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "", soe_id = "";
	public String StrHTML = "";
	public String BranchAccess = "", emp_copy_access = "0", dr_branch_id = "0";
	public String go = "", chk_team_lead = "0";
	public String dr_totalby = "0", dr_orderby = "0";
	public String ExeAccess = "";
	public String targetendtime = "";
	public String targetstarttime = "";
	public String branch_name = "";
	public String StrModel = "";
	public String StrSoe = "";
	public String StrExe = "";
	public String StrBranch = "";
	public String StrSearch = "";
	public String TeamSql = "";
	public String emp_all_exe = "";
	public String SearchURL = "";
	DecimalFormat deci = new DecimalFormat("0.00");
	public axela.sales.MIS_Check1 mischeck = new axela.sales.MIS_Check1();
	public String marital_status = "", emp_active = "", sex = "", address = "", Img = "";
	public String currexp[];
	public String emp_prevexp[];
	public int years = 0;
	public int months = 0;
	public int days = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				go = PadQuotes(request.getParameter("submit_button"));
				emp_copy_access = ReturnPerm(comp_id, "emp_copy_access", request);
				if (go.equals("")) {
					msg = "";
				}

				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();

					StrSearch = BranchAccess;
					// SOP("StrSearch===in mb=" + StrSearch);
					StrSearch += ExeAccess;
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						mischeck.exe_branch_id = branch_id;
						StrSearch = StrSearch + " AND branch_id IN (" + branch_id + ")";
					}
					if (!team_id.equals("")) {
						StrSearch = StrSearch + " AND team_id IN (" + team_id + ") ";
					}
					if (!exe_id.equals("")) {
						StrSearch = StrSearch + " AND so_emp_id IN (" + exe_id + ")";
					}
					if (!model_id.equals("")) {
						StrSearch = StrSearch + " AND model_id IN (" + model_id + ")";
					}
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ListData();
					}
				}

			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
		model_id = RetrunSelectArrVal(request, "dr_model");
		model_ids = request.getParameterValues("dr_model");
		exe_id = RetrunSelectArrVal(request, "dr_executive");
		exe_ids = request.getParameterValues("dr_executive");
		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		team_id = RetrunSelectArrVal(request, "dr_team");
		team_ids = request.getParameterValues("dr_team");
	}

	protected void CheckForm() {
		msg = "";
		if (starttime.equals("")) {
			msg = msg + "<br>Select Start Date!";
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
			msg = msg + "<br>Select End Date!<br>";
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

	public String PopulateTeam() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT team_id, team_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_team "
					+ " WHERE team_branch_id=" + dr_branch_id + " "
					+ " AND team_active = 1"
					+ " GROUP BY team_id "
					+ " ORDER BY team_name ";
			// SOP("PopulateTeam query ==== " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("team_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("team_id"), team_ids));
				Str.append(">").append(crs.getString("team_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String ListData() {
		int count = 0;
		String StrSql = "";
		StringBuilder Str = new StringBuilder();
		try {

			StrSql = "SELECT"
					+ " so_id,"
					+ " so_quote_id,"
					+ " so_grandtotal,"
					+ " so_invoice_request_emp_id,"
					+ " COALESCE (request.emp_name, '') AS request_emp,"
					+ " so_emp_id,"
					+ " COALESCE (so.emp_name, '') AS so_emp"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp request ON request.emp_id = so_invoice_request_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp so ON so.emp_id = so_emp_id";

			if (!team_id.equals("")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id ";
			}

			if (!model_id.equals("")) {
				StrSql += " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id";
			}

			StrSql += " WHERE 1 = 1"
					+ " AND so_invoice_request = 1"
					+ " AND so_retail_date = ''"
					+ " AND so_active = 1"
					+ StrSearch
					+ " GROUP BY so_id"
					+ " ORDER BY so_entry_date DESC";

			// SOP("StrSql--------------------" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("<b>Conversion Summary</b>");
				Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\">SO Executive</th>\n");
				Str.append("<th>Quote ID</th>\n");
				Str.append("<th>SO ID</th>\n");
				Str.append("<th data-hide=\"phone\">SO Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Requested By</th>\n");
				Str.append("</tr>");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				crs.beforeFirst();
				while (crs.next()) {
					count++;
					Str.append("<tr align=center>\n");
					Str.append("<td align=center>").append(count).append("</td>");
					Str.append("<td align=left>").append(ExeDetailsPopover(crs.getInt("so_emp_id"), crs.getString("so_emp"), "")).append("</td>");
					Str.append("<td align=center>").append("<a href=../sales/veh-quote-list.jsp?quote_id=" + crs.getString("so_quote_id") + ">" + crs.getString("so_quote_id") + "</a>")
							.append("</td>");
					Str.append("<td align=center>").append("<a href=../sales/veh-salesorder-list.jsp?so_id=" + crs.getString("so_id") + ">" + crs.getString("so_id") + "</a>").append("</td>");
					Str.append("<td align=right>").append(IndDecimalFormat(deci.format(crs.getDouble("so_grandtotal")))).append("</td>");
					Str.append("<td align=left>").append(ExeDetailsPopover(crs.getInt("so_invoice_request_emp_id"), crs.getString("request_emp"), "")).append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>");
				Str.append("</div>\n");
			} else {
				Str.append("<font color='red'><b>No Result Found!</b></font>");
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
