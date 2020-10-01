package axela.preowned;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Dash_Add extends Connect {

	public String StrSql = "";
	public String StrHTML = "";
	public String StrHTML1 = "";
	public String enquiry_id = "0";
	public String comp_id = "0";
	public String emp_id = "0";
	public String preowned_branch_id = "0";
	public String preowned_id = "0";
	public String preowned_regno = "";
	public String preowned_manufyear = "";
	public String preowned_kms = "";
	public String preowned_ownership_id = "";

	CachedRowSet crs = null;
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			if (!PadQuotes(request.getParameter("enquiry_id")).equals("")) {
				enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
				preowned_regno = PadQuotes(request.getParameter("preowned_regno"));
				preowned_manufyear = CNumeric(PadQuotes(request.getParameter("preowned_manufyear")));
				preowned_kms = CNumeric(PadQuotes(request.getParameter("preowned_kms")));
				preowned_ownership_id = CNumeric(PadQuotes(request.getParameter("preowned_ownership")));
			}

			SOP("preowned_regno==" + preowned_regno);
			SOP("preowned_manufyear==" + preowned_manufyear);
			SOP("preowned_kms==" + preowned_kms);
			SOP("preowned_ownership_id==" + preowned_ownership_id);

			comp_id = (CNumeric(GetSession("comp_id", request)));
			emp_id = CNumeric(GetSession("emp_id", request));
			if (!enquiry_id.equals("0")) {

				StrSql = " SELECT enquiry_id, enquiry_customer_id, enquiry_soe_id, enquiry_contact_id, enquiry_branch_id, enquiry_emp_id, "
						+ " COALESCE(preowned_enquiry_id, 0) AS preowned_enquiry_id,"
						+ " COALESCE(enquiry_tradein_preownedvariant_id,0) AS  enquiry_tradein_preownedvariant_id,"
						+ " COALESCE(team_preownedbranch_id, 0) AS team_preownedbranch_id, "
						+ " COALESCE(team_preownedemp_id, 0) AS team_preownedemp_id, "
						+ " COALESCE(preownedteamtrans_team_id,0) AS preownedteamtrans_team_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_emp_id = enquiry_emp_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_team ON team_id = teamtrans_team_id"
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned ON preowned_enquiry_id = enquiry_id "
						+ " LEFT JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_emp_id = team_preownedemp_id"
						+ " WHERE enquiry_id = " + enquiry_id;
				SOP("StrSql------s-Preowned_Dash_Add----------" + StrSql);
				crs = processQuery(StrSql, 0);
				while (crs.next()) {
					StrHTML = "";
					if (crs.getString("team_preownedbranch_id").equals("0")) {
						StrHTML += "<br><font color=red>Pre-Owned Branch is not configured for this Team!</font>";
					}
					if (crs.getString("team_preownedemp_id").equals("0")) {
						StrHTML += "<br><font color=red>Pre-Owned Consultant is not configured for this Team!</font>";
					}
					if (crs.getString("enquiry_tradein_preownedvariant_id").equals("0")) {
						StrHTML += "<br><font color=red>Trade-In Model is not configured for this Enquiry!</font>";
					}
					if (!crs.getString("preowned_enquiry_id").equals("0")) {
						StrHTML += "<br><font color=red>Pre-Owned Enquiry is already present for this Enquiry!</font>";
					}

					if (preowned_regno.equals("") && StrHTML.equals("") && comp_id.equals("1009")) {
						StrHTML1 += "<br><font color=red>Pre-Owned Reg. No is Empty!</font>";
					}
					if (preowned_manufyear.equals("0") && StrHTML.equals("") && comp_id.equals("1009")) {
						StrHTML1 += "<br><font color=red>Pre-Owned Manuf Year is not Selected!</font>";
					}
					if (preowned_kms.equals("0") && StrHTML.equals("") && comp_id.equals("1009")) {
						StrHTML1 += "<br><font color=red>Pre-Owned Kms is Empty!</font>";
					}
					if (preowned_ownership_id.equals("0") && StrHTML.equals("") && comp_id.equals("1009")) {
						StrHTML1 += "<br><font color=red>Pre-Owned Ownership is not Selected!</font>";
					}

					if (StrHTML.equals("")) {
						StrHTML = StrHTML + StrHTML1;
					}

					if (StrHTML.equals(""))
					{
						Preowned_Quickadd preowned = new Preowned_Quickadd();
						preowned.comp_id = comp_id;
						preowned.emp_id = emp_id;
						preowned.preowned_branch_id = crs.getString("team_preownedbranch_id");
						preowned.preowned_customer_id = crs.getString("enquiry_customer_id");
						preowned.preowned_contact_id = crs.getString("enquiry_contact_id");
						preowned.preowned_title = "New Pre-Owned";
						preowned.preowned_sub_variant = "";
						preowned.preowned_extcolour_id = "0";
						preowned.preowned_intcolour_id = "0";
						preowned.preowned_options = "";
						preowned.preowned_date = strToShortDate(ToShortDate(kknow()));
						preowned.preowned_variant_id = crs.getString("enquiry_tradein_preownedvariant_id");
						preowned.preowned_fcamt = "0";
						preowned.preowned_noc = "";
						preowned.preowned_funding_bank = "";
						preowned.preowned_loan_no = "";
						preowned.preowned_insur_date = "";
						preowned.preowned_insurance_id = "0";
						preowned.preowned_ownership_id = preowned_ownership_id;
						preowned.preowned_regdyear = "";
						preowned.preowned_manufyear = preowned_manufyear;
						preowned.preowned_invoicevalue = "0";
						preowned.preowned_kms = preowned_kms;
						preowned.preowned_regno = preowned_regno;
						preowned.preowned_expectedprice = "0";
						preowned.preowned_quotedprice = "0";
						preowned.preowned_fueltype_id = "0";
						preowned.preowned_close_date = strToShortDate(ToLongDate(kknow()));
						preowned.preowned_emp_id = crs.getString("team_preownedemp_id");
						preowned.preowned_sales_emp_id = crs.getString("enquiry_emp_id");
						preowned.preowned_enquiry_id = enquiry_id;
						preowned.preowned_preownedstatus_id = "1";
						preowned.preowned_preownedstatus_date = "";
						preowned.preowned_preownedstatus_desc = "";
						preowned.preowned_prioritypreowned_id = "1";
						preowned.preownedteam_id = crs.getString("preownedteamtrans_team_id");
						preowned.preowned_soe_id = crs.getString("enquiry_soe_id");
						preowned.preowned_notes = "";
						preowned.preowned_desc = "";
						preowned.preowned_entry_id = emp_id;
						preowned.preowned_entry_date = ToLongDate(kknow());
						preowned.PopulateConfigDetails();
						preowned.PopulateContactCustomerDetails();
						preowned.AddPreownedFields();
						SOP("preowned=123==" + preowned.preowned_id);

						if (!preowned.preowned_id.equals("0")) {
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history "
									+ "(history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " values"
									+ " (" + enquiry_id + ", "
									+ emp_id + ", "
									+ "'" + ToLongDate(kknow()) + "', "
									+ "'ADD EVALUATION', "
									+ "'', "
									+ "CONCAT('Pre-Owned Enquiry added For Enquiry ID:'," + enquiry_id + "))";
							// SOP("StrSql=--------history--------=" + StrSql);
							updateQuery(StrSql);
						}

						StrHTML = preowned.msg;
						preowned_id = preowned.preowned_id;
						if (!preowned_id.equals("0")) {
							StrHTML += "<br><font color=red><a href=../preowned/preowned-list.jsp?preowned_id=" + preowned_id + ">Pre-Owned added successfully!</a></font>";
						}
					}
				}
				crs.close();
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
