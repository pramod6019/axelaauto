package axela.portal;
//Murali 21st jun

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Zone_Update extends Connect {

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
	public String zone_id = "0";
	public String zone_name = "";
	public String QueryString = "";
	public String zone_entry_id = "0";
	public String zone_entry_date = "";
	public String zone_modified_id = "0";
	public String zone_modified_date = "";
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
				zone_id = CNumeric(PadQuotes(request.getParameter("zone_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						zone_name = "";
					} else {
						GetValues(request, response);
						zone_entry_id = emp_id;
						zone_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("zone-list.jsp?zone_id=" + zone_id + "&msg= Zone Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Zone".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Zone".equals(deleteB)) {
						GetValues(request, response);
						zone_modified_id = emp_id;
						zone_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("zone-list.jsp?zone_id=" +zone_id + "&msg=Zone Updated Successfully!"));
						}
					} else if ("Delete Zone".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("zone-list.jsp?msg=Zone Deleted Successfully!"));
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
		zone_name = PadQuotes(request.getParameter("txt_zone_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (zone_name.equals("")) {
			msg = msg + "<br>Enter Region!";
		}
		try {
			if (!zone_name.equals("")) {
				StrSql = "SELECT zone_name FROM " + compdb(comp_id) + "axela_branch_zone WHERE zone_name = '" + zone_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and zone_id != " + zone_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Zone Found! ";
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
				zone_id = ExecuteQuery("SELECT (coalesce(max(zone_id),0)+1) FROM " + compdb(comp_id) + "axela_branch_zone");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_branch_zone"
						+ " (zone_id,"
						+ " zone_name,"
						+ " zone_entry_id,"
						+ " zone_entry_date)"
						+ " values"
						+ " (" + zone_id + ","
						+ " '" + zone_name + "',"
						+ " " + zone_entry_id + ","
						+ " '" + zone_entry_date + "')";
				SOP("StrSql=="+StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_branch_zone WHERE zone_id = " + zone_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					zone_name = crs.getString("zone_name");
					zone_entry_id = crs.getString("zone_entry_id");
					if (!zone_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(zone_entry_id));
						entry_date = strToLongDate(crs.getString("zone_entry_date"));
					}
					zone_modified_id = crs.getString("zone_modified_id");
					if (!zone_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(zone_modified_id));
						modified_date = strToLongDate(crs.getString("zone_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Zone!"));
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
				StrSql = " UPDATE " + compdb(comp_id) + "axela_branch_zone"
						+ " SET"
						+ " zone_name = '" + zone_name + "',"
						+ " zone_modified_id = " + zone_modified_id + ","
						+ " zone_modified_date = '" + zone_modified_date + "' "
						+ " WHERE zone_id = " + zone_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT branch_zone_id FROM " + compdb(comp_id) + "axela_branch WHERE branch_zone_id = " + zone_id + "";

		if (!ExecuteQuery(StrSql).equals(""))
		{
			msg = msg + "<br>Zone is Associated with Branch!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_branch_zone WHERE zone_id =" + zone_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
