package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCity_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String state_id = "0";
	public String city_id = "0";
	public String city_name = "";
	public String user_id = "0";
	public String stateid = "0";
	public String emp_id = "";
	public String comp_id = "0";
	public String QueryString = "";
	public String city_entry_id = "0";
	public String city_entry_date = "";
	public String city_modified_id = "0";
	public String city_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id, emp_service_jobcard_add", request, response);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				city_id = CNumeric(PadQuotes(request.getParameter("city_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						city_name = "";
					} else {
						GetValues(request, response);
						city_entry_id = emp_id;
						city_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("managecity.jsp?city_id="
											+ city_id
											+ "&stateid="
											+ state_id
											+ "&city_name="
											+ city_name
											+ "&msg=City Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB)
							&& !"Delete City".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB)
							&& !"Delete City".equals(deleteB)) {
						GetValues(request, response);
						city_modified_id = emp_id;
						city_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("managecity.jsp?Update=yes&city_id="
											+ city_id
											+ "&msg=City Updated Successfully!"));
						}
					} else if ("Delete City".equals(deleteB)) {

						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("managecity.jsp?msg=City Deleted Successfully!"));
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		state_id = PadQuotes(request.getParameter("dr_state_id"));
		city_name = PadQuotes(request.getParameter("txt_city_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (state_id.equals("0")) {
			msg = msg + "<br>Select State!";
		}
		if (city_name.equals("")) {
			msg = msg + "<br>Enter City!";
		}
		try {
			if (update.equals("yes") && !city_name.equals("")) {
				StrSql = "Select city_name from " + compdb(comp_id)
						+ "axela_city WHERE city_name = '" + city_name
						+ "' and city_state_id = " + state_id
						+ " and city_id != " + city_id + "";
			} else if (add.equals("yes") && !city_name.equals("")) {
				StrSql = "Select city_name from " + compdb(comp_id)
						+ "axela_city WHERE city_state_id = " + state_id
						+ " and city_name = '" + city_name + "'";
			}
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				msg = msg + "<br>Similar City Name Found!";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "Insert into " + compdb(comp_id) + "axela_city"
						+ " (city_name," + " city_state_id ,"
						+ " city_entry_id," + " city_entry_date)" + " values"
						+ " (" + " '" + city_name + "'," + " " + state_id + ","
						+ " " + city_entry_id + "," + " '" + city_entry_date
						+ "')";
				city_id = UpdateQueryReturnID(StrSql);
				// updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			int city_idi = Integer.parseInt(city_id);
			StrSql = "Select * from " + compdb(comp_id) + "axela_city"
					+ " left join " + compdb(comp_id)
					+ "axela_state on city_state_id = state_id"
					+ " WHERE city_id = '" + city_idi + "'";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					city_id = crs.getString("city_id");
					city_name = crs.getString("city_name");
					state_id = crs.getString("city_state_id");
					city_entry_id = crs.getString("city_entry_id");
					if (!city_entry_id.equals("0")) {
						entry_by = Exename(comp_id,
								Integer.parseInt(city_entry_id));
						entry_date = strToLongDate(crs
								.getString("city_entry_date"));
					}
					city_modified_id = crs.getString("city_modified_id");
					if (!city_modified_id.equals("0")) {
						modified_by = Exename(comp_id,
								Integer.parseInt(city_modified_id));
						modified_date = strToLongDate(crs
								.getString("city_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response
						.encodeRedirectURL("../portal/error.jsp?msg=Invalid City!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_city" + " SET"
						+ " city_name = '" + city_name + "',"
						+ " city_state_id = '" + state_id + "',"
						+ " city_modified_id=" + city_modified_id + ","
						+ " city_modified_date='" + city_modified_date + "'"
						+ " WHERE city_id = " + city_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT branch_id FROM " + compdb(comp_id)
				+ "axela_branch WHERE branch_city_id = " + city_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>City is Associated with a Branch!";
		}

		StrSql = "SELECT franchisee_id FROM " + compdb(comp_id)
				+ "axela_franchisee WHERE franchisee_city_id = " + city_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>City is Associated with a Franchisee!";
		}

		StrSql = "SELECT customer_id FROM " + compdb(comp_id)
				+ "axela_customer WHERE customer_city_id = " + city_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>City is Associated with Customer!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE from " + compdb(comp_id)
						+ "axela_city WHERE city_id = " + city_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	public String PopulateState() {
		try {
			if (state_id.equals("")) {
				state_id = stateid;
			}
			String group = "<select name=dr_state_id class=form-control><option value=0>Select</option>";
			StrSql = "SELECT state_id, state_name FROM " + compdb(comp_id)
					+ "axela_state";
			StrSql = StrSql + " order by state_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("state_id")
						+ "";
				group = group
						+ StrSelectdrop(crs.getString("state_id"), state_id);
				group = group + ">" + crs.getString("state_name")
						+ "</option> \n";
			}
			group = group + "</select>";
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}
}
