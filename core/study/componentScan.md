# 컴포넌트 스캔과 의존관계 자동 주입

- 지금까지는 @Bean 을 직접 등록했었다
- 하지만 이러한 @Bean 이 수백 수천개라면? 중복이 많이 발생 -> 의존 관계를 자동 주입하는 기능 사용
- @Bean 으로 등록이 필요한 클래스에는 @Component 어노테이션을 추가 
- @Bean 이 자동 등록 되어 의존성을 주입 할 수 없으므로 의존성 주입 부분에 @Autowired 어노테이션으로 주입도 자동으로 등록한다

```java
package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// 자동 등록을 위한 어노테이션
@Component
public class OrderServiceImpl implements OrderService {    
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    // 자동 주입을 위한 어노테이션
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
```

## @Component 컴포넌트 스캔

- @Component 가 붙은 모든 클래스를 스프링 빈으로 등록
- 클래스의 앞글자만 소문자로 변경하여 빈 이름으로 사용
- 직접 지정을 원하는 경우 @Component("원하는이름") 으로 설정 가능

## @Autowired 의존관계 자동 주입

- @Autowired 를 지정하면 스프링 컨테이너가 자동으로 해당 스프링 빈을 찾아서 주입
- getBean 과 동일하게 작용한다고 생각하면 된다
- 생성자가 많아도 다 자동으로 주입해 준다


