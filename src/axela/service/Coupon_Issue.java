package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Coupon_Issue extends Connect {

	public String StrHTML = "";
	public String StrSearch = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String branch_id = "0";
	public String brand_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String customer_id = "0";
	public String couponcampaign_department_id = "0";
	public String couponcampaign_campaigntype_id = "0", couponcampaign_id = "0";
	public String msg = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				brand_id = CNumeric(PadQuotes(request.getParameter("brand_id")));
				couponcampaign_department_id = CNumeric(PadQuotes(request.getParameter("dept")));

			}
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrincipal(String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					+ " AND branch_branchtype_id = 3"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("PopulateTeam query =====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_principal_id\" id=\"dr_principal_id\" class=\"form-control\" onChange=\"PopulateCouponCampaign();\" disabled>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateDepartment(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT department_id, department_name"
					+ " FROM  " + compdb(comp_id) + "axela_emp_department"
					+ " WHERE 1 = 1"
					+ " GROUP BY department_id"
					+ " ORDER BY department_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_dept_id\" id=\"dr_dept_id\" class=\"form-control\" onChange=\"PopulateCouponCampaign();\" disabled>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("department_id"));
				Str.append(StrSelectdrop(crs.getString("department_id"), couponcampaign_department_id));
				Str.append(">").append(crs.getString("department_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCouponCampaignType(String comp_id, HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT couponcampaintype_id, couponcampaintype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_couponcampain_type"
					+ " WHERE 1 = 1"
					+ " GROUP BY couponcampaintype_id"
					+ " ORDER BY couponcampaintype_name";
			// SOP("StrSql--------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_couponcampaign_type_id\" id=\"dr_couponcampaign_type_id\" class=\"form-control\" onChange=\"PopulateCouponCampaign();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("couponcampaintype_id"));
				Str.append(StrSelectdrop(crs.getString("couponcampaintype_id"), couponcampaign_campaigntype_id));
				Str.append(">").append(crs.getString("couponcampaintype_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCouponCampaign(String brand_id, String dept_id, String type_id, String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT couponcampaign_id, couponcampaign_name"
					+ " FROM " + compdb(comp_id) + "axela_service_coupon_campaign "
					+ " WHERE  1 = 1"
					+ " AND couponcampaign_brand_id = " + brand_id
					+ " AND couponcampaign_department_id = " + dept_id
					+ " AND couponcampaign_campaigntype_id = " + type_id
					+ " AND couponcampaign_active = '1' "
					+ " AND SUBSTR(couponcampaign_startdate, 1, 8) <= SUBSTR('" + ToLongDate(kknow()) + "', 1, 8) "
					+ " AND SUBSTR(couponcampaign_enddate, 1, 8) >= SUBSTR('" + ToLongDate(kknow()) + "', 1, 8) "
					+ " GROUP BY couponcampaign_id "
					+ " ORDER BY couponcampaign_name ";

			CachedRowSet crs = processQuery(StrSql, 0);

			// SOP("PopulateCouponCampaign------" + StrSql);

			Str.append("<select name=\"dr_couponcampaign_id\" id=\"dr_couponcampaign_id\" class=\"form-control\" onChange=\"PopulateCouponDetails();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("couponcampaign_id")).append("");
				Str.append(StrSelectdrop(crs.getString("couponcampaign_id"), couponcampaign_id));
				Str.append(">").append(crs.getString("couponcampaign_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
