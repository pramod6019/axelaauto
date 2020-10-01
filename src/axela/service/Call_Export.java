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

public class Call_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "call-export.jsp";

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
				if (!GetSession("callstrsql", request).equals("")) {
					StrSearch = GetSession("callstrsql", request);
				}

				if (exportB.equals("Export")) {
					CallDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CallDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "Select call_id as 'ID', branch_name as 'Branch', COALESCE(veh_so_id, '') AS 'Number',"
					+ " DATE_FORMAT(call_entry_date,'%d/%m/%Y') as 'Service Call Date',"
					+ " customer_name AS 'Customer Name', COALESCE(veh_modelyear, '') AS 'ModelYear',"
					+ " COALESCE(veh_chassis_no, '') AS 'Vehicle Vin. No.',"
					+ " COALESCE(veh_engine_no, '') AS 'Vehicle Engine No.',"
					+ " COALESCE(veh_reg_no, '') AS 'vehicle Reg. No.',"
					+ " COALESCE(DATE_FORMAT(veh_sale_date, '%d/%m/%Y %H:%i'), '') AS 'Sale Date',"
					+ " IF(branch_active=1,'yes','no') as 'Active', COALESCE(veh_notes, '') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_service_call"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = call_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = call_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_call_type ON calltype_id = call_type_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = call_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " WHERE 1 = 1 " + StrSearch + BranchAccess + ExeAccess.replace("emp_id", "call_emp_id") + ""
					+ " group by call_id"
					+ " order by call_id";
			// SOP("StrSql=====" + StrSqlBreaker(StrSql));
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
		print = print + "<option value = CallDetails" + StrSelectdrop("CallDetails", printoption) + ">Call Details</option>\n";
		return print;
	}
}
