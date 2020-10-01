package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Sms_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportpage = "email-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("smsstrsql", request).equals("")) {
					StrSearch = GetSession("smsstrsql", request);
				}
				if (exportB.equals("Export")) {
					EmailDetails(request, response);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void EmailDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " select sms_id as ID, "
					+ " sms_contact_id as 'Contact ID', "
					+ " sms_contact as 'Contact', "
					+ " sms_lead_id as 'Lead ID', "
					+ " sms_lead as 'Lead', "
					+ " sms_emp_id as 'Emp ID', "
					+ " sms_emp as 'Executive', "
					+ " sms_mobileno as 'Mobileno', "
					+ " sms_msg as 'Message', "
					+ " DATE_FORMAT(sms_date, '%d/%m/%Y') as Date,"
					+ " sms_entry_id as 'Entry ID', "
					+ " if(sms_sent=1,'yes','no') as Sent "
					+ " from " + compdb(comp_id) + "axela_sms "
					+ " left  join " + compdb(comp_id) + "axela_emp on emp_id= sms_emp_id "
					+ " left  join " + compdb(comp_id) + "axela_customer on customer_id= sms_contact_id "
					+ " where 1=1" + StrSearch
					+ " group by sms_id order by sms_id desc";
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
		print = print + "<option value =EmailDetails" + StrSelectdrop("EmailDetails", printoption) + ">Email Details</option>\n";
		return print;
	}
}
