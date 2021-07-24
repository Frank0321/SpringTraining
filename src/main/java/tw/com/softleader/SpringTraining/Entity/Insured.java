package tw.com.softleader.SpringTraining.Entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "INSURED")
public class Insured {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String insuredIndo;

    private String insuredLocalName;
}
