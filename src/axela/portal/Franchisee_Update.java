package axela.portal;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Franchisee_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String franchisee_id = "0";
	public String franchisee_name = "";
	public String franchisee_franchiseetype_id = "0";
	public String franchisee_contact = "";
	public String franchisee_phone1 = "";
	public String franchisee_phone2 = "";
	public String franchisee_mobile1 = "";
	public String franchisee_mobile2 = "";
	public String franchisee_email1 = "";
	public String franchisee_email2 = "";
	public String franchisee_url = "";
	public String franchisee_add = "";
	public String franchisee_city_id = "";
	// public String state_id = "0";
	public String franchisee_pin = "";
	public String franchisee_active = "1";
	public String franchisee_notes = "";
	public String franchisee_entry_id = "0";
	public String franchisee_entry_date = "";
	public String franchisee_modified_id = "0";
	public String franchisee_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String franchisee_entry_by = "";
	public String franchisee_modified_by = "";
	public String QueryString = "";
	// Get the franchisee count from Connect class
	public int franchisee_count = franchiseecount;
	public int active_franchiseecount = 0;
	public String active = "";
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				franchisee_id = CNumeric(PadQuotes(request.getParameter("franchisee_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						franchisee_mobile1 = "91-";
						franchisee_name = "";
					} else {
						CheckPerm(comp_id, "emp_role_id", request, response);
						GetValues(request, response);
						franchisee_entry_id = CNumeric(GetSession("emp_id", request));
						franchisee_entry_date = ToLongDate(kknow());
						franchisee_modified_date = "";
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("franchisee-list.jsp?franchisee_id=" + franchisee_id + "&msg=Franchisee Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					active = ExecuteQuery("Select branch_active from " + compdb(comp_id) + "axela_branch"
							+ " where branch_id = " + branch_id + "");
					if (!"yes".equals(updateB) && !"Delete Franchisee".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Franchisee".equals(deleteB)) {
						CheckPerm(comp_id, "emp_role_id", request, response);
						GetValues(request, response);
						franchisee_modified_id = CNumeric(GetSession("emp_id", request));
						franchisee_modified_date = ToLongDate(kknow());
						franchisee_entry_id = CNumeric(GetSession("emp_id", request));
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("franchisee-list.jsp?franchisee_id=" + franchisee_id + "&msg=Franchisee Updated Successfully!"));
						}
					} else if ("Delete Franchisee".equals(deleteB)) {
						CheckPerm(comp_id, "emp_role_id", request, response);
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("franchisee-list.jsp?all=yes&msg=Franchisee Deleted Successfully!"));
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
		franchisee_name = PadQuotes(request.getParameter("txt_franchisee_name"));
		franchisee_franchiseetype_id = PadQuotes(request.getParameter("drop_franchisee_franchiseetype_id"));
		franchisee_contact = PadQuotes(request.getParameter("txt_franchisee_contact"));
		franchisee_phone1 = PadQuotes(request.getParameter("txt_franchisee_phone1"));
		franchisee_phone2 = PadQuotes(request.getParameter("txt_franchisee_phone2"));
		franchisee_mobile1 = PadQuotes(request.getParameter("txt_franchisee_mobile1"));
		franchisee_mobile2 = PadQuotes(request.getParameter("txt_franchisee_mobile2"));
		franchisee_email1 = PadQuotes(request.getParameter("txt_franchisee_email1"));
		franchisee_email2 = PadQuotes(request.getParameter("txt_franchisee_email2"));
		franchisee_url = PadQuotes(request.getParameter("txt_franchisee_url"));
		franchisee_add = PadQuotes(request.getParameter("txt_franchisee_add"));
		franchisee_pin = PadQuotes(request.getParameter("txt_franchisee_pin"));
		franchisee_notes = PadQuotes(request.getParameter("txt_franchisee_notes"));
		franchisee_active = PadQuotes(request.getParameter("ch_franchisee_active"));
		// state_id = PadQuotes(request.getParameter("dr_state_id"));
		franchisee_city_id = PadQuotes(request.getParameter("maincity"));
		CheckedFields();
		franchisee_entry_by = PadQuotes(request.getParameter("franchisee_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		franchisee_modified_by = PadQuotes(request.getParameter("franchisee_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckedFields() {
		if (franchisee_active.equals("on")) {
			franchisee_active = "1";
		} else {
			franchisee_active = "0";
		}
	}

	protected void CheckForm() {
		String Msg1 = "";
		PopulateConfigDetails();
		if ("Add Franchisee".equals(addB)) {
			if (active_franchiseecount >= franchisee_count) {
				msg = msg + "<br>Maximum Franchisee Count Reached!";
			}
		}
		if ("Update Franchisee".equals(updateB)) {
			// SOP(active + " active");
			// SOP(franchisee_active + " franchisee_active");
			if (active.equals("0") && franchisee_active.equals("1")
					&& franchisee_count <= active_franchiseecount) {
				msg = msg + "<br>Maximum Franchisee count reached!";
			}
		}
		if (franchisee_name.equals("")) {
			msg = msg + "<br>Enter Franchisee!";
		}
		if (!franchisee_name.equals("") && franchisee_name.length() < 3) {
			msg = msg + "<br>Enter Minimum of 3 characters for Franchisee Name!";
		}

		if (franchisee_name.equals("")) {
			msg = msg + "<br>Enter Franchisee Name!";
		} else// similar Name found
		{
			StrSql = "SELECT franchisee_id FROM " + compdb(comp_id) + "axela_franchisee"
					+ " WHERE franchisee_name = '" + franchisee_name + "'"
					+ " AND franchisee_id != " + franchisee_id + "";
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Similar Franchisee Name Found! ";
			}
		}

		if (franchisee_franchiseetype_id.equals("0")) {
			msg = msg + "<br>Select Franchisee Type!";
		}

		if (franchisee_contact.equals("") || franchisee_contact.equals("")) {
			msg = msg + "<br>Enter Contact Person!";
		}
		if (!franchisee_contact.equals("") && franchisee_contact.length() < 3) {
			msg = msg + "<br>Enter Minimum of 3 characters for Contact Person!";
		}

		// Mobile
		if (franchisee_mobile1.equals("91-")) {
			franchisee_mobile1 = "";
		}
		if (franchisee_mobile1.equals("")) {
			msg = msg + "<br>Enter Mobile1!";
		}
		if (!franchisee_mobile1.equals("") && !IsValidMobileNo11(franchisee_mobile1)) {
			msg = msg + "<br>Mobile1 is Invalid!";
		}
		if (!franchisee_mobile2.equals("") && !IsValidMobileNo11(franchisee_mobile2)) {
			msg = msg + "<br>Enter valid Mobile2!";
		}

		// Phone
		if (franchisee_phone1.equals("")) {
			msg = msg + "<br>Enter Phone1!";
		}
		if (!franchisee_phone1.equals("")) {
			if (!IsValidPhoneNo11(franchisee_phone1)) {
				msg = msg + "<br>Enter valid Phone1!";
			}
		}
		if (!franchisee_phone2.equals("") && !IsValidPhoneNo11(franchisee_phone2)) {
			msg = msg + "<br>Enter valid Phone2!";
		}

		// Email1
		if (franchisee_email1.equals("")) {
			msg = msg + "<br>Enter Email1!";
		}
		if (!franchisee_email1.equals("") && !IsValidEmail(franchisee_email1)) {
			msg = msg + "<br>Enter Valid Email1!";
		}
		if (!franchisee_email2.equals("") && !IsValidEmail(franchisee_email2)) {
			msg = msg + "<br>Enter Valid Email2!";
		}
		if (!franchisee_url.equals("") && !IsValidURL(WebValidate(franchisee_url))) {
			msg = msg + "<br>Enter Valid URL!";
		}

		if (franchisee_add.equals("")) {
			msg = msg + "<br>Enter Address!";
		}

		if (franchisee_city_id.equals("")) {
			msg = msg + "<br>Select City!";
		}

		if (franchisee_pin.equals("")) {
			msg = msg + "<br>Enter Pin Code!";
		} else if (!franchisee_pin.equals("") && !isNumeric(franchisee_pin)) {
			msg = msg + "<br>Pin Code: Enter Numeric!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				franchisee_id = ExecuteQuery("Select coalesce(max(franchisee_id),0)+1 from " + compdb(comp_id) + "axela_franchisee");

				franchisee_id = "" + CheckCurrentId(Integer.parseInt(franchisee_id));

				StrSql = "Insert into " + compdb(comp_id) + "axela_franchisee"
						+ " (franchisee_id,"
						+ " franchisee_name,"
						+ " franchisee_franchiseetype_id,"
						+ " franchisee_contact,"
						+ " franchisee_phone1,"
						+ " franchisee_phone2,"
						+ " franchisee_mobile1,"
						+ " franchisee_mobile2,"
						+ " franchisee_email1,"
						+ " franchisee_email2,"
						+ " franchisee_url,"
						+ " franchisee_add,"
						+ " franchisee_city_id,"
						+ " franchisee_pin,"
						+ " franchisee_notes,"
						+ " franchisee_active,"
						+ " franchisee_entry_id,"
						+ " franchisee_entry_date)"
						+ " values"
						+ " (" + franchisee_id + ","
						+ " '" + franchisee_name + "',"
						+ " " + franchisee_franchiseetype_id + ","
						+ " '" + franchisee_contact + "',"
						+ " '" + franchisee_phone1 + "',"
						+ " '" + franchisee_phone2 + "',"
						+ " '" + franchisee_mobile1 + "',"
						+ " '" + franchisee_mobile2 + "',"
						+ " '" + franchisee_email1 + "',"
						+ " '" + franchisee_email2 + "',"
						+ " '" + franchisee_url + "',"
						+ " '" + franchisee_add + "',"
						+ " '" + franchisee_city_id + "',"
						+ " '" + franchisee_pin + "',"
						+ " '" + franchisee_notes + "',"
						+ " '" + franchisee_active + "',"
						+ " " + franchisee_entry_id + ","
						+ " '" + franchisee_entry_date + "')";
				// SOP(StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select " + compdb(comp_id) + "axela_franchisee.*, city_id, state_id"
					+ " from " + compdb(comp_id) + "axela_franchisee"
					+ " inner join " + compdb(comp_id) + "axela_city on city_id=franchisee_city_id"
					+ " inner join " + compdb(comp_id) + "axela_state on state_id=city_state_id"
					+ " where franchisee_id = " + franchisee_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					franchisee_id = crs.getString("franchisee_id");
					franchisee_name = crs.getString("franchisee_name");
					franchisee_franchiseetype_id = crs.getString("franchisee_franchiseetype_id");
					franchisee_contact = crs.getString("franchisee_contact");
					franchisee_phone1 = crs.getString("franchisee_phone1");
					franchisee_phone2 = crs.getString("franchisee_phone2");
					franchisee_mobile1 = crs.getString("franchisee_mobile1");
					franchisee_mobile2 = crs.getString("franchisee_mobile2");
					franchisee_email1 = crs.getString("franchisee_email1");
					franchisee_email2 = crs.getString("franchisee_email2");
					franchisee_add = crs.getString("franchisee_add");
					franchisee_pin = crs.getString("franchisee_pin");
					franchisee_url = crs.getString("franchisee_url");
					franchisee_notes = crs.getString("franchisee_notes");
					franchisee_active = crs.getString("franchisee_active");
					// state_id = crs.getString("state_id");
					franchisee_city_id = crs.getString("city_id");
					// if (franchisee_city_id == null) {
					// franchisee_city_id = "0";
					// }
					franchisee_entry_id = crs.getString("franchisee_entry_id");
					if (!franchisee_entry_id.equals("")) {
						franchisee_entry_by = Exename(comp_id, Integer.parseInt(franchisee_entry_id));
					}
					entry_date = strToLongDate(crs.getString("franchisee_entry_date"));
					franchisee_modified_id = crs.getString("franchisee_modified_id");
					if (!franchisee_modified_id.equals("0")) {
						franchisee_modified_by = Exename(comp_id, Integer.parseInt(franchisee_modified_id));
						modified_date = strToLongDate(crs.getString("franchisee_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Franchisee!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_franchisee"
						+ " SET"
						+ " franchisee_name = '" + franchisee_name + "',"
						+ " franchisee_franchiseetype_id = " + franchisee_franchiseetype_id + ","
						+ " franchisee_contact = '" + franchisee_contact + "',"
						+ " franchisee_phone1 = '" + franchisee_phone1 + "',"
						+ " franchisee_phone2 = '" + franchisee_phone2 + "',"
						+ " franchisee_mobile1 = '" + franchisee_mobile1 + "',"
						+ " franchisee_mobile2 = '" + franchisee_mobile2 + "',"
						+ " franchisee_email1 = '" + franchisee_email1 + "',"
						+ " franchisee_email2 = '" + franchisee_email2 + "',"
						+ " franchisee_url = '" + franchisee_url + "',"
						+ " franchisee_add = '" + franchisee_add + "',"
						+ " franchisee_city_id = " + franchisee_city_id + ","
						+ " franchisee_pin = '" + franchisee_pin + "',"
						+ " franchisee_notes = '" + franchisee_notes + "',"
						+ " franchisee_active = '" + franchisee_active + "',"
						+ " franchisee_modified_id = " + franchisee_modified_id + ","
						+ " franchisee_modified_date = '" + franchisee_modified_date + "'"
						+ " where  franchisee_id = " + franchisee_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT branch_franchisee_id from " + compdb(comp_id) + "axela_branch"
				+ " where branch_franchisee_id = " + franchisee_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Branch is Associated with this Franchisee!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_franchisee"
						+ " where franchisee_id = " + franchisee_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateFranchiseeType() {
		try {
			StrSql = "Select franchiseetype_id, franchiseetype_name"
					+ " from " + compdb(comp_id) + "axela_franchisee_type"
					+ " order by franchiseetype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String stringval = "<option value =0>Select</option>";
			while (crs.next()) {
				stringval = stringval + "<option value=" + crs.getString("franchiseetype_id") + "";
				stringval = stringval + StrSelectdrop(crs.getString("franchiseetype_id"), franchisee_franchiseetype_id);
				stringval = stringval + ">" + crs.getString("franchiseetype_name") + "</option> \n";
			}
			crs.close();
			return stringval;
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public String PopulateState() {
	// try {
	// StrSql = "SELECT state_id, state_name"
	// + " from " + compdb(comp_id) + "axela_state"
	// + " order by state_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// String group =
	// "<Select name=dr_state_id id=dr_state_id class=selectbox onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_city_id','city_id'); \"><option value=0>Select </option>";
	// while (crs.next()) {
	// group = group + "<option value=" + crs.getString("state_id") + "";
	// group = group + StrSelectdrop(crs.getString("state_id"), state_id);
	// group = group + ">" + crs.getString("state_name") + "</option> \n";
	// }
	// crs.close();
	// return group;
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }

	// public String PopulateCity() {
	// try {
	// if (state_id.equals("")) {
	// state_id = "0";
	// }
	// StrSql = "SELECT city_id, city_name from " + compdb(comp_id) +
	// "axela_city"
	// + " left join " + compdb(comp_id) +
	// "axela_state on state_id = city_state_id"
	// + " where city_state_id = " + state_id + " order by city_name";
	//
	// CachedRowSet crs =processQuery(StrSql, 0);
	// String group =
	// "<select name=dr_city_id class=selectbox><option value = 0>Select</option>";
	// if (!state_id.equals("0")) {
	// while (crs.next()) {
	// group = group + "<option value=" + crs.getString("city_id") + "";
	// group = group + StrSelectdrop(crs.getString("city_id"),
	// franchisee_city_id);
	// group = group + ">" + crs.getString("city_name") + "</option> \n";
	// }
	// }
	// group = group + "</select>";
	// crs.close();
	// return group;
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// }

	protected void PopulateConfigDetails() {
		StrSql = "Select count(franchisee_id) as activefranchiseecount"
				+ " from " + compdb(comp_id) + "axela_franchisee"
				+ " where franchisee_active = '1'";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				active_franchiseecount = crs.getInt("activefranchiseecount");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
