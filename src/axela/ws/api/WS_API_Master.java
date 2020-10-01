/**
 * 
 */
package axela.ws.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

/**
 * @author Sharath
 *
 */
public class WS_API_Master extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public Response response = null;
	public String msg = "";

	Gson gson = new Gson();
	JSONObject output = new JSONObject();
	JSONObject config = new JSONObject();
	ArrayList<String> list = new ArrayList<String>();
	Map<String, ArrayList> map = new HashMap<String, ArrayList>();
	private CachedRowSet crs = null;

	public Response MasterResponse(JSONObject input, @Context HttpServletRequest request) {
		try {
			if (AppRun().equals("0")) {
				SOP("input==WS Enquiry Add===" + input.toString(1));
			}
			comp_id = CNumeric(PadQuotes((String) request.getAttribute("comp_id")));
			if (!input.isNull("emp_name")) {
				String emp_name = PadQuotes((String) input.get("emp_name"));
				emp_id = CNumeric(ExecuteQuery("SELECT emp_id"
						+ " FROM " + compdb(comp_id) + "axela_emp"
						+ " WHERE emp_name='" + emp_name + "'"));
			}
			if (!comp_id.equals("0")) {
				if (!emp_id.equals("0")) {
					PopulateContactTitle();
					PopulateCity();
					PopulateTypeBuyer();
					PopulateCategory();
					PopulateTradeInModel();
					PopulateCampaign();
					PopulatePreownModel();
					PopulateFuleType();
					PopulatePrefReg();
					PopulateSoeSOB();
					PopulateModelItem();
					PopulateBranchTeam();
					PopulateTeamSaleConsultant();
					response = Response.status(200).entity(output).build();
				} else {
					output.put("status_message", "Invalid Executive Name!");
					response = Response.status(400).entity(output).build();
				}
			} else {
				output.put("status_message", "Invalid Company ID!");
				response = Response.status(400).entity(output).build();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return response;
	}

	public void PopulateContactTitle() {
		try {
			String StrSql = "SELECT title_desc"
					+ " FROM " + compdb(comp_id) + "axela_title"
					+ " ORDER BY title_desc";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("title_desc")));
				}
				output.put("ContactTitle", list);
				list.clear();
				map.clear();
			} else {
				output.put("ContactTitle", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateCity() {
		try {
			String StrSql = "SELECT  COALESCE(CONCAT(city_name,' - ',state_name), '') as city_name"
					+ " FROM " + compdb(comp_id) + "axela_city"
					+ " INNER JOIN " + compdb(comp_id)
					+ "axela_state ON state_id = city_state_id"
					// + " WHERE city_id = " + cityid
					+ " GROUP BY city_id"
					+ " ORDER BY city_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("city_name")));
				}
				output.put("City", list);
				list.clear();
				map.clear();
			} else {
				output.put("City", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateModelItem() {
		try {
			ArrayList<String> item = new ArrayList<String>();
			Map model = new HashMap<String, HashMap>();
			String StrSql = "SELECT model_name,"
					+ " GROUP_CONCAT(item_name) AS item_name"
					+ " FROM  " + compdb(comp_id) + "axela_inventory_item_model"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
					+ " WHERE 1 = 1"
					+ " AND model_active = 1"
					+ " AND model_sales = 1"
					+ " AND item_type_id = 1"
					+ " AND item_active = 1"
					+ " GROUP BY model_id"
					+ " ORDER BY model_name, item_name";
			// SOP("PopulateModelItem================" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					for (String str : crs.getString("item_name").split(",")) {
						item.add(unescapehtml(str));
					}
					map.put("Variants", item);
					model.put(crs.getString("model_name"), map);
					list.add(gson.toJson(model));
					item.clear();
					model.clear();
					map.clear();
				}
				output.put("Models", list);
				list.clear();
			} else {
				output.put("Models", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateTypeBuyer() {
		try {
			String StrSql = "SELECT buyertype_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_add_buyertype"
					+ " WHERE 1=1"
					+ " ORDER BY buyertype_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("buyertype_name")));
				}
				output.put("TypeBuyer", list);
				list.clear();
				map.clear();
			} else {
				output.put("TypeBuyer", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateCategory() {
		try {
			String StrSql = "SELECT enquirycat_name"
					+ " FROM axela_sales_enquiry_cat"
					+ " GROUP BY enquirycat_id"
					+ " ORDER BY enquirycat_id";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("enquirycat_name")));
				}
				output.put("Category", list);
				list.clear();
				map.clear();
			} else {
				output.put("Category", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateBranchTeam() {
		try {
			ArrayList<String> team = new ArrayList<String>();
			Map model = new HashMap<String, HashMap>();
			String StrSql = "SELECT branch_name, GROUP_CONCAT(team_name) AS team_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_team"
					+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id = team_branch_id"
					+ " WHERE 1 = 1"
					+ " AND team_active = 1"
					+ " AND branch_active = 1"
					+ " AND branch_branchtype_id IN (1, 2)"
					+ " GROUP BY branch_id"
					+ " ORDER BY branch_id";
			// SOP("PopulateBranchTeam================" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					for (String str : crs.getString("team_name").split(",")) {
						team.add(unescapehtml(str));
					}
					map.put("Teams", team);
					model.put(crs.getString("branch_name"), map);
					list.add(gson.toJson(model));
					team.clear();
					model.clear();
					map.clear();
				}
				output.put("Branches", list);
				list.clear();
			} else {
				output.put("Branches", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateTeamSaleConsultant() {
		try {
			ArrayList<String> sale_consultant = new ArrayList<String>();
			Map team = new HashMap<String, HashMap>();
			String StrSql = "(SELECT team_name,"
					+ " GROUP_CONCAT(emp_name) AS emp_name"
					+ " FROM  " + compdb(comp_id) + "axela_sales_team"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sales_team_exe ON teamtrans_team_id = team_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_emp ON emp_id = teamtrans_emp_id"
					+ " WHERE 1=1"
					+ " AND team_active = 1"
					+ " AND emp_active = 1";
			// weekly off
			StrSql += " AND emp_weeklyoff_id != " + ReturnDayOfWeek(ToLongDate(kknow())) + "";

			// leave
			StrSql += " AND emp_id NOT IN (SELECT leave_emp_id FROM " + compdb(comp_id) + "axela_emp_leave"
					+ " WHERE 1 = 1"
					+ " AND leave_fromdate <= " + ToLongDate(kknow())
					+ " AND leave_todate >= " + ToLongDate(kknow())
					+ " AND leave_active = 1 )"
					+ " GROUP BY team_id"
					+ " ORDER BY team_name )";

			// Only for super admin
			if (emp_id.equals("1")) {
				StrSql += " UNION SELECT emp_id,"
						+ " CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
						+ " FROM " + compdb(comp_id) + "axela_emp "
						+ " WHERE emp_id = 1";
			}
			// + " AND emp_branch_id = " + branch_id;
			// SOP("PopulateTeamSaleConsultant====================" + StrSql);
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					for (String str : crs.getString("emp_name").split(",")) {
						sale_consultant.add(unescapehtml(str));
					}
					map.put("Teams", sale_consultant);
					team.put(crs.getString("team_name"), map);
					list.add(gson.toJson(team));
					sale_consultant.clear();
					team.clear();
					map.clear();
				}
				output.put("Teams", list);
				list.clear();
			} else {
				output.put("Teams", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateTradeInModel() {
		try {
			String StrSql = "SELECT"
					// + " variant_id,"
					+ " CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) as variant_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1 "
					// + " AND variant_id = " + variant_id
					+ " GROUP BY variant_id"
					+ " ORDER BY"
					// + " carmanuf_name,"
					// + " preownedmodel_name,"
					+ " variant_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("variant_name")));
				}
				output.put("TradeInModel", list);
				list.clear();
				map.clear();
			} else {
				output.put("TradeInModel", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateSoeSOB() {
		try {
			ArrayList<String> sob = new ArrayList<String>();
			Map soe = new HashMap<String, HashMap>();
			String StrSql = "SELECT soe_name,"
					+ " GROUP_CONCAT(sob_name)AS sob_name"
					+ " FROM  " + compdb(comp_id) + "axela_soe_trans"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_soe ON soe_id = soetrans_soe_id"
					+ " INNER JOIN  " + compdb(comp_id) + "axela_sob on sob_id = soetrans_sob_id"
					+ " WHERE 1 = 1"
					+ " AND soe_active = 1"
					+ " GROUP BY soetrans_soe_id"
					+ " ORDER BY soe_name";
			// SOP("SOE" + StrSql);
			crs = processQuery(StrSql);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					for (String str : crs.getString("sob_name").split(",")) {
						sob.add(unescapehtml(str));
					}
					map.put("SOB", sob);
					soe.put(crs.getString("soe_name"), map);
					list.add(gson.toJson(soe));
					sob.clear();
					soe.clear();
					map.clear();
				}
				output.put("SOE", list);
				list.clear();
			} else {
				output.put("Branchs", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateCampaign() {
		try {
			String StrSql = "SELECT  campaign_name, campaign_startdate, campaign_enddate "
					+ " FROM " + compdb(comp_id) + "axela_sales_campaign "
					+ " INNER JOIN " + compdb(comp_id) + "axela_sales_campaign_branch ON campaign_id = camptrans_campaign_id "
					+ " WHERE  1 = 1"
					// + " AND camptrans_branch_id = " + enquiry_branch_id
					+ " AND campaign_active = '1' "
					// + " AND SUBSTR(campaign_startdate,1,8) <= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8) "
					// + " AND SUBSTR(campaign_enddate,1,8) >= SUBSTR('" + ConvertShortDateToStr(enquiry_date) + "',1,8) "
					+ " GROUP BY campaign_id "
					+ " ORDER BY campaign_name ";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("campaign_name")));
				}
				output.put("Campaign", list);
				list.clear();
				map.clear();
			} else {
				output.put("Campaign", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulatePreownModel() {
		try {
			String StrSql = "SELECT"
					+ " CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) AS variant_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1 = 1 "
					// + " AND variant_id = " + variant_id
					+ " GROUP BY variant_id"
					+ " ORDER BY"
					+ " variant_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("variant_name")));
				}
				output.put("PreownModel", list);
				list.clear();
				map.clear();
			} else {
				output.put("PreownModel", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulateFuleType() {
		try {
			String StrSql = "SELECT  fueltype_name"
					+ " FROM " + compdb(comp_id) + "axela_fueltype"
					+ " ORDER BY fueltype_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("fueltype_name")));
				}
				output.put("FuleType", list);
				list.clear();
				map.clear();
			} else {
				output.put("FuleType", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void PopulatePrefReg() {
		try {
			String StrSql = "SELECT prefreg_name"
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_prefreg"
					+ " WHERE 1 = 1"
					+ " ORDER BY prefreg_name";
			crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					list.add(unescapehtml(crs.getString("prefreg_name")));
				}
				output.put("PrefReg", list);
				list.clear();
				map.clear();
			} else {
				output.put("PrefReg", "");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto-API==" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
