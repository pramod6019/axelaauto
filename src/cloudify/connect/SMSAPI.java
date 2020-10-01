package cloudify.connect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
public class SMSAPI
{
	Connect ct = new Connect();
	public void sendSMS(final String url, final String toList, final String msg)
	{
		Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					String urldata = url.replace("[MOBILENO]", URLEncoder.encode(toList, "UTF-8"));
					urldata = urldata.replace("[MESSAGE]", URLEncoder.encode(msg, "UTF-8"));
					// String data = "http://enterprise.smsgupshup.com/GatewayAPI/rest?method=sendMessage" +
					// "&userid=2000028600" + // your loginId
					// "&password=" + URLEncoder.encode("xxxxx", "UTF-8") + // your password
					// "&msg=" + URLEncoder.encode(msg, "UTF-8") +
					// "&send_to=" + URLEncoder.encode(toList, "UTF-8") + // a valid 10 + 2 digit phone no.
					// "&mask=" + url + // your brand
					// "&v=1.1";
					URL url = new URL(urldata);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setDoOutput(true);
					conn.setDoInput(true);
					conn.setUseCaches(false);
					conn.connect();
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					String line;
					StringBuilder buffer = new StringBuilder();
					while ((line = rd.readLine()) != null)
					{
						buffer.append(line).append("\n");
					}
					ct.SOPInfo(buffer.toString());
					rd.close();
					conn.disconnect();

				} catch (Exception e) {
					ct.SOPInfo("sendsms=fail=" + toList + "=" + e.getMessage());

				}
			}
		});
		t.start();
	}
	public static void main(String[] args)
	{
		SMSAPI vd = new SMSAPI();
		vd.sendSMS("MyCampus", "919845011519", "Test Message qqqqqqqqqq");
	}
}