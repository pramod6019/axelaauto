/* Ved Prakash (8th May 2013) */
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

public class TestClientEnquiry_Dash {

	public static void main(String[] args) {

		try {
			Connect ct = new Connect();
			URL url = new URL("http://localhost:7030/axelaauto-dc/mobile/sales/enquiry-dash");
			// URL url = new URL(ct.WSUrl() + "enquiry-dash");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			// String input = "{\"enquiry_id\":\"218\",\"emp_id\":\"32\",\"update\":\"\"}";
			// String input = "{\"enquiry_id\":\"647\",\"emp_id\":\"11\",\"update\":\"yes\","
			// + " \"name\":\"sp_enquiry_model_id\",\"value\":\"1\","
			// + " \"name1\":\"\",\"value1\":\"\"}";
			String input = "{\"enquiry_id\":\"607\",\"emp_id\":\"11\",\"update\":\"yes\","
					+ " \"name\":\"sp_enquiry_status_id\",\"value\":\"1\","
					+ " \"name1\":\"txt_enquiry_status_desc\",\"value1\":\"asdf\"}";
			// String input = "{\"enquiry_id\":\"607\",\"emp_id\":\"1\",\"update\":\"yes\","
			// + " \"name\":\"txt_enquiry_close_date\",\"value\":\"14/13/2013\","
			// + " \"name1\":\"txt_enquiry_date\",\"value1\":\"15/07/2013\"}";

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
					Logger.getLogger(TestClientEnquiry_Dash.class.getName()).log(Level.SEVERE, null, ex);
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
