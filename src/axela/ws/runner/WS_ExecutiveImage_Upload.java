package axela.ws.runner;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

import org.codehaus.jettison.json.JSONObject;

import cloudify.connect.Connect;

import com.sun.jersey.core.header.FormDataContentDisposition;

public class WS_ExecutiveImage_Upload extends Connect {
	JSONObject output = new JSONObject();
	public String emp_photo = "";
	public String msg = "";
	public String img_imgsize = "";
	public String StrSql = "";
	public java.sql.PreparedStatement psmt = null;
	public Statement stmt = null;
	public byte[] doc_data = null;
	public Connection conn = null;
	public JSONObject ExecutiveImageUpload(String emp_id, String comp_id, InputStream is, FormDataContentDisposition formData) {

		try {

			if (!emp_id.equals("0")) {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET"
						+ " emp_photo = ?"
						+ " WHERE emp_id = ?";
				conn = null;
				conn = connectDB();
				psmt = conn.prepareStatement(StrSql);
				psmt.setBlob(1, is);
				psmt.setString(2, emp_id);
				psmt.executeUpdate();
				output.put("msg", "Uploaded Successfully!");
			} else {
				output.put("msg", "Failed to upload image!");
			}
			psmt.close();
			conn.close();
		} catch (Exception ex) {
			SOPError("app-WS-Uploadimage===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
	}
}
