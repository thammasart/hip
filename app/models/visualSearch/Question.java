package models.visualSearch;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

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
@Table (name="visual_search_question")

public class Question extends Model{
    @Id
    public long id;
    @Column(columnDefinition = "TEXT")
    public String sharps;

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Visual Search Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Sharps(Shape)");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.sharps);

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

    public Question(){
    }

    public static List<Question> findInvolving(Quiz quiz){
        return Question.find.where().eq("quiz", quiz).findList();
    }

    public String toStringJson(){
        return Json.stringify(Json.toJson(this));
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
