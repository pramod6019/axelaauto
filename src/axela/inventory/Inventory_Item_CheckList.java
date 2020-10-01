package axela.inventory;

/**
 *
 * @author Gurumurthy TS, 28 JAN 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Item_CheckList extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = " ";
	public String msg = " ";
	public String StrSql = "";
	public String all = "", update = "", add = "", delete = "";
	public String item_id = "", item_name = "";
	public String check_type = "0";
	public String check_name = "";
	public String check_id = "0";
	public String status = "";
	public String deleteB = "", addB = "";
	public String updateB = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				item_id = PadQuotes(request.getParameter("item_id"));
				check_id = CNumeric(PadQuotes(request.getParameter("check_id")));
				GetValues(request, response);
				if (!item_id.equals("")) {
					item_name = ExecuteQuery("select item_name from " + compdb(comp_id) + "axela_inventory_item where item_id=" + CNumeric(item_id));
					if (item_name.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
					}
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add) && !item_id.equals("")) {
					if (!"Add Check List".equals(addB)) {
					} else {
						CheckForm();
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-item-checklist.jsp?item_id=" + item_id + "&msg=Check List added successfully!"));
						}
					}
				}
				if ("yes".equals(update) && !item_id.equals("")) {
					if (!"Update Check List".equals(updateB) && !"Delete Check List".equals(deleteB)) {
						PopulateFields(request, response);
					} else if ("Update Check List".equals(updateB) && !"Delete Check List".equals(deleteB)) {
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-item-checklist.jsp?item_id=" + item_id + "&msg=Check List Updated successfully!"));
						}
					} else if ("Delete Check List".equals(deleteB) && !item_id.equals("")) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("inventory-item-checklist.jsp?item_id=" + item_id + "&msg=Check List deleted successfully!"));
						}
					}
				}
				StrHTML = Listdata();
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

	public void GetValues(HttpServletRequest request, HttpServletResponse response) {
		check_name = PadQuotes(request.getParameter("txt_item_name"));
		check_type = CNumeric(PadQuotes(request.getParameter("dr_check_type")));
	}

	public void CheckForm() {
		msg = "";
		if (check_name.equals("")) {
			msg = msg + "<br>Please Enter Name!";
		}
		StrSql = "Select check_name from " + compdb(comp_id) + "axela_service_jc_check"
				+ " where check_name = '" + check_name + "'"
				+ " and check_item_id = " + item_id + "";
		if (!check_name.equals("") && check_name.equals(ExecuteQuery(StrSql))) {
			msg = msg + "<br>Name already exist!";
		}

		if (check_type.equals("0")) {
			msg = msg + "<br>Please Select Type!";
		}

	}

	protected void AddFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Insert into " + compdb(comp_id) + "axela_service_jc_check "
						+ "(check_name, "
						+ " check_type, "
						+ " check_item_id)"
						+ " values"
						+ " ('" + check_name + "',"
						+ " '" + check_type + "',"
						+ " " + item_id + ")";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void DeleteFields() {
		StrSql = "Delete from " + compdb(comp_id) + "axela_service_jc_check"
				+ " where check_id = " + check_id + "";
		updateQuery(StrSql);
	}

	public String Listdata() {
		CachedRowSet crs = null;
		int count = 0;
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT check_id, check_item_id, check_name, check_type"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_check "
				+ " WHERE check_item_id = " + CNumeric(item_id) + ""
				+ " AND check_type = '1'"
				+ " GROUP BY check_id"
				+ " ORDER BY check_id desc";
		try {
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<br><tr align=center><b>Before Trial</b></tr>");
				Str.append("<th width=5%>#</th>");
				Str.append("<th>Check Name</th>");
				Str.append("<th width=20%>Actions</th>");
				Str.append("</tr>");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td align=center valign=top nowrap>").append(count).append("</td>\n");
					Str.append("<td align=left>\n").append(crs.getString("check_name")).append("</td>");
					Str.append("<td>").append("<a href=\"inventory-item-checklist.jsp?update=yes&check_id=").append(crs.getString("check_id")).append("&item_id=")
							.append(crs.getString("check_item_id")).append("\">Update Service Check List</a>").append("</td>");
					Str.append("</tr>");

				}
				Str.append("</table>");
			}
			crs.close();

			StrSql = "SELECT check_id, check_item_id , check_name, check_type"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_check "
					+ " WHERE check_item_id=" + CNumeric(item_id) + " AND check_type='2'"
					+ " GROUP BY check_id ORDER BY check_id desc";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				count = 0;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<br><tr align=center><b>After Trial</b></tr>");
				Str.append("<th width=5%>#</th>");
				Str.append("<th>Check Name</th>");
				Str.append("<th width=20%>Actions</th>");
				Str.append("</tr>");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td align=center valign=top nowrap>").append(count).append("</td>\n");
					Str.append("<td align=left>\n").append(crs.getString("check_name")).append("</td>");
					Str.append("<td>").append("<a href=\"inventory-item-checklist.jsp?update=yes&check_id=").append(crs.getString("check_id")).append("&item_id=")
							.append(crs.getString("check_item_id")).append("\">Update Service Check List</a>").append("</td>");
					Str.append("</tr>");
				}
				Str.append("</table>");
			}
			// else {
			//
			// }

			crs.close();
			if (Str.toString().equals("")) {
				Str.append("<br><br><br><br><b><font color=red>No Check list found!</font></b><br><br>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCheckType() {
		StringBuilder str = new StringBuilder();
		str.append("<option value=0 ").append(StrSelectdrop(check_type, "0")).append(">Select</option>");
		str.append("<option value=1 ").append(StrSelectdrop(check_type, "1")).append(">Before Trial</option>");
		str.append("<option value=2 ").append(StrSelectdrop(check_type, "2")).append(">After Trial</option>");
		return str.toString();
	}

	private void UpdateFields() {

		StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_check"
				+ " SET"
				+ " check_name = '" + check_name + "',"
				+ " check_type = '" + check_type + "'"
				+ " WHERE check_id=" + check_id;
		updateQuery(StrSql);
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT check_name, check_type "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_check "
					+ " WHERE check_id = " + check_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					check_name = crs.getString("check_name");
					check_type = crs.getString("check_type");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Checked"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
