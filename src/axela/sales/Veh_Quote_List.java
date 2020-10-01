package axela.sales;
//aJIt

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Veh_Quote_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../sales/index.jsp\">Sales</a>"
			+ " &gt; <a href=\"veh-quote.jsp\">Quotes</a>"
			+ " &gt; <a href=\"veh-quote-list.jsp?all=yes\">List Quotes</a>:";
	public String LinkExportPage = "veh-quote-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
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
	public String quote_id = "0", quote_enquiry_id = "0", android = "";
	public String quote_customer_id = "0";
	public String quote_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_all_exe = "";
	public String config_sales_enquiry_refno = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Quote ID", "numeric", "quote_id"},
			{"Quote No.", "text", "CONCAT(quote_prefix, quote_no)"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Lead ID", "numeric", "quote_lead_id"},
			{"Enquiry ID", "numeric", "quote_enquiry_id"},
			{"Pre-Owned Stock ID", "numeric", "so_preownedstock_id IN (SELECT preownedstock_id FROM compdb.axela_preowned_stock WHERE preownedstock_id"},
			{"Quote Date", "date", "quote_date"},
			{"Net Amount", "numeric", "quote_netamt"},
			{"Discount", "numeric", "quote_discamt"},
			{"Tax", "numeric", "quote_totaltax"},
			{"Total", "numeric", "quote_grandtotal"},
			{"Billing Address", "text", "CONCAT(quote_bill_address, quote_bill_city, quote_bill_pin, quote_bill_state)"},
			{"Shipping Address", "text", "CONCAT(quote_ship_address, quote_ship_city, quote_ship_pin, quote_ship_state)"},
			{"Description", "text", "quote_desc"},
			{"Terms", "text", "quote_terms"},
			{"Ref. No.", "text", "quote_refno"},
			{"Authorized", "boolean", "quote_auth"},
			{"Authorized By", "text", "quote_auth_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"}, // subquery
			{"Authorized Date", "date", "quote_auth_date"},
			{"Executive", "text", "CONCAT(emp_name, emp_ref_no)"},
			{"Active", "boolean", "quote_active"},
			{"Notes", "text", "quote_notes"},
			{"Entry By", "text", "quote_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "quote_entry_date"},
			{"Modified By", "text", "quote_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "quote_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_access", request, response);
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
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				quote_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				quote_customer_id = CNumeric(PadQuotes(request.getParameter("quote_customer_id")));
				all = PadQuotes(request.getParameter("all"));
				android = PadQuotes(request.getParameter("android"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PopulateConfigDetails();

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND quote_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br>Results for all Quotes!";
					StrSearch += " AND quote_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Quotes!";
					StrSearch += " AND quote_id > 0";
				} else if (!quote_id.equals("0")) {
					msg += "<br>Results for Quote ID = " + quote_id + "!";
					StrSearch += " AND quote_id = " + quote_id + "";
				} else if (!quote_enquiry_id.equals("0")) {
					msg += "<br>Results for Enquiry ID = " + quote_enquiry_id + "!";
					StrSearch += " AND quote_enquiry_id = " + quote_enquiry_id;
				} else if (!quote_customer_id.equals("0")) {
					msg += "<br>Results for Customer ID = " + quote_customer_id + "!";
					StrSearch += " AND customer_id = " + quote_customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND quote_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) {
					msg += "<br>Results of Search!";
					if (!GetSession("quotestrsql", request).equals("")) {
						StrSearch = GetSession("quotestrsql", request);
					}
				}
				StrSearch += BranchAccess + ExeAccess.replace("emp_id", "quote_emp_id");

				if (!StrSearch.equals("")) {
					SetSession("quotestrsql", StrSearch, request);
				}
				StrHTML = ListData();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String ListData() {
		CachedRowSet crs = null;
		int TotalRecords;
		String PageURL;
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		StringBuilder customer_info = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT quote_id, quote_branch_id, CONCAT(quote_prefix, quote_no) AS quote_no,"
						+ " quote_netamt, quote_totaltax, quote_grandtotal, quote_refno, quote_auth, quote_active,"
						+ " customer_id, customer_name, contact_id,"
						+ " COALESCE(so_id, 0) AS so_id,"
						+ " COALESCE(so_active, '0') AS  so_active, quote_enquiry_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2, quote_discamt,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name, branch_brand_id, quote_date,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id, COALESCE(item_name, '') As item_name, "
						+ " REPLACE ( COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
						+ " FROM  " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_tag_trans"
						+ " ON tagtrans_tag_id = tag_id WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag ";

				CountSql = "SELECT COUNT(DISTINCT(quote_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_quote"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = quote_enquiry_id AND so_active = '1'"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quote_item_id"
						+ " WHERE quote_enquiry_id != 0";

				StrSql += SqlJoin;
				CountSql += SqlJoin;
				SOP("StrSql--" + StrSql);
				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY quote_id"
							+ " ORDER BY quote_id DESC";
					CountSql += StrSearch;

				}

				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					TotalRecords = Integer.parseInt(CNumeric(ExecuteQuery(CountSql)));
				}

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Quote(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "veh-quote-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

					if (!all.equals("recent")) {
						crs = processQuery(StrSql, 0);
					}
					int count = StartRec - 1;
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th data-hide=\"phone\">No.</th>\n");
					Str.append("<th data-hide=\"phone\">Quote</th>\n");
					Str.append("<th >Customer</th></style>\n");
					Str.append("<th data-hide=\"phone\">Variant</th>\n");
					Str.append("<th data-hide=\"phone\">Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>");
						Str.append("<td>").append(count).append("</td>\n");
						Str.append("<td>").append(crs.getString("quote_id")).append("</td>");
						Str.append("<td>").append(crs.getString("quote_no")).append("</td>");
						Str.append("<td>");
						if (config_sales_enquiry_refno.equals("1")) {
							if (!crs.getString("quote_refno").equals("")) {
								Str.append("Ref. No.: ").append(crs.getString("quote_refno")).append("<br>");
							}
						}

						if (!crs.getString("quote_enquiry_id").equals("0")) {
							Str.append("<a href=\"enquiry-list.jsp?enquiry_id=").append(crs.getString("quote_enquiry_id")).append("\">Enquiry ID: ");
							Str.append(crs.getString("quote_enquiry_id")).append("</a><br>");
						}

						if (crs.getString("quote_active").equals("0")) {
							Str.append("<b><font color=\"red\"><b>[Inactive]</b></font><br>");
						}

						if (crs.getString("quote_auth").equals("1")) {
							Str.append("<font color=\"red\">[Authorized]</font><br>");
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
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("quote_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("quote_id"), "M"));
						}

						// Populating Tags in Enquiry list
						Str.append("<br><br>");

						String Tag = crs.getString("tag");
						Tag = ReplaceStr(Tag, "StartColor", "<button class='btn-xs btn-arrow-left' style='top:-16px; background:");
						Tag = ReplaceStr(Tag, "EndColor", " ; color:white'  disabled>&nbsp");
						Tag = ReplaceStr(Tag, "StartName", "");
						Tag = ReplaceStr(Tag, "EndName", "</button>&nbsp&nbsp&nbsp");
						Str.append(Tag);
						// Tags End

						Str.append("</td>\n");
						Str.append("<td>").append((crs.getString("item_name"))).append("</td>");
						Str.append("<td>").append(strToShortDate(crs.getString("quote_date"))).append("</td>");
						Str.append("<td nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("quote_netamt")))).append("");
						if (!crs.getString("quote_totaltax").equals("0")) {
							Str.append("<br>Service Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("quote_totaltax")))).append("</b>");
						}

						if (!crs.getString("quote_discamt").equals("0")) {
							Str.append("<br>Discount: ").append(IndDecimalFormat(df.format(crs.getDouble("quote_discamt")))).append("</b>");
						}
						Str.append("<br><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("quote_grandtotal")))).append("</b>");
						Str.append("<br><br></td>\n");
						Str.append("<td>");
						Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), ""));
						Str.append("</td>");
						Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branch_name"))
								.append("</a></td>");
						Str.append("<td nowrap>");

						Str.append(
								"<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>")
								.append("<i class='fa fa-pencil'></i></button>")
								.append("<ul class='dropdown-content dropdown-menu pull-right'>");

						if (crs.getString("branch_brand_id").equals("56")) {
							Str.append("<li role=presentation><a href=\"veh-quote-update-new.jsp");
						} else {
							Str.append("<li role=presentation><a href=\"veh-quote-update.jsp");
						}

						Str.append("?update=yes&quote_id=" + crs.getString("quote_id")).append(" \">Update Quote</a></li>");

						Str.append("<li role=presentation><a href=\"veh-quote-authorize.jsp?quote_id=")
								.append(crs.getString("quote_id")).append(" \">Authorize</a></li>");
						Str.append("<li role=presentation><a href=\"veh-quote-discount-update.jsp?add=yes&quote_id=").append(crs.getString("quote_id"));
						Str.append(" \">Request Discount Authorization</a></li>");

						Str.append("<li role=presentation><a href=\"veh-quote-discount-list.jsp?quote_id=").append(crs.getString("quote_id")).append(" \">List Discount Authorization</a></li>");

						if ((!crs.getString("so_id").equals("0") && crs.getString("so_active").equals("0")) || crs.getString("so_id").equals("0")) {
							Str.append("<li role=presentation><a href=\"veh-salesorder-update.jsp?add=yes&quote_id=").append(crs.getString("quote_id")).append(" \">Add Sales Order</a></li>");
						} else if (!crs.getString("so_id").equals("0")) {
							Str.append("<li role=presentation><a href=\"veh-salesorder-list.jsp?quote_id=").append(crs.getString("quote_id")).append(" \">List Sales Order</a></li>");
						}
						Str.append("<li role=presentation><a href=\"veh-quote-email.jsp?quote_id=").append(crs.getString("quote_id"));
						if (crs.getString("branch_brand_id").equals("151")) {
							Str.append("&brand_id=151");
						}
						Str.append(" \">Email Quote</a></li>");
						if (comp_id.equals("1017")) {
							Str.append("<li role=presentation><a href=\"veh-quote-print-joshi.jsp?quote_id=").append(crs.getString("quote_id")).append("&reportname=PROFORMA INVOICE");
						}
						else if (crs.getString("branch_brand_id").equals("56")) {
							Str.append("<li role=presentation><a href=\"veh-quote-print-new.jsp?quote_id=").append(crs.getString("quote_id")).append("&reportname=Proforma Invoice");
						 }
						else{
							Str.append("<li role=presentation><a href=\"veh-quote-print-jasper.jsp?quote_id=").append(crs.getString("quote_id")).append("&reportname=PROFORMA INVOICE");
						}
						if (crs.getString("branch_brand_id").equals("151")) {
							Str.append("&reportfrom=veh-quote-print-one-triumph");
						}
						else if (comp_id.equals("1017")) {
							Str.append("&reportfrom=veh-quote-print-joshi");
						}
						else if (crs.getString("branch_brand_id").equals("56")) {
							Str.append("&reportfrom=veh-quote-print-new");
						}
						else {
							Str.append("&reportfrom=veh-quote-print");
						}
						Str.append("\" target=_blank>Print Quote</a></li>");
						Str.append("</ul></div></center></div>");
						Str.append("</td>\n</tr>\n");

					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();

				} else {
					Str.append("<br><br><font color=\"red\"><b>No Quote(s) found!</b></font>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob,"
				+ " config_sales_campaign, config_sales_enquiry_refno"
				+ " from " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_emp"
				+ " where emp_id = " + emp_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
