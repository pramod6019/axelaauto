/* Ved Prakash (23rd July 2013) */
package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class SalesOrder_Dash_Profitability extends Connect {

	public String StrSql = "";
	public String ExeAccess = "";
	public String BranchAccess = "";
	public String StrHTMLProfitability = "";
	public String so_id = "";
	public String comp_id = "0";
	public String vehstock_invoice_date = "";
	public String so_retail_date = "";
	public int daydiff = 0;
	public int saleprice = 0;
	public int purchasevalue = 0;
	public int grossmargin = 0;
	public int servicemargin = 0;
	public int netmargin = 0;
	public int principalsupport = 0;
	public int financepayout = 0;
	public int insurancepayout = 0;
	public int accessories = 0;
	public int totalincome = 0;
	public int offergiven = 0;
	public int focamount = 0;
	public int totaldiscount = 0;
	public int custoffer = 0;
	public int intrestOnInventory = 0;
	public int totalexpense = 0;
	public int profitorloss = 0;
	public int soprofitabilitysalesprice = 0;
	public int soprofitabilitygrossinvoiceamount = 0;
	public int soprofitabilitygrossmargin = 0;
	public int soprofitabilityservicemargin = 0;
	public int soprofitabilitynetmargin = 0;
	public int soprofitabilityprincipalsupport = 0;
	public int soprofitabilitypaidaccessories = 0;
	public int soprofitabilitytotalincome = 0;
	public int soprofitabilityoffercost = 0;
	public int soprofitabilityinventoryinterest = 0;
	public int soprofitabilitytotalexpense = 0;
	public int soprofitabilityprofit = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));

				StrSql = "SELECT so_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id + BranchAccess + ExeAccess + ""
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						so_id = crs.getString("so_id");
					}
					StrHTMLProfitability = ListProfitabilityData(so_id, BranchAccess, ExeAccess, comp_id);
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Sales Order!");
				}
				crs.close();

			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListProfitabilityData(String so_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT "
				+ " COALESCE(so_profitability_salesprice, 0) AS soprofitabilitysalesprice,"
				+ " COALESCE(so_profitability_grossinvoiceamount, 0) AS soprofitabilitygrossinvoiceamount,"
				+ " COALESCE(so_profitability_grossmargin, 0) AS soprofitabilitygrossmargin,"
				+ " COALESCE(so_profitability_servicemargin, 0) AS soprofitabilityservicemargin,"
				+ " COALESCE(so_profitability_netmargin, 0) AS soprofitabilitynetmargin,"
				+ " COALESCE(so_profitability_principalsupport, 0) AS soprofitabilityprincipalsupport,"
				+ " COALESCE(so_finance_net,0) AS financepayout,"
				+ " COALESCE(so_insur_net,0) AS insurancepayout,"
				+ " COALESCE(so_profitability_paidaccessories,0) AS soprofitabilitypaidaccessories,"
				+ " COALESCE(so_profitability_totalincome,0) AS soprofitabilitytotalincome,"
				+ " COALESCE(so_profitability_offercost,0) AS soprofitabilityoffercost,"
				+ " COALESCE(so_profitability_inventoryinterest,0) AS soprofitabilityinventoryinterest,"
				+ " COALESCE(so_profitability_totalexpense,0) AS soprofitabilitytotalexpense,"
				+ " COALESCE(so_profitability_profit,0) AS soprofitabilityprofit,"

				+ " COALESCE (IF(so_ew_amount != 0, principalsupport_extwty , 0), 0) AS supportextwty,"
				// + " COALESCE (principalsupport_rsa, 0) AS supportrsa,"
				+ " COALESCE (IF(so_insur_amount != 0, principalsupport_insurance, 0), 0) AS supportinsurance,"
				+ " COALESCE (IF(so_offer_consumer != 0, principalsupport_cashdiscount, 0), 0) AS supportcashdiscount,"
				+ " COALESCE (IF(so_exchange = 1 AND so_exchange_amount != 0 ,principalsupport_exchange, 0), 0) AS supportexchange,"
				+ " COALESCE (IF(so_offer_loyalty_bonus != 0 ,principalsupport_loyalty, 0), 0) AS supportloyalty,"
				+ " COALESCE (IF(so_offer_govtempscheme != 0 ,principalsupport_govtempscheme, 0), 0) AS supportgovtempscheme,"
				+ " COALESCE (IF(so_offer_spcl_scheme != 0, principalsupport_monthlyadnbenefit, 0),0) AS supportmonthlyadnbenefit,"
				+ " COALESCE (IF(so_offer_corporate != 0 ,principalsupport_corporate, 0), 0) AS supportcorporate"

				+ " FROM " + compdb(comp_id) + "axela_sales_so"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
				+ " LEFT JOIN (SELECT principalsupport_id,"
				+ " principalsupport_extwty,"
				+ " principalsupport_rsa,"
				+ " principalsupport_insurance,"
				+ " principalsupport_cashdiscount,"
				+ " principalsupport_exchange,"
				+ " principalsupport_loyalty,"
				+ " principalsupport_govtempscheme,"
				+ " principalsupport_monthlyadnbenefit,"
				+ " principalsupport_corporate,"
				+ " principalsupport_model_id,"
				+ " principalsupport_fueltype_id,"
				+ " principalsupport_month"
				+ " FROM " + compdb(comp_id) + "axela_principal_support"
				+ " WHERE 1 = 1 "
				+ " ) AS tblsupport ON tblsupport.principalsupport_model_id = item_model_id"
				+ "  AND tblsupport.principalsupport_fueltype_id = item_fueltype_id"
				+ "  AND SUBSTR(tblsupport.principalsupport_month, 1, 6) = SUBSTR(vehstock_dms_date, 1, 6)"
				+ " WHERE so_id = " + so_id;

		// SOPInfo("StrSql==ListProfitabilityData==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			while (crs.next()) {
				soprofitabilitysalesprice = (int) crs.getDouble("soprofitabilitysalesprice");
				soprofitabilitygrossinvoiceamount = (int) crs.getDouble("soprofitabilitygrossinvoiceamount");
				soprofitabilitygrossmargin = (int) crs.getDouble("soprofitabilitygrossmargin");
				soprofitabilityservicemargin = (int) crs.getDouble("soprofitabilityservicemargin");
				soprofitabilitynetmargin = (int) crs.getDouble("soprofitabilitynetmargin");
				soprofitabilityprincipalsupport = (int) crs.getDouble("soprofitabilityprincipalsupport");
				financepayout = (int) crs.getDouble("financepayout");
				insurancepayout = (int) crs.getDouble("insurancepayout");
				soprofitabilitypaidaccessories = (int) crs.getDouble("soprofitabilitypaidaccessories");
				soprofitabilitytotalincome = (int) crs.getDouble("soprofitabilitytotalincome");
				soprofitabilityoffercost = (int) crs.getDouble("soprofitabilityoffercost");
				soprofitabilityinventoryinterest = (int) crs.getDouble("soprofitabilityinventoryinterest");
				soprofitabilitytotalexpense = (int) crs.getDouble("soprofitabilitytotalexpense");
				soprofitabilityprofit = (int) crs.getDouble("soprofitabilityprofit");

				Str.append("<div class=\"container-fluid portlet box\">");
				Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
				Str.append("<div class=\"caption\" style=\"float: none\">Profitability</div></div>");
				Str.append("<div class=\"portlet-body portlet-empty\">");
				Str.append("<div class=\"table-bordered table-responsive\">\n");
				Str.append("<center><table class=\"table table-bordered table-responsive\" data-filter=\"#filter\" style=\"width: 800px;\">\n");
				Str.append("<thead><tr>\n");
				Str.append("</tr></thead><tbody>\n");
				Str.append("<tr>\n");
				Str.append("<td> Sale Price: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilitysalesprice + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Purchase Value: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilitygrossinvoiceamount + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Gross Margin: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilitygrossmargin + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Service Margin: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilityservicemargin + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Net Margin ( Gross Margin - Service Margin ) :</td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilitynetmargin + "") + "</td>");
				Str.append("</tr>\n");
				// ------------------------------------------------
				Str.append("<tr>\n");
				Str.append("<td colsapn=2 ><b> Principal Support </b></td>");
				// Str.append("<td style=\"text-align: right;\"> " + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Extended Warranty: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportextwty") + "") + "</td>");
				Str.append("</tr>\n");

				// Str.append("<tr>\n");
				// Str.append("<td> RSA: </td>");
				// Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportrsa") + "") + "</td>");
				// Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Insurance: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportinsurance") + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Cash Discount: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportcashdiscount") + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Exchange: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportexchange") + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Loyalty: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportloyalty") + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Govt Emp Scheme: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportgovtempscheme") + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Monthly Additional Benefit: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportmonthlyadnbenefit") + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Corporate: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(crs.getDouble("supportcorporate") + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td><b> Total Principal Support </b> </td>");
				Str.append("<td style=\"text-align: right;\"><b> "
						+ IndFormat(crs.getDouble("supportextwty")
								// + crs.getDouble("supportrsa")
								+ crs.getDouble("supportinsurance")
								+ crs.getDouble("supportcashdiscount")
								+ crs.getDouble("supportexchange")
								+ crs.getDouble("supportloyalty")
								+ crs.getDouble("supportgovtempscheme")
								+ crs.getDouble("supportmonthlyadnbenefit")
								+ crs.getDouble("supportcorporate")
								+ "") + "</b></td>");
				Str.append("</tr>\n");

				// --------------------------------------------------------
				Str.append("<tr>\n");
				Str.append("<td> Finance Payout: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(financepayout + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Insurance Payout: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(insurancepayout + "") + "</td>");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td> Paid Accessory @20%: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilitypaidaccessories + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> <b> Total Income: </b> </td>");
				Str.append("<td style=\"text-align: right;\"> <b> " + IndFormat(soprofitabilitytotalincome + "") + "</b> </td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Offer given including accessory at cost: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilityoffercost + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td> Interest on Inventory: </td>");
				Str.append("<td style=\"text-align: right;\"> " + IndFormat(soprofitabilityinventoryinterest + "") + "</td>");
				Str.append("</tr>\n");

				Str.append("<tr>\n");
				Str.append("<td><b> Total Expense: <b> </td>");
				Str.append("<td style=\"text-align: right;\"> <b> " + IndFormat(soprofitabilitytotalexpense + "") + "</b> </td>");
				Str.append("</tr>\n");

				Str.append("<tr ");
				Str.append(" >\n");

				if (soprofitabilityprofit > 0) {
					Str.append("<td><b><font color='blue'> Profit: </font></b></td>");
					Str.append("<td style=\"text-align: right;\"><b><font color='blue'> " + IndFormat(soprofitabilityprofit + "") + "</font></b></td>");
				} else {
					Str.append("<td><b><font color='red'> Profit: </font></b></td>");
					Str.append("<td style=\"text-align: right;\"><b><font color='red'> " + IndFormat(soprofitabilityprofit + "") + "</font></b></td>");
				}
			}
			crs.close();
			Str.append("</tr>\n");

			Str.append("</tbody></table></center>\n");

			Str.append("</div>");
			// Str.append("</div>");
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
