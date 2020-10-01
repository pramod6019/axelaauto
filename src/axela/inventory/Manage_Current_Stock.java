package axela.inventory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.CalcCurrentStockThread;
import cloudify.connect.Connect;

public class Manage_Current_Stock extends Connect {

	public String add = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String branch_id = "";
	public String BranchAccess = "";
	public String update = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String empEditperm = "";
	public String chkPermMsg = "";
	public String QueryString = "";
	public String stockids = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			// CheckPerm(comp_id, "emp_inventory_access", request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				update = PadQuotes(request.getParameter("Update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				// empEditperm = ExecuteQuery("Select emp_inventory_edit from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + "");
				if (update.equals("yes")) {
					status = "Update";
				}

				if ("Update Current Stock".equals(updateB)) {
					GetValues(request, response);
					checkForm();
					if (msg.equals("")) {
						CheckStock();
						UpdateStock(comp_id, branch_id);
						response.sendRedirect(response.encodeRedirectURL("manage-current-stock.jsp?&msg=Current Stock Status Updated Successfully!"));
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	private void checkForm() {
		msg = "";
		if (branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
	}

	private void UpdateStock(String comp_id, String branch_id) {
		CalcCurrentStockThread calccurrentstockthread = new CalcCurrentStockThread(branch_id, "0", comp_id, "0", "");
		Thread thread = new Thread(calccurrentstockthread);
		thread.start();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
	}

	public void CheckStock() {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_stock"
					+ " (stock_item_id,"
					+ " stock_location_id,"
					+ "	stock_unit_cost,"
					+ "	stock_entry_id,"
					+ " stock_entry_date)"
					+ " SELECT item_id,"
					+ " location_id,"
					+ " COALESCE(price_amt, 0) AS price_amt,"
					+ "	1," // stock_entry_id
					+ "	'" + ToLongDate(kknow()) + "'" // stock_entry_date
					+ " FROM " + compdb(comp_id) + "axela_inventory_item";
			if (comp_id.equals("1011")) {
				StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id";
			}
			StrSql += " ," + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE item_active = 1"
					+ " AND location_branch_id = " + branch_id
					+ " AND item_nonstock = 0";
			if (comp_id.equals("1011")) {
				StrSql += " AND price_id = ( SELECT price_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item_price"
						+ " WHERE price_item_id = item_id"
						+ " AND price_effective_from <= '" + ToLongDate(kknow()) + "'"
						+ " AND price_rateclass_id = 6" // Rate Class hard coded only for Indel, and need to be confirmed
						+ " ORDER BY price_effective_from DESC LIMIT 1 )";
			}
			StrSql += " AND CONCAT(location_id, '-', item_id) NOT IN ( SELECT CONCAT( stock_location_id, '-', stock_item_id )"
					+ " FROM " + compdb(comp_id) + "axela_inventory_stock"
					+ " WHERE stock_location_id = location_id"
					+ " AND stock_item_id = item_id )";
			// SOP(StrSqlBreaker(StrSql));
			updateQuery(StrSql);

			if (comp_id.equals("1011")) {
				StrSql = "SELECT stock_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_stock"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = stock_location_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = stock_item_id"
						+ " WHERE item_active = 1"
						+ " AND location_branch_id = " + branch_id
						+ " AND item_nonstock = 0"
						+ " AND stock_unit_cost = 0";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					stockids += crs.getString("stock_id") + ",";
				}

				if (!stockids.equals("")) {
					stockids = stockids.substring(0, stockids.length() - 1);
					StrSql = "UPDATE"
							+ " " + compdb(comp_id) + "axela_inventory_stock"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = stock_item_id"
							+ " AND price_id = ( SELECT"
							+ " price_id"
							+ " FROM " + compdb(comp_id) + " axela_inventory_item_price"
							+ " WHERE price_item_id = stock_item_id"
							+ " AND price_effective_from <= '" + ToLongDate(kknow()) + "'"
							+ " AND price_rateclass_id = 6"
							+ " ORDER BY price_effective_from DESC LIMIT 1 )"
							+ " SET stock_unit_cost = COALESCE (price_amt, 0)"
							+ " WHERE stock_unit_cost = 0"
							+ " AND stock_id IN (" + stockids + ")";
					updateQuery(StrSql);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateBranches() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " ORDER BY branch_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select Branch</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"))
						.append(" >").append(crs.getString("branch_name"))
						.append("</option> \n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
