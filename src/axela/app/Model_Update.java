package axela.app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Model_Update extends Connect {

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
	public static String msg = "";
	public String model_id = "0";
	public String model_name = "";
	public String model_service_code = "";
	public String model_mileage = "";
	public String model_engine = "";
	public String model_emi = "";

	public String model_desc = "";
	public String model_active = "";
	public String QueryString = "";
	public String model_entry_id = "0";
	public String model_entry_date = "";
	public String model_modified_id = "0";
	public String model_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String emp_role_id = "0";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				QueryString = PadQuotes(request.getQueryString());
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				// SOP(ReturnPerm(comp_id, "emp_role_id", request));
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						model_name = "";
						model_service_code = "";
						model_mileage = "";
						model_engine = "";
						model_emi = "";
						model_desc = "";
						model_active = "1";
					} else {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							GetValues(request, response);
							model_entry_id = emp_id;
							model_entry_date = ToLongDate(kknow());
							AddFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../app/model-list.jsp?model_id=" + model_id + "&msg=Model Added Successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Model".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Model".equals(deleteB)) {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							GetValues(request, response);
							model_modified_id = emp_id;
							model_modified_date = ToLongDate(kknow());
							UpdateFields();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../app/model-list.jsp?model_id=" + model_id + "&msg=Model Updated Successfully!" + msg + ""));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}

					} else if ("Delete Model".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("model-list.jsp?msg=Model Deleted Successfully!"));
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
		model_name = PadQuotes(request.getParameter("txt_model_name"));
		model_service_code = PadQuotes(request.getParameter("txt_model_service_code"));
		model_mileage = PadQuotes(request.getParameter("txt_model_mileage"));
		model_engine = PadQuotes(request.getParameter("txt_model_engine"));
		model_emi = PadQuotes(request.getParameter("txt_model_emi"));
		model_desc = PadQuotes(request.getParameter("txt_model_desc"));
		model_active = PadQuotes(request.getParameter("chk_model_active"));
		if (model_active.equals("on")) {
			model_active = "1";
		} else {
			model_active = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (model_name.equals("")) {
			msg = msg + "<br>Enter Model!";
		}
		try {
			if (!model_name.equals("")) {
				if (update.equals("yes") && !model_name.equals("")) {
					StrSql = "SELECT model_name FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_name = '" + model_name + "' AND model_id != " + model_id + "";
				}
				if (add.equals("yes") && !model_name.equals("")) {
					StrSql = "SELECT model_name FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_name = '" + model_name + "'";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Model Found!";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		if (model_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (model_desc.length() > 255) {
			model_desc = model_desc.substring(0, 254);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				model_id = ExecuteQuery("SELECT max(model_id) as ID FROM " + compdb(comp_id) + "axela_inventory_item_model");
				if (model_id == null || model_id.equals("")) {
					model_id = "0";
				}
				int model_idi = Integer.parseInt(model_id) + 1;
				model_id = "" + model_idi;
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_inventory_item_model"
						+ " (model_id,"
						+ " model_name,"
						+ " model_service_code,"
						+ " model_mileage,"
						+ " model_engine,"
						+ " model_emi,"
						+ " model_desc,"
						+ " model_active,"
						+ " model_entry_id,"
						+ " model_entry_date)"
						+ " VALUES"
						+ " ('" + model_id + "',"
						+ " '" + model_name + "',"
						+ " '" + model_service_code + "',"
						+ " '" + model_mileage + "',"
						+ " '" + model_engine + "',"
						+ " '" + model_emi + "',"
						+ " '" + model_desc + "',"
						+ " '" + model_active + "',"
						+ " " + model_entry_id + ","
						+ " '" + model_entry_date + "')";
				// SOP("StrSql-----" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_id = " + model_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					model_id = crs.getString("model_id");
					model_name = crs.getString("model_name");
					model_service_code = crs.getString("model_service_code");
					model_mileage = crs.getString("model_mileage");
					model_engine = crs.getString("model_engine");
					model_emi = crs.getString("model_emi");
					model_desc = crs.getString("model_desc");
					model_active = crs.getString("model_active");
					model_entry_id = crs.getString("model_entry_id");
					if (!model_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(model_entry_id));
						entry_date = strToLongDate(crs.getString("model_entry_date"));
					}
					model_modified_id = crs.getString("model_modified_id");
					if (!model_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(model_modified_id));
						modified_date = strToLongDate(crs.getString("model_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Item Model!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_inventory_item_model"
						+ " SET"
						+ " model_name = '" + model_name + "',"
						+ " model_service_code = '" + model_service_code + "',"
						+ " model_mileage = '" + model_mileage + "',"
						+ " model_engine = '" + model_engine + "',"
						+ " model_emi = '" + model_emi + "',"
						+ " model_desc = '" + model_desc + "',"
						+ " model_active = '" + model_active + "',"
						+ " model_modified_id = " + model_modified_id + ","
						+ " model_modified_date = '" + model_modified_date + "'"
						+ " WHERE model_id = " + model_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {

		StrSql = "SELECT item_model_id FROM " + compdb(comp_id) + "axela_inventory_item WHERE item_model_id = " + model_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is associated with an Items!";
		}
		StrSql = "SELECT feature_model_id FROM " + compdb(comp_id) + "axela_app_model_feature WHERE feature_model_id = " + model_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is associated with an Features!";
		}

		StrSql = "SELECT enquiry_model_id FROM " + compdb(comp_id) + "axela_sales_enquiry WHERE enquiry_model_id = " + model_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is associated with an Enquiry!";
		}

		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_inventory_item_model WHERE model_id = " + model_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
