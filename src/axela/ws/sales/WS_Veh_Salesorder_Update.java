//Manjur, 18th April, 2015
package axela.ws.sales;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import axela.sales.Veh_Salesorder_Update;
import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Veh_Salesorder_Update extends ConnectWS {
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String so_emp_id = "0";
	public String enquiry_id = "0";
	public String so_id = "0";
	public String so_customer_id = "0";
	public String so_contact_id = "0";
	public String so_branch_id = "0";
	public String emp_uuid = "0";
	public String branch_id = "0";
	public String branch_so_email_enable = "";
	public String branch_so_email_format = "";
	public String branch_so_sms_enable = "";
	public String branch_so_sms_format = "";
	public String branch_so_email_sub = "";
	public String branch_so_delivered_email_enable = "";
	public String branch_so_delivered_email_format = "";
	public String branch_so_delivered_sms_enable = "";
	public String branch_so_delivered_sms_format = "";
	public String branch_so_delivered_email_sub = "";
	public String branch_so_email_exe_sub = "";
	public String branch_so_email_exe_format = "";
	public String branch_so_sms_exe_format = "";
	public String config_admin_email = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_customer_dupnames = "";
	public String config_sales_so_refno = "";
	public String emp_so_priceupdate = "";
	public String emp_so_discountupdate = "";
	public String so_quote_id = "0";
	public String so_vehstock_id = "0";
	public String so_option_id = "0";
	public String so_allot_no = "";
	public String so_fintype_id = "0";
	public String so_fincomp_id = "";
	public String so_finance_amt = "";
	public String so_mga_amount = "", so_refund_amount = "", so_booking_amount = "";
	public String so_po = "";
	public String so_retaildate = "";
	public String so_delivereddate = "";
	public String so_reg_no = "0";
	public String so_regdate = "";
	public String so_dob = "";
	public String dr_day = "";
	public String dr_month = "";
	public String dr_year = "";
	public String so_pan = "";
	public String so_refno = "";
	public String so_canceldate = "";
	public String sodate = "";
	public String so_date = "";
	public String so_paymentdate = "";
	public String so_promisedate = "";
	public String so_form60 = "";
	public String so_open = "0";
	public String so_exchange = "";
	public String so_active = "0";
	public String so_notes = "";
	public String lead_id = "0";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String rateclass_id = "0";
	public String branch_name = "";
	public String so_item_id = "0";
	public String so_grandtotal = "";
	public String so_totaltax = "";
	public String so_discamt = "";
	public String so_netamt = "";
	public String so_exprice = "";
	public String add = "";
	public String addB = "";
	public String msg = "";
	DecimalFormat df = new DecimalFormat("0.00");
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<>();
	Map<String, String> map = new HashMap<>();
	JSONArray jarr_config;

	public JSONObject SalesorderUpdate(JSONObject input) throws Exception {
		SOP("input==" + input);
		try {

			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}

			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			}

			if (!input.isNull("so_quote_id")) {
				so_quote_id = CNumeric(PadQuotes((String) input.get("so_quote_id")));
			}

			if (!input.isNull("add")) {
				add = PadQuotes((String) input.get("add"));
			}

			if (!input.isNull("addB")) {
				addB = PadQuotes((String) input.get("addB"));
			}

			// if (!enquiry_id.equals("0") && add.equals("yes") || addB.equals("yes")) {
			// GetEnquiryDetails();
			// }
			if (add.equals("yes") && !addB.equals("yes")) {
				if (!so_quote_id.equals("0")) {
					GetConfigurationDetails(so_quote_id);
					GetQuoteDetails();
				}
			}
			if (!addB.equals("yes")) {
				PopulateContactDetails();
				PopulateConfigDetails();
				PopulateColour();
				PopulateFinanceType();
				PopulateFinanceBy();
				PopulateDate();
				PopulateExecutives();
				so_paymentdate = strToShortDate(ToLongDate(kknow()));
				so_promisedate = strToShortDate(ToLongDate(kknow()));
				sodate = strToShortDate(ToLongDate(kknow()));
				so_active = "1";
				output.put("so_paymentdate", so_paymentdate);
				output.put("so_promisedate", so_promisedate);
				output.put("sodate", sodate);
				output.put("so_active", so_active);
				output.put("so_form60", so_form60);
				output.put("so_open", so_open);
				output.put("so_exchange", so_exchange);
			}
			// if (add.equals("yes") && !addB.equals("yes")) {
			// if (!so_quote_id.equals("0")) {
			// GetConfigurationDetails(so_quote_id);
			// GetQuoteDetails();
			// }
			//
			// }
			if (add.equals("yes") && addB.equals("yes")) {
				PopulateContactDetails();
				PopulateConfigDetails();
				GetValues(input);
				Veh_Salesorder_Update soupdate = new Veh_Salesorder_Update();
				soupdate.branch_id = so_branch_id;
				soupdate.comp_id = comp_id;
				soupdate.sodate = sodate;
				soupdate.so_customer_id = so_customer_id;
				soupdate.so_contact_id = so_contact_id;
				soupdate.lead_id = lead_id;
				soupdate.enquiry_id = enquiry_id;
				soupdate.so_quote_id = so_quote_id;
				soupdate.so_exprice = so_exprice;
				soupdate.so_netamt = so_netamt;
				soupdate.so_discamt = so_discamt;
				soupdate.so_totaltax = so_totaltax;
				soupdate.so_grandtotal = so_grandtotal;
				soupdate.so_vehstock_id = so_vehstock_id;
				soupdate.so_option_id = so_option_id;
				soupdate.so_allot_no = so_allot_no;
				// soupdate.so_prefix = so_prefix;
				soupdate.so_desc = "";
				soupdate.so_terms = "";
				soupdate.so_mga_amount = so_mga_amount;
				soupdate.so_refund_amount = so_refund_amount;
				soupdate.so_booking_amount = so_booking_amount;
				soupdate.so_po = so_po;
				soupdate.so_fintype_id = so_fintype_id;
				soupdate.so_fincomp_id = so_fincomp_id;
				soupdate.so_finance_amt = so_finance_amt;
				soupdate.so_emp_id = so_emp_id;
				soupdate.so_refno = so_refno;
				soupdate.so_paymentdate = so_paymentdate;
				soupdate.so_promisedate = so_promisedate;
				soupdate.so_retaildate = so_retaildate;
				soupdate.so_delivereddate = so_delivereddate;
				soupdate.so_reg_no = so_reg_no;
				soupdate.so_regdate = so_regdate;
				soupdate.dr_day = dr_day;
				soupdate.dr_month = dr_month;
				soupdate.dr_year = dr_year;
				soupdate.so_dob = so_dob;
				soupdate.so_pan = so_pan;
				soupdate.so_form60 = so_form60;
				soupdate.so_open = so_open;
				soupdate.so_exchange = so_exchange;
				soupdate.so_canceldate = so_canceldate;
				soupdate.so_cancelreason_id = "0";
				soupdate.so_active = so_active;
				soupdate.so_notes = so_notes;
				soupdate.so_entry_id = emp_id;
				soupdate.so_entry_date = ToLongDate(kknow());
				soupdate.AddFields(null);
				msg = soupdate.msg;
				if (msg.equals("")) {
					output.put("msg", "Sales Order Added Successfully!");
					output.put("so_id", soupdate.so_id);
				} else {
					output.put("msg", msg);
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		SOP("output==" + output);
		return output;
	}

	// public void GetEnquiryDetails() {
	// try {
	// SOP("enquiry");
	// StrSql = "SELECT  contact_id, enquiry_branch_id,"
	// + " enquiry_emp_id, "
	// // + "lead_id, customer_id, customer_name,"
	// + " CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname,"
	// // + " contact_mobile1, contact_email1, state_name, city_name,"
	// // + " customer_address, customer_pin, rateclass_id, enquiry_custtype_id,"
	// + " CONCAT(branch_name, ' (', branch_code, ')') AS branchname"
	// + " from " + compdb(comp_id) + "axela_sales_enquiry"
	// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
	// // + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
	// // + " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
	// // + " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
	// // + " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = enquiry_lead_id"
	// + " WHERE enquiry_id = " + CNumeric(enquiry_id) + ""
	// + " GROUP BY enquiry_id";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// if (crs.isBeforeFirst()) {
	// while (crs.next()) {
	// contact_id = crs.getString("contact_id");
	// branch_id = crs.getString("enquiry_branch_id");
	// output.put("branchname", crs.getString("branchname"));
	// output.put("enquiry_emp_id", crs.getString("enquiry_emp_id"));
	// }
	// list.add(gson.toJson(map));
	// }
	// map.clear();
	// list.clear();
	// crs.close();
	// } catch (Exception e) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
	// }
	// }

	public void GetValues(JSONObject input) {

		try {

			if (!input.isNull("so_emp_id")) {
				so_emp_id = CNumeric(PadQuotes((String) input.get("so_emp_id")));
			}

			if (!input.isNull("so_entry_date")) {
				sodate = PadQuotes((String) input.getString("so_entry_date"));
			}

			if (!input.isNull("so_vehstock_id")) {
				so_vehstock_id = CNumeric(PadQuotes((String) input.getString("so_vehstock_id")));
			}

			if (!input.isNull("so_option_id")) {
				so_option_id = CNumeric(PadQuotes((String) input.getString("so_option_id")));
			}

			if (!input.isNull("so_allot_no")) {
				so_allot_no = CNumeric(PadQuotes((String) input.getString("so_allot_no")));
			}

			if (!input.isNull("so_fintype_id")) {
				so_fintype_id = CNumeric(PadQuotes((String) input.getString("so_fintype_id")));
			}

			if (!input.isNull("so_fincomp_id")) {
				so_fincomp_id = CNumeric(PadQuotes((String) input.getString("so_fincomp_id")));
			}

			if (!input.isNull("so_finance_amt")) {
				so_finance_amt = CNumeric(PadQuotes((String) input.getString("so_finance_amt")));
			}

			if (!input.isNull("so_booking_amount")) {
				so_booking_amount = CNumeric(PadQuotes((String) input.getString("so_booking_amount")));
			}

			if (!input.isNull("so_mga_amount")) {
				so_mga_amount = CNumeric(PadQuotes((String) input.getString("so_mga_amount")));
			}

			if (!input.isNull("so_refund_amount")) {
				so_refund_amount = CNumeric(PadQuotes((String) input.getString("so_refund_amount")));
			}

			if (!input.isNull("so_payment_date")) {
				so_paymentdate = PadQuotes((String) input.getString("so_payment_date"));
			}

			if (!input.isNull("so_promise_date")) {
				so_promisedate = PadQuotes((String) input.getString("so_promise_date"));
			}

			if (!input.isNull("so_retail_date")) {
				so_retaildate = PadQuotes((String) input.getString("so_retail_date"));
			}

			if (!input.isNull("so_delivered_date")) {
				so_delivereddate = PadQuotes((String) input.getString("so_delivered_date"));
			}

			if (!input.isNull("so_reg_no")) {
				so_reg_no = CNumeric(PadQuotes((String) input.getString("so_reg_no")));
			}

			if (!input.isNull("so_regdate")) {
				so_regdate = PadQuotes((String) input.getString("so_regdate"));
			}

			if (!input.isNull("so_dr_day")) {
				dr_day = PadQuotes((String) input.getString("so_dr_day"));
				if (dr_day.length() < 2) {
					dr_day = "0" + dr_day;
				}
			}

			if (!input.isNull("so_dr_month")) {
				dr_month = PadQuotes((String) input.getString("so_dr_month"));
				if (dr_month.length() < 2) {
					dr_month = "0" + dr_month;
				}
			}

			if (!input.isNull("so_dr_year")) {
				dr_year = PadQuotes((String) input.getString("so_dr_year"));
			}
			so_dob = dr_day + "/" + dr_month + "/" + dr_year;

			if (!input.isNull("so_pan")) {
				so_pan = PadQuotes((String) input.getString("so_pan"));
			}

			if (!input.isNull("so_form60")) {
				so_form60 = CNumeric(PadQuotes((String) input.getString("so_form60")));
			}

			if (!input.isNull("so_open")) {
				so_open = CNumeric(PadQuotes((String) input.getString("so_open")));
			}

			if (!input.isNull("so_exchange")) {
				so_exchange = CNumeric(PadQuotes((String) input.getString("so_exchange")));
			}

			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.getString("emp_id")));
			}

			if (!input.isNull("so_cancel_date")) {
				so_canceldate = PadQuotes((String) input.getString("so_cancel_date"));
			}

			if (!input.isNull("so_active")) {
				so_active = CNumeric(PadQuotes((String) input.getString("so_active")));
			}

			if (!input.isNull("so_notes")) {
				so_notes = PadQuotes((String) input.getString("so_notes"));
			}
			if (!input.isNull("so_contact_id")) {
				so_contact_id = PadQuotes((String) input.getString("so_contact_id"));
			}
			if (!input.isNull("so_customer_id")) {
				so_customer_id = PadQuotes((String) input.getString("so_customer_id"));
			}
			if (!input.isNull("so_branch_id")) {
				so_branch_id = PadQuotes((String) input.getString("so_branch_id"));
			}

		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected JSONObject PopulateContactDetails() {
		try {
			if (!so_contact_id.equals("0")) {
				StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname,"
						+ " contact_lname, title_desc"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
						+ " WHERE contact_id = " + so_contact_id + "";

				// SOP("contact===========" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						so_customer_id = crs.getString("customer_id");
						output.put("customer_name", crs.getString("customer_name"));
						output.put("contact_name", crs.getString("title_desc") + "" + crs.getString("contact_fname") + "" + crs.getString("contact_lname"));

					}
					list.add(gson.toJson(map));
				}
				map.clear();
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT COALESCE(branch_so_email_enable, '') AS branch_so_email_enable,"
					+ " COALESCE(branch_so_email_exe_sub, '') AS branch_so_email_exe_sub,"
					+ " COALESCE(branch_so_email_exe_format, '') AS branch_so_email_exe_format,"
					+ " COALESCE(branch_so_sms_exe_format, '') AS branch_so_sms_exe_format,"
					+ " COALESCE(branch_so_email_format, '') AS branch_so_email_format,"
					+ " COALESCE(branch_so_email_sub, '') AS branch_so_email_sub,"
					+ " COALESCE(branch_so_sms_enable, '') AS branch_so_sms_enable,"
					+ " COALESCE(branch_so_sms_format, '') AS branch_so_sms_format,"
					+ " COALESCE(branch_so_delivered_email_enable, '') AS branch_so_delivered_email_enable,"
					+ " COALESCE(branch_so_delivered_email_format, '') AS branch_so_delivered_email_format,"
					+ " COALESCE(branch_so_delivered_email_sub, '') AS branch_so_delivered_email_sub,"
					+ " COALESCE(branch_so_delivered_sms_enable, '') AS branch_so_delivered_sms_enable,"
					+ " COALESCE(branch_so_delivered_sms_format, '') AS branch_so_delivered_sms_format,"
					+ " COALESCE(IF(emp.emp_email1 != '', emp.emp_email1, emp.emp_email2), '') AS emp_email,"
					+ " COALESCE(emp.emp_name, '') AS emp_name, COALESCE(branch_email1, '') AS branch_email1,"
					+ " COALESCE(IF(emp.emp_mobile1 != '', emp.emp_mobile1, emp.emp_mobile2), '') AS emp_mobile,"
					+ " config_admin_email, config_email_enable, config_sms_enable, comp_sms_enable,"
					+ " config_sales_so_refno, config_customer_dupnames, comp_email_enable,"
					+ " COALESCE(emp.emp_so_priceupdate, '') AS emp_so_priceupdate,"
					+ " COALESCE(emp.emp_so_discountupdate, '') AS emp_so_discountupdate"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch on branch_id = " + branch_id + ""
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp on emp.emp_id = " + emp_id + ""
					+ " WHERE admin.emp_id = " + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				branch_so_email_enable = crs.getString("branch_so_email_enable");
				branch_so_email_format = crs.getString("branch_so_email_format");
				branch_so_sms_enable = crs.getString("branch_so_sms_enable");
				branch_so_sms_format = crs.getString("branch_so_sms_format");
				branch_so_email_sub = crs.getString("branch_so_email_sub");
				branch_so_delivered_email_enable = crs.getString("branch_so_delivered_email_enable");
				branch_so_delivered_email_format = crs.getString("branch_so_delivered_email_format");
				branch_so_delivered_sms_enable = crs.getString("branch_so_delivered_sms_enable");
				branch_so_delivered_sms_format = crs.getString("branch_so_delivered_sms_format");
				branch_so_delivered_email_sub = crs.getString("branch_so_delivered_email_sub");
				branch_so_email_exe_sub = crs.getString("branch_so_email_exe_sub");
				branch_so_email_exe_format = crs.getString("branch_so_email_exe_format");
				branch_so_sms_exe_format = crs.getString("branch_so_sms_exe_format");
				// emp_name = crs.getString("emp_name");
				// emp_email = crs.getString("emp_email");
				// emp_mobile = crs.getString("emp_mobile");
				// branch_email1 = crs.getString("branch_email1");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_customer_dupnames = crs.getString("config_customer_dupnames");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				config_sales_so_refno = crs.getString("config_sales_so_refno");
				emp_so_priceupdate = crs.getString("emp_so_priceupdate");
				emp_so_discountupdate = crs.getString("emp_so_discountupdate");
				output.put("config_sales_so_refno", config_sales_so_refno);
				output.put("emp_so_priceupdate", emp_so_priceupdate);
				output.put("emp_so_discountupdate", emp_so_discountupdate);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public JSONObject PopulateExecutives() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_sales = 1"
					+ " AND (emp_branch_id = " + so_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + so_branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			SOP("StrSql------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("emp_id", "0");
				map.put("emp_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("emp_id", crs.getString("emp_id"));
					map.put("emp_name", crs.getString("emp_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("emp_id", "0");
				map.put("emp_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateexecutive", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateColour() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT option_id, option_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE 1 = 1"
					+ " GROUP BY option_id"
					+ " ORDER BY option_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("option_id", "0");
				map.put("option_desc", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("option_id", crs.getString("option_id"));
					map.put("option_desc", crs.getString("option_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("option_id", "0");
				map.put("option_desc", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatecolour", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateFinanceType() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT fintype_id, fintype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_so_finance_type"
					+ " GROUP BY fintype_id"
					+ " ORDER BY fintype_id";
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				map.put("fintype_id", "0");
				map.put("fintype_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("fintype_id", crs.getString("fintype_id"));
					map.put("fintype_name", crs.getString("fintype_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("fintype_id", "0");
				map.put("fintype_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatfinancetype", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateFinanceBy() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT fincomp_id, fincomp_name"
					+ " FROM " + compdb(comp_id) + "axela_finance_comp"
					+ " WHERE fincomp_active = 1"
					+ " ORDER BY fincomp_name";
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				map.put("fincomp_id", "0");
				map.put("fincomp_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("fincomp_id", crs.getString("fincomp_id"));
					map.put("fincomp_name", crs.getString("fincomp_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("fincomp_id", "0");
				map.put("fincomp_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatfinanceby", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateDate() {
		String[] months = {"January",
				"February",
				"March",
				"April",
				"May",
				"June",
				"July",
				"August",
				"September",
				"October",
				"November",
				"December"};
		try {
			map.put("date_id", "-1");
			map.put("date_name", "Select Day");
			list.add(gson.toJson(map));
			for (int i = 1; i <= 31; i++) {
				map.put("date_id", i + "");
				map.put("date_name", i + "");
				list.add(gson.toJson(map));
			}
			output.put("populateday", list);
			map.clear();
			list.clear();
			map.put("month_id", "-1");
			map.put("month_name", "Select Month");
			list.add(gson.toJson(map));
			for (int i = 0; i < months.length; i++) {
				map.put("month_id", i + 1 + "");
				map.put("month_name", months[i]);
				list.add(gson.toJson(map));
			}
			output.put("populatemonth", list);
			map.clear();
			list.clear();
			map.put("year_id", "-1");
			map.put("year_name", "Select Year");
			list.add(gson.toJson(map));
			for (int i = Integer.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow())))); i >= 1920; i--) {
				map.put("year_id", i + "");
				map.put("year_name", i + "");
				list.add(gson.toJson(map));
			}
			output.put("populateyear", list);
			map.clear();
			list.clear();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject GetConfigurationDetails(String so_quote_id) {
		CachedRowSet crs = null;
		String group = "", group_name = "", aftertax = "", grand_total = "0", item_price;
		Double quote_exprice = 0.00, price_amount;
		int groupitemcount = 0;

		try {
			StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name,"
					+ " item_small_desc, quoteitem_option_group, quoteitem_option_group_tax,"
					+ " quoteitem_price, quoteitem_qty, quoteitem_disc, quoteitem_total,"
					+ " quoteitem_tax_rate, quote_exprice, quote_grandtotal"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote ON quote_id = quoteitem_quote_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = quoteitem_item_id"
					+ " AND quote_id = " + CNumeric(so_quote_id) + ""
					+ " GROUP BY item_id"
					+ " ORDER BY quoteitem_option_id, quoteitem_option_group_tax, quoteitem_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("quoteitem_option_group", crs.getString("quoteitem_option_group"));
					map.put("item_name", crs.getString("item_name"));
					map.put("item_small_desc", crs.getString("item_small_desc"));
					item_price = df.format((crs.getDouble("quoteitem_price") * crs.getDouble("quoteitem_tax_rate") / 100) + crs.getDouble("quoteitem_price"));
					// Price
					map.put("quoteitem_price", item_price);
					// Discount
					map.put("quoteitem_disc", crs.getString("quoteitem_disc"));
					// Amount
					map.put("quoteitem_total", crs.getString("quoteitem_total"));
					// Ex-Showroom Price
					map.put("quote_exprice", crs.getString("quote_exprice"));
					map.put("quote_grandtotal", crs.getString("quote_grandtotal"));
					map.put("quoteitem_option_group_tax", crs.getString("quoteitem_option_group_tax"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("listdata", list);
				list.clear();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public void GetQuoteDetails() {

		try {

			StrSql = "SELECT quote_emp_id, COALESCE(enquiry_id, 0) AS enquiry_id, COALESCE(so_id, 0) AS so_id,COALESCE(lead_id, 0) AS lead_id,"
					+ " customer_id, contact_id, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contactname, quote_vehstock_id,"
					+ " contact_mobile1, contact_email1, quote_grandtotal, quote_totaltax, quote_netamt, quote_discamt,"
					+ " state_name, city_name, customer_address, customer_pin, quote_exprice, quoteitem_item_id, customer_name,"
					+ " quote_branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branchname, rateclass_id"
					+ " FROM " + compdb(comp_id) + "axela_sales_quote"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_quote_item ON quoteitem_quote_id = quote_id"
					+ " AND quoteitem_rowcount != 0"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = quote_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = quote_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = quote_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_rate_class ON rateclass_id = branch_rateclass_id	"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = quote_enquiry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = enquiry_lead_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = quote_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = quote_enquiry_id"
					+ " WHERE quote_id = " + CNumeric(so_quote_id) + ""
					+ " GROUP BY quote_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					so_emp_id = crs.getString("quote_emp_id");
					so_customer_id = crs.getString("customer_id");
					SOP("customer_id========" + so_customer_id);
					so_branch_id = crs.getString("quote_branch_id");
					enquiry_id = crs.getString("enquiry_id");
					lead_id = crs.getString("lead_id");
					customer_name = crs.getString("customer_name");
					so_contact_id = crs.getString("contact_id");
					SOP("contact_id========" + so_contact_id);
					contact_name = crs.getString("contactname");
					output.put("so_emp_id", so_emp_id);
					output.put("branchname", crs.getString("branchname"));
					so_id = crs.getString("so_id");
					if (!so_id.equals("0")) {
						output.put("msg", "Sales Order already present for this Enquiry!");
					}

					output.put("so_contact_id", so_contact_id);
					output.put("so_customer_id", so_customer_id);
					output.put("so_branch_id", so_branch_id);
					output.put("contact_name", contact_name);
					output.put("customer_name", customer_name);
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_email1 = crs.getString("contact_email1");
					branch_id = CNumeric(crs.getString("quote_branch_id"));
					rateclass_id = CNumeric(crs.getString("rateclass_id"));
					branch_name = crs.getString("branchname");
					so_item_id = crs.getString("quoteitem_item_id");
					so_grandtotal = crs.getString("quote_grandtotal");
					so_totaltax = crs.getString("quote_totaltax");
					so_discamt = crs.getString("quote_discamt");
					so_netamt = crs.getString("quote_netamt");
					so_exprice = crs.getString("quote_exprice");
					so_vehstock_id = crs.getString("quote_vehstock_id");
				}
			} else {
				// response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Quote"));
			}

		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}
