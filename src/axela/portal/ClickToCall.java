package axela.portal;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import cloudify.connect.Connect;

@SuppressWarnings("deprecation")
public class ClickToCall {

	public String StrSql = "";
	public String StrHTML = "";
	public String callno = "";
	public String emp_clicktocall = "";
	public String emp_routeno = "";
	public String emp_callerid = "";
	Connect ct = new Connect();
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			HttpSession session = request.getSession(true);
			// comp_id = CNumeric(GetSession("comp_id", request));
			if (!(ct.GetSession("emp_clicktocall", request).equals(""))) {
				emp_clicktocall = ct.GetSession("emp_clicktocall", request);
				emp_routeno = ct.GetSession("emp_routeno", request);
				emp_callerid = ct.GetSession("emp_callerid", request);
				callno = ct.PadQuotes(request.getParameter("callno"));
				callno = "+" + callno;
				// SOP("emp_callerid===" + emp_callerid);
				if (emp_clicktocall.equals("1") && !emp_callerid.equals("")) {
					if (!callno.equals("") && (callno.length() == 13 || callno.length() == 14)) {
						StrHTML = "Calling " + callno + "...";
						CallServiceProvider(emp_routeno, emp_callerid, callno);
					} else {
						StrHTML = "<font color=red>Invalid No.!</font>";
					}
				} else {
					StrHTML = "<font color=red>Access Denied for Click to Call!</font>";
				}
			}

		} catch (Exception ex) {
			// SOPError("Axelaauto===" + this.getClass().getName());
			// SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void CallServiceProvider(String fromno, String caller_id, String tono) throws Exception {
		SSLContext sslContext = SSLContext.getInstance("SSL");

		// set up a TrustManager that trusts everything
		sslContext.init(null, new TrustManager[]{new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				// SOPError("getAcceptedIssuers =============");
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs,
					String authType) {
				// SOPError("checkClientTrusted =============");
			}

			public void checkServerTrusted(X509Certificate[] certs,
					String authType) {
				// SOPError("checkServerTrusted =============");
			}

			public boolean isClientTrusted(X509Certificate[] arg0) {
				return false;
			}

			public boolean isServerTrusted(X509Certificate[] arg0) {
				return false;
			}
		}}, new SecureRandom());

		SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme httpsScheme = new Scheme("https", sf, 443);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);

		HttpParams params = new BasicHttpParams();
		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);

		DefaultHttpClient client = new DefaultHttpClient(cm, params);

		// Replace "<Exotel SID>" and "<Exotel Token>" with your SID and Token
		client.getCredentialsProvider().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new UsernamePasswordCredentials("ddmotors", "18a8e81ef7acccdccb1a75d9748cf6c7fe8ba034")
				);
		HttpPost post = new HttpPost("https://twilix.exotel.in/v1/Accounts/ddmotors/Calls/connect");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

		/*
		 * Replace the text enclosed in < > with your desired valuesThe options for CallType are "trans" for transactional call and "promo" for promotional call
		 */
		nameValuePairs.add(new BasicNameValuePair("From", fromno));
		nameValuePairs.add(new BasicNameValuePair("To", tono));
		nameValuePairs.add(new BasicNameValuePair("CallerId", caller_id));
		nameValuePairs.add(new BasicNameValuePair("CallType", "trans"));
		/*
		 * Optional params nameValuePairs.add(new BasicNameValuePair("TimeLimit", "<time-in-seconds>")); nameValuePairs.add(new BasicNameValuePair("TimeOut", "<time-in-seconds>"));
		 * nameValuePairs.add(new BasicNameValuePair("StatusCallback", "<http//: your company URL>"));
		 */

		post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = client.execute(post, responseHandler);
		// SOPError(response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		doPost(request, response);
	}
}
