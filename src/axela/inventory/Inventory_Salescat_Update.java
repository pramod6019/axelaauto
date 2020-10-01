package axela.inventory;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Salescat_Update extends Connect {

	public String add = "", emp_id = "", branch_id = "", BranchAccess = "";
	public String update = "";
	public String emp_role_id = "0";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String salescat_id = "";
	public String salescat_name = "";
	public String salescat_entry_id = "", entry_by = "";
	public String salescat_entry_date = "", entry_date = "";
	public String salescat_modified_id = "", modified_by = "";
	public String salescat_modified_date = "", modified_date = "";
	public String[] inventory_item_trans;
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
				salescat_id = CNumeric(PadQuotes(request.getParameter("salescat_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"Add Sales Category".equals(addB)) {
						salescat_name = "";
						salescat_entry_id = "";
						salescat_entry_date = "";
						salescat_modified_id = "";
						salescat_modified_date = "";
					} else {
						CheckPerm(comp_id, "emp_item_add, emp_sales_item_add, emp_pos_item_add", request, response);
						GetValues(request, response);
						salescat_entry_id = emp_id;
						salescat_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-salescat-list.jsp?salescat_id=" + salescat_id + "&msg=Sales Category added successfully!"));
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"Update Sales Category".equals(updateB) && !"Delete Sales Category".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Sales Category".equals(updateB) && !"Delete Sales Category".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_edit, emp_sales_item_edit, emp_pos_item_edit", request, response);
						GetValues(request, response);
						UpdateList();
						salescat_modified_id = emp_id;
						salescat_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-salescat-list.jsp?salescat_id=" + salescat_id + "&msg=Sales Category updated successfully!" + msg + ""));
						}
					} else if ("Delete Sales Category".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_delete, emp_sales_item_delete, emp_pos_item_delete", request, response);
						GetValues(request, response);
						UpdateList();
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-salescat-list.jsp?msg=Sales Category deleted successfully!"));
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
		salescat_name = PadQuotes(request.getParameter("txt_salescat_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		inventory_item_trans = request.getParameterValues("inventory_item_trans");
	}

	protected void CheckForm() {
		msg = "";
		if (salescat_name.equals("")) {
			msg = msg + "<br>Enter  Name!";
		}
		try {
			StrSql = "Select salescat_name from " + compdb(comp_id) + "axela_inventory_salescat"
					+ " where salescat_name = '" + salescat_name + "'";
			if (update.equals("yes")) {
				StrSql = StrSql + " and salescat_id != " + salescat_id + "";
			}
			ResultSet rsuname = processQuery(StrSql, 0);
			if (rsuname.isBeforeFirst()) {
				msg = msg + "<br>Similar Name found!";
			}
			// SOP(msg);
			rsuname.close();
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "Insert into " + compdb(comp_id) + "axela_inventory_salescat"
						+ " (salescat_name,"
						+ " salescat_entry_id,"
						+ " salescat_entry_date,"
						+ " salescat_modified_id,"
						+ " salescat_modified_date)"
						+ " values"
						+ " ('" + salescat_name + "',"
						+ " '" + salescat_entry_id + "',"
						+ " '" + salescat_entry_date + "',"
						+ " '0',"
						+ " '')";
				salescat_id = UpdateQueryReturnID(StrSql);
				if (!salescat_id.equals("") && !salescat_id.equals("0")) {
					UpdateList();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select salescat_id, salescat_name, salescat_entry_id,"
					+ " salescat_entry_date, salescat_modified_id, salescat_modified_date"
					+ " from " + compdb(comp_id) + "axela_inventory_salescat"
					+ " where salescat_id = " + salescat_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					salescat_id = crs.getString("salescat_id");
					salescat_name = crs.getString("salescat_name");
					salescat_entry_id = crs.getString("salescat_entry_id");
					salescat_entry_date = crs.getString("salescat_entry_date");
					salescat_modified_id = crs.getString("salescat_modified_id");
					salescat_modified_date = crs.getString("salescat_modified_date");
					if (!salescat_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(salescat_entry_id));
						entry_date = strToLongDate(crs.getString("salescat_entry_date"));
					}
					if (!salescat_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(salescat_modified_id));
						modified_date = strToLongDate(crs.getString("salescat_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Sales Category!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_salescat"
						+ " SET"
						+ " salescat_name = '" + salescat_name + "',"
						+ " salescat_modified_id = '" + salescat_modified_id + "',"
						+ " salescat_modified_date = '" + salescat_modified_date + "'"
						+ " where salescat_id = " + salescat_id + "";
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
				StrSql = "Delete from " + compdb(comp_id) + "axela_inventory_salescat_trans"
						+ " where trans_salescat_id = " + salescat_id + "";
				updateQuery(StrSql);
				StrSql = "Delete from " + compdb(comp_id) + "axela_inventory_salescat"
						+ " where salescat_id = " + salescat_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void UpdateList() {
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_inventory_salescat_trans"
						+ " where trans_salescat_id = " + salescat_id + "";
				updateQuery(StrSql);

				if (inventory_item_trans != null) {
					for (int i = 0; i < inventory_item_trans.length; i++) {
						StrSql = "Insert into " + compdb(comp_id) + "axela_inventory_salescat_trans"
								+ " (trans_salescat_id,"
								+ " trans_item_id)"
								+ " values"
								+ " (" + salescat_id + ","
								+ " " + inventory_item_trans[i] + ")";
						updateQuery(StrSql);
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateItem(String comp_id) {
		try {
			StrSql = "Select item_id, if(item_code!='',concat(item_name,' (',item_code,')'), item_name) as item_name"
					+ " from " + compdb(comp_id) + "axela_inventory_item"
					+ " where item_active = '1'"
					+ " order by item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String stringval = "";
			while (crs.next()) {
				stringval = stringval + "<option value=" + crs.getString("item_id") + " >" + crs.getString("item_name") + "</option> \n";
			}
			crs.close();
			return stringval;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItemTrans(String comp_id) {
		String stringval = "";
		try {
			StrSql = "Select item_id, concat(item_name,' (',item_code,')') as item_name"
					+ " from " + compdb(comp_id) + "axela_inventory_item"
					+ " inner JOIN " + compdb(comp_id) + "axela_inventory_salescat_trans ON trans_item_id = item_id"
					+ " where  trans_salescat_id = '" + salescat_id + "'"
					+ " and item_active = '1' order by item_name";
			if (add.equals("yes") && inventory_item_trans != null) {
				StrSql = "Select concat(item_name,' (',item_code,')') as item_name, item_id"
						+ " from " + compdb(comp_id) + "axela_inventory_item"
						+ " where item_active = '1' order by item_name";
			}
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if (add.equals("yes") && inventory_item_trans != null) {
					for (int i = 0; i < inventory_item_trans.length; i++) {
						if (crs.getString("item_id").equals(inventory_item_trans[i])) {
							stringval = stringval + "<option value=" + crs.getString("item_id") + " selected>"
									+ crs.getString("item_name") + "</option> \n";
						}
					}
				} else if (update.equals("yes")) {
					stringval = stringval + "<option value=" + crs.getString("item_id") + " selected>"
							+ crs.getString("item_name") + "</option>\n";
				}
			}
			crs.close();
			return stringval;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
