package tw.com.softleader.SpringTraining.Entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue
    private Long id;

    private Long insuredId;

    private String code;

    private String itemLocalNames;

    private Integer amount;

    private Integer premium;

}
