//굉장히 편하지만 모든 Bean들이 올라가야하므로 느리다.
//스프링부트 테스트이자 통합테스트(API 테스트 , End-to-End (E2E) 테스트라고 부른다.
package jocture.testcode.service;

import jocture.testcode.domain.Member;
import jocture.testcode.exception.DuplicateEmailMemberException;
import jocture.testcode.exception.NoExistsMemberException;
import jocture.testcode.repository.MemberRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceSpringBootTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    //TC-Test Case
    //@Disabled
    @Test
    //@RepeatedTest(10)
    //@Commit
    //@Rollback
    void join() {
        //BDD 스타일 - Given/When/Then
        //given 사전에 준비되어야 하는 것들
        Member member = new Member("jjlim", "jjlim@ab.com");

        //when
        memberService.join(member);

        //then 정상적으로 작동하는 지 테스트하는 코드
        assertThat(member.getId()).isNotNull();
        assertThat(member.getId()).isPositive();

        System.out.println("name >> " + member.getName());
        System.out.println("id >> " + member.getId());
        System.out.println("email >> " + member.getEmail());

        Optional<Member> result = memberRepository.findById(member.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(member.getName());
        assertThat(result.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    @DisplayName("Duplicate Email Check를 위한 테스트 케이스")
    void join_duplicateEmail() {
        //given
        Member member1 = new Member("jjlim", "jjlim@ab.com");
        memberRepository.save(member1);
        Member member2 = new Member("jjlim22222", "jjlim@ab.com");

        //when
        ThrowableAssert.ThrowingCallable callable = () -> memberService.join(member2);

        //then
        assertThatThrownBy(callable).isInstanceOf(DuplicateEmailMemberException.class);
    }

    @Test
    void getMember() {
        //given
        Member member = new Member("hgkim", "khg2154@naver.com");
        memberRepository.save(member);

        //when
        Optional<Member> optionalMember = memberRepository.findById(member.getId());

        //then
        assertThat(optionalMember.get().getName()).isEqualTo(member.getName());
        assertThat(optionalMember.get().getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void getMember_noExists() {
        //given
        int noExistsMemberId = -99;
        //when
        ThrowableAssert.ThrowingCallable throwingCallable =
                () -> memberService.getMember(noExistsMemberId);
        //then
        assertThatThrownBy(throwingCallable).isInstanceOf(NoExistsMemberException.class);
    }
}