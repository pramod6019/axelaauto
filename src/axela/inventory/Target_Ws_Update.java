// smitha nag 28, 29 march 2013
package axela.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Target_Ws_Update extends Connect {
	public String msg = "";
	public String update = "";
	public String updateB = "";
	public String delete = "";
	public String comp_id = "0";
	public String branch_id = "0", dr_branch_id = "0";
	public String branch_name = "";
	public String BranchAccess = "";
	public String year = "";
	public String wstarget_id = "0";
	public String StrSql = "";
	public String SqlJoin = "";
	public String wstarget_model_id = "";
	public String wstarget_fueltype_id = "";
	public String wstarget_count = "0";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String StrSearch = "", StrHTML = "";
	public String QueryString = "";
	public String month = "", month_name = "";
	public String status = "";
	public String wstarget_entry_id = "";
	public String wstarget_modified_id = "";
	public String wstarget_modified_date = "";
	public String emp_id = "";
	public String entry_by = "", entry_date = "", modified_by = "",
			modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
				branch_name = ExecuteQuery("SELECT branch_name"
						+ " FROM " + compdb(comp_id) + "axela_branch "
						+ " WHERE branch_id=" + branch_id + "");
				wstarget_id = CNumeric(PadQuotes(request.getParameter("wstarget_id")));
				delete = PadQuotes(request.getParameter("delete"));
				month = CNumeric(PadQuotes(request.getParameter("month")));
				month_name = TextMonth(Integer.parseInt(month) - 1);
				month_name = month_name.substring(0, 3);
				QueryString = PadQuotes(request.getQueryString());
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				year = CNumeric(PadQuotes(request.getParameter("year")));
				BranchAccess = GetSession("BranchAccess", request);
				// if (ReturnPerm(comp_id, "emp_stock_add,emp_stock_add",
				// request).equals("1")) {
				if (update.equals("yes")) {
					status = "Update";
					PopulateFields(response);
				}
				// }
				if (updateB.equals("Add Target")) {
					if (ReturnPerm(comp_id, "emp_stock_add,emp_stock_add", request).equals("1")) {
						GetValues(request, response);
						UpdateFields(request);
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
				else if (!wstarget_id.equals("0") && delete.equals("yes")) {
					if (ReturnPerm(comp_id, "emp_stock_delete", request).equals("1")) {
						DeleteWsTarget();
						msg = "<br>Target deleted successfully!";
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
				// UpdateFields(request);
				StrHTML = Listdata();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	private void GetValues(HttpServletRequest request,
			HttpServletResponse response) {
		wstarget_model_id = CNumeric(PadQuotes(request.getParameter("dr_wstarget_model_id")));
		wstarget_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_wstarget_fueltype_id")));
		wstarget_count = PadQuotes(request.getParameter("txt_wstarget_count"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	private void PopulateFields(HttpServletResponse response) {

		if (!branch_id.equals("0")) {
			StrSql = "SELECT wstarget_id, wstarget_branch_id, wstarget_model_id, wstarget_fueltype_id,"
					+ " coalesce(wstarget_month, '') as wstarget_month,"
					+ " wstarget_entry_id, coalesce(wstarget_entry_date, '') as wstarget_entry_date,"
					+ " wstarget_modified_id, coalesce(wstarget_modified_date, '') as wstarget_modified_date,"
					+ " coalesce(wstarget_count, '0') wstarget_count";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_target_wholesale"
					+ " WHERE wstarget_branch_id=" + branch_id + ""
					+ " AND SUBSTR(wstarget_month, 1, 6) = " + year + doublenum(Integer.parseInt(month));

			StrSql = StrSql + SqlJoin;
			// SOP("StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);
			try {

				while (crs.next()) {
					wstarget_id = crs.getString("wstarget_id");
					wstarget_count = crs.getString("wstarget_count");
					wstarget_model_id = crs.getString("wstarget_model_id");
					wstarget_fueltype_id = crs.getString("wstarget_fueltype_id");
					entry_by = Exename(comp_id, crs.getInt("wstarget_entry_id"));
					entry_date = strToLongDate(crs
							.getString("wstarget_entry_date"));
					wstarget_modified_id = crs.getString("wstarget_modified_id");
					if (!wstarget_modified_id.equals("0")) {
						modified_by = Exename(comp_id,
								Integer.parseInt(wstarget_modified_id));
						modified_date = strToLongDate(crs
								.getString("wstarget_modified_date"));
					}
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			}
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void CheckForm() {

		if (wstarget_model_id.equals("0")) {
			msg = msg + "<br>Select Model!";
		}
		if (wstarget_fueltype_id.equals("0")) {
			msg = msg + "<br>Select Fueltype!";
		}
		if (wstarget_count.equals("")) {
			msg = msg + "<br>Enter Wholesale Count!";
		}
		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
	}

	public void UpdateFields(HttpServletRequest request) throws SQLException {
		try {
			CheckForm();
			// SOP("msg===" + msg);
			if (msg.equals("")) {
				wstarget_id = CNumeric(ExecuteQuery("SELECT wstarget_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_target_wholesale"
						+ " WHERE substr(wstarget_month,1,6) = " + year + doublenum(Integer.parseInt(month))
						+ " AND wstarget_branch_id = " + branch_id + ""
						+ " AND wstarget_model_id = " + wstarget_model_id + ""
						+ " AND wstarget_fueltype_id = " + wstarget_fueltype_id + ""));
				// SOP("updatefield target_id=====" + wstarget_id);

				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// SOP("wstarget_id----------" + wstarget_id);
				if (wstarget_id.equals("0")) {
					AddTargetFields();
					msg = "<br>Target added successfully!";
				}
				else {
					UpdateTargetFields();
					msg = "<br>Target Updated Successfully!";
				}
				conntx.commit();
			}

		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		} finally {
			try {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void AddTargetFields() throws SQLException {
		try {

			StrSql = "INSERT INTO " + compdb(comp_id)
					+ "axela_sales_target_wholesale (  "
					+ " wstarget_branch_id,"
					+ " wstarget_model_id,"
					+ " wstarget_fueltype_id,"
					+ " wstarget_month,"
					+ " wstarget_count,"
					+ " wstarget_entry_id,"
					+ " wstarget_entry_date ) "
					+ " values  "
					+ " (" + branch_id + ","
					+ " " + wstarget_model_id + ","
					+ " " + wstarget_fueltype_id + ","
					+ " " + year + doublenum(Integer.parseInt(month)) + "01000000" + ","
					+ " " + wstarget_count + ","
					+ " " + emp_id + "," + " "
					+ ToLongDate(kknow()) + ")";
			// SOP("StrSql------Add-----" + StrSql);
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();

			while (rs.next()) {
				wstarget_id = rs.getString(1);
				// SOP("inside add targets===targetid===" + target_id);
			}
			rs.close();
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
	}

	public void UpdateTargetFields() throws SQLException {
		try {

			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_target_wholesale "
					+ " SET"
					+ " wstarget_branch_id= " + branch_id + ", "
					+ " wstarget_model_id= " + wstarget_model_id + ","
					+ " wstarget_fueltype_id= " + wstarget_fueltype_id + ","
					+ " wstarget_month= " + year + doublenum(Integer.parseInt(month)) + "01000000" + ","
					+ " wstarget_count= " + wstarget_count + ", "
					+ " wstarget_modified_id = " + emp_id + ", "
					+ " wstarget_modified_date = " + ToLongDate(kknow()) + " "
					+ " WHERE wstarget_id=" + wstarget_id + "";

			// SOP("Strsql update targets===" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
	}

	public void DeleteWsTarget() {
		StrSql = "Delete from " + compdb(comp_id) + "axela_sales_target_wholesale"
				+ " where wstarget_id = " + wstarget_id + "";
		updateQuery(StrSql);
	}

	public String Listdata() {
		int count = 0;
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT model_name, fueltype_name, wstarget_id, wstarget_branch_id, wstarget_count,"
				+ " COALESCE((SELECT COUNT(vehstock_id) FROM " + compdb(comp_id) + "axela_vehstock"
				+ " WHERE vehstock_item_id = item_id AND vehstock_branch_id = " + branch_id + " AND vehstock_delstatus_id != 6),0) AS stockcount"
				+ " FROM " + compdb(comp_id) + "axela_sales_target_wholesale"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = wstarget_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype on fueltype_id = wstarget_fueltype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = wstarget_model_id AND item_fueltype_id = wstarget_fueltype_id"
				+ " WHERE wstarget_branch_id=" + branch_id + ""
				+ " AND SUBSTR(wstarget_month, 1, 6) = " + year + doublenum(Integer.parseInt(month));

		if (!(StrSearch.equals(""))) {
			StrSql = StrSql + StrSearch;
		}
		StrSql += " GROUP BY model_name, fueltype_name"
				+ " ORDER BY model_name, fueltype_name";
		// SOP("StrSql===" + StrSqlBreaker(StrSql));
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int total = 0, stockcount = 0;
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th>Fuel Type</th>\n");
				Str.append("<th data-hide=\"phone\">Wholesale Count</th>\n");
				Str.append("<th data-hide=\"phone\">Stock Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					total += Integer.parseInt(crs.getString("wstarget_count"));
					stockcount += Integer.parseInt(crs.getString("stockcount"));
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=left >").append(crs.getString("model_name"));
					Str.append("</td>");
					Str.append("<td valign=top align=left>");
					Str.append("").append(crs.getString("fueltype_name"));
					Str.append("</td>");
					Str.append("<td valign=top align=right >");
					Str.append(crs.getString("wstarget_count"));
					Str.append("</td>");
					Str.append("<td valign=top align=right >");
					Str.append(crs.getString("stockcount"));
					Str.append("</td>");
					Str.append("<td valign=top align=left nowrap><a href=\"target-ws-update.jsp?delete=yes&dr_branch_id=").append(crs.getString("wstarget_branch_id"))
							.append("&year=").append(year).append("&month=").append(month).append("&wstarget_id=");
					Str.append(crs.getString("wstarget_id")).append(" \">Delete Target</a>");
					Str.append("</td>");
					Str.append("</tr>\n");
				}
				Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
				Str.append("<td valign=top align=right><b>&nbsp;</b></td>");
				Str.append("<td valign=top align=right><b>Total:</b></td>");
				Str.append("<td valign=top align=right><b>");
				Str.append(total);
				Str.append("</b></td><td align=right><b>").append(stockcount).append("</b></td><td></td>");
				Str.append("</tbody>");
				Str.append("</table>");
				Str.append("</div>");
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
				+ " WHERE 1=1 " + BranchAccess
				+ " AND branch_id = " + branch_id
				+ " AND model_sales = 1"
				+ " AND model_active = 1"
				+ " GROUP BY model_id"
				+ " ORDER BY model_brand_id, model_name";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append(" ");
				Str.append(StrSelectdrop(crs.getString("model_id"), wstarget_model_id));
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

	public String PopulateFuelType() {
		StringBuilder Str = new StringBuilder();
		StrSql = "Select fueltype_id, fueltype_name"
				+ " from " + compdb(comp_id) + "axela_fueltype";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append(" ");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), wstarget_fueltype_id));
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
}
