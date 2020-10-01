package axela.accounting;

//aJIt 25th july

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Accounting_Branch extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String chkPermMsg = "";
	public String go = "";
	public static String msg = "";

	public String StrSql = "";
	public String branch_id = "0", emp_branch_id = "0";
	public String rateclass_id = "0";
	public String location_id = "0";
	public String para = "";
	public String vouchertype_id = "0";
	public String voucherclass_id = "0";
	public String heading = "";
	public String emp_role_id = "";
	public String ticket_id = "0";
	public String oppr_id = "0";
	public String comp_module_inventory = "0";
	public String config_inventory_current_stock = "0";
	public String config_inventory_location_name = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm(comp_id, "emp_quote_add, emp_bill_add, emp_sales_order_add, emp_invoice_add, emp_purchase_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				// SOP("emp_branch_id=="+emp_branch_id);
				branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
				location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
				ticket_id = CNumeric(PadQuotes(request.getParameter("ticket_id")));
				config_inventory_location_name = GetSession("config_inventory_location_name", request);
				oppr_id = CNumeric(PadQuotes(request.getParameter("oppr_id")));
				para = PadQuotes(request.getParameter("para"));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				go = PadQuotes(request.getParameter("go_button"));
				msg = PadQuotes(request.getParameter("msg"));
				if (para.equals("quote")) {
					heading = "Quote Branch";
				} else if (para.equals("sales")) {
					heading = "Sales Order Branch";
				} else if (para.equals("salesreturn")) {
					heading = "Sales Return Branch";
				} else if (para.equals("invoice")) {
					heading = "Invoice Branch";
				} else if (para.equals("bill")) {
					heading = "Bill Branch";
				} else if (para.equals("purchase")) {
					heading = "Purchase Order Branch";
				} else if (para.equals("purchasereturn")) {
					heading = "Purchase Return Branch";
				} else if (para.equals("deliverynote")) {
					heading = "Delivery Note Branch";
				} else if (para.equals("grn")) {
					heading = "GRN Branch";
				} else if (para.equals("enquiry")) {
					heading = "" + GetSession("config_sales_oppr_name", request) + " Branch";
				} else if (para.equals("prop")) {
					heading = "Property Branch";
				}
				PopulateConfigDetails();

				if (go.equals("GO")) {
					GetValues(request, response);
					CheckForm();
					if (msg.equals("")) {
						SetSession("voucher_branch_id", branch_id, request);
						SetSession("voucher_rateclass_id", rateclass_id, request);
						SetSession("vouchertrans_location_id", location_id, request);
					}

					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else if (msg.equals("") && para.equals("quote") && oppr_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/quote-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("quote") && !oppr_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/quote-update.jsp?add=yes&vouchertype_id=" + vouchertype_id + "&oppr_id=" + oppr_id));
					} else if (msg.equals("") && para.equals("sales")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/so-update.jsp?add=yes&vouchertype_id=" + vouchertype_id + "&voucherclass_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("salesreturn")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/salesreturn-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("bill")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/bill-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("purchase")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/po-update.jsp?add=yes&vouchertype_id=" + vouchertype_id + "&voucherclass_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("purchasereturn")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/purchasereturn-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("deliverynote")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/deliverynote-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("grn")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/grn-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("accounting")
							&& ticket_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/invoice-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("invoice")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/invoice-update.jsp?add=yes&vouchertype_id=" + vouchertype_id));
					} else if (msg.equals("") && para.equals("invoice")
							&& !ticket_id.equals("0")) {
						response.sendRedirect(response.encodeRedirectURL("../accounting/salesorder-update.jsp?add=yes&vouchertype_id=" + vouchertype_id + "&ticket_id=" + ticket_id + ""));
					} else if (msg.equals("") && para.equals("enquiry")) {
						response.sendRedirect(response.encodeRedirectURL("enquiry-quickadd.jsp"));
					} else if (msg.equals("") && para.equals("prop")) {
						response.sendRedirect(response.encodeRedirectURL("../realtor/property-update.jsp?add=yes"));
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
		location_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
		if (!emp_branch_id.equals("0")) {
			branch_id = emp_branch_id;
		} else {
			branch_id = PadQuotes(request.getParameter("dr_branch_id"));
		}
		StrSql = "SELECT rateclass_id FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN  " + compdb(comp_id)
				+ "axela_rate_class ON rateclass_id = branch_rateclass_id"
				+ " WHERE branch_id = " + branch_id + "";
		rateclass_id = ExecuteQuery(StrSql);
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0") && emp_branch_id.equals("0")) {
			msg = "<br>Select Branch!";
		}

		if (comp_module_inventory.equals("1") && !para.equals("enquiry")
				&& !para.equals("prop")) {
			if (location_id.equals("0")) {
				msg += "<br>Select " + config_inventory_location_name + "!";
			}
		}
	}

	public void PopulateConfigDetails() {
		try {
			StrSql = "SELECT emp_role_id," + " comp_module_inventory,"
					+ " COALESCE('config_inventory_current_stock','0') AS config_inventory_current_stock " + "FROM "
					+ compdb(comp_id) + "axela_emp," + " " + compdb(comp_id)
					+ "axela_comp," + " " + compdb(comp_id) + "axela_config"
					+ " WHERE emp_id = " + emp_id + "";
			// SOP("Str==PopulateConfigDetails==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				emp_role_id = crs.getString("emp_role_id");
				comp_module_inventory = crs.getString("comp_module_inventory");
				config_inventory_current_stock = crs
						.getString("config_inventory_current_stock");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in"
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String PopulateInventoryLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_id, location_name" + " FROM "
					+ compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + branch_id + ""
					+ " GROUP BY location_id" + " ORDER BY location_name";
			// SOP("StrSql==loc=="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select "
					+ config_inventory_location_name + "</option>\n");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("location_id"));
				Str.append(Selectdrop(crs.getInt("location_id"), location_id));
				Str.append(">").append(crs.getString("location_name"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in"
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}
}
