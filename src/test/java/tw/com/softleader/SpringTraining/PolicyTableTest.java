package tw.com.softleader.SpringTraining;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import tw.com.softleader.SpringTraining.Entity.Insured;
import tw.com.softleader.SpringTraining.Entity.Policy;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PolicyTableTest {

    @Autowired
    PolicyRepository policyRepository;

    @BeforeAll
    void initAll(){
        Policy policy = Policy.builder()
                .policyNo("9921ABC00001")
                .endstNo(0)
                .applicantIdno("A123456789")
                .applicantLocalName("先生")
                .insured(Insured.builder()
                        .insuredIndo("A176280531")
                        .insuredLocalName("王哥哥").build())
                .insured(Insured.builder()
                        .insuredIndo("A176280577")
                        .insuredLocalName("王弟弟").build())
                .build();
        policyRepository.save(policy);

        Policy policy1 = Policy.builder()
                .policyNo("9921ABC00002")
                .endstNo(0)
                .applicantIdno("A111222333")
                .applicantLocalName("王叔叔")
                .insured(Insured.builder()
                        .insuredIndo("A111222333")
                        .insuredLocalName("王叔叔")
                        .build())
                .insured(Insured.builder()
                        .insuredIndo("A222333555")
                        .insuredLocalName("王姐姐")
                        .build())
                .build();
        policyRepository.save(policy1);
    }

    @Test
    @Transactional
    void testFindByPolicyNoAndEndstNo() {
        Policy policy = policyRepository.findByPolicyNoAndEndstNo("9921ABC00001", 0);
        assertEquals("A123456789", policy.getApplicantIdno());

        log.info("Now we try to get insureds");
        Set<Insured> insureds = policy.getInsureds();
        for (Insured insured : insureds) {
            log.info("{}", insured);
        }

        //不會新增進去
//        policyRepository.save(Policy.builder()
//                .policyNo("test").build());
    }
}
