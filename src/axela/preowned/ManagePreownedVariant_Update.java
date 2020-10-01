// Bhagwan Singh (27th June 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedVariant_Update extends Connect {

	public String add = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String variant_id = "0";
	public String variant_name = "";
	public String variant_preownedmodel_id = "0";
	public String preownedmodel_carmanuf_id = "0";
	public String carmanuf_id = "0";
	public String variant_fueltype_id = "0";
	public String variant_entry_id = "0";
	public String variant_entry_by = "";
	public String variant_entry_date = "";
	public String variant_modified_id = "0";
	public String variant_modified_by = "";
	public String variant_modified_date = "";
	public String QueryString = "";
	public Preowned_Manufacturer_Check manufacturercheck = new Preowned_Manufacturer_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id, emp_preowned_stock_add", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				variant_id = CNumeric(PadQuotes(request.getParameter("variant_id")));
				variant_preownedmodel_id = CNumeric(PadQuotes(request.getParameter("preownedmodel_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						variant_entry_id = emp_id;
						variant_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedvariant.jsp?variant_id=" + variant_id + "&msg=Pre Owned Variant Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre Owned Variant".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre Owned Variant".equals(deleteB)) {
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedvariant.jsp?variant_id=" + variant_id + "&msg=Pre Owned Variant Updated Successfully!"));
						}
					}
					if ("Delete Pre Owned Variant".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedvariant.jsp?msg=Pre Owned Variant Deleted Successfully!"));
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
		variant_name = PadQuotes(request.getParameter("txt_variant_name"));
		variant_preownedmodel_id = PadQuotes(request.getParameter("manufacturermodel"));
		variant_fueltype_id = PadQuotes(request.getParameter("dr_variant_fueltype_id"));
		// preownedmodel_carmanuf_id =
		// PadQuotes(request.getParameter("manufacturermodel"));
		// SOP("variant_preownedmodel_id" + variant_preownedmodel_id);
		variant_entry_by = PadQuotes(request.getParameter("entry_by"));
		variant_modified_by = PadQuotes(request.getParameter("modified_by"));
		variant_entry_date = PadQuotes(request.getParameter("entry_date"));
		variant_modified_date = PadQuotes(request.getParameter("modified_date"));
		// SOP("variant_modified_by==" + variant_modified_by +
		// "  variant_modified_date" + variant_modified_date);
	}

	protected void CheckForm() {
		msg = "";
		if (variant_name.equals("")) {
			msg = "<br>Enter Name!";
		}
		try {
			if (!variant_name.equals("")) {
				StrSql = "SELECT variant_name"
						+ " FROM axela_preowned_variant"
						+ " WHERE variant_name = '" + variant_name + "'"
						+ " AND variant_preownedmodel_id = " + variant_preownedmodel_id;
				// SOP("StrSql--------" + StrSql);
				if (update.equals("yes")) {
					StrSql = StrSql + " AND variant_id != " + variant_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Variant Name Found! ";
				}
				crs.close();
			}

			// if (!variant_name.equals("")) {
			// StrSql = "SELECT variant_service_code"
			// + " FROM axela_preowned_variant"
			// + " WHERE variant_service_code = '" + variant_service_code + "'"
			// + " AND variant_preownedmodel_id = " + variant_preownedmodel_id
			// + " AND variant_name = '" + variant_name + "'";
			// if (update.equals("yes")) {
			// StrSql = StrSql + " AND variant_service_code != '" + variant_service_code + "'";
			// }
			// // SOP("StrSql--------" + StrSql);
			// CachedRowSet crs = processQuery(StrSql, 0);
			// if (crs.isBeforeFirst()) {
			// msg = msg + "<br>Similar Service Code Found! ";
			// }
			// crs.close();
			// }
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		if (variant_preownedmodel_id.equals("0")) {
			msg = msg + "<br>Select Pre Owned Model!";
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				variant_id = ExecuteQuery("SELECT COALESCE(MAX(variant_id), 0) + 1 AS variant_id FROM axela_preowned_variant");

				StrSql = "INSERT INTO axela_preowned_variant"
						+ " (variant_id,"
						+ " variant_name,"
						+ " variant_preownedmodel_id,"
						+ " variant_fueltype_id,"
						+ " variant_entry_id,"
						+ " variant_entry_date)"
						+ " VALUES"
						+ " (" + variant_id + ","
						+ " '" + variant_name + "',"
						+ " " + variant_preownedmodel_id + ","
						+ " " + variant_fueltype_id + ","
						+ " " + variant_entry_id + ","
						+ " '" + variant_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT variant_name, variant_preownedmodel_id, variant_entry_id,"
					+ " variant_fueltype_id,"
					+ " variant_entry_date, variant_modified_id, variant_modified_date,"
					+ " preownedmodel_carmanuf_id"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE variant_id = " + variant_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					variant_name = crs.getString("variant_name");
					variant_preownedmodel_id = crs.getString("variant_preownedmodel_id");
					variant_fueltype_id = crs.getString("variant_fueltype_id");
					variant_entry_id = crs.getString("variant_entry_id");
					if (!variant_entry_id.equals("")) {
						variant_entry_by = Exename(comp_id, Integer.parseInt(variant_entry_id));
					}
					variant_entry_date = strToLongDate(crs.getString("variant_entry_date"));
					variant_modified_id = crs.getString("variant_modified_id");
					if (!variant_modified_id.equals("")) {
						variant_modified_by = Exename(comp_id, Integer.parseInt(variant_modified_id));
					}
					variant_modified_date = strToLongDate(crs.getString("variant_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Pre Owned Variant!"));
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
				variant_modified_id = emp_id;
				variant_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE  axela_preowned_variant"
						+ " SET"
						+ " variant_name = '" + variant_name + "',"
						+ " variant_preownedmodel_id = " + variant_preownedmodel_id + ","
						+ " variant_fueltype_id = " + variant_fueltype_id + ","
						+ " variant_modified_id = " + variant_modified_id + ","
						+ " variant_modified_date  = '" + variant_modified_date + "'"
						+ " WHERE variant_id = " + variant_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	/*
	 * protected void DeleteFields() { StrSql = "SELECT preownedmodel_id FROM axela_preowned_model where preownedmodel_id = " + variant_preownedmodel_id + ""; if (!ExecuteQuery(StrSql).equals("")) {
	 * msg = msg + "<br>Variant is Associated with a Model!"; } if (msg.equals("")) { try { StrSql = "Delete from " + compdb(comp_id) + "axela_preowned_variant where variant_id = " + variant_id + "";
	 * updateQuery(StrSql); } catch (Exception ex) { SOPError("Axelaauto===" + this.getClass().getName()); SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex); } } }
	 */

	protected void DeleteFields() {

		StrSql = "SELECT" + " table_schema FROM" + " information_schema.TABLES" +
				" WHERE" + " table_schema LIKE 'axelaauto\\_%'" +
				" GROUP BY table_schema" + " ORDER BY table_schema";
		// SOP("StrSql---------" + StrSql);
		try {
			CachedRowSet crs = processQuery(StrSql, 0);
			int a = 0, b = 0;
			String compname = "";
			while (crs.next()) {

				compname = ExecuteQuery("SELECT comp_name" + "	FROM " +
						crs.getString("table_schema") + ".axela_comp");

				StrSql = "SELECT preowned_variant_id" + " FROM " +
						crs.getString("table_schema") + ".axela_preowned" +
						" WHERE preowned_variant_id = " + variant_id + "";

				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Pre Owned Variant is associated with Pre Owned of " + compname + "!";
				}

				StrSql = "SELECT enquiry_preownedvariant_id"
						+ " FROM " + crs.getString("table_schema") + ".axela_sales_enquiry" +
						" WHERE enquiry_preownedvariant_id = " + variant_id + ""
						+ " OR enquiry_tradein_preownedvariant_id = " + variant_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Pre Owned Variant is associated with Enquiry of " + compname + "!";
				}

				StrSql = "SELECT veh_variant_id"
						+ " FROM " + crs.getString("table_schema") + ".axela_service_veh" +
						" WHERE veh_variant_id = " + variant_id + "";
				if (!ExecuteQuery(StrSql).equals("")) {
					msg = msg + "<br>Pre Owned Variant is associated with Vehicle of " + compname + "!";
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM axela_preowned_variant_servicecode"
						+ " WHERE servicecode_variant_id = " + variant_id + "";
				updateQuery(StrSql);

				StrSql = "DELETE FROM axela_preowned_variant"
						+ " WHERE variant_id = " + variant_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
