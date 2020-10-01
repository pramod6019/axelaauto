package axela.sales;
//Saiman 11th Feb 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Enquiry_List extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../sales/index.jsp\">Sales</a>"
			+ " &gt; <a href=\"enquiry.jsp\">Enquiry</a>"
			+ " &gt; <a href=\"enquiry-list.jsp?all=yes\">List Enquiry</a><b>:</b>";
	public String LinkExportPage = "enquiry-export.jsp?smart=yes";
	public String LinkAddPage = "<a href=\"enquiry-quickadd.jsp\">Add New Enquiry ...</a>";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String priority = "";
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "", ExeAccess = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String enquiry_id = "0";
	public String enquiry_customer_id = "0", enquiry_contact_id = "0";
	public String advhtml = "";
	public String enquirybranch_id = "0";
	public String executive_id = "0";
	public String enquirypriority_id = "0";
	// public String enquirybuyertype_id = "0";
	public String soe_id = "0";
	public String sob_id = "0";
	public String campaign_id = "0";
	public String comp_module_preowned = "0";
	public String config_sales_soe = "0";
	public String config_sales_sob = "0";
	public String config_sales_campaign = "0";
	public String config_sales_enquiry_refno = "0";
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";
	public String[] enquiry_enquirytype_ids;
	public String[] model_ids;
	public String[] status_ids;

	// public Customer_Tags_Check tagcheck = new Customer_Tags_Check();
	public String tags = "";

	public String enquiry_customer_tags_id = "0";

	public String[] stage_ids;
	public String[] preownedmanuf_ids;
	public String newcar = "";
	public String preownedcar = "";
	public String emp_all_exe = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Enquiry ID", "numeric", "enquiry_id"},
			{"Enquiry No.", "text", "CONCAT('ENQ', branch_code, enquiry_no)"},
			{"DMS No.", "text", "enquiry_dmsno"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			// {"Brand ID", "numeric", "branch_brand_id"},
			{"Brand", "text", "branch_brand_id IN (SELECT brand_id FROM axela_brand WHERE brand_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"New Car", "boolean", "IF(enquiry_enquirytype_id=1,1,0)"},
			{"Pre Owned", "boolean", "IF(enquiry_enquirytype_id=2,1,0)"},
			{"Title", "text", "enquiry_title"},
			{"Descripition", "text", "enquiry_desc"},
			{"Date", "date", "enquiry_date"},
			{"Closing Date", "date", "enquiry_close_date"},
			{"Follow-up Time", "date", "enquiry_id IN (SELECT followup_enquiry_id FROM compdb.axela_sales_enquiry_followup WHERE followup_followup_time"},
			{"Follow-up Type ID", "numeric", "enquiry_id IN (SELECT followup_enquiry_id FROM compdb.axela_sales_enquiry_followup WHERE followup_followuptype_id"},
			{"Value", "numeric", "enquiry_value"},
			{"Model", "text", "enquiry_model_id IN (SELECT model_id FROM compdb.axela_inventory_item_model WHERE model_name"},
			{"Variant", "text", "item_name"},
			{"Pre Owned Model", "text", "enquiry_preownedvariant_id in (SELECT variant_id FROM axela_preowned_variant "
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id WHERE preownedmodel_name"},
			{"Pre Owned Variant", "text", "enquiry_preownedvariant_id IN (SELECT variant_id FROM axela_preowned_variant WHERE variant_name"},
			{"AV Presentation", "boolean", "enquiry_avpresent"},
			{"Manager Assistance", "boolean", "enquiry_manager_assist"},
			{"Stage", "text", "stage_name"},
			{"Status", "text", "status_name"},
			{"Status Date", "date", "enquiry_status_date"},
			{"Status Description", "text", "enquiry_status_desc"},
			{"Priority", "text", "enquiry_priorityenquiry_id IN (SELECT priorityenquiry_id FROM compdb.axela_sales_enquiry_priority WHERE priorityenquiry_name"},
			{"Buyer Type", "text", "enquiry_buyertype_id IN (SELECT buyertype_id FROM compdb.axela_sales_enquiry_add_buyertype WHERE buyertype_name"},
			{"SOE", "text", "soe_name"},
			{"SOB", "text", "sob_name"},
			{"Campaign", "text", "campaign_name"},
			{"QCS No.", "text", "enquiry_qcsno"},
			{"DMS", "boolean", "IF(enquiry_dms = 1, 1, 0)"},
			{"Executive", "text", "CONCAT(emp.emp_name, emp.emp_ref_no)"},
			{"Notes", "text", "enquiry_notes"},
			{"Entry By", "text", "enquiry_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "enquiry_entry_date"},
			{"Modified By", "text", "enquiry_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "enquiry_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_enquiry_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				// SOP("emp_id-----8888-------" + emp_id);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_all_exe = CNumeric(GetSession("emp_all_exe", request));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				priority = CNumeric(PadQuotes(request.getParameter("priority")));

				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				enquiry_customer_id = CNumeric(PadQuotes(request.getParameter("enquiry_customer_id")));
				enquiry_contact_id = CNumeric(PadQuotes(request.getParameter("contact_id")));
				campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
				orderby = PadQuotes(request.getParameter("orderby"));
				ordertype = PadQuotes(request.getParameter("ordertype"));
				msg = PadQuotes(request.getParameter("msg"));

				PopulateConfigDetails();
				advhtml = BuildAdvHtml(request, response);
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND enquiry_id = 0";
				} else if ("yes".equals(all)) // for all enquiry to b displayed
				{
					msg = "Results for all Enquiry!";
					StrSearch += " AND enquiry_id > 0"
							+ " AND enquiry_status_id = 1 ";
				} else if ("recent".equals(all)) // for recent enquiry to b
				// displayed
				{
					msg = "Results for Recent Enquiry!";
					StrSearch += " AND enquiry_id > 0 ";
				} else if (!enquiry_id.equals("0")) {
					msg += "<br/>Results for Enquiry ID = " + enquiry_id + "!";
					StrSearch += " AND enquiry_id = " + enquiry_id + "";
				} else if (!enquiry_customer_id.equals("0")) {
					msg += "<br/>Results for Customer ID = " + enquiry_customer_id + "!";
					StrSearch = StrSearch + " AND customer_id = " + enquiry_customer_id + "";
				} else if (!enquiry_contact_id.equals("0")) {
					msg += "<br/>Results for Contact ID = " + enquiry_contact_id + "!";
					StrSearch = StrSearch + " AND contact_id = " + enquiry_contact_id + "";
				} else if (!campaign_id.equals("0")) {
					msg += "<br/>Results for Campaign ID = " + campaign_id + "!";
					StrSearch += " AND enquiry_campaign_id = " + campaign_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch += SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Results for all Enquiry!";
						// msg = "Enter search text!";
						// StrSearch += " AND enquiry_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart) && !priority.equals("0")) {
					msg += "<br>Results of Search!";
					StrSearch += " AND enquiry_id > 0"
							+ " AND enquiry_status_id = 1"
							+ " AND enquiry_priorityenquiry_id = " + priority;
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("enquirystrsql", request).equals("")) {
						StrSearch = GetSession("enquirystrsql", request);
					}
				}
				if (!StrSearch.equals("")) {
					SetSession("enquirystrsql", StrSearch, request);
				}
				StrSearch += BranchAccess + ExeAccess.replace("emp_id", "enquiry_emp_id");

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
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		int TotalRecords;
		String PageURL;
		StringBuilder Str = new StringBuilder();
		StringBuilder customer_info = new StringBuilder();
		String update_info = "";
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT enquiry_id,"
						+ " CONCAT('ENQ', branch_code, enquiry_no) AS enquiry_no, enquiry_title,"
						+ " enquiry_dmsno, enquiry_qcsno, enquiry_date, enquiry_close_date, enquiry_lead_id, enquiry_item_id,"
						+ " customer_id, customer_name, contact_id, contact_mobile1, contact_phone1, contact_phone2, contact_mobile2, contact_email2,"
						+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, "
						+ " COALESCE ( CONCAT( contact_fname, ' ', contact_lname ), '' ) AS contactflname,"
						+ " contact_email1,"
						+ " COALESCE(soe_name, '') AS soe_name,"
						+ " COALESCE(sob_name, '') AS sob_name,"
						+ " COALESCE(enquiry_model_id, 0) as enquiry_model_id, enquiry_status_id, enquiry_desc, status_name,"
						+ " COALESCE(campaign_id, '') AS campaign_id,"
						+ " COALESCE(campaign_name, '') AS campaign_name,"
						+ " emp.emp_id as emp_id, CONCAT(emp.emp_name, ' (', emp.emp_ref_no, ')') AS emp_name,"
						+ " COALESCE(item_name, '') item_name, stage_name, enquiry_enquirytype_id,  enquirytype_name, "
						+ " COALESCE((SELECT CONCAT(carmanuf_name,' - ', preownedmodel_name,' - ',variant_name)"
						+ " FROM axela_preowned_variant"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id "
						+ " where variant_id = enquiry_preownedvariant_id),'') preownedmodel,"
						+ " COALESCE(vehtype_name,'') AS vehtype_name,"
						+ " enquiry_priorityenquiry_id, branch_id, branch_code, "
						+ " branch_brand_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
						+ " COALESCE((select so_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_enquiry_id = enquiry_id limit 1),'') AS so_id,"
						+ " REPLACE(COALESCE ( ("
						+ " SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
						+ " FROM " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_tag_trans ON tagtrans_tag_id = tag_id"
						+ " WHERE tagtrans_customer_id = customer_id ), '' ),',','') AS tag,"
						+ " COALESCE(priorityenquiry_color, '') AS bgcolor";

				CountSql = "SELECT COUNT(DISTINCT(enquiry_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = enquiry_emp_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_type ON enquirytype_id = enquiry_enquirytype_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
						+ " LEFT JOIN axela_veh_type ON vehtype_id = model_vehtype_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = enquiry_sob_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = enquiry_campaign_id"
						+ "	LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id";
				if (preownedmanuf_ids != null && preownedcar.equals("1")) {
					SqlJoin += " INNER JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id";
					SqlJoin += " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id";
				}
				SqlJoin += " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;
				QueryString = QueryString.replaceAll("&orderby=" + orderby, "");
				QueryString = QueryString.replaceAll("&ordertype=" + ordertype, "");
				ordernavi = "enquiry-list.jsp?" + QueryString;

				if (ordertype.equals("asc")) {
					ordertype = "desc";
				} else {
					ordertype = "asc";
				}

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY enquiry_id";
					if (orderby.equals("")) {
						StrSql += " ORDER BY enquiry_id DESC";
					} else {
						StrSql += " ORDER BY " + orderby + " " + ordertype + " ";
					}
					CountSql += StrSearch;
					SOP("StrSearch==" + StrSearch);

				}
				if (all.equals("recent")) {
					StrSql += " LIMIT " + recperpage + "";
					crs = processQuery(StrSql, 0);
					crs.last();
					TotalRecords = crs.getRow();
					crs.beforeFirst();
				} else {
					// StrSql += " LIMIT 1000";
					TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				}
				// SOPInfo("CountSql==123==enquiry===list====" + CountSql);
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit i.e. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Enquiry(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "enquiry-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_sales_enquiry\\b",
								"FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN (SELECT enquiry_id"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " WHERE 1 = 1 "
										// + "" + BranchAccess + ExeAccess
										+ " GROUP BY enquiry_id "
										+ " ORDER BY enquiry_id desc) AS myresults using (enquiry_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable ";

						if (orderby.equals("")) {
							StrSql = StrSql + " ORDER BY enquiry_id desc ";
						} else {
							StrSql = StrSql + " ORDER BY " + orderby + " " + ordertype + " ";
						}
					}
					if (!all.equals("recent")) {
						StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
						crs = processQuery(StrSql, 0);
					}

					SOP("all-----------" + StrSql);

					int count = StartRec - 1;
					String bgcolor = "";
					Str.append("<div class=\"table-hover\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">");
					Str.append("<thead><tr>\n");
					// Str.append("<tr align=\"center\">\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th>").append(GridLink("ID", "enquiry_id", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone\">").append(GridLink("No.", "enquiry_no", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone\">").append(GridLink("Enquiry", "enquiry_desc", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone\">").append(GridLink("Date", "enquiry_date", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone\">").append(GridLink("Customer", "customer_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Variant", "item_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Stage", "stage_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Status", "status_name", ordernavi, ordertype)).append("</th>\n");
					if (config_sales_soe.equals("1")) {
						Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("SOE", "soe_name", ordernavi, ordertype)).append("</th>\n");
					}

					if (config_sales_sob.equals("1")) {
						Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("SOB", "sob_name", ordernavi, ordertype)).append("</th>\n");
					}

					if (config_sales_campaign.equals("1")) {
						Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Campaign", "campaign_name", ordernavi, ordertype)).append("</th>\n");
					}
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Sales Consultant", "emp_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Branch", "branch_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						if (crs.getString("enquiry_priorityenquiry_id").equals("1")) {
							bgcolor = "red";
						} else if (crs.getString("enquiry_priorityenquiry_id").equals("2")) {
							bgcolor = "blue";
						} else if (crs.getString("enquiry_priorityenquiry_id").equals("3")) {
							bgcolor = "yellow";
						}
						bgcolor = crs.getString("bgcolor");
						count++;

						Str.append("<tr >");
						Str.append("<td bgcolor=" + bgcolor + ">").append(count).append("</td>\n");
						Str.append("<td >");
						Str.append("<a href=\"javascript:remote=window.open('enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("','enquirydash','');remote.focus();\">");
						Str.append(crs.getString("enquiry_id")).append("</a></td>\n");
						Str.append("<td>");
						if (crs.getString("enquiry_enquirytype_id").equals("1")) {
							Str.append("New ");
						} else if (crs.getString("enquiry_enquirytype_id").equals("2")) {
							Str.append("Pre-Owned ");
						}
						Str.append(crs.getString("vehtype_name") + "<br/>" + crs.getString("enquiry_no"));
						if (!crs.getString("enquiry_dmsno").equals("")) {
							Str.append("<br>DMS No.: " + crs.getString("enquiry_dmsno"));
						}
						Str.append("</td>\n");
						Str.append("<td>");
						Str.append("<a href=\"javascript:remote=window.open('enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("','enquirydash','');remote.focus();\">");
						Str.append(crs.getString("enquiry_title")).append("</a>");
						if (!crs.getString("enquiry_desc").equals("")) {
							Str.append("<br>").append(crs.getString("enquiry_desc"));
						}

						if (config_sales_enquiry_refno.equals("1")) {
							if (!crs.getString("enquiry_qcsno").equals("")) {
								Str.append("<br>QCS No.: ").append(crs.getString("enquiry_qcsno"));
							}
						}

						if (!crs.getString("enquiry_lead_id").equals("0")) {
							Str.append("<br><a href=\"lead-list.jsp?lead_id=").append(crs.getString("enquiry_lead_id")).append("\">Lead ID: ").append(crs.getString("enquiry_lead_id")).append("</a>");
						}
						Str.append("</td>\n");
						Str.append("<td>");
						Str.append("<div onMouseOver=\"populatefollowup(").append(crs.getString("enquiry_id")).append(")\">");
						Str.append(strToShortDate(crs.getString("enquiry_date"))).append(" - ");
						Str.append(strToShortDate(crs.getString("enquiry_close_date")));
						Str.append("<br><a href=\"enquiry-dash.jsp?enquiry_id=").append(crs.getString("enquiry_id")).append("#tabs-2\">").append("Follow-up=>").append("</a></div>");
						Str.append("<div id=\"followup_").append(crs.getString("enquiry_id")).append("\">").append("</div>");
						Str.append("</td>\n");

						Str.append("<td >");

						// Customer Info
						if (!crs.getString("contact_mobile1").equals("")) {
							customer_info.append(crs.getString("contact_mobile1"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							customer_info.append("<br/>" + crs.getString("contact_mobile2"));
						}
						if (!crs.getString("contact_email1").equals("")) {
							customer_info.append("<br/>" + crs.getString("contact_email1"));
						}
						if (!crs.getString("contact_email2").equals("")) {
							customer_info.append("<br/>" + crs.getString("contact_email2"));
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

						Str.append("<br/>" + CustomerContactDetailsPopup(crs.getString("contact_id"), crs.getString("contactname"), customer_info.toString(), "contact"));

						customer_info.setLength(0);

						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("enquiry_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("enquiry_id"), "M"));
						}
						if (!crs.getString("contact_phone1").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_phone1"), crs.getString("enquiry_id"), "T"));
						}
						if (!crs.getString("contact_phone2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_phone2"), crs.getString("enquiry_id"), "T"));
						}

						// Populating Tags in Enquiry list
						Str.append("<br><br>");

						// Tags Starts
						String Tag = crs.getString("tag");
						Tag = ReplaceStr(Tag, "StartColor", "<label class='btn-xs btn-arrow-left' style='border: 1px solid aliceblue;top:-16px; background:");
						Tag = ReplaceStr(Tag, "EndColor", " ; color:white'>&nbsp");
						Tag = ReplaceStr(Tag, "StartName", "");
						Tag = ReplaceStr(Tag, "EndName", "</label>&nbsp&nbsp&nbsp");
						Str.append(Tag);
						// Tags End

						Str.append("</td>\n");
						Str.append("<td >").append(crs.getString("item_name"));
						Str.append("<br>").append(crs.getString("preownedmodel")).append("</td>\n");
						Str.append("<td >").append(crs.getString("stage_name")).append("</td>\n");
						Str.append("<td >").append(crs.getString("status_name")).append("</td>\n");

						if (config_sales_soe.equals("1")) {
							Str.append("<td >").append(crs.getString("soe_name")).append("</td>\n");
						}

						if (config_sales_sob.equals("1")) {
							Str.append("<td >").append(crs.getString("sob_name")).append("</td>\n");
						}

						if (config_sales_campaign.equals("1")) {
							Str.append("<td >");
							Str.append("<a href=\"campaign-list.jsp?campaign_id=").append(crs.getString("campaign_id")).append("\">");
							Str.append(crs.getString("campaign_name")).append("</a></td>\n");
						}
						Str.append("<td >");
						Str.append(ExeDetailsPopover(crs.getInt("emp_id"), crs.getString("emp_name"), ""));
						Str.append("</td>\n");
						Str.append("<td >");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append("\">");
						Str.append(crs.getString("branchname")).append("</a></td\n>");
						Str.append("<td nowrap>");

						update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=enquiry-update.jsp?update=yes"
								+ "&enquiry_id=" + crs.getString("enquiry_id") + ">Update Enquiry</a></li>";

						if (crs.getString("branch_brand_id").equals("1") || crs.getString("branch_brand_id").equals("2") || crs.getString("branch_brand_id").equals("153")) {
							update_info += "<li role=presentation><a target=_blank href=enquiry-trackingcard-maruti.jsp?enquiry_id=" + crs.getString("enquiry_id") + "&brand_id="
									+ crs.getString("branch_brand_id") + " >" + "Print Tracking Card" + "</a></li>";

						} else if (crs.getString("branch_brand_id").equals("6")) {
							update_info += "<li role=presentation><a target=_blank href=enquiry-trackingcard-hyundai.jsp?dr_report=trackingcard-hyundai" +
									"&enquiry_id=" + crs.getString("enquiry_id") + "&brand_id=" + crs.getString("branch_brand_id") +
									"&dr_format=pdf  >" + "Print Tracking Card" + "</a></li>";

						} else if (crs.getString("branch_brand_id").equals("7")) {
							update_info += "<li role=presentation><a target=_blank href=enquiry-trackingcard-ford.jsp?dr_report=trackingcard-ford" +
									"&enquiry_id=" + crs.getString("enquiry_id") + "&brand_id=" + crs.getString("branch_brand_id") +
									"&dr_format=pdf  >" + "Print Tracking Card" + "</a></li>";
						}
						else if (crs.getString("branch_brand_id").equals("55")) {
							update_info += "<li role=presentation><a target=_blank href=enquiry-trackingcard-mb.jsp?dr_report=trackingcard-mb" +
									"&enquiry_id=" + crs.getString("enquiry_id") + "&brand_id=" + crs.getString("branch_brand_id") +
									"&dr_format=pdf  >" + "Print Tracking Card" + "</a></li>";
						} else if (crs.getString("branch_brand_id").equals("151")) {
							update_info += "<li role=presentation><a target=_blank href=enquiry-trackingcard-triumph.jsp?dr_report=trackingcard-triumph" +
									"&enquiry_id=" + crs.getString("enquiry_id") + "&brand_id=" + crs.getString("branch_brand_id") +
									"&dr_format=pdf  >" + "Print Tracking Card" + "</a></li>";
						} else if (crs.getString("branch_brand_id").equals("11")) {
							update_info += "<li role=presentation><a target=_blank href=enquiry-trackingcard-skoda.jsp?dr_report=enquiry-trackingcard-skoda" +
									"&enquiry_id=" + crs.getString("enquiry_id") + "&brand_id=" + crs.getString("branch_brand_id") +
									"&dr_format=pdf  >" + "Print Tracking Card" + "</a></li>";
						}
						if (crs.getString("enquiry_enquirytype_id").equals("1")) {
							update_info += "<li role=presentation><a href=testdrive-update.jsp?add=yes&enquiry_id=" +
									crs.getString("enquiry_id") + ">" + "Add Test Drive" +
									"</a></li>";
						} else {
							update_info += "<li role=presentation><a href=../preowned/preowned-testdrive-update.jsp?add=yes&enquiry_id=" +
									crs.getString("enquiry_id") + ">" + "Add Pre Owned Test Drive" +
									"</a></li>";
						}
						// if (emp_id.equals("1")) {
						if (crs.getString("enquiry_status_id").equals("1")) {
							update_info += "<li role=presentation><a href=veh-quote-add.jsp?enquiry_id=" +
									crs.getString("enquiry_id") + ">" + "Add Quote" +
									"</a></li>";
						}

						update_info += "<li role=presentation><a href=veh-quote-list.jsp?enquiry_id=" +
								crs.getString("enquiry_id") + ">" + "List Quotes" +
								"</a></li>";

						// }

						update_info += "<li role=presentation><a href=../service/ticket-add.jsp?add=yes&contact_id=" +
								crs.getString("contact_id") + ">" + "Add Ticket" +
								"</a></li>";
						if (!crs.getString("contact_email1").equals("")) {
							update_info += "<li role=presentation><a href=enquiry-brochure-email.jsp?enquiry_id=" +
									crs.getString("enquiry_id") + ">" + "Email Brochure" +
									"</a></li>";
						}
						update_info += "</ul></div></center></div>";
						Str.append(update_info);
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
					// SOP("Table ====" + Str);
				} else {
					RecCountDisplay = "<br><br><font color=\"red\">No Enquiry found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";

			}

		}
		return Str.toString();
	}
	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob,"
				+ " config_sales_campaign, config_sales_enquiry_refno,"
				+ " comp_module_preowned"
				+ " FROM " + compdb(comp_id) + "axela_comp, "
				+ compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id = " + emp_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			while (crs.next()) {
				comp_module_preowned = crs.getString("comp_module_preowned");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String BuildAdvHtml(HttpServletRequest request, HttpServletResponse response) {
		enquirybranch_id = CNumeric(PadQuotes(request.getParameter("dr_enquirybranch")));
		executive_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		enquirypriority_id = CNumeric(PadQuotes(request.getParameter("dr_enquirypriority")));
		enquiry_customer_tags_id = CNumeric(PadQuotes(request.getParameter("dr_enquiry_customer_tags_id")));
		// enquirybuyertype_id = CNumeric(PadQuotes(request.getParameter("dr_enquirybuyertype")));
		// SOP("enquiry_customer_tags_id----" + enquiry_customer_tags_id);
		soe_id = CNumeric(PadQuotes(request.getParameter("dr_soe")));
		sob_id = CNumeric(PadQuotes(request.getParameter("dr_sob")));
		// //////For enquiry Type///////////
		newcar = CheckBoxValue(PadQuotes(request.getParameter("chk_newcar")));
		preownedcar = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedcar")));
		String i = "0";
		if (!(newcar.equals("1") && preownedcar.equals("1"))) {
			if (newcar.equals("1")) {
				i = 1 + "";
				StrSearch = StrSearch + " AND enquiry_enquirytype_id in (" + i + ")";
			}
			if (preownedcar.equals("1")) {
				i = 2 + "";
				StrSearch = StrSearch + " AND enquiry_enquirytype_id in (" + i + ")";
			}
		} else if (newcar.equals("1") && preownedcar.equals("1")) {
			i = 1 + "," + 2 + "";
			StrSearch = StrSearch + " AND enquiry_enquirytype_id in (" + i + ")";
		}
		// /////////////////////
		if (request.getParameterValues("chk_enquiry_status") != null) {
			status_ids = request.getParameterValues("chk_enquiry_status");
		}

		if (request.getParameterValues("chk_enquiry_model") != null) {
			model_ids = request.getParameterValues("chk_enquiry_model");
		}

		if (request.getParameterValues("chk_enquiry_preownedmanuf") != null) {
			preownedmanuf_ids = request.getParameterValues("chk_enquiry_preownedmanuf");
		}

		if (request.getParameterValues("chk_enquiry_stage") != null) {
			stage_ids = request.getParameterValues("chk_enquiry_stage");
		}

		if (!enquirybranch_id.equals("0")) {
			StrSearch += " AND enquiry_branch_id = " + enquirybranch_id + "";
		}

		if (!executive_id.equals("0")) {
			StrSearch += " AND enquiry_emp_id = " + executive_id + "";
		}

		if (!enquirypriority_id.equals("0")) {
			StrSearch += " AND enquiry_priorityenquiry_id = " + enquirypriority_id + "";
		}

		// if (!enquirybuyertype_id.equals("0")) {
		// StrSearch += " AND enquiry_buyertype_id = " + enquirybuyertype_id +
		// "";
		// }

		if (!soe_id.equals("0")) {
			StrSearch += " AND enquiry_soe_id = " + soe_id + "";
		}

		if (!sob_id.equals("0")) {
			StrSearch += " AND enquiry_sob_id = " + sob_id + "";
		}

		if (stage_ids != null) {
			StrSearch += " AND enquiry_stage_id IN (" + RetrunSelectArrVal(request, "chk_enquiry_stage") + ")";
		}

		if (model_ids != null) {
			StrSearch += " AND enquiry_model_id IN (" + RetrunSelectArrVal(request, "chk_enquiry_model") + ")";
		}

		if (preownedmanuf_ids != null && preownedcar.equals("1")) {
			StrSearch = StrSearch + " AND preownedmodel_carmanuf_id in (" + RetrunSelectArrVal(request, "chk_enquiry_preownedmanuf") + ")";
		}
		if (status_ids != null) {
			StrSearch += " AND enquiry_status_id IN (" + RetrunSelectArrVal(request, "chk_enquiry_status") + ")";
		}

		if (!enquiry_customer_tags_id.equals("0")) {
			StrSearch += " AND enquiry_customer_id IN (SELECT tagtrans_customer_id FROM " + compdb(comp_id) + "axela_customer_tag_trans WHERE tagtrans_tag_id = " + enquiry_customer_tags_id + " )";
		}
		StringBuilder Str = new StringBuilder();
		// Str.append("<div>\n");
		// Str.append("\n");
		// Str.append("\n");
		// Str.append("");
		// Str.append("<div class=\"col-md-3 col-xs-12\" style=\"\">Branch: <select name=\"dr_enquirybranch\" class=\"form-control\" id=\"dr_enquirybranch\">");
		// Str.append(PopulateEnquiryBranch()).append("</select></div>");
		// Str.append("<div class=\"col-md-3 col-xs-12\" style=\"\">&nbsp;&nbsp;Sales Consultant: <select name=\"dr_executive\" class=\"form-control\" id=\"dr_executive\">");
		// Str.append(PopulateExecutive()).append("</select></div>");
		// Str.append("<div class=\"col-md-3 col-xs-12\" style=\"\">&nbsp;&nbsp; Priority: <select name=\"dr_enquirypriority\" class=\"form-control\" id=\"dr_enquirypriority\">");
		// Str.append(PopulateEnquiryPriority()).append("</select></div>").append("");
		// // Str.append("<td align=center>");
		// Str.append("");
		// // Str.append(PopulateenquiryType());
		// Str.append("<div><br>&nbsp;&nbsp;<input type=checkbox name=chk_newcar id=chk_newcar ").append(PopulateCheck(newcar));
		// Str.append(" onClick=\"javascript:DisplayModel();\">New Car").append("");
		//
		// if (comp_module_preowned.equals("1")) {
		// Str.append("&nbsp;&nbsp;<input type=checkbox name=chk_preownedcar id=chk_preownedcar ").append(PopulateCheck(preownedcar));
		// Str.append(" onClick=\"javascript:DisplayManuf();\">Pre Owned Car</div><br>");
		// }
		// // Str.append("\n");
		// // Str.append("");
		// // Str.append("</thead>\n");
		// // Str.append("<tbody>\n");
		//
		// if (config_sales_soe.equals("1") || config_sales_sob.equals("1")) {
		// // Str.append("<tr>\n");
		// // Str.append("<td nowrap>");
		// if (config_sales_soe.equals("1")) {
		// Str.append("<div class=\"container-fluid\"><div class=\"col-md-3\"></div><div class=\"col-md-3 col-xs-12\" style=\"\">SOE: <select name=\"dr_soe\" class=\"form-control\" id=\"dr_soe\">");
		// Str.append(PopulateSOE()).append("</select></div>");
		// }
		// if (config_sales_sob.equals("1")) {
		// Str.append("<div class=\"col-md-3 col-xs-12\" style=\"\">&nbsp;&nbsp;SOB: <select name=\"dr_sob\" class=\"form-control\" id=\"dr_sob\">");
		// Str.append(PopulateSOB()).append("</select></div>");
		// }
		// // Str.append("</td>\n");
		// // Str.append("</tr>\n");
		// }
		// // Str.append("<tr align=\"center\">\n");
		// // Str.append("<td align=\"center\" nowrap>");
		// //
		// Str.append("&nbsp;&nbsp;Buyer Type: <select name=\"dr_enquirybuyertype\" class=\"selectbox\" id=\"dr_enquirypriority\">");
		// // Str.append(PopulateEnquiryBuyerType());
		// // Str.append("</select>\n");
		// // Str.append("<tr>\n");
		// // Str.append("<td align=\"center\">");
		// Str.append(PopulateStage());
		// // Str.append("</td>\n");
		// // Str.append("</tr>\n");
		// Str.append("<div id=\"preownedmodel\">");
		// // Str.append("<div>");
		// Str.append(PopulateModel());
		// // Str.append("</div>\n");
		// Str.append("</div>\n");
		// if (comp_module_preowned.equals("1")) {
		// Str.append("<div id=\"preownedmanuf\">");
		// // Str.append("<td align=center>");
		// Str.append(PopulatePreownedManuf());
		// // Str.append("</td>\n");
		// Str.append("</div>");
		// }
		// // Str.append("<tr>");
		// // Str.append("<tr>\n");
		// Str.append("<div>");
		// Str.append(PopulateStatus());
		// Str.append("</div>\n");
		// // Str.append("</tr>\n");
		// // Str.append("</tbody>\n");
		// // Str.append("</table>\n");
		// Str.append("</div>\n");

		Str.append("<table>\n");
		Str.append("<tr align=\"\">\n");
		Str.append("<td align=\"\">\n");
		Str.append("<div class=\"container-fluid\"><div class=\"form-element3\"><label>Branch: </label>");
		Str.append("<div ><select name=\"dr_enquirybranch\" class=\"form-control\" id=\"dr_enquirybranch\" onchange=\"PopulateExecutive();\">");
		Str.append(PopulateBranch(enquirybranch_id, "", "1,2", "", request));
		Str.append("</select></div></div>\n");
		Str.append("<div class=\"form-element3\"><label>Sales Consultant: </label>");
		Str.append("<div><span id='hint_exe'>");
		Str.append(PopulateExecutive("", comp_id));
		Str.append("</span></div></div>\n");
		Str.append("<div class=\"form-element3\"><label>Priority: </label>");
		Str.append("<div><select name=\"dr_enquirypriority\" class=\"form-control\" id=\"dr_enquirypriority\">");
		Str.append(PopulateEnquiryPriority());
		Str.append("</select></div></div>\n");
		// Str.append("<td align=center>");
		// Str.append("</br>");
		// Str.append(PopulateenquiryType());

		Str.append("<div class=\"form-element3\"><label>Tags: </label>");
		Str.append(PopulateEnquiryTags());
		Str.append("</div></div>");
		Str.append("</td>\n");

		Str.append("</tr>");

		Str.append("<tr align=\"center\">\n");
		Str.append("<td align=\"center\" nowrap><div class=\"container-fluid\" style=\"margin-top:10px;\">");
		if (config_sales_soe.equals("1") || config_sales_sob.equals("1")) {

			if (config_sales_soe.equals("1")) {
				Str.append("<div class=\"col-md-4 col-xs-12\"><label class=\"control-label col-md-4 col-xs-1\">SOE:</label>");
				Str.append("<div class=\"col-md-8 col-xs-12\"><select name=\"dr_soe\" class=\"form-control\" id=\"dr_soe\">");
				Str.append(PopulateSOE()).append("</select></div></div>\n");
			}
			if (config_sales_sob.equals("1")) {
				Str.append("<div class=\"col-md-4 col-xs-12\"><label class=\"control-label col-md-4 col-xs-1\">SOB:</label>");
				Str.append("<div class=\"col-md-8 col-xs-12\"><select name=\"dr_sob\" class=\"form-control\" id=\"dr_sob\">");
				Str.append(PopulateSOB()).append("</select></div></div>\n");
			}

			Str.append("</div></td>\n");
			Str.append("</tr>\n");
		}
		// Str.append("<tr align=\"center\">\n");
		// Str.append("<td align=\"center\" nowrap>");
		// Str.append("&nbsp;&nbsp;Buyer Type: <select name=\"dr_enquirybuyertype\" class=\"selectbox\" id=\"dr_enquirypriority\">");
		// Str.append(PopulateEnquiryBuyerType());
		// Str.append("</select>\n");
		Str.append("<tr><td><center>\n");
		Str.append("<br><div class=\"container-fluid\" style=\"margin-right:10px;\"><input type=checkbox name=chk_newcar id=chk_newcar ").append(PopulateCheck(newcar));
		Str.append(" onClick=\"javascript:DisplayModel();\">&nbsp;New Car");

		if (comp_module_preowned.equals("1")) {
			Str.append("&nbsp;&nbsp;<input type=checkbox name=chk_preownedcar id=chk_preownedcar ").append(PopulateCheck(preownedcar));
			Str.append(" onClick=\"javascript:DisplayManuf();\">&nbsp;Pre Owned Car");
		}

		Str.append("</center></td></tr><tr>\n");
		Str.append("<td><center>");
		Str.append(PopulateStage());
		Str.append("</center></td>\n");
		Str.append("</tr>\n");
		Str.append("<tr id=\"preownedmodel\">");
		Str.append("<td><center>");
		Str.append(PopulateModel());
		Str.append("</center></td>\n");
		Str.append("</tr>\n");
		if (comp_module_preowned.equals("1")) {
			Str.append("<tr id=\"preownedmanuf\">");
			Str.append("<td><center>");
			Str.append(PopulatePreownedManuf());
			Str.append("</center></td>\n");
			Str.append("</tr>");
		}
		Str.append("<tr>");
		Str.append("<tr>\n");
		Str.append("<td><center>");
		Str.append(PopulateStatus());
		Str.append("</center></td>\n");
		Str.append("</tr>\n");
		Str.append("</table>\n");
		return Str.toString();
	}
	// public String PopulateEnquiryBranch() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=\"0\" class=\"form-control\">Select</option>\n");
	// try {
	// StrSql = "SELECT branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
	// + " FROM " + compdb(comp_id) + "axela_branch"
	// + " WHERE branch_active = 1 AND branch_branchtype_id IN (1,2)" + BranchAccess + ""
	// + " GROUP BY branch_id"
	// + " ORDER BY branch_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("branch_id"));
	// Str.append(StrSelectdrop(crs.getString("branch_id"), enquirybranch_id));
	// Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String PopulateExecutive(String branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<select name=\"dr_executive\" class=\"form-control\" id=\"dr_executive\">");
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT emp_name, emp_ref_no, emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ ExeAccess + " ";
			if (!branch_id.equals("")) {
				StrSql += " AND emp_branch_id = " + branch_id;
			}
			StrSql += " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("PopulateExecutive----" + StrSql);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), executive_id));
				Str.append(">").append(crs.getString("emp_name"));
				Str.append(" (").append(crs.getString("emp_ref_no")).append(")</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePreownedManuf() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT carmanuf_id, carmanuf_name"
				+ " FROM axela_preowned_manuf"
				+ " WHERE 1=1"
				+ " ORDER BY carmanuf_name";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			Str.append(" ");
			while (crs.next()) {
				Str.append("<div id=\"listcheck\" style=\"margin-right:10px;\"><input type=checkbox name=chk_enquiry_preownedmanuf value=").append(crs.getString("carmanuf_id")).append(" ");
				Str.append(ArrSelectCheck(crs.getInt("carmanuf_id"), preownedmanuf_ids)).append("");
				Str.append(">&nbsp;<label>").append(crs.getString("carmanuf_name")).append("</label>&nbsp;&nbsp;&nbsp;</div>");
			}
			Str.append(" ");
			crs.close();
			if (!Str.toString().equals("")) {
				// Str.append("|&nbsp;");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEnquiryPriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\" class=\"form-control\"> Select </option>\n");
		try {
			StrSql = "SELECT priorityenquiry_id, priorityenquiry_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
					+ " GROUP BY priorityenquiry_id"
					+ " ORDER BY priorityenquiry_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityenquiry_id"));
				Str.append(StrSelectdrop(crs.getString("priorityenquiry_id"), enquirypriority_id));
				Str.append(">").append(crs.getString("priorityenquiry_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateEnquiryTags() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT tag_id, tag_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_tag"
					+ " GROUP BY tag_id"
					+ " ORDER BY tag_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_enquiry_customer_tags_id\" class=\"form-control\" id=\"dr_enquiry_customer_tags_id\">\n");
			Str.append("<option value=\"0\" class=\"form-control\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tag_id"));
				Str.append(StrSelectdrop(crs.getString("tag_id"), enquiry_customer_tags_id));
				Str.append(">").append(crs.getString("tag_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	// public String PopulateEnquiryBuyerType() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT buyertype_id, buyertype_name"
	// + " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype" +
	// " WHERE 1=1"
	// + " ORDER BY buyertype_name";
	// // SOP("SqlStr=="+SqlStr);
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<option value=0>Select</option>");
	// while (crs.next()) {
	// Str.append("<option value=")
	// .append(crs.getString("buyertype_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("buyertype_id"),
	// enquirybuyertype_id));
	// Str.append(">").append(crs.getString("buyertype_name"))
	// .append("</option>\n");
	// }
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in "
	// + new Exception().getStackTrace()[0].getMethodName() + ": "
	// + ex);
	// return "";
	// }
	// }

	public String PopulateSOE() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\" class=\"form-control\"> Select </option>\n");
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id"));
				Str.append(StrSelectdrop(crs.getString("soe_id"), soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSOB() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\" class=\"form-control\"> Select </option>\n");
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id"));
				Str.append(StrSelectdrop(crs.getString("sob_id"), sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option>\n");
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
		StrSql = "SELECT stage_id, stage_name"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage"
				+ " ORDER BY stage_rank";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			Str.append("<div class=\"container-fluid\">");
			while (crs.next()) {
				Str.append("<div id=\"listcheck\"><input type=\"checkbox\" name=\"chk_enquiry_stage\" value=").append(crs.getString("stage_id"));
				Str.append(ArrSelectCheck(crs.getInt("stage_id"), stage_ids));
				Str.append(">&nbsp;").append(crs.getString("stage_name")).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>");
			}
			Str.append("</div>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		String principal = "";
		StrSql = "SELECT DISTINCT model_id, model_name, brand_name"
				+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
				+ " INNER JOIN axela_brand ON brand_id = model_brand_id "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
				+ " WHERE 1=1"
				+ BranchAccess
				+ " AND model_sales = 1"
				+ " AND model_active = 1"
				+ " GROUP BY model_id "
				+ " ORDER BY brand_name, model_name";
		// SOP("strsql=====" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			Str.append("<div class=\"container-fluid\">");
			while (crs.next()) {
				if (principal.equals(crs.getString("brand_name"))) {
					Str.append("<div id=\"listcheck\">&nbsp;&nbsp;<input type=\"checkbox\" name=\"chk_enquiry_model\" value=").append(crs.getString("model_id"));
					Str.append(ArrSelectCheck(crs.getInt("model_id"), model_ids)).append("");
					Str.append(">&nbsp;").append(crs.getString("model_name")).append("&nbsp;</div>");
				} else {
					if (!principal.equals(""))
						Str.append("");
					Str.append("<div id=\"listcheck\"><input type=\"checkbox\" name=\"chk_enquiry_model\" value=").append(crs.getString("model_id"));
					Str.append(ArrSelectCheck(crs.getInt("model_id"), model_ids)).append("");
					Str.append(">&nbsp;").append(crs.getString("model_name")).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>");
					principal = crs.getString("brand_name");
				}

			}
			Str.append("</div>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		String ids = "";

		StrSql = "SELECT status_id, status_name"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status"
				+ " ORDER BY status_id";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (all.equals("yes")) {
				while (crs.next()) {
					if (crs.getInt("status_id") == 1) {
						ids = ids + " " + crs.getInt("status_id");
					}
				}
				status_ids = ids.trim().split(" ");
			}
			crs.beforeFirst();
			Str.append("<div class=\"container-fluid\">");
			while (crs.next()) {
				Str.append("<div id=\"listcheck\"><input type=\"checkbox\" name=\"chk_enquiry_status\" value=").append(crs.getString("status_id"));
				Str.append(ArrSelectCheck(crs.getInt("status_id"), status_ids)).append("");
				Str.append(">&nbsp;").append(crs.getString("status_name")).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>");
			}
			Str.append("<center>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
