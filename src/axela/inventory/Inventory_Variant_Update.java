package axela.inventory;
//aJIt

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Variant_Update extends Connect {

	public String add = "";
	public String update = "";
	public String addB = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String item_id = "0";
	public String item_name = "";
	public String item_code = "";
	public String item_service_code = "";
	public String item_small_desc = "";
	public String item_big_desc = "";
	public String item_fueltype_id = "0";
	public String item_active = "0";
	public String item_notes = "";
	public String item_entry_date = "";
	public String item_modified_date = "";
	public String item_entry_id = "0";
	public String entry_date = "";
	public String item_modified_id = "0";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String item_entry_by = "";
	public String item_modified_by = "";
	public String QueryString = "";
	public String item_model_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id, emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						item_active = "1";
					} else {
						if (ReturnPerm(comp_id, "emp_role_id, emp_item_add", request).equals("1")) {
							GetValues(request, response);
							item_entry_id = emp_id;
							item_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("inventory-variant-list.jsp?variant_id=" + item_id + "&msg=Variant added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !"Delete Variant".equals(deleteB)) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !"Delete Variant".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_role_id, emp_item_edit", request).equals("1")) {
							GetValues(request, response);
							item_modified_id = emp_id;
							item_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("inventory-variant-list.jsp?variant_id=" + item_id + "&msg=Variant updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Variant".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id, emp_item_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("inventory-variant-list.jsp?msg=Variant Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}

						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {

						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		item_name = PadQuotes(request.getParameter("txt_item_name"));
		item_code = PadQuotes(request.getParameter("txt_item_code"));
		item_service_code = PadQuotes(request.getParameter("txt_item_service_code"));
		item_model_id = CNumeric(PadQuotes(request.getParameter("drop_item_model_id")));
		item_small_desc = PadQuotes(request.getParameter("txt_item_small_desc"));
		item_big_desc = PadQuotes(request.getParameter("txt_item_big_desc"));
		item_notes = PadQuotes(request.getParameter("txt_item_notes"));
		item_active = CheckBoxValue(PadQuotes(request.getParameter("chk_item_active")));
		item_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_item_fueltype_id")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		item_entry_by = unescapehtml(PadQuotes(request.getParameter("item_entry_by")));
		item_modified_by = PadQuotes(request.getParameter("item_modified_by"));
	}

	protected void CheckForm() {
		msg = "";
		if (item_name.equals("")) {
			msg += "<br>Enter Variant Name!";
		}
		else {
			StrSql = "SELECT item_name FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_name = '" + item_name + "'"
					+ " AND item_id != " + item_id + "";
			// SOP("StrSql---------" + StrSqlBreaker(StrSql));
			if (ExecuteQuery(StrSql).equals(item_name)) {
				msg += "<br>Similar Variant found!";
			}
		}

		if (item_model_id.equals("0")) {
			msg += "<br>Select Model!";
		}

		if (item_fueltype_id.equals("0")) {
			msg += "<br>Select Fuel Type!";
		}

		if (item_small_desc.length() > 255) {
			item_small_desc = item_small_desc.substring(0, 254);
		}

		if (item_big_desc.length() > 2000) {
			item_big_desc = item_big_desc.substring(0, 1999);
		}

		if (item_notes.length() > 8000) {
			item_notes = item_notes.substring(0, 7999);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			item_id = ExecuteQuery("SELECT COALESCE(MAX(item_id), 0) + 1 FROM " + compdb(comp_id) + "axela_inventory_item");
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item"
					+ " (item_id,"
					+ " item_name,"
					+ " item_code,"
					+ " item_service_code,"
					+ " item_cat_id,"
					+ " item_model_id,"
					+ " item_type_id,"
					+ " item_fueltype_id,"
					+ " item_small_desc,"
					+ " item_big_desc,"
					+ " item_uom_id,"
					+ " item_active,"
					+ " item_notes,"
					+ " item_entry_id,"
					+ " item_entry_date)"
					+ " VALUES"
					+ " (" + item_id + ","
					+ " '" + item_name + "',"
					+ " '" + item_code + "',"
					+ " '" + item_service_code + "',"
					+ " '1',"// item_cat_id
					+ " " + item_model_id + ","
					+ " '1',"// item_type_id
					+ " " + item_fueltype_id + ","
					+ " '" + item_small_desc + "',"
					+ " '" + item_big_desc + "',"
					+ " '1',"// item_uom_id
					+ " '" + item_active + "',"
					+ " '" + item_notes + "',"
					+ " '" + item_entry_id + "',"
					+ " '" + item_entry_date + "')";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT item_name, item_code, item_service_code, item_fueltype_id, item_model_id,"
					+ " item_small_desc, item_big_desc, item_active, item_notes,"
					+ " item_entry_id, item_entry_date, item_modified_id, item_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + item_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_name = crs.getString("item_name");
					item_code = crs.getString("item_code");
					item_service_code = crs.getString("item_service_code");
					item_fueltype_id = crs.getString("item_fueltype_id");
					item_model_id = crs.getString("item_model_id");
					item_small_desc = crs.getString("item_small_desc");
					item_big_desc = crs.getString("item_big_desc");
					item_active = crs.getString("item_active");
					item_notes = crs.getString("item_notes");
					item_entry_id = crs.getString("item_entry_id");
					if (!item_entry_id.equals("0")) {
						item_entry_by = Exename(comp_id, Integer.parseInt(item_entry_id));
					}
					entry_date = strToLongDate(crs.getString("item_entry_date"));
					item_modified_id = crs.getString("item_modified_id");
					if (!item_modified_id.equals("0")) {
						item_modified_by = Exename(comp_id, Integer.parseInt(item_modified_id));
						modified_date = strToLongDate(crs.getString("item_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item"
					+ " SET"
					+ " item_name = '" + item_name + "',"
					+ " item_code = '" + item_code + "',"
					+ " item_service_code = '" + item_service_code + "',"
					+ " item_model_id = " + item_model_id + ","
					+ " item_fueltype_id = " + item_fueltype_id + ","
					+ " item_small_desc = '" + item_small_desc + "',"
					+ " item_big_desc = '" + item_big_desc + "',"
					+ " item_active = '" + item_active + "',"
					+ " item_notes = '" + item_notes + "',"
					+ " item_modified_id = '" + item_modified_id + "',"
					+ " item_modified_date = '" + item_modified_date + "'"
					+ " WHERE item_id = " + item_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, CONCAT(brand_name,' - ',model_name) as model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " WHERE 1 = 1"
					+ " AND model_active = 1"
					+ " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			// SOP("StrSql-----PopulateModel----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), item_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	protected void DeleteFields() {

		if (item_id.equals("1")) {
			msg = msg + "<br>Cannot Delete First Record!";
		}
		// Enquiry Association
		StrSql = "SELECT enquiry_item_id FROM " + compdb(comp_id)
				+ "axela_sales_enquiry" + " WHERE enquiry_item_id = " + item_id
				+ "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Enquiry!";
		}

		// sales Association
		StrSql = "SELECT quoteitem_item_id FROM " + compdb(comp_id)
				+ "axela_sales_quote_item" + " WHERE quoteitem_item_id = "
				+ item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Quote!";
		}

		StrSql = "SELECT soitem_item_id FROM " + compdb(comp_id)
				+ "axela_sales_so_item" + " WHERE soitem_item_id = " + item_id
				+ "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Sales Order!";
		}

		// Voucher association
		StrSql = "select vouchertrans_voucher_id from " + compdb(comp_id)
				+ "axela_acc_voucher_trans where vouchertrans_item_id = " + item_id
				+ " LIMIT 1";

		if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
			msg += "<br>Item is associated with Voucher!";
		}

		// Stock Option association
		StrSql = "SELECT option_item_id FROM " + compdb(comp_id)
				+ "axela_inventory_item_option" + " WHERE option_item_id = "
				+ item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Item Option!";
		}

		// association with Test Drive
		StrSql = "SELECT veh_item_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
				+ " WHERE veh_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is associated with Vehicle!";
		}

		StrSql = "SELECT COALESCE(option_item_id, '')"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_option"
				+ " WHERE option_item_id = " + item_id + "";
		if (ExecuteQuery(StrSql).equals(item_id)) {
			msg += "<br>Item is configured for an Item!";
		}

		// Delete records
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_item_price"
					+ " WHERE price_item_id = " + item_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_stock"
					+ " WHERE stock_item_id = " + item_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_id = " + item_id + "";
			updateQuery(StrSql);
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_id = " + item_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String PopulateFuelType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " GROUP BY fueltype_id"
					+ " ORDER BY fueltype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
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
}
