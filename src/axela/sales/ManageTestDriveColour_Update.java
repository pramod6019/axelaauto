// Ved Prakash (14 Feb 2013)
//-- edited 30 april, 2 may 2013(smitha  n)
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTestDriveColour_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String colour_id = "0";
	public String colour_name = "";
	public String colour_code = "";
	public String colour_active = "";
	public String colour_model_id = "0";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String colour_entry_id = "0";
	public String colour_entry_date = "";
	public String colour_modified_id = "";
	public String colour_modified_date = "";
	public String colour_item_id = "0";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				colour_id = CNumeric(PadQuotes(request.getParameter("colour_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						colour_active = "1";
					} else {
						GetValues(request, response);
						colour_entry_id = emp_id;
						colour_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivecolour.jsp?colour_id=" + colour_id + "&msg=Colour Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Colour".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Colour".equals(deleteB)) {
						GetValues(request, response);
						colour_modified_id = emp_id;
						colour_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivecolour.jsp?colour_id=" + colour_id + "&msg=Colour Updated Successfully!"));
						}
					} else if ("Delete Colour".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetestdrivecolour.jsp?msg=Colour Deleted Successfully!"));
						}
					}
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

	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		colour_id = PadQuotes(request.getParameter("colour_id"));
		colour_model_id = CNumeric(PadQuotes(request.getParameter("dr_model_id")));
		colour_name = PadQuotes(request.getParameter("txt_colour_name"));
		colour_code = PadQuotes(request.getParameter("txt_colour_code"));
		colour_active = PadQuotes(request.getParameter("ch_colour_active"));
		if (colour_active.equals("on")) {
			colour_active = "1";
		} else {
			colour_active = "0";
		}
		colour_item_id = PadQuotes(request.getParameter("dr_item_id"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (colour_model_id.equals("") || colour_model_id.equals("0")) {
			msg = msg + "<br>Select Model!";
		}
		if (colour_item_id.equals("0")) {
			msg = msg + "<br>Select Item!";
		}
		if (colour_name.equals("")) {
			msg = msg + "<br>Enter Colour!";
		} else {
			try {
				if (!colour_name.equals("")) {
					if (update.equals("yes") && !colour_name.equals("")) {
						StrSql = "Select colour_name"
								+ " from " + compdb(comp_id) + "axela_sales_testdrive_colour"
								+ " where colour_name = '" + colour_name + "' and colour_item_id = " + colour_item_id + " and colour_id != " + colour_id + "";
					}
					if (add.equals("yes") && !colour_name.equals("")) {
						StrSql = "Select colour_name"
								+ " from " + compdb(comp_id) + "axela_sales_testdrive_colour"
								+ " where colour_name = '" + colour_name + "' and colour_item_id = " + colour_item_id + "";
					}
					CachedRowSet crs1 = processQuery(StrSql, 0);
					while (crs1.next()) {
						msg = msg + "<br>Similar Colour Found!";
					}
					crs1.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		// if (colour_code.equals("")) {
		// msg = msg + "<br>Enter Colour Code!";
		// }
		// else {
		// try {
		// if (update.equals("yes") && !colour_code.equals("")) {
		// StrSql = "Select colour_code"
		// + " from " + compdb(comp_id) + "axela_sales_testdrive_colour"
		// + " where colour_code = '" + colour_code +"' and colour_id != " +
		// colour_id + "";
		// }
		// if (add.equals("yes") && !colour_code.equals("")) {
		// StrSql = "Select colour_code"
		// + " from " + compdb(comp_id) + "axela_sales_testdrive_colour"
		// + " where colour_code = '" + colour_code + "'";
		// }
		// CachedRowSet crs1 =processQuery(StrSql, 0);
		// while (crs1.next()) {
		// msg = msg + "<br>Similar Colour Code Found!";
		// }
		// crs1.close();
		// } catch (Exception ex) {
		// SOPError("Axelaauto== " + this.getClass().getName());
		// SOPError("Error in " + new
		// Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		// }
		// }
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				colour_id = ExecuteQuery("Select (coalesce(max(colour_id),0)+1) from " + compdb(comp_id) + "axela_sales_testdrive_colour");
				StrSql = "insert into " + compdb(comp_id) + "axela_sales_testdrive_colour"
						+ " (colour_id,"
						+ " colour_name,"
						+ " colour_code,"
						+ " colour_model_id,"
						+ " colour_item_id,"
						+ " colour_active,"
						+ " colour_entry_id,"
						+ " colour_entry_date)"
						+ " values"
						+ " (" + colour_id + ","
						+ " '" + colour_name + "',"
						+ " '" + colour_code + "',"
						+ " " + colour_model_id + ","
						+ " " + colour_item_id + ","
						+ " '" + colour_active + "',"
						+ " " + colour_entry_id + ","
						+ " '" + colour_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select colour_id, colour_name, colour_code, colour_model_id, colour_item_id, "
					+ " colour_active, colour_entry_id, colour_entry_date, colour_modified_id, "
					+ " colour_modified_date"
					+ " from " + compdb(comp_id) + "axela_sales_testdrive_colour"
					+ " where colour_id = " + colour_id + "";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					colour_name = crs.getString("colour_name");
					colour_code = crs.getString("colour_code");
					colour_item_id = crs.getString("colour_item_id");
					colour_model_id = crs.getString("colour_model_id");
					colour_active = crs.getString("colour_active");
					colour_entry_id = crs.getString("colour_entry_id");
					entry_by = Exename(comp_id, crs.getInt("colour_entry_id"));
					entry_date = strToLongDate(crs.getString("colour_entry_date"));
					colour_modified_id = crs.getString("colour_modified_id");
					if (!colour_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(colour_modified_id));
						modified_date = strToLongDate(crs.getString("colour_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?&msg=Invalid Colour!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive_colour"
						+ " SET"
						+ " colour_name = '" + colour_name + "',"
						+ " colour_code = '" + colour_code + "',"
						+ " colour_model_id  = " + colour_model_id + ","
						+ " colour_item_id  = " + colour_item_id + ","
						+ " colour_active = " + colour_active + ","
						+ " colour_modified_id = " + colour_modified_id + ","
						+ " colour_modified_date = '" + colour_modified_date + "'"
						+ " where colour_id = " + colour_id + "";
				// SOP("StrSql--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT trans_colour_id"
				+ " FROM " + compdb(comp_id) + "axela_sales_testdrive_colour_trans"
				+ " where trans_colour_id = " + colour_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Colour is associated with Test Drive!";
		}
		StrSql = "SELECT vehstock_colour_id"
				+ " FROM " + compdb(comp_id) + "axela_vehstock"
				+ " where vehstock_colour_id = " + colour_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Colour is associated with Stock!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_testdrive_colour "
						+ " where colour_id = " + colour_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateModel(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select model_id, model_name"
					+ " from " + compdb(comp_id) + "axela_inventory_item_model"
					+ " order by model_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP(StrSqlBreaker(StrSql));
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("model_id"));
				Str.append(StrSelectdrop(crs.getString("model_id"), colour_model_id));
				Str.append(">").append(crs.getString("model_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulateItem(String comp_id) {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select item_id, item_name "
					+ " from " + compdb(comp_id) + "axela_inventory_item "
					+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item_model on model_id = item_model_id "
					+ " where item_model_id = " + colour_model_id + " and item_type_id=1 "
					+ " order by item_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select name=\"dr_item_id\" id=\"dr_item_id\" class=\"form-control\" >");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("item_id")).append("");
				Str.append(StrSelectdrop(crs.getString("item_id"), colour_item_id));
				Str.append(">").append(crs.getString("item_name")).append("</option>\n");
			}
			crs.close();
			Str.append("</select>");
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}
}
