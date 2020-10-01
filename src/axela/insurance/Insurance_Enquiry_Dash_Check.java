/*Bhagwan Singh 18th April 2013*/
package axela.insurance;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Insurance_Enquiry_Dash_Check extends Connect {

	public String insurenquiry_id = "";
	public String insurenquiry_branch_id = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String name = "";
	public String value = "";
	public String history_actiontype = "";
	public String history_oldvalue = "";
	public String history_newvalue = "";
	public String listpolicy = "";
	public String parentid = "", followupdetails = "";
	public int divid = 0;
	public String validatefollowupdetails = "", listfollowupdetails = "", nextfollowup = "",
			followupcomments = "", followupinstructions = "", reportInsurFollowup = "", reportInsurFollowupFields = "";
	public String exe_id = "", starttime = "", endtime = "", pending_followup = "", dr_followup_disp1_id = "", dr_followup_disp2_id = "",
			dr_followup_disp3_id = "", dr_followup_disp4_id = "", dr_followup_disp5_id = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		// SOP("Comp_id" + comp_id);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
			listpolicy = PadQuotes(request.getParameter("listpolicy"));
			insurenquiry_branch_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_branch_id")));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));

			validatefollowupdetails = PadQuotes(request.getParameter("validatefollowupdetails"));
			listfollowupdetails = PadQuotes(request.getParameter("listfollowupdetails"));
			nextfollowup = PadQuotes(request.getParameter("nextfollowup"));
			followupcomments = PadQuotes(request.getParameter("followupcomments"));
			followupinstructions = PadQuotes(request.getParameter("followupinstructions"));
			parentid = CNumeric(PadQuotes(request.getParameter("parentid")));
			followupdetails = PadQuotes(request.getParameter("followupdetails"));
			divid = Integer.parseInt(CNumeric(PadQuotes(request.getParameter("divid"))));
			reportInsurFollowup = PadQuotes(request.getParameter("reportInsurFollowup"));
			reportInsurFollowupFields = PadQuotes(request.getParameter("reportInsurFollowupFields"));

			if (followupdetails.equals("yes") && !parentid.equals("0")) {
				StrHTML = new Insurance_Enquiry_Dash().PopulateInsurFollowupFields(comp_id, parentid, divid);
			}
			if (followupdetails.equals("yes") && parentid.equals("0") && divid == 0) {
				StrHTML = new Insurance_Enquiry_Dash().PopulateInsurFollowupFields(comp_id, parentid, divid);
			}
			if (nextfollowup.equals("yes") && !parentid.equals("0")) {
				StrHTML = new Insurance_Enquiry_Dash().PopulateNextFollowupField(comp_id, parentid, divid);
			}
			if (followupcomments.equals("yes") && !parentid.equals("0")) {
				StrHTML = new Insurance_Enquiry_Dash().PopulateCommentsField(comp_id, parentid, divid);
			}
			if (followupinstructions.equals("yes") && !parentid.equals("0")) {
				StrHTML = new Insurance_Enquiry_Dash().PopulateInstructionField(comp_id, parentid, divid);
			}

			// for policy to populate in dash page
			if (!insurenquiry_id.equals("0") && listpolicy.equals("yes")) {
				StrHTML = new Insurance_Enquiry_Dash().ListPolicy(comp_id, insurenquiry_id);
			}

			if (validatefollowupdetails.equals("yes")) {
				StrHTML = new Insurance_Enquiry_Dash().AddInsuranceFollowup(comp_id, request);
			}
			if (listfollowupdetails.equals("yes")) {
				StrHTML = new Insurance_Enquiry_Dash().ListInsuranceFollowup(comp_id, insurenquiry_id, emp_id);
			}

			if (reportInsurFollowup.equals("yes")) {
				exe_id = PadQuotes(request.getParameter("dr_emp_id"));
				starttime = ConvertShortDateToStr(PadQuotes(request.getParameter("txt_starttime")));
				endtime = ConvertShortDateToStr(PadQuotes(request.getParameter("txt_endtime")));
				if (starttime.equals("")) {
					starttime = ReportStartdate();
				}

				if (endtime.equals("")) {
					endtime = strToShortDate(ToShortDate(kknow()));
				}
				pending_followup = PadQuotes(request.getParameter("chk_pending_followup"));
				SOP("in check==" + pending_followup);

				if (pending_followup.equals("true")) {
					pending_followup = "1";
				} else {
					pending_followup = "0";
				}
				dr_followup_disp1_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp1")));
				dr_followup_disp2_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp2")));
				dr_followup_disp3_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp3")));
				dr_followup_disp4_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp4")));
				dr_followup_disp5_id = CNumeric(PadQuotes(request.getParameter("dr_followup_disp5")));

				if (!dr_followup_disp1_id.equals("")) {
					dr_followup_disp1_id = ExecuteQuery("SELECT insurdisposition_name, insurdisposition_mandatory"
							+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
							+ " WHERE insurdisposition_id = " + dr_followup_disp1_id);
				}
				if (!dr_followup_disp2_id.equals("")) {
					dr_followup_disp2_id = ExecuteQuery("SELECT insurdisposition_name, insurdisposition_mandatory"
							+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
							+ " WHERE insurdisposition_id = " + dr_followup_disp2_id);
				}
				if (!dr_followup_disp3_id.equals("")) {
					dr_followup_disp3_id = ExecuteQuery("SELECT insurdisposition_name, insurdisposition_mandatory"
							+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
							+ " WHERE insurdisposition_id = " + dr_followup_disp3_id);
				}
				if (!dr_followup_disp4_id.equals("")) {
					dr_followup_disp4_id = ExecuteQuery("SELECT insurdisposition_name, insurdisposition_mandatory"
							+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
							+ " WHERE insurdisposition_id = " + dr_followup_disp4_id);
				}
				if (!dr_followup_disp5_id.equals("")) {
					dr_followup_disp5_id = ExecuteQuery("SELECT insurdisposition_name, insurdisposition_mandatory"
							+ " FROM " + compdb(comp_id) + "axela_insurance_disposition"
							+ " WHERE insurdisposition_id = " + dr_followup_disp5_id);
				}

				StrHTML = new Report_Insurance_Followup().FollowupDetails(comp_id, exe_id, starttime, endtime, pending_followup, dr_followup_disp1_id, dr_followup_disp2_id, dr_followup_disp3_id,
						dr_followup_disp4_id, dr_followup_disp5_id);
			}

			if (reportInsurFollowupFields.equals("yes") && !parentid.equals("0")) {
				StrHTML = new Report_Insurance_Followup().PopulateInsurFollowupFields(comp_id, parentid, divid);
			}

		}

		if (name.equals("dr_insurenquiry_emp_id")) {
			if (!value.equals("0")) {
				value = value.replaceAll("nbsp", "&");

				history_oldvalue = ExecuteQuery("SELECT emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON emp_id = insurenquiry_emp_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_emp_id = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);
				history_actiontype = "Previous Insurance Executive";

				history_newvalue = ExecuteQuery("SELECT emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON emp_id = insurenquiry_emp_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

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
				updateQuery(StrSql);

				StrHTML = "Insurance Executive updated!";
			}
			else {
				StrHTML = "Select Insurance Executive!";
			}
		} else if (name.equals("preownedvariant")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");

				history_oldvalue = ExecuteQuery("SELECT CONCAT(carmanuf_name,' ',preownedmodel_name, '',variant_name) AS variant_name"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_variant_id = variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON preownedmodel_carmanuf_id = carmanuf_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_variant_id = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);
				history_actiontype = "Previous Variant";

				history_newvalue = ExecuteQuery("SELECT CONCAT(carmanuf_name,' ',preownedmodel_name, '',variant_name) AS variant_name"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_variant_id = variant_id"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON preownedmodel_carmanuf_id = carmanuf_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

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
				updateQuery(StrSql);

				StrHTML = "Previous Variant updated!";
			}
		} else if (name.equals("txt_insurenquiry_reg_no")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				value = value.replaceAll(" ", "");
				value = value.replaceAll("-", "");

				StrSql = "SELECT insurenquiry_reg_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_reg_no = '" + value + "'"
						+ " AND insurenquiry_id != " + insurenquiry_id + "";
				if (!IsValidRegNo(value)) {
					StrHTML = "<font color=\"red\">Enter valid Reg. No.!</font>";
				} else if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Reg. No. Found!</font>";
				} else {
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_reg_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_reg_no = '" + value.toUpperCase().replaceAll(" ", "") + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					history_actiontype = "Reg. No.";

					StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_insurance_history"
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
					StrHTML = "Reg. No. updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Reg. No.!</font>";
			}
		} else if (name.equals("txt_insurenquiry_chassis_no")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				value = value.replaceAll(" ", "");
				value = value.replaceAll("-", "");

				StrSql = "SELECT insurenquiry_chassis_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_chassis_no = '" + value + "'"
						+ " AND insurenquiry_id != " + insurenquiry_id + "";
				if (!IsValidRegNo(value)) {
					StrHTML = "<font color=\"red\">Enter valid Chassis No.!</font>";
				} else if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Chassis No. Found!</font>";
				} else {
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_chassis_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_chassis_no = '" + value.toUpperCase().replaceAll(" ", "") + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					// To Update The jobcard Vehicle Details

					history_actiontype = " Chassis No.";

					StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_insurance_history"
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
					StrHTML = " Chassis No. updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Chassis No.!</font>";
			}
		} else if (name.equals("txt_insurenquiry_engine_no")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				value = value.replaceAll(" ", "");
				value = value.replaceAll("-", "");

				StrSql = "SELECT insurenquiry_engine_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_engine_no = '" + value + "'"
						+ " AND insurenquiry_id != " + insurenquiry_id + "";
				if (!IsValidRegNo(value)) {
					StrHTML = "<font color=\"red\">Enter valid Engine No.!</font>";
				} else if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Engine No. Found!</font>";
				} else {
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_engine_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_engine_no = '" + value.toUpperCase().replaceAll(" ", "") + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					// To Update The jobcard Vehicle Details

					history_actiontype = "Engine No.";

					StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_insurance_history"
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
					StrHTML = " Engine No. updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Engine No.!</font>";
			}
		} else if (name.equals("txt_insurenquiry_reg_no")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				value = value.replaceAll(" ", "");
				value = value.replaceAll("-", "");

				StrSql = "SELECT insurenquiry_reg_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_reg_no = '" + value + "'"
						+ " AND insurenquiry_id != " + insurenquiry_id + "";
				if (!IsValidRegNo(value)) {
					StrHTML = "<font color=\"red\">Enter valid Reg No.!</font>";
				} else if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Reg No. Found!</font>";
				} else {
					history_oldvalue = ExecuteQuery("SELECT insurenquiry_reg_no FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_reg_no = '" + value.toUpperCase().replaceAll(" ", "") + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					// To Update The jobcard Vehicle Details

					history_actiontype = "Reg No.";

					StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_insurance_history"
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
					StrHTML = " Reg No. updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Reg No.!</font>";
			}
		} else if (name.equals("txt_insurenquiry_sale_date")) {
			if (!value.equals("")) {
				// SOP("coming..." + value);
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurenquiry_sale_date FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_sale_date = '" + ConvertShortDateToStr(value) + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				// SOP("StrSql..." + StrSql);
				updateQuery(StrSql);
				history_actiontype = "Sales Date";

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

				StrHTML = "Sales Date Updated!";
			}
		} else if (name.equals("txt_insurenquiry_renewal_date")) {
			if (!value.equals("")) {
				// SOP("coming..." + value);
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurenquiry_renewal_date FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_renewal_date = '" + ConvertShortDateToStr(value) + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				// SOP("StrSql..." + StrSql);
				updateQuery(StrSql);
				history_actiontype = "Renewal Date";

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

				StrHTML = "Renewal Date Updated!";
			}
		} else if (name.equals("dr_insurenquiry_insurtype_id")) {
			if (!value.equals("0")) {

				history_oldvalue = ExecuteQuery("SELECT insurtype_name FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurenquiry_insurtype_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_insurtype_id = " + value + ""
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				// To Update The jobcard Vehicle Details
				history_newvalue = ExecuteQuery("SELECT insurtype_name FROM " + compdb(comp_id) + "axela_insurance_type "
						+ " WHERE insurtype_id = " + value);

				history_actiontype = "Insurance Type ";

				StrSql = "INSERT INTO  " + compdb(comp_id) + "axela_insurance_history"
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
				updateQuery(StrSql);
				StrHTML = " Insurance Type updated!";
			}
			else {
				StrHTML = "<font color=\"red\">Select Insurance Type!</font>";
			}
		} else if (name.equals("txt_insurenquiry_address")) {
			if (!value.equals("")) {

				history_oldvalue = ExecuteQuery("SELECT contact_address"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON  insurenquiry_contact_id = contact_id"
						+ " SET"
						+ " contact_address = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				// To Update The jobcard Vehicle Details

				history_actiontype = " Insurance Address";

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
				StrHTML = " Insurance Address updated!";
			} else {
				StrHTML = "<font color=\"red\">Insurance Address!</font>";
			}
		} else if (name.equals("dr_insur_soe_id")) {
			if (!value.equals("")) {

				history_oldvalue = ExecuteQuery("SELECT soe_name FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_soe ON soe_id = insurenquiry_soe_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_soe_id = " + value + ""
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				history_newvalue = ExecuteQuery("SELECT soe_name FROM " + compdb(comp_id) + "axela_soe"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
						+ " AND soe_id = " + value);

				// To Update The jobcard Vehicle Details

				history_actiontype = " SOE ";

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
				updateQuery(StrSql);
				StrHTML = " SOE updated!";
			} else {
				StrHTML = "<font color=\"red\">SOE!</font>";
			}
		} else if (name.equals("dr_insur_sob_id")) {
			if (!value.equals("")) {

				history_oldvalue = ExecuteQuery("SELECT sob_name FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sob ON sob_id = insurenquiry_sob_id "
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_sob_id = " + value + ""
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				history_newvalue = ExecuteQuery("SELECT sob_name FROM " + compdb(comp_id) + "axela_sob"
						// + " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
						+ " WHERE sob_id = " + value);

				// To Update The jobcard Vehicle Details

				history_actiontype = " SOB ";

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
				updateQuery(StrSql);
				StrHTML = " SOB updated!";
			} else {
				StrHTML = "<font color=\"red\">SOB!</font>";
			}
		} else if (name.equals("dr_insurenquiry_campaign_id")) {
			if (!value.equals("")) {

				history_oldvalue = ExecuteQuery("SELECT campaign_name FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign ON campaign_id = insurenquiry_campaign_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_campaign_id = " + value + ""
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				history_newvalue = ExecuteQuery("SELECT campaign_name FROM " + compdb(comp_id) + "axela_sales_campaign"
						+ " WHERE campaign_id = " + value);

				// To Update The jobcard Vehicle Details

				history_actiontype = " Campaign ";

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
				updateQuery(StrSql);
				StrHTML = " Campaign updated!";
			} else {
				StrHTML = "<font color=\"red\">Campaign!</font>";
			}
		} else if (name.equals("txt_insurenquiry_notes")) {
			if (!value.equals("")) {

				history_oldvalue = ExecuteQuery("SELECT insurenquiry_notes FROM  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE  " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_notes = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				// To Update The jobcard Vehicle Details

				history_actiontype = " Notes ";

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
				StrHTML = " Notes updated!";
			} else {
				StrHTML = "<font color=\"red\">Notes!</font>";
			}
		} else if (name.equals("txt_enquiry_customer_name")) {
			if (!value.equals("")) {
				if (value.length() < 3) {
					StrHTML = ("Enter atleast 3 Characters for Customer Name!");
					return;
				}
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT COALESCE(customer_name,'') AS customer_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_customer_id = customer_id"
						+ " SET"
						+ " customer_name = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "CUSTOMER";
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
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Customer Name updated!";
			} else {
				StrHTML = "Enter Customer Name!";
			}
		} else if (name.equals("dr_title")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT title_desc "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
						+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
						+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
						+ " SET"
						+ " contact_title_id = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				String history_newvalue = ExecuteQuery("SELECT title_desc "
						+ "FROM " + compdb(comp_id) + "axela_title"
						+ " WHERE title_id=" + value + " ");

				String history_actiontype = "CONTACT_TITLE";

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
				StrHTML = "Title updated!";
			} else {
				StrHTML = "Select Title!";
			}
		}

		if (name.equals("txt_contact_fname")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT contact_fname "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
						+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " SET"
						+ " contact_fname = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "CONTACT_FIRST_NAME";

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
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Contact first name updated!";
			} else {
				StrHTML = "Enter Contact first name!";
			}
		}

		if (name.equals("txt_contact_lname")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT contact_lname "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
						+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " SET"
						+ " contact_lname = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "CONTACT_LAST_NAME";

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
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Contact last name updated!";
			} else {
				StrHTML = "Enter Contact last name!";
			}
		}

		if (name.equals("txt_contact_mobile1")) {
			if (!value.equals("") && IsValidMobileNo11(value)) {
				value = value.replaceAll("nbsp", "&");
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " WHERE 1 = 1 "
						+ " AND (contact_mobile1 = '" + value + "' OR contact_mobile2 = '" + value + "')"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id
						+ " AND insurenquiry_id!=" + insurenquiry_id;
				// SOP("Mob1===" + StrSql);
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "Similar Mobile 1 Found!";
				} else {
					String history_oldvalue = ExecuteQuery("SELECT contact_mobile1 "
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
							+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
							+ " SET"
							+ " contact_mobile1 = '" + value + "',"
							+ " customer_mobile1 = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					String history_actiontype = "CONTACT_MOBILE_1";

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
					StrHTML = "Contact Mobile 1 updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Valid Mobile 1!</font>";
			}
		}
		if (name.equals("txt_contact_mobile2")) {
			String history_oldvalue = "";
			String history_actiontype = "";
			if (!value.equals("") && IsValidMobileNo11(value)) {
				value = value.replaceAll("nbsp", "&");
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " WHERE 1 = 1 "
						+ " AND (contact_mobile1 = '" + value + "' OR contact_mobile2 = '" + value + "')"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id
						+ " AND insurenquiry_id!=" + insurenquiry_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "Similar Mobile 2 Found!";
				} else if (!value.equals("") && IsValidMobileNo11(value)) {
					history_oldvalue = ExecuteQuery("SELECT contact_mobile2 "
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
							+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
							+ " SET"
							+ " contact_mobile2 = '" + value + "',"
							+ " customer_mobile2 = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					history_actiontype = "CONTACT_MOBILE_2";

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
					StrHTML = "Contact Mobile 2 updated!";
				}

			} else if (!value.equals("") && !IsValidMobileNo11(value)) {
				StrHTML = "<font color=\"red\">Enter Valid Contact Mobile2!</font>";
			} else if (value.equals("")) {
				history_oldvalue = ExecuteQuery("SELECT contact_mobile2 "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
						+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
						+ " SET"
						+ " contact_mobile2 = '" + value + "',"
						+ " customer_mobile2 = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

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
				StrHTML = "Contact Mobile2 updated!";
			}
		}

		if (name.equals("txt_contact_phone1")) {
			if (!value.equals("") && IsValidPhoneNo11(value)) {
				value = value.replaceAll("nbsp", "&");
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " WHERE 1 = 1 "
						+ " AND (contact_phone1 = '" + value + "' OR contact_phone2 = '" + value + "')"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id;
				// + " and insurenquiry_id!=" + insurenquiry_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Phone 1 Found!</font>";
				} else {
					String history_oldvalue = ExecuteQuery("SELECT contact_phone1 "
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
							+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
							+ " SET"
							+ " contact_phone1 = '" + value + "',"
							+ " customer_phone1 = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";

					updateQuery(StrSql);

					String history_actiontype = "CONTACT_PHONE_1";

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

					StrHTML = "Contact Phone 1 updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Valid Phone 1!</font>";
			}
		}

		if (name.equals("txt_contact_phone2")) {
			if (!value.equals("") && IsValidPhoneNo11(value)) {
				value = value.replaceAll("nbsp", "&");
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " WHERE 1 = 1 "
						+ " AND (contact_phone1 = '" + value + "' OR contact_phone2 = '" + value + "')"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id;
				// + " and insurenquiry_id!=" + insurenquiry_id;
				// SOP(StrSql);
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color=\"red\">Similar Phone 2 Found!</font>";
				} else {
					String history_oldvalue = ExecuteQuery("SELECT contact_phone2"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
							+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
							+ " SET"
							+ " contact_phone2 = '" + value + "',"
							+ " customer_phone2 = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					String history_actiontype = "CONTACT_PHONE_2";

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
					StrHTML = "Contact Phone 2 updated!";
				}
			} else if (!value.equals("") && !IsValidPhoneNo11(value)) {
				StrHTML = "<font color=\"red\">Enter Valid Contact Phone 2!</font>";
			}
		}

		if (name.equals("txt_contact_email1")) {
			if (!value.equals("") && IsValidEmail(value)) {
				value = value.replaceAll("nbsp", "&");
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " WHERE 1 = 1"
						+ " AND contact_email1 = '" + value + "'"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id
						+ " AND insurenquiry_id!=" + insurenquiry_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "Similar Email 1 Found!!";
				} else {
					String history_oldvalue = ExecuteQuery("SELECT contact_email1 "
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
							+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
							+ " SET"
							+ " contact_email1 = '" + value + "',"
							+ " customer_email1 = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					String history_actiontype = "CONTACT_EMAIL_1";
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
					StrHTML = "Contact Email 1 updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Valid Contact Email 1!</font>";
			}
		}
		if (name.equals("txt_contact_email2")) {
			if (!value.equals("") && IsValidEmail(value)) {
				value = value.replaceAll("nbsp", "&");
				StrSql = "SELECT contact_id "
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_contact_id = contact_id"
						+ " WHERE 1 = 1"
						+ " AND contact_email2 = '" + value + "'"
						+ " AND insurenquiry_branch_id = " + insurenquiry_branch_id
						+ " AND insurenquiry_id!=" + insurenquiry_id;
				if (!ExecuteQuery(StrSql).equals("")) {
					StrHTML = "Similar Email 2 Found!!";
				} else {
					String history_oldvalue = ExecuteQuery("SELECT contact_email2 "
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
							+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
							+ " SET"
							+ " contact_email2 = '" + value + "',"
							+ " customer_email2 = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);

					String history_actiontype = "CONTACT_EMAIL_2";
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
					StrHTML = "Contact Email 2 updated!";
				}
			} else {
				StrHTML = "<font color=\"red\">Enter Valid Contact Email 2!</font>";
			}
		}

		if (name.equals("txt_contact_address")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT contact_address "
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry "
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id "
						+ " WHERE insurenquiry_id=" + insurenquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
						+ " SET"
						+ " contact_address = '" + value + "',"
						+ " customer_address = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "CONTACT_ADDRESS";

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
				StrHTML = "Contact Address updated!";
			} else {
				StrHTML = "Enter Contact Address!";
			}
		}

		if (name.equals("maincity")) {
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
				StrHTML = "City updated!";
			} else {
				StrHTML = "Select City!";
			}
		}

		if (name.equals("txt_contact_pin")) {
			if (!value.equals("")) {
				if (!isNumeric(value) || value.length() != 6) {
					StrHTML = "Enter Valid Pin!";
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
		} else if (name.equals("dr_insurenquiry_status_id")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");

				String enquiry_edit = CNumeric(ExecuteQuery("SELECT emp_close_enquiry"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + emp_id));

				if (value.equals("2")) {
					StrHTML = "<font color=\"red\">Update Permission Denied!</font><br>";
				}
				else if ((value.equals("3") || value.equals("4")) && enquiry_edit.equals("0")) {
					StrHTML = "<font color=\"red\">Update Permission Denied!</font><br>";
				} else {

					history_oldvalue = ExecuteQuery("SELECT insurstatus_name"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_status"
							+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_insurstatus_id = insurstatus_id"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

					StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
							+ " SET"
							+ " insurenquiry_insurstatus_id = '" + value + "'"
							+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
					updateQuery(StrSql);
					history_actiontype = "Previous Status";

					history_newvalue = ExecuteQuery("SELECT insurstatus_name"
							+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry_status"
							+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_insurstatus_id = insurstatus_id"
							+ " WHERE insurenquiry_id = " + insurenquiry_id);

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
					updateQuery(StrSql);

					StrHTML = "<font color='red'>Select Lost Case 1!</font>";
				}
			}
		} else if (name.equals("dr_insurenquiry_lostcase1_id")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");

				history_oldvalue = ExecuteQuery("SELECT insurlostcase1_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_lostcase1"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_insurlostcase1_id = insurlostcase1_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_insurlostcase1_id = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);
				history_actiontype = "Previous Lost Case1";

				history_newvalue = ExecuteQuery("SELECT insurlostcase1_name"
						+ " FROM " + compdb(comp_id) + "axela_insurance_lostcase1"
						+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_enquiry ON insurenquiry_insurlostcase1_id = insurlostcase1_id"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

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
				updateQuery(StrSql);

				StrSql = "SELECT insurenquiry_insurstatus_desc"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id;
				if (ExecuteQuery(StrSql).equals("")) {
					StrHTML = "<font color='red'>Enter Status Comments!</font>";
				} else {
					StrHTML = "Previous Status updated!";
				}

			}
		} else if (name.equals("txt_insurenquiry_status_desc")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurenquiry_insurstatus_desc"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_insurstatus_desc = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);
				history_actiontype = "Previous Status Comments";

				history_newvalue = ExecuteQuery("SELECT insurenquiry_insurstatus_desc"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

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
				updateQuery(StrSql);

				StrHTML = "Previous Status updated!";
			} else {
				StrHTML = "<font color='red'>Enter Status Comments!</font>";
			}
		} else if (name.equals("txt_insurenquiry_notes")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = ExecuteQuery("SELECT insurenquiry_notes"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " SET"
						+ " insurenquiry_notes = '" + value + "'"
						+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
				updateQuery(StrSql);
				history_actiontype = "Previous Notes";

				history_newvalue = ExecuteQuery("SELECT insurenquiry_notes"
						+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
						+ " WHERE insurenquiry_id = " + insurenquiry_id);

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
				updateQuery(StrSql);

				StrHTML = "Previous Notes updated!";
			} else {
				StrHTML = "Enter Notes !";
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
