package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Check extends Connect {

	public String cat_id = "";
	public String comp_id = "0";
	public String adj_cat_id = "";
	public String BranchAccess = "";
	public String po_supplier_id = "";
	public String rateclass_id = "";
	public String id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String location_id = "";
	public String uom_id = "";
	public String branch_location = "";
	public String vehstock_branch_id = "0";
	public String stock_branch_id = "0";
	public String brand_id = "0";
	public String branch_id = "0";
	public String model = "";
	public String model_id = "0";
	public String location = "", stockbranchitem = "", stockbranchmodel = "", stockbranchpaintwork = "", displayCommission = "", vehstock_model_id = "0";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			rateclass_id = CNumeric(PadQuotes(request.getParameter("rateclass_id")));
			location_id = CNumeric(PadQuotes(request.getParameter("dr_vehstock_location_id")));
			id = CNumeric(PadQuotes(request.getParameter("id")));
			cat_id = PadQuotes(request.getParameter("cat_id"));
			po_supplier_id = CNumeric(PadQuotes(request.getParameter("po_supplier_id")));
			adj_cat_id = PadQuotes(request.getParameter("adj_cat_id"));
			uom_id = CNumeric(PadQuotes(request.getParameter("uom_id")));
			cat_id = cat_id.replaceAll("nbsp", "&");
			stock_branch_id = CNumeric(PadQuotes(request.getParameter("stock_branch_id")));
			vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("vehstock_branch_id")));
			stockbranchitem = PadQuotes(request.getParameter("stockbranchitem"));
			stockbranchmodel = PadQuotes(request.getParameter("stockbranchmodel"));
			stockbranchpaintwork = PadQuotes(request.getParameter("stockbranchpaintwork"));
			displayCommission = PadQuotes(request.getParameter("displayCommission"));
			location = PadQuotes(request.getParameter("location"));
			vehstock_model_id = CNumeric(PadQuotes(request.getParameter("vehstock_model_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
			branch_location = PadQuotes(request.getParameter("branch_location"));

			model = PadQuotes(request.getParameter("model"));
			if (!cat_id.equals("") && !id.equals("0")) {
				// StrHTML = PopulateItem();
			}

			if (!vehstock_branch_id.equals("0")) {
				StrHTML = PopulateLocation();
			}
			if (!vehstock_branch_id.equals("0") && stockbranchmodel.equals("yes")) {
				StrHTML = new Stock_Update().PopulateModel(comp_id, vehstock_branch_id);
			}
			if (!vehstock_branch_id.equals("0") && stockbranchitem.equals("yes")) {
				StrHTML = new Stock_Update().PopulateItem(comp_id, vehstock_model_id, vehstock_branch_id);
			}
			if (!vehstock_branch_id.equals("0") && stockbranchpaintwork.equals("yes")) {
				StrHTML = new Stock_Update().PopulatePaintWork(comp_id, vehstock_branch_id);
			}

			if (!vehstock_branch_id.equals("0") && displayCommission.equals("yes")) {
				StrSql = "SELECT branch_brand_id FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_id = " + vehstock_branch_id + "";

				StrHTML = CNumeric(ExecuteQuery(StrSql));
				// SOP("StrHTML==" + StrHTML);
			}

			if (!location_id.equals("0")) {
				StringBuilder Str = new StringBuilder();
				try {
					StrSql = "SELECT location_name, location_id, location_address, location_phone1,"
							+ " location_mobile1, city_id, city_name, state_name, location_pin"
							+ " FROM " + compdb(comp_id) + "axela_inventory_location"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_id = location_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
							+ " WHERE location_id = " + location_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						Str.append("<input type=hidden name=\"order_value\" id=\"order_value\"");
						Str.append("value=\"").append(crs.getString("location_address"));
						Str.append("[&%]").append(crs.getString("city_name"));
						Str.append("[&%]").append(crs.getString("location_pin"));
						Str.append("[&%]").append(crs.getString("state_name"));
						Str.append("[&%]").append(crs.getString("location_phone1"));
						Str.append("[&%]").append(crs.getString("location_mobile1"));
						Str.append("\">");
					}
					crs.close();
					StrHTML = Str.toString();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}

			if (!uom_id.equals("0")) {
				StringBuilder Str = new StringBuilder();
				try {
					StrSql = "SELECT uom_ratio"
							+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
							+ " WHERE uom_id = " + uom_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						Str.append(crs.getString("uom_ratio"));
					}
					crs.close();
					StrHTML = Str.toString();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}

			if (!po_supplier_id.equals("0") && id.equals("0")) {
				StringBuilder Str = new StringBuilder();
				try {
					StrSql = "SELECT customer_address, customer_phone1, customer_mobile1,"
							+ " customer_city_id, city_name, state_name, customer_pin"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " INNER JOIN " + compdb(comp_id) + "axela_city on city_id = customer_city_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
							+ " WHERE customer_id = " + po_supplier_id + "";
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						Str.append("<input type=hidden name=\"supplier\" id=\"supplier\"");
						Str.append(" value=\"").append(crs.getString("customer_address"));
						Str.append("[&%]").append(crs.getString("city_name"));
						Str.append("[&%]").append(crs.getString("customer_pin"));
						Str.append("[&%]").append(crs.getString("state_name"));
						Str.append("[&%]").append(crs.getString("customer_phone1"));
						Str.append("[&%]").append(crs.getString("customer_mobile1"));
						Str.append("\">");
					}
					crs.close();
					StrHTML = Str.toString();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}

			if (!adj_cat_id.equals("")) {
				StringBuilder str = new StringBuilder();
				try {
					StrSql = "SELECT item_name, item_code, item_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_cat_id = " + adj_cat_id + ""
							+ " AND item_active = '1'"
							+ " AND item_nonstock = '0'"
							+ " ORDER BY item_name";
					str.append("<select name= dr_item_id id= dr_item_id class=selectbox ");
					str.append(" ><option value = 0>Select</option>");
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						str.append("<option value=").append(crs.getString("item_id")).append("");
						str.append(">").append(crs.getString("item_name")).append("");
						if (!crs.getString("item_code").equals("")) {
							str.append(" (").append(crs.getString("item_code")).append(")");
						}
						str.append("</option>\n");
					}
					str.append("</select>");
					crs.close();
					StrHTML = (str.toString());
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
			if (branch_location.equals("yes") && !vehstock_branch_id.equals("0")) {
				PopulateStockLocation(comp_id, vehstock_branch_id, brand_id);
			}

			if (model.equals("yes")) {
				branch_id = PadQuotes(request.getParameter("dr_branch_id"));
				if (branch_id.contains(",")) {
					branch_id = CleanArrVal(branch_id);
				}
				StrHTML = PopulateModel(branch_id, model_id, comp_id);
			}

		}

		// if (location.equals("yes")) {
		// StrHTML = PopulateLocation1();
		// }
		if (location.equals("yes") && !stock_branch_id.equals("0")) {
			StrHTML = PopulateLocation1();
		}
	}

	// public String PopulateItem() { // /to populate the items according to the category
	// StringBuilder Str1 = new StringBuilder();
	// StringBuilder Str2 = new StringBuilder();
	// try {
	// StrSql = "(SELECT concat(item_id,'-0') as itemdata, item_id, item_name, uom_name as poitem_uom, uom_name as our_uom,"
	// + " 1 as convfactor, item_code as item_code"
	// + " from " + compdb(comp_id) + "axela_inventory_item"
	// + " inner join " + compdb(comp_id) + "axela_inventory_uom on uom_id = item_uom_id"
	// + " where item_nonstock ='0' and item_cat_id = " + cat_id + ""
	// + " order by item_name)"
	// + " UNION "
	// + " (SELECT concat(item_id,'-',podata_id) as itemdata, item_id, podata_supplier_desc as item_name,"
	// + " podata_supplier_uom as poitem_uom, uom_name as our_uom,"
	// + " podata_convfactor as convfactor, podata_supplier_itemcode as item_code"
	// + " from " + compdb(comp_id) + "axela_inventory_item"
	// + " inner join " + compdb(comp_id) + "axela_inventory_uom on uom_id = item_uom_id"
	// + " inner join " + compdb(comp_id) + "axela_inventory_po_data on podata_item_id = item_id"
	// + " where item_nonstock = '0'"
	// + " and item_cat_id = " + cat_id + ""
	// + " and podata_supplier_id = " + po_supplier_id + ""
	// + " order by item_name)";
	// SOP("items===========" + StrSqlBreaker(StrSql));
	// CachedRowSet crs = processQuery(StrSql, 0);
	// Str2.append("<select name=dr_item").append(id).append(" id=dr_item").append(id).append(" class=selectbox onchange=\"PurchaseOrderCheck(this.value,'").append(id).append("','','');\">"
	// + "<option value = -1>Select</option>\n");
	// while (crs.next()) {
	// Str1.append("<input name=\"poitm_desc_").append(crs.getString("itemdata")).append("\" id=\"poitem_desc_").append(crs.getString("itemdata")).append("\" type=\"hidden\" value=\"")
	// .append(crs.getString("item_name")).append("\">\n");
	// Str1.append("<input name=\"poitem_itemcode_").append(crs.getString("itemdata")).append("\" id=\"poitem_itemcode_").append(crs.getString("itemdata"))
	// .append("\" type=\"hidden\" value=\"").append(crs.getString("item_code")).append("\">\n");
	// Str1.append("<input name=\"poitem_uom_").append(crs.getString("itemdata")).append("\" id=\"poitem_uom_").append(crs.getString("itemdata")).append("\" type=\"hidden\" value=\"")
	// .append(crs.getString("poitem_uom")).append("\">\n");
	// Str1.append("<input name=\"our_uom_").append(crs.getString("itemdata")).append("\" id=\"our_uom_").append(crs.getString("itemdata")).append("\" type=\"hidden\" value=\"")
	// .append(crs.getString("our_uom")).append("\">\n");
	// Str1.append("<input name=\"convfactor_").append(crs.getString("itemdata")).append("\" id=\"convfactor_").append(crs.getString("itemdata")).append("\" type=\"hidden\" value=\"")
	// .append(crs.getString("convfactor")).append("\">\n");
	// Str2.append("<option value=").append(crs.getString("itemdata")).append(">").append(crs.getString("item_name")).append("");
	// if (!crs.getString("item_code").equals("")) {
	// Str2.append(" (").append(crs.getString("item_code")).append(")");
	// }
	// Str2.append("</option>\n");
	// }
	// Str2.append("</select>");
	// Str1.append(Str2.toString());
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str1.toString();
	// }

	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("PopulateLocation==");
			StrSql = "SELECT vehstocklocation_id, vehstocklocation_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " where vehstocklocation_branch_id = " + vehstock_branch_id + ""
					+ " ORDER BY vehstocklocation_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("inv check==" + StrSql);
			Str.append("<select name=\"dr_vehstock_location_id\" id=\"dr_vehstock_location_id\" class=\"form-control selectbox\">");
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstocklocation_id")).append("");
				Str.append(">").append(crs.getString("vehstocklocation_name")).append("</option> \n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		// SOP("PopulateLocation==" + Str.toString());
		return Str.toString();
	}

	public String PopulateLocation1() {
		StringBuilder Str = new StringBuilder();
		if (!stock_branch_id.equals("")) {
			// SOP("PopulateLocation1==");
			try {
				StrSql = "SELECT location_id, location_name, location_code"
						+ " FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_branch_id = " + stock_branch_id + ""
						+ " GROUP BY location_id"
						+ " ORDER BY location_name";
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<select name=\"dr_location_id\" id=\"dr_location_id\" class=\"form-control\" style=\"margin-top: 9px;\">");
				Str.append("<option value=0>Select</option>");
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("location_id"));
					Str.append(StrSelectdrop(crs.getString("location_id"), location_id));
					Str.append(">").append(crs.getString("location_name")).append(" (");
					Str.append(crs.getString("location_code")).append(")");
					Str.append("</option> \n");
				}
				Str.append("</select>");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}

	public String PopulateStockLocation(String comp_id, String branch_id, String brand_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehstocklocation_id, vehstocklocation_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstocklocation_branch_id"
					+ " WHERE branch_active = 1"
					+ "	AND branch_id = " + branch_id
					+ "	AND branch_brand_id = " + brand_id;
			StrSql += " ORDER BY vehstocklocation_id";
			// SOP("StrSql==PopulateStockLocation==Check==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_location_id\" id=\"dr_location_id\" class=\"form-control\" style=\"margin-top: 9px;\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstocklocation_id")).append("");
				Str.append(StrSelectdrop(crs.getString("vehstocklocation_id"), location_id));
				Str.append(">").append(crs.getString("vehstocklocation_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateModel(String branch_id, String model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1 = 1 "
					+ BranchAccess
					+ " AND branch_id = " + branch_id
					+ " AND model_sales = 1"
					+ " AND model_active = 1"
					+ " GROUP BY model_id"
					+ " ORDER BY model_brand_id, model_name";
			// SOP("ModelPopulate---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_orderplaced_model_id\" name=\"dr_orderplaced_model_id\" class=\"form-control\">");
			Str.append("<option value =0>Select Model</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append(" ");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
