package axela.sales;
//aJIt 10th December, 2012

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Quote_Discount_Authorize extends Connect {
	
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String declineB = "";
	public String acceptB = "";
	public String StrSql = "";
	public String msg = "";
	public String quote_id = "0";
	public String quotediscount_id = "0";
	public String quotediscount_requestedamount = "0.0";
	public String quotediscount_authorizedamount = "0.0";
	public String quotediscount_authorize_time = "";
	public String quotediscount_authorizetime = "";
	public String quotediscount_authorize_status = "";
	public String BranchAccess = "", ExeAccess = "";
	public String quotediscount_authorize_emp_id = "0";
	public String quotediscount_authorized_by = "";
	public String customer_name = "";
	public String item_name = "";
	public String model_name = "";
	
	public String team_leader_id = "";
	public String team_leader = "";
	public String branch_name = "";
	public String branch_id = "";
	public String quote_date = "";
	public String quote_emp_id = "";
	public String quote_emp = "";
	public String quote_grandtotal = "0.0";
	public String so_id = "0";
	public String so_date = "";
	public String so_active = "0";
	
	public Connection conntx = null;
	public Statement stmttx = null;
	DecimalFormat df = new DecimalFormat("0.00");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_authorize", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				update = PadQuotes(request.getParameter("update"));
				acceptB = PadQuotes(request.getParameter("accept_button"));
				declineB = PadQuotes(request.getParameter("decline_button"));
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				quotediscount_id = CNumeric(PadQuotes(request.getParameter("quotediscount_id")));
				quotediscount_authorize_status = CNumeric(PadQuotes(request.getParameter("quotediscount_authorize_status")));
				if (!quotediscount_authorize_status.equals("0")) {
					response.sendRedirect("../portal/error.jsp?msg=Discount authorization is been completed!");
				}
				
				quotediscount_authorize_emp_id = CNumeric(PadQuotes(ExecuteQuery("SELECT quotediscount_authorize_emp_id FROM " + compdb(comp_id)
						+ "axela_sales_quote_discount WHERE quotediscount_id = " + quotediscount_id)));
				
				if (!quote_id.equals("0") && !quotediscount_id.equals("0")) {
					PopulateFields(request, response);
				}
				if (acceptB.equals("yes") && !declineB.equals("Decline")) {
					if (quotediscount_authorize_emp_id.equals(emp_id) || emp_id.equals("1")) {
						GetValues(request, response);
						CheckForm();
						if (msg.equals("")) {
							UpdateAdditionalDiscount("Authorize", quote_id, quotediscount_id, emp_id, comp_id);
							response.sendRedirect("veh-quote-discount-list.jsp?quote_id=" + quote_id + "&quotediscount_id=" + quotediscount_id + "&msg=Quote Discount Authorized successfully!");
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				} else if (declineB.equals("Decline")) {
					if (quotediscount_authorize_emp_id.equals(emp_id)) {
						UpdateAdditionalDiscount("Decline", quote_id, quotediscount_id, emp_id, comp_id);
						response.sendRedirect("veh-quote-discount-list.jsp?quote_id=" + quote_id + "&quotediscount_id=" + quotediscount_id + "&msg=Quote Discount Declined successfully!");
					} else {
						response.sendRedirect(AccessDenied());
						
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
	
	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// quotediscount_requestedamount = CNumeric(request.getParameter("txt_quotediscount_requestedamount"));
		quotediscount_authorizedamount = CNumeric(request.getParameter("txt_quotediscount_authorizedamount"));
		quotediscount_authorize_emp_id = emp_id;
		quotediscount_authorize_time = ToLongDate(kknow());
	}
	
	protected void CheckForm() {
		msg = "";
		if (Double.parseDouble(quotediscount_authorizedamount) == 0) {
			msg += "Authorized Discount Amount can't be Zero!";
		}
		if (Double.parseDouble(quotediscount_authorizedamount) > Double.parseDouble(quotediscount_requestedamount)) {
			msg += "Authorized Discount Amount can't be greater than Requested Discount Amount!";
		}
		
		StrSql = "SELECT so_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_quote_id = " + quote_id
				+ " AND so_active = 1"
				+ " AND so_retail_date != ''";
		so_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
		if (!so_id.equals("0")) {
			msg += "<br>Discount can't be authorized for Retailed Sales Order!";
		}
		
	}
	
	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT quotediscount_authorize_time, quotediscount_authorize_status, quotediscount_authorize_emp_id,"
					+ " quotediscount_requestedamount, branch_id, branch_name, quote_date, quote_emp_id, quote_grandtotal,"
					+ " item_name, model_name,"
					+ " COALESCE(team_emp_id, 0) AS team_emp_id, "
					+ " COALESCE(so_id, '') AS so_id,"
					+ " COALESCE(so_date, '') AS so_date,"
					+ " COALESCE(so_active, '') AS so_active,"
					+ " customer_name, customer_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_discount"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = quotediscount_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quote_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_quote_id = quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = quotediscount_request_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " WHERE quote_id = " + quote_id
					+ " AND quotediscount_id = " + quotediscount_id
					+ BranchAccess + ExeAccess + "";
			// SOP("StrSql===pop===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					quotediscount_authorize_status = crs.getString("quotediscount_authorize_status");
					quotediscount_authorize_emp_id = crs.getString("quotediscount_authorize_emp_id");
					quotediscount_requestedamount = df.format(Double.parseDouble(crs.getString("quotediscount_requestedamount")));
					quotediscount_authorizedamount = quotediscount_requestedamount;
					if (!quotediscount_authorize_emp_id.equals("0")) {
						quotediscount_authorized_by = Exename(comp_id, Integer.parseInt(quotediscount_authorize_emp_id));
					}
					quotediscount_authorize_time = crs.getString("quotediscount_authorize_time");
					quotediscount_authorizetime = strToLongDate(quotediscount_authorize_time);
					branch_id = crs.getString("branch_id");
					branch_name = crs.getString("branch_name");
					quote_date = strToShortDate(crs.getString("quote_date"));
					quote_emp_id = crs.getString("quote_emp_id");
					quote_emp = Exename(comp_id, Integer.parseInt(crs.getString("quote_emp_id")));
					quote_grandtotal = IndDecimalFormat(df.format(Double.parseDouble(crs.getString("quote_grandtotal"))));
					item_name = crs.getString("item_name");
					model_name = crs.getString("model_name");
					so_id = crs.getString("so_id");
					if (!crs.getString("so_date").equals("")) {
						so_date = strToShortDate(crs.getString("so_date"));
					}
					so_active = crs.getString("so_active");
					team_leader_id = crs.getString("team_emp_id");
					team_leader = Exename(comp_id, Integer.parseInt(crs.getString("team_emp_id")));
					quotediscount_authorize_time = crs.getString("quotediscount_authorize_time");
					quotediscount_authorize_time = crs.getString("quotediscount_authorize_time");
					
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Quote"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	public void UpdateAdditionalDiscount(String status, String quote_id, String quotediscount_id, String emp_id, String comp_id) throws SQLException {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		String quoteitem_id = "0";
		try {
			conntx = connectDB();
			conntx.setAutoCommit(false);
			stmttx = conntx.createStatement();
			
			if (status.equals("Authorize")) {
				// Update Discount status as Accept and update other related fields.
				updateQuoteDiscount();
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote_discount"
						+ " SET quotediscount_authorize_time = " + ToLongDate(kknow()) + ","
						+ "	quotediscount_authorize_status = '1',"
						+ " quotediscount_authorizedamount = " + quotediscount_authorizedamount
						+ " WHERE quotediscount_id = " + quotediscount_id;
				SOP("StrSql==update additional discount==" + StrSql);
				stmttx.addBatch(StrSql);
			} else if (status.equals("Decline")) {
				// Update Discount status as Decline
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote_discount"
						+ " SET quotediscount_authorize_time = " + ToLongDate(kknow()) + ","
						+ "	quotediscount_authorize_status = '2'"
						+ " WHERE quotediscount_id = " + quotediscount_id;
				// SOP("StrSql==update additional discount==" + StrSql);
				stmttx.addBatch(StrSql);
			}
			stmttx.executeBatch();
			conntx.commit();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				SOP("Transaction Error==");
				conntx.rollback();
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			}
			msg = "<br>Transaction Error!";
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
	
	public void updateQuoteDiscount() throws SQLException {
		try {
			CachedRowSet crs = null;
			String quoteitem_id = "0", quoteitem_item_id = "0", quoteitem_option_id = "0";
			String quote_netamt = "";
			String quote_discamt = "";
			String quote_grandtotal = "";
			String quote_exprice = "";
			String quote_totaltax = "";
			
			StrSql = "SELECT quoteitem_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " WHERE quoteitem_quote_id = " + quote_id
					+ " AND quoteitem_option_group = 'Additional Discounts'"
					+ " AND item_name = 'Additional Discount'";
			quoteitem_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));
			
			if (!quoteitem_id.equals("0")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote_item"
						+ " SET quoteitem_disc = " + quotediscount_authorizedamount + ","
						+ " quoteitem_total = -" + quotediscount_authorizedamount
						+ " WHERE quoteitem_id = " + quoteitem_id;
				// SOP("StrSql==update additional discount==" + StrSql);
				stmttx.execute(StrSql);
			} else if (quoteitem_id.equals("0")) {
				StrSql = "SELECT quoteitem_rowcount, item_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item, " + compdb(comp_id) + "axela_inventory_item"
						+ " WHERE quoteitem_quote_id = " + quote_id
						+ " AND item_name = 'Additional Discount'"
						+ "	AND quoteitem_rowcount != 0";
				// SOP("StrSql==get item==" + StrSqlBreaker(StrSql));
				
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					quoteitem_option_id = CNumeric(crs.getString("quoteitem_rowcount"));
					quoteitem_item_id = CNumeric(crs.getString("item_id"));
				}
				crs.close();
				
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // to insert the configured items
						+ " ("
						+ "	quoteitem_quote_id,"
						+ " quoteitem_rowcount,"
						+ " quoteitem_item_id,"
						+ " quoteitem_option_id,"
						+ " quoteitem_option_group,"
						+ " quoteitem_option_group_tax,"
						+ " quoteitem_qty,"
						+ " quoteitem_disc,"
						+ " quoteitem_total"
						+ ")"
						+ " VALUES"
						+ " ("
						+ " " + quote_id + ","
						+ " 0," // quoteitem_rowcount
						+ " " + quoteitem_item_id + ","
						+ " " + quoteitem_option_id + ","
						+ " 'Additional Discounts'," // quoteitem_option_group
						+ " '2'," // quoteitem_option_group_tax
						+ " 1," // quoteitem_qty
						+ " " + quotediscount_authorizedamount + ","
						+ " -" + quotediscount_authorizedamount
						+ ")";
				// SOP("StrSql==insert Additional Discount==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);
			}
			conntx.commit();
			
			// Get the total from Quote-Item table
			StrSql = "SELECT SUM(quoteitem_price) AS quote_netamt,"
					+ " SUM(quoteitem_disc) AS quote_discamt,"
					+ " SUM(quoteitem_tax) AS quote_totaltax,"
					+ " SUM(quoteitem_total) AS quote_grandtotal,"
					+ " ( SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item item"
					+ " WHERE item.quoteitem_option_group_tax = 1"
					+ " AND item.quoteitem_quote_id = " + quote_id
					+ ") AS quote_exprice"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " WHERE quoteitem_quote_id = " + quote_id + "";
			// SOP("StrSql==quote total==" + StrSql);
			crs = processQuery(StrSql, 0);
			
			while (crs.next()) {
				quote_netamt = crs.getString("quote_netamt");
				quote_discamt = crs.getString("quote_discamt");
				quote_totaltax = crs.getString("quote_totaltax");
				quote_grandtotal = crs.getString("quote_grandtotal");
				quote_exprice = crs.getString("quote_exprice");
			}
			crs.close();
			
			// For updating the Quote total feilds
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote"
					+ " SET" + " quote_netamt = " + quote_netamt + ","
					+ " quote_discamt = " + quote_discamt + ","
					+ " quote_exprice = " + quote_exprice + ","
					+ " quote_totaltax = " + quote_totaltax + ","
					+ " quote_grandtotal = " + Math.ceil(Double.parseDouble(quote_grandtotal)) + ","
					+ " quote_quotediscount_id = " + quotediscount_id
					+ " WHERE quote_id = " + quote_id + "";
			// SOP("Update==SalesQuote==" + StrSql);
			stmttx.execute(StrSql);
			
			// Update Active Sales Order if present.
			StrSql = "SELECT so_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_quote_id = " + quote_id
					+ " AND so_active = 1";
			// SOP("StrSql==so_id==" + StrSqlBreaker(StrSql));
			
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				so_id = CNumeric(crs.getString("so_id"));
			}
			crs.close();
			
			if (!so_id.equals("0")) {
				// Delete so_items
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_so_item"
						+ " WHERE soitem_so_id = " + so_id;
				// SOP("StrSql==Delete So Items==" + StrSql);
				stmttx.execute(StrSql);
				
				// Insert so_items from quote_items
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_item"
						+ " ("
						+ " soitem_so_id,"
						+ " soitem_rowcount,"
						+ " soitem_item_id,"
						+ " soitem_option_id,"
						+ " soitem_option_group,"
						+ " soitem_option_group_tax,"
						+ " soitem_item_serial,"
						+ " soitem_qty,"
						+ " soitem_price,"
						+ " soitem_disc,"
						+ " soitem_tax,"
						+ " soitem_tax_id,"
						+ " soitem_tax_rate,"
						+ " soitem_total"
						+ " )"
						+ " SELECT"
						+ " " + so_id + "," // soitem_so_id
						+ " quoteitem_rowcount,"
						+ " quoteitem_item_id,"
						+ " quoteitem_option_id,"
						+ " quoteitem_option_group,"
						+ " quoteitem_option_group_tax,"
						+ " quoteitem_item_serial,"
						+ " quoteitem_qty,"
						+ " quoteitem_price,"
						+ " quoteitem_disc,"
						+ " quoteitem_tax,"
						+ " quoteitem_tax_id,"
						+ " quoteitem_tax_rate,"
						+ " quoteitem_total"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " WHERE quoteitem_quote_id = " + quote_id;
				// SOP("StrSql==Update So_items==" + StrSql);
				stmttx.addBatch(StrSql);
			}
			
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
