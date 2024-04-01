package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
