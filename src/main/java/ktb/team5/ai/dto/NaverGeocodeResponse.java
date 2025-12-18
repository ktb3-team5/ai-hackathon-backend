package ktb.team5.ai.dto;

import lombok.Getter;
import java.util.List;

@Getter
public class NaverGeocodeResponse {
    private List<Address> addresses;

    @Getter
    public static class Address {
        private String x; // longitude
        private String y; // latitude
    }
}

