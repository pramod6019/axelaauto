package axela.portal;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import cloudify.connect.Connect;

public class Executive_Photo_Upload extends Connect {

	public String branch_id = "";
	public String comp_logo = "";
	public String emp_id = "";
	public String comp_id = "0";
	public String StrSql = "";
	// public String emp_name = "";
	public String sessionid = "";
	public String exeaccess = "0";
	public String Img = "";
	public String alt = "";
	private String fileName;
	public String updatestatus = "";
	public String deleteB = "";
	public String updateB = "";
	public String add = "";
	public String update = "";
	public String status = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String emp_photo = "";
	public String img_imgsize = "";
	public String chkPermMsg = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_id = CNumeric(GetSession("emp_id", request));
			// emp_photo = GetSession("emp_photo", request);
			if (!comp_id.equals("0")) {
				// // Start executive photo
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				QueryString = PadQuotes(request.getQueryString());
				savePath = ExeImgPath(comp_id);
				img_imgsize = (ImageSize(comp_id));
				msg = PadQuotes(request.getParameter("msg"));
				SOP("above populate");
				PopulateFields();
				SOP("below populate");
				if (emp_photo.equals("")) {
					status = "Add";
				} else {
					status = "Update";

				}
				// if (update.equals("yes")) {
				// PopulateFields();
				// }

				boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
				SOP("coming hear isMultipart" + isMultipart);
				if (isMultipart) {
					DiskFileItemFactory factory = new DiskFileItemFactory();
					int val = (int) ((1024 * 1024 * Double.parseDouble(img_imgsize)) + (1024 * 1024));
					SOP("val====" + val);
					factory.setSizeThreshold(val);
					// File f = new File("d:/");
					File f = new File(savePath);
					if (!f.exists()) {
						f.mkdirs();
					}
					SOP("aaaaaaaaaa");
					factory.setRepository(f);
					// Create a new file upload handler

					ServletFileUpload upload = new ServletFileUpload(factory);
					// Set overall request size constraint

					upload.setSizeMax(val);
					SOP("upload====" + upload.getSizeMax());
					// Parse the request
					SOP("bbbbbbbbbb");
					SOP("sssssssssss");

					List items = upload.parseRequest(request);
					SOP("ggggggggggg");
					Iterator it = items.iterator();
					for (int i = 0; it.hasNext() && i < 9; i++) {
						FileItem button = (FileItem) it.next();
						if (button.isFormField()) {
							str1[i] = button.getString();
						}
					}
					msg = "";
					SOP("ccccccccc");
					Iterator iter = items.iterator();
					for (int i = 0; iter.hasNext() && i < 9; i++) {
						if (str1[i].equals("Update Photo") || str1[i].equals("Add Photo")) {
							// if (status.equals("Add")) {
							// chkPermMsg = CheckPerm(comp_id, "emp_patient_add", request, response);
							// }
							// if (status.equals("Update")) {
							// chkPermMsg = CheckPerm(comp_id, "emp_patient_edit", request, response);
							// }
							FileItem item = (FileItem) iter.next();
							if (!item.isFormField()) {
								fileName = item.getName();
								CheckForm();
								if (!fileName.equals("")) {
									int pos = fileName.lastIndexOf(".");
									if (pos != -1) {
										fileName = "emp_" + emp_id + fileName.substring(pos);
									}

									if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
										msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
									}
								}

								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (msg.equals("") && chkPermMsg.equals("")) {
										String prevFile = ExecuteQuery("SELECT emp_photo"
												+ " FROM " + compdb(comp_id) + "axela_emp"
												+ " WHERE emp_id=" + emp_id + "");
										if (!prevFile.equals("")) {
											File uploadedFile1 = new File(ExeImgPath(comp_id) + prevFile);
											if (uploadedFile1.exists()) {
												uploadedFile1.delete();
											}
										}
										if (!fileName.equals("")) {
											SOP("555555");
											UpdateFields();
											SOP("7777777");
											// SetSession("emp_photo", emp_photo, request);
											msg = "Photo uploaded successfully!";
											File uploadedFile = new File(ExeImgPath(comp_id) + fileName);

											if (uploadedFile.exists()) {
												uploadedFile.delete();
											}
											item.write(uploadedFile);
											// SetSession("emp_photo", emp_photo, request);
											response.sendRedirect(response.encodeRedirectURL("../portal/executive-list.jsp?emp_id=" + emp_id + "&msg=" + msg));
										}
									}
								}
							}
						}
						if (str1[i].equals("Delete Photo")) {

							// chkPermMsg = CheckPerm(comp_id, "emp_patient_delete", request, response);
							if (chkPermMsg.equals("")) {
								fileName = ExecuteQuery("SELECT emp_photo"
										+ " FROM " + compdb(comp_id) + "axela_emp"
										+ " WHERE emp_id=" + emp_id + "");
								SOP("222222");
								if (!fileName.equals("")) {
									File uploadedFile = new File(ExeImgPath(comp_id) + fileName);
									if (uploadedFile.exists()) {
										uploadedFile.delete();
									}
									fileName = "";
									UpdateFields();
									msg = "";
									SOP("33333333");
									// SetSession("emp_photo", "", request);
									response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?emp_id=" + emp_id + "&msg=Photo deleted successfully!"));
								} else {
									msg = " No Photo found!";
								}
							}
						}
					}
				}
				// End executive photo

				// Start Duplicate Sign in
			}
			else {
				response.sendRedirect("../portal/index.jsp?msg=Session Expired!");
			}
			// End Duplicate Sign in
		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in "
					+ new Exception().getStackTrace()[0].getMethodName() + ": "
					+ ex);
		}
	}
	protected void PopulateFields() {

		try {
			StrSql = "SELECT emp_photo"
					+ " FROM " + compdb(comp_id) + "axela_emp"
					+ " WHERE emp_id=" + emp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			SOP("StrSql==populate==" + StrSql);
			while (crs.next()) {
				// emp_name = crs.getString("emp_name");
				emp_photo = crs.getString("emp_photo");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {
				StrSql = "UPDATE " + compdb(comp_id) + "axela_emp"
						+ " SET "
						+ " emp_photo = '" + fileName + "'"
						+ " WHERE emp_id = " + emp_id + " ";
				updateQuery(StrSql);
				SOP("SqlStr= update=" + StrSql);

			} catch (Exception ex) {
				SOPError("Axelaauto===" + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm() {

		msg = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Photo!";
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}
}
