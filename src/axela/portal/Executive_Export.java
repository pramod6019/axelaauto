package axela.portal;
//Murali 21st aug 2012

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Executive_Export extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "executive-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("Select comp_export_count"
						+ " from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("exestrsql", request).equals("")) {
					StrSearch = GetSession("exestrsql", request);
				}
				// SOP("StrSql===" + session.getAttribute("StrSql"));
				// SOP("StrSearch===" + session.getAttribute("StrSearch"));
				if (!GetSession("ExportStrSearch", request).equals("")) {
					StrSearch = GetSession("ExportStrSearch", request);
				}
				if (exportB.equals("Export") && !StrSearch.equals("")) {
					ExecutiveDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void ExecutiveDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "Select emp_id as 'ID', "
					+ " emp_name as Name,"
					+ " emp_ref_no AS 'Ref. No.',"
					+ " emp_phone1 as Phone1,"
					+ " emp_phone2 as Phone2,"
					+ " emp_mobile1 as Mobile1,"
					+ " emp_mobile2 as Mobile2,"
					+ " emp_email1 as Email1,"
					+ " emp_email2 as Email2,"
					+ " COALESCE(branch_name, 'Head Office'),"
					+ " jobtitle_desc AS 'Job Title',"
					+ " department_name AS 'Department',"
					+ " IF(emp_dob != '', DATE_FORMAT(emp_dob, '%d/%m/%Y'), '') AS 'Date Of Birth',"
					+ " IF(emp_date_of_join != '', DATE_FORMAT(emp_date_of_join, '%d/%m/%Y'), '') AS 'Date Of Join',"
					+ " if(emp_active=1,'Yes','No') as Active"
					// + " emp_notes as Notes"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_role ON role_id = emp_role_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = emp_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " LEFT JOIN axela_branch_type on branch_branchtype_id = branchtype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch_region ON region_id = branch_region_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_department ON department_id = emp_department_id"
					+ " LEFT JOIN axela_brand ON brand_id = branch_brand_id "
					+ " where 1=1 " + StrSearch + ""
					+ " group by emp_id order by emp_name"
					+ " limit " + exportcount;
			// StrSql = StrSearch;
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value =ExecutiveDetails" + StrSelectdrop("ExecutiveDetails", printoption) + ">Executive Details</option>\n";
		return print;
	}
}
