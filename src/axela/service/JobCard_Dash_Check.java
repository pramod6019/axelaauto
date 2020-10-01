package axela.service;

////////////Divya 19th jan 2013
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class JobCard_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String name = "";
	public String value = "";
	public String checked = "";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String history_actiontype = "";
	public String history_oldvalue = "";
	public String history_newvalue = "";
	public String chk_invent_name = "";
	public String fpath = "";
	public String customer_id = "0";
	public String jc_id = "0";
	public String jc_comm_contact_id = "0";
	public String jc_emp_id = "0";
	public String jc_stage_id = "0";
	public String jc_ro_no = "";
	public String jc_technician_emp_id = "0";
	public String jc_veh_id = "0";
	public String veh_variant_id = "0";
	public String jc_branch_id = "0";
	public String jc_contact_id = "0";
	public String jc_time_in = "";
	public String contact_email1 = "", contact_email2 = "", contact_mobile1 = "";
	public String contact_fname = "", contact_lname = "";
	public String comp_email_enable = "0";
	public String comp_sms_enable = "0";
	public String brandconfig_jc_delivered_email_enable = "0";
	public String brandconfig_jc_delivered_email_format = "";
	public String brandconfig_jc_delivered_email_sub = "";
	public String brandconfig_jc_delivered_sms_enable = "0";
	public String brandconfig_jc_delivered_sms_format = "";
	public String config_admin_email = "";
	public String config_refno_enable = "0";
	public String config_email_enable = "0";
	public String config_sms_enable = "0";
	public String emailmsg = "", emailsub = "";
	public String smsmsg = "";
	public String customer_details = "";
	public String manhour_details = "";
	public String man_hours = "";
	public String baytrans_emp_id = "0";
	public BufferedImage bufferedImage;
	public String display_customer_details = "";
	public String populate_contacts = "";
	public String customer_edit_perm = "0";
	public String invoice_details = "";
	public String receipt_details = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		// SOP("JobCard_Dash_Check==");
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			emp_id = CNumeric(GetSession("emp_id", request));
			customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
			jc_comm_contact_id = CNumeric(PadQuotes(request.getParameter("jc_comm_contact_id")));
			jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
			jc_stage_id = CNumeric(PadQuotes(request.getParameter("jc_stage_id")));
			jc_ro_no = PadQuotes(request.getParameter("jc_ro_no"));
			baytrans_emp_id = CNumeric(PadQuotes(request.getParameter("baytrans_emp_id")));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			man_hours = PadQuotes(request.getParameter("man_hours"));
			customer_details = PadQuotes(request.getParameter("customer_details"));
			manhour_details = PadQuotes(request.getParameter("manhour_details"));
			checked = PadQuotes(request.getParameter("checked"));
			display_customer_details = PadQuotes(request.getParameter("display_customer_details"));
			populate_contacts = PadQuotes(request.getParameter("populate_contacts"));
			chk_invent_name = PadQuotes(request.getParameter("chk_invent_name"));
			invoice_details = PadQuotes(request.getParameter("invoice_details"));
			receipt_details = PadQuotes(request.getParameter("receipt_details"));

			Vehicle_Dash veh_dash = new Vehicle_Dash();
			File f = new File(JobCardFuelGuagePath(comp_id));
			if (!f.exists()) {
				f.mkdirs();
			}

			if (man_hours.equals("yes") && !jc_id.equals("0")) {
				StrHTML = new JobCard_Dash().ListJCManHours(jc_id, baytrans_emp_id);
			} else if (customer_details.equals("yes")) {
				StrHTML = new Enquiry_Dash_Customer().CustomerDetails(response, customer_id, "yes", comp_id);
			} else if (display_customer_details.equals("yes")) {
				customer_edit_perm = ReturnPerm(comp_id, "emp_customer_edit", request);
				StrHTML = veh_dash.CustomerDetails(customer_id, customer_edit_perm, comp_id);
			} else if (populate_contacts.equals("yes")) {
				StrHTML = new JobCard_Dash().PopulateCustomerContacts(customer_id, jc_comm_contact_id, comp_id);
			} else if (invoice_details.equals("yes")) {
				StrHTML = new JobCard_Dash_Invoice().ListInvoice(jc_id, BranchAccess, ExeAccess, comp_id);
			} else if (receipt_details.equals("yes")) {
				StrHTML = new JobCard_Dash_Receipt().ListReceipt(jc_id, BranchAccess, ExeAccess, comp_id);
			} else {
				fpath = JobCardFuelGuagePath(comp_id) + "jcfg_" + jc_id + ".jpg";
				try {
					if (!jc_id.equals("0")) {
						StrSql = "SELECT jc_emp_id, jc_veh_id, jc_branch_id, jc_time_in, jc_contact_id, veh_variant_id"
								+ " FROM " + compdb(comp_id) + "axela_service_jc"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = jc_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
								+ " WHERE jc_id = " + jc_id + BranchAccess + ExeAccess + ""
								+ " GROUP BY jc_id";
						CachedRowSet crs = processQuery(StrSql, 0);

						while (crs.next()) {
							jc_contact_id = crs.getString("jc_contact_id");
							jc_emp_id = crs.getString("jc_emp_id");
							jc_veh_id = crs.getString("jc_veh_id");
							veh_variant_id = crs.getString("veh_variant_id");
							jc_branch_id = crs.getString("jc_branch_id");
							jc_time_in = crs.getString("jc_time_in");
							PopulateContactDetails();
							PopulateConfigDetails();
						}
						crs.close();
					} else {
						StrHTML = "<font color=\"red\">Update Permission Denied!</font>";
						return;
					}
					// SOP("name==" + name);
					if (name.equals("txt_jc_bill_cash_no") || name.equals("txt_jc_bill_cash_parts") || name.equals("txt_jc_bill_cash_customername")

							|| name.equals("txt_jc_bill_cash_parts_tyre_qty") || name.equals("txt_jc_bill_cash_parts_tyre")
							|| name.equals("txt_jc_bill_cash_parts_oil") || name.equals("txt_jc_bill_cash_parts_accessories")
							|| name.equals("txt_jc_bill_cash_parts_labour") || name.equals("txt_jc_bill_cash_parts_valueadd")
							|| name.equals("txt_jc_bill_cash_parts_battery_qty") || name.equals("txt_jc_bill_cash_parts_battery")
							|| name.equals("txt_jc_bill_cash_parts_brake_qty") || name.equals("txt_jc_bill_cash_parts_brake")
							|| name.equals("txt_jc_bill_cash_parts_extwarranty_qty") || name.equals("txt_jc_bill_cash_parts_extwarranty")
							|| name.equals("txt_jc_bill_cash_parts_wheelalign") || name.equals("txt_jc_bill_cash_parts_cng")
							|| name.equals("txt_jc_bill_cash_parts_discamt")
							|| name.equals("txt_jc_bill_cash_labour_tyre_qty") || name.equals("txt_jc_bill_cash_labour_tyre")
							|| name.equals("txt_jc_bill_cash_labour_oil") || name.equals("txt_jc_bill_cash_labour_accessories")
							|| name.equals("txt_jc_bill_cash_labour_labour") || name.equals("txt_jc_bill_cash_labour_valueadd")
							|| name.equals("txt_jc_bill_cash_labour_battery_qty") || name.equals("txt_jc_bill_cash_labour_battery")
							|| name.equals("txt_jc_bill_cash_labour_brake_qty") || name.equals("txt_jc_bill_cash_labour_brake")
							|| name.equals("txt_jc_bill_cash_labour_extwarranty_qty") || name.equals("txt_jc_bill_cash_labour_extwarranty")
							|| name.equals("txt_jc_bill_cash_labour_wheelalign") || name.equals("txt_jc_bill_cash_labour_cng")
							|| name.equals("txt_jc_bill_cash_labour_discamt")) {
						StrSql = "SELECT jc_bill_cash_date  FROM " + compdb(comp_id) + "axela_service_jc WHERE jc_id = " + jc_id;
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "<font color=\"red\">Cash Bill Date can't be empty!</font>";
							return;
						}
					} else if (name.equals("txt_jc_bill_insur_no") || name.equals("txt_jc_bill_insur_customername")
							|| name.equals("txt_jc_bill_insur_parts") || name.equals("txt_jc_bill_insur_parts_tyre_qty")
							|| name.equals("txt_jc_bill_insur_parts_tyre") || name.equals("txt_jc_bill_insur_parts_oil")
							|| name.equals("txt_jc_bill_insur_parts_accessories") || name.equals("txt_jc_bill_insur_parts_labour")
							|| name.equals("txt_jc_bill_insur_parts_valueadd") || name.equals("txt_jc_bill_insur_parts_battery_qty")
							|| name.equals("txt_jc_bill_insur_parts_battery") || name.equals("txt_jc_bill_insur_parts_brake_qty")
							|| name.equals("txt_jc_bill_insur_parts_brake") || name.equals("txt_jc_bill_insur_parts_extwarranty_qty")
							|| name.equals("txt_jc_bill_insur_parts_extwarranty") || name.equals("txt_jc_bill_insur_parts_wheelalign")
							|| name.equals("txt_jc_bill_insur_parts_cng") || name.equals("txt_jc_bill_insur_parts_discamt")

							|| name.equals("txt_jc_bill_insur_labour") || name.equals("txt_jc_bill_insur_labour_tyre_qty")
							|| name.equals("txt_jc_bill_insur_labour_tyre") || name.equals("txt_jc_bill_insur_labour_oil")
							|| name.equals("txt_jc_bill_insur_labour_accessories") || name.equals("txt_jc_bill_insur_labour_labour")
							|| name.equals("txt_jc_bill_insur_labour_valueadd") || name.equals("txt_jc_bill_insur_labour_battery_qty")
							|| name.equals("txt_jc_bill_insur_labour_battery") || name.equals("txt_jc_bill_insur_labour_brake_qty")
							|| name.equals("txt_jc_bill_insur_labour_brake") || name.equals("txt_jc_bill_insur_labour_extwarranty_qty")
							|| name.equals("txt_jc_bill_insur_labour_extwarranty") || name.equals("txt_jc_bill_insur_labour_wheelalign")
							|| name.equals("txt_jc_bill_insur_labour_cng") || name.equals("txt_jc_bill_insur_labour_discamt")) {

						StrSql = "SELECT jc_bill_cash_date  FROM " + compdb(comp_id) + "axela_service_jc WHERE jc_id = " + jc_id;
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "<font color=\"red\">Insurance Bill Date can't be empty!</font>";
							return;
						}
					} else if (name.equals("txt_jc_bill_foc_no") || name.equals("txt_jc_bill_foc_customername")) {
						StrSql = "SELECT jc_bill_foc_date  FROM " + compdb(comp_id) + "axela_service_jc WHERE jc_id = " + jc_id;
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "<font color=\"red\">FOC Bill Date can't be empty!</font>";
							return;
						}
					} else if (name.equals("txt_jc_bill_warranty_no") || name.equals("txt_jc_bill_warranty_customername")
							|| name.equals("txt_jc_bill_warranty_parts_tyre_qty") || name.equals("txt_jc_bill_warranty_parts_tyre")
							|| name.equals("txt_jc_bill_warranty_parts_oil") || name.equals("txt_jc_bill_warranty_parts_accessories")
							|| name.equals("txt_jc_bill_warranty_parts_labour") || name.equals("txt_jc_bill_warranty_parts_valueadd")
							|| name.equals("txt_jc_bill_warranty_parts_battery_qty") || name.equals("txt_jc_bill_warranty_parts_battery")
							|| name.equals("txt_jc_bill_warranty_parts_brake_qty") || name.equals("txt_jc_bill_warranty_parts_brake")
							|| name.equals("txt_jc_bill_warranty_parts_extwarranty_qty") || name.equals("txt_jc_bill_warranty_parts_extwarranty")
							|| name.equals("txt_jc_bill_warranty_parts_wheelalign") || name.equals("txt_jc_bill_warranty_parts_cng")
							|| name.equals("txt_jc_bill_warranty_parts_discamt")
							|| name.equals("txt_jc_bill_warranty_labour_tyre_qty") || name.equals("txt_jc_bill_warranty_labour_tyre")
							|| name.equals("txt_jc_bill_warranty_labour_oil") || name.equals("txt_jc_bill_warranty_labour_accessories")
							|| name.equals("txt_jc_bill_warranty_labour_labour") || name.equals("txt_jc_bill_warranty_labour_valueadd")
							|| name.equals("txt_jc_bill_warranty_labour_battery_qty") || name.equals("txt_jc_bill_warranty_labour_battery")
							|| name.equals("txt_jc_bill_warranty_labour_brake_qty") || name.equals("txt_jc_bill_warranty_labour_brake")
							|| name.equals("txt_jc_bill_warranty_labour_extwarranty_qty") || name.equals("txt_jc_bill_warranty_labour_extwarranty")
							|| name.equals("txt_jc_bill_warranty_labour_wheelalign") || name.equals("txt_jc_bill_warranty_labour_cng")
							|| name.equals("txt_jc_bill_warranty_labour_discamt")) {
						StrSql = "SELECT jc_bill_warranty_date  FROM " + compdb(comp_id) + "axela_service_jc WHERE jc_id = " + jc_id;
						if (ExecuteQuery(StrSql).equals("")) {
							StrHTML = "<font color=\"red\">Warranty Bill Date can't be empty!</font>";
							return;
						}
					}
					if (!jc_emp_id.equals("0") || emp_id.equals("1")) {
						if (name.equals("txt_jc_title")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_title"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_title = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Job Card Title";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Title updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Title!</font>";
							}
						} else if (name.equals("dr_jc_type_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jctype_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_type ON jctype_id = jc_jctype_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_jctype_id = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT jctype_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_type"
										+ " WHERE jctype_id = " + value);

								history_actiontype = "Job Card Type";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Type updated!";
							} else {
								StrHTML = "<font color=\"red\">Select Type!</font>";
							}
						} else if (name.equals("dr_jc_cat_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jccat_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_cat ON jccat_id = jc_jccat_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_jccat_id = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT jccat_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_cat"
										+ " WHERE jccat_id = " + value);
								history_actiontype = "Job Card Category";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Category updated!";
							} else {
								StrHTML = "<font color=\"red\">Select Category!</font>";
							}
						} else if (name.equals("dr_jc_comm_contact_id")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"
									+ " FROM " + compdb(comp_id) + "axela_service_jc"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_comm_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
									+ " WHERE jc_id = " + jc_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET jc_comm_contact_id = '" + value + "'"
									+ " WHERE jc_id = " + jc_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)"
									+ " FROM " + compdb(comp_id) + "axela_service_jc"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_comm_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
									+ " WHERE contact_id = " + value);

							history_actiontype = "Job Card Communication";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
									+ " (history_jc_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + jc_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Communication updated!";
						} else if (name.equals("txt_jc_kms")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_kms"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_kms = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Kms";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Kms updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Kms!</font>";
							}
						} else if (name.equals("txt_jc_fuel_guage")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_fuel_guage"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_fuel_guage = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Fuel Guage";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "%',"
										+ " '" + value + "%')";
								updateQuery(StrSql);
								// if (!jc_id.equals("0")) {
								// SOP("fpath==" + fpath);
								// ImageIO.write(CreateFuelGuageImg(jc_id, value), "jpg", new File(fpath));
								// }
								StrHTML = "Fuel Guage updated!";
							} else {
								StrHTML = "<font color=\"red\">Slect Fuel Guage!</font>";
							}
						} else if (name.equals("txt_jc_cust_voice")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_cust_voice FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_cust_voice = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Customer Voice";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Customer Voice updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Customer Voice!</font>";
							}
						} else if (name.equals("txt_jc_advice")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_advice"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_advice = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Service Advice";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Service Advice updated!";
							}
						} else if (name.equals("txt_jc_instr")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_instructions"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_instructions = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);
								history_actiontype = "Instruction";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Instruction updated!";
							} else {
								StrHTML = "Enter Instruction!";
							}
						} else if (name.equals("txt_jc_terms")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_terms"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_terms = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Terms";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Terms updated!";
							} else {
								StrHTML = "Enter Terms!";
							}
						} else if (name.equals("dr_jc_emp_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT emp_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_emp_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_emp_id = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								StrSql = "SELECT jc_emp_id"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_veh_id = " + jc_veh_id + ""
										+ " ORDER BY jc_id DESC LIMIT 1";
								String jc_emp_id = ExecuteQuery(StrSql);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET veh_emp_id = " + jc_emp_id + ""
										+ " WHERE veh_id = " + jc_veh_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT emp_name"
										+ " FROM " + compdb(comp_id) + "axela_emp"
										+ " WHERE emp_id = " + value);

								history_actiontype = "Service Advisor";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Service Advisor updated!";
							} else {
								StrHTML = "<font color=\"red\">Select Service Advisor!</font>";
							}
						} else if (name.equals("dr_jc_technician_emp_id")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT emp_name"
									+ " FROM " + compdb(comp_id) + "axela_service_jc"
									+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_technician_emp_id"
									+ " WHERE jc_id = " + jc_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET jc_technician_emp_id = '" + value + "'"
									+ " WHERE jc_id = " + jc_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT emp_name"
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE emp_id = " + value);

							history_actiontype = "Technician";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
									+ " (history_jc_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + jc_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);

							UpdateCurrentStage(jc_id);
							StrHTML = "Technician updated!";
						} else if (name.equals("dr_location")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT location_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON location_id = jc_location_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET jc_location_id = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT location_name"
										+ " FROM " + compdb(comp_id) + "axela_inventory_location"
										+ " WHERE location_id = " + value);

								history_actiontype = "Inventory Location";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Inventory Location updated!";
							} else {
								StrHTML = "<font color=\"red\">Select Inventory Location!</font>";
							}
						} else if (name.equals("dr_bay")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT COALESCE(bay_name, '')"
									+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_service_jc ON jc_bay_id = bay_id"
									+ " WHERE jc_id = " + jc_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET jc_bay_id = '" + value + "'"
									+ " WHERE jc_id = " + jc_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT bay_name"
									+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
									+ " WHERE bay_id = " + value);

							history_actiontype = "Bay";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
									+ " (history_jc_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + jc_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);

							UpdateCurrentStage(jc_id);
							StrHTML = "Bay updated!";
						} else if (name.equals("dr_jc_stage_id")) {
							if (!jc_stage_id.equals("0")) {
								if (jc_stage_id.equals("4")) {
									StrSql = "SELECT check_id"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
											+ " WHERE check_type = 1"
											+ " AND check_item_id = " + veh_variant_id + ""
											+ " AND check_id NOT IN (SELECT trans_check_id"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
											+ " WHERE trans_jc_id = " + jc_id + ")";
									if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
										StrHTML = "<font color=\"red\">Inspection Checklist not checked!</font>";
									}
								} else if (jc_stage_id.equals("5") || jc_stage_id.equals("6")) {
									StrSql = "SELECT check_id FROM " + compdb(comp_id) + "axela_service_jc_check"
											+ " WHERE check_item_id = " + veh_variant_id + ""
											+ " AND check_id NOT IN (SELECT trans_check_id FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
											+ " WHERE trans_jc_id = " + jc_id + ")";
									if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
										StrHTML = "<font color=\"red\">Checklist not checked!</font>";
									}
								}

								if (jc_stage_id.equals("6")) {
									StrHTML = "<font color=\"red\">Vehicle can't be delivered through Job Card!</font>";
								}

								if (StrHTML.equals("")) {
									history_oldvalue = ExecuteQuery("SELECT jcstage_name"
											+ " FROM " + compdb(comp_id) + "axela_service_jc"
											+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_stage ON jcstage_id = jc_jcstage_id"
											+ " WHERE jc_id = " + jc_id);

									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
											+ " SET jc_jcstage_id = '" + jc_stage_id + "',"
											+ " jc_stage_trigger = 0"
											+ " WHERE jc_id = " + jc_id + "";
									updateQuery(StrSql);

									history_newvalue = ExecuteQuery("SELECT jcstage_name"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_stage"
											+ " WHERE jcstage_id = " + jc_stage_id);
									history_actiontype = "Stage";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
											+ " (history_jc_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + jc_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + history_newvalue + "')";
									updateQuery(StrSql);

									StrHTML = "<font color=\"red\">Stage updated!";

									String readytime = ExecuteQuery("SELECT jc_time_ready"
											+ " FROM " + compdb(comp_id) + "axela_service_jc"
											+ " WHERE jc_id = " + jc_id);
									if (jc_stage_id.equals("6")) {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
												+ " SET jc_time_out = '" + ToLongDate(kknow()) + "'";
										if (readytime.equals("")) {
											StrSql += ", jc_time_ready = '" + ToLongDate(kknow()) + "'";
										}
										StrSql += " WHERE jc_id = " + jc_id + "";
										updateQuery(StrSql);

										// PSFs
										StrSql = "SELECT jcpsf_jc_id"
												+ " FROM " + compdb(comp_id) + "axela_service_jc_psf"
												+ " WHERE jcpsf_jc_id = " + jc_id + "";
										if (ExecuteQuery(StrSql).equals("")) {
											String veh_iacs = "0", jc_active = "0";
											StrSql = "SELECT veh_iacs, jc_active"
													+ " FROM " + compdb(comp_id) + "axela_service_jc"
													+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = jc_veh_id"
													+ " WHERE jc_id = " + jc_id + "";
											CachedRowSet crs = processQuery(StrSql, 0);

											while (crs.next()) {
												veh_iacs = crs.getString("veh_iacs");
												jc_active = crs.getString("jc_active");
											}
											crs.close();

											StrSql = "SELECT emp_id FROM " + compdb(comp_id) + "axela_emp"
													+ " WHERE emp_active = 1";
											if (veh_iacs.equals("1")) {
												StrSql += " AND emp_service_psf_iacs = 1";
											} else {
												StrSql += " AND emp_service_psf = 1";
											}
											StrSql += " ORDER BY RAND()"
													+ " LIMIT 1";

											String jc_psf_emp_id = CNumeric(ExecuteQuery(StrSql));
											if (jc_psf_emp_id.equals("0")) {
												jc_psf_emp_id = jc_emp_id;
											}
											if (jc_active.equals("1") && !jc_psf_emp_id.equals("0")) {
												if (Long.parseLong(jc_time_in) >= Long.parseLong("20140507000000")) {
													AddPSFDaysFields(jc_id, jc_emp_id, jc_psf_emp_id, ToLongDate(kknow()), emp_id, jc_branch_id);
												}
											}
										}

										StrHTML += "<br>Delivery Time updated!"
												+ "<input type=\"hidden\" name=\"deltime\" id=\"deltime\" value=\"" + strToLongDate(ToLongDate(kknow())) + "\">";
										if (readytime.equals("")) {
											StrHTML += "<br>Ready Time updated!"
													+ "<input type=\"hidden\" name=\"comptime\" id=\"comptime\" value=\"" + strToLongDate(ToLongDate(kknow())) + "\">";
										} else {
											StrHTML += "<input type=\"hidden\" name=\"comptime\" id=\"comptime\" value=\"" + strToLongDate(readytime) + "\">";
										}

										if (comp_email_enable.equals("1")
												&& config_email_enable.equals("1")
												&& !config_admin_email.equals("")
												&& brandconfig_jc_delivered_email_enable.equals("1")) {
											if (!contact_email1.equals("")
													&& !brandconfig_jc_delivered_email_format.equals("")
													&& !brandconfig_jc_delivered_email_sub.equals("")) {
												SendEmail();
											}
										}

										if (comp_sms_enable.equals("1") && config_sms_enable.equals("1") && brandconfig_jc_delivered_sms_enable.equals("1")) {
											if (!brandconfig_jc_delivered_sms_format.equals("") && !contact_mobile1.equals("")) {
												SendSMS();
											}
										}
									} else {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
												+ " SET"
												+ " jc_time_out = '',"
												+ " jc_time_ready = ''"
												+ " WHERE jc_id = " + jc_id + "";
										updateQuery(StrSql);
										// StrHTML +=
										// "<br>Delivered Time updated!"; else {
										// delete all the PSFs for that Job Card
										StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psf"
												+ " WHERE jcpsf_jc_id = " + jc_id + "";
										updateQuery(StrSql);
									}

									if (jc_stage_id.equals("5")) {
										if (readytime.equals("")) {
											StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
													+ " SET"
													+ " jc_time_ready = '" + ToLongDate(kknow()) + "'"
													+ " WHERE jc_id = " + jc_id + "";
											updateQuery(StrSql);

											StrHTML += "<br>Ready Time updated!"
													+ "<input type=\"hidden\" name=\"comptime\" id=\"comptime\" value=\"" + strToLongDate(ToLongDate(kknow())) + "\">";
										} else {
											StrHTML += "<input type=\"hidden\" name=\"comptime\" id=\"comptime\" value=\"" + strToLongDate(readytime) + "\">";
										}
									}
									StrHTML += "</font>";
								}
							} else {
								StrHTML = "Select Stage!";
							}
						} else if (name.equals("dr_bay_id")) {
							if (!value.equals("")) {
								String baytrans_bay_id = "";
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT jc_bay_id"
										+ " FROM  " + compdb(comp_id) + "axela_service_jc"
										+ " LEFT JOIN  " + compdb(comp_id) + "axela_service_jc_bay_trans ON baytrans_bay_id = jc_bay_id"
										+ " WHERE baytrans_bay_id = " + value + ""
										+ " AND baytrans_end_time != ''";
								baytrans_bay_id = ExecuteQuery(StrSql);

								if (baytrans_bay_id.equals("")) {
									history_oldvalue = ExecuteQuery("SELECT bay_name"
											+ " FROM " + compdb(comp_id) + "axela_service_jc"
											+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_bay ON bay_id = jc_bay_id"
											+ " WHERE jc_id = " + jc_id);

									baytrans_bay_id = ExecuteQuery("SELECT jc_bay_id FROM " + compdb(comp_id) + "axela_service_jc"
											+ " WHERE jc_id = " + jc_id);

									if (!baytrans_bay_id.equals(value) && !baytrans_bay_id.equals("") && baytrans_bay_id != null) {
										StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc_bay_trans"
												+ " SET"
												+ " baytrans_end_time = '" + ToLongDate(kknow()) + "',"
												+ " bay_end_id = " + emp_id + ""
												+ " WHERE baytrans_bay_id = " + baytrans_bay_id + "";
										updateQuery(StrSql);
									}

									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
											+ " SET"
											+ " jc_bay_id = '" + value + "'"
											+ " WHERE jc_id = " + jc_id + "";
									updateQuery(StrSql);

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_bay_trans"
											+ " (baytrans_bay_id,"
											+ " baytrans_jc_id,"
											+ " baytrans_start_time,"
											+ " bay_start_id)"
											+ " VALUES"
											+ " ('" + value + "',"
											+ " '" + jc_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " " + emp_id + ")";
									updateQuery(StrSql);

									history_newvalue = ExecuteQuery("SELECT bay_name"
											+ " FROM " + compdb(comp_id) + "axela_service_jc_bay"
											+ " WHERE bay_id = " + value);

									history_actiontype = "Bay";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
											+ " (history_jc_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + jc_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + history_newvalue + "')";
									updateQuery(StrSql);

									UpdateCurrentStage(jc_id);
									StrHTML = "Bay updated!";
								} else {
									StrHTML = "<font color=\"red\">Bay is occupied!</font>";
								}
							} else {
								StrHTML = "Select Bay!";
							}
						} else if (name.equals("txt_jc_ro_no")) {
							history_oldvalue = ExecuteQuery("SELECT jc_ro_no"
									+ " FROM " + compdb(comp_id) + "axela_service_jc"
									+ " WHERE jc_id = " + jc_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
									+ " SET"
									+ " jc_ro_no = '" + jc_ro_no + "'"
									+ " WHERE jc_id = " + jc_id + "";
							updateQuery(StrSql);

							history_actiontype = "RO NO.";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
									+ " (history_jc_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + jc_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + jc_ro_no + "')";
							updateQuery(StrSql);

							UpdateCurrentStage(jc_id);
							StrHTML = "RO No. updated!";
						} else if (name.equals("dr_jc_priorityjc_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT priorityjc_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_priority ON priorityjc_id = jc_priorityjc_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET"
										+ " jc_priorityjc_id = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT priorityjc_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_priority"
										+ " WHERE priorityjc_id = " + value);
								history_actiontype = "Priority";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Priority updated!";
							} else {
								StrHTML = "<font color=\"red\">Select Priority!</font>";
							}
						} else if (name.equals("txt_jc_notes")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_notes"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET"
										+ " jc_notes = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Notes";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Notes updated!";
							} else {
								StrHTML = "Enter Notes!";
							}
						} else if (name.equals("txt_jc_customer_name")) {
							if (!value.equals("")) {
								if (value.length() < 3) {
									StrHTML = ("<font color=\"red\">Enter atleast 3 Characters for Customer Name!</font>");
									return;
								}
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT COALESCE(customer_name, '') AS customer_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " LFET JOIN " + compdb(comp_id) + "axela_customer on customer_id = jc_customer_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " INNER join " + compdb(comp_id) + "axela_service_jc on jc_customer_id = customer_id"
										+ " SET"
										+ " customer_name = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Customer";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Customer Name updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Customer Name!</font>";
							}
						} else if (name.equals("dr_title")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT title_desc"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id "
										+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id"
										+ " SET"
										+ " contact_title_id = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT title_desc"
										+ " FROM " + compdb(comp_id) + "axela_title WHERE title_id = " + value);

								history_actiontype = "Contact Title";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Title updated!";
							} else {
								StrHTML = "<font color=\"red\">Select Title!</font>";
							}
						} else if (name.equals("txt_contact_fname")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT contact_fname"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_contact_id = contact_id"
										+ " SET"
										+ " contact_fname = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact First Name";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Fist Name updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Contact Fist Name!</font>";
							}
						} else if (name.equals("txt_contact_lname")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT contact_lname"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_contact_id = contact_id"
										+ " SET"
										+ " contact_lname = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Last Name";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Last Name updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Contact Last Name!</font>";
							}
						} else if (name.equals("txt_contact_mobile1")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT contact_mobile1"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id "
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
										+ " SET"
										+ " contact_mobile1 = '" + value + "',"
										+ " customer_mobile1 = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Mobile 1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Mobile 1 updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter valid Mobile 1!</font>";
							}
						} else if (name.equals("txt_contact_mobile2")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id"
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_contact_id = contact_id"
										+ " WHERE (contact_mobile1 = '" + value + "'"
										+ " OR contact_mobile2 = '" + value + "')"
										+ " AND jc_id != " + jc_id + "";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "<font color=\"red\">Similar Mobile 2 Found!</font>";
								} else {
									history_oldvalue = ExecuteQuery("SELECT contact_mobile2"
											+ " FROM " + compdb(comp_id) + "axela_service_jc"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
											+ " WHERE jc_id = " + jc_id);

									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
											+ " SET"
											+ " contact_mobile2 = '" + value + "',"
											+ " customer_mobile2 = '" + value + "'"
											+ " WHERE jc_id = " + jc_id + "";
									updateQuery(StrSql);

									history_actiontype = "Contact Mobile 2";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
											+ " (history_jc_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + jc_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Contact Mobile 2 updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter valid Mobile 2!</font>";
							}
						} else if (name.equals("txt_contact_phone1")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT contact_phone1"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
										+ " SET"
										+ " contact_phone1 = '" + value + "',"
										+ " customer_phone1 = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Phone 1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ "(history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);

								StrHTML = "Contact Phone 1 updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter valid Contact Phone 1!</font>";
							}
						} else if (name.equals("txt_contact_phone2")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id"
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc ON jc_contact_id = contact_id"
										+ " WHERE contact_phone1 = '" + value + "'"
										+ " OR contact_phone2 = '" + value + "'"
										+ " AND jc_id != " + jc_id;
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "<font color=\"red\">Similar Phone 2 Found!</font>";
								} else {
									history_oldvalue = ExecuteQuery("SELECT contact_phone2"
											+ " FROM " + compdb(comp_id) + "axela_service_jc"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
											+ " WHERE jc_id = " + jc_id);

									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
											+ " SET"
											+ " contact_phone2 = '" + value + "',"
											+ " customer_phone2 = '" + value + "'"
											+ " WHERE jc_id = " + jc_id + "";
									updateQuery(StrSql);

									history_actiontype = "Contact Phone 2";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
											+ " (history_jc_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + jc_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									StrHTML = "Contact Phone 2 updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter valid Phone 2!</font>";
							}
						} else if (name.equals("txt_contact_email1")) {
							if (!value.equals("") && IsValidEmail(value)) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT contact_email1"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
										+ " SET"
										+ " contact_email1 = '" + value + "',"
										+ " customer_email1 = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Email 1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Email 1 updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter valid Contact Email 1!</font>";
							}
						} else if (name.equals("txt_contact_address")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT contact_address"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
										+ " SET"
										+ " contact_address = '" + value + "',"
										+ " customer_address = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Address";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Address updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter valid Contact Address!</font>";
							}
						} // else if (name.equals("txt_contact_address2")) {
							// value = value.replaceAll("nbsp", "&");
							// history_oldvalue =
							// ExecuteQuery("SELECT contact_address2"
							// + " FROM " + compdb(comp_id) + "axela_service_jc"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_customer_contact ON contact_id = jc_contact_id"
							// + " WHERE jc_id = " + jc_id);
							//
							// StrSql = "UPDATE " + compdb(comp_id) +
							// "axela_service_jc"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_customer_contact ON contact_id = jc_contact_id"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_customer ON customer_id = jc_customer_id"
							// + " SET"
							// + " contact_address2 = '" + value + "',"
							// + " customer_address2 = '" + value + "'"
							// + " WHERE jc_id = " + jc_id + "";
							// updateQuery(StrSql);
							//
							// history_actiontype = "Contact Address";
							//
							// StrSql = "INSERT INTO " + compdb(comp_id) +
							// "axela_service_jc_history"
							// + " (history_jc_id,"
							// + " history_emp_id,"
							// + " history_datetime,"
							// + " history_actiontype,"
							// + " history_oldvalue,"
							// + " history_newvalue)"
							// + " VALUES"
							// + " ('" + jc_id + "',"
							// + " '" + emp_id + "',"
							// + " '" + ToLongDate(kknow()) + "',"
							// + " '" + history_actiontype + "',"
							// + " '" + history_oldvalue + "',"
							// + " '" + value + "')";
							// updateQuery(StrSql);
							// StrHTML = "Contact Address updated!";
							// } else if (name.equals("txt_contact_address3")) {
							// value = value.replaceAll("nbsp", "&");
							// history_oldvalue =
							// ExecuteQuery("SELECT contact_address3"
							// + " FROM " + compdb(comp_id) + "axela_service_jc"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_customer_contact ON contact_id = jc_contact_id"
							// + " WHERE jc_id = " + jc_id);
							//
							// StrSql = "UPDATE " + compdb(comp_id) +
							// "axela_service_jc"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_customer_contact ON contact_id = jc_contact_id"
							// + " INNER JOIN " + compdb(comp_id) +
							// "axela_customer ON customer_id = jc_customer_id"
							// + " SET"
							// + " contact_address3 = '" + value + "',"
							// + " customer_address3 = '" + value + "'"
							// + " WHERE jc_id = " + jc_id + "";
							// updateQuery(StrSql);
							//
							// history_actiontype = "Contact Address";
							//
							// StrSql = "INSERT INTO " + compdb(comp_id) +
							// "axela_service_jc_history"
							// + " (history_jc_id,"
							// + " history_emp_id,"
							// + " history_datetime,"
							// + " history_actiontype,"
							// + " history_oldvalue,"
							// + " history_newvalue)"
							// + " VALUES"
							// + " ('" + jc_id + "',"
							// + " '" + emp_id + "',"
							// + " '" + ToLongDate(kknow()) + "',"
							// + " '" + history_actiontype + "',"
							// + " '" + history_oldvalue + "',"
							// + " '" + value + "')";
							// updateQuery(StrSql);
							//
							// StrHTML = "Contact Address updated!";
							// }
						else if (name.equals("dr_city_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT city_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
										+ " SET"
										+ " contact_city_id = '" + value + "',"
										+ " customer_city_id = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT city_name"
										+ " FROM " + compdb(comp_id) + "axela_city"
										+ " WHERE city_id = " + value);

								history_actiontype = "Contact City";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "City updated!";
							} else {
								StrHTML = "<font color=\"red\">Select City!</font>";
							}
						} else if (name.equals("txt_contact_pin")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT contact_pin"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
										+ " SET"
										+ " contact_pin = '" + value + "',"
										+ " customer_pin = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Pin";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Pin updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Contact Pin!</font>";
							}
						} else if (name.equals("txt_jc_time_promised")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");

								if (!isValidDateFormatLong(value)) {
									StrHTML = "<font color=\"red\">Enter valid Promised Time!</font>";
									return;
								} else if (!value.equals("") && Long.parseLong(jc_time_in) > Long.parseLong(ConvertLongDateToStr(value))) {
									StrHTML = "<font color=\"red\">Promised Time must be greater than the Time In!</font>";
									return;
								}

								history_oldvalue = strToLongDate(ExecuteQuery("SELECT jc_time_promised"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id));

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET"
										+ " jc_time_promised = '" + Long.parseLong(ConvertLongDateToStr(value)) + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Promised Time";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);

								StrHTML = "Promised Time updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Promised Time!</font>";
							}
						} else if (name.equals("txt_jc_time_posted")) {
							value = value.replaceAll("nbsp", "&");

							if (!value.equals("") && !isValidDateFormatLong(value)) {
								StrHTML = "<font color=\"red\">Enter valid Posted Invoice Date!</font>";
								return;
							} else if (!value.equals("") && Long.parseLong(jc_time_in) > Long.parseLong(ConvertLongDateToStr(value))) {
								StrHTML = "<font color=\"red\">Posted Invoice Date must be greater than the Time In!</font>";
								return;
							}
							history_oldvalue = strToLongDate(ExecuteQuery("SELECT jc_time_posted"
									+ " FROM " + compdb(comp_id) + "axela_service_jc"
									+ " WHERE jc_id = " + jc_id));

							if (!value.equals("")) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET"
										+ " jc_time_posted = '" + Long.parseLong(ConvertLongDateToStr(value)) + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);
							} else {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET"
										+ " jc_time_posted = ''"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);
							}

							history_actiontype = "Posted Invoice Date";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
									+ " (history_jc_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + jc_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Posted Invoice Date updated!";
						} else if (name.equals("chk_jc_critical")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_critical"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);
								if (history_oldvalue.equals("1")) {
									history_oldvalue = "1";
								} else {
									history_oldvalue = "0";
								}

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET"
										+ " jc_critical = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_newvalue = ExecuteQuery("SELECT jc_critical"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								if (history_newvalue.equals("1")) {
									history_newvalue = "1";
								} else {
									history_newvalue = "0";
								}

								history_actiontype = "Critical";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "Critical updated!";
							}
						} else if (name.contains("chk_check_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = "";
								history_newvalue = "";

								if (checked.equals("true")) {
									history_oldvalue = "UnChecked";
									history_newvalue = "Checked";
								} else {
									history_oldvalue = "Checked";
									history_newvalue = "UnChecked";
								}

								if (checked.equals("false")) {
									StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
											+ " WHERE trans_check_id = " + value + "";
									updateQuery(StrSql);
								}

								if (checked.equals("true")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_check_trans"
											+ " (trans_jc_id,"
											+ " trans_check_id,"
											+ " trans_check_entry_id,"
											+ " trans_check_entry_date)"
											+ " VALUES"
											+ " (" + jc_id + ","
											+ " " + value + ","
											+ " " + emp_id + ","
											+ " '" + ToLongDate(kknow()) + "')";
									updateQuery(StrSql);
								}

								String name = ExecuteQuery("SELECT check_name"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
										+ " WHERE check_id = " + value);
								history_actiontype = "Checklist: " + name;

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "<font color=\"red\"><b>Checklist updated!</b></font>";
							} else {
								StrHTML = "<font color=\"red\"><b>Enter Checklist!</b></font>";
							}
						} else if (name.contains("all_checklist")) {
							if (value.equals("1")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_check_trans"
										+ " (trans_jc_id,"
										+ " trans_check_id,"
										+ " trans_check_entry_id,"
										+ " trans_check_entry_date)"
										+ " SELECT " + jc_id + ","
										+ " check_id,"
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "'"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
										+ " WHERE check_item_id = " + veh_variant_id + ""
										+ " AND check_type = 1"
										+ " AND check_id NOT IN (SELECT trans_check_id"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
										+ " WHERE trans_jc_id = " + jc_id + ")";
								updateQuery(StrSql);

								history_actiontype = "Inspection Checklist all selected";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " 'All Selected')";
								updateQuery(StrSql);
								StrHTML = "<font color=\"red\"><b>Inspection Checklist updated!</b></font>";
							} else if (value.equals("2")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_check_trans"
										+ " (trans_jc_id,"
										+ " trans_check_id,"
										+ " trans_check_entry_id,"
										+ " trans_check_entry_date)"
										+ " SELECT " + jc_id + ","
										+ " check_id,"
										+ " " + emp_id + ","
										+ " '" + ToLongDate(kknow()) + "'"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_check"
										+ " WHERE check_item_id = " + veh_variant_id + ""
										+ " AND check_type = 2"
										+ " AND check_id NOT IN (SELECT trans_check_id"
										+ " FROM " + compdb(comp_id) + "axela_service_jc_check_trans"
										+ " WHERE trans_jc_id = " + jc_id + ")";
								updateQuery(StrSql);

								history_actiontype = "Washing Checklist all selected";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " 'All Selected')";
								updateQuery(StrSql);
								StrHTML = "<font color=\"red\"><b>Washing Checklist updated!</b></font>";
							}
						} else if (name.contains("chk_jc_invent_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = "";
								history_newvalue = "";

								if (checked.equals("true")) {
									history_oldvalue = chk_invent_name + " UnChecked";
									history_newvalue = chk_invent_name + " Checked";
								} else {
									history_oldvalue = chk_invent_name + " Checked";
									history_newvalue = chk_invent_name + " UnChecked";
								}

								if (checked.equals("false")) {
									StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_invent_trans"
											+ " WHERE inventtrans_invent_id = " + value + "";
									updateQuery(StrSql);
								} else if (checked.equals("true")) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_invent_trans"
											+ " (inventtrans_jc_id,"
											+ " inventtrans_invent_id)"
											+ " VALUES"
											+ " (" + jc_id + ","
											+ " " + value + ")";
									updateQuery(StrSql);
								}

								history_actiontype = "Inventory";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								StrHTML = "<font color=\"red\"><b>Inventory updated!</b></font>";
							} else {
								StrHTML = "<font color=\"red\"><b>Select Inventory!</b></font>";
							}
						} else if (name.equals("txt_jc_inventory")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = ExecuteQuery("SELECT jc_inventory"
										+ " FROM " + compdb(comp_id) + "axela_service_jc"
										+ " WHERE jc_id = " + jc_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
										+ " SET"
										+ " jc_inventory = '" + value + "'"
										+ " WHERE jc_id = " + jc_id + "";
								updateQuery(StrSql);

								history_actiontype = "Other Inventory";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
										+ " (history_jc_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + jc_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "<font color=\"red\"><b>Inventory updated!</b></font>";
							} else {
								StrHTML = "<font color=\"red\"><b>Enter Inventory!</b></font>";
							}
						} else if (name.contains("bill")) {
							billdashcheck(name);
						}

					} else {
						StrHTML = "Update Permission Denied!";
					}
				} catch (Exception ex) {
					SOPError("Axelaauto===" + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}
	public void UpdateCurrentStage(String jc_id) {
		try {
			String jc_stage_id = "0";
			StrSql = "SELECT jc_jcstage_id, jc_ro_no, jc_technician_emp_id,"
					+ " jc_bay_id, jc_time_ready, jc_time_out"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				if (!crs.getString("jc_ro_no").equals("")) {
					jc_stage_id = "1";
				}

				if (!crs.getString("jc_technician_emp_id").equals("0") && !crs.getString("jc_bay_id").equals("0")) {
					jc_stage_id = "2";
				}

				if (!crs.getString("jc_time_ready").equals("")) {
					jc_stage_id = "5";
				}

				if (!crs.getString("jc_time_out").equals("")) {
					jc_stage_id = "6";
				}

				if (!crs.getString("jc_jcstage_id").equals(jc_stage_id)) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET"
							+ " jc_jcstage_id = " + jc_stage_id + ","
							+ " jc_stage_trigger = 0"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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
		g1.dispose();
		g.dispose();
		return jcfg;
	}

	protected void SendEmail() {
		emailsub = brandconfig_jc_delivered_email_sub;
		emailmsg = brandconfig_jc_delivered_email_format;

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
		emailmsg = "REPLACE(" + emailmsg + ", '[BRANCHADDRESS]', branch_add)";

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
				+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_entry_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
				+ " WHERE jc_id = " + jc_id + "";

		StrSql = "INSERT INTO " + compdb(comp_id) + "axela_email"
				+ " ("
				+ "	email_branch_id,"
				+ "	email_contact_id,"
				+ " email_contact,"
				+ " email_FROM,"
				+ " email_to,"
				+ " email_subject,"
				+ " email_msg,"
				+ " email_date,"
				+ " email_entry_id,"
				+ " email_sent)"
				+ " " + StrSql + "";
		updateQuery(StrSql);
	}

	protected void SendSMS() {
		smsmsg = brandconfig_jc_delivered_sms_format;

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
		smsmsg = "REPLACE(" + smsmsg + ", '[BRANCHADDRESS]', branch_add)";

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
				+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = jc_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = jc_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = jc_entry_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_jobtitle ON jobtitle_id = emp_jobtitle_id"
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
		updateQuery(StrSql);
	}

	protected void PopulateContactDetails() {
		try {
			StrSql = "SELECT contact_fname, contact_lname, contact_email1,"
					+ " contact_email2, contact_mobile1"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					// + " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " WHERE contact_id = " + jc_contact_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				contact_email1 = crs.getString("contact_email1");
				contact_email2 = crs.getString("contact_email2");
				contact_mobile1 = crs.getString("contact_mobile1");
				contact_fname = crs.getString("contact_fname");
				contact_lname = crs.getString("contact_lname");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateConfigDetails() {
		try {
			StrSql = "SELECT COALESCE(brandconfig_jc_delivered_email_enable, '') AS brandconfig_jc_delivered_email_enable,"
					+ " COALESCE(brandconfig_jc_delivered_sms_format, '') AS brandconfig_jc_delivered_sms_format,"
					+ " COALESCE(brandconfig_jc_delivered_sms_enable, '') AS brandconfig_jc_delivered_sms_enable,"
					+ " COALESCE(brandconfig_jc_delivered_email_sub, '') AS brandconfig_jc_delivered_email_sub, comp_sms_enable,"
					+ " COALESCE(brandconfig_jc_delivered_email_format, '') AS brandconfig_jc_delivered_email_format,"
					+ " config_admin_email, config_email_enable, config_sms_enable, comp_email_enable"
					+ " FROM " + compdb(comp_id) + "axela_config, " + compdb(comp_id) + "axela_comp"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_branch ON branch_id = " + jc_branch_id + ""
					+ "	INNER JOIN " + compdb(comp_id) + "axela_brand_config ON brandconfig_brand_id = branch_brand_id";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				brandconfig_jc_delivered_email_enable = crs.getString("brandconfig_jc_delivered_email_enable");
				brandconfig_jc_delivered_email_format = crs.getString("brandconfig_jc_delivered_email_format");
				brandconfig_jc_delivered_sms_enable = crs.getString("brandconfig_jc_delivered_sms_enable");
				brandconfig_jc_delivered_sms_format = crs.getString("brandconfig_jc_delivered_sms_format");
				brandconfig_jc_delivered_email_sub = crs.getString("brandconfig_jc_delivered_email_sub");
				config_admin_email = crs.getString("config_admin_email");
				config_email_enable = crs.getString("config_email_enable");
				config_sms_enable = crs.getString("config_sms_enable");
				comp_email_enable = crs.getString("comp_email_enable");
				comp_sms_enable = crs.getString("comp_sms_enable");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void AddPSFDaysFields(String jc_id, String jc_emp_id, String jc_psf_emp_id, String jc_time_out, String emp_id, String branch_id) {
		// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_jc_psf"
		// + " WHERE jcpsf_jc_id = " + jc_id + "";
		// updateQuery(StrSql);

		StrSql = "SELECT " + jc_id + ","
				// + " IF(jcpsfdays_exe_type = 1, " + jc_emp_id + ", " +
				// jc_psf_emp_id + "),"
				+ " psfdays_emp_id,"
				+ " DATE_FORMAT(DATE_ADD(CONCAT(SUBSTR('" + jc_time_out + "', 1, 8), '100000'), INTERVAL (jcpsfdays_daycount-0) DAY), '%Y%m%d%H%i%s'),"
				+ " jcpsfdays_id,"
				+ " " + emp_id + ","
				+ " '" + ToLongDate(kknow()) + "'"
				+ " FROM " + compdb(comp_id) + "axela_service_jc_psfdays"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = psfdays_brand_id"
				+ " WHERE branch_id = " + branch_id + ""
				+ " AND " + jc_id + " NOT IN (SELECT jcpsf_jc_id FROM " + compdb(comp_id) + "axela_service_jc_psf)";

		StrSql = " INSERT INTO " + compdb(comp_id) + "axela_service_jc_psf"
				+ " (jcpsf_jc_id,"
				+ " jcpsf_emp_id,"
				+ " jcpsf_followup_time,"
				+ " jcpsf_jcpsfdays_id,"
				+ " jcpsf_entry_id,"
				+ " jcpsf_entry_time)"
				+ StrSql;
		// SOP("StrSql = " + StrSql);
		updateQuery(StrSql);
	}
	public void billdashcheck(String name) {
		SOP("name=bill==" + name);
		if (name.equals("txt_jc_bill_cash_customername")) {// Cash Bill
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_customername"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_customername = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_customername = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Customer Name";
			if (value.equals("")) {
				value = "";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " (" + jc_id + ","
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Customer Name Updated!";
		} else if (name.equals("txt_jc_bill_cash_date")) {
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = strToShortDate(ExecuteQuery("SELECT jc_bill_cash_date"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_id = " + jc_id));
				if (!value.equals("") && !isValidDateFormatLong(value)) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_cash_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				} else {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_cash_date = ''"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				}

				history_actiontype = "Cash Bill Date";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
						+ " (history_jc_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + jc_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Cash Bill Date updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Bill Date!</font>";
			}
		} else if (name.equals("txt_jc_bill_cash_no")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_no"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_no = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_no = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill No";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill No updated!";
		}
		// Bill Cash Parts
		else if (name.equals("txt_jc_bill_cash_parts")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_tyre_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_tyre_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_tyre_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_tyre_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Cash Bill Parts Tyre Qty";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Bill Tyre Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_tyre")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_tyre"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_tyre = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_tyre = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Cash Bill Parts Tyre";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Tyre Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_oil")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_oil"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_oil = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_oil = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Cash Bill Parts Oil";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Oil Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_accessories")) {
			value = value.replaceAll("nbsp", "&");
			// SOP("value===" + value);
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_accessories"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			// SOP("history_oldvalue=" + history_oldvalue);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_accessories = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_accessories = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Accessories";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Accessories Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_labour")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_labour"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET"
						+ " jc_bill_cash_parts_labour = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_labour = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Updated!";
		}

		else if (name.equals("txt_jc_bill_cash_parts_valueadd")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_valueadd"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_valueadd = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_valueadd = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Value Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Value Added Service Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_battery")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_battery"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_battery = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_battery = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Battery Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Battery Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_battery_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_battery_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_battery_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_battery_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Battery  Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Battery Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_brake_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_brake_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_brake_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_brake_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Brake Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Brake Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_brake")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_brake"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_brake = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_brake = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Brake Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Brake Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_extwarranty_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_extwarranty_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_extwarranty_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_extwarranty_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Extended Warranty Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Extended Warranty Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_extwarranty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_extwarranty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_extwarranty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_extwarranty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Extended Warranty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Extended Warranty Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_wheelalign")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_wheelalign"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_wheelalign = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_wheelalign = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Wheel-Alignment Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Wheel-Alignment Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_cng")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_cng"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_cng = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_cng = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts CNG Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts CNG Updated!";
		} else if (name.equals("txt_jc_bill_cash_parts_discamt")) {
			// SOP("coming");
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_parts_discamt"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_discamt = " + value + ","
						+ "	jc_discamt = jc_bill_cash_parts_discamt + jc_bill_cash_parts_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_parts_discamt = 0,"
						+ "	jc_discamt = jc_bill_cash_parts_discamt + jc_bill_cash_parts_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Parts Discount Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Parts Discount Updated!";
		}
		// Bill Cash Labour
		else if (name.equals("txt_jc_bill_cash_labour")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_tyre_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_tyre_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_tyre_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_tyre_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Cash Bill Labour Tyre Qty";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Tyre Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_tyre")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_tyre"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_tyre = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_tyre = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Cash Bill Labour Tyre";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Tyre Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_oil")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_oil"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_oil = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_oil = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Cash Bill Labour Oil";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Oil Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_accessories")) {
			value = value.replaceAll("nbsp", "&");
			// SOP("value===" + value);
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_accessories"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			// SOP("history_oldvalue=" + history_oldvalue);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_accessories = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_accessories = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Accessories";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Accessories Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_labour")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_labour"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET"
						+ " jc_bill_cash_labour_labour = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_labour = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Updated!";
		}

		else if (name.equals("txt_jc_bill_cash_labour_valueadd")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_valueadd"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_valueadd = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_valueadd = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Value Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Value Added Service Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_battery")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_battery"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_battery = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_battery = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Battery Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Battery Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_battery_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_battery_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_battery_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_battery_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Battery  Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Battery Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_brake_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_brake_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_brake_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_brake_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Brake Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Brake Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_brake")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_brake"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_brake = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_brake = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Brake Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Brake Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_extwarranty_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_extwarranty_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_extwarranty_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_extwarranty_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Extended Warranty Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Extended Warranty Qty Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_extwarranty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_extwarranty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_extwarranty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_extwarranty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Extended Warranty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Extended Warranty Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_wheelalign")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_wheelalign"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_wheelalign = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_wheelalign = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Wheel-Alignment Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Wheel-Alignment Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_cng")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_cng"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_cng = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_cng = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour CNG Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour CNG Updated!";
		} else if (name.equals("txt_jc_bill_cash_labour_discamt")) {
			// SOP("coming");
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_cash_labour_discamt"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_discamt = " + value + ","
						+ "	jc_discamt = jc_bill_cash_labour_discamt + jc_bill_cash_labour_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_cash_labour_discamt = 0,"
						+ "	jc_discamt = jc_bill_cash_labour_discamt + jc_bill_cash_labour_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Cash Bill Labour Discount Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Cash Bill Labour Discount Updated!";
		}

		// Bill Insurance

		else if (name.equals("txt_jc_bill_insur_customername")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_customername"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_customername = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_customername = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Customer Name";
			if (value.equals("")) {
				value = "";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " (" + jc_id + ","
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Customer Name Updated!";
		} else if (name.equals("txt_jc_bill_insur_date")) { // Insurance Bill
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = strToShortDate(ExecuteQuery("SELECT jc_bill_insur_date"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_id = " + jc_id));
				if (!value.equals("") && !isValidDateFormatLong(value)) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_insur_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				} else {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_insur_date = ''"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				}

				history_actiontype = "Insurance Bill Date";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
						+ " (history_jc_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + jc_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Insurance Bill Date updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Insurance Bill Date!</font>";
			}
		} else if (name.equals("txt_jc_bill_insur_no")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_no"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_no = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_no = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill No";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill No updated!";
		}
		// Bill Insurance Parts
		else if (name.equals("txt_jc_bill_insur_parts")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_tyre_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_tyre_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_tyre_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_tyre_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Insurance Bill Parts Tyre Qty";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Tyre Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_tyre")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_tyre"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_tyre = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_tyre = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Insurance Bill Parts Tyre";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Tyre Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_oil")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_oil"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_oil = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_oil = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Insurance Insurance Bill Parts Oil";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Oil Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_accessories")) {
			value = value.replaceAll("nbsp", "&");
			// SOP("value===" + value);
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_accessories"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			// SOP("history_oldvalue=" + history_oldvalue);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_accessories = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_accessories = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Accessories";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Accessories Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_valueadd")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_valueadd"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_valueadd = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_valueadd = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Value Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Value Added Service Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_battery")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_battery"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_battery = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_battery = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Battery Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Battery Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_battery_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_battery_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_battery_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_battery_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Battery  Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Battery Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_brake_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_brake_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_brake_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_brake_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Brake Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Brake Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_brake")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_brake"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_brake = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_brake = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Brake Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Brake Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_extwarranty_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_extwarranty_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_extwarranty_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_extwarranty_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Extended Warranty Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Extended Warranty Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_extwarranty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_extwarranty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_extwarranty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_extwarranty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Extended Warranty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Extended Warranty Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_wheelalign")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_wheelalign"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_wheelalign = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_wheelalign = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Wheel-Alignment Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Wheel-Alignment Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_cng")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_cng"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_cng = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_cng = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts CNG Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts CNG Updated!";
		} else if (name.equals("txt_jc_bill_insur_parts_discamt")) {
			// SOP("coming");
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_parts_discamt"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_discamt = " + value + ","
						+ "	jc_discamt = jc_bill_insur_parts_discamt + jc_bill_insur_parts_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);

			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_parts_discamt = 0,"
						+ "	jc_discamt = jc_bill_insur_parts_discamt + jc_bill_insur_parts_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Parts Discount Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Parts Discount Updated!";
		}

		// Bill Insurance Labour
		else if (name.equals("txt_jc_bill_insur_labour")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_tyre_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_tyre_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_tyre_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_tyre_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Insurance Bill Labour Tyre Qty";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Tyre Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_tyre")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_tyre"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_tyre = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_tyre = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Insurance Bill Labour Tyre";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Tyre Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_oil")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_oil"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_oil = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_oil = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Insurance Insurance Bill Labour Oil";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Oil Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_accessories")) {
			value = value.replaceAll("nbsp", "&");
			// SOP("value===" + value);
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_accessories"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			// SOP("history_oldvalue=" + history_oldvalue);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_accessories = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_accessories = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Accessories";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Accessories Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_valueadd")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_valueadd"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_valueadd = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_valueadd = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Value Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Value Added Service Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_battery")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_battery"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_battery = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_battery = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Battery Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Battery Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_battery_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_battery_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_battery_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_battery_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Battery  Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Battery Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_brake_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_brake_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_brake_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_brake_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Brake Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Brake Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_brake")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_brake"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_brake = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_brake = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Brake Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Brake Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_extwarranty_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_extwarranty_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_extwarranty_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_extwarranty_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Extended Warranty Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Extended Warranty Qty Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_extwarranty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_extwarranty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_extwarranty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_extwarranty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Extended Warranty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Extended Warranty Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_wheelalign")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_wheelalign"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_wheelalign = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_wheelalign = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Wheel-Alignment Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Wheel-Alignment Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_cng")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_cng"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_cng = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_cng = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour CNG Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour CNG Updated!";
		} else if (name.equals("txt_jc_bill_insur_labour_discamt")) {
			// SOP("coming");
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_insur_labour_discamt"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_discamt = " + value + ","
						+ "	jc_discamt = jc_bill_insur_labour_discamt + jc_bill_insur_labour_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);

			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_insur_labour_discamt = 0,"
						+ "	jc_discamt = jc_bill_insur_labour_discamt + jc_bill_insur_labour_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Insurance Bill Labour Discount Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Insurance Bill Labour Discount Updated!";
		}

		// FOC
		else if (name.equals("txt_jc_bill_foc_customername")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_foc_customername"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_foc_customername = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_foc_customername = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "FOC Customer Name";
			if (value.equals("")) {
				value = "";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " (" + jc_id + ","
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "FOC Customer Name Updated!";
		} else if (name.equals("txt_jc_bill_foc_date")) { // FOC Bill
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = strToShortDate(ExecuteQuery("SELECT jc_bill_foc_date"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_id = " + jc_id));
				if (!value.equals("") && !isValidDateFormatLong(value)) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_foc_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				} else {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_foc_date = ''"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				}

				history_actiontype = "FOC Bill Date";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
						+ " (history_jc_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + jc_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "FOC Bill Date updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter FOC Bill Date!</font>";
			}
		} else if (name.equals("txt_jc_bill_foc_no")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_foc_no"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_foc_no = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_foc_no = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "FOC Bill No";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "FOC Bill No updated!";
		}

		// Warranty
		else if (name.equals("txt_jc_bill_warranty_customername")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_customername"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_customername = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_customername = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Customer Name";
			if (value.equals("")) {
				value = "";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " (" + jc_id + ","
					+ " " + emp_id + ","
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Customer Name Updated!";
		} else if (name.equals("txt_jc_bill_warranty_date")) { // Warranty Bill
			value = value.replaceAll("nbsp", "&");
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				history_oldvalue = strToShortDate(ExecuteQuery("SELECT jc_bill_warranty_date"
						+ " FROM " + compdb(comp_id) + "axela_service_jc"
						+ " WHERE jc_id = " + jc_id));
				if (!value.equals("") && !isValidDateFormatLong(value)) {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_warranty_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				} else {
					StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
							+ " SET jc_bill_warranty_date = ''"
							+ " WHERE jc_id = " + jc_id + "";
					updateQuery(StrSql);
				}

				history_actiontype = "Warranty Bill Date";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
						+ " (history_jc_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES"
						+ " ('" + jc_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Warranty Bill Date updated!";
			} else {
				StrHTML = "<font color=\"red\">Enter Warranty Bill Date!</font>";
			}
		} else if (name.equals("txt_jc_bill_warranty_no")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_no"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_no = '" + value + "'"
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_no = ''"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill No.";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill No updated!";
		}
		// Bill Warranty Parts
		else if (name.equals("txt_jc_bill_warranty_parts")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_tyre_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_tyre_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_tyre_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_tyre_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Warranty Bill Parts Tyre Qty";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Bill Tyre Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_tyre")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_tyre"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_tyre = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_tyre = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Warranty Bill Parts Tyre";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Tyre Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_oil")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_oil"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_oil = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_oil = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Warranty Bill Parts Oil";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Oil Updated!";
		}
		else if (name.equals("txt_jc_bill_warranty_parts_accessories")) {
			value = value.replaceAll("nbsp", "&");
			// SOP("value===" + value);
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_accessories"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			// SOP("history_oldvalue=" + history_oldvalue);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_accessories = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_accessories = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Accessories";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Accessories Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_labour")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_labour"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET"
						+ " jc_bill_warranty_parts_labour = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_labour = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_valueadd")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_valueadd"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_valueadd = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_valueadd = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Value Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Value Added Service Updated!";
		}
		else if (name.equals("txt_jc_bill_warranty_parts_battery")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_battery"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_battery = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_battery = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Battery Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Battery Updated!";
		}
		else if (name.equals("txt_jc_bill_warranty_parts_battery_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_battery_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_battery_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_battery_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Battery  Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Battery Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_brake_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_brake_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_brake_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_brake_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Brake Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Brake Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_brake")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_brake"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_brake = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_brake = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Brake Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Brake Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_extwarranty_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_extwarranty_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_extwarranty_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_extwarranty_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Extended Warranty Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Extended Warranty Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_extwarranty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_extwarranty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_extwarranty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_extwarranty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Extended Warranty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Extended Warranty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_wheelalign")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_wheelalign"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_wheelalign = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_wheelalign = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Wheel-Alignment Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Wheel-Alignment Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_cng")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_cng"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_cng = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_cng = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts CNG Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts CNG Updated!";
		} else if (name.equals("txt_jc_bill_warranty_parts_discamt")) {
			// SOP("coming");
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_parts_discamt"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_discamt = " + value + ","
						+ "	jc_discamt = jc_bill_warranty_parts_discamt + jc_bill_warranty_parts_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_parts_discamt = 0,"
						+ "	jc_discamt = jc_bill_warranty_parts_discamt + jc_bill_warranty_parts_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Parts Discount Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Parts Discount Updated!";
		}
		// Bill Warranty Labour
		else if (name.equals("txt_jc_bill_warranty_labour")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_tyre_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_tyre_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_tyre_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_tyre_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Warranty Bill Labour Tyre Qty";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Tyre Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_tyre")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_tyre"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_tyre = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_tyre = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Warranty Bill Labour Tyre";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Tyre Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_oil")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_oil"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_oil = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_oil = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}

			history_actiontype = "Warranty Bill Labour Oil";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Oil Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_accessories")) {
			value = value.replaceAll("nbsp", "&");
			// SOP("value===" + value);
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_accessories"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			// SOP("history_oldvalue=" + history_oldvalue);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_accessories = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				// SOP("StrSql==" + StrSql);
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_accessories = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Accessories";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Accessories Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_labour")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_labour"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET"
						+ " jc_bill_warranty_labour_labour = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_labour = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_valueadd")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_valueadd"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_valueadd = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_valueadd = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Value Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Value Added Service Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_battery")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_battery"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_battery = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_battery = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Battery Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Battery Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_battery_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_battery_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_battery_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_battery_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Battery  Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Battery Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_brake_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_brake_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_brake_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_brake_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Brake Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Brake Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_brake")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_brake"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_brake = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_brake = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Brake Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Brake Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_extwarranty_qty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_extwarranty_qty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_extwarranty_qty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_extwarranty_qty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Extended Warranty Qty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Extended Warranty Qty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_extwarranty")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_extwarranty"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_extwarranty = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_extwarranty = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Extended Warranty Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Extended Warranty Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_wheelalign")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_wheelalign"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_wheelalign = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_wheelalign = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Wheel-Alignment Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Wheel-Alignment Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_cng")) {
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_cng"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_cng = " + value + ""
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			} else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_cng = 0"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour CNG Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);

			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour CNG Updated!";
		} else if (name.equals("txt_jc_bill_warranty_labour_discamt")) {
			// SOP("coming");
			value = value.replaceAll("nbsp", "&");
			history_oldvalue = ExecuteQuery("SELECT jc_bill_warranty_labour_discamt"
					+ " FROM " + compdb(comp_id) + "axela_service_jc"
					+ " WHERE jc_id = " + jc_id);
			if (!value.equals("")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_discamt = " + value + ","
						+ "	jc_discamt = jc_bill_warranty_labour_discamt + jc_bill_warranty_labour_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			else {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_jc"
						+ " SET jc_bill_warranty_labour_discamt = 0,"
						+ "	jc_discamt = jc_bill_warranty_labour_discamt + jc_bill_warranty_labour_discamt"
						+ " WHERE jc_id = " + jc_id + "";
				updateQuery(StrSql);
			}
			history_actiontype = "Warranty Bill Labour Discount Added";
			if (value.equals("")) {
				value = "0";
			}
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_history"
					+ " (history_jc_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES"
					+ " ('" + jc_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " " + history_oldvalue + ","
					+ " '" + value + "')";
			updateQuery(StrSql);
			UpdateCurrentStage(jc_id);
			StrHTML = "Warranty Bill Labour Discount Updated!";
		}

	}

}
