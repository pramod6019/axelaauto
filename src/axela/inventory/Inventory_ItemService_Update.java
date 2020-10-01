package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_ItemService_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String itemservice_item_id = "0";
	public String item_name = "";
	public String itemservice_id = "0";
	public String itemservice_jctype_id = "0";
	public String itemservice_kms = "";
	public String itemservice_days = "";
	public String item_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String QueryString = "";
	public String itemservice_entry_id = "0";
	public String itemservice_entry_date = "";
	public String entry_date = "";
	public String itemservice_modified_id = "0";
	public String itemservice_modified_date = "";
	public String modified_date = "";
	public String itemservice_entry_by = "";
	public String itemservice_modified_by = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				itemservice_item_id = PadQuotes(request.getParameter("item_id"));
				if (!itemservice_item_id.equals("") && add.equals("yes")) {
					item_name = ExecuteQuery("Select if(item_code!='',concat(item_name,' (',item_code,')'), item_name) as item_name"
							+ " from " + compdb(comp_id) + "axela_inventory_item where item_id = " + CNumeric(itemservice_item_id));
					if (item_name.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
					}
				}

				itemservice_id = CNumeric(PadQuotes(request.getParameter("itemservice_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Service".equals(addB)) {
					} else {
						CheckPerm(comp_id, "emp_item_add, emp_sales_item_add, emp_pos_item_add", request, response);
						GetValues(request, response);
						itemservice_entry_id = emp_id;
						itemservice_entry_date = ToLongDate(kknow());
						itemservice_modified_date = "";
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-itemservice-list.jsp?item_id=" + itemservice_item_id + "&msg=Item Service added successfully!"));
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"Update Service".equals(updateB) && !"Delete Service".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Service".equals(updateB) && !"Delete Service".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_edit, emp_sales_item_edit, emp_pos_item_edit", request, response);
						GetValues(request, response);
						itemservice_entry_id = emp_id;
						itemservice_entry_date = ToLongDate(kknow());
						itemservice_modified_id = emp_id;
						itemservice_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-itemservice-list.jsp?item_id=" + itemservice_item_id + "&msg=Item Service updated successfully!" + msg + ""));
						}
					} else if ("Delete Service".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_delete, emp_sales_item_delete, emp_pos_item_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-itemservice-list.jsp?all=yes&msg=Item Service deleted successfully!"));
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
		itemservice_item_id = PadQuotes(request.getParameter("txt_item_id"));
		itemservice_jctype_id = CNumeric(PadQuotes(request.getParameter("itemservice_jctype_id")));
		itemservice_kms = PadQuotes(request.getParameter("txt_itemservice_kms"));
		itemservice_days = PadQuotes(request.getParameter("txt_itemservice_days"));
		item_name = PadQuotes(request.getParameter("txt_item_name"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		itemservice_entry_by = unescapehtml(PadQuotes(request.getParameter("itemservice_entry_by")));
		itemservice_modified_by = PadQuotes(request.getParameter("itemservice_modified_by"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (itemservice_jctype_id.equals("0")) {
				msg = msg + "<br>Enter Service Type!";
			}

			if (itemservice_kms.equals("0") || itemservice_kms.equals("")) {
				msg = msg + "<br>Enter Kilometer!";
			}
			if (itemservice_days.equals("0") || itemservice_days.equals("")) {
				msg = msg + "<br>Enter Days!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				// itemservice_item_id = ExecuteQuery("Select (coalesce(max(itemservice_item_id),0)+1) from " + compdb(comp_id) + "axela_inventory_item_service as itemservice_item_id");
				itemservice_id = ExecuteQuery("SELECT (COALESCE(MAX(itemservice_id),0)+1) FROM " + compdb(comp_id) + "axela_inventory_item_service as itemservice_id");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_service"
						+ " (itemservice_id,"
						+ " itemservice_item_id,"
						+ " itemservice_jctype_id,"
						+ " itemservice_kms,"
						+ " itemservice_days,"
						+ " itemservice_entry_id,"
						+ " itemservice_entry_date)"
						+ " VALUES"
						+ " (" + itemservice_id + ","
						+ " " + itemservice_item_id + ","
						+ " " + itemservice_jctype_id + ","
						+ " " + itemservice_kms + ","
						+ " " + CNumeric(itemservice_days) + ","
						+ " '" + itemservice_entry_id + "',"
						+ " '" + itemservice_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT itemservice_id, itemservice_item_id, itemservice_jctype_id, itemservice_kms,"
					+ " itemservice_days, itemservice_entry_id, itemservice_entry_date,"
					+ " itemservice_modified_id, itemservice_modified_date,"
					+ " if(item_code!='',concat(item_name,' (',item_code,')'), item_name) as item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_service"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = itemservice_item_id"
					+ " WHERE itemservice_id = " + itemservice_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_name = crs.getString("item_name");
					itemservice_id = crs.getString("itemservice_id");
					itemservice_item_id = crs.getString("itemservice_item_id");
					itemservice_jctype_id = crs.getString("itemservice_jctype_id");
					itemservice_kms = crs.getString("itemservice_kms");
					itemservice_days = crs.getString("itemservice_days");
					itemservice_entry_id = crs.getString("itemservice_entry_id");
					if (!itemservice_entry_id.equals("")) {
						itemservice_entry_by = Exename(comp_id, Integer.parseInt(itemservice_entry_id));
					}
					entry_date = strToLongDate(crs.getString("itemservice_entry_date"));

					itemservice_modified_id = crs.getString("itemservice_modified_id");
					if (!itemservice_modified_id.equals("0")) {
						itemservice_modified_by = Exename(comp_id, Integer.parseInt(itemservice_modified_id));
						modified_date = strToLongDate(crs.getString("itemservice_modified_date"));
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
			try {

				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_service"
						+ " SET"
						+ " itemservice_jctype_id = " + itemservice_jctype_id + ","
						+ " itemservice_kms = " + itemservice_kms + ","
						+ " itemservice_days = " + itemservice_days + ","
						+ " itemservice_modified_id = '" + itemservice_modified_id + "',"
						+ " itemservice_modified_date = '" + itemservice_modified_date + "'"
						+ " WHERE itemservice_id = " + itemservice_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		// Delete records
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_item_service"
						+ " WHERE itemservice_item_id = " + itemservice_item_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateServiceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " WHERE 1=1 order by jctype_id";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("jctype_id"), itemservice_jctype_id));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
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
