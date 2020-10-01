//@Bhagwan Singh 11 feb 2013
package axela.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Customer_Contact_List_Modal extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=../customer/index.jsp>Customers</a>"
			+ " &gt; <a href=customer-contact.jsp>Contacts</a> &gt;";
	public String LinkExportPage = "customer-contact-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String all = "";
	public String StrHTML = "";
	public String smart = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String contact_id = "0";
	public String customer_id = "0";
	public String contact_customer_id = "0";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String config_sales_enquiry = "0";
	public String advSearch = "";
	public String group = "";
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Job Title", "text", "contact_jobtitle"},
			{"Location", "text", "contact_location"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1,'-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Phone1", "text", "contact_phone1"},
			{"Phone2", "text", "contact_phone2"},
			{"Email1", "text", "contact_email1"},
			{"Email2", "text", "contact_email2"},
			{"Yahoo", "text", "contact_yahoo"},
			{"MSN", "text", "contact_msn"},
			{"AOL", "text", "contact_aol"},
			{"Skype", "text", "contact_skype"},
			{"Address", "text", "CONCAT(contact_address, ', ', city_name, ' - ', contact_pin, ', ', state_name, '.')"},
			{"Landmark", "text", "contact_landmark"},
			{"DOB", "date", "contact_dob"},
			{"Anniversary", "date", "contact_anniversary"},
			{"Active", "boolean", "contact_active"},
			{"Notes", "text", "contact_notes"},
			{"Entry By", "text", "contact_entry_id IN (SELECT emp_id from compdb.axela_emp where emp_name"},
			{"Entry Date", "date", "contact_entry_date"},
			{"Modified By", "text", "contact_modified_id IN (SELECT emp_id from compdb.axela_emp where emp_name"},
			{"Modified Date", "date", "contact_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				CheckPerm(comp_id, "emp_contact_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				group = PadQuotes(request.getParameter("group"));
				SOP("group====" + group);
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				contact_customer_id = CNumeric(PadQuotes(request.getParameter("contact_customer_id")));
				PopulateConfigDetails();

				if (!group.equals("select_quote_contact")) {
					LinkHeader = LinkHeader + "&nbsp;" + "<a href=\"customer-contact-list.jsp?all=yes\">List Contact Persons</a>:";
				} else {
					LinkHeader = "";
				}
				LinkAddPage = "<a href=customer-contact-update.jsp?Add=yes>Add New Contact Person</a>";

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND contact_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Contact Persons!";
					StrSearch += " and contact_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent Contact Persons!";
					StrSearch += " AND contact_id > 0";
				} else if (!customer_id.equals("0") && contact_id.equals("0")) {
					LinkAddPage = "<a href=customer-contact-update.jsp?Add=yes&customer_id=" + customer_id + ">Add New Contact Person...</a>";
					StrSearch += " AND contact_customer_id = " + customer_id + "";
					msg += "<br>Results for Contacts for Customer ID = " + customer_id + "!";
				} else if (!contact_id.equals("0")) {
					StrSearch += " AND contact_id = " + contact_id + "";
					msg += "<br>Results for Contact ID = " + contact_id + "!";
				} else if (!contact_customer_id.equals("0")) {
					StrSearch += " AND customer_id = " + contact_customer_id + "";
					msg += "<br>Results for Customer ID = " + contact_customer_id + "!";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND contact_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) // for smart search
				{
					msg = msg + "<br>Results of Search!";
					if (!GetSession("contactstrsql", request).equals("")) {
						StrSearch += GetSession("contactstrsql", request);
					}
				}

				StrSearch += BranchAccess;
				if (!StrSearch.equals("")) {
					SetSession("contactstrsql", StrSearch, request);
				}
				StrHTML = ListData();
				if (!StrHTML.equals("")) {
					LinkAddPage += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=../portal/email-send.jsp?contact_id=" + contact_id + ">Send Email</a>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href=../portal/sms-send.jsp?contact_id=" + contact_id + ">Send Sms</a>";
				}
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
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		String address = "";
		String active = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search
				StrSql = "SELECT contact_id, contact_customer_id, contact_active, contact_pin,"
						+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
						+ " contact_jobtitle, contact_location, contact_phone1, contact_phone2, contact_mobile1,"
						+ " contact_mobile2, contact_anniversary, contact_email1, contact_email2, contact_yahoo,"
						+ " contact_msn, contact_aol, contact_address, contact_landmark, contact_dob,"
						+ " COALESCE(state_name, '') AS state_name, COALESCE(city_name, '') AS city_name,"
						+ " customer_curr_bal, customer_id, customer_name, branch_id, contact_landmark,"
						+ " COALESCE(CONCAT(branch_name, ' (', branch_code, ')'), '') AS branch_name";

				CountSql = " SELECT Count(distinct(contact_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_city on city_id = contact_city_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
						+ " WHERE 1 = 1";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;
				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY contact_id"
							+ " ORDER BY contact_id DESC";
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
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Contact Person(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "customer-contact-list.jsp?" + QueryString + "&PageCurrent=";
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
					// SOP("StrSql===customer modal====" + StrSql);
					int count = StartRec - 1;
					Str.append("<div class=\"table\">\n");
					Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th>Contact Person</th>\n");
					Str.append("<th data-hide=\"phone\">Communication</th>\n");
					Str.append("<th data-hide=\"phone\">Address</th>\n");
					Str.append("<th data-hide=\"phone\">Customer</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr></thead><tbody>\n");
					while (crs.next()) {
						count++;
						if (crs.getString("contact_active").equals("0")) {
							active = "<font color=\"red\"><b>&nbsp;[Inactive]</b></font>";
						} else {
							active = "";
						}
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(crs.getString("contact_id")).append("</td>\n");
						Str.append("<td valign=top align=left>").append(crs.getString("contact_name")).append(active);
						// Str.append(rs.getString("contact_name")).append(active);
						if (!crs.getString("contact_jobtitle").equals("")) {
							Str.append("<br>").append(crs.getString("contact_jobtitle"));
						}
						if (!crs.getString("contact_location").equals("")) {
							Str.append("<br>").append(crs.getString("contact_location"));
						}
						Str.append("</td>\n<td valign=top align=left nowrap>");
						if (!crs.getString("contact_phone1").equals("")) {
							Str.append(crs.getString("contact_phone1")).append("<br>");
						}
						if (!crs.getString("contact_phone2").equals("")) {
							Str.append(crs.getString("contact_phone2")).append("<br>");
						}
						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append(crs.getString("contact_mobile1")).append("<br>");
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(crs.getString("contact_mobile2")).append("<br>");
						}
						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_email1")).append(">").append(crs.getString("contact_email1")).append("</a>").append("<br>");
						}
						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_email2")).append(">").append(crs.getString("contact_email2")).append("</a>").append("<br>");
						}
						if (!crs.getString("contact_yahoo").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_yahoo")).append(">").append(crs.getString("contact_yahoo")).append("</a>").append("<br>");
						}
						if (!crs.getString("contact_msn").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_msn")).append(">").append(crs.getString("contact_msn")).append("</a>").append("<br>");
						}
						if (!crs.getString("contact_aol").equals("")) {
							Str.append("<a href=mailto:").append(crs.getString("contact_aol")).append(">").append(crs.getString("contact_aol")).append("</a>").append("<br>");
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=left>");
						address = crs.getString("contact_address");
						if (!address.equals("")) {
							address = crs.getString("contact_address");
							if (!crs.getString("city_name").equals("")) {
								address += ", " + crs.getString("city_name");
							}
							address += " - " + crs.getString("contact_pin");
							if (!crs.getString("state_name").equals("")) {
								address += ", " + crs.getString("state_name");
							}
							if (!crs.getString("contact_landmark").equals("")) {
								address += "<br>Landmark: " + crs.getString("contact_landmark");
							}
						}
						Str.append(address).append("</td>\n");
						Str.append("<td valign=top align=left >");
						Str.append("<a href=\"customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left >");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a></td>\n");
						Str.append("<td valign=top align=left nowrap>");
						if (group.equals("select_veh_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Vehicle Contact</a>\n");
						} else if (group.equals("select_call_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(");
							Str.append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("','");
							Str.append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("customer_name")).append("','");
							Str.append(crs.getString("contact_address")).append("','");
							Str.append(crs.getString("contact_landmark")).append("','");
							Str.append(crs.getString("contact_mobile1")).append("','");
							Str.append(crs.getString("contact_mobile2")).append("','hide');\">Select Call Contact</a>\n");
						} else if (group.equals("select_pickup_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(");
							Str.append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("','");
							Str.append(crs.getString("customer_id")).append("','").append(crs.getString("customer_name")).append("','");
							Str.append(crs.getString("contact_address")).append("','").append(crs.getString("contact_landmark")).append("','");
							Str.append(crs.getString("contact_mobile1")).append("','").append(crs.getString("contact_mobile2")).append("','");
							Str.append("hide');\">Select Pickup Contact</a>\n");
						} else if (group.equals("select_courtesycar_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(");
							Str.append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("','");
							Str.append(crs.getString("contact_mobile1")).append("','");
							Str.append(crs.getString("contact_mobile2")).append("','");
							Str.append(crs.getString("contact_landmark")).append("','");
							Str.append(crs.getString("contact_address")).append("','");
							Str.append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("customer_name")).append("','");
							Str.append("hide');\">Select Courtesy Contact</a>\n");
						} else if (group.equals("select_insurance_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(");
							// Str.append(rs.getString("insurpolicy_title")).append("','");
							// Str.append(rs.getString("insur_company")).append("','");
							// Str.append(rs.getString("insur_policy_name")).append("','");
							// Str.append(rs.getString("insurpolicy_policy_no")).append("','");
							// Str.append(rs.getString("insur_amt")).append("','");
							// Str.append(rs.getString("insurpolicy_desc")).append("','");
							Str.append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("','");
							Str.append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("customer_name")).append("','");
							Str.append("hide');\">Select Insurance Contact</a>\n");
						} else if (group.equals("select_quote_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id"));
							Str.append(",'").append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_so_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_invoice_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append(" (").append(crs.getString("customer_curr_bal")).append(")','hide');\">Select Contact</a>\n");
						} else if (group.equals("select_contract_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id"));
							Str.append(",'").append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_jobcard_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("selectcontact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectCompanyContact1(").append(crs.getString("contact_id")).append(", ");
							Str.append(crs.getString("contact_customer_id")).append(", '").append(crs.getString("customer_name")).append("', '");
							Str.append(crs.getString("contact_name"));
							Str.append("', 'prop_contact_id', 'Contact', 'span_prop_contact_id');\">Select Contact</a>\n");
						} else {
							Str.append("<a href=\"customer-contact-update.jsp?update=yes&customer_id=").append(crs.getString("customer_id"));
							Str.append("&contact_id=").append(crs.getString("contact_id")).append("\">Update Contact Person</a>");
							Str.append("<br><a href=\"../portal/activity-update.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add Activity</a>");
							if (autosales == 1) {
								Str.append("<br><a href=\"../sales/enquiry-quickadd.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add Enquiry</a>");
							}
							Str.append("<br><a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add Ticket</a>");
							if (comp_email_enable.equals("1") && config_email_enable.equals("1")) {
								Str.append("<br><a href=../portal/email-send.jsp?contact_id=").append(crs.getString("contact_id")).append(">Send Email</a>");
							}
							if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
								Str.append("<br><a href=../portal/sms-send.jsp?contact_id=").append(crs.getString("contact_id")).append(">Send SMS</a>");
							}
						}
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody></table>\n");

				} else {
					Str.append("<br><br><br><br><b><font color=\"red\">No Contact Person(s) found!</font><b><br><br>");
				}
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	protected void PopulateConfigDetails() {

		StrSql = "SELECT coalesce(comp_email_enable,'') as comp_email_enable,"
				+ " coalesce(comp_sms_enable,'') as comp_sms_enable,"
				+ " coalesce(config_email_enable,'') as config_email_enable,"
				+ " coalesce(config_sms_enable,'') as config_sms_enable"
				// + " coalesce(config_sales_enquiry,'') as config_sales_enquiry"
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp on emp_id = " + emp_id;
		try {
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				// config_sales_enquiry = rs.getString("config_sales_enquiry");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
