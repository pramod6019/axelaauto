package axela.preowned;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Stock_Ageing extends Connect {

	public String StrSql = "";
	public static String msg = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String LinkHeader = "";
	public String preownedstock_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_report_access, emp_mis_access, emp_preowned_access", request, response);
				preownedstock_id = PadQuotes(request.getParameter("preownedstock_id"));
				if (!preownedstock_id.equals("0")) {
					// LinkHeader = "<a href=../portal/home.jsp>Home</a>"
					// + " &gt; <a href=../preowned/index.jsp>Pre Owned</a>"
					// + " &gt; <a href=../preowned/preowned-stock.jsp>Stock</a>"
					// + " &gt; <a href=preowned-stock-list.jsp?preownedstock_id=" + preownedstock_id + ">List Stocks</a>"
					// + " &gt; <a href=preowned-stock-ageing.jsp?preownedstock_id=" + preownedstock_id + ">Stock Ageing Status</a>:";
					msg = "Ageing Status for True-Value Stock ID = " + preownedstock_id + "!";
					StrHTML = AgeingStatus();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String AgeingStatus() {
		StringBuilder Str = new StringBuilder();
		String prev_date = "";
		int days = 0;
		try {
			int count = 0;
			StrSql = "SELECT preownedstockgatepass_id, preownedstockgatepass_preownedstock_id,"
					+ " preownedstockgatepass_time, preownedstockgatepass_from_location_id, branch_id,"
					+ " preownedstockgatepass_to_location_id, preownedstockgatepass_driver_id, driver_name,"
					+ " CONCAT(fromloc.preownedlocation_name, ' (', fromloc.preownedlocation_id, ')') AS from_location_name,"
					+ " CONCAT(toloc.preownedlocation_name, ' (', toloc.preownedlocation_id, ')') AS to_location_name,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, preownedstock_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock_gatepass"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = preownedstockgatepass_preownedstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_driver ON driver_id = preownedstockgatepass_driver_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location fromloc ON fromloc.preownedlocation_id = preownedstockgatepass_from_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location toloc ON toloc.preownedlocation_id = preownedstockgatepass_to_location_id"
					+ " WHERE preownedstockgatepass_preownedstock_id = " + preownedstock_id + ""
					+ " GROUP BY preownedstockgatepass_id"
					+ " ORDER BY preownedstockgatepass_time";
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead>\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>From</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">To</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Driver</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Day(s)</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					if (prev_date.equals("")) {
						prev_date = crs.getString("preownedstock_date");
					}
					days = (int) getDaysBetween(prev_date, crs.getString("preownedstockgatepass_time"));
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=\"managepreownedlocation.jsp?preownedlocation_id=");
					Str.append(crs.getString("preownedstockgatepass_from_location_id")).append("\">");
					Str.append(crs.getString("from_location_name")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=\"managepreownedlocation.jsp?preownedlocation_id=");
					Str.append(crs.getString("preownedstockgatepass_to_location_id")).append("\">");
					Str.append(crs.getString("to_location_name")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=\"../sales/managetestdrivedriver.jsp?driver_id=");
					Str.append(crs.getString("preownedstockgatepass_driver_id")).append("\">");
					Str.append(crs.getString("driver_name")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("preownedstockgatepass_time"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(days).append(" Day(s)");
					Str.append("</td>\n</tr>\n");
					prev_date = crs.getString("preownedstockgatepass_time");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<font color=\"red\"><b>No Gate Pass found!</b></font>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
