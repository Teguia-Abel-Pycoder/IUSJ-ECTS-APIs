package iusj.ECTS.models;

import iusj.ECTS.enumerations.ClassLevel;
import iusj.ECTS.enumerations.Semester;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long operationId;

    private String studentName;

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

    private double studentMgp;
}
