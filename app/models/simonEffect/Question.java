package models.simonEffect;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.Random;

import play.db.ebean.Model.Finder;

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
@Table (name = "simon_effect_question")
public class Question extends Model{

    public static final char[] alphabets = {'O', 'X'};
    public static final String[] colors = {"red", "green"};

    @Id
    public long id;
    public String color;
    public char alphabet;
    public String direction;
    @OneToMany(cascade=CascadeType.REMOVE,mappedBy="question")
    private List<Quiz> quizzes;

    public Question(String color,char alphabet , String direction) {
    	this.color = color;
    	this.alphabet = alphabet;
    	this.direction = direction;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Simon Effect Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Color");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Alphabet");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Direction");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Quiz Ids");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.color);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.alphabet);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.direction);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(quizzes_id);

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

    public Question() {

    }


    public static Question findQuestionByType(QuestionType questionType) {
        Random random = new Random();
        String alp = alphabets[random.nextInt(alphabets.length)] + "";
        String cl = "";
        if(questionType == QuestionType.ONEFEATURE){
            if(alp.equals("O")){
                cl = "green";
            }else if(alp.equals("X")){
                cl = "red";
            }
        }
        else if(questionType == QuestionType.TWOFEATURE){
            cl = colors[random.nextInt(colors.length)];
        }
        return find.where().eq("alphabet", alp).eq("color", cl).findUnique();
    }
}
