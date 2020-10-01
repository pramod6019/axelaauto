// Ved Prakash (13 Feb 2013)
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTestDriveVehicle_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String testdriveveh_id = "0";
	public String testdriveveh_name = "";
	public String testdriveveh_type_id = "0";
	public String testdriveveh_model_id = "0";
	public String testdriveveh_branch_id = "0", branch_name = "";
	public String testdriveveh_regno = "";
	public String testdriveveh_service_start_date = "";
	public String testdriveveh_service_end_date = "";
	public String testdriveveh_active = "";
	public String testdriveveh_notes = "";
	public String testdriveveh_entry_id = "";
	public String testdriveveh_entry_date = "";
	public String testdriveveh_modified_id = "";
	public String testdriveveh_modified_date = "";
	public String testdriveveh_item_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_testdrive_edit", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				testdriveveh_id = CNumeric(PadQuotes(request.getParameter("testdriveveh_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						testdriveveh_active = "1";
					} else {
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							GetValues(request, response);
							testdriveveh_entry_id = emp_id;
							testdriveveh_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managetestdrivevehicle.jsp?testdriveveh_id=" + testdriveveh_id + "&msg=Vehicle Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Vehicle".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Vehicle".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							GetValues(request, response);
							testdriveveh_modified_id = emp_id;
							testdriveveh_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managetestdrivevehicle.jsp?testdriveveh_id=" + testdriveveh_id + "&msg=Vehicle Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Vehicle".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_testdrive_edit", request).equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("managetestdrivevehicle.jsp?all=yes"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		testdriveveh_id = CNumeric(PadQuotes(request.getParameter("testdriveveh_id")));
		testdriveveh_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		testdriveveh_type_id = CNumeric(PadQuotes(request.getParameter("dr_testdriveveh_type_id")));
		if (testdriveveh_type_id.equals("1")) {
			testdriveveh_model_id = CNumeric(PadQuotes(request.getParameter("dr_model_id")));
			testdriveveh_item_id = CNumeric(PadQuotes(request.getParameter("dr_item_id")));
		} else {
			testdriveveh_model_id = "0";
			testdriveveh_item_id = "0";
		}
		testdriveveh_name = PadQuotes(request.getParameter("txt_testdriveveh_name"));
		testdriveveh_regno = PadQuotes(request.getParameter("txt_testdriveveh_regno"));
		testdriveveh_service_start_date = PadQuotes(request.getParameter("txt_testdriveveh_service_start_date"));
		testdriveveh_service_end_date = PadQuotes(request.getParameter("txt_testdriveveh_service_end_date"));
		testdriveveh_active = PadQuotes(request.getParameter("chk_testdriveveh_active"));
		if (testdriveveh_active.equals("on")) {
			testdriveveh_active = "1";
		} else {
			testdriveveh_active = "0";
		}
		testdriveveh_notes = PadQuotes(request.getParameter("txt_testdriveveh_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (testdriveveh_branch_id.equals("0")) {
			msg = msg + "<br>Select a Branch!";
		}
		if (testdriveveh_name.equals("")) {
			msg = msg + "<br>Enter Vehicle Name!";
		} else {
			try {
				StrSql = "Select testdriveveh_name from " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
						+ " where testdriveveh_name = '" + testdriveveh_name + "'"
						+ " and testdriveveh_branch_id = " + testdriveveh_branch_id + "";
				if (update.equals("yes")) {
					StrSql += " and testdriveveh_id != " + testdriveveh_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Vehicle Found!";
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (testdriveveh_type_id.equals("1")) {
			if (testdriveveh_model_id.equals("0")) {
				msg = msg + "<br>Select a Model!";
			}
			if (testdriveveh_item_id.equals("0")) {
				msg = msg + "<br>Select a Product!";
			}
		}
		if (!testdriveveh_regno.equals("")) // similar Name found
		{
			try {
				StrSql = "select testdriveveh_regno from " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
						+ " where testdriveveh_regno = '" + testdriveveh_regno + "' and testdriveveh_branch_id = " + testdriveveh_branch_id + "";
				if (update.equals("yes")) {
					StrSql += " and testdriveveh_id != " + testdriveveh_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Registration No. Found!";
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (testdriveveh_service_start_date.equals("")) {
			msg = msg + "<br>Select Start Date!";
		}
		if (!testdriveveh_service_start_date.equals("") && !isValidDateFormatShort(testdriveveh_service_start_date)) {
			msg = msg + "<br>Enter valid Service Start Date!";
		}
		if (!testdriveveh_service_end_date.equals("") && !isValidDateFormatShort(testdriveveh_service_end_date)) {
			msg = msg + "<br>Enter valid Service End Date!";
		}
		if (!testdriveveh_service_start_date.equals("") && isValidDateFormatShort(testdriveveh_service_start_date) && !testdriveveh_service_end_date.equals("")
				&& isValidDateFormatShort(testdriveveh_service_end_date)) {
			long campaign_sdate = Long.parseLong(ConvertShortDateToStr(testdriveveh_service_start_date));
			long campaign_edate = Long.parseLong(ConvertShortDateToStr(testdriveveh_service_end_date));
			if (campaign_sdate > campaign_edate) {
				msg = msg + "<br>End Date should be greater than Start Date!";
			}
		}
		if (testdriveveh_notes.length() > 8000) {
			testdriveveh_notes = testdriveveh_notes.substring(0, 7999);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				testdriveveh_id = ExecuteQuery("Select (coalesce(max(testdriveveh_id),0)+1) from " + compdb(comp_id) + "axela_sales_testdrive_vehicle");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
						+ " (testdriveveh_id,"
						+ " testdriveveh_type_id,"
						+ " testdriveveh_branch_id,"
						+ " testdriveveh_item_id,"
						+ " testdriveveh_name,"
						+ " testdriveveh_regno,"
						+ " testdriveveh_service_start_date,"
						+ " testdriveveh_service_end_date,"
						+ " testdriveveh_active,"
						+ " testdriveveh_notes,"
						+ " testdriveveh_entry_id,"
						+ " testdriveveh_entry_date)"
						+ " values"
						+ " (" + testdriveveh_id + ","
						+ " " + testdriveveh_type_id + ","
						+ " " + testdriveveh_branch_id + ","
						+ " " + testdriveveh_item_id + ","
						+ " '" + testdriveveh_name + "',"
						+ " '" + testdriveveh_regno + "',"
						+ " '" + ConvertShortDateToStr(testdriveveh_service_start_date) + "',"
						+ " '" + ConvertShortDateToStr(testdriveveh_service_end_date) + "',"
						+ " '" + testdriveveh_active + "',"
						+ " '" + testdriveveh_notes + "',"
						+ "'" + testdriveveh_entry_id + "',"
						+ "'" + testdriveveh_entry_date + "')";
				// SOP(StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT testdriveveh_id, testdriveveh_type_id, testdriveveh_name, testdriveveh_branch_id,"
					+ " branch_name, branch_code, testdriveveh_item_id, testdriveveh_regno,"
					+ " testdriveveh_service_start_date, testdriveveh_service_end_date, testdriveveh_active,"
					+ " testdriveveh_notes, item_model_id, testdriveveh_entry_id, testdriveveh_entry_date,"
					+ " COALESCE(testdriveveh_modified_id, '0') AS testdriveveh_modified_id,"
					+ " COALESCE(testdriveveh_modified_date, '') AS testdriveveh_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = testdriveveh_branch_id"
					+ " WHERE testdriveveh_id = " + testdriveveh_id + "";
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst() && !testdriveveh_id.equals("0")) {
				while (crs.next()) {
					testdriveveh_type_id = crs.getString("testdriveveh_type_id");
					testdriveveh_branch_id = crs.getString("testdriveveh_branch_id");
					branch_name = crs.getString("branch_name") + " (" + crs.getString("branch_code") + ")";
					testdriveveh_item_id = crs.getString("testdriveveh_item_id");
					testdriveveh_model_id = crs.getString("item_model_id");
					testdriveveh_name = crs.getString("testdriveveh_name");
					testdriveveh_regno = crs.getString("testdriveveh_regno");
					testdriveveh_service_start_date = strToShortDate(crs.getString("testdriveveh_service_start_date"));
					testdriveveh_service_end_date = strToShortDate(crs.getString("testdriveveh_service_end_date"));
					testdriveveh_active = crs.getString("testdriveveh_active");
					testdriveveh_notes = crs.getString("testdriveveh_notes");
					testdriveveh_entry_id = crs.getString("testdriveveh_entry_id");
					entry_by = Exename(comp_id, crs.getInt("testdriveveh_entry_id"));
					entry_date = strToLongDate(crs.getString("testdriveveh_entry_date"));
					testdriveveh_modified_id = crs.getString("testdriveveh_modified_id");
					if (!testdriveveh_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(testdriveveh_modified_id));
						modified_date = strToLongDate(crs.getString("testdriveveh_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_vehicle"
						+ " SET"
						+ " testdriveveh_type_id = " + testdriveveh_type_id + ","
						+ " testdriveveh_branch_id= " + testdriveveh_branch_id + ","
						+ " testdriveveh_item_id= " + testdriveveh_item_id + ","
						+ " testdriveveh_name= '" + testdriveveh_name + "',"
						+ " testdriveveh_regno= '" + testdriveveh_regno + "',"
						+ " testdriveveh_service_start_date= '" + ConvertShortDateToStr(testdriveveh_service_start_date) + "',"
						+ " testdriveveh_service_end_date= '" + ConvertShortDateToStr(testdriveveh_service_end_date) + "',"
						+ " testdriveveh_active= '" + testdriveveh_active + "',"
						+ " testdriveveh_notes= '" + testdriveveh_notes + "',"
						+ " testdriveveh_modified_id = '" + testdriveveh_modified_id + "',"
						+ " testdriveveh_modified_date = '" + testdriveveh_modified_date + "'"
						+ " where testdriveveh_id = " + testdriveveh_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void DeleteFields() {
		StrSql = "Select count(testdrive_id) from " + compdb(comp_id) + "axela_sales_testdrive where testdrive_testdrivetestdriveveh_id = " + testdriveveh_id;
		if (!ExecuteQuery(StrSql).equals("0")) {
			msg = "<br>Vehicle is Associated with a Test Drive(s)!";
		}
		StrSql = "select salesgatepass_testdriveveh_id from " + compdb(comp_id) + "axela_sales_testdrive_gatepass where salesgatepass_testdriveveh_id = " + testdriveveh_id;
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = "<br>Vehicle is Associated with a Test Drive(s)!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive_vehicle where testdriveveh_id = " + testdriveveh_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select model_id, model_name"
					+ " from " + compdb(comp_id) + "axela_inventory_item_model"
					+ " group by model_id"
					+ " order by model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP(StrSqlBreaker(StrSql));
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), testdriveveh_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateProduct() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id,"
					+ " if(item_code != '', concat(item_name, '(',item_code,')'), item_name) as item_name,"
					+ " item_code"
					+ " from " + compdb(comp_id) + "axela_inventory_item"
					+ " where item_model_id = " + testdriveveh_model_id + ""
					+ " and item_model_id != 0 and item_type_id = 1";
			if (add.equals("yes")) {
				StrSql += " AND item_active = 1";
			}
			StrSql += " group by item_id"
					+ " order by item_name";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_item_id class=form-control><option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(Selectdrop(crs.getInt("item_id"), testdriveveh_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateVehicleType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=1").append(StrSelectdrop("1", testdriveveh_type_id)).append(">Test Drive</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", testdriveveh_type_id)).append(">Others</option>\n");
		return Str.toString();
	}
}
