package axela.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//@saiman 21st june 2012
public class Customer_Group_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String msg = "";
	public String group_id = "";
	public String group_desc = "";
	public String group_entry_id = "", entry_by = "";
	public String group_entry_date = "", entry_date = "";
	public String group_modified_id = "", modified_by = "";
	public String group_modified_date = "", modified_date = "";
	public String emp_id = "", branch_id = "", BranchAccess = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_customer_access", request, response);
			HttpSession session = request.getSession(true);
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
				group_id = PadQuotes(request.getParameter("group_id"));
				group_id = CNumeric(group_id);

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Group".equals(addB)) {
						group_desc = "";
						group_entry_id = "";
						group_entry_date = "";
						group_modified_id = "";
						group_modified_date = "";
					} else {
						CheckPerm(comp_id, "emp_customer_add", request, response);
						GetValues(request, response);
						group_entry_id = emp_id;
						group_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("customer-group-list.jsp?msg=Group added successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"Update Group".equals(updateB) && !"Delete Group".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Group".equals(updateB) && !"Delete Group".equals(deleteB)) {
						CheckPerm(comp_id, "emp_customer_edit", request, response);
						GetValues(request, response);
						group_modified_id = emp_id;
						group_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("customer-group-list.jsp?group_id=" + group_id + "&msg=Group updated successfully!" + msg + ""));
						}
					} else if ("Delete Group".equals(deleteB)) {
						CheckPerm(comp_id, "emp_customer_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("customer-group-list.jsp?group_id=" + group_id + "&msg=Group deleted successfully!"));
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		group_desc = PadQuotes(request.getParameter("txt_group_desc"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (group_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		try {
			StrSql = "select group_desc from " + compdb(comp_id) + "axela_customer_group "
					+ " where 1=1"
					+ " and group_desc = '" + group_desc + "'";
			if ("yes".equals(update)) {
				StrSql = StrSql + " and group_id!=" + CNumeric(group_id) + "";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				msg = msg + "<br>Similar Description found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "insert into " + compdb(comp_id) + "axela_customer_group"
						+ "(group_id , "
						+ "group_desc,"
						+ "group_entry_id,"
						+ "group_entry_date,"
						+ "group_modified_id,"
						+ "group_modified_date) "
						+ "values "
						+ " ((Select (coalesce(max(group_id),0)+1) from " + compdb(comp_id) + "axela_customer_group as group_id),"
						+ "'" + group_desc + "',"
						+ "'" + group_entry_id + "',"
						+ "'" + group_entry_date + "',"
						+ "'0',"
						+ "'')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {

		try {
			StrSql = "select group_id, group_desc, "
					+ "group_entry_id, group_entry_date, group_modified_id, group_modified_date "
					+ "from " + compdb(comp_id) + "axela_customer_group "
					+ "where  group_id=" + group_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				group_id = crs.getString("group_id");
				group_desc = crs.getString("group_desc");
				group_entry_id = crs.getString("group_entry_id");
				group_entry_date = crs.getString("group_entry_date");
				group_modified_id = crs.getString("group_modified_id");
				group_modified_date = crs.getString("group_modified_date");
				if (!group_entry_id.equals("")) {
					entry_by = Exename(comp_id, Integer.parseInt(group_entry_id));
					SOP("entry_by====" + entry_by);
					entry_date = strToLongDate(crs.getString("group_entry_date"));
				}
				if (!group_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(group_modified_id));
					modified_date = strToLongDate(crs.getString("group_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();

		if (msg.equals("")) {
			try {
				StrSql = " UPDATE " + compdb(comp_id) + "axela_customer_group SET "
						+ " group_desc  ='" + group_desc + "',"
						+ " group_modified_id  ='" + group_modified_id + "',"
						+ " group_modified_date  ='" + group_modified_date + "' "
						+ " where  group_id = " + group_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "SELECT trans_group_id from " + compdb(comp_id) + "axela_customer_group_trans where and trans_group_id = " + group_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Group is associated with a Contact!";
			}
			if (msg.equals("")) {
				StrSql = "Delete from " + compdb(comp_id) + "axela_customer_group where  group_id =" + group_id + "";
				updateQuery(StrSql);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
