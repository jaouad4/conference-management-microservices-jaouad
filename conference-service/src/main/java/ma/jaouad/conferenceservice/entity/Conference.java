package ma.jaouad.conferenceservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conference {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeConference type;
    
    @Column(nullable = false)
    private LocalDate date;
    
    @Column(nullable = false)
    private Integer duree; // en heures
    
    @Column(nullable = false)
    private Integer nombreInscrits;
    
    @Column(nullable = false)
    private Double score;
    
    @Column(nullable = false)
    private Long keynoteId;
    
    @OneToMany(mappedBy = "conference", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
