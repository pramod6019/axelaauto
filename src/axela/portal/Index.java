package axela.portal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.util.codec.binary.Base64;

import cloudify.connect.Connect;

public class Index extends Connect {

	public String action = "";
	public String msg = "";
	public String signinid = "0";
	public String password = "";
	public String StrSql = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String user_jsessionid = "0";
	public int signincount = 0;
	public String captcha = "";
	public String code = "";
	public String emp_sales = "";
	public String uuid = "", check = "";
	public String remember = "";
	public String credentials1 = "", credentials2 = "", credentials3 = "";
	public static final String CHARSET = "ISO-8859-1";
	public String enCodeUid = "";
	public String deCodeUid = "";
	public String enCodePass = "";
	public String deCodePass = "";
	public String enCode_comp_id = "";
	public String deCode_comp_id = "";
	public Index() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			getcompsession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			user_jsessionid = PadQuotes(session.getId());
			action = PadQuotes(request.getParameter("action"));
			msg = PadQuotes(request.getParameter("msg"));
			remember = PadQuotes(request.getParameter("remember"));
			if (session.getAttribute("signincount") != null) {
				// signincount = Integer.parseInt(GetSession("signincount",
				// request).toString());
				signincount = Integer.parseInt(session.getAttribute("signincount").toString());
				// SOP("signincount===" + signincount);
			}

			if (session.getAttribute("captcha") != null) {
				captcha = session.getAttribute("captcha") + "";
			}
			code = PadQuotes(request.getParameter("code"));
			// SOP("captcha===" + captcha);
			// SOP("code===" + code);
			// //code = (String) request.getParameter("code");
			GetValues(request, response);
			saveCookies(signinid, password, request, response);

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void saveCookies(String uuid, String check, HttpServletRequest request, HttpServletResponse response) {
		try {

			Cookie[] cookies = request.getCookies();

			for (int i = 0; i < cookies.length; i++) {

				if (cookies[i].getName().equals("uuid")) {
					if (cookies[i].getValue() != ""
							&& cookies[i].getValue().length() > 0) {
						deCodeUid = new String(Base64.decodeBase64(cookies[i]
								.getValue().getBytes(CHARSET)));
						credentials1 = deCodeUid;

					}
				}
				if (cookies[i].getName().equals("check")) {
					if (cookies[i].getValue() != ""
							&& cookies[i].getValue().length() > 0) {

						deCodePass = new String(Base64.decodeBase64(cookies[i]
								.getValue().getBytes(CHARSET)));

						credentials2 = deCodePass;

					}
				}

				if (cookies[i].getName().equals("comp_id")) {
					if (cookies[i].getValue() != ""
							&& cookies[i].getValue().length() > 0) {

						deCode_comp_id = new String(Base64.decodeBase64(cookies[i]
								.getValue().getBytes(CHARSET)));

						credentials3 = deCode_comp_id;

					}
				}

			}

			if (credentials1 != "" && credentials2 != "" && credentials3 != "" && comp_id.equals(credentials3)) {
				signinid = credentials1;
				password = credentials2;
				comp_id = credentials3;

				Signin(signinid, password, "", comp_id, request, response);
				if (msg.equals("") && code.equalsIgnoreCase(captcha)) {
					request.getSession().setAttribute("signincount", null);
					response.sendRedirect(response
							.encodeRedirectURL("home.jsp"));
				}
				if (msg.equals("ERROR")) {
					response.sendRedirect(response
							.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
				} else if (!msg.equals("")) {
					msg = " " + msg;
					signincount++;
					request.getSession().setAttribute("signincount",
							signincount);
				}
			}

			if ("Sign In".equals(action)) {

				Signin(signinid, password, "", comp_id, request, response);
				if (remember.equals("1")) {

					enCodeUid = new String(Base64.encodeBase64(uuid.getBytes(CHARSET)));
					enCodePass = new String(Base64.encodeBase64(check.getBytes(CHARSET)));
					enCode_comp_id = new String(Base64.encodeBase64(comp_id.getBytes(CHARSET)));

					Cookie c1 = new Cookie("uuid", enCodeUid);
					c1.setMaxAge(365 * 24 * 60 * 60);
					response.addCookie(c1);
					Cookie c2 = new Cookie("check", enCodePass);
					c2.setMaxAge(365 * 24 * 60 * 60);
					response.addCookie(c2);
					Cookie c3 = new Cookie("comp_id", enCode_comp_id);
					c3.setMaxAge(365 * 24 * 60 * 60);
					response.addCookie(c3);
				}

				if (msg.equals("ERROR")) {
					response.sendRedirect(response.encodeRedirectURL("error.jsp?msg=Access denied. Please contact system administrator!"));
				} else if (!msg.equals("")) {
					msg = " " + msg;
					signincount++;
					request.getSession().setAttribute("signincount", signincount);
					// request.getSession().setAttribute("signincount",
					// signincount);
				} else if (msg.equals("")) {
					request.getSession().setAttribute("signincount", null);
					// request.getSession().setAttribute("signincount", null);
					if (emp_sales.equals("1")) {
						response.sendRedirect(response.encodeRedirectURL("../sales/index.jsp"));
					} else {
						response.sendRedirect(response.encodeRedirectURL("home.jsp"));
					}
				}
			}

		} catch (UnsupportedEncodingException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		} catch (IOException ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			ex.printStackTrace();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	protected void GetValues(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		signinid = PadQuotes(request.getParameter("userid"));
		password = PadQuotes(request.getParameter("password"));
	}

	protected void CheckFields() {
		msg = "";
		if (signinid.equals("") && password.equals("")) {
			msg = "Sign In Id & Password is blank!";
		} else if (signinid.equals("")) {
			msg = "Sign In Id is blank!";
		} else if (password.equals("")) {
			msg = "Password is blank!";
		}
		if (!captcha.equals("") && !code.equals("") && signincount > 2) {
			if (captcha.equalsIgnoreCase(code)) {
				// SOP("matching....");
			} else {
				msg += "<br>Incorrect Captcha Code!";
				// SOP("not matching....");
			}
		}
		if (!captcha.equals("") && code.equals("") && signincount > 2) {
			msg += "<br>Please Enter Captcha Code!";
		}
	}

	protected String Signin(String signinid, String password, String admin, String comp_id,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		CheckFields();
		String loginmsg = "";
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			StrSql = "SELECT emp_id, emp_email1, emp_upass, emp_branch_id, emp_role_id,"
					+ " emp_all_exe, emp_all_branches, emp_clicktocall, emp_routeno, emp_callerid, emp_recperpage, emp_timeout, emp_theme_id, emp_ip_access,"
					+ " emp_sales"
					+ " FROM " + compdb(comp_id) + "axela_emp, " + compdb(comp_id) + "axela_comp"
					+ " WHERE comp_active = 1"
					+ " AND emp_active = 1 ";
			if (AppRun().equals("1") && !admin.equals("yes")) {
				StrSql += " AND emp_id != 1";
			}
			StrSql += " AND emp_email1 = ? "
					+ " AND emp_upass = ? ";

			conn = connectDB();
			pstmt = conn.prepareStatement(StrSql);

			pstmt.setString(1, signinid);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.isBeforeFirst()) {
				while (rs.next()) {
					emp_id = rs.getString("emp_id");
					uuid = rs.getString("emp_email1");
					check = rs.getString("emp_upass");
					if (!password.equals(rs.getString("emp_upass"))) {
						msg = "Please check Sign In Id and password!";
					}
					if (!rs.getString("emp_ip_access").equals("")) {
						String IP = getIP(request);
						String[] str = new String[10];
						if (rs.getString("emp_ip_access").contains(", ")) {
							str = rs.getString("emp_ip_access").split(", ");
							for (int j = 0; j < str.length; j++) {
								if (!IP.equals(str[j])) {
									msg = "Access denied!";
								} else {
									msg = "";
									break;
								}
							}
						} else if (!IP.equals(rs.getString("emp_ip_access"))) {
							msg = "Access denied!";
						}
					}
					// test
					msg = "";

					if (msg.equals("")) {
						emp_sales = rs.getString("emp_sales");
						AssignSession(comp_id, rs.getString("emp_id"), rs.getString("emp_branch_id"), rs.getString("emp_role_id"),
								rs.getString("emp_all_exe"), rs.getString("emp_all_branches"),
								rs.getString("emp_clicktocall"), rs.getString("emp_routeno"), rs.getString("emp_callerid"),
								rs.getString("emp_recperpage"), rs.getInt("emp_timeout"), request);
						Cookie cookie = new Cookie("axelatheme", rs.getString("emp_theme_id"));
						cookie.setMaxAge(60 * 60 * 24 * 30 * 60);
						cookie.setPath(request.getContextPath());
						response.addCookie(cookie);
						if (AppRun().equals("1")) {

							updateQuery("UPDATE " + compdb(comp_id) + "axela_emp_log"
									+ " SET"
									+ " log_signout_time = " + ToLongDate(kknow()) + ""
									+ " WHERE log_emp_id = " + emp_id + ""
									+ " AND log_signout_time = ''");

							updateQuery("INSERT INTO " + compdb(comp_id) + "axela_emp_log"
									+ " (log_emp_id,"
									+ " log_session_id,"
									+ " log_remote_host,"
									+ " log_remote_agent,"
									+ " log_attemptcount,"
									+ " log_signin_time, "
									+ " log_signout_time)"
									+ " VALUES"
									+ " (" + emp_id + ","
									+ " '" + user_jsessionid + "',"
									+ " '" + request.getRemoteHost() + "',"
									+ " '" + request.getHeader("User-Agent") + "',"
									+ " " + signincount + ","
									+ " " + ToLongDate(kknow()) + ","
									+ " '')");

							updateQuery("DELETE FROM " + compdb(comp_id) + "axela_emp_user"
									+ " WHERE user_emp_id = " + emp_id);

							updateQuery("INSERT INTO " + compdb(comp_id) + "axela_emp_user"
									+ " (user_jsessionid,"
									+ " user_emp_id,"
									+ " user_logintime,"
									+ " user_ip,"
									+ " user_agent)"
									+ " VALUES"
									+ " ('" + user_jsessionid + "',"
									+ " " + rs.getString("emp_id") + ","
									+ " '" + ToLongDate(kknow()) + "',"
									+ " '" + request.getRemoteHost() + "',"
									+ " '" + request.getHeader("User-Agent") + "')");
						}
						loginmsg = "Valid";
					} else {
						loginmsg = msg;
					}
				}
			} else {
				msg = "The Sign In Id or password you entered is incorrect!";
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			msg = "ERROR";
		} finally {
			rs.close();
			if (pstmt != null && !pstmt.isClosed()) {
				pstmt.close();
			}
			if (conn != null && !conn.isClosed()) {
				conn.close();
			}
			// ////////
		}
		return loginmsg;
	}
}
