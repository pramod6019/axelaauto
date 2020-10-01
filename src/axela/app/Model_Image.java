package axela.app;

//aJIt 8th October, 2012
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import cloudify.connect.Connect;

public class Model_Image extends Connect {

	private String fileName;
	public String deleteB = "";
	public String addB = "";
	public String updateB = "";
	public String StrSql = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String model_id = "0";
	public String model_img_value = "";
	public String img_imgsize = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String QueryString = "";
	public String filename = "";
	Map<Integer, Object> map = new HashMap<Integer, Object>();
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0"))
			{
				CheckPerm(comp_id, "emp_role_id", request, response);
				emp_id = CNumeric(GetSession("emp_id", request));
				savePath = ModelImgPath(comp_id);
				img_imgsize = ImageSize(comp_id);
				msg = PadQuotes(request.getParameter("msg"));
				QueryString = PadQuotes(request.getQueryString());
				model_id = CNumeric(PadQuotes(request.getParameter("model_id")));
				PopulateFields(response);
				boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
				if (isMultipart) {
					// Create a factory for disk-based file items
					DiskFileItemFactory factory = new DiskFileItemFactory();
					// Set factory constraints
					int val = (int) ((1024 * 1024 * Double.parseDouble(img_imgsize)) + (1024 * 1024));
					factory.setSizeThreshold(val);
					// File f = new File("d:/");
					File f = new File(savePath);
					if (!f.exists()) {
						f.mkdirs();
					}
					factory.setRepository(f);
					// Create a new file upload handler
					ServletFileUpload upload = new ServletFileUpload(factory);
					// Set overall request size constraint
					upload.setSizeMax(val);
					// Parse the request
					List items = upload.parseRequest(request);
					Iterator it = items.iterator();
					for (int i = 0; it.hasNext() && i < 9; i++) {
						FileItem button = (FileItem) it.next();
						if (button.isFormField()) {
							str1[i] = button.getString();
						}
					}
					Iterator iter = items.iterator();
					for (int i = 0; iter.hasNext() && i < 9; i++) {

						if (str1[i].equals("Update Image")) {
							CheckPerm(comp_id, "emp_role_id", request, response);
							FileItem item = (FileItem) iter.next();
							if (!item.isFormField()) {
								fileName = item.getName();
								CheckForm();
								if (!fileName.equals("")) {
									int pos = fileName.lastIndexOf(".");
									if (pos != -1) {
										fileName = "model_img_" + model_id + "" + fileName.substring(pos);
									}
									if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
										msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
									}
								}
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (!fileName.equals("")) {
										String prevFile = ExecuteQuery("select model_img_value from " + compdb(comp_id) + "axela_inventory_item_model"
												+ " where model_id = " + model_id + "");
										if (!prevFile.equals("")) {
											File uploadedFileprevFile = new File(ModelImgPath(comp_id) + prevFile);
											if (uploadedFileprevFile.exists()) {
												uploadedFileprevFile.delete();
											}
										}
										UpdateFields();
										msg = msg + "Image updated successfully!";
										File uploadedFile = new File(ModelImgPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										response.sendRedirect(response.encodeRedirectURL("model-list.jsp?model_id=" + model_id + "&msg=" + msg));
									}
								}
							}
						}
						if (str1[i].equals("Delete Image")) {
							CheckPerm(comp_id, "emp_role_id", request, response);
							fileName = ExecuteQuery("Select img_value from " + compdb(comp_id) + "axela_app_model_img"
									+ " where img_model_id = " + model_id + "");
							File uploadedFile = new File(ModelImgPath(comp_id) + fileName);
							if (uploadedFile.exists()) {
								uploadedFile.delete();
							}
							DeleteFields();
							response.sendRedirect(response.encodeRedirectURL("model-list.jsp?model_id=" + model_id + "&msg=Image deleted successfully!"));
						}
					}
				}
			}

		} catch (FileUploadException Fe) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("model-image.jsp?update=yes&model_id=" + model_id + "&msg=" + msg);
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}

	protected void GetValues(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void PopulateFields() {
		try {
			StrSql = "select model_img_value from " + compdb(comp_id) + "axela_inventory_item_model"
					+ " where model_id = " + model_id;
			map.put(1, model_id);
			CachedRowSet crs = processPrepQuery(StrSql, map, 0);
			while (crs.next()) {
				model_img_value = crs.getString("model_img_value");
			}
			crs.close();
			map.clear();
		} catch (Exception ex) {
			SOPError("AxelaCRM===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void PopulateFields(HttpServletResponse response) {
		try {
			StrSql = "select model_img_value from " + compdb(comp_id) + "axela_inventory_item_model"
					+ " where model_id = " + model_id;
			CachedRowSet crs =processQuery(StrSql, 0);
			if (crs.isBeforeFirst()) {
				while (crs.next()) {
					model_img_value = crs.getString("model_img_value");
				}
			}
			/*
			 * else { response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Model!")); }
			 */
			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);

		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE  " + compdb(comp_id) + "axela_inventory_item_model"
						+ " SET "
						+ " model_img_value = '" + fileName + "'"
						+ " where model_id = " + model_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void DeleteFields() {
		if (msg.equals("")) {
			try {
				StrSql = "Update " + compdb(comp_id) + "axela_inventory_item_model"
						+ " SET"
						+ " model_img_value = ''"
						+ " where model_id = " + model_id + "";
				updateQuery(StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm() {
		msg = "";
		String Msg1 = "", Msg2 = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Image!";
		}
		Msg1 = Msg1 + Msg2;
		msg = msg + Msg1;
	}
}
