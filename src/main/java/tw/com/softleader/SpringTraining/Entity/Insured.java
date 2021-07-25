package tw.com.softleader.SpringTraining.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
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
}
