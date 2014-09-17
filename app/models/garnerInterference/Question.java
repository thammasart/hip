package models.garnerInterference;

import com.fasterxml.jackson.annotation.JsonBackReference;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Random;
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
@Table (name="garner_interference_question")
public class Question extends Model{
    @Id
    public long id;
    public String colorPic;
    public String sizePic;
    public String colorQuestion;
    public String sizeQuestion;
    public boolean colorMatch;
    public boolean sizeMatch;

    @OneToOne(mappedBy = "question", cascade = CascadeType.REMOVE)
    private Quiz quiz;

    public static final String[] SIZES = {"big", "small"};
    public static final String[] COLORS = {"dark", "light"};

    public Question(){
    	
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

    public static Question create(QuestionType questionType, boolean isNotFake) {
        Question question = new Question();
        switch (questionType){
            case COLOR:createSimpleColorQuestion(question,isNotFake);break;
            case SIZE:createSimpleSizeQuestion(question, isNotFake);break;
            case BOTH:createSimpleBiDirectionQuestion(question, isNotFake);break;
        }
        return question;
    }

    private static void createSimpleBiDirectionQuestion(Question question, boolean isNotFake) {
        question.sizePic = generateSizePic();
        question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
        question.sizeMatch = isNotFake ? true : false;
        question.colorPic = generateColorPic();
        question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
        question.colorMatch = isNotFake ? true : false;
    }

    private static String calculateSizeFake(String sizePic) {
        if(sizePic.equals(SIZES[0]))
            return SIZES[1];
        return SIZES[0];
    }



    private static void createSimpleSizeQuestion(Question question, boolean isNotFake) {
        question.sizePic = generateSizePic();
        question.sizeQuestion = isNotFake ? question.sizePic : calculateSizeFake(question.sizePic);
        question.sizeMatch = isNotFake ? true : false;
        question.colorPic = generateColorPic();
    }

    private static String calculateColorFake(String colorPic) {
        return colorPic.equals(COLORS[0]) ? COLORS[1] : COLORS[0];
    }

    private static String generateColorPic() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }

    private static String generateSizePic() {
        return SIZES[new Random().nextInt(SIZES.length)];
    }

    private static void createSimpleColorQuestion(Question question,boolean isNotFake) {
        question.colorPic = generateColorPic();
        question.colorQuestion = isNotFake ? question.colorPic : calculateColorFake(question.colorPic);
        question.colorMatch = isNotFake ? true : false;
        question.sizePic = generateSizePic();
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Garner Interference Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell firstCell = headerRow.createCell(col++);
            firstCell.setCellValue("ColorPic");

            Cell secondCell = headerRow.createCell(col++);
            secondCell.setCellValue("SizePic");

            Cell thirdCell = headerRow.createCell(col++);
            thirdCell.setCellValue("Color Question");

            Cell forthCell = headerRow.createCell(col++);
            forthCell.setCellValue("Size Question");

            Cell fifthCell = headerRow.createCell(col++);
            fifthCell.setCellValue("Color Match");

            Cell sixthCell = headerRow.createCell(col++);
            sixthCell.setCellValue("Size Match");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Id");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.colorPic);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.sizePic);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.colorQuestion);

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.sizeQuestion);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.colorMatch);

                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(temp.sizeMatch);

                Cell data7 = dataRow.createCell(col++);
                data7.setCellValue(temp.quiz.id);

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
}
