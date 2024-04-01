# 웹 어플리케이션과 싱글톤

- 스프링은 태생이 기업용 온라인 서비스 기술을 지원하기 위해 탄생
- 웹 어플리케이션은 보통 여러 고객이 동시에 요청을 한다
- 스프링 없는 순수한 DI 컨테이너는 AppConfig 요청을 할 때마다 매번 인스턴스를 새로 생성
- 따라서, 메모리 낭비가 심하다
- 해당 인스턴스를 딱 1개만 생성하고 공유하여 문제를 해결

## 싱글톤 패턴

- 클래스의 인스턴스가 딱 1개만 생성되는 것을 봥하는 패턴
- 생성자를 private 로 하여 외부에서 임의로 new 를 못쓰도록 막는 법을 적용

```java
public class SingletonService {
    private static final SingletonService instance = new SingletonService();

    // static 영역에 1개 생성해 놓고, 필요하면 해당 인스턴스의 참조값을 찾아서 전달하여 공유
    public static SingletonService getInstance() {
        return instance;
    }
    
    // 생성자를 private 로 선언하여 새롭게 생성 자체를 막기, 이를 통해 메모리 낭비를 막는다
    private SingletonService() {};

    public void logic() {
        System.out.println("SingletonService.logic");
    }
}
```

- 단, 스프링의 경우 알아서 인스턴스를 싱글톤 패턴으로 생성해 준다

## 싱글톤 패턴의 문제점

- 싱글톤 패턴을 구현하는 코드가 길어진다
- 의존 관계상 클라이언트가 구현체 클래스에 의존해야 한다 -> DIP 를 위반한다
- 클라이언트가 구현체 클래스에 의존하므로 OCP 도 위반한다
- 테스트하기도 어렵다
- 내부 속성을 변경하거나 초기화하기 어렵다
- private 생성자가 강제 되므로 사직 클래스 만들기 어렵다
- 결론적으로 유연성이 떨어지기 때문에, 안티 패턴으로 불리기도 한다

## 싱글톤 컨테이너 with 스프링

- 스프링 컨테이너는 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤으로 관리한다
- 지금까지 우리가 학습한 컨테이너가 싱글톤으로 운영되는 @Bean 이다
- 스프링을 쓰면 싱글톤 패턴을 위한 지저분한 코드가 필요하지 않는다
- DIP, OCP, private 의 제약사항에서 벗어나 자유롭게 싱글톤을 사용할 수 있다
- AppConfig 를 스프링 컨테이너로 불러서 사용하면 끝!

```java
public class SingletonTest {
    @Test
    @DisplayName("스프링 컨테이너와 싱글콘")
    void springContainter() {
        // 스프링 컨테이너 Bean 을 사용하여 테스트
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        Assertions.assertThat(memberService1).isSameAs(memberService2);
    }
}
```

## 싱글톤 방식의 주의점

- 싱글톤 개념이 적용된 방식은 클라이언트가 하나의 같은 인서턴스를 공유하기 때문에 상태값을 유지(stateful) 하도록 설계하면 안된다
- 무상태(stateless)로 설계를 해야만 한다
  - 특정 클라이언트에 의존적인 필드가 있으면 안된다
  - 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다
  - 가급적 읽기만 가능해야 한다
  - 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야 한다
- 스프링 빈에 필드 공유 값을 설정하면 큰 장애의 시발점이 된다

## @Configuration 과 싱글톤

- 실제로 AppConfig 에 보면 MemoryMemberRepository 가 2번 new 로 생성, 이러면 싱글톤이 깨지는 것이 아닌가?
- 하지만 스프링이 알아서 각각의 @Bean 의 객체를 한번만 생성되고 참조 되도록 관리를 해준다
- 단, 이때 static 이 사용되면 해당 Bean 은 바로 static 영역에 등록이 되어, 스프링의 관리에서 벗어나 싱글톤을 보장하지 못한다

## @Configuration 과 바이트코드 조작의 마법

- 자바 코드 상, AppConfig 의 MemberRepository 는 3번이 호출이 되어야 하는데 실제로는 1번만 호출
- AppConfig 를 @Configuration 으로 등록을 하면, 실제 AppConfig 인스턴스가 메모리에 올라가는 것이 아니다
- 실제로는 AppConfig@CGLIB 같은 형태로 CGLIB 이라는 바이트 조작 라이브러리를 사용하여 상속이 진행된 자식 클래스가 등록이 된다
- 해당 클래스는 @Configuration 이 동작하면
  - 해당 클래스가 스프링 컨테이너에 등록되어 있으면 해당 컨테이너의 참조를 찾아서 반환
  - 없으면 새롭게 인스턴스를 생성하고 스프링 컨테이너에 등록
- 위와 같은 과정을 통해서 스프링은 싱글톤 보장이 가능하다

### @Configuration 없이 @Bean 만 적용하면 어떻게 될까?

- 안붙여도 @Bean 등록은 문제 없지만, AppConfig 에 CGLIB 기술 적용이 안되어 결국 여러개의 인스턴스가 스프링 컨테이너에 등록되어 싱글톤 보장이 깨지게 된다





