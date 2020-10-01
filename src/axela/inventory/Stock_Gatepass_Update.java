package axela.inventory;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Stock_Gatepass_Update extends Connect {
	
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
	public String vehstock_id = "0";
	public String vehstockgatepass_id = "0";
	public String vehstock_branch_id = "0";
	public String brand_id = "0";
	public String vehstock_item_id = "0";
	public String branch_name = "";
	public String item_name = "";
	public String vehstock_chassis_no = "";
	public String vehstockgatepass_time = "";
	public String vehstockgatepass_from_location_id = "0";
	public String vehstockgatepass_from_location_name = "0";
	public String vehstockgatepass_to_location_id = "0";
	public String vehstockgatepass_stockdriver_id = "0", vehstockgatepass_out_kms = "0";
	public String vehstockgatepass_notes = "";
	public String vehstockgatepass_entry_id = "0";
	public String vehstockgatepass_entry_date = "";
	public String vehstockgatepass_entry_by = "";
	public String entry_date = "";
	public String vehstockgatepass_modified_id = "0";
	public String vehstockgatepass_modified_date = "";
	public String modified_date = "";
	public String vehstockgatepass_modified_by = "";
	public String QueryString = "";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				vehstockgatepass_id = CNumeric(PadQuotes(request.getParameter("vehstockgatepass_id")));
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
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
						vehstockgatepass_time = ToLongDate(kknow());
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							vehstockgatepass_entry_id = emp_id;
							vehstockgatepass_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Stock Gate Pass added successfully!";
								response.sendRedirect(response.encodeRedirectURL("stock-gatepass-list.jsp?vehstock_id=" + vehstock_id + "&vehstockgatepass_id=" + vehstockgatepass_id + "&msg="
										+ msg));
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
						if (ReturnPerm(comp_id, "emp_stock_add", request).equals("1")) {
							vehstockgatepass_modified_id = emp_id;
							vehstockgatepass_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Stock Gate Pass updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("stock-gatepass-list.jsp?vehstock_id=" + vehstock_id + "&vehstockgatepass_id=" + vehstockgatepass_id + "&msg="
										+ msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Gate Pass")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_stock_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Stock Gate Pass deleted successfully!";
								response.sendRedirect(response.encodeRedirectURL("stock-gatepass-list.jsp?vehstock_id=" + vehstock_id + "&msg=" + msg));
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
		vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_vehstock_id")));
		vehstock_branch_id = CNumeric(PadQuotes(request.getParameter("txt_vehstock_branch_id")));
		vehstock_item_id = CNumeric(PadQuotes(request.getParameter("txt_vehstock_item_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		item_name = PadQuotes(request.getParameter("txt_item_name"));
		vehstock_chassis_no = PadQuotes(request.getParameter("txt_vehstock_chassis_no"));
		vehstockgatepass_time = PadQuotes(request.getParameter("txt_vehstockgatepass_time"));
		vehstockgatepass_from_location_id = PadQuotes(request.getParameter("txt_vehstockgatepass_from_location_id"));
		vehstockgatepass_from_location_name = PadQuotes(request.getParameter("txt_vehstockgatepass_from_location_name"));
		vehstockgatepass_to_location_id = PadQuotes(request.getParameter("dr_vehstockgatepass_to_location_id"));
		vehstockgatepass_stockdriver_id = PadQuotes(request.getParameter("dr_vehstockgatepass_stockdriver_id"));
		vehstockgatepass_out_kms = PadQuotes(request.getParameter("txt_vehstockgatepass_out_kms"));
		vehstockgatepass_notes = PadQuotes(request.getParameter("txt_vehstockgatepass_notes"));
		if (vehstockgatepass_notes.length() > 5000) {
			vehstockgatepass_notes = vehstockgatepass_notes.substring(0, 4999);
		}
		vehstockgatepass_entry_by = PadQuotes(request.getParameter("vehstockgatepass_entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		vehstockgatepass_modified_by = PadQuotes(request.getParameter("vehstockgatepass_modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}
	
	public void CheckForm() {
		msg = "";
		if (vehstockgatepass_to_location_id.equals("0")) {
			msg += "<br>Select To Location!";
		}
		
		if (vehstockgatepass_stockdriver_id.equals("0")) {
			msg += "<br>Select Driver!";
		}
		
		if (vehstockgatepass_out_kms.equals("0")) {
			msg += "<br>Enter Out KMS!";
		}
		
		// if (status.equals("Add")) {
		// StrSql = "SELECT vehstockgatepass_from_location_id, vehstockgatepass_to_location_id "
		// + " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
		// + " WHERE vehstockgatepass_from_location_id = " + vehstockgatepass_from_location_id
		// + " AND vehstockgatepass_to_location_id = " + vehstockgatepass_to_location_id
		// + " AND vehstock_id = " + vehstock_id;
		// // SOP("StrSql===" + StrSql);
		//
		// if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
		// msg += "<br> Already stock dispatched to this location!";
		// }
		// }
		
		if (status.equals("Add")) {
			StrSql = "SELECT vehstockgatepass_id "
					+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " WHERE 1=1"
					+ " AND vehstockgatepass_in_kms = '0.00'"
					+ " AND vehstockgatepass_vehstock_id = " + vehstock_id;
			// SOP("StrSql===" + StrSql);
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				msg += "<br> In Kms is not updated for previous Gatepass!!";
			}
		} else if (status.equals("Update")) {
			StrSql = "SELECT vehstockgatepass_id "
					+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " WHERE 1=1"
					+ " AND vehstockgatepass_in_kms != '0.00'"
					+ " AND vehstockgatepass_id = " + vehstockgatepass_id;
			// SOP("StrSql===" + StrSql);
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				msg += "<br> In Kms is already updated!";
			}
			
			if (msg.equals("")) {
				StrSql = "SELECT vehstockgatepass_id "
						+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
						+ " WHERE 1=1"
						+ " AND vehstockgatepass_in_kms = '0.00'"
						+ " AND vehstockgatepass_vehstock_id = " + vehstock_id
						+ " AND vehstockgatepass_id != " + vehstockgatepass_id;
				// SOP("StrSql===" + StrSql);
				if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
					msg += "<br> In Kms is not updated for previous Gatepass!";
				}
			}
		}
	}
	
	public void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " (vehstockgatepass_vehstock_id,"
					+ " vehstockgatepass_time,"
					+ " vehstockgatepass_from_location_id,"
					+ " vehstockgatepass_to_location_id,"
					+ " vehstockgatepass_stockdriver_id,"
					+ " vehstockgatepass_out_kms,"
					+ " vehstockgatepass_notes,"
					+ " vehstockgatepass_entry_id,"
					+ " vehstockgatepass_entry_date)"
					+ " VALUES"
					+ " (" + vehstock_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + vehstockgatepass_from_location_id + ","
					+ " " + vehstockgatepass_to_location_id + ","
					+ " " + vehstockgatepass_stockdriver_id + ","
					+ " '" + vehstockgatepass_out_kms + "',"
					+ " '" + vehstockgatepass_notes + "',"
					+ " " + vehstockgatepass_entry_id + ","
					+ " '" + vehstockgatepass_entry_date + "')";
			vehstockgatepass_id = UpdateQueryReturnID(StrSql);
			
			// StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
			// + " SET"
			// + " vehstock_vehstocklocation_id = " + vehstockgatepass_to_location_id + ""
			// + " WHERE vehstock_id = " + vehstock_id + "";
			// updateQuery(StrSql);
		}
	}
	
	protected void GetStockDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT vehstock_vehstocklocation_id, vehstock_branch_id, vehstocklocation_name, item_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, branch_brand_id, vehstock_chassis_no,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstock_vehstocklocation_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " WHERE vehstock_id = " + vehstock_id + "";
			// SOP("StrSql==GetStockDetails==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehstock_item_id = crs.getString("item_id");
					item_name = crs.getString("item_name");
					vehstock_chassis_no = crs.getString("vehstock_chassis_no");
					vehstock_branch_id = crs.getString("vehstock_branch_id");
					brand_id = crs.getString("branch_brand_id");
					vehstockgatepass_from_location_id = crs.getString("vehstock_vehstocklocation_id");
					vehstockgatepass_from_location_name = crs.getString("vehstocklocation_name");
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
			StrSql = "SELECT"
					+ " vehstockgatepass_vehstock_id,"
					+ " vehstockgatepass_time,"
					+ " vehstock_branch_id,"
					+ " vehstockgatepass_from_location_id,"
					+ " vehstockgatepass_to_location_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " branch_brand_id, vehstock_chassis_no,"
					+ " vehstockgatepass_stockdriver_id,"
					+ " vehstockgatepass_out_kms,"
					+ " vehstockgatepass_notes,"
					+ " vehstocklocation_name,"
					+ " item_id,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " vehstockgatepass_entry_id, vehstockgatepass_entry_date,"
					+ " vehstockgatepass_modified_id, vehstockgatepass_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = vehstockgatepass_vehstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstockgatepass_from_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " WHERE vehstockgatepass_id = " + vehstockgatepass_id + ""
					+ " AND vehstock_delstatus_id != 6";
			// SOP("StrSql==PopulateFields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehstock_id = crs.getString("vehstockgatepass_vehstock_id");
					vehstock_item_id = crs.getString("item_id");
					item_name = crs.getString("item_name");
					vehstock_chassis_no = crs.getString("vehstock_chassis_no");
					vehstock_branch_id = crs.getString("vehstock_branch_id");
					brand_id = crs.getString("branch_brand_id");
					vehstockgatepass_time = crs.getString("vehstockgatepass_time");
					vehstockgatepass_from_location_id = crs.getString("vehstockgatepass_from_location_id");
					vehstockgatepass_from_location_name = crs.getString("vehstocklocation_name");
					vehstockgatepass_to_location_id = crs.getString("vehstockgatepass_to_location_id");
					vehstockgatepass_stockdriver_id = crs.getString("vehstockgatepass_stockdriver_id");
					vehstockgatepass_out_kms = crs.getString("vehstockgatepass_out_kms");
					branch_name = crs.getString("branch_name");
					vehstockgatepass_entry_id = crs.getString("vehstockgatepass_entry_id");
					if (!vehstockgatepass_entry_id.equals("0")) {
						vehstockgatepass_entry_by = Exename(comp_id, Integer.parseInt(vehstockgatepass_entry_id));
					}
					
					entry_date = strToLongDate(crs.getString("vehstockgatepass_entry_date"));
					vehstockgatepass_modified_id = crs.getString("vehstockgatepass_modified_id");
					if (!vehstockgatepass_modified_id.equals("0")) {
						vehstockgatepass_modified_by = Exename(comp_id, Integer.parseInt(vehstockgatepass_modified_id));
						modified_date = strToLongDate(crs.getString("vehstockgatepass_modified_date"));
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
			StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " SET"
					+ " vehstockgatepass_to_location_id = " + vehstockgatepass_to_location_id + ","
					+ " vehstockgatepass_stockdriver_id = " + vehstockgatepass_stockdriver_id + ","
					+ " vehstockgatepass_out_kms = '" + vehstockgatepass_out_kms + "',"
					+ " vehstockgatepass_notes = '" + vehstockgatepass_notes + "',"
					+ " vehstockgatepass_modified_id = " + vehstockgatepass_modified_id + ","
					+ " vehstockgatepass_modified_date = '" + vehstockgatepass_modified_date + "'"
					+ " WHERE vehstockgatepass_id = " + vehstockgatepass_id + "";
			updateQuery(StrSql);
			
			// StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
			// + " SET"
			// + " vehstock_vehstocklocation_id = " + vehstockgatepass_to_location_id + ""
			// + " WHERE vehstock_id = " + vehstock_id + "";
			// updateQuery(StrSql);
		}
	}
	protected void DeleteFields() {
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
					+ " WHERE vehstockgatepass_id = " + vehstockgatepass_id + "";
			updateQuery(StrSql);
		}
	}
	
	public String PopulateStockLocation() {
		if (brand_id.equals("10") || brand_id.equals("2")) {
			brand_id = "10,2";
		}
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehstocklocation_id, vehstocklocation_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstocklocation_branch_id"
					+ " WHERE 1=1"
					// + " vehstocklocation_branch_id = " + vehstock_branch_id + ""
					+ " AND vehstocklocation_id != " + vehstockgatepass_from_location_id + "";
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id IN ( " + brand_id + ")";
			}
			
			StrSql += " ORDER BY vehstocklocation_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql===" + StrSql);
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstocklocation_id"));
				Str.append(StrSelectdrop(crs.getString("vehstocklocation_id"), vehstockgatepass_to_location_id));
				Str.append(">").append(crs.getString("vehstocklocation_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	
	public String PopulateStockDriver() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT vehstockdriver_id, vehstockdriver_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_driver"
					+ " WHERE 1=1";
			if (status.equals("Add")) {
				StrSql += " AND vehstockdriver_active = 1";
			}
			StrSql += " ORDER BY vehstockdriver_name";
			
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstockdriver_id"));
				Str.append(StrSelectdrop(crs.getString("vehstockdriver_id"), vehstockgatepass_stockdriver_id));
				Str.append(">").append(crs.getString("vehstockdriver_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
