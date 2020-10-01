/* Annappa (20th May 2015) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_Dash extends ConnectWS {

	public String msg = "";
	public String StrSql = "";
	public String update = "";
	public String branch_id = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String role_id = "";
	public String enquiry_id = "";
	public String enquiry_emp_id = "";
	public String enquiry_branch_id = "";
	public String model_id = "";
	public String name = "";
	public String value = "";
	public String name1 = "";
	public String value1 = "";
	public String make_id = "0", enquiry_tradeinmake_id = "0", status_id = "";
	public String commonfields = "";
	public String status_desc = "";
	public String lostcase2 = "";
	public String lostcase3 = "";
	public String lostcase1 = "";
	public String city_name = "";
	public String contact_city_id = "0";
	public String enquiry_age_id = "";
	public String enquiry_occ_id = "";
	public String item_model_id = "";
	public String ExeAccess = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject Enquiry_Dash(JSONObject input) {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));

				if (!input.isNull("comp_id")) {
					comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
				}
				if (!input.isNull("emp_uuid")) {
					emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
				}
				if (!input.isNull("enquiry_id")) {
					enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
				}

				if (!input.isNull("commonfields")) {
					commonfields = PadQuotes((String) input.get("commonfields"));
				}

				if (!emp_id.equals("0")) {
					String StrSql1 = "SELECT emp_branch_id, emp_role_id FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + emp_id;
					CachedRowSet crs1 = processQuery(StrSql1, 0);
					while (crs1.next()) {
						role_id = crs1.getString("emp_role_id");
						branch_id = crs1.getString("emp_branch_id");
					}
					crs1.close();
					if (!input.isNull("update")) {
						update = PadQuotes((String) input.get("update"));
					}
					// ExeAccess = WSCheckExeAccess(emp_id);
					if (!enquiry_id.equals("0") && update.equals("")) {
						if (!commonfields.equals("yes")) {
							PopulateFields();
							PopulateModel();
							PopulateVariantColour();
							PopulateOpprPriority();
							PopulateStatus();
							PopulateContactTitle();
							PopulateItem();
							PopulateExecutive();
							PopulateCustomerType();
							PopulateAge();
							PopulateOccupation();
							PopulateMonthKms();
							PopulatePurchaseMode();
							PopulateIncome();
							PopulateExpectation();
							PopulateBuyerType();
							PopulateOwnership();
							//

						} else {
							PopulateFields();
						}
					} else if (!enquiry_id.equals("0") && update.equals("yes")) {
						if (!input.isNull("model_id")) {
							model_id = CNumeric(PadQuotes((String) input.get("model_id")));
							if (!model_id.equals("0")) {
								PopulateModelItem(input);
							}
						}
						output = UpdateFields(input);
					}
				}
			}
			if (AppRun().equals("0")) {
				SOP("output = " + output);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateFields() {

		StrSql = "select " + compdb(comp_id) + "axela_sales_enquiry.*, customer_name, contact_title_id, contact_fname, contact_lname, "
				+ " contact_mobile1, contact_mobile2, contact_phone1, contact_email1, contact_email2,"
				+ " contact_phone1, contact_phone2, contact_address,"
				+ " contact_city_id, contact_pin, "
				+ " branch_name, branch_code, branch_brand_id, stage_name, coalesce(soe_name,'') as soe_name, title_desc, enquiry_evaluation,"
				+ " coalesce((SELECT CONCAT(emp_name, ' (', emp_ref_no, ')') FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = enquiry_entry_id), '') as entryby,"
				+ " coalesce((SELECT CONCAT(emp_name, ' (', emp_ref_no, ')')  FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = enquiry_modified_id), '') as modifiedby"
				+ " from " + compdb(comp_id) + "axela_sales_enquiry"
				+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id "
				+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id "
				+ " inner join " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id "
				+ " inner join " + compdb(comp_id) + "axela_branch on branch_id = enquiry_branch_id "
				+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_stage on stage_id = enquiry_stage_id "
				+ " inner join " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " left join " + compdb(comp_id) + "axela_soe on soe_id = enquiry_soe_id "
				+ " where enquiry_id=" + enquiry_id
				+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id)
				+ "axela_emp_exe where empexe_emp_id =" + emp_id + ") OR enquiry_emp_id =" + emp_id + "),enquiry_id > 0)"
				// + ExeAccess.replace("emp_id", "enquiry_emp_id")
				+ " group by enquiry_id ";
		// SOPError("StrSql ====EnquiryDash===PopulateFields==== " + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					if (commonfields.equals("yes")) {
						map.put("enquiry_no", "ENQ" + crs.getString("branch_code") + crs.getString("enquiry_no"));
						map.put("branch_name", crs.getString("branch_name"));
						map.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
						map.put("contact_fname", crs.getString("contact_fname"));
						map.put("contact_lname", crs.getString("contact_lname"));
						map.put("customer_name", crs.getString("customer_name"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("title_desc", crs.getString("title_desc"));
						map.put("enquiry_customer_id", crs.getString("enquiry_customer_id"));
					} else {
						map.put("enquiry_id", crs.getString("enquiry_id"));
						map.put("enquiry_lead_id", crs.getString("enquiry_lead_id"));
						map.put("enquiry_model_id", crs.getString("enquiry_model_id"));
						item_model_id = crs.getString("enquiry_model_id");
						map.put("enquiry_item_id", crs.getString("enquiry_item_id"));
						map.put("enquiry_title", crs.getString("enquiry_title"));
						map.put("enquiry_no", "ENQ" + crs.getString("branch_code") + crs.getString("enquiry_no"));
						map.put("enquiry_branch_id", crs.getString("enquiry_branch_id"));
						map.put("branch_name", crs.getString("branch_name"));
						map.put("branch_brand_id", crs.getString("branch_brand_id"));
						map.put("enquiry_date", strToShortDate(crs.getString("enquiry_date")));
						map.put("enquiry_close_date", strToShortDate(crs.getString("enquiry_close_date")));
						map.put("enquiry_desc", crs.getString("enquiry_desc"));
						map.put("enquiry_emp_id", crs.getString("enquiry_emp_id"));
						map.put("enquiry_customer_id", crs.getString("enquiry_customer_id"));
						map.put("enquiry_contact_id", crs.getString("enquiry_contact_id"));
						map.put("enquiry_dmsno", crs.getString("enquiry_dmsno"));
						map.put("enquiry_value_syscal", crs.getString("enquiry_value_syscal"));
						map.put("enquiry_value", crs.getString("enquiry_value"));
						map.put("stage_name", crs.getString("stage_name"));
						map.put("enquiry_avpresent", crs.getString("enquiry_avpresent"));
						map.put("enquiry_manager_assist", crs.getString("enquiry_manager_assist"));
						map.put("enquiry_status_id", crs.getString("enquiry_status_id"));
						map.put("enquiry_status_date", strToLongDate(crs.getString("enquiry_status_date")));
						map.put("enquiry_status_desc", crs.getString("enquiry_status_desc"));
						map.put("enquiry_priorityenquiry_id", crs.getString("enquiry_priorityenquiry_id"));
						map.put("enquiry_age_id", crs.getString("enquiry_age_id"));
						map.put("enquiry_occ_id", crs.getString("enquiry_occ_id"));
						map.put("enquiry_custtype_id", crs.getString("enquiry_custtype_id"));
						map.put("enquiry_custid", crs.getString("enquiry_custid"));
						map.put("enquiry_fuelallowance", crs.getString("enquiry_fuelallowance"));
						map.put("enquiry_add_model_id", crs.getString("enquiry_add_model_id"));
						map.put("enquiry_variantcolour_id", crs.getString("enquiry_option_id"));
						map.put("enquiry_monthkms_id", crs.getString("enquiry_monthkms_id"));
						map.put("enquiry_purchasemode_id", crs.getString("enquiry_purchasemode_id"));
						map.put("enquiry_income_id", crs.getString("enquiry_income_id"));
						map.put("enquiry_familymember_count", crs.getString("enquiry_familymember_count"));
						map.put("enquiry_expectation_id", crs.getString("enquiry_expectation_id"));
						map.put("enquiry_othercar", crs.getString("enquiry_othercar"));
						map.put("enquiry_buyertype_id", crs.getString("enquiry_buyertype_id"));
						map.put("enquiry_ownership_id", crs.getString("enquiry_ownership_id"));
						map.put("enquiry_existingvehicle", crs.getString("enquiry_existingvehicle"));
						map.put("enquiry_purchasemonth", strToShortDate(crs.getString("enquiry_purchasemonth")));
						map.put("enquiry_loancompletionmonth", crs.getString("enquiry_loancompletionmonth"));
						map.put("enquiry_currentemi", crs.getString("enquiry_currentemi"));
						map.put("enquiry_loanfinancer", crs.getString("enquiry_loanfinancer"));
						map.put("enquiry_kms", crs.getString("enquiry_kms"));
						map.put("enquiry_expectedprice", crs.getString("enquiry_expectedprice"));
						map.put("enquiry_quotedprice", crs.getString("enquiry_quotedprice"));
						map.put("enquiry_evaluation", crs.getString("enquiry_evaluation"));
						map.put("enquiry_notes", crs.getString("enquiry_notes").replace("\"", ""));
						map.put("customer_name", crs.getString("customer_name"));
						map.put("contact_title_id", crs.getString("contact_title_id"));
						map.put("title_desc", crs.getString("title_desc"));
						map.put("contact_fname", crs.getString("contact_fname"));
						map.put("contact_lname", crs.getString("contact_lname"));
						map.put("contact_mobile1", crs.getString("contact_mobile1"));
						map.put("contact_mobile2", crs.getString("contact_mobile2"));
						map.put("contact_email1", crs.getString("contact_email1"));
						map.put("contact_email2", crs.getString("contact_email2"));
						map.put("contact_phone1", crs.getString("contact_phone1"));
						map.put("contact_phone2", crs.getString("contact_phone2"));
						map.put("contact_address", crs.getString("contact_address"));
						map.put("contact_pin", crs.getString("contact_pin"));
						contact_city_id = crs.getString("contact_city_id");
						city_name = ExecuteQuery("select CONCAT(city_name, '-', state_name) as cityname from " + compdb(comp_id) + "axela_city"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_city_id = city_id"
								+ " where  contact_city_id =" + contact_city_id);
						map.put("city_name", city_name);
						map.put("soe_name", crs.getString("soe_name"));
						map.put("enquiry_entry_id", crs.getString("enquiry_entry_id"));
						map.put("enquiry_entry_by", crs.getString("entryby"));
						map.put("enquiry_entry_date", strToLongDate(crs.getString("enquiry_entry_date")));
						map.put("enquiry_modified_id", crs.getString("enquiry_modified_id"));
						map.put("enquiry_modified_by", crs.getString("modifiedby"));
						map.put("enquiry_modified_date", strToLongDate(crs.getString("enquiry_modified_date")));
					}
					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("populatefields", list);
				list.clear();
			} else {
				output.put("msg", "No Enquiry Found!");
			}
			crs.close();
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
					+ " WHERE 1 = 1 "
					+ " AND model_active = '1'"
					+ " AND model_sales = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("StrSql---1---" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("model_id", "0");
				map.put("model_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", crs.getString("model_name"));
					list.add(gson.toJson(map)); // Converting String to Json
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateItem() {
		try {
			StrSql = "SELECT item_id, CONCAT(item_name, ' (', item_code, ')') AS item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1 = 1 AND item_type_id = 1 AND item_active = '1' AND item_model_id = " + item_model_id;
			// SOP("StrSql==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map)); // Converting String to Json
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
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateVariantColour() {
		try {
			StrSql = "Select option_id, option_name"
					+ " from " + compdb(comp_id) + "axela_vehstock_option"
					+ " order by option_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("variantcolour_id", "0");
				map.put("variantcolour_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("variantcolour_id", crs.getString("option_id"));
					map.put("variantcolour_name", unescapehtml(crs.getString("option_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("variantcolour_id", "0");
				map.put("variantcolour_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatevariantcolour", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateContactTitle() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			// SOP("StrSql=================" + StrSql);

			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("title_id", "0");
				map.put("title_desc", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("title_id", crs.getString("title_id"));
					map.put("title_desc", crs.getString("title_desc"));
					list.add(gson.toJson(map)); // Converting String to Json
				}

			} else {
				map.put("title_id", "0");
				map.put("title_desc", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatecontacttitle", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateAge() {
		try {
			StrSql = "Select age_id, age_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_age"
					+ " order by age_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("age_id", "0");
				map.put("age_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("age_id", crs.getString("age_id"));
					map.put("age_name", crs.getString("age_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("age_id", "0");
				map.put("age_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateage", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateOccupation() {
		try {
			StrSql = "Select occ_id, occ_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_occ"
					+ " order by occ_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("occ_id", "0");
				map.put("occ_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("occ_id", crs.getString("occ_id"));
					map.put("occ_name", crs.getString("occ_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("occ_id", "0");
				map.put("occ_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateoccupation", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateCustomerType() {
		try {
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

	public JSONObject PopulateStatus() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status"
					+ " WHERE 1 = 1"
					+ " ORDER BY status_id";
			// SOP("StrSql==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("status_id", crs.getString("status_id"));
					map.put("status_name", crs.getString("status_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("status_id", "0");
				map.put("status_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatestatus", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateOpprPriority() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT priorityenquiry_id, priorityenquiry_desc, priorityenquiry_duehrs"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
					+ " WHERE 1 = 1"
					+ " ORDER BY priorityenquiry_rank";
			// SOP("StrSql==" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("priorityenquiry_id", "0");
				map.put("priorityenquiry_desc", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("priorityenquiry_id", crs.getString("priorityenquiry_id"));
					map.put("priorityenquiry_desc", crs.getString("priorityenquiry_desc") + " ("
							+ crs.getString("priorityenquiry_duehrs") + " Hrs)");
					list.add(gson.toJson(map)); // Converting String to Json
				}
			} else {
				map.put("priorityenquiry_id", "0");
				map.put("priorityenquiry_desc", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateopprpriority", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateMonthKms() {
		try {
			StrSql = "Select monthkms_id, monthkms_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms"
					+ " order by monthkms_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("monthkms_id", "0");
				map.put("monthkms_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("monthkms_id", crs.getString("monthkms_id"));
					map.put("monthkms_name", unescapehtml(crs.getString("monthkms_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("monthkms_id", "0");
				map.put("monthkms_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatemonthkms", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulatePurchaseMode() {
		try {
			StrSql = "Select purchasemode_id, purchasemode_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode"
					+ " order by purchasemode_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("purchasemode_id", "0");
				map.put("purchasemode_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("purchasemode_id", crs.getString("purchasemode_id"));
					map.put("purchasemode_name", unescapehtml(crs.getString("purchasemode_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("purchasemode_id", "0");
				map.put("purchasemode_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatepurchasemode", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateIncome() {
		try {
			StrSql = "Select income_id, income_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_income"
					+ " order by income_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("income_id", "0");
				map.put("income_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("income_id", crs.getString("income_id"));
					map.put("income_name", unescapehtml(crs.getString("income_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("income_id", "0");
				map.put("income_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateincome", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateExpectation() {
		try {
			StrSql = "Select expectation_id, expectation_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_expectation"
					+ " order by expectation_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("expectation_id", "0");
				map.put("expectation_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("expectation_id", crs.getString("expectation_id"));
					map.put("expectation_name", unescapehtml(crs.getString("expectation_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("expectation_id", "0");
				map.put("expectation_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateexpectation", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateBuyerType() {
		try {
			StrSql = "Select buyertype_id, buyertype_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
					+ " order by buyertype_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("buyertype_id", "0");
				map.put("buyertype_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("buyertype_id", crs.getString("buyertype_id"));
					map.put("buyertype_name", unescapehtml(crs.getString("buyertype_name")));
					list.add(gson.toJson(map));
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
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateOwnership() {
		try {
			StrSql = "Select ownership_id, ownership_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_add_ownership"
					+ " order by ownership_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("ownership_id", "0");
				map.put("ownership_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("ownership_id", crs.getString("ownership_id"));
					map.put("ownership_name", unescapehtml(crs.getString("ownership_name")));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("ownership_id", "0");
				map.put("ownership_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populateownership", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}

	public JSONObject PopulateExecutive() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_emp_id = emp_id"
					+ " WHERE emp_active = 1"
					+ " and emp_sales='1'"
					// + " and emp_id = " + emp_id
					+ " AND IF((select emp_all_exe from "
					+ compdb(comp_id)
					+ "axela_emp "
					+ " where emp_id="
					+ emp_id // + " AND emp_uuid='"+ emp_uuid + "'"
					+ ")=0, (emp_id in (SELECT empexe_id from "
					+ compdb(comp_id)
					+ "axela_emp_exe where empexe_emp_id ="
					+ emp_id
					+ ") OR emp_id ="
					+ emp_id
					+ "),emp_id > 0)"
					+ ExeAccess
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("StrSql------" + StrSql);
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

	// divya
	public JSONObject PopulateModelItem(JSONObject input) {
		CachedRowSet crs = null;
		try {
			if (!input.isNull("model_id")) {
				model_id = (String) input.get("model_id");
			}
			StrSql = "SELECT item_id, item_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item"
					+ " WHERE 1 = 1 AND item_type_id = 1"
					+ " AND item_model_id = " + model_id
					+ " order by item_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name", unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("populatemodelitem", list);
				list.clear();
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject UpdateFields(JSONObject input) {
		String history_oldvalue = "";
		String history_newvalue = "";
		String history_actiontype = "";
		try {
			if (!input.isNull("enquiry_tradeinmake_id")) {
				enquiry_tradeinmake_id = (String) input.get("enquiry_tradeinmake_id");
			}
			// if (!input.isNull("status_id")) {
			// status_id = (String) input.get("status_id");
			// }
			// if (!input.isNull("status_desc")) {
			// status_desc = (String) input.get("status_desc");
			// }
			// if (!input.isNull("lostcase1")) {
			// lostcase1 = (String) input.get("lostcase1");
			// }
			// if (!input.isNull("lostcase2")) {
			// lostcase2 = (String) input.get("lostcase2");
			// }
			// if (!input.isNull("lostcase3")) {
			// lostcase3 = (String) input.get("lostcase3");
			// }

			if (!input.isNull("name")) {
				name = PadQuotes((String) input.get("name"));
			}
			if (!input.isNull("value")) {
				value = PadQuotes((String) input.get("value"));
			}
			if (!input.isNull("name1")) {
				name1 = PadQuotes((String) input.get("name1"));
			}
			if (!input.isNull("value1")) {
				value1 = PadQuotes((String) input.get("value1"));
			}
			if (!enquiry_id.equals("0")) {
				StrSql = "SELECT enquiry_emp_id, enquiry_branch_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
						+ " WHERE 1 = 1 AND enquiry_id = " + enquiry_id
						// + " and emp_id = " + emp_id
						// + WSCheckBranchAccess(emp_id, branch_id, role_id)
						+ ExeAccess + ""
						+ " GROUP BY enquiry_id";
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					enquiry_emp_id = crs.getString("enquiry_emp_id");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
				}
				crs.close();
			} else {
				output.put("msg", "Update Permission Denied!");
			}
			if (!enquiry_emp_id.equals("0") || emp_id.equals("1")) {
				if (name.equals("txt_enquiry_close_date") && name1.equals("txt_enquiry_date")) {
					if (!value.equals("")) {
						if (!isValidDateFormatShort(value)) {
							output.put("msg", "Enter Valid Close Date!");
						} else if (!value1.equals("") && !value.equals("") && Long.parseLong(ConvertShortDateToStr(value1)) > Long.parseLong(ConvertShortDateToStr(value))) {
							output.put("msg", "Closing Date should be greater than the Start Date!");
						} else {
							history_oldvalue = strToShortDate(ExecuteQuery("SELECT enquiry_close_date FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id = " + enquiry_id + ""));
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_close_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_trigger = '0'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							history_newvalue = value;
							history_actiontype = "Close Date";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							msg = "Days Left: " + (int) (getDaysBetween(ToShortDate(kknow()), ConvertShortDateToStr(value)));
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Close Date!");
					}
				}
				if (name.equals("txt_enquiry_title")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT enquiry_title FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						history_actiontype = "Title";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_title = '" + value + "'"
								+ " WHERE enquiry_id= " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Title!");
					}
				}

				if (name.equals("txt_enquiry_value")) {
					if (!value.equals("")) {
						if (!isNumeric(value)) {
							output.put("msg", "Value should be Numeric Only!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT enquiry_value FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							history_actiontype = "Value";
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_value = " + value + ""
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " " + history_newvalue + ",";
							}
							StrSql += " " + history_oldvalue + ")";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Value!");
					}
				}

				if (name.equals("txt_enquiry_desc")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT enquiry_desc FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						history_actiontype = "Description";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_desc = '" + value + "'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Description!");
					}
				}
				if (name.equals("sp_enquiry_emp_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = enquiry_emp_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						SOP("value===" + value);
						history_newvalue = ExecuteQuery("SELECT emp_name FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + value + "");
						history_actiontype = "Executive";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry SET enquiry_emp_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Executive!");
					}
				}
				if (name.equals("sp_enquiry_model_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT model_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = ExecuteQuery("SELECT model_name FROM " + compdb(comp_id) + "axela_inventory_item_model"
								+ " WHERE model_id = " + value + "");
						history_actiontype = "Model";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_model_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Model!");
					}
				}
				if (name.equals("sp_enquiry_item_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT item_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = ExecuteQuery("SELECT item_name FROM " + compdb(comp_id) + "axela_inventory_item"
								+ " WHERE item_id = " + value + "");
						history_actiontype = "Variant";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_item_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Variant!");
					}
				}

				if (name.equals("sp_enquiry_add_model_id")) {
					// if (!value.equals("0")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select model_name from " + compdb(comp_id) + "axela_sales_enquiry "
							+ "inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = enquiry_add_model_id where enquiry_id=" + enquiry_id + "");

					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_add_model_id = " + value + ""
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_newvalue = ExecuteQuery("Select model_name from " + compdb(comp_id) + "axela_inventory_item_model where model_id=" + value + "");
					history_actiontype = "Additional Model";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " " + enquiry_id + ","
							+ " " + emp_id + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					output.put("msg", "Additional Model updated!");
				}

				if (name.equals("sp_enquiry_variantcolour_id")) {
					if (!value.equals("0")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select option_name from " + compdb(comp_id) + "axela_sales_enquiry "
								+ " inner join " + compdb(comp_id) + "axela_vehstock_option on option_id = enquiry_option_id "
								+ " where enquiry_id=" + enquiry_id + "");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_option_id = " + value + ""
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_newvalue = ExecuteQuery("Select option_name from " + compdb(comp_id) + "axela_vehstock_option where option_id=" + value + "");

						history_actiontype = "Colour";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " " + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Colour!");
					}
				}

				if (name.equals("sp_enquiry_custtype_id")) {
					if (!value.equals("")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select custtype_name from " + compdb(comp_id) + "axela_sales_enquiry inner join " + compdb(comp_id)
								+ "axela_sales_enquiry_custtype on custtype_id = enquiry_custtype_id where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_custtype_id = " + value + ""
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);

						history_newvalue = ExecuteQuery("Select custtype_name from " + compdb(comp_id) + "axela_sales_enquiry_custtype where custtype_id=" + value + " ");

						history_actiontype = "Customer Type";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " " + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Customer Type!");
					}
				}

				if (name.equals("sp_enquiry_decisiontime_id")) {
					if (!value.equals("")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select decisiontime_name from " + compdb(comp_id) + "axela_sales_enquiry inner join " + compdb(comp_id)
								+ "axela_sales_enquiry_decisiontime on decisiontime_id = enquiry_decisiontime_id where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_decisiontime_id = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);

						history_newvalue = ExecuteQuery("Select decisiontime_name from " + compdb(comp_id) + "axela_sales_enquiry_decisiontime where decisiontime_id=" + value + " ");

						history_actiontype = "Decision Time";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Decision Time!");
					}
				}

				// start understanding customer n exchange offer
				if (name.equals("sp_enquiry_monthkms_id")) {
					if (!value.equals("0")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select monthkms_name from " + compdb(comp_id) + "axela_sales_enquiry "
								+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms on monthkms_id = enquiry_monthkms_id "
								+ " where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_monthkms_id = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_newvalue = ExecuteQuery("Select monthkms_name from " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms where monthkms_id=" + value + " ");

						history_actiontype = "Month Kms";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Month Kms!");
					}
				}

				if (name.equals("sp_enquiry_purchasemode_id")) {
					if (!value.equals("0")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select purchasemode_name from " + compdb(comp_id) + "axela_sales_enquiry "
								+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode on purchasemode_id = enquiry_purchasemode_id "
								+ " where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_purchasemode_id = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_newvalue = ExecuteQuery("Select purchasemode_name from " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode where purchasemode_id=" + value + " ");

						history_actiontype = "Purchase Mode";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Purchase Mode!");
					}
				}

				if (name.equals("sp_enquiry_income_id")) {
					if (!value.equals("0")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select income_name from " + compdb(comp_id) + "axela_sales_enquiry "
								+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_add_income on income_id = enquiry_income_id "
								+ " where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_income_id = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_newvalue = ExecuteQuery("Select income_name from " + compdb(comp_id) + "axela_sales_enquiry_add_income where income_id=" + value + " ");

						history_actiontype = "Income";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Income!");
					}
				}

				if (name.equals("txt_enquiry_familymember_count")) {
					if (!value.equals("")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select enquiry_familymember_count from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_familymember_count = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_actiontype = "Family Members";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + value + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Family Members!");
					}
				}

				if (name.equals("sp_enquiry_expectation_id")) {
					if (!value.equals("0")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select expectation_name from " + compdb(comp_id) + "axela_sales_enquiry "
								+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_add_expectation on expectation_id = enquiry_expectation_id "
								+ " where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_expectation_id = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_newvalue = ExecuteQuery("Select expectation_name from " + compdb(comp_id) + "axela_sales_enquiry_add_expectation where expectation_id=" + value + " ");

						history_actiontype = "Expectation";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Expectation!");
					}
				}

				if (name.equals("txt_enquiry_othercar")) {
					if (!value.equals("")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select enquiry_othercar from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_othercar = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_actiontype = "Other Car";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + value + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Other Car!");
					}
				}

				if (name.equals("sp_enquiry_buyertype_id")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select buyertype_name from " + compdb(comp_id) + "axela_sales_enquiry "
							+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype on buyertype_id = enquiry_buyertype_id "
							+ " where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_buyertype_id = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_newvalue = ExecuteQuery("Select buyertype_name from " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype where buyertype_id=" + value + " ");

					history_actiontype = "Buyer Type";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Select Buyer Type!");
					// }
				}

				if (name.equals("sp_enquiry_ownership_id")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select ownership_name from " + compdb(comp_id) + "axela_sales_enquiry "
							+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_add_ownership on ownership_id = enquiry_ownership_id "
							+ " where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_ownership_id = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_newvalue = ExecuteQuery("Select ownership_name from " + compdb(comp_id) + "axela_sales_enquiry_add_ownership where ownership_id=" + value + " ");

					history_actiontype = "Ownership";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Select Ownership!");
					// }
				}

				if (name.equals("txt_enquiry_existingvehicle")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_existingvehicle from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_existingvehicle = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Existing Vehicle";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Existing Vehicle!");
					// }
				}

				if (name.equals("txt_enquiry_purchasemonth")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					// SOP("vlaue----------"+value);
					history_oldvalue = ExecuteQuery("Select enquiry_purchasemonth from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					if (!history_oldvalue.equals("")) {
						history_oldvalue = strToShortDate(history_oldvalue);
					}
					history_newvalue = value;
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_purchasemonth = '" + ConvertShortDateToStr(value) + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					// SOP("StrSql----------"+StrSql);
					updateQuery(StrSql);
					history_actiontype = "Purchase Month";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// }
					// else {
					// StrHTML = "Enter Purchased Month!";
					// }
				}

				if (name.equals("txt_enquiry_loancompletionmonth")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_loancompletionmonth from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_loancompletionmonth = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Loan Completion Month";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Loan Completion Month!");
					// }
				}

				if (name.equals("txt_enquiry_currentemi")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_currentemi from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_currentemi = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					// SOP("strsql emi========="+StrSql);
					history_actiontype = "Current EMI";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					// SOP("strsql emi====2222====="+StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Current EMI!");
					// }
				}

				if (name.equals("txt_enquiry_loanfinancer")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_loanfinancer from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_loanfinancer = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Loan Financer";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Loan Financer!");
					// }
				}

				if (name.equals("txt_enquiry_kms")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_kms from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_kms = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Kms";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Kms!");
					// }
				}

				if (name.equals("txt_enquiry_expectedprice")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_expectedprice from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_expectedprice = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Expected Price";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Expected Price!");
					// }
				}

				if (name.equals("txt_enquiry_quotedprice")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_quotedprice from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_quotedprice = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Quoted Price";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Quoted Price!");
					// }
				}

				if (name.equals("chk_enquiry_evaluation")) {
					// if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("Select enquiry_evaluation from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_evaluation = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Evaluation";

					if (history_oldvalue.equals("1")) {
						history_oldvalue = "YES";
					} else {
						history_oldvalue = "NO";
					}

					if (value.equals("1")) {
						value = "YES";
					} else {
						value = "NO";
					}

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Quoted Price!");
					// }
				}
				// end understanding customer n exchange offer

				if (name.equals("sp_enquiry_tradeincolour_id")) {
					if (!make_id.equals("0")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select tradeincolour_name from " + compdb(comp_id) + "axela_sales_tradein_colour "
								+ " inner join " + compdb(comp_id) + "axela_sales_enquiry on enquiry_tradeincolour_id = tradeincolour_id "
								+ " where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_tradeincolour_id = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_newvalue = ExecuteQuery("Select tradeincolour_name from " + compdb(comp_id) + "axela_sales_tradein_colour where tradeincolour_id=" + value + " ");

						history_actiontype = "Colour";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Make!");
					}
				}

				if (name.equals("txt_enquiry_tradein_priceoffered")) {
					if (!make_id.equals("0")) {
						value = value.replaceAll("nbsp", "&");
						// SOP("value==" + value);
						history_oldvalue = ExecuteQuery("Select enquiry_tradein_priceoffered from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_tradein_priceoffered = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_actiontype = "Price Offered";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + value + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Make!");
					}
				}

				if (name.equals("sp_enquiry_age_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT age_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_age ON age_id = enquiry_age_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = ExecuteQuery("SELECT age_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_age WHERE age_id = " + value + "");
						history_actiontype = "Age";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_age_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Age!");
					}
				}

				if (name.equals("sp_enquiry_occ_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT occ_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ on occ_id = enquiry_occ_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = ExecuteQuery("SELECT occ_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_occ WHERE occ_id = " + value + "");
						history_actiontype = "Occupation";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry SET enquiry_occ_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Occupation!");
					}
				}

				if (name.equals("sp_enquiry_custtype_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT custtype_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_custtype on custtype_id = enquiry_custtype_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = ExecuteQuery("SELECT custtype_name FROM " + compdb(comp_id) + "axela_sales_enquiry_add_custtype WHERE custtype_id = " + value + "");
						history_actiontype = "Type Of Customer";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_custtype_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Type Of Customer!");
					}
				}

				if (name.equals("txt_enquiry_custid")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT enquiry_custid FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						history_actiontype = "ID";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_custid = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter ID!");
					}
				}

				if (name.equals("txt_enquiry_fuelallowance")) {
					if (!value.equals("")) {
						if (!isNumeric(value)) {
							output.put("msg", "Fuel Allowance should be Numeric Only!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT enquiry_fuelallowance FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							history_actiontype = "Fuel Allowance";
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_fuelallowance = " + value + ""
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " " + history_newvalue + ",";
							}
							StrSql += " " + history_oldvalue + ")";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Fuel Allowance!");
					}
				}

				if (name.equals("txt_customer_name")) {
					if (!value.equals("")) {
						if (value.length() < 3) {
							output.put("msg", "Enter atleast 3 Characters for Customer Name!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT COALESCE(customer_name, '') AS customer_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
									+ " WHERE enquiry_id = " + enquiry_id);
							history_newvalue = value;
							history_actiontype = "Customer";
							StrSql = "Update " + compdb(comp_id) + "axela_customer"
									+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_customer_id = customer_id"
									+ " SET"
									+ " customer_name = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Customer!");
					}
				}
				if (name.equals("sp_contact_title_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT title_desc"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
								+ " SET"
								+ " contact_title_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);

						history_newvalue = ExecuteQuery("SELECT title_desc"
								+ " FROM " + compdb(comp_id) + "axela_title WHERE title_id = " + value + "");

						history_actiontype = "Contact Title";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Title!");
					}
				}

				if (name.equals("txt_contact_fname")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT contact_fname"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " SET"
								+ " contact_fname = '" + value + "'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);

						history_actiontype = "Contact First Name";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Contact First Name!");
					}
				}

				if (name.equals("txt_contact_lname")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT contact_lname"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " SET"
								+ " contact_lname = '" + value + "'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);

						history_actiontype = "Contact Last Name";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Contact Last Name!");
					}
				}

				if (name.equals("txt_contact_mobile1")) {
					if (!value.equals("")) {
						StrSql = "SELECT contact_mobile1"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " WHERE enquiry_status_id = 1 AND contact_mobile1 = '" + value + "' AND enquiry_branch_id = " + enquiry_branch_id
								+ " AND enquiry_id != " + enquiry_id;
						if (!IsValidMobileNo(value)) {
							output.put("msg", "Enter Valid Mobile1!");
						} else if (!ExecuteQuery(StrSql).equals("")) {
							output.put("msg", "Similar Mobile1 Found!!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT contact_mobile1"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
									+ " SET"
									+ " contact_mobile1 = '" + value + "',"
									+ " customer_mobile1 = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							history_actiontype = "Mobile 1";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Mobile1!");
					}
				}
				if (name.equals("txt_contact_mobile2")) {
					if (!value.equals("")) {
						StrSql = "SELECT contact_mobile2"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " WHERE enquiry_status_id = 1 AND contact_mobile2 = '" + value + "' AND enquiry_branch_id = " + enquiry_branch_id
								+ " AND enquiry_id != " + enquiry_id;
						if (!IsValidMobileNo(value)) {
							output.put("msg", "Enter Valid Mobile 2!");
						} else if (!ExecuteQuery(StrSql).equals("")) {
							output.put("msg", "Similar Mobile 2 Found!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT contact_mobile2"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
									+ " SET"
									+ " contact_mobile2 = '" + value + "',"
									+ " customer_mobile2 = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							history_actiontype = "Mobile 2";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Mobile 2!");
					}
				}
				if (name.equals("txt_contact_phone1")) {
					if (!value.equals("")) {
						StrSql = "SELECT contact_phone1"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " WHERE enquiry_status_id = 1 AND contact_phone1 = '" + value + "' AND enquiry_branch_id = " + enquiry_branch_id
								+ " AND enquiry_id != " + enquiry_id;
						if (!IsValidPhoneNo(value)) {
							output.put("msg", "Enter Valid Phone1!");
						} else if (!ExecuteQuery(StrSql).equals("")) {
							output.put("msg", "Similar Phone1 Found!!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT contact_phone1"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
									+ " SET"
									+ " contact_phone1 = '" + value + "',"
									+ " customer_phone1 = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							history_actiontype = "Phone 1";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Phone1!");
					}
				}
				if (name.equals("txt_contact_phone2")) {
					if (!value.equals("")) {
						StrSql = "SELECT contact_phone2"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " WHERE enquiry_status_id = 1 AND contact_phone2 = '" + value + "' AND enquiry_branch_id = " + enquiry_branch_id
								+ " AND enquiry_id != " + enquiry_id;
						if (!IsValidPhoneNo(value)) {
							output.put("msg", "Enter Valid Phone 2!");
						} else if (!ExecuteQuery(StrSql).equals("")) {
							output.put("msg", "Similar Phone 2 Found!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT contact_phone2"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
									+ " SET"
									+ " contact_phone2 = '" + value + "',"
									+ " customer_phone2 = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							history_actiontype = "Phone 2";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Phone 2!");
					}
				}
				if (name.equals("txt_contact_email1")) {
					if (!value.equals("")) {
						StrSql = "SELECT contact_email1"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " WHERE enquiry_status_id = 1 AND contact_email1 = '" + value + "' AND enquiry_branch_id = " + enquiry_branch_id
								+ " AND enquiry_id != " + enquiry_id;
						if (!IsValidEmail(value)) {
							output.put("msg", "Enter Valid Contact Email1!");
						} else if (!ExecuteQuery(StrSql).equals("")) {
							output.put("msg", "Similar Contact Email 1 Found!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT contact_email1"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
									+ " SET"
									+ " contact_email1 = '" + value + "',"
									+ " customer_email1 = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Email1";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Contact Email 1!");
					}
				}
				if (name.equals("txt_contact_email2")) {
					if (!value.equals("")) {
						StrSql = "SELECT contact_email2"
								+ " FROM " + compdb(comp_id) + "axela_customer_contact"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
								+ " WHERE enquiry_status_id = 1 AND contact_email2 = '" + value + "' AND enquiry_branch_id = " + enquiry_branch_id
								+ " AND enquiry_id != " + enquiry_id;
						if (!IsValidEmail(value)) {
							output.put("msg", "Enter Valid Contact Email 2!");
						} else if (!ExecuteQuery(StrSql).equals("")) {
							output.put("msg", "Similar Contact Email 2 Found!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT contact_email2"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
									+ " SET"
									+ " contact_email2 = '" + value + "',"
									+ " customer_email2 = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Email 2";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Contact Email 2!");
					}
				}

				if (name.equals("txt_contact_address")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT contact_address"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
								+ " SET"
								+ " contact_address = '" + value + "',"
								+ " customer_address = '" + value + "'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);

						history_actiontype = "Contact Address";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Contact Address!");
					}
				}

				if (name.equals("sp_city_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT COALESCE(CONCAT(city_name, ' - ', state_name), '') as city_name"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = enquiry_customer_id"
								+ " SET"
								+ " contact_city_id = " + value + ","
								+ " customer_city_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_newvalue = ExecuteQuery("SELECT COALESCE(CONCAT(city_name, ' - ', state_name), '') as city_name"
								+ " FROM " + compdb(comp_id) + "axela_city"
								+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
								+ " WHERE city_id = " + value + "");

						history_actiontype = "City";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select City!");
					}
				}

				if (name.equals("txt_contact_pin")) {
					if (!value.equals("")) {
						if (!isNumeric(value)) {
							output.put("msg", "Contact Pin should be Numeric Only!");
						} else {
							history_oldvalue = ExecuteQuery("SELECT contact_pin"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " WHERE enquiry_id = " + enquiry_id + "");
							history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
									+ " SET"
									+ " contact_pin = '" + value + "',"
									+ " customer_pin = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Pin";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_newvalue,"
									+ " history_oldvalue)"
									+ " VALUES"
									+ " (" + enquiry_id + ","
									+ " " + emp_id + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',";
							if (!value.equals("")) {
								StrSql += " '" + history_newvalue + "',";
							}
							StrSql += " '" + history_oldvalue + "')";
							updateQuery(StrSql);
							output.put("msg", history_actiontype + " Updated Successfully!");
						}
					} else {
						output.put("msg", "Enter Contact Pin!");
					}
				}

				if (name.equals("sp_enquiry_status_id")) {
					// if (name.equals("sp_enquiry_status_id") && name1.equals("txt_enquiry_status_desc")) {
					// if (value1.equals("")) {
					// msg = msg + "Enter Status Comments!";
					// output.put("msg", msg);
					// } else {
					if (value.equals("1") || value.equals("2")) {
						history_oldvalue = ExecuteQuery("SELECT status_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");

						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_status_id = " + value + ","
								+ " enquiry_status_date = '" + ToLongDate(kknow()) + "'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);

						history_newvalue = ExecuteQuery("SELECT status_name FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status on status_id = enquiry_status_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_actiontype = "Status";
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						msg = history_actiontype + " Updated Successfully!";
						output.put("msg", msg);

					}
					// }
				}

				if (name.equals("txt_enquiry_status_desc")) {
					value = value.replaceAll("nbsp", "&");
					// if (!value.equals("")) {
					history_oldvalue = ExecuteQuery("Select enquiry_status_desc"
							+ " from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");

					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_status_desc = '" + value + "'"
							+ " where enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Status Comments";

					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					output.put("msg", history_actiontype + " Updated Successfully!");
					// } else {
					// output.put("msg", "Enter Status Description!");
					// }
				}

				if (name.equals("sp_enquiry_priorityenquiry_id")) {
					if (!value.equals("0")) {
						history_oldvalue = ExecuteQuery("SELECT priorityenquiry_name"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
								+ " WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = ExecuteQuery("SELECT priorityenquiry_name FROM " + compdb(comp_id) + "axela_sales_enquiry_priority WHERE priorityenquiry_id = " + value + "");
						history_actiontype = "Priority";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_priorityenquiry_id = " + value + ""
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Select Priority!");
					}
				}

				if (name.equals("txt_enquiry_qcsno")) {
					String update = "";
					StrSql = "select enquiry_qcsno"
							+ " from " + compdb(comp_id) + "axela_sales_enquiry"
							+ " where 1=1 "
							+ " and enquiry_qcsno = '" + value + "'";
					if (!ExecuteQuery(StrSql).equals("")) {
						output.put("msg", "Similar QCS No. found!");
						update = "no";
					}
					if (!update.equals("no")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("Select enquiry_qcsno from " + compdb(comp_id) + "axela_sales_enquiry where enquiry_id=" + enquiry_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_qcsno = '" + value + "'"
								+ " where enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						history_actiontype = "QCS NO.";

						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values ("
								+ " '" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + value + "')";
						updateQuery(StrSql);
						output.put("msg", "QCS No. updated!");
					}
					// } else {
					// StrHTML = "Enter QCS No. !";
					// }
				}

				if (name.equals("txt_enquiry_dmsno")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT enquiry_dmsno FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						history_actiontype = "DMS No.";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_dmsno = '" + value + "'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter DMS No.!");
					}
				}
				if (name.equals("txt_enquiry_notes")) {
					if (!value.equals("")) {
						history_oldvalue = ExecuteQuery("SELECT enquiry_notes FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id = " + enquiry_id + "");
						history_newvalue = value;
						history_actiontype = "Notes";
						StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
								+ " SET"
								+ " enquiry_notes = '" + value + "'"
								+ " WHERE enquiry_id = " + enquiry_id + "";
						updateQuery(StrSql);
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_newvalue,"
								+ " history_oldvalue)"
								+ " VALUES"
								+ " (" + enquiry_id + ","
								+ " " + emp_id + ","
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',";
						if (!value.equals("")) {
							StrSql += " '" + history_newvalue + "',";
						}
						StrSql += " '" + history_oldvalue + "')";
						updateQuery(StrSql);
						output.put("msg", history_actiontype + " Updated Successfully!");
					} else {
						output.put("msg", "Enter Notes!");
					}
				}
			} else {
				output.put("msg", "Update Permission Denied!");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
