package tw.com.softleader.SpringTraining;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import tw.com.softleader.SpringTraining.DAO.PolicyRepository;
import tw.com.softleader.SpringTraining.Entity.Policy;

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
        log.info("start beforeAll annotation");
        Policy policy = Policy.builder()
                .policyNo("9921ABC00001")
                .endstNo(0)
                .applicantIdno("A123456789")
                .applicantLocalName("王小名")
                .build();
        policyRepository.save(policy);

        Policy policy1 = Policy.builder()
                .policyNo("9921ABC00002")
                .endstNo(1)
                .applicantIdno("A123456788")
                .applicantLocalName("王小明")
                .build();
        policyRepository.save(policy1);

        Policy policy2 = Policy.builder()
                .policyNo("9921ABC00003")
                .endstNo(2)
                .applicantIdno("A123456787")
                .applicantLocalName("王小銘")
                .build();
        policyRepository.save(policy2);
    }

    @BeforeEach
    void beforeEachTest(){
        log.info("start beforeEach annotation");
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

    @Test
    void testPageable(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("policyNo").descending());
        Page<Policy> page = policyRepository.findAll(pageable);
        log.debug("print page info : {}", page);
        List<Policy> policies = page.getContent();
        policies.stream().forEach(e -> log.debug("print each policy {}", e));
    }


}
