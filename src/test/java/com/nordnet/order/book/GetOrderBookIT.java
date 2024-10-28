package nordnet.order.book;

import nordnet.order.book.entities.OrderBook;
import nordnet.order.book.model.Side;
import nordnet.order.book.repositories.OrderBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetOrderBookIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderBookRepository orderBookRepository;

    @BeforeEach
    public void clearDatabase() {
        orderBookRepository.deleteAll(); // Clear the database before each test
    }

    @Test
    public void testGetOrderBookById_Success() throws Exception {
        OrderBook orderBook = new OrderBook("SAVE", 10, 100, Side.BUY, "USD");
        OrderBook savedOrderBook = orderBookRepository.save(orderBook);

        mockMvc.perform(get("/order-book/" + savedOrderBook.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedOrderBook.getId().toString()))
                .andExpect(jsonPath("$.ticker").value("SAVE"))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.price").value(100))
                .andExpect(jsonPath("$.side").value(Side.BUY))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    public void testGetOrderBookById_NotFound() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/order-book/" + nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetOrderBookById_BadFormatId() throws Exception {
        int badFormatId = 123;

        mockMvc.perform(get("/order-book/" + badFormatId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
