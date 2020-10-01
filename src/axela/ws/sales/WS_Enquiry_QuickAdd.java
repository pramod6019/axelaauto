package axela.ws.sales;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Enquiry_Quickadd;
import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_QuickAdd extends ConnectWS {

	public String addB = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_enquiry_edit = "";
	public String lead_id = "0";
	public String emp_uuid = "0";
	public String enquiry_title_id = "0";
	public String enquiry_fname = "";
	public String enquiry_lname = "";
	public String enquiry_jobtitle = "";
	public String enquiry_id = "0";
	public String enquiry_branch_id = "0";
	public String enquiry_customer_id = "0";
	public String enquiry_contact_id = "0";
	public String enquiry_title = "";
	public String enquiry_desc = "";
	public String enquiry_date = "";
	// public String enquiry_close_date = "";
	// public String close_date = "";
	public String enquiry_emp_id = "0";
	public String enquiry_campaign_id = "0";
	public String enquiry_soe_id = "0";
	public String enquiry_sob_id = "0";
	// public String enquiry_refno = "";
	public String enquiry_notes = "";
	// public String enquiry_custtype_id = "0";
	public String enquiry_buyertype_id = "0";
	public String enquiry_entry_id = "0";
	public String enquiry_entry_date = "";
	public String branch_id = "0";
	public String branch_brand_id = "0";
	public String branch_name = "";
	public String branch_city_id = "0";
	public String branch_pin = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_name = "";
	public String emp_email_formail = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String title_desc = "";
	public String contact_name = "";
	public String contact_info = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_mobile2 = "";
	public String contact_email2 = "";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String contact_city_id = "";
	public String contact_pin = "";
	public String state_id = "0";
	public String customer_name = "";
	public String customer_info = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String team_id = "0";
	public String brandconfig_enquiry_email_enable = "";
	public String brandconfig_enquiry_email_sub = "";
	public String brandconfig_enquiry_email_format = "";
	public String brandconfig_enquiry_email_exe_sub = "";
	public String brandconfig_enquiry_email_exe_format = "";
	public String brandconfig_enquiry_sms_enable = "";
	public String brandconfig_enquiry_sms_exe_format = "";
	public String brandconfig_enquiry_sms_format = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_sales_lead_for_enquiry = "";
	public String config_customers_dupnames = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String config_sales_enquiry_refno = "";
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String emp_role_id = "";
	public String config_sales_sob = "";
	public String enquiry_model_id = "0";
	public String model_name = "", city_name = "";
	public String enquiry_item_id = "0";
	public String attachment = "";
	public String branch_enquiry_brochure_email_enable = "";
	public String branch_enquiry_brochure_email_format = "";
	public String branch_enquiry_brochure_email_sub = "";
	public String send_contact_email = "";
	public String crmfollowup_crm_emp_id = "";
	public String crmfollowupdays_daycount = "";
	public String crmfollowup_followup_time = "";
	public String add = "";
	// ws
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject Enquiry_QuickAdd(JSONObject input) throws Exception {

		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			enquiry_date = strToShortDate(ToShortDate(kknow()));

			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("enquiry_branch_id")) {
				enquiry_branch_id = CNumeric(PadQuotes((String) input
						.get("enquiry_branch_id")));
				SOP("empbranchid======" + enquiry_branch_id);
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!emp_id.equals("0")) {
				String StrSql1 = "SELECT emp_branch_id" + " FROM "
						+ compdb(comp_id) + "axela_emp" + " WHERE emp_id = "
						+ emp_id;
				CachedRowSet crs1 = processQuery(StrSql1, 0);
				while (crs1.next()) {
					branch_id = crs1.getString("emp_branch_id");
				}
				crs1.close();

				if (emp_id.equals("1")) {
					branch_id = "1";
				}

				if (!enquiry_branch_id.equals("0")) {
					branch_name = ExecuteQuery("SELECT CONCAT(branch_name, ' (', branch_code,')') AS branch_name"
							+ " FROM " + compdb(comp_id) + "axela_branch"
							+ " WHERE branch_id = " + enquiry_branch_id);
				}
				output.put("emp_branch_name", branch_name);

				if (!input.isNull("enquiry_branch_id")) {
					enquiry_branch_id = CNumeric(PadQuotes((String) input
							.get("enquiry_branch_id")));
				}
				if (!input.isNull("add")) {
					add = PadQuotes((String) input.get("add"));
				}
				if (!input.isNull("addB")) {
					addB = PadQuotes((String) input.get("addB"));
				}
				if (!input.isNull("lead_id")) {
					lead_id = CNumeric(PadQuotes((String) input.get("lead_id")));
				}
				if (!input.isNull("enquiry_id")) {
					enquiry_id = CNumeric(PadQuotes((String) input
							.get("enquiry_id")));
				}
				if (!input.isNull("enquiry_contact_id")) {
					enquiry_contact_id = CNumeric(PadQuotes((String) input
							.get("enquiry_contact_id")));
				}
				if (!input.isNull("enquiry_model_id")) {
					enquiry_model_id = CNumeric(PadQuotes((String) input
							.get("enquiry_model_id")));
				}
				if (!input.isNull("enquiry_emp_id")) {
					enquiry_emp_id = CNumeric(PadQuotes((String) input
							.get("enquiry_emp_id")));
				}
				if (!input.isNull("emp_enquiry_edit")) {
					emp_enquiry_edit = PadQuotes((String) input
							.get("emp_enquiry_edit"));
				}

				if (add.equals("yes") && !addB.equals("yes")) {
					// enquiry_emp_id = emp_id;
					team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id FROM "
							+ compdb(comp_id)
							+ "axela_sales_team_exe WHERE teamtrans_emp_id = "
							+ emp_id));
					if (!enquiry_branch_id.equals("0")) {
						StrSql = "SELECT city_id,COALESCE(CONCAT(city_name, ' - ', state_name), '') as city_name, branch_brand_id"
								+ " FROM " + compdb(comp_id) + "axela_branch "
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " WHERE branch_id = " + enquiry_branch_id;
						CachedRowSet crs = processQuery(StrSql, 0);
						while (crs.next()) {
							city_name = crs.getString("city_name");
							contact_city_id = crs.getString("city_id");
							branch_brand_id = crs.getString("branch_brand_id");
						}
						crs.close();
					}
				}

				if (add.equals("yes") && !addB.equals("yes")) {
					PopulateSalesExecutives();
					PopulateCampaign();
					PopulateTitle();
					PopulateTeam();
					PopulateSOB();
					PopulateSOE();
					PopulateItem();
					PopulateModel();
					PopulateBuyerType();
					// output.put("emp_branch_name", branch_name);
					output.put("enquiry_emp_id", emp_id);
					output.put("team_id", team_id);
					output.put("city_name", city_name);
					output.put("city_id", contact_city_id);
					output.put("branch_brand_id", branch_brand_id);
				}
				PopulateConfigDetails();
				output.put("config_sales_soe", config_sales_soe);
				output.put("config_sales_sob", config_sales_sob);
				output.put("config_sales_campaign", config_sales_campaign);

				if ("yes".equals(addB)) {
					GetValues(input);
					// enquiry_entry_date = ToLongDate(kknow());
					// start - now
					Enquiry_Quickadd enq = new Enquiry_Quickadd();
					// SOP("emp_id========" + emp_id);
					// SOP("enquiry_id========" + enquiry_emp_id);

					enq.emp_id = emp_id;
					enq.comp_id = comp_id;
					// -----customer n contact-------
					enq.enquiry_branch_id = enquiry_branch_id;
					enq.customer_name = enquiry_fname + " " + enquiry_lname;
					enq.contact_mobile1 = contact_mobile1;
					enq.contact_mobile2 = "";
					enq.contact_city_id = branch_city_id;
					enq.enquiry_soe_id = enquiry_soe_id;
					enq.enquiry_sob_id = enquiry_sob_id;
					enq.enquiry_emp_id = enquiry_emp_id;
					enq.contact_address = "";
					enq.contact_pin = "";
					enq.contact_email1 = contact_email1;
					enq.contact_email2 = "";
					enq.contact_phone1 = contact_phone1;
					enq.contact_id = "0";
					enq.contact_title_id = enquiry_title_id;
					enq.contact_fname = enquiry_fname;
					enq.contact_lname = enquiry_lname;
					enq.contact_jobtitle = "";

					// -----config-------
					enq.config_sales_soe = config_sales_soe;
					enq.config_sales_sob = config_sales_sob;
					enq.config_sales_campaign = config_sales_campaign;
					enq.config_sales_enquiry_refno = config_sales_enquiry_refno;
					enq.comp_email_enable = comp_email_enable;
					enq.config_email_enable = config_email_enable;
					enq.branch_email1 = branch_email1;
					enq.brandconfig_enquiry_email_enable = brandconfig_enquiry_email_enable;
					enq.brandconfig_enquiry_email_format = brandconfig_enquiry_email_format;
					enq.brandconfig_enquiry_email_sub = brandconfig_enquiry_email_sub;
					enq.emp_email_formail = emp_email_formail;
					enq.brandconfig_enquiry_email_exe_format = brandconfig_enquiry_email_exe_format;
					enq.brandconfig_enquiry_email_exe_sub = brandconfig_enquiry_email_exe_sub;
					enq.comp_sms_enable = comp_sms_enable;
					enq.config_sms_enable = config_sms_enable;
					enq.brandconfig_enquiry_sms_enable = brandconfig_enquiry_sms_enable;
					enq.brandconfig_enquiry_sms_format = brandconfig_enquiry_sms_format;
					enq.brandconfig_enquiry_sms_exe_format = brandconfig_enquiry_sms_exe_format;
					enq.branch_enquiry_brochure_email_enable = branch_enquiry_brochure_email_enable;
					enq.branch_enquiry_brochure_email_format = branch_enquiry_brochure_email_format;
					enq.branch_enquiry_brochure_email_sub = branch_enquiry_brochure_email_sub;

					// -----enquiry-------
					enq.enquiry_title = enquiry_title;
					enq.enquiry_desc = enquiry_desc;
					enq.enquiry_date = enquiry_date;
					enq.enquiry_model_id = enquiry_model_id;
					enq.enquiry_item_id = enquiry_item_id;
					enq.enquiry_close_date = strToShortDate(ToLongDate(kknow()));
					enq.enquiry_campaign_id = enquiry_campaign_id;
					enq.enquiry_notes = enquiry_notes;
					enq.enquiry_buyertype_id = enquiry_buyertype_id;
					enq.enquiry_qcsno = "";
					enq.enquiry_finance = "0";
					enq.enquiry_budget = "0";
					enq.enquiry_entry_id = emp_id;

					// enq.enquiry_entry_date =

					// -----followup-------
					// enq.crmfollowup_crm_emp_id = "";

					enq.AddEnquiryFields();

					if (!enq.msg.equals("")) {
						output.put("msg", "Error!" + enq.msg);
					} else {
						msg = "Enquiry Added Successfully!";
						output.put("msg", msg);
						output.put("enquiry_id", enq.enquiry_id);
					}
					// end - now
				}

				if (AppRun().equals("0")) {
					SOP("output = " + output);
				}
			}
		}
		return output;
	}

	public JSONObject PopulateSalesExecutives() {
		CachedRowSet crs = null;
		try {
			// StrSql =
			// "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
			// + " FROM " + compdb(comp_id) + "axela_emp"
			// + " WHERE 1 = 1 AND emp_sales = '1' AND emp_active = '1'"
			// + " AND (emp_branch_id = " + enquiry_branch_id + " OR emp_id = 1"
			// + " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) +
			// "axela_emp_branch empbr"
			// + " WHERE " + compdb(comp_id) +
			// "axela_emp.emp_id = empbr.emp_id AND empbr.emp_branch_id = " +
			// branch_id + "))"
			// + " GROUP BY emp_id"
			// + " ORDER BY emp_name";
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " from "
					+ compdb(comp_id)
					+ "axela_emp "
					+ " where 1=1 "
					+ "and emp_sales='1' and emp_active='1' and (emp_branch_id = "
					+ enquiry_branch_id
					+ " or emp_id = 1 "
					+ " or emp_id in (select empbr.emp_id from "
					+ compdb(comp_id)
					+ "axela_emp_branch empbr "
					+ " where "
					+ compdb(comp_id)
					+ "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id="
					+ enquiry_branch_id + "))";
			if (!team_id.equals("0")) {
				StrSql = StrSql
						+ " and emp_id in (select teamtrans_emp_id from "
						+ compdb(comp_id) + "axela_sales_team_exe "
						+ " where teamtrans_team_id=" + team_id + ")";
			}
			StrSql = StrSql + " group by emp_id " + " order by emp_name ";
			// SOP("PopulateSalesExecutives SQL---" + StrSql);
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
			output.put("populatesalesexecutive", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateCampaign() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT campaign_id, concat(campaign_name, ' (', DATE_FORMAT(campaign_startdate, '%d/%m/%Y'), ' - ', DATE_FORMAT(campaign_enddate, '%d/%m/%Y'), ')') as campaign_name"
					+ " FROM "
					+ compdb(comp_id)
					+ "axela_sales_campaign"
					+ " INNER JOIN "
					+ compdb(comp_id)
					+ "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id"
					+ " WHERE 1 = 1 AND camptrans_branch_id = "
					+ enquiry_branch_id
					+ " AND campaign_active = '1'"
					+ " AND SUBSTR(campaign_startdate, 1, 8) <= SUBSTR('"
					+ ConvertShortDateToStr(enquiry_date)
					+ "', 1, 8)"
					+ " AND SUBSTR(campaign_enddate, 1, 8) >= SUBSTR('"
					+ ConvertShortDateToStr(enquiry_date)
					+ "', 1, 8)"
					+ " GROUP BY campaign_id" + " ORDER BY campaign_name";
			// SOP("PopulateCampaign SQL------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("campaign_id", "0");
				map.put("campaign_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("campaign_id", crs.getString("campaign_id"));
					map.put("campaign_name", crs.getString("campaign_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("campaign_id", "0");
				map.put("campaign_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatecampaign", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBuyerType() {
		try {

			StrSql = "Select buyertype_id, buyertype_name" + " from "
					+ compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
					+ " where 1=1" + " order by buyertype_name";
			// SOP("SqlStr=="+SqlStr);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("buyertype_id", "0");
				map.put("buyertype_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("buyertype_id", crs.getString("buyertype_id"));
					map.put("buyertype_name", crs.getString("buyertype_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("buyertype_id", "0");
				map.put("buyertype_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatebuyertype", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP("Cauvery Ford == " + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return output;
	}

	public JSONObject PopulateTeam() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT team_id, team_name" + " FROM " + compdb(comp_id)
					+ "axela_sales_team" + " WHERE 1 = 1"
					+ " AND team_branch_id = " + branch_id
					+ " GROUP BY team_id" + " ORDER BY team_name";
			// SOP("PopulateTeam SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("team_id", "0");
				map.put("team_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("team_id", crs.getString("team_id"));
					map.put("team_name",
							unescapehtml(crs.getString("team_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("team_id", "0");
				map.put("team_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateteam", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateTitle() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT title_id, title_desc" + " FROM " + compdb(comp_id)
					+ "axela_title" + " ORDER BY title_desc";
			// SOP("PopulateTitle SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("title_id", "0");
				map.put("title_desc", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("title_id", crs.getString("title_id"));
					map.put("title_desc", crs.getString("title_desc"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("title_id", "0");
				map.put("title_desc", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatetitle", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateModel() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					// + " INNER JOIN axela_brand ON brand_id = model_brand_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1 = 1"
					+ " AND model_brand_id = " + branch_brand_id
					+ " AND model_active = '1'"
					+ " AND model_sales = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("PopulateModel SQL------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", crs.getString("model_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatemodel", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateItem() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT item_id, item_name" + " FROM " + compdb(comp_id)
					+ "axela_inventory_item"
					+ " WHERE 1 = 1 AND item_type_id = 1 AND item_active = '1'";
			// if (!enquiry_model_id.equals("0")) {
			// StrSql += " AND item_model_id = " + enquiry_model_id;
			// }
			StrSql += " ORDER BY item_name";
			// SOP("PopulateItem SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name",
							unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("item_id", "0");
				map.put("item_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateitem", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateSOB() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT sob_id, sob_name" + " FROM " + compdb(comp_id)
					+ "axela_sob" + " GROUP BY sob_id" + " ORDER BY sob_name";
			// SOP("PopulateSOB SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("sob_id", "0");
				map.put("sob_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("sob_id", crs.getString("sob_id"));
					map.put("sob_name", crs.getString("sob_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("sob_id", "0");
				map.put("sob_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatesob", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateSOE() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT soe_id, soe_name" + " from " + compdb(comp_id)
					+ "axela_soe" + " INNER JOIN " + compdb(comp_id)
					+ "axela_emp_soe ON empsoe_soe_id = soe_id" + " where 1=1"
					+ " AND empsoe_emp_id=" + emp_id + "" + " group by soe_id"
					+ " order by soe_name";
			// SOP("PopulateSOE SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("soe_id", "0");
				map.put("soe_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("soe_id", crs.getString("soe_id"));
					map.put("soe_name", crs.getString("soe_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("soe_id", "0");
				map.put("soe_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatesoe", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject GetValues(JSONObject input) throws Exception {
		JSONObject output = new JSONObject();
		if (enquiry_contact_id.equals("0")) {
			// if (!input.isNull("customer_name")) {
			// customer_name = (String) input.get("customer_name");
			// }
			if (!input.isNull("enquiry_title_id")) {
				enquiry_title_id = (String) input.get("enquiry_title_id");
			}
			if (!input.isNull("enquiry_fname")) {
				enquiry_fname = (String) input.get("enquiry_fname");
			}
			if (!input.isNull("enquiry_lname")) {
				enquiry_lname = (String) input.get("enquiry_lname");
			}
			// if (!input.isNull("enquiry_jobtitle")) {
			// enquiry_jobtitle = (String) input.get("enquiry_jobtitle");
			// }
			if (!input.isNull("contact_mobile1")) {
				contact_mobile1 = (String) input.get("contact_mobile1");
			}
			// if (!input.isNull("contact_mobile2")) {
			// contact_mobile2 = (String) input.get("contact_mobile2");
			// }
			if (!input.isNull("contact_email1")) {
				contact_email1 = (String) input.get("contact_email1");
			}
			// if (!input.isNull("contact_email2")) {
			// contact_email2 = (String) input.get("contact_email2");
			// }
			if (!input.isNull("contact_phone1")) {
				contact_phone1 = (String) input.get("contact_phone1");
			}
			// if (!input.isNull("contact_address")) {
			// contact_address = (String) input.get("contact_address");
			// }
			// if (!input.isNull("state_id")) {
			// state_id = (String) input.get("state_id");
			// }
			// if (!input.isNull("contact_city_id")) {
			// contact_city_id = (String) input.get("contact_city_id");
			// }
			// if (!input.isNull("contact_state_id")) {
			// contact_state_id = (String) input.get("contact_state_id");
			// }
			// if (!input.isNull("contact_pin")) {
			// contact_pin = (String) input.get("contact_pin");
			// }
		}
		if (emp_enquiry_edit.equals("1")) {
			if (!input.isNull("enquiry_date")) {
				enquiry_date = (String) input.get("enquiry_date");
			}
		} else {
			enquiry_date = strToShortDate(ToShortDate(kknow()));
		}
		// if (!input.isNull("enquiry_close_date")) {
		// enquiry_close_date = (String) input.get("enquiry_close_date");
		// }
		// close_date = enquiry_close_date;

		if (!input.isNull("enquiry_desc")) {
			enquiry_desc = (String) input.get("enquiry_desc");
		}
		if (!input.isNull("enquiry_item_id")) {
			enquiry_item_id = (String) input.get("enquiry_item_id");
		}
		if (!input.isNull("team_id")) {
			team_id = (String) input.get("team_id");
		}
		if (!input.isNull("enquiry_soe_id")) {
			enquiry_soe_id = (String) input.get("enquiry_soe_id");
		}
		if (!input.isNull("enquiry_sob_id")) {
			enquiry_sob_id = (String) input.get("enquiry_sob_id");
		}
		if (!input.isNull("enquiry_campaign_id")) {
			enquiry_campaign_id = (String) input.get("enquiry_campaign_id");
		}
		// if (!input.isNull("enquiry_refno")) {
		// enquiry_refno = (String) input.get("enquiry_refno");
		// }
		if (!input.isNull("enquiry_notes")) {
			enquiry_notes = (String) input.get("enquiry_notes");
		}
		if (!input.isNull("enquiry_buyertype_id")) {
			enquiry_buyertype_id = (String) input.get("enquiry_buyertype_id");
		}
		return output;
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob,"
				+ " config_email_enable, config_sms_enable, config_sales_lead_for_enquiry,"
				+ " comp_email_enable, comp_sms_enable, config_sales_campaign, config_sales_enquiry_refno,"
				+ " COALESCE(branch_email1, '') as branch_email1,"
				+ " coalesce(branch_brand_id,0) as branch_brand_id,"
				+ " COALESCE(brandconfig_enquiry_email_enable, '') AS brandconfig_enquiry_email_enable,"
				+ " COALESCE(brandconfig_enquiry_email_format, '') AS brandconfig_enquiry_email_format,"
				+ " COALESCE(brandconfig_enquiry_email_sub, '') AS brandconfig_enquiry_email_sub,"
				+ " COALESCE(brandconfig_enquiry_email_exe_sub, '') AS brandconfig_enquiry_email_exe_sub,"
				+ " COALESCE(brandconfig_enquiry_email_exe_format, '') AS brandconfig_enquiry_email_exe_format,"
				+ " COALESCE(brandconfig_enquiry_sms_enable, '') AS brandconfig_enquiry_sms_enable,"
				+ " COALESCE(brandconfig_enquiry_sms_format, '') AS brandconfig_enquiry_sms_format,"
				+ " COALESCE(brandconfig_enquiry_sms_exe_format, '') AS brandconfig_enquiry_sms_exe_format,"
				+ " COALESCE(branch_enquiry_brochure_email_enable, '') AS branch_enquiry_brochure_email_enable,"
				+ " COALESCE(branch_enquiry_brochure_email_format, '') AS branch_enquiry_brochure_email_format,"
				+ " COALESCE(branch_enquiry_brochure_email_sub, '') AS branch_enquiry_brochure_email_sub,"
				//
				+ " branch_city_id, branch_pin,"
				//
				+ " COALESCE(emp.emp_email1, '') AS emp_email1, COALESCE(emp.emp_email2, '') AS emp_email2,"
				+ " COALESCE(emp.emp_name, '') AS emp_name, COALESCE(emp.emp_mobile1, '') as emp_mobile1,"
				+ " COALESCE(emp.emp_mobile2, '') as emp_mobile2,"
				+ " config_customer_dupnames,"
				+ " COALESCE(lead_empcount_id, 0) as lead_empcount_id, COALESCE(branch_id, 0) AS lead_branch_id,"
				+ " COALESCE(lead_soe_id,0) as lead_soe_id, COALESCE(lead_sob_id,0) as lead_sob_id, COALESCE(emp.emp_id,0) as lead_emp_id, "
				+ " COALESCE(lead_refno,'') as lead_refno, COALESCE(lead_company,'') as lead_company, coalesce(lead_title_id,0) as lead_title_id, "
				+ " COALESCE(lead_fname,'') as lead_fname, COALESCE(lead_lname,'') as lead_lname, coalesce(lead_jobtitle,'') as lead_jobtitle, "
				+ " COALESCE(lead_mobile,'') as lead_mobile, COALESCE(lead_website,'') as lead_website, "
				+ " COALESCE(lead_phone,'') as lead_phone, COALESCE(lead_email,'') as lead_email, COALESCE(lead_req,'') as lead_req"
				+ " FROM " + compdb(comp_id) + "axela_config, "
				+ compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = " + lead_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + enquiry_branch_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + enquiry_emp_id + ""
				+ " WHERE 1 = 1 AND admin.emp_id = " + emp_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_brand_id = crs.getString("branch_brand_id");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
				config_sales_lead_for_enquiry = crs.getString("config_sales_lead_for_enquiry");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				config_customers_dupnames = crs.getString("config_customer_dupnames");
				brandconfig_enquiry_email_enable = crs.getString("brandconfig_enquiry_email_enable");
				brandconfig_enquiry_email_format = crs.getString("brandconfig_enquiry_email_format");
				brandconfig_enquiry_email_sub = crs.getString("brandconfig_enquiry_email_sub");
				brandconfig_enquiry_email_exe_sub = crs.getString("brandconfig_enquiry_email_exe_sub");
				brandconfig_enquiry_email_exe_format = crs.getString("brandconfig_enquiry_email_exe_format");
				brandconfig_enquiry_sms_enable = crs.getString("brandconfig_enquiry_sms_enable");
				brandconfig_enquiry_sms_format = crs.getString("brandconfig_enquiry_sms_format");
				brandconfig_enquiry_sms_exe_format = crs.getString("brandconfig_enquiry_sms_exe_format");
				branch_enquiry_brochure_email_enable = crs.getString("branch_enquiry_brochure_email_enable");
				branch_enquiry_brochure_email_format = crs.getString("branch_enquiry_brochure_email_format");
				branch_enquiry_brochure_email_sub = crs.getString("branch_enquiry_brochure_email_sub");
				branch_city_id = crs.getString("branch_city_id");
				branch_pin = crs.getString("branch_pin");
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email2 = crs.getString("emp_email2");
				if (!emp_email2.equals("")) {
					emp_email_formail = emp_email1 + "," + emp_email2;
				} else {
					emp_email_formail = emp_email1;
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}

}
