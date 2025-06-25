package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import app.entity.Member;

@Repository
//例: MemberRepository
public interface MemberRepository extends JpaRepository<Member, Integer> {
 // ここにfindByIdは定義しなくてOK

	// 退会していない会員全件取得
	@Query("SELECT m FROM Member m WHERE m.retireDate IS NULL")
	List<Member> findAllActive();

	// キーワード検索（退会していない人のみ）
	@Query("SELECT m FROM Member m WHERE m.retireDate IS NULL AND " +
	       "(m.name LIKE %:keyword% OR CAST(m.memberId AS string) LIKE %:keyword%)")
	List<Member> searchByKeywordAndNotRetired(@Param("keyword") String keyword);

	// キーワード検索（全員対象）
	@Query("SELECT m FROM Member m WHERE " +
	       "(m.name LIKE %:keyword% OR CAST(m.memberId AS string) LIKE %:keyword%)")
	List<Member> searchByKeyword(@Param("keyword") String keyword);


}
