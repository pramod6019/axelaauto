package axela.portal;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Executive_Access extends Connect {

	public String StrHTML = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String updateB = "";
	public String emp_name = "";
	public String values = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String emp_mis_access = "";
	public String emp_report_access = "";
	public String emp_export_access = "";
	public String emp_copy_access = "";
	public String RecCountDisplay = "";
	public String access_entry_id = "";
	public int txt_count = 0;
	public String StrSearch = "";
	public String exe_access_update = "";
	public ArrayList<String> emp_access = null;
	public static ArrayList<String> comp_mod_name_arr = new ArrayList<String>();
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "", entry_id = "", modified_id;
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				exe_access_update = PadQuotes(request.getParameter("exe_access_update"));
				if (!exe_access_update.equals("yes")) {
					CheckPerm(comp_id, "emp_role_id", request, response);
				}
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				access_entry_id = GetSession("emp_id", request);
				emp_name = ExecuteQuery("select concat(emp_name,' (',emp_ref_no,')')  from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + "");
				updateB = PadQuotes(request.getParameter("update_button"));
				if (emp_name.equals("") || emp_id.equals("1")) {
					response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?msg=Invalid Executive!"));
				} else {
					if ("Update".equals(updateB)) {
						GetValues(request, response);
						UpdateFields();
						response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?emp_id=" + emp_id + "&msg=Executive access rights updated successfully!"));
					} else {
						StrHTML = Listdata();
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
		emp_export_access = PadQuotes(request.getParameter("ch_emp_export_access"));
		emp_report_access = PadQuotes(request.getParameter("ch_emp_report_access"));
		emp_mis_access = PadQuotes(request.getParameter("ch_emp_mis_access"));
		emp_copy_access = PadQuotes(request.getParameter("ch_emp_copy_access"));
		txt_count = Integer.parseInt(PadQuotes(request.getParameter("txt_count")));
		emp_access = new ArrayList<String>();
		for (int i = 1; i <= txt_count; i++) {
			values = PadQuotes(request.getParameter("chk_access_" + i));
			if (values.equals("on")) {
				emp_access.add(PadQuotes(request.getParameter("txt_access_" + i)));
			}
		}
		if (emp_mis_access.equals("on")) {
			emp_mis_access = "1";
		} else {
			emp_mis_access = "0";
		}
		if (emp_export_access.equals("on")) {
			emp_export_access = "1";
		} else {
			emp_export_access = "0";
		}
		if (emp_report_access.equals("on")) {
			emp_report_access = "1";
		} else {
			emp_report_access = "0";
		}
		if (emp_copy_access.equals("on")) {
			emp_copy_access = "1";
		} else {
			emp_copy_access = "0";
		}
	}

	public String Listdata() {

		String oldSubmodule_name = "", access_name = "", submodule_name = "";
		String colHead = "";
		String modSearch = "";
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT emp_export_access, emp_report_access, emp_mis_access, emp_copy_access, emp_entry_id,"
				+ " emp_entry_date, emp_modified_id, emp_modified_date"
				+ " FROM " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id=" + emp_id;
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				emp_export_access = crs.getString("emp_export_access");
				emp_report_access = crs.getString("emp_report_access");
				// SOP("emp_report_access--------" + emp_report_access);
				emp_mis_access = crs.getString("emp_mis_access");
				emp_copy_access = crs.getString("emp_copy_access");
				entry_id = crs.getString("emp_entry_id");
				if (!entry_id.equals("0")) {
					entry_by = Exename(comp_id, Integer.parseInt(entry_id));
					entry_date = strToLongDate(crs.getString("emp_entry_date"));
				}
				modified_id = crs.getString("emp_modified_id");
				if (!modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(modified_id));
					modified_date = strToLongDate(crs.getString("emp_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		/*
		 * ====For getting the module names======================================
		 */
		StrSql = "SELECT CONCAT('comp_module_',module_name) AS module_name"
				+ " FROM " + maindb() + "module";
		// SOP("Module====" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append(crs.getString("module_name")).append(", ");
					comp_mod_name_arr.add(crs.getString("module_name"));
				}
				StrSearch = Str.toString();
				StrSearch = StrSearch.substring(0, StrSearch.lastIndexOf(","));
			}
			crs.close();
			/*
			 * ====End of module names======================================================
			 */

			StrSql = "SELECT " + StrSearch + " FROM " + compdb(comp_id) + "axela_comp";
			crs = processQuery(StrSql, 0);
			int mod_name_arr_count = comp_mod_name_arr.size();
			if (crs.isBeforeFirst()) {
				Str = new StringBuilder();
				Str.append(" and (");
				while (crs.next()) {
					for (int i = 0; i < mod_name_arr_count; i++) {
						if (crs.getString(comp_mod_name_arr.get(i)).equals("1")) {
							String mod_name[] = comp_mod_name_arr.get(i).split("_");
							Str.append(" module_name='").append(mod_name[2].toString().toLowerCase());
							Str.append("' or ");
						}
					}
				}
				modSearch = Str.toString();
				modSearch = modSearch.substring(0, modSearch.lastIndexOf("or")) + " )";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		StrSql = "SELECT access_id, access_module_id, module_name, access_name, access_rank,"
				+ " COALESCE(empaccess_access_id,0) AS empaccess_access_id"
				+ " FROM " + maindb() + "module_access "
				+ " INNER JOIN " + maindb() + "module on module_id=access_module_id  "
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_access ON empaccess_access_id=access_id AND empaccess_emp_id = " + emp_id
				+ " WHERE 1=1 "
				+ modSearch
				+ " ORDER BY module_rank, access_rank ";
		int i = 0;
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			Str = new StringBuilder();
			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				while (crs.next()) {
					i++;
					String check = "";
					SOP("submodule_name=11==" + submodule_name);
					access_name = crs.getString("access_name");
					if (!colHead.equals(crs.getString("module_name"))) {
						Str.append("<tr><thead><th colspan=2 align=center><strong>").append(crs.getString("module_name")).append("</strong></th></tr></thead>");
						colHead = crs.getString("module_name");
					}
					SOP("submodule_name==22=" + submodule_name);
					submodule_name = access_name.substring(access_name.indexOf("_") + 1, access_name.lastIndexOf("_"));
					if (!submodule_name.equals(oldSubmodule_name)) {
						Str.append("<tr><td align=right width=20%>").append(toTitleCase(submodule_name.replace("_", " ")).trim());
						Str.append(":</td><td>");
					}
					SOP("submodule_name==33=" + submodule_name);
					if (crs.getString("empaccess_access_id").equals(crs.getString("access_id"))) {
						check = "checked";
					}
					Str.append("<input type=hidden name=txt_access_").append(i).append(" id=txt_access_");
					Str.append(i).append(" value=").append(crs.getString("access_id")).append(" />\n");
					Str.append("<div id=\"display-line\" nowrap><input type=checkbox name=chk_access_").append(i).append(" ").append(check);
					Str.append(" id=chk_access_").append(i).append(" >\n");
					Str.append(toTitleCase(access_name.substring(access_name.lastIndexOf("_") + 1)).trim());
					SOP("submodule_name==44=" + submodule_name);
					Str.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>");
					oldSubmodule_name = submodule_name;
					SOP("submodule_name==55=" + submodule_name);
				}
				Str.append("<input type=hidden name=txt_count").append(" id=txt_count").append(" value=");
				Str.append(i).append(">");
				Str.append("</table><br>");
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red>No Module(s) found!</font><br><br>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

		return Str.toString();
	}

	protected void UpdateFields() throws SQLException {
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET "
					+ "emp_export_access = '" + emp_export_access + "', "
					+ "emp_report_access = '" + emp_report_access + "', "
					+ "emp_mis_access = " + emp_mis_access + ", "
					+ "emp_copy_access = '" + emp_copy_access + "',"
					+ " emp_modified_id = " + access_entry_id + ","
					+ " emp_modified_date = '" + ToLongDate(kknow()) + "'"
					+ "WHERE emp_id =" + emp_id;
			stmttx.addBatch(StrSql);
			StrSql = "Delete from " + compdb(comp_id) + "axela_emp_access where empaccess_emp_id =" + emp_id;
			stmttx.addBatch(StrSql);
			for (int i = 0; i < emp_access.size(); i++) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_access"
						+ "(empaccess_emp_id , "
						+ " empaccess_access_id"
						+ ")"
						+ " VALUES"
						+ "(" + emp_id + ", "
						+ "" + emp_access.get(i) + ") ";
				stmttx.addBatch(StrSql);
			}
			stmttx.executeBatch();
			conntx.commit();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...\n sql--" + StrSql);
			}
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		} finally {
			conntx.setAutoCommit(true);
			if (stmttx != null && !stmttx.isClosed()) {
				stmttx.close();
			}
			if (conntx != null && !conntx.isClosed()) {
				conntx.close();
			}
		}
	}
}
