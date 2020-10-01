package axela.preowned;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import cloudify.connect.Connect;

public class Preowned_Variant_Switch extends Connect {

	public String emp_id = "0";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String carmanuf_id = "0";
	public String carmanuf_name = "";
	public String preownedmodel_id = "0";
	public String preownedmodel_name = "";
	public String variant_id = "0";
	public String variant_name = "";
	public String to_variant_id = "0";
	public String QueryString = "";
	public String StrHTML = "", addB = "";
	public Connection conntx = null;
	public Statement stmttx = null;
	public Preowned_Variant_Check modelcheck = new Preowned_Variant_Check();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				CheckPerm(comp_id, "emp_role_id, emp_preowned_stock_add", request, response);
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				addB = PadQuotes(request.getParameter("add_button"));
				variant_id = CNumeric(PadQuotes(request.getParameter("variant_id")));
				PopulateFields(response);
				// SOP("add====" + addB);
				if ("yes".equals(addB)) {
					GetValues(request, response);
					SwitchVariant();
					if (!msg.equals("") && !msg.equals("Variant Transferred Successfully!")) {
						msg = "Error!" + msg;
					} else {
						// response.sendRedirect(response.encodeRedirectURL("managepreownedvariant-servicecode.jsp?servicecode_id=" + servicecode_id
						// + "&msg=Pre Owned Variant Service Code Added Successfully!"));
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
		to_variant_id = PadQuotes(request.getParameter("preownedvariant"));
		carmanuf_id = PadQuotes(request.getParameter("carmanuf_id"));
		preownedmodel_id = PadQuotes(request.getParameter("preownedmodel_id"));
		variant_id = PadQuotes(request.getParameter("variant_id"));
	}

	protected void CheckForm() {
		msg = "";
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "SELECT carmanuf_id, carmanuf_name, preownedmodel_id, preownedmodel_name,"
					+ " variant_name"
					+ " FROM axela_preowned_variant"
					+ " INNER JOIN axela_preowned_model ON preownedmodel_id = variant_preownedmodel_id"
					+ " INNER JOIN axela_preowned_manuf ON carmanuf_id = preownedmodel_carmanuf_id"
					+ " WHERE variant_id = " + variant_id;
			// SOP("StrSql--pop--" + StrSql);
			CachedRowSet crs = processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					carmanuf_id = crs.getString("carmanuf_id");
					carmanuf_name = crs.getString("carmanuf_name");
					preownedmodel_id = crs.getString("preownedmodel_id");
					preownedmodel_name = crs.getString("preownedmodel_name");
					variant_name = crs.getString("variant_name");
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

	public void SwitchVariant() {
		try {
			StrSql = "SELECT table_schema FROM information_schema.TABLES"
					+ " WHERE table_schema LIKE 'axelaauto\\_%'"
					+ " GROUP BY table_schema"
					+ " ORDER BY table_schema";
			// SOP("StrSql----111-----" + StrSql);
			// SOP("tovariant_id---" + to_variant_id);
			try {
				conntx = connectDB();
				conntx.setAutoCommit(false);
				stmttx = conntx.createStatement();
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					StrSql = "UPDATE " + crs.getString("table_schema") + ".axela_sales_enquiry"
							+ " SET enquiry_preownedvariant_id = " + to_variant_id
							+ " WHERE enquiry_preownedvariant_id = " + variant_id + "";
					stmttx.addBatch(StrSql);
					// SOP("StrSql----222-----" + StrSql);

					StrSql = "UPDATE " + crs.getString("table_schema") + ".axela_sales_enquiry"
							+ " SET enquiry_tradein_preownedvariant_id = " + to_variant_id
							+ " WHERE enquiry_tradein_preownedvariant_id = " + variant_id + "";
					stmttx.addBatch(StrSql);
					// SOP("StrSql----555-----" + StrSql);

					StrSql = "UPDATE " + crs.getString("table_schema") + ".axela_service_veh"
							+ " SET veh_variant_id = " + to_variant_id
							+ " WHERE veh_variant_id = " + variant_id + "";
					stmttx.addBatch(StrSql);
					// SOP("StrSql----333-----" + StrSql);

					StrSql = "UPDATE " + crs.getString("table_schema") + ".axela_sales_enquiry_currentcars"
							+ " SET currentcars_variant_id = " + to_variant_id
							+ " WHERE currentcars_variant_id = " + variant_id + "";
					stmttx.addBatch(StrSql);
					// SOP("StrSql----333-----" + StrSql);

					StrSql = "UPDATE " + crs.getString("table_schema") + ".axela_preowned"
							+ " SET preowned_variant_id = " + to_variant_id
							+ " WHERE preowned_variant_id = " + variant_id + "";
					stmttx.addBatch(StrSql);
					// SOP("StrSql----444-----" + StrSql);

				}

				stmttx.executeBatch();
				conntx.commit();
				// if msg is changed then changes have to be made for doPost() if() conditon
				msg = "Variant Transferred Successfully!";
			} catch (Exception e) {
				if (conntx.isClosed()) {
					SOPError("connection is closed...");
				}
				if (!conntx.isClosed() && conntx != null) {
					conntx.rollback();
					SOPError("connection rollback...");
				}
				msg = "<br>Transaction Error!";
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + e);
			} finally {
				conntx.setAutoCommit(true);// Enables auto commit
				if (stmttx != null && !stmttx.isClosed()) {
					stmttx.close();
				}
				if (conntx != null && !conntx.isClosed()) {
					conntx.close();
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

}
