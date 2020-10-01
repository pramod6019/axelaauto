/* Ved Prakash (17th May 2013) */
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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

public class TestClientEnquiry_DashFollowUp {

	public static void main(String[] args) {

		try {
			Connect ct = new Connect();
			URL url = new URL("http://localhost:7030/axelaauto-dc/mobile/sales/enquiry-dash-followup");
			// URL url = new URL(ct.WSUrl() + "enquiry-dash-followup");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			String input = "{\"enquiry_id\":\"381\",\"emp_id\":\"2\"}";
			// String input = "{\"enquiry_id\":\"260\",\"emp_id\":\"1\",\"submitB\":\"yes\","
			// + " \"followup_desc\":\"okay...\",\"followup_time\":\"08/13/2013\",\"followup_followuptype_id\":\"1\"}";
			// String input = "{\"enquiry_id\":\"381\",\"emp_id\":\"1\",\"delete\":\"yes\","
			// + " \"followup_id\":\"381\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
				try {
					JSONObject obj = new JSONObject(output);
					JSONArray data = null;
					if (!obj.isNull("listdata")) {
						data = (JSONArray) obj.get("listdata");
						for (int i = 0; i < data.length(); i++) {
							JSONObject oppr = data.getJSONObject(i);
							// String activity_id = oppr.getString("activity_id");
							// SOP("activity_id----" + activity_id);
						}
					}
					if (!obj.isNull("msg")) {
						obj.get("msg");
					}
				} catch (JSONException ex) {
					Logger.getLogger(TestClientEnquiry_DashFollowUp.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
