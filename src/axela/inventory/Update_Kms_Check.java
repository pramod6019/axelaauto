package axela.inventory;
//aJIt 11th March, 2013

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Update_Kms_Check extends Connect {
	
	public String StrHTML = "", StrPostponed = "";
	public String StrSearch = "";
	public String addB = "";
	public String comp_id = "0";
	public String StrSql = "";
	public String msg = "";
	public String branch_id = "0";
	public String BranchAccess = "";
	public String ExeAccess = "";
	public String go = "";
	public String vehstockgatepass_id = "0";
	public String vehstockgatepass_vehstock_id = "0";
	public String vehstockgatepass_in_kms = "0", vehstockgatepass_out_kms = "0";
	public String emp_id = "0";
	public String vehstock_vehstocklocation_id = "0";
	DecimalFormat df = new DecimalFormat("0.00");
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				branch_id = CNumeric(GetSession("emp_branch_id", request));
				emp_id = CNumeric(GetSession("emp_id", request));
				BranchAccess = GetSession("BranchAccess", request);
				ExeAccess = GetSession("ExeAccess", request);
				addB = PadQuotes(request.getParameter("add_button"));
				emp_id = CNumeric(GetSession("emp_id", request));
				vehstockgatepass_id = PadQuotes(request.getParameter("vehstockgatepass_id"));
				vehstockgatepass_vehstock_id = PadQuotes(request.getParameter("vehstockgatepass_vehstock_id"));
				vehstockgatepass_in_kms = PadQuotes(request.getParameter("vehstockgatepass_in_kms"));
				
				if (addB.equals("yes") && ReturnPerm(comp_id, "emp_stock_inkms", request).equals("1")) {
					UpdateKms();
					if (!msg.equals("")) {
						msg = "Error!" + msg;
					} else {
						msg = "In Kms updated";
					}
					StrHTML = msg;
				} else {
					StrHTML = "Access Denied!";
				}
			}
			
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	
	protected void CheckForm() {
		msg = "";
		StrSql = "SELECT"
				+ " vehstockgatepass_out_kms"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
				+ " WHERE vehstockgatepass_id = " + vehstockgatepass_id + "";
		vehstockgatepass_out_kms = ExecuteQuery(StrSql);
		if (Double.parseDouble(vehstockgatepass_out_kms) > Double.parseDouble(vehstockgatepass_in_kms)) {
			msg = msg + "<br>In KMS cannot be less than Out KMS!";
		}
		// for updating the actual stocks location
		vehstock_vehstocklocation_id = ExecuteQuery("SELECT vehstockgatepass_to_location_id"
				+ " FROM " + compdb(comp_id) + "axela_vehstock_gatepass"
				+ " WHERE vehstockgatepass_id = " + vehstockgatepass_id);
	}
	
	protected void UpdateKms() throws SQLException {
		CheckForm();
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock_gatepass"
						+ " SET "
						+ " vehstockgatepass_in_kms = " + vehstockgatepass_in_kms + ","
						+ " vehstockgatepass_inkms_entry_id = " + emp_id + ","
						+ " vehstockgatepass_inkms_entry_time= " + ToLongDate(kknow()) + ""
						+ " WHERE vehstockgatepass_id = " + vehstockgatepass_id + "";
				// SOP("Update==" + StrSql);
				updateQuery(StrSql);
				
				// updating the actual stocks location when in kms is updated
				StrSql = "UPDATE " + compdb(comp_id) + "axela_vehstock"
						+ " SET"
						+ " vehstock_vehstocklocation_id = " + vehstock_vehstocklocation_id + ""
						+ " WHERE vehstock_id = " + vehstockgatepass_vehstock_id + "";
				updateQuery(StrSql);
				
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
}
