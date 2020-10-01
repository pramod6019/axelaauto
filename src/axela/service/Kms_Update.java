package axela.service;
//satish 11-march-2013

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Kms_Update extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_role_id = "0";
	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String vehkms_id = "0";
	public String veh_customer_id = "0";
	public String veh_contact_id = "0";
	public String vehkms_entry_id = "0";
	public String vehkms_entry_date = "";
	public String vehkms_modified_id = "0";
	public String vehkms_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String BranchAccess = "";
	public String customer_name = "";
	public String link_customer_name = "";
	public String link_contact_name = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public String vehkms_veh_id = "0";
	public String veh_chassis_no = "";
	public String veh_engine_no = "", veh_reg_no = "";
	public String QueryString = "";
	public String veh_kms = "";
	public String contact_id = "0";
	public String vehkms_kms = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				add = PadQuotes(request.getParameter("add"));
				addB = PadQuotes(request.getParameter("add_button"));
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				vehkms_veh_id = PadQuotes(request.getParameter("vehkms_veh_id"));
				vehkms_id = PadQuotes(request.getParameter("vehkms_id"));
				QueryString = PadQuotes(request.getQueryString());
				if (!vehkms_veh_id.equals("") && add.equals("yes")) {
					PopulateVehicleDetails(response);
				}

				if (add.equals("yes")) {
					status = "Add";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_vehicle_add", request).equals("1")) {
							vehkms_entry_id = emp_id;
							vehkms_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("kms-list.jsp?vehkms_veh_id=" + vehkms_veh_id + "&msg=Kms added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}

				if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(update)) {
					if (!"yes".equals(updateB)) {
						contact_id = veh_contact_id;
						PopulateFields(request, response);
					} else if ("yes".equals(updateB) && !"Delete Kms".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_vehicle_edit", request).equals("1")) {
							CheckForm();
							vehkms_modified_id = emp_id;
							vehkms_modified_date = ToLongDate(kknow());
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								StrSql = "Select vehkms_veh_id from " + compdb(comp_id) + "axela_service_veh_kms where vehkms_id =" + vehkms_id + "";
								vehkms_veh_id = ExecuteQuery(StrSql);
								response.sendRedirect(response.encodeRedirectURL("kms-list.jsp?vehkms_veh_id=" + vehkms_veh_id + "&msg=Kms updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}

					if ("Delete Kms".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_vehicle_delete", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("kms-list.jsp?all=yes&msg=Kms deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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

		veh_chassis_no = PadQuotes(request.getParameter("txt_veh_chassis_no"));
		veh_reg_no = PadQuotes(request.getParameter("txt_veh_reg_no"));
		veh_engine_no = PadQuotes(request.getParameter("txt_veh_engine_no"));
		veh_contact_id = CNumeric(PadQuotes(request.getParameter("cont_id")));
		veh_customer_id = CNumeric(PadQuotes(request.getParameter("acct_id")));
		vehkms_veh_id = CNumeric(PadQuotes(request.getParameter("vehkms_veh_id")));
		veh_kms = PadQuotes(request.getParameter("txt_veh_kms"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		if (!veh_contact_id.equals("0") && update.equals("yes")) {
			PopulateContactDetails();
		}
	}

	protected void CheckForm() {
		msg = "";
		if (veh_kms.equals("")) {
			msg = msg + "<br>Enter Kms!";
		}
		if (!veh_reg_no.equals("")) {
			veh_reg_no = veh_reg_no.replaceAll(" ", "");
			veh_reg_no = veh_reg_no.replaceAll("-", "");
		}
		// StrSql = "SELECT max(vehkms_kms)"
		// + " FROM " + compdb(comp_id) + "axela_service_veh_kms"
		// + " WHERE vehkms_veh_id = " + vehkms_veh_id + "";
		// SOP("==" + StrSql);
		// if (update.equals("yes") && !vehkms_id.equals("")) {
		// StrSql = StrSql +" "
		// }
		// if (!veh_kms.equals("")) {
		// if (Integer.parseInt(veh_kms) <
		// Integer.parseInt(ExecuteQuery(StrSql))) {
		// msg = msg + "<br>Invalid Kms!";
		// }
		// }

	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				vehkms_id = ExecuteQuery("SELECT COALESCE(MAX(vehkms_id), 0) + 1"
						+ " FROM " + compdb(comp_id) + "axela_service_veh_kms");

				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();

				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_kms"
						+ " (vehkms_id,"
						+ " vehkms_veh_id,"
						+ " vehkms_kms,"
						+ " vehkms_entry_id,"
						+ " vehkms_entry_date)"
						+ " VALUES"
						+ " ('" + vehkms_id + "',"
						+ " '" + vehkms_veh_id + "',"
						+ " '" + veh_kms + "',"
						+ " '" + vehkms_entry_id + "',"
						+ " '" + vehkms_entry_date + "')";
				stmttx.execute(StrSql, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = stmttx.getGeneratedKeys();
				while (rs.next()) {
					vehkms_id = rs.getString(1);
				}
				rs.close();
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh"
						+ " SET"
						+ " veh_kms = '" + veh_kms + "',"
						+ " veh_lastservice = '" + vehkms_entry_date + "'"
						+ " WHERE veh_id = " + vehkms_veh_id + "";
				stmttx.execute(StrSql);

				conntx.commit();
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("conn is closed.....");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
				}
				msg = msg + "<br>Transaction Error!";
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);
				stmttx.close();
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		}
	}

	public void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (msg.equals("")) {
			try {

				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh_kms"
						+ " SET"
						+ " vehkms_kms = '" + veh_kms + "',"
						+ " vehkms_modified_id = " + vehkms_modified_id + ","
						+ " vehkms_modified_date = '" + vehkms_modified_date + "'"
						+ " WHERE vehkms_id = " + vehkms_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT veh_id, vehkms_veh_id, title_id, title_desc, contact_fname,"
					+ " contact_lname, contact_mobile1, contact_phone1, contact_email1,"
					+ " contact_id, customer_id, customer_name, veh_chassis_no,"
					+ " veh_reg_no, veh_engine_no, vehkms_kms, vehkms_entry_id,"
					+ " vehkms_entry_date, vehkms_modified_id, vehkms_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_kms"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = vehkms_veh_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " WHERE vehkms_id = " + CNumeric(vehkms_id) + BranchAccess;
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					vehkms_kms = crs.getString("vehkms_kms");
					veh_kms = crs.getString("vehkms_kms");
					veh_chassis_no = crs.getString("veh_chassis_no");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					veh_engine_no = crs.getString("veh_engine_no");
					vehkms_veh_id = crs.getString("vehkms_veh_id");
					veh_customer_id = crs.getString("customer_id");
					veh_contact_id = crs.getString("contact_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id")
							+ "\">" + crs.getString("customer_name") + " (" + crs.getString("customer_id") + ")</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">"
							+ crs.getString("title_desc") + " " + crs.getString("contact_fname") + " "
							+ crs.getString("contact_lname") + " (" + crs.getString("contact_id") + ")</a>";

					customer_name = crs.getString("customer_name");
					vehkms_entry_id = crs.getString("vehkms_entry_id");
					if (!vehkms_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(vehkms_entry_id));
					}
					entry_date = strToLongDate(crs.getString("vehkms_entry_date"));
					vehkms_modified_id = crs.getString("vehkms_modified_id");
					if (!vehkms_modified_id.equals("")) {
						modified_by = Exename(comp_id, Integer.parseInt(vehkms_modified_id));
					}
					modified_date = strToLongDate(crs.getString("vehkms_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle Kms!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void DeleteFields(HttpServletResponse response) {

		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_service_veh_kms"
						+ " WHERE vehkms_id = " + vehkms_id + "";
				// SOP("St===" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public void PopulateVehicleDetails(HttpServletResponse response) {
		StrSql = "SELECT veh_reg_no, veh_chassis_no, veh_engine_no, customer_id,"
				+ " contact_id, customer_name, contact_fname, veh_variant_id, contact_lname,"
				+ " title_desc, customer_address, city_name, state_name, customer_pin, item_model_id"
				+ " FROM " + compdb(comp_id) + "axela_service_veh"
				+ " INNER JOIN " + compdb(comp_id) + "axela_inventory_item ON item_id = veh_variant_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = veh_customer_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_customer_contact ON contact_id = veh_contact_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = customer_city_id"
				+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
				+ " WHERE veh_id = " + CNumeric(vehkms_veh_id) + "";

		CachedRowSet crs = processQuery(StrSql, 0);
		try {
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					veh_chassis_no = crs.getString("veh_chassis_no");
					veh_engine_no = crs.getString("veh_engine_no");
					veh_reg_no = SplitRegNo(crs.getString("veh_reg_no"), 2);
					veh_customer_id = crs.getString("customer_id");
					veh_contact_id = crs.getString("contact_id");
					link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + " ("
							+ crs.getString("customer_id") + ")</a>";
					link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("title_desc") + " "
							+ crs.getString("contact_fname")
							+ " " + crs.getString("contact_lname") + " (" + crs.getString("contact_id") + ")</a>";
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Vehicle!"));
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void PopulateContactDetails() {
		try {
			StrSql = "SELECT customer_id, contact_id, customer_name, contact_fname,"
					+ " contact_lname, contact_email1, contact_mobile1, title_desc"
					+ " FROM " + compdb(comp_id) + "axela_customer_contact"
					+ " INNER JOIN " + compdb(comp_id) + "axela_title ON title_id = contact_title_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_customer ON customer_id = contact_customer_id"
					+ " WHERE contact_id = " + veh_contact_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			while (crs.next()) {
				link_customer_name = "<a href=\"../customer/customer-list.jsp?customer_id=" + crs.getString("customer_id") + "\">" + crs.getString("customer_name") + " ("
						+ crs.getString("customer_id") + ")</a>";
				link_contact_name = "<a href=\"../customer/customer-contact-list.jsp?contact_id=" + crs.getString("contact_id") + "\">" + crs.getString("title_desc") + " "
						+ crs.getString("contact_fname")
						+ " " + crs.getString("contact_lname") + " (" + crs.getString("contact_id") + ")</a>";
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
