package axela.accounting;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Group_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public String StrHTML = "";
	public static String msg = "";
	public String accgroup_id = "0";
	public String accsubgroup_id = "0";
	public String accsubgroup_accgroup_id = "0";
	public String accgroup_parent_id = "0";// , accgroup_subgroup_id;
	public String QueryString = "";
	public String accgroup_name = "";
	// public String parent = "", subgroup = "";
	public String accgroup_affects_gross = "";
	public String emp_id = "0";
	public String comp_id = "0", accgroup_type = "";
	public String accgroup_alie_id = "0", descreption = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String accgroup_entry_id = "0";
	public String accgroup_entry_date = "";
	public String accgroup_modified_id = "0";
	public String accgroup_modified_date = "";
	public String access = "";
	public String entry_by = "";
	public String entry_date = "";
	public String accgroupid = "0";
	public String modified_by = "";
	public String modified_date = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public int mapkey = 0;
	public String group_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id,emp_acc_group_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				accgroup_id = CNumeric(PadQuotes(request.getParameter("accgroup_id")));
				QueryString = PadQuotes(request.getQueryString());
				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						accgroup_entry_id = emp_id;
						accgroup_entry_date = ToLongDate(kknow());
						if (ReturnPerm(comp_id, "emp_role_id,emp_acc_group_add", request).equals("1")) {
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Group added successfully!";
							}
						} else {
							msg = "Error<br>Access Denied!";
							// response.encodeRedirectURL("../portal/error.jsp?msg=Access Denied!");
						}
					}
				} else {
					if ("yes".equals(update)) {
						status = "Update";
						if (!"yes".equals(updateB) && !"Delete Group".equals(deleteB)) {
							PopulateFields(response);
						} else if ("yes".equals(updateB) && !"Delete Group".equals(deleteB)) {
							GetValues(request, response);
							accgroup_modified_id = emp_id;
							accgroup_modified_date = ToLongDate(kknow());
							if (ReturnPerm(comp_id, "emp_role_id,emp_acc_group_edit", request).equals("1")) {
								UpdateFields();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									msg = "Group updated successfully!";
								}
							} else {
								msg = "Error<br>Access Denied!";
							}
						} else if ("Delete Group".equals(deleteB)) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_role_id,emp_acc_group_delete", request).equals("1")) {
								DeleteFields();
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									msg = "Group deleted Successfully!";
								}
							} else {
								msg = "Error<br>Access Denied!";
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError(" AxelaCRM===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		accgroup_name = PadQuotes(request.getParameter("txt_group_name"));
		accgroup_parent_id = CNumeric(PadQuotes(request.getParameter("dr_accgroup_parent_id")));
		accgroup_type = CNumeric(PadQuotes(ExecuteQuery("SELECT accgrouppop_alie FROM " + compdb(comp_id) + "axela_acc_group_pop"
				+ " WHERE accgrouppop_id = " + accgroup_parent_id)));
		descreption = PadQuotes(request.getParameter("txt_descreption"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (accgroup_parent_id.equals("0")) {
			msg = msg + "<br>Select Parent Group!";
		}

		if (accgroup_name.equals("")) {
			msg += "<br>Enter Group!";
		}

		if (add.equals("yes")) {
			StrSql = "SELECT accgroup_id FROM " + compdb(comp_id) + "axela_acc_group"
					+ " WHERE accgroup_name = '" + accgroup_name + "'"
					+ " AND accgroup_parent_id = " + accgroup_parent_id
					+ " AND accgroup_alie = " + accgroup_type;

			if (!CNumeric(PadQuotes(ExecuteQuery(StrSql))).equals("0")) {
				msg += "<br>Group Already Exists!";
			}
		}

		if (update.equals("yes")) {
			if (accgroup_id.equals(accgroup_parent_id)) {
				msg += "<br>Group and Sub-Group can't be same!";
			}
		}

	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				stmttx = conntx.createStatement();
				StrSql = "INSERT INTO " + compdb(comp_id) + " axela_acc_group"
						+ " ( accgroup_parent_id,"
						+ " accgroup_name,"
						+ " accgroup_alie,"
						+ " accgroup_notes,"
						+ " accgroup_entry_id,"
						+ " accgroup_entry_date)"
						+ " VALUES"
						+ "(" + accgroup_parent_id + ","
						+ " '" + accgroup_name + "',"
						+ " " + accgroup_type + ","
						+ " '" + descreption + "',"
						+ " " + accgroup_entry_id + ","
						+ " '" + accgroup_entry_date + "')";
				// SOP("StrSql====insert1====" + StrSql);
				// updateQuery(StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					group_id = rs1.getString(1);
				}
				rs1.close();
				SOP("group_id===" + group_id);
				if (!accgroup_parent_id.equals("0") && !group_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
							+ " SET customer_accgroup_id = " + group_id
							+ " WHERE customer_accgroup_id = " + accgroup_parent_id;
					updateQuery(StrSql);
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_group_pop";
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_group_pop"
						+ " (accgrouppop_id,"
						+ " accgrouppop_name,"
						+ " accgrouppop_rank,"
						+ " accgrouppop_alie"
						+ ")"
						+ " VALUES " + UpdateGroupPop("0");
				StrSql = StrSql.substring(0, StrSql.length() - 1);
				// SOP("StrSql====insert2====" + StrSql);
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError(" AxelaCRM===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public String UpdateGroupPop(String p_id) {
		String Str = "", groupname = "";
		int rank = 0;
		try {
			StrSql = "SELECT accgroup_id, accgroup_name, accgroup_parent_id, accgroup_alie"
					+ " FROM " + compdb(comp_id) + "axela_acc_group"
					+ " where accgroup_parent_id = " + p_id + ""
					+ " order by accgroup_name";
			CachedRowSet rs = processQuery(StrSql, 0);

			while (rs.next()) {
				rank++;
				if (CNumeric(PadQuotes(rs.getString("accgroup_parent_id"))).equals("0")) {
					groupname = rs.getString("accgroup_name");
				} else {
					groupname = PopulateGroupStr(rs.getString("accgroup_id"), comp_id);
				}
				p_id = rs.getString("accgroup_id");
				Str += "(" + rs.getString("accgroup_id") + ", '" + groupname + "', " + rank + ", " + rs.getString("accgroup_alie") + "),";
				Str += UpdateGroupPop(p_id);
			}
			rs.close();
			return Str;
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT accgroup_id, accgroup_name,"
					+ " IF ( accgroup_alie = 1, 'Assets',"
					+ " IF ( accgroup_alie = 2, 'Liabilities',"
					+ " IF ( accgroup_alie = 3, 'Income',"
					+ " IF ( accgroup_alie = 4, 'Expense',"
					+ " IF ( accgroup_alie = 5, 'Owners Equity',"
					+ " '' ) ) ) ) ) AS accgroup_alie_name, accgroup_alie,"
					+ " accgroup_notes, accgroup_parent_id,"
					+ " accgroup_entry_id, accgroup_entry_date, accgroup_modified_id,"
					+ " accgroup_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_acc_group"
					+ " WHERE accgroup_id =" + accgroup_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					accgroup_id = crs.getString("accgroup_id");
					accgroup_name = crs.getString("accgroup_name");
					accgroup_parent_id = crs.getString("accgroup_parent_id");
					accgroup_type = crs.getString("accgroup_alie_name");
					accgroup_alie_id = CNumeric(crs.getString("accgroup_alie"));
					descreption = unescapehtml(crs.getString("accgroup_notes"));
					accgroup_entry_id = crs.getString("accgroup_entry_id");
					if (!accgroup_entry_id.equals("0")) {
						entry_by = Exename(comp_id, crs.getInt("accgroup_entry_id"));
						entry_date = strToLongDate(crs.getString("accgroup_entry_date"));
					}
					accgroup_modified_id = crs.getString("accgroup_modified_id");
					if (!accgroup_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(accgroup_modified_id));
						modified_date = strToLongDate(crs.getString("accgroup_modified_date"));
					}
				}

			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Group!"));
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {
			SOPError(" AxelaCRM===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE  " + compdb(comp_id) + " axela_acc_group"
					+ " SET"
					+ " accgroup_parent_id = " + accgroup_parent_id + ","
					+ " accgroup_name = '" + accgroup_name + "',"
					+ " accgroup_notes = '" + descreption + "',"
					+ " accgroup_modified_id = " + accgroup_modified_id + ","
					+ " accgroup_modified_date = '" + accgroup_modified_date + "'"
					+ " WHERE accgroup_id = " + accgroup_id + "";
			// SOP("Update===" + StrSql);
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_group_pop";
			updateQuery(StrSql);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_group_pop"
					+ " (accgrouppop_id,"
					+ " accgrouppop_name,"
					+ " accgrouppop_rank,"
					+ " accgrouppop_alie"
					+ ")"
					+ " VALUES " + UpdateGroupPop("0");
			StrSql = StrSql.substring(0, StrSql.length() - 1);
			// SOP("StrSql====insert2====" + StrSql);
			updateQuery(StrSql);

		}
	}

	protected void DeleteFields() {
		if (accgroup_id.equals("1")) {
			msg = "<br>Cannot Delete First Record!";
			return;
		}

		// Category association
		StrSql = "SELECT accgroup_id FROM " + compdb(comp_id) + "axela_acc_group"
				+ " WHERE accgroup_parent_id = " + accgroup_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Group is associated with Sub Group(s)!";
		}

		StrSql = "SELECT accgroup_id FROM " + compdb(comp_id) + "axela_acc_group"
				+ " WHERE accgroup_parent_id = 0"
				+ " AND accgroup_id = " + accgroup_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Main Group Cannot Be Deleted!";
		}

		StrSql = "SELECT customer_accgroup_id FROM " + compdb(comp_id) + "axela_customer"
				+ " WHERE customer_accgroup_id = " + accgroup_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Group is Associated With Ledger(s)!";
		}

		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_group"
					+ " WHERE accgroup_id = " + accgroup_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_group_pop"
					+ " WHERE accgrouppop_id = " + accgroup_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateGroup(String dr_group) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=1").append(StrSelectdrop("1", dr_group)).append(">Assets</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", dr_group)).append(">Liabilities</option>\n");
		Str.append("<option value=3").append(StrSelectdrop("3", dr_group)).append(">Revenue</option>\n");
		Str.append("<option value=4").append(StrSelectdrop("4", dr_group)).append(">Expense</option>\n");
		Str.append("<option value=5").append(StrSelectdrop("5", dr_group)).append(">Owners Equity</option>\n");
		return Str.toString();
	}

	public String PopulateParentGroup(String comp_id, String onlyparrent, String accgroup_parent_id, String accgroup_alie) {
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		try {
			StrSql = "SELECT accgrouppop_id, accgrouppop_name"
					+ " FROM " + compdb(comp_id) + "axela_acc_group_pop"
					// + " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
					+ " WHERE 1 = 1";

			if (!accgroup_alie_id.equals("0")) {
				StrSql += " AND accgrouppop_alie = " + accgroup_alie_id;
			}

			if (!accgroup_id.equals("0")) {
				StrSql += " AND accgrouppop_id != " + accgroup_id + ""
						+ " ";
			}
			StrSql += " ORDER BY accgrouppop_name";
			// SOP("StrSql====" + StrSql);

			CachedRowSet rs = processQuery(StrSql, 0);
			Str.append("<option value=0 >Select Parent</option>");
			while (rs.next()) {

				Str.append("<option value=").append(rs.getString("accgrouppop_id"));
				Str.append(" " + StrSelectdrop(rs.getString("accgrouppop_id"), accgroup_parent_id));
				Str.append(" >").append(rs.getString("accgrouppop_name")).append(":</option>\n");
			}
			rs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

}
