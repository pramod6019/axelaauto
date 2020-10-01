package axela.portal;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Region_Update extends Connect {

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
	public String branch_id = "0";
	public String region_id = "0";
	public String region_name = "";
	public String QueryString = "";
	public String region_entry_id = "0";
	public String region_entry_date = "";
	public String region_modified_id = "0";
	public String region_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				region_id = CNumeric(PadQuotes(request.getParameter("region_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						region_name = "";
					} else {
						GetValues(request, response);
						region_entry_id = emp_id;
						region_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("region-list.jsp?region_id=" + region_id + "&msg= Region Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Region".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Region".equals(deleteB)) {
						GetValues(request, response);
						region_modified_id = emp_id;
						region_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("region-list.jsp?region_id=" + region_id + "&msg=Region Updated Successfully!"));
						}
					} else if ("Delete Region".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("region-list.jsp?msg=Region Deleted Successfully!"));
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
		region_name = PadQuotes(request.getParameter("txt_region_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (region_name.equals("")) {
			msg = msg + "<br>Enter Region!";
		}
		try {
			if (!region_name.equals("")) {
				StrSql = "SELECT region_name FROM " + compdb(comp_id) + "axela_branch_region WHERE region_name = '" + region_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and region_id != " + region_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Region Found! ";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				region_id = ExecuteQuery("SELECT (coalesce(max(region_id),0)+1) FROM " + compdb(comp_id) + "axela_branch_region");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_branch_region"
						+ " (region_id,"
						+ " region_name,"
						+ " region_entry_id,"
						+ " region_entry_date)"
						+ " values"
						+ " (" + region_id + ","
						+ " '" + region_name + "',"
						+ " " + region_entry_id + ","
						+ " '" + region_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_branch_region WHERE region_id = " + region_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					region_name = crs.getString("region_name");
					region_entry_id = crs.getString("region_entry_id");
					if (!region_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(region_entry_id));
						entry_date = strToLongDate(crs.getString("region_entry_date"));
					}
					region_modified_id = crs.getString("region_modified_id");
					if (!region_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(region_modified_id));
						modified_date = strToLongDate(crs.getString("region_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Region!"));
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_branch_region"
						+ " SET"
						+ " region_name = '" + region_name + "',"
						+ " region_modified_id = " + region_modified_id + ","
						+ " region_modified_date = '" + region_modified_date + "' "
						+ " WHERE region_id = " + region_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT branch_region_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_region_id = " + region_id + "";

		if (!ExecuteQuery(StrSql).equals(""))
		{
			msg = msg + "<br>Region is Associated with Branch!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_branch_region WHERE region_id =" + region_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
