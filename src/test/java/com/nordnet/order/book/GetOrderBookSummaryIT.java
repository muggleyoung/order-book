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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetOrderBookSummaryIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderBookRepository orderBookRepository;

    private final String today = LocalDate.now().toString();

    @BeforeEach
    public void clearDatabase() {
        orderBookRepository.deleteAll();
        // Create OrderBooks and save them to the database
        OrderBook orderBook1 = new OrderBook("SAVE", 1, 20, Side.BUY, "USD");
        orderBookRepository.save(orderBook1);
        OrderBook orderBook2 = new OrderBook("SAVE", 1, 10, Side.SELL, "USD");
        orderBookRepository.save(orderBook2);
        OrderBook orderBook3 = new OrderBook("SAVE", 1, 30, Side.BUY, "USD");
        orderBookRepository.save(orderBook3);
        OrderBook orderBook4 = new OrderBook("SAVE", 1, 20, Side.SELL, "USD");
        orderBookRepository.save(orderBook4);
        OrderBook orderBook5 = new OrderBook("GME", 10, 100, Side.SELL, "USD");
        orderBookRepository.save(orderBook5);
    }

    @Test
    public void testGetOrderBooksSummary_Success() throws Exception {
        mockMvc.perform(get("/order-book/summary?ticker=SAVE&side=buy&date="+ today)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("SAVE"))
                .andExpect(jsonPath("$.date").value(today))
                .andExpect(jsonPath("$.side").value(Side.BUY))
                .andExpect(jsonPath("$.orderCount").value(2))
                .andExpect(jsonPath("$.averagePrice").value(25.0))
                .andExpect(jsonPath("$.maxPrice").value(30.0))
                .andExpect(jsonPath("$.minPrice").value(20.0));
    }

    @Test
    public void testGetOrderBooksSummary_MissDateParam() throws Exception {
        mockMvc.perform(get("/order-book/summary?ticker=SAVE&side=buy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetOrderBooksSummary_MissTickerParam() throws Exception {
        mockMvc.perform(get("/order-book/summary?side=buy&date="+ today)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetOrderBooksSummary_MissSideParam() throws Exception {
        mockMvc.perform(get("/order-book/summary?ticker=SAVE&date="+ today)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
