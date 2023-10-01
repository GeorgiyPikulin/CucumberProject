package utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelDataReader {
	public static HashMap<String, String> storeValues = new HashMap<>();

	public static List<HashMap<String, String>> data(String filepath, String sheetName) {
		List<HashMap<String, String>> myData = new ArrayList<>();
		try (FileInputStream fileInputStream = new FileInputStream(filepath);
			 XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {

			XSSFSheet sheet = workbook.getSheet(sheetName);
			Row HeaderRow = sheet.getRow(0);

			for (int rowIndex = 1; rowIndex < sheet.getPhysicalNumberOfRows(); rowIndex++) {
				Row currentRow = sheet.getRow(rowIndex);
				HashMap<String, String> currentHash = new HashMap<>();
				for (Cell currentCell : currentRow) {
					if (currentCell.getCellType() == CellType.STRING) {
						currentHash.put(HeaderRow.getCell(currentCell.getColumnIndex()).getStringCellValue(), currentCell.getStringCellValue());
					}
				}
				myData.add(currentHash);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return myData;
	}
}
