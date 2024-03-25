package P3;

import java.io.IOException;
import java.util.*;

public class FriendshipGraph {
    private HashMap<Person,List<Person>> relation=new HashMap<Person, List<Person>>();

    public Set<Person> getAllVertex(){
        return this.relation.keySet();
    }
    /**
     * 向关系图中加入新人，检查是否有重名的人
     * @param person Person类型，待添加人
     * @throws IllegalArgumentException 已有同名的人，应终止程序运行
     */
    public void addVertex(Person person){
        if(this.relation.containsKey(person)) {
            throw new IllegalArgumentException("关系图中已经有"+person.getName()+"!");
        }
        relation.put(person,new ArrayList<Person>());
    }

    /**
     * 向关系图中加入一条单向边
     * @param start 有向边的弧尾（出发点）
     * @param end 有向边的弧头（到达点）
     * @throws IllegalArgumentException 所写关系已经写过，并存在了关系图中
     */
    public void addEdge(Person start,Person end){
        if(!this.relation.containsKey(start)) throw new IllegalArgumentException("该起点不存在！");
        if(!this.relation.containsKey(end)) throw new IllegalArgumentException("该终点不存在！");
        if(start==end) throw new IllegalArgumentException("起点和终点相同！");
        List<Person> list = relation.get(start);
        if(list.contains(end)) throw new IllegalArgumentException("已有该关系！");
        list.add(end);
    }

    /**
     * 找到从起点到终点在关系网中最近的距离
     * 使用BFS算法
     * start和end不能是同一个人
     * @param start 关系起点的人
     * @param end 关系终点的人
     * @return 如果两者之间存在联系，返回联系的最短距离，否则返回-1
     * @throws IllegalArgumentException 起点或终点不在图中
     */
    public int getDistance(Person start,Person end){
        //这三个直接退出的条件顺序不能乱，否则就会出错
        if(!this.relation.containsKey(start)||!this.relation.containsKey(end))
            throw new IllegalArgumentException("有一个人不在关系网中！");
        if(start==end) return 0;
        if(this.relation.get(start).isEmpty()||this.relation.get(end).isEmpty()) return -1;
        Queue<Person> queue=new LinkedList<>();
        Map<Person,Integer> flag=new HashMap<>();
        flag.put(start,0);
        queue.add(start);
        while(!queue.isEmpty()){
            Person nowPerson= queue.remove();
            int dis=flag.get(nowPerson)+1;
            for(Person person : this.relation.get(nowPerson)){
                if(person==end) return dis;
                if(!flag.containsKey(person)){
                    queue.add(person);
                    flag.put(person, dis);
                }
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        FriendshipGraph graph = new FriendshipGraph();
        Person rachel = new Person("Rachel");
        Person ross = new Person("Ross");
        Person ben = new Person("Ben");
        Person kramer = new Person("Kramer");
        graph.addVertex(rachel);
        graph.addVertex(ross);
        graph.addVertex(ben);
        graph.addVertex(kramer);
        graph.addEdge(rachel, ross);
        graph.addEdge(ross, rachel);
        graph.addEdge(ross, ben);
        graph.addEdge(ben, ross);
        System.out.println(graph.getDistance(rachel, ross));//should print 1
        System.out.println(graph.getDistance(rachel, ben));//should print 2
        System.out.println(graph.getDistance(rachel, rachel));//should print 0
        System.out.println(graph.getDistance(rachel, kramer));//should print -1
    }
}