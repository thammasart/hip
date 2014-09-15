package controllers;

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

/**
 * Created by ohmini on 12/9/2557.
 */
public class ExportController extends Controller {
    public static Result download() throws IOException {

        String sql = "SELECT * FROM brown_peterson_answer a LEFT JOIN user u ON(a.user_username = u.username)";
        try{
            Workbook wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("test");
            Row headerRow = userSheet.createRow(0);
            Cell idHeaderCell = headerRow.createCell(0);
            idHeaderCell.setCellValue("ID");
            Cell firstCell = headerRow.createCell(1);
            firstCell.setCellValue("first word");
            Cell secondCell = headerRow.createCell(2);
            firstCell.setCellValue("second word");
            Cell thirdCell = headerRow.createCell(3);
            firstCell.setCellValue("third word");

            Cell correctCell = headerRow.createCell(4);
            correctCell.setCellValue("IsCorrect");
            Statement stmt = DB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int row = 1;
            while(rs.next()){
                String username = rs.getString("username");
                boolean correct = rs.getBoolean("is_correct");
                Row dataRow = userSheet.createRow(row);
                Cell dataId = dataRow.createCell(0);
                dataId.setCellValue(username);
                Cell dataFirst = dataRow.createCell(1);
                dataFirst.setCellValue(rs.getString("first_word"));
                Cell dataSecond = dataRow.createCell(2);
                dataSecond.setCellValue(rs.getString("second_word"));
                Cell dataThird = dataRow.createCell(3);
                dataThird.setCellValue(rs.getString("third_word"));
                Cell dataCorrect = dataRow.createCell(4);
                dataCorrect.setCellValue(correct);
                row++;
            }
            File file = new File("user.xls");
            FileOutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
            return ok(file);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return badRequest();
    }
}
