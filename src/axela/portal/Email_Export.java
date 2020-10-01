package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Email_Export extends Connect {

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
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("emailstrsql", request).equals("")) {
					StrSearch = GetSession("emailstrsql", request);
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
			StrSql = " select email_id as ID, "
					+ " email_contact_id as 'Contact ID', "
					+ " email_contact as 'Contact', "
					+ " email_lead_id as 'Lead ID', "
					+ " email_lead as 'Lead', "
					+ " email_emp_id as 'Emp ID', "
					+ " email_emp as 'Executive', "
					+ " email_from as 'From', "
					+ " email_to as 'To', "
					+ " email_subject as 'Subject', "
					+ " email_msg as 'Message', "
					+ " DATE_FORMAT(email_date, '%d/%m/%Y') as Date,"
					+ " email_entry_id as 'Entry ID', "
					+ " if(email_sent=1,'yes','no') as Sent "
					+ " from " + compdb(comp_id) + "axela_email "
					+ " left  join " + compdb(comp_id) + "axela_emp on emp_id= email_emp_id "
					+ " left  join " + compdb(comp_id) + "axela_customer on customer_id= email_contact_id "
					+ " where 1=1" + StrSearch
					+ " group by email_id order by email_id desc";
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
