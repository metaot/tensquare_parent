package test;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.BSON;
import org.bson.Document;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MongoTest {


    //查询所有数据
    @Test
    public void test1() {
        //0. 创建Mongodb的客户端
        //MongoClient mongoClient = new MongoClient("192.168.72.130",27017);
        MongoClient mongoClient = new MongoClient("192.168.72.130");

        //1. 选择数据库  use spitdb
        //使用数据库名,获取数据库
        MongoDatabase spitdb = mongoClient.getDatabase("spitdb");


        //db.spit.find()在数据库中使用集合,查询集合中的所有数据
        //2. 获取集合,使用集合名从数据库中获取集合
        MongoCollection<Document> spit = spitdb.getCollection("spit");

        //3. 使用集合查询所有数据
        FindIterable<Document> documents = spit.find();

        //4. 展示结果
        for (Document document : documents) {
            System.out.println("-------------------------");
            System.out.println("_id:" + document.getString("_id"));
            System.out.println("content:" + document.getString("content"));
            System.out.println("userid:" + document.getString("userid"));
            System.out.println("nickname:" + document.getString("nickname"));
            //System.out.println("visits:" + document.getInteger("visits"));
            System.out.println("visits:" + document.get("visits"));
        }
    }

    //条件查询 db.spit.find({"userid" : "1013"})
    @Test
    public void test2() {
        MongoClient mongoClient = new MongoClient("192.168.72.130", 27017);

        MongoDatabase spitdb = mongoClient.getDatabase("spitdb");

        MongoCollection<Document> spit = spitdb.getCollection("spit");

        //使用BSON封装{"userid" : "1013"}数据
        //BasicDBObject:操作的事BSON对象,使用的事Mongodb-driver的实现
        BasicDBObject basicDBObject = new BasicDBObject("userid", "1013");

        //db.spit.find({"userid" : "1013"})
        FindIterable<Document> documents = spit.find(basicDBObject);

        for (Document document : documents) {
            System.out.println("-------------------------");
            System.out.println("_id:" + document.getString("_id"));
            System.out.println("content:" + document.getString("content"));
            System.out.println("userid:" + document.getString("userid"));
            System.out.println("nickname:" + document.getString("nickname"));
            //System.out.println("visits:" + document.getInteger("visits"));
            System.out.println("visits:" + document.get("visits"));
        }
    }


    //插入数据
    @Test
    public void test3() {
        MongoClient mongoClient = new MongoClient("192.168.72.130");

        MongoDatabase spitdb = mongoClient.getDatabase("spitdb");

        MongoCollection<Document> spit = spitdb.getCollection("spit");

        //创建封装数据的map容器
        Map<String, Object> map = new HashMap<String, Object>();
        //设置需要保存到Mongodb中的数据
        map.put("_id", "123");
        map.put("content", "高薪就业,迎娶白富美,走上人生巅峰");
        map.put("userid", "8888");
        map.put("nickname", "张三");
        map.put("visits", 66666);

        //封装保存数据的对象
        Document document = new Document(map);

        //执行保存
        spit.insertOne(document);
    }

}
