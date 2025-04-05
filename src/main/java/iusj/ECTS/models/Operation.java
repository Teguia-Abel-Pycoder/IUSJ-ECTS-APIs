package iusj.ECTS.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.OperationStatus;
import iusj.ECTS.enumerations.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationId;

    private String studentName;
    private String studentID;
    private String schoolName;

    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    private Boolean computed;

    @Enumerated(EnumType.STRING)
    private ClassLevel classLevel;

    @Enumerated(EnumType.STRING)
    private Semester semester;

    @ElementCollection
    @CollectionTable(name = "operation_grades", joinColumns = @JoinColumn(name = "operation_id"))
    @MapKeyColumn(name = "subject")
    @Column(name = "grade")
    private Map<String, String> grades;

    @ElementCollection
    @CollectionTable(name = "operation_results", joinColumns = @JoinColumn(name = "operation_id"))
    @MapKeyColumn(name = "subject")
    @Column(name = "result_value")
    private Map<String, Double> result;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "UTC")
    private Date dueDate;

    private double studentMgp;

    // Getters and 
    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public OperationStatus getStatus() {
        return status;
    }

    public void setStatus(OperationStatus status) {
        this.status = status;
    }

    public Boolean getComputed() {
        return computed;
    }

    public void setComputed(Boolean computed) {
        this.computed = computed;
    }

    public ClassLevel getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(ClassLevel classLevel) {
        this.classLevel = classLevel;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Map<String, String> getGrades() {
        return grades;
    }

    public void setGrades(Map<String, String> grades) {
        this.grades = grades;
    }

    public Map<String, Double> getResult() {
        return result;
    }

    public void setResult(Map<String, Double> result) {
        this.result = result;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public double getStudentMgp() {
        return studentMgp;
    }

    public void setStudentMgp(double studentMgp) {
        this.studentMgp = studentMgp;
    }
}
