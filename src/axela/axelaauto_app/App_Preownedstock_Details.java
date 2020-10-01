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

public class App_Preownedstock_Details extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0", emp_uuid = "";
	public String StrSearch = "";
	public String StrOption = "";
	public String StrTeam = "";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String model_id = "0", location_id = "0";
	public String item_id = "0";
	public String option_id = "0";
	public String delstatus_id = "0";
	public String pending_delivery = "";
	public String order_by = "";
	public String StrOrder = "", stock_blocked = "";
	public String stock_access = "";
	public String brand_id = "0";
	public String stock_branch_id = "0";
	public String team_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String preownedstock_id = "0";

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
			}
		}
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		if (!emp_id.equals("0")) {
			preownedstock_id = CNumeric(PadQuotes(request.getParameter("preownedstock_id")));
			if (!preownedstock_id.equals("0"))
			{
				StrHTML = PreownedStockDetail(comp_id);
			}

		}

	}

	public String PreownedStockDetail(String comp_id) {
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		// int grandsoamount = 0;
		// int grandreceiptamount = 0;
		// int Totalcount = 0;
		int stockdays = 0;
		int count = 0;
		try {
			// to know no of records depending on search
			StrSql = "SELECT "
					+ " COALESCE(preownedstock_id, 0) AS preownedstock_id,"
					+ " COALESCE(preowned_id, 0) AS preowned_id, "
					+ " COALESCE(preowned_title,'') AS preowned_title,  COALESCE(preownedmodel_id, 0) AS  preownedmodel_id,"
					+ " COALESCE(preownedmodel_name, '') AS preownedmodel_name,"
					+ " COALESCE(variant_id, 0) AS variant_id, COALESCE(variant_name, '') AS variant_name,"
					+ " COALESCE(preownedstock_status_id, 0) AS preownedstock_status_id,"
					+ " COALESCE(preowned_sub_variant, '') AS preowned_sub_variant,"
					+ " COALESCE(extcolour_name, '') AS extcolour_name,"
					+ " COALESCE(intcolour_name, '') AS intcolour_name, COALESCE(preowned_options,'') AS preowned_options,"
					+ " COALESCE(preownedstock_blocked, 0) AS preownedstock_blocked,"
					+ " COALESCE(preownedstock_date,'') AS preownedstock_date,"
					+ " COALESCE(preownedstatus_id, 0) AS preownedstatus_id,"
					+ " COALESCE(preownedstatus_name, '') AS preownedstatus_name, "
					+ " COALESCE(ownership_id, 0) AS ownership_id, COALESCE(ownership_name, '') AS ownership_name,"
					+ " COALESCE(fueltype_id, 0) AS fueltype_id,"
					+ " COALESCE(fueltype_name, '') AS fueltype_name, COALESCE(preownedstock_selling_price, '') AS preownedstock_selling_price,"
					+ " COALESCE(preowned_regdyear,'') AS preowned_regdyear, COALESCE(preowned_insur_date,'') AS preowned_insur_date,"
					+ " COALESCE(preowned_kms, '') AS preowned_kms,"
					+ " COALESCE(preowned_branch_id, 0) AS preowned_branch_id,"
					+ " COALESCE(branch_id, 0) AS branch_id, COALESCE(concat(branch_name, ' (', branch_code, ')'), '') as branch_name,"
					+ " COALESCE(preowned_regno, '') AS preowned_regno,"
					+ " COALESCE(preownedlocation_name,'') AS preownedlocation_name, "
					+ " COALESCE((SELECT count(distinct quote_id) FROM " + compdb(comp_id) + "axela_sales_quote WHERE quote_preownedstock_id= preownedstock_id), 0) as quotecount,"
					+ " COALESCE(so_id, 0) as so_id, COALESCE(so_date, '') as so_date,"
					+ " COALESCE(customer_id, 0) as customer_id, COALESCE(customer_name, '') as customer_name,"
					+ " COALESCE(so_grandtotal, 0) as so_grandtotal, "
					+ " COALESCE(GROUP_CONCAT(CONCAT('<a href=../Fetchdocs.do?doc_preowned_id=',doc_id,'><font color=blue>',doc_title,'</font></a>') SEPARATOR '<br>'), '&nbsp;') as docs"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN axela_preowned_variant on variant_id = preowned_variant_id"
					+ " INNER JOIN axela_preowned_model on preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype on fueltype_id = preowned_fueltype_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_ownership on ownership_id = preowned_ownership_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock_status on preownedstatus_id = preownedstock_status_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_location on preownedlocation_id = preownedstock_preownedlocation_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_docs ON doc_preowned_id = preowned_id"
					+ " LEFT JOIN axela_preowned_extcolour on extcolour_id = preowned_extcolour_id"
					+ " LEFT JOIN axela_preowned_intcolour on intcolour_id = preowned_intcolour_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_preownedstock_id = preownedstock_id and so_active = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " WHERE 1 = 1" + StrSearch
					+ " AND preownedstock_id=" + preownedstock_id
					+ " GROUP BY preownedstock_id"
					+ " ORDER BY preownedstock_date";
			// SOP("StrSql---222----" + StrSql);
			// + " ORDER BY " + StrOrder;

			CachedRowSet crs = processQuery(StrSql, 0);
			// int count = 0;
			// int soamount = 0;
			// int receiptamount = 0;
			// int invoicedays = 0;
			// int stockallocationdays = 0;

			if (crs.isBeforeFirst()) {
				while (crs.next())
				{
					if (!crs.getString("preownedstock_date").equals("")) {
						stockdays = (int) Math.round(getDaysBetween(crs.getString("preownedstock_date"), ToLongDate(kknow())));
					} else {
						stockdays = 0;
					}
					count = count + 1;
					if (stockdays < 45) // invoicedays>=0 &&
					{
						bgcol = "#ffffff";
					} else if (stockdays >= 45 && stockdays <= 74) {
						bgcol = "orange";
					} else if (stockdays > 74) {
						bgcol = "red";
					}
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Stock ID: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("preownedstock_id")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Model: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("preownedmodel_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Variant: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<span>").append(crs.getString("variant_name"));
					if (!crs.getString("preowned_sub_variant").equals("")) {
						Str.append("<br>").append(crs.getString("preowned_sub_variant"));
					}
					Str.append("</span>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Exterior").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("extcolour_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Interior ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("intcolour_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Options").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("preowned_options")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Date").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(strToShortDate(crs.getString("preownedstock_date"))).append("<br>").append(stockdays).append(" Days").append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Status: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("preownedstatus_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Selling Price: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(IndFormat(crs.getString("preownedstock_selling_price"))).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Year: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("preowned_regdyear")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Insurance: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(strToShortDate(crs.getString("preowned_insur_date"))).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Kms: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(IndFormat(crs.getString("preowned_kms"))).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Reg. No.: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(SplitRegNo(crs.getString("preowned_regno"), 2)).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Location: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>");
					// Str.append("<a href=../sales/veh-quote-list.jsp?preownedstock_id=").append(crs.getString("preownedstock_id")).append(">");
					// if (!crs.getString("quotecount").equals("0")) {
					// Str.append(crs.getString("quotecount")).append("</a>\n");
					// }
					Str.append(crs.getString("preownedlocation_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Quote Count: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(SplitRegNo(crs.getString("quotecount"), 2)).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Sales Order: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>");
					if (!crs.getString("so_id").equals("0")) {
						Str.append("<SO ID: ").append(crs.getString("so_id"));
						if (!crs.getString("so_date").equals("")) {
							Str.append("<br>").append(strToShortDate(crs.getString("so_date"))).append("<br>").append(Math.round(getDaysBetween(crs.getString("so_date"), ToShortDate(kknow()))))
									.append(" Days");
						}
					}
					Str.append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Customer: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("customer_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>SO Amount: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>");
					if (!crs.getString("so_grandtotal").equals("")) {
						Str.append("").append(IndFormat(crs.getString("so_grandtotal"))).append("");
					}
					Str.append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

					// Str.append("<div class=\"form-group\">")
					// .append("<div class=\"col-md-12 col-xs-12\">")
					// .append("<label class=\"control-label col-md-4 col-xs-4\">")
					// .append("<b>Receipt Amount: ").append("</b></label>")
					// .append("<div class=\"col-md-8 col-xs-8\">")
					// .append("<p>").append("").append("</p>")
					// .append("</div>")
					// .append("</div>")
					// .append("</div>");

					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Branch: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("branch_name")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");
					Str.append("<div class=\"form-group\">")
							.append("<div class=\"col-md-12 col-xs-12\">")
							.append("<label class=\"control-label col-md-4 col-xs-4\">")
							.append("<b>Docs: ").append("</b></label>")
							.append("<div class=\"col-md-8 col-xs-8\">")
							.append("<p>").append(crs.getString("docs")).append("</p>")
							.append("</div>")
							.append("</div>")
							.append("</div>");

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
			// SOP("/---22222----" + StrHTML);
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
