package go4lunch.data.model;

import com.google.gson.annotations.SerializedName;

public class Address {

    public String city;
    public String road;
    public String pedestrian;
    @SerializedName("house_number")
    public String houseNumber;
    public String postcode;

    public String getAddress() {
        StringBuilder sb = new StringBuilder();
        if (houseNumber != null) sb.append(houseNumber).append(" ");
        if (road != null) {
            sb.append(road).append(", ");
        } else if (pedestrian != null) {
            sb.append(pedestrian).append(", ");
        }
        if (postcode != null) sb.append(postcode).append(" ");
        if (city != null) sb.append(city);

        String result = sb.toString().trim();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }
        return result.isEmpty() ? "Unknown Address" : result;
    }

}
