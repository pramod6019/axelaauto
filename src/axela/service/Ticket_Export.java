package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.ExportToHTML;
import cloudify.connect.ExportToPDF;
import cloudify.connect.ExportToXLSX;

public class Ticket_Export extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String exporttype = "";
	public String exportB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String exportpage = "ticket-export.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);

				CheckPerm(comp_id, "emp_export_access", request, response);
				printoption = PadQuotes(request.getParameter("report"));
				exporttype = PadQuotes(request.getParameter("exporttype"));
				exportB = PadQuotes(request.getParameter("btn_export"));
				if (!GetSession("ticketstrsql", request).equals("")) {
					StrSearch = GetSession("ticketstrsql", request);
				}
				if (StrSearch.contains("ticket.branch_id")) {
					StrSearch = StrSearch.replace("ticket.branch_id", "ticket_branch_id");
				}
				if (StrSearch.contains(" customer.branch_active")) {
					StrSearch = StrSearch.replace("customer.branch_active", " branch_active");
				}
				if (exportB.equals("Export")) {
					TicketDetails(request, response);

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void TicketDetails(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT ticket_id AS 'ID', ticket_parent_id AS 'Parent ID',"
					+ " ticketsource_name AS 'Source', DATE_FORMAT(ticket_report_time,'%d/%m/%Y %H:%i') AS 'Report Time', "
					+ " ticketstatus_name AS 'Status', COALESCE(customer_name,'') AS 'Customer Name',"
					+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),'') AS 'Contact Name', "
					+ " COALESCE(contact_mobile1,'') AS 'Contact Mobile',"
					+ " COALESCE(contact_email1,'')  AS 'Contact Email',"
					+ " COALESCE(variant_name,'') AS 'Item Name',"
					+ " ticket_veh_id AS 'Vehicle ID',"
					+ " COALESCE(veh_reg_no,'') AS 'Reg. No.', COALESCE(veh_chassis_no,'') AS 'Chassis No.',"
					+ " ticket_jc_id AS 'JC ID', COALESCE(jc_ro_no, '') AS 'RO No.', "
					+ " ticket_dept_name AS 'Department', COALESCE(ticketcat_name,'') AS 'Category',"
					+ " COALESCE(tickettype_name,'') AS 'Type',priorityticket_name AS 'Priority', "
					+ " DATE_FORMAT(ticket_due_time,'%d/%m/%Y %H:%i') AS 'Due Time',"
					+ " concat(emp_name,' (',emp_ref_no,')') AS 'Executive', "
					+ " ticket_subject AS 'Subject', ticket_desc AS 'Description',"
					+ "  COALESCE(branch_name,'') AS 'Branch',"
					+ " (SELECT crmconcern_desc FROM "
					+ compdb(comp_id) + "axela_sales_crm"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crm_concern ON crmconcern_id = crm_crmconcern_id"
					+ " WHERE crm_id = ticket_crm_id ) AS 'Sales Concern',"
					+ " (SELECT jcpsfconcern_desc FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psf_concern ON jcpsfconcern_id = jcpsf_jcpsfconcern_id"
					+ " WHERE jcpsf_id = ticket_jcpsf_id ) AS 'Service Concern',"
					+ " ticket_closed_comments AS 'Closing Comments',"
					+ " ticket_reopened_comments AS 'Reopened Comments', "
					+ " DATE_FORMAT(ticket_closed_time, '%d/%m/%Y') AS 'Close Date',"
					+ " ticket_notes AS Notes "
					+ " FROM "
					+ compdb(comp_id) + "axela_service_ticket "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = ticket_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_source ON ticketsource_id = ticket_ticketsource_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_status ON ticketstatus_id = ticket_ticketstatus_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_dept ON ticket_dept_id = ticket_ticket_dept_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_ticket_priority ON priorityticket_id = ticket_priorityticket_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = ticket_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = ticket_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = ticket_branch_id"
					+ BranchAccess
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_cat ON ticketcat_id = ticket_ticketcat_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_ticket_type ON tickettype_id = ticket_tickettype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = ticket_jc_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ticket_veh_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " WHERE 1 = 1 "
					+ StrSearch
					+ ExeAccess + ""
					+ " GROUP BY ticket_id ORDER BY ticket_id ";
			// SOPInfo("StrSql===" + StrSql);
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
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = TicketDetails" + StrSelectdrop("TicketDetails", printoption) + ">Ticket Details</option>\n";
		return print;
	}
}
