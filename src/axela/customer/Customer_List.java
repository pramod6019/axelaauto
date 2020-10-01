package axela.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Customer_List extends Connect {

	public String LinkHeader = "";
	public String LinkExportPage = "customer-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String tag = "";
	public String url_tag = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String msg = "";
	public String BranchAccess = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String all = "";
	public String group = "";
	public String branch_id = "0";
	public String customer_id = "0";
	public String customer_type = "";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String advSearch = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Mobile", "text", "concat(REPLACE(customer_mobile1,'-',''), REPLACE(customer_mobile2,'-',''))"},
			{"Code", "text", "customer_code"},
			{"Phone1", "text", "customer_phone1"},
			{"Phone2", "text", "customer_phone2"},
			{"Phone3", "text", "customer_phone3"},
			{"Phone4", "text", "customer_phone4"},
			{"Fax1", "text", "customer_fax1"},
			{"Fax2", "text", "customer_fax2"},
			{"Email1", "text", "customer_email1"},
			{"Email2", "text", "customer_email2"},
			{"Website1", "text", "customer_website1"},
			{"Website2", "text", "customer_website2"},
			{"Address", "text", "CONCAT(customer_address,', ',city_name,' - ',customer_pin,', ',state_name,'.')"},
			{"Landmark", "text", "customer_landmark"},
			{"Pan Number", "text", "customer_pan_no"},
			{"SOE Name", "text", "customer_soe_id in (select soe_id from compdb.axela_soe where soe_name"},
			{"SOB Name", "text", "customer_sob_id in (select sob_id from compdb.axela_sob where sob_name"},
			{"Customer Since", "date", "customer_since"},
			{"Opening Balance", "numeric", "customer_open_bal"},
			{"Current Balance", "numeric", "customer_curr_bal"},
			{"Executive", "text", "concat(emp_name,emp_ref_no)"},
			{"Active", "boolean", "customer_active"},
			{"Groups", "text", "customer_id in (select trans_customer_id from compdb.axela_customer_group_trans inner join "
					+ "compdb.axela_customer_group on group_id=trans_group_id where group_desc"},
			{"Notes", "text", "customer_notes"},
			{"Entry By", "text", "customer_entry_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Entry Date", "date", "customer_entry_date"},
			{"Modified By", "text", "customer_modified_id in (select emp_id from compdb.axela_emp where emp_name"},
			{"Modified Date", "date", "customer_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_customer_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				all = PadQuotes(request.getParameter("all"));
				url_tag = PadQuotes(request.getParameter("tag"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (url_tag.equals("vendors")) {
					tag = "Supplier";
				} else {
					tag = "Customer";
				}
				if (!group.equals("selectcustomer")) {
					LinkHeader = "<a href=../portal/home.jsp>Home</a>"
							+ " &gt; <a href=customer.jsp>Customers</a>"
							+ " &gt; <a href=../customer/customer-list.jsp?all=yes&tag=" + url_tag + ">List " + tag + "s</a><b>:</b>";
				}

				LinkAddPage = "<center><a href=customer-update.jsp?Add=yes&tag=" + url_tag + ">Add New " + tag + " ... </a></center>";

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND customer_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all " + tag + "s!";
					StrSearch = StrSearch + " AND customer_id > 0";
				} else if (all.equals("recent")) {
					msg = "Recent " + tag + "s!";
					StrSearch = StrSearch + " AND customer_id > 0";
				} else if (!customer_id.equals("0")) {
					msg = msg + "<br>Result for " + tag + " ID: " + customer_id + "!";
					StrSearch = StrSearch + " AND customer_id = " + customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " AND customer_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) // for smart search
				{
					msg += "<br>Results for Search!";
					if (!GetSession("customerstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("customerstrsql", request);
					}
				}
				StrSearch += BranchAccess;
				if (!StrSearch.equals("")) {
					SetSession("customerstrsql", StrSearch, request);
				}
				if (tag.equals("Supplier")) {
					StrSearch += " AND customer_id IN (SELECT voucher_customer_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ " WHERE voucher_vouchertype_id = 12"
							+ " AND voucher_active = 1)";
				}
				StrHTML = Listdata();
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

	public String Listdata() {
		CachedRowSet crs = null;
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		String PageURL = "";
		String address = "";
		StringBuilder Str = new StringBuilder();
		String update_info = "";
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);
				if (!msg.equals("")) {
					StrSql = "SELECT customer_id, customer_name,customer_type, customer_active, customer_open_bal, customer_curr_bal,"
							+ " COALESCE(emp_id, 0) AS emp_id,"
							+ " OCTET_LENGTH(COALESCE (customer_gst_doc, '0')) AS customer_gst_doc,customer_gst_doc_value,"
							+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), '') AS emp_name, customer_email1, customer_email2,"
							+ " customer_phone1, customer_phone2, customer_phone3, customer_phone4, customer_mobile1, customer_mobile2,"
							+ " customer_branch_id, customer_landmark, customer_fax1, customer_fax2, customer_website1, customer_website2,"
							+ " customer_address,"
							+ " COALESCE(customer_type, '') AS customer_type,"
							+ " COALESCE(branch_id, 0) AS branch_id,"
							+ " COALESCE(CONCAT(branch_name, ' (', branch_code, ')'), '') AS branch_name,"
							+ " COALESCE(city_name, '') AS city_name, COALESCE(state_name,'') AS state_name,"
							+ " customer_pin, COUNT(DISTINCT contact_id) AS contactcount";

					SqlJoin = " FROM " + compdb(comp_id) + "axela_customer"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = customer_branch_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_emp on emp_id = customer_emp_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_city on city_id = customer_city_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact on customer_id = contact_customer_id"
							+ " WHERE 1 = 1"
							+ " AND customer_type != 0";

					CountSql = "SELECT COUNT(DISTINCT(customer_id))";

					StrSql = StrSql + SqlJoin;
					CountSql = CountSql + SqlJoin;
					if (!StrSearch.equals("")) {
						StrSql += StrSearch + " GROUP BY customer_id"
								+ " ORDER BY customer_id DESC";
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
					// SOP("CountSql==" + CountSql);
					if (TotalRecords != 0) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						// if limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}
						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " " + tag + "(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}
						PageURL = "customer-list.jsp?" + QueryString + "&PageCurrent=";
						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount = PageCount + 1;
						}
						// display on jsp page
						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

						if (!all.equals("recent")) {
							crs = processQuery(StrSql, 0);
						}
						// SOP("Listdata==========" + StrSql);
						int count = StartRec - 1;
						String active = "";
						Str.append("<div class=\"  table\">\n");
						Str.append("<table class=\"table table-bordered  table-hover\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th data-toggle=\"true\">#</th>\n");
						Str.append("<th >ID</th>\n");
						Str.append("<th>").append(tag).append("s</th>\n");
						Str.append("<th style=\"width:200px;\" data-hide=\"phone\">Contacts</th></style>\n");
						Str.append("<th data-hide=\"phone\">Address</th>\n");
						Str.append("<th data-hide=\"phone\">Type</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Opening Balance</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Current Balance</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Executive</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Branch</th>\n");
						Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
						Str.append("</tr></thead><tbody>\n");

						while (crs.next()) {
							count = count + 1;
							if (crs.getString("customer_active").equals("0")) {
								active = "<br><font color=red> [Inactive] </font>";
							} else {
								active = "";
							}
							if (crs.getString("customer_type").equals("1")) {
								customer_type = "Customer";
							} else if (crs.getString("customer_type").equals("2")) {
								customer_type = "Supplier";
							}
							Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("customer_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("customer_id") + ");'");
							Str.append(" style='height:200px'>\n");
							Str.append("<td>").append(count).append("</td>\n");
							// Str.append("<td nowrap>").append(crs.getString("customer_id")).append("</td>\n");

							Str.append("<td>");
							Str.append("<a href=\"../customer/customer-dash.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
							Str.append(crs.getString("customer_id")).append("</a>").append("</td>\n");

							Str.append("<td>");
							Str.append("<a href=\"../customer/customer-dash.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
							Str.append(crs.getString("customer_name")).append("</a>").append("</td>\n");
							Str.append("<td nowrap>");
							if (!crs.getString("customer_phone1").equals("")) {
								Str.append(SplitPhoneNoSpan(crs.getString("customer_phone1"), 4, "T", crs.getString("customer_id")))
										.append(ClickToCall(crs.getString("customer_phone1"), comp_id)).append("<br>");
							}
							if (!crs.getString("customer_phone2").equals("")) {
								Str.append(SplitPhoneNoSpan(crs.getString("customer_phone2"), 4, "T", crs.getString("customer_id")))
										.append(ClickToCall(crs.getString("customer_phone2"), comp_id)).append("<br>");
							}
							if (!crs.getString("customer_phone3").equals("")) {
								Str.append(SplitPhoneNoSpan(crs.getString("customer_phone3"), 4, "T", crs.getString("customer_id")))
										.append(ClickToCall(crs.getString("customer_phone3"), comp_id)).append("<br>");
							}
							if (!crs.getString("customer_phone4").equals("")) {
								Str.append(SplitPhoneNoSpan(crs.getString("customer_phone4"), 4, "T", crs.getString("customer_id")))
										.append(ClickToCall(crs.getString("customer_phone4"), comp_id)).append("<br>");
							}
							if (!crs.getString("customer_mobile1").equals("")) {
								Str.append(SplitPhoneNoSpan(crs.getString("customer_mobile1"), 5, "M", crs.getString("customer_id")))
										.append(ClickToCall(crs.getString("customer_mobile1"), comp_id)).append("<br>");
							}
							if (!crs.getString("customer_mobile2").equals("")) {
								Str.append(SplitPhoneNoSpan(crs.getString("customer_mobile2"), 5, "M", crs.getString("customer_id")))
										.append(ClickToCall(crs.getString("customer_mobile2"), comp_id)).append("<br>");
							}
							if (!crs.getString("customer_fax1").equals("")) {
								Str.append(crs.getString("customer_fax1")).append("<br>");
							}
							if (!crs.getString("customer_fax2").equals("")) {
								Str.append(crs.getString("customer_fax2")).append("<br>");
							}
							if (!crs.getString("customer_email1").equals("")) {
								Str.append("<span class='customer_info customer_" + crs.getString("customer_id") + "'  style='display: none;'><a href=\"mailto:")
										.append(crs.getString("customer_email1")).append("\">");
								Str.append(crs.getString("customer_email1")).append("</a></span>").append("<br>");
							}
							if (!crs.getString("customer_email2").equals("")) {
								Str.append("<span class='customer_info customer_" + crs.getString("customer_id") + "'  style='display: none;'><a href=\"mailto:")
										.append(crs.getString("customer_email2")).append("\">");
								Str.append(crs.getString("customer_email2")).append("</a></span>").append("<br>");
							}
							if (!crs.getString("customer_website1").equals("")) {
								Str.append("<a href=http://").append(crs.getString("customer_website1")).append(" target=_blank>").append(crs.getString("customer_website1")).append("<br></a>");
							}
							if (!crs.getString("customer_website2").equals("")) {
								Str.append("<a href=http://").append(crs.getString("customer_website2")).append(" target=_blank>").append(crs.getString("customer_website2")).append("</a>");
							}
							Str.append("</td>\n");
							Str.append("<td>");
							address = crs.getString("customer_address");
							if (!address.equals("")) {
								address = crs.getString("customer_address");
								if (!crs.getString("city_name").equals("")) {
									address = address + ", " + crs.getString("city_name");
								}
								address = address + " - " + crs.getString("customer_pin");
								if (!crs.getString("state_name").equals("")) {
									address = address + ", " + crs.getString("state_name") + ".";
								}
								if (!crs.getString("customer_landmark").equals("")) {
									address = address + "<br>Landmark: " + crs.getString("customer_landmark");
								}
							}
							Str.append(address);
							Str.append("</td>\n");
							Str.append("<td>");
							Str.append(customer_type).append("</td>\n");
							Str.append("<td>").append(crs.getString("customer_open_bal")).append("</td>\n");
							Str.append("<td>").append(crs.getString("customer_curr_bal")).append("</td>\n");
							Str.append("<td><a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name"))
									.append("</a></td>\n");
							Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">").append(crs.getString("branch_name"))
									.append("</a><br>");
							if (crs.getInt("customer_gst_doc") > 1) {
								Str.append("<a href=\"../Fetchdocs.do?").append(QueryString).append("&customer_id=").append(crs.getString("customer_id")).append("\">");
								Str.append(crs.getString("customer_gst_doc_value")).append(" (").append(ConvertFileSizeToBytes(crs.getInt("customer_gst_doc"))).append(")</a><br>");
							}
							Str.append("</td>\n");
							Str.append("<td nowrap>");

							update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
									+ "<i class='fa fa-pencil'></i></button>"
									+ "<ul class='dropdown-content dropdown-menu pull-right'>";

							if (group.equals("selectcustomer")) {
								update_info += "<li role=presentation><a href=# onClick=\"javascript:SelectCompanyProperty("
										+ crs.getString("customer_id") + ", '" + PadQuotes(crs.getString("customer_name"))
										+ "', 'prop_client_id', 'Customer', 'span_prop_client_id');\">Select " + tag + "</a></li>";
							} else if (group.equals("prop")) {
								update_info += "<li role=presentation><a href=# onClick=\"javascript:SelectCompanyPropertya("
										+ crs.getString("customer_id") + ", '" + PadQuotes(crs.getString("customer_name"))
										+ "', 'prop_customer_id', 'customer', 'span_prop_customer_id')\">Select Customer for Property</a></li>";
							} else if (group.equals("select_po_customer")) {
								update_info += "<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectPOCustomer(" + crs.getString("customer_id") + ", '"
										+ PadQuotes(crs.getString("customer_name")) + "');\">Select Supplier</a></li>";
							} else if (group.equals("select_pay_customer")) {
								update_info += "<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectPayCustomer("
										+ crs.getString("customer_id") + ",'" + crs.getString("customer_name") + " ("
										+ crs.getString("customer_curr_bal") + ")','hide');\">Select Customer</a></li>";
							} else {
								update_info += "<li role=presentation><a href=\"customer-update.jsp?update=yes&customer_id="
										+ crs.getString("customer_id") + "&tag=" + url_tag + " \">Update " + tag + "</a></li>";
								// if (crs.getString("customer_type").equals("2")) {
								// Str.append("<a href=\"../gst/gst-supplier-update.jsp?update=yes&customer_id=").append(crs.getString("customer_id")).append("&tag=").append("Gst")
								// .append(" \">GST ")
								// .append(tag).append("</a>");
								// } else {
								update_info += "<li role=presentation><a href=\"../customer/gst-document-update.jsp?update=yes&customer_id="
										+ crs.getString("customer_id") + "&tag=" + tag + " \">Update GST Document " + "" + "</a></li>";

								update_info += "<li role=presentation><a href=\"customer-contact-list.jsp?customer_id=" + crs.getString("customer_id")
										+ " \">List Contacts (" + crs.getString("contactcount") + ")</a></li>";
								update_info += "<li role=presentation><a href=\"customer-docs-list.jsp?customer_id="
										+ crs.getString("customer_id") + " \">List Documents</a></li>";

							}
							update_info += "</ul></div></center></div>";
							Str.append(update_info);
							Str.append("</td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody></table>\n");
						crs.close();
					} else {
						RecCountDisplay = "<br><br><font color=red>No " + tag + "(s) found!</font><br><br>";
					}
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
