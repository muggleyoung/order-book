package nordnet.order.book;

import nordnet.order.book.services.OrderBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateOrderIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderBookService orderBookService;

    @Test
    public void testCreateOrderBook() throws Exception {
        String jsonRequest = """
                {
                    "ticker": "SAVE",
                    "quantity": 10,
                    "price": 100,
                    "side": "buy",
                    "currency": "USD"
                }
                """;

        mockMvc.perform(post("/order-book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.ticker").value("SAVE"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.side").value("buy"))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    public void testCreateOrderBook_InvalidFields() throws Exception {
        String jsonRequest = """
                {
                    "ticker": "",
                    "quantity": -5,
                    "price": 0,
                    "side": "",
                    "currency": ""
                }
                """;

        mockMvc.perform(post("/order-book")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(status().isBadRequest()); // Expecting a 400 Bad Request
    }
}
