package iusj.ECTS.services;

import iusj.ECTS.models.AcademicLevel;

import java.util.Map;

public interface AcademicLevelService {
    AcademicLevel createAcademicLevel(String levelName, Map<String, String> courses);
    AcademicLevel addCoursesToAcademicLevel(Long academicLevelId, Map<String, String> courses);
    AcademicLevel deleteCourseFromAcademicLevel(Long academicLevelId, String courseName);
    AcademicLevel modifyCourseInAcademicLevel(Long academicLevelId, String courseName, String newCourseDescription);
}
