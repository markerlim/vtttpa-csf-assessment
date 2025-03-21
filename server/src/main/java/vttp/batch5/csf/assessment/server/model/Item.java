package vttp.batch5.csf.assessment.server.model;

public class Item {
    private String id;
    private float price;
    private int quantity;
    public Item(){
        
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    
}
