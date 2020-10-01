//package axela.sales;
//
//import au.com.bytecode.opencsv.CSVWriter;
//import cloudify.connect.Connect;
//
//import com.mysql.jdbc.ResultSetMetaData;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//public class ExportToCSV {
//
//    public void Export(HttpServletResponse response, ResultSet rs, String filename) {
//        try {
//            Connect ct = new Connect();
//            String comp_id = "0";
////            HttpSession session = request.getSession(true);
////            String comp_id = ct.CNumeric(GetSession("comp_id", request));
//            //Create the List to add the ResultSet Elements
//            List demo_list = new ArrayList();
//            int columncount = 0;
//            String[] data_arr, head_arr;
//
//            ResultSetMetaData rsmd = (ResultSetMetaData) crs.getMetaData();
//            if (crs.isBeforeFirst()) {
//                columncount = rsmd.getColumnCount();
//                head_arr = new String[columncount];
//                data_arr = new String[columncount];
//                for (int k = 1; k <= columncount; k++) {
//                    head_arr[k - 1] = rsmd.getColumnLabel(k);
//                }
//                demo_list.add(head_arr);
//
//                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//                response.setHeader("Content-Disposition", "attachment; filename=" + filename + ".csv");
//
//                while (crs.next()) {
//                    for (int j = 1; j <= columncount; j++) {//Add the elements in the List
//                        data_arr[j - 1] = crs.getString(j);
//                    }
//                    demo_list.add(data_arr);
//                }
//
//                CSVWriter writer = new CSVWriter(new FileWriter(ct.TemplatePath(comp_id) + "template.csv"));
//                writer.writeAll(demo_list);
//                writer.close();
//
//                File file = new File(ct.TemplatePath(comp_id) + "template.csv");
//                FileInputStream fileInputStream = new FileInputStream(file);
//                int i;
//                while ((i = fileInputStream.read()) != -1) {
//                    response.getOutputStream().write(i);
//                }
//                response.getOutputStream().flush();
//                response.getOutputStream().close();
//                fileInputStream.close();
//            }
//        } catch (Exception ex) {
//            SOPError("Axelaauto== " + this.getClass().getName());
//            SOPError("Error in " + new Exception().getStackTrace()[0].getMethodName() + ": " + ex);
//        }
//    }
//}
