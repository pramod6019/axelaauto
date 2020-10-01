package axela.preowned;
//Saiman 11th Feb 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.City_Check;
import cloudify.connect.Connect;
//import cloudify.connect.Connect_Pre Owned;

public class Preowned_Dash extends Connect {
	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String preowned_branch_id = "0";
	public String branch_name = "";
	public String preowned_id = "0";
	public String preowned_title = "";
	public String preowned_no = "";
	public String preowned_date = "";
	public String date = "";
	public String preowned_contact_id = "0";
	public String preowned_customer_id = "0";
	public String preowned_close_date = "";
	public String closedate = "";
	public String preowned_fcamt = "";
	public String preowned_noc = "";
	public String preowned_funding_bank = "";
	public String preowned_loan_no = "";
	public String contact_mobile1 = "";
	public String contact_email1 = "";
	public String contact_mobile2 = "";
	public String contact_email2 = "";
	public String preownedstage_name = "";
	public String contact_fname = "";
	public String contact_lname = "";
	public String contact_pin = "";
	public String contact_title_id = "0";
	public String contact_phone1 = "";
	public String contact_address = "";
	public String preowned_customer_name = "";
	public String preowned_desc = "";
	public String preowned_refno = "";
	public String preowned_dmsno = "";
	public String preowned_emp_id = "0";
	public String preowned_notes = "";
	public String preowned_prioritypreowned_id = "0";
	public String preowned_preownedstatus_id = "";
	public String preowned_preownedstatus_desc = "";
	public String preowned_preownedlostcase1_id = "0";
	public String preowned_preownedlostcase2_id = "0";
	public String preowned_preownedlostcase3_id = "0";
	public String preowned_status_date = "";
	public String statusdate = "";
	public String soe_name = "";
	public String sob_name = "";
	public String campaign_name = "";
	public int days_diff = 0;
	public String StrHTML = "";
	public String contact_city_id = "";
	public String preowned_entry_id = "0";
	public String preowned_entry_date = "";
	public String preowned_modified_id = "0";
	public String preowned_modified_date = "";
	public String entry_by = "", entry_date = "";
	public String modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	// public String preowned_preownedmodel_id = "0";
	public String preowned_variant_id = "0";
	public String preowned_insur_date = "";
	public String preowned_ownership_id = "";
	public String preowned_regdyear = "", preowned_manufyear = "";
	public String preowned_invoicevalue = "", preowned_kms = "";
	public String preowned_regno = "";
	public String preowned_regno1 = "";
	public String preowned_expectedprice = "";
	// public String preowned_quotedprice = "";
	public String preownedstage_id = "";
	public String preowned_fueltype_id = "0";
	public String preowned_sub_variant = "";
	public String preowned_extcolour_id = "0";
	public String preowned_intcolour_id = "0";
	public String preowned_options = "";
	public String preowned_insurance_id = "0";
	public String insurdate = "";
	public String config_preowned_refno = "";
	public String config_preowned_soe = "";
	public String config_preowned_sob = "";
	public String config_preowned_campaign = "";
	public String emp_role_id = "";
	public String returnperm = "";

	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				modelcheck.comp_id = comp_id;
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_preowned_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				// SOP(" preowned========="+preowned_id);
				PopulateConfigDetails();
				PopulateFields(response);
			}

			// SOP("emp_role_id=="+emp_role_id);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "select " + compdb(comp_id) + "axela_preowned.*, customer_name, contact_title_id, contact_fname, contact_lname, "
					+ " contact_mobile1, contact_mobile2, contact_phone1, contact_email1, contact_email2,"
					+ " contact_address, contact_city_id, contact_pin, branch_name, branch_code,"
					+ " coalesce(soe_name,'') as soe_name, coalesce(sob_name, '') as sob_name,"
					+ " coalesce(campaign_name, '') as campaign_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch on branch_id = preowned_branch_id "
					+ " LEFT JOIN " + compdb(comp_id) + "axela_soe on soe_id = preowned_soe_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sob on sob_id = preowned_sob_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_campaign on campaign_id = preowned_campaign_id"
					+ " WHERE preowned_id=" + preowned_id + BranchAccess + ExeAccess + " "
					+ " GROUP BY preowned_id ";
			// SOP("StrSql in PopulateFields--------------" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preowned_id = crs.getString("preowned_id");
					// preowned_preownedmodel_id = crs.getString("preowned_preownedmodel_id");
					preowned_variant_id = crs.getString("preowned_variant_id");
					// SOP("preowned_variant_id--------" + preowned_variant_id);
					preowned_manufyear = crs.getString("preowned_manufyear");
					preowned_regdyear = crs.getString("preowned_regdyear");
					// preowned_regdyear = strToMonthYear(preowned_regdyear);
					preowned_regno = crs.getString("preowned_regno");
					preowned_sub_variant = crs.getString("preowned_sub_variant");
					preowned_kms = crs.getString("preowned_kms");
					preowned_fcamt = crs.getString("preowned_fcamt");
					preowned_noc = crs.getString("preowned_noc");
					preowned_funding_bank = crs.getString("preowned_funding_bank");
					preowned_loan_no = crs.getString("preowned_loan_no");
					preowned_insur_date = crs.getString("preowned_insur_date");
					insurdate = strToShortDate(preowned_insur_date);
					preowned_ownership_id = crs.getString("preowned_ownership_id");
					preowned_invoicevalue = crs.getString("preowned_invoicevalue");
					preowned_expectedprice = crs.getString("preowned_expectedprice");
					// preowned_quotedprice = crs.getString("preowned_quotedprice");
					preowned_title = crs.getString("preowned_title");
					preowned_desc = crs.getString("preowned_desc");
					preowned_no = "PRE" + crs.getString("branch_code") + crs.getString("preowned_no");
					preowned_branch_id = crs.getString("preowned_branch_id");
					branch_name = crs.getString("branch_name");
					preowned_date = ConvertShortDateToStr(strToShortDate(crs.getString("preowned_date")));
					date = strToShortDate(preowned_date);
					preowned_close_date = crs.getString("preowned_close_date");
					closedate = strToShortDate(preowned_close_date);
					days_diff = (int) (getDaysBetween(ToShortDate(kknow()), preowned_close_date));
					// SOP("days_diff==here2=="+days_diff);
					preowned_no = crs.getString("preowned_no");
					preowned_emp_id = crs.getString("preowned_emp_id");
					preowned_customer_id = crs.getString("preowned_customer_id");
					preowned_contact_id = crs.getString("preowned_contact_id");
					preowned_preownedstatus_id = crs.getString("preowned_preownedstatus_id");
					statusdate = strToLongDate(preowned_status_date);
					preowned_preownedstatus_desc = crs.getString("preowned_preownedstatus_desc");
					preowned_preownedlostcase1_id = crs.getString("preowned_preownedlostcase1_id");
					preowned_preownedlostcase2_id = crs.getString("preowned_preownedlostcase2_id");
					preowned_preownedlostcase3_id = crs.getString("preowned_preownedlostcase3_id");
					preowned_prioritypreowned_id = crs.getString("preowned_prioritypreowned_id");
					preowned_extcolour_id = crs.getString("preowned_extcolour_id");
					preowned_intcolour_id = crs.getString("preowned_intcolour_id");
					preowned_fueltype_id = crs.getString("preowned_fueltype_id");
					preowned_insurance_id = crs.getString("preowned_insurance_id");
					preowned_options = crs.getString("preowned_options");
					preowned_notes = crs.getString("preowned_notes");
					preowned_customer_name = crs.getString("customer_name");
					soe_name = crs.getString("soe_name");
					sob_name = crs.getString("sob_name");
					campaign_name = crs.getString("campaign_name");
					contact_title_id = crs.getString("contact_title_id");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_phone1 = crs.getString("contact_phone1");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_email1 = crs.getString("contact_email1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_email2 = crs.getString("contact_email2");
					contact_address = crs.getString("contact_address");
					contact_pin = crs.getString("contact_pin");
					contact_city_id = crs.getString("contact_city_id");
					preowned_entry_id = crs.getString("preowned_entry_id");
					entry_by = Exename(comp_id, crs.getInt("preowned_entry_id"));
					entry_date = strToLongDate(crs.getString("preowned_entry_date"));
					preowned_modified_id = crs.getString("preowned_modified_id");
					// preowned_regno1 = crs.getString("preowned_regno");
					// SOP("preowned_regno1==="+SplitRegNo(preowned_regno1, 2));

					if (!preowned_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(preowned_modified_id));
						modified_date = strToLongDate(crs.getString("preowned_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Pre Owned!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " where 1 = 1"
					+ " AND emp_preowned = 1"
					+ " AND emp_active = 1 "
					+ " AND (emp_branch_id = " + preowned_branch_id + " or emp_id = 1 "
					+ " OR emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id AND empbr.emp_branch_id=" + preowned_branch_id + "))"
					+ ExeAccess
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			// SOP("StrSql--"+StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(StrSelectdrop(crs.getString("emp_id"), preowned_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateContactTitle() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc "
					+ " FROM " + compdb(comp_id) + "axela_title "
					+ " WHERE 1 = 1 "
					+ " ORDER BY title_desc ";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id")).append("");
				Str.append(StrSelectdrop(crs.getString("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateStatus(String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT preownedstatus_id, preownedstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_status "
					+ " WHERE 1 = 1 "
					+ " ORDER BY preownedstatus_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateCountry==========" + StrSql);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedstatus_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedstatus_id"), preowned_preownedstatus_id));
				Str.append(">").append(crs.getString("preownedstatus_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePreownedPriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT prioritypreowned_id, prioritypreowned_desc, prioritypreowned_duehrs"
					+ " FROM " + compdb(comp_id) + "axela_preowned_priority"
					+ " WHERE 1 = 1"
					+ " ORDER BY prioritypreowned_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option  value=").append(crs.getString("prioritypreowned_id")).append("");
				Str.append(StrSelectdrop(crs.getString("prioritypreowned_id"), preowned_prioritypreowned_id));
				Str.append(">").append(crs.getString("prioritypreowned_desc")).append(" (").append(crs.getString("prioritypreowned_duehrs")).append(")</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
		return Str.toString();
	}

	public String PopulateLostCase1(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlostcase1_id,  preownedlostcase1_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase1"
					+ " WHERE 1 = 1"
					+ " ORDER BY preownedlostcase1_name";
			// SOP("strstql1=======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=")
						.append(crs.getString("preownedlostcase1_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedlostcase1_id"), preowned_preownedlostcase1_id));
				Str.append(">").append(crs.getString("preownedlostcase1_name"))
						.append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase2(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlostcase2_id, preownedlostcase2_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase2"
					+ " WHERE 1 = 1"
					+ " ORDER BY preownedlostcase2_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlostcase2_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedlostcase2_id"), preowned_preownedlostcase2_id));
				Str.append(">").append(crs.getString("preownedlostcase2_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateLostCase3(String comp_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlostcase3_id, preownedlostcase3_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_lostcase3"
					+ " WHERE 1 = 1"
					+ " ORDER BY preownedlostcase3_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlostcase3_id")).append("");
				Str.append(StrSelectdrop(crs.getString("preownedlostcase3_id"), preowned_preownedlostcase3_id));
				Str.append(">").append(crs.getString("preownedlostcase3_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateVariant() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " WHERE 1 = 1"
					// + " and variant_preownedmodel_id = " + preowned_preownedmodel_id
					+ " ORDER BY variant_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id")).append("");
				Str.append(StrSelectdrop(crs.getString("variant_id"), preowned_variant_id));
				Str.append(">").append(crs.getString("variant_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id,  fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " ORDER BY fueltype_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("fueltype_id"), preowned_fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateOwnership() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT ownership_id, ownership_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_ownership"
					+ " ORDER BY ownership_id";
			// SOP("StrSql PopulateOwnership = " + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("ownership_id")).append("");
				Str.append(StrSelectdrop(crs.getString("ownership_id"), preowned_ownership_id));
				Str.append(">").append(crs.getString("ownership_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateNoc() {

		StringBuilder Str = new StringBuilder();
		Str.append("<option value=0>Select</option>");
		Str.append("<option value=1").append(StrSelectdrop("1", preowned_noc)).append(">Yes</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", preowned_noc)).append(">No</option>\n");
		return Str.toString();
	}

	public String PopulateInsuranceType() {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT insurance_id, insurance_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_insurance"
					+ " ORDER BY insurance_name";
			// SOP("StrSql PopulateOwnership = " + StrSql);
			Str.append("<option value=0>Select</option>");
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("insurance_id")).append("");
				Str.append(StrSelectdrop(crs.getString("insurance_id"), preowned_insurance_id));
				Str.append(">").append(crs.getString("insurance_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateExterior() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT extcolour_id, extcolour_name"
					+ " FROM axela_preowned_extcolour"
					+ " WHERE extcolour_id >= 0"
					+ " GROUP BY extcolour_id"
					+ " ORDER BY extcolour_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("extcolour_id")).append("");
				Str.append(StrSelectdrop(crs.getString("extcolour_id"), preowned_extcolour_id));
				Str.append(">").append(crs.getString("extcolour_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateInterior() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT intcolour_id, intcolour_name"
					+ " FROM axela_preowned_intcolour"
					+ " WHERE intcolour_id >= 0"
					+ " GROUP BY intcolour_id"
					+ " ORDER BY intcolour_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("intcolour_id")).append("");
				Str.append(StrSelectdrop(crs.getString("intcolour_id"), preowned_intcolour_id));
				Str.append(">").append(crs.getString("intcolour_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT config_preowned_refno, config_preowned_soe, config_preowned_sob, config_preowned_campaign "
				+ " FROM " + compdb(comp_id) + "axela_config," + compdb(comp_id) + "axela_emp "
				+ " WHERE 1 = 1"
				+ " AND emp_id = " + emp_id + "";
		// SOP(StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				config_preowned_refno = crs.getString("config_preowned_refno");
				config_preowned_soe = crs.getString("config_preowned_soe");
				config_preowned_sob = crs.getString("config_preowned_sob");
				config_preowned_campaign = crs.getString("config_preowned_campaign");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateManufYear(String preowned_manufyear) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =-1>Select</option>");
		for (int i = Integer
				.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow())))); i >= 1996; i--) {
			Str.append("<option value =").append(i).append("");
			Str.append(Selectdrop(i, preowned_manufyear)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}

	public String PopulateRegYear(String preowned_regdyear) {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value =-1>Select</option>");
		for (int i = Integer
				.parseInt(SplitYear(ConvertShortDateToStr(DateToShortDate(kknow())))); i >= 1996; i--) {
			Str.append("<option value =").append(i).append("");
			Str.append(Selectdrop(i, preowned_regdyear)).append(">").append(i);
			Str.append("</option>\n");
		}
		return Str.toString();
	}
}
