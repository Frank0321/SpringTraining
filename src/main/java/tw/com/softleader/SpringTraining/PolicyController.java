package tw.com.softleader.SpringTraining;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyRepository policyRepository;

    @GetMapping
    public List<Policy> query(Policy form){
        /**
         * 如果有保單號 & 申請人 -> 用這兩個去找
         * 如果有保單號 & 沒有申請人 -> 保單去找
         * 如果沒有有保單號 & 有申請人 -> 申請人去找
         * 找全部
         */
        if(form.getPolicyNo() != null && form.getApplicantLocalName() != null) {
            return policyRepository.findByPolicyNoAndApplicantLocalNameLike(form.getPolicyNo(), form.getApplicantLocalName()+"%");
        } else if (form.getPolicyNo() != null && form.getApplicantLocalName() == null) {
            return policyRepository.findByPolicyNo(form.getPolicyNo());
        } else if (form.getPolicyNo() == null && form.getApplicantLocalName() != null) {
            return policyRepository.findByApplicantLocalNameLike(form.getApplicantLocalName()+"%");
        } else {
            return policyRepository.findAll();
        }
    }
}
