package axela.ws.axelaautoapp;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.sun.jersey.core.header.FormDataContentDisposition;

public class WS_Driving_Licence_Upload extends Connect {
	JSONObject output = new JSONObject();
	public String emp_photo = "";
	public String msg = "";
	public String img_imgsize = "";
	public java.sql.PreparedStatement psmt = null;
	public Statement stmt = null;
	public byte[] doc_data = null;
	public Connection conn = null;

	public JSONObject UploadDrivingLicence(String emp_id, String testdrive_id, String comp_id, InputStream is, FormDataContentDisposition formData, @Context HttpServletRequest request) {
		img_imgsize = (ImageSize(comp_id));
		String fileName = "testdrive_" + testdrive_id + ".jpg";
		String StrSql = "";
		if (!emp_id.equals("0")) {
			new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_sales_testdrive"
						+ " SET"
						+ " testdrive_doc_data = ?,"
						+ " testdrive_doc_value = ?,"
						+ " testdrive_doc_modified_id=?,"
						+ " testdrive_doc_modified_date =?"
						+ " WHERE testdrive_id = ?";
				conn = null;
				conn = connectDB();
				psmt = conn.prepareStatement(StrSql);
				psmt.setBlob(1, is);
				psmt.setString(2, fileName);
				psmt.setString(3, emp_id);
				psmt.setString(4, ToLongDate(kknow()));
				psmt.setString(5, testdrive_id);
				psmt.executeUpdate();
				msg = "Image Uploaded Successfully! ";
				output.put("msg", msg);
			} catch (Exception ex) {
				SOPError("Axelaauto-App===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			} finally {
				try {
					if (psmt != null) {
						psmt.close();
					}
					if (stmt != null) {
						stmt.close();
					}
					if (conn != null) {
						conn.close();
					}

				} catch (Exception ex) {
					SOP(this.getClass().getName());
					SOP(new Exception().getStackTrace()[0].getMethodName() + " : " +
							ex);
				}
			}
			if (AppRun().equals("0")) {
				SOP("output=====ws_driving-licence=====" + output);
			}
		} else {
			msg = "Upload Failed!";
		}
		return output;
		// return "FILE UPLOADED TO LOcation: " + fileLocation;
	}
}
