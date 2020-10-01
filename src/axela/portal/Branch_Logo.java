package axela.portal;
//Bhagwan Sing 05/07/2013

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

public class Branch_Logo extends Connect {

    private String fileName;
    public String updatestatus = "";
    public String deleteB = "";
    public String updateB = "";
    public String comp_id = "0";
    public String add = "";
    public String update = "";
    public String status = "";
    public String StrSql = "";
    public String msg = "";
    public String savePath = "";
    public String str1[] = {"", "", "", "", "", "", "", "", ""};
    public String name = "";
    public String branch_id = "";
    public String branch_name = "";
    public String branch_logo = "";
    public String img_imgsize = "";
    public String chkPermMsg = "";
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
            branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
            add = PadQuotes(request.getParameter("add"));
            update = PadQuotes(request.getParameter("update"));
            QueryString = PadQuotes(request.getQueryString());

            savePath = BranchLogoPath(comp_id);
            img_imgsize = (ImageSize(comp_id));
            msg = PadQuotes(request.getParameter("msg"));
            StrSql = "SELECT branch_logo, branch_name"
                    + " FROM " + compdb(comp_id) + "axela_branch"
                    + " WHERE branch_id = " + branch_id;

            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                branch_name = crs.getString("branch_name");
                branch_logo = crs.getString("branch_logo");
            }
            crs.close();

            if (branch_logo.equals("")) {
                status = "Add";
            } else {
                status = "Update";
            }

            if (update.equals("yes")) {
                branch_id = PadQuotes(request.getParameter("branch_id"));
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
                    if (str1[i].equals("Update Logo") || str1[i].equals("Add Logo")) {
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
                                    fileName = "branchlogo_" + branch_id + fileName.substring(pos);
                                }

                                if (!ImageFormats.contains(fileName.substring(fileName.lastIndexOf(".")).toLowerCase())) {
                                    msg = msg + "<br>Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
                                }
                            }

                            if (!msg.equals("")) {
                                msg = "Error!" + msg;
                            } else {
                                if (msg.equals("") && chkPermMsg.equals("")) {
                                    String prevFile = ExecuteQuery("SELECT branch_logo"
                                            + " FROM " + compdb(comp_id) + "axela_branch"
                                            + " WHERE branch_id = " + branch_id + "");
                                    if (!prevFile.equals("")) {
                                        File uploadedFile1 = new File(BranchLogoPath(comp_id) + prevFile);
                                        if (uploadedFile1.exists()) {
                                            uploadedFile1.delete();
                                        }
                                    }
                                    if (!fileName.equals("")) {
                                        UpdateFields();
                                        msg = "Logo uploaded successfully!";
                                        File uploadedFile = new File(BranchLogoPath(comp_id) + fileName);

                                        if (uploadedFile.exists()) {
                                            uploadedFile.delete();
                                        }
                                        item.write(uploadedFile);
                                        response.sendRedirect(response.encodeRedirectURL("branch-list.jsp?branch_id=" + branch_id + "&msg=" + msg));
                                    }
                                }
                            }
                        }
                    }
                    if (str1[i].equals("Delete Logo")) {
                        //chkPermMsg = CheckPerm(comp_id, "emp_patient_delete", request, response);
                        if (chkPermMsg.equals("")) {
                            fileName = ExecuteQuery("SELECT branch_logo"
                                    + " FROM " + compdb(comp_id) + "axela_branch"
                                    + " WHERE branch_id = " + branch_id + "");
                            if (!fileName.equals("")) {
                                File uploadedFile = new File(BranchLogoPath(comp_id) + fileName);
                                if (uploadedFile.exists()) {
                                    uploadedFile.delete();
                                }
                                fileName = "";
                                UpdateFields();
                                response.sendRedirect(response.encodeRedirectURL("branch-list.jsp?branch_id=" + branch_id + "&msg=Logo deleted successfully!"));
                            } else {
                                msg = "No Logo found!";
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
            response.sendRedirect("branch-list.jsp?branch_id=" + branch_id + "&msg=" + msg);
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
            StrSql = "SELECT branch_name, branch_logo"
                    + " FROM " + compdb(comp_id) + "axela_branch"
                    + " WHERE branch_id = " + branch_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            while (crs.next()) {
                branch_name = crs.getString("branch_name");
                branch_logo = crs.getString("branch_logo");
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
                StrSql = "UPDATE " + compdb(comp_id) + "axela_branch"
                        + " SET"
                        + " branch_name = '" + branch_name + "',"
                        + " branch_logo = '" + fileName + "'"
                        + " WHERE branch_id = " + branch_id + " ";
                updateQuery(StrSql);
            } catch (Exception ex) {
                SOPError("Axelaauto===" + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void CheckForm() {
        msg = "";
        if (fileName.equals("")) {
            msg = msg + "<br>Select Logo!";
        }
    }
}
