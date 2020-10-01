package axela.sales;

//aJIt 1st December, 2012

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Salesorder_Update extends Connect {

	public String emp_id = "0", emp_branch_id = "0";
	public String emp_name = "";
	public String emp_email = "";
	public String comp_id = "0";
	public String emp_mobile = "";
	public String branch_email1 = "";
	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String msgChk = "";
	public String strHTML = "";
	public String empEditperm = "0";
	public String QueryString = "";
	public String branch_id = "0";
	public String branch_name = "";
	public String enquiry_enquirytype_id = "0";
	// public String so_finance_id = "0";
	public String voucher_id = "0", session_id = "", location_id = "0";
	public String voucher_vouchertype_id = "6";
	public String rateclass_id = "0";
	public String emp_role_id = "0";
	/* Sales Order Variables */
	public String so_id = "0";
	public String so_date = "", so_stockallocation_time = "";
	public String sodate = "";
	public String so_item_id = "0";
	public String item_id = "0";
	public String so_vehstock_id = "0";
	public String so_vehstock_comm_no = "0";
	public String so_preownedstock_id = "0";
	public String so_option_id = "0";
	public String so_allot_no = "";
	public String so_fintype_id = "0";
	public String so_netqty = "";
	public String so_netamt = "";
	public String so_discamt = "";
	public String so_grandtotal = "";
	public String so_quote_id = "0", enquiry_id = "0", lead_id = "0";
	public String so_totaltax = "";
	public String so_mga_amount = "", so_refund_amount = "",
			so_booking_amount = "";
	public String so_po = "";
	public String so_pan = "";
	public String so_gst = "";
	public String so_form60 = "";
	// public String so_form61 = "";
	public String so_dob = "";
	public String dr_month = "";
	public String dr_day = "";
	public String dr_year = "";
	public String so_payment_date = "", so_paymentdate = "";
	public String so_promise_date = "", so_promisedate = "";
	public String so_retail_date = "", so_retaildate = "";
	public String so_delivered_date = "", so_delivereddate = "";
	public String so_reg_no = "0";
	public String so_reg_date = "", so_regdate = "";
	public String so_cancel_date = "", so_canceldate = "";
	public String so_cancelreason_id = "0";
	public String so_prioritybalance_id = "0";
	public String so_open = "0";
	public String so_exchange = "";
	public String history_old_value = "";
	public String so_emp_id = "0";
	public String so_refno = "";
	public String so_active = "0";
	public String so_notes = "";
	public String so_auth = "0";
	public String so_desc = "", so_terms = "";
	public String so_entry_id = "0";
	public String so_entry_by = "";
	public String so_entry_date = "";
	public String so_modified_id = "0";
	public String so_modified_by = "";
	public String so_modified_date = "";
	public String entry_date = "";
	public String modified_date = "";
	/* End Of Sales Order Variables */
	/* Config Variables */
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String brandconfig_so_email_enable = "0";
	public String brandconfig_so_email_exe_enable = "0";
	public String brandconfig_so_email_format = "";
	public String brandconfig_so_email_sub = "";
	public String brandconfig_so_sms_enable = "0";
	public String brandconfig_so_sms_exe_enable = "0";
	public String brandconfig_so_sms_format = "";
	public String brandconfig_so_delivered_email_enable = "0";
	public String brandconfig_so_delivered_email_format = "";
	public String brandconfig_so_delivered_email_sub = "";
	public String brandconfig_so_delivered_sms_enable = "0";
	public String brandconfig_so_delivered_sms_format = "";
	public String brandconfig_so_email_exe_sub = "";
	public String brandconfig_so_email_exe_format = "";
	public String branch_sales_mobile = "";
	public String[] sales_mobile = null;
	public String brandconfig_so_sms_exe_format = "";
	public String config_admin_email = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String config_customer_dupnames = "0";
	public String config_sales_so_refno = "0";
	public String emp_so_priceupdate = "";
	public String emp_so_discountupdate = "";
	/* End of Config Variables */
	DecimalFormat deci = new DecimalFormat("#.##");
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String enquiry_status_id = "0";
	public String colspan = "8";
	public String contact_id = "0";
	public String so_contact_id = "0";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String contact_pin = "";
	public String contact_city_id = "0";
	public String state_id = "0";
	public String so_fincomp_id = "";
	public String so_finance_amt = "";
	public String so_customer_id = "0";
	public String customer_id = "0";
	public String customer_name = "";
	public String customer_email1 = "";
	public String customer_mobile1 = "";
	public String customer_address = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String branch_address = "";
	public String branch_city_id = "0";
	public String branch_brand_id = "0";
	public String branch_pin = "";
	public String readOnly = "";
	public String display = "";
	public String emailmsg = "", emailsub = "";
	public String smsmsg = "";
	public String emp_all_exe = "";
	public String exeemailmsg = "", exeemailsub = "";
	public String exesmsmsg = "", so_exprice = "", emp_close_enquiry = "0",
			chk_so_delivered_date = "";
	DecimalFormat df = new DecimalFormat("0.00");
	DecimalFormat df1 = new DecimalFormat("0");

	public String profitability_principalsupport_model_id = "0";
	public String profitability_principalsupport_fueltype_id = "0";
	public String profitability_principalsupport_month = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_sales_order_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_all_exe = GetSession("emp_all_exe", request);
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				contact_id = CNumeric(PadQuotes(request.getParameter("span_cont_id")));
				so_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				customer_id = CNumeric(PadQuotes(request.getParameter("span_acct_id")));
				so_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				so_quote_id = PadQuotes(request.getParameter("quote_id"));
				branch_id = CNumeric(PadQuotes(request.getParameter("txt_branch_id")));
				if (!so_quote_id.equals("") && add.equals("yes")) {
					GetQuoteDetails(response);
				}
				empEditperm = ReturnPerm(comp_id, "emp_sales_order_edit", request);
				if (!empEditperm.equals("1")) {
					readOnly = "readonly";
				}
				// For Generating session each time
				session_id = PadQuotes(request.getParameter("txt_session_id"));

				so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
				chk_so_delivered_date = PadQuotes(ExecuteQuery("SELECT so_delivered_date FROM " + compdb(comp_id) + "axela_sales_so" + " WHERE so_id = " + so_id));
				// SOP("chk_so_delivered_date===" + chk_so_delivered_date);
				// SOP("emp_id===" + emp_id);

				emp_close_enquiry = CNumeric(ExecuteQuery("SELECT emp_close_enquiry " + " FROM " + compdb(comp_id) + "axela_emp" + " WHERE" + " emp_id = " + emp_id));
				PopulateContactDetails();
				PopulateConfigDetails();
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
					display = "none";
					so_date = ToLongDate(kknow());
					sodate = strToShortDate(so_date);
					if (!addB.equals("yes")) {
						if (session_id.equals("")) {
							String key = "", possible = "0123456789";
							for (int i = 0; i < 9; i++) {
								double math = Math.random();
								key += possible.charAt((int) Math.floor(Math.random() * possible.length()));
							}
							session_id = key;
						}
						StrSql = "SELECT branch_so_desc, branch_so_terms, city_id, state_id"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " WHERE branch_id = " + branch_id + "";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							contact_city_id = crs.getString("city_id");
							state_id = crs.getString("state_id");
							so_desc = crs.getString("branch_so_desc");
							so_terms = crs.getString("branch_so_terms");
						}
						crs.close();
						// so_payment_date = ToLongDate(kknow());
						// so_paymentdate = strToShortDate(so_payment_date);
						// so_promise_date = ToLongDate(kknow());
						// so_promisedate = strToShortDate(so_promise_date);
						so_payment_date = "";
						so_paymentdate = "";
						so_promise_date = "";
						so_promisedate = "";
						// so_date = ToLongDate(kknow());
						// sodate = strToShortDate(so_date);
						so_active = "1";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_sales_order_add", request).equals("1")) {
							so_entry_id = emp_id;
							so_entry_date = ToLongDate(kknow());
							so_active = "1";
							AddFields(request);
							if (so_active.equals("1") && msg.equals("")) {
								SendEmailToNewSales(so_id, so_vehstock_id, so_entry_id, so_active, branch_email1, comp_id);
							}

							if (so_active.equals("1") && !CNumeric(so_preownedstock_id).equals("0")) {
								// SendEmailToPreOwnedSales(so_id, so_preownedstock_id, so_entry_id, so_active, branch_email1);
							}

							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("veh-salesorder-list.jsp?so_id=" + so_id + "&msg=Sales Order added successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				} else if (update.equals("yes")) {
					if (!chk_so_delivered_date.equals("")
							&& !emp_id.equals("1")) {
						response.sendRedirect(AccessDenied());
					} else {
						status = "Update";
						if (!updateB.equals("yes") && !deleteB.equals("Delete Sales Order")) {
							PopulateFields(request, response);
							contact_id = so_contact_id;
						} else if (updateB.equals("yes") && !deleteB.equals("Delete Sales Order")) {
							GetValues(request, response);
							so_active = CNumeric(ExecuteQuery("SELECT so_active FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id));
							if (ReturnPerm(comp_id, "emp_sales_order_edit", request).equals("1")) {
								so_modified_id = emp_id;
								so_modified_date = ToLongDate(kknow());
								UpdateFields(request, response);
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("veh-salesorder-list.jsp?so_id=" + so_id
											+ "&msg=Sales Order updated successfully!"
											+ msg + ""));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}

						} else if (deleteB.equals("Delete Sales Order")) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_sales_order_delete", request).equals("1")) {
								DeleteFields(response);
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("veh-salesorder-list.jsp?msg=Sales Order deleted successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// so_finance_id =
		// CNumeric(PadQuotes(request.getParameter("dr_so_finance_id")));
		enquiry_enquirytype_id = PadQuotes(request.getParameter("txt_enquiry_enquirytype_id"));
		so_preownedstock_id = CNumeric(PadQuotes(request.getParameter("txt_so_preownedstock_id")));
		dr_month = PadQuotes(request.getParameter("dr_DOBMonth"));
		dr_day = PadQuotes(request.getParameter("dr_DOBDay"));
		dr_year = PadQuotes(request.getParameter("dr_DOBYear"));
		if (dr_month.length() < 2) {
			dr_month = "0" + dr_month;
		}
		if (dr_day.length() < 2) {
			dr_day = "0" + dr_day;
		}
		so_dob = dr_day + "/" + dr_month + "/" + dr_year;
		so_pan = PadQuotes(request.getParameter("txt_so_pan"));
		so_gst = PadQuotes(request.getParameter("txt_so_gst"));
		so_form60 = CheckBoxValue(PadQuotes(request.getParameter("chk_so_form60")));
		// so_form61 =
		// CheckBoxValue(PadQuotes(request.getParameter("chk_so_form61")));
		lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
		branch_name = PadQuotes(request.getParameter("txt_branch_name"));
		so_quote_id = CNumeric(PadQuotes(request.getParameter("txt_so_quote_id")));
		if (so_quote_id.equals("0")) {
			customer_name = PadQuotes(request.getParameter("txt_customer_name"));
			contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_title")));
			contact_fname = PadQuotes(request.getParameter("txt_contact_fname"));
			contact_lname = PadQuotes(request.getParameter("txt_contact_lname"));
			contact_mobile1 = PadQuotes(request.getParameter("txt_contact_mobile1"));
			contact_email1 = PadQuotes(request.getParameter("txt_contact_email1"));
			contact_phone1 = PadQuotes(request.getParameter("txt_contact_phone1"));
			contact_address = PadQuotes(request.getParameter("txt_contact_address"));
			contact_city_id = CNumeric(PadQuotes(request.getParameter("dr_contact_city_id")));
			state_id = CNumeric(PadQuotes(request.getParameter("dr_contact_state_id")));
			contact_pin = PadQuotes(request.getParameter("txt_contact_pin"));
		}
		so_auth = CheckBoxValue(PadQuotes(request.getParameter("chk_so_auth")));
		so_item_id = CNumeric(PadQuotes(request.getParameter("txt_so_item_id")));
		sodate = PadQuotes(request.getParameter("txt_so_date"));
		// SOP("sodate-------------get-------" + sodate);
		so_netqty = CNumeric(PadQuotes(request.getParameter("txt_so_qty")));
		so_netamt = CNumeric(PadQuotes(request.getParameter("txt_so_netamt")));
		so_discamt = CNumeric(PadQuotes(request.getParameter("txt_so_discamt")));
		so_totaltax = CNumeric(PadQuotes(request.getParameter("txt_so_totaltax")));
		so_grandtotal = CNumeric(PadQuotes(request.getParameter("txt_so_grandtotal")));
		so_desc = PadQuotes(request.getParameter("txt_so_desc"));
		so_terms = PadQuotes(request.getParameter("txt_so_terms"));
		so_refno = PadQuotes(request.getParameter("txt_so_refno"));
		so_mga_amount = CNumeric(PadQuotes(request.getParameter("txt_so_mga_amount")));
		so_refund_amount = CNumeric(PadQuotes(request.getParameter("txt_so_refund_amount")));
		so_booking_amount = CNumeric(PadQuotes(request.getParameter("txt_so_booking_amount")));
		so_po = PadQuotes(request.getParameter("txt_so_po"));
		so_fintype_id = CNumeric(PadQuotes(request.getParameter("dr_so_fintype")));
		so_fincomp_id = CNumeric(PadQuotes(request.getParameter("dr_finance_by")));
		so_finance_amt = CNumeric(PadQuotes(request.getParameter("txt_so_finance_amt")));
		so_paymentdate = PadQuotes(request.getParameter("txt_so_payment_date"));
		so_promisedate = PadQuotes(request.getParameter("txt_so_promise_date"));
		so_reg_no = PadQuotes(request.getParameter("txt_so_reg_no"));
		so_regdate = PadQuotes(request.getParameter("txt_so_reg_date"));
		so_vehstock_id = CNumeric(PadQuotes(request.getParameter("txt_so_vehstock_id")));
		so_option_id = CNumeric(PadQuotes(request.getParameter("dr_option_id")));
		so_allot_no = CNumeric(PadQuotes(request.getParameter("txt_so_allot_no")));
		branch_brand_id = CNumeric(PadQuotes(request.getParameter("txt_branch_brand_id")));
		so_open = CheckBoxValue(PadQuotes(request.getParameter("chk_so_open")));
		enquiry_id = CNumeric(PadQuotes(request.getParameter("txt_enquiry_id")));
		so_exchange = CheckBoxValue(PadQuotes(request.getParameter("chk_so_exchange")));
		so_exprice = PadQuotes(request.getParameter("txt_so_exprice"));
		so_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		so_notes = PadQuotes(request.getParameter("txt_so_notes"));
		so_entry_by = PadQuotes(request.getParameter("entry_by"));
		so_modified_by = PadQuotes(request.getParameter("modified_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (sodate.equals("")) {
			msg += "<br>Enter Sales Order Date!";
		} else {
			if (isValidDateFormatShort(sodate)) {
				so_date = ConvertShortDateToStr(sodate);
				if (Long.parseLong(so_date) > Long.parseLong(ToShortDate(kknow()))) {
					msg += "<br>Sales Order Date must be less than or equal to Current Date!";
				}
			} else {
				msg += "<br>Enter valid Sales Order Date!";
			}
		}
		if (!so_vehstock_id.equals("0")) {
			StrSql = "SELECT vehstock_id FROM " + compdb(comp_id) + "axela_vehstock" + " WHERE vehstock_id = " + so_vehstock_id + "";
			if (CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				msg += "<br>Invalid Stock!";
			} else {
				// SOP("so_item_id----------" + so_item_id);
				StrSql = "SELECT vehstock_item_id FROM " + compdb(comp_id) + "axela_vehstock" + " WHERE vehstock_id = " + so_vehstock_id + "";
				// SOP("StrSql-------" + StrSql);
				if (!CNumeric(ExecuteQuery(StrSql)).equals(so_item_id)) {
					msg += "<br>Invalid Stock ID!";
				}
			}
			StrSql = "SELECT vehstock_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " WHERE vehstock_id = " + so_vehstock_id + ""
					+ " AND so_active = 1"
					+ " AND so_id != " + so_id + "";
			if (ExecuteQuery(StrSql).equals(so_vehstock_id)) {
				msg += "<br>Stock ID is associated with other Sales Order!";
			}
		}

		if (!so_preownedstock_id.equals("0")) {
			StrSql = "SELECT preownedstock_id FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " WHERE preownedstock_id = " + so_preownedstock_id + "";
			if (!ExecuteQuery(StrSql).equals(so_preownedstock_id)) {
				msg += "<br>Invalid Pre-Owned Stock!";
			} else {
				StrSql = "SELECT so_preownedstock_id FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE so_preownedstock_id = " + so_preownedstock_id + ""
						+ " AND so_active = 1"
						+ " AND so_id != " + so_id + "";
				// SOP("StrSql-----------" + StrSql);
				if (ExecuteQuery(StrSql).equals(so_preownedstock_id)) {
					msg += "<br>Pre-Owned Stock is associated with other Sales Order!";
				}
			}
		}

		if (!so_vehstock_id.equals("0") && status.equals("Add")) {
			so_stockallocation_time = ToLongDate(kknow());
		} else if (so_vehstock_id.equals("0") && status.equals("Add")) {
			so_stockallocation_time = "";
		} else if (!so_vehstock_id.equals("0") && status.equals("Update")) {
			so_stockallocation_time = ExecuteQuery("SELECT so_stockallocation_time FROM " + compdb(comp_id) + "axela_sales_so WHERE so_id = " + so_id);
			if (so_stockallocation_time.equals("")) {
				so_stockallocation_time = ToLongDate(kknow());
			}
		} else if (so_vehstock_id.equals("0") && status.equals("Update")) {
			so_stockallocation_time = "";
		}

		if (so_paymentdate.equals("")) {
			msg += "<br>Enter Payment Date!";
		} else {
			if (isValidDateFormatShort(so_paymentdate)) {
				so_payment_date = ConvertShortDateToStr(so_paymentdate);
				if (Long.parseLong(so_date) > Long.parseLong(so_payment_date)) {
					msg += "<br>Payment Date must be greater than or equal to Sales Order Date!";
				}
			} else {
				msg += "<br>Enter valid Payment Date!";
			}
		}

		if (so_promisedate.equals("")) {
			msg += "<br>Enter Tentative Delivery Date!";
		} else {
			if (isValidDateFormatShort(so_promisedate)) {
				so_promise_date = ConvertShortDateToStr(so_promisedate);
				if (Long.parseLong(so_date) > Long.parseLong(so_promise_date)) {
					msg += "<br>Tentative Delivery Date must be greater than or equal to Sales Order date!";
				}
			} else {
				msg += "<br>Enter valid Tentative Delivery Date!";
			}
		}

		// if (!so_retaildate.equals("")) {
		// if (isValidDateFormatShort(so_retaildate)) {
		// so_retail_date = ConvertShortDateToStr(so_retaildate);
		// if (!emp_id.equals("1")) {
		// if (!ToLongDate(kknow()).substring(0, 8).equals(ConvertShortDateToStr(so_retaildate).substring(0, 8))) {
		// msg += "<br>Retail Date should be Today's Date!";
		// }
		// }
		// // if (Long.parseLong(so_date) > Long.parseLong(so_retail_date)) {
		// // msg += "<br>Retail Date must be greater than or equal to Sales Order date!";
		// // }
		// } else {
		// msg += "<br>Enter valid Retail Date!";
		// }
		// }
		// if (CNumeric(so_vehstock_id).equals("0") && !so_retaildate.equals("")) {
		// msg += "<br>Stock ID is not associated with Retail Date!";
		// }

		// if (!so_delivereddate.equals("")) {
		// if (isValidDateFormatShort(so_delivereddate)) {
		// so_delivered_date = ConvertShortDateToStr(so_delivereddate);
		// if (Long.parseLong(so_date) > Long.parseLong(so_delivered_date)) {
		// msg += "<br>Delivered Date must be greater than or equal to Sales Order date!";
		// }
		// if (Long.parseLong((so_delivered_date).substring(0, 8)) > Long
		// .parseLong(ToLongDate(kknow()).substring(0, 8))) {
		// msg += "<br>Delivered Date should not be greater than today's date!";
		// }
		//
		// if (Long.parseLong((so_delivered_date).substring(0, 8)) < Long
		// .parseLong(ToLongDate(kknow()).substring(0, 8))
		// && !emp_id.equals("1")) {
		// msg += "<br>Delivered Date should not be less than today's date!";
		// }
		// // String so_retail_date = ExecuteQuery("SELECT so_retail_date FROM " + compdb(comp_id) + "axela_sales_so "
		// // + " WHERE so_id = " + so_id);
		// if (so_retail_date.equals("")) {
		// msg += "<br>Enter Retail Date!";
		// }
		// // if (Long.parseLong(so_delivered_date) >
		// // Long.parseLong(ToLongDate(kknow()))) {
		// // msg +=
		// // "<br>Delivered Date must be less than or equal to Current Date!";
		// // }
		// } else {
		// msg += "<br>Enter valid Delivered Date!";
		// }
		//
		// // StrSql = "SELECT so_vehstock_id FROM " + compdb(comp_id) +
		// // "axela_sales_so"
		// // + " WHERE so_id = " + so_id + "";
		// if (CNumeric(so_vehstock_id).equals("0")) {
		// msg += "<br>Stock ID is not associated with Delivered Date!";
		// }
		// StrSql = "SELECT vehstock_location_id FROM " + compdb(comp_id)
		// + "axela_vehstock" + " WHERE vehstock_id = " + so_vehstock_id + "";
		// if (CNumeric(ExecuteQuery(StrSql)).equals("1")
		// && comp_id.equals("1009")) {
		// msg += "<br>Stock can not be Delivered from PDI!";
		// }
		// }
		if (!so_regdate.equals("")) {
			if (isValidDateFormatShort(so_regdate)) {
				so_reg_date = ConvertShortDateToStr(so_regdate);
			} else {
				msg += "<br>Enter valid Registration Date!";
			}
		}
		// if (so_finance_id.equals("0")) {
		// msg += "<br>Select Finance!";
		// }
		if (!comp_id.equals("1009") && (so_pan.equals("") && so_form60.equals("0"))) {
			msg += "<br>Enter Pan No. Or Form 60!";
		}

		if (comp_id.equals("1009") && so_pan.equals("")) {
			msg += "<br>Enter Pan No.!";
		}

		if (!so_pan.equals("")) {
			if (so_pan.length() < 10) {
				msg += "<br>PAN No. should be of 10 digits!";
			}
		}

		if (!so_gst.equals("") && !so_gst.matches("\\d{2}[a-zA-Z]{5}\\d{4}[a-zA-Z]{1}[a-zA-Z0-9]{3}")) {
			msg = msg + "<br>Enter Valid GST No !";
		}

		if (dr_year.equals("-1") && dr_month.equals("-1") && dr_day.equals("-1")) {
			msg = msg + "<br>Select DOB!";

		}
		if (!dr_year.equals("-1") && !dr_month.equals("-1") && !dr_day.equals("-1")) {
			if (!isValidDateFormatShort(so_dob)) {
				msg += "<br>Select valid DOB!";
			}
		}
		if (!dr_year.equals("-1") || !dr_month.equals("-1") || !dr_day.equals("-1")) {
			if (dr_year.equals("-1")) {
				msg = msg + "<br>DOB: Select Year!";
			}
			if (dr_month.equals("-1")) {
				msg = msg + "<br>DOB: Select Month!";
			}
			if (dr_day.equals("-1")) {
				msg = msg + "<br>DOB: Select Day!";
			}
		}
		if (!dr_year.equals("-1") && !dr_month.equals("-1") && !dr_day.equals("-1")) {
			if (Long.parseLong(ConvertShortDateToStr(so_dob).substring(0, 8)) > Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
				msg += "<br>Customer DOB can't be greater than today's date!";
			}
		}
		if (enquiry_enquirytype_id.equals("1")) {
			if (so_option_id.equals("0")) {
				msg += "<br>Select Colour!";
			}
		}

		if (so_fintype_id.equals("0")) {
			msg += "<br>Select Finance Type!";
		} else if (so_fintype_id.equals("1")) {
			if (so_fincomp_id.equals("0")) {
				msg += "<br>Select Finance By!";
			} else if (!so_fincomp_id.equals("0") && (so_finance_amt.equals("0") || so_finance_amt.equals(""))) {
				msg += "<br>Enter Finance Amount!";
			}
		} else if (so_fintype_id.equals("3")) {
			so_fincomp_id = "0";
			so_finance_amt = "0";
		}

		if (so_booking_amount.equals("") || so_booking_amount.equals("0")) {
			msg += "<br>Enter Booking Amount!";
		}

		if (Double.parseDouble(so_booking_amount) < 5000 && comp_id.equals("1009")) {
			msg += "<br>Booking Amount must be greater than or equal to 5,000/-!";
		}

		if (!so_fintype_id.equals("1")) {
			if (!so_fincomp_id.equals("0") && (so_finance_amt.equals("0") || so_finance_amt.equals(""))) {
				msg += "<br>Enter Finance Amount!";
			} else if (so_fincomp_id.equals("0") && !so_finance_amt.equals("0") && !so_finance_amt.equals("")) {
				msg += "<br>Select Finance By because Finance amount is greater than 0!";
			}
		}
		if (so_emp_id.equals("0")) {
			msg += "<br>Select Sales Consultant!";
		}

		if (config_sales_so_refno.equals("1")) {
			if (so_refno.equals("")) {
				msg += "<br>Enter Sales Order Reference No.!";
			} else {
				if (so_refno.length() < 2) {
					msg += "<br>Sales Order Reference No. should be atleast Two Digits!";
				}

				if (!branch_id.equals("0")) {
					StrSql = "SELECT so_refno"
							+ " FROM " + compdb(comp_id) + "axela_sales_so"
							+ " WHERE so_branch_id = " + branch_id + ""
							+ " AND so_refno = '" + so_refno + "'";
					if (update.equals("yes")) {
						StrSql += " AND so_id != " + so_id;
					}
					if (!ExecuteQuery(StrSql).equals("")) {
						msg += "<br>Similar Sales Order Reference No. found!";
					}
				}
			}
		}
		// if (so_notes.length() < 5) {
		// msg += "<br>Notes should contain at least 5 characters!";
		// }
		if (so_grandtotal.equals("0.00")) {
			msg += "<br>Sales Order Amount cannot be equal to zero!";
		}

		msg = msgChk + msg;
	}

	public void GetQuoteDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT  quote_emp_id, COALESCE(enquiry_id, 0) AS enquiry_id,"
					+ "	COALESCE(enquiry_enquirytype_id, 0) AS enquiry_enquirytype_id,"
					+ " quote_item_id, COALESCE(so_id, 0) AS so_id,"
					+ " COALESCE(so_active, '0') AS  so_active, customer_id, contact_id,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " quote_vehstock_id, quote_preownedstock_id,"
					+ " contact_mobile1, contact_email1, quote_grandtotal, quote_totaltax, quote_netamt, quote_discamt,"
					+ " COALESCE(customer_address, '') AS customer_address,"
					+ " city_name, state_name, COALESCE(customer_pin, '') AS customer_pin,"
					+ " COALESCE(customer_landmark, '') AS customer_landmark,"
					+ " quote_exprice, quoteitem_item_id, customer_name, quote_branch_id,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branchname, rateclass_id, branch_brand_id,"
					+ " COALESCE(vehstock_comm_no, '0') AS vehstock_comm_no "
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
					+ " AND quoteitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = quote_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id"
					+ " WHERE quote_id = " + CNumeric(so_quote_id) + ""
					+ " GROUP BY quote_id";
			// SOP("StrSql===quote==111==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					so_emp_id = crs.getString("quote_emp_id");
					customer_id = crs.getString("customer_id");
					customer_address = crs.getString("customer_address");

					if (!customer_address.equals("")) {

						if (!crs.getString("city_name").equals("")) {
							customer_address += ", " + crs.getString("city_name");
						}
						if (!crs.getString("customer_pin").equals("")) {
							customer_address += " - " + crs.getString("customer_pin");
						}
						if (!crs.getString("state_name").equals("")) {
							customer_address += ", " + crs.getString("state_name");
						}

						if (!crs.getString("customer_landmark").equals("")) {
							customer_address += "\nLandmark: " + crs.getString("customer_landmark");
						}
					}

					enquiry_id = crs.getString("enquiry_id");
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					so_preownedstock_id = crs.getString("quote_preownedstock_id");
					item_id = crs.getString("quote_item_id");
					customer_name = crs.getString("customer_name");
					link_customer_name = "<a href=../customer/customer-list.jsp?customer_id=" + customer_id + ">" + customer_name + "</a>";
					contact_id = crs.getString("contact_id");
					contact_name = crs.getString("contactname");
					link_contact_name = "<a href=../customer/customer-contact-list.jsp?contact_id=" + contact_id + ">" + contact_name + "</a>";
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_email1 = crs.getString("contact_email1");
					branch_id = CNumeric(crs.getString("quote_branch_id"));
					rateclass_id = CNumeric(crs.getString("rateclass_id"));
					branch_name = crs.getString("branchname");
					branch_brand_id = crs.getString("branch_brand_id");
					so_item_id = crs.getString("quoteitem_item_id");
					so_grandtotal = crs.getString("quote_grandtotal");
					so_totaltax = crs.getString("quote_totaltax");
					so_discamt = crs.getString("quote_discamt");
					so_netamt = crs.getString("quote_netamt");
					so_exprice = crs.getString("quote_exprice");
					so_vehstock_id = crs.getString("quote_vehstock_id");
					so_vehstock_comm_no = crs.getString("vehstock_comm_no");
					if (!crs.getString("so_id").equals("0") && crs.getString("so_active").equals("1")) {
						response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Sales Order already present for this Enquiry!"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Quote"));
			}

			crs.close();
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
		}
	}

	public void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			// so_date = ToLongDate(kknow());
			// sodate = strToShortDate(so_date);
			// if (enquiry_enquirytype_id.equals("1")) {
			// StrSql = "SELECT vehstock_id FROM axela_vehstock"
			// + " WHERE vehstock_ref_no = '" + vehstock_ref_no + "'";
			// so_vehstock_id = CNumeric(ExecuteQuery(StrSql));
			// } else {
			// so_vehstock_id = "0";
			// }

			if (!enquiry_id.equals("") && !enquiry_id.equals("0")) {
				StrSql = "SELECT enquiry_status_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				enquiry_status_id = ExecuteQuery(StrSql);
			}
			so_dob = dr_day + "/" + dr_month + "/" + dr_year;
			if ((dr_month.equals("-1")) && (dr_day.equals("-1")) && (dr_year.equals("-1"))) {
				so_dob = "";
			} else {
				so_dob = ConvertShortDateToStr(so_dob);
			}

			// Get the total from Quote-Item table
			StrSql = "SELECT COALESCE(SUM(quoteitem_price), 0) AS so_netamt,"
					+ " COALESCE(SUM(quoteitem_disc), 0) AS so_discamt,"
					+ " COALESCE(SUM(quoteitem_tax), 0) AS so_totaltax,"
					+ " COALESCE(SUM(quoteitem_total), 0) AS so_grandtotal,"
					+ " COALESCE((SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item item"
					+ " WHERE item.quoteitem_option_group_tax = 1"
					+ " AND item.quoteitem_quote_id = " + CNumeric(so_quote_id) + "), 0) AS so_exprice"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " WHERE quoteitem_quote_id = " + CNumeric(so_quote_id) + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				so_netamt = crs.getString("so_netamt");
				so_discamt = crs.getString("so_discamt");
				so_totaltax = crs.getString("so_totaltax");
				so_grandtotal = crs.getString("so_grandtotal");
				so_exprice = crs.getString("so_exprice");
			}
			crs.close();
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so"
						+ " ( so_branch_id,"
						+ " so_prefix,"
						+ " so_no,"
						+ " so_date,"
						+ " so_customer_id,"
						+ " so_contact_id,";
				if (!so_quote_id.equals("")) {
					StrSql += " so_lead_id,"
							+ " so_enquiry_id,"
							+ " so_quote_id,";
				}
				StrSql += "	so_item_id,"
						+ " so_exprice,"
						+ " so_netamt,"
						+ " so_discamt,"
						+ " so_totaltax,"
						+ " so_grandtotal,"
						+ " so_vehstock_id,"
						+ " so_preownedstock_id,"
						+ " so_stockallocation_time,"
						+ " so_option_id,"
						+ " so_allot_no,"
						+ " so_desc,"
						+ " so_terms,"
						+ " so_mga_amount,"
						+ " so_refund_amount,"
						+ " so_booking_amount,"
						+ " so_po,"
						+ " so_fintype_id,"
						+ " so_fincomp_id,"
						+ " so_finance_amt,"
						+ " so_emp_id,";
				if (config_sales_so_refno.equals("1")) {
					StrSql += " so_refno,";
				}
				StrSql += " so_prioritybalance_id,"
						+ " so_payment_date,"
						+ " so_promise_date,"
						+ " so_retail_date,"
						+ " so_delivered_date,"
						+ " so_reg_no,"
						+ " so_reg_date,"
						// + " so_finance_id,"
						+ " so_dob,"
						+ " so_pan,"
						+ " so_gst,"
						+ " so_form60,"
						// + " so_form61,"
						+ " so_open,"
						+ " so_exchange,"
						+ " so_cancelreason_id,"
						+ " so_cancel_date,"
						+ " so_active,"
						+ " so_notes,"
						+ " so_entry_id,"
						+ " so_entry_date"
						+ ") VALUES ("
						+ branch_id + ","
						+ " COALESCE((SELECT"
						+ " branch_so_prefix"
						+ " FROM " + compdb(comp_id) + "axela_branch"
						+ " WHERE"
						+ " branch_id = " + branch_id + "),''),"
						+ " COALESCE(("
						+ "SELECT so.so_no"
						+ " FROM " + compdb(comp_id) + "axela_sales_so AS so"
						+ " WHERE"
						+ " so.so_branch_id = " + branch_id + ""
						+ " ORDER BY"
						+ " so.so_id DESC LIMIT 1), 0) + 1,"
						+ " '" + so_date + "',"
						+ " " + so_customer_id + ","
						+ " " + so_contact_id + ",";
				if (!so_quote_id.equals("")) {
					StrSql += " " + lead_id + ","
							+ " " + enquiry_id + ","
							+ " " + so_quote_id + ",";
				}
				StrSql += " " + item_id + ","
						+ " '" + so_exprice + "',"
						+ " '" + so_netamt + "',"
						+ " '" + so_discamt + "',"
						+ " '" + so_totaltax + "',"
						+ " '" + Math.ceil(Double.parseDouble(so_grandtotal)) + "',"
						+ " " + so_vehstock_id + ","
						+ " " + CNumeric(so_preownedstock_id) + ","
						+ " '" + so_stockallocation_time + "',"
						+ " " + so_option_id + ","
						+ " " + so_allot_no + ","
						+ " '" + so_desc + "',"
						+ " '" + so_terms + "',"
						+ " " + so_mga_amount + ","
						+ " " + so_refund_amount + ","
						+ " " + so_booking_amount + ","
						+ " '" + so_po + "',"
						+ " " + so_fintype_id + ","
						+ " " + so_fincomp_id + ","
						+ " " + so_finance_amt + ","
						+ " " + so_emp_id + ",";
				if (config_sales_so_refno.equals("1")) {
					StrSql += " '" + so_refno + "',";
				}
				StrSql += " " + so_prioritybalance_id + ","
						+ " '" + so_payment_date + "',"
						+ " '" + so_promise_date + "',"
						+ " '" + so_retail_date + "',"
						+ " '" + so_delivered_date + "',"
						+ " '" + so_reg_no + "',"
						+ " '" + so_reg_date + "',"
						// + " " + so_finance_id + ","
						+ " '" + so_dob + "',"
						+ " '" + so_pan + "',"
						+ " '" + so_gst + "',"
						+ "'" + so_form60 + "',"
						// + "'" + so_form61 + "',"
						+ " " + so_open + ","
						+ " " + so_exchange + ","
						+ " " + so_cancelreason_id + ","
						+ " '" + so_cancel_date + "',"
						+ " " + so_active + ","
						+ " '" + so_notes + "',"
						+ " " + so_entry_id + ","
						+ " '" + so_entry_date + "')";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs1 = stmttx.getGeneratedKeys();
				while (rs1.next()) {
					so_id = rs1.getString(1);
				}
				rs1.close();

				contact_id = so_contact_id;

				if (!so_delivered_date.equals("")
						&& !CNumeric(so_vehstock_id).equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
							+ " SET" + " vehstock_delstatus_id = 6"
							+ " WHERE"
							+ " vehstock_id = " + so_vehstock_id + "";
					stmttx.execute(StrSql);
				}

				AddItemFields(request);

				if (!so_delivered_date.equals("")) {
					// To add all the services of main item to SO service table
					AddItemServices();
				}

				// Required Workflow Documents
				StrSql = "SELECT"
						+ " " + so_id + ","
						+ " doc_wf_title,"
						+ " doc_effective,"
						+ " doc_daynos"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_wf_docs";

				StrSql = "INSERT INTO " + compdb(comp_id)
						+ "axela_sales_so_docs"
						+ "("
						+ " doc_so_id,"
						+ " doc_wf_title,"
						+ " doc_effective,"
						+ " doc_duedays)"
						+ " " + StrSql + "";
				stmttx.execute(StrSql);

				if (so_delivered_date.equals("")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET enquiry_stage_id = 5"
							+ " WHERE enquiry_id = " + enquiry_id + "";
				} else {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET" + " enquiry_stage_id = 6"
							+ " WHERE enquiry_id = " + enquiry_id + "";
				}
				stmttx.execute(StrSql);

				if (so_active.equals("1") && !so_id.equals("0")) {
					if (!enquiry_status_id.equals("2")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET" + " enquiry_status_id = 2,"
								+ " enquiry_status_date = '" + ToLongDate(kknow()) + "',"
								+ " enquiry_status_desc = 'Sales Order Raised'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						stmttx.execute(StrSql);

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " ( history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue"
								+ ") VALUES ('"
								+ enquiry_id + "',"
								+ " '" + emp_id + "'," + " '" + ToLongDate(kknow()) + "',"
								+ " 'STATUS',"
								+ " '" + history_old_value + "',"
								+ " 'Closed Won'"
								+ ")";
						stmttx.execute(StrSql);
					}
				} else if (so_active.equals("0") && !so_id.equals("0")) {
					if (!enquiry_status_id.equals("3")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET" + " enquiry_status_id = 3,"
								+ " enquiry_status_date = '" + ToLongDate(kknow()) + "',"
								+ " enquiry_status_desc = 'Sales Order Closed'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						stmttx.execute(StrSql);

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " ( history_enquiry_id," + " history_emp_id,"
								+ " history_datetime," + " history_actiontype,"
								+ " history_oldvalue," + " history_newvalue"
								+ ") VALUES ('"
								+ enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " 'STATUS',"
								+ " '" + history_old_value + "',"
								+ " 'Closed Lost')";
						stmttx.execute(StrSql);
					}
				}
				// ADD into payment track table
				AddPaymentTrack();

				conntx.commit();

				// Update Profitability fields.
				UpdateProfitability(so_id, comp_id);

				if (msg.equals("")) {
					// Copied from Enquiry_Quickadd
					if (!so_id.equals("0")) {
						if (comp_email_enable.equals("1")
								&& config_email_enable.equals("1")
								&& !branch_email1.equals("")) {
							if (!contact_email1.equals("")
									&& brandconfig_so_email_enable.equals("1")
									&& !brandconfig_so_email_format.equals("")
									&& !brandconfig_so_email_sub.equals("")) {
								SendEmail();
							}

							if (!emp_email.equals("")
									&& brandconfig_so_email_exe_enable.equals("1")
									&& !brandconfig_so_email_exe_format.equals("")
									&& !brandconfig_so_email_exe_sub.equals("")) {
								SendEmailToExecutive();
							}
						}

						if (!so_delivered_date.equals("")
								&& comp_email_enable.equals("1")
								&& config_email_enable.equals("1")
								&& !branch_email1.equals("")) {
							if (!contact_email1.equals("")
									&& brandconfig_so_delivered_email_enable.equals("1")
									&& !brandconfig_so_delivered_email_format.equals("")
									&& !brandconfig_so_delivered_email_sub.equals("")) {
								SendSODeliveredEmail();
							}
						}
						// SOP("branch_so_sms_enable---------" + branch_so_sms_enable);
						// SOP("branch_so_sms_format---------" + branch_so_sms_format);
						// SOP("contact_mobile1---------" + contact_mobile1);
						// SOP("branch_sales_mobile---------" + branch_sales_mobile);
						// SOP("emp_mobile---------" + emp_mobile);
						// SOP("branch_so_sms_exe_format---------" + branch_so_sms_exe_format);

						if (comp_sms_enable.equals("1")
								&& config_sms_enable.equals("1")) {
							if (!brandconfig_so_sms_format.equals("") && !contact_mobile1.equals("") && brandconfig_so_sms_enable.equals("1")) {
								SendSMS();
							}
							if (!brandconfig_so_sms_exe_format.equals("") && !emp_mobile.equals("") && brandconfig_so_sms_exe_enable.equals("1")) {
								SendSMSToExecutive(emp_mobile);
							}

							if (!branch_sales_mobile.equals("") && brandconfig_so_sms_exe_enable.equals("1") && !brandconfig_so_sms_exe_format.equals("")) {
								sales_mobile = branch_sales_mobile.split(",");
								for (int i = 0; i < sales_mobile.length; i++) {
									sales_mobile[i] = sales_mobile[i].replace(" ", "");
									// SOP("sales_mobile[i]-----------------" + sales_mobile[i]);
									SendSMSToExecutive(sales_mobile[i]);
								}

							}
						}

						if (!so_delivered_date.equals("")
								&& comp_sms_enable.equals("1")
								&& config_sms_enable.equals("1")) {
							if (brandconfig_so_delivered_sms_enable.equals("1") && !brandconfig_so_delivered_sms_format.equals("")
									&& !contact_mobile1.equals("")) {
								SendSODeliveredSMS();
							}
						}
					}

					// ///FROM SO DATE
					if (so_active.equals("1") && !so_date.equals("")) {
						AddCustomCRMFields(so_id, "2", "0", comp_id);
					}
					// ///For Inactive SO
					// if (so_active.equals("0") && !so_date.equals("")) {
					// AddCustomCRMFields(so_id, "2", "1", comp_id);
					// }
					// //////FROM DELIVER DATE
					if (so_active.equals("1") && !so_delivered_date.equals("")) {
						AddCustomCRMFields(so_id, "3", "0", comp_id);
					}

					// Add Sales Invoice
					// if (!so_id.equals("0")) {
					// if (!comp_id.equals("1009") || !branch_brand_id.equals("151")) {
					// CopySalesItemToCart(request, emp_id, session_id, so_id, voucher_vouchertype_id, "");
					// AddSalesInvoice();
					// }
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
	public void AddSalesInvoice() {
		try {
			String voucher_no = "0";
			StrSql = "SELECT location_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_location ON location_branch_id = branch_id"
					+ " WHERE branch_id = " + branch_id + " LIMIT 1";
			location_id = CNumeric(PadQuotes(ExecuteQuery(StrSql)));

			StrSql = "SELECT COALESCE(MAX(voucher_no),0)+1"
					+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
					+ " WHERE 1=1"
					+ " AND voucher_branch_id = " + branch_id
					+ " AND voucher_vouchertype_id = " + voucher_vouchertype_id;
			voucher_no = CNumeric(ExecuteQuery(StrSql));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_acc_voucher"
					+ " ("
					+ " voucher_vouchertype_id,"
					+ " voucher_no,"
					+ " voucher_branch_id,"
					+ " voucher_location_id,"
					+ " voucher_date,"
					+ " voucher_amount,"
					+ " voucher_lead_id,"
					+ " voucher_enquiry_id,"
					+ " voucher_quote_id,"
					+ " voucher_so_id,"
					+ " voucher_rateclass_id,"
					+ " voucher_customer_id,"
					+ " voucher_contact_id,"
					+ " voucher_emp_id,"
					+ "	voucher_downpayment,"
					+ " voucher_payment_date,"
					+ " voucher_promise_date,"
					+ " voucher_delivery_date,"
					+ " voucher_billing_add,"
					+ " voucher_consignee_add,"
					+ " voucher_open,"
					+ " voucher_active,"
					+ " voucher_notes,"
					+ "	voucher_terms,"
					+ " voucher_entry_id,"
					+ " voucher_entry_date)"
					+ " VALUES" + " ("
					+ voucher_vouchertype_id + ","
					+ " " + voucher_no + ","
					+ " " + branch_id + ","
					+ " " + location_id + ","
					+ " '" + so_date + "',"
					+ " " + so_netamt + ","
					+ " " + lead_id + ","
					+ " " + enquiry_id + ","
					+ " " + so_quote_id + ","
					+ " " + so_id + ","
					+ " " + rateclass_id + ","
					+ " " + so_customer_id + ","
					+ " " + so_contact_id + ","
					+ " " + so_emp_id + ","
					+ " " + so_booking_amount + ","
					+ " '" + so_payment_date + "',"
					+ " '" + so_promise_date + "',"
					+ " '" + so_delivered_date + "',"
					+ " '" + customer_address + "',"
					+ " '" + customer_address + "',"
					+ " " + so_open + ","
					+ " '" + so_active + "',"
					+ " '" + so_notes + "',"
					+ "	''" + "," // voucher_terms
					+ " " + so_entry_id + ","
					+ " " + ToLongDate(kknow()) + ")";

			// SOP("StrSQl=voucher=" + StrSqlBreaker(StrSql));
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);

			ResultSet rs1 = stmttx.getGeneratedKeys();
			while (rs1.next()) {
				voucher_id = rs1.getString(1);
				// vouchertrans_voucher_id = voucher_id;

			}
			rs1.close();

			AddSalesInvoiceItems();
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_acc_cart"
					+ " WHERE 1=1" + " AND cart_emp_id = " + emp_id + ""
					+ " AND cart_session_id = " + session_id + ""
					+ " AND cart_vouchertype_id = " + voucher_vouchertype_id + "";
			stmttx.addBatch(StrSql);
			stmttx.executeBatch();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
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

			StrSql = "SELECT "
					+ voucher_id + ","
					+ " cart_multivoucher_id,"
					+ " cart_customer_id,"
					+ " " + location_id + ",";
			StrSql += " cart_item_id,"
					+ " cart_discount,"
					+ " cart_discount_perc,"
					+ " cart_tax,"
					+ " cart_tax_id,"
					+ " cart_rowcount, "
					+ " cart_option_id, "
					+ " cart_price,"
					+ " cart_netprice,"
					+ " cart_delivery_date,"
					+ " cart_convfactor,"
					+ " cart_qty,"
					+ " cart_truckspace,"
					+ " cart_unit_cost,"
					+ " cart_amount,"
					+ " cart_discountamount,"
					+ " cart_taxamount,"
					+ " cart_alt_qty,"
					+ " cart_alt_uom_id,"
					+ " cart_time,"
					+ " cart_dc"
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
					+ so_customer_id + ","
					+ "	" + location_id + ","
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
					+ Double.parseDouble(so_netamt) + ","
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
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String SendEmailToNewSales(String so_id, String vehstock_id,
			String emp_id, String so_active, String branch_email1,
			String comp_id) {
		String email_msg = "", branch_id = "0";
		String subject = "";
		String branch_sales_email = "";
		int invoicedays;
		String bgcol = "";
		try {
			StrSql = "SELECT"
					+ " so_id, branch_id, customer_name, contact_mobile1, so_date, so_booking_amount, "
					+ " COALESCE(model_name, '') model_name,"
					+ " COALESCE(item_name, '') item_name,"
					+ " COALESCE (option_name,'') AS colour,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name,"
					+ " COALESCE(cancelreason_name, '') AS cancelreason_name,"
					+ " COALESCE(salesemp.emp_name, '') AS emp_name, COALESCE(manager.emp_name, '') AS managername,"
					+ " CONCAT(sesemp.emp_name, ' <br>', jobtitle_desc) AS sesempname,"
					+ " branch_name,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " COALESCE(vehstock_id, 0)  AS vehstock_id, COALESCE(vehstock_chassis_no, '') AS vehstock_chassis_no,"
					+ " COALESCE(vehstock_engine_no, '')  AS vehstock_engine_no, so_preownedstock_id, branch_sales_email,"
					+ "  COALESCE(vehstock_invoice_date, '') AS vehstock_invoice_date,"
					+ " COALESCE(vehstock_status_id, 0) AS vehstock_status_id"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp salesemp ON salesemp.emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sesemp ON sesemp.emp_id = " + emp_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = sesemp.emp_jobtitle_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id AND vehstock_id = " + vehstock_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team on team_id = teamtrans_team_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp manager on manager.emp_id = team_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = so_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_cancelreason ON cancelreason_id = so_cancelreason_id"
					+ " WHERE"
					+ " so_id = " + so_id + ""
					+ " GROUP BY"
					+ " so_id";
			// SOP("StrSql==so inactive mail==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_id = crs.getString("branch_id");
					if (so_active.equals("1")) {
						subject = "Car Booked";
						email_msg = " Dear All, <br><br>The following car is booked by our colleague "
								+ crs.getString("emp_name")
								+ " to "
								+ crs.getString("contactname")
								+ " from "
								+ crs.getString("customer_name") + "<br><br>";
					} else if (so_active.equals("0")) {
						subject = "Booking Cancelled";
						email_msg = " Dear All, <br><br>The following car booked by "
								+ crs.getString("emp_name")
								+ " to "
								+ crs.getString("contactname")
								+ " from "
								+ crs.getString("customer_name")
								+ " is cancelled and now available for sale.<br><br>";
					}

					branch_sales_email = crs.getString("branch_sales_email");

					if (!crs.getString("vehstock_invoice_date").equals("")) {
						invoicedays = (int) Math.round(getDaysBetween(
								crs.getString("vehstock_invoice_date"),
								ToLongDate(kknow())));
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
						bgcol = " ";
					}

					email_msg += "<table border=1 cellspacing=0 cellpadding=5>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Branch</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("branch_name") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Booking ID</td>\n";
					email_msg += "<td valign=\"top\">" + crs.getString("so_id")
							+ "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Customer Name</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("customer_name") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Contact Name</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("contactname") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Mobile</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("contact_mobile1") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Booking Date</td>\n";
					email_msg += "<td valign=\"top\">"
							+ strToShortDate(crs.getString("so_date"))
							+ "</td>\n";
					email_msg += "</tr>\n";
					if (!so_cancel_date.equals("")) {
						email_msg += "<tr height=50>\n";
						email_msg += "<td bgColor=" + bgcol
								+ " valign=\"top\">Cancellation Date</td>\n";
						email_msg += "<td valign=\"top\">" + so_canceldate
								+ "</td>\n";
						email_msg += "</tr>\n";
					}
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Amount:</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("so_booking_amount") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Model:</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("model_name") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Variant</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("item_name") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Colour</td>\n";
					email_msg += "<td valign=\"top\">" + crs.getString("colour")
							+ "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Delivery Status</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("delstatus_name") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">Cancel Reason</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("cancelreason_name") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">EXE. NAME</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("emp_name") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=50>\n";
					email_msg += "<td bgColor=" + bgcol
							+ " valign=\"top\">T.L. NAME</td>\n";
					email_msg += "<td valign=\"top\">"
							+ crs.getString("managername") + "</td>\n";
					email_msg += "</tr>\n";
					email_msg += "</table>\n";
				}
				email_msg += "<br><br>Regards,<br><br>";

				crs.beforeFirst();
				while (crs.next()) {
					email_msg += crs.getString("sesempname");
					email_msg += "<br>" + crs.getString("branch_name");
				}

				email_msg = "<html><body><basefont face=arial, verdana size=2>" + email_msg + "</body></html>";
				// postMail(branch_sales_email, "", "sujay@emax.in",
				// branch_email1, subject, email_msg, "");
				// SOP("email_msg===" + email_msg);
				if (!branch_sales_email.equals("")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
							+ " ("
							+ "	email_branch_id,"
							+ "	email_contact_id,"
							+ " email_contact,"
							+ " email_from,"
							+ " email_to,"
							+ " email_subject,"
							+ " email_msg,"
							+ " email_date,"
							+ " email_entry_id,"
							+ " email_sent"
							+ ")"
							+ " VALUES"
							+ " ("
							+ " " + branch_id + ","
							+ "	'0',"
							+ " '',"
							+ " '" + branch_email1 + "',"
							+ " '" + branch_sales_email + "',"
							+ " '" + subject + "',"
							+ " '" + email_msg + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " " + emp_id + ","
							+ " 0)";
					// SOP("StrSql=mail==" + StrSql);
					// SOP("StrSql-------8-----" + StrSql);
					updateQuery(StrSql);
				}

			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			email_msg = "";
		}
		return email_msg;
	}

	protected String SendEmailToSalesOnStockChange(String so_id,
			String vehstock_id, String emp_id, String branch_email1) {
		String email_msg = "", branch_id = "0";
		String subject = "";
		int invoicedays;
		String bgcol = "";
		String car_available;
		String branch_sales_email = "";
		CachedRowSet crs;
		try {
			StrSql = "SELECT"
					+ "	branch_id,"
					+ " so_id, COALESCE(stock.item_id, 0) AS vehstock_item_id, so_booking_amount,"
					+ " COALESCE(so.item_id, 0) AS so_item_id, COALESCE(so.item_name, '') AS so_item_name,"
					+ " COALESCE(stock.item_name, '') AS vehstock_item_name, salesemp.emp_name AS emp_name,"
					+ " customer_name, COALESCE(model_name, '') AS model_name, soe_name,"
					+ " CONCAT(sesemp.emp_name, ' <br>', jobtitle_desc) AS sesempname,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 1"
					+ " AND option_brand_id = branch_brand_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS paintwork,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 2"
					+ " AND option_brand_id = branch_brand_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS upholstery,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 4"
					+ " AND option_brand_id = branch_brand_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS package,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 3"
					+ " AND option_brand_id = branch_brand_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS otheroptions,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " COALESCE(vehstock_id, 0) AS vehstock_id,"
					+ " COALESCE(vehstock_chassis_no, '') AS vehstock_chassis_no, customer_name,"
					+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no,"
					+ " branch_name, branch_sales_email, so_promise_date,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name,"
					+ " COALESCE(vehstock_invoice_date, '') AS vehstock_invoice_date,"
					+ " COALESCE(vehstock_status_id, 0) AS vehstock_status_id,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE"
					+ " soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Interior Upholstery'), '') AS interior,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE"
					+ " soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Exterior Paint'), '') AS exterior"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
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
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_brand_id = option_brand_id"
					+ " WHERE"
					+ " so_id = " + so_id + ""
					+ " GROUP BY"
					+ " so_id";
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_id = crs.getString("branch_id");
					branch_sales_email = crs.getString("branch_sales_email");

					if (!crs.getString("vehstock_id").equals("0")) {
						car_available = "Yes";
					} else {
						car_available = "No";
					}

					if (!crs.getString("vehstock_invoice_date").equals("")) {
						invoicedays = (int) Math.round(getDaysBetween(crs.getString("vehstock_invoice_date"), ToLongDate(kknow())));
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
						bgcol = " ";
					}
					subject = "Car Swapped";
					email_msg = " Dear All, <br><br>Stock ID for Sales Order ID: "
							+ so_id
							+ ""
							+ " has been changed from "
							+ crs.getString("vehstock_id")
							+ " to "
							+ vehstock_id
							+ ".<br>"
							+ " Customer: "
							+ crs.getString("customer_name")
							+ "<br>"
							+ " Sales Order Consultant: "
							+ crs.getString("emp_name") + "<br><br>";

					if (!crs.getString("vehstock_id").equals("0")) {
						email_msg += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n";
						email_msg += "<tr height=\"30\">\n";
						email_msg += "<td align=\"center\" colspan=\"11\" bgColor=\""
								+ bgcol
								+ "\" valign=\"top\">Old Stock Details</td>\n";
						email_msg += "</tr>\n<tr height=\"30\">\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">SO ID</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Stock ID</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Customer Name</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Sales Consultant</td>\n";
						email_msg += "<td bgColor=\""
								+ bgcol
								+ "\" valign=\"top\">Tentative Delivery Date</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Car Availability</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Booking Amount</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Car</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Interior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Exterior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Source Of Enquiry</td>\n";
						email_msg += "</tr>\n";
						email_msg += "<tr height=\"50\">\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">" + crs.getString("so_id")
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("vehstock_id") + "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("customer_name") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("emp_name") + " </td>\n";
						email_msg += "<td bgColor=\""
								+ bgcol
								+ "\" valign=\"top\">"
								+ strToShortDate(crs
										.getString("so_promise_date"))
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">" + car_available
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ IndFormat(crs.getString("so_booking_amount"))
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">";
						if (!crs.getString("vehstock_item_name").equals("")) {
							email_msg += crs.getString("vehstock_item_name")
									+ "<br>";
						} else if (!crs.getString("so_item_name").equals("0")) {
							email_msg += crs.getString("so_item_name") + "<br>";
						}

						if (!crs.getString("model_name").equals("")) {
							email_msg += " Model: "
									+ crs.getString("model_name");
						}

						email_msg += "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("interior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("exterior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("soe_name") + "&nbsp;</td>\n";
						email_msg += "</tr>\n</table>\n";
					}
				}
			}
			crs.close();

			StrSql = "SELECT"
					+ "	branch_id,"
					+ " so_id, COALESCE(stock.item_id, 0) AS vehstock_item_id, so_booking_amount,"
					+ " COALESCE(so.item_id, 0) AS so_item_id, COALESCE(so.item_name, '') AS so_item_name,"
					+ " COALESCE(stock.item_name, '') AS vehstock_item_name, salesemp.emp_name AS emp_name,"
					+ " customer_name, COALESCE(model_name, '') AS model_name, soe_name,"
					+ " CONCAT(sesemp.emp_name, ' <br>', jobtitle_desc) AS sesempname,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 1"
					+ " AND option_brand_id = branch_brand_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS paintwork,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 2"
					+ " AND option_brand_id = branch_brand_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS upholstery,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 4"
					+ " AND option_brand_id = model_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS package,"
					+ " COALESCE((SELECT GROUP_CONCAT((CONCAT(option_name, ' (', option_code, ')')) SEPARATOR ', ')"
					+ " FROM "
					+ compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " WHERE"
					+ " option_optiontype_id = 3"
					+ " AND option_brand_id = branch_brand_id"
					+ " AND trans_vehstock_id = vehstock_id), '') AS otheroptions,"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
					+ " COALESCE(vehstock_id, 0) AS vehstock_id,"
					+ " COALESCE(vehstock_chassis_no, '') AS vehstock_chassis_no, customer_name,"
					+ " COALESCE(vehstock_engine_no, '') AS vehstock_engine_no,"
					+ " branch_name, branch_sales_email, so_promise_date,"
					+ " COALESCE(delstatus_name, '') AS delstatus_name,"
					+ " COALESCE(vehstock_invoice_date, '') AS vehstock_invoice_date,"
					+ " COALESCE(vehstock_status_id, 0) AS vehstock_status_id,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE"
					+ " soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Interior Upholstery'), '') AS interior,"
					+ " COALESCE((SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name)"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = soitem_item_id"
					+ " WHERE"
					+ " soitem_so_id = so_id"
					+ " AND soitem_option_group = 'Exterior Paint'), '') AS exterior"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
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
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = " + vehstock_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item stock ON stock.item_id = vehstock_item_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so_delstatus ON delstatus_id = vehstock_delstatus_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_vehstock_id = vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = trans_option_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_brand_id = option_brand_id"
					+ " WHERE"
					+ " so_id = " + so_id + "" + " GROUP BY so_id";
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				if (!vehstock_id.equals("0")) {
					while (crs.next()) {
						if (!crs.getString("vehstock_id").equals("0")) {
							car_available = "Yes";
						} else {
							car_available = "No";
						}

						if (!crs.getString("vehstock_invoice_date").equals("")) {
							invoicedays = (int) Math.round(getDaysBetween(
									crs.getString("vehstock_invoice_date"),
									ToLongDate(kknow())));
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
							bgcol = " ";
						}

						email_msg += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n";
						email_msg += "<tr height=\"30\">\n";
						email_msg += "<td align=\"center\" colspan=\"11\" bgColor=\""
								+ bgcol
								+ "\" valign=\"top\">New Stock Details</td>\n";
						email_msg += "</tr>\n<tr height=\"30\">\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">SO ID</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Stock ID</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Customer Name</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Sales Consultant</td>\n";
						email_msg += "<td bgColor=\""
								+ bgcol
								+ "\" valign=\"top\">Tentative Delivery Date</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Car Availability</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Booking Amount</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Car</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Interior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Exterior</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">Source Of Enquiry</td>\n";
						email_msg += "</tr>\n";
						email_msg += "<tr height=\"50\">\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">" + crs.getString("so_id")
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("vehstock_id") + "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("customer_name") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("emp_name") + " </td>\n";
						email_msg += "<td bgColor=\""
								+ bgcol
								+ "\" valign=\"top\">"
								+ strToShortDate(crs
										.getString("so_promise_date"))
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">" + car_available
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ IndFormat(crs.getString("so_booking_amount"))
								+ " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">";
						if (!crs.getString("vehstock_item_name").equals("")) {
							email_msg += crs.getString("vehstock_item_name")
									+ "<br>";
						} else if (!crs.getString("so_item_name").equals("0")) {
							email_msg += crs.getString("so_item_name") + "<br>";
						}

						if (!crs.getString("model_name").equals("")) {
							email_msg += " Model: "
									+ crs.getString("model_name");
						}

						email_msg += "&nbsp;</td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("interior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("exterior") + " </td>\n";
						email_msg += "<td bgColor=\"" + bgcol
								+ "\" valign=\"top\">"
								+ crs.getString("soe_name") + "&nbsp;</td>\n";
						email_msg += "</tr>\n</table>\n";
					}
				}

				email_msg += "<br><br>Regards,<br><br>";

				crs.beforeFirst();
				while (crs.next()) {
					email_msg += crs.getString("emp_name");
					email_msg += "<br>" + crs.getString("branch_name");
				}

				email_msg = "<html><body><basefont face=arial, verdana size=2>"
						+ email_msg + "</body></html>";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
						+ " ("
						+ "	email_branch_id,"
						+ "	email_contact_id,"
						+ " email_contact,"
						+ " email_from,"
						+ " email_to,"
						+ " email_subject,"
						+ " email_msg,"
						+ " email_date,"
						+ " email_entry_id,"
						+ " email_sent"
						+ ")"
						+ " VALUES"
						+ " ("
						+ " " + branch_id + ","
						+ "	'0',"
						+ " '',"
						+ " '" + branch_email1 + "',"
						+ " '" + branch_sales_email + "',"
						+ " '" + subject + "',"
						+ " '" + email_msg + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " " + emp_id + ","
						+ " 0)";
				// SOP("StrSql-------1-----" + StrSql);
				updateQuery(StrSql);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			email_msg = "";
		}
		return email_msg;
	}

	protected void AddItemFields(HttpServletRequest request)
			throws SQLException {
		try {
			StrSql = "SELECT" + " " + so_id + ","
					+ " quoteitem_rowcount,"
					+ " quoteitem_item_id,"
					+ " quoteitem_option_id,"
					+ " quoteitem_option_group_id,"
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
					+ " WHERE"
					+ " quoteitem_quote_id = " + CNumeric(so_quote_id) + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_item"
					+ " ("
					+ "	soitem_so_id,"
					+ " soitem_rowcount,"
					+ " soitem_item_id,"
					+ " soitem_option_id,"
					+ " soitem_option_group_id,"
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
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql==add so_item===" + StrSql);
			stmttx.execute(StrSql);
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
		}
	}

	public void AddPaymentTrack() throws SQLException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_so_payment_track"
					+ "("
					+ "	track_so_id,"
					+ " track_fincomp_id,"
					+ " track_date,"
					+ " track_amt,"
					+ " track_entry_id,"
					+ " track_entry_date"
					+ ")"
					+ " VALUES"
					+ " ("
					+ so_id + ","
					+ " 1,"
					+ " '" + so_date + "',"
					+ " " + so_grandtotal + ","
					+ " " + so_entry_id + ","
					+ " '" + so_entry_date + "')";
			stmttx.execute(StrSql);
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
		}
	}

	public String GetConfigurationDetails(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		String group = "", aftertax = "", grand_total = "0";
		Double quote_exprice = 0.00;
		int groupitemcount = 0;
		String preowned = "";
		try {
			if (enquiry_enquirytype_id.equals("2") && !so_preownedstock_id.equals("0")) {
				StrSql = "SELECT preownedstock_selling_price, variant_name,"
						+ " preownedmodel_name, preowned_sub_variant"
						+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
						+ "	INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " WHERE preownedstock_id = " + so_preownedstock_id + "";
				// SOP("StrSql==" + StrSql);
				crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned = crs.getString("preownedmodel_name") + " - " + crs.getString("variant_name");
						if (!crs.getString("preowned_sub_variant").equals("")) {
							preowned += "<br>" + crs.getString("preowned_sub_variant");
						}
					}
				}
				crs.close();
			}

			StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " item_small_desc, quoteitem_option_group, quoteitem_option_group_tax,"
					+ " quoteitem_price, quoteitem_qty, quoteitem_disc, quoteitem_total,"
					+ " quoteitem_tax_rate, quoteitem_tax2_rate, quoteitem_tax3_rate, quoteitem_tax4_rate,"
					+ " quote_exprice, quote_grandtotal"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = quoteitem_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " AND quote_id = " + CNumeric(so_quote_id) + ""
					+ " GROUP BY item_id"
					+ " ORDER BY  quoteitem_option_id, quoteitem_option_group_tax, quoteitem_id";
			// SOP("StrSql==GetConfigurationDetails==" + StrSql);

			crs = processQuery(StrSql, 0);
			Str.append("<div class=\"  table-bordered\">\n");
			Str.append("<table class=\"table table-bordered table-hover\" data-filter=\"#filter\">\n");
			Str.append("<thead><tr>\n");
			Str.append("");
			Str.append("<th data-toggle=\"true\">Select</th>\n");
			Str.append("<th>Item</th>\n");
			Str.append("<th data-hide=\"phone\">Qty</th>\n");
			Str.append("<th data-hide=\"phone\">Price</th>\n");
			Str.append("<th data-hide=\"phone\">Discount</th>\n");
			Str.append("<th data-hide=\"phone\">Amount</th>\n");
			Str.append("</tr>\n");
			Str.append("</thead>\n");
			Str.append("<tbody>\n");
			Str.append("<tr>\n<td>");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					String group_name = crs.getString("quoteitem_option_group");
					if (!crs.getString("quoteitem_option_group_tax").equals(aftertax) && !aftertax.equals("")) {
						Str.append("<tr valign=\"top\"><td colspan=5 align=\"right\" nowrap><b>");
						Str.append("Ex-Showroom Price: </td>");
						Str.append("<td align=\"left\"><b>")
								.append(df.format(crs.getDouble("quote_exprice")))
								.append("</b></td>\n</tr>\n");
					}
					if (!group.equals(group_name) && !group.equals("")) {
						groupitemcount = 0;
					}
					if (!group.equals(group_name)) {
						Str.append("<tr>\n");
						Str.append("<td colspan=6><b><center>").append(group_name).append("</center></b></td></tr>\n");
					}
					groupitemcount++;
					Str.append("<tr>\n");
					Str.append("<td>");
					Str.append("<input type=\"checkbox\" checked=\"checked\" disabled=\"disabled\"/>");
					Str.append("</td>\n<td>").append(crs.getString("item_name"));

					if (!crs.getString("item_small_desc").equals("") && enquiry_enquirytype_id.equals("1")) {
						Str.append("<br>").append(crs.getString("item_small_desc"));
					} else if (enquiry_enquirytype_id.equals("2")) {
						Str.append("<br>").append(preowned);
					}

					Str.append("</td>\n<td>");
					Str.append(crs.getDouble("quoteitem_qty"));
					Str.append("</td>\n<td>");
					Str.append(df.format((crs.getDouble("quoteitem_price")
							* (crs.getDouble("quoteitem_tax_rate")
									+ crs.getDouble("quoteitem_tax2_rate")
									+ crs.getDouble("quoteitem_tax3_rate")
									+ crs.getDouble("quoteitem_tax4_rate")) / 100)
							+ crs.getDouble("quoteitem_price")));
					Str.append("</td>\n<td>").append(df.format(crs.getDouble("quoteitem_disc")));
					Str.append("</td>\n<td>")
							.append(df.format(crs.getDouble("quoteitem_total")))
							.append("</td>\n</tr>\n");
					group = group_name;
					aftertax = crs.getString("quoteitem_option_group_tax");
					quote_exprice = crs.getDouble("quote_exprice");
					grand_total = crs.getString("quote_grandtotal");
				}
			}

			if (aftertax.equals("1")) {
				Str.append("<tr valign=\"top\">\n<td colspan=\"5\" align=\"right\" nowrap><b>");
				Str.append("Ex-Showroom Price: </td>\n");
				Str.append("<td align=\"left\"><b>").append(df.format(quote_exprice))
						.append("</b></td>\n</tr>\n");
			}

			Str.append("<tr valign=\"top\">\n<td colspan=\"5\" align=\"right\"><b>On-Road Price: </b></td>\n");
			Str.append("<td align=\"left\"><b>")
					.append(df.format(Double.parseDouble(grand_total)))
					.append("</b></td>\n");
			Str.append("</tr>\n");
			Str.append("</tbody>\n");
			Str.append("</table>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	protected void PopulateFields(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_sales_so.*,"
					+ " CONCAT(branch_name, ' (', branch_code, ')') AS branch_name,"
					+ " rateclass_id, contact_lname, contact_fname, title_id, title_desc,"
					+ " soitem_item_id, contact_mobile1, contact_phone1, contact_email1,"
					+ " customer_name, customer_id, contact_id, quote_branch_id, branch_brand_id, "
					+ " COALESCE(vehstock_id, 0) AS vehstock_id, so_preownedstock_id, enquiry_enquirytype_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = so_id" + " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = so_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = so_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " WHERE"
					+ " so_id = " + so_id + BranchAccess + "";

			if (emp_all_exe.equals("0")) {
				StrSql += ExeAccess.replace("emp_id", "so_emp_id");
			}
			// SOP("StrSql===" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					enquiry_enquirytype_id = crs.getString("enquiry_enquirytype_id");
					so_id = crs.getString("so_id");
					branch_id = crs.getString("quote_branch_id");
					// so_finance_id = crs.getString("so_finance_id");
					branch_name = crs.getString("branch_name");
					branch_brand_id = crs.getString("branch_brand_id");
					so_contact_id = crs.getString("so_contact_id");
					item_id = crs.getString("so_item_id");
					contact_title_id = crs.getString("title_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id")
							+ "\">"
							+ crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id="
							+ crs.getString("contact_id")
							+ "\">"
							+ crs.getString("title_desc")
							+ " "
							+ crs.getString("contact_fname")
							+ " "
							+ crs.getString("contact_lname") + "</a>";
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_phone1 = crs.getString("contact_phone1");
					contact_email1 = crs.getString("contact_email1");
					rateclass_id = crs.getString("rateclass_id");

					so_exprice = crs.getString("so_exprice");
					so_date = crs.getString("so_date");
					sodate = strToShortDate(so_date);
					// SOP("sodate----populate---------" + sodate);
					so_fintype_id = crs.getString("so_fintype_id");
					so_fincomp_id = crs.getString("so_fincomp_id");
					so_dob = crs.getString("so_dob");
					dr_month = SplitMonth(so_dob);
					dr_day = SplitDate(so_dob);
					dr_year = SplitYear(so_dob);
					so_pan = crs.getString("so_pan");
					so_gst = crs.getString("so_gst");
					so_form60 = crs.getString("so_form60");
					// so_form61 = crs.getString("so_form61");
					so_finance_amt = df1.format(crs.getDouble("so_finance_amt"));
					so_payment_date = crs.getString("so_payment_date");
					so_paymentdate = strToShortDate(so_payment_date);
					so_promise_date = crs.getString("so_promise_date");
					so_promisedate = strToShortDate(so_promise_date);
					so_retail_date = crs.getString("so_retail_date");
					so_retaildate = strToShortDate(so_retail_date);
					so_delivered_date = crs.getString("so_delivered_date");
					so_delivereddate = strToShortDate(so_delivered_date);
					// SOP("so_delivereddate-----------" + so_delivereddate);
					so_reg_no = crs.getString("so_reg_no");
					so_reg_date = crs.getString("so_reg_date");
					so_regdate = strToShortDate(so_reg_date);
					so_item_id = crs.getString("soitem_item_id");
					branch_name = crs.getString("branch_name");
					so_exchange = crs.getString("so_exchange");
					so_open = crs.getString("so_open");
					so_refno = crs.getString("so_refno");
					so_mga_amount = crs.getString("so_mga_amount");
					so_refund_amount = crs.getString("so_refund_amount");
					so_booking_amount = df1.format(crs.getDouble("so_booking_amount"));
					so_po = crs.getString("so_po");
					so_desc = crs.getString("so_desc");
					so_terms = crs.getString("so_terms");
					so_netamt = crs.getString("so_netamt");
					so_discamt = crs.getString("so_discamt");
					so_totaltax = crs.getString("so_totaltax");
					so_grandtotal = crs.getString("so_grandtotal");
					so_vehstock_id = crs.getString("so_vehstock_id");
					so_preownedstock_id = crs.getString("so_preownedstock_id");
					so_option_id = crs.getString("so_option_id");
					so_allot_no = crs.getString("so_allot_no");
					so_quote_id = crs.getString("so_quote_id");
					enquiry_id = crs.getString("so_enquiry_id");
					so_emp_id = crs.getString("so_emp_id");
					so_cancel_date = crs.getString("so_cancel_date");
					so_cancelreason_id = crs.getString("so_cancelreason_id");
					so_canceldate = strToShortDate(so_cancel_date);
					so_active = crs.getString("so_active");
					so_notes = crs.getString("so_notes");

					so_entry_id = crs.getString("so_entry_id");
					if (!so_entry_id.equals("")) {
						so_entry_by = Exename(comp_id,
								Integer.parseInt(so_entry_id));
					}
					entry_date = strToLongDate(crs.getString("so_entry_date"));
					so_modified_id = crs.getString("so_modified_id");
					if (!so_modified_id.equals("")) {
						so_modified_by = Exename(comp_id, Integer.parseInt(so_modified_id));
					}
					modified_date = strToLongDate(crs.getString("so_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Sales Order"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			CheckPageperm(response);
		}
		if (msg.equals("")) {

			so_dob = dr_day + "/" + dr_month + "/" + dr_year;
			if ((dr_month.equals("-1")) && (dr_day.equals("-1"))
					&& (dr_year.equals("-1"))) {
				so_dob = "";
			} else {
				so_dob = ConvertShortDateToStr(so_dob);
			}

			if (!CNumeric(enquiry_id).equals("0")) {
				StrSql = "SELECT enquiry_status_id, status_name"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
						+ " WHERE"
						+ " enquiry_id = " + enquiry_id + "";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					enquiry_status_id = crs.getString("enquiry_status_id");
					history_old_value = crs.getString("status_name");
				}
				crs.close();
			}

			StrSql = "SELECT so_vehstock_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_id = " + so_id + "";
			if (!ExecuteQuery(StrSql).equals(so_vehstock_id)) {
				// SendEmailToSalesOnStockChange(so_id, so_vehstock_id, emp_id,
				// branch_email1);
			}
			// SOP("so_date-------------update------" + so_date);
			// Get the total from Quote-Item table
			StrSql = "SELECT"
					+ " COALESCE(SUM(quoteitem_price), 0) AS so_netamt,"
					+ " COALESCE(SUM(quoteitem_disc), 0) AS so_discamt,"
					+ " COALESCE(SUM(quoteitem_tax), 0) AS so_totaltax,"
					+ " COALESCE(SUM(quoteitem_total), 0) AS so_grandtotal,"
					+ " COALESCE((SELECT SUM(quoteitem_total)"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item item"
					+ " WHERE"
					+ " item.quoteitem_option_group_tax = 1"
					+ " AND item.quoteitem_quote_id = " + so_quote_id + "), 0) AS so_exprice"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " WHERE"
					+ " quoteitem_quote_id = " + so_quote_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				so_netamt = crs.getString("so_netamt");
				so_discamt = crs.getString("so_discamt");
				so_totaltax = crs.getString("so_totaltax");
				so_grandtotal = crs.getString("so_grandtotal");
				so_exprice = crs.getString("so_exprice");
			}
			crs.close();
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
						+ " SET" + " so_branch_id = " + branch_id + ","
						+ " so_customer_id = " + so_customer_id + ","
						+ " so_contact_id = " + so_contact_id + ","
						// + " so_item_id = " + item_id + ","
						+ " so_date = '" + so_date + "',"
						+ " so_netamt = '" + so_netamt + "',"
						+ " so_discamt = '" + so_discamt + "',"
						+ " so_totaltax = '" + so_totaltax + "',"
						+ " so_grandtotal = '" + Math.ceil(Double.parseDouble(so_grandtotal)) + "',"
						+ " so_vehstock_id = " + so_vehstock_id + ","
						+ " so_stockallocation_time = '" + so_stockallocation_time + "',"
						+ " so_option_id = " + so_option_id + ","
						+ " so_allot_no = " + so_allot_no + ","
						+ " so_desc = '" + so_desc + "',"
						+ " so_terms = '" + so_terms + "',"
						+ " so_emp_id  = " + so_emp_id + ",";
				if (config_sales_so_refno.equals("1")) {
					StrSql += " so_refno = '" + so_refno + "',";
				}
				StrSql += " so_mga_amount = " + so_mga_amount + ","
						+ " so_refund_amount = " + so_refund_amount + ","
						+ " so_booking_amount = " + so_booking_amount + ","
						+ " so_po = '" + so_po + "',"
						+ " so_fintype_id = " + so_fintype_id + ","
						+ " so_fincomp_id = " + so_fincomp_id + ","
						+ " so_finance_amt = " + so_finance_amt + ","
						// + " so_finance_id = " + so_finance_id + ","
						+ " so_dob = '" + so_dob + "',"
						+ " so_pan = '" + so_pan + "',"
						+ " so_gst = '" + so_gst + "',"
						+ " so_form60 = '" + so_form60 + "',"
						// + " so_form61 = '" + so_form61 + "',"
						+ " so_payment_date = '" + so_payment_date + "',"
						+ " so_promise_date = '" + so_promise_date + "',"
						+ " so_reg_no = '" + so_reg_no + "',"
						+ " so_reg_date = '" + so_reg_date + "',"
						+ " so_open = '" + so_open + "',"
						+ " so_exchange = '" + so_exchange + "',"
						+ " so_notes = '" + so_notes + "',"
						+ " so_modified_id = " + so_modified_id + ","
						+ " so_modified_date = '" + so_modified_date + "'"
						+ " WHERE"
						+ " so_id = " + so_id + "";
				// SOP("StrSql-------update SO-" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql);

				// Active/ In-active Sales Invoice when Sales Order is Active/ In-active
				StrSql = "SELECT voucher_id "
						+ "	FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ "	WHERE voucher_so_id = " + so_id + ""
						+ " LIMIT 1";
				voucher_id = CNumeric(ExecuteQuery(StrSql));

				if (!voucher_id.equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_acc_voucher"
							+ " SET voucher_active = '" + so_active + "',"
							+ " voucher_modified_id = " + emp_id + ","
							+ " voucher_modified_date = " + ToLongDate(kknow()) + ""
							+ " WHERE voucher_id = " + voucher_id + "";
					stmttx.execute(StrSql);
				}

				if (!so_quote_id.equals("0") && !so_quote_id.equals("")) {
					// delete all the previous items and get the quote items
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_so_item"
							+ " WHERE"
							+ " soitem_so_id = " + so_id + "";
					stmttx.execute(StrSql);

					// add the quote items to the so_item table
					AddItemFields(request);
				}
				StrSql = "SELECT"
						+ " soservice_kms"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_service"
						+ " WHERE"
						+ " soservice_so_id = " + so_id + "";
				if (ExecuteQuery(StrSql).equals("")
						&& !so_delivered_date.equals("")) {
					AddItemServices();
				} else if (!so_delivered_date.equals("")
						&& !ExecuteQuery(StrSql).equals("")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so_service"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item ON soitem_so_id = soservice_so_id"
							+ " AND soitem_rowcount != 0"
							+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_service ON itemservice_item_id = soitem_item_id"
							+ " SET"
							+ " soservice_date = DATE_FORMAT(DATE_ADD(" + so_delivered_date
							+ ", INTERVAL itemservice_days DAY),'%Y%m%d%H%i%S')"
							+ " WHERE"
							+ " soservice_so_id = " + so_id + "";
					stmttx.execute(StrSql);
				} else if (so_delivered_date.equals("")
						&& !ExecuteQuery(StrSql).equals("")) {
					StrSql = "DELETE FROM "
							+ compdb(comp_id) + "axela_sales_so_service"
							+ " WHERE soservice_so_id = " + so_id + "";
					stmttx.execute(StrSql);
				}

				if (so_delivered_date.equals("")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET" + " enquiry_stage_id = 5"
							+ " WHERE"
							+ " enquiry_id = " + enquiry_id + "";
				} else {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET" + " enquiry_stage_id = 6"
							+ " WHERE"
							+ " enquiry_id = " + enquiry_id + "";
				}
				stmttx.execute(StrSql);

				if (!so_delivered_date.equals("")
						&& !CNumeric(so_vehstock_id).equals("0")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
							+ " SET" + " vehstock_delstatus_id = '6'"
							+ " WHERE"
							+ " vehstock_id = " + so_vehstock_id + "";
					stmttx.execute(StrSql);
				}
				if (so_delivered_date.equals("")) { // if so_delivered_date is
													// not there, delete all the
													// PSFs for that SO
					StrSql = "DELETE"
							+ " salescrm.*"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm AS salescrm"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = salescrm.crm_crmdays_id"
							+ " WHERE"
							+ " salescrm.crm_so_id = " + so_id + ""
							+ " AND crmdays_crmtype_id = 3";
					stmttx.execute(StrSql);
				}
				if (so_active.equals("1") && !so_id.equals("0")) {
					StrSql = "DELETE"
							+ " salescrm.*"
							+ " FROM " + compdb(comp_id) + "axela_sales_crm AS salescrm"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = salescrm.crm_crmdays_id"
							+ " WHERE"
							+ " salescrm.crm_so_id = " + so_id + ""
							+ " AND crmdays_crmtype_id = 2"
							+ " AND crmdays_so_inactive = 1";
					stmttx.execute(StrSql);

					if (!enquiry_status_id.equals("2")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET" + " enquiry_status_id = 2,"
								+ " enquiry_status_date = '" + ToLongDate(kknow()) + "',"
								+ " enquiry_status_desc = 'Sales Order Raised'"
								+ " WHERE"
								+ " enquiry_id = " + enquiry_id + "";
						stmttx.execute(StrSql);

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " ("
								+ "	history_enquiry_id," + " history_emp_id,"
								+ " history_datetime," + " history_actiontype,"
								+ " history_oldvalue," + " history_newvalue"
								+ ")"
								+ " VALUES"
								+ " ('" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " 'STATUS',"
								+ " '" + history_old_value + "',"
								+ " 'Closed Won')";
						stmttx.execute(StrSql);
					}
				} else if (so_active.equals("0") && !so_id.equals("0")) {
					if (!enquiry_status_id.equals("3")) {
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET" + " enquiry_status_id = 3,"
								+ " enquiry_status_date = '" + ToLongDate(kknow()) + "',"
								+ " enquiry_status_desc = 'Sales Order Closed'"
								+ " WHERE"
								+ " enquiry_id = " + enquiry_id + "";
						stmttx.execute(StrSql);

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " ("
								+ "	history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue"
								+ ")"
								+ " VALUES"
								+ " ('"
								+ enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " 'STATUS',"
								+ " '" + history_old_value + "',"
								+ " 'Closed Lost')";
						stmttx.execute(StrSql);
					}
				}

				conntx.commit();

				// Get the total from Quote-Item table
				StrSql = "SELECT"
						+ " SUM(soitem_price) AS so_netamt, SUM(soitem_disc) AS so_discamt,"
						+ " SUM(soitem_tax) AS so_totaltax, SUM(soitem_total) AS so_grandtotal,"
						+ " (SELECT SUM(soitem_total)"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_item item"
						+ " WHERE"
						+ " item.soitem_option_group_tax = 1"
						+ " AND item.soitem_so_id = " + so_id
						+ ") AS so_exprice"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
						+ " WHERE"
						+ " soitem_so_id = " + so_id + "";
				CachedRowSet crs1 = processQuery(StrSql, 0);

				while (crs1.next()) {
					so_netamt = crs1.getString("so_netamt");
					so_discamt = crs1.getString("so_discamt");
					so_totaltax = crs1.getString("so_totaltax");
					so_grandtotal = crs1.getString("so_grandtotal");
					so_exprice = crs1.getString("so_exprice");
				}
				crs1.close();
				// For updating the Quote total feilds
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
						+ " SET" + " so_netamt = " + so_netamt + ","
						+ " so_discamt = " + so_discamt + ","
						+ " so_exprice = " + so_exprice + ","
						+ " so_totaltax = " + so_totaltax + ","
						+ " so_grandtotal = " + Math.ceil(Double.parseDouble(so_grandtotal)) + ""
						+ " WHERE"
						+ " so_id = " + so_id + "";
				stmttx.execute(StrSql);

				conntx.commit();
				// Update Profitability fields.
				UpdateProfitability(so_id, comp_id);

				// ///FROM SO DATE
				if (so_active.equals("1") && !so_date.equals("")) {
					AddCustomCRMFields(so_id, "2", "0", comp_id);
				}
				// //////FROM DELIVER DATE
				if (so_active.equals("1") && !so_delivered_date.equals("")) {
					AddCustomCRMFields(so_id, "3", "0", comp_id);
				}
				// SOP("so_delivered_date==00==" + so_delivered_date);
				if (!so_delivered_date.equals("")) {
					// SOP("so_delivered_date==11==" + so_delivered_date);
					AddSOVehicle(so_id, emp_id, comp_id, so_delivered_date);
				}
				// SOP("1111");
				if (so_active.equals("0")) {
					AddCustomCRMFields(so_id, "2", "1", comp_id);
					SendEmailToNewSales(so_id, so_vehstock_id, so_modified_id, so_active, branch_email1, comp_id);
				}
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = msg + "<br>Transaction Error!";
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
		// StrSql = "SELECT"
		// + " voucher_id"
		// + " FROM "
		// + compdb(comp_id) + "axela_acc_voucher"
		// + " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
		// + " INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
		// + " WHERE"
		// + " voucher_so_id = " + so_id + "";
		// if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
		// msg += "<br>Sales Order is associated with Invoice!";
		// }

		// check association for vehicle
		StrSql = "SELECT"
				+ " veh_so_id"
				+ " FROM " + compdb(comp_id) + "axela_service_veh"
				+ " WHERE"
				+ " veh_so_id = " + so_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Sales Order is associated with Vehicle!";
		}

		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				// Delete all the documents associated with the sales Order
				StrSql = "SELECT"
						+ " doc_value"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_docs"
						+ " WHERE doc_so_id = " + so_id + "";
				String filename = ExecuteQuery(StrSql);
				if (!filename.equals("") && filename != null) {
					File f = new File(SODocPath(comp_id) + filename);
					if (f.exists()) {
						f.delete();
					}
				}

				StrSql = "DELETE"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_docs"
						+ " WHERE doc_so_id = " + so_id + "";
				stmttx.execute(StrSql);
				// documents delete end

				StrSql = "DELETE"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
						+ " WHERE soitem_so_id = " + so_id + "";
				stmttx.execute(StrSql);

				StrSql = "DELETE"
						+ " FROM " + compdb(comp_id) + "axela_sales_crm"
						+ " WHERE"
						+ " crm_so_id = " + so_id + "";
				stmttx.execute(StrSql);

				StrSql = "DELETE"
						+ " FROM " + compdb(comp_id) + "axela_sales_so"
						+ " WHERE"
						+ " so_id = " + so_id + "";
				stmttx.execute(StrSql);

				// Delete Sales Invoice associated with the Sales Order
				StrSql = "DELETE"
						+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
						+ " WHERE voucher_so_id = " + so_id;
				stmttx.execute(StrSql);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET" + " enquiry_stage_id = 4"
						+ " WHERE"
						+ " enquiry_id = " + enquiry_id + "";
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
				SOPError("Error in "
						+ new Exception().getStackTrace()[0].getMethodName()
						+ ": " + e);
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

	public void AddSOVehicle(String so_id, String emp_id, String comp_id, String so_delivered_date) {
		try {
			// SOP("AddSOVehicle==");

			String veh_chassis_no = "", veh_engine_no = "", veh_fastag = "", veh_comm_no = "";
			String veh_variant_id = "0", veh_preowned_model_id = "0", veh_modelyear = "", veh_id = "0";
			String so_branch_id = "0", so_customer_id = "0";
			String model_brand_id = "0";
			String vehfollowup_emp_id = "0", brandconfig_vehfollowup_days = "0";
			String vehcrm_emp_id = "0";
			StrSql = "SELECT vehstock_chassis_no, vehstock_engine_no, vehstock_fastag, so_branch_id, so_contact_id, so_customer_id,"
					+ " vehstock_item_id, vehstock_modelyear, brandconfig_vehfollowup_days,"
					+ " customer_soe_id, customer_sob_id, model_id, model_brand_id ,"
					+ " branch_veh_crm_emp_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " WHERE 1=1 "
					+ " AND so_id = " + so_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql==fetch===" + StrSql);
			while (crs.next()) {
				so_contact_id = crs.getString("so_contact_id");
				so_branch_id = crs.getString("so_branch_id");
				so_customer_id = crs.getString("so_customer_id");
				veh_chassis_no = crs.getString("vehstock_chassis_no");
				veh_engine_no = crs.getString("vehstock_engine_no");
				veh_fastag = crs.getString("vehstock_fastag");
				veh_variant_id = crs.getString("vehstock_item_id");
				veh_modelyear = crs.getString("vehstock_modelyear");
				model_brand_id = crs.getString("model_brand_id");
				vehfollowup_emp_id = CNumeric(crs.getString("branch_veh_crm_emp_id"));
				brandconfig_vehfollowup_days = CNumeric(crs.getString("brandconfig_vehfollowup_days"));
			}
			crs.close();

			if (!vehfollowup_emp_id.equals("0")) {
				vehcrm_emp_id = vehfollowup_emp_id;
			} else {
				// setting only for entry of vehicle
				vehcrm_emp_id = "1";
			}

			StrSql = "SELECT variant_id, variant_preownedmodel_id"
					+ " FROM axelaauto.axela_preowned_variant"
					+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE 1=1"
					+ " AND variant_name =( SELECT item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " WHERE item_id = " + veh_variant_id
					+ " AND model_brand_id = preownedmodel_carmanuf_id )";
			crs = processQuery(StrSql, 0);
			while (crs.next()) {
				veh_variant_id = crs.getString("variant_id");
				veh_preowned_model_id = crs.getString("variant_preownedmodel_id");
			}
			crs.close();

			if (!veh_chassis_no.equals("")) {

				if (model_brand_id.equals("1") || model_brand_id.equals("2")) {
					StrSql = "SELECT" + " veh_id"
							+ " FROM " + compdb(comp_id) + "axela_service_veh"
							+ " WHERE 1=1"
							+ " AND CONCAT(veh_chassis_no,'-',veh_engine_no) = '" + veh_chassis_no + "-" + veh_engine_no + "'";

					// SOP("StrSql==for maruti && general===" + StrSql);
					veh_id = CNumeric(ExecuteQuery(StrSql));
				} else {
					StrSql = "SELECT" + " veh_id"
							+ " FROM " + compdb(comp_id) + "axela_service_veh"
							+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
							+ " WHERE 1=1"
							+ " AND variant_preownedmodel_id = '" + veh_preowned_model_id + "'"
							+ " AND veh_chassis_no = '" + veh_chassis_no + "'";

					veh_id = CNumeric(ExecuteQuery(StrSql));
				}
				// SOP("veh_id==" + veh_id);
				if (veh_id.equals("0")) {
					// SOP("vehfollowup_emp_id==" + vehfollowup_emp_id);
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh"
							+ " ("
							+ "	veh_branch_id,"
							+ " veh_customer_id,"
							+ " veh_contact_id,"
							+ " veh_so_id,"
							+ " veh_variant_id,"
							+ " veh_modelyear,"
							+ " veh_comm_no,"
							+ " veh_chassis_no,"
							+ " veh_engine_no,"
							+ " veh_fastag,"
							+ " veh_sale_date,"
							+ " veh_emp_id,"
							+ " veh_crmemp_id,"
							// + " veh_insurtype_id,"
							+ " veh_notes,"
							+ " veh_entry_id,"
							+ " veh_entry_date"
							+ ")"
							+ " VALUES"
							+ " ("
							+ so_branch_id + ","
							+ so_customer_id + ","
							+ " " + so_contact_id + ","
							+ " " + so_id + ","
							+ " " + veh_variant_id + ","
							+ " '" + veh_modelyear + "',"
							+ " 0,"
							+ " '" + veh_chassis_no + "',"
							+ " '" + veh_engine_no + "',"
							+ " '" + veh_fastag + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 1,"
							+ "" + vehcrm_emp_id + ","
							// + " '1' ," // veh_insurtype_id
							+ " '',"
							+ " " + emp_id + ","
							+ " '" + ToLongDate(kknow()) + "')";
					// SOP("StrSql==insert==" + StrSql);
					veh_id = UpdateQueryReturnID(StrSql);

				} else {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
							+ " SET veh_customer_id = " + so_customer_id + ","
							+ " veh_contact_id = " + so_contact_id + ","
							+ " veh_modified_id = " + emp_id + ","
							+ " veh_modified_date = '" + ToLongDate(kknow()) + "'"
							+ " WHERE veh_id = " + veh_id;
					// SOP("StrSql==update==" + StrSql);
					updateQuery(StrSql);
				}

				if (veh_id.equals("0")) {
					StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_so_id = " + so_id;
					veh_id = CNumeric(ExecuteQuery(StrSql));
				}

				if (!so_delivered_date.equals("") && !veh_id.equals("0")) {

					// // Insert Vehicle Follow-up
					// 5th day call
					if (!vehfollowup_emp_id.equals("0")) {
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_followup"
								+ " ("
								+ "	vehfollowup_veh_id,"
								+ " vehfollowup_emp_id,"
								+ " vehfollowup_vehcalltype_id,"
								+ " vehfollowup_dueservice,"
								+ " vehfollowup_followup_time,"
								+ " vehfollowup_followup_main,"
								+ " vehfollowup_entry_id,"
								+ " vehfollowup_entry_time"
								+ ")"
								+ " VALUES"
								+ " ('"
								+ veh_id + "',"
								+ " " + vehfollowup_emp_id + ","
								+ " 1," // vehfollowup_vehcalltype_id
								+ " '1st Service'," // vehfollowup_dueservice
								+ " CONCAT(DATE_FORMAT(DATE_ADD(" + so_delivered_date + ",INTERVAL " + brandconfig_vehfollowup_days + " DAY),'%Y%m%d'), '100000'),"
								+ " " + 1 + "," // vehfollowup_followup_main
								+ " " + 1 + "," // vehfollowup_entry_id
								+ " '" + ToLongDate(kknow()) + "'"
								+ " )";
						// SOP("StrSql===veh follow===" + StrSql);
						updateQuery(StrSql);
					}

				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto" + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	// To add all the services of main item to SO service table
	public void AddItemServices() throws SQLException {
		try {
			String item_id = "0";
			if (add.equals("yes")) {
				StrSql = "SELECT"
						+ " quoteitem_item_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote on quote_id = quoteitem_quote_id"
						+ " WHERE"
						+ " quoteitem_quote_id = " + so_quote_id + ""
						+ " AND quoteitem_rowcount != 0";
				item_id = CNumeric(ExecuteQuery(StrSql));
			} else {
				StrSql = "SELECT"
						+ " soitem_item_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_so_item"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so on so_id = soitem_so_id"
						+ " WHERE"
						+ " soitem_so_id = " + so_id + ""
						+ " AND soitem_rowcount != 0";
				item_id = CNumeric(ExecuteQuery(StrSql));
			}

			// StrSql = "SELECT " + so_id + ","
			// + " itemservice_jctype_id,"
			// + " itemservice_kms,"
			// + " DATE_FORMAT(DATE_ADD(" + so_delivered_date +
			// ",INTERVAL itemservice_days DAY),'%Y%m%d%H%i%S')"
			// + " FROM " + compdb(comp_id) + "axela_inventory_item_service"
			// + " WHERE itemservice_item_id = " + item_id + "";
			//
			// StrSql = "INSERT INTO " + compdb(comp_id) +
			// "axela_sales_so_service"
			// + " (soservice_so_id,"
			// + " soservice_jctype_id,"
			// + " soservice_kms,"
			// + " soservice_date)"
			// + " " + StrSql + "";
			// stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	void CheckPageperm(HttpServletResponse response) {
		try {
			StrSql = "SELECT"
					+ " so_id FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = so_branch_id"
					+ " WHERE"
					+ " so_id = " + so_id + BranchAccess + "";
			if (ExecuteQuery(StrSql).equals("")) {
				response.sendRedirect("../portal/error.jsp?msg=Access denied!");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	protected void SendEmail() throws SQLException {
		emailmsg = (brandconfig_so_email_format);
		emailsub = (brandconfig_so_email_sub);

		emailsub = "REPLACE('" + emailsub + "', '[SOID]',so_id)";
		emailsub = "REPLACE(" + emailsub + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERID]',customer_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERNAME]',customer_name)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTPHONE1]',contact_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTEMAIL1]',contact_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXENAME]',emp_name)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEJOBTITLE]',jobtitle_desc)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEMOBILE1]',emp_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEPHONE1]',emp_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEEMAIL1]',emp_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[MODELNAME]',model_name)";
		emailsub = "REPLACE(" + emailsub + ", '[ITEMNAME]',item_name)";
		emailsub = "REPLACE(" + emailsub + ", '[BRANCHNAME]',branch_name)";
		emailsub = "REPLACE(" + emailsub + ", '[BRANCHADDRESS]',branch_add)";

		emailmsg = "REPLACE('" + emailmsg + "', '[SOID]',so_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERID]',customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERNAME]',customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXENAME]',emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEPHONE1]',emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEEMAIL1]',emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[MODELNAME]',model_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[ITEMNAME]',item_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHNAME]',branch_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " '" + so_contact_id + "',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '" + branch_email1 + "',"
					+ " '" + contact_email1 + "',"
					+ " " + emailsub + ","
					+ " " + emailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " WHERE"
					+ " so_id = " + so_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql==="+StrSqlBreaker(StrSql));
			// SOP("StrSql-------2-----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
		}
	}

	protected void SendSODeliveredEmail() throws SQLException {
		emailmsg = (brandconfig_so_delivered_email_format);
		emailsub = (brandconfig_so_delivered_email_sub);

		emailsub = "REPLACE('" + emailsub + "', '[SOID]',so_id)";
		emailsub = "REPLACE(" + emailsub + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERID]',customer_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERNAME]',customer_name)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTPHONE1]',contact_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTEMAIL1]',contact_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[DELIVEREDDATE]', DATE_FORMAT(so_delivered_date, '%d/%m/%Y'))";
		emailsub = "REPLACE(" + emailsub + ", '[EXENAME]',emp_name)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEJOBTITLE]',jobtitle_desc)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEMOBILE1]',emp_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEPHONE1]',emp_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEEMAIL1]',emp_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[MODELNAME]',model_name)";
		emailsub = "REPLACE(" + emailsub + ", '[ITEMNAME]',item_name)";
		emailsub = "REPLACE(" + emailsub + ", '[BRANCHNAME]',branch_name)";
		emailsub = "REPLACE(" + emailsub + ", '[BRANCHADDRESS]',branch_add)";

		emailmsg = "REPLACE('" + emailmsg + "', '[SOID]',so_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERID]',customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERNAME]',customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTNAME]', CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[DELIVEREDDATE]', DATE_FORMAT(so_delivered_date, '%d/%m/%Y'))";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXENAME]',emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEPHONE1]',emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEEMAIL1]',emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[MODELNAME]',model_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[ITEMNAME]',item_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHNAME]',branch_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " '" + so_contact_id + "',"
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '" + branch_email1 + "'," + " '" + contact_email1
					+ "'," + " " + emailsub + "," + " " + emailmsg + "," + " '"
					+ ToLongDate(kknow()) + "'," + " " + emp_id + "," + " 0"
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " WHERE"
					+ " so_id = " + so_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql-------3-----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
		}
	}

	protected void SendSMS() throws SQLException {
		smsmsg = (brandconfig_so_sms_format);

		smsmsg = " REPLACE('" + smsmsg + "', '[SOID]',so_id)";
		smsmsg = " REPLACE(" + smsmsg + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		smsmsg = " REPLACE(" + smsmsg + ", '[CUSTOMERID]',customer_id)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CUSTOMERNAME]',customer_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXENAME]',emp_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEPHONE1]',emp_phone1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEEMAIL1]',emp_email1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[MODELNAME]',model_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[ITEMNAME]',item_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[BRANCHNAME]',branch_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " " + so_contact_id + ","
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '" + contact_mobile1 + "'," + " " + smsmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " WHERE"
					+ " so_id = " + so_id + "";
			// SOP("StrSql--------se----" + StrSqlBreaker(StrSql));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " ("
					+ "	sms_branch_id,"
					+ "	sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql==sms==" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
		}
	}

	protected void SendSODeliveredSMS() throws SQLException {
		smsmsg = (brandconfig_so_delivered_sms_format);

		smsmsg = " REPLACE('" + smsmsg + "', '[SOID]',so_id)";
		smsmsg = " REPLACE(" + smsmsg + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		smsmsg = " REPLACE(" + smsmsg + ", '[CUSTOMERID]',customer_id)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CUSTOMERNAME]',customer_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[DELIVEREDDATE]', DATE_FORMAT(so_delivered_date, '%d/%m/%Y'))";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXENAME]',emp_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEPHONE1]',emp_phone1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[EXEEMAIL1]',emp_email1)";
		smsmsg = " REPLACE(" + smsmsg + ", '[MODELNAME]',model_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[ITEMNAME]',item_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[BRANCHNAME]',branch_name)";
		smsmsg = " REPLACE(" + smsmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT "
					+ "	so_branch_id,"
					+ " " + so_contact_id + ","
					+ " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '" + contact_mobile1 + "',"
					+ " " + smsmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " WHERE"
					+ " so_id = " + so_id + "";
			// SOP("selects sms----------------" + StrSqlBreaker(StrSql));
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " ("
					+ "	sms_branch_id,"
					+ "	sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id"
					+ ")"
					+ " " + StrSql + "";
			// SOP("insert into sms----------------" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendEmailToExecutive() throws SQLException {
		exeemailmsg = (brandconfig_so_email_exe_format);
		exeemailsub = (brandconfig_so_email_exe_sub);

		exeemailsub = "REPLACE('" + exeemailsub + "', '[SOID]',so_id)";
		exeemailsub = " REPLACE(" + exeemailsub + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CUSTOMERID]',customer_id)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CUSTOMERNAME]',customer_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTMOBILE1]',contact_mobile1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTPHONE1]',contact_phone1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[CONTACTEMAIL1]',contact_email1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXENAME]',emp_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEJOBTITLE]',jobtitle_desc)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEMOBILE1]',emp_mobile1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEPHONE1]',emp_phone1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[EXEEMAIL1]',emp_email1)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[MODELNAME]',model_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[ITEMNAME]',item_name)";
		exeemailsub = " REPLACE(" + exeemailsub + ", '[BRANCHNAME]',branch_name)";
		exeemailsub = "REPLACE(" + exeemailsub + ", '[BRANCHADDRESS]',branch_add)";

		exeemailmsg = "REPLACE('" + exeemailmsg + "', '[SOID]',so_id)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CUSTOMERID]',customer_id)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CUSTOMERNAME]',customer_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXENAME]',emp_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEPHONE1]',emp_phone1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[EXEEMAIL1]',emp_email1)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[MODELNAME]',model_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[ITEMNAME]',item_name)";
		exeemailmsg = " REPLACE(" + exeemailmsg + ", '[BRANCHNAME]',branch_name)";
		exeemailmsg = "REPLACE(" + exeemailmsg + ", '[BRANCHADDRESS]',branch_add)";

		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " " + emp_id + ","
					+ " '" + emp_name + "',"
					+ " '" + branch_email1 + "',"
					+ " '" + emp_email + "',"
					+ " " + exeemailsub + ","
					+ " " + exeemailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0," + " "
					+ emp_id + ""
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " WHERE"
					+ " so_id = " + so_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_emp_id,"
					+ " email_emp,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql-------5-----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
		}
	}

	protected void SendSMSToExecutive(String mobile) throws SQLException {
		exesmsmsg = (brandconfig_so_sms_exe_format);

		exesmsmsg = "REPLACE('" + exesmsmsg + "', '[SOID]',so_id)";
		exesmsmsg = " REPLACE(" + exesmsmsg + ", '[SODATE]', DATE_FORMAT(so_date, '%d/%m/%Y'))";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[CUSTOMERID]',customer_id)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[CUSTOMERNAME]',customer_name)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[CONTACTNAME]',CONCAT(title_desc, ' ', contact_fname,' ', contact_lname))";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[CONTACTJOBTITLE]',contact_jobtitle)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[CONTACTMOBILE1]',contact_mobile1)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[CONTACTPHONE1]',contact_phone1)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[CONTACTEMAIL1]',contact_email1)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[EXENAME]',emp_name)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[EXEJOBTITLE]',jobtitle_desc)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[EXEMOBILE1]',emp_mobile1)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[EXEPHONE1]',emp_phone1)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[EXEEMAIL1]',emp_email1)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[MODELNAME]',model_name)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[ITEMNAME]',item_name)";
		exesmsmsg = " REPLACE(" + exesmsmsg + ", '[BRANCHNAME]',branch_name)";
		exesmsmsg = "REPLACE(" + exesmsmsg + ", '[BRANCHADDRESS]',branch_add)";
		// SOP("mobile--------------" + mobile);
		try {
			StrSql = "SELECT"
					+ "	so_branch_id,"
					+ " " + emp_id + ","
					+ " '" + emp_name + "',"
					+ " '" + mobile + "',"
					+ " " + exesmsmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM "
					+ compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = so_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = so_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = so_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle on jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_so_item on soitem_so_id = so_id"
					+ " AND soitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item on item_id = soitem_item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id"
					+ " WHERE"
					+ " so_id = " + so_id + "";
			// SOP("StrSql--------se-----" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " ("
					+ "	sms_branch_id,"
					+ "	sms_emp_id,"
					+ " sms_emp,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id"
					+ ")"
					+ " " + StrSql + "";
			// SOP("StrSql-------6-----" + StrSql);
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ e);
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT"
					+ " COALESCE (brandconfig_so_email_enable,'') AS brandconfig_so_email_enable,"
					+ " COALESCE (brandconfig_so_email_sub,'') AS brandconfig_so_email_sub,"
					+ " COALESCE (brandconfig_so_email_format,'') AS brandconfig_so_email_format,"
					+ " COALESCE (brandconfig_so_email_exe_enable,'') AS brandconfig_so_email_exe_enable,"
					+ " COALESCE (brandconfig_so_email_exe_sub,'') AS brandconfig_so_email_exe_sub,"
					+ " COALESCE (brandconfig_so_email_exe_format,'') AS brandconfig_so_email_exe_format,"
					+ " COALESCE (brandconfig_so_sms_enable,'') AS brandconfig_so_sms_enable,"
					+ " COALESCE (brandconfig_so_sms_format,'') AS brandconfig_so_sms_format,"
					+ " COALESCE (brandconfig_so_sms_exe_enable,'') AS brandconfig_so_sms_exe_enable,"
					+ " COALESCE (brandconfig_so_sms_exe_format,'') AS brandconfig_so_sms_exe_format,"
					+ " COALESCE (brandconfig_so_delivered_email_enable,'') AS brandconfig_so_delivered_email_enable,"
					+ " COALESCE (brandconfig_so_delivered_email_sub,'') AS brandconfig_so_delivered_email_sub,"
					+ " COALESCE (brandconfig_so_delivered_email_format,'') AS brandconfig_so_delivered_email_format,"
					+ " COALESCE (brandconfig_so_delivered_sms_enable,'') AS brandconfig_so_delivered_sms_enable,"
					+ " COALESCE (brandconfig_so_delivered_sms_format,'') AS brandconfig_so_delivered_sms_format,"
					+ " COALESCE(IF(emp.emp_email1 != '', emp.emp_email1, emp.emp_email2), '') AS emp_email,"
					+ " COALESCE(emp.emp_name, '') AS emp_name, COALESCE(branch_email1, '') AS branch_email1,"
					+ " COALESCE(IF(emp.emp_mobile1 != '', emp.emp_mobile1, emp.emp_mobile2), '') AS emp_mobile,"
					+ " config_admin_email, config_email_enable, config_sms_enable, comp_sms_enable,"
					+ " config_sales_so_refno, config_customer_dupnames, comp_email_enable, "
					+ " COALESCE(branch_sales_mobile, '') AS branch_sales_mobile,"
					+ " COALESCE(emp.emp_so_priceupdate, '') AS emp_so_priceupdate,"
					+ " COALESCE(emp.emp_so_discountupdate, '') AS emp_so_discountupdate"
					+ " FROM "
					+ compdb(comp_id) + "axela_config, "
					+ compdb(comp_id) + "axela_comp, "
					+ compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + branch_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp on emp.emp_id = " + emp_id + ""
					+ " WHERE"
					+ " admin.emp_id = " + emp_id + "";
			// SOP("Strsql---------------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {

				brandconfig_so_email_enable = crs.getString("brandconfig_so_email_enable");
				brandconfig_so_email_format = crs.getString("brandconfig_so_email_format");
				brandconfig_so_email_sub = crs.getString("brandconfig_so_email_sub");
				brandconfig_so_email_exe_enable = crs.getString("brandconfig_so_email_exe_enable");
				brandconfig_so_email_exe_format = crs.getString("brandconfig_so_email_exe_format");
				brandconfig_so_email_exe_sub = crs.getString("brandconfig_so_email_exe_sub");
				brandconfig_so_sms_enable = crs.getString("brandconfig_so_sms_enable");
				brandconfig_so_sms_format = crs.getString("brandconfig_so_sms_format");
				brandconfig_so_sms_exe_enable = crs.getString("brandconfig_so_sms_exe_enable");
				brandconfig_so_sms_exe_format = crs.getString("brandconfig_so_sms_exe_format");
				brandconfig_so_delivered_email_enable = crs.getString("brandconfig_so_delivered_email_enable");
				brandconfig_so_delivered_email_format = crs.getString("brandconfig_so_delivered_email_format");
				brandconfig_so_delivered_email_sub = crs.getString("brandconfig_so_delivered_email_sub");
				brandconfig_so_delivered_sms_enable = crs.getString("brandconfig_so_delivered_sms_enable");
				brandconfig_so_delivered_sms_format = crs.getString("brandconfig_so_delivered_sms_format");
				branch_sales_mobile = crs.getString("branch_sales_mobile");
				emp_name = crs.getString("emp_name");
				emp_email = crs.getString("emp_email");
				emp_mobile = crs.getString("emp_mobile");
				branch_email1 = crs.getString("branch_email1");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_sales_so_refno = crs.getString("config_sales_so_refno");
				emp_so_priceupdate = crs.getString("emp_so_priceupdate");
				emp_so_discountupdate = crs.getString("emp_so_discountupdate");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public String PopulateState(String state_id, String span_id,
			String dr_state_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT"
					+ " state_id, state_name"
					+ " FROM "
					+ compdb(comp_id) + "axela_state"
					+ " ORDER BY state_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_state_id).append(" id=")
					.append(dr_state_id);
			Str.append(" class=\"selectbox\" onchange=\"showHint('../portal/location.jsp?state_id=' + GetReplace(this.value)+'&dr_city_id=dr_contact_city_id','");
			Str.append(span_id).append("');\">\n");
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("state_id"));
				Str.append(StrSelectdrop(crs.getString("state_id"), state_id));
				Str.append(">").append(crs.getString("state_name"))
						.append("</option>\n");
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
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
			StrSql = "SELECT"
					+ " city_id, city_name"
					+ " FROM "
					+ compdb(comp_id) + "axela_city"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE"
					+ " city_state_id = " + state_id + ""
					+ " ORDER by"
					+ " city_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<select name=").append(dr_city_id).append(" id=")
					.append(dr_city_id).append(" class=\"selectbox\">\n");
			Str.append("<option value=\"0\">Select</option>\n");
			if (!state_id.equals("0")) {
				while (crs.next()) {
					Str.append("<option value=")
							.append(crs.getString("city_id"));
					Str.append(StrSelectdrop(crs.getString("city_id"), city_id));
					Str.append(">").append(crs.getString("city_name"))
							.append("</option>\n");
				}
			}
			Str.append("</select>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return "";
		}
	}

	protected void PopulateContactDetails() {
		try {
			if (!contact_id.equals("0") || !so_contact_id.equals("0")) {
				StrSql = "SELECT"
						+ " customer_id, contact_id, customer_name, contact_fname,"
						+ " contact_lname, contact_email1, contact_mobile1, title_desc"
						+ " FROM "
						+ compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id";
				if (!contact_id.equals("0")) {
					StrSql += " WHERE"
							+ " contact_id = " + contact_id + "";
				} else if (!so_contact_id.equals("0")) {
					StrSql += " WHERE"
							+ " contact_id = " + so_contact_id + "";
				}
				// SOP("PopulateContactDetails===" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					so_customer_id = crs.getString("customer_id");
					so_contact_id = crs.getString("contact_id");
					contact_email1 = crs.getString("contact_email1");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id="
							+ crs.getString("customer_id")
							+ "\">"
							+ crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id="
							+ crs.getString("contact_id")
							+ "\">"
							+ crs.getString("contact_fname")
							+ " "
							+ crs.getString("contact_lname") + "</a>";
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

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			Str.append("<option value=\"0\">Select Sales Consultant</option>\n");

			StrSql = "SELECT"
					+ " emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM "
					+ compdb(comp_id) + "axela_emp"
					+ " WHERE"
					+ " emp_active = 1"
					+ " AND emp_sales = 1"
					+ " AND (emp_branch_id = " + branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM "
					+ compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE "
					+ compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + branch_id + "))"
					+ " GROUP BY"
					+ " emp_id" + " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), so_emp_id));
				Str.append(">").append(crs.getString("emp_name"))
						.append("</option>\n");
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

	public String PopulateTitle() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id)
					+ "axela_title" + " ORDER BY title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(StrSelectdrop(crs.getString("title_id"),
						contact_title_id));
				Str.append(">").append(crs.getString("title_desc"))
						.append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateCancelReason() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT cancelreason_id, cancelreason_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_cancelreason"
					+ " ORDER BY cancelreason_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("cancelreason_id"));
				Str.append(StrSelectdrop(crs.getString("cancelreason_id"), so_cancelreason_id));
				Str.append(">").append(crs.getString("cancelreason_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	public String PopulateFinanceType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fintype_id, fintype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_type"
					+ " GROUP BY fintype_id"
					+ " ORDER BY fintype_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fintype_id"));
				Str.append(StrSelectdrop(crs.getString("fintype_id"), so_fintype_id));
				Str.append(">").append(crs.getString("fintype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateFinanceBy() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\"> Select </option>\n");
		try {
			StrSql = "SELECT fincomp_id, fincomp_name"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = 1"
					+ " ORDER BY fincomp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fincomp_id"));
				Str.append(StrSelectdrop(crs.getString("fincomp_id"), so_fincomp_id));
				Str.append(">").append(crs.getString("fincomp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	// public String PopulateFinance() {
	// StringBuilder Str = new StringBuilder();
	// Str.append("<option value=\"0\">Select</option>\n");
	// try {
	// StrSql = "SELECT sofinance_id, sofinance_name"
	// + " FROM " + compdb(comp_id) + "axela_sales_so_finance"
	// + " ORDER BY sofinance_rank";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// while (crs.next()) {
	// Str.append("<option value=").append(crs.getString("sofinance_id"));
	// Str.append(StrSelectdrop(crs.getString("sofinance_id"), so_finance_id));
	// Str.append(">").append(crs.getString("sofinance_name")).append("</option>\n");
	// }
	// crs.close();
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new
	// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str.toString();
	// }
	public String PopulateMonth(String month) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value = -1>Select</option>");
		Str.append("<option value = 1").append(Selectdrop(1, month)).append(">January</option>\n");
		Str.append("<option value = 2").append(Selectdrop(2, month)).append(">February</option>\n");
		Str.append("<option value = 3").append(Selectdrop(3, month)).append(">March</option>\n");
		Str.append("<option value = 4").append(Selectdrop(4, month)).append(">April</option>\n");
		Str.append("<option value = 5").append(Selectdrop(5, month)).append(">May</option>\n");
		Str.append("<option value = 6").append(Selectdrop(6, month)).append(">June</option>\n");
		Str.append("<option value = 7").append(Selectdrop(7, month)).append(">July</option>\n");
		Str.append("<option value = 8").append(Selectdrop(8, month)).append(">August</option>\n");
		Str.append("<option value = 9").append(Selectdrop(9, month)).append(">September</option>\n");
		Str.append("<option value = 10").append(Selectdrop(10, month)).append(">October</option>\n");
		Str.append("<option value = 11").append(Selectdrop(11, month)).append(">November</option>\n");
		Str.append("<option value = 12").append(Selectdrop(12, month)).append(">December</option>\n");
		return Str.toString();
	}

	public String PopulateDay(String day) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =-1>Select</option>");
		for (int i = 1; i <= 31; i++) {
			Str.append("<option value=").append(i).append(Selectdrop(i, day));
			Str.append(">").append(i).append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateYear(String year) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =-1>Select</option>");
		for (int i = 1920; i <= Integer.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow())))); i++) {
			Str.append("<option value =").append(i).append("");
			Str.append(Selectdrop(i, year)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateColour() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT option_id, option_name"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
				+ " WHERE 1 = 1"
				+ " AND option_brand_id = " + branch_brand_id + "";
		if (branch_brand_id.equals("56")) {
			StrSql += " AND option_optiontype_id = 1";
		}
		StrSql += " GROUP BY option_id"
				+ " ORDER BY option_id";
		// SOP("StrSql==" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		Str.append("<option value='0'>Select</option>");
		try {
			while (crs.next()) {
				Str.append("<option value=" + crs.getString("option_id") + " ");
				Str.append(StrSelectdrop(crs.getString("option_id"), so_option_id)).append(">");
				Str.append(crs.getString("option_name")).append("</option>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public void AddCustomCRMFields(String so_id, String crmtype, String so_inactive, String comp_id) throws SQLException {
		try {
			String sodate = "";
			if (crmtype.equals("2") && so_inactive.equals("0")) {
				sodate = "so_date";
			} else if (crmtype.equals("2") && so_inactive.equals("1")) {
				sodate = "so_cancel_date";
			} else if (crmtype.equals("3") && so_inactive.equals("0")) {
				sodate = "so_delivered_date";
			}

			StrSql = "SELECT"
					+ " so_enquiry_id, so_id,"
					+ " COALESCE((SELECT CASE WHEN crmdays_exe_type = 1 "
					+ " THEN (CASE WHEN crmdays_crmtype_id = 1 THEN team_crm_emp_id "
					+ " WHEN crmdays_crmtype_id = 2 THEN team_pbf_emp_id "
					+ " WHEN crmdays_crmtype_id = 3 THEN team_psf_emp_id END)"
					+ " WHEN crmdays_exe_type = 2 THEN so_emp_id"
					+ " WHEN crmdays_exe_type = 3 THEN team_emp_id"
					+ " WHEN crmdays_exe_type = 4 THEN team_servicepsf_emp_id END"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
					+ " WHERE" + " team_branch_id = so_branch_id"
					+ " AND teamtrans_emp_id = so_emp_id"
					+ " LIMIT 1), so_emp_id) as crmempid, "
					+ " crmdays_id, "
					+ " DATE_FORMAT(DATE_ADD(concat(substr(" + sodate + ", 1, 8), "
					+ " SUBSTR('" + ToLongDate(kknow()) + "', 9, 14)), INTERVAL (crmdays_daycount-1) DAY), '%Y%m%d%H%i%s')"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = so_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_brand_id = branch_brand_id"
					+ "	LEFT JOIN " + compdb(comp_id) + "axela_sales_crm ON crm_crmdays_id = crmdays_id AND crm_so_id = so_id"
					+ " WHERE"
					+ " so_id = " + so_id
					+ " AND crmdays_so_inactive = " + so_inactive
					+ " AND crmdays_active = 1 "
					+ " AND crmdays_crmtype_id=" + crmtype + "";
			if (crmtype.equals("2")) {
				StrSql += " AND crmdays_waitingperiod='0'";
			}
			StrSql += " AND crm_id IS NULL";

			// SOP("StrSql===" + StrSql);
			// StrSql += " AND concat(so_enquiry_id, '-', so_id, '-', crmdays_id) NOT IN "
			// + " (SELECT concat(crm_enquiry_id, '-', crm_so_id, '-', crm_crmdays_id)"
			// + " FROM " + compdb(comp_id) + "axela_sales_crm)";

			StrSql = " INSERT INTO " + compdb(comp_id) + "axela_sales_crm"
					+ " ("
					+ "	crm_enquiry_id,"
					+ " crm_so_id,"
					+ " crm_emp_id,"
					+ " crm_crmdays_id,"
					+ " crm_followup_time"
					+ ")"
					+ StrSql;
			// SOP("StrSql==CRM=====" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

	public void UpdateProfitability(String so_id, String comp_id) throws SQLException {
		StringBuilder Str = new StringBuilder();
		int daydiff = 0;
		String vehstock_invoice_date = "";
		double saleprice = 0.0, purchasevalue = 0.0, grossmargin = 0.0, servicemargin = 0.0, netmargin = 0.0;
		double principalsupport = 0.0, financepayout = 0.0, insurancepayout = 0.0, accessoriespaidat = 0.0, totalincome = 0.0;
		double offergiven = 0.0, focamount = 0.0, totaldiscount = 0.0, custoffer = 0.0, intrestOnInventory = 0.0, totalexpense = 0.0, profitorloss = 0.0;
		String update_so_id = "";
		StrSql = "SELECT so_id, COALESCE (vehstock_id, 0) AS vehstock_id,"
				+ " COALESCE (preownedstock_id, 0) AS preownedstock_id,"
				+ "	COALESCE (vouchertrans_price, 0 ) AS vouchertrans_price,"
				+ "	COALESCE (( so_exprice * ( 100 / ( COALESCE (COALESCE(tax.customer_rate,0) + COALESCE (cess.customer_rate,0), 0) + 100 ) ) ), 0 ) AS saleprice,"
				+ "	COALESCE (( vehstock_invoice_amount * ( 100 / ( COALESCE (COALESCE(tax.customer_rate,0) + COALESCE (cess.customer_rate,0), 0) + 100 ) ) ), 0 ) AS purchasevalue,"
				+ " COALESCE (vehstock_invoiceamountaftertax, 0) AS invoiceamountaftertax,"
				+ "	COALESCE (preownedstock_purchase_amt, 0) AS preowned_purchasevalue,"
				+ " COALESCE(so_finance_net,0) AS financepayout,"
				+ " COALESCE(so_insur_net,0) AS insurancepayout,"
				+ " COALESCE(so_mga_paid,0) AS accessories,"
				+ " COALESCE(so_discamt,0) AS totaldiscount,"
				+ " COALESCE(SUM(so_offer_consumer+IF(so_insur_type_id =1, so_insur_net, 0)+IF(so_ew_type_id=1 ,so_ew_payout, 0)),0) AS custoffer,"
				+ " COALESCE(so_mga_foc_amount,0) AS focamount,"
				+ " COALESCE(vehstock_invoice_date,'') AS vehstock_invoice_date,"
				+ " COALESCE(so_retail_date,'') AS so_retail_date,"
				+ " COALESCE(model_service_margin,0) AS servicemargin,"
				+ " COALESCE(SUM("
				+ " IF(so_offer_loyalty_bonus != 0 ,principalsupport_loyalty, 0)"
				+ " + IF(so_offer_govtempscheme != 0 ,principalsupport_govtempscheme, 0)"
				+ " + IF(so_offer_corporate != 0 ,principalsupport_corporate, 0)"
				+ " + IF(so_offer_spcl_scheme != 0, principalsupport_monthlyadnbenefit, 0) "
				+ " + IF(so_offer_consumer != 0, principalsupport_cashdiscount, 0)"
				+ " + IF(so_exchange = 1 AND so_exchange_amount != 0 ,principalsupport_exchange, 0)"
				+ " + IF(so_ew_amount != 0, principalsupport_extwty , 0)"
				+ " + IF(so_insur_amount != 0, principalsupport_insurance, 0)),0) AS principalsupport"

				// + " COALESCE(vehstock_principalsupport, 0) AS principalsupport"

				+ " FROM "
				+ compdb(comp_id) + "axela_sales_so"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
				+ "	LEFT JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_id = so_preownedstock_id"
				+ "	LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher ON voucher_so_id = so_id"
				+ " AND voucher_active = '1' AND voucher_vouchertype_id = 6"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " AND vouchertrans_rowcount != 0 AND vouchertrans_option_id = 0"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_insurance_policy ON insurpolicy_so_id = so_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = IF(voucher_id != 0, vouchertrans_item_id, so_item_id )"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ "	LEFT JOIN " + compdb(comp_id) + "axela_customer tax ON tax.customer_id = item_salestax3_ledger_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_customer cess ON cess.customer_id = item_salestax4_ledger_id"

				// -------------profitability start------------
				+ " LEFT JOIN (SELECT "
				+ " principalsupport_extwty,"
				+ " principalsupport_insurance,"
				+ " principalsupport_cashdiscount,"
				+ " principalsupport_exchange,"
				+ " principalsupport_loyalty,"
				+ " principalsupport_govtempscheme,"
				+ " principalsupport_monthlyadnbenefit,"
				+ " principalsupport_corporate,"
				+ " principalsupport_model_id,"
				+ " principalsupport_fueltype_id,"
				+ " principalsupport_month"
				+ " FROM " + compdb(comp_id) + "axela_principal_support"
				+ " WHERE 1 = 1 "
				+ " ) AS tblsupport ON tblsupport.principalsupport_model_id = item_model_id"
				+ "  AND tblsupport.principalsupport_fueltype_id = item_fueltype_id"
				+ "  AND SUBSTR(tblsupport.principalsupport_month, 1, 6) = SUBSTR(vehstock_dms_date, 1, 6)"

				// --------------profitability end-------

				+ " WHERE item_type_id = 1";
		if (!so_id.equals("0")) {
			StrSql += " AND so_id = " + so_id;
		} else {
			StrSql += " AND so_id IN ( "
					+ " SELECT so_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_so"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = so_vehstock_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
					+ " WHERE 1=1 "
					+ " AND item_model_id = " + profitability_principalsupport_model_id
					+ " AND item_fueltype_id = " + profitability_principalsupport_fueltype_id
					+ " AND SUBSTR(vehstock_dms_date, 1, 6) = " + profitability_principalsupport_month
					+ " )"
					+ " GROUP BY so_id";
		}

		CachedRowSet crs = processQuery(StrSql, 0);
		// SOPInfo("StrSql=profitability===select222====" + StrSql);

		try {
			while (crs.next()) {
				update_so_id = crs.getString("so_id");
				if (!crs.getString("vouchertrans_price").equals("0.0") && !crs.getString("vouchertrans_price").equals("0")) {
					saleprice = crs.getDouble("vouchertrans_price");
				} else {
					saleprice = crs.getDouble("saleprice");
				}
				if (!crs.getString("vehstock_id").equals("0")) {
					if (!crs.getString("invoiceamountaftertax").equals("0.0")) {
						purchasevalue = crs.getDouble("invoiceamountaftertax");
					} else {
						purchasevalue = crs.getDouble("purchasevalue");
					}
				} else if (!crs.getString("preownedstock_id").equals("0")) {
					purchasevalue = crs.getDouble("preowned_purchasevalue");
				}
				grossmargin = saleprice - purchasevalue;

				servicemargin = crs.getDouble("servicemargin");

				netmargin = grossmargin - servicemargin;

				principalsupport = crs.getDouble("principalsupport");

				financepayout = crs.getDouble("financepayout");

				insurancepayout = crs.getDouble("insurancepayout");

				accessoriespaidat = (crs.getDouble("accessories") * 20) / 100;

				totalincome = netmargin + principalsupport + financepayout + insurancepayout + accessoriespaidat;

				custoffer = crs.getDouble("custoffer"); // consumer Offer
														// + INSURANCE Gross Payout (IF TYPE FOC)
														// + EW Gross Payout (IF TYPE FOC)
														// + 80 % of Accessories FOC Amount

				focamount = (crs.getDouble("focamount") * 80) / 100;

				offergiven = custoffer + focamount;

				so_retail_date = crs.getString("so_retail_date");
				vehstock_invoice_date = crs.getString("vehstock_invoice_date");

				if (so_retail_date.equals("")) {
					so_retail_date = ToLongDate(kknow());
				}
				if (!vehstock_invoice_date.equals("")) {
					daydiff = (int) getDaysBetween(vehstock_invoice_date, so_retail_date) + 1;
				} else {
					daydiff = 0;
				}

				intrestOnInventory = (15 * daydiff * purchasevalue) / (100 * 365);

				totalexpense = offergiven + intrestOnInventory;

				profitorloss = totalincome - totalexpense;
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_so"
						+ " SET so_profitability_salesprice = " + deci.format(saleprice) + ","
						+ " so_profitability_grossinvoiceamount = " + deci.format(purchasevalue) + ","
						+ " so_profitability_grossmargin = " + deci.format(grossmargin) + ","
						+ " so_profitability_servicemargin = " + deci.format(servicemargin) + ","
						+ " so_profitability_netmargin = " + deci.format(netmargin) + ","
						+ " so_profitability_principalsupport = " + deci.format(principalsupport) + ","
						+ " so_profitability_paidaccessories = " + deci.format(accessoriespaidat) + ","
						+ " so_profitability_totalincome = " + deci.format(totalincome) + ","
						+ "	so_profitability_offercost = " + deci.format(offergiven) + ","
						+ " so_profitability_inventoryinterest = " + deci.format(intrestOnInventory) + ","
						+ " so_profitability_totalexpense = " + deci.format(totalexpense) + ","
						+ " so_profitability_profit = " + deci.format(profitorloss)
						+ " WHERE 1=1";
				if (!so_id.equals("0")) {
					StrSql += " AND so_id = " + so_id;
				} else {
					StrSql += " AND so_id = " + update_so_id;
				}
				// SOPInfo("StrSql=profitability===update222==" + StrSql);
				updateQuery(StrSql);

			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
