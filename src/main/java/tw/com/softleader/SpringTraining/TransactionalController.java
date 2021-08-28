package tw.com.softleader.SpringTraining;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.softleader.SpringTraining.DAO.PolicyRepository;
import tw.com.softleader.SpringTraining.Entity.Policy;

@RestController
@RequestMapping("/transTest")
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TransactionalController {

  final PolicyRepository policyRepository;

  @PostConstruct
  public void init(){
    Policy rhys = Policy.builder()
                      .id(1L)
                      .applicantIdno("Rhys")
                      .build();
    var a = policyRepository.save(rhys);
    //TODO : 儲存後，更新資料，有辦法同步更新遭料庫嗎 ?
    a.setApplicantLocalName("matt");

  }
}
