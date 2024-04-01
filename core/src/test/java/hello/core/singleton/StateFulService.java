package hello.core.singleton;

public class StateFulService {
    // stateful 한 필드는 없앤다
    // private int price;

    public int order(String name, int price) {
        System.out.println("name = " + name);
        System.out.println("price = " + price);
        
        // 여기가 문제
        // this.price = price;
        
        // 바로 지역변수를 리턴해서 해결
        return price;
    }

    public int getPrice() {
        return 0;
    }
}

