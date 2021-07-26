package tw.com.softleader.SpringTraining.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@Table(name = "INSURED")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Insured {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String insuredIndo;

    private String insuredLocalName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "INSURED_ID")
    private Set<Item> items;
}
