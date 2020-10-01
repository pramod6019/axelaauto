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

public class App_Preownedstock_List extends Connect {

	public String emp_id = "";
	public String StrOption = "";
	public String StrTeam = "";
	public String model_id = "0", location_id = "0";
	public String item_id = "0";
	public String option_id = "0";
	public String delstatus_id = "0";
	public String order_by = "";
	public String StrOrder = "", stock_blocked = "";
	public String brand_id = "0";
	public String region_id = "0", fueltype_id = "0";
	public String team_id = "0";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String StrHTML = "";
	public String StrSearch = "";
	public String comp_id = "0", emp_uuid = "";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String preowned_model_id = "0";
	public String preowned_variant_id = "0";
	public String preowned_fueltype_id = "0";
	public String preowned_ownership_id = "0";
	public String preowned_status_id = "0";
	public String stock_access = "";
	public String stock_branch_id = "0";
	public String preownedlocation_id = "0";
	public String preowned_branch_id = "0";
	public String go = "";
	public String pending_delivery = "";
	public String preownedstock_blocked = "";
	public String ExeAccess = "";

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
		new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		if (!comp_id.equals("0")) {
			branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
			ExeAccess = CheckNull(GetSession("ExeAccess", request));
			preowned_variant_id = CNumeric(PadQuotes(request.getParameter("dr_variant_id")));
			preowned_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_preownedfuel_id")));
			preowned_ownership_id = CNumeric(PadQuotes(request.getParameter("dr_preownedownership_id")));
			preowned_model_id = CNumeric(PadQuotes(request.getParameter("dr_preownedmodel_id")));
			brand_id = CNumeric(PadQuotes(request.getParameter("drop_brand_id")));
			region_id = CNumeric(PadQuotes(request.getParameter("dr_region")));
			preowned_status_id = CNumeric(PadQuotes(request.getParameter("dr_preownedstatus_id")));
			pending_delivery = CNumeric(PadQuotes(request.getParameter("dr_pending_delivery_id")));
			preownedstock_blocked = CNumeric(PadQuotes(request.getParameter("dr_preownedstock_blocked")));
			preownedlocation_id = CNumeric(PadQuotes(request.getParameter("dr_location_id")));
			go = PadQuotes(request.getParameter("submit_button1"));
			// if (branch_id.equals("0")) {
			// stock_branch_id == CNumeric(PadQuotes(request.getParameter("dr_branch")));
			// } else {
			stock_branch_id = branch_id;
			// }
			stock_access = ReturnPerm(comp_id, "emp_preowned_stock_access", request);

			if (!preowned_model_id.equals("0")) {
				StrSearch += " AND preownedmodel_id = " + preowned_model_id + "";
				if (!preowned_variant_id.equals("0")) {
					StrSearch += " AND variant_id = " + preowned_variant_id + "";
				}
			}

			if (!preowned_status_id.equals("0")) {
				StrSearch += " AND preownedstatus_id = " + preowned_status_id + "";
			}
			if (!brand_id.equals("0")) {
				StrSearch += " AND branch_brand_id = " + brand_id + "";
			}

			if (!region_id.equals("0")) {
				StrSearch += " AND branch_region_id = " + region_id + "";
			}

			if (!preowned_ownership_id.equals("0")) {
				StrSearch += " AND ownership_id = " + preowned_ownership_id + "";
			}

			if (!preowned_fueltype_id.equals("0")) {
				StrSearch += " AND fueltype_id = " + preowned_fueltype_id + "";
			}
			if (!stock_branch_id.equals("0")) {
				StrSearch += " AND branch_id = " + stock_branch_id + "";
			}

			if (stock_access.equals("0")) {
				StrSearch += " AND preownedstock_blocked = 0";
			}

			if (pending_delivery.equals("")) {
				pending_delivery = "2";
			}

			if (pending_delivery.equals("0")) {
				StrSearch += " AND (preownedstock_id NOT IN (SELECT so_preownedstock_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_active = 1)"
						+ " OR preownedstock_id IN (SELECT so_preownedstock_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_delivered_date = ''"
						+ " AND so_active = 1"
						// + BranchAccess.replace("branch_id", "so_branch_id")
						+ ExeAccess.replace("emp_id", "so_emp_id") + "))";
			} else if (pending_delivery.equals("1")) {
				StrSearch += " AND preownedstock_id IN (SELECT so_preownedstock_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_delivered_date = ''"
						+ " AND so_active = 1"
						// + BranchAccess.replace("branch_id", "so_branch_id")
						+ ExeAccess.replace("emp_id", "so_emp_id") + ")";
			} else if (pending_delivery.equals("2")) {
				// StrSearch += " AND preownedstock_id NOT IN (SELECT so_preownedstock_id"
				// + " FROM " + compdb(comp_id) + "axela_sales_so"
				// + " WHERE so_active = 1)";
				StrSearch += " AND so_preownedstock_id is null";
			}

			if (!preownedstock_blocked.equals("") && !preownedstock_blocked.equals("-1")) {
				StrSearch += " AND preownedstock_blocked = " + preownedstock_blocked + "";
			}

			if (!preownedlocation_id.equals("0")) {
				StrSearch += " AND preownedstock_preownedlocation_id = " + preownedlocation_id + "";
			}

			if (go.equals("Go")) {
				CheckForm();
				try {
					if (msg.equals("")) {
						StrHTML = PreownedStockDetail(comp_id);
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}
	public void CheckForm() {
		msg = "";
		try {
			// if (stock_branch_id.equals("0")) {
			// // msg = msg + "<br>Select Branch!";
			// }
			// if (model_id.equals("0")) {
			// // msg = msg + "<br>Select Model!";
			// }

		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	public String PreownedStockDetail(String comp_id) {
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		int grandsoamount = 0;
		int grandreceiptamount = 0;
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
					+ " GROUP BY preownedstock_id"
					+ " ORDER BY preownedstock_date";

			// CountSql = "SELECT COUNT(DISTINCT(stock_id))";
			// TotalRecords = Integer.parseInt(ExecuteQuery(StrSql));
			// SOP("TotalRecords--" + TotalRecords);
			// totalcount = TotalRecords / 25;
			// StrSql += LimitRecords(TotalRecords,
			// Integer.toString(pagecurrent));
			// SOP("StrSql===PreownedStockDetail===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			int stockdays = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<tr>\n")
						.append("<th>#</th>\n")
						.append("<th>Stock ID </th>\n")
						.append("<th>Model </th>\n")
						.append("<th>Variant </th>\n")
						.append("<th>Selling Price </th>\n")
						.append("<th>Year </th>\n")
						.append("<th>Kms </th>\n")
						.append("<th>Location </th>\n")
						// .append("<th> </th>")
						.append("</tr>");

				while (crs.next()) {
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
						bgcol = "#ffc04c"; // orange
					} else if (stockdays > 74) {
						bgcol = "#ff7f7f"; // red
					}
					// if (crs.getString("preownedstock_status_id").equals("2")) {
					// bgcol = "green";
					// }
					Str.append("<tr bgcolor=").append(bgcol).append(">\n");
					Str.append("<td>").append(count).append("</td>\n");
					Str.append("<td><a href=\"callurlapp-preownedstock-details.jsp?preownedstock_id=").append(crs.getString("preownedstock_id")).append("\"").append(">");
					Str.append(crs.getString("preownedstock_id")).append("</a>\n");
					// Str.append("<td valign=top align=left>").append("<a href=\"../preowned/preowned-list.jsp?preowned_id=");
					// Str.append(crs.getInt("preowned_id")).append("\">").append(crs.getString("preowned_title")).append("</a></td>");
					Str.append("</td><td>").append(crs.getString("preownedmodel_name")).append("</td>\n");
					Str.append("<td>").append(crs.getString("variant_name"));
					if (!crs.getString("preowned_sub_variant").equals("")) {
						Str.append("<br>").append(crs.getString("preowned_sub_variant"));
					}
					Str.append("</td>\n");
					Str.append("<td>").append(IndFormat(crs.getString("preownedstock_selling_price"))).append("</td>\n");
					Str.append("<td>").append(crs.getString("preowned_regdyear")).append("</td>\n");
					Str.append("<td>").append(IndFormat(crs.getString("preowned_kms"))).append("</td>\n");
					Str.append("<td>").append(crs.getString("preownedlocation_name")).append("</td>\n");
					Str.append("</tr>\n");
				}
				// // if (jscrollcount == (TotalRecords - 1)) {
				// Str.append("</div>");
				// }

			}
			// SOP("count------" + count);
			else {
				Str.append("<div class=\"container\" align=\"center\"><b><h4>&nbsp;</h4>\n").append("No Pre-Owned Stock(s) Found!").append("</b></div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
