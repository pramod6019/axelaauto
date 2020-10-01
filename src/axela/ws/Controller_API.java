package axela.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Enquiry_Quickadd;
import cloudify.connect.Connect;

import com.google.gson.Gson;

@Path("/api1.0")
public class Controller_API extends Connect {

	public String comp_id = "0";
	public String emp_id = "0";
	public String enquiry_emp_id = "6";
	public String enquiry_team_id = "0";
	public String msg = "";
	public Response response = null;
	private String contact_email1 = "";
	private String contact_mobile1 = "";
	private String enquiry_model_id = "0";
	private String enquiry_item_id = "0";
	public String comp_email_enable = "";
	public String comp_sms_enable = "";
	public String branchtype_id = "0";
	public String branch_brand_id = "";
	public String branch_email1 = "";
	public String branch_pin = "";
	public String config_email_enable = "";
	public String config_sms_enable = "";
	public String config_sales_enquiry_refno = "";
	public String config_sales_campaign = "";
	public String config_sales_soe = "";
	public String config_sales_sob = "";
	public String config_customer_dupnames = "";
	public String brandconfig_enquiry_email_enable = "";
	public String brandconfig_enquiry_email_sub = "";
	public String brandconfig_enquiry_email_format = "";
	public String brandconfig_enquiry_email_exe_enable = "";
	public String brandconfig_enquiry_email_exe_format = "";
	public String brandconfig_enquiry_email_exe_sub = "";
	public String brandconfig_enquiry_sms_enable = "";
	public String brandconfig_enquiry_sms_format = "";
	public String brandconfig_enquiry_sms_exe_format = "";
	public String brandconfig_enquiry_sms_exe_enable = "";
	public String brandconfig_enquiry_brochure_email_enable = "";
	public String brandconfig_enquiry_brochure_email_format = "";
	public String brandconfig_enquiry_brochure_email_sub = "";
	public String team_preownedbranch_id = "0";
	public String team_preownedemp_id = "0";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_email_formail = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String enquiry_model_name = "";
	public String enquiry_branch_id = "1";
	public String customer_name = "";
	public String contact_city_id = "";
	public String enquiry_id = "";
	public String enquiry_branch_name = "";
	public String enquiry_soe_name = "";
	public String enquiry_soe_id = "0";
	public String enquiry_sob_id = "0";
	public String StrSql = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	JSONObject config = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	@Path("/test")
	@GET
	@Produces("text/html")
	public String test() throws Exception {
		return "CarWale WS Test Successful.";
	}

	@Path("/enquiry-add")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response EnquiryAdd(JSONObject input, @Context HttpServletRequest request) throws Exception {
		try {
			if (AppRun().equals("0")) {
				SOP("input==WS Enquiry Add===" + input.toString(1));
			}
			comp_id = CNumeric(PadQuotes((String) request.getAttribute("comp_id")));
			emp_id = CNumeric(PadQuotes((String) request.getAttribute("emp_id")));
			if (!comp_id.equals("0") && !emp_id.equals("0")) {
				if (!enquiry_emp_id.equals("0")) {
					PopulateConfigDetails();
					GetValues(input);
					if (msg.equals("")) {
						AddEnquiry();
					}
				} else {
					msg += "<br> Invalid Sales Consultant";
				}
				if (!msg.equals("")) {
					output.put("status_message", msg.replaceFirst("<br>", "").replace("<br>", " and ").replace("!", ""));
					response = Response.status(400).entity(output).build();
				} else {
					output.put("status_message", "Enquiry Added Successfully!");
					output.put("enquiry_id", enquiry_id);
					response = Response.status(200).entity(output).build();
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto API===API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return response;
	}

	public void GetValues(JSONObject input) throws ServletException, IOException {
		try {
			if (!input.isNull("customerName")) {
				customer_name = PadQuotes(PadQuotes((String) input.get("customerName")));
			} else {
				msg += " <br> Enter Customer Name";
			}
			if (!input.isNull("phoneNumber")) {
				contact_mobile1 = PadQuotes(PadQuotes((String) input.get("phoneNumber")));
				if (!isNumericNeg(contact_mobile1)) {
					msg += " <br> Invalid Phone Number";
				}
				if (!contact_mobile1.contains("91-")) {
					contact_mobile1 = "91-" + contact_mobile1;
				}
			} else {
				msg += " <br> Enter Phone Number";
			}
			if (!input.isNull("email")) {
				contact_email1 = PadQuotes(PadQuotes((String) input.get("email")));
			} else {
				msg += " <br> Enter Email";
			}
			if (!input.isNull("cityName")) {
				String contact_city_name = PadQuotes((String) input.get("cityName"));
				contact_city_id = CNumeric(ExecuteQuery("SELECT city_id"
						+ " FROM " + compdb(comp_id) + "axela_city"
						+ " WHERE city_name='" + contact_city_name + "'"));
				if (contact_city_id.equals("0")) {
					msg += " <br> Invalid City Name";
				}
			} else {
				msg += " <br> Enter City Name";
			}
			if (!input.isNull("model")) {
				enquiry_model_name = PadQuotes((String) input.get("model"));
				enquiry_model_id = CNumeric(ExecuteQuery("SELECT model_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
						+ " WHERE model_name='" + enquiry_model_name + "'"
						+ "  AND model_active = 1"));
				if (enquiry_model_id.equals("0")) {
					msg += " <br> Invalid Model";
				}
				// else {
				// String model_id = CNumeric(ExecuteQuery("SELECT model_id"
				// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
				// + " WHERE model_id='" + enquiry_model_id + "'"
				// + " AND model_brand_id='" + branch_brand_id + "'"
				// + " AND model_active = 1"));
				// SOP("SELECT model_id"
				// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
				// + " WHERE model_id='" + enquiry_model_id + "'"
				// + " AND model_brand_id='" + branch_brand_id + "'"
				// + " AND model_active = 1");
				// if (model_id.equals("0")) {
				// msg += " <br>" + enquiry_model_name + " is not Associated with " + enquiry_branch_name + "";
				// }
				//
				// }
			} else {
				msg += " <br> Enter Model Name";
			}
			if (!input.isNull("version")) {
				String enquiry_item_name = PadQuotes((String) input.get("version"));
				enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " WHERE item_name='" + enquiry_item_name + "'"
						+ " AND item_type_id = 1"
						+ " AND item_active = '1'"));
				if (enquiry_item_id.equals("0")) {
					enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_model_id=" + enquiry_model_id + ""
							+ " AND item_active = '1'"
							+ " ORDER BY item_name"
							+ " LIMIT 1"));
				}
			} else if (enquiry_item_id.equals("0") && !enquiry_model_id.equals("0")) {
				enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item"
						+ " WHERE item_model_id=" + enquiry_model_id + ""
						+ " AND item_active = '1'"
						+ " AND item_type_id = 1"
						+ " ORDER BY item_name"
						+ " LIMIT 1"));
			}
			if (config_sales_soe.equals("1")) {
				if (!input.isNull("inquirySourceName")) {
					enquiry_soe_name = PadQuotes((String) input.get("inquirySourceName"));
					enquiry_soe_id = CNumeric(ExecuteQuery("SELECT soe_id"
							+ " FROM " + compdb(comp_id) + "axela_soe"
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
							+ " WHERE 1 = 1"
							+ " AND soe_active = 1"
							+ " AND empsoe_emp_id = " + emp_id + ""
							+ " AND soe_name = '" + enquiry_soe_name + "'"
							+ " GROUP BY soe_id"
							+ " ORDER BY soe_name"));
					if (enquiry_soe_id.equals("0")) {
						msg += " <br> Invalid Source of Enquiry";
					} else {
						enquiry_sob_id = CNumeric(ExecuteQuery("SELECT sob_id"
								+ " FROM " + compdb(comp_id) + "axela_sob"
								+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
								+ " WHERE 1 = 1"
								+ " AND soetrans_soe_id = " + enquiry_soe_id + ""
								+ " GROUP BY sob_id"
								+ " ORDER BY sob_name"
								+ " LIMIT 1"));
						if (enquiry_sob_id.equals("0")) {
							msg += " <br> No Source of Business found for " + enquiry_soe_name;
						}
					}
				} else {
					msg += " <br> Enter Source of Enquiry";
				}
			}
			if (!enquiry_emp_id.equals("0") && !enquiry_branch_id.equals("0")) {
				enquiry_team_id = CNumeric(ExecuteQuery("SELECT teamtrans_team_id "
						+ " FROM " + compdb(comp_id) + "axela_sales_team_exe "
						+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
						+ " WHERE teamtrans_emp_id =" + enquiry_emp_id + ""
						+ " AND team_active = 1"));
			}
			if (enquiry_team_id.equals("0")) {
				msg += " <br> Sales Consultant is not associated with any Team!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto API===API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT "
				+ " config_sales_soe,"
				+ " config_sales_sob,"
				+ " config_sales_enquiry_refno,"
				+ " config_email_enable,"
				+ " config_sms_enable,"
				+ " comp_email_enable,"
				+ " comp_sms_enable,"
				+ " config_sales_campaign,"
				+ " branch_brand_id,"
				+ " COALESCE (branch_branchtype_id, 0) AS branch_branchtype_id,"
				+ " COALESCE (branch_email1, '') AS branch_email1,"
				+ " branch_pin,"
				+ " COALESCE (brandconfig_enquiry_email_enable,'') AS brandconfig_enquiry_email_enable,"
				+ " COALESCE (brandconfig_enquiry_email_format,'') AS brandconfig_enquiry_email_format,"
				+ " COALESCE (brandconfig_enquiry_email_sub,'') AS brandconfig_enquiry_email_sub,"
				+ " COALESCE (brandconfig_enquiry_email_exe_enable,'') AS brandconfig_enquiry_email_exe_enable,"
				+ " COALESCE (brandconfig_enquiry_email_exe_sub,'') AS brandconfig_enquiry_email_exe_sub,"
				+ " COALESCE (brandconfig_enquiry_email_exe_format,'') AS brandconfig_enquiry_email_exe_format,"
				+ " COALESCE (brandconfig_enquiry_sms_enable,'') AS brandconfig_enquiry_sms_enable,"
				+ " COALESCE (brandconfig_enquiry_sms_format,'') AS brandconfig_enquiry_sms_format,"
				+ " COALESCE (brandconfig_enquiry_sms_exe_enable,'') AS brandconfig_enquiry_sms_exe_enable,"
				+ " COALESCE (brandconfig_enquiry_sms_exe_format,'') AS brandconfig_enquiry_sms_exe_format,"
				+ " COALESCE (brandconfig_enquiry_brochure_email_enable,'') AS brandconfig_enquiry_brochure_email_enable,"
				+ " COALESCE (brandconfig_enquiry_brochure_email_format,'') AS brandconfig_enquiry_brochure_email_format,"
				+ " COALESCE (brandconfig_enquiry_brochure_email_sub,'') AS brandconfig_enquiry_brochure_email_sub,"
				+ " COALESCE (emp.emp_email1, '') AS emp_email1,"
				+ " COALESCE (emp.emp_email2, '') AS emp_email2,"
				+ " COALESCE (emp.emp_name, '') AS emp_name,"
				+ " COALESCE (emp.emp_mobile1, '') AS emp_mobile1,"
				+ " COALESCE (emp.emp_mobile2, '') AS emp_mobile2,"
				+ " config_email_enable,"
				+ " config_sms_enable,"
				+ " comp_email_enable,"
				+ " comp_sms_enable,"
				+ " config_sales_campaign,"
				+ " config_customer_dupnames,"
				+ " COALESCE (team_preownedbranch_id, 0) AS team_preownedbranch_id,"
				+ " COALESCE (team_preownedemp_id, 0) AS team_preownedemp_id"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = 1"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = 1"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id,"
				+ compdb(comp_id) + "axela_config,"
				+ compdb(comp_id) + "axela_comp"
				+ " WHERE 1 = 1"
				+ " AND branch_id = " + enquiry_branch_id;
		// SOP("StrSql---PopulateConfigDetails-------" + StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				branchtype_id = crs.getString("branch_branchtype_id");
				branch_brand_id = crs.getString("branch_brand_id");
				branch_email1 = crs.getString("branch_email1");
				branch_pin = crs.getString("branch_pin");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				config_sales_enquiry_refno = crs.getString("config_sales_enquiry_refno");
				config_sales_campaign = crs.getString("config_sales_campaign");
				config_sales_soe = crs.getString("config_sales_soe");
				config_sales_sob = crs.getString("config_sales_sob");
				config_customer_dupnames = crs.getString("config_customer_dupnames");

				brandconfig_enquiry_email_enable = crs.getString("brandconfig_enquiry_email_enable");
				brandconfig_enquiry_email_format = crs.getString("brandconfig_enquiry_email_format");
				brandconfig_enquiry_email_sub = crs.getString("brandconfig_enquiry_email_sub");
				brandconfig_enquiry_email_exe_enable = crs.getString("brandconfig_enquiry_email_exe_enable");
				brandconfig_enquiry_email_exe_format = crs.getString("brandconfig_enquiry_email_exe_format");
				brandconfig_enquiry_email_exe_sub = crs.getString("brandconfig_enquiry_email_exe_sub");
				brandconfig_enquiry_sms_enable = crs.getString("brandconfig_enquiry_sms_enable");
				brandconfig_enquiry_sms_format = crs.getString("brandconfig_enquiry_sms_format");
				brandconfig_enquiry_sms_exe_enable = crs.getString("brandconfig_enquiry_sms_exe_enable");
				brandconfig_enquiry_sms_exe_format = crs.getString("brandconfig_enquiry_sms_exe_format");
				brandconfig_enquiry_brochure_email_enable = crs.getString("brandconfig_enquiry_brochure_email_enable");
				brandconfig_enquiry_brochure_email_format = crs.getString("brandconfig_enquiry_brochure_email_format");
				brandconfig_enquiry_brochure_email_sub = crs.getString("brandconfig_enquiry_brochure_email_sub");

				team_preownedbranch_id = crs.getString("team_preownedbranch_id");
				team_preownedemp_id = crs.getString("team_preownedemp_id");
				emp_name = crs.getString("emp_name");
				emp_email1 = crs.getString("emp_email1");
				emp_email_formail = emp_email1;
				emp_mobile1 = crs.getString("emp_mobile1");
				emp_mobile2 = crs.getString("emp_mobile2");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto API API== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddEnquiry() {
		try {
			Enquiry_Quickadd enq = new Enquiry_Quickadd();
			enq.emp_id = emp_id;
			enq.enquiry_branch_id = enquiry_branch_id;
			enq.enquiry_customer_id = "0";
			enq.customer_name = customer_name;
			enq.enquiry_title = "New Enquiry";
			enq.contact_fname = customer_name;
			enq.contact_lname = "";
			enq.contact_mobile1 = contact_mobile1;
			enq.contact_mobile2 = "";
			enq.contact_city_id = contact_city_id;
			enq.enquiry_soe_id = enquiry_soe_id;
			enq.enquiry_sob_id = enquiry_sob_id;
			enq.enquiry_emp_id = enquiry_emp_id;
			enq.enquiry_team_id = enquiry_team_id;
			enq.contact_address = "";
			enq.contact_pin = "";
			enq.contact_email1 = contact_email1;
			enq.contact_email2 = "";
			enq.contact_phone1 = "";
			enq.contact_phone2 = "";
			enq.enquiry_buyertype_id = "1";
			enq.enquiry_enquirycat_id = "0";
			enq.enquiry_contact_id = "0";
			enq.comp_id = comp_id;
			enq.branchtype_id = branchtype_id;
			enq.contact_title_id = "1";
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
			enq.brandconfig_enquiry_brochure_email_enable = brandconfig_enquiry_brochure_email_enable;
			enq.brandconfig_enquiry_brochure_email_format = brandconfig_enquiry_brochure_email_format;
			enq.brandconfig_enquiry_brochure_email_sub = brandconfig_enquiry_brochure_email_sub;
			enq.emp_email1 = emp_email1;
			enq.enquiry_desc = "";
			enq.enquiry_date = strToShortDate(ToShortDate(kknow()));
			enq.enquiry_model_id = enquiry_model_id;
			enq.enquiry_item_id = enquiry_item_id;
			enq.enquiry_close_date = strToShortDate(ToShortDate(kknow()));
			enq.enquiry_campaign_id = "2";
			enq.enquiry_notes = "";
			enq.enquiry_finance = "0";
			enq.enquiry_enquirytype_id = "1";
			enq.enquiry_tradein_preownedvariant_id = "0";
			enq.branch_brand_id = branch_brand_id;
			if (branchtype_id.equals("1")) {
				enq.enquiry_enquirytype_id = "1";
			}
			if (branchtype_id.equals("2")) {
				enq.enquiry_enquirytype_id = "2";
			}
			enq.enquiry_entry_id = emp_id;
			enq.enquiry_entry_date = ToLongDate(kknow());
			enq.enquiry_budget = "0";
			// if (branchtype_id.equals("2")) {
			// enq.enquiry_preownedvariant_id = enquiry_preownedvariant_id;
			// enq.enquiry_fueltype_id = enquiry_fueltype_id;
			// enq.enquiry_prefreg_id = enquiry_prefreg_id;
			// enq.enquiry_presentcar = enquiry_presentcar;
			// enq.enquiry_budget = enquiry_budget;
			// enq.enquiry_finance = enquiry_finance;
			// }
			enq.contact_jobtitle = "0";
			enq.emp_enquiry_edit = "0";
			enq.AddEnquiryFields();
			msg = enq.msg;
			enquiry_id = enq.enquiry_id;
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Axelaauto-API=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

}
