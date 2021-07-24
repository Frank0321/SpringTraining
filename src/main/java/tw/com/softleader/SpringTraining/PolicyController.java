package tw.com.softleader.SpringTraining;

import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tw.com.softleader.SpringTraining.Entity.Policy;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyRepository policyRepository;

    @PostConstruct
    void init(){
        Policy policy1 = Policy.builder().policyNo("AAA").applicantLocalName("aaa").endstNo(0).build();
        policyRepository.save(policy1);

        Policy policy2 = Policy.builder().policyNo("AAA").applicantLocalName("bbb").build();
        policyRepository.save(policy2);

        Policy policy3 = Policy.builder().policyNo("BBB").applicantLocalName("aaacc").build();
        policyRepository.save(policy3);

        Policy policy4 = Policy.builder().policyNo("CCC").applicantLocalName("ccc").build();
        policyRepository.save(policy4);

        Policy policy5 = Policy.builder().policyNo("AAA").applicantLocalName("aaa").endstNo(1).build();
        policyRepository.save(policy5);
    }

    @GetMapping("/test0")
    public List<Policy> queryOriginal(Policy form){
        return policyRepository.findByPolicyNoAndApplicantLocalNameLike(form.getPolicyNo(), form.getApplicantLocalName()+"%");
    }

    @GetMapping("/test1")
    public List<Policy> query(Policy form){
        /**
         * 如果有保單號 & 申請人 -> 用這兩個去找  ->1
         * 如果有保單號 & 沒有申請人 -> 保單去找  -> 1,2
         * 如果沒有有保單號 & 有申請人 -> 申請人去找  ->1,3
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

    @GetMapping("/test2")
    public List<Policy> query(@And({
            @Spec(path = "policyNo", spec = Equal.class),
            @Spec(path = "applicantLocalName", spec = Like.class)
//            ,@Spec(path = "endstNo", spec = Equal.class)
    })Specification<Policy> specification){
        return policyRepository.findAll(specification);
    }

    /**
     * http://localhost:8081/policy/test1?policyNo=AAA&applicantLocalName=aaa
     * http://localhost:8081/policy/test1?policyNo=AAA
     * http://localhost:8081/policy/test1?applicantLocalName=aaa
     * http://localhost:8081/policy/test1
     * 把 test1 改成 test2
     */

    @GetMapping("/test3")
    public List<Policy> maxQuery(@And({
            @Spec(path = "policyNo", spec = Equal.class),
            @Spec(path = "applicantLocalName", spec = Like.class)
    })Specification<Policy> specification) {
        Specification<Policy> newSpec = (Specification<Policy>) (root, criteriaQuery, criteriaBuilder) -> {
            Subquery<Long> subQuery = criteriaQuery.subquery(Long.class);
            Root<Policy> subQueryRoot = subQuery.from(Policy.class);
            //查詢 ensarNo 最大值
            subQuery.select(criteriaBuilder.max(subQueryRoot.get("endstNo")));
            // where 條件
            subQuery.where(criteriaBuilder.equal(root.get("policyNo"), subQueryRoot.get("policyNo")));
            return criteriaBuilder.equal(root.get("endstNo"), subQuery);
        };

        return policyRepository.findAll(Specification.where(specification).and(newSpec));

    }
}
