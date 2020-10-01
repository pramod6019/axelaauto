package axela.inventory;
/*
 *@author GuruMurthy TS 15 FEB 2013
 */
//@Bhagwan Singh 16 feb 2013

import java.io.IOException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Stock_List extends Connect {

	public String LinkHeader = "<a href=\"../portal/home.jsp\">Home</a>"
			+ " &gt; <a href=\"../inventory/index.jsp\">Inventory</a>"
			+ " &gt; <a href=\"../inventory/stock.jsp\">Stock</a>"
			+ " &gt; <a href=\"stock-list.jsp?all=yes\">List Stocks</a>:";
	public String LinkExportPage = "stock-export.jsp?smart=yes"; // &target=" + Math.random() + "";
	public String LinkAddPage = "<a href=\"stock-update.jsp?add=yes\">Add Stock...</a>";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String msg = "";
	public String PageURL = "";
	public String StrSql = "";
	public String CountSql = "";
	public String SqlJoin = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String BranchAccess = "";
	public String QueryString = "";
	public String all = "";
	public String smart = "";
	public String advSearch = "";
	public String vehstock_id = "0", so_delivered_date = "";
	static DecimalFormat deci = new DecimalFormat("#.###");
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {
			{"Keyword", "text", "keyword_arr"},
			{"Stock ID", "numeric", "vehstock_id"},
			{"Branch ID", "numeric", "vehstock_branch_id"},
			{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"},
			{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"},
			{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"},
			{"Stock Comm. No.", "text", "vehstock_comm_no"},
			{"Stock Status", "text", "vehstock_status_id IN (SELECT status_id FROM compdb.axela_vehstock_status WHERE status_name"},
			{"Stock Engine No.", "text", "vehstock_engine_no"},
			{"Stock Chassis Prefix", "text", "vehstock_chassis_prefix"},
			{"Stock Chassis No.", "text", "vehstock_chassis_no"},
			{"FASTag", "text", "vehstock_fastag"},
			{"Item Name", "text", "item_name"},
			{"Model Name", "text", "model_name"},
			{"Stock Invoice No", "text", "vehstock_invoice_no"},
			{"Stock Ordered Date", "date", "vehstock_ordered_date"},
			{"Stock Invoice Date", "date", "vehstock_invoice_date"},
			{"Stock Invoice Amount", "numeric", "vehstock_invoice_amount"},
			{"Stock DMS Date", "date", "vehstock_dms_date"},
			{"Stock PDI Date", "date", "vehstock_pdi_date"},
			{"Stock Ex Price", "numeric", "vehstock_ex_price"},
			{"Delivery Status", "text", "vehstock_delstatus_id IN (SELECT delstatus_id FROM compdb.axela_sales_so_delstatus WHERE delstatus_name"},
			{"SO ID", "numeric", "so_id"},
			{"SO Date", "date", "so_date"},
			{"Delivered", "boolean", "IF((so_delivered_date != ''), 1, 0)"},
			// {"Nadcon Date", "date", "vehstock_nadcon_date"},
			{"Retail Date", "date", "so_retail_date"},
			{"Delivered Date", "date", "so_delivered_date"},
			{"Branch Name", "text", "branch_name"},
			{"Notes", "text", "vehstock_notes"},
			{"Entry By", "text", "vehstock_entry_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Entry Date", "date", "vehstock_entry_date"},
			{"Modified By", "text", "vehstock_modified_id IN (SELECT emp_id FROM compdb.axela_emp WHERE emp_name"},
			{"Modified Date", "date", "vehstock_modified_date"}
	};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				all = PadQuotes(request.getParameter("all"));
				BranchAccess = GetSession("BranchAccess", request);
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				// SOP("QueryString====" + QueryString);
				msg = PadQuotes(request.getParameter("msg"));
				smart = PadQuotes(request.getParameter("smart"));
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND vehstock_id = 0";
				} else if ("yes".equals(all)) {
					msg = "Results for all Stock!";
					StrSearch += " AND vehstock_id > 0"
							+ " AND so_delivered_date = ''";
				} else if (all.equals("recent")) {
					msg = "Recent Stock!";
					StrSearch += " AND vehstock_id > 0";
				} else if (!vehstock_id.equals("0")) {
					msg += "<br>Results for Stock ID =" + vehstock_id;
					StrSearch += " AND vehstock_id = " + vehstock_id + "";
				} else if (advSearch.equals("Search")) // for keyword search
				{
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (StrSearch.equals("")) {
						msg = "Enter search text!";
						StrSearch += " AND vehstock_id = 0";
					} else {
						msg = "Results for Search!";
					}
				} else if ("yes".equals(smart)) {
					msg += "<br>Results of Search!";
					if (!GetSession("stockstrsql", request).equals("")) {
						StrSearch += GetSession("stockstrsql", request);
					}
				}
				// SOP("StrSearch=123==" + StrSearch);
				if (!StrSearch.equals("")) {
					SetSession("stockstrsql", StrSearch, request);
				}
				StrSearch += BranchAccess;
				StrHTML = ListData();
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
		CachedRowSet crs = null;
		int TotalRecords = 0;
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		StringBuilder Str = new StringBuilder();
		StringBuilder customer_info = new StringBuilder();
		String update_info = "";
		if (!msg.equals("")) {
			try {
				if (PageCurrents.equals("0")) {
					PageCurrents = "1";
				}

				PageCurrent = Integer.parseInt(PageCurrents);
				StrSql = "SELECT vehstock_id, vehstock_branch_id,"
						// + " vehstock_comm_no,"
						+ " vehstock_invoice_date, vehstock_dms_date, vehstock_pdi_date,"
						+ " COALESCE(vehstock_invoice_amount, '0') AS vehstock_invoice_amount,"
						+ " COALESCE(vehstock_principalsupport, '0') AS vehstock_principalsupport,"
						+ " vehstock_blocked, branch_rateclass_id,"
						+ " COALESCE(vehstockpriority_name, '') AS vehstockpriority_name, vehstock_stockpriority_id,"
						+ " COALESCE(IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name), '') AS item_name,"
						+ " COALESCE(so_id, '') AS so_id, COALESCE(so_date, '') AS so_date, COALESCE(so_delivered_date, '') AS so_delivered_date,"
						+ " COALESCE(so_retail_date, '') AS so_retail_date,"
						+ " COALESCE(CONCAT(customer_name, ' (', customer_id, ')'), '') AS customer,"
						+ " COALESCE(customer_id, '') AS customer_id,"
						+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname,"
						+ " COALESCE(vehstocklocation_name, '') AS vehstocklocation_name,"
						+ " branch_id, vehstock_ex_price, vehstock_item_id,"
						+ " vehstock_chassis_prefix, vehstock_delstatus_id,"
						+ " vehstock_comm_no, vehstock_chassis_no, vehstock_engine_no,"
						+ " vehstock_fastag,"
						+ " COALESCE(CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname), '') AS contact_name,"
						+ " COALESCE(contact_id, '') AS contact_id,"
						+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
						+ " COALESCE(contact_mobile2, '') AS contact_mobile2,"
						+ " COALESCE(contact_email1, '') AS contact_email1,"
						+ " COALESCE(contact_email2, '') AS contact_email2,"
						+ " COALESCE(" + compdb(comp_id) + "axela_sales_so_delstatus.delstatus_name, '') AS delstatus_name,"
						+ " COALESCE(status_name, '') AS status_name,"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE option_optiontype_id = 1"
						+ " AND trans_vehstock_id = vehstock_id), '') AS paintwork,"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE option_optiontype_id = 2"
						+ " AND trans_vehstock_id = vehstock_id), '') AS upholstery,"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE option_optiontype_id = 4"
						+ " AND trans_vehstock_id = vehstock_id), '') AS package,"
						+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
						+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
						+ " WHERE option_optiontype_id = 3"
						+ " AND trans_vehstock_id = vehstock_id), '') AS topcolour";
				// + " AND so_delivered_date ='' ";
				CountSql = "SELECT COUNT(DISTINCT vehstock_id)";

				SqlJoin = " FROM "
						+ "(SELECT vehstock_id, vehstock_branch_id, vehstock_invoice_date, vehstock_principalsupport, vehstock_blocked,"
						+ " vehstock_ex_price, vehstock_item_id, vehstock_chassis_prefix, vehstock_delstatus_id,"
						+ " vehstock_comm_no, vehstock_chassis_no, vehstock_engine_no, vehstock_fastag, vehstock_vehstocklocation_id, vehstock_status_id,"
						+ " vehstock_stockpriority_id, vehstock_invoice_no,vehstock_ordered_date,vehstock_invoice_amount,vehstock_notes,"
						+ " vehstock_dms_date, vehstock_pdi_date, vehstock_entry_date,vehstock_modified_date,"
						+ " COALESCE (so_customer_id,0) AS so_customer_id,"
						+ " COALESCE (so_contact_id,0) AS so_contact_id, COALESCE (so_id, '') AS so_id,"
						+ " COALESCE (so_date, '') AS so_date, COALESCE (so_delivered_date, '') AS so_delivered_date,"
						+ " COALESCE (so_retail_date, '') AS so_retail_date"
						+ " FROM " + compdb(comp_id) + "axela_vehstock"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id"
						+ " AND so_active = 1 GROUP BY vehstock_id ) AS stock "

						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = vehstock_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_location ON vehstocklocation_id = vehstock_vehstocklocation_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_status ON status_id = vehstock_status_id"
						// + " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_vehstock_id = vehstock_id AND so_active = 1 "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_priority ON vehstockpriority_id = vehstock_stockpriority_id"
						+ " WHERE 1 = 1";

				StrSql += SqlJoin;
				CountSql += SqlJoin;

				if (!StrSearch.equals("")) {
					StrSql += StrSearch + " GROUP BY vehstock_id"
							+ " ORDER BY vehstock_id DESC";
					CountSql += StrSearch;
				}
				// SOP("StrSql====" + CountSql);
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

					RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec) + " of " + TotalRecords + " Stock";
					if (QueryString.contains("PageCurrent") == true) {
						QueryString = QueryString.replaceAll("&PageCurrent=" + PageCurrent + "", "");
					}

					PageURL = "stock-list.jsp?" + QueryString + "&PageCurrent=";
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

					SOP("StrSql===vehstock_id------------- " + StrSql);
					int count = StartRec - 1;
					Str.append("<div class=\"table-bordered\">\n");
					Str.append("\n<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<tr align=\"center\">\n");
					Str.append("<th data-hide=\"phone, tablet\">#</th>\n");
					Str.append("<th data-toggle=\"true\">ID</th>\n");
					// Str.append("<th>Comm. No.</th>\n");
					Str.append("<th >Product</th>\n");
					Str.append("<th data-hide=\"phone\">Invoice Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Delivery Status</th>\n");
					Str.append("<th data-hide=\"phone\">Location</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">SO</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Customer</th>\n");
					Str.append("<th>Branch</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Actions</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count++;
						Str.append("<tr>");
						Str.append("<td valign=top align=center >").append(count).append("</td>\n");// 1
						Str.append("<td valign=top align=center><a href='../inventory/stock-dash.jsp?vehstock_id=")
								.append(crs.getString("vehstock_id")).append("'>").append(crs.getString("vehstock_id")).append("</a>");
						if (!crs.getString("vehstock_stockpriority_id").equals("0")) {
							Str.append("<br><font color=\"#ff0000\">").append(crs.getString("vehstockpriority_name")).append("</font>");
						}
						if (crs.getString("vehstock_blocked").equals("1")) {
							Str.append("<br><font color=\"#ff0000\">Blocked</font>");
						}
						Str.append("</td>\n");// 2
						// Str.append("<td valign=top align=left nowrap>").append(crs.getString("vehstock_comm_no"));
						// if (!crs.getString("vehstock_stockpriority_id").equals("0")) {
						// Str.append("<br><font color=\"#ff0000\">").append(crs.getString("stockpriority_name")).append("</font>");
						// }
						//
						// if (crs.getString("vehstock_blocked").equals("1")) {
						// Str.append("<br><font color=\"#ff0000\">Blocked</font>");
						// }
						//
						// Str.append("</td>\n");
						Str.append("<td valign=top align=left><a href=\"inventory-item-list.jsp?item_id=").append(crs.getString("vehstock_item_id")).append(" \">");
						Str.append(crs.getString("item_name")).append(" (").append(crs.getString("vehstock_item_id")).append(")</a><br>");
						if (!crs.getString("paintwork").equals("")) {
							Str.append("Paintwork: ").append(crs.getString("paintwork")).append("<br>");
						}

						if (!crs.getString("upholstery").equals("")) {
							Str.append("Upholstery: ").append(crs.getString("upholstery")).append("<br>");
						}

						if (!crs.getString("package").equals("")) {
							Str.append("Package: ").append(crs.getString("package")).append("<br>");
						}

						if (!crs.getString("topcolour").equals("")) {
							Str.append("Top Colour: ").append(crs.getString("topcolour")).append("<br>");
						}

						if (!crs.getString("status_name").equals("")) {
							Str.append("Status: ").append(crs.getString("status_name")).append("<br>");
						}

						if (!crs.getString("vehstock_comm_no").equals("") || !crs.getString("vehstock_comm_no").equals("0")) {
							Str.append("Comm. No.: ").append(crs.getString("vehstock_comm_no")).append("<br>");
						}

						// if (!crs.getString("vehstock_comm_no").equals("")) {
						// Str.append("Comm. No.: ").append(crs.getString("vehstock_comm_no")).append("<br>");
						// }
						if (!crs.getString("vehstock_engine_no").equals("")) {
							Str.append("Engine No.: ").append(crs.getString("vehstock_engine_no")).append("<br>");
						}

						if (!crs.getString("vehstock_chassis_prefix").equals("")) {
							Str.append("Chassis Prefix.: ").append(crs.getString("vehstock_chassis_prefix")).append("<br>");
						}

						if (!crs.getString("vehstock_chassis_no").equals("")) {
							Str.append("Chassis No.: ").append(crs.getString("vehstock_chassis_no")).append("<br>");
						}

						if (!crs.getString("vehstock_invoice_amount").equals("0")) {
							Str.append("Invoice Amount: ").append(IndFormat(deci.parse(crs.getString("vehstock_invoice_amount")) + "")).append("<br>");
						}

						if (!crs.getString("vehstock_principalsupport").equals("0")) {
							Str.append("Principal Support: ").append(IndFormat(deci.parse(crs.getString("vehstock_principalsupport")) + ""));
						}

						Str.append("</td>\n");// 3
						Str.append("<td  valign=top align=center>");
						if (!crs.getString("vehstock_invoice_date").equals("")) {
							Str.append(strToShortDate(crs.getString("vehstock_invoice_date"))).append("<br>");
							Str.append(Math.round(getDaysBetween(crs.getString("vehstock_invoice_date"), ToLongDate(kknow())))).append(" Days");
						}

						Str.append("</td>\n");// 4
						Str.append("<td valign=top align=left>").append(crs.getString("delstatus_name")).append("</td>\n");// 5
						Str.append("<td valign=top align=left>").append(crs.getString("vehstocklocation_name")).append("</td>\n");// 6
						Str.append("<td valign=top align=left>");
						if (!crs.getString("so_id").equals("")) {
							Str.append("<a href=\"../sales/veh-salesorder-list.jsp?so_id=").append(crs.getString("so_id")).append("\">SO ID: ");
							Str.append(crs.getString("so_id")).append("</a>");
						}

						if (!crs.getString("so_date").equals("")) {
							Str.append("<br>").append(strToShortDate(crs.getString("so_date")));
						}
						Str.append("</td>\n");// 7
						// if (!crs.getString("so_retail_date").equals("")) {
						// Str.append("<br>Retail Date</br>").append(strToShortDate(crs.getString("so_retail_date")));
						// }
						// if (!crs.getString("so_delivered_date").equals("")) {
						// Str.append("<br>Delivered Date</br>").append(strToShortDate(crs.getString("so_delivered_date")));
						// }

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

						Str.append(CustomerContactDetailsPopup(crs.getString("customer_id"), crs.getString("customer"), customer_info.toString(), "customer"));

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
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile1"), crs.getString("vehstock_id"), "M"));
						}
						if (!crs.getString("contact_mobile2").equals("")) {
							Str.append(ContactMobilePopup(comp_id, crs.getString("contact_mobile2"), crs.getString("vehstock_id"), "M"));
						}

						Str.append("</td>\n");// 8

						Str.append("<td valign=top align=left >");// 9
						if (branch_id.equals("0")) {
							Str.append("<a href=\"../portal/branch-summary.jsp?branch_id=")
									.append(crs.getString("vehstock_branch_id")).append("\">").append(crs.getString("branchname")).append("</a>");

						}
						Str.append("</td>\n");// 9

						Str.append("<td nowrap>");
						update_info = "<div class='dropdown' style='display: block'><center><div style='right: 4px;' class='btn-group pull-right'><button type=button style='margin: 0' class='btn btn-success'>"
								+ "<i class='fa fa-pencil'></i></button>"
								+ "<ul style='margin-top: -5px;' class='dropdown-content dropdown-menu pull-right'>"
								+ "<li role=presentation><a href=\"stock-update.jsp?update=yes&vehstock_id="
								+ crs.getString("vehstock_id") + " \">Update Stock</a></li>"
								+ "<li role=presentation><a href=\"stock-options.jsp?vehstock_id=" + crs.getString("vehstock_id") + "\">Configure Stock</a></li>";
						if (!crs.getString("vehstock_delstatus_id").equals("6")) {
							update_info += "<li role=presentation><a href=\"stock-gatepass-update.jsp?add=yes&vehstock_id=" + crs.getString("vehstock_id") + "\">Add Gate Pass</a></li>";
						}

						update_info += "<li role=presentation><a href=\"../accounting/so-update.jsp?add=yes&vouchertype_id=27&voucherclass_id=27"
								+ "&dr_voucher_rateclass_id=" + crs.getString("branch_rateclass_id")
								+ "&voucher_vehstock_id=" + crs.getString("vehstock_id")
								+ "&so_branch_id=" + crs.getString("vehstock_branch_id")
								+ "\">Add Pre-Order</a></li>"
								+ "<li role=presentation><a href=\"../accounting/voucher-list.jsp?vouchertype_id=27&voucherclass_id=27"
								+ "&voucher_vehstock_id=" + crs.getString("vehstock_id") + "\">List Pre-Order</a></li>"
								+ "<li role=presentation><a href=\"stock-gatepass-list.jsp?vehstock_id=" + crs.getString("vehstock_id") + "\">List Gate Passes</a></li>";
						update_info += "</ul></div></center></div>";
						Str.append(update_info);
						Str.append("</td>\n");
						Str.append("</tr>\n");
					}

					Str.append("</tbody>\n");
					Str.append("</table>\n");
					Str.append("</div>\n");
					crs.close();
					// return Str.toString();
				} else {
					RecCountDisplay = "<br><br><br><br><font color=\"red\">No Stock found!</font><br><br>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}
}
