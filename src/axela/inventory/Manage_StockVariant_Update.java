// Smitha Nag (16 march 2013)
package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_StockVariant_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";

	public String checkperm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";

	public String vehstockvariant_id = "0";
	public String vehstockvariant_item_id = "0";
	public String vehstockvariant_code = "";
	public String vehstockvariant_entry_id = "";
	public String vehstockvariant_entry_date = "";
	public String vehstockvariant_modified_id = "";
	public String vehstockvariant_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				vehstockvariant_id = CNumeric(PadQuotes(request.getParameter("stockvariant_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if ("yes".equals(addB)) {//
						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							GetValues(request, response);
							vehstockvariant_entry_id = emp_id;
							vehstockvariant_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-stockvariant.jsp?stockvariant_id=" + vehstockvariant_id + "&msg=Variant Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Variant".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Variant".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							GetValues(request, response);
							vehstockvariant_modified_id = emp_id;
							vehstockvariant_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-stockvariant.jsp?stockvariant_id=" + vehstockvariant_id + "&msg=Variant Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Variant".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manage-stockvariant.jsp?msg=Variant Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-stockvariant.jsp?stockvariant_id=" + vehstockvariant_id + "&msg=Variant Updated Successfully!"));
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
		vehstockvariant_id = CNumeric(PadQuotes(request.getParameter("stockvariant_id")));
		vehstockvariant_item_id = CNumeric(PadQuotes(request.getParameter("dr_stockvariant_item_id")));
		vehstockvariant_code = PadQuotes(request.getParameter("txt_stockvariant_code"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (vehstockvariant_item_id.equals("0")) {
			msg += "<br>Select Item!";
		}

		if (vehstockvariant_code.equals("")) {
			msg = msg + "<br>Enter Variant Code!";
		} else {
			try {
				if (!vehstockvariant_code.equals("")) {
					StrSql = "SELECT vehstockvariant_code"
							+ " from " + compdb(comp_id) + "axela_vehstock_variant"
							+ " where vehstockvariant_code = '" + vehstockvariant_code + "'";
					if (update.equals("yes")) {
						StrSql += " and vehstockvariant_id!='" + vehstockvariant_id + "'";
					}
					// SOP("StrSql....."+StrSql);
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Variant Code Found!";
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				vehstockvariant_id = ExecuteQuery("Select (coalesce(max(vehstockvariant_id),0)+1) from " + compdb(comp_id) + "axela_vehstock_variant");
				StrSql = "Insert into " + compdb(comp_id) + "axela_vehstock_variant"
						+ " (vehstockvariant_id,"
						+ " vehstockvariant_item_id,"
						+ " vehstockvariant_code,"
						+ " vehstockvariant_entry_id,"
						+ " vehstockvariant_entry_date)"
						+ " values"
						+ " (" + vehstockvariant_id + ","
						+ " " + vehstockvariant_item_id + ","
						+ " '" + vehstockvariant_code + "',"
						+ " " + vehstockvariant_entry_id + ","
						+ " '" + vehstockvariant_entry_date + "')";
				updateQuery(StrSql);
				// SOP(StrSqlBreaker(StrSql));
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT vehstockvariant_id, vehstockvariant_item_id, vehstockvariant_code, "
					+ " vehstockvariant_entry_date, vehstockvariant_entry_id,"
					+ " vehstockvariant_modified_id, vehstockvariant_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_variant"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstockvariant_item_id"
					+ " WHERE vehstockvariant_id = " + vehstockvariant_id + "";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehstockvariant_item_id = crs.getString("vehstockvariant_item_id");
					vehstockvariant_code = crs.getString("vehstockvariant_code");
					vehstockvariant_entry_id = crs.getString("vehstockvariant_entry_id");
					entry_by = Exename(comp_id, crs.getInt("vehstockvariant_entry_id"));
					entry_date = strToLongDate(crs.getString("vehstockvariant_entry_date"));
					vehstockvariant_modified_id = crs.getString("vehstockvariant_modified_id");
					if (!vehstockvariant_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(vehstockvariant_modified_id));
						modified_date = strToLongDate(crs.getString("vehstockvariant_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Variant ID!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock_variant"
						+ " SET"
						+ " vehstockvariant_item_id = " + vehstockvariant_item_id + ","
						+ " vehstockvariant_code = '" + vehstockvariant_code + "',"
						+ " vehstockvariant_modified_id = " + vehstockvariant_modified_id + ","
						+ " vehstockvariant_modified_date = '" + vehstockvariant_modified_date + "'"
						+ " WHERE vehstockvariant_id = " + vehstockvariant_id + "";
				// SOP("StrSql--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_variant"
						+ " WHERE vehstockvariant_id = " + vehstockvariant_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateItem(String item_id, String param, HttpServletRequest request) {
		StringBuilder stringval = new StringBuilder();
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		try {
			String StrSql = "SELECT item_id, item_name, item_code"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE item_type_id = 1"
					+ " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (param.equals("")) {
				stringval.append("<option value =0>Select Item</option>");
			} else if (param.equals("all")) {
				stringval.append("<option value =0>All Items</option>");
			} else {
				stringval.append("<option value =0>").append(param).append("</option>");
			}
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("item_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("item_id"), item_id));
				stringval.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
