package axela.axelaauto_app;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import axela.portal.Header;
import axela.sales.Veh_Quote_Email;
import axela.sales.Veh_Quote_Print;
import axela.sales.Veh_Quote_Print_Joshi;
import cloudify.connect.Connect;

public class App_Email_Quote extends Connect {
	public String comp_email_enable = "0";
	public String brandconfig_quote_email_enable = "";
	public String brandconfig_quote_email_format = "";
	public String brandconfig_quote_email_sub = "", attachment = "", insurcompattachment = "";
	public String quote_emp_email = "";
	public String config_email_enable = "0", msg = "", branch_email1 = "";
	public String contact_email1 = "";
	public String emp_id = "0", quote_id = "0", brand_id = "0";
	public String comp_id = "0";
	public String emp_uuid = "";
	public String inscomp_value = "";
	public String inscomp_title = "";
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
		Veh_Quote_Email obj = new Veh_Quote_Email();
		obj.quote_id = quote_id;
		obj.emp_id = emp_id;
		obj.comp_id = comp_id;
		obj.PopulateFields();
		comp_email_enable = obj.comp_email_enable;
		config_email_enable = obj.config_email_enable;
		brandconfig_quote_email_enable = obj.brandconfig_quote_email_enable;
		// quote_emp_email = obj.quote_emp_email;
		contact_email1 = obj.contact_email1;
		brandconfig_quote_email_format = obj.brandconfig_quote_email_format;
		brandconfig_quote_email_sub = obj.brandconfig_quote_email_sub;
		branch_email1 = obj.branch_email1;
		try {
			if (comp_email_enable.equals("1")
					&& config_email_enable.equals("1")
					&& !branch_email1.equals("")
					&& brandconfig_quote_email_enable.equals("1")
					&& !contact_email1.equals("")
					&& !brandconfig_quote_email_format.equals("")
					&& !brandconfig_quote_email_sub.equals("")) {
				attachment = CachePath(comp_id) + "Quote_" + quote_id + ".pdf,Quote_" + quote_id + ".pdf";
				if (!obj.inscomp_value.equals("") && !obj.inscomp_title.equals("")) {
					insurcompattachment = InsurCompDocPath(comp_id) + obj.inscomp_value + "," + obj.inscomp_title + "" + fileext(obj.inscomp_value);
					attachment = attachment + ";" + obj.insurcompattachment;
				}
				if (comp_id.equals("1017")) {
					Veh_Quote_Print_Joshi joshiquote = new Veh_Quote_Print_Joshi();
					joshiquote.comp_id = comp_id;
					joshiquote.quote_id = quote_id;
					attachment = joshiquote.GeneratePDFQuote(quote_id, request, response);
				} else {
					Veh_Quote_Print quoteprint = new Veh_Quote_Print();
					quoteprint.comp_id = comp_id;
					quoteprint.brand_id = brand_id;
					attachment = quoteprint.GeneratePDFQuote(quote_id, request, response);
				}
				obj.attachment = attachment;
				obj.SendEmail();
				msg = msg + "Email Sent Successfully";
			} else {
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
			response.setContentType("text/html");
			response.getWriter().write(msg);
		} catch (Exception ex) {
			SOPError("Axelaauto-App=== " + this.getClass().getName());
			SOPError("Axelaauto-App=== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
