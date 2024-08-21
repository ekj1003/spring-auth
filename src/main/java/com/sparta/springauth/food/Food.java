package com.sparta.springauth.food;

public interface Food {
    // 1. 같은 타입의 Bean은 등록될 수 있다.
    // Food 인터페이스를 상속받은 Chicken과 Pizza는 @Component로 서로 같은 타입의 Bean으로 등록되었다.
    //
    void eat();
}