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
            Cell passwordCell = headerRow.createCell(1);
            passwordCell.setCellValue("IsCorrect");
            Statement stmt = DB.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            int row = 1;
            while(rs.next()){
                String username = rs.getString("username");
                boolean correct = rs.getBoolean("is_correct");
                Row dataRow = userSheet.createRow(row);
                Cell dataId = dataRow.createCell(0);
                dataId.setCellValue(username);
                Cell dataPwd = dataRow.createCell(1);
                dataPwd.setCellValue(correct);
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
