package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Item_Model_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String model_id = "0";
	public String model_name = "";
	public String model_service_code = "";
	public String model_service_margin = "0";
	public String model_desc = "";
	public String model_active = "";
	public String model_sales = "";
	public String QueryString = "";
	public String model_vehtype_id = "0";
	public String model_brand_id = "0";
	public String model_entry_id = "0";
	public String model_entry_date = "";
	public String model_modified_id = "0";
	public String model_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id, emp_service_insurance_add, emp_service_jobcard_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						model_name = "";
						model_service_code = "";
						model_desc = "";
						model_active = "1";
					} else {
						if (ReturnPerm(comp_id, "emp_role_id, emp_service_insurance_add, emp_service_jobcard_add", request).equals("1")) {
							GetValues(request, response);
							model_entry_id = emp_id;
							model_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../inventory/item-model.jsp?model_id=" + model_id + "&msg=Model Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Model".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Model".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_role_id, emp_service_insurance_add, emp_service_jobcard_add", request).equals("1")) {
							GetValues(request, response);
							model_modified_id = emp_id;
							model_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../inventory/item-model.jsp?model_id=" + model_id + "&msg=Model Updated Successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}

					} else if ("Delete Model".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("item-model.jsp?msg=Model Deleted Successfully!"));
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		model_name = PadQuotes(request.getParameter("txt_model_name"));
		model_service_code = PadQuotes(request.getParameter("txt_model_service_code"));
		model_service_margin = request.getParameter("txt_model_service_margin");
		model_desc = PadQuotes(request.getParameter("txt_model_desc"));
		model_vehtype_id = CNumeric(PadQuotes(request.getParameter("drop_model_vehtype_id")));
		model_brand_id = CNumeric(PadQuotes(request.getParameter("drop_model_brand_id")));
		model_sales = PadQuotes(request.getParameter("chk_model_sales"));
		model_active = PadQuotes(request.getParameter("chk_model_active"));
		if (model_sales.equals("on")) {
			model_sales = "1";
		} else {
			model_sales = "0";
		}
		if (model_active.equals("on")) {
			model_active = "1";
		} else {
			model_active = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}
	protected void CheckForm() {
		msg = "";
		if (model_name.equals("")) {
			msg = msg + "<br>Enter Model!";
		}

		try {
			if (!model_name.equals("")) {
				if (update.equals("yes") && !model_name.equals("")) {
					StrSql = "SELECT model_name FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_name = '" + model_name + "' AND model_id != " + model_id + "";
				}
				if (add.equals("yes") && !model_name.equals("")) {
					StrSql = "SELECT model_name FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_name = '" + model_name + "'";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Model Found!";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		if (model_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (model_desc.length() > 255) {
			model_desc = model_desc.substring(0, 254);
		}
		if (model_vehtype_id.equals("0")) {
			msg += "<br>Select Vehicle Type!";
		}
		if (model_brand_id.equals("0")) {
			msg += "<br>Select Principal!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				model_id = ExecuteQuery("SELECT max(model_id) as ID FROM " + compdb(comp_id) + "axela_inventory_item_model");
				if (model_id == null || model_id.equals("")) {
					model_id = "0";
				}
				int model_idi = Integer.parseInt(model_id) + 1;
				model_id = "" + model_idi;
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_model"
						+ " (model_id,"
						+ " model_name,"
						+ " model_service_code,"
						+ " model_service_margin,"
						+ " model_desc,"
						+ " model_vehtype_id,"
						+ " model_brand_id,"
						+ " model_sales,"
						+ " model_active,"
						+ " model_entry_id,"
						+ " model_entry_date)"
						+ " VALUES"
						+ " ('" + model_id + "',"
						+ " '" + model_name + "',"
						+ " '" + model_service_code + "',"
						+ " '" + model_service_margin + "',"
						+ " '" + model_desc + "',"
						+ " " + model_vehtype_id + ","
						+ " '" + model_brand_id + "',"
						+ " '" + model_sales + "',"
						+ " '" + model_active + "',"
						+ " " + model_entry_id + ","
						+ " '" + model_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_id = " + model_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					model_id = crs.getString("model_id");
					model_name = crs.getString("model_name");
					model_service_code = crs.getString("model_service_code");
					model_service_margin = crs.getString("model_service_margin");
					model_desc = crs.getString("model_desc");
					model_vehtype_id = crs.getString("model_vehtype_id");
					model_brand_id = crs.getString("model_brand_id");
					model_sales = crs.getString("model_sales");
					model_active = crs.getString("model_active");
					model_entry_id = crs.getString("model_entry_id");
					if (!model_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(model_entry_id));
						entry_date = strToLongDate(crs.getString("model_entry_date"));
					}
					model_modified_id = crs.getString("model_modified_id");
					if (!model_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(model_modified_id));
						modified_date = strToLongDate(crs.getString("model_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item Model!"));
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
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_model"
						+ " SET"
						+ " model_name = '" + model_name + "',"
						+ " model_service_code = '" + model_service_code + "',"
						+ " model_service_margin = '" + model_service_margin + "',"
						+ " model_desc = '" + model_desc + "',"
						+ " model_vehtype_id = " + model_vehtype_id + ","
						+ " model_brand_id = '" + model_brand_id + "',"
						+ " model_sales = '" + model_sales + "',"
						+ " model_active = '" + model_active + "',"
						+ " model_modified_id = " + model_modified_id + ","
						+ " model_modified_date = '" + model_modified_date + "'"
						+ " WHERE model_id = " + model_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_model_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_model_id = " + model_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is associated with a Enquiry!";
		}
		StrSql = "SELECT enquiry_add_model_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_add_model_id = " + model_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is associated with a Enquiry Additional Model!";
		}
		StrSql = "SELECT modeltarget_model_id  "
				+ " FROM " + compdb(comp_id) + "axela_sales_target_model  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_sales_target ON modeltarget_target_id = target_id "
				+ " WHERE modeltarget_model_id = " + model_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is associated with a Target(s)!";
		}

		StrSql = "SELECT item_model_id FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_model_id = " + model_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is associated with a Item(s)!";
		}
		if (model_id.equals("1")) {
			msg = msg + "<br>Cannot Delete First Record!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_id = " + model_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateVehicleType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehtype_id, vehtype_name"
					+ " FROM axela_veh_type"
					+ " GROUP BY vehtype_id"
					+ " ORDER BY vehtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehtype_id"));
				Str.append(StrSelectdrop(crs.getString("vehtype_id"), model_vehtype_id));
				Str.append(">").append(crs.getString("vehtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePrincipal() {
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
				Str.append(StrSelectdrop(crs.getString("brand_id"), model_brand_id));
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
