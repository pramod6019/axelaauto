package axela.service;
//Bhagwan Singh 22th Feb, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Vehicle_Options extends Connect {
	// ///// List page links

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String all = "", add = "", delete = "";
	public String variant_name = "";
	public String itemmaster_name = "";
	public String vehtrans_veh_id = "0";
	public String vehtrans_option_id = "0";
	public String emp_role_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_item_access, emp_sales_item_access, emp_pos_item_access", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				add = PadQuotes(request.getParameter("add"));
				delete = PadQuotes(request.getParameter("delete"));
				vehtrans_veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				vehtrans_option_id = CNumeric(PadQuotes(request.getParameter("option_id")));
				PopulateConfigDetails();

				if (!vehtrans_veh_id.equals("0")) {
					StrSql = "SELECT IF(variant_service_code != '', CONCAT(variant_name, ' (', variant_service_code, ')'), variant_name) AS variant_name"
							+ " FROM axela_preowned_variant"
							+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_variant_id = variant_id"
							+ " WHERE veh_id = " + vehtrans_veh_id + "";
					itemmaster_name = ExecuteQuery(StrSql);
				}

				if (itemmaster_name.equals("")) {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle!"));
				}

				if (add.equals("yes") && !vehtrans_veh_id.equals("0") && !vehtrans_option_id.equals("0")) {
					AddFields();
				}

				if (delete.equals("yes") && !vehtrans_veh_id.equals("0") && !vehtrans_option_id.equals("0")) {
					DeleteOptionItem();
					msg = "<br>Option deleted successfully!";
				}

				if (itemmaster_name.equals("")) {
					StrHTML = "<font color = red><b>Invalid Vehicle</b></font>!";
				} else {
					StrHTML = Listdata();
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void CheckForm() {
		StrSql = "SELECT vehtrans_veh_id"
				+ " FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
				+ " WHERE vehtrans_veh_id = " + vehtrans_veh_id + ""
				+ " AND vehtrans_option_id = " + vehtrans_option_id + "";
		if (ExecuteQuery(StrSql).equals(vehtrans_veh_id)) {
			msg = msg + "<br>Option already added!";
		}
		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
	}

	public void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_option_trans"
					+ " (vehtrans_option_id,"
					+ " vehtrans_veh_id)"
					+ " VALUES"
					+ " (" + vehtrans_option_id + ","
					+ " " + vehtrans_veh_id + ")";
			updateQuery(StrSql);
			msg = "<br>Option added successfully!";
		}
	}

	public void DeleteOptionItem() {

		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_veh_option_trans"
				+ " WHERE vehtrans_veh_id = " + vehtrans_veh_id + ""
				+ " AND vehtrans_option_id = " + vehtrans_option_id + "";

		updateQuery(StrSql);
	}

	public String Listdata() {
		int count = 0;
		StringBuilder Str = new StringBuilder();

		StrSql = "SELECT option_id, option_code, option_name, model_name, optiontype_name"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh_option_trans ON vehtrans_option_id = option_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehtrans_veh_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_brand_id = option_brand_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
				+ " WHERE veh_id = " + vehtrans_veh_id + ""
				+ " GROUP BY option_id"
				+ " ORDER BY option_name";
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				// Str.append("\n <table border=1 cellspacing=0 cellpadding=0 class=\"listtable\">");
				// Str.append("<tr align=center>\n");
				Str.append("<div class=\"table-responsive\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr>\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Option Name</th>\n");
				Str.append("<th>Option Type</th>\n");
				Str.append("<th data-hide=\"phone\">Model</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr>\n");
				Str.append("</thead>\n");
				Str.append("<tbody>\n");
				while (crs.next()) {
					count = count + 1;
					Str.append("<tr>\n<td valign=top align=center>").append(count).append("</td>\n");
					Str.append("<td valign=top align=left>");
					Str.append(crs.getString("option_name"));
					if (!crs.getString("option_code").equals("")) {
						Str.append(" <br>").append("Code: ").append(crs.getString("option_code"));
					}
					Str.append("</td>\n<td valign=top align=left>").append(crs.getString("optiontype_name"));
					Str.append("</td>\n<td valign=top align=left>").append(crs.getString("model_name"));
					Str.append("</td>\n<td valign=top align=left nowrap><a href=\"vehicle-options.jsp?delete=yes&option_id=");
					Str.append(crs.getString("option_id")).append("&veh_id=");
					Str.append(vehtrans_veh_id).append(" \">Delete Option</a>");
					Str.append("</td>\n</tr>\n");
				}
				Str.append("</tbody>");
				Str.append("</table>");
				Str.append("</div>");
			} else {
				Str.append("<br><br><br><br><b><center><font color=red>No Option(s) found!</font></center></b><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {

		StrSql = "SELECT emp_role_id"
				+ " FROM " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id = " + emp_id + "";
		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			while (crs.next()) {
				emp_role_id = crs.getString("emp_role_id");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
