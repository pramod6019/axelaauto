/*Bhagwan Singh 18th April 2013*/
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Vehicle_Dash_Insurance_Followup_Check extends Connect {

	public String followupinfo = "";
	public String insurenquiry_id = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String name = "";
	public String value = "";
	public String history_actiontype = "";
	public String history_oldvalue = "";
	public String history_newvalue = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		// SOP("Comp_id" + comp_id);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
			followupinfo = PadQuotes(request.getParameter("followupinfo"));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			// SOP("name==" + name);
			// SOP("value==" + value);
			if (followupinfo.equals("yes")) {
				StrHTML = new Vehicle_Dash_Insurance_Followup().ListInsuranceFollowup(insurenquiry_id, comp_id);
			}

			if (name.equals("txt_insurfollowup_previouscompname")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_previouscompname"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_previouscompname = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Previous Insurance Company Name";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Previous Insurance Company Name updated!";
					SOP("StrHTML===" + StrHTML);
				}
			} else if (name.equals("txt_insurfollowup_previousyearidv")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_previousyearidv"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_previousyearidv = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Previous Year IDV";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Previous Year IDV Updated!";
				}
			} else if (name.equals("txt_insurfollowup_previousgrosspremium")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_previousgrosspremium"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_previousgrosspremium = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Previous Gross Premium";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Previous Gross Premium Updated!";
				}
			} else if (name.equals("txt_insurfollowup_previousplanname")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_previousplanname"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_previousplanname = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Previous Plan Name";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Previous Plan Name Updated!";
				}
			} else if (name.equals("txt_insurfollowup_policyexpirydate")) {
				if (!value.equals("")) {
					// SOP("coming..." + value);
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_policyexpirydate"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_policyexpirydate = '" + ConvertShortDateToStr(value) + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Policy Expiry Date";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + strToShortDate(history_oldvalue) + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Policy Expiry Date Updated!";
				}
			} else if (name.equals("txt_insurfollowup_currentidv")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_currentidv"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_currentidv = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Current IDV";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Current IDV Updated!";
				}
			} else if (name.equals("txt_insurfollowup_premium")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_premium"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_premium = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Insurance Premium";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Insurance Premium Updated!";
				}
			} else if (name.equals("txt_insurfollowup_premiumwithzerodept")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_premiumwithzerodept"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_premiumwithzerodept = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Insurance Premium With Zero Dept";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Insurance Premium With Zero Dept Updated!";
				}
			} else if (name.equals("txt_insurfollowup_companyoffered")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_compoffered"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_compoffered = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Company Offered";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Company Offered Updated!";
				}
			} else if (name.equals("txt_insurfollowup_plansuggested")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_plansuggested"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_plansuggested = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Plan Suggested";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Plan Suggested Updated!";
				}
			} else if (name.equals("txt_insurfollowup_name")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT veh_contactname"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " veh_contactname = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					// SOP("StrSql..." + StrSql);
					updateQuery(StrSql);
					history_actiontype = "Name";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Name Updated!";
				}
			} else if (name.equals("txt_insurfollowup_variant")) {
				if (!value.equals("")) {
					// SOP("coming...");
					value = value.replaceAll("nbsp", "&");
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_variant"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_variant = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Variant";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);

					StrHTML = "Variant Updated!";
				}
			} else if (name.equals("txt_insurfollowup_ncb")) {
				// SOP("coming...");
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurenquiry_ncb"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_ncb = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				SOP("StrSql..." + StrSql);
				updateQuery(StrSql);
				history_actiontype = "NCB";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
						+ " (history_insurenquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + insurenquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				SOP("StrSql====" + StrSql);
				updateQuery(StrSql);

				StrHTML = "NCB Updated!";
			} else if (name.equals("txt_insurfollowup_reg_no")) {
				// SOP("coming...");
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurenquiry_reg_no"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_reg_no = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				SOP("StrSql..." + StrSql);
				updateQuery(StrSql);
				history_actiontype = "Reg No.";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
						+ " (history_insurenquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + insurenquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				SOP("StrSql====" + StrSql);
				updateQuery(StrSql);

				StrHTML = "Reg No. Updated!";
			} else if (name.equals("txt_insurfollowup_chassis_no")) {
				// SOP("coming...");
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurenquiry_chassis_no"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_chassis_no = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				SOP("StrSql..." + StrSql);
				updateQuery(StrSql);
				history_actiontype = "Chassis No.";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
						+ " (history_insurenquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + insurenquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				SOP("StrSql====" + StrSql);
				updateQuery(StrSql);

				StrHTML = "Chassis No. Updated!";
			}

			else if (name.equals("dr_insurenquiry_insurgift_id")) {
				// SOP("coming...");
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurgift_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_gift ON insurgift_id = insurenquiry_insurgift_id "
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				history_newvalue = ExecuteQuery("SELECT insurgift_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_gift"
						+ " WHERE insurgift_id = " + value);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_insurgift_id = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);
				history_actiontype = "Gift";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
						+ " (history_insurenquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + insurenquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				// SOP("StrSql===Gift Combo=" + StrSql);
				updateQuery(StrSql);

				StrHTML = "Gift Updated!";
			}

			else if (name.equals("txt_insurfollowup_address")) {
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT contact_address"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " SET"
						+ " contact_address = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);
				history_actiontype = "Address";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
						+ " (history_insurenquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + insurenquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);

				StrHTML = "Contact Address Updated!";
			}
			if (name.equals("contactmaincity")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("SELECT city_name "
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id "
							+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
							+ " SET"
							+ " contact_city_id = '" + value + "',"
							+ " customer_city_id = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);
					String history_newvalue = ExecuteQuery("SELECT city_name from " + compdb(comp_id) + "axela_city WHERE city_id=" + value + " ");

					String history_actiontype = "CONTACT_CITY";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Contact City updated!";
				} else {
					StrHTML = "Select Contact  City!";
				}
			} else if (name.equals("txt_insurfollowup_contact_pin")) {
				if (!value.equals("")) {
					if (!isNumeric(value) || value.length() != 6) {
						StrHTML = "Enter Valid Contact Pin!";
					}
					else {
						value = value.replaceAll("nbsp", "&");
						String history_oldvalue = ExecuteQuery("SELECT contact_pin "
								+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
								+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

						StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
								+ " SET"
								+ " contact_pin = '" + value + "',"
								+ " customer_pin = '" + value + "'"
								+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
						updateQuery(StrSql);

						String history_actiontype = "CONTACT_PIN";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
								+ " (history_insurenquiry_id, "
								+ " history_emp_id, "
								+ " history_datetime, "
								+ " history_actiontype, "
								+ " history_oldvalue, "
								+ " history_newvalue)"
								+ " VALUES ("
								+ " '" + insurenquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + value + "')";
						updateQuery(StrSql);
						StrHTML = "Contact Pin updated!";
					}
				} else {
					StrHTML = "Enter Contact Pin!";
				}
			} else if (name.equals("txt_insurfollowup_pickupaddress")) {
				if (!value.equals("")) {

					history_oldvalue = ExecuteQuery("SELECT insurenquiry_pickupaddress"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_pickupaddress = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					// To Update The jobcard Vehicle Details

					history_actiontype = " Insurance Pick-up Address";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_insurance_history"
							+ " (history_insurenquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES"
							+ " ('" + insurenquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Pick-up Address Updated!";
				} else {
					StrHTML = "<font color=\"red\">Insurance Address!</font>";
				}
			}

		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
