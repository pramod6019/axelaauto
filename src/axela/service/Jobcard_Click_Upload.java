/* Ved Prakash & $at!sh (16th, 17th July 2013 )*/
package axela.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;

public class Jobcard_Click_Upload extends Connect {

    private String fileName;
    public String StrSql = "";
    public String savePath = "";
    public String chkPermMsg = "";
    public String emp_id = "";
    public String comp_id = "0";
    public String emp_idsession = "";
    public String BranchAccess = "";
    public String ExeAccess = "";
    public String jc_id = "";
    public String img_id = "";
    public String img_title = "";
    public String img_value = "";
    public String img_entry_id = "";
    public String img_entry_date = "";

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            emp_id = CNumeric(GetSession("emp_id", request));
            jc_id = CNumeric(PadQuotes(request.getParameter("jc_id")));
            if (!jc_id.equals("0") && ReturnPerm(comp_id, "emp_service_jobcard_add", request).equals("1")) {
                img_id = ExecuteQuery("SELECT COALESCE(MAX(img_id), 0) + 1 AS img_id FROM " + compdb(comp_id) + "axela_service_jc_img");
                savePath = JobCardImgPath(comp_id);

                if (!request.getMethod().equalsIgnoreCase("post")) {
                    SOP("exit()");
                }
                String folder = savePath;
                fileName = "jcimg_" + img_id + ".jpg";

                String original = folder + fileName;
                File f = new File(folder);
                if (!f.exists()) {
                    f.mkdir();
                }

                /* The JPEG snapshot is sent as raw input: */
                FileOutputStream fileOutputStream = new FileOutputStream(original);
                int res;
                while ((res = request.getInputStream().read()) != -1) {
                    fileOutputStream.write(res);
                }
                fileOutputStream.close();
                response.getWriter().append(original);
                AddFields();
            }
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        doPost(request, response);
    }

    protected void AddFields() {
        try {
            int pos = fileName.lastIndexOf(".");
            if (pos != -1) {
                fileName = "jcimg_" + img_id + fileName.substring(pos);
            }
            img_title = "jcimg_" + img_id;
            img_entry_id = emp_id;
            img_entry_date = ToLongDate(kknow());

            StrSql = "INSERT INTO " + compdb(comp_id) + "axela_service_jc_img"
                    + " (img_id,"
                    + " img_jc_id,"
                    + " img_value,"
                    + " img_title,"
                    + " img_entry_id,"
                    + " img_entry_date)"
                    + " VALUES"
                    + " (" + img_id + ","
                    + " " + jc_id + ","
                    + " '" + fileName + "',"
                    + " '" + img_title + "',"
                    + " '" + img_entry_id + "',"
                    + " '" + img_entry_date + "')";
//                SOP("StrSql==" + StrSqlBreaker(StrSql));
            updateQuery(StrSql);
        } catch (Exception ex) {
            SOPError("Axelaauto== " + this.getClass().getName());
            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
        }
    }
}
