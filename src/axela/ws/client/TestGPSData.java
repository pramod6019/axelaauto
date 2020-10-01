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

public class TestGPSData {

	public static void main(String[] args) {
		URL url = null;
		HttpURLConnection conn = null;
		BufferedReader br = null;
		try {
			Connect ct = new Connect();
			url = new URL(ct.WSUrl() + "sales/gps");
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json");

			String input = "{\"comp_id\":\"1\",\"gps_emp_id\":\"34\","
					+ "\"gps_latitude\":\"12.96\",\"gps_longitude\":\"77.66\"}";

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
			br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));
			String output = "";
			while ((output = br.readLine()) != null) {
				System.out.println("output = " + output);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.disconnect();
				br.close();
			} catch (IOException ex) {
				Logger.getLogger(TestGPSData.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}
