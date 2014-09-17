package models.changeBlindness;

import play.db.ebean.Model;
import javax.persistence.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Entity
@Table (name="change_blindness_question")
public class Question extends Model{

    @Id
    public long id;
    public String pathOfPic1;
    public String pathOfPic2;
    public double answerAreaWidth;
    public double answerAreaHeight;
    public double positionOfChangeX;
    public double positionOfChangeY;

    public Question(){
        this.pathOfPic1 = "";
        this.pathOfPic2 = "";
        this.answerAreaWidth = 10.0;
        this.answerAreaHeight = 10.0;
        this.positionOfChangeX = 0.0;
        this.positionOfChangeY = 0.0;
    }

    public static Question create(String pathOfPic1, String pathOfPic2, double answerAreaWidth, 
        double answerAreaHeight, double positionOfChangeX, double positionOfChangeY){
        Question newQuestion = new Question();
        newQuestion.pathOfPic1 = pathOfPic1;
        newQuestion.pathOfPic2 = pathOfPic2;
        newQuestion.answerAreaWidth = answerAreaWidth;
        newQuestion.answerAreaHeight = answerAreaHeight;
        newQuestion.positionOfChangeX = positionOfChangeX;
        newQuestion.positionOfChangeY = positionOfChangeY;
        return newQuestion;
    }

    public Question(String pathOfPic1, String pathOfPic2, double answerAreaWidth, double answerAreaHeight, double positionOfChangeX, double positionOfChangeY){
        this.pathOfPic1 = pathOfPic1;
        this.pathOfPic2 = pathOfPic2;
        this.answerAreaWidth = answerAreaWidth;
        this.answerAreaHeight = answerAreaHeight;
        this.positionOfChangeX = positionOfChangeX;
        this.positionOfChangeY = positionOfChangeY;
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Change Blindness Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell firstCell = headerRow.createCell(col++);
            firstCell.setCellValue("pathOfPic1");

            Cell secondCell = headerRow.createCell(col++);
            secondCell.setCellValue("pathOfPic2");

            Cell thirdCell = headerRow.createCell(col++);
            thirdCell.setCellValue("Answer Area Width");

            Cell forthCell = headerRow.createCell(col++);
            forthCell.setCellValue("Answer Area Height");

            Cell fifthCell = headerRow.createCell(col++);
            fifthCell.setCellValue("Position of Change X");

            Cell sixthCell = headerRow.createCell(col++);
            sixthCell.setCellValue("Position of Change Y");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.pathOfPic1);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.pathOfPic2);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.answerAreaWidth);

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.answerAreaHeight);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.positionOfChangeX);

                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(temp.positionOfChangeY);

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
    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
