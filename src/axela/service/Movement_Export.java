package axela.service;
//Bhagwan Singh

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Movement_Export extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "movement-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("vehmovestrsql", request).equals("")) {
					StrSearch = GetSession("vehmovestrsql", request);
				}
				if (exportB.equals("Export")) {
					MovementDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void MovementDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT vehmove_id AS 'ID',"
					+ " vehmove_reg_no AS 'Reg. No.',"
					+ " DATE_FORMAT(vehmove_timein, '%d/%m/%Y %H:%i') AS 'Time In',"
					+ " COALESCE(DATE_FORMAT(vehmove_timeout, '%d/%m/%Y %H:%i'), '') AS 'Time Out',"
					+ " COALESCE(jc_id, '0') AS 'JC ID',"
					+ " COALESCE(DATE_FORMAT(jc_time_in,'%d/%m/%Y %H:%i'), '') AS 'JobCard Time',"
					+ " COALESCE(veh_id, '0') AS 'Vehicle ID',"
					+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS 'Item',"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS 'Branch'"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_move"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehmove_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_reg_no = vehmove_reg_no"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_veh_id = veh_id"
					+ " AND SUBSTR(jc_time_in, 1, 8) = SUBSTR(vehmove_timein, 1, 8)"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
					+ " WHERE 1 = 1" + StrSearch + ""
					+ " GROUP BY vehmove_id"
					+ " ORDER BY vehmove_id DESC"
					+ " LIMIT " + exportcount;
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
		print = print + "<option value =CustomerDetails" + StrSelectdrop("MovementDetails", printoption) + ">Movement Details</option>\n";
		return print;
	}
}
