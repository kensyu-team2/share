package app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import app.entity.Member;
import app.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Integer id) {
        return memberRepository.findById(id);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void deleteById(Integer id) {
        memberRepository.deleteById(id);
    }

    public Optional<Object> findByEmail(String mail) {
        return Optional.ofNullable(memberRepository.findByMail(mail));
    }
}
