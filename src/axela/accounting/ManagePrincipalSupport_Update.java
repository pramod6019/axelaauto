package axela.accounting;
//Divya 3rd Oct

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import axela.sales.MIS_Check1;
import axela.sales.Veh_Salesorder_Update;
import cloudify.connect.Connect;

public class ManagePrincipalSupport_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String model_id, fueltype_id;
	public String principalsupport_id = "";
	public String brand_id = "0";
	public String principalsupport_model_id = "";
	public String principalsupport_fueltype_id = "";
	public String principalsupport_month = "";

	public String principalsupport_customer3rdyearextwty = "";
	public String principalsupport_customerrsa = "";
	public String principalsupport_customerinsurance = "";
	public String principalsupport_customercashdiscount = "";
	public String principalsupport_customerexchange = "";
	public String principalsupport_customerloyalty = "";
	public String principalsupport_customergovtempscheme = "";
	public String principalsupport_customerotherbenefit = "";
	public String principalsupport_customercorporate = "";
	public String principalsupport_extwty = "";
	public String principalsupport_rsa = "";
	public String principalsupport_insurance = "";
	public String principalsupport_cashdiscount = "";
	public String principalsupport_exchange = "";
	public String principalsupport_loyalty = "";
	public String principalsupport_govtempscheme = "";
	public String principalsupport_monthlyadnbenefit = "";
	public String principalsupport_corporate = "";
	public String principalsupport_eb1_fromdate = "";
	public String principalsupport_eb1_todate = "";
	public String principalsupport_eb1_wholesaletargetcount = "";
	public String principalsupport_eb1_wholesaletargetperc = "";
	public String principalsupport_eb1_retailtargetcount = "";
	public String principalsupport_eb1_retailtargetperc = "";
	public String principalsupport_eb2_fromdate = "";
	public String principalsupport_eb2_todate = "";
	public String principalsupport_eb2_wholesaletargetcount = "";
	public String principalsupport_eb2_wholesaletargetperc = "";
	public String principalsupport_eb2_retailtargetcount = "";
	public String principalsupport_eb2_retailtargetperc = "";

	public String principalsupport_entry_id = "0";
	public String principalsupport_entry_date = "";
	public String principalsupport_modified_id = "0";
	public String principalsupport_modified_date = "";
	public String BranchAccess = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_id = "";
	public String comp_id = "";
	public String emp_role_id = "";
	public String QueryString = "";

	public MIS_Check1 mischeck = new MIS_Check1();
	Veh_Salesorder_Update vehsoupdate = new Veh_Salesorder_Update();
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			comp_id = GetSession("comp_id", request);
			CheckPerm(comp_id, "emp_principal_support_add", request, response);
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request).toString();
				emp_id = GetSession("emp_id", request);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				QueryString = PadQuotes(request.getQueryString());
				msg = PadQuotes(request.getParameter("msg"));
				principalsupport_id = PadQuotes(request.getParameter("principalsupport_id"));

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					status = "Add";
					if ("yes".equals(addB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_principal_support_add", request).equals("1")) {
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manageprincipalsupport.jsp?principalsupport_id=" + principalsupport_id
										+ "&msg=Support Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Principal Support".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Principal Support".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_principal_support_edit", request).equals("1")) {
							UpdateFields(request);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manageprincipalsupport.jsp?update=yes&principalsupport_id=" + principalsupport_id
										+ "&msg=Principal Support Updated Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if ("Delete Principal Support".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_principal_support_delete", request).equals("1")) {
							DeleteFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("manageprincipalsupport.jsp?msg=Support Deleted Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		brand_id = CNumeric(PadQuotes(request.getParameter("dr_brand")));
		principalsupport_model_id = CNumeric(PadQuotes(request.getParameter("dr_model")));
		principalsupport_fueltype_id = CNumeric(PadQuotes(request.getParameter("dr_fuel_type_id")));
		principalsupport_month = PadQuotes(request.getParameter("dr_month"));

		principalsupport_customer3rdyearextwty = CNumeric(PadQuotes(request.getParameter("txt_customer3rdyearextwty")));
		principalsupport_customerrsa = CNumeric(PadQuotes(request.getParameter("txt_customerrsa")));
		principalsupport_customerinsurance = CNumeric(PadQuotes(request.getParameter("txt_customerinsurance")));
		principalsupport_customercashdiscount = CNumeric(PadQuotes(request.getParameter("txt_customercashdiscount")));
		principalsupport_customerexchange = CNumeric(PadQuotes(request.getParameter("txt_customerexchange")));
		principalsupport_customerloyalty = CNumeric(PadQuotes(request.getParameter("txt_customerloyalty")));
		principalsupport_customergovtempscheme = CNumeric(PadQuotes(request.getParameter("txt_customergovtempscheme")));
		principalsupport_customerotherbenefit = CNumeric(PadQuotes(request.getParameter("txt_customerotherbenefit")));
		principalsupport_customercorporate = CNumeric(PadQuotes(request.getParameter("txt_customercorporate")));
		principalsupport_extwty = CNumeric(PadQuotes(request.getParameter("txt_extwty")));
		principalsupport_rsa = CNumeric(PadQuotes(request.getParameter("txt_rsa")));
		principalsupport_insurance = CNumeric(PadQuotes(request.getParameter("txt_insurance")));
		principalsupport_cashdiscount = CNumeric(PadQuotes(request.getParameter("txt_cash_discount")));
		principalsupport_exchange = CNumeric(PadQuotes(request.getParameter("txt_exchange")));
		principalsupport_loyalty = CNumeric(PadQuotes(request.getParameter("txt_loyalty")));
		principalsupport_govtempscheme = CNumeric(PadQuotes(request.getParameter("txt_govtempscheme")));
		principalsupport_monthlyadnbenefit = CNumeric(PadQuotes(request.getParameter("txt_monthlyadnbenefit")));
		principalsupport_corporate = CNumeric(PadQuotes(request.getParameter("txt_corporate")));
		principalsupport_eb1_fromdate = PadQuotes(request.getParameter("txt_eb1_fromdate"));
		principalsupport_eb1_todate = PadQuotes(request.getParameter("txt_eb1_todate"));
		principalsupport_eb1_wholesaletargetcount = CNumeric(PadQuotes(request.getParameter("txt_eb1_wholesaletargetcount")));
		principalsupport_eb1_wholesaletargetperc = CNumeric(PadQuotes(request.getParameter("txt_eb1_wholesaletargetperc")));
		principalsupport_eb1_retailtargetcount = CNumeric(PadQuotes(request.getParameter("txt_eb1_retailtargetcount")));
		principalsupport_eb1_retailtargetperc = CNumeric(PadQuotes(request.getParameter("txt_eb1_retailtargetperc")));
		principalsupport_eb2_fromdate = PadQuotes(request.getParameter("txt_eb2_fromdate"));
		principalsupport_eb2_todate = PadQuotes(request.getParameter("txt_eb2_todate"));
		principalsupport_eb2_wholesaletargetcount = CNumeric(PadQuotes(request.getParameter("txt_eb2_wholesaletargetcount")));
		principalsupport_eb2_wholesaletargetperc = CNumeric(PadQuotes(request.getParameter("txt_eb2_wholesaletargetperc")));
		principalsupport_eb2_retailtargetcount = CNumeric(PadQuotes(request.getParameter("txt_eb2_retailtargetcount")));
		principalsupport_eb2_retailtargetperc = CNumeric(PadQuotes(request.getParameter("txt_eb2_retailtargetperc")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = ToLongDate(kknow());
		// SOP("modified_date==" + modified_date);
	}

	protected void CheckForm(HttpServletRequest request) {
		msg = "";
		if (brand_id.equals("0")) {
			msg += "<br>Select Brand";
		}
		if (principalsupport_model_id.equals("0")) {
			msg += "<br>Select Model";
		}
		if (principalsupport_fueltype_id.equals("0")) {
			msg += "<br>Select Fuel Type";
		}
		if (principalsupport_month.equals("0")) {
			msg += "<br>Select Month";
		}
		if (!principalsupport_eb1_fromdate.equals("")) {
			principalsupport_eb1_fromdate = ConvertShortDateToStr(principalsupport_eb1_fromdate);
		}
		if (!principalsupport_eb1_todate.equals("")) {
			principalsupport_eb1_todate = ConvertShortDateToStr(principalsupport_eb1_todate);
		}

		if (!principalsupport_eb1_fromdate.equals("") && !principalsupport_eb1_todate.equals("")) {
			if (Double.parseDouble(principalsupport_eb1_fromdate) > Double.parseDouble(principalsupport_eb1_todate)) {
				msg += "<br>EB1 From Date cannot be Greater than EB1 To Date";
			}
		}
		if (!principalsupport_eb2_fromdate.equals("")) {
			principalsupport_eb2_fromdate = ConvertShortDateToStr(principalsupport_eb2_fromdate);
		}
		if (!principalsupport_eb2_todate.equals("")) {
			principalsupport_eb2_todate = ConvertShortDateToStr(principalsupport_eb2_todate);
		}
		if (!principalsupport_eb2_fromdate.equals("") && !principalsupport_eb2_todate.equals("")) {
			if (Double.parseDouble(principalsupport_eb2_fromdate) > Double.parseDouble(principalsupport_eb2_todate)) {
				msg += "<br>EB2 From Date cannot be Greater than EB2 To Date";
			}
		}

	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		StrSql = "SELECT COUNT(principalsupport_id)"
				+ " FROM " + compdb(comp_id) + "axela_principal_support"
				+ " WHERE principalsupport_brand_id = " + brand_id
				+ " AND principalsupport_model_id = " + principalsupport_model_id
				+ " AND principalsupport_fueltype_id = " + principalsupport_fueltype_id
				+ " AND principalsupport_month = '" + SplitYear(ToLongDate(kknow())) + principalsupport_month + "01000000'";
		String principalsupport_id = CNumeric(ExecuteQuery(StrSql));

		if (!principalsupport_id.equals("0")) {
			msg += "<br/>Principal Support Already Found!<br/>";
		}
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_principal_support"
						+ " (principalsupport_id,"
						+ " principalsupport_brand_id,"
						+ " principalsupport_model_id,"
						+ " principalsupport_fueltype_id,"
						+ " principalsupport_month,"

						+ " principalsupport_customer3rdyearextwty,"
						+ " principalsupport_customerrsa,"
						+ " principalsupport_customerinsurance,"
						+ " principalsupport_customercashdiscount,"
						+ " principalsupport_customerexchange,"
						+ " principalsupport_customerloyalty,"
						+ " principalsupport_customergovtempscheme,"
						+ " principalsupport_customerotherbenefit,"
						+ " principalsupport_customercorporate,"
						+ " principalsupport_extwty,"
						+ " principalsupport_rsa,"
						+ " principalsupport_insurance,"
						+ " principalsupport_cashdiscount,"
						+ " principalsupport_exchange,"
						+ " principalsupport_loyalty,"
						+ " principalsupport_govtempscheme,"
						+ " principalsupport_monthlyadnbenefit,"
						+ " principalsupport_corporate,"
						+ " principalsupport_eb1_fromdate,"
						+ " principalsupport_eb1_todate,"
						+ " principalsupport_eb1_wholesaletargetcount,"
						+ " principalsupport_eb1_wholesaletargetperc,"
						+ " principalsupport_eb1_retailtargetcount,"
						+ " principalsupport_eb1_retailtargetperc,"
						+ " principalsupport_eb2_fromdate,"
						+ " principalsupport_eb2_todate,"
						+ " principalsupport_eb2_wholesaletargetcount,"
						+ " principalsupport_eb2_wholesaletargetperc,"
						+ " principalsupport_eb2_retailtargetcount,"
						+ " principalsupport_eb2_retailtargetperc,"

						+ " principalsupport_entry_id,"
						+ " principalsupport_entry_date)"
						+ " VALUES"
						+ " ('" + principalsupport_id + "',"
						+ " '" + brand_id + "',"
						+ " '" + principalsupport_model_id + "',"
						+ " '" + principalsupport_fueltype_id + "',"
						+ " '" + SplitYear(ToLongDate(kknow())) + principalsupport_month + "01000000" + "',"

						+ " " + principalsupport_customer3rdyearextwty + ","
						+ " " + principalsupport_customerrsa + ","
						+ " " + principalsupport_customerinsurance + ","
						+ " " + principalsupport_customercashdiscount + ","
						+ " " + principalsupport_customerexchange + ","
						+ " " + principalsupport_customerloyalty + ","
						+ " " + principalsupport_customergovtempscheme + ","
						+ " " + principalsupport_customerotherbenefit + ","
						+ " " + principalsupport_customercorporate + ","
						+ " " + principalsupport_extwty + ","
						+ " " + principalsupport_rsa + ","
						+ " " + principalsupport_insurance + ","
						+ " '" + principalsupport_cashdiscount + "',"
						+ " " + principalsupport_exchange + ","
						+ " " + principalsupport_loyalty + ","
						+ " " + principalsupport_govtempscheme + ","
						+ " " + principalsupport_monthlyadnbenefit + ","
						+ " " + principalsupport_corporate + ","
						+ " '" + principalsupport_eb1_fromdate + "',"
						+ " '" + principalsupport_eb1_todate + "',"
						+ " " + principalsupport_eb1_wholesaletargetcount + ","
						+ " " + principalsupport_eb1_wholesaletargetperc + ","
						+ " " + principalsupport_eb1_retailtargetcount + ","
						+ " " + principalsupport_eb1_retailtargetperc + ","
						+ " '" + principalsupport_eb2_fromdate + "',"
						+ " '" + principalsupport_eb2_todate + "',"
						+ " " + principalsupport_eb2_wholesaletargetcount + ","
						+ " " + principalsupport_eb2_wholesaletargetperc + ","
						+ " " + principalsupport_eb2_retailtargetcount + ","
						+ " " + principalsupport_eb2_retailtargetperc + ","

						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "' )";

				// SOP("StrSql===" + StrSql);
				updateQuery(StrSql);

				// profitability calculation for all so associted with stock of this type and of this dms month
				vehsoupdate.profitability_principalsupport_model_id = principalsupport_model_id;
				vehsoupdate.profitability_principalsupport_fueltype_id = principalsupport_fueltype_id;
				vehsoupdate.profitability_principalsupport_month = SplitYear(ToLongDate(kknow())) + principalsupport_month;
				vehsoupdate.UpdateProfitability("0", comp_id);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = " SELECT principalsupport_id, principalsupport_brand_id, principalsupport_model_id, principalsupport_fueltype_id,"
					+ " principalsupport_month, principalsupport_customer3rdyearextwty, principalsupport_customerrsa,"
					+ " principalsupport_customerinsurance, principalsupport_customercashdiscount, principalsupport_customerexchange, principalsupport_customerloyalty,"
					+ " principalsupport_customergovtempscheme, principalsupport_customerotherbenefit, principalsupport_customercorporate, "
					+ " principalsupport_extwty, principalsupport_rsa,"
					+ " principalsupport_insurance, principalsupport_cashdiscount, principalsupport_exchange, principalsupport_loyalty, principalsupport_govtempscheme,"
					+ " principalsupport_monthlyadnbenefit, principalsupport_corporate, principalsupport_eb1_fromdate, principalsupport_eb1_todate, principalsupport_eb1_wholesaletargetcount,"
					+ " principalsupport_eb1_wholesaletargetperc, principalsupport_eb1_retailtargetcount, principalsupport_eb1_retailtargetperc, principalsupport_eb2_fromdate, principalsupport_eb2_todate,"
					+ " principalsupport_eb2_wholesaletargetcount, principalsupport_eb2_wholesaletargetperc, principalsupport_eb2_retailtargetcount, principalsupport_eb2_retailtargetperc,"

					+ " principalsupport_entry_id, principalsupport_entry_date, principalsupport_modified_id, principalsupport_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_principal_support"
					+ " WHERE principalsupport_id = " + principalsupport_id;
			// SOP("pop==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					principalsupport_id = crs.getString("principalsupport_id");
					brand_id = crs.getString("principalsupport_brand_id");
					principalsupport_model_id = crs.getString("principalsupport_model_id");
					principalsupport_fueltype_id = crs.getString("principalsupport_fueltype_id");
					principalsupport_month = crs.getString("principalsupport_month");
					principalsupport_customer3rdyearextwty = crs.getString("principalsupport_customer3rdyearextwty");
					principalsupport_customerrsa = crs.getString("principalsupport_customerrsa");
					principalsupport_customerinsurance = crs.getString("principalsupport_customerinsurance");
					principalsupport_customercashdiscount = crs.getString("principalsupport_customercashdiscount");
					principalsupport_customerexchange = crs.getString("principalsupport_customerexchange");
					principalsupport_customerloyalty = crs.getString("principalsupport_customerloyalty");
					principalsupport_customergovtempscheme = crs.getString("principalsupport_customergovtempscheme");
					principalsupport_customerotherbenefit = crs.getString("principalsupport_customerotherbenefit");
					principalsupport_customercorporate = crs.getString("principalsupport_customercorporate");
					principalsupport_extwty = crs.getString("principalsupport_extwty");
					principalsupport_rsa = crs.getString("principalsupport_rsa");
					principalsupport_insurance = crs.getString("principalsupport_insurance");
					principalsupport_cashdiscount = crs.getString("principalsupport_cashdiscount");
					principalsupport_exchange = crs.getString("principalsupport_exchange");
					principalsupport_loyalty = crs.getString("principalsupport_loyalty");
					principalsupport_govtempscheme = crs.getString("principalsupport_govtempscheme");
					principalsupport_monthlyadnbenefit = crs.getString("principalsupport_monthlyadnbenefit");
					principalsupport_corporate = crs.getString("principalsupport_corporate");
					principalsupport_eb1_fromdate = strToShortDate(crs.getString("principalsupport_eb1_fromdate"));
					principalsupport_eb1_todate = strToShortDate(crs.getString("principalsupport_eb1_todate"));
					principalsupport_eb1_wholesaletargetcount = crs.getString("principalsupport_eb1_wholesaletargetcount");
					principalsupport_eb1_wholesaletargetperc = crs.getString("principalsupport_eb1_wholesaletargetperc");
					principalsupport_eb1_retailtargetcount = crs.getString("principalsupport_eb1_retailtargetcount");
					principalsupport_eb1_retailtargetperc = crs.getString("principalsupport_eb1_retailtargetperc");
					principalsupport_eb2_fromdate = strToShortDate(crs.getString("principalsupport_eb2_fromdate"));
					principalsupport_eb2_todate = strToShortDate(crs.getString("principalsupport_eb2_todate"));
					principalsupport_eb2_wholesaletargetcount = crs.getString("principalsupport_eb2_wholesaletargetcount");
					principalsupport_eb2_wholesaletargetperc = crs.getString("principalsupport_eb2_wholesaletargetperc");
					principalsupport_eb2_retailtargetcount = crs.getString("principalsupport_eb2_retailtargetcount");
					principalsupport_eb2_retailtargetperc = crs.getString("principalsupport_eb2_retailtargetperc");
					principalsupport_entry_id = crs.getString("principalsupport_entry_id");
					principalsupport_entry_date = crs.getString("principalsupport_entry_date");
					entry_by = Exename(comp_id, crs.getInt("principalsupport_entry_id"));
					entry_date = strToLongDate(crs.getString("principalsupport_entry_date"));
					modified_by = Exename(comp_id, crs.getInt("principalsupport_modified_id"));
					modified_date = strToLongDate(crs.getString("principalsupport_modified_date"));

				}

			} else {
				msg = "msg=Invalid Principal Support!";
				response.sendRedirect("../portal/error.jsp?" + msg);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request) throws Exception {
		CheckForm(request);
		StrSql = "SELECT COUNT(principalsupport_id)"
				+ " FROM " + compdb(comp_id) + "axela_principal_support"
				+ " WHERE principalsupport_brand_id = " + brand_id
				+ " AND principalsupport_model_id = " + principalsupport_model_id
				+ " AND principalsupport_fueltype_id = " + principalsupport_fueltype_id
				+ " AND SUBSTR(principalsupport_month,1,6) = '" + SplitYear(ToLongDate(kknow())) + principalsupport_month + "'"
				+ " AND principalsupport_id != " + principalsupport_id;
		String supportid = CNumeric(ExecuteQuery(StrSql));

		// SOP("StrSql===Update==" + StrSql);

		if (!supportid.equals("0")) {
			msg += "<br/>Support Already Found!<br/>";
		}

		if (msg.equals("")) {
			try {
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_principal_support"
						+ " SET"
						+ " principalsupport_brand_id = " + brand_id + ","
						+ " principalsupport_model_id = " + principalsupport_model_id + ","
						+ " principalsupport_fueltype_id = " + principalsupport_fueltype_id + ","
						+ " principalsupport_month = '" + SplitYear(ToLongDate(kknow())) + principalsupport_month + "01000000" + "',"

						+ " principalsupport_customer3rdyearextwty = " + principalsupport_customer3rdyearextwty + ","
						+ " principalsupport_customerrsa = " + principalsupport_customerrsa + ","
						+ " principalsupport_customerinsurance = " + principalsupport_customerinsurance + ","
						+ " principalsupport_customercashdiscount = " + principalsupport_customercashdiscount + ","
						+ " principalsupport_customerexchange = " + principalsupport_customerexchange + ","
						+ " principalsupport_customerloyalty = " + principalsupport_customerloyalty + ","
						+ " principalsupport_customergovtempscheme = " + principalsupport_customergovtempscheme + ","
						+ " principalsupport_customerotherbenefit = " + principalsupport_customerotherbenefit + ","
						+ " principalsupport_customercorporate = " + principalsupport_customercorporate + ","
						+ " principalsupport_extwty = " + principalsupport_extwty + ","
						+ " principalsupport_rsa = " + principalsupport_rsa + ","
						+ " principalsupport_insurance = " + principalsupport_insurance + ","
						+ " principalsupport_cashdiscount = " + principalsupport_cashdiscount + ","
						+ " principalsupport_exchange = " + principalsupport_exchange + ","
						+ " principalsupport_loyalty = " + principalsupport_loyalty + ","
						+ " principalsupport_govtempscheme = " + principalsupport_govtempscheme + ","
						+ " principalsupport_monthlyadnbenefit = " + principalsupport_monthlyadnbenefit + ","
						+ " principalsupport_corporate = " + principalsupport_corporate + ","
						+ " principalsupport_eb1_fromdate = '" + principalsupport_eb1_fromdate + "',"
						+ " principalsupport_eb1_todate = '" + principalsupport_eb1_todate + "',"
						+ " principalsupport_eb1_wholesaletargetcount = " + principalsupport_eb1_wholesaletargetcount + ","
						+ " principalsupport_eb1_wholesaletargetperc = " + principalsupport_eb1_wholesaletargetperc + ","
						+ " principalsupport_eb1_retailtargetcount = " + principalsupport_eb1_retailtargetcount + ","
						+ " principalsupport_eb1_retailtargetperc = " + principalsupport_eb1_retailtargetperc + ","
						+ " principalsupport_eb2_fromdate = '" + principalsupport_eb2_fromdate + "',"
						+ " principalsupport_eb2_todate = '" + principalsupport_eb2_todate + "',"
						+ " principalsupport_eb2_wholesaletargetcount = " + principalsupport_eb2_wholesaletargetcount + ","
						+ " principalsupport_eb2_wholesaletargetperc = " + principalsupport_eb2_wholesaletargetperc + ","
						+ " principalsupport_eb2_retailtargetcount = " + principalsupport_eb2_retailtargetcount + ","
						+ " principalsupport_eb2_retailtargetperc = " + principalsupport_eb2_retailtargetperc + ","
						+ " principalsupport_modified_id = " + emp_id + ","
						+ " principalsupport_modified_date = '" + ToLongDate(kknow()) + "'"
						+ " WHERE principalsupport_id = " + principalsupport_id + "";

				// SOP("update===" + StrSql);
				updateQuery(StrSql);

				// profitability calculation for all so associted with stock of this type and of this dms month
				vehsoupdate.profitability_principalsupport_model_id = principalsupport_model_id;
				vehsoupdate.profitability_principalsupport_fueltype_id = principalsupport_fueltype_id;
				vehsoupdate.profitability_principalsupport_month = SplitYear(ToLongDate(kknow())) + principalsupport_month;
				vehsoupdate.UpdateProfitability("0", comp_id);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "SELECT principalsupport_model_id,"
					+ " principalsupport_fueltype_id,"
					+ " principalsupport_month"
					+ " FROM " + compdb(comp_id) + "axela_principal_support"
					+ " WHERE 1=1"
					+ " AND principalsupport_id = " + principalsupport_id;
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql===" + StrSql);
			while (crs.next()) {
				vehsoupdate.profitability_principalsupport_model_id = crs.getString("principalsupport_model_id");
				vehsoupdate.profitability_principalsupport_fueltype_id = crs.getString("principalsupport_fueltype_id");
				vehsoupdate.profitability_principalsupport_month = crs.getString("principalsupport_month").substring(0, 6);
			}
			crs.close();

			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_principal_support WHERE principalsupport_id = " + principalsupport_id;
			updateQuery(StrSql);

			// profitability calculation for all so associted with stock of this type and of this dms month
			vehsoupdate.UpdateProfitability("0", comp_id);

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulatePrincipal(String brand_id, String comp_id, HttpServletRequest request) {

		String BranchAccess = GetSession("BranchAccess", request);
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name "
					+ " FROM axela_brand "
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = brand_id"
					+ " WHERE branch_active = 1"
					+ BranchAccess
					+ " AND branch_branchtype_id IN (1,2)"
					+ " GROUP BY brand_id "
					+ " ORDER BY brand_name ";
			// SOP("brand query======" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id")).append("");
				Str.append(StrSelectdrop(crs.getString("brand_id"), brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateModel(String model_id, String brand_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " WHERE 1 = 1"
					+ " AND model_active = 1 "
					+ " AND model_sales = 1";
			if (!brand_id.equals("") && !brand_id.equals("0")) {
				StrSql += " AND model_brand_id = " + brand_id;
			}
			StrSql += " ORDER BY model_name";
			// SOP("StrSql-----PopulateModel----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select id=\"dr_model\" name=\"dr_model\" class=\"form-control\"");
			if (status.equals("Update")) {
				// Str.append("disabled");
			}
			Str.append(" >");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateFuelType(String fueltype_id, HttpServletRequest request) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT fueltype_id, fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " WHERE 1 = 1"
					+ " ORDER BY fueltype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// Str.append("<select name=dr_fueltype_id id=dr_fueltype_id class=form-control style=\"padding:10px\"");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("fueltype_id")).append("");
				Str.append(StrSelectdrop(crs.getInt("fueltype_id") + "", fueltype_id));
				Str.append(">").append(crs.getString("fueltype_name")).append("</option>\n");
			}
			// Str.append("</select>");
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateMonth(String month) {
		if (month.length() == 14) {
			month = SplitMonth(month);
		}
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=00").append(StrSelectdrop("00", month)).append(">Select</option>");
		Str.append("<option value=01").append(StrSelectdrop("01", month)).append(">January</option>");
		Str.append("<option value=02").append(StrSelectdrop("02", month)).append(">February</option>");
		Str.append("<option value=03").append(StrSelectdrop("03", month)).append(">March</option>");
		Str.append("<option value=04").append(StrSelectdrop("04", month)).append(">April</option>");
		Str.append("<option value=05").append(StrSelectdrop("05", month)).append(">May</option>");
		Str.append("<option value=06").append(StrSelectdrop("06", month)).append(">June</option>");
		Str.append("<option value=07").append(StrSelectdrop("07", month)).append(">July</option>");
		Str.append("<option value=08").append(StrSelectdrop("08", month)).append(">August</option>");
		Str.append("<option value=09").append(StrSelectdrop("09", month)).append(">September</option>");
		Str.append("<option value=10").append(StrSelectdrop("10", month)).append(">October</option>");
		Str.append("<option value=11").append(StrSelectdrop("11", month)).append(">November</option>");
		Str.append("<option value=12").append(StrSelectdrop("12", month)).append(">December</option>");

		return Str.toString();
	}
}
