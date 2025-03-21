package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.model.Item;
import vttp.batch5.csf.assessment.server.model.Menu;
import vttp.batch5.csf.assessment.server.model.Order;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

  @Autowired
  private OrdersRepository ordersRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  // TODO: Task 2.2
  // You may change the method's signature
  public Object[] getMenu() {
    List<Menu> list = ordersRepository.getMenu();
    return list.toArray();
  }

  // TODO: Task 4
  public Map<String, String> postFoodOrder(String payload) {
    Map<String, String> response = new HashMap<>();
    JsonReader reader = Json.createReader(new StringReader(payload));
    JsonObject jObject = reader.readObject();
    String username = jObject.getString("username");
    String password = jObject.getString("password");
    if (!restaurantRepository.validUser(username, password)) {
      response.put("message", "Invalid username and/or password");
      response.put("status", "401");
      return response;
    }
    JsonArray itemsArray = jObject.getJsonArray("items");

    Order order = new Order();
    List<Item> list = new ArrayList<>();

    float totalpayment = 0;
    for (int i = 0; i < itemsArray.size(); i++) {
      JsonObject itemObject = itemsArray.getJsonObject(i);
      Item item = new Item();
      item.setId(itemObject.getString("id"));
      float price = itemObject.getJsonNumber("price").bigDecimalValue().floatValue();
      item.setPrice(price);
      item.setQuantity(itemObject.getInt("quantity"));
      totalpayment += price;
      list.add(item);
    }

    JsonObject payment = Json.createObjectBuilder()
        .add("payer", username)
        .add("order_id", UUID.randomUUID().toString().substring(0, 7))
        .add("payee", "Marcus Lim Rui Po")
        .add("payment", totalpayment)
        .build();

    String paymentReceipt = makePayment(username, payment);
    if (paymentReceipt.startsWith("400")) {
      response.put("message", paymentReceipt);
      response.put("status", "500");
      return response;
    }
    JsonReader paymentReader = Json.createReader(new StringReader(paymentReceipt));
    JsonObject paymentReceiptObj = paymentReader.readObject();

    order.setOrder_id(paymentReceiptObj.getString("order_id"));
    order.setPayment_id(paymentReceiptObj.getString("payment_id"));
    order.setUsername(username);
    order.setTotal(totalpayment);
    order.setTimestamp(new Date(TimeUnit.MILLISECONDS.toMillis(paymentReceiptObj.getJsonNumber("timestamp").longValue())));
    order.setItems(list);
    ordersRepository.insertOrder(order);
    String pattern = "yyyy-MM-dd";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    String date = simpleDateFormat.format(order.getTimestamp());
    logger.info(order.getTimestamp().toString());
    logger.info(date);
    restaurantRepository.insertData(order.getOrder_id(), order.getPayment_id(), date, order.getTotal(),
        order.getUsername());

    response.put("message", "{ \"order_id\": \"" + order.getOrder_id() + "\",\"payment_id\": \"" + order.getPayment_id()
        + "\",\"total\": \"" + order.getTotal() + "\",\"timestamp\": \"" + order.getTimestamp() + "\" }");
    response.put("status", "200");
    return response;
  }

  public String makePayment(String username, JsonObject json) {
    String URL = "https://payment-service-production-a75a.up.railway.app/api/payment";
    try {
      RestTemplate restTemplate = new RestTemplate();

      RequestEntity<String> request = RequestEntity
          .post(URI.create(URL))
          .contentType(MediaType.APPLICATION_JSON)
          .header("X-Authenticate", username)
          .body(json.toString());

      ResponseEntity<String> response = restTemplate.exchange(request, String.class);
      return response.getBody();
    } catch (Exception ex) {
      return ex.getMessage();
    }
  }

}
