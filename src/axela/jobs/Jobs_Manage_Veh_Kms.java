//$atiSh 24-Oct-2013
package axela.jobs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import axela.service.Manage_Veh_Kms;
import cloudify.connect.Connect;

public class Jobs_Manage_Veh_Kms extends Connect {

	public String comp_id = "0";
	public String StrHTML = "";
	Manage_Veh_Kms managekms = new Manage_Veh_Kms();

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			if (AppRun().equals("0")) {
				comp_id = "1000";
				managekms.VehKmsUpdate("0", comp_id);
				Thread.sleep(2000);
			} else {
				// comp_id = "1009";
				// managekms.VehKmsUpdate("0", comp_id);
				// Thread.sleep(2000);
			}
			StrHTML = "Vehicle Kms Updated Successfully!";
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
	}

}
