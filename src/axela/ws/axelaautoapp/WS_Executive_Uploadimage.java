package axela.ws.axelaautoapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONObject;

import axela.portal.Header;
import cloudify.connect.Connect;

import com.sun.jersey.core.header.FormDataContentDisposition;

public class WS_Executive_Uploadimage extends Connect {
	JSONObject output = new JSONObject();
	public String emp_photo = "";
	public String msg = "";
	public String img_imgsize = "";

	public JSONObject UploadImage(String emp_id, String comp_id, InputStream is, FormDataContentDisposition formData, @Context HttpServletRequest request) {
		new Header().UserActivity(emp_id, request.getRequestURI(), "1", comp_id);
		img_imgsize = (ImageSize(comp_id));
		String fileName = formData.getFileName();
		String imgExt = fileext(fileName);
		String StrSql = "";
		// JSONObject output = new JSONObject();
		fileName = "emp_" + emp_id + imgExt;
		String fileLocation = ExeImgPath(comp_id) + fileName;

		DiskFileItemFactory factory = new DiskFileItemFactory();
		int val = (int) ((1024 * 1024 * Double.parseDouble(img_imgsize)) + (1024 * 1024));
		factory.setSizeThreshold(val);

		File f = new File(ExeImgPath(comp_id));
		if (!f.exists()) {
			f.mkdirs();
		}
		factory.setRepository(f);

		ServletFileUpload upload = new ServletFileUpload(factory);
		// Set overall request size constraint
		upload.setSizeMax(val);

		try {
			if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
				msg = "Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
			}
			if (!msg.equals("")) {

				msg = "Error " + msg;
			} else {
				saveFile(is, fileLocation);
				StrSql = "UPDATE " + compdb(comp_id) + " axela_emp"
						+ " SET emp_photo ='" + fileName + "'"
						+ " WHERE emp_id = " + emp_id;
				updateQuery(StrSql);
				emp_photo = ExecuteQuery("SELECT emp_photo FROM " + compdb(comp_id) + "axela_emp WHERE emp_id = " + emp_id);
				output.put("emp_photo", emp_photo);
				msg = "Image Uploaded Sucessfully! ";

			}
			output.put("msg", msg);

		} catch (Exception ex) {
			SOPError("app-WS-Uploadimage===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
		return output;
		// return "FILE UPLOADED TO LOcation: " + fileLocation;
	}
	private void saveFile(InputStream is, String fileLocation) throws IOException {
		OutputStream out = new FileOutputStream(new File(fileLocation));
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(fileLocation));

		while ((read = is.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}

}
