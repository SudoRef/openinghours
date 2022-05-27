package my.project.openinghours;

public class TestUtils {
    public static final String OK_REQUEST = "{\"monday\": [],\"tuesday\": [{\"type\": \"open\",\"value\": 36000},{\"type\": \"close\",\"value\": 64800}],\"wednesday\": [],\"thursday\": [{\"type\": \"open\",\"value\": 37800},{\"type\": \"close\",\"value\": 64800}],\"friday\": [{\"type\": \"open\",\"value\": 36000}],\"saturday\": [{\"type\": \"close\",\"value\": 3600},{\"type\": \"open\",\"value\": 32400},{\"type\": \"close\",\"value\": 39600},{\"type\": \"open\",\"value\": 57600},{\"type\": \"close\",\"value\": 82800}],\"sunday\": [{\"type\": \"close\",\"value\": 3600},{\"type\": \"open\",\"value\": 43200},{\"type\": \"close\",\"value\": 75600}]}";
    public static final String OK_RESPONSE = "{\"Monday\":\"Closed\",\"Tuesday\":\"10:00 AM - 06:00 PM\",\"Wednesday\":\"Closed\",\"Thursday\":\"10:30 AM - 06:00 PM\",\"Friday\":\"10:00 AM - 01:00 AM\",\"Saturday\":\"09:00 AM - 11:00 AM, 04:00 PM - 11:00 PM\",\"Sunday\":\"12:00 PM - 09:00 PM\"}";
    public static final String BROKEN_REQUEST_NULL_VALUE = "{\"monday\": [{\"type\": \"open\",\"value\": null},{\"type\": \"close\",\"value\": 64800}]}";
    public static final String BROKEN_REQUEST_NEGATIVE_VALUE = "{\"monday\": [{\"type\": \"open\",\"value\": -36000},{\"type\": \"close\",\"value\": 64800}]}";
    public static final String BROKEN_REQUEST_TOO_BIG_VALUE = "{\"monday\": [{\"type\": \"open\",\"value\": 360000},{\"type\": \"close\",\"value\": 64800}]}";
    public static final String BROKEN_REQUEST_MIXED_UP_VALUE = "{\"monday\": [],\"tuesday\": [{\"type\": \"open\",\"value\": 64800},{\"type\": \"close\",\"value\": 36000}],\"wednesday\": [],\"thursday\": [{\"type\": \"open\",\"value\": 37800},{\"type\": \"close\",\"value\": 64800}],\"friday\": [{\"type\": \"open\",\"value\": 36000}],\"saturday\": [{\"type\": \"close\",\"value\": 3600},{\"type\": \"open\",\"value\": 32400},{\"type\": \"close\",\"value\": 39600},{\"type\": \"open\",\"value\": 57600},{\"type\": \"close\",\"value\": 82800}],\"sunday\": [{\"type\": \"close\",\"value\": 3600},{\"type\": \"open\",\"value\": 43200},{\"type\": \"close\",\"value\": 75600}]}";

}
