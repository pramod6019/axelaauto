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

public class Executives_Photo extends Connect {

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
    public String str1[] = {"", "", "", "", "", "", "", "", ""};
    public String name = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String emp_name = "";
    public String emp_photo = "";
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
            if(!comp_id.equals("0")){
            emp_idsession = CNumeric(GetSession("emp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            //branch_id = CNumeric(GetSession("emp_branch_id", request));
            emp_id = CNumeric(PadQuotes(request.getParameter("emp_id")));
            add = PadQuotes(request.getParameter("add"));
            update = PadQuotes(request.getParameter("update"));
            QueryString = PadQuotes(request.getQueryString());

            savePath = ExeImgPath(comp_id);
            img_imgsize = (ImageSize(comp_id));
            msg = PadQuotes(request.getParameter("msg"));
            StrSql = "select emp_photo, emp_name from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id;
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                emp_name = crs.getString("emp_name");
                emp_photo = crs.getString("emp_photo");
            }
            crs.close();
            if (emp_photo.equals("")) {
                status = "Add";
            } else {
                status = "Update";
            }
            if (update.equals("yes")) {
                emp_id = PadQuotes(request.getParameter("emp_id"));
                PopulateFields();
            }

            boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
            if (isMultipart) {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                int val = (int) ((1024 * 1024 * Double.parseDouble(img_imgsize)) + (1024 * 1024));
                factory.setSizeThreshold(val);
//                File f = new File("d:/");
                File f = new File(savePath);
                if (!f.exists()) {
                    f.mkdirs();
                }
                factory.setRepository(f);
//				 Create a new file upload handler
                ServletFileUpload upload = new ServletFileUpload(factory);
//				 Set overall request size constraint
                upload.setSizeMax(val);
//				 Parse the request
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
                    if (str1[i].equals("Update Photo") || str1[i].equals("Add Photo")) {
//                        if (status.equals("Add")) {
//                            chkPermMsg = CheckPerm(comp_id, "emp_patient_add", request, response);
//                        }
//                        if (status.equals("Update")) {
//                            chkPermMsg = CheckPerm(comp_id, "emp_patient_edit", request, response);
//                        }
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
                                    String prevFile = ExecuteQuery("select emp_photo from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + "");
                                    if (!prevFile.equals("")) {
                                        File uploadedFile1 = new File(ExeImgPath(comp_id) + prevFile);
                                        if (uploadedFile1.exists()) {
                                            uploadedFile1.delete();
                                        }
                                    }
                                    if (!fileName.equals("")) {
                                        UpdateFields();
                                        msg = "Photo uploaded successfully!";
                                        File uploadedFile = new File(ExeImgPath(comp_id) + fileName);

                                        if (uploadedFile.exists()) {
                                            uploadedFile.delete();
                                        }
                                        item.write(uploadedFile);
                                        response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?emp_id=" + emp_id + "&msg=" + msg));
                                    }
                                }
                            }
                        }
                    }
                    if (str1[i].equals("Delete Photo")) {
                        //chkPermMsg = CheckPerm(comp_id, "emp_patient_delete", request, response);
                        if (chkPermMsg.equals("")) {
                            fileName = ExecuteQuery("select emp_photo from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + "");
                            if (!fileName.equals("")) {
                                File uploadedFile = new File(ExeImgPath(comp_id) + fileName);
                                if (uploadedFile.exists()) {
                                    uploadedFile.delete();
                                }
                                fileName = "";
                                UpdateFields();
                                msg = "";
                                response.sendRedirect(response.encodeRedirectURL("executive-list.jsp?emp_id=" + emp_id + "&msg=Photo deleted successfully!"));
                            } else {
                                msg = " No Photo found!";
                            }
                        }
                    }
                }
            }
            }
        } catch (FileUploadException Fe) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + Fe);
            msg = "Uploaded file size is large!";
            response.sendRedirect("executives-list.jsp?emp_id=" + emp_id + "&msg=" + msg);
        } catch (Exception ex) {
            SOPError("Axelaauto===" + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        doPost(request, response);
    }

    protected void PopulateFields() {

        try {
            StrSql = "select emp_name, emp_photo from " + compdb(comp_id) + "axela_emp where emp_id=" + emp_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                emp_name = crs.getString("emp_name");
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
                        + " emp_name = '" + emp_name + "',"
                        + " emp_photo = '" + fileName + "'"
                        + " where emp_id = " + emp_id + " ";
                updateQuery(StrSql);
//                SOP("SqlStr==" + StrSql);
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
}
