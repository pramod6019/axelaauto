package axela.accounting;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class Ledger_Check extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String msg = "";
	public String emp_id = "0", temp = "0";
	public String comp_id = "0";
	public String getledger = "", ledger = "", id = "0", pursales = "";
	public String voucher_customer_id = "0", voucher_paydays_id = "0";
	public String entity_id = "0";
	public String curr_balance = "";
	public String currbalance = "", journalcurrbalance = "";
	public String journaldate = "";
	public String outstandingadj = "";
	public String upsubgroup_id = "0", customer_id = "0", contact_id = "0";
	public String vouchertrans_id = "0", voucherid = "0", vouchertype_id = "0";
	public String opening_bal = "", closing_bal = "", total_amount = "";
	public String reconcil_amount = "", diff_amount = "";
	public String itemgroup_id = "0", subgroup_id = "0", pay = "";
	public String invoicedetail = "", expenseledger = "", expledger = "", customer = "";
	public String supplier = "", unallocateddetail = "";
	public String customer_type_id = "0", tax_customer = "";
	public String getreconciledata = "", listreconciledata = "", fromdate = "";
	public String todate = "", status = "", StrSerach = "";
	public String reconcile_check = "0";
	public String reconcile_checkArr[] = null;
	public String reconcile = "";
	Map<Integer, Object> prepmap = new HashMap<Integer, Object>();
	DecimalFormat df = new DecimalFormat("0.00");
	public int prepkey = 1;
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageSpan = 10;
	public int PageCurrent = 0;
	public String QueryString = "";
	public String PageCurrents = "1";
	public String contact_fname = "";
	public String contact_lname = "";
	public String customer_name = "";
	public String search_customer = "";
	public String contact_mobile1 = "";
	public String contact_phone1 = "";
	public String contact_email1 = "", q = "";
	public String rateclass_id = "0";
	public String invoiceno = "";
	public String cdnotedetail = "";
	public String gst_type = "";
	public String taxes = "", taxes2 = "";
	public String ledgerstate = "";
	public String currbal = "", contact = "", address = "";
	public String supplier_rateclass = "", customer_rateclass = "";
	public String payment_date = "", voucher_date = "", voucherdate = "";
	public String voucher_payment_date = "", paydays_days = "0";
	public String addmultiparty = "", partyamount = "0.00", mainpartyamount = "0.00";
	public String deletemultiparty = "", cart_id = "0";
	public String addmultiledger = "", deletemultiledger = "", rowupdate = "";
	public String dramount = "", cramount = "";
	public String session_id = "";
	public String StrSqlSearch = "";
	public String voucher_tax1 = "";
	public String voucher_tax2 = "";
	public String voucher_tax1_id = "0";
	public String voucher_tax2_id = "0";
	public String item = "";
	public String include_zero_bal = "0";
	public String voucherno = "", branch_id = "0";
	public String location_id = "0", voucher_id = "0", customer_branch_id = "0";
	public String state = "", vouchertype_defaultauthorize = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {

		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			if (!GetSession("emp_id", request).equals("")) {
				try {
					emp_id = CNumeric(GetSession("emp_id", request)) + "";
					PageCurrents = PadQuotes(request.getParameter("PageCurrent"));
					recperpage = Integer.parseInt(CNumeric(GetSession("emp_recperpage", request) + ""));
					QueryString = PadQuotes(request.getQueryString());
					id = CNumeric(PadQuotes(request.getParameter("ledgerid")));
					getledger = PadQuotes(request.getParameter("getledger"));
					pursales = PadQuotes(request.getParameter("pursales"));
					ledger = ((PadQuotes(request.getParameter("ledger"))).replace("&#40;", "(")).replace("&#41;", ")");
					itemgroup_id = PadQuotes(request.getParameter("itemgroup_id"));
					upsubgroup_id = PadQuotes(request.getParameter("upsubgroup_id"));
					subgroup_id = PadQuotes(request.getParameter("subgroup_id"));
					invoicedetail = PadQuotes(request.getParameter("invoicedetail"));
					unallocateddetail = PadQuotes(request.getParameter("unallocateddetail"));
					outstandingadj = PadQuotes(request.getParameter("outstandingadj"));
					cdnotedetail = PadQuotes(request.getParameter("cdnotedetail"));
					invoiceno = PadQuotes(request.getParameter("invoiceno"));
					currbalance = PadQuotes(request.getParameter("currbalance"));
					journalcurrbalance = PadQuotes(request.getParameter("journalcurrbalance"));
					journaldate = PadQuotes(request.getParameter("journaldate"));
					vouchertrans_id = PadQuotes(request.getParameter("vouchertrans_id"));
					voucherid = PadQuotes(request.getParameter("voucher_id"));
					expenseledger = PadQuotes(request.getParameter("expnsledger"));
					expledger = PadQuotes(request.getParameter("expledger"));
					pay = PadQuotes(request.getParameter("pay"));
					state = PadQuotes(request.getParameter("state"));
					status = PadQuotes(request.getParameter("status"));
					vouchertype_defaultauthorize = CNumeric(PadQuotes(request.getParameter("vouchertype_defaultauthorize")));
					getreconciledata = PadQuotes(request.getParameter("getreconciledata"));
					listreconciledata = PadQuotes(request.getParameter("listreconciledata"));
					reconcile = PadQuotes(request.getParameter("reconcile"));
					fromdate = PadQuotes(request.getParameter("fromdate"));
					todate = PadQuotes(request.getParameter("todate"));
					customer_type_id = CNumeric(PadQuotes(request.getParameter("customer_type_id")));
					tax_customer = PadQuotes(request.getParameter("tax_customer"));
					search_customer = PadQuotes(request.getParameter("search_customer"));
					contact_fname = PadQuotes(request.getParameter("contact_fname"));
					contact_lname = PadQuotes(request.getParameter("contact_lname"));
					customer_name = PadQuotes(request.getParameter("customer_name"));
					contact_mobile1 = PadQuotes(request.getParameter("contact_mobile"));
					contact_phone1 = PadQuotes(request.getParameter("contact_phone"));
					currbal = PadQuotes(request.getParameter("currbal"));
					vouchertype_id = CNumeric(PadQuotes(request.getParameter("vouchertype_id")));

					customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
					// SOP("customer_id==" + customer_id);
					include_zero_bal = CNumeric(PadQuotes(request.getParameter("include_zero")));

					// for journal multi ledger
					addmultiledger = PadQuotes(request.getParameter("addmultiledger"));
					rowupdate = PadQuotes(request.getParameter("rowupdate"));
					deletemultiledger = PadQuotes(request.getParameter("deletemultiledger"));
					dramount = CNumeric(PadQuotes(request.getParameter("dramount")));
					cramount = CNumeric(PadQuotes(request.getParameter("cramount")));

					// SOP("include_zero_bal===" + include_zero_bal);

					addmultiparty = PadQuotes(request.getParameter("addmultiparty"));
					deletemultiparty = PadQuotes(request.getParameter("deletemultiparty"));
					cart_id = CNumeric(PadQuotes(request.getParameter("cart_id")));
					partyamount = CNumeric(PadQuotes(request.getParameter("party_amount")));
					mainpartyamount = CNumeric(PadQuotes(request.getParameter("main_party_amount")));
					session_id = CNumeric(PadQuotes(request.getParameter("session_id")));
					customer = PadQuotes(request.getParameter("customer"));
					supplier = PadQuotes(request.getParameter("supplier"));
					ledgerstate = PadQuotes(request.getParameter("ledgerstate"));
					taxes = PadQuotes(request.getParameter("taxes"));
					taxes2 = PadQuotes(request.getParameter("taxes2"));
					gst_type = PadQuotes(request.getParameter("txt_gst_type"));
					branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
					if (!customer_id.equals("") && !branch_id.equals("0")) {
						gst_type = GetGstType(customer_id, branch_id, comp_id);
					}
					voucher_tax1 = PadQuotes(request.getParameter("dr_voucher_tax1"));
					if (!voucher_tax1.equals("0") && !voucher_tax1.equals("")) {
						voucher_tax1_id = voucher_tax1.split("_")[0];
						voucher_tax2 = PadQuotes(request.getParameter("dr_voucher_tax2"));
					} else {
						voucher_tax2 = "";
					}
					if (!voucher_tax2.equals("0") && !voucher_tax2.equals("")) {
						voucher_tax2_id = voucher_tax2.split("_")[0];
					}
					// SOP("voucher_tax2_id==ledgercheck==" + voucher_tax2_id);
					// SOP("gst_type==" + gst_type);
					contact = PadQuotes(request.getParameter("contact"));
					customer_rateclass = PadQuotes(request.getParameter("customer_rateclass"));
					supplier_rateclass = PadQuotes(request.getParameter("supplier_rateclass"));
					address = PadQuotes(request.getParameter("address"));
					payment_date = PadQuotes(request.getParameter("payment_date"));
					voucher_date = PadQuotes(request.getParameter("voucher_date"));
					item = PadQuotes(request.getParameter("item"));
					if (taxes.equals("yes")) {
						if (gst_type.equals("state")) {
							StrSqlSearch = " AND customer_taxtype_id IN (3,4)";
						}
						if (gst_type.equals("central")) {
							StrSqlSearch = " AND customer_taxtype_id IN (5)";
						}
						StrHTML = PopulateTax(comp_id, voucher_tax1_id);
					}
					if (taxes2.equals("yes")) {
						if (gst_type.equals("state")) {
							StrSqlSearch = " AND customer_taxtype_id IN (3,4)";
						}
						if (gst_type.equals("central")) {
							StrSqlSearch = " AND customer_taxtype_id IN (5)";
						}
						StrHTML = PopulateTax2(comp_id, voucher_tax2_id);
					}
					if (!contact_lname.equals("")) {
						contact_lname = " " + contact_lname;
					}

					// for voucher no
					voucherno = PadQuotes(request.getParameter("voucherno"));

					location_id = CNumeric(PadQuotes(request.getParameter("voucher_location_id")));
					voucher_id = CNumeric(PadQuotes(request.getParameter("voucher_id")));
					if (voucherno.equals("yes")) {
						if (vouchertype_defaultauthorize.equals("1")) {
							StrSql = "select COALESCE(MAX(voucher_no),0)+1"
									+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
									+ " WHERE 1=1"
									+ " AND voucher_branch_id = " + branch_id
									+ " AND voucher_vouchertype_id = " + vouchertype_id;
							// SOP("StrSql===" + StrSql);
							StrHTML = "<input type='text' id='txt_voucher_no' name='txt_voucher_no'"
									+ " value='" + ExecuteQuery(StrSql) + "'";
							if (status.equals("Add")) {
								StrHTML += " hidden";
							} else {
								StrHTML += " class='form-control'";
							}
							StrHTML += " />";
						}
						// else {
						// StrHTML = "<input type='text' id='txt_voucher_no' name='txt_voucher_no' value='0' hidden />";
						// }
					}
					// For journal
					// add multi ledger entry
					if (addmultiledger.equals("yes")) {

						String flag = "";
						if (!dramount.equals("0")) {
							flag = "1";
						} else if (!cramount.equals("0")) {
							flag = "0";
						}

						if (rowupdate.equals("")) {

							if (!flag.equals("")) {
								AddMultipleLedger(customer_id, flag);
								StrHTML = DisplayMultiLedger(session_id, comp_id, "", "", request);
							}
						} else {
							if (!flag.equals("")) {
								updateMultipleLedger(cart_id, flag);
								StrHTML = DisplayMultiLedger(session_id, comp_id, "", "", request);
							}
						}
					}

					// multiple party entry for payment
					if (addmultiparty.equals("yes")) {
						AddMultipleParty(customer_id, partyamount);
						StrHTML = DisplayMultiParty(session_id, mainpartyamount, comp_id);
					}
					// delete multi party
					if (deletemultiparty.equals("yes")) {
						DeleteMultipleParty(cart_id);
						StrHTML = DisplayMultiParty(session_id, mainpartyamount, comp_id);
					}
					// getting customer curr. balance
					if (!CNumeric(customer_id).equals("0") && currbal.equals("yes")) {
						StrHTML = ReturnCustomerCurrBalance(customer_id, comp_id, vouchertype_id);
					}

					// getting party curr bal for journal
					if (!customer_id.equals("0") && journalcurrbalance.equals("yes")) {
						StrHTML = custcurrbal(customer_id);
					}

					if (!emp_id.equals("0")) {
						if (customer.equals("yes")) {
							// SOP("customer");
							StrHTML = PopulateLedgers("32", "0", comp_id);
						}
						if (supplier.equals("yes")) {
							// SOP("supplier");
							StrHTML = PopulateLedgers("31", "0", comp_id);
						}
						if (getledger.equals("yes")) {
							// SOP("getledger");
							StrHTML = PopulateLedgers("0", "0", comp_id);
						}
						if (!id.equals("0")) {
							// SOP("expenseledger");
							StrHTML = PopulateLedgers("0", "0", comp_id);
						}

						if (expenseledger.equals("yes")) {
							StrHTML = PopulateExpenseLedgers();
						}

						if (expledger.equals("yes")) {
							StrHTML = PopulateExpLedgers("0", "0", comp_id);
						}
						if (getreconciledata.equals("yes")) {
							StrHTML = GetReconcileData();
						}
						if (listreconciledata.equals("yes")) {
							StrHTML = ReconcileListData(request);
						}
						if (pay.equals("yes") || contact.equals("yes")
								|| customer_rateclass.equals("yes")
								|| supplier_rateclass.equals("yes")
								|| address.equals("yes")
								|| payment_date.equals("yes")) {
							voucher_customer_id = CNumeric(customer_id);
							if (contact.equals("yes")) {
								StrHTML = PopulateContact(voucher_customer_id);
							}

							if (!voucher_customer_id.equals("0")) {
								customer_branch_id = ExecuteQuery("SELECT customer_branch_id "
										// + " customer_rateclass_id	"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id = "
										+ voucher_customer_id + "");
							}

							if (address.equals("yes")) {
								StrSql = "SELECT COALESCE(customer_address, '') as customer_address,"
										+ " COALESCE(city_name, '') AS city_name, COALESCE(customer_pin, '') as customer_pin,"
										+ " COALESCE(state_name, '') AS state_name,"
										// +
										// " COALESCE(country_name, '') AS country_name, "
										+ " COALESCE(customer_landmark, '') as customer_landmark"
										// + " COALESCE(zone_name, '') AS zone_name"
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " INNER JOIN  " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
										+ " INNER JOIN  " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
										// + " INNER JOIN  "+compdb(comp_id)+"" +
										// compdb(comp_id) +
										// "axela_country ON country_id = state_country_id"
										// + " LEFT JOIN  "+compdb(comp_id)+"" +
										// compdb(comp_id) +
										// "axela_zone ON zone_id = customer_zone_id"
										+ " WHERE customer_id = "
										+ voucher_customer_id + "";
								// SOP("StrSql==Add==" + StrSqlBreaker(StrSql));
								CachedRowSet crs = processQuery(StrSql, 0);
								while (crs.next()) {
									address = crs.getString("customer_address");
									if (!address.equals("")) {
										address = crs.getString("customer_address");
										if (!crs.getString("city_name").equals("")) {
											address += ", "
													+ crs.getString("city_name");
										}
										if (!crs.getString("customer_pin")
												.equals("")) {
											address += " - "
													+ crs.getString("customer_pin");
										}
										if (!crs.getString("state_name").equals("")) {
											address += ", "
													+ crs.getString("state_name");
										}

										// if (!crs.getString("Country_name")
										// .equals("")) {
										// address += ", "
										// + crs.getString("Country_name")
										// + ".";
										// }

										if (!crs.getString("customer_landmark")
												.equals("")) {
											address += "\nLandmark: "
													+ crs.getString("customer_landmark");
										}
									}
								}
								crs.close();
								StrHTML = address;
							}

							if (payment_date.equals("yes")) {
								StrSql = "SELECT * "
										// + " customer_paydays_id "
										+ " FROM " + compdb(comp_id) + "axela_customer"
										+ " WHERE customer_id="
										+ voucher_customer_id;
								// SOP("StrSq==="+StrSqlBreaker(StrSql));
								paydays_days = CNumeric(ExecuteQuery(StrSql));

								voucher_payment_date = ConvertShortDateToStr(AddDayMonthYear(PadQuotes(voucher_date),
										Integer.parseInt(paydays_days) - 1, 0, 0, 0));

								StrHTML = strToShortDate(voucher_payment_date);
							}
						}
						if (invoicedetail.equals("yes")) {
							StrHTML = PopulatePendingInvoices(request, customer_id, "", vouchertype_id);
						}
						// for displaying aaaunallocated vouchers
						if (unallocateddetail.equals("yes")) {
							StrHTML = PopulateUnAllocatedVoucher(customer_id);
						}
						if (cdnotedetail.equals("yes")) {
							StrHTML = PopulateCDNotePending(request, invoiceno, "", vouchertype_id);

							// StrHTML = PopulateCNoteInvoices(request, customer_id,
							// "", vouchertype_id);
						}
						if (outstandingadj.equals("yes")) {
							StrHTML = PopulateCDNotePending(request, invoiceno, "", vouchertype_id);
						}

						if (item.equals("yes")) {
							StrHTML = PopulateItemLedgers("0", "0", comp_id);
						}
						if (state.equals("yes")) {
							StrHTML = CompareState(comp_id, customer_id, branch_id);
						}
						if (ledgerstate.equals("yes")) {
							StrHTML = GetGstType(customer_id, branch_id, comp_id);
							// SOP("StrHTML======" + StrHTML);
						}

					} else {
						StrHTML = "SignIn";
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			} else {
				StrHTML = "SignIn";
			}
		}
	}
	public String CompareState(String comp_id, String customer_id, String branch_id) {
		String StrSql = "SELECT city_state_id FROM " + compdb(comp_id) + "axela_customer"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
				+ " WHERE 1 = 1"
				+ " AND customer_id = " + customer_id;
		// SOP("For Customer StrSql==" + StrSql);
		String customer_state = CNumeric(PadQuotes(ExecuteQuery(StrSql)));

		StrSql = "SELECT city_state_id"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
				+ " WHERE 1 = 1"
				+ " AND branch_id = " + branch_id;
		// SOP("For Branch StrSql==" + StrSql);
		String branch_state = CNumeric(PadQuotes(ExecuteQuery(StrSql)));

		if (!customer_state.equals("0") && !branch_state.equals("0")) {
			if (customer_state.equals(branch_state))
				StrSql = "state";
			else
				StrSql = "central";
		} else {
			StrSql = "";
		}
		return StrSql;
	}

	public String custcurrbal(String customer_id) {
		String StrSql = "SELECT COALESCE(customer_curr_bal, 0.00) AS customer_curr_bal"
				+ " FROM " + compdb(comp_id) + "axela_customer"
				+ " WHERE customer_id = " + customer_id;
		// SOP("StrSql=="+StrSqlBreaker(StrSql));
		return ExecuteQuery(StrSql);
	}

	public String PopulateLedgers(String accsubgroup_id, String ledger_id, String comp_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		String str1 = "";
		str1 = "SELECT customer_id,"
				+ " customer_code,"
				+ "	COALESCE(CONCAT(customer_name,' (', customer_id, ')')) AS customer_name"
				+ " FROM " + compdb(comp_id) + "axela_customer"
				+ " WHERE 1=1 "
				+ " AND customer_id = " + ledger_id;
		try {
			if (!ledger_id.equals("0") && !ledger_id.equals("")) {
				String str = "";
				str = "SELECT customer_id,"
						+ " customer_code,"
						+ "	COALESCE(CONCAT(customer_name,' (', customer_id, ')')) AS customer_name"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " WHERE 1=1 "
						+ " AND customer_id = " + ledger_id
						+ " LIMIT 0, 10 ";
				CachedRowSet crs = processQuery(str, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						str = "<option value=\"" + crs.getString("customer_id") + "\">" + unescapehtml(crs.getString("customer_name")) + "</option>";
					}
				}
				crs.close();
				return str;
			} else {
				if (!ledger.equals("")) {

					StrSql = "SELECT customer_id, customer_code, COALESCE(customer_name, '') AS customer_name"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE 1=1 ";

					if (!ledger.equals("")) {
						StrSql += " AND (CONCAT (customer_name,' (',customer_id,')') LIKE ?"
								+ " OR customer_code LIKE ?"
								+ " OR customer_mobile1 LIKE ?"
								+ " OR customer_mobile2 LIKE ?"
								+ " OR customer_id LIKE ? )";
						prepmap.put(prepkey++, ledger + "%");
						prepmap.put(prepkey++, ledger + "%");
						prepmap.put(prepkey++, "%" + ledger + "%");
						prepmap.put(prepkey++, "%" + ledger + "%");
						prepmap.put(prepkey++, ledger + "%");
					}
					if (!accsubgroup_id.equals("0")) {
						StrSql += " AND customer_accgroup_id = ?";
						prepmap.put(prepkey++, accsubgroup_id);
					}
					if (pursales.equals("yes")) {
						StrSql += " AND (customer_accgroup_id = ?"
								+ " OR customer_accgroup_id = ? "
								+ " OR customer_accgroup_id = ?"
								+ " OR customer_accgroup_id = ?)";
						prepmap.put(prepkey++, 69);
						prepmap.put(prepkey++, 70);
						prepmap.put(prepkey++, 81);
						prepmap.put(prepkey++, 77);
					}
					if (!id.equals("0")) {
						StrSql += " AND customer_id = ?";
						prepmap.put(prepkey++, id);
					}
					StrSql += " GROUP BY customer_id"
							+ " ORDER BY customer_name LIMIT 20";
					// SOP("StrSql====" + StrSql);

					CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
					prepmap.clear();
					prepkey = 1;
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!ledger.equals("")) {
								map.put("id", crs.getString("customer_id"));
								map.put("text", unescapehtml(crs.getString("customer_name")) + " (" + crs.getString("customer_id") + ")");
								list.add(gson.toJson(map));
							}
							if (!id.equals("0")) {
								output.put("text", unescapehtml(crs.getString("customer_name")) + " ("
										+ crs.getString("customer_id") + ")");
							}
						}
						if (!ledger.equals("")) {
							map.clear();
							output.put("ledgers", list);
							list.clear();
						}
					} else {
						if (!ledger.equals("")) {
							output.put("ledgers", "");
						}

						if (!id.equals("0")) {
							output.put("text", "");
						}
					}
					crs.close();
				}
				return JSONPadQuotes(output.toString());
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItemLedgers(String accgroup_id, String ledger_id, String comp_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			if (!ledger_id.equals("0") && !ledger_id.equals("")) {
				String str = "";
				str = "SELECT customer_id, CONCAT(accgrouppop_name,' : ',customer_name) AS customer_name"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
						+ " WHERE 1=1 "
						+ " AND customer_id = " + ledger_id;
				// SOP("Ledger Check --PopulateLedgers()---str===" + str);
				CachedRowSet crs = processQuery(str, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						str = "<option value=\"" + crs.getString("customer_id") + "\">" + unescapehtml(crs.getString("customer_name")) + "</option>";
					}
				}
				crs.close();
				return str;
			} else {
				if (!ledger.equals("")) {

					StrSql = "SELECT customer_id, CONCAT(accgrouppop_name,' : ',customer_name) AS customer_name"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group_pop ON accgrouppop_id = customer_accgroup_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = accgrouppop_id"
							+ " WHERE 1=1 ";

					if (!ledger.equals("")) {
						StrSql += " AND (CONCAT (accgrouppop_name,' : ', customer_name,' (',customer_id,')') LIKE ?"
								+ " OR customer_code LIKE ? )";
						prepmap.put(prepkey++, "%" + ledger + "%");
						prepmap.put(prepkey++, "%" + ledger + "%");
					}
					if (!accgroup_id.equals("0")) {
						StrSql += " AND customer_accgroup_id = ?";
						prepmap.put(prepkey++, accgroup_id);
					}
					if (pursales.equals("yes")) {
						StrSql += " AND (customer_accgroup_id = ?"
								+ " OR customer_accgroup_id = ? "
								+ " OR customer_accgroup_id = ?"
								+ " OR customer_accgroup_id = ?)";
						prepmap.put(prepkey++, 69);
						prepmap.put(prepkey++, 70);
						prepmap.put(prepkey++, 81);
						prepmap.put(prepkey++, 77);
					}
					if (!id.equals("0")) {
						StrSql += " AND customer_id = ?";
						prepmap.put(prepkey++, id);
					}
					StrSql += " GROUP BY customer_id"
							+ " ORDER BY customer_name LIMIT 30";

					// SOP("StrSql====" + StrSql);

					CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
					prepmap.clear();
					prepkey = 1;
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!ledger.equals("")) {
								map.put("id", crs.getString("customer_id"));
								map.put("text", unescapehtml(crs.getString("customer_name")) + " (" + crs.getString("customer_id") + ")");
								list.add(gson.toJson(map));
							}
							if (!id.equals("0")) {
								output.put("text", unescapehtml(crs.getString("customer_name")) + " ("
										+ crs.getString("customer_id") + ")");
							}
						}
						if (!ledger.equals("")) {
							map.clear();
							output.put("ledgers", list);
							list.clear();
						}
					} else {
						if (!ledger.equals("")) {
							output.put("ledgers", "");
						}

						if (!id.equals("0")) {
							output.put("text", "");
						}
					}
					crs.close();
				}
				return JSONPadQuotes(output.toString().replace("u003e", ">"));
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExpLedgers(String accsubgroup_id, String ledger_id, String comp_id) {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		id = CNumeric(PadQuotes(ledger_id));
		try {

			if (!ledger_id.equals("0") && !ledger_id.equals("")) {
				String str = "";
				str = "SELECT"
						+ " customer_id, CONCAT(customer_name,\" - \",accgroup_name) AS customer_name,"
						+ " customer_code, customer_code, customer_accgroup_id"
						+ " FROM " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_subgroup ON accsubgroup_id = customer_accgroup_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id"
						+ " WHERE 1=1 "
						+ " AND customer_active != 0"
						+ " AND accgroup_alie = 4"
						// + " AND accgroup_id IN (17, 21, 24, 56)"
						// 17 = DIRECT (EXPENSES)
						// 21 = INDIRECT EXPENSES
						// 24 = MISC. EXPENSES (ASSETS)
						// 56 = EXPENSES (P/L)
						+ " AND customer_id = " + ledger_id;
				// + " LIMIT 0, 10 ";
				// SOP("Str======" + str);
				CachedRowSet crs = processQuery(str, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						str = "<option value=\"" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</option>";
					}
				}
				crs.close();
				return str;
			} else {
				if (!ledger.equals("")) {

					StrSql = "SELECT"
							+ " customer_id, CONCAT(customer_name,\" - \",accgroup_name) AS customer_name,"
							+ " customer_code, customer_code, customer_accgroup_id"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							// + " INNER JOIN " + compdb(comp_id) + "axela_acc_subgroup ON accsubgroup_id = customer_accgroup_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id"
							+ " WHERE 1=1 "
							+ " AND customer_active != 0"
							// + " AND accgroup_id IN (17, 21, 24, 56)"
							+ " AND accgroup_alie = 4"
							+ " AND customer_name LIKE ('%" + ledger + "%')"
							+ " GROUP BY customer_id"
							+ " ORDER BY customer_name LIMIT 20";
					CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
					prepmap.clear();
					prepkey = 1;
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							if (!ledger.equals("")) {
								map.put("id", crs.getString("customer_id"));
								map.put("text", crs.getString("customer_name")
										+ " (" + crs.getString("customer_id") + ")");
								list.add(gson.toJson(map));
							}
							if (!id.equals("0")) {
								output.put("text", crs.getString("customer_name")
										+ " (" + crs.getString("customer_id") + ")");
							}
						}
						if (!ledger.equals("")) {
							map.clear();
							output.put("ledgers", list);
							list.clear();
						}
					} else {
						if (!ledger.equals("")) {
							output.put("ledgers", "");
						}

						if (!id.equals("0")) {
							output.put("text", "");
						}
					}
					crs.close();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return JSONPadQuotes(output.toString());
	}
	public String PopulateExpenseLedgers() {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		try {
			StrSql = "SELECT customer_id, COALESCE(customer_name, '') AS customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					// + " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_subgroup ON accsubgroup_id = customer_accgroup_id"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_group ON accgroup_id = customer_accgroup_id";
			if (!ledger.equals("")) {
				StrSql += " WHERE 1=1" + " AND customer_name LIKE ?"
						+ " AND (accgroup_alie = ? OR accgroup_alie = ?)";
				prepmap.put(prepkey++, "%" + ledger + "%");
				prepmap.put(prepkey++, "2");
				prepmap.put(prepkey++, "4");

			} else if (!id.equals("0")) {
				// SOP("id===" + id);
				StrSql += " WHERE customer_id = ?";
				prepmap.put(prepkey++, id);
			}
			StrSql += " GROUP BY customer_id" + " ORDER BY customer_name";
			if (!ledger.equals("")) {
				StrSql += " LIMIT 10";
			}
			// SOP("StrSql==ledger check===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (!ledger.equals("")) {
						map.put("id", crs.getString("customer_id"));
						map.put("text", crs.getString("customer_name"));
						list.add(gson.toJson(map));
					}
					if (!id.equals("0")) {
						output.put("text", crs.getString("customer_name"));
					}
				}
				if (!ledger.equals("")) {
					map.clear();
					output.put("ledgers", list);
					list.clear();
				}
			} else {
				if (!ledger.equals("")) {
					output.put("ledgers", "");
				}

				if (!id.equals("0")) {
					output.put("text", "");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return JSONPadQuotes(output.toString());
	}

	public String PopulatePendingInvoices(HttpServletRequest request,
			String vouchertrans_customer_id, String voucher_id,
			String vouchertype_id) {
		// SOP("dopost");
		voucher_id = CNumeric(voucher_id);
		String voucherbal_trans_id = "";
		String voucherbal_trans_idarr[] = null;
		CachedRowSet crs;
		comp_id = CNumeric(GetSession("comp_id", request));
		try {
			StringBuilder Str = new StringBuilder();

			int count = 0, invcount = 0, totalcount = 0;
			double balamt = 0, balamount = 0.00;
			String voucherbal_amount = "0";
			StrSql = "SELECT voucher_id, voucher_date,"
					+ " CONCAT(vouchertype_prefix,voucher_no,vouchertype_suffix) AS voucher_no,"
					+ " voucher_ref_no,"
					+ " vouchertype_id,"
					+ " (DATEDIFF(SYSDATE(),vou.voucher_date)-1) AS duedays,"
					+ " @vocheramount:=voucher_amount AS voucher_amount,"
					+ " @receipt:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher rcpt"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = rcpt.voucher_id AND rcpt.voucher_vouchertype_id = 9"
					+ " WHERE 1=1"
					+ " AND rcpt.voucher_active = 1"
					+ " AND rcpt.voucher_branch_id = branch_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS receipt,"
					+ " @payment:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher pymt"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = pymt.voucher_id AND pymt.voucher_vouchertype_id = 15"
					+ " WHERE 1=1"
					+ " AND pymt.voucher_active = 1"
					// + " AND pymt.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS payment,"
					+ " @creditnote:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher cdn"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = cdn.voucher_id AND cdn.voucher_vouchertype_id = 10"
					+ " WHERE 1=1"
					+ " AND cdn.voucher_active = 1"
					+ " AND cdn.voucher_branch_id = branch_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS creditnote,"
					+ " @debitnote:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher dbn"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = dbn.voucher_id AND dbn.voucher_vouchertype_id = 11"
					+ " WHERE 1=1"
					+ " AND dbn.voucher_active = 1"
					+ " AND dbn.voucher_branch_id = branch_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS debitnote,"
					+ " @journal:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher jr"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = jr.voucher_id AND jr.voucher_vouchertype_id = 18"
					+ " WHERE 1=1"
					+ " AND jr.voucher_active = 1"
					+ " AND jr.voucher_branch_id = branch_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS journal,"
					+ " @salesreturn:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher sr"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = sr.voucher_id AND sr.voucher_vouchertype_id = 23"
					+ " WHERE 1=1"
					+ " AND sr.voucher_active = 1"
					// + " AND sr.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS salesreturn,"
					+ " @purchasereturn:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher pr"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = pr.voucher_id AND pr.voucher_vouchertype_id = 24"
					+ " WHERE 1=1"
					+ " AND pr.voucher_active = 1"
					+ " AND pr.voucher_branch_id = branch_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS purchasereturn,"
					+ " COALESCE((case when vou.voucher_vouchertype_id = 6"
					+ " then @vocheramount - @receipt + @payment + @debitnote - @creditnote - @salesreturn"
					+ " when vou.voucher_vouchertype_id  = 21"
					+ " then cast(@vocheramount + @receipt - @payment - @debitnote + @creditnote - @purchasereturn as DECIMAL(10,2))"
					+ " end "
					+ " ),0.00) AS balance";
			if (!voucher_id.equals("0")) {
				StrSql += " , coalesce((select voucherbal_amount FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " where voucherbal_voucher_id = "
						+ voucher_id
						+ ""
						+ " and voucherbal_trans_id = vou.voucher_id),0) receiptamount,"
						+ " COALESCE((SELECT if(voucherbal_voucher_id>0,1,0)"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id
						+ " AND voucherbal_trans_id = vou.voucher_id),0) AS checked";
			}
			StrSql += " FROM  " + compdb(comp_id) + "axela_acc_voucher vou "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = vou.voucher_vouchertype_id"
					+ " WHERE 1=1 "
					// + " AND branch_company_id = " + comp_id
					+ " AND vouchertype_id IN (6,21)"
					+ " AND vou.voucher_active = 1"
					+ " AND voucher_customer_id = " + vouchertrans_customer_id
					+ " GROUP BY vou.voucher_id";
			if (include_zero_bal.equals("0") && voucher_id.equals("0")) {
				StrSql += " HAVING balance > 0";
			}
			StrSql += " ORDER BY vou.voucher_date";

			// SOP("from Ledger_Check StrSql===pending invoice===" + StrSql);

			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-hide=\"phone\">#</th>\n");
				Str.append("<th data-toggle=\"true\"></th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th data-hide=\"phone\">Date</th>\n");
				Str.append("<th data-hide=\"phone\">Due Days</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Ref. No.</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Balance Amount</th>\n");

				if (!vouchertype_id.equals("9")) {
					Str.append("<th data-hide=\"phone, tablet\">Adjustment Amount</th>\n");
				} else {
					Str.append("<th data-hide=\"phone, tablet\">Receipt Amount</th>\n");
				}
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					invcount++;
					String checked = "0";
					Str.append("<tr align=center id=\"row_" + count + "\">");
					Str.append("<td>");
					Str.append(invcount);
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<input type=\"hidden\" id=\"txt_voucherid_"
							+ count
							+ "\" name=\"txt_voucherid_" + count
							+ "\" value=" + crs.getString("voucher_id") + ">");
					if (!voucher_id.equals("0")) {
						Str.append("<input type=\"checkbox\" title='ckeckbox' name='chk_"
								+ count
								+ "' id='chk_"
								+ count
								+ "' "
								+ PopulateCheck(crs.getString("checked")));
					} else {
						Str.append("<input type=\"checkbox\" title='ckeckbox' name='chk_"
								+ count
								+ "' id='chk_"
								+ count
								+ "' "
								+ PopulateCheck("0"));
					}
					if (!vouchertype_id.equals("0")) {
						Str.append("onclick=\"CalAmountBal(" + count + ");\"");
					} else {
						Str.append("onclick=\"CalcBalanceCheck(" + count + ");\"");
					}
					Str.append("/>");
					Str.append("</td>\n");
					Str.append("<td align='center' >").append(
							"<a href='../accounting/voucher-list.jsp?vouchertype_id="
									+ crs.getString("vouchertype_id")
									+ "&voucher_id="
									+ crs.getString("voucher_id") + "'>");
					Str.append(crs.getString("voucher_id")).append("</a></td>");

					Str.append("<td align='center' >")
							.append(strToShortDate(crs.getString("voucher_date")))
							.append("</td>");
					// due date
					Str.append("<td align='center' >")
							.append(crs.getString("duedays"))
							.append("</td>");

					Str.append("<td align='center' >")
							.append(crs.getString("voucher_no"))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("voucher_ref_no"))
							.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(crs.getString("voucher_amount"));
					Str.append("</td>\n");
					Str.append("<td align=right >");
					// + crs.getString("vouchertype_id") + "---- ");
					if (!voucher_id.equals("0")) {
						if (crs.getString("vouchertype_id").equals("21")) {
							if (vouchertype_id.equals("11") || vouchertype_id.equals("15")) {
								Str.append(df.format(crs.getDouble("balance") + crs.getDouble("receiptamount")));
							}
							else {
								Str.append(df.format(crs.getDouble("balance") - crs.getDouble("receiptamount")));
							}
						} else if (crs.getString("vouchertype_id").equals("6")) {
							if (vouchertype_id.equals("11") || vouchertype_id.equals("15")) {
								Str.append(df.format(crs.getDouble("balance") - crs.getDouble("receiptamount")));
							} else {
								Str.append(df.format(crs.getDouble("balance") + crs.getDouble("receiptamount")));
							}
						}
					} else {
						Str.append(df.format(crs.getDouble("balance")));
					}
					// vouchertype_id set to hidden field
					Str.append("<input type=\"hidden\" id=\"txt_vouchertypeid_"
							+ count + "\" name=\"txt_vouchertypeid_" + count
							+ "\" value=" + crs.getString("vouchertype_id") + " >");
					// balamount set to hidden field
					Str.append("<input type=\"hidden\" id=\"txt_balamount_"
							+ count + "\" name=\"txt_balamount_" + count
							+ "\" value=");
					if (!voucher_id.equals("0")) {
						if (crs.getString("vouchertype_id").equals("21")) {
							if (vouchertype_id.equals("11") || vouchertype_id.equals("15")) {
								Str.append(df.format(crs.getDouble("balance") + crs.getDouble("receiptamount")));
							}
							else {
								Str.append(df.format(crs.getDouble("balance") - crs.getDouble("receiptamount")));
							}
						} else if (crs.getString("vouchertype_id").equals("6")) {
							if (vouchertype_id.equals("11") || vouchertype_id.equals("15")) {
								Str.append(df.format(crs.getDouble("balance") - crs.getDouble("receiptamount")));
							} else {
								Str.append(df.format(crs.getDouble("balance") + crs.getDouble("receiptamount")));
							}
						}
					} else {
						Str.append(df.format(crs.getDouble("balance")));
					}
					Str.append(">");
					// voucherno set to hidden field
					Str.append("<input type=\"hidden\" id=\"txt_voucherno_"
							+ count + "\" name=\"txt_voucherno_" + count
							+ "\" value=" + crs.getString("voucher_no") + ">");
					Str.append("</td>\n");

					Str.append("<td align=right><font color='red'><b><span id=\"sp_rebaldispaly_" + count
							+ "\"></span></b></font>");

					if (!CNumeric(voucher_id).equals("0")
							&& crs.getDouble("receiptamount") != 0) {
						Str.append("<input style=\"display: inline; width: unset;\" name='txt_amount_" + count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);CalAmountBal2(" + count + ");\" value='"
								+ crs.getString("receiptamount")
								+ "' type='text' class='form-control adjamount' size=10 maxlength=10 style='display: inline;width: unset' />");

					} else {
						Str.append("<input style=\"display: inline; width: unset;\" name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);CalAmountBal2(" + count + ");\" value='"
								+ df.format(crs.getDouble("balance")) // voucherbal_amount
								+ "' type='text' class='form-control adjamount' size=10 maxlength=10 style='display: inline;width: unset' />");

					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
					count++;
				}
				totalcount = count;
				Str.append("<input type=\"hidden\" id=\"txt_count\" name=\"txt_count\" value="
						+ count + ">");

				Str.append("</tbody></table></div>");
				Str.append("<br><br><br><br><center><font id='font' color='red'></font></center>");
				crs.close();
			} else {
				Str.append("<br><br><br><br><center><font id='font' color='red'>No records found!</font></center>");
				Str.append("<input type=\"hidden\" id=\"txt_count\" name=\"txt_count\" value="
						+ count + ">");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateCDNotePending(HttpServletRequest request,
			String voucher_no, String voucher_id,
			String vouchertype_id) {
		String voucherbal_trans_id = "";
		String voucherbal_trans_idarr[] = null;
		voucher_id = CNumeric(voucher_id);
		comp_id = CNumeric(GetSession("comp_id", request));
		try {
			StringBuilder Str = new StringBuilder();
			CachedRowSet crs;
			// SOP("voucherbal_trans_id==="+voucherbal_trans_id);
			int count = 0, invcount = 0, totalcount = 0;
			double balamt = 0, balamount = 0.00;
			String voucherbal_amount = "0";

			StrSql = "SELECT voucher_id, voucher_date,"
					+ " CONCAT(vouchertype_prefix,voucher_no,vouchertype_suffix) AS voucher_no,"
					+ " voucher_ref_no,"
					+ " vouchertype_id,"
					+ " (DATEDIFF(SYSDATE(),vou.voucher_date)-1) AS duedays,"
					+ " @vocheramount:=voucher_amount AS voucher_amount,"
					+ " @receipt:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher rcpt"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = rcpt.voucher_id AND rcpt.voucher_vouchertype_id = 105"
					+ " WHERE 1=1 "
					// + " AND rcpt.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS receipt,"
					+ " @payment:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher pymt"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = pymt.voucher_id AND pymt.voucher_vouchertype_id = 109"
					+ " WHERE 1=1"
					// + " AND pymt.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS payment,"
					+ " @creditnote:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher cdn"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = cdn.voucher_id AND cdn.voucher_vouchertype_id = 106"
					+ " WHERE 1=1"
					// + " AND cdn.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS creditnote,"
					+ " @debitnote:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher dbn"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = dbn.voucher_id AND dbn.voucher_vouchertype_id = 107"
					+ " WHERE 1=1"
					// + " AND dbn.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS debitnote,"
					+ " @journal:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher jr"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = jr.voucher_id AND jr.voucher_vouchertype_id = 111"
					+ " WHERE 1=1"
					// + " AND jr.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS journal,"
					+ " @salesreturn:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher sr"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = sr.voucher_id AND sr.voucher_vouchertype_id = 116"
					+ " WHERE 1=1"
					// + " AND sr.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS salesreturn,"
					+ " @purchasereturn:=(SELECT COALESCE(SUM(voucherbal_amount),0.00)"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher pr"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = pr.voucher_id AND pr.voucher_vouchertype_id = 117"
					+ " WHERE 1=1"
					// + " AND pr.voucher_branch_id = branch_company_id"
					+ " AND voucherbal_trans_id = vou.voucher_id) AS purchasereturn,"
					+ " if(vou.voucher_vouchertype_id = 102,"
					+ " COALESCE((@vocheramount-@receipt+@payment+@debitnote-@creditnote-@salesreturn),0.00)"
					+ " ,COALESCE((@voucheramount+@receipt-@payment+@debitnote-@creditnote-@purchasereturn),0.00)) AS balance";
			if (!voucher_id.equals("0")) {
				StrSql += " , coalesce((select voucherbal_amount FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " where voucherbal_voucher_id = "
						+ voucher_id
						+ ""
						+ " and voucherbal_trans_id = vou.voucher_id),0) receiptamount,"
						+ " COALESCE((SELECT if(voucherbal_voucher_id>0,1,0)"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id
						+ " AND voucherbal_trans_id = vou.voucher_id),0) AS checked";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_acc_voucher vou"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = vou.voucher_vouchertype_id"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_branch ON branch_id = vou.voucher_branch_id";

			StrSql += " WHERE 1=1";
			// + " AND branch_company_id = " + comp_id;
			StrSql += " AND vouchertype_id IN (102,115)";
			StrSql += " AND vou.voucher_active = 1";
			if (!voucher_no.equals("0")) {
				StrSql += " AND (CONCAT(vouchertype_prefix,vou.voucher_no,vouchertype_suffix) = '" + voucher_no + "'"
						+ " OR vou.voucher_ref_no = '" + voucher_no + "')";
			}
			StrSql += " GROUP BY vou.voucher_id";
			StrSql += " ORDER BY voucher_date";
			// SOP("StrSql --cdnote---" + StrSqlBreaker(StrSql));
			crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			if (crs.isBeforeFirst()) {
				Str.append("<html>\n"
						+ "<body><table width=80% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th></th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Balance Amount</th>\n");
				if (vouchertype_id.equals("106")) {
					Str.append("<th>Credit Amount</th>\n");
				} else {
					Str.append("<th>Debit Amount</th>\n");
				}
				Str.append("</tr>\n");
				while (crs.next()) {
					invcount++;
					String checked = "0";
					Str.append("<tr align=center>");
					Str.append("<td>");
					Str.append(invcount);
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<input type=\"hidden\" id=\"txt_voucherid_"
							+ count + "\" name=\"txt_voucherid_" + count
							+ "\" value=" + crs.getString("voucher_id") + ">");
					if (!voucher_id.equals("0")) {
						Str.append("<input type=\"checkbox\" title='ckeckbox' name='chk_"
								+ count
								+ "' id='chk_"
								+ count
								+ "' "
								+ PopulateCheck(crs.getString("checked")));
					} else {
						Str.append("<input type=\"checkbox\" title='ckeckbox' name='chk_"
								+ count
								+ "' id='chk_"
								+ count
								+ "' "
								+ PopulateCheck("0"));
					}
					Str.append("</td>\n");
					Str.append("<td align='center' >").append(
							"<a href='../accounting/voucher-list.jsp?vouchertype_id="
									+ crs.getString("vouchertype_id")
									+ "&voucher_id="
									+ crs.getString("voucher_id") + "'>");
					Str.append(crs.getString("voucher_id")).append("</a></td>");
					Str.append("<td align='center' >")
							.append(strToLongDate(crs.getString("voucher_date")))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("voucher_no"))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("voucher_ref_no"))
							.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(crs.getString("voucher_amount"));
					Str.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(df.format(crs.getDouble("balance")));
					Str.append("<input type=\"hidden\" id=\"txt_balamount_"
							+ count + "\" name=\"txt_balamount_" + count
							+ "\" value=" + balamount + ">");
					Str.append("<input type=\"hidden\" id=\"txt_voucherno_"
							+ count + "\" name=\"txt_voucherno_" + count
							+ "\" value=" + crs.getString("voucher_no") + ">");
					Str.append("</td>\n");
					Str.append("<td align=right>");
					if (!CNumeric(voucher_id).equals("0")

							&& crs.getDouble("receiptamount") != 0) {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ crs.getString("receiptamount")
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toFloat('txt_amount_"
								+ count + "','amt')\" />");
					} else {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ df.format(crs.getDouble("balance")) // voucherbal_amount
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toFloat('txt_amount_"
								+ count + "','amt')\" />");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
					count++;
				}
				totalcount = count;
				Str.append("<input type=\"hidden\" id=\"txt_count\" name=\"txt_count\" value="
						+ count + ">");
				Str.append("</table>\n</body>\n</html>");
				crs.close();
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
	public String PopulateCNoteInvoices(HttpServletRequest request,
			String vouchertrans_customer_id, String voucher_id,
			String vouchertype_id) {
		String voucherbal_trans_id = "";
		String voucherbal_trans_idarr[] = null;
		voucher_id = CNumeric(voucher_id);

		try {
			StringBuilder Str = new StringBuilder();
			CachedRowSet crs;
			if (!voucher_id.equals("0")) {
				StrSql = "SELECT COALESCE(voucherbal_trans_id,0)AS voucherbal_trans_id"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id;

				crs = processPrepQuery(StrSql, prepmap, 0);
				while (crs.next()) {
					voucherbal_trans_id += crs.getString("voucherbal_trans_id")
							+ ",";
				}
				crs.close();
				voucherbal_trans_id = voucherbal_trans_id.substring(0,
						voucherbal_trans_id.length() - 1);
				voucherbal_trans_idarr = voucherbal_trans_id.split(",");
			}

			int count = 0, totalcount = 0;
			double balamt = 0, balamount = 0.00;
			String voucherbal_amount = "0";

			StrSql = "SELECT voucher_id, voucher_date,"
					+ " vouchertype_prefix, vouchertype_suffix, voucher_no,"
					+ " voucher_ref_no, voucher_amount, vouchertype_id,"
					+ " COALESCE(voucherbal_trans_id,0) AS  voucherbal_trans_id,"
					+ " COALESCE(voucherbal_amount,0) AS voucherbal_amount,"
					+ " (voucher_amount - coalesce(sum(voucherbal_amount),0)) as balamount,"
					+ " coalesce(sum(voucherbal_amount),0) as diffamount";
			if (!voucher_id.equals("0")) {
				StrSql += " , coalesce((select voucherbal_amount FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " where voucherbal_voucher_id = "
						+ voucher_id
						+ ""
						+ " and voucherbal_trans_id = voucher_id),0) receiptamount";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_bal on voucherbal_trans_id = voucher_id";

			StrSql += " WHERE 1=1 ";
			if (vouchertype_id.equals("106")) {
				StrSql += " AND vouchertype_voucherclass_id = 102";
			}
			if (vouchertype_id.equals("107")) {
				StrSql += " AND vouchertype_voucherclass_id = 115";
			}
			StrSql += " AND voucher_active = 1";
			StrSql += " AND vouchertrans_customer_id = ?"
					+ " GROUP BY voucher_id";
			if (voucher_id.equals("0")) {
				StrSql += " HAVING balamount > 0";
			}
			StrSql += " ORDER BY voucher_date";
			// SOP("vouchertrans_customer_id====" + vouchertrans_customer_id);
			// SOP("StrSql==PopulateCNoteInvoices===" + StrSqlBreaker(StrSql));

			prepmap.put(prepkey++, vouchertrans_customer_id);
			crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			if (crs.isBeforeFirst()) {
				Str.append("<html>\n"
						+ "<body><table width=80% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
				Str.append("<tr align=center>\n");
				Str.append("<th></th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Balance Amount</th>\n");
				if (vouchertype_id.equals("106")) {
					Str.append("<th>Credit Amount</th>\n");
				} else {
					Str.append("<th>Debit Amount</th>\n");
				}
				Str.append("</tr>\n");
				while (crs.next()) {
					String checked = "0";
					if (request.getParameter("txt_voucherid_" + count + "") != null) {
						voucherbal_trans_id = CNumeric(PadQuotes(request
								.getParameter("txt_voucherid_" + count + "")));
					} else if (!voucher_id.equals("")) {
						voucherbal_trans_id = crs
								.getString("voucherbal_trans_id");
					}

					if (request.getParameter("txt_amount_" + count + "") != null) {
						voucherbal_amount = PadQuotes(request
								.getParameter("txt_amount_" + count + ""));
						checked = CheckBoxValue(PadQuotes(request
								.getParameter("chk_" + count + "")));
					} else if (!voucher_id.equals("0")) {
						voucherbal_amount = crs.getString("voucherbal_amount");
						// if (!voucherbal_amount.equals("0.00")) {
						for (String str : voucherbal_trans_idarr) {
							if (str.equals(voucherbal_trans_id)) {
								checked = "1";
							}
						}
						// }
					}
					// SOP("receipt_id===" + receipt_id);
					if (CNumeric(voucher_id).equals("0")) {
						balamount = crs.getDouble("balamount");
					} else {
						balamount = crs.getDouble("balamount")
								+ crs.getDouble("receiptamount");
					}
					// SOP("balamount==="+balamount);
					Str.append("<tr align=center>");
					Str.append("<td>");
					Str.append("<input type=\"hidden\" id=\"txt_voucherid_"
							+ count + "\" name=\"txt_voucherid_" + count
							+ "\" value=" + crs.getString("voucher_id") + ">");
					Str.append("<input type=\"checkbox\" name='chk_" + count
							+ "' id='chk_" + count + "' "
							+ PopulateCheck(checked) + "/>");
					Str.append("</td>\n");
					Str.append("<td align='center' >").append(
							"<a href='../accounting/voucher-list.jsp?vouchertype_id="
									+ crs.getString("vouchertype_id")
									+ "&voucher_id="
									+ crs.getString("voucher_id") + "'>");
					Str.append(crs.getString("voucher_id")).append("</a></td>");
					Str.append("<td align='center' >")
							.append(strToLongDate(crs.getString("voucher_date")))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("vouchertype_prefix")
									+ crs.getString("voucher_no")
									+ crs.getString("vouchertype_suffix"))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("voucher_ref_no"))
							.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(crs.getString("voucher_amount"));
					Str.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(df.format(balamount));
					Str.append("<input type=\"hidden\" id=\"txt_balamount_"
							+ count + "\" name=\"txt_balamount_" + count
							+ "\" value=" + balamount + ">");
					Str.append("<input type=\"hidden\" id=\"txt_voucherno_"
							+ count + "\" name=\"txt_voucherno_" + count
							+ "\" value=" + crs.getString("voucher_no") + ">");
					Str.append("</td>\n");
					Str.append("<td align=right>");
					if (!CNumeric(voucher_id).equals("0")
							&& crs.getDouble("receiptamount") != 0) {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ crs.getString("receiptamount")
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toFloat('txt_amount_"
								+ count + "','amt')\" />");
					} else if (Double.parseDouble(voucherbal_amount) == 0
							&& crs.getDouble("balamount") != 0) {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ balamount // voucherbal_amount
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toFloat('txt_amount_"
								+ count + "','amt')\" />");
					} else {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ balamount // /voucherbal_amount
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toInteger('txt_amount_"
								+ count + "','amt')\" />");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
					count++;
				}
				totalcount = count;
				Str.append("<input type=\"hidden\" id=\"txt_count\" name=\"txt_count\" value="
						+ count + ">");
				Str.append("</table>\n</body>\n</html>");
				crs.close();
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateOutstandingPayments(HttpServletRequest request,
			String vouchertrans_customer_id, String payment_id) {
		Receipt_Update rcpt = new Receipt_Update();
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs;
		int count = 0, totalcount = 0;
		double balamt = 0;
		String voucherbal_trans_id = "0", voucherbal_amount = "0";

		StrSql = "SELECT voucher_id, voucher_date,"
				+ " vouchertype_prefix, vouchertype_suffix, voucher_no,"
				+ " voucher_ref_no, voucher_amount, vouchertype_id,"
				+ " COALESCE(voucherbal_trans_id,0) voucherbal_trans_id, COALESCE(voucherbal_amount,0) voucherbal_amount,"
				+ " (voucher_amount - coalesce(sum(voucherbal_amount),0)) as balamount,"
				+ " coalesce(sum(voucherbal_amount),0) as diffamount";
		if (!CNumeric(payment_id).equals("0")) {
			StrSql += " , coalesce((select voucherbal_amount FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
					+ " where voucherbal_voucher_id = "
					+ payment_id
					+ " and voucherbal_trans_id = voucher_id),0) paymentamount";
		}
		StrSql += " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_bal on voucherbal_trans_id = voucher_id";
		if (!CNumeric(payment_id).equals("0")) {
			StrSql += " AND voucherbal_voucher_id = " + CNumeric(payment_id)
					+ "";
		}
		StrSql += " WHERE vouchertype_voucherclass_id = 108 AND voucher_active = 1"
				+ " AND vouchertrans_customer_id = ?"
				+ " GROUP BY voucher_id"
				+ " HAVING balamount > 0" + " ORDER BY voucher_date";
		// SOP("StrSql = " + StrSqlBreaker(StrSql));
		try {
			prepmap.put(prepkey++, vouchertrans_customer_id);
			crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			if (crs.isBeforeFirst()) {
				Str.append("<html>\n"
						+ "<body><table width=80% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
				Str.append("<tr align=center>\n");
				Str.append("<th></th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Balance Amount</th>\n");
				Str.append("<th>Adjustment Amount</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					String checked = "0";
					if (request.getParameter("txt_voucherid_" + count + "") != null) {
						voucherbal_trans_id = CNumeric(PadQuotes(request
								.getParameter("txt_voucherid_" + count + "")));
					} else if (!payment_id.equals("")) {
						voucherbal_trans_id = crs
								.getString("voucherbal_trans_id");
					}
					if (request.getParameter("txt_amount_" + count + "") != null) {
						voucherbal_amount = PadQuotes(request
								.getParameter("txt_amount_" + count + ""));
						checked = CheckBoxValue(PadQuotes(request
								.getParameter("chk_" + count + "")));
					} else if (!CNumeric(payment_id).equals("0")) {
						voucherbal_amount = crs.getString("voucherbal_amount");
						if (!voucherbal_amount.equals("0.00")) {
							checked = "1";
						}
					}
					Str.append("<tr align=center>");
					Str.append("<td>");
					Str.append("<input type=\"hidden\" id=\"txt_voucherid_"
							+ count + "\" name=\"txt_voucherid_" + count
							+ "\" value=" + crs.getString("voucher_id") + ">");
					Str.append("<input type=\"checkbox\" name='chk_" + count
							+ "' id='chk_" + count + "' "
							+ PopulateCheck(checked) + "/>");
					Str.append("</td>\n");
					Str.append("<td align='center' >").append(
							"<a href='../accounting/voucher-list.jsp?vouchertype_id="
									+ crs.getString("vouchertype_id")
									+ "&voucher_id="
									+ crs.getString("voucher_id") + "'>");
					Str.append(crs.getString("voucher_id")).append("</a></td>");
					Str.append("<td align='center' >")
							.append(strToLongDate(crs.getString("voucher_date")))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("vouchertype_prefix")
									+ crs.getString("voucher_no")
									+ crs.getString("vouchertype_suffix"))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("voucher_ref_no"))
							.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(crs.getString("voucher_amount"));
					Str.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(crs.getString("balamount"));
					Str.append("<input type=\"hidden\" id=\"txt_balamount_"
							+ count + "\" name=\"txt_balamount_" + count
							+ "\" value=" + crs.getString("balamount") + ">");
					Str.append("<input type=\"hidden\" id=\"txt_voucherno_"
							+ count + "\" name=\"txt_voucherno_" + count
							+ "\" value=" + crs.getString("voucher_no") + ">");
					Str.append("</td>\n");
					Str.append("<td align=right >");
					if (!CNumeric(payment_id).equals("0")
							&& crs.getDouble("paymentamount") != 0) {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ crs.getString("paymentamount")
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toInteger('txt_amount_"
								+ count + "','amt')\" />");
					} else if (Double.parseDouble(voucherbal_amount) == 0
							&& crs.getDouble("balamount") != 0) {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ crs.getString("balamount")
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toInteger('txt_amount_"
								+ count + "','amt')\" />");
					} else {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);\" value='"
								+ voucherbal_amount
								+ "' type='text' class=textbox size=10 maxlength=10  onKeyUp=\"toInteger('txt_amount_"
								+ count + "','amt')\" />");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
					count++;
				}
				totalcount = count;
				Str.append("<input type=\"hidden\" id=\"txt_count\" name=\"txt_count\" value="
						+ count + ">");
				Str.append("</table>\n</body>\n</html>");
				crs.close();
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String GetReconcileData() {
		StringBuilder Str = new StringBuilder();
		if (!vouchertrans_id.equals("0")
				&& (reconcile.equals("yes") || reconcile.equals("no"))) {
			StrSql = "UPDATE  " + compdb(comp_id) + "axela_acc_voucher_trans";
			if (reconcile.equals("yes")) {
				StrSql += " set vouchertrans_reconciliation = 1,"
						+ " vouchertrans_reconciliation_date = "
						+ ToShortDate(kknow()) + ","
						+ " vouchertrans_reconciliation_emp_id = " + emp_id
						+ "";
			} else if (reconcile.equals("no")) {
				StrSql += " set vouchertrans_reconciliation = 0,"
						+ " vouchertrans_reconciliation_date = '',"
						+ " vouchertrans_reconciliation_emp_id = 0";
			}
			StrSql += " where vouchertrans_id = " + vouchertrans_id;
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		}
		if (!customer_id.equals("0") && isValidDateFormatShort(fromdate)
				&& isValidDateFormatShort(todate)) {
			StrSql = "select"
					+ " (customer_open_bal +  (Select COALESCE((sum(if(vouchertrans_dc=1,vouchertrans_amount,0))-sum(if(vouchertrans_dc=0,vouchertrans_amount,0))),0)"
					+ " from " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans on vouchertrans_voucher_id = voucher_id"
					+ " where voucher_active = 1 and vouchertrans_customer_id = customer_id and substr(voucher_date ,1,8) < substr("
					+ ConvertShortDateToStr(fromdate)
					+ ",1,8) )) as openingbal,"
					+ " (customer_open_bal +  (Select COALESCE((sum(if(vouchertrans_dc=1,vouchertrans_amount,0))-sum(if(vouchertrans_dc=0,vouchertrans_amount,0))),0)"
					+ " from " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans on vouchertrans_voucher_id = voucher_id"
					+ " where voucher_active = 1 and vouchertrans_customer_id = customer_id and substr(voucher_date ,1,8) <= substr("
					+ ConvertShortDateToStr(todate)
					+ ",1,8))) as closingbal,"
					+ " @total:=(Select COALESCE(sum(vouchertrans_amount),0)"
					+ " from " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans on vouchertrans_voucher_id = voucher_id"
					+ " where voucher_active = 1 and vouchertrans_customer_id = customer_id and substr(voucher_date ,1,8) >= substr("
					+ ConvertShortDateToStr(fromdate)
					+ ",1,8)"
					+ " and substr(voucher_date ,1,8) <= substr("
					+ ConvertShortDateToStr(todate)
					+ ",1,8) )  as total,"
					+ " @reconcileamt:=(Select COALESCE(sum(vouchertrans_amount),0)"
					+ " from " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans on vouchertrans_voucher_id = voucher_id"
					+ " where voucher_active = 1 and vouchertrans_customer_id = customer_id"
					+ " and substr(voucher_date ,1,8) >= substr("
					+ ConvertShortDateToStr(fromdate)
					+ ",1,8)"
					+ " and substr(voucher_date ,1,8) <= substr("
					+ ConvertShortDateToStr(todate)
					+ ",1,8)"
					+ " and vouchertrans_reconciliation=1 )  as reconcileamt,"
					+ " (@total - @reconcileamt) as notreconcileamt"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " WHERE 1 = 1"
					+ " AND customer_id = " + customer_id
					+ " GROUP BY customer_id";

			// SOP("StrSql=getreconciledata==" + StrSqlBreaker(StrSql));

			try {
				CachedRowSet crs = processQuery(StrSql, 0);
				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("\n <table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">Opening Balance</th>\n");
				Str.append("<th>Closing Balance</th>\n");
				Str.append("<th>Total</th>\n");
				Str.append("<th data-hide=\"phone\">Reconcil Amount</th>\n");
				Str.append("<th data-hide=\"phone\">Difference</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					Double diff_number = Double.parseDouble(crs.getString("notreconcileamt"));
					// df.setMaximumFractionDigits(2);
					String diff_amount = df.format(diff_number);
					Str.append("<tr>\n");
					Str.append("<td valign=top align=right>")
							.append("<div id='opening_bal'>"
									+ crs.getString("openingbal") + "</div>")
							.append("</td>\n");
					Str.append("<td valign=top align=right >")
							.append("<div id='closing_bal' align='right'>"
									+ crs.getString("closingbal") + "</div>")
							.append("</td>\n");
					Str.append("<td valign=top align=right >")
							.append("<div id='total_amount'>"
									+ crs.getString("total") + "</div>")
							.append("</td>\n");
					Str.append("<td valign=top align=right >")
							.append("<div id='reconcil_amount'>"
									+ crs.getString("reconcileamt") + "</div>")
							.append("</td>\n");
					Str.append("<td valign=top align=right >")
							.append("<div id='diff_amount'>"
									+ diff_amount
									+ "</div>").append("</td>\n");
				}
				crs.close();
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	// ////reconcilation
	public String ReconcileListData(HttpServletRequest request) {
		int PageListSize = 10;
		int StartRec = 0;
		int EndRec = 0;
		int TotalRecords = 0;
		String CountSql, SqlJoin = "";
		String PageURL = "";
		StringBuilder Str = new StringBuilder();
		String StrSearch = "";
		int iterate = 0;

		if (customer_id.equals("0")) {
			Str.append("<br><br><br><b><center><font color=red>Select Ledger!</font></center></b>");
		}
		if (fromdate.equals("")) {
			Str.append("<br><b><font color=red>Enter From Date!</font></b>");
		}
		if (todate.equals("")) {
			Str.append("<br><b><font color=red>Enter To Date!</font></b>");
		}
		if (Long.parseLong(ConvertShortDateToStr(fromdate)) > Long
				.parseLong(ConvertShortDateToStr(todate))) {
			Str.append("<br><b><font color=red>From Date can't be greater than To Date!</font></b>");
		}
		if (Str.toString().equals("")) {
			if ((CNumeric(PageCurrents).equals("0"))) {
				PageCurrents = "1";
			}
			PageCurrent = Integer.parseInt(PageCurrents);
			// to know no of records depending on search
			StrSql = "select coalesce(voucher_id, 0) as voucher_id, CONCAT(vouchertype_prefix, voucher_no, vouchertype_suffix) AS voucher_no, voucher_narration, voucher_date,"
					+ " vouchertrans_id, vouchertrans_amount, vouchertrans_reconciliation, vouchertrans_dc,"
					+ " vouchertype_name";
			CountSql = "SELECT Count(distinct vouchertrans_id) ";
			SqlJoin = " FROM " + compdb(comp_id) + "axela_acc_voucher "
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_voucher_type on vouchertype_id = voucher_vouchertype_id"
					+ " WHERE 1 = 1" + " AND vouchertrans_customer_id = "
					+ customer_id + ""
					+ " AND SUBSTR(voucher_date ,1,8) >= SUBSTR('"
					+ ConvertShortDateToStr(fromdate) + "',1,8)"
					+ " AND SUBSTR(voucher_date ,1,8) <= SUBSTR('"
					+ ConvertShortDateToStr(todate) + "',1,8)"
					+ " AND voucher_active = 1";
			StrSql = StrSql + SqlJoin;
			CountSql = CountSql + SqlJoin;
			TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
			if (TotalRecords != 0) {
				StartRec = ((PageCurrent - 1) * recperpage) + 1;
				EndRec = ((PageCurrent - 1) * recperpage) + recperpage;
				if (EndRec > TotalRecords) {
					EndRec = TotalRecords;
				}

				RecCountDisplay = "Displaying " + (StartRec) + " - " + (EndRec)
						+ " of " + TotalRecords + " Record(s)";
				if (QueryString.contains("PageCurrent") == true) {
					QueryString = QueryString.replaceAll("&PageCurrent="
							+ PageCurrent + "", "");
				}
				PageURL = "javascript:ReconcileList";
				PageCount = (TotalRecords / recperpage);
				if ((TotalRecords % recperpage) > 0) {
					PageCount = PageCount + 1;
				}
				// display on jsp page
				// PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount,
				// PageListSize);
				PageNaviStr = PageNavi(PageURL, PageCurrent, PageCount, PageListSize);
				StrSql += " GROUP BY vouchertrans_id"
						+ " order by vouchertrans_id desc";
				StrSql = StrSql + " limit " + (StartRec - 1) + ", "
						+ recperpage + "";

				Str.append("<b><center>" + RecCountDisplay);
				Str.append("</br>");
				Str.append(PageNaviStr + "</b></center>");
				try {

					// SOP("StrSql=======" + StrSql);

					CachedRowSet crs = processQuery(StrSql, 0);
					int count = StartRec - 1;
					Str.append("<div class=\"table-responsive table-bordered\">\n");
					Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
					Str.append("<thead><tr>\n");
					Str.append("<th data-hide=\"phone\">#</th>\n");
					Str.append("<th data-toggle=\"true\">Voucher ID</th>\n");
					Str.append("<th>Voucher</th>\n");
					Str.append("<th data-hide=\"phone\">Voucher No.</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Narration</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Debit</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Credit</th>\n");
					Str.append("<th data-hide=\"phone, tablet\">Yes/No</th>\n");
					Str.append("</tr>\n");
					Str.append("</thead>\n");
					Str.append("<tbody>\n");
					while (crs.next()) {
						count = count + 1;
						Str.append("<tr>\n");
						Str.append("<td valign=top align=center >")
								.append(count).append("</td>\n");
						Str.append("<td valign=top align=center >")
								.append(crs.getString("voucher_id"))
								.append("</td>\n");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("vouchertype_name"))
								.append("</td>\n");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("voucher_no"))
								.append("</td>\n");
						Str.append("<td valign=top align=left >")
								.append(crs.getString("voucher_narration"))
								.append("</td>\n");
						Str.append("<td valign=top align=center >")
								.append(strToShortDate(crs
										.getString("voucher_date")))
								.append("</td>\n");
						Str.append("<td valign=top align=right >&nbsp;");
						if (crs.getString("vouchertrans_dc").equals("1")) {
							Str.append(crs.getString("vouchertrans_amount"));
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=right >&nbsp;");
						if (crs.getString("vouchertrans_dc").equals("0")) {
							Str.append(crs.getString("vouchertrans_amount"));
						}
						Str.append("</td>\n");
						Str.append("<td valign=top align=center >")
								.append("<input type='checkbox' id='check"
										+ crs.getString("vouchertrans_id")
										+ "'"
										+ " onchange=\"ReconcileData("
										+ crs.getString("vouchertrans_id")
										+ ");\" "
										+ PopulateCheck(crs
												.getString("vouchertrans_reconciliation"))
										+ "/>");
						Str.append("</td>\n");
						Str.append("</tr>\n");
						Str.append("</tbody>\n");

						iterate++;
					}
					Str.append("</table>");
					Str.append("</div>");
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
					return "";
				}
			} else {
				Str.append("<br><br><b><center><font color=red>No Record(s) found!</font></center</b><br><br>");
			}
		}
		return Str.toString();
	}

	public String PopulateTax(String comp_id, String price_tax_id) {
		try {
			StrSql = "SELECT CONCAT(customer_id) AS tax_id, customer_rate,"
					+ " CONCAT(customer_name) AS tax_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
					+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
					+ " WHERE 1 = 1"
					+ " AND customer_active = 1"
					+ " AND customer_tax = 1";
			if (!StrSqlSearch.equals("")) {
				StrSql += StrSqlSearch;
			}
			StrSql += " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql===PopulateTax1===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			Str.append("<select name=\"dr_voucher_tax1\" id=\"dr_voucher_tax1\" class=\"form-control\">");
			Str.append("<option value='0'> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tax_id") + "_" + crs.getString("customer_rate"));
				Str.append(StrSelectdrop(crs.getString("tax_id"), price_tax_id));
				Str.append(">").append(crs.getString("tax_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateTax2(String comp_id, String price_tax_id) {
		try {
			StrSql = "SELECT CONCAT(customer_id) AS tax_id, customer_rate,"
					+ " CONCAT(customer_name) AS tax_name"
					+ " FROM  " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_acc_tax_cat ON taxcat_id = customer_taxcat_id"
					+ " INNER JOIN axela_acc_tax_type ON taxtype_id = customer_taxtype_id"
					+ " WHERE 1 = 1"
					+ " AND customer_active = 1"
					+ " AND customer_tax = 1";
			if (!StrSqlSearch.equals("")) {
				StrSql += StrSqlSearch;
			}
			StrSql += " GROUP BY customer_id"
					+ " ORDER BY customer_name";
			// SOP("StrSql===PopulateTax2===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			StringBuilder Str = new StringBuilder();
			Str.append("<select name=\"dr_voucher_tax2\" id=\"dr_voucher_tax2\" class=\"form-control\">");
			Str.append("<option value='0'> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tax_id") + "_" + crs.getString("customer_rate"));
				Str.append(StrSelectdrop(crs.getString("tax_id"), price_tax_id));
				Str.append(">").append(crs.getString("tax_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateContact(String customer_id) {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		try {
			Str.append("<select name='dr_contact_id' class='form-control' id='dr_contact_id' >");
			Str.append("<option value = 0>Select</option>");
			StrSql = "SELECT contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname,' ', contact_lname) AS contact_name"
					+ " FROM " + compdb(comp_id) + "axela_customer"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = customer_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE customer_id = " + CNumeric(customer_id)
					+ " GROUP BY contact_id"
					+ " ORDER by contact_fname";
			// SOP("StrSql=contact==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				count++;
				if (count == 1) {
					Str.append("<option selected value=").append(crs.getString("contact_id"));
				} else {
					Str.append("<option value=").append(crs.getString("contact_id"));
				}
				Str.append(StrSelectdrop(crs.getString("contact_id"), contact_id)).append(">");
				Str.append(crs.getString("contact_name"));
				Str.append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			// SOP("Str.toString()=====" + Str.toString());
			return Str.toString();
		} catch (Exception ex) {
			SOPError(" Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
	public void AddMultipleLedger(String customer_id, String flag) {
		if (!customer_id.equals("0")) {
			// for debit multi enrty
			// SOP("session_id----" + session_id);
			StrSql = "SELECT cart_customer_id"
					+ " FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE cart_customer_id = " + customer_id
					+ " AND cart_session_id = " + session_id;
			if (ExecuteQuery(StrSql).equals("")) {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart ("
						+ " cart_vouchertype_id,"
						+ " cart_customer_id,"
						+ " cart_option_id,"
						+ " cart_amount,"
						+ " cart_emp_id,"
						+ " cart_session_id,"
						+ " cart_dc,"
						+ " cart_time)"
						+ " VALUES("
						+ " 18,"
						+ " " + customer_id + ","
						+ " 1,";
				if (flag.equals("0")) {
					StrSql += "" + cramount + ",";
				} else if (flag.equals("1")) {
					StrSql += "" + dramount + ",";
				}
				StrSql += "" + emp_id + ","
						+ " " + session_id + ","
						+ " " + flag + ","
						+ " '" + ToLongDate(kknow()) + "')";

				// SOP("StrSql==18=" + StrSql);
				updateQuery(StrSql);
			} else {
				new Journal_Update().msg = "Error" + "<br>Similar Customer found!";
			}
		}
	}

	public void updateMultipleLedger(String cart_id, String flag) {
		StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_cart"
				+ " SET cart_amount = ";
		if (flag.equals("0")) {
			StrSql += "" + cramount + ",";
		} else if (flag.equals("1")) {
			StrSql += "" + dramount + ",";
		}
		StrSql += " cart_dc = " + flag
				+ " WHERE cart_id = " + cart_id;
		updateQuery(StrSql);

	}

	public void AddMultipleParty(String customer_id,
			String partyamount) {
		StrSql = "SELECT cart_customer_id"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart"
				+ " WHERE cart_customer_id = " + customer_id
				+ " AND cart_session_id = " + session_id;
		if (ExecuteQuery(StrSql).equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_cart ("
					+ " cart_vouchertype_id,"
					+ " cart_customer_id,"
					+ " cart_option_id,"
					+ " cart_amount,"
					+ " cart_emp_id,"
					+ " cart_session_id,"
					+ " cart_time)"
					+ " VALUES("
					+ " 15,"
					+ " " + customer_id + ","
					+ " 1,"
					+ "" + partyamount + ","
					+ "" + emp_id + ","
					+ " " + session_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			// SOP("StrSql===" + StrSql);
			updateQuery(StrSql);
		} else {
			new Payment_Update().msg = "Error" + "<br>Similar Customer founr!";
		}
	}

	public void DeleteMultipleParty(String cart_id) {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart WHERE cart_id = " + cart_id
				+ " AND cart_vouchertype_id = 15"
				+ " AND cart_session_id = " + session_id;
		updateQuery(StrSql);
	}

	public String DisplayMultiLedger(String voucher_id, String comp_id, String addB, String updateB, HttpServletRequest request) {
		int count = 0, i = 0, index = 0;
		double total = 0.00;
		StringBuilder Str = new StringBuilder();
		double total_dr = 0.0;
		double total_cr = 0.0;
		try {

			Str.append("<table class=\"table table-hover table-bordered\" data-filter=\"#filter\">\n");
			Str.append("<thead>");
			Str.append("<tr>");
			Str.append("<th data-toggle=\"true\">#</th>");
			Str.append("<th>X</th>");
			Str.append("<th>Party</th>");
			Str.append("<th data-hide=\"phone\">Debit</th>");
			Str.append("<th data-hide=\"phone\">Credit</th>");
			Str.append("</tr>");
			Str.append("</thead>");
			Str.append("<tbody>");

			if (!voucher_id.equals("0") && (!addB.equals("yes") && !updateB.equals("yes"))) {
				StrSql = "SELECT"
						+ " vouchertrans_id, vouchertrans_customer_id,"
						+ " customer_name, vouchertrans_amount, vouchertrans_dc"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = vouchertrans_customer_id"
						+ " WHERE 1 = 1"
						+ " AND vouchertrans_voucher_id = " + voucher_id
						+ " AND vouchertrans_option_id = 1";

				// SOP("StrSql==display==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						count++;
						Str.append("<tr id='tr_" + count + "' class='ledger_tr'>");
						Str.append("<td id='td_" + count + "' align='center'><input type='text' id='count_" + count + "' value='" + count + "' hidden/>" + ++index + "</td>");
						Str.append("<td onclick='deleteRow(this);' align='center'>X</td>");
						Str.append("<td width='50%'><select onchange='addNewRecord();' class='form-control select2 ledger' id='dr_ledger_" + count + "' id='dr_ledger_" + count + "' >"
								+ PopulateLedgers("0", crs.getString("vouchertrans_customer_id"), comp_id) + "</select>");
						Str.append("<input type=hidden value=" + crs.getString("vouchertrans_customer_id") + " id=hid_ledger_" + count + " name=hid_ledger_" + count + " />");
						Str.append("</td>");
						if (crs.getString("vouchertrans_dc").equals("1")) {
							Str.append("<td align='right'>"
									+ " <input type='text' maxlength=\"10\""
									+ " id='dr_journal_" + count + "'"
									+ " name='dr_journal_" + count + "'"
									+ " value='" + df.format(crs.getDouble("vouchertrans_amount")) + "'"
									+ " onchange=\"toFloat('dr_journal_" + count + "');journalclear('dr_journal_" + count + "');journalupdate('dr_journal_" + count + "');\""
									+ " class='form-control text-right'>"
									+ " </td>");
							Str.append("<td align='right'>"
									+ " <input type='text' maxlength=\"10\""
									+ " id='cr_journal_" + count + "'"
									+ " name='cr_journal_" + count + "'"
									+ " value='0'"
									+ " onchange=\"toFloat('cr_journal_" + count + "');journalclear('cr_journal_" + count + "');journalupdate('cr_journal_" + count + "');\""
									+ " class='form-control text-right'>"
									+ " </td>");
						} else {
							Str.append("<td align='right'>"
									+ " <input type='text' maxlength=\"10\""
									+ " id='dr_journal_" + count + "'"
									+ " name='dr_journal_" + count + "'"
									+ " value='0'"
									+ " onchange=\"toFloat('dr_journal_" + count + "');journalclear('dr_journal_" + count + "');journalupdate('dr_journal_" + count + "');\""
									+ " class='form-control text-right'>"
									+ " </td>");
							Str.append("<td align='right'>"
									+ " <input type='text' maxlength=\"10\""
									+ " id='cr_journal_" + count + "'"
									+ " name='cr_journal_" + count + "'"
									+ " value='" + df.format(crs.getDouble("vouchertrans_amount")) + "'"
									+ " onchange=\"toFloat('cr_journal_" + count + "');journalclear('cr_journal_" + count + "');journalupdate('cr_journal_" + count + "');\""
									+ " class='form-control text-right'>"
									+ " </td>");
						}
						Str.append("</tr>");
					}
					crs.beforeFirst();

					while (crs.next()) {
						if (crs.getString("vouchertrans_dc").equals("0")) {
							total_cr += crs.getDouble("vouchertrans_amount");
						} else if (crs.getString("vouchertrans_dc").equals("1")) {
							total_dr += crs.getDouble("vouchertrans_amount");
						}
					}

				}
				crs.close();

				++count;
				Str.append("<tr id='tr_" + count + "' class='ledger_tr'>");
				Str.append("<td id='td_" + count + "' align='center'><input type='text' id='count_" + count + "' value='" + count + "' hidden/>" + ++index + "</td>");
				Str.append("<td onclick='deleteRow(this);'align='center'>X</td>");
				Str.append("<td width='50%'><select onchange='addNewRecord();' class='form-control select2 ledger' id='dr_ledger_" + count + "' id='dr_ledger_" + count + "' ></select>"
						+ "<input type=hidden value='0' id=hid_ledger_" + count + " name=hid_ledger_" + count + "> </td>");
				Str.append("<td><input type='text' class='form-control text-right' id='dr_journal_" + count + "' name='dr_journal_" + count + "' value='0' onchange=\"toFloat('dr_journal_" + count
						+ "');journalclear('dr_journal_" + count
						+ "');journalupdate('dr_journal_" + count + "');\"></td>");
				Str.append("<td><input type='text' class='form-control text-right' id='cr_journal_" + count + "' name='cr_journal_" + count + "' value='0' onchange=\"toFloat('cr_journal_" + count
						+ "');journalclear('cr_journal_" + count
						+ "');journalupdate('cr_journal_" + count + "');\"></td>");
				Str.append("</tr>");

			} else if (addB.equals("yes") || updateB.equals("yes")) {
				i = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_ledger_count"))));
				while (count <= i) {
					++count;

					if (!CNumeric(PadQuotes(request.getParameter("hid_ledger_" + count))).equals("0")) {
						Str.append("<tr id='tr_" + count + "' class='ledger_tr'>");
						Str.append("<td id='td_" + count + "' align='center'><input type='text' id='count_" + count + "' value='" + count + "' hidden/>" + ++index + "</td>");
						Str.append("<td onclick='deleteRow(this);' align='center'>X</td>");
						Str.append("<td width='50%'><select onchange='addNewRecord();' class='form-control select2 ledger' id='dr_ledger_" + count + "' id='dr_ledger_" + count + "' >"
								+ PopulateLedgers("0", CNumeric(PadQuotes(request.getParameter("hid_ledger_" + count))), comp_id) + "</select>");
						Str.append("<input type=hidden value=" + CNumeric(PadQuotes(request.getParameter("hid_ledger_" + count))) + " id=hid_ledger_" + count + " name=hid_ledger_" + count + " />");
						Str.append("</td>");

						Str.append("<td align='right'>"
								+ " <input type='text' maxlength=\"10\""
								+ " id='dr_journal_" + count + "'"
								+ " name='dr_journal_" + count + "'"
								+ " value='" + CNumeric(PadQuotes(request.getParameter("dr_journal_" + count))) + "'"
								+ " onchange=\"toFloat('dr_journal_" + count + "');journalclear('dr_journal_" + count + "');journalupdate('dr_journal_" + count + "');\""
								+ " class='form-control text-right'>"
								+ " </td>");
						Str.append("<td align='right'>"
								+ " <input type='text' maxlength=\"10\""
								+ " id='cr_journal_" + count + "'"
								+ " name='cr_journal_" + count + "'"
								+ " value='" + CNumeric(PadQuotes(request.getParameter("cr_journal_" + count))) + "'"
								+ " onchange=\"toFloat('cr_journal_" + count + "');journalclear('cr_journal_" + count + "');journalupdate('cr_journal_" + count + "');\""
								+ " class='form-control text-right'>"
								+ " </td>");

						Str.append("</tr>");

						total_dr += Double.parseDouble(CNumeric(PadQuotes(request.getParameter("dr_journal_" + count))));
						total_cr += Double.parseDouble(CNumeric(PadQuotes(request.getParameter("cr_journal_" + count))));

					}
				}
				++count;
				Str.append("<tr id='tr_" + count + "' class='ledger_tr'>");
				Str.append("<td id='td_" + count + "' align='center'><input type='text' id='count_" + count + "' value='" + count + "' hidden/>" + ++index + "</td>");
				Str.append("<td onclick='deleteRow(this);' align='center'>X</td>");
				Str.append("<td width='50%'><select onchange='addNewRecord();' class='form-control select2 ledger' id='dr_ledger_" + count + "' id='dr_ledger_" + count + "' ></select>"
						+ "<input type=hidden value='0' id=hid_ledger_" + count + " name=hid_ledger_" + count + "> </td>");
				Str.append("<td><input type='text' class='form-control text-right' id='dr_journal_" + count + "' name='dr_journal_" + count + "' value='0' onchange=\"toFloat('dr_journal_" + count
						+ "');journalclear('dr_journal_" + count
						+ "');journalupdate('dr_journal_" + count + "');\"></td>");
				Str.append("<td><input type='text' class='form-control text-right' id='cr_journal_" + count + "' name='cr_journal_" + count + "' value='0' onchange=\"toFloat('cr_journal_" + count
						+ "');journalclear('cr_journal_" + count
						+ "');journalupdate('cr_journal_" + count + "');\"></td>");
				Str.append("</tr>");

			} else {

				while (count < 2) {
					++count;
					Str.append("<tr id='tr_" + count + "' class='ledger_tr'>");
					Str.append("<td id='td_" + count + "' align='center'><input type='text' id='count_" + count + "' value='" + count + "' hidden/>" + ++index + "</td>");
					Str.append("<td onclick='deleteRow(this);' align='center'>X</td>");
					Str.append("<td width='50%'><select onchange='addNewRecord();' class='form-control select2 ledger' id='dr_ledger_" + count + "' id='dr_ledger_" + count + "' ></select>"
							+ "<input type=hidden value='0' id=hid_ledger_" + count + " name=hid_ledger_" + count + "> </td>");
					Str.append("<td><input type='text' class='form-control text-right' id='dr_journal_" + count + "' name='dr_journal_" + count + "' value='0' onchange=\"toFloat('dr_journal_" + count
							+ "');journalclear('dr_journal_" + count
							+ "');journalupdate('dr_journal_" + count + "');\"></td>");
					Str.append("<td><input type='text' class='form-control text-right' id='cr_journal_" + count + "' name='cr_journal_" + count + "' value='0' onchange=\"toFloat('cr_journal_" + count
							+ "');journalclear('cr_journal_" + count
							+ "');journalupdate('cr_journal_" + count + "');\"></td>");
					Str.append("</tr>");
				}
			}
			Str.append("<tr>");
			Str.append("<td colspan='3' align='right'>").append("<b>Total</b>").append("</td>");
			Str.append("<td align='right'><b><input type='text' id='total_dr' name='total_dr' class='form-control text-right' readonly value='").append(df.format(total_dr)).append("'></b></td>");
			Str.append("<td align='right'><b><input type='text' id='total_cr' name='total_cr' class='form-control text-right' readonly value='").append(df.format(total_cr)).append("'></b></td>");
			Str.append("</tr>");
			Str.append("<tr>");
			Str.append("<td colspan='3' align='right'>").append("<b>Diff. Amount:</b>").append("</td>");
			if (total_cr > total_dr) {
				Str.append("<td align='right'><b><span id='dr_difference'>").append(df.format((total_cr - total_dr))).append("</span></b>");
				Str.append("<td align='right'><b><span id='cr_difference'>").append("&nbsp;").append("</span></b>");
			} else {
				Str.append("<td align='right'><b><span id='dr_difference'>").append("&nbsp;").append("</span></b>");
				Str.append("<td align='right'><b><span id='cr_difference'>").append(df.format((total_dr - total_cr))).append("</span></b>");
			}
			Str.append("<input type='hidden' id='txt_ledger_count' name='txt_ledger_count' value='" + count + "'>");
			Str.append("</tr>");
			Str.append("</tbody></table>");

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return Str.toString();
	}
	public String DisplayMultiParty(String session_id, String mainpartyamount, String comp_id) {
		int count = 0;
		double total = 0.00;
		total = Double.parseDouble(mainpartyamount);
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT cart_id, customer_name,"
				+ " cart_amount"
				+ " FROM " + compdb(comp_id) + "axela_acc_cart"
				+ " INNER JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = cart_customer_id"
				+ " WHERE 1=1"
				+ " AND cart_vouchertype_id = 15"
				+ " AND cart_session_id = " + session_id;
		// SOP("StrSql==display==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<html>\n"
						+ "<body><div class=\"table-responsive table-bordered\">"
						+ "<table class=\"table table-responsive\" data-filter=\"#filter\"><tbody>\n");
				while (crs.next()) {
					count++;
					total += crs.getDouble("cart_amount");
					Str.append("<tr>");
					Str.append("<td align='center'>" + count + "</td>");
					Str.append("<td align='center'>");
					Str.append("<a href=\"javascript:delete_cart_item(")
							.append(crs.getString("cart_id"))
							.append(");\">X</a>");
					Str.append("</td>");
					Str.append("<td align='center'>"
							+ crs.getString("customer_name") + "</td>");
					Str.append("<td align='right'>"
							+ df.format(crs.getDouble("cart_amount")) + "</td>");
					Str.append("</tr>");
				}
				Str.append("<tr>");
				Str.append("<td colspan='4' align='right'>").append(df.format(Double.parseDouble(mainpartyamount))).append("</td>");
				Str.append("</tr>");
				Str.append("<tr>");
				Str.append("<td colspan='3' align='right'>").append("<b>Total:</b>").append("</td>");
				Str.append("<td align='right'><b>").append(df.format(total)).append("</b></td>");
				Str.append("</tr>");
				Str.append("</tbody></table></div></body></html>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		return Str.toString();
	}

	public String PopulatePendingBills(HttpServletRequest request,
			String vouchertrans_customer_id, String voucher_id,
			String vouchertype_id) {
		voucher_id = CNumeric(voucher_id);
		String voucherbal_trans_id = "";
		String voucherbal_trans_idarr[] = null;
		CachedRowSet crs;
		comp_id = CNumeric(GetSession("comp_id", request));
		try {
			if (!voucher_id.equals("0")) {
				StrSql = "SELECT COALESCE(voucherbal_trans_id,0)AS voucherbal_trans_id"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " WHERE voucherbal_voucher_id = " + voucher_id;

				crs = processPrepQuery(StrSql, prepmap, 0);
				while (crs.next()) {
					voucherbal_trans_id += crs.getString("voucherbal_trans_id")
							+ ",";
				}
				crs.close();
				if (!voucherbal_trans_id.equals("")) {
					voucherbal_trans_id = voucherbal_trans_id.substring(0,
							voucherbal_trans_id.length() - 1);
					voucherbal_trans_idarr = voucherbal_trans_id.split(",");
				}
			}
			StringBuilder Str = new StringBuilder();

			int count = 0, invcount = 0, totalcount = 0;
			double vouchertype_authorize = 0, balamount = 0.00;
			String voucherbal_amount = "0";

			StrSql = "SELECT voucher_id, voucher_date,"
					+ " vouchertype_prefix, vouchertype_suffix, voucher_no,"
					+ " voucher_ref_no, voucher_amount, vouchertype_id,"
					+ " COALESCE(voucherbal_trans_id,0) AS  voucherbal_trans_id,"
					+ " COALESCE(voucherbal_amount,0) AS voucherbal_amount,"
					+ " if(vouchertype_id = 115,"
					+ "(voucher_amount - coalesce(sum(voucherbal_amount),0))"
					+ ","
					+ " (voucher_amount + coalesce(sum(voucherbal_amount),0))) "
					+ "as balamount,"
					+ " coalesce(sum(voucherbal_amount),0) as diffamount";
			if (!voucher_id.equals("0")) {
				StrSql += " , coalesce((select voucherbal_amount FROM  " + compdb(comp_id) + "axela_acc_voucher_bal"
						+ " where voucherbal_voucher_id = "
						+ voucher_id
						+ ""
						+ " and voucherbal_trans_id = voucher_id),0) receiptamount";
			}
			StrSql += " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
					+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_bal on voucherbal_trans_id = voucher_id"
					+ " WHERE 1=1 ";
			// + " AND branch_company_id = " + comp_id;

			// if (vouchertype_id.equals("105")) {
			StrSql += " AND vouchertype_id IN (105,106,107,109,111,116)";
			// }
			// if (vouchertype_id.equals("109")) {
			// StrSql += " AND vouchertype_id IN (115)";
			// }
			StrSql += " AND voucher_active = 1";
			StrSql += " AND vouchertrans_customer_id = " + vouchertrans_customer_id
					+ " GROUP BY voucher_id";
			// if (voucher_id.equals("0")) {
			if (include_zero_bal.equals("0")) {
				StrSql += " HAVING balamount > 0";
			}
			// }
			StrSql += " ORDER BY voucher_date";
			// SOP("StrSql===pending invoice===" + StrSql);
			// prepmap.put(prepkey++, vouchertrans_customer_id);
			crs = processPrepQuery(StrSql, prepmap, 0);
			prepmap.clear();
			prepkey = 1;
			if (crs.isBeforeFirst()) {
				Str.append("<html>\n"
						+ "<body><table width=80% border=1 bordercolor=\"black\" cellspacing=0 cellpadding=0 class=\"listtable\">\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th></th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Balance Amount</th>\n");
				if (vouchertype_id.equals("106")) {
					Str.append("<th>Adjustment Amount</th>\n");
				} else {
					Str.append("<th>Receipt Amount</th>\n");
				}
				Str.append("</tr>\n");
				while (crs.next()) {
					invcount++;
					String checked = "0";
					if (request.getParameter("txt_voucherid_" + count + "") != null) {
						voucherbal_trans_id = CNumeric(PadQuotes(request
								.getParameter("txt_voucherid_" + count + "")));
					} else if (!voucher_id.equals("")) {
						voucherbal_trans_id = crs
								.getString("voucherbal_trans_id");
					}

					if (request.getParameter("txt_amount_" + count + "") != null) {
						voucherbal_amount = PadQuotes(request
								.getParameter("txt_amount_" + count + ""));
						checked = CheckBoxValue(PadQuotes(request
								.getParameter("chk_" + count + "")));
					} else if (!voucher_id.equals("0")) {
						voucherbal_amount = crs.getString("voucherbal_amount");
						if (voucherbal_trans_idarr != null) {
							for (String str : voucherbal_trans_idarr) {
								if (str.equals(voucherbal_trans_id)) {
									checked = "1";
								}
							}
						}
					}
					// SOP("receipt_id===" + receipt_id);
					if (CNumeric(voucher_id).equals("0")) {
						balamount = crs.getDouble("balamount");
					} else {
						balamount = crs.getDouble("balamount")
								+ crs.getDouble("receiptamount");
					}
					// SOP("balamount==="+balamount);
					Str.append("<tr align=center id=\"row_" + count + "\">");
					Str.append("<td>");
					Str.append(invcount);
					Str.append("</td>\n");
					Str.append("<td>");
					Str.append("<input type=\"hidden\" id=\"txt_voucherid_"
							+ count
							+ "\" name=\"txt_voucherid_" + count
							+ "\" value=" + crs.getString("voucher_id") + ">");
					Str.append("<input type=\"checkbox\" title='ckeckbox' name='chk_"
							+ count
							+ "' id='chk_"
							+ count
							+ "' "
							+ PopulateCheck(checked)
							+ " onclick=\"CalAmountBal(" + count + ");\"/>");
					Str.append("</td>\n");
					Str.append("<td align='center' >").append(
							"<a href='../accounting/voucher-list.jsp?vouchertype_id="
									+ crs.getString("vouchertype_id")
									+ "&voucher_id="
									+ crs.getString("voucher_id") + "'>");
					Str.append(crs.getString("voucher_id")).append("</a></td>");
					Str.append("<td align='center' >")
							.append(strToLongDate(crs.getString("voucher_date")))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("vouchertype_prefix")
									+ crs.getString("voucher_no")
									+ crs.getString("vouchertype_suffix"))
							.append("</td>");
					Str.append("<td align='center' >")
							.append(crs.getString("voucher_ref_no"))
							.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(crs.getString("voucher_amount"));
					Str.append("</td>\n");
					Str.append("<td align=right >");
					Str.append(df.format(balamount));
					Str.append("<input type=\"hidden\" id=\"txt_balamount_"
							+ count + "\" name=\"txt_balamount_" + count
							+ "\" value=" + balamount + ">");
					Str.append("<input type=\"hidden\" id=\"txt_voucherno_"
							+ count + "\" name=\"txt_voucherno_" + count
							+ "\" value=" + crs.getString("voucher_no") + ">");
					Str.append("</td>\n");
					Str.append("<td align=right><font color='red'><b><span id=\"sp_rebaldispaly_" + count
							+ "\"></span></b></font>&nbsp;&nbsp;");
					if (!CNumeric(voucher_id).equals("0")
							&& crs.getDouble("receiptamount") != 0) {
						Str.append("<input name='txt_amount_" + count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);CalAmountBal();\" value='"
								+ crs.getString("receiptamount")
								+ "' type='text' class='textbox adjamount' size=10 maxlength=10   />");
					} else if (Double.parseDouble(voucherbal_amount) == 0
							&& crs.getDouble("balamount") != 0) {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);CalAmountBal();\" value='"
								+ balamount // voucherbal_amount
								+ "' type='text' class='textbox adjamount' size=10 maxlength=10  />");
					} else {
						Str.append("<input name='txt_amount_"
								+ count
								+ "' id='txt_amount_"
								+ count
								+ "' onkeyup=\"toFloat(this.id);CalAmountBal();\" value='"
								+ balamount // /voucherbal_amount
								+ "' type='text' class='textbox adjamount' size=10 maxlength=10  />");
					}
					Str.append("</td>\n");
					Str.append("</tr>\n");
					count++;
				}
				totalcount = count;
				Str.append("<input type=\"hidden\" id=\"txt_count\" name=\"txt_count\" value="
						+ count + ">");
				Str.append("</table>\n</body>\n</html>");
				crs.close();
			} else {

			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateUnAllocatedVoucher(String customer_id) {
		StringBuilder Str = new StringBuilder();
		int count = 0, billcount = 0;;
		StrSql = " SELECT voucher_id, CONCAT(vouchertype_prefix,voucher_no,vouchertype_suffix) AS voucher_no,"
				+ " voucher_date, voucher_ref_no,"
				+ " voucher_amount,"
				+ " COALESCE (SUM(voucherbal_amount), 0) AS adjustedamt,"
				+ " COALESCE ((voucher_amount-COALESCE (SUM(voucherbal_amount), 0) ), 0) AS balanceamount"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ " INNER JOIN  " + compdb(comp_id) + "" + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_bal ON voucherbal_voucher_id = voucher_id"
				+ " WHERE 1=1"
				+ " AND voucher_customer_id = " + customer_id
				+ " AND vouchertype_id IN (105,106,107,109,111,116,117)"
				// + " AND branch_company_id = " + comp_id
				+ " GROUP BY voucher_id"
				+ " HAVING voucher_amount > adjustedamt";
		// SOP("StrSql===PopulateUnAllocatedVoucher====" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				Str.append("<html>\n"
						+ "<body><table width=80% border=1 bordercolor=\"black\" cellspacing=0 cellpadding=0 class=\"listtable\">\n");
				Str.append("<tr align=center>\n");
				Str.append("<th>#</th>\n");
				Str.append("<th></th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Date</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th>Ref. No.</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Balance Amount</th>\n");
				Str.append("<th>Adjustment Amount</th>\n");

				while (crs.next()) {
					billcount++;
					Str.append("<tr>");
					Str.append("<td align='center'>").append(billcount).append("</td>");
					Str.append("<td align='center'>")
							.append("<input type='hidden' id='txt_unallocated_voucherid_"
									+ count
									+ "' name='txt_unallocated_voucherid_" + count
									+ "' value='" + crs.getString("voucher_id") + "' />")
							.append("<input type=checkbox  name='chk_unallocated_"
									+ count
									+ "' id='chk_unallocated_"
									+ count
									+ "' "
									// + PopulateCheck("0")
									+ " onclick='CalcBalanceCheck(" + count + ");' />")
							.append("</td>");
					Str.append("<td align='center'>").append(crs.getString("voucher_id")).append("</td>");
					Str.append("<td align='center'>").append(strToShortDate(crs.getString("voucher_date"))).append("</td>");
					Str.append("<td align='center'>").append(crs.getString("voucher_no")).append("</td>");
					Str.append("<td align='center'>").append(crs.getString("voucher_ref_no")).append("</td>");
					Str.append("<td align='right'>").append(df.format(crs.getDouble("voucher_amount"))).append("</td>");
					Str.append("<td align='right'>").append(df.format(crs.getDouble("balanceamount"))).append("</td>");
					Str.append("<td align='right'>")
							.append("<font color='red'><b><span id='txt_unallocated_bal_" + count + "'></span></b></font>")
							.append("<input type='hidden' id='txt_unallocated_main_amount_" + count
									+ "' name='txt_unallocated_main_amount_" + count
									+ "' value='" + df.format(crs.getDouble("balanceamount")) + "' />")
							.append("&nbsp;&nbsp;&nbsp;&nbsp;<input name='txt_unallocated_amount_" + count
									+ "' id='txt_unallocated_amount_"
									+ count
									+ "' onkeyup=\"toFloat(this.id);CalcBalanceText(" + count + ");\" value='"
									+ df.format(crs.getDouble("balanceamount"))
									+ "' type='text' class='textbox adjamount' size=10 maxlength=10  />").append("</td>");
					Str.append("</tr>");
					count++;
				}
				Str.append("<input type=\"hidden\" id=\"txt_unallocated_count\" name=\"txt_unallocated_count\" value="
						+ count + ">");
				Str.append("<table>");
				crs.close();
			} else {
				Str.append("<br><br><br><br><font color='red'><b>No Records Found!</b></fomt>");
				Str.append("<input type=\"hidden\" id=\"txt_unallocated_count\" name=\"txt_unallocated_count\" value="
						+ count + ">");
			}
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
