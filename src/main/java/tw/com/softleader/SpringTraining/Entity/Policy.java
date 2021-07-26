package tw.com.softleader.SpringTraining.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "POLICY")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
//@NamedEntityGraph(name = "policy.insureds", attributeNodes = @NamedAttributeNode("insureds"))
@NamedEntityGraph(
        name = "policy.insureds",
        attributeNodes = @NamedAttributeNode(value= "insureds",subgraph = "policy.insureds.items"),
        subgraphs= @NamedSubgraph(name = "policy.insureds.items", attributeNodes = @NamedAttributeNode( value= "items"))
)
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String policyNo;

    private int endstNo;

    private String applicantLocalName;

    private String applicantIdno;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "POLICY_ID")    //刪除則會在建立時，自動產生一個 policy_insured 的 table
    @Singular
    private Set<Insured> insureds;
}
