package axela.portal;

/** @saiman 20 june 2012 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Faqexecutivecat_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String cat_id = "0";
	public String cat_name = "";
	public String cat_comp_id = "0";
	public String cat_entry_id = "0";
	public String entry_by = "";
	public String cat_entry_date = "";
	public String entry_date = "";
	public String cat_modified_id = "0";
	public String modified_by = "";
	public String cat_modified_date = "";
	public String modified_date = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_faq_access", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				cat_id = CNumeric(PadQuotes(request.getParameter("cat_id")));
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
						CheckPerm(comp_id, "emp_faq_add", request, response);
						GetValues(request, response);
						cat_entry_id = emp_id;
						cat_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("faqexecutivecat-list.jsp?msg=Category added successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"Update Category".equals(updateB) && !"Delete Category".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Category".equals(updateB) && !"Delete Category".equals(deleteB)) {
						CheckPerm(comp_id, "emp_faq_edit", request, response);
						GetValues(request, response);
						cat_modified_id = emp_id;
						cat_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("faqexecutivecat-list.jsp?cat_id=" + cat_id + "&msg=Category updated successfully!" + msg + ""));
						}
					} else if ("Delete Category".equals(deleteB)) {
						CheckPerm(comp_id, "emp_faq_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("faqexecutivecat-list.jsp?msg=Category deleted successfully!"));
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
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (cat_name.equals("")) {
			msg = msg + "<br>Enter  Name!";
		}
		try {
			StrSql = "select cat_name from " + compdb(comp_id) + "axela_faq_cat where 1=1 "
					+ " and cat_name = '" + cat_name + "'";
			if (update.equals("yes")) {
				StrSql = StrSql + " and cat_id!=" + CNumeric(cat_id) + "";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				msg = msg + "<br>Similar Name found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "insert into " + compdb(comp_id) + "axela_faq_cat"
						+ "(cat_id , "
						+ "cat_name,"
						+ "cat_entry_id,"
						+ "cat_entry_date,"
						+ "cat_modified_id,"
						+ "cat_modified_date) "
						+ "values "
						+ "((Select (coalesce(max(cat_id),0)+1) from " + compdb(comp_id) + "axela_faq_cat as category_id),"
						+ "'" + cat_name + "',"
						+ "'" + cat_entry_id + "',"
						+ "'" + cat_entry_date + "',"
						+ "'0',"
						+ "'')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "select cat_id, cat_name, "
					+ "cat_entry_id, cat_entry_date, cat_modified_id, cat_modified_date "
					+ "from " + compdb(comp_id) + "axela_faq_cat"
					+ " where cat_id=" + cat_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				cat_id = crs.getString("cat_id");
				cat_name = crs.getString("cat_name");
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_faq_cat SET "
						+ " cat_name  ='" + cat_name + "',"
						+ " cat_modified_id  ='" + cat_modified_id + "',"
						+ " cat_modified_date  ='" + cat_modified_date + "' "
						+ " where cat_id = " + cat_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT faq_cat_id from " + compdb(comp_id) + "axela_faq where faq_cat_id = " + cat_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Category is associated with a FAQ!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_faq_cat where cat_id =" + cat_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
