package axela.insurance;
/*
 *@author Satish 2nd march 2013
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Vehicle_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportpage = "vehicle-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("vehstrsql", request).equals("")) {
					StrSearch = GetSession("vehstrsql", request);
				}

				if (exportB.equals("Export")) {
					VehicleDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void VehicleDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT veh_id AS 'ID',"
					+ " customer_name AS 'Customer',"
					+ " CONCAT(title_desc, ' ', contact_fname, contact_lname) AS 'Contact',"
					+ " COALESCE(customer_mobile1, '') AS 'Mobile1',"
					+ " COALESCE(customer_mobile2, '') AS 'Mobile2',"
					+ " COALESCE(customer_mobile3, '') AS 'Mobile3',"
					+ " COALESCE(customer_mobile4, '') AS 'Mobile4',"
					+ " COALESCE(customer_mobile5, '') AS 'Mobile5',"
					+ " COALESCE(customer_mobile6, '') AS 'Mobile6',"
					+ " COALESCE(customer_email1, '') AS 'Email1',"
					+ " COALESCE(customer_email2, '') AS 'Email2',"
					+ " COALESCE(customer_phone1, '') AS 'Phone1',"
					+ " COALESCE(customer_phone2, '') AS 'Phone2',"
					+ " CONCAT(customer_address, ', ', city_name, ' - ', customer_pin) AS 'Address',"
					+ " veh_so_id AS 'SO ID',"
					+ " model_name AS 'Model',"
					+ " CONCAT(item_name, ' (', item_code,')') AS 'Item',"
					+ " veh_reg_no AS 'Reg. No.',"
					+ " veh_chassis_no AS 'Vin. No.',"
					+ " veh_engine_no AS 'Engine No.',"
					+ " IF(veh_iacs = 1, 'Yes', 'No') AS IACS,"
					+ " veh_modelyear AS 'Model Year',"
					+ " veh_kms AS 'Last Kms',"
					+ " veh_cal_kms AS 'Calculated Kms',"
					+ " COALESCE(DATE_FORMAT(veh_lastservice,'%d/%m/%Y'), '') AS 'Last Service Date',"
					+ " COALESCE(DATE_FORMAT(veh_sale_date,'%d/%m/%Y'), '') AS 'Sale Date',"
					+ " COALESCE(emp_name, '') AS 'Advisor',"
					+ " COALESCE(branch_name, '') AS 'Branch',"
					+ " DATE_FORMAT(veh_entry_date,'%d/%m/%Y') AS 'Service Date',"
					+ " IF(branch_active=1,'Yes','No') AS 'Active',"
					+ " veh_notes AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN axela_preowned_varint ON variant_id = veh_variant_id"
					+ " INNER JOIN axela_preowned_model ON model_id = variant_preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = veh_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_id = veh_so_id"
					+ " WHERE 1 = 1 " + StrSearch + ""
					+ " GROUP BY veh_id"
					+ " ORDER BY veh_id";
			// SOP("StrSql========" + StrSqlBreaker(StrSql));
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
		print = print + "<option value = VehicleDetails" + StrSelectdrop("VehicleDetails", printoption) + ">Vehicle Details</option>\n";
		return print;
	}
}
