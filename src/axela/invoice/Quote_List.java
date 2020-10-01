package axela.invoice;
// Murali 2nd july
//divya 29th nov

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Quote_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=../portal/home.jsp>Home</a>"
			+ " &gt; <a href=../invoice/index.jsp>Invoice</a>"
			+ " &gt; <a href=quote.jsp>Quotes</a>"
			+ " &gt; <a href=quote-list.jsp?all=yes>List Quotes</a>:";
	public String LinkExportPage = "sc-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=quote-update.jsp?add=yes>Add New Quote...</a>";
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
	public String quote_id = "0";
	public String customer_id = "0";
	public String quote_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String config_sales_enquiry_refno = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{ "Keyword", "text", "keyword_arr" },
			{ "Quote ID", "numeric", "quote_id" },
			{ "Quote No.", "text", "concat('QT',branch_code,quote_no)" },
			{ "Branch ID", "numeric", "branch_id" },
			{ "Branch Name", "text", "branch_name" },
			{ "Customer ID", "numeric", "customer_id" },
			{ "Contact ID", "numeric", "contact_id" },
			{ "Customer Name", "text", "customer_name" },
			{ "Contact Name", "text", "concat(title_desc,' ',contact_fname,' ',contact_lname)" },
			{ "Contact Mobile", "text", "concat(REPLACE(contact_mobile1,'-',''), REPLACE(contact_mobile2,'-',''))" },
			{ "Contact Email", "text", "concat(contact_email1,contact_email2)" },
			{ "Lead ID", "numeric", "quote_lead_id" },
			{ "Enquiry ID", "numeric", "quote_enquiry_id" },
			{ "Quote Date", "date", "quote_date" },
			{ "Net Amount", "numeric", "quote_netamt" },
			{ "Discount", "numeric", "quote_discamt" },
			{ "Tax", "numeric", "quote_totaltax" },
			{ "Total", "numeric", "quote_grandtotal" },
			{ "Billing Address", "text", "concat(quote_bill_address,quote_bill_city,quote_bill_pin,quote_bill_state)" },
			{ "Shipping Address", "text", "concat(quote_ship_address,quote_ship_city,quote_ship_pin,quote_ship_state)" },
			{ "Description", "text", "quote_desc" },
			{ "Terms", "text", "quote_terms" },
			{ "Ref. No.", "text", "quote_refno" },
			{ "Authorized", "boolean", "quote_auth" },
			{ "Authorized By", "text", "quote_auth_id in (select emp_id from compdb.axela_emp where emp_name" }, // subquery
			{ "Authorized Date", "date", "quote_auth_date" },
			{ "Executive", "text", "concat(emp_name,emp_ref_no)" },
			{ "Active", "boolean", "quote_active" },
			{ "Notes", "text", "quote_notes" },
			{ "Entry By", "text", "quote_entry_id in (select emp_id from compdb.axela_emp where emp_name" },
			{ "Entry Date", "date", "quote_entry_date" },
			{ "Modified By", "text", "quote_modified_id in (select emp_id from compdb.axela_emp where emp_name" },
			{ "Modified Date", "date", "quote_modified_date" }
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_quote_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				PopulateConfigDetails();

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND quote_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br>Results for all Quotes!";
					StrSearch = StrSearch + " and quote_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Quotes!";
					StrSearch = StrSearch + " AND quote_id > 0";
				} else if (!quote_id.equals("0")) {
					msg = msg + "<br>Results for Quote ID = " + quote_id + "!";
					StrSearch = StrSearch + " AND quote_id = " + quote_id;
				} else if (!customer_id.equals("0")) {
					msg = msg + "<br>Results for Customer ID = " + customer_id + "!";
					StrSearch = StrSearch + " AND customer_id = " + customer_id;
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND quote_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("quotestrsql", request).equals("")) {
						StrSearch = GetSession("quotestrsql", request);
					}
				}
				StrSearch += BranchAccess + ExeAccess;
				if (!StrSearch.equals("")) {
					SetSession("quotestrsql", StrSearch, request);
				}
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
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT quote_id, quote_branch_id, CONCAT('QT', branch_code, quote_no) AS quote_no,"
						+ " quote_date, quote_enquiry_id, quote_netamt, quote_totaltax, quote_grandtotal,"
						+ " quote_refno, quote_auth, quote_active, customer_id, customer_name, contact_id,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name, emp_id";

				CountSql = "SELECT COUNT(DISTINCT(quote_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_quote"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
						+ " WHERE 1 = 1 "
						+ " AND quote_enquiry_id = 0";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;

				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrSearch + " GROUP BY quote_id"
							+ " ORDER BY quote_id DESC";
					CountSql = CountSql + StrSearch;
				}
				if (all.equals("recent")) {
					StrSql = StrSql + " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}
				// SOP("Quote StrSql=1111=" + StrSqlBreaker(StrSql));
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
					PageURL = "quote-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					if (!all.equals("recent")) {
						// SOP("Quote StrSql=2222=" + StrSqlBreaker(StrSql));
						crs = processQuery(StrSql, 0);
					}

					int count = StartRec - 1;
					Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
					Str.append("<tr align=center>\n");
					Str.append("<th>#</th>\n");
					Str.append("<th>ID</th>\n");
					Str.append("<th>No.</th>\n");
					Str.append("<th>Quote</th>\n");
					Str.append("<th>Customer</th>\n");
					Str.append("<th>Date</th>\n");
					Str.append("<th>Amount</th>\n");
					Str.append("<th>Executive</th>\n");
					Str.append("<th>Branch</th>\n");
					Str.append("<th>Actions</th>\n");
					Str.append("</tr>\n");

					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td align=center valign=top>").append(count).append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("quote_id")).append("</td>\n");
						Str.append("<td valign=top align=center>").append(crs.getString("quote_no")).append("</td>\n");
						Str.append("<td valign=top align=left>");
						if (config_sales_enquiry_refno.equals("1")) {
							if (!crs.getString("quote_refno").equals("")) {
								Str.append("Ref. No.: ").append(crs.getString("quote_refno")).append("<br>");
							}
						}

						if (crs.getString("quote_active").equals("0")) {
							Str.append("<b><font color=red><b>[Inactive]</b></font><br>");
						}

						if (crs.getString("quote_auth").equals("1")) {
							Str.append("<font color=red>[Authorized]</font><br>");
						}

						if (!crs.getString("quote_enquiry_id").equals("0")) {
							Str.append("<a href=enquiry-list.jsp?enquiry_id=").append(crs.getString("quote_enquiry_id")).append(">Enquiry ID: ").append(crs.getString("quote_enquiry_id"))
									.append("</a><br>");
						}

						Str.append("</td>\n");
						Str.append("<td valign=top align=left>");
						Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").
								append(crs.getString("customer_id")).append("\">").
								append(crs.getString("customer_name")).append("</a>");
						Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").
								append(crs.getString("contact_id")).append("\">").
								append(crs.getString("contact_name")).append("</a>");
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M"));
						}

						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M"));
						}

						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>");
						}

						Str.append("</td>\n");
						Str.append("<td valign=top align=center>").append(strToShortDate(crs.getString("quote_date"))).append("</td>\n");
						Str.append("<td valign=top align=right nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("quote_netamt"))));
						if (!crs.getString("quote_totaltax").equals("0")) {
							Str.append("<br>Service Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("quote_totaltax")))).append("</b>");
						}

						Str.append("<br><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("quote_grandtotal")))).append("</b>");
						Str.append("<br><br></td>\n");
						Str.append("<td valign=top>");
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
						Str.append("<td valign=top><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branch_name"))
								.append("</a></td>\n");
						Str.append("<td valign=top align=left nowrap>");
						if (!crs.getString("quote_enquiry_id").equals("0")) {
							Str.append("<a href=\"quote-update.jsp?update=yes&quote_id=").append(crs.getString("quote_id")).append("&enquiry_id=").append(crs.getString("quote_enquiry_id"))
									.append(" \">Update Quote</a>");
						} else {
							Str.append("<a href=\"quote-update.jsp?update=yes&quote_id=").append(crs.getString("quote_id")).append(" \">Update Quote</a>");
						}
						Str.append("<br><a href=\"quote-authorize.jsp?quote_id=").append(crs.getString("quote_id")).append("\">Authorize</a>");
						Str.append("<br><a href=\"invoice-update.jsp?add=yes&quote_id=").append(crs.getString("quote_id")).append(" \">Add Invoice</a>");
						Str.append("<br><a href=\"quote-email.jsp?quote_id=").append(crs.getString("quote_id")).append(" \">Email Quote</a>");
						Str.append("<br><a href=\"quote-print.jsp?quote_id=").append(crs.getString("quote_id")).append("&target=").append(Math.random());
						Str.append("\" target=_blank>Print Quote</a>");
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} else {
					Str.append("<br><br><font color=red><b>No Quote(s) found!</b></font>");
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
				+ " FROM " + compdb(comp_id) + "axela_comp,"
				+ " " + compdb(comp_id) + "axela_config,"
				+ " " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id = " + emp_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
