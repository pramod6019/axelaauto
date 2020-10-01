package axela.preowned;
//sangita
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.google.gson.Gson;

public class Preowned_Check extends Connect {

	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String lead_id = "0";
	public String contact_mobile = "";
	public String contact_email = "";
	public String team_id = "0";
	public String comp_id = "0";
	public String preowned_branch_id = "0";
	// public String preowned_preownedmodel_id = "0";
	public String preownedteam = "", salesexecutive = "";
	public String update = "";
	public String executive = "", preexecutive = "";
	public String variant = "";
	public String variantid = "";
	public String lostcase2 = "";
	public String precrmfollowup_lostcase1_id = "0";
	public String lostcase3 = "";
	public String precrmfollowup_lostcase2_id = "0";

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		if (!comp_id.equals("0"))
		{
			lead_id = CNumeric(PadQuotes(request.getParameter("lead_id")));
			contact_mobile = PadQuotes(request.getParameter("contact_mobile"));
			contact_email = PadQuotes(request.getParameter("contact_email"));
			team_id = CNumeric(PadQuotes(request.getParameter("team_id")));
			preowned_branch_id = CNumeric(PadQuotes(request.getParameter("preowned_branch_id")));
			preownedteam = PadQuotes(request.getParameter("preownedteam"));
			executive = PadQuotes(request.getParameter("executive"));
			preexecutive = PadQuotes(request.getParameter("preexecutive"));
			salesexecutive = PadQuotes(request.getParameter("salesexecutive"));
			variant = PadQuotes(request.getParameter("variant"));
			// preowned_preownedmodel_id = CNumeric(PadQuotes(request.getParameter("preowned_model_id")));
			update = PadQuotes(request.getParameter("update"));
			variantid = CNumeric(PadQuotes(request.getParameter("variantid")));
			lostcase2 = PadQuotes(request.getParameter("lostcase2"));
			precrmfollowup_lostcase1_id = CNumeric(PadQuotes(request.getParameter("precrmfollowup_lostcase1_id")));
			lostcase3 = PadQuotes(request.getParameter("lostcase3"));
			precrmfollowup_lostcase2_id = CNumeric(PadQuotes(request.getParameter("precrmfollowup_lostcase2_id")));

			// SOP("lostcase2----------" + lostcase2);
			// SOP("precrmfollowup_lostcase1_id-----------" + precrmfollowup_lostcase1_id);
			// SOP("lostcase3---------" + lostcase3);
			// SOP("precrmfollowup_lostcase2_id--------" + precrmfollowup_lostcase2_id);

			// variant = PadQuotes(request.getParameter("variant"));
			if (!contact_mobile.equals("") && contact_mobile.length() == 13) {
				StrHTML = SearchContactMobile();
			}
			if (!contact_email.equals("")) {
				StrHTML = SearchContactEmail();
			}
			if (executive.equals("yes")) {
				StrHTML = PopulateTeamExecutives();
			}
			if (!variant.equals("")) {
				StrHTML = PopulateVariant();
			}
			if (lostcase2.equals("yes")) {
				// SOP("inside");
				StrHTML = new Preowned_CRMFollowup_Update().PopulateLostCase2(comp_id, "0", precrmfollowup_lostcase1_id);
			}
			if (lostcase3.equals("yes")) {
				StrHTML = new Preowned_CRMFollowup_Update().PopulateLostCase3(comp_id, "0", precrmfollowup_lostcase2_id);
			}
			if (preexecutive.equals("yes")) {
				StrHTML = new Preowned_Quickadd().PopulatePreownedExecutives(preowned_branch_id, team_id, "", comp_id, request);
			}
			if (preownedteam.equals("yes")) {
				StrHTML = new Preowned_Update().PopulatePreownedTeam(preowned_branch_id, team_id, comp_id);
			}
			if (salesexecutive.equals("yes")) {
				StrHTML = new Preowned_Update().PopulateSalesExecutives("0", team_id, comp_id);
			}
		}

	}

	public String PopulateTeamExecutives() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = " SELECT emp_id, CONCAT(emp_name,' (',emp_ref_no,')') as emp_name"
					+ " FROM " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1 "
					+ " and emp_sales = 1 "
					+ " and emp_active='1' "
					+ " and (emp_branch_id = " + preowned_branch_id + " or emp_id = 1 "
					+ " or emp_id in (SELECT empbr.emp_id FROM " + compdb(comp_id) + "axela_emp_branch empbr "
					+ " WHERE " + compdb(comp_id) + "axela_emp.emp_id=empbr.emp_id and empbr.emp_branch_id=" + preowned_branch_id + "))";
			StrSql = StrSql + " and emp_id in (SELECT teamtrans_emp_id FROM " + compdb(comp_id) + "axela_sales_team_exe "
					+ " WHERE teamtrans_team_id=" + team_id + ")";
			StrSql = StrSql + " GROUP BY emp_id "
					+ " ORDER BY emp_name ";
			SOP("PopulateTeamExecutives ----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<SELECT name=\"dr_preowned_emp_id\" id=\"dr_preowned_emp_id\" class=\"SELECTbox\">");
			Str.append("<option value=0>SELECT</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("emp_id")).append("");
				Str.append(">").append(crs.getString("emp_name")).append("</option> \n");
			}
			crs.close();
			Str.append("</SELECT>");
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String SearchContactMobile() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preowned_id, preownedstatus_name, emp_name, contact_id, customer_name"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned on preowned_contact_id = contact_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_preowned_status on preownedstatus_id = preowned_preownedstatus_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_emp on emp_id = preowned_emp_id "
					+ " WHERE (contact_mobile1 = '" + contact_mobile + "' or contact_mobile2 = '" + contact_mobile + "') "
					+ " and preowned_branch_id = " + preowned_branch_id
					+ " GROUP BY preowned_id "
					+ " ORDER BY preowned_id "
					+ " LIMIT 10 ";
			// SOP("StrSql==" + StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Similar Contact</b>");
				while (crs.next()) {
					Str.append("<br><a href=../preowned/preowned-quickadd.jsp?contact_id=").append(crs.getString("contact_id")).append(">" + "<b>").append(crs.getString("customer_name"))
							.append("</b></a>&nbsp;").append("<a href=../preowned/preowned-list.jsp?preowned_id=").append(crs.getString("preowned_id")).append(">" + "<b>")
							.append(crs.getString("emp_name")).append(": Pre Owned ID ").append(crs.getString("preowned_id")).append(" = ").append(crs.getString("preownedstatus_name"))
							.append("</b></a>");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	public String SearchContactEmail() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT concat(title_desc,' ', contact_fname,' ',contact_lname) as contact_name, "
					+ " contact_email1, contact_id "
					+ " FROM " + compdb(comp_id) + "axela_customer_contact "
					+ " INNER JOIN " + compdb(comp_id) + "axela_title on title_id = contact_title_id "
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer on customer_id = contact_customer_id"
					+ " WHERE (contact_email1 = '" + contact_email + "' or contact_email2 = '" + contact_email + "') "
					+ " ORDER BY contact_fname "
					+ " LIMIT 10 ";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				Str.append("<b>Similar Contact</b>");
				while (crs.next()) {
					Str.append("<br><a href=../sales/enquiry-quickadd.jsp?contact_id=").append(crs.getString("contact_id")).append(">" + "<b>").append(crs.getString("contact_name")).append(", ")
							.append(contact_email).append("</b></a>");
					Str.append("<br>");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateVariant() {
		Gson gson = new Gson();
		JSONObject output = new JSONObject();
		ArrayList<String> list = new ArrayList<String>();
		Map<String, String> map = new HashMap<String, String>();
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, CONCAT(carmanuf_name,' - ',preownedmodel_name,' - ',variant_name) as variant_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE 1=1";
			if (!variant.equals("")) {
				StrSql += " AND variant_name LIKE '%" + variant + "%'";
			} else if (!variantid.equals("0")) {
				StrSql += " AND variant_id = " + variantid + "";
			}
			StrSql = StrSql + " GROUP BY variant_id"
					+ " ORDER BY carmanuf_name, preownedmodel_name, variant_name";
			if (!variant.equals("")) {
				StrSql += " LIMIT 10";
			}
			SOP("StrSql PopulateVariant ==" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				SOP("inside");
				while (crs.next()) {
					if (!variant.equals("")) {
						map.put("id", crs.getString("variant_id"));
						map.put("text", crs.getString("variant_name"));
						list.add(gson.toJson(map));
					}
					if (!variantid.equals("0")) {
						output.put("text", crs.getString("variant_name"));
					}
				}
				if (!variant.equals("")) {
					map.clear();
					output.put("variants", list);
					list.clear();
				}
			} else {
				if (!variant.equals("")) {
					output.put("variants", "");
				}

				if (!variantid.equals("0")) {
					output.put("text", "");
				}
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		SOP(JSONPadQuotes(output.toString()));
		return JSONPadQuotes(output.toString());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
