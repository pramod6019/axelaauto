package axela.ddmotors_app;
//Shilpashree
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Showroom_List extends Connect {

	public String emp_id = "0";
	public String user_id = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String CountSql = "";
	public String msg = "";
	public String SqlJoin = "";
	public String comp_id = "0";
	public String model_id = "0", page_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				page_id = CNumeric(PadQuotes(request.getParameter("page_id")));
				msg = PadQuotes(request.getParameter("msg"));
				StrHTML = ListOurLocations(response);
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String ListOurLocations(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {

			// if (!user_id.equals("0")) {
			StrSql = "SELECT CAST(showroom_name AS CHAR) AS showroom_name,"
					+ " CAST(showroom_address AS CHAR) AS showroom_address,"
					+ " CAST(state_name AS CHAR) AS state_name,"
					+ " CAST(showroom_pin AS CHAR) AS showroom_pin,"
					+ " CAST(city_name AS CHAR) AS city_name,"
					+ " showroom_mobile1, showroom_mobile2, showroom_phone1, showroom_phone2,showroom_id,"
					+ " showroom_email1, showroom_email2, showroom_website1, showroom_website2, "
					+ " COALESCE(showroom_latitude, '') AS latitude,"
					+ " COALESCE(showroom_longitude, '') AS longitude"
					+ " FROM " + compdb(comp_id) + "axela_app_showroom"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = showroom_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE 1 = 1"
					+ " AND showroom_active = 1"
					+ " GROUP BY showroom_id"
					+ " ORDER BY showroom_id";
			// SOP("StrSql=======" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			String Img = "";
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"panel-group accordion scrollable\" id=\"accordion2\">");
				while (crs.next()) {

					Str.append("<div class=\"panel panel-default\">")
							.append("<div class=\"panel-heading\">")
							.append("<h4 class=\"panel-title\">")
							.append("<a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion2\" href=\"#collapse_" + crs.getString("showroom_id") + "\">\n")
							.append(crs.getString("showroom_name"))
							.append("</a></h4></div><br>\n");
					if (count == 0) {
						Str.append("<div id=\"collapse_" + crs.getString("showroom_id") + "\" class=\"panel-collapse in\">\n");
					} else {
						Str.append("<div id=\"collapse_" + crs.getString("showroom_id") + "\" class=\"panel-collapse collapse\">\n");
					}

					Str.append("<div class=\"panel-body\">\n")

							.append("<table class=\"table table-responsive\">\n")
							.append("<tr><td class=\"col-md-4 col-xs-4\"><b>Address:").append("</b></td>\n")

							.append("<td class=\"col-md-8 col-xs-8\"><b>").append(unescapehtml(crs.getString("showroom_address"))).append("<br>\n")
							.append(crs.getString("city_name"))
							.append("</b></td>")
							.append("</tr></table></div><br>\n");

					Str.append("<div class=\"panel-body\">\n")
							.append("<table class=\"table table-responsive\">")
							.append("<tr><td class=\"col-md-4 col-xs-4\"><b>Web:").append("</b></td>\n")

							.append("<td class=\"col-md-8 col-xs-8\"><span onclick=\"sendMail('" + crs.getString("showroom_email1") + "')\"><b>Email: \n")
							.append(crs.getString("showroom_email1"))
							.append("</b></span><br>\n")
							.append("<span onclick=\"openURL('" + crs.getString("showroom_website1") + "')\"><b>Web: \n")
							.append(crs.getString("showroom_website1"))
							.append("</b></span><br></td>\n")
							.append("</tr></table></div><br>");

					Str.append("<div class=\"panel-body\"><table class=\"table table-responsive\">\n")

							.append("<tr> <td class=\"col-md-4 col-xs-4\"><b>Phone:").append("</b></td>")
							.append("<td class=\"col-md-8 col-xs-8\"><span onclick=\"callNo('" + crs.getString("showroom_phone1").replace("-", "") + "')\"><b>Tel: ")
							.append(crs.getString("showroom_phone1"))
							.append("</b></span><br>")
							.append("<span onclick=\"callNo('" + crs.getString("showroom_mobile1").replace("-", "") + "')\"><b>Mob: \n")
							.append(crs.getString("showroom_mobile1"))
							.append("</b></span></td>")
							.append("</tr></table></div><br>\n");

					Str.append("<div class=\"panel-body\"><table class=\"table table-responsive\">\n")

							.append("<tr><td class=\"col-md-4 col-xs-4\"><b>Location: ").append("</b></td>\n")

							.append("<td class=\"col-md-8 col-xs-8\"><b>")

							.append("<span onclick=\"openMap("
									+ "'" + crs.getString("latitude") + "',"
									+ "'" + crs.getString("longitude") + "',"
									+ "'" + crs.getString("showroom_name") + "',"
									+ "'" + crs.getString("showroom_address") + "'"
									+ ");\">")
							.append("<span>")
							.append("Click here to locate us >>")
							.append("</b></td>")
							.append("</tr></table></div></div></div>\n");
					count++;
				}
				Str.append("</div>");

			} else {
				msg = "No Showrooms Found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}
}
