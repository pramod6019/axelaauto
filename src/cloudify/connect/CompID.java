package cloudify.connect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CompID {

	public static String comp_id = "";

	// static{
	// setcompid(request, response);
	// }

	public static void setcompid(HttpServletRequest request,
			HttpServletResponse response) {
		Connect ct = new Connect();
		try {
			String servername = request.getServerName() + "";

			if (servername.equals("localhost")) {
				comp_id = "1000";
			} else if (servername.equals("127.0.0.1")) {
				comp_id = "1001";
			}
		} catch (Exception ex) {
			// SOPError("AxelaAuto===" + this.getClass().getName());
			ct.SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName()
					+ " : " + ex);
		}
	}
	public String getcompid() {
		return comp_id + "";
	}
}
