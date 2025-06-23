package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import app.entity.Lending;
import app.entity.Member;

public interface LendingRepository extends JpaRepository<Lending, Integer>, JpaSpecificationExecutor<Lending> { // ★ LongからIntegerに修正
    long countByMemberAndReturnDateIsNull(Member member);
    List<Lending> findByMemberAndReturnDateIsNull(Member member);
}