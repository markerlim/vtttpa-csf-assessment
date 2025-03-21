package vttp.batch5.csf.assessment.server.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

  @Autowired
  private RestaurantService restaurantService;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object[]> getMenus() {
    return ResponseEntity.ok(restaurantService.getMenu());
  }

  // TODO: Task 4
  // Do not change the method's signature
  @PostMapping(path = "/food_order")
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {
    Map<String,String> result = restaurantService.postFoodOrder(payload);
    String status = result.get("status");
    logger.info(result.get("message"));
    if(status.equals("401")){
      logger.info("401 activated");
      return ResponseEntity.status(401).body(result.get("message"));
    }
    if(status.equals("500")){
      logger.info("500 activated");
      return ResponseEntity.status(500).body(result.get("message"));
    }
    return ResponseEntity.ok(result.get("message"));
  }
}
