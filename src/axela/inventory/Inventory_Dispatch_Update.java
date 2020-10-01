package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Dispatch_Update extends Connect {

	public String add = "", emp_id = "", branch_id = "", BranchAccess = "";
	public String comp_id = "0";
	public String inst_id = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String dispatch_id = "", empEditperm = "";
	public String dispatch_warehouse_id = "";
	public String dispatch_item_id = "";
	public String cat_id = "";
	public String dispatch_date = "", dispatchdate = "";
	public String dispatch_qty = "";
	public String dispatch_naration = "";
	public String dispatch_inst_id = "";
	public String dispatch_pid = "";
	public String dispatch_entry_id = "", entry_by = "";
	public String dispatch_entry_date = "", entry_date = "";
	public String dispatch_modified_id = "", modified_by = "";
	public String dispatch_modified_date = "", modified_date = "";
	public String chkPermMsg = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				dispatch_id = PadQuotes(request.getParameter("dispatch_id"));
				dispatch_id = CNumeric(dispatch_id);

				empEditperm = ExecuteQuery("Select emp_receipt_edit from campus_emp where emp_inst_id=" + inst_id + " and  emp_id=" + emp_id + "");
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Dispatch".equals(addB)) {
						dispatch_date = ToLongDate(kknow());
						dispatchdate = strToLongDate(dispatch_date);
						dispatch_entry_id = "";
						dispatch_entry_date = "";
						dispatch_modified_id = "";
						dispatch_modified_date = "";
					} else {
						GetValues(request, response);
						dispatch_entry_id = emp_id;
						dispatch_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("") || !chkPermMsg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-dispatch-list.jsp?dr_warehouse=" + dispatch_warehouse_id + "&dr_category=" + cat_id + "&dr_item="
									+ dispatch_item_id + "&msg=Dispatch added successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"Update Dispatch".equals(updateB) && !"Delete Dispatch".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Dispatch".equals(updateB) && !"Delete Dispatch".equals(deleteB)) {
						GetValues(request, response);
						dispatch_modified_id = emp_id;
						dispatch_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("") || !chkPermMsg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-dispatch-list.jsp?dr_warehouse=" + dispatch_warehouse_id + "&dr_category=" + cat_id + "&dr_item="
									+ dispatch_item_id + "&msg=Dispatch updated successfully!" + msg + ""));
						}
					} else if ("Delete Dispatch".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("") || !chkPermMsg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-dispatch-list.jsp?dr_warehouse=" + dispatch_warehouse_id + "&dr_category=" + cat_id + "&dr_item="
									+ dispatch_item_id + "&msg=Dispatch deleted successfully!"));
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
		dispatch_warehouse_id = PadQuotes(request.getParameter("dr_dispatch_warehouse_id"));
		cat_id = PadQuotes(request.getParameter("dr_cat_id"));
		dispatch_item_id = PadQuotes(request.getParameter("dr_item_id"));
		dispatchdate = PadQuotes(request.getParameter("txt_dispatch_date"));
		dispatch_qty = PadQuotes(request.getParameter("txt_dispatch_qty"));
		dispatch_naration = PadQuotes(request.getParameter("txt_dispatch_naration"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (dispatch_warehouse_id.equals("0")) {
				msg = msg + "<br>Select Location!";
			}
			if (cat_id.equals("0")) {
				msg = msg + "<br>Select Category!";
			}
			if (dispatch_item_id.equals("0")) {
				msg = msg + "<br>Select Item!";
			}
			if (dispatchdate.equals("")) {
				msg = msg + "<br>Select Date!";
			}
			if (!dispatchdate.equals("")) {
				if (!isValidDateFormatLong(dispatchdate)) {
					msg = msg + "<br>Enter Valid Date!";
				} else {
					dispatch_date = ConvertLongDateToStr(dispatchdate);
				}
			}
			if (dispatch_qty.equals("")) {
				msg = msg + "<br>Enter Quantity!";
			} else if (!dispatch_qty.equals("") && !isNumericNeg(dispatch_qty)) {
				msg = msg + "<br>Quantity: Enter Numeric!";
			}
			if (dispatch_qty.equals("0")) {
				msg = msg + "<br>Quantity can not be zero!";
			}
			if (dispatch_naration.equals("")) {
				msg = msg + "<br>Enter Narration!";
			}
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("") && chkPermMsg.equals("")) {
			try {
				StrSql = "INSERT INTO campus_inventory_item_dispatch"
						+ "(dispatch_id,"
						+ " dispatch_pid,"
						+ " dispatch_inst_id,"
						+ " dispatch_warehouse_id,"
						+ " dispatch_item_id,"
						+ " dispatch_date,"
						+ " dispatch_qty,"
						+ " dispatch_naration,"
						+ " dispatch_entry_id,"
						+ " dispatch_entry_date,"
						+ " dispatch_modified_id,"
						+ " dispatch_modified_date)"
						+ " VALUES "
						+ "((SELECT (COALESCE(MAX(dispatch_id),0)+1) FROM campus_inventory_item_dispatch AS dispatch_id),"
						+ " (SELECT (COALESCE(MAX(dispatch_pid),0)+1) FROM campus_inventory_item_dispatch AS dispatch_pid WHERE dispatch_inst_id=" + inst_id + "),"
						+ " " + inst_id + ","
						+ " " + dispatch_warehouse_id + ","
						+ " " + dispatch_item_id + ","
						+ " " + dispatch_date + ","
						+ " " + dispatch_qty + ","
						+ " '" + dispatch_naration + "',"
						+ " " + dispatch_entry_id + ","
						+ " " + dispatch_entry_date + ","
						+ " '0',"
						+ " '')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT dispatch_id, dispatch_warehouse_id, dispatch_item_id,"
					+ " item_cat_id, dispatch_date, dispatch_qty, dispatch_naration,"
					+ " dispatch_entry_id, dispatch_entry_date, dispatch_modified_id, dispatch_modified_date"
					+ " FROM campus_inventory_item_dispatch"
					+ " INNER JOIN campus_inventory_item ON item_id = dispatch_item_id"
					+ " WHERE dispatch_inst_id = " + inst_id + ""
					+ " AND dispatch_id = " + dispatch_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					dispatch_id = crs.getString("dispatch_id");
					dispatch_warehouse_id = crs.getString("dispatch_warehouse_id");
					cat_id = crs.getString("item_cat_id");
					dispatch_item_id = crs.getString("dispatch_item_id");
					dispatch_date = crs.getString("dispatch_date");
					dispatchdate = strToLongDate(dispatch_date);
					dispatch_qty = crs.getString("dispatch_qty");
					dispatch_naration = crs.getString("dispatch_naration");
					dispatch_entry_id = crs.getString("dispatch_entry_id");
					dispatch_entry_date = crs.getString("dispatch_entry_date");
					dispatch_modified_id = crs.getString("dispatch_modified_id");
					dispatch_modified_date = crs.getString("dispatch_modified_date");
					if (!dispatch_entry_id.equals("")) {
						entry_date = strToLongDate(crs.getString("dispatch_entry_date"));
					}
					if (!dispatch_modified_id.equals("0")) {
						modified_date = strToLongDate(crs.getString("dispatch_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Dispatch!"));

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("") && chkPermMsg.equals("")) {
			try {
				StrSql = "UPDATE campus_inventory_item_dispatch"
						+ " SET"
						+ " dispatch_warehouse_id = " + dispatch_warehouse_id + ","
						+ " dispatch_item_id = " + dispatch_item_id + ","
						+ " dispatch_date = " + dispatch_date + ","
						+ " dispatch_qty = " + dispatch_qty + ","
						+ " dispatch_naration = '" + dispatch_naration + "',"
						+ " dispatch_modified_id = " + dispatch_modified_id + ","
						+ " dispatch_modified_date = " + dispatch_modified_date + " "
						+ " WHERE dispatch_inst_id = " + inst_id + ""
						+ " AND dispatch_id = " + dispatch_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("") && chkPermMsg.equals("")) {
			try {
				StrSql = "DELETE FROM campus_inventory_item_dispatch where dispatch_inst_id=" + inst_id + " and dispatch_id =" + dispatch_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateLocation() {
		try {
			StrSql = "SELECT warehouse_id, warehouse_pid, warehouse_name"
					+ " FROM campus_inventory_warehouse"
					+ " WHERE warehouse_inst_id = " + inst_id + ""
					+ " ORDER BY warehouse_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0>Select</option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("warehouse_id") + "";
				group = group + StrSelectdrop(crs.getString("warehouse_id"), dispatch_warehouse_id);
				group = group + ">" + crs.getString("warehouse_name") + " (" + crs.getString("warehouse_pid") + ")" + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem() {
		try {
			if (cat_id.equals("")) {
				cat_id = "0";
			}
			StrSql = "SELECT item_id, item_code, item_name"
					+ " FROM campus_inventory_item"
					+ " WHERE item_inst_id = " + inst_id + ""
					+ " AND item_cat_id = " + cat_id + ""
					+ " AND item_active = '1' "
					+ " ORDER BY item_name ";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<select name=dr_item_id id=dr_item_id class=selectbox><option value=0>Select</option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("item_id") + "";
				group = group + StrSelectdrop(crs.getString("item_id"), dispatch_item_id);
				group = group + ">" + crs.getString("item_name") + "";
				if (!crs.getString("item_code").equals("")) {
					group = group + " (" + crs.getString("item_code") + ")";
				}
				group = group + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCat() {
		try {
			String group = "";
			StrSql = "SELECT cat_id, cat_pid, cat_name"
					+ " FROM campus_inventory_cat"
					+ " WHERE cat_inst_id = " + inst_id + ""
					+ " ORDER BY cat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			group = "<option value=0 >Select</option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("cat_id") + "";
				group = group + StrSelectdrop(crs.getString("cat_id"), cat_id);
				group = group + ">" + crs.getString("cat_name") + " (" + crs.getString("cat_pid") + ")" + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
