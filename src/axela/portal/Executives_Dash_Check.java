package axela.portal;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;
import cloudify.connect.Ecover_WS;

public class Executives_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String branch_id = "0";
	public String exe_id = "0";
	public String branch_brand_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String name = "";
	public String value = "";
	public String StrSql = "";
	public String StrHTML = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String labelname = "";
	// for Tabs
	public String history = "";
	public String accessright = "", accessId = "";
	public String updatefields = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			BranchAccess = GetSession("BranchAccess", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				name = PadQuotes(request.getParameter("name"));
				value = PadQuotes(request.getParameter("value"));
				exe_id = PadQuotes(request.getParameter("exe_id"));
				labelname = PadQuotes(request.getParameter("labelname"));
				accessright = PadQuotes(request.getParameter("accessright"));
				accessId = PadQuotes(request.getParameter("accessid"));
				updatefields = PadQuotes(request.getParameter("updatefields"));

				// for tabs
				history = PadQuotes(request.getParameter("history"));
				try {
					if (!exe_id.equals("0")) {
						if (history.equals("yes")) {
							StrHTML = new Executives_Dash().HistoryDetails(comp_id, exe_id);
						}
						if (ReturnPerm(comp_id, "emp_executive_add", request).equals("1")) {
							if (updatefields.equals("yes")) {
								StrHTML = UpdateFields(name, value);
							}

							if (ReturnPerm(comp_id, "emp_role_id", request).equals("1") && accessright.equals("yes")) {
								AccessRightsUpdate();
							}
							if (updatefields.equals("yes") || accessright.equals("yes")) {
								WSEcoverRequest(request);
							}
						} else {
							StrHTML = "<font color='red'>Access Denied!</font>";
						}

					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void WSEcoverRequest(HttpServletRequest request) throws JSONException {
		// Start Ecover Exe Update
		JSONObject input = new JSONObject();
		input.put("emp_id", emp_id);
		input.put("name", name);
		input.put("value", value);
		input.put("exe_id", exe_id);
		input.put("labelname", labelname);
		input.put("accessright", accessright);
		input.put("accessId", accessId);
		input.put("history", history);
		input.put("accessright", accessright);
		input.put("updatefields", updatefields);
		new Ecover_WS().WSRequest(input, "axelaauto-exedash-check", request);
	}

	public String UpdateFields(String name, String value) throws SQLException {

		// txt_emp_name
		if (name.equals("txt_emp_name")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				String history_oldvalue = ExecuteQuery("SELECT emp_name" + " FROM " + compdb(comp_id) + "axela_emp" + " WHERE emp_id = " + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_name = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Executive Name";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Executive Name Updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Name!</font>";
			}
		}

		// txt_emp_ref_no
		if (name.equals("txt_emp_ref_no")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				StrSql = "SELECT emp_ref_no FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_ref_no = '" + value + "'"
						+ " AND emp_id != " + exe_id + "";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					StrHTML = "<font color=\"red\">Similar Reference Number found!</font>";
				}
				crs.close();

				if (StrHTML.equals("")) {
					String history_oldvalue = ExecuteQuery("SELECT emp_ref_no"
							+ " FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE emp_id=" + exe_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET"
							+ " emp_ref_no = '" + value + "'"
							+ " WHERE emp_id = " + exe_id + "";
					updateQuery(StrSql);
					String history_actiontype = "Reference No.";
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
							+ " ("
							+ " history_emp_id,"
							+ " history_modified_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + exe_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Reference No. Updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Reference No.!</font>";
			}
		}

		// txt_emp_qualification
		if (name.equals("txt_emp_qualification")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_qualification"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_qualification = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Qualification";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ " history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "Qualification Updated!";
		}

		// txt_emp_certification
		if (name.equals("txt_emp_certification")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_certification"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_certification = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Certification";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ " history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "Certification Updated!";
		}

		// emp_phone1
		if (name.equals("txt_emp_phone1")) {
			value = value.replaceAll("nbsp", "&");
			if (IsValidPhoneNo11(value)) {
				String history_oldvalue = ExecuteQuery("SELECT emp_phone1 "
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id =" + exe_id + "");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_phone1 = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Phone 1";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Phone 1 Updated!";

			} else {
				StrHTML = "<font color=\"red\">Enter Valid Phone 1!</font>";
			}
		}

		// emp_phone2
		if (name.equals("txt_emp_phone2")) {
			if (IsValidPhoneNo11(value)) {
				value = value.replaceAll("nbsp", "&");

				String history_oldvalue = ExecuteQuery("SELECT emp_phone2 "
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=" + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_phone2 = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Phone 2";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Phone 2 Updated!";

			} else {
				StrHTML = "<font color=\"red\">Enter Valid Phone 2!</font>";
			}
		}

		// emp_mobile1
		if (name.equals("txt_emp_mobile1")) {
			if (IsValidMobileNo11(value)) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT emp_mobile1 "
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id =" + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_mobile1 = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Mobile 1";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Mobile 1 Updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Valid Mobile 1!</font>";
			}
		}

		// emp_mobile2
		if (name.equals("txt_emp_mobile2")) {
			if (IsValidMobileNo11(value)) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT emp_mobile2 "
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=" + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_mobile2 = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Mobile 2";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Mobile 2 Updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Valid Mobile 2!</font>";
			}
		}

		// emp_email1
		if (name.equals("txt_emp_email1")) {
			if (!value.equals("") && IsValidEmail(value)) {
				value = value.replaceAll("nbsp", "&");
				StrSql = "SELECT emp_email1 FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE" + " (emp_email1 = '" + value
						+ "' OR emp_email2 = '" + value + "')"
						+ " AND emp_id !=" + exe_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Email 1  Found!</font>";
				}
				StrSql = "SELECT emp_email1 From " + maindb() + "uni_emp"
						+ " WHERE " + " emp_email1 = '" + value + "'"
						+ " AND emp_id !=" + exe_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">User is already present in Universal Database!</font>";
				}
				if (StrHTML.equals("")) {
					String history_oldvalue = ExecuteQuery("SELECT emp_email1 "
							+ " FROM " + compdb(comp_id) + "axela_emp "
							+ " WHERE emp_id=" + exe_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET"
							+ " emp_email1 = '" + value + "'"
							+ " WHERE emp_id = " + exe_id + "";
					updateQuery(StrSql);

					new Executive_Univ_Check().UpdateUniversalEmp(exe_id, comp_id);

					String history_actiontype = "Email 1";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
							+ " ("
							+ " history_emp_id,"
							+ "	history_modified_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + exe_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Email 1 Updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Valid Email 1!</font>";
			}
		}

		// emp_email2
		if (name.equals("txt_emp_email2")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				value = value.toLowerCase();
				if (!IsValidEmail(value)) {
					StrHTML = "<font color=\"red\">Enter Valid Email 2!</font>";
				}
				StrSql = "SELECT emp_id FROM " + compdb(comp_id)
						+ "axela_emp" + " WHERE (emp_email1 = '"
						+ value + "' OR emp_email2 = '" + value
						+ "')"
						+ " AND emp_id != " + exe_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Email 2  Found!</font>";
				}
				String Email1 = ExecuteQuery("SELECT emp_email1 "
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=" + exe_id + " ");
				if (value.equals(Email1)) {
					StrHTML = "<font color=\"red\">Email2 is same as Email1!</font>";
				}
			}

			if (StrHTML.equals("")) {
				String history_oldvalue = ExecuteQuery("SELECT emp_email2 "
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id=" + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_email2 = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Email 2";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Email 2 Updated!";
			}

		}

		// txt_emp_address
		if (name.equals("txt_emp_address")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				String history_oldvalue = ExecuteQuery("SELECT emp_address"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_address = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Address";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Address Updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Address!</font>";
			}
		}

		// txt_emp_landmark
		if (name.equals("txt_emp_landmark")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_landmark"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_landmark = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Landmark";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ " history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "Landmark Updated!";
		}

		// txt_emp_state
		if (name.equals("txt_emp_state")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				String history_oldvalue = ExecuteQuery("SELECT emp_state"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_state = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "State";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "State Updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter State!</font>";
			}
		}

		// txt_emp_city
		if (name.equals("txt_emp_city")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				String history_oldvalue = ExecuteQuery("SELECT emp_city"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_city = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "City";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "City Updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter City!</font>";
			}
		}

		// txt_emp_pin
		if (name.equals("txt_emp_pin")) {
			value = value.replaceAll("nbsp", "&");

			if (!value.equals("")) {
				if (value.length() == 6) {
					String history_oldvalue = ExecuteQuery("SELECT emp_pin FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + exe_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET"
							+ " emp_pin = '" + value + "'"
							+ " WHERE emp_id = " + exe_id + "";
					updateQuery(StrSql);
					String history_actiontype = "Pin/Zip";
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
							+ " ("
							+ " history_emp_id,"
							+ "	history_modified_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + exe_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Pin/Zip Updated!";
				} else {
					StrHTML = "<font color=\"red\">Enter Valid Pin/Zip!</font>";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Pin/Zip!</font>";
			}
		}

		// role
		if (name.equals("drop_emp_role_id")) {
			value = value.replaceAll("nbsp", "&");
			String emp_branch_id = ExecuteQuery("SELECT emp_branch_id"
					+ " FROM " + compdb(comp_id) + "axela_emp" + " "
					+ " WHERE emp_id = " + exe_id);
			if (!value.equals("-1")) {
				if (!emp_branch_id.equals("-1") && !emp_branch_id.equals("0") && !value.equals("2")) {
					StrHTML = "<font color=\"red\">Select Head Office Branch for Administrator Role !</font>";
				}

				if (StrHTML.equals("")) {
					String history_oldvalue = ExecuteQuery("SELECT role_name"
							+ " FROM " + compdb(comp_id) + "axela_emp_role" + " "
							+ " INNER JOIN " + compdb(comp_id) + " axela_emp ON emp_role_id = role_id"
							+ " WHERE emp_id = " + exe_id);
					String history_newvalue = ExecuteQuery("SELECT role_name"
							+ " FROM " + compdb(comp_id) + "axela_emp_role" + " "
							+ " WHERE role_id = " + value);
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET"
							+ " emp_role_id = '" + value + "'"
							+ " WHERE emp_id = " + exe_id + "";
					updateQuery(StrSql);
					String history_actiontype = "Role";
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
							+ " ("
							+ " history_emp_id,"
							+ "	history_modified_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + exe_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Role Updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Select Role!</font>";
			}
		}

		// emp_department_id
		if (name.equals("drop_emp_department_id")) {
			value = value.replaceAll("nbsp", "&");

			if (!value.equals("-1")) {
				String history_oldvalue = ExecuteQuery("SELECT department_name"
						+ " FROM " + compdb(comp_id) + "axela_emp_department" + " "
						+ " INNER JOIN " + compdb(comp_id) + " axela_emp ON emp_department_id = department_id"
						+ " WHERE emp_id = " + exe_id);
				String history_newvalue = ExecuteQuery("SELECT department_name"
						+ " FROM " + compdb(comp_id) + "axela_emp_department" + " "
						+ " WHERE department_id = " + value);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_department_id = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Department";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				StrHTML = "Department Updated!";
			} else {
				StrHTML = "<font color=\"red\">Select Department!</font>";
			}
		}

		// drop_emp_jobtitle_id
		if (name.equals("drop_emp_jobtitle_id")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("-1")) {
				String history_oldvalue = ExecuteQuery("SELECT jobtitle_desc"
						+ " FROM " + compdb(comp_id) + "axela_jobtitle" + " "
						+ " INNER JOIN " + compdb(comp_id) + " axela_emp ON emp_jobtitle_id = jobtitle_id"
						+ " WHERE emp_id = " + exe_id);
				String history_newvalue = ExecuteQuery("SELECT jobtitle_desc"
						+ " FROM " + compdb(comp_id) + "axela_jobtitle" + " "
						+ " WHERE jobtitle_id = " + value);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_jobtitle_id = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "JobTitle";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				StrHTML = "JobTitle Updated!";
			} else {
				StrHTML = "<font color='red'>Select JobTitle!</font>";
			}
		}

		// branch dropdown
		if (name.equals("dr_emp_branch_id")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("-1")) {
				StrSql = "SELECT emp_role_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + exe_id;
				if (ExecuteQuery(StrSql).equals("1") && !value.equals("0")) {
					StrHTML = "Branch should be Headoffice for Administrator!";
				} else {
					String history_oldvalue = ExecuteQuery("SELECT branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch" + " "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_branch_id = branch_id"
							+ " WHERE emp_id = " + exe_id);
					String history_newvalue = ExecuteQuery("SELECT branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch" + " "
							+ " WHERE branch_id = " + value);
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET"
							+ " emp_branch_id = '" + value + "'"
							+ " WHERE emp_id = " + exe_id + "";
					updateQuery(StrSql);
					String history_actiontype = "Branch";
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
							+ " ("
							+ " history_emp_id,"
							+ "	history_modified_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + exe_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Branch Updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Select Branch!</font>";
			}
		}

		// weekly off
		if (name.equals("dr_weeklyoff_id")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT weeklyoff_name"
					+ " FROM " + compdb(comp_id) + "axela_emp_weeklyoff" + " "
					+ " INNER JOIN " + compdb(comp_id) + " axela_emp ON emp_weeklyoff_id = weeklyoff_id"
					+ " WHERE emp_id = " + exe_id);
			String history_newvalue = ExecuteQuery("SELECT weeklyoff_name"
					+ " FROM " + compdb(comp_id) + "axela_emp_weeklyoff" + " "
					+ " WHERE weeklyoff_id = " + value);
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_weeklyoff_id = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Weeklyoff";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + history_newvalue + "')";
			updateQuery(StrSql);
			StrHTML = "Weeklyoff Updated!";
		}

		// emp_pass
		if (name.equals("txt_emp_upass")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				if (!value.equals("") && value.length() < 8) {
					StrHTML = "<font color=\"red\">Your new password cannot be less than 8 Characters!</font><br>";
				}
				if (exe_id.equals("1")) {
					StrHTML += "<font color=\"red\">Can't update Password for Admin!</font>";
				}
				if (StrHTML.equals("")) {
					String history_oldvalue = ExecuteQuery("SELECT emp_upass "
							+ " FROM " + compdb(comp_id) + "axela_emp "
							+ " WHERE emp_id=" + exe_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET"
							+ " emp_upass = '" + value + "'"
							+ " WHERE emp_id = " + exe_id + "";
					updateQuery(StrSql);

					new Executive_Univ_Check().UpdateUniversalEmp(exe_id, comp_id);

					String history_actiontype = "Password";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
							+ " ("
							+ " history_emp_id,"
							+ "	history_modified_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + exe_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Password Updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Your new password cannot be blank!</font>";
			}
		}

		// txt_emp_caller_id
		if (name.equals("txt_emp_caller_id")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_callerid"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_callerid = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "CallerID";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "CallerID Updated!";
		}

		// txt_emp_routeno
		if (name.equals("txt_emp_routeno")) {
			value = value.replaceAll("nbsp", "&");
			String emp_clicktocall = ExecuteQuery("SELECT emp_clicktocall"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			if (emp_clicktocall.equals("1") && value.equals("")) {
				StrHTML = "<font color=\"red\">Enter Route No.!</font>";
			} else {
				String history_oldvalue = ExecuteQuery("SELECT emp_routeno"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_routeno = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Route No.";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Route No. Updated!";
			}
		}
		// txt_emp_clicktocall_username
		if (name.equals("txt_emp_clicktocall_username")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_clicktocall_username"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_clicktocall_username = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "ClickToCallUsername";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "ClickToCallUsername Updated!";
		}

		// txt_emp_clicktocall_password
		if (name.equals("txt_emp_clicktocall_password")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_clicktocall_password"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_clicktocall_password = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "ClickToCallPassword";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "ClickToCallPassword Updated!";
		}

		// txt_emp_clicktocall_campaign
		if (name.equals("txt_emp_clicktocall_campaign")) {
			value = value.replaceAll("nbsp", "&");

			String history_oldvalue = ExecuteQuery("SELECT emp_clicktocall_campaign"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_clicktocall_campaign = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "ClickToCallCampaign";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "ClickToCallCampaign Updated!";
		}

		// drop_emp_married
		if (name.equals("drop_emp_married")) {
			String history_newvalue = "";
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("-1")) {
				if (value.equals("0")) {
					history_newvalue = "Unmarried";
				} else {
					history_newvalue = "Married";
				}

				String history_oldvalue = ExecuteQuery("SELECT emp_married"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + exe_id + " ");
				if (history_oldvalue.equals("0"))
				{
					history_oldvalue = "Unmarried";
				}
				else
				{
					history_oldvalue = "Married";
				}
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_married = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Marital Status";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				StrHTML = "Marital Status Updated!";
			}
			else {
				StrHTML = "<font color=\"red\">Select Marital Status!</font>";
			}
		}

		// sex
		if (name.equals("drop_emp_sex"))
		{
			String history_newvalue = "";
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("-1")) {
				if (value.equals("0")) {
					history_newvalue = "Female";
				} else {
					history_newvalue = "Male";
				}
				String history_oldvalue = ExecuteQuery("SELECT emp_sex"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + exe_id + " ");
				if (history_oldvalue.equals("1")) {
					history_oldvalue = "Male";
				} else {
					history_oldvalue = "Female";
				}
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_sex = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Gender Status";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				StrHTML = "Gender Updated!";
			}
			else {
				StrHTML = "<font color=\"red\">Select Gender!</font>";
			}
		}

		// previous exp
		// prev year exp
		if (name.equals("dr_emp_prevexp_year")) {
			String year = "";
			String month = "";
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_prevexp"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + exe_id + " ");
			year = history_oldvalue.split(",")[0];
			month = history_oldvalue.split(",")[1];
			history_oldvalue = year + " Year(s), " + month + " Month(s)";
			year = value;
			String history_newvalue = year + "," + month;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_prevexp = '" + history_newvalue + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Previous Year Experience";
			history_newvalue = year + " Year(s), " + month + " Month(s)";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + history_newvalue + "')";
			updateQuery(StrSql);
			StrHTML = "Previous Year Experience Updated!";
		}

		// prev month exp
		if (name.equals("dr_emp_prevexp_month")) {
			value = value.replaceAll("nbsp", "&");
			String year = "";
			String month = "";
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_prevexp"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			year = history_oldvalue.split(",")[0];
			month = history_oldvalue.split(",")[1];
			history_oldvalue = year + " Year(s), " + month + " Month(s)";
			month = value;
			String history_newvalue = year + "," + month;
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_prevexp = '" + history_newvalue + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Previous Month Experience";
			history_newvalue = year + " Year(s), " + month + " Month(s)";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + history_newvalue + "')";
			updateQuery(StrSql);
			StrHTML = "Previous Month Experience Updated!";
		}

		// drop_DOBDay
		if (name.equals("drop_DOBDay") || name.equals("drop_DOBMonth") || name.equals("drop_DOBYear")) {
			value = value.replaceAll("nbsp", "&");
			if (isValidDateFormatStr(value)) {
				if (value.length() == 1) {
					value = "0" + value;
				}
				String history_oldvalue = ExecuteQuery("SELECT emp_dob"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + exe_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_dob = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "DOB";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + strToShortDate(history_oldvalue) + "',"
						+ " '" + strToShortDate(value) + "')";
				updateQuery(StrSql);
				StrHTML = "DOB Updated!";
			} else {
				StrHTML = "<font color=\"red\">Select Valid DOB!</font>";
			}
		}

		// txt_emp_date_of_join
		if (name.equals("txt_emp_date_of_join")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("") && isValidDateFormatShort(value)) {
				String history_oldvalue = ExecuteQuery("SELECT emp_date_of_join"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_date_of_join = '" + ConvertShortDateToStr(value) + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "DateOfJoin";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + strToShortDate(history_oldvalue) + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Date Of Join Updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Date Of Join!</font>";
			}
		}

		// txt_emp_date_of_relieve
		if (name.equals("txt_emp_date_of_relieve")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_date_of_relieve"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_date_of_relieve = '" + ConvertShortDateToStr(value) + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Date Of Relieve";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + strToShortDate(history_oldvalue) + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "Date Of Relieve Updated!";
		}

		// reason of leaving
		if (name.equals("txt_emp_reason_of_leaving")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_reason_of_leaving"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_reason_of_leaving = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Reason Of Leaving";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "Reason Of Leaving Updated!";
		}

		// notes
		if (name.equals("txt_emp_notes")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_notes"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_notes = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Notes";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "Notes Updated!";
		}

		if (name.equals("exe_soe_trans")) {
			value = PadQuotes(value.replaceAll("nbsp", "&"));
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			String exe_soe_trans[] = null;

			if (!value.equals("")) {
				exe_soe_trans = value.split(",");
			}

			String history_oldvalue = ExecuteQuery("SELECT COALESCE(GROUP_CONCAT(empsoe_soe_id), '')"
					+ " FROM " + compdb(comp_id) + "axela_emp_soe  WHERE empsoe_emp_id =" + exe_id);

			try {
				stmttx.addBatch("DELETE FROM " + compdb(comp_id) + "axela_emp_soe" + " WHERE empsoe_emp_id = " + exe_id);

				String history_actiontype = "SOE Added";
				if (exe_soe_trans != null) {
					for (int i = 0; i < exe_soe_trans.length; i++) {
						if (!exe_soe_trans[i].equals(null)) {

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_soe"
									+ " (empsoe_emp_id,"
									+ " empsoe_soe_id)"
									+ " VALUES"
									+ " ("
									+ exe_id + ","
									+ exe_soe_trans[i]
									+ ")";
							stmttx.addBatch(StrSql);

							if (!history_oldvalue.contains(exe_soe_trans[i])) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
										+ " ("
										+ " history_emp_id,"
										+ " history_modified_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + exe_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '',"
										+ " (SELECT COALESCE (soe_name, '') FROM " + compdb(comp_id) + "axela_soe WHERE soe_id = " + exe_soe_trans[i] + "))";
								stmttx.addBatch(StrSql);
							}
						}
					}
				}

				history_actiontype = "SOE Removed";

				String history_remove_trans1[] = value.split(",");
				String history_remove_trans2 = history_oldvalue;
				for (int i = 0; i < history_remove_trans1.length; i++) {
					history_remove_trans2 = history_remove_trans2.replaceFirst(history_remove_trans1[i], "");
				}
				history_remove_trans1 = history_remove_trans2.split(",");
				if (history_remove_trans2.length() > 1) {
					for (int j = 0; j < history_remove_trans1.length; j++) {
						if (!history_remove_trans1[j].equals("")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
									+ " ("
									+ " history_emp_id,"
									+ " history_modified_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + exe_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '',"
									+ " (SELECT COALESCE (soe_name, '') FROM " + compdb(comp_id) + "axela_soe WHERE soe_id = " + history_remove_trans1[j] + "))";
							stmttx.addBatch(StrSql);
						}
					}
				}

				stmttx.executeBatch();
				conntx.commit();
				StrHTML = "SOE Updated!";
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Connection rollback...");
				}
				msg = "<br>Transaction Error!";
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

		if (name.equals("exe_branch_trans")) {
			value = PadQuotes(value.replaceAll("nbsp", "&"));
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			String exe_branch_trans[] = null;

			if (!value.equals("")) {
				exe_branch_trans = value.split(",");
			}

			String history_oldvalue = ExecuteQuery("SELECT COALESCE(GROUP_CONCAT(emp_branch_id), '')"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch  WHERE emp_id =" + exe_id);

			try {
				stmttx.addBatch("DELETE FROM " + compdb(comp_id) + "axela_emp_branch" + " WHERE emp_id = " + exe_id);

				String history_actiontype = "Branch Added";
				if (exe_branch_trans != null) {
					for (int i = 0; i < exe_branch_trans.length; i++) {
						if (!exe_branch_trans[i].equals(null)) {

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_branch"
									+ " (emp_id,"
									+ " emp_branch_id)"
									+ " VALUES"
									+ " ("
									+ exe_id + ","
									+ exe_branch_trans[i]
									+ ")";
							stmttx.addBatch(StrSql);

							if (!history_oldvalue.contains(exe_branch_trans[i])) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
										+ " (history_emp_id,"
										+ " history_modified_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + exe_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '',"
										+ " (SELECT COALESCE(branch_name, '') AS branch_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + exe_branch_trans[i] + "))";
								stmttx.addBatch(StrSql);
							}
						}
					}
				}

				history_actiontype = "Branch Removed";

				String history_remove_trans1[] = value.split(",");
				String history_remove_trans2 = history_oldvalue;
				for (int i = 0; i < history_remove_trans1.length; i++) {
					history_remove_trans2 = history_remove_trans2.replaceFirst(history_remove_trans1[i], "");
				}
				history_remove_trans1 = history_remove_trans2.split(",");
				if (history_remove_trans2.length() > 1) {
					for (int j = 0; j < history_remove_trans1.length; j++) {
						if (!history_remove_trans1[j].equals("")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
									+ " ("
									+ " history_emp_id,"
									+ " history_modified_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + exe_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '',"
									+ " (SELECT COALESCE(branch_name, '') AS branch_name FROM " + compdb(comp_id) + "axela_branch WHERE branch_id = " + history_remove_trans1[j] + "))";
							stmttx.addBatch(StrSql);
						}
					}
				}

				stmttx.executeBatch();
				conntx.commit();
				StrHTML = "Branch Updated!";
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Connection rollback...");
				}
				msg = "<br>Transaction Error!";
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

		if (name.equals("exe_team_trans")) {
			value = PadQuotes(value.replaceAll("nbsp", "&"));
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			String exe_team_trans[] = null;

			if (!value.equals("")) {
				exe_team_trans = value.split(",");
			}

			String history_oldvalue = ExecuteQuery("SELECT COALESCE(GROUP_CONCAT(empexe_id), '')"
					+ " FROM " + compdb(comp_id) + "axela_emp_exe  WHERE empexe_emp_id =" + exe_id);

			try {
				stmttx.addBatch("DELETE FROM " + compdb(comp_id) + "axela_emp_exe" + " WHERE empexe_emp_id = " + exe_id);

				String history_actiontype = "Executive Added";
				if (exe_team_trans != null) {
					for (int i = 0; i < exe_team_trans.length; i++) {
						if (!exe_team_trans[i].equals(null)) {

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_exe"
									+ " (empexe_emp_id,"
									+ " empexe_id)"
									+ " VALUES"
									+ " ("
									+ exe_id + ","
									+ exe_team_trans[i]
									+ ")";
							stmttx.addBatch(StrSql);

							if (!history_oldvalue.contains(exe_team_trans[i])) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
										+ " ("
										+ " history_emp_id,"
										+ " history_modified_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + exe_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '',"
										+ " (SELECT COALESCE (emp_name, '') FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + exe_team_trans[i] + "))";
								stmttx.addBatch(StrSql);
							}
						}
					}
				}

				history_actiontype = "Executive Removed";

				String history_remove_trans1[] = value.split(",");
				String history_remove_trans2 = history_oldvalue;
				for (int i = 0; i < history_remove_trans1.length; i++) {
					history_remove_trans2 = history_remove_trans2.replaceFirst(history_remove_trans1[i], "");
				}
				history_remove_trans1 = history_remove_trans2.split(",");
				if (history_remove_trans2.length() > 1) {
					for (int j = 0; j < history_remove_trans1.length; j++) {
						if (!history_remove_trans1[j].equals("")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
									+ " ("
									+ " history_emp_id,"
									+ " history_modified_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + exe_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '',"
									+ " (SELECT COALESCE (emp_name, '') FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + history_remove_trans1[j] + "))";
							stmttx.addBatch(StrSql);
						}
					}
				}

				stmttx.executeBatch();
				conntx.commit();
				StrHTML = "Executive Updated!";
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Connection rollback...");
				}
				msg = "<br>Transaction Error!";
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

		if (name.equals("chk_emp_all_branches")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT emp_all_branches" + " FROM " + compdb(comp_id) + "axela_emp" + " WHERE emp_id =" + exe_id + " ");

				if (history_oldvalue.equals("0")) {
					history_oldvalue = "All Branches Access Removed";
				} else {
					history_oldvalue = "All Branches Access Added";
				}

				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_all_branches = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);

				String history_newvalue = "";
				if (value.equals("0")) {
					history_newvalue = "All Branches Access Removed";
				} else {
					history_newvalue = "All Branches Access Added";
				}

				updateQuery("DELETE FROM " + compdb(comp_id) + "axela_emp_branch" + " WHERE emp_id = " + exe_id);

				String history_actiontype = "All Branches";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				StrHTML = "All Branches Updated!";
			}
		}

		if (name.equals("chk_emp_all_exe")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT emp_all_exe" + " FROM " + compdb(comp_id) + "axela_emp" + " WHERE emp_id =" + exe_id + " ");

				if (history_oldvalue.equals("0")) {
					history_oldvalue = "All Executive Access Removed";
				} else {
					history_oldvalue = "All Executive Access Added";
				}

				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_all_exe = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);

				String historyNewValue = "";
				if (value.equals("0")) {
					historyNewValue = "All Executive Removed";
				}
				else {
					historyNewValue = "All Executive Added";
				}

				updateQuery("DELETE FROM " + compdb(comp_id) + "axela_emp_exe" + " WHERE empexe_emp_id = " + exe_id);

				String history_actiontype = "All Executives";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ " history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + historyNewValue + "')";
				updateQuery(StrSql);
				StrHTML = "All Executives Updated!";
			}
		}

		// for all checkboxes
		if (name.startsWith("ch_") && !accessright.equals("yes")) {
			name = name.substring(3);
			if (!value.equals("")) {
				StrHTML = "";
				value = value.replaceAll("nbsp", "&");
				if (name.equals("emp_clicktocall") && value.equals("1")) {
					String emp_routeno = ExecuteQuery("SELECT emp_routeno FROM " + compdb(comp_id) + "axela_emp" + " WHERE emp_id =" + exe_id + " ");
					if (emp_routeno.equals("")) {
						StrHTML = "<font color=\"red\">Enter Route No.!</font>";
					}
				}
				if (StrHTML.equals("")) {
					String history_oldvalue = ExecuteQuery("SELECT IF(" + name + " = 1, 'Added', 'Removed') AS checked"
							+ " FROM " + compdb(comp_id) + "axela_emp" + " WHERE emp_id =" + exe_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
							+ " SET " + name + " = '" + value + "'"
							+ " WHERE emp_id = " + exe_id + "";
					updateQuery(StrSql);

					if (name.equals("emp_active")) {
						new Executive_Univ_Check().UpdateUniversalEmp(exe_id, comp_id);
					}
					String history_newvalue = "";
					if (value.equals("0")) {
						history_newvalue = "Removed";
					} else {
						history_newvalue = "Added";
					}

					String history_actiontype = labelname;
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
							+ " ("
							+ " history_emp_id,"
							+ "	history_modified_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + exe_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = labelname + " Access " + history_newvalue + "!";
				}
			}
		}

		// device_id
		if (name.equals("txt_emp_device_id")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT emp_device_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id = " + exe_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_device_id = '" + value + "'"
					+ " WHERE emp_id = " + exe_id + "";
			updateQuery(StrSql);

			new Executive_Univ_Check().UpdateUniversalEmp(exe_id, comp_id);

			String history_actiontype = "Device ID";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			StrHTML = "Device ID Updated!";
		}

		// IP Access
		if (name.equals("txt_emp_ip_access")) {
			value = value.replaceAll("nbsp", "&");
			String[] str = new String[10];
			try {
				str = value.split(" ");
				for (int i = 0; i < str.length - 1; i++) {
					if (!str[i].endsWith(",")) {
						StrHTML = "<font color=\"red\">Invalid IP access!</font>";
					}
				}
				if (str[str.length - 1].contains(",")) {
					StrHTML = "<font color=\"red\">Invalid IP access!</font>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}

			if (StrHTML.equals("")) {
				String history_oldvalue = ExecuteQuery("SELECT emp_ip_access" + " FROM " + compdb(comp_id) + "axela_emp" + " WHERE emp_id=" + exe_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET" + " emp_ip_access = '" + value + "'"
						+ " WHERE emp_id = " + exe_id + "";
				updateQuery(StrSql);
				String history_actiontype = "IP Access";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
						+ " ("
						+ " history_emp_id,"
						+ "	history_modified_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + exe_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "IP Access Updated!";
			}
		}

		return StrHTML;

	}
	// access rights
	public void AccessRightsUpdate() {
		// Get the name of the Acces rights
		String history_actiontype = ExecuteQuery("SELECT access_name FROM axelaauto.axela_module_access WHERE access_id = " + accessId);
		if (accessright.equals("yes") && name.startsWith("chk_access_")) {
			value = value.replaceAll("nbsp", "&");

			// ------ name.replaceAll("chk_access_", "") methods is used to get the Id of the Access rights
			String history_oldvalue = ExecuteQuery("SELECT COUNT(empaccess_access_id) FROM " + compdb(comp_id) + "axela_emp_access"
					+ " WHERE empaccess_emp_id = " + exe_id + " AND empaccess_access_id = " + accessId);
			String history_newvalue = "";

			if (history_oldvalue.equals("0")) {
				history_oldvalue = "Removed";
			} else {
				history_oldvalue = "Added";
			}

			// Delete the existing data from trans table if the value is 0
			if (value.equals("0")) {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_emp_access "
						+ " WHERE empaccess_emp_id = " + exe_id
						+ " AND empaccess_access_id = " + accessId;
				// SOP("StrSql--delete-- " + StrSql);
				updateQuery(StrSql);
			}

			// Insert the entry if the value is 1 into the trans table
			if (history_oldvalue.equals("Removed") && value.equals("1")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_access"
						+ " (empaccess_emp_id, empaccess_access_id)"
						+ " VALUES"
						+ "(" + exe_id + "," + accessId + ")";
				// SOP("StrSql--insert-- " + StrSql);
				updateQuery(StrSql);
			}

			if (value.equals("0")) {
				history_newvalue = "Removed!";
			} else {
				history_newvalue = "Added!";
			}

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_emp_history"
					+ " ("
					+ " history_emp_id,"
					+ "	history_modified_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + exe_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + toTitleCase(history_actiontype.substring(4).replaceAll("_", " ")) + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + history_newvalue + "')";
			updateQuery(StrSql);
			StrHTML = toTitleCase(history_actiontype.substring(4).replaceAll("_", " ")) + " Access " + history_newvalue;
		}
	}
}