/* Created Method Ved Prakash (23rd July 2013) */
package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.sales.Enquiry_Dash_Customer;
import cloudify.connect.Connect;

public class Vehicle_Dash_Check extends Connect {

	public String vehicleinfo = "";
	public int recperpage = 0;
	public String veh_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String customer_id = "0";
	public String StrHTML = "";
	public String item_model_id = "0";
	public String StrSql = "";
	public String list_model_item = "";
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
	public String veh_emp_id = "0";
	public String contact_email1 = "";
	public String contact_mobile1 = "";
	public String contact_fname = "";
	public String contact_lname = "";
	public String emailmsg = "";
	public String emailsub = "";
	public String smsmsg = "";
	public String stock_status = "";
	public String item_name = "";
	public String StrSearch = "";
	public String branch_id = "0", veh_branch_id = "0";
	public String location = "";
	public String location_id = "0";
	public String option_id = "0";
	public String customer_details = "";
	public String ownership_details = "";
	public String followup_details = "";
	public String booking_details = "";
	public String jobcard_details = "";
	public String psf_details = "";
	public String invoice_details = "";
	public String receipt_details = "";
	public String ticket_details = "";
	public String insurance_details = "";
	public String history = "", model = "";
	public String customer_edit_perm = "0";
	public String display_customer_details = "";
	public String doc_details = "";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
			customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
			vehicleinfo = PadQuotes(request.getParameter("vehicleinfo"));
			display_customer_details = PadQuotes(request.getParameter("display_customer_details"));
			list_model_item = PadQuotes(request.getParameter("list_model_item"));
			item_model_id = CNumeric(PadQuotes(request.getParameter("item_model_id")));
			/* For Stock Status */
			branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
			item_name = PadQuotes(request.getParameter("item_name"));
			location = PadQuotes(request.getParameter("location"));
			location_id = CNumeric(PadQuotes(request.getParameter("location_id")));
			stock_status = PadQuotes(request.getParameter("stock_status"));
			/* ........... */
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			checked = PadQuotes(request.getParameter("checked"));
			customer_details = PadQuotes(request.getParameter("customer_details"));
			ownership_details = PadQuotes(request.getParameter("ownership_details"));
			followup_details = PadQuotes(request.getParameter("followup_details"));
			booking_details = PadQuotes(request.getParameter("booking_details"));
			jobcard_details = PadQuotes(request.getParameter("jobcard_details"));
			invoice_details = PadQuotes(request.getParameter("invoice_details"));
			receipt_details = PadQuotes(request.getParameter("receipt_details"));
			ticket_details = PadQuotes(request.getParameter("ticket_details"));
			insurance_details = PadQuotes(request.getParameter("insurance_details"));
			history = PadQuotes(request.getParameter("history"));
			chk_invent_name = PadQuotes(request.getParameter("chk_invent_name"));
			veh_branch_id = CNumeric(PadQuotes(request.getParameter("veh_branch_id")));
			model = PadQuotes(request.getParameter("model"));
			doc_details = PadQuotes(request.getParameter("doc_details"));
			psf_details = PadQuotes(request.getParameter("psf_details"));
			Vehicle_Dash veh_dash = new Vehicle_Dash();

			if (vehicleinfo.equals("yes")) {
				StrHTML = veh_dash.VehicleInfo(comp_id, veh_id);
			} else if (customer_details.equals("yes")) {
				StrHTML = new Enquiry_Dash_Customer().CustomerDetails(response, customer_id, "yes", comp_id);
			} else if (ownership_details.equals("yes")) {
				StrHTML = veh_dash.ListOwnershipData(comp_id, veh_id);
			} else if (followup_details.equals("yes")) {
				StrHTML = veh_dash.ListServiceFollowup(comp_id, veh_id);
			} else if (booking_details.equals("yes")) {
				StrHTML = veh_dash.ListBookingdata(comp_id, veh_id);
				// SOP("StrHTML===" + StrHTML);
			} else if (jobcard_details.equals("yes")) {
				StrHTML = veh_dash.ListJobCardData(comp_id, veh_id);
			} else if (invoice_details.equals("yes")) {
				// StrHTML = veh_dash.ListInvoiceData(comp_id, veh_id);
			} else if (receipt_details.equals("yes")) {
				// StrHTML = veh_dash.ListReceiptData(comp_id, veh_id);
				// StrHTML = "<div>No data found</div>";
			} else if (doc_details.equals("yes")) {
				StrHTML = veh_dash.ListDocs(veh_id, "1", "", recperpage, comp_id);
			} else if (ticket_details.equals("yes")) {
				StrHTML = veh_dash.ListTicketData(comp_id, veh_id);
			} else if (insurance_details.equals("yes")) {
				StrHTML = veh_dash.ListInsurance(comp_id, veh_id);
			} else if (history.equals("yes")) {
				StrHTML = veh_dash.ListHistory(comp_id, veh_id);
			} else if (location.equals("yes")) {
				StrHTML = veh_dash.PopulateLocation(branch_id);
			} else if (display_customer_details.equals("yes")) {
				customer_edit_perm = ReturnPerm(comp_id, "emp_customer_edit", request);
				StrHTML = veh_dash.CustomerDetails(customer_id, customer_edit_perm, comp_id);
			} else if (psf_details.equals("yes") && !veh_id.equals("0")) {
				StrHTML = veh_dash.ListPSFFollowup(veh_id, comp_id);
			}

			if (stock_status.equals("yes") && !item_name.equals("")) {
				if (location_id.equals("0")) {
					StrHTML = "<br><br><br><br><font color=\"red\"><b>Select Location!</b></font>";
				} else {
					StrSearch += " AND location_id = " + location_id + ""
							+ " AND item_name LIKE '%" + item_name + "%'";
					StrHTML = StockOnHandDetails();
				}
			}

			try {
				if (ReturnPerm(comp_id, "emp_service_vehicle_add, emp_insurance_vehicle_add", request).equals("1")) {
					if (name.equals("dr_item_model_id")) {
						if (!value.equals("0")) {
							value = value.replaceAll("nbsp", "&");
							StrHTML = "<font color=\"red\">Select Variant!</font>";
						}
						// else {
						// StrHTML = "<font color=\"red\">Select Model!</font>";
						// }
					} else if (name.equals("servicepreownedvariant")) {
						if (!value.equals("0")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT variant_name FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN axelaauto.axela_preowned_variant ON variant_id = veh_variant_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									+ " veh_variant_id = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT variant_name FROM axelaauto.axela_preowned_variant" + " WHERE variant_id = " + value);

							history_actiontype = "Variant";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Item updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Variant!</font>";
						}
					} else if (name.equals("dr_exterior_id")) {
						if (!value.equals("0")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT option_name "
									+ " FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
									+ " WHERE option_optiontype_id = 1 and vehtrans_veh_id = " + veh_id + " ");

							StrSql = "SELECT vehtrans_option_id FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
									+ " WHERE option_optiontype_id = 1"
									+ " AND vehtrans_veh_id = " + veh_id + "";
							option_id = CNumeric(ExecuteQuery(StrSql));

							StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " WHERE vehtrans_option_id = " + option_id + "";
							updateQuery(StrSql);

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " (vehtrans_option_id,"
									+ " vehtrans_veh_id)"
									+ " VALUES"
									+ " (" + value + ","
									+ " " + veh_id + ")";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT option_name"
									+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " WHERE option_id = " + value);

							history_actiontype = "Exterior";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Exterior updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Exterior!</font>";
						}
					} else if (name.equals("dr_interior_id")) {
						if (!value.equals("0")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT option_name"
									+ " FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
									+ " WHERE option_optiontype_id = 2"
									+ " AND vehtrans_veh_id = " + veh_id);

							StrSql = "SELECT vehtrans_option_id FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = vehtrans_option_id"
									+ " WHERE option_optiontype_id = 2"
									+ " AND vehtrans_veh_id = " + veh_id + "";
							option_id = CNumeric(ExecuteQuery(StrSql));

							StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " WHERE vehtrans_option_id = " + option_id + "";
							updateQuery(StrSql);

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
									+ " (vehtrans_option_id,"
									+ " vehtrans_veh_id)"
									+ " VALUES"
									+ " (" + value + ","
									+ " " + veh_id + ")";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT option_name FROM " + compdb(comp_id) + "axela_vehstock_option"
									+ " WHERE option_id = " + value);

							history_actiontype = "Interior";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Interior updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Interior!</font>";
						}
					} else if (name.equals("txt_veh_modelyear")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT veh_modelyear"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									+ " veh_modelyear = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							// SOP("StrSql==" + StrSql);
							updateQuery(StrSql);
							history_actiontype = "MODEL YEAR";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);

							StrHTML = "Model Year updated!";
						} else {
							StrHTML = "<font color=\"red\">Enter Model Year!</font>";
						}
					} else if (name.equals("txt_veh_chassis_no")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT veh_chassis_no FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_chassis_no = '" + value + "'"
									+ " AND veh_id != " + veh_id + "";
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "<font color=\"red\">Similar Chassis No. found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT veh_chassis_no FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET"
										+ " veh_chassis_no = '" + value + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);
								history_actiontype = "Chassis Number";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Chassis No. updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter Chassis No.!</font>";
						}
					} else if (name.equals("txt_veh_engine_no")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT veh_engine_no FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_engine_no = '" + value + "'"
									+ " AND veh_id != " + veh_id + "";
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "<font color=\"red\">Similar Engine No. Found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT veh_engine_no FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET"
										+ " veh_engine_no = '" + value + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);
								history_actiontype = "Engine No.";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Engine No. updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter Engine No.!</font>";
						}
					} else if (name.equals("txt_veh_fastag")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT veh_fastag FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_fastag = '" + value + "'"
									+ " AND veh_id != " + veh_id + "";
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "<font color=\"red\">Similar FASTag ID Found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT veh_fastag FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET"
										+ " veh_fastag = '" + value + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);
								history_actiontype = "FASTag ID";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "FASTag ID updated!";
							}
						}
						// else {
						// StrHTML = "<font color=\"red\">Enter Engine No.!</font>";
						// }
					} else if (name.equals("txt_veh_reg_no")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							value = value.replaceAll(" ", "");
							value = value.replaceAll("-", "");
							value = value.replaceAll("[^a-zA-Z0-9]+", "");
							StrSql = "SELECT veh_id FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_reg_no = '" + value + "'"
									+ " AND veh_id != " + veh_id + "";
							// SOP("StrSql==" + StrSql);
							if (!IsValidRegNo(value)) {
								StrHTML = "<font color=\"red\">Enter valid Reg. No.!</font>";
							} else if (!CNumeric(ExecuteQuery(StrSql)).equals("0")) {
								StrHTML = "<font color=\"red\">Similar Reg. No. Found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT veh_reg_no FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET"
										+ " veh_reg_no = '" + value.toUpperCase().replaceAll(" ", "") + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);

								// To Update The jobcard Vehicle Details
								new Vehicle_Update().UpdateJobCardVehicle(veh_id, value.replaceAll(" ", ""));

								history_actiontype = "Reg. No.";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
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
					} else if (name.equals("txt_veh_sale_date")) {
						if (!value.equals("")) {
							if (isValidDateFormatShort(value)) {
								value = value.replaceAll("nbsp", "&");
								history_oldvalue = strToShortDate(ExecuteQuery("SELECT veh_sale_date"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id));

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET"
										+ " veh_sale_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);

								history_actiontype = "Sale Date";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Sale Date updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter valid Sale Date!</font>";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter Sale Date!</font>";
						}

					} else if (name.equals("txt_veh_sale_amount")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT veh_sale_amount FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									+ " veh_sale_amount = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
							history_actiontype = "Sale Amount";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Sale Amount updated!";

						}
					} else if (name.equals("dr_veh_vehsource_id")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT vehsource_name FROM " + compdb(comp_id) + "axela_service_veh"
								+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh_source ON vehsource_id = veh_vehsource_id"
								+ " WHERE veh_id = " + veh_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
								+ " SET"
								+ " veh_vehsource_id = '" + value + "'"
								+ " WHERE veh_id = " + veh_id + "";
						updateQuery(StrSql);

						history_newvalue = ExecuteQuery("SELECT vehsource_name FROM " + compdb(comp_id) + "axela_service_veh_source"
								+ " WHERE vehsource_id = " + value);

						history_actiontype = "Veh. Source";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
								+ " (history_veh_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " ('" + veh_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						StrHTML = "Veh. Source updated!";
					} else if (name.equals("txt_veh_service_duekms")) {
						value = value.replaceAll("nbsp", "&");
						history_oldvalue = ExecuteQuery("SELECT veh_service_duekms"
								+ " FROM " + compdb(comp_id) + "axela_service_veh"
								+ " WHERE veh_id = " + veh_id);

						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
								+ " SET"
								+ " veh_service_duekms = '" + CNumeric(value) + "'"
								+ " WHERE veh_id = " + veh_id + "";
						updateQuery(StrSql);
						history_actiontype = "Service Due Kms";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
								+ " (history_veh_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " ('" + veh_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + CNumeric(value) + "')";
						updateQuery(StrSql);
						StrHTML = "Service Due Kms updated!";
					} else if (name.equals("txt_veh_service_duedate")) {
						value = value.replaceAll("nbsp", "&");

						if ((!value.equals("") && isValidDateFormatShort(value)) || value.equals("")) {
							String veh_sale_date = ExecuteQuery("SELECT veh_sale_date"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id);
							if (!value.equals("") && Long.parseLong(veh_sale_date) > Long.parseLong(ConvertShortDateToStr(value))) {
								StrHTML = "<font color=\"red\">Service Due Date must be greater than or equal to the Vehicle Sale Date!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT veh_service_duedate"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);
								if (!history_oldvalue.equals("")) {
									history_oldvalue = strToShortDate(history_oldvalue);
								} else {
									history_oldvalue = "";
								}
								if (!value.equals("") && isValidDateFormatShort(value)) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_service_duedate = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_service_duedate = ''"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								}

								history_actiontype = "Service Due Date";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Service Due Date updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Service Due Date!</font>";
						}
					}
					else if (name.equals("txt_veh_lastservice")) {
						value = value.replaceAll("nbsp", "&");

						if ((!value.equals("") && isValidDateFormatShort(value)) || value.equals("")) {
							String veh_sale_date = ExecuteQuery("SELECT veh_sale_date"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id);
							if (!value.equals("") && Long.parseLong(veh_sale_date) > Long.parseLong(ConvertShortDateToStr(value))) {
								StrHTML = "<font color=\"red\">Last Service Date must be greater than or equal to the Vehicle Sale Date!</font>";
							}
							else {
								history_oldvalue = ExecuteQuery("SELECT veh_lastservice"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);
								if (!history_oldvalue.equals("")) {
									history_oldvalue = strToShortDate(history_oldvalue);
								} else {
									history_oldvalue = "";
								}
								if (!value.equals("") && isValidDateFormatShort(value)) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_lastservice = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_lastservice = ''"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								}

								history_actiontype = "Last Service Date";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Last Service Date updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Last Service Date!</font>";
						}
					}
					// ///Last Service Kms
					else if (name.equals("txt_veh_lastservice_kms")) {
						value = value.replaceAll("nbsp", "&");

						if (!value.equals("")) {
							// String veh_sale_date = ExecuteQuery("SELECT veh_sale_date"
							// + " FROM " + compdb(comp_id) + "axela_service_veh"
							// + " WHERE veh_id = " + veh_id);
							// if (!value.equals("") && Long.parseLong(veh_sale_date) > Long.parseLong(ConvertShortDateToStr(value))) {
							// StrHTML = "<font color=\"red\">Last Service Date must be greater than or equal to the Vehicle Sale Date!</font>";
							// }
							// else {
							history_oldvalue = ExecuteQuery("SELECT veh_lastservice_kms"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id);
							if (!value.equals("")) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET"
										+ " veh_lastservice_kms = " + value + ""
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);
							} else {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " SET"
										+ " veh_lastservice = 0"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);
							}

							history_actiontype = "Last Service Kms";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Last Service Kms updated!";
							// }
						} else {
							StrHTML = "<font color=\"red\">Enter valid Last Service Kms!</font>";
						}
					}
					// //End of Last Service Kms
					// else if (name.equals("txt_veh_insur_date")) {
					// if ((!value.equals("") && isValidDateFormatShort(value)) || value.equals("")) {
					// value = value.replaceAll("nbsp", "&");
					// history_oldvalue = strToShortDate(ExecuteQuery("SELECT veh_insur_date"
					// + " FROM " + compdb(comp_id) + "axela_service_veh"
					// + " WHERE veh_id = " + veh_id));
					//
					// if (!value.equals("") && isValidDateFormatShort(value)) {
					// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
					// + " SET"
					// + " veh_insur_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
					// + " WHERE veh_id = " + veh_id + "";
					// updateQuery(StrSql);
					// } else {
					// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
					// + " SET"
					// + " veh_insur_date = ''"
					// + " WHERE veh_id = " + veh_id + "";
					// updateQuery(StrSql);
					// }
					// history_newvalue = strToShortDate(ExecuteQuery("SELECT veh_insur_date"
					// + " FROM " + compdb(comp_id) + "axela_service_veh"
					// + " WHERE veh_id = " + veh_id));
					//
					// history_actiontype = "Insurance Date";
					//
					// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
					// + " (history_veh_id,"
					// + " history_emp_id,"
					// + " history_datetime,"
					// + " history_actiontype,"
					// + " history_oldvalue,"
					// + " history_newvalue)"
					// + " VALUES"
					// + " ('" + veh_id + "',"
					// + " '" + emp_id + "',"
					// + " '" + ToLongDate(kknow()) + "',"
					// + " '" + history_actiontype + "',"
					// + " '" + history_oldvalue + "',"
					// + " '" + history_newvalue + "')";
					// updateQuery(StrSql);
					// StrHTML = "Insurance Date updated!";
					// }
					// // else {
					// // StrHTML = "<font color=\"red\">Enter valid Renewal Date!</font>";
					// // }
					// }
					// GIVEN IN FOLLOWUP TAB

					// else if (name.equals("dr_veh_crmemp_id")) {
					// if (!value.equals("0")) {
					// value = value.replaceAll("nbsp", "&");
					// history_oldvalue = (ExecuteQuery("SELECT emp_name"
					// + " FROM " + compdb(comp_id) + "axela_emp"
					// + " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_crmemp_id = emp_id"
					// + " WHERE veh_id = " + veh_id));
					//
					// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
					// + " SET"
					// + " veh_crmemp_id = " + CNumeric(value) + ""
					// + " WHERE veh_id = " + veh_id + "";
					// updateQuery(StrSql);
					//
					// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_followup"
					// + " SET"
					// + " vehfollowup_emp_id = " + CNumeric(value) + ""
					// + " WHERE vehfollowup_veh_id = " + veh_id
					// + " AND vehfollowup_desc = ''";
					// updateQuery(StrSql);
					//
					// history_newvalue = ExecuteQuery("SELECT emp_name"
					// + " FROM " + compdb(comp_id) + "axela_emp"
					// + " WHERE emp_id = " + value);
					//
					// history_actiontype = "CRM Executive";
					//
					// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
					// + " (history_veh_id,"
					// + " history_emp_id,"
					// + " history_datetime,"
					// + " history_actiontype,"
					// + " history_oldvalue,"
					// + " history_newvalue)"
					// + " VALUES"
					// + " ('" + veh_id + "',"
					// + " '" + emp_id + "',"
					// + " '" + ToLongDate(kknow()) + "',"
					// + " '" + history_actiontype + "',"
					// + " '" + history_oldvalue + "',"
					// + " '" + history_newvalue + "')";
					// updateQuery(StrSql);
					// StrHTML = "CRM Executive updated!";
					// } else {
					// StrHTML = "<font color=\"red\">Select CRM Executive!</font>";
					// }
					// }

					else if (name.equals("dr_veh_emp_id")) {
						if (!value.equals("0")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = (ExecuteQuery("SELECT emp_name"
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_emp_id = emp_id"
									+ " WHERE veh_id = " + veh_id));

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									+ " veh_emp_id = '" + CNumeric(value) + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT emp_name"
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE emp_id = " + value);

							history_actiontype = "Vehicle Executive";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
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
					}
					else if (name.equals("txt_veh_warranty_expirydate")) {
						if ((!value.equals("") && isValidDateFormatShort(value)) || value.equals("")) {
							value = value.replaceAll("nbsp", "&");

							StrSql = "SELECT veh_sale_date FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id + "";
							String veh_sale_date = ExecuteQuery(StrSql);
							if (!value.equals("") && Long.parseLong(veh_sale_date) > Long.parseLong(ConvertShortDateToStr(value))) {
								StrHTML = "<font color=\"red\">Warranty Expiry Date must be greater than or equal to the Vehicle Sale Date!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT veh_warranty_expirydate"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);
								if (!history_oldvalue.equals("")) {
									history_oldvalue = strToShortDate(history_oldvalue);
								}

								if (!value.equals("") && isValidDateFormatShort(value)) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_warranty_expirydate = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_warranty_expirydate = ''"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								}

								history_actiontype = "Warranty Expiry Date";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Warranty Expiry Date updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Warranty Expiry Date!</font>";
						}
					} else if (name.equals("txt_veh_warranty_expirydate")) {
						if ((!value.equals("") && isValidDateFormatShort(value)) || value.equals("")) {
							value = value.replaceAll("nbsp", "&");

							StrSql = "SELECT veh_sale_date FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id + "";
							String veh_sale_date = ExecuteQuery(StrSql);
							if (!value.equals("") && Long.parseLong(veh_sale_date) > Long.parseLong(ConvertShortDateToStr(value))) {
								StrHTML = "<font color=\"red\">Warranty Expiry Date must be greater than or equal to the Vehicle Sale Date!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT veh_warranty_expirydate"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " WHERE veh_id = " + veh_id);
								if (!history_oldvalue.equals("")) {
									history_oldvalue = strToShortDate(history_oldvalue);
								}

								if (!value.equals("") && isValidDateFormatShort(value)) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_warranty_expirydate = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								} else {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
											+ " SET"
											+ " veh_warranty_expirydate = ''"
											+ " WHERE veh_id = " + veh_id + "";
									updateQuery(StrSql);
								}

								history_actiontype = "Warranty Expiry Date";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Warranty Expiry Date updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Warranty Expiry Date!</font>";
						}
					} else if (name.equals("chk_veh_iacs")) {
						if (value.equals("1")) {
							history_oldvalue = "No";
							history_newvalue = "Yes";
						} else {
							history_oldvalue = "Yes";
							history_newvalue = "No";
						}

						StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
								+ " SET"
								+ " veh_iacs = '" + value + "'"
								+ " WHERE veh_id = " + veh_id + "";
						updateQuery(StrSql);

						history_actiontype = "IACS";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
								+ " (history_veh_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " VALUES"
								+ " ('" + veh_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + history_newvalue + "')";
						updateQuery(StrSql);
						StrHTML = "IACS updated!";
					} else if (name.equals("txt_veh_notes")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT veh_notes"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									+ " veh_notes = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Notes";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Notes updated!";
						} else {
							StrHTML = "<font color=\"red\">Enter Notes!</font>";
						}
					} else if (name.equals("txt_veh_customer_name")) {
						if (!value.equals("")) {
							if (value.length() < 3) {
								StrHTML = ("<font color=\"red\">Enter atleast 3 Characters for Customer Name!</font>");
								return;
							}
							value = value.replaceAll("nbsp", "&");

							history_oldvalue = ExecuteQuery("SELECT COALESCE(customer_name, '') AS customer_name"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_customer_id = customer_id"
									+ " SET"
									+ " customer_name = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Customer";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
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
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
									+ " SET"
									+ " contact_title_id = " + value + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT title_desc"
									+ " FROM " + compdb(comp_id) + "axela_title"
									+ " WHERE title_id = " + value);

							history_actiontype = "Contact Title";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
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
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " SET"
									+ " contact_fname = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact First Name";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact First Name updated!";
						} else {
							StrHTML = "<font color=\"red\">Enter Contact First Name!</font>";
						}
					} else if (name.equals("txt_contact_lname")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_lname"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " SET"
									+ " contact_lname = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Last Name";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
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
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "'"
									+ " OR contact_mobile2 = '" + value + "'"
									+ " OR contact_mobile3 = '" + value + "'"
									+ " OR contact_mobile4 = '" + value + "'"
									+ " OR contact_mobile5 = '" + value + "'"
									+ " OR contact_mobile6 = '" + value + "')"
									+ " AND veh_id != " + veh_id + "";
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "<font color=\"red\">Similar Mobile 1 Found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT contact_mobile1"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
										+ " SET"
										+ " contact_mobile1 = '" + value + "',"
										+ " customer_mobile1 = '" + value + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Mobile1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Mobile1 updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Mobile1!</font>";
						}
					} else if (name.equals("txt_contact_mobile2")) {
						if (!value.equals("") && IsValidMobileNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "'"
									+ " OR contact_mobile2 = '" + value + "'"
									+ " OR contact_mobile3 = '" + value + "'"
									+ " OR contact_mobile4 = '" + value + "'"
									+ " OR contact_mobile5 = '" + value + "'"
									+ " OR contact_mobile6 = '" + value + "')"
									+ " AND veh_id != " + veh_id + "";
							// SOP("StrSql==" + StrSql);
							// if (!ExecuteQuery(StrSql).equals("")) {
							// StrHTML = "<font color=\"red\">Similar Mobile 2 Found!</font>";
							// } else {
							history_oldvalue = ExecuteQuery("SELECT contact_mobile2"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile2 = '" + value + "',"
									+ " customer_mobile2 = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Mobile2";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Mobile2 updated!";
							// }
						} else {
							StrHTML = "<font color=\"red\">Enter valid Mobile2!</font>";
						}
					} else if (name.equals("txt_contact_mobile3")) {
						if (!value.equals("") && IsValidMobileNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "'"
									+ " OR contact_mobile2 = '" + value + "'"
									+ " OR contact_mobile3 = '" + value + "'"
									+ " OR contact_mobile4 = '" + value + "'"
									+ " OR contact_mobile5 = '" + value + "'"
									+ " OR contact_mobile6 = '" + value + "')"
									+ " AND veh_id != " + veh_id + "";
							// SOP("StrSql==" + StrSql);
							// if (!ExecuteQuery(StrSql).equals("")) {
							// StrHTML = "<font color=\"red\">Similar Mobile 3 Found!</font>";
							// } else {
							history_oldvalue = ExecuteQuery("SELECT contact_mobile3"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile3 = '" + value + "',"
									+ " customer_mobile3 = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Mobile3";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Mobile3 updated!";
							// }
						} else {
							StrHTML = "<font color=\"red\">Enter valid Mobile3!</font>";
						}
					} else if (name.equals("txt_contact_mobile4")) {
						if (!value.equals("") && IsValidMobileNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "'"
									+ " OR contact_mobile2 = '" + value + "'"
									+ " OR contact_mobile3 = '" + value + "'"
									+ " OR contact_mobile4 = '" + value + "'"
									+ " OR contact_mobile5 = '" + value + "'"
									+ " OR contact_mobile6 = '" + value + "')"
									+ " AND veh_id != " + veh_id + "";
							// SOP("StrSql==" + StrSql);
							// if (!ExecuteQuery(StrSql).equals("")) {
							// StrHTML = "<font color=\"red\">Similar Mobile 4 Found!</font>";
							// } else {
							history_oldvalue = ExecuteQuery("SELECT contact_mobile4"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile4 = '" + value + "',"
									+ " customer_mobile4 = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Mobile4";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Mobile4 updated!";
							// }
						} else {
							StrHTML = "<font color=\"red\">Enter valid Mobile4!</font>";
						}
					} else if (name.equals("txt_contact_mobile5")) {
						if (!value.equals("") && IsValidMobileNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "'"
									+ " OR contact_mobile2 = '" + value + "'"
									+ " OR contact_mobile3 = '" + value + "'"
									+ " OR contact_mobile4 = '" + value + "'"
									+ " OR contact_mobile5 = '" + value + "'"
									+ " OR contact_mobile6 = '" + value + "')"
									+ " AND veh_id != " + veh_id + "";
							// SOP("StrSql==" + StrSql);
							// if (!ExecuteQuery(StrSql).equals("")) {
							// StrHTML = "<font color=\"red\">Similar Mobile 5 Found!</font>";
							// } else {
							history_oldvalue = ExecuteQuery("SELECT contact_mobile5"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile5 = '" + value + "',"
									+ " customer_mobile5 = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Mobile5";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Mobile5 updated!";
							// }
						} else {
							StrHTML = "<font color=\"red\">Enter valid Mobile5!</font>";
						}
					} else if (name.equals("txt_contact_mobile6")) {
						if (!value.equals("") && IsValidMobileNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "'"
									+ " OR contact_mobile2 = '" + value + "'"
									+ " OR contact_mobile3 = '" + value + "'"
									+ " OR contact_mobile4 = '" + value + "'"
									+ " OR contact_mobile5 = '" + value + "'"
									+ " OR contact_mobile6 = '" + value + "')"
									+ " AND veh_id != " + veh_id + "";
							// SOP("StrSql==" + StrSql);
							// if (!ExecuteQuery(StrSql).equals("")) {
							// StrHTML = "<font color=\"red\">Similar Mobile 6 Found!</font>";
							// } else {
							history_oldvalue = ExecuteQuery("SELECT contact_mobile6"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile6 = '" + value + "',"
									+ " customer_mobile6 = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Mobile6";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Mobile6 updated!";
							// }
						} else {
							StrHTML = "<font color=\"red\">Enter valid Mobile6!</font>";
						}
					} else if (name.equals("dr_new_phonetype_id1")) {
						if (!value.equals("")) {
							// value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_mobile1_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_oldvalue ==" + history_oldvalue);
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile1_phonetype_id = " + value + ","
									+ " customer_mobile1_phonetype_id = " + value + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
							// SOP("Update===" + StrSql);
							history_newvalue = ExecuteQuery("SELECT contact_mobile1_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_newvalue ===" + history_newvalue);
							history_actiontype = "Phone Type";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							// SOP("History new insert ===" + StrSql);
							StrHTML = "Phone Type updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Phone Type!</font>";
						}
					} else if (name.equals("dr_new_phonetype_id2")) {
						if (!value.equals("")) {
							// value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_mobile2_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_oldvalue ==" + history_oldvalue);
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile2_phonetype_id = " + value + ","
									+ " customer_mobile2_phonetype_id = " + value + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
							// SOP("Update===" + StrSql);
							history_newvalue = ExecuteQuery("SELECT contact_mobile2_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_newvalue ===" + history_newvalue);
							history_actiontype = "Phone Type";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							// SOP("History new insert ===" + StrSql);
							StrHTML = "Phone Type updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Phone Type!</font>";
						}
					} else if (name.equals("dr_new_phonetype_id3")) {
						if (!value.equals("")) {
							// value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_mobile3_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_oldvalue ==" + history_oldvalue);
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile3_phonetype_id = " + value + ","
									+ " customer_mobile3_phonetype_id = " + value + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
							// SOP("Update===" + StrSql);
							history_newvalue = ExecuteQuery("SELECT contact_mobile3_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_newvalue ===" + history_newvalue);
							history_actiontype = "Phone Type";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							// SOP("History new insert ===" + StrSql);
							StrHTML = "Phone Type updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Phone Type!</font>";
						}
					} else if (name.equals("dr_new_phonetype_id4")) {
						if (!value.equals("")) {
							// value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_mobile4_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_oldvalue ==" + history_oldvalue);
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile4_phonetype_id = " + value + ","
									+ " customer_mobile4_phonetype_id = " + value + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
							// SOP("Update===" + StrSql);
							history_newvalue = ExecuteQuery("SELECT contact_mobile4_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_newvalue ===" + history_newvalue);
							history_actiontype = "Phone Type";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							// SOP("History new insert ===" + StrSql);
							StrHTML = "Phone Type updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Phone Type!</font>";
						}
					} else if (name.equals("dr_new_phonetype_id5")) {
						if (!value.equals("")) {
							// value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_mobile5_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_oldvalue ==" + history_oldvalue);
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile5_phonetype_id = " + value + ","
									+ " customer_mobile5_phonetype_id = " + value + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
							// SOP("Update===" + StrSql);
							history_newvalue = ExecuteQuery("SELECT contact_mobile5_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_newvalue ===" + history_newvalue);
							history_actiontype = "Phone Type";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							// SOP("History new insert ===" + StrSql);
							StrHTML = "Phone Type updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Phone Type!</font>";
						}
					} else if (name.equals("dr_new_phonetype_id6")) {
						if (!value.equals("")) {
							// value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_mobile6_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_oldvalue ==" + history_oldvalue);
							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_mobile6_phonetype_id = " + value + ","
									+ " customer_mobile6_phonetype_id = " + value + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);
							// SOP("Update===" + StrSql);
							history_newvalue = ExecuteQuery("SELECT contact_mobile6_phonetype_id"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " WHERE veh_id = " + veh_id);
							// SOP("history_newvalue ===" + history_newvalue);
							history_actiontype = "Phone Type";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							// SOP("History new insert ===" + StrSql);
							StrHTML = "Phone Type updated!";
						} else {
							StrHTML = "<font color=\"red\">Select Phone Type!</font>";
						}
					} else if (name.equals("txt_contact_address")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_address"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_address = '" + value + "',"
									+ " customer_address = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Address";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Address updated!";
						} else {
							StrHTML = "<font color=\"red\">Enter Contact Address!</font>";
						}
					} // else if (name.equals("txt_contact_address2")) {
						// value = value.replaceAll("nbsp", "&");
						// history_oldvalue = ExecuteQuery("SELECT contact_address2"
						// + " FROM " + compdb(comp_id) + "axela_service_veh"
						// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						// + " WHERE veh_id = " + veh_id);
						//
						// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
						// + " SET"
						// + " contact_address2 = '" + value + "',"
						// + " customer_address2 = '" + value + "'"
						// + " WHERE veh_id = " + veh_id + "";
						// updateQuery(StrSql);
						//
						// history_actiontype = "Contact Address";
						//
						// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
						// + " (history_veh_id,"
						// + " history_emp_id,"
						// + " history_datetime,"
						// + " history_actiontype,"
						// + " history_oldvalue,"
						// + " history_newvalue)"
						// + " VALUES"
						// + " ('" + veh_id + "',"
						// + " '" + emp_id + "',"
						// + " '" + ToLongDate(kknow()) + "',"
						// + " '" + history_actiontype + "',"
						// + " '" + history_oldvalue + "',"
						// + " '" + value + "')";
						// updateQuery(StrSql);
						// StrHTML = "Contact Address updated!";
						// } else if (name.equals("txt_contact_address3")) {
						// value = value.replaceAll("nbsp", "&");
						// history_oldvalue = ExecuteQuery("SELECT contact_address3"
						// + " FROM " + compdb(comp_id) + "axela_service_veh"
						// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						// + " WHERE veh_id = " + veh_id);
						//
						// StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						// + " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
						// + " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
						// + " SET"
						// + " contact_address3 = '" + value + "',"
						// + " customer_address3 = '" + value + "'"
						// + " WHERE veh_id = " + veh_id + "";
						// updateQuery(StrSql);
						//
						// history_actiontype = "Contact Address";
						//
						// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
						// + " (history_veh_id,"
						// + " history_emp_id,"
						// + " history_datetime,"
						// + " history_actiontype,"
						// + " history_oldvalue,"
						// + " history_newvalue)"
						// + " VALUES"
						// + " ('" + veh_id + "',"
						// + " '" + emp_id + "',"
						// + " '" + ToLongDate(kknow()) + "',"
						// + " '" + history_actiontype + "',"
						// + " '" + history_oldvalue + "',"
						// + " '" + value + "')";
						// updateQuery(StrSql);
						// StrHTML = "Contact Address updated!";
						// }
					else if (name.equals("dr_city_id")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT city_name"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_city_id = '" + value + "',"
									+ " customer_city_id = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT city_name FROM " + compdb(comp_id) + "axela_city"
									+ " WHERE city_id = " + value);

							history_actiontype = "Contact City";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Contact City updated!";
						} else {
							StrHTML = "<font color=\"red\">Select City!</font>";
						}
					} else if (name.equals("txt_contact_pin")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = ExecuteQuery("SELECT contact_pin"
									+ " FROM " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " WHERE veh_id = " + veh_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
									+ " SET"
									+ " contact_pin = '" + value + "',"
									+ " customer_pin = '" + value + "'"
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							history_actiontype = "Contact Pin";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
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
					} else if (name.equals("txt_contact_phone1")) {
						if (!value.equals("") && IsValidPhoneNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE contact_phone1 = '" + value + "'"
									+ " OR contact_phone2 = '" + value + "'"
									+ " AND veh_id != " + veh_id;
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "<font color=\"red\">Similar Phone 1 Found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT contact_phone1"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
										+ " SET"
										+ " contact_phone1 = '" + value + "',"
										+ " customer_phone1 = '" + value + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Phone1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Phone1 updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Phone1!</font>";
						}
					} else if (name.equals("txt_contact_phone2")) {
						if (!value.equals("") && IsValidPhoneNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_id FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE contact_phone1 = '" + value + "'"
									+ " OR contact_phone2 = '" + value + "'"
									+ " AND veh_id != " + veh_id;

							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "<font color=\"red\">Similar Phone 2 Found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT contact_phone2"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
										+ " SET"
										+ " contact_phone2 = '" + value + "',"
										+ " customer_phone2 = '" + value + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Phone1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Phone2 updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Phone2!</font>";
						}
					} else if (name.equals("txt_contact_email1")) {
						if (!value.equals("") && IsValidEmail(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_email1 FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_contact_id = contact_id"
									+ " WHERE contact_email1 = '" + value + "'"
									+ " AND veh_id != " + veh_id;
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "<font color=\"red\">Similar Email 1 Found!</font>";
							} else {
								history_oldvalue = ExecuteQuery("SELECT contact_email1"
										+ " FROM " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " WHERE veh_id = " + veh_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
										+ " SET"
										+ " contact_email1 = '" + value + "'"
										+ " WHERE veh_id = " + veh_id + "";
								updateQuery(StrSql);

								history_actiontype = "Contact Email1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
										+ " (history_veh_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES"
										+ " ('" + veh_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Email1 updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Enter valid Email1!</font>";
						}
					} else if (name.equals("dr_vehfollowup_crmexe_id")) {
						if (!value.equals("0")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = (ExecuteQuery("SELECT emp_name"
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_crmemp_id = emp_id"
									+ " WHERE veh_id = " + veh_id));

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									+ " veh_crmemp_id = " + CNumeric(value) + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_followup"
									+ " SET"
									+ " vehfollowup_emp_id = " + CNumeric(value) + ""
									+ " WHERE vehfollowup_veh_id = " + veh_id
									+ " AND vehfollowup_desc = ''";
							updateQuery(StrSql);

							history_newvalue = ExecuteQuery("SELECT emp_name"
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE emp_id = " + value);

							history_actiontype = "CRM Executive";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "CRM Executive updated!";
						} else {
							StrHTML = "<font color=\"red\">Select CRM Executive!</font>";
						}
					} else if (name.equals("chk_veh_classified")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							history_oldvalue = (ExecuteQuery("SELECT IF(veh_classified = 0, 'Inactive', 'Active')"
									+ " FROM " + compdb(comp_id) + "axela_service_veh" + " WHERE veh_id = " + veh_id));

							StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
									+ " SET"
									+ " veh_classified = " + CNumeric(value) + ""
									+ " WHERE veh_id = " + veh_id + "";
							updateQuery(StrSql);

							if (value.equals("0")) {
								history_newvalue = "Inactive";
							} else {
								history_newvalue = "Active";
							}

							history_actiontype = "Classified";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_history"
									+ " (history_veh_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + veh_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Classified updated!";
						} else {
							StrHTML = "<font color=\"red\">Select CRM Executive!</font>";
						}
					}
				} else {
					StrHTML = "<font color=\"red\">Update Permission Denied!</font>";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	/* Through Search get Item in Stock Status Tab (Vehicle-Dash.jsp) */
	public String StockOnHandDetails() {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT item_id, item_code, item_name, uom_name, stock_reorderlevel,"
					+ " stock_current_qty, stock_stockinorder, stock_stockindemand,"
					+ " stock_unit_cost, stock_onhandvalue, item_eoq"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_cat_pop ON cat_id = item_cat_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_uom ON item_uom_id = uom_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_stock ON stock_item_id = item_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_location ON stock_location_id = location_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = location_branch_id"
					+ " WHERE item_active = 1" + StrSearch + BranchAccess + ""
					+ " GROUP BY item_id"
					+ " ORDER BY item_name"
					+ " LIMIT 10";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				int count = 0;
				double nett_available = 0.00;
				String short_fall = "", order = "";

				Str.append("<table border=\"1\" cellspacing=\"0\" cellpadding=\"0\" class=\"listtable\">\n");
				Str.append("<tr align=\"center\">\n");
				Str.append("<td height=\"20\" align=\"center\"><b>#</b></td>\n");
				Str.append("<td align=\"center\"><b>Item ID</b></td>\n");
				Str.append("<td align=\"center\"><b>Item Name</b></td>\n");
				Str.append("<td align=\"center\"><b>Item Code</b></td>\n");
				Str.append("<td align=\"center\"><b>UOM</b></td>\n");
				Str.append("<td align=\"center\"><b>Unit Cost</b></td>\n");
				Str.append("<td align=\"center\"><b>On Hand Value</b></td>\n");
				Str.append("<td align=\"center\"><b>Closing Stock</b></td>\n");
				Str.append("<td align=\"center\"><b>Purchase Order Pending</b></td>\n");
				Str.append("<td align=\"center\"><b>Purchase Orders Due</b></td>\n");
				Str.append("<td align=\"center\"><b>Nett Available</b></td>\n");
				Str.append("<td align=\"center\"><b>Reorder Level</b></td>\n");
				Str.append("<td align=\"center\"><b>Short Fall</b></td>\n");
				Str.append("<td align=\"center\"><b>Minimum Order Quantity</b></td>\n");
				Str.append("<td align=\"center\"><b>Order to be Placed</b></td>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					short_fall = "";
					order = "";
					nett_available = crs.getDouble("stock_current_qty") + crs.getDouble("stock_stockinorder") - crs.getDouble("stock_stockindemand");
					short_fall = Double.toString(crs.getDouble("stock_reorderlevel") - nett_available);
					if (Double.parseDouble(CNumeric(short_fall)) < 1) {
						short_fall = "";
					}

					if (!short_fall.equals("")) {
						if (Double.parseDouble(CNumeric(short_fall)) > crs.getDouble("stock_reorderlevel")) {
							order = short_fall;
						} else {
							order = crs.getString("stock_reorderlevel");
						}
					}

					Str.append("<tr>\n<td align=\"center\" valign=\"top\" height=\"20\">").append(count).append("</td>\n");
					Str.append("<td align=\"center\" valign=\"top\">").append(crs.getString("item_id")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("item_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("item_code")).append("</td>\n");
					Str.append("<td align=\"left\" valign=\"top\">").append(crs.getString("uom_name")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("stock_unit_cost")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("stock_onhandvalue")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("stock_current_qty")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("stock_stockinorder")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("stock_stockindemand")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(nett_available).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getString("stock_reorderlevel")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(short_fall).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(crs.getDouble("item_eoq")).append("</td>\n");
					Str.append("<td align=\"right\" valign=\"top\">").append(order).append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<font color=\"red\"><b>No Stock!<b></font>");
			}
			crs.close();
			Str.append("</table>\n");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
