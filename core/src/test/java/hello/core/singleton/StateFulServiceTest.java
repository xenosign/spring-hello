package hello.core.singleton;

import hello.core.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StateFulServiceTest {
    @Test
    void statefulServiceSingleTon() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StateFulService statefulService1 = ac.getBean(StateFulService.class);
        StateFulService statefulService2 = ac.getBean(StateFulService.class);

        // 쓰레드 A: 10000원 주문
        int userAPrice = statefulService1.order("userA", 10000);
        // 쓰레드 B: 20000원 주문
        int userBPrice = statefulService2.order("userB", 20000);

        // 쓰레드 A: 사용자A 주문 금액 조회
        // int price = statefulService1.getPrice();
        // 기대 금액은 10000 이지만 사용자B 주문으로 인하여 20000 이 나온다
        System.out.println("price = " + userAPrice);
        System.out.println("price = " + userBPrice);

        Assertions.assertThat(userAPrice).isEqualTo(10000);
    }

    static class TestConfig {
        @Bean
        public StateFulService statefulService() {
            return new StateFulService();
        }
    }
}