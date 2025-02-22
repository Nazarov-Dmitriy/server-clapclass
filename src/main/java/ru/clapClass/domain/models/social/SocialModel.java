package ru.clapClass.domain.models.social;
import jakarta.persistence.*;
import lombok.*;

@ToString
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SOCIAL")
public class SocialModel {
    @Id
    @Column(unique=true)
    private String name;

    @Column()
    private String link;
}
