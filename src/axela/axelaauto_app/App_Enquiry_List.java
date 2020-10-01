package axela.axelaauto_app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;

import cloudify.connect.Connect;

public class App_Enquiry_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public int totalcount = 0;
	public int recperpage = 0;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String enqstatus = "";
	public String StrSearch = "", Subquery = "";
	public String pagecount = "0";
	public int pagecurrent = 1;

	public String emp_uuid = "0";
	public String emp_id = "0";
	public String so_id = "0";
	public String branch_id = "";
	public String role_id = "";
	public String keyword = "", search = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile = "";
	public String contact_phone = "";
	public String contact_email = "";
	public String model_id = "0";
	public String enquiry_id = "0";
	public String item_id = "0";
	public String status_id = "0";
	public String priority_id = "0";
	public String populate = "";
	public String searchkeyname = "";

	public String searchvalue = "";
	public String StrHTML = "", msg = "";
	public String enquirystatus = "";
	public String comp_id = "0";
	public String contact_id = "0";
	public String emp_all_exe = "", access = "";
	public String filter = "";
	// public String[] model_ids, status_ids, priority_ids, stage_ids, emp_ids,
	// soe_ids;
	// // public CachedRowSet crs =null;
	JSONArray arr_keywords;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);

		comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		if (!emp_id.equals("0")) {
			try {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "enquiry_branch_id");
				ExeAccess = ExeAccess.replace("emp_id", "enquiry_emp_id");
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				emp_all_exe = GetSession("emp_all_exe", request);
				access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				enquirystatus = PadQuotes(request.getParameter("enquirystatus"));
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				if (enquirystatus.equals("")) {
					enquirystatus = "empty";
				}

				populate = PadQuotes(request.getParameter("populate"));
				msg = PadQuotes(request.getParameter("msg"));
				filter = PadQuotes(request.getParameter("filter"));
				pagecount = PadQuotes(request.getParameter("pagenumber"));
				if (pagecount.equals("")) {
					pagecurrent = 1;
				} else {
					pagecurrent = Integer.parseInt(pagecount);
				}

			}

			catch (Exception ex) {
				SOPError("Axelaauto-App===" + this.getClass().getName());
				SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

		if (!contact_id.equals("0")) {

			StrSearch = StrSearch + " AND contact_id = " + contact_id;
		}

		else if (enquirystatus.equals("monthenquries")) {

			StrSearch += " AND SUBSTR(enquiry_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
		}

		else if (enquirystatus.equals("todayenquires")) {

			StrSearch = " AND SUBSTR(enquiry_entry_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)";
		}
		else if (enquirystatus.equals("totalhotenquires")) {
			StrSearch = " AND enquiry_priorityenquiry_id = 1 AND enquiry_status_id = 1";
		}

		else if (enquirystatus.equals("totalenquires")) {
			StrSearch = " AND enquiry_status_id = 1";
		}

		else if (enquirystatus.equals("level1")) {
			StrSearch = " AND enquiry_status_id = 1 "
					+ " AND enquiry_id IN ( SELECT followup_enquiry_id" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup" + " WHERE followup_trigger = 1"
					+ " AND followup_desc = ''"
					+ " ORDER BY followup_id ) ";
		}
		else if (enquirystatus.equals("level2")) {
			StrSearch = " AND enquiry_status_id = 1 "
					+ " AND enquiry_id IN ( SELECT followup_enquiry_id" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup" + " WHERE followup_trigger = 2"
					+ " AND followup_desc = ''"
					+ " ORDER BY followup_id ) ";
		}
		else if (enquirystatus.equals("level3")) {
			StrSearch = " AND enquiry_status_id = 1 "
					+ " AND enquiry_id IN ( SELECT followup_enquiry_id" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup" + " WHERE followup_trigger = 3"
					+ " AND followup_desc = ''"
					+ " ORDER BY followup_id ) ";
		}
		else if (enquirystatus.equals("level4")) {
			StrSearch = " AND enquiry_status_id = 1 "
					+ " AND enquiry_id IN ( SELECT followup_enquiry_id" + " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup" + " WHERE followup_trigger = 4"
					+ " AND followup_desc = ''"
					+ " ORDER BY followup_id ) ";
		}
		else if (enquirystatus.equals("level5")) {
			StrSearch = " AND enquiry_status_id = 1 "
					+ " AND enquiry_id IN ( SELECT followup_enquiry_id " + " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup" + " WHERE followup_trigger = 5"
					+ " AND followup_desc = ''"
					+ " ORDER BY followup_id ) ";
		}
		else if (!enquiry_id.equals("0")) {
			StrSearch = " AND enquiry_id =" + enquiry_id;
		}
		else if (filter.equals("yes")) {
			StrSearch = ProcessFilter(request);
		}
		else if (enquirystatus.equals("empty")) {
			StrSearch = " AND enquiry_status_id = 1";
		}

		StrSearch += BranchAccess;
		if (emp_all_exe.equals("0")) {
			StrSearch += ExeAccess;
		}

		if (!populate.equals("yes")) {
			StrHTML = Listdata(comp_id);
		}

	}
	public String Listdata(String comp_id) {
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT customer_name, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name, branch_brand_id,"
					+ " enquiry_id, enquiry_status_id, enquiry_date, contact_mobile1, contact_mobile2, contact_email1, contact_email2," + " contact_phone1, contact_phone2, enquiry_model_id,"
					+ " CONCAT(exeemp.emp_name, ' (', exeemp.emp_ref_no, ')') AS emp_name," + " item_name, stage_name, status_name, enquiry_priorityenquiry_id, enquiry_enquirytype_id";

			CountSql = "SELECT COUNT(DISTINCT enquiry_id)";
			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON  contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON  title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp exeemp ON exeemp.emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id";

			SqlJoin += Subquery + " , " + compdb(comp_id) + "axela_emp emp "
					+ " WHERE 1 = 1 "
					+ " AND emp.emp_id = " + emp_id + StrSearch;
			StrSql = StrSql + SqlJoin;
			StrSql = StrSql + " GROUP BY enquiry_id"
					+ " ORDER BY enquiry_id DESC "
					+ LimitRecords(TotalRecords, Integer.toString(pagecurrent));

			CountSql += SqlJoin;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			totalcount = TotalRecords / 25;

			CachedRowSet crs =processQuery(StrSql, 0);
			// SOP("TotalRecords--------" + TotalRecords);
			if (crs.isBeforeFirst()) {
				if (TotalRecords >= 0) {
					count++;
					while (crs.next()) {
						Str.append("<div class=\"container\">\n")
								.append("<div class=\"col-md-12\">\n")
								.append("<div class=\"row\" style=\"background-color: #fff\"><br><div class=\"col-md-8 col-xs-8\" " + "onclick=\"callURL('app-enquiry-dash.jsp?enquiry_id="
										+ crs.getString("enquiry_id") + "')\">\n<b>\n").append(crs.getString("contact_name")).append("</b><br>\n").append(unescapehtml(crs.getString("item_name")))
								.append("<br>\n").append(crs.getString("emp_name")).append("<br>\n").append(strToShortDate(crs.getString("enquiry_date"))).append("<br>\n").append("Stage: ")
								.append(crs.getString("stage_name")).append("<br>\n").append("Status: ").append(crs.getString("status_name")).append("</div>");
						Str.append("<div class=\"col-md-4 col-xs-4\">\n").append("<div class=\"row\">\n").append("<b style=\"float: right\">\n")
								.append("&nbsp;&nbsp;&nbsp;&nbsp;" + crs.getString("enquiry_id")).append("</b></div>\n");

						// //////////for mobile data/////////////
						Str.append(
								"<div class=\"row\">\n<img src=\"ifx/icon-call.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal1" + crs.getString("enquiry_id")
										+ "\" style=\"float: right\">\n").append("<div class=\"modal fade\" id=\"myModal1" + crs.getString("enquiry_id") + "\"   role=\"dialog\">\n")
								.append("<div class=\"modal-dialog\">\n").append("<div class=\"modal-content\">\n").append("<div class=\"modal-header\">\n")
								.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n")
								.append("<center><h4 class=\"modal-title\"><b style=\"color:white\">Contact</b></h4></center>").append("</div>\n");
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<div class=\"modal-body\" id=\"myModal1\" onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\">\n").append("<p><b style=\"color:black\">Call ")
									.append(crs.getString("contact_mobile1")).append("</b></p>").append("</div>\n");
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<div class=\"modal-body\" id=\"myModal1\" onclick=\"callNo('" + crs.getString("contact_mobile2") + "')\">\n").append("<span><b style=\"color:black\">Call ")
									.append(crs.getString("contact_mobile2")).append("</b></span>\n").append("</div>\n");
						}
						if (!crs.getString("contact_phone1").equals("")) {
							Str.append("<div class=\"modal-body\" id=\"myModal1\" onclick=\"callNo('" + crs.getString("contact_phone1") + "')\">\n").append("<span><b style=\"color:black\">Call ")
									.append(crs.getString("contact_phone1")).append("</b></span>").append("</div>\n");
						}
						Str.append("</div>\n").append("</div>\n").append("</div>\n").append("</div><br>\n");

						// //////// for email data/////////////////
						if (!crs.getString("contact_email2").equals("") || !crs.getString("contact_email1").equals("")) {
							Str.append(
									"<div class=\"row\">\n<img src=\"ifx/icon-at.png\" class=\"img-responsive\" data-toggle=\"modal\"  data-target=\"#myModal" + crs.getString("enquiry_id")
											+ "\"  style=\"float: right\">\n").append("<div class=\"modal fade \" id=\"myModal" + crs.getString("enquiry_id") + "\"  role=\"dialog\">\n")
									.append("<div class=\"modal-dialog\">\n").append("<div class=\"modal-content\">\n").append("<div class=\"modal-header\">\n")
									.append("<button type=\"button\" class=\"close\" >&times;</button>\n")
									.append("<center>\n<h4 class=\"modal-title\">\n<b style=\"color:white\">Mail</b>\n</h4>\n</center>").append("</div>\n");
							if (!crs.getString("contact_email1").equals("")) {
								Str.append("<div class=\"modal-body\"  onclick=\"sendMail('" + crs.getString("contact_email1") + "')\">\n").append("<p><b style=\"color:black\">\n")
										.append(crs.getString("contact_email1")).append("</b></p>\n").append("</div>\n");
							}
							if (!crs.getString("contact_email2").equals("")) {
								Str.append("<div class=\"modal-body\" onclick=\"sendMail('" + crs.getString("contact_email2") + "')\">\n").append("<p><b style=\"color:black\">\n")
										.append(crs.getString("contact_email2")).append("</b>\n</p>\n").append("</div>\n");
							}
							Str.append("</div>\n").append("</div>\n").append("</div>\n").append("</div><br>\n");

						}

						// //////////////////////for Add demo/////////////
						Str.append(
								"<div class=\"row\">\n<img src=\"../axelaauto-app/ifx/icon-arrow.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal2"
										+ crs.getString("enquiry_id") + "\" style=\"float: right\">\n")
								.append("<div class=\"modal fade\" id=\"myModal2" + crs.getString("enquiry_id") + "\" role=\"dialog\">\n").append("<div class=\"modal-dialog\">\n")

								.append("<div class=\"modal-content\">\n").append("<div class=\"modal-header\">\n")
								.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n")
								.append("<center><h4 class=\"modal-title\"><b style=\"color:white\">Actions</b></h4></center>\n").append("</div>\n")

								.append("<div class=\"modal-body\" onclick=\"callURL('app-testdrive-add.jsp?add=yes&enquiry_id=" + crs.getString("enquiry_id") + "')\">\n")
								.append("<span>\n<b style=\"color:black\">Add Test Drive").append("</b></span>\n").append("</div>\n");

						if (crs.getString("branch_brand_id").equals("1") || crs.getString("branch_brand_id").equals("2")) {
							Str.append("<div class=\"modal-body\">")
									.append("<a href=\"printtrackingcardapp-enquiry-trackingcard.jsp?enquiry_id=" + crs.getString("enquiry_id") + "\" style=\"text-decoration: none\">\n")
									.append("<span>\n<b style=\"color:black\">Print Tracking Card").append("</b></span>\n").append("</a></div>\n");
						}
						// else if
						// (crs.getString("branch_brand_id").equals("6"))
						// {
						// Str.append("<div class=\"modal-body\" onclick=\"callURL('enquiry_trackingcard2.jsp?enquiry_id="
						// + crs.getString("enquiry_id") + "')\">\n");
						// Str.append("<span>\n<b style=\"color:black\">Print Tracking Card").append("</b></span>\n").append("</div>\n");
						// }

						Str.append("<div class=\"modal-body\" onclick=\"callURL('app-veh-quote-add.jsp?enquiry_id=" + crs.getString("enquiry_id") + "')\">\n")
								.append("<span>\n<b style=\"color:black\">Add Quote").append("</b>\n</span>\n").append("</div>\n")

								.append("<div class=\"modal-body\" onclick=\"callURL('app-veh-quote-list.jsp?enquiry_id=" + crs.getString("enquiry_id") + "')\">\n")
								.append("<span>\n<b style=\"color:black\">List Quotes").append("</b></span>\n").append("</div>\n")

								.append("<div class=\"modal-body\" onclick=\"callURL('app-enquiry-dash.jsp?enquiry_id=" + crs.getString("enquiry_id") + "')\">\n")
								.append("<span>\n<b style=\"color:black\">View Enquiry").append("</b></span>\n").append("</div>\n")

								.append("<div class=\"modal-body\" onclick=\"callURL('app-enquiry-dash.jsp?enquiry_id=" + crs.getString("enquiry_id") + "&followuptab=active')\">\n")
								.append("<span>\n<b style=\"color:black\">Add Follow-up").append("</b></span>\n").append("</div>\n");

						if (!crs.getString("contact_email2").equals("") || !crs.getString("contact_email1").equals("")) {
							Str.append("<div class=\"modal-body\" onclick=\"callURL('app-enquiry-brochure-email.jsp?enquiry_id=" + crs.getString("enquiry_id") + "')\">\n")
									.append("<span>\n<b style=\"color:black\">Email Brochure").append("</b>\n</span>\n").append("</div>\n");
						}
						Str.append("</div>\n").append("</div>\n").append("</div>\n").append("</div><br>\n")

								// ////////////////for share image
								// .append("<div class=\"row\">\n<img src=\"ifx/camera.png\" class=\"img-responsive\" style=\"float: right\" "
								// +
								// "onclick=\"callURL('share-images.jsp?model_id="
								// + crs.getString("enquiry_model_id")
								// + "&enquiry_id="
								// + crs.getString("enquiry_id")
								// + "')\"></div>\n")
								.append("</div>\n</div>\n</div>\n</div>\n");
					}

					// // if (jscrollcount == (TotalRecords - 1)) {
					// Str.append("</div>");
					// }

				}
				// SOP("count------" + count);
			} else {
				Str.append("<div class=\"container\" align=\"center\"><b><h4>&nbsp;</h4>\n").append("No Records Found!").append("</b></div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	private String ProcessFilter(HttpServletRequest request) {
		StringBuilder str = new StringBuilder();
		String strsearch = "";
		String model_ids = "";
		String brand_ids = "";
		String region_ids = "";
		String branch_ids = "";
		String status_ids = "";
		String stage_ids = "";
		String priority_ids = "";
		String executive_ids = "";
		String soe_ids = "";
		String[] model_id = CheckNull(request.getParameterValues("chk_model_id"));
		String[] brand_id = CheckNull(request.getParameterValues("chk_brand_id"));
		String[] region_id = CheckNull(request.getParameterValues("chk_region_id"));
		String[] branch_id = CheckNull(request.getParameterValues("chk_branch_id"));
		String[] status_id = CheckNull(request.getParameterValues("chk_status_id"));
		String[] stage_id = CheckNull(request.getParameterValues("chk_stage_id"));
		String[] priority_id = CheckNull(request.getParameterValues("chk_priority_id"));
		String[] executive_id = CheckNull(request.getParameterValues("chk_executive_id"));
		String[] soe_id = CheckNull(request.getParameterValues("chk_soe_id"));

		if (model_id.length != 0)
		{
			for (int i = 0; i < model_id.length; i++)
			{
				model_ids = str.append(model_id[i] + ",").toString();
			}
			model_ids = CleanArrVal(model_ids);
			strsearch = " AND enquiry_model_id IN(" + model_ids + ")";
		}

		if (brand_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < brand_id.length; i++)
			{
				brand_ids = str.append(brand_id[i] + ",").toString();
			}
			brand_ids = CleanArrVal(brand_ids);
			strsearch += " AND branch_brand_id IN(" + brand_ids + ")";
		}

		if (region_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < region_id.length; i++)
			{
				region_ids = str.append(region_id[i] + ",").toString();
			}
			region_ids = CleanArrVal(region_ids);
			strsearch += " AND branch_region_id IN(" + region_ids + ")";
		}

		if (branch_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < branch_id.length; i++)
			{
				branch_ids = str.append(branch_id[i] + ",").toString();

			}
			branch_ids = CleanArrVal(branch_ids);
			strsearch += " AND enquiry_branch_id IN(" + branch_ids + ")";
		}

		if (status_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < status_id.length; i++)
			{
				status_ids = str.append(status_id[i] + ",").toString();
			}
			status_ids = CleanArrVal(status_ids);
			strsearch += " AND enquiry_status_id IN(" + status_ids + ")";
		}

		if (stage_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < stage_id.length; i++)
			{
				stage_ids = str.append(stage_id[i] + ",").toString();
			}
			stage_ids = CleanArrVal(stage_ids);
			strsearch += " AND enquiry_stage_id IN(" + stage_ids + ")";
		}

		if (priority_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < priority_id.length; i++)
			{
				priority_ids = str.append(priority_id[i] + ",").toString();

			}
			priority_ids = CleanArrVal(priority_ids);
			strsearch += " AND enquiry_priorityenquiry_id IN(" + priority_ids + ")";
		}

		if (executive_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < executive_id.length; i++)
			{
				executive_ids = str.append(executive_id[i] + ",").toString();
			}
			executive_ids = CleanArrVal(executive_ids);
			strsearch += " AND enquiry_emp_id IN(" + executive_ids + ")";
		}

		if (soe_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < soe_id.length; i++)
			{
				soe_ids = str.append(soe_id[i] + ",").toString();
			}
			soe_ids = CleanArrVal(soe_ids);
			strsearch += " AND enquiry_soe_id IN(" + soe_ids + ")";
		}

		String filterenquiry_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_id")));
		// String filterenquiry_no =
		// CNumeric(PadQuotes(request.getParameter("txt_enquiry_no")));
		String filterenquiry_dms_no = PadQuotes(request.getParameter("txt_dms_no"));
		String filterenquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_id")));
		// String filterenquiry_branch_name =
		// PadQuotes(request.getParameter("txt_branch_name"));
		String filterenquiry_customer_id = CNumeric(PadQuotes(request.getParameter("txt_customer_id")));
		String filterenquiry_contact_id = CNumeric(PadQuotes(request.getParameter("txt_contact_id")));
		String filterenquiry_customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		String filterenquiry_contact_name = PadQuotes(request.getParameter("txt_contact_name"));
		String filterenquiry_contact_mobile = PadQuotes(request.getParameter("txt_contact_mobile"));
		String filterenquiry_contact_email = PadQuotes(request.getParameter("txt_contact_email"));
		if (!filterenquiry_id.equals("0"))
		{
			strsearch += " AND enquiry_id=" + filterenquiry_id;
		}

		// if (!filterenquiry_no.equals("0"))
		// {
		// strsearch += " AND enquiry_no=" + filterenquiry_no;
		// }

		if (!filterenquiry_dms_no.equals(""))
		{
			strsearch += " AND enquiry_dmsno='" + filterenquiry_dms_no + "'";
		}

		if (!filterenquiry_branch_id.equals("0"))
		{
			strsearch += " AND enquiry_branch_id=" + filterenquiry_branch_id;
		}

		// if (!filterenquiry_branch_name.equals(""))
		// {
		// strsearch += " AND branch_name='" + filterenquiry_branch_name + "'";
		// }

		if (!filterenquiry_customer_id.equals("0"))
		{
			strsearch += " AND enquiry_customer_id=" + filterenquiry_customer_id;
		}

		if (!filterenquiry_contact_id.equals("0"))
		{
			strsearch += " AND enquiry_contact_id=" + filterenquiry_contact_id;
		}

		if (!filterenquiry_customer_name.equals(""))
		{
			strsearch += " AND customer_name='" + filterenquiry_customer_name + "'";
		}

		if (!filterenquiry_contact_name.equals(""))
		{
			strsearch += " AND contact_name='" + filterenquiry_contact_name + "'";
		}

		if (!filterenquiry_contact_mobile.equals(""))
		{
			strsearch += " AND (contact_mobile1='" + filterenquiry_contact_mobile + "'"
					+ " OR contact_mobile2='" + filterenquiry_contact_mobile + "'" + ")";
		}

		if (!filterenquiry_contact_email.equals(""))
		{
			strsearch += " AND (contact_email1='" + filterenquiry_contact_email + "'"
					+ " OR contact_email1='" + filterenquiry_contact_email + "'" + ")";
		}

		return strsearch;

	}

	public String[] CheckNull(String[] values)
	{
		if (values != null)
		{
			return values;
		}
		else
		{
			return new String[0];
		}
	}

}
