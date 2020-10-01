/*
 ****JEET 12/05/2015
 */
package axela.inventory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Item_Conversion_Update extends Connect {
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String StrHTML1 = "";
	public String StrSearch = "";
	public String msg = "";
	public String QueryString = "";
	public String session_id = "0";
	public String emp_id = "0", comp_id = "0";
	public String branch_id = "0";
	public Connection conntx = null;
	public Statement stmttx = null;

	public String rateclass_id = "0";

	public String voucher_id = "0", voucher_rateclass_id = "0";
	public String voucherclass_id = "3", voucherclass_file = "";
	public String vouchertype_id = "3";
	public String vouchertype_name = "";
	public String voucher_date = "", voucherdate = "";
	public String voucher_amount = "0";
	// 1
	public String item_itemgroup_id = "";
	public String item_id = "";
	public String vouchertrans_qty = "";
	public String vouchertrans_location_id = "0";

	// 2
	public String item_id1 = "";
	public String item_itemgroup_id1 = "";
	public String vouchertrans_qty1 = "";
	public String vouchertrans_location_id1 = "0";

	public String voucher_terms = "", rateclass_name = "";
	public String item_itemgroup_idarr[] = null;
	public String voucher_entry_id = "0";
	public String voucher_entry_date = "";
	public String voucher_modified_id = "0";
	public String voucher_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String validate = "";
	public String addissue = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public int mapkey = 0;
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_acc_voucher_access, emp_item_conversion_access", request, response);
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				emp_id = CNumeric(GetSession("emp_id", request)) + "";
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				vouchertype_id = CNumeric(PadQuotes(request
						.getParameter("vouchertype_id")));
				vouchertype_name = ExecuteQuery("SELECT vouchertype_name "
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_type WHERE vouchertype_id ="
						+ vouchertype_id);

				session_id = PadQuotes(request.getParameter("txt_session_id"));
				voucher_date = ToLongDate(kknow());
				voucherdate = strToShortDate(voucher_date);
				if (add.equals("yes")) {
					status = "Add";
					if (!addB.equals("yes")) {
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
					} else {

						if (ReturnPerm(comp_id, "emp_item_conversion_add", request).equals(
								"1")) {
							GetValues(request, response);
							voucher_entry_id = emp_id;
							voucher_entry_date = ToLongDate(kknow());

							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("../accounting/voucher-list.jsp?voucher_id="
												+ voucher_id
												+ "&msg=Item Conversion Added successfully!"
												+ msg + ""));
							}

						} else {
							response.sendRedirect(AccessDenied());
						}

					}
				} else if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes")
							&& !deleteB.equals("Delete Conversion")) {
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
						CopyVoucherTransToCart(request, emp_id, voucher_id,
								vouchertype_id);
						PopulateFields(response);
					} else if (updateB.equals("yes")
							&& !deleteB.equals("Delete Conversion")) {
						if (ReturnPerm(comp_id, "emp_item_conversion_edit", request).equals(
								"1")) {
							GetValues(request, response);
							voucher_modified_id = emp_id;
							voucher_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("../accounting/voucher-list.jsp?voucher_id="
												+ voucher_id
												+ "&msg=Item Conversion Updated successfully!"
												+ msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Conversion")) {
						if (ReturnPerm(comp_id, "emp_item_conversion_delete", request)
								.equals("1")) {
							GetValues(request, response);
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response
										.encodeRedirectURL("../accounting/voucher-list.jsp?voucher_id="
												+ voucher_id
												+ "&msg=Item Conversion Deleated successfully!"
												+ msg + ""));
							}

						} else {
							response.sendRedirect(AccessDenied());
						}

					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		voucherdate = request.getParameter("txt_voucher_date");
		rateclass_id = CNumeric(PadQuotes(request
				.getParameter("dr_rateclass_id")));
		voucher_amount = CNumeric(PadQuotes(request
				.getParameter("txt_invoice_grandtotal")));

		// item 1
		item_id = PadQuotes(request.getParameter("itemconversion"));
		if (request.getParameterValues("dr_itemgroup_id") != null) {
			item_itemgroup_idarr = request
					.getParameterValues("dr_itemgroup_id");
			item_itemgroup_id = "";
			for (String str : item_itemgroup_idarr) {
				item_itemgroup_id += str + ",";
			}
			item_itemgroup_id = item_itemgroup_id.substring(0,
					item_itemgroup_id.length() - 1);
		}
		vouchertrans_qty = PadQuotes(request
				.getParameter("txt_quantity"));
		vouchertrans_location_id = CNumeric(PadQuotes(request
				.getParameter("dr_location")));

		// item2
		item_id1 = PadQuotes(request.getParameter("itemconversion1"));
		if (request.getParameterValues("dr_itemgroup_id1") != null) {
			item_itemgroup_idarr = request
					.getParameterValues("dr_itemgroup_id1");
			item_itemgroup_id1 = "";
			for (String str : item_itemgroup_idarr) {
				item_itemgroup_id1 += str + ",";
			}
			item_itemgroup_id1 = item_itemgroup_id1.substring(0,
					item_itemgroup_id1.length() - 1);
		}
		vouchertrans_qty1 = PadQuotes(request
				.getParameter("txt_quantity1"));
		vouchertrans_location_id1 = CNumeric(PadQuotes(request
				.getParameter("dr_location1")));

		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (rateclass_id.equals("0")) {
			msg += "<br>Select Rate class!";
		}
		if (item_id.equals("") || item_id.equals("0")) {
			msg = msg + "<br>Select Issued Item !";
		}

		if (vouchertrans_qty.equals("") || vouchertrans_qty.equals("0")) {
			msg = msg + "<br>Enter Issued Quantity !";
		}
		if (vouchertrans_location_id.equals("0")) {
			msg = msg + "<br>Select Issued Location!";
		}

		if (item_id1.equals("") || item_id1.equals("0")) {
			msg = msg + "<br>Select Received Item !";
		}

		if (vouchertrans_qty1.equals("") || vouchertrans_qty1.equals("0")) {
			msg = msg + "<br>Enter Received Quantity !";
		}
		if (vouchertrans_location_id1.equals("0")) {
			msg = msg + "<br>Select Received Location!";
		}

		if (!vouchertrans_location_id1.equals(vouchertrans_location_id)) {
			msg += "<br>Issued and Received Location not matching!";
		}
		if ((!(vouchertrans_qty.equals(""))
				&& !(vouchertrans_qty1.equals(""))) && ((vouchertrans_qty.equals("0"))
				&& !(vouchertrans_qty1.equals("0")))) {
			if (!(Double.parseDouble(vouchertrans_qty) == Double
					.parseDouble(vouchertrans_qty1))) {
				msg = msg + "<br>" + "Issued & Received Quantity not matching!";
			}
		}
	}

	/**
	 * 
	 */
	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				try {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher"
							+ " ("
							+ "voucher_vouchertype_id,"
							+ " voucher_branch_id," + " voucher_no,"
							+ " voucher_date," + " voucher_amount,"
							+ "voucher_terms," + "voucher_rateclass_id	,"
							+ " voucher_active,"
							+ " voucher_entry_id," + " voucher_entry_date)"
							+ " VALUES" + " ("
							+ vouchertype_id
							+ ","
							+ " "
							+ branch_id
							+ ","
							+ " (SELECT COALESCE(MAX(voucher.voucher_no), 0) + 1"
							+ " FROM  " + compdb(comp_id) + "axela_acc_voucher as voucher"
							+ " WHERE voucher.voucher_branch_id  = "
							+ branch_id
							+ " AND voucher_vouchertype_id = "
							+ vouchertype_id
							+ "),"
							+ " '"
							+ voucher_date
							+ "',"
							+ ""
							+ voucher_amount
							+ ","
							+ " '"
							+ voucher_terms
							+ "' ,"
							+ ""
							+ rateclass_id
							+ ","
							+ "'1',"
							+ " "
							+ emp_id
							+ "," + " " + ToLongDate(kknow()) + ")";

					// SOP("StrSQl=voucher=" + StrSqlBreaker(StrSql));
					stmttx.executeUpdate(StrSql,
							Statement.RETURN_GENERATED_KEYS);
					ResultSet rs = stmttx.getGeneratedKeys();
					while (rs.next()) {
						voucher_id = rs.getString(1);
					}
					rs.close();

					AddItemFields("issue", stmttx);
					AddItemFields("receive", stmttx);

					// // item stock
					// if (msg.equals("")
					// && comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")
					// && vouchertype_affects_inventory.equals("1")
					// && !vouchertype_id.equals("115")) {
					// CalcCurrentStockThread calccurrentstockthread =
					// new CalcCurrentStockThread(voucher_id, company_id, vouchertype_id, "0", "0");
					// Thread thread = new Thread(calccurrentstockthread);
					// thread.start();
					// CalCurrentStockVoucher(voucher_id, company_id,
					// vouchertype_id);
					// }

					// empty cart
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart where"
							+ " cart_vouchertype_id = 3 AND cart_session_id = "
							+ session_id;

					stmttx.executeUpdate(StrSql);

					conntx.commit();
				} catch (Exception e) {
					if (conntx.isClosed()) {
						SOPError("connection is closed...");
					}
					if (!conntx.isClosed() && conntx != null) {
						conntx.rollback();
						SOPError("connection rollback...");
					}
					msg = "<br>Transaction Error!";
					System.out.println("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
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
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);
			} finally {
				try {
					conntx.setAutoCommit(true);
					if (stmttx != null && !stmttx.isClosed()) {
						stmttx.close();
					}
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	protected void UpdateFields() {
		CheckForm();
		conntx = connectDB();

		if (msg.equals("")) {
			try {
				try {
					conntx.setAutoCommit(false);
					stmttx = conntx.createStatement();

					StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher" + " SET"
							+ " voucher_amount = " + voucher_amount + ","
							+ " voucher_modified_id = " + voucher_modified_id
							+ "," + " voucher_modified_date = '"
							+ voucher_modified_date + "'"

							+ " WHERE voucher_id = " + voucher_id + "";
					// SOP("StrSql=update==" + StrSqlBreaker(StrSql));
					stmttx.addBatch(StrSql);
					// Delete before Entries
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
							+ " WHERE vouchertrans_voucher_id = " + voucher_id
							+ "";
					// SOP("StrSql=update==" + StrSqlBreaker(StrSql));
					updateQuery(StrSql);

					AddItemFields("issue", stmttx);
					AddItemFields("receive", stmttx);

					// // item stock
					// if (msg.equals("")
					// && comp_module_inventory.equals("1") && config_inventory_current_stock.equals("1")
					// && vouchertype_affects_inventory.equals("1")
					// && !vouchertype_id.equals("115")) {
					// CalcCurrentStockThread calccurrentstockthread =
					// new CalcCurrentStockThread(voucher_id, company_id, vouchertype_id, "0", "0");
					// Thread thread = new Thread(calccurrentstockthread);
					// thread.start();
					// // CalCurrentStockVoucher(voucher_id, company_id,
					// // vouchertype_id);
					// }

					// empty cart
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart where"
							+ " cart_vouchertype_id = 3 AND cart_session_id = "
							+ session_id;

					stmttx.executeUpdate(StrSql);

					conntx.commit();
				} catch (Exception e) {
					if (conntx.isClosed()) {
						SOPError("connection is closed...");
					}
					if (!conntx.isClosed() && conntx != null) {
						conntx.rollback();
						SOPError("connection rollback...");
					}
					msg = "<br>Transaction Error!";
					System.out
							.println("Axelaauto===" + this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0]
									.getMethodName() + ": " + e);
				} finally {
					try {
						conntx.setAutoCommit(true);
						if (stmttx != null && !stmttx.isClosed()) {
							stmttx.close();
						}
						if (conntx != null && !conntx.isClosed()) {
							conntx.close();
						}
					} catch (Exception ex) {
						SOPError("Axelaauto===" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + ex);

			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT voucher_active, voucher_entry_id, voucher_entry_date, rateclass_name, "
					+ " voucher_modified_id, "
					+ " voucher_modified_date,"
					+ " vouchertrans_item_id, vouchertrans_location_id, vouchertrans_alt_qty"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = vouchertrans_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = voucher_rateclass_id	"
					+ " WHERE  voucher_id = "
					+ voucher_id
					+ " AND vouchertrans_alt_qty != 0"
					+ " AND vouchertrans_qty != 0"
					+ " GROUP BY vouchertrans_id" + " ORDER BY vouchertrans_id";
			// SOP("StrSql=popu==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					count++;
					if (count == 1) {
						item_id = crs.getString("vouchertrans_item_id");
						vouchertrans_qty = crs.getString("vouchertrans_alt_qty")
								.substring(1);
						vouchertrans_location_id = crs.getString("vouchertrans_location_id");
					} else if (count == 2) {
						item_id1 = crs.getString("vouchertrans_item_id");
						vouchertrans_qty1 = crs
								.getString("vouchertrans_alt_qty");
						vouchertrans_location_id1 = crs
								.getString("vouchertrans_location_id");
					}
					rateclass_name = crs.getString("rateclass_name");
					voucher_entry_id = crs.getString("voucher_entry_id");
					entry_by = Exename(comp_id, Integer.parseInt(voucher_entry_id));
					entry_date = strToShortDate(crs
							.getString("voucher_entry_date"));
					voucher_modified_id = crs.getString("voucher_modified_id");
					if (!voucher_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer
								.parseInt(voucher_modified_id));
					}
					modified_date = strToShortDate(crs
							.getString("voucher_modified_date"));
				}
			}
			// SOP(item_id + "---" + item_id1);
			// SOP(vouchertrans_location_id + "===" + vouchertrans_location_id1);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void DeleteFields() {

		if (msg.equals("")) {

			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				// Delete all the items for the current Invoice
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				// Finally Delete the Vonversion
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_id = " + voucher_id + "";
				stmttx.addBatch(StrSql);

				stmttx.executeBatch();

				conntx.commit();

			} catch (Exception e) {
				try {
					if (conntx.isClosed()) {
						SOPError("connection is closed...");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if (!conntx.isClosed() && conntx != null) {
						conntx.rollback();
						SOPError("connection rollback...");
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
			} finally {
				try {
					conntx.setAutoCommit(true);
					if (stmttx != null && !stmttx.isClosed()) {
						stmttx.close();
					}
					if (conntx != null && !conntx.isClosed()) {
						conntx.close();
					}
				} catch (SQLException e) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in "
							+ new Exception().getStackTrace()[0].getMethodName()
							+ ": " + e);
				}

			}
		}
	}

	public String PopulateIssueItem(String item_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<input tabindex='-1' class='bigdrop select2-offscreen' id='itemconversion'"
					+ " name='itemconversion' style='width:250px'  value='"
					+ item_id + "' type='hidden' />");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateReceivedItem(String item_id1) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<input tabindex='-1' class='bigdrop select2-offscreen' id='itemconversion1'"
					+ " name='itemconversion1' style='width:250px'  value='"
					+ item_id1 + "' type='hidden' />");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateLocation(String vouchertrans_location_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT location_id, location_name, location_code"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location" + " WHERE 1=1 " + ""
					+ " GROUP BY location_id" + " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("location_id"));
				Str.append(StrSelectdrop(crs.getString("location_id"),
						vouchertrans_location_id));
				Str.append(">").append(crs.getString("location_name"))
						.append(" (");
				Str.append(crs.getString("location_code"))
						.append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}

		return Str.toString();
	}

	public String PopulateGroup(String item_itemgroup_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT itemgroup_id, itemgroup_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_group" + " WHERE 1=1"
					+ " GROUP BY itemgroup_id" + " ORDER BY itemgroup_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("itemgroup_id"));
				Str.append(StrSelectdrop(crs.getString("itemgroup_id"),
						item_itemgroup_id));
				Str.append(">").append(crs.getString("itemgroup_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("AxelaCRM===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();

	}

	public String PopulateBranchClass() {
		StringBuilder Str = new StringBuilder();
		String StrSql = "";
		try {
			StrSql = "SELECT rateclass_id,rateclass_name"
					+ " FROM " + compdb(comp_id) + "axela_rate_class"
					+ " WHERE rateclass_type =2"
					+ " GROUP BY rateclass_id" + " ORDER BY rateclass_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(
						crs.getString("rateclass_id"));
				Str.append(StrSelectdrop(crs.getString("rateclass_id"),
						rateclass_id));
				Str.append(">").append(crs.getString("rateclass_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
			return "";
		}
	}

	public String ListCartItems(String emp_id, String vouchertype_id,
			String term, String comp_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs1 = null;
		double invoice_grandtotal = 0.00, invoice_total = 0.00, total = 0.00, invoice_optnetamt = 0.00, cart_alt_qty = 0.00, uom_ratio = 0.00;
		double item_price = 0.00, item_unit_price = 0.00;
		double discount = 0.00;
		double discpercent = 0.00;
		double quantity = 0.00;
		double pricetax = 0.00;
		int invoice_qty = 0;
		double mainitemamt = 0.00;
		double total_discount = 0.00;
		double total_tax = 0.00;
		double total_truckspace = 0.00;
		int count = 0;
		String voucher_id = "0";
		String itemtype_name = "";

		StrSql = "SELECT cart.cart_id, cart.cart_rowcount, cart.cart_voucher_id AS cart_voucher_id, item_id, price_amt, price_disc, uom_name,"
				+ " item_name, item_code, cart.cart_qty AS cart_qty, cart.cart_alt_qty, cart.cart_alt_uom_id, cart.cart_truckspace,"
				+ " cart.cart_price, cart.cart_netprice, cart.cart_amount,"
				+ " IF(item_nonstock = 1, COALESCE(stock_current_qty, 0), '_') AS stock_current_qty,"
				+ " (COALESCE((SELECT count(distinct opt.cart_id)"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart opt"
				+ " WHERE opt.cart_option_id = cart.cart_rowcount and opt.cart_discount = 0 and opt.cart_tax = 0"
				+ " GROUP BY opt.cart_rowcount), 0) + 1) AS optitemcount,"
				+ " COALESCE((SELECT sum(optamount.cart_amount)"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart optamount"
				+ " WHERE optamount.cart_option_id = cart.cart_rowcount AND optamount.cart_tax= 0"
				+ " AND optamount.cart_discount= 0 AND optamount.cart_session_id = " + session_id + ""
				+ " GROUP BY optamount.cart_rowcount), 0) AS optamount,"
				// + " COALESCE((SELECT sum(disc.cart_amount)"
				+ " COALESCE((SELECT sum(disc.cart_discount)"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart disc"
				+ " WHERE disc.cart_option_id = cart.cart_rowcount and disc.cart_discount = 1"
				+ " and disc.cart_session_id = " + session_id + ""
				+ " GROUP BY disc.cart_rowcount), 0) AS discount,"
				+ " COALESCE((SELECT sum(tax.cart_amount)"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart tax"
				+ " WHERE tax.cart_option_id = cart.cart_rowcount and tax.cart_tax = 1"
				+ " and tax.cart_session_id = " + session_id + ""
				+ " GROUP BY tax.cart_rowcount), 0) AS tax"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart cart"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = cart.cart_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price on price_item_id = item_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_uom ON uom_id = item_uom_id"
				// + " LEFT JOIN " + compdb(comp_id) + "axela_inventory_boxtype ON boxtype_id = item_boxtype_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id = item_id"
				+ " WHERE 1=1"
				// + " cart.cart_session_id = "+ emp_id
				+ ""
				+ " and cart.cart_vouchertype_id ="
				+ vouchertype_id
				+ " and cart.cart_discount=0 "
				+ " and cart.cart_tax=0 "
				+ " and cart.cart_item_id!=0 "
				// + " and cart.cart_alt_uom_id = "+cart_alt_uom_id+""
				+ " and cart.cart_rowcount!=0 "
				+ " and cart.cart_option_id=0"
				+ " GROUP BY cart.cart_id";
		if (term.equals("issue")) {
			StrSql += " HAVING cart_qty <0"
					+ " ORDER BY cart.cart_rowcount,cart.cart_id LIMIT 1";
		} else if (term.equals("receive")) {
			StrSql += " HAVING cart_qty >0"
					+ " ORDER BY cart.cart_rowcount,cart.cart_id LIMIT 1";
		}

		// SOP("StrSql=List cart items==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		if (!vouchertype_id.equals("3")) {
			StrSql = "SELECT"
					+ " COALESCE(cart_id, 0) AS cart_id,"
					+ " COALESCE(customer_name,'') AS tax_name,"
					+ " COALESCE(customer_rate, 0.00) AS tax_rate,"
					+ " COALESCE(cart_amount, 0.00) AS cart_amount"
					+ " FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = cart_tax_id"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + vouchertype_id
					+ " AND cart_rowcount = 0"
					+ " AND cart_option_id = 0";
			// SOP("StrSql=List cart bs items==" + StrSqlBreaker(StrSql));
			crs1 = processQuery(StrSql, 0);
		}
		Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable footable\">\n");
		try {
			Str.append("<thead>\n<tr>\n");
			Str.append("<th>#</th>\n");
			Str.append("<th>X</th>\n");
			Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>Item</th>\n");
			Str.append("<th data-hide=\"phone\">Qty</th>\n");
			// if (!vouchertype_id.equals("3")) {
			// Str.append("<th data-hide=\"phone\">Truck Space</th>\n");
			// }
			Str.append("<th data-hide=\"phone\">UOM</th>\n");
			Str.append("<th data-hide=\"phone\">Price</th>\n");
			Str.append("<th data-hide=\"phone\">Discount</th>\n");
			Str.append("<th data-hide=\"phone\">Tax</th>\n");
			// Str.append("<th data-hide=\"phone\"></th>\n");
			Str.append("<th>Amount</th>\n");
			Str.append("</tr>\n</thead>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					voucher_id = crs.getString("cart_voucher_id");
					item_price = crs.getDouble("cart_price");
					// SOP("item_price==="+item_price);
					if (term.equals("issue")) {
						cart_alt_qty = Double.parseDouble(crs.getString(
								"cart_alt_qty").substring(1));
					} else if (term.equals("receive")) {
						cart_alt_qty = crs.getDouble("cart_alt_qty");
					}
					// SOP("cart_alt_qty==="+cart_alt_qty);
					if (term.equals("issue")) {
						quantity = Double.parseDouble(crs.getString("cart_qty")
								.substring(1));
					} else if (term.equals("receive")) {
						quantity = crs.getDouble("cart_qty");
					}
					// SOP("quantity==="+quantity);
					uom_ratio = (quantity / cart_alt_qty);

					discount = crs.getDouble("discount") / quantity;
					discpercent = ((discount * 100) / (item_price + 0));
					mainitemamt = (item_price) * quantity;
					invoice_qty += (int) crs.getDouble("cart_alt_qty");
					total_discount += crs.getDouble("discount");
					total = ((((item_price + crs.getDouble("optamount")) - discount) * quantity) + crs
							.getDouble("tax"));
					invoice_total += total;
					total_tax += Double.parseDouble(crs.getString("tax"));
					total_truckspace += Double.parseDouble(crs
							.getString("cart_truckspace"));
					invoice_optnetamt = ((crs.getDouble("optamount")));
					String item_name = crs.getString("item_name");
					if (!crs.getString("item_code").equals("")) {
						item_name += " (" + crs.getString("item_code") + ")";
					}
					if (!crs.getString("cart_rowcount").equals("0")) {
						++count;
					}
					Str.append("\n<tr valign=\"top\"");
					Str.append(" onClick=\"ItemDetails(");
					Str.append(crs.getString("item_id")).append(",");
					Str.append(crs.getDouble("cart_alt_qty")).append(",");
					Str.append(crs.getDouble("cart_alt_uom_id")).append(",");
					Str.append(item_price + ",");
					Str.append(crs.getDouble("discount") + ",");
					// Str.append(crs.getDouble("boxtype_size")).append(",");
					Str.append(" 0,");
					Str.append(crs.getString("cart_id")).append(",");
					Str.append("'update',0);\"");
					// Str.append("'update');\"");

					// Str.append("\n\n");

					Str.append(">\n<td width=\"5%\" align=\"center\">\n");
					Str.append(count);
					Str.append("</td>\n<td align=\"center\">\n");
					Str.append("<a href=\"javascript:delete_cart_item(")
							.append(crs.getString("item_id"))
							.append(",'" + term + "');\">X</a>");
					Str.append("</td>\n");
					Str.append("<td align=\"left\">\n");
					Str.append(item_name);
					Str.append("</td>\n<td align=\"center\">\n");
					if (term.equals("issue")) {
						Str.append(((int) crs.getDouble("cart_alt_qty") + "")
								.substring(1));
					} else if (term.equals("receive")) {
						Str.append((int) crs.getDouble("cart_alt_qty") + "");
					}
					Str.append("</td>");

					Str.append("<td align=\"left\">\n");
					Str.append(crs.getString("uom_name"));
					Str.append("</td>");
					Str.append("\n<td align=\"right\">\n");
					Str.append(df.format((crs.getDouble("cart_price") * uom_ratio)));
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format(crs.getDouble("discount")));
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format(crs.getDouble("tax")));
					Str.append("</td>\n<td align=\"right\">\n");
					Str.append(df.format(total));
					Str.append("</td>\n</tr>\n");
					// SOP(Str.toString());
				}
			}

			Str.append("<tr>\n<td valign=\"top\" align=\"right\"></td>\n");
			Str.append("<td align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><b>Total:</b></td>\n");
			Str.append("<td valign=\"top\" align=\"center\"><input type=\"hidden\" name=\"txt_invoice_qty\" id=\"txt_invoice_qty\" value=\"");
			Str.append(invoice_qty).append("\"><b>");
			if (term.equals("issue")) {
				Str.append((invoice_qty + "").substring(1));
			} else if (term.equals("receive")) {
				Str.append(invoice_qty + "");
			}

			Str.append("</b></td>\n");
			Str.append("<td align=\"right\">&nbsp;</td>\n");
			Str.append("<td align=\"right\">&nbsp;</td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><b>")
					.append(df.format(total_discount)).append("</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\"><b>")
					.append(df.format(total_tax)).append("</b></td>\n");
			Str.append("<td valign=\"top\" align=\"right\">\n");
			if (term.equals("receive")) {
				Str.append("<input type=\"hidden\" name=\"txt_invoice_grandtotal\" id=\"txt_invoice_grandtotal\" value=\"");
				Str.append(invoice_total).append("\">\n");
			}
			Str.append("<b>").append(df.format(invoice_total))
					.append("</b></td>\n");
			Str.append("</tr>\n");
			if (!vouchertype_id.equals("3")) {
				if (crs1.isBeforeFirst()) {
					while (crs1.next()) {
						invoice_grandtotal += crs1.getDouble("cart_amount");
						Str.append("<tr valign=\"top\">");
						Str.append("<td valign=\"top\" colspan=\"8\" align=\"left\">")
								.append(crs1.getString("tax_name")).append("</td>");
						Str.append("<td valign=\"top\" align=\"right\">")
								.append(crs1.getString("tax_rate")).append("</td>");
						Str.append("<td valign=\"top\" align=\"right\">")
								.append(crs1.getString("cart_amount")).append("</td>");
						Str.append("</tr>");
					}
				}

				invoice_grandtotal += invoice_total;
				// Str.append(Listdata());
				Str.append("<tr valign=\"top\">\n");
				Str.append("<td valign=\"top\" colspan='7' align='left'><input type='button' id='billsundry' name='billsundry' class='button' value='Bill Sundry' onclick=\"BillSundry('"
						+ vouchertype_id + "','"
						+ voucher_id + "','"
						+ invoice_total + "');return false;\"/></td>");
				Str.append("<td valign=\"top\" align='right'><b>Grand Total: </b></td>");
				Str.append("<td valign=\"top\" align='right'><b>").append(df.format(invoice_grandtotal)).append("</b></td>");
				Str.append("</tr>\n");
				crs1.close();
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</table>\n");
		return Str.toString();
	}

	protected void AddItemFields(String term, Statement stmttx)
			throws SQLException {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
			stmttx.addBatch(StrSql);
			StrSql = "SELECT" + " " + voucher_id + "," + " cart_customer_id,"
					+ " " + vouchertrans_location_id + "," + " cart_item_id,"
					+ " cart_discount," + " cart_tax," + " cart_tax_id,"
					+ " cart_rowcount, " + " cart_option_id, " + " cart_price,"
					+ " cart_netprice," + " cart_delivery_date,"
					+ " cart_convfactor,";
			// if (term.equals("issue")) {
			// StrSql += "cart_qty,";
			// } else {
			StrSql += "cart_qty,";
			// }
			StrSql += " cart_truckspace,"
					+ " cart_unit_cost,"
					+ " cart_amount,";
			// if (term.equals("issue")) {
			// StrSql += " cart_alt_qty,";
			// } else {
			StrSql += " cart_alt_qty,";
			// }
			StrSql += " cart_alt_uom_id," + " cart_time," + " cart_dc"
					+ " FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_vouchertype_id = "
					+ vouchertype_id + " AND cart_session_id = " + session_id + "";
			if (term.equals("issue")) {
				StrSql += "  AND cart_item_id = " + item_id;
			} else if (term.equals("receive")) {
				StrSql += "  AND cart_item_id = " + item_id1;
			}
			StrSql += " GROUP BY cart_id" + " ORDER BY cart_id";
			// SOP("StrSql==select===" + StrSqlBreaker(StrSql));

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_customer_id,"
					+ " vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_delivery_date,"
					+ " vouchertrans_convfactor,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_truckspace,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " " + StrSql + "";
			// SOP("StrSql==cart--vouchertrans==== " + StrSqlBreaker(StrSql));

			stmttx.executeUpdate(StrSql);

		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void CopyVoucherTransToCart(HttpServletRequest request,
			String emp_id, String voucher_id, String voucher_vouchertype_id) {
		String StrSql = "";
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart" + " WHERE cart_voucher_id = "
				+ voucher_id + "";
		updateQuery(StrSql);

		StrSql = "SELECT" + " vouchertrans_voucher_id," + " "
				+ voucher_vouchertype_id + " ," + " " + emp_id + "," + " "
				+ session_id + "," + " vouchertrans_customer_id,"
				+ " vouchertrans_location_id," + " vouchertrans_item_id,"
				+ " vouchertrans_discount," + " vouchertrans_tax,"
				+ " vouchertrans_tax_id," + " vouchertrans_rowcount,"
				+ " vouchertrans_option_id," + " vouchertrans_option_group,"
				+ " vouchertrans_item_batch_id," + " vouchertrans_item_serial,"
				+ " vouchertrans_qty," + " vouchertrans_truckspace,"
				+ " vouchertrans_price," + " vouchertrans_netprice,"
				+ " vouchertrans_unit_cost," + " vouchertrans_amount,"
				+ " vouchertrans_supplier_code," + " vouchertrans_alt_qty,"
				+ " vouchertrans_alt_uom_id," + " vouchertrans_delivery_date,"
				+ " " + ToLongDate(kknow()) + "," + " vouchertrans_dc"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_id = vouchertrans_voucher_id"
				+ " WHERE vouchertrans_item_id != 0"
				+ " AND vouchertrans_voucher_id = " + voucher_id + "";
		// SOPError("StrSql =select=copy to cart==1223= " + StrSqlBreaker(StrSql));
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart" + " (cart_voucher_id,"
				+ " cart_vouchertype_id," + " cart_emp_id,"
				+ " cart_session_id," + " cart_customer_id,"
				+ " cart_location_id," + " cart_item_id," + " cart_discount,"
				+ " cart_tax," + " cart_tax_id," + " cart_rowcount,"
				+ " cart_option_id," + " cart_option_group,"
				+ " cart_item_batch_id," + " cart_item_serial," + " cart_qty,"
				+ " cart_truckspace," + " cart_price," + " cart_netprice,"
				+ " cart_unit_cost," + " cart_amount," + " cart_supplier_code,"
				+ " cart_alt_qty," + " cart_alt_uom_id,"
				+ " cart_delivery_date," + " cart_time," + " cart_dc)" + " "
				+ StrSql + "";
		// SOPError("StrSql =insert=copy to cart123=== " +
		// StrSqlBreaker(StrSql));
		updateQuery(StrSql);

		StrSql = "SELECT" + " vouchertrans_voucher_id," + " "
				+ voucher_vouchertype_id + " ," + " " + emp_id + "," + " "
				+ session_id + "," + " vouchertrans_customer_id,"
				+ " vouchertrans_location_id," + " vouchertrans_item_id,"
				+ " vouchertrans_discount," + " vouchertrans_tax,"
				+ " vouchertrans_tax_id," + " vouchertrans_rowcount,"
				+ " vouchertrans_option_id," + " vouchertrans_option_group,"
				+ " vouchertrans_item_batch_id," + " vouchertrans_item_serial,"
				+ " vouchertrans_qty," + " vouchertrans_truckspace,"
				+ " vouchertrans_price," + " vouchertrans_netprice,"
				+ " vouchertrans_unit_cost," + " vouchertrans_amount,"
				+ " vouchertrans_supplier_code," + " vouchertrans_alt_qty,"
				+ " vouchertrans_alt_uom_id," + " vouchertrans_delivery_date,"
				+ " " + ToLongDate(kknow()) + "," + " vouchertrans_dc"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher on voucher_id = vouchertrans_voucher_id"
				+ " WHERE  vouchertrans_tax_id !=0"
				+ " AND vouchertrans_item_id = 0"
				+ " AND vouchertrans_voucher_id = " + voucher_id + "";
		// SOPError("StrSql =select=copy to bs cart=== " +
		// StrSqlBreaker(StrSql));
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart" + " (cart_voucher_id,"
				+ " cart_vouchertype_id," + " cart_emp_id,"
				+ " cart_session_id," + " cart_customer_id,"
				+ " cart_location_id," + " cart_item_id," + " cart_discount,"
				+ " cart_tax," + " cart_tax_id," + " cart_rowcount,"
				+ " cart_option_id," + " cart_option_group,"
				+ " cart_item_batch_id," + " cart_item_serial," + " cart_qty,"
				+ " cart_truckspace," + " cart_price," + " cart_netprice,"
				+ " cart_unit_cost," + " cart_amount," + " cart_supplier_code,"
				+ " cart_alt_qty," + " cart_alt_uom_id,"
				+ " cart_delivery_date," + " cart_time," + " cart_dc)" + " "
				+ StrSql + "";
		// SOPError("StrSql =insert=copy to bs cart123=== " +
		// StrSqlBreaker(StrSql));
		updateQuery(StrSql);
	}
}
