package axela.ddmotors_app;
//Divya

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Model_Variant_Details extends Connect {

	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String StrHTML = "", StrHTMLdetails = "", StrHTMLFeature = "";
	public String pagecurrent = "";
	public String user_id = "";
	public String brand_id = "0";
	public String model_id = "0", item_id = "0";
	public String comp_id = "0";
	public int TotalRecords = 0;
	public String model_name = "", item_name = "", model_mileage = "", model_emi = "", model_engine = "";
	public String showroomprice = "";
	public String img = "", msg = "";
	public StringBuilder tab = new StringBuilder();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				// brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				item_id = ExecuteQuery("SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " WHERE item_model_id = " + model_id
						+ " AND item_active = 1 AND item_type_id = 1"
						+ " LIMIT 1 ");
				StrHTML = ListVariant(response);
				StrHTMLdetails = ListVariantDetails(response);
				StrHTMLFeature = ListVariantFeature(response);
			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String ListVariant(HttpServletResponse response) {
		int j = 0;
		int i = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_name, item_id, colours_id, colours_title, colours_colour, colours_value, "
					+ " COALESCE(price_amt-price_disc) As exshowroomprice, model_mileage, model_engine, model_emi "
					+ " FROM " + compdb(comp_id) + "axela_app_model_colours"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = colours_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " WHERE colours_model_id = " + model_id
					+ " GROUP BY colours_id "
					+ " ORDER BY colours_title ";
			// SOP("StrSql---" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div id=\"myCarousel\" class=\"carousel slide\" data-ride=\"carousel\">\n");
				Str.append("<ol class=\"carousel-indicators\" style=position:relative;top:250px>\n");
				while (crs.next()) {

					model_name = crs.getString("model_name");
					model_mileage = crs.getString("model_mileage");
					model_engine = crs.getString("model_engine");
					model_emi = crs.getString("model_emi");
					showroomprice = IndFormat(crs.getString("exshowroomprice"));
					if (!crs.getString("colours_value").equals("")) {

						if (i == 0) {
							Str.append("<li data-target=\"#myCarousel\" data-slide-to=" + i + " class=\"active\" style=\"background-color:" + crs.getString("colours_colour") + "\"></li>\n");
						} else {
							Str.append("<li data-target=\"#myCarousel\" data-slide-to=" + i + " style=\"background-color:" + crs.getString("colours_colour") + "\"></li>\n");
						}
					}
					i++;
				}

				Str.append("</ol>");
				Str.append("<center><div class=\"carousel-inner\" role=\"listbox\">\n");
				crs.beforeFirst();
				while (crs.next()) {
					if (!crs.getString("colours_value").equals("")) {
						if (j == 0) {
							Str.append("<div class=\"item active\">");
						} else {
							Str.append("<div class=\"item\">");
						}
						Str.append("<img src=\"../DDMotorsThumbnail.do?modelcoloursimg=" + crs.getString("colours_value") + "&width=300\" class=\"img-responsive\">\n");
					}
					Str.append("</div>\n");
					j++;
				}
				Str.append("</div></center></div><br>");
			} else {
				msg = "No Variants Found!";
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

	public String ListVariantDetails(HttpServletResponse response) {
		int j = 0;
		int i = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name, COALESCE(price_amt-price_disc) As exshowroomprice"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " WHERE 1=1 AND item_img !=''"
					+ " AND item_model_id =" + model_id;
			// SOP("StrSql---" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {

				Str.append("<div id=\"myCarousel1\" class=\"carousel slide\" data-ride=\"carousel\">\n");
				Str.append("<center><div class=\"carousel-inner\" role=\"listbox\">\n");
				while (crs.next()) {
					if (j == 0) {
						Str.append("<div class=\"item active\">");
					} else {
						Str.append("<div class=\"item\">");
					}
					Str.append("<p>").append(crs.getString("item_name")).append("</p>\n");
					Str.append("<p>11.96 kmpl &nbsp;&nbsp;&nbsp; * 1298 cc Petrol <br><br>");
					Str.append("&#8377;&nbsp;&nbsp;");
					Str.append(IndFormat(crs.getString("exshowroomprice")));
					Str.append("</p></div>\n");
					j++;
				}
				Str.append("</div></center></div>");
			} else {
				msg = "No Variants Found!";
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

	public String ListVariantFeature(HttpServletResponse response) {
		int j = 0;
		int i = 0;
		int count = 0;
		String feature_desc = "";
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT feature_id, feature_name, feature_desc"
					+ " FROM " + compdb(comp_id) + "axela_app_model_feature"
					+ " WHERE 1=1 AND feature_model_id =" + model_id;
			// SOP("StrSql---" + StrSqlBreaker(StrSql));
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"panel-group accordion scrollable\" id=\"accordion2\">");
				while (crs.next()) {
					feature_desc = unescapehtml(crs.getString("feature_desc")).replace("<br>", "").replace("<br />", "").replace("&nbsp;", "").replace("<>", "").trim();
					Str.append("<div class=\"panel panel-default\">");
					Str.append("<div class=\"panel-heading\" style=\"color: #fff; background-color: #000\">\n")
							.append("<h4 class=\"panel-title\">\n")
							.append("<a class=\"accordion-toggle\" data-toggle=\"collapse\"	data-parent=\"#accordion2\" href=\"#collapse_" + crs.getString("feature_id") + "\">\n")
							.append(crs.getString("feature_name"))
							.append("</a></h4></div>\n");
					if (count == 0) {
						Str.append("<div id=\"collapse_" + crs.getString("feature_id") + "\" class=\"panel-collapse in\">\n");
					} else {
						Str.append("<div id=\"collapse_" + crs.getString("feature_id") + "\" class=\"panel-collapse collapse\">\n");
					}

					Str.append("<div class=\"panel-body\" style=\"color: red\">")
							.append("<p class=\"text\">")
							.append(feature_desc)
							.append("</p></div></div></div>");
					count++;

				}
				Str.append("</div>");
				crs.close();
			} else {
				msg = "No Variants Found!";
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
