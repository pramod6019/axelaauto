package axela.axelaauto_app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.ConnectWS;

public class App_TestDrive_List extends ConnectWS {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String StrSearch = "";
	public int pagecurrent = 1;
	public String emp_uuid = "0";
	public String emp_id = "0";
	public String pagecount = "0";
	public String so_id = "0";
	public String branch_id = "0";
	public String enquiry_id = "0", testdrive_id = "0";
	public String populate = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String StrHTML = "";
	public int totalcount = 0;
	public String startdate = "";
	public String msg = "";
	public String comp_id = "0", emp_all_exe = "", access = "", startdate1 = "";
	public String testdrive_date = "", strtime = "";
	public String testdrive_enquiry_id = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try
		{
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			// testdrive_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
			msg = PadQuotes(request.getParameter("msg"));
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "testdrive_emp_id");
				// emp_all_exe = GetSession("emp_all_exe", request);
				pagecount = PadQuotes(request.getParameter("pagenumber"));
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				if (pagecount.equals("")) {
					pagecurrent = 1;
				} else {
					pagecurrent = Integer.parseInt(pagecount);
				}
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				if (!enquiry_id.equals("0")) {
					StrSearch = " AND testdrive_enquiry_id =" + enquiry_id + "";
				}
				if (!testdrive_id.equals("0")) {
					StrSearch = " AND testdrive_id =" + testdrive_id + "";
				}
				testdrive_id = CNumeric(PadQuotes(request.getParameter("testdrive_id")));
				startdate1 = PadQuotes(request.getParameter("txt_testdrive_date"));
				if (startdate1.equals(""))
				{
					startdate1 = DateToShortDate(kknow());
					startdate = ConvertShortDateToStr(startdate1);
					// startdate1 = strToShortDate(startdate);
					// startdate1 = FormatDateMobileStr(startdate1);
					
				}
				else {
					if (!startdate1.equals("")) {
						// startdate = FormatDateStr(startdate1);
						startdate = ConvertShortDateToStr(startdate1);
					}
					
				}
				
				if (!testdrive_id.equals("0")) {
					StrSearch = StrSearch + " AND testdrive_id =" + testdrive_id + "";
					strtime = "";
					
				}
				else
				{
					strtime = " AND SUBSTRING(testdrive_time,1,8) =SUBSTRING(" + startdate + ",1,8)";
				}
				StrSearch += BranchAccess
						+ ExeAccess;
				if (!populate.equals("yes")) {
					StrHTML = Listdata(response);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		
	}
	public String Listdata(HttpServletResponse response) {
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT testdrive_id, testdriveveh_id, testdriveveh_name, branch_code, customer_name, contact_id,"
					+ " CONCAT(contact_fname, ' ', contact_lname) AS contactname, contact_mobile1,"
					+ " contact_mobile2, contact_email1, contact_email2, COALESCE(model_name, '') AS model_name,"
					+ " testdrive_time_to, testdrive_time_from, testdrive_type, testdrive_confirmed,"
					+ " COALESCE(testdrive_notes, '') AS testdrive_notes, testdrive_out_kms,"
					+ " testdrive_doc_value, customer_id, enquiry_id, branch_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
					+ " emp_id, concat(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
					+ " testdrive_time, location_name, testdrive_in_time, testdrive_in_kms,"
					+ " testdrive_fb_taken, COALESCE(testdrive_fb_status_id, 0) AS testdrive_fb_status_id,"
					+ " coalesce(status_name, '') AS status_name, testdrive_out_time,"
					+ " testdrive_fb_status_comments, testdrive_fb_budget, testdrive_fb_finance,"
					+ " testdrive_fb_finance_amount, testdrive_fb_finance_comments,"
					+ " testdrive_fb_insurance, testdrive_fb_insurance_comments";
			
			CountSql = "SELECT COUNT(DISTINCT testdrive_id) ";
			
			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_testdrive"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_location on location_id= testdrive_location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry on enquiry_id = testdrive_enquiry_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_testdrive_vehicle on testdriveveh_id = testdrive_veh_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = veh_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = testdrive_emp_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_testdrive_status on status_id= testdrive_fb_status_id"
					+ " WHERE 1 = 1";
			// if (enquiry_id.equals("0")) {
			SqlJoin += strtime;
			// }
			// + " AND SUBSTRING(testdrive_time,1,8) =SUBSTRING(" + startdate +
			// ",1,8)";
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			
			StrSql += StrSearch + " GROUP BY testdrive_id"
					+ " ORDER BY testdrive_id DESC"
					+ LimitRecords(TotalRecords, Integer.toString(pagecurrent));
			CountSql += StrSearch;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			totalcount = TotalRecords / 25;
			CachedRowSet crs = processQuery(StrSql, 0);
			
			if (crs.isBeforeFirst()) {
				if (pagecurrent == 1)
				{
					Str.append("<div class=\"panel-heading\" style=\"background-color: #8E44AD; border: 1px solid transparent; border-radius: 0px;\">");
					Str.append("<span class=\"panel-title\">");
					Str.append("<center><strong>List Test Drives</strong></center>");
					Str.append("</span> </div>");
				}
				if (TotalRecords >= 0) {
					count++;
					while (crs.next()) {
						SOP("======" + unescapehtml((crs.getString("model_name"))));
						
						Str.append("<div class=\"container\">\n")
								.append("<div class=\"col-md-12 col-xs-12\">\n")
								.append("<div class=\"row\" style=\"background-color: #fff\">\n")
								.append("<br>\n")
								.append("<div class=\"col-md-8 col-xs-8\">\n")
								.append("<b>Test Drive ID: ")
								.append(crs.getString("testdrive_id"))
								.append("</b><br>\n")
								.append("Enquiry ID: ")
								.append(crs.getString("enquiry_id"))
								.append("<br>\n")
								.append("Model: ")
								.append(unescapehtml((crs.getString("model_name"))))
								.append("<br>\n")
								.append("Test Drive Time: ")
								.append(strToShortDate(crs.getString("testdrive_time")))
								.append("<br>\n")
								.append("Location: ")
								.append(crs.getString("location_name"))
								.append("<br>\n")
								.append("</div>")
								
								.append("<div class=\"col-md-4 col-xs-4\">")
								.append("<div class=\"row\">")
								.append("<img src=\"ifx/icon-arrow.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal1" + crs.getString("testdrive_id")
										+ "\"  style=\"float:right\">\n")
								.append("<div class=\"modal fade\" id=\"myModal1" + crs.getString("testdrive_id") + "\"  role=\"dialog\">")
								.append("<div class=\"modal-dialog\">")
								
								.append("<div class=\"modal-content\">\n")
								.append("<div class=\"modal-header\">\n")
								.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">\n").append("&times;").append("</button>\n")
								.append("<center>")
								.append("<h4 class=\"modal-title\">")
								.append("<b style=\"color:white\">").append("Action").append("</b>")
								.append("</h4>")
								.append("</center>")
								.append("</div>")
								
								.append("<div class=\"modal-body\" onclick=\"callURL('app-testdrive-feedback.jsp?testdrive_id=" + crs.getString("testdrive_id") + "')\">\n")
								.append("<span>\n<b style=\"color:black\">Update Feedback").append("</b></span>\n").append("</div>\n")
								// .append("<div class=\"modal-body\" onclick=\"callURL('app-testdrive-feedback.jsp?testdrive_id="
								// + crs.getString("testdrive_id") +
								// "') id=\"myModal1\">\n")
								// .append("<p>")
								// .append("<b style=\"color:black\">")
								// .append("Add Feedback")
								// .append("</b>\n")
								// .append("</p>\n")
								// .append("</div>\n")
								
								.append("</div>\n")
								.append("</div>\n")
								.append("</div>\n")
								.append("</div>\n")
								.append("</div>\n")
								.append("</div>\n")
								.append("</div>\n")
								
								.append("</div>\n");
						
						// Str.append("<div class=\"container\">\n")
						// .append("<div class=\"col-md-12 col-xs-12\" style=\"background-color: #fff\">")
						// .append("<div class=\"row\" style=\"background-color: #fff\"><br><div class=\"col-md-8 col-xs-8\">\n<br>\n")
						// .append("<b>Test Drive ID: ").append(crs.getString("testdrive_id")).append("</b><br>\n")
						// .append("Enquiry ID: ").append(crs.getString("enquiry_id")).append("<br>\n")
						// .append("Model: ").append(crs.getString("model_name")).append("<br>\n")
						// .append("Test Drive Time: ").append(strToShortDate(crs.getString("testdrive_time"))).append("<br>\n")
						// .append("Location: ").append(crs.getString("location_name")).append("<br>\n")
						// .append("</div>\n")
						// .append("<div class=\"col-md-4 col-xs-4\">\n<br>\n");
						// Str.append(
						// "<div class=\"row\">\n<img src=\"../axelaauto-app/ifx/icon-arrow.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal2"
						// + crs.getString("enquiry_id") +
						// "\" style=\"float: right\">\n")
						// .append("<div class=\"modal fade\" id=\"myModal2" +
						// crs.getString("enquiry_id") +
						// "\" role=\"dialog\">\n").append("<div class=\"modal-dialog\">\n")
						//
						// .append("<div class=\"modal-content\">\n").append("<div class=\"modal-header\">\n")
						// .append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n")
						// .append("<center><h4 class=\"modal-title\"><b style=\"color:white\">Actions</b></h4></center>\n").append("</div>\n")
						//
						// .append("<div class=\"modal-body\" onclick=\"callURL('app-testdrive-add.jsp?add=yes&enquiry_id="
						// + crs.getString("enquiry_id") + "')\">\n")
						// .append("<span>\n<b style=\"color:black\">Add Test Drive").append("</b></span>\n").append("</div>\n")
						//
						// .append("</div></div></div></div><br>\n");
						
					}
				}
			} else {
				Str.append("<div class=\"container\" align=\"center\"><br><b>\n").append("No Records Found!").append("</b></div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
