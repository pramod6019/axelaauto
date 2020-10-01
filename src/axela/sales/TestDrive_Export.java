package axela.sales;
//Murali 23 july

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class TestDrive_Export extends Connect {
	
	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "testdrive-export.jsp";
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				
				if (!GetSession("testdrivestrsql", request).equals("")) {
					StrSearch = GetSession("testdrivestrsql", request);
					if (!StrSearch.equals("")) {
						StrSearch = StrSearch.replace("emp_branch_id", "main.emp_branch_id");
						StrSearch = StrSearch.replace("emp_active", "main.emp_active");
					}
					// emp_id is passed FROM list testdrive
					if (GetSession("testdrivestrsql", request).contains("emp_id")) {
						StrSearch = GetSession("testdrivestrsql", request).replace("emp_id", "main.emp_id").
								replace(" emp_active", " main.emp_active");
						StrSearch = StrSearch.replace("teamtrans_main.emp_id", "teamtrans_emp_id");
					}
					// emp_id is passed from monitoring board search query
					StrSearch = StrSearch.replace(" emp_id", " main.emp_id").
							replace(" emp_active", " main.emp_active")
							.replace(" emp_branch_id", " main.emp_branch_id");
					
				}
				// SOP("StrSearch======" + StrSearch);
				if (exportB.equals("Export")) {
					TestDriveDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void TestDriveDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " SELECT testdrive_id AS 'Test Drive ID', testdriveveh_id AS 'Vehicle ID', testdriveveh_name AS 'Vehicle Name', "
					+ " branch_code AS 'Branch Code', customer_name AS 'Customer Name', "
					+ " contact_id AS 'Contact ID', concat(contact_fname,' ', contact_lname) AS 'Contact Name',"
					+ " contact_mobile1 AS 'Mobile1', contact_mobile2 AS 'Mobile2',   "
					+ " COALESCE(model_name, '') AS  model_name, "
					+ " COALESCE(DATE_FORMAT(testdrive_time_from, '%d/%m/%Y %h:%i'),'') AS 'Test Drive From',"
					+ " COALESCE(DATE_FORMAT(testdrive_time_to, '%d/%m/%Y %h:%i'),'') AS 'Test Drive To',"
					+ " testdrive_type AS 'Test Drive Type', testdrive_confirmed AS 'Test Drive Confirmed',  "
					+ " COALESCE(testdrive_notes, '') AS 'Test Drive Notes',  concat('ENQ',branch_code,enquiry_no) AS 'Enquiry No',  "
					+ " testdrive_doc_value AS 'Test Drive Type', customer_id AS 'Customer ID', enquiry_id AS 'Enquiry ID',"
					+ " branch_id AS 'Branch ID', CONCAT(branch_name,' (',branch_code,')') AS 'Branch Name', "
					+ " main.emp_id AS 'Employee ID', CONCAT(main.emp_name,' (', main.emp_ref_no, ')') AS 'Employee Name',"
					+ " CONCAT(manager.emp_name,' (', manager.emp_ref_no, ')') AS 'Manager Name', "
					+ " COALESCE(DATE_FORMAT(testdrive_time, '%d/%m/%Y %h:%i'),'') AS 'Test Drive Time',"
					+ " location_name AS 'Location Name', "
					+ " COALESCE(DATE_FORMAT(testdrive_in_time, '%d/%m/%Y %h:%i'),'') AS 'Test Drive In Time',"
					+ " testdrive_in_kms AS 'Test Drive In Kms',"
					+ " COALESCE(DATE_FORMAT(testdrive_out_time, '%d/%m/%Y %h:%i'),'') AS 'Test Drive Out Time',"
					+ " testdrive_out_kms AS 'Test Drive Out Kms', testdrive_fb_taken AS 'Test Drive Feedback', "
					+ " COALESCE(testdrive_fb_status_id,'0') AS 'Test Drive Feedback Status',  "
					+ " COALESCE(status_name, '') AS 'Status Name', "
					+ " testdrive_fb_status_comments AS 'Feedback Comments',"
					+ " COALESCE(soe_name, '') AS 'SOE',"
					+ " COALESCE(sob_name, '') AS 'SOB',"
					+ " testdrive_fb_budget AS 'Budget', "
					+ " testdrive_fb_finance AS 'Finance', testdrive_fb_finance_amount AS 'Amount', "
					+ " testdrive_fb_finance_comments AS 'Comments', testdrive_fb_insurance AS 'Insurance', "
					+ " testdrive_fb_insurance_comments AS 'Insurance Comments'  "
					+ " FROM " + compdb(comp_id) + "axela_sales_testdrive  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location ON location_id= testdrive_location_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = testdrive_enquiry_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id "
					+ " INNER JOIN " + compdb(comp_id) + " axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle ON testdriveveh_id = testdrive_testdriveveh_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = testdriveveh_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id   "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp main ON main.emp_id = testdrive_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = testdrive_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp manager ON manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status ON status_id= testdrive_fb_status_id  "
					+ " WHERE 1=1"
					+ " AND testdrive_id > 0"
					+ StrSearch + " "
					+ " GROUP BY testdrive_id "
					+ " ORDER BY testdrive_id DESC"
					+ " limit " + exportcount;
			SOPInfo("StrSql=tsetdrive export==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} // else if (exporttype.equals("pdf")) {
				// // ExportToPDF exportToPDF = new ExportToPDF();
				// // exportToPDF.Export(request, response, rs, printoption, "A2");
				// PrintToPDF exportToPDF = new PrintToPDF();
				// exportToPDF.Export(request, response, rs, printoption, "A2");
				// }
			else if (exporttype.equals("pdf")) {
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
		print = print + "<option value = TestDriveDetails" + StrSelectdrop("TestDriveDetails", printoption) + ">Test Drive Details</option>\n";
		return print;
	}
}
