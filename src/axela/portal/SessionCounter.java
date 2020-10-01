package axela.portal;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import cloudify.connect.Connect;

public class SessionCounter implements HttpSessionListener {

	private static int activesessions = 0;
	Connect ct = new Connect();
	String user_id = "";
	public String comp_id = "0";
	String channeluser_id = "";
	String emp_id = "", StrSql = "";

	public void sessionCreated(HttpSessionEvent se) {
		activesessions++;
	}

	public void sessionDestroyed(HttpSessionEvent se) {
		try {
			if (activesessions > 0) {
				activesessions--;
			}

			// comp_id = ct.CNumeric(ct.GetSession("comp_id", request));
			HttpSession session = se.getSession();
			// SOPError("session==" + session.getId());
			if (session.getAttribute("sessionMap") != null) {
				Map getMap = (Map) session.getAttribute("sessionMap");
				comp_id = ct.CNumeric(getMap.get("comp_id") + "");
				emp_id = ct.CNumeric(getMap.get("emp_id") + "");
				if (!comp_id.equals("0") && !emp_id.equals("0")) {
					StrSql = "Update " + ct.compdb(comp_id) + "axela_emp_log set "
							+ " log_signout_time=" + ct.ToLongDate(ct.kknow()) + ""
							+ " where log_emp_id=" + emp_id + " and log_session_id='" + session.getId() + "' and log_signout_time=''";
					// SOPError("StrSql==" + StrSql);
					ct.updateQuery(StrSql);

					ct.updateQuery("delete from " + ct.compdb(comp_id) + "axela_emp_user where user_jsessionid = '" + session.getId() + "'");
				}
			}

		} catch (Exception ex) {
			ct.SOPError("Axelaauto===" + this.getClass().getName());
			ct.SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public static int getActiveSessions() {
		return activesessions;
	}
}
