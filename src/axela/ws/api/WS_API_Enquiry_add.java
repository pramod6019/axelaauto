package axela.ws.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import axela.sales.Enquiry_Quickadd;
import cloudify.connect.Connect;

import com.google.gson.Gson;

public class WS_API_Enquiry_add extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String enquiry_contact_id = "0";
	public String customer_name = "";
	public String contact_title_id = "0";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_mobile1 = "";
	public String contact_jobtitle = "";
	public String enquiry_budget = "0";
	public String enquiry_finance = "0";
	public String enquiry_presentcar = "";
	public String enquiry_prefreg_id = "0";
	public String enquiry_fueltype_id = "0";
	public String enquiry_tradein_preownedvariant_id = "0";
	public String enquiry_preownedvariant_id = "0";
	public String enquiry_enquirytype_id = "0";
	public String enquiry_notes = "";
	public String enquiry_enquirycat_id = "0";
	public String enquiry_buyertype_id = "0";
	public String enquiry_campaign_id = "0";
	public String enquiry_sob_id = "0";
	public String enquiry_soe_id = "0";
	public String enquiry_team_id = "0";
	public String enquiry_item_id = "0";
	public String enquiry_model_id = "0";
	public String enquiry_desc = "";
	public String close_date = "";
	public String enquiry_close_date = "";
	public String enquiry_date = "";
	public String emp_enquiry_edit = "";
	public String contact_pin = "";
	public String contact_city_id = "0";
	public String contact_address = "";
	public String contact_phone2 = "";
	public String contact_phone1 = "";
	public String contact_email2 = "";
	public String contact_mobile2 = "";
	public String contact_email1 = "";
	public String emp_role_id = "0";
	public String enquiry_branch_id = "0";
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
	public String branch_enquiry_email_enable = "";
	public String branch_enquiry_email_format = "";
	public String branch_enquiry_email_sub = "";
	public String branch_enquiry_email_exe_sub = "";
	public String branch_enquiry_email_exe_format = "";
	public String branch_enquiry_sms_enable = "";
	public String branch_enquiry_sms_format = "";
	public String branch_enquiry_sms_exe_format = "";
	public String branch_enquiry_brochure_email_enable = "";
	public String branch_enquiry_brochure_email_format = "";
	public String branch_enquiry_brochure_email_sub = "";
	public String team_preownedbranch_id = "0";
	public String team_preownedemp_id = "0";
	public String emp_name = "";
	public String emp_email1 = "";
	public String emp_email_formail = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String enquiry_emp_id = "0";
	public String enquiry_budget_id = "";
	public String branch_id = "0";
	public String enquiry_title = "";
	public String enquiry_id = "0";
	public String enquiry_branch_name = "";
	public String enquiry_team_name = "";
	public String enquiry_model_name = "";
	public String enquiry_soe_name = "";
	public String msg = "";
	public Response response = null;

	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	JSONObject config = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public Response Enquiry_add(JSONObject input, @Context HttpServletRequest request) {
		try {
			if (AppRun().equals("0")) {
				SOP("input==WS Enquiry Add===" + input.toString(1));
			}
			comp_id = CNumeric(PadQuotes((String) request.getAttribute("comp_id")));
			if (!comp_id.equals("0")) {
				if (!input.isNull("emp_name")) {
					String emp_name = PadQuotes((String) input.get("emp_name"));
					emp_id = CNumeric(ExecuteQuery("SELECT emp_id"
							+ " FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE emp_name='" + emp_name + "'"
							+ " AND emp_active = '1'"));
				}
				if (!emp_id.equals("0")) {
					if (!input.isNull("enquiry_branch_name")) {
						enquiry_branch_name = PadQuotes((String) input.get("enquiry_branch_name"));
						enquiry_branch_id = CNumeric(ExecuteQuery("SELECT branch_id"
								+ " FROM " + compdb(comp_id) + "axela_branch"
								+ " WHERE branch_name='" + enquiry_branch_name + "'"
								+ " AND branch_active = 1"));
						if (enquiry_branch_id.equals("0")) {
							msg += "<br> Invalid Branch";
						}
					}
					if (!input.isNull("enquiry_team_name")) {
						enquiry_team_name = PadQuotes((String) input.get("enquiry_team_name"));
						enquiry_team_id = CNumeric(ExecuteQuery("SELECT team_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_team"
								+ " WHERE team_name='" + enquiry_team_name + "'"
								+ " AND team_branch_id=" + enquiry_branch_id + ""
								+ " AND team_active = 1"));
						// + " AND team_branch_id=" + enquiry_branch_id));
						if (enquiry_team_id.equals("0")) {
							msg += "<br> Invalid Team";
						} else if (!enquiry_branch_id.equals("0")) {
							if (CNumeric(ExecuteQuery("SELECT team_id"
									+ " FROM " + compdb(comp_id) + "axela_sales_team"
									+ " WHERE team_id='" + enquiry_team_id + "'"
									+ " AND team_branch_id=" + enquiry_branch_id)).equals("0")) {
								msg += " <br> " + enquiry_team_name + " is not Associated with " + enquiry_branch_name + "";
							}
						}
					}
					if (!input.isNull("enquiry_emp_name")) {
						String enquiry_emp_name = PadQuotes((String) input.get("enquiry_emp_name"));
						enquiry_emp_id = CNumeric(ExecuteQuery("SELECT emp_id"
								+ " FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_name='" + enquiry_emp_name + "'"
								+ " AND emp_active = '1'"
								+ " AND emp_sales = '1'"));
						if (enquiry_emp_id.equals("0")) {
							msg += "<br> Invalid Sales Consultant";
						} else if (!(enquiry_emp_id.equals("1") && emp_id.equals("1"))) {
							if (!enquiry_team_id.equals("0")) {
								if (CNumeric(ExecuteQuery("SELECT emp_id"
										+ " FROM " + compdb(comp_id) + "axela_emp"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = emp_id"
										+ " WHERE emp_id='" + enquiry_emp_id + "'"
										+ " AND teamtrans_team_id=" + enquiry_team_id + "")).equals("0")) {
									msg += "<br> " + enquiry_emp_name + " is not Associated with " + enquiry_team_name;
								}
							}
						}
					}

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
				} else {
					output.put("status_message", "Invalid Executive Name");
					response = Response.status(400).entity(output).build();
				}
			} else {
				output.put("status_message", "Invalid Company ID");
				response = Response.status(400).entity(output).build();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto API===API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return response;
	}
	public void PopulateConfigDetails() {
		String StrSql = "SELECT config_sales_soe, config_sales_sob, config_sales_enquiry_refno,"
				+ " config_email_enable, "
				+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
				+ " config_sales_campaign, "
				+ " COALESCE(branch_branchtype_id,0) As branch_branchtype_id, "
				+ " COALESCE(branch_email1,'') AS branch_email1, branch_pin,"
				+ " COALESCE(branch_enquiry_email_enable,'') AS branch_enquiry_email_enable,"
				+ " COALESCE(branch_enquiry_email_format,'') AS branch_enquiry_email_format,"
				+ " COALESCE(branch_enquiry_email_sub,'') AS branch_enquiry_email_sub,"
				+ " COALESCE(branch_enquiry_email_exe_sub,'') AS branch_enquiry_email_exe_sub,"
				+ " COALESCE(branch_enquiry_email_exe_format,'') AS branch_enquiry_email_exe_format,"
				+ " COALESCE(branch_enquiry_sms_enable,'') AS branch_enquiry_sms_enable,"
				+ " COALESCE(branch_enquiry_sms_format,'') AS branch_enquiry_sms_format,"
				+ " COALESCE(branch_enquiry_sms_exe_format,'') AS branch_enquiry_sms_exe_format,"
				+ " COALESCE(branch_enquiry_brochure_email_enable,'') AS branch_enquiry_brochure_email_enable, "
				+ " COALESCE(branch_enquiry_brochure_email_format,'') AS branch_enquiry_brochure_email_format, "
				+ " COALESCE(branch_enquiry_brochure_email_sub,'') AS branch_enquiry_brochure_email_sub, "
				+ " COALESCE(emp.emp_email1,'') AS emp_email1, COALESCE(emp.emp_email2,'') AS emp_email2,"
				+ " COALESCE(emp.emp_name,'') AS emp_name, "
				+ " COALESCE(emp.emp_mobile1,'') AS emp_mobile1 , COALESCE(emp.emp_mobile2,'') AS emp_mobile2, "
				+ " config_email_enable, "
				+ " config_sms_enable, comp_email_enable, comp_sms_enable, "
				+ " config_sales_campaign,"
				+ " COALESCE(branch_brand_id,0) AS branch_brand_id,"
				+ " COALESCE(branch_email1,'') AS branch_email1, branch_pin,"
				+ " COALESCE(branch_enquiry_email_enable,'') AS branch_enquiry_email_enable,"
				+ " COALESCE(branch_enquiry_email_format,'') AS branch_enquiry_email_format,"
				+ " COALESCE(branch_enquiry_email_sub,'') AS branch_enquiry_email_sub,"
				+ " COALESCE(branch_enquiry_email_exe_sub,'') AS branch_enquiry_email_exe_sub,"
				+ " COALESCE(branch_enquiry_email_exe_format,'') AS branch_enquiry_email_exe_format,"
				+ " COALESCE(branch_enquiry_sms_enable,'') AS branch_enquiry_sms_enable,"
				+ " COALESCE(branch_enquiry_sms_format,'') AS branch_enquiry_sms_format,"
				+ " COALESCE(branch_enquiry_sms_exe_format,'') AS branch_enquiry_sms_exe_format,"
				+ " COALESCE(branch_enquiry_brochure_email_enable,'') AS branch_enquiry_brochure_email_enable, "
				+ " COALESCE(branch_enquiry_brochure_email_format,'') AS branch_enquiry_brochure_email_format, "
				+ " COALESCE(branch_enquiry_brochure_email_sub,'') AS branch_enquiry_brochure_email_sub, "
				+ " COALESCE(emp.emp_email1,'') AS emp_email1, COALESCE(emp.emp_email2,'') AS emp_email2,"
				+ " COALESCE(emp.emp_name,'') AS emp_name, "
				+ " COALESCE(emp.emp_mobile1,'') AS emp_mobile1 , COALESCE(emp.emp_mobile2,'') AS emp_mobile2, "
				+ " config_customer_dupnames, "
				+ " COALESCE(team_preownedbranch_id, 0) AS team_preownedbranch_id,"
				+ " COALESCE(team_preownedemp_id, 0) AS team_preownedemp_id"
				+ " FROM " + compdb(comp_id) + "axela_branch"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_emp emp ON emp.emp_id = " + enquiry_emp_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id =" + enquiry_emp_id + ""
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

	public void GetValues(JSONObject input) throws ServletException, IOException {
		try {
			if (!input.isNull("emp_role_id")) {
				emp_role_id = CNumeric(PadQuotes((String) input.get("emp_role_id")));
			}
			if (!input.isNull("enquiry_contact_id")) {
				enquiry_contact_id = CNumeric(PadQuotes((String) input.get("enquiry_contact_id")));
			}
			if (!input.isNull("enquiry_contact_title_name")) {
				String contact_title_name = PadQuotes((String) input.get("enquiry_contact_title_name"));
				contact_title_id = CNumeric(ExecuteQuery("SELECT title_id"
						+ " FROM " + compdb(comp_id) + "axela_title"
						+ " WHERE title_desc='" + contact_title_name + "'"));
				if (contact_title_id.equals("0")) {
					msg += " <br> Invalid Contact Title";
				}
			}
			if (enquiry_contact_id.equals("0")) {
				if (!input.isNull("enquiry_customer_name")) {
					customer_name = PadQuotes(PadQuotes((String) input.get("enquiry_customer_name")));
				}
			}
			if (!input.isNull("enquiry_contact_fname")) {
				contact_fname = PadQuotes(PadQuotes((String) input.get("enquiry_contact_fname")));
			}
			if (!input.isNull("enquiry_contact_lname")) {
				contact_lname = PadQuotes(PadQuotes((String) input.get("enquiry_contact_lname")));
			}
			if (!input.isNull("enquiry_contact_jobtitle")) {
				contact_jobtitle = PadQuotes(PadQuotes((String) input.get("enquiry_contact_jobtitle")));
			}
			if (!input.isNull("enquiry_contact_mobile1")) {
				contact_mobile1 = PadQuotes(PadQuotes((String) input.get("enquiry_contact_mobile1")));
				if (!isNumericNeg(contact_mobile1)) {
					msg += " <br> Invalid Mobile1";
				}
			}
			if (!input.isNull("enquiry_contact_email1")) {
				contact_email1 = PadQuotes(PadQuotes((String) input.get("enquiry_contact_email1")));
			}
			if (!input.isNull("enquiry_contact_mobile2")) {
				contact_mobile2 = PadQuotes(PadQuotes((String) input.get("enquiry_contact_mobile2")));
				if (!isNumericNeg(contact_mobile2)) {
					msg += " <br> Invalid Mobile2";
				}
			}
			// if (!input.isNull("enquiry_contact_email2")) {
			// contact_email2 = PadQuotes(PadQuotes((String) input.get("enquiry_contact_email2")));
			// }
			if (!input.isNull("enquiry_contact_phone1")) {
				contact_phone1 = PadQuotes(PadQuotes((String) input.get("enquiry_contact_phone1")));
				if (!isNumericNeg(contact_phone1)) {
					msg += " <br> Invalid Phone 1";
				}
			}
			if (!input.isNull("enquiry_contact_phone2")) {
				contact_phone2 = PadQuotes(PadQuotes((String) input.get("enquiry_contact_phone2")));
				if (!isNumericNeg(contact_phone2)) {
					msg += " <br> Invalid Phone 2";
				}
			}
			if (!input.isNull("enquiry_contact_address")) {
				contact_address = PadQuotes(PadQuotes((String) input.get("enquiry_contact_address")));
			}
			if (!input.isNull("enquiry_contact_city_name")) {
				String contact_city_name = PadQuotes((String) input.get("enquiry_contact_city_name"));
				contact_city_id = CNumeric(ExecuteQuery("SELECT city_id"
						+ " FROM " + compdb(comp_id) + "axela_city"
						+ " WHERE city_name='" + contact_city_name + "'"));
				if (contact_city_id.equals("0")) {
					msg += " <br> Invalid City Name";
				} else {

				}
			}
			if (!input.isNull("enquiry_contact_pin")) {
				contact_pin = CNumeric(PadQuotes((String) input.get("enquiry_contact_pin")));
				if (!contact_pin.equals("0")) {
					if (contact_pin.length() != 6) {
						msg += " <br> Contact Pin must be 6 digits";
					}
					if (!contact_pin.matches("[0-9]+") || contact_pin.matches("[a-zA-Z]+")) {
						msg += " <br> Invalid Pin/Zip";
					}
				}
			}

			if (!input.isNull("enquiry_budget")) {
				enquiry_budget = CNumeric(PadQuotes((String) input.get("enquiry_budget")));
				if (enquiry_budget.length() > 10) {
					msg += " <br> Invalid Budget";
				}
			}
			if (!input.isNull("emp_enquiry_edit")) {
				emp_enquiry_edit = PadQuotes((String) input.get("emp_enquiry_edit"));
			}
			// if (emp_enquiry_edit.equals("1")) {
			if (!input.isNull("enquiry_date")) {
				enquiry_date = PadQuotes((String) input.get("enquiry_date"));
				if (!isValidDateFormatShort(enquiry_date)) {
					msg += " <br> Invalid Enquiry Date";
				}
			}
			// else {
			// msg += "<br>Select Enquiry Date!";
			// }
			// } else {
			// enquiry_date = strToShortDate(ToShortDate(kknow()));
			// }
			if (!input.isNull("enquiry_close_date")) {
				enquiry_close_date = PadQuotes((String) input.get("enquiry_close_date"));
				if (!isValidDateFormatShort(enquiry_close_date)) {
					msg += " <br> Invalid Close Date";
				} else {
					close_date = enquiry_close_date;
				}
			}
			// else {
			// msg += "<br>Select Closing Date!";
			// }
			if (!input.isNull("enquiry_desc")) {
				enquiry_desc = PadQuotes((String) input.get("enquiry_desc"));
			}
			if (!input.isNull("enquiry_title")) {
				enquiry_title = PadQuotes((String) input.get("enquiry_title"));
			}
			// new car
			if (branchtype_id.equals("1")) {
				if (!input.isNull("enquiry_model_name")) {
					enquiry_model_name = PadQuotes((String) input.get("enquiry_model_name"));
					enquiry_model_id = CNumeric(ExecuteQuery("SELECT model_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
							+ " WHERE model_name='" + enquiry_model_name + "'"
							+ "  AND model_active = 1"));
					if (enquiry_model_id.equals("0")) {
						msg += " <br> Invalid Model";
					} else {
						String model_id = CNumeric(ExecuteQuery("SELECT model_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
								+ " WHERE model_id='" + enquiry_model_id + "'"
								+ " AND model_brand_id='" + branch_brand_id + "'"
								+ " AND model_active = 1"));
						SOP("SELECT model_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
								+ " WHERE model_id='" + enquiry_model_id + "'"
								+ " AND model_brand_id='" + branch_brand_id + "'"
								+ " AND model_active = 1");
						if (model_id.equals("0")) {
							msg += " <br>" + enquiry_model_name + " is not Associated with " + enquiry_branch_name + "";
						}

					}
				}
				if (!input.isNull("enquiry_item_name")) {
					String enquiry_item_name = PadQuotes((String) input.get("enquiry_item_name"));
					enquiry_item_id = CNumeric(ExecuteQuery("SELECT item_id"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " WHERE item_name='" + enquiry_item_name + "'"
							+ " AND item_active = '1'"));
					// + " AND item_model_id=" + enquiry_model_id + ""));
					if (enquiry_item_id.equals("0")) {
						msg += "<br> Invalid Variant";
					} else if (!enquiry_model_id.equals("0")) {
						if (CNumeric(ExecuteQuery("SELECT item_id"
								+ " FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE item_id='" + enquiry_item_id + "'"
								+ " AND item_model_id=" + enquiry_model_id + "")).equals("0")) {
							msg += " <br> " + enquiry_item_name + " is not Associated with " + enquiry_model_name + "";
						}
					}
				}
			}
			if (config_sales_soe.equals("1")) {
				if (!input.isNull("enquiry_soe_name")) {
					enquiry_soe_name = PadQuotes((String) input.get("enquiry_soe_name"));
					enquiry_soe_id = CNumeric(ExecuteQuery("SELECT soe_id"
							+ " FROM " + compdb(comp_id) + "axela_soe"
							+ " WHERE soe_name='" + enquiry_soe_name + "'"
							+ " AND soe_active = 1"));
					if (enquiry_soe_id.equals("0")) {
						msg += " <br> Invalid SOE";
					}
				}
			}
			if (config_sales_sob.equals("1")) {
				if (!input.isNull("enquiry_sob_name")) {
					String enquiry_sob_name = PadQuotes((String) input.get("enquiry_sob_name"));
					enquiry_sob_id = CNumeric(ExecuteQuery("SELECT sob_id"
							+ " FROM " + compdb(comp_id) + "axela_sob"
							+ " WHERE sob_name='" + enquiry_sob_name + "' "));
					if (enquiry_sob_id.equals("0")) {
						msg += " <br> Invalid SOB";
					} else {
						String sob_id = CNumeric(ExecuteQuery("SELECT sob_id"
								+ " FROM " + compdb(comp_id) + "axela_sob"
								+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
								+ " WHERE 1 = 1"
								+ " AND soetrans_soe_id = " + enquiry_soe_id));
						if (sob_id.equals("0")) {
							msg += " <br>" + enquiry_sob_name + " not Associated with " + enquiry_soe_name + "";
						}

					}
				}
			}
			if (config_sales_campaign.equals("1")) {
				if (!input.isNull("enquiry_campaign_name")) {
					String enquiry_campaign_name = PadQuotes((String) input.get("enquiry_campaign_name"));
					enquiry_campaign_id = CNumeric(ExecuteQuery("SELECT campaign_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_campaign"
							+ " WHERE campaign_name='" + enquiry_campaign_name + "'"
							+ " AND campaign_active = '1' "));
					if (enquiry_campaign_id.equals("0")) {
						msg += " <br> Invalid Campaign";
					}
				}
			}
			if (!input.isNull("enquiry_buyertype_name")) {
				String enquiry_buyertype_name = PadQuotes((String) input.get("enquiry_buyertype_name"));
				enquiry_buyertype_id = CNumeric(ExecuteQuery("SELECT buyertype_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
						+ " WHERE buyertype_name='" + enquiry_buyertype_name + "'"));
				if (enquiry_buyertype_id.equals("0")) {
					msg += " <br> Invalid Buyer Type";
				}
			}
			if (!input.isNull("enquiry_enquirycat_name")) {
				String enquiry_enquirycat_name = PadQuotes((String) input.get("enquiry_enquirycat_name"));
				enquiry_enquirycat_id = CNumeric(ExecuteQuery("SELECT enquirycat_id"
						+ " FROM axela_sales_enquiry_cat"
						+ " WHERE enquirycat_name='" + enquiry_enquirycat_name + "'"));
				if (enquiry_enquirycat_id.equals("0")) {
					msg += " <br> Invalid Category";
				}
			}
			if (!input.isNull("enquiry_notes")) {
				enquiry_notes = PadQuotes((String) input.get("enquiry_notes"));
			}
			if (!input.isNull("enquiry_enquirytype_id")) {
				enquiry_enquirytype_id = CNumeric(PadQuotes((String) input.get("enquiry_enquirytype_id")));
			}
			// Pre-owned fields
			if (branchtype_id.equals("2")) {
				if (!input.isNull("enquiry_preownedvariant_name")) {
					String enquiry_preownedvariant_name = PadQuotes((String) input.get("enquiry_preownedvariant_name"));
					enquiry_preownedvariant_id = CNumeric(ExecuteQuery("SELECT variant_id"
							+ " FROM axela_preowned_variant"
							+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
							+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
							+ " WHERE CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)='" + enquiry_preownedvariant_name + "'"));
					if (enquiry_preownedvariant_id.equals("0")) {
						msg += " <br> Invalid Preowned MOdel";
					}
				}

				if (!input.isNull("enquiry_fueltype_name")) {
					String enquiry_fueltype_name = PadQuotes((String) input.get("enquiry_fueltype_name"));
					enquiry_fueltype_id = CNumeric(ExecuteQuery("SELECT fueltype_id"
							+ " FROM " + compdb(comp_id) + "axela_fueltype"
							+ " WHERE fueltype_name='" + enquiry_fueltype_name + "'"));
					if (enquiry_fueltype_id.equals("0")) {
						msg += " <br> Invalid Fuel Type";
					}
				}

				if (!input.isNull("enquiry_prefreg_name")) {
					String enquiry_prefreg_name = PadQuotes((String) input.get("enquiry_prefreg_name"));
					enquiry_prefreg_id = CNumeric(ExecuteQuery("SELECT prefreg_id"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_prefreg"
							+ " WHERE prefreg_name='" + enquiry_prefreg_name + "'"));
					if (enquiry_prefreg_id.equals("0")) {
						msg += " <br> Invalid Fuel Type";
					}
				}

				if (!input.isNull("enquiry_presentcar")) {
					enquiry_presentcar = PadQuotes((String) input.get("enquiry_presentcar"));
				}
				if (!input.isNull("enquiry_finance")) {
					enquiry_finance = PadQuotes((String) input.get("enquiry_finance"));
					if (enquiry_finance.equalsIgnoreCase("yes")) {
						enquiry_finance = "1";
					} else if (enquiry_finance.equalsIgnoreCase("no")) {
						enquiry_finance = "0";
					} else {
						msg += " <br> Enquiry Finance shoud be Yes or No!";
					}
				}
				if (!input.isNull("enquiry_budget")) {
					enquiry_budget = PadQuotes((String) input.get("enquiry_budget"));
				}
			}
			if (!input.isNull("enquiry_tradein_preownedvariant_name")) {
				String enquiry_tradein_preownedvariant_name = PadQuotes((String) input.get("enquiry_tradein_preownedvariant_name"));
				enquiry_tradein_preownedvariant_id = CNumeric(ExecuteQuery("SELECT variant_id"
						+ " FROM axela_preowned_variant"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)='" + enquiry_tradein_preownedvariant_name + "'"));
				if (enquiry_tradein_preownedvariant_id.equals("0")) {
					msg += " <br> Invalid Trade-In Model";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto API===API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void AddEnquiry() {
		try {
			Enquiry_Quickadd enq = new Enquiry_Quickadd();
			enq.emp_id = emp_id;
			enq.enquiry_branch_id = enquiry_branch_id;
			enq.customer_name = contact_fname + " " + contact_lname;
			enq.contact_fname = contact_fname;
			enq.enquiry_title = enquiry_title;
			enq.contact_lname = contact_lname;
			enq.contact_mobile1 = contact_mobile1;
			enq.contact_mobile2 = contact_mobile2;
			enq.contact_city_id = contact_city_id;
			enq.enquiry_soe_id = enquiry_soe_id;// 22
			enq.enquiry_sob_id = enquiry_sob_id;// 40
			enq.enquiry_emp_id = enquiry_emp_id;
			enq.enquiry_team_id = enquiry_team_id;
			enq.contact_address = contact_address;
			enq.contact_pin = contact_pin;
			enq.contact_email1 = contact_email1;
			enq.contact_email2 = contact_email2;
			enq.contact_phone1 = contact_phone1;
			enq.contact_phone2 = contact_phone2;
			enq.enquiry_buyertype_id = enquiry_buyertype_id;
			enq.enquiry_enquirycat_id = enquiry_enquirycat_id;
			enq.enquiry_contact_id = enquiry_contact_id;
			enq.comp_id = comp_id;
			enq.branchtype_id = branchtype_id;
			enq.contact_title_id = contact_title_id;
			enq.config_sales_soe = config_sales_soe;
			enq.config_sales_sob = config_sales_sob;
			enq.config_sales_campaign = config_sales_campaign;
			enq.config_sales_enquiry_refno = config_sales_enquiry_refno;
			enq.comp_email_enable = comp_email_enable;
			enq.config_email_enable = config_email_enable;
			enq.branch_email1 = branch_email1;
			enq.branch_enquiry_email_enable = branch_enquiry_email_enable;
			enq.branch_enquiry_email_format = branch_enquiry_email_format;
			enq.branch_enquiry_email_sub = branch_enquiry_email_sub;
			enq.emp_email_formail = emp_email_formail;
			enq.branch_enquiry_email_exe_format = branch_enquiry_email_exe_format;
			enq.branch_enquiry_email_exe_sub = branch_enquiry_email_exe_sub;
			enq.comp_sms_enable = comp_sms_enable;
			enq.config_sms_enable = config_sms_enable;
			enq.branch_enquiry_sms_enable = branch_enquiry_sms_enable;
			enq.branch_enquiry_sms_format = branch_enquiry_sms_format;
			enq.branch_enquiry_sms_exe_format = branch_enquiry_sms_exe_format;
			enq.branch_enquiry_brochure_email_enable = branch_enquiry_brochure_email_enable;
			enq.branch_enquiry_brochure_email_format = branch_enquiry_brochure_email_format;
			enq.branch_enquiry_brochure_email_sub = branch_enquiry_brochure_email_sub;
			enq.emp_email1 = emp_email1;
			enq.enquiry_desc = "";
			enq.enquiry_date = enquiry_date;
			enq.enquiry_model_id = enquiry_model_id;
			enq.enquiry_item_id = enquiry_item_id;
			enq.enquiry_close_date = enquiry_close_date;
			enq.enquiry_campaign_id = enquiry_campaign_id;
			enq.enquiry_notes = enquiry_notes;
			enq.enquiry_finance = enquiry_finance;
			enq.enquiry_enquirytype_id = "1";
			enq.enquiry_tradein_preownedvariant_id = enquiry_tradein_preownedvariant_id;
			SOP("enquiry_tradein_preownedvariant_id" + enquiry_tradein_preownedvariant_id);
			enq.branch_brand_id = branch_brand_id;
			if (branchtype_id.equals("1")) {
				enq.enquiry_enquirytype_id = "1";
			}
			if (branchtype_id.equals("2")) {
				enq.enquiry_enquirytype_id = "2";
			}
			enq.enquiry_entry_id = emp_id;
			enq.enquiry_entry_date = ToLongDate(kknow());
			enq.enquiry_budget = enquiry_budget;
			if (branchtype_id.equals("2")) {
				enq.enquiry_preownedvariant_id = enquiry_preownedvariant_id;
				enq.enquiry_fueltype_id = enquiry_fueltype_id;
				enq.enquiry_prefreg_id = enquiry_prefreg_id;
				enq.enquiry_presentcar = enquiry_presentcar;
				enq.enquiry_budget = enquiry_budget;
				enq.enquiry_finance = enquiry_finance;
			}
			enq.contact_jobtitle = contact_jobtitle;
			enq.emp_enquiry_edit = EnquiryDateEdit();
			enq.AddEnquiryFields();
			msg = enq.msg;
			enquiry_id = enq.enquiry_id;
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Axelaauto-API=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String EnquiryDateEdit() {
		String CheckPerm = "1", fieldn_name = "";
		try {
			if (!emp_id.equals("1")) {
				fieldn_name = "emp_enquiry_edit";
				String Strsql = "SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_emp_access on empaccess_emp_id=emp_id "
						+ " LEFT JOIN axela_module_access on access_id=empaccess_access_id "
						+ " WHERE emp_id=" + emp_id
						+ " AND access_name='" + fieldn_name + "'"
						+ " GROUP BY emp_id";

				// SOP("EnquiryDateEditEnquiryDateEditEnquiryDateEdit : " + StrSqlBreaker(Strsql));
				CheckPerm = ExecuteQuery(Strsql);

				if (!CheckPerm.equals("")) {
					CheckPerm = "1";
				} else {
					CheckPerm = "0";
				}
			}
		} catch (Exception ex) {

		}
		return CheckPerm;
	}
	// public void AddFields() {
	// try {
	// Enquiry_Quickadd obj = new Enquiry_Quickadd();
	// obj.emp_id = emp_id;
	// if (enquiry_contact_id.equals("0")) {
	//
	// obj.enquiry_branch_id = enquiry_branch_id;
	// obj.contact_title_id = contact_title_id;
	// obj.contact_fname = contact_fname;
	// obj.contact_fname = contact_fname;
	// obj.contact_lname = contact_lname;
	// obj.contact_jobtitle = contact_jobtitle;
	// obj.contact_mobile1 = contact_mobile1;
	// obj.contact_mobile2 = contact_mobile2;
	// obj.contact_email1 = contact_email1;
	// obj.contact_email2 = contact_email2;
	// obj.contact_phone1 = contact_phone1;
	// obj.contact_phone2 = contact_phone2;
	// obj.contact_address = contact_address;
	// obj.contact_city_id = contact_city_id;
	// obj.contact_pin = contact_pin;
	// obj.AddCustomerFields();
	// obj.enquiry_customer_id = obj.enquiry_customer_id;
	// obj.AddContactFields();
	// }
	// } catch (Exception ex) {
	// SOPError("Axelaauto API===API==" + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// }

}
