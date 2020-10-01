package axela.ws.api;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.codec.binary.Base64;

import cloudify.connect.Connect;
public class SecurityFilter extends Connect implements javax.servlet.Filter {

	public String authCredentials = "";
	public String StrSql = "";
	public static final String AUTHENTICATION_HEADER = "Authorization";
	public String comp_id = "0";
	public String emp_id = "0";
	public Connection conn = null;
	public PreparedStatement pstmt = null;
	public ResultSet rs = null;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterchain) throws IOException, ServletException {
		try {
			HttpServletRequest req = (HttpServletRequest) request;
			HttpServletResponse httpServletResponse = (HttpServletResponse) response;
			comp_id = "1009";// CNumeric(PadQuotes(req.getHeader("comp_id")));
			if (!comp_id.equals("0")) {
				HttpServletRequest httpServletRequest = (HttpServletRequest) request;
				if (httpServletRequest.getRequestURI().equals("/axelaauto/ws/api1.0/test")) { // test url doest not need any authentication.
					filterchain.doFilter(request, response);
				}
				String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);
				if (Authenticate(authCredentials)) {
					request.setAttribute("comp_id", comp_id);
					request.setAttribute("emp_id", emp_id);
					filterchain.doFilter(request, response);
				} else {
					if (response instanceof HttpServletResponse) {
						httpServletResponse.setStatus(401);
						httpServletResponse.setHeader("status_message", "User Name or Password is incorrect!");
					}
				}
			} else {
				httpServletResponse.setStatus(401);
				httpServletResponse.setHeader("status_message", "comp_id cannot be empty!");
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-API======" + this.getClass().getName());
			SOPError("Axelaauto-API===== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public boolean Authenticate(String authCredentials) throws SQLException {
		boolean authenticationStatus = false;
		try {
			if (authCredentials.equals(""))
				return false;
			// header value format will be "Basic encodedstring" for Basic
			// authentication. Example "Basic YWRtaW46YWRtaW4="
			final String encodedUserPassword = authCredentials.replaceFirst("Basic"
					+ " ", "");
			String usernameAndPassword = null;
			try {
				byte[] decodedBytes = Base64.decodeBase64(
						encodedUserPassword);
				usernameAndPassword = new String(decodedBytes, "UTF-8");
			} catch (IOException e) {
				e.printStackTrace();
			}
			final StringTokenizer tokenizer = new StringTokenizer(
					usernameAndPassword, ":");
			final String emp_email1 = tokenizer.nextToken();
			final String emp_upass = tokenizer.nextToken();
			StrSql = "SELECT emp_id, emp_email1, emp_upass"
					+ " FROM  " + compdb(comp_id) + "axela_emp "
					+ " WHERE 1=1"
					+ " AND emp_active = 1"
					+ " AND emp_email1 = ? "
					+ " AND emp_upass = ? ";
			conn = connectDB();
			pstmt = conn.prepareStatement(StrSql);
			pstmt.setString(1, emp_email1);
			pstmt.setString(2, emp_upass);
			rs = pstmt.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					if (emp_email1.equals(rs.getString("emp_email1")) && emp_upass.equals(rs.getString("emp_upass"))) {
						emp_id = rs.getString("emp_id");
						authenticationStatus = true;
					}
				}
			}
			rs.close();
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto-API======" + this.getClass().getName());
			SOPError("Axelaauto-API===== " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return authenticationStatus;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}
}
