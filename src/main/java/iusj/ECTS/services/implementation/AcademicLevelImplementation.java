package iusj.ECTS.services.implementation;

import iusj.ECTS.models.AcademicLevel;
import iusj.ECTS.repositories.AcademicLevelRepository;
import iusj.ECTS.services.AcademicLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AcademicLevelImplementation implements AcademicLevelService {
    @Autowired
    private AcademicLevelRepository academicLevelRepository;

    public AcademicLevel createAcademicLevel(String levelName, Map<String, String> courses) {
        AcademicLevel academicLevel = new AcademicLevel();
        academicLevel.setLevelName(levelName);
        academicLevel.setCourses(courses);
        return academicLevelRepository.save(academicLevel);
    }
    // Method to add multiple courses to an existing academic level
    public AcademicLevel addCoursesToAcademicLevel(Long academicLevelId, Map<String, String> courses) {
        // Retrieve the existing AcademicLevel from the repository
        AcademicLevel academicLevel = academicLevelRepository.findById(academicLevelId)
                .orElseThrow(() -> new RuntimeException("Academic Level not found"));

        // Add the new courses to the existing courses map
        academicLevel.getCourses().putAll(courses);

        // Save the updated AcademicLevel entity
        return academicLevelRepository.save(academicLevel);
    }
    // Method to delete a course from an existing academic level
    public AcademicLevel deleteCourseFromAcademicLevel(Long academicLevelId, String courseName) {
        // Retrieve the existing AcademicLevel from the repository
        AcademicLevel academicLevel = academicLevelRepository.findById(academicLevelId)
                .orElseThrow(() -> new RuntimeException("Academic Level not found"));

        // Remove the course from the courses map
        academicLevel.getCourses().remove(courseName);

        // Save the updated AcademicLevel entity
        return academicLevelRepository.save(academicLevel);
    }
    public AcademicLevel modifyCourseInAcademicLevel(Long academicLevelId, String courseName, String newCourseDescription) {
        // Retrieve the existing AcademicLevel from the repository
        AcademicLevel academicLevel = academicLevelRepository.findById(academicLevelId)
                .orElseThrow(() -> new RuntimeException("Academic Level not found"));

        // Check if the course exists and modify the description
        if (academicLevel.getCourses().containsKey(courseName)) {
            academicLevel.getCourses().put(courseName, newCourseDescription);
        } else {
            throw new RuntimeException("Course not found in this academic level");
        }

        // Save the updated AcademicLevel entity
        return academicLevelRepository.save(academicLevel);
    }
}
