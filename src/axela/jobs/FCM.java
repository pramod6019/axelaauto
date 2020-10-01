package axela.jobs;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

//import org.codehaus.jettison.json.JSONObject;
import cloudify.connect.Connect;

public class FCM extends Connect {

	public static final String GOOGLE_SERVER_KEY = "AAAAxO4bYsc:APA91bHKL9tA3i6n7Vj_QO-lQprRaBBCUSXMhDzwWzajy4Fy4H4XfZsQGGXTyo5BSQDTW0kmM6JgFL1rIaWx-Bf-znX-ONTab2IJjnnts-8vvdCjV__G2tjvj-zVvv0W2flyqkSuRL8o";
	public static final String MESSAGE = "message";
	public static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	public String title = "";
	public String msg = "";
	public String filename = "";
	public String invalidToken = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws InterruptedException {
		String msg = "Enquiry ID: 1122\n"
				+ "Follow-up Time: 12:00\n"
				+ "Contact: Sanjay\n"
				+ "Mobile: 988989899\n";
		SendPushNotification("fV21wJiQkF4:APA91bECao0aZvPhIaUgmZzKwEaJ7Xfx9LTiCcIaCVmMgWS_WuoQNvWaOaW0HC2y7y_ok4v4alBEIqqgLswZQbVz11qrIrRZjM3X3k4z944ZwG_UZT_eioAIUi62n7LkDQlg-WYKBS2Z", "android",
				"Enquiry Escalation", msg);
	}

	public void SendPushNotification(final String emp_device_fcmtoken, final String emp_device_os, final String title, final String msg) throws InterruptedException {
		Thread thread = new Thread(new Runnable() {
			@SuppressWarnings("unchecked")
			public void run() {
				try {
					URL url = new URL(FCM_URL);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setUseCaches(false);
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Authorization", "key=" + GOOGLE_SERVER_KEY);
					conn.setRequestProperty("Content-Type", "application/json");
					JSONObject json = new JSONObject();
					json.put("to", emp_device_fcmtoken.trim());
					JSONObject info = new JSONObject();
					info.put("title", title);
					if (emp_device_os.equals("android")) {
						info.put("message", msg);
						json.put("data", info);
					} else if (emp_device_os.equals("ios")) {
						info.put("body", msg);
						json.put("notification", info);
					}
					OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
					wr.write(json.toString());
					wr.flush();
					conn.getInputStream();
					conn.disconnect();
					wr.close();
				} catch (Exception ex) {
					SOPError("Axelaauto-App== " + this.getClass().getName());
					SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ":" + ex);
				}
			}
		});
		thread.start();
	}
}
