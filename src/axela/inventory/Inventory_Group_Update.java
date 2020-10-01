package axela.inventory;
//Bhagwan Singh 23/01/2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Group_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String group_id = "0";
	public String group_name = "";
	public String group_desc = "";
	public String group_type = "";
	public String group_aftertax = "";
	public String group_active = "1";
	public String group_rank = "";
	public String QueryString = "";
	public String group_entry_id = "0";
	public String group_entry_date = "";
	public String group_modified_id = "0";
	public String group_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				group_id = CNumeric(PadQuotes(request.getParameter("group_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						group_name = "";
					} else {
						GetValues(request, response);
						group_entry_id = emp_id;
						group_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-group-list.jsp?group_id=" + group_id + "&msg=Item Group Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Group".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Group".equals(deleteB)) {
						GetValues(request, response);
						group_modified_id = emp_id;
						group_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-group-list.jsp?group_id=" + group_id + "&msg=Item Group Updated Successfully!"));
						}
					} else if ("Delete Group".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-group-list.jsp?msg=Item Group Deleted Successfully!"));
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
		group_id = CNumeric(PadQuotes(request.getParameter("group_id")));
		group_name = PadQuotes(request.getParameter("txt_group_name"));
		group_desc = PadQuotes(request.getParameter("txt_group_desc"));
		group_type = PadQuotes(request.getParameter("dr_group_type"));
		group_aftertax = PadQuotes(request.getParameter("ch_group_aftertax"));
		group_active = PadQuotes(request.getParameter("ch_group_active"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		try {
			if (group_name.equals("")) {
				msg = msg + "<br>Enter the Item Group Name!";
			}
			if (!group_name.equals("")) {
				StrSql = "SELECT group_name"
						+ " FROM " + compdb(comp_id) + "axela_inventory_group"
						+ " WHERE group_name = '" + group_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND group_id != " + group_id + "";
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Similar Item Group found!";
				}
			}
			if (group_type.equals("0")) {
				msg = msg + "<br>Select Item Group Type!";
			}
			if (group_aftertax.equals("on")) {
				group_aftertax = "1";
			} else {
				group_aftertax = "0";
			}
			if (group_active.equals("on")) {
				group_active = "1";
			} else {
				group_active = "0";
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
				group_id = ExecuteQuery("Select (coalesce(max(group_id),0)+1) from " + compdb(comp_id) + "axela_inventory_group");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_group"
						+ " (group_id,"
						+ " group_name,"
						+ " group_desc,"
						+ " group_type,"
						+ " group_aftertax,"
						+ " group_active,"
						+ " group_rank,"
						+ " group_entry_id,"
						+ " group_entry_date)"
						+ " VALUES"
						+ " (" + group_id + ","
						+ " '" + group_name + "',"
						+ " '" + group_desc + "',"
						+ " " + group_type + ","
						+ " " + group_aftertax + ","
						+ " " + group_active + ","
						+ " (SELECT (COALESCE(MAX(group_rank),0)+1) from " + compdb(comp_id) + "axela_inventory_group AS Rank WHERE 1=1 ),"
						+ " " + group_entry_id + ","
						+ " '" + group_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT *"
					+ " FROM " + compdb(comp_id) + "axela_inventory_group"
					+ " WHERE group_id = " + group_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					group_name = crs.getString("group_name");
					group_desc = crs.getString("group_desc");
					group_type = crs.getString("group_type");
					group_aftertax = crs.getString("group_aftertax");
					group_active = crs.getString("group_active");
					group_entry_id = crs.getString("group_entry_id");
					if (!group_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(group_entry_id));
						entry_date = strToLongDate(crs.getString("group_entry_date"));
					}
					group_modified_id = crs.getString("group_modified_id");
					if (!group_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(group_modified_id));
						modified_date = strToLongDate(crs.getString("group_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item Group!"));
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
				StrSql = " UPDATE  " + compdb(comp_id) + "axela_inventory_group"
						+ " SET"
						+ " group_name = '" + group_name + "',"
						+ " group_desc = '" + group_desc + "',"
						+ " group_aftertax = '" + group_aftertax + "',"
						+ " group_active = '" + group_active + "',"
						+ " group_type = '" + group_type + "',"
						+ " group_modified_id = " + group_modified_id + ","
						+ " group_modified_date = '" + group_modified_date + "'"
						+ " where group_id = " + group_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT option_group_id FROM " + compdb(comp_id) + "axela_inventory_item_option"
				+ " WHERE option_group_id = " + group_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Item Group is Associated with Item!";
		}
		if (group_id.equals("1")) {
			msg = msg + "<br>Item Group Cannot Be Deleted!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_group WHERE  group_id = " + group_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateGroupType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"").append(StrSelectdrop("0", group_type)).append(">Select</option>");
		Str.append("<option value=\"1\"").append(StrSelectdrop("1", group_type)).append(">Default</option>");
		Str.append("<option value=\"2\"").append(StrSelectdrop("2", group_type)).append(">All Selected</option>");
		Str.append("<option value=\"3\"").append(StrSelectdrop("3", group_type)).append(">Multi Select</option>");
		return Str.toString();
	}
}
