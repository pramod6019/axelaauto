/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

/**
 *
 * @author Gopal
 */
public class Search extends Connect {

	public static String msg = "";
	public String StrSql = "";
	public String PermStr = "";
	public String ExeAccess = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String activity_exe_id = "";
	public String branch_id = "";
	public String BranchAccess = "";
	public String BatchTimeTableStr = "";
	public String go = "";
	public String module = "";
	public String type = "";
	public String search = "";
	public String value = "";
	public String StrNews = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				SetSession("activity_emp_id", emp_id, request);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				search = PadQuotes(request.getParameter("txt_search"));
				module = PadQuotes(request.getParameter("dr_module_id"));
				type = PadQuotes(request.getParameter("dr_module_type"));
				go = PadQuotes(request.getParameter("btn_go"));
				StringBuilder str = new StringBuilder();
				search = str.append(search).toString();
				search = search.trim();
				if (go.equals("Go")) {
					if (search != null && search != "" && !search.isEmpty())
					{
						response.sendRedirect(response.encodeRedirectURL(SearchModule(search, module, type)));
					}

				} else {
					// StrNews = ListNews();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
		}
	}
	public String PopulateModule() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=Enquiry>Enquiry</option>\n");
			Str.append("<option value=TestDrive>Test Drive</option>\n");
			Str.append("<option value=Quote>Quote</option>\n");
			Str.append("<option value=SalesOrder>Sales Order</option>\n");
			Str.append("<option value=Pre-Owned>Pre-Owned</option>\n");
			Str.append("<option value=Ticket>Ticket</option>\n");
			Str.append("<option value=Stock>Stock</option>\n");
			Str.append("<option value=Vehicle>Vehicle</option>\n");
			Str.append("<option value=JobCard>Job Card</option>\n");
			Str.append("<option value=Insurance>Insurance</option>\n");
			Str.append("<option value=Item>Item</option>\n");
			Str.append("<option value=Voucher>Voucher</option>\n");
			if (emp_id.equals("1"))
			{
				Str.append("<option value=Executives>Executives</option>\n");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModuleType() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=Keyword>Keyword</option>\n");
			Str.append("<option value=ID>ID</option>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String SearchModule(String search, String module, String type) {
		String Str = "", url = "";
		String paramkey = "?1_dr_field=0-text&1_dr_param=0-text&1_txt_value_1=search_text&1_dr_filter=and&dr_enquirybranch=0&dr_executive=0&dr_enquirypriority=0&dr_soe=0&dr_sob=0&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1";
		String paramid = "?1_dr_field=search_dropdown&1_dr_param=0-numeric&1_txt_value_1=search_text&1_dr_filter=and&dr_enquirybranch=0&dr_executive=0&dr_enquirypriority=0&dr_soe=0&dr_sob=0&advsearch_button=Search&dr_searchcount=1&dr_searchcount_var=1";
		try {
			if (module.equals("Enquiry")) {
				url = "../sales/enquiry-list.jsp";
			}

			if (module.equals("TestDrive")) {
				url = "../sales/testdrive-list.jsp";
			}

			if (module.equals("Quote")) {
				url = "../sales/veh-quote-list.jsp";
			}

			if (module.equals("SalesOrder")) {
				url = "../sales/veh-salesorder-list.jsp";
			}
			if (module.equals("Pre-Owned")) {
				url = "../preowned/preowned-list.jsp";
			}
			if (module.equals("Ticket")) {
				url = "../service/ticket-list.jsp";
			}

			if (module.equals("Stock")) {
				url = "../inventory/stock-list.jsp";
			}

			if (module.equals("Vehicle")) {
				url = "../service/vehicle-list.jsp";
			}
			if (module.equals("JobCard")) {
				url = "../service/jobcard-list.jsp";
			}
			if (module.equals("Insurance")) {
				url = "../insurance/insurance-list.jsp";
			}
			if (module.equals("Item")) {
				url = "../inventory/inventory-item-list.jsp";
			}
			if (module.equals("Voucher")) {
				url = "../accounting/voucher-list.jsp";
			}
			if (module.equals("Executives")) {
				url = "../portal/executive-list.jsp";
			}

			if (type.equals("ID")) {
				Str = url + paramid;
			} else if (type.equals("Keyword")) {
				Str = url + paramkey;
			}
			if (!module.equals("Voucher")) {
				if (module.equals("SalesOrder") || module.equals("Enquiry") || module.equals("TestDrive")) {
					Str = Str.replace("search_text", search).replace("search_dropdown", "0-numeric");
				} else {
					Str = Str.replace("search_text", search).replace("search_dropdown", "1-numeric");
				}

			} else {
				Str = Str.replace("search_text", search).replace("search_dropdown", "2-numeric");
			}
			return unescapehtml(Str);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
