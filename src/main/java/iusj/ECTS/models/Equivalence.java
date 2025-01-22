package iusj.ECTS.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Equivalence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long equivalenceId;

    private String schoolName;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String isiCoursesJson;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String srtCoursesJson;

    @Transient
    private Map<String, List<String>> isiCourses;

    @Transient
    private Map<String, List<String>> srtCourses;

    @PrePersist
    @PreUpdate
    public void serializeMaps() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.isiCoursesJson = objectMapper.writeValueAsString(isiCourses);
        this.srtCoursesJson = objectMapper.writeValueAsString(srtCourses);
    }

    @PostLoad
    public void deserializeMaps() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.isiCourses = objectMapper.readValue(isiCoursesJson, new TypeReference<Map<String, List<String>>>() {});
        this.srtCourses = objectMapper.readValue(srtCoursesJson, new TypeReference<Map<String, List<String>>>() {});
    }

    public Long getEquivalenceId() {
        return equivalenceId;
    }

    public void setEquivalenceId(Long equivalenceId) {
        this.equivalenceId = equivalenceId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getIsiCoursesJson() {
        return isiCoursesJson;
    }

    public void setIsiCoursesJson(String isiCoursesJson) {
        this.isiCoursesJson = isiCoursesJson;
    }

    public String getSrtCoursesJson() {
        return srtCoursesJson;
    }

    public void setSrtCoursesJson(String srtCoursesJson) {
        this.srtCoursesJson = srtCoursesJson;
    }

    public Map<String, List<String>> getIsiCourses() {
        return isiCourses;
    }

    public void setIsiCourses(Map<String, List<String>> isiCourses) {
        this.isiCourses = isiCourses;
    }

    public Map<String, List<String>> getSrtCourses() {
        return srtCourses;
    }

    public void setSrtCourses(Map<String, List<String>> srtCourses) {
        this.srtCourses = srtCourses;
    }

}
