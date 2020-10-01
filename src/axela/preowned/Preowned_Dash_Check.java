package axela.preowned;
//divya 4th dec
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String StrSql = "";
	public String name = "";
	public String value = "";
	public String preowned_id = "0";
	public String preowned_dat = "";
	public String preowned_fcamt = "";
	public String preowned_funding_bank = "";
	public String preowned_loan_no = "";
	public String preowned_emp_id = "0";
	// public String preowned_preownedmodel_id = "0";
	public String variant_name = "";
	public String preownedmodel_name = "";
	public String preownedmodel_id = "";
	public String status_id = "0";
	public String status_desc = "";
	public String lostcase1 = "0";
	public String lostcase2 = "0";
	public String lostcase3 = "0";
	public String preowned_branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String value1 = "";
	public String value2 = "";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{
			BranchAccess = GetSession("BranchAccess", request);
			ExeAccess = GetSession("ExeAccess", request);
			emp_id = CNumeric(GetSession("emp_id", request));
			preowned_id = CNumeric(PadQuotes(request.getParameter("preowned_id")));
			name = PadQuotes(request.getParameter("name"));
			value = PadQuotes(request.getParameter("value"));
			value1 = PadQuotes(request.getParameter("value1"));
			value2 = PadQuotes(request.getParameter("value2"));
			preowned_dat = PadQuotes(request.getParameter("preowned_dat"));
			preowned_fcamt = PadQuotes(request.getParameter("preowned_fcamt"));
			preowned_funding_bank = PadQuotes(request.getParameter("preowned_funding_bank"));
			preowned_loan_no = PadQuotes(request.getParameter("preowned_loan_no"));
			// preowned_preownedmodel_id = CNumeric(PadQuotes(request.getParameter("preowned_preownedmodel_id")));
			status_id = CNumeric(PadQuotes(request.getParameter("status_id")));
			status_desc = PadQuotes(request.getParameter("status_desc"));
			lostcase1 = CNumeric(PadQuotes(request.getParameter("lostcase1")));
			lostcase2 = CNumeric(PadQuotes(request.getParameter("lostcase2")));
			lostcase3 = CNumeric(PadQuotes(request.getParameter("lostcase3")));

			try {
				if (!preowned_id.equals("0")) {
					StrSql = "SELECT preowned_emp_id, preowned_branch_id "
							+ " FROM " + compdb(comp_id) + "axela_preowned "
							+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = preowned_branch_id "
							+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
							+ " WHERE 1 = 1 and preowned_id = " + preowned_id + BranchAccess + ExeAccess + ""
							+ " GROUP BY preowned_id ";
					// SOP("StrSql========222====="+StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						preowned_emp_id = crs.getString("preowned_emp_id");
						preowned_branch_id = crs.getString("preowned_branch_id");
					}
					crs.close();
				} else {
					StrHTML = "Update Permission Denied!";
					return;
				}

				if (!preowned_emp_id.equals("0") || emp_id.equals("1")) {
					// ============================================================================================================
					if (name.equals("txt_preowned_exp_close_date") && !preowned_dat.equals("")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							if (!isValidDateFormatShort(value)) {
								StrHTML = "<font color=\"red\">Enter Valid Close Date!</font>";
								return;
							}

							if (!preowned_dat.equals("") && !value.equals("") && Long.parseLong(ConvertShortDateToStr(preowned_dat)) > Long.parseLong(ConvertShortDateToStr(value))) {
								StrHTML = "Closing Date can't be less than the Date!";
								return;
							}
							String history_oldvalue = strToShortDate(ExecuteQuery("Select preowned_close_date from " + compdb(comp_id) + "axela_preowned"
									+ " where preowned_id = " + preowned_id + ""));

							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " SET"
									+ " preowned_close_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " SET"
									+ " preowned_trigger = '0'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CLOSING_DATE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Days Left: " + (int) getDaysBetween((ToShortDate(kknow())), ConvertShortDateToStr(value));
							StrHTML = StrHTML + "<br>Close Date updated!";
						} else {
							StrHTML = "Enter Close Date!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_preowned_title")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT preowned_title FROM " + compdb(comp_id) + "axela_preowned WHERE preowned_id=" + preowned_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
									+ " SET"
									+ " preowned_title = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);
							String history_actiontype = "TITLE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Title updated!";
						} else {
							StrHTML = "Enter Title!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_preowned_desc")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT preowned_desc FROM " + compdb(comp_id) + "axela_preowned WHERE preowned_id=" + preowned_id + " ");
							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
									+ " SET"
									+ " preowned_desc = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);
							String history_actiontype = "DESCRIPTION";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Description updated!";
						} else {
							StrHTML = "Enter Description!";
						}
					}
					// ============================================================================================================
					// Model list starts here===============================================
					// if (name.equals("dr_preowned_preownedmodel_id")) {
					// if (!value.equals("")) {
					// value = value.replaceAll("nbsp", "&");
					// String history_oldvalue = ExecuteQuery("Select model_name from " + compdb(comp_id) + "axela_preowned "
					// + "inner join " + compdb(comp_id) + "axela_inventory_item_model on model_id = preowned_preownedmodel_id where preowned_id=" + preowned_id + " ");
					//
					// StrSql = "Update " + compdb(comp_id) + "axela_preowned"
					// + " SET"
					// + " preowned_preownedmodel_id = '" + value + "'"
					// + " where preowned_id = " + preowned_id + "";
					// updateQuery(StrSql);
					// String history_newvalue = ExecuteQuery("Select model_name from " + compdb(comp_id) + "axela_inventory_item_model where model_id=" + value + " ");
					// String history_actiontype = "MODEL";
					//
					// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
					// + " (preownedhistory_preowned_id,"
					// + " preownedhistory_emp_id,"
					// + " preownedhistory_datetime,"
					// + " preownedhistory_actiontype,"
					// + " preownedhistory_oldvalue,"
					// + " preownedhistory_newvalue)"
					// + " VALUES ("
					// + " '" + preowned_id + "',"
					// + " '" + emp_id + "',"
					// + " '" + ToLongDate(kknow()) + "',"
					// + " '" + history_actiontype + "',"
					// + " '" + history_oldvalue + "',"
					// + " '" + history_newvalue + "')";
					// updateQuery(StrSql);
					// StrHTML = "Model updated!";
					// } else {
					// }
					// }
					// ============================================================================================================
					// if (name.equals("dr_preowned_variant_id")) {
					// if (!value.equals("")) {
					// value = value.replaceAll("nbsp", "&");
					// String history_oldvalue = ExecuteQuery("Select variant_name from " + compdb(comp_id) + "axela_preowned "
					// + " inner join axela_preowned_variant on variant_id = preowned_variant_id "
					// + " where preowned_id=" + preowned_id + " ");
					// StrSql = "Update " + compdb(comp_id) + "axela_preowned"
					// + " SET"
					// + " preowned_variant_id = '" + value + "'"
					// + " where preowned_id = " + preowned_id + "";
					// updateQuery(StrSql);
					// String history_newvalue = ExecuteQuery("Select variant_name from axela_preowned_variant where variant_id=" + value + " ");
					//
					// String history_actiontype = "VARIANT";
					//
					// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
					// + " (preownedhistory_preowned_id,"
					// + " preownedhistory_emp_id,"
					// + " preownedhistory_datetime,"
					// + " preownedhistory_actiontype,"
					// + " preownedhistory_oldvalue,"
					// + " preownedhistory_newvalue)"
					// + " VALUES ("
					// + " '" + preowned_id + "',"
					// + " '" + emp_id + "',"
					// + " '" + ToLongDate(kknow()) + "',"
					// + " '" + history_actiontype + "',"
					// + " '" + history_oldvalue + "',"
					// + " '" + history_newvalue + "')";
					// updateQuery(StrSql);
					// StrHTML = "Variant updated!";
					// } else {
					// StrHTML = "Select Variant!";
					// }
					// }
					// ============================================================================================================
					// //// Variant Starts here

					if (name.equals("preownedvariant")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT variant_name, preownedmodel_name "
									+ " FROM " + compdb(comp_id) + "axela_preowned "
									+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id "
									+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id "
									+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id "
									+ " WHERE preowned_id=" + preowned_id;
							// SOP("strsql===" + StrSql);
							CachedRowSet crs = processQuery(StrSql, 0);
							while (crs.next()) {
								variant_name = crs.getString("variant_name");
								preownedmodel_name = crs.getString("preownedmodel_name");
							}
							// SOP("preownedmodel_name===" + preownedmodel_name);
							// SOP("variant_name===" + variant_name);
							preownedmodel_id = ExecuteQuery("SELECT preownedmodel_id FROM axela_preowned_variant"
									+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
									+ " WHERE variant_id = " + value);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
									+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id"
									+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
									+ " SET preowned_variant_id = " + value + ""
									// + " preowned_preownedmodel_id =" + preownedmodel_id
									+ " where preowned_id =" + preowned_id;
							updateQuery(StrSql);
							String history_newvalue1 = ExecuteQuery("SELECT preownedmodel_name "
									+ " FROM " + compdb(comp_id) + "axela_preowned"
									+ " INNER JOIN axela_preowned_model ON preownedmodel_id = preowned_preownedmodel_id "
									+ " WHERE preowned_id = " + preowned_id);
							String history_newvalue2 = ExecuteQuery("SELECT variant_name "
									+ " FROM " + compdb(comp_id) + "axela_preowned"
									+ " INNER JOIN axela_preowned_variant ON variant_id = preowned_variant_id "
									+ " WHERE preowned_id = " + preowned_id);

							String history_actiontype1 = "MODEL";
							if (!preownedmodel_name.equals("")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES ("
										+ " '" + preowned_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype1 + "',"
										+ " '" + preownedmodel_name + "',"
										+ " '" + history_newvalue1 + "')";
								updateQuery(StrSql);
							}
							String history_actiontype2 = "VARIANT";
							if (!variant_name.equals("")) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES ("
										+ " '" + preowned_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype2 + "',"
										+ " '" + variant_name + "',"
										+ " '" + history_newvalue2 + "')";
								updateQuery(StrSql);
								StrHTML = "Variant updated!";
							}
						} else {
							StrHTML = "Select Variant!";
						}
					}
					// ///////////////////////////////////////////

					if (name.equals("dr_preowned_emp_id")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT emp_name "
									+ " FROM " + compdb(comp_id) + "axela_preowned "
									+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = preowned_emp_id "
									+ " WHERE preowned_id=" + preowned_id + " ");

							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
									+ " SET"
									+ " preowned_emp_id = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_crmfollowup"
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_id = precrmfollowup_preowned_id "
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_crmfollowupdays ON precrmfollowupdays_id = precrmfollowup_precrmfollowupdays_id"
									+ " SET precrmfollowup_crm_emp_id = COALESCE ((SELECT CASE "
									+ " WHEN precrmfollowupdays_exe_type = 1 THEN "
									+ " (CASE WHEN precrmfollowupdays_precrmtype_id = 1 THEN preownedteam_crm_emp_id "
									+ " WHEN precrmfollowupdays_precrmtype_id = 3 THEN preownedteam_psf_emp_id END)"
									+ " WHEN precrmfollowupdays_exe_type = 2 THEN preowned_emp_id"
									+ " WHEN precrmfollowupdays_exe_type = 3 THEN preownedteam_emp_id"
									+ " END"
									+ " FROM " + compdb(comp_id) + "axela_preowned_team "
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_team_exe ON preownedteamtrans_team_id = preownedteam_id"
									+ " WHERE preownedteam_branch_id = preowned_branch_id"
									+ " AND preownedteamtrans_emp_id = preowned_emp_id"
									+ " LIMIT 1),0)"
									+ " WHERE 1 = 1 "
									+ " AND precrmfollowup_desc = ''"
									+ " AND preowned_id = " + preowned_id;
							updateQuery(StrSql);
//							SOP("StrSql--------crm-----------" + StrSql);

							StrSql = " UPDATE " + compdb(comp_id) + "axela_preowned_followup"
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_id = preownedfollowup_preowned_id"
									+ " SET preownedfollowup_emp_id = '" + value + "'"
									+ " WHERE preownedfollowup_desc = ''"
									+ " AND preowned_id = " + preowned_id;
//							SOP("strsql========" + StrSql);
							updateQuery(StrSql);
							// //////

							String history_newvalue = ExecuteQuery("Select emp_name from " + compdb(comp_id) + "axela_emp where emp_id=" + value + " ");

							String history_actiontype = "PRE-OWNED CONSULTANT";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Pre-Owned Consultant updated!";
						} else {
							StrHTML = "Select Pre-Owned Consultant!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_preowned_customer_name")) {
						if (!value.equals("")) {
							if (value.length() < 3) {
								StrHTML = ("Enter atleast 3 Characters for Customer Name!");
								return;
							}
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT coalesce(customer_name,'') AS customer_name FROM " + compdb(comp_id) + "axela_preowned"
									+ " LEFT JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
									+ " WHERE preowned_id = " + preowned_id);

							StrSql = "UPDATE " + compdb(comp_id) + "axela_customer"
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_customer_id = customer_id"
									+ " SET"
									+ " customer_name = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "Customer";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Customer Name updated!";
						} else {
							StrHTML = "Enter Customer Name!";
						}
					}
					// ============================================================================================================
					if (name.equals("dr_title")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT title_desc "
									+ " FROM " + compdb(comp_id) + "axela_preowned "
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
									+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
									+ " WHERE preowned_id=" + preowned_id + " ");

							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned "
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
									+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id "
									+ " SET"
									+ " contact_title_id = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_newvalue = ExecuteQuery("SELECT title_desc "
									+ "FROM " + compdb(comp_id) + "axela_title WHERE title_id=" + value + " ");

							String history_actiontype = "CONTACT_TITLE";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "Title updated!";
						} else {
							StrHTML = "Select Title!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_fname")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT contact_fname "
									+ " FROM " + compdb(comp_id) + "axela_preowned "
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
									+ " WHERE preowned_id=" + preowned_id + " ");

							StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
									+ " SET"
									+ " contact_fname = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CONTACT_FNAME";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact fname updated!";
						} else {
							StrHTML = "Enter Contact fname!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_lname")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("SELECT contact_lname "
									+ " FROM " + compdb(comp_id) + "axela_preowned "
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
									+ " WHERE preowned_id=" + preowned_id + " ");

							StrSql = "UPDATE " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
									+ " SET"
									+ " contact_lname = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CONTACT_LNAME";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact lname updated!";
						} else {
							StrHTML = "Enter Contact lname!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_mobile1")) {
						if (!value.equals("") && IsValidMobileNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_mobile1 "
									+ " FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "' or contact_mobile2 = '" + value + "')"
									// + " and preowned_branch_id = " + preowned_branch_id
									+ " AND preowned_id!=" + preowned_id;
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "Similar Mobile 1 Found!";
							} else {
								String history_oldvalue = ExecuteQuery("SELECT contact_mobile1 "
										+ " FROM " + compdb(comp_id) + "axela_preowned "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
										+ " WHERE preowned_id=" + preowned_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
										+ " SET"
										+ " contact_mobile1 = '" + value + "',"
										+ " customer_mobile1 = '" + value + "'"
										+ " WHERE preowned_id = " + preowned_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CONTACT_MOBILE_1";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES ("
										+ " '" + preowned_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Mobile1 updated!";
							}
						} else {
							StrHTML = "Enter Valid Mobile1!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_mobile2")) {
						if (!value.equals("") && IsValidMobileNo11(value)) {
							value = value.replaceAll("nbsp", "&");
							StrSql = "SELECT contact_mobile2 "
									+ " FROM " + compdb(comp_id) + "axela_customer_contact"
									+ " INNER JOIN " + compdb(comp_id) + "axela_preowned ON preowned_contact_id = contact_id"
									+ " WHERE (contact_mobile1 = '" + value + "' or contact_mobile2 = '" + value + "')"
									+ " AND preowned_branch_id = " + preowned_branch_id
									+ " AND preowned_id!=" + preowned_id;
							if (!ExecuteQuery(StrSql).equals("")) {
								StrHTML = "Similar Mobile 2 Found!";
							} else {
								String history_oldvalue = ExecuteQuery("SELECT contact_mobile2 "
										+ " FROM " + compdb(comp_id) + "axela_preowned "
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
										+ " WHERE preowned_id=" + preowned_id + " ");

								StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
										+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
										+ " SET"
										+ " contact_mobile2 = '" + value + "',"
										+ " customer_mobile2 = '" + value + "'"
										+ " WHERE preowned_id = " + preowned_id + "";
								updateQuery(StrSql);

								String history_actiontype = "CONTACT_MOBILE_2";

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES ("
										+ " '" + preowned_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								StrHTML = "Contact Mobile2 updated!";
							}
						} else {
							StrHTML = "Enter Valid Mobile2!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_email1")) {
						if (!value.equals("") && IsValidEmail(value)) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select contact_email1 "
									+ " FROM " + compdb(comp_id) + "axela_preowned "
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id "
									+ " WHERE preowned_id=" + preowned_id + " ");

							StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = preowned_contact_id"
									+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = preowned_customer_id"
									+ " SET"
									+ " contact_email1 = '" + value + "',"
									+ " customer_email1 = '" + value + "'"
									+ " WHERE preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CONTACT_EMAIL_1";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Email1 updated!";
						} else {
							StrHTML = "Enter Valid Contact Email1!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_email2")) {
						if (!value.equals("") && IsValidEmail(value)) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select contact_email2 "
									+ " from " + compdb(comp_id) + "axela_preowned "
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
									+ " where preowned_id=" + preowned_id + " ");

							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id"
									+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = preowned_customer_id"
									+ " SET"
									+ " contact_email2 = '" + value + "',"
									+ " customer_email2 = '" + value + "'"
									+ " where preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CONTACT_EMAIL_2";
							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Email2 updated!";
						} else {
							StrHTML = "Enter Valid Contact Email2!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_address")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select contact_address "
									+ " from " + compdb(comp_id) + "axela_preowned "
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
									+ " where preowned_id=" + preowned_id + " ");

							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id"
									+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = preowned_customer_id"
									+ " SET"
									+ " contact_address = '" + value + "',"
									+ " customer_address = '" + value + "'"
									+ " where preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CONTACT_ADDRESS";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Address updated!";
						} else {
							StrHTML = "Enter Valid Contact Address!";
						}
					}
					// ============================================================================================================
					if (name.equals("maincity")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select city_name "
									+ " from " + compdb(comp_id) + "axela_preowned "
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
									+ " inner join " + compdb(comp_id) + "axela_city on city_id = contact_city_id "
									+ " where preowned_id=" + preowned_id + " ");

							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id"
									+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = preowned_customer_id"
									+ " SET"
									+ " contact_city_id = '" + value + "',"
									+ " customer_city_id = '" + value + "'"
									+ " where preowned_id = " + preowned_id + "";
							updateQuery(StrSql);
							String history_newvalue = ExecuteQuery("Select city_name from " + compdb(comp_id) + "axela_city where city_id=" + value + " ");

							String history_actiontype = "CONTACT_CITY";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + history_newvalue + "')";
							updateQuery(StrSql);
							StrHTML = "City updated!";
						} else {
							StrHTML = "Select City!";
						}
					}
					// ============================================================================================================
					if (name.equals("txt_contact_pin")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select contact_pin "
									+ " from " + compdb(comp_id) + "axela_preowned "
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id "
									+ " where preowned_id=" + preowned_id + " ");

							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " inner join " + compdb(comp_id) + "axela_customer_contact on contact_id = preowned_contact_id"
									+ " inner join " + compdb(comp_id) + "axela_customer on customer_id = preowned_customer_id"
									+ " SET"
									+ " contact_pin = '" + value + "',"
									+ " customer_pin = '" + value + "'"
									+ " where preowned_id = " + preowned_id + "";
							updateQuery(StrSql);

							String history_actiontype = "CONTACT_PIN";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Contact Pin updated!";
						} else {
							StrHTML = "Enter Contact Pin!";
						}
					}
					// ============================================================================================================
					// ////////////////////////enquiry Status//////////////////////////////
					if (name.equals("dr_preowned_preownedstatus_id")) {
						status_id = status_id.replaceAll("nbsp", "&");
						String Str = "";
						// String enquiry_edit = ReturnPerm(comp_id,
						// "emp_close_enquiry", request);
						String preowned_edit = CNumeric(ExecuteQuery("SELECT emp_close_enquiry FROM " + compdb(comp_id) + "axela_emp"
								+ " WHERE emp_id=" + emp_id));
						if (status_id.equals("2")) {
							Str = "<font color=\"red\">Update Permission Denied!</font><br>";
						}
						else if ((status_id.equals("3") || status_id.equals("4")) && preowned_edit.equals("0"))
						{
							Str = "<font color=\"red\">Update Permission Denied!</font><br>";
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
								StrSql = "Select preownedlostcase2_id from " + compdb(comp_id) + "axela_preowned_lostcase2"
										+ " where preownedlostcase2_lostcase1_id = " + lostcase1 + "";
								if (!ExecuteQuery(StrSql).equals("")) {
									if (lostcase2.equals("0")) {
										Str = Str + "Select Lost Case 2!<br>";
									}
								}
								StrSql = "Select preownedlostcase3_id from " + compdb(comp_id) + "axela_preowned_lostcase3"
										+ " where preownedlostcase3_lostcase2_id = " + lostcase2 + "";
								// SOP("Str 123= " + ExecuteQuery(StrSql));
								// SOP("Str lostcase3= " + lostcase3);
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

							StrSql = "Select preowned_preownedstatus_id, preownedstatus_name,"
									+ " coalesce(preownedlostcase1_name, '') AS preownedlostcase1_name, "
									+ " coalesce(preownedlostcase2_name,'') AS preownedlostcase2_name,"
									+ " coalesce(preownedlostcase3_name, '') AS preownedlostcase3_name "
									+ " from " + compdb(comp_id) + "axela_preowned"
									+ " inner join " + compdb(comp_id) + "axela_preowned_status on preownedstatus_id = preowned_preownedstatus_id"
									+ " left join " + compdb(comp_id) + "axela_preowned_lostcase1 on preownedlostcase1_id  = preowned_preownedlostcase1_id"
									+ " left join " + compdb(comp_id) + "axela_preowned_lostcase2 on preownedlostcase2_id  = preowned_preownedlostcase2_id"
									+ " left join " + compdb(comp_id) + "axela_preowned_lostcase3 on preownedlostcase3_id  = preowned_preownedlostcase3_id"
									+ " where preowned_id = " + preowned_id + " ";
							// SOP("StrSql----------" + StrSql);
							crs = processQuery(StrSql, 0);
							while (crs.next()) {
								historyoldvalue_status_id = crs.getString("preowned_preownedstatus_id");
								historyoldvalue_status_name = crs.getString("preownedstatus_name");
								historyoldvalue_lostcase1 = crs.getString("preownedlostcase1_name");
								historyoldvalue_lostcase2 = crs.getString("preownedlostcase2_name");
								historyoldvalue_lostcase3 = crs.getString("preownedlostcase3_name");
							}
							crs.close();
							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " set preowned_preownedstatus_id = " + status_id + ","
									+ " preowned_preownedstatus_date = '" + ToLongDate(kknow()) + "',"
									+ " preowned_preownedlostcase1_id = " + lostcase1 + ","
									+ " preowned_preownedlostcase2_id = " + lostcase2 + ","
									+ " preowned_preownedlostcase3_id = " + lostcase3 + ""
									+ " where preowned_id = " + preowned_id;
							// SOP("StrSql-----update pre-----" + StrSql);
							updateQuery(StrSql);

							StrSql = "Select preowned_preownedstatus_id, preownedstatus_name,"
									+ " coalesce(preownedlostcase1_name, '') preownedlostcase1_name, "
									+ " coalesce(preownedlostcase2_name,'') preownedlostcase2_name,"
									+ " coalesce(preownedlostcase3_name, '') preownedlostcase3_name "
									+ " from " + compdb(comp_id) + "axela_preowned"
									+ " inner join " + compdb(comp_id) + "axela_preowned_status on preownedstatus_id = preowned_preownedstatus_id"
									+ " left join " + compdb(comp_id) + "axela_preowned_lostcase1 on preownedlostcase1_id  = preowned_preownedlostcase1_id"
									+ " left join " + compdb(comp_id) + "axela_preowned_lostcase2 on preownedlostcase2_id  = preowned_preownedlostcase2_id"
									+ " left join " + compdb(comp_id) + "axela_preowned_lostcase3 on preownedlostcase3_id  = preowned_preownedlostcase3_id"
									+ " where preowned_id = " + preowned_id + " ";
							// SOP("StrSql-----after update----" + StrSqlBreaker(StrSql));
							crs = processQuery(StrSql, 0);
							while (crs.next()) {
								historynewvalue_status_name = crs.getString("preownedstatus_name");
								historynewvalue_lostcase1 = crs.getString("preownedlostcase1_name");
								historynewvalue_lostcase2 = crs.getString("preownedlostcase2_name");
								historynewvalue_lostcase3 = crs.getString("preownedlostcase3_name");
							}
							crs.close();

							historyactiontype_status_name = "STATUS";
							if (!status_id.equals(historyoldvalue_status_id)) {
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES"
										+ " ('" + preowned_id + "',"
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
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES"
										+ " ('" + preowned_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + historyactiontype_lostcase1 + "',"
										+ " '" + historyoldvalue_lostcase1 + "',"
										+ " '" + historynewvalue_lostcase1 + "')";
								// SOP("StrSql==" + StrSql);
								updateQuery(StrSql);

								historyactiontype_lostcase2 = "LOST CASE 2";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES"
										+ " ('" + preowned_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + historyactiontype_lostcase2 + "',"
										+ " '" + historyoldvalue_lostcase2 + "',"
										+ " '" + historynewvalue_lostcase2 + "')";
								// SOP("StrSql==" + StrSql);
								updateQuery(StrSql);

								historyactiontype_lostcase3 = "LOST CASE 3";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
										+ " (preownedhistory_preowned_id,"
										+ " preownedhistory_emp_id,"
										+ " preownedhistory_datetime,"
										+ " preownedhistory_actiontype,"
										+ " preownedhistory_oldvalue,"
										+ " preownedhistory_newvalue)"
										+ " VALUES"
										+ " ('" + preowned_id + "',"
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
								String preowned_date = ToLongDate(kknow());
								SOP("preowned_date===" + preowned_date);
								new Preowned_Quickadd().AddPreownedCRMFollowupFields(preowned_id, preowned_date, "lost", "1", comp_id);
							}
							StrHTML = "Status Updated!";
						}
					}
					// //////

					// /////////////////////// Status Description //////////////
					if (name.equals("txt_preowned_preownedstatus_desc")) {
						value = value.replaceAll("nbsp", "&");
						if (!value.equals("")) {
							String history_oldvalue = ExecuteQuery("Select preowned_preownedstatus_desc"
									+ " from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");

							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " SET"
									+ " preowned_preownedstatus_desc = '" + value + "'"
									+ " where preowned_id = " + preowned_id + "";
							updateQuery(StrSql);
							String history_actiontype = "STATUS DESCRIPTION";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Status Description updated!";
						} else {
							StrHTML = "<font color=\"red\">Enter Status Description!</font>";
						}
					}

					if (name.equals("dr_preowned_lostcase1_id")) {
						StringBuilder Str = new StringBuilder();
						StrSql = "Select preownedlostcase2_id, preownedlostcase2_name"
								+ " from " + compdb(comp_id) + "axela_preowned_lostcase2"
								+ " where 1=1"
								+ " and preownedlostcase2_lostcase1_id = " + value
								+ " order by preownedlostcase2_name";
						// SOP("StrSql=lost2=" + StrSql);
						CachedRowSet crs = processQuery(StrSql, 0);
						Str.append("<select name=\"dr_preowned_lostcase2_id\" id=\"dr_preowned_lostcase2_id\" class=\"form-control\""
								+ " onChange=\"populateLostCase3('dr_preowned_lostcase2_id',this,'span_lostcase3');StatusUpdate();\">\n");
						Str.append("<option value=0>Select</option>");
						while (crs.next()) {
							Str.append("<option value=").append(
									crs.getString("preownedlostcase2_id"));
							Str.append(">")
									.append(crs.getString("preownedlostcase2_name"))
									.append("</option>\n");
						}
						Str.append("</select>\n");
						crs.close();
						StrHTML = Str.toString();
					}
					if (name.equals("dr_preowned_lostcase2_id")) {
						StringBuilder Str = new StringBuilder();
						StrSql = "Select preownedlostcase3_id, preownedlostcase3_name"
								+ " from " + compdb(comp_id) + "axela_preowned_lostcase3"
								+ " where 1=1"
								+ " and preownedlostcase3_lostcase2_id = " + value
								+ " order by preownedlostcase3_name";
						CachedRowSet crs = processQuery(StrSql, 0);
						Str.append("<select name=\"dr_preowned_lostcase3_id\" id=\"dr_preowned_lostcase3_id\" class=\"form-control\""
								+ " onChange=\"StatusUpdate();\">\n");
						Str.append("<option value=0>Select</option>");
						while (crs.next()) {
							Str.append("<option value=").append(
									crs.getString("preownedlostcase3_id"));
							Str.append(">")
									.append(crs.getString("preownedlostcase3_name"))
									.append("</option>\n");
						}
						Str.append("</select>\n");
						crs.close();
						StrHTML = Str.toString();
					}
					// ============================================================================================================
					if (name.equals("txt_preowned_notes")) {
						if (!value.equals("")) {
							value = value.replaceAll("nbsp", "&");
							String history_oldvalue = ExecuteQuery("Select preowned_notes from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
							StrSql = "Update " + compdb(comp_id) + "axela_preowned"
									+ " SET"
									+ " preowned_notes = '" + value + "'"
									+ " where preowned_id = " + preowned_id + "";
							updateQuery(StrSql);
							String history_actiontype = "NOTES";

							StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
									+ " (preownedhistory_preowned_id,"
									+ " preownedhistory_emp_id,"
									+ " preownedhistory_datetime,"
									+ " preownedhistory_actiontype,"
									+ " preownedhistory_oldvalue,"
									+ " preownedhistory_newvalue)"
									+ " VALUES ("
									+ " '" + preowned_id + "',"
									+ " '" + emp_id + "',"
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + history_actiontype + "',"
									+ " '" + history_oldvalue + "',"
									+ " '" + value + "')";
							updateQuery(StrSql);
							StrHTML = "Notes updated!";
						} else {
							StrHTML = "Enter Notes !";
						}
					}
					// ============================================================================================================
					// if (!preowned_preownedmodel_id.equals("0") && name.equals("")) {
					// StringBuilder Str = new StringBuilder();
					// try {
					// StrSql = "Select variant_id, variant_name"
					// + " from axela_preowned_variant"
					// + " where 1=1 "
					// + " and variant_preownedmodel_id = " + preowned_preownedmodel_id
					// + " order by variant_name";
					// // SOP("StrSql==" + StrSql);
					// CachedRowSet crs =processQuery(StrSql, 0);
					// Str.append("<select name=\"dr_preowned_variant_id\" id=\"dr_preowned_variant_id\" class=\"selectbox\" onChange=\"SecurityCheck('dr_preowned_variant_id',this,'hint_dr_preowned_variant_id');\" >");
					// while (crs.next()) {
					// Str.append("<option value=").append(crs.getString("variant_id")).append("");
					// Str.append(">").append(crs.getString("variant_name")).append("</option> \n");
					// }
					// Str.append("</select>");
					// crs.close();
					// String variant = "";
					// variant = ExecuteQuery("select variant_id from axela_preowned_variant where 1=1 "
					// + " and variant_preownedmodel_id = " + preowned_preownedmodel_id
					// + " order by variant_name limit 1");
					// String history_oldvalue = ExecuteQuery("Select variant_name from " + compdb(comp_id) + "axela_preowned "
					// + " inner join axela_preowned_variant on variant_id = preowned_variant_id "
					// + " where preowned_id=" + preowned_id + " ");
					// String history_newvalue = "";
					// if (!variant.equals("")) {
					// StrSql = "Update " + compdb(comp_id) + "axela_preowned"
					// + " SET"
					// + " preowned_variant_id = '" + variant + "'"
					// + " where preowned_id = " + preowned_id + "";
					// updateQuery(StrSql);
					//
					// history_newvalue = ExecuteQuery("Select variant_name from axela_preowned_variant where variant_id=" + variant + " ");
					// }
					// String history_actiontype = "VARIANT";
					//
					// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
					// + " (preownedhistory_preowned_id,"
					// + " preownedhistory_emp_id,"
					// + " preownedhistory_datetime,"
					// + " preownedhistory_actiontype,"
					// + " preownedhistory_oldvalue,"
					// + " preownedhistory_newvalue)"
					// + " VALUES ("
					// + " '" + preowned_id + "',"
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
					// StrHTML = StrHTML + "<br>Variant updated!";
					// }
				} else {
					StrHTML = "Update Permission Denied!";
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
			// ============================================================================================================
			if (name.equals("dr_preowned_fueltype_id")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("select fueltype_name"
							+ " from " + compdb(comp_id) + "axela_fueltype"
							+ " inner join " + compdb(comp_id) + "axela_preowned on preowned_fueltype_id = fueltype_id"
							+ " where  preowned_id = " + preowned_id + " ");

					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_fueltype_id = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_newvalue = ExecuteQuery("Select fueltype_name from " + compdb(comp_id) + "axela_fueltype"
							+ " where fueltype_id = " + value + "");
					String history_actiontype = "FUEL";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Fuel updated!";
				} else {
				}
			}
			// ==================================================================================================
			if (name.equals("txt_preowned_options")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select preowned_options from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_options = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_actiontype = "OPTIONS";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Options updated!";
				} else {
				}
			}
			// =========================================================================================================
			if (name.equals("txt_preowned_sub_variant")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select preowned_sub_variant from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_sub_variant = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_actiontype = "SUB VARIANT";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Sub Variant updated!";
				} else {
				}
			}
			// =================================================================================================================
			if (name.equals("dr_preowned_extcolour_id")) {
				if (!value.equals("0")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("select extcolour_name"
							+ " from axela_preowned_extcolour"
							+ " inner join " + compdb(comp_id) + "axela_preowned on preowned_extcolour_id = extcolour_id"
							+ " where  preowned_id = " + preowned_id + " ");

					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_extcolour_id = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_newvalue = ExecuteQuery("Select extcolour_name from axela_preowned_extcolour where extcolour_id=" + value + " ");
					String history_actiontype = "EXTERIOR";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Exterior updated!";
				} else {
					StrHTML = "Select Exterior!";
				}
			}
			// ========================================================================================================
			if (name.equals("dr_preowned_intcolour_id")) {
				if (!value.equals("0")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("select intcolour_name"
							+ " from axela_preowned_intcolour"
							+ " inner join " + compdb(comp_id) + "axela_preowned on preowned_intcolour_id = intcolour_id"
							+ " where  preowned_id = " + preowned_id + " ");

					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_intcolour_id = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_newvalue = ExecuteQuery("Select intcolour_name from axela_preowned_intcolour where intcolour_id=" + value + " ");
					String history_actiontype = "INTERIOR";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Interior updated!";
				} else {
					StrHTML = "Select Interior!";
				}
			}
			// ==============================================================================================================
			if (name.equals("dr_preowned_insurance_id")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("select insurance_name"
							+ " from " + compdb(comp_id) + "axela_preowned_insurance"
							+ " inner join " + compdb(comp_id) + "axela_preowned on preowned_insurance_id = insurance_id"
							+ " where  preowned_id = " + preowned_id + " ");

					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_insurance_id = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_newvalue = ExecuteQuery("Select insurance_name from " + compdb(comp_id) + "axela_preowned_insurance where insurance_id=" + value + " ");
					String history_actiontype = "INSURANCE";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Insurance updated!";
				} else {
				}
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_regno")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					value = value.replaceAll(" ", "");
					value = value.replaceAll("-", "");

					// StrSql = "SELECT preowned_regno FROM " + compdb(comp_id) + "axela_preowned"
					// + " WHERE preowned_regno = '" + value + "'"
					// + " and preowned_id != " + preowned_id + "";
					if (!IsValidUsername(value).equals("")) {
						StrHTML = "Enter valid Registration No.!";
					}
					// else if (!ExecuteQuery(StrSql).equals("")) {
					// StrHTML = "Similar Registration Number Found!";
					// }
					else {
						String history_oldvalue = ExecuteQuery("Select preowned_regno from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
						StrSql = "Update " + compdb(comp_id) + "axela_preowned"
								+ " SET"
								+ " preowned_regno = '" + value.replace(" ", "") + "'"
								+ " where preowned_id = " + preowned_id + "";
						updateQuery(StrSql);
						String history_actiontype = "REG_NO.";

						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
								+ " (preownedhistory_preowned_id,"
								+ " preownedhistory_emp_id,"
								+ " preownedhistory_datetime,"
								+ " preownedhistory_actiontype,"
								+ " preownedhistory_oldvalue,"
								+ " preownedhistory_newvalue)"
								+ " VALUES ("
								+ " '" + preowned_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + history_actiontype + "',"
								+ " '" + history_oldvalue + "',"
								+ " '" + value + "')";
						updateQuery(StrSql);
						StrHTML = "Reg No. updated!";
					}
				} else {
					StrHTML = "Enter Reg No.!";
				}
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_kms")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select preowned_kms from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_kms = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_actiontype = "KMS";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Kms updated!";
				} else {
					StrHTML = "Enter Kms!";
				}
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_fcamt")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("Select preowned_fcamt from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");

				StrSql = "Update " + compdb(comp_id) + "axela_preowned"
						+ " SET"
						+ " preowned_fcamt = '" + CNumeric(value) + "'"
						+ " where preowned_id = " + preowned_id + "";
				updateQuery(StrSql);

				String history_actiontype = "Foreclosure Amount";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
						+ " (preownedhistory_preowned_id,"
						+ " preownedhistory_emp_id,"
						+ " preownedhistory_datetime,"
						+ " preownedhistory_actiontype,"
						+ " preownedhistory_oldvalue,"
						+ " preownedhistory_newvalue)"
						+ " VALUES ("
						+ " '" + preowned_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + preowned_fcamt + "')";
				updateQuery(StrSql);
				StrHTML = "Foreclosure Amt updated!";

			}
			// ============================================================================================================
			if (name.equals("dr_preowned_noc")) {
				// if (!value.equals("0")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("Select preowned_noc from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
				if (history_oldvalue.equals("2")) {
					history_oldvalue = "No";
				} else if (history_oldvalue.equals("1")) {
					history_oldvalue = "Yes";
				} else {
					history_oldvalue = " ";
				}
				StrSql = "Update " + compdb(comp_id) + "axela_preowned"
						+ " SET"
						+ " preowned_noc = '" + value + "'"
						+ " where preowned_id = " + preowned_id + "";
				// SOP("StrSql==="+StrSql);
				updateQuery(StrSql);
				String history_actiontype = "NOC";

				if (value.equals("2")) {
					value = "No";
				} else if (value.equals("1")) {
					value = "Yes";
				} else {
					value = "";
				}
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
						+ " (preownedhistory_preowned_id,"
						+ " preownedhistory_emp_id,"
						+ " preownedhistory_datetime,"
						+ " preownedhistory_actiontype,"
						+ " preownedhistory_oldvalue,"
						+ " preownedhistory_newvalue)"
						+ " VALUES ("
						+ " '" + preowned_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "NOC updated!";
				// }
				// } else {
				// StrHTML = "Enter NOC!";
				// }
			}

			// ============================================================================================================
			if (name.equals("txt_preowned_funding_bank")) {
				// if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");

				String history_oldvalue = ExecuteQuery("Select preowned_funding_bank from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
				StrSql = "Update " + compdb(comp_id) + "axela_preowned"
						+ " SET"
						+ " preowned_funding_bank = '" + value + "'"
						+ " where preowned_id = " + preowned_id + "";
				updateQuery(StrSql);
				String history_actiontype = "FUNDING_BANK";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
						+ " (preownedhistory_preowned_id,"
						+ " preownedhistory_emp_id,"
						+ " preownedhistory_datetime,"
						+ " preownedhistory_actiontype,"
						+ " preownedhistory_oldvalue,"
						+ " preownedhistory_newvalue)"
						+ " VALUES ("
						+ " '" + preowned_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Funding Bank updated!";
				// } else {
				// StrHTML = "Enter Funding Bank!";
				// }
				// }
			}

			// ============================================================================================================
			if (name.equals("txt_preowned_loan_no")) {
				// if (!value.equals("")) {
				value = value.replaceAll("nbsp", "&");
				String history_oldvalue = ExecuteQuery("Select preowned_loan_no from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
				StrSql = "Update " + compdb(comp_id) + "axela_preowned"
						+ " SET"
						+ " preowned_loan_no = '" + value + "'"
						+ " where preowned_id = " + preowned_id + "";
				updateQuery(StrSql);
				String history_actiontype = "LOAN NO.";

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
						+ " (preownedhistory_preowned_id,"
						+ " preownedhistory_emp_id,"
						+ " preownedhistory_datetime,"
						+ " preownedhistory_actiontype,"
						+ " preownedhistory_oldvalue,"
						+ " preownedhistory_newvalue)"
						+ " VALUES ("
						+ " '" + preowned_id + "',"
						+ " '" + emp_id + "',"
						+ " '" + ToLongDate(kknow()) + "',"
						+ " '" + history_actiontype + "',"
						+ " '" + history_oldvalue + "',"
						+ " '" + value + "')";
				updateQuery(StrSql);
				StrHTML = "Loan No. updated!<input name=\"preowned_loan_no\" type=\"hidden\" class=\"textbox\"  id =\"preowned_loan_no\" value = \"loan\">";
				// } else {
				// StrHTML = "Enter Loan No.!";
				// }
				// }
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_invoicevalue")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select preowned_invoicevalue from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_invoicevalue = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_actiontype = "INVOICE_VALUE";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Invoice Value updated!";
				} else {
					StrHTML = "Enter Invoice Value!";
				}
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_expectedprice")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select preowned_expectedprice from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_expectedprice = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_actiontype = "EXPECTED_VALUE";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Expected Price updated!";
				} else {
					StrHTML = "Enter Expected Price!";
				}
			}
			// ============================================================================================================
			// if (name.equals("txt_preowned_quotedprice")) {
			// if (!value.equals("")) {
			// value = value.replaceAll("nbsp", "&");
			// String history_oldvalue = ExecuteQuery("Select preowned_quotedprice from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
			// StrSql = "Update " + compdb(comp_id) + "axela_preowned"
			// + " SET"
			// + " preowned_quotedprice = '" + value + "'"
			// + " where preowned_id = " + preowned_id + "";
			// updateQuery(StrSql);
			// String history_actiontype = "QUOTED_PRICE";
			//
			// StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
			// + " (preownedhistory_preowned_id,"
			// + " preownedhistory_emp_id,"
			// + " preownedhistory_datetime,"
			// + " preownedhistory_actiontype,"
			// + " preownedhistory_oldvalue,"
			// + " preownedhistory_newvalue)"
			// + " VALUES ("
			// + " '" + preowned_id + "',"
			// + " '" + emp_id + "',"
			// + " '" + ToLongDate(kknow()) + "',"
			// + " '" + history_actiontype + "',"
			// + " '" + history_oldvalue + "',"
			// + " '" + value + "')";
			// updateQuery(StrSql);
			// StrHTML = "Quoted Price updated!";
			// } else {
			// StrHTML = "Enter Quoted Price!";
			// }
			// }
			// ============================================================================================================
			if (name.equals("dr_preowned_ownership_id")) {
				if (!value.equals("0")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("select ownership_id, ownership_name"
							+ " from " + compdb(comp_id) + "axela_preowned_ownership "
							+ " inner join " + compdb(comp_id) + "axela_preowned on preowned_ownership_id = ownership_id"
							+ " where  preowned_id = " + preowned_id + " ");

					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_ownership_id = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_newvalue = ExecuteQuery("Select ownership_name from " + compdb(comp_id) + "axela_preowned_ownership where ownership_id=" + value + " ");
					String history_actiontype = "OWNERSHIP";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Ownership updated!";
				} else {
					StrHTML = " Select Ownership!";
				}
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_manufyear")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select preowned_manufyear from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_manufyear = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_actiontype = "MANUFACTURED_YEAR";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Manfactured Year updated!";
				} else {
					StrHTML = "Enter Manfactured Year!";
				}
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_regdyear")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select preowned_regdyear from " + compdb(comp_id) + "axela_preowned where preowned_id=" + preowned_id + " ");
					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_regdyear = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_actiontype = "REGISTERED_YEAR";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					StrHTML = "Registered Year updated!";
				} else {
					StrHTML = "Enter Registered Year!";
				}
			}
			// ============================================================================================================
			if (name.equals("txt_preowned_insur_date")) {
				if (!value.equals("")) {
					value = value.replaceAll("nbsp", "&");
					if (!isValidDateFormatShort(value)) {
						StrHTML = "<font color=\"red\">Enter Valid Insurance Date!</font>";
						return;
					}
					String history_oldvalue = strToShortDate(ExecuteQuery("Select preowned_insur_date from " + compdb(comp_id) + "axela_preowned"
							+ " where preowned_id = " + preowned_id + ""));

					StrSql = "Update " + compdb(comp_id) + "axela_preowned"
							+ " SET"
							+ " preowned_insur_date = '" + Long.parseLong(ConvertShortDateToStr(value)) + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);

					String history_actiontype = "INSURANCE_DATE";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + value + "')";
					updateQuery(StrSql);
					// StrHTML = "Days Left: " + (int) getDaysBetween((ToShortDate(kknow())), ConvertShortDateToStr(value));
					StrHTML = "Insurance Date updated!";
				} else {
					StrHTML = "Enter Insurance Date!";
				}
			}
			// ============================================================================================================
			if (name.equals("dr_preowned_prioritypreowned_id")) {

				if (!value.equals("0")) {
					value = value.replaceAll("nbsp", "&");
					String history_oldvalue = ExecuteQuery("Select prioritypreowned_name "
							+ "from " + compdb(comp_id) + "axela_preowned "
							+ "inner join " + compdb(comp_id) + "axela_preowned_priority on prioritypreowned_id = preowned_prioritypreowned_id where preowned_id=" + preowned_id + " ");

					StrSql = "Update " + compdb(comp_id) + "axela_preowned_priority"
							+ " inner join " + compdb(comp_id) + "axela_preowned on preowned_prioritypreowned_id = prioritypreowned_id"
							+ " SET"
							+ " preowned_prioritypreowned_id = '" + value + "'"
							+ " where preowned_id = " + preowned_id + "";
					updateQuery(StrSql);
					String history_newvalue = ExecuteQuery("Select prioritypreowned_name from " + compdb(comp_id) + "axela_preowned_priority where prioritypreowned_id=" + value + " ");

					String history_actiontype = "preowned_PRIORITY";

					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_history"
							+ " (preownedhistory_preowned_id,"
							+ " preownedhistory_emp_id,"
							+ " preownedhistory_datetime,"
							+ " preownedhistory_actiontype,"
							+ " preownedhistory_oldvalue,"
							+ " preownedhistory_newvalue)"
							+ " VALUES ("
							+ " '" + preowned_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',"
							+ " '" + history_oldvalue + "',"
							+ " '" + history_newvalue + "')";
					updateQuery(StrSql);
					StrHTML = "Priority updated!";
				} else {
					StrHTML = "Select Priority!";
				}
			}
		}

		// ============================================================================================================
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
