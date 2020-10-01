package axela.service;
/*
 *@author GuruMurthy TS 30 JAN 2012
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Coupon_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "";
	// = "coupon-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("couponstrsql", request).equals("")) {
					StrSearch = GetSession("couponstrsql", request);
				}

				if (exportB.equals("Export")) {
					couponDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void couponDetails(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "SELECT couponcampaign_id AS ID, "
					+ " COALESCE(brand_name, '') AS 'Brand',"
					+ " COALESCE(department_name, '') AS 'Department',"
					+ " COALESCE(couponcampaintype_name, '') AS 'Campaign Type',"
					+ " couponcampaign_name AS 'Campaign Name',"
					+ " couponcampaign_desc AS 'Campaign Desc',"
					+ " couponcampaign_startdate AS 'Start Date',"
					+ " couponcampaign_enddate AS 'End Date',"
					+ " couponcampaign_couponoffer AS 'Coupon Offer',"
					+ " couponcampaign_couponcount AS 'Coupon Count',"
					+ " couponcampaign_couponvalue AS 'Coupon Value',"
					+ " couponcampaign_budget AS 'Coupon Budget'"
					+ " FROM " + compdb(comp_id) + "axela_service_coupon_campaign"
					+ " INNER JOIN " + "axela_brand ON brand_id = couponcampaign_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_department ON department_id = couponcampaign_department_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_couponcampain_type ON couponcampaintype_id = couponcampaign_campaigntype_id"
					+ " WHERE 1 = 1";
			// SOP("StrSql===jcexport===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A1", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "<option value = Coupon Details" + StrSelectdrop("CouponDetails", printoption) + "> Coupon Details</option>\n";
		return print;
	}
}
