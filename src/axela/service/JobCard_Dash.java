package axela.service;
//aJIt 18 Nov, 2013

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.accounting.Ledger_Check;
import axela.inventory.Item_Price_Update;
import cloudify.connect.Connect;

public class JobCard_Dash extends Connect {

	public String StrSql = "";
	public String msg = "", contact_msg = "", jcpsfmsg = "", ticketmsg = "";
	public String StrCustomerDetails = "";
	public String modal = "";
	public String branch_id = "0";
	public String customer_edit_perm = "0";
	public String jc_branch_id = "0";
	public String branch_name = "";
	public String rateclass_id = "0";
	public String jc_id = "0";
	public String jc_no = "";
	public String jc_time_in = "";
	public String date = "";
	public String jc_contact_id = "0";
	public String jc_comm_contact_id = "0";
	public String jc_customer_id = "0";
	public String jc_time_promised = "";
	public String jc_promisetime = "";
	public String jc_time_posted = "";
	public String jc_postedtime = "";
	public String jc_time_out = "";
	public String jc_timeout = "";
	public String jc_ready_time = "";
	public String jc_readytime = "";
	public String jc_model_name = "";
	public String jc_item_name = "";
	public String jc_type_id = "0";
	public String jc_cat_id = "0";
	public String jc_title = "";
	public String jc_kms = "";
	public String jc_fuel_guage = "";
	public String jc_cust_voice = "";
	public String jc_advice = "";
	public String jc_instr = "";
	public String jc_terms = "";
	public String jc_ro_no = "";
	public String jc_emp_id = "0";
	public String jc_technician_emp_id = "0";
	public String emp_role_id = "0";
	public String jc_location_id = "0";
	public String jc_critical = "0";
	public String jc_notes = "";
	public String jc_priorityjc_id = "0";
	public String jc_stage_id = "0";
	public String jc_bay_id = "0";
	public String StrHTML = "";
	public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String jc_modified_id = "0";
	public String jc_modified_date = "";
	public String jctrans_billtype_id = "1";
	public String emp_jc_priceupdate = "0", emp_jc_discountupdate = "0";
	public String entry_by = "", entry_date = "";
	public String modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String link_refcustomer_name = "";
	public String veh_engine_no = "";
	public String veh_reg_no = "", veh_chassis_no = "";
	public String jc_customer_name = "";
	public String contact_mobile1 = "", new_contact_mobile1 = "";
	public String contact_mobile2 = "", new_contact_mobile2 = "", contact_mobile3 = "", new_contact_mobile3 = "";
	public String contact_mobile4 = "", new_contact_mobile4 = "", contact_mobile5 = "", new_contact_mobile5 = "", contact_mobile6 = "", new_contact_mobile6 = "";
	public String contact_mobile1_phonetype_id = "0";
	public String contact_mobile2_phonetype_id = "0";
	public String contact_mobile3_phonetype_id = "0";
	public String contact_mobile4_phonetype_id = "0";
	public String contact_mobile5_phonetype_id = "0";
	public String contact_mobile6_phonetype_id = "0";
	public String contact_phone1 = "";
	public String contact_phone2 = "";
	public String contact_email1 = "", new_contact_email1 = "";
	public String contact_fname = "", new_contact_fname = "";
	public String contact_lname = "", new_contact_lname = "";
	public String contact_pin = "";
	public String contact_title_id = "0", new_contact_title_id = "1";
	public String new_contact_contacttype_id = "7";
	public String new_contact_notes = "";
	public String contact_city_id = "0";
	public String contactdetails = "";
	public String contact_address = "";
	public String variant_id = "0";
	public String check_id = "0";
	public int i = 1;
	public int j = 1;
	public String PageCurrents = "";
	public String PageNaviStr = "";
	public String RecCountDisplay = "";
	public int recperpage = 0;
	public int PageCount = 10;
	public int PageCurrent = 0;
	public String QueryString = "";
	public String doc_id = "0";
	public String all = "";
	public String StrSearch = "";
	public String invent_model_id = "0";
	public String flag = "";
	public String addContactB = "";
	public String jc_check = "";
	public String veh_id = "0";
	public String jc_docs = "";
	public String jc_images = "";
	public String jc_inventory = "";
	public String jc_manhours = "";
	public String status = "";

	public String jc_bill_discount = "0";
	public String jc_bill_cash_customername = "";
	public String jc_bill_insur_customername = "";
	public String jc_bill_foc_customername = "";
	public String jc_bill_warranty_customername = "";

	public String jc_bill_cash_date = "";
	public String jc_bill_cash_no = "";
	public String jc_bill_cash_parts = "0";
	public String jc_bill_cash_parts_tyre = "0";
	public String jc_bill_cash_parts_oil = "0";
	public String jc_bill_cash_parts_accessories = "0";
	public String jc_bill_cash_labour = "0";
	public String jc_bill_cash_parts_valueadd = "0";
	public String jc_bill_cash_parts_battery = "0";
	public String jc_bill_cash_parts_brake = "0";
	public String jc_bill_cash_parts_extwarranty = "0";
	public String jc_bill_cash_parts_wheelalign = "0";
	public String jc_bill_cash_parts_cng = "0";
	public String jc_bill_cash_parts_tyre_qty = "0";
	public String jc_bill_cash_parts_battery_qty = "0";
	public String jc_bill_cash_parts_brake_qty = "0";
	public String jc_bill_cash_parts_extwarranty_qty = "0";
	public String jc_bill_cash_parts_discamt = "0";

	public String jc_bill_cash_labour_tyre_qty = "0";
	public String jc_bill_cash_labour_tyre = "0";
	public String jc_bill_cash_labour_oil = "0";
	public String jc_bill_cash_labour_battery_qty = "0";
	public String jc_bill_cash_labour_battery = "0";
	public String jc_bill_cash_labour_brake_qty = "0";
	public String jc_bill_cash_labour_brake = "0";
	public String jc_bill_cash_labour_accessories = "0";
	public String jc_bill_cash_labour_valueadd = "0";
	public String jc_bill_cash_labour_extwarranty_qty = "0";
	public String jc_bill_cash_labour_extwarranty = "0";
	public String jc_bill_cash_labour_wheelalign = "0";
	public String jc_bill_cash_labour_cng = "0";

	public String jc_bill_cash_labour_discamt = "0";

	public String jc_bill_insur_date = "";
	public String jc_bill_insur_no = "";
	public String jc_bill_insur_parts = "0";
	public String jc_bill_insur_parts_tyre = "0";
	public String jc_bill_insur_parts_tyre_qty = "0";
	public String jc_bill_insur_parts_oil = "0";
	public String jc_bill_insur_parts_battery_qty = "0";
	public String jc_bill_insur_parts_battery = "0";
	public String jc_bill_insur_parts_brake_qty = "0";
	public String jc_bill_insur_parts_brake = "0";
	public String jc_bill_insur_parts_accessories = "0";
	public String jc_bill_insur_parts_valueadd = "0";
	public String jc_bill_insur_parts_extwarranty_qty = "0";
	public String jc_bill_insur_parts_extwarranty = "0";
	public String jc_bill_insur_parts_wheelalign = "0";
	public String jc_bill_insur_parts_cng = "0";
	public String jc_bill_insur_parts_discamt = "0";

	public String jc_bill_insur_labour = "0";
	public String jc_bill_insur_labour_tyre = "0";
	public String jc_bill_insur_labour_tyre_qty = "0";
	public String jc_bill_insur_labour_oil = "0";
	public String jc_bill_insur_labour_battery_qty = "0";
	public String jc_bill_insur_labour_battery = "0";
	public String jc_bill_insur_labour_brake_qty = "0";
	public String jc_bill_insur_labour_brake = "0";
	public String jc_bill_insur_labour_accessories = "0";
	public String jc_bill_insur_labour_valueadd = "0";
	public String jc_bill_insur_labour_extwarranty_qty = "0";
	public String jc_bill_insur_labour_extwarranty = "0";
	public String jc_bill_insur_labour_wheelalign = "0";
	public String jc_bill_insur_labour_cng = "0";
	public String jc_bill_insur_labour_discamt = "0";

	public String jc_bill_foc_date = "";
	public String jc_bill_foc_no = "";

	public String jc_bill_warranty_date = "";
	public String jc_bill_warranty_no = "";
	public String jc_bill_warranty_parts = "0";
	public String jc_bill_warranty_parts_tyre = "0";
	public String jc_bill_warranty_parts_oil = "0";
	public String jc_bill_warranty_parts_accessories = "0";
	public String jc_bill_warranty_labour = "0";
	public String jc_bill_warranty_parts_valueadd = "0";
	public String jc_bill_warranty_parts_battery = "0";
	public String jc_bill_warranty_parts_brake = "0";
	public String jc_bill_warranty_parts_extwarranty = "0";
	public String jc_bill_warranty_parts_wheelalign = "0";
	public String jc_bill_warranty_parts_cng = "0";
	public String jc_bill_warranty_parts_tyre_qty = "0";
	public String jc_bill_warranty_parts_battery_qty = "0";
	public String jc_bill_warranty_parts_brake_qty = "0";
	public String jc_bill_warranty_parts_extwarranty_qty = "0";
	public String jc_bill_warranty_parts_discamt = "0";

	public String jc_bill_warranty_labour_tyre_qty = "0";
	public String jc_bill_warranty_labour_tyre = "0";
	public String jc_bill_warranty_labour_oil = "0";
	public String jc_bill_warranty_labour_battery_qty = "0";
	public String jc_bill_warranty_labour_battery = "0";
	public String jc_bill_warranty_labour_brake_qty = "0";
	public String jc_bill_warranty_labour_brake = "0";
	public String jc_bill_warranty_labour_accessories = "0";
	public String jc_bill_warranty_labour_valueadd = "0";
	public String jc_bill_warranty_labour_extwarranty_qty = "0";
	public String jc_bill_warranty_labour_extwarranty = "0";
	public String jc_bill_warranty_labour_wheelalign = "0";
	public String jc_bill_warranty_labour_cng = "0";

	public String jc_bill_warranty_labour_discamt = "0";

	public String jc_history = "";
	public String jcwarranty = "";
	public String jcpsf_id = "0", jcpsf = "";
	public String psfmsg = "", delete = "";
	public String baytrans_bay_id = "0";
	public String baytrans_emp_id = "0";
	public String baytrans_start_time = "";
	public String baytrans_end_time = "";
	public String jc_grandtotal = "0";
	// gst
	public String gst_type = "";

	public Item_Price_Update itemPriceUpdate = new Item_Price_Update();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_jobcard_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				customer_edit_perm = ReturnPerm(comp_id, "emp_customer_edit", request);
				msg = PadQuotes(request.getParameter("msg"));
				jcpsfmsg = PadQuotes(request.getParameter("jcpsfmsg"));
				ticketmsg = PadQuotes(request.getParameter("ticketmsg"));
				modal = PadQuotes(request.getParameter("modal"));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				jcpsf_id = PadQuotes(request.getParameter("jcpsf_id"));
				jcpsf = PadQuotes(request.getParameter("jcpsf"));
				psfmsg = PadQuotes(request.getParameter("psfmsg"));
				if (PadQuotes(request.getParameter("txt_jctrans_billtype")).equals("")) {
					jctrans_billtype_id = PadQuotes(request.getParameter("txt_jctrans_billtype"));
				}
				addContactB = PadQuotes(request.getParameter("add_contact_button"));
				delete = PadQuotes(request.getParameter("Delete"));
				baytrans_emp_id = PadQuotes(request.getParameter("dr_baytrans_emp_id"));
				baytrans_start_time = PadQuotes(request.getParameter("txt_insurfollowup_time"));

				// this id to populate Rate Class in parts tab
				itemPriceUpdate.comp_id = comp_id;
				itemPriceUpdate.jc_id = jc_id;
				// end

				PopulateFields(response);
				gst_type = PadQuotes(new Ledger_Check().CompareState(comp_id, jc_customer_id, jc_branch_id));
				StrSql = "SELECT emp_jc_priceupdate, emp_jc_discountupdate"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id;
				CachedRowSet crs = processQuery(StrSql);
				while (crs.next()) {
					emp_jc_priceupdate = crs.getString("emp_jc_priceupdate");
					emp_jc_discountupdate = crs.getString("emp_jc_discountupdate");
				}

				contactdetails = ListCustomerDetails(comp_id, jc_id);
				StrCustomerDetails = new Vehicle_Dash().CustomerDetails(jc_customer_id, customer_edit_perm, comp_id);
				if (delete.equals("yes") && (emp_id.equals("1") || emp_id.equals("147"))) {
					DeletePSFFields(jcpsf_id, jcpsf);
					if (jcpsf.equals("yes")) {
						response.sendRedirect(response.encodeRedirectURL("jobcard-dash.jsp?jc_id=" + jc_id + "&psfmsg=PSF deleted successfully!#tabs-9"));
					}
				}

				if (addContactB.equals("Add Contact")) {
					new_contact_title_id = CNumeric(PadQuotes(request.getParameter("dr_new_contact_title_id")));
					new_contact_fname = PadQuotes(request.getParameter("txt_new_contact_fname"));
					new_contact_lname = PadQuotes(request.getParameter("txt_new_contact_lname"));
					new_contact_mobile1 = PadQuotes(request.getParameter("txt_new_contact_mobile1"));
					new_contact_mobile2 = PadQuotes(request.getParameter("txt_new_contact_mobile2"));
					new_contact_mobile3 = PadQuotes(request.getParameter("txt_new_contact_mobile3"));
					new_contact_mobile4 = PadQuotes(request.getParameter("txt_new_contact_mobile4"));
					new_contact_mobile5 = PadQuotes(request.getParameter("txt_new_contact_mobile5"));
					new_contact_mobile6 = PadQuotes(request.getParameter("txt_new_contact_mobile6"));
					new_contact_email1 = PadQuotes(request.getParameter("txt_new_contact_email1"));
					new_contact_contacttype_id = CNumeric(PadQuotes(request.getParameter("dr_new_contact_contacttype_id")));
					new_contact_notes = PadQuotes(request.getParameter("txt_new_contact_notes"));
					ContactCheckForm();
					if (contact_msg.equals("")) {
						AddNewContact();
						contact_msg = "Contact added successfully!";
					} else {
						contact_msg = "Error!" + contact_msg;
					}
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void CheckForm() {
		msg = "";

		if (baytrans_start_time.equals("")) {
			msg += "<br>Select Next Follow-up Time!";
		} else {
			if (!isValidDateFormatLong(baytrans_start_time)) {
				msg += "<br>Enter valid Follow-up Time!";
			} else {
				if (Long.parseLong(ConvertLongDateToStr(baytrans_start_time)) <= Long.parseLong(ToLongDate(kknow()))) {
					msg += "<br>Next Follow-up Time must be greater than Current Time!";
				}

				if (Integer.parseInt(CNumeric(getMonthsBetween(ToLongDate(kknow()), ConvertLongDateToStr(baytrans_start_time)))) > 11) {
					msg += "<br>Next Follow-up can't exceed 11 months!";
				}

				if ((Integer.parseInt(ConvertLongDateToStr(baytrans_start_time).substring(8, 10)) > 21) || (Integer.parseInt(ConvertLongDateToStr(baytrans_start_time).substring(8, 10)) < 8)) {
					msg += "<br>Next Follow-up Time should be greater than 8 AM and less than 9 PM!";
				}
			}
		}

		if (baytrans_emp_id.equals("0")) {
			msg += "<br>Select Next Follow-up Service Advisor!";
		}
	}

	public String ListCustomerDetails(String comp_id, String jc_id)
	{
		StringBuilder Str = new StringBuilder();
		if (!comp_id.equals("0"))
		{
			StrSql = " SELECT customer_id, customer_name, contact_id, veh_contactable_id, "
					+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, contact_mobile1, variant_name,veh_reg_no "
					+ " FROM " + compdb(comp_id) + "axela_service_jc "
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id= veh_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE jc_id = " + jc_id
					+ " GROUP BY jc_id";
			// SOP("StrSql==ListCustomerDetails==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			try {

				Str.append("<center>");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-responsive\" data-filter=\"#filter\">\n");

				while (crs.next()) {
					Str.append("<tr align=center>\n");
					Str.append("<td >Customer: <a href=\"../customer/customer-list.jsp?customer_id=")
							.append(crs.getString("customer_id")).append(" \">")
							.append(crs.getString("customer_name")).append(" (")
							.append(crs.getString("customer_id")).append(")</td>\n");
					Str.append("<td>Contact: <a href=\"../customer/customer-contact-list.jsp?contact_id=")
							.append(crs.getString("contact_id")).append(" \">")
							.append(crs.getString("contacts")).append("</a></td>\n");
					Str.append("<td>Mobile: ").append(crs.getString("contact_mobile1"));
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append(ClickToCall(crs.getString("contact_mobile1"), comp_id));
					}
					Str.append("</td>\n");
					Str.append("<td>Variant: ").append(crs.getString("variant_name")).append("</td>\n");

					// Str.append("<td>Sale Date: ").append(strToShortDate(crs.getString("veh_sale_date"))).append("</td>\n");
					Str.append("<td>Registration No: ").append(crs.getString("veh_reg_no")).append("</td>\n");
					// Str.append("<td>Next Service Date: ").append(strToShortDate(crs.getString("veh_service_duedate"))).append("</td>\n");

					Str.append("</tr>");
					// if (type.equals("Service")) {
					// Str.append("<tr align=center>\n");
					// Str.append("<td>Kms: ").append(IndFormat(crs.getString("veh_kms"))).append("</td>\n");
					// Str.append("<td>Cal. Kms: ").append(IndFormat(crs.getString("veh_cal_kms"))).append("</td>\n");
					// Str.append("<td>Last Service Date: ").append(strToShortDate(crs.getString("veh_lastservice"))).append("</td>\n");
					// Str.append("<td colspan=\"2\">Last Service Kms: ").append(IndFormat(crs.getString("veh_lastservice_kms"))).append("</td>\n");
					// // .append("<td colspan=\"3\">&nbsp;</td>\n");
					// Str.append("<td colspan=\"2\">Contactable: ");
					// if (crs.getString("veh_contactable_id").equals("1")) {
					// Str.append("Yes").append("</td>\n");
					// } else if (crs.getString("veh_contactable_id").equals("2")) {
					// Str.append("No").append("</td>\n");
					// } else if (crs.getString("veh_contactable_id").equals("0")) {
					// Str.append("").append("</td>\n");
					// }
					// Str.append("</td>\n");
					// Str.append("<td></td>");
					// Str.append("<td></td>");
					// Str.append("</tr>");
					// }
				}
				Str.append("</table></div></center>\n");
				crs.close();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				return "";
			}
		}
		return Str.toString();
	}

	public void AddFields() {
		if (msg.equals("")) {
			// insurfollowup_id =
			// ExecuteQuery("SELECT COALESCE(MAX(insurfollowup_id), 0)"
			// + " FROM " + compdb(comp_id) + "axela_insurance_followup"
			// + " WHERE insurfollowup_veh_id = " + veh_id);
			// StrSql = "UPDATE " + compdb(comp_id) +
			// "axela_insurance_followup"
			// + " SET"
			// + " insurfollowup_desc = '" + insurfollowup_desc + "',"
			// + " insurfollowup_entry_time = '" + insurfollowup_entry_time +
			// "'"
			// + " WHERE insurfollowup_id = " + insurfollowup_id + ""
			// + " AND insurfollowup_desc = ''"
			// + " AND insurfollowup_veh_id = " + veh_id + "";
			// updateQuery(StrSql);
			// StrSql = "INSERT INTO " + compdb(comp_id) +
			// "axela_insurance_followup"
			// + " (insurfollowup_veh_id,"
			// + " insurfollowup_emp_id,"
			// + " insurfollowup_followuptype_id,"
			// + " insurfollowup_priorityinsurfollowup_id,"
			// + " insurfollowup_followup_time,"
			// + " insurfollowup_entry_id,"
			// + " insurfollowup_entry_time,"
			// + " insurfollowup_trigger)"
			// + " VALUES"
			// + " (" + veh_id + ","
			// + " " + insurfollowup_emp_id + ","
			// + " " + insurfollowup_followuptype_id + ","
			// + " " + insurfollowup_priorityinsurfollowup_id + ","
			// + " '" + ConvertLongDateToStr(insurfollowup_time) + "',"
			// + " " + insurfollowup_entry_id + ","
			// + " '" + insurfollowup_entry_time + "',"
			// + " 0)";
			// updateQuery(StrSql);
			// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
			// + " SET"
			// + " veh_priorityinsurfollowup_id = '" +
			// insurfollowup_priorityinsurfollowup_id + "'"
			// + " WHERE veh_id = " + veh_id + "";
			// updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT jc_id, jc_branch_id, jc_no, jc_time_in, jc_customer_id, jc_contact_id, jc_warranty,"
					+ " jc_comm_contact_id, jc_jctype_id, jc_jccat_id, jc_kms, jc_fuel_guage, jc_discamt, jc_title,"
					+ " jc_cust_voice, jc_advice, jc_instructions, jc_terms, jc_emp_id,"
					+ " jc_technician_emp_id, jc_ro_no, jc_bill_cash_no, jc_bill_insur_no,"
					+ " jc_time_promised, jc_time_ready, jc_time_out, jc_time_posted,"
					+ " jc_jcstage_id, jc_priorityjc_id, jc_bay_id, jc_location_id,"
					+ " jc_bill_cash_date, jc_bill_cash_parts, jc_bill_cash_parts_tyre_qty,"
					+ " jc_bill_cash_parts_tyre, jc_bill_cash_parts_oil, jc_bill_cash_parts_battery_qty,"
					+ " jc_bill_cash_parts_battery, jc_bill_cash_parts_brake_qty, jc_bill_cash_parts_brake,"
					+ " jc_bill_cash_parts_accessories, jc_bill_cash_parts_valueadd,"
					+ " jc_bill_cash_parts_extwarranty_qty, jc_bill_cash_parts_extwarranty, jc_bill_cash_parts_wheelalign,"
					+ " jc_bill_cash_parts_cng, jc_bill_cash_parts_discamt, jc_bill_cash_labour,"
					+ " jc_bill_cash_labour_tyre_qty, jc_bill_cash_labour_tyre, jc_bill_cash_labour_oil,"
					+ " jc_bill_cash_labour_battery_qty, jc_bill_cash_labour_battery, jc_bill_cash_labour_brake_qty,"
					+ " jc_bill_cash_labour_brake, jc_bill_cash_labour_accessories, jc_bill_cash_labour_valueadd,"
					+ " jc_bill_cash_labour_extwarranty_qty, jc_bill_cash_labour_extwarranty,"
					+ " jc_bill_cash_labour_wheelalign, jc_bill_cash_labour_cng, jc_bill_cash_labour_discamt,"
					+ " jc_bill_insur_date, jc_bill_insur_parts, jc_bill_insur_parts_tyre_qty,"
					+ " jc_bill_insur_parts_tyre, jc_bill_insur_parts_oil, jc_bill_insur_parts_battery_qty, jc_bill_insur_parts_battery,"
					+ " jc_bill_insur_parts_brake_qty, jc_bill_insur_parts_brake, jc_bill_insur_parts_accessories,"
					+ " jc_bill_insur_parts_valueadd, jc_bill_insur_parts_extwarranty_qty,"
					+ " jc_bill_insur_parts_extwarranty, jc_bill_insur_parts_wheelalign, jc_bill_insur_parts_cng, jc_bill_insur_parts_discamt,"
					+ " jc_bill_insur_labour, jc_bill_insur_labour_tyre_qty, jc_bill_insur_labour_tyre,"
					+ " jc_bill_insur_labour_oil, jc_bill_insur_labour_battery_qty, jc_bill_insur_labour_battery,"
					+ " jc_bill_insur_labour_brake_qty, jc_bill_insur_labour_brake, jc_bill_insur_labour_accessories,"
					+ " jc_bill_insur_labour_valueadd, jc_bill_insur_labour_extwarranty_qty, jc_bill_insur_labour_extwarranty,"
					+ " jc_bill_insur_labour_wheelalign, jc_bill_insur_labour_cng, jc_bill_insur_labour_discamt,"
					+ " jc_bill_cash_customername, jc_bill_insur_customername, jc_bill_foc_customername,"
					+ " jc_bill_warranty_customername, jc_bill_warranty_date, jc_bill_foc_date, jc_bill_warranty_no, jc_bill_foc_no,"

					+ " jc_bill_warranty_parts, jc_bill_warranty_parts_tyre_qty,"
					+ " jc_bill_warranty_parts_tyre, jc_bill_warranty_parts_oil, jc_bill_warranty_parts_battery_qty,"
					+ " jc_bill_warranty_parts_battery, jc_bill_warranty_parts_brake_qty, jc_bill_warranty_parts_brake,"
					+ " jc_bill_warranty_parts_accessories, jc_bill_warranty_parts_valueadd,"
					+ " jc_bill_warranty_parts_extwarranty_qty, jc_bill_warranty_parts_extwarranty, jc_bill_warranty_parts_wheelalign,"
					+ " jc_bill_warranty_parts_cng, jc_bill_warranty_parts_discamt, jc_bill_warranty_labour,"
					+ " jc_bill_warranty_labour_tyre_qty, jc_bill_warranty_labour_tyre, jc_bill_warranty_labour_oil,"
					+ " jc_bill_warranty_labour_battery_qty, jc_bill_warranty_labour_battery, jc_bill_warranty_labour_brake_qty,"
					+ " jc_bill_warranty_labour_brake, jc_bill_warranty_labour_accessories, jc_bill_warranty_labour_valueadd,"
					+ " jc_bill_warranty_labour_extwarranty_qty, jc_bill_warranty_labour_extwarranty,"
					+ " jc_bill_warranty_labour_wheelalign, jc_bill_warranty_labour_cng, jc_bill_warranty_labour_discamt,"

					+ " jc_critical, jc_notes, jc_entry_id, jc_entry_date, jc_modified_id, jc_modified_date,"
					+ " veh_id, veh_variant_id, veh_engine_no, veh_chassis_no, veh_reg_no,"
					+ " COALESCE(customer.customer_name, '') AS customer_name, COALESCE(customer.customer_id, '') AS customer_id,"
					+ " COALESCE(refcustomer.customer_name, '') AS refcustomer_name,"
					+ " COALESCE(refcustomer.customer_id, '') AS refcustomer_id,"
					+ " contact_address, contact_title_id,"
					+ " contact_fname, contact_lname, contact_id, contact_email1,"
					+ " contact_mobile1, contact_mobile2, contact_phone1, contact_phone2, contact_city_id,"
					+ " contact_pin, title_desc, branch_name, branch_code, branch_rateclass_id,"
					+ " COALESCE(variant_name,'') AS variant_name, preownedmodel_id, "
					+ " COALESCE(preownedmodel_name,'') AS preownedmodel_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer customer ON customer.customer_id = jc_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer refcustomer ON refcustomer.customer_id = jc_ref_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp service ON service.emp_id = jc_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp technician ON technician.emp_id = jc_technician_emp_id"
					+ " WHERE 1=1 "
					+ " AND jc_id = " + jc_id
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ ExeAccess.replace("emp_id", "jc_emp_id") + ""
					+ " GROUP BY jc_id";
			// SOP("StrSql==PopulateFields==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					jc_id = crs.getString("jc_id");
					jc_no = "JC" + crs.getString("branch_code") + crs.getString("jc_no");
					jc_branch_id = crs.getString("jc_branch_id");
					rateclass_id = crs.getString("branch_rateclass_id");
					branch_name = crs.getString("branch_name");
					jc_customer_id = crs.getString("jc_customer_id");
					jc_customer_name = crs.getString("customer_name");
					if (crs.getString("jc_warranty").equals("1")) {
						jcwarranty = "Warranty";
					}

					contact_title_id = crs.getString("contact_title_id");
					contact_fname = crs.getString("contact_fname");
					contact_lname = crs.getString("contact_lname");
					contact_phone1 = crs.getString("contact_phone1");
					contact_phone2 = crs.getString("contact_phone2");
					contact_mobile1 = crs.getString("contact_mobile1");
					contact_mobile2 = crs.getString("contact_mobile2");
					contact_email1 = crs.getString("contact_email1");
					contact_address = crs.getString("contact_address");
					contact_city_id = crs.getString("contact_city_id");
					contact_pin = crs.getString("contact_pin");
					jc_time_in = crs.getString("jc_time_in");
					date = strToLongDate(jc_time_in);
					jc_time_promised = crs.getString("jc_time_promised");
					jc_promisetime = strToLongDate(jc_time_promised);
					jc_time_posted = crs.getString("jc_time_posted");
					if (!jc_time_posted.equals("")) {
						jc_postedtime = strToLongDate(jc_time_posted);
					}
					jc_time_out = crs.getString("jc_time_out");
					jc_timeout = strToLongDate(jc_time_out);
					jc_ready_time = crs.getString("jc_time_ready");
					jc_readytime = strToLongDate(jc_ready_time);
					jc_emp_id = crs.getString("jc_emp_id");
					jc_technician_emp_id = crs.getString("jc_technician_emp_id");
					jc_location_id = crs.getString("jc_location_id");
					jc_model_name = crs.getString("preownedmodel_name");
					invent_model_id = crs.getString("preownedmodel_id");
					variant_id = crs.getString("veh_variant_id");
					jc_item_name = crs.getString("variant_name");

					jc_bill_discount = crs.getString("jc_discamt");

					// Bill Cash fields
					jc_bill_cash_customername = crs.getString("jc_bill_cash_customername");
					jc_bill_cash_no = crs.getString("jc_bill_cash_no");
					jc_bill_cash_date = crs.getString("jc_bill_cash_date");
					jc_bill_cash_date = strToShortDate(jc_bill_cash_date);

					// Bill Cash Parts fields
					jc_bill_cash_parts = crs.getString("jc_bill_cash_parts");
					jc_bill_cash_parts_tyre = crs.getString("jc_bill_cash_parts_tyre");
					jc_bill_cash_parts_oil = crs.getString("jc_bill_cash_parts_oil");
					jc_bill_cash_parts_accessories = crs.getString("jc_bill_cash_parts_accessories");
					jc_bill_cash_parts_valueadd = crs.getString("jc_bill_cash_parts_valueadd");
					jc_bill_cash_parts_battery = crs.getString("jc_bill_cash_parts_battery");
					jc_bill_cash_parts_brake = crs.getString("jc_bill_cash_parts_brake");
					jc_bill_cash_parts_extwarranty = crs.getString("jc_bill_cash_parts_extwarranty");
					jc_bill_cash_parts_wheelalign = crs.getString("jc_bill_cash_parts_wheelalign");
					jc_bill_cash_parts_cng = crs.getString("jc_bill_cash_parts_cng");
					jc_bill_cash_parts_tyre_qty = crs.getString("jc_bill_cash_parts_tyre_qty");
					jc_bill_cash_parts_brake_qty = crs.getString("jc_bill_cash_parts_brake_qty");
					jc_bill_cash_parts_battery_qty = crs.getString("jc_bill_cash_parts_battery_qty");
					jc_bill_cash_parts_extwarranty_qty = crs.getString("jc_bill_cash_parts_extwarranty_qty");
					jc_bill_cash_parts_discamt = crs.getString("jc_bill_cash_parts_discamt");

					// Bill Cash Labour fields
					jc_bill_cash_labour = crs.getString("jc_bill_cash_labour");
					jc_bill_cash_labour_tyre = crs.getString("jc_bill_cash_labour_tyre");
					jc_bill_cash_labour_oil = crs.getString("jc_bill_cash_labour_oil");
					jc_bill_cash_labour_accessories = crs.getString("jc_bill_cash_labour_accessories");
					jc_bill_cash_labour_valueadd = crs.getString("jc_bill_cash_labour_valueadd");
					jc_bill_cash_labour_battery = crs.getString("jc_bill_cash_labour_battery");
					jc_bill_cash_labour_brake = crs.getString("jc_bill_cash_labour_brake");
					jc_bill_cash_labour_extwarranty = crs.getString("jc_bill_cash_labour_extwarranty");
					jc_bill_cash_labour_wheelalign = crs.getString("jc_bill_cash_labour_wheelalign");
					jc_bill_cash_labour_cng = crs.getString("jc_bill_cash_labour_cng");
					jc_bill_cash_labour_tyre_qty = crs.getString("jc_bill_cash_labour_tyre_qty");
					jc_bill_cash_labour_brake_qty = crs.getString("jc_bill_cash_labour_brake_qty");
					jc_bill_cash_labour_battery_qty = crs.getString("jc_bill_cash_labour_battery_qty");
					jc_bill_cash_labour_extwarranty_qty = crs.getString("jc_bill_cash_labour_extwarranty_qty");
					jc_bill_cash_labour_discamt = crs.getString("jc_bill_cash_labour_discamt");

					// Bill Insurance fields
					jc_bill_insur_customername = crs.getString("jc_bill_insur_customername");
					jc_bill_insur_no = crs.getString("jc_bill_insur_no");
					jc_bill_insur_date = crs.getString("jc_bill_insur_date");
					jc_bill_insur_date = strToShortDate(jc_bill_insur_date);

					// Bill Insurance Parts fields
					jc_bill_insur_parts = crs.getString("jc_bill_insur_parts");
					jc_bill_insur_parts_tyre = crs.getString("jc_bill_insur_parts_tyre");
					jc_bill_insur_parts_oil = crs.getString("jc_bill_insur_parts_oil");
					jc_bill_insur_parts_accessories = crs.getString("jc_bill_insur_parts_accessories");
					jc_bill_insur_parts_valueadd = crs.getString("jc_bill_insur_parts_valueadd");
					jc_bill_insur_parts_battery = crs.getString("jc_bill_insur_parts_battery");
					jc_bill_insur_parts_brake = crs.getString("jc_bill_insur_parts_brake");
					jc_bill_insur_parts_extwarranty = crs.getString("jc_bill_insur_parts_extwarranty");
					jc_bill_insur_parts_wheelalign = crs.getString("jc_bill_insur_parts_wheelalign");
					jc_bill_insur_parts_cng = crs.getString("jc_bill_insur_parts_cng");
					jc_bill_insur_parts_tyre_qty = crs.getString("jc_bill_insur_parts_tyre_qty");
					jc_bill_insur_parts_brake_qty = crs.getString("jc_bill_insur_parts_brake_qty");
					jc_bill_insur_parts_battery_qty = crs.getString("jc_bill_insur_parts_battery_qty");
					jc_bill_insur_parts_extwarranty_qty = crs.getString("jc_bill_insur_parts_extwarranty_qty");
					jc_bill_insur_parts_discamt = crs.getString("jc_bill_insur_parts_discamt");

					// Bill Insurance Labour fields
					jc_bill_insur_labour = crs.getString("jc_bill_insur_labour");
					jc_bill_insur_labour_tyre = crs.getString("jc_bill_insur_labour_tyre");
					jc_bill_insur_labour_oil = crs.getString("jc_bill_insur_labour_oil");
					jc_bill_insur_labour_accessories = crs.getString("jc_bill_insur_labour_accessories");
					jc_bill_insur_labour_valueadd = crs.getString("jc_bill_insur_labour_valueadd");
					jc_bill_insur_labour_battery = crs.getString("jc_bill_insur_labour_battery");
					jc_bill_insur_labour_brake = crs.getString("jc_bill_insur_labour_brake");
					jc_bill_insur_labour_extwarranty = crs.getString("jc_bill_insur_labour_extwarranty");
					jc_bill_insur_labour_wheelalign = crs.getString("jc_bill_insur_labour_wheelalign");
					jc_bill_insur_labour_cng = crs.getString("jc_bill_insur_labour_cng");
					jc_bill_insur_labour_tyre_qty = crs.getString("jc_bill_insur_labour_tyre_qty");
					jc_bill_insur_labour_brake_qty = crs.getString("jc_bill_insur_labour_brake_qty");
					jc_bill_insur_labour_battery_qty = crs.getString("jc_bill_insur_labour_battery_qty");
					jc_bill_insur_labour_extwarranty_qty = crs.getString("jc_bill_insur_labour_extwarranty_qty");
					jc_bill_insur_labour_discamt = crs.getString("jc_bill_insur_labour_discamt");

					// Bill FOC fields
					jc_bill_foc_customername = crs.getString("jc_bill_foc_customername");
					jc_bill_foc_no = crs.getString("jc_bill_foc_no");
					jc_bill_foc_date = crs.getString("jc_bill_foc_date");
					jc_bill_foc_date = strToShortDate(jc_bill_foc_date);

					// Bill Warranty fields
					jc_bill_warranty_customername = crs.getString("jc_bill_warranty_customername");
					jc_bill_warranty_no = crs.getString("jc_bill_warranty_no");
					jc_bill_warranty_date = crs.getString("jc_bill_warranty_date");
					jc_bill_warranty_date = strToShortDate(jc_bill_warranty_date);

					// Bill Warranty Parts fields
					jc_bill_warranty_parts = crs.getString("jc_bill_warranty_parts");
					jc_bill_warranty_parts_tyre = crs.getString("jc_bill_warranty_parts_tyre");
					jc_bill_warranty_parts_oil = crs.getString("jc_bill_warranty_parts_oil");
					jc_bill_warranty_parts_accessories = crs.getString("jc_bill_warranty_parts_accessories");
					jc_bill_warranty_parts_valueadd = crs.getString("jc_bill_warranty_parts_valueadd");
					jc_bill_warranty_parts_battery = crs.getString("jc_bill_warranty_parts_battery");
					jc_bill_warranty_parts_brake = crs.getString("jc_bill_warranty_parts_brake");
					jc_bill_warranty_parts_extwarranty = crs.getString("jc_bill_warranty_parts_extwarranty");
					jc_bill_warranty_parts_wheelalign = crs.getString("jc_bill_warranty_parts_wheelalign");
					jc_bill_warranty_parts_cng = crs.getString("jc_bill_warranty_parts_cng");
					jc_bill_warranty_parts_tyre_qty = crs.getString("jc_bill_warranty_parts_tyre_qty");
					jc_bill_warranty_parts_brake_qty = crs.getString("jc_bill_warranty_parts_brake_qty");
					jc_bill_warranty_parts_battery_qty = crs.getString("jc_bill_warranty_parts_battery_qty");
					jc_bill_warranty_parts_extwarranty_qty = crs.getString("jc_bill_warranty_parts_extwarranty_qty");
					jc_bill_warranty_parts_discamt = crs.getString("jc_bill_warranty_parts_discamt");

					// Bill warranty Labour fields
					jc_bill_warranty_labour = crs.getString("jc_bill_warranty_labour");
					jc_bill_warranty_labour_tyre = crs.getString("jc_bill_warranty_labour_tyre");
					jc_bill_warranty_labour_oil = crs.getString("jc_bill_warranty_labour_oil");
					jc_bill_warranty_labour_accessories = crs.getString("jc_bill_warranty_labour_accessories");
					jc_bill_warranty_labour_valueadd = crs.getString("jc_bill_warranty_labour_valueadd");
					jc_bill_warranty_labour_battery = crs.getString("jc_bill_warranty_labour_battery");
					jc_bill_warranty_labour_brake = crs.getString("jc_bill_warranty_labour_brake");
					jc_bill_warranty_labour_extwarranty = crs.getString("jc_bill_warranty_labour_extwarranty");
					jc_bill_warranty_labour_wheelalign = crs.getString("jc_bill_warranty_labour_wheelalign");
					jc_bill_warranty_labour_cng = crs.getString("jc_bill_warranty_labour_cng");
					jc_bill_warranty_labour_tyre_qty = crs.getString("jc_bill_warranty_labour_tyre_qty");
					jc_bill_warranty_labour_brake_qty = crs.getString("jc_bill_warranty_labour_brake_qty");
					jc_bill_warranty_labour_battery_qty = crs.getString("jc_bill_warranty_labour_battery_qty");
					jc_bill_warranty_labour_extwarranty_qty = crs.getString("jc_bill_warranty_labour_extwarranty_qty");
					jc_bill_warranty_labour_discamt = crs.getString("jc_bill_warranty_labour_discamt");

					veh_id = crs.getString("veh_id");
					veh_chassis_no = crs.getString("veh_chassis_no");
					veh_engine_no = crs.getString("veh_engine_no");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					jc_fuel_guage = crs.getString("jc_fuel_guage");
					jc_kms = crs.getString("jc_kms");
					jc_type_id = crs.getString("jc_jctype_id");
					jc_cat_id = crs.getString("jc_jccat_id");
					jc_title = crs.getString("jc_title");

					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id")
							+ "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " "
							+ crs.getString("contact_lname") + "</a>";
					link_refcustomer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("refcustomer_id")
							+ "\">" + crs.getString("refcustomer_name") + "</a>";
					jc_cust_voice = crs.getString("jc_cust_voice");
					jc_advice = crs.getString("jc_advice");
					jc_terms = crs.getString("jc_terms");
					jc_instr = crs.getString("jc_instructions");
					jc_customer_id = crs.getString("jc_customer_id");
					jc_contact_id = crs.getString("jc_contact_id");
					jc_comm_contact_id = crs.getString("jc_comm_contact_id");
					jc_ro_no = crs.getString("jc_ro_no");
					jc_priorityjc_id = crs.getString("jc_priorityjc_id");
					jc_notes = crs.getString("jc_notes");
					jc_stage_id = crs.getString("jc_jcstage_id");
					jc_bay_id = crs.getString("jc_bay_id");
					jc_critical = crs.getString("jc_critical");
					jc_entry_id = crs.getString("jc_entry_id");
					entry_by = Exename(comp_id, crs.getInt("jc_entry_id"));
					entry_date = strToLongDate(crs.getString("jc_entry_date"));
					jc_modified_id = crs.getString("jc_modified_id");
					if (!jc_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(jc_modified_id));
						modified_date = strToLongDate(crs.getString("jc_modified_date"));
					}
				}
				jc_check = ListJCCheckList();
				jc_docs = ListJCDocs();
				jc_images = ListJCImages();
				gst_type = PadQuotes(new Ledger_Check().CompareState(comp_id, jc_customer_id, jc_branch_id));
				jc_inventory = ListJCInventory();
				jc_manhours = ListJCManHours(jc_id, "0");
				jc_history = ListJCHistory();
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void ContactCheckForm() {
		contact_msg = "";

		if (new_contact_title_id.equals("0")) {
			contact_msg += "<br>Select Contact Title for New Contact!";
		}

		if (new_contact_fname.equals("")) {
			contact_msg += "<br>Enter Contact First Name for New Contact!";
		}

		if (new_contact_mobile1.equals("")) {
			contact_msg += "<br>Enter Contact Mobile 1 for New Contact!";
		} else if (!IsValidMobileNo11(new_contact_mobile1)) {
			contact_msg += "<br>Enter valid Contact Mobile 1 for New Contact!";
		} else {
			StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " WHERE contact_mobile1 = '" + new_contact_mobile1 + "'"
					+ " OR contact_mobile2 = '" + new_contact_mobile1 + "'";
			if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
				contact_msg += "<br>Similar Contact Mobile 1 found for New Contact!";
			}
		}

		if (new_contact_email1.equals("")) {
			contact_msg += "<br>Enter Contact Email 1 for New Contact!";
		} else if (!IsValidEmail(new_contact_email1)) {
			contact_msg += "<br>Enter valid Contact Email 1 for New Contact!";
		}

		if (new_contact_contacttype_id.equals("0")) {
			contact_msg += "<br>Select Contact Type for New Contact!";
		}
	}

	public void AddNewContact() {
		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_customer_contact"
				+ " (contact_customer_id,"
				+ " contact_contacttype_id,"
				+ " contact_title_id,"
				+ " contact_fname,"
				+ " contact_lname,"
				+ " contact_mobile1,"
				+ " contact_email1,"
				+ " contact_address,"
				+ " contact_city_id,"
				+ " contact_pin,"
				+ " contact_active,"
				+ " contact_notes,"
				+ " contact_entry_id,"
				+ " contact_entry_date)"
				+ " VALUES"
				+ " (" + jc_customer_id + ","
				+ " " + new_contact_contacttype_id + ","
				+ " " + new_contact_title_id + ","
				+ " '" + new_contact_fname + "',"
				+ " '" + new_contact_lname + "',"
				+ " '" + new_contact_mobile1 + "',"
				+ " '" + new_contact_email1 + "',"
				+ " '" + contact_address + "',"
				+ " " + contact_city_id + ","
				+ " '" + contact_pin + "',"
				+ " 1,"
				+ " '" + new_contact_notes + "',"
				+ " " + emp_id + ","
				+ " '" + ToLongDate(kknow()) + "')";
		updateQuery(StrSql);
	}

	public String ListJCCheckList() {
		CachedRowSet crs = null;
		int count = 0;
		String checked = "";
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT check_id, check_name, check_type,"
				+ " (SELECT COUNT(DISTINCT(check_id))"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
				+ " WHERE check_item_id = " + variant_id + ""
				+ " AND check_type = 1) AS count,"
				+ " COALESCE((SELECT IF(trans_jc_id > 0, 1, 0)"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
				+ " WHERE trans_jc_id = " + jc_id + ""
				+ " AND trans_check_id = check_id), 0) AS checked"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
				+ " WHERE check_item_id = " + variant_id + ""
				+ " AND check_type = 1"
				+ " GROUP BY check_id"
				+ " ORDER BY check_name";
		// SOP("StrSql==ListJCCheckList==" + StrSql);
		try {
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				int ins_count = 0;
				crs.last();
				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"table\">\n");
				Str.append("<div class=\"hint\" id=\"hint_chk_check_id\"></div><br>");
				Str.append("<tr>\n<td colspan=\"4\" align=\"right\"><a href=\"javascript:CheckAllChecklist('1');\">Check All</a></td>\n</tr>\n");
				Str.append("<tr>\n<th colspan=\"4\" align=\"center\"><b>Inspection</b></th>\n</tr>\n<tr>\n");
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					ins_count++;
					if (i > 4) {
						Str.append("</tr>\n<tr>\n");
						i = 1;
					}

					if (crs.getString("checked").equals("1")) {
						checked = "checked";
					} else {
						checked = "";
					}

					Str.append("<td width=\"25%\">\n");
					Str.append("<input type=\"checkbox\" id=\"chk_check_id").append(count).append("\" name=\"chk_check_id");
					Str.append(count).append("\" value=").append(crs.getString("check_id")).append(" ");
					Str.append("onClick=\"javascript:ChecklistItem(").append(count);
					Str.append(", 'chk_check_id").append(count).append("', this.value, 'hint_chk_check_id');\"");
					Str.append(" ").append(checked).append(">").append(crs.getString("check_name"));
					Str.append("</td>\n");
					i++;
				}

				if (i < 5) {
					for (int x = i; x < 5; x++) {
						Str.append("<td>&nbsp;</td>\n");
					}
				}
				Str.append("<input type=\"hidden\" id=\"txt_ins_check_count\" name=\"txt_ins_check_count\" value=\"").append(ins_count).append("\">");
				Str.append("</tr>\n");
				Str.append("</table>\n");
			}
			crs.close();

			StrSql = "SELECT check_id, check_name, check_type,"
					+ " (SELECT COUNT(DISTINCT(check_id))"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
					+ " WHERE check_item_id = " + variant_id + ""
					+ " AND check_type = 2) AS count,"
					+ " COALESCE((SELECT IF(trans_jc_id > 0, 1, 0)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
					+ " WHERE trans_jc_id = " + jc_id + ""
					+ " AND trans_check_id = check_id), 0) AS checked"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
					+ " WHERE check_item_id = " + variant_id + ""
					+ " AND check_type = 2"
					+ " GROUP BY check_id"
					+ " ORDER BY check_name";
			crs = processQuery(StrSql, 0);
			checked = "";

			if (crs.isBeforeFirst()) {
				int wash_count = 0;
				crs.last();
				Str.append("<br/><br/><table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
				Str.append("<tr>\n<td colspan=\"4\" align=\"right\"><a href=\"javascript:CheckAllChecklist('2');\">Check All</a></td>\n</tr>\n");
				Str.append("<tr>\n<th colspan=\"4\" align=\"center\">Washing</th>\n</tr>\n");
				crs.beforeFirst();
				while (crs.next()) {
					count++;
					wash_count++;
					if (j > 4) {
						Str.append("</tr>\n<tr>\n");
						j = 1;
					}

					if (crs.getString("checked").equals("1")) {
						checked = "checked";
					} else {
						checked = "";
					}

					Str.append("<td width=\"25%\">\n");
					Str.append("<input type=\"checkbox\" id=\"chk_check_id").append(count);
					Str.append("\" name=\"chk_check_id").append(count).append("\" value=").append(crs.getString("check_id"));
					Str.append(" onClick=\"javascript:ChecklistItem(").append(count);
					Str.append(", 'chk_check_id").append(count).append("', this.value, 'hint_chk_check_id');\"");
					Str.append(" ").append(checked).append(">").append(crs.getString("check_name"));
					Str.append("</td>\n");
					j++;
				}

				if (j < 5) {
					for (int y = j; y < 5; y++) {
						Str.append("<td>&nbsp;</td>\n");
					}
				}
				Str.append("<input type=\"hidden\" id=\"txt_wash_check_count\" name=\"txt_wash_check_count\" value=\"").append(wash_count).append("\">");
				Str.append("</tr>\n");
				Str.append("</table>\n");
			}
			crs.close();
			if (Str.toString().equals("")) {
				Str.append("<br><br><br><br><br><center><b><font color=\"red\">No Check list found!</font></b></center><br><br><br><br><br>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListJCDocs() {
		StringBuilder Str = new StringBuilder();
		int count = 0;
		StrSql = "SELECT doc_id, doc_value, doc_title, doc_remarks"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_docs"
				+ " WHERE doc_jc_id = " + jc_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"table\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th width=\"5%\">#</th>\n");
				Str.append("<th>Document Details</th>\n");
				Str.append("<th width=\"20%\">Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					if (!crs.getString("doc_value").equals("")) {
						if (!new File(JobCardDocPath(comp_id)).exists()) {
							new File(JobCardDocPath(comp_id)).mkdirs();
						}

						File f = new File(JobCardDocPath(comp_id) + crs.getString("doc_value"));
						Str.append("<td valign=\"top\" align=\"left\"><a href=../Fetchdocs.do?jc_doc_id=").append(crs.getString("doc_id")).append(">");
						Str.append(crs.getString("doc_title")).append(" (").append(ConvertFileSizeToBytes(FileSize(f)));
						Str.append(")</a><br>").append(crs.getString("doc_remarks")).append("</td>\n");
					} else {
						Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("doc_title")).append(" (0 Bytes)<br> ");
						Str.append(crs.getString("doc_remarks")).append("</td>\n");
					}

					if (!jc_id.equals("0")) {
						Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/docs-update.jsp?update=yes&jc_id=");
						Str.append(jc_id).append("&doc_id=").append(crs.getString("doc_id")).append("\">Update Document</a></td>\n");
					}
					Str.append("</tr>\n");
				}
				crs.close();
				Str.append("</table>\n");
			} else {
				msg = "";
				Str.append("<br><br><br><br><br><center><font color=\"red\"><b>No Document(s) found!</b></font></center><br><br><br><br><br>");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListJCImages() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT img_id, img_value, img_title"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_img"
				+ " WHERE img_jc_id = " + jc_id + StrSearch + "";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"container-fluid\">\n");
				while (crs.next()) {
					if (count == 4) {
						Str.append("</div>\n");
						Str.append("<div class=\"container-fluid\">\n");
						count = 0;
					}
					Str.append("<div class=\"col-md-3 col-sm-3\">\n");
					if (!crs.getString("img_value").equals("")) {
						File f = new File(JobCardImgPath(comp_id) + crs.getString("img_value"));
						Str.append("<img src=\"../Thumbnail.do?jcimg=").append(crs.getString("img_value")).append("&width=175\"/></b>");
						Str.append("<br><b>").append(crs.getString("img_title")).append("</b>");
						Str.append("<br>(").append(ConvertFileSizeToBytes(FileSize(f))).append(")<br>");
					} else {
						Str.append("<b>").append(crs.getString("img_title")).append(" (").append(crs.getString("img_id")).append(") (0 Bytes)</b><br>");
					}

					if (!jc_id.equals("0")) {
						Str.append("<a href=\"../service/jobcard-image-update.jsp?update=yes&jc_id=").append(jc_id);
						Str.append("&img_id=").append(crs.getString("img_id")).append("\">Update Image</a>");
					}
					Str.append("</div>\n");

					count++;
				}
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><br><br><center><font color=\"red\"><b>No Image found!</b></font></center><br><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListJCInventory() {
		String checked = "", jc_inventory = "";
		int count = 0, td_count = 0;
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT COALESCE(invent_id, 0) AS invent_id,"
				+ " COALESCE(invent_name, '') AS invent_name,"
				+ " COALESCE(jc_inventory, '') AS jc_inventory,"
				+ " COALESCE(inventtrans_jc_id, 0) AS inventtrans_jc_id"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_invent"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_invent_trans ON inventtrans_invent_id = invent_id"
				+ " AND inventtrans_jc_id = " + jc_id + ""
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = " + jc_id + ""
				+ " WHERE invent_model_id = " + invent_model_id + ""
				+ " GROUP BY invent_id"
				+ " ORDER BY invent_name";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("");
			Str.append("<center><div class=\"hint\" id=\"hint_jc_invent_id\"></div></center>\n");
			// Str.append("Car Inventory");
			while (crs.next()) {
				if (!crs.getString("jc_inventory").equals("")) {
					jc_inventory = crs.getString("jc_inventory");
				}

				if (!crs.getString("invent_id").equals("0")) {
					checked = "";
					count++;
					td_count++;
					if (td_count > 4) {
						td_count = 1;
						if (count != 1) {
							Str.append("\n");
						}
					}

					if (!crs.getString("inventtrans_jc_id").equals("0")) {
						checked = "checked";
					}

					Str.append("<input id=\"chk_veh_invent").append(count).append("\" name=\"chk_veh_invent");
					Str.append(count).append("\" value=\"").append(crs.getString("invent_id")).append("\" type=\"checkbox\" ").append(checked);
					Str.append(" onclick=\"UpdateInventory(this.id, this.value,'").append(crs.getString("invent_name")).append("');\"/>");
					Str.append(crs.getString("invent_name"));
					Str.append("<input type=\"hidden\" id=\"txt_veh_invent").append(count).append("\" name=\"txt_veh_invent");
					Str.append(count).append("\" value=\"").append(crs.getString("invent_id")).append("\">\n");
				}
			}

			if (td_count < 4 && td_count != 0) {
				for (int i = td_count; i < 4; i++) {
					Str.append("\n");
				}
			}

			Str.append("<div class=\"form-group\"><label class=\"control-label col-md-4\"\n");
			Str.append(">\n");
			Str.append("Other Inventory: </label>\n");
			Str.append("<div class=\"col-md-6 col-xs-12\" id=\"emprows\">");
			Str.append("<textarea name=\"txt_jc_inventory\" id=\"txt_jc_inventory\" cols=\"70\" rows=\"5\"");
			Str.append(" class=\"form-control\" onChange=\"UpdateJCInventory(this.value);\">").append(jc_inventory).append("</textarea>");
			Str.append("</div>");
			Str.append("</div>");
			Str.append("");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String ListJCManHours(String jc_id, String baytrans_emp_id) {
		StringBuilder Str = new StringBuilder();
		int hrs = 0, mins = 0, total = 0;
		String endtime = "";
		// if (!baytrans_emp_id.equals("0")) {
		// StrSearch = " AND emp_id = " + baytrans_emp_id + "";
		// }

		StrSql = "SELECT bay_id, COALESCE(bay_name, '') AS bay_name,"
				+ " COALESCE(baytrans_id, 0) AS baytrans_id,"
				+ " COALESCE(baytrans_start_time, '') AS baytrans_start_time,"
				+ " COALESCE(baytrans_end_time, '') AS baytrans_end_time,"
				+ " emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = baytrans_emp_id"
				+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_bay ON baytrans_bay_id = bay_id"
				+ " WHERE baytrans_jc_id = " + jc_id
				// + BranchAccess.replace("branch_id", "bay_branch_id")
				+ StrSearch + ""
				+ " ORDER BY baytrans_start_time";
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<table width=\"100%\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"table\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<td align=right colspan=6>\n");
				Str.append("<a href=\"javascript:window.parent.AddNewManHour(").append(jc_id).append(");\" id=\"new_man_hour_link\">Add New Man Hours...</a></td>");
				Str.append("</tr>\n<tr align=\"center\">\n");
				Str.append("<th>Technician</th>\n");
				Str.append("<th>Bay</th>\n");
				Str.append("<th>Start Time</th>\n");
				Str.append("<th>End Time</th>\n");
				Str.append("<th>Man Hours</th>\n");
				Str.append("<th>Action</th>\n");
				Str.append("</tr>\n");

				while (crs.next()) {
					if (!crs.getString("baytrans_end_time").equals("")) {
						endtime = crs.getString("baytrans_end_time");
						hrs = +(int) getHoursBetween(StringToDate(crs.getString("baytrans_start_time")), StringToDate(endtime));
						mins = +(int) getMinBetween(StringToDate(crs.getString("baytrans_start_time")), StringToDate(endtime));
					} else {
						endtime = "";
						hrs = 0;
						mins = 0;
					}
					total += (hrs * 60) + mins;

					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("bay_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("baytrans_start_time"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("baytrans_end_time"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"right\">");
					if (!endtime.equals("")) {
						Str.append(ConvertMintoHrsMins((hrs * 60) + mins));
					} else {
						Str.append("");
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">");
					Str.append("<a href=\"javascript:window.parent.AddNewManHourUpdate(").append(crs.getString("baytrans_id")).append(");\" id=\"new_man_hour_link\">Update Man Hours</a>");
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("<tr>\n");
				Str.append("<td colspan=\"4\" align=\"right\"><b>Total:</b></td>\n");
				Str.append("<td valign=\"top\" align=\"right\"><b>").append(ConvertMintoHrsMins(total)).append("</b></td>\n");
				Str.append("<td >&nbsp;</td>\n");
				Str.append("</tr>\n");
				Str.append("</table>\n");
				flag = "1";
			} else {
				flag = "0";
				Str.append("<table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<td align=\"right\" colspan=\"6\">");
				Str.append("<a href=\"javascript:window.parent.AddNewManHour(").append(jc_id).append(");\" id=\"new_man_hour_link\">Add New Man Hours...</a>");
				Str.append("</td>\n");
				Str.append("</tr>\n");
				Str.append("</table>\n");
				Str.append("<br><br><br><br><br><center><font color=\"red\"><b>No Man Hour(s) found!</b></font></center><br><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCustomerContacts(String jc_customer_id, String jc_comm_contact_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT contact_id, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname, ' (', contacttype_name, ')') AS contactname"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact_type ON contacttype_id = contact_contacttype_id"
					+ " WHERE contact_customer_id = " + jc_customer_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contact_id"));
				Str.append(StrSelectdrop(crs.getString("contact_id"), jc_comm_contact_id));
				Str.append(">").append(crs.getString("contactname")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String ListPSFFollowup(String jc_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		try {
			Str.append("<div class=\"container-fluid portlet box\">");
			Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
			Str.append("<div class=\"caption\" style=\"float: none\"> " + psfmsg + " </div></div>");

			StrSql = " SELECT customer_id, customer_name, contact_id,"
					+ " CONCAT(title_desc,' ', contact_fname, ' ', contact_lname) AS contacts, contact_mobile1, variant_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE 1=1 "
					+ " AND jcpsf_jc_id IN ( " + jc_id + ")"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ " GROUP BY customer_id";
			crs = processQuery(StrSql, 0);
			Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
			while (crs.next()) {
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>Customer: <a href=\"../customer/customer-list.jsp?customer_id=")
						.append(crs.getString("customer_id")).append(" \">")
						.append(crs.getString("customer_name")).append(" (")
						.append(crs.getString("customer_id")).append(")</td>\n");
				Str.append("<td align=center>Contact: <a href=\"../customer/customer-contact-list.jsp?contact_id=")
						.append(crs.getString("contact_id")).append(" \">")
						.append(crs.getString("contacts")).append("</a></td>\n");
				Str.append("</tr>");
				Str.append("<tr align=center>\n");
				Str.append("<td align=center>Mobile: ")
						.append(crs.getString("contact_mobile1"))
						.append(ClickToCall(crs.getString("contact_mobile1"), comp_id))
						.append("</td>\n");
				Str.append("<td align=center>Variant: ").append(crs.getString("variant_name")).append("</td>\n");
				Str.append("</tr>");
			}
			Str.append("</table>\n");
			Str.append("</td>");
			Str.append("</tr>");
			crs.close();

			// //////// Start of PSF Details////////////
			int count = 0;
			int type = 0;
			StrSql = " SELECT jcpsf_id, jcpsf_emp_id, jcpsf_jc_id,jcpsf_followup_time, jcpsf_desc, "
					+ " jcpsf_entry_time, jcpsf_entry_id, jcpsf_modified_id, jcpsf_modified_time,"
					+ " COALESCE(psffeedbacktype_name, '') AS psffeedbacktype_name,"
					+ " COALESCE (CASE WHEN jcpsf_satisfied = 0 THEN '' END,"
					+ " CASE WHEN jcpsf_satisfied = 1 THEN 'Satisfied' END,"
					+ " CASE WHEN jcpsf_satisfied = 2 THEN 'Dis-Satisfied' END,'') AS jcpsf_satisfied,"
					+ " jcpsf_psfdays_id, psfdays_daycount, psfdays_desc, jcpsf_id,"
					+ " crm.emp_id AS crmemp_id,"
					+ " COALESCE(CONCAT(crm.emp_name,' (',crm.emp_ref_no,')'),'') AS crmemp_name,"
					+ " COALESCE(CONCAT(e.emp_name,' (',e.emp_ref_no,')'),'') AS entry_by, "
					+ " COALESCE(CONCAT(m.emp_name,' (',m.emp_ref_no,')'),'') AS modified_by "
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_id = jcpsf_psfdays_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = jcpsf_jc_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp AS crm ON crm.emp_id = jcpsf_emp_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS e ON e.emp_id = jcpsf_entry_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_emp AS m ON m.emp_id = jcpsf_modified_id"
					+ " LEFT JOIN " + "axela_service_psf_feedbacktype ON psffeedbacktype_id = jcpsf_psffeedbacktype_id"
					+ " WHERE 1 = 1 "
					+ " AND jcpsf_jc_id IN ( " + jc_id + ")"
					+ BranchAccess.replace("branch_id", "jc_branch_id")
					+ " GROUP BY jcpsf_id"
					+ " ORDER BY jcpsf_followup_time ";
			// SOP("psf---------------" + StrSql);
			crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\" width=5%>#</th>\n");
				Str.append("<th>Time</th>\n");
				Str.append("<th>Days</th>\n");
				Str.append("<th data-hide=\"phone\">Feedback Type</th>\n");
				Str.append("<th data-hide=\"phone\">Description</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">PSF Executive</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Entry by</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Update</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=top align=center >").append(count).append("</td>");
					Str.append("<td valign=top align=center >").append(
							strToLongDate(crs.getString("jcpsf_followup_time")));
					if (emp_id.equals("1")) {
						Str.append("<br><a href=\"jobcard-dash.jsp?Delete=yes&jcpsf=yes&jc_id=")
								.append(crs.getString("jcpsf_jc_id"))
								.append("&jcpsf_id=")
								.append(crs.getString("jcpsf_id"))
								.append(" \">Delete Follow-up</a>");
					}
					Str.append("</td>");

					Str.append("<td valign=top align=left>")
							.append(crs.getString("psfdays_daycount"))
							.append(crs.getString("psfdays_desc"))
							.append("</td>\n");
					Str.append("<td valign=top align=center >")
							.append(crs.getString("psffeedbacktype_name"));
					Str.append("<br>" + crs.getString("jcpsf_satisfied")).append("</td>");
					Str.append("<td valign=top align=left >")
							.append(crs.getString("jcpsf_desc"))
							.append("</td>");

					Str.append("<td valign=top align=left >");
					Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
							.append(crs.getInt("jcpsf_emp_id")).append(">")
							.append(crs.getString("crmemp_name"))
							.append("</a>");
					Str.append("</td>");

					Str.append("<td valign=top align=left >");
					if (!crs.getString("jcpsf_entry_id").equals("0")
							&& crs.getString("jcpsf_modified_id").equals("0")) {
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getInt("jcpsf_entry_id"))
								.append(">").append(crs.getString("entry_by"))
								.append("</a>");
						Str.append("<br>")
								.append(strToShortDate(crs.getString("jcpsf_entry_time"))).append("");
					}

					if (!crs.getString("jcpsf_modified_id").equals("0")) {
						Str.append("<a href=../portal/executive-summary.jsp?emp_id=")
								.append(crs.getInt("jcpsf_modified_id"))
								.append(">")
								.append(crs.getString("modified_by"))
								.append("</a>");
						Str.append("<br>")
								.append(strToShortDate(crs.getString("jcpsf_modified_time"))).append("");
					}
					Str.append("&nbsp;</td>");
					Str.append("<td valign=top align=left >");
					Str.append("<a href=../service/jobcard-psf-update.jsp?update=yes&jcpsf_id=")
							.append(crs.getInt("jcpsf_id"))
							.append(">Update Follow-up</a>");
					Str.append("<br><a href=../service/jobcard-psf-print.jsp?jcpsf_id=")
							.append(crs.getInt("jcpsf_id"))
							.append("&target=")
							.append(Math.random())
							.append("\" target=_blank>Print Follow-up</a>" + "");
					Str.append("</td>");
					Str.append("</tr>");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
				crs.close();
			} else {
				Str.append("<center><font color=red><b>No PSF Follow-up found!</b></font></center>");
			}
			Str.append("</div>");
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}
	// public String JCPSFDetails(String jc_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// SOP("=============");
	// Str.append("<table width=\"100%\"  border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n");
	// Str.append("<tr>\n<td align=\"center\" valign=\"top\"><font color=\"#ff0000\"><b>").append(psfmsg).append("</b></font></td>\n</tr>\n");
	// Str.append("<tr>\n<td align=\"right\" valign=\"top\">");
	// Str.append("<a href=\"call-update.jsp?add=yes&veh_id=").append(veh_id).append("\">Add New Call...</a></td>\n</tr>\n");
	//
	// StrSql = "SELECT jcpsf_id, jcpsf_jc_id, jcpsf_followup_time, jcpsf_desc,"
	// + " jcpsf_entry_time, jcpsf_entry_id, jcpsf_modified_id,"
	// + " COALESCE(jcpsf_modified_time, '') AS jcpsf_modified_time,"
	// + " COALESCE(jcpsffeedbacktype_name, '') AS jcpsffeedbacktype_name,"
	// +
	// " COALESCE(CONCAT(e.emp_name, ' (', e.emp_ref_no, ')'), '') AS entry_by,"
	// +
	// " COALESCE(CONCAT(m.emp_name, ' (', m.emp_ref_no, ')'), '') AS modified_by,"
	// +
	// " COALESCE(CONCAT(c.emp_name, ' (', c.emp_ref_no, ')'), '') AS emp_name,"
	// + " jcpsfdays_daycount, jcpsfdays_desc, jc_id, jcpsf_emp_id"
	// + " FROM " + compdb(comp_id) + "axela_service_jc_psf"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_service_jc_psfdays ON jcpsfdays_id = jcpsf_jcpsfdays_id"
	// + " INNER JOIN " + compdb(comp_id) +
	// "axela_service_jc ON jc_id = jcpsf_jc_id"
	// +
	// " LEFT JOIN axela_service_psf_feedbacktype ON psffeedbacktype_id = jcpsf_jcpsffeedbacktype_id"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_emp AS e ON e.emp_id = jcpsf_entry_id"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_emp AS m ON m.emp_id = jcpsf_modified_id"
	// + " LEFT JOIN " + compdb(comp_id) +
	// "axela_emp AS c ON c.emp_id = jcpsf_emp_id"
	// + " WHERE jc_active = 1"
	// + " AND jc_id = " + jc_id + ""
	// + " ORDER BY jc_id, jcpsf_followup_time";
	// CachedRowSet crs = processQuery(StrSql, 0);
	//
	// int count = 0;
	// if (crs.isBeforeFirst()) {
	// Str.append("<tr>\n<td height=\"300\" colspan=4 align=\"center\" valign=\"top\">");
	// Str.append("<br><b>Post Sales Follow-up</b><table width=100% border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
	// Str.append("<tr align=\"center\">\n");
	// Str.append("<th>#</th>\n");
	// Str.append("<th>Time</th>\n");
	// Str.append("<th>Job Card ID</th>\n");
	// Str.append("<th>PSF Days</th>\n");
	// Str.append("<th>PSF Feedback Type</th>\n");
	// Str.append("<th>PSF Description</th>\n");
	// Str.append("<th>PSF Executive</th>\n");
	// Str.append("<th>Entry by</th>\n");
	// Str.append("<th>Update</th>\n");
	// Str.append("</tr>\n");
	// while (crs.next()) {
	// count++;
	// Str.append("<tr>\n");
	// Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
	// Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("jcpsf_followup_time")));
	// if (emp_id.equals("1")) {
	// Str.append("<br><a href=\"jobcard-dash.jsp?Delete=yes&jcpsf=yes&jc_id=").append(crs.getString("jc_id")).append("&jcpsf_id=").append(crs.getString("jcpsf_id")).append("\">Delete PSF</a>");
	// }
	// Str.append("</td>\n<td valign=top align=center><a href=\"../service/jobcard-list.jsp?jc_id=").append(crs.getString("jc_id")).append("\">").append(crs.getString("jc_id")).append("</a>");
	// Str.append("</td>\n<td valign=top align=left>").append(crs.getString("jcpsfdays_daycount")).append(crs.getString("jcpsfdays_desc"));
	// Str.append("</td>\n<td valign=top align=left>").append(crs.getString("jcpsffeedbacktype_name"));
	// Str.append("</td>\n<td valign=top align=left>").append(crs.getString("jcpsf_desc"));
	// Str.append("</td>\n<td valign=top align=left>").append("<a href=../portal/executive-summary.jsp?emp_id=").append(crs.getString("jcpsf_emp_id")).append(">");
	// Str.append(crs.getString("emp_name")).append("</a>");
	// Str.append("</td>\n<td valign=top align=left>");
	// if (!crs.getString("jcpsf_entry_id").equals("0") &&
	// crs.getString("jcpsf_modified_id").equals("0")) {
	// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("jcpsf_entry_id")).append("\">").append(crs.getString("entry_by")).append("</a>");
	// Str.append("<br>").append(strToLongDate(crs.getString("jcpsf_entry_time"))).append("");
	// }
	// if (!crs.getString("jcpsf_modified_id").equals("0")) {
	// Str.append("<a href=\"../portal/executive-summary.jsp?emp_id=").append(crs.getInt("jcpsf_modified_id")).append("\">").append(crs.getString("modified_by")).append("</a>");
	// Str.append("<br>").append(strToLongDate(crs.getString("jcpsf_modified_time"))).append("");
	// }
	// Str.append("&nbsp;</td>\n<td valign=top align=left>");
	// Str.append("<a href=\"../service/jobcard-psf-update.jsp?update=yes&jc_id=").append(crs.getInt("jc_id")).append("&jcpsf_id=").append(crs.getInt("jcpsf_id")).append("\">Update PSF</a>");
	// Str.append("</td>\n</tr>\n");
	// }
	// Str.append("</table>\n");
	// Str.append("</td>\n</tr>\n");
	// crs.close();
	// } else {
	// Str.append("<tr>\n<td height=\"300\" colspan=\"4\" align=\"center\">");
	// Str.append("<font color=\"red\"><b>No PSF found!</b></font>");
	// Str.append("</td>\n</tr>\n");
	// }
	// ////main table end
	// Str.append("</td>\n</tr>\n");
	// Str.append("</table>\n");
	// } catch (Exception ex) {
	// SOPError("Axelaauto== " + this.getClass().getName());
	// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
	// + ": " + ex);
	// return "";
	// }
	// return Str.toString();
	// }

	protected void DeletePSFFields(String jcpsf_id, String jcpsf) {
		// SOP("before delete");
		if (jcpsf.equals("yes")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " WHERE jcpsf_id =" + jcpsf_id + "";
			// SOP("StrSql===="+StrSql);
			updateQuery(StrSql);
		}
	}

	public String ListJCHistory() {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT " + compdb(comp_id) + "axela_service_jc_history.*, emp_id, jc_title,"
				+ " CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_history"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_id = history_jc_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = history_emp_id"
				+ " WHERE history_jc_id = " + jc_id + ""
				+ " ORDER BY history_id DESC";
		// SOP("StrSql======"+StrSql);
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {

				Str.append("<div class=\"table-responsive table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("");
				Str.append("<th data-toggle=\"true\">Date</th>\n");
				Str.append("<th data-hide=\"phone\">Action By</th>\n");
				Str.append("<th data-hide=\"phone\">Type of Action</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Old Value</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">New Value</th>\n");
				Str.append("</tr></thead>\n");

				while (crs.next()) {
					jc_title = crs.getString("jc_title");
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(strToLongDate(crs.getString("history_datetime"))).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\"><a href=\"../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getString("emp_id")).append("\">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("history_actiontype")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("history_oldvalue")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("history_newvalue")).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
				Str.append("</div>\n");
			} else {
				Str.append("<br><br><br><center><font color=\"red\"><b>No History(s) found!</b></font></center>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateUser() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">All Users</option>\n");
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_bay_trans ON baytrans_emp_id = emp_id"
					+ " WHERE baytrans_jc_id = " + jc_id + ""
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), jc_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service = 1"
					+ " AND (emp_branch_id = " + jc_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + jc_branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), jc_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTechnician() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_technician = 1"
					+ " AND (emp_branch_id = " + jc_branch_id + ""
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = " + jc_branch_id + "))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), jc_technician_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulatePriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT priorityjc_id, priorityjc_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_priority"
					+ " ORDER BY priorityjc_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("priorityjc_id"));
				Str.append(StrSelectdrop(crs.getString("priorityjc_id"), jc_priorityjc_id));
				Str.append(">").append(crs.getString("priorityjc_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " ORDER BY jctype_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id"));
				Str.append(StrSelectdrop(crs.getString("jctype_id"), jc_type_id));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateCategory() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jccat_id, jccat_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
					+ " ORDER BY jccat_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<option value=\"0\">Select</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jccat_id"));
				Str.append(StrSelectdrop(crs.getString("jccat_id"), jc_cat_id));
				Str.append(">").append(crs.getString("jccat_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateStage() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jcstage_id, jcstage_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
					+ " ORDER BY jcstage_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jcstage_id"));
				Str.append(StrSelectdrop(crs.getString("jcstage_id"), jc_stage_id));
				Str.append(">").append(crs.getString("jcstage_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBay() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT bay_name, bay_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
					+ " WHERE bay_branch_id = " + jc_branch_id + ""
					+ " AND bay_active = 1"
					+ " AND bay_open = 1"
					+ " ORDER BY bay_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select Bay</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("bay_id"));
				Str.append(Selectdrop(crs.getInt("bay_id"), jc_bay_id));
				Str.append(">").append(crs.getString("bay_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateInventoryLocation() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_name, location_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + jc_branch_id + ""
					+ " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select Location</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(Selectdrop(crs.getInt("location_id"), jc_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTitle(String contact_title_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT title_id, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER by title_desc";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("title_id"));
				Str.append(Selectdrop(crs.getInt("title_id"), contact_title_id));
				Str.append(">").append(crs.getString("title_desc")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateContactType() {
		StringBuilder Str = new StringBuilder();
		Str.append("<option value=\"0\">Select</option>\n");
		try {
			StrSql = "SELECT contacttype_id, contacttype_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact_type"
					+ " GROUP BY contacttype_id"
					+ " ORDER BY contacttype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("contacttype_id"));
				Str.append(StrSelectdrop(crs.getString("contacttype_id"), new_contact_contacttype_id));
				Str.append(">").append(crs.getString("contacttype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error In " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateCity() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT city_id, city_name"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " ORDER BY city_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("city_id"));
				Str.append(Selectdrop(crs.getInt("city_id"), contact_city_id));
				Str.append(">").append(crs.getString("city_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String PopulateBillType(String jctrans_billtype_id) {

		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT billtype_id, billtype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_bill_type"
					+ " ORDER BY billtype_name";
			// SOP("PopulateBillType==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("billtype_id"));
				Str.append(Selectdrop(crs.getInt("billtype_id"), jctrans_billtype_id));
				Str.append(">").append(crs.getString("billtype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}

	}

}
