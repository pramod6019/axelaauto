package axela.gst;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Header extends Connect {

	public String comp_id = "0";
	public String comp_logo = "";
	public String branch_address = "0";
	public String city_name = "0";
	public String branch_pin = "0";

	public String StrSql = "";
	public String updatestatus = "";
	public String deleteB = "";
	public String updateB = "";
	public String add = "";
	public String update = "";
	public String status = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			Map getMap = (Map) session.getAttribute("sessionMap");
			comp_id = CNumeric(GetSession("comp_id", request));
			comp_logo = getMap.get("comp_logo") + "";
			branch_address = getMap.get("branch_address") + "";
			city_name = getMap.get("city_name") + "";
			branch_pin = CNumeric(PadQuotes(getMap.get("branch_pin") + ""));
			if (!branch_pin.equals("0")) {
				branch_pin = ", " + branch_pin;
			}
			if (!city_name.equals("")) {
				city_name = ", " + city_name;
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
}
