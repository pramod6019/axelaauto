package axela.insurance;

////////////Divya 17th April 2013
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle_Dash_Insurance extends Connect {

	public String veh_id = "0";
	public String veh_reg_no = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String BranchAccess = "";
	public String StrHTML = "";
	public String StrSql = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				StrSql = "SELECT veh_reg_no FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_id = " + veh_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					while (crs.next()) {
						veh_reg_no = crs.getString("veh_reg_no");
					}
					StrHTML = ListInsurance(comp_id, veh_id);
				} else {
					response.sendRedirect("../portal/error.jsp?msg=Invalid Vehicle ID!");
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public String ListInsurance(String comp_id, String id) {
		StringBuilder Str = new StringBuilder();
		StrSql = "SELECT insurpolicy_id, insurpolicy_branch_id, veh_id, veh_reg_no, insurpolicy_contact_id,"
				+ " branch_id, concat(branch_name, ' (', branch_code, ')') AS branch_name,"
				+ " insurpolicy_date, policytype_name, insurpolicy_policy_no, inscomp_name,"
				+ " insurtype_name, insurpolicy_customer_id, insurpolicy_start_date, insurpolicy_end_date,"
				+ " customer_name, contact_fname, contact_id, contact_mobile1, contact_mobile2,"
				+ " model_name, insurpolicy_active, item_name, insurpolicy_premium_amt, insurpolicy_idv_amt,"
				+ " insurpolicy_od_amt, insurpolicy_od_discount, insurpolicy_payout"
				+ " FROM  " + compdb(comp_id) + "axela_insurance_policy"
				+ " INNER JOIN " + compdb(comp_id) + "axela_branch ON branch_id=insurpolicy_branch_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id=insurpolicy_veh_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_item_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_type ON insurtype_id = insurpolicy_insurtype_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = insurpolicy_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_comp ON inscomp_id = insurpolicy_inscomp_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_insurance_policy_type ON policytype_id = insurpolicy_policytype_id"
				+ " WHERE insurpolicy_veh_id = " + id + BranchAccess
				+ " GROUP BY insurpolicy_id"
				+ " ORDER BY insurpolicy_id desc";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				int count = 0;
				Str.append("<table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				Str.append("<tr align=\"center\">\n");
				Str.append("<th align=\"center\" width=3%>#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Insurance</th>\n");
				Str.append("<th>Vehicle</th>\n");
				Str.append("<th>Customer</th>\n");
				Str.append("<th>Type</th>\n");
				Str.append("<th>Term</th>\n");
				Str.append("<th>Amount</th>\n");
				Str.append("<th>Branch</th>\n");
				Str.append("<th>Actions</th>\n");
				Str.append("</tr>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td align=center valign=top>").append(count).append("</td>\n");
					Str.append("<td valign=top align=center nowrap>").append(crs.getString("insurpolicy_id")).append("</td>\n");
					Str.append("<td valign=top align=left nowrap><b>Company:</b> ").append(crs.getString("inscomp_name"));
					Str.append("<br><b>Policy:</b> ").append(crs.getString("policytype_name"));
					Str.append("<br><b>Policy No.:</b> ").append(crs.getString("insurpolicy_policy_no"));
					if (crs.getString("insurpolicy_active").equals("0")) {
						Str.append("<br><font color=red>[Inactive]</font>");
					}
					Str.append("</td>\n<td valign=top align=left nowrap>");
					Str.append("<a href=\"../service/vehicle-list.jsp?veh_id=");
					Str.append(crs.getString("veh_id")).append("\">").append(SplitRegNo(crs.getString("veh_reg_no"), 2)).append("</a>");
					Str.append("<br>").append(crs.getString("item_name"));
					if (crs.getString("insurpolicy_active").equals("0")) {
						Str.append("<br><font color=red>[Inactive]</font>");
					}
					Str.append("</td>\n<td valign=top align=left nowrap>");
					Str.append("<a href=\"../customer/customer-list.jsp?customer_id=").append(crs.getString("insurpolicy_customer_id")).append(" \">");
					Str.append(crs.getString("customer_name")).append("</a>");
					Str.append("<br><a href=\"../customer/customer-contact-list.jsp?contact_id=").append(crs.getString("contact_id")).append("\">");
					Str.append(crs.getString("contact_fname")).append("</a>");
					if (!crs.getString("contact_mobile1").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile1"), 5, "M")).append("</a>");
					}
					if (!crs.getString("contact_mobile2").equals("")) {
						Str.append("<br>").append(SplitPhoneNo(crs.getString("contact_mobile2"), 5, "M")).append("</a>");
					}
					Str.append("</td>\n<td align=left valign=top>").append(crs.getString("insurtype_name")).append("</td>\n");
					Str.append("<td valign=top align=center nowrap>");
					if (!crs.getString("insurpolicy_start_date").equals("")) {
						Str.append(strToShortDate(crs.getString("insurpolicy_start_date"))).append("-").append(strToShortDate(crs.getString("insurpolicy_end_date"))).append(" ");
					}

					String startdate = crs.getString("insurpolicy_start_date").substring(0, 8);
					String enddate = crs.getString("insurpolicy_end_date").substring(0, 8);

					if (Long.parseLong(enddate) < Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
						Str.append("<br><font color=red>[Expired]</font>");
					} else if (Long.parseLong(startdate) > Long.parseLong(ToLongDate(kknow()).substring(0, 8))) {
						Str.append("<br><font color=blue>[Future Insurance]</font>");
					}
					Str.append("</td>\n<td valign=\"top\" align=\"right\" nowrap>");
					if (!crs.getString("insurpolicy_premium_amt").equals("0")) {
						Str.append("Premium Amount: ").append(crs.getString("insurpolicy_premium_amt")).append("<br>");
					}

					if (!crs.getString("insurpolicy_idv_amt").equals("0")) {
						Str.append("IDV Amount: ").append(crs.getString("insurpolicy_idv_amt")).append("<br>");
					}

					if (!crs.getString("insurpolicy_od_amt").equals("0")) {
						Str.append("OD Amount: ").append(crs.getString("insurpolicy_od_amt")).append("<br>");
					}

					if (!crs.getString("insurpolicy_od_discount").equals("0")) {
						Str.append("OD Discount: ").append(crs.getString("insurpolicy_od_discount")).append("<br>");
					}

					if (!crs.getString("insurpolicy_payout").equals("0")) {
						Str.append("Payout Amount: ").append(crs.getString("insurpolicy_payout"));
					}
					Str.append("</td>\n<td valign=top align=left nowrap>");
					Str.append("<a href=../portal/branch-summary.jsp?branch_id=").append(crs.getInt("branch_id")).append(">");
					Str.append(crs.getString("branch_name")).append("</a></td>\n");
					Str.append("<td valign=top align=left nowrap>");
					Str.append("<a href=\"insurance-update.jsp?update=yes&insurpolicy_id=").append(crs.getString("insurpolicy_id")).append("\"> Update Insurance</a>");
					Str.append("<br><a href=\"insurance-docs-list.jsp?insurpolicy_id=").append(crs.getString("insurpolicy_id")).append("\">List Documents</a>");
					Str.append("<br><a href=\"insurance-service.jsp?insurpolicy_id=").append(crs.getString("insurpolicy_id")).append("\">Manage services</a>");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</table>\n");
			} else {
				Str.append("<br><br><br><br><font color=red><b>No Insurance found!</b></font><br><br><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
}
