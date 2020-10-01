package axela.service;
//aJIt
//satish 01-Apr-2013

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class JobCard_Update extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	// public String branch_id = "0";
	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "", err_msg = "";
	public String jc_id = "0";
	public String jc_branch_id = "0";
	public String brand_id = "0";
	public String jc_no = "";
	public String jc_time_in = "";
	public String jcdate = "";
	public String jc_time_promised = "";
	public String jc_promisetime = "";
	public String jc_time_ready = "";
	public String jc_readytime = "";
	public String jc_time_out = "";
	public String jc_time_posted = "";
	public String jc_deliveredtime = "";
	public String jc_stage_id = "0";
	public String jc_customer_id = "0";
	public String jc_ref_customer_id = "0";
	public String jc_contact_id = "0";
	public String jc_type_id = "0";
	public String jc_variant_id = "0";
	public String jc_warranty = "0";
	public String jc_cat_id = "0";
	public String jc_netamt = "0";
	public String jc_discamt = "0";
	public String jc_totaltax = "0";
	public String jc_emp_id = "0";
	public String jc_technician_emp_id = "0";
	public String jc_location_id = "0";
	public String jc_ro_no = "";
	public String jc_active = "0";
	public String jc_critical = "0";
	public String jc_notes = "";
	public String jc_entry_id = "0";
	public String jc_entry_date = "";
	public String jc_modified_id = "0";
	public String jc_modified_date = "";
	public String entry_by = "";
	public String modified_by = "";
	public String BranchAccess = "";
	public String customer_name = "";
	public String contact_fname = "", contact_lname = "";
	public String contact_email1 = "", contact_email2 = "", contact_mobile1 = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public String link_refcustomer_name = "";
	public String jc_name = "";
	public String veh_id = "0";
	public String jc_veh_id = "0";
	public String jc_vehmove_id = "0";
	public String jc_reg_no = "";
	public String link_veh_name = "";
	public String jc_bill_address = "";
	public String jc_bill_city = "";
	public String jc_bill_state = "";
	public String jc_bill_pin = "";
	public String jc_del_address = "";
	public String jc_del_state = "";
	public String jc_del_city = "";
	public String jc_del_pin = "";
	public String jc_amt = "0";
	public String veh_chassis_no = "", veh_iacs = "0";
	public String veh_engine_no = "", veh_reg_no = "";
	public String jc_grandtotal = "0";
	public String jc_cust_voice = "";
	public String jc_advice = "";
	public String jc_terms = "";
	public String jc_inventory = "";
	public String jc_po = "", modelid = "0";
	public String jc_priorityjc_id = "1";
	public String config_service_jc_refno = "";
	public String QueryString = "";
	public String jc_instructions = "";
	public String jc_title = "";
	public String jc_kms = "", jc_fuel_guage = "0";
	public String veh_service_duekms = "0", vehkms_id = "0";
	public String veh_service_duedate = "0";
	public String contact_id = "0", invent_count = "0";
	public String inventtrans_jc_id = "0", inventtrans_invent_id = "0";
	public String fpath = "";
	public String jcpsf_entry_time = "";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String brandconfig_jc_new_email_enable = "0";
	public String brandconfig_jc_new_email_sub = "";
	public String brandconfig_jc_new_email_format = "";
	public String brandconfig_jc_new_sms_enable = "0";
	public String brandconfig_jc_new_sms_format = "";
	public String branch_jc_new_email_exe_sub = "";
	public String branch_jc_new_email_exe_format = "";
	public String brandconfig_principal_id = "0", principalcontactid = "0";
	public String config_admin_email = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String emailmsg = "", emailsub = "";
	public String smsmsg = "";
	public String recent_jc_emp_id = "0";
	public String jcwarranty = "";
	public BufferedImage bufferedImage;
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
				BranchAccess = GetSession("BranchAccess", request);
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				jc_branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
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

					// SOP("strsql===" + StrSql);

					if (!ExecuteQuery(StrSql).equals("")) {
						err_msg = "<br>Previous Job Card is not yet Ready!";
						response.sendRedirect("../portal/error.jsp?msg=" + err_msg);
					}
				}

				if (err_msg.equals("")) {
					File f = new File(JobCardFuelGuagePath(comp_id));
					if (!f.exists()) {
						f.mkdirs();
					}
					fpath = JobCardFuelGuagePath(comp_id) + "jcfg_" + jc_id + ".jpg";

					if (!jc_veh_id.equals("") && add.equals("yes") && veh_id.equals("0")) {
						PopulateVehicleDetails(response);
					}

					if (!veh_id.equals("0")) {
						PopulateVehDetails();
					}

					if (add.equals("yes")) {
						status = "Add";
						if (!addB.equals("yes")) {
							jc_location_id = "1";
							jc_active = "1";
							jc_time_in = strToLongDate(ToLongDate(kknow()));
							jc_promisetime = strToLongDate(ToLongDate(kknow()));
							jc_time_posted = strToLongDate(ToLongDate(kknow()));
						} else {
							GetValues(request, response);
							PopulateConfigDetails();
							if (ReturnPerm(comp_id, "emp_service_jobcard_add", request).equals("1")) {
								jc_entry_id = emp_id;
								jc_entry_date = ToLongDate(kknow());
								AddFields(request, "");
								if (!jc_id.equals("0")) {
									fpath = JobCardFuelGuagePath(comp_id) + "jcfg_" + jc_id + ".jpg";
									// ImageIO.write(CreateFuelGuageImg(jc_id, jc_fuel_guage), "jpg", new File(fpath));
								}

								if (!msg.equals("")) {
									msg = "Error! " + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + jc_id + "&msg=Job Card added successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						}
					} else if (update.equals("yes")) {
						status = "Update";
						if (!updateB.equals("yes") && !deleteB.equals("Delete Job Card")) {
							contact_id = jc_contact_id;
							PopulateFields(request, response);
						} else if (updateB.equals("yes") && !deleteB.equals("Delete Job Card")) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_service_jobcard_edit", request).equals("1")) {
								CheckForm();
								UpdateFields(request, response);
								if (!jc_deliveredtime.equals("")) {
									// SOP("111111");
									if (brand_id.equals("2")) {
										AddPSFFields(jc_id, comp_id, jc_cat_id, "0");
									}
									if (brand_id.equals("7")) {
										AddPSFFields(jc_id, comp_id, "0", jc_type_id);
									}
									CalculateVehicleService();
								}
								if (!jc_id.equals("0")) {
									// SOP("222222");
									// ImageIO.write(CreateFuelGuageImg(jc_id, jc_fuel_guage), "jpg", new File(fpath));
								}

								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?jc_id=" + jc_id + "&msg=Job Card updated successfully!"));
								}
							} else {
								response.sendRedirect(AccessDenied());
							}
						} else if (deleteB.equals("Delete Job Card")) {
							GetValues(request, response);
							if (ReturnPerm(comp_id, "emp_service_jobcard_delete", request).equals("1")) {
								DeleteFields(response);
								DeleteFuelGuageImg(jc_id);
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									response.sendRedirect(response.encodeRedirectURL("jobcard-list.jsp?msg=Job Card deleted successfully!"));
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
		jc_warranty = CheckBoxValue(PadQuotes(request.getParameter("chk_jc_warranty")));
		jc_branch_id = CNumeric(PadQuotes(request.getParameter("dr_branch")));
		if (!jc_branch_id.equals("0")) {
			StrSql = "SELECT branch_brand_id"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_id = " + jc_branch_id;
			// SOP("StrSql==" + StrSql);
			brand_id = CNumeric(ExecuteQuery(StrSql));
		}
		jc_no = PadQuotes(request.getParameter("jc_no"));
		jc_time_in = PadQuotes(request.getParameter("txt_jc_time_in"));
		jcdate = ConvertLongDateToStr(jc_time_in);
		jc_active = CheckBoxValue(PadQuotes(request.getParameter("chk_jc_active")));
		jc_critical = CheckBoxValue(PadQuotes(request.getParameter("chk_jc_critical")));
		veh_chassis_no = PadQuotes(request.getParameter("txt_veh_chassis_no"));
		jc_reg_no = PadQuotes(request.getParameter("txt_jc_reg_no"));
		veh_reg_no = PadQuotes(request.getParameter("txt_veh_reg_no"));
		veh_engine_no = PadQuotes(request.getParameter("txt_veh_engine_no"));
		jc_variant_id = CNumeric(request.getParameter("txt_variant_id"));
		modelid = PadQuotes(request.getParameter("txt_model_id"));
		jc_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
		jc_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
		veh_id = CNumeric(PadQuotes(request.getParameter("span_veh_id")));
		jc_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
		if (!veh_id.equals("0") && !jc_veh_id.equals(veh_id)) {
			jc_veh_id = veh_id;
		}

		PopulateContactDetails();
		jc_time_promised = PadQuotes(request.getParameter("txt_jc_time_promised"));
		jc_promisetime = ConvertLongDateToStr(jc_time_promised);
		jc_stage_id = CNumeric(PadQuotes(request.getParameter("dr_jc_stage")));
		jc_time_out = PadQuotes(request.getParameter("txt_jc_deltime"));
		jc_deliveredtime = ConvertLongDateToStr(jc_time_out);
		jc_time_posted = PadQuotes(request.getParameter("txt_jc_time_posted"));
		jc_time_ready = PadQuotes(request.getParameter("txt_jc_comptime"));
		jc_readytime = ConvertLongDateToStr(jc_time_ready);
		jc_cat_id = CNumeric(PadQuotes(request.getParameter("dr_jc_cat")));
		jc_kms = CNumeric(PadQuotes(request.getParameter("txt_jc_kms")));
		jc_fuel_guage = PadQuotes(request.getParameter("txt_jc_fuel_guage"));
		jc_ro_no = PadQuotes(request.getParameter("txt_jc_ro_no"));
		jc_emp_id = CNumeric(PadQuotes(request.getParameter("dr_executive")));
		jc_technician_emp_id = CNumeric(PadQuotes(request.getParameter("dr_jc_technician_emp_id")));
		jc_location_id = CNumeric(PadQuotes(request.getParameter("dr_location")));
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
		jc_title = PadQuotes(request.getParameter("txt_jc_title"));
		jc_instructions = PadQuotes(request.getParameter("txt_jc_instructions"));
		jc_terms = PadQuotes(request.getParameter("txt_jc_terms"));
		jc_inventory = PadQuotes(request.getParameter("txt_jc_inventory"));
		invent_count = CNumeric(PadQuotes(request.getParameter("txt_invent_count")));
		jc_priorityjc_id = CNumeric(PadQuotes(request.getParameter("dr_jc_priority")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		jc_entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		jc_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (add.equals("yes")) {
			StrSql = "SELECT jc_id FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_veh_id = " + jc_veh_id + ""
					+ " AND jc_time_out = ''";

			// SOP("StrSql===2==" + StrSql);

			if (!ExecuteQuery(StrSql).equals("")) {
				msg = "<br>Previous Job Card is not yet completed!";
			}
		}

		if (msg.equals("")) {
			if (jc_title.equals("")) {
				msg += "<br>Enter Job Card Title!";
			}

			if (jc_branch_id.equals("0")) {
				msg += "<br>Select Branch!";
			}

			if (jc_time_in.equals("")) {
				msg += "<br>Enter Job Card Time In!";
			} else {
				if (!isValidDateFormatLong(jc_time_in)) {
					msg += "<br>Enter valid Job Card Time In!";
				} else {
					if (Long.parseLong(ConvertLongDateToStr(jc_time_in)) > Long.parseLong(ToLongDate(kknow()))) {
						msg += "<br>Job Card Time In must be less than or equal to Current Date!";
					}
				}
			}

			if (jc_time_promised.equals("")) {
				msg += "<br>Enter Promised Time!";
			} else {
				if (!isValidDateFormatLong(jc_time_promised)) {
					msg += "<br>Enter valid Promised Time!";
				}
			}

			if (!jc_time_ready.equals("")) {
				if (!isValidDateFormatLong(jc_time_ready)) {
					msg += "<br>Enter valid Ready Time!";
				}
			}

			if (!jc_time_out.equals("")) {
				if (!isValidDateFormatLong(jc_time_out)) {
					msg += "<br>Enter valid Delivered Time!";
				}
			}

			if (!jc_time_posted.equals("")) {
				if (!isValidDateFormatLong(jc_time_posted)) {
					msg += "<br>Enter valid Delivered Time!";
				} else {
					if (!jc_time_in.equals("") && !isValidDateFormatLong(jc_time_in)
							&& Long.parseLong(ConvertLongDateToStr(jc_time_in)) > Long.parseLong(ConvertLongDateToStr(jc_time_posted))) {
						msg += "<br>Job Card Posted Time must be greater than Job Card Time IN!";
					}
				}
			}
			if (jc_time_posted.equals("") && !jc_time_out.equals("")) {
				jc_time_posted = jc_time_out;
			}

			if (jc_stage_id.equals("0")) {
				msg += "<br>Select Stage!";
			}

			if (jc_type_id.equals("0")) {
				msg += "<br>Select Type!";
			}

			if (jc_cat_id.equals("0")) {
				msg += "<br>Select Category!";
			}

			if (jc_kms.equals("0")) {
				msg += "<br>Enter Kms!";
			}

			if (jc_reg_no.equals("")) {
				msg += "<br>Enter Job Card Reg. No.!";
			} else {
				jc_reg_no = jc_reg_no.replaceAll(" ", "");
				jc_reg_no = jc_reg_no.replaceAll("-", "");
				if (!IsValidRegNo(jc_reg_no)) {
					msg += "<br>Enter valid Job Card Registration No.!";
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
						msg += "<br>Delivered Time must be greater than Ready Date!";
					}
				}
			}
			if (status.equals("Add") && jc_warranty.equals("1")) {
				if (brandconfig_principal_id.equals("0")) {
					msg += "<br>Principal is not configured for the Brand!";
				} else if (!brandconfig_principal_id.equals("0") && principalcontactid.equals("0")) {
					msg += "<br>Contact not added for Brand Principal!";
				}
			}
			if (jc_time_ready.equals("") && !jc_time_out.equals("")) {
				jc_readytime = jc_deliveredtime;
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
			if (jc_ro_no.equals("")) {
				msg += "<br>Enter RO No.!";
			}
			if ((!jc_time_out.equals("") && isValidDateFormatLong(jc_time_out)) || jc_stage_id.equals("6")) {
				if (!jc_ro_no.equals("") && jc_ro_no.length() < 2) {
					msg += "<br>RO No. should be atleast Two Digits!";
				} else {
					StrSql = "SELECT jc_ro_no FROM " + compdb(comp_id) + "axela_service_jc"
							+ " WHERE jc_branch_id = " + jc_branch_id + ""
							+ " AND jc_ro_no = '" + jc_ro_no + "'";
					// SOP("strsql===2==" + StrSql);
					if (!ExecuteQuery(StrSql).equals("") && add.equals("yes")) {
						msg += "<br>Similar RO No. found!";
					}
				}
				// else {
				// msg += "<br>Enter RO No.!";
				// }
			}

			if (jc_priorityjc_id.equals("0")) {
				msg += "<br>Select Priority!";
			}

			if (jc_emp_id.equals("0")) {
				msg += "<br>Select Service Advisor!";
			}
			if (jc_technician_emp_id.equals("0")) {
				msg += "<br>Select Technician!";
			}
			if (jc_location_id.equals("0")) {
				msg += "<br>Select Location!";
			}
		}
	}

	protected void AddFields(HttpServletRequest request, String param) throws Exception {
		String veh_lastservice = "", veh_sale_date = "";
		if (param.equals("")) {
			CheckForm();
		}

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
						+ "	jc_ref_customer_id,"
						+ " jc_veh_id,"
						+ " jc_vehmove_id,"
						+ " jc_reg_no,"
						+ " jc_variant_id,"
						+ " jc_warranty,"
						+ " jc_jctype_id,"
						+ " jc_jccat_id,"
						+ " jc_kms,"
						+ " jc_fuel_guage,"
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
						+ " jc_inventory,"
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
						+ " jc_critical,"
						+ " jc_active,"
						+ " jc_notes,"
						+ " jc_entry_id,"
						+ " jc_entry_date)"
						+ " VALUES"
						+ " (" + jc_branch_id + ","
						+ " (SELECT COALESCE(MAX(jc.jc_no), 0) + 1 FROM " + compdb(comp_id) + "axela_service_jc AS jc"
						+ " WHERE jc.jc_branch_id  = " + jc_branch_id + "),"
						+ " '" + jcdate + "',";
				if (jc_warranty.equals("1")) {
					StrSql += " " + brandconfig_principal_id + ","
							+ "	" + principalcontactid + ","
							+ "	" + principalcontactid + ","
							+ " " + jc_customer_id + ","; // jc_ref_customer_id
				} else {
					StrSql += " " + jc_customer_id + ","
							+ " " + jc_contact_id + ","
							+ " " + jc_contact_id + ","
							+ "	0,"; // jc_ref_customer_id
				}
				StrSql += " " + jc_veh_id + ","
						+ " " + jc_vehmove_id + ","
						+ " '" + jc_reg_no.toUpperCase() + "',"
						+ " " + jc_variant_id + ","
						+ " '" + jc_warranty + "',"
						+ " " + jc_type_id + ","
						+ " " + jc_cat_id + ","
						+ " '" + jc_kms + "',"
						+ " '" + jc_fuel_guage + "',"
						+ " '" + jc_bill_address + "',"
						+ " '" + jc_bill_city + "',"
						+ " '" + jc_bill_pin + "',"
						+ " '" + jc_bill_state + "',"
						+ " '" + jc_del_address + "',"
						+ " '" + jc_del_city + "',"
						+ " '" + jc_del_pin + "',"
						+ " '" + jc_del_state + "',"
						+ " '" + jc_title + "',"
						+ " '" + jc_cust_voice + "',"
						+ " '" + jc_advice + "',"
						+ " '" + jc_terms + "',"
						+ " '" + jc_inventory + "',"
						+ " '" + jc_instructions + "',"
						+ " " + jc_emp_id + ","
						+ " " + jc_technician_emp_id + ","
						+ " " + jc_location_id + ","
						+ " '" + jc_ro_no + "',"
						+ " '" + jc_promisetime + "',"
						+ " '" + jc_readytime + "',"
						+ " '" + jc_deliveredtime + "',"
						+ " " + jc_stage_id + ","
						+ " " + jc_priorityjc_id + ","
						+ " " + jc_critical + ","
						+ " '" + jc_active + "',"
						+ " '" + jc_notes + "',"
						+ " " + jc_entry_id + ","
						+ " '" + jc_entry_date + "')";
				// SOP("StrSql===addFields===" + StrSqlBreaker(StrSql));
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();

				while (rs.next()) {
					jc_id = rs.getString(1);
				}
				rs.close();
				// To insert the inventories into invent_trans table
				if (Integer.parseInt(CNumeric(invent_count)) > 0) {
					AddVehicleInventory(request);
				}
				StrSql = "SELECT veh_sale_date, veh_lastservice"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " WHERE veh_id = " + jc_veh_id;
				// SOP("StrSql=kms==" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						veh_sale_date = PadQuotes(crs.getString("veh_sale_date"));
						if (veh_sale_date.equals("")) {
							veh_sale_date = "20140101000000";
						}
						veh_lastservice = PadQuotes(crs.getString("veh_lastservice"));
						if (veh_lastservice.equals("")) {
							veh_lastservice = veh_sale_date;
						}
					}
				}
				crs.close();
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
						+ " " + jc_veh_id + ","
						+ " '" + jc_kms + "',"
						+ " '" + jc_entry_id + "',"
						+ " '" + veh_lastservice + "')";
				stmttx.execute(StrSql);
				// for updating the recent kms of the vehicle into veh table
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET"
						+ " veh_kms = " + jc_kms + ","
						+ " veh_emp_id = " + jc_emp_id + ""
						+ " WHERE veh_id = " + jc_veh_id + "";
				stmttx.execute(StrSql);

				if (!jc_id.equals("0")) {
					if (jc_active.equals("1")) {
						if (comp_email_enable.equals("1")
								&& config_email_enable.equals("1")
								&& !config_admin_email.equals("")
								&& brandconfig_jc_new_email_enable.equals("1")) {
							if (!contact_email1.equals("")
									&& !brandconfig_jc_new_email_format.equals("")
									&& !brandconfig_jc_new_email_sub.equals("")) {
								SendEmail();
							}
						}

						if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && brandconfig_jc_new_sms_enable.equals("1")) {
							if (!brandconfig_jc_new_sms_format.equals("") && !contact_mobile1.equals("")) {
								SendSMS();
							}
						}
					}
				}
				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
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

	protected void PopulateVehDetails() {
		try {
			if (!veh_id.equals("0") || !jc_veh_id.equals("0")) {
				StrSql = "SELECT veh_id, IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name) AS variant_name"
						+ " FROM " + compdb(comp_id) + "axela_service_veh"
						+ " INNER JOIN  axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
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

	public void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (msg.equals("")) {

			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				jc_modified_id = emp_id;
				jc_modified_date = ToLongDate(kknow());

				if (!jc_readytime.equals("")) {
					jc_stage_id = "5";
				}

				if (!jc_deliveredtime.equals("")) {
					jc_stage_id = "6";
				}
				// SOP("veh_variant_id======="+jc_variant_id);
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET"
						+ " jc_branch_id = " + jc_branch_id + ","
						+ " jc_jctype_id = " + jc_type_id + ","
						+ " jc_time_in = '" + jcdate + "',"
						+ " jc_time_promised = '" + jc_promisetime + "',"
						+ " jc_time_ready = '" + jc_readytime + "',"
						+ " jc_time_out = '" + jc_deliveredtime + "',"
						+ " jc_time_posted = '" + ConvertLongDateToStr(jc_time_posted) + "',"
						+ " jc_jcstage_id = " + jc_stage_id + ","
						+ " jc_customer_id = " + jc_customer_id + ","
						+ " jc_contact_id = " + jc_contact_id + ","
						+ " jc_veh_id = " + jc_veh_id + ","
						+ " jc_reg_no = '" + jc_reg_no.toUpperCase() + "',"
						+ " jc_variant_id = " + jc_variant_id + ","
						+ " jc_jccat_id = " + jc_cat_id + ","
						+ " jc_kms = " + jc_kms + ","
						+ " jc_fuel_guage = " + jc_fuel_guage + ","
						+ " jc_bill_address = '" + jc_bill_address + "',"
						+ " jc_bill_city = '" + jc_bill_city + "',"
						+ " jc_bill_pin = '" + jc_bill_pin + "',"
						+ " jc_bill_state = '" + jc_bill_state + "',"
						+ " jc_del_address = '" + jc_del_address + "',"
						+ " jc_del_city = '" + jc_del_city + "',"
						+ " jc_del_pin = '" + jc_del_pin + "',"
						+ " jc_del_state = '" + jc_del_state + "',"
						+ " jc_title = '" + jc_title + "',"
						+ " jc_cust_voice = '" + jc_cust_voice + "',"
						+ " jc_advice = '" + jc_advice + "',"
						+ " jc_instructions = '" + jc_instructions + "',"
						+ " jc_terms = '" + jc_terms + "',"
						+ " jc_inventory = '" + jc_inventory + "',"
						+ " jc_emp_id = " + jc_emp_id + ","
						+ " jc_technician_emp_id = " + jc_technician_emp_id + ","
						+ " jc_location_id = " + jc_location_id + ","
						+ " jc_priorityjc_id = '" + jc_priorityjc_id + "',"
						+ " jc_ro_no = '" + jc_ro_no + "',"
						+ " jc_critical = '" + jc_critical + "',"
						+ " jc_active = '" + jc_active + "',"
						+ " jc_notes = '" + jc_notes + "',"
						+ " jc_modified_id = " + jc_modified_id + ","
						+ " jc_modified_date = '" + jc_modified_date + "'"
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("veh_variant_id========" + StrSql);
				stmttx.execute(StrSql);

				// delete all the inventory for that service card and add again
				// in trans table
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_invent_trans"
						+ " WHERE inventtrans_jc_id = " + jc_id + "";
				stmttx.execute(StrSql);

				// adding the vehicle inventories in trans table
				if (Integer.parseInt(CNumeric(invent_count)) > 0) {
					AddVehicleInventory(request);
				}

				recent_jc_emp_id = ExecuteQuery("SELECT jc_emp_id FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_veh_id = " + jc_veh_id + ""
						+ " ORDER BY jc_id DESC LIMIT 1");
				// SOP("veh_variant_id===update==="+jc_variant_id);

				// for updating the recent kms of the vehicle into veh table
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET"
						+ " veh_kms = " + jc_kms + ","
						+ " veh_emp_id = " + recent_jc_emp_id + ""
						+ " WHERE veh_id = " + jc_veh_id + "";
				// SOP("Strsql====="+StrSql);
				stmttx.execute(StrSql);

				// for updating the vehicle servicing info, only if the Job Card
				// delivery time is given
				if (!jc_deliveredtime.equals("")) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
							+ " SET"
							+ " veh_lastservice = '" + jc_deliveredtime + "',"
							+ " veh_service_duekms = '" + veh_service_duekms + "',"
							+ " veh_service_duedate = '" + veh_service_duedate + "'"
							+ " WHERE veh_id = " + jc_veh_id + "";
					stmttx.execute(StrSql);
				}

				// Delete PSF if posted time is blank
				if (jc_time_out.equals("")) {
					StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psf"
							+ " WHERE jcpsf_jc_id = " + jc_id + "";
					stmttx.execute(StrSql);
				}

				conntx.commit();

			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
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

	protected void AddVehicleInventory(HttpServletRequest request) throws SQLException {
		try {
			for (int i = 1; i <= Integer.parseInt(CNumeric(invent_count)); i++) {
				inventtrans_jc_id = jc_id;
				inventtrans_invent_id = PadQuotes(request.getParameter("txt_veh_invent" + i));
				if (PadQuotes(request.getParameter("chk_veh_invent" + i)).equals("on")) {
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_invent_trans"
							+ " (inventtrans_jc_id,"
							+ " inventtrans_invent_id)"
							+ " VALUES"
							+ " (" + inventtrans_jc_id + ","
							+ " " + inventtrans_invent_id + ")";
					stmttx.addBatch(StrSql);
				}
			}
			stmttx.executeBatch();
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
			}
			msg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void CalculateVehicleService() throws SQLException {
		try {
			String itemservice_id = "0";
			String itemservice_kms = "0";
			String itemservice_days = "0";

			StrSql = "SELECT itemservice_id FROM " + compdb(comp_id) + "axela_inventory_item_service"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_variant_id = itemservice_item_id"
					+ " WHERE itemservice_jctype_id > " + jc_type_id + ""
					+ " AND veh_id = " + jc_veh_id + ""
					+ " GROUP BY itemservice_id"
					+ " ORDER BY itemservice_jctype_id"
					+ " LIMIT 1";
			itemservice_id = CNumeric(ExecuteQuery(StrSql));

			if (itemservice_id.equals("0")) {
				StrSql = "SELECT MAX(itemservice_id) FROM " + compdb(comp_id) + "axela_inventory_item_service"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_variant_id = itemservice_item_id"
						+ " WHERE veh_id = " + jc_veh_id + ""
						+ " GROUP BY itemservice_id"
						+ " ORDER BY itemservice_jctype_id";
				itemservice_id = CNumeric(ExecuteQuery(StrSql));
			}

			if (itemservice_id.equals("0")) {
				itemservice_kms = "0";
				itemservice_days = "0";
			} else {
				StrSql = "SELECT itemservice_kms, itemservice_days"
						+ " FROM " + compdb(comp_id) + "axela_inventory_item_service"
						+ " WHERE itemservice_id = " + itemservice_id + "";
				CachedRowSet crs = processQuery(StrSql, 0);

				while (crs.next()) {
					itemservice_kms = crs.getString("itemservice_kms");
					itemservice_days = crs.getString("itemservice_days");
				}
				crs.close();
			}

			veh_service_duekms = (Integer.parseInt(jc_kms) + Integer.parseInt(itemservice_kms)) + "";
			veh_service_duedate = ConvertShortDateToStr(AddDayMonthYear(strToShortDate(jc_deliveredtime), Integer.parseInt(itemservice_days), 0, 0, 0));
		} catch (Exception e) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	public void AddPSFFields(String jc_id, String comp_id, String jc_jccat_id, String jc_jctype_id) throws SQLException {
		try {
			StrSql = "SELECT jc_id,"
					+ " COALESCE((SELECT emp_id"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service_psf = 1"
					+ " AND emp_active = 1"
					+ " AND (emp_branch_id = jc_branch_id"
					+ " OR emp_id IN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp_branch"
					+ " WHERE emp_branch_id = jc_branch_id ))"
					+ " AND FIND_IN_SET(emp_id,psfdays_emp_id)"
					+ " LIMIT 1), 1),"
					+ " psfdays_id,"
					+ " DATE_FORMAT(DATE_ADD(CONCAT(SUBSTR(jc_time_out, 1, 8), SUBSTR(jc_time_out, 9, 14)),"
					+ " INTERVAL (psfdays_daycount - 1) DAY	),'%Y%m%d%H%i%s')"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_brand_id = branch_brand_id"
					+ " WHERE jc_id =" + jc_id
					+ " AND psfdays_active = 1";
			if (!jc_jccat_id.equals("0")) {
				StrSql += " AND jc_jccat_id IN ( SELECT psfdayscattrans_jccat_id"
						+ " FROM " + compdb(comp_id) + "axela_psfdayscattrans"
						+ " WHERE 1 = 1"
						+ " AND psfdayscattrans_psfdays_id = psfdays_id )";
			}
			if (!jc_jctype_id.equals("0")) {
				StrSql += " AND jc_jctype_id IN ( SELECT psfdaytypestrans_jctype_id"
						+ " FROM " + compdb(comp_id) + "axela_psfdaystypetrans"
						+ " WHERE 1 = 1"
						+ " AND psfdaytypestrans_psfdays_id = psfdays_id )";
			}
			StrSql += " AND concat(jc_id, psfdays_id) NOT IN (SELECT concat(jcpsf_jc_id, jcpsf_psfdays_id)"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_psf)";

			// SOP("strsql============selectpsf==============" + StrSql);
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_psf"
					+ " (jcpsf_jc_id,"
					+ " jcpsf_emp_id,"
					+ " jcpsf_psfdays_id,"
					+ " jcpsf_followup_time" + " ) "
					+ StrSql;
			// SOP("strsql==AddPSFFields==" + StrSql);
			updateQuery(StrSql);
		} catch (Exception ex) {

			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

	// public void AddPSFFields(String jc_id, String comp_id) throws SQLException {
	// StrSql = "Select "
	// + " jc_id,"
	// + " COALESCE((SELECT CASE WHEN psfdays_exe_type = 1 THEN (SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
	// + " where emp_branch_id = jc_branch_id and emp_service_psf = 1 and emp_active=1 ORDER BY RAND() LIMIT 1)"
	// + " WHEN  psfdays_exe_type = 2 THEN jc_emp_id END), jc_emp_id),"
	// + " psfdays_id,"
	// + " DATE_FORMAT(DATE_ADD(concat(substr(jc_time_out, 1, 8), substr(jc_time_out, 9, 14)),"
	// + " INTERVAL (psfdays_daycount - 1) DAY	),'%Y%m%d%H%i%s')"
	// + " FROM " + compdb(comp_id) + "axela_service_jc"
	// + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_service_jc_psfdays ON psfdays_brand_id = branch_brand_id"
	// + " WHERE jc_id =" + jc_id
	// + " AND psfdays_active = 1"
	// + " AND psfdays_jccat_id = jccat_id"
	// + " AND concat(jc_id, psfdays_id) NOT IN (SELECT concat(jcpsf_jc_id, jcpsf_psfdays_id)"
	// + " FROM " + compdb(comp_id) + "axela_service_jc_psf)";
	// SOP("StrSql----------" + StrSql);
	// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_psf ("
	// + " jcpsf_jc_id,"
	// + " jcpsf_emp_id,"
	// + " jcpsf_psfdays_id,"
	// + " jcpsf_followup_time"
	// + " ) " + StrSql;
	// // SOP("strsql=psf=======" + StrSql);
	// updateQuery(StrSql);
	// }

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_service_jc.*, COALESCE(veh_id, 0) AS veh_id,"
					+ " COALESCE(title_id, 0) AS title_id,"
					+ " COALESCE(title_desc, '') AS title_desc,"
					+ " COALESCE(contact_fname, '') AS contact_fname,"
					+ " COALESCE(contact_lname, '') AS contact_lname,"
					+ " COALESCE(contact_mobile1, '') AS contact_mobile1,"
					+ " COALESCE(contact_phone1, '') AS contact_phone1,"
					+ " COALESCE(contact_email1, '') AS contact_email1,"
					+ " COALESCE(contact_email2, '') AS contact_email2,"
					+ " COALESCE(contact_id, 0) AS contact_id,"
					+ " COALESCE(customer.customer_id, 0) AS customer_id,"
					+ " COALESCE(customer.customer_name, '') AS customer_name,"
					+ " COALESCE(refcustomer.customer_id, 0) AS refcustomer_id,"
					+ " COALESCE(refcustomer.customer_name, '') AS refcustomer_name,"
					+ " COALESCE(veh_chassis_no, '') AS veh_chassis_no,"
					+ " COALESCE(veh_reg_no, '') AS veh_reg_no,"
					+ " COALESCE(veh_engine_no, '') AS veh_engine_no,"
					+ " COALESCE(variant_preownedmodel_id, 0) AS variant_preownedmodel_id,"
					+ " COALESCE(jc_variant_id, 0) AS jc_variant_id,"
					+ " COALESCE(IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name), '') AS variant_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN  axelaauto.axela_preowned_variant ON variant_id = jc_variant_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer customer ON customer.customer_id = jc_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer refcustomer ON refcustomer.customer_id = jc_ref_customer_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE 1=1 "
					+ " AND jc_id = " + jc_id + " "
					+ BranchAccess.replace("branch_id", "jc_branch_id");
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					jc_title = crs.getString("jc_title");
					jc_branch_id = crs.getString("jc_branch_id");
					jc_type_id = crs.getString("jc_jctype_id");
					jc_no = crs.getString("jc_no");
					jc_time_in = strToLongDate((crs.getString("jc_time_in")));
					jc_fuel_guage = crs.getString("jc_fuel_guage");
					jc_reg_no = SplitRegNo(crs.getString("jc_reg_no"), 2);
					modelid = crs.getString("variant_preownedmodel_id");
					jc_variant_id = crs.getString("jc_variant_id");
					jc_warranty = crs.getString("jc_warranty");
					if (jc_warranty.equals("1")) {
						jcwarranty = "Warranty";
					}
					veh_id = crs.getString("veh_id");
					jc_veh_id = crs.getString("jc_veh_id");
					if (!crs.getString("veh_id").equals("0")) {
						link_veh_name = "<a href=\"../service/vehicle-list.jsp?veh_id=" + crs.getString("veh_id") + "\">"
								+ crs.getString("variant_name") + "</a>";
					}
					jc_kms = crs.getString("jc_kms");
					veh_chassis_no = crs.getString("veh_chassis_no");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					veh_engine_no = crs.getString("veh_engine_no");
					jc_cat_id = crs.getString("jc_jccat_id");
					jc_instructions = crs.getString("jc_instructions");
					jc_time_promised = strToLongDate(crs.getString("jc_time_promised"));
					jc_time_ready = strToLongDate(crs.getString("jc_time_ready"));
					jc_time_out = strToLongDate(crs.getString("jc_time_out"));
					jc_time_posted = strToLongDate(crs.getString("jc_time_posted"));
					jc_stage_id = crs.getString("jc_jcstage_id");
					jc_customer_id = crs.getString("jc_customer_id");
					jc_contact_id = crs.getString("jc_contact_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">"
							+ crs.getString("customer_name") + "</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " " + crs.getString("contact_lname") + "</a>";
					link_refcustomer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("refcustomer_id") + "\">"
							+ crs.getString("refcustomer_name") + "</a>";
					customer_name = crs.getString("customer_name");
					jc_bill_address = crs.getString("jc_bill_address");
					jc_bill_city = crs.getString("jc_bill_city");
					jc_bill_pin = crs.getString("jc_bill_pin");
					jc_bill_state = crs.getString("jc_bill_state");
					jc_del_address = crs.getString("jc_del_address");
					jc_del_city = crs.getString("jc_del_city");
					jc_del_pin = crs.getString("jc_del_pin");
					jc_del_state = crs.getString("jc_del_state");
					jc_cust_voice = crs.getString("jc_cust_voice");
					jc_advice = crs.getString("jc_advice");
					jc_terms = crs.getString("jc_terms");
					jc_inventory = crs.getString("jc_inventory");
					jc_emp_id = crs.getString("jc_emp_id");
					jc_technician_emp_id = crs.getString("jc_technician_emp_id");
					jc_location_id = crs.getString("jc_location_id");
					jc_priorityjc_id = crs.getString("jc_priorityjc_id");
					jc_ro_no = crs.getString("jc_ro_no");
					jc_critical = crs.getString("jc_critical");
					jc_active = crs.getString("jc_active");
					jc_notes = crs.getString("jc_notes");
					jc_entry_id = crs.getString("jc_entry_id");
					if (!jc_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(jc_entry_id));
					}
					jc_entry_date = strToLongDate(crs.getString("jc_entry_date"));
					jc_modified_id = crs.getString("jc_modified_id");
					if (!jc_modified_id.equals("")) {
						modified_by = Exename(comp_id, Integer.parseInt(jc_modified_id));
					}
					jc_modified_date = strToLongDate(crs.getString("jc_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Job Card!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void SendEmail() throws SQLException {
		emailmsg = brandconfig_jc_new_email_format;
		emailsub = brandconfig_jc_new_email_sub;

		emailsub = "REPLACE('" + emailsub + "','[JOBCARDID]', jc_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERID]', customer_id)";
		emailsub = "REPLACE(" + emailsub + ", '[CUSTOMERNAME]', customer_name)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTMOBILE1]', contact_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTPHONE1]', contact_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[CONTACTEMAIL1]', contact_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXENAME]', emp_name)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEJOBTITLE]', jobtitle_desc)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEMOBILE1]', emp_mobile1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEPHONE1]', emp_phone1)";
		emailsub = "REPLACE(" + emailsub + ", '[EXEEMAIL1]', emp_email1)";
		emailsub = "REPLACE(" + emailsub + ", '[MODELNAME]', COALESCE(preownedmodel_name,''))";
		emailsub = "REPLACE(" + emailsub + ", '[ITEMNAME]', COALESCE(variant_name,''))";
		emailsub = "REPLACE(" + emailsub + ", '[REGNO]', veh_reg_no)";

		emailmsg = "REPLACE('" + emailmsg + "','[JOBCARDID]', jc_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERID]', customer_id)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CUSTOMERNAME]', customer_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTMOBILE1]', contact_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTPHONE1]', contact_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[CONTACTEMAIL1]', contact_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXENAME]', emp_name)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEJOBTITLE]', jobtitle_desc)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEMOBILE1]', emp_mobile1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEPHONE1]', emp_phone1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[EXEEMAIL1]', emp_email1)";
		emailmsg = "REPLACE(" + emailmsg + ", '[MODELNAME]', COALESCE(preownedmodel_name,''))";
		emailmsg = "REPLACE(" + emailmsg + ", '[ITEMNAME]', COALESCE(variant_name,''))";
		emailmsg = "REPLACE(" + emailmsg + ", '[REGNO]', veh_reg_no)";
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]', branch_add)";

		try {
			String email_to = contact_email1;
			if (!contact_email2.equals("")) {
				email_to += "," + contact_email2;
			}

			StrSql = "SELECT"
					+ "	branch_id,"
					+ " '" + jc_contact_id + "',"
					+ " '" + contact_fname + " " + contact_lname + "',"
					+ " '" + config_admin_email + "',"
					+ " '" + email_to + "',"
					+ " " + emailsub + ","
					+ " " + emailmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " " + emp_id + ","
					+ " 0"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_entry_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_trans ON jctrans_jc_id = jc_id"
					+ " AND jctrans_rowcount != 0"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = jctrans_item_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE jc_id = " + jc_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
					+ " ("
					+ "	email_branch_id,"
					+ "	email_contact_id,"
					+ " email_contact,"
					+ " email_from,"
					+ " email_to,"
					+ " email_subject,"
					+ " email_msg,"
					+ " email_date,"
					+ " email_entry_id,"
					+ " email_sent)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed...");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			emailmsg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void SendSMS() throws SQLException {
		smsmsg = brandconfig_jc_new_sms_format;

		smsmsg = "REPLACE('" + smsmsg + "','[JOBCARDID]', jc_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERID]', customer_id)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CUSTOMERNAME]', customer_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTNAME]', CONCAT(contact_fname, ' ', contact_lname))";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTJOBTITLE]', contact_jobtitle)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTMOBILE1]', contact_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTPHONE1]', contact_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[CONTACTEMAIL1]', contact_email1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXENAME]', emp_name)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEJOBTITLE]', jobtitle_desc)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEMOBILE1]', emp_mobile1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEPHONE1]', emp_phone1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[EXEEMAIL1]', emp_email1)";
		smsmsg = "REPLACE(" + smsmsg + ", '[MODELNAME]', COALESCE(preownedmodel_name,''))";
		smsmsg = "REPLACE(" + smsmsg + ", '[ITEMNAME]', COALESCE(variant_name,''))";
		smsmsg = "REPLACE(" + smsmsg + ", '[REGNO]', veh_reg_no)";
		smsmsg = "REPLACE(" + smsmsg + ", '[BRANCHADDRESS]', branch_add)";

		try {
			StrSql = "SELECT"
					+ " " + jc_branch_id + ","
					+ " " + jc_contact_id + ","
					+ " '" + contact_fname + " " + contact_lname + "',"
					+ " '" + contact_mobile1 + "',"
					+ " " + smsmsg + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " 0,"
					+ " " + emp_id + ""
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_entry_id"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_trans ON jctrans_jc_id = jc_id"
					+ " AND jctrans_rowcount != 0"
					+ " LEFT JOIN axelaauto.axela_preowned_variant ON variant_id = jctrans_item_id"
					+ " LEFT JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE jc_id = " + jc_id + "";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sms"
					+ " (sms_branch_id,"
					+ " sms_contact_id,"
					+ " sms_contact,"
					+ " sms_mobileno,"
					+ " sms_msg,"
					+ " sms_date,"
					+ " sms_sent,"
					+ " sms_entry_id)"
					+ " " + StrSql + "";
			stmttx.execute(StrSql);
		} catch (Exception e) {
			if (conntx.isClosed()) {
				SOPError("connection is closed.....");
			}
			if (!conntx.isClosed() && conntx != null) {
				conntx.rollback();
				SOPError("connection rollback...");
			}
			smsmsg = "<br>Transaction Error!";
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
		}
	}

	protected void DeleteFields(HttpServletResponse response) {
		// Association with Invoice
		// StrSql = "SELECT invoice_jc_id FROM " + compdb(comp_id) +
		// "axela_invoice"
		// + " WHERE invoice_jc_id = " + jc_id + "";
		// if (ExecuteQuery(StrSql).equals(jc_id)) {
		// msg += "<br>Job Card is associated with Invoice!";
		// }

		// Association with Ticket
		StrSql = "SELECT ticket_jc_id FROM " + compdb(comp_id) + "axela_service_ticket"
				+ " WHERE ticket_jc_id = " + jc_id + "";
		if (ExecuteQuery(StrSql).equals(jc_id)) {
			msg += "<br>Job Card is associated with Ticket!";
		}
		StrSql = "SELECT voucher_jc_id FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " WHERE voucher_jc_id = " + jc_id + "";
		if (ExecuteQuery(StrSql).equals(jc_id)) {
			msg += "<br>Job Card is associated with Voucher!";
		}
		if (msg.equals("")) {
			StrSql = "SELECT doc_value FROM " + compdb(comp_id) + "axela_service_jc_docs"
					+ " WHERE doc_jc_id = " + jc_id + "";
			String filename = ExecuteQuery(StrSql);

			if (!filename.equals("") && filename != null) {
				File f = new File(ContractDocPath(comp_id) + filename);
				if (f.exists()) {
					f.delete();
				}
			}

			// Delete all the Documents
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_docs"
					+ " WHERE doc_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Delete all the Images
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_img"
					+ " WHERE img_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Delete all the Inventory Trans
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_invent_trans"
					+ " WHERE inventtrans_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Delete all the Check Trans
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
					+ " WHERE trans_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Delete all the Bay Trans
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_bay_trans"
					+ " WHERE baytrans_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Delete all the History
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_history"
					+ " WHERE history_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Delete all the PSFs
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psf"
					+ " WHERE jcpsf_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Delete all the Items
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_trans"
					+ " WHERE jctrans_jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// At the last delete the JobCard
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id + "";
			updateQuery(StrSql);

			// Update the Inventory Current Stock by calling Connect class
			// function
			// UpdateStock(comp_id, "0", "0");
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

	public String PopulateExecutive(String jc_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			// SOP("jc_branch_id+====" + jc_branch_id);
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_service = 1";
			if (!jc_branch_id.equals("0")) {
				StrSql += " AND emp_branch_id = " + jc_branch_id;
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";

			// SOP("strsql====+hello" + StrSql);

			// SOP("strsql====+PopulateExecutive" + StrSqlBreaker(StrSql));

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_executive\" id=\"dr_executive\" class=\"form-control\">");
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), jc_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("<select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateTechnicianExecutive(String jc_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT emp_id, CONCAT(emp_name, ' (', emp_ref_no, ')') AS emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_technician = 1";
			if (!jc_branch_id.equals("0")) {
				StrSql += " AND emp_branch_id = " + jc_branch_id;
			}
			StrSql += " GROUP BY emp_id"
					+ " ORDER BY emp_name";
			// SOP("strsql==tech===" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_jc_technician_emp_id\" id=\"dr_jc_technician_emp_id\" class=\"form-control\">");
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id"));
				Str.append(StrSelectdrop(crs.getString("emp_id"), jc_technician_emp_id));
				Str.append(">").append(crs.getString("emp_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateInventoryLocation(String jc_branch_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT location_name, location_id"
					+ " FROM " + compdb(comp_id) + "axela_inventory_location"
					+ " WHERE location_branch_id = " + jc_branch_id + ""
					+ " ORDER BY location_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("PopulateInventoryLocation=====" + StrSqlBreaker(StrSql));
			Str.append("<select name=\"dr_location\" id=\"dr_location\" class=\"form-control\">");
			Str.append("<option value=\"0\"> Select Location </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("location_id"));
				Str.append(Selectdrop(crs.getInt("location_id"), jc_location_id));
				Str.append(">").append(crs.getString("location_name")).append("</option>\n");
			}
			Str.append("</select>");
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

	public String PopulateJobCardCategory() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jccat_id, jccat_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
					+ " ORDER BY jccat_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
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
					jc_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					jc_customer_id = crs.getString("customer_id");
					jc_variant_id = crs.getString("veh_variant_id");
					modelid = crs.getString("variant_preownedmodel_id");
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

	public String PopulateVehicleInventory(HttpServletRequest request) {
		StringBuilder Str = new StringBuilder();
		try {
			int count = 0, td_count = 0;
			String checked = "";
			StrSql = "SELECT invent_id, invent_name, COALESCE(inventtrans_jc_id, 0) AS inventtrans_jc_id"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_invent"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc_invent_trans ON inventtrans_invent_id = invent_id"
					+ " AND inventtrans_jc_id = " + jc_id + ""
					+ " WHERE invent_model_id = " + modelid + ""
					+ " GROUP BY invent_id"
					+ " ORDER BY invent_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			// Str.append("<table border=0 cellspacing=0 cellpadding=0 class=\"listtable\">\n");
			if (crs.isBeforeFirst()) {
				Str.append("<label >\n Car Inventory \n</label>\n");
				while (crs.next()) {
					checked = "";
					count++;
					td_count++;
					Str.append("<div class=col-md-6>\n");
					if (td_count > 4) {
						td_count = 1;
						// Str.append("<div>\n");
					}
					if (PadQuotes(request.getParameter("chk_veh_invent" + count)).equals("on")) {
						checked = "checked";
					} else if (!crs.getString("inventtrans_jc_id").equals("0")) {
						checked = "checked";
					}
					Str.append("</div>\n");
					Str.append("<div><input id=\"chk_veh_invent").append(count).append("\" name=\"chk_veh_invent");
					Str.append(count).append("\" type=\"checkbox\" ").append(checked).append("/> ").append(crs.getString("invent_name"));
					Str.append("<input type=\"hidden\" id=\"txt_veh_invent").append(count).append("\" name=\"txt_veh_invent");
					Str.append(count).append("\" value=\"").append(crs.getString("invent_id")).append("\">");
					Str.append("</div>\n");
				}
				if (td_count < 4) {
					for (int i = td_count; i < 4; i++) {
						// Str.append("<td>&nbsp;</td>\n");
					}
				}
				// Str.append("</div>\n");
			}
			crs.close();
			Str.append("<label > Other Inventory:</label>");
			Str.append("<textarea name=\"txt_jc_inventory\" id=\"txt_jc_inventory\" cols=\"50\" rows=\"5\"");
			Str.append(" class=\"form-control\">").append(jc_inventory).append("</textarea>");
			Str.append("<input type=\"hidden\" id=\"txt_invent_count\" name=\"txt_invent_count\" value=\"").append(count).append("\">");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	protected void PopulateContactDetails() {
		try {
			StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname, title_desc,"
					+ " contact_lname, contact_email1, contact_email2, contact_mobile1"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " WHERE contact_id = " + jc_contact_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
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

	public String PopulatePriority() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT priorityjc_id, priorityjc_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_priority"
					+ " ORDER BY priorityjc_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\">Select</option>\n");
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

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT COALESCE(brandconfig_jc_new_email_enable, '') AS brandconfig_jc_new_email_enable,"
					+ " COALESCE(brandconfig_jc_new_email_format, '') AS brandconfig_jc_new_email_format,"
					+ " COALESCE(brandconfig_jc_new_sms_enable, '') AS brandconfig_jc_new_sms_enable,"
					+ " COALESCE(brandconfig_jc_new_sms_format, '') AS brandconfig_jc_new_sms_format,"
					+ " COALESCE(brandconfig_jc_new_email_sub, '') AS brandconfig_jc_new_email_sub,"
					+ " config_admin_email, config_email_enable, config_sms_enable, comp_sms_enable,"
					+ " config_invoice_invoice_refno, comp_email_enable,"
					+ " COALESCE(brandconfig_principal_id, 0) AS brandconfig_principal_id,"
					+ " COALESCE(contact_id, 0) AS principalcontactid"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + jc_branch_id
					+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_customer_id = brandconfig_principal_id";
			// SOP("StrSql==PopulateConfigDetails==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				brandconfig_jc_new_email_enable = crs.getString("brandconfig_jc_new_email_enable");
				brandconfig_jc_new_email_format = crs.getString("brandconfig_jc_new_email_format");
				brandconfig_jc_new_sms_enable = crs.getString("brandconfig_jc_new_sms_enable");
				brandconfig_jc_new_sms_format = crs.getString("brandconfig_jc_new_sms_format");
				brandconfig_jc_new_email_sub = crs.getString("brandconfig_jc_new_email_sub");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
				brandconfig_principal_id = crs.getString("brandconfig_principal_id");
				principalcontactid = crs.getString("principalcontactid");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String PopulateStage() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT jcstage_id, jcstage_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_stage";
			if (add.equals("yes")) {
				StrSql += " WHERE jcstage_id != 5"
						+ " AND jcstage_id != 6";
			}
			StrSql += " ORDER BY jcstage_rank";
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

	BufferedImage CreateFuelGuageImg(String jc_id, String jc_fuel_guage) throws IOException {

		int fuelGuage = Integer.parseInt(jc_fuel_guage);
		String filePath = TemplatePath(comp_id) + "fuelguage.jpg";
		ImageIO.setUseCache(false);
		// Read the fuel guage image from the Server tamplate Location
		bufferedImage = ImageIO.read(new File(filePath));
		BufferedImage jcfg = new BufferedImage(300, 60, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = jcfg.createGraphics();
		g.setComposite(AlphaComposite.Src);
		g.drawImage(bufferedImage, 0, 0, null);
		if (fuelGuage < 10) {
			fuelGuage = (3 * fuelGuage) + 12;
		} else if (fuelGuage > 90) {
			fuelGuage = (3 * fuelGuage) - 12;
		} else if (fuelGuage < 48) {
			fuelGuage = (3 * fuelGuage) + 5;
		} else if (fuelGuage > 52) {
			fuelGuage = (3 * fuelGuage) - 5;
		} else {
			fuelGuage = (3 * fuelGuage) - 1;
		}
		Graphics2D g1 = (Graphics2D) g;
		g.setColor(Color.red);
		g.setBackground(Color.cyan);
		g1.drawLine(fuelGuage - 10, 10, fuelGuage + 10, 10);
		g1.drawLine(fuelGuage, 20, fuelGuage + 10, 10);
		g1.drawLine(fuelGuage, 21, fuelGuage + 10, 11);
		g1.drawLine(fuelGuage - 10, 10, fuelGuage, 20);
		g1.drawLine(fuelGuage - 11, 10, fuelGuage, 21);
		// g1.setComposite(AlphaComposite.Src);
		g1.dispose();
		g.dispose();
		return jcfg;
	}

	void DeleteFuelGuageImg(String jc_id) throws IOException {
		File f1 = new File(fpath);
		if (f1.exists()) {
			f1.delete();
		}
	}
}
