package axela.customer;
//saiman 21st june 2012

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Customer_Contact_Export extends Connect {

	public String emp_id = "";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String exportcount = "";
	public String exportpage = "customer-contact-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_export_access", request, response);
				exportcount = ExecuteQuery("select comp_export_count from " + compdb(comp_id) + "axela_comp");
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("contactstrsql", request).equals("")) {
					StrSearch = GetSession("contactstrsql", request);
				}
				if (exportB.equals("Export")) {
					ContactDetails(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void ContactDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "Select contact_id as ID,"
					+ " customer_name as Name,"
					+ " title_desc as 'Title',"
					+ " contact_fname as 'First Name',"
					+ " contact_lname as 'Last Name', "
					+ " contact_jobtitle as 'Job Title',"
					+ " contact_location as Location,"
					+ " contact_phone1 as Phone1,"
					+ " contact_phone2 as Phone2,"
					+ " contact_mobile1 as Mobile1,"
					+ " contact_mobile2 as Mobile2,"
					+ " contact_mobile3 as Mobile3,"
					+ " contact_mobile4 as Mobile4,"
					+ " contact_mobile5 as Mobile5,"
					+ " contact_mobile6 as Mobile6,"
					+ " contact_email1 as Email1,"
					+ " contact_email2 as Email2,"
					+ " contact_yahoo as Yahoo,"
					+ " contact_msn as MSN,"
					+ " contact_aol as AOL,"
					+ " contact_skype as Skype,"
					+ " contact_address as Address,"
					+ " coalesce(city_name,'') as City,"
					+ " contact_pin as Pin,"
					+ " contact_landmark as Landmark,"
					+ " if(contact_dob != '',DATE_FORMAT(contact_dob, '%d/%m/%Y'),'') as DOB,"
					+ " if(contact_anniversary != '',DATE_FORMAT(contact_anniversary, '%d/%m/%Y'),'') as Anniversary,"
					+ " if(contact_active=1,'yes','no') as Active,"
					+ " coalesce(contact_notes,'') as Notes"
					+ " from " + compdb(comp_id) + "axela_customer_contact"
					+ " inner JOIN " + compdb(comp_id) + "axela_title on title_id=contact_title_id"
					+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id"
					+ " left join " + compdb(comp_id) + "axela_city on city_id = contact_city_id"
					+ " left join " + compdb(comp_id) + "axela_state on state_id = city_state_id"
					+ " where 1=1" + StrSearch + ""
					+ " group by contact_id"
					+ " order by contact_id desc"
					+ " limit " + exportcount;
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
				exportToPDF.Export(request, response, crs, printoption, "A2", comp_id);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value =ContactDetails" + StrSelectdrop("ContactDetails", printoption) + ">Contact Details</option>\n";
		return print;
	}
}
