package axela.inventory;
//Bhagwan Singh 22th Feb, 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Stock_Options extends Connect {

	// ///// List page links
	public String emp_id = "0";
	public String comp_id = "0";
	public String StrHTML = "";
	public String msg = "";
	public String StrSql = "";
	public String StrJoin = "";
	public String StrSearch = "";
	public String all = "", add = "", delete = "";
	public String item_name = "";
	public String itemmaster_name = "";
	public String trans_vehstock_id = "0";
	public String trans_option_id = "0";
	public String emp_role_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_stock_add", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				msg = PadQuotes(request.getParameter("msg"));
				all = PadQuotes(request.getParameter("all"));
				add = PadQuotes(request.getParameter("add"));
				delete = PadQuotes(request.getParameter("delete"));
				trans_vehstock_id = CNumeric(PadQuotes(request.getParameter("vehstock_id")));
				trans_option_id = CNumeric(PadQuotes(request.getParameter("option_id")));
				PopulateConfigDetails();

				if (!trans_vehstock_id.equals("0")) {
					StrSql = "SELECT IF(item_code != '', CONCAT(item_name, ' (', item_code, ')'), item_name) AS item_name"
							+ " FROM " + compdb(comp_id) + "axela_inventory_item"
							+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_item_id = item_id"
							+ " WHERE vehstock_id = " + trans_vehstock_id + "";
					itemmaster_name = ExecuteQuery(StrSql);
				}

				if (itemmaster_name.equals("")) {
					response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Stock!"));
				}

				if (add.equals("yes") && !trans_option_id.equals("0") && !trans_option_id.equals("0")) {
					AddFields();
				}

				if (delete.equals("yes") && !trans_vehstock_id.equals("0") && !trans_option_id.equals("0")) {
					DeleteOptionItem();
					msg = "<br>Option deleted successfully!";
				}

				if (itemmaster_name.equals("")) {
					StrHTML = "<font color=\"red\"><b>Invalid Stock</b></font>!";
				} else {
					StrHTML = Listdata();
				}
			}

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void CheckForm() {
		StrSql = "SELECT trans_vehstock_id"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
				+ " WHERE trans_vehstock_id = " + trans_vehstock_id + ""
				+ " AND trans_option_id = " + trans_option_id + "";
		if (ExecuteQuery(StrSql).equals(trans_vehstock_id)) {
			msg += "<br>Option already added!";
		}

		if (!msg.equals("")) {
			msg = "Error!" + msg;
		}
	}

	public void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option_trans"
					+ " (trans_option_id,"
					+ " trans_vehstock_id)"
					+ " VALUES"
					+ " (" + trans_option_id + ","
					+ " " + trans_vehstock_id + ")";
			updateQuery(StrSql);
			msg = "<br>Option added successfully!";
		}
	}

	public void DeleteOptionItem() {
		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option_trans"
				+ " WHERE trans_vehstock_id = " + trans_vehstock_id + ""
				+ " AND trans_option_id = " + trans_option_id + "";
		updateQuery(StrSql);
	}

	public String Listdata() {
		int count = 0;
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT option_id, option_code, option_name,"
					+ " COALESCE(model_name,'') AS model_name, optiontype_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_trans ON trans_option_id = option_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock ON vehstock_id = trans_vehstock_id"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item ON item_code = option_code"
					+ " LEFT JOIN " + compdb(comp_id) + "axela_inventory_item_model ON model_id = item_model_id"
					+ " INNER JOIN axelaauto.axela_brand ON brand_id = option_brand_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_vehstock_option_type ON optiontype_id = option_optiontype_id"
					+ " WHERE vehstock_id = " + trans_vehstock_id + ""
					+ " GROUP BY option_id"
					+ " ORDER BY option_name";
			// SOP("StrSql===" + StrSql);

			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				// Str.append("<div class=\" table-bordered\">\n");
				Str.append("<table class=\"table table-bordered table-hover table-responsive\" data-filter=\"#filter\">\n");
				Str.append("<thead><tr align=\"center\">\n");
				Str.append("<th data-toggle=\"true\">#</th>\n");
				Str.append("<th>Option Name</th>\n");
				Str.append("<th>Option Type</th>\n");
				Str.append("<th data-hide=\"phone\">Model</th>\n");
				Str.append("<th data-hide=\"phone\">Actions</th>\n");
				Str.append("</tr></thead><tbody>\n");
				while (crs.next()) {
					count++;
					Str.append("<tr>\n");
					Str.append("<td valign=\"top\" align=\"center\">").append(count).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">");
					Str.append(crs.getString("option_name"));
					if (!crs.getString("option_code").equals("")) {
						Str.append(" <br>").append("Code: ").append(crs.getString("option_code"));
					}
					Str.append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("optiontype_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\">").append(crs.getString("model_name")).append("</td>\n");
					Str.append("<td valign=\"top\" align=\"left\" nowrap><a href=\"stock-options.jsp?delete=yes&option_id=");
					Str.append(crs.getString("option_id")).append("&vehstock_id=").append(trans_vehstock_id).append(" \">Delete Option</a>");
					Str.append("</td>\n");
					Str.append("</tr>\n");
				}
				Str.append("</tbody></table>\n");
			} else {
				Str.append("<br><br><br><br><b><font color=\"red\">No Option(s) found!</font></b><br><br>");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public void PopulateConfigDetails() {
		StrSql = "SELECT emp_role_id FROM " + compdb(comp_id) + "axela_emp"
				+ " WHERE emp_id = " + emp_id + "";
		emp_role_id = ExecuteQuery(StrSql);
	}
}
