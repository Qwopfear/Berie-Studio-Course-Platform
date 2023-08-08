package berie.studio.beriestudiocourseplatform.entity.course;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Course {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private Long price;
    private String title;
    private String description;
    @OneToMany
    @JoinColumn(name = "lesson_id")
    private List<Lesson> lessons;

}
