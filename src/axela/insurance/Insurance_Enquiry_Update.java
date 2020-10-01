package axela.insurance;
//@Bhagwan Singh 1 March 2013

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.preowned.Preowned_Variant_Check;
import cloudify.connect.Connect;

public class Insurance_Enquiry_Update extends Connect {

	public String update = "";
	public String deleteB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String customer_id = "0";
	public String customer_name = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String insurenquiry_id = "0";
	public String insurenquiry_date = "";
	public String insurenquiry_branch_id = "0";
	public String insurenquiry_customer_id = "0";
	public String contact_id = "0";
	public String branch_id = "0";
	public String insurenquiry_contact_id = "0";
	public String insurenquiry_variant_id = "0";
	public String insurenquiry_variant = "";
	public String insurenquiry_modelyear = "";
	public String insurenquiry_chassis_no = "";
	public String insurenquiry_engine_no = "";
	public String insurenquiry_reg_no = "";
	public String insurenquiry_sale_date = "", insurenquirysaledate = "";
	public String insurenquiry_renewal_date = "", renewal_date = "";;
	public String insurenquiry_insurtype_id = "";

	public String insurenquiry_previouscompname = "";
	public String insurenquiry_previousyearidv = "";
	public String insurenquiry_previousgrosspremium = "";
	public String insurenquiry_previousplanname = "";
	public String insurenquiry_policyexpirydate = "";
	public String insurenquiry_currentidv = "";
	public String insurenquiry_premium = "";
	public String insurenquiry_premiumwithzerodept = "";
	public String insurenquiry_compoffered = "";
	public String insurenquiry_plansuggested = "";
	public String insurenquiry_ncb = "";
	public String insurenquiry_address = "";
	public String insurenquiry_soe_id = "0";
	public String insurenquiry_sob_id = "0";
	public String insurenquiry_campaign_id = "0";

	public String insurenquiry_notes = "";
	public String insurenquiry_entry_id = "0";
	public String insurenquiry_entry_date = "";
	public String insurenquiry_entry_by = "";
	public String entry_date = "";
	public String emp_role_id = "0";
	public String insurenquiry_modified_id = "0";
	public String insurenquiry_modified_date = "";
	public String modified_date = "";
	public String insurenquiry_modified_by = "";
	public String QueryString = "";
	public String insurenquiry_emp_id = "0";
	public String BranchAccess = "0";
	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();
	public MIS_Check mischeck = new MIS_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_enquiry_access", request, response);
			// SOP("comp_id===" + comp_id);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				insurenquiry_id = CNumeric(PadQuotes(request.getParameter("insurenquiry_id")));
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					status = "Update";
					PopulateFields(request, response);
					if ("yes".equals(updateB) && !deleteB.equals("Delete Insurance Enquiry")) {
						// PopulateFields(request, response);
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_insurance_enquiry_edit", request).equals("1")) {
							insurenquiry_modified_id = emp_id;
							insurenquiry_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insurance-enquiry-list.jsp?insurenquiry_id=" + insurenquiry_id + "&msg=Insurance Enquiry updated successfully!" + msg
										+ ""));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					} else if (deleteB.equals("Delete Insurance Enquiry")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_insurance_enquiry_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("insurance-enquiry-list.jsp?msg=Insurance Enquiry deleted successfully!"));
							}
						} else {
							response.sendRedirect(response.encodeRedirectURL(AccessDenied()));
						}
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		insurenquiry_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		insurenquiry_variant_id = CNumeric(PadQuotes(request.getParameter("preownedvariant")));
		insurenquiry_modelyear = PadQuotes(request.getParameter("txt_insurenquiry_modelyear"));
		insurenquiry_chassis_no = PadQuotes(request.getParameter("txt_insurenquiry_chassis_no"));
		insurenquiry_engine_no = PadQuotes(request.getParameter("txt_insurenquiry_engine_no"));
		insurenquiry_reg_no = PadQuotes(request.getParameter("txt_insurenquiry_reg_no"));
		insurenquirysaledate = PadQuotes(request.getParameter("txt_insurenquiry_sale_date"));
		insurenquiry_emp_id = CNumeric(PadQuotes(request.getParameter("dr_insurenquiry_insuremp_id")));
		renewal_date = PadQuotes(request.getParameter("txt_insurenquiry_renewal_date"));
		insurenquiry_insurtype_id = CNumeric(PadQuotes(request.getParameter("dr_insurenquiry_insurtype_id")));

		insurenquiry_previouscompname = PadQuotes(request.getParameter("txt_insurenquiry_previouscompname"));
		insurenquiry_previousyearidv = PadQuotes(request.getParameter("txt_insurenquiry_previousyearidv"));
		insurenquiry_previousgrosspremium = PadQuotes(request.getParameter("txt_insurenquiry_previousgrosspremium"));
		insurenquiry_previousplanname = PadQuotes(request.getParameter("txt_insurenquiry_previousplanname"));
		insurenquiry_policyexpirydate = PadQuotes(request.getParameter("txt_insurenquiry_policyexpirydate"));
		insurenquiry_currentidv = PadQuotes(request.getParameter("txt_insurenquiry_currentidv"));
		insurenquiry_premium = PadQuotes(request.getParameter("txt_insurenquiry_premium"));
		insurenquiry_premiumwithzerodept = PadQuotes(request.getParameter("txt_insurenquiry_premiumwithzerodept"));
		insurenquiry_compoffered = PadQuotes(request.getParameter("txt_insurenquiry_compoffered"));
		insurenquiry_plansuggested = PadQuotes(request.getParameter("txt_insurenquiry_plansuggested"));
		insurenquiry_ncb = PadQuotes(request.getParameter("txt_insurenquiry_ncb"));
		insurenquiry_address = PadQuotes(request.getParameter("txt_insurenquiry_address"));
		insurenquiry_soe_id = CNumeric(PadQuotes(request.getParameter("dr_insur_soe_id")));
		insurenquiry_sob_id = CNumeric(PadQuotes(request.getParameter("dr_insur_sob_id")));
		insurenquiry_campaign_id = CNumeric(PadQuotes(request.getParameter("dr_insurenquiry_campaign_id")));

		insurenquiry_notes = PadQuotes(request.getParameter("txt_insurenquiry_notes"));
	}

	public void CheckForm() {
		msg = "";
		if (insurenquiry_branch_id.equals("0")) {
			msg += "<br>Select Branch!";
		}

		if (insurenquiry_variant_id.equals("0")) {
			msg += "<br>Select Variant!";
		}

		if (insurenquiry_modelyear.equals("")) {
			msg += "<br>Enter Model Year!";
		}
		else {
			if (insurenquiry_modelyear.length() < 4) {
				msg += "<br>Enter Valid Model Year!";
			}
			else if (Integer.parseInt(insurenquiry_modelyear) > Integer.parseInt(ToShortDate(kknow()).substring(0, 4))) {
				msg += "<br> Model Year Cannot be Greater Than Current Year!";
			}
		}

		// if (insurenquiry_chassis_no.equals("")) {
		// msg += "<br>Enter Chassis Number!";
		// } else {
		// StrSql = "SELECT insurenquiry_chassis_no FROM " + compdb(comp_id) + "axela_insurance_enquiry"
		// + " WHERE insurenquiry_chassis_no = '" + insurenquiry_chassis_no + "'"
		// + " and insurenquiry_id != " + insurenquiry_id + "";
		//
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg += "<br>Similar Chassis Number found!";
		// }
		// }
		//
		// if (insurenquiry_engine_no.equals("")) {
		// msg += "<br>Enter Engine Number!";
		// } else {
		// StrSql = "SELECT insurenquiry_engine_no FROM " + compdb(comp_id) + "axela_insurance_enquiry"
		// + " WHERE insurenquiry_engine_no = '" + insurenquiry_engine_no + "'"
		// + " and insurenquiry_id != " + insurenquiry_id + "";
		//
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg += "<br>Similar Engine Number found!";
		// }
		// }
		//
		// if (insurenquiry_reg_no.equals("")) {
		// msg += "<br>Enter Reg No. Number!";
		// } else {
		// StrSql = "SELECT insurenquiry_reg_no FROM " + compdb(comp_id) + "axela_insurance_enquiry"
		// + " WHERE insurenquiry_reg_no = '" + insurenquiry_reg_no + "'"
		// + " and insurenquiry_id != " + insurenquiry_id + "";
		//
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg += "<br>Similar Reg. Nofound!";
		// }
		// }

		if (!insurenquirysaledate.equals("")) {
			if (isValidDateFormatShort(insurenquirysaledate)) {
				insurenquiry_sale_date = ConvertShortDateToStr(insurenquirysaledate);
			} else {
				msg += "<br>Enter valid Sale Date!";
			}
		}

		if (insurenquiry_emp_id.equals("0")) {
			msg += "<br>Select Insurance Executive!";
		}

		if (insurenquiry_insurtype_id.equals("0")) {
			msg += "<br>Select Insurance Type!";
		}

		if (!renewal_date.equals("")) {
			if (isValidDateFormatShort(renewal_date)) {
				insurenquiry_renewal_date = ConvertShortDateToStr(renewal_date);
			} else {
				msg += "<br>Enter valid Insurance Renewal Date!";
			}
		}

	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT insurenquiry_customer_id, insurenquiry_contact_id,"
					+ " COALESCE (customer_name, '') AS customer_name,"
					+ " COALESCE (contact_title_id, 0) AS contact_title_id,"
					+ " COALESCE (title_desc, '') AS title_desc,"
					+ " COALESCE (contact_fname, '') AS contact_fname,"
					+ " COALESCE (contact_lname, '') AS contact_lname,"
					+ " insurenquiry_id, insurenquiry_date, insurenquiry_branch_id,"
					+ " COALESCE (variant_id, 0) AS variant_id,"
					+ " insurenquiry_variant, insurenquiry_modelyear, insurenquiry_chassis_no,"
					+ " insurenquiry_engine_no, insurenquiry_reg_no, insurenquiry_sale_date,"
					+ " insurenquiry_emp_id, insurenquiry_renewal_date, insurenquiry_insurtype_id,"
					+ " insurenquiry_previouscompname, insurenquiry_previousyearidv,"
					+ " insurenquiry_previousgrosspremium, insurenquiry_previousplanname,"
					+ " insurenquiry_policyexpirydate, insurenquiry_currentidv,"
					+ " insurenquiry_premium, insurenquiry_premiumwithzerodept,"
					+ " insurenquiry_compoffered, insurenquiry_plansuggested, insurenquiry_ncb,"
					+ " insurenquiry_address, insurenquiry_soe_id, insurenquiry_sob_id,"
					+ " insurenquiry_campaign_id, insurenquiry_notes, insurenquiry_entry_id,"
					+ " insurenquiry_entry_date, insurenquiry_variant_id, insurenquiry_variant_id,"
					+ " insurenquiry_modified_id, insurenquiry_modified_date "
					+ " FROM " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurenquiry_contact_id"
					+ " LEFT JOIN axela_preowned_variant ON variant_id = insurenquiry_variant_id"
					+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = insurenquiry_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = insurenquiry_branch_id"
					+ " WHERE 1 = 1 "
					+ " AND insurenquiry_id = " + insurenquiry_id + ""
					+ BranchAccess.replace("branch_id", "insurenquiry_branch_id");
			// SOP("str----55-------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					// branch_id = crs.getString("cust_branch_id");
					// SOP("branch_id------" + branch_id);
					insurenquiry_id = crs.getString("insurenquiry_id");
					insurenquiry_customer_id = crs.getString("insurenquiry_customer_id");
					insurenquiry_contact_id = crs.getString("insurenquiry_contact_id");

					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("insurenquiry_customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";

					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("insurenquiry_contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";

					insurenquiry_branch_id = crs.getString("insurenquiry_branch_id");
					insurenquiry_variant_id = crs.getString("insurenquiry_variant_id");
					insurenquiry_variant = crs.getString("insurenquiry_variant");
					insurenquiry_modelyear = crs.getString("insurenquiry_modelyear");
					insurenquiry_chassis_no = crs.getString("insurenquiry_chassis_no");
					insurenquiry_engine_no = crs.getString("insurenquiry_engine_no");
					insurenquiry_reg_no = SplitRegNo(crs.getString("insurenquiry_reg_no"), 2);
					insurenquirysaledate = strToShortDate(crs.getString("insurenquiry_sale_date"));
					insurenquiry_emp_id = crs.getString("insurenquiry_emp_id");
					renewal_date = strToShortDate(crs.getString("insurenquiry_renewal_date"));
					insurenquiry_insurtype_id = crs.getString("insurenquiry_insurtype_id");

					insurenquiry_previouscompname = crs.getString("insurenquiry_previouscompname");
					insurenquiry_previousyearidv = crs.getString("insurenquiry_previousyearidv");
					insurenquiry_previousgrosspremium = crs.getString("insurenquiry_previousgrosspremium");
					insurenquiry_previousplanname = crs.getString("insurenquiry_previousplanname");
					insurenquiry_policyexpirydate = strToShortDate(crs.getString("insurenquiry_policyexpirydate"));
					insurenquiry_currentidv = crs.getString("insurenquiry_currentidv");
					insurenquiry_premium = crs.getString("insurenquiry_premium");
					insurenquiry_premiumwithzerodept = crs.getString("insurenquiry_premiumwithzerodept");
					insurenquiry_compoffered = crs.getString("insurenquiry_compoffered");
					insurenquiry_plansuggested = crs.getString("insurenquiry_plansuggested");
					insurenquiry_ncb = crs.getString("insurenquiry_ncb");
					insurenquiry_address = crs.getString("insurenquiry_address");
					insurenquiry_soe_id = crs.getString("insurenquiry_soe_id");
					insurenquiry_sob_id = crs.getString("insurenquiry_sob_id");
					insurenquiry_campaign_id = crs.getString("insurenquiry_campaign_id");
					// SOP("insurenquiry_campaign_id==" + insurenquiry_campaign_id);

					insurenquiry_notes = crs.getString("insurenquiry_notes");
					insurenquiry_entry_id = crs.getString("insurenquiry_entry_id");
					if (!insurenquiry_entry_id.equals("")) {
						insurenquiry_entry_by = Exename(comp_id, Integer.parseInt(insurenquiry_entry_id));
					}
					entry_date = strToLongDate(crs.getString("insurenquiry_entry_date"));
					insurenquiry_modified_id = crs.getString("insurenquiry_modified_id");
					if (!insurenquiry_modified_id.equals("0")) {
						insurenquiry_modified_by = Exename(comp_id, Integer.parseInt(insurenquiry_modified_id));
						modified_date = strToLongDate(crs.getString("insurenquiry_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Enquiry!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " SET"
					+ " insurenquiry_branch_id = " + insurenquiry_branch_id + ","
					+ " insurenquiry_variant_id = " + insurenquiry_variant_id + ","
					+ " insurenquiry_variant = '" + insurenquiry_variant + "',"
					+ " insurenquiry_modelyear = '" + insurenquiry_modelyear + "',"
					+ " insurenquiry_chassis_no = '" + insurenquiry_chassis_no + "',"
					+ " insurenquiry_engine_no = '" + insurenquiry_engine_no + "',"
					+ " insurenquiry_reg_no = '" + insurenquiry_reg_no.toUpperCase() + "',"
					+ " insurenquiry_sale_date = '" + insurenquiry_sale_date + "',"
					+ " insurenquiry_emp_id = " + insurenquiry_emp_id + ","
					+ " insurenquiry_renewal_date = '" + insurenquiry_renewal_date + "',"

					+ " insurenquiry_insurtype_id = " + insurenquiry_insurtype_id + ","
					+ " insurenquiry_previouscompname = '" + insurenquiry_previouscompname + "',"
					+ " insurenquiry_previousyearidv = '" + insurenquiry_previousyearidv + "',"
					+ " insurenquiry_previousgrosspremium = '" + insurenquiry_previousgrosspremium + "',"
					+ " insurenquiry_previousplanname = '" + insurenquiry_previousplanname + "',"
					+ " insurenquiry_policyexpirydate = '" + ConvertShortDateToStr(insurenquiry_policyexpirydate) + "',"
					+ " insurenquiry_currentidv = '" + insurenquiry_currentidv + "',"
					+ " insurenquiry_premium = '" + insurenquiry_premium + "',"
					+ " insurenquiry_premiumwithzerodept = '" + insurenquiry_premiumwithzerodept + "',"
					+ " insurenquiry_compoffered = '" + insurenquiry_compoffered + "',"
					+ " insurenquiry_plansuggested = '" + insurenquiry_plansuggested + "',"
					+ " insurenquiry_ncb = '" + insurenquiry_ncb + "',"
					+ " insurenquiry_address = '" + insurenquiry_address + "',"
					+ " insurenquiry_soe_id = " + insurenquiry_soe_id + ","
					+ " insurenquiry_sob_id = " + insurenquiry_sob_id + ","
					+ " insurenquiry_campaign_id = " + insurenquiry_campaign_id + ","

					+ " insurenquiry_notes = '" + insurenquiry_notes + "',"
					+ " insurenquiry_modified_id = " + emp_id + ","
					+ " insurenquiry_modified_date = '" + ToLongDate(kknow()) + "'"
					+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
			updateQuery(StrSql);
			// SOP("StrSql===" + StrSql);
		}
	}

	protected void DeleteFields() {

		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_enquiry_followup"
					+ " WHERE insurenquiryfollowup_insurenquiry_id = " + insurenquiry_id + "";
			updateQuery(StrSql);

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_insurance_enquiry"
					+ " WHERE insurenquiry_id = " + insurenquiry_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulateInsurExecutive(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS insuremp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_insur = 1"
					// + " AND emp_active = 1"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Executive</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), insurenquiry_emp_id));
				Str.append(">").append(crs.getString("insuremp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInsurType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurtype_id, insurtype_name"
					+ " FROM " + compdb(comp_id) + "axela_insurance_type"
					+ " WHERE 1 = 1"
					+ " GROUP BY insurtype_id"
					+ " ORDER BY insurtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Insurance Type</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurtype_id"));
				Str.append(Selectdrop(crs.getInt("insurtype_id"), insurenquiry_insurtype_id));
				Str.append(">").append(crs.getString("insurtype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();

	}

	public String PopulateSoe(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp_soe ON empsoe_soe_id = soe_id"
					+ " WHERE 1 = 1"
					+ " AND soe_active = 1"
					+ " AND empsoe_emp_id = " + emp_id + ""
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("soe_id")).append("");
				Str.append(StrSelectdrop(crs.getString("soe_id"), insurenquiry_soe_id));
				Str.append(">").append(crs.getString("soe_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateSOB() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT sob_id, sob_name"
					+ " FROM " + compdb(comp_id) + "axela_sob"
					+ " INNER JOIN " + compdb(comp_id) + "axela_soe_trans ON soetrans_sob_id = sob_id "
					+ " WHERE 1 = 1"
					+ " AND soetrans_soe_id = " + insurenquiry_soe_id + ""
					+ " GROUP BY sob_id"
					+ " ORDER BY sob_name";
			// SOP("StrSql===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("sob_id")).append("");
				Str.append(StrSelectdrop(crs.getString("sob_id"), insurenquiry_sob_id));
				Str.append(">").append(crs.getString("sob_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCampaign(String insurenquiry_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT campaign_id, campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1 "
					+ " AND camptrans_branch_id = " + insurenquiry_branch_id
					+ " AND campaign_active = '1' "
					+ " AND SUBSTR(campaign_startdate, 1, 8) <= SUBSTR('" + ToLongDate((kknow())) + "', 1, 8) "
					+ " AND SUBSTR(campaign_enddate, 1, 8) >= SUBSTR('" + ToLongDate(kknow()) + "', 1, 8) "
					// + " AND branch_id =" + booking_veh_branch_id
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			// SOP("PopulateCampaign-------" + StrSql);
			// //SOP("insurance_veh_branch_id==" + insurenquiry_branch_id);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_insurenquiry_campaign_id\" id=\"dr_insurenquiry_campaign_id\" class=\"form-dropdown form-control\">\n");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("campaign_id"));
				Str.append(StrSelectdrop(crs.getString("campaign_id"), insurenquiry_campaign_id));
				Str.append(">").append(crs.getString("campaign_name")).append(" (");
				Str.append(strToShortDate(crs.getString("campaign_startdate"))).append(" - ")
						.append(strToShortDate(crs.getString("campaign_enddate"))).append(")</option>\n");
			}
			crs.close();
			Str.append("</select>\n");
			// //SOP("Str==" + Str);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

}
