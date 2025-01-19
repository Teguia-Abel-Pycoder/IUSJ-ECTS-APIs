package iusj.ECTS.models;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.Semester;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pvId;
    private ClassLevel classLevel;
    private Semester semester;
    private String pvPath;
    private String academicYear;
    private String uploadedBy;
    public String getPvPath() {
        return pvPath;
    }

    public void setPvPath(String pvPath) {
        this.pvPath = pvPath;
    }
}
