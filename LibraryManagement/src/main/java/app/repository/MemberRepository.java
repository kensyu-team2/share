package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
	boolean existsByEmail(String email);  // メールの重複チェック用（任意）
}