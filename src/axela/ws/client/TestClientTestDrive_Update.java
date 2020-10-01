/* Divya 26 march 2014 */
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

import cloudify.connect.Connect;

public class TestClientTestDrive_Update {

	public static void main(String[] args) {
		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			Connect ct = new Connect();
			url = new URL("http://localhost:7030/axelaauto-dc/mobile/sales/testdrive-update");
			// url = new URL(ct.WSUrl() + "enquiry-add");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			// String input = "{\"emp_id\":\"2\",\"add\":\"yes\"}";
			String input = "{\"emp_id\":\"2\",\"add\":\"yes\",\"addB\":\"yes\","
					+ " \"testdrive_type\":\"1\",\"testdrive_veh_id\":\"12\",\"testdrive_location_id\":\"1\","
					+ " \"testdrive_time\":\"18/05/2013 14:20\",\"testdrive_confirmed\":\"1\",\"testdrive_notes\":\"\"}";

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
			// while ((output = br.readLine()) != null) {
			// SOPError(output);
			// try {
			// JSONObject obj = new JSONObject(output);
			// JSONArray data = null;
			// if (!obj.isNull("listdata")) {
			// data = (JSONArray) obj.get("listdata");
			// for (int i = 0; i < data.length(); i++) {
			// JSONObject oppr = data.getJSONObject(i);
			// // String activity_id = oppr.getString("activity_id");
			// // SOP("activity_id----" + activity_id);
			// }
			// }
			// if (!obj.isNull("msg")) {
			// obj.get("msg");
			// }
			// } catch (JSONException ex) {
			// Logger.getLogger(TestClientActivity_List.class.getName()).log(Level.SEVERE, null, ex);
			// }
			// }
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
				Logger.getLogger(TestClientTestDrive_Update.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
