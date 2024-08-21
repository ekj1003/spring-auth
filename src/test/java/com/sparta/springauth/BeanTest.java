package com.sparta.springauth;

import com.sparta.springauth.food.Food;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class BeanTest {
    // @Autowired
    // Food food;
    // 문제: Food 타입으로 Bean 객체를 2개 등록했기 때문에 자동 주입할 수 없다.
    // 해결방안: 골라줘야함

//    // 해결 방안 1) Bean의 이름을 명시해주기
//    // Chicken, Pizza 클래스이지만 Bean으로 등록하면 chicken, pizza로 등록되므로
//    // chicken, pizza가 Bean의 이름이다.
//    @Autowired
//    Food pizza;
//
//    @Autowired
//    Food chicken;
//
//    @Test
//    @DisplayName("테스트")
//    void test1() {
//        pizza.eat();
//        chicken.eat();
//    }

//    // 해결 방안 2) @Primary
//    // Chicken or Pizza에 @Primary를 붙여준다.
//    // Ex) Chicken에 @Primary를 붙여주어 자동으로 Chicken을 선택한다.
//    @Autowired
//    Food food;
//
//    @Test
//    @DisplayName("테스트")
//    void test1() {
//        food.eat();
//    }

    // 해결 방안 3) @Qualifier("Bean 이름")
    // Chicken or Pizza에 @Qualifier("Bean 이름")을 해준다.
    // Ex) Pizza에 @Qualifier("pizza")를 붙여주어 자동으로 Pizza를 선택한다.
    @Autowired
    @Qualifier("pizza")
    Food food;

    @Test
    @DisplayName("Primary와 Qualifier 중 누가 더 우선순위가 있을까?")
    void test1() {
        food.eat(); // Qualifier가 우선순위가 높다.
    }

    // 좁은 범위가 최우선 순위 => @Qualifier("Bean 이름")
    // 넓은 범위가 나중 순위 => @Primary

}
