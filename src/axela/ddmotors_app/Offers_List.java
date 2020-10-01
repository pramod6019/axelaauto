package axela.ddmotors_app;

//@Shilpashree

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Offers_List extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String user_id = "0";
	public String comp_id = "0";
	public String offers_id = "0";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String msg = "";
	public String RecCountDisplay = "";
	public String brand_id = "0", page_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				user_id = CNumeric(PadQuotes(request.getParameter("user_id")));
				offers_id = CNumeric(PadQuotes(request
						.getParameter("offers_id")));
				page_id = CNumeric(PadQuotes(request.getParameter("page_id")));
				StrHTML = ListOffers(response);
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String ListOffers(HttpServletResponse response) {
		String offertype_name = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT offers_id, offers_topic, offers_desc, offertype_name, offers_date, "
					+ " offers_offertype_id, CAST(offertype_name AS CHAR) AS  offertype_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_app_offers"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_app_offers_type ON offertype_id = offers_offertype_id"
					+ " WHERE offers_active = 1"
					+ " GROUP BY offers_id"
					+ " ORDER BY offers_offertype_id, offers_id DESC";

			// SOP("StrSql================//===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"panel-group accordion scrollable\" id=\"accordion2\">");
				while (crs.next()) {
					if (!crs.getString("offertype_name").equals(offertype_name)) {
						offertype_name = crs.getString("offertype_name");

						if (count != 0) {
							Str.append("</div>\n");
							Str.append("</div>\n");
						}
						Str.append("<div class=\"panel panel-default\">")
								.append("<div class=\"panel-heading\">")
								.append("<h5 class=\"panel-title\">")
								.append("<a class=\"accordion-toggle\" data-toggle=\"collapse\" data-parent=\"#accordion2\" href=\"#collapse_"
										+ crs.getString("offers_id") + "\">\n")
								.append(crs.getString("offertype_name"))
								.append("</a></h5></div><br>\n");
						// if (count == 0) {
						// Str.append("<div id=\"collapse_" +
						// crs.getString("offers_id") +
						// "\" class=\"panel-collapse in\">\n");
						// } else {

						Str.append("<div id=\"collapse_"
								+ crs.getString("offers_id")
								+ "\" class=\"panel-collapse collapse\">\n");

						// }
					}

					// Str.append("<div class=\"panel-body\">\n")
					// .append("<table class=\"table table-responsive\">\n")
					// .append("<tr>")
					// //
					// .append("<td class=\"col-md-4 col-xs-4\"><b>Topic:").append("</b></td>\n")
					// .append("<td class=\"col-md-8 col-xs-8\">")
					// .append("<h4>").append(unescapehtml(crs.getString("offers_topic"))).append("</h4><br>\n")
					// .append(unescapehtml(crs.getString("offers_desc"))).append("</td>")
					// .append("</tr></table></div>\n");

					Str.append("<div class=\"container\">")
							.append("<div class=\"row col-md-12\"><b>\n")
							.append("<h4>")
							.append(crs.getString("offers_topic"))
							.append("</h4></b>").append("<p>")
							.append(unescapehtml(crs.getString("offers_desc")))
							.append("</p><br>\n").append("</div></div>");

					// Str.append("<div class=\"panel-body\">\n")
					// .append("<table class=\"table table-responsive\">")
					// .append("<tr><td class=\"col-md-4 col-xs-4\"><b>Offers Description:").append("</b></td>\n")
					// .append("<td class=\"col-md-8 col-xs-8\"><b>").append(unescapehtml(crs.getString("offers_desc"))).append("<br>\n")
					// .append("</b></td>")
					// .append("</tr></table></div><br>\n");

					count++;

				}
				Str.append("</div>\n");
				Str.append("</div>");

			} else {
				response.sendRedirect(response
						.encodeRedirectURL("../ddmotors-app/offers-list.jsp?msg=No Offers found!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}

		return Str.toString();
	}
}
