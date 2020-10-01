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

public class Vehicle_List extends Connect {
	// ///// List page links

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../service/vehicle.jsp\">Vehicles</a>"
			+ " &gt; <a href=\"vehicle-list.jsp?all=yes\">List Vehicles</a>:";
	public String LinkExportPage = "vehicle-export.jsp?smart=yes&target=" + Math.random() + "";
	public String LinkPrintPage = "";
	public String LinkAddPage = "<a href=\"vehicle-update.jsp?add=yes\">Add New Vehicle...</a>";
	public String ExportPerm = "";
	public String msg = "";
	public String StrHTML = "";
	public String advhtml = "";
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
	public String brand_id = "", model_id = "";
	public String[] brand_ids, model_ids;
	public String veh_customer_id = "0";
	// public String jc_no = "";
	public String emp_id = "0";
	public String comp_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Vehicle ID", "numeric", "veh_id"},
			{"Variant ID", "numeric", "veh_variant_id"},
			{"Model Name", "text", "preownedmodel_name"},
			{"Branch ID", "numeric", "branch_id"},
			{"Branch Name", "text", "branch_name"},
			{"Reg. No.", "text", "veh_reg_no"},
			{"FASTag", "text", "veh_fastag"},
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
			{"Contact Address", "text", "contact_address"},
			{"Contact City", "text", "contact_city_id IN (SELECT contact_city_id FROM compdb.axela_city WHERE city_name"},
			{"Contact Pin", "text", "contact_pin"},
			{"Contactable", "boolean", "if(veh_contactable_id=1,1,0)"},
			{"Not Contactable", "boolean", "if(veh_contactable_id=2,1,0)"},
			{"Classified", "boolean", "IF(veh_classified = 1, 1, 0)"},
			{"SO ID", "numeric", "veh_so_id"},
			{"Sale Date", "date", "veh_sale_date"},
			{"Notes", "text", "veh_notes"},
			{"Entry By", "text", "veh_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "veh_entry_date"},
			{"Modified By", "text", "veh_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "veh_modified_date"}
	};
	public axela.service.MIS_Check1 mischeck = new axela.service.MIS_Check1();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access, emp_insurance_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
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
				advhtml = BuildAdvHtml(request, response);
				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND veh_id = 0";
				} else if ("yes".equals(all)) {
					StrSearch += " AND SUBSTR(veh_entry_date,1,8) >= DATE_FORMAT(ADDDATE('" + ToLongDate(kknow()) + "',INTERVAL -7 DAY),'%Y%m%d')";
					msg = "<br/>Results for all Vehicle!";
				} else if (!veh_id.equals("0")) {
					msg += "<br/>Results for Vehicle ID = " + veh_id + "!";
					StrSearch += " AND veh_id = " + veh_id + "";
				} else if (!veh_customer_id.equals("0")) {
					msg += "<br/>Results for Customer ID = " + veh_customer_id + "!";
					StrSearch += " AND customer_id = " + veh_customer_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = StrSearch + SmartSearch.BuildSmartSql(smartarr, request);
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
					}
				}
				SetSession("vehstrsql", StrSearch, request);
				// StrSearch += BranchAccess.replace("branch_id", "veh_branch_id");

				if (!BranchAccess.equals("")) {
					StrSearch += " AND branch_brand_id IN (SELECT branch_brand_id"
							+ " FROM " + compdb(comp_id) + "axela_emp_branch"
							+ " WHERE 1=1"
							+ BranchAccess + ")";
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
		CachedRowSet crs;
		int TotalRecords;
		String PageURL;
		StringBuilder Str = new StringBuilder();
		String update_info = "";
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

				StrSql = "SELECT veh_id, veh_variant_id, veh_emp_id, veh_insuremp_id, veh_branch_id, COALESCE(preownedmodel_name, '') AS preownedmodel_name,"
						+ " veh_chassis_no, veh_kms,"
						+ " COALESCE(IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name), '') AS variantname,"
						+ " COALESCE(variant_id, 0) AS variant_id, veh_engine_no, veh_reg_no, veh_lastservice_kms, veh_sale_date,"
						+ " COALESCE(customer_id, 0) AS customer_id, COALESCE(customer_name, '') AS customer_name,"
						+ " COALESCE(contact_id, 0) AS contact_id, veh_so_id, veh_iacs, veh_cal_kms,"
						+ " veh_modelyear, IF(veh_lastservice != 0, veh_lastservice, '') AS veh_lastservice,"
						+ " IF(veh_classified != 0, 'Classified', '') AS veh_classified,"
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
						+ " FROM " + compdb(comp_id) + "axela_customer_tag"
						+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_tag_trans"
						+ " ON tagtrans_tag_id = tag_id WHERE tagtrans_customer_id = customer_id ), '' ), ',', '' ) AS tag ";

				CountSql = "SELECT COUNT(DISTINCT(veh_id))";

				SqlJoin = " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = veh_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY veh_id";
				}

				if (!all.equals("yes")) {
					CountSql += StrSearch;
				}

				TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
				if (TotalRecords != 0) {
					StartRec = ((PageCurrent - 1) * recperpage) + 1;
					EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
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
					StrSql += " ORDER BY veh_id DESC";
					StrSql += " LIMIT " + (StartRec - 1) + ", " + recperpage + "";
					// SOP("StrSql=====" + StrSql);
					crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"  table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover  \" data-filter=\"#filter\">");
					Str.append("<thead>\n");
					Str.append("<tr>\n");
					Str.append("<th data-toggle=\"true\">#</th>\n");
					Str.append("<th >ID</th>\n");
					Str.append("<th style=\"width:200px;\">Customer</th>\n");
					Str.append("<th>Model</th>\n");
					Str.append("<th data-hide=\"phone\">Item</th>\n");
					Str.append("<th data-hide=\"phone\">Reg. No.</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Chassis Number</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Engine No.</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Year</th>\n");
					Str.append("<th data-hide=\"phone,tablet\" nowrap>Sale Date</th>\n");
					Str.append("<th data-hide=\"phone,tablet\" nowrap>Last Service </th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Kms</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Cal. Service</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Advisor</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Branch</th>\n");
					Str.append("<th data-hide=\"phone,tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");

					while (crs.next()) {
						count++;
						// Str.append("<tr onmouseover='ShowCustomerInfo(" + crs.getString("veh_id") + ")' onmouseout='HideCustomerInfo(" + crs.getString("veh_id") + ");'");
						// Str.append(" style='height:200px'>\n");
						Str.append("<tr>");
						Str.append("<td >").append(count).append("</td>\n");
						Str.append("<td>");
						Str.append("<a href=\"../service/vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
						Str.append(crs.getString("veh_id")).append("</a>");
						Str.append("</td>");

						// Customer Info
						Str.append("<td>");
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
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("veh_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("veh_id"), "M"));
						}
						if (!crs.getString("contact_mobile3").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile3"), crs.getString("veh_id"), "M"));
						}
						if (!crs.getString("contact_mobile4").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile4"), crs.getString("veh_id"), "M"));
						}
						if (!crs.getString("contact_mobile5").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile5"), crs.getString("veh_id"), "M"));
						}
						if (!crs.getString("contact_mobile6").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile6"), crs.getString("veh_id"), "M"));
						}

						// if (!crs.getString("contact_email1").equals("")) {
						// Str.append("<br><span class='customer_info customer_" + crs.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
						// .append(crs.getString("contact_email1")).append("\">");
						// Str.append(crs.getString("contact_email1")).append("</a></span>");
						// }
						//
						// if (!crs.getString("contact_email2").equals("")) {
						// Str.append("<br><span class='customer_info customer_" + crs.getString("veh_id") + "'  style='display: none;'><a href=\"mailto:")
						// .append(crs.getString("contact_email2")).append("\">");
						// Str.append(crs.getString("contact_email2")).append("</a></span>");
						// }
						// Populating Tags in Enquiry list
						Str.append("<br><br>");

						String Tag = crs.getString("tag");
						Tag = ReplaceStr(Tag, "StartColor", "<button class='btn-xs btn-arrow-left' style='top:-16px; background:");
						Tag = ReplaceStr(Tag, "EndColor", " ; color:white'  disabled>&nbsp");
						Tag = ReplaceStr(Tag, "StartName", "");
						Tag = ReplaceStr(Tag, "EndName", "</button>&nbsp&nbsp&nbsp");
						Str.append(Tag);
						// Tags End

						Str.append("</td>");
						customer_info.setLength(0);
						Str.append("<td>").append(crs.getString("preownedmodel_name"));
						Str.append("</td>\n<td>");
						if (!crs.getString("variant_id").equals("0")) {
							Str.append("<a href=\"../preowned/managepreownedvariant.jsp?variant_id=").append(crs.getString("variant_id"));
							Str.append("\">").append(crs.getString("variantname")).append("</a>");
						}

						Str.append("</td>\n<td nowrap>");
						Str.append("<a href=\"../service/vehicle-dash.jsp?veh_id=").append(crs.getString("veh_id")).append("\">");
						Str.append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
						Str.append("</td>\n<td>").append(crs.getString("veh_chassis_no"));
						if (!crs.getString("veh_classified").equals("")) {
							Str.append("<br/><font color=\"red\"><b>[Classified]</b></font>");
						}
						Str.append("</td>\n<td>").append(crs.getString("veh_engine_no"));
						if (crs.getString("veh_iacs").equals("1")) {
							Str.append("<br/><font color=\"red\"><b>IACS</b></font>");
						}
						Str.append("</td>\n<td>").append(crs.getString("veh_modelyear"));
						Str.append("</td>\n");
						Str.append("<td nowrap>").append(strToShortDate(crs.getString("veh_sale_date")));
						if (!crs.getString("veh_so_id").equals("0")) {
							Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("veh_so_id"));
							Str.append("\">").append("<br>SO ID: " + crs.getString("veh_so_id")).append("</a>");
						}
						// Str.append("<br>SO ID: " + crs.getString("veh_so_id"));
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
						// if (!crs.getString("veh_insuremp_id").equals("0")) {
						// Str.append(Exename(comp_id, crs.getInt("veh_insuremp_id")));
						// }
						// // Str.append("\n<td align=\"center\" valign=\"top\">").append(crs.getString("branch_name"));
						// // Str.append("</td>\n");
						// Str.append("\n<td>");
						Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=").append(crs.getString("branch_id")).append("\">");
						Str.append(crs.getString("branch_name")).append("</a>");
						Str.append("</td>\n");
						Str.append("<td nowrap>");
						update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul style='margin-top: -5px;' class='dropdown-content dropdown-menu pull-right'>";

						if (group.equals("select_jc_veh")) {
							update_info += "<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectVeh('" + crs.getString("veh_id") + "','"
									+ crs.getString("veh_variant_id") + "','" + crs.getString("variantname") + "','"
									+ crs.getString("veh_chassis_no") + "','" + crs.getString("veh_engine_no")
									+ "','" + SplitRegNo(crs.getString("veh_reg_no"), 2) + "','" + crs.getString("customer_id") + "','"
									+ crs.getString("contact_id") + "','" + crs.getString("customer_name") + "','"
									+ crs.getString("contactname") + "');\">Select Vehicle </a></li>";
						} else if (group.equals("select_veh_insurance")) {
							update_info += "<li role=presentation><a href=# data-dismiss='modal' onClick=\"javascript:window.parent.SelectVehicle('" + crs.getString("veh_id") + "','"
									+ crs.getString("veh_variant_id") + "','" + crs.getString("variantname") + "','"
									+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "','" + crs.getString("customer_id") + "','"
									+ crs.getString("contact_id") + "','" + crs.getString("customer_name") + "','"
									+ crs.getString("contactname") + "');\">Select Vehicle </a></li>";
						} else if (group.equals("select_veh_call")) {
							update_info += "<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectVehicle('" + crs.getString("veh_id") + "','"
									+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "','" + crs.getString("contact_id") + "','"
									+ crs.getString("contactname") + "','" + crs.getString("customer_id") + "','"
									+ crs.getString("customer_name") + "','" + crs.getString("variantname") + "','"
									+ crs.getString("variant_id") + "','" + crs.getString("contact_address").replaceAll("\n", " ").replaceAll("'", "single_quote") + "','"
									+ crs.getString("contact_landmark") + "','" + crs.getString("contact_mobile1") + "','"
									+ crs.getString("contact_mobile2") + "');\">Select Vehicle </a></li>";
						} else if (group.equals("select_veh_pickup")) {
							update_info += "<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectVehicle('" + crs.getString("veh_id") + "','"
									+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "','" + crs.getString("contact_id") + "','"
									+ crs.getString("contactname") + "','" + crs.getString("customer_id") + "','"
									+ crs.getString("customer_name") + "','" + crs.getString("contact_address").replaceAll("\n", " ").replaceAll("'", "single_quote") + "','"
									+ crs.getString("contact_landmark") + "','" + crs.getString("contact_mobile1") + "','"
									+ crs.getString("contact_mobile2") + "');\">Select Vehicle </a></li>";
						} else if (group.equals("select_veh_ownership")) {
							update_info += "<li role=presentation><a href=# onClick=\"javascript:window.parent.SelectVehicle('" + crs.getString("veh_id") + "','"
									+ SplitRegNo(crs.getString("veh_reg_no"), 2) + "','"
									+ crs.getString("variantname") + "','"
									+ crs.getString("veh_variant_id") + "','"
									+ "');\">Select Vehicle</a></li>";
						} else {
							update_info += "<li role=presentation><a href=\"vehicle-update.jsp?update=yes&veh_id=" + crs.getString("veh_id") + "&veh_branch_id=" + crs.getString("veh_branch_id")
									+ "\">Update Vehicle</a></li>"
									+ "<li role=presentation><a href=\"../service/vehicle-options.jsp?veh_id=" + crs.getString("veh_id") + "\">Configure Vehicle</a></li>"
									+ "<li role=presentation><a href=\"../service/kms-list.jsp?vehkms_veh_id=" + crs.getString("veh_id") + "\">List Kms</a></li>"
									+ "<li role=presentation><a href=\"../service/kms-update.jsp?add=yes&vehkms_veh_id=" + crs.getString("veh_id") + "\">Add Kms</a></li>"
									+ "<li role=presentation><a href=\"../service/jobcard-list.jsp?jc_veh_id=" + crs.getString("veh_id") + "\">List Job Cards</a></li>"
									// Str.append("<br/><a href=\"../insurance/insurance-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("&veh_branch_id=")
									// .append(crs.getString("veh_branch_id")).append("\">Add Insurance</a>");
									+ "<li role=presentation><a href=\"../service/veh-ownership-update.jsp?add=yes&veh_id=" + crs.getString("veh_id") + "\">Add Ownership</a></li>"
									+ "<li role=presentation><a href=\"../service/jobcard-update.jsp?add=yes&veh_id=" + crs.getString("veh_id") + "&branch_id=" + crs.getString("branch_id")
									+ "\">Add Job Card</a></li>"
									+ "<li role=presentation><a href=\"../service/jobcard-quickadd.jsp?add=yes&veh_id=" + crs.getString("veh_id") + "\">Quick Add Job Card</a></li>"
									+ "<li role=presentation><a href=\"../customer/customer-contact-update.jsp?Add=yes&customer_id=" + crs.getString("customer_id") + "\">Add New Contact</a></li>"
									+ "<li role=presentation><a href=\"../service/ticket-add.jsp?add=yes&veh_id=" + crs.getString("veh_id") + "\">Add Ticket</a></li>";
							// Str.append("<br/><a href=\"../service/pickup-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Pickup</a>");
							// Str.append("<br/><a href=\"../service/booking-list.jsp?booking_veh_id=").append(crs.getString("veh_id")).append("\">List Bookings</a>");
							// Str.append("<br/><a href=\"../service/booking-update.jsp?add=yes&veh_id=").append(crs.getString("veh_id")).append("\">Add Booking</a>");
						}
						update_info += "</ul></div></center></div>";
						Str.append(update_info);
						Str.append("</td>\n</tr>\n");
					}
					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
					// // Add News Letter Link
					// LinkAddPage += "<br/><a href=\"../portal/news-letter-enquiry.jsp?target=vehicles\" target=_blank>Send News Letter</a>";
				} else {
					Str.append("<br/><br/><font color=\"red\"><center><b>No Vehicle(s) found!</b></center></font>");
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
		brand_id = RetrunSelectArrVal(request, "dr_brand");
		brand_ids = request.getParameterValues("dr_brand");
		// SOP("brand_id-----------" + brand_id);
		// SOP("brand_ids-----------" + brand_ids);
		model_id = RetrunSelectArrVal(request, "dr_model_id");
		model_ids = request.getParameterValues("dr_model_id");

		if (!brand_id.equals("")) {
			StrSearch = StrSearch + " AND branch_brand_id IN (" + brand_id + ")";
		}
		if (!model_id.equals("")) {
			StrSearch = StrSearch + " AND model_id IN (" + model_id + ")";
		}
		StringBuilder Str = new StringBuilder();
		// Str.append("<table class=\"footable-loaded footable\">");
		// Str.append("<tr>\n");
		Str.append("<div class=\"container-fluid\">");
		Str.append("<div class=\"form-element4\"></div>");
		Str.append("<div class=\"form-element2\">");
		Str.append("<label>Brands:</label>");
		Str.append("<div><select name='dr_brand' multiple='multiple' class='form-control multiselect-dropdown'  id='dr_brand' ");
		Str.append(" onChange=PopulateModel()>");
		Str.append(mischeck.PopulatePrincipal(brand_ids, comp_id, request));
		Str.append("</select></div></div>");

		Str.append("<div class=\"form-element2\">");
		Str.append("<label>Model:</label>");
		Str.append("<div id=modelHint>");
		Str.append(mischeck.PopulateModels(brand_id, model_ids, "", comp_id, request));
		Str.append("</div></div>");

		Str.append("</div>");
		// Str.append("</tr>\n");
		// Str.append("</table>");
		return Str.toString();

	}
}
