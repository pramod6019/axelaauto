package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import axela.portal.Header;
import axela.sales.Veh_Quote_Email;
import cloudify.connect.Connect;

public class App_Quote_Email extends Connect {
	public String comp_email_enable = "0";
	public String brandconfig_quote_email_enable = "";
	public String brandconfig_quote_email_format = "";
	public String brandconfig_quote_email_sub = "";
	public String quote_emp_email = "";
	public String config_email_enable = "0", msg = "";
	public String contact_email1 = "";
	public String emp_id = "0", quote_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "";
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(PadQuotes(request.getParameter("comp_id")));
			emp_uuid = PadQuotes(request.getParameter("emp_uuid"));
			CheckAppSession(emp_uuid, comp_id, request);
			emp_id = CNumeric(session.getAttribute("emp_id") + "");
			quote_id = CNumeric(PadQuotes(request.getParameter("quote_id")));
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			if (!emp_id.equals("0")) {
				sendmail(request, response);
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void sendmail(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Veh_Quote_Email vqe = new Veh_Quote_Email();
		vqe.quote_id = quote_id;
		vqe.emp_id = emp_id;
		vqe.comp_id = comp_id;
		vqe.PopulateFields();
		comp_email_enable = vqe.comp_email_enable;
		config_email_enable = vqe.config_email_enable;
		brandconfig_quote_email_enable = vqe.brandconfig_quote_email_enable;
		// / quote_emp_email = obj.quote_emp_email;
		contact_email1 = vqe.contact_email1;
		brandconfig_quote_email_format = vqe.brandconfig_quote_email_format;
		brandconfig_quote_email_sub = vqe.brandconfig_quote_email_sub;
		try {

			if (!quote_id.equals("0")) {

				if (comp_email_enable.equals("1")
						&& config_email_enable.equals("1")
						&& brandconfig_quote_email_enable.equals("1")
						// && !quote_emp_email.equals("")
						&& !contact_email1.equals("")
						&& !brandconfig_quote_email_format.equals("")
						&& !brandconfig_quote_email_sub.equals("")) {
					vqe.SendEmail();
					msg = msg + "Email Sent Successfully";

				}
				else {
					if (!comp_email_enable.equals("1")) {
						msg = msg + "<br>Email Option is Disabled!";
					}
					if (!config_email_enable.equals("1")) {
						msg = msg + "<br>Email Gateway is Disabled!";
					}
					// if (quote_emp_email.equals("")) {
					// msg = msg + "<br>Sales Consultant Email is Blank!";
					// }
					if (contact_email1.equals("")) {
						msg = msg + "<br>Contact Email is Blank!";
					}
					if (!brandconfig_quote_email_enable.equals("1")) {
						msg = msg + "<br>Quote Email Option is Disabled!";
					}
					if (brandconfig_quote_email_format.equals("")) {
						msg = msg + "<br>Quote Email Format is Blank";
					}
					if (brandconfig_quote_email_sub.equals("")) {
						msg = msg + "<br>Quote Email Subject is Blank";
					}

				}

			}
			response.setContentType("text/html");
			// response.getWriter().write(msg);

		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}

	}

}
