package axela.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Manage_JobCard_Inventory_List extends Connect {

	public String LinkAddPage = "<a href=manage-jobcard-inventory-update.jsp?add=yes>Add Job Card Inventory...</a>";
	public String StrHTML = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSearch = "";
	public String StrSql = "";
	public String BranchAccess;
	public String msg = "";
	public String invent_id = "0";
	public String invent_model_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CheckSession(request, response);
		HttpSession session = request.getSession(true);
		comp_id = CNumeric(GetSession("comp_id", request));
		CheckPerm(comp_id, "emp_service_jobcard_access", request, response);
		if (!comp_id.equals("0")) {
			emp_id = CNumeric(GetSession("emp_id", request));
			msg = PadQuotes(request.getParameter("msg"));
			invent_model_id = CNumeric(PadQuotes(request.getParameter("dr_model_id")));
			invent_id = CNumeric(PadQuotes(request.getParameter("invent_id")));
			if (invent_model_id.equals("0")) {
				invent_model_id = CNumeric(PadQuotes(request.getParameter("invent_model_id")));
			}
			BranchAccess = GetSession("BranchAccess", request);

			if (!invent_id.equals("0") && invent_model_id.equals("0")) {
				invent_model_id = ExecuteQuery("SELECT model_id FROM " + compdb(comp_id) + "axela_inventory_item_model"
						+ " INNER JOIN " + compdb(comp_id) + "axela_service_jc_invent ON invent_model_id = model_id"
						+ " WHERE invent_id = " + invent_id);
				StrSearch = StrSearch + " AND invent_id = " + invent_id + "";
			} else {
				msg = "";
			}
			try {
				CheckForm();
				StrHTML = JobCardDetail();
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void CheckForm() {
		if (invent_model_id.equals("0")) {
			msg += "<br>Select Model!";
		}
	}

	public String JobCardDetail() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT invent_id, invent_name, model_name"
					+ " FROM " + compdb(comp_id) + "axela_service_jc_invent"
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = invent_model_id"
					+ " WHERE invent_model_id = " + invent_model_id + StrSearch + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			int count = 0;
			if (crs.isBeforeFirst()) {
				Str.append("<div class=\" table-bordered table-hover \">\n");
				Str.append("<table class=\"table table-bordered table-hover \" data-filter=\"#filter\">\n");
				//Str.append("<tr align=center>\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>ID</th>\n");
				Str.append("<th>Model</th>\n");
				Str.append("<th data-hide=\"phone\">Job Card Inventory Details</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");

				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>");
					Str.append("<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=center>").append(crs.getString("invent_id")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("model_name")).append("</td>\n");
					Str.append("<td valign=top>").append(crs.getString("invent_name")).append("</td>\n");
					Str.append("<td valign=top> <a href=\"manage-jobcard-inventory-update.jsp?update=yes&invent_id=");
					Str.append(crs.getString("invent_id")).append(" \">Update Job Card Inventory</a></td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tr></tbody>\n");
				Str.append("</table></div>\n");
			} else {
				Str.append("<br><font color=red><b>No Inventory Found!</b></font>");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public String PopulateModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT model_id, model_name"
					+ " FROM " + compdb(comp_id) + "axela_inventory_item_model"
					+ " ORDER BY model_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id")).append("");
				Str.append(StrSelectdrop(crs.getString("model_id"), invent_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
