package axela.inventory;
/*Bhagwan Singh 21/01/2013*/

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class Stock_Dash extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrSql = "", SqlJoin = "";
	public String StrHTML = "";
	public String emp_role_id = "0";
	public String branch_brand_id = "0";
	public String vehstock_id = "0";
	public String vehstock_branch_id = "0";
	public String vehstock_item_id = "0";
	public String vehstock_modelyear = "";
	public String vehstock_mfgyear = "";
	public String vehstock_ref_no = "";
	public String vehstock_comm_no = "";
	public String vehstock_chassis_prefix = "";
	public String vehstock_chassis_no = "";
	public String vehstock_paintwork_id = "0";
	public String vehstock_engine_no = "";
	public String vehstock_fastag = "";
	public String vehstock_key_no = "";
	public String vehstock_vehstocklocation_id = "1";
	public String vehstock_parking_no = "";
	public String vehstock_confirmed_sgw = "", confirmed_sgw = "";
	public String vehstock_invoice_no = "";
	public String invoice_date = "", ordered_date = "";
	public String vehstock_invoice_date = "";
	public String vehstock_ordered_date = "";
	public String vehstock_invoice_amount = "";
	public String vehstock_invoiceamountaftertax = "";
	public String vehstock_principalsupport = "";
	public String pi_date = "";
	public String vehstock_arrival_date = "";
	public String vehstock_pdi_date = "";
	public String vehstock_dms_date = "";

	public String vehstock_nsc = "";
	public String arrival_date = "";
	public String nadcon_date = "";
	public String vehstock_stockpriority_id = "0";
	public String vehstock_delstatus_id = "0", chk_delstatus_id = "0";
	public String vehstock_status_id = "0";
	public String vehstock_intransit_damage = "";
	public String rectification_date = "";
	public String vehstock_rectification_date = "";
	public String vehstock_blocked = "";
	public String vehstock_incentive = "";
	public String vehstock_notes = "";
	public String vehstock_entry_id = "0";
	public String vehstock_entry_date = "";
	public String vehstock_entry_by = "";
	public String vehstock_modified_id = "0";
	public String vehstock_modified_date = "";
	public String vehstock_modified_by = "";
	// public String entry_by = "", entry_date = "";
	// public String modified_by = "", modified_date = "";
	public City_Check citycheck = new City_Check();
	public DecimalFormat deci = new DecimalFormat("0.00");
	DecimalFormat df = new DecimalFormat("#.#");

	public String QueryString = "";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));

			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_stock_access", request, response);
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				PopulateFields(response);
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

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT vehstock_id, vehstock_modelyear, vehstock_mfgyear, vehstock_ref_no, vehstock_comm_no,"
					+ " vehstock_chassis_prefix, vehstock_chassis_no, vehstock_engine_no, vehstock_fastag,"
					+ " COALESCE(vehstock_confirmed_sgw, '') AS vehstock_confirmed_sgw, vehstock_key_no,"
					+ " vehstock_vehstocklocation_id, vehstock_parking_no, branch_brand_id,"
					+ " COALESCE((SELECT option_id FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1 AND trans_vehstock_id = vehstock_id), 0) AS paintwork_id,"
					+ " vehstock_item_id, vehstock_branch_id, vehstock_blocked, vehstock_incentive,"
					+ " vehstock_invoice_no, vehstock_ordered_date, vehstock_invoice_date, vehstock_rectification_date,"
					+ " vehstock_arrival_date, vehstock_pdi_date, vehstock_dms_date, vehstock_nsc,"
					+ " vehstock_invoice_amount, vehstock_invoiceamountaftertax, vehstock_principalsupport,"
					+ " vehstock_delstatus_id, vehstock_ex_price,"
					+ " vehstock_status_id, "
					+ " vehstock_stockpriority_id, "
					+ " vehstock_intransit_damage,"
					+ " vehstock_notes, vehstock_entry_id, vehstock_entry_date, vehstock_modified_id, vehstock_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
					+ " WHERE vehstock_id = " + vehstock_id
					+ BranchAccess + "";
			// SOP("StrSql===po==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehstock_branch_id = crs.getString("vehstock_branch_id");
					vehstock_item_id = crs.getString("vehstock_item_id");
					vehstock_modelyear = crs.getString("vehstock_modelyear");
					vehstock_mfgyear = crs.getString("vehstock_mfgyear");
					vehstock_ref_no = crs.getString("vehstock_ref_no");
					vehstock_comm_no = crs.getString("vehstock_comm_no");
					vehstock_chassis_prefix = crs.getString("vehstock_chassis_prefix");
					vehstock_chassis_no = crs.getString("vehstock_chassis_no");
					vehstock_engine_no = crs.getString("vehstock_engine_no");
					vehstock_fastag = crs.getString("vehstock_fastag");
					vehstock_key_no = crs.getString("vehstock_key_no");
					vehstock_vehstocklocation_id = crs.getString("vehstock_vehstocklocation_id");
					vehstock_parking_no = crs.getString("vehstock_parking_no");
					vehstock_paintwork_id = crs.getString("paintwork_id");
					vehstock_confirmed_sgw = crs.getString("vehstock_confirmed_sgw");
					confirmed_sgw = strToShortDate(vehstock_confirmed_sgw);
					vehstock_ordered_date = crs.getString("vehstock_ordered_date");
					ordered_date = strToShortDate(vehstock_ordered_date);
					vehstock_invoice_no = crs.getString("vehstock_invoice_no");
					vehstock_invoice_date = crs.getString("vehstock_invoice_date");
					invoice_date = strToShortDate(vehstock_invoice_date);
					vehstock_invoice_amount = deci.format(crs.getDouble("vehstock_invoice_amount"));
					vehstock_invoiceamountaftertax = deci.format(crs.getDouble("vehstock_invoiceamountaftertax"));
					vehstock_principalsupport = deci.format(crs.getDouble("vehstock_principalsupport"));
					vehstock_arrival_date = crs.getString("vehstock_arrival_date");
					arrival_date = strToShortDate(vehstock_arrival_date);
					vehstock_pdi_date = strToShortDate(crs.getString("vehstock_pdi_date"));
					vehstock_dms_date = strToShortDate(crs.getString("vehstock_dms_date"));
					vehstock_nsc = crs.getString("vehstock_nsc");
					vehstock_delstatus_id = crs.getString("vehstock_delstatus_id");
					vehstock_status_id = crs.getString("vehstock_status_id");
					vehstock_intransit_damage = crs.getString("vehstock_intransit_damage");
					vehstock_rectification_date = crs.getString("vehstock_rectification_date");
					rectification_date = strToShortDate(vehstock_rectification_date);
					vehstock_stockpriority_id = crs.getString("vehstock_stockpriority_id");
					vehstock_blocked = crs.getString("vehstock_blocked");
					vehstock_incentive = df.format(crs.getDouble("vehstock_incentive"));
					vehstock_notes = crs.getString("vehstock_notes");
					branch_brand_id = crs.getString("branch_brand_id");
					vehstock_entry_id = crs.getString("vehstock_entry_id");
					vehstock_entry_by = Exename(comp_id, crs.getInt("vehstock_entry_id"));
					vehstock_entry_date = strToLongDate(crs.getString("vehstock_entry_date"));
					vehstock_modified_id = crs.getString("vehstock_modified_id");
					if (!vehstock_modified_id.equals("0")) {
						vehstock_modified_by = Exename(comp_id, Integer.parseInt(vehstock_modified_id));
						vehstock_modified_date = strToLongDate(crs.getString("vehstock_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Stock!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateItem(String comp_id, String branch_id) {
		// SOP("vehstock_item_id==" + vehstock_item_id);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " INNER JOIN axela_brand on brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_brand_id = model_brand_id"
					+ " WHERE item_type_id = 1"
					+ " AND item_name != 'Pre Owned'"
					+ " AND item_active = 1"
					+ " AND branch_id = " + branch_id
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("StrSql==item==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_vehstock_item_id\" class=\"form-control\" id=\"dr_vehstock_item_id\" onchange=\"SecurityCheck('dr_vehstock_item_id',this,'hint_dr_vehstock_item_id')\" >");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), vehstock_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDeliveryStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT delstatus_id, delstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " ORDER BY delstatus_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("delstatus_id"));
				Str.append(StrSelectdrop(crs.getString("delstatus_id"), vehstock_delstatus_id));
				Str.append(">").append(crs.getString("delstatus_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_status"
					+ " ORDER BY status_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("status_id"));
				Str.append(StrSelectdrop(crs.getString("status_id"), vehstock_status_id));
				Str.append(">").append(crs.getString("status_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateStockPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehstockpriority_id, vehstockpriority_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_priority"
					+ " ORDER BY vehstockpriority_rank";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstockpriority_id"));
				Str.append(StrSelectdrop(crs.getString("vehstockpriority_id"), vehstock_stockpriority_id));
				Str.append(">").append(crs.getString("vehstockpriority_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT vehstocklocation_id, vehstocklocation_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " WHERE 1=1"
					// + " vehstocklocation_branch_id = " + vehstock_branch_id + ""
					+ " ORDER BY vehstocklocation_name";

			// SOP("StrSql==========" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_vehstock_vehstocklocation_id\" class=\"form-control\" id=\"dr_vehstock_vehstocklocation_id\" onchange=\"SecurityCheck('dr_vehstock_vehstocklocation_id',this,'hint_dr_vehstock_vehstocklocation_id')\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("vehstocklocation_id"));
				Str.append(StrSelectdrop(crs.getString("vehstocklocation_id"), vehstock_vehstocklocation_id));
				Str.append(">").append(crs.getString("vehstocklocation_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePaintWork(String comp_id, String vehstock_branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, CONCAT(option_name,' (',option_code,')')as option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = option_brand_id"
					+ " WHERE 1 = 1"
					+ " AND option_optiontype_id = 1";
			if (!vehstock_branch_id.equals("0")) {
				StrSql += " AND branch_id = " + vehstock_branch_id;
			}
			StrSql += " GROUP BY option_name"
					+ " ORDER BY option_name";
			// SOP("StrSql == paintwork =" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_vehstock_paintwork_id\" class=\"form-control\" id=\"dr_vehstock_paintwork_id\" onchange=\"SecurityCheck('dr_vehstock_paintwork_id',this,'hint_dr_vehstock_paintwork_id')\">");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("option_id"));
				Str.append(StrSelectdrop(crs.getString("option_id"), vehstock_paintwork_id));
				Str.append(">").append(crs.getString("option_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String HistoryDetails(String comp_id, String vehstock_id) {
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = "SELECT " + compdb(comp_id) + "axela_vehstock_history.*,"
					+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name,emp_id "
					+ " FROM " + compdb(comp_id) + "axela_vehstock_history"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = history_vehstock_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = history_emp_id "
					+ " WHERE history_vehstock_id = " + vehstock_id + ""
					+ " ORDER BY history_id desc";
			// SOP("StrSql------ListHistory------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {
				if (crs.isBeforeFirst()) {
					Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
					Str.append("<div class=\"caption\" style='float: none'>History</div>\n</div>\n");
					Str.append("<div class=\"portlet-body portlet-empty\">");
					Str.append("<div class=\"tab-pane\" id=''>");
					Str.append("<div class=\"table-responsive \">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">Date</th>");
					Str.append("<th>Action By</th>");
					Str.append("<th data-hide=\"phone, tablet\">Type of Action</th>");
					Str.append("<th data-hide=\"phone, tablet\">New Value</th>");
					Str.append("<th data-hide=\"phone, tablet\"> Old Value</th>");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						Str.append("<tr>\n");
						Str.append("<td valign=top align=left >").append(strToLongDate(crs.getString("history_datetime"))).append("</td>");
						Str.append("<td valign=top align=left >");
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("emp_id")).append(">")
								.append(crs.getString("emp_name")).append("</a>").append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("history_actiontype")).append(" </td>");
						Str.append("<td valign=top align=left >").append(crs.getString("history_newvalue")).append("</td>");
						Str.append("<td valign=top align=left >").append(crs.getString("history_oldvalue")).append("</td>");
						Str.append("</tr>" + "\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>");
					Str.append("</div>\n </div>\n </div></div>\n");
				} else {
					Str.append("<div class=\"portlet box\"><div class=\"portlet-title\" style='text-align: center'>");
					Str.append("<div class=\"caption\" style='float: none'>History</div></div>");
					Str.append("<div class=\"portlet-body portlet-empty\">");
					Str.append("<div class=\"tab-pane\" id=''>");
					Str.append("<div align=center><br><br><font color=red><b>No History found!</b></font></div>");
					Str.append("</div> </div></div></div>");
				}
				crs.close();
				/*
				 * Str.append("</td>"); Str.append("</tr>"); Str.append("</table>"); Str.append("</div>");
				 */// /

			} catch (Exception ex) {
				SOPError("AxelaAuto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return Str.toString();
	}
}
