package axela.ws.sales;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_AddData extends ConnectWS {

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
	public String enquiry_enquirytype_id = "0";// /ccccccccc
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
	public String enquiry_entry_id = "0";
	public String enquiry_entry_date = "";
	public String branch_id = "0";
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
	// public String contact_state_id = "0";
	public String state_id = "0";
	public String customer_name = "";
	public String customer_info = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String BranchAccess = "";
	public String team_id = "0";
	public String branch_enquiry_email_enable = "";
	public String branch_enquiry_email_sub = "";
	public String branch_enquiry_email_format = "";
	public String branch_enquiry_email_exe_sub = "";
	public String branch_enquiry_email_exe_format = "";
	public String branch_enquiry_sms_enable = "";
	public String branch_enquiry_sms_exe_format = "";
	public String branch_enquiry_sms_format = "";
	public String branch_email1 = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_sales_lead_for_enquiry = "";
	public String config_customers_dupnames = "";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	// public String config_sales_enquiry_refno = "";
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String emp_role_id = "";
	public String config_sales_sob = "";
	public String enquiry_model_id = "0";
	public String model_name = "", city_name = "";
	public String enquiry_item_id = "0";
	// Brochure variables
	public String attachment = "";
	public String branch_enquiry_brochure_email_enable = "";
	public String branch_enquiry_brochure_email_format = "";
	public String branch_enquiry_brochure_email_sub = "";
	// End of Brochure variables
	public String send_contact_email = "";
	public String crmfollowup_crm_emp_id = "";
	public String crmfollowupdays_daycount = "";
	public String crmfollowup_followup_time = "";
	// new
	public String add = "";
	public String role_id = "";
	// ws
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject Enquiry_Add(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			enquiry_date = strToShortDate(ToShortDate(kknow()));

			if (!emp_id.equals("0")) {
				String StrSql1 = "SELECT emp_branch_id, emp_role_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id;
				CachedRowSet crs1 = processQuery(StrSql1, 0);
				while (crs1.next()) {
					role_id = crs1.getString("emp_role_id");
					branch_id = crs1.getString("emp_branch_id");
				}
				crs1.close();

				if (emp_id.equals("1") || emp_id.equals("2")) {
					branch_id = "1";
				}

				if (!branch_id.equals("0")) {
					branch_name = ExecuteQuery("SELECT concat(branch_name, ' (', branch_code,')') as branch_name"
							+ " from " + compdb(comp_id) + "axela_branch"
							+ " where branch_id = " + branch_id);
				}
				if (!input.isNull("enquiry_branch_id")) {
					enquiry_branch_id = CNumeric(PadQuotes((String) input.get("enquiry_branch_id")));
				}
				if (!input.isNull("add")) {
					add = PadQuotes((String) input.get("add"));
				}
				if (!input.isNull("comp_id")) {
					comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
				}
				if (!input.isNull("emp_uuid")) {
					emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
				}
				if (!input.isNull("addB")) {
					addB = PadQuotes((String) input.get("addB"));
				}
				if (!input.isNull("lead_id")) {
					lead_id = CNumeric(PadQuotes((String) input.get("lead_id")));
				}
				if (!input.isNull("enquiry_id")) {
					enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
				}
				if (!input.isNull("enquiry_contact_id")) {
					enquiry_contact_id = CNumeric(PadQuotes((String) input.get("enquiry_contact_id")));
				}
				if (!input.isNull("enquiry_model_id")) {
					enquiry_model_id = CNumeric(PadQuotes((String) input.get("enquiry_model_id")));
				}
				if (!input.isNull("enquiry_emp_id")) {
					enquiry_emp_id = CNumeric(PadQuotes((String) input.get("enquiry_emp_id")));
				}
				if (!input.isNull("emp_enquiry_edit")) {
					emp_enquiry_edit = PadQuotes((String) input.get("emp_enquiry_edit"));
				}

				if (add.equals("yes") && !addB.equals("yes")) {
					enquiry_emp_id = emp_id;
					team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id FROM " + compdb(comp_id) + "axela_sales_team_exe WHERE teamtrans_emp_id = " + emp_id));
					if (!enquiry_branch_id.equals("0")) {
						StrSql = "select city_id,COALESCE(CONCAT(city_name, ' - ', state_name), '') as city_name"
								+ " from " + compdb(comp_id) + "axela_branch "
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = branch_city_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " where branch_id = " + branch_id;
						CachedRowSet crs = processQuery(StrSql, 0);
						while (crs.next()) {
							city_name = crs.getString("city_name");
							contact_city_id = crs.getString("city_id");
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
					PopulateBranch();

					// PopulateCustomerType();
					output.put("emp_branch_name", branch_name);
					output.put("enquiry_emp_id", enquiry_emp_id);
					output.put("team_id", team_id);
					output.put("city_name", city_name);
					output.put("city_id", contact_city_id);
				}
				PopulateConfigDetails();
				output.put("config_sales_soe", config_sales_soe);
				output.put("config_sales_sob", config_sales_sob);
				output.put("config_sales_campaign", config_sales_campaign);

				if ("yes".equals(addB)) {
					GetValues(input);
					enquiry_entry_date = ToLongDate(kknow());
					// axela.sales.Enquiry_Quickadd add = new axela.sales.Enquiry_Quickadd();
					// add.enquiry_branch_id = enquiry_branch_id;
					// add.enquiry_customer_id = enquiry_customer_id;
					// add.enquiry_contact_id = enquiry_contact_id;
					// add.enquiry_title = enquiry_title;
					// add.enquiry_desc = enquiry_desc;
					// add.enquiry_date = enquiry_date;
					// add.enquiry_enquirytype_id = enquiry_enquirytype_id;
					// add.enquiry_model_id = enquiry_model_id;
					// add.enquiry_item_id = enquiry_item_id;
					// add.close_date = close_date;
					// add.enquiry_preownedmodel_id = enquiry_preownedmodel_id;
					// add.enquiry_preownedvariant_id = enquiry_preownedvariant_id;
					// add.enquiry_fueltype_id = enquiry_fueltype_id;
					// add.enquiry_prefreg_id = enquiry_prefreg_id;
					// add.enquiry_presentcar = enquiry_presentcar;
					// add.enquiry_finance = enquiry_finance;
					// add.enquiry_budget = enquiry_budget;
					// add.enquiry_emp_id = enquiry_emp_id;
					// add.config_sales_soe =config_sales_soe;
					// add.config_sales_sob = config_sales_sob;
					// add.config_sales_campaign = config_sales_campaign;
					// add.enquiry_priorityenquiry_id = enquiry_priorityenquiry_id;
					// add.enquiry_notes = enquiry_notes;
					// add.config_sales_enquiry_refno = config_sales_enquiry_refno;
					// add.enquiry_dmsno = enquiry_dmsno;
					// add.enquiry_buyertype_id = enquiry_buyertype_id;
					// add.emp_id = emp_id;
					// add.customer_name = customer_name;
					// add.contact_mobile1 = contact_mobile1;
					// add.contact_mobile2 = contact_mobile2;
					// add.contact_city_id = contact_city_id;
					// add.enquiry_soe_id = enquiry_soe_id;
					// add.enquiry_sob_id = enquiry_sob_id;
					// add.contact_address = contact_address;
					// add.contact_pin = contact_pin;
					// add.contact_email1 = contact_email1;
					// add.contact_email2 = contact_email2;
					// add.contact_phone1 = contact_phone1;
					// add.contact_phone2 = contact_phone2;
					// add.contact_title_id = contact_title_id;
					// add.contact_fname = contact_fname;
					// add.contact_lname = contact_lname;
					// add.contact_jobtitle = contact_jobtitle;

					AddFields();
					// add.AddEnquiryFields();
					// msg = add.msg;
					// if(msg.equals("")){
					// output.put("msg", "Enquiry Added Sucessfully!");
					// output.put("enquiry_id", add.enquiry_id);
					// }else{
					// output.put("msg", msg);
					// }
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
			// StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
			// + " FROM " + compdb(comp_id) + "axela_emp"
			// + " WHERE 1 = 1 AND emp_sales = '1' AND emp_active = '1'"
			// + " AND (emp_branch_id = " + branch_id + " OR emp_id = 1"
			// + " OR emp_id IN (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr"
			// + " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id AND empbr.emp_branch_id = " + branch_id + "))"
			// + " GROUP BY emp_id"
			// + " ORDER BY emp_name";
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " from " + compdb(comp_id) + "axela_emp "
					+ " where 1=1 and emp_sales='1' and emp_active='1' and (emp_branch_id = "
					+ enquiry_branch_id
					+ " or emp_id = 1 "
					+ " or emp_id in (select empbr.emp_id from " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " where " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id="
					+ enquiry_branch_id + "))";
			if (!team_id.equals("0")) {
				StrSql = StrSql
						+ " and emp_id in (select teamtrans_emp_id from " + compdb(comp_id) + "axela_sales_team_exe "
						+ " where teamtrans_team_id=" + team_id + ")";
			}
			StrSql = StrSql + " group by emp_id " + " order by emp_name ";
			SOP("PopulateSalesExecutives SQL---" + StrSql);
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateCampaign() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT campaign_id, concat(campaign_name, ' (', DATE_FORMAT(campaign_startdate, '%d/%m/%Y'), ' - ', DATE_FORMAT(campaign_enddate, '%d/%m/%Y'), ')') as campaign_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id"
					+ " WHERE 1 = 1 AND camptrans_branch_id = " + branch_id + " AND campaign_active = '1'"
					+ " AND SUBSTR(campaign_startdate, 1, 8) <= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "', 1, 8)"
					+ " AND SUBSTR(campaign_enddate, 1, 8) >= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "', 1, 8)"
					+ " GROUP BY campaign_id"
					+ " ORDER BY campaign_name";
			// SOP("PopulateCampaign SQL------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("campaign_id", crs.getString("campaign_id"));
					map.put("campaign_name", crs.getString("campaign_name"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populatecampaign", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateTeam() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT team_id, team_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " WHERE 1 = 1"
					+ " AND team_branch_id = " + branch_id
					+ " GROUP BY team_id"
					+ " ORDER BY team_name";
			// SOP("PopulateTeam SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("team_id", "0");
				map.put("team_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("team_id", crs.getString("team_id"));
					map.put("team_name", unescapehtml(crs.getString("team_name")));
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateTitle() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			// SOP("PopulateTitle SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("title_id", crs.getString("title_id"));
					map.put("title_desc", crs.getString("title_desc"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populatetitle", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateModel() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " WHERE 1 = 1"
					+ " AND branch_id=" + enquiry_branch_id + ""
					+ " AND model_active = '1'"
					+ " AND model_sales = '1'"
					// + " AND item_type_id = 1"
					// + " AND item_active = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("PopulateModel SQL------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", crs.getString("model_name"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populatemodel", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateItem() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1 = 1 AND item_type_id = 1 AND item_active = '1'";
			// if (!enquiry_model_id.equals("0")) {
			// StrSql += " AND item_model_id = " + enquiry_model_id;
			// }
			StrSql += " ORDER BY item_name";
			// SOP("PopulateItem SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populateitem", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateType() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT enquirytype_id, enquirytype_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_type"
					+ " group by enquirytype_id"
					+ " order by enquirytype_name";
			StrSql += " ORDER BY enquirytype_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("enquirytype_id", crs.getString("enquirytype_id"));
					map.put("enquirytype_name", unescapehtml(crs.getString("enquirytype_name")));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populatetype", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateCustomerType() {
		try {
			SOP("2.1");
			StrSql = "Select custtype_id, custtype_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_custtype"
					+ " where 1=1"
					+ " order by custtype_name";
			// SOP("SqlStr=="+SqlStr);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("custtype_id", "0");
				map.put("custtype_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("custtype_id", crs.getString("custtype_id"));
					map.put("custtype_name", crs.getString("custtype_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("custtype_id", "0");
				map.put("custtype_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatecusttype", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP("Cauvery Ford == " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}
	public JSONObject PopulateSOB() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("PopulateSOB SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("sob_id", crs.getString("sob_id"));
					map.put("sob_name", crs.getString("sob_name"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populatesob", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateSOE() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			// SOP("PopulateSOE SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("soe_id", crs.getString("soe_id"));
					map.put("soe_name", crs.getString("soe_name"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populatesoe", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateBranch() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT branch_id, CONCAT(branch_name, ' (', branch_code, ')') AS branch_name"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active = '1' " + WSCheckBranchAccess(emp_id, branch_id, role_id) + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("PopulateBranch SQL==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("branch_id", crs.getString("branch_id"));
					map.put("branch_name", crs.getString("branch_name"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("populatebranch", list);
				list.clear();
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
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
		// if (!input.isNull("enquiry_sob_id")) {
		// enquiry_sob_id = (String) input.get("enquiry_sob_id");
		// }
		// if (!input.isNull("enquiry_campaign_id")) {
		// enquiry_campaign_id = (String) input.get("enquiry_campaign_id");
		// }
		// if (!input.isNull("enquiry_refno")) {
		// enquiry_refno = (String) input.get("enquiry_refno");
		// }
		if (!input.isNull("enquiry_notes")) {
			enquiry_notes = (String) input.get("enquiry_notes");
		}
		return output;
	}

	public void CheckForm() {
		msg = "";
		String customername = "";
		if (enquiry_contact_id.equals("0")) {
			if (enquiry_title_id.equals("0")) {
				msg = msg + "<br>Select Title!";
			}
			if (enquiry_fname.equals("")) {
				msg = msg + "<br>Enter the Contact Person Name!";
			} else {
				enquiry_fname = toTitleCase(enquiry_fname);
			}
			if (!enquiry_lname.equals("")) {
				enquiry_lname = toTitleCase(enquiry_lname);
			}
			if (customer_name.equals("")) {
				customername = (enquiry_fname + " " + enquiry_lname).trim();
				customer_name = toTitleCase(customername);
			} else {
				customername = toTitleCase(customer_name);
				customer_name = customername;
			}
			if (contact_mobile1.equals("") && contact_phone1.equals("")) {
				msg = msg + "<br>Enter Either Contact Mobile 1 or Phone 1!";
			} else if (!contact_mobile1.equals("")) {
				if (!IsValidMobileNo(contact_mobile1)) {
					msg = msg + "<br>Enter Valid Contact Mobile 1!";
				} else {
					StrSql = "select contact_id "
							+ " from " + compdb(comp_id) + "axela_customer_contact"
							+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_contact_id = contact_id"
							+ " where enquiry_status_id = 1 "
							+ " and (contact_mobile1 = '" + contact_mobile1 + "' or contact_mobile2 = '" + contact_mobile1 + "') "
							+ " and enquiry_branch_id = " + enquiry_branch_id;
					// SOP("StrSql--" + StrSql);
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Mobile 1 Found!";
					}
				}
			}
			if (!contact_mobile2.equals("")) {
				if (!IsValidMobileNo(contact_mobile2)) {
					msg = msg + "<br>Enter Valid Contact Mobile 2!";
				} else {
					StrSql = "select contact_id "
							+ " from " + compdb(comp_id) + "axela_customer_contact"
							+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_contact_id = contact_id"
							+ " where enquiry_status_id = 1 "
							+ " and (contact_mobile1 = '" + contact_mobile2 + "' or contact_mobile2 = '" + contact_mobile2 + "') "
							+ " and enquiry_branch_id = " + enquiry_branch_id;
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Mobile 2 Found!";
					}
				}
			}
			if (!contact_email1.equals("")) {
				if (!IsValidEmail(contact_email1)) {
					msg = msg + "<br>Enter Valid Contact Email 1!";
				} else {
					StrSql = "SELECT contact_id "
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
							+ " WHERE enquiry_status_id = 1 AND contact_email1 = '" + contact_email1 + "' AND enquiry_branch_id = " + enquiry_branch_id;
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Email 1 Found!";
					}
					contact_email1 = contact_email1.toLowerCase();
				}
			}
			if (!contact_email2.equals("")) {
				if (!IsValidEmail(contact_email2)) {
					msg = msg + "<br>Enter valid Contact Email 2!";
				} else {
					StrSql = "SELECT contact_id "
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
							+ " WHERE enquiry_status_id = 1 AND contact_email2 = '" + contact_email2 + "' AND enquiry_branch_id = " + enquiry_branch_id;
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Email 2 Found!!";
					}
					contact_email2 = contact_email2.toLowerCase();
				}
			}
			if (!contact_phone1.equals("")) {
				if (!IsValidPhoneNo(contact_phone1)) {
					msg = msg + "<br>Enter Valid Contact Phone!";
				} else {
					StrSql = "SELECT contact_id "
							+ " FROM " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
							+ " WHERE enquiry_status_id = 1"
							+ " AND (contact_phone1 = '" + contact_phone1 + "' OR contact_phone2 = '" + contact_phone1 + "')"
							+ " AND enquiry_branch_id = " + enquiry_branch_id;
					// SOP("StrSql--"+StrSql);
					if (!ExecuteQuery(StrSql).equals("")) {
						msg = msg + "<br>Similar Phone Found!";
					}
				}
			}
			contact_address = "";
			contact_city_id = branch_city_id;
			contact_pin = "";
			// if (contact_address.equals("")) {
			// msg = msg + "<br>Enter Contact Address!";
			// }
			// if (contact_city_id.equals("0")) {
			// msg = msg + "<br>Select Contact City!";
			// }
			// if (contact_pin.equals("")) {
			// msg = msg + "<br>Enter Contact Pin!";
			// } else if (!isNumeric(contact_pin)) {
			// msg = msg + "<br>Contact Pin: Enter Numeric!";
			// }
		}
		if (emp_enquiry_edit.equals("1")) {
			if (enquiry_date.equals("")) {
				msg = msg + "<br>Enter Date!";
			} else {
				if (!isValidDateFormatShort(enquiry_date)) {
					msg = msg + "<br>Enter Valid Date!";
				}
				if (Long.parseLong(ToLongDate(kknow())) < Long.parseLong(ConvertShortDateToStr(enquiry_date))) {
					msg = msg + "<br>Date can't be greater than Current Date!";
				}
			}
		}
		// if (enquiry_close_date.equals("")) {
		// msg = msg + "<br>Enter Closed Date!";
		// } else {
		// if (isValidDateFormatShort(enquiry_close_date)) {
		// close_date = ConvertShortDateToStr(enquiry_close_date);
		// if (Long.parseLong(ConvertShortDateToStr(enquiry_close_date)) < Long.parseLong(ToShortDate(kknow()))) {
		// msg = msg + " <br>Close Date cannot be less than Current Date!";
		// }
		// } else {
		// msg = msg + "<br>Enter valid Closed Date!";
		// }
		// }
		if (!enquiry_model_id.equals("0")) {
			model_name = ExecuteQuery("SELECT model_name FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_id = " + enquiry_model_id);
			enquiry_title = "New " + model_name;
		}
		if (enquiry_model_id.equals("0")) {
			msg = msg + "<br>Select Model!";
		}
		if (enquiry_item_id.equals("0")) {
			msg = msg + "<br>Select Variant!";
		}
		if (enquiry_emp_id.equals("0")) {
			msg = msg + "<br>Select Executive!";
		}
		if (config_sales_soe.equals("1")) {
			if (enquiry_soe_id.equals("0")) {
				msg = msg + "<br>Select Source of Enquiry!";
			}
		}
		if (config_sales_sob.equals("1")) {
			if (enquiry_sob_id.equals("0")) {
				msg = msg + "<br>Select Source of Bussiness!";
			}
		}
		if (config_sales_campaign.equals("1")) {
			if (enquiry_campaign_id.equals("0")) {
				msg = msg + "<br>Select Campaign!";
			}
		}
		// if (config_sales_enquiry_refno.equals("1")) {
		// if (enquiry_refno.equals("")) {
		// msg = msg + "<br>Enter Enquiry Reference No.!";
		// } else {
		// if (enquiry_refno.length() < 2) {
		// msg = msg + "<br>Enquiry Reference No. Should be Atleast Two Digits!";
		// }
		// if (!enquiry_branch_id.equals("0")) {
		// StrSql = "select enquiry_refno"
		// + " from " + compdb(comp_id) + "axela_sales_enquiry"
		// + " where enquiry_branch_id = " + enquiry_branch_id + ""
		// + " and enquiry_refno = '" + enquiry_refno + "'";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>Similar Enquiry Reference No. found!";
		// }
		// }
		// }
		// }
		if (!enquiry_branch_id.equals("0")) {
			if (!enquiry_emp_id.equals("0")) {
				StrSql = "SELECT coalesce((SELECT team_crm_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
						+ " WHERE team_branch_id = " + enquiry_branch_id + ""
						+ " AND teamtrans_emp_id=" + enquiry_emp_id + " AND  team_crm_emp_id != 0 limit 1), 0)";
				crmfollowup_crm_emp_id = ExecuteQuery(StrSql);
				// SOP("crmfollowup_crm_emp_id--"+crmfollowup_crm_emp_id);
			}
		}
	}

	public void AddFields() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				if (enquiry_customer_id.equals("0") || (!enquiry_customer_id.equals("0") && config_customers_dupnames.equals("1"))) {
					AddCustomerFields();
				}
				if (enquiry_contact_id.equals("0")) {
					AddContactFields();
				}
				StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry"
						+ " (enquiry_no,"
						+ " enquiry_branch_id,"
						+ " enquiry_lead_id,"
						+ " enquiry_customer_id,"
						+ " enquiry_contact_id,"
						// + " enquiry_enquirytype_id," ////ccccccc
						+ " enquiry_title,"
						+ " enquiry_desc,"
						+ " enquiry_date,"
						+ " enquiry_model_id,"
						+ " enquiry_item_id,"
						+ " enquiry_close_date,"
						+ " enquiry_value_syscal,"
						+ " enquiry_avpresent,"
						+ " enquiry_manager_assist,"
						+ " enquiry_emp_id,"
						+ " enquiry_stage_id,";
				if (config_sales_soe.equals("1")) {
					StrSql += " enquiry_soe_id,";
				}
				if (config_sales_sob.equals("1")) {
					StrSql += " enquiry_sob_id,";
				}
				if (config_sales_campaign.equals("1")) {
					StrSql += " enquiry_campaign_id,";
				}
				StrSql += " enquiry_status_id,"
						+ " enquiry_status_date,"
						+ " enquiry_status_desc,"
						+ " enquiry_priorityenquiry_id,"
						+ " enquiry_notes,";

				// if (config_sales_enquiry_refno.equals("1")) {
				// StrSql += "enquiry_refno,";
				// }
				StrSql += " enquiry_entry_id,"
						+ " enquiry_entry_date,"
						+ " enquiry_modified_id,"
						+ " enquiry_modified_date)"
						+ " VALUES"
						+ " ((SELECT COALESCE(MAX(enquiry.enquiry_no), 0) + 1"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry as enquiry"
						+ " WHERE enquiry.enquiry_branch_id = " + enquiry_branch_id + "),"
						+ " " + enquiry_branch_id + ","
						+ " " + lead_id + ","
						+ " " + enquiry_customer_id + ","
						+ " " + enquiry_contact_id + ","
						// + " " + enquiry_enquirytype_id + ","
						+ " '" + enquiry_title + "',"
						+ " '" + enquiry_desc + "',"
						+ " '" + ConvertShortDateToStr(enquiry_date) + "',"
						+ " " + enquiry_model_id + ","
						+ " " + enquiry_item_id + ","
						+ " '" + ToShortDate(kknow()) + "',"
						+ " '1',";
				StrSql += " '0',"
						+ " '0',"
						+ " " + enquiry_emp_id + ","
						+ " " + 2 + ",";
				if (config_sales_soe.equals("1")) {
					StrSql += " " + enquiry_soe_id + ",";
				}
				if (config_sales_sob.equals("1")) {
					StrSql += " " + enquiry_sob_id + ",";
				}
				if (config_sales_campaign.equals("1")) {
					StrSql += " " + enquiry_campaign_id + ",";
				}
				StrSql += " 1,"
						+ " '',"
						+ " '',"
						+ " 1,"
						// + " 1,"
						+ " '" + enquiry_notes + "',";
				// if (config_sales_enquiry_refno.equals("1")) {
				// StrSql += "'" + enquiry_refno + "',";
				// }
				StrSql += " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "',"
						+ " 0,"
						+ " '')";
				// SOP(StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					enquiry_id = rs.getString(1);
				}
				rs.close();

				if (!enquiry_id.equals("0")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime ,"
							+ "history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " (" + enquiry_id + ","
							+ " " + emp_id + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " 'NEW_ENQUIRY', '',"
							+ " 'New Enquiry added')";
					stmttx.execute(StrSql);
				}

				if (!enquiry_id.equals("0")) {

					if (!contact_email2.equals("") && !contact_email1.equals("")) {
						send_contact_email = contact_email1 + "," + contact_email2;
					} else if (!contact_email1.equals("")) {
						send_contact_email = contact_email1;
					}
					if (comp_email_enable.equals("1")
							&& config_email_enable.equals("1")
							&& !branch_email1.equals("")
							&& branch_enquiry_email_enable.equals("1")) {
						if (!contact_email1.equals("")
								&& !branch_enquiry_email_format.equals("")
								&& !branch_enquiry_email_sub.equals("")) {
							SendEmail();
						}
						if (!emp_email_formail.equals("") && !branch_enquiry_email_exe_format.equals("")
								&& !branch_enquiry_email_exe_sub.equals("")) {
							SendEmailToExecutive();
						}
					}

					if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && branch_enquiry_sms_enable.equals("1")) {
						if (!branch_enquiry_sms_format.equals("")) {
							if (!contact_mobile1.equals("")) {
								SendSMS(contact_mobile1);
							}
							if (!contact_mobile2.equals("")) {
								SendSMS(contact_mobile2);
							}
						}
						if (!branch_enquiry_sms_exe_format.equals("")) {
							if (!emp_mobile1.equals("")) {
								SendSMSToExecutive(emp_mobile1);
							}
							if (!emp_mobile2.equals("")) {
								SendSMSToExecutive(emp_mobile2);
							}
						}
					}

					if (!enquiry_emp_id.equals("0")) {
						AddFollowupFields();
					}
					if (comp_email_enable.equals("1")
							&& config_email_enable.equals("1")
							&& !branch_email1.equals("")
							&& branch_enquiry_brochure_email_enable.equals("1")
							&& !contact_email1.equals("")
							&& !branch_enquiry_brochure_email_format.equals("")
							&& !branch_enquiry_brochure_email_sub.equals("")) {
						BrochureAttachment();
						if (!attachment.equals("")) {
							SendBrochureEmail();
						}
					}
				}
				// ********
				conntx.commit();
				msg = "Enquiry Added Successfully!";
				output.put("enquiry_id", enquiry_id);
				output.put("msg", msg);
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connOppr is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("Axelaauto ==" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
				}
			} finally {
				conntx.setAutoCommit(true);
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		} else {
			output.put("msg", "Error!" + msg);
		}
	}

	public void AddFollowupFields() throws SQLException {
		try {
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " (followup_enquiry_id,"
					+ " followup_emp_id,"
					+ " followup_followup_time,"
					+ " followup_followuptype_id,"
					+ " followup_desc,"
					+ " followup_entry_id,"
					+ " followup_entry_time,"
					+ " followup_trigger)"
					+ " VALUES"
					+ " ('" + enquiry_id + "',"
					+ " " + enquiry_emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 1,"
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0)";
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddCustomerFields() throws SQLException, JSONException {
		try {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer"
					+ " (customer_branch_id,"
					+ " customer_name,"
					+ " customer_mobile1,"
					+ " customer_mobile2,"
					+ " customer_city_id,"
					+ " customer_soe_id,"
					+ " customer_sob_id,"
					+ " customer_emp_id,"
					+ " customer_since,"
					+ " customer_address,"
					+ " customer_pin,"
					+ " customer_email1,"
					+ " customer_email2,"
					+ " customer_active,"
					+ " customer_notes,"
					+ " customer_entry_id,"
					+ " customer_entry_date)"
					+ " VALUES"
					+ " (" + branch_id + ","
					+ " '" + customer_name + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + contact_mobile2 + "',"
					+ " " + branch_city_id + ","
					+ " " + enquiry_soe_id + ","
					+ " " + enquiry_sob_id + ","
					+ " " + enquiry_emp_id + ","
					+ " '" + ToShortDate(kknow()) + "',"
					+ " '" + contact_address + "',"
					+ " '" + contact_pin + "',"
					+ " '" + contact_email1 + "',"
					+ " '" + contact_email2 + "',"
					+ " '1',"
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			stmttx.executeUpdate(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				enquiry_customer_id = rs.getString(1);
			}
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddContactFields() throws SQLException, JSONException {
		try {
			StrSql = "INSERT into " + compdb(comp_id) + "axela_customer_contact"
					+ " (contact_customer_id,"
					+ " contact_title_id,"
					+ " contact_fname,"
					+ " contact_lname,"
					+ " contact_jobtitle,"
					+ " contact_mobile1,"
					+ " contact_mobile2,"
					+ " contact_email1,"
					+ " contact_email2,"
					+ " contact_phone1,"
					+ " contact_address,"
					+ " contact_city_id,"
					+ " contact_pin,"
					+ " contact_active,"
					+ " contact_notes,"
					+ " contact_entry_id,"
					+ " contact_entry_date)"
					+ " VALUES"
					+ " (" + enquiry_customer_id + ","
					+ " " + enquiry_title_id + ","
					+ " '" + enquiry_fname + "',"
					+ " '" + enquiry_lname + "',"
					+ " '" + enquiry_jobtitle + "',"
					+ " '" + contact_mobile1 + "',"
					+ " '" + contact_mobile2 + "',"
					+ " '" + contact_email1 + "',"
					+ " '" + contact_email2 + "',"
					+ " '" + contact_phone1 + "',"
					+ " '" + contact_address + "',"
					+ " '" + branch_city_id + "',"
					+ " '" + contact_pin + "',"
					+ " '1',"
					+ " '',"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "')";
			stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmttx.getGeneratedKeys();
			while (rs.next()) {
				enquiry_contact_id = rs.getString(1);
			}
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_sales_soe, config_sales_sob,"
				+ " config_email_enable, config_sms_enable, config_sales_lead_for_enquiry,"
				+ " comp_email_enable, comp_sms_enable, config_sales_campaign,"
				+ " COALESCE(branch_email1, '') as branch_email1,"
				+ " COALESCE(branch_enquiry_email_enable, '') AS branch_enquiry_email_enable,"
				+ " COALESCE(branch_enquiry_email_format, '') AS branch_enquiry_email_format,"
				+ " COALESCE(branch_enquiry_email_sub, '') AS branch_enquiry_email_sub,"
				+ " COALESCE(branch_enquiry_email_exe_sub, '') AS branch_enquiry_email_exe_sub,"
				+ " COALESCE(branch_enquiry_email_exe_format, '') AS branch_enquiry_email_exe_format,"
				+ " COALESCE(branch_enquiry_sms_enable, '') AS branch_enquiry_sms_enable,"
				+ " COALESCE(branch_enquiry_sms_format, '') AS branch_enquiry_sms_format,"
				+ " COALESCE(branch_enquiry_sms_exe_format, '') AS branch_enquiry_sms_exe_format,"
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
				+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp, " + compdb(comp_id) + "axela_emp admin"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_lead ON lead_id = " + lead_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + enquiry_branch_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + enquiry_emp_id + ""
				+ " WHERE 1 = 1 AND admin.emp_id = " + emp_id;
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branch_email1 = crs.getString("branch_email1");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				// config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
				config_sales_lead_for_enquiry = crs.getString("config_sales_lead_for_enquiry");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				config_customers_dupnames = crs.getString("config_customer_dupnames");
				branch_enquiry_email_enable = crs.getString("branch_enquiry_email_enable");
				branch_enquiry_email_format = crs.getString("branch_enquiry_email_format");
				branch_enquiry_email_sub = crs.getString("branch_enquiry_email_sub");
				branch_enquiry_email_exe_sub = crs.getString("branch_enquiry_email_exe_sub");
				branch_enquiry_email_exe_format = crs.getString("branch_enquiry_email_exe_format");
				branch_enquiry_sms_enable = crs.getString("branch_enquiry_sms_enable");
				branch_enquiry_sms_format = crs.getString("branch_enquiry_sms_format");
				branch_enquiry_sms_exe_format = crs.getString("branch_enquiry_sms_exe_format");
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendEmail() throws SQLException {
		String emailmsg, sub;
		emailmsg = (branch_enquiry_email_format);
		sub = (branch_enquiry_email_sub);

		sub = "replace('" + sub + "','[ENQUIRYID]',enquiry_id)";
		sub = "replace(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		emailmsg = "replace('" + emailmsg + "','[ENQUIRYID]',enquiry_id)";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]',emp_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " '" + enquiry_contact_id + "',"
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + branch_email1 + "',"
					+ " '" + send_contact_email + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " WHERE enquiry_id = " + enquiry_id;
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " (email_contact_id,"
					+ " email_contact,"
					+ " email_FROM,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMS(String contact_mobile) throws SQLException {
		String smsmsg = (branch_enquiry_sms_format);

		smsmsg = "replace('" + smsmsg + "','[ENQUIRYID]',enquiry_id)";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " " + enquiry_contact_id + ","
					+ " concat(title_desc, ' ', contact_fname, ' ', contact_lname),"
					+ " '" + contact_mobile + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " WHERE enquiry_id = " + enquiry_id;
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			// SOP("INSERT-sms-" + StrSql);
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendEmailToExecutive() throws SQLException {
		String emailmsg, sub;
		emailmsg = (branch_enquiry_email_exe_format);
		sub = (branch_enquiry_email_exe_sub);

		sub = "replace('" + sub + "','[ENQUIRYID]',enquiry_id)";
		sub = "replace(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		emailmsg = "replace('" + emailmsg + "','[ENQUIRYID]',enquiry_id)";
		emailmsg = "replace(" + emailmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERID]',customer_id)";
		emailmsg = "replace(" + emailmsg + ",'[CUSTOMERNAME]',customer_name)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		emailmsg = "replace(" + emailmsg + ",'[EXENAME]',emp_name)";
		emailmsg = "replace(" + emailmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		emailmsg = "replace(" + emailmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEPHONE1]',emp_phone1)";
		emailmsg = "replace(" + emailmsg + ",'[EXEEMAIL1]',emp_email1)";
		emailmsg = "replace(" + emailmsg + ",'[MODELNAME]',model_name)";
		emailmsg = "replace(" + emailmsg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " " + enquiry_emp_id + ","
					+ " '" + emp_name + "',"
					+ " '" + branch_email1 + "',"
					+ " '" + emp_email_formail + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(emailmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " WHERE enquiry_id = " + enquiry_id;
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " (email_emp_id,"
					+ " email_emp,"
					+ " email_FROM,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_sent,"
					+ " email_entry_id)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void SendSMSToExecutive(String emp_mobile) throws SQLException {
		String smsmsg = (branch_enquiry_sms_exe_format);

		smsmsg = "replace('" + smsmsg + "','[ENQUIRYID]',enquiry_id)";
		smsmsg = "replace(" + smsmsg + ",'[ENQUIRYNAME]',enquiry_title)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERID]',customer_id)";
		smsmsg = "replace(" + smsmsg + ",'[CUSTOMERNAME]',customer_name)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTPHONE1]',contact_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[CONTACTEMAIL1]',contact_email1)";
		smsmsg = "replace(" + smsmsg + ",'[EXENAME]',emp_name)";
		smsmsg = "replace(" + smsmsg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		smsmsg = "replace(" + smsmsg + ",'[EXEMOBILE1]',emp_mobile1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEPHONE1]',emp_phone1)";
		smsmsg = "replace(" + smsmsg + ",'[EXEEMAIL1]',emp_email1)";
		smsmsg = "replace(" + smsmsg + ",'[MODELNAME]',model_name)";
		smsmsg = "replace(" + smsmsg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " " + enquiry_emp_id + ","
					+ " '" + emp_name + "',"
					+ " '" + emp_mobile + "',"
					+ " " + unescapehtml(smsmsg) + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
					+ " WHERE enquiry_id = " + enquiry_id;
			StrSql = "INSERT into " + compdb(comp_id) + "axela_sms"
					+ " (sms_emp_id,"
					+ " sms_emp,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connemsgction rollback...");
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	// For getting the attachments of brochure
	protected void BrochureAttachment() throws ServletException, IOException, SQLException {
		StrSql = "SELECT brochure_value, brochure_title  "
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_brochure  "
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id	= " + enquiry_branch_id
				+ " WHERE brochure_rateclass_id = branch_rateclass_id"
				+ " AND AND brochure_brand_id = branch_brand_id  "
				+ " AND (brochure_item_id=0 OR brochure_item_id= " + enquiry_item_id + ")";
		CachedRowSet crs = processQuery(StrSql, 0);
		if (crs.isBeforeFirst()) {
			attachment = "";
			while (crs.next()) {
				attachment = attachment + EnquiryBrochurePath(comp_id) + crs.getString("brochure_value") + ","
						+ crs.getString("brochure_title") + fileext(crs.getString("brochure_value"));
				attachment = attachment + ";";
			}
			attachment = attachment.substring(0, attachment.lastIndexOf(";"));
		}
		crs.close();
	}

	protected void SendBrochureEmail() throws SQLException {
		String msg = "", sub = "";
		msg = branch_enquiry_brochure_email_format;
		sub = branch_enquiry_brochure_email_sub;

		sub = "replace('" + sub + "','[ENQUIRYID]', enquiry_id)";
		sub = "replace(" + sub + ",'[ENQUIRYNAME]',enquiry_title)";
		sub = "replace(" + sub + ",'[CUSTOMERID]',customer_id)";
		sub = "replace(" + sub + ",'[CUSTOMERNAME]',customer_name)";
		sub = "replace(" + sub + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		sub = "replace(" + sub + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		sub = "replace(" + sub + ",'[CONTACTMOBILE1]',contact_mobile1)";
		sub = "replace(" + sub + ",'[CONTACTPHONE1]',contact_phone1)";
		sub = "replace(" + sub + ",'[CONTACTEMAIL1]',contact_email1)";
		sub = "replace(" + sub + ",'[EXENAME]',emp_name)";
		sub = "replace(" + sub + ",'[EXEJOBTITLE]',jobtitle_desc)";
		sub = "replace(" + sub + ",'[EXEMOBILE1]',emp_mobile1)";
		sub = "replace(" + sub + ",'[EXEPHONE1]',emp_phone1)";
		sub = "replace(" + sub + ",'[EXEEMAIL1]',emp_email1)";
		sub = "replace(" + sub + ",'[MODELNAME]',model_name)";
		sub = "replace(" + sub + ",'[ITEMNAME]',item_name)";

		msg = "replace('" + msg + "','[ENQUIRYID]',enquiry_id)";
		msg = "replace(" + msg + ",'[ENQUIRYNAME]',enquiry_title)";
		msg = "replace(" + msg + ",'[CUSTOMERID]',customer_id)";
		msg = "replace(" + msg + ",'[CUSTOMERNAME]',customer_name)";
		msg = "replace(" + msg + ",'[CONTACTNAME]',concat(title_desc, ' ', contact_fname,' ', contact_lname))";
		msg = "replace(" + msg + ",'[CONTACTJOBTITLE]',contact_jobtitle)";
		msg = "replace(" + msg + ",'[CONTACTMOBILE1]',contact_mobile1)";
		msg = "replace(" + msg + ",'[CONTACTPHONE1]',contact_phone1)";
		msg = "replace(" + msg + ",'[CONTACTEMAIL1]',contact_email1)";
		msg = "replace(" + msg + ",'[EXENAME]',emp_name)";
		msg = "replace(" + msg + ",'[EXEJOBTITLE]',jobtitle_desc)";
		msg = "replace(" + msg + ",'[EXEMOBILE1]',emp_mobile1)";
		msg = "replace(" + msg + ",'[EXEPHONE1]',emp_phone1)";
		msg = "replace(" + msg + ",'[EXEEMAIL1]',emp_email1)";
		msg = "replace(" + msg + ",'[MODELNAME]',model_name)";
		msg = "replace(" + msg + ",'[ITEMNAME]',item_name)";

		try {
			StrSql = "SELECT"
					+ " '" + enquiry_contact_id + "',"
					+ " concat(title_desc, ' ', contact_fname,' ', contact_lname),"
					+ " '" + branch_email1 + "',"
					+ " '" + send_contact_email + "',"
					+ " " + unescapehtml(sub) + ","
					+ " " + unescapehtml(msg) + ","
					+ " '" + attachment.replace("\\", "/") + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0 "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry   "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON contact_title_id = title_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON emp_jobtitle_id = jobtitle_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id  "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id  "
					+ " WHERE enquiry_id = " + enquiry_id + "";
			StrSql = "INSERT into " + compdb(comp_id) + "axela_email"
					+ " (email_contact_id,"
					+ " email_contact,"
					+ " email_FROM,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_attach1,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				msg = "<br>Transaction Error!";
				SOPError("conn is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				msg = "<br>Transaction Error!";
				SOPError("connection rollback...");
			}
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	/* Eof code for Sending Brochure as attachment */
}
