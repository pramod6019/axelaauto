package axela.preowned;
//Sangita

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;
import cloudify.connect.Smart;

public class Preowned_List extends Connect {
	public String LinkHeader = "";
	public String LinkExportPage = "";
	public String LinkAddPage = "";
	public String LinkPrintPage = "";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
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
	public String preowned_id = "0";
	public String preowned_date = "";
	public String preowned_customer_id = "0";
	public String advhtml = "";
	public String preowned_branch_id = "0";
	public String executive_id = "0";
	public String campaign_id = "0";
	public String preowned_prioritypreowned_id = "0";
	public String soe_id = "0";
	public String sob_id = "0";
	public String config_preowned_soe = "";
	public String config_preowned_sob = "";
	public String config_preowned_campaign = "";
	public String config_preowned_refno = "";
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";

	public String[] model_ids;
	public String[] status_ids;
	public String eval_status;
	public String[] eval_status_id;
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Pre Owned ID", "numeric", "preowned_id"},
			{"Pre Owned No.", "text", "concat('PRE',branch_code,preowned_no)"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "concat(title_desc,' ',contact_fname,' ',contact_lname)"},
			{"Contact Mobile", "text", "concat(REPLACE(contact_mobile1,'-',''),REPLACE(contact_mobile2,'-',''))"},
			{"Contact Email", "text", "concat(contact_email1, contact_email2)"},
			{"Date", "date", "preowned_date"},
			{"Title", "text", "preowned_title"},
			{"Model", "text", "variant_preownedmodel_id in (SELECT preownedmodel_id FROM axela_preowned_model WHERE preownedmodel_name"},
			{"Variant", "text", "variant_name"},
			{"Sub Variant", "text", "preowned_sub_variant"},
			{"Fuel Type", "text", "preowned_fueltype_id in(SELECT fueltype_id FROM compdb.axela_fueltype WHERE fueltype_name"},
			{"Exterior", "text", "preowned_extcolour_id in(SELECT extcolour_id FROM compdb.axela_preowned_extcolour WHERE extcolour_name"},
			{"Interior", "text", "preowned_intcolour_id in(SELECT intcolour_id FROM compdb.axela_preowned_intcolour WHERE intcolour_name"},
			{"Options", "text", "preowned_options"},
			{"Manuf. Year", "numeric", "preowned_manufyear"},
			{"Regd. Year", "numeric", "preowned_regdyear"},
			{"Reg. No.", "text", "preowned_regno"},
			{"Kms", "numeric", "preowned_kms"},
			{"Foreclosure Amt", "numeric", "preowned_fcamt"},
			{"Funding Bank", "text", "preowned_funding_bank"},
			{"Loan No.", "text", "preowned_loan_no"},
			{"Insurance Date", "date", "preowned_insur_date"},
			{"Insurance Type", "text", "preowned_insurance_id in(SELECT insurance_id FROM compdb.axela_preowned_insurance WHERE insurance_name"},
			{"Ownership", "text", "preowned_ownership_id in(SELECT ownership_id FROM compdb.axela_preowned_ownership WHERE ownership_name"},
			{"Invoice Value", "numeric", "preowned_invoicevalue"},
			{"Expected Price", "numeric", "preowned_expectedprice"},
			{"Offered Price", "numeric", "eval_offered_price"},
			{"Quoted Price", "numeric", "preowned_quotedprice"},
			// {"Stage", "text", "preownedstage_name"},
			{"Status", "text", "preownedstatus_name"},
			{"Status Date", "date", "preowned_preownedstatus_date"},
			{"Status Description", "text", "preowned_preownedstatus_desc"},
			{"Pre Owned Priority", "text", "preowned_prioritypreowned_id in (SELECT prioritypreowned_id FROM compdb.axela_preowned_priority WHERE prioritypreowned_name"},
			{"SOE", "text", "soe_name"},
			{"SOB", "text", "sob_name"},
			{"Campaign", "text", "campaign_name"},
			{"Ref. No.", "text", "preowned_refno"},
			// {"Quote", "numeric",
			// "preowned_id in (SELECT quote_enquiry_id FROM " + compdb(comp_id)
			// + "axela_sales_quote WHERE quote_active='1' and quote_id"},
			// {"Sales Order", "numeric", "enquiry_refno"},
			{"Executive", "text", "concat(preowned.emp_name,preowned.emp_ref_no)"},
			{"Enquiry ID", "text", "preowned_enquiry_id IN (SELECT enquiry_id FROM compdb.axela_sales_enquiry WHERE enquiry_id"},
			{"Sales Executive", "text", "preowned_sales_emp_id IN (SELECT sales.emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Notes", "text", "preowned_notes"},
			{"Entry By", "text", "preowned_entry_id in (SELECT preowned.emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "preowned_entry_date"},
			{"Modified By", "text", "preowned_modified_id in (SELECT preowned.emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "preowned_modified_date"}
	};
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			LinkHeader = "<a href=../portal/home.jsp>Home</a>"
					+ " &gt; <a href=../preowned/index.jsp>" + ReturnPreOwnedName(request) + "</a>"
					+ " &gt; <a href=preowned.jsp>" + ReturnPreOwnedName(request) + "</a>"
					+ " &gt; <a href=preowned-list.jsp?all=yes>List " + ReturnPreOwnedName(request) + "</a>:";
			LinkExportPage = "preowned-export.jsp?smart=yes";
			LinkAddPage = "<a href=preowned-quickadd.jsp>Add New " + ReturnPreOwnedName(request) + "...</a>";
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {

				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				preowned_customer_id = CNumeric(PadQuotes(request.getParameter("preowned_customer_id")));
				campaign_id = CNumeric(PadQuotes(request.getParameter("campaign_id")));
				orderby = PadQuotes(request.getParameter("orderby"));
				ordertype = PadQuotes(request.getParameter("ordertype"));
				msg = PadQuotes(request.getParameter("msg"));
				PopulateConfigDetails();
				advhtml = BuildAdvHtml(request, response);

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND preowned_id = 0";
				} else if ("yes".equals(all)) // for all enquirytunity to b
												// displayed
				{
					msg = "Results for all Open " + ReturnPreOwnedName(request);
					StrSearch = StrSearch + " AND preowned_preownedstatus_id = 1 ";
					// StrSearch = StrSearch + " and preowned_id > 0 ";
				} else if ("recent".equals(all)) // for all enquirytunity to b
													// displayed
				{
					msg = "Results for Recent Pre Owned!";
					StrSearch = StrSearch + " and preowned_id > 0 ";
				} else if (!preowned_id.equals("0")) {
					msg = msg + "<br/>Results for Pre-Owned ID = " + preowned_id + "!";
					StrSearch = StrSearch + " AND preowned_id = " + preowned_id;
				} else if (!preowned_customer_id.equals("0")) {
					msg = msg + "<br/>Results for Customer ID = " + preowned_customer_id + "!";
					StrSearch = StrSearch + " and preowned_customer_id = " + preowned_customer_id;
				} else if (!campaign_id.equals("0")) {
					msg = msg + "<br/>Results for Campaign ID = " + campaign_id + "!";
					StrSearch = StrSearch + " AND preowned_campaign_id = " + campaign_id;
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = StrSearch + SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch = StrSearch + " and preowned_id = 0 ";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("preownedstrsql", request).equals("")) {
						StrSearch = GetSession("preownedstrsql", request);
					}
				}
				if (StrSearch.contains("preowned_preowned.emp_id")) {
					StrSearch = StrSearch.replace("preowned_emp_id", "preowned.emp_id");
				}
				if (StrSearch.contains(" emp_id")) {
					StrSearch = StrSearch.replace(" emp_id", " preowned.emp_id");
				}
				StrSearch += BranchAccess.replace("branch_id", "preowned_branch_id")
						+ ExeAccess.replace("emp_id", " preowned_emp_id");
				// SOP("StrSearch-----2------" + StrSearch);
				// if (!StrSearch.equals("")) {
				SetSession("preownedstrsql", StrSearch, request);
				// }
				StrHTML = Listdata(request);
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	public String Listdata(HttpServletRequest request) {

		CachedRowSet crs = null;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		StringBuilder customer_info = new StringBuilder();
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				// to know no of records depending ON search
				StrSql = "SELECT preowned_id, CONCAT('PRE',branch_code,preowned_no) AS preowned_no,"
						+ " preowned_title, preowned_date, preowned_close_date,COALESCE(preowned_enquiry_id, 0) AS preowned_enquiry_id,"
						// / + " preowned_preownedmodel_id, "
						+ " preowned_sub_variant,"
						// +
						// " fueltype_id, fueltype_name, extcolour_id, extcolour_name, intcolour_id, intcolour_name,"
						+ " preowned_options, preowned_manufyear, preowned_regdyear, preowned_regno, preowned_kms,"
						// +
						// " preowned_fcamt, preowned_insur_date, insurance_id, insurance_name, ownership_id, ownership_name,"
						// +
						// " preowned_invoicevalue, preowned_expectedprice, preowned_quotedprice,"
						+ " customer_id, customer_name,"
						+ " contact_id, CONCAT(title_desc,' ',contact_fname,' ', contact_lname) AS contactname,"
						+ " contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
						+ " COALESCE(soe_name,'') AS soe_name, COALESCE(sob_name,'') AS sob_name,"
						+ " COALESCE(campaign_id,'') AS campaign_id, COALESCE(campaign_name,'') AS campaign_name,"
						+ " COALESCE(preowned.emp_id,0) AS preownedempid, CONCAT(preowned.emp_name,' (', preowned.emp_ref_no, ')') AS preownedemp_name, "
						+ " carmanuf_name, preownedmodel_name,"
						+ " variant_name, preownedstatus_name,"
						+ " COALESCE(preowned_expectedprice, '') AS preowned_expectedprice, "
						+ " COALESCE(eval_offered_price, '') AS eval_offered_price, "
						+ " COALESCE(sales.emp_id, 0) AS salesempid, CONCAT(sales.emp_name,' (', sales.emp_ref_no, ')') AS salesemp_name, "
						+ " branch_id, branch_code, CONCAT(branch_name,' (', branch_code, ')') AS branchname,"
						+ " preowned_refno, preowned_desc, "
						+ " COALESCE((SELECT preownedstock_id FROM " + compdb(comp_id) + "axela_preowned_stock WHERE preownedstock_preowned_id=preowned_id limit 1), 0)AS stock_id,"
						+ " COALESCE((SELECT eval_id FROM " + compdb(comp_id) + "axela_preowned_eval WHERE eval_preowned_id=preowned_id limit 1), 0)AS eval_id ";

				CountSql = " SELECT Count(distinct(preowned_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id =preowned_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp preowned ON preowned.emp_id = preowned_emp_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"

						// + " INNER JOIN axela_preowned_extcolour ON extcolour_id = preowned_extcolour_id"
						// + " INNER JOIN axela_preowned_intcolour ON intcolour_id = preowned_intcolour_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = preowned_fueltype_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_preowned_ownership ON ownership_id = preowned_ownership_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_status ON preownedstatus_id = preowned_preownedstatus_id"
						// + " LEFT JOIN " + compdb(comp_id) + "axela_preowned_insurance ON insurance_id = preowned_insurance_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp sales ON sales.emp_id = preowned_sales_emp_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_soe ON soe_id = preowned_soe_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sob ON sob_id = preowned_sob_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = preowned_campaign_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_eval ON eval_preowned_id = preowned_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_followup ON preownedfollowup_preowned_id = preowned_id "
						+ " WHERE 1 = 1 ";

				StrSql = StrSql + SqlJoin;
				CountSql = CountSql + SqlJoin;
				QueryString = QueryString.replaceAll("&orderby=" + orderby, "");
				QueryString = QueryString.replaceAll("&ordertype=" + ordertype, "");
				ordernavi = "preowned-list.jsp?" + QueryString;
				if (ordertype.equals("asc")) {
					ordertype = "desc";
				} else {
					ordertype = "asc";
				}

				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrSearch + " GROUP BY preowned_id";
					if (orderby.equals("")) {
						StrSql = StrSql + " ORDER BY preowned_id desc ";
					} else {
						StrSql = StrSql + " ORDER BY " + orderby + " " + ordertype + " ";
					}
				}
				CountSql = CountSql + StrSearch;
				// SOP("StrSearch=====" + StrSearch);
				// SOP("CountSql------------" + CountSql);

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " " + ReturnPreOwnedName(request) + "(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "preowned-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display ON jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);

					if (all.equals("yes")) {
						StrSql = StrSql.replaceAll("\\bfrom " + compdb(comp_id) + "axela_preowned\\b", "FROM " + compdb(comp_id) + "axela_preowned "
								+ " INNER JOIN (SELECT preowned_id FROM " + compdb(comp_id) + "axela_preowned "
								+ " WHERE 1 = 1 " + StrSearch + ""
								+ " GROUP BY preowned_id ORDER BY preowned_id desc"
								+ " LIMIT " + (StartRec - 1) + ", " + recperpage + ") AS myresults using (preowned_id)");

						StrSql = "SELECT * FROM (" + StrSql + ") AS datatable ";

						if (orderby.equals("")) {
							StrSql = StrSql + " ORDER BY preowned_id desc ";
						} else {
							StrSql = StrSql + " ORDER BY " + orderby + " " + ordertype + " ";
						}
					}
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";

					// SOP("ExeAccess = " + ExeAccess);
					// SOP("StrSql----Listdata-----------" + StrSql);
					crs = processQuery(StrSql, 0);
					int count = StartRec - 1;

					Str.append("<table class=\"table table-bordered table-responsive table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
					Str.append("<th data-toggle=\"true\">").append(GridLink("ID", "preowned_id", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("No.", "preowned_no", ordernavi, ordertype)).append("</th>\n");
					// Str.append("<th>").append(GridLink("Pre Owned", "enquiry_desc", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Date", "preowned_date", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th  data-toggle=\"true\">").append(GridLink("Customer", "customer_name", ordernavi, ordertype)).append("</th></style>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Manufacturer", "carmanuf_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Model", "preownedmodel_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Variant", "variant_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Status", "preownedstatus_name", ordernavi, ordertype)).append("</th>\n");
					if (config_preowned_soe.equals("1")) {
						Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("SOE", "soe_name", ordernavi, ordertype)).append("</th>\n");
					}
					if (config_preowned_sob.equals("1")) {
						Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("SOB", "sob_name", ordernavi, ordertype)).append("</th>\n");
					}
					if (config_preowned_campaign.equals("1")) {
						Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Campaign", "campaign_name", ordernavi, ordertype)).append("</th>\n");
					}
					Str.append("<th data-toggle=\"true\">").append(GridLink("Evaluation Price", "eval_offered_price", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Pre-Owned Consultant", "emp_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">").append(GridLink("Branch", "branch_name", ordernavi, ordertype)).append("</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						// Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("preowned_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("preowned_id") + ");'");
						// Str.append("style='height:200px'>\n");
						Str.append("<tr>");
						Str.append("<td valign=top align=center>").append(count).append("</td>\n");
						Str.append("<td valign=top align=center>").append("<a href=\"javascript:remote=window.open('preowned-dash.jsp?preowned_id=").append(crs.getString("preowned_id"))
								.append("','preowneddash','');remote.focus();\">").append(crs.getString("preowned_id")).append("</a></td>");
						Str.append("<td valign=top align=left>");
						Str.append(crs.getString("preowned_no")).append("<br/><a href=\"javascript:remote=window.open('preowned-dash.jsp?preowned_id=").append(crs.getString("preowned_id"))
								.append("','preowneddash','');remote.focus();\">").append(crs.getString("preowned_title")).
								append("</a>");
						if (!crs.getString("preowned_desc").equals("")) {
							Str.append("<br>").append(crs.getString("preowned_desc"));
						}
						if (config_preowned_refno.equals("1")) {
							if (!crs.getString("preowned_refno").equals("")) {
								Str.append("<br>Ref. No.: ").append(crs.getString("preowned_refno"));
							}
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left><div").append(" onclick=\"populatefollowup(");
						Str.append(crs.getString("preowned_id")).append(")\" onMouseOver=\"populatefollowup(");
						Str.append(crs.getString("preowned_id")).append(")\">");
						Str.append(strToShortDate(crs.getString("preowned_date"))).append(" - ");
						Str.append(strToShortDate(crs.getString("preowned_close_date"))).append("</div>");
						Str.append("<a href=\"javascript:remote=window.open('preowned-dash-followup.jsp?preowned_id=").append(crs.getString("preowned_id"))
								.append("','preowneddash','');remote.focus();\">").append("Follow-up=>").append("</a>");
						Str.append("<div id=\"followup_").append(crs.getString("preowned_id")).append("\">").append("</div>");
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

						Str.append("<br/>" + CustomerContactDetailsPopup(crs.getString("contact_id"), crs.getString("contactname"), customer_info.toString(), "contact"));

						customer_info.setLength(0);

						if (!crs.getString("contact_mobile1").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("preowned_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("preowned_id"), "M"));
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left>").append(crs.getString("carmanuf_name")).append("</td>");
						Str.append("<td valign=top align=left>").append(crs.getString("preownedmodel_name")).append("</td>");
						Str.append("<td valign=top align=left>").append(crs.getString("variant_name"));
						if (!crs.getString("preowned_sub_variant").equals("")) {
							Str.append("<br>").append(crs.getString("preowned_sub_variant"));
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left>").append(crs.getString("preownedstatus_name")).append("</td>");
						//
						if (config_preowned_soe.equals("1")) {
							Str.append("<td valign=top align=left>").append(crs.getString("soe_name")).append("</td>");
						}
						if (config_preowned_sob.equals("1")) {
							Str.append("<td valign=top align=left>").append(crs.getString("sob_name")).append("</td>\n");
						}
						if (config_preowned_campaign.equals("1")) {
							Str.append("<td valign=top align=left><a href=../sales/campaign-list.jsp?campaign_id=").append(crs.getString("campaign_id")).append(">")
									.append(crs.getString("campaign_name")).append("</a></td>");
						}

						Str.append("<td valign=top align=left nowrap>");
						if (!crs.getString("eval_offered_price").equals("")) {
							Str.append("Expected Price: ");
							Str.append(crs.getString("preowned_expectedprice")).append("/-");
							Str.append("<br>").append("Offered Price: ");
							Str.append(crs.getString("eval_offered_price")).append("/-");
						} else {
							Str.append("");
						}
						Str.append("</td>");

						Str.append("<td valign=top align=left>Pre-Owned Consultant: ");
						Str.append(ExeDetailsPopover(crs.getInt("preownedempid"), crs.getString("preownedemp_name"), ""));
						if (!crs.getString("salesempid").equals("0")) {
							Str.append("<br>Sales Consultant: ");
							Str.append(ExeDetailsPopover(crs.getInt("salesempid"), crs.getString("salesemp_name"), ""));
						}
						if (!crs.getString("preowned_enquiry_id").equals("0")) {
							Str.append("<br>Enquiry ID: <a href=\"../sales/enquiry-list.jsp?enquiry_id=").append(crs.getInt("preowned_enquiry_id")).append("\">")
									.append(crs.getString("preowned_enquiry_id"))
									.append("</a>");
						}
						Str.append("</td>");
						Str.append("<td valign=top align=left><a href=\"../portal/branch-summary.jsp?branch_id=");
						Str.append(crs.getInt("branch_id")).append("\">").append(crs.getString("branchname")).append("</a></td>");
						Str.append("<td valign=top align=left nowrap>");

						Str.append(
								"<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>")
								.append("<i class='fa fa-pencil'></i></button>")
								.append("<ul class='dropdown-content dropdown-menu pull-right'>")
								.append("<li role=presentation><a href=\"preowned-update.jsp?update=yes&preowned_id=")
								.append(crs.getString("preowned_id")).append(" \">Update " + ReturnPreOwnedName(request) + "</a></li>");
						// Str.append("<a href=\"preowned-update.jsp?update=yes&preowned_id=");
						// Str.append(crs.getString("preowned_id")).append(" \">Update " + ReturnPreOwnedName(request) + "</a>");
						// SOP("s--"+crs.getString("preowned_id"));

						// SOP("stock--"+crs.getString("preownedstock_preowned_id"));
						if (crs.getString("eval_id").equals("0")) {
							Str.append("<li role=presentation><a href=\"preowned-eval-update.jsp?add=yes&preowned_id=").append(crs.getString("preowned_id")).append(" \">Add Evaluation</a></li>");
						} else {
							Str.append("<li role=presentation><a href=\"preowned-eval-list.jsp?preowned_id=").append(crs.getString("preowned_id")).append(" \">List Evaluation</a></li>");
						}
						if (crs.getString("stock_id").equals("0")) {
							Str.append("<li role=presentation><a href=\"preowned-stock-update.jsp?add=yes&preowned_id=").append(crs.getString("preowned_id")).append(" \">Add Stock</a></li>");
						} else {
							Str.append("<li role=presentation><a href=\"preowned-stock-list.jsp?preowned_id=").append(crs.getString("preowned_id")).append(" \">List Stock</a></li>");
						}
						Str.append("<li role=presentation><a href=\"preowned-testdrive-list.jsp?preowned_id=").append(crs.getString("preowned_id")).append(" \">List Test Drives</a></li>");
						Str.append("</ul></div></center></div>");
						Str.append("</td></tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					crs.close();

				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No " + ReturnPreOwnedName(request) + " found!</font><br><br>";
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
		StrSql = "SELECT config_preowned_soe, config_preowned_sob,"
				+ " config_preowned_campaign, config_preowned_refno"
				+ " FROM " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_config," + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id = " + emp_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_preowned_soe = crs.getString("config_preowned_soe");
				config_preowned_sob = crs.getString("config_preowned_sob");
				config_preowned_campaign = crs.getString("config_preowned_campaign");
				config_preowned_refno = crs.getString("config_preowned_refno");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String BuildAdvHtml(HttpServletRequest request, HttpServletResponse response) {
		preowned_branch_id = CNumeric(PadQuotes(request.getParameter("dr_preownedbranch")));
		executive_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		preowned_prioritypreowned_id = CNumeric(PadQuotes(request.getParameter("prioritypreowned_id")));
		soe_id = CNumeric(PadQuotes(request.getParameter("dr_soe")));
		sob_id = CNumeric(PadQuotes(request.getParameter("dr_sob")));
		if (request.getParameterValues("chk_preowned_status") != null) {
			status_ids = request.getParameterValues("chk_preowned_status");
		}

		// if (request.getParameterValues("chk_evalstatus") != null) {
		// eval_status = request.getParameterValues("chk_evalstatus");
		// }
		if (request.getParameterValues("chk_evalstatus") != null) {
			eval_status_id = request.getParameterValues("chk_evalstatus");
		}

		eval_status = PadQuotes(RetrunSelectArrVal(request, "chk_evalstatus"));

		if (request.getParameterValues("chk_preowned_model") != null) {
			model_ids = request.getParameterValues("chk_preowned_model");
		}

		if (!preowned_branch_id.equals("0")) {
			StrSearch = StrSearch + " AND preowned_branch_id = " + preowned_branch_id;
		}
		if (!executive_id.equals("0")) {
			StrSearch = StrSearch + " AND preowned_emp_id = " + executive_id;
		}
		if (!preowned_prioritypreowned_id.equals("0")) {
			StrSearch = StrSearch + " AND preowned_prioritypreowned_id = " + preowned_prioritypreowned_id;
		}
		if (!soe_id.equals("0")) {
			StrSearch = StrSearch + " AND preowned_soe_id = " + soe_id;
		}
		if (!sob_id.equals("0")) {
			StrSearch = StrSearch + " AND preowned_sob_id = " + sob_id;
		}

		if (eval_status.contains("1") && !eval_status.contains("0")) {
			StrSearch = StrSearch + " AND eval_preowned_id != 0 ";
		}

		if (eval_status.contains("0") && !eval_status.contains("1")) {
			StrSearch = StrSearch + " AND NOT preowned_id IN (SELECT eval_preowned_id FROM " + compdb(comp_id) + "axela_preowned_eval) ";
		}

		// if (model_ids != null) {
		// StrSearch = StrSearch + " and preowned_preownedmodel_id in (" +
		// RetrunSelectArrVal(request, "chk_preowned_model") + ")";
		// }

		if (status_ids != null) {
			StrSearch += " AND preowned_preownedstatus_id IN (" + RetrunSelectArrVal(request, "chk_preowned_status") + ")";
		}

		StringBuilder Str = new StringBuilder();
		Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");
		Str.append("<tr align=center>\n");
		Str.append("<td align=center>");
		Str.append("<div class=\"container-fluid\"><div class=\"col-md-4 col-xs-12\"><label class=\"control-label col-md-4 col-xs-1\">Branch: </label>");
		Str.append("<div class=\"col-md-8 col-xs-12\"><SELECT name=dr_preownedbranch class=form-control id=dr_preownedbranch>");
		Str.append(PopulateBranch(preowned_branch_id, "", "2", "", request));
		Str.append("</SELECT></div></div>");
		Str.append("<div class=\"col-md-4 col-xs-12\"><label class=\"control-label col-md-4 col-xs-1\">Pre-Owned Consultant: </label>");
		Str.append("<div class=\"col-md-8 col-xs-12\"><SELECT name=dr_executive class=form-control id=dr_executive>");
		Str.append(PopulateExecutive());
		Str.append("</SELECT></div></div>");
		Str.append("<div class=\"col-md-4 col-xs-12\"><label class=\"control-label col-md-4 col-xs-1\">Priority: </label>");
		Str.append("<div class=\"col-md-8 col-xs-12\"><SELECT name=prioritypreowned_id class=form-control id=prioritypreowned_id>");
		Str.append(PopulatePreownedPriority());
		Str.append("</SELECT></div>");
		Str.append("</div></div>");
		// Str.append("</br>");
		Str.append("</td>\n");
		Str.append("</tr>");

		if (config_preowned_soe.equals("1") || config_preowned_sob.equals("1")) {
			Str.append("<tr align=center>");
			Str.append("<td align=center nowrap>");
			if (config_preowned_soe.equals("1")) {
				Str.append("<div style=\"display:inline-block\">&nbsp;&nbsp;SOE: <SELECT name=dr_soe class=form-control id=dr_soe>");
				Str.append(PopulateSOE());
				Str.append("</SELECT></div>");
			}
			if (config_preowned_sob.equals("1")) {
				Str.append("<div style=\"display:inline-block\">&nbsp;&nbsp;SOB: <SELECT name=dr_sob class=form-control id=dr_sob>");
				Str.append(PopulateSOB());
				Str.append("</SELECT></div>");
			}
			Str.append("</td>\n");
			Str.append("</tr>");
		}
		// Str.append("<tr>");
		// Str.append("<td align=center>");
		// Str.append(PopulateModel());
		// Str.append("</td>\n");
		// Str.append("</tr>");
		Str.append("<tr>");
		Str.append("<td align=center>");
		Str.append(PopulateStatus());
		Str.append("</td>\n");
		Str.append("</tr>");

		Str.append("<tr>");
		Str.append("<td align=center>");
		Str.append(PopulateEvalStatus());
		Str.append("</td");
		Str.append("</tr>");

		Str.append("</table>");
		return Str.toString();
	}

	// public String PopulatePreownedBranch() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value = 0>SELECT</option>");
	// try {
	// StrSql = "SELECT branch_id, concat(branch_name,' (',branch_code,')') AS branch_name "
	// + " FROM " + compdb(comp_id) + "axela_branch"
	// + " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_branch_id = branch_id "
	// + " WHERE 1=1 and branch_active='1' AND branch_branchtype_id = 2" + BranchAccess.replace("branch_id", "preowned_branch_id")
	// + ExeAccess.replace("emp_id", "preowned_emp_id")
	// + " GROUP BY branch_id"
	// + " ORDER BY branch_name";
	// CachedRowSet crs = processQuery(StrSql, 0);
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("branch_id")).append("");
	// Str.append(StrSelectdrop(crs.getString("branch_id"), preowned_branch_id));
	// Str.append(">").append(crs.getString("branch_name")).append("</option> \n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto===" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0>SELECT</option>");
		try {
			StrSql = "SELECT emp_name, emp_ref_no, " + compdb(comp_id) + "axela_emp.emp_id AS emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_emp_id = " + compdb(comp_id) + "axela_emp.emp_id"
					+ " WHERE emp_active = '1'" + ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), executive_id));
				Str.append(">").append(crs.getString("emp_name")).append(" (").append(crs.getString("emp_ref_no")).append(") </option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePreownedPriority() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> SELECT </option>");
		try {
			StrSql = "SELECT prioritypreowned_id, prioritypreowned_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_priority "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_prioritypreowned_id = prioritypreowned_id "
					+ " WHERE 1=1 " + BranchAccess.replace("branch_id", "preowned_branch_id")
					+ ExeAccess.replace("emp_id", "preowned_emp_id")
					+ " GROUP BY prioritypreowned_id "
					+ " ORDER BY prioritypreowned_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("prioritypreowned_id")).append("");
				Str.append(StrSelectdrop(crs.getString("prioritypreowned_id"), preowned_prioritypreowned_id));
				Str.append(">").append(crs.getString("prioritypreowned_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	//

	public String PopulateSOE() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> SELECT </option>");
		try {
			StrSql = "SELECT soe_id, soe_name "
					+ " FROM " + compdb(comp_id) + "axela_soe "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_soe_id = soe_id "
					+ " WHERE 1=1 " + BranchAccess.replace("branch_id", "preowned_branch_id")
					+ ExeAccess.replace("emp_id", "preowned_emp_id")
					+ " GROUP BY soe_id "
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateSOB() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = 0> SELECT </option>");
		try {
			StrSql = "SELECT sob_id, sob_name "
					+ " FROM " + compdb(comp_id) + "axela_sob "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_sob_id = sob_id "
					+ " WHERE 1=1 " + BranchAccess.replace("branch_id", "preowned_branch_id")
					+ ExeAccess.replace("emp_id", "preowned_emp_id")
					+ " GROUP BY sob_id "
					+ " ORDER BY sob_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), soe_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	//

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT preownedmodel_id, preownedmodel_name "
				+ " FROM axela_preowned_model"
				+ " WHERE 1=1" // model_name like '%mercedes%'" //only to
								// display particular model
				+ " ORDER BY preownedmodel_name";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				Str.append("<input type=checkbox name=chk_preowned_model value=").append(crs.getString("preownedmodel_id")).append(" ");
				Str.append(ArrSelectCheck(crs.getInt("preownedmodel_id"), model_ids)).append("");
				Str.append(">").append(crs.getString("preownedmodel_name")).append("&nbsp;");
			}
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

	public String PopulateStatus() {
		StringBuilder Str = new StringBuilder();
		String ids = "";
		StrSql = "SELECT preownedstatus_id, preownedstatus_name "
				+ " FROM " + compdb(comp_id) + "axela_preowned_status "
				+ " ORDER BY preownedstatus_id";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (all.equals("yes")) {
				while (crs.next()) {
					if (crs.getInt("preownedstatus_id") == 1) {
						ids = ids + " " + crs.getInt("preownedstatus_id");
					}
				}
				status_ids = ids.trim().split(" ");
			}
			crs.beforeFirst();
			while (crs.next()) {
				Str.append("<input type=checkbox name=chk_preowned_status value=").append(crs.getString("preownedstatus_id")).append(" ");
				Str.append(ArrSelectCheck(crs.getInt("preownedstatus_id"), status_ids)).append("");
				Str.append(">").append(crs.getString("preownedstatus_name")).append("&nbsp;");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateEvalStatus() {
		StringBuilder Str = new StringBuilder();

		Str.append("<input type='checkbox' name='chk_evalstatus' value='").append("1").append("' ");
		Str.append(ArrSelectCheck(1, eval_status_id)).append("");
		Str.append("/>").append("Evaluation Done").append("&nbsp;");

		Str.append("<input type='checkbox' name='chk_evalstatus' value='").append("0").append("' ");
		Str.append(ArrSelectCheck(0, eval_status_id)).append("");
		Str.append("/>").append("Evaluation Not Done").append("&nbsp;");

		return Str.toString();

	}

}
