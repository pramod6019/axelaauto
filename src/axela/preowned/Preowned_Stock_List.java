package axela.preowned;
//Dilip 01 JUL 2013   

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Preowned_Stock_List extends Connect {

	public String LinkHeader = "";
	public String LinkExportPage = "preowned-stock-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String msg = "";
	public String PageURL = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "", retailFilter = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String advSearch = "";
	public String preownedstock_id = "0";
	public String preowned_id = "0";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Stock ID", "numeric", "preownedstock_id"},
			{"Pre Owned ID", "numeric", "preownedstock_preowned_id"},
			{"Pre Owned No.", "numeric", "preowned_no"},
			{"Branch ID", "numeric", "preowned_branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "preownedcustomer_id"},
			{"Contact ID", "numeric", "preownedcontact_id"},
			{"Customer Name", "text", "preownedcustomer_name"},
			{"Contact Name", "text", "CONCAT(preownedtitle.title_desc, ' ', preownedcont.contact_fname, ' ', preownedcont.contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(preownedcont.contact_mobile1, '-', ''), REPLACE(preownedcont.contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(preownedcont.contact_email1, preownedcont.contact_email2)"},
			{"Status ID", "numeric", "preownedstock_status_id"},
			{"Status Name", "text", "preownedstatus_name"},
			{"Stock Date", "date", "preownedstock_date"},
			{"Put To Sale Date", "date", "preownedstock_puttosale_date"},
			{"Purchase Amount", "numeric", "preownedstock_purchase_amt"},
			{"Selling Price", "numeric", "preownedstock_selling_price"},
			{"Stock Engine No.", "text", "preownedstock_engine_no"},
			{"Stock Chassis No.", "text", "preownedstock_chassis_no"},
			{"Model", "text", "preownedmodel_name"},
			{"Variant", "text", "variant_name"},
			{"Sub Variant", "text", "preowned_sub_variant"},
			{"Exterior", "numeric", "extcolour_name"},
			{"Interior", "numeric", "intcolour_name"},
			{"Options", "text", "preowned_options"},
			{"Manuf. Year", "text", "preowned_manufyear"},
			{"Regd. Year", "text", "preowned_regdyear"},
			{"Reg. No.", "text", "preowned_regno"},
			{"Kms", "numeric", "preowned_kms"},
			{"Foreclosure Amt", "numeric", "preowned_fcamt"},
			{"Insurance Date", "text", "preowned_insur_date"},
			{"Insurance Type", "text", "preowned_insurance_id IN (SELECT insurance_id FROM compdb.axela_preowned_insurance WHERE insurance_name"},
			{"Ownership", "text", "preowned_ownership_id IN (SELECT ownership_id FROM compdb.axela_preowned_ownership WHERE ownership_name"},
			{"Invoice Value", "numeric", "preowned_invoicevalue"},
			{"Expected Price", "numeric", "preowned_expectedprice"},
			{"Quoted Price", "numeric", "preowned_quotedprice"},
			{"Notes", "text", "preownedstock_notes"},
			{"Entry By", "text", "preownedstock_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "preownedstock_entry_date"},
			{"Modified By", "text", "preownedstock_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "preownedstock_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			LinkHeader = "<a href=../portal/home.jsp>Home</a>"
					+ " &gt; <a href=../preowned/index.jsp>" + ReturnPreOwnedName(request) + "</a>"
					+ " &gt; <a href=../preowned/preowned-stock.jsp>Stock</a>"
					+ " &gt; <a href=../preowned/preowned-stock-list.jsp?all=yes>List Stocks</a>:";
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "preowned_branch_id");
				CheckPerm(comp_id, "emp_preowned_stock_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				preownedstock_id = CNumeric(PadQuotes(request.getParameter("preownedstock_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				// //SOP("preownedstock_id--------" + preownedstock_id);
				// //SOP("msg--------" + msg);
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND preownedstock_id = 0";
				}

				if ("yes".equals(all)) {
					msg = "Results for all Stock!";
					// StrSearch += " AND preownedstock_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Stock!";
					StrSearch += " AND preownedstock_id > 0";
				} else if (!preownedstock_id.equals("0")) {
					msg += "<br>Results for Stock ID = " + preownedstock_id;
					StrSearch += " AND preownedstock_id = " + preownedstock_id + "";
				} else if (!preowned_id.equals("0")) {
					msg += "<br>Results for Pre Owned ID = " + preowned_id;
					StrSearch += " AND preowned_id = " + preowned_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND preownedstock_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("preownedstockstrsql", request).equals("")) {
						StrSearch += GetSession("preownedstockstrsql", request);
					}
					if (!GetSession("retailFilter", request).equals("")) {
						retailFilter += GetSession("retailFilter", request);
					}
				}

				SOP("list StrSearch==" + StrSearch);

				StrSearch += BranchAccess;
				if (StrSearch.contains("emp_id")) {

				}
				SetSession("preownedstockstrsql", StrSearch, request);
				StrHTML = Listdata();
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

	public String Listdata() {
		CachedRowSet crs = null;
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT preownedstock_id, preownedmodel_id, preownedmodel_name, variant_id,"
						+ " variant_name, preownedstock_date,"
						+ " preownedstock_delstatus_id, "
						+ " preownedstatus_name, preownedtype_name, preowned_id,"
						+ " preowned_extcolour_id, preowned_intcolour_id,"
						+ " preownedstock_engine_no, preownedstock_chassis_no, "
						+ " COALESCE(stock.emp_id,0) AS stockemp_id,"
						+ " COALESCE(sales.emp_id,0) AS salesempid,"
						+ " CONCAT(sales.emp_name,' (', sales.emp_ref_no, ')') AS salesemp_name,"
						+ " preowned_title,"
						// +
						// " COALESCE(preowned_preownedmodel_id, 0) AS preowned_preownedmodel_id,"
						+ " COALESCE(extcolour_id, 0) AS extcolour_id,"
						+ " COALESCE(extcolour_name, '') AS extcolour_name,"
						+ " COALESCE(intcolour_id,0) AS intcolour_id,"
						+ " COALESCE(intcolour_name, '') As intcolour_name, preowned_kms,"
						+ " COALESCE(preowned_options,'') preowned_options,"
						+ " COALESCE(preowned_manufyear,'') preowned_manufyear,"
						+ " COALESCE(preowned_regdyear,'') preowned_regdyear, preowned_regno,"
						+ " preowned_fcamt,"
						+ " COALESCE(preowned_insur_date,'') preowned_insur_date,"
						+ " COALESCE(preowned_sub_variant,'') preowned_sub_variant,"
						+ " preowned_invoicevalue, preowned_expectedprice, preowned_quotedprice,"
						+ " CONCAT(stock.emp_name, ' (', stock.emp_ref_no, ')') AS stockemp_name, preowned_branch_id,"
						+ " CONCAT(preownedcust.customer_name,	' (', preownedcust.customer_id,	')') AS preownedcustomer_name,"
						// + " COALESCE (preownedcust.customer_id,'') AS preownedcontact_id,"
						+ " COALESCE (CONCAT(preownedtitle.title_desc, ' ', preownedcont.contact_fname, ' ', preownedcont.contact_lname),'') AS preownedcontact_name,"
						+ " COALESCE (preownedcust.customer_id,'') AS preownedcustomer_id,"
						+ " COALESCE (preownedcont.contact_id,'') AS preownedcontact_id,"
						+ " COALESCE (preownedcont.contact_mobile1, '') preownedcontact_mobile1,"
						+ " COALESCE (preownedcont.contact_mobile2, '') preownedcontact_mobile2,"
						+ " COALESCE (preownedcont.contact_email1, '') preownedcontact_email1,"
						+ " COALESCE (preownedcont.contact_email2, '') preownedcontact_email2,"

						+ " COALESCE(CONCAT(salescust.customer_name,	' (', salescust.customer_id,	')'), '') AS salesCustomer,"
						+ " COALESCE(salescust.customer_id, '0') AS salescustomer_id,"
						+ " COALESCE(CONCAT(salestitle.title_desc, ' ', salescont.contact_fname, ' ', salescont.contact_lname), '') AS salescontact_name,"
						+ " COALESCE(salescont.contact_id, '0') AS salescontact_id,"
						+ " COALESCE (salescont.contact_mobile1, '') salescontact_mobile1,"
						+ " COALESCE (salescont.contact_mobile2, '') salescontact_mobile2,"

						+ " branch_id,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " COALESCE(so_id, '0') so_id";

				CountSql = "SELECT COUNT(DISTINCT preownedstock_id)";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned_stock"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"

						+ " INNER JOIN " + compdb(comp_id) + "axela_customer preownedcust ON preownedcust.customer_id = preowned_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact preownedcont ON preownedcont.contact_id = preowned_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title preownedtitle ON preownedtitle.title_id = preownedcont.contact_title_id"

						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock_status ON preownedstatus_id = preownedstock_status_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_type ON preownedtype_id = preownedstock_preownedtype_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = preowned_fueltype_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_preowned_ownership ON ownership_id = preowned_ownership_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp stock ON stock.emp_id = preownedstock_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp sales ON sales.emp_id = preowned_sales_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = preowned_campaign_id"
						+ " LEFT JOIN axela_preowned_extcolour ON extcolour_id = preowned_extcolour_id"
						+ " LEFT JOIN axela_preowned_intcolour ON intcolour_id = preowned_intcolour_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_preownedstock_id = preownedstock_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer salescust ON salescust.customer_id = so_customer_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact salescont ON salescont.contact_id = so_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title salestitle ON salestitle.title_id = salescont.contact_title_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch
							+ " GROUP BY preownedstock_id"
							+ " ORDER BY preownedstock_id DESC";
				}
				// //SOP("ExeAccess = " + ExeAccess);
				CountSql += StrSearch;
				// //SOP("CountSql -----= " + CountSql);
				SOP("StrSql -----= " + StrSql);

				// if (all.equals("recent")) {
				// StrSql += " LIMIT " + recperpage + "";
				// rs = processQuery(StrSql, 0);
				// crs.last();
				// TotalRecords = crs.getRow();
				// crs.beforeFirst();
				// } else {
				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				// }

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Stock";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}

					PageURL = "preowned-stock-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_preowned_stock\\b", "FROM " + compdb(comp_id) + "axela_preowned_stock "
								+ " INNER JOIN (SELECT preownedstock_id FROM " + compdb(comp_id) + "axela_preowned_stock"
								+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = preownedstock_preowned_id"
								+ " WHERE 1=1 " + StrSearch
								+ " GROUP BY preownedstock_id"
								+ " ORDER BY preownedstock_id desc"
								+ " LIMIT " + (StartRec - 1) + ","
								+ " " + recperpage + ") as myresults using (preownedstock_id)");

						StrSql = "SELECT * "
								+ " FROM (" + StrSql + ") AS datatable"
								+ " ORDER BY preownedstock_id desc";
					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}

					// SOP("Pre stock Lists-----------" + StrSql);
					crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-hover\">\n");
					Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
					Str.append("<th data-toggle=\"true\">Stock ID</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Pre-Owned ID</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Model</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Variant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Exterior</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Interior</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
					Str.append("<th style=\"min-width:200px;\" data-toggle=\"true\">Customer</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Year</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Kms</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Reg. No.</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Type</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Status</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Delivery Status</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Pre-Owned Consultant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("preownedstock_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("preownedstock_id") + ");'");
						Str.append("style='height:200px'>\n");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("preownedstock_id")).append("</td>\n");
						Str.append("<td valign=top align=left>").append("<a href=\"../preowned/preowned-list.jsp?preowned_id=");
						Str.append(crs.getInt("preowned_id")).append("\" target=_blank>").append(crs.getString("preowned_id")).append("</a>").append("</td>\n");
						Str.append("<td valign=top align=left>").append("<a href=\"../preowned/managepreownedmodel.jsp?preownedmodel_id=");
						Str.append(crs.getInt("preownedmodel_id")).append("\">").append(crs.getString("preownedmodel_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left nowrap>").append("<a href=\"../preowned/managepreownedvariant.jsp?variant_id=");
						Str.append(crs.getInt("variant_id")).append("\">").append(crs.getString("variant_name")).append("</a>");
						if (!crs.getString("preowned_sub_variant").equals("")) {
							Str.append("<br>").append(crs.getString("preowned_sub_variant"));
						}
						Str.append("<br>").append("Engine No.:" + crs.getString("preownedstock_engine_no"));
						Str.append("<br>").append("Chassis No.:" + crs.getString("preownedstock_chassis_no"));
						// SOP("1");
						Str.append("</td>\n<td valign=\"top\" align=\"left\">").append(crs.getString("extcolour_name")).append("</td>\n");
						// SOP("1.1");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("intcolour_name")).append("</td>\n");
						// SOP("1.2");
						Str.append("<td valign=\"top\" align=\"left\">").append(strToShortDate(crs.getString("preownedstock_date"))).append("</td>\n");
						// SOP("1.3");
						Str.append("<td valign=\"top\" align=\"left\" style='weight:200px'>");
						// SOP("1.4");
						// SOP("preownedcustomer_id===" + crs.getString("preownedcustomer_id"));
						// SOP("1.5");
						// SOP("preownedcustomer_name===" + crs.getString("preownedcustomer_name"));
						// SOP("1.56");
						// SOP("preownedcontact_id===" + crs.getString("preownedcontact_id"));
						// SOP("1.57");
						// SOP("preownedcontact_name===" + crs.getString("preownedcontact_name"));
						// SOP("1.58");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("preownedcustomer_id")).append(" \">")
								.append(crs.getString("preownedcustomer_name")).append("</a>");
						// SOP("1.59");
						Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("preownedcontact_id")).append("\">");
						// SOP("1.6");
						Str.append("<br>").append(crs.getString("preownedcontact_name")).append("</a>");
						// SOP("1.7");
						if (!crs.getString("preownedcontact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("preownedcontact_mobile1"), 5, "M", crs.getString("preownedstock_id")))
									.append(ClickToCall(crs.getString("preownedcontact_mobile1"), comp_id));
						}
						// SOP("1.8");
						if (!crs.getString("preownedcontact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("preownedcontact_mobile2"), 5, "M", crs.getString("preownedstock_id")))
									.append(ClickToCall(crs.getString("preownedcontact_mobile2"), comp_id));
						}
						// SOP("1.9");
						if (!crs.getString("preownedcontact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("preownedstock_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("preownedcontact_email1")).append("\">");
							Str.append(crs.getString("preownedcontact_email1")).append("</a></span>");
						}
						// SOP("1.10");
						if (!crs.getString("preownedcontact_email2").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("preownedstock_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("preownedcontact_email2")).append("\">");
							Str.append(crs.getString("preownedcontact_email2")).append("</a></span>");
						}
						// SOP("2");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("preowned_manufyear")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("preowned_kms")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\" nowrap>").append(SplitRegNo(crs.getString("preowned_regno"), 2)).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("preownedtype_name")).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("preownedstatus_name")).append("</td>\n");
						// SOP("3");
						Str.append("<td valign=\"top\" align=\"left\" style='weight:200px'>");
						if (crs.getString("preownedstock_delstatus_id").equals("2")) {
							Str.append("Delivered");

						} else if (crs.getString("preownedstock_delstatus_id").equals("1")) {
							Str.append("In Stock");

						}
						if (!crs.getString("so_id").equals("0")) {
							Str.append("<br>SO ID: <a href='../sales/veh-salesorder-list.jsp?so_id=" + crs.getString("so_id") + "' target=_blank>" + crs.getString("so_id") + "</a>");
						}
						if (!crs.getString("salescustomer_id").equals("0")) {
							Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("salescustomer_id")).append(" \">")
									.append(crs.getString("salesCustomer")).append("</a>");
							Str.append("<a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("salescontact_id")).append("\">");
							Str.append("<br>").append(crs.getString("salescontact_name")).append("</a>");
							if (!crs.getString("salescontact_mobile1").equals("")) {
								Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("salescontact_mobile1"), 5, "M", crs.getString("preownedstock_id")))
										.append(ClickToCall(crs.getString("salescontact_mobile1"), comp_id));
							}
							if (!crs.getString("salescontact_mobile2").equals("")) {
								Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("salescontact_mobile2"), 5, "M", crs.getString("preownedstock_id")))
										.append(ClickToCall(crs.getString("salescontact_mobile2"), comp_id));
							}
						}
						// SOP("4");
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/executive-summary.jsp?emp_id=");
						Str.append(crs.getInt("stockemp_id")).append("\">").append(crs.getString("stockemp_name")).append("</a>");
						if (!crs.getString("salesempid").equals("0")) {
							Str.append("<br>Sales Consultant: <a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("salesempid")).append("\">")
									.append(crs.getString("salesemp_name")).append("</a>");
						}
						// SOP("5");
						Str.append("</td>\n");
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/branch-summary.jsp?branch_id=");
						Str.append(crs.getString("preowned_branch_id")).append("\">").append(crs.getString("branch_name"));
						Str.append("</a></td>\n");
						Str.append("<td valign=\"top\" nowrap><a href=\"preowned-stock-update.jsp?update=yes&preownedstock_id=");
						Str.append(crs.getString("preownedstock_id")).append("&preowned_id=").append(crs.getString("preowned_id"));
						Str.append("\">Update Stock</a><br>\n");
						Str.append("<a href=\"preowned-stock-ageing.jsp?preownedstock_id=").append(crs.getString("preownedstock_id")).append("\">Ageing Status</a><br>\n");
						Str.append("<a href=\"preowned-stock-gatepass-update.jsp?add=yes&preownedstock_id=").append(crs.getString("preownedstock_id")).append("\">Add Gate Pass</a><br>\n");
						Str.append("<a href=\"preowned-stock-gatepass-list.jsp?preownedstock_id=").append(crs.getString("preownedstock_id")).append("\">List Gate Passes</a>\n");
						Str.append("</td>\n</tr>\n");
						// SOP("6");
						// Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					Str.append("</div>\n");
					crs.close();
					return Str.toString();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Stock found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
