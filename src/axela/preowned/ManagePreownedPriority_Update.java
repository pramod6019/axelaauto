package axela.preowned;
//Bhagwan Singh 28th jun 2013
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedPriority_Update extends Connect {

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
	public String prioritypreowned_id = "0";
	public String prioritypreowned_name = "";
	public String prioritypreowned_desc = "";
	public String prioritypreowned_duehrs = "";
	public String duehrs = "";
	public String prioritypreowned_rank = "";
	public String trigger1_hrs = "0";
	public String trigger2_hrs = "0";
	public String trigger3_hrs = "0";
	public String trigger4_hrs = "0";
	public String trigger5_hrs = "0";
	public String prioritypreowned_trigger1_hrs = "0:00";
	public String prioritypreowned_trigger2_hrs = "0:00";
	public String prioritypreowned_trigger3_hrs = "0:00";
	public String prioritypreowned_trigger4_hrs = "0:00";
	public String prioritypreowned_trigger5_hrs = "0:00";
	public String prioritypreowned_entry_id = "0";
	public String prioritypreowned_entry_by = "";
	public String prioritypreowned_entry_date = "";
	public String prioritypreowned_modified_id = "0";
	public String prioritypreowned_modified_by = "";
	public String prioritypreowned_modified_date = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				prioritypreowned_id = CNumeric(PadQuotes(request.getParameter("prioritypreowned_id")));
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
						prioritypreowned_name = "";
						prioritypreowned_desc = "";
						prioritypreowned_trigger1_hrs = "0";
						prioritypreowned_trigger2_hrs = "0";
						prioritypreowned_trigger3_hrs = "0";
						prioritypreowned_trigger4_hrs = "0";
						prioritypreowned_trigger5_hrs = "0";
						prioritypreowned_duehrs = "";
					} else {
						GetValues(request, response);
						prioritypreowned_entry_id = emp_id;
						prioritypreowned_entry_date = ToLongDate(kknow());
						// prioritypreowned_duehrs = prioritypreowned_duehrs.replaceAll(":", "\\.");
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedpriority.jsp?msg=Pre Owned Priority Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre Owned Priority".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre Owned Priority".equals(deleteB)) {
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedpriority.jsp?msg=Priority Updated Successfully!"));
						}
					} else if ("Delete Pre Owned Priority".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedpriority.jsp?msg=Priority Deleted Successfully!"));
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
		prioritypreowned_name = PadQuotes(request.getParameter("txt_prioritypreowned_name"));
		prioritypreowned_desc = PadQuotes(request.getParameter("txt_prioritypreowned_desc"));
		prioritypreowned_duehrs = PadQuotes(request.getParameter("txt_prioritypreowned_duehrs"));
		prioritypreowned_trigger1_hrs = PadQuotes(request.getParameter("txt_prioritypreowned_trigger1_hrs"));
		prioritypreowned_trigger2_hrs = PadQuotes(request.getParameter("txt_prioritypreowned_trigger2_hrs"));
		prioritypreowned_trigger3_hrs = PadQuotes(request.getParameter("txt_prioritypreowned_trigger3_hrs"));
		prioritypreowned_trigger4_hrs = PadQuotes(request.getParameter("txt_prioritypreowned_trigger4_hrs"));
		prioritypreowned_trigger5_hrs = PadQuotes(request.getParameter("txt_prioritypreowned_trigger5_hrs"));
		if (prioritypreowned_trigger1_hrs.equals("")) {
			prioritypreowned_trigger1_hrs = "0:00";
		}
		if (prioritypreowned_trigger2_hrs.equals("")) {
			prioritypreowned_trigger2_hrs = "0:00";
		}
		if (prioritypreowned_trigger3_hrs.equals("")) {
			prioritypreowned_trigger3_hrs = "0:00";
		}
		if (prioritypreowned_trigger4_hrs.equals("")) {
			prioritypreowned_trigger4_hrs = "0:00";
		}
		if (prioritypreowned_trigger5_hrs.equals("")) {
			prioritypreowned_trigger5_hrs = "0:00";
		}
		duehrs = prioritypreowned_duehrs;
		trigger1_hrs = prioritypreowned_trigger1_hrs;
		trigger2_hrs = prioritypreowned_trigger2_hrs;
		trigger3_hrs = prioritypreowned_trigger3_hrs;
		trigger4_hrs = prioritypreowned_trigger4_hrs;
		trigger5_hrs = prioritypreowned_trigger5_hrs;
		prioritypreowned_entry_by = PadQuotes(request.getParameter("entry_by"));
		prioritypreowned_entry_date = PadQuotes(request.getParameter("entry_date"));
		prioritypreowned_modified_by = PadQuotes(request.getParameter("modified_by"));
		prioritypreowned_modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {

		msg = "";
		if (prioritypreowned_name.equals("")) {
			msg = "<br>Enter Priority!";
		} else {
			try {
				if (!prioritypreowned_name.equals("")) {
					StrSql = "SELECT prioritypreowned_name"
							+ " FROM " + compdb(comp_id) + "axela_preowned_priority"
							+ " WHERE prioritypreowned_name = '" + prioritypreowned_name + "'";
					if (update.equals("yes")) {
						StrSql = StrSql + " AND prioritypreowned_id != " + prioritypreowned_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Priority Found!";
					}
					crs.close();
				}
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
		if (prioritypreowned_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		if (prioritypreowned_duehrs.equals("")) {
			msg = msg + "<br>Enter Due Hours!";
		}

		if (!prioritypreowned_trigger5_hrs.equals("0") && !prioritypreowned_trigger5_hrs.equals("0:00")) {
			if (Double.parseDouble(prioritypreowned_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger4_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(prioritypreowned_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger3_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(prioritypreowned_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(prioritypreowned_trigger5_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br>Level-5 hours should be more than previous Levels hours!";
			}
		}
		if (!prioritypreowned_trigger4_hrs.equals("0") && !prioritypreowned_trigger4_hrs.equals("0:00")) {
			if (Double.parseDouble(prioritypreowned_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger3_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(prioritypreowned_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(prioritypreowned_trigger4_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br>Level-4 hours should be more than previous Levels hours!";
			}
		}
		if (!prioritypreowned_trigger3_hrs.equals("0") && !prioritypreowned_trigger3_hrs.equals("0:00")) {
			if (Double.parseDouble(prioritypreowned_trigger3_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger2_hrs.replaceAll(":", "\\."))
					|| Double.parseDouble(prioritypreowned_trigger3_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br>Level-3 hours should be more than previous Levels hours!";
			}
		}
		if (!prioritypreowned_trigger2_hrs.equals("0") && !prioritypreowned_trigger2_hrs.equals("0:00")) {
			if (Double.parseDouble(prioritypreowned_trigger2_hrs.replaceAll(":", "\\.")) < Double.parseDouble(prioritypreowned_trigger1_hrs.replaceAll(":", "\\."))) {
				msg = msg + "<br>Level-2 hours should be more than previous Level hours!";
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				prioritypreowned_id = ExecuteQuery("SELECT (COALESCE(MAX(prioritypreowned_id), '0')+1) FROM " + compdb(comp_id) + "axela_preowned_priority");
				StrSql = "INSERT INTO " + compdb(comp_id) + "axela_preowned_priority"
						+ " (prioritypreowned_id,"
						+ " prioritypreowned_name,"
						+ " prioritypreowned_desc,"
						+ " prioritypreowned_duehrs,"
						+ " prioritypreowned_rank,"
						+ " prioritypreowned_trigger1_hrs,"
						+ " prioritypreowned_trigger2_hrs,"
						+ " prioritypreowned_trigger3_hrs,"
						+ " prioritypreowned_trigger4_hrs,"
						+ " prioritypreowned_trigger5_hrs,"
						+ " prioritypreowned_entry_id,"
						+ " prioritypreowned_entry_date)"
						+ " VALUES"
						+ " (" + prioritypreowned_id + ","
						+ " '" + prioritypreowned_name + "',"
						+ " '" + prioritypreowned_desc + "',"
						+ " '" + duehrs + "',"
						+ " (SELECT (COALESCE(MAX(prioritypreowned_rank), '0')+1) FROM " + compdb(comp_id) + "axela_preowned_priority AS Rank WHERE 1 = 1 ),"
						+ " '" + trigger1_hrs + "',"
						+ " '" + trigger2_hrs + "',"
						+ " '" + trigger3_hrs + "',"
						+ " '" + trigger4_hrs + "',"
						+ " '" + trigger5_hrs + "',"
						+ " " + prioritypreowned_entry_id + ","
						+ " '" + prioritypreowned_entry_date + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = " SELECT prioritypreowned_name, prioritypreowned_desc, prioritypreowned_duehrs, prioritypreowned_trigger1_hrs,"
					+ " prioritypreowned_trigger2_hrs, prioritypreowned_trigger3_hrs, prioritypreowned_trigger4_hrs, prioritypreowned_trigger5_hrs,"
					+ " prioritypreowned_entry_id, prioritypreowned_modified_id, prioritypreowned_entry_date, prioritypreowned_modified_date"
					+ " FROM " + compdb(comp_id) + "axela_preowned_priority"
					+ " WHERE prioritypreowned_id = " + prioritypreowned_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					prioritypreowned_name = crs.getString("prioritypreowned_name");
					prioritypreowned_desc = crs.getString("prioritypreowned_desc");
					prioritypreowned_duehrs = crs.getString("prioritypreowned_duehrs");
					// if (!prioritypreowned_duehrs.contains(".")) {
					// prioritypreowned_duehrs = prioritypreowned_duehrs + ".00";
					// }
					//
					// String prioritypreowned1_duehrs[] = prioritypreowned_duehrs.split("\\.");
					// if (prioritypreowned1_duehrs[0].length() < 2) {
					// prioritypreowned1_duehrs[0] = "0" + prioritypreowned1_duehrs[0];
					// }
					// if (prioritypreowned1_duehrs[1].length() < 2) {
					// prioritypreowned1_duehrs[1] = prioritypreowned1_duehrs[1] + "0";
					// }
					// prioritypreowned_duehrs = prioritypreowned1_duehrs[0] + ":" + prioritypreowned1_duehrs[1];

					prioritypreowned_trigger1_hrs = crs.getString("prioritypreowned_trigger1_hrs");
					// if (!prioritypreowned_trigger1_hrs.contains(".")) {
					// prioritypreowned_trigger1_hrs = prioritypreowned_trigger1_hrs + ".00";
					// }
					// String prioritypreowned1_trigger1_hrs[] = prioritypreowned_trigger1_hrs.split("\\.");
					// if (prioritypreowned1_trigger1_hrs[0].length() < 2) {
					// prioritypreowned1_trigger1_hrs[0] = "0" + prioritypreowned1_trigger1_hrs[0];
					// }
					// if (prioritypreowned1_trigger1_hrs[1].length() < 2) {
					// prioritypreowned1_trigger1_hrs[1] = prioritypreowned1_trigger1_hrs[1] + "0";
					// }
					// prioritypreowned_trigger1_hrs = prioritypreowned1_trigger1_hrs[0] + ":" + prioritypreowned1_trigger1_hrs[1];

					prioritypreowned_trigger2_hrs = crs.getString("prioritypreowned_trigger2_hrs");
					// if (!prioritypreowned_trigger2_hrs.contains(".")) {
					// prioritypreowned_trigger2_hrs = prioritypreowned_trigger2_hrs + ".00";
					// }
					// String prioritypreowned1_trigger2_hrs[] = prioritypreowned_trigger2_hrs.split("\\.");
					// if (prioritypreowned1_trigger2_hrs[0].length() < 2) {
					// prioritypreowned1_trigger2_hrs[0] = "0" + prioritypreowned1_trigger2_hrs[0];
					// }
					// if (prioritypreowned1_trigger2_hrs[1].length() < 2) {
					// prioritypreowned1_trigger2_hrs[1] = prioritypreowned1_trigger2_hrs[1] + "0";
					// }
					// prioritypreowned_trigger2_hrs = prioritypreowned1_trigger2_hrs[0] + ":" + prioritypreowned1_trigger2_hrs[1];

					prioritypreowned_trigger3_hrs = crs.getString("prioritypreowned_trigger3_hrs");
					// if (!prioritypreowned_trigger3_hrs.contains(".")) {
					// prioritypreowned_trigger3_hrs = prioritypreowned_trigger3_hrs + ".00";
					// }
					// String prioritypreowned1_trigger3_hrs[] = prioritypreowned_trigger3_hrs.split("\\.");
					// if (prioritypreowned1_trigger3_hrs[0].length() < 2) {
					// prioritypreowned1_trigger3_hrs[0] = "0" + prioritypreowned1_trigger3_hrs[0];
					// }
					// if (prioritypreowned1_trigger3_hrs[1].length() < 2) {
					// prioritypreowned1_trigger3_hrs[1] = prioritypreowned1_trigger3_hrs[1] + "0";
					// }
					// prioritypreowned_trigger3_hrs = prioritypreowned1_trigger3_hrs[0] + ":" + prioritypreowned1_trigger3_hrs[1];

					prioritypreowned_trigger4_hrs = crs.getString("prioritypreowned_trigger4_hrs");
					// if (!prioritypreowned_trigger4_hrs.contains(".")) {
					// prioritypreowned_trigger4_hrs = prioritypreowned_trigger4_hrs + ".00";
					// }
					// String prioritypreowned1_trigger4_hrs[] = prioritypreowned_trigger4_hrs.split("\\.");
					// if (prioritypreowned1_trigger4_hrs[0].length() < 2) {
					// prioritypreowned1_trigger4_hrs[0] = "0" + prioritypreowned1_trigger4_hrs[0];
					// }
					// if (prioritypreowned1_trigger4_hrs[1].length() < 2) {
					// prioritypreowned1_trigger4_hrs[1] = prioritypreowned1_trigger4_hrs[1] + "0";
					// }
					// prioritypreowned_trigger4_hrs = prioritypreowned1_trigger4_hrs[0] + ":" + prioritypreowned1_trigger4_hrs[1];

					prioritypreowned_trigger5_hrs = crs.getString("prioritypreowned_trigger5_hrs");
					// if (!prioritypreowned_trigger5_hrs.contains(".")) {
					// prioritypreowned_trigger5_hrs = prioritypreowned_trigger5_hrs + ".00";
					// }
					// String prioritypreowned1_trigger5_hrs[] = prioritypreowned_trigger5_hrs.split("\\.");
					// if (prioritypreowned1_trigger5_hrs[0].length() < 2) {
					// prioritypreowned1_trigger5_hrs[0] = "0" + prioritypreowned1_trigger5_hrs[0];
					// }
					// if (prioritypreowned1_trigger5_hrs[1].length() < 2) {
					// prioritypreowned1_trigger5_hrs[1] = prioritypreowned1_trigger5_hrs[1] + "0";
					// }
					// prioritypreowned_trigger5_hrs = prioritypreowned1_trigger5_hrs[0] + ":" + prioritypreowned1_trigger5_hrs[1];
					SOP("priorityenquiry_duehrs-----" + prioritypreowned_duehrs);
					SOP("prioritypreowned_trigger1_hrs-----" + prioritypreowned_trigger1_hrs);
					SOP("prioritypreowned_trigger2_hrs-----" + prioritypreowned_trigger1_hrs);
					SOP("prioritypreowned_trigger3_hrs-----" + prioritypreowned_trigger3_hrs);
					SOP("prioritypreowned_trigger4_hrs-----" + prioritypreowned_trigger4_hrs);
					SOP("prioritypreowned_trigger5_hrs-----" + prioritypreowned_trigger5_hrs);
					prioritypreowned_entry_id = crs.getString("prioritypreowned_entry_id");

					if (!prioritypreowned_entry_id.equals("0")) {
						prioritypreowned_entry_by = Exename(comp_id, Integer.parseInt(prioritypreowned_entry_id));
					}
					prioritypreowned_entry_date = strToLongDate(crs.getString("prioritypreowned_entry_date"));
					prioritypreowned_modified_id = crs.getString("prioritypreowned_modified_id");
					if (!prioritypreowned_modified_id.equals("0")) {
						prioritypreowned_modified_by = Exename(comp_id, Integer.parseInt(prioritypreowned_modified_id));
					}
					prioritypreowned_modified_date = strToLongDate(crs.getString("prioritypreowned_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Pre Owned Priority!"));
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
				prioritypreowned_modified_id = emp_id;
				prioritypreowned_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE " + compdb(comp_id) + "axela_preowned_priority"
						+ " SET"
						+ " prioritypreowned_name = '" + prioritypreowned_name + "',"
						+ " prioritypreowned_desc = '" + prioritypreowned_desc + "',"
						+ " prioritypreowned_duehrs = '" + duehrs + "',"
						+ " prioritypreowned_trigger1_hrs = '" + trigger1_hrs + "',"
						+ " prioritypreowned_trigger2_hrs = '" + trigger2_hrs + "',"
						+ " prioritypreowned_trigger3_hrs = '" + trigger3_hrs + "',"
						+ " prioritypreowned_trigger4_hrs = '" + trigger4_hrs + "',"
						+ " prioritypreowned_trigger5_hrs = '" + trigger5_hrs + "',"
						+ " prioritypreowned_modified_id = " + prioritypreowned_modified_id + ","
						+ " prioritypreowned_modified_date = '" + prioritypreowned_modified_date + "'"
						+ " WHERE prioritypreowned_id = " + prioritypreowned_id + " ";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (prioritypreowned_id.equals("1")) {
			msg = "<br>First Record Cannot be Deleted!";
			return;
		}
		// StrSql = "SELECT preowned_prioritypreowned_id FROM " + compdb(comp_id) + "axela_sales_preowned where preowned_prioritypreowned_id = " + prioritypreowned_id + "";
		// if (!ExecuteQuery(StrSql).equals("")) {
		// msg = msg + "<br>Priority is Associated with Opportunity!";
		// }
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM " + compdb(comp_id) + "axela_preowned_priority"
						+ " WHERE  prioritypreowned_id =" + prioritypreowned_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
