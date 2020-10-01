package axela.sales;
///divya 4th dec

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Enquiry_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String name = "";
	public String value = "";
	public String enquiry_id = "0", so_id = "", so_delivered_date = "";
	public String enquiry_dat = "", from_date = "";
	public String enquiry_emp_id = "0", enquiry_branch_id = "0";
	public String enquiry_date = "";
	public String enquiry_model_id = "0";
	public String enquiry_buyertype_id = "0";
	public String enquiry_existingvehicle = "";
	// public String enquiry_preownedmodel_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String name1 = "";
	public String value1 = "";
	public String name2 = "";
	public String value2 = "";
	public String followup = "";
	public String crm_details = "";
	public String customer_details = "";
	public String doc_details = "";
	public String testdrive_details = "";
	public String quote_details = "";
	public String so_details = "";
	public String invoice_details = "";
	public String receipt_details = "";
	public String history = "";
	public int recperpage = 0;
	public String customer_id = "0", PageCurrents = "", QueryString = "";
	public String contact_id = "";
	public String delete = "";
	public String enquiry_tradeinmake_id = "0", make_id = "0";
	public String lostcase2 = "";
	public String lostcase3 = "";
	public String lostcase1 = "";
	public String status_id = "";
	public String status_desc = "";
	public String preownedmodel_name = "";
	public String variant_name = "";
	public String emp_all_exe = "";
	public String preownedmodel_id = "0", emp_role_id = "0";
	public String branch_brand_id = "0";
	public String porsche = "";
	public String jlr = "";
	public String enquiry_brand_id = "";
	public String needassesment = "";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		// System.out.println("Enquiry_Dash_Check==Coming==");
		if (!comp_id.equals("0")) {
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			emp_all_exe = GetSession("emp_all_exe", request);
			recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			enquiry_id = CNumeric(PadQuotes(request.getParameter("enquiry_id")));
			so_id = CNumeric(PadQuotes(request.getParameter("so_id")));
			emp_role_id = CNumeric(GetSession("emp_role_id", request));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			name1 = PadQuotes(request.getParameter("name1"));
			value1 = PadQuotes(request.getParameter("value1"));
			name2 = PadQuotes(request.getParameter("name2"));
			value2 = PadQuotes(request.getParameter("value2"));
			enquiry_dat = PadQuotes(request.getParameter("enquiry_dat"));
			from_date = PadQuotes(request.getParameter("from_date"));
			enquiry_model_id = (PadQuotes(request.getParameter("enquiry_model_id")));
			// enquiry_preownedmodel_id = CNumeric(PadQuotes(request.getParameter("enquiry_preownedmodel_id")));
			contact_id = PadQuotes(request.getParameter("contact_id"));
			delete = PadQuotes(request.getParameter("delete"));
			customer_id = CNumeric(PadQuotes(request.getParameter("customer_id")));
			enquiry_tradeinmake_id = CNumeric(PadQuotes(request.getParameter("enquiry_tradeinmake_id")));
			enquiry_buyertype_id = CNumeric(PadQuotes(request.getParameter("enquiry_buyertype_id")));
			enquiry_existingvehicle = PadQuotes(request.getParameter("txt_enquiry_existingvehicle"));
			make_id = CNumeric(PadQuotes(request.getParameter("make_id")));
			followup = PadQuotes(request.getParameter("followup"));
			crm_details = PadQuotes(request.getParameter("crm_details"));
			customer_details = PadQuotes(request.getParameter("customer_details"));
			doc_details = PadQuotes(request.getParameter("doc_details"));
			testdrive_details = PadQuotes(request.getParameter("testdrive_details"));
			quote_details = PadQuotes(request.getParameter("quote_details"));
			so_details = PadQuotes(request.getParameter("so_details"));
			invoice_details = PadQuotes(request.getParameter("invoice_details"));
			receipt_details = PadQuotes(request.getParameter("receipt_details"));
			history = PadQuotes(request.getParameter("history"));
			status_id = CNumeric(PadQuotes(request.getParameter("status_id")));
			status_desc = PadQuotes(request.getParameter("status_desc"));
			lostcase1 = PadQuotes(request.getParameter("lostcase1"));
			lostcase2 = PadQuotes(request.getParameter("lostcase2"));
			lostcase3 = PadQuotes(request.getParameter("lostcase3"));
			PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
			QueryString = PadQuotes(request.getQueryString());

			enquiry_brand_id = CNumeric(PadQuotes(request.getParameter("enquiry_brand_id")));
			needassesment = PadQuotes(request.getParameter("needassesment"));

			if (name.contains("porsche")) {
				StrHTML = doGetPorsche(request, response);

			} else if (name.contains("jlr")) {
				StrHTML = doGetJLR(request, response);
			} else if (needassesment.equals("skoda") && enquiry_brand_id.equals("11")) {
				StrHTML = EnquiryDashCheckSkoda(request, response);
			} else if (invoice_details.equals("yes")) {
				StrHTML = ListInvoice(enquiry_id, BranchAccess, ExeAccess, comp_id);
			} else if (receipt_details.equals("yes")) {
				StrHTML = ListReceipt(so_id, BranchAccess, ExeAccess, comp_id);
			}
			else {
				try {
					if (!enquiry_id.equals("0")) {
						StrSql = "SELECT enquiry_emp_id, enquiry_branch_id, enquiry_date, branch_brand_id, "
								+ " COALESCE(so_delivered_date, '') AS  'so_delivered_date'"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
								+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id "
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id "
								+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id AND so_active=1"
								+ " WHERE 1 = 1 "
								+ " AND enquiry_id = " + enquiry_id
								+ BranchAccess + ExeAccess;

						StrSql += " GROUP BY enquiry_id "
								+ " ORDER BY enquiry_id";
						// SOP("StrSql--Enquiry-Dash-Check----" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);
						while (crs.next()) {
							enquiry_emp_id = crs.getString("enquiry_emp_id");
							enquiry_branch_id = crs.getString("enquiry_branch_id");
							enquiry_date = crs.getString("enquiry_date");
							so_delivered_date = crs.getString("so_delivered_date");
							branch_brand_id = crs.getString("branch_brand_id");

						}
						crs.close();
					} else {
						StrHTML = "Update Permission Denied!";
						return;
					}
					if (!emp_role_id.equals("1") && !so_delivered_date.equals("")) {
						StrHTML = "SO Delivered<br>Update Permission Denied!";
						return;
					}
					else {
						// if (crm_details.equals("yes")) {
						// StrHTML = new Enquiry_Dash().ListCRMDetails(enquiry_id);
						// } else if (customer_details.equals("yes")) {
						// StrHTML = new
						// Enquiry_Dash_Customer().CustomerDetails(response,
						// customer_id, "");
						// } else if (doc_details.equals("yes")) {
						// StrHTML = new Enquiry_Dash().ListDocs(enquiry_id,
						// PageCurrents, QueryString, recperpage);
						// } else if (testdrive_details.equals("yes")) {
						// StrHTML = new Enquiry_Dash().ListTestDrive(enquiry_id,
						// BranchAccess, ExeAccess, request, response);
						// } else if (quote_details.equals("yes")) {
						// StrHTML = new Enquiry_Dash().ListQuotes(enquiry_id,
						// BranchAccess, ExeAccess);
						// } else if (so_details.equals("yes")) {
						// StrHTML = new Enquiry_Dash().ListSO(enquiry_id,
						// BranchAccess, ExeAccess);
						// } else if (history.equals("yes")) {
						// StrHTML = new Enquiry_Dash().ListHistory(enquiry_id,
						// BranchAccess, ExeAccess);
						// } else {
						StrHTML = "";
						if (name.equals("txt_enquiry_exp_close_date") && !enquiry_dat.equals("")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								// SOP("value in closing" + value);
								if (!isValidDateFormatShort(value)) {
									StrHTML = "<font color=\"red\">Enter Valid Close Date!</font>";
									return;
								} else {
									if (!enquiry_dat.equals("") && !value.equals("") && Long.parseLong(ConvertShortDateToStr(enquiry_dat)) > Long.parseLong(ConvertShortDateToStr(value))) {
										StrHTML = "Closing Date can't be less than the Start Date!";
										return;
									}
									String history_oldvalue = strToShortDate(ExecuteQuery("SELECT enquiry_close_date"
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
											+ " WHERE enquiry_id = " + enquiry_id + ""));
									// SOP("ConvertShortDateToStr(value))---"+ConvertShortDateToStr(value));
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " SET"
											+ " enquiry_close_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " SET"
											+ " enquiry_trigger = '0'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									// SOP("StrSql---"+StrSql);
									updateQuery(StrSql);
									String history_actiontype = "CLOSING_DATE";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Days Left: " + (int) (getDaysBetween(ToShortDate(kknow()), ConvertShortDateToStr(value)));
									// StrHTML = "Days Left: " +
									// Integer.toString((int)
									// getDaysBetween(ToShortDate(kknow()),
									// ConvertShortDateToStr(value)));

									StrHTML = StrHTML + "<br>Close Date Updated!";
								}
								EnquiryPriorityUpdate(comp_id, enquiry_id);
							} else {
								StrHTML = "Enter Close Date!";
							}
						}

						if (name.equals("txt_enquiry_title")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								// SOP("value==" + value);
								String history_oldvalue = ExecuteQuery("SELECT enquiry_title"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_title = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "TITLE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Title Updated!";
							} else {
								StrHTML = "Enter Title!";
							}
						}
						if (name.equals("txt_enquiry_desc")) {
							// SOPInfo("txt_enquiry_desc==");
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_desc"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_desc = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "DESCRIPTION";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue) "
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Description Updated!";
							} else {
								StrHTML = "Enter Description!";
							}
						}

						if (name.equals("txt_enquiry_value")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_value"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_value = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "BUDGET";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Budget Updated!";
							} else {
								StrHTML = "Enter Budget!";
							}
						}

						// Model list starts here
						if (name.equals("dr_enquiry_model_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT model_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ "INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_model_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_model_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT model_name"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
										+ " WHERE model_id=" + value + " ");
								String history_actiontype = "MODEL";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Model Updated!";
								// if (value.equals("0")) {
								// StrSql = "UPDATE " + compdb(comp_id) +
								// "axela_sales_enquiry"
								// + " SET"
								// + " enquiry_stage_id = 1"
								// + " WHERE enquiry_id = " + enquiry_id + "";
								// updateQuery(StrSql);
								// StrHTML = StrHTML + "<br>Stage Updated!";
								// } else {
								// StrSql = "UPDATE " + compdb(comp_id) +
								// "axela_sales_enquiry"
								// + " SET"
								// + " enquiry_stage_id = 2"
								// + " WHERE enquiry_id = " + enquiry_id + "";
								// updateQuery(StrSql);
								// StrHTML = StrHTML + "<br>Stage Updated!";
								// }
							}
							// else {
							// StrSql = "UPDATE " + compdb(comp_id) +
							// "axela_sales_enquiry"
							// + " SET"
							// + " enquiry_stage_id = 1"
							// + " WHERE enquiry_id = " + enquiry_id + "";
							// updateQuery(StrSql);
							// StrHTML = StrHTML + "<br>Stage Updated!";
							// StrHTML = "Select Model!";
							// }
						}

						if (name.equals("dr_enquiry_item_id")) {
							// StrHTML = "";
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT item_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_item_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT item_name FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_id=" + value + " ");

								String history_actiontype = "VARIANT";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Variant Updated!";
							} else {
								StrHTML = "Select Variant!";
							}
						}

						if (name.equals("dr_enquiry_add_model_id")) {
							// if (!value.equals("0")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT model_name"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = enquiry_add_model_id"
									+ " WHERE enquiry_id=" + enquiry_id + " ");

							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_add_model_id = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("SELECT model_name"
									+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
									+ " WHERE model_id=" + value + " ");
							String history_actiontype = "ADDITIONAL MODEL";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Additional Model Updated!";
							// } else {
							// StrHTML = "Select Age!";
							// }
						}

						if (name.equals("dr_enquiry_option_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT option_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option ON option_id = enquiry_option_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_option_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT option_name"
										+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
										+ " WHERE option_id=" + value + " ");

								String history_actiontype = "COLOUR";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Colour Updated!";
							} else {
								StrHTML = "Select Colour!";
							}
						}

						if (name.equals("dr_enquiry_custtype_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT custtype_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_custtype ON custtype_id = enquiry_custtype_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_custtype_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT custtype_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_custtype"
										+ " WHERE custtype_id=" + value + " ");

								String history_actiontype = "Customer Type";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Customer Type Updated!";
							} else {
								StrHTML = "Select Customer Type!";
							}
						}

						// Category Starts
						if (name.equals("dr_enquiry_enquirycat_id")) {

							if (!value.equals("") || value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquirycat_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN axela_sales_enquiry_cat ON enquirycat_id = enquiry_enquirycat_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_enquirycat_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquirycat_name"
										+ " FROM axela_sales_enquiry_cat"
										+ " WHERE enquirycat_id=" + value + " ");

								String history_actiontype = "CATEGORY";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Category Updated!";
							}
							// else {
							// ///StrHTML = "Select Category!";
							// }
						}
						// Category Ends

						// Corporate Starts
						if (name.equals("dr_enquiry_corporate_id")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT corporate_name"
									+ " FROM " + compdb(comp_id) + " axela_sales_enquiry"
									+ " INNER JOIN " + compdb(comp_id) + " axela_sales_enquiry_corporate ON corporate_id = enquiry_corporate_id"
									+ " WHERE enquiry_id=" + enquiry_id + " ");

							StrSql = "UPDATE " + compdb(comp_id) + " axela_sales_enquiry"
									+ " SET enquiry_corporate_id = " + value + ""
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("SELECT corporate_name"
									+ " FROM " + compdb(comp_id) + " axela_sales_enquiry_corporate"
									+ " WHERE corporate_id=" + value + " ");
							String history_actiontype = "CORPORATE";
							StrSql = "INSERT INTO " + compdb(comp_id) + " axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Corporate Updated!";
						}
						// Corporate Ends

						if (name.equals("dr_enquiry_age_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT age_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_age ON age_id = enquiry_age_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_age_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT age_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_age"
										+ " WHERE age_id=" + value + " ");

								String history_actiontype = "AGE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Age Updated!";
							} else {
								StrHTML = "Select Age!";
							}
						}

						if (name.equals("dr_enquiry_occ_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT occ_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_occ ON occ_id = enquiry_occ_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_occ_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT occ_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_occ"
										+ " WHERE occ_id=" + value + " ");

								String history_actiontype = "OCCUPATION";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Occupation Updated!";
							} else {
								StrHTML = "Select Occupation!";
							}
						}

						/*
						 * if (name.equals("txt_enquiry_custid")) { if (!value.equals("")) { value = value.replaceAll("nbsp", "&"); String history_oldvalue = ExecuteQuery("SELECT enquiry_custid" +
						 * " FROM " + compdb(comp_id) + "axela_sales_enquiry" + " WHERE enquiry_id=" + enquiry_id + " "); StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry" + " SET" +
						 * " enquiry_custid = '" + value + "'" + " WHERE enquiry_id = " + enquiry_id + ""; updateQuery(StrSql); String history_actiontype = "ID";
						 * 
						 * StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history" + " (history_enquiry_id," + " history_emp_id," + " history_datetime," + " history_actiontype," +
						 * " history_oldvalue," + " history_newvalue)" + " VALUES (" + " '" + enquiry_id + "'," + " '" + emp_id + "'," + " '" + ToLongDate(kknow()) + "'," + " '" + history_actiontype +
						 * "'," + " '" + history_oldvalue + "'," + " '" + value + "')"; updateQuery(StrSql); StrHTML = "ID Updated!"; } else { StrHTML = "Enter ID!"; } }
						 */

						if (name.equals("txt_enquiry_fuelallowance")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_fuelallowance"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_fuelallowance = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "FUEL ALLOWANCE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Fuel Allowance Updated!";
							} else {
								StrHTML = "Enter Fuel Allowance!";
							}
						}

						// /////////
						if (name.equals("preownedvariant2")) {
							if (!value.equals("") || value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT variant_name "
										+ " FROM axela_preowned_variant "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_tradein_preownedvariant_id = variant_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								if (history_oldvalue.equals("0")) {
									history_oldvalue = "";
								}
								// SOP("SELECT variant_name "
								// + " from axela_preowned_variant "
								// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_tradein_preownedvariant_id = variant_id "
								// + " WHERE enquiry_id=" + enquiry_id + " ");
								// SOP("history_oldvalue-----------" + history_oldvalue);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_tradein_preownedvariant_id = 0" + value + ""
										+ " WHERE enquiry_id = " + enquiry_id + "";
								// SOP("StrSql----------" + StrSql);
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT variant_name from axela_preowned_variant WHERE variant_id='" + value + "'");
								// SOP("history_newvalue-----------" + history_newvalue);
								if (history_newvalue.equals("0")) {
									history_newvalue = "";
								}
								String history_actiontype = "TRADE-IN MODEL";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Trade-In Model Updated!";
							}
							// else {
							// StrHTML = "Select Trade-In Model!";
							// }
						}
						// //////
						// SOP("name-----------" + name);
						if (name.equals("preownedvariant1")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT variant_name "
										+ " FROM axela_preowned_variant "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_preownedvariant_id = variant_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								// SOP("history_oldvalue-----------" + history_oldvalue);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_preownedvariant_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								// SOP("StrSql----------" + StrSql);
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT variant_name"
										+ " FROM axela_preowned_variant"
										+ " WHERE variant_id=" + value + " ");
								// SOP("history_newvalue-----------" + history_newvalue);
								String history_actiontype = "Pre-Owned Model";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Pre-Owned Model Updated!";
							} else {
								StrHTML = "Select Pre-Owned Model!";
							}
						}
						// ///////

						// Start maruti
						if (name.equals("dr_enquiry_buyertype_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT buyertype_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype ON buyertype_id = enquiry_buyertype_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_buyertype_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT buyertype_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
										+ " WHERE buyertype_id=" + value + " ");

								String history_actiontype = "BUYER TYPE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Buyer Type Updated!";
							} else {
								StrHTML = "Select Buyer Type!";
							}
						}

						if (name.equals("dr_enquiry_monthkms_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT monthkms_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms ON monthkms_id = enquiry_monthkms_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_monthkms_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT monthkms_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_monthkms"
										+ " WHERE monthkms_id=" + value + " ");

								String history_actiontype = "MONTH KMS";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Month Kms Updated!";
							} else {
								StrHTML = "Select Month Kms!";
							}
						}

						if (name.equals("dr_enquiry_purchasemode_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT purchasemode_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode ON purchasemode_id = enquiry_purchasemode_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_purchasemode_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT purchasemode_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_purchasemode"
										+ " WHERE purchasemode_id=" + value + " ");

								String history_actiontype = "PURCHASE MODE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Purchase Mode Updated!";
							} else {
								StrHTML = "Select Purchase Mode!";
							}
						}

						if (name.equals("dr_enquiry_income_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT income_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_income ON income_id = enquiry_income_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_income_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT income_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_income"
										+ " WHERE income_id=" + value + " ");

								String history_actiontype = "INCOME";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Income Updated!";
							} else {
								StrHTML = "Select Income!";
							}
						}

						if (name.equals("txt_enquiry_familymember_count")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_familymember_count"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_familymember_count = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "FAMILY MEMBERS";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Family Members Updated!";
							} else {
								StrHTML = "Enter Family Members!";
							}
						}

						if (name.equals("dr_enquiry_expectation_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT expectation_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_expectation ON expectation_id = enquiry_expectation_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_expectation_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT expectation_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_expectation"
										+ " WHERE expectation_id=" + value + " ");

								String history_actiontype = "EXPECTATION";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Expectation Updated!";
							} else {
								StrHTML = "Select Expectation!";
							}
						}

						if (name.equals("txt_enquiry_othercar")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_othercar"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_othercar = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "OTHER CAR";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Other Car Updated!";
							} else {
								StrHTML = "Enter Other Car!";
							}
						}

						if (name.equals("dr_enquiry_corporate")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_corporate"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_corporate = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Corporate";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Corporate Updated!";
							}
							// else {
							// StrHTML = "Enter Other Car!";
							// }
						}

						if (name.equals("dr_enquiry_ownership_id")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT ownership_name"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
									+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_add_ownership ON ownership_id = enquiry_ownership_id "
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ownership_id = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("SELECT ownership_name"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_ownership"
									+ " WHERE ownership_id=" + value + " ");

							String history_actiontype = "OWNERSHIP";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Ownership Updated!";
							// } else {
							// StrHTML = "Select Ownership!";
							// }
						}

						if (name.equals("txt_enquiry_existingvehicle")) {
							// if (enquiry_buyertype_id.equals("2") &&
							// !enquiry_existingvehicle.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_existingvehicle"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_existingvehicle = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "EXISTING VEHICLE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Existing Vehicle Updated!";
							// } else {
							// StrHTML = "Enter Existing Vehicle!";
							// }
						}

						if (name.equals("txt_enquiry_purchasemonth")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_purchasemonth"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							if (!history_oldvalue.equals("")) {
								history_oldvalue = strToShortDate(history_oldvalue);
							}
							String history_newvalue = value;
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_purchasemonth = '" + ConvertShortDateToStr(value) + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							// SOP("StrSql----------"+StrSql);
							updateQuery(StrSql);
							String history_actiontype = "PURCHASED MONTH";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Purchased Month Updated!";
							// }
							// else {
							// StrHTML = "Enter Purchased Month!";
							// }
						}

						if (name.equals("txt_enquiry_loancompletionmonth")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_loancompletionmonth"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_loancompletionmonth = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "LOAN COMPLETION MONTH";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Loan Completion Month Updated!";
							// } else {
							// StrHTML = "Enter Loan Completion Month!";
							// }
						}

						if (name.equals("txt_enquiry_currentemi")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (value.equals("")) {
								value = "0";
							}
							String history_oldvalue = ExecuteQuery("SELECT enquiry_currentemi"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_currentemi = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CURRENT EMI";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Current EMI Updated!";
							// } else {
							// StrHTML = "Enter Current EMI!";
							// }
						}

						if (name.equals("txt_enquiry_loanfinancer")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_loanfinancer"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_loanfinancer = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "LOAN FINANCER";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Loan Financer Updated!";
							// } else {
							// StrHTML = "Enter Loan Financer!";
							// }
						}

						if (name.equals("txt_enquiry_kms")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (value.equals("")) {
								value = "0";
							}
							String history_oldvalue = ExecuteQuery("SELECT enquiry_kms"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_kms = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "KMS";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Kms Updated!";
							// } else {
							// StrHTML = "Enter Kms!";
							// }
						}

						if (name.equals("txt_enquiry_expectedprice")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (value.equals("")) {
								value = "0";
							}
							String history_oldvalue = ExecuteQuery("SELECT enquiry_expectedprice"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_expectedprice = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "EXPECTED PRICE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Expected Price Updated!";
							// } else {
							// StrHTML = "Enter Expected Price!";
							// }
						}

						if (name.equals("txt_enquiry_quotedprice")) {
							value = value.replaceAll("nbsp", "&");
							if (value.equals("")) {
								value = "0";
							}
							String history_oldvalue = ExecuteQuery("SELECT enquiry_quotedprice"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_quotedprice = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "QUOTED PRICE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Quoted Price Updated!";
							// } else {
							// StrHTML = "Enter Quoted Price!";
							// }
						}

						if (name.equals("chk_enquiry_evaluation")) {
							// if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_evaluation"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_evaluation = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "Evaluation";
							if (history_oldvalue.equals("1")) {
								history_oldvalue = "YES";
							} else {
								history_oldvalue = "NO";
							}

							if (value.equals("1")) {
								value = "YES";
							} else {
								value = "NO";
							}
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							// SOP("history======" + StrSql);
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Evaluation Updated!";

						}

						if (name.equals("dr_enquiry_emp_id")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT emp_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_emp_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_crm"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = crm_enquiry_id "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_crmdays ON crmdays_id = crm_crmdays_id"
										+ " SET crm_emp_id = COALESCE ((SELECT CASE "
										+ " WHEN crmdays_exe_type = 1 THEN "
										+ " (CASE WHEN crmdays_crmtype_id = 1 THEN team_crm_emp_id "
										+ " WHEN crmdays_crmtype_id = 2 THEN team_pbf_emp_id"
										+ " WHEN crmdays_crmtype_id = 3 THEN team_psf_emp_id END)"
										+ " WHEN crmdays_exe_type = 2 THEN enquiry_emp_id"
										+ " WHEN crmdays_exe_type = 3 THEN team_emp_id"
										+ " END"
										+ " FROM " + compdb(comp_id) + "axela_sales_team "
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
										+ " WHERE team_branch_id = enquiry_branch_id"
										+ " AND teamtrans_emp_id = enquiry_emp_id"
										+ " LIMIT 1),0)"
										+ " WHERE 1 = 1 "
										+ " AND crm_desc = ''"
										+ " AND enquiry_id = " + enquiry_id;
								updateQuery(StrSql);
								// SOP("StrSql--------crm-----------" + StrSql);

								StrSql = " UPDATE " + compdb(comp_id) + "axela_sales_enquiry_followup"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_id = followup_enquiry_id"
										+ " SET followup_emp_id = '" + value + "'"
										+ " WHERE followup_desc = ''"
										+ " AND enquiry_id = " + enquiry_id;
								// SOP("strsql========" + StrSql);
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT emp_name"
										+ " FROM " + compdb(comp_id) + "axela_emp"
										+ " WHERE emp_id=" + value + " ");

								String history_actiontype = "SALES CONSULTANT";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Sales Consultant Updated!";
							} else {
								StrHTML = "Select Sales Consultant!";
							}
						}

						// if (name.equals("dr_enquiry_project_id")) {
						// if (!value.equals("")) {
						// value = value.replaceAll("nbsp", "&");
						// String history_oldvalue =
						// ExecuteQuery("SELECT coalesce(project_title,'') from " +
						// compdb(comp_id) + "axela_sales_enquiry left join " +
						// compdb(comp_id) +
						// "axela_project ON project_id = enquiry_project_id WHERE enquiry_id="
						// + enquiry_id + " ");
						// StrSql = "UPDATE " + compdb(comp_id) +
						// "axela_sales_enquiry"
						// + " SET"
						// + " enquiry_project_id = '" + value + "'"
						// + " WHERE enquiry_id = " + enquiry_id + "";
						// updateQuery(StrSql);
						// String history_newvalue =
						// ExecuteQuery("SELECT project_title from " +
						// compdb(comp_id) + "axela_project WHERE project_id=" +
						// value + " ");
						// String history_actiontype = "PROJECT";
						//
						// StrSql = "INSERT INTO " + compdb(comp_id) +
						// "axela_sales_enquiry_history"
						// + " (history_enquiry_id,"
						// + " history_emp_id,"
						// + " history_datetime,"
						// + " history_actiontype,"
						// + " history_oldvalue,"
						// + " history_newvalue)"
						// + " VALUES ("
						// + " '" + enquiry_id + "',"
						// + " '" + emp_id + "',"
						// + " '" + ToLongDate(kknow()) + "',"
						// + " '" + history_actiontype + "',"
						// + " '" + history_oldvalue + "',"
						// + " '" + history_newvalue + "')";
						// updateQuery(StrSql);
						// StrHTML = "Project Updated!";
						// } else {
						// StrHTML = "Select Project!";
						// }
						// }
						if (name.equals("chk_enquiry_avpresent")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_avpresent"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								if (history_oldvalue.equals("1")) {
									history_oldvalue = "1";
								} else {
									history_oldvalue = "0";
								}
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_avpresent = '" + value + "'"
										+ " WHERE enquiry_id=" + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_avpresent"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								if (history_newvalue.equals("1")) {
									history_newvalue = "1";
								} else {
									history_newvalue = "0";
								}
								String history_actiontype = "AV_PRESENTATION";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "AV Presentation Updated!";
							}
						}

						if (name.equals("chk_enquiry_manager_assist")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_manager_assist"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								if (history_oldvalue.equals("1")) {
									history_oldvalue = "1";
								} else {
									history_oldvalue = "0";
								}
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_manager_assist = '" + value + "'"
										+ " WHERE enquiry_id=" + enquiry_id + "";
								updateQuery(StrSql);

								String history_newvalue = ExecuteQuery("SELECT enquiry_manager_assist"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								if (history_newvalue.equals("1")) {
									history_newvalue = "1";
								} else {
									history_newvalue = "0";
								}
								String history_actiontype = "MANAGER_ASSISTANCE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Manager Assistance Updated!";
							}
						}

						if (name.equals("txt_enquiry_customer_name")) {
							if (!value.equals("")) {
								if (value.length() < 3) {
									StrHTML = ("Enter atleast 3 Characters for Customer Name!");
									return;
								}
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT COALESCE(customer_name,'') AS customer_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
										+ " WHERE enquiry_id = " + enquiry_id);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_customer_id = customer_id"
										+ " SET"
										+ " customer_name = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CUSTOMER";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Customer Name Updated!";
							} else {
								StrHTML = "Enter Customer Name!";
							}
						}

						if (name.equals("dr_title")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT title_desc "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
										+ " SET"
										+ " contact_title_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_newvalue = ExecuteQuery("SELECT title_desc "
										+ "FROM " + compdb(comp_id) + "axela_title"
										+ " WHERE title_id=" + value + " ");

								String history_actiontype = "CONTACT_TITLE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Title Updated!";
							} else {
								StrHTML = "Select Title!";
							}
						}

						if (name.equals("txt_contact_fname")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT contact_fname "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " SET"
										+ " contact_fname = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CONTACT_FIRST_NAME";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Contact First Name Updated!";
							} else {
								StrHTML = "Enter Contact First Name!";
							}
						}

						if (name.equals("txt_contact_lname")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT contact_lname "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " SET"
										+ " contact_lname = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CONTACT_LAST_NAME";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Contact Last Name Updated!";
							} else {
								StrHTML = "Enter Contact Last Name!";
							}
						}

						if (name.equals("txt_contact_jobtitle")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT contact_jobtitle "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " SET"
										+ " contact_jobtitle = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CONTACT_JOBTITLE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Contact Job Title Updated!";
							}
						}

						if (name.equals("txt_contact_mobile1")) {
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id "
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " WHERE enquiry_status_id = 1 "
										+ " AND (contact_mobile1 = '" + value + "' OR contact_mobile2 = '" + value + "')"
										+ " AND enquiry_branch_id = " + enquiry_branch_id
										+ " AND enquiry_id!=" + enquiry_id;
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Mobile 1 Found!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT contact_mobile1 "
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
											+ " WHERE enquiry_id=" + enquiry_id + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
											+ " SET"
											+ " contact_mobile1 = '" + value + "',"
											+ " customer_mobile1 = '" + value + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									String history_actiontype = "CONTACT_MOBILE_1";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id, "
											+ " history_emp_id, "
											+ " history_datetime, "
											+ " history_actiontype, "
											+ " history_oldvalue, "
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Contact Mobile 1 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Mobile 1!</font>";
							}
						}
						if (name.equals("txt_contact_mobile2")) {
							String history_oldvalue = "";
							String history_actiontype = "";
							if (!value.equals("") && IsValidMobileNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id "
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " WHERE enquiry_status_id = 1 "
										+ " AND (contact_mobile1 = '" + value + "' OR contact_mobile2 = '" + value + "')"
										+ " AND enquiry_branch_id = " + enquiry_branch_id
										+ " AND enquiry_id!=" + enquiry_id;
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Mobile 2 Found!";
								} else if (!value.equals("") && IsValidMobileNo11(value)) {
									history_oldvalue = ExecuteQuery("SELECT contact_mobile2 "
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
											+ " WHERE enquiry_id=" + enquiry_id + " ");

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
											+ " SET"
											+ " contact_mobile2 = '" + value + "',"
											+ " customer_mobile2 = '" + value + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									history_actiontype = "CONTACT_MOBILE_2";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id, "
											+ " history_emp_id, "
											+ " history_datetime, "
											+ " history_actiontype, "
											+ " history_oldvalue, "
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Contact Mobile 2 Updated!";
								}

							} else if (!value.equals("") && !IsValidMobileNo11(value)) {
								StrHTML = "<font color=\"red\">Enter Valid Contact Mobile2!</font>";
							} else if (value.equals("")) {
								history_oldvalue = ExecuteQuery("SELECT contact_mobile2 "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
										+ " SET"
										+ " contact_mobile2 = '" + value + "',"
										+ " customer_mobile2 = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id, "
										+ " history_emp_id, "
										+ " history_datetime, "
										+ " history_actiontype, "
										+ " history_oldvalue, "
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Contact Mobile2 Updated!";
							}
						}

						if (name.equals("txt_contact_phone1")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id "
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " WHERE enquiry_status_id = 1 "
										+ " AND (contact_phone1 = '" + value + "' OR contact_phone2 = '" + value + "')"
										+ " AND enquiry_branch_id = " + enquiry_branch_id;
								// + " and enquiry_id!=" + enquiry_id;
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "<font color=\"red\">Similar Phone 1 Found!</font>";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT contact_phone1 "
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
											+ " WHERE enquiry_id=" + enquiry_id + " ");

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
											+ " SET"
											+ " contact_phone1 = '" + value + "',"
											+ " customer_phone1 = '" + value + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";

									updateQuery(StrSql);

									String history_actiontype = "CONTACT_PHONE_1";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id, "
											+ " history_emp_id, "
											+ " history_datetime, "
											+ " history_actiontype, "
											+ " history_oldvalue, "
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Contact Phone 1 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Phone 1!</font>";
							}
						}

						if (name.equals("txt_contact_phone2")) {
							if (!value.equals("") && IsValidPhoneNo11(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id "
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " WHERE enquiry_status_id = 1 "
										+ " AND (contact_phone1 = '" + value + "' OR contact_phone2 = '" + value + "')"
										+ " AND enquiry_branch_id = " + enquiry_branch_id;
								// + " and enquiry_id!=" + enquiry_id;
								// SOP(StrSql);
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "<font color=\"red\">Similar Phone 2 Found!</font>";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT contact_phone2"
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
											+ " WHERE enquiry_id=" + enquiry_id + " ");

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
											+ " SET"
											+ " contact_phone2 = '" + value + "',"
											+ " customer_phone2 = '" + value + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									String history_actiontype = "CONTACT_PHONE_2";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id, "
											+ " history_emp_id, "
											+ " history_datetime, "
											+ " history_actiontype, "
											+ " history_oldvalue, "
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Contact Phone 2 Updated!";
								}
							} else if (!value.equals("") && !IsValidPhoneNo11(value)) {
								StrHTML = "<font color=\"red\">Enter Valid Contact Phone 2!</font>";
							}
						}

						if (name.equals("txt_contact_email1")) {
							if (!value.equals("") && IsValidEmail(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id "
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " WHERE enquiry_status_id = 1"
										+ " AND contact_email1 = '" + value + "'"
										+ " AND enquiry_branch_id = " + enquiry_branch_id
										+ " AND enquiry_id!=" + enquiry_id;
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Email 1 Found!!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT contact_email1 "
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
											+ " WHERE enquiry_id=" + enquiry_id + " ");

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
											+ " SET"
											+ " contact_email1 = '" + value + "',"
											+ " customer_email1 = '" + value + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									String history_actiontype = "CONTACT_EMAIL_1";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id, "
											+ " history_emp_id, "
											+ " history_datetime, "
											+ " history_actiontype, "
											+ " history_oldvalue, "
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Contact Email 1 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Contact Email 1!</font>";
							}
						}
						if (name.equals("txt_contact_email2")) {
							if (!value.equals("") && IsValidEmail(value)) {
								value = value.replaceAll("nbsp", "&");
								StrSql = "SELECT contact_id "
										+ " FROM " + compdb(comp_id) + "axela_customer_contact"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
										+ " WHERE enquiry_status_id = 1"
										+ " AND contact_email2 = '" + value + "'"
										+ " AND enquiry_branch_id = " + enquiry_branch_id
										+ " AND enquiry_id!=" + enquiry_id;
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar Email 2 Found!!";
								} else {
									String history_oldvalue = ExecuteQuery("SELECT contact_email2 "
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
											+ " WHERE enquiry_id=" + enquiry_id + " ");

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
											+ " SET"
											+ " contact_email2 = '" + value + "',"
											+ " customer_email2 = '" + value + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									String history_actiontype = "CONTACT_EMAIL_2";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id, "
											+ " history_emp_id, "
											+ " history_datetime, "
											+ " history_actiontype, "
											+ " history_oldvalue, "
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Contact Email 2 Updated!";
								}
							} else {
								StrHTML = "<font color=\"red\">Enter Valid Contact Email 2!</font>";
							}
						}

						if (name.equals("txt_contact_address")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT contact_address "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
										+ " SET"
										+ " contact_address = '" + value + "',"
										+ " customer_address = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CONTACT_ADDRESS";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id, "
										+ " history_emp_id, "
										+ " history_datetime, "
										+ " history_actiontype, "
										+ " history_oldvalue, "
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Contact Address Updated!";
							} else {
								StrHTML = "Enter Contact Address!";
							}
						}

						if (name.equals("maincity")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT city_name "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
										+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = contact_city_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
										+ " SET"
										+ " contact_city_id = '" + value + "',"
										+ " customer_city_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT city_name from " + compdb(comp_id) + "axela_city WHERE city_id=" + value + " ");

								String history_actiontype = "CONTACT_CITY";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "City Updated!";
							} else {
								StrHTML = "Select City!";
							}
						}

						if (name.equals("txt_contact_pin")) {
							if (!value.equals("")) {
								if (!isNumeric(value)) {
									StrHTML = "Enter Valid Pin!";
								}
								else {
									value = value.replaceAll("nbsp", "&");
									String history_oldvalue = ExecuteQuery("SELECT contact_pin "
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id "
											+ " WHERE enquiry_id=" + enquiry_id + " ");

									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
											+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
											+ " SET"
											+ " contact_pin = '" + value + "',"
											+ " customer_pin = '" + value + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									String history_actiontype = "CONTACT_PIN";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id, "
											+ " history_emp_id, "
											+ " history_datetime, "
											+ " history_actiontype, "
											+ " history_oldvalue, "
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "Contact Pin Updated!";
								}
							} else {
								StrHTML = "Enter Contact Pin!";
							}
						}

						// ////////////////////////enquiry
						// Status//////////////////////////////
						if (name.equals("dr_enquiry_status_id")) {
							status_id = status_id.replaceAll("nbsp", "&");
							String Str = "";
							int dayslefttoclose = 0;
							// String enquiry_edit = ReturnPerm(comp_id,
							// "emp_close_enquiry", request);

							String enquiry_edit = CNumeric(ExecuteQuery("SELECT emp_close_enquiry"
									+ " FROM " + compdb(comp_id) + "axela_emp"
									+ " WHERE emp_id=" + emp_id));
							String enquiry_closedays_after = CNumeric(ExecuteQuery("SELECT brandconfig_closeenqafterdays"
									+ " FROM " + compdb(comp_id) + "axela_brand_config"
									+ " WHERE brandconfig_brand_id = " + branch_brand_id));
							if (!enquiry_closedays_after.equals("0")) {
								dayslefttoclose = Integer.parseInt(enquiry_closedays_after) - (int) getDaysBetween(enquiry_date, ToShortDate(kknow()));
							}
							// SOP("enquiry_closedays_after===" + enquiry_closedays_after);
							// SOP("enquiry_closedays_after===" + Integer.parseInt(enquiry_closedays_after));
							// SOP("coming...===" + !((int) getDaysBetween(enquiry_date, ToShortDate(kknow())) > Integer.parseInt(enquiry_closedays_after)));

							if (status_id.equals("2")) {
								Str = "<font color=\"red\">Update Permission Denied!</font><br>";
							}
							else if ((status_id.equals("3") || status_id.equals("4")) && enquiry_edit.equals("0")) {
								Str = "<font color=\"red\">Update Permission Denied!</font><br>";
							}
							else if (!enquiry_closedays_after.equals("0") && !status_id.equals("1")
									&& dayslefttoclose > 0) {
								Str = "<font color=\"red\">Enquiry Can be closed only after " + dayslefttoclose + " Days!</font><br>";
							}
							else {
								if (status_desc.equals("")) {
									Str = "<font color=\"red\">Enter Status Description!</font><br>";
								}
								if (status_id.equals("1") || status_id.equals("2")) {
									lostcase1 = "0";
									lostcase2 = "0";
									lostcase3 = "0";
								} else {
									if (lostcase1.equals("0")) {
										Str = Str + "Select Lost Case 1!<br>";
									}
									StrSql = "SELECT lostcase2_id"
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
											+ " WHERE lostcase2_lostcase1_id = " + lostcase1 + "";
									if (!ExecuteQuery(StrSql).equals("")) {
										if (lostcase2.equals("0")) {
											Str = Str + "Select Lost Case 2!<br>";
										}
									}
									StrSql = "SELECT lostcase3_id"
											+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
											+ " WHERE lostcase3_lostcase2_id = " + lostcase2 + "";
									if (!ExecuteQuery(StrSql).equals("")) {
										if (lostcase3.equals("0")) {
											Str = Str + "Select Lost Case 3!";
										}
									}
								}
							}

							StrHTML = Str;
							if (StrHTML.equals("")) {
								String historyoldvalue_status_name = "";
								String historyoldvalue_status_id = "";
								String historyoldvalue_lostcase1 = "", historyoldvalue_lostcase2 = "", historyoldvalue_lostcase3 = "";
								String historynewvalue_status_name = "";
								String historynewvalue_lostcase1 = "", historynewvalue_lostcase2 = "", historynewvalue_lostcase3 = "";
								String historyactiontype_status_name = "";
								String historyactiontype_lostcase1 = "", historyactiontype_lostcase2 = "", historyactiontype_lostcase3 = "";
								CachedRowSet crs = null;

								StrSql = "SELECT enquiry_status_id, status_name,"
										+ " COALESCE(lostcase1_name, '') lostcase1_name,"
										+ " COALESCE(lostcase2_name,'') lostcase2_name,"
										+ " COALESCE(lostcase3_name, '') lostcase3_name "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id  = enquiry_lostcase1_id"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id  = enquiry_lostcase2_id"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_id  = enquiry_lostcase3_id"
										+ " WHERE enquiry_id = " + enquiry_id + " ";
								// SOP("StrSql----------" + StrSql);
								crs = processQuery(StrSql, 0);
								while (crs.next()) {
									historyoldvalue_status_id = crs.getString("enquiry_status_id");
									historyoldvalue_status_name = crs.getString("status_name");
									historyoldvalue_lostcase1 = crs.getString("lostcase1_name");
									historyoldvalue_lostcase2 = crs.getString("lostcase2_name");
									historyoldvalue_lostcase3 = crs.getString("lostcase3_name");
								}
								crs.close();
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET enquiry_status_id = " + status_id + ","
										+ " enquiry_status_date = " + ToLongDate(kknow()) + ","
										+ " enquiry_lostcase1_id = " + lostcase1 + ","
										+ " enquiry_lostcase2_id = " + lostcase2 + ","
										+ " enquiry_lostcase3_id = " + lostcase3 + ""
										+ " WHERE enquiry_id = " + enquiry_id;
								// SOP("StrSql==UPDATE" + StrSql);
								updateQuery(StrSql);

								StrSql = "SELECT enquiry_status_id, status_name,"
										+ " COALESCE(lostcase1_name, '') lostcase1_name, "
										+ " COALESCE(lostcase2_name,'') lostcase2_name,"
										+ " COALESCE(lostcase3_name, '') lostcase3_name "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 ON lostcase1_id  = enquiry_lostcase1_id"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 ON lostcase2_id  = enquiry_lostcase2_id"
										+ " LEFT JOIN " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 ON lostcase3_id  = enquiry_lostcase3_id"
										+ " WHERE enquiry_id = " + enquiry_id + " ";
								crs = processQuery(StrSql, 0);
								while (crs.next()) {
									historynewvalue_status_name = crs.getString("status_name");
									historynewvalue_lostcase1 = crs.getString("lostcase1_name");
									historynewvalue_lostcase2 = crs.getString("lostcase2_name");
									historynewvalue_lostcase3 = crs.getString("lostcase3_name");
								}
								crs.close();

								historyactiontype_status_name = "STATUS";
								if (!status_id.equals(historyoldvalue_status_id)) {
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + historyactiontype_status_name + "',"
											+ " '" + historyoldvalue_status_name + "',"
											+ " '" + historynewvalue_status_name + "')";
									// SOP("StrSql==" + StrSql);
									updateQuery(StrSql);
								}

								if (Integer.parseInt(status_id) > 2) {
									historyactiontype_lostcase1 = "LOST CASE 1";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + historyactiontype_lostcase1 + "',"
											+ " '" + historyoldvalue_lostcase1 + "',"
											+ " '" + historynewvalue_lostcase1 + "')";
									// SOP("StrSql==" + StrSql);
									updateQuery(StrSql);

									historyactiontype_lostcase2 = "LOST CASE 2";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + historyactiontype_lostcase2 + "',"
											+ " '" + historyoldvalue_lostcase2 + "',"
											+ " '" + historynewvalue_lostcase2 + "')";
									// SOP("StrSql==" + StrSql);
									updateQuery(StrSql);

									historyactiontype_lostcase3 = "LOST CASE 3";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES"
											+ " ('" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + historyactiontype_lostcase3 + "',"
											+ " '" + historyoldvalue_lostcase3 + "',"
											+ " '" + historynewvalue_lostcase3 + "')";
									// SOP("StrSql==" + StrSql);
									updateQuery(StrSql);
								}
								// / Insert Lost Case CRM Follow-up
								if (Integer.parseInt(status_id) > 2) {
									String enquiry_date = ToLongDate(kknow());
									new Enquiry_Quickadd().AddCustomCRMFields(enquiry_id, enquiry_date, "lost", comp_id);
								}
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Status Updated!";
							}
						}

						// /////////////////////// Status Description //////////////
						if (name.equals("txt_enquiry_status_desc")) {
							value = value.replaceAll("nbsp", "&");
							// SOP("value=" + value);
							if (!value.equals("")) {
								String history_oldvalue = ExecuteQuery("SELECT enquiry_status_desc"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_status_desc = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "STATUS DESCRIPTION";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Status Description Updated!";
							} else {
								StrHTML = "<font color=\"red\">Enter Status Description!</font>";
							}
						}

						// //////////////populate lost case2////////
						if (name.equals("dr_enquiry_lostcase1_id")) {
							StringBuilder Str = new StringBuilder();
							String case2 = "";
							StrSql = "SELECT lostcase2_id, lostcase2_name"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
									+ " WHERE 1=1"
									+ " AND lostcase2_lostcase1_id = " + value
									+ " ORDER BY lostcase2_name";
							// SOP("StrSql=lost=" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							Str.append("<select name=\"dr_enquiry_lostcase2_id\" id=\"dr_enquiry_lostcase2_id\" class=\"form-control\""
									+ " onChange=\"populateLostCase3('dr_enquiry_lostcase2_id',this,'span_lostcase3'); StatusUpdate();\">\n");

							Str.append("<option value=0>Select</option>");

							while (crs.next()) {
								Str.append("<option value=").append(crs.getString("lostcase2_id"));
								Str.append(">").append(crs.getString("lostcase2_name")).append("</option>\n");
							}
							if (crs.isBeforeFirst()) {
								case2 = "<br>Select Lost Case 2!";
							}
							Str.append("</select>\n");
							crs.close();
							StrHTML = Str.toString();
							if (!case2.equals("")) {
								StrHTML += case2;
							}
						}

						// //////////////populate lost case3////////
						if (name.equals("dr_enquiry_lostcase2_id")) {
							StringBuilder Str = new StringBuilder();
							StrSql = "SELECT lostcase3_id, lostcase3_name"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
									+ " WHERE 1=1"
									+ " AND lostcase3_lostcase2_id = " + value
									+ " ORDER BY lostcase3_name";
							// SOP("StrSql=lost=" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							Str.append("<select name=\"dr_enquiry_lostcase3_id\" id=\"dr_enquiry_lostcase3_id\" class=\"form-control\""
									+ " onChange=\"StatusUpdate();\">\n");
							Str.append("<option value=0>Select</option>");
							while (crs.next()) {
								Str.append("<option value=").append(crs.getString("lostcase3_id"));
								Str.append(">").append(crs.getString("lostcase3_name")).append("</option>\n");
							}
							Str.append("</select>\n");
							crs.close();
							StrHTML = Str.toString();
						}

						if (name.equals("drop_priorityenquiry_id")) {

							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT priorityenquiry_name "
										+ "FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ "INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_priority ON priorityenquiry_id = enquiry_priorityenquiry_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_priorityenquiry_id = " + value + ""
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT priorityenquiry_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority"
										+ " WHERE priorityenquiry_id=" + value + " ");

								String history_actiontype = "PRIORITY";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Priority Updated!";
							} else {
								StrHTML = "Select Priority!";
							}
						}

						if (name.equals("txt_enquiry_dmsno")) {
							if (!value.equals("")) {
								StrSql = "SELECT enquiry_dmsno"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE 1=1 "
										+ " AND enquiry_branch_id = " + enquiry_branch_id
										+ " AND enquiry_id != " + enquiry_id
										+ " AND enquiry_dmsno = '" + value + "'";
								if (!ExecuteQuery(StrSql).equals("")) {
									StrHTML = "Similar DMS No. found!";
									return;
								}
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_dmsno"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_dmsno = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "DMS NO.";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "DMS No. Updated!";
							} else {
								StrHTML = "Enter DMS No. !";
							}
						}

						if (name.equals("txt_enquiry_notes")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_notes"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_notes = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "NOTES";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Notes Updated!";
							} else {
								StrHTML = "Enter Notes !";
							}
						}
						// for preowned
						// if (name.equals("modelvariant")) {
						// if (!value.equals("")) {
						// value = value.replaceAll("nbsp", "&");
						// StrSql = "SELECT variant_name, preownedmodel_name "
						// + " from " + compdb(comp_id) + "axela_sales_enquiry "
						// + " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id "
						// + " INNER JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id "
						// // + " INNER JOIN " + compdb(comp_id) +
						// // "axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id "
						// + " WHERE enquiry_id=" + enquiry_id;
						// CachedRowSet crs =processQuery(StrSql, 0);
						// while (crs.next()) {
						// variant_name = crs.getString("variant_name");
						// preownedmodel_name = crs.getString("preownedmodel_name");
						// }
						// preownedmodel_id = ExecuteQuery("SELECT preownedmodel_id from axela_preowned_variant"
						// + " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						// + " WHERE variant_id = " + value);
						//
						// StrSql = "update " + compdb(comp_id) + "axela_sales_enquiry"
						// + " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
						// + " INNER JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id"
						// + " SET preowned_variant_id = " + value + ","
						// + " preowned_preownedmodel_id =" + preownedmodel_id
						// + " WHERE enquiry_id =" + enquiry_id;
						// updateQuery(StrSql);
						// String history_newvalue1 = ExecuteQuery("SELECT preownedmodel_name "
						// + " from " + compdb(comp_id) + "axela_sales_enquiry"
						// + " INNER JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id "
						// + " WHERE enquiry_id = " + enquiry_id);
						// String history_newvalue2 = ExecuteQuery("SELECT variant_name "
						// + " from " + compdb(comp_id) + "axela_sales_enquiry"
						// + " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id "
						// + " WHERE enquiry_id = " + enquiry_id);
						//
						// String history_actiontype1 = "MODEL";
						// if (!preownedmodel_name.equals("")) {
						// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						// + " (history_enquiry_id,"
						// + " history_emp_id,"
						// + " history_datetime,"
						// + " history_actiontype,"
						// + " history_oldvalue,"
						// + " history_newvalue)"
						// + " VALUES ("
						// + " '" + enquiry_id + "',"
						// + " '" + emp_id + "',"
						// + " '" + ToLongDate(kknow()) + "',"
						// + " '" + history_actiontype1 + "',"
						// + " '" + preownedmodel_name + "',"
						// + " '" + history_newvalue1 + "')";
						// updateQuery(StrSql);
						// }
						// String history_actiontype2 = "VARIANT";
						// if (!variant_name.equals("")) {
						// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						// + " (history_enquiry_id,"
						// + " history_emp_id,"
						// + " history_datetime,"
						// + " history_actiontype,"
						// + " history_oldvalue,"
						// + " history_newvalue)"
						// + " VALUES ("
						// + " '" + enquiry_id + "',"
						// + " '" + emp_id + "',"
						// + " '" + ToLongDate(kknow()) + "',"
						// + " '" + history_actiontype2 + "',"
						// + " '" + variant_name + "',"
						// + " '" + history_newvalue2 + "')";
						// updateQuery(StrSql);
						// StrHTML = "Variant Updated!";
						// }
						// } else {
						// StrHTML = "Select Variant!";
						// }
						// }

						// for preowned
						// if (name.equals("dr_enquiry_preownedmodel_id")) {
						// if (!value.equals("0")) {
						// value = value.replaceAll("nbsp", "&");
						// String history_oldvalue = ExecuteQuery("SELECT preownedmodel_name from " + compdb(comp_id)
						// + "axela_sales_enquiry INNER JOIN axela_preowned_model ON preownedmodel_id = enquiry_preownedmodel_id WHERE enquiry_id=" + enquiry_id + " ");
						// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						// + " SET"
						// + " enquiry_preownedmodel_id = '" + value + "'"
						// + " WHERE enquiry_id = " + enquiry_id + "";
						// updateQuery(StrSql);
						// String history_newvalue = ExecuteQuery("SELECT preownedmodel_name from axela_preowned_model WHERE preownedmodel_id=" + value + " ");
						//
						// String history_actiontype = "TRUE-VALUE MODEL";
						//
						// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						// + " (history_enquiry_id,"
						// + " history_emp_id,"
						// + " history_datetime,"
						// + " history_actiontype,"
						// + " history_oldvalue,"
						// + " history_newvalue)"
						// + " VALUES ("
						// + " '" + enquiry_id + "',"
						// + " '" + emp_id + "',"
						// + " '" + ToLongDate(kknow()) + "',"
						// + " '" + history_actiontype + "',"
						// + " '" + history_oldvalue + "',"
						// + " '" + history_newvalue + "')";
						// updateQuery(StrSql);
						// StrHTML = "True-Value Model Updated!";
						// } else {
						// StrHTML = "<font color=\"red\">Select True-Value Model!</font>";
						// }
						// }

						if (name.equals("dr_enquiry_preownedvariant_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT variant_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_preownedvariant_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT variant_name"
										+ " FROM axela_preowned_variant"
										+ " WHERE variant_id=" + value + " ");

								String history_actiontype = "TRUE-VALUE VARIANT";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "True-Value Variant Updated!";
							} else {
								StrHTML = "<font color=\"red\">Select True-Value Variant!</font>";
							}
						}

						if (name.equals("dr_enquiry_fueltype_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT fueltype_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_fueltype ON fueltype_id = enquiry_fueltype_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_fueltype_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT fueltype_name"
										+ " FROM " + compdb(comp_id) + "axela_fueltype"
										+ " WHERE fueltype_id=" + value + " ");

								String history_actiontype = "FUELTYPE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Fuel Type Updated!";
							} else {
								StrHTML = "Select Fuel Type!";
							}
						}

						if (name.equals("dr_enquiry_prefreg_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT prefreg_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_prefreg ON prefreg_id = enquiry_prefreg_id"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_prefreg_id = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT prefreg_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_prefreg"
										+ " WHERE prefreg_id=" + value + " ");

								String history_actiontype = "PREFREG";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Prefered Reg. Updated!";
							} else {
								StrHTML = "Select Prefered Reg.!";
							}
						}

						if (name.equals("txt_enquiry_presentcar")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_presentcar "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										// + " INNER JOIN " + compdb(comp_id) +
										// "axela_account_contact ON contact_id = enquiry_contact_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_presentcar = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_actiontype = "PRESENT CAR";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Present Car Updated!";
							} else {
								StrHTML = "Enter Present Car!";
							}
						}

						if (name.equals("dr_enquiry_finance")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_finance "
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_finance = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								String history_actiontype = "Finance";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Finance Updated!";
							} else {
								StrHTML = "Finance Car!";
							}
						}

						// if (!enquiry_preownedmodel_id.equals("0") && name.equals("")) {
						// StringBuilder Str = new StringBuilder();
						// try {
						// // StrSql = "SELECT item_id, item_name"
						// // + " FROM " + compdb(comp_id) +
						// // "axela_inventory_item"
						// // + " WHERE 1=1 and item_type_id = 1 "
						// // + " and item_model_id = " + enquiry_model_id
						// // + " order by item_name";
						// // // SOP("StrSql==" + StrSql);
						// // CachedRowSet crs =processQuery(StrSql, 0);
						//
						// // Str.append("<option value=0>SELECT</option>");
						// StrSql = "SELECT variant_id, variant_name"
						// + " FROM axela_preowned_variant"
						// + " WHERE 1=1"
						// + " and variant_preownedmodel_id = " + enquiry_preownedmodel_id
						// + " order by variant_name";
						// // SOP("StrSql==" + StrSql);
						// CachedRowSet crs =processQuery(StrSql, 0);
						// Str.append("<select name=\"dr_enquiry_preownedvariant_id\" id=\"dr_enquiry_preownedvariant_id\" class=\"selectbox\" onChange=\"SecurityCheck('dr_enquiry_preownedvariant_id',this,'hint_dr_enquiry_preownedvariant_id');\" >");
						// while (crs.next()) {
						// Str.append("<option value=").append(crs.getString("variant_id")).append("");
						// Str.append(">").append(crs.getString("variant_name")).append("</option> \n");
						// }
						// Str.append("</select>");
						// crs.close();
						// String item = "";
						// item = ExecuteQuery("SELECT variant_id FROM axela_preowned_variant WHERE 1=1"
						// + " AND variant_preownedmodel_id = " + enquiry_preownedmodel_id
						// + " ORDER BY variant_name limit 1");
						// // SOP("item===select item_id FROM " +
						// // compdb(comp_id) +
						// // "axela_inventory_item WHERE 1=1 and item_type_id = 1 "
						// // + " AND item_model_id = " + enquiry_model_id
						// // + " ORDER BY item_name limit 1");
						// String history_oldvalue = ExecuteQuery("SELECT variant_name FROM " + compdb(comp_id) + "axela_sales_enquiry "
						// + " INNER JOIN axela_preowned_variant ON variant_id = enquiry_preownedvariant_id "
						// + " WHERE enquiry_id=" + enquiry_id + " ");
						// // SOP("history_oldvalue=="+history_oldvalue);
						// // SOP("SELECT item_name FROM " + compdb(comp_id) +
						// // "axela_sales_enquiry "
						// // + " INNER JOIN " + compdb(comp_id) +
						// // "axela_inventory_item ON item_id = enquiry_item_id "
						// // + " WHERE enquiry_id=" + enquiry_id + " ");
						// String history_newvalue = "";
						// if (!item.equals("")) {
						// StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						// + " SET"
						// + " enquiry_preownedvariant_id = '" + item + "'"
						// + " WHERE enquiry_id = " + enquiry_id + "";
						// updateQuery(StrSql);
						//
						// history_newvalue = ExecuteQuery("SELECT variant_name FROM axela_preowned_variant WHERE variant_id=" + item + " ");
						// }
						// String history_actiontype = "TRUE-VALUE VARIANT";
						//
						// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						// + " (history_enquiry_id,"
						// + " history_emp_id,"
						// + " history_datetime,"
						// + " history_actiontype,"
						// + " history_oldvalue,"
						// + " history_newvalue)"
						// + " VALUES ("
						// + " '" + enquiry_id + "',"
						// + " '" + emp_id + "',"
						// + " '" + ToLongDate(kknow()) + "',"
						// + " '" + history_actiontype + "',"
						// + " '" + history_oldvalue + "',"
						// + " '" + history_newvalue + "')";
						// updateQuery(StrSql);
						// } catch (Exception ex) {
						// SOPError("Axelaauto===" + this.getClass().getName());
						// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
						// }
						// StrHTML = Str.toString();
						// StrHTML = StrHTML + "<br>True-Value Variant Updated!";
						// }
						// // end preowned

						if (!enquiry_model_id.equals("") && name.equals("")) {
							StringBuilder Str = new StringBuilder();
							try {
								// StrSql = "SELECT item_id, item_name"
								// + " FROM " + compdb(comp_id) + "axela_inventory_item"
								// + " WHERE 1=1 and item_type_id = 1 "
								// + " AND item_model_id = " + enquiry_model_id
								// + " ORDER BY item_name";
								// // SOP("StrSql==" + StrSql);
								// CachedRowSet crs =processQuery(StrSql, 0);

								// Str.append("<option value=0>Select</option>");
								StrSql = "SELECT item_id, item_name"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE 1=1 and item_type_id = 1 "
										+ " AND item_model_id = " + enquiry_model_id
										+ " ORDER BY item_name";
								// SOP("StrSql=item=" + StrSql);
								CachedRowSet crs = processQuery(StrSql, 0);
								Str.append("<select name=\"dr_enquiry_item_id\" id=\"dr_enquiry_item_id\" class=\"form-control\" onChange=\"SecurityCheck('dr_enquiry_item_id',this,'hint_dr_enquiry_item_id');\" >");
								while (crs.next()) {
									Str.append("<option value=").append(crs.getString("item_id")).append("");
									Str.append(">").append(crs.getString("item_name")).append("</option> \n");
								}
								Str.append("</select>");
								crs.close();
								String Variant = "";
								Variant = ExecuteQuery("SELECT item_id as item_id"
										+ " FROM " + compdb(comp_id) + "axela_inventory_item"
										+ " WHERE 1=1 and item_type_id = 1 "
										+ " AND item_model_id = " + enquiry_model_id
										+ " ORDER BY item_name limit 1");
								// SOP("Variant==" + Variant);
								// SOP("Variant===select item_id FROM " +
								// compdb(comp_id) + "axela_inventory_item WHERE 1=1 AND item_type_id = 1 "
								// + " AND item_model_id = " + enquiry_model_id
								// + " ORDER BY item_name limit 1");
								String history_oldvalue = ExecuteQuery("SELECT item_name"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry "
										+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id "
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								// SOP("history_oldvalue==" + history_oldvalue);
								// SOP("SELECT item_name from " + compdb(comp_id) +
								// "axela_sales_enquiry "
								// + " INNER JOIN " + compdb(comp_id) +
								// "axela_inventory_item ON item_id = enquiry_item_id "
								// + " WHERE enquiry_id=" + enquiry_id + " ");
								String history_newvalue = "";
								if (!Variant.equals("")) {
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " SET"
											+ " enquiry_item_id = '" + Variant + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);
									history_newvalue = ExecuteQuery("SELECT item_name"
											+ " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE item_id=" + Variant + " ");
								}
								String history_actiontype = "VARIANT";
								//
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);

								// String history_oldvalue =
								// ExecuteQuery("SELECT enquiry_item_id from " +
								// compdb(comp_id) +
								// "axela_sales_enquiry WHERE enquiry_id=" +
								// enquiry_id + " ");
								// StrSql = "UPDATE " + compdb(comp_id) +
								// "axela_sales_enquiry"
								// + " SET"
								// + " enquiry_item_id = '" + value + "'"
								// + " WHERE enquiry_id = " + enquiry_id + "";
								// updateQuery(StrSql);
								// StrSql = "SELECT item_id, item_name"
								// + " from " + compdb(comp_id) +
								// "axela_inventory_item"
								// + " WHERE 1=1 and item_type_id = 1 "
								// + " and item_model_id = " + enquiry_model_id
								// + " order by item_name";
								// // SOP("StrSql==" + StrSql);
								// CachedRowSet crs =processQuery(StrSql, 0);
								// Str.append("<select name=\"dr_enquiry_item_id\" id=\"dr_enquiry_item_id\" class=\"selectbox\" onChange=\"SecurityCheck('dr_enquiry_item_id',this,'hint_dr_enquiry_item_id');\" >");
								// while (crs.next()) {
								// Str.append("<option value=").append(crs.getString("item_id")).append("");
								// Str.append(">").append(crs.getString("item_name")).append("</option> \n");
								// }
								// // Str.append("</select>");
							} catch (Exception ex) {
								SOPError("Axelaauto== " + this.getClass().getName());
								SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
							}
							StrHTML = Str.toString();
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = StrHTML + "<br>Variant Updated!";
							// return;

						}

						// Start Nexa Conditions
						if (name.equals("dr_enquiry_nexa_gender")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_nexa_gender"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_nexa_gender = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_nexa_gender"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_nexa_gender = '" + value + "'");

								String history_actiontype = "Gender";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Gender Is Updated!";
							}
						}

						if (name.equals("dr_enquiry_nexa_beveragechoice")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_nexa_beveragechoice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_nexa_beveragechoice = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_nexa_beveragechoice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_nexa_beveragechoice = '" + value + "'");

								String history_actiontype = "BEVERAGE CHOICE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Beverage Choice Is Updated!";
							}
						}

						if (name.equals("dr_enquiry_nexa_autocard")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_nexa_autocard"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_nexa_autocard = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_nexa_autocard"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_nexa_autocard = '" + value + "'");

								String history_actiontype = "INTERESTED IN AUTOCARD";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Interested In Autocard Is Updated!";
							}
						}

						if (name.equals("dr_enquiry_nexa_fueltype")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_nexa_fueltype"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_nexa_fueltype = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_nexa_fueltype"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_nexa_fueltype = '" + value + "'");

								String history_actiontype = "FUEL TYPE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Fuel Type Is Updated!";
							}
						}

						if (name.equals("dr_enquiry_nexa_specreq")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_nexa_specreq"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_nexa_specreq = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_nexa_specreq"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_nexa_specreq = '" + value + "'");

								String history_actiontype = "SPECIFIC REQUIREMENT FROM THE CAR";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Specific Requirement From The Car Is Updated!";
							}
						}

						if (name.equals("dr_enquiry_nexa_testdrivereq")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_nexa_testdrivereq"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_nexa_testdrivereq = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_nexa_testdrivereq"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_nexa_testdrivereq = '" + value + "'");

								String history_actiontype = "TEST DRIVE REQUIRED";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Test Drive Required Is Updated!";
							}
						}

						if (name.equals("txt_enquiry_nexa_testdrivereqreason")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_nexa_testdrivereqreason"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_nexa_testdrivereqreason = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_nexa_testdrivereqreason"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_nexa_testdrivereqreason = '" + value + "'");

								String history_actiontype = "REASON";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Reason Is Updated!";
							}
							else
							{
								StrHTML = "Enter Reason ";
							}
						}

						// / End Nexa Conditions

						// Start Hyundai Please choose one option?
						if (name.equals("dr_enquiry_hyundai_chooseoneoption")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_chooseoneoption"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_chooseoneoption = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_chooseoneoption"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_chooseoneoption = '" + value + "'");

								String history_actiontype = "CHOOSE ONE OPTION";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Please Choose One Option Is Updated!";
							}/*
							 * else { StrHTML = "<font color=\"red\">Select choose one option!</font>" ; }
							 */
						}
						// End Hyundai Please choose one option?

						// Start Hyundai How many kilometers you drive in a month?
						if (name.equals("dr_enquiry_hyundai_kmsinamonth")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_kmsinamonth"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_kmsinamonth = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_kmsinamonth"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_kmsinamonth = '" + value + "'");
								String history_actiontype = "HOW MANY KILOMETERS YOU DRIVE IN A MONTH?";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "How many kilometers you drive in a month is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Select How many kilometers you drive in a month!</font>" ; }
							 */
						}
						// End Hyundai How many kilometers you drive in a month?

						// Start Hyundai How many members are there in your family?
						if (name.equals("dr_enquiry_hyundai_membersinthefamily")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_membersinthefamily"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_membersinthefamily = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_membersinthefamily"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_membersinthefamily = '" + value + "'");

								String history_actiontype = "HOW MANY MEMBERS ARE THERE IN YOUR FAMILY?";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "How many members are there in your family is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Select  How many members are there in your family!</font>" ; }
							 */
						}
						// End Hyundai How many members are there in your family?

						// Start Hyundai What is your top most priority expectation
						// from your car?
						if (name.equals("dr_enquiry_hyundai_topexpectation")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_topexpectation"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_topexpectation = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_topexpectation"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_topexpectation = '" + value + "'");

								String history_actiontype = "WHAT IS YOUR TOP MOST PRIORITY EXPECTATION FROM YOUR CAR?";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Top most priority expectation from your car is Updated!";
							}/*
							 * else { StrHTML = "<font color=\"red\">Select What is your top most priority expectation from your car!</font>" ; }
							 */
						}
						// End What is your top most priority expectation from your
						// car?

						// Start Hyundai By when are you expecting to finalize your
						// new car?
						if (name.equals("dr_enquiry_hyundai_finalizenewcar")) {
							if (!value.equals("0")) {
								// value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_finalizenewcar"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_finalizenewcar = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_finalizenewcar"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_finalizenewcar = '" + value + "'");

								String history_actiontype = "BY WHEN ARE YOU EXPECTING TO FINALIZE YOUR NEW CAR?";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "By when are you expecting to finalize your new car is Updated!";
							}
							/*
							 * else { StrHTML = "<font color=\"red\">Select By when are you expecting to finalize your new car!</font>" ; }
							 */
						}
						// End By when are you expecting to finalize your new car?

						// Start Hyundai What will be your mode of purchase?
						if (name.equals("dr_enquiry_hyundai_modeofpurchase")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_modeofpurchase"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_modeofpurchase = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_modeofpurchase"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_modeofpurchase = '" + value + "'");

								String history_actiontype = "WHAT WILL BE YOUR MODE OF PURCHASE?";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Mode of purchase is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Select What will be your mode of purchase!</font>" ; }
							 */
						}
						// End What will be your mode of purchase?

						// Start Hyundai What is your appropriate annual household
						// income(INR)?
						if (name.equals("dr_enquiry_hyundai_annualincome")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_annualincome"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_annualincome = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_annualincome"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_annualincome = '" + value + "'");

								String history_actiontype = "WHAT IS YOUR APPROPRIATE ANNUAL HOUSEHOLD INCOME(INR)?";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Annual household income is Updated!";
							}/*
							 * else { StrHTML = "<font color=\"red\">Select What is your appropriate annual household income(INR)!</font>" ; }
							 */
						}
						// End What is your appropriate annual household
						// income(INR)?

						// Start EnquiryHyundaiOtherCars
						if (name.equals("txt_enquiry_hyundai_othercars")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_othercars"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_othercars = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_othercars"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_othercars = '" + value + "'");

								String history_actiontype = "WHICH OTHER CARS ARE YOU CONSIDERING?";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Which other cars are you considering is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Enter Which other cars are you considering!</font>" ; }
							 */
						}
						// End EnquiryHyundaiOtherCars

						// Start EnquiryHyundaiCurrentCars
						if (name.equals("txt_enquiry_hyundai_currentcars")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_currentcars"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_currentcars = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_currentcars"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_currentcars = '" + value + "'");

								String history_actiontype = "CURRENT CARS";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Current cars is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Enter Which other cars are you considering!</font>" ; }
							 */
						}
						// End EnquiryHyundaiCurrentCars

						// Start EnquiryHyundaiDOB
						if (name.equals("txt_enquiry_hyundai_dob")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								if (!isValidDateFormatShort(value)) {
									StrHTML = "<font color=\"red\">Enter Valid DOB!</font>";
									return;
								} else {
									String history_oldvalue = strToShortDate(ExecuteQuery("SELECT enquiry_hyundai_dob FROM " + compdb(comp_id) + "axela_sales_enquiry"
											+ " WHERE enquiry_id = " + enquiry_id + ""));
									StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
											+ " SET"
											+ " enquiry_hyundai_dob = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
											+ " WHERE enquiry_id = " + enquiry_id + "";
									updateQuery(StrSql);

									String history_actiontype = "DOB";

									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
											+ " (history_enquiry_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + enquiry_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									LastTimeUpdate(enquiry_id, comp_id);
									StrHTML = "DOB is Updated!";
								}
							}
						}
						// End EnquiryHyundaiDOB

						// Start EnquiryHyundaiExManuf
						if (name.equals("txt_enquiry_hyundai_ex_manuf")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_manuf"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_manuf = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_manuf"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_manuf = '" + value + "'");

								String history_actiontype = "MANUFACTURE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Manufacturer is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Enter Manufacturer!</font>"; }
							 */
						}
						// End EnquiryHyundaiExManuf

						// Start EnquiryHyundaiExModel
						if (name.equals("txt_enquiry_hyundai_ex_model")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_model"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_model = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_model"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_model = '" + value + "'");

								String history_actiontype = "MODEL / VARIANT";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Model / Variant is Updated!";
							}/*
							 * else { StrHTML = "<font color=\"red\">Enter Model!</font>"; }
							 */
						}
						// End EnquiryHyundaiExModel

						// Start HyundaiYearOfManufacture
						if (name.equals("dr_enquiry_hyundai_ex_manufyear")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_manufyear"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_manufyear = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_manufyear"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_manufyear = '" + value + "'");

								String history_actiontype = "YEAR OF MANUFACTURE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Year of Manufacture is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Select Year of Manufacture!</font>" ; }
							 */
						}
						// End HyundaiYearOfManufacture

						// Start Hyundai Purchase Month / Year
						if (name.equals("txt_enquiry_hyundai_ex_purchasedate")) {
							value = value.replaceAll("nbsp", "&");
							if ((!value.equals("") && !isValidDateFormatShort(value))) {
								StrHTML = "<font color=\"red\">Enter valid Purchase Month / Year!</font>";
								return;
							}
							String history_oldvalue = strToShortDate(ExecuteQuery("SELECT enquiry_hyundai_ex_purchasedate"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id = " + enquiry_id));
							if (!value.equals("") && isValidDateFormatShort(value)) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_purchasedate = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
							} else {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_purchasedate = ''"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
							}

							String history_actiontype = "PURCHASE MONTH / YEAR";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Purchase Month Year Updated!";
						}
						// End Hyundai Purchase Month / Year

						// Start EnquiryHyundaiexOwner
						if (name.equals("txt_enquiry_hyundai_ex_owner")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_owner"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_owner = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_owner"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_owner = '" + value + "'");

								String history_actiontype = "OWNER";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Owner is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Enter Owner!</font>"; }
							 */
						}
						// End EnquiryHyundaiexOwner

						// Start EnquiryHyundaiExLoanCompletion

						if (name.equals("txt_enquiry_hyundai_ex_loancompletion")) {
							value = value.replaceAll("nbsp", "&");
							if ((!value.equals("") && !isValidDateFormatShort(value))) {
								StrHTML = "<font color=\"red\">Enter Valid Loan completion Month / Year (if any)!</font>";
								return;
							}
							String history_oldvalue = strToShortDate(ExecuteQuery("SELECT enquiry_hyundai_ex_loancompletion"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id = " + enquiry_id));
							if (!value.equals("") && isValidDateFormatShort(value)) {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_loancompletion = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
							} else {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_loancompletion = ''"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
							}
							String history_actiontype = "LOAN COMPLETION MONTH / YEAR (IF ANY)";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES"
									+ " ('" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Loan completion is Updated!";
						}
						// End EnquiryHyundaiExLoanCompletion

						// Start EnquiryHyundaiExKmsdone
						if (name.equals("txt_enquiry_hyundai_ex_kmsdone")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_kmsdone"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_kmsdone = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_kmsdone"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_kmsdone = '" + value + "'");

								String history_actiontype = "KMS. DONE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Kms. Done is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Enter Kms Done!</font>"; }
							 */
						}
						// End EnquiryHyundaiExKmsdone

						// Start EnquiryHyundaiExFinancer
						if (name.equals("txt_enquiry_hyundai_ex_financer")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_financer"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_financer = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_financer"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_financer = '" + value + "'");

								String history_actiontype = "FINANCER NAME";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Financer Name is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Enter Financer Name!</font>"; }
							 */
						}
						// End EnquiryHyundaiExFinancer

						// Start EnquiryHyundaiExExpectedprice
						if (name.equals("txt_enquiry_hyundai_ex_expectedprice")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_expectedprice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_expectedprice = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_expectedprice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_expectedprice = '" + value + "'");

								String history_actiontype = "EXPECTED PRICE (INR)(A)";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Expected Price is Updated!";
							}/*
							 * else { StrHTML = "<font color=\"red\">Enter Expected Price!</font>"; }
							 */
						}
						// End EnquiryHyundaiExExpectedprice

						// Start EnquiryHyundaiExQuotedPrice
						if (name.equals("txt_enquiry_hyundai_ex_quotedprice")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_quotedprice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_hyundai_ex_quotedprice = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_hyundai_ex_quotedprice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_hyundai_ex_quotedprice = '" + value + "'");

								String history_actiontype = "QUOTED PRICE (INR)(B)";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Quoted Price is Updated!";
							} /*
							 * else { StrHTML = "<font color=\"red\">Enter Quoted Price!</font>"; }
							 */
						}
						// End EnquiryHyundaiExQuotedPrice

						// start ford customer type
						if (name.equals("dr_enquiry_ford_custtype_id")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_customertype"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_customertype = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_customertype"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_customertype = '" + value + "'");

								String history_actiontype = "Customer Type";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Customer Type Updated!";
							}
							else
							{
								StrHTML = "Select Customer Type!";
							}
						}
						// Start Ford PopulateFordIntentionPurchase
						if (name.equals("dr_enquiry_ford_intentionpurchase")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_intentionpurchase"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_intentionpurchase = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_intentionpurchase"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_intentionpurchase = '" + value + "'");

								String history_actiontype = "INTENTION TO PURCHASE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Intention to purchase is Updated!";
							}
						}

						if (name.equals("txt_enquiry_ford_kmsdriven")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_kmsdriven"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_kmsdriven = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "NO OF KMS DRIVEN EVERY DAY";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = " No. of Kms driven every day Updated!";
							} else {
								StrHTML = " Enter No. of Kms driven every day!";
							}
						}
						if (name.equals("dr_enquiry_ford_newvehfor")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_newvehfor"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_newvehfor = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_newvehfor"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_newvehfor = '" + value + "'");

								String history_actiontype = "NEW VEHICLE FOR SELF OR SOMEONE ELSE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "New vehicle for self or someone else is Updated!";
							}
						}

						if (name.equals("txt_enquiry_ford_investment")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_investment"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_investment = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "AMOUNT OF INVESTMENT IN NEW CAR";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Amount of investment in new car Updated!";

							} else {
								StrHTML = " Enter Amount of investment in new car!";
							}
						}
						if (name.equals("dr_enquiry_ford_colourofchoice")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_colourofchoice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_colourofchoice = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_colourofchoice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_colourofchoice = '" + value + "'");

								String history_actiontype = "ANY SPECIFIC COLOUR CHOICE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Specific Colour Choice Is Updated!";
							}
						}

						if (name.equals("dr_enquiry_ford_cashorfinance")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_cashorfinance"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_cashorfinance = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_cashorfinance"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_cashorfinance = '" + value + "'");

								String history_actiontype = "CASH/FINANCE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "   Cash / Finance is Updated!";
							}
						}
						if (name.equals("txt_enquiry_ford_currentcar")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_currentcar"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ford_currentcar = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "WHICH CAR YOU DRIVING NOW";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Which car you driving now Updated!";
						}

						if (name.equals("dr_enquiry_ford_exchangeoldcar")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_exchangeoldcar"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_exchangeoldcar = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_exchangeoldcar"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_exchangeoldcar = '" + value + "'");

								String history_actiontype = "DO YOU WANT TO EXCHANGE YOUR OLD CAR";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Exchange your old car is Updated!";
							}
						}

						if (name.equals("txt_enquiry_ford_othercarconcideration")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_othercarconcideration"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ford_othercarconcideration = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "WHICH OTHER CARS YOU CONSIDERING";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Other cars you considering is Updated!";
						}

						if (name.equals("txt_enquiry_ford_ex_make")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_make"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ford_ex_make = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "MAKE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Make Updated!";
						}

						if (name.equals("txt_enquiry_ford_ex_model")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_model"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ford_ex_model = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "MODEL";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Model Updated!";
						}

						if (name.equals("txt_enquiry_ford_ex_derivative")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_derivative"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ford_ex_derivative = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "DERIVATIVE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Derivative Updated!";
						}

						if (name.equals("dr_enquiry_ford_ex_year")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_year"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_year = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_ex_year"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_ex_year = '" + value + "'");

								String history_actiontype = "YEAR";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Year is Updated!";
							}
						}

						if (name.equals("txt_enquiry_ford_ex_odoreading")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_odoreading"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ford_ex_odoreading = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "ODO KM READING";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Odo KM Reading Updated!";
						}

						if (name.equals("dr_enquiry_ford_ex_doors")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_doors"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_doors = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_ex_doors"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_ex_doors = '" + value + "'");

								String history_actiontype = "DOORS";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Doors Is Updated!";
							}
						}

						if (name.equals("dr_enquiry_ford_ex_bodystyle")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_bodystyle"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_bodystyle = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_ex_bodystyle"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_ex_bodystyle = '" + value + "'");

								String history_actiontype = "BODY STYLE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Body Style is Updated!";
							}
						}

						if (name.equals("txt_enquiry_ford_ex_enginesize")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_enginesize"
									+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
									+ " WHERE enquiry_id=" + enquiry_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
									+ " SET"
									+ " enquiry_ford_ex_enginesize = '" + value + "'"
									+ " WHERE enquiry_id = " + enquiry_id + "";
							updateQuery(StrSql);
							String history_actiontype = "ENGINE SIZE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
									+ " (history_enquiry_id,"
									+ " history_emp_id,"
									+ " history_datetime,"
									+ " history_actiontype,"
									+ " history_oldvalue,"
									+ " history_newvalue)"
									+ " VALUES ("
									+ " '" + enquiry_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							LastTimeUpdate(enquiry_id, comp_id);
							StrHTML = "Engine Size Updated!";
						}

						if (name.equals("dr_enquiry_ford_ex_fueltype")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_fueltype"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_fueltype = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_ex_fueltype"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_ex_fueltype = '" + value + "'");

								String history_actiontype = "FUEL TYPE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Fuel Type is Updated!";
							}
						}

						if (name.equals("dr_enquiry_ford_ex_drive")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_drive"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_drive = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_ex_drive"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_ex_drive = '" + value + "'");

								String history_actiontype = "DRIVE";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Drive is Updated!";
							}
						}

						if (name.equals("dr_enquiry_ford_ex_transmission")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_transmission"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_transmission = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_ex_transmission"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_ex_transmission = '" + value + "'");

								String history_actiontype = "TRANSMISSION";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Transmission is Updated!";
							}
						}

						if (name.equals("dr_enquiry_ford_ex_colour")) {
							if (!value.equals("0")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_colour"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_colour = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_newvalue = ExecuteQuery("SELECT enquiry_ford_ex_colour"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_ford_ex_colour = '" + value + "'");

								String history_actiontype = "COLOUR";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + history_newvalue + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Colour is Updated!";
							}
						}

						if (name.equals("txt_enquiry_ford_ex_priceoffered")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_priceoffered"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_priceoffered = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "PRICE OFFERED ";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Price Offered Updated!";
							} else {
								StrHTML = "Enter Price Offered!";
							}
						}

						if (name.equals("txt_enquiry_ford_ex_estmtprice")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_ex_estmtprice"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_ex_estmtprice = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "ESTIMATED PRICE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Estimated Price Updated!";
							} else {
								StrHTML = "Enter Estimated Price!";
							}
						}

						if (name.equals("txt_enquiry_ford_vistacontractnumber")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_vistacontractnumber"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_vistacontractnumber = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "VISTA CONTRACT NUMBER";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Vista Contract Number Updated!";
							} else {
								StrHTML = "Enter Vista Contract Number!";
							}
						}

						if (name.equals("txt_enquiry_ford_nscordernumber")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_nscordernumber"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_nscordernumber = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "NSC ORDER NUMBER";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "NSC Order number Updated!";
							} else {
								StrHTML = "Enter NSC Order number!";
							}
						}

						if (name.equals("txt_enquiry_ford_qcsid")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_qcsid"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_qcsid = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "QCS ID";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "QCS ID Updated!";
							} else {
								StrHTML = "Enter QCS ID!";
							}
						}

						if (name.equals("txt_enquiry_ford_qpdid")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_ford_qpdid"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_ford_qpdid = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "QPD ID";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "QPD ID Updated!";
							} else {
								StrHTML = "Enter QPD ID!";
							}
						}

						// End Ford Conditions

						// Start MB Conditions
						if (name.equals("dr_enquiry_mb_occupation")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_mb_occupation"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id = " + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_mb_occupation = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "OCCUPATION";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Occupation Updated!";
							} else {
								// StrHTML = "Enter Estimated Price!";
							}
						}

						if (name.equals("dr_enquiry_mb_carusage")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_mb_carusage"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_mb_carusage = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "CAR USAGE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Car Usage Conditions Updated!";
							} else {
								// StrHTML = "Enter Estimated Price!";
							}
						}

						if (name.equals("dr_enquiry_mb_type")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_mb_type"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_mb_type = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "TYPE";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Type Updated!";
							} else {
								// StrHTML = "Enter Estimated Price!";
							}
						}

						if (name.equals("dr_enquiry_mb_drivingpattern")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_mb_drivingpattern"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_mb_drivingpattern = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "DRIVING PATTERN";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Driving Pattern Updated!";
							} else {
								// StrHTML = "Enter Estimated Price!";
							}
						}

						if (name.equals("dr_enquiry_mb_income")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_mb_income"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_mb_income = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "INCOME";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Income Updated!";
							} else {
								// StrHTML = "Enter Estimated Price!";
							}
						}

						if (name.equals("dr_enquiry_mb_avgdriving")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_mb_avgdriving"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_mb_avgdriving = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "AVG. DRIVING (KMS/MONTH)";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Avg. Driving Updated!";
							} else {
								// StrHTML = "Enter Estimated Price!";
							}
						}

						if (name.equals("dr_enquiry_mb_currentcars")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT enquiry_mb_currentcars"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_mb_currentcars = '" + value + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);
								String history_actiontype = "PRESENTLY OWNED";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "Presently Owned Updated!";
							} else {
								// StrHTML = "Enter Estimated Price!";
							}
						}
						// End MB Conditions.

						if (name.equals("chk_enquiry_dms")) {
							if (!value.equals("")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT IF(enquiry_dms = 0 , 'Inactive', 'Active')"
										+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
										+ " WHERE enquiry_id=" + enquiry_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
										+ " SET"
										+ " enquiry_dms = '" + CNumeric(value) + "'"
										+ " WHERE enquiry_id = " + enquiry_id + "";
								updateQuery(StrSql);

								if (value.equals("0")) {
									value = "Inactive";
								} else {
									value = "Active";
								}

								String history_actiontype = "DMS";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
										+ " (history_enquiry_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + enquiry_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								LastTimeUpdate(enquiry_id, comp_id);
								StrHTML = "DMS Updated!";
							}
						}

					}

				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}

			}
		}
	}
	public String doGetPorsche(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (name.equals("dr_enquiry_porsche_gender")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_gender"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_gender = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Gender";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Gender Updated!";
			} else {
				StrHTML = "Select Gender!";
			}
		}

		if (name.equals("dr_enquiry_porsche_nationality")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_nationality"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_nationality = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Nationality";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Nationality Updated!";
			}
		}

		if (name.equals("dr_enquiry_porsche_religion")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_religion"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_religion = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Religion";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Religion Updated!";
			} else {
				StrHTML = "Select Religion!";
			}
		}

		if (name.equals("dr_enquiry_porsche_maritalstatus")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_maritalstatus"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_maritalstatus = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Marital Status!";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Marital Status Updated!";
			} else {
				StrHTML = "Select Marital Status!";
			}
		}

		if (name.equals("dr_enquiry_porsche_financeoption")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_financeoption"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_financeoption = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Finance Option";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Finance Option Updated!";
			} else {
				StrHTML = "Select Finance Option!";
			}
		}

		if (name.equals("dr_enquiry_porsche_insuranceoption")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_insuranceoption"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_insuranceoption = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Insurance Option";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Insurance Option Updated!";
			} else {
				StrHTML = "Select Insurance Option!";
			}
		}

		if (name.equals("txt_enquiry_porsche_spousename")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_spousename"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_spousename = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Spouse Name";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Spouse Name Updated!";
			} else {
				// StrHTML = "Enter QCS ID!";
			}
		}

		if (name.equals("dr_enquiry_porsche_spousedrive")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_spousedrive"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_spousedrive = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Spouse Drive";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Does Spouse Drive Updated!";
			} else {
				// StrHTML = "Enter QCS ID!";
			}
		}

		if (name.equals("txt_enquiry_porsche_clubmembership")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_clubmembership"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_clubmembership = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Club Membership";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Club Membership Updated!";
			} else {
				// StrHTML = "Enter QCS ID!";
			}
		}

		if (name.equals("txt_enquiry_porsche_vehicleinhouse")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_vehicleinhouse"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_vehicleinhouse = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Number Of Vehicle In House";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Number Of Vehicle In House Updated!";
			} else {
				StrHTML = "Enter Number Of Vehicle In House!";
			}
		}

		if (name.equals("chk_enquiry_porsche_interest")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_interest"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_interest = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Interest";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Interest Updated!";
			} else {
				// StrHTML = "Enter Estimated Price!";
			}
		}

		if (name.equals("chk_enquiry_porsche_language")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_language"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_language = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Language";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Language Updated!";

				// SOP("StrHTML==" + StrHTML);
			} else {
				StrHTML = "Select Language!";
			}
		}

		if (name.equals("chk_enquiry_porsche_preferredcomm")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_preferredcomm"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_preferredcomm = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Preferred Communication";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Preferred Communication Updated!";
			} else {
				StrHTML = "Select Preferred Communication!";
			}
		}

		if (name.equals("chk_enquiry_porsche_socialmediapref")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_socialmediapref"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_socialmediapref = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Social Media Preference";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Social Media Preference Updated!";
			} else {
				StrHTML = "Select Social Media Preference!";
			}
		}

		if (name.equals("porscheothervehicle")) {
			if (!value.equals("")) {
				value = CleanArrVal(value.replaceAll("nbsp", "&"));
				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id;
				// SOP("StrSql==1==" + StrSql);
				String history_oldvalue = PadQuotes(ExecuteQuery(StrSql));

				if (history_oldvalue.equals("")) {
					history_oldvalue = "";
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_currentcars ("
						+ " currentcars_variant_id,"
						+ " currentcars_enquiry_id"
						+ " ) SELECT"
						+ " variant_id,"
						+ " " + enquiry_id + ""
						+ " FROM axelaauto.axela_preowned_variant WHERE variant_id IN (" + value + ")";
				// SOP("StrSql----" + StrSql);
				updateQuery(StrSql);

				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id;
				// SOP("StrSql==2==" + StrSql);
				String history_newvalue = PadQuotes(ExecuteQuery(StrSql));
				if (history_newvalue.equals("")) {
					history_newvalue = "";
				}
				String history_actiontype = "Current Cars!";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Current Cars Updated!";
			} else {
				StrHTML = "Select Current Cars!";
				// String history_newvalue = "";
				//
				// String history_actiontype = "Current Cars!";
				//
				// StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
				// + " FROM axelaauto.axela_preowned_variant"
				// + " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
				// + " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
				// + " WHERE currentcars_enquiry_id = " + enquiry_id;
				// // SOP("StrSql==1==" + StrSql);
				// String history_oldvalue = PadQuotes(ExecuteQuery(StrSql));
				//
				// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
				// + " WHERE currentcars_enquiry_id = " + enquiry_id + "";
				// updateQuery(StrSql);
				//
				// StrHTML = "Current Cars Updated!";
				//
				// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
				// + " (history_enquiry_id,"
				// + " history_emp_id,"
				// + " history_datetime,"
				// + " history_actiontype,"
				// + " history_oldvalue,"
				// + " history_newvalue)"
				// + " VALUES ("
				// + " '" + enquiry_id + "',"
				// + " '" + emp_id + "',"
				// + " '" + ToLongDate(kknow()) + "',"
				// + " '" + history_actiontype + "',"
				// + " '" + history_oldvalue + "',"
				// + " '" + history_newvalue + "')";
				// updateQuery(StrSql);
			}
		}

		if (name.equals("txt_enquiry_porsche_householdcount")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_householdcount"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_householdcount = " + value + ""
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Persons In Household!";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Person's In Household!";
			} else {
				StrHTML = "Enter Person's In Household!";
			}
		}

		if (name.equals("txt_enquiry_porsche_contact_dob")) {
			if (!value.equals("")) {
				if (Double.parseDouble(ConvertShortDateToStr(value)) > Double.parseDouble(ToLongDate(kknow()))) {
					StrHTML = "DOB cannot be greater than current date!";
				}
				if (StrHTML.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("SELECT contact_dob"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " WHERE enquiry_id = " + enquiry_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
							+ " SET"
							+ " contact_dob = '" + ConvertShortDateToStr(value) + "'"
							+ " WHERE enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					String history_actiontype = "DOB!";

					if (!history_oldvalue.equals("")) {
						history_oldvalue = SplitDate(history_oldvalue) + "/" + SplitMonth(history_oldvalue);
					}
					if (!value.equals("")) {
						value = SplitDate(ConvertShortDateToStr(value)) + "/" + SplitMonth(ConvertShortDateToStr(value));
					}

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					LastTimeUpdate(enquiry_id, comp_id);
					StrHTML = "DOB Updated!";
				}
				return StrHTML.toString();
			} else {
				StrHTML = "Enter DOB!";
			}
		}

		if (name.equals("txt_enquiry_porsche_contact_anniversary")) {
			if (!value.equals("")) {
				if (Double.parseDouble(ConvertShortDateToStr(value)) > Double.parseDouble(ToLongDate(kknow()))) {
					StrHTML = "Anniversary cannot be greater than current date!";
				}
				if (StrHTML.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("SELECT contact_anniversary"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " WHERE enquiry_id = " + enquiry_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
							+ " SET"
							+ " contact_anniversary = '" + ConvertShortDateToStr(value) + "'"
							+ " WHERE enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					String history_actiontype = "Anniversary";

					if (!history_oldvalue.equals("")) {
						history_oldvalue = SplitDate(history_oldvalue) + "/" + SplitMonth(history_oldvalue);
					}
					if (!value.equals("")) {
						value = SplitDate(ConvertShortDateToStr(value)) + "/" + SplitMonth(ConvertShortDateToStr(value));
					}

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					LastTimeUpdate(enquiry_id, comp_id);
					StrHTML = "Anniversary Updated!";
				}
			} else {
				StrHTML = "Enter Anniversary Updated!";
			}
		}

		if (name.equals("dr_enquiry_porsche_industry")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_industry"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_porsche_industry = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Industry";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Industry Updated!";
			}
		}

		return StrHTML.toString();

	}

	public String doGetJLR(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (name.equals("dr_enquiry_jlr_employmentstatus")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_employmentstatus"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_employmentstatus = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Employment Status";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Employment Status Updated!";
			} else {
				StrHTML = "<font color='red'>Select Employment Status!</font>";
			}
		}

		if (name.equals("dr_enquiry_jlr_industry")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_industry"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_industry = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Industry";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Industry Updated!";
			} else {
				StrHTML = "<font color='red'>Select Industry!</font>";
			}
		}

		if (name.equals("txt_enquiry_jlr_birthday")) {
			if (!value.equals("")) {
				if (Double.parseDouble(ConvertShortDateToStr(value)) > Double.parseDouble(ToLongDate(kknow()))) {
					StrHTML = "Birthday cannot be greater than current date!";
				}
				if (StrHTML.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("SELECT contact_dob"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = enquiry_contact_id"
							+ " WHERE enquiry_id = " + enquiry_id + " ");
					StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
							+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
							+ " SET"
							+ " contact_dob = '" + ConvertShortDateToStr(value) + "'"
							+ " WHERE enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);
					String history_actiontype = "Birthday!";

					if (!history_oldvalue.equals("")) {
						history_oldvalue = SplitDate(history_oldvalue) + "/" + SplitMonth(history_oldvalue);
					}
					if (!value.equals("")) {
						value = SplitDate(ConvertShortDateToStr(value)) + "/" + SplitMonth(ConvertShortDateToStr(value));
					}

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					LastTimeUpdate(enquiry_id, comp_id);
					StrHTML = "Birthday Updated!";
				}
				return StrHTML.toString();
			}
		}

		if (name.equals("dr_enquiry_jlr_gender")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_porsche_gender"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_gender = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Gender";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Gender Updated!";
			} else {
				StrHTML = "<font color='red'>Select Gender!</font>";
			}
		}

		if (name.equals("dr_enquiry_jlr_occupation")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_occupation"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_occupation = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Occupation";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Occupation Updated!";
			} else {
				StrHTML = "<font color='red'>Select Occupation!</font>";
			}
		}

		if (name.equals("jlrcurrentvehicle")) {
			if (!value.equals("")) {
				value = CleanArrVal(value.replaceAll("nbsp", "&"));
				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id;
				// SOP("StrSql==1==" + StrSql);
				String history_oldvalue = PadQuotes(ExecuteQuery(StrSql));

				if (history_oldvalue.equals("")) {
					history_oldvalue = "";
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_currentcars ("
						+ " currentcars_variant_id,"
						+ " currentcars_enquiry_id"
						+ " ) SELECT"
						+ " variant_id,"
						+ " " + enquiry_id + ""
						+ " FROM axelaauto.axela_preowned_variant WHERE variant_id IN (" + value + ")";
				// SOP("StrSql----" + StrSql);
				updateQuery(StrSql);

				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id;
				// SOP("StrSql==2==" + StrSql);
				String history_newvalue = PadQuotes(ExecuteQuery(StrSql));
				if (history_newvalue.equals("")) {
					history_newvalue = "";
				}
				String history_actiontype = "Current Vehicle!";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Current Vehicle Updated!";
			} else {
				StrHTML = "<font color='red'>Select Current Vehicle!</font>";
			}
		}

		if (name.equals("jlrothermodelofinterest")) {
			if (!value.equals("")) {
				value = CleanArrVal(value.replaceAll("nbsp", "&"));
				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name)), '') AS preownedmodel"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_othermodels"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = othermodels_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE othermodels_enquiry_id = " + enquiry_id;
				// SOP("StrSql==1==" + StrSql);
				String history_oldvalue = PadQuotes(ExecuteQuery(StrSql));

				if (history_oldvalue.equals("")) {
					history_oldvalue = "";
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_othermodels"
						+ " WHERE othermodels_enquiry_id = " + enquiry_id + "";
				// SOP("del----" + StrSql);
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_othermodels ("
						+ " othermodels_preownedmodel_id,"
						+ " othermodels_enquiry_id"
						+ " ) SELECT"
						+ " preownedmodel_id,"
						+ " " + enquiry_id + ""
						+ " FROM axelaauto.axela_preowned_model WHERE preownedmodel_id IN (" + value + ")";
				// SOP("StrSql----" + StrSql);
				updateQuery(StrSql);

				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name)), '') AS preownedmodel"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_othermodels"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = othermodels_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE othermodels_enquiry_id = " + enquiry_id;
				// SOP("StrSql==2==" + StrSql);
				String history_newvalue = PadQuotes(ExecuteQuery(StrSql));
				if (history_newvalue.equals("")) {
					history_newvalue = "";
				}
				String history_actiontype = "Other Models Interest!";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Other Models Interest Updated!";
			} else {
				StrHTML = "<font color='red'>Select Other Models Interest!</font>";
			}
		}

		if (name.equals("chk_enquiry_jlr_financeinterest")) {
			if (!value.equals("0")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_financeinterest"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_financeinterest = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Finance Interest";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Finance Interest Updated!";
			}
		}

		if (name.equals("chk_enquiry_jlr_highnetworth")) {
			if (!value.equals("0")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_highnetworth"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_highnetworth = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "High Net Worth";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "High Net Worth Updated!";
			}
		}

		if (name.equals("txt_enquiry_jlr_noofchildren")) {
			// if (!value.equals("0") && !value.equals("")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_noofchildren"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_id = " + enquiry_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
					+ " SET"
					+ " enquiry_jlr_noofchildren = " + value + ""
					+ " WHERE enquiry_id = " + enquiry_id + "";
			updateQuery(StrSql);
			String history_actiontype = "No. of Children!";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
					+ " (history_enquiry_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + enquiry_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			LastTimeUpdate(enquiry_id, comp_id);
			StrHTML = "No. of Children!";
			// }
		}

		if (name.equals("txt_enquiry_jlr_noofpeopleinhousehold")) {
			// if (!value.equals("0") && !value.equals("")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_noofpeopleinhousehold"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_id = " + enquiry_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
					+ " SET"
					+ " enquiry_jlr_noofpeopleinhousehold = " + value + ""
					+ " WHERE enquiry_id = " + enquiry_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Persons In Household!";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
					+ " (history_enquiry_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + enquiry_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			LastTimeUpdate(enquiry_id, comp_id);
			StrHTML = "Person's In Household!";
			// }
		}

		if (name.equals("txt_enquiry_jlr_householdincome")) {
			// if (!value.equals("0") && !value.equals("")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_householdincome"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_id = " + enquiry_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
					+ " SET"
					+ " enquiry_jlr_householdincome = " + value + ""
					+ " WHERE enquiry_id = " + enquiry_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Household Income!";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
					+ " (history_enquiry_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + enquiry_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			LastTimeUpdate(enquiry_id, comp_id);
			StrHTML = "Household Income Updated!";
			// }
		}

		if (name.equals("txt_enquiry_jlr_annualrevenue")) {
			// if (!value.equals("0") && !value.equals("")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_annualrevenue"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_id = " + enquiry_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
					+ " SET"
					+ " enquiry_jlr_annualrevenue = " + value + ""
					+ " WHERE enquiry_id = " + enquiry_id + "";
			updateQuery(StrSql);
			String history_actiontype = "Annual Revenue!";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
					+ " (history_enquiry_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + enquiry_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			LastTimeUpdate(enquiry_id, comp_id);
			StrHTML = "Annual Revenue Updated!";
			// }
		}

		if (name.equals("txt_enquiry_jlr_noofemployees")) {
			// if (!value.equals("0") && !value.equals("")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_noofemployees"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_id = " + enquiry_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
					+ " SET"
					+ " enquiry_jlr_noofemployees = " + value + ""
					+ " WHERE enquiry_id = " + enquiry_id + "";
			updateQuery(StrSql);
			String history_actiontype = "No. of Employees!";

			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
					+ " (history_enquiry_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + enquiry_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			LastTimeUpdate(enquiry_id, comp_id);
			StrHTML = "No. of Employees!";
			// }
		}

		if (name.equals("chk_enquiry_jlr_interest")) {
			// if (!value.equals("")) {
			value = value.replaceAll("nbsp", "&");
			String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_interests"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
					+ " WHERE enquiry_id = " + enquiry_id + " ");
			StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
					+ " SET"
					+ " enquiry_jlr_interests = '" + value + "'"
					+ " WHERE enquiry_id = " + enquiry_id + "";
			// SOP("StrSql===" + StrSql);
			updateQuery(StrSql);
			String history_actiontype = "Interests!";
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
					+ " (history_enquiry_id,"
					+ " history_emp_id,"
					+ " history_datetime,"
					+ " history_actiontype,"
					+ " history_oldvalue,"
					+ " history_newvalue)"
					+ " VALUES ("
					+ " '" + enquiry_id + "',"
					+ " '" + emp_id + "',"
					+ " '" + ToLongDate(kknow()) + "',"
					+ " '" + history_actiontype + "',"
					+ " '" + history_oldvalue + "',"
					+ " '" + value + "')";
			updateQuery(StrSql);
			LastTimeUpdate(enquiry_id, comp_id);
			StrHTML = "Interests Updated!";
			// }
		}

		if (name.equals("dr_enquiry_jlr_accounttype")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_accounttype"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_accounttype = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Account Type!";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Account Type Updated!";
			} else {
				StrHTML = "<font color='red'>Select Account Type!</font>";
			}
		}

		if (name.equals("dr_enquiry_jlr_status")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT enquiry_jlr_enquirystatus"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
						+ " SET"
						+ " enquiry_jlr_enquirystatus = '" + value + "'"
						+ " WHERE enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "Enquiry Status!";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Enquiry Status Updated!";
			} else {
				StrHTML = "<font color='red'>Select Enquiry Status!</font>";
			}
		}

		return StrHTML.toString();
	}

	public String EnquiryDashCheckSkoda(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Check if enquiry exist
		String na_enquiry_id = CNumeric(ExecuteQuery("SELECT na_enquiry_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
				+ " WHERE na_enquiry_id = " + enquiry_id + " "));

		// if enquiry doesn't exist insert
		if (na_enquiry_id.equals("0")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_na"
					+ "(na_enquiry_id) VALUES(" + enquiry_id + ")";
			updateQuery(StrSql);
		}

		if (name.equals("na_skoda_ownbusiness")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_ownbusiness"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_ownbusiness = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Own Business";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Own Business Updated";
			} else {
				StrHTML = "Select Own Business!";
			}
		} else if (name.equals("na_skoda_companyname")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_companyname"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_companyname = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Company Name";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Company Name Updated";
			} else {
				StrHTML = "Enter Company Name!";
			}
		} else if (name.equals("txt_skoda_contact_jobtitle")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");

				StrSql = "SELECT enquiry_contact_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " WHERE enquiry_id = " + enquiry_id;

				String enquiry_contact_id = ExecuteQuery(StrSql);

				String history_oldvalue = ExecuteQuery("SELECT"
						+ " contact_jobtitle"
						+ " FROM " + compdb(comp_id) + "axela_customer_contact"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry ON enquiry_contact_id = contact_id"
						+ " WHERE enquiry_contact_id =" + enquiry_contact_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
						+ " SET"
						+ " contact_jobtitle = '" + value + "'"
						+ " WHERE contact_id = " + enquiry_contact_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Job Title";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Job Title Updated";
			} else {
				StrHTML = "Enter Job Title!";
			}
		} else if (name.equals("na_skoda_financerequired")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_financerequired"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_financerequired = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Finance Required";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Finance Required Updated!";
			} else {
				StrHTML = "Select Finance Required!";
			}
		} else if (name.equals("txt_skoda_enquiry_exp_close_date")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				// SOP("value in closing" + value);
				if (!isValidDateFormatShort(value)) {
					StrHTML = "<font color=\"red\">Enter Valid Date for How soon is the purchase!</font>";
				} else {
					String history_oldvalue = strToShortDate(ExecuteQuery("SELECT enquiry_close_date"
							+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " WHERE enquiry_id = " + enquiry_id + ""));
					// SOP("ConvertShortDateToStr(value))---"+ConvertShortDateToStr(value));
					StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
							+ " SET"
							+ " enquiry_close_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
							+ " WHERE enquiry_id = " + enquiry_id + "";
					updateQuery(StrSql);

					String history_actiontype = "How soon is the purchase";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " VALUES ("
							+ " '" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					LastTimeUpdate(enquiry_id, comp_id);
					StrHTML = StrHTML + "<br>How soon is the purchase Updated!";
				}
			} else {
				StrHTML = "Enter How soon is the purchase!";
			}
		} else if (name.equals("na_skoda_currentcarappxkmsrun")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_currentcarappxkmsrun"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_currentcarappxkmsrun = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Approximate kms run";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Approximate kms run Updated!";
			} else {
				StrHTML = "Select Approximate kms run!";
			}
		} else if (name.equals("chk_na_skoda_whatareyoulookingfor")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_whatareyoulookingfor"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id=" + enquiry_id + " ");
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_whatareyoulookingfor = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);
				String history_actiontype = "What are you looking for in your car";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "What are you looking for in your car Updated!";
			} else {
				StrHTML = "Select What are you looking for in your car!";
			}
		} else if (name.equals("na_skoda_numberoffamilymembers")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_numberoffamilymembers"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_numberoffamilymembers = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Number of Family Members";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Number of Family Members Updated!";
			} else {
				StrHTML = "Select Number of Family Members!";
			}
		} else if (name.equals("na_skoda_whowilldrive")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_whowilldrive"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_whowilldrive = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Who will drive the car";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Who will drive the car Updated!";
			} else {
				StrHTML = "Select Who will drive the car!";
			}
		} else if (name.equals("na_skoda_whoareyoubuyingfor")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_whoareyoubuyingfor"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_whoareyoubuyingfor = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Who are you buying the car for";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Who are you buying the car for Updated!";
			} else {
				StrHTML = "Select Who are you buying the car for!";
			}
		} else if (name.equals("na_skoda_newcarappxrun")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_newcarappxrun"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_newcarappxrun = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Approximately how many kms in a day will the car be run";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Approximately how many kms in a day will the car be run Updated!";
			} else {
				StrHTML = "Select Approximately how many kms in a day will the car be run!";
			}
		} else if (name.equals("na_skoda_wherewillbecardriven")) {
			if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("SELECT na_skoda_wherewillbecardriven"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " WHERE na_enquiry_id = " + enquiry_id + " ");

				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry_na"
						+ " SET"
						+ " na_skoda_wherewillbecardriven = '" + value + "'"
						+ " WHERE na_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Where will the car be driven mostly";
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Where will the car be driven mostly Updated!";
			} else {
				StrHTML = "Select Where will the car be driven mostly!";
			}
		}

		if (name.equals("dr_na_skoda_enquiry_currentcars")) {
			if (!value.equals("")) {
				value = CleanArrVal(value.replaceAll("nbsp", "&"));
				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id;
				// SOP("StrSql==1==" + StrSql);
				String history_oldvalue = PadQuotes(ExecuteQuery(StrSql));

				if (history_oldvalue.equals("")) {
					history_oldvalue = "";
				}

				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id + "";
				updateQuery(StrSql);

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_currentcars ("
						+ " currentcars_variant_id,"
						+ " currentcars_enquiry_id"
						+ " ) SELECT"
						+ " variant_id,"
						+ " " + enquiry_id + ""
						+ " FROM axelaauto.axela_preowned_variant WHERE variant_id IN (" + value + ")";
				// SOP("StrSql----" + StrSql);
				updateQuery(StrSql);

				StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
						+ " FROM axelaauto.axela_preowned_variant"
						+ " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
						+ " WHERE currentcars_enquiry_id = " + enquiry_id;
				// SOP("StrSql==2==" + StrSql);
				String history_newvalue = PadQuotes(ExecuteQuery(StrSql));
				if (history_newvalue.equals("")) {
					history_newvalue = "";
				}
				String history_actiontype = "Current Cars!";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
						+ " (history_enquiry_id,"
						+ " history_emp_id,"
						+ " history_datetime,"
						+ " history_actiontype,"
						+ " history_oldvalue,"
						+ " history_newvalue)"
						+ " VALUES ("
						+ " '" + enquiry_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + history_newvalue + "')";
				updateQuery(StrSql);
				LastTimeUpdate(enquiry_id, comp_id);
				StrHTML = "Current Cars Updated!";
			} else {
				StrHTML = "Select Current Cars!";
				// String history_newvalue = "";
				//
				// String history_actiontype = "Current Cars!";
				//
				// StrSql = "SELECT COALESCE(GROUP_CONCAT(CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name)), '') AS variant"
				// + " FROM axelaauto.axela_preowned_variant"
				// + " INNER JOIN axelaauto.axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
				// + " INNER JOIN axelaauto.axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
				// + " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_currentcars ON currentcars_variant_id = variant_id"
				// + " WHERE currentcars_enquiry_id = " + enquiry_id;
				// // SOP("StrSql==1==" + StrSql);
				// String history_oldvalue = PadQuotes(ExecuteQuery(StrSql));
				//
				// StrSql = "DELETE FROM " + compdb(comp_id) + "axela_sales_enquiry_currentcars"
				// + " WHERE currentcars_enquiry_id = " + enquiry_id + "";
				// updateQuery(StrSql);
				//
				// StrHTML = "Current Cars Updated!";
				//
				// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
				// + " (history_enquiry_id,"
				// + " history_emp_id,"
				// + " history_datetime,"
				// + " history_actiontype,"
				// + " history_oldvalue,"
				// + " history_newvalue)"
				// + " VALUES ("
				// + " '" + enquiry_id + "',"
				// + " '" + emp_id + "',"
				// + " '" + ToLongDate(kknow()) + "',"
				// + " '" + history_actiontype + "',"
				// + " '" + history_oldvalue + "',"
				// + " '" + history_newvalue + "')";
				// updateQuery(StrSql);
			}
		}

		return StrHTML.toString();
	}

	public String ListInvoice(String enquiry_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		Str.append("<div class=\"container-fluid portlet box\">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">Invoice</div></div>");
		Str.append("<div class=\"portlet-body portlet-empty\">");
		Str.append("<div class=\"tab-pane\" id=\"\"></div>");
		int count = 0;
		StrSql = "SELECT voucher_id, voucher_so_id, voucher_invoice_id, voucher_branch_id, voucher_amount,"
				+ " CONCAT( 'INV', branch_code, voucher_no ) AS voucher_no, voucher_date,"
				+ " customer_id, customer_name, vouchertrans_netprice, vouchertrans_taxamount,"
				+ " voucher_quote_id, voucher_active, voucher_authorize, voucher_entry_id,vouchertype_authorize,vouchertype_defaultauthorize, voucher_entry_date,"
				+ "	vouchertype_id, vouchertype_name, voucherclass_id, voucherclass_file,"
				+ " CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name, emp_id, branch_name,branch_brand_id,"
				+ " GROUP_CONCAT(item_name SEPARATOR '<br>') AS items"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_trans ON vouchertrans_voucher_id = voucher_id"
				+ " AND vouchertrans_rowcount != 0"
				+ "	AND vouchertrans_tax =0"
				+ " AND vouchertrans_discount = 0"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ "	INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vouchertrans_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " WHERE"
				+ " voucher_enquiry_id = " + enquiry_id
				+ " AND voucher_vouchertype_id = 6"
				+ " GROUP BY  voucher_id"
				+ " ORDER BY voucher_id DESC";
		// SOP("StrSql==" + StrSqlBreaker(StrSql));
		CachedRowSet crs = processQuery(StrSql, 0);

		try {
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\"  table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Items</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n<td>").append(count).append("</td>\n");
					Str.append("<td align='center'><a href=../accounting/voucher-list.jsp?vouchertype_id=6&voucher_id=");
					Str.append(crs.getInt("voucher_id")).append(">").append(crs.getString("voucher_id")).append("</a></td>\n");
					Str.append("<td>").append(crs.getString("voucher_no"));
					if (crs.getString("voucher_authorize").equals("1")) {
						Str.append("<br><font color=\"#ff0000\">[Authorized]</font>");
					}
					if (crs.getString("voucher_active").equals("0")) {
						Str.append("<br><font color=red>[Inactive]</font>");
					}
					Str.append("</td>\n<td><a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getInt("customer_id")).append(">").append(crs.getString("customer_name"));
					Str.append("</a></td>\n<td align='center'>").append(strToShortDate(crs.getString("voucher_date")));
					Str.append("</td>\n<td>").append(crs.getString("items"));
					Str.append("</td>\n<td>Net Total: ");
					Str.append(IndDecimalFormat(crs.getString("vouchertrans_netprice")));
					if (!crs.getString("vouchertrans_taxamount").equals("0")) {
						Str.append("<br>Tax: ").append(IndDecimalFormat(crs.getString("vouchertrans_taxamount"))).append("</b>");
					}
					Str.append("<br><b>Total: ").append(IndDecimalFormat(crs.getString("voucher_amount")));
					Str.append("</b><br></td>\n");
					Str.append("<td><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name")).append("</a></td>\n");
					Str.append("<td><a href=\"../accounting/so-update2.jsp?update=yes");
					Str.append("&voucher_id=").append(crs.getString("voucher_id"));
					Str.append("&voucherclass_id=6").append("&vouchertype_id=6");
					Str.append("\">Update Invoice</a><br>");
					Str.append("<a href=\"../accounting/voucher-list.jsp?voucher_invoice_id=");
					Str.append(crs.getString("voucher_id")).append("&voucherclass_id=9");
					Str.append("&vouchertype_id=9").append("\">List Receipts</a>");
					if (crs.getString("vouchertype_authorize").equals("1") || crs.getString("vouchertype_defaultauthorize").equals("1")) {

						Str.append("<br/><a href=\"../accounting/" + "voucher" + "-authorize.jsp?voucher_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
						Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id"));
						Str.append("\">Authorize</a>");

					}
					Str.append("<br><a href=\"../accounting/receipt-update.jsp?add=yes&ledger=");
					Str.append(crs.getString("customer_id")).append("&voucherclass_id=9");
					Str.append("&vouchertype_id=9").append("&voucher_invoice_id=").append(crs.getString("voucher_id"));
					Str.append("&voucher_so_id=").append(crs.getString("voucher_so_id")).append("\">Add Receipt</a>");

					String print = "yes";

					if ((crs.getString("vouchertype_authorize").equals("1")
							|| crs.getString("vouchertype_defaultauthorize").equals("1"))
							&& CNumeric(crs.getString("voucher_authorize")).equals("0")) {
						print = "";
					}
					if (print.equals("yes")) {
						if (Double.parseDouble(CNumeric(crs.getString("voucher_date"))) <= Double.parseDouble("20170630000000")) {
							Str.append("</br><a target='_blank' href=\"../accounting/");
							// Go to different report page for "One Triumph" branch "onetriumph-receipt-print.jsp"
							if (crs.getString("branch_name").toLowerCase().contains("one triumph")
									|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
								Str.append("onetriumph-");
							}
							if (crs.getString("branch_brand_id").equals("2") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-maruthi-suzuki" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-honda" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-hyundai" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-ford" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-yamaha" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else if (crs.getString("branch_brand_id").equals("101") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(crs.getString("voucherclass_file") + "-print" + "-suzuki" + ".jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							} else {
								Str.append(crs.getString("voucherclass_file") + "-print.jsp?voucher_id=")
										.append(crs.getString("voucher_id")).append("&voucherclass_id=")
										.append(crs.getString("voucherclass_id"));
								Str.append("&vouchertype_id=").append(
										crs.getString("vouchertype_id")).append("&dr_report=");
							}

							// Go to different report page for "One Triumph" branch
							if (crs.getString("branch_name").toLowerCase().contains("one triumph")
									|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
								Str.append("onetriumph-");
							}

							if (crs.getString("branch_brand_id").equals("2") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-maruthi-suzuki")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-honda")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-hyundai")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-ford")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-yamaha")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else if (crs.getString("branch_brand_id").equals("101") && crs.getString("vouchertype_id").equals("6") && !crs.getString("voucher_so_id").equals("0")) {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-suzuki")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							} else {
								Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print")
										.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
										.append("&brand_id=").append(crs.getString("branch_brand_id"));
								Str.append("\">Print " + crs.getString("vouchertype_name")
										+ "</a>");
							}

						} else {
							Str.append("</br><a target='_blank' href=\"../accounting/");
							Str.append(crs.getString("voucherclass_file") + "-print.jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
							// }
							// Gate Pass Print For Hyundai
							// if (crs.getString("vouchertype_id").equals("6") && crs.getString("branch_brand_id").equals("6")) {
							// Str.append("<br><a href=\"delivery-receipt-print.jsp?voucher_id=").append(crs.getString("voucher_id")).append(" \">Delivery Receipt Print</a>");
							// }
						}
					}

					Str.append("</td>\n</tr>\n");
				}
				Str.append("</table>\n");
				Str.append("</tbody>\n");
			} else {
				Str.append("<br><br><center><font color=red><b>No Invoice(s) found!</b></font></center>");
			}
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();

	}
	public String ListReceipt(String so_id, String BranchAccess, String ExeAccess, String comp_id) {
		StringBuilder Str = new StringBuilder();
		CachedRowSet crs = null;
		Str.append("<div class=\"container-fluid portlet box\">");
		Str.append("<div class=\"portlet-title\" style=\"text-align: center\">");
		Str.append("<div class=\"caption\" style=\"float: none\">Receipt</div></div>");
		Str.append("<div class=\"portlet-body portlet-empty\">");
		Str.append("<div class=\"tab-pane\" id=\"\"></div>");

		int count = 0;
		StrSql = "SELECT voucher_id, voucher_so_id, voucher_branch_id,"
				+ " CONCAT( 'RCT', branch_code, voucher_no ) AS voucher_no,"
				+ " voucher_date, customer_id, customer_name, voucher_amount,vouchertype_authorize,vouchertype_defaultauthorize,voucher_authorize,"
				+ " COALESCE(voucher_invoice_id, 0) AS voucher_invoice_id,"
				+ " voucher_active, voucher_entry_id, voucher_entry_date,"
				+ "	vouchertype_id, vouchertype_name, voucherclass_id, voucherclass_file,branch_brand_id,"
				+ " CONCAT( emp_name, ' (', emp_ref_no, ')' ) AS emp_name,"
				+ " emp_id, branch_name"
				+ " FROM " + compdb(comp_id) + "axela_acc_voucher"
				+ " INNER JOIN " + compdb(comp_id) + "axela_acc_voucher_type ON vouchertype_id = voucher_vouchertype_id"
				+ "	INNER JOIN axela_acc_voucher_class ON voucherclass_id = vouchertype_voucherclass_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = voucher_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = voucher_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = voucher_emp_id"
				+ " WHERE voucher_so_id = " + so_id
				+ "	AND voucher_vouchertype_id = 9"
				+ " GROUP BY voucher_id"
				+ " ORDER BY voucher_id DESC";
		// SOP("enquiry_dash_check===ListReceiptStrSql==" + StrSql);
		if (!so_id.equals("0")) {
			crs = processQuery(StrSql, 0);
		}
		try {
			if (crs != null && crs.isBeforeFirst()) {
				Str.append("<div class=\"table\">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>No.</th>\n");
				Str.append("<th data-hide=\"phone\">Invoice</th>\n");
				Str.append("<th data-hide=\"phone\">Customer</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Date</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Amount</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Sales Consultant</th>\n");
				Str.append("<th data-hide=\"phone, tablet\">Action</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n<td valign=top align=center>").append(count);
					Str.append("</td>\n<td valign=top align=center><a href=../accounting/voucher-list.jsp?vouchertype_id=9&voucher_id=");
					Str.append(crs.getInt("voucher_id")).append(">").append(crs.getString("voucher_id"));
					Str.append("</a></td>\n<td valign=top align=center>").append(crs.getString("voucher_no"));
					if (crs.getString("voucher_authorize").equals("1")) {
						Str.append("<br><font color=\"#ff0000\">[Authorized]</font>");
					}
					if (crs.getString("voucher_active").equals("0")) {
						Str.append("<br><font color=red>[Inactive]</font>");
					}
					Str.append("</td>\n<td valign=top align=left>");
					if (!crs.getString("voucher_invoice_id").equals("0")) {
						Str.append("<a href=../accounting/voucher-list.jsp?vouchertype_id=6&voucher_id=").append(crs.getInt("voucher_invoice_id"));
						Str.append(">Invoice ID: ").append(crs.getString("voucher_invoice_id")).append("</a>");
					} else {
						Str.append("");
					}
					Str.append("</td>\n<td valign=top align=left><a href=../customer/customer-list.jsp?customer_id=");
					Str.append(crs.getInt("customer_id")).append(">").append(crs.getString("customer_name"));
					Str.append("</a></td>\n<td valign=top align=center>").append(strToShortDate(crs.getString("voucher_date")));
					Str.append("</td>\n<td valign=top align=right>").append(IndDecimalFormat(crs.getString("voucher_amount")));
					Str.append("</td>\n<td valign=top align=left><a href=../portal/executive-summary.jsp?emp_id=");
					Str.append(crs.getInt("emp_id")).append(">").append(crs.getString("emp_name"));
					Str.append("</a></td>\n<td valign=top align=left nowrap><a href=\"../accounting/receipt-update.jsp?update=yes&voucher_id=");
					Str.append(crs.getString("voucher_id")).append("&voucherclass_id=9").append("&vouchertype_id=9");
					Str.append(" \" target=_parent>Update Receipt</a>");

					if (crs.getString("vouchertype_authorize").equals("1") || crs.getString("vouchertype_defaultauthorize").equals("1")) {

						Str.append("<br/><a href=\"../accounting/" + "voucher" + "-authorize.jsp?voucher_id=")
								.append(crs.getString("voucher_id"))
								.append("&voucherclass_id=").append(crs.getString("voucherclass_id"));
						Str.append("&vouchertype_id=").append(crs.getString("vouchertype_id"));
						Str.append("\">Authorize</a>");

					}

					String print = "yes";

					if ((crs.getString("vouchertype_authorize").equals("1")
							|| crs.getString("vouchertype_defaultauthorize").equals("1"))
							&& CNumeric(crs.getString("voucher_authorize")).equals("0")) {
						print = "";
					}
					if (print.equals("yes")) {
						Str.append("</br><a target='_blank' href=\"../accounting/");

						// Go to different report page for "One Triumph" branch "onetriumph-so-print.jsp"
						if (crs.getString("branch_name").toLowerCase().contains("one triumph")
								|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
							Str.append("onetriumph-");
						}

						if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-ford" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-hyundai" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-honda" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-yamaha" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else if (crs.getString("branch_brand_id").equals("51") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(crs.getString("voucherclass_file") + "-print" + "-volvo" + ".jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						} else {
							Str.append(crs.getString("voucherclass_file") + "-print.jsp?voucher_id=")
									.append(crs.getString("voucher_id")).append("&voucherclass_id=")
									.append(crs.getString("voucherclass_id"));
							Str.append("&vouchertype_id=").append(
									crs.getString("vouchertype_id")).append("&dr_report=");
						}

						// Go to different report page for "One Triumph" branch
						if (crs.getString("branch_name").toLowerCase().contains("one triumph")
								|| crs.getString("branch_name").toLowerCase().contains("onetriumph")) {
							Str.append("onetriumph-");
						}
						// SOP("brand_id==" + crs.getString("branch_brand_id"));
						//

						if (crs.getString("branch_brand_id").equals("7") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-ford")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("9") && crs.getString("vouchertype_id").equals("9")) {

							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-honda")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("6") && crs.getString("vouchertype_id").equals("9")) {

							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-hyundai")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("102") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-yamaha")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						} else if (crs.getString("branch_brand_id").equals("51") && crs.getString("vouchertype_id").equals("9")) {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print").append("-volvo")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"))
									.append("&brand_id=").append(crs.getString("branch_brand_id"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						}
						else {
							Str.append(ReplaceStr(crs.getString("vouchertype_name").toLowerCase(), " ", "")).append("-print")
									.append("&dr_format=").append("pdf").append("&voucher_authorize=").append(crs.getString("voucher_authorize"));
							Str.append("\">Print " + crs.getString("vouchertype_name")
									+ "</a>");
						}
					}
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</tbody>\n");
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><center><font color=red><b>No Receipt(s) found!</b></font></center>");
			}
			Str.append("</div>\n");
			Str.append("</div>\n");
			Str.append("</div>\n");
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public void LastTimeUpdate(String enquiry_id, String comp_id) {
		StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_enquiry"
				+ " SET enquiry_lastupdatetime = '" + ToLongDate(kknow()) + "'"
				+ " WHERE enquiry_id = " + enquiry_id + "";
		updateQuery(StrSql);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
