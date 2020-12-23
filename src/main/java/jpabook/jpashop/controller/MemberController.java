package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor // final로 된 필드만 자동으로 생성자 인젝션
public class MemberController {

    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        /*
         * MemberForm form <= 왜 Member Entity로 안하고 굳이 MemberForm 을 만들어서
         * 사용하는 이유는 서로 필드가 정확하게 똑같진 않고 엔티티에 @NotEmpty 같은것을 쓰게 되면
         * 엔티티가 점점 지저분해지게 되기 때문에 화면에 핏한 폼을 만들어서 사용을 권장한다.
         * */

        if (result.hasErrors()) {
            // 회원가입폼에서 이름 빼고 (도시, 거리, 우편번호)만 입력하고
            // 이 if 문을 타더라도 MemberForm form 으로 넘어온 데이터는
            // 그대로 다시 가져가기때문에 보이는 폼에 유지되어 있다.
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    @GetMapping("members")
    public String list(Model model) {
        // model.addAttribute("members", memberService.findMembers());
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
