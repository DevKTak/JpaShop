package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given 이런게 주어졌을 때
        Member member = new Member();
        member.setName("kim");

        //when 이걸 실행하면
        Long saveId = memberService.join(member);

        //then 이렇게 되어야 한다.
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given 이런게 주어졌을 때
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when 이걸 실행하면
        memberService.join(member1);
        memberService.join(member2);

        //then 이렇게 되어야 한다.
        fail("예외가 발생해야 한다."); // 여기는 오면 안되는 것일때 fail()
    }

}