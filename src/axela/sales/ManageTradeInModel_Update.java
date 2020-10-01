//Shivaprasad 7July2014
package axela.sales;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManageTradeInModel_Update extends Connect {

	public String add = "";
	public String update = "";
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public static String status = "";
	public String StrSql = "";
	public static String msg = "";
	public String tradeinmodel_id = "0";
	public String tradeinmodel_name = "";
	public String model_make_id = "0";
	public String tradeinmodel_entry_id = "";
	public String tradeinmodel_entry_date = "";
	public String tradeinmodel_modified_id = "";
	public String tradeinmodel_modified_date = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String entry_by = "";
	public String entry_date = "";
	public String modified_by = "";
	public String modified_date = "";

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
				tradeinmodel_id = CNumeric(PadQuotes(request.getParameter("tradeinmodel_id")));
				QueryString = PadQuotes(request.getQueryString());

				if ("yes".equals(add)) {
					status = "Add";
					if (!"yes".equals(addB)) {
					} else {

						GetValues(request, response);
						tradeinmodel_entry_id = emp_id;
						tradeinmodel_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetradeinmodel.jsp?tradeinmodel_id=" + tradeinmodel_id + "&msg=Model Added Successfully!"));
						}

					}
				}
				if ("yes".equals(update)) {
					status = "Update";
					if (!"yes".equals(updateB) && !"Delete Model".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Model".equals(deleteB)) {
						GetValues(request, response);
						tradeinmodel_modified_id = emp_id;
						tradeinmodel_modified_date = ToLongDate(kknow());
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetradeinmodel.jsp?tradeinmodel_id=" + tradeinmodel_id + "&msg=Model Updated Successfully!"));
						}

					} else if ("Delete Model".equals(deleteB)) {
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managetradeinmodel.jsp?all=yes"));
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
		tradeinmodel_id = CNumeric(PadQuotes(request.getParameter("tradeinmodel_id")));
		model_make_id = CNumeric(PadQuotes(request.getParameter("dr_tradeinmake_id")));
		tradeinmodel_name = PadQuotes(request.getParameter("txt_tradeinmodel_name"));
		entry_by = PadQuotes(request.getParameter("entry_by"));
		entry_date = PadQuotes(request.getParameter("entry_date"));
		modified_by = PadQuotes(request.getParameter("modified_by"));
		modified_date = PadQuotes(request.getParameter("modified_date"));
	}

	protected void CheckForm() {
		if (model_make_id.equals("0")) {
			msg = msg + "<br>Select Make!";
		}
		if (tradeinmodel_name.equals("")) {
			msg = msg + "<br>Enter Model!";
		} else {
			if (!model_make_id.equals("0")) {
				try {
					StrSql = "Select tradeinmodel_name from " + compdb(comp_id) + "axela_sales_tradein_model"
							+ " where tradeinmodel_name = '" + tradeinmodel_name + "'"
							+ " AND tradeinmodel_tradeinmake_id = '" + model_make_id + "'";
					if (update.equals("yes")) {
						StrSql += " and tradeinmodel_id != " + tradeinmodel_id + "";
					}
					CachedRowSet crs = processQuery(StrSql, 0);
					if (crs.isBeforeFirst()) {
						msg = msg + "<br>Similar Model Found!";
					}
					crs.close();
				} catch (Exception ex) {
					SOPError("Axelaauto== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
				}
			}
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				tradeinmodel_id = ExecuteQuery("Select (coalesce(max(tradeinmodel_id),0)+1) from " + compdb(comp_id) + "axela_sales_tradein_model");
				StrSql = "Insert into " + compdb(comp_id) + "axela_sales_tradein_model"
						+ " (tradeinmodel_id,"
						+ " tradeinmodel_tradeinmake_id,"
						+ " tradeinmodel_name,"
						+ " tradeinmodel_entry_id,"
						+ " tradeinmodel_entry_date)"
						+ " values"
						+ " (" + tradeinmodel_id + ","
						+ " " + model_make_id + ","
						+ " '" + tradeinmodel_name + "',"
						+ " '" + tradeinmodel_entry_id + "',"
						+ " '" + tradeinmodel_entry_date + "')";
				// SOP(StrSqlBreaker(StrSql));
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "Select tradeinmodel_id, tradeinmodel_name, "
					+ " tradeinmodel_tradeinmake_id, tradeinmodel_entry_id, tradeinmodel_entry_date,"
					+ " COALESCE(tradeinmodel_modified_id, '0') as tradeinmodel_modified_id,"
					+ " COALESCE(tradeinmodel_modified_date, '') as tradeinmodel_modified_date"
					+ " from " + compdb(comp_id) + "axela_sales_tradein_model"
					+ " where tradeinmodel_id= " + tradeinmodel_id + "";
			// SOP(StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst() && !tradeinmodel_id.equals("0")) {
				while (crs.next()) {
					model_make_id = crs.getString("tradeinmodel_tradeinmake_id");
					tradeinmodel_name = crs.getString("tradeinmodel_name");
					tradeinmodel_entry_id = crs.getString("tradeinmodel_entry_id");
					if (!tradeinmodel_entry_id.equals("0")) {
						entry_by = Exename(comp_id, crs.getInt("tradeinmodel_entry_id"));
						entry_date = strToLongDate(crs.getString("tradeinmodel_entry_date"));
					}
					tradeinmodel_modified_id = crs.getString("tradeinmodel_modified_id");
					if (!tradeinmodel_modified_id.equals("0")) {
						modified_by = Exename(comp_id, Integer.parseInt(tradeinmodel_modified_id));
						modified_date = strToLongDate(crs.getString("tradeinmodel_modified_date"));
					}
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Model!"));
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_tradein_model"
						+ " SET"
						+ " tradeinmodel_tradeinmake_id= " + model_make_id + ","
						+ " tradeinmodel_name= '" + tradeinmodel_name + "',"
						+ " tradeinmodel_modified_id = '" + tradeinmodel_modified_id + "',"
						+ " tradeinmodel_modified_date = '" + tradeinmodel_modified_date + "'"
						+ " where tradeinmodel_id = " + tradeinmodel_id + "";
				// SOP("StrSql--" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		msg = "";
		StrSql = "SELECT enquiry_tradeinmodel_id FROM " + compdb(comp_id) + "axela_sales_enquiry where enquiry_tradeinmodel_id = " + tradeinmodel_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is Associated with Enquiry!";
		}
		StrSql = "SELECT tradeinmodel_tradeinmake_id FROM " + compdb(comp_id) + "axela_sales_tradein_model where tradeinmodel_tradeinmake_id = " + tradeinmodel_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Model is Associated with Make!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "Delete from " + compdb(comp_id) + "axela_sales_tradein_model where tradeinmodel_id = " + tradeinmodel_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	public String PopulateMake() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "Select tradeinmake_id, tradeinmake_name"
					+ " from " + compdb(comp_id) + "axela_sales_tradein_make"
					+ " group by tradeinmake_id"
					+ " order by tradeinmake_name";
			CachedRowSet crs = processQuery(StrSql, 0);
			// SOP(StrSqlBreaker(StrSql));
			Str.append("<option value = 0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("tradeinmake_id"));
				Str.append(StrSelectdrop(crs.getString("tradeinmake_id"), model_make_id));
				Str.append(">").append(crs.getString("tradeinmake_name")).append("</option>\n");
			}
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

}
