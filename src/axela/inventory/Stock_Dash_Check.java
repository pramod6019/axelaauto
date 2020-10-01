package axela.inventory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Stock_Dash_Check extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String msg = "";
	public String branch_id = "0";
	public String vehstock_id = "0";
	public String branch_brand_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String name = "";
	public String value = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String chk_delstatus_id = "0";
	// for Tabs
	public String history = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			BranchAccess = GetSession("BranchAccess", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				name = PadQuotes(request.getParameter("name"));
				value = PadQuotes(request.getParameter("value"));
				vehstock_id = PadQuotes(request.getParameter("vehstock_id"));
				// SOP("vehstock_id=== " + vehstock_id);
				branch_brand_id = CNumeric(ExecuteQuery("SELECT model_brand_id "
						+ " FROM " + compdb(comp_id) + "axela_vehstock"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = vehstock_item_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
						+ " WHERE vehstock_id =" + vehstock_id));
				// for tabs
				history = PadQuotes(request.getParameter("history"));
				try {
					if (!vehstock_id.equals("0")) {
						if (history.equals("yes")) {
							StrHTML = new Stock_Dash().HistoryDetails(comp_id, vehstock_id);
						}
						if (ReturnPerm(comp_id, "emp_stock_access", request).equals("1")) {

							if (name.equals("dr_vehstock_branch_id")) {
								if (!value.equals("0")) {
									value = value.replaceAll("nbsp", "&");
									String oldBranchId = ExecuteQuery("SELECT vehstock_branch_id"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id= " + vehstock_id + " ");
									String history_oldvalue = ExecuteQuery("SELECT  branch_name"
											+ " FROM " + compdb(comp_id) + "axela_branch"
											+ " WHERE branch_id= " + oldBranchId + " ");
									String branch_name = ExecuteQuery("SELECT  branch_name"
											+ " FROM " + compdb(comp_id) + "axela_branch"
											+ " WHERE branch_id= " + value + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_branch_id = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									// SOP("Sql up" + StrSql);
									updateQuery(StrSql);
									String history_actiontype = "Branch";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + branch_name + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Branch Updated!";
								} else {
									StrHTML = "<font color=\"red\">Select Branch!</font>";
								}
							}

							if (name.equals("dr_vehstock_vehstocklocation_id")) {
								if (!value.equals("0")) {
									value = value.replaceAll("nbsp", "&");
									String oldBranchId = ExecuteQuery("SELECT vehstock_vehstocklocation_id"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id= " + vehstock_id + " ");
									String history_oldvalue = ExecuteQuery("SELECT  vehstocklocation_name"
											+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
											+ " WHERE vehstocklocation_id= " + oldBranchId + " ");
									String vehstocklocation_name = ExecuteQuery("SELECT  vehstocklocation_name"
											+ " FROM " + compdb(comp_id) + "axela_vehstock_location"
											+ " WHERE vehstocklocation_id= " + value + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_vehstocklocation_id = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									// SOP("Sql up" + StrSql);
									updateQuery(StrSql);
									String history_actiontype = "Location";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + vehstocklocation_name + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Location Updated!";
								}
								else {
									StrHTML = "<font color=\"red\">Select Location!</font>";
								}
							}

							if (name.equals("dr_vehstock_item_id")) {
								if (!value.equals("0")) {
									value = value.replaceAll("nbsp", "&");
									String oldBranchId = ExecuteQuery("SELECT vehstock_item_id"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id= " + vehstock_id + " ");
									String history_oldvalue = ExecuteQuery("SELECT  item_name"
											+ " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE item_id= " + oldBranchId + " ");
									String item_name = ExecuteQuery("SELECT  item_name"
											+ " FROM " + compdb(comp_id) + "axela_inventory_item"
											+ " WHERE item_id= " + value + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_item_id = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									// SOP("Sql up" + StrSql);
									updateQuery(StrSql);
									String history_actiontype = "Item";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + item_name + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Item Updated!";
								}
								else {
									StrHTML = "<font color=\"red\">Select Item!</font>";
								}
							}

							if (name.equals("txt_vehstock_modelyear")) {
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");
									StrSql = "SELECT vehstock_entry_date"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id=" + vehstock_id + "";
									if (!value.equals("") && !ExecuteQuery(StrSql).equals("")) {
										if (Integer.parseInt(CNumeric(value)) > ((Integer.parseInt(SplitYear(ExecuteQuery(StrSql)))) + 1)) {
											StrHTML = "<font color=\"red\">Model Year Should be less than current Year!</font>";
										}
									}
									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT vehstock_modelyear"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id=" + vehstock_id + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_modelyear = '" + value + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										updateQuery(StrSql);
										String history_actiontype = "Model Year";
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Model Year Updated!";
									}
								}
								else {
									StrHTML = "<font color=\"red\">Select Model Year!</font>";
								}
							}

							if (branch_brand_id.equals("52") || branch_brand_id.equals("55") || branch_brand_id.equals("56")) {
								if (name.equals("txt_vehstock_mfgyear")) {
									value = value.replaceAll("nbsp", "&");
									String history_oldvalue = ExecuteQuery("SELECT vehstock_mfgyear"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id=" + vehstock_id + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_mfgyear = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Manufacturing Year";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Manufacturing Year Updated!";
								}

								if (name.equals("txt_vehstock_ref_no")) {
									value = value.replaceAll("nbsp", "&");
									StrSql = "SELECT vehstock_ref_no FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_ref_no = '" + value + "'"
											+ " AND vehstock_id = " + vehstock_id + "";
									CachedRowSet crs = processQuery(StrSql, 0);
									if (crs.isBeforeFirst()) {
										StrHTML = "<font color=\"red\">Similar Reference Number found!</font>";
									}
									crs.close();

									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT vehstock_ref_no"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id=" + vehstock_id + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_ref_no = '" + value + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										updateQuery(StrSql);
										String history_actiontype = "Reference No.";
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Reference No. Updated!";
									}
								}

								if (name.equals("txt_vehstock_comm_no")) {
									value = value.replaceAll("nbsp", "&");

									if (value.equals("")) {
										StrHTML = "<font color=\"red\">Commission Number cannot be empty!</font>";
									}

									StrSql = "SELECT vehstock_comm_no FROM " + compdb(comp_id) + "axela_vehstock"
											+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id =  vehstock_branch_id"
											+ " WHERE vehstock_comm_no = '" + value + "'"
											+ " AND vehstock_id = " + vehstock_id
											+ " AND branch_brand_id = " + branch_brand_id;
									CachedRowSet crs = processQuery(StrSql, 0);
									if (crs.isBeforeFirst()) {
										StrHTML = "<font color=\"red\">Similar Commission Number found!</font>";
									}
									crs.close();
									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT vehstock_comm_no"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id=" + vehstock_id + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_comm_no = '" + value + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										updateQuery(StrSql);
										String history_actiontype = "Commission No.";
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Commission No. Updated!";
									}
								}

								if (name.equals("txt_vehstock_confirmed_sgw")) {
									value = value.replaceAll("nbsp", "&");
									value = ConvertShortDateToStr(value);

									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT vehstock_confirmed_sgw"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id=" + vehstock_id + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_confirmed_sgw = '" + value + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										updateQuery(StrSql);
										String history_actiontype = "Confirmed SGW";
										value = strToShortDate(value);
										history_oldvalue = strToShortDate(history_oldvalue);
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Confirmed SGW Updated!";
									}
								}
							}

							if (name.equals("txt_vehstock_chassis_prefix")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT vehstock_chassis_prefix"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id=" + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_chassis_prefix = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Chassis Prefix";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Chassis Prefix Updated!";

							}

							if (name.equals("txt_vehstock_chassis_no")) {
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");
									String engine_no = ExecuteQuery("SELECT vehstock_engine_no"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id = " + vehstock_id + " ");

									if (branch_brand_id.equals("2") || branch_brand_id.equals("10")) {

										if (!value.equals("") && !engine_no.equals("")) {
											StrSql = "SELECT CONCAT(vehstock_chassis_no,'-',vehstock_engine_no) FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE CONCAT(vehstock_chassis_no,'-',vehstock_engine_no) = '" + value + "-" + engine_no + "'"
													+ " AND vehstock_id != " + vehstock_id + "";
											if (!ExecuteQuery(StrSql).equals("")) {
												StrHTML = "<font color=\"red\">Similar Chassis and Engine Number found!</font>";
											}
										}
									} else {
										if (!engine_no.equals("")) {
											StrSql = "SELECT vehstock_engine_no"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_engine_no = '" + engine_no + "'"
													+ " AND vehstock_id != " + vehstock_id + "";
											if (!ExecuteQuery(StrSql).equals("")) {
												StrHTML += "<font color=\"red\"><br>Similar Engine Number found!</font>";
											}
										}

										if (!value.equals("")) {
											StrSql = "SELECT vehstock_chassis_no"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_chassis_no = '" + value + "'"
													+ " AND vehstock_id != " + vehstock_id + "";
											if (!ExecuteQuery(StrSql).equals("")) {
												StrHTML += "<br><font color=\"red\">Similar Chassis Number found!</font>";
											}
										}
									}

									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT vehstock_chassis_no"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id=" + vehstock_id + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_chassis_no = '" + value + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										updateQuery(StrSql);
										String history_actiontype = "Chassis No.";
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Chassis No. Updated!";
									}
								} else if (!branch_brand_id.equals("52") && !branch_brand_id.equals("55") && !branch_brand_id.equals("56")) {
									StrHTML = "<font color=\"red\">Enter Chassis No.!</font>";
								}
							}

							if (name.equals("dr_vehstock_paintwork_id")) {
								value = value.replaceAll("nbsp", "&");

								String oldBranchId = ExecuteQuery("SELECT vehstock_paintwork_id"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id= " + vehstock_id + " ");

								String history_oldvalue = ExecuteQuery("SELECT  option_name"
										+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
										+ " WHERE option_id= " + oldBranchId + " ");

								String option_name = ExecuteQuery("SELECT  option_name"
										+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
										+ " WHERE option_id= " + value + " ");

								StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
										+ " WHERE trans_vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);

								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
										+ " (trans_option_id,"
										+ " trans_vehstock_id)"
										+ " VALUES"
										+ " (" + value + ","
										+ " " + vehstock_id + ")";
								// SOP("StrSql===option trans===" + StrSql);
								updateQuery(StrSql);

								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_paintwork_id = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								// SOP("Sql up" + StrSql);
								updateQuery(StrSql);

								String history_actiontype = "Paintwork";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + option_name + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Paintwork Updated!";
							}

							if (name.equals("txt_vehstock_engine_no")) {
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");

									String chassis_no = ExecuteQuery("SELECT vehstock_chassis_no"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id = " + vehstock_id + " ");
									if (branch_brand_id.equals("2") || branch_brand_id.equals("10")) {
										if (!value.equals("") && !chassis_no.equals("")) {
											StrSql = "SELECT CONCAT(vehstock_chassis_no,'-',vehstock_engine_no) FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE CONCAT(vehstock_chassis_no,'-',vehstock_engine_no) = '" + chassis_no + "-" + value + "'"
													+ " AND vehstock_id != " + vehstock_id + "";
											if (!ExecuteQuery(StrSql).equals("")) {
												StrHTML = "<font color=\"red\">Similar Chassis and Engine Number found!</font>";
											}
										}
									} else {
										if (!value.equals("")) {
											StrSql = "SELECT vehstock_engine_no"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_engine_no = '" + value + "'"
													+ " AND vehstock_id != " + vehstock_id + "";
											if (!ExecuteQuery(StrSql).equals("")) {
												StrHTML += "<font color=\"red\"><br>Similar Engine Number found!</font>";
											}
										}

										if (!chassis_no.equals("")) {
											StrSql = "SELECT vehstock_chassis_no"
													+ " FROM " + compdb(comp_id) + "axela_vehstock"
													+ " WHERE vehstock_chassis_no = '" + chassis_no + "'"
													+ " AND vehstock_id != " + vehstock_id + "";
											if (!ExecuteQuery(StrSql).equals("")) {
												StrHTML += "<font color=\"red\"><br>Similar Chassis Number found!</font>";
											}
										}
									}

									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT vehstock_engine_no"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id=" + vehstock_id + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_engine_no = '" + value + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										updateQuery(StrSql);
										String history_actiontype = "Engine No.";
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Engine No. Updated!";
									}
								} else if (branch_brand_id.equals("2") || branch_brand_id.equals("10")) {
									StrHTML = "<font color=\"red\">Enter Engine No.!</font>";
								}
							}

							if (name.equals("txt_vehstock_fastag")) {
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");

									if (!value.equals("")) {
										StrSql = "SELECT vehstock_fastag"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_fastag = '" + value + "'"
												+ " AND vehstock_id != " + vehstock_id + "";
										SOP("StrSql----" + StrSql);
										if (!ExecuteQuery(StrSql).equals("")) {
											StrHTML += "<font color=\"red\"><br>Similar Fast Tag found!</font>";
										}
									}

									if (StrHTML.equals("")) {
										String history_oldvalue = ExecuteQuery("SELECT vehstock_fastag"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id = " + vehstock_id + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_fastag = '" + value.toUpperCase() + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										updateQuery(StrSql);
										String history_actiontype = "Fast Tag";
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + value + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Fast Tag Updated!";
									}
								}
								// else {
								// StrHTML = "<font color=\"red\">Enter Engine No.!</font>";
								// }
							}

							if (name.equals("txt_vehstock_parking_no")) {
								value = value.replaceAll("nbsp", "&");
								if (value.equals("")) {
									value = "0";
								}
								String history_oldvalue = ExecuteQuery("SELECT vehstock_parking_no"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_parking_no = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Parking No.";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Parking No. Updated!";
							}

							if (name.equals("txt_vehstock_ordered_date")) {
								value = value.replaceAll("nbsp", "&");
								value = ConvertShortDateToStr(value);
								String history_oldvalue = ExecuteQuery("SELECT vehstock_ordered_date"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_ordered_date = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Order Date";
								value = strToShortDate(value);
								history_oldvalue = strToShortDate(history_oldvalue);
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Order Date Updated!";
							}

							if (name.equals("txt_vehstock_invoice_no")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT vehstock_invoice_no"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_invoice_no = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Invoice No.";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Invoice No. Updated!";
							}

							if (name.equals("txt_vehstock_invoice_date")) {
								value = value.replaceAll("nbsp", "&");
								value = ConvertShortDateToStr(value);
								String history_oldvalue = ExecuteQuery("SELECT vehstock_invoice_date"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id=" + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_invoice_date = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Invoice Date";
								value = strToShortDate(value);
								history_oldvalue = strToShortDate(history_oldvalue);
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Invoice Date Updated!";
							}

							if (name.equals("txt_vehstock_invoice_amount")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT vehstock_invoice_amount"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id=" + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_invoice_amount = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Invoice Amount";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Invoice Amount Updated!";
							}

							if (name.equals("txt_vehstock_arrival_date")) {
								value = value.replaceAll("nbsp", "&");
								value = ConvertShortDateToStr(value);
								String invoice_date = ExecuteQuery("SELECT vehstock_invoice_date"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id + " ");
								if (!value.equals("") && !invoice_date.equals("")) {
									if (isValidDateFormatShort(strToShortDate(value)) == true && isValidDateFormatShort(strToShortDate(invoice_date)) == true) {
										if (Long.parseLong(invoice_date) > Long.parseLong(value)) {
											StrHTML = "<font color=\"red\">Arrival Date cannot be less than Invoice date!</font>";
										}
									}
								}
								if (StrHTML.equals("")) {
									String history_oldvalue = ExecuteQuery("SELECT vehstock_arrival_date"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id=" + vehstock_id + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_arrival_date = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Arrival Date";
									value = strToShortDate(value);
									history_oldvalue = strToShortDate(history_oldvalue);
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Arrival Date Updated!";
								}
							}

							if (name.equals("txt_vehstock_pdi_date")) {
								value = value.replaceAll("nbsp", "&");
								value = ConvertShortDateToStr(value);
								String history_oldvalue = ExecuteQuery("SELECT vehstock_pdi_date"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id=" + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_pdi_date = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "PDI Date";
								value = strToShortDate(value);
								history_oldvalue = strToShortDate(history_oldvalue);
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "PDI Date Updated!";
							}

							if (name.equals("txt_vehstock_dms_date")) {
								value = value.replaceAll("nbsp", "&");
								value = ConvertShortDateToStr(value);
								String invoice_date = ExecuteQuery("SELECT vehstock_invoice_date"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id + " ");
								if (Long.parseLong(value) <= Long.parseLong(invoice_date)) {
									StrHTML = "<font color=\"red\">DMS Date Should be greater then Invoice date!</font>";
								}
								if (StrHTML.equals("")) {
									String history_oldvalue = ExecuteQuery("SELECT vehstock_dms_date"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id=" + vehstock_id + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_dms_date = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									updateQuery(StrSql);
									String history_actiontype = "DMS Date";
									value = strToShortDate(value);
									history_oldvalue = strToShortDate(history_oldvalue);
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "DMS Date Updated!";
								}
							}

							if (name.equals("txt_vehstock_nsc")) {
								value = value.replaceAll("nbsp", "&");
								if (value.length() > 10) {
									value = value.substring(0, 10);
								}
								String history_oldvalue = ExecuteQuery("SELECT vehstock_nsc"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id=" + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_nsc = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "NSC Date";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "NSC Updated!";
							}

							if (name.equals("dr_vehstock_delstatus_id")) {
								value = value.replaceAll("nbsp", "&");

								String chk_delstatus_id = CNumeric(ExecuteQuery("SELECT vehstock_delstatus_id FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id));
								if (!chk_delstatus_id.equals("6") || ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {

									if (!value.equals("0")) {
										String oldBranchId = ExecuteQuery("SELECT vehstock_delstatus_id"
												+ " FROM " + compdb(comp_id) + "axela_vehstock"
												+ " WHERE vehstock_id = " + vehstock_id + " ");
										String history_oldvalue = ExecuteQuery("SELECT  delstatus_name"
												+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
												+ " WHERE delstatus_id = " + oldBranchId + " ");
										String delstatus_name = ExecuteQuery("SELECT  delstatus_name"
												+ " FROM " + compdb(comp_id) + "axela_sales_so_delstatus"
												+ " WHERE delstatus_id = " + value + " ");
										StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
												+ " SET"
												+ " vehstock_delstatus_id = '" + value + "'"
												+ " WHERE vehstock_id = " + vehstock_id + "";
										// SOP("Sql up" + StrSql);
										updateQuery(StrSql);
										String history_actiontype = "Delivery	Status";
										StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
												+ " (history_vehstock_id,"
												+ " history_emp_id,"
												+ " history_datetime,"
												+ " history_actiontype,"
												+ " history_oldvalue,"
												+ " history_newvalue)"
												+ " VALUES ("
												+ " '" + vehstock_id + "',"
												+ " '" + emp_id + "',"
												+ " '" + ToLongDate(kknow()) + "',"
												+ " '" + history_actiontype + "',"
												+ " '" + history_oldvalue + "',"
												+ " '" + delstatus_name + "')";
										updateQuery(StrSql);
										// SOP(StrSql);
										StrHTML = "Delivery	Status Updated!";
									}
									else {
										StrHTML = "<font color=\"red\">Select Delivery	Status!</font>";
									}
								} else {
									StrHTML = "<font color=\"red\">Access Denied!</font>";
								}
							}
							if (name.equals("dr_vehstock_status_id")) {
								if (!value.equals("0")) {
									value = value.replaceAll("nbsp", "&");
									String oldBranchId = ExecuteQuery("SELECT vehstock_status_id"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id = " + vehstock_id + " ");
									String history_oldvalue = ExecuteQuery("SELECT  status_name"
											+ " FROM " + compdb(comp_id) + "axela_vehstock_status"
											+ " WHERE status_id = " + oldBranchId + " ");
									String status_name = ExecuteQuery("SELECT  status_name"
											+ " FROM " + compdb(comp_id) + "axela_vehstock_status"
											+ " WHERE status_id = " + value + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_status_id = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									// SOP("Sql up" + StrSql);
									updateQuery(StrSql);
									String history_actiontype = "Status";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + status_name + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Status Updated!";
								}
								else {
									StrHTML = "<font color=\"red\">Select Status!</font>";
								}
							}

							if (name.equals("chk_vehstock_blocked")) {
								if (!value.equals("")) {
									value = value.replaceAll("nbsp", "&");
									String history_oldvalue = ExecuteQuery("SELECT vehstock_blocked"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id =" + vehstock_id + " ");
									if (history_oldvalue.equals("0")) {
										history_oldvalue = "Blocked Inactive";
									}
									else {
										history_oldvalue = "Blocked Active";
									}
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_blocked = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									String historyNewValue = "";
									if (value.equals("0")) {
										historyNewValue = "Blocked Inactive";
									}
									else {
										historyNewValue = "Blocked Active";
									}
									updateQuery(StrSql);
									String history_actiontype = "Blocked";
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + historyNewValue + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Blocked Updated!";
								}
							}

							if (name.equals("txt_vehstock_incentive")) {
								value = value.replaceAll("nbsp", "&");

								String history_oldvalue = ExecuteQuery("SELECT vehstock_incentive"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_incentive = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Incentive";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Incentive Updated!";
							}

							if (name.equals("txt_vehstock_rectification_date")) {
								value = value.replaceAll("nbsp", "&");
								value = ConvertShortDateToStr(value);
								String arrival_date = ExecuteQuery("SELECT vehstock_arrival_date"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id = " + vehstock_id + " ");

								if (!arrival_date.equals("") && !value.equals("")) {
									if (isValidDateFormatShort(strToShortDate(arrival_date)) && isValidDateFormatShort(strToShortDate(value))) {
										if (Long.parseLong(arrival_date) > Long.parseLong(value)) {
											StrHTML = "<font color=\"red\">Rectification Date cannot be less than Arrival Date!</font>";
										}
									}
								}
								if (StrHTML.equals("")) {
									String history_oldvalue = ExecuteQuery("SELECT vehstock_rectification_date"
											+ " FROM " + compdb(comp_id) + "axela_vehstock"
											+ " WHERE vehstock_id=" + vehstock_id + " ");
									StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
											+ " SET"
											+ " vehstock_rectification_date = '" + value + "'"
											+ " WHERE vehstock_id = " + vehstock_id + "";
									updateQuery(StrSql);
									String history_actiontype = "Rectification Date";
									value = strToShortDate(value);
									history_oldvalue = strToShortDate(history_oldvalue);
									StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
											+ " (history_vehstock_id,"
											+ " history_emp_id,"
											+ " history_datetime,"
											+ " history_actiontype,"
											+ " history_oldvalue,"
											+ " history_newvalue)"
											+ " VALUES ("
											+ " '" + vehstock_id + "',"
											+ " '" + emp_id + "',"
											+ " '" + ToLongDate(kknow()) + "',"
											+ " '" + history_actiontype + "',"
											+ " '" + history_oldvalue + "',"
											+ " '" + value + "')";
									updateQuery(StrSql);
									// SOP(StrSql);
									StrHTML = "Rectification Date Updated!";
								}
							}

							if (name.equals("txt_vehstock_intransit_damage")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT vehstock_intransit_damage"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id=" + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_intransit_damage = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Nature of Damage";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Nature of Damage Updated!";
							}

							if (name.equals("txt_vehstock_notes")) {
								value = value.replaceAll("nbsp", "&");
								String history_oldvalue = ExecuteQuery("SELECT vehstock_notes"
										+ " FROM " + compdb(comp_id) + "axela_vehstock"
										+ " WHERE vehstock_id=" + vehstock_id + " ");
								StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
										+ " SET"
										+ " vehstock_notes = '" + value + "'"
										+ " WHERE vehstock_id = " + vehstock_id + "";
								updateQuery(StrSql);
								String history_actiontype = "Notes";
								StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_history"
										+ " (history_vehstock_id,"
										+ " history_emp_id,"
										+ " history_datetime,"
										+ " history_actiontype,"
										+ " history_oldvalue,"
										+ " history_newvalue)"
										+ " VALUES ("
										+ " '" + vehstock_id + "',"
										+ " '" + emp_id + "',"
										+ " '" + ToLongDate(kknow()) + "',"
										+ " '" + history_actiontype + "',"
										+ " '" + history_oldvalue + "',"
										+ " '" + value + "')";
								updateQuery(StrSql);
								// SOP(StrSql);
								StrHTML = "Notes Updated!";
							}
						} else {
							StrHTML = "<font color=\"red\">Access Denied!</font>";
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}