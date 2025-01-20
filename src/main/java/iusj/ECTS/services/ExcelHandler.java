package iusj.ECTS.services;

import iusj.ECTS.repositories.AcademicLevelRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class ExcelHandler {

    @Value("${upload.path}")
    private String uploadDir;
    @Autowired
    private AcademicLevelRepository academicLevelRepository;
    private Map<String, String> courses;
    Map<String, String> idsNames = new HashMap<>();

    void setCourses(String level){
        switch (level) {
            case "ISI3":
                courses = academicLevelRepository.findByLevelName("ISI3").getCourses();
                break;
            case "ISI4":
                courses = academicLevelRepository.findByLevelName("ISI4").getCourses();
                break;
            case "ISI5":
                courses = academicLevelRepository.findByLevelName("ISI5").getCourses();
                break;
            case "SRT3":
                courses = academicLevelRepository.findByLevelName("SRT3").getCourses();
                break;
            case "SRT4":
                courses = academicLevelRepository.findByLevelName("SRT4").getCourses();
                break;
            case "SRT5":
                courses = academicLevelRepository.findByLevelName("SRT5").getCourses();
                break;

            default:
                System.out.println("Unknown academic level");
        }
        System.out.println("Courses:=======================================================================");
        for (Map.Entry<String, String> entry : courses.entrySet()) {
            System.out.println("EU: " + entry.getKey() + ", Course name: " + entry.getValue());
        }
    }

    public void readAndManipulateExcel(String fileName) {
        setCourses("SRT4");
        try {
            // Build the full path to the Excel file
            File excelFile = Paths.get(uploadDir, fileName).toFile();

            // Open the file as an InputStream
            FileInputStream fis = new FileInputStream(excelFile);

            // Open the workbook
            Workbook workbook = new XSSFWorkbook(fis);

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Flag to track if "Matricules" has been found and processed
            boolean foundMatricules = false;

            for (int i = 0; i <= sheet.getLastRowNum(); i++) { // Iterate over rows
                Row row = sheet.getRow(i);
                if (row != null) {
//                    System.out.println(  row.getRowNum() + ": number of physical (non-empty) cells: "+row.getPhysicalNumberOfCells());

                    for (int j = 0; j < row.getLastCellNum(); j++) { // Iterate over cells in the row
                        Cell cell = row.getCell(j);
                        if (cell != null && cell.getCellType() == CellType.STRING) {
                            if (!foundMatricules && Objects.equals(cell.getStringCellValue(), "Matricules")) {
                                foundMatricules = true; // Mark as processed

                                // Extract data from subsequent rows
                                Map<String, String> idsNames = extractIDnNamesPV(sheet, i + 2, j);
                                System.out.println("Extracted Data: " + idsNames);

                                break; // Exit the inner loop
                            }
                        }
                    }
                    if (foundMatricules) break; // Exit the outer loop after processing "Matricules"
                }
            }
            for (int i = 0; i <= sheet.getLastRowNum(); i++) { // Iterate over rows
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < row.getLastCellNum(); j++) { // Iterate over cells in the row
                        Cell cell = row.getCell(j);
                        if (cell != null && cell.getCellType() == CellType.STRING) {

                            if (cell.getCellType() == CellType.STRING && Objects.equals(cell.getStringCellValue(), "MATIERES")){
                                System.out.println("OKAY00000000000000000000000000000000000000000000000000000000000000000000000");
                                int k = j+1;
                                System.out.println(k + ", on "+ "Max "+ row.getLastCellNum());
                                while(k < row.getLastCellNum()){
                                    System.out.println("\n"+row.getCell(k).getStringCellValue());
                                    if (courses.containsValue(row.getCell(k).getStringCellValue())) {
                                        System.out.println("OKAYaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa0");

                                        int z = k;
                                        int y = i+3;
                                        System.out.println("CourseName" + row.getCell(k).getStringCellValue());
                                        while (z < row.getLastCellNum()){
                                            System.out.println("OKAY666666666666 in zzzzzzzzzzzzzzz");

                                            if(Objects.equals( sheet.getRow(i+1).getCell(z).getStringCellValue(), "M.Dis")){
                                                System.out.println("Yes  " + idsNames.values().size());
                                                for (int a = 0; a < idsNames.values().size(); a++){
                                                    System.out.println(sheet.getRow(y).getCell(z).getStringCellValue());
                                                    y++;
                                                }
                                                System.out.println("==============================================================");
                                                break;
                                            }
                                            z++;
                                        }
                                    }
                                    k++;
                                }
                            }
                        }
                    }
                }
            }

            // Close the workbook and InputStream
            workbook.close();
            fis.close();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read or manipulate Excel file.", e);
        }
    }


    private Map<String, String> extractIDnNamesPV(Sheet sheet, int startRow, int column) {
        int currentRow = startRow;
        Row row = sheet.getRow(currentRow);

        while (row != null && row.getCell(column) != null &&
                row.getCell(column).getStringCellValue().length() > 2) {
            String id = row.getCell(column).getStringCellValue();
            String name = row.getCell(column + 1).getStringCellValue();
            idsNames.put(id, name);

            currentRow++;
            row = sheet.getRow(currentRow); // Move to the next row
        }

        // Print the content of the idsNames map
        System.out.println("Extracted IDs and Names:");
        for (Map.Entry<String, String> entry : idsNames.entrySet()) {
            System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue());
        }

        return idsNames;
    }
}
