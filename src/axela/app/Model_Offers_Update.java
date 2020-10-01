package axela.app;
//@Shilpashree 05 oct 2015

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Model_Offers_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String comp_id = "0";
	public String offers_id = "0";
	public String offers_topic = "";
	public String offers_offertype_id = "0";
	public String offers_desc = "";
	public String emp_role_id = "0";
	public String offers_active = "0";
	public String offers_entry_id = "0", offers_entry_date = "";
	public String offers_modified_id = "0", offers_modified_date = "";
	public String entry_by = "", entry_date = "", modified_by = "", modified_date = "";
	public String emp_id = "0";
	public String BranchAccess = "";
	public String QueryString = "";
	public String offers_date = "";
	public String offersdate = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();

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
				offers_id = CNumeric(PadQuotes(request.getParameter("offers_id")));
				QueryString = PadQuotes(request.getQueryString());

				if (add.equals("yes")) {
					offers_active = "1";
					status = "Add";
					if (addB.equals("yes")) {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							offers_entry_id = emp_id;
							offers_entry_date = ToLongDate(kknow());
							GetValues(request, response);
							AddFields(request);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("model-offers-list.jsp?offers_id=" + offers_id + "&msg=Offer added successfully!"));
								status = "";
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					}
				}
				if (update.equals("yes")) {
					status = "Update";
					if (!updateB.equals("yes") && !deleteB.equals("Delete Offer")) {
						PopulateFields(response);
					} else if (updateB.equals("yes") && !deleteB.equals("Delete Offer")) {
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							offers_modified_id = emp_id;
							offers_modified_date = ToLongDate(kknow());
							GetValues(request, response);
							UpdateFields(request, response);
							if (!msg.equals("")) {
								msg = "Error! " + msg;
							} else {
								msg = "Offer updated successfully!";
								response.sendRedirect(response.encodeRedirectURL("model-offers-list.jsp?offers_id=" + offers_id + "&msg=" + msg));
							}
						} else {
							response.sendRedirect(AccessDenied());
						}
					} else if (deleteB.equals("Delete Offer")) {
						GetValues(request, response);
						if (ReturnPerm(comp_id, "emp_role_id", request).equals("1")) {
							DeleteFields(response);
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							} else {
								response.sendRedirect(response.encodeRedirectURL("model-offers-list.jsp?msg=Offer deleted successfully!"));
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

	protected void GetValues(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		offers_topic = PadQuotes(request.getParameter("txt_offers_topic"));
		offers_desc = PadQuotes(request.getParameter("txt_offers_desc"));
		// if (offers_desc.length() > 5000) {
		// offers_desc = offers_desc.substring(0, 4999);
		// }
		// SOP("offers_desc--------" + offers_desc.length());
		offers_active = CheckBoxValue(PadQuotes(request.getParameter("chk_offers_active")));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		offersdate = PadQuotes(request.getParameter("txt_offers_date"));
		offers_date = offersdate;
		offers_offertype_id = CNumeric(PadQuotes(request.getParameter("dr_offers_offertype_id")));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (offers_topic.equals("")) {
			msg = msg + "<br>Enter Topic!";
		}
		if (offersdate.equals("")) {
			msg = msg + "<br>Select Date!";
		}
		if (!offersdate.equals("")) {
			boolean t2 = isValidDateFormatShort(offersdate);
			if (t2 == true) {
				offers_date = ConvertShortDateToStr(offersdate);
			} else if (t2 == false) {
				msg = msg + "<br>Enter Valid Date!";
			}
		}
		if (offers_offertype_id.equals("0")) {
			msg = msg + "<br>Select Type!";
		}
		if (offers_desc.equals("")) {
			msg = msg + "<br>Enter Description!";
		}
		// else if (offers_desc.length() > 5000) {
		// offers_desc = offers_desc.substring(0, 4999);
		// }
	}

	protected void AddFields(HttpServletRequest request) throws Exception {
		CheckForm();
		if (msg.equals("")) {
			offers_id = ExecuteQuery("SELECT (COALESCE(MAX(offers_id),0)+1) FROM " + compdb(comp_id) + "axela_app_offers");
			StrSql = "INSERT INTO " + compdb(comp_id) + "axela_app_offers"
					+ "("
					+ " offers_id,"
					+ " offers_offertype_id,"
					+ " offers_topic,"
					+ " offers_desc,"
					+ " offers_date,"
					+ " offers_active,"
					+ " offers_entry_id,"
					+ " offers_entry_date)"
					+ " VALUES"
					+ " ("
					+ " " + offers_id + ","
					+ " " + offers_offertype_id + ","
					+ " '" + offers_topic + "',"
					+ " '" + offers_desc + "',"
					+ " '" + offers_date + "',"
					+ " '" + offers_active + "',"
					+ " " + offers_entry_id + ","
					+ " '" + offers_entry_date + "')";
			updateQuery(StrSql);
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT * FROM " + compdb(comp_id) + "axela_app_offers"
					+ " WHERE offers_id = ?";
			map.put(1, offers_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					offers_id = crs.getString("offers_id");
					offers_offertype_id = crs.getString("offers_offertype_id");
					offers_topic = crs.getString("offers_topic");
					offers_desc = unescapehtml(crs.getString("offers_desc"));
					offers_active = crs.getString("offers_active");
					offers_date = strToShortDate(crs.getString("offers_date"));
					offers_entry_id = crs.getString("offers_entry_id");
					entry_by = Exename(comp_id, crs.getInt("offers_entry_id"));
					entry_date = strToLongDate(crs.getString("offers_entry_date"));
					offers_modified_id = crs.getString("offers_modified_id");
					if (!offers_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(offers_modified_id));
						modified_date = strToLongDate(crs.getString("offers_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Offer!"));
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
			// try {
			StrSql = "UPDATE " + compdb(comp_id) + "axela_app_offers"
					+ " SET"
					+ " offers_topic = '" + offers_topic + "',"
					+ " offers_desc = '" + offers_desc + "',"
					+ " offers_date = '" + offers_date + "',"
					+ " offers_offertype_id = '" + offers_offertype_id + "',"
					+ " offers_active = '" + offers_active + "',"
					+ " offers_modified_id = " + offers_modified_id + ","
					+ " offers_modified_date = '" + offers_modified_date + "'"
					+ " WHERE offers_id = " + offers_id + " ";
			updateQuery(StrSql);
		}
	}

	public String PopulateType() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT offertype_id, offertype_name "
					+ " FROM " + compdb(comp_id) + " axela_app_offers_type"
					+ " WHERE 1=1"
					+ " GROUP BY offertype_id"
					+ " ORDER BY offertype_name";
			CachedRowSet crs =processQuery(StrSql, 0);
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("offertype_id")).append("");
				Str.append(StrSelectdrop(crs.getString("offertype_id"), offers_offertype_id));
				Str.append(">").append(crs.getString("offertype_name")).append("</option> \n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return Str.toString();
	}
	protected void DeleteFields(HttpServletResponse response) {

		StrSql = "DELETE FROM " + compdb(comp_id) + "axela_app_offers" + " WHERE offers_id = " + offers_id + "";
		updateQuery(StrSql);
	}
}
