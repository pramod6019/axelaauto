package axela.ddmotors_app;
//Divya

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Model_List extends Connect {

	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String pagecurrent = "";
	public String user_id = "";
	public String comp_id = "0";
	public boolean flag = false;
	public int TotalRecords = 0;
	public String msg = "";
	public String model_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			comp_id = ddmotors_app_comp_id();
			if (!comp_id.equals("0")) {
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				msg = PadQuotes(request.getParameter("msg"));
				StrHTML = ListData();
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String ListData() {
		int TotalRecords = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name, model_desc, CAST(model_img_value AS CHAR) AS image, "
					+ " GROUP_CONCAT(DISTINCT fueltype_name SEPARATOR ', ') as fueltype_name,"
					+ " COALESCE (FORMAT(MIN(price_amt - price_disc)/100000,2),0) AS pricefrom,"
					+ " COALESCE (FORMAT(MAX(price_amt - price_disc)/100000,2),0) AS priceto";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_app_model_colours on colours_model_id = model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_model_id = model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype on fueltype_id = item_fueltype_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = price_rateclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_type on type_id = item_type_id"
					+ " WHERE 1=1 " + " AND model_active = '1' AND model_sales = '1'"
					+ " AND item_active = 1 AND item_type_id = 1"
					+ " AND price_effective_from <= '" + ToLongDate(kknow()) + "' AND price_active = '1'"
					+ " AND (price_amt > 100000) ";
			CountSql = " SELECT COUNT(DISTINCT(model_id))";

			StrSql += SqlJoin;
			CountSql += SqlJoin;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StrSql = StrSql + " GROUP BY model_id" + " ORDER BY model_name";
				// SOP("STrSql=========" + StrSqlBreaker(StrSql));
				CachedRowSet crs =processQuery(StrSql, 0);
				int count = 0;

				String Img = "";
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						if (!crs.getString("image").equals("")) {
							Str.append(
									"<div class=\"row\" onclick=\"callURL('model-variant-details.jsp?model_id="
											+ crs.getString("model_id")
											+ "')\">\n")
									.append("<div class=\"col-md-6 col-xs-6\">\n")
									.append("<img src=\"../DDMotorsThumbnail.do?modelimg=" + crs.getString("image") + "&width=200\" class=\"img-responsive\"></div>\n");
							Str.append(
									"<div class=\"col-md-6 col-xs-6\"><h4><b>\n")
									.append(crs.getString("model_name"))
									.append("</b></h4><br>")

									.append("<b>Fuel Type:&nbsp;&nbsp;&nbsp;")
									.append(crs.getString("fueltype_name")).append("</b><br>")
									.append("<b><font color=#ff0000> Price:&nbsp;&nbsp;&nbsp;")
									.append(IndFormat(crs.getString("pricefrom")))
									.append("&nbsp;Lakh - ")
									.append(IndFormat(crs.getString("priceto")))
									.append("&nbsp;Lakh")
									.append("</font></b></div></div>");
							// Str.append(
							// "<div class=\"col-md-6 col-xs-6\" style=\"color: #000000;\">\n")
							// .append("Fuel Type:&nbsp;&nbsp;&nbsp;")
							// .append(crs.getString("fueltype_name"))
							// .append("</div>");
							// Str.append(
							// "<div class=\"col-md-6 col-xs-6\" style=\"color: #ff0000;\">\n")
							// .append("<b style=\"color: #ff0000;\">Price:</b>")
							// .append(IndFormat(crs.getString("pricefrom")))
							// .append(" - ")
							// .append(IndFormat(crs.getString("priceto")))
							// .append("</div>").append("</div><br>\n");
						}
					}
				} else {
					msg = "No Models Found!";
				}
				crs.close();
			} else {
				msg = "No Models Found!";
			}
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			return "";
		}

		return Str.toString();
	}
}
