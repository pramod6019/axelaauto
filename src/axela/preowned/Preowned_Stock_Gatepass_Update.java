package axela.preowned;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;

public class Preowned_Stock_Gatepass_Update extends Connect {

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
	public String emp_role_id = "0";
	public String preownedstock_id = "0";
	public String preownedstockgatepass_id = "0";
	public String preowned_branch_id = "0";
	public String preowned_variant_id = "0";
	public String branch_name = "";
	public String variant_name = "";
	public String preownedstock_chassis_no = "";
	public String preownedstockgatepass_time = "";
	public String preownedstockgatepass_from_location_id = "0";
	public String preownedstockgatepass_from_location_name = "0";
	public String preownedstockgatepass_to_location_id = "0";
	public String preownedstockgatepass_driver_id = "0";
	public String preownedstockgatepass_notes = "";
	public String preownedstockgatepass_entry_id = "0";
	public String preownedstockgatepass_entry_date = "";
	public String preownedstockgatepass_entry_by = "";
	public String entry_date = "";
	public String preownedstockgatepass_modified_id = "0";
	public String preownedstockgatepass_modified_date = "";
	public String modified_date = "";
	public String preownedstockgatepass_modified_by = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_preowned_stock_access", request, response);
				preownedstockgatepass_id = CNumeric(PadQuotes(request.getParameter("preownedstockgatepass_id")));
				// preownedstockgatepass_id = "374";
				preownedstock_id = CNumeric(PadQuotes(request.getParameter("preownedstock_id")));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						GetStockDetails(response);
						preownedstockgatepass_time = ToLongDate(kknow());
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_stock_add", request).equals("1")) {
							preownedstockgatepass_entry_id = emp_id;
							preownedstockgatepass_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Stock Gate Pass added successfully!";
								response.sendRedirect(response.encodeRedirectURL("preowned-stock-gatepass-list.jsp?preownedstock_id=" + preownedstock_id + "&preownedstockgatepass_id="
										+ preownedstockgatepass_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Gate Pass")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Gate Pass")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_stock_edit", request).equals("1")) {
							preownedstockgatepass_modified_id = emp_id;
							preownedstockgatepass_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Stock Gate Pass updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("preowned-stock-gatepass-list.jsp?preownedstock_id=" + preownedstock_id + "&preownedstockgatepass_id="
										+ preownedstockgatepass_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Gate Pass")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_preowned_stock_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Stock Gate Pass deleted successfully!";
								response.sendRedirect(response.encodeRedirectURL("preowned-stock-gatepass-list.jsp?preownedstock_id=" + preownedstock_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		preownedstock_id = CNumeric(PadQuotes(request.getParameter("txt_preownedstock_id")));
		preowned_branch_id = CNumeric(PadQuotes(request.getParameter("txt_preowned_branch_id")));
		preowned_variant_id = CNumeric(PadQuotes(request.getParameter("txt_preowned_variant_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		variant_name = PadQuotes(request.getParameter("txt_variant_name"));
		preownedstock_chassis_no = PadQuotes(request.getParameter("txt_preownedstock_chassis_no"));
		preownedstockgatepass_time = PadQuotes(request.getParameter("txt_preownedstockgatepass_time"));
		preownedstockgatepass_from_location_id = CNumeric(PadQuotes(request.getParameter("txt_preownedstockgatepass_from_location_id")));
		preownedstockgatepass_from_location_name = PadQuotes(request.getParameter("txt_preownedstockgatepass_from_location_name"));
		preownedstockgatepass_to_location_id = PadQuotes(request.getParameter("dr_preownedstockgatepass_to_location_id"));
		preownedstockgatepass_driver_id = PadQuotes(request.getParameter("dr_preownedstockgatepass_driver_id"));
		preownedstockgatepass_notes = PadQuotes(request.getParameter("txt_preownedstockgatepass_notes"));
		if (preownedstockgatepass_notes.length() > 5000) {
			preownedstockgatepass_notes = preownedstockgatepass_notes.substring(0, 4999);
		}
		preownedstockgatepass_entry_by = PadQuotes(request.getParameter("preownedstockgatepass_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		preownedstockgatepass_modified_by = PadQuotes(request.getParameter("preownedstockgatepass_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	public void CheckForm() {
		msg = "";
		if (preownedstockgatepass_to_location_id.equals("0")) {
			msg += "<br>Select To Location!";
		}

		if (preownedstockgatepass_driver_id.equals("0")) {
			msg += "<br>Select Driver!";
		}
	}

	public void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_stock_gatepass"
					+ " (preownedstockgatepass_preownedstock_id,"
					+ " preownedstockgatepass_time,"
					+ " preownedstockgatepass_from_location_id,"
					+ " preownedstockgatepass_to_location_id,"
					+ " preownedstockgatepass_driver_id,"
					+ " preownedstockgatepass_notes,"
					+ " preownedstockgatepass_entry_id,"
					+ " preownedstockgatepass_entry_date)"
					+ " VALUES"
					+ " (" + preownedstock_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + preownedstockgatepass_from_location_id + ","
					+ " " + preownedstockgatepass_to_location_id + ","
					+ " " + preownedstockgatepass_driver_id + ","
					+ " '" + preownedstockgatepass_notes + "',"
					+ " " + preownedstockgatepass_entry_id + ","
					+ " '" + preownedstockgatepass_entry_date + "')";
			preownedstockgatepass_id = UpdateQueryReturnID(StrSql);
			SOP("preownedstockgatepass_notes=222222==" + preownedstockgatepass_notes);
			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_stock"
					+ " SET"
					+ " preownedstock_preownedlocation_id = " + preownedstockgatepass_to_location_id + ""
					+ " WHERE preownedstock_id = " + preownedstock_id + "";
			updateQuery(StrSql);
		}
	}

	protected void GetStockDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT preownedstock_preownedlocation_id, preowned_branch_id, variant_name,"
					+ " preownedlocation_name, variant_id, preownedstock_chassis_no,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location ON preownedlocation_id = preownedstock_preownedlocation_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " WHERE preownedstock_id = " + preownedstock_id + "";
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preowned_variant_id = crs.getString("variant_id");
					variant_name = crs.getString("variant_name");
					preownedstock_chassis_no = crs.getString("preownedstock_chassis_no");
					preowned_branch_id = crs.getString("preowned_branch_id");
					preownedstockgatepass_from_location_id = crs.getString("preownedstock_preownedlocation_id");
					preownedstockgatepass_from_location_name = crs.getString("preownedlocation_name");
					branch_name = crs.getString("branch_name");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Stock!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT preownedstockgatepass_preownedstock_id, preownedstockgatepass_time,"
					+ " preowned_branch_id, preownedstockgatepass_from_location_id,"
					+ " preownedstockgatepass_to_location_id, preownedstock_chassis_no,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " preownedstockgatepass_driver_id, preownedstockgatepass_notes,"
					+ " preownedlocation_name, variant_id, variant_name,"
					+ " preownedstockgatepass_entry_id, preownedstockgatepass_entry_date,"
					+ " preownedstockgatepass_modified_id, preownedstockgatepass_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock_gatepass"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = preownedstockgatepass_preownedstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location ON preownedlocation_id = preownedstockgatepass_from_location_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " WHERE preownedstockgatepass_id = " + preownedstockgatepass_id + "";
			// SOP("StrSql = " + StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preownedstock_id = crs.getString("preownedstockgatepass_preownedstock_id");
					preowned_variant_id = crs.getString("variant_id");
					variant_name = crs.getString("variant_name");
					preownedstock_chassis_no = crs.getString("preownedstock_chassis_no");
					preowned_branch_id = crs.getString("preowned_branch_id");
					preownedstockgatepass_time = crs.getString("preownedstockgatepass_time");
					preownedstockgatepass_from_location_id = crs.getString("preownedstockgatepass_from_location_id");
					preownedstockgatepass_from_location_name = crs.getString("preownedlocation_name");
					preownedstockgatepass_to_location_id = crs.getString("preownedstockgatepass_to_location_id");
					preownedstockgatepass_driver_id = crs.getString("preownedstockgatepass_driver_id");
					branch_name = crs.getString("branch_name");
					preownedstockgatepass_entry_id = crs.getString("preownedstockgatepass_entry_id");
					preownedstockgatepass_notes = crs.getString("preownedstockgatepass_notes");
					if (!preownedstockgatepass_entry_id.equals("0")) {
						preownedstockgatepass_entry_by = Exename(comp_id, Integer.parseInt(preownedstockgatepass_entry_id));
					}

					entry_date = strToLongDate(crs.getString("preownedstockgatepass_entry_date"));
					preownedstockgatepass_modified_id = crs.getString("preownedstockgatepass_modified_id");
					if (!preownedstockgatepass_modified_id.equals("0")) {
						preownedstockgatepass_modified_by = Exename(comp_id, Integer.parseInt(preownedstockgatepass_modified_id));
						modified_date = strToLongDate(crs.getString("preownedstockgatepass_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Stock Gate Pass!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_stock_gatepass"
					+ " SET"
					+ " preownedstockgatepass_to_location_id = " + preownedstockgatepass_to_location_id + ","
					+ " preownedstockgatepass_driver_id = " + preownedstockgatepass_driver_id + ","
					+ " preownedstockgatepass_notes = '" + preownedstockgatepass_notes + "',"
					+ " preownedstockgatepass_modified_id = " + preownedstockgatepass_modified_id + ","
					+ " preownedstockgatepass_modified_date = '" + preownedstockgatepass_modified_date + "'"
					+ " WHERE preownedstockgatepass_id = " + preownedstockgatepass_id + "";
			updateQuery(StrSql);

			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_stock"
					+ " SET"
					+ " preownedstock_preownedlocation_id = " + preownedstockgatepass_to_location_id + ""
					+ " WHERE preownedstock_id = " + preownedstock_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_stock_gatepass"
					+ " WHERE preownedstockgatepass_id = " + preownedstockgatepass_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulatePreownedLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlocation_id, preownedlocation_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_location"
					+ " WHERE preownedlocation_branch_id = " + preowned_branch_id + ""
					+ " AND preownedlocation_id != " + preownedstockgatepass_from_location_id + ""
					+ " ORDER BY preownedlocation_name";
			// SOP("StrSql----------to------"+StrSql);
			CachedRowSet crs =processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlocation_id"));
				Str.append(StrSelectdrop(crs.getString("preownedlocation_id"), preownedstockgatepass_to_location_id));
				Str.append(">").append(crs.getString("preownedlocation_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedDriver() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT driver_id, driver_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_driver"
					+ " WHERE driver_active = 1"
					+ " ORDER BY driver_name";
			CachedRowSet crs =processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("driver_id"));
				Str.append(StrSelectdrop(crs.getString("driver_id"), preownedstockgatepass_driver_id));
				Str.append(">").append(crs.getString("driver_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
