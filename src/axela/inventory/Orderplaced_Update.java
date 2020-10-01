// smitha nag 28, 29 march 2013
package axela.inventory;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Orderplaced_Update extends Connect {
	public String addB = "";
	public String msg = "";
	public String add = "";
	public String update = "";
	public String updateB = "";
	public String delete = "";
	public String deleteB = "";
	public String comp_id = "0";
	public String branch_id = "0", dr_orderplaced_branch_id = "0";
	public String branch_name = "";
	public String BranchAccess = "";
	public String year = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String StrSearch = "", StrHTML = "";
	public String QueryString = "";
	public String month = "", month_name = "";
	public String status = "";
	public String emp_id = "";
	public String orderplaced_id = "";
	public String dr_orderplaced_model_id = "";
	public String dr_orderplaced_fueltype_id = "";
	public String orderplaced_count = "0";
	public String orderplaced_entry_id = "";
	public String orderplaced_entry_date = "";
	public String orderplaced_modified_id = "";
	public String orderplaced_modified_date = "";
	public String orderplaced_date = "";
	public String entry_by = "", entry_date = "", modified_by = "",
			modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				msg = PadQuotes(request.getParameter("msg"));
				branch_id = CNumeric(GetSession("emp_id", request));
				QueryString = PadQuotes(request.getQueryString());
				orderplaced_id = CNumeric(PadQuotes(request.getParameter("orderplaced_id")));
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				update = PadQuotes(request.getParameter("update"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				// SOP("updateB--2----" + updateB);
				if (add.equals("yes")) {
					status = "Add";
				}
				if (update.equals("yes")) {
					status = "Update";
					PopulateFields(response);
				}

				if (add.equals("yes")) {
					if (!addB.equals("Add Orderplaced")) {
						orderplaced_entry_id = "";
						orderplaced_entry_date = "";
						orderplaced_modified_id = "";
						orderplaced_modified_date = "";
					} else {
						GetValues(request, response);
						CheckForm();
						// if (ReturnPerm(comp_id, "emp_campaign_add", request).equals("1")) {
						CheckForm();
						orderplaced_entry_id = emp_id;
						orderplaced_entry_id = ToLongDate(kknow());
						if (msg.equals("")) {
							AddOrderPlaced();
						}
						if (!msg.equals("")) {
							msg = "Error! " + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("orderplaced-list.jsp?orderplaced_id=" + orderplaced_id + "&msg=Orderplaced Added Successfully!"));
							status = "";
						}
						// }
						// else {
						// response.sendRedirect(AccessDenied());
						// }
					}
				}
				if ("yes".equals(update)) {
					if (!("yes").equals(updateB) && !deleteB.equals("Delete Orderplaced")) {
					} else if (("yes").equals(updateB) && !"Delete Orderplaced".equals(deleteB)) {
						GetValues(request, response);
						// if (ReturnPerm(comp_id, "emp_enquiry_edit", request).equals("1")) {
						orderplaced_modified_id = emp_id;
						orderplaced_modified_date = ToLongDate(kknow());
						UpdateOrderplaced();
						if (!msg.equals("")) {
							msg = "Error! " + msg;
						} else {
							msg = "Orderplaced updated successfully!";
							response.sendRedirect(response.encodeRedirectURL("orderplaced-list.jsp?orderplaced_id=" + orderplaced_id + "&msg=" + msg));
						}
						// } else {
						// response.sendRedirect(AccessDenied());
						// }
					} else if ("Delete Orderplaced".equals(deleteB)) {
						GetValues(request, response);
						// if (ReturnPerm(comp_id, "emp_enquiry_delete", request).equals("1")) {
						DeleteOrderPlaced();
						if (!msg.equals("")) {
							msg = "Error! " + msg;
						} else {
							msg = "Orderplaced deleted successfully!";
							response.sendRedirect(response.encodeRedirectURL("orderplaced-list.jsp?msg=" + msg));
						}
						// } else {
						// response.sendRedirect(AccessDenied());
						// }
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private void GetValues(HttpServletRequest request,
			HttpServletResponse response) {
		dr_orderplaced_branch_id = CNumeric(PadQuotes(request.getParameter("dr_orderplaced_branch_id")));
		dr_orderplaced_model_id = CNumeric(PadQuotes(request.getParameter("dr_orderplaced_model_id")));
		dr_orderplaced_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_orderplaced_fueltype_id")));
		orderplaced_date = PadQuotes(request.getParameter("txt_orderplaced_date"));
		orderplaced_count = PadQuotes(request.getParameter("txt_orderplaced_count"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	private void PopulateFields(HttpServletResponse response) {

		StrSql = "SELECT orderplaced_id, orderplaced_branch_id, orderplaced_model_id,"
				+ " orderplaced_fueltype_id,"
				+ " COALESCE(orderplaced_date, '') AS orderplaced_date,"
				+ " orderplaced_entry_id,"
				+ " COALESCE(orderplaced_entry_date, '') AS orderplaced_entry_date,"
				+ " orderplaced_modified_id, COALESCE(orderplaced_modified_date, '') AS orderplaced_modified_date,"
				+ " COALESCE(orderplaced_count, '0') AS orderplaced_count"
				+ " FROM " + compdb(comp_id) + "axela_sales_orderplaced"
				+ " WHERE orderplaced_id = " + orderplaced_id;

		CachedRowSet crs = processQuery(StrSql, 0);
		// SOP("StrSql===" + StrSql);
		try {

			while (crs.next()) {
				dr_orderplaced_branch_id = crs.getString("orderplaced_branch_id");
				dr_orderplaced_model_id = crs.getString("orderplaced_model_id");
				dr_orderplaced_fueltype_id = crs.getString("orderplaced_fueltype_id");
				orderplaced_date = strToShortDate(crs.getString("orderplaced_date"));
				orderplaced_count = crs.getString("orderplaced_count");
				entry_by = Exename(comp_id, crs.getInt("orderplaced_entry_id"));
				entry_date = strToLongDate(crs.getString("orderplaced_entry_date"));
				orderplaced_modified_id = crs.getString("orderplaced_modified_id");
				if (!orderplaced_modified_id.equals("0")) {
					modified_by = Exename(comp_id, Integer.parseInt(orderplaced_modified_id));
					modified_date = strToLongDate(crs.getString("orderplaced_modified_date"));
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void CheckForm() {
		if (dr_orderplaced_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}
		if (dr_orderplaced_model_id.equals("0")) {
			msg += "<br>Select Model!";
		}
		if (dr_orderplaced_fueltype_id.equals("0")) {
			msg += "<br>Select Fueltype!";
		}
		if (orderplaced_date.equals("")) {
			msg += "<br>Select Order Placed Date!";
		}
		if (orderplaced_count.equals("0")) {
			msg += "<br>Enter Order Placed Count!";
		}
	}

	public void AddOrderPlaced() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_orderplaced"
					+ " (  "
					+ " orderplaced_branch_id,"
					+ " orderplaced_model_id,"
					+ " orderplaced_fueltype_id,"
					+ " orderplaced_date,"
					+ " orderplaced_count,"
					+ " orderplaced_entry_id,"
					+ " orderplaced_entry_date ) "
					+ " VALUES  "
					+ " (" + dr_orderplaced_branch_id + ","
					+ " " + dr_orderplaced_model_id + ","
					+ " " + dr_orderplaced_fueltype_id + ","
					+ " '" + ConvertShortDateToStr(orderplaced_date) + "',"
					+ " " + orderplaced_count + ","
					+ " " + emp_id + "," + " "
					+ ToLongDate(kknow()) + ")";
			// SOP("StrSql------Add-----" + StrSql);
			orderplaced_id = UpdateQueryReturnID(StrSql);

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranches(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name, branch_code "
					+ " FROM " + compdb(comp_id) + "axela_branch "
					+ " WHERE 1 = 1"
					+ BranchAccess
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id IN (1)"
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value =0>Select Branch</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id")).append("");
				Str.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name"))
						.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public void UpdateOrderplaced() throws SQLException {
		CheckForm();
		try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_orderplaced "
					+ " SET"
					+ " orderplaced_branch_id = " + dr_orderplaced_branch_id + ", "
					+ " orderplaced_model_id = " + dr_orderplaced_model_id + ","
					+ " orderplaced_fueltype_id = " + dr_orderplaced_fueltype_id + ","
					+ " orderplaced_date = " + ConvertShortDateToStr(orderplaced_date) + ", "
					+ " orderplaced_count = " + orderplaced_count + ", "
					+ " orderplaced_modified_id = " + emp_id + ", "
					+ " orderplaced_modified_date = " + ToLongDate(kknow()) + " "
					+ " WHERE orderplaced_id = " + orderplaced_id + "";

			// SOP("Strsql update targets===" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public void DeleteOrderPlaced() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_orderplaced"
				+ " WHERE orderplaced_id = " + orderplaced_id + "";
		updateQuery(StrSql);
	}

	public String PopulateFuelType() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT fueltype_id, fueltype_name"
				+ " FROM " + compdb(comp_id) + "axela_fueltype";
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<option value =0>Select FuelType</option>");
		try {
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append(" ");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), dr_orderplaced_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT model_id, model_name"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
				+ " WHERE 1 = 1 "
				+ BranchAccess
				+ " AND branch_id = " + dr_orderplaced_branch_id
				+ " AND model_sales = 1"
				+ " AND model_active = 1"
				+ " GROUP BY model_id"
				+ " ORDER BY model_brand_id, model_name";
		SOP("ModelPopulate---" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<option value =0>Select Model</option>");
		try {
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append(" ");
				Str.append(StrSelectdrop(crs.getString("model_id"), dr_orderplaced_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
