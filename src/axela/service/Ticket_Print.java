package axela.service;
/*
 * @author Gurumurthy TS 11 FEB 2013
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Ticket_Print extends Connect {

	public String emp_id = "0";
	public String branch_id = "0";
	public String brand_id = "0";
	public String comp_id = "0";
	public String printoption = "";
	public String printtype = "";
	public String printB = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String printpage = "ticket-print.jsp";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_export_access", request, response);
				printoption = PadQuotes(request.getParameter("report"));
				printtype = PadQuotes(request.getParameter("printtype"));
				printB = PadQuotes(request.getParameter("btn_print"));

				if (printB.equals("Print")) {
					Ticket_Maruti_Print marutiticketprint = new Ticket_Maruti_Print();
					marutiticketprint.reportfrom = "ticket-maruti-print";
					marutiticketprint.doPost(request, response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulatePrintOption() {
		String print = "";
		print = print + "<option value = MarutiTicketDetails" + StrSelectdrop("MarutiTicketDetails", printoption) + ">Maruti Ticket Details</option>\n";
		return print;
	}
}
