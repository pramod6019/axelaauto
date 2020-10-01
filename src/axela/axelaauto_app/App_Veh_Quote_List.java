package axela.axelaauto_app;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Veh_Quote_List extends Connect {
	public int i = 0;
	public static int total = 0;
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public int TotalRecords = 0;
	public int pagecurrent = 0;
	public String emp_uuid = "0";
	public String emp_id = "0";
	public String quote_id = "0";
	public String enquiry_id = "0";
	public String config_sales_enquiry_refno = "";
	public String populate = "";
	public String StrSearch = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String QueryString = "";
	public int totalcount = 0;
	public String pagecount = "0";
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "", emp_all_exe = "";
	public String ExeAccess = "";
	public String quote_enquiry_id = "0", android = "";
	public String quote_customer_id = "0";
	public String quote_no = "";
	public String mobile1 = "";
	// // mail variablesss
	public String comp_email_enable = "0";
	public String branch_quote_email_enable = "0";
	public String branch_quote_email_format = "";
	public String branch_quote_email_sub = "";
	public String branch_quote_email_exe_sub = "";
	public String branch_quote_email_exe_format = "";
	public String quote_emp_email = "";
	public String config_email_enable = "0";
	public String contact_email1 = "";
	public String comp_id = "0";
	public String access = "0";
	public String filter = "", quotestatus = "";
	DecimalFormat df = new DecimalFormat("0.00");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "quote_branch_id");
				ExeAccess = ExeAccess.replace("emp_id", "quote_emp_id");
				emp_all_exe = GetSession("emp_all_exe", request);
				access = ReturnPerm(comp_id, "emp_sales_quote_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				quotestatus = PadQuotes(request.getParameter("quotestatus"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				filter = PadQuotes(request.getParameter("filter"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				populate = PadQuotes(request.getParameter("populate"));
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				quote_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				quote_customer_id = CNumeric(PadQuotes(request.getParameter("quote_customer_id")));
				all = PadQuotes(request.getParameter("all"));
				android = PadQuotes(request.getParameter("android"));
				pagecount = PadQuotes(request.getParameter("pagenumber"));
				PopulateConfigDetails();
				if (pagecount.equals("")) {
					pagecurrent = 1;
				} else {
					pagecurrent = Integer.parseInt(pagecount);
				}

				if (!quote_id.equals("0")) {
					StrSearch += " AND quote_id = " + quote_id;
				}

				if (!quote_enquiry_id.equals("0")) {
					StrSearch += " AND quote_enquiry_id = " + quote_enquiry_id;
				}
				else if (filter.equals("yes")) {
					StrSearch = ProcessFilter(request);
				}
				StrSearch += BranchAccess;
				if (emp_all_exe.equals("0"))
				{
					StrSearch += ExeAccess;
				}

				if (!populate.equals("yes")) {
					StrHTML = ListData();
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListData() {
		CachedRowSet crs = null;
		int TotalRecords;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT quote_id, quote_branch_id, quote_no, so_no,"
					+ " quote_netamt, quote_totaltax, quote_grandtotal, quote_refno, quote_auth, quote_active,"
					+ " customer_id, customer_name, contact_id, COALESCE(so_id, 0) AS so_id, quote_enquiry_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
					+ " CONCAT(contact_mobile1, ' ')AS mobile1,"
					+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, quote_discamt,"
					+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, quote_date,"
					+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id";

			CountSql = "SELECT COUNT(DISTINCT(quote_id))";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_quote_id = quote_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item on quoteitem_quote_id = quote_id"
					+ " AND quoteitem_rowcount != 0"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " WHERE quote_enquiry_id != 0";

			StrSql += SqlJoin;
			CountSql += SqlJoin;
			CountSql += StrSearch;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			totalcount = TotalRecords / 25;
			StrSql += StrSearch + " GROUP BY quote_id" + " ORDER BY quote_id DESC";
			StrSql += LimitRecords(TotalRecords, Integer.toString(pagecurrent));
			SOP("StrSql--quote---" + StrSql);
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<div class=\"container\" id=\"container\">");
					Str.append("<div class=\"row col-md-12\" style=\"background-color: #fff\"><div class=\"col-md-8 col-xs-8\">\n<br><b>\n")
							.append(crs.getString("contact_name")).append("</b><br>\n")
							.append(strToShortDate(crs.getString("quote_date")))
							.append("<br>\n").append(IndFormat(crs.getString("quote_netamt")))
							.append("<br>\n");

					if (!crs.getString("quote_enquiry_id").equals("0")) {
						Str.append("Enquiry: ");
						Str.append(crs.getString("quote_enquiry_id")).append("<br>");
					}
					if (!crs.getString("so_id").equals("0")) {
						Str.append("SO:")
								.append(crs.getString("so_no"))
								.append("</a><br>");
					}
					Str.append(crs.getString("emp_name")).append("<br>\n").append("</div><br>\n");
					Str.append("<div class=\"col-md-4 col-xs-4\">\n")
							.append("<div class=\"row\">")
							.append("<b style=\"float: right\">")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;" + crs.getString("quote_id"))
							.append("</b></div>\n");

					// //////////for mobile data////////////
					Str.append("<div class=\"row\">")
							.append("<img src=\"ifx/icon-call.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal1" + crs.getString("contact_mobile1")
									+ "\" style=\"float: right\">")
							.append("<div class=\"modal fade\" id=\"myModal1" + crs.getString("contact_mobile1") + "\" role=\"dialog\">")
							.append("<div class=\"modal-dialog\">\n")
							.append("<div class=\"modal-content\">\n")
							.append("<div class=\"modal-header\">\n")
							.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n")
							.append("<center><h4 class=\"modal-title\"><b style=\"color:white\">Contact</b></h4></center>")
							.append("</div>\n");

					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<div class=\"modal-body\" id=\"myModal1\" onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\">")
								.append("<p><b style=\"color:black\">Call ")
								.append(crs.getString("contact_mobile1"))
								.append("</b></p>\n")
								.append("</div>\n");
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<div class=\"modal-body\" id=\"myModal1\" onclick=\"callNo('" + crs.getString("contact_mobile2") + "')\">\n")
								.append("<span><b style=\"color:black\">Call ")
								.append(crs.getString("contact_mobile2")).append("</b></span>")
								.append("</div>\n");
					}
					Str.append("</div>\n").append("</div>\n").append("</div>\n").append("</div><br>\n");

					// ////////// for email data/////////////////
					if (!crs.getString("contact_email2").equals("") || !crs.getString("contact_email1").equals("")) {
						Str.append(
								"<div class=\"row\"><img src=\"ifx/icon-at.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal2" + crs.getString("quote_id")
										+ "\" style=\"float: right\">\n")
								.append("<div class=\"modal fade\" id=\"myModal2" + crs.getString("quote_id") + "\"  role=\"dialog\">\n")
								.append("<div class=\"modal-dialog\">\n")
								.append("<div class=\"modal-content\">\n")
								.append("<div class=\"modal-header\">\n")
								.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n")
								.append("<center><h4 class=\"modal-title\"><b style=\"color:white\">Mail</b></h4></center>").append("</div>\n");
						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<div class=\"modal-body\"  id=\"myModal2\"  onclick=\"sendMail('" + crs.getString("contact_email1") + "')\"><br>\n")
									.append("<span><b style=\"color:black\">Mail ")
									.append(crs.getString("contact_email1"))
									.append("</b></span>")
									.append("</div>\n");
						}
						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<div class=\"modal-body\"  id=\"myModal2\"  onclick=\"sendMail('" + crs.getString("contact_email2") + "')\"><br>\n")
									.append("<span><b style=\"color:black\">Mail ")
									.append(crs.getString("contact_email2"))
									.append("</b></span>")
									.append("</div>\n");
						}
						Str.append("</div>\n")
								.append("</div>\n")
								.append("</div>\n")
								.append("</div><br>\n");
					}
					// //////////////////////for Add SO link/////////////
					Str.append(
							"<div class=\"row\"><img src=\"../axelaauto-app/ifx/icon-arrow.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal" + crs.getString("quote_id")
									+ "\" style=\"float: right\">\n")
							.append("<div class=\"modal fade\" id=\"myModal" + crs.getString("quote_id") + "\" role=\"dialog\">\n")
							.append("<div class=\"modal-dialog\">\n")
							.append("<div class=\"modal-content\">\n")
							.append("<div class=\"modal-header\">\n")
							.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>")
							.append("<center>")
							.append("<h4 class=\"modal-title\"> <b style=\"color:white\">")
							.append("Actions")
							.append("</b></h4></center>")
							.append("</div>\n")
							.append("<div class=\"modal-body\" >\n")
							.append("<div class=\"row\">\n");

					if (!crs.getString("contact_email1").equals("")) {
						Str.append("<div class=\"col-md-12\" onclick=\"mail('" + crs.getString("quote_id") + "')\">\n")
								.append("<span><b style=\"color:black\">&nbsp;&nbsp;&nbsp;Email Quote</b></span>")
								.append("</div><br>\n");
					}

					Str.append("<div class=\"col-md-12\">")
							.append("<a href=\"printpdfapp-veh-quote-print-pdf.jsp?quote_id=" + crs.getString("quote_id") + "\" style=\"text-decoration: none\">\n")
							.append("<span><b style=\"color:black\">&nbsp;&nbsp;&nbsp;Print Quote</b></span>")
							.append("</a></div><br>\n");

					// Str.append("<div class=\"col-md-12\" onclick=\"printPDF('"
					// + crs.getString("quote_id") + "')\">\n")
					// .append("<span><b style=\"color:black\">&nbsp;&nbsp;&nbsp;Print Quote</b></span>")
					// .append("</div><br>\n");

					if (crs.getString("so_id").equals("0")) {
						Str.append("<div class=\"col-md-12\" onclick=\"callURL('app-salesorder-update.jsp?add=yes&quote_id=" + crs.getString("quote_id") + "')\">\n")
								.append("<span><b style=\"color:black\">&nbsp;&nbsp;&nbsp;Add Sales Order</b></span>")
								.append("</div><br>\n");
					} else if (!crs.getString("so_id").equals("0")) {
						Str.append("<div class=\"col-md-12\" onclick=\"callURL('app-veh-salesorder-list.jsp?quote_id=" + crs.getString("quote_id") + "')\">\n")
								.append("<span><b style=\"color:black\">&nbsp;&nbsp;&nbsp;List Sales Order</b></span>").append("</div><br>\n");
					}

					Str.append("</div><br>\n")
							.append("</div>\n")
							.append("</div>\n")
							.append("</div>\n")
							.append("</div>\n")
							.append("</div>\n")
							// .append("<div class=\"row\"><img src=\"../axelaauto-app/ifx/icon-arrow.png\" class=\"img-responsive\" style=\"float: right\"></div><br>\n")
							.append("</div></div></div>");
				}
			} else {
				Str.append("<div class=\"container\" align=\"center\"><br><b>\n").append("No Records Found!").append("</b></div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob," + " config_sales_campaign, config_sales_enquiry_refno" + " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id = " + emp_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	private String ProcessFilter(HttpServletRequest request) {
		StringBuilder str = new StringBuilder();
		String strsearch = "";
		String model_ids = "";
		String brand_ids = "";
		String region_ids = "";
		String branch_ids = "";
		String executive_ids = "";
		String[] model_id = CheckNull(request.getParameterValues("chk_model_id"));
		String[] brand_id = CheckNull(request.getParameterValues("chk_brand_id"));
		String[] region_id = CheckNull(request.getParameterValues("chk_region_id"));
		String[] branch_id = CheckNull(request.getParameterValues("chk_branch_id"));
		String[] executive_id = CheckNull(request.getParameterValues("chk_executive_id"));

		if (model_id.length != 0)
		{
			for (int i = 0; i < model_id.length; i++)
			{
				model_ids = str.append(model_id[i] + ",").toString();
			}
			model_ids = CleanArrVal(model_ids);
			strsearch = " AND item_model_id IN(" + model_ids + ")";
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
			strsearch += " AND quote_branch_id IN(" + branch_ids + ")";
		}

		if (executive_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < executive_id.length; i++)
			{
				executive_ids = str.append(executive_id[i] + ",").toString();
			}
			executive_ids = CleanArrVal(executive_ids);
			strsearch += " AND quote_emp_id IN(" + executive_ids + ")";
		}

		String filterquote_id = CNumeric(PadQuotes(request.getParameter("txt_quote_id")));
		String filterquote_no = CNumeric(PadQuotes(request.getParameter("txt_quote_no")));
		String filterquote_from_date = PadQuotes(request.getParameter("txt_from_date"));
		String filterquote_to_date = PadQuotes(request.getParameter("txt_to_date"));
		if (!filterquote_from_date.equals(""))
		{
			filterquote_from_date = FormatDateStr(filterquote_from_date);
			filterquote_from_date = ConvertShortDateToStr(filterquote_from_date);
		}
		if (!filterquote_to_date.equals(""))
		{
			filterquote_to_date = FormatDateStr(filterquote_to_date);
			filterquote_to_date = ConvertShortDateToStr(filterquote_to_date);

		}
		String filterquote_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_name")));
		String filterquote_customer_id = CNumeric(PadQuotes(request.getParameter("txt_customer_id")));
		String filterquote_customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		String filterquote_contact_id = CNumeric(PadQuotes(request.getParameter("txt_contact_id")));
		String filterquote_contact_name = PadQuotes(request.getParameter("txt_contact_name"));
		String filterquote_contact_mobile = PadQuotes(request.getParameter("txt_contact_mobile"));
		String filterquote_contact_email = PadQuotes(request.getParameter("txt_contact_email"));
		String filterquote_enquiry_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_id")));
		// String filterquote_autorized_by =
		// CNumeric(PadQuotes(request.getParameter("txt_authorized_id")));
		String filterquote_autorized_from_date = PadQuotes(request.getParameter("txt_from_authorized_date"));
		String filterquote_autorized_to_date = PadQuotes(request.getParameter("txt_to_authorized_date"));

		if (!filterquote_autorized_from_date.equals(""))
		{
			filterquote_autorized_from_date = FormatDateStr(filterquote_autorized_from_date);
			filterquote_autorized_from_date = ConvertShortDateToStr(filterquote_autorized_from_date);
		}
		if (!filterquote_autorized_to_date.equals(""))
		{
			filterquote_autorized_to_date = FormatDateStr(filterquote_autorized_to_date);
			filterquote_autorized_to_date = ConvertShortDateToStr(filterquote_autorized_to_date);

		}

		String filterquote_active = PadQuotes(request.getParameter("dr_action"));

		if (!filterquote_id.equals("0"))
		{
			strsearch += " AND quote_id=" + filterquote_id;
		}

		if (!filterquote_no.equals("0"))
		{
			strsearch += " AND quote_no=" + filterquote_no;
		}

		if (!filterquote_from_date.equals(""))
		{
			strsearch += " AND SUBSTR(quote_date,1,8) >=SUBSTR('" + filterquote_from_date + "',1,8)";

		}

		if (!filterquote_to_date.equals(""))
		{
			strsearch += " AND SUBSTR(quote_date,1,8) <=SUBSTR('" + filterquote_to_date + "',1,8)";

		}

		if (!filterquote_branch_id.equals("0"))
		{
			strsearch += " AND quote_branch_id=" + filterquote_branch_id;
		}

		if (!filterquote_customer_id.equals("0"))
		{
			strsearch += " AND quote_customer_id=" + filterquote_customer_id;
		}

		if (!filterquote_customer_name.equals(""))
		{
			strsearch += " AND customer_name='" + filterquote_customer_name + "'";
		}

		if (!filterquote_contact_id.equals("0"))
		{
			strsearch += " AND quote_contact_id=" + filterquote_contact_id;
		}

		if (!filterquote_contact_name.equals(""))
		{
			strsearch += " AND contact_name='" + filterquote_contact_name + "'";
		}

		if (!filterquote_contact_mobile.equals(""))
		{
			strsearch += " AND (contact_mobile1='" + filterquote_contact_mobile + "'"
					+ " OR contact_mobile2='" + filterquote_contact_mobile + "'" + ")";
		}

		if (!filterquote_contact_email.equals(""))
		{
			strsearch += " AND (contact_email1='" + filterquote_contact_email + "'"
					+ " OR contact_email1='" + filterquote_contact_email + "'" + ")";
		}

		if (!filterquote_enquiry_id.equals("0"))
		{
			strsearch += " AND quote_enquiry_id=" + filterquote_enquiry_id;
		}

		if (!filterquote_autorized_from_date.equals(""))
		{
			strsearch += " AND SUBSTR(quote_auth_date,1,8) >=SUBSTR('" + filterquote_autorized_from_date + "',1,8)";
		}

		if (!filterquote_autorized_to_date.equals(""))
		{
			strsearch += " AND SUBSTR(quote_auth_date,1,8)<=SUBSTR('" + filterquote_autorized_to_date + "',1,8)";
		}

		if (!filterquote_active.equals("7"))
		{
			strsearch += " AND quote_active=" + filterquote_active;
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
