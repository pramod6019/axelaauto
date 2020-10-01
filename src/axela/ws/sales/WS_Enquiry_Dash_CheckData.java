/* Ved Prakash (12th Sept 2013) */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_Dash_CheckData extends ConnectWS {

	public String msg = "";
	public String StrSql = "";
	public String update = "";
	public String enquiry_id = "0";
	public String emp_uuid = "0";
	public String enquiry_emp_id = "";
	public String enquiry_branch_id = "";
	public String name = "";
	public String value = "";
	public String name1 = "";
	public String value1 = "";
	public String status_id = "0";
	public String status_desc = "";
	public String lostcase2 = "";
	public String lostcase3 = "";
	public String lostcase1 = "";
	public String lostcase = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_close_enquiry = "";
	public String ExeAccess = "";
	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();

	public JSONObject dashcheck(JSONObject input) {
		if (AppRun().equals("0")) {
			SOP("input = " + input);
		}
		try {

			if (!input.isNull("emp_id")) {
				emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			}
			// ExeAccess = WSCheckExeAccess(emp_id);
			if (!input.isNull("enquiry_id")) {
				enquiry_id = CNumeric(PadQuotes((String) input.get("enquiry_id")));
			}
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}

			if (!emp_id.equals("0") && !enquiry_id.equals("0")) {
				if (!emp_id.equals("1")) {
					emp_close_enquiry = ExecuteQuery("SELECT emp_close_enquiry\n"
							+ "FROM " + compdb(comp_id) + "axela_emp\n"
							+ "WHERE emp_id = " + emp_id + "");
				} else {
					emp_close_enquiry = "1";
				}

				if (emp_close_enquiry.equals("1")) {
					if (!input.isNull("update")) {
						update = PadQuotes((String) input.get("update"));
					}
					if (!input.isNull("sp_lostcase1_id")) {
						lostcase1 = (String) input.get("sp_lostcase1_id");
					}
					if (!input.isNull("sp_lostcase2_id")) {
						lostcase2 = (String) input.get("sp_lostcase2_id");
					}
					if (!input.isNull("sp_lostcase3_id")) {
						lostcase3 = (String) input.get("sp_lostcase3_id");
					}
					if (!input.isNull("lostcase")) {
						lostcase = (String) input.get("lostcase");
					}
					if (update.equals("")) {
						output.put("msg_access", "");
						if (lostcase.equals("yes")) {
							if (!lostcase1.equals("")) {
								PopulateLostCase2();
							}
							if (!lostcase2.equals("")) {
								PopulateLostCase3();
							}
						} else {
							PopulateStatus();
							PopulateLostCase1();
						}
					} else if (update.equals("yes")) {
						output = UpdateFields(input);
					}
				} else {
					output.put("msg_access", "Access Denied!");
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		if (AppRun().equals("0")) {
			SOP("output = " + output);
		}
		return output;
	}

	public JSONObject PopulateStatus() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT status_id, status_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status"
					+ " WHERE 1 = 1"
					+ " AND status_id > 2"
					+ " ORDER BY status_id";
			// SOP("StrSql==" + StrSql);
			crs = processQuery(StrSql, 0);
			map.put("status_id", "0");
			map.put("status_name", "Select");
			list.add(gson.toJson(map)); // Converting String to Json
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("status_id", crs.getString("status_id"));
					map.put("status_name", crs.getString("status_name"));
					list.add(gson.toJson(map)); // Converting String to Json
				}
				map.clear();
				output.put("populatestatus", list);
				list.clear();
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateLostCase1() {
		try {
			StrSql = "Select lostcase1_id, lostcase1_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_lostcase1"
					+ " where 1=1"
					+ " order by lostcase1_name";
			// SOP("StrSql=lost=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("lostcase1_id", "0");
				map.put("lostcase1_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					map.put("lostcase1_id", crs.getString("lostcase1_id"));
					map.put("lostcase1_name", crs.getString("lostcase1_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("lostcase1_id", "0");
				map.put("lostcase1_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatelostcase1", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateLostCase2() {
		try {
			String case2 = "";
			StrSql = "Select lostcase2_id, lostcase2_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
					+ " where 1=1"
					+ " and lostcase2_lostcase1_id = " + lostcase1
					+ " order by lostcase2_name";
			// SOP("StrSql=lost=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("lostcase2_id", "0");
				map.put("lostcase2_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					case2 = "<br>Select Lost Case 2!";
					map.put("lostcase2_id", crs.getString("lostcase2_id"));
					map.put("lostcase2_name", crs.getString("lostcase2_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("lostcase2_id", "0");
				map.put("lostcase2_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatelostcase2", list);
			list.clear();
			crs.close();
			output.put("msg", case2);
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateLostCase3() {
		try {
			String case3 = "";
			StrSql = "Select lostcase3_id, lostcase3_name"
					+ " from " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
					+ " where 1=1"
					+ " and lostcase3_lostcase2_id = " + lostcase2
					+ " order by lostcase3_name";
			// SOP("StrSql=lost=" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				map.put("lostcase3_id", "0");
				map.put("lostcase3_name", "Select");
				list.add(gson.toJson(map));
				while (crs.next()) {
					case3 = "<br>Select Lost Case 3!";
					map.put("lostcase3_id", crs.getString("lostcase3_id"));
					map.put("lostcase3_name", crs.getString("lostcase3_name"));
					list.add(gson.toJson(map));
				}
			} else {
				map.put("lostcase3_id", "0");
				map.put("lostcase3_name", "Select");
				list.add(gson.toJson(map));
			}
			map.clear();
			output.put("populatelostcase3", list);
			list.clear();
			crs.close();
			output.put("msg", case3);
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}
		return output;
	}

	public JSONObject UpdateFields(JSONObject input) {
		String history_oldvalue = "";
		String history_newvalue = "";
		String history_actiontype = "";
		try {
			if (!enquiry_id.equals("0")) {
				StrSql = "SELECT enquiry_emp_id, enquiry_branch_id"
						+ " FROM " + compdb(comp_id) + "axela_sales_enquiry"
						+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
						+ " INNER JOIN " + compdb(comp_id) + "axela_emp ON emp_id = enquiry_emp_id"
						+ " WHERE 1 = 1 AND enquiry_id = " + enquiry_id
						// + " and emp_id = " + emp_id
						// + WSCheckBranchAccess(emp_id, branch_id, role_id)
						+ " AND IF((select emp_all_exe from "
						+ compdb(comp_id)
						+ "axela_emp "
						+ " where emp_id="
						+ emp_id // + " AND emp_uuid='"+ emp_uuid + "'"
						+ ")=0, (enquiry_emp_id in (SELECT empexe_id from "
						+ compdb(comp_id)
						+ "axela_emp_exe where empexe_emp_id ="
						+ emp_id
						+ ") OR enquiry_emp_id ="
						+ emp_id
						+ "),enquiry_id > 0)"
						+ " GROUP BY enquiry_id";
				// SOP("strsql=========="+StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					enquiry_emp_id = crs.getString("enquiry_emp_id");
					enquiry_branch_id = crs.getString("enquiry_branch_id");
				}
				crs.close();
			} else {
				output.put("msg", "\nUpdate Permission Denied!");
			}

			if (!enquiry_emp_id.equals("0") || emp_id.equals("1")) {

				if (!input.isNull("name")) {
					name = (String) input.get("name");
				}
				if (!input.isNull("value")) {
					value = (String) input.get("value");
				}
				if (!input.isNull("name1")) {
					name1 = (String) input.get("name1");
				}
				if (!input.isNull("value1")) {
					value1 = (String) input.get("value1");
				}

				if (!input.isNull("enquiry_status_id")) {
					status_id = (String) input.get("enquiry_status_id");
				}
				if (!input.isNull("enquiry_status_desc")) {
					status_desc = (String) input.get("enquiry_status_desc");
				}

				// status new status
				// if (name.equals("sp_enquiry_status_id")) {
				status_id = status_id.replaceAll("nbsp", "&");
				String Str = "";
				if (status_desc.equals("")) {
					Str = "Enter Status Description!<br>";
				}
				if (status_id.equals("1") || status_id.equals("2")) {
					lostcase1 = "0";
					lostcase2 = "0";
					lostcase3 = "0";
				} else {
					if (lostcase1.equals("0")) {
						Str = Str + "Select Lost Case 1!<br>";
					}
					StrSql = "Select lostcase2_id from " + compdb(comp_id) + "axela_sales_enquiry_lostcase2"
							+ " where lostcase2_lostcase1_id = " + lostcase1 + "";
					if (!ExecuteQuery(StrSql).equals("")) {
						if (lostcase2.equals("0")) {
							Str = Str + "Select Lost Case 2!<br>";
						}
					}
					// SOP("lostcase2 ====== " + lostcase2);
					StrSql = "Select lostcase3_id from " + compdb(comp_id) + "axela_sales_enquiry_lostcase3"
							+ " where lostcase3_lostcase2_id = " + lostcase2 + "";
					// SOP("Str 123= " + ExecuteQuery(StrSql));
					// SOP("Str lostcase3= " + lostcase3);
					if (!ExecuteQuery(StrSql).equals("")) {
						if (lostcase3.equals("0")) {
							Str = Str + "Select Lost Case 3!";
						}
					}
				}
				// SOP("Str = " + Str);
				if (Str.equals("")) {
					String historyoldvalue_status_name = "";
					String historyoldvalue_lostcase1 = "", historyoldvalue_lostcase2 = "", historyoldvalue_lostcase3 = "";
					String historynewvalue_status_name = "";
					String historynewvalue_lostcase1 = "", historynewvalue_lostcase2 = "", historynewvalue_lostcase3 = "";
					String historyactiontype_status_name = "";
					String historyactiontype_lostcase1 = "", historyactiontype_lostcase2 = "", historyactiontype_lostcase3 = "";
					CachedRowSet crs = null;

					StrSql = "Select enquiry_status_id, status_name,"
							+ " coalesce(lostcase1_name, '') lostcase1_name, coalesce(lostcase2_name,'') lostcase2_name,"
							+ " coalesce(lostcase3_name, '') lostcase3_name "
							+ " from " + compdb(comp_id) + "axela_sales_enquiry"
							+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_status on status_id = enquiry_status_id"
							+ " left join " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 on lostcase1_id  = enquiry_lostcase1_id"
							+ " left join " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 on lostcase2_id  = enquiry_lostcase2_id"
							+ " left join " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 on lostcase3_id  = enquiry_lostcase3_id"
							+ " where enquiry_id = " + enquiry_id + " ";
					// SOP("StrSql----------" + StrSql);
					crs = processQuery(StrSql, 0);
					while (crs.next()) {
						historyoldvalue_status_name = crs.getString("status_name");
						historyoldvalue_lostcase1 = crs.getString("lostcase1_name");
						historyoldvalue_lostcase2 = crs.getString("lostcase2_name");
						historyoldvalue_lostcase3 = crs.getString("lostcase3_name");
					}
					crs.close();

					StrSql = "Update " + compdb(comp_id) + "axela_sales_enquiry"
							+ " set enquiry_status_id = " + status_id + ","
							+ " enquiry_status_desc = '" + status_desc + "',"
							+ " enquiry_status_date = " + ToLongDate(kknow()) + ","
							+ " enquiry_lostcase1_id = " + lostcase1 + ","
							+ " enquiry_lostcase2_id = " + lostcase2 + ","
							+ " enquiry_lostcase3_id = " + lostcase3 + ""
							+ " where enquiry_id = " + enquiry_id;
					// SOP("updating status StrSql==" + StrSql);
					updateQuery(StrSql);

					StrSql = "Select enquiry_status_id, status_name,"
							+ " coalesce(lostcase1_name, '') lostcase1_name, coalesce(lostcase2_name,'') lostcase2_name,"
							+ " coalesce(lostcase3_name, '') lostcase3_name "
							+ " from " + compdb(comp_id) + "axela_sales_enquiry"
							+ " inner join " + compdb(comp_id) + "axela_sales_enquiry_status on status_id = enquiry_status_id"
							+ " left join " + compdb(comp_id) + "axela_sales_enquiry_lostcase1 on lostcase1_id  = enquiry_lostcase1_id"
							+ " left join " + compdb(comp_id) + "axela_sales_enquiry_lostcase2 on lostcase2_id  = enquiry_lostcase2_id"
							+ " left join " + compdb(comp_id) + "axela_sales_enquiry_lostcase3 on lostcase3_id  = enquiry_lostcase3_id"
							+ " where enquiry_id = " + enquiry_id + " ";
					crs = processQuery(StrSql, 0);
					while (crs.next()) {
						historynewvalue_status_name = crs.getString("status_name");
						historynewvalue_lostcase1 = crs.getString("lostcase1_name");
						historynewvalue_lostcase2 = crs.getString("lostcase2_name");
						historynewvalue_lostcase3 = crs.getString("lostcase3_name");
					}
					crs.close();

					historyactiontype_status_name = "STATUS";
					StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_oldvalue,"
							+ " history_newvalue)"
							+ " values"
							+ " ('" + enquiry_id + "',"
							+ " '" + emp_id + "',"
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + historyactiontype_status_name + "',"
							+ " '" + historyoldvalue_status_name + "',"
							+ " '" + historynewvalue_status_name + "')";
					// SOP("StrSql==" + StrSql);
					updateQuery(StrSql);

					if (Integer.parseInt(status_id) > 2) {
						historyactiontype_lostcase1 = "LOST CASE 1";
						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values"
								+ " ('" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + historyactiontype_lostcase1 + "',"
								+ " '" + historyoldvalue_lostcase1 + "',"
								+ " '" + historynewvalue_lostcase1 + "')";
						// SOP("StrSql==" + StrSql);
						updateQuery(StrSql);

						historyactiontype_lostcase2 = "LOST CASE 2";
						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values"
								+ " ('" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + historyactiontype_lostcase2 + "',"
								+ " '" + historyoldvalue_lostcase2 + "',"
								+ " '" + historynewvalue_lostcase2 + "')";
						// SOP("StrSql==" + StrSql);
						updateQuery(StrSql);

						historyactiontype_lostcase3 = "LOST CASE 3";
						StrSql = "INSERT into " + compdb(comp_id) + "axela_sales_enquiry_history"
								+ " (history_enquiry_id,"
								+ " history_emp_id,"
								+ " history_datetime,"
								+ " history_actiontype,"
								+ " history_oldvalue,"
								+ " history_newvalue)"
								+ " values"
								+ " ('" + enquiry_id + "',"
								+ " '" + emp_id + "',"
								+ " '" + ToLongDate(kknow()) + "',"
								+ " '" + historyactiontype_lostcase3 + "',"
								+ " '" + historyoldvalue_lostcase3 + "',"
								+ " '" + historynewvalue_lostcase3 + "')";
						// SOP("StrSql==" + StrSql);
						updateQuery(StrSql);
					}

					history_oldvalue = ExecuteQuery("SELECT enquiry_status_desc FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " WHERE enquiry_id = " + enquiry_id + "");

					history_newvalue = ExecuteQuery("SELECT enquiry_status_desc FROM " + compdb(comp_id) + "axela_sales_enquiry"
							+ " WHERE enquiry_id = " + enquiry_id + "");
					history_actiontype = "Status Comments";
					StrSql = "INSERT INTO " + compdb(comp_id) + "axela_sales_enquiry_history"
							+ " (history_enquiry_id,"
							+ " history_emp_id,"
							+ " history_datetime,"
							+ " history_actiontype,"
							+ " history_newvalue,"
							+ " history_oldvalue)"
							+ " VALUES"
							+ " (" + enquiry_id + ","
							+ " " + emp_id + ","
							+ " '" + ToLongDate(kknow()) + "',"
							+ " '" + history_actiontype + "',";
					// if (!value.equals("")) {
					StrSql += " '" + history_newvalue + "',";
					// }
					StrSql += " '" + history_oldvalue + "')";
					updateQuery(StrSql);

					// start adding crm followup
					if (Integer.parseInt(status_id) > 2) {
						String crmfollowup_crm_emp_id = "";
						if (!enquiry_branch_id.equals("0") && !enquiry_emp_id.equals("0")) {
							StrSql = "select coalesce((select team_crm_emp_id "
									+ " from " + compdb(comp_id) + "axela_sales_team"
									+ " inner join " + compdb(comp_id) + "axela_sales_team_exe on teamtrans_team_id = team_id"
									+ " where team_branch_id = " + enquiry_branch_id + " and teamtrans_emp_id=" + enquiry_emp_id + " limit 1) ,0)";
							// SOP("StrSql-crm-" + StrSqlBreaker(StrSql));
							crmfollowup_crm_emp_id = ExecuteQuery(StrSql);
							// crmfollowup_crm_emp_id = "21";
						}
					}
					// end adding crm followup
					output.put("msg", "Status Updated Successfully!");
				} else {
					output.put("msg", Str.toString());
				}
				// }
				// end new status
			}
		} catch (Exception ex) {
			SOPError("Axelaauto ==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return output;
		}

		return output;
	}
}
