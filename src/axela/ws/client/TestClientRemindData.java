/* Ved Prakash (20rd April or 2nd May 2013) */
package axela.ws.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

public class TestClientRemindData {

	public static void main(String[] args) {
		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			Connect ct = new Connect();
			url = new URL("http://localhost:7030/axelaauto-dc/mobile/sales/remind");
			// url = new URL(ct.WSUrl() + "remind");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = "{\"submitB\":\"Submit\",\"email_id\":\"admini@emax.in\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				output = ct.JSONPadQuotes(output);
				System.out.println(output);
				try {
					JSONObject obj = new JSONObject(output);
					if (!obj.isNull("msg")) {
						obj.get("msg");
					}
				} catch (JSONException ex) {
					Logger.getLogger(TestClientRemindData.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			conn.disconnect();
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.disconnect();
				br.close();
			} catch (IOException ex) {
				Logger.getLogger(TestClientRemindData.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
