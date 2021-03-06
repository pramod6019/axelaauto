package axela.axelaauto_app;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.Header;
import axela.preowned.Preowned_Stock_Update;
import cloudify.connect.Connect;

public class App_Preowned_Stock_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String emp_id = "0";
	public String emp_preowned_stock_access = "0";
	public String comp_id = "0", emp_uuid = "";
	public String emp_role_id = "0";
	public String branch_id = "0";
	public String preownedstock_id = "0";
	public String preownedstock_date = "";
	// public String stockdate = "";
	// public String preownedstock_puttosale_date = "";
	public String puttosale_date = "";
	public String preownedstock_status_id = "0";
	public String preownedstock_preownedtype_id = "0";
	public String preownedstatus_name = "";
	public String preownedstock_selling_price = "0";
	public String preowned_id = "0";
	public String preowned_title = "";
	public String preowned_branch_id = "0";
	public String preownedstock_title = "";
	public String preownedmodel_name = "";
	public String variant_name = "";
	public String preownedmodel_id = "0";
	public String variant_id = "";
	public String preownedstock_engine_no = "";
	public String preownedstock_chassis_no = "";
	// public String preownedstock_comm_no = "";
	public String preownedstock_preownedlocation_id = "0";
	public String preownedstock_so_id = "0";
	public String preownedstock_blocked = "";
	public String preownedstock_emp_id = "0";
	public String preownedstock_notes = "";
	public String preownedstock_entry_id = "0";
	public String preownedstock_entry_date = "";
	public String stock_entry_by = "";
	public String preownedstock_modified_id = "0";
	public String preownedstock_modified_date = "";
	public String stock_modified_by = "";
	public String QueryString = "";
	public String preownedstock_purchase_amt = "";
	public String preownedstock_refurbish_amt = "";
	public String preownedstock_check_tradein_contract = "";
	public String preownedstock_check_original_rc = "";
	public String preownedstock_check_insurance = "";
	public String preownedstock_check_forms_283 = "";
	public String preownedstock_check_forms_292 = "";
	public String preownedstock_check_forms_302 = "";
	public String preownedstock_check_forms_352 = "";
	public String preownedstock_check_aff_sale = "";
	public String preownedstock_check_aff_hypo = "";
	public String preownedstock_check_aff_noc = "";
	public String preownedstock_check_aff_poa = "";
	public String preownedstock_check_photographs = "";
	public String preownedstock_check_rationcard = "";
	public String preownedstock_check_telebill = "";
	public String preownedstock_check_pancopy = "";
	public String preownedstock_check_mileage_verification = "";
	public String preownedstock_check_servicebook = "";
	public String preownedstock_check_noc_authority = "";
	public String preownedstock_check_fop_ncrb = "";
	public String preownedstock_check_partnerdeed = "";
	public String preownedstock_check_moa = "";
	public String branch_email1 = "";
	public String preownedpsf_crm_emp_id = "0";
	public DecimalFormat df = new DecimalFormat("0.00");
	// //////
	// public String preowned_preownedmodel_id = "0";
	public String preowned_variant_id = "0", preowned_customer_id = "0", preowned_contact_id = "0";
	public String contact_fname = "", contact_lname = "", contact_mobile1 = "", contact_phone1 = "";
	public String customer_address = "", contact_city_id = "", contact_pin = "";
	public String preowned_date = "", preowned_close_date = "", preowned_fueltype_id = "", preowned_manufyear = "";
	public String preowned_regno = "", preowned_insur_date = "", preowned_ownership_id = "", preowned_emp_id = "", preowned_preownedstatus_id = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			if (!CNumeric(GetSession("emp_id", request) + "").equals("0") && !emp_uuid.equals("")) {
				if (ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id=" + CNumeric(GetSession("emp_id", request) + "") + ""
						+ " AND emp_uuid='" + emp_uuid + "' ").equals(""))
				{
					session.setAttribute("emp_id", "0");
					session.setAttribute("sessionMap", null);
				}
			}
			SetSession("comp_id", comp_id, request);
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0"))
			{
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				emp_preowned_stock_access = ReturnPerm(comp_id, "emp_preowned_stock_access", request);
				if (emp_preowned_stock_access.equals("0")) {
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!"));
				}
				preownedstock_id = CNumeric(PadQuotes(request.getParameter("preownedstock_id")));
				preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				PreownedDetails(response);
				StrSql = "SELECT preownedstock_preowned_id FROM " + compdb(comp_id) + "axela_preowned_stock"
						+ " WHERE preownedstock_preowned_id = " + preowned_id + "";
				if (ExecuteQuery(StrSql).equals(preowned_id) && add.equals("yes")) {
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Stock already added!"));
				}
				StrSql = "SELECT preowned_title, preowned_branch_id, COALESCE(preownedmodel_id,0) AS preownedmodel_id, "
						+ " COALESCE(preownedmodel_name,'') AS preownedmodel_name, variant_id, variant_name"
						+ " FROM " + compdb(comp_id) + "axela_preowned"
						+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
						+ " LEFT JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " WHERE 1=1"
						+ " AND preowned_id = " + preowned_id + "";
				if (!BranchAccess.equals("")) {
					StrSql += BranchAccess.replace("branch_id", "preowned_branch_id") + "";
				}
				if (!ExeAccess.equals("")) {
					StrSql += ExeAccess.replace("emp_id", "preowned_emp_id") + "";
				}
				// SOP("StrSql---------" + StrSqlBreaker(StrSql));
				CachedRowSet crs = processQuery(StrSql, 0);

				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						preowned_title = crs.getString("preowned_title");
						preownedmodel_id = crs.getString("preownedmodel_id");
						preownedmodel_name = crs.getString("preownedmodel_name");
						variant_id = crs.getString("variant_id");
						variant_name = crs.getString("variant_name");
						preowned_branch_id = crs.getString("preowned_branch_id");
					}
				} else {
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Invalid Pre Owned!"));
				}
				crs.close();
				//
				// if ("yes".equals(add)) {
				status = "Add";
				if (!"yes".equals(addB)) {
					preownedstock_blocked = "0";
				} else {
					GetValues(request, response);
					if (ReturnPerm(comp_id, "emp_preowned_stock_add", request).equals("1")) {
						preownedstock_entry_id = emp_id;
						preownedstock_entry_date = ToLongDate(kknow());
						AddPreownedStock();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("callurlapp-stock-list.jsp?stock_id=" + preownedstock_id + "&msg=Stock added successfully!"));
						}
					} else {
						response.sendRedirect(AccessDenied());
					}
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// stockdate =
		// PadQuotes(request.getParameter("txt_preownedstock_date"));
		// preownedstock_date = ConvertShortDateToStr(stockdate);
		// puttosale_date =
		// PadQuotes(request.getParameter("txt_preownedstock_puttosale_date"));
		// preownedstock_puttosale_date = ConvertShortDateToStr(puttosale_date);
		preownedstock_status_id = PadQuotes(request.getParameter("dr_preownedstock_status"));
		preownedstock_preownedtype_id = PadQuotes(request.getParameter("dr_preownedstock_preownedtype"));
		preownedstock_selling_price = PadQuotes(request.getParameter("txt_preownedstock_selling_price"));
		preowned_title = PadQuotes(request.getParameter("txt_preowned_title"));
		preownedmodel_name = PadQuotes(request.getParameter("txt_preownedmodel_name"));
		variant_name = PadQuotes(request.getParameter("txt_variant_name"));
		preownedmodel_id = PadQuotes(request.getParameter("txt_preownedmodel_id"));
		variant_id = PadQuotes(request.getParameter("txt_variant_id"));
		preownedstock_engine_no = PadQuotes(request.getParameter("txt_preownedstock_engine_no"));
		preownedstock_chassis_no = PadQuotes(request.getParameter("txt_preownedstock_chassis_no"));
		// preownedstock_comm_no =
		// PadQuotes(request.getParameter("txt_preownedstock_comm_no"));
		preownedstock_purchase_amt = PadQuotes(request.getParameter("txt_preownedstock_purchase_amt"));
		preownedstock_refurbish_amt = CNumeric(PadQuotes(request.getParameter("txt_preownedstock_refurbish_amt")));
		preownedstock_check_tradein_contract = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_tradein_contract")));
		preownedstock_check_original_rc = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_original_rc")));
		preownedstock_check_insurance = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_insurance")));
		preownedstock_check_forms_283 = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_forms_283")));
		preownedstock_check_forms_292 = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_forms_292")));
		preownedstock_check_forms_302 = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_forms_302")));
		preownedstock_check_forms_352 = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_forms_352")));
		preownedstock_check_aff_sale = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_aff_sale")));
		preownedstock_check_aff_hypo = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_aff_hypo")));
		preownedstock_check_aff_noc = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_aff_noc")));
		preownedstock_check_aff_poa = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_aff_poa")));
		preownedstock_check_photographs = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_photographs")));
		preownedstock_check_rationcard = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_rationcard")));
		preownedstock_check_telebill = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_telebill")));
		preownedstock_check_pancopy = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_pancopy")));
		preownedstock_check_mileage_verification = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_mileage_verification")));
		preownedstock_check_servicebook = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_servicebook")));
		preownedstock_check_noc_authority = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_noc_authority")));
		preownedstock_check_fop_ncrb = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_fop_ncrb")));
		preownedstock_check_partnerdeed = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_partnerdeed")));
		preownedstock_check_moa = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_check_moa")));
		preownedstock_preownedlocation_id = CNumeric(PadQuotes(request.getParameter("dr_preownedstock_preownedlocation")));
		preownedstock_so_id = CNumeric(PadQuotes(request.getParameter("txt_preownedstock_so_id")));
		preownedstock_blocked = CheckBoxValue(PadQuotes(request.getParameter("chk_preownedstock_blocked")));
		preownedstock_emp_id = PadQuotes(request.getParameter("dr_preownedstock_executive"));
		preownedstock_notes = PadQuotes(request.getParameter("txt_preownedstock_notes"));
		stock_entry_by = PadQuotes(request.getParameter("stock_entry_by"));
		preownedstock_entry_date = PadQuotes(request.getParameter("preownedstock_entry_date"));
		stock_modified_by = PadQuotes(request.getParameter("stock_modified_by"));
		preownedstock_modified_date = PadQuotes(request.getParameter("preownedstock_modified_date"));
	}

	public void CheckForm() {
		msg = "";
		// if (stockdate.equals("")) {
		// msg += "<br>Enter Date!";
		// } else {
		// if (isValidDateFormatShort(stockdate)) {
		// preownedstock_date = ConvertShortDateToStr(stockdate);
		// } else {
		// msg += "<br>Enter Valid Stock Date!";
		// }
		// if (Long.parseLong(ToLongDate(kknow())) <
		// Long.parseLong(ConvertShortDateToStr(stockdate))) {
		// msg += " <br>Stock Date can't be greater than Current Date!";
		// }
		// }

		// if (!puttosale_date.equals("") && !stockdate.equals("")) {
		// if (isValidDateFormatShort(puttosale_date)) {
		// preownedstock_puttosale_date = ConvertShortDateToStr(puttosale_date);
		// } else {
		// msg += "<br>Enter Valid Put To Sale Date!";
		// }
		// if (Long.parseLong(ConvertShortDateToStr(puttosale_date)) <
		// Long.parseLong(ConvertShortDateToStr(stockdate))) {
		// msg += " <br>Put To Sale Date can't be less than Stock Date!";
		// }
		// }

		if (preownedstock_status_id.equals("0")) {
			msg += "<br>Select Status!";
		}

		if (preownedstock_preownedtype_id.equals("0")) {
			msg += "<br>Select Type!";
		}
		if (Double.parseDouble(preownedstock_purchase_amt) <= 0) {
			msg += "<br>Enter Purchase Amount!";
		}
		if (CNumeric(preownedstock_selling_price).equals("0")) {
			msg += "<br>Enter Selling Price!";
		} else if (Double.parseDouble(preownedstock_selling_price) < Double.parseDouble(preownedstock_purchase_amt)) {
			msg += "<br>Selling Price Should Be Greater Than Or Equal To Purchase Amount!";
		}

		if (preownedstock_engine_no.equals("") || preownedstock_engine_no.equals("0")) {
			msg = msg + "<br>Enter Engine Number!";
		}
		// else {
		// StrSql = "SELECT preownedstock_engine_no FROM " + compdb(comp_id) +
		// "axela_preowned_stock"
		// + " WHERE preownedstock_engine_no = '" + preownedstock_engine_no +
		// "'";
		// if (update.equals("yes")) {
		// StrSql += " AND preownedstock_id != " + preownedstock_id + "";
		// }
		//
		// if (ExecuteQuery(StrSql).equals(preownedstock_engine_no)) {
		// msg += "<br>Similar Engine Number found!";
		// }
		// }

		if (preownedstock_chassis_no.equals("") || preownedstock_chassis_no.equals("0")) {
			msg += "<br>Enter Chassis Number!";
		}
		// else {
		// StrSql = "SELECT preownedstock_chassis_no FROM " + compdb(comp_id) +
		// "axela_preowned_stock"
		// + " WHERE preownedstock_chassis_no = '" + preownedstock_chassis_no +
		// "'";
		// if (update.equals("yes")) {
		// StrSql += " AND preownedstock_id != " + preownedstock_id + "";
		// }
		//
		// if (ExecuteQuery(StrSql).equals(preownedstock_chassis_no)) {
		// msg += "<br>Similar Chassis Number found!";
		// }
		// }

		if (preownedstock_preownedlocation_id.equals("0")) {
			msg += "<br>Select Location!";
		}

		if (!preownedstock_so_id.equals("0")) {
			StrSql = "SELECT so_id FROM " + compdb(comp_id) + "axela_sales_so"
					+ " WHERE so_id = " + preownedstock_so_id + "";
			if (!ExecuteQuery(StrSql).equals(preownedstock_so_id)) {
				msg += "<br>Invalid Sales Order!";
			}
		}
		if (preownedstock_emp_id.equals("0")) {
			msg += "<br>Select Pre-Owned Consultant!";
		}

		if (!preowned_branch_id.equals("0")) {
			if (!preownedstock_emp_id.equals("0")) {
				StrSql = "SELECT COALESCE((SELECT team_crm_emp_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_team"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
						+ " WHERE team_branch_id = " + preowned_branch_id + ""
						+ " AND teamtrans_emp_id = " + preownedstock_emp_id + ""
						+ " LIMIT 1), 0)";
				preownedpsf_crm_emp_id = ExecuteQuery(StrSql);
				// SOP("preownedpsf_crm_emp_id = " + preownedpsf_crm_emp_id);
			}
		}
	}

	public void AddPreownedStock() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			preownedstock_id = ExecuteQuery("SELECT COALESCE(MAX(preownedstock_id), 0) + 1"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock");
			Preowned_Stock_Update preownedstock = new Preowned_Stock_Update();
			preownedstock.comp_id = comp_id;
			preownedstock.emp_id = emp_id;
			preownedstock.preownedstock_id = preownedstock_id;
			preownedstock.preowned_id = preowned_id;
			preownedstock.preownedstock_date = DateToShortDate(kknow());
			preownedstock.preownedstock_puttosale_date = DateToShortDate(kknow());
			preownedstock.preownedstock_status_id = preownedstock_status_id;
			preownedstock.preownedstock_preownedtype_id = preownedstock_preownedtype_id;
			preownedstock.preownedstock_selling_price = preownedstock_selling_price;
			preownedstock.preownedstock_engine_no = preownedstock_engine_no;
			preownedstock.preownedstock_chassis_no = preownedstock_chassis_no;
			preownedstock.preownedstock_purchase_amt = preownedstock_purchase_amt;
			preownedstock.preownedstock_refurbish_amt = preownedstock_refurbish_amt;
			preownedstock.preownedstock_check_tradein_contract = preownedstock_check_tradein_contract;
			preownedstock.preownedstock_check_original_rc = preownedstock_check_original_rc;
			preownedstock.preownedstock_check_insurance = preownedstock_check_insurance;
			preownedstock.preownedstock_check_forms_283 = preownedstock_check_forms_283;
			preownedstock.preownedstock_check_forms_292 = preownedstock_check_forms_292;
			preownedstock.preownedstock_check_forms_302 = preownedstock_check_forms_302;
			preownedstock.preownedstock_check_forms_352 = preownedstock_check_forms_352;
			preownedstock.preownedstock_check_aff_sale = preownedstock_check_aff_sale;
			preownedstock.preownedstock_check_aff_hypo = preownedstock_check_aff_hypo;
			preownedstock.preownedstock_check_aff_noc = preownedstock_check_aff_noc;
			preownedstock.preownedstock_check_aff_poa = preownedstock_check_aff_poa;
			preownedstock.preownedstock_check_photographs = preownedstock_check_photographs;
			preownedstock.preownedstock_check_rationcard = preownedstock_check_rationcard;
			preownedstock.preownedstock_check_telebill = preownedstock_check_telebill;
			preownedstock.preownedstock_check_pancopy = preownedstock_check_pancopy;
			preownedstock.preownedstock_check_mileage_verification = preownedstock_check_mileage_verification;
			preownedstock.preownedstock_check_servicebook = preownedstock_check_servicebook;
			preownedstock.preownedstock_check_noc_authority = preownedstock_check_noc_authority;
			preownedstock.preownedstock_check_fop_ncrb = preownedstock_check_fop_ncrb;
			preownedstock.preownedstock_check_partnerdeed = preownedstock_check_partnerdeed;
			preownedstock.preownedstock_check_moa = preownedstock_check_moa;
			preownedstock.preownedstock_preownedlocation_id = preownedstock_preownedlocation_id;
			preownedstock.preownedstock_so_id = preownedstock_so_id;
			preownedstock.preownedstock_blocked = preownedstock_blocked;
			preownedstock.preownedstock_emp_id = preownedstock_emp_id;
			preownedstock.preownedstock_notes = preownedstock_notes;
			preownedstock.preownedstock_entry_id = preownedstock_entry_id;
			preownedstock.preownedstock_entry_date = preownedstock_entry_date;

			preownedstock.preowned_branch_id = preowned_branch_id;
			preownedstock.contact_fname = contact_fname;
			preownedstock.contact_lname = contact_lname;
			preownedstock.contact_mobile1 = contact_mobile1;
			preownedstock.contact_phone1 = contact_phone1;
			preownedstock.contact_city_id = contact_city_id;
			preownedstock.contact_pin = contact_pin;
			preownedstock.preowned_date = preowned_date;
			preownedstock.preowned_close_date = preowned_close_date;
			preownedstock.preowned_variant_id = preowned_variant_id;
			preownedstock.preowned_fueltype_id = preowned_fueltype_id;
			preownedstock.preowned_manufyear = preowned_manufyear;
			preownedstock.preowned_regno = preowned_regno;
			preownedstock.preowned_insur_date = preowned_insur_date;
			preownedstock.preowned_ownership_id = preowned_ownership_id;
			preownedstock.preowned_emp_id = preowned_emp_id;
			preownedstock.preowned_preownedstatus_id = preowned_preownedstatus_id;

			preownedstock.AddFields();
			msg = preownedstock.msg;
		}
	}
	public void PreownedDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT contact_fname, contact_lname, contact_mobile1, contact_phone1, "
					// + " preowned_preownedmodel_id, "
					+ " preowned_variant_id, preowned_customer_id, preowned_contact_id,"
					+ " preowned_date, preowned_close_date, preowned_fueltype_id, preowned_manufyear,"
					+ " preowned_regno, preowned_insur_date, preowned_ownership_id, contact_city_id,"
					+ " contact_pin, preowned_branch_id,"
					+ " preowned_emp_id, preowned_preownedstatus_id, contact_mobile1, contact_phone1 "
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id=preowned_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id=preowned_contact_id"
					// + " INNER JOIN " + compdb(comp_id) +
					// "axela_customer_contact ON contact_id=preowned_contact_id"
					+ " WHERE 1=1"
					+ " AND preowned_id = " + preowned_id + "";
			if (!BranchAccess.equals("")) {
				StrSql += BranchAccess.replace("branch_id", "preowned_branch_id") + "";
			}
			if (!ExeAccess.equals("")) {
				StrSql += ExeAccess.replace("emp_id", "preowned_emp_id") + "";
			}
			// SOP("StrSql---------" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preowned_branch_id = crs.getString("preowned_branch_id");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_phone1 = crs.getString("contact_phone1");
					contact_city_id = crs.getString("contact_city_id");
					contact_pin = crs.getString("contact_pin");
					preowned_date = crs.getString("preowned_date");
					preowned_close_date = crs.getString("preowned_close_date");
					// preowned_preownedmodel_id =
					// crs.getString("preowned_preownedmodel_id");
					preowned_variant_id = crs.getString("preowned_variant_id");
					preowned_fueltype_id = crs.getString("preowned_fueltype_id");
					preowned_manufyear = crs.getString("preowned_manufyear");
					preowned_regno = crs.getString("preowned_regno");
					preowned_insur_date = crs.getString("preowned_insur_date");
					preowned_ownership_id = crs.getString("preowned_ownership_id");
					preowned_emp_id = crs.getString("preowned_emp_id");
					preowned_preownedstatus_id = crs.getString("preowned_preownedstatus_id");

					// branch_brand_id =
					// crs.getString("branch_brand_id");
				}
				if (preowned_branch_id.equals("0")) {
					msg += "<font color=red>Update Pre-Owned Branch! </font>";
				}
				if (contact_fname.equals("")) {
					msg += "<br><font color=red>Update Contact First Name! </font>";
				}
				if (contact_lname.equals("")) {
					msg += "<br><font color=red>Update Contact Last Name! </font>";
				}
				if (contact_mobile1.equals("")) {
					msg += "<br><font color=red>Update Contact Mobile 1! </font>";
				}
				// if (contact_phone1.equals("")) {
				// msg += "<br><font color=red>Update Contact Phone 1! </font>";
				// }
				if (contact_city_id.equals("0")) {
					msg += "<br><font color=red>Update Contact City! </font>";
				}
				if (contact_pin.equals("")) {
					msg += "<br><font color=red>Update Contact Pin! </font>";
				}
				if (preowned_date.equals("")) {
					msg += "<br><font color=red>Update Pre-Owned Date!</font>";
				}
				if (preowned_close_date.equals("")) {
					msg += "<br><font color=red>Update Closing Date!</font>";
				}
				// if (preowned_preownedmodel_id.equals("0")) {
				// msg += "<br><font color=red>Update Preowned Model!</font>";
				// }
				// if (preowned_variant_id.equals("0")) {
				// msg += "<br><font color=red>Update Preowned Variant!</font>";
				// }
				if (preowned_fueltype_id.equals("0")) {
					msg += "<br><font color=red>Update Pre-Owned Fuel Type!</font>";
				}
				if (preowned_manufyear.equals("")) {
					msg += "<br><font color=red>Update Pre-Owned Manufacture Year!</font>";
				}
				if (preowned_regno.equals("")) {
					msg += "<br><font color=red>Update Pre-Owned Registration No!</font>";
				}
				// if (preowned_insur_date.equals("")) {
				// msg += "<br><font color=red>Update Pre-Owned Insurance Date!</font>";
				// }
				if (preowned_ownership_id.equals("0")) {
					msg += "<br><font color=red>Update Pre-Owned Ownership!</font>";
				}
				if (preowned_emp_id.equals("0")) {
					msg += "<br><font color=red>Update Pre-Owned Consultant!</font>";
				}
				if (preowned_preownedstatus_id.equals("0")) {
					msg += "<br><font color=red>Update Pre-Owned Status!</font>";
				}
				if (!msg.equals("")) {
					response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=" + msg));
				}

			} else {
				response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Invalid Pre-Owned!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}
	protected String SendEmailToPreownedStockAdd() {
		String email_msg = "";
		String subject = "";
		String branch_sales_email = "";
		int invoicedays = 0;
		String bgcol = "";
		try {
			StrSql = "SELECT preownedstock_selling_price, preownedmodel_name, preowned_customer_id, variant_name,"
					+ " preowned_sub_variant, preowned_extcolour_id, preowned_intcolour_id, preowned_contact_id,"
					+ " COALESCE(intcolour_name, '') AS intcolour_name, branch_name, branch_sales_email,"
					+ " CONCAT(sesemp.emp_name, ' <br>', jobtitle_desc) AS sesempname, preownedstock_date,"
					+ " COALESCE(extcolour_name, '') AS extcolour_name, preownedstock_id, branch_email1,"
					// + " preownedstock_comm_no, "
					+ " preowned_regno, preowned_manufyear, preowned_kms"
					+ " FROM " + compdb(comp_id) + "axela_preowned"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock ON preownedstock_preowned_id = preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp sesemp ON sesemp.emp_id = " + emp_id + ""
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = sesemp.emp_jobtitle_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = preowned_preownedmodel_id"
					+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
					+ " LEFT JOIN axela_preowned_intcolour ON intcolour_id = preowned_intcolour_id"
					+ " LEFT JOIN axela_preowned_extcolour ON extcolour_id = preowned_extcolour_id"
					+ " WHERE preownedstock_id = " + preownedstock_id + ""
					+ " GROUP BY preownedstock_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					branch_email1 = crs.getString("branch_email1");
					branch_sales_email = crs.getString("branch_sales_email");
					subject = "Pre Owned Car Purchased";
					email_msg = " Dear All, <br><br>The following Pre Owned car is purchased by Axelaauto is now available for sale.<br><br>";

					if (!crs.getString("preownedstock_date").equals("")) {
						invoicedays = (int) Math.round(getDaysBetween(crs.getString("preownedstock_date"), ToLongDate(kknow())));
					} else {
						invoicedays = 0;
					}
					invoicedays = invoicedays - 1;

					if (invoicedays < 45) // invoicedays>=0 &&
					{
						bgcol = "#ffffff";
					} else if (invoicedays >= 45 && invoicedays <= 74) // bgcol
																		// =
																		// "#ffcfa4";
					{
						bgcol = "orange";
					} else if (invoicedays > 74) // bgcol = "#ffdfdf";
					{
						bgcol = "red";
					}

					email_msg += "<table border=\"1\" cellspacing=\"0\" cellpadding=\"5\">\n";
					email_msg += "<tr height=\"50\">\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Pre Owned Stock ID</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Model</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Variant</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Sub Variant</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Interior</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Exterior</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Comm. No.</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Reg. No.</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">Year</td>\n";
					email_msg += "<td bgColor=\"" + bgcol + "\" valign=\"top\">KMS</td>\n";
					email_msg += "</tr>\n";
					email_msg += "<tr height=\"50\" align=\"center\">\n";
					email_msg += "<td valign=top>" + crs.getString("preownedstock_id") + "&nbsp;</td>\n";
					email_msg += "<td valign=top>" + crs.getString("preownedmodel_name") + "&nbsp;</td>\n";
					email_msg += "<td valign=top>" + crs.getString("variant_name") + "&nbsp;</td>\n";
					email_msg += "<td valign=top>" + crs.getString("preowned_sub_variant") + "&nbsp;</td>\n";
					email_msg += "<td valign=top>" + crs.getString("intcolour_name") + "&nbsp;</td>\n";
					email_msg += "<td valign=top>" + crs.getString("extcolour_name") + "&nbsp;</td>\n";
					// email_msg += "<td valign=\"top\">" +
					// crs.getString("preownedstock_comm_no") + "&nbsp;</td>\n";
					email_msg += "<td valign=\"top\">" + crs.getString("preowned_regno") + "&nbsp;</td>\n";
					email_msg += "<td valign=\"top\">" + crs.getString("preowned_manufyear") + "&nbsp;</td>\n";
					email_msg += "<td valign=\"top\">" + crs.getString("preowned_kms") + "&nbsp;</td>\n";

					email_msg += "</tr>\n</table>\n";
				}
				email_msg += "<br><br>Regards,<br><br>";

				crs.beforeFirst();
				while (crs.next()) {
					email_msg += "" + crs.getString("sesempname");
					email_msg += "<br>" + crs.getString("branch_name");
				}

				email_msg = "<html><body><basefont face=arial, verdana size=2>" + email_msg + "</body></html>";
				// postMail(branch_sales_email, "", "sujay@emax.in",
				// branch_email1, subject, email_msg, "");

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
						+ " (email_contact_id,"
						+ " email_contact,"
						+ " email_from,"
						+ " email_to,"
						+ " email_cc,"
						+ " email_bcc,"
						+ " email_subject,"
						+ " email_msg,"
						+ " email_date,"
						+ " email_entry_id,"
						+ " email_sent)"
						+ " VALUES"
						+ " ('0',"
						+ " '',"
						+ " '" + branch_email1 + "',"
						+ " '" + branch_sales_email + "',"
						+ " '',"
						+ " 'sujay@emax.in',"
						+ " '" + subject + "',"
						+ " '" + email_msg + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " " + emp_id + ","
						+ " 0)";
				updateQuery(StrSql);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			email_msg = "";
		}
		return email_msg;
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT preownedstock_id, preownedstock_date, preownedstock_puttosale_date, preownedstock_status_id, preownedstatus_name,"
					+ " preownedstock_preownedtype_id, preownedstock_selling_price, preownedstock_engine_no,"
					+ " preownedstock_chassis_no, preownedstock_preownedlocation_id," // preownedstock_comm_no,
					+ " preownedstock_blocked, preownedstock_purchase_amt, preownedstock_check_tradein_contract,"
					+ " preownedstock_so_id, preownedstock_refurbish_amt, preowned_title, preownedstock_modified_id,"
					+ " preownedstock_check_original_rc, preownedstock_check_insurance, preownedstock_check_forms_283,"
					+ " preownedstock_check_forms_292, preownedstock_check_forms_302, preownedstock_check_forms_352,"
					+ " preownedstock_check_aff_sale, preownedstock_check_aff_hypo, preownedstock_check_aff_noc,"
					+ " preownedstock_check_aff_poa, preownedstock_check_photographs, preownedstock_check_rationcard,"
					+ " preownedstock_check_telebill, preownedstock_check_pancopy, preownedstock_check_mileage_verification,"
					+ " preownedstock_check_servicebook, preownedstock_check_noc_authority, preownedstock_check_fop_ncrb,"
					+ " preownedstock_check_partnerdeed, preownedstock_check_moa, preownedstock_check_moa,"
					+ " preownedstock_notes, preownedstock_entry_id, preownedstock_entry_date,"
					+ " preownedstock_modified_date, preownedstock_emp_id, emp_id, emp_name "
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedstock_preowned_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp preowned ON emp_id = preownedstock_emp_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_stock_status ON preownedstatus_id = preownedstock_status_id"
					+ " WHERE preownedstock_id = " + preownedstock_id;
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preownedstock_id = crs.getString("preownedstock_id");
					// preownedstock_date = crs.getString("preownedstock_date");
					// stockdate = strToShortDate(preownedstock_date);
					// preownedstock_puttosale_date =
					// crs.getString("preownedstock_puttosale_date");
					// puttosale_date =
					// strToShortDate(preownedstock_puttosale_date);
					preownedstock_status_id = crs.getString("preownedstock_status_id");
					preownedstock_preownedtype_id = crs.getString("preownedstock_preownedtype_id");
					preownedstock_selling_price = df.format(Double.parseDouble(crs.getString("preownedstock_selling_price")));
					preowned_title = crs.getString("preowned_title");
					preownedstock_title = crs.getString("preowned_title");
					preownedstock_engine_no = crs.getString("preownedstock_engine_no");
					preownedstock_chassis_no = crs.getString("preownedstock_chassis_no");
					// preownedstock_comm_no =
					// crs.getString("preownedstock_comm_no");
					preownedstock_purchase_amt = df.format(Double.parseDouble(crs.getString("preownedstock_purchase_amt")));
					preownedstock_refurbish_amt = crs.getString("preownedstock_refurbish_amt");
					preownedstock_check_tradein_contract = crs.getString("preownedstock_check_tradein_contract");
					preownedstock_check_original_rc = crs.getString("preownedstock_check_original_rc");
					preownedstock_check_insurance = crs.getString("preownedstock_check_insurance");
					preownedstock_check_forms_283 = crs.getString("preownedstock_check_forms_283");
					preownedstock_check_forms_292 = crs.getString("preownedstock_check_forms_292");
					preownedstock_check_forms_302 = crs.getString("preownedstock_check_forms_302");
					preownedstock_check_forms_352 = crs.getString("preownedstock_check_forms_352");
					preownedstock_check_aff_sale = crs.getString("preownedstock_check_aff_sale");
					preownedstock_check_aff_hypo = crs.getString("preownedstock_check_aff_hypo");
					preownedstock_check_aff_noc = crs.getString("preownedstock_check_aff_noc");
					preownedstock_check_aff_poa = crs.getString("preownedstock_check_aff_poa");
					preownedstock_check_photographs = crs.getString("preownedstock_check_photographs");
					preownedstock_check_rationcard = crs.getString("preownedstock_check_rationcard");
					preownedstock_check_telebill = crs.getString("preownedstock_check_telebill");
					preownedstock_check_pancopy = crs.getString("preownedstock_check_pancopy");
					preownedstock_check_mileage_verification = crs.getString("preownedstock_check_mileage_verification");
					preownedstock_check_servicebook = crs.getString("preownedstock_check_servicebook");
					preownedstock_check_noc_authority = crs.getString("preownedstock_check_noc_authority");
					preownedstock_check_fop_ncrb = crs.getString("preownedstock_check_fop_ncrb");
					preownedstock_check_partnerdeed = crs.getString("preownedstock_check_partnerdeed");
					preownedstock_check_moa = crs.getString("preownedstock_check_moa");
					preownedstock_preownedlocation_id = crs.getString("preownedstock_preownedlocation_id");
					preownedstock_so_id = crs.getString("preownedstock_so_id");
					preownedstock_blocked = crs.getString("preownedstock_blocked");
					preownedstock_emp_id = crs.getString("preownedstock_emp_id");
					preownedstock_notes = crs.getString("preownedstock_notes");
					preownedstock_entry_id = crs.getString("preownedstock_entry_id");
					if (!preownedstock_entry_id.equals("")) {
						stock_entry_by = Exename(comp_id, Integer.parseInt(preownedstock_entry_id));
					}

					preownedstock_entry_date = strToLongDate(crs.getString("preownedstock_entry_date"));
					preownedstock_modified_id = crs.getString("preownedstock_modified_id");
					if (!preownedstock_modified_id.equals("0")) {
						stock_modified_by = Exename(comp_id, Integer.parseInt(preownedstock_modified_id));
						preownedstock_modified_date = strToLongDate(crs.getString("preownedstock_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("callurl" + "app-error.jsp?msg=Invalid Stock!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_stock"
					+ " SET"
					// + " preownedstock_date = '" + preownedstock_date + "',"
					// + " preownedstock_puttosale_date = '" +
					// preownedstock_puttosale_date + "',"
					+ " preownedstock_status_id = " + preownedstock_status_id + ","
					+ " preownedstock_preownedtype_id = " + preownedstock_preownedtype_id + ","
					+ " preownedstock_engine_no = '" + preownedstock_engine_no + "',"
					+ " preownedstock_chassis_no = '" + preownedstock_chassis_no + "',"
					// + " preownedstock_comm_no = '" + preownedstock_comm_no +
					// "',"
					+ " preownedstock_purchase_amt = " + preownedstock_purchase_amt + ","
					+ " preownedstock_refurbish_amt = " + preownedstock_refurbish_amt + ","
					+ " preownedstock_check_tradein_contract = '" + preownedstock_check_tradein_contract + "',"
					+ " preownedstock_check_original_rc = '" + preownedstock_check_original_rc + "',"
					+ " preownedstock_check_insurance = '" + preownedstock_check_insurance + "',"
					+ " preownedstock_check_forms_283 = '" + preownedstock_check_forms_283 + "',"
					+ " preownedstock_check_forms_292 = '" + preownedstock_check_forms_292 + "',"
					+ " preownedstock_check_forms_302 = '" + preownedstock_check_forms_302 + "',"
					+ " preownedstock_check_forms_352 = '" + preownedstock_check_forms_352 + "',"
					+ " preownedstock_check_aff_sale = '" + preownedstock_check_aff_sale + "',"
					+ " preownedstock_check_aff_hypo = '" + preownedstock_check_aff_hypo + "',"
					+ " preownedstock_check_aff_noc = '" + preownedstock_check_aff_noc + "',"
					+ " preownedstock_check_aff_poa = '" + preownedstock_check_aff_poa + "',"
					+ " preownedstock_check_photographs = '" + preownedstock_check_photographs + "',"
					+ " preownedstock_check_rationcard = '" + preownedstock_check_rationcard + "',"
					+ " preownedstock_check_telebill = '" + preownedstock_check_telebill + "',"
					+ " preownedstock_check_pancopy = '" + preownedstock_check_pancopy + "',"
					+ " preownedstock_check_mileage_verification = '" + preownedstock_check_mileage_verification + "',"
					+ " preownedstock_check_servicebook = '" + preownedstock_check_servicebook + "',"
					+ " preownedstock_check_noc_authority = '" + preownedstock_check_noc_authority + "',"
					+ " preownedstock_check_fop_ncrb = '" + preownedstock_check_fop_ncrb + "',"
					+ " preownedstock_check_partnerdeed = '" + preownedstock_check_partnerdeed + "',"
					+ " preownedstock_check_moa = '" + preownedstock_check_moa + "',"
					+ " preownedstock_preownedlocation_id = '" + preownedstock_preownedlocation_id + "',"
					+ " preownedstock_so_id = '" + preownedstock_so_id + "',"
					+ " preownedstock_blocked = '" + preownedstock_blocked + "',"
					+ " preownedstock_selling_price = " + preownedstock_selling_price + ","
					+ " preownedstock_emp_id = " + preownedstock_emp_id + ","
					+ " preownedstock_notes = '" + preownedstock_notes + "',"
					+ " preownedstock_modified_id = " + preownedstock_modified_id + ","
					+ " preownedstock_modified_date = '" + preownedstock_modified_date + "'"
					+ " WHERE preownedstock_id = " + preownedstock_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT testdrive_id FROM " + compdb(comp_id) + "axela_preowned_testdrive"
				+ " WHERE testdrive_preownedstock_id = " + preownedstock_id + "";

		if (ExecuteQuery(StrSql).equals(preownedstock_id)) {
			msg += "<br>Pre Owned Stock is associated with a Pre Owned Items!";
		}

		StrSql = "SELECT preownedstockgatepass_id FROM " + compdb(comp_id) + "axela_preowned_stock_gatepass"
				+ " WHERE preownedstockgatepass_preownedstock_id = " + preownedstock_id + "";

		if (ExecuteQuery(StrSql).equals(preownedstock_id)) {
			msg += "<br>Pre Owned Stock is associated with a Gate Pass!";
		}

		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_stock"
					+ " WHERE preownedstock_id = " + preownedstock_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulatePreownedStockStatus() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedstatus_id, preownedstatus_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_stock_status"
					+ " WHERE preownedstatus_id >= 0"
					+ " GROUP BY preownedstatus_id"
					+ " ORDER BY preownedstatus_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedstatus_id"));
				Str.append(StrSelectdrop(crs.getString("preownedstatus_id"), preownedstock_status_id));
				Str.append(">").append(crs.getString("preownedstatus_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreownedType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedtype_id, preownedtype_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_type"
					+ " GROUP BY preownedtype_id"
					+ " ORDER BY preownedtype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedtype_id"));
				Str.append(StrSelectdrop(crs.getString("preownedtype_id"), preownedstock_preownedtype_id));
				Str.append(">").append(crs.getString("preownedtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreownedLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedlocation_id, preownedlocation_name"
					+ " FROM " + compdb(comp_id) + "axela_preowned_location"
					+ " WHERE preownedlocation_branch_id = " + preowned_branch_id
					+ " GROUP BY preownedlocation_id"
					+ " ORDER BY preownedlocation_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedlocation_id"));
				Str.append(StrSelectdrop(crs.getString("preownedlocation_id"), preownedstock_preownedlocation_id));
				Str.append(">").append(crs.getString("preownedlocation_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulatePreownedStockExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_active = 1"
					+ " AND emp_preowned = 1"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), preownedstock_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
