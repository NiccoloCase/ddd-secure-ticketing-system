package org.swe;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestFakeServiceIT {

    private static FakeService myService;

    @BeforeAll
    public static void setUp() {
        myService = new FakeService();
    }

    @Test
    public void testGetDataFromDatabase() {
        String result = myService.getDataFromDatabase();
        System.out.println("Data from database: " + result);
        assertEquals("Data from database", result, "The data should be correctly retrieved from the database");
    }
}
