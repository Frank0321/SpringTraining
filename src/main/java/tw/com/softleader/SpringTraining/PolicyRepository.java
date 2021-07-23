package tw.com.softleader.SpringTraining;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PolicyRepository extends JpaRepository<Policy, Long> , JpaSpecificationExecutor<Policy> {

    Policy findByPolicyNoAndEndstNo(String policyNo, int endstNo);

    List<Policy> findByApplicantIdno(String applicantIdno);

    List<Policy> findByApplicantLocalNameLike(String name);

    List<Policy> findByPolicyNoAndApplicantLocalNameLike(String policyNo, String s);

    List<Policy> findByPolicyNo(String policyNo);
}
