package iusj.ECTS.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcademicLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long academicLevelId;
    private String levelName;

    @ElementCollection
    @CollectionTable(name = "academic_level_courses", joinColumns = @JoinColumn(name = "academic_level_id"))
    @MapKeyColumn(name = "course_name")
    @Column(name = "course_description")
    private Map<String, String> courses;

    // Getter and Setter for academicLevelId
    public Long getAcademicLevelId() {
        return academicLevelId;
    }

    public void setAcademicLevelId(Long academicLevelId) {
        this.academicLevelId = academicLevelId;
    }

    // Getter and Setter for levelName
    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    // Getter and Setter for courses
    public Map<String, String> getCourses() {
        return courses;
    }

    public void setCourses(Map<String, String> courses) {
        this.courses = courses;
    }
}

