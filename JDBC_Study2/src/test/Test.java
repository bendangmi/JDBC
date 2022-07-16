package test;

/**
 * @code Description
 * @code author 本当迷
 * @code date 2022/7/16-6:24
 */

class A{
    {
        System.out.println("父类代码块");
        this.showInfo();
    }
    void find(){
        System.out.println("父类中的find方法！");
    }
    void showInfo(){
        System.out.println("父类中的showInfo方法！");
        System.out.println(this);
        this.find();
    }
}

class B extends A{
    {
        System.out.println("子类代码块");
    }
    void find(){
        System.out.println("子类中的find方法！");
    }
    void showInfo(){
        System.out.println("子类中的showInfo方法！");
        System.out.println(this);
        super.showInfo();
    }
    void test2(){
        System.out.println("父类没有声明！");
    }
}
public class Test {
    public static void main(String[] args) {
        final B b = new B();

    }
}
