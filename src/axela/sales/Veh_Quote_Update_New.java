package axela.sales;

//aJIt 1st December, 2012

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class Veh_Quote_Update_New extends Connect {

	public String emp_id = "0", emp_branch_id = "0";
	public String add = "", addB = "";
	public String update = "", updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String comp_id = "0";
	public String msg = "", msgChk = "", errormsg = "";
	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String voucher_id = "0", session_id = "", voucher_vouchertype_id = "6", invoice_rateclass_id = "0";
	public String vehstocklocation_id = "0";
	public String branch_id = "0", branch_email1 = "", branch_name = "";
	public String rateclass_id = "0";
	public String enquiry_enquirytype_id = "0";
	// * Quote Variables */
	public String quote_id = "0", so_id = "0", vehstock_id = "0", vehstock_comm_no = "";
	public String addoff_item_check = "";
	public String quote_date = "";
	public String quotedate = "";
	public String quote_netqty = "";
	public String quote_netamt = "";
	public String quote_discamt = "";
	public String quote_grandtotal = "";
	public String quote_exprice = "";
	public String quote_enquiry_id = "0", lead_id = "0";
	public String quote_totaltax = "";
	public static String quote_emp_id = "0";
	public String quote_inscomp_id = "0";
	public String quote_refno = "";
	public String quote_active = "0";
	public String quote_notes = "";
	public String quote_auth = "0", quote_auth_id = "0";
	public String quote_auth_date = "", quote_authdate = "";
	public String quote_vehstock_id = "0";
	public String quote_preownedstock_id = "0";
	public String quote_fin_loan1 = "", quote_fin_loan2 = "", quote_fin_loan3 = "";
	public String quote_fin_tenure1 = "", quote_fin_tenure2 = "", quote_fin_tenure3 = "";
	public String quote_fin_adv_emi1 = "", quote_fin_adv_emi2 = "", quote_fin_adv_emi3 = "";
	public String quote_fin_emi1 = "", quote_fin_emi2 = "", quote_fin_emi3 = "";
	public String quote_fin_baloonemi1 = "", quote_fin_baloonemi2 = "", quote_fin_baloonemi3 = "";
	public String quote_fin_fee1 = "", quote_fin_fee2 = "", quote_fin_fee3 = "";
	public String quote_fin_downpayment1 = "", quote_fin_downpayment2 = "", quote_fin_downpayment3 = "";
	public String quote_desc = "", quote_terms = "", item_netamountwod = "";
	public String quote_entry_id = "0", quote_entry_by = "", quote_entry_date = "";
	public String quote_modified_id = "0", quote_modified_by = "";
	public String quote_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	/* End Of Quote Variables */
	/* Config Variables */
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String brandconfig_quote_email_enable = "0";
	public String brandconfig_quote_email_format = "";
	public String brandconfig_quote_email_sub = "";
	public String brandconfig_quote_sms_enable = "0";
	public String brandconfig_quote_sms_format = "";
	public String branch_quote_email_exe_sub = "";
	public String branch_quote_email_exe_format = "";
	public String config_admin_email = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String config_customer_dupnames = "0";
	public String config_sales_quote_refno = "0";
	public String emp_quote_priceupdate = "";
	public String emp_quote_discountupdate = "";
	public String emp_role_id = "0";
	/* End of Config Variables */
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df1 = new DecimalFormat("0");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String contact_id = "0", item_id = "0", item_name = "";
	public String quote_contact_id = "0", item_small_desc = "";
	public String quote_customer_id = "0";
	public String quote_itemid = "0";
	public String customer_id = "0";
	public String customer_address = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String branch_city_id = "0";
	public String model_id = "0", model_name = "";
	public String branch_pin = "";
	public String readOnly = "";
	public String display = "";
	public String emp_all_exe = "";
	public String quoteitem_rowcount = "", quoteitem_item_id = "0";
	public String quoteitem_option_id = "0", quoteitem_option_group = "";
	public String quoteitem_item_serial = "", quoteitem_price = "0";
	public String quoteitem_qty = "0", quoteitem_disc = "0";

	public String quoteitem_tax = "0", quoteitem_tax2 = "0", quoteitem_tax3 = "0", quoteitem_tax4 = "0";
	public String quoteitem_tax_id = "0", quoteitem_tax2_id = "0", quoteitem_tax3_id = "0", quoteitem_tax4_id = "0";
	public String quoteitem_tax_rate = "0", quoteitem_tax2_rate = "0", quoteitem_tax3_rate = "0", quoteitem_tax4_rate = "0";

	public String quoteitem_total = "0";
	public String item_netprice = "0", item_netdisc = "0", item_price = "0";

	public String item_tax1_id = "0", item_tax1_rate = "0";
	public String item_tax2_id = "0", item_tax2_rate = "0";
	public String item_tax3_id = "0", item_tax3_rate = "0";
	public String item_tax4_id = "0", item_tax4_rate = "0";

	public String item_netamount = "0", item_tax1 = "0", item_tax2 = "0", item_tax3 = "0", item_tax4 = "0";
	public String beforetax_gp_count = "", aftertax_gp_count = "";
	public String quote_fin_option1 = "", quote_fin_option2 = "", quote_fin_option3 = "", android = "";
	public String app = "";
	public String gsttype = "", groupName = "";

	public List<Map> configuredItemList = new ArrayList<Map>();
	public LinkedHashSet<String> groupSet = new LinkedHashSet<String>();
	public Map<String, String> mainItemDetails = new HashMap<String, String>();
	public Map<String, String> groupItemCount = new HashMap<String, String>();
	public Map<String, String> itemTotals = new HashMap<String, String>();// It holds total calculated Amounts i.e Ex-Showroom Price.

	public Map<String, String> totalBtItemDetails = new HashMap<String, String>();
	public Map<String, String> totalAtItemDetails = new HashMap<String, String>();

	public Map<String, String> configuredbtItem;
	public Map<String, String> configuredatItem;
	public boolean containsMap = false, containsGroup = false;

	public String jsonconfiguredItemList = "";
	public String itemdetails = "", addItemToList = "";
	public String itemids = "";

	// JSONObject
	Gson gson = new Gson();
	JSONObject input = new JSONObject();

	// Script
	public ScriptEngineManager manager = new ScriptEngineManager();
	public ScriptEngine engine = manager.getEngineByName("JavaScript");

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_quote_access", request, response);

			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				emp_all_exe = GetSession("emp_all_exe", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				// For Generating session each time
				session_id = PadQuotes(request.getParameter("txt_session_id"));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				quote_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				quote_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				quote_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				vehstock_comm_no = PadQuotes(request.getParameter("vehstock_comm_no"));
				quote_preownedstock_id = CNumeric(PadQuotes(request.getParameter("preownedstock_id")));
				quote_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
				android = PadQuotes(request.getParameter("android"));
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine eng = mgr.getEngineByName("JavaScript");
				// response1 = response;

				if (!quote_enquiry_id.equals("0") && add.equals("yes")) {
					GetEnquiryDetails(response);
				}

				if (!vehstock_id.equals("") && add.equals("yes") && !vehstock_id.equals("0")) {
					quote_vehstock_id = vehstock_id;
				}

				empEditperm = ReturnPerm(comp_id, "emp_sales_quote_edit", request);

				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}
				quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
				// SOP("quote_id1========="+quote_id);
				PopulateContactDetails();
				PopulateConfigDetails();

				QueryString = PadQuotes(request.getQueryString());
				// SOP("QueryString====="+QueryString);
				if (add.equals("yes")) {
					status = "Add";
					item_id = PadQuotes(request.getParameter("item_id"));

					if (!item_id.equals("0") && enquiry_enquirytype_id.equals("1")) {
						quote_customer_id = CNumeric(ExecuteQuery("SELECT enquiry_customer_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + quote_enquiry_id));
						gsttype = GetGstType(quote_customer_id, branch_id, comp_id);
						StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
								+ " price_amt, item_small_desc, price_disc,";
						if (gsttype.equals("state")) {
							StrSql += " COALESCE(sgst.customer_id, 0) AS tax1_id,"
									+ " COALESCE(sgst.customer_rate, 0) AS tax1_rate,"
									+ "	COALESCE(cgst.customer_id, 0) AS tax2_id,"
									+ "	COALESCE(cgst.customer_rate, 0) AS tax2_rate,";
						} else if (gsttype.equals("central")) {
							StrSql += " COALESCE(igst.customer_id, 0) AS tax3_id,"
									+ " COALESCE(igst.customer_rate, 0) AS tax3_rate,";
						}
						StrSql += " COALESCE(cess.customer_id, 0) AS tax4_id,"
								+ "	COALESCE(cess.customer_rate, 0) AS tax4_rate,"
								+ " model_id, model_name"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
								+ " AND price_rateclass_id	 = branch_rateclass_id"
								+ " AND price_effective_from <= " + ToLongDate(kknow()) + ""
								+ " AND price_active = '1'";
						if (gsttype.equals("state")) {
							StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer sgst ON sgst.customer_id = item_salestax1_ledger_id"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cgst ON cgst.customer_id = item_salestax2_ledger_id";
						} else if (gsttype.equals("central")) {
							StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer igst ON igst.customer_id = item_salestax3_ledger_id";
						}
						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer cess ON cess.customer_id = item_salestax4_ledger_id"
								+ " WHERE item_id = " + CNumeric(item_id)
								+ " ORDER BY" + " price_effective_from" + " DESC"
								+ " LIMIT 1";
						// SOP("StrSql==111==" + StrSqlBreaker(StrSql));
						CachedRowSet crs = processQuery(StrSql, 0);

						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								item_id = crs.getString("item_id");
								item_name = crs.getString("item_name");
								model_id = crs.getString("model_id");
								model_name = crs.getString("model_name");

								item_price = crs.getString("price_amt");
								if (gsttype.equals("state")) {
									item_netprice = Double.toString((crs.getDouble("price_amt") * crs.getDouble("tax1_rate") / 100) + crs.getDouble("price_amt"));
									item_netprice = Double.toString((crs.getDouble("price_amt") * crs.getDouble("tax2_rate") / 100) + Double.parseDouble(item_netprice));
								} else if (gsttype.equals("central")) {
									item_netprice = Double.toString((crs.getDouble("price_amt") * crs.getDouble("tax3_rate") / 100) + crs.getDouble("price_amt"));
								}
								item_netprice = Double.toString((crs.getDouble("price_amt") * crs.getDouble("tax4_rate") / 100) + Double.parseDouble(item_netprice));

								if (gsttype.equals("state")) {
									item_tax1_id = crs.getString("tax1_id");
									item_tax1_rate = crs.getString("tax1_rate");
									item_tax2_id = crs.getString("tax2_id");
									item_tax2_rate = crs.getString("tax2_rate");
								} else if (gsttype.equals("central")) {
									item_tax3_id = crs.getString("tax3_id");
									item_tax3_rate = crs.getString("tax3_rate");
								}
								item_tax4_id = crs.getString("tax4_id");
								item_tax4_rate = crs.getString("tax4_rate");

								item_netdisc = Double.toString(crs.getDouble("price_disc"));
								item_small_desc = crs.getString("item_small_desc");
								if (gsttype.equals("state")) {
									item_tax1 = Double.toString((crs.getDouble("price_amt") - crs.getDouble("price_disc")) * crs.getDouble("tax1_rate") / 100);
									item_tax2 = Double.toString((crs.getDouble("price_amt") - crs.getDouble("price_disc")) * crs.getDouble("tax2_rate") / 100);
								} else if (gsttype.equals("central")) {
									item_tax3 = Double.toString((crs.getDouble("price_amt") - crs.getDouble("price_disc")) * crs.getDouble("tax3_rate") / 100);
								}
								item_tax4 = Double.toString((crs.getDouble("price_amt") - crs.getDouble("price_disc")) * crs.getDouble("tax4_rate") / 100);

								if (gsttype.equals("state")) {
									item_netamount = Double.toString(((crs.getDouble("price_amt")
											- crs.getDouble("price_disc")) * (crs.getDouble("tax1_rate") / 100) * (crs.getDouble("tax2_rate") / 100))
											+ (crs.getDouble("price_amt") - crs.getDouble("price_disc")));

									item_netamountwod = Double.toString((crs.getDouble("price_amt")
											* (crs.getDouble("tax1_rate") / 100) * (crs.getDouble("tax2_rate") / 100))
											+ crs.getDouble("price_amt"));
								} else if (gsttype.equals("central")) {
									item_netamount = Double.toString(((crs.getDouble("price_amt") - crs.getDouble("price_disc"))
											* crs.getDouble("tax3_rate") / 100)
											+ (crs.getDouble("price_amt") - crs.getDouble("price_disc")));

									item_netamountwod = Double.toString((crs.getDouble("price_amt")
											* crs.getDouble("tax3_rate") / 100)
											+ crs.getDouble("price_amt"));
								}

								item_netamount = Double.toString(((crs.getDouble("price_amt")
										- crs.getDouble("price_disc"))
										* crs.getDouble("tax4_rate") / 100)
										+ (crs.getDouble("price_amt")
										- crs.getDouble("price_disc")) + Double.parseDouble(item_netamount));
								item_netamountwod = Double.toString((crs.getDouble("price_amt")
										* crs.getDouble("tax4_rate") / 100)
										+ crs.getDouble("price_amt") + Double.parseDouble(item_netamountwod));
							}
						} else {
							// response.sendRedirect(response .encodeRedirectURL("../portal/error.jsp?msg=Invalid Item!"));
						}
						crs.close();
					}
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add) && android.equals("yes")) {
					display = "none";
					if (!"yes".equals(addB)) {
						StrSql = "SELECT branch_quote_desc, branch_quote_terms"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE"
								+ " branch_id = " + branch_id + "";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							quote_desc = crs.getString("branch_quote_desc");
							quote_terms = crs.getString("branch_quote_terms");
						}
						crs.close();
						quote_date = ToLongDate(kknow());
						quotedate = strToShortDate(quote_date);
						quote_auth = "0";
						quote_netqty = "0";
						quote_netamt = "0";
						quote_grandtotal = "0";
						quote_totaltax = "0";
						quote_active = "1";
					} else {
						GetValues(request, response);

						if (ReturnPerm(comp_id, "emp_sales_quote_add", request).equals("1")) {
							if (!branch_id.equals("0")) {
								StrSql = "SELECT branch_city_id, branch_pin"
										+ " FROM " + compdb(comp_id) + "axela_branch"
										+ " WHERE"
										+ " branch_id = " + branch_id + "";
								CachedRowSet crs = processQuery(StrSql, 0);
								while (crs.next()) {
									branch_city_id = crs.getString("branch_city_id");
									branch_pin = crs.getString("branch_pin");
								}
								crs.close();
							}
							quote_entry_id = emp_id;
							quote_entry_date = ToLongDate(kknow());
							AddFields(request, null);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								msg = "Quote Added Successfully";
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(add) && !android.equals("yes")) {
					display = "none";
					if (!"yes".equals(addB)) {
						StrSql = "SELECT branch_quote_desc, branch_quote_terms"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE"
								+ " branch_id = " + branch_id + "";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							quote_desc = crs.getString("branch_quote_desc");
							quote_terms = crs.getString("branch_quote_terms");
						}
						crs.close();
						quote_date = ToLongDate(kknow());
						quotedate = strToShortDate(quote_date);
						quote_auth = "0";
						quote_netqty = "0";
						quote_netamt = "0";
						quote_grandtotal = "0";
						quote_totaltax = "0";
						quote_active = "1";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_sales_quote_add", request).equals("1")) {
							if (!branch_id.equals("0")) {
								StrSql = "SELECT branch_city_id, branch_pin"
										+ " FROM " + compdb(comp_id) + "axela_branch"
										+ " WHERE"
										+ " branch_id = " + branch_id + "";
								CachedRowSet crs = processQuery(StrSql, 0);
								while (crs.next()) {
									branch_city_id = crs.getString("branch_city_id");
									branch_pin = crs.getString("branch_pin");
								}
								crs.close();
							}
							quote_entry_id = emp_id;
							quote_entry_date = ToLongDate(kknow());
							AddFields(request, null);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("veh-quote-list.jsp?quote_id=" + quote_id + "&msg=Quote added successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					if (!updateB.equals("yes") && !deleteB.equals("Delete Quote")) {
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
						PopulateFields(request, response);
						contact_id = quote_contact_id;
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Quote")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_sales_quote_edit", request).equals("1")) {
							quote_modified_id = emp_id;
							quote_modified_date = ToLongDate(kknow());
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("veh-quote-list.jsp?quote_id=" + quote_id + "&msg=Quote updated successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Quote")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_sales_quote_delete", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("veh-quote-list.jsp?msg=Quote deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
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
		// SOP("GetValues==");
		enquiry_enquirytype_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_enquirytype_id")));
		branch_id = PadQuotes(request.getParameter("dr_branch"));
		lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		item_id = PadQuotes(request.getParameter("dr_item_id"));
		item_name = PadQuotes(request.getParameter("txt_item_name"));
		item_small_desc = unescapehtml(PadQuotes(request.getParameter("txt_item_small_desc")));
		model_id = PadQuotes(request.getParameter("dr_model_id"));
		model_name = PadQuotes(request.getParameter("txt_model_name"));
		item_price = PadQuotes(request.getParameter("txt_item_priceamt"));
		item_netdisc = PadQuotes(request.getParameter("txt_item_netdisc"));
		item_netprice = PadQuotes(request.getParameter("txt_item_price"));
		item_tax1_id = PadQuotes(request.getParameter("txt_item_tax_id"));
		item_tax1_rate = PadQuotes(request.getParameter("txt_item_tax_rate"));
		quote_auth = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_auth")));
		quote_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_quote_vehstock_id")));
		quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
		quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
		quotedate = PadQuotes(request.getParameter("txt_quote_date"));
		quote_netqty = CNumeric(PadQuotes(request.getParameter("txt_quote_qty")));
		quote_netamt = CNumeric(PadQuotes(request.getParameter("txt_quote_netamt")));
		quote_discamt = CNumeric(PadQuotes(request.getParameter("txt_quote_discamt")));
		quote_totaltax = CNumeric(PadQuotes(request.getParameter("txt_quote_totaltax")));
		quote_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_quote_grandtotal")));
		// SOP("quote_grandtotal==GetValues==" + quote_grandtotal);

		if (quote_vehstock_id.equals("0")) {
			quote_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_quote_vehstock_id")));
		}
		quote_preownedstock_id = CNumeric(PadQuotes(request.getParameter("txt_quote_preownedstock_id")));
		// SOP("quote_preownedstock_id==GetValues==" + quote_preownedstock_id);
		quote_fin_loan1 = PadQuotes(request.getParameter("txt_quote_fin_loan1"));
		quote_fin_loan2 = PadQuotes(request.getParameter("txt_quote_fin_loan2"));
		quote_fin_loan3 = PadQuotes(request.getParameter("txt_quote_fin_loan3"));
		quote_fin_tenure1 = PadQuotes(request.getParameter("txt_quote_fin_tenure1"));
		quote_fin_tenure2 = PadQuotes(request.getParameter("txt_quote_fin_tenure2"));
		quote_fin_tenure3 = PadQuotes(request.getParameter("txt_quote_fin_tenure3"));
		quote_fin_adv_emi1 = PadQuotes(request.getParameter("txt_quote_fin_adv_emi1"));
		quote_fin_adv_emi2 = PadQuotes(request.getParameter("txt_quote_fin_adv_emi2"));
		quote_fin_adv_emi3 = PadQuotes(request.getParameter("txt_quote_fin_adv_emi3"));
		quote_fin_emi1 = PadQuotes(request.getParameter("txt_quote_fin_emi1"));
		quote_fin_emi2 = PadQuotes(request.getParameter("txt_quote_fin_emi2"));
		quote_fin_emi3 = PadQuotes(request.getParameter("txt_quote_fin_emi3"));
		quote_fin_baloonemi1 = PadQuotes(request.getParameter("txt_quote_fin_baloonemi1"));
		quote_fin_baloonemi2 = PadQuotes(request.getParameter("txt_quote_fin_baloonemi2"));
		quote_fin_baloonemi3 = PadQuotes(request.getParameter("txt_quote_fin_baloonemi3"));
		quote_fin_fee1 = PadQuotes(request.getParameter("txt_quote_fin_fee1"));
		quote_fin_fee2 = PadQuotes(request.getParameter("txt_quote_fin_fee2"));
		quote_fin_fee3 = PadQuotes(request.getParameter("txt_quote_fin_fee3"));
		quote_fin_downpayment1 = PadQuotes(request.getParameter("txt_quote_fin_downpayment1"));
		quote_fin_downpayment2 = PadQuotes(request.getParameter("txt_quote_fin_downpayment2"));
		quote_fin_downpayment3 = PadQuotes(request.getParameter("txt_quote_fin_downpayment3"));
		quotedate = PadQuotes(request.getParameter("txt_quote_date"));
		quote_netqty = CNumeric(PadQuotes(request.getParameter("txt_quote_qty")));
		quote_netamt = CNumeric(PadQuotes(request.getParameter("txt_quote_netamt")));
		quote_discamt = CNumeric(PadQuotes(request.getParameter("txt_quote_discamt")));
		quote_totaltax = CNumeric(PadQuotes(request.getParameter("txt_quote_totaltax")));
		quote_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_quote_grandtotal")));
		quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
		quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
		quote_refno = PadQuotes(request.getParameter("txt_quote_refno"));
		quote_active = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_active")));
		quote_fin_option1 = PadQuotes(request.getParameter("dr_quote_fin_option1"));
		quote_fin_option2 = PadQuotes(request.getParameter("dr_quote_fin_option2"));
		quote_fin_option3 = PadQuotes(request.getParameter("dr_quote_fin_option3"));
		quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
		quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
		quote_refno = PadQuotes(request.getParameter("txt_quote_refno"));
		quote_active = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_active")));
		quote_inscomp_id = CNumeric(PadQuotes(request.getParameter("dr_quote_inscomp_id")));
		quote_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		quote_notes = PadQuotes(request.getParameter("txt_quote_notes"));
		quote_entry_by = PadQuotes(request.getParameter("entry_by"));
		quote_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		beforetax_gp_count = PadQuotes(request.getParameter("txt_bt_group_count"));
		aftertax_gp_count = PadQuotes(request.getParameter("txt_at_group_count"));
	}

	protected void CheckForm() {
		msg = "";

		if (quotedate.equals("")) {
			msg += "<br>Enter Quote Date!";
		} else {
			if (isValidDateFormatShort(quotedate)) {
				quote_date = ConvertShortDateToStr(quotedate);
			} else {
				msg += "<br>Enter Valid Quote Date!";
			}
		}

		if (item_id.equals("0")) {
			msg += "<br>Select the Item!";
		}

		if (!quote_vehstock_id.equals("0") && !item_id.equals("0")) {
			StrSql = "SELECT vehstock_item_id FROM " + compdb(comp_id)
					+ "axela_vehstock"
					+ " WHERE vehstock_id = " + quote_vehstock_id + "";

			if (!ExecuteQuery(StrSql).equals(item_id)) {
				// msg = msg + "<br>Invalid Stock ID!";
				quote_vehstock_id = "0";
			}
		}

		if (config_sales_quote_refno.equals("1")) {
			if (quote_refno.equals("")) {
				msg += "<br>Enter Quote Reference No.!";
			} else {
				if (quote_refno.length() < 2) {
					msg += "<br>Quote Reference No. Should be Atleast Two Digits! ";
				}

				if (!branch_id.equals("0")) {
					StrSql = "SELECT quote_refno FROM " + compdb(comp_id)
							+ "axela_sales_quote" + " WHERE quote_branch_id = "
							+ branch_id + " " + " AND quote_refno = '"
							+ quote_refno + "'";
					if (update.equals("yes")) {
						StrSql += " and quote_id != " + quote_id;
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Quote Reference No. found!";
					}
				}
			}
		}

		if (!isNumeric(quote_totaltax)) {
			quote_totaltax = "0.0";
		}

		if (quote_grandtotal.equals("0.00")) {
			msg += "<br>Quote Amount cannot be equal to zero!";
		}

		// if (!quote_preownedstock_id.equals("0")) {
		// StrSql = "SELECT so_preownedstock_id FROM " + compdb(comp_id) + "axela_sales_so"
		// + " WHERE so_preownedstock_id = " + quote_preownedstock_id + ""
		// + " AND so_active = 1";
		// if (ExecuteQuery(StrSql).equals(quote_preownedstock_id)) {
		// msg += "<br>Pre-Owned Stock is associated with other Sales Order!";
		// }
		// }

		if (!quote_fin_loan1.equals("") && quote_fin_option1.equals("")) {
			msg += "<br>Select Finance Option 1!";
		} else if (quote_fin_loan1.equals("") && !quote_fin_option1.equals("")) {
			msg += "<br>Enter Loan Amount for Option 1!";
		}

		if (!quote_fin_loan2.equals("") && quote_fin_option2.equals("")) {
			msg += "<br>Select Finance Option 2!";
		} else if (quote_fin_loan2.equals("") && !quote_fin_option2.equals("")) {
			msg += "<br>Enter Loan Amount for Option 2!";
		}

		if (!quote_fin_loan3.equals("") && quote_fin_option3.equals("")) {
			msg += "<br>Select Finance Option 3!";
		} else if (quote_fin_loan3.equals("") && !quote_fin_option3.equals("")) {
			msg += "<br>Enter Loan Amount for Option 3!";
		}

		// if (quote_inscomp_id.equals("0")) {
		// msg += "<br>Select Insurance Company!";
		// }
		if (quote_emp_id.equals("0")) {
			msg += "<br>Select Sales Consultant!";
		}
		// SOP("quote_grandtotal==checkform==" + quote_grandtotal);
		// SOP("quote_netamt==checkform==" + quote_netamt);
		msg = msgChk + msg;
	}

	public void GetEnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT enquiry_emp_id, enquiry_enquirytype_id, lead_id, customer_id,"
					+ " customer_name," + " contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, model_name"
					+ " contact_mobile1, contact_email1, state_name, city_name,"
					+ " customer_address, customer_pin, enquiry_branch_id, rateclass_id, enquiry_custtype_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, item_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = enquiry_lead_id"
					+ " WHERE"
					+ " enquiry_id = " + CNumeric(quote_enquiry_id) + ""
					+ " GROUP BY enquiry_id";
			// SOP("enquiry=====" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_name = crs.getString("item_name");
					model_name = crs.getString("model_name");
					quote_emp_id = crs.getString("enquiry_emp_id");
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					customer_id = crs.getString("customer_id");
					lead_id = crs.getString("lead_id");
					link_customer_name = "<a href=../customer/customer-list.jsp?customer_id=" + customer_id + ">" + crs.getString("customer_name") + "</a>";
					contact_id = crs.getString("contact_id");
					link_contact_name = "<a href=../customer/customer-contact-list.jsp?contact_id=" + contact_id + ">" + crs.getString("contactname") + "</a>";
					branch_id = CNumeric(crs.getString("enquiry_branch_id"));
					rateclass_id = CNumeric(crs.getString("rateclass_id"));
					branch_name = crs.getString("branchname");
					customer_address = crs.getString("customer_address");

				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Enquiry!"));
			}
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void AddFields(HttpServletRequest request, JSONObject input)
			throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote"
						+ " ("
						+ "quote_branch_id,"
						+ " quote_prefix,"
						+ " quote_no,"
						+ " quote_date,"
						+ " quote_customer_id,"
						+ " quote_contact_id,";
				if (!quote_enquiry_id.equals("0")) {
					StrSql += " quote_lead_id,"
							+ " quote_enquiry_id,";
				}
				StrSql += " quote_item_id,"
						+ " quote_netamt,"
						+ " quote_discamt,"
						+ " quote_totaltax,"
						+ " quote_grandtotal,"
						+ " quote_vehstock_id,"
						+ " quote_preownedstock_id,"
						+ " quote_fin_option1,"
						+ " quote_fin_loan1,"
						+ " quote_fin_loan2,"
						+ " quote_fin_loan3,"
						+ " quote_fin_tenure1,"
						+ " quote_fin_tenure2,"
						+ " quote_fin_tenure3,"
						+ " quote_fin_adv_emi1,"
						+ " quote_fin_adv_emi2,"
						+ " quote_fin_adv_emi3,"
						+ " quote_fin_emi1,"
						+ " quote_fin_emi2,"
						+ " quote_fin_emi3,"
						+ " quote_fin_baloonemi1,"
						+ " quote_fin_baloonemi2,"
						+ " quote_fin_baloonemi3,"
						+ " quote_fin_fee1,"
						+ " quote_fin_fee2,"
						+ " quote_fin_fee3,"
						+ " quote_fin_downpayment1,"
						+ " quote_fin_option2,"
						+ " quote_fin_downpayment2,"
						+ " quote_fin_option3,"
						+ " quote_fin_downpayment3,"
						+ " quote_desc,"
						+ " quote_terms,"
						+ " quote_inscomp_id,"
						+ " quote_emp_id,";
				if (config_sales_quote_refno.equals("1")) {
					StrSql += " quote_refno,";
				}
				StrSql += " quote_auth,"
						+ " quote_active,"
						+ " quote_notes,"
						+ " quote_entry_id,"
						+ " quote_entry_date"
						+ ")"
						+ " VALUES"
						+ " ("
						+ branch_id + ","
						+ " (SELECT branch_quote_prefix"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE"
						+ " branch_id = " + branch_id
						+ "),"
						+ " COALESCE((SELECT quote.quote_no"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote AS quote"
						+ " WHERE"
						+ " quote.quote_branch_id = " + branch_id + ""
						+ " ORDER BY quote.quote_id DESC LIMIT 1),0) + 1,"
						+ " '" + quote_date + "',"
						+ " " + quote_customer_id + ","
						+ " " + quote_contact_id + ",";
				if (!quote_enquiry_id.equals("0")) {
					StrSql += " " + lead_id + ","
							+ " " + quote_enquiry_id + ",";
				}
				StrSql += " " + item_id + ","
						+ " " + quote_netamt + ","
						+ " " + quote_discamt + ","
						+ " " + quote_totaltax + ","
						+ " " + Double.parseDouble(quote_grandtotal) + ","
						+ " " + CNumeric(quote_vehstock_id) + ","
						+ " " + CNumeric(quote_preownedstock_id) + ","
						+ " '" + quote_fin_option1 + "',"
						+ " '" + quote_fin_loan1 + "',"
						+ " '" + quote_fin_loan2 + "',"
						+ " '" + quote_fin_loan3 + "',"
						+ " '" + quote_fin_tenure1 + "',"
						+ " '" + quote_fin_tenure2 + "',"
						+ " '" + quote_fin_tenure3 + "',"
						+ " '" + quote_fin_adv_emi1 + "',"
						+ " '" + quote_fin_adv_emi2 + "',"
						+ " '" + quote_fin_adv_emi3 + "',"
						+ " '" + quote_fin_emi1 + "',"
						+ " '" + quote_fin_emi2 + "',"
						+ " '" + quote_fin_emi3 + "',"
						+ " '" + quote_fin_baloonemi1 + "',"
						+ " '" + quote_fin_baloonemi2 + "',"
						+ " '" + quote_fin_baloonemi3 + "',"
						+ " '" + quote_fin_fee1 + "',"
						+ " '" + quote_fin_fee2 + "',"
						+ " '" + quote_fin_fee3 + "',"
						+ " '" + quote_fin_downpayment1 + "',"
						+ " '" + quote_fin_option2 + "',"
						+ " '" + quote_fin_downpayment2 + "',"
						+ " '" + quote_fin_option3 + "',"
						+ " '" + quote_fin_downpayment3 + "',"
						+ " '" + quote_desc + "',"
						+ " '" + quote_terms.replaceAll("&lt;p&gt;", "").replaceAll("&lt;/p&gt;", "")
						+ "'," + " " + quote_inscomp_id + ","
						+ " " + quote_emp_id + ",";
				if (config_sales_quote_refno.equals("1")) {
					StrSql += " '" + quote_refno + "',";
				}
				StrSql += " '" + quote_auth + "',"
						+ " '" + quote_active + "',"
						+ " '" + quote_notes + "',"
						+ " " + quote_entry_id + ","
						+ " '" + quote_entry_date + "')";
				// SOP("StrSql==sales quote==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

				ResultSet rs1 = stmttx.getGeneratedKeys();

				while (rs1.next()) {
					quote_id = rs1.getString(1);
				}
				rs1.close();
				contact_id = quote_contact_id;
				PopulateContactDetails();
				// SOP("quote_id==========" + quote_id);
				// Adding the configured items into Quote-Item table
				if (android.equals("")) {
					AddItemFields(request);
				}
				conntx.commit();

				// Get the total from Quote-Item table
				StrSql = "SELECT SUM(quoteitem_price) AS quote_netamt,"
						+ " SUM(quoteitem_disc) AS quote_discamt,"
						+ " SUM(quoteitem_tax) AS quote_totaltax,"
						+ " SUM(quoteitem_total) AS quote_grandtotal,"
						+ " ( SELECT SUM(quoteitem_total)"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item item"
						+ " WHERE"
						+ " item.quoteitem_option_group_tax = 1"
						+ " AND item.quoteitem_quote_id = " + quote_id
						+ ") AS quote_exprice"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " WHERE"
						+ " quoteitem_quote_id = " + quote_id + "";
				// SOP("StrSql=back=======to get amount-----=" + StrSqlBreaker(StrSql));
				ResultSet rs = stmttx.executeQuery(StrSql);

				while (rs.next()) {
					quote_netamt = rs.getString("quote_netamt");
					quote_discamt = rs.getString("quote_discamt");
					quote_totaltax = rs.getString("quote_totaltax");
					quote_grandtotal = rs.getString("quote_grandtotal");
					quote_exprice = rs.getString("quote_exprice");
				}
				rs.close();
				// For updating the Quote total feilds
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote"
						+ " SET" + " quote_netamt = " + quote_netamt + ","
						+ " quote_discamt = " + quote_discamt + ","
						+ " quote_exprice = " + quote_exprice + ","
						+ " quote_totaltax = " + quote_totaltax + ","
						+ " quote_grandtotal = " + Double.parseDouble(quote_grandtotal) + ""
						+ " WHERE"
						+ " quote_id = " + quote_id + "";
				// SOP("Update==SalesQuote==" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET" + " enquiry_stage_id = 4"
						+ " WHERE"
						+ " enquiry_id = " + quote_enquiry_id + "";
				stmttx.execute(StrSql);

				conntx.commit();

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	protected void AddItemFields(HttpServletRequest request) throws SQLException {
		try {
			quoteitem_rowcount = CNumeric(ExecuteQuery("SELECT COALESCE(MAX(quoteitem_rowcount), 0) + 1" + " FROM " + compdb(comp_id) + "axela_sales_quote_item"));
			quote_netamt = "0";
			quote_discamt = "0";
			quote_totaltax = "0";
			quote_grandtotal = "0";
			quote_exprice = "0";
			quoteitem_rowcount = ExecuteQuery("SELECT COALESCE(MAX(quoteitem_rowcount), 0) + 1" + " FROM " + compdb(comp_id) + "axela_sales_quote_item");
			quote_desc = PadQuotes(request.getParameter("txt_item_desc"));
			quoteitem_disc = CNumeric(PadQuotes(request.getParameter("txt_item_disc")));
			quoteitem_tax_id = CNumeric(PadQuotes(request.getParameter("txt_item_tax_id1")));
			quoteitem_tax2_id = CNumeric(PadQuotes(request.getParameter("txt_item_tax_id2")));
			quoteitem_tax3_id = CNumeric(PadQuotes(request.getParameter("txt_item_tax_id3")));
			quoteitem_tax4_id = CNumeric(PadQuotes(request.getParameter("txt_item_tax_id4")));

			quoteitem_tax_rate = CNumeric(PadQuotes(request.getParameter("txt_item_tax_rate1")));
			quoteitem_tax2_rate = CNumeric(PadQuotes(request.getParameter("txt_item_tax_rate2")));
			quoteitem_tax3_rate = CNumeric(PadQuotes(request.getParameter("txt_item_tax_rate3")));
			quoteitem_tax4_rate = CNumeric(PadQuotes(request.getParameter("txt_item_tax_rate4")));

			quoteitem_total = CNumeric(PadQuotes(request.getParameter("div_main_item_amount")));

			quoteitem_tax = Double.toString((Double.parseDouble(item_price) - Double.parseDouble(quoteitem_disc)) * Double.parseDouble(quoteitem_tax_rate) / 100);
			quoteitem_tax2 = Double.toString((Double.parseDouble(item_price) - Double.parseDouble(quoteitem_disc)) * Double.parseDouble(quoteitem_tax2_rate) / 100);
			quoteitem_tax3 = Double.toString((Double.parseDouble(item_price) - Double.parseDouble(quoteitem_disc)) * Double.parseDouble(quoteitem_tax3_rate) / 100);
			quoteitem_tax4 = Double.toString((Double.parseDouble(item_price) - Double.parseDouble(quoteitem_disc)) * Double.parseDouble(quoteitem_tax4_rate) / 100);

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // To Insert Main-Item
					+ " ("
					+ "quoteitem_quote_id,"
					+ " quoteitem_rowcount,"
					+ " quoteitem_item_id,"
					+ " quoteitem_option_id,"
					+ " quoteitem_option_group,"
					+ " quoteitem_option_group_tax,"
					+ " quoteitem_item_serial,"
					+ " quoteitem_qty,"
					+ " quoteitem_price,"
					+ " quoteitem_disc,";
			if (gsttype.equals("state")) {
				StrSql += " quoteitem_tax,"
						+ " quoteitem_tax_id,"
						+ " quoteitem_tax_rate,"
						+ " quoteitem_tax2,"
						+ " quoteitem_tax2_id,"
						+ " quoteitem_tax2_rate,";
			} else if (gsttype.equals("central")) {
				StrSql += " quoteitem_tax3,"
						+ " quoteitem_tax3_id,"
						+ " quoteitem_tax3_rate,";
			}

			StrSql += " quoteitem_tax4,"
					+ " quoteitem_tax4_id,"
					+ " quoteitem_tax4_rate,"
					+ " quoteitem_total"
					+ ")"
					+ " VALUES"
					+ " (" + quote_id + ","
					+ " " + quoteitem_rowcount + ","
					+ " " + item_id + ","
					+ " 0," // quoteitem_option_id
					+ " ''," // quoteitem_option_group
					+ " '1'," // quoteitem_option_group_tax
					+ " ''," // quoteitem_item_serial
					+ " '1'," // quoteitem_qty
					+ " " + item_price + ","
					+ " " + quoteitem_disc + ",";
			if (gsttype.equals("state")) {
				StrSql += " " + quoteitem_tax + ","
						+ " " + quoteitem_tax_id + ","
						+ " " + quoteitem_tax_rate + ","

						+ " " + quoteitem_tax2 + ","
						+ " " + quoteitem_tax2_id + ","
						+ " " + quoteitem_tax2_rate + ",";
			} else if (gsttype.equals("central")) {
				StrSql += " " + quoteitem_tax3 + ","
						+ " " + quoteitem_tax3_id + ","
						+ " " + quoteitem_tax3_rate + ",";
			}
			StrSql += " " + quoteitem_tax4 + ","
					+ " " + quoteitem_tax4_id + ","
					+ " " + quoteitem_tax4_rate + ",";

			StrSql += " " + quoteitem_total
					+ ")";
			// SOP("StrSql==Main Item==" + StrSql);
			stmttx.addBatch(StrSql);
			quote_netamt = Double.toString(Double.parseDouble(quote_netamt) + Double.parseDouble(item_price));
			quote_discamt = Double.toString(Double.parseDouble(quote_discamt) + Double.parseDouble(quoteitem_disc));
			quote_totaltax = Double.toString(Double.parseDouble(quote_totaltax) + Double.parseDouble(quoteitem_tax));
			quote_grandtotal = Double.toString(Double.parseDouble(quote_grandtotal) + Double.parseDouble(quoteitem_total));
			quote_exprice = Double.toString(Double.parseDouble(quote_exprice) + Double.parseDouble(quoteitem_total));
			String group_count = "0", gp_item_count = "0", tax = "0", quoteitem_option_group_tax = "0";
			String check = "", gp_name = "", addoff_gp_name = "";
			int addoff_item_count = 0;
			quoteitem_option_id = quoteitem_rowcount;
			for (int x = 1; x <= 2; x++) {
				if (x == 1) {
					group_count = beforetax_gp_count;
					tax = "bt";
					quoteitem_option_group_tax = "1";
				} else if (x == 2) {
					group_count = aftertax_gp_count;
					tax = "at";
					quoteitem_option_group_tax = "2";
				}
				if (!group_count.equals("0") && !group_count.equals("")) {
					for (int i = 1; i <= Integer.parseInt(group_count); i++) {

						gp_item_count = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_count"));
						// SOP("gp_item_count==" + gp_item_count);
						gp_name = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_gpname"));
						// SOP("gp_name==" + gp_name);
						for (int j = 1; j <= Integer.parseInt(gp_item_count); j++) {

							if (request.getParameter("chk_" + tax + "_" + i + "_" + j) != null) {
								check = PadQuotes(request.getParameter("chk_" + tax + "_" + i + "_" + j));
							} else {
								check = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_chk"));

							}
							if (check.equals("on")) {
								quoteitem_item_id = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_id")));
								quoteitem_option_group = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_option_group"));

								quoteitem_item_serial = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_serial"));
								String variable = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_variable"));
								if (variable.equals("1")) {
									quoteitem_price = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_price")));
								} else {
									quoteitem_price = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_baseprice")));
								}
								quoteitem_qty = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_qty")));
								quoteitem_disc = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_disc")));
								quoteitem_tax_id = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_tax_id")));
								quoteitem_tax_rate = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_tax_rate")));
								quoteitem_total = CNumeric(PadQuotes(request.getParameter("div_" + tax + "_" + i + "_" + j + "_amount")));
								quoteitem_tax = Double.toString((Double.parseDouble(quoteitem_price)
										- Double.parseDouble(quoteitem_disc))
										* Double.parseDouble(quoteitem_tax_rate)
										/ 100);
								quoteitem_total = String.valueOf(Double.parseDouble(quoteitem_total)
										- Double.parseDouble(quoteitem_disc)
										+ Double.parseDouble(quoteitem_tax));
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // To insert configured items
										+ " ("
										+ "quoteitem_quote_id,"
										+ " quoteitem_rowcount,"
										+ " quoteitem_item_id,"
										+ " quoteitem_option_id,"
										+ " quoteitem_option_group,"
										+ " quoteitem_option_group_tax,"
										+ " quoteitem_item_serial,"
										+ " quoteitem_qty,"
										+ " quoteitem_price,"
										+ " quoteitem_disc,"
										+ " quoteitem_tax,"
										+ " quoteitem_tax_id,"
										+ " quoteitem_tax_rate,"
										+ " quoteitem_total"
										+ ")"
										+ " VALUES"
										+ " ("
										+ quote_id + ","
										+ " 0," // quoteitem_rowcount
										+ " " + quoteitem_item_id + ","
										+ " " + quoteitem_option_id + ","
										+ " '" + quoteitem_option_group + "',"
										+ " '" + quoteitem_option_group_tax + "',"
										+ " '" + quoteitem_item_serial + "',"
										+ " " + quoteitem_qty + ","
										+ " " + quoteitem_price + ","
										+ " " + quoteitem_disc + ","
										+ " " + quoteitem_tax + ","
										+ " " + quoteitem_tax_id + ","
										+ " " + quoteitem_tax_rate + ","
										+ " " + quoteitem_total
										+ ")";
								// SOP("StrSql=sales quote configured items===" + StrSql);

								stmttx.addBatch(StrSql);
								quote_netamt = Double.toString(Double.parseDouble(quote_netamt) + Double.parseDouble(quoteitem_price));
								quote_discamt = Double.toString(Double.parseDouble(quote_discamt) + Double.parseDouble(quoteitem_disc));
								quote_totaltax = Double.toString(Double.parseDouble(quote_totaltax) + Double.parseDouble(quoteitem_tax));
								quote_grandtotal = Double.toString(Double.parseDouble(quote_grandtotal) + Double.parseDouble(quoteitem_total));
								if (quoteitem_option_group_tax.equals("1")) {
									quote_exprice = Double.toString(Double.parseDouble(quote_exprice) + Double.parseDouble(quoteitem_total));
								}
							}
						}
					}
				}
			}

			// to insert the additional offers into quote_item table
			addoff_gp_name = PadQuotes(request.getParameter("txt_addoff_at_gpname"));
			if (!addoff_gp_name.equals("")) {
				addoff_item_count = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("txt_addoff_at_item_count"))));
				for (int j = 1; j <= addoff_item_count; j++) {
					addoff_item_check = PadQuotes(request.getParameter("chk_addoff_at_" + j));
					quoteitem_item_id = CNumeric(PadQuotes(request.getParameter("txt_addoff_at_" + j + "_id")));
					quoteitem_item_serial = "";
					quoteitem_price = "0";
					quoteitem_qty = "1";
					quoteitem_disc = String.valueOf(CNumeric(PadQuotes(request.getParameter("txt_addoff_at_" + j + "_amt"))));
					quoteitem_tax_id = "0";
					quoteitem_tax_rate = "0";
					quoteitem_total = "-" + quoteitem_disc;
					// SOP("quoteitem_total=at=" + quoteitem_total);
					quoteitem_tax = "0";
					// SOP("quoteitem_item_id==" + quoteitem_item_id);
					if (addoff_item_check.equals("on")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // to insert the configured items
								+ " ("
								+ "	quoteitem_quote_id,"
								+ " quoteitem_rowcount,"
								+ " quoteitem_item_id,"
								+ " quoteitem_option_id,"
								+ " quoteitem_option_group,"
								+ " quoteitem_option_group_tax,"
								+ " quoteitem_item_serial,"
								+ " quoteitem_qty,"
								+ " quoteitem_price,"
								+ " quoteitem_disc,"
								+ " quoteitem_tax,"
								+ " quoteitem_tax_id,"
								+ " quoteitem_tax_rate,"
								+ " quoteitem_total"
								+ ")"
								+ " VALUES"
								+ " ("
								+ " " + quote_id + ","
								+ " 0," // quoteitem_rowcount
								+ " " + quoteitem_item_id + ","
								+ " " + quoteitem_option_id + ","
								+ " 'Additional Discounts'," // quoteitem_option_group
								+ " '2'," // quoteitem_option_group_tax
								+ " '" + quoteitem_item_serial + "',"
								+ " " + quoteitem_qty + ","
								+ " " + quoteitem_price + ","
								+ " " + quoteitem_disc + ","
								+ " " + quoteitem_tax + ","
								+ " " + quoteitem_tax_id + ","
								+ " " + quoteitem_tax_rate + ","
								+ " " + quoteitem_total
								+ ")";
						// SOP("StrSql==at config==" + StrSqlBreaker(StrSql));
						stmttx.addBatch(StrSql);
						quote_netamt = Double.toString(Double.parseDouble(quote_netamt) + Double.parseDouble(quoteitem_price));
						quote_discamt = Double.toString(Double.parseDouble(quote_discamt) + Double.parseDouble(quoteitem_disc));
						quote_totaltax = Double.toString(Double.parseDouble(quote_totaltax) + Double.parseDouble(quoteitem_tax));
						quote_grandtotal = Double.toString(Double.parseDouble(quote_grandtotal) + Double.parseDouble(quoteitem_total));
					}
				}
			}
			// to insert the additional offers into quote_item table
			// addoff_gp_name = PadQuotes(request.getParameter("txt_addoff_bt_gpname"));
			// SOP("addoff_gp_name===bt===" + addoff_gp_name);

			// to insert the additional offers into quote_item table
			// addoff_gp_name = PadQuotes(request.getParameter("txt_addoff_at_gpname"));

			stmttx.executeBatch();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("Connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_sales_quote.*,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " rateclass_id, contact_lname, contact_fname, title_id,"
					+ " title_desc, contact_mobile1," + " contact_phone1,"
					+ " contact_email1, customer_name, customer_id, contact_id, quote_preownedstock_id,"
					+ " quoteitem_item_id," + " quoteitem_price, quoteitem_disc,"
					+ " quoteitem_tax_id, quoteitem_tax2_id, quoteitem_tax3_id, quoteitem_tax4_id,"
					+ " quoteitem_tax_rate, quoteitem_tax2_rate, quoteitem_tax3_rate, quoteitem_tax4_rate,"
					+ " quote_inscomp_id, item_small_desc,"
					+ " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " enquiry_enquirytype_id, COALESCE(model_id, 0) AS model_id, enquiry_custtype_id,"
					+ " COALESCE(model_name, '') AS model_name, COALESCE(vehstock_comm_no, '') AS vehstock_comm_no"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
					+ " AND quoteitem_rowcount != 0"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = quote_vehstock_id"
					+ " WHERE quote_id = " + quote_id + "" + BranchAccess + "";

			if (emp_all_exe.equals("0")) {
				StrSql += ExeAccess.replace("emp_id", "quote_emp_id");
			}
			// SOP("StrSql==populate fields===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					quote_id = crs.getString("quote_id");
					branch_id = crs.getString("quote_branch_id");
					branch_name = crs.getString("branch_name");
					quote_refno = crs.getString("quote_refno");
					quote_auth = crs.getString("quote_auth");
					vehstock_comm_no = crs.getString("vehstock_comm_no");

					// option details
					quote_fin_option1 = crs.getString("quote_fin_option1");
					quote_fin_option2 = crs.getString("quote_fin_option2");
					quote_fin_option3 = crs.getString("quote_fin_option3");

					// financial details
					quote_fin_loan1 = crs.getString("quote_fin_loan1");
					quote_fin_loan2 = crs.getString("quote_fin_loan2");
					quote_fin_loan3 = crs.getString("quote_fin_loan3");

					quote_fin_tenure1 = crs.getString("quote_fin_tenure1");
					quote_fin_tenure2 = crs.getString("quote_fin_tenure2");
					quote_fin_tenure3 = crs.getString("quote_fin_tenure3");

					quote_fin_adv_emi1 = crs.getString("quote_fin_adv_emi1");
					quote_fin_adv_emi2 = crs.getString("quote_fin_adv_emi2");
					quote_fin_adv_emi3 = crs.getString("quote_fin_adv_emi3");

					quote_fin_emi1 = crs.getString("quote_fin_emi1");
					quote_fin_emi2 = crs.getString("quote_fin_emi2");
					quote_fin_emi3 = crs.getString("quote_fin_emi3");

					quote_fin_baloonemi1 = crs.getString("quote_fin_baloonemi1");
					quote_fin_baloonemi2 = crs.getString("quote_fin_baloonemi2");
					quote_fin_baloonemi3 = crs.getString("quote_fin_baloonemi3");

					quote_fin_fee1 = crs.getString("quote_fin_fee1");
					quote_fin_fee2 = crs.getString("quote_fin_fee2");
					quote_fin_fee3 = crs.getString("quote_fin_fee3");

					quote_fin_downpayment1 = crs.getString("quote_fin_downpayment1");
					quote_fin_downpayment2 = crs.getString("quote_fin_downpayment2");
					quote_fin_downpayment3 = crs.getString("quote_fin_downpayment3");

					quote_vehstock_id = crs.getString("quote_vehstock_id");
					quote_preownedstock_id = crs.getString("quote_preownedstock_id");
					// SOP("quote_preownedstock_id==Populate==" + quote_preownedstock_id);
					quote_contact_id = crs.getString("quote_contact_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id="
							+ crs.getString("contact_id") + "\">" + crs.getString("title_desc") + " "
							+ crs.getString("contact_fname") + " "
							+ crs.getString("contact_lname") + "</a>";
					rateclass_id = crs.getString("rateclass_id");
					model_id = crs.getString("model_id");
					model_name = crs.getString("model_name");
					item_id = crs.getString("quoteitem_item_id");
					item_name = crs.getString("item_name");
					item_price = crs.getString("quoteitem_price");
					item_netprice = Double.toString((crs.getDouble("quoteitem_price") * (crs.getDouble("quoteitem_tax_rate") / 100))
							+ (crs.getDouble("quoteitem_price") * (crs.getDouble("quoteitem_tax2_rate") / 100))
							// + (crs.getDouble("quoteitem_price") * (crs.getDouble("quoteitem_tax3_rate") / 100))
							+ (crs.getDouble("quoteitem_price") * (crs.getDouble("quoteitem_tax4_rate") / 100))
							+ crs.getDouble("quoteitem_price"));
					item_netdisc = Double.toString(crs.getDouble("quoteitem_disc"));
					item_tax1_id = crs.getString("quoteitem_tax_id");
					item_tax1_rate = crs.getString("quoteitem_tax_rate");
					item_tax2_id = crs.getString("quoteitem_tax2_id");
					item_tax2_rate = crs.getString("quoteitem_tax2_rate");
					item_tax3_id = crs.getString("quoteitem_tax3_id");
					item_tax3_rate = crs.getString("quoteitem_tax3_rate");
					item_tax4_id = crs.getString("quoteitem_tax4_id");
					item_tax4_rate = crs.getString("quoteitem_tax4_rate");
					item_small_desc = crs.getString("item_small_desc");

					item_netamount = Double.toString(((crs.getDouble("quoteitem_price") - crs.getDouble("quoteitem_disc")) * crs.getDouble("quoteitem_tax_rate") / 100)
							+ ((crs.getDouble("quoteitem_price") - crs.getDouble("quoteitem_disc")) * crs.getDouble("quoteitem_tax2_rate") / 100)
							// + ((crs.getDouble("quoteitem_price") - crs.getDouble("quoteitem_disc")) * crs.getDouble("quoteitem_tax3_rate") / 100)
							+ ((crs.getDouble("quoteitem_price") - crs.getDouble("quoteitem_disc")) * crs.getDouble("quoteitem_tax4_rate") / 100)
							+ (crs.getDouble("quoteitem_price")
							- crs.getDouble("quoteitem_disc")));
					quote_date = crs.getString("quote_date");
					quotedate = strToShortDate(quote_date);
					branch_name = crs.getString("branch_name");
					quote_refno = crs.getString("quote_refno");
					quote_auth = crs.getString("quote_auth");
					quote_vehstock_id = crs.getString("quote_vehstock_id");

					quote_desc = crs.getString("quote_desc");
					quote_terms = crs.getString("quote_terms");
					quote_netamt = crs.getString("quote_netamt");
					quote_totaltax = crs.getString("quote_totaltax");
					quote_grandtotal = crs.getString("quote_grandtotal");
					quote_enquiry_id = crs.getString("quote_enquiry_id");
					quote_inscomp_id = crs.getString("quote_inscomp_id");
					quote_emp_id = crs.getString("quote_emp_id");
					quote_active = crs.getString("quote_active");
					quote_notes = crs.getString("quote_notes");

					quote_entry_id = crs.getString("quote_entry_id");
					if (!quote_entry_id.equals("")) {
						quote_entry_by = Exename(comp_id, Integer.parseInt(quote_entry_id));
					}
					entry_date = strToLongDate(crs.getString("quote_entry_date"));
					quote_modified_id = crs.getString("quote_modified_id");
					if (!quote_modified_id.equals("")) {
						quote_modified_by = Exename(comp_id, Integer.parseInt(quote_modified_id));
					}
					modified_date = strToLongDate(crs.getString("quote_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Quote!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			CheckPageperm(response);
		}
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote"
						+ " SET" + " quote_branch_id = " + branch_id + ","
						+ " quote_customer_id = " + quote_customer_id + ","
						+ " quote_contact_id = " + quote_contact_id + ","
						+ "	quote_item_id = " + item_id + ","
						+ " quote_date = '" + quote_date + "',"
						+ " quote_netamt = " + quote_netamt + ","
						+ " quote_discamt = " + quote_discamt + ","
						+ " quote_totaltax = " + quote_totaltax + ","
						+ " quote_grandtotal = " + Double.parseDouble(quote_grandtotal) + ","
						+ " quote_fin_loan1 = '" + quote_fin_loan1 + "',"
						+ " quote_fin_loan2 = '" + quote_fin_loan2 + "',"
						+ " quote_fin_loan3 = '" + quote_fin_loan3 + "',"
						+ " quote_fin_tenure1 = '" + quote_fin_tenure1 + "',"
						+ " quote_fin_tenure2 = '" + quote_fin_tenure2 + "',"
						+ " quote_fin_tenure3 = '" + quote_fin_tenure3 + "',"
						+ " quote_fin_adv_emi1 = '" + quote_fin_adv_emi1 + "',"
						+ " quote_fin_adv_emi2 = '" + quote_fin_adv_emi2 + "',"
						+ " quote_fin_adv_emi3 = '" + quote_fin_adv_emi3 + "',"
						+ " quote_fin_emi1 = '" + quote_fin_emi1 + "',"
						+ " quote_fin_emi2 = '" + quote_fin_emi2 + "',"
						+ " quote_fin_emi3 = '" + quote_fin_emi3 + "',"
						+ " quote_fin_baloonemi1 = '" + quote_fin_baloonemi1 + "',"
						+ " quote_fin_baloonemi2 = '" + quote_fin_baloonemi2 + "',"
						+ " quote_fin_baloonemi3 = '" + quote_fin_baloonemi3 + "',"
						+ " quote_fin_fee1 = '" + quote_fin_fee1 + "',"
						+ " quote_fin_fee2 = '" + quote_fin_fee2 + "',"
						+ " quote_fin_fee3 = '" + quote_fin_fee3 + "',"
						+ " quote_fin_downpayment1 = '" + quote_fin_downpayment1 + "',"
						+ " quote_fin_downpayment2 = '" + quote_fin_downpayment2 + "',"
						+ " quote_fin_downpayment3 = '" + quote_fin_downpayment3 + "',"
						+ " quote_fin_option1 = '" + quote_fin_option1 + "',"
						+ " quote_fin_option2 = '" + quote_fin_option2 + "',"
						+ " quote_fin_option3 = '" + quote_fin_option3 + "',"
						+ " quote_terms = '" + quote_terms.replaceAll("&lt;p&gt;", "").replaceAll("&lt;/p&gt;", "") + "',"
						+ " quote_desc = '" + quote_desc + "',"
						+ " quote_inscomp_id = " + quote_inscomp_id + ","
						+ " quote_emp_id = " + quote_emp_id + ","
						+ " quote_vehstock_id = " + quote_vehstock_id + ",";
				if (config_sales_quote_refno.equals("1")) {
					StrSql += " quote_refno = '" + quote_refno + "',";
				}
				StrSql += " quote_auth = '" + quote_auth + "',"
						+ " quote_active = '" + quote_active + "',"
						+ " quote_notes = '" + quote_notes + "',"
						+ " quote_modified_id = " + quote_modified_id + ","
						+ " quote_modified_date = '" + quote_modified_date + "'"
						+ " WHERE quote_id = " + quote_id + " ";
				// SOP("StrSql===upuuuuupppppppppppppppf==" + StrSql);
				stmttx.execute(StrSql);
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " WHERE quoteitem_quote_id = " + quote_id + "";
				// SOP("StrSql===delete==" + StrSql);
				stmttx.execute(StrSql);

				AddItemFields(request);
				conntx.commit();
				// Get the total from Quote-Item table
				StrSql = "SELECT SUM(quoteitem_price) AS quote_netamt,"
						+ " SUM(quoteitem_disc) AS quote_discamt,"
						+ " SUM(quoteitem_tax) AS quote_totaltax,"
						+ " SUM(quoteitem_total) AS quote_grandtotal,"
						+ " (SELECT SUM(quoteitem_total)"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item item"
						+ " WHERE"
						+ " item.quoteitem_option_group_tax = 1"
						+ " AND item.quoteitem_quote_id = " + quote_id
						+ ") AS quote_exprice"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " WHERE quoteitem_quote_id = " + quote_id + "";

				// SOP("StrSql==Get Quote Amounts==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						quote_netamt = crs.getString("quote_netamt");
						quote_discamt = crs.getString("quote_discamt");
						quote_totaltax = crs.getString("quote_totaltax");
						quote_grandtotal = crs.getString("quote_grandtotal");
						quote_exprice = crs.getString("quote_exprice");
					}
					// For updating the Quote total fields
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote"
							+ " SET" + " quote_netamt = " + quote_netamt + ","
							+ " quote_discamt = " + quote_discamt + ","
							+ " quote_exprice = " + quote_exprice + ","
							+ " quote_totaltax = " + quote_totaltax + ","
							+ " quote_grandtotal = " + Double.parseDouble(quote_grandtotal) + ""
							+ " WHERE quote_id = " + quote_id + "";
					// SOP("StrSql==Update Quote Amounts==" + StrSql);
					stmttx.execute(StrSql);
					// Update sales order if sales order is raised for the quote
					StrSql = "SELECT" + " so_quote_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " WHERE so_quote_id = " + quote_id + ""
							+ " AND so_active = 1";
					// SOP("StrSql==so_quote_id==" + StrSql);

					if (!ExecuteQuery(StrSql).equals("")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
								+ " SET" + " so_branch_id = " + branch_id + ","
								+ " so_customer_id = " + quote_customer_id + ","
								+ " so_contact_id = " + quote_contact_id + ","
								+ " so_item_id = " + item_id + ","
								+ " so_date = '" + quote_date + "',"
								+ " so_netamt = '" + quote_netamt + "',"
								+ " so_discamt = '" + quote_discamt + "',"
								+ " so_totaltax = '" + quote_totaltax + "',"
								+ " so_grandtotal = " + Double.parseDouble(quote_grandtotal) + ""
								// + " so_emp_id  = " + quote_emp_id
								+ " WHERE so_quote_id = " + quote_id + "";
						// SOP("StrSql-------update SO-" +
						// StrSqlBreaker(StrSql));
						stmttx.execute(StrSql);
						// Update Additional Offers item amounts to axela_sales_so table

						StrSql = "SELECT quoteitem_item_id, item_name, quoteitem_disc"
								+ " FROM " + compdb(comp_id) + "axela_sales_quote"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id AND item_id IN (2461, 2462, 2463)"
								+ " WHERE"
								+ " 1 = 1"
								+ " AND quote_id = " + quote_id;
						CachedRowSet crs2 = processQuery(StrSql, 0);
						if (crs2.isBeforeFirst()) {
							while (crs2.next()) {
								String item_name = crs2.getString("item_name");
								String quoteitem_disc = crs2.getString("quoteitem_disc");
								if (item_name.equals("Exchange / Loyalty Bonus")) { // Update Exchange/ Loyalty Bonus to axela_sales_so
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET so_offer_exchange_bonus = " + quoteitem_disc
											+ " WHERE so_quote_id = " + quote_id;
									stmttx.execute(StrSql);
								}
								if (item_name.equals("Corporate / Any")) { // Update Corporate/Any to axela_sales_so
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET so_offer_corporate = " + quoteitem_disc
											+ " WHERE so_quote_id = " + quote_id;
									stmttx.execute(StrSql);
								}
								if (item_name.equals("Special Schemes")) { // Update Special Schemes to axela_sales_so
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
											+ " SET so_offer_spcl_scheme = " + quoteitem_disc
											+ " WHERE so_quote_id = " + quote_id;
									stmttx.execute(StrSql);
								}
							}
						}

					}
					// Delete so_items
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_so_item"
							+ " WHERE soitem_so_id = (SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
							+ " WHERE so_quote_id = " + quote_id + " AND so_active = 1)";
					// SOP("StrSql==Delete So Items==" + StrSql);
					stmttx.execute(StrSql);
					//
					StrSql = "SELECT so_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " WHERE so_quote_id = " + quote_id;

					// SOP("StrSql==so_id==" + StrSql);
					CachedRowSet crs1 = processQuery(StrSql, 0);
					while (crs1.next()) {
						so_id = crs1.getString("so_id");
						// Insert so_items from quote_items
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_item"
								+ " ("
								+ " soitem_so_id,"
								+ " soitem_rowcount,"
								+ " soitem_item_id,"
								+ " soitem_option_id,"
								+ " soitem_option_group,"
								+ " soitem_option_group_tax,"
								+ " soitem_item_serial,"
								+ " soitem_qty,"
								+ " soitem_price,"
								+ " soitem_disc,"
								+ " soitem_tax,"
								+ " soitem_tax_id,"
								+ " soitem_tax_rate,"
								+ " soitem_total"
								+ " )"
								+ " SELECT"
								+ " " + crs1.getString("so_id") + "," // soitem_so_id
								+ " quoteitem_rowcount,"
								+ " quoteitem_item_id,"
								+ " quoteitem_option_id,"
								+ " quoteitem_option_group,"
								+ " quoteitem_option_group_tax,"
								+ " quoteitem_item_serial,"
								+ " quoteitem_qty,"
								+ " quoteitem_price,"
								+ " quoteitem_disc,"
								+ " quoteitem_tax,"
								+ " quoteitem_tax_id,"
								+ " quoteitem_tax_rate,"
								+ " quoteitem_total"
								+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
								+ " WHERE quoteitem_quote_id = " + quote_id;
						// SOP("StrSql==Update So_items==" + StrSql);
						stmttx.execute(StrSql);
					}
					crs1.close();

					conntx.commit();

					// Update Profitabilty fields of Sales Order
					if (!so_id.equals("0")) {
						new Veh_Salesorder_Update().UpdateProfitability(so_id, comp_id);
					}
					// Update Sales Invoice if it is present
					StrSql = "SELECT voucher_id"
							+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
							+ "	WHERE 1 = 1"
							+ " AND voucher_vouchertype_id = 6"
							+ " AND voucher_so_id = " + so_id;
					// SOP("StrSql==" + StrSql);
					voucher_id = CNumeric(ExecuteQuery(StrSql));
					// if (!voucher_id.equals("0")) {
					// CopySalesItemToCart(request, emp_id, session_id, so_id, voucher_vouchertype_id, "");
					// AddSalesInvoice();
					// }
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}
	protected void DeleteFields(HttpServletResponse response) throws Exception {
		CheckPageperm(null);
		StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
				+ " WHERE so_quote_id = " + quote_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Quote is associated with Sales Order!";
		}

		// StrSql = "SELECT invoice_id FROM " + compdb(comp_id) +
		// "axela_invoice"
		// + " WHERE invoice_quote_id = " + quote_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg += "<br>Quote is associated with Invoice!";
		// }
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " where quoteitem_quote_id = " + quote_id + "";
				stmttx.execute(StrSql);

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote"
						+ " WHERE quote_id = " + quote_id + "";
				stmttx.execute(StrSql);
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public void AddSalesInvoice() {
		try {
			StrSql = "SELECT vehstocklocation_id,branch_rateclass_id"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = vehstocklocation_branch_id"
					+ " WHERE branch_id = " + branch_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehstocklocation_id = CNumeric(PadQuotes(crs.getString("vehstocklocation_id")));
					invoice_rateclass_id = CNumeric(PadQuotes(crs.getString("branch_rateclass_id")));
				}
			}
			StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
					+ " SET voucher_branch_id = " + branch_id + ","
					+ " voucher_location_id = " + vehstocklocation_id + ","
					+ " voucher_rateclass_id = " + invoice_rateclass_id + ","
					+ " voucher_customer_id = " + quote_customer_id + ","
					+ " voucher_contact_id = " + quote_contact_id + ","
					+ " voucher_billing_add = '" + customer_address + "',"
					+ " voucher_consignee_add = '" + customer_address + "',"
					+ " voucher_amount = '" + Double.parseDouble(quote_grandtotal) + "',"
					+ " voucher_emp_id  = " + quote_emp_id + ""
					+ " WHERE voucher_quote_id = " + quote_id + "";
			// SOP("StrSql-------update SO-" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql);
			AddSalesInvoiceItems();
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1" + " AND cart_emp_id = " + emp_id + ""
					+ " AND cart_session_id = " + session_id + ""
					+ " AND cart_vouchertype_id = " + voucher_vouchertype_id + "";
			stmttx.addBatch(StrSql);
			stmttx.executeBatch();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void AddSalesInvoiceItems() {
		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " WHERE vouchertrans_voucher_id = " + voucher_id + "";
			stmttx.addBatch(StrSql);
			StrSql = "SELECT" + " " + voucher_id + ","
					+ " cart_multivoucher_id," + " cart_customer_id," + " " + vehstocklocation_id + ",";
			StrSql += " cart_item_id," + " cart_discount," + " cart_discount_perc,"
					+ " cart_tax," + " cart_tax_id," + " cart_rowcount, "
					+ " cart_option_id, " + " cart_price," + " cart_netprice,"
					+ " cart_delivery_date," + " cart_convfactor," + " cart_qty,"
					+ " cart_truckspace," + " cart_unit_cost," + " cart_amount,"
					+ " cart_discountamount," + " cart_taxamount," + " cart_alt_qty,"
					+ " cart_alt_uom_id," + " cart_time," + " cart_dc"
					+ " FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1"
					+ " AND cart_vouchertype_id = " + voucher_vouchertype_id + ""
					+ " AND cart_emp_id = " + emp_id + ""
					+ " AND cart_session_id = " + session_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ "	vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_delivery_date,"
					+ " vouchertrans_convfactor,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_truckspace,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_discountamount,"
					+ " vouchertrans_taxamount,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)" + " " + StrSql + "";
			// SOP("StrSql==cart--vouchertrans==== " + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);
			// party entry
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher_trans"
					+ " (vouchertrans_voucher_id,"
					+ " vouchertrans_multivoucher_id,"
					+ " vouchertrans_customer_id,"
					+ "	vouchertrans_location_id,"
					+ " vouchertrans_item_id,"
					+ " vouchertrans_discount,"
					+ " vouchertrans_discount_perc,"
					+ " vouchertrans_tax,"
					+ " vouchertrans_tax_id,"
					+ " vouchertrans_rowcount,"
					+ " vouchertrans_option_id,"
					+ " vouchertrans_price,"
					+ " vouchertrans_netprice,"
					+ " vouchertrans_convfactor,"
					+ " vouchertrans_qty,"
					+ " vouchertrans_unit_cost,"
					+ " vouchertrans_amount,"
					+ " vouchertrans_alt_qty,"
					+ " vouchertrans_alt_uom_id,"
					+ " vouchertrans_time,"
					+ " vouchertrans_dc)"
					+ " VALUES ("
					+ " " + voucher_id + ","
					+ " 0," + " " // vouchertrans_multivoucher_id
					+ quote_customer_id + ","
					+ "	" + vehstocklocation_id + ","
					+ " 0, " // vouchertrans_item_id
					+ " 0, " // vouchertrans_discount
					+ " 0, " // vouchertrans_discount_perc
					+ " 0, " // vouchertrans_tax
					+ " 0, " // vouchertrans_tax_id
					+ " 0, " // vouchertrans_rowcount
					+ " 0, " // vouchertrans_option_id
					+ " 0, " // vouchertrans_price
					+ "	0, " // vouchertrans_netprice
					+ " 0," // vouchertrans_convfactor
					+ " 0, " // vouchertrans_qty
					+ " 0," // vouchertrans_unit_cost
					+ Double.parseDouble(quote_grandtotal) + ","
					+ " 0," // vouchertrans_alt_qty
					+ " 0," // vouchertrans_alt_uom_id
					+ "'" + ToLongDate(kknow()) + "',";
			// for sales return
			if (voucher_vouchertype_id.equals("23")) {
				StrSql += " '0'" // vouchertrans_dc
						+ " )";
				// for so and sales invoice
			} else if (voucher_vouchertype_id.equals("6") || voucher_vouchertype_id.equals("5") || voucher_vouchertype_id.equals("25")) {
				StrSql += " '1'" // vouchertrans_dc
						+ " )";

			}
			// SOP("StrSql==sup==" + StrSqlBreaker(StrSql));

			stmttx.addBatch(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	void CheckPageperm(HttpServletResponse response) {
		try {
			StrSql = "SELECT quote_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " WHERE quote_id = " + quote_id + BranchAccess + "";
			if (ExecuteQuery(StrSql).equals("")) {
				msg = "Access denied!";
				response.sendRedirect("../portal/error.jsp?msg=" + msg + "");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String GetConfigurationDetails(HttpServletRequest request, String quote_id,
			String item_id, String branch_id, String vehstock_id, String vehstock_comm_no,
			String emp_quote_discountupdate, String quote_date, String enquiry_id, ArrayList<Map> configuredItemList, ArrayList<Map> newconfiguredbtItemList, String comp_id) {

		StringBuilder Str = new StringBuilder();
		StringBuilder Str1 = new StringBuilder();
		String tempGroup = "";
		double so_total_disc = 0.00;
		String add_disc_readOnly = "";
		String preowned = "", preownedprice = "";
		double price_amount;
		CachedRowSet crs = null;
		int groupitemcount = 0, groupcount = 0, totalBtGroupCount = 0, totalAtGroupCount = 0;
		try {
			// SOP("configuredItemList==full data==" + gson.toJson(configuredItemList));
			if (configuredItemList == null) {
				configuredItemList = new ArrayList<Map>();
			}
			if (newconfiguredbtItemList == null) {
				newconfiguredbtItemList = new ArrayList<Map>();
			}
			if (configuredItemList != null) {
				// This logic will add all the groups in the LinkedHashSet, to have unique values as well as to be in the order.
				for (Map groups : configuredItemList) {
					if (groups.containsKey("group_name") && groups.containsValue("groupSet")) {
						Iterator iterator = groups.keySet().iterator();
						for (int i = 1; i < groups.size(); i++) {
							groupSet.add(String.valueOf(groups.get("group_" + i)));
						}
					}
					if (groups.containsKey("group_name") && groups.containsValue("totalBtItemDetails")) {
						Iterator iterator = groups.keySet().iterator();
						while (iterator.hasNext()) {
							tempGroup = String.valueOf(iterator.next());
							totalBtItemDetails.put(tempGroup, String.valueOf(groups.get(tempGroup)));
							if (tempGroup.equals("bt_group_count")) {
								totalBtGroupCount = Integer.parseInt(String.valueOf(groups.get(tempGroup)));
							}

						}

					}
					if (groups.containsKey("group_name") && groups.containsValue("totalAtItemDetails")) {
						Iterator iterator = groups.keySet().iterator();
						while (iterator.hasNext()) {
							tempGroup = String.valueOf(iterator.next());
							totalAtItemDetails.put(tempGroup, String.valueOf(groups.get(tempGroup)));
							if (tempGroup.equals("at_group_count")) {
								totalAtGroupCount = Integer.parseInt(String.valueOf(groups.get(tempGroup)));
							}
						}

					}
					if (groups.containsKey("group_name") && groups.containsValue("groupItemCount")) {
						Iterator iterator = groups.keySet().iterator();
						while (iterator.hasNext()) {
							tempGroup = String.valueOf(iterator.next());
							groupItemCount.put(tempGroup, String.valueOf(groups.get(tempGroup)));
						}
					}
				}
			}

			if (!quote_enquiry_id.equals("0")) {
				quote_customer_id = CNumeric(ExecuteQuery("SELECT enquiry_customer_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + quote_enquiry_id));
				gsttype = GetGstType(quote_customer_id, branch_id, comp_id);
			} else if (!enquiry_id.equals("0")) {
				quote_customer_id = CNumeric(ExecuteQuery("SELECT enquiry_customer_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id));
				gsttype = GetGstType(quote_customer_id, branch_id, comp_id);
			}

			if (!item_id.equals("0")) {
				// For Pre-Owned Car
				if (enquiry_enquirytype_id.equals("2") && !quote_preownedstock_id.equals("0")) {
					StrSql = "SELECT preownedstock_selling_price, variant_name,"
							+ " preownedmodel_name, preowned_sub_variant"
							+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
							+ "	INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
							+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
							+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
							+ " WHERE preownedstock_id = " + quote_preownedstock_id + "";
					// SOP("StrSql==" + StrSql);
					crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							preownedprice = crs.getString("preownedstock_selling_price");
							preowned = crs.getString("preownedmodel_name") + " - " + crs.getString("variant_name");
							// SOP("preowned==" + preowned);
							if (!crs.getString("preowned_sub_variant").equals("")) {
								preowned += "<br>" + crs.getString("preowned_sub_variant");
							}
						}
					}
					crs.close();
				}

				// This checks whether Main Groups is already added in the Map or not, if it's added then it won't add again.
				containsMap = false;
				if (configuredItemList != null) {
					for (Map configuredItem : configuredItemList) {
						if (configuredItem.containsKey("group_name") && configuredItem.containsValue("mainItemDetails")) {
							containsMap = true;
							break;
						}
					}
				}

				if (containsMap == false) {
					StrSql = "SELECT item_code, item_name, price_amt, item_small_desc, price_disc,";
					if (gsttype.equals("state")) {
						StrSql += " COALESCE(sgst.customer_id, 0) AS tax1_id,"
								+ " COALESCE(sgst.customer_rate, 0) AS tax1_rate,"
								+ "	COALESCE(cgst.customer_id, 0) AS tax2_id,"
								+ "	COALESCE(cgst.customer_rate, 0) AS tax2_rate,";
					} else if (gsttype.equals("central")) {
						StrSql += " COALESCE(igst.customer_id, 0) AS tax3_id,"
								+ " COALESCE(igst.customer_rate, 0) AS tax3_rate,";
					}

					StrSql += " COALESCE(cess.customer_id, 0) AS tax4_id,"
							+ "	COALESCE(cess.customer_rate, 0) AS tax4_rate,"
							+ " COALESCE(quoteitem_disc, 0) AS quoteitem_disc,"
							+ " COALESCE(quoteitem_id, 0) AS quoteitem_id,"
							+ "	model_id,model_name"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_id = (SELECT recent_price.price_id"
							+ " FROM " + compdb(comp_id) + " axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = " + item_id + ""
							+ " AND recent_price.price_rateclass_id	 = branch_rateclass_id"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1)"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_item_id = item_id"
							+ " AND quoteitem_rowcount != 0"
							+ " AND quoteitem_quote_id = " + quote_id;
					if (gsttype.equals("state")) {
						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer sgst ON sgst.customer_id = item_salestax1_ledger_id"
								+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cgst ON cgst.customer_id = item_salestax2_ledger_id";
					} else if (gsttype.equals("central")) {
						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer igst ON igst.customer_id = item_salestax3_ledger_id";
					}
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer cess ON cess.customer_id = item_salestax4_ledger_id";

					StrSql += " WHERE item_id = " + CNumeric(item_id) + ""
							+ " LIMIT 1";
					// SOP("StrSql===quote==main item==" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							model_id = crs.getString("model_id");
							model_name = crs.getString("model_name");
							item_name = crs.getString("item_name");
							item_small_desc = crs.getString("item_small_desc");
							item_small_desc = crs.getString("item_small_desc");
							if (enquiry_enquirytype_id.equals("2")) {
								item_small_desc = preowned;
								item_price = preownedprice;
							} else {
								item_price = crs.getString("price_amt");
								// SOP("item_price==="+item_price);
							}
							price_amount = Double.parseDouble(df.parse(item_price) + "");
							if (gsttype.equals("state")) {
								item_netprice = new BigDecimal((price_amount * crs.getDouble("tax1_rate") / 100) + price_amount).toString();
								item_netprice = new BigDecimal((price_amount * crs.getDouble("tax2_rate") / 100) + Double.parseDouble(item_netprice)).toString();
								item_tax1_id = crs.getString("tax1_id");
								item_tax1_rate = crs.getString("tax1_rate");
								item_tax2_id = crs.getString("tax2_id");
								item_tax2_rate = crs.getString("tax2_rate");
							} else if (gsttype.equals("central")) {
								item_netprice = new BigDecimal((price_amount * crs.getDouble("tax3_rate") / 100) + price_amount).toString();
								item_tax3_id = crs.getString("tax3_id");
								item_tax3_rate = crs.getString("tax3_rate");
							}
							item_netprice = new BigDecimal((price_amount * crs.getDouble("tax4_rate") / 100) + Double.parseDouble(item_netprice)).toString();
							item_tax4_id = crs.getString("tax4_id");
							item_tax4_rate = crs.getString("tax4_rate");
							if (!crs.getString("quoteitem_id").equals("0")) {
								item_netdisc = crs.getString("quoteitem_disc");
							} else {
								item_netdisc = Double.toString(crs.getDouble("price_disc"));
							}
							if (!PadQuotes(request.getParameter("txt_item_disc")).equals("")) {
								item_netdisc = CNumeric(PadQuotes(request.getParameter("txt_item_disc")));
							}
							if (!PadQuotes(request.getParameter("div_main_item_amount")).equals("")) {
								item_netamount = CNumeric(PadQuotes(request.getParameter("div_main_item_amount")));
							}
							if (!PadQuotes(request.getParameter("div_main_item_amountwod")).equals("")) {
								item_netamountwod = CNumeric(PadQuotes(request.getParameter("div_main_item_amountwod")));
							}
							so_total_disc = Double.parseDouble(item_netdisc);
							if (gsttype.equals("state")) {
								item_tax1 = new BigDecimal((price_amount - so_total_disc) * crs.getDouble("tax1_rate") / 100).toString();
								item_tax2 = new BigDecimal((price_amount - so_total_disc) * crs.getDouble("tax2_rate") / 100).toString();
							} else if (gsttype.equals("central")) {
								item_tax3 = new BigDecimal((price_amount - so_total_disc) * crs.getDouble("tax3_rate") / 100).toString();
							}
							item_tax4 = new BigDecimal((price_amount - so_total_disc) * crs.getDouble("tax4_rate") / 100).toString();
							if (gsttype.equals("state")) {
								item_netamount = new BigDecimal(((price_amount - so_total_disc) * (crs.getDouble("tax1_rate") / 100))).toString();
								item_netamount = new BigDecimal(((price_amount - so_total_disc) * (crs.getDouble("tax2_rate") / 100)) + Double.parseDouble(item_netamount)).toString();

								item_netamountwod = new BigDecimal((price_amount * (crs.getDouble("tax1_rate") / 100))).toString();
								item_netamountwod = new BigDecimal((price_amount * (crs.getDouble("tax2_rate") / 100)) + Double.parseDouble(item_netamountwod)).toString();

							} else if (gsttype.equals("central")) {
								item_netamount = new BigDecimal(((price_amount - so_total_disc) * (crs.getDouble("tax3_rate") / 100))).toString();
								item_netamountwod = new BigDecimal((price_amount * (crs.getDouble("tax3_rate") / 100))).toString();
							}
							item_netamount = new BigDecimal(((price_amount - so_total_disc) * (crs.getDouble("tax4_rate") / 100)) + (price_amount - so_total_disc)
									+ Double.parseDouble(item_netamount))
									.toString();
							item_netamountwod = new BigDecimal((price_amount * crs.getDouble("tax4_rate") / 100) + price_amount + Double.parseDouble(item_netamountwod)).toString();

							mainItemDetails.put("group_name", "mainItemDetails");
							mainItemDetails.put("model_id", model_id);
							mainItemDetails.put("item_id", item_id);
							mainItemDetails.put("item_code", crs.getString("item_code"));
							mainItemDetails.put("item_name", unescapehtml(item_name));
							mainItemDetails.put("item_price", item_price);

							if (gsttype.equals("state")) {
								mainItemDetails.put("item_tax1_id", item_tax1_id);
								mainItemDetails.put("item_tax1_rate", item_tax1_rate);
								mainItemDetails.put("item_tax2_id", item_tax2_id);
								mainItemDetails.put("item_tax2_rate", item_tax2_rate);
							} else if (gsttype.equals("central")) {
								mainItemDetails.put("item_tax3_id", item_tax3_id);
								mainItemDetails.put("item_tax3_rate", item_tax3_rate);
							}
							mainItemDetails.put("item_tax4_id", item_tax4_id);
							mainItemDetails.put("item_tax4_rate", item_tax4_rate);
							mainItemDetails.put("item_netprice", df.format(Double.parseDouble(item_netprice)));
							mainItemDetails.put("item_netdisc", df.format(Double.parseDouble(item_netdisc)));
							mainItemDetails.put("item_netamount", df.format(Double.parseDouble(item_netamount)));
							mainItemDetails.put("item_netamountwod", df.format(Double.parseDouble(item_netamountwod)));
						}
						configuredItemList.add(mainItemDetails);

						groupItemCount.put("group_name", "groupItemCount");
						configuredItemList.add(groupItemCount);
					}
					// SOP("configuredItemList==after main item==" + configuredItemList);
					crs.close();
				}
				// Start Before Tax
				String group = "", checked = "", disabled = "";
				int addoffer_bt_item_count = 0;
				int addoffer_at_item_count = 0;
				String addoff_bt_check = "";
				String addoff_at_check = "";
				Double all_selected_total = 0.00, multi_select_basetotal = 0.00;
				String disc_amt = "";
				String vehstock_group = "0";
				Double before_tax_totalwod = Double.parseDouble(CNumeric(item_netamountwod)), amountwod = 0.00;
				Double amount = 0.00, before_tax_total = 0.00, after_tax_total = 0.00;

				if (configuredItemList != null) {

					for (Map groups : configuredItemList) {
						if (groups.containsKey("group_name") && groups.containsValue("itemTotals")) {
							Iterator iterator = groups.keySet().iterator();
							while (iterator.hasNext()) {
								tempGroup = String.valueOf(iterator.next());
								if (tempGroup.equals("before_tax_total")) {
									before_tax_total = Double.parseDouble(String.valueOf(groups.get(tempGroup)));
								}
								if (tempGroup.equals("before_tax_totalwod")) {
									before_tax_totalwod = Double.parseDouble(String.valueOf(groups.get(tempGroup)));
								}
								if (tempGroup.equals("after_tax_total")) {
									after_tax_total = Double.parseDouble(String.valueOf(groups.get(tempGroup)));
								}
								if (tempGroup.equals("so_total_disc")) {
									so_total_disc = Double.parseDouble(String.valueOf(groups.get(tempGroup)));
								}
							}
						}
					}
				}

				// This checks whether before tax Groups are already added in the Map or not, if it's added then it won't add again.
				containsMap = false;
				for (Map configuredItem : configuredItemList) {
					if (configuredItem.containsKey("bt_item_name")) {
						containsMap = true;
						break;
					}
				}
				if (containsMap == false) {
					// Start Before Tax
					before_tax_totalwod = Double.parseDouble(CNumeric(item_netamountwod));
					before_tax_total = Double.parseDouble(CNumeric(item_netamount));
					if (!PadQuotes(request.getParameter("txt_item_disc")).equals("")) {
						before_tax_total = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("div_main_item_amount"))));
						before_tax_totalwod = Double.parseDouble(CNumeric(PadQuotes(request.getParameter("div_main_item_amountwod"))));
					}

					StrSql = "SELECT item_id, item_code, item_name,"
							+ " IF ( mainitemoption.option_select = 1 OR COALESCE(trans_option_id, 0) != 0 OR COALESCE(quoteitem_id, 0) != 0, 'checked', '' ) AS item_check,"
							+ " IF ( COALESCE (trans_option_id, 0) != 0 AND trans_vehstock_id IS NOT NULL AND trans_vehstock_id = " + vehstock_id + ", 'disabled', '' ) AS item_disabled,"
							+ " group_type, group_name, COALESCE(pricetrans_amt, 0) AS pricetrans_amt,"
							+ " mainitemoption.option_group_id, mainitemoption.option_qty, item_code, mainitemoption.option_id, mainitemoption.option_select, item_small_desc,"
							+ " COALESCE(customer_rate, 0) AS tax_rate, COALESCE(customer_id, 0) AS tax_id,"
							+ " (SELECT COUNT(DISTINCT itemoption.option_id)"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_option itemoption"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = itemoption.option_item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch price_branch ON price_branch.branch_id = " + branch_id + ""
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price price ON price.price_id = (SELECT recent_price.price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = itemoption.option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id	 = price_branch.branch_rateclass_id	"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1)"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " WHERE itemoption.option_itemmaster_id = mainitemoption.option_itemmaster_id"
							+ " AND itemoption.option_group_id = group_id"
							+ " GROUP BY option_group_id) AS groupitemcount,"
							+ " COALESCE(quoteitem_option_id, 0) AS quoteitem_option_id,"
							+ " COALESCE(quoteitem_option_group, '') AS quoteitem_option_group,"
							+ " item_aftertaxcal, item_serial, group_id,"
							+ " COALESCE(quoteitem_price, '') AS quoteitem_price,"
							+ " COALESCE(quoteitem_qty, '') AS quoteitem_qty,"
							+ " COALESCE(quoteitem_disc, '0.00') AS quoteitem_disc,"
							+ " COALESCE(quoteitem_tax_rate, '') AS quoteitem_tax_rate,"
							+ " COALESCE(quoteitem_tax_id, '') AS quoteitem_tax_id,"
							+ " COALESCE(quoteitem_total, '') AS quoteitem_total,"
							+ " group_aftertax"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_option mainitemoption"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option vehoption ON vehoption.option_code = item_code"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = vehoption.option_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_id = (SELECT recent_price.price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id	 = branch_rateclass_id"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1)"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax3_ledger_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_item_id = item_id AND quoteitem_quote_id = " + quote_id + ""
							+ " WHERE option_itemmaster_id = " + item_id + "";
					if (!vehstock_id.equals("0")) {
						StrSql += " AND ( mainitemoption.option_select = 1 OR trans_vehstock_id = " + vehstock_id + " OR quoteitem_quote_id = " + quote_id + ")";
					}
					StrSql += "	AND item_id = item_id"
							+ " AND group_active = 1"
							+ " AND group_aftertax = 0"
							+ " GROUP BY group_name, group_type, item_id"
							+ " ORDER BY group_rank, group_name DESC, item_id";
					// SOP("StrSql====bt==" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);

					if (crs.isBeforeFirst()) {
						while (crs.next()) {

							configuredbtItem = new HashMap<String, String>();
							Double item_tax_value, sub_item_price;
							int sub_item_tax_id, item_qty = 0;
							double item_disc;
							String group_name, option_id, default_select;
							if (!crs.getString("quoteitem_disc").equals("")) {
								item_disc = (int) crs.getDouble("quoteitem_disc");
							} else {
								item_disc = 0;
							}
							sub_item_price = (crs.getDouble("pricetrans_amt") * crs.getDouble("tax_rate") / 100) + crs.getDouble("pricetrans_amt");
							item_qty = crs.getInt("option_qty");
							item_tax_value = crs.getDouble("tax_rate");
							group_name = crs.getString("group_name");
							sub_item_tax_id = crs.getInt("tax_id");
							option_id = crs.getString("option_id");
							amount = item_qty * (((crs.getDouble("pricetrans_amt") - item_disc) * item_tax_value / 100) + (crs.getDouble("pricetrans_amt") - item_disc));
							amountwod = item_qty * ((crs.getDouble("pricetrans_amt") * item_tax_value / 100) + crs.getDouble("pricetrans_amt"));

							checked = "";
							disabled = "";
							groupName = "";

							disabled = crs.getString("item_disabled");

							if (!group.equals(group_name) && !group.equals("")) {
								groupitemcount = 0;
							}

							if (!group.equals(group_name)) {
								if (!group_name.equals("Additional Discounts")) {
									groupcount++;
									totalBtGroupCount++;
									if (crs.getString("group_type").equals("1")) {
										groupName = group_name + " (Default)";
									} else {
										groupName = group_name;
									}
								}
							}
							groupitemcount++;
							if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1")) {
								multi_select_basetotal += amount;
							}
							group = group_name;
							so_total_disc += item_disc;
							before_tax_total += amount;
							before_tax_totalwod += amountwod;

							groupSet.add("bt_" + group_name);
							configuredbtItem.put("group_name", group_name);
							configuredbtItem.put("group", groupName);
							configuredbtItem.put("bt_group_id", crs.getString("group_id"));
							configuredbtItem.put("bt_item_check", crs.getString("item_check"));
							if (crs.getString("item_check").equals("checked")) {
								configuredbtItem.put("bt_check", "on");
							} else {
								configuredbtItem.put("bt_check", "off");
							}

							configuredbtItem.put("bt_item_disabled", disabled);
							configuredbtItem.put("bt_item_code", unescapehtml(crs.getString("item_code")));
							configuredbtItem.put("bt_item_name", unescapehtml(crs.getString("item_name")));
							configuredbtItem.put("bt_groupcount", String.valueOf(groupcount));
							configuredbtItem.put("bt_groupitemcount", String.valueOf(groupitemcount));
							configuredbtItem.put("bt_group_aftertax", crs.getString("group_aftertax"));
							configuredbtItem.put("bt_item_id", crs.getString("item_id"));
							configuredbtItem.put("bt_item_qty", String.valueOf(item_qty));
							configuredbtItem.put("bt_tax_id", String.valueOf(sub_item_tax_id));
							configuredbtItem.put("bt_tax_rate", df.format(item_tax_value));
							configuredbtItem.put("bt_option_id", String.valueOf(option_id));
							configuredbtItem.put("bt_amount", df.format(amount));
							configuredbtItem.put("bt_amountwod", df.format(amountwod));
							configuredbtItem.put("bt_netprice", df.format(sub_item_price));
							configuredbtItem.put("bt_baseprice", df.format(crs.getDouble("pricetrans_amt")));
							configuredbtItem.put("bt_basedisc", df.format(item_disc));
							configuredItemList.add(configuredbtItem);

							// This logic will get the particular group count from the Map and increments it by 1.
							String isGroupCountAdded = "no";
							for (Map m : configuredItemList) {
								if (m.containsKey("group_name") && m.containsValue("groupItemCount")) {
									if (m.containsKey(group_name)) {
										isGroupCountAdded = "yes";
										m.put(group_name, Integer.parseInt(String.valueOf(m.get(group_name))) + 1);
										break;
									}
								}
							}
							if (isGroupCountAdded.equals("no")) {
								for (Map m : configuredItemList) {
									if (m.containsKey("group_name") && m.containsValue("groupItemCount")) {
										m.put(group_name, String.valueOf(groupitemcount));
										isGroupCountAdded = "no";
										break;
									}
								}
							}
						}
					}
					crs.close();
				}
				// jsonconfiguredItemList = gson.toJson(configuredItemList);
				// SOP("configuredItemList==after configured bt==" + configuredItemList);

				// This logic checks whether the newly Item already present or not.
				if (newconfiguredbtItemList != null) {
					for (Map newConfiguredbtItem : newconfiguredbtItemList) {
						for (Map configureItem : configuredItemList) {
							if (configureItem.get("group_name").equals(newConfiguredbtItem.get("bt_group_name"))) {
								if (configureItem.containsKey("bt_item_name")) {
									if (newConfiguredbtItem.containsKey("bt_item_name")) {
										if (configureItem.get("bt_item_name").equals(newConfiguredbtItem.get("bt_item_name"))) {
											errormsg = "<br>Item is already added!";
											addItemToList = "no";
										}
									}
								}
							}
						}

						// This logic will add the Item in the itemids variable if that Item is not added.
						if (!addItemToList.equals("no")) {
							for (Map configureItem : configuredItemList) {
								if (newConfiguredbtItem.get("bt_groupadded").equals("new")) {
									if (!itemids.contains(String.valueOf(newConfiguredbtItem.get("bt_item_id")))) {
										itemids += String.valueOf(newConfiguredbtItem.get("bt_item_id")) + ",";
									}
								}
							}
							addItemToList = "";
						}
					}
				}

				// SOP("itemids==bt==" + itemids);
				if (!itemids.equals("")) {
					itemids = itemids.substring(0, itemids.length() - 1);
					StrSql = "SELECT item_id, item_name, group_type, group_name, COALESCE (pricetrans_amt, 0) AS pricetrans_amt,"
							+ " opt.option_qty, item_code, opt.option_id, opt.option_select, COALESCE (customer_rate, 0) AS tax_rate, COALESCE (customer_id, 0) AS tax_id,"
							+ " ( SELECT COUNT(DISTINCT optitem.option_id)"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_item_option optitem"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = optitem.option_item_id"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_branch price_branch ON price_branch.branch_id = " + branch_id + ""
							+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item_price price ON price.price_id = ("
							+ " SELECT recent_price.price_id"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = optitem.option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id = price_branch.branch_rateclass_id"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1 )"
							+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " WHERE optitem.option_itemmaster_id = opt.option_itemmaster_id"
							+ " AND optitem.option_group_id = group_id"
							+ " GROUP BY option_group_id ) AS groupitemcount,"
							+ " item_aftertaxcal, item_serial, group_id, group_aftertax"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_item_option opt"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
							+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
							+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item_price ON price_id = ("
							+ " SELECT recent_price.price_id"
							+ " FROM  " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id = branch_rateclass_id"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1 )"
							+ " LEFT JOIN  " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " LEFT JOIN  " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax3_ledger_id"
							+ " WHERE option_itemmaster_id = " + item_id
							+ " AND item_id IN (" + itemids + ")"
							+ " AND group_active = 1"
							+ " AND group_aftertax = 0"
							+ " GROUP BY group_name, group_type, item_id"
							+ " ORDER BY group_rank, group_name DESC, item_id";
					// SOP("StrSql====new==bt==" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						itemids = "";
						while (crs.next()) {
							configuredbtItem = new HashMap<String, String>();
							Double item_tax_value, sub_item_price;
							int sub_item_tax_id, item_qty = 0;
							double item_disc;
							String group_name, option_id, default_select;
							item_disc = 0;
							groupName = "";
							sub_item_price = (crs.getDouble("pricetrans_amt") * crs.getDouble("tax_rate") / 100) + crs.getDouble("pricetrans_amt");
							item_qty = crs.getInt("option_qty");
							item_tax_value = crs.getDouble("tax_rate");
							group_name = crs.getString("group_name");
							sub_item_tax_id = crs.getInt("tax_id");
							option_id = crs.getString("option_id");
							amount = item_qty * (((crs.getDouble("pricetrans_amt") - item_disc) * item_tax_value / 100) + (crs.getDouble("pricetrans_amt") - item_disc));
							amountwod = item_qty * ((crs.getDouble("pricetrans_amt") * item_tax_value / 100) + crs.getDouble("pricetrans_amt"));
							checked = "";
							disabled = "";
							for (Map m : configuredItemList) {
								if (m.containsKey("group_name") && m.containsValue(group_name)) {
									groupcount = Integer.parseInt(String.valueOf(m.get("bt_groupcount")));
									groupitemcount = Integer.parseInt(String.valueOf(m.get("bt_groupitemcount")));
								}
							}
							if (groupcount == 0) {
								// This logic gets the group count if the Group is New.
								for (String s : groupSet) {
									if (s.contains("bt_")) {
										groupcount++;
									}
								}
								groupcount++;
							}

							groupitemcount++;
							if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1")) {
								multi_select_basetotal += amount;
							}
							if (crs.getString("group_type").equals("1")) {
								groupName = group_name + " (Default)";
							} else {
								groupName = group_name;
							}
							group = group_name;
							so_total_disc += item_disc;
							before_tax_total += amount;
							before_tax_totalwod += amountwod;

							groupSet.add("bt_" + group_name);
							configuredbtItem.put("group_name", group_name);
							configuredbtItem.put("group", groupName);
							configuredbtItem.put("bt_group_id", crs.getString("group_id"));
							configuredbtItem.put("bt_item_check", "checked");
							configuredbtItem.put("bt_check", "on");
							configuredbtItem.put("bt_item_code", unescapehtml(crs.getString("item_code")));
							configuredbtItem.put("bt_item_name", unescapehtml(crs.getString("item_name")));
							configuredbtItem.put("bt_groupcount", String.valueOf(groupcount));
							configuredbtItem.put("bt_groupitemcount", String.valueOf(groupitemcount));
							configuredbtItem.put("bt_group_aftertax", crs.getString("group_aftertax"));
							configuredbtItem.put("bt_item_id", crs.getString("item_id"));
							configuredbtItem.put("bt_item_qty", String.valueOf(item_qty));
							configuredbtItem.put("bt_tax_id", String.valueOf(sub_item_tax_id));
							configuredbtItem.put("bt_tax_rate", df.format(item_tax_value));
							configuredbtItem.put("bt_option_id", String.valueOf(option_id));
							configuredbtItem.put("bt_amount", df.format(amount));
							configuredbtItem.put("bt_amountwod", df.format(amountwod));
							configuredbtItem.put("bt_netprice", df.format(sub_item_price));
							configuredbtItem.put("bt_baseprice", df.format(crs.getDouble("pricetrans_amt")));
							configuredbtItem.put("bt_basedisc", df.format(item_disc));

							configuredItemList.add(configuredbtItem);

							String isGroupCountAdded = "no";
							for (Map m : configuredItemList) {
								if (m.containsKey("group_name") && m.containsValue("groupItemCount")) {
									if (m.containsKey(group_name)) {
										isGroupCountAdded = "yes";
										m.put(group_name, Integer.parseInt(String.valueOf(m.get(group_name))) + 1);
										break;
									}
								}
							}
							if (isGroupCountAdded.equals("no")) {
								for (Map m : configuredItemList) {
									if (m.containsKey("group_name") && m.containsValue("groupItemCount")) {
										m.put(group_name, String.valueOf(groupitemcount));
										isGroupCountAdded = "no";
									}
									if (m.containsKey("group_name") && m.containsValue("totalBtItemDetails")) {
										totalBtGroupCount = Integer.parseInt(String.valueOf(m.get("bt_group_count")));
										SOP("totalBtGroupCount==" + totalBtGroupCount);
										totalBtGroupCount++;
									}
								}
							}
						}
					}
					crs.close();
				}

				containsGroup = false;
				for (Map m : configuredItemList) {
					if (m.containsKey("group_name") && m.containsValue("totalBtItemDetails")) {
						m.put("bt_group_count", String.valueOf(totalBtGroupCount));
						containsGroup = true;
						break;
					}
				}
				if (containsGroup == false) {
					totalBtItemDetails.put("group_name", "totalBtItemDetails");
					totalBtItemDetails.put("bt_group_count", String.valueOf(totalBtGroupCount));
					configuredItemList.add(totalBtItemDetails);
				}

				// jsonconfiguredItemList = gson.toJson(configuredItemList);
				// SOP("jsonData from JSON==222==" + jsonconfiguredItemList);
				// SOP("configuredItemList==after new bt==" + gson.toJson(configuredItemList));

				Str.append("<div class=\"table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th width=\"10%\" data-toggle=\"true\">Select</th>\n");
				Str.append("<th width=\"40%\" >Item</th>\n");
				Str.append("<th width=\"10%\" >Code</th>\n");
				Str.append("<th width=\"10%\" data-hide=\"phone\">Qty</th>\n");
				Str.append("<th width=\"10%\" data-hide=\"phone\">Price</th>\n");
				Str.append("<th width=\"10%\" data-hide=\"phone\">Discount</th>\n");
				Str.append("<th width=\"10%\" data-hide=\"phone\">Amount</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				for (Map configureItem : configuredItemList) {
					if (configureItem.get("group_name").equals("mainItemDetails")) {
						Str.append("<tr>\n");

						// Select
						Str.append("<td align=center>");

						Str.append("<input type=\"checkbox\" checked=\"checked\" disabled=\"disabled\"/>\n");

						// Hidden Fields related to the Item
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_name\"");
						Str.append(" name=\"txt_item_name\"");
						Str.append(" value=\"").append(configureItem.get("item_name")).append("\"/>\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_code\"");
						Str.append(" name=\"txt_item_code\"");
						Str.append(" value=\"").append(configureItem.get("item_code")).append("\"/>\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_id\"");
						Str.append(" name=\"txt_item_id\"");
						Str.append(" value=\"").append(configureItem.get("item_id")).append("\">\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_baseprice\"");
						Str.append(" name=\"txt_item_baseprice\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_price")))))).append("\">\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_priceamt\"");
						Str.append(" name=\"txt_item_priceamt\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_price")))))).append("\">\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_basedisc\"");
						Str.append(" name=\"txt_item_basedisc\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netdisc")))))).append("\">\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"div_main_item_amount\"");
						Str.append(" name=\"div_main_item_amount\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(CNumeric(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netamount"))))))))).append("\">\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"div_main_item_amountwod\"");
						Str.append(" name=\"div_main_item_amountwod\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(CNumeric(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netamountwod")))))))))
								.append("\">\n");

						if (gsttype.equals("state")) {
							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_item_tax_id1\"");
							Str.append(" name=\"txt_item_tax_id1\"");
							Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax1_id")))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_item_tax_rate1\"");
							Str.append(" name=\"txt_item_tax_rate1\"");
							Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax1_rate")))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_item_tax_id2\"");
							Str.append(" name=\"txt_item_tax_id2\"");
							Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax2_id")))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_item_tax_rate2\"");
							Str.append(" name=\"txt_item_tax_rate2\"");
							Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax2_rate")))))).append("\">\n");

						} else if (gsttype.equals("central")) {
							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_item_tax_id3\"");
							Str.append(" name=\"txt_item_tax_id3\"");
							Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax3_id")))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_item_tax_rate3\"");
							Str.append(" name=\"txt_item_tax_rate3\"");
							Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax3_rate")))))).append("\">\n");

						}
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_tax_id4\"");
						Str.append(" name=\"txt_item_tax_id4\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax4_id")))))).append("\">\n");

						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_tax_rate4\"");
						Str.append(" name=\"txt_item_tax_rate4\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_tax4_rate")))))).append("\">\n");

						Str.append("</td>\n");

						// Item
						Str.append("<td>").append(configureItem.get("item_name")).append("</td>\n");

						// Code
						Str.append("<td>").append(configureItem.get("item_code")).append("</td>\n");

						// Qty
						Str.append("<td align=center>1</td>\n");

						// Price
						Str.append("<td  align=right>").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netprice"))))))).append("</td>\n");

						// Discount
						Str.append("<td>");

						Str.append("<input style=\"text-align: right\"");
						Str.append(" type=\"text\"");
						Str.append(" id=\"txt_item_disc\"");
						Str.append(" name=\"txt_item_disc\"");
						Str.append(" value=\"").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("item_netdisc")))))).append("\"");
						Str.append(" class=\"form-control\" size=\"8\" maxlength=\"8\"");
						Str.append(" onkeyup=\"toFloat('txt_item_disc','');CheckItemBasePrice('" + gsttype + "','")
								.append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_price")))))).append("', '")
								.append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netdisc")))))).append("');\"");
						Str.append(" onblur=\"AddDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '").append(gsttype).append("', '")
								.append(configureItem.get("item_id")).append("');\"/>\n");

						Str.append("</td>\n");

						// Amount
						Str.append("<td align=right>");

						// Str.append("<input type=\"hidden\"");
						// Str.append(" id=\"txt_item_disc\"");
						// Str.append(" name=\"txt_item_disc\"");
						// Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netdisc")))))).append("\">\n");

						Str.append("<div align=right id=\"div_main_item_total\">").append((IndDecimalFormat(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netamount"))))))))
								.append("</div>\n");
						Str.append("</td>\n");

						Str.append("</tr>\n");
						if (emp_quote_discountupdate.equals("0")) {
							add_disc_readOnly = "readOnly";
						} else {
							add_disc_readOnly = "";
						}

					}
				}

				for (String groupset : groupSet) {
					String groupHeaderDisplayed = "no";
					for (Map configureItem : configuredItemList) {
						if ((!configureItem.get("group_name").equals("mainItemDetails")) && configureItem.get("group_name").equals(groupset.substring(3)) && groupset.substring(0, 3).equals("bt_")) {
							// This if condition displays the Group header
							if (groupHeaderDisplayed.equals("no")) {
								String groupItemCount = "0";
								for (Map m : configuredItemList) {
									if (m.containsValue("groupItemCount")) {
										if (m.containsKey(groupset.substring(3))) {
											groupItemCount = String.valueOf(m.get(groupset.substring(3)));
										}
									}
								}

								groupHeaderDisplayed = "yes";
								Str.append("<tr>\n");
								Str.append("<td colspan=6>");
								Str.append("<center><b>").append(configureItem.get("group")).append("</b></center>");
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_count\"");
								Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_count\"");
								Str.append(" value=\"").append(groupItemCount).append("\">\n");
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_value\"");
								Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_value\">\n");
								Str.append("</td>\n");

								Str.append("</tr>\n");
							}

							Str.append("<tr>\n");

							// Select
							Str.append("<td align=center>");

							Str.append("<input type=\"checkbox\"");
							Str.append(" id=\"chk_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("\"");
							Str.append(" name=\"chk_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("\"");
							Str.append(" " + configureItem.get("bt_item_check") + "");
							Str.append(" " + configureItem.get("bt_item_disabled") + "");
							Str.append(" onclick=\"CalculateTotal(1);\"/>\n");

							// Hidden Fields related to the Item
							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_chk\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_chk\"");
							Str.append(" value=\"").append(configureItem.get("bt_check")).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_gpname\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_gpname\"");
							Str.append(" value=\"").append(configureItem.get("group_name")).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_id\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_id\"");
							Str.append(" value=\"").append(configureItem.get("bt_item_id")).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_qty\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_qty\"");
							Str.append(" value=\"").append(configureItem.get("bt_item_qty")).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_tax_id\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_tax_id\"");
							Str.append(" value=\"").append(configureItem.get("bt_tax_id")).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_tax_rate\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_tax_rate\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_tax_rate"))))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_option_id\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_option_id\"");
							Str.append(" value=\"").append(configureItem.get("bt_option_id")).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_option_group\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_option_group\"");
							Str.append(" value=\"").append(configureItem.get("group_name")).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_amount\"");
							Str.append(" name=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_amount\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_baseprice"))))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_amountwod\"");
							Str.append(" name=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_amountwod\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_amountwod"))))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_netprice\"");
							Str.append(" name=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_netprice\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_netprice"))))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_baseprice\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_baseprice\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_baseprice"))))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_basedisc\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_basedisc\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_basedisc"))))))).append("\">\n");

							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_totalwod\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_totalwod\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_amountwod"))))))).append("\">\n");

							Str.append("</td>\n");

							// Item
							Str.append("<td>").append(configureItem.get("bt_item_name")).append("</td>\n");

							// Code
							Str.append("<td>").append(configureItem.get("bt_item_code")).append("</td>\n");

							// Qty
							Str.append("<td align=center>").append(configureItem.get("bt_item_qty")).append("</td>\n");

							// Price
							Str.append("<td align=right>").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("bt_netprice")))))).append("</td>\n");

							// Discount
							Str.append("<td>");

							Str.append("<input style=\"text-align: right\"");
							Str.append(" type=\"text\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\"");
							Str.append(" value=\"").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("bt_basedisc")))))).append("\"");
							Str.append(" class=\"form-control\" size=\"8\" maxlength=\"8\"");
							Str.append(" onkeyup=\"toFloat(this.id, '');");
							Str.append("CheckBasePrice(").append(configureItem.get("bt_group_id")).append(", ").append(configureItem.get("bt_groupitemcount")).append(", ");
							Str.append(configureItem.get("bt_groupitemcount")).append(", ").append(configureItem.get("bt_groupcount")).append(", ").append(configureItem.get("bt_group_aftertax"))
									.append(");\"");
							Str.append(" onblur=\"AddBTDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '").append(configureItem.get("bt_item_id"))
									.append("');\"/>\n");

							Str.append("</td>\n");

							// Amount
							Str.append("<td align=right>\n");
							Str.append("<div id=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_total\">");
							Str.append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("bt_amount"))))));
							Str.append("</div>");
							Str.append("</td>\n");

							Str.append("</tr>\n");
						}
					}

				}

				for (Map configureItem : configuredItemList) {

					if ((!configureItem.get("group_name").equals("mainItemDetails")) && configureItem.get("group_name").equals("totalBtItemDetails")) {
						Str.append("<tr>\n");
						// Ex-Showroom Price
						Str.append("<td colspan=6 align=\"right\" nowrap>");
						Str.append("<b>Ex-Showroom Price: </b>");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_bt_group_count\"");
						Str.append(" name=\"txt_bt_group_count\"");
						Str.append(" value=\"").append(configureItem.get("bt_group_count")).append("\"/>\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_bt_multiselect_basetotal\"");
						Str.append(" name=\"txt_bt_multiselect_basetotal\"");
						Str.append(" value=\"").append(df.format(multi_select_basetotal)).append("\"/>\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_bt_item_baseprice\"");
						Str.append(" name=\"txt_bt_item_baseprice\"");
						Str.append(" value=\"").append(df.format(before_tax_total)).append("\">\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_addoff_bt_item_count\"");
						Str.append(" name=\"txt_addoff_bt_item_count\"");
						Str.append(" value=\"").append(addoffer_bt_item_count).append("\"/>\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_bt_allselected_total\"");
						Str.append(" name=\"txt_bt_allselected_total\"");
						Str.append(" value=\"").append(df.format(all_selected_total)).append("\"/>\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_price\"");
						Str.append(" name=\"txt_item_price\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(CNumeric(item_netprice)))).append("\"/>\n");
						Str.append("</td>\n");
						Str.append("<td>");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_expricewod\"");
						Str.append(" name=\"txt_expricewod\"");
						Str.append(" value=\"").append(df.format(before_tax_totalwod)).append("\"/>\n");
						Str.append("<b><div align=\"right\" id=\"div_item_price\">").append(IndDecimalFormat(df.format(before_tax_total))).append("</div></b>");
						Str.append("</td>\n");

						Str.append("</tr>\n");
						break;
					}
				}
				// End Before Tax

				// Start After Tax
				groupitemcount = 0;
				groupcount = 0;
				all_selected_total = 0.00;
				multi_select_basetotal = 0.00;
				group = "";
				String formulae;
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine eng = mgr.getEngineByName("JavaScript");
				// configuredItemList.addAll(configuredBtItemList);
				// SOP("configuredItemList==before at==" + gson.toJson(configuredItemList));

				// This checks whether after tax Groups are already added in the Map or not, if it's added then it won't add again.
				containsMap = false;
				for (Map configuredItem : configuredItemList) {
					if (configuredItem.containsKey("at_item_name")) {
						containsMap = true;
						break;
					}
				}
				if (containsMap == false) {
					StrSql = "SELECT item_id, item_code, item_name,"
							+ " IF ( mainitemoption.option_select = 1 OR COALESCE(trans_option_id, 0) != 0 OR COALESCE(quoteitem_id, 0) != 0, 'checked', '' ) AS item_check,"
							+ " IF ( COALESCE (trans_option_id, 0) != 0 AND trans_vehstock_id IS NOT NULL AND trans_vehstock_id = " + vehstock_id + ", 'disabled', '' ) AS item_disabled,"
							+ " group_type, group_name, COALESCE(pricetrans_amt, 0) AS pricetrans_amt,"
							+ " COALESCE(pricetrans_variable, 0) AS pricetrans_variable, mainitemoption.option_group_id, mainitemoption.option_qty,"
							+ " item_code, mainitemoption.option_id, mainitemoption.option_select, item_small_desc,"
							+ " COALESCE(customer_rate, 0) AS tax_rate, COALESCE(customer_id, 0) AS tax_id,"
							+ " item_aftertaxcal, IF(item_aftertaxcal_formulae != '', item_aftertaxcal_formulae, '') AS item_aftertaxcal_formulae,"
							+ " (SELECT COUNT(DISTINCT itemoption.option_id)"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_option itemoption"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = itemoption.option_item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch price_branch ON price_branch.branch_id = " + branch_id + ""
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price price ON price.price_id = (SELECT recent_price.price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = itemoption.option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id	 = price_branch.branch_rateclass_id	"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1)"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " WHERE itemoption.option_itemmaster_id = mainitemoption.option_itemmaster_id"
							+ " AND itemoption.option_group_id = group_id"
							+ " GROUP BY option_group_id) AS groupitemcount,"
							+ " COALESCE(quoteitem_option_id, 0) AS quoteitem_option_id,"
							+ " COALESCE(quoteitem_option_group, '') AS quoteitem_option_group,"
							+ " item_aftertaxcal, item_serial, group_id,"
							+ " COALESCE(quoteitem_price, '') AS quoteitem_price,"
							+ " COALESCE(quoteitem_qty, '') AS quoteitem_qty,"
							+ " COALESCE(quoteitem_disc, '0.00') AS quoteitem_disc,"
							+ " COALESCE(quoteitem_tax_rate, '') AS quoteitem_tax_rate,"
							+ " COALESCE(quoteitem_tax_id, '') AS quoteitem_tax_id,"
							+ " COALESCE(quoteitem_total, '') AS quoteitem_total,"
							+ " group_aftertax"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_option mainitemoption"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option vehoption ON vehoption.option_code = item_code"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = vehoption.option_id"
							// + " AND trans_vehstock_id = " + vehstock_id
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_id = (SELECT recent_price.price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id	 = branch_rateclass_id"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1)"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax3_ledger_id";
					// if (!quote_id.equals("0")) {
					StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_item_id = item_id AND quoteitem_quote_id = " + quote_id + "";
					// }
					StrSql += " WHERE option_itemmaster_id = " + item_id;
					if (!vehstock_id.equals("0")) {
						StrSql += " AND ( mainitemoption.option_select = 1 OR trans_vehstock_id = " + vehstock_id + " OR quoteitem_quote_id = " + quote_id + ")";
					}
					StrSql += "	AND (price_id > 0 OR (item_aftertaxcal  = '1' AND item_aftertaxcal_formulae != '')) "
							+ "	AND item_id = item_id"
							+ " AND group_active = 1"
							+ " AND group_aftertax = 1"
							+ " GROUP BY group_name, group_type, item_id"
							+ " ORDER BY group_rank, group_name DESC, item_id";
					// SOP("StrSql====at==" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);
					// SOP("before_tax_total==" + before_tax_total);
					// SOP("before_tax_totalwod==" + before_tax_totalwod);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							configuredatItem = new HashMap<String, String>();
							totalAtItemDetails = new HashMap<String, String>();

							Double pricetrans_amt = 0.00, item_price_amt = 0.00, base_price = 0.00;
							Double item_tax_value, item_price;
							int item_tax_id, item_qty = 0;
							double item_disc;
							String group_name, option_id;

							if (crs.getString("quoteitem_disc").equals("")) {
								item_disc = (int) crs.getDouble("price_disc");
							} else {
								item_disc = (int) crs.getDouble("quoteitem_disc");
							}

							if (crs.getString("quoteitem_price").equals("")) {
								item_price_amt = 0.00;
							} else {
								item_price_amt = crs.getDouble("quoteitem_price");
							}
							item_price = (crs.getDouble("pricetrans_amt") * crs.getDouble("tax_rate") / 100) + crs.getDouble("pricetrans_amt");
							item_qty = crs.getInt("option_qty");
							item_tax_id = crs.getInt("tax_id");
							option_id = crs.getString("option_id");
							item_tax_value = crs.getDouble("tax_rate");
							group_name = crs.getString("group_name");
							amount = item_qty * (((item_price - item_disc) * item_tax_value / 100) + (item_price - item_disc));

							if (crs.getString("item_aftertaxcal_formulae").contains("expricewd") && crs.getString("item_aftertaxcal").equals("1")) {
								formulae = crs.getString("item_aftertaxcal_formulae").replace("expricewd", Double.toString(before_tax_total));
								pricetrans_amt = ((Double) eng.eval(unescapehtml(formulae)));
								amount = item_qty * (pricetrans_amt - item_disc);
								base_price = pricetrans_amt;
								item_price_amt = pricetrans_amt;
							} else if (crs.getString("item_aftertaxcal_formulae").contains("expricewod") && crs.getString("item_aftertaxcal").equals("1")) {
								formulae = crs.getString("item_aftertaxcal_formulae").replace("expricewod", df.format(before_tax_totalwod));
								pricetrans_amt = ((Double) eng.eval(unescapehtml(formulae)));
								amount = item_qty * (pricetrans_amt - item_disc);
								item_price_amt = pricetrans_amt;
							} else if (crs.getString("item_aftertaxcal_formulae").equals("") && crs.getString("item_aftertaxcal").equals("0")) {
								if (crs.getString("pricetrans_variable").equals("0")) {
									pricetrans_amt = item_price;
									amount = ((crs.getDouble("pricetrans_amt") - item_disc) * item_tax_value / 100) + crs.getDouble("pricetrans_amt") - item_disc;
									base_price = crs.getDouble("pricetrans_amt");
									item_price_amt = pricetrans_amt;
								} else {
									amount = ((item_price_amt - item_disc) * item_tax_value / 100) + item_price_amt - item_disc;

								}

							}

							checked = "";
							disabled = "";
							groupName = "";

							disabled = crs.getString("item_disabled");

							if (crs.getString("group_type").equals("1")) {
								groupName = group_name + " (Default)";
							} else {
								groupName = group_name;
							}
							if (!group.equals(group_name) && !group.equals("")) {
								groupitemcount = 0;
							}
							if (!group.equals(group_name)) {
								if (!group_name.equals("Additional Discounts")) {
									groupcount++;
									totalAtGroupCount++;
								}
								if (unescapehtml(group_name).equals("Additional Discounts")) {
									addoffer_at_item_count++;
									disc_amt = "0";
									addoff_at_check = "";
									if (!quote_id.equals("0")) {
										if (!crs.getString("quoteitem_option_group").equals("")) {
											addoff_at_check = "checked";
											disc_amt = crs.getString("quoteitem_disc");
										}
									} else if (!crs.getString("option_id").equals("0")) {
										if (crs.getString("option_select").equals("1")) {
											addoff_at_check = "checked";
										}
										disc_amt = crs.getString("pricetrans_amt");
									}

									if (addoff_at_check.equals("checked")) {
										after_tax_total = after_tax_total - Double.parseDouble(CNumeric(disc_amt));
									}
								}
							}
							groupitemcount++;
							if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1")) {
								multi_select_basetotal += amount;
							}
							group = group_name;
							so_total_disc += item_disc;
							after_tax_total += amount;

							groupSet.add("at_" + group_name);
							if (!group_name.equals("Additional Discounts")) {
								configuredatItem.put("group_name", group_name);
								configuredatItem.put("group", groupName);
								configuredatItem.put("at_group_id", crs.getString("group_id"));
								configuredatItem.put("at_item_check", crs.getString("item_check"));
								if (crs.getString("item_check").equals("checked")) {
									configuredatItem.put("at_check", "on");
								} else {
									configuredatItem.put("at_check", "off");
								}

								configuredatItem.put("at_item_disabled", disabled);
								configuredatItem.put("at_item_code", unescapehtml(crs.getString("item_code")));
								configuredatItem.put("at_item_name", unescapehtml(crs.getString("item_name")));
								configuredatItem.put("at_groupcount", String.valueOf(groupcount));
								configuredatItem.put("at_groupitemcount", String.valueOf(groupitemcount));
								configuredatItem.put("at_group_aftertax", crs.getString("group_aftertax"));
								configuredatItem.put("at_item_id", crs.getString("item_id"));
								configuredatItem.put("at_item_qty", String.valueOf(item_qty));
								configuredatItem.put("at_tax_id", String.valueOf(item_tax_id));
								configuredatItem.put("at_tax_rate", df.format(item_tax_value));
								configuredatItem.put("at_option_id", String.valueOf(option_id));
								configuredatItem.put("at_pricetrans_variable", String.valueOf(crs.getString("pricetrans_variable")));
								configuredatItem.put("at_item_aftertaxcal_formulae", String.valueOf(unescapehtml(crs.getString("item_aftertaxcal_formulae"))));
								configuredatItem.put("at_amount", df.format(amount));
								configuredatItem.put("at_amountwod", df.format(amountwod));
								if (!crs.getString("pricetrans_amt").equals("0.0")) {
									configuredatItem.put("at_baseprice", df.format(crs.getDouble("pricetrans_amt")));
								} else {
									configuredatItem.put("at_baseprice", df.format(item_price_amt));
								}
								configuredatItem.put("at_netprice", df.format(item_price_amt));
								configuredatItem.put("at_basedisc", df.format(item_disc));

							} else if (group_name.equals("Additional Discounts")) {
								// Additional Discount
								configuredatItem.put("group_name", group_name);
								configuredatItem.put("group", groupName);
								configuredatItem.put("at_group_id", crs.getString("group_id"));
								configuredatItem.put("at_item_id", crs.getString("item_id"));
								configuredatItem.put("at_item_check", crs.getString("item_check"));
								configuredatItem.put("at_item_code", unescapehtml(crs.getString("item_code")));
								configuredatItem.put("at_item_name", unescapehtml(crs.getString("item_name")));
								configuredatItem.put("at_groupitemcount", String.valueOf(addoffer_at_item_count));
								SOP("disc_amt==" + disc_amt);
								configuredatItem.put("at_disc_amt", df.format(Double.parseDouble(CNumeric(disc_amt))));
							}

							configuredItemList.add(configuredatItem);

							String isGroupCountAdded = "no";
							for (Map m : configuredItemList) {
								if (m.containsValue("groupItemCount")) {
									if (m.containsKey(group_name)) {
										isGroupCountAdded = "yes";
										m.put(group_name, Integer.parseInt(String.valueOf(m.get(group_name))) + 1);
										break;
									}
								}
							}
							if (isGroupCountAdded.equals("no")) {
								for (Map m : configuredItemList) {
									if (m.containsKey("group_name") && m.containsValue("groupItemCount")) {
										m.put(group_name, String.valueOf(groupitemcount));
										isGroupCountAdded = "no";
										break;
									}
								}
							}
						}
					}
					crs.close();
				}

				// SOP("itemids==at==" + itemids);
				if (!itemids.equals("")) {
					if (itemids.charAt(itemids.length() - 1) == ',') {
						itemids = itemids.substring(0, itemids.length() - 1);
					}

					StrSql = "SELECT item_id, item_name, group_type, group_name, COALESCE(pricetrans_amt, 0) AS pricetrans_amt,"
							+ " opt.option_qty, item_code, opt.option_id, opt.option_select,"
							+ " COALESCE(customer_rate, 0) AS tax_rate, COALESCE(customer_id, 0) AS tax_id,"
							+ "	item_aftertaxcal, COALESCE(item_aftertaxcal_formulae, 0) AS item_aftertaxcal_formulae, COALESCE(pricetrans_variable, 0) AS pricetrans_variable,"
							+ " (SELECT COUNT(DISTINCT optitem.option_id)"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_option optitem"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = optitem.option_item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch price_branch ON price_branch.branch_id = " + branch_id + ""
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price price ON price.price_id = (SELECT recent_price.price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = optitem.option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id	 = price_branch.branch_rateclass_id	"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1)"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " WHERE optitem.option_itemmaster_id = opt.option_itemmaster_id"
							+ " AND optitem.option_group_id = group_id"
							+ " GROUP BY option_group_id) AS groupitemcount,"
							+ " item_aftertaxcal, item_serial, group_id,"
							+ " group_aftertax"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_option opt"
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_group ON group_id = option_group_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = option_item_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_id = (SELECT recent_price.price_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_price recent_price"
							+ " WHERE recent_price.price_item_id = option_itemmaster_id"
							+ " AND recent_price.price_rateclass_id	 = branch_rateclass_id"
							+ " AND recent_price.price_effective_from <= '" + quote_date + "'"
							+ " AND recent_price.price_active = 1"
							+ " ORDER BY recent_price.price_effective_from DESC"
							+ " LIMIT 1)"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_price_trans ON pricetrans_price_id = price_id"
							+ " AND pricetrans_item_id = option_item_id"
							+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = item_salestax3_ledger_id"
							+ " WHERE option_itemmaster_id = " + item_id + ""
							+ " AND item_id IN (" + itemids + ")"
							+ "	AND (price_id > 0 OR (item_aftertaxcal  = '1' AND item_aftertaxcal_formulae != ''))"
							+ " AND group_active = 1"
							+ " AND group_aftertax = 1"
							+ " GROUP BY group_name, group_type, item_id"
							+ " ORDER BY group_rank, group_name DESC, item_id";
					// SOP("StrSql====new==at==" + StrSqlBreaker(StrSql));
					crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						while (crs.next()) {
							configuredatItem = new HashMap<String, String>();
							Double pricetrans_amt = 0.00, item_price_amt = 0.00, base_price = 0.00;
							Double item_tax_value, item_price;
							int item_tax_id, item_qty = 0;
							double item_disc;
							String group_name, option_id;
							item_disc = 0;
							item_price_amt = crs.getDouble("pricetrans_amt");
							item_price = (crs.getDouble("pricetrans_amt") * crs.getDouble("tax_rate") / 100) + crs.getDouble("pricetrans_amt");
							item_qty = crs.getInt("option_qty");
							item_tax_id = crs.getInt("tax_id");
							option_id = crs.getString("option_id");
							item_tax_value = crs.getDouble("tax_rate");
							group_name = crs.getString("group_name");
							groupName = "";
							if (crs.getString("group_type").equals("1")) {
								groupName = group_name + " (Default)";
							} else {
								groupName = group_name;
							}
							amount = item_qty * (((item_price - item_disc) * item_tax_value / 100) + (item_price - item_disc));
							if (crs.getString("item_aftertaxcal_formulae").contains("expricewd") && crs.getString("item_aftertaxcal").equals("1")) {
								formulae = crs.getString("item_aftertaxcal_formulae").replace("expricewd", Double.toString(before_tax_total));
								pricetrans_amt = ((Double) eng.eval(unescapehtml(formulae)));
								amount = item_qty * (pricetrans_amt - item_disc);
								base_price = pricetrans_amt;
								item_price_amt = pricetrans_amt;
							} else if (crs.getString("item_aftertaxcal_formulae").contains("expricewod") && crs.getString("item_aftertaxcal").equals("1")) {
								formulae = crs.getString("item_aftertaxcal_formulae").replace("expricewod", df.format(before_tax_totalwod));
								pricetrans_amt = ((Double) eng.eval(unescapehtml(formulae)));
								amount = item_qty * (pricetrans_amt - item_disc);
								item_price_amt = pricetrans_amt;
							} else if (crs.getString("item_aftertaxcal_formulae").equals("") && crs.getString("item_aftertaxcal").equals("0")) {
								if (crs.getString("pricetrans_variable").equals("0")) {
									pricetrans_amt = item_price;
									amount = ((crs.getDouble("pricetrans_amt") - item_disc) * item_tax_value / 100) + crs.getDouble("pricetrans_amt") - item_disc;
									base_price = crs.getDouble("pricetrans_amt");
									item_price_amt = pricetrans_amt;
								} else {
									amount = ((item_price_amt - item_disc) * item_tax_value / 100) + item_price_amt - item_disc;

								}

							}
							checked = "";
							disabled = "";
							if (!group.equals(group_name) && !group.equals("")) {
								groupitemcount = 0;
							}

							for (Map m : configuredItemList) {
								if (m.containsKey("group_name") && m.containsValue(group_name)) {
									groupcount = Integer.parseInt(String.valueOf(m.get("at_groupcount")));
									groupitemcount = Integer.parseInt(String.valueOf(m.get("at_groupitemcount")));
								}
							}
							if (groupcount == 0) {
								// This logic gets the group count if the Group is New.
								for (String s : groupSet) {
									if (s.contains("at_")) {
										groupcount++;
									}
								}
								groupcount++;
							}

							groupitemcount++;
							if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1")) {
								multi_select_basetotal += amount;
							}
							group = group_name;
							so_total_disc += item_disc;
							after_tax_total += amount;

							groupSet.add("at_" + group_name);
							configuredatItem.put("group_name", group_name);
							configuredatItem.put("group", groupName);
							configuredatItem.put("at_group_id", crs.getString("group_id"));
							configuredatItem.put("at_item_check", "checked");
							if (crs.getString("item_check").equals("checked")) {
								configuredatItem.put("at_check", "on");
							} else {
								configuredatItem.put("at_check", "off");
							}
							configuredatItem.put("at_item_code", unescapehtml(crs.getString("item_code")));
							configuredatItem.put("at_item_name", unescapehtml(crs.getString("item_name")));
							configuredatItem.put("at_groupcount", String.valueOf(groupcount));
							configuredatItem.put("at_groupitemcount", String.valueOf(groupitemcount));
							configuredatItem.put("at_group_aftertax", crs.getString("group_aftertax"));
							configuredatItem.put("at_item_id", crs.getString("item_id"));
							configuredatItem.put("at_item_qty", String.valueOf(item_qty));
							configuredatItem.put("at_tax_id", String.valueOf(item_tax_id));
							configuredatItem.put("at_tax_rate", df.format(item_tax_value));
							configuredatItem.put("at_option_id", String.valueOf(option_id));
							configuredatItem.put("at_pricetrans_variable", String.valueOf(crs.getString("pricetrans_variable")));
							configuredatItem.put("at_item_aftertaxcal_formulae", String.valueOf(unescapehtml(crs.getString("item_aftertaxcal_formulae"))));
							configuredatItem.put("at_amount", df.format(amount));
							configuredatItem.put("at_amountwod", df.format(amountwod));
							if (!crs.getString("pricetrans_amt").equals("0.0")) {
								configuredatItem.put("at_baseprice", df.format(crs.getDouble("pricetrans_amt")));
							} else {
								configuredatItem.put("at_baseprice", df.format(item_price_amt));
							}
							configuredatItem.put("at_netprice", df.format(item_price_amt));
							configuredatItem.put("at_basedisc", df.format(item_disc));

							configuredItemList.add(configuredatItem);

							String isGroupCountAdded = "no";
							for (Map m : configuredItemList) {
								if (m.containsValue("groupItemCount")) {
									if (m.containsKey(group_name)) {
										isGroupCountAdded = "yes";
										m.put(group_name, Integer.parseInt(String.valueOf(m.get(group_name))) + 1);
										break;
									}
								}
							}
							if (isGroupCountAdded.equals("no")) {
								for (Map m : configuredItemList) {
									if (m.containsKey("group_name") && m.containsValue("groupItemCount")) {
										m.put(group_name, String.valueOf(groupitemcount));
										isGroupCountAdded = "no";
									}
									if (m.containsKey("group_name") && m.containsValue("totalAtItemDetails")) {
										totalAtGroupCount = Integer.parseInt(String.valueOf(m.get("at_group_count")));
										totalAtGroupCount++;
									}

								}
							}
						}
					}
					crs.close();
				}

				containsGroup = false;
				for (Map m : configuredItemList) {
					if (m.containsKey("group_name") && m.containsValue("totalAtItemDetails")) {
						m.put("at_group_count", String.valueOf(totalAtGroupCount));
						containsGroup = true;
						break;
					}
				}
				if (containsGroup == false) {
					totalAtItemDetails.put("group_name", "totalAtItemDetails");
					totalAtItemDetails.put("at_group_count", String.valueOf(totalAtGroupCount));
					configuredItemList.add(totalAtItemDetails);
				}

				// SOP("configuredItemList==after new at==" + gson.toJson(configuredItemList));
				for (String groupset : groupSet) {
					String groupHeaderDisplayed = "no";
					for (Map configureItem : configuredItemList) {
						if ((!configureItem.get("group_name").equals("mainItemDetails")) && configureItem.get("group_name").equals(groupset.substring(3)) && groupset.substring(0, 3).equals("at_")) {
							// This if condition displays the Group header
							if (groupHeaderDisplayed.equals("no")) {
								String groupItemCount = "0";
								for (Map m : configuredItemList) {
									if (m.containsValue("groupItemCount")) {
										if (m.containsKey(groupset.substring(3))) {
											groupItemCount = String.valueOf(m.get(groupset.substring(3)));
										}
									}
								}

								groupHeaderDisplayed = "yes";
								Str.append("<tr>\n");
								Str.append("<td colspan=6>");
								Str.append("<center><b>").append(configureItem.get("group")).append("</b></center>");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_count\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_count\"");
								Str.append(" value=\"").append(groupItemCount).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_value\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_value\">\n");
								Str.append("</td>\n");

								Str.append("</tr>\n");
							}
							Str.append("<tr>\n");

							if (!configureItem.get("group_name").equals("Additional Discounts")) {
								// Select
								Str.append("<td align=center>");
								Str.append("<input type=\"checkbox\"");
								Str.append(" id=\"chk_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" name=\"chk_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" " + configureItem.get("at_item_check") + "");
								Str.append(" " + configureItem.get("at_item_disabled") + "");
								Str.append(" onclick=\"CalculateTotal(1);\"/>\n");

								// Hidden Fields related to the Item.
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_chk\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_chk\"");
								Str.append(" value=\"").append(configureItem.get("at_check")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_gpname\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_gpname\"");
								Str.append(" value=\"").append(configureItem.get("group_name")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_id\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_id\"");
								Str.append(" value=\"").append(configureItem.get("at_item_id")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_qty\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_qty\"");
								Str.append(" value=\"").append(configureItem.get("at_item_qty")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_tax_id\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_tax_id\"");
								Str.append(" value=\"").append(configureItem.get("at_tax_id")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_tax_rate\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_tax_rate\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_tax_rate"))))))).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_option_id\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_option_id\"");
								Str.append(" value=\"").append(configureItem.get("at_option_id")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_option_group\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_option_group\"");
								Str.append(" value=\"").append(configureItem.get("group_name")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_amount\"");
								Str.append(" name=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_amount\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_baseprice"))))))).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_amountwod\"");
								Str.append(" name=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_amountwod\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_amountwod"))))))).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_netprice\"");
								Str.append(" name=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_netprice\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_netprice"))))))).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_baseprice\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_baseprice\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_baseprice"))))))).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_tax_formulae\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_tax_formulae\"");
								Str.append(" value=\"").append(configureItem.get("at_item_aftertaxcal_formulae")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_basedisc\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_basedisc\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_basedisc"))))))).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_variable\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_variable\"");
								Str.append(" value=\"").append(String.valueOf((configureItem.get("at_pricetrans_variable")))).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_totalwod\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_totalwod\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_amountwod"))))))).append("\">\n");
								Str.append("</td>\n");

								// Item
								Str.append("<td>").append(configureItem.get("at_item_name")).append("</td>\n");

								// Code
								Str.append("<td>").append(configureItem.get("at_item_code")).append("</td>\n");

								// Qty
								Str.append("<td align=center>").append(configureItem.get("at_item_qty")).append("</td>\n");

								// Price
								Str.append("<td  align=right>\n");
								if (String.valueOf(configureItem.get("at_pricetrans_variable")).equals("0")) {
									Str.append("<div id=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_price\">");
									Str.append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_netprice"))))));
									Str.append("</div>\n");
								} else if (String.valueOf(configureItem.get("at_pricetrans_variable")).equals("1")) {
									Str.append("<input type=\"text\"");
									Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_price\"");
									Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_price\"");
									Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_netprice"))))).append("\"");
									Str.append(" class=\"form-control\" size=\"10\" maxlength=\"10\"");
									Str.append(" onkeyup=\"toFloat(this.id, '');");
									Str.append(" CheckBasePrice(").append(configureItem.get("at_group_id")).append(", ").append(configureItem.get("at_groupitemcount")).append(", ");
									Str.append(configureItem.get("at_groupitemcount")).append(", ").append(configureItem.get("at_groupcount")).append(", ")
											.append(configureItem.get("at_group_aftertax")).append(", ");
									Str.append(configureItem.get("at_pricetrans_variable")).append(");\"");
									Str.append(" onblur=\"AddATDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '")
											.append(configureItem.get("at_item_id")).append("');\"/>\n");
								}

								Str.append("</td>\n");

								// Discount
								Str.append("<td>");
								Str.append("<input style=\"text-align: right\" type=\"text\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\"");
								Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_basedisc"))))).append("\"");
								Str.append(" class=\"form-control\" size=\"8\" maxlength=\"8\"");
								Str.append(" onkeyup=\"toFloat(this.id, '');CheckBasePrice(").append(configureItem.get("at_group_id")).append(", ").append(configureItem.get("at_groupitemcount"))
										.append(", ");
								Str.append(configureItem.get("at_groupitemcount")).append(", ").append(configureItem.get("at_groupcount")).append(", ").append(configureItem.get("at_group_aftertax"))
										.append(", ");
								Str.append(configureItem.get("at_pricetrans_variable"));
								Str.append(");\"");
								Str.append(" onblur=\"AddATDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '")
										.append(configureItem.get("at_item_id")).append("');\"/>\n");
								Str.append("</td>\n");

								// Amount
								Str.append("<td align=right>\n");
								Str.append("<div id=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_total\">");
								Str.append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_amount"))))));
								Str.append("</div>");

							} else if (configureItem.get("group_name").equals("Additional Discounts")) {
								Str.append("<td align=center>");
								Str.append("<input type=\"checkbox\"");
								Str.append(" id=\"chk_addoff_at_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" name=\"chk_addoff_at_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" " + configureItem.get("at_item_check") + "");
								Str.append(" onclick=\"CalculateTotal(1);\"/>\n");

								// Hidden Fields related to the Item.
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_addoff_at_").append(configureItem.get("at_groupitemcount")).append("_id\"");
								Str.append(" name=\"txt_addoff_at_").append(configureItem.get("at_groupitemcount")).append("_id\"");
								Str.append(" value=\"").append(configureItem.get("at_item_id")).append("\">\n");

								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_addoff_at_gpname\"");
								Str.append(" name=\"txt_addoff_at_gpname\"");
								Str.append(" value=\"").append(configureItem.get("group_name")).append("\">\n");
								Str.append("</td>\n");

								// Item
								Str.append("<td>").append(configureItem.get("at_item_name")).append("</td>\n");

								// Code
								Str.append("<td>").append(configureItem.get("at_item_code")).append("</td>\n");

								// Discount
								Str.append("<td>");

								Str.append("<input style=\"text-align: right\" type=\"text\"");
								Str.append(" id=\"txt_addoff_at_").append(configureItem.get("at_groupitemcount")).append("_amt\"");
								Str.append(" name=\"txt_addoff_at_").append(configureItem.get("at_groupitemcount")).append("_amt\"");
								Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_disc_amt"))))).append("\"");
								Str.append(" class=\"form-control\" size=\"8\" maxlength=\"8\"");
								Str.append(" onkeyup=\"toFloat(this.id, '');CalculateTotal(1);\"/>\n");
								Str.append("</td>\n");
							}

							Str.append("</tr>\n");
						}
					}

				}
				// End After Tax
				Str.append("<tr>\n");
				Str.append("<td colspan=\"6\" align=\"right\">");

				for (Map configureItem : configuredItemList) {
					if ((!configureItem.get("group_name").equals("mainItemDetails")) && configureItem.get("group_name").equals("totalAtItemDetails")) {
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_at_group_count\"");
						Str.append(" name=\"txt_at_group_count\"");
						Str.append(" value=\"").append(configureItem.get("at_group_count")).append("\"/>\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_at_multiselect_basetotal\"");
						Str.append(" name=\"txt_at_multiselect_basetotal\"");
						Str.append(" value=\"").append(df.format(multi_select_basetotal)).append("\"/>\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_at_item_baseprice\"");
						Str.append(" name=\"txt_at_item_baseprice\"");
						Str.append(" value=\"").append(df.format(after_tax_total)).append("\">\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_addoff_at_item_count\"");
						Str.append(" name=\"txt_addoff_at_item_count\"");
						Str.append(" value=\"").append(addoffer_at_item_count).append("\"/>\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_at_allselected_total\"");
						Str.append(" name=\"txt_at_allselected_total\"");
						Str.append(" value=\"").append(df.format(all_selected_total)).append("\"/>\n");
					}
				}
				// On-Road Price
				Str.append("<b>On-Road Price: </b>");
				Str.append("</td>\n");
				Str.append("<td>");
				Str.append("<b><div align=\"right\" id=\"div_total_price\">").append(IndDecimalFormat(df.format(after_tax_total + before_tax_total))).append("</div></b>");
				Str.append("</td>\n");
				Str.append("</tr>\n");
				Str.append("<tr>\n");
				Str.append("<td colspan=\"6\" align=\"right\">");
				Str.append("<b>Total Savings: </b>");
				Str.append("</td>\n");
				Str.append("<td>");
				Str.append("<b><div align=\"right\" id=\"div_total_disc\">").append(IndDecimalFormat(df.format(so_total_disc))).append("</div></b>");
				Str.append("</td>\n");
				Str.append("</tr>\n");
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				Str.append("</div>\n");

				if (configuredItemList != null) {
					for (Map<String, String> m : configuredItemList) {
						if (m.containsKey("group_name") && m.containsValue("itemTotals")) {
							m.put("before_tax_total", df.format(before_tax_total));
							m.put("before_tax_totalwod", df.format(before_tax_totalwod));
							m.put("after_tax_total", df.format(after_tax_total));
							m.put("so_total_disc", df.format(so_total_disc));
							containsMap = true;
							break;
						}
					}
				}
				if (configuredItemList != null && containsMap == false) {
					itemTotals.put("group_name", "itemTotals");
					itemTotals.put("before_tax_total", df.format(before_tax_total));
					itemTotals.put("before_tax_totalwod", df.format(before_tax_totalwod));
					itemTotals.put("after_tax_total", df.format(after_tax_total));
					itemTotals.put("so_total_disc", df.format(so_total_disc));
					configuredItemList.add(itemTotals);
					containsMap = true;
				}

				// This logic adds the Groups in the List.
				HashMap<String, String> groups = new HashMap<String, String>();
				groups.put("group_name", "groupSet");
				int i = 1;
				for (String groupset : groupSet) {
					groups.put("group_" + i++, groupset);
				}
				configuredItemList.add(groups);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		jsonconfiguredItemList = gson.toJson(configuredItemList);
		SOP("Str==with gson==" + gson.toJson(configuredItemList));

		return Str.append("__").append(gson.toJson(configuredItemList)).append("__").append(errormsg).append("__").append(vehstock_comm_no)
				.append(",").append(vehstock_id).append(",").append(item_name).append(",").append(item_id).append(",").append(model_name).append(",").append(model_id).toString();
	}
	public String PopulateModel(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// StrSql = "SELECT model_id, model_name"
			// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
			// + " ORDER BY model_name";
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE model_active = 1"
					+ " AND branch_id = " + branch_id
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("StrSql==11111==" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String model_id, String branch_id,
			String status, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " AND price_rateclass_id = branch_rateclass_id"
					+ " AND price_effective_from <= " + ToLongDate(kknow()) + ""
					+ " AND price_active = 1"
					+ " WHERE item_model_id = " + model_id + "";
			if (status.equals("Add")) {
				StrSql += " AND item_active = 1";
			}
			StrSql += " AND item_model_id != 0"
					+ " AND item_type_id = 1"
					+ " GROUP BY item_id"
					+ " ORDER BY item_name";
			// SOP("StrSql------------item----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"form-control\" onChange=\"GetConfigurationDetails();\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT "
					+ " COALESCE(brandconfig_quote_email_enable, '') AS brandconfig_quote_email_enable,"
					+ " COALESCE(brandconfig_quote_sms_format, '') AS brandconfig_quote_sms_format,"
					+ " COALESCE(brandconfig_quote_sms_enable, '') AS brandconfig_quote_sms_enable,"
					+ " COALESCE(brandconfig_quote_email_sub, '') AS brandconfig_quote_email_sub,"
					+ " COALESCE(brandconfig_quote_email_format, '') AS brandconfig_quote_email_format,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " config_sales_quote_refno, config_customer_dupnames,"
					+ " comp_email_enable, comp_sms_enable,"
					+ " COALESCE(emp.emp_quote_priceupdate, 0) AS emp_quote_priceupdate,"
					+ " COALESCE(emp.emp_quote_discountupdate, 0) AS emp_quote_discountupdate"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id
					+ " WHERE admin.emp_id = " + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("config====" + StrSqlBreaker(StrSql));

			while (crs.next()) {
				brandconfig_quote_email_enable = crs.getString("brandconfig_quote_email_enable");
				brandconfig_quote_email_format = crs.getString("brandconfig_quote_email_format");
				brandconfig_quote_sms_enable = crs.getString("brandconfig_quote_sms_enable");
				brandconfig_quote_sms_format = crs.getString("brandconfig_quote_sms_format");
				brandconfig_quote_email_sub = crs.getString("brandconfig_quote_email_sub");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				config_sales_quote_refno = crs.getString("config_sales_quote_refno");
				emp_quote_priceupdate = crs.getString("emp_quote_priceupdate");
				emp_quote_discountupdate = crs.getString("emp_quote_discountupdate");
				// SOP("emp_quote_discountupdate===" +
				// emp_quote_discountupdate);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateFinOption(String comp_id, String financeoption_name) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT financeoption_id, financeoption_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_financeoption"
					+ " ORDER BY financeoption_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=''>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=\"").append(crs.getString("financeoption_name")).append("\"");
				Str.append(StrSelectdrop(crs.getString("financeoption_name"), financeoption_name));
				Str.append(">").append(crs.getString("financeoption_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateState(String state_id, String span_id, String dr_state_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT state_id, state_name"
					+ " FROM " + compdb(comp_id) + "axela_state"
					+ " ORDER BY state_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_state_id).append(" id=").append(dr_state_id);
			Str.append("class=\"form-control\" onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_contact_city_id','")
					.append(span_id).append("'); \">");
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("state_id"));
				Str.append(StrSelectdrop(crs.getString("state_id"), state_id));
				Str.append(">").append(crs.getString("state_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCity(String state_id, String city_id,
			String dr_city_id) {
		StringBuilder Str = new StringBuilder();
		try {
			if (state_id.equals("")) {
				state_id = "0";
			}
			StrSql = "SELECT city_id, city_name"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE city_state_id = " + state_id + ""
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_city_id).append(" id=").append(dr_city_id).append(" class=\"form-control\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			if (!state_id.equals("0")) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("city_id"));
					Str.append(StrSelectdrop(crs.getString("city_id"), city_id));
					Str.append(">").append(crs.getString("city_name")).append("</option>\n");
				}
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !quote_contact_id.equals("0")) {
				StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname,"
						+ " contact_lname, contact_email1, contact_mobile1, title_desc"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id";
				if (!contact_id.equals("0")) {
					StrSql += " WHERE"
							+ " contact_id = " + contact_id + "";
				} else if (!quote_contact_id.equals("0")) {
					StrSql += " WHERE"
							+ " contact_id = " + quote_contact_id + "";
				}
				// SOP("contact==========="+StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					quote_customer_id = crs.getString("customer_id");
					quote_contact_id = crs.getString("contact_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String PopulateBranch(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, IF(branch_code != '', CONCAT(branch_name, ' (', branch_code, ')'), branch_name) AS branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ " group by branch_id "
					+ " order by branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=\"").append(crs.getString("branch_id"));
				Str.append("\" ").append(Selectdrop(crs.getInt("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateInsuranceCompany(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT inscomp_id, inscomp_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_active = 1" + "" + " GROUP BY inscomp_id"
					+ " ORDER BY inscomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("inscomp_id"));
				Str.append(StrSelectdrop(crs.getString("inscomp_id"), quote_inscomp_id));
				Str.append(">").append(crs.getString("inscomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateExecutive(String comp_id, String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<select name='dr_executive' id='dr_executive' class='form-control'>"
					+ "<option value=\"0\">Select Sales Consultant</option>\n");

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE"
					+ " emp_active = 1"
					+ " AND emp_sales = 1"
					+ " AND (emp_branch_id = " + branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE "
					+ compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + branch_id
					+ "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql==PopulateExecutive==" + StrSql);
			// SOP("quote_emp_id==PopulateExecutive==" + quote_emp_id);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), quote_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		Str.append("</select>");
		return Str.toString();
	}
	// public void message(String msg, String quote_id) throws Exception {
	// SOP("jgjgj");
	// ScriptEngineManager manager = new ScriptEngineManager();
	// ScriptEngine engine = manager.getEngineByName("javascript");
	// String script = "function hello() {print('jjjjjj') }";
	// engine.eval(script);
	// Invocable inv = (Invocable) engine;
	// inv.invokeFunction("hello","");
	// SOP("gfhfgh");
	// }
}
