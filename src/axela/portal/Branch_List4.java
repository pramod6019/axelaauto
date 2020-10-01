// Smitha Nag
package axela.portal;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cloudify.connect.Connect;
import cloudify.connect.Smart;

public class Branch_List4 extends Connect {

    public String LinkHeader = "<a href=home.jsp>Home</a> &gt; <a href=branch.jsp>Branches</a> &gt; <a href=branch-list.jsp?all=yes>List Branches</a>:";
    public String LinkListPage = "branch-list.jsp";
    public String LinkExportPage = "branch.jsp?smart=yes&target=" + Math.random() + "";
//    public String LinkFilterPage = "branch-filter.jsp";
    public String LinkAddPage = "<a href=branch-update.jsp?add=yes>Add New Branch...</a>";
    public String ExportPerm = "";
    public String emp_id = "0";
    public String comp_id = "0";
    public String StrHTML = "";
    public String msg = "";
    public String StrSql = "";
    public String StrSearch = "";
    public String smart = "";
    public String PageNaviStr = "";
    public String RecCountDisplay = "";
    public int recperpage = 0;
    public int PageCount = 10;
    public int PageCurrent = 0;
    public String PageCurrents = "";
    public String QueryString = "";
    public String all = "";
    public String branch_id = "0";
    public String emp_branch_id = "";
    public String branch_name = "";
    public String franchisee_id = "";
    public String advSearch = "";
    public Smart SmartSearch = new Smart();
    public String smartarr[][] = {
        {"Keyword", "text", "keyword_arr"},
        {"Branch ID", "numeric", "branch_id"},
        {"Branch Name", "text", "branch_name"},
        {"Branch Code", "text", "branch_code"},
        {"Branch Phone1", "text", "branch_phone1"},
        {"Branch Phone2", "text", "branch_phone2"},
        {"Branch Mobile1", "text", "branch_mobile1"},
        {"Branch Mobile2", "text", "branch_mobile2"},
        {"Branch Email1", "text", "branch_email1"},
        {"Branch Email2", "text", "branch_email2"},
        {"Branch Address", "text", "branch_add"},
        {"Branch Pin", "text", "branch_pin"},
        {"Active", "boolean", "branch_active"},
        {"Branch Notes", "text", "branch_notes"},
        {"Entry Date", "date", "branch_entry_date"},
        {"Modified Date", "date", "branch_modified_date"}
    };
    // duetime variables
    String item[];

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        try {
            CheckSession(request, response);
            HttpSession session = request.getSession(true);
            comp_id = CNumeric(GetSession("comp_id", request));
            if(!comp_id.equals("0")){
            emp_id = CNumeric(GetSession("emp_id", request));
            recperpage = Integer.parseInt(GetSession("emp_recperpage", request));
            CheckPerm(comp_id, "emp_role_id", request, response);
            emp_branch_id = CNumeric(GetSession("emp_branch_id", request));
            PageCurrents = CNumeric(PadQuotes(request.getParameter("PageCurrent")));
            smart = PadQuotes(request.getParameter("smart"));
            QueryString = PadQuotes(request.getQueryString());
            msg = PadQuotes(request.getParameter("msg"));
            branch_id = CNumeric(PadQuotes(request.getParameter("branch_id")));
            franchisee_id = CNumeric(PadQuotes(request.getParameter("franchisee_id")));
            all = PadQuotes(request.getParameter("all"));
            advSearch = PadQuotes(request.getParameter("advsearch_button"));

            double duehours = 8;
//            double s = 18.10 - 9.20;
//            SOP("s.." + s);
            String due_time = DueTime("20130408134500", 10.00, 18.00, duehours, "1", "0", "0", "0", "0", "0", "0");
//            SOP("due_time------" + due_time);
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

    public String DueTime(String starttime, double fromtime, double totime, double duehours, String Sun,
            String Mon, String Tue, String Wed, String Thu, String Fri, String Sat) {
        // from time
        int days_a = 1;
        int x = 0;
        Date mydate;
        String textday = "";
        String holiday = "1";
        String st = starttime.substring(8, 10) + "." + starttime.substring(10, 12);
        SOP("starttime......" + st);
        String str_fromtime = Double.toString(fromtime);
        SOP("str_fromtime......" + str_fromtime);
        String split_fromtime[] = str_fromtime.split("\\.");
        SOP("split_fromtime[0]......" + split_fromtime[0]);
        SOP("split_fromtime[1]......" + split_fromtime[1]);

        if (split_fromtime[0].length() < 2) {
            split_fromtime[0] = "0" + split_fromtime[0];
            SOP("0+split_fromtime[0]......" + split_fromtime[0] + "");
        }
        if (split_fromtime[1].length() < 2) {
            split_fromtime[1] = split_fromtime[1] + "0";
            SOP("split_fromtime[1]+0......" + split_fromtime[1] + "");
        }
        SOP("fromtime......" + fromtime);
        String date_fromtime = "20131210" + split_fromtime[0] + split_fromtime[1] + "00";
        SOP("date_fromtime......" + date_fromtime);

        // to time
        String str_totime = Double.toString(totime);
        SOP("str_totime......" + str_totime);
        String split_totime[] = str_totime.split("\\.");
        SOP("split_totime[0]......" + split_totime[0]);
        SOP("split_totime[1]......" + split_totime[1]);

        if (split_totime[0].length() < 2) {
            split_totime[0] = "0" + split_totime[0];
            SOP("0+split_totime[0]......" + split_totime[0] + "");
        }
        if (split_totime[1].length() < 2) {
            split_totime[1] = split_totime[1] + "0";
            SOP("split_totime[1]+0......" + split_totime[1] + "");
        }
        SOP("totime......" + totime);
        String date_totime = "20131210" + split_totime[0] + split_totime[1] + "00";
        SOP("date_totime......" + date_totime);

        double workingHours = getHoursBetween(StringToDate(date_fromtime), StringToDate(date_totime));
        SOP("convert(totime)......" + convert(totime));
        SOP("convert(fromtime)......" + convert(fromtime));
        workingHours = convert(totime) - convert(fromtime);
        SOP("workingHours...sa..." + workingHours / 60);
        int min_wh = (int) (workingHours);
        SOP("minwh...sa..." + min_wh);
        int min_dh = (int) (duehours * 60);
        SOP("due...sa..." + min_dh);
        int days = min_dh / min_wh;
        SOP("days...sa..." + days);
        int days_add = days;
        Date added_date = StringToDate(starttime);
        int minutes = min_dh % min_wh;

        SOP("days...sa...min" + minutes);
        int secs = minutes % 60;
        SOP("days_addsecs......" + secs + "");
        minutes = minutes / 60;
        SOP("minutes......" + minutes + "");


        if (Double.parseDouble(starttime.substring(8, 10) + "." + starttime.substring(10, 12))
                < (Double.parseDouble(split_fromtime[0] + "." + split_fromtime[1]))) {
            starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
            SOP("starttime alter......" + starttime);
            mydate = StringToDate(starttime);
            if (!CheckHoli(mydate, Sun, Mon, Tue, Wed, Thu, Fri, Sat).equals("0")) {
                while (CheckHoli(added_date, Sun, Mon, Tue, Wed, Thu, Fri, Sat).equals("1")) {
                    added_date = AddHoursDate(mydate, 1, 0, 0);
//                    CheckHoli(added_date, "0", "1", "0", "1", "0", "0", "0");
                }
                SOP("sssaaannn...n" + added_date);
                starttime = ToLongDate(added_date);
                starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
                added_date = StringToDate(starttime);
                SOP("sssaaannnn.." + added_date);
            }
        } else if (Double.parseDouble(starttime.substring(8, 10) + "." + starttime.substring(10, 12))
                > (Double.parseDouble(split_totime[0] + "." + split_totime[1]))) {
            /*Checking the Start time After The to time*/
            mydate = AddHoursDate(StringToDate(starttime), 1, 0, 0);
//            SOP("mydate......" + mydate + "");
            if (!CheckHoli(mydate, Sun, Mon, Tue, Wed, Thu, Fri, Sat).equals("0")) {
                while (CheckHoli(added_date, Sun, Mon, Tue, Wed, Thu, Fri, Sat).equals("1")) {
                    added_date = AddHoursDate(mydate, 1, 0, 0);
//                    CheckHoli(added_date, "0", "1", "0", "1", "0", "0", "0");
                }
                SOP("sssaaannnn2" + added_date);
                starttime = ToLongDate(added_date);
                starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
                added_date = StringToDate(starttime);
//                SOP("sssaaannnn2" + added_date);
            } else {
                starttime = ToLongDate(mydate);
                starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
                added_date = StringToDate(starttime);
                SOP("sssaaannnn22" + added_date);
            }

//            SOP("days_a......" + days_a + "");
//            starttime = ToLongDate(mydate);
//            starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
//            starttime = ToLongDate(AddHoursDate(StringToDate(starttime), days_a, 0, 0));
//            starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
            SOP("for late time......" + starttime + "");
        } else if (Double.parseDouble(starttime.substring(8, 10) + "." + starttime.substring(10, 12))
                < (Double.parseDouble(split_totime[0] + "." + split_totime[1]))
                && Double.parseDouble(starttime.substring(8, 10) + "." + starttime.substring(10, 12))
                > (Double.parseDouble(split_fromtime[0] + "." + split_fromtime[1]))) {
            String holi = CheckHoli(added_date, Sun, Mon, Tue, Wed, Thu, Fri, Sat);
            SOP("sssaaannnn" + holi);
            if (!CheckHoli(added_date, Sun, Mon, Tue, Wed, Thu, Fri, Sat).equals("0")) {
                while (CheckHoli(added_date, Sun, Mon, Tue, Wed, Thu, Fri, Sat).equals("1")) {
                    added_date = AddHoursDate(added_date, 1, 0, 0);
//                    CheckHoli(added_date, Sun, Mon, Tue, Wed, Thu, Fri, Sat);
                }
                starttime = ToLongDate(added_date);
                starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
//            SOP("sssaaannnn" + added_date);
                added_date = StringToDate(starttime);
            }
            SOP("actual start time h" + added_date);
//      
        }
        //for (int j = 1; j <= days; j++) {
        //smitha
        if (min_wh == min_dh && days!=1) {
            if (minutes == 0) {
                days_add = days_add - 1;
            }
        }
        if (days_add != 0) {
            for (int i = 1; i <= days_add; i++) {
                added_date = AddHoursDate(added_date, 1, 0, 0);
                SOP("2nd block added_date......" + added_date + "");
                int y = ReturnDayOfWeek(ToLongDate(added_date));
                SOP("2nd block x......" + y + "");
//            if(y!=7){

//            }
                if (y == 1) {
                    days_add = days_add + Integer.parseInt(Sun);
//                    days_add1 = days_add1 + Integer.parseInt(Sun);
                    SOP("111111111111111111......" + days_add + ".." + i);
                } else if (y == 2) {
                    days_add = days_add + Integer.parseInt(Mon);
//                    days_add1 = days_add1 + Integer.parseInt(Mon);
                    SOP("22222222222222222......" + days_add + ".." + i);
                } else if (y == 3) {
                    days_add = days_add + Integer.parseInt(Tue);
//                    days_add1 = days_add1 + Integer.parseInt(Tue);
                    SOP("333333333333333333......" + days_add + ".." + i);
                } else if (y == 4) {
                    days_add = days_add + Integer.parseInt(Wed);
                    SOP("44444444444444444......" + days_add + ".." + i);
                } else if (y == 5) {
                    days_add = days_add + Integer.parseInt(Thu);
//                    days_add1 = days_add1 + Integer.parseInt(Thu);
                    SOP("55555555555555555......" + days_add + ".." + i);
                } else if (y == 6) {
                    days_add = days_add + Integer.parseInt(Fri);
//                    days_add1 = days_add1 + Integer.parseInt(Fri);
                    SOP("66666666666666666......" + days_add + ".." + i);
                } else if (y == 7) {
                    days_add = days_add + Integer.parseInt(Sat);
//                    days_add1 = days_add1 + Integer.parseInt(Sat);
                    SOP("77777777777777777......" + days_add + ".." + i);
                }
//        }
            }

            SOP("days......" + days_add);
            SOP("minutes......" + minutes);
            SOP("workingHours......" + workingHours);
            SOP("starttime......" + starttime);
            Date new_date = AddHoursDate(StringToDate(starttime), days_add, 0, 0);
            SOP("1st block added_date......" + new_date + "");
            x = ReturnDayOfWeek(ToLongDate(new_date));
            added_date = new_date;

//        double a = Double.parseDouble(split_totime[0] + "." + split_totime[1]);
//        SOP("due_date is   a......" + a + " n x is.....");
//        String st1 = ToLongDate(added_date).substring(8, 10) + "." + ToLongDate(added_date).substring(10, 12);
//        SOP("due_date is   st1......" + st1 + " n x is.....");
//        double b = Double.parseDouble(st1);
//        SOP("due_date is   st1..ffg...." + b + " n x is.....");
////        double p =a-b;
//        double p = (double)(float) (a - b);
//        SOP("due_date is   satish.45....." + p + ".. n x is.....");
        }
        SOP("minutesafter"+minutes);
        if (minutes == 0 && (min_dh==min_wh) && days!=1) {
            starttime = ToLongDate(added_date);
            starttime = starttime.substring(0, 8) + split_totime[0] + split_totime[1] + "00";
            added_date = StringToDate(starttime);
        } else if ((days_add == 1 || minutes != 0) && days!=1) {
            double a = Double.parseDouble(split_totime[0] + "." + split_totime[1]);
            SOP("due_date is   a......" + a + " n x is.....");
            a = convert(a);
            SOP("due_date is   a......" + a + " n x is.....");
            String st1 = ToLongDate(added_date).substring(8, 10) + "." + ToLongDate(added_date).substring(10, 12);
            SOP("due_date is   st1......" + st1 + " n x is.....");
            double b = Double.parseDouble(st1);
            b = convert(b);
            SOP("due_date is   st1..ffg...." + b + " n x is.....");
            double p = a - b;
            SOP("due_date is   satish.45....." + p + ".. n x is.....");
            if (p >= (minutes * 60) + secs) {
                added_date = AddHoursDate(StringToDate(ToLongDate(added_date)), 0, 0, minutes * 60);
                SOP("due_date is......" + added_date + " n x is.....");
            } else {
                minutes = (minutes * 60) + secs;
                SOP("vvvp" + p);
                added_date = AddHoursDate(StringToDate(ToLongDate(added_date)), 0, 0, p);
                mydate = added_date;
                SOP("vvv" + added_date);
                SOP("minutes" + minutes);
                minutes = (int) (minutes - p);
                SOP("minutes2" + minutes);
                days_add = 1;
                String day = "0";
                int min = minutes;
                SOP("minutes......" + min + "");
                while (min != 0) {
                    mydate = AddHoursDate(StringToDate(ToLongDate(mydate)), 1, 0, 0);
//            added_date = AddHoursDate(added_date, 0, 1, 0);
                    SOP("2nd block added_date......" + added_date + "");
                    int y = ReturnDayOfWeek(ToLongDate(mydate));
                    if (y == 1) {
                        days_add = days_add + Integer.parseInt(Sun);
                        day = Sun;
                        SOP("111111111111111111......" + day + "");
                    } else if (y == 2) {
                        days_add = days_add + Integer.parseInt(Mon);
                        day = Mon;
                        SOP("22222222222222222......" + day + "");
                    } else if (y == 3) {
                        days_add = days_add + Integer.parseInt(Tue);
                        day = Tue;
                        SOP("333333333333333333......" + day + "");
                    } else if (y == 4) {
                        days_add = days_add + Integer.parseInt(Wed);
                        day = Wed;
                        SOP("44444444444444444......" + day + "");
                    } else if (y == 5) {
                        days_add = days_add + Integer.parseInt(Thu);
                        day = Thu;
                        SOP("55555555555555555......" + day + "");
                    } else if (y == 6) {
                        days_add = days_add + Integer.parseInt(Fri);
                        day = Fri;
                        SOP("66666666666666666......" + day + "");
                    } else if (y == 7) {
                        days_add = days_add + Integer.parseInt(Sat);
                        day = Sat;
                        SOP("77777777777777777......" + day + "");
                    }
                    if (day.equals("0")) {
                        min = 0;
                    }

                }
                SOP("minutes is......" + minutes + " n x is.....");

//            if (days_add == 0) {
//                minutes = minutes + (int) ((24.00 - Double.parseDouble(ToLongDate(added_date).substring(8, 10) + "." + ToLongDate(added_date).substring(10, 12))) + fromtime);
//            }
                SOP("due_date is...... ty" + days_add + " n x is.....");
                added_date = AddHoursDate(StringToDate(ToLongDate(added_date)), days_add, 0, 0);
                starttime = ToLongDate(added_date);
                starttime = starttime.substring(0, 8) + split_fromtime[0] + split_fromtime[1] + "00";
                SOP("due_date is.after days....." + added_date + " n x is.....");
                SOP("due_date is.after days.starttime...." + starttime + " n x is.....");
//            added_date = AddHoursDate(StringToDate(starttime), 0, minutes, 0);
                added_date = AddHoursDate(StringToDate(starttime), 0, 0, minutes);
                SOP("due_date is......" + added_date + " n x is.....");
            }
        }
        SOP("due_date is......" + added_date + " n x is.....");
        return ToLongDate(added_date).toString();

    }

    public double convert(double x) {
//        SOP("x" + x);
        String ze = x + "";
        x = Double.parseDouble(ze);
        String split_ze[] = ze.split("\\.");

        double y = Math.floor(x);
//        SOP("fjsj" + y);
//        float z = (float) (12.34 - 12);
//        x = (12.34 - 12.00);
//        SOP("xin" + x);
//        SOP("xin" + ze);
        y = y * 60;
//        SOP("....."+Integer.parseInt(split_ze[1]+"0"));
        y = y + Integer.parseInt(split_ze[1] + "0");
//        SOP("y======"+y);
        return y;

    }

    public String CheckHoli(Date mydate, String sun, String mon, String tue, String wed,
            String thu, String fri, String sat) {
        String holi = "0";
        int x = ReturnDayOfWeek(ToLongDate(mydate));
        if (x == 1 && sun.equals("1")) {
            holi = "1";

        } else if (x == 2 && mon.equals("1")) {
            holi = "1";
        } else if (x == 3 && tue.equals("1")) {
            holi = "1";
        } else if (x == 4 && wed.equals("1")) {
            holi = "1";
        } else if (x == 5 && thu.equals("1")) {
            holi = "1";
        } else if (x == 6 && fri.equals("1")) {
            holi = "1";
        } else if (x == 7 && sat.equals("1")) {
            holi = "1";
        }
        return holi;
    }

    public int CheckNext(Date added_date, int days_add, int x, String sun, String mon, String tue, String wed, String thu, String fri, String sat) {
//        for (int i = 1; i <= days_add; i++) {
//            added_date = AddHoursDate(added_date, 1, 0, 0);
//            SOP("2nd block added_date......" + added_date + "");
//            int y = ReturnDayOfWeek(ToLongDate(added_date));
//            SOP("2nd block x......" + y + "");
        if (x == 1) {
            days_add = days_add + Integer.parseInt(sun);
            SOP("111111111111111111......" + days_add + "");
        } else if (x == 2) {
            days_add = days_add + Integer.parseInt(mon);
            SOP("22222222222222222......" + days_add + "");
        } else if (x == 3) {
            days_add = days_add + Integer.parseInt(tue);
            SOP("333333333333333333......" + days_add + "");
        } else if (x == 4) {
            days_add = days_add + Integer.parseInt(wed);
            SOP("44444444444444444......" + days_add + "");
        } else if (x == 5) {
            days_add = days_add + Integer.parseInt(thu);
            SOP("55555555555555555......" + days_add + "");
        } else if (x == 6) {
            days_add = days_add + Integer.parseInt(fri);
            SOP("66666666666666666......" + days_add + "");
        }
//        }
        return days_add;
    }

    public Date CheckNextWorking(Date mydate, int days_a, int x, String sun, String mon, String tue,
            String wed, String thu, String fri, String sat) {
        int ddd = 1;
        for (int i = 1; i <= 7; i++) {

            if (x == 1) {
                if (sun.equals(1)) {
                    SOP("inside x==1......" + x);
                    days_a = days_a + Integer.parseInt(sun);
                    if (days_a != ddd) {
                        if (mon.equals(1)) {
                            x = ReturnDayOfWeek(ToLongDate(mydate));
                            SOP(";;;;;;;;;;;;;;;  X calculated 11111......" + x);
                        }
                    }
                    SOP("days_a for x==1......" + days_a);
                    mydate = AddHoursDate(mydate, 1, 0, 0);
                    SOP(";;;;;;;;;;;;;;;  mydate11111......" + mydate);
                }
            }
            if (x == 2) {
                if (mon.equals(1)) {
                    SOP("inside x==2......" + x);
                    days_a = days_a + Integer.parseInt(mon);
                    if (days_a != ddd) {
                        if (tue.equals(1)) {
                            x = ReturnDayOfWeek(ToLongDate(mydate));
                            SOP(";;;;;;;;;;;;;;;  X calculated 22222......" + x);
                        }
                    }
                    SOP("days_a for x==2......" + days_a);
                    mydate = AddHoursDate(mydate, 1, 0, 0);
                    SOP(";;;;;;;;;;;;;;;  mydate2222222......" + mydate);
                }
            }
            if (x == 3) {
                if (tue.equals(1)) {
                    SOP("inside x==3......" + x);
                    days_a = days_a + Integer.parseInt(tue);
                    if (days_a != ddd) {
                        if (wed.equals(1)) {
                            x = ReturnDayOfWeek(ToLongDate(mydate));
                            SOP(";;;;;;;;;;;;;;;  X calculated 3333......" + x);
                        }
                    }
                    SOP("days_a for x==3......" + days_a);
                    mydate = AddHoursDate(mydate, 1, 0, 0);
                    SOP(";;;;;;;;;;;;;;;  mydate333333......" + mydate);
                }
            }
            if (x == 4) {
                SOP("inside smitha x==4......" + x + "wed is" + wed);
                if (wed.equals(1)) {
                    SOP("inside x==4......" + x);
                    days_a = days_a + Integer.parseInt(wed);
                    if (days_a != ddd) {
                        if (thu.equals(1)) {
                            x = ReturnDayOfWeek(ToLongDate(mydate));
                            SOP(";;;;;;;;;;;;;;;  X calculated 44444......" + x);
                        }
                    }
                    SOP("days_a for x==4......" + days_a);
                    mydate = AddHoursDate(mydate, 1, 0, 0);
                    SOP(";;;;;;;;;;;;;;;  mydate44444......" + mydate);
                }
            }
            if (x == 5) {
                if (thu.equals(1)) {
                    SOP("inside x==5......" + x);
                    days_a = days_a + Integer.parseInt(thu);
                    if (days_a != ddd) {
                        if (fri.equals(1)) {
                            x = ReturnDayOfWeek(ToLongDate(mydate));
                            SOP(";;;;;;;;;;;;;;;  X calculated 5555......" + x);
                        }
                    }
                    SOP("days_a for x==5......" + days_a);
                    mydate = AddHoursDate(mydate, 1, 0, 0);
                    SOP(";;;;;;;;;;;;;;;  mydate555555......" + mydate);
                }
            }
            if (x == 6) {
                if (fri.equals(1)) {
                    SOP("inside x==6......" + x);
                    days_a = days_a + Integer.parseInt(fri);
                    if (days_a != ddd) {
                        if (sat.equals(1)) {
                            x = ReturnDayOfWeek(ToLongDate(mydate));
                            SOP(";;;;;;;;;;;;;;;  X calculated 6666......" + x);
                        }
                    }
                    SOP("days_a for x==6......" + days_a);
                    mydate = AddHoursDate(mydate, 1, 0, 0);
                    SOP(";;;;;;;;;;;;;;;  mydate77777......" + mydate);
                }
            }
            if (x == 7) {
                // if (sat.equals(1)) {
                SOP("inside x==7......" + x);
                days_a = days_a + Integer.parseInt(sat);
                if (days_a != ddd) {
                    if (sun.equals(1)) {
                        x = ReturnDayOfWeek(ToLongDate(mydate));
//                    CheckHolidays(mydate, days_a, x, sun, mon, tue, wed, thu, fri);
                        SOP(";;;;;;;;;;;;;;;  X calculated 7777......" + x);
                    }
                }
                SOP("days_a for x==7......" + days_a);
                mydate = AddHoursDate(mydate, 1, 0, 0);

                SOP(";;;;;;;;;;;;;;;  mydate77777......" + mydate);
                //}
            }
        }
        return mydate;

    }

    public void CheckHolidays(Date mydate, int days_a, int x, String sun, String mon, String tue, String wed, String thu, String fri) {

        if (x == 1) {
            SOP("inside x==1......" + x);
//                if(sun.equals(1)){
            days_a = days_a + Integer.parseInt(sun);
            SOP("days_a for x==1......" + days_a);
//                    x = ReturnDayOfWeek(ToLongDate(AddHoursDate(StringToDate(starttime), 1, 0, 0)));
//                    SOP(";;;;;;;;;;;;;;;  X calculated next time......" + x);
//                }
            mydate = AddHoursDate(mydate, 1, 0, 0);
            x = ReturnDayOfWeek(ToLongDate(mydate));
            SOP(";;;;;;;;;;;;;;;  mydate1111111......" + mydate);
            SOP(";;;;;;;;;;;;;;;  X calculated in 1111......" + x);
        }
//            x=2;
        if (x == 2) {
//                if(mon.equals(1)){
            SOP("inside x==2......" + x);
            days_a = days_a + Integer.parseInt(mon);
            SOP("days_a for x==2......" + days_a);
//                }
            mydate = AddHoursDate(mydate, 1, 0, 0);
            x = ReturnDayOfWeek(ToLongDate(mydate));
            SOP(";;;;;;;;;;;;;;;  mydate222222......" + mydate);
            SOP(";;;;;;;;;;;;;;;  X calculated 2222......" + x);
//
        }
//            x=3;
        if (x == 3) {
//                if(tue.equals(1)){
            SOP("inside x==3......" + x);
            days_a = days_a + Integer.parseInt(tue);
            SOP("days_a for x==3......" + days_a);
//                }
            mydate = AddHoursDate(mydate, 1, 0, 0);
            x = ReturnDayOfWeek(ToLongDate(mydate));
            SOP(";;;;;;;;;;;;;;;  mydate3333333......" + mydate);
            SOP(";;;;;;;;;;;;;;;  X calculated 3333......" + x);
        }
//            x=4;
        if (x == 4) {
//                if(wed.equals(1)){
            SOP("inside x==4......" + x);
            days_a = days_a + Integer.parseInt(wed);
            SOP("days_a for x==4......" + days_a);
//                }
            mydate = AddHoursDate(mydate, 1, 0, 0);
            x = ReturnDayOfWeek(ToLongDate(mydate));
            SOP(";;;;;;;;;;;;;;;  mydate444444......" + mydate);
            SOP(";;;;;;;;;;;;;;;  X calculated 4444......" + x);
        }
//            x=5;
        if (x == 5) {
//                if(thu.equals(1)){
            SOP("inside x==5......" + x);
            days_a = days_a + Integer.parseInt(thu);
            SOP("days_a for x==5......" + days_a);
//                }
            mydate = AddHoursDate(mydate, 1, 0, 0);
            x = ReturnDayOfWeek(ToLongDate(mydate));
            SOP(";;;;;;;;;;;;;;;  mydate55555......" + mydate);
            SOP(";;;;;;;;;;;;;;;  X calculated 55555......" + x);
        }
        //x=6;
        if (x == 6) {
//                if(fri.equals(1)){
            SOP("inside x==6......" + x);
            days_a = days_a + Integer.parseInt(fri);
            SOP("days_a for x==6......" + days_a);
//                }
            mydate = AddHoursDate(mydate, 1, 0, 0);
            x = ReturnDayOfWeek(ToLongDate(mydate));
            SOP(";;;;;;;;;;;;;;;  mydate6666......" + mydate);
            SOP(";;;;;;;;;;;;;;;  X calculated 66666......" + x);
        }

    }

    public String DateCheck(String xi, String sat, String sun) {
        int isHoliday = 0;
        String starttime = xi;
        int x = ReturnDayOfWeek(ToLongDate(AddHoursDate(StringToDate(xi), 1, 0, 0)));
        if (x == 1) {
            xi = ToLongDate(AddHoursDate(StringToDate(xi), 1, 0, 0));
            SOP("inside datecheck is.xi1....." + xi);

            DateCheck(xi, "1", "1");
//            isHoliday = isHoliday;
        } else if (x == 7) {
            xi = ToLongDate(AddHoursDate(StringToDate(xi), 1, 0, 0));
            SOP("inside datecheck is.xi2....." + xi);
            DateCheck(xi, "1", "1");
//            isHoliday = isHoliday;
        }
//        if (x == 1 || x == 7) {
//            DateCheck(xi,"1","1");
//        }
        SOP("inside datecheck is.xi23....." + xi);
        return xi;
    }
}
