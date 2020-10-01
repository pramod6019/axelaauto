package axela.insurance;
//Dilip Kumar 18 APR 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageInsurFollowupPriority_Update extends Connect {

	public String update = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String priorityinsurfollowup_id = "0";
	public String priorityinsurfollowup_name = "";
	public String priorityinsurfollowup_desc = "";
	public String priorityinsurfollowup_duehrs = "";
	public String priorityinsurfollowup_rank = "";
	public String priorityinsurfollowup_trigger1_hrs = "0:00";
	public String priorityinsurfollowup_trigger2_hrs = "0:00";
	public String priorityinsurfollowup_trigger3_hrs = "0:00";
	public String priorityinsurfollowup_trigger4_hrs = "0:00";
	public String priorityinsurfollowup_trigger5_hrs = "0:00";
	public String priorityinsurfollowup_modified_id = "0";
	public String priorityinsurfollowup_modified_date = "";
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
				update = PadQuotes(request.getParameter("update"));
				updateB = PadQuotes(request.getParameter("update_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());

				if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(update)) {
					if (!"yes".equals(updateB)) {
						priorityinsurfollowup_id = CNumeric(PadQuotes(request.getParameter("priorityinsurfollowup_id")));
						PopulateFields(response);
					} else if ("yes".equals(updateB)) {
						GetValues(request, response);
						priorityinsurfollowup_modified_id = emp_id;
						priorityinsurfollowup_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("manageinsurfollowuppriority.jsp?msg=Insurance Follow-up Priority Updated Successfully!"));
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
		priorityinsurfollowup_id = CNumeric(PadQuotes(request.getParameter("priorityinsurfollowup_id")));
		priorityinsurfollowup_name = PadQuotes(request.getParameter("txt_priorityinsurfollowup_name"));
		priorityinsurfollowup_desc = PadQuotes(request.getParameter("txt_priorityinsurfollowup_desc"));
		priorityinsurfollowup_duehrs = PadQuotes(request.getParameter("txt_priorityinsurfollowup_duehrs"));
		priorityinsurfollowup_trigger1_hrs = PadQuotes(request.getParameter("txt_priorityinsurfollowup_trigger1_hrs"));
		priorityinsurfollowup_trigger2_hrs = PadQuotes(request.getParameter("txt_priorityinsurfollowup_trigger2_hrs"));
		priorityinsurfollowup_trigger3_hrs = PadQuotes(request.getParameter("txt_priorityinsurfollowup_trigger3_hrs"));
		priorityinsurfollowup_trigger4_hrs = PadQuotes(request.getParameter("txt_priorityinsurfollowup_trigger4_hrs"));
		priorityinsurfollowup_trigger5_hrs = PadQuotes(request.getParameter("txt_priorityinsurfollowup_trigger5_hrs"));
		if (priorityinsurfollowup_trigger1_hrs.equals("")) {
			priorityinsurfollowup_trigger1_hrs = "0:00";
		}
		if (priorityinsurfollowup_trigger2_hrs.equals("")) {
			priorityinsurfollowup_trigger2_hrs = "0:00";
		}
		if (priorityinsurfollowup_trigger3_hrs.equals("")) {
			priorityinsurfollowup_trigger3_hrs = "0:00";
		}
		if (priorityinsurfollowup_trigger4_hrs.equals("")) {
			priorityinsurfollowup_trigger4_hrs = "0:00";
		}
		if (priorityinsurfollowup_trigger5_hrs.equals("")) {
			priorityinsurfollowup_trigger5_hrs = "0:00";
		}
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		msg = "";
		if (priorityinsurfollowup_name.equals("")) {
			msg = "<br>Enter Priority!";
		} else {
			try {
				if (!priorityinsurfollowup_name.equals("")) {
					StrSql = "SELECT priorityinsurfollowup_name"
							+ " FROM " + compdb(comp_id) + "axela_insurance_followup_priority"
							+ " WHERE priorityinsurfollowup_name = '" + priorityinsurfollowup_name + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND priorityinsurfollowup_id!=" + priorityinsurfollowup_id + "";
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
		if (priorityinsurfollowup_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (priorityinsurfollowup_duehrs.equals("")) {
			msg = msg + "<br>Enter Due Hours!";
		}

		if (!priorityinsurfollowup_trigger5_hrs.equals("0") && !priorityinsurfollowup_trigger5_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityinsurfollowup_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger4_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityinsurfollowup_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger3_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityinsurfollowup_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityinsurfollowup_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-5 hours should be more than previous Levels hours";
			}
		}
		if (!priorityinsurfollowup_trigger4_hrs.equals("0") && !priorityinsurfollowup_trigger4_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityinsurfollowup_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger3_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityinsurfollowup_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityinsurfollowup_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-4 hours should be more than previous Levels hours";
			}
		}
		if (!priorityinsurfollowup_trigger3_hrs.equals("0") && !priorityinsurfollowup_trigger3_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityinsurfollowup_trigger3_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(priorityinsurfollowup_trigger3_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-3 hours should be more than previous Levels hours";
			}
		}
		if (!priorityinsurfollowup_trigger2_hrs.equals("0") && !priorityinsurfollowup_trigger1_hrs.equals("0:00")) {
			if (Double.parseDouble(priorityinsurfollowup_trigger2_hrs.replaceAll(":", "\\.")) < Double.parseDouble(priorityinsurfollowup_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br> Level-2 hours should be more than previous Levels hours";
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_insurance_followup_priority"
					+ " WHERE priorityinsurfollowup_id = " + priorityinsurfollowup_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					priorityinsurfollowup_name = crs.getString("priorityinsurfollowup_name");
					priorityinsurfollowup_desc = crs.getString("priorityinsurfollowup_desc");
					priorityinsurfollowup_duehrs = crs.getString("priorityinsurfollowup_duehrs");
					priorityinsurfollowup_trigger1_hrs = crs.getString("priorityinsurfollowup_trigger1_hrs");
					priorityinsurfollowup_trigger2_hrs = crs.getString("priorityinsurfollowup_trigger2_hrs");
					priorityinsurfollowup_trigger3_hrs = crs.getString("priorityinsurfollowup_trigger3_hrs");
					priorityinsurfollowup_trigger4_hrs = crs.getString("priorityinsurfollowup_trigger4_hrs");
					priorityinsurfollowup_trigger5_hrs = crs.getString("priorityinsurfollowup_trigger5_hrs");
					priorityinsurfollowup_modified_id = crs.getString("priorityinsurfollowup_modified_id");
					if (!priorityinsurfollowup_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(priorityinsurfollowup_modified_id));
						modified_date = strToLongDate(crs.getString("priorityinsurfollowup_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Insurance Follow-up Priority!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_followup_priority"
						+ " SET"
						+ " priorityinsurfollowup_name = '" + priorityinsurfollowup_name + "',"
						+ " priorityinsurfollowup_desc = '" + priorityinsurfollowup_desc + "',"
						+ " priorityinsurfollowup_duehrs = '" + priorityinsurfollowup_duehrs + "',"
						+ " priorityinsurfollowup_trigger1_hrs = '" + priorityinsurfollowup_trigger1_hrs + "',"
						+ " priorityinsurfollowup_trigger2_hrs = '" + priorityinsurfollowup_trigger2_hrs + "',"
						+ " priorityinsurfollowup_trigger3_hrs = '" + priorityinsurfollowup_trigger3_hrs + "',"
						+ " priorityinsurfollowup_trigger4_hrs = '" + priorityinsurfollowup_trigger4_hrs + "',"
						+ " priorityinsurfollowup_trigger5_hrs = '" + priorityinsurfollowup_trigger5_hrs + "',"
						+ " priorityinsurfollowup_modified_id = " + priorityinsurfollowup_modified_id + ","
						+ " priorityinsurfollowup_modified_date = '" + priorityinsurfollowup_modified_date + "'"
						+ " WHERE priorityinsurfollowup_id = " + priorityinsurfollowup_id + "";

				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
