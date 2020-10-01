package axela.service;
//aJIt
//satish 01-Apr-2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_QuickAdd extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String add = "";
	public String addB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "", err_msg = "";
	public String jc_id = "0";
	public String jc_no = "";
	public String jcdate = "", jc_time_in = "";
	public String jc_time_promised = "";
	public String jc_promisetime = "";
	public String jc_time_ready = "";
	public String jc_readytime = "";
	public String jc_time_out = "";
	public String jc_deliveredtime = "";
	public String jc_customer_id = "0";
	public String jc_contact_id = "0";
	public String jc_type_id = "0";
	public String veh_variant_id = "0";
	public String jc_cat_id = "0";
	public String jc_technician_emp_id = "0";
	public String jc_emp_id = "0";
	public String jc_ro_no = "";
	public String jc_labour_cost = "";
	public String jc_part_cost = "";
	public String jc_notes = "";
	public String customer_name = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_email1 = "", contact_mobile1 = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String jc_name = "";
	public String veh_id = "0";
	public String jc_veh_id = "0";
	public String link_veh_name = "";
	public String jc_bill_address = "";
	public String jc_bill_city = "";
	public String jc_bill_state = "";
	public String jc_bill_pin = "";
	public String jc_del_address = "";
	public String jc_del_state = "";
	public String jc_del_city = "";
	public String jc_del_pin = "";
	public String veh_iacs = "0";
	public String veh_chassis_no = "";
	public String veh_engine_no = "", veh_reg_no = "";
	public String jc_cust_voice = "";
	public String jc_advice = "";
	public String jc_terms = "";
	public String jc_po = "", jc_model_id = "0";
	public String config_service_jc_refno = "0";
	public String QueryString = "";
	public String jc_instructions = "";
	public String jc_kms = "";
	public String veh_service_duekms = "0", vehkms_id = "0";
	public String veh_service_duedate = "0";
	public String contact_id = "0";
	public String config_refno_enable = "0";
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				msg = PadQuotes(request.getParameter("msg"));
				jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
				jc_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
				jc_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
				QueryString = PadQuotes(request.getQueryString());
				veh_id = CNumeric(PadQuotes(request.getParameter("span_veh_id")));
				jc_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));

				if (add.equals("yes")) {
					StrSql = "SELECT jc_id FROM " + compdb(comp_id) + "axela_service_jc"
							+ " WHERE jc_veh_id = " + jc_veh_id + ""
							+ " AND jc_time_out = ''";
					if (!ExecuteQuery(StrSql).equals("")) {
						err_msg = "<br>Previous Job Card is not yet Ready!";
						response.sendRedirect("../portal/error.jsp?msg=" + err_msg);
					}
				}

				if (err_msg.equals("")) {
					if (!jc_veh_id.equals("0") && add.equals("yes") && veh_id.equals("0")) {
						PopulateVehicleDetails(response);
					}

					if (!veh_id.equals("0")) {
						PopulateVehDetails();
					}

					if ("yes".equals(add)) {
						status = "Quick Add";
						if (addB.equals("yes")) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_service_jobcard_add", request).equals("1")) {
								AddFields(request);
								if (!msg.equals("")) {
									msg = "Error! " + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + jc_id + "&msg=Job Card added successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		jc_type_id = CNumeric(PadQuotes(request.getParameter("dr_jc_type")));
		jc_no = PadQuotes(request.getParameter("jc_no"));
		jc_time_in = PadQuotes(request.getParameter("txt_jc_time_in"));
		jc_time_promised = PadQuotes(request.getParameter("txt_jc_time_promised"));
		jc_time_out = PadQuotes(request.getParameter("txt_jc_deltime"));
		jc_time_ready = PadQuotes(request.getParameter("txt_jc_comptime"));

		veh_chassis_no = PadQuotes(request.getParameter("txt_veh_chassis_no"));
		veh_reg_no = PadQuotes(request.getParameter("txt_veh_reg_no"));
		veh_engine_no = PadQuotes(request.getParameter("txt_veh_engine_no"));
		veh_variant_id = CNumeric(PadQuotes(request.getParameter("txt_variant_id")));

		jc_model_id = CNumeric(PadQuotes(request.getParameter("txt_model_id")));
		jc_part_cost = CNumeric(PadQuotes(request.getParameter("txt_jc_part_cost")));
		jc_labour_cost = CNumeric(PadQuotes(request.getParameter("txt_jc_labour_cost")));
		jc_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
		jc_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
		veh_id = CNumeric(PadQuotes(request.getParameter("span_veh_id")));
		jc_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
		if (!veh_id.equals("0") && !jc_veh_id.equals(veh_id)) {
			jc_veh_id = veh_id;
		}
		PopulateContactDetails();
		jc_cat_id = CNumeric(PadQuotes(request.getParameter("dr_jc_cat")));
		jc_kms = CNumeric(PadQuotes(request.getParameter("txt_jc_kms")));
		jc_ro_no = PadQuotes(request.getParameter("txt_jc_ro_no"));
		jc_emp_id = CNumeric(PadQuotes(request.getParameter("dr_jc_emp_id")));
		jc_technician_emp_id = CNumeric(PadQuotes(request.getParameter("dr_jc_technician_emp_id")));
		jc_bill_address = PadQuotes(request.getParameter("txt_bill_address"));
		jc_bill_city = PadQuotes(request.getParameter("txt_bill_city"));
		jc_bill_pin = PadQuotes(request.getParameter("txt_bill_pin"));
		jc_bill_state = PadQuotes(request.getParameter("txt_bill_state"));
		jc_del_address = PadQuotes(request.getParameter("txt_del_address"));
		jc_del_city = PadQuotes(request.getParameter("txt_del_city"));
		jc_del_pin = PadQuotes(request.getParameter("txt_del_pin"));
		jc_del_state = PadQuotes(request.getParameter("txt_del_state"));
		jc_notes = PadQuotes(request.getParameter("txt_jc_notes"));
		jc_cust_voice = PadQuotes(request.getParameter("jc_cust_voice"));
		jc_advice = PadQuotes(request.getParameter("txt_jc_advice"));
		jc_instructions = PadQuotes(request.getParameter("txt_jc_instructions"));
		jc_terms = PadQuotes(request.getParameter("txt_jc_terms"));
	}

	protected void CheckForm() {
		msg = "";
		StrSql = "SELECT jc_id FROM " + compdb(comp_id) + "axela_service_jc"
				+ " WHERE jc_veh_id = " + jc_veh_id + ""
				+ " AND jc_time_out = ''";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg += "<br>Previous Job Card is not yet completed!";
		} else {
			if (jc_time_in.equals("")) {
				msg += "<br>Enter Job Card Time In!";
			} else {
				if (!isValidDateFormatLong(jc_time_in)) {
					msg += "<br>Enter valid Job Card Time In!";
				} else {
					jcdate = ConvertLongDateToStr(jc_time_in);
					if (Long.parseLong(ConvertLongDateToStr(jc_time_in)) > Long.parseLong(ToLongDate(kknow()))) {
						msg += "<br>Job Card Time In must be less than or equal to Current Time!";
					}
				}
			}

			if (!jc_time_promised.equals("")) {
				if (!isValidDateFormatLong(jc_time_promised)) {
					msg += "<br>Enter valid Promised Time!";
				} else {
					jc_promisetime = ConvertLongDateToStr(jc_time_promised);
				}
			}

			if (!jc_time_ready.equals("")) {
				if (!isValidDateFormatLong(jc_time_ready)) {
					msg += "<br>Enter valid Ready Time!";
				} else {
					jc_readytime = ConvertLongDateToStr(jc_time_ready);
				}
			}

			if (!jc_time_out.equals("")) {
				if (!isValidDateFormatLong(jc_time_out)) {
					msg += "<br>Enter valid Delivered Time!";
				} else {
					jc_deliveredtime = ConvertLongDateToStr(jc_time_out);
				}
			}

			if (!jc_time_in.equals("") && !jc_time_promised.equals("")) {
				if (isValidDateFormatLong(jc_time_promised) && isValidDateFormatLong(jc_time_in)) {
					if (Long.parseLong(ConvertLongDateToStr(jc_time_promised)) < Long.parseLong(ConvertLongDateToStr(jc_time_in))) {
						msg += "<br>Promised Time must be greater than Job Card Time In!";
					}
				}
			}

			if (!jc_time_in.equals("") && !jc_time_out.equals("")) {
				if (isValidDateFormatLong(jc_time_out) && isValidDateFormatLong(jc_time_in)) {
					if (Long.parseLong(ConvertLongDateToStr(jc_time_out)) < Long.parseLong(ConvertLongDateToStr(jc_time_in))) {
						msg += "<br>Delivered Time must be greater than Job Card Time In!";
					}
				}
			}

			if (!jc_time_ready.equals("") && !jc_time_out.equals("")) {
				if (isValidDateFormatLong(jc_time_out) && isValidDateFormatLong(jc_time_ready)) {
					if (Long.parseLong(ConvertLongDateToStr(jc_time_out)) < Long.parseLong(ConvertLongDateToStr(jc_time_ready))) {
						msg += "<br>Delivered Time must be greater than Ready Time!";
					}
				}
			}

			if (jc_time_promised.equals("")) {
				jc_time_promised = jc_time_in;
				jc_promisetime = ConvertLongDateToStr(jc_time_promised);
			}

			if (jc_time_ready.equals("")) {
				jc_time_ready = jc_time_promised;
				jc_readytime = ConvertLongDateToStr(jc_time_ready);
			}

			if (jc_time_promised.equals("") && jc_time_out.equals("")) {
				jc_time_out = jc_time_in;
				jc_deliveredtime = ConvertLongDateToStr(jc_time_out);
			} else if (!jc_time_promised.equals("") && jc_time_out.equals("")) {
				jc_time_out = jc_time_promised;
				jc_deliveredtime = ConvertLongDateToStr(jc_time_out);
			}

			if (Long.parseLong(ConvertLongDateToStr(jc_time_out)) < Long.parseLong(ConvertLongDateToStr(jc_time_ready))) {
				jc_time_out = jc_time_ready;
				jc_deliveredtime = ConvertLongDateToStr(jc_time_out);
			}

			if (jc_type_id.equals("0")) {
				msg += "<br>Select Type!";
			}

			if (jc_cat_id.equals("0")) {
				msg += "<br>Select Category!";
			}

			if (CNumeric(jc_kms).equals("0")) {
				msg += "<br>Enter Kms!";
			}

			if (jc_bill_address.equals("")) {
				msg += "<br>Enter Billing Address!";
			}

			if (jc_bill_city.equals("")) {
				msg += "<br>Enter Billing City!";
			}

			if (jc_bill_pin.equals("")) {
				msg += "<br>Enter Billing Pin!";
			}

			if (jc_bill_state.equals("")) {
				msg += "<br>Enter Billing State!";
			}

			if (jc_del_address.equals("")) {
				msg += "<br>Enter Delivery Address!";
			}

			if (jc_del_city.equals("")) {
				msg += "<br>Enter Delivery City!";
			}

			if (jc_del_pin.equals("")) {
				msg += "<br>Enter Delivery Pin!";
			}

			if (jc_del_state.equals("")) {
				msg += "<br>Enter Delivery State!";
			}

			if (jc_cust_voice.equals("")) {
				msg += "<br>Enter Customer Voice!";
			}

			if (!jc_ro_no.equals("")) {
				if (jc_ro_no.length() < 2) {
					msg += "<br>RO. No. should be atleast Two Characters!";
				} else {
					StrSql = "SELECT jc_ro_no FROM " + compdb(comp_id) + "axela_service_jc"
							+ " WHERE jc_branch_id = 2"
							+ " AND jc_ro_no = '" + jc_ro_no + "'";
					if (!ExecuteQuery(StrSql).equals("") && add.equals("yes")) {
						msg += "<br>Similar RO No. found!";
					}
				}
			}

			if (!jc_part_cost.equals("0") && !isFloat(jc_part_cost)) {
				msg += "<br>Enter valid Parts Cost!";
			}

			if (!jc_labour_cost.equals("0") && !isFloat(jc_labour_cost)) {
				msg += "<br>Enter valid Labour Cost!";
			}

			if (jc_emp_id.equals("0")) {
				msg += "<br>Select Service Advisor!";
			}
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc"
						+ " (jc_branch_id,"
						+ " jc_no,"
						+ " jc_time_in,"
						+ " jc_customer_id,"
						+ " jc_contact_id,"
						+ " jc_comm_contact_id,"
						+ " jc_veh_id,"
						+ " jc_reg_no,"
						+ " jc_jctype_id,"
						+ " jc_jccat_id,"
						+ " jc_kms,"
						+ " jc_netamt,"
						+ " jc_grandtotal,"
						+ " jc_bill_address,"
						+ " jc_bill_city,"
						+ " jc_bill_pin,"
						+ " jc_bill_state,"
						+ " jc_del_address,"
						+ " jc_del_city,"
						+ " jc_del_pin,"
						+ " jc_del_state,"
						+ " jc_title,"
						+ " jc_cust_voice,"
						+ " jc_advice,"
						+ " jc_terms,"
						+ " jc_instructions,"
						+ " jc_emp_id,"
						+ " jc_technician_emp_id,"
						+ " jc_location_id,"
						+ " jc_ro_no,"
						+ " jc_time_promised,"
						+ " jc_time_ready,"
						+ " jc_time_out,"
						+ " jc_jcstage_id,"
						+ " jc_priorityjc_id,"
						+ " jc_inventory,"
						+ " jc_active,"
						+ " jc_notes,"
						+ " jc_entry_id,"
						+ " jc_entry_date)"
						+ " VALUES"
						+ " (2,"
						+ " (SELECT COALESCE(MAX(jc.jc_no), 0) + 1 FROM " + compdb(comp_id) + "axela_service_jc AS jc"
						+ " WHERE jc.jc_branch_id  = 2),"
						+ " '" + jcdate + "',"
						+ " " + jc_customer_id + ","
						+ " " + jc_contact_id + ","
						+ " " + jc_contact_id + ","
						+ " " + jc_veh_id + ","
						+ " '" + veh_reg_no + "',"
						+ " " + jc_type_id + ","
						+ " " + jc_cat_id + ","
						+ " '" + jc_kms + "',"
						+ " '" + (Double.parseDouble(CNumeric(jc_part_cost)) + Double.parseDouble(CNumeric(jc_labour_cost))) + "',"
						+ " '" + (Double.parseDouble(CNumeric(jc_part_cost)) + Double.parseDouble(CNumeric(jc_labour_cost))) + "',"
						+ " '" + jc_bill_address + "',"
						+ " '" + jc_bill_city + "',"
						+ " '" + jc_bill_pin + "',"
						+ " '" + jc_bill_state + "',"
						+ " '" + jc_del_address + "',"
						+ " '" + jc_del_city + "',"
						+ " '" + jc_del_pin + "',"
						+ " '" + jc_del_state + "',"
						+ " 'Job Card',"
						+ " '" + jc_cust_voice + "',"
						+ " '" + jc_advice + "',"
						+ " '" + jc_terms + "',"
						+ " '" + jc_instructions + "',"
						+ " " + jc_emp_id + ","
						+ " " + jc_technician_emp_id + ","
						+ " 1,"
						+ " '" + jc_ro_no + "',"
						+ " '" + jc_promisetime + "',"
						+ " '" + jc_readytime + "',"
						+ " '" + jc_deliveredtime + "',"
						+ " 6,"
						+ " 1,"
						+ " '',"
						+ " 1,"
						+ " '" + jc_notes + "',"
						+ " " + emp_id + ","
						+ " '" + ToLongDate(kknow()) + "')";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					jc_id = rs.getString(1);
				}
				rs.close();

				if (Double.parseDouble(CNumeric(jc_labour_cost)) > 0) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_trans"
							+ " (jctrans_jc_id,"
							+ " jctrans_rowcount,"
							+ " jctrans_item_id,"
							+ " jctrans_price,"
							+ " jctrans_qty,"
							+ " jctrans_netprice)"
							+ " VALUES"
							+ " (" + jc_id + ","
							+ " (SELECT COALESCE(MAX(jctrans_rowcount), 0) + 1 FROM " + compdb(comp_id) + "axela_service_jc_trans AS rowcount),"
							+ " 738,"
							+ " " + jc_labour_cost + ","
							+ " 1,"
							+ " " + jc_labour_cost + ")";
					stmttx.execute(StrSql);
				}

				if (Double.parseDouble(CNumeric(jc_part_cost)) > 0) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_item"
							+ " (jcitem_jc_id,"
							+ " jcitem_rowcount,"
							+ " jcitem_item_id,"
							+ " jcitem_price,"
							+ " jcitem_qty,"
							+ " jcitem_total)"
							+ " VALUES"
							+ " (" + jc_id + ","
							+ " (SELECT COALESCE(MAX(jcitem_rowcount), 0) + 1 FROM " + compdb(comp_id) + "axela_service_jc_item AS rowcount),"
							+ " 737,"
							+ " " + jc_part_cost + ","
							+ " 1,"
							+ " " + jc_part_cost + ")";
					stmttx.execute(StrSql);
				}

				// StrSql = "SELECT emp_id FROM " + compdb(comp_id) +
				// "axela_emp"
				// + " WHERE emp_active = 1";
				// if (veh_iacs.equals("1")) {
				// StrSql += " AND emp_service_psf_iacs = 1";
				// } else {
				// StrSql += " AND emp_service_psf = 1";
				// }
				// StrSql += " ORDER BY RAND()"
				// + " LIMIT 1";
				//
				// String jc_psf_emp_id = CNumeric(ExecuteQuery(StrSql));
				// if (jc_psf_emp_id.equals("0")) {
				// jc_psf_emp_id = jc_emp_id;
				// }
				//
				// //Add PSF days
				// if (!jc_psf_emp_id.equals("0") && Long.parseLong(jcdate) >=
				// Long.parseLong("20140507000000")) {
				// AddPSFDaysFields(jc_id, jc_emp_id, jc_psf_emp_id,
				// jc_deliveredtime, emp_id, "5");
				// }

				// if (Double.parseDouble(CNumeric(jc_part_cost)) > 0 || Double.parseDouble(CNumeric(jc_labour_cost)) > 0) {
				// String invoice_id = "0";
				// Double invoice_total = (Double.parseDouble(CNumeric(jc_part_cost)) + Double.parseDouble(CNumeric(jc_labour_cost)));
				//
				// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_invoice"
				// + " (invoice_branch_id,"
				// + " invoice_no,"
				// + " invoice_date,"
				// + " invoice_customer_id,"
				// + " invoice_contact_id,"
				// + " invoice_location_id,"
				// + " invoice_jc_id,"
				// + " invoice_netamt,"
				// + " invoice_grandtotal,"
				// + " invoice_emp_id,"
				// + " invoice_payment_date,"
				// + " invoice_active,"
				// + " invoice_desc,"
				// + " invoice_terms,"
				// + " invoice_notes,"
				// + " invoice_entry_id,"
				// + " invoice_entry_date)"
				// + " VALUES"
				// + " (2,"
				// + " (SELECT COALESCE(MAX(invoice.invoice_no), 0) + 1"
				// + " FROM " + compdb(comp_id) + "axela_invoice AS invoice WHERE invoice.invoice_branch_id  = 2),"
				// + " '" + jcdate + "',"
				// + " " + jc_customer_id + ","
				// + " " + jc_contact_id + ","
				// + " 1,"
				// + " '" + CNumeric(jc_id) + "',"
				// + " '" + invoice_total + "',"
				// + " '" + invoice_total + "',"
				// + " '" + jc_emp_id + "',"
				// + " '" + jcdate + "',"
				// + " 1,"
				// + " '',"
				// + " '',"
				// + " '" + jc_notes + "',"
				// + " " + emp_id + ","
				// + " '" + ToLongDate(kknow()) + "')";
				// stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				// ResultSet rs1 = stmttx.getGeneratedKeys();
				//
				// while (rs1.next()) {
				// invoice_id = rs1.getString(1);
				// }
				// rs1.close();
				// if (!invoice_id.equals("0")) {
				// StrSql = "SELECT"
				// + " " + invoice_id + ","
				// + " jcitem_rowcount,"
				// + " jcitem_item_id,"
				// + " jcitem_price,"
				// + " jcitem_qty,"
				// + " jcitem_total"
				// + " FROM " + compdb(comp_id) + "axela_service_jc_item"
				// + " WHERE jcitem_jc_id = " + CNumeric(jc_id) + "";
				//
				// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_invoice_item"
				// + " (invoiceitem_invoice_id,"
				// + " invoiceitem_rowcount,"
				// + " invoiceitem_item_id,"
				// + " invoiceitem_price,"
				// + " invoiceitem_qty,"
				// + " invoiceitem_total)"
				// + " " + StrSql + "";
				// stmttx.execute(StrSql);
				// }
				// }

				// For inserting the recent kms of the vehicle into veh_kms
				// table
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
						+ " (vehkms_id,"
						+ " vehkms_veh_id,"
						+ " vehkms_kms,"
						+ " vehkms_entry_id,"
						+ " vehkms_entry_date)"
						+ " VALUES"
						+ " (" + vehkms_id + ","
						+ " '" + jc_veh_id + "',"
						+ " '" + jc_kms + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "')";
				stmttx.execute(StrSql);

				// for updating the recent kms of the vehicle into veh table
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET"
						+ " veh_kms = " + jc_kms + ","
						+ " veh_emp_id = " + jc_emp_id + ""
						+ " WHERE veh_id = " + jc_veh_id + "";
				stmttx.execute(StrSql);

				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public void AddPSFDaysFields(String jc_id, String jc_emp_id, String jc_psf_emp_id, String jc_timeout, String emp_id, String branch_id) throws SQLException {
		try {
			StrSql = "SELECT " + jc_id + ","
					// + " IF(jcpsfdays_exe_type = 1, " + jc_emp_id + ", " +
					// jc_psf_emp_id + "),"
					+ " psfdays_emp_id,"
					+ " DATE_FORMAT(DATE_ADD(CONCAT(SUBSTR('" + jc_timeout + "', 1, 8), '100000'), INTERVAL (jcpsfdays_daycount-0) DAY), '%Y%m%d%H%i%s'),"
					+ " jcpsfdays_id,"
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "'"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id"
					+ " WHERE branch_id = " + branch_id + "";

			StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_psf"
					+ " (jcpsf_jc_id,"
					+ " jcpsf_emp_id,"
					+ " jcpsf_followup_time,"
					+ " jcpsf_jcpsfdays_id,"
					+ " jcpsf_entry_id,"
					+ " jcpsf_entry_time)"
					+ StrSql;
			stmttx.execute(StrSql);
		} catch (Exception ex) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateVehDetails() {
		try {
			if (!veh_id.equals("0") || !jc_veh_id.equals("0")) {
				StrSql = "SELECT veh_id, IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name) AS variant_name"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
						+ " WHERE 1 = 1";
				if (!veh_id.equals("0")) {
					StrSql += " AND veh_id = " + veh_id + "";
				} else if (!jc_veh_id.equals("0")) {
					StrSql += " AND veh_id = " + jc_veh_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					link_veh_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + "\">" + crs.getString("variant_name") + "</a>";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateJCType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jctype_id, jctype_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
					+ " ORDER BY jctype_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("jctype_id"));
				Str.append(StrSelectdrop(crs.getString("jctype_id"), jc_type_id));
				Str.append(">").append(crs.getString("jctype_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateExecutive() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service = 1"
					+ " AND (emp_branch_id = 2"
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = 2))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Service Advisor</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), jc_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateTechnician() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_technician = 1"
					+ " AND (emp_branch_id = 2"
					+ " OR emp_id = 1"
					+ " OR emp_id IN (SELECT empbr.emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp_branch empbr"
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id = empbr.emp_id"
					+ " AND empbr.emp_branch_id = 2))"
					+ " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select Technician</option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(Selectdrop(crs.getInt("emp_id"), jc_technician_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateJobCardType() {
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
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateJobCardCategory() {
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

	public void PopulateVehicleDetails(HttpServletResponse response) {
		try {
			StrSql = "SELECT veh_reg_no, veh_chassis_no, veh_engine_no, customer_id, veh_iacs,"
					+ " contact_id, customer_name, contact_fname, veh_variant_id, contact_lname, title_desc,"
					+ " customer_address, city_name, customer_pin, variant_preownedmodel_id, state_name, veh_id,"
					+ " IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name) AS variant_name"
					+ " FROM " + compdb(comp_id) + "axela_service_veh"
					+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE veh_id = " + CNumeric(jc_veh_id) + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					veh_iacs = crs.getString("veh_iacs");
					veh_chassis_no = crs.getString("veh_chassis_no");
					veh_engine_no = crs.getString("veh_engine_no");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					jc_customer_id = crs.getString("customer_id");
					veh_variant_id = crs.getString("veh_variant_id");
					jc_model_id = crs.getString("variant_preownedmodel_id");
					jc_contact_id = crs.getString("contact_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					link_veh_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + "\">" + crs.getString("variant_name") + "</a>";
					jc_bill_address = crs.getString("customer_address");
					jc_bill_city = crs.getString("city_name");
					jc_bill_state = crs.getString("state_name");
					jc_bill_pin = crs.getString("customer_pin");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateContactDetails() {
		try {
			StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname,"
					+ " contact_lname, contact_email1, contact_mobile1, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " WHERE contact_id = " + jc_contact_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				contact_email1 = crs.getString("contact_email1");
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_fname = crs.getString("contact_fname");
				contact_lname = crs.getString("contact_lname");
				link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + "</a>";
				link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
						+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
