package axela.service;
//Dilip Kumar 24 Apr 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Veh_Ownership_Update extends Connect {

	public String add = "";
	public String addB = "";
	public String update = "";
	public String updateB = "";
	public String deleteB = "";
	public String status = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String veh_id = "0";
	public String veh_reg_no = "";
	public static String msg = "";
	public String StrSql = "";
	public String emp_role_id = "";
	public String ownership_id = "0";
	public String ownership_veh_id = "0";
	public String ownership_name = "";
	public String ownership_address = "";
	public String ownership_mobile1 = "91-";
	public String ownership_email1 = "";
	public String ownership_from = "";
	public String ownershipfrom = "";
	public String ownership_to = "";
	public String ownershipto = "";
	public String ownership_notes = "";
	public String entry_by = "";
	public String ownership_entry_id = "";
	public String ownership_entry_date = "";
	public String ownership_modified_id = "0";
	public String ownership_modified_date = "";
	public String modified_by = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_role_id = CNumeric(GetSession("emp_role_id", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_service_vehicle_access", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				ownership_id = CNumeric(PadQuotes(request.getParameter("ownership_id")));
				veh_id = CNumeric(PadQuotes(request.getParameter("veh_id")));
				QueryString = PadQuotes(request.getQueryString());
				veh_reg_no = ExecuteQuery("SELECT veh_reg_no FROM " + compdb(comp_id) + "axela_service_veh WHERE veh_id = " + veh_id);
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						ownership_mobile1 = "91-";
						ownership_from = DateToShortDate(kknow());
						ownershipfrom = ownership_from;
						ownership_to = DateToShortDate(kknow());
						ownershipto = ownership_to;
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_vehicle_add", request).equals("1")) {
							ownership_entry_id = emp_id;
							ownership_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../service/vehicle-dash.jsp?veh_id=" + veh_id + "&msg=Ownership added successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Ownership".equals(deleteB)) {
						PopulateFields(request, response);
					} else if ("yes".equals(updateB) && !"Delete Ownership".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_vehicle_edit", request).equals("1")) {
							ownership_modified_id = emp_id;
							ownership_modified_date = ToLongDate(kknow());
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../service/vehicle-dash.jsp?veh_id=" + veh_id + "&msg=Ownership updated successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
					if ("Delete Ownership".equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_service_vehicle_delete", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("../service/vehicle-dash.jsp?veh_id=" + veh_id + "&msg=Ownership deleted successfully!"));
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
		ownership_name = PadQuotes(request.getParameter("txt_ownership_name"));
		ownership_address = PadQuotes(request.getParameter("txt_ownership_address"));
		ownership_mobile1 = PadQuotes(request.getParameter("txt_ownership_mobile"));
		ownership_email1 = PadQuotes(request.getParameter("txt_ownership_email1"));
		ownershipfrom = PadQuotes(request.getParameter("txt_ownership_from"));
		ownershipto = PadQuotes(request.getParameter("txt_ownership_to"));
		ownership_notes = PadQuotes(request.getParameter("txt_ownership_notes"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		ownership_entry_date = PadQuotes(request.getParameter("ownership_entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		ownership_modified_date = PadQuotes(request.getParameter("ownership_modified_date"));
	}

	protected void CheckForm() {
		msg = "";

		if (ownership_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}
		if (ownership_address.equals("")) {
			msg = msg + "<br>Enter Address!";
		}
		if (ownership_mobile1.equals("91-"))
		{
			ownership_mobile1 = "";
		}
		if (ownership_mobile1.equals("")) {
			msg = msg + "<br>Enter Mobile!";
		}
		if (!ownership_mobile1.equals("") && !IsValidMobileNo11(ownership_mobile1)) {
			msg = msg + "<br>Enter Valid Mobile!";
		}
		if (ownershipfrom.equals("")) {
			msg = msg + "<br>SELECT Ownership FROM!";
		} else {
			if (isValidDateFormatShort(ownershipfrom)) {
				ownership_from = ConvertShortDateToStr(ownershipfrom);
			} else {
				msg = msg + "<br>Enter Valid Ownership FROM!";
			}
		}
		if (ownershipto.equals("")) {
			msg = msg + "<br>SELECT Ownership To!";
		} else {
			if (isValidDateFormatShort(ownershipto)) {
				ownership_to = ConvertShortDateToStr(ownershipto);
			} else {
				msg = msg + "<br>Enter Valid Ownership To!";
			}
		}

		if (!ownershipto.equals("") && !ownershipfrom.equals("") && isValidDateFormatShort(ownershipfrom) && isValidDateFormatShort(ownershipto)) {
			if (Long.parseLong(ConvertShortDateToStr(ownershipto)) < Long.parseLong(ConvertShortDateToStr(ownershipfrom))) {
				msg = msg + "<br>End Date Should be greater than Start Date!";
			}
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();

		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_veh_ownership"
						+ " (ownership_veh_id,"
						+ " ownership_name,"
						+ " ownership_address,"
						+ " ownership_email1,"
						+ " ownership_mobile1,"
						+ " ownership_from,"
						+ " ownership_to,"
						+ " ownership_notes,"
						+ " ownership_entry_id,"
						+ " ownership_entry_date)"
						+ " VALUES"
						+ " (" + veh_id + ","
						+ " '" + ownership_name + "',"
						+ " '" + ownership_address + "',"
						+ " '" + ownership_email1 + "',"
						+ " '" + ownership_mobile1 + "',"
						+ " '" + ownership_from + "',"
						+ " '" + ownership_to + "',"
						+ " '" + ownership_notes + "',"
						+ " " + ownership_entry_id + ","
						+ " '" + ownership_entry_date + "')";
				// SOP("SqlStr==" + StrSql);
				ownership_id = UpdateQueryReturnID(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {
			StrSql = "SELECT " + compdb(comp_id) + "axela_service_veh_ownership.*, veh_id, veh_reg_no"
					+ " FROM " + compdb(comp_id) + "axela_service_veh_ownership"
					+ " INNER JOIN " + compdb(comp_id) + "axela_service_veh ON veh_id = ownership_veh_id"
					+ " WHERE ownership_id = " + ownership_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);

			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					ownership_name = crs.getString("ownership_name");
					ownership_veh_id = crs.getString("veh_id");
					ownership_address = crs.getString("ownership_address");
					ownership_mobile1 = crs.getString("ownership_mobile1");
					ownership_email1 = crs.getString("ownership_email1");
					ownership_from = crs.getString("ownership_from");
					ownershipfrom = strToShortDate(ownership_from);
					ownership_to = crs.getString("ownership_to");
					ownershipto = strToShortDate(ownership_to);
					ownership_notes = crs.getString("ownership_notes");
					ownership_entry_id = crs.getString("ownership_entry_id");
					if (!ownership_entry_id.equals("")) {
						entry_by = Exename(comp_id, Integer.parseInt(ownership_entry_id));
					}
					ownership_entry_date = strToLongDate(crs.getString("ownership_entry_date"));
					ownership_modified_id = crs.getString("ownership_modified_id");
					if (!ownership_modified_id.equals("")) {
						modified_by = Exename(comp_id, Integer.parseInt(ownership_modified_id));
					}
					ownership_modified_date = strToLongDate(crs.getString("ownership_modified_date"));
				}
			} else {
				msg = "msg=Access denied. Please contact system administrator!";
				response.sendRedirect("../portal/error.jsp?" + msg);
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_veh_ownership"
						+ " SET"
						+ " ownership_name = '" + ownership_name + "',"
						+ " ownership_address = '" + ownership_address + "',"
						+ " ownership_mobile1 = '" + ownership_mobile1 + "',"
						+ " ownership_email1 = '" + ownership_email1 + "',"
						+ " ownership_from = '" + ownership_from + "',"
						+ " ownership_to = '" + ownership_to + "',"
						+ " ownership_notes = '" + ownership_notes + "',"
						+ " ownership_modified_id = " + ownership_modified_id + ","
						+ " ownership_modified_date = '" + ownership_modified_date + "'"
						+ " WHERE ownership_id = " + ownership_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields(HttpServletResponse response) {
		if (msg.equals("")) {
			try {
				StrSql = "Delete FROM " + compdb(comp_id) + "axela_service_veh_ownership"
						+ " WHERE ownership_id = " + ownership_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
