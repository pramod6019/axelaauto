package axela.inventory;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Inventory_Location_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "", Msg1 = "";
	public String location_id = "", location_branch_id = "";
	public String location_pid = "";
	public String comp_id = "0";
	public String location_name = "", location_code = "";
	public String location_phone1 = "";
	public String location_phone2 = "";
	public String location_mobile1 = "";
	public String location_mobile2 = "";
	public String location_add = "";
	public String location_city_id = "";
	public String location_pin = "";
	public String location_entry_date = "";
	public String location_modified_date = "";
	public String location_entry_id = "";
	public String BranchAccess;
	public String entry_date = "", location_modified_id = "",
			modified_date = "";
	public String state_id = "0";
	public String emp_role_id = "";
	public String emp_id = "", location_entry_by = "",
			location_modified_by = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);

			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_item_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				location_id = CNumeric(PadQuotes(request
						.getParameter("location_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"Add Location".equals(addB)) {
						location_branch_id = "";
						location_name = "";
						location_code = "";
						location_mobile1 = "91-";
					} else {
						CheckPerm(comp_id, "emp_item_add", request, response);
						GetValues(request, response);
						location_entry_id = CNumeric(GetSession("emp_id",
								request));
						location_entry_date = ToLongDate(kknow());
						location_modified_date = "";
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("inventory-location-list.jsp?location_id="
											+ location_id
											+ "&msg=Location added successfully!"));
						}
					}
				}

				if ("yes".equals(update)) {
					if (!"Update Location".equals(updateB)
							&& !"Delete Location".equals(deleteB)) {
						PopulateFields(response);
					} else if ("Update Location".equals(updateB)
							&& !"Delete Location".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_edit", request, response);
						GetValues(request, response);
						location_entry_id = CNumeric(GetSession("emp_id",
								request));
						location_entry_date = ToLongDate(kknow());
						location_modified_id = CNumeric(GetSession("emp_id",
								request));
						location_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("inventory-location-list.jsp?location_id="
											+ location_id
											+ "&msg=Location updated successfully!"
											+ msg + ""));
						}
					} else if ("Delete Location".equals(deleteB)) {
						CheckPerm(comp_id, "emp_item_delete", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response
									.encodeRedirectURL("inventory-location-list.jsp?location_id="
											+ location_id
											+ "&msg=Location deleted successfully!"));
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
		location_branch_id = PadQuotes(request.getParameter("dr_branch"));
		location_name = PadQuotes(request.getParameter("txt_location_name"));
		location_code = PadQuotes(request.getParameter("txt_location_code"));
		location_phone1 = PadQuotes(request.getParameter("txt_location_phone1"));
		location_phone2 = PadQuotes(request.getParameter("txt_location_phone2"));
		location_mobile1 = PadQuotes(request
				.getParameter("txt_location_mobile1"));
		location_mobile2 = PadQuotes(request
				.getParameter("txt_location_mobile2"));
		location_add = PadQuotes(request.getParameter("txt_location_add"));
		state_id = PadQuotes(request.getParameter("dr_state_id"));
		location_city_id = PadQuotes(request.getParameter("dr_city_id"));
		location_pin = PadQuotes(request.getParameter("txt_location_pin"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		location_entry_by = PadQuotes(request.getParameter("location_entry_by"));
		location_modified_by = PadQuotes(request
				.getParameter("location_modified_by"));
	}

	protected void CheckForm() {
		msg = "";
		try {
			if (location_branch_id.equals("0")) {
				msg = msg + "<br>Select Branch!";
			}
			if (location_name.equals("")) {
				msg = msg + "<br>Enter Name!";
			}
			if (!location_name.equals("")) {
				StrSql = "Select location_name from " + compdb(comp_id)
						+ "axela_inventory_location "
						+ " where location_name = '" + location_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and location_id != " + location_id + "";
				}
				ResultSet rsname = processQuery(StrSql, 0);
				if (rsname.isBeforeFirst()) {
					msg = msg + "<br>Similar Location Name found!";
				}
				rsname.close();
			}

			if (location_code.equals("")) {
				msg = msg + "<br>Enter Code!";
			}
			if (!location_code.equals("")) {
				StrSql = "Select location_code from " + compdb(comp_id)
						+ "axela_inventory_location"
						+ " where location_code = '" + location_code + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " and location_id != " + location_id + "";
				}
				ResultSet rsname = processQuery(StrSql, 0);
				if (rsname.isBeforeFirst()) {
					msg = msg + "<br>Similar Location Code found!";
				}
				rsname.close();
			}

			if (!location_phone1.equals("") && !IsValidPhoneNo11(location_phone1)) {
				msg = msg + "<br>Phone1 is Invalid!";
			}
			if (!location_phone2.equals("") && !IsValidPhoneNo11(location_phone2)) {
				msg = msg + "<br>Phone2 is Invalid!";
			}

			if (location_mobile1.equals("91-")) {
				location_mobile1 = "";
			}
			if (!location_mobile1.equals("")
					&& !IsValidMobileNo11(location_mobile1)) {
				msg = msg + "<br>Mobile1 is Invalid!";
			}
			if (!location_mobile2.equals("")
					&& !IsValidMobileNo11(location_mobile2)) {
				msg = msg + "<br>Mobile2 is Invalid!";
			}

			if (location_add.equals("")) {
				msg = msg + "<br>Enter Address!";
			}
			if (state_id.equals("0")) {
				msg = msg + "<br>Select State!";
			}
			if (location_city_id.equals("0")) {
				msg = msg + "<br>Select City!";
			}
			if (location_pin.equals("")) {
				msg = msg + "<br>Enter Pin Code!";
			} else if (!location_pin.equals("") && !isNumeric(location_pin)) {
				msg = msg + "<br>Pin Code: Enter Numeric!";
			}
			if (location_add.length() > 255) {
				location_add = location_add.substring(0, 254);
			}
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
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_location"
						+ " (location_id,"
						+ " location_branch_id,"
						+ " location_name,"
						+ " location_code,"
						+ " location_address,"
						+ " location_city_id,"
						+ " location_pin,"
						+ " location_phone1,"
						+ " location_phone2,"
						+ " location_mobile1,"
						+ " location_mobile2,"
						+ " location_entry_id,"
						+ " location_entry_date,"
						+ " location_modified_id,"
						+ " location_modified_date)"
						+ " VALUES"
						+ " ((SELECT (COALESCE(MAX(location_id),0)+1) FROM " + compdb(comp_id) + "axela_inventory_location as location_id)," + " "
						+ location_branch_id + "," + " '" + location_name
						+ "'," + " '" + location_code + "'," + " '"
						+ location_add + "'," + " '" + location_city_id + "',"
						+ " '" + location_pin + "'," + " '" + location_phone1
						+ "'," + " '" + location_phone2 + "'," + " '"
						+ location_mobile1 + "'," + " '" + location_mobile2
						+ "'," + " '" + location_entry_id + "'," + " '"
						+ location_entry_date + "'," + " '0'," + " '')";
				updateQuery(StrSql);
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_stock"
						+ " ("
						+ " stock_location_id,"
						+ " stock_item_id"
						+ " )"
						+ " ("
						+ " SELECT" + " ("
						+ " SELECT" + " (" + " COALESCE (MAX(location_id), 0)"
						+ " )" + " FROM " + compdb(comp_id) + "axela_inventory_location AS location_id" + " ),"
						+ " item_id" + " FROM " + compdb(comp_id) + "axela_inventory_item)";
				// SOP("StrSql==locationstock==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select *, city_state_id" + " from " + compdb(comp_id)
					+ "axela_inventory_location" + " inner join "
					+ compdb(comp_id)
					+ "axela_branch on branch_id = location_branch_id"
					+ " inner join " + compdb(comp_id)
					+ "axela_city on city_id = location_city_id"
					+ " inner join " + compdb(comp_id)
					+ "axela_state on state_id = city_state_id"
					+ " where location_id = " + location_id + BranchAccess + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					location_branch_id = crs.getString("location_branch_id");
					location_name = crs.getString("location_name");
					location_code = crs.getString("location_code");
					location_phone1 = crs.getString("location_phone1");
					location_phone2 = crs.getString("location_phone2");
					location_mobile1 = crs.getString("location_mobile1");
					location_mobile2 = crs.getString("location_mobile2");
					location_add = crs.getString("location_address");
					state_id = crs.getString("city_state_id");
					location_city_id = crs.getString("location_city_id");
					if (state_id == null) {
						state_id = "0";
					}
					if (location_city_id == null) {
						location_city_id = "0";
					}
					location_pin = crs.getString("location_pin");
					location_entry_id = crs.getString("location_entry_id");
					if (!location_entry_id.equals("")) {
						location_entry_by = Exename(comp_id,
								Integer.parseInt(location_entry_id));
					}
					entry_date = strToLongDate(crs
							.getString("location_entry_date"));
					location_modified_id = crs.getString("location_modified_id");
					if (!location_modified_id.equals("0")) {
						location_modified_by = Exename(comp_id,
								Integer.parseInt(location_modified_id));
						modified_date = strToLongDate(crs
								.getString("location_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response
						.encodeRedirectURL("../portal/error.jsp?msg=Invalid Location!"));
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
				StrSql = "UPDATE " + compdb(comp_id)
						+ "axela_inventory_location" + " SET"
						+ " location_branch_id = " + location_branch_id + ","
						+ " location_name = '" + location_name + "',"
						+ " location_code = '" + location_code + "',"
						+ " location_address = '" + location_add + "',"
						+ " location_city_id = " + location_city_id + ","
						+ " location_pin = '" + location_pin + "',"
						+ " location_phone1 = '" + location_phone1 + "',"
						+ " location_phone2 = '" + location_phone2 + "',"
						+ " location_mobile1 = '" + location_mobile1 + "',"
						+ " location_mobile2 = '" + location_mobile2 + "',"
						+ " location_modified_id = '" + location_modified_id
						+ "'," + " location_modified_date = '"
						+ location_modified_date + "'"
						+ " where location_id = " + location_id + "";
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

		StrSql = "select voucher_id"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " where voucher_location_id = " + location_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Location is associated with Voucher!";
		}

		// Delete records
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_stock"
						+ " WHERE stock_location_id = " + location_id + "";
				updateQuery(StrSql);
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_inventory_location"
						+ " WHERE location_id = " + location_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateState() {
		try {
			StrSql = "SELECT state_id, state_name"
					+ " FROM " + compdb(comp_id) + "axela_state"
					+ " ORDER BY state_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<select name=dr_state_id id=dr_state_id class=form-control onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_city_id','city_id'); \"><option value = 0> Select </option>";
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

	public String PopulateCity() {
		try {
			StrSql = "SELECT city_id, city_name" + " from " + compdb(comp_id)
					+ "axela_city" + " left join " + compdb(comp_id)
					+ "axela_state on state_id = city_state_id"
					+ " where city_state_id = " + state_id + ""
					+ " order by city_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<select name=dr_city_id class=form-control><option value = 0>Select</option>";
			if (!state_id.equals("0")) {
				while (crs.next()) {
					group = group + "<option value=" + crs.getString("city_id")
							+ "";
					group = group
							+ StrSelectdrop(crs.getString("city_id"),
									location_city_id);
					group = group + ">" + crs.getString("city_name")
							+ "</option> \n";
				}
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
