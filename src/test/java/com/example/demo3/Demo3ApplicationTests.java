package com.example.demo3;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Demo3ApplicationTests {
    @Autowired
    private RedisTemplate<String, User> redisTemplate1;

    @Autowired
    private RedisTemplate<String, String> redisTemplate2;

    @Test
    public void testString() {
        redisTemplate2.opsForValue().set("test", "test1");
        System.out.println("存储到redis中的内容为：" + redisTemplate2.opsForValue().get("test"));
    }

    /**
     * 存储对象，对象需要序列化
     */
    @Test
    public void testObject() {
        User user = new User(1, "onism", "1234", "Man", "老王");
        ValueOperations<String, User> valueOperations = redisTemplate1.opsForValue();
        valueOperations.set("onism", user);
        System.out.println("存储到redis中的内容为：" + valueOperations.get("onism"));
    }

    /**
     * 测试hash
     */
    @Test
    public void testHash() {
        //第一个参数为key，第二个参数为属性，第三个参数为属性值
        HashOperations<String, Object, Object> hashOperations = redisTemplate2.opsForHash();
        hashOperations.put("onism", "sex", "man");
        String value = (String) hashOperations.get("onism", "sex");
        System.out.println("读取到redis中的内容为 :" + value);
    }

    /**
     * 测试List
     */
    @Test
    public void testList() {
        ListOperations<String, String> listOperations = redisTemplate2.opsForList();
        //从左侧插入
        listOperations.leftPush("list", "www");
        listOperations.leftPush("list", "onism");
        listOperations.leftPush("list", "me");
        //弹出左侧第一个值
        String value = (String) listOperations.leftPop("list");
        System.out.println("读取到redis中的内容为 :" + value);

        //遍历list
        //第一个参数为key、第二个参数为起始范围、第二个参数为结束范围
        List<String> list = listOperations.range("list", 0, 2);
        System.out.println("读取到list中的内容为 :" + list.toString());
    }

    /**
     * 测试set
     */
    @Test
    public void testSet() {
        //set集合有自动去重的功能，无法自动排序
        SetOperations<String, String> setOperations = redisTemplate2.opsForSet();
        setOperations.add("set", "www");
        setOperations.add("set", "www");
        setOperations.add("set", "onism");
        setOperations.add("set", "onism");
        setOperations.add("set", "top");
        Set<String> set = setOperations.members("set");
        System.out.println("读取到set中的内容为 :" + set.toString());
    }

    /**
     * 测试zset，在set的基础上增加了自动排序的功能
     */
    @Test
    public void testZset() {
        ZSetOperations<String, String> zset = redisTemplate2.opsForZSet();
        //第一个参数key、第二个参数为值，第三个参数权重值，就是根据这个权重值进行排序
        zset.add("zset", "http", 1);
        zset.add("zset", "www", 3);
        zset.add("zset", "onism", 4);
        zset.add("zset", "top", 6);

        Set<String> zsets = zset.range("zset", 0, 3);
        System.out.println("读取到zsets中的内容为 :" + zsets.toString());

        //获取指定权重值范围之类的元素集合
        Set<String> zsetB = zset.rangeByScore("zset", 0, 3);
        System.out.println("读取到zsets中的内容为 :" + zsetB.toString());
    }

}
