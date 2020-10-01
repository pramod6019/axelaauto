/* Ved Prakash (17th May 2013) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_DashFollowUpData extends ConnectWS {

	public String update = "";
	public String delete = "";
	public String submitB = "";
	public String StrSql = "";
	public String ExeAccess = "";
	public String msg = "";
	public String emp_id = "0";
	public String emp_uuid = "0";
	public String branch_brand_id = "0";
	public String comp_id = "0";
	public String enquiry_id = "";
	public String enquiry_status_id = "";
	public String followup = "";
	public String followup_id = "0";
	public String followup_desc = "";
	public String followup_emp_id = "0";
	public String followupdesc_id = "0";
	public String followupdesc_name = "";
	public String followup_followuptype_id = "";
	public String followup_followup_time = "";
	public String followuptime = "";
	public String followup_time = "";
	public String followup_entry_id = "0";
	public String followup_entry_time = "";
	public String role_id = "";
	public String branch_id = "";
	// public String enquiry_decisiontime_id = "0";
	public String enquiry_custtype_id = "0";
	public String enquiry_tradeinmake_id = "0";
	public String enquiry_tradeinmodel_id = "0";
	public String enquiry_tradein_year = "";
	public String enquiry_tradein_fueltype_id = "0";
	public String enquiry_variantcolour_id = "0";
	// ws
	Gson gson = new Gson();
	// JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONObject output = new JSONObject();
	public String status = "";

	public JSONObject Enquiry_Dash_FollowUp(JSONObject input) {
		if (AppRun().equals("0")) {
			SOPError("DD-Motors Enquiry_Dash_FollowUp input = " + input);
		}
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
				if (!emp_id.equals("0")) {
					if (!input.isNull("enquiry_id")) {
						enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
					}
					if (!input.isNull("comp_id")) {
						comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
					}
					if (!input.isNull("emp_uuid")) {
						emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
					}
					if (!input.isNull("followup_id")) {
						followup_id = CNumeric(PadQuotes((String) input.get("followup_id")));
					}
					if (!input.isNull("delete")) {
						delete = PadQuotes((String) input.get("delete"));
					}
					if (!input.isNull("submitB")) {
						submitB = PadQuotes((String) input.get("submitB"));
					}
					// ExeAccess = WSCheckExeAccess(emp_id);
					if (!enquiry_id.equals("0") && !delete.equals("yes")) {
						String StrSql1 = "SELECT emp_branch_id, emp_role_id"
								+ " FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_id = " + emp_id;
						CachedRowSet crs1 = processQuery(StrSql1, 0);
						while (crs1.next()) {
							role_id = crs1.getString("emp_role_id");
							branch_id = crs1.getString("emp_branch_id");
						}
						crs1.close();
						getOpportunityDetails();
						ListFollowup();
						PopulateFollowupType();
						PopulateDescription();
						if (submitB.equals("yes")) {
							GetValues(input);
							followup_entry_id = emp_id;
							followup_entry_time = ToLongDate(kknow());

							AddFields();
						}
					}
					if (delete.equals("yes") && !followup_id.equals("0") && emp_id.equals("1")) {
						DeleteFields();
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		if (AppRun().equals("0")) {
			SOPError("DD-Motors Enquiry_Dash_FollowUp output = " + output);
		}
		return output;
	}

	public JSONObject getOpportunityDetails() {

		StrSql = "SELECT enquiry_emp_id, enquiry_status_id, enquiry_title, enquiry_option_id,"
				+ " enquiry_custtype_id, enquiry_tradein_year, enquiry_tradeinmake_id,"
				+ " enquiry_tradeinmodel_id, enquiry_tradein_fueltype_id, branch_brand_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
				+ " WHERE 1 = 1 "
				+ " AND IF((select emp_all_exe from " + compdb(comp_id) + "axela_emp where emp_id="
				+ emp_id
				+ ")=0, (enquiry_emp_id in (SELECT empexe_id from " + compdb(comp_id) + "axela_emp_exe where empexe_emp_id ="
				+ emp_id + ") OR enquiry_emp_id =" + emp_id
				+ "),enquiry_id > 0) AND enquiry_enquirytype_id = 1"
				+ " AND enquiry_id = " + enquiry_id + ""
				+ " GROUP BY enquiry_id"
				+ " ORDER BY enquiry_id DESC";
		// SOPError("StrSql=======getOpportunityDetails======" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// enquiry_decisiontime_id = crs.getString("enquiry_decisiontime_id");
					enquiry_custtype_id = crs.getString("enquiry_custtype_id");
					enquiry_tradeinmake_id = crs.getString("enquiry_tradeinmake_id");
					enquiry_tradeinmodel_id = crs.getString("enquiry_tradeinmodel_id");
					enquiry_tradein_year = crs.getString("enquiry_tradein_year");
					enquiry_tradein_fueltype_id = crs.getString("enquiry_tradein_fueltype_id");
					enquiry_variantcolour_id = crs.getString("enquiry_option_id");
					enquiry_status_id = crs.getString("enquiry_status_id");
					branch_brand_id = crs.getString("branch_brand_id");
					followup_emp_id = crs.getString("enquiry_emp_id");
					map.put("enquiry_title", crs.getString("enquiry_title"));
					map.put("enquiry_emp_id", crs.getString("enquiry_emp_id"));
					map.put("enquiry_status_id", crs.getString("enquiry_status_id"));
					list.add(gson.toJson(map));
				}
				map.clear();
				output.put("enquirydetails", list);
				list.clear();
			} else {
				output.put("msg", "\nInvalid Enquiry!");
			}
			crs.close();

			// ListCustomerDetails();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject ListFollowup() {
		try {
			StrSql = "SELECT followup_id, followup_enquiry_id, followup_followup_time,"
					+ " followup_desc, followup_entry_time, followup_entry_id,"
					+ " followuptype_name, CONCAT(emp_name, ' (',emp_ref_no, ')') AS entry_by"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_followup_type ON followuptype_id = followup_followuptype_id"
					+ " WHERE 1 = 1 AND followup_enquiry_id = " + enquiry_id
					+ " ORDER BY followup_followup_time";
			// SOPError("StrSql(listfollowup)---" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("followup_id", crs.getString("followup_id"));
					map.put("followup_enquiry_id", crs.getString("followup_enquiry_id"));
					map.put("followuptype_name", crs.getString("followuptype_name"));
					map.put("followup_desc", crs.getString("followup_desc").replace("\"", ""));
					map.put("followup_followup_time", strToLongDate(crs.getString("followup_followup_time")));
					map.put("followup_entry_id", crs.getString("followup_entry_id"));
					map.put("entry_by", crs.getString("entry_by"));
					map.put("followup_entry_time", strToLongDate(crs.getString("followup_entry_time")));
					list.add(gson.toJson(map));
					if (crs.getString("followup_desc").equals("")) {
						status = "Update";
					}
				}
				map.clear();
				output.put("listfollowup", list);
				list.clear();
			} else {
				status = "Add";
			}
			if (status.equals("") && enquiry_status_id.equals("1")) {
				status = "Add";
			}
			output.put("status", status);
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject GetValues(JSONObject input) throws Exception {
		JSONObject output = new JSONObject();
		if (!input.isNull("followupdesc_id")) {
			followupdesc_id = CNumeric(PadQuotes((String) input.get("followupdesc_id")));
			// SOP("followupdesc_id = " + followupdesc_id);
		}
		if (!input.isNull("followup_desc")) {
			followup_desc = PadQuotes((String) input.get("followup_desc"));
			// SOP("followup_desc = " + followup_desc);
		}
		if (!input.isNull("followup_time")) {
			followup_followup_time = PadQuotes((String) input.get("followup_time"));
		}
		if (!input.isNull("followup_followuptype_id")) {
			followup_followuptype_id = CNumeric(PadQuotes((String) input.get("followup_followuptype_id")));
			// SOP("followup_followuptype_id------------" + followup_followuptype_id);
		}
		return output;
	}

	protected void CheckForm() {
		msg = "";

		if (followup_emp_id.equals("0")) {
			msg = msg + "<br>No Executive allocated!";
		}
		if (branch_brand_id.equals("1") || branch_brand_id.equals("2")) {
			if (enquiry_variantcolour_id.equals("0")) {
				msg = msg + "<br>Colour is not Selected!";
			}
			StrSql = " select enquiry_id "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " where (enquiry_age_id=0 or enquiry_occ_id=0 or enquiry_custtype_id=0 or enquiry_monthkms_id=0 or enquiry_purchasemode_id=0 or "
					+ " enquiry_income_id=0  or enquiry_expectation_id=0) and enquiry_id = " + enquiry_id; // // or enquiry_familymember_count=0
			// SOPError("StrSql-------" + StrSql);
			if (!ExecuteQuery(StrSql).equals("")) {
				msg = msg + "<br>Enter full customer information!";
			}

			StrSql = " select enquiry_id "
					+ " from " + compdb(comp_id) + "axela_sales_enquiry"
					+ " where enquiry_buyertype_id=3 and (enquiry_ownership_id=0 or enquiry_existingvehicle='' or enquiry_purchasemonth=''"
					// + " or enquiry_loancompletionmonth='' "
					// + " or enquiry_loanfinancer=''"
					+ " or enquiry_kms=0 or enquiry_expectedprice=0"
					// + " or enquiry_quotedprice=0"
					+ " ) and enquiry_id = " + enquiry_id; // //or enquiry_currentemi=0
			// SOP("StrSql-----" + StrSql);
			// SOP("exchange---------info----" + ExecuteQuery(StrSql));
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg = msg + "<br>Enter full exchange information!";
			// }
		}
		if (!followupdesc_id.equals("0")) {
			followupdesc_name = ExecuteQuery("select followupdesc_name from " + compdb(comp_id) + "axela_sales_enquiry_followup_desc where followupdesc_id=" + followupdesc_id);
		}

		if (status.equals("Update") && followup_desc.equals("") && followupdesc_id.equals("0")) {
			msg = msg + "<br>Select Description!";
		}
		// if (followup_desc.equals("")) {
		// msg = msg + "<br>Enter Description!";
		// }
		if (enquiry_status_id.equals("1")) {
			if (followup_followup_time.equals("")) {
				msg = msg + "<br>Select Follow-up Time!";
			} else {
				if (!isValidDateFormatLong(followup_followup_time)) {
					msg = msg + "<br>Enter Valid Follow-up Time!";
				}
			}

			followuptime = ExecuteQuery("select followup_followup_time from " + compdb(comp_id) + "axela_sales_enquiry_followup where followup_desc='' and followup_enquiry_id = " + enquiry_id);
			// SOP("followuptime--"+followuptime);
			if (!followup_followup_time.equals("") && isValidDateFormatLong(followup_followup_time) && !followuptime.equals("") && isValidDateFormatLong(strToLongDate(followuptime))) {

				if (Long.parseLong(ConvertLongDateToStr(followup_followup_time)) <= Long.parseLong(ToLongDate(kknow()))) {
					msg = msg + "<br>Follow-up time must be greater than " + strToLongDate(ToLongDate(kknow())) + "!";
				}
				// if (Long.parseLong(ConvertLongDateToStr(followup_followup_time)) < Long.parseLong(followuptime)) {
				// msg = msg + "<br>Follow-up time should be greater than previous follow-up time!";
				// }
			}

			if (followup_followuptype_id.equals("0")) {
				msg = msg + "<br>Select Follow-up Type!";
			}
		}
		if (msg.equals("") && (!followupdesc_id.equals("0") || !followup_desc.equals(""))) {
			if (!followupdesc_id.equals("0") && !followup_desc.equals("")) {
				followup_desc = followupdesc_name + "<br/>" + followup_desc;
			} else if (!followupdesc_id.equals("0") && followup_desc.equals("")) {
				followup_desc = followupdesc_name;
			}
		}
	}

	public JSONObject AddFields() throws Exception {
		CheckForm();
		if (msg.equals("")) {
			followup_followup_time = ConvertLongDateToStr(followup_followup_time);
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " SET"
						+ " followup_desc= '" + followup_desc + "',"
						+ " followup_modified_id = " + emp_id + ", "
						+ " followup_modified_time = '" + ToLongDate(kknow()) + "' "
						+ " WHERE followup_desc = '' AND followup_enquiry_id = " + enquiry_id;
				// SOPError("strsql---dashfollowUp----UPDATE----" + StrSql);
				updateQuery(StrSql);
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " (followup_enquiry_id,"
						+ " followup_emp_id,"
						+ " followup_followuptype_id,"
						+ " followup_followup_time,"
						+ " followup_desc,"
						+ " followup_entry_id,"
						+ " followup_entry_time,"
						+ " followup_trigger)"
						+ " VALUES"
						+ " (" + enquiry_id + ","
						+ " " + emp_id + ", "
						+ " " + followup_followuptype_id + ","
						+ " '" + followup_followup_time + "',"
						+ " '',"
						+ "" + followup_entry_id + ","
						+ " '" + followup_entry_time + "',"
						+ " 0)";

				// SOPError("StrSql--dashfollowUp--INSERT-------" + StrSql);
				if (updateQuery(StrSql) == 1) {
					ListFollowup();
					output.put("msg", "Follow-Up Added Successfully!");
				}
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		} else {
			output.put("msg", "Error!<br>" + msg);
		}
		return output;

	}

	public JSONObject DeleteFields() throws Exception {
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
						+ " WHERE followup_id = " + followup_id + "";
				updateQuery(StrSql);
				output.put("msg", "Followup Deleted Successfully!");
				// SOPError("DeleteFields----" + StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto ==" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		return output;
	}

	public JSONObject PopulateFollowupType() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT followuptype_id, followuptype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_type"
					+ " WHERE 1 = 1"
					+ " ORDER BY followuptype_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("followuptype_id", "0");
				map.put("followuptype_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("followuptype_id", crs.getString("followuptype_id"));
					map.put("followuptype_name", crs.getString("followuptype_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("followuptype_id", "0");
				map.put("followuptype_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatefollowuptype", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateDescription() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT followupdesc_id, followupdesc_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup_desc"
					+ " WHERE 1 = 1"
					+ " ORDER BY followupdesc_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("followupdesc_id", crs.getString("followupdesc_id"));
					map.put("followupdesc_name", crs.getString("followupdesc_name"));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populatefollowupdesc", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}
}
