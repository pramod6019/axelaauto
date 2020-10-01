package axela.sales;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToXLSX;
import cloudify.connect.PrintToPDF;

public class Veh_Quote_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "veh-quote-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_export_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				exportcount = ExecuteQuery("SELECT comp_export_count FROM " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));

				if (!GetSession("quotestrsql", request).equals("")) {
					StrSearch = GetSession("quotestrsql", request);
				}
				if (exportB.equals("Export")) {
					QuoteDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void QuoteDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT quote_id AS ID,"
					+ " branch_name AS 'Branch',"
					+ " CONCAT(quote_prefix, quote_no) AS 'Quote No.',"
					+ " COALESCE(enquiry_dmsno, '') AS 'DMS No.',"
					+ " DATE_FORMAT(quote_date, '%d/%m/%Y') AS Date,"
					+ " COALESCE(customer_name, '') AS 'Customer Name',"
					+ " COALESCE(CONCAT(title_desc, ' ', lead_fname, ' ', lead_lname, ''), '') AS 'Lead Name',"
					+ " COALESCE(enquiry_title, '') AS 'Enquiry Name',"
					+ " quote_netamt AS 'Net Amount',"
					+ " quote_discamt AS 'Discount Amount',"
					+ " quote_totaltax AS 'Total Tax',"
					+ " quote_grandtotal AS 'Grand Total',"
					+ " quote_bill_address AS 'Billing Address',"
					+ " quote_bill_city AS 'Billing City',"
					+ " quote_bill_pin AS 'Billing Pin', "
					+ " quote_bill_state AS 'Billing State',"
					+ " quote_ship_city AS 'Shipping City',"
					+ " quote_ship_pin AS 'Shipping pin',"
					+ " quote_ship_state AS 'Shipping State',"
					+ " COALESCE(quote_desc, '') AS Descripition,"
					+ " COALESCE(quote_terms, '') AS Terms,"
					+ " COALESCE(emp_name, '') AS 'Employee Name',"
					+ " COALESCE(quote_refno, '') AS 'Reference No.',"
					+ " IF(quote_auth = 1, 'Yes', 'No') AS 'Authorisation',"
					+ " COALESCE(quote_auth_id, '') AS 'Authorised ID',"
					+ " IF(quote_auth_date != '', DATE_FORMAT(quote_auth_date, '%d/%m/%Y'), '') AS 'Authorised Date',"
					+ " IF(quote_active = 1, 'Yes', 'No') AS 'Active',"
					+ " COALESCE(quote_notes, '') AS 'Notes'"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = quote_lead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = lead_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " WHERE 1 = 1" + StrSearch + ""
					+ " GROUP BY quote_id"
					+ " ORDER BY quote_id DESC"
					+ " LIMIT " + exportcount + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				PrintToPDF exportToPDF = new PrintToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A2");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "<option value=QuoteDetails" + StrSelectdrop("QuoteDetails", printoption) + ">Quote Details</option>\n";
		return print;
	}
}
