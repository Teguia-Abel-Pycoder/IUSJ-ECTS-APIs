package iusj.ECTS.services;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import iusj.ECTS.models.AcademicFile;
import iusj.ECTS.repositories.AcademicLevelRepository;
import iusj.ECTS.repositories.AcademicFileRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;

@Service
public class ExcelHandler {

    @Value("${upload.path}")
    private String uploadDir;
    @Autowired
    private AcademicLevelRepository academicLevelRepository;
    @Autowired
    private AcademicFileRepository academicFileRepository;
    private Map<String, String> courses;
    private Map<String, String> courses1;
    String academicYear = getCurrentAcademicYear();
    Map<String, String> idsNames = new HashMap<>();
    Map<String, Double> nameMarks = new HashMap<>();
    Map<String, String> idsNames1 = new HashMap<>();
    Map<String, Double> nameMarks1 = new HashMap<>();
    Map<String, ArrayList<Double>> marksPerCourse = new HashMap<>();
    Map<String, ArrayList<Double>> marksPerCourse1 = new HashMap<>();

    @Autowired
    private PercentileService percentileService;
    public static String getCurrentAcademicYear() {
        int currentYear = Year.now().getValue(); // Get the current year (e.g., 2025)
        int startYear = currentYear - 1;        // Academic year starts from the previous year
        int endYear = currentYear;             // Ends at the current year
        return startYear + "-" + endYear;      // Format as "YYYY-YYYY"
    }
    public Map<String, Double> mainFunction(ClassLevel lvl, Semester semester, Boolean mgp, Map<String, String> translatedCourses, double studentMgp){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory("2021-2022", lvl, semester, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                String filePath = optionalFile.get().getFilePath();
                readAndManipulateExcel(filePath, lvl, mgp);
                mgp = false;
                System.out.println("hasCoursesWithMoreThan30Marks? " + hasCoursesWithMoreThan30Marks(marksPerCourse));

                while (hasCoursesWithMoreThan30Marks(marksPerCourse)) {
                    academicYear = decreaseAcademicYear(academicYear);



                    Optional<AcademicFile> optionalFile2 = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(
                            academicYear, lvl, semester, FileCategory.PV
                    );

                    if (optionalFile2.isPresent()) {
                        System.out.println("optionalFile2 : " + optionalFile2.get().getFilePath());

                        filePath = optionalFile2.get().getFilePath();
                        readAndManipulateExcel(filePath, lvl, mgp);
                    } else {
                        System.out.println("No file found for academic year: " + academicYear);
                        break; // Exit loop if no file is found
                    }
                }
                ArrayList<Double> marksList = new ArrayList<>(nameMarks.values());
                // Printing using a for-each loop
                System.out.println("Marks per Course:");
                for (Map.Entry<String, ArrayList<Double>> entry : marksPerCourse.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                Map<String, Double> result = compute(marksPerCourse, translatedCourses,studentMgp,marksList);


                System.out.println("Course and Marks:");
                for (Map.Entry<String, Double> entry : result.entrySet()) {
                    System.out.println("Course: " + entry.getKey() + ", Mark: " + entry.getValue());
                }
                return  result;
            }
            else {
                System.out.println("No file found with the specified criteria.");
            }
        return null;
    }

    public String pvForMgp(ClassLevel classLevel, Semester semester){
        if(classLevel == ClassLevel.ISI3 && semester == Semester.SEMESTER2){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, classLevel, Semester.SEMESTER1, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }
        if(classLevel == ClassLevel.ISI4 && semester == Semester.SEMESTER2){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, classLevel, Semester.SEMESTER1, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }

        if(classLevel == ClassLevel.ISI5 && semester == Semester.SEMESTER2){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, classLevel, Semester.SEMESTER1, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }

        if(classLevel == ClassLevel.ISI5 && semester == Semester.SEMESTER1){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, ClassLevel.ISI4, Semester.SEMESTER2, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }
        if(classLevel == ClassLevel.ISI4 && semester == Semester.SEMESTER1){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, ClassLevel.ISI3, Semester.SEMESTER2, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }


        if(classLevel == ClassLevel.SRT3 && semester == Semester.SEMESTER2){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, classLevel, Semester.SEMESTER1, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }
        if(classLevel == ClassLevel.SRT4 && semester == Semester.SEMESTER2){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, classLevel, Semester.SEMESTER1, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }

        if(classLevel == ClassLevel.SRT5 && semester == Semester.SEMESTER2){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, classLevel, Semester.SEMESTER1, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }

        if(classLevel == ClassLevel.SRT5 && semester == Semester.SEMESTER1){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, ClassLevel.SRT4, Semester.SEMESTER2, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }
        if(classLevel == ClassLevel.SRT4 && semester == Semester.SEMESTER1){
            Optional<AcademicFile> optionalFile = academicFileRepository.findPvByAcademicYearAndClassLevelAndSemesterAndCategory(academicYear, ClassLevel.SRT3, Semester.SEMESTER2, FileCategory.PV);
            if (optionalFile.isPresent()) {
                System.out.println("File info: " + optionalFile);
                return optionalFile.get().getFilePath();
            }
        }
        return "Out of range!";
    }
    public static String decreaseAcademicYear(String academicYear) {
        // Split the academic year into start and end years
        String[] years = academicYear.split("-");
        if (years.length != 2) {
            throw new IllegalArgumentException("Invalid academic year format: " + academicYear);
        }

        int startYear = Integer.parseInt(years[0]); // Extract the start year
        int endYear = Integer.parseInt(years[1]);   // Extract the end year

        // Decrease both years by 1
        startYear--;
        endYear--;

        // Reconstruct the academic year
        return startYear + "-" + endYear;
    }

    void setCourses(ClassLevel level){
        switch (level) {
            case ISI3:
                courses = academicLevelRepository.findByLevelName("ISI3").getCourses();
                break;
            case ISI4:
                courses = academicLevelRepository.findByLevelName("ISI4").getCourses();
                break;
            case ISI5:
                courses = academicLevelRepository.findByLevelName("ISI5").getCourses();
                break;
            case SRT3:
                courses = academicLevelRepository.findByLevelName("SRT3").getCourses();
                break;
            case SRT4:
                courses = academicLevelRepository.findByLevelName("SRT4").getCourses();
                break;
            case SRT5:
                courses = academicLevelRepository.findByLevelName("SRT5").getCourses();
                break;

            default:
                System.out.println("Unknown academic level");
        }
        for (Map.Entry<String, String> entry : courses.entrySet()) {
            System.out.println("EU: " + entry.getKey() + ", Course name: " + entry.getValue());
        }
    }

    public void readAndManipulateExcel(String fileName, ClassLevel classLevel, Boolean mgpOkay) {
        setCourses(classLevel);
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
                                Map<String, String> idsNames =  extractIDnNamesPV(sheet, i + 2, j);
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
                                int k = j+1;
                                while(k < row.getLastCellNum()){
                                    if (courses.containsValue(row.getCell(k).getStringCellValue())) {

                                        int z = k;
                                        int y = i+3;
                                        String courseName = row.getCell(k).getStringCellValue();
                                        marksPerCourse.putIfAbsent(courseName, new ArrayList<>());

                                        while (z < row.getLastCellNum()){

                                            if(Objects.equals( sheet.getRow(i+1).getCell(z).getStringCellValue(), "M.Dis")){
                                                for (int a = 0; a < idsNames.values().size(); a++){
                                                    if(sheet.getRow(y).getCell(z).getStringCellValue().isBlank() || sheet.getRow(y).getCell(z).getStringCellValue().isEmpty()){
                                                        double mark = 0;
                                                    }else{
                                                        double mark = Double.parseDouble(sheet.getRow(y).getCell(z).getStringCellValue());
                                                        if(mark >= 10.0){
                                                            marksPerCourse.get(courseName).add(mark);
                                                        }
                                                    }

                                                    y++;
                                                }
                                                break;
                                            }
                                            z++;
                                        }
                                    }
                                    k++;
                                }
                            }

                            if (mgpOkay && cell.getCellType() == CellType.STRING && Objects.equals(cell.getStringCellValue(), "MGP")){
                                int k = j;
                                int l = i+2;
                                for (Map.Entry<String, String> entry : idsNames.entrySet()) {

                                    if(sheet.getRow(l).getCell(k).getStringCellValue().isBlank() || sheet.getRow(l).getCell(k).getStringCellValue().isEmpty()){
                                        double mgp = 0;
                                        nameMarks.put(entry.getValue(), mgp);
                                    }else{
                                        double mgp = Double.parseDouble(sheet.getRow(l).getCell(k).getStringCellValue());
                                            nameMarks.put(entry.getValue(), mgp);
//                                            System.out.println("ids ---  Name: " + entry.getKey() + " --- " + entry.getValue());
//                                            System.out.println("MGP: " + mgp);
                                    }

                                    l++;
                                }
                                mgpOkay = false;
                                nameMarks = sortByDoubleValues(nameMarks);
                            }

                        }
                    }
                }

            }
            System.out.println("Extracted Names and Marks:");
            for (Map.Entry<String, Double> entry : nameMarks.entrySet()) {
                System.out.println("Name: " + entry.getKey() + ", Mark: " + entry.getValue());
            }
//            System.out.println("Marks per Course:");
//            marksPerCourse.forEach((course, marks) -> {
//                System.out.println(course + " -> " + marks);
//            });
            workbook.close();
            fis.close();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read or manipulate Excel file.", e);
        }
    }
    public double readAndManipulateExcel1(String fileName, ClassLevel classLevel, Boolean mgpOkay, String name) {
        Double studentMgp = 0.0;
        setCourses(classLevel);
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
                                Map<String, String> idsNames = extractIDnNamesPV1(sheet, i + 2, j);
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
                                int k = j+1;
                                while(k < row.getLastCellNum()){
                                    if (courses.containsValue(row.getCell(k).getStringCellValue())) {

                                        int z = k;
                                        int y = i+3;
                                        String courseName = row.getCell(k).getStringCellValue();
                                        marksPerCourse1.putIfAbsent(courseName, new ArrayList<>());

                                        while (z < row.getLastCellNum()){

                                            if(Objects.equals( sheet.getRow(i+1).getCell(z).getStringCellValue(), "M.Dis")){
                                                for (int a = 0; a < idsNames1.values().size(); a++){
                                                    if(sheet.getRow(y).getCell(z).getStringCellValue().isBlank() || sheet.getRow(y).getCell(z).getStringCellValue().isEmpty()){
                                                        double mark = 0;
                                                    }else{
                                                        double mark = Double.parseDouble(sheet.getRow(y).getCell(z).getStringCellValue());
                                                        if(mark >= 10.0){
                                                            marksPerCourse1.get(courseName).add(mark);
                                                        }
                                                    }

                                                    y++;
                                                }
                                                break;
                                            }
                                            z++;
                                        }
                                    }
                                    k++;
                                }
                            }

                            if (mgpOkay && cell.getCellType() == CellType.STRING && Objects.equals(cell.getStringCellValue(), "MGP")){
                                int k = j;
                                int l = i+2;
                                for (Map.Entry<String, String> entry : idsNames1.entrySet()) {
                                    System.out.println("Id: " + entry.getKey() + ", NAmeee: " + entry.getValue());
                                }
                                for (Map.Entry<String, String> entry : idsNames1.entrySet()) {

                                    if(sheet.getRow(l).getCell(k).getStringCellValue().isBlank() || sheet.getRow(l).getCell(k).getStringCellValue().isEmpty()){
                                        double mgp = 0;
                                        nameMarks1.put(entry.getValue(), mgp);
                                    }else{
                                        double mgp = Double.parseDouble(sheet.getRow(l).getCell(k).getStringCellValue());
                                        nameMarks1.put(entry.getValue(), mgp);
//                                            System.out.println("ids ---  Name: " + entry.getKey() + " --- " + entry.getValue());
//                                            System.out.println("MGP: " + mgp);
                                    }

                                    l++;
                                }
                                mgpOkay = false;
                                nameMarks1 = sortByDoubleValues(nameMarks1);
                                for (Map.Entry<String, Double> entry : nameMarks1.entrySet()) {
                                    System.out.println("Name: " + entry.getKey() + ", Mark: " + entry.getValue());
                                }
                                studentMgp = nameMarks1.get(name);
                            }

                        }
                    }
                }

            }
            System.out.println("Extracted Names and Marks:");
            for (Map.Entry<String, Double> entry : nameMarks1.entrySet()) {
                System.out.println("Name: " + entry.getKey() + ", Mark: " + entry.getValue());
            }
//            System.out.println("Marks per Course:");
//            marksPerCourse.forEach((course, marks) -> {
//                System.out.println(course + " -> " + marks);
//            });
            workbook.close();
            fis.close();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read or manipulate Excel file.", e);
        }
        return studentMgp;
    }
    public boolean hasCoursesWithMoreThan30Marks(Map<String, ArrayList<Double>> marksCourses) {
        for (Map.Entry<String, ArrayList<Double>> entry : marksCourses.entrySet()) {
            String courseName = entry.getKey();
            ArrayList<Double> marks = entry.getValue();

            if (marks.size() > 30) {
                System.out.println("Course with more than 30 marks: " + courseName);
                return false;
            }
        }
        return true;
    }


    private Map<String, String> extractIDnNamesPV(Sheet sheet, int startRow, int column) {
        int currentRow = startRow;
        Row row = sheet.getRow(currentRow);

        while (row != null && row.getCell(column) != null &&
                row.getCell(column).getStringCellValue().length() > 2) {
            String id = row.getCell(column).getStringCellValue();
            String name = row.getCell(column + 1).getStringCellValue();
            idsNames.put(id, name);
            idsNames1.put(id, name);

            currentRow++;
            row = sheet.getRow(currentRow); // Move to the next row
        }
        idsNames = sortByValues(idsNames);
        idsNames1 = sortByValues(idsNames1);;

        // Print the content of the idsNames map
//        System.out.println("Extracted IDs and Names:");
//        for (Map.Entry<String, String> entry : idsNames.entrySet()) {
//            System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue());
//        }

        return idsNames;
    }
    private Map<String, String> extractIDnNamesPV1(Sheet sheet, int startRow, int column) {
        int currentRow = startRow;
        Row row = sheet.getRow(currentRow);

        while (row != null && row.getCell(column) != null &&
                row.getCell(column).getStringCellValue().length() > 2) {
            String id = row.getCell(column).getStringCellValue();
            String name = row.getCell(column + 1).getStringCellValue();
            idsNames.put(id, name);
            idsNames1.put(id, name);

            currentRow++;
            row = sheet.getRow(currentRow); // Move to the next row
        }
        idsNames = sortByValues(idsNames);
        idsNames1 = sortByValues(idsNames1);;

        // Print the content of the idsNames map
//        System.out.println("Extracted IDs and Names:");
//        for (Map.Entry<String, String> entry : idsNames.entrySet()) {
//            System.out.println("ID: " + entry.getKey() + ", Name: " + entry.getValue());
//        }

        return idsNames1;
    }
    public static Map<String, String> sortByValues(Map<String, String> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue()) // Sorting by values
                .collect(LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll);
    }
    public static Map<String, Double> sortByDoubleValues(Map<String, Double> map) {
        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey()) // Sort by values (marks)
                .collect(LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll);
    }

    public Map<String, Double> compute(Map<String, ArrayList<Double>> marksPerCourses, Map<String, String> translatedCourses, double stdtMgp, ArrayList<Double> mgps){
        Map<String, Double> result = new HashMap<>();
        marksPerCourses.forEach((key, value) ->{
//            System.out.println(percentileService.gradePercentileComparism(key, translatedCourses.get(key), percentileService.myPercentile(value), stdtMgp, mgps));
//            System.out.println("============================================");
//
//            System.out.println("\nkey ==== " + key);
//            System.out.println("\ntranslatedCourses.get(key)" + translatedCourses.get(key));
//            System.out.println("\npercentileService.myPercentile(value)" + percentileService.myPercentile(value));
//            System.out.println("\nstdtMgp"+ stdtMgp);
//            System.out.println("\nmgps" + mgps);
//            System.out.println("============================================");
            result.put(key, percentileService.gradePercentileComparism(key, translatedCourses.get(key), percentileService.myPercentile(value), stdtMgp, mgps));
        });
        return result;
    }
}
