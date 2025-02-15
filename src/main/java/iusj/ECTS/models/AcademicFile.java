package iusj.ECTS.models;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.FileCategory;
import iusj.ECTS.enumerations.Semester;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClassLevel classLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Semester semester;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false, length = 9) // Example: "2023-2024"
    private String academicYear;

    @Column(nullable = false)
    private String uploadedBy;

    public Date getUploadedTime() {
        return uploadedTime;
    }
    @PrePersist
    protected void onCreate() {
        this.uploadedTime = new Date();
    }

    public void setUploadedTime(Date uploadedTime) {
        this.uploadedTime = uploadedTime;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileCategory category;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date uploadedTime;

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public FileCategory getCategory() {
        return category;
    }

    public void setCategory(FileCategory category) {
        this.category = category;
    }

}
