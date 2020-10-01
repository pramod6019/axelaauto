package axela.sales;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;
import cloudify.connect.Ecover_WS;

public class SalesOrder_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String name = "";
	public String value = "";
	public String item_id = "0";
	public String checked = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String history_actiontype = "";
	public String customer_details = "";
	public String doc_details = "";
	public String invoice_details = "";
	public String receipt_details = "";
	public String history = "";
	public String profitability = "";
	public String insurance = "";
	public int recperpage = 0;
	public String PageCurrents = "", QueryString = "";
	public String so_cancelreason_id = "0", so_cancel_date = "";
	public String so_id = "0", vehstock_id = "0";
	public String so_branch_id = "0";
	public String branch_branchtype_id = "0";
	public String so_contact_id = "0";
	public String customer_id = "0";
	public String so_date = "";
	public String so_emp_id = "0", so_incentive_emp_id = "0";
	public String so_vehstock_id = "0";
	public String so_stockallocation_time = "";
	public String emp_sales_order_edit = "0", invoice_date = "";
	public String so_fintype_id = "0";
	public String chk_so_delivered_date = "", emp_role_id = "0", so_retail_date = "0";;
	public String branch_brand_id = "0";
	public String so_item_id = "0", so_option_id = "0", so_din_del_location = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		BranchAccess = GetSession("BranchAccess", request);
		ExeAccess = GetSession("ExeAccess", request);
		recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
		emp_id = CNumeric(GetSession("emp_id", request));
		emp_role_id = CNumeric(GetSession("emp_role_id", request));
		if (!comp_id.equals("0")) {
			so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
			item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			checked = PadQuotes(request.getParameter("checked"));
			customer_details = PadQuotes(request.getParameter("customer_details"));
			so_cancel_date = PadQuotes(request.getParameter("so_cancel_date"));
			so_cancelreason_id = PadQuotes(request.getParameter("so_cancel_reason"));
			doc_details = PadQuotes(request.getParameter("doc_details"));
			invoice_details = PadQuotes(request.getParameter("invoice_details"));
			receipt_details = PadQuotes(request.getParameter("receipt_details"));
			history = PadQuotes(request.getParameter("history"));
			profitability = PadQuotes(request.getParameter("profitability"));
			insurance = PadQuotes(request.getParameter("insurance"));
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			QueryString = PadQuotes(request.getQueryString());
			if (ReturnPerm(comp_id, "emp_acc_receipt_add", request).equals("1")) {
				emp_sales_order_edit = "1";
			}
			try {
				StrSql = "SELECT so_id, so_branch_id, branch_branchtype_id, branch_brand_id, so_item_id, so_option_id, so_contact_id, emp_id,"
						+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS customer_exe,"
						+ " so_emp_id, so_retail_date, so_delivered_date, so_din_del_location,"
						+ " so_vehstock_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
						+ " WHERE so_id = " + so_id
						// + BranchAccess + ExeAccess
						+ " GROUP BY so_id"
						+ " ORDER BY so_id DESC";
				// SOP("strsql===-----" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					so_id = crs.getString("so_id");
					so_incentive_emp_id = crs.getString("so_emp_id");
					so_contact_id = crs.getString("so_contact_id");
					so_branch_id = crs.getString("so_branch_id");
					branch_branchtype_id = crs.getString("branch_branchtype_id");
					branch_brand_id = crs.getString("branch_brand_id");
					so_item_id = crs.getString("so_item_id");
					so_option_id = crs.getString("so_option_id");
					chk_so_delivered_date = crs.getString("so_delivered_date");
					vehstock_id = crs.getString("so_vehstock_id");
					so_retail_date = crs.getString("so_retail_date");
					so_din_del_location = crs.getString("so_din_del_location");
				}
				crs.close();

				if (CNumeric(so_id).equals("0")) {
					StrHTML = "Update Permission Denied!";
				}
				if (customer_details.equals("yes")) {
					StrHTML = new Enquiry_Dash_Customer().CustomerDetails(response, customer_id, "", comp_id);
				} else if (doc_details.equals("yes")) {
					StrHTML = new SalesOrder_Dash_Docs().ListDocs(so_id, PageCurrents, recperpage, QueryString, comp_id);
				} else if (invoice_details.equals("yes")) {
					StrHTML = new SalesOrder_Dash_Invoice().ListInvoice(so_id, BranchAccess, ExeAccess, comp_id);
				} else if (receipt_details.equals("yes")) {
					StrHTML = new SalesOrder_Dash_Receipt().ListReceipt(so_id, BranchAccess, ExeAccess, comp_id);
				} else if (history.equals("yes")) {
					StrHTML = new SalesOrder_Dash_History().ListHistoryData(so_id, BranchAccess, ExeAccess, comp_id);
				}
				else if (profitability.equals("yes")) {
					StrHTML = new SalesOrder_Dash_Profitability().ListProfitabilityData(so_id, BranchAccess, ExeAccess, comp_id);
				}
				else if (insurance.equals("yes")) {
					JSONObject output = new JSONObject();
					output.put("so_id", so_id);
					StrHTML = new Ecover_WS().WSRequest(output, "axelaauto-insurance", request);
				}
				else {
					if (ReturnPerm(comp_id, "emp_sales_order_add, emp_sales_order_retail", request).equals("1")) {
						if (comp_id.equals("1011")) {
						}

						if (!(emp_id.equals("1") || emp_id.equals("88")) && !chk_so_delivered_date.equals("")) {
							if (StrHTML.equals("")) {
								StrHTML = "SO Delivered<br>Update Permission Denied!";
							}
						}

						else {
							if (name.equals("txt_so_preownedstock_id")) {
								value = value.replaceAll("nbsp", "&");
								if (!CNumeric(value).equals("0")) {
									StrSql = "SELECT preownedstock_id FROM " + compdb(comp_id) + "axela_preowned_stock"
											+ " WHERE preownedstock_id = " + value + "";
									if (!ExecuteQuery(StrSql).equals(value)) {
										StrHTML = "<font color=\"red\">Invalid Pre-Owned Stock!</font>";
									} else {
										// StrSql = "SELECT variant_preownedmodel_id"
										// + " FROM " + compdb(comp_id) + "axela_preowned"
										// + " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_preowned_id = preowned_id"
										// + " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
										// + " WHERE preownedstock_id = " + value + ""
										// + " AND variant_preownedmodel_id IN ( SELECT variant_preownedmodel_id"
										// + " FROM axela_preowned_variant WHERE variant_id = " + item_id + ")";
										// // if (emp_id.equals("1")) {
										// SOPInfo("StrSql===Dash_check==" + StrSqlBreaker(StrSql));
										// // }
										// if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
										// SOP("111");
										// StrHTML = "<font color=\"red\">Invalid Pre-Owned Stock ID!</font>";
										// } else {
										StrSql = "SELECT so_preownedstock_id FROM " + compdb(comp_id) + "axela_sales_so"
												+ " WHERE so_preownedstock_id = " + value + ""
												+ " AND so_active = 1"
												+ " AND so_id != " + so_id + "";
										if (ExecuteQuery(StrSql).equals(value)) {
											StrHTML = "<font color=\"red\">Pre-Owned Stock is associated with other Sales Order!</font>";
										}
										// }
									}
								}

								if (StrHTML.equals("")) {
									String history_oldvalue = ExecuteQuery("SELECT so_preownedstock_id"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET so_preownedstock_id = '" + CNumeric(value) + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);
									// UpdateOpprSFDate();

									history_actiontype = "Pre-Owned Stock ID";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + so_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Pre-Owned Stock ID updated!";
								}
							} else if (name.equals("txt_vehstock_id")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String soretaildate = PadQuotes(ExecuteQuery("SELECT so_retail_date"
										+ " FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id));

								if ((ReturnPerm(comp_id, "emp_sales_order_stock", request).equals("1")
										&& soretaildate.equals("")) || emp_id.equals("1")) {
									if (!CNumeric(value).equals("0")) {
										StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id = " + value + "";

										so_vehstock_id = CNumeric(ExecuteQuery(StrSql));

										if (so_vehstock_id.equals("0")) {
											StrHTML = "<font color=\"red\">Invalid Stock!</font>";
										} else {

											// StrSql = "SELECT vehstock_item_id FROM "
											// + compdb(comp_id) + "axela_vehstock"
											// + " WHERE vehstock_id = " + value + "";
											StrSql = "SELECT item_model_id"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
													+ " WHERE vehstock_id = " + value + ""
													+ " AND item_model_id IN (SELECT item_model_id"
													+ " FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_id =" + item_id + ")";
											// SOP("StrSql===item_model_id===" + StrSql);
											if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
												StrHTML = "<font color=\"red\">Invalid Stock ID!</font>";
											}
										}
										StrSql = "SELECT so_id"
												+ " FROM " + compdb(comp_id) + "axela_sales_so"
												+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
												+ " WHERE vehstock_id =  " + value + ""
												+ " AND so_active = 1"
												+ " AND so_id != " + so_id + "";

										// SOP("StrSql===111===" + StrSqlBreaker(StrSql));
										String tmpso_id = CNumeric(ExecuteQuery(StrSql));
										if (!tmpso_id.equals(so_id) && !tmpso_id.equals("0")) {
											StrHTML = "<font color=\"red\">Stock ID is associated with other Sales Order!</font>";
										}
									}
									if (!CNumeric(value).equals("0")) {
										StrSql = "SELECT so_id"
												+ " FROM " + compdb(comp_id) + "axela_sales_so"
												+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
												+ " WHERE vehstock_id = " + value + ""
												+ " AND so_active = 1"
												+ " AND so_id != " + so_id + "";
										// SOP("StrSql===222===" + StrSqlBreaker(StrSql));
										// SOP("so_id==111==" + CNumeric(ExecuteQuery(StrSql)));
										// SOP("so_id==222==" + so_id);
										String tmpso_id = CNumeric(ExecuteQuery(StrSql));
										if (!tmpso_id.equals(so_id) && !tmpso_id.equals("0")) {
											StrHTML = "<font color=\"red\">Stock ID is associated with other Sales Order!</font>";
										}
										// checking for Stock Chassis Number
										StrSql = "SELECT vehstock_chassis_no FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id = " + CNumeric(value) + "";
										// SOP("StrSql===" + StrSql);
										if (ExecuteQuery(StrSql).equals("")) {
											StrHTML += " <font color=\"red\">Stock ID does not have Chassis Number!</font>";
										}
										so_stockallocation_time = ExecuteQuery("select so_stockallocation_time from "
												+ compdb(comp_id) + "axela_sales_so where so_id = " + so_id);
										if (so_stockallocation_time.equals("")) {
											so_stockallocation_time = ToLongDate(kknow());
										}
									} else if (value.equals("")) {
										so_vehstock_id = "0";
										so_stockallocation_time = "";
									}

									if (StrHTML.equals("")) {
										String history_oldvalue = "", branch_email1 = "", old_vehstock_id = "0";

										StrSql = "SELECT so_vehstock_id, COALESCE(vehstock_id, 0) AS vehstock_id, branch_email1"
												+ " FROM " + compdb(comp_id) + "axela_sales_so"
												+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
												+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
												+ " WHERE so_id = " + so_id + "";
										ResultSet rset = processQuery(StrSql, 0);

										while (rset.next()) {
											old_vehstock_id = rset.getString("so_vehstock_id");
											history_oldvalue = rset.getString("vehstock_id");
											branch_email1 = rset.getString("branch_email1");
										}
										rset.close();

										if (!CNumeric(so_vehstock_id).equals("0")) {
											if (!old_vehstock_id.equals(so_vehstock_id)) {
												// new
												// Veh_Salesorder_Update().SendEmailToSalesOnStockChange(so_id,
												// CNumeric(value), emp_id,
												// branch_email1);
											}
										}

										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
												+ " SET" + " so_vehstock_id = " + CNumeric(value) + ","
												+ " so_stockallocation_time = '" + ToLongDate(kknow()) + "'"
												+ " WHERE so_id = " + so_id + "";
										updateQuery(StrSql);
										history_actiontype = "Stock ID";

										new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

										StrSql = "INSERT INTO " + compdb(comp_id)
												+ "axela_sales_so_history"
												+ " (history_so_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES"
												+ " ("
												+ so_id + ","
												+ " " + emp_id + ","
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + CNumeric(value) + "')";
										updateQuery(StrSql);
										StrHTML = "Stock ID Updated!";
									}
								} else {
									StrHTML = "<font color=\"red\">Update Permission denied!</font>";
								}
							} else if (name.equals("dr_option_id")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT option_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
										+ " WHERE so_id = " + so_id);
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET" + " so_option_id = " + value + ""
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT option_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
										+ " WHERE so_option_id = " + value);
								history_actiontype = "Colour";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ("
										+ " " + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Colour Updated!";
							} else if (name.equals("txt_so_allot_no")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_allot_no"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET " + " so_allot_no = '" + CNumeric(value) + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Allotment No.";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ("
										+ " " + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Allotment No. Updated!";
							} else if (name.equals("txt_so_booking_amount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_booking_amount"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id =" + so_id);
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET so_booking_amount = '" + CNumeric(value) + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "SO Booking Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " (" + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Booking Amount Updated!";
							}
							else if (name.equals("txt_so_finstatus_disbursed_date")) {
								StrHTML = "";
								if (!value.equals("") && !isValidDateFormatShort(value)) {
									StrHTML = "<font color=\"red\">Enter valid Disbursed Date!</font>";
								} else {
									if (!value.equals("")) {
										StrSql = "SELECT so_date FROM " + compdb(comp_id) + "axela_sales_so" + " WHERE so_id = " + so_id + "";
										so_date = ExecuteQuery(StrSql);
										if (Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
											StrHTML = "<font color=\"red\">Disbursed Date must be greater than or equal to Sales Order Date!</font>";
										}
									}

									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT so_finstatus_disbursed_date"
												+ " FROM " + compdb(comp_id) + "axela_sales_so"
												+ " WHERE so_id = " + so_id);
										if (!history_oldvalue.equals("")) {
											history_oldvalue = strToShortDate(history_oldvalue);
										}

										if (!value.equals("")) {
											StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
													+ " SET so_finstatus_disbursed_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
													+ " WHERE so_id = " + so_id + "";
											updateQuery(StrSql);
										} else {
											StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
													+ " SET so_finstatus_disbursed_date = ''"
													+ " WHERE so_id = " + so_id + "";
											updateQuery(StrSql);
										}

										history_actiontype = "Disbursed Date";

										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
												+ " (history_so_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)" + " VALUES"
												+ " (" + so_id + ","
												+ " " + emp_id + ","
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										StrHTML += "Disbursed Date Updated!";
									}
								}
							} else if (name.equals("txt_so_payment_date")) {
								StrHTML = "";
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");
									if (!isValidDateFormatShort(value)) {
										StrHTML = "<font color=\"red\">Enter Valid Payment Date!</font>";
									} else {
										StrSql = "SELECT so_date "
												+ "FROM " + compdb(comp_id) + "axela_sales_so"
												+ " WHERE so_id = " + so_id + "";
										so_date = ExecuteQuery(StrSql);
										if (Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
											StrHTML = "<font color=\"red\">Payment Date must be greater than or equal to Sales Order Date!</font>";
										} else {
											String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_payment_date"
													+ " FROM " + compdb(comp_id) + "axela_sales_so"
													+ " WHERE so_id = " + so_id));

											StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
													+ " SET" + " so_payment_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
													+ " WHERE so_id = " + so_id + "";
											updateQuery(StrSql);

											history_actiontype = "Payment Date";

											StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
													+ " (history_so_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)"
													+ " VALUES"
													+ " (" + so_id + ","
													+ " " + emp_id + ","
													+ " '" + ToLongDate(kknow()) + "',"
													+ " '" + history_actiontype + "',"
													+ " '" + history_oldvalue + "',"
													+ " '" + value + "')";
											updateQuery(StrSql);
											StrHTML += "Payment Date updated!";
										}
									}
								} else {
									StrHTML = "<font color=\"red\">Enter Payment Date!</font>";
								}
							} else if (name.equals("txt_so_promise_date")) {
								StrHTML = "";
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");

									if (!isValidDateFormatShort(value)) {
										StrHTML = "<font color=\"red\">Enter valid Tentative Delivery Date!</font>";
									} else {
										StrSql = "SELECT so_date"
												+ "	FROM " + compdb(comp_id) + "axela_sales_so"
												+ " WHERE so_id = " + so_id + "";
										so_date = ExecuteQuery(StrSql);

										if (Long.parseLong(so_date) > Long.parseLong(ConvertShortDateToStr(value))) {
											StrHTML = "<font color=\"red\">Tentative Delivery Date must be greater than or equal to Sales Order Date!</font>";
										} else {
											String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_promise_date"
													+ " FROM " + compdb(comp_id) + "axela_sales_so"
													+ " WHERE so_id = " + so_id));

											StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
													+ " SET so_promise_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
													+ " WHERE so_id = " + so_id + "";
											updateQuery(StrSql);

											history_actiontype = "Tentative Delivery Date";

											StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
													+ " (history_so_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)"
													+ " VALUES"
													+ " (" + so_id + ","
													+ " " + emp_id + ","
													+ " '" + ToLongDate(kknow()) + "',"
													+ " '" + history_actiontype + "',"
													+ " '" + history_oldvalue + "',"
													+ " '" + value + "')";
											updateQuery(StrSql);
											StrHTML = StrHTML + "Tentative Delivery Date updated!";
										}
									}
								} else {
									StrHTML = "<font color=\"red\">Enter Tentative Delivery Date!</font>";
								}
							}
							// / Start txt_so_retail_date
							else if (name.equals("txt_so_retail_date")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");

								if (ReturnPerm(comp_id, "emp_sales_order_retail", request).equals("1")) {
									if (StrHTML.equals("")) {
										if (value.equals("") && (emp_id.equals("1") || emp_id.equals("88"))) {

											String history_oldvalue = ExecuteQuery("SELECT so_retail_date"
													+ " FROM " + compdb(comp_id) + "axela_sales_so"
													+ " WHERE so_id = " + so_id);
											if (!history_oldvalue.equals("")) {
												history_oldvalue = strToLongDate(history_oldvalue);
											}

											if (!value.equals("")) {
												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_retail_date = '" + Long.parseLong(ConvertLongDateToStr(value)) + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);
											} else {
												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_retail_date = ''"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);
											}

											history_actiontype = "Retail Date";

											StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
													+ " (history_so_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)" + " VALUES"
													+ " (" + so_id + ","
													+ " " + emp_id + ","
													+ " '" + ToLongDate(kknow()) + "',"
													+ " '" + history_actiontype + "',"
													+ " '" + history_oldvalue + "',"
													+ " '" + value + "')";
											updateQuery(StrSql);
											StrHTML += "Retail Date Updated!";
											new Incentive_Calculate().IncentiveUpdate(comp_id, branch_brand_id, so_incentive_emp_id, ConvertLongDateToStr(value).substring(0, 6));
										} else {
											StrHTML += "<font color=\"red\">Access Denied!</font>";
										}

										if (!value.equals("")) {
											StrHTML = "";
											if (!emp_id.equals("1") && !(comp_id.equals("1011") && emp_id.equals("88"))) {
												StrSql = "SELECT so_retail_date "
														+ "	FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id + "";
												if (!ExecuteQuery(StrSql).equals("")) {
													StrHTML = "<font color=\"red\">Retail Date already present for this Sales Order!</font>";
												}
											}
											if (!isValidDateFormatLong(value)) {
												StrHTML = "<font color=\"red\">Enter valid Retail Date!</font>";
											} else if (emp_id.equals("1") || (comp_id.equals("1011") && emp_id.equals("88"))) {
												StrSql = "SELECT so_date "
														+ "FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id + "";
												so_date = ExecuteQuery(StrSql);
												if (Long.parseLong(so_date) > Long.parseLong(ConvertLongDateToStr(value))) {
													StrHTML = "<font color=\"red\">Retail Date must be greater than or equal to Sales Order Date!</font>";
												}

											} else if (!ToLongDate(kknow()).substring(0, 8).equals(ConvertLongDateToStr(value).substring(0, 8))) {
												StrHTML = "<font color=\"red\">Retail Date should be Today's Date!</font>";
											}
											// FOR INDEL
											if (comp_id.equals("1011")
													&& CNumeric(PadQuotes(ExecuteQuery("SELECT COALESCE (voucher_id, '0') AS voucher_id FROM " + compdb(comp_id) + "axela_acc_voucher"
															+ " WHERE 1 = 1"
															+ " AND voucher_vouchertype_id = 6"
															+ " AND voucher_authorize = 1"
															+ " AND voucher_so_id = " + so_id))).equals("0")
													&& !branch_branchtype_id.equals("2")) {

												StrHTML = "<font color=\"red\">Invoice is not authorized!</font>";
											}
										}

										if (StrHTML.equals("")) {
											String history_oldvalue = ExecuteQuery("SELECT so_retail_date"
													+ " FROM " + compdb(comp_id) + "axela_sales_so"
													+ " WHERE so_id = " + so_id);
											if (!history_oldvalue.equals("")) {
												history_oldvalue = strToLongDate(history_oldvalue);
											}
											String so_vehstock_id = "0", so_preownedstock_id = "0", vehstock_vehstocklocation_id = "0";
											String enquiry_enquirytype_id = "0";
											StrSql = "SELECT so_date, so_vehstock_id, so_preownedstock_id,"
													+ " COALESCE(vehstock_vehstocklocation_id,0) vehstock_vehstocklocation_id,"
													+ "	enquiry_enquirytype_id"
													+ " FROM " + compdb(comp_id) + "axela_sales_so"
													+ "	INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
													+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
													+ " WHERE so_id = " + so_id + "";
											crs = processQuery(StrSql, 0);
											while (crs.next()) {
												so_date = crs.getString("so_date");
												so_vehstock_id = crs.getString("so_vehstock_id");
												so_preownedstock_id = crs.getString("so_preownedstock_id");
												vehstock_vehstocklocation_id = crs.getString("vehstock_vehstocklocation_id");
												enquiry_enquirytype_id = CNumeric(crs.getString("enquiry_enquirytype_id"));
											}
											crs.close();
											if (enquiry_enquirytype_id.equals("1") && CNumeric(so_vehstock_id).equals("0")) {
												StrHTML = "<font color=\"red\">Stock ID is not associated with Retail Date!</font>";
											} else if (enquiry_enquirytype_id.equals("2") && CNumeric(so_preownedstock_id).equals("0")) {
												StrHTML = "<font color=\"red\">Pre-Owned Stock ID is not associated with Retail Date!</font>";
											}

											StrSql = "SELECT variant_name"
													+ " FROM axelaauto.axela_preowned_variant"
													+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
													+ " WHERE 1=1"
													+ " AND variant_name =( SELECT item_name"
													+ " FROM " + compdb(comp_id) + "axela_inventory_item"
													+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
													+ " WHERE item_id = " + so_item_id;

											if (enquiry_enquirytype_id.equals("1")) {
												StrSql += " AND model_brand_id = preownedmodel_carmanuf_id ";
											}
											StrSql += " )";

											if (ExecuteQuery(StrSql).equals("")) {
												StrHTML = "<font color=\"red\">Variant not found in master table!</font>";
											}

											StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
													+ " WHERE veh_fastag = (SELECT vehstock_fastag"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_id = " + vehstock_id + ""
													+ " AND vehstock_fastag != '')"
													+ " AND CONCAT(veh_chassis_no,'-',veh_engine_no) != "
													+ " (SELECT CONCAT(vehstock_chassis_no,'-',vehstock_engine_no)"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_id = " + vehstock_id + ""
													+ " AND vehstock_fastag != '' )";

											// SOP("StrSql===" + StrSql);

											if (!PadQuotes(ExecuteQuery(StrSql)).equals("")) {
												StrHTML = "<font color=\"red\">Similar Fastag ID found!</font>";
											}

											if (!value.equals("") && StrHTML.equals("")) {
												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_retail_date = '" + Long.parseLong(ConvertLongDateToStr(value)) + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);

												// Updating DMS Date if it is empty
												StrSql = "SELECT "
														+ " vehstock_dms_date"
														+ " FROM " + compdb(comp_id) + "axela_vehstock"
														+ " WHERE vehstock_id = " + vehstock_id;

												if (ExecuteQuery(StrSql).equals("")) {
													StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
															+ " SET "
															+ " vehstock_dms_date = '" + Long.parseLong(ConvertLongDateToStr(value)) + "'"
															+ " WHERE vehstock_id = " + vehstock_id + "";
													updateQuery(StrSql);
												}

												new Veh_Salesorder_Update().AddSOVehicle(so_id, emp_id, comp_id, "");

											} else {
												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_retail_date = ''"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);
											}

											history_actiontype = "Retail Date";
											if (!value.equals("") && StrHTML.equals("")) {
												StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
														+ " (history_so_id,"
														+ " history_emp_id,"
														+ " history_datetime,"
														+ " history_actiontype,"
														+ " history_oldvalue,"
														+ " history_newvalue)" + " VALUES"
														+ " (" + so_id + "," + " " + emp_id
														+ "," + " '" + ToLongDate(kknow())
														+ "'," + " '" + history_actiontype
														+ "'," + " '" + history_oldvalue + "',"
														+ " '" + value + "')";
												updateQuery(StrSql);
												StrHTML = StrHTML + "Retail Date updated!";
											}
											new Incentive_Calculate().IncentiveUpdate(comp_id, branch_brand_id, so_incentive_emp_id, ConvertLongDateToStr(value).substring(0, 6));
										}
									}
								} else {
									StrHTML = "<font color=\"red\">Update Permission denied!</font>";
								}
								// /// End of txt_so_retail_date
								// /// Start txt_so_delivered_date
							} else if (name.equals("txt_so_delivered_date")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								if (ReturnPerm(comp_id, "emp_sales_order_delivery", request).equals("1")) {
									String so_retail_date = ExecuteQuery("SELECT so_retail_date FROM " + compdb(comp_id) + "axela_sales_so " + " WHERE so_id = " + so_id);
									// if (so_retail_date.equals("")) {
									// StrHTML = "<font color=\"red\">Enter Retail Date!</font>";
									// }
									// if (value.length() == 10) {
									// value = value + " " + ToLongDate(kknow()).substring(8, 10) + ":" + ToLongDate(kknow()).substring(10, 12);
									// }
									// if (!ReturnPerm(comp_id, "emp_acc_receipt_add", request).equals("1")) {
									// StrHTML = "<font color=\"red\">Update Permission denied!</font>";
									// }
									if (!isValidDateFormatLong(value) && !value.equals("") && !emp_id.equals("1")) {
										StrHTML = "<font color=\"red\">Enter valid Delivered Date!</font>";
									} else {
										String so_vehstock_id = "0", so_preownedstock_id = "0", vehstock_vehstocklocation_id = "0", enquiry_enquirytype_id = "0";
										StrSql = "SELECT so_date, so_vehstock_id, so_preownedstock_id,"
												+ " enquiry_enquirytype_id, COALESCE(vehstock_vehstocklocation_id,0) vehstock_vehstocklocation_id "
												+ "FROM " + compdb(comp_id) + "axela_sales_so"
												+ "	INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
												+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
												+ " WHERE so_id = " + so_id + "";
										// SOP("StrSql==So delivered==" + StrSql);
										crs = processQuery(StrSql, 0);
										while (crs.next()) {
											so_date = crs.getString("so_date");
											so_vehstock_id = crs.getString("so_vehstock_id");
											vehstock_vehstocklocation_id = crs.getString("vehstock_vehstocklocation_id");
											so_preownedstock_id = crs.getString("so_preownedstock_id");
											enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
										}
										crs.close();

										if (!value.equals("")) {
											// if (so_retail_date.equals("")) {
											// StrHTML = "<font color=\"red\">Enter Retail Date!</font>";
											// }
											if (!emp_sales_order_edit.equals("1")) {
												StrHTML = "<font color=\"red\">Update Permission Denied!</font>";
											} else if (so_retail_date.equals("") && !emp_id.equals("1")) {
												StrHTML = "<font color=\"red\">Enter Retail Date!</font>";
											} else if (Long.parseLong(ConvertLongDateToStr(value)) < Long.parseLong(so_retail_date)) {
												StrHTML = "<font color=\"red\">Delivered Date can't be less than Retail Date!</font>";
											} else if (emp_id.equals("1") || emp_id.equals("88")) {
												StrSql = "SELECT so_date "
														+ "FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id + "";
												so_date = ExecuteQuery(StrSql);
												if (Long.parseLong(so_date) > Long.parseLong(ConvertLongDateToStr(value)) && !emp_id.equals("1")) {
													StrHTML = "<font color=\"red\">Delivered Date must be greater than or equal to Sales Order Date!</font>";
												}
											} else if (Long.parseLong(ConvertLongDateToStr(value).substring(0, 8)) >
													Long.parseLong(ToLongDate(kknow()).substring(0, 8)) && !emp_id.equals("1")) {
												StrHTML = "<font color=\"red\">Delivered Date Should Not Be Greater Than Today's Date!</font>";
											} else if (Long.parseLong(ConvertLongDateToStr(value).substring(0, 8)) <
													Long.parseLong(ToLongDate(kknow()).substring(0, 8)) && !emp_id.equals("1")) { //
												StrHTML = "<font color=\"red\">Delivered Date Should Not Be Less Than Today's Date!</font>";
											} else if (Long.parseLong(so_date) > Long.parseLong(ConvertLongDateToStr(value)) && !emp_id.equals("1")) {
												StrHTML = "<font color=\"red\">Delivered Date must be greater than or equal to Sales Order Date!</font>";
											} else if (CNumeric(enquiry_enquirytype_id).equals("1") && CNumeric(so_vehstock_id).equals("0") && !emp_id.equals("1")) {
												// If Enquiry is for New Car
												StrHTML = "<font color=\"red\">Stock ID is not associated with Sales Order!</font>";
											} else if (CNumeric(enquiry_enquirytype_id).equals("2") && CNumeric(so_preownedstock_id).equals("0") && !emp_id.equals("1")) {
												// If Enquiry is for Pre-Owned
												StrHTML = "<font color=\"red\">Pre-Owned Stock ID is not associated with Sales Order!</font>";
											} else if (CNumeric(vehstock_vehstocklocation_id).equals("1") && comp_id.equals("1009") && !emp_id.equals("1")) {
												StrHTML = "<font color=\"red\">Stock can not be Delivered from PDI!</font>";
											} else if (!CNumeric(so_vehstock_id).equals("0")
													&& !CNumeric(ExecuteQuery("SELECT vehstocklocation_branch_id FROM " + compdb(comp_id) + "axela_vehstock_location WHERE vehstocklocation_id = "
															+ vehstock_vehstocklocation_id))
															.equals(so_branch_id) && !emp_id.equals("1")) {
												StrHTML = "<font color=\"red\">Stock Location does not belong to this Sales Order Branch!</font>";
											}

											StrSql = "SELECT variant_name"
													+ " FROM axelaauto.axela_preowned_variant"
													+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
													+ " WHERE 1=1"
													+ " AND variant_name =( SELECT item_name"
													+ " FROM " + compdb(comp_id) + "axela_inventory_item"
													+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
													+ " WHERE item_id = " + so_item_id;

											if (enquiry_enquirytype_id.equals("1")) {
												StrSql += " AND model_brand_id = preownedmodel_carmanuf_id ";
											}
											StrSql += " )";
											// SOPInfo("variant not found===" + StrSql);
											if (ExecuteQuery(StrSql).equals("")) {
												StrHTML = "<font color=\"red\">Variant not found in master table!</font>";
											}

											StrSql = "SELECT veh_id FROM "
													+ compdb(comp_id)
													+ "axela_service_veh"
													+ " WHERE veh_fastag = (SELECT vehstock_fastag"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_id = " + vehstock_id + ""
													+ " AND vehstock_fastag != '')"
													+ " AND CONCAT(veh_chassis_no,'-',veh_engine_no) != "
													+ " (SELECT concat(vehstock_chassis_no,'-',vehstock_engine_no)"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_id = " + vehstock_id + ""
													+ " AND vehstock_fastag != '' )";

											// SOP("StrSql====" + StrSql);

											if (!PadQuotes(ExecuteQuery(StrSql)).equals("")) {
												StrHTML = "<font color=\"red\">Similar Fastag ID found!</font>";
											}

											// StrSql = "SELECT"
											// + " COALESCE(("
											// + " sum(IF ( sovoucher.voucher_vouchertype_id = 27, sovoucher.voucher_amount, 0 ))"
											// + " -"
											// + " sum(IF ( invoice.voucher_vouchertype_id = 6, invoice.voucher_amount, 0 ))"
											// + " ),0) AS totalBalance"
											// + " FROM " + compdb(comp_id) + "axela_acc_voucher sovoucher"
											// + " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher invoice ON invoice.voucher_preorder_id = sovoucher.voucher_id"
											// + " AND	sovoucher.voucher_vouchertype_id = 27"
											// + " AND invoice.voucher_vouchertype_id = 6"
											// + " AND ( sovoucher.voucher_so_id = " + so_id + ""
											// + " OR sovoucher.voucher_vehstock_id = " + so_vehstock_id + " )"
											// + " AND invoice.voucher_active = 1"
											// + " AND sovoucher.voucher_active = 1";
											//
											// SOP("Pre-order StrSql=======" + StrSql);
											//
											// if (Double.parseDouble(CNumeric(PadQuotes(ExecuteQuery(StrSql)))) != 0) {
											// StrHTML = "<font color=\"red\">Pre-Order Amount is not Equals to Sales Invoice Amount!</font>";
											// }

										}
										if (StrHTML.equals("")) {
											String enquiry_id = "0", so_active = "";
											String branch_id = "0";
											String history_oldvalue = strToLongDate(ExecuteQuery("SELECT so_delivered_date"
													+ " FROM " + compdb(comp_id) + "axela_sales_so"
													+ " WHERE so_id = " + so_id));
											if (!value.equals("")) {
												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET so_delivered_date = '" + Long.parseLong(ConvertLongDateToStr(value)) + "'"
														+ " WHERE so_id = " + so_id + "";
												// SOP("enquiry_enquirytype_id=====" + enquiry_enquirytype_id);
												if (enquiry_enquirytype_id.equals("1")) {
													// Add Vehicle if Sales Order is for New Car
													new Veh_Salesorder_Update().AddSOVehicle(so_id, emp_id, comp_id, ConvertLongDateToStr(value));
												}
											} else {
												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET so_delivered_date = ''"
														+ " WHERE so_id = " + so_id + "";
											}
											updateQuery(StrSql);

											StrSql = "SELECT so_enquiry_id, so_vehstock_id, so_active,"
													+ " so_branch_id, so_emp_id"
													+ " FROM " + compdb(comp_id) + "axela_sales_so"
													+ " WHERE so_id = " + so_id + "";
											ResultSet rset = processQuery(StrSql, 0);

											if (rset.isBeforeFirst()) {
												while (rset.next()) {
													enquiry_id = rset.getString("so_enquiry_id");
													so_vehstock_id = rset.getString("so_vehstock_id");
													so_active = rset.getString("so_active");
													branch_id = rset.getString("so_branch_id");
													so_emp_id = rset.getString("so_emp_id");
												}

												if (value.equals("")) {
													StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
															+ " SET" + " enquiry_stage_id = 5"
															+ " WHERE enquiry_id = " + enquiry_id + "";
												} else {
													StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
															+ " SET" + " enquiry_stage_id = 6"
															+ " WHERE enquiry_id = " + enquiry_id + "";
												}
												updateQuery(StrSql);

												if (!value.equals("") && !CNumeric(so_vehstock_id).equals("0")) {
													StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
															+ " SET" + " vehstock_delstatus_id = '6'"
															+ " WHERE vehstock_id = " + so_vehstock_id + "";
													updateQuery(StrSql);
												}

												if (!value.equals("") && !CNumeric(so_vehstock_id).equals("0")) {
													StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
															+ " SET" + " vehstock_delstatus_id = '6'"
															+ " WHERE vehstock_id = " + so_vehstock_id + "";
													updateQuery(StrSql);
												}
												if (so_active.equals("1") && !value.equals("")) {
													Veh_Salesorder_Update soorder = new Veh_Salesorder_Update();
													soorder.AddCustomCRMFields(so_id, "3", "0", comp_id);
													soorder.so_id = so_id;
													soorder.comp_id = comp_id;
													soorder.emp_id = emp_id;
													soorder.branch_id = branch_id;
													soorder.so_contact_id = so_contact_id;
													soorder.PopulateConfigDetails();
													soorder.PopulateContactDetails();
													if (soorder.comp_email_enable.equals("1")
															&& soorder.config_email_enable.equals("1")
															&& !soorder.branch_email1.equals("")
															&& soorder.brandconfig_so_delivered_email_enable.equals("1")) {
														if (!soorder.contact_email1.equals("")
																&& !soorder.brandconfig_so_delivered_email_format.equals("")
																&& !soorder.brandconfig_so_delivered_email_sub.equals("")) {
															soorder.SendSODeliveredEmail();
														}
													}
													if (soorder.comp_sms_enable.equals("1")
															&& soorder.config_sms_enable.equals("1")
															&& soorder.brandconfig_so_delivered_sms_enable.equals("1")) {
														if (!soorder.brandconfig_so_delivered_sms_format.equals("")
																&& !soorder.contact_mobile1.equals("")) {
															soorder.SendSODeliveredSMS();
														}
													}

												} else if (value.equals("")) { // if so_delivered_date is not there, delete all the PSFs for that SO
													StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_crm"
															+ " WHERE" + " crm_so_id = " + so_id
															+ " AND crm_crmdays_id IN (" + " SELECT crmdays_id"
															+ " FROM " + compdb(comp_id) + "axela_sales_crmdays"
															+ " WHERE crmdays_crmtype_id = 3 )";
													// SOP("StrSql==CRM Delete==" + StrSql);
													updateQuery(StrSql);
												}

											}
											rset.close();

											// Set Preowned Stock delstatus to 2
											if (!value.equals("") && !CNumeric(so_preownedstock_id).equals("0") && CNumeric(so_vehstock_id).equals("0")) {
												StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_stock"
														+ " SET" + " preownedstock_delstatus_id = 2"
														+ " WHERE preownedstock_id = " + so_preownedstock_id + "";
												// SOP("StrSql-----" + StrSql);
												updateQuery(StrSql);
											}

											history_actiontype = "Delivered Date";

											StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
													+ " (history_so_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)" + " VALUES"
													+ " (" + so_id + ","
													+ " " + emp_id + ","
													+ " '" + ToLongDate(kknow()) + "',"
													+ " '" + history_actiontype + "',"
													+ " '" + history_oldvalue + "',"
													+ " '" + value + "')";
											// SOP("StrSql==History==" + StrSql);
											updateQuery(StrSql);
											StrHTML += "Delivered Date updated!";
										}
									}
								} else {
									// SOP("coming..7");
									StrHTML = "<font color=\"red\">Update Permission denied!</font>";
								}
								// /// End txt_so_delivered_date
								// /// Start txt_so_reg_no
							} else if (name.equals("txt_so_reg_no")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_reg_no"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET" + " so_reg_no = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Registration No.";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " (" + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Registration No. Updated!";
							} else if (name.equals("txt_so_reg_date")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");

								if (StrHTML.equals("")) {
									String history_oldvalue = ExecuteQuery("SELECT so_reg_date"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);
									if (!history_oldvalue.equals("")) {
										history_oldvalue = strToShortDate(history_oldvalue);
									}

									if (!value.equals("")) {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
												+ " SET" + " so_reg_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
												+ " WHERE so_id = " + so_id + "";
										updateQuery(StrSql);
									} else {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
												+ " SET" + " so_reg_date = ''"
												+ " WHERE so_id = " + so_id + "";
										updateQuery(StrSql);
									}

									history_actiontype = "Registration Date";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " (" + so_id + ","
											+ " " + emp_id + ","
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = StrHTML + "Registration Date updated!";
								}
							} else if (name.equals("txt_so_dob")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");

								if (StrHTML.equals("")) {
									String history_oldvalue = ExecuteQuery("SELECT so_dob"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);
									if (!history_oldvalue.equals("")) {
										history_oldvalue = strToShortDate(history_oldvalue);
									}

									if (!value.equals("")) {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
												+ " SET" + " so_dob = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
												+ " WHERE so_id = " + so_id + "";
										updateQuery(StrSql);
									} else {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
												+ " SET" + " so_dob = ''"
												+ " WHERE so_id = " + so_id + "";
										updateQuery(StrSql);
									}

									history_actiontype = "DOB";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " (" + so_id + ","
											+ " " + emp_id + ","
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "DOB updated!";
									// SOP("StrHTML-------" + StrHTML);
								}
							} else if (name.equals("txt_so_pan")) {
								StrHTML = "";
								if (!value.equals("")) {
									if (value.length() == 10) {
										value = value.replaceAll("nbsp", "&");
										String history_oldvalue = ExecuteQuery("SELECT so_pan"
												+ " FROM " + compdb(comp_id) + "axela_sales_so"
												+ " WHERE so_id = " + so_id);

										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
												+ " SET" + " so_pan = '" + value + "'"
												+ " WHERE so_id = " + so_id + "";
										updateQuery(StrSql);

										history_actiontype = "PAN No.";

										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
												+ " (history_so_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES"
												+ " (" + so_id + ","
												+ " " + emp_id + ","
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')" + "";
										updateQuery(StrSql);
										StrHTML = "PAN No. Updated!";
									}
									else {
										StrHTML = "<font color=\"red\">Sales Order PAN No. should be of 10 Digits!</font>";
									}
								}
							}
							// else {
							// StrHTML =
							// "<font color=\"red\">Enter Reference No.!</font>";
							// }

							else if (name.equals("chk_so_open")) {
								StrHTML = "";
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");
									String history_oldvalue = ExecuteQuery("SELECT so_open"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);
									if (history_oldvalue.equals("1")) {
										history_oldvalue = "Open";
									} else {
										history_oldvalue = "Close";
									}

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET" + " so_open = '" + value + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									String history_newvalue = ExecuteQuery("SELECT so_open"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);

									if (history_newvalue.equals("1")) {
										history_newvalue = "Open";
									} else {
										history_newvalue = "Close";
									}

									history_actiontype = "Sales Order Open";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " (" + so_id + ","
											+ " " + emp_id + ","
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + history_newvalue + "')";
									updateQuery(StrSql);
									StrHTML = "Sales Order Open updated!";
								}
							} else if (name.equals("txt_so_refno")) {
								StrHTML = "";
								String history_oldvalue = "";
								history_actiontype = "Ref No.";
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT so_refno"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);
								if (!value.equals("")) {
									if (value.length() >= 2) {
										StrSql = "SELECT so_refno"
												+ " FROM " + compdb(comp_id) + "axela_sales_so"
												+ " WHERE so_refno = " + value + ""
												+ " AND so_id != " + so_id + "";
										if (ExecuteQuery(StrSql).equals("")) {

											StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
													+ " SET" + " so_refno = '" + value + "'"
													+ " WHERE so_id = " + so_id + "";
											updateQuery(StrSql);
											StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
													+ " (history_so_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)"
													+ " VALUES"
													+ " (" + so_id + ","
													+ " " + emp_id + ","
													+ " '" + ToLongDate(kknow()) + "',"
													+ " '" + history_actiontype + "',"
													+ " '" + history_oldvalue + "',"
													+ " '" + value + "')" + "";
											updateQuery(StrSql);
											StrHTML = "Reference No. Updated!";
										} else {
											StrHTML = "<font color=\"red\">Similar Sales Order Reference No. found!</font>";
										}
									} else {
										StrHTML = "<font color=\"red\">Sales Order Reference No. should be atleast Two Digits!</font>";
									}
								}
								else {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET" + " so_refno = '" + value + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " (" + so_id + ","
											+ " " + emp_id + ","
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')" + "";
									updateQuery(StrSql);
									StrHTML = "Reference No. Updated!";
								}

							} else if (name.equals("txt_so_cancel_date")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");

								if (!value.equals("")
										&& !isValidDateFormatShort(value)) {
									StrHTML = "<font color=\"red\">Enter valid Cancel Date!</font>";
								} else {
									StrSql = "SELECT so_date FROM "
											+ compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id + "";
									so_date = ExecuteQuery(StrSql);

									if (!value.equals("")
											&& Long.parseLong(so_date) > Long
													.parseLong(ConvertShortDateToStr(value))) {
										StrHTML = "<font color=\"red\">Cancel Date must be greater than or equal to Sales Order Date!</font>";
									} else {
										String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_cancel_date"
												+ " FROM "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " WHERE so_id = " + so_id));

										if (!value.equals("")) {
											StrSql = "UPDATE "
													+ compdb(comp_id)
													+ "axela_sales_so"
													+ " SET"
													+ " so_cancel_date = '"
													+ Long.parseLong(ConvertShortDateToStr(value))
													+ "'" + " WHERE so_id = "
													+ so_id + "";
										} else {
											StrSql = "UPDATE " + compdb(comp_id)
													+ "axela_sales_so" + " SET"
													+ " so_cancel_date = ''"
													+ " WHERE so_id = " + so_id
													+ "";
										}
										updateQuery(StrSql);

										history_actiontype = "Cancel Date";

										StrSql = "INSERT INTO " + compdb(comp_id)
												+ "axela_sales_so_history"
												+ " (history_so_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)" + " VALUES"
												+ " (" + so_id + "," + " "
												+ emp_id + "," + " '"
												+ ToLongDate(kknow()) + "'," + " '"
												+ history_actiontype + "'," + " '"
												+ history_oldvalue + "'," + " '"
												+ value + "')";
										updateQuery(StrSql);
										StrHTML = "Cancel Date updated!";
									}
								}
							} else if (name.equals("dr_cancel_reason")) {
								StrHTML = "";
								String history_oldvalue = "", so_active = "0";

								StrSql = "SELECT so_active, so_cancel_date,"
										+ " COALESCE(cancelreason_name, '') AS cancelreason_name"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " LEFT JOIN "
										+ compdb(comp_id)
										+ "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id"
										+ " WHERE so_id = " + so_id + "";
								ResultSet rset = processQuery(StrSql, 0);

								while (rset.next()) {
									history_oldvalue = rset
											.getString("cancelreason_name");
									so_active = rset.getString("so_active");
									so_cancel_date = rset
											.getString("so_cancel_date");
								}
								rset.close();
								if (!value.equals("0") && so_active.equals("1")) {
									// StrHTML =
									// "<font color=\"red\">Active Sales Order cannot have Cancel Reason!</font>";
									StrHTML = "";
								} else if (value.equals("0")
										&& so_active.equals("0")) {
									StrHTML = "";
								} else {
									if (so_cancel_date.equals("")) {
										StrHTML = "<font color=\"red\">Enter Cancel Date!</font>";
									} else {
										StrHTML = "";
										StrSql = "UPDATE " + compdb(comp_id)
												+ "axela_sales_so" + " SET"
												+ " so_cancelreason_id = " + value
												+ "" + " WHERE so_id = " + so_id
												+ "";
										updateQuery(StrSql);

										String history_newvalue = ExecuteQuery("SELECT COALESCE(cancelreason_name, '') AS cancelreason_name"
												+ " FROM "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " LEFT JOIN "
												+ compdb(comp_id)
												+ "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id"
												+ " WHERE so_cancelreason_id = "
												+ value);

										history_actiontype = "Cancel Reason";

										StrSql = "INSERT INTO " + compdb(comp_id)
												+ "axela_sales_so_history"
												+ " (history_so_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)" + " VALUES"
												+ " (" + so_id + "," + " "
												+ emp_id + "," + " '"
												+ ToLongDate(kknow()) + "'," + " '"
												+ history_actiontype + "'," + " '"
												+ history_oldvalue + "'," + " '"
												+ history_newvalue + "')";
										updateQuery(StrSql);
										StrHTML = "Cancel Reason Updated!";
									}
								}
							} else if (name.equals("dr_so_cinstatus_id")) {
								String so_active = CNumeric(ExecuteQuery("SELECT so_active FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id));
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								if (so_active.equals("0")) {
									String history_oldvalue = ExecuteQuery("SELECT cinstatus_name"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_cin_status ON cinstatus_id = so_cinstatus_id"
											+ " WHERE so_id = " + so_id);

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET so_cinstatus_id = " + value + ""
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									String history_newvalue = ExecuteQuery("SELECT cinstatus_name"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_cin_status ON cinstatus_id = so_cinstatus_id"
											+ " WHERE so_id = " + so_id);
									history_actiontype = "CIN";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ("
											+ " " + so_id + ","
											+ " " + emp_id + ","
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + history_newvalue + "')";
									updateQuery(StrSql);
									StrHTML = "CIN Updated!";
								} else {
									StrHTML = "<font color=\"red\">Sales Order is Active!</font>";
								}

							} else if (name.equals("chk_so_active")) {
								StrHTML = "";
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");
									String history_oldvalue = "", so_vehstock_id = "0";
									String vehstock_id = "", branch_email1 = "";
									StrSql = "SELECT so_active, so_vehstock_id, branch_email1,"
											+ " COALESCE(vehstock_id, 0) AS vehstock_id"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " INNER JOIN "
											+ compdb(comp_id)
											+ "axela_branch ON branch_id = so_branch_id"
											+ " LEFT JOIN "
											+ compdb(comp_id)
											+ "axela_vehstock ON vehstock_id = so_vehstock_id"
											+ " WHERE so_id = " + so_id + "";
									ResultSet rset = processQuery(StrSql, 0);

									while (rset.next()) {
										history_oldvalue = rset
												.getString("so_active");
										so_vehstock_id = rset.getString("so_vehstock_id");
										vehstock_id = rset.getString("vehstock_id");
										branch_email1 = rset
												.getString("branch_email1");
									}
									rset.close();
									if ((!so_cancel_date.equals("")
											&& !so_cancelreason_id.equals("0") && history_oldvalue
												.equals("1"))
											|| history_oldvalue.equals("0")) {
										if (!isValidDateFormatShort(so_cancel_date)
												&& history_oldvalue.equals("1")) {
											StrHTML = "<font color=\"red\">Enter valid Cancel Date!</font>";
										} else {
											if (history_oldvalue.equals("1")) {
												history_oldvalue = "Active";
											} else {
												history_oldvalue = "Inactive";
											}

											StrSql = "UPDATE " + compdb(comp_id)
													+ "axela_sales_so" + " SET";
											if (value.equals("1")) {
												StrHTML = "";
												StrSql += " so_cancel_date = '',"
														+ " so_cancelreason_id = 0,";
											} else if (value.equals("0")) {
												StrHTML = "";
												StrSql += " so_cancel_date = '"
														+ ConvertShortDateToStr(so_cancel_date)
														+ "',"
														+ " so_cancelreason_id = "
														+ so_cancelreason_id + ",";
											}
											StrSql += " so_active = '" + value
													+ "'" + " WHERE so_id = "
													+ so_id + "";
											updateQuery(StrSql);

											if (history_oldvalue.equals("Active")) {
												// /// SO is Inactive
												new Veh_Salesorder_Update().AddCustomCRMFields(so_id, "2", "1", comp_id);
												new Veh_Salesorder_Update().SendEmailToNewSales(so_id, vehstock_id, emp_id, "0", branch_email1, comp_id);
											} else {
												// /// SO is active
												StrSql = "DELETE salescrm.* FROM " + compdb(comp_id) + "axela_sales_crm AS salescrm"
														+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = salescrm.crm_crmdays_id"
														+ " WHERE salescrm.crm_so_id = " + so_id + ""
														+ " AND crmdays_crmtype_id = 2"
														+ " AND crmdays_so_inactive = 1";
												updateQuery(StrSql);
											}
											String history_newvalue = ExecuteQuery("SELECT so_active"
													+ " FROM "
													+ compdb(comp_id)
													+ "axela_sales_so"
													+ " WHERE so_id = " + so_id);

											if (history_newvalue.equals("1")) {
												history_newvalue = "Active";
											} else {
												history_newvalue = "Inactive";
											}

											history_actiontype = "Sales Order Active";

											StrSql = "INSERT INTO "
													+ compdb(comp_id)
													+ "axela_sales_so_history"
													+ " (history_so_id,"
													+ " history_emp_id,"
													+ " history_datetime,"
													+ " history_actiontype,"
													+ " history_oldvalue,"
													+ " history_newvalue)"
													+ " VALUES" + " (" + so_id
													+ "," + " " + emp_id + ","
													+ " " + ToLongDate(kknow())
													+ "," + " '"
													+ history_actiontype + "',"
													+ " '" + history_oldvalue
													+ "'," + " '"
													+ history_newvalue + "')";
											updateQuery(StrSql);
											if (history_newvalue.equals("Inactive")) {
												StrHTML = "Sales Order Inactivated!";
											} else {
												StrHTML = "Sales Order Activated!";
											}
										}
									} else {
										StrHTML = "<font color=\"red\">Enter Cancel Date and Select Cancel Reason for Inactivating the Sales Order!</font>";
									}
								}
							} else if (name.equals("txt_so_notes")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");

								if (value.length() > 255) {
									value = value.substring(0, 254);
								}

								String history_oldvalue = ExecuteQuery("SELECT so_notes"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_notes = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Notes";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Notes Updated!";
							} else if (name.equals("chk_so_authorize_pdi") || name.equals("chk_so_authorize_accessories")
									|| name.equals("chk_so_authorize_accounts") || name.equals("chk_so_authorize_insurance")
									|| name.equals("chk_so_authorize_registration") || name.equals("chk_so_authorize_deliverycoordinator")) {
								if (!so_din_del_location.equals("0")) {
									if (name.equals("chk_so_authorize_pdi")) {
										StrHTML = "";
										if (!value.equals("")) {
											if (ReturnPerm(comp_id, "emp_authorize_pdi", request).equals("1")) {
												value = value.replaceAll("nbsp", "&");
												String history_oldvalue = ExecuteQuery("SELECT so_authorize_pdi"
														+ " FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id);
												if (history_oldvalue.equals("1")) {
													history_oldvalue = "Authorize";
												} else {
													history_oldvalue = "Unauthorize";
												}

												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_authorize_pdi = '" + value + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);

												if (value.equals("1")) {
													value = "Authorize";
												} else {
													value = "Unauthorize";
												}

												history_actiontype = "PDI Authorize";

												StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
														+ " (history_so_id,"
														+ " history_emp_id,"
														+ " history_datetime,"
														+ " history_actiontype,"
														+ " history_oldvalue,"
														+ " history_newvalue)"
														+ " VALUES"
														+ " (" + so_id + ","
														+ " " + emp_id + ","
														+ " '" + ToLongDate(kknow()) + "',"
														+ " '" + history_actiontype + "',"
														+ " '" + history_oldvalue + "',"
														+ " '" + value + "')";
												updateQuery(StrSql);
												StrHTML = "Authorize PDI updated!";
											} else {
												StrHTML = "Access Denied!";
											}
										}
									} else if (name.equals("chk_so_authorize_accessories")) {
										StrHTML = "";
										if (!value.equals("")) {
											if (ReturnPerm(comp_id, "emp_authorize_accessories", request).equals("1")) {
												value = value.replaceAll("nbsp", "&");
												String history_oldvalue = ExecuteQuery("SELECT so_authorize_accessories"
														+ " FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id);
												if (history_oldvalue.equals("1")) {
													history_oldvalue = "Authorize";
												} else {
													history_oldvalue = "Unauthorize";
												}

												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_authorize_accessories = '" + value + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);

												if (value.equals("1")) {
													value = "Authorize";
												} else {
													value = "Unauthorize";
												}

												history_actiontype = "Accessories Authorize";

												StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
														+ " (history_so_id,"
														+ " history_emp_id,"
														+ " history_datetime,"
														+ " history_actiontype,"
														+ " history_oldvalue,"
														+ " history_newvalue)"
														+ " VALUES"
														+ " (" + so_id + ","
														+ " " + emp_id + ","
														+ " '" + ToLongDate(kknow()) + "',"
														+ " '" + history_actiontype + "',"
														+ " '" + history_oldvalue + "',"
														+ " '" + value + "')";
												updateQuery(StrSql);
												StrHTML = "Authorize Accessories updated!";
											} else {
												StrHTML = "Access Denied!";
											}
										}
									} else if (name.equals("chk_so_authorize_accounts")) {
										StrHTML = "";
										if (!value.equals("")) {
											if (ReturnPerm(comp_id, "emp_authorize_accounts", request).equals("1")) {
												value = value.replaceAll("nbsp", "&");
												String history_oldvalue = ExecuteQuery("SELECT so_authorize_accounts"
														+ " FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id);
												if (history_oldvalue.equals("1")) {
													history_oldvalue = "Authorize";
												} else {
													history_oldvalue = "Unauthorize";
												}

												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_authorize_accounts = '" + value + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);

												if (value.equals("1")) {
													value = "Authorize";
												} else {
													value = "Unauthorize";
												}

												history_actiontype = "Accounts Authorize";

												StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
														+ " (history_so_id,"
														+ " history_emp_id,"
														+ " history_datetime,"
														+ " history_actiontype,"
														+ " history_oldvalue,"
														+ " history_newvalue)"
														+ " VALUES"
														+ " (" + so_id + ","
														+ " " + emp_id + ","
														+ " '" + ToLongDate(kknow()) + "',"
														+ " '" + history_actiontype + "',"
														+ " '" + history_oldvalue + "',"
														+ " '" + value + "')";
												updateQuery(StrSql);
												StrHTML = "Authorize Accounts updated!";
											} else {
												StrHTML = "Access Denied!";
											}
										}
									} else if (name.equals("chk_so_authorize_insurance")) {
										StrHTML = "";
										if (!value.equals("")) {
											if (ReturnPerm(comp_id, "emp_authorize_insurance", request).equals("1")) {
												value = value.replaceAll("nbsp", "&");
												String history_oldvalue = ExecuteQuery("SELECT so_authorize_insurance"
														+ " FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id);
												if (history_oldvalue.equals("1")) {
													history_oldvalue = "Authorize";
												} else {
													history_oldvalue = "Unauthorize";
												}

												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_authorize_insurance = '" + value + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);

												if (value.equals("1")) {
													value = "Authorize";
												} else {
													value = "Unauthorize";
												}

												history_actiontype = "Insurance Authorize";

												StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
														+ " (history_so_id,"
														+ " history_emp_id,"
														+ " history_datetime,"
														+ " history_actiontype,"
														+ " history_oldvalue,"
														+ " history_newvalue)"
														+ " VALUES"
														+ " (" + so_id + ","
														+ " " + emp_id + ","
														+ " '" + ToLongDate(kknow()) + "',"
														+ " '" + history_actiontype + "',"
														+ " '" + history_oldvalue + "',"
														+ " '" + value + "')";
												updateQuery(StrSql);
												StrHTML = "Authorize Insurance updated!";
											} else {
												StrHTML = "Access Denied!";
											}
										}
									} else if (name.equals("chk_so_authorize_registration")) {
										StrHTML = "";
										if (!value.equals("")) {
											if (ReturnPerm(comp_id, "emp_authorize_registration", request).equals("1")) {
												value = value.replaceAll("nbsp", "&");
												String history_oldvalue = ExecuteQuery("SELECT so_authorize_registration"
														+ " FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id);
												if (history_oldvalue.equals("1")) {
													history_oldvalue = "Authorize";
												} else {
													history_oldvalue = "Unauthorize";
												}

												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_authorize_registration = '" + value + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);

												if (value.equals("1")) {
													value = "Authorize";
												} else {
													value = "Unauthorize";
												}

												history_actiontype = "Registration Authorize";

												StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
														+ " (history_so_id,"
														+ " history_emp_id,"
														+ " history_datetime,"
														+ " history_actiontype,"
														+ " history_oldvalue,"
														+ " history_newvalue)"
														+ " VALUES"
														+ " (" + so_id + ","
														+ " " + emp_id + ","
														+ " '" + ToLongDate(kknow()) + "',"
														+ " '" + history_actiontype + "',"
														+ " '" + history_oldvalue + "',"
														+ " '" + value + "')";
												updateQuery(StrSql);
												StrHTML = "Authorize Registration updated!";
											} else {
												StrHTML = "Access Denied!";
											}
										}
									} else if (name.equals("chk_so_authorize_deliverycoordinator")) {
										StrHTML = "";
										if (!value.equals("")) {
											if (ReturnPerm(comp_id, "emp_authorize_deliverycoordinator", request).equals("1")) {
												value = value.replaceAll("nbsp", "&");
												String history_oldvalue = ExecuteQuery("SELECT so_authorize_deliverycoordinator"
														+ " FROM " + compdb(comp_id) + "axela_sales_so"
														+ " WHERE so_id = " + so_id);
												if (history_oldvalue.equals("1")) {
													history_oldvalue = "Authorize";
												} else {
													history_oldvalue = "Unauthorize";
												}

												StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
														+ " SET" + " so_authorize_deliverycoordinator = '" + value + "'"
														+ " WHERE so_id = " + so_id + "";
												updateQuery(StrSql);

												if (value.equals("1")) {
													value = "Authorize";
												} else {
													value = "Unauthorize";
												}

												history_actiontype = "Delivery Coordinator Authorize";

												StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
														+ " (history_so_id,"
														+ " history_emp_id,"
														+ " history_datetime,"
														+ " history_actiontype,"
														+ " history_oldvalue,"
														+ " history_newvalue)"
														+ " VALUES"
														+ " (" + so_id + ","
														+ " " + emp_id + ","
														+ " '" + ToLongDate(kknow()) + "',"
														+ " '" + history_actiontype + "',"
														+ " '" + history_oldvalue + "',"
														+ " '" + value + "')";
												updateQuery(StrSql);
												StrHTML = "Authorize Delivery Coordinator updated!";
											} else {
												StrHTML = "Access Denied!";
											}
										}
									}
								} else {
									StrHTML = "DIN is not updated!";
								}
							} else if (name.equals("dr_so_finstatus_id")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT COALESCE(finstatus_name, '') AS finstatus_name"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " LEFT JOIN "
										+ compdb(comp_id)
										+ "axela_sales_so_finance_status ON finstatus_id = so_finstatus_id"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_id = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								String history_newvalue = ExecuteQuery("SELECT COALESCE(finstatus_name, '') AS finstatus_name"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " LEFT JOIN "
										+ compdb(comp_id)
										+ "axela_sales_so_finance_status ON finstatus_id = so_finstatus_id"
										+ " WHERE so_finstatus_id = " + value);

								history_actiontype = "Finance By";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Finance Status Update!";
							} else if (name.equals("txt_so_finstatus_desc")) {
								// SOP("111");
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_desc"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id =" + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_desc = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Description";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')" + "";
								updateQuery(StrSql);
								StrHTML = "Description Updated!";
							} else if (name.equals("txt_so_finstatus_loan_amt")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_loan_amt"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id =" + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_loan_amt = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Loan Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')" + "";
								updateQuery(StrSql);
								StrHTML = "Loan Amount Updated!";
							} else if (name.equals("txt_so_finstatus_emi_value")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_emi_value"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id =" + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET" + " so_finstatus_emi_value = '" + CNumeric(value) + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "EMI/Value";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')" + "";
								updateQuery(StrSql);
								StrHTML = "EMI/Value Updated!";
							} else if (name.equals("dr_so_finstatus_advance")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_advance"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id ="
										+ so_id);
								if (history_oldvalue.equals("0")) {
									history_oldvalue = "";
								} else if (history_oldvalue.equals("1")) {
									history_oldvalue = "Advance";
								} else if (history_oldvalue.equals("2")) {
									history_oldvalue = "Arrears";
								}

								String history_newvalue = "";
								if (value.equals("0")) {
									history_newvalue = "";
								} else if (value.equals("1")) {
									history_newvalue = "Advance";
								} else if (value.equals("2")) {
									history_newvalue = "Arrears";
								}

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_advance = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Advance/Arrears";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Advance/Arrears Updated!";
							} else if (name.equals("txt_so_finstatus_disbursed_amt")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_disbursed_amt"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_disbursed_amt = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Disbursed Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')" + "";
								updateQuery(StrSql);
								StrHTML = "Disbursed Amount Updated!";
							} else if (name.equals("txt_so_finstatus_tenure")) {
								StrHTML = "";
								if (Integer.parseInt(CNumeric(value)) <= 100) {
									String history_oldvalue = ExecuteQuery("SELECT so_finstatus_tenure"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id =" + so_id);

									StrSql = "UPDATE " + compdb(comp_id)
											+ "axela_sales_so" + " SET"
											+ " so_finstatus_tenure = '"
											+ CNumeric(value) + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);
									history_actiontype = "Tenure";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " " + emp_id
											+ "," + " '" + ToLongDate(kknow())
											+ "'," + " '" + history_actiontype
											+ "'," + " '" + history_oldvalue + "',"
											+ " '" + CNumeric(value) + "')" + "";
									updateQuery(StrSql);
									StrHTML = "Tenure Updated!";
								} else {
									StrHTML = "<font color=\"red\">Tenure should not greater than 100!</font>";
								}
							} else if (name.equals("dr_so_finstatus_bank_id")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT COALESCE(bank_name, '') FROM "
										+ compdb(comp_id)
										+ "axela_sales_so_bank"
										+ " LEFT JOIN "
										+ compdb(comp_id)
										+ "axela_sales_so ON so_finstatus_bank_id = bank_id"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_bank_id = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								String history_newvalue = ExecuteQuery("SELECT COALESCE(bank_name, '') FROM "
										+ compdb(comp_id)
										+ "axela_sales_so_bank"
										+ " LEFT JOIN "
										+ compdb(comp_id)
										+ "axela_sales_so ON so_finstatus_bank_id = bank_id"
										+ " WHERE so_id = " + so_id);

								history_actiontype = "Bank";
								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Bank Updated!";
							} else if (name.equals("txt_so_finstatus_scheme")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_scheme"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id ="
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_scheme = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Scheme";
								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Scheme Updated!";
							} else if (name.equals("txt_so_finstatus_process_fee")) {
								StrHTML = "";
								value = CNumeric(value);
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_process_fee"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_process_fee = '" + value
										+ "'" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Processing Fees";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Processing Fees Updated!";
							} else if (name.equals("txt_so_finstatus_subvention")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_subvention"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_subvention = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);

								history_actiontype = "subvention";
								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Subvention Updated!";
							} else if (name.equals("txt_so_finstatus_gross_payout")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_gross_payout"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_gross_payout = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Gross Payout";
								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);

								// txt_so_finstatus_gross_payout for finance payout
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								StrHTML = "Gross Payout Updated!";
							} else if (name.equals("txt_so_finstatus_income_on_payment")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_income_on_payment"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_income_on_payment = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Income On Payment";
								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Income On Payment Updated!";
							} else if (name.equals("txt_so_finstatus_bank_rack_rate")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_bank_rack_rate"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_bank_rack_rate = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Bank Rack Rate";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Bank Rack Rate Updated!";
							} else if (name.equals("txt_so_finstatus_customer_roi")) {
								StrHTML = "";
								if (Double.parseDouble(CNumeric(value)) > 100) {
									StrHTML = "<font color=\"red\">Enter valid Customer ROI!</font>!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT so_finstatus_customer_roi"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);

									StrSql = "UPDATE " + compdb(comp_id)
											+ "axela_sales_so" + " SET"
											+ " so_finstatus_customer_roi = '"
											+ CNumeric(value) + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									history_actiontype = "Customer ROI";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " " + emp_id
											+ "," + " '" + ToLongDate(kknow())
											+ "'," + " '" + history_actiontype
											+ "'," + " '" + history_oldvalue + "',"
											+ " '" + CNumeric(value) + "')";
									updateQuery(StrSql);
									StrHTML = "Customer ROI Updated!";
								}
							} else if (name.equals("txt_so_finstatus_occupation")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_occupation"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_occupation = '" + value
										+ "'" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Occupation";
								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Occupation Updated!";
							} else if (name.equals("txt_so_finstatus_industry")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_industry"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_industry = '" + value
										+ "'" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Industry";
								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Industry Updated!";
							} else if (name.equals("txt_so_finstatus_income_asperdoc")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_income_asperdoc"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_income_asperdoc = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Income as per Doc";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Income as per Doc Updated!";
							} else if (name.equals("txt_so_finstatus_dob")) {
								StrHTML = "";
								if (!value.equals("")
										&& !isValidDateFormatShort(value)) {
									StrHTML = "<font color=\"red\">Enter Date of Birth!</font>";
								} else {
									String history_oldvalue = "";
									String so_finstatus_dob = ExecuteQuery("SELECT so_finstatus_dob"
											+ " FROM " + compdb(comp_id) + "axela_sales_so"
											+ " WHERE so_id = " + so_id);
									if (!so_finstatus_dob.equals("")) {
										history_oldvalue = strToShortDate(so_finstatus_dob);
									}

									if (!value.equals("")) {
										so_finstatus_dob = ConvertShortDateToStr(value);
									} else {
										so_finstatus_dob = "";
									}
									StrSql = "UPDATE " + compdb(comp_id)
											+ "axela_sales_so" + " SET"
											+ " so_finstatus_dob = '"
											+ so_finstatus_dob + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									history_actiontype = "Date of Birth";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " " + emp_id
											+ "," + " '" + ToLongDate(kknow())
											+ "'," + " '" + history_actiontype
											+ "'," + " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML += "Date of Birth updated!";
								}
							} else if (name.equals("dr_so_finstatus_sex")) {
								StrHTML = "";
								String gender = ExecuteQuery("SELECT so_finstatus_sex"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								String history_oldvalue = "";
								if (gender.equals("1")) {
									history_oldvalue = "Male";
								} else if (gender.equals("2")) {
									history_oldvalue = "Female";
								}

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_sex = '" + CNumeric(value)
										+ "'" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								String history_newvalue = "";
								if (value.equals("1")) {
									history_newvalue = "Male";
								} else if (value.equals("2")) {
									history_newvalue = "Female";
								}

								history_actiontype = "Gender";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Gender Updated!";
							} else if (name.equals("txt_so_notes")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");

								if (value.length() > 255) {
									value = value.substring(0, 254);
								}

								String history_oldvalue = ExecuteQuery("SELECT so_notes"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_notes = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Notes";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Notes Updated!";
							} else if (name.equals("txt_so_fin_notes")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");

								if (value.length() > 255) {
									value = value.substring(0, 254);
								}

								String history_oldvalue = ExecuteQuery("SELECT so_notes"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_notes = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Notes";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '" + value
										+ "')";
								updateQuery(StrSql);
								StrHTML = "Notes Updated!";
							} else if (name.equals("txt_so_finstatus_payout_rate")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_payout_rate"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_payout_rate = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Payout Rate";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Payout Rate Updated!";
							} else if (name.equals("txt_so_finstatus_payback")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_payback"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_payback = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Payback";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Payback Amount Updated!";
							} else if (name.equals("txt_so_finstatus_netincome")) {
								StrHTML = "";
								String history_oldvalue = ExecuteQuery("SELECT so_finstatus_netincome"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_finstatus_netincome = '"
										+ CNumeric(value) + "'" + " WHERE so_id = "
										+ so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Net Income";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Net Income Updated!";
							}
						}
						// // For Account Tab
						if ((ReturnPerm(comp_id, "emp_acc_receipt_edit", request).equals("1") && !chk_so_delivered_date.equals(""))
								|| (ReturnPerm(comp_id, "emp_acc_receipt_add", request).equals("1") && chk_so_delivered_date.equals(""))
								|| emp_role_id.equals("1")) {
							// Accounts Tab Fields Begin
							if (name.equals("txt_so_mga_amount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_mga_amount"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);
								// SOP("so_id===" + so_id);
								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_mga_amount = " + CNumeric(value) + ""
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Accessories Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " (" + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "'," +
										" '" + history_oldvalue + "',"
										+ " " + CNumeric(value) + ")";
								// SOP("StrSql=mga amount==" + StrSql);
								updateQuery(StrSql);
								StrHTML = "Accessories Amount Updated!";

								if (!so_retail_date.equals("0")) {
									new Incentive_Calculate().IncentiveUpdate(comp_id, branch_brand_id, so_incentive_emp_id, so_retail_date.substring(0, 6));
								}

							} else if (name.equals("txt_so_mga_paid")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_mga_paid"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);
								// SOP("so_id===" + so_id);
								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_mga_paid = " + CNumeric(value) + ""
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Accessories Paid";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " (" + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "'," +
										" '" + history_oldvalue + "',"
										+ " " + CNumeric(value) + ")";
								// SOP("StrSql=mga amount==" + StrSql);
								updateQuery(StrSql);

								// txt_so_mga_paid for gross payout of Insurance
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								StrHTML = "Accessories Paid Updated!";
							} else if (name.equals("txt_so_mga_focamount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_mga_foc_amount"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);
								// SOP("so_id===" + so_id);
								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_mga_foc_amount = " + CNumeric(value) + ""
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Accessories Paid";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " (" + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "'," +
										" '" + history_oldvalue + "',"
										+ " " + CNumeric(value) + ")";
								// SOP("StrSql=mga amount==" + StrSql);
								updateQuery(StrSql);

								// profitablity for txt_so_mga_focamount
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								StrHTML = "Accessories FOC Amount Updated!";
							} else if (name.equals("txt_so_ew_amount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_ew_amount"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_ew_amount = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								history_actiontype = "EW Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "EW Amount Updated!";

								if (!so_retail_date.equals("0")) {
									new Incentive_Calculate().IncentiveUpdate(comp_id, branch_brand_id, so_incentive_emp_id, so_retail_date.substring(0, 6));
								}

							} else if (name.equals("txt_so_ew_payout")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_ew_payout"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_ew_payout = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "EW Payout";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								// UpdateProfitability for txt_so_ew_payout
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								StrHTML = "EW Payout Updated!";
							} else if (name.equals("dr_ew_type_id")) {
								StrHTML = "";
								String history_oldvalue = "";
								value = value.replaceAll("nbsp", "&");
								String so_ew_type_id = ExecuteQuery("SELECT so_ew_type_id"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								if (so_ew_type_id.equals("1")) {
									history_oldvalue = "FOC";

								} else if (so_ew_type_id.equals("2")) {
									history_oldvalue = "PAID";
								}

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_ew_type_id = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "EW Type";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '";
								if (CNumeric(value).equals("1")) {
									StrSql += "FOC" + "')";
								} else if (CNumeric(value).equals("2")) {
									StrSql += "PAID" + "')";
								}

								// UpdateProfitability for dr_ew_type_id
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								updateQuery(StrSql);
								StrHTML = "EW Type Updated!";
							} else if (name.equals("txt_so_insur_amount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_insur_amount"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_insur_amount = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								history_actiontype = "Insurance Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Insurance Amount Updated!";

								if (!so_retail_date.equals("0")) {
									new Incentive_Calculate().IncentiveUpdate(comp_id, branch_brand_id, so_incentive_emp_id, so_retail_date.substring(0, 6));
								}

							} else if (name.equals("txt_so_insur_amount1")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_insur_amount"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_insur_amount = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Insurance Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Insurance Amount Updated!";
							} else if (name.equals("txt_so_insur_gross")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_insur_gross"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_insur_gross = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Insurance Gross";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Insurance Gross Updated!";
							} else if (name.equals("txt_so_insur_gross1")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_insur_gross"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_insur_gross = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Insurance Gross";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Insurance Gross Updated!";
							} else if (name.equals("txt_so_insur_net")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_insur_net"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_insur_net = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Insurance Net";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Insurance Net Updated!";
								// txt_so_insur_net for Net payout of Insurance
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);
							} else if (name.equals("txt_so_insur_net1")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_insur_net"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_insur_net = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Insurance Net";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Insurance Net Updated!";
							} else if (name.equals("dr_insurtype_id")) {
								StrHTML = "";
								String history_oldvalue = "";
								value = value.replaceAll("nbsp", "&");
								String so_insur_type_id = ExecuteQuery("SELECT so_insur_type_id"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);
								if (so_insur_type_id.equals("1")) {
									history_oldvalue = "FOC";
								} else if (so_insur_type_id.equals("2")) {
									history_oldvalue = "PAID";
								}

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_insur_type_id = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Insurance Type";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '";
								if (CNumeric(value).equals("1")) {
									StrSql += "FOC" + "')";
								} else if (CNumeric(value).equals("2")) {
									StrSql += "PAID" + "')";
								}
								updateQuery(StrSql);
								// UpdateProfitability for dr_insurtype_id when it made to foc(1)
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								StrHTML = "Insurance Type Updated!";
							} else if (name.equals("dr_insurtype_id1")) {
								StrHTML = "";
								String history_oldvalue = "";
								value = value.replaceAll("nbsp", "&");
								String so_insur_type_id = ExecuteQuery("SELECT so_insur_type_id"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);
								if (so_insur_type_id.equals("1")) {
									history_oldvalue = "FOC";
								} else if (so_insur_type_id.equals("2")) {
									history_oldvalue = "PAID";
								}

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_insur_type_id = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Insurance Type";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '";
								if (CNumeric(value).equals("1")) {
									StrSql += "FOC" + "')";
								} else if (CNumeric(value).equals("2")) {
									StrSql += "PAID" + "')";
								}
								updateQuery(StrSql);
								StrHTML = "Insurance Type Updated!";
							} else if (name.equals("chk_so_exchange")) {
								StrHTML = "";
								if (!value.equals("")) {
									String history_oldvalue = ExecuteQuery("SELECT so_exchange"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " WHERE so_id = "
											+ so_id);
									if (history_oldvalue.equals("1")) {
										history_oldvalue = "Exchange";
									} else {
										history_oldvalue = "Non-Exchange";
									}

									StrSql = "UPDATE " + compdb(comp_id)
											+ "axela_sales_so" + " SET"
											+ " so_exchange = '" + value + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									String history_newvalue = ExecuteQuery("SELECT so_exchange"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " WHERE so_id = "
											+ so_id);

									if (history_newvalue.equals("1")) {
										history_newvalue = "Exchange";
									} else {
										history_newvalue = "Non-Exchange";
									}

									history_actiontype = "Exchange";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " " + emp_id
											+ "," + " '" + ToLongDate(kknow())
											+ "'," + " '" + history_actiontype
											+ "'," + " '" + history_oldvalue + "',"
											+ " '" + history_newvalue + "')";
									updateQuery(StrSql);
									StrHTML = "Exchange updated!";
								}
							}
							else if (name.equals("txt_so_exchange_amount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_exchange_amount"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_exchange_amount = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								history_actiontype = "Exchange Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Exchange Amount Updated!";

								if (!so_retail_date.equals("0")) {
									new Incentive_Calculate().IncentiveUpdate(comp_id, branch_brand_id, so_incentive_emp_id, so_retail_date.substring(0, 6));
								}

							} else if (name.equals("txt_so_off_consumer")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_offer_consumer"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_offer_consumer = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Consumer Offers";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);

								// UpdateProfitability for txt_so_off_consumer
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								StrHTML = "Consumer Offers Updated!";
							} else if (name.equals("txt_so_off_exchange_bonus")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_offer_exchange_bonus"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_offer_exchange_bonus = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Exchange Bonus";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Exchange Bonus Updated!";
							} else if (name.equals("txt_so_off_corporate")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_offer_corporate" + " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_offer_corporate = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								history_actiontype = "Corporate Amount";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " (" + so_id + ","
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Corporate Amount Updated!";
							} else if (name.equals("txt_so_off_spcl_scheme")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_offer_spcl_scheme FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_offer_spcl_scheme = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								history_actiontype = "Special Schemes";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Special Schemes Updated!";
							} else if (name.equals("txt_so_off_loyalty_bonus")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_offer_loyalty_bonus"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_offer_loyalty_bonus = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								// txt_so_off_loyalty_bonus
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								history_actiontype = "Loyalty Bonus";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Loyalty Bonus Updated!";
							} else if (name.equals("txt_so_off_govtempscheme")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_offer_govtempscheme"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_offer_govtempscheme = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								// txt_so_off_govtempscheme
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);

								history_actiontype = "Govt Emp Scheme";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Govt Emp Scheme Updated!";
							} else if (name.equals("dr_so_fintype_id")) {
								StrHTML = "";
								if (value.equals("0")) {
									StrHTML = "<font color=\"red\">Select Finance Type!</font>";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT COALESCE(fintype_name, '') AS fintype_name"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " LEFT JOIN "
											+ compdb(comp_id)
											+ "axela_sales_so_finance_type ON fintype_id = so_fintype_id"
											+ " WHERE so_id = " + so_id);

									StrSql = "UPDATE " + compdb(comp_id)
											+ "axela_sales_so" + " SET"
											+ " so_fintype_id = '" + value + "'"
											+ " WHERE so_id = " + so_id + "";
									updateQuery(StrSql);

									String history_newvalue = ExecuteQuery("SELECT COALESCE(fintype_name, '') AS fintype_name"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " LEFT JOIN "
											+ compdb(comp_id)
											+ "axela_sales_so_finance_type ON fintype_id = so_fintype_id"
											+ " WHERE so_fintype_id = " + value);

									history_actiontype = "Finance Type";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " " + emp_id
											+ "," + " '" + ToLongDate(kknow())
											+ "'," + " '" + history_actiontype
											+ "'," + " '" + history_oldvalue + "',"
											+ " '" + history_newvalue + "')";
									updateQuery(StrSql);
									StrHTML = "Finance Type Updated!";
								}
							} else if (name.equals("dr_finance_by")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT COALESCE(fincomp_name, '') AS fincomp_name"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " LEFT JOIN "
										+ compdb(comp_id)
										+ "axela_finance_comp ON fincomp_id = so_fincomp_id"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_fincomp_id = " + value + ""
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								String history_newvalue = ExecuteQuery("SELECT COALESCE(fincomp_name, '') AS fincomp_name"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " LEFT JOIN "
										+ compdb(comp_id)
										+ "axela_finance_comp ON fincomp_id = so_fincomp_id"
										+ " WHERE so_fincomp_id = " + value);

								history_actiontype = "Finance By";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES" + " ("
										+ so_id + "," + " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Finance By Updated!";

							} else if (name.equals("txt_so_finance_amt")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_finance_amt"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_finance_amt = " + CNumeric(value) + ""
										+ " WHERE so_id = " + so_id + "";
								// SOP("StrSql=txt_so_finance_amt==" + StrSql);
								updateQuery(StrSql);
								history_actiontype = "Finance Amount";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Finance Amount Updated!";

								if (!so_retail_date.equals("0")) {
									new Incentive_Calculate().IncentiveUpdate(comp_id, branch_brand_id, so_incentive_emp_id, so_retail_date.substring(0, 6));
								}

							} else if (name.equals("txt_so_finance_gross")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_finance_gross"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_finance_gross = " + CNumeric(value) + ""
										+ " WHERE so_id = " + so_id + "";
								// SOP("StrSql=txt_so_finance_amt==" + StrSql);
								updateQuery(StrSql);
								history_actiontype = "Finance Gross";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Finance Gross Updated!";
							} else if (name.equals("txt_so_finance_net")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_finance_net"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_finance_net = " + CNumeric(value) + ""
										+ " WHERE so_id = " + so_id + "";
								// SOP("StrSql=txt_so_finance_amt==" + StrSql);
								updateQuery(StrSql);
								history_actiontype = "Finance Net";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Finance Net Updated!";

								// txt_so_finance_net
								new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);
							}
							else if (name.equals("txt_so_refund_amount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_refund_amount"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_refund_amount = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Excess Refund";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Excess Refund Updated!";
							} else if (name.equals("txt_so_bankrefund_amount")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT so_bankrefund_amount"
										+ " FROM " + compdb(comp_id) + "axela_sales_so"
										+ " WHERE so_id = " + so_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
										+ " SET"
										+ " so_bankrefund_amount = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Bank Refund";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ CNumeric(value) + "')";
								updateQuery(StrSql);
								StrHTML = "Bank Refund Updated!";
							}
							else if (name.equals("dr_saletype_id")) {
								StrHTML = "";
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT saletype_name"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_saletype ON saletype_id = so_saletype_id"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_saletype_id = " + CNumeric(value)
										+ "" + " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);
								history_actiontype = "Sale Type";
								String history_newvalue = ExecuteQuery("SELECT saletype_name"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " INNER JOIN " + compdb(comp_id) + " axela_sales_so_saletype ON saletype_id = so_saletype_id"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id," + " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES" + " ("
										+ so_id + "," + " " + emp_id + "," + " '"
										+ ToLongDate(kknow()) + "'," + " '"
										+ history_actiontype + "'," + " '"
										+ history_oldvalue + "'," + " '"
										+ history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Sale Type Updated!";
							}
							// End Accounts
						} else {
							if (StrHTML.equals("")) {
								// SOP("coming..8");
								StrHTML = "Update Permission Denied!";
							}
						}
					} else {
						// SOP("coming..9");
						StrHTML = "Update Permission Denied!";
					}

					if ((ReturnPerm(comp_id, "emp_sales_order_add", request).equals("1")
							&& ReturnPerm(comp_id, "emp_acc_receipt_add", request).equals("1")) ||
							(ReturnPerm(comp_id, "emp_sales_order_add", request).equals("1")
							&& ReturnPerm(comp_id, "emp_acc_receipt_add", request).equals("0"))) {
						// Registration Tab
						// SOP("name====" + name);
						// SOP("value====" + value);
						// StrHTML = "";
						if (name.equals("dr_so_reg_rc_delivery")) {
							StrHTML = "";
							String history_oldvalue = ExecuteQuery("SELECT so_reg_rc_delivery"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id);
							if (history_oldvalue.equals("1")) {
								history_oldvalue = "Home";
							} else if (history_oldvalue.equals("2")) {
								history_oldvalue = "Showroom";
							} else {
								history_oldvalue = "";
							}
							StrSql = "UPDATE " + compdb(comp_id)
									+ "axela_sales_so" + " SET"
									+ " so_reg_rc_delivery = '"
									+ CNumeric(value) + "'" + " WHERE so_id = "
									+ so_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("SELECT so_reg_rc_delivery"
									+ " FROM " + compdb(comp_id) + "axela_sales_so"
									+ " WHERE so_id = " + so_id);

							if (history_newvalue.equals("1")) {
								history_newvalue = "Home";
							} else if (history_newvalue.equals("2")) {
								history_newvalue = "Showroom";
							} else {
								history_newvalue = "";
							}
							history_actiontype = "RC Delivery";

							StrSql = "INSERT INTO " + compdb(comp_id)
									+ "axela_sales_so_history"
									+ " (history_so_id," + " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)" + " VALUES" + " ("
									+ so_id + "," + " " + emp_id + "," + " '"
									+ ToLongDate(kknow()) + "'," + " '"
									+ history_actiontype + "'," + " '"
									+ history_oldvalue + "'," + " '"
									+ history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "RC Delivery Updated!";
						}
						else if (name.equals("chk_so_reg_hsrp")) {
							StrHTML = "";
							if (!value.equals("")) {
								String history_oldvalue = ExecuteQuery("SELECT so_reg_hsrp"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);
								if (history_oldvalue.equals("1")) {
									history_oldvalue = "HSRP";
								} else {
									history_oldvalue = "Non-HSRP";
								}

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_reg_hsrp = '" + value + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								String history_newvalue = ExecuteQuery("SELECT so_reg_hsrp"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								if (history_newvalue.equals("1")) {
									history_newvalue = "HSRP";
								} else {
									history_newvalue = "Non-HSRP";
								}

								history_actiontype = "Sales Order HSRP";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES"
										+ " (" + so_id + "," + " " + emp_id
										+ "," + " '" + ToLongDate(kknow())
										+ "'," + " '" + history_actiontype
										+ "'," + " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "HSRP updated!";
							}
						} else if (name.equals("txt_so_reg_hsrp_received_date")) {
							StrHTML = "";
							if (!isValidDateFormatShort(value)
									&& !value.equals("")) {
								StrHTML = "<font color=\"red\">Enter Valid HSRP Received Date!</font>";
							} else {
								StrSql = "SELECT so_date"
										// +
										// " , COALESCE(invoice_date, '') AS invoice_date"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										// + " LEFT JOIN "
										// + compdb(comp_id)
										// +
										// "axela_invoice ON invoice_so_id = so_id"
										+ " WHERE so_id = " + so_id + "";
								crs = processQuery(StrSql, 0);

								while (crs.next()) {
									so_date = crs.getString("so_date");
									// invoice_date =
									// crs.getString("invoice_date");
								}
								crs.close();

								if (!value.equals("")) {
									if (Long.parseLong(so_date) > Long
											.parseLong(ConvertShortDateToStr(value))) {
										StrHTML = "<font color=\"red\">HSRP Received Date must be greater than or equal to Sales Order Date!</font>";
									}
								}

								if (StrHTML.equals("")) {
									String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_reg_hsrp_received_date"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " WHERE so_id = " + so_id));

									if (!value.equals("")) {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_hsrp_received_date = '"
												+ Long.parseLong(ConvertShortDateToStr(value))
												+ "'" + " WHERE so_id = "
												+ so_id + "";
									} else {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_hsrp_received_date = ''"
												+ " WHERE so_id = " + so_id
												+ "";
									}
									updateQuery(StrSql);

									history_actiontype = "HSRP Received Date";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " "
											+ emp_id + "," + " '"
											+ ToLongDate(kknow()) + "'," + " '"
											+ history_actiontype + "'," + " '"
											+ history_oldvalue + "'," + " '"
											+ value + "')";
									updateQuery(StrSql);
									StrHTML += "HSRP Received Date updated!";
								}
							}
						} else if (name.equals("txt_so_reg_hsrp_install_date")) {
							StrHTML = "";
							if (!isValidDateFormatShort(value)
									&& !value.equals("")) {
								StrHTML = "<font color=\"red\">Enter Valid HSRP Installation Date!</font>";
							} else {
								StrSql = "SELECT so_date"
										// +
										// " , COALESCE(invoice_date, '') AS invoice_date"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										// + " LEFT JOIN "
										// + compdb(comp_id)
										// +
										// "axela_invoice ON invoice_so_id = so_id"
										+ " WHERE so_id = " + so_id + "";
								crs = processQuery(StrSql, 0);

								while (crs.next()) {
									so_date = crs.getString("so_date");
									// invoice_date =
									// crs.getString("invoice_date");
								}
								crs.close();

								if (!value.equals("")) {
									if (Long.parseLong(so_date) > Long
											.parseLong(ConvertShortDateToStr(value))) {
										StrHTML = "<font color=\"red\">HSRP Installation Date must be greater than or equal to Sales Order Date!</font>";
									}
								}

								if (StrHTML.equals("")) {
									String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_reg_hsrp_install_date"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " WHERE so_id = " + so_id));

									if (!value.equals("")) {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_hsrp_install_date = '"
												+ Long.parseLong(ConvertShortDateToStr(value))
												+ "'" + " WHERE so_id = "
												+ so_id + "";
									} else {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_hsrp_install_date = ''"
												+ " WHERE so_id = " + so_id
												+ "";
									}
									updateQuery(StrSql);

									history_actiontype = "HSRP Installation Date";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " "
											+ emp_id + "," + " '"
											+ ToLongDate(kknow()) + "'," + " '"
											+ history_actiontype + "'," + " '"
											+ history_oldvalue + "'," + " '"
											+ value + "')";
									updateQuery(StrSql);
									StrHTML += "HSRP Installation Date updated!";
								}
							}
						}
						else if (name.equals("txt_so_reg_perm_reg_no")) {
							StrHTML = "";
							if (!value.equals("")) {
								if (value.length() >= 2) {
									StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
											+ " WHERE veh_reg_no = '" + value + "'"
											+ " AND veh_so_id != " + so_id + "";
									if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
										value = value.replace(" ", "");
										String history_oldvalue = ExecuteQuery("SELECT so_reg_perm_reg_no"
												+ " FROM " + compdb(comp_id) + "axela_sales_so"
												+ " WHERE so_id = " + so_id);

										StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
												+ " SET so_reg_perm_reg_no = '" + value.toUpperCase() + "'"
												+ " , so_reg_no = '" + value.toUpperCase() + "'"
												+ " WHERE so_id = " + so_id + "";
										updateQuery(StrSql);

										// Updating Reg. No. to the Vehicle table for the same SO ID.
										StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
												+ " SET veh_reg_no = '" + value.toUpperCase() + "'"
												+ " WHERE veh_so_id = " + so_id + "";
										updateQuery(StrSql);

										history_actiontype = "Permanent Registration Number";

										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history"
												+ " (history_so_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES" + " (" + so_id
												+ "," + " " + emp_id + ","
												+ " '" + ToLongDate(kknow())
												+ "'," + " '"
												+ history_actiontype + "',"
												+ " '" + history_oldvalue
												+ "'," + " '" + value + "')";
										updateQuery(StrSql);
										StrHTML = "Permanent Registration Number Updated!";
									} else {
										StrHTML = "<font color=\"red\">Similar Permanent Registration Number found!</font>";
									}
								} else {
									StrHTML = "<font color=\"red\">Permanent Registration Number should be atleast Two Digits!</font>";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Permanent Registration Number!</font>";
							}
						} else if (name.equals("txt_so_reg_rc_received_date")) {
							StrHTML = "";
							if (!isValidDateFormatShort(value)
									&& !value.equals("")) {
								StrHTML = "<font color=\"red\">Enter valid RC Received Date!</font>";
							} else {
								StrSql = "SELECT so_date"
										// +
										// " , COALESCE(invoice_date, '') AS invoice_date"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										// + " LEFT JOIN "
										// + compdb(comp_id)
										// +
										// "axela_invoice ON invoice_so_id = so_id"
										+ " WHERE so_id = " + so_id + "";
								crs = processQuery(StrSql, 0);

								while (crs.next()) {
									so_date = crs.getString("so_date");
									// invoice_date =
									// crs.getString("invoice_date");
								}
								crs.close();

								if (!value.equals("")) {
									if (Long.parseLong(so_date) > Long
											.parseLong(ConvertShortDateToStr(value))) {
										StrHTML = "<font color=\"red\">RC Received Date must be greater than or equal to Sales Order Date!</font>";
									}
								}

								if (StrHTML.equals("")) {
									String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_reg_rc_received_date"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " WHERE so_id = " + so_id));

									if (!value.equals("")) {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_rc_received_date = '"
												+ Long.parseLong(ConvertShortDateToStr(value))
												+ "'" + " WHERE so_id = "
												+ so_id + "";
									} else {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_rc_received_date = ''"
												+ " WHERE so_id = " + so_id
												+ "";
									}
									updateQuery(StrSql);

									history_actiontype = "RC Received Date";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " "
											+ emp_id + "," + " '"
											+ ToLongDate(kknow()) + "'," + " '"
											+ history_actiontype + "'," + " '"
											+ history_oldvalue + "'," + " '"
											+ value + "')";
									updateQuery(StrSql);
									StrHTML += "RC Received Date updated!";
								}
							}
						} else if (name.equals("txt_so_reg_rc_handover_date")) {
							StrHTML = "";
							if (!isValidDateFormatShort(value)
									&& !value.equals("")) {
								StrHTML = "<font color=\"red\">Enter Valid RC Handover Date!</font>";
							} else {
								StrSql = "SELECT so_date "
										// +
										// ", COALESCE(invoice_date, '') AS invoice_date"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										// + " LEFT JOIN "
										// + compdb(comp_id)
										// +
										// "axela_invoice ON invoice_so_id = so_id"
										+ " WHERE so_id = " + so_id + "";
								crs = processQuery(StrSql, 0);

								while (crs.next()) {
									so_date = crs.getString("so_date");
									// invoice_date =
									// crs.getString("invoice_date");
								}
								crs.close();

								if (!value.equals("")) {
									if (Long.parseLong(so_date) > Long
											.parseLong(ConvertShortDateToStr(value))) {
										StrHTML = "<font color=\"red\">RC Handover Date must be greater than or equal to Sales Order Date!</font>";
									}
								}

								if (StrHTML.equals("")) {
									String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_reg_rc_handover_date"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " WHERE so_id = " + so_id));

									if (!value.equals("")) {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_rc_handover_date = '"
												+ Long.parseLong(ConvertShortDateToStr(value))
												+ "'" + " WHERE so_id = "
												+ so_id + "";
									} else {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_reg_rc_handover_date = ''"
												+ " WHERE so_id = " + so_id
												+ "";
									}
									updateQuery(StrSql);

									history_actiontype = "RC Handover Date";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " "
											+ emp_id + "," + " '"
											+ ToLongDate(kknow()) + "'," + " '"
											+ history_actiontype + "'," + " '"
											+ history_oldvalue + "'," + " '"
											+ value + "')";
									updateQuery(StrSql);
									StrHTML += "RC Handover Date updated!";
								}
							}
						} else if (name.equals("txt_so_reg_notes")) {
							StrHTML = "";
							value = value.replaceAll("nbsp", "&");

							if (value.length() > 2000) {
								value = value.substring(0, 1999);
							}

							String history_oldvalue = ExecuteQuery("SELECT so_reg_notes"
									+ " FROM "
									+ compdb(comp_id)
									+ "axela_sales_so"
									+ " WHERE so_id = "
									+ so_id);

							StrSql = "UPDATE " + compdb(comp_id)
									+ "axela_sales_so" + " SET"
									+ " so_reg_notes = '" + value + "'"
									+ " WHERE so_id = " + so_id + "";
							updateQuery(StrSql);

							history_actiontype = "Notes";

							StrSql = "INSERT INTO " + compdb(comp_id)
									+ "axela_sales_so_history"
									+ " (history_so_id," + " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)" + " VALUES" + " ("
									+ so_id + "," + " " + emp_id + "," + " '"
									+ ToLongDate(kknow()) + "'," + " '"
									+ history_actiontype + "'," + " '"
									+ history_oldvalue + "'," + " '" + value
									+ "')";
							updateQuery(StrSql);
							StrHTML = "Notes Updated!";
						}
						else if (name.equals("txt_so_rcdel_person_received")) {
							StrHTML = "";
							if (!value.equals("")) {
								StrSql = "SELECT so_id FROM " + compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_rcdel_person_received = '"
										+ value.toUpperCase() + "'"
										+ " AND so_id != " + so_id + "";

								value = value.replace(" ", "");
								String history_oldvalue = ExecuteQuery("SELECT so_rcdel_person_received"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_rcdel_person_received = '"
										+ value.toUpperCase() + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Delivery Person Received";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES"
										+ " (" + so_id + "," + " " + emp_id
										+ "," + " '" + ToLongDate(kknow())
										+ "'," + " '" + history_actiontype
										+ "'," + " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Delivery Person Received Updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Delivery Person Received!</font>";
							}
						} else if (name.equals("txt_so_rcdel_person_contact_no")) {
							StrHTML = "";
							if (!value.equals("")) {
								StrSql = "SELECT so_id FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_rcdel_person_contact_no = '"
										+ value.toUpperCase() + "'"
										+ " AND so_id != " + so_id + "";

								value = value.replace(" ", "");
								String history_oldvalue = ExecuteQuery("SELECT so_rcdel_person_contact_no"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_rcdel_person_contact_no = '"
										+ value.toUpperCase() + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Delivery Person Contact No.";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES"
										+ " (" + so_id + "," + " " + emp_id
										+ "," + " '" + ToLongDate(kknow())
										+ "'," + " '" + history_actiontype
										+ "'," + " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Delivery Person Contact No. Updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Delivery Person Contact No.!</font>";
							}
						} else if (name.equals("txt_so_rcdel_person_relation")) {
							StrHTML = "";
							if (!value.equals("")) {
								StrSql = "SELECT so_id FROM " + compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_rcdel_person_relation = '"
										+ value.toUpperCase() + "'"
										+ " AND so_id != " + so_id + "";

								value = value.replace(" ", "");
								String history_oldvalue = ExecuteQuery("SELECT so_rcdel_person_relation"
										+ " FROM "
										+ compdb(comp_id)
										+ "axela_sales_so"
										+ " WHERE so_id = "
										+ so_id);

								StrSql = "UPDATE " + compdb(comp_id)
										+ "axela_sales_so" + " SET"
										+ " so_rcdel_person_relation = '"
										+ value.toUpperCase() + "'"
										+ " WHERE so_id = " + so_id + "";
								updateQuery(StrSql);

								history_actiontype = "Delivery Person Relation";

								StrSql = "INSERT INTO " + compdb(comp_id)
										+ "axela_sales_so_history"
										+ " (history_so_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)" + " VALUES"
										+ " (" + so_id + "," + " " + emp_id
										+ "," + " '" + ToLongDate(kknow())
										+ "'," + " '" + history_actiontype
										+ "'," + " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Delivery Person Relation Updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Delivery Person Relation!</font>";
							}
						} else if (name.equals("txt_so_rcdel_delivery_time")) {
							StrHTML = "";
							if (!isValidDateFormatLong(value)
									&& !value.equals("")) {
								StrHTML = "<font color=\"red\">Enter Valid Delivery Time!</font>";
							} else {
								// StrSql =
								// "SELECT so_date, COALESCE(invoice_date, '') AS invoice_date"
								// + " FROM " + compdb(comp_id) +
								// "axela_sales_so"
								// + " LEFT JOIN " + compdb(comp_id) +
								// "axela_invoice ON invoice_so_id = so_id"
								// + " WHERE so_id = " + so_id + "";
								// CachedRowSet crs =processQuery(StrSql, 0);
								//
								// while (crs.next()) {
								// so_date = crs.getString("so_date");
								// invoice_date = crs.getString("invoice_date");
								// }
								// crs.close();

								// if (!value.equals("")) {
								// if (Long.parseLong(so_date) >
								// Long.parseLong(ConvertLongDateToStr(value)))
								// {
								// StrHTML =
								// "<font color=\"red\">Delivery Time must be greater than or equal to Sales Order Date!</font>";
								// }
								// }

								if (StrHTML.equals("")) {
									String history_oldvalue = strToShortDate(ExecuteQuery("SELECT so_rcdel_delivery_time"
											+ " FROM "
											+ compdb(comp_id)
											+ "axela_sales_so"
											+ " WHERE so_id = " + so_id));

									if (!value.equals("")) {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_rcdel_delivery_time = '"
												+ Long.parseLong(ConvertLongDateToStr(value))
												+ "'" + " WHERE so_id = "
												+ so_id + "";
									} else {
										StrSql = "UPDATE "
												+ compdb(comp_id)
												+ "axela_sales_so"
												+ " SET"
												+ " so_rcdel_delivery_time = ''"
												+ " WHERE so_id = " + so_id
												+ "";
									}
									updateQuery(StrSql);

									history_actiontype = "Delivery Time";

									StrSql = "INSERT INTO " + compdb(comp_id)
											+ "axela_sales_so_history"
											+ " (history_so_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)" + " VALUES"
											+ " (" + so_id + "," + " "
											+ emp_id + "," + " '"
											+ ToLongDate(kknow()) + "'," + " '"
											+ history_actiontype + "'," + " '"
											+ history_oldvalue + "'," + " '"
											+ value + "')";
									updateQuery(StrSql);
									StrHTML += "Delivery Time updated!";
								}
							}
						}
						// Registration Tab Ends
					} else {
						if (StrHTML.equals("")) {
							// SOP("coming..10");
							StrHTML = "Update Permission Denied!";
						}
					}
				}
			} catch (Exception ex) {
				SOPError(ClientName + "===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
