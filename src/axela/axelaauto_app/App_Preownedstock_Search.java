package axela.axelaauto_app;
///// divya
// modified by sn 6, 7 may 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import cloudify.connect.Connect;

public class App_Preownedstock_Search extends Connect {

	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0", BranchAccess = "";
	public String msg = "";
	public String model_id = "0", location_id = "0";
	public String item_id = "0";
	public String option_id = "0";
	public String delstatus_id = "0";
	public String pending_delivery = "2";
	public String order_by = "";
	public String StrOrder = "", vehstock_blocked = "0";
	public String vehstock_access = "";
	public String brand_id = "0", region_id = "0";
	public String vehstock_branch_id = "0";
	public String team_id = "0";
	public String go = "";
	public String ExeAccess = "";
	public String emp_uuid = "";
	public String access = "0", fueltype_id = "0";
	public axela.preowned.MIS_Check mischeck = new axela.preowned.MIS_Check();

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
				// branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				go = PadQuotes(request.getParameter("submit_button"));

			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}

	public String PopulateBrand(String comp_id) {
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

			Str.append("<option value=\"0\">Select</option>\n");
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

	public String PopulateBranch(String brand_id, String region_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
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
			Str.append("<select name=\"dr_branch\" id=\"dr_branch\" class=\"form-control\">");
			Str.append("<option value =0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("branch_id"));
				Str.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name"));
				Str.append("</option>\n");
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

	public String PopulateRegion(String brand_id, String comp_id) {
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
			if (!brand_id.equals("0")) {
				StrSql += " AND branch_brand_id in (" + brand_id + ") ";
			}
			StrSql += " GROUP BY region_id "
					+ " ORDER BY region_name ";
			// SOP("StrSql------PopulateRegion-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=dr_region id=dr_region class=form-control  onchange=\"PopulateBranch();\">");
			Str.append("<option value =0>Select</option>");
			// if (crs.isBeforeFirst()) {
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("region_id")).append("");
				Str.append(StrSelectdrop(crs.getString("region_id"), ""));
				Str.append(">").append(crs.getString("region_name")).append("</option> \n");
				// }
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
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), ""));
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
				Str.append(StrSelectdrop(crs.getString("preownedstatus_id"), ""));
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
					Str.append(StrSelectdrop(crs.getString("variant_id"), ""));
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
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), ""));
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
					+ " ORDER BY ownership_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id"));
				Str.append(StrSelectdrop(crs.getString("ownership_id"), ""));
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
		Str.append("<option value=-1").append(StrSelectdrop("-1", vehstock_blocked)).append(">Select</option> \n");
		Str.append("<option value=1").append(StrSelectdrop("1", vehstock_blocked)).append(">Blocked</option> \n");
		Str.append("<option value=0").append(StrSelectdrop("0", vehstock_blocked)).append(">Not Blocked</option> \n");
		return Str.toString();
	}

	public String PopulateLocation(String preowned_branch_id, String comp_id) {
		try {
			// SOP("preowned_branch_id = " + preowned_branch_id);
			// if(preowned_branch_id.equals("0")){
			// preowned_branch_id = branch_id;
			// }
			StringBuilder Str = new StringBuilder();
			StrSql = "SELECT preownedlocation_id, preownedlocation_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_location"
					+ " WHERE 1=1"
					// + " and preownedlocation_branch_id IN (" + preowned_branch_id + ")"
					+ " GROUP BY preownedlocation_id"
					+ " ORDER BY preownedlocation_name";
			// SOP("StrSql---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=\"dr_location_id\" class=\"selectbox\" id=\"dr_location_id\">");
			Str.append("<option value=0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlocation_id"));
				Str.append(StrSelectdrop(crs.getString("preownedlocation_id"), ""));
				Str.append(">").append(crs.getString("preownedlocation_name")).append("</option>\n");
			}
			Str.append("<select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
