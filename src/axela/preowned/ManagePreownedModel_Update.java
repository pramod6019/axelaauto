package axela.preowned;
/*
 * @author Bhagwan Singh (10th July 2013)
 */

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedModel_Update extends Connect {

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
	public String preownedmodel_id = "0";
	public String preownedmodel_name = "";
	public String preownedmodel_carmanuf_id = "0";
	public String preownedmodel_entry_id = "0";
	public String preownedmodel_entry_by = "";
	public String preownedmodel_entry_date = "";
	public String preownedmodel_modified_id = "0";
	public String preownedmodel_modified_by = "";
	public String preownedmodel_modified_date = "";
	public String entry_by = "", modified_by = "";
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
				preownedmodel_id = CNumeric(PadQuotes(request.getParameter("preownedmodel_id")));
				preownedmodel_carmanuf_id = CNumeric(PadQuotes(request.getParameter("preownedmodel_carmanuf_id")));
				// SOP("preownedmodel_carmanuf_id===in update=" + preownedmodel_carmanuf_id);
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
						preownedmodel_entry_id = emp_id;
						preownedmodel_entry_date = ToLongDate(kknow());
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedmodel.jsp?preownedmodel_id=" + preownedmodel_id + "&msg=Pre Owned Model Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre Owned Model".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre Owned Model".equals(deleteB)) {
						GetValues(request, response);

						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedmodel.jsp?preownedmodel_id=" + preownedmodel_id + "&msg=Pre Owned Model Updated Successfully!"));
						}
					}

					if ("Delete Pre Owned Model".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedmodel.jsp?msg=Pre Owned Model Deleted Successfully!"));
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
		preownedmodel_carmanuf_id = PadQuotes(request.getParameter("manufacturer"));
		preownedmodel_name = PadQuotes(request.getParameter("txt_preownedmodel_name"));
		preownedmodel_entry_by = PadQuotes(request.getParameter("entry_by"));
		preownedmodel_modified_by = PadQuotes(request.getParameter("modified_by"));
		preownedmodel_entry_date = PadQuotes(request.getParameter("entry_date"));
		preownedmodel_modified_date = PadQuotes(request.getParameter("modified_date"));
	}
	protected void CheckForm() {
		msg = "";
		if (preownedmodel_carmanuf_id.equals("")) {
			msg = msg + "<br>Enter Manufacturer!";
		}
		if (preownedmodel_name.equals("")) {
			msg = msg + "<br>Enter Name!";
		}
		try {
			if (!preownedmodel_name.equals("")) {
				StrSql = "SELECT preownedmodel_name"
						+ " FROM axela_preowned_model"
						+ " WHERE preownedmodel_name = '" + preownedmodel_name + "'";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND preownedmodel_id != " + preownedmodel_id + "";
				}
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Name Found!";
				}
				crs.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void AddFields() {
		CheckForm();
		if (msg.equals("")) {
			try {
				preownedmodel_id = ExecuteQuery("SELECT COALESCE(MAX(preownedmodel_id),0)+1 AS preownedmodel_id FROM axela_preowned_model");

				StrSql = "INSERT INTO axela_preowned_model"
						+ "(preownedmodel_id,"
						+ " preownedmodel_carmanuf_id,"
						+ " preownedmodel_name,"
						+ " preownedmodel_entry_id,"
						+ " preownedmodel_entry_date)"
						+ " values"
						+ " (" + preownedmodel_id + ","
						+ " " + preownedmodel_carmanuf_id + ", "
						+ " '" + preownedmodel_name + "',"
						+ " " + preownedmodel_entry_id + ","
						+ " '" + preownedmodel_entry_date + "')";
				SOP("StrSql==AddFields" + StrSql);
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT preownedmodel_carmanuf_id, preownedmodel_name, preownedmodel_entry_id, preownedmodel_entry_date, preownedmodel_modified_id,"
					+ " preownedmodel_modified_date"
					+ " FROM axela_preowned_model"
					+ " WHERE preownedmodel_id = " + preownedmodel_id + "";

			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					preownedmodel_carmanuf_id = crs.getString("preownedmodel_carmanuf_id");
					preownedmodel_name = crs.getString("preownedmodel_name");
					preownedmodel_entry_id = crs.getString("preownedmodel_entry_id");
					if (!preownedmodel_entry_id.equals("")) {
						preownedmodel_entry_by = Exename(comp_id, Integer.parseInt(preownedmodel_entry_id));
					}
					preownedmodel_entry_date = strToLongDate(crs.getString("preownedmodel_entry_date"));
					preownedmodel_modified_id = crs.getString("preownedmodel_modified_id");
					if (!preownedmodel_modified_id.equals("")) {
						preownedmodel_modified_by = Exename(comp_id, Integer.parseInt(preownedmodel_modified_id));
					}
					preownedmodel_modified_date = strToLongDate(crs.getString("preownedmodel_modified_date"));
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Used Model!"));
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
				preownedmodel_modified_id = emp_id;
				preownedmodel_modified_date = ToLongDate(kknow());
				StrSql = "UPDATE axela_preowned_model"
						+ " SET"
						+ " preownedmodel_carmanuf_id = " + preownedmodel_carmanuf_id + ","
						+ " preownedmodel_name = '" + preownedmodel_name + "',"
						+ " preownedmodel_modified_id = " + preownedmodel_modified_id + ","
						+ " preownedmodel_modified_date = '" + preownedmodel_modified_date + "'"
						+ " WHERE preownedmodel_id = " + preownedmodel_id + "";
				updateQuery(StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		StrSql = "SELECT variant_preownedmodel_id"
				+ " FROM axela_preowned_variant"
				+ " WHERE variant_preownedmodel_id = " + preownedmodel_id + "";
		if (!ExecuteQuery(StrSql).equals("")) {
			msg = msg + "<br>Pre Owned Model is associated with Variant!";
		}
		if (msg.equals("")) {
			try {
				StrSql = "DELETE FROM axela_preowned_model"
						+ " WHERE preownedmodel_id = " + preownedmodel_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
}
