//package service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import form.MemberSearchForm;
//import repository.MemberRepository;
//
//@Service
//public class MemberService {
//    private final MemberRepository repo;
//    public MemberService(MemberRepository repo) { this.repo = repo; }
//
//    // ── 既存メソッド略 ──
//
//    /** 会員検索 */
//    public List<Member> search(MemberSearchForm f) {
//        // リポジトリに同名メソッドを定義しておくこと
//        return repo.findByNameContainingAndPhoneContaining(
//                blankToEmpty(f.getName()),
//                blankToEmpty(f.getPhone()));
//    }
//
//    private String blankToEmpty(String s) {
//        return (s == null ? "" : s);
//    }
//}