package axela.customer;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;
//@saiman 21st june 2012
public class Customer_Group_List extends Connect {

	// ///// List page links
	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=index.jsp>Front Office</a> &gt; "
			+ "<a href=\"customer-group-list.jsp?all=yes\">List Groups</a><b>:</b>";
	public String LinkListPage = "fo-group-list.jsp";
	public String LinkExportPage = "group.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkFilterPage = "group-filter.jsp";
	public String LinkAddPage = "<a href=customer-group-update.jsp?Add=yes>Add New Group..</a>";
	public String ExportPerm = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String search = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String smart = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String txt_search = "";
	public String drop_search;
	public String all = "";
	public String group = "";
	public String alpha = "";
	public String others = "";
	public String go = "";
	public String group_id = "";
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Enquiry ID", "numeric", "enquiry_id"},
			{"Enquiry No.", "text", "CONCAT('ENQ', branch_code, enquiry_no)"},
			{"DMS No.", "text", "enquiry_dmsno"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			// {"Lead ID", "numeric", "enquiry_lead_id"},
			{"New Car", "boolean", "if(enquiry_enquirytype_id=1,1,0)"},
			{"Pre Owned", "boolean", "if(enquiry_enquirytype_id=2,1,0)"},
			{"Title", "text", "enquiry_title"},
			{"Descripition", "text", "enquiry_desc"},
			{"Date", "date", "enquiry_date"},
			{"Closing Date", "date", "enquiry_close_date"},
			{"Follow-up Time", "date", "enquiry_id IN (SELECT followup_enquiry_id FROM compdb.axela_sales_enquiry_followup WHERE followup_followup_time"},
			{"Follow-up Type ID", "numeric", "enquiry_id IN (SELECT followup_enquiry_id FROM compdb.axela_sales_enquiry_followup WHERE followup_followuptype_id"},
			{"Value", "numeric", "enquiry_value"},
			{"Model", "text", "enquiry_model_id IN (SELECT model_id FROM compdb.axela_inventory_item_model WHERE model_name"},
			{"Variant", "text", "item_name"},
			// {"Pre Owned Manufacture", "text",
			// "preownedmodel_carmanuf_id in (select concat(carmanuf_name) from axela_preowned_variant"
			// +
			// " inner join axela_preowned_model on preownedmodel_id = variant_preownedmodel_id "
			// +
			// " inner join axela_preowned_manuf on carmanuf_id = preownedmodel_carmanuf_id "
			// + " where variant_id = enquiry_preownedvariant_id"},
			// "preownedmodel_carmanuf_id in (select carmanuf_name from axela_preowned_model"
			// +
			// " inner join axela_preowned_manuf on carmanuf_id = preownedmodel_carmanuf_id "
			// + " where preownedmodel_name"},
			{"Pre Owned Model", "text", "enquiry_preownedvariant_id in (select variant_id from axela_preowned_variant "
					+ " INNER JOIN axelaauto.axela_preowned_model on preownedmodel_id = variant_preownedmodel_id where preownedmodel_name"},
			{"Pre Owned Variant", "text", "enquiry_preownedvariant_id in (select variant_id from axela_preowned_variant where variant_name"},
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
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));

				CheckPerm(comp_id, "emp_customer_access", request, response);
				PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
				smart = PadQuotes(request.getParameter("smart"));
				QueryString = PadQuotes(request.getQueryString());
				search = PadQuotes(request.getParameter("search"));
				go = PadQuotes(request.getParameter("go_button"));
				msg = PadQuotes(request.getParameter("msg"));
				group_id = CNumeric(PadQuotes(request.getParameter("group_id")));
				all = PadQuotes(request.getParameter("all"));
				alpha = PadQuotes(request.getParameter("Alpha"));
				others = PadQuotes(request.getParameter("Others"));
				group = PadQuotes(request.getParameter("group"));

				if ("yes".equals(all)) {
					msg = "Results for all Groups!";
					StrSearch = " AND group_id > 0";
				} else if (!group_id.equals("0")) {
					msg = msg + "<br>Results for group!";
					StrSearch = StrSearch + " AND group_id = " + group_id + "";
				} else if ("yes".equals(smart)) {
					msg = msg + "<br>Results of Search!";
					if (!GetSession("centerstrsql", request).equals("")) {
						StrSearch = StrSearch + GetSession("centerstrsql", request);
					}
				} else if ("yes".equals(search)) // for keyword search
				{
					GetValues(request, response);
					if (!txt_search.equals("")) {
						if (drop_search.equals("-1")) {
							msg = "";
							msg = "Result for Search = " + txt_search + "!";
							StrSearch = StrSearch + " OR group_desc LIKE '%" + txt_search + "%'";
							StrSearch = StrSearch + ") ";
						} else if (drop_search.equals("1")) {
							msg = "Result for Search = " + txt_search + "";
							StrSearch = StrSearch + "";
						} else if (drop_search.equals("2")) {
							msg = "Result for Search =" + txt_search + "";
							StrSearch = StrSearch + " AND (group_desc LIKE '%" + txt_search + "%')";
						}
					} else {
						msg = "Select a search parameter!";
						StrSearch = StrSearch + " AND group_id = 0";
					}
				}
				if (!StrSearch.equals("")) {

					SetSession("centerstrsql", StrSearch, request);
					SetSession("centerFilterStr", StrSearch, request);
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			txt_search = PadQuotes(request.getParameter("txt_search"));
			drop_search = PadQuotes(request.getParameter("dr_search"));

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String Listdata() {

		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		String StrJoin = "";
		StringBuilder Str = new StringBuilder();
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;

		if (!msg.equals("")) {
			if ((PageCurrents == null) || (PageCurrents.length() < 1) || isNumeric(PageCurrents) == false) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search

			if (!msg.equals("")) {

				StrSql = "SELECT group_id, group_desc";

				StrJoin = " FROM " + compdb(comp_id) + "axela_customer_group"
						+ " WHERE 1 = 1";

				CountSql = " SELECT COUNT(DISTINCT group_id)";

				StrSql = StrSql + StrJoin;
				CountSql = CountSql + StrJoin;

				if (!(StrSearch.equals(""))) {
					StrSql = StrSql + StrSearch;
					CountSql = CountSql + StrSearch;
				}
				// SOP("StrSql"+StrSql);

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {

					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}
					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Group(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}
					PageURL = "fo-group-list.jsp?" + QueryString + "&PageCurrent=";
					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount = PageCount + 1;
					}
					// display on jsp page

					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					// StrSql = StrSql + StrSearch;
					if (all.equals("yes")) {
						StrSql = StrSql + " ORDER BY group_id DESC";
					} else {
						StrSql = StrSql + "  ORDER BY group_id";
					}
					StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// SOP("StrSql...."+StrSql);

					try {
						CachedRowSet crs = processQuery(StrSql, 0);
						int count = StartRec - 1;
						String active = "";
						Str.append("<div class=\"table-responsive\">\n");
						Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
						Str.append("<thead><tr>\n");
						Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
						Str.append("<th>Description</th>\n");
						Str.append("<th data-hide=\"phone\"> Actions</th>\n");
						Str.append("</tr>\n");
						Str.append("</thead>\n");
						Str.append("<tbody>\n");

						while (crs.next()) {

							count = count + 1;
							Str.append("<tr>\n");
							Str.append("<td valign=top align=center >").append(count).append("</td>\n");
							Str.append("<td valign=top align=left>");
							Str.append("").append(crs.getString("group_desc"));
							Str.append("</td>\n");
							Str.append("<td valign=top><a href=\"customer-group-update.jsp?Update=yes&group_id=").append(crs.getString("group_id")).append(" \">Update Group</a></td>\n");
							Str.append("</tr>\n");
						}
						Str.append("</tbody>\n");
						Str.append("</table>\n");
						Str.append("</div>\n");
						crs.close();
					} catch (Exception ex) {
						SOPError("Axelaauto== " + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						return "";
					}
				} else {
					RecCountDisplay = "<br><br><br><br><font color=red>No Group(s) found!</font><br><br>";
				}
			}
		}
		return Str.toString();
	}

	public String PopulateSearch() {
		search = "<option value = -1" + Selectdrop(-1, drop_search) + ">Keyword</option>";
		search = search + "<option value = 1" + Selectdrop(1, drop_search) + ">Group ID</option>\n";
		search = search + "<option value = 2" + Selectdrop(2, drop_search) + ">Description</option>\n";
		return search;
	}

	public String AtoZ() {
		String atoz = "";
		for (int i = 65; i <= 90; i++) {
			atoz = atoz + " <a href=fo-group-list.jsp?Alpha=" + (char) i + ">" + (char) i + "</a>&nbsp;";
		}
		return atoz;
	}
}
// Created by Dhanesh on 12-05-08

