package models.brownPeterson;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import javax.persistence.*;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonManagedReference;
@Entity
@Table (name="brown_peterson_question")
public class Question extends Model {
	@Id
	public Long id;
	@Column(nullable=false, length=20)
	public String firstWord;
	@Column(nullable=false, length=20)
	public String secondWord;
	@Column(nullable=false, length=20)
	public String thirdWord;
	public String trigramType = Trial.WORD;
	public String trigramLanguage = Trial.ENGLISH;

	@OneToMany(cascade=CascadeType.REMOVE, mappedBy = "question")
	private List<Quiz> quizzes = new ArrayList<Quiz>();

    public List<Quiz> findQuizzes(){
        return quizzes;
    }

	public Question (String firstWord, String secondWord,String thirdWord){
		this.firstWord = firstWord;
		this.secondWord = secondWord;
		this.thirdWord = thirdWord;
	}

	public static Question create(String firstWord, String secondWord, String thirdWord, 
		String trigramType, String trigramLanguage){
		Question question = new Question(firstWord, secondWord, thirdWord);
		question.trigramType = trigramType;
		question.trigramLanguage = trigramLanguage;
		return question;
	}

	public static Question findQuestionById(int id) {
		return find.byId(new Long(id));
	}

	public static List<Question> getQuestionListBy(int amount){
		List<Question> allQuestion = new ArrayList<Question>();
        List<Question> questionList = new ArrayList<Question>();
        allQuestion = find.all();
        if(allQuestion.size() >= amount){
	        Collections.shuffle(allQuestion);
			for(int i = 0;i < amount;i++){
	        	questionList.add(allQuestion.get(i));
			}
			return questionList;
		}
		return null;
	}

	public static List<Question> findInvolving(List<Quiz> quizzes){
		List<Question> questions = new ArrayList<Question>();
		for(Quiz quiz : quizzes){
			questions.add(quiz.question);
		}
		return questions;
	}

	public static Question randomNewQuestion(List<Question> questions){
		Random random = new Random();
		int index = random.nextInt(questions.size());
		Question question = questions.get(index);
		return question;
	}

	public String toString(){
		return firstWord + ", " + secondWord + ", " + thirdWord;
	}

	public static void generateQuestion(String questionText, String trigramType, String trigramLanguage){
		String[] questions = questionText.split("\\r?\\n");
        int counter = 0;

        for(String question : questions){
            try{
                String[] words = null;
                if(question.contains(",")){
                    words = question.split(",");
                }else{
                    words = question.split("\\s+");
                }   
                create(words[0],words[1],words[2], trigramType, trigramLanguage).save();
            }catch(Exception e){
                System.out.println("generateQuestion error");
            }
            counter++;
        }
	}

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Brown Peterson Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell firstCell = headerRow.createCell(col++);
            firstCell.setCellValue("first word");

            Cell secondCell = headerRow.createCell(col++);
            secondCell.setCellValue("second word");

            Cell thirdCell = headerRow.createCell(col++);
            thirdCell.setCellValue("third word");

            Cell userCell = headerRow.createCell(col++);
            userCell.setCellValue("Trigram Type");

            Cell correctCell = headerRow.createCell(col++);
            correctCell.setCellValue("Trigram Language");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Ids");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell dataFirst = dataRow.createCell(col++);
                dataFirst.setCellValue(temp.firstWord);

                Cell dataSecond = dataRow.createCell(col++);
                dataSecond.setCellValue(temp.secondWord);

                Cell dataThird = dataRow.createCell(col++);
                dataThird.setCellValue(temp.thirdWord);

                Cell dataType = dataRow.createCell(col++);
                dataType.setCellValue(temp.trigramType);

                Cell dataLanguage = dataRow.createCell(col++);
                dataLanguage.setCellValue(temp.trigramLanguage);


                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                Cell dataQuiz = dataRow.createCell(col++);
                dataQuiz.setCellValue(quizzes_id);

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
	public static Finder<Long,Question> find = new Finder(Long.class,Question.class);

}
