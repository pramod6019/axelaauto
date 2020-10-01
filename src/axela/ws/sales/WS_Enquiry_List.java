/* Annappa May 20 2015 */
package axela.ws.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.ConnectWS;

import com.google.gson.Gson;

public class WS_Enquiry_List extends ConnectWS {
	public int i = 0;
	public String StrSql = "";
	public String SqlJoin = "";
	public String CountSql = "";
	public int TotalRecords = 0;
	public String enqstatus = "";
	public String StrSearch = "", Subquery = "";
	public String pagecurrent = "";
	public String emp_uuid = "0";
	public String emp_id = "";
	public String so_id = "0";
	public String comp_id = "0";
	public String branch_id = "";
	public String role_id = "";
	public String keyword_search = "", keyword = "", search = "", enquiry_search = "";
	public String customer_name = "";
	public String contact_name = "";
	public String contact_mobile = "";
	public String contact_phone = "";
	public String contact_email = "";
	public String model_id = "0";
	public String enquiry_id = "0";
	public String item_id = "0";
	public String status_id = "0";
	public String priority_id = "0";
	public String populate = "";
	public String searchkeyname = "";
	public String searchtype = "";
	public String searchvalue = "";
	public String model_ids = "";
	public String status_ids = "";
	public String stage_ids = "";
	public String priority_ids = "";
	public String emp_ids = "";
	public String soe_ids = "";
	public CachedRowSet crs = null;
	Gson gson = new Gson();
	JSONObject obj = new JSONObject();
	JSONObject output = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, String> map = new HashMap<String, String>();
	JSONArray arr_keywords;

	public JSONObject EnquiryList(JSONObject input) throws Exception {
		if (AppRun().equals("0")) {
			SOP("input =enq-list---- " + input);
		}
		// JSONObject output = new JSONObject();
		if (!input.isNull("emp_id")) {
			emp_id = CNumeric(PadQuotes((String) input.get("emp_id")));
			if (!input.isNull("comp_id")) {
				comp_id = CNumeric(PadQuotes((String) input.get("comp_id")));
			}
			if (!input.isNull("emp_uuid")) {
				emp_uuid = CNumeric(PadQuotes((String) input.get("emp_uuid")));
			}
			if (!input.isNull("enquiry_id")) {
				enquiry_id = PadQuotes((String) input.get("enquiry_id"));
			}
			if (!input.isNull("populate")) {
				populate = PadQuotes((String) input.get("populate"));
			}
			if (!input.isNull("searchkeyname")) {
				searchkeyname = ((String) input.get("searchkeyname")).replace("u0027", "'");
			}
			if (!input.isNull("searchtype")) {
				searchtype = PadQuotes((String) input.get("searchtype"));
			}

			// if (!searchparam.equals("") && searchtype.equals("int")) {
			// StrSearch += "AND " + searchkeyname + " = " + searchparam;
			// } else if (!searchparam.equals("") && searchtype.equals("text"))
			// {
			// StrSearch += "AND " + searchkeyname + " LIKE '%" + searchparam +
			// "%'";
			// }

			// adv search
			if (!input.isNull("model_ids")) {
				model_ids = (PadQuotes((String) input.get("model_ids")));
				model_ids = model_ids.replace("[", "").replace("]", "");
				if (!model_ids.equals("")) {
					SOP("model_ids==" + model_ids);
					StrSearch += " AND enquiry_model_id IN (" + model_ids + ")";
				}
			}
			if (!input.isNull("status_ids")) {
				status_ids = (PadQuotes((String) input.get("status_ids")));
				status_ids = status_ids.replace("[", "").replace("]", "");
				if (!status_ids.equals("")) {
					StrSearch += " AND enquiry_status_id IN (" + status_ids
							+ ")";
				}
			}
			if (!input.isNull("stage_ids")) {
				stage_ids = (PadQuotes((String) input.get("stage_ids")));
				stage_ids = stage_ids.replace("[", "").replace("]", "");
				if (!stage_ids.equals("")) {
					StrSearch += " AND enquiry_stage_id IN (" + stage_ids + ")";
				}
			}
			if (!input.isNull("priority_ids")) {
				priority_ids = (PadQuotes((String) input.get("priority_ids")));
				priority_ids = priority_ids.replace("[", "").replace("]", "");
				if (!priority_ids.equals("")) {
					StrSearch += " AND enquiry_priorityenquiry_id IN ("
							+ priority_ids + ")";
				}
			}
			if (!input.isNull("emp_ids")) {
				emp_ids = (PadQuotes((String) input.get("emp_ids")));
				emp_ids = emp_ids.replace("[", "").replace("]", "");
				if (!emp_ids.equals("")) {
					StrSearch += " AND enquiry_emp_id IN (" + emp_ids + ")";
				}
			}
			if (!input.isNull("soe_ids")) {
				soe_ids = (PadQuotes((String) input.get("soe_ids")));
				soe_ids = soe_ids.replace("[", "").replace("]", "");
				if (!soe_ids.equals("")) {
					StrSearch += " AND enquiry_soe_id IN (" + soe_ids + ")";
				}
			}
			if (!input.isNull("arr_keywords")) {
				arr_keywords = input.getJSONArray("arr_keywords");
				for (int i = 0; i < arr_keywords.length(); i++) {
					JSONObject jo = arr_keywords.getJSONObject(i);
					searchkeyname = jo.getString("searchkeyname");
					searchtype = jo.getString("searchtype");
					if (searchtype.equals("date")) {
						searchvalue = ConvertShortDateToStr(jo.getString("searchvalue"));
					} else {
						searchvalue = jo.getString("searchvalue");
					}

					if (!searchvalue.equals("") && (searchtype.equals("int") || searchtype.equals("date") || searchtype.equals("boolean"))) {
						if (searchkeyname.contains("(") && !searchkeyname.contains(")")) {
							StrSearch += " AND " + searchkeyname + " = '" + searchvalue + "')";
						} else {
							StrSearch += " AND " + searchkeyname + " = '" + searchvalue + "'";
						}
					} else if (!searchvalue.equals("") && searchtype.equals("text")) {
						if (searchkeyname.contains("(") && !searchkeyname.contains(")")) {
							StrSearch += " AND " + searchkeyname + " LIKE '%" + searchvalue + "%')";
							SOP("StrSearch=====" + StrSearch);
						} else {
							StrSearch += " AND " + searchkeyname + " LIKE '%" + searchvalue + "%'";
						}
					}
				}
			}
			if (populate.equals("yes") && !emp_id.equals("0")) {
				PopulateModel();
				PopulateStatus();
				PopulateStage();
				PopulatePriority();
				PopulateExecutive();
				PopulateSOE();
				PopulateSmartSearch();
				if (AppRun().equals("0")) {
					SOP("output = " + output);
				}
			}

			if (!emp_id.equals("0")) {
				if (keyword.equals("") && !enquiry_id.equals("0")) {
					keyword_search += " AND enquiry_id =" + enquiry_id;
				}
				if (!input.isNull("pagecurrent")) {
					pagecurrent = PadQuotes((Integer) input.get("pagecurrent") + "");
				}
				if (!input.isNull("enqstatus")) {
					enqstatus = PadQuotes((String) input.get("enqstatus"));
					if (enqstatus.equals("enquiry")) {
						StrSearch = " AND substr(enquiry_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)";
					}
					if (enqstatus.equals("booking")) {
						Subquery = " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id";
						StrSearch = " and substr(so_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)" + " and so_active = 1";
					}
					// if (enqstatus.equals("retail")) {
					// Subquery = " INNER JOIN " + compdb(comp_id) + "axela_invoice ON invoice_enquiry_id = enquiry_id";
					// StrSearch = " and substr(invoice_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)" + " and invoice_active = 1";
					// }
					if (enqstatus.equals("delivered")) {
						Subquery = " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id";
						StrSearch = " and substr(so_delivered_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)" + " and so_active = 1";
					}
					if (enqstatus.equals("cancellation")) {
						Subquery = " INNER JOIN " + compdb(comp_id) + "axela_sales_so ON so_enquiry_id = enquiry_id";
						StrSearch = " and substr(so_cancel_date,1,6) = substr(" + ToLongDate(kknow()) + ",1,6)" + " and so_active = 0";
					}

					if (enqstatus.equals("today")) {
						StrSearch = " and substr(enquiry_entry_date,1,8) = substr(" + ToLongDate(kknow()) + ",1,8)";
					}
					if (enqstatus.equals("hot")) {
						StrSearch = " and enquiry_status_id = 1 AND enquiry_priorityenquiry_id = 1 ";
					}
					if (enqstatus.equals("open")) {
						StrSearch = " and enquiry_status_id = 1 ";
					}
					if (enqstatus.equals("level1")) {
						// StrSearch =
						// " and enquiry_status_id = 1 and followup_trigger = 1 AND followup_desc = '' ";
						StrSearch = " and enquiry_status_id = 1 AND enquiry_id in ( SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
								+ " WHERE followup_trigger = 1"
								+ " AND followup_desc = ''"
								+ " ORDER BY followup_id ) ";
					}
					if (enqstatus.equals("level2")) {
						// StrSearch =
						// " and enquiry_status_id = 1 AND followup_trigger = 2 AND followup_desc = '' ";
						StrSearch = " and enquiry_status_id = 1 AND enquiry_id in ( SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
								+ " WHERE followup_trigger = 2"
								+ " AND followup_desc = ''"
								+ " ORDER BY followup_id ) ";
					}
					if (enqstatus.equals("level3")) {
						// StrSearch =
						// " and enquiry_status_id = 1 AND followup_trigger = 3 AND followup_desc = '' ";
						StrSearch = " and enquiry_status_id = 1 AND enquiry_id in ( SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
								+ " WHERE followup_trigger = 3"
								+ " AND followup_desc = ''"
								+ " ORDER BY followup_id ) ";
					}
					if (enqstatus.equals("level4")) {
						// StrSearch =
						// " and enquiry_status_id = 1 AND followup_trigger = 4 AND followup_desc = '' ";
						StrSearch = " and enquiry_status_id = 1 AND enquiry_id in ( SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
								+ " WHERE followup_trigger = 4"
								+ " AND followup_desc = ''"
								+ " ORDER BY followup_id ) ";
					}
					if (enqstatus.equals("level5")) {
						// StrSearch =
						// " and enquiry_status_id = 1 AND followup_trigger = 5 AND followup_desc = '' ";
						StrSearch = " and enquiry_status_id = 1 AND enquiry_id in ( SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
								+ " WHERE followup_trigger = 5"
								+ " AND followup_desc = ''"
								+ " ORDER BY followup_id ) ";
					}

					if (enqstatus.equals("total")) {
						// StrSearch =
						// " and enquiry_status_id = 1 AND followup_trigger !=0 AND followup_desc = '' ";
						StrSearch = " and enquiry_status_id = 1 AND enquiry_id in ( SELECT followup_enquiry_id"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
								+ " WHERE followup_trigger !=0"
								+ " AND followup_desc = ''"
								+ " ORDER BY followup_id ) ";
					}

				}
				// SOP("---------"+PadQuotes((String) input.get("search")));
				if (!input.isNull("search")) {
					search = PadQuotes((String) input.get("search"));
					// SOP("search = " + search);
					if (search.equals("yes")) {
						if (!input.isNull("customer_name")) {
							customer_name = PadQuotes((String) input.get("customer_name"));
							if (!customer_name.equals("")) {
								enquiry_search = " and customer_name like '%" + customer_name + "%'";
							}
						}
						if (!input.isNull("contact_name")) {
							contact_name = PadQuotes((String) input.get("contact_name"));
							if (!contact_name.equals("")) {
								enquiry_search = enquiry_search + " and concat(title_desc,' ',contact_fname,' ',contact_lname) like '%" + contact_name + "%'";
							}
						}
						if (!input.isNull("contact_mobile")) {
							contact_mobile = PadQuotes((String) input.get("contact_mobile"));
							if (!contact_mobile.equals("")) {
								enquiry_search = enquiry_search
										+ " and concat(REPLACE(contact_mobile1,'-',''), REPLACE(contact_mobile2,'-','')) like '%" + contact_mobile.replace("-", "") + "%'";
							}
						}
						if (!input.isNull("contact_phone")) {
							contact_phone = PadQuotes((String) input.get("contact_phone"));
							if (!contact_phone.equals("")) {
								enquiry_search = enquiry_search
										+ " and concat(REPLACE(contact_phone1,'-',''), REPLACE(contact_phone2,'-','')) like '%"
										+ contact_phone.replace("-", "") + "%'";
							}
						}
						if (!input.isNull("contact_email")) {
							contact_email = PadQuotes((String) input.get("contact_email"));
							if (!contact_email.equals("")) {
								enquiry_search = enquiry_search
										+ " and concat(contact_email1,contact_email2) like '%"
										+ contact_email + "%'";
							}
						}
						if (!input.isNull("model_id")) {
							model_id = CNumeric(PadQuotes((String) input.get("model_id")));
							if (!model_id.equals("0")) {
								enquiry_search = enquiry_search
										+ " and enquiry_model_id = " + model_id;
							}
						}
						if (!input.isNull("item_id")) {
							item_id = CNumeric(PadQuotes((String) input.get("item_id")));
							if (!item_id.equals("0")) {
								enquiry_search = enquiry_search
										+ " and enquiry_item_id = " + item_id;
							}
						}
						if (!input.isNull("status_id")) {
							status_id = CNumeric(PadQuotes((String) input.get("status_id")));
							if (!status_id.equals("0")) {
								enquiry_search = enquiry_search
										+ " and enquiry_status_id = "
										+ status_id;
							}
						}
						if (!input.isNull("priority_id")) {
							priority_id = CNumeric(PadQuotes((String) input.get("priority_id")));
							if (!priority_id.equals("0")) {
								enquiry_search = enquiry_search
										+ " and enquiry_priorityenquiry_id = "
										+ priority_id + "";
							}
						}
						// SOP("enquiry_search = " + enquiry_search);
					}
				}
				if (!populate.equals("yes")) {
					try {
						StrSql = "SELECT customer_name, CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname) AS contact_name,"
								+ " enquiry_id, enquiry_status_id, enquiry_date, contact_mobile1, contact_mobile2, contact_email1, contact_email2,"
								+ " contact_phone1, contact_phone2, "
								+ " CONCAT(exeemp.emp_name, ' (', exeemp.emp_ref_no, ')') AS emp_name,"
								+ " item_name, stage_name, status_name, enquiry_priorityenquiry_id, enquiry_enquirytype_id,"
								+ " if((SELECT count(followup_followuptype_id)"
								+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_followup"
								+ " WHERE followup_enquiry_id = enquiry_id"
								+ " AND followup_followuptype_id=3"
								+ " )>0,'yes','no') as homevisit";

						CountSql = "SELECT COUNT(DISTINCT enquiry_id)";
						SqlJoin = " FROM "
								+ compdb(comp_id) + "axela_sales_enquiry"
								// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = enquiry_branch_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON  contact_id = enquiry_contact_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = enquiry_customer_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_title ON  title_id = contact_title_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_emp exeemp ON exeemp.emp_id = enquiry_emp_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = enquiry_item_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_stage ON stage_id = enquiry_stage_id"
								+ " INNER JOIN " + compdb(comp_id) + "axela_sales_enquiry_status ON status_id = enquiry_status_id"
								+ Subquery
								+ " ," + compdb(comp_id) + "axela_emp emp"
								+ " WHERE 1 = 1 "
								+ " AND emp.emp_id = " + emp_id
								+ " AND IF (emp.emp_exeaccess != '', FIND_IN_SET(enquiry_emp_id, emp.emp_exeaccess), 1=1)"
								+ " AND enquiry_enquirytype_id = 1 "
								+ keyword_search + " " + StrSearch
								+ enquiry_search;
						StrSql = StrSql + SqlJoin;
						StrSql = StrSql + " GROUP BY enquiry_id"
								+ " ORDER BY enquiry_id DESC "
								+ LimitRecords(TotalRecords, pagecurrent);
						CountSql += SqlJoin;
						// SOP("strsql===enqlist==" + StrSqlBreaker(StrSql));
						// SOP("countSql=========="+CountSql);
						TotalRecords = Integer.parseInt(ExecuteQuery(CountSql));
						crs = processQuery(StrSql, 0);
						// SOP("strsql==="+StrSql);

						if (crs.isBeforeFirst()) {
							while (crs.next()) {
								i++;
								map.put("enquiry_id",
										crs.getString("enquiry_id"));
								map.put("enquiry_date", strToShortDate(crs
										.getString("enquiry_date")));
								map.put("enquiry_priorityenquiry_id",
										crs.getString("enquiry_priorityenquiry_id"));
								map.put("item_name",
										unescapehtml(crs.getString("item_name")));
								map.put("stage_name",
										crs.getString("stage_name"));
								map.put("status_name",
										crs.getString("status_name"));
								map.put("customer_name",
										crs.getString("customer_name"));
								map.put("contact_name",
										crs.getString("contact_name"));
								map.put("enquiry_status_id",
										crs.getString("enquiry_status_id"));
								//
								map.put("contact_mobile1",
										crs.getString("contact_mobile1"));
								map.put("contact_mobile2",
										crs.getString("contact_mobile2"));
								map.put("contact_email1",
										crs.getString("contact_email1"));
								map.put("contact_email2",
										crs.getString("contact_email2"));
								map.put("contact_phone1",
										crs.getString("contact_phone1"));
								map.put("contact_phone2",
										crs.getString("contact_phone2"));
								map.put("enquiry_enquirytype_id",
										crs.getString("enquiry_enquirytype_id"));
								map.put("emp_name", crs.getString("emp_name"));
								map.put("homevisit", crs.getString("homevisit"));
								list.add(gson.toJson(map)); // Converting String
															// to
							}
							map = null;
							output.put("listdata", list);
							obj = new JSONObject();
							// obj.put("totalrecords", TotalRecords);
							ArrayList list1 = new ArrayList();
							list1.add(obj);
							obj = null;
							output.put("totalrecords", TotalRecords);
							output.put("pagedata", list1);
							output.put("msg", "");
						} else {
							output.put("msg", "No Records Found!");
						}
						crs.close();
						if (AppRun().equals("0")) {
							SOP("output = " + output);
						}
					} catch (Exception ex) {
						SOPError("Axelaauto =="
								+ this.getClass().getName());
						SOPError("Error in "
								+ new Exception().getStackTrace()[0]
										.getMethodName() + ": " + ex);
						return output;
					}
				}
			}
		}
		return output;
	}

	public JSONObject PopulateModel() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN axela_brand ON brand_id = model_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id = model_brand_id"
					+ " WHERE 1 = 1 "
					+ " AND model_active = '1'"
					+ " AND model_sales = '1'"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name";
			// SOP("PopulateModel SQL------" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("model_id", crs.getString("model_id"));
					map.put("model_name", crs.getString("model_name"));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populatemodel", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateItem() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT item_id, item_name" + " FROM " + compdb(comp_id)
					+ "axela_inventory_item"
					+ " WHERE 1 = 1 AND item_type_id = 1 AND item_active = '1'";
			StrSql += " GROUP BY item_id" + " ORDER BY item_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("item_id", crs.getString("item_id"));
					map.put("item_name",
							unescapehtml(crs.getString("item_name")));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populateitem", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateStatus() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT status_id, status_name" + " FROM "
					+ compdb(comp_id) + "axela_sales_enquiry_status"
					+ " WHERE 1 = 1 " + " GROUP BY status_id"
					+ " ORDER BY status_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("status_id", crs.getString("status_id"));
					map.put("status_name",
							unescapehtml(crs.getString("status_name")));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populatestatus", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateExecutive() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT emp_name, emp_ref_no, emp_id" + " FROM "
					+ compdb(comp_id) + "axela_emp" + " WHERE emp_active = 1"
					// + ExeAccess + ""
					+ " AND IF((select emp_all_exe from "
					+ compdb(comp_id)
					+ "axela_emp "
					+ " where emp_id="
					+ emp_id // + " AND emp_uuid='"+ emp_uuid + "'"
					+ ")=0, (emp_id in (SELECT empexe_id from "
					+ compdb(comp_id)
					+ "axela_emp_exe where empexe_emp_id ="
					+ emp_id
					+ ") OR emp_id ="
					+ emp_id
					+ "),emp_id > 0)"
					+ " GROUP BY emp_id "
					+ " ORDER BY emp_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {

				while (crs.next()) {
					map.put("emp_id", crs.getString("emp_id"));
					map.put("emp_name",
							crs.getString("emp_name") + " ("
									+ crs.getString("emp_ref_no") + ")");
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populateexecutive", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateStage() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT stage_id, stage_name" + " FROM " + compdb(comp_id)
					+ "axela_sales_enquiry_stage" + " ORDER BY stage_rank";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("stage_id", crs.getString("stage_id"));
					map.put("stage_name", crs.getString("stage_name"));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populatestage", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateSOE() {
		CachedRowSet crs = null;
		try {
			StrSql = "SELECT soe_id, soe_name" + " FROM " + compdb(comp_id)
					+ "axela_soe" + " GROUP BY soe_id" + " ORDER BY soe_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("soe_id", crs.getString("soe_id"));
					map.put("soe_name", crs.getString("soe_name"));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populatesoe", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulatePriority() {
		CachedRowSet crs = null;
		try {
			StrSql = "select priorityenquiry_id, priorityenquiry_name "
					+ " from " + compdb(comp_id)
					+ "axela_sales_enquiry_priority " + " where 1 = 1 "
					+ " group by priorityenquiry_id"
					+ " order by priorityenquiry_rank";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					map.put("priorityenquiry_id",
							crs.getString("priorityenquiry_id"));
					map.put("priorityenquiry_name",
							unescapehtml(crs.getString("priorityenquiry_name")));
					list.add(gson.toJson(map));
				}
			}
			map.clear();
			output.put("populatepriority", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}

	public JSONObject PopulateSmartSearch() {
		CachedRowSet crs = null;
		try {

			map.put("enquiry_id_type", "int");
			map.put("enquiry_id", "enquiry_id");

			map.put("enquiry_no_type", "text");
			map.put("enquiry_no", "CONCAT('ENQ', branch_code, enquiry_no)");

			map.put("enquiry_dmsno_type", "text");
			map.put("enquiry_dmsno", "enquiry_dmsno");

			map.put("branch_id_type", "int");
			map.put("branch_id", "branch_id");

			map.put("branch_name_type", "text");
			map.put("branch_name", "branch_name");

			map.put("customer_id_type", "int");
			map.put("customer_id", "customer_id");

			map.put("contact_id_type", "int");
			map.put("contact_id", "contact_id");

			map.put("customer_name_type", "text");
			map.put("customer_name", "customer_name");

			map.put("contactname_type", "text");
			map.put("contactname",
					"CONCAT(title_desc, ' ', contact_fname, ' ', contact_lname)");

			map.put("contactmobile_type", "text");
			map.put("contactmobile",
					"CONCAT(REPLACE(contact_mobile1, '-', ''), REPLACE(contact_mobile2, '-', ''))");

			map.put("contactemail_type", "text");
			map.put("contactemail", "CONCAT(contact_email1, contact_email2)");

			map.put("enquiry_title_type", "enquiry_title");
			map.put("enquiry_title", "enquiry_title");

			map.put("enquiry_desc_type", "text");
			map.put("enquiry_desc", "enquiry_desc");

			map.put("enquiry_date_type", "date");
			map.put("enquiry_date", "enquiry_date");

			map.put("enquiry_close_date_type", "date");
			map.put("enquiry_close_date", "enquiry_close_date");

			map.put("followup_followup_time_type", "date");
			map.put("followup_followup_time",
					"enquiry_id IN (SELECT followup_enquiry_id FROM "
							+ compdb(comp_id)
							+ "axela_sales_enquiry_followup WHERE followup_followup_time");

			map.put("followup_followuptype_id_type", "int");
			map.put("followup_followuptype_id",
					"enquiry_id IN (SELECT followup_enquiry_id FROM "
							+ compdb(comp_id)
							+ "axela_sales_enquiry_followup WHERE followup_followuptype_id");

			map.put("enquiry_value_type", "int");
			map.put("enquiry_value", "enquiry_value");

			map.put("model_name_type", "text");
			map.put("model_name", "enquiry_model_id IN (SELECT model_id FROM "
					+ compdb(comp_id)
					+ "axela_inventory_item_model WHERE model_name");

			map.put("item_name_type", "text");
			map.put("item_name", "item_name");

			map.put("enquiry_avpresent_type", "boolean");
			map.put("enquiry_avpresent", "enquiry_avpresent");

			map.put("enquiry_manager_assist_type", "boolean");
			map.put("enquiry_manager_assist", "enquiry_manager_assist");

			map.put("stage_name_type", "text");
			map.put("stage_name", "stage_name");

			map.put("status_name_type", "text");
			map.put("status_name", "status_name");

			map.put("enquiry_status_date_type", "date");
			map.put("enquiry_status_date", "enquiry_status_date");

			map.put("enquiry_status_desc_type", "text");
			map.put("enquiry_status_desc", "enquiry_status_desc");

			map.put("priorityenquiry_name_type", "text");
			map.put("priorityenquiry_name",
					"enquiry_priorityenquiry_id IN (SELECT priorityenquiry_id FROM "
							+ compdb(comp_id)
							+ "axela_sales_enquiry_priority WHERE priorityenquiry_name");

			map.put("buyertype_name_type", "text");
			map.put("buyertype_name",
					"enquiry_buyertype_id IN (SELECT buyertype_id FROM "
							+ compdb(comp_id)
							+ "axela_sales_enquiry_add_buyertype WHERE buyertype_name");

			map.put("soe_name_type", "text");
			map.put("soe_name", "enquiry_soe_id IN (SELECT soe_id FROM "
					+ compdb(comp_id)
					+ "axela_soe WHERE soe_name");

			map.put("sob_name_type", "text");
			map.put("sob_name", "enquiry_sob_id IN (SELECT sob_id FROM "
					+ compdb(comp_id)
					+ "axela_sob WHERE sob_name");

			map.put("campaign_name_type", "text");
			map.put("campaign_name", "enquiry_campaign_id IN (SELECT campaign_id FROM "
					+ compdb(comp_id) + "axela_sales_campaign WHERE campaign_name");

			map.put("enquiry_qcsno_type", "text");
			map.put("enquiry_qcsno", "enquiry_qcsno");

			map.put("emp_name_type", "text");
			map.put("emp_name", "CONCAT(emp_name, emp_ref_no)");

			map.put("enquiry_notes_type", "text");
			map.put("enquiry_notes", "enquiry_notes");

			map.put("enquiry_entry_id_type", "text");
			map.put("enquiry_entry_id",
					"enquiry_entry_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name");

			map.put("enquiry_entry_date_type", "date");
			map.put("enquiry_entry_date", "enquiry_entry_date");

			map.put("enquiry_modified_id_type", "text");
			map.put("enquiry_modified_id",
					"enquiry_modified_id IN (SELECT emp_id FROM "
							+ compdb(comp_id) + "axela_emp WHERE emp_name");

			map.put("enquiry_modified_date_type", "date");
			map.put("enquiry_modified_date", "enquiry_modified_date");
			list.add(gson.toJson(map));
			map.clear();
			output.put("populatesmartsearch", list);
			list.clear();
			crs.close();
		} catch (Exception ex) {
			SOP(this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
			return output;
		}
		return output;
	}
}
