package axela.ddmotors_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cloudify.connect.Connect;

public class Success extends Connect {

	public String msg = "";
	public String emp_id = "";
	public String branch_id = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {

		msg = PadQuotes(request.getParameter("msg"));
		msg = unescapehtml(msg);

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
