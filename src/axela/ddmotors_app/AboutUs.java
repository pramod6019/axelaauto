package axela.ddmotors_app;
//Shilpashree

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class AboutUs extends Connect {

	public String StrSql = "";
	public String user_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String SqlJoin = "";
	public String model_id = "0", page_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				page_id = CNumeric(PadQuotes(request.getParameter("page_id")));
				msg = PadQuotes(request.getParameter("msg"));
				StrHTML = AboutUsData(response);
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String AboutUsData(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT appconfig_aboutus" + " FROM " + compdb(comp_id) + "axela_app_config";

			// SOP("StrSql=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);
			String Img = "";
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<div class=\"portlet light\"><p class=\"abtus\">")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + unescapehtml(crs.getString("appconfig_aboutus")))
							.append("</div>");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}
}
