package models;

import models.brownPeterson.Answer;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import play.data.format.Formats;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Entity
public class User extends Model{

	@Id
	@Column(length=20)
	public String username;
	@Column(nullable=false, length=20)
	public String password;
	public UserRole status = UserRole.STUDENT;
    public String firstName ="";
    public String lastName ="";
    public String gender ="";
    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Date   birthDate;
    public String section="";
    public String semester="";
    public String academicYear="";
    public int year = 1;
    public String eMail ="";
    public String faculty="";
    public String department="";


	@OneToMany
	List<Answer> answers = new ArrayList<Answer>();
	@OneToMany
	List<TimeLog> timeLogs = new ArrayList<TimeLog>();

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("User");

            Row headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(0);
            someCell.setCellValue("User Info");

            headerRow = userSheet.createRow(headerRowIndex++);

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("UserName");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Status");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("FirstName");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("LastName");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Gender");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("BirthDate(dd/MM/yyyy)");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Section");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Semester");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("AcademicYear");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Year");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("E-mail");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Faculty");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Department");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("TimeLog");

            List<User> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                User temp = tempList.get(row-headerRowIndex);
                col = 0;
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.username);

                String type = "";
                if (temp.status == UserRole.ADMIN)
                    type = "Admin";
                else if (temp.status == UserRole.STUDENT)
                    type = "Student";
                else if (temp.status == UserRole.SPECIAL)
                    type = "Special";
                else if (temp.status == UserRole.DUMMY)
                    type = "Dummy";
                else if (temp.status == UserRole.GUEST)
                    type = "Guest";
                else if (temp.status == UserRole.TA)
                    type = "TA";
                else if (temp.status == UserRole.DELETED)
                    type = "Deleted";

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.firstName);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.lastName);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.gender);

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(df.format(temp.birthDate));

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.section);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.semester);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.academicYear);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.year);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.eMail);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.faculty);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.department);

                String timeLogs_id = "";

                int subListSize = temp.timeLogs.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        timeLogs_id = timeLogs_id + String.valueOf( temp.timeLogs.get(i).id) + ",";
                    else
                        timeLogs_id = timeLogs_id + String.valueOf( temp.timeLogs.get(i).id);
                }
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(timeLogs_id);
            }

            //File file = new File("brown_peterson_user.xls");
            //FileOutputStream out = new FileOutputStream(file);
            //wb.write(out);
            //out.close();
            //return file;

        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }


	public static List<User> getAllUser() {
		return find.all();
	}

	public static User authenticate(String username, String password) {
        User user = find.where().eq("username", username).eq("password", password).findUnique();
		if(user == null){
            return null;
        }
        else if(user.status == UserRole.DELETED){
            return null;
        }
        else if(!username.equals(user.username) || !password.equals(user.password)){
            return null;
        }
        return user;
	}

    public int getAge(){
        if (birthDate != null){
            Date d = new Date();
            long diff = d.getTime() - birthDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            long years = days/365;
            return (int)years;
        }
        else
            return 0;
    }

	@SuppressWarnings("unchecked")
	public static Finder<String,User> find = new Finder(String.class,User.class);
}
