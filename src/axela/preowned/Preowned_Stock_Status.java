package axela.preowned;

//

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Stock_Status extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String BranchAccess = "";
	public String msg = "";
	public String emp_id = "", branch_id = "";
	public String[] brand_ids, region_ids, branch_ids, team_ids, exe_ids, model_ids, soe_ids, preownedlocation_ids;
	public String brand_id = "", region_id = "", team_id = "", exe_id = "", model_id = "", soe_id = "";
	public String preowned_model_id = "0";
	public String preowned_variant_id = "0";
	public String preowned_fueltype_id = "0";
	public String preowned_ownership_id = "0";
	public String preowned_status_id = "0";
	public String stock_access = "";
	public String stock_branch_id = "";
	public String preownedlocation_id = "";
	public String preowned_branch_id = "0", dr_branch_id = "0";
	public String go = "";
	public String pending_delivery = "";
	public String preownedstock_blocked = "";
	public String ExeAccess = "";
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_preowned_access, emp_preowned_stock_access, emp_enquiry_access", request, response);
		if (!comp_id.equals("0"))
		{
			branch_id = CNumeric(GetSession("emp_branch_id", request));
			// BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = CheckNull(GetSession("ExeAccess", request));
			preowned_status_id = CNumeric(PadQuotes(request.getParameter("dr_preownedstatus_id")));
			pending_delivery = PadQuotes(request.getParameter("dr_pending_delivery_id"));
			preownedstock_blocked = CNumeric(PadQuotes(request.getParameter("dr_preownedstock_blocked")));
			preowned_ownership_id = CNumeric(PadQuotes(request.getParameter("dr_preownedownership_id")));
			go = PadQuotes(request.getParameter("submit_button1"));

			// stock_branch_id = branch_id;
			stock_access = ReturnPerm(comp_id, "emp_preowned_stock_access", request);

			GetValues(request, response);
			if (!preowned_model_id.equals("0")) {
				StrSearch += " AND preownedmodel_id = " + preowned_model_id + "";
				if (!preowned_variant_id.equals("0")) {
					StrSearch += " AND variant_id = " + preowned_variant_id + "";
				}
			}
			if (!preowned_status_id.equals("0")) {
				StrSearch += " AND preownedstatus_id = " + preowned_status_id + "";
			}

			if (!preowned_ownership_id.equals("0")) {
				StrSearch += " AND ownership_id = " + preowned_ownership_id + "";
			}

			if (!preowned_fueltype_id.equals("0")) {
				StrSearch += " AND fueltype_id = " + preowned_fueltype_id + "";
			}
			if (!brand_id.equals("")) {
				StrSearch += " AND branch_brand_id IN (" + brand_id + ")";
			}
			if (!region_id.equals("")) {
				StrSearch = StrSearch + " and branch_region_id IN (" + region_id + ")";
			}
			if (!branch_id.equals("")) {
				StrSearch += " AND branch_id IN (" + branch_id + ")";
			}
			if (!preownedlocation_id.equals("")) {
				StrSearch += " AND preownedstock_preownedlocation_id IN (" + preownedlocation_id + ")";
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
				// StrSearch +=
				// " AND preownedstock_id NOT IN (SELECT so_preownedstock_id"
				// + " FROM " + compdb(comp_id) + "axela_sales_so"
				// + " WHERE so_active = 1)";
				StrSearch += " AND so_preownedstock_id is null";
			}

			if (!preownedstock_blocked.equals("") && !preownedstock_blocked.equals("-1")) {
				StrSearch += " AND preownedstock_blocked = " + preownedstock_blocked + "";
			}

			if (go.equals("Go")) {
				CheckForm();
				try {
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					}
					if (msg.equals("")) {
						StrHTML = StockDetail();
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}

	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// SOP("branch_id-------" + branch_id);
		if (branch_id.equals("0")) {
			dr_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		} else {
			dr_branch_id = branch_id;
		}

		brand_id = RetrunSelectArrVal(request, "dr_principal");
		brand_ids = request.getParameterValues("dr_principal");
		region_id = RetrunSelectArrVal(request, "dr_region");
		region_ids = request.getParameterValues("dr_region");
		// SOP("region_ids-----------" + region_ids);
		// SOP("region_id-----------" + region_id);
		branch_id = RetrunSelectArrVal(request, "dr_branch");
		branch_ids = request.getParameterValues("dr_branch");
		preownedlocation_id = RetrunSelectArrVal(request, "dr_location_id");
		preownedlocation_ids = request.getParameterValues("dr_location_id");

	}

	public void CheckForm() {
		msg = "";
		if (brand_id.equals("")) {
			msg = msg + "<br>Select Brand!";
		}
	}

	public String StockDetail() {
		String bgcol = "";
		StringBuilder Str = new StringBuilder();
		try {
			// to know no of records depending on search
			StrSql = "SELECT "
					+ " COALESCE(preownedstock_id, 0) AS preownedstock_id,"
					+ " COALESCE(preowned_id, 0) AS preowned_id, "
					+ " COALESCE(preowned_title,'') AS preowned_title,  COALESCE(preownedmodel_id, 0) AS  preownedmodel_id,"
					+ " COALESCE(preownedmodel_name, '') AS preownedmodel_name,"
					+ " preownedstock_notes,"
					+ " COALESCE(variant_id, 0) AS variant_id, COALESCE(variant_name, '') AS variant_name,"
					+ " COALESCE(preownedstock_status_id, 0) AS preownedstock_status_id,"
					+ " COALESCE(preowned_sub_variant, '') AS preowned_sub_variant,"
					+ " COALESCE(extcolour_name, '') AS extcolour_name,"
					+ " COALESCE(intcolour_name, '') AS intcolour_name, COALESCE(preowned_options,'') AS preowned_options,"
					+ " COALESCE(preownedstock_blocked, 0) AS preownedstock_blocked,"
					+ " COALESCE(preownedstock_date,'') AS preownedstock_date,"
					+ " COALESCE(preownedstatus_id, 0) AS preownedstatus_id,"
					+ " COALESCE(preownedstatus_name, '') AS preownedstatus_name, preownedtype_name,"
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
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_type ON preownedtype_id = preownedstock_preownedtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_docs ON doc_preowned_id = preowned_id"
					+ " LEFT JOIN axela_preowned_extcolour on extcolour_id = preowned_extcolour_id"
					+ " LEFT JOIN axela_preowned_intcolour on intcolour_id = preowned_intcolour_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so on so_preownedstock_id = preownedstock_id and so_active = 1"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " WHERE 1 = 1 AND preownedstock_status_id = 1" + StrSearch
					+ " GROUP BY preownedstock_id"
					+ " ORDER BY preownedstock_date";

			// SOP("StrSql============" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			int count = 0;
			int stockdays = 0;

			Str.append("<div class=\"table-responsive table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");

			// Str.append("<br><table width=\"100%\" border=1 cellspacing=0 cellpadding=3 style=\"border-collapse:collapse;border-color:#726a7a;padding:3px;\">\n");
			if (crs.isBeforeFirst()) {
				Str.append("<thead><tr>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th data-toggle=\"true\">Stock ID</th>\n");
				Str.append("<th >Model</th>\n");
				Str.append("<th >Variant</th>\n");
				Str.append("<th data-hide=\"phone\">Exterior</th>\n");
				Str.append("<th data-hide=\"phone\">Interior</th>\n");
				Str.append("<th data-hide=\"phone\">Options</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Type</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Selling Price</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Year</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Insurance</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Kms</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Reg. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Location</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Quote Count</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Order</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">SO Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Docs</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
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
						bgcol = "orange";
					} else if (stockdays > 74) {
						bgcol = "red";
					}
					if (crs.getString("preownedstock_status_id").equals("2")) {
						bgcol = " ";
					}
					Str.append("<tr bgcolor=").append(bgcol).append(">\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>\n");
					Str.append("<td valign=top align=center><a href=\"../preowned/preowned-stock-list.jsp?preownedstock_id=").append(crs.getString("preownedstock_id")).append("\">");

					Str.append(crs.getString("preownedstock_id")).append("</a>\n");
					if (crs.getString("preownedstock_blocked").equals("1")) {
						Str.append("<br><font color=#ff0000>Blocked</font>");
					}
					// Str.append("<td valign=top align=left>").append("<a href=\"../preowned/preowned-list.jsp?preowned_id=");
					// Str.append(crs.getInt("preowned_id")).append("\">").append(crs.getString("preowned_title")).append("</a></td>");
					Str.append("</td><td valign=top align=left>").append(crs.getString("preownedmodel_name")).append("</td>");

					Str.append("<td valign=top align=left>").append("<a href=\"../preowned/managepreownedvariant.jsp?variant_id=");

					Str.append(crs.getInt("variant_id")).append("\">").append(crs.getString("variant_name")).append("</a>");

					if (!crs.getString("preowned_sub_variant").equals("")) {
						Str.append("<br>").append(crs.getString("preowned_sub_variant"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>");
					if (!crs.getString("extcolour_name").equals("")) {
						Str.append(crs.getString("extcolour_name"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>");
					if (!crs.getString("intcolour_name").equals("")) {
						Str.append(crs.getString("intcolour_name"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("preowned_options")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(strToShortDate(crs.getString("preownedstock_date"))).append("<br>").append(stockdays).append(" Days").append("</td>\n");
					Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("preownedtype_name")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(crs.getString("preownedstatus_name")).append("</td>\n");
					Str.append("<td valign=top align=left>").append(IndFormat(crs.getString("preownedstock_selling_price"))).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("preowned_regdyear")).append("</td>\n");
					Str.append("<td valign=top align=center>").append(strToShortDate((crs.getString("preowned_insur_date")))).append("</td>\n");

					Str.append("<td valign=top align=right>").append(IndFormat(crs.getString("preowned_kms"))).append("</td>\n");
					Str.append("<td valign=top align=left nowrap>").append(SplitRegNo(crs.getString("preowned_regno"), 2)).append("</td>\n");
					Str.append("<td valign=top align=left>")
							.append(crs.getString("preownedlocation_name"));
					if (!crs.getString("preownedstock_notes").equals("")) {
						Str.append("<br>Notes: " + crs.getString("preownedstock_notes"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=right>");
					Str.append("<a href=../sales/veh-quote-list.jsp?preownedstock_id=").append(crs.getString("preownedstock_id")).append(">");
					if (!crs.getString("quotecount").equals("0")) {
						Str.append(crs.getString("quotecount")).append("</a>").append("</td>\n");
					}
					Str.append("<td valign=top align=left>");
					if (!crs.getString("so_id").equals("0")) {
						Str.append("<a href=../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("so_id")).append(">SO ID: ").append(crs.getString("so_id")).append("</a>");
						if (!crs.getString("so_date").equals("")) {
							Str.append("<br>").append(strToShortDate(crs.getString("so_date"))).append("<br>").append(Math.round(getDaysBetween(crs.getString("so_date"), ToShortDate(kknow()))))
									.append(" Days");
						}
					}
					Str.append("</td>\n");
					Str.append("<td valign=top align=left>").append("<a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getString("customer_id")).append(">").append(crs.getString("customer_name")).append("</a>").append("</td>\n");
					Str.append("<td valign=top align=right>");
					if (!crs.getString("so_grandtotal").equals("")) {
						Str.append("").append(IndFormat(crs.getString("so_grandtotal"))).append("");
					}
					Str.append("</td>\n");
					// Str.append("<td valign=top align=right>");
					// if (!crs.getString("so_receiptamount").equals("")) {
					// SOP("llllll");
					// Str.append("").append(IndFormat(crs.getString("so_receiptamount"))).append("");
					// SOP("llllllsssss");
					// }
					// Str.append("</td>");
					Str.append("<td valign=top align=left ><a href=\"../portal/branch-summary.jsp?branch_id=");
					Str.append(crs.getString("preowned_branch_id")).append("\">").append(crs.getString("branch_name")).append("</a>");
					Str.append("</td>\n");

					Str.append("<td valign=top align=left >");
					Str.append(crs.getString("docs"));
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}

			} else {
				Str.append("<tr align=center>\n");
				Str.append("<td><br><br><br><br><b><font color=red>No Stock(s) found!</font></b><br><br></td>\n");
				Str.append("</tr>\n");
			}
			crs.close();
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateBrand() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ " AND branch_branchtype_id = 2"
					// + BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public String PopulateBranch(String brand_id, String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT branch_id, branch_name, branch_code"
	// + " FROM " + compdb(comp_id) + "axela_branch"
	// + " WHERE branch_active = 1"
	// + " AND branch_branchtype_id = 2";
	// if (!brand_id.equals("0")) {
	// StrSql += " AND branch_brand_id = " + brand_id;
	// }
	// StrSql += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
	// SOP("StrSql--------" + StrSql);
	// CachedRowSet crs = processQuery(StrSql, 0);
	// Str.append("<select name=\"dr_branch\" id=\"dr_branch\" class=\"form-control\" onchange=\"PopulateLocations();\">");
	// Str.append("<option value =0>Select Branch</option>");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("branch_id"));
	// Str.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
	// Str.append(">").append(crs.getString("branch_name"));
	// Str.append(" (").append(crs.getString("branch_code")).append(")</option>\n");
	// }
	// Str.append("</select>");
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	public String PopulateModel() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT preownedmodel_id, preownedmodel_name"
					+ " FROM axela_preowned_model"
					+ " ORDER BY preownedmodel_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id"));
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), preowned_model_id));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStatus(String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT preownedstatus_id, preownedstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock_status"
					+ " WHERE preownedstatus_id != 0"
					+ " ORDER BY preownedstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedstatus_id"));
				Str.append(StrSelectdrop(crs.getString("preownedstatus_id"), preowned_status_id));
				Str.append(">").append(crs.getString("preownedstatus_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateVariant(String preowned_model_id) {
		try {
			StringBuilder Str = new StringBuilder();
			Str.append("<select id=\"dr_variant_id\" name=\"dr_variant_id\" class=\"selectbox\">");
			Str.append("<option value=0>Select</option>");
			if (!preowned_model_id.equals("0")) {
				StrSql = "SELECT variant_id, variant_name AS Variant"
						+ " FROM axela_preowned_variant"
						+ " WHERE variant_preownedmodel_id = " + preowned_model_id
						+ " ORDER BY variant_name";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					Str.append("<option value=" + crs.getString("variant_id"));
					Str.append(StrSelectdrop(crs.getString("variant_id"), preowned_variant_id));
					Str.append(">" + crs.getString("Variant") + "</option> \n");
				}
				crs.close();
			}
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType() {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE fueltype_id != 0"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id"));
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), preowned_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateOwnership(String comp_id) {
		try {
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT ownership_id, ownership_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_ownership"
					+ " WHERE ownership_id != 0"
					+ " ORDER BY ownership_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("PopulateOwnership====" + StrSqlBreaker(StrSql));

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id"));
				Str.append(StrSelectdrop(crs.getString("ownership_id"), preowned_ownership_id));
				Str.append(">").append(crs.getString("ownership_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePendingdelivery() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", pending_delivery)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", pending_delivery)).append(">Yes</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", pending_delivery)).append(">No</option> \n");
		return Str.toString();
	}

	public String PopulateBlocked() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=-1").append(StrSelectdrop("-1", preownedstock_blocked)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", preownedstock_blocked)).append(">Blocked</option> \n");
		Str.append("<option value=0").append(StrSelectdrop("0", preownedstock_blocked)).append(">Not Blocked</option> \n");
		return Str.toString();
	}

	public String PopulateLocation(String[] preownedlocation_ids, String comp_id) {
		try {

			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT preownedlocation_id, preownedlocation_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_location"
					+ " WHERE 1=1"
					// + " and preownedlocation_branch_id IN (" + preowned_branch_id + ")"
					+ " GROUP BY preownedlocation_id"
					+ " ORDER BY preownedlocation_name";
			// SOP("PopulateLocation---------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_location_id\" class=\"form-control\" multiple=multiple size=10 id=\"dr_location_id\">");
			// Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlocation_id"));
				Str.append(ArrSelectdrop(crs.getInt("preownedlocation_id"), preownedlocation_ids));
				Str.append(">").append(crs.getString("preownedlocation_name")).append("</option>\n");
			}
			// Str.append("<select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePrincipal(String[] brand_ids, String comp_id, HttpServletRequest request) {

		// String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			// SOP(Str);
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					// + BranchAccess
					+ " AND branch_branchtype_id IN (2)"
					// + BranchAccess
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("PopulateTeam query======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(ArrSelectdrop(crs.getInt("brand_id"), brand_ids));
				Str.append(">").append(crs.getString("brand_name")).append("</option> \n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateRegion(String brand_id, String[] region_ids, String comp_id, HttpServletRequest request) {
		// String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT region_id, region_name "
					+ " FROM " + compdb(comp_id) + "axela_branch_region"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id = region_id"
					+ " WHERE 1=1 "
					+ " AND branch_active = 1  "
					+ " AND branch_branchtype_id IN (2)";
			// + BranchAccess;

			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}

			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";

			// SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_region id=dr_region class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"PopulateBranches();\"  style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("region_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("region_id"), region_ids));
					Str.append(">").append(crs.getString("region_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranches(String brand_id, String region_id, String[] branch_ids, String comp_id, HttpServletRequest request) {

		// String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		// SOP("branch_ids-----///----------" + branch_ids);
		try {
			StrSql = "SELECT branch_id, branch_name "
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE 1=1"
					+ " AND branch_active = 1 "
					+ " AND branch_branchtype_id IN (2)";
			// + BranchAccess;
			if (!brand_id.equals("")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}

			if (!region_id.equals("") && !region_id.equals("0")) {
				StrSql += " AND branch_region_id in (" + region_id + ") ";
			}
			StrSql += " GROUP BY branch_id "
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name ";

			// SOP("StrSql==PopulateBranches==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_branch id=dr_branch class='form-control multiselect-dropdown' multiple=multiple size=10 onchange=\"PopulateExecutives();\" style=\"padding:10px\">");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("branch_id")).append("");
					Str.append(ArrSelectdrop(crs.getInt("branch_id"), branch_ids));
					Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
				}
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
