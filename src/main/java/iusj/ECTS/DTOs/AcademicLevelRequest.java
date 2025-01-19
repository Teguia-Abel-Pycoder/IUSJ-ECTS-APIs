package iusj.ECTS.DTOs;

import java.util.Map;

public class AcademicLevelRequest {
    private String levelName;
    private Map<String, String> courses;

    // Getters and Setters
    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public Map<String, String> getCourses() {
        return courses;
    }

    public void setCourses(Map<String, String> courses) {
        this.courses = courses;
    }
}
