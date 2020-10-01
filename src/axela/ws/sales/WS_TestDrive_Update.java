package axela.ws.sales;
//divya 26th march 2014

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_TestDrive_Update extends ConnectWS {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String msg = "";
	public String emp_uuid = "0";
	public String chkpermmsg = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String BranchAccess = "", branch_id = "";
	public Connection conntx = null;
	Statement stmttx = null;
	public String QueryString = "";
	public String testdrive_id = "";
	public String testdrive_veh_id = "";
	public String testdrive_emp_id = "";
	public String testdrive_location_id = "";
	public String testdrive_type_id = "";
	public String testdrive_time = "", testdrive_time_from = "", testdrive_time_to = "";
	public String testdrive_confirmed = "", unconfirm = "";
	public String testdrive_notes = "";
	public String testdrive_entry_id = "";
	public String testdrive_entry_date = "";
	public String enquiry_id = "";
	public String testdrive_modified_id = "";
	public String testdrive_modified_date = "";
	public String enquiry_branch_id = "";
	public String enquiry_model_id = "";
	public String testdrive_customer_id = "";
	public String customer_name = "", customer_email1 = "", customer_pin = "", customer_address = "";
	public String model_name = "";
	public String executive_name = "";
	public String strHTML = "";
	public String enquiry_no = "", enquiry_date = "", enquiry_name = "";
	public String pop = "";
	// public String testdrivedate = "";
	// ws
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject TestDrive_Update(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
		}
		if (!input.isNull("comp_id")) {
			comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
		}
		if (!input.isNull("emp_uuid")) {
			emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
		}
		if (!input.isNull("add")) {
			add = PadQuotes((String) input.get("add"));
		}
		if (!input.isNull("addB")) {
			addB = PadQuotes((String) input.get("addB"));
		}
		if (!input.isNull("update")) {
			update = PadQuotes((String) input.get("update"));
		}
		if (!input.isNull("updateB")) {
			updateB = PadQuotes((String) input.get("updateB"));
		}
		if (!input.isNull("enquiry_id")) {
			enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			if (!enquiry_id.equals("0") && add.equals("yes") && !"yes".equals(addB)) {
				EnquiryDetails(input);
			}
		}
		if (!input.isNull("testdrive_id")) {
			testdrive_id = CNumeric(PadQuotes((String) input.get("testdrive_id")));
			if (!add.equals("yes")) {
				PopulateFields(input);
			}
		}
		if (!input.isNull("unconfirm")) {
			unconfirm = PadQuotes((String) input.get("unconfirm"));
		}

		if (add.equals("yes")) {
			status = "Add";
		} else if (update.equals("yes")) {
			status = "Update";
		}

		if (!emp_id.equals("0")) {
			if ("yes".equals(add)) {
				if (!"yes".equals(addB)) {
					output.put("type_id", "1");
					output.put("testdrive_confirmed", "1");
					output.put("testdrive_time", strToLongDate(ToLongDate(kknow())));
					// SOP("enquiry_branch_id = " + enquiry_branch_id);
					PopulateVehicle();
					PopulateLocation();
					PopulateTestDriveType();
					// SOP("output = " + output);
				} else {
					GetValues(input);
					testdrive_entry_id = emp_id;
					testdrive_entry_date = ToLongDate(kknow());
					AddFields();
					// if (!msg.equals("")) {
					// msg = "Error!" + msg;
					// } else {
					// msg = "TestDrive added successfully!";
					// }
				}
			}
			try {

				if (unconfirm.equals("yes")) {
					if (testdrive_confirmed.equals("1")) {
						StrSql = "SELECT emp_id, testdrive_id, "
								+ "(select concat(emp_name,' (',emp_ref_no,')') "
								+ " from " + compdb(comp_id) + "axela_emp where emp_id = " + emp_id + ") as updateemp "
								+ " from " + compdb(comp_id) + "axela_emp"
								+ " inner join " + compdb(comp_id) + "axela_sales_testdrive on testdrive_emp_id = emp_id "
								+ " WHERE emp_active ='1' and testdrive_id = " + testdrive_id + "";
						//
						StrSql = StrSql + " ORDER BY emp_name";
						CachedRowSet crs = processQuery(StrSql, 0);
						String testdrive_notes = "";
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								testdrive_notes = "Test Drive unconfirmed by " + crs.getString("updateemp") + " at " + strToLongDate(ToLongDate(kknow())) + ", ";
							}

							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
									+ " SET "
									+ " testdrive_confirmed = '0', "
									+ " testdrive_notes = concat('" + testdrive_notes + "',testdrive_notes)"
									+ " where testdrive_id =" + testdrive_id + "";
							updateQuery(StrSql);
							// response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive unconfirmed successfully!"));

						} else {
							// response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Permission denied!"));
						}
						crs.close();
					} else {
						// response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive unconfirmed successfully!"));
					}
				}

				// if ("yes".equals(update)) {
				// if (!"yes".equals(updateB) && !"Delete Test Drive".equals(deleteB)) {
				// // PopulateFields(response);
				// } else if ("yes".equals(updateB) && !"Delete Test Drive".equals(deleteB)) {
				// if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
				// GetValues(request, response);
				// testdrive_modified_id = CNumeric(GetSession("emp_id", request));
				// testdrive_modified_date = ToLongDate(kknow());
				// UpdateFields();
				// if (!msg.equals("")) {
				// msg = "Error!" + msg;
				// } else {
				// response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?testdrive_id=" + testdrive_id + "&msg=Test Drive details updated successfully."));
				// }
				// } else {
				// response.sendRedirect(AccessDenied());
				// }
				// } else if ("Delete Test Drive".equals(deleteB)) {
				// if (ReturnPerm(comp_id, "emp_testdrive_delete", request).equals("1")) {
				// GetValues(request, response);
				// DeleteFields();
				// if (!msg.equals("")) {
				// msg = "Error!" + msg;
				// } else {
				// response.sendRedirect(response.encodeRedirectURL("testdrive-list.jsp?msg=Test Drive details deleted successfully."));
				// }
				// } else {
				// response.sendRedirect(AccessDenied());
				// }
				// }
				// }
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

	// public void doGet(HttpServletRequest request, HttpServletResponse response)
	// throws ServletException, IOException {
	// doPost(request, response);
	// }
	protected JSONObject GetValues(JSONObject input) throws JSONException {
		if (!input.isNull("testdrive_id")) {
			testdrive_id = CNumeric(PadQuotes((String) input.get("testdrive_id")));
		}
		if (!input.isNull("testdrive_type_id")) {
			testdrive_type_id = PadQuotes((String) input.get("testdrive_type_id"));
		}
		if (!input.isNull("testdrive_veh_id")) {
			testdrive_veh_id = CNumeric(PadQuotes((String) input.get("testdrive_veh_id")));
		}
		if (!input.isNull("testdrive_emp_id")) {
			testdrive_emp_id = CNumeric(PadQuotes((String) input.get("testdrive_emp_id")));
		}
		if (!input.isNull("testdrive_location_id")) {
			testdrive_location_id = CNumeric(PadQuotes((String) input.get("testdrive_location_id")));
		}
		if (!input.isNull("testdrive_time")) {
			testdrive_time = PadQuotes((String) input.get("testdrive_time"));
		}
		if (!input.isNull("testdrive_confirmed")) {
			testdrive_confirmed = PadQuotes((String) input.get("testdrive_confirmed"));
		}
		if (!input.isNull("model_name")) {
			model_name = PadQuotes((String) input.get("model_name"));
		}
		testdrive_confirmed = "1";
		if (!input.isNull("testdrive_notes")) {
			testdrive_notes = PadQuotes((String) input.get("testdrive_notes"));
		}
		return output;
	}

	protected void CheckForm() {
		msg = "";
		StrSql = "select location_leadtime, location_testdrive_dur "
				+ " from " + compdb(comp_id) + "axela_sales_testdrive_location "
				+ " where location_id=" + testdrive_location_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Date start = AddHoursDate(StringToDate(ConvertLongDateToStr(testdrive_time)), 0, 0, -crs.getDouble("location_leadtime"));
				testdrive_time_from = ToLongDate(start);
				Date end = AddHoursDate(StringToDate(ConvertLongDateToStr(testdrive_time)), 0, 0, crs.getDouble("location_testdrive_dur"));
				testdrive_time_to = ToLongDate(end);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("error in cal of start and end time---" + ex);
		}
		if (model_name.equals("")) {
			msg = msg + "<br>Select Model!";
		}
		if (testdrive_veh_id.equals("0")) {
			msg = msg + "<br>Select Vehicle!";
		}
		if (testdrive_emp_id.equals("0")) {
			msg = msg + "<br>Select Executive!";
		}
		if (testdrive_time.equals("")) {
			msg = msg + "<br>Select Test Drive Date!";
		} else {
			if (isValidDateFormatLong(testdrive_time)) {
				if (!testdrive_time_from.equals("") && !testdrive_time_to.equals("")) {
					if (testdrive_confirmed.equals("1")) {
						StrSql = "select testdrive_id from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_confirmed='1' and testdrive_veh_id=" + testdrive_veh_id + " and "
								+ " ((testdrive_time_from >= " + testdrive_time_from + " and testdrive_time_from < " + testdrive_time_to + ")"
								+ " or (testdrive_time_to > " + testdrive_time_from + " and testdrive_time_to <= " + testdrive_time_to + ") "
								+ " or (testdrive_time_from >= " + testdrive_time_from + " and testdrive_time_to <= " + testdrive_time_to + ") "
								+ " or (testdrive_time_from <= " + testdrive_time_from + " and testdrive_time_to >= " + testdrive_time_to + "))";
						if (!update.equals("")) {
							StrSql = StrSql + " and testdrive_id!=" + testdrive_id;
						}
						if (!ExecuteQuery(StrSql).equals("")) {
							msg = msg + "<br>Vehicle is occupied by other Test Drives!";
						}
						StrSql = "SELECT outage_veh_id, outage_fromtime, outage_totime FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle_outage"
								+ " where outage_veh_id=" + testdrive_veh_id + " and "
								+ "((outage_fromtime >= " + testdrive_time_from + " and outage_fromtime < " + testdrive_time_to + ") "
								+ " or (outage_totime > " + testdrive_time_from + " and outage_totime <= " + testdrive_time_to + ") "
								+ " or (outage_fromtime < " + testdrive_time_from + " and outage_totime > " + testdrive_time_to + "))";
						if (!ExecuteQuery(StrSql).equals("")) {
							msg = msg + "<br>Vehicle outage!";
						}
					}
				}
			} else {
				msg = msg + "<br>Enter Valid Test Drive Date!";
			}
		}
		if (testdrive_location_id.equals("0")) {
			msg = msg + "<br>Select Location!";
		}
		if (testdrive_type_id.equals("0")) {
			msg = msg + "<br>Select Test Drive Type!";
		}
		if (testdrive_notes.length() > 8000) {
			testdrive_notes = testdrive_notes.substring(0, 7999);
		}
	}

	protected void AddFields() throws SQLException, JSONException {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				// testdrive_id = ExecuteQuery("Select coalesce(max(testdrive_id),0)+1 as testdrive_id from " + compdb(comp_id) + "axela_sales_testdrive");
				StrSql = "insert into " + compdb(comp_id) + "axela_sales_testdrive"
						+ "("
						// + "testdrive_id,"
						+ "testdrive_enquiry_id,"
						+ "testdrive_veh_id, "
						+ "testdrive_emp_id, "
						+ "testdrive_location_id,"
						+ "testdrive_type,"
						+ "testdrive_time,"
						+ "testdrive_time_from,"
						+ "testdrive_time_to,"
						+ "testdrive_confirmed,"
						+ "testdrive_notes,"
						+ "testdrive_entry_id,"
						+ "testdrive_entry_date,"
						+ "testdrive_modified_id,"
						+ "testdrive_modified_date"
						+ ") "
						+ "values	"
						+ "("
						// + "" + testdrive_id + ","
						+ "" + enquiry_id + ","
						+ "" + testdrive_veh_id + ","
						+ "" + testdrive_emp_id + ","
						+ "" + testdrive_location_id + ","
						+ "" + testdrive_type_id + ","
						+ "'" + ConvertLongDateToStr(testdrive_time) + "',"
						+ "'" + testdrive_time_from + "',"
						+ "'" + testdrive_time_to + "',"
						+ "'" + testdrive_confirmed + "',"
						+ "'" + testdrive_notes + "',"
						+ "'" + testdrive_entry_id + "',"
						+ "'" + testdrive_entry_date + "',"
						+ "0,"
						+ "''"
						+ ")";
				// SOP("s---"+StrSql);
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					testdrive_id = rs.getString(1);
				}
				rs.close();
				StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry set enquiry_stage_id=3 where enquiry_id=" + enquiry_id;
				stmttx.execute(StrSql);

				conntx.commit();
				msg = "Test Drive Added Successfully!";
				output.put("testdrive_id", testdrive_id);
				output.put("msg", msg);
				// SOP("Transaction commit...");
			} catch (Exception e) {
				conntx.rollback();
				SOPError("Axelaauto ==" + this.getClass().getName());
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
		} else {
			output.put("msg", "Error!" + msg);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
						+ " SET "
						+ "testdrive_veh_id= '" + testdrive_veh_id + "', "
						+ "testdrive_emp_id = '" + testdrive_emp_id + "', "
						+ "testdrive_location_id = '" + testdrive_location_id + "', "
						+ "testdrive_time = '" + testdrive_time + "', "
						+ "testdrive_time_from = '" + testdrive_time_from + "', "
						+ "testdrive_time_to = '" + testdrive_time_to + "', "
						+ "testdrive_type = '" + testdrive_type_id + "', "
						+ "testdrive_confirmed = '" + testdrive_confirmed + "', "
						+ "testdrive_notes = '" + testdrive_notes + "', "
						+ "testdrive_modified_id = '" + testdrive_modified_id + "', "
						+ "testdrive_modified_date = '" + testdrive_modified_date + "' "
						+ "where testdrive_id = " + testdrive_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("") && chkpermmsg.equals("")) {
			try {
				StrSql = "Select testdrive_doc_value from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_id = " + testdrive_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(TestDriveDocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive_colour_trans where trans_testdrive_id =" + testdrive_id + "";
				updateQuery(StrSql);
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_id =" + testdrive_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected JSONObject PopulateFields(JSONObject input) {
		try {
			StrSql = "Select testdrive_veh_id, testdrive_emp_id,testdrive_time, testdrive_time_from, testdrive_type,"
					+ " testdrive_time_to, testdrive_location_id, testdrive_confirmed, testdrive_notes, testdrive_entry_id,"
					+ " testdrive_entry_date, testdrive_modified_id, testdrive_modified_date "
					+ " from " + compdb(comp_id) + "axela_sales_testdrive"
					+ " where testdrive_id=" + testdrive_id + "";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					testdrive_veh_id = crs.getString("testdrive_veh_id");
					testdrive_emp_id = crs.getString("testdrive_emp_id");
					testdrive_time = crs.getString("testdrive_time");
					testdrive_time_from = crs.getString("testdrive_time_from");
					testdrive_time_to = crs.getString("testdrive_time_to");
					// testdrivedate = SplitDate(testdrive_time) + "/" + SplitMonth(testdrive_time) + "/" + SplitYear(testdrive_time) + " " + SplitHourMin(testdrive_time);
					Date stdate = StringToDate(testdrive_time_from);
					Date eddate = StringToDate(testdrive_time_to);
					testdrive_location_id = crs.getString("testdrive_location_id");
					testdrive_type_id = crs.getString("testdrive_type");
					testdrive_confirmed = crs.getString("testdrive_confirmed");
					testdrive_notes = crs.getString("testdrive_notes");
					testdrive_entry_id = crs.getString("testdrive_entry_id");
					entry_by = Exename(comp_id, crs.getInt("testdrive_entry_id"));
					entry_date = strToLongDate(crs.getString("testdrive_entry_date"));
					testdrive_modified_id = crs.getString("testdrive_modified_id");
					if (!testdrive_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(testdrive_modified_id));
						modified_date = strToLongDate(crs.getString("testdrive_modified_date"));
					}
				}
			} else {
				// response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Test Drive"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateVehicle() throws JSONException {
		CachedRowSet crs = null;
		try {
			StrSql = "select veh_id, veh_name"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_vehicle "
					+ " inner join " + compdb(comp_id) + "axela_inventory_item on item_id=veh_item_id "
					+ " where veh_branch_id= " + CNumeric(enquiry_branch_id) + ""
					+ " and item_model_id=" + CNumeric(enquiry_model_id) + ""
					+ " and veh_active=1 "
					+ " group by veh_id"
					+ " order by veh_name";
			// SOP("StrSql ==vehicle==== " + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("veh_id", "0");
				map.put("veh_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("veh_id", crs.getString("veh_id"));
					map.put("veh_name", crs.getString("veh_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("veh_id", "0");
				map.put("veh_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatevehicle", list);
			list.clear();
			crs.close();
			return output;
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
	}

	public JSONObject PopulateLocation() {
		CachedRowSet crs = null;
		try {
			StrSql = " SELECT location_name, location_id"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_location "
					+ " where location_active='1' "
					+ " and location_branch_id= " + CNumeric(enquiry_branch_id) + " "
					+ " order by location_name ";
			// SOP("StrSql ==location==== " + StrSqlBreaker(StrSql));
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("location_id", "0");
				map.put("location_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("location_id", crs.getString("location_id"));
					map.put("location_name", crs.getString("location_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("location_id", "0");
				map.put("location_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatelocation", list);
			list.clear();
			crs.close();
			return output;
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
	}

	public JSONObject PopulateTestDriveType() throws JSONException {
		String result = "";
		StrSql = "SELECT testdrive_id\n"
				+ "FROM " + compdb(comp_id) + "axela_sales_testdrive\n"
				+ "WHERE testdrive_enquiry_id = " + enquiry_id + "\n"
				+ "AND testdrive_fb_taken = '1'";
		// SOP("StrSql = " + StrSql);
		result = ExecuteQuery(StrSql);
		if (result.equals("")) {
			output.put("testdrive_type_id", "1");
			output.put("testdrive_type_name", "Main Test Drive");
		} else {
			output.put("testdrive_type_id", "2");
			output.put("testdrive_type_name", "Alternate Test Drive");
		}
		return output;
	}

	protected JSONObject EnquiryDetails(JSONObject input) throws JSONException {
		if (!input.isNull("enquiry_id")) {
			enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
		}
		try {
			if (!enquiry_id.equals("0")) {
				StrSql = "Select customer_id, customer_name, enquiry_branch_id, "
						+ " customer_email1, customer_address, customer_pin, enquiry_date, "
						+ " concat('ENQ',branch_code,enquiry_no) as enquiry_no, branch_code,  "
						+ " enquiry_model_id, coalesce(model_name, '') as model_name, "
						+ " enquiry_emp_id, concat(emp_name, ' (', emp_ref_no, ')') as emp_name  "
						+ " from " + compdb(comp_id) + "axela_customer  "
						+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_customer_id=customer_id  "
						+ " left join " + compdb(comp_id) + "axela_emp on emp_id=enquiry_emp_id "
						+ " left join " + compdb(comp_id) + "axela_inventory_item_model on model_id=enquiry_model_id  "
						+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=enquiry_branch_id"
						+ " where enquiry_id = " + CNumeric(enquiry_id) + BranchAccess + " ";
				// SOP("StrSql(11)==="+StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						customer_name = crs.getString("customer_name");
						customer_address = crs.getString("customer_address");
						customer_pin = crs.getString("customer_pin");
						model_name = crs.getString("model_name");
						enquiry_branch_id = crs.getString("enquiry_branch_id");
						enquiry_model_id = crs.getString("enquiry_model_id");
						enquiry_date = crs.getString("enquiry_date");
						enquiry_no = crs.getString("enquiry_no");
						testdrive_emp_id = crs.getString("enquiry_emp_id");
						executive_name = crs.getString("emp_name");
						map.put("customer_name", crs.getString("customer_name") + " (" + crs.getString("customer_id") + ")");
						map.put("model_name", crs.getString("model_name"));
						map.put("enquiry_branch_id", crs.getString("enquiry_branch_id"));
						map.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
						map.put("enquiry_no", crs.getString("enquiry_no"));
						map.put("testdrive_emp_id", testdrive_emp_id);
						map.put("emp_name", crs.getString("emp_name"));
						list.add(gson.toJson(map));
					}
					map.clear();
					output.put("enquirydetails", list);
					list.clear();

					msg = "";
					if (enquiry_model_id.equals("0")) {
						msg = msg + "<br>Select Model for the Enquiry!";
					}
					if (customer_address.equals("")) {
						msg = msg + "<br>Full Address not updated for this Enquiry!";
					}
					if (customer_pin.equals("")) {
						msg = msg + "<br>Address Pin Code not updated for this Enquiry!";
					}
					if (testdrive_emp_id.equals("0")) {
						msg = msg + "<br>Select Execuitve for the Enquiry!";
					}
					if (!msg.equals("")) {
						output.put("accessmsg", msg);
					} else {
						output.put("accessmsg", "");
					}
				} else {
					output.put("accessmsg", "Invalid Enquiry!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
