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
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import cloudify.connect.Connect;

public class Company_Logo extends Connect {

	private String fileName;
	public String updatestatus = "";
	public String deleteB = "";
	public String updateB = "";
	public String add = "";
	public String update = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = { "", "", "", "", "", "", "", "", "" };
	public String name = "";
	public String comp_id = "";
	public String comp_name = "";
	public String branch_id = "";
	public String BranchAccess = "";
	public String comp_logo = "";
	public String img_imgsize = "";
	public String chkPermMsg = "";
	public String msg2 = "";
	public String emp_idsession = "";
	public String QueryString = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			if (!comp_id.equals("0")) {
				BranchAccess = GetSession("BranchAccess", request);
				CheckPerm(comp_id, "emp_role_id", request, response);
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				QueryString = PadQuotes(request.getQueryString());

				savePath = CompLogoPath();
				img_imgsize = (ImageSize(comp_id));
				msg = PadQuotes(request.getParameter("msg"));
				StrSql = "select comp_logo, comp_name from " + compdb(comp_id) + " axela_comp where comp_id=" + comp_id;

				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					comp_name = crs.getString("comp_name");
					comp_logo = crs.getString("comp_logo");
				}
				crs.close();
				if (comp_logo.equals("")) {
					status = "Add";
				} else {
					status = "Update";
				}
				// SOP("status====="+status);
				if (update.equals("yes")) {
					comp_id = PadQuotes(request.getParameter("comp_id"));
					PopulateFields();
				}

				boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
				if (isMultipart) {
					DiskFileItemFactory factory = new DiskFileItemFactory();
					int val = (int) ((1024 * 1024 * Double.parseDouble(img_imgsize)) + (1024 * 1024));
					factory.setSizeThreshold(val);
					// File f = new File("D:\\");
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
					msg = "";
					Iterator iter = items.iterator();
					for (int i = 0; iter.hasNext() && i < 9; i++) {
						if (str1[i].equals("Update Company Logo") || str1[i].equals("Add Company Logo")) {

							FileItem item = (FileItem) iter.next();

							if (!item.isFormField()) {
								fileName = item.getName();
								CheckForm();
								if (!fileName.equals("")) {
									int pos = fileName.lastIndexOf(".");
									if (pos != -1)
									{

										fileName = "complogo_" + comp_id + fileName.substring(pos);

									}

									if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
										msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
									}
								}
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (msg.equals("") && chkPermMsg.equals("")) {
										String prevFile = ExecuteQuery("select comp_logo from " + compdb(comp_id) + "  axela_comp where comp_id=" + comp_id + "");
										if (!prevFile.equals("")) {
											File uploadedFile1 = new File(CompLogoPath() + prevFile);
											if (uploadedFile1.exists()) {
												uploadedFile1.delete();
											}
										}

										if (!fileName.equals("")) {
											UpdateFields();
											msg = "Company Logo uploaded successfully!";
											File uploadedFile = new File(CompLogoPath() + fileName);

											if (uploadedFile.exists()) {
												uploadedFile.delete();
											}
											item.write(uploadedFile);
											response.sendRedirect(response.encodeRedirectURL("company-logo.jsp?msg=" + msg));
										}
									}
								}
							}
						}
						if (str1[i].equals("Delete Company Logo")) {
							// chkPermMsg = CheckPerm("emp_patient_delete", request, response);
							if (chkPermMsg.equals("")) {
								fileName = ExecuteQuery("select comp_logo from  " + compdb(comp_id) + " axela_comp where comp_id=" + comp_id + "");
								if (!fileName.equals("")) {
									File uploadedFile = new File(CompLogoPath() + fileName);
									if (uploadedFile.exists()) {
										uploadedFile.delete();
									}
									fileName = "";
									UpdateFields();
									msg = "";
									response.sendRedirect(response.encodeRedirectURL("company-logo.jsp?comp_id=" + comp_id + "&msg=Company Logo deleted successfully!"));
								} else {
									msg = " No Company Logo found!";
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException Fe) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("company-logo.jsp?comp_id=" + comp_id + "&msg=" + msg);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}

	protected void PopulateFields() {

		try {
			StrSql = "select comp_logo from  " + compdb(comp_id) + " axela_comp where comp_id=" + comp_id + "";
			// SOP("strsql==============="+StrSqlBreaker(StrSql));
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				comp_logo = crs.getString("comp_logo");
			}
			crs.close();

		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	protected void UpdateFields() {
		if (msg.equals("")) {
			try {

				StrSql = "UPDATE " + compdb(comp_id) + " axela_comp "
						+ " SET "
						+ " comp_logo = '" + fileName + "'"
						+ " where comp_id = " + comp_id + " ";

				// SOP("StrSql========"+StrSqlBreaker(StrSql));
				updateQuery(StrSql);
				// SOP("SqlStr==" + StrSql);
			} catch (Exception ex) {
				SOPError("Axelaauto== " + this.getClass().getName());
				SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
			}
		}
	}

	protected void CheckForm() {

		msg = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Company Logo!";
		}
	}
}
