package axela.service;
/*
 *@author Satish 25th march 2013
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Booking_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "booking-export.jsp";

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
				if (!GetSession("apptstrsql", request).equals("")) {
					StrSearch = GetSession("apptstrsql", request);
				}

				if (exportB.equals("Export")) {
					ApptDetails(request, response);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ApptDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT booking_id AS 'Booking ID',"
					+ " branch_name AS 'Branch',"
					+ " DATE_FORMAT(booking_time, '%d/%m/%Y %h:%m') AS 'Booking Date',"
					+ " customer_name AS 'Customer Name',"
					+ " contact_mobile1 AS 'Mobile No.',"
					+ " veh_modelyear AS 'ModelYear',"
					+ " veh_chassis_no AS 'Vehicle Vin. No.',"
					+ " veh_engine_no AS 'Vehicle Engine No.',"
					+ " veh_reg_no AS 'vehicle Reg. No.',"
					+ " bookingstatus_name AS 'Status',"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS 'Service Advisor'"
					+ " FROM " + compdb(comp_id) + "axela_service_booking"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = booking_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = booking_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = booking_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_booking_status ON bookingstatus_id = booking_bookingstatus_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = booking_service_emp_id"
					+ " WHERE 1 = 1" + StrSearch + BranchAccess + ""
					+ ExeAccess.replace("emp_id", "booking_service_emp_id") + ""
					+ " GROUP BY booking_id"
					+ " ORDER BY booking_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A4", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = BookingDetails" + StrSelectdrop("BookingDetails", printoption) + ">Booking Details</option>\n";
		return print;
	}
}
