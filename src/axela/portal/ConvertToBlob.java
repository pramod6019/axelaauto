package axela.portal;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cloudify.connect.Connect;

public class ConvertToBlob extends Connect {

	public String updatestatus = "";
	public String deleteB = "";
	public String updateB = "";
	public String add = "";
	public String update = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public byte[] img_pic = null;
	public boolean isMultipart;
	public InputStream inputStream = null;
	public List<FileItem> items = null;
	public ServletFileUpload upload = null;
	DiskFileItemFactory factory = null;
	Map<String, String> fieldsMap = new HashMap<String, String>();
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String name = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String emp_name = "";
	public String emp_photo = "";
	public String img_imgsize = "";
	public String chkPermMsg = "";
	public String msg2 = "";
	public String emp_idsession = "";
	public String QueryString = "";
	public PreparedStatement psmt = null;
	public CachedRowSet crs = null;
	public Connection conn = null;
	private String Image = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = "1000";
			CheckPerm(comp_id, "emp_role_id", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
				add = PadQuotes(request.getParameter("addbutton"));

				if (add.equals("Add Photo")) {

					StrSql = "SELECT emp_photo, emp_id FROM " + compdb(comp_id) + "axela_emp_copy WHERE emp_photo != '' ";
					// SOP("StrSql======" + StrSql);
					CachedRowSet crs = processQuery(StrSql, 0);
					while (crs.next()) {
						// SOP("----===----");
						Image = ExeImgPath(comp_id) + crs.getString("emp_photo");
						// SOP("Image-----" + Image);
						inputStream = new FileInputStream(Image);
						// SOP("Coming----");

						if (inputStream != null) {
							try {
								StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
										+ " SET " + " emp_photo = ?"
										+ " WHERE emp_id = " + crs.getString("emp_id");
								conn = connectDB();
								// SOP("StrSql-----" + StrSql);
								psmt = conn.prepareStatement(StrSql);
								if (inputStream != null) {
									psmt.setBlob(1, inputStream);
								}
								psmt.executeUpdate();
							} catch (Exception ex) {
								SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
							} finally {
								try {
									if (psmt != null) {
										psmt.close();
									}
									if (conn != null) {
										conn.close();
									}
								} catch (Exception ex) {
									SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
								}
							}
						}
						inputStream.close();
					}
					crs.close();
				}
			}
		} catch (Exception ex) {
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}

}
