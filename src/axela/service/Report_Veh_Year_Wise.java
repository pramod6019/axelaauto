package axela.service;
// S Nag,, 20,21 may 2013    
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Report_Veh_Year_Wise extends Connect {

	public String StrSql = "";
	public String emp_id = "0";
	public String StrHTML = "";
	public String comp_id = "0";
	public String[] brand_ids, region_ids, branch_ids;
	public String sale_date_from = "", sale_date_to = "";
	public String go = "";
	public String brand_id = "", region_id = "", branch_id = "", StrSearch = "";
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_report_access, emp_mis_access", request, response);
				go = PadQuotes(request.getParameter("submit_button"));
				GetValues(request, response);
				if (go.equals("Go")) {

					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id IN (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch += " AND branch_id IN (" + branch_id + ")";
					}
					StrHTML = ListVehYearWise();
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
		branch_id = RetrunSelectArrVal(request, "dr_branch_id");
		branch_ids = request.getParameterValues("dr_branch_id");
	}
	public String ListVehYearWise() {

		StringBuilder Str = new StringBuilder();
		int total_count = 0;
		try {
			StrSql = "SELECT COALESCE(SUBSTR(veh_sale_date,1,4), '') as vehyear, COALESCE(count(veh_id), 0) as vehcount"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ "	INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id=veh_branch_id "
					+ " WHERE 1=1"
					+ StrSearch
					+ " GROUP BY SUBSTR(veh_sale_date,1,4)"
					+ " ORDER BY SUBSTR(veh_sale_date,1,4)";
			SOP("ListVehYearWise======= " + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"portlet box\">");
				Str.append("<div class=\"portlet-title\" style=\"text-align:center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">");
				Str.append("Veh. Year Wise </div> </div>");
				Str.append("<div class=\"portlet-body portlet-empty container-fluid\">");
				Str.append("<div class=\"tab-pane\">");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover   \" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th data-toggle=\"true\">Year</th>\n");
				Str.append("<th>Total Vehicle Count</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");

				int count = 0;

				while (crs.next()) {

					count++;
					Str.append("<tr>");
					Str.append("<td valign=top align=center>").append(count).append("</td>");
					Str.append("<td valign=top align=center>").append(crs.getString("vehyear")).append("</td>");
					Str.append("<td valign=top align=right>").append(crs.getString("vehcount")).append("</td>");
					total_count += Integer.parseInt(crs.getString("vehcount"));
				}
				Str.append("</tr>\n");
				Str.append("<tr>");
				Str.append("<td valign=top align=right colspan=2><b>Total: </b></td>");
				Str.append("<td valign=top align=right><b>").append(total_count).append("</b></td>");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div> </div> </div>");
			} else {
				Str.append("<font color=red><b>No Details Found!</b></font>");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
