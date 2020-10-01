package axela.axelaauto_app;

//aJIt 26th June 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Exestock_Details extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0", emp_uuid = "";
	public String StrSearch = "";
	public String StrOption = "";
	public String StrTeam = "";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String model_id = "0";
	public String item_id = "0";
	public String option_id = "0";
	public String delstatus_id = "0";
	public String pending_delivery = "";
	public String order_by = "";
	public String StrOrder = "", vehstock_blocked = "";
	public String vehstock_access = "";
	public String brand_id = "0";
	public String vehstock_branch_id = "0";
	public String team_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String vehstock_id = "0";

	// public String exporttype = "pdf";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
			if (ExecuteQuery("SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
					+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
			{
				session.setAttribute("emp_id", "0");
				session.setAttribute("sessionMap", null);
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
		new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		if (!comp_id.equals("0")) {
			vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
			if (!vehstock_id.equals("0"))
			{
				StrHTML = StockDetail(comp_id);
			}

		}

	}

	public String StockDetail(String comp_id) {
		// String bgcol = "";
		StringBuilder Str = new StringBuilder();
		String optionnames = "";
		// int grandsoamount = 0;
		// int grandreceiptamount = 0;
		// int Totalcount = 0;
		try {
			// to know no of records depending on search
			StrSql = "SELECT vehstock_id,"
					// + " vehstock_comm_no,"
					+ " vehstock_parking_no, vehstock_vehstocklocation_id,"
					+ " COALESCE(vehstocklocation_name, '') AS vehstocklocation_name, vehstock_comm_no,"
					+ " COALESCE(vehstock_notes,'') AS vehstock_notes,"
					+ "vehstock_chassis_no, vehstock_engine_no,"
					+ " IF(vehstock_invoice_date = '', '99999999999999', vehstock_invoice_date) AS vehstock_invoice_date,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " item_id, model_id,  model_name, vehstock_item_id, vehstock_ex_price, delstatus_name,"
					+ " vehstock_status_id, status_name, vehstock_pdi_date,"
					+ " COALESCE(vehstock_intransit_damage, '') AS vehstock_intransit_damage, vehstock_intransit_damage,"
					+ " vehstock_rectification_date, vehstock_blocked,"
					+ " COALESCE(CONCAT(option_name, ' (', option_code, ')'),'') AS option_name, "
					+ " COALESCE(option_id, 0) as option_id,"
					+ " COALESCE(sobranch.branch_id, 0) as sobranch_id, COALESCE(CONCAT(sobranch.branch_name, ' (', sobranch.branch_code, ')'), '') AS sobranchname,"
					+ " vehstockbranch.branch_id as vehstockbranch_id, CONCAT(vehstockbranch.branch_name, ' (', vehstockbranch.branch_code, ')') AS vehstockbranchname,"
					+ " COUNT(DISTINCT quote_id) AS quotecount,"
					+ " COALESCE(so_id, '0') AS so_id,"
					+ " IF(COALESCE(so_no, 0) != '0', CONCAT(vehstockbranch.branch_code, so_no), '') AS so_no,"
					+ " COALESCE(so_date, '') AS so_date,"
					+ "	COALESCE (so_retail_date, '') AS so_retail_date,"
					+ " COALESCE(so_stockallocation_time, '') AS so_stockallocation_time,"
					+ " COALESCE(customer_id, 0) AS customer_id,"
					+ " COALESCE(customer_name, '') AS customer_name, COALESCE(so_grandtotal, 0) AS so_grandtotal,"
					// + " COALESCE((SELECT SUM(receipt_amount) FROM " +
					// compdb(comp_id) + "axela_invoice_receipt"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_invoice ON invoice_id = receipt_invoice_id"
					// + " WHERE invoice_so_id = so_id), 0)
					+ " 0 AS so_receiptamount";

			if (brand_id.equals("56")) {
				StrSql += ", COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE 1 = 1"
						+ " AND trans_vehstock_id = vehstock_id), '') AS optionnames";
			}

			StrSql += " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch vehstockbranch ON vehstockbranch.branch_id = vehstock_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_status ON status_id = vehstock_status_id"
					+ " AND status_id != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
					+ " AND so_active = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch sobranch ON sobranch.branch_id = so_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstock_vehstocklocation_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_vehstock_id = vehstock_id"
					+ " WHERE 1 = 1"
					+ " AND delstatus_id!=6"
					+ " AND vehstock_id=" + vehstock_id
					+ " GROUP BY vehstock_id";
			// + " ORDER BY " + StrOrder;
			SOP("StrSql=============" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// int count = 0;
			// int soamount = 0;
			// int receiptamount = 0;
			// int invoicedays = 0;
			// int stockallocationdays = 0;
			if (crs.isBeforeFirst()) {
				while (crs.next())
				{
					if (brand_id.equals("56")) {
						optionnames = crs.getString("optionnames");
					}
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Stock ID: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("vehstock_id")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Variant: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("item_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Colour: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<span>").append(crs.getString("option_name")).append("</span>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Commision No: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("vehstock_comm_no")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Chassis No: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("vehstock_chassis_no")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Engine No: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("vehstock_engine_no")).append("</p>");
					if (!crs.getString("vehstock_intransit_damage").equals("")) {
						Str.append("<p>").append(crs.getString("vehstock_intransit_damage")).append("</p>");
					}
					if (!crs.getString("vehstock_notes").equals("")) {
						Str.append("<p>").append(crs.getString("vehstock_notes")).append("</p>");
					}
					Str.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Invoice Date: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">");
					if (!crs.getString("vehstock_invoice_date").equals("99999999999999")) {
						Str.append("<p>").append(strToShortDate(crs.getString("vehstock_invoice_date"))).append("</p>");
					}
					Str.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Retail Date: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(strToLongDate(crs.getString("so_retail_date"))).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Status: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("status_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Delivery Status: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("delstatus_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>PDI: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(strToShortDate(crs.getString("vehstock_pdi_date"))).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Quote Count: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("quotecount")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Sales Order:").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">");
					if (!crs.getString("so_id").equals("0")) {
						if (!crs.getString("so_no").equals("")) {
							Str.append("<a href=veh-salesorder-list.jsp?so_id=").append(crs.getString("so_id")).append(">SO ID: ").append(crs.getString("so_id")).append("</a>");
						}
						if (!crs.getString("sobranch_id").equals("0")) {
							Str.append("<br/>" + crs.getString("sobranchname"));
						}
						if (!crs.getString("so_date").equals("")) {
							Str.append("<br/>").append(strToShortDate(crs.getString("so_date")));
							Str.append("<br/>").append(Math.round(getDaysBetween(crs.getString("so_date"),
									ToShortDate(kknow())))).append(" Days");
						}
						if (!crs.getString("so_stockallocation_time").equals(""))
						{
							Str.append("<br/>Allocation:");
							Str.append("<br/>").append(Math.round(getDaysBetween(crs.getString("so_stockallocation_time"),
									ToShortDate(kknow())))).append(" Days");
						} else {
							Str.append("<br/>Allocation :");
							Str.append("<br/>").append("0 Days");
						}
						if (!crs.getString("customer_name").equals("")) {
							Str.append("<br/>").append(crs.getString("customer_name")).append(" (").append(crs.getString("customer_id")).append(")");
						}
					}
					Str.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>SO Amount: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">");
					if (!crs.getString("so_grandtotal").equals("")) {
						Str.append("<p>").append(IndFormat(crs.getString("so_grandtotal"))).append("</p>");
					}
					Str.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Receipt Amount: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">");
					if (!crs.getString("so_receiptamount").equals("")) {
						Str.append("<p>").append(IndFormat(crs.getString("so_receiptamount"))).append("</p>");
					}
					Str.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Location: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("vehstocklocation_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Branch: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("vehstockbranchname")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Stock Options: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">");
					if (brand_id.equals("56")) {
						for (int i = 0; i < optionnames.split(",").length; i++) {
							Str.append(optionnames.split(",")[i] + "<br>");
						}
					} else {
						Str.append(crs.getString("option_name"));
					}

					Str.append("</div>");
					Str.append("</div>");
					Str.append("</div>");

				}

			}
			crs.close();
			// Str.append("<tr align=center>\n<td align=right colspan=15><b>Total Stock: ").append(Totalcount).append(" "
			// +
			// "<br>Grand Total Sales Order Amount: ").append(IndFormat(grandsoamount
			// +
			// ""))
			// .append("<br>Grand Total Receipt Amount: ").append(IndFormat(grandreceiptamount
			// + "")).append("</b></td>\n</tr>\n");
			// Str.append("</table>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
