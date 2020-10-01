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

public class Managedocs_Update extends Connect {

    private String fileName;
    public String updatestatus = "";
    public String displayform = "no";
    public String comp_id = "0";
    public String RefreshForm = "";
    public String add = "";
    public String update = "";
    public String deleteB = "";
    public String addB = "";
    public String updateB = "";
    public static String status = "";
    public String StrSql = "";
    public static String msg = "";
    public String savePath = "";
    public String str1[] = {"", "", "", "", "", "", "", "", ""};
    public String str2[] = {"", "", "", "", "", "", "", "", ""};
    public String name = "";
    public String doc_id = "";
    public String client_id = "";
    public String module_id = "";
    public String proj_id = "";
    public String doc_value = "";
    public String doc_name = "";
    public long comp_docsize;
    public String comp_docformat = "";
    public String[] docformat;
    public String doc_remarks = "";
    public String doc_update_id = "";
    public String doc_update_date = "";
    public String chkPermMsg = "", msg2 = "", emp_id = "", branch_id = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
			comp_id = CNumeric(GetSession("comp_id", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            StrSql = "select config_doc_size,config_doc_formats from " + compdb(comp_id) + "axela_config";
            CachedRowSet crs2 =processQuery(StrSql, 0);
            while (crs2.next()) {
                comp_docsize = crs2.getLong("config_doc_size");
                comp_docformat = crs2.getString("config_doc_formats");
            }
            crs2.close();

            HttpSession session = request.getSession(true);
             if(!comp_id.equals("0"))
            {
            	emp_id = CNumeric(GetSession("emp_id", request));
                branch_id = CNumeric(GetSession("emp_branch_id", request));
//                savePath = CourseDocPath(comp_id);
                add = PadQuotes(request.getParameter("Add"));
                update = PadQuotes(request.getParameter("Update"));
                msg = PadQuotes(request.getParameter("msg"));
                client_id = PadQuotes(request.getParameter("client_id"));
                module_id = PadQuotes(request.getParameter("module_id"));
                proj_id = PadQuotes(request.getParameter("proj_id"));
                doc_id = PadQuotes(request.getParameter("doc_id"));

                if (client_id.equals("") || !isNumeric(client_id)) {
                    client_id = "0";
                }
                if (module_id.equals("") || !isNumeric(module_id)) {
                    module_id = "0";
                }
                if (proj_id.equals("") || !isNumeric(proj_id)) {
                    proj_id = "0";
                }
                if (doc_id.equals("") || !isNumeric(doc_id)) {
                    doc_id = "0";
                }
                if (client_id != null && !client_id.equals("")) {
                    name = ExecuteQuery("select concat(client_firm_name,' (',client_id,')') from " + compdb(comp_id) + "axela_client where client_id=" + client_id + "");
                }
                if (module_id != null && !module_id.equals("")) {
                    name = ExecuteQuery("select concat(module_name,' (',module_id,')') from " + maindb() + "module where module_id=" + module_id + "");
                }
                if (proj_id != null && !proj_id.equals("")) {
                    name = ExecuteQuery("select concat(proj_name,' (',proj_id,')') from " + compdb(comp_id) + "axela_project where proj_id=" + proj_id + "");
                }
                if (add == null) {
                    add = "";
                }
                if (update == null) {
                    update = "";
                }
                if (msg == null) {
                    msg = "";
                }
                if (add.equals("yes")) {
                    status = "Add";
                    displayform = "yes";
                } else if (update.equals("yes")) {
                    status = "Update";
                    displayform = "yes";
                }
                if (update.equals("yes")) {
                    doc_id = CNumeric(PadQuotes(request.getParameter("doc_id")));
                    PopulateFields(response);
                }
                boolean isMultipart = ServletFileUpload.isMultipartContent(new ServletRequestContext(request));
                if (isMultipart) {
//    				 Create a factory for disk-based file items
                    DiskFileItemFactory factory = new DiskFileItemFactory();
//    				 Set factory constraints
                    factory.setSizeThreshold(10 * 1024 * 1024);
                    File f = new File("d:/");
                    factory.setRepository(f);
//    				 Create a new file upload handler
                    ServletFileUpload upload = new ServletFileUpload(factory);
//    				 Set overall request size constraint
                    upload.setSizeMax(10 * 1024 * 1024);
//    				 Parse the request
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
                        if (str1[i].equals("Add Document")) {
                            //chkPermMsg= CheckPerm(comp_id, "emp_doc_add",request,response);
                            doc_update_id = "0";
                            doc_update_date = "";
                            FileItem item = (FileItem) iter.next();
                            if (!item.isFormField()) {
                                fileName = item.getName();
                                AddFields();
//                                File uploadedFile = new File(CourseDocPath() + fileName);
//                                if (uploadedFile.exists()) {
//                                    uploadedFile.delete();
//                                }
//                                item.write(uploadedFile);
                                displayform = "no";
                                RefreshForm = RefreshForm();
                            }
                        }
                        if (str1[i].equals("Update Document")) {
                            //chkPermMsg=CheckPerm(comp_id, "emp_doc_edit",request,response);
                            doc_update_id = CNumeric(GetSession("emp_id", request));
                            doc_update_date = ToLongDate(kknow());
                            FileItem item = (FileItem) iter.next();
                            if (!item.isFormField()) {
                                fileName = item.getName();
                                if (!fileName.equals("")) {
                                    int pos = fileName.lastIndexOf(".");
                                    if (pos != -1) {
                                        fileName = "doc_" + str1[6] + "_" + str1[5] + fileName.substring(pos);
                                    }
                                    long sizeInBytes = item.getSize();
                                    sizeInBytes = (sizeInBytes / 1000);
                                    docformat = comp_docformat.split(",");
                                    for (int j = 0; j < docformat.length; j++) {
                                        if (!fileName.substring(fileName.lastIndexOf(".")).toLowerCase().equals(docformat[j])) {
                                            msg2 = "Unable to upload " + fileName.substring(fileName.lastIndexOf(".")) + " format";
                                        }
                                    }
                                    if (comp_docsize < sizeInBytes) {
                                        msg = msg + msg2 + "<br>Uploaded file size is large!";
                                    }
                                }
                                if (msg.equals("")) {
                                    UpdateFieldsWithoutFile();
                                    if (!fileName.equals("")) {
                                        UpdateFields();
//                                        File uploadedFile = new File(CourseDocPath() + fileName);
//                                        if (uploadedFile.exists()) {
//                                            uploadedFile.delete();
//                                        }
//                                        item.write(uploadedFile);
                                    }
                                    displayform = "no";
                                    RefreshForm = RefreshForm();
                                }

                            }
                        }
                        if (str1[i].equals("Delete Document")) {
                            //chkPermMsg=CheckPerm(comp_id, "emp_doc_delete",request,response);
                            fileName = ExecuteQuery("select doc_value from " + compdb(comp_id) + "axela_doc where doc_id=" + doc_id + "");
//                            File uploadedFile = new File(CourseDocPath() + fileName);
//                            if (uploadedFile.exists()) {
//                                uploadedFile.delete();
//                            }
                            DeleteFields();
                            displayform = "no";
                            RefreshForm = RefreshForm();
                        }
                    }
                }
            }
            
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    protected void AddFields() {
        if (msg.equals("")) {
            try {
                String cmp_id = "";
                doc_id = ExecuteQuery("Select max(doc_id) as doc_id from " + compdb(comp_id) + "axela_doc");
                if (doc_id == null || doc_id.equals("")) {
                    doc_id = "0";
                }
                int i = Integer.parseInt(doc_id) + 1;
                doc_id = "" + i;
                int pos = fileName.lastIndexOf(".");
                if (pos != -1) {
                    fileName = "doc_" + str1[6] + "_" + doc_id + fileName.substring(pos);
                }
                if (client_id != null && !client_id.equals("")) {
                    cmp_id = "doc_client_id";
                }
                if (module_id != null && !module_id.equals("")) {
                    cmp_id = "doc_module_id";
                }
                if (proj_id != null && !proj_id.equals("")) {
                    cmp_id = "doc_proj_id";
                }
                StrSql = "insert into " + compdb(comp_id) + "axela_doc"
                        + "(doc_id,"
                        + "" + cmp_id + ","
                        + "doc_value,"
                        + "doc_name,"
                        + "doc_update_id,"
                        + "doc_update_date,"
                        + "doc_remarks)"
                        + "values	"
                        + "('" + doc_id + "',"
                        + "'" + str1[6] + "',"
                        + "'" + fileName + "',"
                        + "'" + str1[1] + "',"
                        + "'" + doc_update_id + "',"
                        + "'" + doc_update_date + "',"
                        + "'" + str1[2] + "')";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }

    }

    protected void PopulateFields(HttpServletResponse response) {
        try {
            StrSql = "select * from " + compdb(comp_id) + "axela_doc where doc_id=" + doc_id + "";
            CachedRowSet crs =processQuery(StrSql, 0);
            if (crs.isBeforeFirst()) {
                while (crs.next()) {
                    doc_name = crs.getString("doc_name");
                    doc_remarks = crs.getString("doc_remarks");
                }
            } else {
                response.sendRedirect(response.encodeRedirectURL("../portal/error.jsp?msg=Invalid Docs!"));
            }
            crs.close();
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    protected void UpdateFieldsWithoutFile() {
        if (msg.equals("")) {
            try {
                StrSql = "UPDATE " + compdb(comp_id) + "axela_doc"
                        + " SET "
                        + "doc_name = '" + str1[1] + "',"
                        + "doc_update_id= '" + doc_update_id + "', "
                        + "doc_update_date= '" + doc_update_date + "', "
                        + "doc_remarks = '" + str1[2] + "'"
                        + "where doc_id = " + doc_id + " ";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void UpdateFields() {
        //CheckForm();
        if (msg.equals("")) {
            try {
                StrSql = "UPDATE " + compdb(comp_id) + "axela_doc"
                        + " SET "
                        + "doc_value = '" + fileName + "',"
                        + "doc_name = '" + str1[1] + "',"
                        + "doc_update_id= '" + doc_update_id + "', "
                        + "doc_update_date= '" + doc_update_date + "', "
                        + "doc_remarks = '" + str1[2] + "'"
                        + "where doc_id = " + doc_id + " ";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    protected void DeleteFields() {
        if (msg.equals("")) {
            try {
                StrSql = "Delete from " + compdb(comp_id) + "axela_doc where doc_id =" + doc_id + "";
                updateQuery(StrSql);

            } catch (Exception ex) {
                SOPError("Axelaauto== " + this.getClass().getName());
                SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
            }
        }
    }

    public String RefreshForm() {
        if (displayform.equals("no")) {
            if (client_id != null && !client_id.equals("")) {
                RefreshForm = "onunload=\"opener.location ='managedocs.jsp?client_id=" + client_id + "'\"";
            }
            if (module_id != null && !module_id.equals("")) {
                RefreshForm = "onunload=\"opener.location ='managedocs.jsp?module_id=" + module_id + "'\"";
            }
            if (proj_id != null && !proj_id.equals("")) {
                RefreshForm = "onunload=\"opener.location ='managedocs.jsp?proj_id=" + proj_id + "'\"";
            }
        }
        return RefreshForm;
    }
}
