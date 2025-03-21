package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;

import vttp.batch5.csf.assessment.server.model.Menu;
import vttp.batch5.csf.assessment.server.model.Order;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;
  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
  //  db.getCollection("menus").find({})
  //
  public List<Menu> getMenu(){
    Query query = new Query();
    query.with(Sort.by(Sort.Direction.ASC,"name"));
    return mongoTemplate.find(query, Menu.class,"menus");
  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  //
  //  db.getCollection("orders").insert({...})
  public void insertOrder(Order order){
    mongoTemplate.insert(order,"orders");
  }
}
