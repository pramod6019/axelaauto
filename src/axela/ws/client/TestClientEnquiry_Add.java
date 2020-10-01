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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

public class TestClientEnquiry_Add {

	public static void main(String[] args) {
		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			Connect ct = new Connect();
			url = new URL("http://localhost:7030/axelaauto-dc/mobile/sales/enquiry-add");
			// url = new URL(ct.WSUrl() + "enquiry-add");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");
			String input = "{\"emp_id\":\"2\",\"add\":\"yes\"}";
			// String input = "{\"emp_id\":\"37\",\"add\":\"yes\",\"addB\":\"yes\","
			// + " \"customer_name\":\"Kunal Gaurav\",\"enquiry_title_id\":\"1\",\"enquiry_fname\":\"\","
			// + " \"enquiry_lname\":\"\",\"enquiry_jobtitle\":\"manager\",\"contact_mobile1\":\"9524597212\","
			// + " \"contact_mobile2\":\"\",\"contact_email1\":\"\",\"contact_email2\":\"\","
			// + " \"contact_address\":\"Dhaka\",\"contact_city_id\":\"5\",\"contact_state_id\":\"2\","
			// + " \"contact_pincode\":\"845414\",\"enquirydate\":\"18/05/2013\",\"enquiry_close_date\":\"31/05/2013\","
			// + " \"branch_id\":\"2\",\"enquiry_desc\":\"xyz\",\"model_id\":\"2\",\"item_id\":\"5\","
			// + " \"team_id\":\"0\",\"enquiry_emp_id\":\"2\",\"soe_id\":\"2\",\"sob_id\":\"2\","
			// + " \"enquiry_campaign_id\":\"1\",\"enquiry_refno\":\"789\"}";

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
					Logger.getLogger(TestClientActivity_List.class.getName()).log(Level.SEVERE, null, ex);
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
				Logger.getLogger(TestClientEnquiry_Add.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
