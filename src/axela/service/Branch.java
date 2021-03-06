package axela.service;
//divya 3rd July 2012

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Branch extends Connect {
	// ///// List page links

	public String LinkHeader = "";
	public String search = "";
	public String txt_search = "";
	public String cli = "";
	public String comp_id = "0";
	public String type_id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				// CheckPerm(comp_id, "emp_contact_access", request, response);
				search = PadQuotes(request.getParameter("search_button"));
				txt_search = PadQuotes(request.getParameter("txt_search"));
				type_id = CNumeric(PadQuotes(request.getParameter("dr_type_id")));
				cli = PadQuotes(request.getParameter("From"));
				if (!cli.equals("")) {
					txt_search = cli;
				}
			}
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public String PopulateType(String comp_id) {
		StringBuilder Str = new StringBuilder();
		// Str.append("<option value=0>Select</option>\n");
		Str.append("<option value=1").append(StrSelectdrop("1", type_id)).append(">Employees</option>\n");
		Str.append("<option value=2").append(StrSelectdrop("2", type_id)).append(">Branches</option>\n");
		return Str.toString();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
