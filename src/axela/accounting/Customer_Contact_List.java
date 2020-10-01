package axela.accounting;
//saiman 21st june 2012

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Customer_Contact_List extends Connect {
	// ///// List page links

	public String LinkHeader = "";
	public String LinkExportPage = "customer-contact-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String comp_id = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String emp_id = "0", branch_id = "0";

	public String contact_id = "0";
	public String customer_id = "0";
	public String comp_businesstype_id = "0";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String comp_module_sales = "0";
	public String config_sales_oppr = "0";
	public String comp_module_pos = "0";
	public String comp_module_service = "0";
	public String comp_module_realtor = "0";
	public String comp_module_promoter = "0";
	public String config_sales_oppr_name = "";
	public Smart SmartSearch = new Smart();
	public String config_customer_name = "";
	public String advSearch = "";
	public String group = "";

	public String smartarr[][];

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			// CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_contact_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request)));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				config_customer_name = GetSession("config_customer_name", request);
				config_sales_oppr_name = GetSession("config_sales_oppr_name", request);
				LinkHeader = "<a href=../portal/home.jsp>Home</a>"
						+ " &gt; <a href=../customer/index.jsp>" + config_customer_name + "</a>"
						+ " &gt; <a href=customer-contact.jsp>Contacts</a> &gt;";
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));

				PopulateConfigDetails();
				BuildSmartArr();
				if (!group.equals("select_quote_contact")) {
					LinkHeader += "&nbsp;<a href=\"customer-contact-list.jsp?all=yes\">List Contact Persons</a>:";
				} else {
					LinkHeader = "";
				}
				LinkAddPage = "<a href=\"customer-contact-update.jsp?Add=yes\">Add New Contact Person..</a>";

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND contact_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Contact Persons!";
				} else if (!customer_id.equals("0") && contact_id.equals("0")) {
					LinkAddPage = "<a href=\"customer-contact-update.jsp?Add=yes&customer_id=" + customer_id + "\">Add New Contact Person...</a>";
					StrSearch += " AND contact_customer_id =" + customer_id + "";
					msg += "<br>Results for Contacts for " + config_customer_name + " ID = " + customer_id + "!";
				} else if (!contact_id.equals("0")) {
					StrSearch += " AND contact_id =" + contact_id + "";
					msg += "<br>Results for Contact ID =" + contact_id + "!";
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
					msg += "Results of Search!";
					if (!GetSession("contactstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("contactstrsql", request);
					}
				}
				StrSearch += BranchAccess.replace("branch_id", "customer_branch_id");
				SetSession("contactstrsql", StrSearch, request);

				StrHTML = ListData();
				if (!StrHTML.equals("")) {
					LinkAddPage += "<br><a href=\"../portal/email-send.jsp?contact_id=" + contact_id + "\">Send Email</a>";
					LinkAddPage += "<br><a href=\"../portal/sms-send.jsp?contact_id=" + contact_id + "\">Send Sms</a>";
				}
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

	public String ListData() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		String address = "";
		String active = "";
		String Img = "";
		StringBuilder Str = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				// to know no of records depending on search
				StrSql = "SELECT contact_id, contact_customer_id, contact_address, contact_photo,"
						+ " title_desc,contact_fname, contact_lname,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
						+ " contact_jobtitle, contact_location, contact_phone1,"
						+ " contact_phone2, contact_mobile1, contact_mobile2, contact_anniversary,"
						+ " contact_email1, contact_email2, contact_yahoo, contact_msn, customer_name,"
						+ " contact_pin, contact_landmark, contact_dob, contact_active, customer_id,"
						+ " COALESCE(state_name, '') AS state_name, contact_aol, branch_id,"
						+ " COALESCE(city_name, '') AS city_name, COALESCE(country_name, '') AS country_name,"
						+ " COALESCE(CONCAT(branch_name, ' (', branch_code, ')'), '') AS branch_name,"
						+ " currentbal_amount";

				CountSql = "SELECT COUNT(DISTINCT(contact_id))";

				SqlJoin = " FROM  " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = customer_branch_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_country ON country_id = state_country_id"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_currentbal ON currentbal_customer_id = contact_customer_id "
						+ " AND currentbal_company_id =" + comp_id
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY contact_id"
							+ " ORDER BY contact_id DESC";
				}
				CountSql += StrSearch;

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

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

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM  " + compdb(comp_id) + "axela_customer_contact\\b",
								"FROM  " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN  " + compdb(comp_id) + "(SELECT contact_id FROM  " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
										+ " WHERE 1=1" + StrSearch
										+ " GROUP BY contact_id"
										+ " ORDER BY contact_id DESC"
										+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults USING (contact_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
								+ " ORDER BY contact_id DESC";

					} else {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);

					int count = StartRec - 1;
					Str.append("<table width=100% border=1 cellspacing=0 cellpadding=0 class=\"footable listtable\">");
					Str.append("<thead>\n<tr align=\"center\">\n");
					Str.append("<th data-hide=\"phone,tablet\" data-ignore=\"true\">#</th>\n");
					Str.append("<th data-toggle=\"true\"><span class=\"footable-toggle\"></span>ID</th>\n");
					Str.append("<th>Contact Person</th>\n");
					Str.append("<th data-hide=\"phone\">Communication</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Address</th>\n");
					Str.append("<th>" + config_customer_name + "</th>\n");
					Str.append("<th data-hide=\"phone\">Branch</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n</thead>\n");
					while (crs.next()) {
						if (crs.getString("contact_photo").equals("")) {
							Img = "";
						} else {
							Img = "<img src=../Thumbnail.do?contactphoto=" + crs.getString("contact_photo") + "&width=100 alt=" + crs.getString("contact_name") + "><br>";
						}
						count++;
						if (crs.getString("contact_active").equals("0")) {
							active = "<font color=\"red\"><b>&nbsp;[Inactive]</b></font>";
						} else {
							active = "";
						}
						Str.append("<tr>\n");
						Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
						Str.append("<td valign=\"top\" align=\"center\">");
						Str.append(crs.getString("contact_id"));
						Str.append("</td>\n<td valign=\"top\" align=\"left\">");
						Str.append(Img).append(crs.getString("contact_name")).append(active);
						if (!crs.getString("contact_jobtitle").equals("")) {
							Str.append("<br>").append(crs.getString("contact_jobtitle"));
						}

						if (!crs.getString("contact_location").equals("")) {
							Str.append("<br>").append(crs.getString("contact_location"));
						}
						Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>");
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
							Str.append("<a href=\"mailto:").append(crs.getString("contact_email1")).append("\">").append(crs.getString("contact_email1")).append("</a><br>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<a href=\"mailto:").append(crs.getString("contact_email2")).append("\">").append(crs.getString("contact_email2")).append("</a><br>");
						}

						if (!crs.getString("contact_yahoo").equals("")) {
							Str.append("<a href=\"mailto:").append(crs.getString("contact_yahoo")).append("\">").append(crs.getString("contact_yahoo")).append("</a><br>");
						}

						if (!crs.getString("contact_msn").equals("")) {
							Str.append("<a href=\"mailto:").append(crs.getString("contact_msn")).append("\">").append(crs.getString("contact_msn")).append("</a><br>");
						}

						if (!crs.getString("contact_aol").equals("")) {
							Str.append("<a href=\"mailto:").append(crs.getString("contact_aol")).append("\">").append(crs.getString("contact_aol")).append("</a><br>");
						}
						Str.append("</td>\n<td valign=\"top\" align=\"left\">");
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

							if (!crs.getString("Country_name").equals("")) {
								address += ", " + crs.getString("Country_name") + ".";
							}

							if (!crs.getString("contact_landmark").equals("")) {
								address += "<br>Landmark: " + crs.getString("contact_landmark");
							}
						}
						Str.append(address);
						Str.append("</td>\n<td valign=\"top\" align=\"left\"><a href=\"customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
						Str.append(crs.getString("customer_name")).append("</a></td>");
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a></td>\n");
						Str.append("<td valign=\"top\" align=\"left\" nowrap>");
						if (group.equals("select_bill_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_quote_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_dlnote_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_so_contact")) {
							Str.append("<a href=# onClick=\"javascript:SelectSOContact(").append(crs.getString("contact_id")).append(");window.parent.SelectContact(");
							Str.append(crs.getString("contact_id")).append(",'").append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_invoice_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'");
							Str.append(crs.getString("customer_name")).append(" (").append(crs.getString("currentbal_amount")).append(")','hide');\">Select Contact</a>\n");
						} else if (group.equals("select_prop_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectPropContact('").append(crs.getString("contact_id")).append("','");
							Str.append(crs.getString("title_desc")).append("','");
							Str.append(crs.getString("contact_fname")).append("','");
							Str.append(crs.getString("contact_lname")).append("','");
							Str.append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_joborder_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'").append(crs.getString("customer_name"))
									.append("');\">Select Contact</a>\n");
						} else if (group.equals("select_billreceipt_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'");
							Str.append(crs.getString("contact_name")).append("',").append(crs.getString("customer_id")).append(",'").append(crs.getString("customer_name")).append("','");
							Str.append(crs.getString("currentbal_amount")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_contract_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'").append(crs.getString("contact_name"))
									.append("',");
							Str.append(crs.getString("customer_id")).append(",'").append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("select_jobcard_contact")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectContact(").append(crs.getString("contact_id")).append(",'").append(crs.getString("contact_name"))
									.append("',");
							Str.append(crs.getString("customer_id")).append(",'").append(crs.getString("customer_name")).append("');\">Select Contact</a>\n");
						} else if (group.equals("selectcontact")) {
							Str.append("<a href=# onClick=\"javascript:SelectCompanyContact1(").append(crs.getString("contact_id")).append(", ").append(crs.getString("contact_customer_id"))
									.append(", '");
							Str.append(crs.getString("customer_name")).append("', '").append(crs.getString("contact_name"))
									.append("', 'prop_contact_id', 'Contact', 'span_prop_contact_id');\">Select Contact</a>\n");
						} else {
							Str.append("<a href=\"customer-contact-update.jsp?Update=yes&customer_id=").append(crs.getString("customer_id")).append("&contact_id=");
							Str.append(crs.getString("contact_id")).append("\">UPDATE  " + compdb(comp_id) + "Contact Person</a><br>");
							Str.append("<a href=\"../portal/activity-update.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add Activity</a><br>");
							if (comp_module_sales.equals("1") && config_sales_oppr.equals("1")) {
								Str.append("<a href=\"../sales/enquiry-quickadd.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add " + config_sales_oppr_name + "</a><br>");
							}

							if (comp_module_realtor.equals("1")) {
								Str.append("<a href=\"../realtor/property-update.jsp?add=yes&prop_contact_id=").append(crs.getString("contact_id")).append("\">Add Property</a><br>");
							}

							if (comp_module_pos.equals("1")) {
								Str.append("<a href=\"../pos/bill-update.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append(" \">Add Bill</a><br>");
							}

							if (comp_module_service.equals("1")) {
								Str.append("<a href=\"../service/ticket-add.jsp?add=yes&contact_id=").append(crs.getString("contact_id")).append("\">Add Ticket</a><br>");
							}

							if (comp_email_enable.equals("1") && config_email_enable.equals("1") && !(crs.getString("contact_email1").equals(""))) {
								Str.append("<a href=\"../portal/email-send.jsp?contact_id=").append(crs.getString("contact_id")).append("\">Send Email</a><br>");
							}

							if (comp_sms_enable.equals("1") && config_sms_enable.equals("1")) {
								Str.append("<a href=\"../portal/sms-send.jsp?contact_id=").append(crs.getString("contact_id")).append("\">Send SMS</a><br>");
							}
							Str.append("<a href=\"customer-contact-photo.jsp?contact_id=").append(crs.getString("contact_id")).append("\">UPDATE  " + compdb(comp_id) + "Photo</a>");
							Str.append("<br><br>\n");
						}
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</table>\n");
					crs.close();
				} else {
					Str.append("<br><br><br><br><b><font color=\"red\">No Contact Person(s) found!</font><b><br><br>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT COALESCE(comp_businesstype_id, 0) AS comp_businesstype_id,"
					+ " COALESCE(comp_email_enable, '') AS comp_email_enable,"
					+ " COALESCE(comp_sms_enable, '') AS comp_sms_enable,"
					+ " COALESCE(config_email_enable, '') AS config_email_enable,"
					+ " COALESCE(config_sms_enable, '') AS config_sms_enable,"
					+ " COALESCE(comp_module_sales, '') AS comp_module_sales,"
					+ " COALESCE(config_sales_oppr, '') AS config_sales_oppr,"
					+ " COALESCE(comp_module_pos, '') AS comp_module_pos,"
					+ " COALESCE(comp_module_service, '') AS comp_module_service,"
					+ " COALESCE(comp_module_realtor, '') AS comp_module_realtor,"
					+ " COALESCE(comp_module_promoter, '') AS comp_module_promoter"
					+ " FROM  " + compdb(comp_id) + "axela_config,"
					+ "  axela_comp"
					+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = " + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				comp_businesstype_id = crs.getString("comp_businesstype_id");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				comp_module_sales = crs.getString("comp_module_sales");
				comp_module_pos = crs.getString("comp_module_pos");
				comp_module_service = crs.getString("comp_module_service");
				comp_module_realtor = crs.getString("comp_module_realtor");
				comp_module_promoter = crs.getString("comp_module_promoter");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_sales_oppr = crs.getString("config_sales_oppr");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void BuildSmartArr() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"Keyword", "text", "keyword_arr"});
		list.add(new String[]{"Contact ID", "numeric", "contact_id"});
		list.add(new String[]{"Contact Name", "text", "CONCAT(title_desc,' ',contact_fname,' ',contact_lname)"});
		list.add(new String[]{"" + config_customer_name + " ID", "numeric", "customer_id"});
		list.add(new String[]{"" + config_customer_name + " Name", "text", "customer_name"});
		list.add(new String[]{"Branch ID", "numeric", "branch_id"});
		list.add(new String[]{"Branch Name", "text", "branch_name"});

		list.add(new String[]{"Mobile", "text", "concat(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"});
		list.add(new String[]{"Phone", "text", "concat(REPLACE(contact_phone1,'-',''),REPLACE(contact_phone2,'-',''))"});
		list.add(new String[]{"Email", "text", "concat(contact_email1, contact_email2)"});
		list.add(new String[]{"Job Title", "text", "contact_jobtitle"});
		list.add(new String[]{"Location", "text", "contact_location"});
		list.add(new String[]{"Fax1", "text", "customer_fax1"});
		list.add(new String[]{"Fax2", "text", "customer_fax2"});
		list.add(new String[]{"Website1", "text", "customer_website1"});
		list.add(new String[]{"Website2", "text", "customer_website2"});
		list.add(new String[]{"Address", "text", "CONCAT(customer_address, ', ', city_name, ' - ', customer_pin, ', ', state_name, ', ', country_name, '.')"});
		list.add(new String[]{"Yahoo", "text", "contact_yahoo"});
		list.add(new String[]{"MSN", "text", "contact_msn"});
		list.add(new String[]{"AOL", "text", "contact_aol"});
		list.add(new String[]{"Skype", "text", "contact_skype"});
		list.add(new String[]{"Landmark", "text", "contact_landmark"});
		list.add(new String[]{"DOB", "date", "contact_dob"});
		list.add(new String[]{"Anniversary", "date", "contact_anniversary"});
		list.add(new String[]{"Active", "boolean", "contact_active"});
		list.add(new String[]{"Notes", "text", "customer_notes"});
		list.add(new String[]{"Entry By", "text", "contact_entry_id in (select emp_id from compdb.axela_emp where emp_name"});
		list.add(new String[]{"Entry Date", "date", "contact_entry_date"});
		list.add(new String[]{"Modified By", "text", "contact_modified_id in (select emp_id from compdb.axela_emp where emp_name"});
		list.add(new String[]{"Modified Date", "date", "contact_modified_date"});
		list.add(new String[]{"Custom Fields", "text", "contact_id IN (SELECT cftrans_row_id FROM compdb.axela_cf_trans WHERE cftrans_submodule_id = 2 and cftrans_value"});
		smartarr = list.toArray(new String[list.size()][2]);
		// smartarr = new String[list.size()][2];
		// for (int i = 0; i < list.size(); i++) {
		// for (int j = 0; j < 2; j++) {
		// SOP("new_arr==" + list.get(i)[j]);
		// smartarr[i][j] = list.get(i)[j];
		// }
		// }
		list.clear();
	}
}
