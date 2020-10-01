package axela.app;
//@Shilpashree 03 oct 2015

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class ServiceCentre_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "0";
	public String servicecenter_id = "0";
	public String servicecenter_name = "";
	public String servicecenter_address = "";
	public String servicecenter_city_id = "0";
	public String servicecenter_pin = "";
	public String servicecenter_latitude = "";
	public String servicecenter_longitude = "";
	public String emp_role_id = "0";
	public String servicecenter_active = "0";
	public String servicecenter_entry_id = "0", servicecenter_entry_date = "";
	public String servicecenter_modified_id = "0", servicecenter_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String QueryString = "";
	public String servicecenter_mobile1 = "";
	public String servicecenter_mobile2 = "";
	public String servicecenter_phone1 = "";
	public String servicecenter_phone2 = "";
	public String servicecenter_email1 = "";
	public String servicecenter_email2 = "";
	public String servicecenter_website1 = "", servicecenter_website2 = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				emp_role_id = CNumeric(PadQuotes(GetSession("emp_role_id", request) + ""));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				servicecenter_id = CNumeric(PadQuotes(request.getParameter("servicecenter_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				// SOP("status = " + status);
				if ("yes".equals(add)) {
					if (!("yes").equals(addB)) {
						servicecenter_active = "1";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							servicecenter_entry_id = emp_id;
							servicecenter_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("servicecentre-list.jsp?servicecenter_id=" + servicecenter_id + "&msg=Service Centre added successfully!"));
								status = "";
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!("yes").equals(updateB) && !("Delete Service Centre").equals(deleteB)) {
						PopulateFields(response);
					} else if (("yes").equals(updateB) && !("Delete Service Centre").equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							servicecenter_modified_id = emp_id;
							servicecenter_modified_date = ToLongDate(kknow());
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Service Centre updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("servicecentre-list.jsp?servicecenter_id=" + servicecenter_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (("Delete Service Centre").equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("servicecentre-list.jsp?msg=Service Centre deleted successfully!"));
							}
						} else {
							response.sendRedirect(AccessDenied());
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
		servicecenter_name = PadQuotes(request.getParameter("txt_servicecenter_name"));
		servicecenter_mobile1 = PadQuotes(request.getParameter("txt_servicecenter_mobile1"));
		servicecenter_mobile2 = PadQuotes(request.getParameter("txt_servicecenter_mobile2"));
		servicecenter_phone1 = PadQuotes(request.getParameter("txt_servicecenter_phone1"));
		servicecenter_phone2 = PadQuotes(request.getParameter("txt_servicecenter_phone2"));
		servicecenter_email1 = PadQuotes(request.getParameter("txt_servicecenter_email1"));
		servicecenter_email2 = PadQuotes(request.getParameter("txt_servicecenter_email2"));
		servicecenter_website1 = PadQuotes(request.getParameter("txt_servicecenter_website1"));
		servicecenter_website2 = PadQuotes(request.getParameter("txt_servicecenter_website2"));
		servicecenter_address = PadQuotes(request.getParameter("txt_servicecenter_address"));
		if (servicecenter_address.length() > 255) {
			servicecenter_address = servicecenter_address.substring(0, 254);
		}
		servicecenter_city_id = PadQuotes(request.getParameter("maincity"));
		servicecenter_pin = PadQuotes(request.getParameter("txt_servicecenter_pin"));
		servicecenter_latitude = PadQuotes(request.getParameter("txt_servicecenter_latitude"));
		servicecenter_longitude = PadQuotes(request.getParameter("txt_servicecenter_longitude"));
		servicecenter_active = PadQuotes(request.getParameter("chk_servicecenter_active"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
		if (servicecenter_active.equals("on")) {
			servicecenter_active = "1";
		} else {
			servicecenter_active = "0";
		}
	}

	protected void CheckForm() {
		if (servicecenter_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			servicecenter_name = toTitleCase(servicecenter_name);
		}
		if (!servicecenter_phone1.equals("")) {
			if (!IsValidPhoneNo(servicecenter_phone1)) {
				msg = msg + "<br>Enter valid Phone1!";
			}
			// else {
			// StrSql = "SELECT servicecenter_id"
			// + " FROM " + compdb(comp_id) + "axela_app_servicecenter"
			// + " WHERE 1=1"
			// + " AND (servicecenter_phone1 = '" + servicecenter_phone1 + "'"
			// + " OR servicecenter_phone2 = '" + servicecenter_phone1 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND servicecenter_id != " + servicecenter_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Phone 1 found!";
			// }
			// }
		} else {
			msg = msg + "<br>Enter Phone 1!";
		}

		if (!servicecenter_phone2.equals("")) {
			if (!IsValidPhoneNo(servicecenter_phone2)) {
				msg = msg + "<br>Enter valid Phone2!";
			}
			// else {
			// StrSql = "SELECT servicecenter_id"
			// + " FROM " + compdb(comp_id) + "axela_app_servicecenter"
			// + " WHERE 1=1"
			// + " AND (servicecenter_phone1 = '" + servicecenter_phone2 + "'"
			// + " OR servicecenter_phone2 = '" + servicecenter_phone2 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND servicecenter_id != " + servicecenter_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Phone 2 found!";
			// } else if (!servicecenter_phone1.equals("") && servicecenter_phone2.equals(servicecenter_phone1)) {
			// msg += "<br>Phone2 is same as Phone1!";
			// }
			// }
		}

		if (!servicecenter_mobile1.equals("")) {
			if (!IsValidMobileNo(servicecenter_mobile1)) {
				msg = msg + "<br>Enter valid Mobile1!";
			}
			// else {
			// StrSql = "SELECT servicecenter_id"
			// + " FROM " + compdb(comp_id) + "axela_app_servicecenter"
			// + " WHERE 1=1"
			// + " AND (servicecenter_mobile1 = '" + servicecenter_mobile1 + "'"
			// + " OR servicecenter_mobile2 = '" + servicecenter_mobile1 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND servicecenter_id != " + servicecenter_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Mobile 1 found!";
			// }
			// }
		} else {
			msg = msg + "<br>Enter Mobile1!";
		}

		if (!servicecenter_mobile2.equals("")) {
			if (!IsValidMobileNo(servicecenter_mobile2)) {
				msg = msg + "<br>Enter valid Mobile2!";
			}
			// else {
			// StrSql = "SELECT servicecenter_id"
			// + " FROM " + compdb(comp_id) + "axela_app_servicecenter"
			// + " WHERE 1=1"
			// + " AND (servicecenter_mobile1 = '" + servicecenter_mobile2 + "'"
			// + " OR servicecenter_mobile2 = '" + servicecenter_mobile2 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND servicecenter_id != " + servicecenter_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Mobile 2 found!";
			// } else if (!servicecenter_mobile1.equals("") && servicecenter_mobile2.equals(servicecenter_mobile1)) {
			// msg += "<br>Mobile2 is same as Mobile1!";
			// }
			// }
		}

		if (!servicecenter_email1.equals("")) {
			if (!IsValidEmail(servicecenter_email1)) {
				msg = msg + "<br>Enter valid Email1!";
			} else {
				StrSql = "SELECT servicecenter_id"
						+ " FROM " + compdb(comp_id) + "axela_app_servicecenter"
						+ " WHERE 1=1"
						+ " AND (servicecenter_email1 = '" + servicecenter_email1 + "'"
						+ " OR servicecenter_email2 = '" + servicecenter_email1 + "')";
				if (status.equals("Update")) {
					StrSql += " AND servicecenter_id != " + servicecenter_id + "";
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Simillar Email 1 found!";
				}
			}
		} else {
			msg = msg + "<br>Enter valid Email1!";
		}

		if (!servicecenter_email2.equals("")) {
			if (!IsValidEmail(servicecenter_email2)) {
				msg = msg + "<br>Enter valid Email2!";
			} else {
				StrSql = "SELECT servicecenter_id"
						+ " FROM " + compdb(comp_id) + "axela_app_servicecenter"
						+ " WHERE 1=1"
						+ " AND (servicecenter_email1 = '" + servicecenter_email2 + "'"
						+ " OR servicecenter_email2 = '" + servicecenter_email2 + "')";
				if (status.equals("Update")) {
					StrSql += " AND servicecenter_id != " + servicecenter_id + "";
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Simillar Email 2 found!";
				} else if (!servicecenter_email1.equals("") && servicecenter_email2.equals(servicecenter_email1)) {
					msg += "<br>Email2 is same as Email1!";
				}
			}
		}
		if (servicecenter_website1.equals("")) {
			msg = msg + "<br>Enter Website1!";
		} else if (!servicecenter_website1.equals("")) {
			servicecenter_website1 = WebValidate(servicecenter_website1);
		}

		if (!servicecenter_website2.equals("")) {
			servicecenter_website2 = WebValidate(servicecenter_website2);
		}

		if (!servicecenter_website1.equals("") && !servicecenter_website2.equals("") && servicecenter_website2.equals(servicecenter_website1)) {
			msg += "<br>Website 2 is same as Website 1!";
		}

		if (servicecenter_address.equals("")) {
			msg = msg + "<br>Enter Address!";
		} else if (servicecenter_address.length() > 255) {
			servicecenter_address = servicecenter_address.substring(0, 254);
		}
		if (servicecenter_city_id.equals("0")) {
			msg = msg + "<br>Select City!";
		}
		if (servicecenter_pin.equals("")) {
			msg = msg + "<br>Enter Pin Code!";
		} else if (!servicecenter_pin.equals("") && !isNumeric(servicecenter_pin)) {
			msg = msg + "<br>Pin Code: Enter Numeric!";
		}
		if (servicecenter_latitude.equals("")) {
			msg = msg + "<br>Enter Latitude!";
		} else if (!servicecenter_latitude.equals("") && !isFloat(servicecenter_latitude)) {
			msg = msg + "<br>Latitude: Enter Numeric!";
		}
		if (servicecenter_longitude.equals("")) {
			msg = msg + "<br>Enter Longitude!";
		} else if (!servicecenter_longitude.equals("") && !isFloat(servicecenter_longitude)) {
			msg = msg + "<br>Longitude: Enter Numeric!";
		}
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_servicecenter"
						+ "("
						+ " servicecenter_name,"
						+ " servicecenter_mobile1,"
						+ " servicecenter_mobile2,"
						+ " servicecenter_phone1,"
						+ " servicecenter_phone2,"
						+ " servicecenter_email1,"
						+ " servicecenter_email2,"
						+ " servicecenter_website1,"
						+ " servicecenter_website2,"
						+ " servicecenter_address,"
						+ " servicecenter_city_id,"
						+ " servicecenter_pin,"
						+ " servicecenter_latitude,"
						+ " servicecenter_longitude,"
						+ " servicecenter_active,"
						+ " servicecenter_entry_id,"
						+ " servicecenter_entry_date)"
						+ " VALUES"
						+ " ("
						+ " '" + servicecenter_name + "',"
						+ " '" + servicecenter_mobile1 + "',"
						+ " '" + servicecenter_mobile2 + "',"
						+ " '" + servicecenter_phone1 + "',"
						+ " '" + servicecenter_phone2 + "',"
						+ " '" + servicecenter_email1 + "',"
						+ " '" + servicecenter_email2 + "',"
						+ " '" + servicecenter_website1 + "',"
						+ " '" + servicecenter_website2 + "',"
						+ " '" + servicecenter_address + "',"
						+ " " + servicecenter_city_id + ","
						+ " '" + servicecenter_pin + "',"
						+ " '" + servicecenter_latitude + "',"
						+ " '" + servicecenter_longitude + "',"
						+ " '" + servicecenter_active + "',"
						+ " " + servicecenter_entry_id + ","
						+ " '" + servicecenter_entry_date
						+ "')";
				// SOP("StrSql===="+StrSql);
				servicecenter_id = UpdateQueryReturnID(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT servicecenter_id,servicecenter_mobile1,servicecenter_mobile2,"
					+ " servicecenter_phone1, servicecenter_phone2,"
					+ " servicecenter_email1, servicecenter_email2,"
					+ " servicecenter_website1, servicecenter_website2,"
					+ " servicecenter_name, servicecenter_address, servicecenter_city_id,"
					+ " servicecenter_pin, servicecenter_latitude, servicecenter_longitude, servicecenter_active,"
					+ " servicecenter_entry_id, servicecenter_entry_date, servicecenter_modified_id, servicecenter_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_app_servicecenter"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = servicecenter_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state on state_id = city_state_id"
					+ " WHERE servicecenter_id = ?";
			// SOP("PopField -------------" + StrSql);
			map.put(1, servicecenter_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					servicecenter_id = crs.getString("servicecenter_id");
					servicecenter_name = crs.getString("servicecenter_name");
					servicecenter_mobile1 = crs.getString("servicecenter_mobile1");
					servicecenter_mobile2 = crs.getString("servicecenter_mobile2");
					servicecenter_phone1 = crs.getString("servicecenter_phone1");
					servicecenter_phone2 = crs.getString("servicecenter_phone2");
					servicecenter_email1 = crs.getString("servicecenter_email1");
					servicecenter_email2 = crs.getString("servicecenter_email2");
					servicecenter_website1 = crs.getString("servicecenter_website1");
					servicecenter_website2 = crs.getString("servicecenter_website2");
					servicecenter_address = crs.getString("servicecenter_address");
					servicecenter_city_id = crs.getString("servicecenter_city_id");
					servicecenter_pin = crs.getString("servicecenter_pin");
					servicecenter_latitude = crs.getString("servicecenter_latitude");
					servicecenter_longitude = crs.getString("servicecenter_longitude");
					servicecenter_active = crs.getString("servicecenter_active");
					servicecenter_entry_id = crs.getString("servicecenter_entry_id");
					entry_by = Exename(comp_id, crs.getInt("servicecenter_entry_id"));
					entry_date = strToLongDate(crs.getString("servicecenter_entry_date"));
					servicecenter_modified_id = crs.getString("servicecenter_modified_id");
					if (!servicecenter_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(servicecenter_modified_id));
						modified_date = strToLongDate(crs.getString("servicecenter_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Service Centre!"));
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_app_servicecenter"
						+ " SET"
						+ " servicecenter_name = '" + servicecenter_name + "',"
						+ " servicecenter_mobile1 = '" + servicecenter_mobile1 + "',"
						+ " servicecenter_mobile2 = '" + servicecenter_mobile2 + "',"
						+ " servicecenter_phone1 = '" + servicecenter_phone1 + "',"
						+ " servicecenter_phone2 = '" + servicecenter_phone2 + "',"
						+ " servicecenter_email1 = '" + servicecenter_email1 + "',"
						+ " servicecenter_email2 = '" + servicecenter_email2 + "',"
						+ " servicecenter_website1 = '" + servicecenter_website1 + "',"
						+ " servicecenter_website2 = '" + servicecenter_website2 + "',"
						+ " servicecenter_address = '" + servicecenter_address + "',"
						+ " servicecenter_city_id = " + servicecenter_city_id + ","
						+ " servicecenter_pin = '" + servicecenter_pin + "',"
						+ " servicecenter_latitude = '" + servicecenter_latitude + "',"
						+ " servicecenter_longitude = '" + servicecenter_longitude + "',"
						+ " servicecenter_active = '" + servicecenter_active + "',"
						+ " servicecenter_modified_id = " + servicecenter_modified_id + ","
						+ " servicecenter_modified_date = '" + servicecenter_modified_date + "'"
						+ " WHERE servicecenter_id = " + servicecenter_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields(HttpServletResponse response) {

		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_app_servicecenter"
					+ " WHERE servicecenter_id = " + servicecenter_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
