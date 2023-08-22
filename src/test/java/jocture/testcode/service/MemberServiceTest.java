package jocture.testcode.service;

import jocture.testcode.domain.Member;
import jocture.testcode.exception.DuplicateEmailMemberException;
import jocture.testcode.exception.NoExistsMemberException;
import jocture.testcode.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    void join() {
        //given
        Member member = new Member("hgkim", "hgkim@naver.com");
        //when
        memberService.join(member);
//        ArgumentCaptor<Member> captor = ArgumentCaptor.forClass(Member.class);

        //then
//        verify(memberRepository , times(1)).save(captor.capture());
//        assertEquals("hgkim" , captor.getValue().getName());
        then(memberRepository).should().save(member);
    }

    @Test
    @DisplayName("join 시 중복 이메일 에러")
    void join_duplicateEmail() {
        //given
        Member member1 = new Member("hgkim", "hgkim@naver.com");
        Member member2 = new Member("kim", "hgkim@naver.com");
        given(memberRepository.findByEmail(any()))
                .willReturn(Optional.of(member1));
        //when
        ThrowableAssert.ThrowingCallable callable =
                () -> memberService.join(member2);
        //then
        Assertions.assertThatThrownBy(callable)
                .isInstanceOf(DuplicateEmailMemberException.class);
        then(memberRepository).should(never()).save(member2);
    }

    @Test
    void getMember() {
        //given
        int memberId = 1;
        Member member = new Member("hgkim", "hgkim@naver.com");
        given(memberRepository.findById(anyInt()))
                .willReturn(Optional.of(member));
        //when
        Member serviceMember = memberService.getMember(memberId);

        //then
        assertThat(serviceMember.getName()).isEqualTo("hgkim");
        assertThat(serviceMember.getEmail()).isEqualTo("hgkim@naver.com");

        then(memberRepository).should().findById(anyInt());
    }

    @Test
    @DisplayName("getMember 시 존재하지 않는 회원 테스트 케이스")
    void getMember_noExists() {
        //given
        int noExistsMemberId = -99;

//        given(memberRepository.findById(anyInt()))
//                .willReturn(Optional.empty());

        //when
        ThrowableAssert.ThrowingCallable callable =
                () -> memberService.getMember(noExistsMemberId);

        //then
        assertThatThrownBy(callable)
                .isInstanceOf(NoExistsMemberException.class);
    }
}