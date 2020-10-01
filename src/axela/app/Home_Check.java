// Ved (30 Jan 2013)
package axela.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Home_Check extends Connect {

	public String StrSql = "", StrHTML = "";
	public String emp_id = "";
	public String from_date = "", to_date = "";
	public String ajax = "";
	public String comp_id = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				ajax = PadQuotes(request.getParameter("ajax"));
				if (ajax.equals("yes")) {
					from_date = PadQuotes(request.getParameter("from"));
					to_date = PadQuotes(request.getParameter("to"));
					StrHTML = TodayDetails();
				}
			}
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	public String TodayDetails() {
		StringBuilder Str = new StringBuilder();
		try {
			String fromdate = "", todate = "";
			fromdate = ConvertShortDateToStr(from_date).substring(0, 8);
			todate = ConvertShortDateToStr(to_date).substring(0, 8);

			StrSql = "SELECT COUNT(DISTINCT model_id) as models,"
					+ " "
					+ " (SELECT COUNT(DISTINCT offers_id)"
					+ " FROM " + compdb(comp_id) + "axela_app_offers"
					+ " WHERE (SUBSTR(offers_entry_date, 1,8) >= '" + fromdate + "'"
					+ " AND SUBSTR(offers_entry_date, 1,8) <= '" + todate + "')) as offers,"
					+ " "
					+ " (SELECT COUNT(DISTINCT servicecenter_id)"
					+ " FROM " + compdb(comp_id) + "axela_app_servicecenter"
					+ " WHERE (SUBSTR(servicecenter_entry_date, 1,8) >= '" + fromdate + "'"
					+ " AND SUBSTR(servicecenter_entry_date, 1,8) <= '" + todate + "')) as servicecenters,"
					+ " "
					+ " (SELECT COUNT(DISTINCT showroom_id)"
					+ " FROM " + compdb(comp_id) + "axela_app_showroom"
					+ " WHERE (SUBSTR(showroom_entry_date, 1,8) >= '" + fromdate + "'"
					+ " AND SUBSTR(showroom_entry_date, 1,8) <= '" + todate + "')) as showrooms"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE (SUBSTR(model_entry_date, 1,8) >= '" + fromdate + "'"
					+ " AND SUBSTR(model_entry_date, 1,8) <= '" + todate + "')";
			// SOPError("StrSql--------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<table width=\"100%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"5\" class=\"myclass\">");
				Str.append("<tr align=\"center\" >");
				Str.append("<td align=\"center\" style=\"background-color:#DC143C\" onclick=\"Redirect(1);\" class=\"button float\"><div id=\"dbvalue\">" + crs.getString("models")
						+ "</div><div id=\"dblabel\">Models</div></td>");
				Str.append("<td align=\"center\" style=\"background-color:#9ACD32\" onclick=\"Redirect(2);\" class=\"button float\"><div id=\"dbvalue\">" + crs.getString("offers")
						+ "</div><div id=\"dblabel\">Offers</div></td>");
				Str.append("<td align=\"center\" style=\"background-color:#9400D3\" onclick=\"Redirect(3);\" class=\"button float\"><div id=\"dbvalue\">" + crs.getString("servicecenters")
						+ "</div><div id=\"dblabel\">Service Centers</div></td>");
				Str.append("<td align=\"center\" style=\"background-color:#48D1CC\" onclick=\"Redirect(4);\" class=\"button float\"><div id=\"dbvalue\">" + crs.getString("showrooms")
						+ "</div><div id=\"dblabel\">Showrooms</div></td>");
				Str.append("</tr>");
				Str.append("</table>");
				// 48D1CC - blue
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaAuto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
