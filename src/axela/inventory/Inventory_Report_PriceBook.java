package axela.inventory;
//Dilip kumar 11 Jul 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Report_PriceBook extends Connect {

	public String StrHTML = "";
	public String StrSearch = "", StrSql = "";
	public String rateclass_id = "0";
	public String cat_id = "0";
	public String comp_id = "0";
	public String item_inventory_type_id = "0", item_fueltype_id = "0";
	public String date = "";
	public String msg = "";
	public String model_id = "0";
	public String brand_id = "0";
	public String go = "";
	DecimalFormat df = new DecimalFormat("0.00");
	StringBuilder strpricebook = new StringBuilder();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		CheckSession(request, response);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_mis_access,emp_report_access,emp_item_access", request, response);
		if (!comp_id.equals("0")) {
			date = strToShortDate(ToLongDate(kknow()));
			rateclass_id = CNumeric(PadQuotes(request.getParameter("dr_rateclass_id	")));
			go = PadQuotes(request.getParameter("submit_button"));
			try {
				if (go.equals("Go")) {
					GetValues(request, response);
					CheckForm();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = ItemPriceDetails();
						if (!strpricebook.toString().equals("")) {
							SetSession("pricebookstrsql", " AND (" + strpricebook.toString() + ")", request);
							SetSession("rateclass_id", rateclass_id, request);
						}
					}
					// SOP("sostrsql==000===" + GetSession("sostrsql", request));
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		brand_id = CNumeric(PadQuotes(request.getParameter("drop_model_brand_id")));
		model_id = PadQuotes(request.getParameter("dr_model_id"));
		cat_id = CNumeric(PadQuotes(request.getParameter("dr_cat_id")));
		item_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_fueltype_id")));
		item_inventory_type_id = CNumeric(PadQuotes(request.getParameter("dr_item_type_id")));
		if (!brand_id.equals("") && !brand_id.equals("0")) {
			StrSearch = StrSearch + " AND model_brand_id = " + brand_id + "";
		}
		if (!model_id.equals("") && !model_id.equals("0")) {
			StrSearch = StrSearch + " AND item_model_id = " + model_id + "";
		}
		if (!cat_id.equals("0") && !cat_id.equals("")) {
			StrSearch = StrSearch + " AND item_cat_id = " + cat_id + "";
		}
		if (!item_fueltype_id.equals("0")) {
			StrSearch = StrSearch + " AND item_fueltype_id = " + item_fueltype_id + "";
		}
		if (!item_inventory_type_id.equals("0") && !item_inventory_type_id.equals("")) {
			StrSearch = StrSearch + " AND item_type_id = " + item_inventory_type_id + "";
		}
	}

	protected void CheckForm() {

		if (rateclass_id.equals("0")) {
			msg = msg + "<br>Select Rate Class!";
		}
		if (brand_id.equals("0")) {
			msg += "<br>Select Brand!";
		}
	}

	public String ItemPriceDetails() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_rateclass_id	 = rateclass_id"
					+ " WHERE rateclass_id = " + rateclass_id + ""
					+ " GROUP BY rateclass_id"
					+ " ORDER BY rateclass_name";
			CachedRowSet crs1 = processQuery(StrSql, 0);

			if (crs1.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\">");
				while (crs1.next()) {
					Str.append("<thead><tr><td align=center><b>").append(crs1.getString("rateclass_name")).append("</b></td></thead></tr>");
					// Str.append("<tr><td align=center>");
					Str.append("</table>\n");
					// Index for Price fields: 0-price.price_amt, 1- price_disc, 2-(price_amt - price_disc),
					// 3-price_effective_from, 4-price_rateclass_id

					StrSql = "SELECT item_id,"
							+ "	COALESCE (( SELECT CONCAT( price.price_amt, ',', price.price_disc, ',',"
							+ "	price.price_amt - price.price_disc, ',', price_effective_from, ',', price_rateclass_id )"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price price"
							+ " WHERE price.price_item_id = item_id"
							+ " AND price.price_rateclass_id = " + rateclass_id + ""
							+ " AND price.price_effective_from <= '" + ToLongDate(kknow()) + "'"
							+ " AND price.price_active = '1'"
							+ " AND price.price_amt != 0"
							+ " ORDER BY price.price_effective_from DESC"
							+ " LIMIT 1 ), '0,0,0,0,0' ) AS price,"
							+ " IF(price_variable = 1, 'Yes', 'No') AS price_variable, fueltype_id,"
							+ " COALESCE(fueltype_name, '') AS fueltype_name, model_brand_id,"
							+ " item_name, item_code, item_service_code,  item_hsn,"
							+ " COALESCE(sgst.customer_rate, 0) AS sgst,"
							+ " COALESCE(cgst.customer_rate, 0) AS cgst,"
							+ " COALESCE(igst.customer_rate, 0) AS igst,"
							+ " COALESCE(cess.customer_rate, 0) AS cess"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = item_fueltype_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop ON cat_id = item_cat_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = price_rateclass_id	"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer sgst ON sgst.customer_id = item_salestax1_ledger_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cgst ON cgst.customer_id = item_salestax2_ledger_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer igst ON igst.customer_id = item_salestax3_ledger_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cess ON cess.customer_id = item_salestax4_ledger_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
							+ " LEFT JOIN axela_brand ON brand_id = model_brand_id"
							+ " WHERE item_active = '1'"
							+ "	AND model_active = '1'"
							+ " AND price_rateclass_id = " + rateclass_id + ""
							+ " AND price_effective_from <= " + ToLongDate(kknow()) + ""
							+ " AND price_amt != 0"
							+ " AND price_active = '1'"
							+ StrSearch + ""
							+ " GROUP BY item_id"
							+ " ORDER BY item_name";

					// SOPInfo("StrSql==ItemPriceDetails==" + StrSqlBreaker(StrSql));
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						int count = 0;
						Str.append("<div class=\"table-responsive table-bordered\">\n");
						Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th data-hide=\"phone\">#</th>\n");
						Str.append("<th data-toggle=\"true\"><b>ID</b></th>\n");
						Str.append("<th align=center><b>Item Name</b></th>\n");
						Str.append("<th data-hide=\"phone\"><b>Item Code</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Service Code</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>HSN</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Fuel Type</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Effective</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>SGST</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>CGST</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>IGST</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Cess</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Price</b></th>\n");
						// Str.append("<th data-hide=\"phone, tablet\"><b>Discount</b></th>\n");
						Str.append("<th data-hide=\"phone, tablet\"><b>Net Price</b></th>\n");
						Str.append("</tr>");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");
						while (crs.next()) {
							if (count == 0) {
								strpricebook.append(" item_id = ").append(crs.getString("item_id"));
							} else {
								strpricebook.append(" OR item_id = ").append(crs.getString("item_id"));
							}
							count++;
							Str.append("<tr>\n");
							Str.append("<td align=center valign=top>").append(count).append("</td>");
							Str.append("<td align=center valign=top>").append(crs.getString("item_id")).append("</td>");
							Str.append("<td align=left valign=top><a href=../inventory/item-price-list.jsp?item_id=");
							Str.append(crs.getString("item_id")).append("&rateclass_id=").append(crs.getString("price").split(",")[4])
									.append(">").append(crs.getString("item_name")).append("</a></td>");
							Str.append("<td align=left valign=top>").append(crs.getString("item_code")).append("</td>");
							Str.append("<td align=left valign=top>").append(crs.getString("item_service_code")).append("</td>");
							Str.append("<td align=center valign=top>").append(crs.getString("item_hsn")).append("</td>");
							Str.append("<td align=left valign=top>").append(crs.getString("fueltype_name")).append("</td>");
							Str.append("<td align=center valign=top> ").append(strToShortDate(crs.getString("price").split(",")[3])).append("</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("sgst")).append("%</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("cgst")).append("%</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("igst")).append("%</td>");
							Str.append("<td align=right valign=top>").append(crs.getString("cess")).append("%</td>");
							Str.append("<td align=right valign=top>").append(IndDecimalFormat(df.format(Double.parseDouble(crs.getString("price").split(",")[0])))).append("</td>");
							// Str.append("<td align=right valign=top>").append(IndDecimalFormat(df.format(crs.getDouble("price_disc")))).append("</td>");
							Str.append("<td align=right valign=top>")
									.append(IndDecimalFormat(df.format(Double.parseDouble(PopulateTotal(crs.getString("price").split(",")[2],
											CalGSTTax(crs.getString("price").split(",")[2], crs.getString("sgst"), crs.getString("cgst"), crs.getString("cess")))))))
									.append("</td>");
							Str.append("</tr>");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
					} else {
						Str.append("<center><font color=red><b>No Items found!</b></font></center></td></tr>");
					}
					crs.close();
					// Str.append("</table>\n");
				}
			} else {
				Str.append("<center><font color=red><b>No Items found!</b></font></center>");
			}
			crs1.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String comp_id, String brand_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT model_id, CONCAT(brand_name,' - ',model_name) as model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " WHERE model_brand_id = " + brand_id
					+ " AND model_active = 1"
					+ " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql---------1-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_model_id class=form-control id=dr_model_id>");
			Str.append("<option value=0>Select</option>");
			if (crs.isBeforeFirst())
			{
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("model_id")).append("");
					Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
					Str.append(">").append(crs.getString("model_name")).append("</option> \n");
				}
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " GROUP BY fueltype_name"
					+ " ORDER BY fueltype_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id"));
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), item_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItemType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT type_id, type_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_type"
					+ " ORDER BY type_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("type_id"));
				Str.append(StrSelectdrop(crs.getString("type_id"), item_inventory_type_id));
				Str.append(">").append(crs.getString("type_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranchClass(String comp_id) {
		try {
			StrSql = "SELECT rateclass_id, rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_rateclass_id	 = rateclass_id"
					+ " GROUP BY rateclass_id"
					+ " ORDER BY rateclass_name";

			// SOP("StrSql==="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			String stringval = "<option value = 0> Select </option>";
			while (crs.next()) {
				stringval = stringval + "<option value=" + crs.getString("rateclass_id") + "";
				stringval = stringval + StrSelectdrop(crs.getString("rateclass_id"), rateclass_id);
				stringval = stringval + ">" + crs.getString("rateclass_name") + "</option> \n";
			}
			crs.close();
			return stringval;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulatePrincipal(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
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
