package axela.customer;
//Murali 21st aug 2012

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Customer_Export extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "customer-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("Select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("customerstrsql", request).equals("")) {
					StrSearch = GetSession("customerstrsql", request);
				}
				if (exportB.equals("Export")) {
					CustomerDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void CustomerDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT customer_id AS 'ID', branch_name AS Branch, customer_name AS Name,"
					+ " customer_code AS Code, customer_phone1 AS Phone1, customer_phone2 AS Phone2,"
					+ " customer_phone3 AS Phone3, customer_phone4 as Phone4,"
					+ " customer_mobile1 AS Mobile1, customer_mobile2 AS Mobile2,"
					+ " customer_mobile3 AS Mobile3, customer_mobile4 AS Mobile4,"
					+ " customer_mobile5 AS Mobile5, customer_mobile6 AS Mobile6,"
					+ " customer_fax1 AS Fax1, customer_fax2 AS Fax2,"
					+ " customer_email1 AS Email1, customer_email2 AS Email2, customer_website1 AS WebSite1,"
					+ " customer_website2 AS WebSite2,"
					+ " customer_gst_no AS 'Customer GSTIN',"
					+ " COALESCE(date_format(customer_gst_regdate,'%d/%m/%Y'),'') AS 'Customer GSTIN Date',"
					+ " customer_arn_no AS 'Customer ARN',"
					+ " itstatus_name AS 'Status',"
					+ " customer_address AS Address,"
					+ " city_name AS City, customer_pin AS PAN, customer_landmark AS Landmark,"
					+ " customer_pan_no AS 'Pan No.',"
					+ " coalesce(soe_name,'') AS 'Source Of Enquiry',"
					+ " coalesce(sob_name,'') AS 'Source Of Business',"
					// + " coalesce(empcount_desc,'') as 'Employee Count',"
					+ " customer_curr_bal AS 'Current Balance',"
					+ " if(customer_active=1,'yes','no') AS Active,"
					+ " coalesce(DATE_FORMAT(customer_since, '%d/%m/%Y'),'') AS Since,"
					+ " coalesce(group_concat(group_desc order by group_desc separator ', '),'') AS 'Groups',"
					+ " customer_notes as Notes"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=customer_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id= city_state_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_itstatus ON itstatus_id = customer_itstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = customer_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = customer_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_group_trans ON customer_id = trans_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_group ON group_id = trans_group_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = customer_emp_id"
					+ " WHERE 1=1 " + StrSearch + ""
					+ " GROUP BY customer_id ORDER BY customer_name"
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
		print = print + "<option value =CustomerDetails" + StrSelectdrop("CustomerDetails", printoption) + ">Customer Details</option>\n";
		return print;
	}
}
