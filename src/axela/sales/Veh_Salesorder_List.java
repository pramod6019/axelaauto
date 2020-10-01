package axela.sales;
//divya nov 29th

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Veh_Salesorder_List extends Connect {
	// ///// /List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../sales/index.jsp\">Sales</a>"
			+ " &gt; <a href=\"veh-salesorder.jsp\">Sales Orders</a>"
			+ " &gt; <a href=\"veh-salesorder-list.jsp?all=yes\">List Sales Orders</a>:";
	public String LinkExportPage = "veh-salesorder-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String group = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String so_id = "0";
	public String so_quote_id = "0", so_customer_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_all_exe = "";
	public String config_sales_enquiry_refno = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			// {"Keyword", "text", "keyword_arr"},
			{"Sales Order ID", "numeric", "so_id"},
			{"Sales Order No.", "text", "concat(so_prefix,so_no)"},
			{"Sales Order Date", "date", "so_date"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch", "text", "branch_name"},
			// {"Brand ID", "numeric", "branch_brand_id"},
			{"Brand", "text", "branch_brand_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Customer DOB", "date", "so_dob"},
			{"PAN NO.", "text", "so_pan"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Lead ID", "numeric", "so_lead_id"},
			{"Enquiry ID", "numeric", "so_enquiry_id"},
			{"Quote ID", "numeric", "so_quote_id"},
			{"Stock ID", "numeric", "vehstock_id"},
			// {"Pre-Owned Stock ID", "numeric", "preownedstock_id"},
			{"Pre-Owned Stock ID", "numeric", "so_preownedstock_id IN (SELECT preownedstock_id FROM compdb.axela_preowned_stock WHERE preownedstock_id"},
			{"Engine No.", "text", "vehstock_engine_no"},
			{"Chassis No.", "text", "vehstock_chassis_no"},
			// {"Pre-Owned Chassis No.", "text", "preownedstock_chassis_no = (SELECT preownedstock_chassis_no FROM compdb.axela_preowned_stock WHERE preownedstock_chassis_no"},
			{"Net Amount", "numeric", "so_netamt"},
			{"Discount", "numeric", "so_discamt"},
			{"Tax", "numeric", "so_totaltax"},
			{"Total", "numeric", "so_grandtotal"},
			// {"Billing Address", "text",
			// "concat(so_bill_address, so_bill_city,so_bill_pin,so_bill_state)"},
			// {"Shipping Address", "text",
			// "concat(so_ship_address,so_ship_city,so_ship_pin,so_ship_state)"},
			{"Description", "text", "so_desc"},
			{"Terms", "text", "so_terms"},
			{"Accessories Amount", "numeric", "so_mga_amount"},
			{"Purchase Order", "text", "so_po"},
			{"Ref. No.", "text", "so_refno"},
			{"Payment Date", "date", "so_payment_date"},
			{"Tentative Delivery Date", "date", "so_promise_date"},
			{"Retail Date", "date", "so_retail_date"},
			{"Delivered Date", "date", "so_delivered_date"},
			{"Cancel Date", "date", "so_cancel_date"},
			{"Delivery Status", "text", "delstatus_name"},
			{"SO Open", "boolean", "so_open"},
			// {"SO Finance", "boolean", "so_finance_id"},
			{"Executive", "text", "CONCAT(emp_name, emp_ref_no)"},
			{"Accessories Amount", "numeric", "so_mga_amount"},
			{"Extended Warrenty Amount", "numeric", "so_ew_amount"},
			{"Insurance Amount", "numeric", "so_insur_amount"},
			{"Exchange Amount", "numeric", "so_exchange_amount"},
			{"Finance Type", "text", "so_fintype_id IN (SELECT fintype_id FROM compdb.axela_sales_so_finance_type WHERE fintype_name"},
			{"Finance By", "text", "so_fincomp_id IN (SELECT fincomp_id FROM compdb.axela_finance_comp WHERE fincomp_name"},
			{"Finance Amount", "numeric", "so_finance_amt"},
			{"Excess Refund", "numeric", "so_refund_amount"},
			{"Bank Refund", "numeric", "so_bankrefund_amount"},
			{"Type of Sale", "text", "so_saletype_id IN (SELECT saletype_id FROM compdb.axela_sales_so_saletype WHERE saletype_name"},
			{"Active", "boolean", "so_active"},
			{"Notes", "text", "so_notes"},
			{"Entry By", "text", "so_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "so_entry_date"},
			{"Modified By", "text", "so_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "so_modified_date"}

	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = GetSession("emp_all_exe", request);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				so_quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				so_customer_id = CNumeric(PadQuotes(request.getParameter("so_customer_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				PopulateConfigDetails(comp_id);

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND so_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br>Results for all Sales Orders!";
					StrSearch += " AND so_id > 0"
							+ " AND so_delivered_date = ''"
							+ "	AND so_active = '1'";
				} else if (all.equals("recent")) {
					msg = "Recent Sales Orders!";
					StrSearch += " AND so_id > 0";
				} else if (!so_id.equals("0")) {
					msg += "<br>Results for Sales Order ID = " + so_id + "!";
					StrSearch += " AND so_id =" + so_id + "";
				} else if (!so_quote_id.equals("0")) {
					msg += "<br>Results for Quote ID = " + so_quote_id + "!";
					StrSearch += " AND so_quote_id = " + so_quote_id + "";
				} else if (!so_customer_id.equals("0")) {
					msg += "<br>Results for Customer ID = " + so_customer_id + "!";
					StrSearch += " AND customer_id =" + so_customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);

					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " and so_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("sostrsql", request).equals("")) {
						StrSearch += GetSession("sostrsql", request);

					}
				}

				if (!StrSearch.equals("")) {

					SetSession("sostrsql", StrSearch, request);
				}
				StrSearch += BranchAccess + ExeAccess.replace("emp_id", "so_emp_id");

				StrHTML = Listdata(comp_id);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata(String comp_id) {
		CachedRowSet crs = null;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		StringBuilder customer_info = new StringBuilder();

		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT so_id, so_branch_id, branch_brand_id, branch_rateclass_id,"
						+ " CONCAT(so_prefix, so_no) AS so_no, so_date,"
						+ " so_delivered_date, so_dob, so_pan, so_netamt,"
						+ " so_totaltax, so_grandtotal, so_refno, so_active,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, so_auth,"
						+ " contact_id, so_quote_id, so_promise_date, COALESCE(vehstock_id, 0) AS vehstock_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, customer_id,"
						+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name),'') AS item_name,"
						+ " customer_name, emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name,"
						+ " COALESCE(vehstock_delstatus_id, 0) AS vehstock_delstatus_id, so_enquiry_id,"
						+ " COALESCE(delstatus_name, '') AS delstatus_name, customer_curr_bal, so_retail_date,"
						+ " COALESCE(vehstock_delstatus_id, '0') AS vehstock_delstatus_id,"
						+ " COALESCE(vehstock_comm_no, '') AS vehstock_comm_no,"
						+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no,"
						+ " COALESCE(vehstock_chassis_prefix, '') AS vehstock_chassis_prefix,"
						+ " COALESCE(vehstock_chassis_no, '') AS vehstock_chassis_no,"
						// + " COALESCE(voucher_id, 0) AS voucher_id,"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
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
						+ " AND trans_vehstock_id = vehstock_id), '') AS 'Package',"
						+ " COALESCE(vehstock_id, 0) AS vehstock_id, "
						+ "	so_preownedstock_id, COALESCE ( ( SELECT CONCAT( carmanuf_name, '-', preownedmodel_name, '-', variant_name )"
						+ " FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_preowned_id = preowned_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE preownedstock_id = so_preownedstock_id ), '' ) AS Preowned,"
						+ "	TRIM( COALESCE ( IF ( so_preownedstock_id != 0, ( SELECT CONCAT("
						+ " IF(preownedstock_comm_no != '', CONCAT('<br>Commission No.: ', preownedstock_comm_no) , ''),"
						+ " IF(preownedstock_engine_no != '', CONCAT('<br>Engine No.: ', preownedstock_engine_no) , ''),"
						+ " IF(preownedstock_chassis_no != '', CONCAT('<br>Chassis No.: ', preownedstock_chassis_no) , '')"
						+ " ) FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_preowned_id = preowned_id"
						+ " WHERE preownedstock_id = so_preownedstock_id ), '' ), '' )) AS PreownedDetails,"
						+ " REPLACE ( COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
						+ " FROM " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
						+ " WHERE tagtrans_customer_id = so_customer_id ), '' ), ',', '' ) AS tag";

				CountSql = "SELECT COUNT(DISTINCT(so_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_so"
						// + "	INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = so_preownedstock_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = so_item_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
						// + " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_so_id = so_id and voucher_vouchertype_id = 6"
						// + " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {

					StrSql += StrSearch
							+ " GROUP BY so_id"
							+ " ORDER BY so_id DESC";

					CountSql += StrSearch;

				}

				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";

					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {

					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Sales Order(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "veh-salesorder-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}

					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// if (emp_id.equals("1") && comp_id.equals("1011")) {
					// SOPInfo("StrSql==123==sales===mb====" + StrSql);
					// }
					// SOP("List Salesorder-----------" + StrSql);
					if (!all.equals("recent")) {
						crs = processQuery(StrSql, 0);
					}

					int count = StartRec - 1;
					Str.append("<div class=\"table-hover\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
					Str.append("<thead><tr>\n");
					// Str.append("<tr align=\"center\">\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th data-hide=\"phone\">No.</th>\n");
					Str.append("<th data-hide=\"phone\">Sales Order</th>\n");
					Str.append("<th >Customer</th></style>\n");
					Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Tentative Delivery Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Retail Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Delivered Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Delivery Status</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Item</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr >\n");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td >").append("<a href=\"javascript:remote=window.open('salesorder-dash.jsp?so_id=");
						Str.append(crs.getString("so_id")).append("','salesorderdash','');remote.focus();\">");
						Str.append(crs.getString("so_id")).append("</a></td>\n");
						Str.append("<td >").append(crs.getString("so_no")).append("</td>\n");
						Str.append("<td nowrap>\n");
						if (config_sales_enquiry_refno.equals("1")) {
							if (!crs.getString("so_refno").equals("")) {
								Str.append("Ref. No.: ").append(crs.getString("so_refno")).append("<br>");
							}
						}

						if (crs.getString("so_active").equals("0")) {
							Str.append("<font color=\"red\">&nbsp;<b>[Inactive]</b></font><br>");
						}

						if (!crs.getString("so_enquiry_id").equals("0")) {
							Str.append("<a href=\"enquiry-list.jsp?enquiry_id=").append(crs.getString("so_enquiry_id")).append("\">Enquiry ID: ");
							Str.append(crs.getString("so_enquiry_id")).append("</a><br>");
						}

						if (!crs.getString("so_quote_id").equals("0")) {
							Str.append("<a href=\"veh-quote-list.jsp?quote_id=").append(crs.getString("so_quote_id")).append("\">Quote ID: ");
							Str.append(crs.getString("so_quote_id")).append("</a>");
						}

						if (crs.getString("so_auth").equals("1")) {
							Str.append("<br/><font color=red>[Authorized]</font><br>");
						}
						Str.append("</td>");

						Str.append("<td>");
						// Customer Info

						if (!crs.getString("contact_mobile1").equals("")) {
							customer_info.append(crs.getString("contact_mobile1"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_mobile2"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email1"));
						}
						if (!crs.getString("contact_email2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email2"));
						}

						Str.append(CustomerContactDetailsPopup(crs.getString("customer_id"), crs.getString("customer_name"), customer_info.toString(), "customer"));

						customer_info.setLength(0);

						// Contact Info

						if (!crs.getString("contact_mobile1").equals("")) {
							customer_info.append(crs.getString("contact_mobile1"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_mobile2"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email1"));
						}
						if (!crs.getString("contact_email2").equals("")) {
							customer_info.append("<br />" + crs.getString("contact_email2"));
						}

						Str.append("<br/>" + CustomerContactDetailsPopup(crs.getString("contact_id"), crs.getString("contact_name"), customer_info.toString(), "contact"));

						customer_info.setLength(0);

						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("contact_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("contact_id"), "M"));
						}

						// Populating Tags in Enquiry list
						Str.append("<br><br>");
						String Tag = crs.getString("tag");
						Tag = ReplaceStr(Tag, "StartColor", "<label class='btn-xs btn-arrow-left' style='top:-16px; background:");
						Tag = ReplaceStr(Tag, "EndColor", " ; color:white'>&nbsp");
						Tag = ReplaceStr(Tag, "StartName", "");
						Tag = ReplaceStr(Tag, "EndName", "</label>&nbsp&nbsp&nbsp");
						Str.append(Tag);
						// Tags End
						Str.append("</td>");
						customer_info.setLength(0);
						Str.append("<td >").append(strToShortDate(crs.getString("so_date"))).append("</td>\n");
						Str.append("<td  >").append(strToShortDate(crs.getString("so_promise_date"))).append("</td>\n");
						Str.append("<td  >");
						if (!crs.getString("so_retail_date").equals("")) {
							Str.append(strToLongDate(crs.getString("so_retail_date")));
						}
						Str.append("&nbsp;<td >");
						if (!crs.getString("so_delivered_date").equals("")) {
							Str.append(strToLongDate(crs.getString("so_delivered_date")));
						}
						Str.append("&nbsp;</td>\n<td >").append(crs.getString("delstatus_name")).append("</td>");
						Str.append("</td>\n<td>").append(crs.getString("item_name"));
						if (!crs.getString("vehstock_id").equals("0")) {
							Str.append("<a href=\"../inventory/stock-list.jsp?vehstock_id=").append(crs.getString("vehstock_id")).append("\"><br>Stock ID: ");
							Str.append(crs.getString("vehstock_id")).append("</a></b>");

							if (!crs.getString("vehstock_comm_no").equals("")) {
								Str.append("<br>Commission No.: ").append(crs.getString("vehstock_comm_no"));
							}

							if (!crs.getString("vehstock_engine_no").equals("")) {
								Str.append("<br>Engine No.: ").append(crs.getString("vehstock_engine_no"));
							}

							if (!crs.getString("vehstock_chassis_prefix").equals("")) {
								Str.append("<br>Chassis Prefix: ").append(crs.getString("vehstock_chassis_prefix"));
							}

							if (!crs.getString("vehstock_chassis_no").equals("")) {
								Str.append("<br>Chassis No.: ").append(crs.getString("vehstock_chassis_no"));
							}

							if (!crs.getString("Paintwork").equals("")) {
								Str.append("<br>Color : ").append(crs.getString("Paintwork"));
							}

						} else if (!crs.getString("so_preownedstock_id").equals("0")) {
							Str.append("<a href=\"../preowned/preowned-stock-list.jsp?preownedstock_id=").append(crs.getString("so_preownedstock_id")).append("\"><br>Pre-Owned Stock ID: ");
							Str.append(crs.getString("so_preownedstock_id")).append("</a></b><br>");
							Str.append(crs.getString("Preowned"));
							if (!crs.getString("PreownedDetails").equals("")) {
								Str.append(crs.getString("PreownedDetails"));
							}
						}
						Str.append("</td>");
						Str.append("<td nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("so_netamt"))));
						if (!crs.getString("so_totaltax").equals("0")) {
							Str.append("<br>Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("so_totaltax"))));
						}
						Str.append("<br><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("so_grandtotal")))).append("</b>");
						Str.append("</td>\n<td >");
						Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), "")).append("</td>");
						Str.append("<td >");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a></td>\n");
						Str.append("<td nowrap>");

						Str.append(
								"<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>")
								.append("<i class='fa fa-pencil'></i></button>")
								.append("<ul style='margin-top: -5px; width: 27em;' class='dropdown-content dropdown-menu pull-right'><div class='col-md-6 col-xs-6 col-sm-6'>");
						if (group.equals("select_veh_so")) {
							Str.append("<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectVehSO(").append(crs.getString("so_id")).append(",'"
									).append(crs.getString("so_no")).append("');\">Select Vehicle SO</a></li>");
						} else if (group.equals("select_so")) {
							Str.append("<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectSO(").append(crs.getString("so_id")).append(",'")
									.append(crs.getString("contact_name")).append("','")
									.append(crs.getString("contact_id")).append("','")
									.append(crs.getString("customer_name")).append(" (").append(crs.getString("customer_curr_bal")).append(")','")
									.append(crs.getString("customer_id")).append("');\">Select SO</a></li>");
						} else {
							Str.append("<li role=presentation><a href=veh-salesorder-update.jsp?update=yes&so_id=").append(crs.getString("so_id")).append(">Update Sales Order</a></li>")
									.append("<li role=presentation><a href=salesorder-payment-track.jsp?add=yes&so_id=").append(crs.getString("so_id")).append(">Payment Track</a></li>")
									.append("<li role=presentation><a href=veh-salesorder-authorize.jsp?so_id=").append(crs.getString("so_id")).append(">Authorize</a></li>")
									.append("<li role=presentation><a href=veh-salesorder-doc-list.jsp?so_id=").append(crs.getString("so_id")).append(" >List Documents</a></li>")
									.append("<li role=presentation><a href=veh-salesorder-wf-doc-list.jsp?so_id=").append(crs.getString("so_id")).append(" >List Workflow Documents</a></li>")
									.append("<li role=presentation><a href=../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append(">Add Ticket</a></li>");

							if (!crs.getString("so_id").equals("0")) {
								Str.append("<li role=presentation><a href='../accounting/report-ledgerstatement.jsp?all=yes&so_date=").append(crs.getString("so_date")).append("&ledger=")
										.append(crs.getString("customer_id")).append("&so_id=").append(crs.getString("so_id")).append("' target='_blank'>Ledger Statement</a></li>");
							}
							if (!crs.getString("so_retail_date").equals("")) {
								Str.append("<li role=presentation><a href='../sales/sales-ecover-policy-add.jsp?so_id=").append(crs.getString("so_id"))
										.append("' target='_blank'>Add Policy</a></li>");
							}
							Str.append("</div>");
							Str.append("<div class='col-md-6 col-xs-6 col-sm-6'><li role=presentation><a href=../accounting/so-update2.jsp?add=yes&vouchertype_id=27&voucherclass_id=27&dr_executive=")
									.append(crs.getString("emp_id"))
									.append("&dr_voucher_rateclass_id=").append(crs.getString("branch_rateclass_id")).append("&voucher_so_id=").append(crs.getString("so_id")).append("&so_branch_id=")
									.append(crs.getString("so_branch_id")).append(">Add Pre-Order</a></li>");
							Str.append("<li role=presentation><a href=../accounting/voucher-list.jsp?vouchertype_id=27&voucherclass_id=27&voucher_so_id=").append(crs.getString("so_id"))
									.append(">List Pre-Order</a></li>");

							Str.append("<li role=presentation><a href=../accounting/so-update2.jsp?add=yes&checkinvoice=yes&vouchertype_id=6&voucherclass_id=6&vouchertrans_customer_id=")
									.append(crs.getString("customer_id")).append("&span_cont_id=").append(crs.getString("contact_id")).append("&dr_voucher_rateclass_id=")
									.append(crs.getString("branch_rateclass_id"))
									.append("&dr_executive=").append(crs.getString("emp_id")).append("&voucher_so_id=").append(crs.getString("so_id")).append("&so_branch_id=")
									.append(crs.getString("so_branch_id"))
									.append(">Add Invoice</a></li>");
							Str.append("<li role=presentation><a href=../accounting/voucher-list.jsp?vouchertype_id=6&voucherclass_id=6&customer_id=").append(crs.getString("customer_id"))
									.append("&voucher_so_id=").append(crs.getString("so_id")).append(">List Invoice</a></li>");

							Str.append("<li role=presentation><a href=../accounting/so-update2.jsp?add=yes&vouchertype_id=6&voucherclass_id=6&vouchertrans_customer_id=")
									.append(crs.getString("customer_id")).append("&span_cont_id=" + crs.getString("contact_id")).append("&dr_voucher_rateclass_id=")
									.append(crs.getString("branch_rateclass_id"))
									.append("&voucher_so_id=").append(crs.getString("so_id")).append("&so_branch_id=").append(crs.getString("so_branch_id")).append(">Add Accessories</a></li>");

							Str.append("<li role=presentation><a href=../accounting/receipt-update.jsp?add=yes&vouchertype_id=9&voucherclass_id=9&voucher_so_id=").append(crs.getString("so_id"))
									.append("&so_emp_id=").append(crs.getInt("emp_id")).append("&ledger=").append(crs.getString("customer_id")).append("&so_branch=")
									.append(crs.getString("so_branch_id")).append(">Add Receipt</a></li>");
							// .append("&checkinvoice=yes")
							Str.append("<li role=presentation><a href=../accounting/voucher-list.jsp?&vouchertype_id=9&voucherclass_id=9&voucher_so_id=").append(crs.getString("so_id"))
									.append("&customer_id=").append(crs.getString("customer_id")).append(">List Receipts</a></li>");

							if (crs.getString("branch_brand_id").equals("153")) {
								Str.append("<li role=presentation><a href=../sales/print-booking-form-harley.jsp?reportfrom=booking-form-harley&so_id=").append(crs.getString("so_id"))
										.append(">Print Booking Form</a></li>");
							}
							Str.append("<li role=presentation><a href=\"javascript:remote=window.open('salesorder-dash.jsp?so_id=").append(crs.getString("so_id")).append("#tabs-7")
									.append("','sodash','');remote.focus();\"> Add Registration </a></li>");

							// action_info += "<li role=presentation><a href=\"veh-salesorder-print.jsp?so_id="+crs.getString("so_id"));
							// Str.append("&target="+Math.random()+"\" target=_blank>Print Sales Order</a>");
							if (!crs.getString("vehstock_id").equals("0")) {
								Str.append("<li role=presentation><a href=veh-salesorder-din-update.jsp?so_id=").append(crs.getString("so_id")).append(">Update DIN</a></li>");
							}
							if (!crs.getString("so_delivered_date").equals("")) {
								Str.append("<li role=presentation><a href=veh-salesorder-deliverychallan-print1.jsp?brand_id=").append(crs.getString("branch_brand_id")).append("&so_id=")
										.append(crs.getString("so_id")).append(" target=_blank>Print Delivery Challan</a></li>");
							}

							if (crs.getString("branch_brand_id").equals("102")) {
								Str.append("<li role=presentation><a href=form-21-print.jsp?reportfrom=form-21&reportname=Form-21&brand_id=").append(crs.getString("branch_brand_id"))
										.append("&so_id=")
										.append(crs.getString("so_id")).append("target=_blank>Print Form-21</a></li>");
							}
							Str.append("<li role=presentation><a href=veh-salesorder-pdi-print.jsp?reportfrom=veh-salesorder-pdi-print&reportname=&brand_id=")
									.append(crs.getString("branch_brand_id")).append("&so_id=")
									.append(crs.getString("so_id")).append(" target=_blank>Print PDI FORM</a></li>");

							// if (comp_id.equals("1009") && crs.getString("branch_brand_id").equals("2") && !crs.getString("so_delivered_date").equals("")) {
							Str.append("<li role=presentation><a href=report-salesorder-customer-profile-print.jsp?reportfrom=report-salesorder-customer-profile&reportname=&brand_id=")
									.append(crs.getString("branch_brand_id")).append("&so_id=")
									.append(crs.getString("so_id")).append(" target=_blank>Print Customer Profile</a></li>");
							// }

							// Str.append("<br><a href=\"pbf-print.jsp?so_id=").append(crs.getString("so_id"));
							// Str.append("&target=").append(Math.random()).append("\" target=_blank>Print PBF</a>");
							// Str.append("<br><a href=\"psf-print.jsp?so_id=").append(crs.getString("so_id"));
							// Str.append("&target=").append(Math.random()).append("\" target=_blank>Print PSF</a>");
						}

						Str.append("</div>");
						Str.append("</ul></div></center></div>");
						// Str.append(action_info);
						Str.append("</td>\n</tr>\n");
						// action_info = "";
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();

				} else {
					Str.append("<br><br><br><br><font color=\"red\"><b>No Sales Order(s) found!</b></font><br><br>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	public void PopulateConfigDetails(String comp_id) {
		StrSql = "SELECT config_sales_enquiry_refno"
				+ " FROM " + compdb(comp_id) + "axela_config";
		config_sales_enquiry_refno = CNumeric(ExecuteQuery(StrSql));
	}

}
