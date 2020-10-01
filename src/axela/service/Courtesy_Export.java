package axela.service;
//Sri Venkatesh 08 APRIL 2013

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Courtesy_Export extends Connect {

	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String BranchAccess = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "courtesy-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				exportcount = ExecuteQuery("Select comp_export_count from " + compdb(comp_id) + "axela_comp");
				BranchAccess = GetSession("BranchAccess", request);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("courtesycarstrsql", request).equals("")) {
					StrSearch = GetSession("courtesycarstrsql", request) + "";
				}
				if (exportB.equals("Export")) {
					PickupDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PickupDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT courtesycar_id AS ID, customer_name AS Customer,"
					+ " concat(contact_fname, ' ', contact_lname) AS 'Contact Name', branch_name as Branch,"
					+ " courtesycar_mobile1 as Mobile1, courtesycar_mobile2 as Mobile2,"
					+ " courtesycar_landmark as Landmark, courtesycar_add as Address,"
					+ " concat(DATE_FORMAT(courtesycar_time_from,'%d/%m/%Y %h:%i'), '-', DATE_FORMAT(courtesycar_time_to,'%h:%i')) as Duration"
					+ " FROM " + compdb(comp_id) + "axela_service_courtesy_car"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_courtesy_vehicle on courtesyveh_id = courtesycar_courtesyveh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = courtesyveh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = courtesycar_customer_id"
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = courtesycar_contact_id"
					+ " where 1 = 1 " + StrSearch + BranchAccess
					+ " group by courtesycar_id"
					+ " order by courtesycar_id"
					+ " limit " + exportcount;
			// SOP("StrSql(11)===" + StrSqlBreaker(StrSql));
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
		String print = "";
		print = print + "<option value = CourtesycarDetails" + StrSelectdrop("CourtesycarDetails", printoption)
				+ ">Courtesy Car Details</option>\n";
		return print;
	}
}
