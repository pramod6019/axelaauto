package axela.invoice;
//Murali 23 july

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToXLSX;
import cloudify.connect.PrintToPDF;

public class Quote_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "quote-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void QuoteDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = " select quote_id as ID,"
					+ " branch_name as 'Branch',"
					+ " coalesce(quote_no,'') as 'Quote No.',"
					+ " DATE_FORMAT(quote_date,'%d/%m/%Y') as Date,"
					+ " coalesce(customer_name,'') as 'Customer Name',"
					+ " coalesce(CONCAT(title_desc,' ',lead_fname,' ',lead_lname,''),'') as 'Lead Name',"
					+ " coalesce(enquiry_title,'') as 'Enquiry Name',"
					+ " quote_netamt as 'Net Amount',"
					+ " quote_discamt as 'Discount Amount',"
					+ " quote_totaltax as 'Total Tax',"
					+ " quote_grandtotal as 'Grand Total',"
					+ " quote_bill_address as 'Billing Address',"
					+ " quote_bill_city as 'Billing City',"
					+ " quote_bill_pin as 'Billing Pin', "
					+ " quote_bill_state as 'Billing State',"
					+ " quote_ship_city as 'Shipping City',"
					+ " quote_ship_pin as 'Shipping pin',"
					+ " quote_ship_state as 'Shipping State',"
					+ " coalesce(quote_desc,'') as Descripition,"
					+ " coalesce(quote_terms,'') as Terms,"
					+ " coalesce(emp_name,'') as 'Employee Name',"
					+ " coalesce(quote_refno,'') as 'Reference No.',"
					+ " if(quote_auth=1,'yes','no') as 'Authorisation', "
					+ " coalesce(quote_auth_id,'') as 'Authorised ID',"
					+ " if(quote_auth_date != '',DATE_FORMAT(quote_auth_date, '%d/%m/%Y'),'') as 'Authorised Date',"
					+ " if(quote_active=1,'yes','no') as 'Active',"
					+ " coalesce(quote_notes,'') as 'Notes'"
					+ " from " + compdb(comp_id) + "axela_sales_quote "
					+ " inner join " + compdb(comp_id) + "axela_branch on branch_id=quote_branch_id "
					+ " left join " + compdb(comp_id) + "axela_sales_lead on lead_id = quote_lead_id "
					+ " left join " + compdb(comp_id) + "axela_title on title_id = lead_title_id "
					+ " left join " + compdb(comp_id) + "axela_emp on emp_id = quote_emp_id "
					+ " left join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id=quote_enquiry_id "
					+ " left join " + compdb(comp_id) + "axela_customer on customer_id=quote_customer_id "
					+ " where 1=1" + StrSearch + " "
					+ " group by quote_id order by quote_id desc"
					+ " limit " + exportcount;
			// SOP("StrSql==="+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (exporttype.equals("xlsx")) {
				ExportToXLSX exportToXLSX = new ExportToXLSX();
				exportToXLSX.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("html")) {
				ExportToHTML exportToHTML = new ExportToHTML();
				exportToHTML.Export(request, response, crs, printoption, comp_id);
			} else if (exporttype.equals("pdf")) {
				// ExportToPDF exportToPDF = new ExportToPDF();
				// exportToPDF.Export(request, response, rs, printoption, "A2");
				PrintToPDF exportToPDF = new PrintToPDF();
				exportToPDF.Export(request, response, crs, printoption, "A2");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = QuoteDetails" + StrSelectdrop("QuoteDetails", printoption) + ">Quote Details</option>\n";
		return print;
	}
}
