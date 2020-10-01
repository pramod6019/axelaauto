package cloudify.connect;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONObject;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

public class Ecover_WS extends Connect {
	public String comp_id = "0";
	public String emp_id = "0";
	public String line = "";

	public String WSRequest(JSONObject input, String uri, HttpServletRequest request) {
		try {
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			if (!comp_id.equals("0") && !emp_id.equals("0")) {
				// input.put("emp_id", emp_id);
				input.put("comp_id", comp_id);
				StringBuilder Str = new StringBuilder();
				URL url = new URL(WSRunnerUrl() + uri);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setUseCaches(false);
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Content-Type", "application/json");
				OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
				wr.write(input.toString());
				wr.flush();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					Str.append(line + "\n");
				}
				conn.disconnect();
				wr.close();
				br.close();
				return Str.toString();
			}
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
		return "";
	}

	public String WSMultiPartRequest(String uri, String formdataparam, InputStream fileInputStream, String filefield) throws ParseException, IOException {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;
		String twoHyphens = "--";
		String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
		String lineEnd = "\r\n";
		String result = "";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;

		try {
			URL url = new URL(WSRunnerUrl() + uri);
			connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + filefield + "\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);

			// Upload InputStream
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			outputStream.writeBytes(lineEnd);

			// Upload POST Data
			String[] posts = formdataparam.split("&");
			int max = posts.length;
			for (int i = 0; i < max; i++) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				String[] kv = posts[i].split("=");
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
				outputStream.writeBytes("Content-Type: text/plain" + lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(kv[1]);
				outputStream.writeBytes(lineEnd);
			}
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			inputStream = connection.getInputStream();
			fileInputStream.close();
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			return result;
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			return "";
		}
	}

	public String WSRunnerUrl() {
		String url = "";
		if (AppRun().equals("1")) {
			if (comp_id.equals("1000")) {
				url = "http://demo.ecover.in/ecover/ws/ecover/";
			} else if (comp_id.equals("1009")) {
				url = "https://ddmotors.ecover.in/ecover/ws/ecover/";
			} else if (comp_id.equals("1011")) {
				url = "http://indel.ecover.in/ecover/ws/ecover/";
			}
		} else {
			url = "http://localhost:7090/ecover/ws/ecover/";
		}
		return url;
	}
}
