package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // メールで検索などのカスタムメソッドは必要に応じて追加
    Member findByEmail(String email);
}
