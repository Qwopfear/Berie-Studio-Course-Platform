package berie.studio.beriestudiocourseplatform.entity.course;

import jakarta.persistence.*;

@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String description;

    @ManyToOne
    private Course course;
}
