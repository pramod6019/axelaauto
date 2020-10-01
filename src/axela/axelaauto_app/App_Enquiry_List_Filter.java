package axela.axelaauto_app;
//Divya & Sangita

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class App_Enquiry_List_Filter extends Connect {

	public String StrSql = "";
	public String branch_id = "0";
	public String enquiry_id = "0";
	public String StrHTML = "";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String comp_id = "0";
	public String emp_uuid = "0";
	public String access = "";
	public String emp_all_exe = "";
	public int enquirystatusid = 0;
	public Connection conntx = null;
	public Statement stmttx = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			if (!emp_id.equals("0")) {
				branch_id = CNumeric((session.getAttribute("emp_branch_id")) + "");
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);

				access = ReturnPerm(comp_id, "emp_enquiry_access", request);
				if (access.equals("0"))
				{
					response.sendRedirect("callurl" + "app-error.jsp?msg=Access denied. Please contact system administrator!");
				}
				emp_all_exe = GetSession("emp_all_exe", request);
				// if (emp_all_exe.equals("1"))
				// {
				// ExeAccess = "";
				// }

			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	// public String PopulateExecutive(String comp_id) {
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') AS emp_name"
	// + " FROM " + compdb(comp_id) + "axela_emp "
	// + " WHERE 1=1 "
	// + " AND emp_sales='1'"
	// + " AND emp_active='1'"
	// + BranchAccess
	// + ExeAccess
	// + " GROUP BY emp_id "
	// + " ORDER BY emp_name ";
	// CachedRowSet crs =processQuery(StrSql, 0);
	// Str.append("<div class=\"row\">")
	// .append("<table class=\"table\" id=\"two\">\n")
	// .append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
	// .append("<td class=\"pull-left\">\n").append("Executives").append("</td>\n")
	// .append("<td class=\"pull-right\" style=\"position:relative; bottom:5px\">\n").append("Select All").append("&nbsp;")
	// .append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallexec_id\" style=\"position:relative; top:3px\">\n")
	// .append("</td>\n")
	// .append("</tr>\n");
	// while (crs.next()) {
	// Str.append("<tr>")
	// .append("<td class=\"pull-left\">")
	// .append(crs.getString("emp_name"))
	// .append("</td>\n")
	// .append("<td class=\"pull-right\">")
	// .append("<input type=\"checkbox\" class=\"caseexec_id icheck\" name=\"chk_executive_id\" value=\"" + crs.getString("emp_id") + " \">").append("</td>\n")
	// .append("</tr>\n");
	// }
	// Str.append("</table>\n")
	// .append("</div>\n");
	//
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// }
	// return Str.toString();
	// }

	public String PopulateStage(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT stage_id, stage_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_stage "
					+ " ORDER BY stage_rank";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"two\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Stage").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallstage_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("stage_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"casestage_id icheck\" name=\"chk_stage_id\" value=\"" + crs.getString("stage_id") + " \">")
						.append("</td>\n")
						.append("</tr>\n");
			}
			Str.append("</table>\n")
					.append("</div>\n");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateStatus(String comp_id) {
		StringBuilder Str = new StringBuilder();

		try {
			StrSql = "SELECT status_id, status_name "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_status " + " WHERE 1 = 1 "
					+ " ORDER BY status_id";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP("StrSql in PopulateStatus==========" + StrSql);

			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"two\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Status").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallstatus_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("status_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"casestatus_id icheck\" name=\"chk_status_id\" value=\"" + crs.getString("status_id")
								+ " \">").append("</td>\n")
						.append("</tr>\n");
			}
			Str.append("</table>\n")
					.append("</div>\n");

			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateOpprPriority(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT priorityenquiry_id, priorityenquiry_name, priorityenquiry_duehrs "
					+ " FROM " + compdb(comp_id) + "axela_sales_enquiry_priority "
					+ " WHERE 1 = 1 "
					+ " GROUP BY priorityenquiry_id"
					+ " ORDER BY priorityenquiry_rank";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"two\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("Priority").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallpriority_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("priorityenquiry_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"casepriority_id icheck\" name=\"chk_priority_id\" value=\""
								+ crs.getString("priorityenquiry_id") + " \">").append("</td>\n")
						.append("</tr>\n");
			}
			Str.append("</table>\n")
					.append("</div>\n");

			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App==="
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);

		}
		return Str.toString();
	}

	// public String PopulateModel(String comp_id) {
	//
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT model_id, model_name"
	// + " FROM " + compdb(comp_id) + "axela_inventory_item_model"
	// + " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_model_id = model_id"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=model_brand_id"
	// + " AND item_type_id = 1"
	// + " WHERE model_active = '1'"
	// + " AND model_sales = '1'"
	// + " GROUP BY model_id"
	// + " ORDER BY model_brand_id, model_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// Str.append("<div class=\"row\">")
	// .append("<table class=\"table\" id=\"one\">\n")
	// .append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
	// .append("<td class=\"pull-left\">\n").append("Model").append("</td>\n")
	// .append("<td class=\"pull-right\" style=\"position:relative; bottom:5px\">\n").append("Select All").append("&nbsp;")
	// .append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallmodel_id\" style=\"position:relative; top:3px\">\n")
	// .append("</td>\n")
	// .append("</tr>\n");
	// while (crs.next()) {
	// Str.append("<tr>")
	// .append("<td class=\"pull-left\">")
	// .append(crs.getString("model_name"))
	// .append("</td>\n")
	// .append("<td class=\"pull-right\">")
	// .append("<input type=\"checkbox\" class=\"casemodel_id icheck\" name=\"chk_model_id\" value=\"" + crs.getString("model_id") + " \">").append("</td>\n")
	// .append("</tr>\n");
	// }
	// Str.append("</table>\n")
	// .append("</div>\n");
	//
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	public String PopulateSOE(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT soe_id, soe_name"
					+ " FROM " + compdb(comp_id) + "axela_soe"
					+ " GROUP BY soe_id"
					+ " ORDER BY soe_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<div class=\"row\">")
					.append("<table class=\"table\" id=\"two\">\n")
					.append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
					.append("<td class=\"pull-left\">\n").append("SOE").append("</td>\n")
					.append("<td class=\"pull-right\" style=\"position:relative; right:20px;\">\n").append("Select All").append("&nbsp;")
					.append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallsoe_id\" style=\"position:absolute; bottom:8px; width:20px;\">\n")
					.append("</td>\n")
					.append("</tr>\n");
			while (crs.next()) {
				Str.append("<tr>")
						.append("<td class=\"pull-left\">")
						.append(crs.getString("soe_name"))
						.append("</td>\n")
						.append("<td class=\"pull-right\">")
						.append("<input style=\"position:absolute; right:10px;\" type=\"checkbox\" class=\"casesoe_id icheck\" name=\"chk_soe_id\" value=\"" + crs.getString("soe_id") + " \">")
						.append("</td>\n")
						.append("</tr>\n");

			}
			Str.append("</table>\n")
					.append("</div>\n");

			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto-App===" + this.getClass().getName());
			SOPError("Axelaauto-App==="
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
		return Str.toString();
	}

	// public String PopulateRegion(String comp_id) {
	//
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT region_id, region_name"
	// + " FROM " + compdb(comp_id) + "axela_branch_region"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_region_id=region_id"
	// + BranchAccess
	// + " GROUP BY region_id"
	// + " ORDER BY region_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// Str.append("<div class=\"row\" id=\"regionHint\">")
	// .append("<table class=\"table\" id=\"one\">\n")
	// .append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
	// .append("<td class=\"pull-left\">\n").append("Region").append("</td>\n")
	// .append("<td class=\"pull-right\" style=\"position:relative; bottom:5px\">\n").append("Select All").append("&nbsp;")
	// .append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallregion_id\" style=\"position:relative; top:3px\">\n")
	// .append("</td>\n")
	// .append("</tr>\n");
	// while (crs.next()) {
	// Str.append("<tr>")
	// .append("<td class=\"pull-left\">")
	// .append(crs.getString("region_name"))
	// .append("</td>\n")
	// .append("<td class=\"pull-right\">")
	// .append("<input type=\"checkbox\" class=\"caseregion_id icheck\" name=\"chk_region_id\" value=\"").append(crs.getString("region_id")).append("\"></td>\n")
	// // .append("<input type=\"checkbox\" class=\"caseregion_id icheck\" name=\"chk_region_id\" value=\"" + crs.getString("region_id") + " \">").append("</td>\n")
	// .append("</tr>\n");
	//
	// }
	//
	// Str.append("</table>\n")
	// .append("</div>\n");
	//
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	// public String PopulateBrand(String comp_id) {
	//
	// StringBuilder Str = new StringBuilder();
	// try {
	// StrSql = "SELECT brand_id, brand_name"
	// + " FROM axela_brand"
	// + " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_brand_id=brand_id"
	// + BranchAccess
	// + " GROUP BY brand_id"
	// + " ORDER BY brand_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// Str.append("<div class=\"row\">")
	// .append("<table class=\"table\" id=\"one\">\n")
	// .append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
	// .append("<td class=\"pull-left\">\n").append("Brand").append("</td>\n")
	// .append("<td class=\"pull-right\" style=\"position:relative; bottom:5px\">\n").append("Select All").append("&nbsp;")
	// .append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallbrand_id\" style=\"position:relative; top:3px\">\n")
	// .append("</td>\n")
	// .append("</tr>\n");
	// while (crs.next()) {
	// Str.append("<tr>")
	// .append("<td class=\"pull-left\">")
	// .append(crs.getString("brand_name"))
	// .append("</td>\n")
	// .append("<td class=\"pull-right\">")
	// .append("<input type=\"checkbox\" class=\"casebrand_id icheck\" onChange=\"PopulateRegion()\" name=\"chk_brand_id\" id=\"chk_brand_id\" value=\"")
	// .append(crs.getString("brand_id"))
	// .append("\"></td>\n")
	// // .append("<input type=\"checkbox\" class=\"casebrand_id icheck\" name=\"chk_brand_id\" value=\"" + crs.getString("brand_id") + " \">").append("</td>\n")
	// .append("</tr>\n");
	//
	// }
	//
	// Str.append("</table>\n")
	// .append("</div>\n");
	//
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	// }

	// public String Populatebranch1(String comp_id) {
	//
	// StringBuilder Str = new StringBuilder();
	// try {
	// String StrSql = "SELECT branch_id, branch_name, branch_code"
	// + " FROM " + compdb(comp_id) + "axela_branch"
	// + " WHERE branch_active='1' "
	// + BranchAccess
	// + " ORDER BY branch_name";
	// CachedRowSet crs =processQuery(StrSql, 0);
	//
	// Str.append("<div class=\"row\">")
	// .append("<table class=\"table\" id=\"one\">\n")
	// .append("<tr style=\"background-color: #2f353b; color: #fff;\">\n")
	// .append("<td class=\"pull-left\">\n").append("Branch").append("</td>\n")
	// .append("<td class=\"pull-right\" style=\"position:relative; bottom:5px\">\n").append("Select All").append("&nbsp;")
	// .append("<input type=\"checkbox\" class=\"icheck\" id=\"selectallbranch_id\" style=\"position:relative; top:3px\">\n")
	// .append("</td>\n")
	// .append("</tr>\n");
	// while (crs.next()) {
	// Str.append("<tr>")
	// .append("<td class=\"pull-left\">")
	// .append(crs.getString("branch_name"))
	// .append("</td>\n")
	// .append("<td class=\"pull-right\">")
	// .append("<input type=\"checkbox\" class=\"casebranch_id icheck\" name=\"chk_branch_id\" value=\"").append(crs.getString("branch_id")).append("\"></td>\n")
	// // .append("<input type=\"checkbox\" class=\"casebranch_id icheck\" name=\"chk_branch_id\" value=\"" + crs.getString("branch_id") + " \">").append("</td>\n")
	// .append("</tr>\n");
	//
	// }
	//
	// Str.append("</table>\n")
	// .append("</div>\n");
	//
	// crs.close();
	// return Str.toString();
	// } catch (Exception ex) {
	// SOPError("Axelaauto-App===" + this.getClass().getName());
	// SOPError("Axelaauto-App===" + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
	// return "";
	// }
	//
	// }

	public String PopulateBranch(String branch_id, String param, HttpServletRequest request) {
		String BranchAccess = "";
		StringBuilder stringval = new StringBuilder();
		HttpSession session = request.getSession(true);
		BranchAccess = GetSession("BranchAccess", request);
		String comp_id = CNumeric(GetSession("comp_id", request));
		try {
			String SqlStr = "SELECT branch_id, branch_name, branch_code"
					+ " FROM " + compdb(comp_id) + "axela_branch"
					+ " WHERE branch_active= 1 "
					+ BranchAccess + ""
					+ " ORDER BY branch_brand_id, branch_branchtype_id, branch_name";
			// SOP("SqlStr===" + SqlStr);
			CachedRowSet crs = processQuery(SqlStr, 0);
			if (param.equals("")) {
				stringval.append("<option value =0>Select</option>");
			}
			while (crs.next()) {
				stringval.append("<option value=").append(crs.getString("branch_id")).append("");
				stringval.append(StrSelectdrop(crs.getString("branch_id"), branch_id));
				stringval.append(">").append(crs.getString("branch_name")).append(" (").append(crs.getString("branch_code")).append(")</option>\n");
			}
			crs.close();
			return stringval.toString();
		} catch (Exception ex) {
			SOPError("AxelaAuto-App=== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + " : " + ex);
			return "";
		}
	}
}
