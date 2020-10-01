package axela.ddmotors_app;

// divya
// modified by sn 6, 7 may 2013

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Model_Check extends Connect {

	public String StrSql = "";

	public String StrSearch = "";
	public String StrHTML = "";
	public String newcar = "", enquiry = "";
	public String comp_id = "0";
	public String brand_id = "0", model_id = "0";
	public String model = "";
	Book_A_TestDrive tstdrive = new Book_A_TestDrive();
	Book_A_Car car = new Book_A_Car();

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		comp_id = ddmotors_app_comp_id();
		if (!comp_id.equals("0")) {
			model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
			enquiry = PadQuotes(request.getParameter("enquiry"));
			SOP("model_id===11=" + model_id);
			model = PadQuotes(request.getParameter("model"));
			if (model.equals("yes")) {
				StrHTML = tstdrive.PopulateItem(model_id, comp_id);
			}

			if (enquiry.equals("yes")) {
				StrHTML = car.PopulateItem(model_id, comp_id);
			}
		}
	}
}
