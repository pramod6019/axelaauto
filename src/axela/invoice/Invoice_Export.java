package axela.invoice;
//Divya 5th dec

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Invoice_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";

	public String BranchAccess = "";
	public String exportpage = "invoice-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("invoicestrsql", request).equals("")) {
					StrSearch = GetSession("invoicestrsql", request);
				}
				if (exportB.equals("Export")) {
					InvoiceDetails(request, response);
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void InvoiceDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT invoice_id as 'ID',"
					+ " concat(invoice_prefix, invoice_no) as 'No.',"
					+ " concat(branch_name,' (', branch_code, ')') as 'Branch',"
					+ " DATE_FORMAT(invoice_date, '%d/%m/%Y') as 'Date',"
					+ " customer_name as 'Customer',"
					+ " concat(title_desc,' ',contact_fname,' ',contact_lname) as 'Contact',"
					+ " concat(emp_name,' (', emp_ref_no, ')') as 'Executive',"
					+ " if(invoice_active=1,'yes','no') as 'Active',"
					+ " invoice_notes as 'Notes'"
					+ " from " + compdb(comp_id) + "axela_invoice"
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=invoice_branch_id"
					+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = invoice_emp_id"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = invoice_customer_id"
					+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = invoice_contact_id"
					+ " inner join " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " where invoice_active = 1 " + StrSearch + BranchAccess + ""
					+ " group by invoice_id order by invoice_id desc"
					+ " limit " + exportcount;
			// SOP("StrSql-xxxxxx-"+StrSql);
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value =InvoiceDetails" + StrSelectdrop("InvoiceDetails", printoption) + ">Invoice Details</option>\n";
		return print;
	}
}
