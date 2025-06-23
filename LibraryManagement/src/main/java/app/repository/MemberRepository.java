package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import app.entity.Member;

/**
 * 会員情報(membersテーブル)の永続化を担うリポジトリ
 */
public interface MemberRepository extends JpaRepository<Member, Integer>, JpaSpecificationExecutor<Member> {

	Object findByMail(String mail);
    // JpaRepositoryの基本的なメソッド(findById, saveなど)で要件を満たせるため、
    // 現時点ではカスタムメソッドの追加は不要です。
    // 必要に応じて、メールアドレスや電話番号での検索メソッドなどを追加します。
}