package axela.sales;
//Saiman 7th dec 2012

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Quote_Print_HTML extends Connect {

	public String quote_id = "0";
	public String StrSql = "";
	public String StrHTML = "";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String BranchAccess;
	public String ExeAccess;
	public String total_disc = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_access", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				QuoteDetails(request, response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void QuoteDetails(HttpServletRequest request, HttpServletResponse response) {
		StringBuilder Str = new StringBuilder();
		String css = TemplatePath(comp_id) + "pdfstyle.css";
		try {
			StrSql = "SELECT quote_date, quote_id, concat('QT',branch_code,quote_no) as quote_no, quote_discamt, "
					+ " comp_name,branch_add, branch_pin, city_name, state_name, branch_phone1,"
					+ " branch_mobile1, branch_email1, quote_bill_address, quote_bill_city,"
					+ " quote_bill_pin, quote_bill_state, quote_ship_address, quote_ship_city,"
					+ " quote_ship_pin, quote_ship_state, quote_desc,"
					+ " quote_terms, quote_grandtotal, concat(branch_name, ' (', branch_code,')') as branch_name,"
					+ " customer_name, emp_name, emp_phone1, emp_mobile1, emp_email1, jobtitle_desc"
					+ " from " + compdb(comp_id) + "axela_sales_quote"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = quote_branch_id"
					+ " inner join " + compdb(comp_id) + "axela_city on city_id = branch_city_id"
					+ " inner join " + compdb(comp_id) + "axela_state on state_id = city_state_id"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = quote_customer_id"
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = quote_emp_id"
					+ " inner join " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id,"
					+ " " + compdb(comp_id) + "axela_comp"
					+ " where quote_id = " + quote_id + BranchAccess + ExeAccess + ""
					+ " group by quote_id order by quote_id desc";
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					total_disc = crs.getString("quote_discamt");
					Str.append("<html xmlms=\"http://www.w3.org/1999/xhtml\"><head>");
					Str.append("<link href=\"").append(css).append("\" rel=\"stylesheet\" type=\"text/css\"/>");
					Str.append("</head><body>");
					Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"5\" class=\"listtable\">");
					Str.append("<tr>");
					Str.append("<th colspan=\"2\" height=\"30\" align=\"center\"><b>PRO FORMA INVOICE</b></th>");
					Str.append("</tr>");
					Str.append("<tr>");
					Str.append("<td rowspan=\"3\" valign=\"top\"><p><b>M/s. ").append(crs.getString("comp_name")).append("</b></p>");
					Str.append("\n<p>").append(crs.getString("branch_add")).append(",</p>");
					Str.append("\n<p>").append(crs.getString("city_name")).append(" - ").append(crs.getString("branch_pin")).append(",</p>\n");
					Str.append("<p>").append(crs.getString("state_name")).append(".</p>");
					if (!crs.getString("branch_phone1").equals("")) {
						Str.append("\n<p>").append(crs.getString("branch_phone1")).append("</p>");
					}
					if (!crs.getString("branch_mobile1").equals("")) {
						Str.append("\n<p>").append(crs.getString("branch_mobile1")).append("</p>");
					}
					Str.append("</td>");
					Str.append("<td valign=\"top\"> Date: ").append(strToShortDate(crs.getString("quote_date"))).append("</td>");
					Str.append("</tr>");
					Str.append("<tr>");
					Str.append("<td valign=\"top\">Quote ID: ").append(crs.getString("quote_id")).append("</td>");
					Str.append("</tr>");
					Str.append("<tr>");
					Str.append("<td valign=\"top\">Quote No.: ").append(crs.getString("quote_no")).append("</td>");
					Str.append("</tr>");
					Str.append("<tr>");
					Str.append("<td width=\"50%\" valign=\"top\">Billing Address:<p>").append(crs.getString("customer_name")).append("</p>\n");
					Str.append("<p>").append(crs.getString("quote_bill_address")).append(",</p>\n");
					Str.append("<p>").append(crs.getString("quote_bill_city")).append(" - ").append(crs.getString("quote_bill_pin")).append(",</p>\n");
					Str.append("<p>").append(crs.getString("quote_bill_state")).append(".</p>\n");
					Str.append("</td>");
					Str.append("<td width=\"50%\" valign=\"top\">Shipping Address:<p>").append(crs.getString("customer_name")).append("</p>\n");
					Str.append("<p>").append(crs.getString("quote_ship_address")).append(",</p>\n");
					Str.append("<p>").append(crs.getString("quote_ship_city")).append(" - ").append(crs.getString("quote_ship_pin")).append(",</p>\n");
					Str.append("<p>").append(crs.getString("quote_ship_state")).append(".</p></td>");
					Str.append("</tr>");
					Str.append("<tr>");
					Str.append("<td colspan=\"2\" align=\"center\" valign=\"top\">").append(ItemDetails()).append("</td>");
					Str.append("</tr>");
					Str.append("<tr>");
					Str.append("<td colspan=\"2\">Amount Chargeable (in words):\n<p>Rupees ").append(toTitleCase(NumberToWordFormat(crs.getInt("quote_grandtotal")))).append(" Only/-.</p></td>");
					Str.append("</tr>");
					if (crs.getString("quote_desc").length() > 5) {
						Str.append("<tr>");
						Str.append("<td colspan=\"2\">").append(crs.getString("quote_desc")).append("</td>");
						Str.append("</tr>");
					}
					if (crs.getString("quote_terms").length() > 5) {
						Str.append("<tr>");
						Str.append("<td colspan=\"2\"><b>Terms:</b>\n<p>").append(crs.getString("quote_terms")).append("</p></td>");
						Str.append("</tr>");
					}
					Str.append("<tr>");
					Str.append("<td align=\"center\" >&nbsp;</td>");
					Str.append("<td align=\"right\" > For M/s. ").append(crs.getString("comp_name"));
					Str.append("\n<p>").append(crs.getString("emp_name")).append("</p>");
					Str.append("\n<p>").append(crs.getString("jobtitle_desc")).append("</p>");
					if (!crs.getString("emp_phone1").equals("")) {
						Str.append("\n<p>").append(crs.getString("emp_phone1")).append("</p>");
					}
					if (!crs.getString("emp_mobile1").equals("")) {
						Str.append("\n<p>").append(crs.getString("emp_mobile1")).append("</p>");
					}
					if (!crs.getString("emp_email1").equals("")) {
						Str.append("\n<p>").append(crs.getString("emp_email1")).append("</p>");
					}
					Str.append("</td>");
					Str.append("</tr>");
					Str.append("</table></body></html>");
				}
			} else {
				Str.append("Invalid Quote!");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		// Str.toString().replace("<", "&lt;");
		// Str.toString().replace(">", "&gt;");
		StrHTML = Str.toString();
	}

	public String ItemDetails() {
		StringBuilder Str = new StringBuilder();
		Double grandtotal = 0.00, total = 0.00;
		Double discamt = 0.00;
		Double selling_price = 0.00;
		Double qty = 0.00;
		int count = 0;
		String colspan = "";
		StrSql = "Select item_name, item_code,"
				+ " quoteitem_qty, quoteitem_price,"
				+ " quoteitem_disc, quoteitem_tax"
				+ " from " + compdb(comp_id) + "axela_sales_quote_item"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = quoteitem_item_id"
				+ " where quoteitem_quote_id = " + quote_id + ""
				+ " group by quoteitem_id"
				+ " order by quoteitem_id";
		// SOP(StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">");
		try {
			Str.append("<tr><th>#</th>");
			Str.append("<th>Item</th>");
			Str.append("<th>Qty</th>");
			Str.append("<th>Price</th>");
			if (!total_disc.equals("0.00")) {
				Str.append("<th>Discount</th>");
			}
			Str.append("<th>Tax</th>");
			Str.append("<th>Selling Price</th>");
			Str.append("<th>Amount</th>");
			Str.append("</tr>");
			while (crs.next()) {
				count = count + 1;
				total = crs.getDouble("quoteitem_qty") * (crs.getDouble("quoteitem_price") - crs.getDouble("quoteitem_disc") + crs.getDouble("quoteitem_tax"));
				selling_price = (crs.getDouble("quoteitem_price") - crs.getDouble("quoteitem_disc") + crs.getDouble("quoteitem_tax"));
				grandtotal = grandtotal + total;
				discamt = discamt + (crs.getDouble("quoteitem_qty") * crs.getDouble("quoteitem_disc"));
				qty = qty + crs.getDouble("quoteitem_qty");
				String item_name = crs.getString("item_name");
				if (!crs.getString("item_code").equals("")) {
					item_name = item_name + " (" + crs.getString("item_code") + ")";
				}
				Str.append("\n<tr valign=\"top\">");
				Str.append("<td align=\"center\">");
				Str.append(count).append(".");
				Str.append("</td>");
				Str.append("<td align=\"left\">");
				Str.append(item_name).append("\n");
				Str.append("</td><td align=\"right\">");
				Str.append(df.format(crs.getDouble("quoteitem_qty")));
				Str.append("</td><td align=\"right\">");
				Str.append(df.format(crs.getDouble("quoteitem_price")));
				Str.append("</td>");
				if (!total_disc.equals("0.00")) {
					Str.append("<td align=\"right\">");
					Str.append(df.format(crs.getDouble("quoteitem_disc")));
					Str.append("</td>");
				}
				Str.append("<td align=\"right\">");
				Str.append(df.format(crs.getDouble("quoteitem_tax")));
				Str.append("</td><td align=\"right\">");
				Str.append(df.format(selling_price));
				Str.append("</td><td align=\"right\">");
				Str.append(df.format(total));
				Str.append("</td></tr>");
			}
			if (total_disc.equals("0.00")) {
				colspan = "6";
			} else {
				colspan = "7";
			}
			Str.append("<tr><td align=\"right\"");
			Str.append(" colspan=\"").append(colspan).append("\"><b>Grand Total:</b></td>");
			Str.append("<td align=\"right\"><b>").append(df.format(Math.ceil(grandtotal))).append("</b></td>");
			Str.append("</tr>");
			if (discamt > 0) {
				Str.append("<tr><td align=\"right\"");
				Str.append(" colspan=\"").append(colspan).append("\">Total Savings:</td>");
				Str.append("<td align=\"right\">").append(df.format(discamt)).append("</td>");
				Str.append("</tr>");
			}
			Str.append("<tr><td align=\"right\"");
			Str.append(" colspan=\"").append(colspan).append("\">Net Quantity:</td>");
			Str.append("<td align=\"right\">").append(df.format(qty)).append("</td>");
			Str.append("</tr>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		Str.append("</table>");
		return Str.toString();
	}
}
