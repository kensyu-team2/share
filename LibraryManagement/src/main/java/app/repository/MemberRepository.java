package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Member;

@Repository
//例: MemberRepository
public interface MemberRepository extends JpaRepository<Member, Integer> {
 // ここにfindByIdは定義しなくてOK
}
