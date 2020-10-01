package axela.inventory;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Cat_Update extends Connect {

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
	public String cat_id = "", cat_parent_id = "";
	public String cat_name = "";
	public String cat_pid = "";
	public String cat_entry_id = "", entry_by = "";
	public String cat_entry_date = "", entry_date = "";
	public String cat_modified_id = "", modified_by = "";
	public String cat_modified_date = "", modified_date = "";
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
				cat_id = PadQuotes(request.getParameter("cat_id"));
				cat_parent_id = PadQuotes(request.getParameter("cat_parent_id"));// cat_id
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Category".equals(addB)) {
						cat_name = "";
						cat_entry_id = "";
						cat_entry_date = "";
						cat_modified_id = "";
						cat_modified_date = "";
					} else {
						CheckPerm(comp_id, "emp_item_add, emp_sales_item_add, emp_pos_item_add", request, response);
						GetValues(request, response);
						cat_entry_id = emp_id;
						cat_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-cat-list.jsp?cat_id=" + CNumeric(cat_id) + "&msg=Category added successfully!"));
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"Update Category".equals(updateB) && !"Delete Category".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Category".equals(updateB) && !"Delete Category".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_edit, emp_sales_item_edit, emp_pos_item_edit", request, response);
						GetValues(request, response);
						cat_modified_id = emp_id;
						cat_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-cat-list.jsp?cat_id=" + CNumeric(cat_id) + "&msg=Category updated successfully!" + msg + ""));
						}
					} else if ("Delete Category".equals(deleteB)) {

						CheckPerm(comp_id, "emp_item_delete, emp_sales_item_delete, emp_pos_item_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-cat-list.jsp?msg=Category deleted successfully!"));
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
		cat_name = PadQuotes(request.getParameter("txt_cat_name"));
		cat_parent_id = PadQuotes(request.getParameter("dr_cat_parent"));
		entry_by = unescapehtml(PadQuotes(request.getParameter("entry_by")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (cat_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}
		try {
			StrSql = "SELECT cat_name FROM " + compdb(comp_id) + "axela_inventory_cat"
					+ " WHERE cat_name = '" + cat_name + "'"
					+ " AND cat_parent_id = " + cat_parent_id;
			if (update.equals("yes")) {
				StrSql = StrSql + " AND cat_id != " + CNumeric(cat_id) + "";
			}
			ResultSet rsuname = processQuery(StrSql, 0);
			if (rsuname.isBeforeFirst()) {
				msg = msg + "<br>Similar Name found!";
			}
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
				cat_id = CNumeric(PadQuotes(ExecuteQuery("SELECT (COALESCE(MAX(cat_id),0)+1) FROM " + compdb(comp_id) + "axela_inventory_cat AS category_id")));
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_cat"
						+ " (cat_id,"
						+ " cat_name,"
						+ " cat_parent_id,"
						+ " cat_active,"
						+ " cat_entry_id,"
						+ " cat_entry_date,"
						+ " cat_modified_id,"
						+ " cat_modified_date)"
						+ " VALUES"
						+ " (" + CNumeric(cat_id) + ","
						+ " '" + cat_name + "',"
						+ " '" + cat_parent_id + "',"
						+ " '1',"
						+ " '" + cat_entry_id + "',"
						+ " '" + cat_entry_date + "',"
						+ " '0',"
						+ " '')";
				// SOP("insert ----------" + StrSql);
				updateQuery(StrSql);
				// SOP("after insert query----" + StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_cat_pop";
				updateQuery(StrSql);
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_cat_pop"
						+ " ("
						+ "cat_id,"
						+ " cat_name,"
						+ " cat_rank,"
						+ " cat_active)"
						+ " VALUES "
						+ UpdateCategoryPop("0");
				StrSql = StrSql.substring(0, StrSql.length() - 1);
				// SOP("StrSql-----" + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String UpdateCategoryPop(String p_id) {
		String Str = "", catname = "";
		int rank = 0;
		try {
			StrSql = "SELECT cat_id, cat_name, cat_parent_id, cat_active"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat"
					+ " WHERE cat_parent_id = " + p_id + "";
			StrSql = StrSql + " ORDER BY cat_name";
			// SOP("StrSql==catpop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				rank++;
				if (crs.getString("cat_parent_id").equals("0")) {
					catname = crs.getString("cat_name");
				} else {
					catname = PopulateCategoryStr(crs.getString("cat_id"), comp_id);
				}
				p_id = crs.getString("cat_id");
				Str = Str + "(" + crs.getString("cat_id") + ", '" + catname + "', " + rank + " ,'" + crs.getString("cat_active") + "'),";
				Str = Str + UpdateCategoryPop(p_id);
			}
			crs.close();
			return Str;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			StrSql = "SELECT cat_name, cat_parent_id,"
					+ " cat_entry_id, cat_entry_date, cat_modified_id, cat_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat"
					+ " WHERE cat_id = " + CNumeric(cat_id);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					cat_name = crs.getString("cat_name");
					cat_parent_id = crs.getString("cat_parent_id");
					cat_entry_id = crs.getString("cat_entry_id");
					cat_entry_date = crs.getString("cat_entry_date");
					cat_modified_id = crs.getString("cat_modified_id");
					cat_modified_date = crs.getString("cat_modified_date");
					if (!cat_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(cat_entry_id));
						entry_date = strToLongDate(crs.getString("cat_entry_date"));
					}
					if (!cat_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(cat_modified_id));
						modified_date = strToLongDate(crs.getString("cat_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Category!"));

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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_cat"
						+ " SET"
						+ " cat_name = '" + cat_name + "',"
						+ " cat_parent_id = " + cat_parent_id + ","
						+ " cat_modified_id = " + cat_modified_id + ","
						+ " cat_modified_date = '" + cat_modified_date + "'"
						+ " WHERE cat_id = " + CNumeric(cat_id) + "";
				// SOP("before updateQuery-----" + StrSql);
				updateQuery(StrSql);
				// SOP("after updateQuery-----" + StrSql);
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_cat_pop";
				updateQuery(StrSql);
				// SOP("after delete");

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_cat_pop"
						+ " (cat_id, cat_name, cat_rank, cat_active)"
						+ " values " + UpdateCategoryPop("0");
				StrSql = StrSql.substring(0, StrSql.length() - 1);
				// SOP("StrSql------->" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (cat_id.equals("1")) {
			msg = "<br>Cannot Delete First Record!";
			return;
		}

		// Category association
		StrSql = "SELECT cat_id FROM " + compdb(comp_id) + "axela_inventory_cat"
				+ " WHERE cat_parent_id = " + CNumeric(cat_id) + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Category has Sub Category(s)!";
		}

		StrSql = "SELECT cat_id FROM " + compdb(comp_id) + "axela_inventory_cat"
				+ " WHERE cat_parent_id != '0' and cat_id = " + CNumeric(cat_id) + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Category is a Child Category!";
		}

		// Product association
		StrSql = "SELECT item_id FROM " + compdb(comp_id) + "axela_inventory_item"
				+ " WHERE item_cat_id = " + CNumeric(cat_id) + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Category is associated with a Product!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_cat"
						+ " WHERE cat_id = " + CNumeric(cat_id) + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_cat_pop"
						+ " WHERE cat_id = " + CNumeric(cat_id) + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateCat(String cat_p_id, String active) {
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		try {
			StrSql = "SELECT cat_id, cat_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_cat"
					+ " WHERE 1 = 1";

			if (!active.equals("")) {
				StrSql = StrSql + " AND cat_active = " + active + "";
			}
			if (!cat_id.equals("")) {
				StrSql = StrSql + " AND cat_id != " + CNumeric(cat_id) + "";
			}
			StrSql = StrSql + " ORDER BY cat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cat_id"));
				Str.append(StrSelectdrop(crs.getString("cat_id"), cat_parent_id));
				Str.append(">").append(crs.getString("cat_name"));
				Str.append(":</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
}
