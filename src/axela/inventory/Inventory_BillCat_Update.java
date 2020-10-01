package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_BillCat_Update extends Connect {

	public String add = "", emp_id = "", branch_id = "", BranchAccess = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String comp_id = "0";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_role_id = "0";
	public String brand_id = "0", brand_name = "", type = "";
	public String billcat_id = "";
	public String billcat_name = "", billcat_code = "";
	public String cat_pid = "";
	public String billcat_entry_id = "", entry_by = "";
	public String billcat_entry_date = "", entry_date = "";
	public String billcat_modified_id = "", modified_by = "";
	public String billcat_modified_date = "", modified_date = "";
	// public String chkPermMsg = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				billcat_id = PadQuotes(request.getParameter("billcat_id"));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Category".equals(addB)) {
						billcat_name = "";
						billcat_code = "";
						billcat_entry_id = "";
						billcat_entry_date = "";
						billcat_modified_id = "";
						billcat_modified_date = "";
					} else {
						CheckPerm(comp_id, "emp_item_add, emp_sales_item_add, emp_pos_item_add", request, response);
						GetValues(request, response);
						billcat_entry_id = emp_id;
						billcat_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-billcat-list.jsp?billcat_id=" + CNumeric(billcat_id) + "&msg=Bill Category added successfully!"));
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"Update Category".equals(updateB) && !"Delete Category".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Category".equals(updateB) && !"Delete Category".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_edit, emp_sales_item_edit, emp_pos_item_edit", request, response);
						GetValues(request, response);
						billcat_modified_id = emp_id;
						billcat_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-billcat-list.jsp?billcat_id=" + CNumeric(billcat_id) + "&msg=Bill Category updated successfully!" + msg + ""));
						}
					} else if ("Delete Category".equals(deleteB)) {

						CheckPerm(comp_id, "emp_item_delete, emp_sales_item_delete, emp_pos_item_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-billcat-list.jsp?msg=Bill Category deleted successfully!"));
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
		billcat_name = PadQuotes(request.getParameter("txt_billcat_name"));
		brand_id = CNumeric(PadQuotes(request.getParameter("dr_billcat_brand_id")));
		type = PadQuotes(request.getParameter("dr_billcat_type_id"));
		billcat_code = PadQuotes(request.getParameter("txt_billcat_code"));
		entry_by = unescapehtml(PadQuotes(request.getParameter("entry_by")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}
	protected void CheckForm() {
		msg = "";
		if (brand_id.equals("0")) {
			msg = msg + "<br>Select Brand!";
		}
		if (type.equals("")) {
			msg = msg + "<br>Enter Type!";
		}
		if (billcat_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}

		try {
			StrSql = "SELECT billcat_name FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
					+ " WHERE billcat_name = '" + billcat_name + "'"
					+ "	AND billcat_brand_id = " + brand_id;
			if (update.equals("yes")) {
				StrSql = StrSql + " AND billcat_id != " + CNumeric(billcat_id) + "";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				msg = msg + "<br>Similar Name found!";
			}
			if (!billcat_code.equals("")) {
				StrSql = "SELECT billcat_code FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
						+ " WHERE billcat_code = '" + billcat_code + "'"
						+ "	AND billcat_brand_id = " + brand_id;
				if (update.equals("yes")) {
					StrSql = StrSql + " AND billcat_id != " + CNumeric(billcat_id) + "";
				}
				crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Code found!";
				}
			}
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_bill_cat"
						+ " ("
						+ " billcat_name,"
						+ " billcat_brand_id,"
						+ " billcat_billtype, "
						+ " billcat_code,"
						+ " billcat_entry_id,"
						+ " billcat_entry_date)"
						+ " VALUES"
						+ " ("
						+ " '" + billcat_name + "',"
						+ " " + brand_id + ","
						+ "'" + type + "',"
						+ "'" + billcat_code + "',"
						+ " '" + billcat_entry_id + "',"
						+ " '" + billcat_entry_date + "'" + ")";
				// SOP("StrSql==" + StrSql);
				billcat_id = UpdateQueryReturnID(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			StrSql = "SELECT billcat_name, brand_id,billcat_billtype, billcat_code,"
					+ " billcat_entry_id, billcat_entry_date, billcat_modified_id, billcat_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
					+ " INNER JOIN axela_brand ON brand_id = billcat_brand_id"
					+ " WHERE billcat_id = " + CNumeric(billcat_id);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					billcat_name = crs.getString("billcat_name");
					brand_id = crs.getString("brand_id");
					type = crs.getString("billcat_billtype");
					billcat_code = crs.getString("billcat_code");
					billcat_entry_id = crs.getString("billcat_entry_id");
					billcat_entry_date = crs.getString("billcat_entry_date");
					billcat_modified_id = crs.getString("billcat_modified_id");
					billcat_modified_date = crs.getString("billcat_modified_date");
					if (!billcat_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(billcat_entry_id));
						entry_date = strToLongDate(crs.getString("billcat_entry_date"));
					}
					if (!billcat_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(billcat_modified_id));
						modified_date = strToLongDate(crs.getString("billcat_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Bill Category!"));

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBrand(String brand_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ "	GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			stringval.append("<option value =0>Select Brand</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("brand_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				stringval.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateTypes() {
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<option value=\"\">Select Type</option>\n");
			Str.append("<option value=\"Parts\"").append(StrSelectdrop("Parts", type)).append(">Parts</option>\n");
			Str.append("<option value=\"Tyre\"").append(StrSelectdrop("Tyre", type)).append(">Tyre</option>\n");
			Str.append("<option value=\"Oil\"").append(StrSelectdrop("Oil", type)).append(">Oil</option>\n");
			Str.append("<option value=\"Battery\"").append(StrSelectdrop("Battery", type)).append(">Battery</option>\n");
			Str.append("<option value=\"Break\"").append(StrSelectdrop("Break", type)).append(">Brake</option>\n");
			Str.append("<option value=\"Accessories\"").append(StrSelectdrop("Accessories", type)).append(">Accessories</option>\n");
			Str.append("<option value=\"Labour\"").append(StrSelectdrop("Labour", type)).append(">Labour</option>\n");
			Str.append("<option value=\"Valueadd\"").append(StrSelectdrop("Valueadd", type)).append(">Value Added Service</option>\n");
			Str.append("<option value=\"Extwarranty\"").append(StrSelectdrop("Extwarranty", type)).append(">Extended Warranty</option>\n");
			Str.append("<option value=\"Wheelalign\"").append(StrSelectdrop("Wheelalign", type)).append(">Wheel Alignment</option>\n");
			Str.append("<option value=\"Cng\"").append(StrSelectdrop("Cng", type)).append(">CNG</option>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	protected void UpdateFields() {
		CheckForm();

		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_bill_cat"
						+ " SET"
						+ " billcat_name = '" + billcat_name + "',"
						+ " billcat_brand_id = " + brand_id + ","
						+ " billcat_billtype ='" + type + "',"
						+ "	billcat_code = '" + billcat_code + "',"
						+ " billcat_modified_id = '" + billcat_modified_id + "',"
						+ " billcat_modified_date = '" + billcat_modified_date + "'"
						+ " WHERE billcat_id = " + CNumeric(billcat_id) + "";
				// SOP("StrSql==UpdateFields==" + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (billcat_id.equals("1")) {
			msg = "<br>Cannot Delete First Record!";
			return;
		}

		// Product association
		StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
				+ " WHERE item_billcat_id = " + CNumeric(billcat_id) + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Bill Category is associated with a Product!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_item_bill_cat"
						+ " WHERE billcat_id = " + CNumeric(billcat_id) + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

}
