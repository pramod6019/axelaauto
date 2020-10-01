package axela.mktg;
//Murali 21st jun

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Lead_Export extends Connect {

	public String emp_id = "";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String ExportPerm = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "lead-export.jsp";
	public String comp_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = (session.getAttribute("emp_id")).toString();
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			exportcount = ExecuteQuery("select comp_export_count from axela_comp");
			printoption = PadQuotes(request.getParameter("report"));
			exporttype = PadQuotes(request.getParameter("exporttype"));
			exportB = PadQuotes(request.getParameter("btn_export"));
			if (session.getAttribute("leadstrsql") != null) {
				StrSearch = session.getAttribute("leadstrsql").toString();
				SOP("StrSearch===" + StrSearch);
			}
			if (exportB.equals("Export")) {
				LeadDetails(request, response);
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void LeadDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "Select lead_id as ID,"
					+ " concat(branch_name,' (', branch_code, ')') as Branch,"
					+ " title_desc as 'Title',"
					+ " lead_fname as 'First Name',"
					+ " lead_lname as 'Last Name',"
					+ " lead_jobtitle as 'Job Title',"
					+ " DATE_FORMAT(lead_date, '%d/%m/%Y') as 'Date',"
					+ " lead_email as 'Email',"
					+ " lead_mobile as 'Mobile',"
					+ " lead_phone as 'Phone',"
					+ " lead_company as 'Company',"
					+ " coalesce(empcount_desc,'') as 'Employee Count',"
					+ " lead_req as 'Requirement',"
					+ " coalesce(soe_name,'') as 'Source Of Enquiry',"
					+ " coalesce(sob_name,'') as 'Source Of Business',"
					+ " coalesce(emp_name,'') as 'Executive',"
					+ " if(lead_active=1,'yes','no') as 'Active'"
					+ " from axela_mktg_lead"
					+ " inner join axela_branch on branch_id = lead_branch_id"
					+ " inner join axela_title on title_id = lead_title_id"
					+ " left join axela_soe on soe_id=lead_soe_id"
					+ " left join axela_sob on sob_id=lead_sob_id"
					+ " left join axela_empcount on empcount_id = lead_empcount_id"
					+ " left join axela_emp on emp_id = lead_emp_id"
					+ " where 1 = 1 " + StrSearch + ""
					+ " group by lead_id order by lead_id desc"
					+ " limit " + exportcount;
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				ExportToPDF exportToPDF = new ExportToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A3", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value =LeadDetails" + StrSelectdrop("LeadDetails", printoption) + ">Lead Details</option>\n";
		return print;
	}
}
