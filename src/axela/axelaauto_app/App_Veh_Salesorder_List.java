package axela.axelaauto_app;

import java.text.DecimalFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Veh_Salesorder_List extends Connect {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String enquiry_id = "0";
	public int TotalRecords = 0;
	public String StrSearch = "";
	public String so_id = "0";
	public String quote_id = "0";
	public String so_quote_id = "0", so_customer_id = "0";
	public String emp_id = "";
	public String emp_uuid = "0";
	public String populate = "";
	public String config_sales_enquiry_refno = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String msg = "";
	public String StrHTML = "";
	// public String all = "";
	public String QueryString = "";
	public String pagecount = "0";
	public int Pagecurrent = 0;
	public int totalcount = 0;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String comp_id = "0", emp_all_exe = "";
	public String mobile1 = "", sostatus = "", access = "";
	public String filter = "";
	public String filterso_branch_id1 = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
		emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
		CheckAppSession(emp_uuid, comp_id, request);
		emp_id = CNumeric(session.getAttribute("emp_id") + "");
		SOP("111111111");
		if (!emp_id.equals("0")) {
			try
			{
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "so_branch_id");
				ExeAccess = ExeAccess.replace("emp_id", "so_emp_id");
				emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }
				access = ReturnPerm(comp_id, "emp_sales_order_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				so_quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				so_customer_id = CNumeric(PadQuotes(request.getParameter("so_customer_id")));
				sostatus = PadQuotes(request.getParameter("enquirystatus"));
				filter = PadQuotes(request.getParameter("filter"));

				pagecount = PadQuotes(request.getParameter("pagenumber"));

				if (!so_id.equals("0")) {
					StrSearch += " AND so_id = " + so_id + "";
				}
				else if (!so_quote_id.equals("0")) {
					msg += "<br>Results for Quote ID = " + so_quote_id + "!";
					StrSearch += " AND so_quote_id = " + so_quote_id + "";
				}
				else if (sostatus.equals("monthbooking")) {
					StrSearch = " AND SUBSTR(so_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";
				}
				else if (sostatus.equals("monthretails")) {
					StrSearch = " AND SUBSTR(so_retail_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";

				}
				else if (sostatus.equals("monthcancellation")) {
					StrSearch = " AND SUBSTR(so_cancel_date,1,6) = SUBSTR(" + ToLongDate(kknow()) + ",1,6)";

				}
				else if (sostatus.equals("todaybooking")) {
					StrSearch = " AND SUBSTR(so_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)";

				}
				else if (sostatus.equals("todayretails")) {
					StrSearch = " AND SUBSTR(so_retail_date,1,8) = SUBSTR(" + ToLongDate(kknow()) + ",1,8)";

				}
				else if (sostatus.equals("totalbookings")) {
					StrSearch = " AND so_delivered_date=''"
							+ " AND so_cancel_date=''";

				}
				else if (filter.equals("yes")) {
					StrSearch = ProcessFilter(request);
				}
				// else if (sostatus.equals("filter")) {
				// filterso_branch_id1 = (String) session1.getAttribute(filterso_branch_id1);
				// SOP("filterso_branch_id1----" + filterso_branch_id1);
				// StrSearch = " AND so_branch_id=" + filterso_branch_id1;
				//
				// }

				StrSearch += BranchAccess;
				if (emp_all_exe.equals("0"))
				{
					StrSearch += ExeAccess;
				}

				if (pagecount.equals("")) {
					Pagecurrent = 1;
				} else {
					Pagecurrent = Integer.parseInt(pagecount);
				}

				if (!populate.equals("yes")) {
					StrHTML = Listdata();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto-App===" + this.getClass().getName());
				SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}

	}
	public String Listdata() {
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT so_id, so_branch_id, CONCAT(so_prefix, so_no) AS so_no,"
					+ " so_date, so_enquiry_id, so_quote_id, so_retail_date, so_delivered_date,"
					+ " so_grandtotal, so_discamt, so_refno, so_active, so_promise_date,"
					+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name," + " contact_mobile1, contact_mobile2, contact_email1 , contact_email2,"
					+ " customer_curr_bal, customer_id, so_auth, customer_name, emp_id," + " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, so_totaltax,"
					+ " COALESCE(item_name, '') AS item_name, so_preownedstock_id," + " COALESCE(stock_delstatus_id, 0) AS vehstock_delstatus_id,"
					+ " COALESCE(stock_engine_no, '') AS vehstock_engine_no,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name,"
					// + " COALESCE(stock_comm_no, '') AS vehstock_comm_no,"
					// + " COALESCE(stock_vin_no, '') AS vehstock_vin_no,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Paintwork',"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 2"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Upholstery',"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 4"
					+ " AND trans_vehstock_id = vehstock_id), '') AS 'Package'," + " COALESCE(vehstock_id, 0) AS vehstock_id";

			CountSql = "SELECT COUNT(DISTINCT(so_id))";

			SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_payment_track track ON track_so_id = so_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					// + " LEFT JOIN " + compdb(comp_id) + "axela_invoice ON invoice_so_id = so_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id" + " WHERE 1 = 1";

			StrSql += SqlJoin;
			CountSql += SqlJoin + StrSearch;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			// SOP("TotalRecords----" + TotalRecords);
			totalcount = TotalRecords / 25;
			StrSql += StrSearch + " GROUP BY so_id" + " ORDER BY so_id DESC";

			CountSql += StrSearch;

			StrSql += LimitRecords(TotalRecords, Integer.toString(Pagecurrent));

			// SOP("StrSql===SO List===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {

					Str.append("<div class=\"container\" id=\"container\">\n");
					Str.append(
							"<div class=\"row col-md-12\" style=\"background-color: #fff\"><br><div class=\"col-md-8 col-xs-8\" " + "onclick=\"callURL('app-salesorder-dash.jsp?so_id="
									+ crs.getString("so_id") + "')\">\n<b>\n").append(crs.getString("contact_name")).append("</b><br>\n").append(unescapehtml(crs.getString("item_name")))
							.append("<br>\n").append(crs.getString("so_no")).append("<br>\n").append(strToShortDate(crs.getString("so_date"))).append("<br>\n")
							.append(IndFormat(crs.getString("so_grandtotal"))).append("/-<br>\n").append(crs.getString("emp_name")).append("<br>\n").append("</div>\n");
					Str.append("<div class=\"col-md-4 col-xs-4\">\n").append("<div class=\"row\">").append("<b style=\"float: right\">").append("&nbsp;&nbsp;&nbsp;&nbsp;" + crs.getString("so_id"))
							.append("</b></div>\n");

					// //////////for mobile data////////////
					Str.append(
							"<div class=\"row\"><img src=\"ifx/icon-call.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal1" + crs.getString("contact_mobile1")
									+ "\" style=\"float: right\">\n").append("<div class=\"modal fade\" id=\"myModal1" + crs.getString("contact_mobile1") + "\"   role=\"dialog\">\n")
							.append("<div class=\"modal-dialog\">\n")

							.append("<div class=\"modal-content\">\n").append("<div class=\"modal-header\">\n")
							.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n")
							.append("<center><h4 class=\"modal-title\"><b style=\"color:white\">Contact</b></h4></center>").append("</div>\n");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<div class=\"modal-body\" id=\"myModal1\" onclick=\"callNo('" + crs.getString("contact_mobile1") + "')\">\n").append("<p><b style=\"color:black\">Call ")
								.append(crs.getString("contact_mobile1")).append("</b></p>").append("</div>\n");
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<div class=\"modal-body\" id=\"myModal1\" onclick=\"callNo('" + crs.getString("contact_mobile2") + "')\">\n").append("<span><b style=\"color:black\">Call ")
								.append(crs.getString("contact_mobile2")).append("</b></span>").append("</div>");
					}
					Str.append("</div>\n").append("</div>\n").append("</div>\n").append("</div><br>\n");

					// ////////// for email data/////////////////

					Str.append(
							"<div class=\"row\"><img src=\"ifx/icon-at.png\" class=\"img-responsive\" data-toggle=\"modal\" data-target=\"#myModal2" + crs.getString("so_id")
									+ "\" style=\"float: right\">\n").append("<div class=\"modal fade\" id=\"myModal2" + crs.getString("so_id") + "\"  role=\"dialog\">\n")
							.append("<div class=\"modal-dialog\">\n")

							.append("<div class=\"modal-content\">\n").append("<div class=\"modal-header\">\n")
							.append("<button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>\n")
							.append("<center><h4 class=\"modal-title\"><b style=\"color:white\">Mail</b></h4></center>").append("</div>\n");
					if (!crs.getString("contact_email1").equals("")) {

						Str.append("<div class=\"modal-body\"  id=\"myModal2\"  onclick=\"sendMail('" + crs.getString("contact_email1") + "')\"><br>\n").append("<span><b style=\"color:black\">Mail ")
								.append(crs.getString("contact_email1")).append("</b></span>").append("</div>\n");
					}
					if (!crs.getString("contact_email2").equals("")) {

						Str.append("<div class=\"modal-body\"  id=\"myModal2\"  onclick=\"sendMail('" + crs.getString("contact_email2") + "')\"><br>\n").append("<span><b style=\"color:black\">Mail ")
								.append(crs.getString("contact_email2")).append("</b></span>").append("</div>\n");
					}
					Str.append("</div>\n").append("</div>\n").append("</div>\n").append("</div><br>\n");

					// //////////////////////for Add SO dash link/////////////

					Str.append(
							"<div class=\"row\"><img src=\"../axelaauto-app/ifx/icon-arrow.png\" style=\"float: right\" " + "onclick=\"callURL('app-salesorder-dash.jsp?so_id="
									+ crs.getString("so_id")
									+ "')\"></div>\n").append("</div>\n").append("</div></div>");
				}
			} else {
				Str.append("<div class=\"container\" align=\"center\"><br><b>\n").append("No Records Found!").append("</b></div>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	private String ProcessFilter(HttpServletRequest request) {
		HttpSession session1 = request.getSession();
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
			strsearch = " AND model_id IN(" + model_ids + ")";
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
			strsearch += " AND so_branch_id IN(" + branch_ids + ")";
		}

		if (executive_id.length != 0)
		{
			str.setLength(0);
			for (int i = 0; i < executive_id.length; i++)
			{
				executive_ids = str.append(executive_id[i] + ",").toString();
			}
			executive_ids = CleanArrVal(executive_ids);
			strsearch += " AND so_emp_id IN(" + executive_ids + ")";
		}

		String filterso_id = CNumeric(PadQuotes(request.getParameter("txt_so_id")));
		String filterso_no = CNumeric(PadQuotes(request.getParameter("txt_so_no")));
		String filterso_from_date = PadQuotes(request.getParameter("txt_From_date"));
		String filterso_to_date = PadQuotes(request.getParameter("txt_to_date"));
		if (!filterso_from_date.equals(""))
		{
			filterso_from_date = FormatDateStr(filterso_from_date);
			filterso_from_date = ConvertShortDateToStr(filterso_from_date);
		}
		if (!filterso_to_date.equals(""))
		{
			filterso_to_date = FormatDateStr(filterso_to_date);
			filterso_to_date = ConvertShortDateToStr(filterso_to_date);

		}
		String filterso_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch_name")));
		// session1.setAttribute("filterso_branch_id1", filterso_branch_id);
		String filterso_customer_id = CNumeric(PadQuotes(request.getParameter("txt_customer_id")));
		String filterso_customer_name = PadQuotes(request.getParameter("txt_customer_name"));
		String dr_day = PadQuotes(request.getParameter("dr_DOBDay"));
		String dr_month = PadQuotes(request.getParameter("dr_DOBMonth"));
		String dr_year = PadQuotes(request.getParameter("dr_DOBYear"));
		if (dr_month.length() < 2) {
			dr_month = "0" + dr_month;
		}
		if (dr_day.length() < 2) {
			dr_day = "0" + dr_day;
		}
		String filterso_dob = dr_day + "/" + dr_month + "/" + dr_year;
		if (!filterso_dob.equals(""))
		{
			if (!isValidDateFormatShort(filterso_dob))
			{
				filterso_dob = "";
			}
		}
		String filterso_contact_id = CNumeric(PadQuotes(request.getParameter("txt_contact_id")));
		String filterso_contact_name = PadQuotes(request.getParameter("txt_contact_name"));
		String filterso_contact_mobile = PadQuotes(request.getParameter("txt_contact_mobile"));
		String filterso_contact_email = PadQuotes(request.getParameter("txt_contact_email"));
		String filterso_pan_no = PadQuotes(request.getParameter("txt_pan_no"));
		String filterso_enquiry_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_id")));
		String filterso_quote_id = CNumeric(PadQuotes(request.getParameter("txt_quote_id")));
		String filterso_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_vehstock_id")));
		String filterso_active = PadQuotes(request.getParameter("dr_action"));
		String filterso_from_payment_date = PadQuotes(request.getParameter("txt_so_from_payment_date"));
		String filterso_to_payment_date = PadQuotes(request.getParameter("txt_so_to_payment_date"));
		String filterso_from_promise_date = PadQuotes(request.getParameter("txt_so_from_promise_date"));
		String filterso_to_promise_date = PadQuotes(request.getParameter("txt_so_to_promise_date"));
		String filterso_from_delivery_date = PadQuotes(request.getParameter("txt_so_from_delivery_date"));
		String filterso_to_delivery_date = PadQuotes(request.getParameter("txt_so_to_delivery_date"));
		String filterso_from_cancel_date = PadQuotes(request.getParameter("txt_so_from_cancel_date"));
		String filterso_to_cancel_date = PadQuotes(request.getParameter("txt_so_to_cancel_date"));

		if (!filterso_from_payment_date.equals(""))
		{
			filterso_from_payment_date = FormatDateStr(filterso_from_payment_date);
			filterso_from_payment_date = ConvertShortDateToStr(filterso_from_payment_date);
		}
		if (!filterso_to_payment_date.equals(""))
		{
			filterso_to_payment_date = FormatDateStr(filterso_to_payment_date);
			filterso_to_payment_date = ConvertShortDateToStr(filterso_to_payment_date);

		}
		if (!filterso_from_promise_date.equals(""))
		{
			filterso_from_promise_date = FormatDateStr(filterso_from_promise_date);
			filterso_from_promise_date = ConvertShortDateToStr(filterso_from_promise_date);

		}
		if (!filterso_to_promise_date.equals(""))
		{
			filterso_to_promise_date = FormatDateStr(filterso_to_promise_date);
			filterso_to_promise_date = ConvertShortDateToStr(filterso_to_promise_date);

		}
		if (!filterso_from_delivery_date.equals(""))
		{
			filterso_from_delivery_date = FormatDateStr(filterso_from_delivery_date);
			filterso_from_delivery_date = ConvertShortDateToStr(filterso_from_delivery_date);

		}
		if (!filterso_to_delivery_date.equals(""))
		{
			filterso_to_delivery_date = FormatDateStr(filterso_to_delivery_date);
			filterso_to_delivery_date = ConvertShortDateToStr(filterso_to_delivery_date);

		}
		if (!filterso_from_cancel_date.equals(""))
		{
			filterso_from_cancel_date = FormatDateStr(filterso_from_cancel_date);
			filterso_from_cancel_date = ConvertShortDateToStr(filterso_from_cancel_date);

		}
		if (!filterso_to_cancel_date.equals(""))
		{
			filterso_to_cancel_date = FormatDateStr(filterso_to_cancel_date);
			filterso_to_cancel_date = ConvertShortDateToStr(filterso_to_cancel_date);

		}

		if (!filterso_id.equals("0"))
		{
			strsearch += " AND so_id=" + filterso_id;
		}

		if (!filterso_no.equals("0"))
		{
			strsearch += " AND so_no=" + filterso_no;
		}

		if (!filterso_from_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_date,1,8) >=SUBSTR('" + filterso_from_date + "',1,8)";

		}

		if (!filterso_to_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_date,1,8) <=SUBSTR('" + filterso_to_date + "',1,8)";
		}

		if (!filterso_branch_id.equals("0"))
		{
			strsearch += " AND so_branch_id=" + filterso_branch_id;
		}

		if (!filterso_customer_id.equals("0"))
		{
			strsearch += " AND so_customer_id=" + filterso_customer_id;
		}

		if (!filterso_customer_name.equals(""))
		{
			strsearch += " AND customer_name='" + filterso_customer_name + "'";
		}

		if (!filterso_dob.equals(""))
		{
			strsearch += " AND so_dob='" + filterso_dob + "'";
		}

		if (!filterso_contact_id.equals("0"))
		{
			strsearch += " AND so_contact_id=" + filterso_contact_id;
		}

		if (!filterso_contact_name.equals(""))
		{
			strsearch += " AND contact_name='" + filterso_contact_name + "'";
		}

		if (!filterso_contact_mobile.equals(""))
		{
			strsearch += " AND (contact_mobile1='" + filterso_contact_mobile + "'"
					+ " OR contact_mobile2='" + filterso_contact_mobile + "'" + ")";
		}

		if (!filterso_contact_email.equals(""))
		{
			strsearch += " AND (contact_email1='" + filterso_contact_email + "'"
					+ " OR contact_email1='" + filterso_contact_email + "'" + ")";
		}

		if (!filterso_pan_no.equals(""))
		{
			strsearch += " AND so_pan='" + filterso_pan_no + "'";
		}

		if (!filterso_enquiry_id.equals("0"))
		{
			strsearch += " AND so_enquiry_id=" + filterso_enquiry_id;
		}

		if (!filterso_quote_id.equals("0"))
		{
			strsearch += " AND so_quote_id=" + filterso_quote_id;
		}

		if (!filterso_vehstock_id.equals("0"))
		{
			strsearch += " AND so_vehstock_id=" + filterso_vehstock_id;
		}

		if (!filterso_active.equals("7"))
		{
			strsearch += " AND so_active=" + filterso_active;
		}

		if (!filterso_from_payment_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_payment_date,1,8) >=SUBSTR('" + filterso_from_payment_date + "',1,8)";
		}

		if (!filterso_to_payment_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_payment_date,1,8) <=SUBSTR('" + filterso_to_payment_date + "',1,8)";
		}

		if (!filterso_from_promise_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_promise_date,1,8) >=SUBSTR('" + filterso_from_promise_date + "',1,8)";
		}

		if (!filterso_to_promise_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_promise_date,1,8) <=SUBSTR('" + filterso_to_promise_date + "',1,8)";
		}

		if (!filterso_from_delivery_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_delivered_date,1,8) >=SUBSTR('" + filterso_from_delivery_date + "',1,8)";
		}
		if (!filterso_to_delivery_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_delivered_date,1,8) <=SUBSTR('" + filterso_to_delivery_date + "',1,8)";
		}

		if (!filterso_from_cancel_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_cancel_date,1,8) >=SUBSTR('" + filterso_from_cancel_date + "',1,8)";
		}

		if (!filterso_to_cancel_date.equals(""))
		{
			strsearch += " AND SUBSTR(so_cancel_date,1,8) >=SUBSTR('" + filterso_to_cancel_date + "',1,8)";
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
