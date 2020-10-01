package axela.axelaauto_app;
////aJIt 1st December, 2012

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
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

import axela.portal.Header;
import axela.sales.Veh_Quote_Update_New;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class App_Veh_Quote_Update_New extends Connect {

	public String emp_id = "0", emp_branch_id = "0";
	public String add = "";
	// public String update = "";
	public String addB = "", additem = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "", errormsg = "";
	public String msgChk = "";
	public String strHTML = "";
	public String empEditperm = "";
	public String QueryString = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String rateclass_id = "0";
	/* / Quote Variables */
	public String quote_id = "0", vehstock_id = "0";
	public String so_id = "0";
	public String enquiry_enquirytype_id = "0";
	public String vehstock_comm_no = "";
	public String item_name = "", model_name = "";
	public String quote_date = "";
	public String quotedate = "";
	public String quote_netqty = "0";
	public String quote_netamt = "0";
	public String quote_discamt = "";
	public String quote_grandtotal = "0";
	public String quote_exprice = "";
	public String quote_enquiry_id = "0", lead_id = "0";
	public String quote_totaltax = "0";
	public String quote_emp_id = "0";
	public String quote_refno = "0";
	public String quote_active = "0";
	public String quote_notes = "";
	public String quote_auth = "0", quote_auth_id = "0";
	public String quote_auth_date = "", quote_authdate = "";
	public String quote_vehstock_id = "0";
	public String quote_preownedstock_id = "0";
	public String quote_fin_loan1 = "";
	public String quote_fin_loan2 = "";
	public String quote_fin_loan3 = "";
	public String quote_fin_tenure1 = "";
	public String quote_fin_tenure2 = "";
	public String quote_fin_tenure3 = "";
	public String quote_fin_adv_emi1 = "";
	public String quote_fin_adv_emi2 = "";
	public String quote_fin_adv_emi3 = "";
	public String quote_fin_emi1 = "";
	public String quote_fin_emi2 = "";
	public String quote_fin_emi3 = "";
	public String quote_fin_baloonemi1 = "";
	public String quote_fin_baloonemi2 = "";
	public String quote_fin_baloonemi3 = "";
	public String quote_fin_fee1 = "";
	public String quote_fin_fee2 = "";
	public String quote_fin_fee3 = "";
	public String quote_fin_downpayment1 = "";
	public String quote_fin_downpayment2 = "";
	public String quote_fin_downpayment3 = "";
	// public String quote_hypothecation = "";
	// public String quote_fintype_id = "0";
	public String quote_desc = "", quote_terms = "", item_netamountwod = "";
	public String display = "";
	public String quote_entry_id = "0";
	public String quote_entry_by = "";
	public String quote_entry_date = "";
	public String quote_modified_id = "0";
	public String quote_modified_by = "";
	public String quote_modified_date = "";
	/* End Of Quote Variables */
	/* Config Variables */
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String branch_quote_email_enable = "0";
	public String branch_quote_email_format = "";
	public String branch_quote_email_sub = "";
	public String branch_quote_sms_enable = "0";
	public String branch_quote_sms_format = "";
	public String branch_quote_email_exe_sub = "";
	public String branch_quote_email_exe_format = "";
	public String branch_email1 = "";
	public String config_admin_email = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String config_customer_dupnames = "";
	public String config_sales_quote_refno = "";
	public String emp_quote_priceupdate = "";
	public String emp_quote_discountupdate = "";
	public String quote_inscomp_id = "0";
	public String emp_role_id = "0";
	/* End of Config Variables */
	DecimalFormat df = new DecimalFormat("0.00");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String model_id = "0";
	public String contact_id = "0", contact_fname = "", item_id = "0";
	public String quote_contact_id = "0", item_small_desc = "";
	public String quote_customer_id = "0";
	public String customer_id = "0", customer_name = "";
	public String link_customer_name = "";
	public String link_contact_name = "", link_contact_name1 = "";
	public String branch_city_id = "0";
	public String branch_pin = "";
	public String readOnly = "";
	public String addoff_item_check = "";
	public String quoteitem_rowcount = "", quoteitem_item_id = "0";
	public String quoteitem_option_id = "0", quoteitem_option_group = "";
	public String quoteitem_item_serial = "", quoteitem_price = "";
	public String quoteitem_qty = "", quoteitem_disc = "0", quoteitem_tax = "0";
	public String quoteitem_tax_id = "0", quoteitem_tax_rate = "0", quoteitem_total = "0";
	public String item_netprice = "", item_netdisc = "", item_tax_id = "0", item_price = "0";
	public String item_tax1_id = "0", item_tax1_rate = "0";
	public String item_tax2_id = "0", item_tax2_rate = "0";
	public String item_tax3_id = "0", item_tax3_rate = "0";
	public String item_tax4_id = "0", item_tax4_rate = "0";
	public String item_netamount = "0", item_tax1 = "0", item_tax2 = "0", item_tax3 = "0", item_tax4 = "0";

	public String item_tax_rate = "", item_tax = "";
	public String beforetax_gp_count = "", aftertax_gp_count = "";
	public String quote_fin_option1 = "0";
	public String quote_fin_option2 = "0";
	public String quote_fin_option3 = "0";
	public String vehstock_block = "0";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String day = "", month = "", year = "";
	public String gsttype = "", groupName = "";;

	public List<Map> configuredItemList = new ArrayList<Map>();
	public LinkedHashSet<String> groupSet = new LinkedHashSet<String>();
	public Map<String, String> mainItemDetails = new HashMap<String, String>();
	public Map<String, String> groupItemCount = new HashMap<String, String>();
	public Map<String, String> itemTotals = new HashMap<String, String>();// It holds total calculated Amounts i.e Ex-Showroom Price.

	public Map<String, String> totalBtItemDetails = new HashMap<String, String>();
	public Map<String, String> totalAtItemDetails = new HashMap<String, String>();

	public Map<String, String> configuredbtItem;
	public Map<String, String> configuredatItem;
	public boolean conatinsMap = false, containsGroup = false;

	public String jsonconfiguredItemList = "";
	public String itemdetails = "", addItemToList = "";
	public String itemids = "";

	Gson gson = new Gson();
	JSONObject input = new JSONObject();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				BranchAccess = BranchAccess.replace("branch_id", "enquiry_branch_id");
				ExeAccess = GetSession("ExeAccess", request);
				ExeAccess = ExeAccess.replace("emp_id", "enquiry_emp_id");
				add = PadQuotes(request.getParameter("add"));
				additem = PadQuotes(request.getParameter("additem"));
				// update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				quote_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				quote_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				vehstock_comm_no = PadQuotes(request.getParameter("vehstock_comm_no"));
				quote_preownedstock_id = CNumeric(PadQuotes(request.getParameter("preownedstock_id")));
				quote_enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
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

				PopulateContactDetails();
				PopulateConfigDetails();
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					display = "none";
					status = "Add";
					item_id = CNumeric(PadQuotes(request.getParameter("item_id")));
					if (!item_id.equals("0") && enquiry_enquirytype_id.equals("1")) {
						quote_customer_id = CNumeric(ExecuteQuery("SELECT enquiry_customer_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + quote_enquiry_id));
						gsttype = GetGstType(quote_customer_id, branch_id, comp_id);
						StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
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
								+ "model_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
								+ " AND price_rateclass_id = branch_rateclass_id"
								+ " AND price_effective_from <= '" + ToLongDate(kknow()) + "'"
								+ " AND price_active = 1";
						if (gsttype.equals("state")) {
							StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer sgst ON sgst.customer_id = item_salestax1_ledger_id"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cgst ON cgst.customer_id = item_salestax2_ledger_id";
						} else if (gsttype.equals("central")) {
							StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer igst ON igst.customer_id = item_salestax3_ledger_id";
						}
						StrSql += " LEFT JOIN " + compdb(comp_id) + "axela_customer cess ON cess.customer_id = item_salestax4_ledger_id"
								+ " WHERE item_id = " + CNumeric(item_id) + ""
								+ " ORDER BY price_effective_from DESC"
								+ " LIMIT 1";
						// SOP("StrSql=============1111==============" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);
						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								model_id = crs.getString("model_id");
								item_name = crs.getString("item_name");
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
							String msg = "Invalid Item!";
							response.sendRedirect("callurlapp-veh-quote-add.jsp?msg=" + msg + "&enquiry_id=" + quote_enquiry_id);
						}
						crs.close();
					}

					if (!addB.equals("yes")) {
						StrSql = "SELECT branch_quote_desc, branch_quote_terms"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_id = " + branch_id + "";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							quote_desc = crs.getString("branch_quote_desc");
							quote_terms = crs.getString("branch_quote_terms");
						}
						crs.close();
						quote_date = ToLongDate(kknow());
						quotedate = strToShortDate(quote_date);
						quote_active = "1";
					} else {
						String addmsg = "Quote added successfully";
						GetValues(request, response);
						StringBuilder Str = new StringBuilder();
						// if (ReturnPerm("emp_sales_quote_add",
						// request).equals("1")) {
						if (!branch_id.equals("0")) {
							StrSql = "SELECT branch_city_id, branch_pin"
									+ " FROM " + compdb(comp_id) + "axela_branch"
									+ " WHERE branch_id = " + branch_id + "";
							CachedRowSet crs = processQuery(StrSql, 0);

							while (crs.next()) {
								branch_city_id = crs.getString("branch_city_id");
								branch_pin = crs.getString("branch_pin");
							}
							crs.close();
						}

						quote_entry_id = emp_id;
						quote_entry_date = ToLongDate(kknow());
						Veh_Quote_Update_New vqu = new Veh_Quote_Update_New();
						vqu.branch_id = branch_id;
						vqu.quote_date = quote_date;
						vqu.quote_customer_id = quote_customer_id;
						vqu.quote_contact_id = quote_contact_id;
						vqu.lead_id = lead_id;
						vqu.quote_enquiry_id = quote_enquiry_id;
						vqu.item_id = item_id;
						vqu.quote_netamt = quote_netamt;
						vqu.quote_discamt = quote_discamt;
						vqu.quote_totaltax = quote_totaltax;
						vqu.quote_grandtotal = quote_grandtotal;
						vqu.quote_vehstock_id = quote_vehstock_id;
						vqu.quote_fin_option1 = quote_fin_option1;
						vqu.quote_fin_loan1 = quote_fin_loan1;
						vqu.quote_fin_loan2 = quote_fin_loan2;
						vqu.quote_fin_loan3 = quote_fin_loan3;
						vqu.quote_fin_tenure1 = quote_fin_tenure1;
						vqu.quote_fin_tenure2 = quote_fin_tenure2;
						vqu.quote_fin_tenure3 = quote_fin_tenure3;
						vqu.quote_fin_adv_emi1 = quote_fin_adv_emi1;
						vqu.quote_fin_adv_emi2 = quote_fin_adv_emi2;
						vqu.quote_fin_adv_emi3 = quote_fin_adv_emi3;
						vqu.quote_fin_emi1 = quote_fin_emi1;
						vqu.quote_fin_emi2 = quote_fin_emi2;
						vqu.quote_fin_emi3 = quote_fin_emi3;
						vqu.quote_fin_baloonemi1 = quote_fin_baloonemi1;
						vqu.quote_fin_baloonemi2 = quote_fin_baloonemi2;
						vqu.quote_fin_baloonemi3 = quote_fin_baloonemi3;
						vqu.quote_fin_fee1 = quote_fin_fee1;
						vqu.quote_fin_fee2 = quote_fin_fee2;
						vqu.quote_fin_fee3 = quote_fin_fee3;
						vqu.quote_fin_downpayment1 = quote_fin_downpayment1;
						vqu.quote_fin_option2 = quote_fin_option2;
						vqu.quote_fin_downpayment2 = quote_fin_downpayment2;
						vqu.quote_fin_option3 = quote_fin_option3;
						vqu.quote_fin_downpayment3 = quote_fin_downpayment3;
						vqu.quote_desc = quote_desc;
						vqu.quote_terms = quote_terms;
						vqu.quote_inscomp_id = quote_inscomp_id;
						vqu.quote_emp_id = quote_emp_id;
						vqu.quote_refno = quote_refno;
						vqu.quote_auth = quote_auth;
						vqu.quote_active = quote_active;
						vqu.quote_notes = quote_notes;
						vqu.quote_entry_id = emp_id;
						vqu.quote_entry_date = quote_entry_date;
						vqu.quotedate = quotedate;
						vqu.quote_vehstock_id = quote_vehstock_id;
						vqu.config_sales_quote_refno = config_sales_quote_refno;
						vqu.quote_refno = quote_refno;
						vqu.comp_id = comp_id;
						vqu.quote_id = quote_id;
						vqu.quoteitem_rowcount = quoteitem_rowcount;
						vqu.item_id = item_id;
						vqu.item_price = item_price;
						vqu.quoteitem_disc = quoteitem_disc;
						vqu.quoteitem_tax = quoteitem_tax;
						vqu.quoteitem_tax_id = quoteitem_tax_id;
						vqu.quoteitem_tax_rate = quoteitem_tax_rate;
						vqu.quoteitem_total = quoteitem_total;
						vqu.beforetax_gp_count = beforetax_gp_count;
						vqu.aftertax_gp_count = aftertax_gp_count;
						vqu.quote_preownedstock_id = quote_preownedstock_id;
						vqu.app = "yes";
						vqu.AddFields(request, null);
						if (vqu.msg.equals("")) {
							msg = addmsg;
							response.sendRedirect(response.encodeRedirectURL("callurlapp-veh-quote-list.jsp?quote_id=" + vqu.quote_id + "&enquiry_id=" + vqu.quote_enquiry_id + "&msg=" + addmsg));
						} else if (!vqu.msg.equals("")) {
							msg = "Error!" + unescapehtml(vqu.msg);
						}
					}
				}

				// else if (update.equals("yes")) {
				// status = "Update";
				// if (!updateB.equals("yes") &&
				// !deleteB.equals("Delete Quote")) {
				// PopulateFields(request, response);
				// contact_id = quote_contact_id;
				// } else if (updateB.equals("yes") &&
				// !deleteB.equals("Delete Quote")) {
				// GetValues(request, response);
				// if (ReturnPerm(comp_id, "emp_sales_quote_edit",
				// request).equals("1")) {
				// quote_modified_id = emp_id;
				// quote_modified_date = ToLongDate(kknow());
				// UpdateFields(request, response);
				// if (!msg.equals("")) {
				// msg = "Error!" + msg;
				// } else {
				// response.sendRedirect(response
				// .encodeRedirectURL("app-veh-quote-list.jsp?quote_id=" +
				// quote_id
				// + "&msg=Quote updated successfully!"
				// + msg + ""));
				// }
				// } else {
				// response.sendRedirect(AccessDenied());
				// }
				// } else if (deleteB.equals("Delete Quote")) {
				// GetValues(request, response);
				// if (ReturnPerm(comp_id, "emp_sales_quote_delete", request)
				// .equals("1")) {
				// DeleteFields(response);
				// if (!msg.equals("")) {
				// msg = "Error!" + msg;
				// } else {
				// response.sendRedirect(response
				// .encodeRedirectURL("app-veh-quote-list.jsp?msg=Quote deleted successfully!"));
				// }
				// } else {
				// response.sendRedirect(AccessDenied());
				// }
				// }
				// }

			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		enquiry_enquirytype_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_enquirytype_id")));
		lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		item_id = PadQuotes(request.getParameter("dr_item_id"));
		so_id = CNumeric(PadQuotes(request.getParameter("txt_so_id")));
		vehstock_block = CNumeric(PadQuotes(request.getParameter("txt_vehstock_block")));
		item_id = PadQuotes(request.getParameter("dr_item_id"));
		item_name = PadQuotes(request.getParameter("txt_item_name"));
		model_id = CNumeric(PadQuotes(request.getParameter("dr_model_id")));
		model_name = PadQuotes(request.getParameter("txt_model_name"));
		item_small_desc = unescapehtml(PadQuotes(request.getParameter("txt_item_small_desc")));
		item_price = PadQuotes(request.getParameter("txt_item_priceamt"));
		item_netdisc = PadQuotes(request.getParameter("txt_item_netdisc"));
		item_netprice = PadQuotes(request.getParameter("txt_item_price"));
		item_tax_id = CNumeric(PadQuotes(request.getParameter("txt_item_tax_id")));
		item_tax_rate = PadQuotes(request.getParameter("txt_item_tax_rate"));
		quote_auth = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_auth")));
		quote_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_quote_vehstock_id")));
		quote_inscomp_id = CNumeric(PadQuotes(request.getParameter("dr_quote_inscomp_id")));
		// vehstock_comm_no = PadQuotes(request.getParameter("txt_stock_comm_no"));

		if (quote_vehstock_id.equals("0")) {
			quote_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_quote_vehstock_id")));
		}
		quote_preownedstock_id = CNumeric(PadQuotes(request.getParameter("txt_quote_preownedstock_id")));
		// quote_hypothecation =
		// PadQuotes(request.getParameter("txt_quote_hypothecation"));
		// quote_fintype_id =
		// CNumeric(PadQuotes(request.getParameter("dr_quote_fintype")));
		quote_desc = PadQuotes(request.getParameter("txt_quote_desc"));
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
		quote_terms = PadQuotes(request.getParameter("txt_quote_terms"));
		quotedate = PadQuotes(request.getParameter("txt_quote_date"));
		quote_netqty = CNumeric(PadQuotes(request.getParameter("txt_quote_qty")));
		quote_netamt = CNumeric(PadQuotes(request.getParameter("txt_quote_netamt")));
		quote_discamt = CNumeric(PadQuotes(request.getParameter("txt_quote_discamt")));
		quote_totaltax = CNumeric(PadQuotes(request.getParameter("txt_quote_totaltax")));
		quote_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_quote_grandtotal")));
		// quote_refno = PadQuotes(request.getParameter("txt_quote_refno"));
		quote_active = CheckBoxValue(PadQuotes(request.getParameter("chk_quote_active")));
		// quote_enquiry_id =
		// CNumeric(PadQuotes(request.getParameter("txt_enquiry_id")));
		quote_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		quote_notes = PadQuotes(request.getParameter("txt_quote_notes"));
		quote_fin_option1 = PadQuotes(request.getParameter("dr_quote_fin_option1"));
		quote_fin_option2 = PadQuotes(request.getParameter("dr_quote_fin_option2"));
		quote_fin_option3 = PadQuotes(request.getParameter("dr_quote_fin_option3"));
		quote_entry_by = PadQuotes(request.getParameter("entry_by"));
		quote_modified_by = PadQuotes(request.getParameter("modified_by"));
		quote_entry_date = PadQuotes(request.getParameter("entry_date"));
		quote_modified_date = PadQuotes(request.getParameter("modified_date"));
		beforetax_gp_count = PadQuotes(request.getParameter("txt_bt_group_count"));
		aftertax_gp_count = PadQuotes(request.getParameter("txt_at_group_count"));
		PopulateContactDetails();
	}

	protected void CheckForm() {
		msg = "";
		if (branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}
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
			msg += "<br>Select the Variant!";
		}
		if (config_sales_quote_refno.equals("1")) {
			if (quote_refno.equals("")) {
				msg += "<br>Enter Quote Reference No.!";
			} else {
				if (quote_refno.length() < 2) {
					msg += "<br>Quote Reference No. should be atleast Two Digits!";
				}
				if (!branch_id.equals("0")) {
					StrSql = "SELECT quote_refno FROM " + compdb(comp_id) + "axela_sales_quote"
							+ " WHERE quote_branch_id = " + branch_id + ""
							+ " AND quote_refno = '" + quote_refno + "'";
					// if (update.equals("yes")) {
					// StrSql += " AND quote_id != " + quote_id + "";
					// }
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
		if (!vehstock_comm_no.equals("") && !vehstock_comm_no.equals("0")) {
			StrSql = "SELECT vehstock_id"
					+ " FROM " + compdb(comp_id) + "axela_vehstock"
					+ " WHERE vehstock_comm_no = '" + vehstock_comm_no + "'";
			if (ExecuteQuery(StrSql).equals("")) {
				msg += "<br>Invalid Stock!";
			} else {
				StrSql = "SELECT vehstock_item_id"
						+ " FROM " + compdb(comp_id) + "axela_vehstock"
						+ " WHERE vehstock_comm_no = '" + vehstock_comm_no + "'";
				if (!ExecuteQuery(StrSql).equals(item_id)) {
					msg += "<br>Invalid Stock Comm. No.!";
				}
			}
			StrSql = "SELECT vehstock_comm_no FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " WHERE vehstock_comm_no = '" + vehstock_comm_no + "'"
					+ " AND so_active = 1"
					+ " AND quote_id != " + quote_id + "";
			if (ExecuteQuery(StrSql).equals(vehstock_comm_no)) {
				msg += "<br>Stock Comm. No. is associated with other Sales Order!";
			}
		} else if (vehstock_comm_no.equals("") || vehstock_comm_no.equals("0")) {
			quote_vehstock_id = "0";
		}
		if (!quote_preownedstock_id.equals("0")) {
			StrSql = "SELECT so_preownedstock_id FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_preownedstock_id = " + quote_preownedstock_id + ""
					+ " AND so_active = '1'";
			if (ExecuteQuery(StrSql).equals(quote_preownedstock_id)) {
				msg += "<br>Pre-Owned Stock is associated with other Sales Order!";
			}
		}
		// if (!quote_fin_loan1.equals("") && quote_fin_option1.equals("")) {
		// msg += "<br>Select Finance Option 1!";
		// } else if (quote_fin_loan1.equals("") &&
		// !quote_fin_option1.equals("")) {
		// msg += "<br>Enter Loan Amount for Option 1!";
		// }
		// if (!quote_fin_loan2.equals("") && quote_fin_option2.equals("")) {
		// msg += "<br>Select Finance Option 2!";
		// } else if (quote_fin_loan2.equals("") &&
		// !quote_fin_option2.equals("")) {
		// msg += "<br>Enter Loan Amount for Option 2!";
		// }
		// if (!quote_fin_loan3.equals("") && quote_fin_option3.equals("")) {
		// msg += "<br>Select Finance Option 3!";
		// } else if (quote_fin_loan3.equals("") &&
		// !quote_fin_option3.equals("")) {
		// msg += "<br>Enter Loan Amount for Option 3!";
		// }
		// if (quote_fintype_id.equals("0")) {
		// msg += "<br>Select Finance Type!";
		// }
		if (quote_emp_id.equals("0")) {
			msg += "<br>Select Sales Consultant!";
		}
		msg = msgChk + msg;
	}

	public void GetEnquiryDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT enquiry_emp_id,"
					// + " COALESCE(lead_id, 0) AS lead_id,"
					+ " customer_id, customer_name,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, contact_fname,"
					+ " contact_mobile1, contact_email1, state_name, city_name, contact_id,"
					+ " customer_address, customer_pin, enquiry_branch_id, rateclass_id, enquiry_enquirytype_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, item_name, model_name"
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
					// + " LEFT JOIN " + compdb(comp_id) +
					// "axela_mktg_lead ON lead_id = enquiry_lead_id"
					+ " WHERE enquiry_id = " + CNumeric(quote_enquiry_id) + ""
					+ " GROUP BY enquiry_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					item_name = crs.getString("item_name");
					model_name = crs.getString("model_name");
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					quote_emp_id = crs.getString("enquiry_emp_id");
					customer_id = crs.getString("customer_id");
					customer_name = ExecuteQuery("SELECT customer_name"
							+ " FROM " + compdb(comp_id) + "axela_customer"
							+ " WHERE customer_id=" + customer_id + "");
					customer_name = customer_name + " " + "(" + customer_id + ")";
					// lead_id = crs.getString("lead_id");
					link_customer_name = crs.getString("customer_name");
					contact_id = crs.getString("contact_id");
					link_contact_name = crs.getString("contactname");
					link_contact_name1 = link_contact_name + " " + "(" + contact_id + ")";
					branch_id = CNumeric(crs.getString("enquiry_branch_id"));
					rateclass_id = CNumeric(crs.getString("rateclass_id"));
					branch_name = crs.getString("branchname");
				}
			} else {
				// response.sendRedirect(response
				// .encodeRedirectURL("../portal/app-error.jsp?msg=Invalid Opportunity!"));
			}
			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	// protected void AddFields(HttpServletRequest request) throws Exception {
	// // CheckForm();
	// if (isValidDateFormatShort(quotedate)) {
	// quote_date = ConvertShortDateToStr(quotedate);
	// } else {
	// msg += "<br>Enter Valid Quote Date!";
	// }
	// if (msg.equals("")) {
	// try {
	// conntx = connectDB();
	// conntx.setAutoCommit(false);
	// stmttx = conntx.createStatement();
	// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote" + " (quote_branch_id,"
	// + " quote_no," + " quote_date," + " quote_customer_id," + " quote_contact_id,";
	// if (!quote_enquiry_id.equals("0")) {
	// StrSql += " quote_lead_id," + " quote_enquiry_id,";
	// }
	// StrSql += " quote_item_id,"
	// + "	quote_netamt,"
	// + " quote_discamt,"
	// + " quote_totaltax,"
	// + " quote_grandtotal,"
	// + " quote_vehstock_id,"
	// + " quote_preownedvehstock_id,"
	// + " quote_fin_option1,"
	// + " quote_fin_loan1,"
	// + " quote_fin_loan2,"
	// + " quote_fin_loan3,"
	// + " quote_fin_tenure1,"
	// + " quote_fin_tenure2,"
	// + " quote_fin_tenure3,"
	// + " quote_fin_adv_emi1,"
	// + " quote_fin_adv_emi2,"
	// + " quote_fin_adv_emi3,"
	// + " quote_fin_emi1,"
	// + " quote_fin_emi2,"
	// + " quote_fin_emi3,"
	// + " quote_fin_baloonemi1,"
	// + " quote_fin_baloonemi2,"
	// + " quote_fin_baloonemi3,"
	// + " quote_fin_fee1,"
	// + " quote_fin_fee2,"
	// + " quote_fin_fee3,"
	// + " quote_fin_downpayment1,"
	// + " quote_fin_option2,"
	// + " quote_fin_downpayment2,"
	// + " quote_fin_option3,"
	// + " quote_fin_downpayment3,"
	// // + " quote_hypothecation,"
	// // + " quote_fintype_id,"
	// + " quote_desc,"
	// + " quote_terms,"
	// + "quote_inscomp_id,"
	// + " quote_emp_id,";
	// if (config_sales_quote_refno.equals("1")) {
	// StrSql += " quote_refno,";
	// }
	// StrSql += " quote_auth,"
	// + " quote_active,"
	// + " quote_notes,"
	// + " quote_entry_id,"
	// + " quote_entry_date)"
	// + " VALUES"
	// + " (" + branch_id + ","
	// + " (COALESCE((SELECT MAX(quote.quote_no)"
	// + " FROM " + compdb(comp_id) + "axela_sales_quote AS quote"
	// + " WHERE quote.quote_branch_id = " + branch_id + "), 0) + 1),"
	// + " '" + quote_date + "'," + " "
	// + quote_customer_id + ","
	// + quote_contact_id + ",";
	// if (!quote_enquiry_id.equals("0")) {
	// StrSql += " " + lead_id + ","
	// + quote_enquiry_id + ",";
	// }
	// StrSql += " " + item_id + ","
	// + " " + quote_netamt + ","
	// + " " + quote_discamt + ","
	// + " " + quote_totaltax + "," + " "
	// + Math.ceil(Double.parseDouble(quote_grandtotal)) + ","
	// + " " + CNumeric(quote_vehstock_id) + "," + " "
	// + CNumeric(quote_preownedvehstock_id) + "," + " '"
	// + quote_fin_option1 + "',"
	// + " '" + quote_fin_loan1 + "',"
	// + " '" + quote_fin_loan2 + "'," + " '"
	// + quote_fin_loan3 + "',"
	// + " '" + quote_fin_tenure1 + "',"
	// + " '" + quote_fin_tenure2 + "',"
	// + " '" + quote_fin_tenure3 + "',"
	// + " '" + quote_fin_adv_emi1 + "',"
	// + " '" + quote_fin_adv_emi2 + "',"
	// + " '" + quote_fin_adv_emi3 + "',"
	// + " '" + quote_fin_emi1 + "',"
	// + " '" + quote_fin_emi2 + "',"
	// + " '" + quote_fin_emi3 + "',"
	// + " '" + quote_fin_baloonemi1 + "',"
	// + " '" + quote_fin_baloonemi2 + "',"
	// + " '" + quote_fin_baloonemi3 + "',"
	// + " '" + quote_fin_fee1 + "',"
	// + " '" + quote_fin_fee2 + "'," + " '"
	// + quote_fin_fee3 + "',"
	// + " '" + quote_fin_downpayment1 + "',"
	// + " '" + quote_fin_option2 + "'," + " '"
	// + quote_fin_downpayment2 + "',"
	// + " '" + quote_fin_option3 + "',"
	// + " '" + quote_fin_downpayment3 + "',"
	// // + " '" + quote_hypothecation + "',"
	// // + " " + quote_fintype_id + ","
	// + " '" + quote_desc + "',"
	// + " '" + quote_terms + "',"
	// + quote_inscomp_id + ", "
	// + quote_emp_id + ",";
	// if (config_sales_quote_refno.equals("1")) {
	// StrSql += " '" + quote_refno + "',";
	// }
	// StrSql += " '" + quote_auth + "'," + " '" + quote_active + "',"
	// + " '" + quote_notes + "'," + " " + quote_entry_id
	// + "," + " '" + quote_entry_date + "')";
	// stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
	// // SOP("StrSql---11----" + StrSql);
	// ResultSet rs1 = stmttx.getGeneratedKeys();
	// while (rs1.next()) {
	// quote_id = rs1.getString(1);
	// }
	// rs1.close();
	// // Adding the configured items into Quote-Item table
	// AddItemFields(request);
	//
	// // For updating the Quote total feilds
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote" + " SET"
	// + " quote_netamt = " + quote_netamt + ","
	// + " quote_discamt = " + quote_discamt + ","
	// + " quote_exprice = " + quote_exprice + ","
	// + " quote_totaltax = " + quote_totaltax + ","
	// + " quote_grandtotal = "
	// + Math.ceil(Double.parseDouble(quote_grandtotal)) + ""
	// + " WHERE quote_id = " + quote_id + "";
	// // SOP("axela_sales_quote=====update==========" + StrSql);
	// stmttx.execute(StrSql);
	//
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
	// + " SET"
	// + " enquiry_stage_id = 4"
	// + " WHERE enquiry_id = "
	// + quote_enquiry_id + "";
	// // SOP("axela_sales_enquiry=====update==========" + StrSql);
	// stmttx.execute(StrSql);
	// conntx.commit();
	// // SetOpprPriority(quote_enquiry_id);
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// SOPError("Connection is closed.....");
	// }
	//
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// SOPError("Connection rollback...");
	// }
	// msg = "<br>Transaction Error!";
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
	// } finally {
	// conntx.setAutoCommit(true);
	// if (stmttx != null && !stmttx.isClosed()) {
	// stmttx.close();
	// }
	// if (conntx != null && !conntx.isClosed()) {
	// conntx.close();
	// }
	// }
	// }
	// }

	// protected void AddItemFields(HttpServletRequest request) throws SQLException {
	// try {
	// quote_netamt = "0";
	// quote_discamt = "0";
	// quote_totaltax = "0";
	// quote_grandtotal = "0";
	// quote_exprice = "0";
	// quoteitem_rowcount = CNumeric(PadQuotes(ExecuteQuery("SELECT COALESCE(MAX(quoteitem_rowcount), 0) + 1"
	// + " FROM " + compdb(comp_id) + "axela_sales_quote_item")));
	// quote_desc = PadQuotes(request.getParameter("txt_item_desc"));
	// quoteitem_disc = CNumeric(PadQuotes(request.getParameter("txt_item_disc")));
	// quoteitem_tax_id = CNumeric(PadQuotes(request.getParameter("txt_item_tax_id")));
	// quoteitem_tax_rate = CNumeric(PadQuotes(request.getParameter("txt_item_tax_rate")));
	// quoteitem_total = CNumeric(PadQuotes(request.getParameter("div_main_item_amount")));
	// quoteitem_tax = Double.toString((Double.parseDouble(item_price) - Double.parseDouble(quoteitem_disc)) * Double.parseDouble(quoteitem_tax_rate) / 100);
	// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // To
	// // Insert
	// // Main-Item
	//
	// + " (quoteitem_quote_id,"
	// + " quoteitem_rowcount,"
	// + " quoteitem_item_id,"
	// + " quoteitem_option_id,"
	// // + " quoteitem_desc,"
	// + " quoteitem_option_group,"
	// + " quoteitem_option_group_tax,"
	// + " quoteitem_item_serial,"
	// + " quoteitem_qty,"
	// + " quoteitem_price,"
	// + " quoteitem_disc,"
	// + " quoteitem_tax,"
	// + " quoteitem_tax_id,"
	// + " quoteitem_tax_rate," + " quoteitem_total)"
	// + " VALUES" + " (" + quote_id + "," + " "
	// + quoteitem_rowcount + ","
	// + " " + item_id + ","
	// + " 0,"
	// // + " '" + item_small_desc + "',"
	// + " '',"
	// + " '1',"
	// + " '',"
	// + " 1,"
	// + " "
	// + item_price
	// + ","
	// + " "
	// + quoteitem_disc
	// + ","
	// + " "
	// + quoteitem_tax
	// + ","
	// + " "
	// + quoteitem_tax_id
	// + ","
	// + " "
	// + quoteitem_tax_rate + "," + " " + quoteitem_total + ")";
	// // SOPInfo("StrSql------aaaaaaa-----" + StrSql);
	// stmttx.addBatch(StrSql);
	// quote_netamt = Double.toString(Double.parseDouble(quote_netamt) + Double.parseDouble(item_price));
	// quote_discamt = Double.toString(Double.parseDouble(quote_discamt) + Double.parseDouble(quoteitem_disc));
	// quote_totaltax = Double.toString(Double.parseDouble(quote_totaltax) + Double.parseDouble(quoteitem_tax));
	// quote_grandtotal = Double.toString(Double.parseDouble(quote_grandtotal) + Double.parseDouble(quoteitem_total));
	// quote_exprice = Double.toString(Double.parseDouble(quote_exprice) + Double.parseDouble(quoteitem_total));
	// String group_count = "0", gp_item_count = "0", tax = "0", quoteitem_option_group_tax = "0";
	// String check = "", gp_name = "", addoff_gp_name = "";
	// int addoff_item_count = 0;
	// quoteitem_option_id = quoteitem_rowcount;
	//
	// for (int x = 1; x <= 2; x++) {
	// if (x == 1) {
	//
	// group_count = beforetax_gp_count;
	// tax = "bt";
	// quoteitem_option_group_tax = "1";
	// } else if (x == 2) {
	// group_count = aftertax_gp_count;
	// tax = "at";
	// quoteitem_option_group_tax = "2";
	// }
	// if (!group_count.equals("0") && !group_count.equals("")) {
	// for (int i = 1; i <= Integer.parseInt(group_count); i++) {
	// gp_item_count = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_count"));
	// gp_name = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_gpname"));
	//
	// for (int j = 1; j <= Integer.parseInt(gp_item_count); j++) {
	// check = PadQuotes(request.getParameter("chk_" + tax + "_" + i + "_" + j));
	// if (check.equals("on")) {
	// quoteitem_item_id = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_id")));
	// quoteitem_option_group = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_option_group"));
	// quoteitem_item_serial = PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_serial"));
	// quoteitem_price = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_baseprice")));
	// quoteitem_qty = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_qty")));
	// quoteitem_disc = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_disc")));
	// quoteitem_tax_id = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_tax_id")));
	// quoteitem_tax_rate = CNumeric(PadQuotes(request.getParameter("txt_" + tax + "_" + i + "_" + j + "_tax_rate")));
	// quoteitem_total = CNumeric(PadQuotes(request.getParameter("div_" + tax + "_" + i + "_" + j + "_amount")));
	// quoteitem_tax = Double.toString((Double.parseDouble(quoteitem_price) - Double.parseDouble(quoteitem_disc)) * Double.parseDouble(quoteitem_tax_rate) / 100);
	// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // To
	// // insert
	// // configured
	// // items
	// + " (quoteitem_quote_id,"
	// + " quoteitem_rowcount,"
	// + " quoteitem_item_id,"
	// + " quoteitem_option_id,"
	// + " quoteitem_option_group,"
	// + " quoteitem_option_group_tax,"
	// + " quoteitem_item_serial,"
	// + " quoteitem_qty,"
	// + " quoteitem_price,"
	// + " quoteitem_disc,"
	// + " quoteitem_tax,"
	// + " quoteitem_tax_id,"
	// + " quoteitem_tax_rate,"
	// + " quoteitem_total)"
	// + " VALUES" + " (" + quote_id + ","
	// + " 0,"
	// + " "
	// + quoteitem_item_id
	// + ","
	// + " "
	// + quoteitem_option_id
	// + ","
	// + " '"
	// + quoteitem_option_group
	// + "',"
	// + " '"
	// + quoteitem_option_group_tax
	// + "',"
	// + " '"
	// + quoteitem_item_serial
	// + "',"
	// + " "
	// + quoteitem_qty
	// + ","
	// + " "
	// + quoteitem_price
	// + ","
	// + " "
	// + quoteitem_disc
	// + ","
	// + " "
	// + quoteitem_tax
	// + ","
	// + " "
	// + quoteitem_tax_id
	// + ","
	// + " "
	// + quoteitem_tax_rate
	// + ","
	// + " "
	// + quoteitem_total + ")";
	// // StrSqlBreaker(StrSql));
	// // SOPInfo("StrSql------bbbbbbbbb-----" + StrSqlBreaker(StrSql));
	// stmttx.addBatch(StrSql);
	//
	// quote_netamt = Double.toString(Double.parseDouble(quote_netamt) + Double.parseDouble(quoteitem_price));
	// quote_discamt = Double.toString(Double.parseDouble(quote_discamt) + Double.parseDouble(quoteitem_disc));
	// quote_totaltax = Double.toString(Double.parseDouble(quote_totaltax) + Double.parseDouble(quoteitem_tax));
	// quote_grandtotal = Double.toString(Double.parseDouble(quote_grandtotal) + Double.parseDouble(quoteitem_total));
	// if (quoteitem_option_group_tax.equals("1")) {
	// quote_exprice = Double.toString(Double.parseDouble(quote_exprice) + Double.parseDouble(quoteitem_total));
	// }
	// }
	// }
	// }
	// }
	// }
	// // To insert additional offers into quote_item table
	// addoff_gp_name = PadQuotes(request.getParameter("txt_addoff_gpname"));
	// if (!addoff_gp_name.equals("")) {
	// addoff_item_count = Integer.parseInt(CNumeric(PadQuotes(request
	// .getParameter("txt_addoff_item_count"))));
	// for (int j = 1; j <= addoff_item_count; j++) {
	// addoff_item_check = PadQuotes(request.getParameter("chk_addoff_" + j));
	// quoteitem_item_id = CNumeric(PadQuotes(request.getParameter("txt_addoff_" + j + "_id")));
	// quoteitem_item_serial = "";
	// quoteitem_price = "0";
	// quoteitem_qty = "1";
	// quoteitem_disc = CNumeric(PadQuotes(request.getParameter("txt_addoff_" + j + "_amt")));
	// quoteitem_tax_id = "0";
	// quoteitem_tax_rate = "0";
	// quoteitem_total = "-" + quoteitem_disc;
	// quoteitem_tax = "0";
	//
	// if (addoff_item_check.equals("on")) {
	// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_quote_item" // To
	// // insert
	// // additional
	// // offers
	// + " (quoteitem_quote_id,"
	// + " quoteitem_rowcount,"
	// + " quoteitem_item_id,"
	// + " quoteitem_option_id,"
	// + " quoteitem_option_group,"
	// + " quoteitem_option_group_tax,"
	// + " quoteitem_item_serial,"
	// + " quoteitem_qty,"
	// + " quoteitem_price,"
	// + " quoteitem_disc,"
	// + " quoteitem_tax,"
	// + " quoteitem_tax_id,"
	// + " quoteitem_tax_rate,"
	// + " quoteitem_total)"
	// + " VALUES" + " ("
	// + quote_id
	// + ","
	// + " 0,"
	// + " "
	// + quoteitem_item_id
	// + ","
	// + " "
	// + quoteitem_option_id
	// + ","
	// + " 'Additional Discounts',"
	// + " '2',"
	// + " '"
	// + quoteitem_item_serial
	// + "',"
	// + " "
	// + quoteitem_qty
	// + ","
	// + " "
	// + quoteitem_price
	// + ","
	// + " "
	// + quoteitem_disc
	// + ","
	// + " "
	// + quoteitem_tax
	// + ","
	// + " "
	// + quoteitem_tax_id
	// + ","
	// + " "
	// + quoteitem_tax_rate
	// + ","
	// + " "
	// + quoteitem_total + ")";
	// // SOPInfo("StrSql------ccccccccc-----" + StrSqlBreaker(StrSql));
	// stmttx.addBatch(StrSql);
	//
	// quote_netamt = Double.toString(Double.parseDouble(quote_netamt) + Double.parseDouble(quoteitem_price));
	// quote_discamt = Double.toString(Double.parseDouble(quote_discamt) + Double.parseDouble(quoteitem_disc));
	// quote_totaltax = Double.toString(Double.parseDouble(quote_totaltax) + Double.parseDouble(quoteitem_tax));
	// quote_grandtotal = Double.toString(Double.parseDouble(quote_grandtotal) + Double.parseDouble(quoteitem_total));
	// // SOP("StrSql----1111111----" + StrSql);
	// }
	// }
	// }
	// // SOPInfo("StrSql----final----" + StrSql);
	// stmttx.executeBatch();
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// SOPError("Connection is closed...");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// SOPError("Connection rollback...");
	// }
	// msg = "<br>Transaction Error!";
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
	// }
	// }
	protected void AddSOItemFields(String so_quote_id, String so_id)
			throws SQLException {
		try {
			StrSql = "SELECT" + " " + so_id + "," + " quoteitem_rowcount,"
					+ " quoteitem_item_id," + " quoteitem_option_id,"
					+ " quoteitem_desc," + " quoteitem_option_group,"
					+ " quoteitem_option_group_tax,"
					+ " quoteitem_item_serial," + " quoteitem_qty,"
					+ " quoteitem_price," + " quoteitem_disc,"
					+ " quoteitem_tax," + " quoteitem_tax_id,"
					+ " quoteitem_tax_rate," + " quoteitem_total"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " WHERE quoteitem_quote_id = " + so_quote_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_item" + " (soitem_so_id,"
					+ " soitem_rowcount," + " soitem_item_id,"
					+ " soitem_option_id," + " soitem_desc,"
					+ " soitem_option_group," + " soitem_option_group_tax,"
					+ " soitem_item_serial," + " soitem_qty,"
					+ " soitem_price," + " soitem_disc," + " soitem_tax,"
					+ " soitem_tax_id," + " soitem_tax_rate,"
					+ " soitem_total)" + " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("Connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("Connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	// protected void PopulateFields(HttpServletRequest request,
	// HttpServletResponse response) {
	// try {
	// StrSql =
	// "SELECT axela_sales_quote.*, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
	// +
	// " contact_lname, contact_fname, title_id, title_desc, contact_mobile1,"
	// +
	// " contact_phone1, contact_email1, customer_name, customer_id, contact_id, quoteitem_item_id,"
	// +
	// " quoteitem_price, quoteitem_disc, quoteitem_tax_id, quoteitem_tax_rate, item_small_desc,"
	// +
	// // " IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
	// // + " COALESCE(stock_comm_no, '') AS vehstock_comm_no, model_id, model_name,"
	// // +
	// " COALESCE(quote_so.so_id, 0) AS so_id, COALESCE(stock_so.so_id, 0) AS vehstock_so_id ,"
	// + " enquiry_enquirytype_id, quote_preownedvehstock_id"
	// + " FROM " + compdb(comp_id) + "axela_sales_quote"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item ON item_id = quoteitem_item_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_inventory_item_model ON model_id = item_model_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_branch on branch_id = quote_branch_id"
	// // + " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = rate_rateclass_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_customer ON customer_id = contact_customer_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_title ON title_id = contact_title_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = quote_emp_id"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_vehstock ON vehstock_id = quote_vehstock_id"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_sales_so quote_so ON quote_so.so_quote_id = quote_id"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_sales_so vehstock_so ON vehstock_so.so_quote_id = quote_id"
	// + " AND vehstock_so.so_active = 1"
	// + " AND vehstock_so.so_delivered_date != ''"
	// + " WHERE quote_id = " + quote_id + ""
	// + " AND quoteitem_rowcount != 0" + BranchAccess + "";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// if (crs.isBeforeFirst()) {
	// while (crs.next()) {
	// enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
	// quote_id = crs.getString("quote_id");
	// so_id = crs.getString("so_id");
	// if (!crs.getString("vehstock_so_id").equals("0")) {
	// vehstock_block = "1";
	// }
	// branch_id = crs.getString("quote_branch_id");
	// branch_name = crs.getString("branch_name");
	// quote_contact_id = crs.getString("quote_contact_id");
	// link_customer_name = crs.getString("customer_name");
	// link_contact_name = crs.getString("contact_fname") + " " +
	// crs.getString("contact_lname");
	// // rateclass_id = crs.getString("rateclass_id");
	// item_name = crs.getString("item_name");
	// item_id = crs.getString("quoteitem_item_id");
	// model_id = crs.getString("model_id");
	// model_name = crs.getString("model_name");
	// item_price = crs.getString("quoteitem_price");
	// item_netprice = Double.toString((crs.getDouble("quoteitem_price") *
	// crs.getDouble("quoteitem_tax_rate") / 100)
	// + crs.getDouble("quoteitem_price"));
	// item_netdisc = Integer.toString(crs.getInt("quoteitem_disc"));
	// item_tax_id = crs.getString("quoteitem_tax_id");
	// item_tax_rate = crs.getString("quoteitem_tax_rate");
	// item_small_desc = crs.getString("item_small_desc");
	// item_netamount = Double.toString(((crs.getDouble("quoteitem_price") -
	// crs.getInt("quoteitem_disc")) * crs.getDouble("quoteitem_tax_rate") / 100)
	// + (crs.getDouble("quoteitem_price") - crs.getInt("quoteitem_disc")));
	// quote_date = crs.getString("quote_date");
	// quotedate = strToShortDate(quote_date);
	// branch_name = crs.getString("branch_name");
	// quote_refno = crs.getString("quote_refno");
	// quote_auth = crs.getString("quote_auth");
	// quote_preownedvehstock_id = crs.getString("quote_preownedvehstock_id");
	// quote_vehstock_id = crs.getString("quote_vehstock_id");
	//
	// vehstock_comm_no = crs.getString("vehstock_comm_no");
	// // quote_hypothecation = crs.getString("quote_hypothecation");
	// // quote_fintype_id = crs.getString("quote_fintype_id");
	// quote_desc = crs.getString("quote_desc");
	//
	// // option details
	// quote_fin_option1 = crs.getString("quote_fin_option1");
	// quote_fin_option2 = crs.getString("quote_fin_option2");
	// quote_fin_option3 = crs.getString("quote_fin_option3");
	//
	// // financial details
	// quote_fin_loan1 = crs.getString("quote_fin_loan1");
	// quote_fin_loan2 = crs.getString("quote_fin_loan2");
	// quote_fin_loan3 = crs.getString("quote_fin_loan3");
	//
	// quote_fin_tenure1 = crs.getString("quote_fin_tenure1");
	// quote_fin_tenure2 = crs.getString("quote_fin_tenure2");
	// quote_fin_tenure3 = crs.getString("quote_fin_tenure3");
	//
	// quote_fin_adv_emi1 = crs.getString("quote_fin_adv_emi1");
	// quote_fin_adv_emi2 = crs.getString("quote_fin_adv_emi2");
	// quote_fin_adv_emi3 = crs.getString("quote_fin_adv_emi3");
	//
	// quote_fin_emi1 = crs.getString("quote_fin_emi1");
	// quote_fin_emi2 = crs.getString("quote_fin_emi2");
	// quote_fin_emi3 = crs.getString("quote_fin_emi3");
	//
	// quote_fin_baloonemi1 = crs.getString("quote_fin_baloonemi1");
	// quote_fin_baloonemi2 = crs.getString("quote_fin_baloonemi2");
	// quote_fin_baloonemi3 = crs.getString("quote_fin_baloonemi3");
	//
	// quote_fin_fee1 = crs.getString("quote_fin_fee1");
	// quote_fin_fee2 = crs.getString("quote_fin_fee2");
	// quote_fin_fee3 = crs.getString("quote_fin_fee3");
	//
	// quote_fin_downpayment1 = crs.getString("quote_fin_downpayment1");
	// quote_fin_downpayment2 = crs.getString("quote_fin_downpayment2");
	// quote_fin_downpayment3 = crs.getString("quote_fin_downpayment3");
	//
	// quote_terms = crs.getString("quote_terms");
	// quote_netamt = crs.getString("quote_netamt");
	// quote_totaltax = crs.getString("quote_totaltax");
	// quote_grandtotal = crs.getString("quote_grandtotal");
	// quote_enquiry_id = crs.getString("quote_enquiry_id");
	// quote_emp_id = crs.getString("quote_emp_id");
	// quote_active = crs.getString("quote_active");
	// quote_notes = crs.getString("quote_notes");
	//
	// quote_entry_id = crs.getString("quote_entry_id");
	// if (!quote_entry_id.equals("")) {
	// quote_entry_by = Exename(comp_id, (Integer.parseInt(quote_entry_id)));
	// }
	// quote_entry_date = strToLongDate(crs.getString("quote_entry_date"));
	// quote_modified_id = crs.getString("quote_modified_id");
	// if (!quote_modified_id.equals("")) {
	// quote_modified_by = Exename(comp_id,
	// (Integer.parseInt(quote_modified_id)));
	// }
	// quote_modified_date = strToLongDate(crs.getString("quote_modified_date"));
	// }
	// } else {
	// response.sendRedirect(response.encodeRedirectURL("../portal/app-error.jsp?msg=Invalid Quote!"));
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

	// protected void UpdateFields(HttpServletRequest request,
	// HttpServletResponse response) throws Exception {
	// // CheckForm();
	// if (msg.equals("")) {
	// CheckPageperm(response);
	// }
	//
	// if (msg.equals("")) {
	// String so_vehstock_id = "0";
	//
	// if (!so_id.equals("0")) {
	// StrSql = "SELECT so_vehstock_id FROM " + compdb(comp_id) + "axela_sales_so"
	// + " WHERE so_id = " + so_id + "";
	// so_vehstock_id = CNumeric(ExecuteQuery(StrSql));
	//
	// if (!so_vehstock_id.equals(quote_vehstock_id)) {
	// SendEmailToSalesOnCommChange(so_id, vehstock_comm_no, emp_id,
	// branch_email1);
	// }
	// }
	// try {
	// connectDB();
	// conntx = myBroker.getConnection();
	// conntx.setAutoCommit(false);
	// stmttx = conntx.createStatement();
	//
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote" + " SET"
	// + " quote_branch_id = "
	// + branch_id
	// + ","
	// + " quote_customer_id = "
	// + quote_customer_id
	// + ","
	// + " quote_contact_id = "
	// + quote_contact_id
	// + ","
	// + " quote_vehstock_id = "
	// + quote_vehstock_id
	// + ","
	// + " quote_date = '"
	// + quote_date
	// + "',"
	// + " quote_netamt = "
	// + quote_netamt
	// + ","
	// + " quote_discamt = "
	// + quote_discamt
	// + ","
	// + " quote_totaltax = "
	// + quote_totaltax
	// + ","
	// + " quote_grandtotal = "
	// + Math.ceil(Double.parseDouble(quote_grandtotal))
	// + ","
	// + " quote_hypothecation = '"
	// + quote_hypothecation
	// + "',"
	// + " quote_fintype_id = "
	// + quote_fintype_id
	// + ","
	// + " quote_desc = '"
	// + quote_desc
	// + "',"
	// + " quote_fin_loan1 = '"
	// + quote_fin_loan1
	// + "',"
	// + " quote_fin_loan2 = '"
	// + quote_fin_loan2
	// + "',"
	// + " quote_fin_loan3 = '"
	// + quote_fin_loan3
	// + "',"
	// + " quote_fin_tenure1 = '"
	// + quote_fin_tenure1
	// + "',"
	// + " quote_fin_tenure2 = '"
	// + quote_fin_tenure2
	// + "',"
	// + " quote_fin_tenure3 = '"
	// + quote_fin_tenure3
	// + "',"
	// + " quote_fin_adv_emi1 = '"
	// + quote_fin_adv_emi1
	// + "',"
	// + " quote_fin_adv_emi2 = '"
	// + quote_fin_adv_emi2
	// + "',"
	// + " quote_fin_adv_emi3 = '"
	// + quote_fin_adv_emi3
	// + "',"
	// + " quote_fin_emi1 = '"
	// + quote_fin_emi1
	// + "',"
	// + " quote_fin_emi2 = '"
	// + quote_fin_emi2
	// + "',"
	// + " quote_fin_emi3 = '"
	// + quote_fin_emi3
	// + "',"
	// + " quote_fin_baloonemi1 = '"
	// + quote_fin_baloonemi1
	// + "',"
	// + " quote_fin_baloonemi2 = '"
	// + quote_fin_baloonemi2
	// + "',"
	// + " quote_fin_baloonemi3 = '"
	// + quote_fin_baloonemi3
	// + "',"
	// + " quote_fin_fee1 = '"
	// + quote_fin_fee1
	// + "',"
	// + " quote_fin_fee2 = '"
	// + quote_fin_fee2
	// + "',"
	// + " quote_fin_fee3 = '"
	// + quote_fin_fee3
	// + "',"
	// + " quote_fin_downpayment1 = '"
	// + quote_fin_downpayment1
	// + "',"
	// + " quote_fin_downpayment2 = '"
	// + quote_fin_downpayment2
	// + "',"
	// + " quote_fin_downpayment3 = '"
	// + quote_fin_downpayment3
	// + "',"
	// + " quote_fin_option1 = '"
	// + quote_fin_option1
	// + "',"
	// + " quote_fin_option2 = '"
	// + quote_fin_option2
	// + "',"
	// + " quote_fin_option3 = '"
	// + quote_fin_option3
	// + "',"
	// + " quote_terms = '"
	// + quote_terms
	// + "',"
	// + " quote_emp_id = " + quote_emp_id + ",";
	// if (config_sales_quote_refno.equals("1")) {
	// StrSql += " quote_refno = '" + quote_refno + "',";
	// }
	//
	// StrSql += " quote_auth = '" + quote_auth + "',"
	// + " quote_active = '" + quote_active + "',"
	// + " quote_notes = '" + quote_notes + "',"
	// + " quote_modified_id = " + quote_modified_id + ","
	// + " quote_modified_date = '" + quote_modified_date
	// + "'" + " WHERE quote_id = " + quote_id + " ";
	// stmttx.execute(StrSql);
	//
	// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote_item"
	// + " WHERE quoteitem_quote_id = " + quote_id + "";
	// stmttx.execute(StrSql);
	//
	// AddItemFields(request);
	//
	// // For updating the Quote total feilds
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_quote" + " SET"
	// + " quote_netamt = " + quote_netamt + ","
	// + " quote_discamt = " + quote_discamt + ","
	// + " quote_exprice = " + quote_exprice + ","
	// + " quote_totaltax = " + quote_totaltax + ","
	// + " quote_grandtotal = "
	// + Math.ceil(Double.parseDouble(quote_grandtotal)) + ""
	// + " WHERE quote_id = " + quote_id + "";
	// stmttx.execute(StrSql);
	//
	// if (!so_id.equals("0")) {
	// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_so_item"
	// + " WHERE soitem_so_id = " + so_id + "";
	// stmttx.execute(StrSql);
	//
	// AddSOItemFields(quote_id, so_id);
	//
	// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_sales_quote ON quote_id = so_quote_id"
	// + " AND quote_id = " + quote_id + "" + " SET"
	// + " so_vehstock_id = quote_vehstock_id,"
	// + " so_netamt = quote_netamt,"
	// + " so_discamt = quote_discamt,"
	// + " so_exprice = quote_exprice,"
	// + " so_totaltax = quote_totaltax,"
	// + " so_grandtotal = quote_grandtotal"
	// + " WHERE so_id = " + so_id + "";
	// stmttx.execute(StrSql);
	// }
	//
	// conntx.commit();
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// SOPError("Connection is closed...");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// SOPError("Connection rollback...");
	// }
	// msg = "<br>Transaction Error!";
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + e);
	// } finally {
	// stmttx.close();
	// myBroker.freeConnection(conntx);
	// myBroker.destroy();
	// if (conntx != null && !conntx.isClosed()) {
	// conntx.close();
	// }
	// }
	// }
	// }

	// protected void DeleteFields(HttpServletResponse response) throws
	// Exception {
	// CheckPageperm(null);
	// StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
	// + " WHERE so_quote_id = "
	// + quote_id + "";
	// if (ExecuteQuery(StrSql).equals(quote_id)) {
	// msg += "<br>Quote is associated with Sales Order!";
	// }
	//
	// StrSql = "SELECT invoice_id FROM " + compdb(comp_id) + "axela_invoice"
	// + " WHERE invoice_quote_id = " + quote_id + "";
	// if (ExecuteQuery(StrSql).equals(quote_id)) {
	// msg += "<br>Quote is associated with Invoice!";
	// }
	//
	// if (msg.equals("")) {
	// try {
	// connectDB();
	// conntx = myBroker.getConnection();
	// conntx.setAutoCommit(false);
	// stmttx = conntx.createStatement();
	//
	// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote_item"
	// + " WHERE quoteitem_quote_id = " + quote_id + "";
	// stmttx.addBatch(StrSql);
	//
	// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_quote" +
	// " WHERE quote_id = "
	// + quote_id + "";
	// stmttx.addBatch(StrSql);
	// stmttx.executeBatch();
	//
	// conntx.commit();
	// } catch (Exception e) {
	// if (conntx.isClosed()) {
	// SOPError("Connection is closed...");
	// }
	// if (!conntx.isClosed() && conntx != null) {
	// conntx.rollback();
	// SOPError("Connection rollback...");
	// }
	// msg = "<br>Transaction Error!";
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + e);
	// } finally {
	// stmttx.close();
	// myBroker.freeConnection(conntx);
	// myBroker.destroy();
	// if (conntx != null && !conntx.isClosed()) {
	// conntx.close();
	// }
	// }
	// }
	// }

	// void CheckPageperm(HttpServletResponse response) {
	// try {
	// StrSql = "SELECT quote_id FROM " + compdb(comp_id) + "axela_sales_quote"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_branch ON branch_id = quote_branch_id"
	// + " WHERE quote_id = " + quote_id + BranchAccess;
	// if (ExecuteQuery(StrSql).equals("")) {
	// msg = "Access denied!";
	// response.sendRedirect("../portal/app-error.jsp?msg=" + msg + "");
	// }
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

	protected String SendEmailToSalesOnCommChange(String so_id,
			String vehstock_comm_no, String emp_id, String branch_email1) {
		String email_msg = "";
		String subject = "";
		String old_comm_no = "";
		int invoicedays = 0;
		String bgcol = "";
		String car_available = "";
		String branch_sales_email = "";
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT so_id, COALESCE(stock.item_id, 0) AS vehstock_item_id, so_downpayment,"
					+ " COALESCE(so.item_id, 0) AS so_item_id, COALESCE(so.item_name, '') AS so_item_name,"
					+ " COALESCE(stock.item_name, '') AS vehstock_item_name, salesemp.emp_name AS emp_name,"
					+ " customer_name, COALESCE(model_name, '') AS model_name,"
					+ " COALESCE(vehstock_comm_no, ''),"
					+ " soe_name,"
					+ " CONCAT(sesemp.emp_name, ' <br>', jobtitle_desc) AS sesempname,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS paintwork,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 2"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS upholstery,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 4"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS package,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 3"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS otheroptions,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " COALESCE(vehstock_comm_no, 0) AS vehstock_comm_no,"
					+ " COALESCE(vehstock_vin_no, '') AS vehstock_vin_no, customer_name,"
					+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no, so_preownedstock_id,"
					+ " branch_name, branch_sales_email, so_promise_date,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name,"
					+ " COALESCE(vehstock_invoice_date, '') AS vehstock_invoice_date,"
					+ " COALESCE(vehstock_status_id, 0) AS vehstock_status_id,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Interior Upholstery'), '') AS interior,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Exterior Paint'), '') AS exterior"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item so ON so.item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp salesemp ON salesemp.emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sesemp ON sesemp.emp_id = " + emp_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = sesemp.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item stock ON stock.item_id = vehstock_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = option_model_id"
					+ " WHERE so_id = " + so_id + "" + " GROUP BY so_id";
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_sales_email = crs.getString("branch_sales_email");

					if (vehstock_comm_no.equals("")) {
						vehstock_comm_no = "0";
					}

					if (!crs.getString("vehstock_comm_no").equals("0")) {
						car_available = "Yes";
					} else {
						car_available = "No";
					}

					if (!crs.getString("vehstock_invoice_date").equals("")) {
						invoicedays = (int) getDaysBetween(
								crs.getString("vehstock_invoice_date"),
								ToLongDate(kknow()));
					} else {
						invoicedays = 0;
					}

					invoicedays--;

					if (invoicedays < 45) // invoicedays>=0 &&
					{
						bgcol = "#ffffff";
					} else if (invoicedays >= 45 && invoicedays <= 74) // bgcol
																		// =
																		// "#ffcfa4";
					{
						bgcol = "orange";
					} else if (invoicedays > 74) // bgcol = "#ffdfdf";
					{
						bgcol = "red";
					}

					if (crs.getString("vehstock_status_id").equals("2")) // bgcol =
																			// "#caffdf";
					{
						bgcol = "green";
					}
					subject = "Car Swapped";
					old_comm_no = crs.getString("vehstock_comm_no");
					email_msg = " Dear All, <br><br>Commision Number for Sales Order ID: "
							+ so_id
							+ ""
							+ " has been changed from "
							+ crs.getString("vehstock_comm_no")
							+ " to "
							+ vehstock_comm_no
							+ ".<br>"
							+ " customer: "
							+ crs.getString("customer_name")
							+ "<br>"
							+ " Sales Order Sales Consultant: "
							+ crs.getString("emp_name") + "<br><br>";

					if (!crs.getString("vehstock_comm_no").equals("0")) {
						email_msg += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n";
						email_msg += "<tr height=\"30\">\n";
						email_msg += "<td align=\"center\" colspan=\"11\" bgColor=\"" + bgcol
								+ "\" valign=\"top\">Old Commision No. Details</td>\n";
						email_msg += "</tr>\n<tr height=\"30\">\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">SO ID</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Comm. No.</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">customer Name</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Sales Consultant</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Promised Date</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Car Availability</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Down Payment</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Car</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Interior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Exterior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Source Of Enquiry</td>\n";
						email_msg += "</tr>\n";
						email_msg += "<tr height=\"50\">\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("so_id") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("vehstock_comm_no") + "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("customer_name") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("emp_name") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + strToShortDate(crs.getString("so_promise_date")) + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + car_available + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + IndFormat(crs.getString("so_downpayment")) + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">";
						if (!crs.getString("vehstock_item_name").equals("")) {
							email_msg += crs.getString("vehstock_item_name") + "<br>";
						} else if (!crs.getString("so_item_name").equals("0")) {
							email_msg += crs.getString("so_item_name") + "<br>";
						}

						if (!crs.getString("model_name").equals("")) {
							email_msg += " Model: "
									+ crs.getString("model_name");
						}

						email_msg += "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("interior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("exterior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("soe_name") + "&nbsp;</td>\n";
						email_msg += "</tr>\n</table>\n";
					}
				}
			}
			crs.close();

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_history" + " (history_so_id,"
					+ " history_emp_id," + " history_datetime,"
					+ " history_actiontype," + " history_oldvalue,"
					+ " history_newvalue)" + " VALUES" + " ('" + so_id + "',"
					+ " '" + emp_id + "'," + " '" + ToLongDate(kknow()) + "',"
					+ " 'Comm. No.'," + " '" + old_comm_no + "'," + " '"
					+ vehstock_comm_no + "')";
			updateQuery(StrSql);

			StrSql = "SELECT so_id, COALESCE(stock.item_id, 0) AS vehstock_item_id, so_downpayment,"
					+ " COALESCE(so.item_id, 0) AS so_item_id, COALESCE(so.item_name, '') AS so_item_name,"
					+ " COALESCE(stock.item_name, '') AS vehstock_item_name, salesemp.emp_name AS emp_name,"
					+ " customer_name, COALESCE(model_name, '') AS model_name, soe_name,"
					+ " CONCAT(sesemp.emp_name, ' <br>', jobtitle_desc) AS sesempname,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 1"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS paintwork,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 2"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS upholstery,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 4"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS package,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE option_optiontype_id = 3"
					+ " AND option_model_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS otheroptions,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " COALESCE(vehstock_comm_no, 0) AS vehstock_comm_no,"
					+ " COALESCE(vehstock_vin_no, '') AS vehstock_vin_no, customer_name,"
					+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no, so_preownedstock_id,"
					+ " branch_name, branch_sales_email, so_promise_date,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name,"
					+ " COALESCE(vehstock_invoice_date, '') AS vehstock_invoice_date,"
					+ " COALESCE(vehstock_status_id, 0) AS vehstock_status_id,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Interior Upholstery'), '') AS interior,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Exterior Paint'), '') AS exterior"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item so ON so.item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = so_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe ON soe_id = enquiry_soe_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp salesemp ON salesemp.emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sesemp ON sesemp.emp_id = " + emp_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = sesemp.emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_comm_no = " + vehstock_comm_no + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item stock ON stock.item_id = vehstock_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = option_model_id"
					+ " WHERE so_id = " + so_id + "" + " GROUP BY so_id";
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				if (!vehstock_comm_no.equals("0")) {
					while (crs.next()) {
						if (!crs.getString("vehstock_comm_no").equals("0")) {
							car_available = "Yes";
						} else {
							car_available = "No";
						}

						if (!crs.getString("vehstock_invoice_date").equals("")) {
							invoicedays = (int) getDaysBetween(
									crs.getString("vehstock_invoice_date"),
									ToLongDate(kknow()));
						} else {
							invoicedays = 0;
						}

						invoicedays--;

						if (invoicedays < 45) // invoicedays>=0 &&
						{
							bgcol = "#ffffff";
						} else if (invoicedays >= 45 && invoicedays <= 74) // bgcol
																			// =
																			// "#ffcfa4";
						{
							bgcol = "orange";
						} else if (invoicedays > 74) // bgcol = "#ffdfdf";
						{
							bgcol = "red";
						}

						if (crs.getString("vehstock_status_id").equals("2")) // bgcol
																				// =
																				// "#caffdf";
						{
							bgcol = "green";
						}

						email_msg += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n";
						email_msg += "<tr height=\"30\">\n";
						email_msg += "<td align=\"center\" colspan=\"11\" bgColor=\"" + bgcol + "\" valign=\"top\">New Commision No. Details</td>\n";
						email_msg += "</tr>\n<tr height=\"30\">\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">SO ID</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Comm. No.</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">customer Name</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Sales Consultant</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Promised Date</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Car Availability</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Down Payment</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Car</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Interior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Exterior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Source Of Enquiry</td>\n";
						email_msg += "</tr>\n<tr height=\"50\">\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("so_id") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("vehstock_comm_no") + "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("customer_name") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("emp_name") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + strToShortDate(crs.getString("so_promise_date")) + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + car_available + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + IndFormat(crs.getString("so_downpayment")) + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">";
						if (!crs.getString("vehstock_item_name").equals("")) {
							email_msg += crs.getString("vehstock_item_name") + "<br>";
						} else if (!crs.getString("so_item_name").equals("0")) {
							email_msg += crs.getString("so_item_name") + "<br>";
						}

						if (!crs.getString("model_name").equals("")) {
							email_msg += " Model: "
									+ crs.getString("model_name");
						}

						email_msg += "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("interior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("exterior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">" + crs.getString("soe_name") + "&nbsp;</td>\n";
						email_msg += "</tr>\n</table>\n";
					}
				}

				email_msg += "<br><br>Regards,<br><br>";

				crs.beforeFirst();
				while (crs.next()) {
					email_msg += crs.getString("emp_name");
					email_msg += "<br>" + crs.getString("branch_name");
				}

				email_msg = "<html><body><basefont face=arial, verdana size=\"2\">"
						+ email_msg + "</body></html>";
				// postMail(branch_sales_email, "", "", branch_email1, subject,
				// email_msg, "");

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email" + " (email_contact_id,"
						+ " email_contact," + " email_from," + " email_to,"
						+ " email_cc," + " email_bcc," + " email_subject,"
						+ " email_msg," + " email_date," + " email_entry_id,"
						+ " email_sent)" + " VALUES" + " ('0'," + " ''," + " '"
						+ branch_email1 + "'," + " '" + branch_sales_email
						+ "'," + " ''," + " ''," + " '" + subject + "'," + " '"
						+ email_msg + "'," + " '" + ToLongDate(kknow()) + "',"
						+ " " + emp_id + "," + " 0)";
				updateQuery(StrSql);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			email_msg = "";
		}
		return email_msg;
	}

	public String GetConfigurationDetails(HttpServletRequest request, String quote_id,
			String item_id, String branch_id, String vehstock_id, String vehstock_comm_no,
			String emp_quote_discountupdate, String quote_date, String enquiry_id, ArrayList<Map> configuredItemList, ArrayList<Map> newconfiguredbtItemList, String comp_id) {

		StringBuilder Str = new StringBuilder();
		String tempGroup = "";
		double so_total_disc = 0.00;
		String add_disc_readOnly = "";
		String preowned = "", preownedprice = "";
		double price_amount;
		CachedRowSet crs = null;
		int groupitemcount = 0, groupcount = 0, totalBtGroupCount = 0, totalAtGroupCount = 0;
		try {
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
				conatinsMap = false;
				if (configuredItemList != null) {
					for (Map configuredItem : configuredItemList) {
						if (configuredItem.containsKey("group_name") && configuredItem.containsValue("mainItemDetails")) {
							conatinsMap = true;
							break;
						}
					}
				}
				if (conatinsMap == false) {
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
							+ "	model_id, model_name"
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
				conatinsMap = false;
				for (Map configuredItem : configuredItemList) {
					if (configuredItem.containsKey("bt_item_name")) {
						conatinsMap = true;
						break;
					}
				}
				if (conatinsMap == false) {
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
								}
							}
							groupitemcount++;
							if (crs.getString("group_type").equals("3") && crs.getString("option_select").equals("1")) {
								multi_select_basetotal += amount;
							}
							group = group_name;
							so_total_disc += item_disc;
							before_tax_total += amount;

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
					// SOP("StrSql====new==bt==" + StrSql);
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
							group = group_name;
							before_tax_total += amount;
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
				jsonconfiguredItemList = gson.toJson(configuredItemList);
				// SOP("jsonData from JSON==222==" + jsonconfiguredItemList);
				// SOP("configuredItemList==after new bt==" + gson.toJson(configuredItemList));

				// ===================================================================================
				// ===============================================Start Main Item==================================================================
				// Start Heading
				Str.append("<div class=\"panel-heading\" style=\"color: #fff; background-color: #8E44AD\">\n")
						.append("<h3 class=\"panel-title\">")
						.append("<center>")
						.append("<strong>").append("Item Details").append("</strong>")
						.append("</center></h3>")
						.append("</div>\n");
				// End Heading
				for (Map configureItem : configuredItemList) {
					if (configureItem.get("group_name").equals("mainItemDetails")) {
						Str.append("<tr>\n");

						// Checkbox
						// Str.append("<td align=center>").append("<input type=\"checkbox\" checked=\"checked\" disabled=\"disabled\"/>\n").append("</td>\n");

						// Item
						Str.append("<div class=\"col-md-12\">\n") // closed at last
								.append("<div class=\"form-group\">\n") // closed at last
								.append("<div class=\"col-md-2 col-xs-2\">\n")
								.append("<br>")
								.append("<input type=\"checkbox\" id=\"checkbox\" disabled=\"disabled\" checked=\"checked\" />")
								.append("</div>\n");
						Str.append("<div class=\"col-md-10 col-xs-10\" style=\"border-left: 1px solid #8E44AD;\">\n") // closed at last
								.append("<center>").append("<br>").append("<b>")
								.append(configureItem.get("item_name"))
								// .append(configureItem.get("item_smal_desc"))
								.append("</b>").append("</center>");

						Str.append("<table class=\"table\" align=\"center\">")
								.append("<tr>")
								.append("<td>").append("Price:")

								.append("</td>")
								.append("<td style=\"text-align: left\">").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("item_netprice")))))).append("</td>")

								.append("<td style=\"text-align: right\">")
								.append("<label id=\"ic_mainitem_down\" class=\"fa fa-chevron-circle-down\" style=\"font-size: 15px; cursor: pointer\"></label>")
								.append("<label id=\"ic_mainitem_up\" class=\"fa fa-chevron-circle-up\" style=\"font-size: 15px; cursor: pointer\"></label>") // up and down button
								.append("</td>")
								.append("</tr>");

						// Str.append("<div id=\"mainitem\">")
						Str.append("<tr id=\"mainitem_discount\">")
								.append("<td>").append("Discount:").append("</td>")
								.append("<td cursor-align=\"right\">");
						Str.append("<input cursor-align=\"right\"");
						Str.append(" type=\"text\"");
						Str.append(" id=\"txt_item_disc\"");
						Str.append(" name=\"txt_item_disc\"");
						Str.append(" value=\"").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("item_netdisc")))))).append("\"");
						Str.append(" class=\"\" size=\"8\" maxlength=\"8\"");
						Str.append(" onkeyup=\"toFloat('txt_item_disc','');CheckItemBasePrice('" + gsttype + "','")
								.append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_price")))))).append("', '")
								.append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netdisc")))))).append("');\"");
						Str.append(" onblur=\"AddDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '").append(gsttype).append("', '")
								.append(configureItem.get("item_id")).append("');\"/>\n")
								.append("</td>")
								.append("</tr>");

						Str.append("<tr id=\"mainitem_amout\">")
								.append("<td>")
								.append("Amount:")
								.append("</td>")
								.append("<td style=\"text-align: left\" id=\"div_main_item_total\">")
								.append((IndDecimalFormat(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netamount"))))))))
								.append("</td>")
								.append("</tr>");
						// .append("</div>");
						// =========================================================================

						// Str.append("<td>").append(configureItem.get("item_code")).append("</td>\n");

						// Str.append("<td align=center>1</td>\n");
						//
						// Str.append("<td  align=right>").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netprice")))))).append("</td>\n");

						Str.append("<td>");
						Str.append("<input type=\"hidden\" id=\"txt_item_name\" name=\"txt_item_name\" value=\"").append(configureItem.get("item_name")).append("\"/>\n");
						Str.append("<input type=\"hidden\" id=\"txt_item_code\" name=\"txt_item_code\" value=\"").append(configureItem.get("item_code")).append("\"/>\n");
						Str.append("<input type=\"hidden\" id=\"txt_item_id\" name=\"txt_item_id\" value=\"").append(configureItem.get("item_id")).append("\">\n");
						Str.append("<input type=\"hidden\" id=\"txt_item_baseprice\" name=\"txt_item_baseprice\" value=\"");
						Str.append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_price")))))).append("\">\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_priceamt\"");
						Str.append(" name=\"txt_item_priceamt\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_price")))))).append("\">\n");
						Str.append("<input type=\"hidden\"");
						Str.append(" id=\"txt_item_disc\"");
						Str.append(" name=\"txt_item_disc\"");
						Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netdisc")))))).append("\">\n");
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
						Str.append(" value=\"").append(df.format(Double.parseDouble(CNumeric(df.format(Double.parseDouble(String.valueOf((configureItem.get("item_netamountwod")))))))));
						Str.append("\">\n");

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
						Str.append("</tr>\n");
						if (emp_quote_discountupdate.equals("0")) {
							add_disc_readOnly = "readOnly";
						} else {
							add_disc_readOnly = "";
						}
					}
				}
				Str.append("</table>\n")
						.append("</div>\n")
						.append("</div>\n")
						.append("</div>\n");
				// script for onchange onclick
				Str.append("<script>\n")
						// on load of the page hide elements
						// main item icons
						.append("$(\"#ic_mainitem_up\").hide();\n")
						.append("$(\"#mainitem_discount\").hide();\n")
						.append("$(\"#mainitem_amout\").hide();\n");

				// Start onclick for main item
				Str.append("$(\"#ic_mainitem_up\").click(function() {\n")
						.append("$(\"#ic_mainitem_up\").hide();\n")
						.append("$(\"#ic_mainitem_down\").show();\n")
						.append("$(\"#mainitem_discount\").toggle();\n")
						.append("$(\"#mainitem_amout\").toggle();\n")
						.append("});\n")

						.append("$(\"#ic_mainitem_down\").click(function() {\n")
						.append("$(\"#ic_mainitem_down\").hide();\n")
						.append("$(\"#ic_mainitem_up\").show();\n")
						.append("$(\"#mainitem_discount\").toggle();\n")
						.append("$(\"#mainitem_amout\").toggle();\n")
						.append("});\n")
						.append("</script>\n");
				// End onclick for main item
				// ===============================================End Main Item==================================================================

				// ===============================================Start bt==================================================================
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
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_count\"");
								Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_count\"");
								Str.append(" value=\"").append(groupItemCount).append("\">\n");
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_value\"");
								Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_value\">\n");

								// Start Heading
								Str.append("<div class=\"panel-heading\" style=\"color: #fff; background-color: #8E44AD\">\n")
										.append("<h3 class=\"panel-title\">")
										.append("<center>")
										.append("<strong>").append(configureItem.get("group_name")).append("</strong>")
										.append("</center></h3>")
										.append("</div>\n");
								// End Heading
							}
							Str.append("<div class=\"col-md-12\">\n"); // closed at last
							Str.append("<div class=\"form-group\">\n") // closed at last
									.append("<div class=\"col-md-2 col-xs-2\">\n")
									.append("<br>");
							Str.append("<input type=\"checkbox\"");
							Str.append(" id=\"chk_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("\"");
							Str.append(" name=\"chk_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("\"");
							Str.append(" " + configureItem.get("bt_item_check") + "");
							Str.append(" " + configureItem.get("bt_item_disabled") + "");
							Str.append(" onclick=\"CalculateTotal(1);\"/>\n")
									.append("</div>\n");
							Str.append("<div class=\"col-md-10 col-xs-10\" style=\"border-left: 1px solid #8E44AD;\">\n") // closed at last
									.append("<center>").append("<br>").append("<b>")
									.append(configureItem.get("bt_item_name"))
									// .append(configureItem.get("item_smal_desc"))
									.append("</b>").append("</center>");
							Str.append("<table class=\"table\" align=\"center\">");
							Str.append("<tr>")
									.append("<td>").append("Price:").append("</td>")
									.append("<td style=\"text-align: left\">").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("bt_netprice"))))))
									.append("</td>")
									.append("<td style=\"text-align: right\">") // up and down button
									// down icon
									.append("<label id=\"ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount"))
									.append("_down\" class=\"fa fa-chevron-circle-down\" style=\"font-size: 15px; cursor: pointer\"></label>")
									// up icon
									.append("<label id=\"ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount"))
									.append("_up\" class=\"fa fa-chevron-circle-up\" style=\"font-size: 15px; cursor: pointer\"></label>")
									.append("</td>")
									.append("</tr>");
							Str.append("<tr")
									.append(" id=\"bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\">")
									.append("<td>").append("Discount:").append("</td>")
									.append("<td style=\"text-align: left\">")
									.append("<input cursor-align=\"right\"")
									.append(" type=\"text\"")
									.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\"")
									.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\"")
									.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_basedisc"))))))).append("\"")
									.append(" class=\"textboxright\" size=\"8\" maxlength=\"8\"")
									.append(" onKeyUp=\"toFloat(this.id, '');")
									.append("CheckBasePrice(").append(configureItem.get("bt_group_id")).append(", ").append(configureItem.get("bt_groupitemcount")).append(", ")
									.append(configureItem.get("bt_groupitemcount")).append(", ").append(configureItem.get("bt_groupcount")).append(", ").append(configureItem.get("bt_group_aftertax"))
									.append(");\"")
									.append(" onblur=\"AddBTDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '")
									.append(configureItem.get("bt_item_id"))
									.append("');\"/>\n")
									.append("</td>")
									.append("</tr>")

									.append("<tr")
									.append(" id=\"bt_").append(configureItem.get("bt_groupcount")).append("_")
									.append(configureItem.get("bt_groupitemcount")).append("_total\">")

									.append("<td>")
									.append("Amount:")
									.append("</td>")
									.append("<td style=\"text-align: left\" id=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_")
									.append(configureItem.get("bt_groupitemcount")).append("_total\"\">")
									.append(df.format(Double.parseDouble(CNumeric(String.valueOf(configureItem.get("bt_amount"))))))
									.append("</td>")
									.append("</tr>");
							Str.append("</table>\n")
									.append("</div>\n")
									.append("</div>\n")
									.append("</div>\n");

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
							Str.append("</td>\n");

							Str.append("<td align=right>\n");
							Str.append("<input type=\"hidden\"");
							Str.append(" id=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_totalwod\"");
							Str.append(" name=\"txt_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_totalwod\"");
							Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_amountwod"))))))).append("\">\n");
							// Str.append("<div id=\"div_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_total\">");
							// Str.append(df.format(Double.parseDouble(String.valueOf((configureItem.get("bt_amount"))))));
							Str.append("</div>");
							Str.append("</td>\n");
							Str.append("</tr>\n");
							// Script for onchange events of BT items (up and down icon).
							Str.append("<script>\n");
							// hide elements onload of the page.
							Str.append("$(\"#bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\").hide();\n")
									.append("$(\"#bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_total\").hide();\n")
									.append("$(\"#ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_up\").hide();\n")
									// onclick for up icon.
									.append("$(\"#ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_up\").click(function() {\n")
									.append("$(\"#ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_up\").hide();\n")
									.append("$(\"#ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_down\").show();\n")
									.append("$(\"#bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\").toggle();\n")
									.append("$(\"#bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_total\").toggle();\n")
									.append("});\n")
									// onclick for down icon.
									.append("$(\"#ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount"))
									.append("_down\").click(function() {\n")
									.append("$(\"#ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_down\").hide();\n")
									.append("$(\"#ic_bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_up\").show();\n")
									.append("$(\"#bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_disc\").toggle();\n")
									.append("$(\"#bt_").append(configureItem.get("bt_groupcount")).append("_").append(configureItem.get("bt_groupitemcount")).append("_total\").toggle();\n")
									.append("});\n");
							Str.append("</script>\n");
						}
					}
				}
				for (Map configureItem : configuredItemList) {
					if (!configureItem.get("group_name").equals("mainItemDetails") && configureItem.get("group_name").equals("totalBtItemDetails")) {
						Str.append("<div class=\"col-md-12\">")
								.append("<div class=\"form-group\">\n")
								.append("<div class=\"col-md-2 col-xs-2\">\n")
								.append("</div>\n")
								.append("<div class=\"col-md-10 col-xs-10\">\n")
								.append("<table class=\"table\" align=\"center\">")
								.append("<tr>");
						Str.append("<tr>\n");
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
						Str.append("<input type=\"hidden\" id=\"txt_expricewod\" name=\"txt_expricewod\" value=\"").append(df.format(before_tax_totalwod)).append("\"/>\n");
						Str.append("<b><div align=\"right\" id=\"div_item_price\">").append(df.format(before_tax_total)).append("</div></b>");
						Str.append("</td>\n");
						Str.append("</tr>\n");
						Str.append("</table>").append("</div>").append("</div>");
						Str.append("</div>\n");
						break;
					}
				}
				// ===============================================End Before Tax==================================================================
				// ===============================================Start after tax==================================================================
				groupitemcount = 0;
				groupcount = 0;
				all_selected_total = 0.00;
				multi_select_basetotal = 0.00;
				group = "";
				String formulae;
				ScriptEngineManager mgr = new ScriptEngineManager();
				ScriptEngine eng = mgr.getEngineByName("JavaScript");
				// This checks whether after tax Groups are already added in the Map or not, if it's added then it won't add again.
				conatinsMap = false;
				for (Map configuredItem : configuredItemList) {
					if (configuredItem.containsKey("at_item_name")) {
						conatinsMap = true;
						break;
					}
				}
				if (conatinsMap == false) {
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
								formulae = crs.getString("item_aftertaxcal_formulae").replace("expricewod", df.format(before_tax_total));
								pricetrans_amt = ((Double) eng.eval(unescapehtml(formulae)));
								amount = item_qty * (pricetrans_amt - item_disc);
								item_price_amt = pricetrans_amt;
							} else if (crs.getString("item_aftertaxcal_formulae").equals("") && crs.getString("item_aftertaxcal").equals("0")) {
								if (!crs.getString("pricetrans_variable").equals("1")) {
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
							disabled = crs.getString("item_disabled");
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
								configuredatItem.put("at_group_id", crs.getString("group_id"));
								configuredatItem.put("at_item_check", crs.getString("item_check"));
								if (crs.getString("item_check").equals("checked")) {
									configuredatItem.put("at_check", "on");
								} else {
									configuredatItem.put("at_check", "off");
								}
								configuredbtItem.put("at_item_disabled", disabled);
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
								configuredatItem.put("at_group_id", crs.getString("group_id"));
								configuredatItem.put("at_item_id", crs.getString("item_id"));
								configuredatItem.put("at_item_check", crs.getString("item_check"));
								configuredatItem.put("at_item_code", unescapehtml(crs.getString("item_code")));
								configuredatItem.put("at_item_name", unescapehtml(crs.getString("item_name")));
								configuredatItem.put("at_groupitemcount", String.valueOf(addoffer_at_item_count));
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
							amount = item_qty * (((item_price - item_disc) * item_tax_value / 100) + (item_price - item_disc));
							if (crs.getString("item_aftertaxcal_formulae").contains("expricewd") && crs.getString("item_aftertaxcal").equals("1")) {
								formulae = crs.getString("item_aftertaxcal_formulae").replace("expricewd", Double.toString(before_tax_total));
								pricetrans_amt = ((Double) eng.eval(unescapehtml(formulae)));
								amount = item_qty * (pricetrans_amt - item_disc);
								base_price = pricetrans_amt;
								item_price_amt = pricetrans_amt;
							} else if (crs.getString("item_aftertaxcal_formulae").contains("expricewod") && crs.getString("item_aftertaxcal").equals("1")) {
								formulae = crs.getString("item_aftertaxcal_formulae").replace("expricewod", df.format(before_tax_total));
								pricetrans_amt = ((Double) eng.eval(unescapehtml(formulae)));
								amount = item_qty * (pricetrans_amt - item_disc);
								item_price_amt = pricetrans_amt;
							} else if (crs.getString("item_aftertaxcal_formulae").equals("") && crs.getString("item_aftertaxcal").equals("0")) {
								if (!crs.getString("pricetrans_variable").equals("1")) {
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
							before_tax_total += amount;
							groupSet.add("at_" + group_name);
							configuredatItem.put("group_name", group_name);
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
								// Str.append("<tr>\n");
								// Str.append("<td colspan=6>");
								// Str.append("<center><b>").append(configureItem.get("group_name")).append("</b></center>");
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_count\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_count\"");
								Str.append(" value=\"").append(groupItemCount).append("\">\n");
								Str.append("<input type=\"hidden\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_value\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_value\">\n");
								// Str.append("</td>\n");

								// Str.append("</tr>\n");
								// Start Heading
								Str.append("<div class=\"panel-heading\" style=\"color: #fff; background-color: #8E44AD\">\n")
										.append("<h3 class=\"panel-title\">")
										.append("<center>")
										.append("<strong>").append(configureItem.get("group_name")).append("</strong>")
										.append("</center></h3>")
										.append("</div>\n");
								// End Heading
							}

							Str.append("<div class=\"col-md-12\">\n"); // closed at last
							Str.append("<div class=\"form-group\">\n"); // closed at last
							if (!configureItem.get("group_name").equals("Additional Discounts")) {
								Str.append("<div class=\"col-md-2 col-xs-2\">\n")
										.append("<br>");
								Str.append("<input type=\"checkbox\"");
								Str.append(" id=\"chk_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" name=\"chk_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" " + configureItem.get("at_item_check") + "");
								Str.append(" " + configureItem.get("at_item_disabled") + "");
								Str.append(" onclick=\"CalculateTotal(1);\"/>\n")
										.append("</div>\n");

								Str.append("<div class=\"col-md-10 col-xs-10\" style=\"border-left: 1px solid #8E44AD;\">\n") // closed at last
										.append("<center>").append("<br>").append("<b>")
										.append(configureItem.get("at_item_name"))
										// .append(configureItem.get("item_smal_desc"))
										.append("</b>").append("</center>");

								Str.append("<table class=\"table\" align=\"center\">");
								Str.append("<tr>")
										.append("<td>").append("Price:").append("</td>");
								if (String.valueOf(configureItem.get("at_pricetrans_variable")).equals("0")) {
									Str.append("<td id=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount"))
											.append("_price\" style=\"text-align: left\">").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_netprice"))))))
											.append("</td>");
								} else if (String.valueOf(configureItem.get("at_pricetrans_variable")).equals("1")) {
									Str.append("<td style=\"text-align: left\">");
									Str.append("<input cursor-align=\"right\" type=\"text\"");
									Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_price\"");
									Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_price\"");
									Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_netprice"))))).append("\"");
									Str.append(" class=\"textboxright\" size=\"10\" maxlength=\"10\"");
									Str.append(" onkeyup=\"toFloat(this.id, '');");
									Str.append(" CheckBasePrice(").append(configureItem.get("at_group_id")).append(", ").append(configureItem.get("at_groupitemcount")).append(", ");
									Str.append(configureItem.get("at_groupitemcount")).append(", ").append(configureItem.get("at_groupcount")).append(", ")
											.append(configureItem.get("at_group_aftertax")).append(", ");
									Str.append(configureItem.get("at_pricetrans_variable")).append(");\"");
									Str.append(" onblur=\"AddATDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '")
											.append(configureItem.get("at_item_id")).append("');\"/>\n");
									// Str.append("<tr>");

								}
								Str.append("<td style=\"text-align: right\">") // up and down button
										// down icon
										.append("<label id=\"ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount"))
										.append("_down\" class=\"fa fa-chevron-circle-down\" style=\"font-size: 15px; cursor: pointer\"></label>")
										// up icon
										.append("<label id=\"ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount"))
										.append("_up\" class=\"fa fa-chevron-circle-up\" style=\"font-size: 15px; cursor: pointer\"></label>")
										.append("</td>");
								Str.append("</tr>");
								Str.append("<tr id=\"at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\">")
										.append("<td>").append("Discount:").append("</td>")
										.append("<td style=\"text-align: left\">");
								Str.append("<input cursor-align=\"right\" type=\"text\"");
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_basedisc"))))))).append("\"");
								Str.append(" class=\"textboxright\" size=\"8\" maxlength=\"8\"");
								Str.append(" onKeyUp=\"toFloat(this.id, '');CheckBasePrice(").append(configureItem.get("at_group_id")).append(", ").append(configureItem.get("at_groupitemcount"))
										.append(", ");
								Str.append(configureItem.get("at_groupitemcount")).append(", ").append(configureItem.get("at_groupcount")).append(", ").append(configureItem.get("at_group_aftertax"))
										.append(", ");
								Str.append(configureItem.get("at_pricetrans_variable"));
								Str.append(");\"");
								Str.append(" onblur=\"AddATDiscountToJSON(").append("this").append(", '").append(configureItem.get("group_name")).append("', '")
										.append(configureItem.get("at_item_id")).append("');\"/>\n");
								Str.append("</tr>");
								Str.append("<tr id=\"at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_total\">")
										.append("<td>").append("Amount:").append("</td>")
										.append("<td id=\"div_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount"))
										.append("_total\" style=\"text-align: left\">").append(IndDecimalFormat(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_amount"))))))
										.append("</td>")
										.append("</tr>");

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
								Str.append(" id=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_totalwod\"");
								Str.append(" name=\"txt_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_totalwod\"");
								Str.append(" value=\"").append((df.format(Double.parseDouble(String.valueOf((configureItem.get("at_amountwod"))))))).append("\">\n");
								Str.append("</td>");

							} else if (configureItem.get("group_name").equals("Additional Discounts")) {
								Str.append("<div class=\"col-md-2 col-xs-2\">\n")
										.append("<br>");
								Str.append("<td align=center>");
								Str.append("<input type=\"checkbox\"");
								Str.append(" id=\"chk_addoff_at_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" name=\"chk_addoff_at_").append(configureItem.get("at_groupitemcount")).append("\"");
								Str.append(" " + configureItem.get("at_item_check") + "");
								Str.append(" onclick=\"CalculateTotal(1);\"/>\n")
										.append("</div>\n");

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
								Str.append("<div class=\"col-md-10 col-xs-10\" style=\"border-left: 1px solid #8E44AD;\">\n") // closed at last
										.append("<center>").append("<br>").append("<b>")
										.append(configureItem.get("at_item_name"))
										// .append(configureItem.get("item_smal_desc"))
										.append("</b>").append("</center>");
								Str.append("<table class=\"table\" align=\"center\">");
								Str.append("<tr>")
										.append("<td>").append("Discount:").append("</td>")
										.append("<td style=\"text-align: left\">");
								Str.append("<input cursor-align=\"right\" type=\"text\"");
								Str.append(" id=\"txt_addoff_at_").append(configureItem.get("at_groupitemcount")).append("_amt\"");
								Str.append(" name=\"txt_addoff_at_").append(configureItem.get("at_groupitemcount")).append("_amt\"");
								Str.append(" value=\"").append(df.format(Double.parseDouble(String.valueOf(configureItem.get("at_disc_amt"))))).append("\"");
								Str.append(" class=\"textboxright\" size=\"8\" maxlength=\"8\"");
								Str.append(" onkeyup=\"toFloat(this.id, '');CalculateTotal(1);\"/>\n");
								Str.append("</td>\n");
								// Discount
							}
							Str.append("</tr>");
							Str.append("</table>\n")
									.append("</div>\n")
									.append("</div>\n")
									.append("</div>\n");
							Str.append("<tr>\n");
							Str.append("</td>\n");
							Str.append("</tr>\n");
							Str.append("<script>\n");
							// hide elements onload of the page.
							Str.append("$(\"#at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\").hide();\n")
									.append("$(\"#at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_total\").hide();\n")
									.append("$(\"#ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_up\").hide();\n")
									// onclick for up icon.
									.append("$(\"#ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_up\").click(function() {\n")
									.append("$(\"#ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_up\").hide();\n")
									.append("$(\"#ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_down\").show();\n")
									.append("$(\"#at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\").toggle();\n")
									.append("$(\"#at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_total\").toggle();\n")
									.append("});\n")
									// onclick for down icon.
									.append("$(\"#ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount"))
									.append("_down\").click(function() {\n")
									.append("$(\"#ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_down\").hide();\n")
									.append("$(\"#ic_at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_up\").show();\n")
									.append("$(\"#at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_disc\").toggle();\n")
									.append("$(\"#at_").append(configureItem.get("at_groupcount")).append("_").append(configureItem.get("at_groupitemcount")).append("_total\").toggle();\n")
									.append("});\n");
							Str.append("</script>\n");
						}
					}

				}
				// End After Tax
				Str.append("<tr>\n");
				Str.append("<td colspan=\"6\" align=\"right\">");

				for (Map configureItem : configuredItemList) {
					if (!configureItem.get("group_name").equals("mainItemDetails") && configureItem.get("group_name").equals("totalAtItemDetails")) {
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
				Str.append("<div class=\"col-md-12\">\n")
						.append("<div class=\"form-group\">\n")
						.append("<div class=\"col-md-2 col-xs-2\">\n")
						.append("</div>\n")
						.append("<div class=\"col-md-10 col-xs-10\">\n")
						.append("<table class=\"table\" align=\"center\">")
						.append("<tr>").append("<td>").append("<b>")
						.append("On-Road Price:").append("</b>")
						.append("</td>");
				Str.append("<td style=\"text-align: left\" id=\"div_total_price\">")
						.append("<b>")
						.append(IndDecimalFormat(df.format(after_tax_total + before_tax_total))).append("</b>")
						.append("</td>").append("</tr>");

				Str.append("<tr>").append("<td>").append("<b>")
						.append("Total Savings:").append("</b>")
						.append("</td>");
				Str.append(
						"<td style=\"text-align: left\" id=\"div_total_disc\">")
						// Str.append("<td align=\"right\"><b><div id=\"div_total_disc\">")
						.append("<b>")
						.append(df.format(Math.ceil(so_total_disc)))
						.append("</b>").append("</td>").append("</tr>")
						.append("</table>").append("</div>").append("</div>")
						.append("</div>\n");

				if (configuredItemList != null) {
					for (Map<String, String> m : configuredItemList) {
						if (m.containsKey("group_name") && m.containsValue("itemTotals")) {
							m.put("before_tax_total", df.format(before_tax_total));
							m.put("before_tax_totalwod", df.format(before_tax_totalwod));
							m.put("after_tax_total", df.format(after_tax_total));
							m.put("so_total_disc", df.format(so_total_disc));
							conatinsMap = true;
							break;
						}
					}
				}
				if (configuredItemList != null && conatinsMap == false) {
					itemTotals.put("group_name", "itemTotals");
					itemTotals.put("before_tax_total", df.format(before_tax_total));
					itemTotals.put("before_tax_totalwod", df.format(before_tax_totalwod));
					itemTotals.put("after_tax_total", df.format(after_tax_total));
					itemTotals.put("so_total_disc", df.format(so_total_disc));
					configuredItemList.add(itemTotals);
					conatinsMap = true;
				}
				// / This logic adds the Groups in the List.
				HashMap<String, String> groups = new HashMap<String, String>();
				groups.put("group_name", "groupSet");
				int i = 1;
				for (String groupset : groupSet) {
					groups.put("group_" + i++, groupset);
				}
				configuredItemList.add(groups);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.append("__").append(gson.toJson(configuredItemList)).append("__").append(errormsg).append("__").append(vehstock_comm_no)
				.append(",").append(vehstock_id).append(",").append(item_name).append(",").append(item_id).append(",").append(model_name).append(",").append(model_id).toString();
	}

	public String PopulateModel() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE model_active = 1" + " AND model_sales = 1"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateItem(String model_id, String branch_id, String status) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT item_id, IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_price ON price_item_id = item_id"
					+ " AND price_rateclass_id = branch_rateclass_id"
					+ " AND price_effective_from <= '" + ToLongDate(kknow()) + "'"
					+ " AND price_active = '1'"
					+ " WHERE item_model_id = " + CNumeric(model_id) + "";
			if (status.equals("Add")) {
				StrSql += " AND item_active = '1'";
			}

			StrSql += " AND item_model_id != 0" + " AND item_type_id = 1"
					+ " GROUP BY item_id" + " ORDER BY item_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"selectbox\" onChange=\"GetConfigurationDetails();\">\n");
			Str.append("<option value=0>Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id"));
				Str.append(StrSelectdrop(crs.getString("item_id"), item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT COALESCE(branch_quote_email_enable, '') AS branch_quote_email_enable,"
					+ " COALESCE(branch_quote_sms_format, '') AS branch_quote_sms_format,"
					+ " COALESCE(branch_quote_sms_enable, '') AS branch_quote_sms_enable,"
					+ " COALESCE(branch_quote_email_sub, '') AS branch_quote_email_sub,"
					+ " COALESCE(branch_quote_email_format, '') AS branch_quote_email_format,"
					+ " COALESCE(branch_email1, '') AS branch_email1,"
					+ " config_admin_email, config_email_enable, config_sms_enable,"
					+ " config_sales_quote_refno, config_customer_dupnames,"
					+ " comp_email_enable, comp_sms_enable,"
					+ " COALESCE(emp.emp_quote_priceupdate, 0) AS emp_quote_priceupdate,"
					+ " COALESCE(emp.emp_quote_discountupdate, 0) AS emp_quote_discountupdate"
					+ " FROM " + compdb(comp_id) + "axela_config," + compdb(comp_id)
					+ " axela_comp," + compdb(comp_id)
					+ " axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + branch_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + emp_id + "" + " WHERE admin.emp_id = " + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				branch_quote_email_enable = crs.getString("branch_quote_email_enable");
				branch_quote_email_format = crs.getString("branch_quote_email_format");
				branch_quote_sms_enable = crs.getString("branch_quote_sms_enable");
				branch_quote_sms_format = crs.getString("branch_quote_sms_format");
				branch_quote_email_sub = crs.getString("branch_quote_email_sub");
				config_admin_email = crs.getString("config_admin_email");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				config_sales_quote_refno = crs.getString("config_sales_quote_refno");
				emp_quote_priceupdate = crs.getString("emp_quote_priceupdate");
				emp_quote_discountupdate = crs.getString("emp_quote_discountupdate");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateState(String state_id, String span_id,
			String dr_state_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT state_id, state_name" + " FROM " + compdb(comp_id) + "axela_state"
					+ " ORDER BY state_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_state_id).append(" id=").append(dr_state_id);
			Str.append(" class=selectbox onchange=\"showHint('../portal/app-location.jsp?state_id='");
			Str.append(" GetReplace(this.value)+'&dr_city_id=dr_contact_city_id', '");
			Str.append(span_id).append("');\">\n");
			Str.append("<option value = 0> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("state_id"));
				Str.append(StrSelectdrop(crs.getString("state_id"), state_id));
				Str.append(">").append(crs.getString("state_name")).append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
			StrSql = "SELECT city_id, city_name FROM " + compdb(comp_id) + "axela_city"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE city_state_id = " + state_id + ""
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_city_id).append(" id=").append(dr_city_id).append("  class=selectbox>\n");
			Str.append("<option value = 0>Select</option>\n");
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
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
					StrSql += " WHERE contact_id = " + contact_id + "";
				} else if (!quote_contact_id.equals("0")) {
					StrSql += " WHERE contact_id = " + quote_contact_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					quote_customer_id = crs.getString("customer_id");
					quote_contact_id = crs.getString("contact_id");
					link_customer_name = crs.getString("customer_name");
					link_contact_name = crs.getString("contact_fname") + " "
							+ crs.getString("contact_lname");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateBranch() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT branch_id, IF(branch_code != '', CONCAT(branch_name, ' (', branch_code, ')'), branch_name) AS branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = 1"
					+ BranchAccess + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=\"").append(crs.getString("branch_id"));
				Str.append("\" ").append(Selectdrop(crs.getInt("branch_id"), branch_id));
				Str.append(">").append(crs.getString("branch_name")).append("</option>\n");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFinOption(String financeoption_name) {

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
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	// public String PopulateFinanceType() {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT fintype_id, fintype_name"
	// + " FROM " + compdb(comp_id) + "axela_sales_so_finance_type"
	// + " GROUP BY fintype_id" + " ORDER BY fintype_id";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// Str.append("<option value=0>Select</option>\n");
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("fintype_id"));
	// Str.append(StrSelectdrop(crs.getString("fintype_id"), quote_fintype_id));
	// Str.append(">").append(crs.getString("fintype_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str.toString();
	// }

	public String PopulateExecutive(String branch_id) {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value = 0>Select Sales Consultant</option>\n");

			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = '1'"
					+ " AND emp_sales = '1'"
					+ " AND (emp_branch_id = " + branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + branch_id + "))"
					+ " GROUP BY emp_id" + " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), quote_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsuranceCompany(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT inscomp_id, inscomp_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_comp"
					+ " WHERE inscomp_active = 1" + ""
					+ " GROUP BY inscomp_id" + " ORDER BY inscomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("inscomp_id"));
				Str.append(StrSelectdrop(crs.getString("inscomp_id"), quote_inscomp_id));
				Str.append(">").append(crs.getString("inscomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-App== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
}
