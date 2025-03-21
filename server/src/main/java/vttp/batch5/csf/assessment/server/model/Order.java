package vttp.batch5.csf.assessment.server.model;

import java.util.Date;
import java.util.List;

public class Order {
    private String _id;
    private String order_id;
    private String payment_id;
    private String username;
    private float total;
    private Date timestamp;
    private List<Item> items;

    public Order(){
        
    }
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getOrder_id() {
        return order_id;
    }
    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }
    public String getPayment_id() {
        return payment_id;
    }
    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public float getTotal() {
        return total;
    }
    public void setTotal(float total) {
        this.total = total;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    @Override
    public String toString() {
        return "Order [_id=" + _id + ", order_id=" + order_id + ", payment_id=" + payment_id + ", username=" + username
                + ", total=" + total + ", timestamp=" + timestamp + ", items=" + items + "]";
    }
    
}
