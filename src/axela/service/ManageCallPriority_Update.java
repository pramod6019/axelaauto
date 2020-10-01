package axela.service;

/**
 * @author Dilip Kumar P 04 APR 2013
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageCallPriority_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String prioritycall_id = "0";
	public String prioritycall_name = "";
	public String prioritycall_desc = "";
	public String prioritycall_duehrs = "";
	public String prioritycall_rank = "";
	public String prioritycall_trigger1_hrs = "0";
	public String prioritycall_trigger2_hrs = "0";
	public String prioritycall_trigger3_hrs = "0";
	public String prioritycall_trigger4_hrs = "0";
	public String prioritycall_trigger5_hrs = "0";
	public String prioritycall_entry_id = "0";
	public String prioritycall_entry_date = "";
	public String prioritycall_modified_id = "0";
	public String prioritycall_modified_date = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";
	public String QueryString = "";

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
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						prioritycall_name = "";
						prioritycall_desc = "";
						prioritycall_trigger1_hrs = "";
						prioritycall_trigger2_hrs = "";
						prioritycall_trigger3_hrs = "";
						prioritycall_trigger4_hrs = "";
						prioritycall_trigger5_hrs = "";
						prioritycall_duehrs = "";
					} else {
						GetValues(request, response);
						prioritycall_entry_id = emp_id;
						prioritycall_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecallpriority.jsp?msg=Priority Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Call Priority".equals(deleteB)) {
						prioritycall_id = CNumeric(PadQuotes(request.getParameter("prioritycall_id")));
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Call Priority".equals(deleteB)) {
						GetValues(request, response);
						prioritycall_modified_id = emp_id;
						prioritycall_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecallpriority.jsp?msg= Priority Updated Successfully!"));
						}
					}
					if ("Delete Call Priority".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managecallpriority.jsp?msg=Priority Deleted Successfully!"));
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
		prioritycall_id = CNumeric(PadQuotes(request.getParameter("prioritycall_id")));
		prioritycall_name = PadQuotes(request.getParameter("txt_prioritycall_name"));
		prioritycall_desc = PadQuotes(request.getParameter("txt_prioritycall_desc"));
		prioritycall_duehrs = PadQuotes(request.getParameter("txt_prioritycall_duehrs"));
		prioritycall_trigger1_hrs = PadQuotes(request.getParameter("txt_prioritycall_trigger1_hrs"));
		prioritycall_trigger2_hrs = PadQuotes(request.getParameter("txt_prioritycall_trigger2_hrs"));
		prioritycall_trigger3_hrs = PadQuotes(request.getParameter("txt_prioritycall_trigger3_hrs"));
		prioritycall_trigger4_hrs = PadQuotes(request.getParameter("txt_prioritycall_trigger4_hrs"));
		prioritycall_trigger5_hrs = PadQuotes(request.getParameter("txt_prioritycall_trigger5_hrs"));
		if (prioritycall_trigger1_hrs.equals("")) {
			prioritycall_trigger1_hrs = "0";
		}
		if (prioritycall_trigger2_hrs.equals("")) {
			prioritycall_trigger2_hrs = "0";
		}
		if (prioritycall_trigger3_hrs.equals("")) {
			prioritycall_trigger3_hrs = "0";
		}
		if (prioritycall_trigger4_hrs.equals("")) {
			prioritycall_trigger4_hrs = "0";
		}
		if (prioritycall_trigger5_hrs.equals("")) {
			prioritycall_trigger5_hrs = "0";
		}
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (prioritycall_name.equals("")) {
			msg = "<br>Enter Priority!";
		} else {
			try {
				if (!prioritycall_name.equals("")) {
					StrSql = "Select prioritycall_name from " + compdb(comp_id) + "axela_service_call_priority where prioritycall_name = '" + prioritycall_name + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " and prioritycall_id != " + prioritycall_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Priority Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (prioritycall_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (prioritycall_duehrs.equals("")) {
			msg = msg + "<br>Enter Due Hours!";
		} else if (!isNumeric(prioritycall_duehrs)) {
			msg = msg + "<br>Due Hours must be Numeric!";
		}
		if ((!prioritycall_trigger5_hrs.equals("0"))) {
			if (Double.parseDouble(prioritycall_trigger5_hrs) < Double.parseDouble(prioritycall_trigger4_hrs)
					|| Double.parseDouble(prioritycall_trigger5_hrs) < Double.parseDouble(prioritycall_trigger3_hrs)
					|| Double.parseDouble(prioritycall_trigger5_hrs) < Double.parseDouble(prioritycall_trigger2_hrs)
					|| Double.parseDouble(prioritycall_trigger5_hrs) < Double.parseDouble(prioritycall_trigger1_hrs)) {
				msg = msg + "<br> Level-5 hours should be more than previous Levels hours";
			}
		}
		if (!prioritycall_trigger4_hrs.equals("0")) {
			if (Double.parseDouble(prioritycall_trigger4_hrs) < Double.parseDouble(prioritycall_trigger3_hrs)
					|| Double.parseDouble(prioritycall_trigger4_hrs) < Double.parseDouble(prioritycall_trigger2_hrs)
					|| Double.parseDouble(prioritycall_trigger4_hrs) < Double.parseDouble(prioritycall_trigger1_hrs)) {
				msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
			}
		}
		if (!prioritycall_trigger3_hrs.equals("0")) {
			if (Double.parseDouble(prioritycall_trigger3_hrs) < Double.parseDouble(prioritycall_trigger2_hrs)
					|| Double.parseDouble(prioritycall_trigger3_hrs) < Double.parseDouble(prioritycall_trigger1_hrs)) {
				msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
			}
		}
		if (!prioritycall_trigger2_hrs.equals("0")) {
			if (Double.parseDouble(prioritycall_trigger2_hrs) < Double.parseDouble(prioritycall_trigger1_hrs)) {
				msg = msg + "<br> Level-2 hours should be more than previous Level hours";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				prioritycall_id = ExecuteQuery("SELECT (COALESCE(MAX(prioritycall_id),0)+1)"
						+ " FROM " + compdb(comp_id) + "axela_service_call_priority");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_call_priority"
						+ " (prioritycall_id,"
						+ " prioritycall_name,"
						+ " prioritycall_desc,"
						+ " prioritycall_duehrs,"
						+ " prioritycall_rank,"
						+ " prioritycall_trigger1_hrs,"
						+ " prioritycall_trigger2_hrs,"
						+ " prioritycall_trigger3_hrs,"
						+ " prioritycall_trigger4_hrs,"
						+ " prioritycall_trigger5_hrs,"
						+ " prioritycall_entry_id,"
						+ " prioritycall_entry_date)"
						+ " VALUES"
						+ " (" + prioritycall_id + ","
						+ " '" + prioritycall_name + "',"
						+ " '" + prioritycall_desc + "',"
						+ " '" + prioritycall_duehrs + "',"
						+ " (SELECT (COALESCE(MAX(prioritycall_rank),0)+1)"
						+ " FROM " + compdb(comp_id) + "axela_service_call_priority AS RANK WHERE 1=1 ),"
						+ " '" + prioritycall_trigger1_hrs + "',"
						+ " '" + prioritycall_trigger2_hrs + "',"
						+ " '" + prioritycall_trigger3_hrs + "',"
						+ " '" + prioritycall_trigger4_hrs + "',"
						+ " '" + prioritycall_trigger5_hrs + "',"
						+ " " + prioritycall_entry_id + ","
						+ " '" + prioritycall_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT *"
					+ " FROM " + compdb(comp_id) + "axela_service_call_priority"
					+ " WHERE prioritycall_id = " + prioritycall_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					prioritycall_name = crs.getString("prioritycall_name");
					prioritycall_desc = crs.getString("prioritycall_desc");
					prioritycall_duehrs = crs.getString("prioritycall_duehrs");
					prioritycall_trigger1_hrs = crs.getString("prioritycall_trigger1_hrs");
					prioritycall_trigger2_hrs = crs.getString("prioritycall_trigger2_hrs");
					prioritycall_trigger3_hrs = crs.getString("prioritycall_trigger3_hrs");
					prioritycall_trigger4_hrs = crs.getString("prioritycall_trigger4_hrs");
					prioritycall_trigger5_hrs = crs.getString("prioritycall_trigger5_hrs");
					prioritycall_entry_id = crs.getString("prioritycall_entry_id");
					if (!prioritycall_entry_id.equals("0")) {
						entry_by = Exename(comp_id, Integer.parseInt(prioritycall_entry_id));
						entry_date = strToLongDate(crs.getString("prioritycall_entry_date"));
					}
					prioritycall_modified_id = crs.getString("prioritycall_modified_id");
					if (!prioritycall_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(prioritycall_modified_id));
						modified_date = strToLongDate(crs.getString("prioritycall_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Call Priority!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_service_call_priority"
						+ " SET"
						+ " prioritycall_name = '" + prioritycall_name + "',"
						+ " prioritycall_desc = '" + prioritycall_desc + "',"
						+ " prioritycall_duehrs = '" + prioritycall_duehrs + "',"
						+ " prioritycall_trigger1_hrs = '" + prioritycall_trigger1_hrs + "',"
						+ " prioritycall_trigger2_hrs = '" + prioritycall_trigger2_hrs + "',"
						+ " prioritycall_trigger3_hrs = '" + prioritycall_trigger3_hrs + "',"
						+ " prioritycall_trigger4_hrs = '" + prioritycall_trigger4_hrs + "',"
						+ " prioritycall_trigger5_hrs = '" + prioritycall_trigger5_hrs + "',"
						+ " prioritycall_modified_id = " + prioritycall_modified_id + ","
						+ " prioritycall_modified_date = '" + prioritycall_modified_date + "'"
						+ " where prioritycall_id = " + prioritycall_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (prioritycall_id.equals("1")) {
			msg = "<br>First Record cannot be Deleted ";
			return;
		}
		StrSql = "SELECT call_prioritycall_id FROM " + compdb(comp_id) + "axela_service_call where call_prioritycall_id = " + prioritycall_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Priority is Associated with Call!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_service_call_priority where prioritycall_id = " + prioritycall_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
