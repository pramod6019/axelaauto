package axela.mktg;
//Sangita
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import axela.sales.XlsREadFile;
import axela.sales.XlsxReadFile;
import cloudify.connect.Connect;

public class Lead_Import extends Connect {

	public String StrSql = "", StrHTML = "";
	public String msg = "", emp_id = "0";
	public String emp_role_id = "0";
	public String savePath = "", importdocformat = "";
	public long docsize;
	public String[] docformat;
	public String displayform = "no";
	public String RefreshForm = "";
	public String fileName = "";
	public String str1[] = {"", "", "", "", "", "", "", "", "", "", "", "", "", "", ""};
	public String doc_value = "";
	public int leadcount = 0;
	public String lead_entry_id = "";
	public String lead_entry_date = "";
	public String BranchAccess = "";
	public String lead_branch_id = "0";
	public String upload = "";
	public String comp_id = "0";
	public String similar_comm_no = "";

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		try {
			CheckSession(request, response);
			HttpSession session = request.getSession(true);
			emp_id = GetSession("emp_id", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			emp_role_id = GetSession("emp_role_id", request);
			BranchAccess = GetSession("BranchAccess", request);
			comp_id = CNumeric(GetSession("comp_id", request));
			// CheckPerm("emp_stock_edit", request, response);
			lead_entry_id = GetSession("emp_id", request);
			lead_entry_date = ToLongDate(kknow());
			upload = PadQuotes(request.getParameter("add_button"));
			Addfile(request, response);
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void Addfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			savePath = ExeDocPath(comp_id);
			docsize = 1;
			importdocformat = ".xls, .xlsx";

			boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
			if (isMultipart) {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold((1024 * 1024 * (int) docsize) + (1024 * 1024));
				File f = new File("d:/");
				factory.setRepository(f);
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setSizeMax((1024 * 1024 * (int) docsize) + (1024 * 1024));
				List items = upload.parseRequest(request);
				Iterator it = items.iterator();
				for (int i = 0; it.hasNext() && i < 15; i++) {
					FileItem button = (FileItem) it.next();
					if (button.isFormField()) {
						str1[i] = button.getString();
						// SOP(" str1[i]=="+ str1[i]);
					}
				}

				Iterator iter = items.iterator();
				msg = "";
				for (int i = 0; iter.hasNext() && i < 15; i++) {
					lead_branch_id = str1[0];
					if (str1[i].equals("Upload")) {
						FileItem item = (FileItem) iter.next();
						if (!item.isFormField()) {
							fileName = item.getName();
							CheckForm();
							if (!msg.equals("")) {
								msg = "Error!" + msg;
							}
							if (!fileName.equals("")) {
								String temp = "";
								if (fileName.contains("/") || fileName.contains("\\")) {
									fileName = Filename(item.getName());
								} else {
									fileName = item.getName();
								}
								if (!fileName.equals("")) {
									docformat = importdocformat.split(", ");
									for (int j = 0; j < docformat.length; j++) {
										if (!fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(docformat[j])) {
											temp = "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format!";
										} else {
											temp = "";
											break;
										}
									}
									msg = msg + temp;
								}
								if (msg.equals("")) {
									if (!fileName.equals("")) {
										File uploadedFile = new File(ExeDocPath(comp_id) + fileName);
										if (uploadedFile.exists()) {
											uploadedFile.delete();
										}
										item.write(uploadedFile);
										msg = msg + "<br>Document contents not in order!";
										String fileName1 = ExeDocPath(comp_id) + fileName;
										getSheetData(fileName1, 0);
										msg = leadcount + " Lead imported successfully!";
									}
								}
							}
						}
					}
				}
			}
		} catch (FileUploadException fe) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + fe);
			msg = "Uploaded file size is large!";
			response.sendRedirect("lead-import.jsp?msg=" + msg);
		}
	}
	public void CheckForm() {
		msg = "";
		if (lead_branch_id.equals("0")) {
			msg = msg + "<br>Select Branch!";
		}
		if (fileName.equals("")) {
			msg = msg + "<br>Select Document!";
		}
	}

	public void getSheetData(String fileName, int sheetIndex) throws FileNotFoundException, IOException, InvalidFormatException {
		try {

			String lead_title_id = "", title_name;
			String lead_fname = "";
			String lead_lname = "";
			String lead_jobtitle = "";
			String lead_date = "";
			String lead_email = "";
			String lead_mobile = "";
			String lead_company = "";
			String lead_empcount_id = "";
			String lead_soe_id = "", soe_name;
			String lead_sob_id = "", sob_name;
			String lead_emp_id = "", empcount_desc;
			int rowLength = 0;
			int columnLength = 0;
			String[][] sheetData = {};
			// XlsxReadFile readFile = new XlsxReadFile(); //if i/p file is .xlsx type
			if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xls")) {
				XlsREadFile readFile = new XlsREadFile(); // if i/p file is .xls type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			} else if (fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(".xlsx")) {
				XlsxReadFile readFile = new XlsxReadFile(); // if i/p file is .xlsx type
				sheetData = readFile.getSheetData(fileName, 0);
				columnLength = readFile.getNumberOfColumn(fileName, 0);
				rowLength = readFile.getNumberOfRow(fileName, 0);
			}

			// if (rowLength > 300) {
			// rowLength = 300;
			// }
			int h = 0;
			int j = 0;

			// rowLength = 30;
			for (j = 1; j < rowLength + 1; j++) {

				for (h = 0; h < columnLength; h++) {

					if (lead_branch_id.equals("0")) {
						lead_branch_id = "1";
					}
					if (h == 0) {
						title_name = sheetData[j][h];
						lead_title_id = ExecuteQuery("SELECT title_id FROM axela_title WHERE title_desc = '" + title_name + "'");
					}
					if (h == 1) {
						lead_fname = sheetData[j][h];
					}
					if (h == 2) {
						lead_lname = sheetData[j][h];
					}
					if (h == 3) {
						lead_jobtitle = sheetData[j][h];
					}
					if (h == 4) {
						lead_date = sheetData[j][h];
						if (!lead_date.equals("")) {
							boolean t2 = isValidDateFormatShort(lead_date);
							if (t2 == true) {
								lead_date = ConvertShortDateToStr(lead_date);
							} else if (t2 == false) {
								lead_date = "";
							}
						}
					}
					if (h == 5) {
						lead_email = sheetData[j][h];
					}
					if (h == 6) {
						lead_mobile = sheetData[j][h];
					}
					if (h == 7) {
						lead_company = sheetData[j][h];
					}
					if (h == 8) {
						empcount_desc = sheetData[j][h];
						lead_empcount_id = ExecuteQuery("SELECT empcount_id FROM axela_empcount WHERE empcount_desc = '" + empcount_desc + "'");
					}
					if (h == 9) {
						soe_name = sheetData[j][h];
						lead_soe_id = ExecuteQuery("SELECT soe_id FROM axela_soe WHERE soe_name = '" + soe_name + "'");
					}
					if (h == 10) {
						sob_name = sheetData[j][h];
						lead_sob_id = ExecuteQuery("SELECT sob_id FROM axela_sob WHERE sob_name = '" + sob_name + "'");
					}
					if (h == 11) {
						lead_emp_id = sheetData[j][h];
					}
				}

				if (!lead_branch_id.equals("") && !lead_email.equals("") || !lead_mobile.equals("")) {
					StrSql = "SELECT lead_id FROM axela_mktg_lead"
							+ " WHERE lead_email = '" + lead_email + "' OR lead_mobile = '" + lead_mobile + "' "
							+ " AND lead_branch_id = " + lead_branch_id + "";
					String lead_id = CNumeric(ExecuteQuery(StrSql));
					if (!lead_id.equals("0")) {
						// /// Update Lead
						StrSql = "UPDATE " + compdb(comp_id) + "axela_mktg_lead"
								+ " SET"
								+ " lead_branch_id = " + lead_branch_id + ","
								+ " lead_title_id = '" + lead_title_id + "',"
								+ " lead_fname = '" + lead_fname + "',"
								+ " lead_jobtitle = '" + lead_jobtitle + "',"
								+ " lead_date = '" + lead_date + "',"
								+ " lead_company = '" + lead_company + "',"
								+ " lead_empcount_id = '" + lead_empcount_id + "',"
								+ " lead_soe_id = " + lead_soe_id + ","
								+ " lead_sob_id = " + lead_sob_id + ","
								+ " lead_emp_id = " + lead_emp_id + ","
								+ " lead_modified_id = " + lead_entry_id + ","
								+ " lead_modified_date = '" + lead_entry_date + "'"
								+ " WHERE lead_id = " + lead_id + "";
						// SOP("StrSql==" + StrSql);

						updateQuery(StrSql);

					} else {
						leadcount++;
						lead_id = ExecuteQuery("SELECT (COALESCE(MAX(lead_id), 0) + 1) FROM axela_mktg_lead");
						StrSql = "INSERT INTO " + compdb(comp_id) + "axela_mktg_lead"
								+ "(lead_id,"
								+ " lead_branch_id,"
								+ " lead_title_id,"
								+ " lead_fname,"
								+ " lead_lname,"
								+ " lead_jobtitle,"
								+ " lead_date,"
								+ " lead_email,"
								+ " lead_mobile,"
								+ " lead_company,"
								+ " lead_empcount_id,"
								+ " lead_soe_id,"
								+ " lead_sob_id,"
								+ " lead_emp_id,"
								+ " lead_entry_id,"
								+ " lead_entry_date)"
								+ " VALUES"
								+ " (" + lead_id + ","
								+ " " + lead_branch_id + ","
								+ " " + lead_title_id + ","
								+ " '" + lead_fname + "',"
								+ " '" + lead_lname + "',"
								+ " '" + lead_jobtitle + "',"
								+ " '" + lead_date + "',"
								+ " '" + lead_email + "',"
								+ " '" + lead_mobile + "',"
								+ " '" + lead_company + "',"
								+ " " + lead_empcount_id + ","
								+ " " + lead_soe_id + ","
								+ " " + lead_sob_id + ","
								+ " " + lead_emp_id + ","
								+ " " + lead_entry_id + ","
								+ " '" + lead_entry_date + "')";
						// SOP("StrSql insert query======== " + StrSql);

						updateQuery(StrSql);
					}
				}
			}
		} catch (Exception ex) {
			SOPError(ClientName + "===" + this.getClass().getName());
			SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
		}
	}
}
