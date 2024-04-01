package hello.core.binfind;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextBasicFindTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    @DisplayName("빈 이름으로 출력하기")
    void findBeanByName() {
        MemberService memberService = ac.getBean("memberService", MemberService.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService = " + memberService.getClass());

        // assertEquals(memberService instanceof MemberServiceImpl, true);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByName2() {
        // 구현체로 타입을 변경해서 조회도 가능
        // 다만 구현체에 의존하는 코드이 경우, MemberServiceImpl 의 구현이 변경되어 버리면 테스트 코드를 수정해야 하므로 안좋은 코드
        // 추상체에 의존하는 것이 좋다
        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService = " + memberService.getClass());

        // assertEquals(memberService instanceof MemberServiceImpl, true);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회, 실패 케이스")
    void findBeanByNameX() {
        // 화살표 함수로 실행 함수를 콜백 전달
        // 앞에는 예상되는 에러, 뒤에는 에러를 발생시키는 함수
        assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("xxxx", MemberService.class));
    }

    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService = ac.getBean(MemberService.class);
        System.out.println("memberService = " + memberService);
        System.out.println("memberService = " + memberService.getClass());


        // assertEquals(memberService instanceof MemberServiceImpl, true);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }


}
