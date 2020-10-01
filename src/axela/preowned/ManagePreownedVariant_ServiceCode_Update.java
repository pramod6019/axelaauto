// Bhagwan Singh (27th June 2013)
package axela.preowned;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class ManagePreownedVariant_ServiceCode_Update extends Connect {

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
	public String carmanuf_id = "0";
	public String manufacturer = "";
	public String model = "";
	public String model_id = "0";
	public String servicecode_variant_id = "0";
	public String servicecode_code = "";
	public String servicecode_id = "";
	public String QueryString = "";
	public String StrHTML = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id, emp_preowned_stock_add", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				addB = PadQuotes(request.getParameter("add_button"));
				updateB = PadQuotes(request.getParameter("update_button"));
				deleteB = PadQuotes(request.getParameter("delete_button"));
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				manufacturer = PadQuotes(request.getParameter("manufacturer"));
				carmanuf_id = CNumeric(PadQuotes(request.getParameter("manuf_id")));
				model = PadQuotes(request.getParameter("model"));
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				servicecode_id = CNumeric(PadQuotes(request.getParameter("servicecode_id")));
				if (manufacturer.equals("yes")) {
					StrHTML = PopulatePreownedModel();
				}
				if (model.equals("yes")) {
					StrHTML = PopulatePreownedVariant();
				}
				if (add.equals("yes")) {
					status = "Add";
				} else if (update.equals("yes")) {
					status = "Update";
				}

				if ("yes".equals(add)) {
					if (!"yes".equals(addB)) {
					} else {
						GetValues(request, response);
						AddFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedvariant-servicecode.jsp?servicecode_id=" + servicecode_id
									+ "&msg=Pre Owned Variant Service Code Added Successfully!"));
						}
					}
				}
				if ("yes".equals(update)) {
					if (!"yes".equals(updateB) && !"Delete Pre Owned Variant Service Code".equals(deleteB)) {
						PopulateFields(response);
					} else if ("yes".equals(updateB) && !"Delete Pre Owned Variant Service Code".equals(deleteB)) {
						GetValues(request, response);
						UpdateFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedvariant-servicecode.jsp?servicecode_id=" + servicecode_id
									+ "&msg=Pre Owned Variant Service Code Updated Successfully!"));
						}
					}
					if ("Delete Pre Owned Variant Service Code".equals(deleteB)) {
						GetValues(request, response);
						DeleteFields();
						if (!msg.equals("")) {
							msg = "Error!" + msg;
						} else {
							response.sendRedirect(response.encodeRedirectURL("managepreownedvariant-servicecode.jsp?msg=Pre Owned Variant Service Code Deleted Successfully!"));
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
		carmanuf_id = PadQuotes(request.getParameter("dr_manufacturer"));
		model_id = PadQuotes(request.getParameter("dr_model"));
		servicecode_variant_id = PadQuotes(request.getParameter("dr_variant"));
		servicecode_code = PadQuotes(request.getParameter("txt_servicecode_code"));
	}

	protected void CheckForm() {
		msg = "";
		if (carmanuf_id.equals("0")) {
			msg = msg + "<br>Select Manufacturer!";
		}
		if (model_id.equals("0")) {
			msg = msg + "<br>Select Model!";
		}
		if (servicecode_variant_id.equals("0")) {
			msg = msg + "<br>Select Variant!";
		}
		if (servicecode_code.equals("")) {
			msg = msg + "<br>Enter Service Code!";
		}
		try {
			if (!servicecode_code.equals("")) {
				StrSql = "SELECT servicecode_variant_id, carmanuf_id"
						+ " FROM axela_preowned_variant_servicecode"
						+ " INNER JOIN axela_preowned_variant ON variant_id = servicecode_variant_id"
						+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
						+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
						+ " WHERE servicecode_code = '" + servicecode_code + "'"
						+ " AND carmanuf_id = " + carmanuf_id + "";
				if (update.equals("yes")) {
					StrSql = StrSql + " AND servicecode_id != " + servicecode_id + "";
				}
				// SOP("strsql===" + StrSql);
				CachedRowSet crs = processQuery(StrSql, 0);
				if (crs.isBeforeFirst()) {
					msg = msg + "<br>Similar Service Code Found!";
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
				StrSql = "INSERT INTO axela_preowned_variant_servicecode"
						+ " (servicecode_variant_id,"
						+ " servicecode_code)"
						+ " VALUES"
						+ " (" + servicecode_variant_id + ","
						+ " '" + servicecode_code + "')";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT servicecode_variant_id, variant_preownedmodel_id, preownedmodel_carmanuf_id,"
					+ " servicecode_code"
					+ " FROM axela_preowned_variant_servicecode"
					+ " INNER JOIN axela_preowned_variant ON variant_id = servicecode_variant_id"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " WHERE servicecode_id = " + servicecode_id + "";
			// SOP("StrSql--pop--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					servicecode_variant_id = crs.getString("servicecode_variant_id");
					model_id = crs.getString("variant_preownedmodel_id");
					carmanuf_id = crs.getString("preownedmodel_carmanuf_id");
					servicecode_code = crs.getString("servicecode_code");
				}
			} else {
				response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Pre Owned Variant!"));
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
				StrSql = "UPDATE  axela_preowned_variant_servicecode"
						+ " SET"
						+ " servicecode_variant_id = " + servicecode_variant_id + ","
						+ " servicecode_code = '" + servicecode_code + "'"
						+ " WHERE servicecode_id = " + servicecode_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		try {
			StrSql = "DELETE FROM axela_preowned_variant_servicecode"
					+ " WHERE servicecode_id = " + servicecode_id + "";
			updateQuery(StrSql);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
		}
	}

	public String PopulatePreownedManufacturer() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT carmanuf_id, carmanuf_name"
					+ " FROM axelaauto.axela_preowned_manuf"
					+ " WHERE 1 = 1"
					+ " GROUP BY carmanuf_id"
					+ " ORDER BY"
					+ " carmanuf_name";

			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select class=\"form-control\" id=\"dr_manufacturer\" name=\"dr_manufacturer\" onchange=\"PopulateModel();\">");
			Str.append("<option value=0 >").append("Select").append("</option>\n");
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					Str.append("<option value=").append(crs.getString("carmanuf_id")).append("");
					Str.append(StrSelectdrop(crs.getString("carmanuf_id"), carmanuf_id));
					Str.append(">").append(crs.getString("carmanuf_name")).append("</option>\n");
				}
			}
			Str.append("</select>");
			crs.close();
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return Str.toString();
	}

	public String PopulatePreownedModel() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT preownedmodel_id, preownedmodel_name"
					+ " FROM axela_preowned_model"
					+ " WHERE preownedmodel_carmanuf_id = " + carmanuf_id + ""
					+ " ORDER BY preownedmodel_name";
			// SOP("StrSql-----" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select class=\"form-control\" id=\"dr_model\" name=\"dr_model\" onchange=\"PopulateVariant();\">");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("preownedmodel_id"));
				Str.append(StrSelectdrop(crs.getString("preownedmodel_id"), model_id));
				Str.append(">").append(crs.getString("preownedmodel_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return "";
		}
	}
	public String PopulatePreownedVariant() {
		StringBuilder Str = new StringBuilder();
		try {
			StrSql = "SELECT variant_id, variant_name"
					+ " FROM axela_preowned_variant"
					+ " WHERE variant_preownedmodel_id = " + model_id + ""
					+ " ORDER BY variant_name";
			// SOP("StrSql--variant--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			Str.append("<select class=\"form-control\" id=\"dr_variant\" name=\"dr_variant\" >");
			Str.append("<option value=0>Select</option>");
			while (crs.next()) {
				Str.append("<option value=").append(crs.getString("variant_id"));
				Str.append(StrSelectdrop(crs.getString("variant_id"),
						servicecode_variant_id));
				Str.append(">").append(crs.getString("variant_name")).append("</option>\n");
			}
			Str.append("</select>");
			crs.close();
			return Str.toString();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName()
					+ ": " + ex);
			return "";
		}
	}

}
