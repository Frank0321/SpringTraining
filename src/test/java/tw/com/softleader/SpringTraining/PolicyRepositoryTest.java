package tw.com.softleader.SpringTraining;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Slf4j
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PolicyRepositoryTest {

    @Autowired
    private PolicyRepository policyRepository;

    @BeforeAll
    void initAll(){

        Policy policy = Policy.builder()
                .policyNo("9921ABC00001")
                .endstNo(0)
                .applicantIdno("A123456789")
                .applicantLocalName("王小名")
                .build();
        policyRepository.save(policy);
    }

    @Test
    void testFindByPolicyNoAndEndstNo(){
        Policy policy = policyRepository.findByPolicyNoAndEndstNo("9921ABC00001", 0);
        assertEquals("A123456789", policy.getApplicantIdno());
    }

    @Test
    void testFindByApplicantIdno() {
        List<Policy> policies = policyRepository.findByApplicantIdno("A123456789");
        for(Policy policy:policies) {
            assertEquals("A123456789", policy.getApplicantIdno());
        }
    }

    @Test
    void testFindByApplicantLocalNameLike() {
        List<Policy> policies = policyRepository.findByApplicantLocalNameLike("王%");
        for(Policy policy:policies) {
            assertEquals("王", policy.getApplicantLocalName().substring(0,1));
        }
    }
}
