package axela.axelaauto_app;
///// divya
// modified by sn 6, 7 may 2013
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Exestock_Search extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String model_id = "0", vehstocklocation_id = "0";
	public String item_id = "0";
	public String option_id = "0";
	public String delstatus_id = "0";
	public String vehstock_status_id = "0";
	public String pending_delivery = "2";
	public String order_by = "";
	public String StrOrder = "", vehstock_blocked = "";
	public String vehstock_access = "";
	public String brand_id = "0";
	public String vehstock_branch_id = "0";
	public String team_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String emp_uuid = "";
	public String access = "0", fueltype_id = "0";
	public int branchcount = 0;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!comp_id.equals("0")) {
				access = ReturnPerm(comp_id, "emp_report_access, emp_mis_access, emp_enquiry_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				emp_id = CNumeric(GetSession("emp_id", request));
				StrSql = "SELECT COUNT(branch_id) AS branchcount, branch_brand_id,"
						+ " IF(COUNT(branch_id) = 1, branch_id, 0) AS branch_id"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE branch_active = 1"
						+ " AND branch_branchtype_id = 1"
						+ BranchAccess;
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					branchcount = Integer.parseInt(CNumeric(crs.getString("branchcount")));
					branch_id = CNumeric(crs.getString("branch_id"));
					brand_id = CNumeric(crs.getString("branch_brand_id"));
				}
				crs.close();
				if (branch_id.equals("0")) {
					branch_id = CNumeric(GetSession("emp_branch_id", request));
				}
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));

			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	// private void populateData() throws SQLException {
	// int countBranch = 0;
	// String StrSql = "SELECT COUNT(branch_id) AS branchCount"
	// + " FROM " + compdb(comp_id) + "axela_branch"
	// + " WHERE branch_active='1'"
	// + " AND branch_branchtype_id = 1"
	// + BranchAccess;
	// CachedRowSet crs = processQuery(StrSql, 0);
	// while (crs.next()) {
	// branchCount = Integer.parseInt(PadQuotes(crs.getString("branchCount")));
	// }
	// if (branchCount == 1) {
	// StrSql = "SELECT branch_id,branch_brand_id"
	// + " FROM " + compdb(comp_id) + "axela_branch"
	// + " WHERE branch_active='1'"
	// + " AND branch_branchtype_id = 1"
	// + BranchAccess;
	// crs = processQuery(StrSql, 0);
	// while (crs.next()) {
	// branch_id = PadQuotes(crs.getString("branch_id"));
	// brand_id = PadQuotes(crs.getString("branch_brand_id"));
	// }
	// }
	// crs.close();
	// }

	public String PopulatePrincipal(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = '1'"
					+ " AND branch_branchtype_id = 1" + BranchAccess
					+ BranchAccess
					+ " GROUP BY brand_id"
					+ " ORDER BY brand_name";
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
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBranch(String principle_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		String SqlStr = "SELECT branch_id, branch_name, branch_code"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " WHERE branch_active='1'"
				+ " AND branch_branchtype_id = 1" + BranchAccess
				+ BranchAccess;
		if (!principle_id.equals("0")) {
			SqlStr += " AND branch_brand_id = " + principle_id;
		}
		SqlStr += " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
		CachedRowSet crs = processQuery(SqlStr, 0);
		try {
			// stringval.append("<select name=\"dr_branch\" id=\"dr_branch\" class=\"form-control\" onchange=\"PopulateTeam(this.value); PopulateModel(this.value);\">");
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), vehstock_branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			// stringval.append("</select>");
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateRegion(String principle_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		StrSql = "SELECT region_id, region_name"
				+ " FROM " + compdb(comp_id) + "axela_branch_region"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
				+ BranchAccess;
		if (!brand_id.equals("")) {
			StrSql += " AND branch_brand_id IN (" + brand_id + ") ";
		}
		StrSql += " GROUP BY region_id"
				+ " ORDER BY region_name";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			// stringval.append("<select name=\"dr_branch\" id=\"dr_branch\" class=\"form-control\" onchange=\"PopulateTeam(this.value); PopulateModel(this.value);\">");
			stringval.append("<option value =0>Select</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("region_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("region_id"), ""));
				stringval.append(">").append(crs.getString("region_name")).append("</option>\n");
			}
			// stringval.append("</select>");
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateStatus(String comp_id) {
		try {
			StrSql = "SELECT delstatus_id, delstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
					+ " WHERE delstatus_id != 0"
					+ " ORDER BY delstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0> Select </option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("delstatus_id") + "";
				group = group + StrSelectdrop(crs.getString("delstatus_id"), "");
				group = group + ">" + crs.getString("delstatus_name") + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStockStatus(String comp_id) {
		try {
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_status"
					+ " WHERE status_id != 0"
					+ " ORDER BY status_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0> Select </option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("status_id") + "";
				group = group + StrSelectdrop(crs.getString("status_id"), vehstock_status_id);
				group = group + ">" + crs.getString("status_name") + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePendingdelivery(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", pending_delivery)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", pending_delivery)).append(">Yes</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", pending_delivery)).append(">No</option> \n");
		return Str.toString();
	}

	public String PopulateTeam(String branch_id, String comp_id) {
		StringBuilder stringval = new StringBuilder();
		try {
			String SqlStr = "SELECT team_id, team_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE team_branch_id = " + branch_id
					+ " ORDER BY team_name";
			CachedRowSet crs = processQuery(SqlStr, 0);
			// stringval.append("<select id=\"dr_team_id\" name=\"dr_team_id\" class=\"form-control\">");
			// stringval.append("<option value =0>Select Team</option>");
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("team_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("team_id"), team_id));
				stringval.append(">").append(crs.getString("team_name")).append("</option>\n");
			}
			// stringval.append("</select>");
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}

	public String PopulateModel(String brand_id, String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ BranchAccess
					+ " WHERE model_active = '1' "
					+ " AND model_sales = '1' ";
			if (!brand_id.equals("0"))
			{
				StrSql += " AND model_brand_id = " + brand_id;
			}
			if (!branch_id.equals("0"))
			{
				StrSql += " AND branch_id = " + branch_id;
			}
			StrSql += " GROUP BY model_id"
					+ " ORDER BY brand_name, model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_model_id\" class=\"form-control\" id=\"dr_model_id\" onChange=\"PopulateItem(this.value);\">");
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("model_id") + "");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">" + crs.getString("model_name") + "</option> \n");
			}
			// Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String model_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_item_id = item_id"
					+ " WHERE item_type_id = 1"
					+ " AND model_sales = 1 AND model_active = 1"
					+ " AND item_active = 1"
					+ " AND model_id = " + model_id + ""
					+ " AND vehstock_id NOT IN (SELECT so_vehstock_id FROM " + compdb(comp_id) + "axela_sales_so "
					+ " WHERE so_active =1 AND so_delivered_date!='')"
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("StrSql---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select id=\"dr_item_id\" name=\"dr_item_id\" class=\"form-control\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			// Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateColour(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, CONCAT(option_name, ' (', option_code, ')') AS option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_inventory_item ON item_id = option_item_id"
					+ " WHERE 1 = 1"
					// + " item_id = " + item_id + ""
					+ " GROUP BY option_id"
					+ " ORDER BY option_name";
			// SOP("StrSql--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select id=\"dr_option_id\" name=\"dr_option_id\" class=\"selectbox\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("option_id")).append("");
				Str.append(StrSelectdrop(crs.getString("option_id"), option_id));
				Str.append(">").append(crs.getString("option_name")).append("</option>\n");
			}
			// Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1=1"
					+ " GROUP BY fueltype_id"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id"));
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), fueltype_id));
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

	public String PopulateBlocked(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=-1").append(StrSelectdrop("-1", vehstock_blocked)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", vehstock_blocked)).append(">Blocked</option> \n");
		Str.append("<option value=0").append(StrSelectdrop("0", vehstock_blocked)).append(">Not Blocked</option> \n");
		return Str.toString();
	}

	public String PopulateLocation(String comp_id) {
		try {
			StrSql = "SELECT vehstocklocation_id, vehstocklocation_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " ORDER BY vehstocklocation_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			String group = "<option value=0> Select </option>";
			while (crs.next()) {
				group = group + "<option value=" + crs.getString("vehstocklocation_id") + "";
				group = group + StrSelectdrop(crs.getString("vehstocklocation_id"), vehstocklocation_id);
				group = group + ">" + crs.getString("vehstocklocation_name") + "</option> \n";
			}
			crs.close();
			return group;
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateOrderBy(String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0").append(StrSelectdrop("0", order_by)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", order_by)).append(">Invoice Date</option> \n");
		Str.append("<option value=2").append(StrSelectdrop("2", order_by)).append(">Allocation Date</option> \n");
		Str.append("<option value=3").append(StrSelectdrop("3", order_by)).append(">Status</option> \n");
		Str.append("<option value=4").append(StrSelectdrop("4", order_by)).append(">Engine No.</option> \n");
		Str.append("<option value=5").append(StrSelectdrop("5", order_by)).append(">Quote Count</option> \n");
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
