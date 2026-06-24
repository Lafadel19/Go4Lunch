package go4lunch.data.model;

public class Address {

    public String city ;
    public String pedestrian;
    public String house_number;
    public String postcode;

        public String getAddress() {
            return house_number + " " + pedestrian + " " + postcode + " " + city;
        }

}
