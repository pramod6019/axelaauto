package axela.inventory;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageStockOption_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String branch_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public String StrSql1 = "";
	public static String msg = "";
	public String option_id = "0";
	public String option_code = "";
	public String option_name = "";
	public String option_optiontype_id = "1", vehstock_branch_id = "0", option_brand_id = "0";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("Add"));
				update = PadQuotes(request.getParameter("Update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				option_id = CNumeric(PadQuotes(request.getParameter("option_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						option_code = "";
					} else {
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("../inventory/managestockoption.jsp?option_id=" + option_id + "&msg=Stock Option Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Stock Option".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Stock Option".equals(deleteB)) {
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("../inventory/managestockoption.jsp?option_id=" + option_id + "&msg=Stock Option Updated Successfully!" + msg + ""));
						}
					} else if ("Delete Stock Option".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managestockoption.jsp?msg=Stock Option Deleted Successfully!"));
						}
					}
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		option_brand_id = CNumeric(PadQuotes(request.getParameter("dr_option_brand_id")));
		option_code = PadQuotes(request.getParameter("txt_option_code"));
		option_name = PadQuotes(request.getParameter("txt_option_name"));
		option_optiontype_id = CNumeric(PadQuotes(request.getParameter("dr_option_optiontype_id")));
	}

	protected void CheckForm() throws SQLException {
		msg = "";
		if (option_brand_id.equals("0")) {
			msg = msg + "<br>Select Principal!";
		}

		if (option_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			StrSql = "SELECT option_name FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE option_name = '" + option_name + "'"
					+ " AND option_brand_id =" + option_brand_id;
			if (update.equals("yes")) {
				StrSql += " AND option_id != " + option_id + "";
			}
			// SOP("StrSql------" + StrSql);
			if (ExecuteQuery(StrSql).equals(option_name)) {
				msg = msg + "<br>Similar Name Found!";
			}
		}

		if (option_code.equals("")) {
			msg = msg + "<br>Enter Code!";
		} else {
			StrSql = "SELECT option_name FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE option_name = '" + option_code + "'"
					+ " AND option_brand_id =" + option_brand_id;
			if (update.equals("yes")) {
				StrSql += " AND option_id != " + option_id + "";
			}
			if (ExecuteQuery(StrSql).equals(option_code)) {
				msg = msg + "<br>Similar Code Found!";
			}
		}

		if (option_optiontype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}
	}

	protected void AddFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				option_id = ExecuteQuery("Select max(option_id) as ID from " + compdb(comp_id) + "axela_vehstock_option");
				if (option_id == null || option_id.equals("")) {
					option_id = "0";
				}
				int variantcolour_idi = Integer.parseInt(option_id) + 1;
				option_id = "" + variantcolour_idi;
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_vehstock_option"
						+ " (option_id,"
						+ " option_code,"
						+ " option_name,"
						+ " option_optiontype_id,"
						+ " option_brand_id)"
						+ " VALUES"
						+ " ('" + option_id + "',"
						+ " '" + option_code + "',"
						+ " '" + option_name + "',"
						+ " " + option_optiontype_id + ","
						+ " " + option_brand_id + ")";
				// SOP("SqlStr---ADDf-----" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT option_id, option_code, option_name, option_brand_id, option_optiontype_id"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE option_id = " + option_id + "";
			// SOP("SqlStr---popu-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					option_id = crs.getString("option_id");
					option_code = crs.getString("option_code");
					option_name = crs.getString("option_name");
					option_brand_id = crs.getString("option_brand_id");
					option_optiontype_id = crs.getString("option_optiontype_id");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Stock Option!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock_option"
					+ " SET"
					+ " option_code = '" + option_code + "',"
					+ " option_name = '" + option_name + "',"
					+ " option_brand_id = " + option_brand_id + ","
					+ " option_optiontype_id = " + option_optiontype_id + ""
					+ " WHERE option_id = " + option_id + "";
			updateQuery(StrSql);
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT enquiry_option_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_option_id = " + option_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Stock Option is associated with a Enquiry!";
		}

		// StrSql = "SELECT item_variantcolour_id FROM " + compdb(comp_id) + "axela_inventory_item where item_variantcolour_id = " + option_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>Stock Option is associated with a Item(s)!";
		// }
		// if (option_id.equals("1")) {
		// msg = msg + "<br>Cannot Delete First Record!";
		// }
		if (msg.equals("")) {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_vehstock_option"
					+ " WHERE option_id = " + option_id + "";
			updateQuery(StrSql);
		}
	}

	public String PopulatePrincipal(String option_brand_id, String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT brand_id, brand_name"
					+ " FROM axela_brand"
					+ " ORDER BY brand_name";
			CachedRowSet crs = processQuery(StrSql, 0);

			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("brand_id"));
				Str.append(StrSelectdrop(crs.getString("brand_id"), option_brand_id));
				Str.append(">").append(crs.getString("brand_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}

	public String PopulateOptionType() {
		try {
			StrSql = "SELECT optiontype_id, optiontype_name"
					+ " FROM " + compdb(comp_id) + "axela_vehstock_option_type"
					+ " ORDER BY optiontype_id";
			CachedRowSet crs = processQuery(StrSql);

			StringBuilder Str = new StringBuilder();
			Str.append("<option value=\"0\"> Select </option>\n");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("optiontype_id"));
				Str.append(StrSelectdrop(crs.getString("optiontype_id"), option_optiontype_id));
				Str.append(">").append(crs.getString("optiontype_name")).append("</option>\n");
			}
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError(this.getClass().getName());
			SOPError(new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
