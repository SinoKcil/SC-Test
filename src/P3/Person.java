package P3;

import java.util.ArrayList;
import java.util.List;

public final class Person {
    static List<String> allPerson=new ArrayList<>();//题目中提到不要使用static field，但是在不更改main代码的前提下只能加上这个属性
    private final String name;

    public Person(String name) throws IllegalArgumentException {
        if(name.trim().length()==0) throw new IllegalArgumentException("输入的名字是空！");
        if(allPerson.contains(name)) throw new IllegalArgumentException("已创建同名的人！");
        this.name = name;
        allPerson.add(name);
    }
    public String getName(){
        return this.name;
    }
}

