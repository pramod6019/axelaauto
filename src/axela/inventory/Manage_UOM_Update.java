package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_UOM_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String uom_id = "0";
	public String uom_name = "";
	public String uom_shortname = "";
	public String uom_ratio = "0";
	public String uom_parent_id = "0";
	public String uom_entry_id = "0";
	public String entry_by = "";
	public String uom_entry_date = "";
	public String entry_date = "";
	public String uom_modified_id = "0";
	public String modified_by = "";
	public String uom_modified_date = "";
	public String modified_date = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				uom_id = CNumeric(PadQuotes(request.getParameter("uom_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						uom_entry_id = "";
						uom_entry_date = "";
						uom_modified_id = "";
						uom_modified_date = "";
					} else {
						CheckPerm(comp_id, "emp_item_add", request, response);
						GetValues(request, response);
						uom_entry_id = emp_id;
						uom_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-uom-list.jsp?uom_id=" + uom_id + "&msg=UOM Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete UOM".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete UOM".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_edit", request, response);
						GetValues(request, response);
						uom_modified_id = emp_id;
						uom_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-uom-list.jsp?uom_id=" + uom_id + "&msg=UOM Updated Successfully!"));
						}
					} else if ("Delete UOM".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manage-uom-list.jsp?msg=UOM Deleted Successfully!"));
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
		uom_name = PadQuotes(request.getParameter("txt_uom_name"));
		uom_shortname = PadQuotes(request.getParameter("txt_uom_shortname"));
		uom_ratio = PadQuotes(request.getParameter("txt_uom_ratio"));
		uom_parent_id = PadQuotes(request.getParameter("dr_uom_parent_id"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		try {
			if (uom_name.equals("")) {
				msg = msg + "<br>Enter Name!";
			}
			if (!uom_name.equals("")) {
				StrSql = "SELECT uom_name"
						+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
						+ " WHERE uom_name = '" + uom_name + "'"
						+ " AND uom_id != " + uom_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Name Found! ";
				}
			}
			if (uom_shortname.equals("")) {
				msg = msg + "<br>Enter Short Name!";
			}
			if (!uom_parent_id.equals("0")) {
				if (uom_ratio.equals("")) {
					msg = msg + "<br>Enter Ratio!";
				}
			} else if (uom_parent_id.equals("0")) {
				uom_ratio = "0";
			}
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				uom_id = ExecuteQuery("Select (coalesce(max(uom_id),0)+1) from " + compdb(comp_id) + "axela_inventory_uom");
				StrSql = "INSERT into " + compdb(comp_id) + "axela_inventory_uom"
						+ " (uom_id,"
						+ " uom_name,"
						+ " uom_shortname,"
						+ " uom_parent_id,"
						+ " uom_ratio,"
						+ " uom_entry_id,"
						+ " uom_entry_date)"
						+ " values"
						+ " (" + uom_id + ","
						+ " '" + uom_name + "',"
						+ " '" + uom_shortname + "',"
						+ " " + uom_parent_id + ","
						+ " " + CNumeric(uom_ratio) + ","
						+ " " + uom_entry_id + ","
						+ " '" + uom_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select uom_id, uom_name,uom_shortname, uom_parent_id, uom_ratio,"
					+ " uom_entry_id, uom_entry_date, uom_modified_id, uom_modified_date"
					+ " from " + compdb(comp_id) + "axela_inventory_uom"
					+ " where uom_id = " + uom_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					uom_name = crs.getString("uom_name");
					uom_shortname = crs.getString("uom_shortname");
					uom_ratio = crs.getString("uom_ratio");
					if (uom_ratio.equals("0.00")) {
						uom_ratio = "";
					}
					uom_parent_id = crs.getString("uom_parent_id");
					uom_entry_id = crs.getString("uom_entry_id");
					uom_entry_date = crs.getString("uom_entry_date");
					uom_modified_id = crs.getString("uom_modified_id");
					uom_modified_date = crs.getString("uom_modified_date");
					if (!uom_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(uom_entry_id));
						entry_date = strToLongDate(crs.getString("uom_entry_date"));
					}
					if (!uom_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(uom_modified_id));
						modified_date = strToLongDate(crs.getString("uom_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid UOM!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_uom"
						+ " SET"
						+ " uom_name = '" + uom_name + "',"
						+ " uom_shortname = '" + uom_shortname + "',"
						+ " uom_ratio = " + CNumeric(uom_ratio) + ","
						+ " uom_parent_id = " + uom_parent_id + ","
						+ " uom_modified_id = " + uom_modified_id + ","
						+ " uom_modified_date = '" + uom_modified_date + "'"
						+ " where uom_id = " + uom_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "Select uom_parent_id from " + compdb(comp_id) + "axela_inventory_uom"
				+ " where uom_parent_id = " + uom_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>UOM Associated with Another UOM!";
		}
		StrSql = "SELECT item_uom_id"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item"
				+ " WHERE item_uom_id = " + uom_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>UOM Associated with Item!";
		}
		if (msg.equals("")) {
			try {

				StrSql = "Delete from " + compdb(comp_id) + "axela_inventory_uom"
						+ " where uom_id = " + uom_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateParentUOM() {
		try {
			String group = "";
			StrSql = "SELECT uom_id, uom_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_uom"
					+ " WHERE uom_parent_id != 0"
					+ " ORDER BY uom_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			group = "<option value=0 >Select</option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("uom_id") + "";
				group = group + StrSelectdrop(crs.getString("uom_id"), uom_parent_id);
				group = group + ">" + crs.getString("uom_name") + "</option> \n";
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
