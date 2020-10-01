package axela.ddmotors_app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ServiceCenter_List extends Connect {

	public String user_id = "";
	public String StrHTML = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String msg = "";
	public String servicecenter_id = "0";
	public String RecCountDisplay = "";
	public String comp_id = "0";
	public String page_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
			if (!comp_id.equals("0")) {
				page_id = CNumeric(PadQuotes(request.getParameter("page_id")));
				StrHTML = ListData(response);
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String ListData(HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT CAST(servicecenter_name AS CHAR) AS servicecenter_name,"
					+ " CAST(servicecenter_address AS CHAR) AS servicecenter_address,"
					+ " CAST(state_name AS CHAR) AS state_name,"
					+ " CAST(servicecenter_pin AS CHAR) AS servicecenter_pin,"
					+ " CAST(city_name AS CHAR) AS city_name,"
					+ " servicecenter_mobile1, servicecenter_mobile2, servicecenter_phone1, servicecenter_phone2, servicecenter_id,"
					+ " servicecenter_email1, servicecenter_email2, servicecenter_website1, servicecenter_website2, "
					+ " COALESCE(servicecenter_latitude, '') AS latitude,"
					+ " COALESCE(servicecenter_longitude, '') AS longitude"
					+ " FROM " + compdb(comp_id) + "axela_app_servicecenter"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = servicecenter_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE 1 = 1"
					+ " AND servicecenter_active = 1"
					+ " GROUP BY servicecenter_id"
					+ " ORDER BY servicecenter_id ";
			if (!servicecenter_id.equals("0")) {
				StrSql = StrSql + " AND servicecenter_id = " + servicecenter_id;
			}
			// SOP("StrSql=====" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			String Img = "";
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"panel-group accordion scrollable\" id=\"accordion2\">");
				while (crs.next()) {

					Str.append("<div class=\"panel panel-default\">")
							.append("<div class=\"panel-heading\">")
							.append("<h4 class=\"panel-title\">")
							.append("<a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion2\" href=\"#collapse_" + crs.getString("servicecenter_id") + "\">\n")
							// .append(crs.getString("servicecenter_id"))
							// .append("\">\n")
							.append(crs.getString("servicecenter_name"))
							.append("</a></h4></div><br>\n");
					if (count == 0) {
						Str.append("<div id=\"collapse_" + crs.getString("servicecenter_id") + "\" class=\"panel-collapse in\">\n");
					} else {
						Str.append("<div id=\"collapse_" + crs.getString("servicecenter_id") + "\" class=\"panel-collapse collapse\">\n");
					}

					Str.append("<div class=\"panel-body\">\n")

							.append("<table class=\"table table-responsive\">\n")
							.append("<tr><td class=\"col-md-4 col-xs-4\"><b>Address:").append("</b></td>\n")

							.append("<td class=\"col-md-8 col-xs-8\"><b>").append(unescapehtml(crs.getString("servicecenter_address"))).append("<br>\n")
							.append(crs.getString("city_name"))
							.append("</b></td>")
							.append("</tr></table></div><br>\n");

					Str.append("<div class=\"panel-body\">\n")
							.append("<table class=\"table table-responsive\">")
							.append("<tr><td class=\"col-md-4 col-xs-4\"><b>Web:").append("</b></td>\n");

					if (!crs.getString("servicecenter_email1").equals("")) {
						Str.append("<td class=\"col-md-8 col-xs-8\"><span onclick=\"sendMail('" + crs.getString("servicecenter_email1") + "')\"><b>Email: \n")
								.append(crs.getString("servicecenter_email1"))
								.append("</b></span><br>\n");
					}
					if (!crs.getString("servicecenter_email2").equals("")) {
						Str.append("<span onclick=\"sendMail('" + crs.getString("servicecenter_email2") + "')\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>\n")
								.append(crs.getString("servicecenter_email2"))
								.append("</b></span><br>\n");
					}

					if (!crs.getString("servicecenter_website1").equals("")) {
						Str.append("<span onclick=\"openURL('" + crs.getString("servicecenter_website1") + "')\"><b>Web: \n")
								.append(crs.getString("servicecenter_website1"))
								.append("</b></span><br>");
					}
					if (!crs.getString("servicecenter_website2").equals("")) {
						Str.append("<span onclick=\"openURL('" + crs.getString("servicecenter_website2") + "')\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b> \n")
								.append(crs.getString("servicecenter_website2"))
								.append("</b></span><br>\n");
					}
					Str.append("</td>\n")
							.append("</tr></table></div><br>");

					Str.append("<div class=\"panel-body\"><table class=\"table table-responsive\">\n")

							.append("<tr> <td class=\"col-md-4 col-xs-4\"><b>Phone:").append("</b></td>")
							.append("<td class=\"col-md-8 col-xs-8\"><span onclick=\"callNo('" + crs.getString("servicecenter_phone1").replace("-", "") + "')\"><b>Tel: ")
							.append(crs.getString("servicecenter_phone1"))
							.append("</b></span><br>")
							.append("<span onclick=\"callNo('" + crs.getString("servicecenter_mobile1").replace("-", "") + "')\"><b>Mob: \n")
							.append(crs.getString("servicecenter_mobile1"))
							.append("</b></span></td>")
							.append("</tr></table></div><br>\n");

					Str.append("<div class=\"panel-body\"><table class=\"table table-responsive\">\n")

							.append("<tr><td class=\"col-md-4 col-xs-4\"><b>Location: ").append("</b></td>\n")

							.append("<td class=\"col-md-8 col-xs-8\"><b>")

							.append("<span onclick=\"openMap("
									+ "'" + crs.getString("latitude") + "',"
									+ "'" + crs.getString("longitude") + "',"
									+ "'" + crs.getString("servicecenter_name") + "',"
									+ "'" + crs.getString("servicecenter_address") + "'"
									+ ");\">")
							// .append("<span>")
							.append("Click here to locate us >>")
							.append("</span>")
							.append("</b></td>")
							.append("</tr></table></div></div></div>\n");
					count++;
				}
				Str.append("</div>");

			} else {
				msg = "No Service Centers Found!";
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
