package app.service;

import java.time.LocalDate;
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

    public Optional<Member> findById(Integer memberId) {
        return memberRepository.findById(memberId);
    }

    public Member save(Member member) {
        if (member.getRegistrationDate() == null) {
            member.setRegistrationDate(LocalDate.now());
        }
        return memberRepository.save(member);
    }


    public void deleteById(Integer id) {
        memberRepository.deleteById(id);
    }

//    public Optional<Member> findByEmail(String email) {
//        return Optional.ofNullable(memberRepository.findByEmail(email));
//    }

    public List<Member> searchMembers(String keyword, boolean includeRetired) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return includeRetired
                ? memberRepository.findAll()
                : memberRepository.findAllActive();
        } else {
            return includeRetired
                ? memberRepository.searchByKeyword(keyword)
                : memberRepository.searchByKeywordAndNotRetired(keyword);
        }
    }

    public List<Member> findAllActive() {
        	return memberRepository.findAllActive();
        }

    }
