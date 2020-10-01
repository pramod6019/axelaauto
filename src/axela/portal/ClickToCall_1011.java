package axela.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import cloudify.connect.Connect;

@SuppressWarnings("deprecation")
public class ClickToCall_1011 {

	public String comp_id = "";
	public String emp_id = "";
	public String StrSql = "";
	public String StrHTML = "";
	public String callno = "";
	public String emp_clicktocall = "";
	public String emp_clicktocall_username = "";
	public String emp_routeno = "";

	Connect ct = new Connect();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			comp_id = ct.CNumeric(ct.GetSession("comp_id", request));
			emp_id = ct.CNumeric(ct.GetSession("emp_id", request));

			if (!(ct.GetSession("emp_clicktocall", request).equals(""))) {
				emp_clicktocall = ct.GetSession("emp_clicktocall", request);
				callno = ct.PadQuotes(request.getParameter("callno"));
				callno = callno.substring(2);
				StrSql = " SELECT emp_clicktocall_username, emp_routeno"
						+ " FROM " + ct.compdb(comp_id) + "axela_emp"
						+ " WHERE emp_id = " + emp_id;
				// ct.SOP("StrSql------------" + StrSql);
				CachedRowSet crs = ct.processQuery(StrSql, 0);
				while (crs.next()) {
					emp_clicktocall_username = crs.getString("emp_clicktocall_username");
					emp_routeno = crs.getString("emp_routeno");
				}
				crs.close();

				StrHTML = "<div class=\"modal-header\">\n";
				StrHTML += "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\"></button>\n";
				StrHTML += "<h4 class=\"modal-title\" style=text-align:center>Click to Call</h4>\n";
				StrHTML += "</div>\n";
				StrHTML += "<div class=\"modal-body \">\n";
				StrHTML += "<div class=\"row\">\n";
				StrHTML += "<div class=\"col-md-12\" style=text-align:center>\n";
				ct.SOP("value==" + callno.length());
				// ct.SOP("emp_clicktocall====" + emp_clicktocall);
				// ct.SOP("value=callno=" + callno);
				if (emp_clicktocall.equals("1") && !emp_clicktocall_username.equals("")) {
					if (!callno.equals("") && callno.length() == 10) {
						StrHTML += "Calling " + callno + "...";
						CallServiceProvider();
					} else {
						StrHTML += "<font color=red>Invalid No.!</font>";
					}
				} else {
					StrHTML += "<font color=red>Access Denied for Click to Call!</font>";
				}
				StrHTML += "</div>\n";
				StrHTML += "</div>\n";
			}

		} catch (Exception ex) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CallServiceProvider() throws Exception {
		String url = "http://117.247.186.28/VOOVEREdge/CRMAPI/click2call.php?"
				+ "userid=" + emp_clicktocall_username
				+ "&exten=" + emp_routeno
				+ "&custno=" + callno
				+ "&crmuid=AxelaAuto100"
				+ "&calltype=SALES";

		HttpClient client = HttpClientBuilder.create().build();
		HttpGet request = new HttpGet(url);

		// add request header
		request.addHeader("User-Agent", "Mozilla/5.0");
		HttpResponse response = client.execute(request);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
