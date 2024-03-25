package P3;

import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.*;
/*
    test strategy:等价类划分
        addVertexTest:，划分为添加的点不在集合中和点在集合中
        addEdgeTest:划分为正常输入结果是否正确，弧的起点和终点相同，弧已经在图中，弧的其中一个顶点不在集合中
        getDistance：划分为起点和终点相同，起点和终点相邻，起点和终点只有一条长度大于1的路径，起点和终点不止一条路径，起点和终点之间没有路径
                    终点到任意点都没有正数路径，起点到任意点都没有正数路径，被孤立的点到自己
                    （被孤立的点可能到别人没有路径，但到自己的路径总是跟别人到自己的路径一样
 */
public class FriendshipGraphTest {//发现这里要加上public才会被识别为测试类
    @Test
    public void addVertexTest(){
        FriendshipGraph graph = new FriendshipGraph();

        Person zero=new Person("Zero");
        Person first=new Person("First");
        Person second=new Person("Second");
        Person third=new Person("Third");
        graph.addVertex(zero);

        assertThrows(IllegalArgumentException.class,()->graph.addVertex(zero));

        graph.addVertex(first);
        graph.addVertex(second);
        graph.addVertex(third);

        assertThrows(IllegalArgumentException.class,()->graph.addVertex(first));
    }
    @Test
    public void addEdgeTest(){
        FriendshipGraph graph = new FriendshipGraph();
        Person alice=new Person("Alice");//由于在person类中设置了static属性，此处设置不能采用上方的名字
        Person bob = new Person("Bob");
        Person cindy=new Person("Cindy");
        graph.addVertex(alice);

        //测试从自身指向自身的边
        assertThrows(IllegalArgumentException.class,()->graph.addEdge(alice,alice));
        //测试终点不在内部
        assertThrows(IllegalArgumentException.class,()->graph.addEdge(alice,bob));
        //测试起点不在内部
        assertThrows(IllegalArgumentException.class,()->graph.addEdge(bob,alice));

        graph.addVertex(bob);
        graph.addVertex(cindy);
        graph.addEdge(bob,alice);
        graph.addEdge(alice,bob);
        graph.addEdge(bob,cindy);
        graph.addEdge(cindy,bob);
        //测试已有边
        assertThrows(IllegalArgumentException.class,()->graph.addEdge(bob,cindy));
        assertThrows(IllegalArgumentException.class,()->graph.addEdge(alice,bob));

        //添加边的结果是否相同
        HashSet<Person> set=new HashSet<Person>();
        set.add(alice);
        set.add(bob);
        set.add(cindy);
        assertEquals(set,graph.getAllVertex());
    }
    @Test
    public void getDistance(){
        FriendshipGraph graph = new FriendshipGraph();
        Person zero = new Person("000");
        Person first = new Person("111");
        Person second = new Person("222");
        Person third = new Person("333");
        Person fourth = new Person("444");
        Person fifth = new Person("555");
        graph.addVertex(zero);
        graph.addVertex(first);
        graph.addVertex(second);
        graph.addVertex(third);
        graph.addVertex(fourth);
        graph.addVertex(fifth);

        graph.addEdge(zero,first);
        graph.addEdge(first,zero);

        //测试自己到自己和无路
        assertEquals(0,graph.getDistance(zero,zero));
        assertEquals(0,graph.getDistance(first,first));
        assertEquals(1,graph.getDistance(zero,first));
        assertEquals(-1,graph.getDistance(zero,second));

        graph.addEdge(first,second);
        graph.addEdge(second,first);

        assertEquals(2,graph.getDistance(zero,second));

        graph.addEdge(zero,second);
        graph.addEdge(second,zero);
        //测试不止一条路径
        assertEquals(1,graph.getDistance(zero,second));
        assertEquals(1,graph.getDistance(second,zero));
        //测试无路
        assertEquals(-1,graph.getDistance(first,third));
        assertEquals(-1,graph.getDistance(third,first));
        assertEquals(0,graph.getDistance(third,third));

        graph.addEdge(fourth,second);
        graph.addEdge(fourth,fifth);
        graph.addEdge(fifth,fourth);
        graph.addEdge(second,fifth);
        //测试有向图
        assertEquals(2,graph.getDistance(fourth,zero));
        assertEquals(3,graph.getDistance(zero,fourth));


    }
}