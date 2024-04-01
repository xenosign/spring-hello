# 스프링으로 전환

## 스프링 구조

- ApplicationContext 를 스프링 컨테이너라 한다
- 스프링 컨테이너는 @Configuration 이 붙은 클래스를 구성 정보로 사용
- @Bean 이 붙어서 반횐된 인스턴스를 컨테이너에 전부 등록
- @Bean 이 붙어서 등록된 인스턴스는 applicationContext.getBean("메서드명", 반환할 클래스) 로 찾는다
- @Bean(name = "ddd") 으로 이름 변경 가능

## 스프링 컨테이너와 스프링 빈

### 스프링 컨테이너

- ApplicationContext 를 스프링 컨테이너라고 하면 인터페이스이다
- @Bean 의 이름은 서로 달라야만 한다. 같을 경우 덮어쓰기 문제가 발생한다

### 빈 조회 기본

- ac.getBean("이름", 타입)
- ac.getBean(타입)
- 조회 대상이 없으면 예외 발생

### 빈 조회 - 동일한 타입이 둘 이상

- 타입으로 조회시 동일 타입의 빈이 2개 이상이면 오류 발생
- ac.getBeansOfType() 을 사용하면 해당 타입의 모든 빈 조회 가능

## 스프링 빈 조회

- 대원칙 : 부모 타입으로 조회하면 자식 타입도 전부 조회된다
- 모든 자바 객체의 Object 타입으로 조회하면 모든 스프링 빈을 조회

## BeanFactory 와 ApplicationContext

### BeanFactory

- 스프링 컨테이너의 최상위 인터페이스, 스프링 빈을 관리 조회
- 지금까지 우리가 사용한 대부분의 기능을 BeanFactory 에서 제공

### ApplicationContext

- 어플리케이션을 개발할 때 필요한 다양한 부가 기능을 제공
- 제공되는 편의 기능 
  - 메세지 소스를 통한 국제화 : 국가별 언어 변경하는 기능
  - 환경 변수 : 로컬, 개발, 운영 등을 구분해서 처리
  - 어플리케이션 이벤트 : 이벤트를 발행하고, 구독하는 모델 지원
  - 편리한 리소스 조회 : 내부, 외부 리소스를 편하게 조회 가능
- 결국 우리가 쓰는 것은 ApplicationContext 다

## 스프링 빈 설정 메타 정보 - BeanDefinition

- 스프링이 다양한 설정 형식을 지원하는 근간이 되는 개념
- 스프링은 결국 Bean 정보를 BeanDefinition 으로만 읽어 들인다. 대신 다양한 형태의 소스(XML, JAVA)를 BeanDefinition 로 만드는 기능을 제공한다
- 즉, 역할과 구현을 개념적으로 나누어서 스프링은 어떤 설정이 적용되는지는 관심이 없고 최종적으로 완성된 BeanDefinition 만 읽어서 처리하면 된다
- 결국 BeanDefinition 는 스프링의 설정 정보를 추상화하여 제공

