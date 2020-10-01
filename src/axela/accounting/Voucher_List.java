package axela.accounting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Voucher_List extends Connect {

	public String LinkHeader = "";
	public String LinkListPage = "voucher-list.jsp";
	public String LinkExportPage = "";
	public String LinkFilterPage = "";
	public String LinkAddPage = "";
	public String ExportPerm = "";
	public String emp_id = "0";
	public String branch_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String search = "";
	// public String comp_id = "0";

	public String SqlJoin = "";
	public String msg = "";
	public String StrSql = "";
	public String StrSearch = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String PageCurrents = "";
	public String QueryString = "";
	public String orderby = "";
	public String ordertype = "";
	public String ordernavi = "";
	public String strq = "";
	public String accessories = "";
	public String voucher_id = "0", voucherclass_id = "0", vouchertype_id = "0";
	public String voucher_enquiry_id = "0", voucher_quote_id = "0";
	public String voucher_invoice_id = "0", voucher_so_id = "0", voucher_vehstock_id = "0", voucher_preorder_id = "0", voucher_jc_id = "0";
	public String voucher_delnote_id = "0", voucher_grn_id = "0";
	public String voucher_git_id = "0";
	public String voucher_po_id = "0", voucher_dcr_request_id = "0", voucher_dcr_id = "0";
	public String voucher_grn_return_id = "0";
	public String entity_id = "0";
	public String vouchertype_name = "Voucher";
	public String voucher_customer_id = "0";
	public String BranchAccess = "", ExeAccess = "";
	public String vouchertype_label = "", voucherclass_file = "";
	public String all = "";
	public String advSearch = "";
	public String authorized_pending = "";

	// receiptdash related variable
	public String receiptdash = "";
	public String paymode_id = "0", starttime = "", endtime = "", exe_emp_id = "", brand_id = "", region_id = "", voucher_authorize = "";
	// -----------------------------

	Map<Integer, Object> prepmap = new HashMap<>();
	public int prepkey = 1;
	public Smart SmartSearch = new Smart();
	public String smartarr[][] = {};
	public String smart = "", access = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {

			// CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request) + ""));
				ExportPerm = ReturnPerm(comp_id, "emp_export_access", request);
				branch_id = CNumeric(GetSession("emp_branch_id", request) + "");
				PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
				QueryString = PadQuotes(request.getQueryString());
				orderby = PadQuotes(request.getParameter("orderby"));
				accessories = PadQuotes(request.getParameter("accessories"));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				ordertype = PadQuotes(request.getParameter("ordertype"));
				search = PadQuotes(request.getParameter("search"));
				advSearch = PadQuotes(request.getParameter("advsearch_button"));
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
				voucherclass_id = CNumeric(PadQuotes(request.getParameter("voucherclass_id")));
				vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));
				voucher_quote_id = CNumeric(PadQuotes(request.getParameter("voucher_quote_id")));
				voucher_invoice_id = CNumeric(PadQuotes(request.getParameter("voucher_invoice_id")));
				voucher_so_id = CNumeric(PadQuotes(request.getParameter("voucher_so_id")));
				voucher_vehstock_id = CNumeric(PadQuotes(request.getParameter("voucher_vehstock_id")));
				voucher_preorder_id = CNumeric(PadQuotes(request.getParameter("voucher_preorder_id")));
				voucher_jc_id = CNumeric(PadQuotes(request.getParameter("voucher_jc_id")));
				voucher_delnote_id = CNumeric(PadQuotes(request.getParameter("voucher_delnote_id")));
				voucher_git_id = CNumeric(PadQuotes(request.getParameter("voucher_git_id")));
				voucher_grn_id = CNumeric(PadQuotes(request.getParameter("voucher_grn_id")));
				voucher_po_id = CNumeric(PadQuotes(request.getParameter("voucher_po_id")));
				voucher_dcr_request_id = CNumeric(PadQuotes(request.getParameter("voucher_dcr_request_id")));
				voucher_dcr_id = CNumeric(PadQuotes(request.getParameter("voucher_dcr_id")));
				voucher_grn_return_id = CNumeric(PadQuotes(request.getParameter("voucher_grn_return_id")));
				voucher_customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
				smart = PadQuotes(request.getParameter("smart"));
				receiptdash = PadQuotes(request.getParameter("receiptdash"));
				authorized_pending = PadQuotes(request.getParameter("authorized_pending"));
				if (receiptdash.equals("yes")) {

					paymode_id = CNumeric(PadQuotes(request.getParameter("paymode_id")));
					starttime = PadQuotes(request.getParameter("starttime"));
					endtime = PadQuotes(request.getParameter("endtime"));
					exe_emp_id = PadQuotes(request.getParameter("exe_emp_id"));
					brand_id = PadQuotes(request.getParameter("brand_id"));
					region_id = PadQuotes(request.getParameter("region_id"));
					branch_id = PadQuotes(request.getParameter("branch_id"));
					voucher_authorize = PadQuotes(request.getParameter("voucher_authorize"));

					if (!paymode_id.equals("0")) {
						StrSearch += " AND vouchertrans_paymode_id = " + paymode_id;
					}
					if (!starttime.equals("")) {
						StrSearch += " AND SUBSTR(voucher_date,1,8) >= SUBSTR('" + starttime + "',1,8)";
					}
					if (!endtime.equals("")) {
						StrSearch += " AND SUBSTR(voucher_date,1,8) <= SUBSTR('" + endtime + "',1,8)";
					}
					if (!exe_emp_id.equals("")) {
						StrSearch += " AND voucher_emp_id IN (" + exe_emp_id + ")";
					}
					if (!brand_id.equals("")) {
						StrSearch += " AND branch_brand_id IN (" + brand_id + ") ";
					}
					if (!region_id.equals("")) {
						StrSearch += " AND branch_region_id in (" + region_id + ") ";
					}
					if (!branch_id.equals("")) {
						StrSearch += " AND voucher_branch_id IN(" + branch_id + ")";
					}
					if (!voucher_authorize.equals("")) {
						StrSearch += " AND voucher_authorize = " + voucher_authorize;
					}
					StrSearch += ExeAccess + BranchAccess + " AND voucher_active = 1";
				}

				if (authorized_pending != "") {
					if (!authorized_pending.contains("-")) {
						if (authorized_pending.equals("30")) {
							StrSearch += " AND DATEDIFF( CURRENT_DATE(),voucher_date) > " + authorized_pending;
						} else {
							StrSearch += " AND DATEDIFF( CURRENT_DATE(),voucher_date) = " + authorized_pending;
						}
					} else {
						StrSearch += " AND DATEDIFF( CURRENT_DATE(),voucher_date) >= " + authorized_pending.split("-")[0];
						StrSearch += " AND DATEDIFF( CURRENT_DATE(),voucher_date) <= " + authorized_pending.split("-")[1];
					}
					StrSearch += " AND voucher_active = 1 AND voucher_authorize = 0"
							+ " AND voucher_branch_id = " + PadQuotes(request.getParameter("authorizepending_branch_id"));

				}

				if (!(ReturnPerm(comp_id, "emp_acc_voucher_access", request).equals("1")))
				{
					if (vouchertype_id.equals("1")) {
						access = "emp_stock_adjustment_access";
					} else if (vouchertype_id.equals("2")) {
						access = "emp_stock_transfer_access";
					} else if (vouchertype_id.equals("3")) {
						access = "emp_item_conversion_access";
					} else if (vouchertype_id.equals("5")) {
						access = "emp_acc_quote_access, emp_sales_quote_access";
					} else if (vouchertype_id.equals("6")) {
						access = "emp_acc_sales_invoice_access";
					} else if (vouchertype_id.equals("7")) {
						access = "emp_acc_bill_access";
					} else if (vouchertype_id.equals("9")) {
						access = "emp_acc_receipt_access";
					} else if (vouchertype_id.equals("10")) {
						access = "emp_acc_credit_note_access";
					} else if (vouchertype_id.equals("11")) {
						access = "emp_acc_debit_note_access";
					} else if (vouchertype_id.equals("12")) {
						access = "emp_acc_purchase_order_access";
					} else if (vouchertype_id.equals("15")) {
						access = "emp_acc_payment_access";
					} else if (vouchertype_id.equals("16")) {
						access = "emp_acc_expense_access";
					} else if (vouchertype_id.equals("18")) {
						access = "emp_acc_journal_access";
					} else if (vouchertype_id.equals("19")) {
						access = "emp_acc_contra_access";
					} else if (vouchertype_id.equals("20")) {
						access = "emp_acc_grn_access";
					} else if (vouchertype_id.equals("21")) {
						access = "emp_acc_purchase_invoice_access";
					} else if (vouchertype_id.equals("23")) {
						access = "emp_acc_sales_return_access";
					} else if (vouchertype_id.equals("24")) {
						access = "emp_acc_purchase_return_access";
					} else if (vouchertype_id.equals("25")) {
						access = "emp_acc_delivery_note_access";
					} else if (vouchertype_id.equals("27")) {
						access = "emp_acc_preorder_access,emp_preorder_access,emp_sales_order_add,emp_stock_add";
					}
					else if (all.equals("yes")) {
						access = "emp_acc_voucher_access";
					}
				}
				else {
					access = "emp_acc_voucher_access";
				}
				CheckPerm(comp_id, access, request, response);

				LinkExportPage = "voucher-export.jsp?vouchertype_id=" + vouchertype_id + "";

				if (!vouchertype_id.equals("0")) {
					StrSql = "SELECT vouchertype_name, vouchertype_label, voucherclass_file"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_type"
							+ " INNER JOIN  " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
							+ " WHERE vouchertype_id =" + vouchertype_id;
					// SOP("StrSql===voucher_list==" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					try {
						while (crs.next()) {
							vouchertype_name = crs.getString("vouchertype_name");
							vouchertype_label = crs.getString("vouchertype_label");
							voucherclass_file = crs.getString("voucherclass_file");
						}
						crs.close();
					} catch (Exception ex) {
						SOPError("Axelaauto===" + this.getClass().getName());
						SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					}
					LinkHeader = "<a href=../portal/home.jsp>Home</a>";
					if (vouchertype_id.equals("3")) {
						LinkHeader += " &gt; <a href=../inventory/index.jsp>Inventory</a>";
					} else if (accessories.equals("yes")) {
						LinkHeader += " &gt; <a href=../accessories/index.jsp>Accessories</a>";
					}
					else {
						LinkHeader += " &gt; <a href=../accounting/index.jsp>Accounting</a>";
					}
					LinkHeader += " &gt; <a href=../accounting/voucher-list.jsp?all=yes"
							+ "&voucherclass_id="
							+ voucherclass_id
							+ "&vouchertype_id="
							+ vouchertype_id
							+ ">List "
							+ vouchertype_name + "s</a>:";

					// LinkAddPage = PadQuotes(GetSession("accaddlink", request));
					if (vouchertype_id.equals("3")) {
						LinkAddPage = "<a href=../inventory/"
								+ voucherclass_file + "-update.jsp?add=yes"
								+ "&voucherclass_id=" + voucherclass_id
								+ "&vouchertype_id=" + vouchertype_id + ">Add "
								+ vouchertype_name + "</a>";
					}
					// LinkAddpage page will be empty when u go and List it from List Sales Orders For Receipts And Invoices
					else if (vouchertype_id.equals("9") && !voucher_so_id.equals("0")) {
						LinkAddPage = "";
					} else if (vouchertype_id.equals("6") && !voucher_so_id.equals("0")) {
						LinkAddPage = "";
					} else {
						if (vouchertype_id.equals("9") || vouchertype_id.equals("15")) {
							LinkAddPage += "<a href=../accounting/"
									+ voucherclass_file + "-update.jsp?add=yes"
									+ "&stockadjustment=yes"
									+ "&voucherclass_id=" + voucherclass_id
									+ "&vouchertype_id=" + vouchertype_id + ">Add " + vouchertype_name + "</a>";
						} else if (vouchertype_id.equals("6") || vouchertype_id.equals("6") || vouchertype_id.equals("20") || vouchertype_id.equals("24") || vouchertype_id.equals("12")) {
							LinkAddPage += "<a href=../accounting/"
									+ voucherclass_file + "-update2.jsp?add=yes"
									+ "&stockadjustment=yes"
									+ "&voucherclass_id=" + voucherclass_id
									+ "&vouchertype_id=" + vouchertype_id + ">Add " + vouchertype_name + "</a>";
						} else if (vouchertype_id.equals("7")) {
							// LinkAddPage += "<a href=../accounting/"
							// + voucherclass_file + "-update.jsp?add=yes"
							// + "&stockadjustment=yes"
							// + "&voucherclass_id=" + voucherclass_id
							// + "&vouchertype_id=" + vouchertype_id + ">Add " + vouchertype_name + "</a>";
							LinkAddPage += "<a href=../accounting/"
									+ voucherclass_file + "-update2.jsp?add=yes"
									+ "&stockadjustment=yes"
									+ "&voucherclass_id=" + voucherclass_id
									+ "&vouchertype_id=" + vouchertype_id + ">Add " + vouchertype_name + "</a>";
						} else {
							LinkAddPage = "<a href=../accounting/"
									+ voucherclass_file + "-update2.jsp?add=yes"
									+ "&stockadjustment=yes"
									+ "&voucherclass_id=" + voucherclass_id
									+ "&vouchertype_id=" + vouchertype_id + ">Add "
									+ vouchertype_name + "</a>";
						}
					}
				} else {
					LinkHeader = "<a href=../portal/home.jsp>Home</a>"
							+ " &gt;<a href=../accounting/index.jsp>Accounting</a>"
							+ " &gt;<a href=../accounting/voucher-list.jsp?all=yes>List Vouchers</a>:";
					LinkAddPage = PadQuotes(GetSession("accaddlink", request));
				}

				if (vouchertype_id.equals("23"))
				{
					LinkAddPage = "<a href=../accounting/"
							+ voucherclass_file + "-update.jsp?add=yes"
							+ "&voucherclass_id=" + voucherclass_id
							+ "&vouchertype_id=" + vouchertype_id + ">Add "
							+ vouchertype_name + "</a>";
				}
				if (vouchertype_id.equals("24")) {
					LinkAddPage = "<a href=../accounting/"
							+ voucherclass_file + "-update.jsp?add=yes"
							+ "&voucherclass_id=" + voucherclass_id
							+ "&vouchertype_id=" + vouchertype_id + ">Add "
							+ vouchertype_name + "</a>";
				}
				LinkHeader += "<input name=vouchertype_id type=hidden  id=vouchertype_id value = "
						+ vouchertype_id
						+ " />"
						+ "<input name=voucherclass_id type=hidden  id=voucherclass_id value = "
						+ voucherclass_id + " />";
				BuildSmartArr();

				if (msg.toLowerCase().contains("delete")) {
					StrSearch = " AND voucher_id = 0";
				} else if ("yes".equals(all) && vouchertype_id.equals("0")
						&& voucherclass_id.equals("0")) {
					msg = msg + "<br>Results for all Voucher(s)!";
					StrSearch += " AND voucher_id > 0";
				} else if ("yes".equals(all) && !vouchertype_id.equals("0")) {
					msg = msg + "<br>Results for all " + vouchertype_name + "(s)!";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, vouchertype_id);
				} else if ("yes".equals(all) && !voucherclass_id.equals("0")) {
					msg = msg + "<br>Results for all Voucher(s)!";
					StrSearch += " AND vouchertype_voucherclass_id = "
							+ voucherclass_id + "";
				} else if (!(voucher_id.equals("0"))) {
					msg = msg + "<br>Results for Voucher ID = " + voucher_id + "!";
					StrSearch += " AND voucher_id = " + voucher_id;
					// prepmap.put(prepkey++, voucher_id);
				} else if (!(voucher_so_id.equals("0"))) {
					msg = msg + "<br>Results for SO ID = " + voucher_so_id + "!";
					if (vouchertype_id.equals("25") || vouchertype_id.equals("6") || vouchertype_id.equals("18")) {
						StrSearch += " AND voucher_so_id = ?";
						StrSearch += " AND vouchertype_id = ?";
						prepmap.put(prepkey++, voucher_so_id);
						prepmap.put(prepkey++, vouchertype_id);
					} else {
						if (!vouchertype_id.equals("9")) {
							StrSearch += " AND voucher_so_id = ?";
						}
						StrSearch += " AND voucher_customer_id = ?";
						StrSearch += " AND vouchertype_id = ?";
						if (!vouchertype_id.equals("9")) {
							prepmap.put(prepkey++, voucher_so_id);
						}
						prepmap.put(prepkey++, voucher_customer_id);
						prepmap.put(prepkey++, vouchertype_id);
					}
				} else if (!(voucher_preorder_id.equals("0"))) {
					msg = msg + "<br>Results for Pre-Order ID = " + voucher_preorder_id + "!";
					StrSearch += " AND voucher_preorder_id = ?";
					StrSearch += " AND vouchertype_id = ?";

					prepmap.put(prepkey++, voucher_preorder_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (!(voucher_jc_id.equals("0"))) {
					msg = msg + "<br>Results for SO ID = " + voucher_jc_id + "!";
					if (!vouchertype_id.equals("9")) {
						StrSearch += " AND voucher_jc_id = ?";
					}
					StrSearch += " AND voucher_customer_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					if (!vouchertype_id.equals("9")) {
						prepmap.put(prepkey++, voucher_jc_id);
					}
					prepmap.put(prepkey++, voucher_customer_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (!(voucher_invoice_id.equals("0"))) {
					msg = msg + "<br>Results for Sales Invoice ID = " + voucher_invoice_id + "!";
					StrSearch += " AND voucher_invoice_id = ?";
					// StrSearch += " AND voucher_customer_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_invoice_id);
					// prepmap.put(prepkey++, voucher_customer_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (!(voucher_delnote_id.equals("0"))) {
					msg = msg + "<br>Results for DelNote ID = " + voucher_delnote_id + "!";
					StrSearch += " AND voucher_delnote_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_delnote_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (!(voucher_git_id.equals("0"))) {
					msg = msg + "<br>Results for Goods In Transit ID = "
							+ voucher_git_id + "!";
					StrSearch += " AND voucher_git_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_git_id);
					prepmap.put(prepkey++, vouchertype_id);

				} else if (!(voucher_grn_id.equals("0"))) {
					msg = msg + "<br>Results for Receipt Note(GRN) ID = "
							+ voucher_grn_id + "!";
					StrSearch += " AND voucher_grn_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_grn_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (!(voucher_po_id.equals("0"))) {
					msg = msg + "<br>Results for PO ID = " + voucher_po_id + "!";
					StrSearch += " AND voucher_po_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_po_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (!(voucher_dcr_request_id.equals("0"))) {
					msg = msg + "<br>Results for DCR Request ID = " + voucher_dcr_request_id + "!";
					StrSearch += " AND voucher_dcr_request_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_dcr_request_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (!(voucher_dcr_id.equals("0"))) {
					msg = msg + "<br>Results for DCR ID = " + voucher_dcr_id + "!";
					StrSearch += " AND voucher_dcr_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_dcr_id);
					prepmap.put(prepkey++, vouchertype_id);
				}
				else if (!(voucher_grn_return_id.equals("0"))) {
					msg = msg + "<br>Results for GRN Return ID = " + voucher_grn_return_id + "!";
					StrSearch += " AND voucher_grn_return_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_grn_return_id);
					prepmap.put(prepkey++, vouchertype_id);
				}
				else if (!(voucher_invoice_id.equals("0"))) {
					msg = msg + "<br>Results for Invoice ID = " + voucher_invoice_id + "!";
					StrSearch += " AND voucher_id IN (SELECT voucherbal_voucher_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher_bal"
							+ " WHERE voucherbal_trans_id = ?)";
					prepmap.put(prepkey++, voucher_invoice_id);
				} else if (!(voucher_quote_id.equals("0"))) {
					msg = msg + "<br>Results for Quote ID = " + voucher_quote_id + "!";
					StrSearch += " AND voucher_quote_id = ?";
					StrSearch += " AND vouchertype_id = ?";
					prepmap.put(prepkey++, voucher_quote_id);
					prepmap.put(prepkey++, vouchertype_id);
				}
				else if (!voucher_vehstock_id.equals("0")) {
					msg = msg + "<br>Results for Stock ID = " + voucher_vehstock_id + "!";
					StrSearch += " AND voucher_vehstock_id = ?";
					StrSearch += " AND voucher_vehstock_id = ?";
					prepmap.put(prepkey++, voucher_vehstock_id);
					prepmap.put(prepkey++, vouchertype_id);
				} else if (advSearch.equals("Search")) {// for keyword search
					StrSearch = SmartSearch.BuildSmartSql(smartarr, request);
					if (!vouchertype_id.equals("0")) {
						StrSearch += " AND vouchertype_id = ?";
						prepmap.put(prepkey++, vouchertype_id);
						if (StrSearch.equals("")) {
							msg = "Enter Search Text!";
							StrSearch += " AND voucher_id = 0 ";
						} else {
							msg = "Results for Search!";
						}
					} else {
						if (StrSearch.equals("")) {
							msg = "Enter Search Text!";
							StrSearch += " AND voucher_id = 0 ";
						} else {
							msg = "Results for Search!";
						}
					}
				}
				if (!(vouchertype_id.equals("0"))) {
					StrSearch += " AND vouchertype_id = ?";
					// SOP("StrSearch=111==" + StrSearch);
					prepmap.put(prepkey++, vouchertype_id);
				}
				if (!(voucherclass_id.equals("0"))) {
					StrSearch += " AND vouchertype_voucherclass_id = ?";
					// SOP("StrSearch==222=" + StrSearch);
					prepmap.put(prepkey++, voucherclass_id);
				}
				if (smart.equals("yes")) {
					if (!GetSession("voucherstrsql", request).equals("")) {
						StrSearch += GetSession("voucherstrsql", request);
						// SOP("StrSearch==333=" + StrSearch);
					}
				}

				SetSession("voucherstrsql", StrSearch.replace("vouchertype_id = ? AND vouchertype_id = ? AND vouchertype_voucherclass_id = ?",
						"vouchertype_id = " + vouchertype_id + " AND vouchertype_voucherclass_id = " + voucherclass_id), request);
				StrHTML = Listdata();
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

	public String Listdata() {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		// Check PageCurrent is valid for parse int
		if (PageCurrents.equals("0")) {
			PageCurrents = "1";
		}
		PageCurrent = Integer.parseInt(PageCurrents);
		// to know no of records depending on search
		StrSql = "SELECT voucher_id, voucher_no, voucher_amount,"
				+ " COALESCE( voucher_ref_no,'') AS voucher_ref_no,"
				+ " COALESCE(voucher_narration,'') AS voucher_narration,"
				+ " voucher_date, voucher_active, vouchertype_authorize, vouchertype_defaultauthorize,"
				+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
				+ " COALESCE(branch_brand_id,0) AS branch_brand_id,"
				+ " voucher_customer_id, voucher_contact_id,"
				+ " voucher_enquiry_id, voucher_modified_id,"
				+ " voucher_modified_date, voucher_quote_id,"
				+ " voucher_invoice_id, voucher_so_id,"
				+ " voucher_vehstock_id, voucher_preorder_id,"
				+ " voucher_jc_id, voucher_delnote_id, voucher_grn_id,"
				+ " voucher_po_id, voucher_rateclass_id, vouchertype_voucherclass_id,"
				+ " COALESCE(customer_id,'0') AS customer_id,"
				+ " customer_entry_date,"
				+ " COALESCE(customer_name,'') AS customer_name,"
				+ " COALESCE(customer_code,'') AS customer_code,"
				+ " COALESCE(customer_credit_limit, 0.00) AS customercreditlimit,"
				+ " customer_ledgertype, voucher_branch_id, voucher_authorize,"
				+ " COALESCE(title_desc,'') AS title_desc,"
				+ " COALESCE(contact_fname,'') AS contact_fname,"
				+ " COALESCE(contact_lname,'') AS contact_lname,"
				+ " COALESCE(contact_mobile1,'') AS contact_mobile1,"
				+ " COALESCE(contact_mobile2,'') AS contact_mobile2,"
				+ " COALESCE(contact_phone1,'') AS contact_phone1,"
				+ " COALESCE(contact_phone2,'') AS contact_phone2,"
				+ " COALESCE(contact_email1,'') AS contact_email1,"
				+ " COALESCE(contact_email2,'') AS contact_email2,"
				+ " COALESCE(CONCAT(emp_name, ' (', emp_ref_no, ')')) AS emp_name,"
				+ " COALESCE(emp_photo,'') AS emp_photo,"
				+ " COALESCE(emp_sex,'') AS emp_sex,"
				+ " COALESCE(emp_id,'') AS emp_id,"
				+ " vouchertype_id, vouchertype_name,"
				+ " vouchertype_prefix, vouchertype_suffix,"
				+ " branch_invoice_prefix, branch_invoice_suffix,"
				+ " branch_receipt_prefix, branch_receipt_suffix,"
				+ " branch_bill_prefix,"
				+ " vouchertype_email_enable,"
				+ " vouchertype_sms_enable, vouchertype_docs, voucherclass_id, "
				+ " voucherclass_file, voucher_date,"
				+ " COALESCE(if(vouchertype_id = 9,(SELECT customer_ledgertype"
				+ " FROM " + compdb(comp_id) + "axela_customer"
				+ " INNER JOIN  " + compdb(comp_id) + " axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
				+ " WHERE  vouchertrans_voucher_id = voucher_id"
				+ " AND vouchertrans_dc = 1 limit 1),"
				+ " if(vouchertype_id = 15,(SELECT customer_ledgertype"
				+ " FROM " + compdb(comp_id) + " axela_customer"
				+ " INNER JOIN  " + compdb(comp_id) + " axela_acc_voucher_trans ON vouchertrans_customer_id = customer_id"
				+ " WHERE  vouchertrans_voucher_id = voucher_id"
				+ " AND vouchertrans_dc = 0 limit 1),0)), 0) AS paymodeid";
		SqlJoin = " FROM " + compdb(comp_id) + " axela_acc_voucher";
		// if (!paymode_id.equals("0")) {
		SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id";
		// }
		SqlJoin += " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN  " + maindb() + "acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_id = voucher_contact_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " LEFT JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " WHERE 1 = 1";
		if (!paymode_id.equals("0")) {
			SqlJoin += " AND vouchertrans_dc = 1";
		}

		if (receiptdash.equals("")) {
			SqlJoin = SqlJoin + BranchAccess + ExeAccess;
		}

		if (!voucher_customer_id.equals("0")) {
			SqlJoin += " AND voucher_customer_id = " + voucher_customer_id;
		}

		CountSql = " SELECT COUNT(DISTINCT(voucher_id))";

		StrSql += SqlJoin;
		CountSql += SqlJoin;
		// SOP("StrSql======" + StrSql);
		QueryString = QueryString.replaceAll("&orderby=" + orderby, "");
		QueryString = QueryString.replaceAll("&ordertype=" + ordertype, "");
		ordernavi = "voucher-list.jsp?" + QueryString;

		if (ordertype.equals("asc")) {
			ordertype = "desc";
		} else {
			ordertype = "asc";
		}

		if (!StrSearch.equals("")) {
			StrSql += StrSearch + " GROUP BY voucher_id";
		}
		CountSql += StrSearch;
		// SOP("CountSql====" + CountSql);
		// SOP("list====" + StrSql);

		TotalRecords = Integer.parseInt(ExecutePrepQuery(CountSql, prepmap, 0));

		if (TotalRecords != 0) {
			StartRec = ((PageCurrent - 1) * recperpage) + 1;
			EndRec = ((PageCurrent - 1) * recperpage) + recperpage;

			// if limit ie. 10 > totalrecord
			if (EndRec > TotalRecords) {
				EndRec = TotalRecords;
			}
			RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
					+ " of " + TotalRecords + " " + vouchertype_name + "(s)";
			if (QueryString.contains("PageCurrent") == true) {
				QueryString = QueryString.replaceAll("&PageCurrent="
						+ PageCurrent + "", "");
			}
			PageURL = "voucher-list.jsp?" + strq + QueryString
					+ "&PageCurrent=";
			PageCount = (TotalRecords / recperpage);
			if ((TotalRecords % recperpage) > 0) {
				PageCount = PageCount + 1;
			}
			// display on jsp page

			PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
			if (orderby.equals("")) {
				StrSql = StrSql + " order by voucher_id desc ";
			} else {
				StrSql = StrSql + " order by " + orderby + " " + ordertype + " ";
			}
			// StrSql = StrSql + " ORDER BY voucher_id DESC";
			StrSql = StrSql + " LIMIT " + (StartRec - 1) + ", " + recperpage
					+ "";
			try {
				// SOP("StrSql==voucher list=" + StrSql);
				CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
				// SOP(StartRec + "," + ordernavi + "," + ordertype + "," + comp_id + "," + vouchertype_id);
				Str.append(DisplayVoucherList(crs, StartRec, ordernavi, ordertype, comp_id, vouchertype_id, emp_id));
				crs.close();
				// Str.append("</table>\n");
			} catch (Exception ex) {

				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		} else {
			if (!vouchertype_id.equals("0")) {
				RecCountDisplay = "<br><br><br><br><font color=red><b>No "
						+ vouchertype_name + "(s) Found!</b></font><br><br>";
			} else {
				RecCountDisplay = "<br><br><br><br><font color=red><b>No Voucher(s) Found!</b></font><br><br>";
			}
		}
		return Str.toString();
	}
	public void BuildSmartArr() {
		ArrayList<String[]> list = new ArrayList<String[]>();
		list.add(new String[]{"Keyword", "text", "keyword_arr"});
		// list.add(new String[]{vouchertype_name, "text", "vouchertype_name"});
		list.add(new String[]{vouchertype_name + " ID", "numeric", "voucher_id"});
		if (vouchertype_id.equals("6")) {
			list.add(new String[]{vouchertype_name + " No.", "text", "concat(branch_invoice_prefix, voucher_no, branch_invoice_suffix)"});
		}
		else if (vouchertype_id.equals("9")) {
			list.add(new String[]{vouchertype_name + " No.", "text", "concat(branch_receipt_prefix, voucher_no, branch_receipt_suffix)"});
		}
		else {
			list.add(new String[]{vouchertype_name + " No.", "text", "concat(vouchertype_prefix, voucher_no, vouchertype_suffix)"});
		}
		list.add(new String[]{"SO ID", "numeric", "voucher_so_id"});
		list.add(new String[]{"JC ID", "numeric", "voucher_jc_id"});
		list.add(new String[]{vouchertype_name + " Date", "date", "voucher_date"});
		list.add(new String[]{"Active", "boolean", "voucher_active"});
		list.add(new String[]{"Cheque No.", "text", "voucher_id IN (SELECT vouchertrans_voucher_id FROM " + compdb(comp_id) + " axela_acc_voucher_trans WHERE vouchertrans_cheque_no "});// cheque
		list.add(new String[]{"Cheque Date", "date", "voucher_id IN (SELECT vouchertrans_voucher_id FROM " + compdb(comp_id) + " axela_acc_voucher_trans WHERE vouchertrans_cheque_date "});
		list.add(new String[]{"Cheque Bank", "text", "voucher_id IN (SELECT vouchertrans_voucher_id FROM " + compdb(comp_id) + " axela_acc_voucher_trans WHERE vouchertrans_cheque_bank "});
		list.add(new String[]{"Cheque Branch", "text", "voucher_id IN (SELECT vouchertrans_voucher_id FROM " + compdb(comp_id) + " axela_acc_voucher_trans WHERE vouchertrans_cheque_branch "});

		list.add(new String[]{"Reference No.", "text", "voucher_ref_no"});
		// vouchertype
		list.add(new String[]{"Voucher Type ID", "numeric", "vouchertype_id"});
		list.add(new String[]{"Voucher Type Name", "text", "vouchertype_name"});
		list.add(new String[]{"Item Group", "text", "voucher_id IN (SELECT vouchertrans_voucher_id FROM " + compdb(comp_id) + " axela_acc_voucher_trans "
				+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id)
				+ " axela_inventory_item ON item_id = vouchertrans_item_id AND vouchertrans_rowcount !=0 AND vouchertrans_option_id = 0 AND vouchertrans_item_id !=0"
				+ " LEFT JOIN " + compdb(comp_id) + " axela_inventory_item_group ON itemgroup_id = item_itemgroup_id"
				+ " WHERE itemgroup_name"});
		list.add(new String[]{"Item Name", "text", "voucher_id IN (SELECT vouchertrans_voucher_id FROM " + compdb(comp_id) + " axela_acc_voucher_trans "
				+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id)
				+ " axela_inventory_item ON item_id = vouchertrans_item_id AND vouchertrans_rowcount !=0 AND vouchertrans_option_id = 0 AND vouchertrans_item_id !=0"
				+ " WHERE"
				+ " item_name "});
		list.add(new String[]{"Ledger ID", "numeric", "customer_id"});
		list.add(new String[]{"Ledger", "text", "customer_name"});
		list.add(new String[]{"Narration", "text", "voucher_narration"});
		list.add(new String[]{"Branch ID", "numeric", "branch_id"});
		list.add(new String[]{"Branch Name", "text", "branch_name"});
		list.add(new String[]{"Customer ID", "numeric", "customer_id"});
		list.add(new String[]{"Customer Code", "numeric", "customer_code"});
		list.add(new String[]{"Customer Name", "text", "customer_name"});
		list.add(new String[]{"Zone", "text", "customer_zone_id IN (SELECT zone_id FROM  " + compdb(comp_id) + " axela_zone WHERE zone_name "});
		list.add(new String[]{"Contact ID", "numeric", "contact_id"});
		list.add(new String[]{"Contact Name", "text", "CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"});
		list.add(new String[]{"Contact Mobile", "text", "CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))"});
		list.add(new String[]{"Contact Email", "text", "CONCAT(contact_email1, contact_email2)"});
		list.add(new String[]{"Amount", "numeric", "voucher_amount"});
		list.add(new String[]{"Location", "text", "voucher_id IN (SELECT vouchertrans_voucher_id"
				+ " FROM " + compdb(comp_id) + " axela_acc_voucher_trans"
				+ " INNER JOIN  " + compdb(comp_id) + " axela_inventory_location ON location_id = vouchertrans_location_id"
				+ " WHERE location_name "});
		list.add(new String[]{"Executive", "text", "emp_name"});
		list.add(new String[]{"Entry By", "text", "voucher_entry_id IN (SELECT emp_id FROM " + compdb(comp_id) + " axela_emp WHERE emp_name "});
		list.add(new String[]{"Entry Date", "date", "voucher_entry_date"});
		list.add(new String[]{"Modified By", "text", "voucher_modified_id IN (SELECT emp_id FROM " + compdb(comp_id) + " axela_emp WHERE emp_name "});
		list.add(new String[]{"Modified Date", "date", "voucher_modified_date"});
		smartarr = list.toArray(new String[list.size()][2]);
		list.clear();
	}

}
