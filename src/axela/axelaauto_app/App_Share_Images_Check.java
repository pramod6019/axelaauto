package axela.axelaauto_app;
//// divya
// modified by sn 6, 7 may 2013
     //import axela.account.Account_Update;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class App_Share_Images_Check extends Connect {

	public String StrSql = "";
	public String StrSearch = "";
	public String StrHTML = "";
	public String team_id = "0";
	public String enquiry_branch_id = "0";
	public String enquiry_model_id = "0";
	public String update = "";
	public String quote_model_id = "0";
	public String imgdatacat_model_id = "0", imgdatacat_type_id = "0";
	public String enquiry_date = "";
	public String imgdatacat_id = "0";
	public String enquiry_preownedmodel_id = "0";
	public String model = "", vehquote = "";
	public String model_id = "0", img = "", share = "", enquiry_type_id = "0";
	App_Share_Images image = new App_Share_Images();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		img = PadQuotes(request.getParameter("image"));
		share = PadQuotes(request.getParameter("share"));
		imgdatacat_model_id = CNumeric(PadQuotes(request.getParameter("imgdatacat_model_id")));
		imgdatacat_type_id = CNumeric(PadQuotes(request.getParameter("imgdatacat_type_id")));
		imgdatacat_id = CNumeric(PadQuotes(request.getParameter("imgdatacat_id")));

		if (img.equals("yes") && !imgdatacat_model_id.equals("0") && !imgdatacat_type_id.equals("0")) {

			StrHTML = image.PopulateCategory(imgdatacat_model_id, imgdatacat_type_id);
		}
		if (share.equals("yes") && !imgdatacat_model_id.equals("0") && !imgdatacat_type_id.equals("0") && !imgdatacat_id.equals("0")) {

			StrHTML = image.ListData(imgdatacat_model_id, imgdatacat_type_id, imgdatacat_id);

		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
