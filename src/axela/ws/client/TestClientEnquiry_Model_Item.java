/* Ved Prakash (12th Sept 2013) */
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

public class TestClientEnquiry_Model_Item {

	public static void main(String[] args) {

		try {
			Connect ct = new Connect();
			URL url = new URL("http://localhost:7030/axelaauto-dc/mobile/sales/model-item");
			// URL url = new URL(ct.WSUrl() + "enquiry-dash");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			String input = "{\"model_id\":\"1\"}";

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
				System.out.println(ct.JSONPadQuotes(output));
				try {
					JSONObject obj = new JSONObject(output);
					// JSONArray data = null;
					// if (!obj.isNull("listdata")) {
					// data = (JSONArray) obj.get("listdata");
					// for (int i = 0; i < data.length(); i++) {
					// JSONObject oppr = data.getJSONObject(i);
					// }
					// }
					if (!obj.isNull("msg")) {
						obj.get("msg");
					}
				} catch (JSONException ex) {
					Logger.getLogger(TestClientEnquiry_Model_Item.class.getName()).log(Level.SEVERE, null, ex);
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
