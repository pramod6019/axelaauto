package axela.ws.runner;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Executive_Univ_Check;
import cloudify.connect.Connect;

public class WS_Ecover_Exe_Update extends Connect {

	public String comp_id = "";
	public String emp_id = "";
	public String add = "";
	public String update = "";
	public String emp_name = "";
	public String emp_role_id = "";
	public String emp_notes = "";
	public String emp_active = "";
	public String emp_uname = "";
	public String emp_ref_no = "";
	public String emp_sex = "";
	public String emp_dob = "";
	public String emp_married = "";
	public String emp_qualification = "";
	public String emp_certification = "";
	public String emp_phone1 = "";
	public String emp_phone2 = "";
	public String emp_mobile1 = "";
	public String emp_mobile2 = "";
	public String emp_email1 = "";
	public String emp_email2 = "";
	public String emp_upass = "";
	public String emp_address = "";
	public String emp_city = "";
	public String emp_pin = "";
	public String emp_state = "";
	public String emp_landmark = "";
	public String emp_weeklyoff_id = "";
	public String emp_prevexp_month = "";
	public String emp_date_of_join = "";
	public String emp_date_of_relieve = "";
	public String emp_reason_of_leaving = "";
	public String emp_theme_id = "";
	public String emp_entry_id = "";
	public String emp_entry_date = "";
	public String emp_modified_id = "";
	public String emp_modified_date = "";
	public String Strsql = "";
	public String emp_prevexp = "";
	public String drop_month = "";
	public String drop_day = "";
	public String drop_year = "";
	public String emp_uuid = "";
	public String emp_branch_id = "";

	public String ExeUpdate(JSONObject input, HttpServletRequest request) {
		try {
			if (AppRun().equals("0")) {
				// SOP("input==WS_Exe_update===" + input.toString());
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_id")) {
				emp_id = PadQuotes((String) input.get("emp_id"));
			}
			if (!input.isNull("add")) {
				add = PadQuotes((String) input.get("add"));
			}
			if (!input.isNull("update")) {
				update = PadQuotes((String) input.get("update"));
			}
			// SOP("update=======" + update);
			if (!comp_id.equals("0")) {

				if (update.equals("yes")) {
					Strsql = "SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
							+ " WHERE emp_id= " + emp_id;
					// SOP("Strsql" + Strsql);
					if (!CNumeric(ExecuteQuery(Strsql)).equals("0")) {
						GetValues(input);
						UpdateFields(request);
						new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
					} else {
						GetValues(input);
						AddFields(request);
						new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
					}
				}
				if (add.equals("yes")) {
					GetValues(input);
					AddFields(request);
					new Executive_Univ_Check().UpdateUniversalEmp(emp_id, comp_id);
				}
			}
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
		return "";
	}

	public void GetValues(JSONObject input) {
		try {
			if (!input.isNull("emp_id")) {
				emp_id = CNumeric((String) input.get("emp_id"));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = PadQuotes((String) input.get("emp_uuid"));
			}
			if (!input.isNull("emp_name")) {
				emp_name = PadQuotes((String) input.get("emp_name"));
			}
			if (!input.isNull("emp_role_id")) {
				emp_role_id = CNumeric((String) input.get("emp_role_id"));
			}
			if (!input.isNull("emp_notes")) {
				emp_notes = PadQuotes((String) input.get("emp_notes"));
			}
			if (!input.isNull("emp_branch_id")) {
				emp_branch_id = PadQuotes((String) input.get("emp_branch_id"));
			}
			if (!input.isNull("emp_active")) {
				emp_active = PadQuotes((String) input.get("emp_active"));
			}
			if (!input.isNull("emp_uname")) {
				emp_uname = PadQuotes((String) input.get("emp_uname"));
			}
			if (!input.isNull("emp_role_id")) {
				emp_role_id = CNumeric((String) input.get("emp_role_id"));
			}
			if (!input.isNull("emp_prevexp")) {
				emp_prevexp = PadQuotes((String) input.get("emp_prevexp"));
			}
			if (!input.isNull("emp_ref_no")) {
				emp_ref_no = PadQuotes((String) input.get("emp_ref_no"));
			}
			if (!input.isNull("emp_sex")) {
				emp_sex = PadQuotes((String) input.get("emp_sex"));
			}
			if (!input.isNull("emp_dob")) {
				emp_dob = PadQuotes((String) input.get("emp_dob"));
			}
			if (!input.isNull("drop_month")) {
				drop_month = PadQuotes((String) input.get("drop_month"));
			}
			if (!input.isNull("drop_day")) {
				drop_day = PadQuotes((String) input.get("drop_day"));
			}
			if (!input.isNull("drop_year")) {
				drop_year = PadQuotes((String) input.get("drop_year"));
			}

			if (!input.isNull("emp_married")) {
				emp_married = PadQuotes((String) input.get("emp_married"));
			}
			if (!input.isNull("emp_qualification")) {
				emp_qualification = PadQuotes((String) input.get("emp_qualification"));
			}
			if (!input.isNull("emp_certification")) {
				emp_certification = PadQuotes((String) input.get("emp_certification"));
			}
			if (!input.isNull("emp_phone1")) {
				emp_phone1 = PadQuotes((String) input.get("emp_phone1"));
			}
			if (!input.isNull("emp_phone2")) {
				emp_phone2 = PadQuotes((String) input.get("emp_phone2"));
			}
			if (!input.isNull("emp_mobile1")) {
				emp_mobile1 = PadQuotes((String) input.get("emp_mobile1"));
			}
			if (!input.isNull("emp_mobile2")) {
				emp_mobile2 = PadQuotes((String) input.get("emp_mobile2"));
			}
			if (!input.isNull("emp_email1")) {
				emp_email1 = PadQuotes((String) input.get("emp_email1"));
			}
			if (!input.isNull("emp_email2")) {
				emp_email2 = PadQuotes((String) input.get("emp_email2"));
			}
			if (!input.isNull("emp_upass")) {
				emp_upass = PadQuotes((String) input.get("emp_upass"));
			}
			if (!input.isNull("emp_address")) {
				emp_address = PadQuotes((String) input.get("emp_address"));
			}
			if (!input.isNull("emp_city")) {
				emp_city = PadQuotes((String) input.get("emp_city"));
			}
			if (!input.isNull("emp_pin")) {
				emp_pin = PadQuotes((String) input.get("emp_pin"));
			}
			if (!input.isNull("emp_state")) {
				emp_state = PadQuotes((String) input.get("emp_state"));
			}
			if (!input.isNull("emp_landmark")) {
				emp_landmark = PadQuotes((String) input.get("emp_landmark"));
			}
			if (!input.isNull("emp_active")) {
				emp_active = PadQuotes((String) input.get("emp_active"));
			}
			if (!input.isNull("emp_weeklyoff_id")) {
				emp_weeklyoff_id = CNumeric((String) input.get("emp_weeklyoff_id"));
			}
			if (!input.isNull("emp_prevexp_month")) {
				emp_prevexp_month = PadQuotes((String) input.get("emp_prevexp_month"));
			}
			if (!input.isNull("emp_date_of_join")) {
				emp_date_of_join = PadQuotes((String) input.get("emp_date_of_join"));
			}
			if (!input.isNull("emp_date_of_relieve")) {
				emp_date_of_relieve = PadQuotes((String) input.get("emp_date_of_relieve"));
			}
			if (!input.isNull("emp_reason_of_leaving")) {
				emp_reason_of_leaving = PadQuotes((String) input.get("emp_reason_of_leaving"));
			}
			if (!input.isNull("emp_notes")) {
				emp_notes = PadQuotes((String) input.get("emp_notes"));
			}
			if (!input.isNull("emp_theme_id")) {
				emp_theme_id = CNumeric((String) input.get("emp_theme_id"));
			}
			if (!input.isNull("emp_entry_id")) {
				emp_entry_id = CNumeric((String) input.get("emp_entry_id"));
			}
			if (!input.isNull("emp_entry_date")) {
				emp_entry_date = PadQuotes((String) input.get("emp_entry_date"));
			}
			if (!input.isNull("emp_modified_id")) {
				emp_modified_id = CNumeric((String) input.get("emp_modified_id"));
			}
			if (!input.isNull("emp_modified_date")) {
				emp_modified_date = CNumeric((String) input.get("emp_modified_date"));
			}
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void AddFields(HttpServletRequest request) {
		try {
			SOP("emp_upass=======" + emp_upass);
			Strsql = "INSERT INTO " + compdb(comp_id) + "axela_emp"
					+ " (emp_id,"
					+ " emp_uuid,"
					+ " emp_name,"
					+ " emp_photo,"
					+ " emp_uname,"
					+ " emp_role_id,"
					+ " emp_department_id,"
					+ " emp_ref_no,"
					+ " emp_jobtitle_id,"
					+ " emp_sex,"
					+ " emp_dob,"
					+ " emp_married,"
					+ " emp_qualification,"
					+ " emp_certification,"
					+ " emp_phone1,"
					+ " emp_phone2,"
					+ " emp_mobile1,"
					+ " emp_mobile2,"
					+ " emp_email1,"
					+ " emp_email2,"
					+ " emp_upass,"
					+ " emp_address,"
					+ " emp_city,"
					+ " emp_pin,"
					+ " emp_state,"
					+ " emp_landmark,"
					+ " emp_active,"
					+ " emp_branch_id,"
					+ " emp_weeklyoff_id,"
					+ " emp_prevexp,"
					+ " emp_date_of_join,"
					+ " emp_date_of_relieve,"
					+ " emp_reason_of_leaving,"
					+ " emp_notes,"
					+ " emp_recperpage,"
					+ " emp_timeout,"
					+ " emp_theme_id,"
					+ " emp_entry_id,"
					+ " emp_entry_date,"
					+ " emp_modified_id,"
					+ " emp_modified_date)"
					+ " VALUES"
					+ " ('" + emp_id + "',"
					+ " '" + emp_uuid + "',"
					+ " '" + emp_name + "',"
					+ " '',"
					+ " '" + emp_uname + "',"
					+ " '" + emp_role_id + "',"
					+ " '0',"
					+ " '" + emp_ref_no + "',"
					+ " '0',"
					+ " '" + emp_sex + "',"
					+ " '" + emp_dob + "',"
					+ " '" + emp_married + "',"
					+ " '" + emp_qualification + "',"
					+ " '" + emp_certification + "',"
					+ " '" + emp_phone1 + "',"
					+ " '" + emp_phone2 + "',"
					+ " '" + emp_mobile1 + "',"
					+ " '" + emp_mobile2 + "',"
					+ " '" + emp_email1 + "',"
					+ " '" + emp_email2 + "',"
					// + " '" + GenPass(8) + "',"
					+ " '" + emp_upass + "',"
					+ " '" + emp_address + "',"
					+ " '" + emp_city + "',"
					+ " '" + emp_pin + "',"
					+ " '" + emp_state + "',"
					+ " '" + emp_landmark + "',"
					+ " '" + emp_active + "',"
					+ " '" + emp_branch_id + "',"
					+ " '" + emp_weeklyoff_id + "',"
					+ " '" + emp_prevexp + "'," // emp_prevexp
					+ " '" + emp_date_of_join + "',"
					+ " '" + emp_date_of_relieve + "',"
					+ " '" + emp_reason_of_leaving + "',"
					+ " '" + emp_notes + "',"
					+ " 10,"
					+ " 20,"
					+ " " + emp_theme_id + ","
					+ " " + emp_entry_id + ","
					+ " '" + emp_entry_date + "',"
					+ " '0',"
					+ " '')";
			// SOP("StrSql======ecover=====================" + Strsql);
			updateQuery(Strsql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void UpdateFields(HttpServletRequest request) {
		try {
			String StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
					+ " SET"
					+ " emp_name = '" + emp_name + "',"
					+ " emp_uname = '" + emp_uname + "',"
					+ " emp_ref_no = '" + emp_ref_no + "',"
					+ " emp_sex = '" + emp_sex + "',"
					+ " emp_dob = '" + emp_dob + "',"
					+ " emp_married = '" + emp_married + "',"
					+ " emp_qualification = '" + emp_qualification + "',"
					+ " emp_certification = '" + emp_certification + "',"
					+ " emp_phone1 = '" + emp_phone1 + "',"
					+ " emp_phone2 = '" + emp_phone2 + "',"
					+ " emp_mobile1 = '" + emp_mobile1 + "',"
					+ " emp_mobile2 = '" + emp_mobile2 + "',"
					+ " emp_email1 = '" + emp_email1 + "',"
					+ " emp_email2 = '" + emp_email2 + "',"
					+ " emp_upass = '" + emp_upass + "',"
					+ " emp_address = '" + emp_address + "',"
					+ " emp_city = '" + emp_city + "',"
					+ " emp_pin = '" + emp_pin + "',"
					+ " emp_state = '" + emp_state + "',"
					+ " emp_landmark = '" + emp_landmark + "',"
					+ " emp_notes = '" + emp_notes + "',"
					+ " emp_active = '" + emp_active + "',"
					+ " emp_branch_id='" + emp_branch_id + "',"
					+ " emp_weeklyoff_id = '" + emp_weeklyoff_id + "',"
					+ " emp_prevexp='" + emp_prevexp + "',"
					+ " emp_date_of_join = '" + emp_date_of_join + "',"
					+ " emp_date_of_relieve = '" + emp_date_of_relieve + "',"
					+ " emp_reason_of_leaving = '" + emp_reason_of_leaving + "',"
					+ " emp_role_id = '" + emp_role_id + "',"
					+ " emp_modified_id = " + emp_modified_id + ","
					+ " emp_modified_date = '" + emp_modified_date + "'"
					+ " WHERE emp_id = " + emp_id + "";
			// SOP("StrSql==1111==axelaauto===" + StrSqlBreaker(StrSql));
			updateQuery(StrSql);
		} catch (Exception e) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}
}
