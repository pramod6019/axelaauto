package axela.service;
//aJIt 1st March, 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Vehicle_List_Modal extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../service/vehicle.jsp\">Vehicles</a>"
			+ " &gt; <a href=\"vehicle-list.jsp?all=yes\">List Vehicles</a>:";
	public String LinkExportPage = "vehicle-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkAddPage = "<a href=\"vehicle-update.jsp?add=yes\">Add New Vehicle...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String all = "";
	public String smart = "";
	public String group = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String QueryString = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public String BranchAccess = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String advSearch = "";
	public Smart SmartSearch = new Smart();
	public String PageCurrents = "";
	public String veh_id = "0";
	public String veh_customer_id = "0";
	public String jc_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Vehicle ID", "numeric", "veh_id"},
			{"Item ID", "numeric", "veh_variant_id"},
			{"Model Name", "text", "model_name"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Reg. No.", "text", "veh_reg_no"},
			{"Chassis Number", "text", "veh_chassis_no"},
			{"Engine No.", "text", "veh_engine_no"},
			{"IACS", "boolean", "veh_iacs"},
			{"Customer ID", "numeric", "customer_id"},
			{"Contact ID", "numeric", "contact_id"},
			{"Customer Name", "text", "customer_name"},
			{"Contact Title", "text", "contact_title_id IN (SELECT title_id FROM compdb.axela_title WHERE title_desc"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Contactable", "boolean", "if(veh_contactable_id=1,1,0)"},
			{"Not Contactable", "boolean", "if(veh_contactable_id=2,1,0)"},
			{"Sale Date", "date", "veh_sale_date"},
			{"Insurance Executive", "text", "veh_insuremp_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Insurance Executive ID", "numeric", "veh_insuremp_id"},
			{"Notes", "text", "veh_notes"},
			{"Entry By", "text", "veh_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "veh_entry_date"},
			{"Modified By", "text", "veh_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "veh_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				smart = PadQuotes(request.getParameter("smart"));
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				veh_customer_id = CNumeric(PadQuotes(request.getParameter("veh_customer_id")));
				all = PadQuotes(request.getParameter("all"));
				group = PadQuotes(request.getParameter("group"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND veh_id = 0";
				} else if ("yes".equals(all)) {
					msg = "<br/>Results for all Vehicle!";
				} else if (!veh_id.equals("0")) {
					msg += "<br/>Results for Vehicle ID = " + veh_id + "!";
					StrSearch += " AND veh_id = " + veh_id + "";
				} else if (!veh_customer_id.equals("0")) {
					msg += "<br/>Results for Customer ID = " + veh_customer_id + "!";
					StrSearch += " AND customer_id = " + veh_customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					// SOP("StrSearch====before=="+StrSearch);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND veh_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if (smart.equals("yes")) {

					msg += "<br/>Results of Search!";
					if (!GetSession("vehstrsql", request).equals("")) {
						StrSearch = GetSession("vehstrsql", request);
						// SOP("StrSearch==" + StrSearch);
					}
				}

				SetSession("vehstrsql", StrSearch, request);
				StrSearch += BranchAccess.replace("branch_id", "veh_branch_id");
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
		int PageListSize = 10;
		int StartRec;
		int EndRec;
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}
				PageCurrent = Integer.parseInt(PageCurrents);

				StrSql = "SELECT veh_id, veh_variant_id, veh_emp_id, veh_insuremp_id, veh_branch_id, COALESCE(model_name, '') AS model_name,"
						+ " veh_chassis_no, veh_kms,"
						+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS itemname,"
						+ " COALESCE(item_id, 0) AS item_id, veh_engine_no, veh_reg_no, veh_lastservice_kms, veh_sale_date,"
						+ " COALESCE(customer_id, 0) AS customer_id, COALESCE(customer_name, '') AS customer_name,"
						+ " COALESCE(contact_id, 0) AS contact_id, veh_so_id, veh_iacs, veh_cal_kms,"
						+ " veh_modelyear, IF(veh_lastservice != 0, veh_lastservice, '') AS veh_lastservice,"
						+ " veh_calservicedate, branch_id, branch_name,"
						+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contactname,"
						+ " COALESCE(contact_address, '') AS contact_address,"
						+ " COALESCE(contact_landmark, '') AS contact_landmark,"
						+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
						+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
						+ " WHERE phonetype_id = contact_mobile1_phonetype_id ), '') AS phonetypemobile1,"
						+ " COALESCE(contact_mobile2, '') AS contact_mobile2,"
						+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
						+ " WHERE phonetype_id = contact_mobile2_phonetype_id ), '') AS phonetypemobile2,"
						+ " COALESCE(contact_mobile3, '') AS contact_mobile3,"
						+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
						+ " WHERE phonetype_id = contact_mobile3_phonetype_id ), '') AS phonetypemobile3,"
						+ " COALESCE(contact_mobile4, '') AS contact_mobile4,"
						+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
						+ " WHERE phonetype_id = contact_mobile4_phonetype_id ), '') AS phonetypemobile4,"
						+ " COALESCE(contact_mobile5, '') AS contact_mobile5,"
						+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
						+ " WHERE phonetype_id = contact_mobile5_phonetype_id ), '') AS phonetypemobile5,"
						+ " COALESCE(contact_mobile6, '') AS contact_mobile6,"
						+ " COALESCE (( SELECT phonetype_name FROM axela_phonetype "
						+ " WHERE phonetype_id = contact_mobile6_phonetype_id ), '') AS phonetypemobile6,"
						+ " COALESCE(contact_email1, '') AS contact_email1,"
						+ " COALESCE(contact_email2, '') AS contact_email2, "
						+ " REPLACE ( COALESCE ( ( SELECT GROUP_CONCAT( 'StartColor', tag_colour, 'EndColor', 'StartName', tag_name, 'EndName' )"
						+ " FROM  " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_tag_trans"
						+ " ON tagtrans_tag_id = tag_id WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag ";

				CountSql = "SELECT COUNT(DISTINCT(veh_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;
				// SOP("StrSql====" + StrSql);
				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY veh_id";
					// + " ORDER BY veh_id DESC";
				}
				// SOP("vehfollowup_appt_time");
				CountSql += StrSearch;
				// SOP("CountSql========" + CountSql);

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));

				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
					// if limit ie. 10 > totalrecord
					if (EndRec > TotalRecords) {
						EndRec = TotalRecords;
					}

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Vehicle(s)";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}

					PageURL = "vehicle-list.jsp?" + QueryString + "&PageCurrent=";

					PageCount = (TotalRecords / recperpage);
					if ((TotalRecords % recperpage) > 0) {
						PageCount++;
					}
					// display on jsp page
					PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
					// if (all.equals("yes")) {
					// StrSql = StrSql.replaceAll("\\bFROM " + compdb(comp_id) + "axela_service_veh\\b",
					// "FROM " + compdb(comp_id) + "axela_service_veh"
					// + " INNER JOIN (SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
					// // + " GROUP BY veh_id"
					// + " ORDER BY veh_id DESC"
					// // + " LIMIT " + (StartRec - 1) + ", " + recperpage + ""
					// + ") AS myresults USING (veh_id)");
					// // StrSql = "SELECT * FROM (" + StrSql + ") AS datatable"
					// }
					// else {
					// StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// }
					// StrSql += " GROUP BY veh_id"
					// + " ORDER BY veh_id DESC";
					StrSql += " ORDER BY veh_id DESC";
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// SOP("Strsql==veh list-===" + StrSql);
					crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table table-responsive\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive width=\"100%\"\" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th>Customer</th>\n");
					Str.append("<th>Model</th>\n");
					Str.append("<th data-hide=\"phone\">Item</th>\n");
					Str.append("<th data-hide=\"phone\">Reg. No.</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Chassis Number</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Engine No.</th>\n");
					Str.append("<th data-hide=\"all\">Year</th>\n");
					Str.append("<th data-hide=\"all\" nowrap>Sale Date</th>\n");
					Str.append("<th data-hide=\"all\" nowrap>Last Service </th>\n");
					Str.append("<th data-hide=\"all\">Kms</th>\n");
					Str.append("<th data-hide=\"all\">Cal. Service</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Advisor</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Insurance Executive</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count++;
						Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("veh_id") + ")'"
								+ " onmouseout='HideCustomerInfo(" + crs.getString("veh_id") + ")' style='height:200px'>\n");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td>");
						Str.append("<a href=\"../service/vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
						Str.append(crs.getString("veh_id")).append("</a>");
						Str.append("</td>\n<td nowrap>");
						if (!crs.getString("customer_id").equals("0")) {
							Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("customer_id")).append("\">");
							Str.append(crs.getString("customer_name")).append("</a>");
						}

						if (!crs.getString("contactname").equals("")) {
							Str.append("<br/><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
							Str.append(crs.getString("contactname")).append("</a>");
						}

						if (!crs.getString("contact_mobile1").equals("")) {
							// Str.append("<br/>").append(crs.getString("contact_mobile1") + " (M) (" + crs.getString("phonetypemobile1") + ")")
							// .append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile1"), 10, "M", crs.getString("veh_id")))
									.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
						}

						if (!crs.getString("contact_mobile2").equals("")) {
							// Str.append("<br/>").append(crs.getString("contact_mobile2") + " (M) (" + crs.getString("phonetypemobile2") + ")")
							// .append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile2"), 10, "M", crs.getString("veh_id")))
									.append(ClickToCall(crs.getString("contact_mobile2"), comp_id));
						}

						if (!crs.getString("contact_mobile3").equals("")) {
							// Str.append("<br/>").append(crs.getString("contact_mobile3") + " (M) (" + crs.getString("phonetypemobile3") + ")")
							// .append(ClickToCall(crs.getString("contact_mobile3"), comp_id));
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile3"), 10, "M", crs.getString("veh_id")))
									.append(ClickToCall(crs.getString("contact_mobile3"), comp_id));
						}

						if (!crs.getString("contact_mobile4").equals("")) {
							// Str.append("<br/>").append(crs.getString("contact_mobile4") + " (M) (" + crs.getString("phonetypemobile4") + ")")
							// .append(ClickToCall(crs.getString("contact_mobile4"), comp_id));
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile4"), 10, "M", crs.getString("veh_id")))
									.append(ClickToCall(crs.getString("contact_mobile4"), comp_id));
						}

						if (!crs.getString("contact_mobile5").equals("")) {
							// Str.append("<br/>").append(crs.getString("contact_mobile5") + " (M) (" + crs.getString("phonetypemobile5") + ")")
							// .append(ClickToCall(crs.getString("contact_mobile5"), comp_id));
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile5"), 10, "M", crs.getString("veh_id")))
									.append(ClickToCall(crs.getString("contact_mobile5"), comp_id));
						}

						if (!crs.getString("contact_mobile6").equals("")) {
							// Str.append("<br/>").append(crs.getString("contact_mobile6") + " (M) (" + crs.getString("phonetypemobile6") + ")")
							// .append(ClickToCall(crs.getString("contact_mobile6"), comp_id));
							Str.append("<br>").append(SplitPhoneNoSpan(crs.getString("contact_mobile6"), 10, "M", crs.getString("veh_id")))
									.append(ClickToCall(crs.getString("contact_mobile6"), comp_id));
						}

						if (!crs.getString("contact_email1").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email1")).append("\">");
							Str.append(crs.getString("contact_email1")).append("</a></span>");
						}

						if (!crs.getString("contact_email2").equals("")) {
							Str.append("<br><span class='customer_info customer_" + crs.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
									.append(crs.getString("contact_email2")).append("\">");
							Str.append(crs.getString("contact_email2")).append("</a></span>");
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

						Str.append("</td>\n<td>").append(crs.getString("model_name"));
						Str.append("</td>\n<td>");
						if (!crs.getString("item_id").equals("0")) {
							Str.append("<a href=\"../inventory/inventory-item-list.jsp?item_id=").append(crs.getString("item_id"));
							Str.append("\">").append(crs.getString("itemname")).append("</a>");
						}

						Str.append("</td>\n<td nowrap>");
						Str.append("<a href=\"../service/vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
						Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
						Str.append("</td>\n<td>").append(crs.getString("veh_chassis_no"));
						Str.append("</td>\n<td>").append(crs.getString("veh_engine_no"));
						if (crs.getString("veh_iacs").equals("1")) {
							Str.append("<br/><font color=\"red\"><b>IACS</b></font>");
						}
						Str.append("</td>\n<td>").append(crs.getString("veh_modelyear"));
						Str.append("</td>\n");
						Str.append("<td>").append(strToShortDate(crs.getString("veh_sale_date")));
						Str.append("</td>\n");
						Str.append("<td nowrap> ");
						if (!crs.getString("veh_lastservice").equals("")) {
							Str.append(strToShortDate(crs.getString("veh_lastservice"))).append("<br/>");
						}
						Str.append(IndFormat(crs.getString("veh_lastservice_kms"))).append(" Kms");
						Str.append("</td>\n<td nowrap>Last: ").append(IndFormat(crs.getString("veh_kms")));
						// Str.append("<br/>Cal: ").append(IndFormat(crs.getString("veh_cal_kms")));
						Str.append("</td>\n");
						Str.append("<td nowrap> ");
						if (!crs.getString("veh_calservicedate").equals("")) {
							Str.append(strToShortDate(crs.getString("veh_calservicedate"))).append("<br/>");
						}
						Str.append(IndFormat(crs.getString("veh_cal_kms"))).append(" Kms");
						Str.append("<td>");
						if (!crs.getString("veh_emp_id").equals("0")) {
							Str.append(Exename(comp_id, crs.getInt("veh_emp_id")));
						}
						Str.append("</td>\n<td>");
						if (!crs.getString("veh_insuremp_id").equals("0")) {
							Str.append(Exename(comp_id, crs.getInt("veh_insuremp_id")));
						}
						// Str.append("\n<td align=\"center\" valign=\"top\">").append(crs.getString("branch_name"));
						// Str.append("</td>\n");
						Str.append("\n<td>");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a>");
						Str.append("</td>\n");
						Str.append("<td nowrap>");
						if (group.equals("select_jc_veh")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectVeh('").append(crs.getString("veh_id")).append("','");
							Str.append(crs.getString("veh_variant_id")).append("','").append(crs.getString("itemname")).append("','");
							Str.append(crs.getString("veh_chassis_no")).append("','").append(crs.getString("veh_engine_no"));
							Str.append("','").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("','").append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("contact_id")).append("','").append(crs.getString("customer_name")).append("','");
							Str.append(crs.getString("contactname")).append("');\">Select Vehicle </a>\n");
						} else if (group.equals("select_veh_insurance")) {
							Str.append("<a href=# data-dismiss='modal' onClick=\"javascript:window.parent.SelectVehicle('").append(crs.getString("veh_id")).append("','");
							Str.append(crs.getString("veh_variant_id")).append("','").append(crs.getString("itemname")).append("','");
							Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("','").append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("contact_id")).append("','").append(crs.getString("customer_name")).append("','");
							Str.append(crs.getString("contactname")).append("');\">Select Vehicle </a>\n");
						} else if (group.equals("select_veh_call")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectVehicle('").append(crs.getString("veh_id")).append("','");
							Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("','").append(crs.getString("contact_id")).append("','");
							Str.append(crs.getString("contactname")).append("','").append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("customer_name")).append("','").append(crs.getString("itemname")).append("','");
							Str.append(crs.getString("item_id")).append("','").append(crs.getString("contact_address").replaceAll("\n", " ").replaceAll("'", "single_quote")).append("','");
							Str.append(crs.getString("contact_landmark")).append("','").append(crs.getString("contact_mobile1")).append("','");
							Str.append(crs.getString("contact_mobile2")).append("');\">Select Vehicle </a>\n");
						} else if (group.equals("select_veh_pickup")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectVehicle('").append(crs.getString("veh_id")).append("','");
							Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("','").append(crs.getString("contact_id")).append("','");
							Str.append(crs.getString("contactname")).append("','").append(crs.getString("customer_id")).append("','");
							Str.append(crs.getString("customer_name")).append("','").append(crs.getString("contact_address").replaceAll("\n", " ").replaceAll("'", "single_quote")).append("','");
							Str.append(crs.getString("contact_landmark")).append("','").append(crs.getString("contact_mobile1")).append("','");
							Str.append(crs.getString("contact_mobile2")).append("');\">Select Vehicle </a>\n");
						} else if (group.equals("select_veh_ownership")) {
							Str.append("<a href=# onClick=\"javascript:window.parent.SelectVehicle('").append(crs.getString("veh_id")).append("','");
							Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("','");
							Str.append(crs.getString("itemname")).append("','");
							Str.append(crs.getString("veh_variant_id")).append("','");
							Str.append("');\">Select Vehicle</a>\n");
						} else {
							Str.append("<a href=\"vehicle-update.jsp?update=yes&veh_id=").append(crs.getString("veh_id")).append("&veh_branch_id=").append(crs.getString("veh_branch_id"))
									.append("\">Update Vehicle</a>");
							Str.append("<br/><a href=\"../service/vehicle-options.jsp?veh_id=").append(crs.getString("veh_id")).append("\">Configure Vehicle</a>");
							Str.append("<br/><a href=\"../service/kms-list.jsp?vehkms_veh_id=").append(crs.getString("veh_id")).append("\">List Kms</a>");
							Str.append("<br/><a href=\"../service/kms-update.jsp?add=yes&vehkms_veh_id=").append(crs.getString("veh_id")).append("\">Add Kms</a>");
							Str.append("<br/><a href=\"../service/jobcard-list.jsp?jc_veh_id=").append(crs.getString("veh_id")).append("\">List Job Cards</a>");
							Str.append("<br/><a href=\"../insurance/insurance-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Insurance</a>");
							Str.append("<br/><a href=\"../service/veh-ownership-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Ownership</a>");
							Str.append("<br/><a href=\"../service/jobcard-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("&branch_id=").append(crs.getString("branch_id"))
									.append("\">Add Job Card</a>");
							Str.append("<br/><a href=\"../service/jobcard-quickadd.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Quick Add Job Card</a>");
							Str.append("<br/><a href=\"../customer/customer-contact-update.jsp?Add=yes&customer_id=").append(crs.getString("customer_id")).append("\">Add New Contact</a>");
							Str.append("<br/><a href=\"../service/ticket-add.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Ticket</a>");
							Str.append("<br/><a href=\"../service/pickup-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Pickup</a>");
							Str.append("<br/><a href=\"../service/booking-list.jsp?booking_veh_id=").append(crs.getString("veh_id")).append("\">List Bookings</a>");
							Str.append("<br/><a href=\"../service/booking-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Booking</a>");
						}
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
					// // Add News Letter Link
					LinkAddPage += "<br/><a href=\"../portal/news-letter-enquiry.jsp?target=vehicles\" target=_blank>Send News Letter</a>";
				} else {
					Str.append("<br/><br/><font color=\"red\"><b>No Vehicle(s) found!</b></font>");
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
