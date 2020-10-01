package axela.app;
//@Shilpashree 03 oct 2015

import java.io.IOException;
//import cloudify.connect.Connect_Demo;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import axela.portal.City_Check;
import cloudify.connect.Connect;

public class ShowRoom_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "0";
	public String showroom_id = "0";
	public String showroom_name = "";
	public String showroom_address = "";
	public String showroom_city_id = "0";
	public String showroom_pin = "";
	public String showroom_latitude = "";
	public String showroom_longitude = "";
	public String emp_role_id = "0";
	public String showroom_active = "0";
	public String showroom_entry_id = "0", showroom_entry_date = "";
	public String showroom_modified_id = "0", showroom_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String QueryString = "";
	public String showroom_mobile1 = "";
	public String showroom_mobile2 = "";
	public String showroom_phone1 = "";
	public String showroom_phone2 = "";
	public String showroom_email1 = "";
	public String showroom_email2 = "";
	public String showroom_website1 = "", showroom_website2 = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public City_Check citycheck = new City_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(PadQuotes(GetSession("emp_id", request) + ""));
				emp_role_id = CNumeric(PadQuotes(GetSession("emp_role_id", request) + ""));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				showroom_id = CNumeric(PadQuotes(request.getParameter("showroom_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}
				if ("yes".equals(add)) {
					if (!("yes").equals(addB)) {
						showroom_active = "1";
					} else {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							showroom_entry_id = emp_id;
							showroom_entry_date = ToLongDate(kknow());
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("showroom-list.jsp?showroom_id=" + showroom_id + "&msg=Showroom added successfully!"));
								status = "";
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if ("yes".equals(update)) {
					if (!("yes").equals(updateB) && !("Delete Showroom").equals(deleteB)) {
						PopulateFields(response);
					} else if (("yes").equals(updateB) && !("Delete Showroom").equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							showroom_modified_id = emp_id;
							showroom_modified_date = ToLongDate(kknow());
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Showroom updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("showroom-list.jsp?showroom_id=" + showroom_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (("Delete Showroom").equals(deleteB)) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("showroom-list.jsp?msg=Showroom deleted successfully!"));
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
		showroom_name = PadQuotes(request.getParameter("txt_showroom_name"));
		showroom_mobile1 = PadQuotes(request.getParameter("txt_showroom_mobile1"));
		showroom_mobile2 = PadQuotes(request.getParameter("txt_showroom_mobile2"));
		showroom_phone1 = PadQuotes(request.getParameter("txt_showroom_phone1"));
		showroom_phone2 = PadQuotes(request.getParameter("txt_showroom_phone2"));
		showroom_email1 = PadQuotes(request.getParameter("txt_showroom_email1"));
		showroom_email2 = PadQuotes(request.getParameter("txt_showroom_email2"));
		showroom_website1 = PadQuotes(request.getParameter("txt_showroom_website1"));
		showroom_website2 = PadQuotes(request.getParameter("txt_showroom_website2"));
		showroom_address = PadQuotes(request.getParameter("txt_showroom_address"));
		if (showroom_address.length() > 255) {
			showroom_address = showroom_address.substring(0, 254);
		}
		showroom_city_id = PadQuotes(request.getParameter("maincity"));
		showroom_pin = PadQuotes(request.getParameter("txt_showroom_pin"));
		showroom_latitude = PadQuotes(request.getParameter("txt_showroom_latitude"));
		showroom_longitude = PadQuotes(request.getParameter("txt_showroom_longitude"));
		showroom_active = CheckBoxValue(request.getParameter("chk_showroom_active"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (showroom_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		} else {
			showroom_name = toTitleCase(showroom_name);
		}
		if (!showroom_phone1.equals("")) {
			if (!IsValidPhoneNo(showroom_phone1)) {
				msg = msg + "<br>Enter valid Phone1!";
			}
			// else {
			// StrSql = "SELECT showroom_id"
			// + " FROM " + compdb(comp_id) + "axela_app_showroom"
			// + " WHERE 1=1"
			// + " AND (showroom_phone1 = '" + showroom_phone1 + "'"
			// + " OR showroom_phone2 = '" + showroom_phone1 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND showroom_id != " + showroom_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Phone 1 found!";
			// }
			// }
		} else {
			msg = msg + "<br>Enter Phone 1!";
		}

		if (!showroom_phone2.equals("")) {
			if (!IsValidPhoneNo(showroom_phone2)) {
				msg = msg + "<br>Enter valid Phone2!";
			}
			// else {
			// StrSql = "SELECT showroom_id"
			// + " FROM " + compdb(comp_id) + "axela_app_showroom"
			// + " WHERE 1=1"
			// + " AND (showroom_phone1 = '" + showroom_phone2 + "'"
			// + " OR showroom_phone2 = '" + showroom_phone2 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND showroom_id != " + showroom_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Phone 2 found!";
			// } else if (!showroom_phone1.equals("") && showroom_phone2.equals(showroom_phone1)) {
			// msg += "<br>Phone2 is same as Phone1!";
			// }
			// }
		}

		// mobile
		if (!showroom_mobile1.equals("")) {
			if (!IsValidMobileNo(showroom_mobile1)) {
				msg = msg + "<br>Enter valid Mobile1!";
			}
			// else {
			// StrSql = "SELECT showroom_id"
			// + " FROM " + compdb(comp_id) + "axela_app_showroom"
			// + " WHERE 1=1"
			// + " AND (showroom_mobile1 = '" + showroom_mobile1 + "'"
			// + " OR showroom_mobile2 = '" + showroom_mobile1 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND showroom_id != " + showroom_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Mobile 1 found!";
			// }
			// }
		} else {
			msg = msg + "<br>Enter Mobile1!";
		}

		if (!showroom_mobile2.equals("")) {
			if (!IsValidMobileNo(showroom_mobile2)) {
				msg = msg + "<br>Enter valid Mobile2!";
			}
			// else {
			// StrSql = "SELECT showroom_id"
			// + " FROM " + compdb(comp_id) + "axela_app_showroom"
			// + " WHERE 1=1"
			// + " AND (showroom_mobile1 = '" + showroom_mobile2 + "'"
			// + " OR showroom_mobile2 = '" + showroom_mobile2 + "')";
			// if (status.equals("Update")) {
			// StrSql += " AND showroom_id != " + showroom_id + "";
			// }
			// if (!ExecuteQuery(StrSql).equals("")) {
			// msg += "<br>Simillar Mobile 2 found!";
			// } else if (!showroom_mobile1.equals("") && showroom_mobile2.equals(showroom_mobile1)) {
			// msg += "<br>Mobile2 is same as Mobile1!";
			// }
			// }
		}

		if (!showroom_email1.equals("")) {
			if (!IsValidEmail(showroom_email1)) {
				msg = msg + "<br>Enter valid Email1!";
			} else {
				StrSql = "SELECT showroom_id"
						+ " FROM " + compdb(comp_id) + "axela_app_showroom"
						+ " WHERE 1=1"
						+ " AND (showroom_email1 = '" + showroom_email1 + "'"
						+ " OR showroom_email2 = '" + showroom_email1 + "')";
				if (status.equals("Update")) {
					StrSql += " AND showroom_id != " + showroom_id + "";
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Simillar Email 1 found!";
				}
			}
		} else {
			msg = msg + "<br>Enter valid Email1!";
		}

		if (!showroom_email2.equals("")) {
			if (!IsValidEmail(showroom_email2)) {
				msg = msg + "<br>Enter valid Email2!";
			} else {
				StrSql = "SELECT showroom_id"
						+ " FROM " + compdb(comp_id) + "axela_app_showroom"
						+ " WHERE 1=1"
						+ " AND (showroom_email1 = '" + showroom_email2 + "'"
						+ " OR showroom_email2 = '" + showroom_email2 + "')";
				if (status.equals("Update")) {
					StrSql += " AND showroom_id != " + showroom_id + "";
				}
				if (!ExecuteQuery(StrSql).equals("")) {
					msg += "<br>Simillar Email 2 found!";
				} else if (!showroom_email1.equals("") && showroom_email2.equals(showroom_email1)) {
					msg += "<br>Email2 is same as Email1!";
				}
			}
		}

		if (showroom_website1.equals("")) {
			msg = msg + "<br>Enter Website1!";
		} else if (!showroom_website1.equals("")) {
			showroom_website1 = WebValidate(showroom_website1);
		}

		if (!showroom_website2.equals("")) {
			showroom_website2 = WebValidate(showroom_website2);
		}

		if (!showroom_website1.equals("") && !showroom_website2.equals("") && showroom_website2.equals(showroom_website1)) {
			msg += "<br>Website 2 is same as Website 1!";
		}

		if (showroom_address.equals("")) {
			msg = msg + "<br>Enter Address!";
		} else if (showroom_address.length() > 255) {
			showroom_address = showroom_address.substring(0, 254);
		}
		if (showroom_city_id.equals("0")) {
			msg = msg + "<br>Select City!";
		}
		if (showroom_pin.equals("")) {
			msg = msg + "<br>Enter Pin Code!";
		} else if (!showroom_pin.equals("") && !isNumeric(showroom_pin)) {
			msg = msg + "<br>Pin Code: Enter Numeric!";
		}
		if (showroom_latitude.equals("")) {
			msg = msg + "<br>Enter Latitude!";
		} else if (!showroom_latitude.equals("") && !isFloat(showroom_latitude)) {
			msg = msg + "<br>Latitude: Enter Numeric!";
		}
		if (showroom_longitude.equals("")) {
			msg = msg + "<br>Enter Longitude!";
		} else if (!showroom_longitude.equals("") && !isFloat(showroom_longitude)) {
			msg = msg + "<br>Longitude: Enter Numeric!";
		}

	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_showroom"
						+ "("
						+ " showroom_name,"
						+ " showroom_mobile1,"
						+ " showroom_mobile2,"
						+ " showroom_phone1,"
						+ " showroom_phone2,"
						+ " showroom_email1,"
						+ " showroom_email2,"
						+ " showroom_website1,"
						+ " showroom_website2,"
						+ " showroom_address,"
						+ " showroom_city_id,"
						+ " showroom_pin,"
						+ " showroom_latitude,"
						+ " showroom_longitude,"
						+ " showroom_active,"
						+ " showroom_entry_id,"
						+ " showroom_entry_date)"
						+ " VALUES"
						+ " ("
						+ " '" + showroom_name + "',"
						+ " '" + showroom_mobile1 + "',"
						+ " '" + showroom_mobile2 + "',"
						+ " '" + showroom_phone1 + "',"
						+ " '" + showroom_phone2 + "',"
						+ " '" + showroom_email1 + "',"
						+ " '" + showroom_email2 + "',"
						+ " '" + showroom_website1 + "',"
						+ " '" + showroom_website2 + "',"
						+ " '" + showroom_address + "',"
						+ " " + showroom_city_id + ","
						+ " '" + showroom_pin + "',"
						+ " '" + showroom_latitude + "',"
						+ " '" + showroom_longitude + "',"
						+ " '" + showroom_active + "',"
						+ " " + showroom_entry_id + ","
						+ " '" + showroom_entry_date
						+ "')";
				// SOP("StrSql===="+StrSql);
				showroom_id = UpdateQueryReturnID(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT showroom_id,showroom_mobile1,showroom_mobile2,"
					+ " showroom_phone1, showroom_phone2,"
					+ " showroom_email1, showroom_email2,"
					+ " showroom_website1, showroom_website2,"
					+ " showroom_name, showroom_address, showroom_city_id,"
					+ " showroom_pin, showroom_latitude, showroom_longitude, showroom_active,"
					+ " showroom_entry_id, showroom_entry_date, showroom_modified_id, showroom_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_app_showroom"
					+ " INNER JOIN " + compdb(comp_id) + "axela_city ON city_id = showroom_city_id"
					+ " INNER JOIN " + compdb(comp_id) + "axela_state ON state_id = city_state_id"
					+ " WHERE showroom_id = ?";
			// SOP("PopField A/C SQL-----" + StrSql);
			map.put(1, showroom_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					showroom_id = crs.getString("showroom_id");
					showroom_name = crs.getString("showroom_name");
					showroom_mobile1 = crs.getString("showroom_mobile1");
					showroom_mobile2 = crs.getString("showroom_mobile2");
					showroom_phone1 = crs.getString("showroom_phone1");
					showroom_phone2 = crs.getString("showroom_phone2");
					showroom_email1 = crs.getString("showroom_email1");
					showroom_email2 = crs.getString("showroom_email2");
					showroom_website1 = crs.getString("showroom_website1");
					showroom_website2 = crs.getString("showroom_website2");
					showroom_address = crs.getString("showroom_address");
					showroom_city_id = crs.getString("showroom_city_id");
					showroom_pin = crs.getString("showroom_pin");
					showroom_latitude = crs.getString("showroom_latitude");
					showroom_longitude = crs.getString("showroom_longitude");
					showroom_active = crs.getString("showroom_active");
					showroom_entry_id = crs.getString("showroom_entry_id");
					entry_by = Exename(comp_id, crs.getInt("showroom_entry_id"));
					entry_date = strToLongDate(crs.getString("showroom_entry_date"));
					showroom_modified_id = crs.getString("showroom_modified_id");
					if (!showroom_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(showroom_modified_id));
						modified_date = strToLongDate(crs.getString("showroom_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Showroom!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_app_showroom"
						+ " SET"
						+ " showroom_name = '" + showroom_name + "',"
						+ " showroom_mobile1 = '" + showroom_mobile1 + "',"
						+ " showroom_mobile2 = '" + showroom_mobile2 + "',"
						+ " showroom_phone1 = '" + showroom_phone1 + "',"
						+ " showroom_phone2 = '" + showroom_phone2 + "',"
						+ " showroom_email1 = '" + showroom_email1 + "',"
						+ " showroom_email2 = '" + showroom_email2 + "',"
						+ " showroom_website1 = '" + showroom_website1 + "',"
						+ " showroom_website2 = '" + showroom_website2 + "',"
						+ " showroom_address = '" + showroom_address + "',"
						+ " showroom_city_id = " + showroom_city_id + ","
						+ " showroom_pin = '" + showroom_pin + "',"
						+ " showroom_latitude = '" + showroom_latitude + "',"
						+ " showroom_longitude = '" + showroom_longitude + "',"
						+ " showroom_active = '" + showroom_active + "',"
						+ " showroom_modified_id = " + showroom_modified_id + ","
						+ " showroom_modified_date = '" + showroom_modified_date + "'"
						+ " WHERE showroom_id = " + showroom_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields(HttpServletResponse response) {

		try {
			StrSql = "DELETE FROM " + compdb(comp_id) + "axela_app_showroom"
					+ " WHERE showroom_id = " + showroom_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
