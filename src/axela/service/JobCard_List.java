package axela.service;
//GURUMURTHY TS, JAN 15 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class JobCard_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../service/index.jsp\">Service</a>"
			+ " &gt; <a href=\"../service/jobcard.jsp\">Job Card</a>"
			+ " &gt; <a href=\"jobcard-list.jsp?all=yes\">List Job Cards</a><b>:</b><br />";
	public String LinkExportPage = "jobcard-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=\"jobcard-veh-search.jsp\">Add New Job Card...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String modal = "";
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
	public int PageCurrent = 0;
	public String advSearch = "";
	public String advhtml = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String jc_id = "0";
	public String jc_customer_id = "0", jc_contact_id = "0";
	public String jc_no = "", jc_veh_id = "0";
	public String jc_emp_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";
	public String executive_id = "0";
	public String crmexecutive_id = "0";
	public String priority_id = "0";
	public String category_id = "0";
	public String type_id = "0";
	public String preownedmodel_id = "0";
	public String modalstatus = "add";
	private String[] stage_ids;
	public String jc_critical = "0";
	public String ids2 = "";
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Job Card ID", "numeric", "jc_id"},
			{"Job Card No.", "text", "CONCAT('JC', branch_code, jc_no)"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Time In", "date", "jc_time_in"},
			{"Promised Time", "date", "jc_time_promised"},
			{"Ready Time", "date", "jc_time_ready"},
			{"Time Out", "date", "jc_time_out"},
			{"Posted Date", "date", "jc_time_posted"},
			{"Vehicle ID", "numeric", "veh_id"},
			{"Veh. Reg. No.", "text", "veh_reg_no"},
			{"JC Reg. No.", "text", "jc_reg_no"},
			{"Chassis No.", "text", "veh_chassis_no"},
			{"Vehicle IACS", "boolean", "veh_iacs"},
			{"Title", "text", "jc_title"},
			{"Billing Address", "text", "CONCAT(jc_bill_address, jc_bill_city, jc_bill_pin, jc_bill_state)"},
			{"Delivery Address", "text", "CONCAT(jc_del_address, jc_del_city, jc_del_pin, jc_del_state)"},
			// {"Description", "text", "jc_desc"},
			{"RO No.", "text", "jc_ro_no"},
			{"Cash Bill. No.", "text", "jc_bill_cash_no"},
			{"Cash Bill Date", "date", "jc_bill_cash_date"},
			{"Insurance Bill. No.", "text", "jc_bill_insur_no"},
			{"Insurance Bill Date", "date", "jc_bill_insur_date"},
			{"Stage", "text", "jcstage_name"},
			{"Priority", "text", "priorityjc_name"},
			{"Type", "text", "jctype_name"},
			{"Category", "text", "jccat_name"},
			{"Classified", "boolean", "IF(veh_classified = 1, 1, 0)"},
			{"Authorized By", "text", "jc_auth_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"}, // subquery
			{"Authorized Date", "date", "jc_auth_date"},
			{"Executive", "text", "CONCAT(emp_name, emp_ref_no)"},
			{"Active", "boolean", "jc_active"},
			{"Warranty", "boolean", "jc_warranty"},
			{"Notes", "text", "jc_notes"},
			{"Entry By", "text", "jc_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Entry Date", "date", "jc_entry_date"},
			{"Modified By", "text", "jc_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_name"},
			{"Modified Date", "date", "jc_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "jc_branch_id");
				emp_id = CNumeric(GetSession("emp_id", request));
				ExeAccess = GetSession("ExeAccess", request);
				// SOP("ExeAccess===" + ExeAccess);
				ExeAccess = ExeAccess.replace("emp_id", "jc_emp_id");
				modal = PadQuotes(request.getParameter("modal"));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				jc_customer_id = CNumeric(PadQuotes(request.getParameter("jc_customer_id")));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				jc_veh_id = CNumeric(PadQuotes(request.getParameter("jc_veh_id")));
				jc_emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				all = PadQuotes(request.getParameter("all"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				orderby = PadQuotes(request.getParameter("orderby"));
				ordertype = PadQuotes(request.getParameter("ordertype"));

				// orderby = "jc_time_in";
				// ordertype = "DESC";

				advhtml = BuildAdvHtml(request, response);
				// SOP("Current date==" + ToLongDate(kknow()));
				// SOP("60 days ago==" +
				// ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()),
				// -60, 0, 0, 0)));

				if (!ids2.equals("")) {
					StrSearch += " AND jc_jcstage_id IN (" + ids2.substring(0, ids2.length() - 2) + ")";
				}
				// SOP("jc_critical===11=" + jc_critical);
				if (!jc_critical.equals("0")) {
					StrSearch += " AND jc_critical = '1'";
				}
				if (msg.toLowerCase().contains("delete")) {
					StrSearch += " AND jc_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br>Results for all Job Card!";
					// StrSearch += " AND jc_time_in >= " + ConvertLongDateToStr(AddDayMonthYearStr(ToLongDate(kknow()), -60, 0, 0, 0)) + "";
				} else if (!jc_veh_id.equals("0")) {
					msg += "<br>Results for Vehicle ID = " + jc_veh_id + "!";
					StrSearch += " AND jc_veh_id = " + jc_veh_id + "";
				} else if (!jc_id.equals("0")) {
					msg += "<br>Results for Job Card ID = " + jc_id + "!";
					StrSearch += " AND jc_id = " + jc_id + "";
				} else if (!jc_customer_id.equals("0")) {
					msg += "<br>Results for Customer ID = " + jc_customer_id + "!";
					StrSearch += " AND customer_id = " + jc_customer_id + "";
				} else if (!jc_emp_id.equals("0")) {
					msg += "<br>Results for Executive ID = " + jc_emp_id + "!";
					StrSearch += " AND jc_emp_id = " + jc_emp_id + ""
							+ " AND jc_time_ready = ''";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch += SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND jc_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) {
					msg += "<br>Results of Search!";
					if (!GetSession("jcstrsql", request).equals("")) {
						StrSearch = GetSession("jcstrsql", request);
					}
				}
				StrSearch += BranchAccess + ExeAccess;

				SetSession("jcstrsql", StrSearch, request);
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
		CachedRowSet crs;
		int TotalRecords;
		String PageURL;
		StringBuilder Str = new StringBuilder();
		String update_info = "";
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec;
		StringBuilder customer_info = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT COALESCE(jc_id, 0) AS jc_id, jc_ref_customer_id,"
						+ " COALESCE(jc_reg_no, '') AS jc_reg_no,"
						+ " COALESCE(jc_emp_id, 0) AS jc_emp_id,"
						+ " COALESCE(jc_location_id, 0) AS jc_location_id,"
						+ " COALESCE(jc_branch_id, 0) AS jc_branch_id, COALESCE(CONCAT('JC', branch_code, jc_no), '') AS jc_no,"
						+ " COALESCE(jc_contact_id, 0) AS jc_contact_id, COALESCE(jc_title, '') AS jc_title, COALESCE(jc_cust_voice, '') AS jc_cust_voice,"
						+ " COALESCE(jc_time_promised, '') AS jc_time_promised, COALESCE(jc_time_ready, '') AS jc_time_ready,"
						+ " COALESCE(jc_netamt, '') AS jc_netamt, COALESCE(jc_totaltax, '') AS jc_totaltax, COALESCE(jc_grandtotal, '') AS jc_grandtotal,"
						+ " COALESCE(jc_ro_no, '') AS jc_ro_no,"
						+ " COALESCE(jc_bill_cash_no, '') AS jc_bill_cash_no,"
						+ " COALESCE(jc_bill_insur_no, '') AS jc_bill_insur_no,"
						+ " COALESCE(jc_auth, '') AS jc_auth, "
						+ " COALESCE(jc_active, '') AS jc_active,"
						+ " COALESCE(jc_time_out, '') AS jc_time_out, COALESCE(jcstage_name, '') AS jcstage_name,"
						+ " COALESCE(variant_id, '0') AS variant_id,"
						+ " IF(veh_classified != 0, 'Classified', '') AS veh_classified,"
						+ " COALESCE(jc_time_in, '') AS jc_time_in, COALESCE(jc_time_posted, '') AS jc_time_posted,"
						+ " COALESCE (branch_rateclass_id, 0) AS branch_rateclass_id,"
						// + " COALESCE(IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name), '') AS variant_name,"
						+ " CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) AS variant_name,"
						+ " COALESCE(veh_id, 0) AS veh_id, COALESCE(veh_reg_no, '') AS veh_reg_no,"
						+ " COALESCE(priorityjc_name, '') AS priorityjc_name, COALESCE(jccat_name, '') AS jccat_name,"
						+ " COALESCE(jctype_name, '') AS jctype_name, COALESCE(jc_stage_trigger, '') AS jc_stage_trigger,"
						+ " COALESCE(jc_priority_trigger, '') AS jc_priority_trigger, COALESCE(customer_id, '0') AS customer_id,"
						+ " COALESCE(customer_name, '') AS customer_name,"
						+ " COALESCE(contact_id, 0) AS contact_id, COALESCE(veh_iacs, '') AS veh_iacs,"
						+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
						+ " COALESCE(contact_mobile1, '') AS contact_mobile1, COALESCE(contact_mobile2, '') AS contact_mobile2,"
						+ " COALESCE(contact_email1, '') AS contact_email1, COALESCE(contact_email2, '') AS contact_email2,"
						+ " COALESCE(branch_id, '') AS branch_id, COALESCE(CONCAT(branch_name, ' (', branch_code, ')'),'') AS branch_name, branch_brand_id,"
						+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')'), '') AS emp_name, COALESCE(emp_id, 0) AS emp_id";

				CountSql = "SELECT COUNT(DISTINCT(jc_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
						+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
						+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " LEFT JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " WHERE 1 = 1";
				// SOP("2---------------------------" + SqlJoin);
				if (!jc_veh_id.equals("0")) {
					SqlJoin += " AND jc_veh_id = " + jc_veh_id + "";
				}

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				QueryString = QueryString.replaceAll("&orderby=" + orderby, "");
				QueryString = QueryString.replaceAll("&ordertype=" + ordertype, "");
				ordernavi = "jobcard-list.jsp?" + QueryString;
				if (ordertype.equals("ASC")) {
					ordertype = "DESC";
				} else {
					ordertype = "ASC";
				}

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY jc_id";
					if (orderby.equals("")) {
						StrSql += " ORDER BY jc_time_in DESC";
					} else {
						StrSql += " ORDER BY " + orderby + " " + ordertype + " ";
					}
				}
				CountSql += StrSearch;
				// SOP("jc list====" + StrSql);

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					if (!msg.equals("")) {
						StartRec = ((PageCurrent - 1) * recperpage) + 1;
						EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
						// if limit ie. 10 > totalrecord
						if (EndRec > TotalRecords) {
							EndRec = TotalRecords;
						}

						RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Job Card(s)";
						if (QueryString.contains("PageCurrent") == true) {
							QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
						}

						PageURL = "jobcard-list.jsp?" + QueryString + "&PageCurrent=";

						PageCount = (TotalRecords / recperpage);
						if ((TotalRecords % recperpage) > 0) {
							PageCount++;
						}
						// display on jsp page
						PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					}

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) +
								"axela_service_jc\\b",
								"FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN (SELECT jc_id FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE 1 = 1" + StrSearch + ""
										+ " GROUP BY jc_id"
										+ " ORDER BY jc_id DESC ) AS myresults USING (jc_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable";

						if (orderby.equals("")) {
							StrSql += " ORDER BY jc_time_in DESC";
						} else {
							StrSql += " ORDER BY " + orderby + " " + ordertype + " ";
						}
					}
					if (!all.equals("recent")) {
						// / SOP("all-----------" + all);
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						crs = processQuery(StrSql, 0);
					}

					// SOP("StrSql==jc list from mb ==" + StrSql);
					// if (emp_id.equals("1")) {
					// SOPInfo("StrSql==jc list from mb ==" + StrSql);
					// }

					crs = processQuery(StrSql, 0);

					int count = StartRec - 1;

					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-reponsive table-hover \" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >").append(GridLink("ID", "jc_id", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("No.", "jc_no", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th>").append(GridLink("Job Card", "jc_title", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th style=\"width:200px;\" data-hide=\"phone\">").append(GridLink("Customer", "customer_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone\">").append(GridLink("Time", "jc_time_in", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Voice", "jc_cust_voice", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Variant", "variant_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Reg. No.", "jc_reg_no", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Stage", "jcstage_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Priority", "priorityjc_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Type", "jctype_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Category", "jccat_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Amount", "jc_grandtotal", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Service Advisor", "emp_id", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">").append(GridLink("Branch", "branch_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						// Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("jc_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("jc_id") + ");'");
						// Str.append(" style='height:200px'>\n");
						Str.append("<tr>");
						Str.append("<td>").append(count).append("</td>\n");
						Str.append("<td>");
						Str.append("<a href=\"javascript:remote=window.open('jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("','jcdash','');remote.focus();\">");
						Str.append(crs.getString("jc_id")).append("</a></td>\n");
						Str.append("<td>");
						if (!crs.getString("jc_no").equals("")) {
							Str.append(crs.getString("jc_no"));
						}
						if (!crs.getString("jc_no").equals("") && !crs.getString("jc_ro_no").equals("")) {
							Str.append("<br>Ref No: ").append(crs.getString("jc_ro_no"));
						} else if (crs.getString("jc_no").equals("") && !crs.getString("jc_ro_no").equals("")) {
							Str.append("Ref No: ").append(crs.getString("jc_ro_no"));
						}
						Str.append("</td>\n<td>");
						Str.append("<a href=\"javascript:remote=window.open('jobcard-dash.jsp?jc_id=").append(crs.getString("jc_id")).append("','ticketdash','');remote.focus();\">");
						Str.append(crs.getString("jc_title")).append("</a>").append("<br>").append(crs.getString("jc_reg_no"));
						if (crs.getString("jc_active").equals("0")) {
							Str.append("<br><font color=\"red\"><b>[Inactive]</b></font>");
						}

						if (crs.getString("jc_auth").equals("1")) {
							Str.append("<br><font color=\"red\">[Authorized]</font>");
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
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("jc_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("jc_id"), "M"));
						}

						// if (!crs.getString("contact_email1").equals("")) {
						// Str.append("<br><span class='customer_info customer_" + crs.getString("jc_id") + "'  style='display: none;'><a href=\"mailto:")
						// .append(crs.getString("contact_email1")).append("\">");
						// Str.append(crs.getString("contact_email1")).append("</a></span>");
						// }
						//
						// if (!crs.getString("contact_email2").equals("")) {
						// Str.append("<br><span class='customer_info customer_" + crs.getString("jc_id") + "'  style='display: none;'><a href=\"mailto:")
						// .append(crs.getString("contact_email2")).append("\">");
						// Str.append(crs.getString("contact_email2")).append("</a></span>");
						// }
						// SOP("time in---" + crs.getString("jc_time_in"));

						Str.append("</td>");
						Str.append("<td nowrap");
						if (!crs.getString("jc_time_in").equals("0")) {
							Str.append(">Time In: ").append(strToLongDate(crs.getString("jc_time_in")));
						}
						Str.append("<br>Promised: ").append(strToLongDate(crs.getString("jc_time_promised")));

						if (!crs.getString("jc_time_ready").equals("")) {
							Str.append("<br>Ready: ").append(strToLongDate(crs.getString("jc_time_ready")));
						}

						if (!crs.getString("jc_time_out").equals("")) {
							Str.append("<br>Time Out: ").append(strToLongDate(crs.getString("jc_time_out")));
						}

						if (!crs.getString("jc_time_posted").equals("")) {
							Str.append("<br>Posted Date: ").append(strToLongDate(crs.getString("jc_time_posted")));
						}
						Str.append("</td>\n<td>").append(crs.getString("jc_cust_voice"));
						Str.append("</td>\n<td>");
						Str.append("<a href=\"../preowned/managepreownedvariant.jsp?variant_id=").append(crs.getString("variant_id")).append("\">");
						// Str.append("<a href=\"../inventory/inventory-item-list.jsp?variant_id=").append(crs.getString("variant_id")).append("\">");
						Str.append(crs.getString("variant_name")).append("</a>");
						Str.append("</td>\n<td valign=\"top\" align=\"left\" nowrap>");
						if (crs.getString("veh_id").equals("0")) {
							Str.append("<a href=\"../service/vehicle-update.jsp?add=yes&veh_reg_no=").append(crs.getString("jc_reg_no")).append("\">Add Vehicle</a>");
						} else {
							Str.append("<a href=\"../service/vehicle-list.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
							Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
							if (!crs.getString("veh_classified").equals("")) {
								Str.append("<br/><font color=\"red\"><b>[Classified]</b></font>");
							}
							if (crs.getString("veh_iacs").equals("1")) {
								Str.append("<br><font color=\"red\"><b>IACS</b></font>");
							}
						}
						Str.append("</td>\n<td nowrap>").append(crs.getString("jcstage_name"));
						if (crs.getString("jc_stage_trigger").equals("5")) {
							Str.append("<br><font color=\"red\">Level: ").append(crs.getString("jc_stage_trigger")).append("</font>");
						} else {
							Str.append("<br>Level: ").append(crs.getString("jc_stage_trigger"));
						}
						Str.append("</td>\n<td nowrap>").append(crs.getString("priorityjc_name"));
						if (crs.getString("jc_priority_trigger").equals("5")) {
							Str.append("<br><font color=\"red\">Level: ").append(crs.getString("jc_priority_trigger")).append("</font>");
						} else {
							Str.append("<br>Level: ").append(crs.getString("jc_priority_trigger"));
						}
						Str.append("</td>\n<td>").append(crs.getString("jctype_name"));
						Str.append("</td>\n<td>").append(crs.getString("jccat_name"));
						Str.append("</td>\n<td nowrap>Net Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_netamt"))));
						if (!crs.getString("jc_totaltax").equals("0")) {
							Str.append("<br> Tax: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_totaltax"))));
						}
						Str.append("<br><b>Total: ").append(IndDecimalFormat(df.format(crs.getDouble("jc_grandtotal")))).append("</b>");
						Str.append("<br/><br/></td>\n<td>");
						Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>");
						Str.append("<td><a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a>");
						Str.append("</td>\n<td nowrap>");
						update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul style='margin-top: -5px;' class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"jobcard-update.jsp?update=yes&jc_id="
								+ crs.getString("jc_id") + "\">Update Job Card</a></li>"
								+ "<li role=presentation><a href=\"jobcard-authorize.jsp?jc_id=" + crs.getString("jc_id") + " \">Authorize</a></li>"
								+ "<li role=presentation><a href=\"../service/call-update.jsp?add=yes&veh_id=" + crs.getString("veh_id") + "\">Add Call</a></li>"
								+ "<li role=presentation><a href=\"../service/ticket-add.jsp?add=yes&jc_id=" + crs.getString("jc_id") + "\">Add Ticket</a></li>"
								+ "<li role=presentation><a href=\"../accounting/so-update2.jsp?add=yes"
								// + "&checkinvoice=yes"// This is to check duplicate invoice
								+ "&vouchertype_id=6&voucherclass_id=6"
								+ "&voucher_jc_id=" + crs.getString("jc_id") + "&so_branch_id=" + crs.getString("jc_branch_id")
								+ "&dr_voucher_rateclass_id=" + crs.getString("branch_rateclass_id") + "&jc_emp_id=" + crs.getString("jc_emp_id")
								+ "&vouchertrans_customer_id=" + crs.getString("customer_id") + "&span_cont_id=" + crs.getString("jc_contact_id")
								+ "&jc_location_id=" + crs.getString("jc_location_id") + "&voucher_ref_customer_id=" + crs.getString("jc_ref_customer_id")
								+ "\">Add Invoice</a></li>"
								+ "<li role=presentation><a href=\"../accounting/voucher-list.jsp?vouchertype_id=6&customer_id=" + crs.getString("customer_id")
								+ "&voucher_jc_id=" + crs.getString("jc_id") + "\">List Invoice</a></li>"

								// if (!crs.getString("jc_time_out").equals("")) {
								// if (Long.parseLong(crs.getString("jc_time_out")) <=
								// Long.parseLong(ToLongDate(kknow()))) {
								// Str.append("<br/><a href=\"../service/jobcard-cust-feedback.jsp?add=yes&jc_id=").append(crs.getString("jc_id")).append("\">Customer Feedback</a>");
								// }
								// }

								+ "<li role=presentation><a href=\"jobcard-print-pdf.jsp?jc_id=" + crs.getString("jc_id")
								+ "&target=" + Math.random() + "&dr_report=jobcard-print" + "\" target=_blank>Print Job Card</a></li>";
						if (crs.getString("branch_brand_id").equals("102")) {
							update_info += "<li role=presentation><a href=\"jobcard-salesinvoice-print-yamaha.jsp?jc_id=" + crs.getString("jc_id")
									+ "&target=" + Math.random() + "&dr_report=jobcard-salesinvoice-print-yamaha" + "\" target=_blank>Print Job Card Invoice</a></li>";
						}
						update_info += "<li role=presentation><a href=\"jobcard-email.jsp?jc_id=" + crs.getString("jc_id") + "\">Email Job Card</a></li>"
								+ "<li role=presentation><a href=\"gate-pass-print-pdf.jsp?jc_id=" + crs.getString("jc_id")
								+ "&target=" + Math.random() + "\" target=_blank>Print Gate Pass</a></li>";
						update_info += "</ul></div></center></div>";
						Str.append(update_info);
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();

				} else {
					Str.append("<br><br><center><font color=\"red\"><b>No Job Card(s) found!</b></font></center>");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
	public String BuildAdvHtml(HttpServletRequest request, HttpServletResponse response) {
		executive_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		crmexecutive_id = CNumeric(PadQuotes(request.getParameter("dr_veh_crm_emp_id")));
		preownedmodel_id = CNumeric(PadQuotes(request.getParameter("dr_model")));
		type_id = CNumeric(PadQuotes(request.getParameter("dr_type")));
		category_id = CNumeric(PadQuotes(request.getParameter("dr_category")));
		priority_id = CNumeric(PadQuotes(request.getParameter("dr_priority")));
		jc_critical = CNumeric(PadQuotes(request.getParameter("chk_jc_critical")));

		if (request.getParameterValues("chk_jc_stage") != null) {
			stage_ids = request.getParameterValues("chk_jc_stage");
		}
		if (!jc_critical.equals("0")) {
			StrSearch += " AND jc_critical ='1'";
		}
		if (!executive_id.equals("0")) {
			StrSearch += " AND jc_emp_id = " + executive_id + "";
		}
		if (!crmexecutive_id.equals("0")) {
			StrSearch += " AND veh_crmemp_id = " + crmexecutive_id + "";
		}

		if (!preownedmodel_id.equals("0")) {
			StrSearch += " AND variant_preownedmodel_id = " + preownedmodel_id + "";
		}

		if (!type_id.equals("0")) {
			StrSearch += " AND jc_jctype_id = " + type_id + "";
		}

		if (!category_id.equals("0")) {
			StrSearch += " AND jc_jccat_id = " + category_id + "";
		}

		if (!priority_id.equals("0")) {
			StrSearch += " AND jc_priorityjc_id = " + priority_id + "";
		}

		if (stage_ids != null) {
			StrSearch += " AND jc_jcstage_id IN (" + RetrunSelectArrVal(request, "chk_jc_stage") + ")";
		}

		StringBuilder Str = new StringBuilder();
		Str.append("<div class=\"container-fluid\">");
		Str.append("<div class=\"form-element3\">");
		Str.append("<label>Executive:</label> <select name=\"dr_executive\" class=\"form-control\" id=\"dr_executive\">");
		Str.append(PopulateExecutive());
		Str.append("</select>\n");
		Str.append("</div>\n");
		Str.append("<div class=\"form-element3\">");
		Str.append("<label>Model:</label> <select name=\"dr_model\" class=\"form-control\" id=\"dr_model\">");
		Str.append(PopulateModel());
		Str.append("</select>\n");
		Str.append("</div>\n");
		Str.append("<div class=\"form-element3\">");
		Str.append("<label>Type:</label> <select name=\"dr_type\" class=\"form-control\" id=\"dr_type\">");
		Str.append(PopulateType());
		Str.append("</select>\n");
		Str.append("</div>\n");
		Str.append("<div class=\"form-element3\">");
		Str.append("<label>Category: </label><select name=\"dr_category\" class=\"form-control\" id=\"dr_category\">");
		Str.append(PopulateCategory());
		Str.append("</select>\n");
		Str.append("</div>\n");
		Str.append("<div class=\"form-element3\">");
		Str.append("<label>Priority:</label> <select name=\"dr_priority\" class=\"form-control\" id=\"dr_priority\">");
		Str.append(PopulatePriority());
		Str.append("</select>\n");
		Str.append("</div>");
		Str.append("<div class=\"form-element3\">");
		Str.append("<label>CRE:</label> <select name=\"dr_veh_crm_emp_id\" class=\"form-control\" id=\"dr_veh_crm_emp_id\">");
		Str.append(PopulateCRE());
		Str.append("</select>\n");
		Str.append("</div></div>");
		Str.append("<center>");
		Str.append(PopulateStage());
		Str.append("</center>");
		return Str.toString();
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT emp_name, emp_ref_no, " + compdb(comp_id) + "axela_emp.emp_id AS emp_id, COUNT(jc_id) AS jobcount"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_emp_id = " + compdb(comp_id) + "axela_emp.emp_id"
					+ " WHERE emp_active = 1"
					+ ExeAccess + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), executive_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (");
				Str.append(crs.getString("emp_ref_no")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCRE() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT emp_id, emp_name, emp_ref_no"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE 1=1"
					+ " AND  emp_active = 1"
					+ " AND emp_crm =1"
					+ ExeAccess.replace("jc_emp_id", "emp_id")
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), crmexecutive_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (");
				Str.append(crs.getString("emp_ref_no")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT priorityjc_id, priorityjc_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_priority"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_priorityjc_id = priorityjc_id"
					+ " WHERE 1 = 1"
					+ ExeAccess
					+ " GROUP BY priorityjc_id"
					+ " ORDER BY priorityjc_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityjc_id"));
				Str.append(StrSelectdrop(crs.getString("priorityjc_id"), priority_id));
				Str.append(">").append(crs.getString("priorityjc_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateCategory() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT jccat_id, jccat_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_jccat_id = jccat_id"
					+ " WHERE 1 = 1" + ExeAccess + ""
					+ " GROUP BY jccat_id"
					+ " ORDER BY jccat_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jccat_id"));
				Str.append(StrSelectdrop(crs.getString("jccat_id"), category_id));
				Str.append(">").append(crs.getString("jccat_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_jctype_id = jctype_id"
					+ " WHERE 1 = 1" + ExeAccess + ""
					+ " GROUP BY jctype_id"
					+ " ORDER BY jctype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id"));
				Str.append(StrSelectdrop(crs.getString("jctype_id"), type_id));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name"
					+ " FROM  axelaauto.axela_preowned_model"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_preownedmodel_id = preownedmodel_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_variant_id = variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_veh_id = veh_id"
					+ " WHERE 1 = 1" + ExeAccess + ""
					+ " GROUP BY preownedmodel_id"
					+ " ORDER BY preownedmodel_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id"));
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), preownedmodel_id));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateStage() {
		StringBuilder Str = new StringBuilder();
		String ids = "";
		try {
			StrSql = "SELECT jcstage_id, jcstage_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " ORDER BY jcstage_rank";

			CachedRowSet crs = processQuery(StrSql, 0);

			if (all.equals("yes")) {
				while (crs.next()) {
					if (crs.getInt("jcstage_id") != 6) {
						ids = ids + " " + crs.getInt("jcstage_id");
						ids2 = ids2 + crs.getInt("jcstage_id") + ", ";
					}
				}
				stage_ids = ids.trim().split(" ");
			}

			// SOP("StrSearch=====" + StrSearch);
			// SOP("ids2.substring(0, ids2.length() - 1)=====" + ids2.substring(0, ids2.length() - 2));

			crs.beforeFirst();
			while (crs.next()) {
				Str.append("<input type=\"checkbox\" id=\"chk_jc_stage\" name=\"chk_jc_stage\" value=").append(crs.getString("jcstage_id")).append(" ");
				Str.append(ArrSelectCheck(crs.getInt("jcstage_id"), stage_ids));
				Str.append(">").append(crs.getString("jcstage_name")).append("&nbsp;&nbsp;");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
