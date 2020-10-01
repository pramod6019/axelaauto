package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import axela.portal.Header;
import axela.sales.Enquiry_Quickadd;
import axela.sales.Enquiry_Transfer_Check;
import cloudify.connect.Connect;

public class App_Enquiry_Transfer extends Connect {

	public String StrSql = "";
	public String StrHTML = "", successmsg = "", transfer = "";
	public String enquiry_id = "0";
	public String comp_id = "0";
	public String emp_id = "0";
	public String emp_uuid = "";
	public String preowned_branch_id = "0";
	public String preowned_id = "0";
	public String enquiry_team_id = "0";
	public String enquiry_model_id = "0";
	public String enquiry_emp_id = "0";
	public String brand_id = "0", branch_id = "0 ", model_id = "0", item_id = "0", team_id = "0", executive_id = "0";
	public String BranchAccess = "", ExeAccess = "";
	public String add = "", msg = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				// add = PadQuotes(request.getParameter("add"));
				brand_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_brand_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_branch_id")));
				model_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_model_id")));
				item_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_item_id")));
				team_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_team_id")));
				executive_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_emp_id")));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				if (!enquiry_id.equals("0") && !brand_id.equals("0") && !branch_id.equals("0")
						&& !model_id.equals("0") && !item_id.equals("0") && !team_id.equals("0") && !executive_id.equals("0")) {
					Enquiry_Transfer_Check enqtrans = new Enquiry_Transfer_Check();
					Enquiry_Quickadd enqadd1 = new Enquiry_Quickadd();
					enqtrans.emp_id = emp_id;
					enqtrans.comp_id = comp_id;
					enqtrans.enquiry_id = enquiry_id;
					enqtrans.brand_id = brand_id;
					enqtrans.branch_id = branch_id;
					enqtrans.model_id = model_id;
					enqtrans.item_id = item_id;
					enqtrans.team_id = team_id;
					enqtrans.executive_id = executive_id;
					enqtrans.PopulateEnquiryFields(response);
					if (!enqtrans.enquiry_id.equals("0") && StrHTML.equals("")) {
						response.sendRedirect(response.encodeRedirectURL("callurl" + "app-enquiry-list.jsp?enquiry_id=" + enqtrans.enquiry_id + "&msg=" + msg));
					} else {
						msg = enqtrans.StrHTML;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
