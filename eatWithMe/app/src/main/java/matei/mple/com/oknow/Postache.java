package matei.mple.com.oknow;

/**
 * Created by Matei on 11/30/2017.
 */

public class Postache {
    public String name, restaurantID;
    public int count;
    public Postache() {

    }

    public Postache(String name, int count, String restaurantID) {
        this.name=name;
        this.count=count;
        this.restaurantID=restaurantID;

    }
    public String getName(){return name;}
    public String getRestaurantID(){return restaurantID;}
    public int getCount(){ return count;}
    public void setName(String name) {this.name=name;}
    public void setCount(int count) {this.count=count;}
    public void setRestaurantID(String restaurantID){this.restaurantID=restaurantID;}

}
