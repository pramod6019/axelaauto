package axela.insurance;

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

public class InsurComp_Doc extends Connect {

	private String fileName;
	public String add = "";
	public String update = "";
	public String status = "";
	public String StrSql = "";
	public String msg = "";
	public String savePath = "";
	public String str1[] = {"", "", "", "", "", "", "", "", ""};
	public String inscomp_name = "";
	public String inscomp_title = "";
	public long config_doc_size;
	public String config_doc_format = "";
	public String[] docformat;
	public String QueryString = "";
	public String emp_id = "0";
	public String comp_id = "0";
	public String inscomp_id = "0";
	public String inscomp_value = "";
	public String name = "";
	public String id = "0";

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			comp_id = CNumeric(GetSession("comp_id", request));
			CheckPerm(comp_id, "emp_insurance_policy_access", request, response);
			if (!comp_id.equals("0")) {
				emp_id = CNumeric(GetSession("emp_id", request));
				inscomp_id = CNumeric(PadQuotes(request.getParameter("inscomp_id")));
				QueryString = PadQuotes(request.getQueryString());
				add = PadQuotes(request.getParameter("add"));
				update = PadQuotes(request.getParameter("update"));
				msg = PadQuotes(request.getParameter("msg"));
				savePath = InsurCompDocPath(comp_id);

				StrSql = "select inscomp_name, inscomp_title, inscomp_value from " + compdb(comp_id) + "axela_insurance_comp where inscomp_id=" + inscomp_id;
				CachedRowSet crs = processQuery(StrSql, 0);
				while (crs.next()) {
					inscomp_name = crs.getString("inscomp_name");
					inscomp_title = crs.getString("inscomp_title");
					inscomp_value = crs.getString("inscomp_value");
				}
				crs.close();
				if (inscomp_value.equals("")) {
					status = "Add";
				} else {
					status = "Update";
				}
				// LinkHeader =
				// "<a href=../service/home.jsp>Home</a> &gt; <a href=../service/index.jsp>Service</a> &gt; <a href=../service/insurance.jsp>Insurance</a> &gt; <a href=../service/insurance-list.jsp?all=yes>List Insurance</a> &gt; <a href=../service/insurance-list.jsp?inscomp_id="
				// + inscomp_id + ">" + name +
				// "</a> &gt; <a href=../service/insurance-docs-list.jsp?inscomp_id="
				// + inscomp_id +
				// " > List Documents</a> &gt; <a href=../portal/docs-update.jsp?"
				// + QueryString + ">" + status + " Document</a>:";
				// }

				PopulateConfigDetails(response);

				if (update.equals("yes")) {
					inscomp_id = PadQuotes(request.getParameter("inscomp_id"));
					PopulateFields(request, response);
				}
				CheckPerm(comp_id, "emp_insurance_policy_add", request, response);

				String button_name = "";
				boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
				if (isMultipart) {
					// Create a factory for disk-based file items
					DiskFileItemFactory factory = new DiskFileItemFactory();
					// Set factory constraints
					factory.setSizeThreshold((1024 * 1024 * (int) config_doc_size) + (1024 * 1024));
					// File f = new File("d:/");
					File f = new File(savePath);
					if (!f.exists()) {
						f.mkdirs();
					}
					factory.setRepository(f);
					// Create a new file upload handler
					ServletFileUpload upload = new ServletFileUpload(factory);
					// Set overall request size constraint
					upload.setSizeMax((1024 * 1024 * (int) config_doc_size) + (1024 * 1024));
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
					// if (add.equals("yes")) {
					// button_name = "Add Document";
					// } else if (update.equals("yes")) {
					// button_name = "Update Document";
					// }

					for (int i = 0; iter.hasNext() && i < 9; i++) {
						if (str1[i].equals("Update Document") || str1[i].equals("Add Document")) {
							// if (str1[i].equals(button_name) &&
							// add.equals("yes")) {
							FileItem item = (FileItem) iter.next();
							if (!item.isFormField()) {
								fileName = item.getName();
								CheckForm();
								if (!fileName.equals("")) {
									String temp = "";
									fileName = item.getName();
									if (!fileName.equals("")) {
										docformat = config_doc_format.split(", ");
										for (int j = 0; j < docformat.length; j++) {
											if (!fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(docformat[j])) {
												temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
											} else {
												temp = "";
												break;
											}
										}
										msg = msg + temp;
									}
								}
								if (!msg.equals("")) {
									msg = "Error!" + msg;
								} else {
									if (!fileName.equals("")) {
										// GetValues(request,response);
										UpdateFields();
										File uploadedFile = new File(savePath + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);

										response.sendRedirect(response.encodeRedirectURL("manageinsurcomp.jsp?manageinsurcomp=" + inscomp_id + "&msg=Document added Successfully!"));

									}
								}
							}
						}

						if (str1[i].equals("Delete Document")) {
							CheckPerm(comp_id, "emp_insurance_policy_delete", request, response);
							fileName = ExecuteQuery("Select inscomp_value from " + compdb(comp_id) + "axela_insurance_comp where inscomp_id = " + inscomp_id + "");
							if (!fileName.equals("")) {
								File uploadedFile = new File(savePath + fileName);
								if (uploadedFile.exists()) {
									uploadedFile.delete();
								}
								fileName = "";
								str1[1] = "";
								UpdateFields();
								msg = "";
								response.sendRedirect(response.encodeRedirectURL("manageinsurcomp.jsp?inscomp_id=" + inscomp_id + "&msg=Document deleted Successfully!"));
							} else {
								msg = " No Document found!";
							}
						}
					}
				}
			}
		} catch (FileUploadException Fe) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("insurcomp-doc.jsp?inscomp_id=" + inscomp_id + "&msg=" + msg);
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		doPost(request, response);
	}

	protected void PopulateFields(HttpServletRequest request, HttpServletResponse response) {
		try {

			StrSql = "select inscomp_title, inscomp_value from " + compdb(comp_id) + "axela_insurance_comp where inscomp_id=" + inscomp_id + "";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				inscomp_title = crs.getString("inscomp_title");
				inscomp_value = crs.getString("inscomp_value");
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
				StrSql = "UPDATE " + compdb(comp_id) + "axela_insurance_comp"
						+ " SET "
						+ " inscomp_title = '" + str1[1] + "',"
						+ " inscomp_value = '" + fileName + "'"
						+ " where inscomp_id = " + inscomp_id + " ";
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
		inscomp_title = "";
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
		if (str1[1].equals("")) {
			msg = msg + "<br>Enter Title!";
		}
		if (!str1[1].equals("")) {
			inscomp_title = str1[1];
		}
	}

	protected void PopulateConfigDetails(HttpServletResponse response) {
		try {
			StrSql = "Select config_doc_size, config_doc_formats"
					+ " from " + compdb(comp_id) + "axela_config";
			CachedRowSet crs = processQuery(StrSql, 0);
			while (crs.next()) {
				config_doc_size = (long) crs.getDouble("config_doc_size");
				config_doc_format = crs.getString("config_doc_formats");
			}

			crs.close();
		} catch (Exception ex) {
			SOPError("Axelaauto== " + this.getClass().getName());
			SOPError("Error in: " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
