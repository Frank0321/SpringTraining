package tw.com.softleader.SpringTraining;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "POLICY")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyNo;

    private int endstNo;

    private String applicantLocalName;

    private String applicantIdno;
}
